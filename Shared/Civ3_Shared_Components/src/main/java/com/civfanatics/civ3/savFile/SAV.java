
package com.civfanatics.civ3.savFile;

import com.civfanatics.civ3.biqFile.CITY;
import com.civfanatics.civ3.biqFile.IO;
import com.civfanatics.civ3.biqFile.util.LittleEndianDataInputStream;
import com.civfanatics.civ3.biqFile.util.LittleEndianDataOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Top-level module for a Save File.  The goal being to be able to read a SAV
 * and process info specific to it.  The old functionality of importing a SAV
 * to the editor purely extracting BIQ info from the SAV and ignored the rest.
 * 
 * I'm also thinking of writing this a bit differently.  Instead of a giant
 * 6000 line main file, hand off the import to each section as it goes, so they
 * handle importing their own stuff.  Should keep it more self-contained and
 * logically divided in theory.
 * @author Andrew
 */
public class SAV {
    
    private String currentCharset = "Windows-1252";//ISO-8859-1";
    
    String header;
    short magicShort;   //0x1A00
    int majorVersion;
    int minorVersion;
    byte[] random16Bytes = new byte[16];    //to store as-is
    String random16;
    
    EmbeddedRules embeddedRules = new EmbeddedRules();
    GameData gameData = new GameData();
    DATE[] dates = new DATE[3];
    PLGI[] plgis = new PLGI[2];
    CNSL cnsl = new CNSL();
    WRLD world = new WRLD();
    GameTILE[]tiles;
    CONT[]continents;
    private int[]goodCounts; //count of each GOOD on this CONT
    public SavLEAD[]players;
    SavUNIT[]savUnits;
    /**
     * There are a <b>lot</b> of "dummy" cities, usually of 12-16 bytes, in the
     * city section.  These include a couple CTPG sections, which have been
     * speculated to be about "city plauge".  But their details are unknown.
     * Keep them here, along with the real ones in the same order as the SAV,
     * so they can be written out to file if modified.
     */
    List<SavCITY> firaxisCities = new ArrayList<SavCITY>();
    /**
     * Actual cities.
     */
    List<SavCITY> realCities = new ArrayList<SavCITY>();
    byte[] zerosBeforePALV = new byte[256];
    List<PALV> palaceViews = new ArrayList<PALV>();
    
    public HIST histograph = new HIST();
    
    //For rule-patching
    int lengthOfRemainingBytes = 0;
    byte[] allTheThings;    //all the things we don't know about yet
    //End for rule patching
    
    public byte[] inputFour = new byte[4];
    public byte[] inputSixteen = new byte[16];
    public byte[] inputTwentyFour = new byte[24];
    public byte[] inputThirtyTwo = new byte[32];
    public byte[] inputForty = new byte[40];
    public byte[] inputFiftySeven;
    public byte[] inputSixtyFour;
    public byte[] inputOneHundredTwentyEight;
    public byte[] inputTwoFiftySix;
    public byte[] inputTwoSixty;
    public byte[] inputSixForty;
    public byte[] inputFiftyTwoHundred;
    
    Logger logger = Logger.getLogger(this.getClass());
    
    int NUM_PALV_PLAYERS = 32;  //always 32, even if there are actually fewer players
    
    //If > 1, uses multithreading for inputting the file.
    static int numProcs = 1;
    
    int dataInputted;
    
    public boolean inputSAV(File file) {
        return inputSAV(file, false);
    }
    
    public boolean inputSAV(File file, boolean alreadyDecompressed)
    {
        dataInputted = 0;              //amount of data read in
        try
        {
            LittleEndianDataInputStream inFile = new LittleEndianDataInputStream(new BufferedInputStream(new FileInputStream(file)));
            //the buffer will be able to contain up to 4 GB theoretically - the loss of precision is irrelevant
            if (logger.isDebugEnabled())
            {
                logger.debug("Size of file:     " + (int)file.length());
                long maxMem = Runtime.getRuntime().maxMemory();
                logger.debug("Max size of heap: " + maxMem);
            }
            byte[] buffer = new byte[(int) file.length()];
            inFile.readFully(buffer);
            inFile.close(); //We can close the file once we've read it into the buffer
            if (logger.isInfoEnabled())
                logger.info("Number of available processors: " + Runtime.getRuntime().availableProcessors() + "; using " + numProcs + " processors");
            //Create an array of LittleEndianDataInputStreams
            LittleEndianDataInputStream[]ins = new LittleEndianDataInputStream[numProcs];
            return inputSAV(ins, buffer, file, alreadyDecompressed);
        }
        catch(IOException ex) {
            logger.error("Error when reading from file into buffer", ex);
            return false;
        }
    }
            
    /**
     * Performs top-level SAV processing.  The structure for this is modeled after the inputBIQ
     * method in IO.java, however it hands off control per-section more often than IO.java, which
     * only does for multithreading.
     * Also note that at least initially, we aren't too fussed about multithreading...
     * we can always skip around with the inputters as desirable later (maybe even having one do one
     * section and another do another, if there aren't cross-dependencies).
     * @param ins
     * @param buffer
     * @param file
     * @return 
     */
    private boolean inputSAV(LittleEndianDataInputStream[] ins, byte[] buffer, File file, boolean alreadyDecompressed) {
        long start = System.nanoTime();
        long fileLength = file.length();
        try {
            for (int i = 0; i < ins.length; i++)
            {
                ins[i] = new LittleEndianDataInputStream(new ByteArrayInputStream(buffer));
            }
            ins[0].read(inputFour, 0, 4);
            dataInputted += 4;
            
            String temp = new String(inputFour, currentCharset);  //256-character ASCII
            //Check to see if the file is compressed or not
            if (!(temp.contains("CIV3")))    //compressed!
            {
                if (alreadyDecompressed) {
                    //Stack overflow waiting to happen
                    logger.error("Cannot read decompressed file " + file.getName());
                    return false;
                }
                
                logger.info("Detected compressed file");
                String[] decomQuery = {"java", "-jar", "./bin/BIQDecompressor.jar", file.getCanonicalPath(), "._tmp.sav"};
                Process dcp = Runtime.getRuntime().exec(decomQuery);
                //wait until it's decompressed before continuing or we'll blow up
                dcp.waitFor();
                return inputSAV(new File("._tmp.sav"), true);
            }
            header = temp;
            
            magicShort = ins[0].readShort();
            dataInputted+=2;
            
            majorVersion = ins[0].readInt();
            dataInputted+=4;
            if (majorVersion >= 17) {
                minorVersion = ins[0].readInt();
                if (minorVersion >= 7) {
                    ins[0].read(inputSixteen, 0, 16);
                    dataInputted+=16;
                    random16 = new String(inputSixteen, currentCharset);
                }
            }
            
            int bytesBeforeBICs = dataInputted;
            //Get read for the embedded BICs
            for (int i = 1; i < ins.length; i++) {
                ins[i].skipBytes(dataInputted);
            }
            
            embeddedRules.readEmbeddedRules(ins[0]);
            
            ins[0].read(inputFour, 0, 4);
            String sectionHeader = new String(inputFour, currentCharset);
            if (sectionHeader.equals("GAME")) {
                try {
                    gameData.readGameDataSection(ins[0], majorVersion, minorVersion, embeddedRules.embeddedRules);
                }
                catch(IOException ex) {
                    logger.error("IOException during GAME", ex);
                }
            }
            else {
                logger.error("GAME header is missing; instead read " + sectionHeader);
            }
            
            ins[0].read(inputFour, 0, 4);
            sectionHeader = new String(inputFour, currentCharset);
            if (sectionHeader.equals("DATE")) {
                try {
                    dates[0] = new DATE();
                    dates[0].readDataSection(ins[0]);
                }
                catch(IOException ex) {
                    logger.error("IOException during DATE", ex);
                    logger.error("Bytes read: " + ins[0].numBytesRead);
                }
            }
            else {
                logger.error("DATE header is missing; instead read " + sectionHeader);
            }
            
            ins[0].read(inputFour, 0, 4);
            sectionHeader = new String(inputFour, currentCharset);
            if (sectionHeader.equals("PLGI")) {
                try {
                    plgis[0] = new PLGI();
                    plgis[0].readDataSection(ins[0]);
                }
                catch(IOException ex) {
                    logger.error("IOException during PLGI", ex);
                }
            }
            else {
                logger.error("PLGI header is missing; instead read " + sectionHeader);
            }
            
            ins[0].read(inputFour, 0, 4);
            sectionHeader = new String(inputFour, currentCharset);
            if (sectionHeader.equals("PLGI")) {
                try {
                    plgis[1] = new PLGI();
                    plgis[1].readDataSection(ins[0]);
                }
                catch(IOException ex) {
                    logger.error("IOException during PLGI", ex);
                }
            }
            else {
                logger.error("PLGI header is missing; instead read " + sectionHeader);
            }
            
            ins[0].read(inputFour, 0, 4);
            sectionHeader = new String(inputFour, currentCharset);
            if (sectionHeader.equals("DATE")) {
                try {
                    dates[1] = new DATE();
                    dates[1].readDataSection(ins[0]);
                }
                catch(IOException ex) {
                    logger.error("IOException during DATE", ex);
                }
            }
            else {
                logger.error("DATE header is missing; instead read " + sectionHeader);
            }
            
            ins[0].read(inputFour, 0, 4);
            sectionHeader = new String(inputFour, currentCharset);
            if (sectionHeader.equals("DATE")) {
                try {
                    dates[2] = new DATE();
                    dates[2].readDataSection(ins[0]);
                }
                catch(IOException ex) {
                    logger.error("IOException during DATE", ex);
                }
            }
            else {
                logger.error("DATE header is missing; instead read " + sectionHeader);
            }
            
            ins[0].read(inputFour, 0, 4);
            sectionHeader = new String(inputFour, currentCharset);
            
            //The last DATE can have an extra 8 bytes that are not reflected in the
            //length.  Account for that here.
            int maxSkipCount = 2;
            while (maxSkipCount > 0 && !sectionHeader.equals("CNSL")) {
                ins[0].read(inputFour, 0, 4);
                sectionHeader = new String(inputFour, currentCharset);
                maxSkipCount--;
            }
            
            if (sectionHeader.equals("CNSL")) {
                try {
                    cnsl.readDataSection(ins[0]);
                }
                catch(IOException ex) {
                    logger.error("IOException during DATE", ex);
                }
            }
            else {
                logger.error("CNSL header is missing; instead read " + sectionHeader);
            }
            
            
            ins[0].read(inputFour, 0, 4);
            sectionHeader = new String(inputFour, currentCharset);
            if (sectionHeader.equals("WRLD")) {
                try {
                    world.readDataSection(ins[0]);
                }
                catch(IOException ex) {
                    logger.error("IOException during CNSL", ex);
                }
            }
            else {
                logger.error("WRLD header is missing; instead read " + sectionHeader);
            }
            
            ins[0].mark(5);
            ins[0].read(inputFour, 0, 4);
            ins[0].reset();
            sectionHeader = new String(inputFour, currentCharset);
            if (sectionHeader.equals("TILE")) {
                int numTiles = 0;
                if (hasTiles(embeddedRules)) {
                    //Use number from BIQ.  N.B. This will change if we later allow map expansion
                    numTiles = embeddedRules.embeddedRules.tile.size();
                }
                else {
                    numTiles = world.getWidth() * world.getHeight() / 2;
                }
                int i = 0;  //stored outside try/catch so I can log it if something goes wrong
                tiles = new GameTILE[numTiles];
                try {
                    for (; i < numTiles; i++) {
                        tiles[i] = new GameTILE();
                        tiles[i].readDataSection(ins[0]);
                    }
                }
                catch(IOException ex) {
                    logger.error("IOException during GameTILE; i = " + i, ex);
                }
            }
            else {
                logger.error("TILE header is missing; instead read " + sectionHeader);
            }
            
            ins[0].mark(5);
            ins[0].read(inputFour, 0, 4);
            ins[0].reset();
            sectionHeader = new String(inputFour, currentCharset);
            if (sectionHeader.equals("CONT")) {
                int numConts = gameData.getNumConts();
                continents = new CONT[numConts];
                int i = 0;  //stored outside try/catch so I can log it if something goes wrong
                try {
                    for (; i < numConts; i++) {
                        continents[i] = new CONT();
                        continents[i].readDataSection(ins[0], embeddedRules.embeddedRules.resource.size());
                    }
                }
                catch(IOException ex) {
                    logger.error("IOException during CONT; i = " + i, ex);
                }
            }
            else {
                logger.error("CONT header is missing; instead read " + sectionHeader);
            }
            
            goodCounts = new int[embeddedRules.embeddedRules.resource.size()];
            for (int i = 0; i < embeddedRules.embeddedRules.resource.size(); i++) {
                goodCounts[i] = ins[0].readInt();
            }
            
            //C3C 1.20
            ins[0].mark(5);
            ins[0].read(inputFour, 0, 4);
            ins[0].reset();
            sectionHeader = new String(inputFour, currentCharset);
            if (sectionHeader.equals("LEAD")) {
                
                //ins[0].skipBytes(0x2B23); //Skipping to second one.  Quint 350.
                //ins[0].skipBytes(0x2AC7);   //Autsave 270 BC
                ins[0].mark(5);
                ins[0].read(inputFour, 0, 4);
                ins[0].reset();
                sectionHeader = new String(inputFour, currentCharset);
                
                
                players = new SavLEAD[32];
                int i = 0;  //stored outside try/catch so I can log it if something goes wrong
                int numBuildings = embeddedRules.embeddedRules.buildings.size();
                int numGoods = embeddedRules.embeddedRules.resource.size();
                int numSSParts = embeddedRules.embeddedRules.rule.get(0).getNumSpaceshipParts();
                int numPrtos = embeddedRules.embeddedRules.getNumFiraxisUnits();
                try {
                    for (; i < 32; i++) {
                        logger.info("Processing player " + i);
                        players[i] = new SavLEAD();
                        players[i].readDataSection(ins[0], numPrtos, numBuildings, numSSParts, numGoods, gameData.getNumPlayers());
                    }
                }
                catch(IOException ex) {
                    logger.error("IOException during LEAD; i = " + i, ex);
                }
            }
            else {
                logger.error("LEAD header is missing; instead read " + sectionHeader);
            }
            
            ins[0].mark(5);
            ins[0].read(inputFour, 0, 4);
            ins[0].reset();
            sectionHeader = new String(inputFour, currentCharset);
            
            //Should be UNIT at this point.
            if ("UNIT".equals(sectionHeader)) {
                int numUnits = gameData.getNumberOfUnits();
                savUnits = new SavUNIT[numUnits];
                int i = 0;  //stored outside try/catch so I can log it if something goes wrong
                try {
                    for (; i < numUnits; i++) {
                        savUnits[i] = new SavUNIT(this);
                        savUnits[i].readDataSection(ins[0]);
                        if (logger.isDebugEnabled()) {
                            logger.debug("New unit: " + savUnits[i]);
                            logger.debug("Bytes read: " + ins[0].numBytesRead);
                        }
                    }
                }
                catch(IOException ex) {
                    logger.error("IOException during UNIT; i = " + i, ex);
                }
            }
            else {
                logger.error("UNIT header is missing; instead read " + sectionHeader);
            }
            
            ins[0].mark(5);
            ins[0].read(inputFour, 0, 4);
            ins[0].reset();
            sectionHeader = new String(inputFour, currentCharset);
            logger.info("Bytes read: " + ins[0].numBytesRead);
            
            if ("CITY".equals(sectionHeader)) {
                int i = 0;  //stored outside try/catch so I can log it if something goes wrong
                try {
                    for (; ; i++) { //due to dummy cities, we don't know how many cities there are
                        SavCITY newCity = new SavCITY(this);
                        newCity.readDataSection(ins[0]);
                        firaxisCities.add(newCity);
                        if (newCity.isRealCity()) {
                            realCities.add(newCity);
                        }
                    }
                }
                catch (EndOfCITYException ex) {
                    logger.info("Reached end of SAV CITY section successfully; have " + firaxisCities.size() + " Firaxis cities and " + realCities.size() + " real cities");
                }
                catch (HeaderException ex) {
                    logger.error("Header exception during CITY; i = " + i + ".  # of real cities = " + realCities.size(), ex);
                }
                catch (IOException ex) {
                    logger.error("IOException during CITY; i = " + i + ".  # of real cities = " + realCities.size(), ex);
                }
            }
            else {
                logger.error("CITY header is missing; instead read " + sectionHeader);
                return false;
            }
            
            ins[0].mark(5);
            ins[0].read(inputFour, 0, 4);
            ins[0].reset();
            sectionHeader = new String(inputFour, currentCharset);
            logger.info("Bytes read: " + ins[0].numBytesRead);
            
            if ("CLNY".equals(sectionHeader)) {
                for (int i = 0; i < gameData.getNumberOfColonies(); i++) {
                    SavCLNY colony = new SavCLNY();
                    colony.readDataSection(ins[0]);
                }
            }
            
            ins[0].read(zerosBeforePALV, 0, 256);
            boolean allZero = true;
            for (int i = 0; i < 256; i++) {
                if (zerosBeforePALV[i] != 0) {
                    allZero = false;
                }
            }
            if (!allZero) {
                logger.warn("Non-zero values present before PALV");
            }
            
            ins[0].mark(5);
            ins[0].read(inputFour, 0, 4);
            ins[0].reset();
            sectionHeader = new String(inputFour, currentCharset);
            logger.info("Bytes read: " + ins[0].numBytesRead);
            
            if ("PALV".equals(sectionHeader)) {
                int i = 0;  //stored outside try/catch so I can log it if something goes wrong
                try {
                    for (; i < NUM_PALV_PLAYERS; i++) {
                        PALV newPALV = new PALV(this);
                        newPALV.readDataSection(ins[0]);
                        palaceViews.add(newPALV);
                    }
                }
                catch(IOException ex) {
                    logger.error("IOException during PALV; i = " + i, ex);
                }
            }
            else {
                logger.error("PALV header is missing; instead read " + sectionHeader);
                return false;
            }
            
            
            ins[0].mark(5);
            ins[0].read(inputFour, 0, 4);
            ins[0].reset();
            sectionHeader = new String(inputFour, currentCharset);
            logger.info("Bytes read: " + ins[0].numBytesRead);
            
            if ("HIST".equals(sectionHeader)) {
                try {
                    histograph.readDataSection(ins[0]);
                }
                catch(IOException ex) {
                    logger.error("IOException during HIST", ex);
                    return false;
                }
            }
            else {
                logger.error("HIST header is missing; instead read " + sectionHeader);
                return false;
            }
            
            logger.info("Reached current end of processing");
        }
        catch(IOException ex) {
            logger.error("IOException during input", ex);
            return false;
        }
        catch(InterruptedException ex) {
            logger.error("File decompression interrupted", ex);
            return false;
        }
        return true;
    }
    
    
    public boolean readSAVThroughEmbeddedRules(File file, boolean alreadyDecompressed)
    {
        dataInputted = 0;              //amount of data read in
        try
        {
            LittleEndianDataInputStream inFile = new LittleEndianDataInputStream(new BufferedInputStream(new FileInputStream(file)));
            //the buffer will be able to contain up to 4 GB theoretically - the loss of precision is irrelevant
            if (logger.isDebugEnabled())
            {
                logger.debug("Size of file:     " + (int)file.length());
                long maxMem = Runtime.getRuntime().maxMemory();
                logger.debug("Max size of heap: " + maxMem);
            }
            byte[] buffer = new byte[(int) file.length()];
            inFile.readFully(buffer);
            inFile.close(); //We can close the file once we've read it into the buffer
            if (logger.isInfoEnabled())
                logger.info("Number of available processors: " + Runtime.getRuntime().availableProcessors() + "; using " + numProcs + " processors");
            //Create an array of LittleEndianDataInputStreams
            LittleEndianDataInputStream[]ins = new LittleEndianDataInputStream[numProcs];
            return readSAVThroughEmbeddedRules(ins, buffer, file, alreadyDecompressed);
        }
        catch(IOException ex) {
            logger.error("Error when reading from file into buffer", ex);
            return false;
        }
    }
    
    private boolean readSAVThroughEmbeddedRules(LittleEndianDataInputStream[] ins, byte[] buffer, File file, boolean alreadyDecompressed) {
        long fileLength = file.length();
        try {
            for (int i = 0; i < ins.length; i++)
            {
                ins[i] = new LittleEndianDataInputStream(new ByteArrayInputStream(buffer));
            }
            ins[0].read(inputFour, 0, 4);
            dataInputted += 4;
            
            String temp = new String(inputFour, currentCharset);  //256-character ASCII
            //Check to see if the file is compressed or not
            if (!(temp.contains("CIV3")))    //compressed!
            {
                if (alreadyDecompressed) {
                    //Stack overflow waiting to happen
                    logger.error("Cannot read decompressed file " + file.getName());
                    return false;
                }
                
                logger.info("Detected compressed file");
                String[] decomQuery = {"java", "-jar", "./bin/BIQDecompressor.jar", file.getCanonicalPath(), "._tmp.sav"};
                Process dcp = Runtime.getRuntime().exec(decomQuery);
                //wait until it's decompressed before continuing or we'll blow up
                dcp.waitFor();
                return readSAVThroughEmbeddedRules(new File("._tmp.sav"), true);
            }
            header = temp;
            
            magicShort = ins[0].readShort();
            dataInputted+=2;
            
            majorVersion = ins[0].readInt();
            dataInputted+=4;
            if (majorVersion >= SavVERSION.SAV_VANILLA_121) {
                minorVersion = ins[0].readInt();
                if (minorVersion >= 7) {
                    ins[0].read(random16Bytes, 0, 16);
                    dataInputted+=16;
                }
            }
            
            int bytesBeforeBICs = dataInputted;
            //Get read for the embedded BICs
            for (int i = 1; i < ins.length; i++) {
                ins[i].skipBytes(dataInputted);
            }
            
            embeddedRules.readEmbeddedRules(ins[0]);
            
            //Read all the things!
            try {
                allTheThings = new byte[buffer.length];
                lengthOfRemainingBytes = ins[0].read(allTheThings);
            }
            catch(EOFException eof) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Reached EOF when scanning SAV.  This is expected");
                }
            }
            catch(IOException ex) {
                logger.error("Unexpected IOException when reading SAV");
            }
            ins[0].close();
            return true;
        }
        catch(IOException ex) {
            logger.error("IOException during input", ex);
            return false;
        }
        catch(InterruptedException ex) {
            logger.error("File decompression interrupted", ex);
            return false;
        }
    }

    public EmbeddedRules getEmbeddedRules() {
        return embeddedRules;
    }
    
    public boolean hasTiles(EmbeddedRules embeddedRules) {
        return embeddedRules.embeddedRules.tile != null && embeddedRules.embeddedRules.tile.size() > 0;
    }
    
    public void writeRulePatchedSAV(File file) throws IOException {
        
        try {
            LittleEndianDataOutputStream out = new LittleEndianDataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            out.writeBytes("CIV3");
            out.writeShort(this.magicShort);
            out.writeInt(majorVersion);
            out.writeInt(minorVersion);
            out.write(random16Bytes);
            
            //Embedded rules
            this.embeddedRules.outputEmbeddedRules(out);
            
            //Write the first 117 bytes of GAME, then 1 for "sav edited" (MegaTrainerXL case), then the remainder
            out.write(allTheThings, 0, 117);
            out.writeByte(1);
            out.write(allTheThings, 118, lengthOfRemainingBytes - 118);
            out.close();
        }
        catch(FileNotFoundException ex) {
            logger.error("File not found for " + file.getPath(), ex);
        }
        catch(IOException ex) {
            logger.error("IOException for " + file.getPath(), ex);
            throw ex;
        }
    }

    public GameData getGameData() {
        return gameData;
    }
    
    
    public void applyMagic() {
        try {
            IO original = new IO();
            original.inputBIQ(new File("D:\\Civilization III\\Conquests\\Scenarios\\Random Uncompressed.biq"));
            
            original.city.clear();
            int cityIndex = 0;
            for (SavCITY city : this.realCities) {
                CITY biqEquivalent = new CITY(original);
                biqEquivalent.setName(city.getName());  //todo: C-style trim
                biqEquivalent.setX(city.getX());
                biqEquivalent.setY(city.getY());
                biqEquivalent.setCulture(city.getCulturePoints());
                biqEquivalent.setOwner(city.getOwner() - 1);
                biqEquivalent.setOwnerType(CITY.OWNER_PLAYER);
                biqEquivalent.setBorderLevel(city.getBorderLevel());
                biqEquivalent.setSize(city.getCitizens().size());
                
                List<Integer> buildingIndices = city.getBuildingsPresent();
                for (Integer index : buildingIndices) {
                    biqEquivalent.addBuilding(index);
                    if (index == 0) {
                        biqEquivalent.setHasPalace((byte)1);
                    }
                }
                
                int tileIndex = original.calculateTileIndex(city.getX(), city.getY());
                original.tile.get(tileIndex).setCity((short)cityIndex);
                
                original.city.add(biqEquivalent);
                cityIndex++;
            }
            original.outputBIQ(new File("C:\\temp\\magic.biq"));
        }
        catch(Exception ex) {
            System.err.println(Arrays.toString(ex.getStackTrace()));
        }
    }
}
