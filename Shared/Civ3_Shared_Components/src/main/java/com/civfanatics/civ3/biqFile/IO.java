package com.civfanatics.civ3.biqFile;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * BUG: TODO: AI Off/Def Unit # Swapped
 * @author Quintillus
 */

import com.civfanatics.civ3.biqFile.util.DefaultRulesLoader;
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;
import org.apache.log4j.*;
import com.civfanatics.civ3.biqFile.util.LittleEndianDataInputStream;
import com.civfanatics.civ3.biqFile.util.LittleEndianDataOutputStream;
import com.civfanatics.civ3.pediaIcons.PediaIconsFile;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.util.Map.Entry;
import scriptFile.ScriptFile;

public class IO
{
    //ISO-8859-1 is what the editor originally used.  In August, 2013, it was discovered Windows-1252 is the actual version supported, and that was switched to.
    //Windows-1251 supports Russian Civ3
    //GB18030 supports Chinese Civ3, is a superset of the GBK that was likely what Civ3 originally supported.  Not sure if GBK would be better
    //still need some work for Chinese
    private String currentCharset = "Windows-1252";//ISO-8859-1";
    
    /**
     * exportEnglish will be set by the Main GUI.  It will be referenced by all
     * the BIQ sections when they are told to export comparisons (or simply
     * their data, minus comparisons) to file.
     */
    private static int numProcs = 1;
    public boolean exportEnglish = true;
    public static final int TILE_LENGTH = 0x2D + 4;
    public static final int PTW_TILE_LENGTH = 33;
    public static final int VANILLA_3_4_TILE_LENGTH = 0x17 + 4;
    public static final int VANILLA_2_TILE_LENGTH = 0x16 + 4;
    public static final int CONT_LENGTH = 12;
    public static final int SLOC_LENGTH = 20;
    private boolean hasCustomRules;
    private boolean hasCustomMap;
    private boolean hasCustomPlayerData;
    Logger logger = Logger.getLogger(this.getClass());
    public byte[] inputFour;
    public byte[] inputTwentyFour;
    public byte[] inputThirtyTwo;
    public byte[] inputForty;
    public byte[] inputFiftySeven;
    public byte[] inputSixtyFour;
    public byte[] inputOneHundredTwentyEight;
    public byte[] inputTwoFiftySix;
    public byte[] inputTwoSixty;
    public byte[] inputSixForty;
    public byte[] inputFiftyTwoHundred;
    public Byte inputByte;
    public Short inputShort;
    public String fileName;
    String verString;
    public civ3Version version;
    //VER# Section
    public int numHeaders = 1;
    public int majorVersionNumber = 12;
    public int minorVersionNumber = 8;
    public String description = "";
    public String title = "";
    //NOT COMPLETED
    //BLDG Section
    public int numBuildings;
    //need to be able to add buildings, so using an List
    public List<BLDG> buildings = new ArrayList<BLDG>();
    //CTZN Section
    public int numCitizens;
    public List<CTZN> citizens = new ArrayList<CTZN>();
    //CULT Section
    public int numCulturalOpinions;
    public List<CULT> culture = new ArrayList<CULT>();
    //DIFF Section
    public int numDifficulties;
    public List<DIFF> difficulties = new ArrayList<DIFF>();
    //ERAS Section
    public int numEras;
    public List<ERAS> eras = new ArrayList<ERAS>();
    //ESPN Section
    public int numEspionage;
    public List<ESPN> espionage = new ArrayList<ESPN>();
    //EXPR Section
    public int numExprLevel;
    public List<EXPR> experience = new ArrayList<EXPR>();
    //GOOD Section
    public int numGoods;
    public List<GOOD> resource = new ArrayList<GOOD>();
    //GOVT Section
    public int numGovernments;
    public List<GOVT> government = new ArrayList<GOVT>();
    //RULE (General Settings) Section
    public int numRules;
    public List<RULE> rule = new ArrayList<RULE>();
    //PRTO Section
    public int numUnits;
    public List<PRTO> unit = new ArrayList<PRTO>();
    //TECH Section
    public int numTechnologies;
    public List<TECH> technology = new ArrayList<TECH>();
    //TRFM Section
    public int numWorkerJobs;
    public List<TRFM> workerJob = new ArrayList<TRFM>();
    //TERR Section
    public int numTerrains;
    public List<TERR> terrain = new ArrayList<TERR>();
    //WSIZ Section
    public int numWorldSizes;
    public List<WSIZ> worldSize = new ArrayList<WSIZ>();
    //FLAV Section
    public int numFlavors;
    public List<FLAV> flavor = new ArrayList<FLAV>();
    //GAME Section
    public int numScenarioProperties;
    public List<GAME> scenarioProperty = new ArrayList<GAME>();
    //WCHR Section
    public int numWorldCharacteristics;
    public List<WCHR> worldCharacteristic = new ArrayList<WCHR>();
    //WMAP Section
    public int numWorldMaps;
    public List<WMAP> worldMap = new ArrayList<WMAP>();
    //TILE Section
    public int numTiles;
    public List<TILE> tile = new ArrayList<TILE>();
    //CONT Section
    public int numContinents;
    public List<CONT> continent = new ArrayList<CONT>();
    //SLOC Section
    public int numStartingLocations;
    public List<SLOC> startingLocation = new ArrayList<SLOC>();
    //CITY Section
    public int numCities;
    public List<CITY> city = new ArrayList<CITY>();
    //UNIT Section
    public int numMapUnits;
    public List<UNIT> mapUnit = new ArrayList<UNIT>();
    //CLNY Section
    public int numColonies;
    public List<CLNY> colony = new ArrayList<CLNY>();
    //LEAD Section;
    public int numPlayers;
    public List<LEAD> player = new ArrayList<LEAD>();
    //RACE Section
    public int numCivilizations;
    public List<RACE> civilization = new ArrayList<RACE>();
    public int dataInputted;
    
    //conversion variables
    public int convertToConquests = 0;
    /**
     * Controls whether we add to data lengths if inputting pre-Conquests info.
     * Once we support saving to BIC/BIX, we will have to.
     * For now, many sections have a static length, always set to the Conquests
     * value, and thus will NOT have this converted.  This variable controls that,
     * and makes it easy to find those sections later for refactoring.
     */
    final static boolean convertDataLengthsForConquests = false;
    
    final static Map<String, String>languageToCharsetMap;
    
    public static String pathToBin = "";    //to override by user program if needed
    
    /**
     * Contains Pedia Icons, from PediaIcons.txt.
     * Not automatically imported as it's not critical for many operations, but
     * your program may import and attach it as well.
     */
    PediaIconsFile pediaIcons = new PediaIconsFile();
    
    /**
     * The "script.txt" file from the Text folder.
     **/
    ScriptFile scriptFile = new ScriptFile();
    
    //static initializer
    static {
        languageToCharsetMap = new HashMap<String, String>(4, 1.0f);
        languageToCharsetMap.put("English", "Windows-1252");
        languageToCharsetMap.put("Russian", "Windows-1251");
        languageToCharsetMap.put("Chinese", "GBK");
    }

    public IO()
    {
        //logger.setLevel(Level.INFO);
        hasCustomRules = false;
        hasCustomMap = false;
        hasCustomPlayerData = false;

        inputFour = new byte[4];
        inputTwentyFour = new byte[24];
        inputThirtyTwo = new byte[32];
        inputForty = new byte[40];
        inputFiftySeven = new byte[57];
        inputSixtyFour = new byte[64];
        inputOneHundredTwentyEight = new byte[128];
        inputTwoFiftySix = new byte[256];
        inputTwoSixty = new byte[260];
        inputSixForty = new byte[640];
        inputFiftyTwoHundred = new byte[5200];
    }

    public void resetLogConfig()
    {
        BasicConfigurator.resetConfiguration();
        BasicConfigurator.configure();
    }

    public static void setNumProcs(int processors)
    {
        numProcs = processors;
    }
    
    public boolean inputBIQ(File file) throws FileNotFoundException {
        return inputBIQ(file, false);
    }

    public boolean inputBIQ(File file, boolean fromSAV) throws FileNotFoundException
    {
        dataInputted = 0;              //amount of data read in
        try
        {
            //Note that BIQ files are little-endian, and Java is big-endian.
            //This necessitates reversing the bytes every time.
            //Little-endian: least significant byte first
            //Big-endian: most significant byte first
            //Sources: http://www.cs.umd.edu/class/sum2003/cmsc311/Notes/Data/endian.html
            //http://www.netrino.com/node/149
            //Wikipedia's examples are misleading at best; I recommend not reading them.
            //A less messy solution may be to use the SwappedDataInputStream
            //class from the Commons IO package (http://commons.apache.org/io/)
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
            return inputBIQ(ins, buffer, file, fromSAV);
        }
        catch(IOException ex) {
            if (ex instanceof FileNotFoundException) {
                throw (FileNotFoundException)ex;
            }
            logger.error("Error when reading from file into buffer", ex);
            return false;
        }
    }
            
    public boolean inputBIQ(LittleEndianDataInputStream[] ins, byte[] buffer, File file, boolean fromSAV) {
        long start = System.nanoTime();
        //TODO: Better handle file length within embedded BIC
        long fileLength = file == null ? 42 : file.length();
        try {
            if (buffer != null) {
                for (int i = 0; i < ins.length; i++)
                {
                    ins[i] = new LittleEndianDataInputStream(new ByteArrayInputStream(buffer));
                }
            }
            ins[0].read(inputFour, 0, 4);
            dataInputted += 4;
            String temp = new String(inputFour, currentCharset);  //256-character ASCII
            //Check to see if the file is compressed or not
            if (!(temp.contains("BIC")))    //compressed!
            {
                logger.info("Detected compressed file");
                logger.info("pathToBin: " + pathToBin);
                logger.info("Path to decompressor: " + pathToBin + "./bin/BIQDecompressor.jar");
                logger.info("Path to temp file: " + pathToBin + "._tmp.biq");
                String[] decomQuery = {"java", "-jar", pathToBin + "./bin/BIQDecompressor.jar", file.getCanonicalPath(), pathToBin + "._tmp.biq"};
                Process dcp = Runtime.getRuntime().exec(decomQuery);
                //wait until it's decompressed before continuing or we'll blow up
                dcp.waitFor();
                return inputBIQ(new File(pathToBin + "._tmp.biq"), fromSAV);
            }
            verString = temp;
            //delete any temporary
            //logger.info(temp);

            ins[0].read(inputFour, 0, 4);
            dataInputted += 4;
            temp = new String(inputFour, currentCharset); //256-character ASCII
            //logger.info(temp);

            //number of headers (should be 1)
            Integer integer = ins[0].readInt();
            dataInputted += 4;
            //logger.info(integer);

            //FOR EACH HEADER - not sure what happens with more than one
            //length of header (should be 720)
            integer = ins[0].readInt();
            dataInputted += 4;
            //logger.info(integer);

            //zero
            integer = ins[0].readInt();
            dataInputted += 4;
            //logger.info(integer);

            //zero
            integer = ins[0].readInt();
            dataInputted += 4;
            //logger.info(integer);

            //BIC major version number
            integer = ins[0].readInt();
            dataInputted += 4;
            majorVersionNumber=integer;


            //logger.info(integer);

            //BIC minor version number
            integer = ins[0].readInt();
            dataInputted += 4;
            minorVersionNumber=integer;
            //logger.info(integer);

            if (verString.equals("BICX") && majorVersionNumber == 12)
                version = civ3Version.CONQUESTS;
            else if (verString.equals("BICX"))
                version = civ3Version.PTW;
            else if (verString.equals("BIC "))
                version = civ3Version.VANILLA;
            else if (verString.equals("BICQ") && majorVersionNumber == 12)  //embedded in SAV
                version = civ3Version.CONQUESTS;
            else
                logger.error("Invalid BIC version");
            if (version != civ3Version.CONQUESTS)
                convertToConquests = 1;
            
            logger.info("major ver: " + majorVersionNumber + ", minorVer: " + minorVersionNumber);

            //next comes a String of length *up to* 640 characters.  Need to be alert for a 0x00 that ends it
            //it appears that all 640 characters are present in the .BIQ anyway for some reason
            ins[0].read(inputSixForty, 0, 640);
            dataInputted += 640;
            description = new String(inputSixForty, currentCharset); //256-character ASCII
            //logger.info(temp);
            //logger.info("title:");
            //title
            ins[0].read(inputSixtyFour, 0, 64);
            dataInputted += 64;
            title = new String(inputSixtyFour, currentCharset); //256-character ASCII
            //logger.info(temp);

            //BLDG
            ins[0].read(inputFour, 0, 4);
            dataInputted += 4;
            temp = new String(inputFour, currentCharset);
            if (temp.equals("BLDG"))
            {
                hasCustomRules = true;
                //logger.info(temp);
                //logger.info(temp);
                //number of buildings
                //input building, then ctzn, etc.]
                long now = System.nanoTime();
                long upToNow = now - start;
                if (logger.isInfoEnabled())
                    logger.info("About to go into the various input processors; has been " + upToNow/1000000 + " milliseconds.");
                inputBLDG(ins[0]);
                logger.debug("BLDG Bytes read: " + dataInputted + "; input bytes read = " + ins[0].numBytesRead);
                inputCTZN(ins[0]);
                logger.debug("CTZN Bytes read: " + dataInputted + "; input bytes read = " + ins[0].numBytesRead);
                inputCULT(ins[0]);
                logger.debug("CULT Bytes read: " + dataInputted + "; input bytes read = " + ins[0].numBytesRead);
                inputDIFF(ins[0]);
                logger.debug("DIFF Bytes read: " + dataInputted + "; input bytes read = " + ins[0].numBytesRead);
                inputERAS(ins[0]);
                logger.debug("ERAS Bytes read: " + dataInputted + "; input bytes read = " + ins[0].numBytesRead);
                inputESPN(ins[0]);
                logger.debug("ESPN Bytes read: " + dataInputted + "; input bytes read = " + ins[0].numBytesRead);
                inputEXPR(ins[0]);
                logger.debug("EXPR Bytes read: " + dataInputted + "; input bytes read = " + ins[0].numBytesRead);
                if (fromSAV) {
                    //FLAV does not ALWAYS follow EXPR when embedded
                    ins[0].mark(4);
                    ins[0].read(inputFour, 0, 4);
                    dataInputted += 4;
                    String temp2 = new String(inputFour, currentCharset);
                    if (temp2.equals("FLAV"))  {
                        ins[0].reset();
                        inputFLAV(ins[0]);
                    }
                    else {
                        ins[0].reset();
                    }
                }
                inputGOOD(ins[0]);
                logger.debug("GOOD Bytes read: " + dataInputted + "; input bytes read = " + ins[0].numBytesRead);
                inputGOVT(ins[0]);
                logger.debug("GOVT Bytes read: " + dataInputted + "; input bytes read = " + ins[0].numBytesRead);
                inputRULE(ins[0]);
                logger.debug("RULE Bytes read: " + dataInputted + "; input bytes read = " + ins[0].numBytesRead);
                inputPRTO(ins[0]);
                logger.debug("PRTO Bytes read: " + dataInputted + "; input bytes read = " + ins[0].numBytesRead);
                inputRACE(ins[0]);
                logger.debug("RACE Bytes read: " + dataInputted + "; input bytes read = " + ins[0].numBytesRead);
                inputTECH(ins[0]);
                logger.debug("TECH Bytes read: " + dataInputted + "; input bytes read = " + ins[0].numBytesRead);
                inputTFRM(ins[0]);
                logger.debug("TFRM Bytes read: " + dataInputted + "; input bytes read = " + ins[0].numBytesRead);
                if (convertToConquests == 1)
                {
                    TRFM airfield = new TRFM(this);
                    airfield.setName("Airfield");
                    airfield.setOrder("Build Airfield");
                    airfield.setCivilopediaEntry("TFRM_Airfield");
                    airfield.setRequiredAdvance(-1);
                    airfield.setTurnsToComplete(1);
                    airfield.setRequiredResource1(-1);
                    airfield.setRequiredResource2(-1);
                    this.workerJob.add(airfield);
                    
                    TRFM radarTower = new TRFM(this);
                    radarTower.setName("Radar Tower");
                    radarTower.setOrder("Build Radar Tower");
                    radarTower.setCivilopediaEntry("TFRM_Radar_Tower");
                    radarTower.setRequiredAdvance(-1);
                    radarTower.setTurnsToComplete(1);
                    radarTower.setRequiredResource1(-1);
                    radarTower.setRequiredResource2(-1);
                    this.workerJob.add(radarTower);
                    
                    TRFM outpost = new TRFM(this);
                    outpost.setName("Outpost");
                    outpost.setOrder("Build Outpost");
                    outpost.setCivilopediaEntry("TFRM_Outpost");
                    outpost.setRequiredAdvance(-1);
                    outpost.setTurnsToComplete(1);
                    outpost.setRequiredResource1(-1);
                    outpost.setRequiredResource2(-1);
                    this.workerJob.add(outpost);
                    
                    TRFM barricade = new TRFM(this);
                    barricade.setName("Barricade");
                    barricade.setOrder("Build Barricade");
                    barricade.setCivilopediaEntry("TFRM_Barricades");
                    barricade.setRequiredAdvance(-1); //let the user set it
                    barricade.setTurnsToComplete(16);
                    barricade.setRequiredResource1(-1);
                    barricade.setRequiredResource2(-1);
                    this.workerJob.add(barricade);
                    
                    //Search the techs to see if any match the default prerequisites
                    for (int i = 0; i < this.technology.size(); i++) {
                        String name = this.technology.get(i).getName();
                        if (name.equals("Flight")) {
                            airfield.setRequiredAdvance(i);
                        }
                        else if (name.equals("Advanced Flight")) {
                            radarTower.setRequiredAdvance(i);
                        }
                        else if (name.equals("Masonry")) {
                            outpost.setRequiredAdvance(i);
                        }
                        else if (name.equals("Construction")) {
                            barricade.setRequiredAdvance(i);
                        }
                    }
                }
                inputTERR(ins[0]);
                logger.debug("TERR Bytes read: " + dataInputted + "; input bytes read = " + ins[0].numBytesRead);
                inputWSIZ(ins[0]);
                logger.debug("WSIZ Bytes read: " + dataInputted + "; input bytes read = " + ins[0].numBytesRead);
                if (version == civ3Version.CONQUESTS && !fromSAV)
                {
                    inputFLAV(ins[0]);
                }
                else if (convertToConquests == 1)
                {
                    flavor = new ArrayList<FLAV>();
                    for (int i = 0; i < 7; i++)
                    {
                        FLAV flavour = new FLAV(this, "Flavor " + i);
                        flavour.setNumberOfFlavors(7);
                        //add relations with other flavours (even if they don't exist yet)
                        for (int j = 0; j < 7; j++)
                        {
                            flavour.relationWithOtherFlavor.add(new Integer(0));
                        }
                        flavor.add(flavour);
                    }
                }


                //Extract Englishes and post-processing.
                try
                {
                    performRulePostProcessingFromBIQ();
                }
                catch (Exception e)
                {
                    logger.error("Exception while extracing binary data", e);
                    JOptionPane.showMessageDialog(null, "Could not input file - error extracting binary data on building/technology/civilization headers.", "File input failed.", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                //If there's more, it's either World Map OR Custom Player Data next
                //OR neither of the above and it's GAME time
                if (logger.isDebugEnabled())
                    logger.debug("at world map/game section; " + dataInputted + " bytes inputted.");
                ins[0].read(inputFour, 0, 4);
                dataInputted += 4;
                temp = new String(inputFour, currentCharset);
                if (logger.isInfoEnabled())
                    logger.info("At end of custom rules; time taken = " + (System.nanoTime() - start)/1000000 + " ms");
            }   //end custom rules
            else
            {
                hasCustomRules = false;
                IO defaultRules = DefaultRulesLoader.getDefaultRules();
                this.buildings = defaultRules.buildings;
                this.citizens = defaultRules.citizens;
                this.culture = defaultRules.culture;
                this.difficulties = defaultRules.difficulties;
                this.eras = defaultRules.eras;
                this.espionage = defaultRules.espionage;
                this.experience = defaultRules.experience;
                this.resource = defaultRules.resource;
                this.government = defaultRules.government;
                this.rule = defaultRules.rule;
                this.unit = defaultRules.unit;
                this.numUnits = defaultRules.numUnits;
                this.civilization = defaultRules.civilization;
                this.technology = defaultRules.technology;
                this.workerJob = defaultRules.workerJob;
                this.terrain = defaultRules.terrain;
                this.worldSize = defaultRules.worldSize;
                this.flavor = defaultRules.flavor;
            }
            if (temp.equals("WCHR"))   //go ahead and do world map
            {   //else, game data comes next anyways
                hasCustomMap = true;
                if (logger.isInfoEnabled())
                    logger.info("custom map");
                //logger.info(temp);


                inputWCHR(ins[0]);
                logger.info("WCHR Bytes read: " + dataInputted + "; input bytes read = " + ins[0].numBytesRead);
                inputWMAP(ins[0]);
                logger.info("WMAP Bytes read: " + dataInputted + "; input bytes read = " + ins[0].numBytesRead);
                if (logger.isDebugEnabled())
                    logger.debug("at tile section; " + dataInputted + " bytes inputted.");
                long beforeMapStuff = System.nanoTime();
                //Multithread this baby!
                for (int i = 1; i < ins.length; i++)
                {
                    ins[i].skipBytes(dataInputted);
                }
                inputTILE(ins);
                logger.info("TILE Bytes read: " + dataInputted + "; input bytes read = " + ins[0].numBytesRead);
                long afterMapStuff = System.nanoTime();
                inputCONT(ins);
                logger.info("CONT Bytes read: " + dataInputted + "; input bytes read = " + ins[0].numBytesRead);
                //SLOCs were introduced in Vanilla 1.29f.  If the scenario is
                //from before then, don't try to import them.
                //In BIC terms, this means version 4.01 or newer.
                //As early as 2.07 has been seen in the wild.
                if (version.ordinal() >= civ3Version.PTW.ordinal() || (majorVersionNumber >= 4 && minorVersionNumber >= 1)) {
                    inputSLOC(ins);
                    logger.info("SLOC Bytes read: " + dataInputted + "; input bytes read = " + ins[0].numBytesRead);
                    int dataPriorToCity = dataInputted;
                    inputCITY(ins[0]);
                    logger.info("CITY Bytes read: " + dataInputted + "; input bytes read = " + ins[0].numBytesRead);
                    for (int i = 1; i < ins.length; i++)
                        ins[i].skipBytes(dataInputted-dataPriorToCity);
                    inputUNIT(ins[0]);
                    logger.info("UNIT Bytes read: " + dataInputted + "; input bytes read = " + ins[0].numBytesRead);
                    inputCLNY(ins[0]);
                    logger.info("CLNY Bytes read: " + dataInputted + "; input bytes read = " + ins[0].numBytesRead);
                }
                long mapStuff = afterMapStuff - beforeMapStuff;
                if (logger.isInfoEnabled())
                    logger.info("Time to process tile stuff: " + mapStuff/1000000 + " milliseconds; total time: " + (afterMapStuff-start)/1000000 + " ms");
                //now set up the x's and y's on TILEs
                int width = worldMap.get(0).width; //will be even
                int height = worldMap.get(0).height;
                int x, y;
                x = y = 0;
                for (int i = 0; i < tile.size(); i++)
                {
                    tile.get(i).calculateUnitWithBestDefence();
                    tile.get(i).xPos = x;
                    tile.get(i).yPos = y;
                    x+=2;
                    if (x >= width && x %2 == 0)    //end of even row
                    {
                        y+=1;
                        x = 1;
                    }
                    else if (x >= width && x % 2 == 1)
                    {
                         y+=1;
                         x = 0;
                    }
                }

                if (logger.isInfoEnabled())
                    logger.info("finished map data");
                //if wmap was skipped, temp is already "GAME"
                //but it wasn't, so input "GAME"
                ins[0].read(inputFour, 0, 4);
                dataInputted += 4;
                temp = new String(inputFour, currentCharset);
                
                //TODO: Some of the above should be moved into post-processing
                performMapPostProcessing();
            }   //inherent else
            //if there was no map, the inherent else is taken and you end up here
            //if there was a map, that's done now, and you have to import stuff here too

            //GAME Section
            //temp will already be ready
            //This is not present in the early builds of Vanilla
            if (version.ordinal() >= civ3Version.PTW.ordinal() || (majorVersionNumber >= 3 && minorVersionNumber >= 8) || (majorVersionNumber >= 4)) {
                if (!(temp.equals("GAME")))
                {
                    logger.error("Failed to input at GAME header - header was " + temp);
                    return false;
                }
                inputGAME(ins[0]);
            }
            else {
                if (convertToConquests == 1) {
                    initializeGAMESection();
                }
            }
            
            if (majorVersionNumber == 12 && minorVersionNumber == 6)
            {
                this.scenarioProperty.get(0).dataLength+=12;    //for MP turn times
            }
            if (logger.isInfoEnabled())
            {
                logger.info("File length: " + fileLength);
                logger.info("data inputted: " + dataInputted);
            }

            boolean isDone = false;
            if (fromSAV) {
                //detect if LEAD
                ins[0].mark(4);
                ins[0].read(inputFour, 0, 4);
                dataInputted += 4;
                temp = new String(inputFour, currentCharset);
                if (!temp.equals("LEAD")) {
                    isDone = true;
                }
                ins[0].reset();
            }
            
            //TODO: Make clearer when I'm less tired
            if ((fileLength <= dataInputted && !fromSAV) || isDone)
            {
                long end = System.nanoTime();
                long duration = end - start;
                if (logger.isInfoEnabled())
                    logger.info("Time to input BIQ: " + duration/1000000 + " milliseconds");
                if (convertToConquests > 0)
                    version = civ3Version.CONQUESTS;
                return true;
            }   //inherent else - if otherwise, input custom player data

            //LEAD Section - MAY NOT EXIST!!!!!!!!!!!!!!!!!!!!!!!!!!
            ins[0].read(inputFour, 0, 4);
            String tempLEAD = new String(inputFour, currentCharset);
            dataInputted += 4;
            if (!(tempLEAD.equals("LEAD")))
            {
                logger.error("Could not input file - failed at LEAD header.  Header is " + temp + "; bytes inputted: " + dataInputted);
                return false;
            }
            inputLEAD(ins[0]);
            if (logger.isDebugEnabled())
                logger.debug("finished custom player data");

            if (logger.isInfoEnabled())
                logger.info("final input:  " + dataInputted);
            if (fileLength == dataInputted || fromSAV)
            {
                long end = System.nanoTime();
                long duration = end - start;
                logger.info("Time to input BIQ: " + duration/1000000 + " milliseconds");
            }
            else
                return false;
            //TODO: Support staying in PTW.  For now, always go to Conquests
            if (convertToConquests > 0)
                version = civ3Version.CONQUESTS;
            //EVERYTHING HAS BEEN INPUTTED
            //If a temporary file exists, pwn it
            if (!fromSAV && file.exists() && file.getName().equals("._tmp.biq"))
                file.delete();
            return true;
        }
        catch (java.io.EOFException e)
        {
            logger.error("file size: " + fileLength);
            logger.error("bytes read at time of crash: " + dataInputted);
            //this means that the end of the file has been reached
            //and there is no LEAD section
            logger.error(e.getCause());
            logger.error(e.toString());
            logger.error(e.getClass());
            logger.error(e);
            return false;
        }
        catch(FileNotFoundException e)
        {
            logger.error("File not found: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(null, "Could not find the file: " + e.getMessage());
            return false;
        }
        catch (java.lang.OutOfMemoryError e)
        {
            logger.error("ERROR: ", e);
            JOptionPane.showMessageDialog(null, "Out of memory!", "No more RAM, dude!", JOptionPane.ERROR_MESSAGE);
        }
        catch (Exception e)
        {
            logger.error("ERROR:  ", e);
            return false;
        }
        logger.warn("Discrepancy in data inputted (" + dataInputted + ") and file size (" + fileLength + ")");
        return false;
    }

    private void initializeGAMESection() {
        //Initialize scenario properties
        scenarioProperty = new ArrayList<GAME>();
        GAME properties = new GAME(this);
        for (int i = 0; i < 5; i++) {
            properties.warWith0.add(0);
            properties.warWith1.add(0);
            properties.warWith2.add(0);
            properties.warWith3.add(0);
            properties.warWith4.add(0);
        }
        properties.dataLength = 7453 - 128;
        properties.numberOfPlayableCivs = 31;
        for (int i = 1; i < civilization.size(); i++) {
            properties.civPartOfWhichAlliance.add(0);
            properties.idOfPlayableCivs.add(i);
        }
        properties.dataLength = properties.dataLength + civilization.size() * 8;
        scenarioProperty.add(properties);
    }

    //TODO: This should likely be combined with the rule post processing from a scenario.
    //There may be a little bit of extra post-processing there but I expect a majority to be shared.
    private void performRulePostProcessingFromBIQ() {
        for (BLDG building : buildings) {
            //logger.info("numFlavors: " + numFlavors);
            building.extractEnglish(numFlavors);
            //The below lines set up TECH links based on int links (not done initially b/c techs not imported yet)
            building.setReqAdvance(building.getReqAdvance());
            building.setObsoleteBy(building.getObsoleteBy());
            building.setUnitProduced(building.getUnitProduced());
            building.setReqResource1(building.getReqResource1());
            building.setReqResource2(building.getReqResource2());
        }
        for (CTZN citizen : citizens) {
            citizen.setPrerequisite(citizen.getPrerequisite());
        }
        for (int i = 0; i < numTechnologies; i++)
        {
            technology.get(i).extractEnglish(numFlavors);
            technology.get(i).refreshPrerequisites();
        }
        for (RACE civ : civilization) {
            civ.extractEnglish(numFlavors);
            civ.refreshFreeTechs();
            civ.setKingUnit(civ.getKingUnit());
        }
        for (GOVT government : government) {
            government.setPrerequisiteTechnology(government.getPrerequisiteTechnology());
        }
        for (int i = 0; i < resource.size(); i++) {
            resource.get(i).setPrerequisite(resource.get(i).getPrerequisite());
        }
        for (int i = 0; i < numTerrains; i++)
        {
            terrain.get(i).simplifyGoods();
        }
        for (RULE ruleSet : rule) {
            ruleSet.setAdvancedBarbarian(ruleSet.getAdvancedBarbarian());
            ruleSet.setBasicBarbarian(ruleSet.getBasicBarbarian());
            ruleSet.setBarbarianSeaUnit(ruleSet.getBarbarianSeaUnit());
            ruleSet.setBattleCreatedUnit(ruleSet.getBattleCreatedUnit());
            ruleSet.setBuildArmyUnit(ruleSet.getBuildArmyUnit());
            ruleSet.setScout(ruleSet.getScout());
            ruleSet.setSlave(ruleSet.getSlave());
            ruleSet.setStartUnit1(ruleSet.getStartUnit1());
            ruleSet.setStartUnit2(ruleSet.getStartUnit2());
            ruleSet.setFlagUnit(ruleSet.getFlagUnit());
        }
        for (TRFM terraform : workerJob) {
            terraform.setRequiredAdvance(terraform.getRequiredAdvance());
        }
        for (PRTO prto : unit) {
            prto.setRequiredTech(prto.getRequiredTech());
            prto.setStealthTargetPRTOLinks();
            prto.setLegalUnitTelepadsPRTOLinks();
            prto.setEnslaveResultsIn(prto.getEnslaveResultsIn());
            prto.setUpgradeTo(prto.getUpgradeTo());
        }
        //Note map/lead extracts MUST be after they are imported
    }

    private boolean inputBLDG(LittleEndianDataInputStream in)
    {
        try
        {
            Integer integer = in.readInt();
            dataInputted += 4;
            //logger.info(integer);
            numBuildings = integer;
            for (int i = 0; i < numBuildings; i++)
            {   //import all the data and add it to the current building
                buildings.add(new BLDG(this));
                //logger.info("buildings.size():  " + buildings.size());
                //logger.info("i:  " + i);
                integer = in.readInt();
                dataInputted += 4;
                //Should always be the same data length
                //But keep a copy here for reading ancient BIC files
                int fileDataLength = integer;
                //buildings.get(i).setDataLength(integer);
                in.read(inputSixtyFour, 0, 64);
                dataInputted += 64;
                buildings.get(i).setDescription(new String(inputSixtyFour, currentCharset));
                //logger.info(buildings.get(i).getDescription());
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                buildings.get(i).setName(new String(inputThirtyTwo, currentCharset));
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                buildings.get(i).setCivilopediaEntry(new String(inputThirtyTwo, currentCharset));
                integer = in.readInt();
                dataInputted += 4;
                buildings.get(i).setDoublesHappiness(integer);
                integer = in.readInt();
                dataInputted += 4;
                buildings.get(i).setGainInEveryCity(integer);
                integer = in.readInt();
                dataInputted += 4;
                buildings.get(i).setGainOnContinent(integer);
                integer = in.readInt();
                dataInputted += 4;
                buildings.get(i).setReqImprovement(integer);
                integer = in.readInt();
                dataInputted += 4;
                buildings.get(i).setCost(integer);
                integer = in.readInt();
                dataInputted += 4;
                buildings.get(i).setCulture(integer);
                integer = in.readInt();
                dataInputted += 4;
                buildings.get(i).setBombardDefence(integer);
                integer = in.readInt();
                dataInputted += 4;
                buildings.get(i).setNavalBombardDefence(integer);
                integer = in.readInt();
                dataInputted += 4;
                buildings.get(i).setDefenceBonus(integer);
                integer = in.readInt();
                dataInputted += 4;
                buildings.get(i).setNavalDefenceBonus(integer);
                integer = in.readInt();
                dataInputted += 4;
                buildings.get(i).setMaintenanceCost(integer);
                integer = in.readInt();
                dataInputted += 4;
                buildings.get(i).setHappyAll(integer);
                integer = in.readInt();
                dataInputted += 4;
                buildings.get(i).setHappy(integer);
                integer = in.readInt();
                dataInputted += 4;
                buildings.get(i).setUnhappyAll(integer);
                integer = in.readInt();
                dataInputted += 4;
                buildings.get(i).setUnhappy(integer);
                integer = in.readInt();
                dataInputted += 4;
                buildings.get(i).setNumReqBuildings(integer);
                integer = in.readInt();
                dataInputted += 4;
                buildings.get(i).setAirPower(integer);
                integer = in.readInt();
                dataInputted += 4;
                buildings.get(i).setNavalPower(integer);
                integer = in.readInt();
                dataInputted += 4;
                buildings.get(i).setPollution(integer);
                integer = in.readInt();
                dataInputted += 4;
                buildings.get(i).setProduction(integer);
                integer = in.readInt();
                dataInputted += 4;
                buildings.get(i).setReqGovernment(integer);
                integer = in.readInt();
                dataInputted += 4;
                buildings.get(i).setSpaceshipPart(integer);
                integer = in.readInt();
                dataInputted += 4;
                buildings.get(i).setReqAdvance(integer);
                integer = in.readInt();
                dataInputted += 4;
                buildings.get(i).setObsoleteBy(integer);
                integer = in.readInt();
                dataInputted += 4;
                buildings.get(i).setReqResource1(integer);
                integer = in.readInt();
                dataInputted += 4;
                buildings.get(i).setReqResource2(integer);
                integer = in.readInt();
                dataInputted += 4;
                buildings.get(i).setImprovements(integer);
                integer = in.readInt();
                dataInputted += 4;
                buildings.get(i).setOtherChar(integer);
                integer = in.readInt();
                dataInputted += 4;
                buildings.get(i).setSmallWonderCharacteristics(integer);
                integer = in.readInt();
                dataInputted += 4;
                buildings.get(i).setWonderCharacteristics(integer);
                if (fileDataLength > 248) {
                    integer = in.readInt();
                    dataInputted += 4;
                    buildings.get(i).setArmiesRequired(integer);
                }
                if (version == civ3Version.CONQUESTS)
                {
                    integer = in.readInt();
                    dataInputted += 4;
                    buildings.get(i).setFlavors(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    buildings.get(i).setQuestionMark(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    buildings.get(i).setUnitProduced(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    buildings.get(i).setUnitFrequency(integer);
                }
                if (convertToConquests > 0)
                {
                    if (logger.isDebugEnabled())
                        logger.debug("Previous data length for " + buildings.get(i).getName() + " is " + buildings.get(i).getDataLength());
                    buildings.get(i).handleConvertToConquests();
                    
                    if (logger.isDebugEnabled())
                        logger.debug("Increasing data length for " + buildings.get(i).getName() + " to " + buildings.get(i).getDataLength());
                }
                else
                {
                    if (logger.isDebugEnabled())
                        logger.debug("Not converting to C3C");
                }
                //logger.info(buildings.get(i).toString());
                //note that gainOnContinent uses zero as the first building
                //so Pyramids causes to gain Building 2, the third entry, granaries
                buildings.get(i).trim();
            }
        }
        catch (Exception e)
        {
            logger.error("Error while inputting BLDG", e);
            return false;
        }
        return true;
    }

    private boolean inputCTZN(LittleEndianDataInputStream in)
    {
        try
        {
//CTZN Section
            in.read(inputFour, 0, 4);
            dataInputted += 4;
            String temp = new String(inputFour, currentCharset);
            if (!(temp.equals("CTZN")))
            {
                logger.error("Could not input file - failed at CTZN header.  Header was " + temp);
                return false;
            }
            //number of citizens
            Integer integer = in.readInt();
            dataInputted += 4;
            //logger.info(integer);
            numCitizens = integer;

            for (int i = 0; i < numCitizens; i++)
            {
                citizens.add(new CTZN(this));
                integer = in.readInt();
                dataInputted += 4;
                //The data length is always the same
                //citizens.get(i).setDataLength(integer);
                integer = in.readInt();
                dataInputted += 4;
                citizens.get(i).setDefaultCitizen(integer);
                in.read(inputThirtyTwo, 0, 32);
                citizens.get(i).setName(new String(inputThirtyTwo, currentCharset));
                in.read(inputThirtyTwo, 0, 32);
                citizens.get(i).setCivilopediaEntry(new String(inputThirtyTwo, currentCharset));
                in.read(inputThirtyTwo, 0, 32);
                citizens.get(i).setPluralName(new String(inputThirtyTwo, currentCharset));
                dataInputted += 96;
                integer = in.readInt();
                dataInputted += 4;
                citizens.get(i).setPrerequisite(integer);
                integer = in.readInt();
                dataInputted += 4;
                citizens.get(i).setLuxuries(integer);
                integer = in.readInt();
                dataInputted += 4;
                citizens.get(i).setResearch(integer);
                integer = in.readInt();
                dataInputted += 4;
                citizens.get(i).setTaxes(integer);
                if (version == civ3Version.CONQUESTS)
                {
                    integer = in.readInt();
                    dataInputted += 4;
                    citizens.get(i).setCorruption(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    citizens.get(i).setConstruction(integer);
                }
                if (convertToConquests > 0 && convertDataLengthsForConquests)
                {
                    citizens.get(i).increaseDataLength(8);
                }
                //logger.info(citizens.get(i).toString());
                //logger.info();
                citizens.get(i).trim();
            }

        }
        catch (Exception e)
        {
            logger.error("Error in CTZN header: ", e);
            return false;
        }
        return true;
    }

    private boolean inputCULT(LittleEndianDataInputStream in)
    {
        try
        {
            in.read(inputFour, 0, 4);
            dataInputted += 4;
            String temp = new String(inputFour, currentCharset);
            if (!(temp.equals("CULT")))
            {
                logger.error("Could not input file - failed at CULT header.  Header is " + temp + "; bytes inputted: " + dataInputted);
                return false;
            }
            //logger.info(temp);
            Integer integer = in.readInt();
            dataInputted += 4;
            //logger.info(integer);
            numCulturalOpinions = integer;
            
            //Early versions of Vanilla have two extra bytes, with values 33
            //and 5 (decimal) to start this section.  By BIC 4.01 (1.27f),
            //these are not present; they are through at least BIC 2.10 (1.16f).
            //Mercator did note this in the BIC format thread at Apolyton, so
            //it is expected.
            if (version == civ3Version.VANILLA && numCulturalOpinions == 33) {
                in.readInt();   //5 decimal
                numCulturalOpinions = in.readInt();
                dataInputted += 8;
            }

            for (int i = 0; i < numCulturalOpinions; i++)
            {
                culture.add(new CULT(this));
                integer = in.readInt();
                dataInputted += 4;
                //Should always be the same
                //culture.get(i).setDataLength(integer);
                in.read(inputSixtyFour, 0, 64);
                dataInputted += 64;
                culture.get(i).setName(new String(inputSixtyFour, currentCharset));

                integer = in.readInt();
                dataInputted += 4;
                culture.get(i).setPropagandaSuccess(integer);
                integer = in.readInt();
                dataInputted += 4;
                culture.get(i).setCultRatioPercent(integer);
                integer = in.readInt();
                dataInputted += 4;
                culture.get(i).setRatioDenominator(integer);
                integer = in.readInt();
                dataInputted += 4;
                culture.get(i).setRatioNumerator(integer);
                integer = in.readInt();
                dataInputted += 4;
                culture.get(i).setInitResistanceChance(integer);
                integer = in.readInt();
                dataInputted += 4;
                culture.get(i).setContinuedResistanceChance(integer);
                //logger.info(culture.get(i).toString());
                //logger.info();
                culture.get(i).trim();
            }
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Error in CULT section: " + e);
            return false;
        }
        return true;
    }

    private boolean inputDIFF(LittleEndianDataInputStream in)
    {
        try
        {
            //DIFF Section
            in.read(inputFour, 0, 4);
            dataInputted += 4;
            String temp = new String(inputFour, currentCharset);
            if (!(temp.equals("DIFF")))
            {
                logger.error("Failed at DIFF header - instead, read " + temp);
                return false;
            }
            //logger.info(temp);
            //logger.info(temp);
            //number of difficulties
            Integer integer = in.readInt();
            dataInputted += 4;
            //logger.info(integer);
            numDifficulties = integer;

            for (int i = 0; i < numDifficulties; i++)
            {
                difficulties.add(new DIFF(this));
                integer = in.readInt();
                int diffLength = integer;
                dataInputted += 4;
                //should always be the same
                //difficulties.get(i).setDataLength(integer);
                in.read(inputSixtyFour, 0, 64);
                dataInputted += 64;
                difficulties.get(i).setName(new String(inputSixtyFour, currentCharset));
                integer = in.readInt();
                dataInputted += 4;
                difficulties.get(i).setContentCitizens(integer);
                //In very early versions of Vanilla (BIC 2.05, initial release),
                //the only difficulty level option was content citizens.
                if (diffLength > 68) {
                    integer = in.readInt();
                    dataInputted += 4;
                    difficulties.get(i).setMaxGovtTransition(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    difficulties.get(i).setAIDefenceStart(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    difficulties.get(i).setAIOffenceStart(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    difficulties.get(i).setExtraStart1(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    difficulties.get(i).setExtraStart2(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    difficulties.get(i).setAdditionalFreeSupport(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    difficulties.get(i).setBonusPerCity(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    difficulties.get(i).setAttackBarbariansBonus(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    difficulties.get(i).setCostFactor(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    difficulties.get(i).setPercentOptimal(integer);
                    //AI-AI Trade and corruption percent were a later addition
                    //to Vanilla.
                    //By BIC 4.01 (Vanilla 1.29f) these were present
                    if (diffLength > 108) {
                        integer = in.readInt();
                        dataInputted += 4;
                        difficulties.get(i).setAIAITrade(integer);
                        integer = in.readInt();
                        dataInputted += 4;
                        difficulties.get(i).setCorruptionPercent(integer);
                    }
                }
                if (version.ordinal() >= civ3Version.PTW.ordinal()) {
                    integer = in.readInt();
                    dataInputted += 4;
                    difficulties.get(i).setMilitaryLaw(integer);
                }
                //logger.info(difficulties.get(i).toString());
                //logger.info();
                difficulties.get(i).trim();
            }
        }
        catch (Exception e)
        {
            logger.error("Error in DIFF section: ", e);
            return false;
        }
        return true;
    }

    private boolean inputERAS(LittleEndianDataInputStream in)
    {
        try
        {
            //ERAS Section
            in.read(inputFour, 0, 4);
            dataInputted += 4;
            String temp = new String(inputFour, currentCharset);
            if (!(temp.equals("ERAS")))
            {
                logger.error("Could not input file - failed at ERAS header.  Header is " + temp + "; bytes inputted: " + dataInputted);
                return false;
            }
            //logger.info(temp);
            //number of difficulties
            Integer integer = in.readInt();
            dataInputted += 4;
            //logger.info(integer);
            numEras = integer;

            for (int i = 0; i < numEras; i++)
            {
                eras.add(new ERAS(this));
                integer = in.readInt();
                dataInputted += 4;
                //Should always be the same
                //eras.get(i).setDataLength(integer);
                in.read(inputSixtyFour, 0, 64);
                dataInputted += 64;
                eras.get(i).setEraName(new String(inputSixtyFour, currentCharset));
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                eras.get(i).setCivilopediaEntry(new String(inputThirtyTwo, currentCharset));
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                eras.get(i).setResearcher1(new String(inputThirtyTwo, currentCharset));
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                eras.get(i).setResearcher2(new String(inputThirtyTwo, currentCharset));
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                eras.get(i).setResearcher3(new String(inputThirtyTwo, currentCharset));
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                eras.get(i).setResearcher4(new String(inputThirtyTwo, currentCharset));
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                eras.get(i).setResearcher5(new String(inputThirtyTwo, currentCharset));
                integer = in.readInt();
                dataInputted += 4;
                eras.get(i).setUsedResearcherNames(integer);
                if (version == civ3Version.CONQUESTS)
                {
                    integer = in.readInt();
                    dataInputted += 4;
                    eras.get(i).setQuestionMark(integer);
                }
                if (convertToConquests > 0)
                {
                    //TODO: Handle not converting to Conquests
                    //For now, always convert, and thus always add to the length
                    //eras.get(i).setDataLength(eras.get(i).getDataLength() + 4);
                }
                //logger.info(eras.get(i).toString());
                //logger.info();
                eras.get(i).trim();
            }
        }
        catch (Exception e)
        {
            logger.error("Error in ERA section: ", e);
            return false;
        }
        return true;
    }

    private boolean inputESPN(LittleEndianDataInputStream in)
    {
        try
        {
            //ESPN Section
            in.read(inputFour, 0, 4);
            dataInputted += 4;
            String temp = new String(inputFour, currentCharset);
            if (!(temp.equals("ESPN")))
            {
                logger.error("Could not input file - failed at ESPN header.  Header is " + temp + "; bytes inputted: " + dataInputted);
                return false;
            }
            //logger.info(temp);
            //number of difficulties
            Integer integer = in.readInt();
            dataInputted += 4;
            //logger.info(integer);
            numEspionage = integer;

            for (int i = 0; i < numEspionage; i++)
            {
                espionage.add(new ESPN(this));
                int espnLength = in.readInt();
                dataInputted += 4;
                //should always be the same
                //espionage.get(i).setDataLength(integer);
                in.read(inputOneHundredTwentyEight, 0, 128);
                dataInputted += 128;
                espionage.get(i).setDescription(new String(inputOneHundredTwentyEight, currentCharset));
                //logger.info(espionage.get(i).getDescription());
                in.read(inputSixtyFour, 0, 64);
                dataInputted += 64;
                espionage.get(i).setName(new String(inputSixtyFour, currentCharset));
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                espionage.get(i).setCivilopediaEntry(new String(inputThirtyTwo, currentCharset));
                integer = in.readInt();
                dataInputted += 4;
                espionage.get(i).setMissionPerformedBy(integer);
                //Base cost was not present in early versions of Vanilla
                if (espnLength > 228) {
                    integer = in.readInt();
                    dataInputted += 4;
                    espionage.get(i).setBaseCost(integer);
                    //logger.info(espionage.get(i).toString());
                    //logger.info();
                }
                espionage.get(i).trim();
            }
        }
        catch (Exception e)
        {
            logger.error("Error in ESPN : ", e);
            return false;
        }
        return true;
    }

    private boolean inputEXPR(LittleEndianDataInputStream in)
    {
        try
        {
            //EXPR Section
            in.read(inputFour, 0, 4);
            dataInputted += 4;
            String temp = new String(inputFour, currentCharset);
            if (!(temp.equals("EXPR")))
            {
                logger.error("Could not input file - failed at EXPR header.  Header is " + temp + "; bytes inputted: " + dataInputted);
                return false;
            }
            //logger.info(temp);
            //number of difficulties
            Integer integer = in.readInt();
            dataInputted += 4;
            //logger.info(integer);
            numExprLevel = integer;

            for (int i = 0; i < numExprLevel; i++)
            {
                experience.add(new EXPR(this));
                int exprLength = in.readInt();
                dataInputted += 4;
                //Should always be the same
                //experience.get(i).setDataLength(integer);
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                experience.get(i).setName(new String(inputThirtyTwo, currentCharset));
                integer = in.readInt();
                dataInputted += 4;
                experience.get(i).setBaseHitPoints(integer);
                //Early versions of Vanilla did not have Retreat Bonus in EXPR
                //This was added in BIC 3.08/Vanilla 1.21f, according to the
                //official release notes.
                if (majorVersionNumber > 3 || (majorVersionNumber == 3 && minorVersionNumber >= 8)) {
                    integer = in.readInt();
                    dataInputted += 4;
                    experience.get(i).setRetreatBonus(integer);
                }
                //logger.info(experience.get(i).toString());
                experience.get(i).trim();
            }
        }
        catch (Exception e)
        {
            logger.error("Error in EXPR: ", e);
            return false;
        }
        return true;
    }

    private boolean inputGOOD(LittleEndianDataInputStream in)
    {
        try
        {
            //GOOD Section
            in.read(inputFour, 0, 4);
            dataInputted += 4;
            String temp = new String(inputFour, currentCharset);
            if (!(temp.equals("GOOD")))
            {
                logger.error("Could not input file - failed at GOOD header.  Header is " + temp + "; bytes inputted: " + dataInputted);
                return false;
            }
            //logger.info(temp);
            //number of difficulties
            Integer integer = in.readInt();
            dataInputted += 4;
            //logger.info(integer);
            numGoods = integer;

            for (int i = 0; i < numGoods; i++)
            {
                resource.add(new GOOD(this));
                resource.get(i).setIndex(i);
                integer = in.readInt();
                dataInputted += 4;
                //Should always be the same
                //resource.get(i).setDataLength(integer);
                in.read(inputTwentyFour, 0, 24);
                dataInputted += 24;
                resource.get(i).setName(new String(inputTwentyFour, currentCharset));
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                resource.get(i).setCivilopediaEntry(new String(inputThirtyTwo, currentCharset));
                integer = in.readInt();
                dataInputted += 4;
                resource.get(i).setType(integer);
                integer = in.readInt();
                dataInputted += 4;
                resource.get(i).setAppearanceRatio(integer);
                integer = in.readInt();
                dataInputted += 4;
                resource.get(i).setDisapperanceProbability(integer);
                integer = in.readInt();
                dataInputted += 4;
                resource.get(i).setIcon(integer);
                integer = in.readInt();
                dataInputted += 4;
                resource.get(i).setPrerequisite(integer);
                integer = in.readInt();
                dataInputted += 4;
                resource.get(i).setFoodBonus(integer);
                integer = in.readInt();
                dataInputted += 4;
                resource.get(i).setShieldsBonus(integer);
                integer = in.readInt();
                dataInputted += 4;
                resource.get(i).setCommerceBonus(integer);
                //logger.info(resource.get(i).toString());
                resource.get(i).trim();
            }
        }
        catch (Exception e)
        {
            logger.error("Error : ", e);
            return false;
        }
        return true;
    }

    private boolean inputGOVT(LittleEndianDataInputStream in)
    {
        try
        {
            //GOVT Section
            in.read(inputFour, 0, 4);
            dataInputted += 4;
            String temp = new String(inputFour, currentCharset);
            //logger.info("GOVERNMENTS");
            //logger.info(dataInputted);
            //logger.info(temp);
            if (!(temp.equals("GOVT")))
            {
                logger.error("Could not input file - failed at GOVT header.  Header is " + temp + "; bytes inputted: " + dataInputted);
                return false;
            }
            //logger.info(temp);
            //number of difficulties
            Integer integer = in.readInt();
            dataInputted += 4;
            //logger.info(integer);
            numGovernments = integer;

            for (int i = 0; i < numGovernments; i++)
            {
                government.add(new GOVT(this));
                integer = in.readInt();
                dataInputted += 4;
                //Auto-calculated
                //government.get(i).setDataLength(integer);
                integer = in.readInt();
                dataInputted += 4;
                government.get(i).setDefaultType(integer);
                integer = in.readInt();
                dataInputted += 4;
                government.get(i).setTransitionType(integer);
                integer = in.readInt();
                dataInputted += 4;
                government.get(i).setRequiresMaintenance(integer);
                integer = in.readInt();
                dataInputted += 4;
                government.get(i).setQuestionMarkOne(integer);
                integer = in.readInt();
                dataInputted += 4;
                government.get(i).setTilePenalty(integer);
                integer = in.readInt();
                dataInputted += 4;
                government.get(i).setCommerceBonus(integer);
                in.read(inputSixtyFour, 0, 64);
                dataInputted += 64;
                government.get(i).setName(new String(inputSixtyFour, currentCharset));
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                government.get(i).setCivilopediaEntry(new String(inputThirtyTwo, currentCharset));
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                government.get(i).setMaleRulerTitle1(new String(inputThirtyTwo, currentCharset));
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                government.get(i).setFemaleRulerTitle1(new String(inputThirtyTwo, currentCharset));
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                government.get(i).setMaleRulerTitle2(new String(inputThirtyTwo, currentCharset));
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                government.get(i).setFemaleRulerTitle2(new String(inputThirtyTwo, currentCharset));
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                government.get(i).setMaleRulerTitle3(new String(inputThirtyTwo, currentCharset));
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                government.get(i).setFemaleRulerTitle3(new String(inputThirtyTwo, currentCharset));
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                government.get(i).setMaleRulerTitle4(new String(inputThirtyTwo, currentCharset));
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                government.get(i).setFemaleRulerTitle4(new String(inputThirtyTwo, currentCharset));
                integer = in.readInt();
                dataInputted += 4;
                government.get(i).setCorruption(integer);
                integer = in.readInt();
                dataInputted += 4;
                government.get(i).setImmuneTo(integer);
                integer = in.readInt();
                dataInputted += 4;
                government.get(i).setDiplomatLevel(integer);
                integer = in.readInt();
                dataInputted += 4;
                government.get(i).setSpyLevel(integer);
                integer = in.readInt();
                dataInputted += 4;
                government.get(i).setNumberOfGovernments(integer);
                //govt-govt relations
                for (int j = 0; j < government.get(i).getNumberOfGovernments(); j++)
                {
                    //logger.info(j);
                    government.get(i).relations.add(new GOVTGOVTRelations());
                    integer = in.readInt();
                    dataInputted += 4;
                    government.get(i).relations.get(j).setCanBribe(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    government.get(i).relations.get(j).setBriberyModifier(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    government.get(i).relations.get(j).setResistanceModifier(integer);
                    //logger.info(government.get(i).relations.get(j).toString());
                }
                //
                integer = in.readInt();
                dataInputted += 4;
                government.get(i).setHurrying(integer);
                integer = in.readInt();
                dataInputted += 4;
                government.get(i).setAssimilationChance(integer);
                integer = in.readInt();
                dataInputted += 4;
                government.get(i).setDraftLimit(integer);
                integer = in.readInt();
                dataInputted += 4;
                government.get(i).setMilitaryPoliceLimit(integer);
                integer = in.readInt();
                dataInputted += 4;
                government.get(i).setRulerTitlePairsUsed(integer);
                integer = in.readInt();
                dataInputted += 4;
                government.get(i).setPrerequisiteTechnology(integer);
                integer = in.readInt();
                dataInputted += 4;
                government.get(i).setScienceCap(integer);
                integer = in.readInt();
                dataInputted += 4;
                government.get(i).setWorkerRate(integer);
                integer = in.readInt();
                dataInputted += 4;
                government.get(i).setQuestionMarkTwo(integer);
                integer = in.readInt();
                dataInputted += 4;
                government.get(i).setQuestionMarkThree(integer);
                integer = in.readInt();
                dataInputted += 4;
                government.get(i).setQuestionMarkFour(integer);
                integer = in.readInt();
                dataInputted += 4;
                government.get(i).setFreeUnits(integer);
                integer = in.readInt();
                dataInputted += 4;
                government.get(i).setFreeUnitsPerTown(integer);
                integer = in.readInt();
                dataInputted += 4;
                government.get(i).setFreeUnitsPerCity(integer);
                integer = in.readInt();
                dataInputted += 4;
                government.get(i).setFreeUnitsPerMetropolis(integer);
                integer = in.readInt();
                dataInputted += 4;
                government.get(i).setCostPerUnit(integer);
                integer = in.readInt();
                dataInputted += 4;
                government.get(i).setWarWeariness(integer);
                if (version == civ3Version.CONQUESTS)
                {
                    integer = in.readInt();
                    dataInputted += 4;
                    government.get(i).setXenophobic(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    government.get(i).setForceResettlement(integer);
                }
                if (convertToConquests > 0 && convertDataLengthsForConquests)
                {
                    government.get(i).dataLength+=8;
                }
                //logger.info(government.get(i).toString());
                //logger.info();
                government.get(i).trim();
            }
        }
        catch (Exception e)
        {
            logger.error("Error in GOVT: ", e);
            return false;
        }
        return true;
    }

    private boolean inputRULE(LittleEndianDataInputStream in)
    {
        try
        {
            //RULE Section
            in.read(inputFour, 0, 4);
            dataInputted += 4;
            String temp = new String(inputFour, currentCharset);
            if (!(temp.equals("RULE")))
            {
                logger.error("Could not input file - failed at RULE header.  Header is " + temp + "; bytes inputted: " + dataInputted);
                return false;
            }
            //logger.info(temp);
            //number of difficulties
            Integer integer = in.readInt();
            dataInputted += 4;
            //logger.info(integer);
            numRules = integer;
            
            for (int i = 0; i < numRules; i++)
            {
                rule.add(new RULE(this));
                //Standard length:
                // 64 * 6 culture levels  = 384
                // 4 * 10 spaceship parts =  40
                // 296 other bytes        = 296
                // 720 total              = 720
                //N.B. This doesn't include the 4 bytes for dataLength
                int adjustmentFromStandardLength = 0;
                int ruleLength = in.readInt();
                dataInputted += 4;
                //Begin fix for 1.00 issue
                int ruleStartInputted = dataInputted;
                boolean convertedRules = false;
                rule.get(i).setDataLength(ruleLength);
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                rule.get(i).setTownName(new String(inputThirtyTwo, currentCharset));
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                rule.get(i).setCityName(new String(inputThirtyTwo, currentCharset));
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                rule.get(i).setMetropolisName(new String(inputThirtyTwo, currentCharset));
                int numSpaceshipParts = in.readInt();
                dataInputted += 4;
                adjustmentFromStandardLength += (numSpaceshipParts - 10) * 4;
                //Obsolete - now calculated automatically
                //rule.get(i).setNumSpaceshipParts(integer);
                //input number of each part required
                for (int j = 0; j < numSpaceshipParts; j++)
                {
                    integer = in.readInt();
                    dataInputted += 4;
                    rule.get(i).addSpaceshipPart(integer, true);
                }
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setAdvancedBarbarian(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setBasicBarbarian(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setBarbarianSeaUnit(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setCitiesForArmy(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setChanceOfRioting(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setDraftTurnPenalty(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setShieldCostInGold(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setFortressDefenceBonus(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setCitizensAffectedByHappyFace(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setQuestionMark1(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setQuestionMark2(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setForestValueInShields(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setShieldValueInGold(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setCitizenValueInShields(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setDefaultDifficultyLevel(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setBattleCreatedUnit(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setBuildArmyUnit(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setBuildingDefensiveBonus(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setCitizenDefensiveBonus(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setDefaultMoneyResource(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setChanceToInterceptAirMissions(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setChanceToInterceptStealthMissions(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setStartingTreasury(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setQuestionMark3(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setFoodConsumptionPerCitizen(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setRiverDefensiveBonus(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setTurnPenaltyForWhip(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setScout(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setSlave(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setRoadMovementRate(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setStartUnit1(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setStartUnit2(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setWLTKDMinimumPop(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setTownDefenceBonus(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setCityDefenceBonus(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setMetropolisDefenceBonus(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setMaxCity1Size(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setMaxCity2Size(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setQuestionMark4(integer);
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setFortificationsDefenceBonus(integer);
                int numCultureLevelNames = in.readInt();
                dataInputted += 4;
                //Obsolete - now calculated automatically
                //rule.get(i).setNumCulturalLevels(integer);
                //input name of each cultural level
                adjustmentFromStandardLength += (numCultureLevelNames - 6) * 64;
                for (int j = 0; j < numCultureLevelNames; j++)
                {
                    in.read(inputSixtyFour, 0, 64);
                    dataInputted += 64;
                    rule.get(i).addCultureLevelName(new String(inputSixtyFour, currentCharset));
                    //logger.info(new String(inputSixtyFour, currentCharset));
                }
                //in.read(inputSixtyFour, 0, 64);
                //temp = new String(inputSixtyFour, currentCharset);
                //logger.info(temp);                integer = in.readInt();
                integer = in.readInt();
                dataInputted += 4;
                rule.get(i).setBorderExpansionMultiplier(integer);
                //Early versions of Vanilla did not have the last 5 fields
                //in RULE.
                //Combined with PTW/Conquests additions, these add up to 28
                //bytes fewer than the 720 standard, which then must be adjusted
                //if spaceship parts/culture level name lengths are non-standard
                if (ruleLength > (692 + adjustmentFromStandardLength)) {
                    integer = in.readInt();
                    dataInputted += 4;
                    rule.get(i).setBorderFactor(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    rule.get(i).setFutureTechCost(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    rule.get(i).setGoldenAgeDuration(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    rule.get(i).setMaximumResearchTime(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    rule.get(i).setMinimumResearchTime(integer);
                }
                if (version.ordinal() >= civ3Version.PTW.ordinal()) {
                    integer = in.readInt();
                    dataInputted += 4;
                    rule.get(i).setFlagUnit(integer);
                    if (version == civ3Version.CONQUESTS)
                    {
                        integer = in.readInt();
                        dataInputted += 4;
                        rule.get(i).setUpgradeCost(integer);
                    }
                    else if (convertToConquests > 0)
                    {
                        convertedRules = true;
                        rule.get(i).convertToConquestsFromPTW();
                    }
                }
                else if (convertToConquests > 0) {
                    //712 is the "standard" length of latest-version Vanilla BICs
                    //The last 8 will be added by the two conversion calls
                    if (ruleLength < (712 + adjustmentFromStandardLength)) {
                        //Add data length for the fields added in later versions
                        //of Vanilla.
                        rule.get(i).setDataLength(712 + adjustmentFromStandardLength);
                    }
                    rule.get(i).convertToPTWFromVanilla();
                    rule.get(i).convertToConquestsFromPTW();
                    convertedRules = true;                    
                }
                
                //Fix for bug in editor version 1.00
                if (!convertedRules) {
                    int dataForRules = dataInputted - ruleStartInputted;
                    if (rule.get(i).getDataLength() != dataForRules) {
                        rule.get(i).setDataLength(dataForRules);
                    }
                }
                //logger.info(rule.get(i).toString());
                //logger.info();
                rule.get(i).trim();

            }
        }
        catch (Exception e)
        {
            logger.error("Error in RULE:", e);
            return false;
        }
        return true;
    }

    private boolean inputPRTO(LittleEndianDataInputStream in)
    {
        //logger.setLevel(Level.DEBUG);
        int i = 0;
        try
        {
            //PRTO Section
            in.read(inputFour, 0, 4);
            dataInputted += 4;
            String temp = new String(inputFour, currentCharset);
            if (!(temp.equals("PRTO")))
            {
                logger.error("Could not input file - failed at PRTO header.  Header is " + temp + "; bytes inputted: " + dataInputted);
                return false;
            }
            //logger.info(temp);
            //number of difficulties
            Integer integer = in.readInt();
            dataInputted += 4;
            //logger.info(integer);
            numUnits = integer;

            for (i = 0; i < numUnits; i++)
            {
                int startData = dataInputted;
                PRTO newPRTO = new PRTO(this, i);
                int prtoLength = in.readInt();
                dataInputted += 4;
                //Auto-calculated
                //newPRTO.setDataLength(integer);
                integer = in.readInt();
                dataInputted += 4;
                newPRTO.setZoneOfControl(integer);
                if (logger.isDebugEnabled())
                    logger.debug("At name: " + dataInputted + " bytes inputted");
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                newPRTO.setName(new String(inputThirtyTwo, currentCharset));
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                newPRTO.setCivilopediaEntry(new String(inputThirtyTwo, currentCharset));
                integer = in.readInt();
                dataInputted += 4;
                newPRTO.setBombardStrength(integer);
                integer = in.readInt();
                dataInputted += 4;
                newPRTO.setBombardRange(integer);
                integer = in.readInt();
                dataInputted += 4;
                newPRTO.setCapacity(integer);
                integer = in.readInt();
                dataInputted += 4;
                newPRTO.setShieldCost(integer);
                integer = in.readInt();
                dataInputted += 4;
                newPRTO.setDefence(integer);
                integer = in.readInt();
                dataInputted += 4;
                newPRTO.setIconIndex(integer);
                integer = in.readInt();
                dataInputted += 4;
                newPRTO.setAttack(integer);
                integer = in.readInt();
                dataInputted += 4;
                newPRTO.setOperationalRange(integer);
                integer = in.readInt();
                dataInputted += 4;
                newPRTO.setPopulationCost(integer);
                integer = in.readInt();
                dataInputted += 4;
                newPRTO.setRateOfFire(integer);
                integer = in.readInt();
                dataInputted += 4;
                newPRTO.setMovement(integer);
                integer = in.readInt();
                dataInputted += 4;
                newPRTO.setRequiredTech(integer);
                integer = in.readInt();
                dataInputted += 4;
                newPRTO.setUpgradeTo(integer);
                integer = in.readInt();
                dataInputted += 4;
                newPRTO.setRequiredResource1(integer);
                integer = in.readInt();
                dataInputted += 4;
                newPRTO.setRequiredResource2(integer);
                integer = in.readInt();
                dataInputted += 4;
                newPRTO.setRequiredResource3(integer);
                integer = in.readInt();
                dataInputted += 4;
                newPRTO.setUnitAbilities(integer);
                integer = in.readInt();
                dataInputted += 4;
                newPRTO.setAIStrategy(integer);
                integer = in.readInt();
                dataInputted += 4;
                newPRTO.setAvailableToBinary(integer);
                integer = in.readInt();
                dataInputted += 4;
                newPRTO.setStandardOrdersSpecialActions(integer);
                integer = in.readInt();
                dataInputted += 4;
                newPRTO.setAirMissions(integer);
                integer = in.readInt();
                dataInputted += 4;
                newPRTO.setUnitClass(integer);
                //Other strategy and HP bonus were not present in early Vanilla.
                //They were added in BIC 3.08/Vanilla 1.21f
                if (prtoLength > 156) {
                    integer = in.readInt();
                    dataInputted += 4;
                    newPRTO.setOtherStrategy(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    newPRTO.setHitPointBonus(integer);
                    if (version.ordinal() >= version.PTW.ordinal()) {
                        integer = in.readInt();
                        dataInputted += 4;
                        newPRTO.setPTWStandardOrders(integer);
                        integer = in.readInt();
                        dataInputted += 4;
                        newPRTO.setPTWSpecialActions(integer);
                        integer = in.readInt();
                        dataInputted += 4;
                        newPRTO.setPTWWorkerActions(integer);
                        integer = in.readInt();
                        dataInputted += 4;
                        newPRTO.setPTWAirMissions(integer);
                        integer = in.readInt();
                        dataInputted += 4;
                        integer = integer - 65536; //only want first two bytes
                        newPRTO.setPTWActionsMix(integer);
                        integer = in.readInt();
                        dataInputted += 4;
                        newPRTO.setBombardEffects(integer);
                        //terrain ignore movements
                        int maxTerr = version == civ3Version.CONQUESTS ? 14 : 12;
                        for (int j = 0; j < maxTerr; j++)
                        {
                            inputByte = in.readByte();
                            dataInputted++;
                            newPRTO.setIgnoredMovementCost(j,inputByte);
                        }
                        integer = in.readInt();
                        dataInputted += 4;
                        newPRTO.setRequiresSupport(integer);
                        if (version == civ3Version.CONQUESTS)
                        {
                            integer = in.readInt();
                            dataInputted += 4;
                            newPRTO.setUseExactCost(integer);
                            integer = in.readInt();
                            dataInputted += 4;
                            newPRTO.setTelepadRange(integer);
                            integer = in.readInt();
                            dataInputted += 4;
                            newPRTO.setQuestionMark3(integer);
                            int numUnitTelepads = in.readInt();
                            dataInputted += 4;
                            //Now calculate this outselves
                            //newPRTO.setNumLegalUnitTelepads(integer);
                            if (logger.isDebugEnabled())
                                logger.debug(dataInputted + " and num unit telepads = " + integer);
                            for (int j = 0; j < numUnitTelepads; j++)
                            {
                                integer = in.readInt();
                                dataInputted += 4;
                                newPRTO.addUnitTelepad(integer);
                            }
                            integer = in.readInt();
                            dataInputted += 4;
                            newPRTO.setEnslaveResultsIn(integer);
                            integer = in.readInt();
                            dataInputted += 4;
                            newPRTO.setQuestionMark5(integer);
                            int numStealthTargets = in.readInt();
                            dataInputted += 4;
                            //newPRTO.setNumStealthTargets(integer);
                            for (int j = 0; j < numStealthTargets; j++)
                            {
                                integer = in.readInt();
                                dataInputted += 4;
                                newPRTO.addStealthAttackTarget(integer);
                            }
                            integer = in.readInt();
                            dataInputted += 4;
                            newPRTO.setQuestionMark6(integer);
                            int numLegalBuildingTelepads = in.readInt();
                            dataInputted += 4;
                            //newPRTO.setNumLegalBuildingTelepads(integer);
                            if (logger.isDebugEnabled())
                                logger.debug(dataInputted + " and num bldg telepads = " + integer);
                            for (int j = 0; j < numLegalBuildingTelepads; j++)
                            {
                                integer = in.readInt();
                                dataInputted += 4;
                                newPRTO.addBuildingTelepad(integer);
                            }
                            inputByte = in.readByte();
                            dataInputted++;
                            newPRTO.setCreatesCraters(inputByte);
                            float floatingPoint = in.readFloat();
                            //integer = in.readInt();
                            dataInputted += 4;
                            newPRTO.setWorkerStrengthFloat(floatingPoint);
                            //need to find out if there are more bytes
                            integer = in.readInt();
                            dataInputted += 4;
                            newPRTO.setQuestionMark8(integer);
                            integer = in.readInt();
                            dataInputted += 4;
                            newPRTO.setAirDefence(integer);
                        }
                    }
                }
                //logger.info(newPRTO.toString());
                //logger.info();
                if (newPRTO.getOtherStrategy() != -1)    //strategy map
                {   //combine the strategies
                    
                    unit.get(newPRTO.getOtherStrategy()).AIStrategy = (unit.get(newPRTO.getOtherStrategy()).AIStrategy) | newPRTO.AIStrategy;
                    //Recalculate the constituent parts
                    unit.get(newPRTO.getOtherStrategy()).extractAIStrategy();
                }
                else{
                    //new unit, add it
                    newPRTO.trim();
                    newPRTO.extractEnglish();
                    unit.add(newPRTO);
                    
                    //Check if we need to convert old-version units to new-version units
                    if (convertToConquests > 0)
                    {
                        if (version == civ3Version.VANILLA) {
                            newPRTO.convertFromVanillaToConquests();
                        }
                        else {
                            newPRTO.convertFromPTWToConquests();
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error("Error in PRTO: ", e);
            logger.error("Bytes inputted: " + dataInputted);
            try{
                logger.error("Unit affected: " + unit.get(unit.size() - 1).getName() + ", index: " + i);
                logger.error("num legal unit telepads: " + unit.get(unit.size() - 1).getNumLegalUnitTelepads());
                logger.error("num legal building telepads: " + unit.get(unit.size() - 1).getNumLegalBuildingTelepads());
            }
            catch(Exception f){
                logger.error("Could not get unit name", f);
            }
            return false;
        }
        return true;
    }

    private boolean inputRACE(LittleEndianDataInputStream in)
    {
        try
        {
            //RACE Section
            in.read(inputFour, 0, 4);
            dataInputted += 4;
            String temp = new String(inputFour, currentCharset);
            if (!(temp.equals("RACE")))
            {
                logger.error("Could not input file - failed at RACE header.  Header is " + temp + "; bytes inputted: " + dataInputted);
                return false;
            }
            //logger.info(temp);
            //number of difficulties
            Integer integer = in.readInt();
            dataInputted += 4;
            //logger.info(integer);
            numCivilizations = integer;

            for (int i = 0; i < numCivilizations; i++)
            {
                //logger.info(i);
                civilization.add(new RACE(this));
                integer = in.readInt();
                dataInputted += 4;
                //civilization.get(i).setDataLength(integer);
                int numCities = in.readInt();
                dataInputted += 4;
                //civilization.get(i).setNumCityNames(integer);
                for (int j = 0; j < numCities; j++)
                {
                    //logger.info(civilization.get(i).getNumCityNames());
                    in.read(inputTwentyFour, 0, 24);
                    dataInputted += 24;
                    civilization.get(i).addCityName(new String(inputTwentyFour, currentCharset));
                }
                int numMilitaryLeaders = in.readInt();
                dataInputted += 4;
                //civilization.get(i).setNumGreatLeaders(integer);
                for (int j = 0; j < numMilitaryLeaders; j++)
                {
                    in.read(inputThirtyTwo, 0, 32);
                    dataInputted += 32;
                    civilization.get(i).addMilitaryLeader(new String(inputThirtyTwo, currentCharset));
                }
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                civilization.get(i).setLeaderName(new String(inputThirtyTwo, currentCharset));
                in.read(inputTwentyFour, 0, 24);
                dataInputted += 24;
                civilization.get(i).setLeaderTitle(new String(inputTwentyFour, currentCharset));
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                civilization.get(i).setCivilopediaEntry(new String(inputThirtyTwo, currentCharset));
                in.read(inputForty, 0, 40);
                dataInputted += 40;
                civilization.get(i).setAdjective(new String(inputForty, currentCharset));
                in.read(inputForty, 0, 40);
                dataInputted += 40;
                civilization.get(i).setCivilizationName(new String(inputForty, currentCharset));
                in.read(inputForty, 0, 40);
                dataInputted += 40;
                civilization.get(i).setNoun(new String(inputForty, currentCharset));
                for (int j = 0; j < numEras; j++)
                {
                    in.read(inputTwoSixty, 0, 260);
                    dataInputted += 260;
                    civilization.get(i).addForwardFilename(new String(inputTwoSixty, currentCharset));
                }
                for (int j = 0; j < numEras; j++)
                {
                    in.read(inputTwoSixty, 0, 260);
                    dataInputted += 260;
                    civilization.get(i).addReverseFilename(new String(inputTwoSixty, currentCharset));
                }
                integer = in.readInt();
                dataInputted += 4;
                civilization.get(i).setCultureGroup(integer);
                integer = in.readInt();
                dataInputted += 4;
                civilization.get(i).setLeaderGender(integer);
                integer = in.readInt();
                dataInputted += 4;
                civilization.get(i).setCivilizationGender(integer);
                integer = in.readInt();
                dataInputted += 4;
                civilization.get(i).setAggressionLevel(integer);
                integer = in.readInt();
                dataInputted += 4;
                civilization.get(i).setUniqueCivilizationCounter(integer);
                integer = in.readInt();
                dataInputted += 4;
                civilization.get(i).setShunnedGovernment(integer);
                integer = in.readInt();
                dataInputted += 4;
                civilization.get(i).setFavoriteGovernment(integer);
                integer = in.readInt();
                dataInputted += 4;
                civilization.get(i).setDefaultColor(integer);
                integer = in.readInt();
                dataInputted += 4;
                civilization.get(i).setUniqueColor(integer);
                integer = in.readInt();
                dataInputted += 4;
                civilization.get(i).setFreeTech1Index(integer);
                integer = in.readInt();
                dataInputted += 4;
                civilization.get(i).setFreeTech2Index(integer);
                integer = in.readInt();
                dataInputted += 4;
                civilization.get(i).setFreeTech3Index(integer);
                integer = in.readInt();
                dataInputted += 4;
                civilization.get(i).setFreeTech4Index(integer);
                integer = in.readInt();
                dataInputted += 4;
                civilization.get(i).setBonuses(integer);
                integer = in.readInt();
                dataInputted += 4;
                civilization.get(i).setGovernorSettings(integer);
                integer = in.readInt();
                dataInputted += 4;
                civilization.get(i).setBuildNever(integer);
                integer = in.readInt();
                dataInputted += 4;
                civilization.get(i).setBuildOften(integer);
                integer = in.readInt();
                dataInputted += 4;
                civilization.get(i).setPlurality(integer);
                if (version.ordinal() >= version.PTW.ordinal()) {
                    integer = in.readInt();
                    dataInputted += 4;
                    civilization.get(i).setKingUnit(integer);
                    if (version == civ3Version.CONQUESTS)
                    {
                        integer = in.readInt();
                        dataInputted += 4;
                        civilization.get(i).setFlavors(integer);
                        integer = in.readInt();
                        dataInputted += 4;
                        civilization.get(i).setQuestionMark(integer);
                        integer = in.readInt();
                        dataInputted += 4;
                        civilization.get(i).setDiplomacyTextIndex(integer);
                        int numScientificLeaders = in.readInt();
                        dataInputted += 4;
                        //civilization.get(i).setnumScientificLeaders(integer);
                        for (int j = 0; j < numScientificLeaders; j++)
                        {
                            in.read(inputThirtyTwo, 0, 32);
                            dataInputted += 32;
                            civilization.get(i).addScientificLeader(new String(inputThirtyTwo, currentCharset));
                        }
                    }
                }
                if (convertToConquests > 0)
                {
                    civilization.get(i).handleConvertFromPTWToConquests();
                }
                //logger.info(civilization.get(i).toString());
                //logger.info();
                civilization.get(i).trim();
            }
        }
        catch (Exception e)
        {
            logger.error("Error in RACE: ", e);
            return false;
        }
        return true;
    }

    private boolean inputTECH(LittleEndianDataInputStream in)
    {
        try
        {
            //TECH Section
            in.read(inputFour, 0, 4);
            dataInputted += 4;
            String temp = new String(inputFour, currentCharset);
            if (!(temp.equals("TECH")))
            {
                logger.error("Could not input file - failed at TECH header.  Header is " + temp + "; bytes inputted: " + dataInputted);
                return false;
            }
            //logger.info(temp);
            //number of difficulties
            Integer integer = in.readInt();
            dataInputted += 4;
            //logger.info(integer);
            numTechnologies = integer;

            for (int i = 0; i < numTechnologies; i++)
            {
                technology.add(new TECH(this));
                technology.get(i).setIndex(i);
                integer = in.readInt();
                dataInputted += 4;
                //technology.get(i).setDataLength(integer);
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                technology.get(i).setName(new String(inputThirtyTwo, currentCharset));
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                technology.get(i).setCivilopediaEntry(new String(inputThirtyTwo, currentCharset));
                integer = in.readInt();
                dataInputted += 4;
                technology.get(i).setCost(integer);
                integer = in.readInt();
                dataInputted += 4;
                technology.get(i).setEra(integer);
                integer = in.readInt();
                dataInputted += 4;
                technology.get(i).setAdvanceIcon(integer);
                integer = in.readInt();
                dataInputted += 4;
                technology.get(i).setX(integer);
                integer = in.readInt();
                dataInputted += 4;
                technology.get(i).setY(integer);
                integer = in.readInt();
                dataInputted += 4;
                technology.get(i).setPrerequisite1(integer);
                integer = in.readInt();
                dataInputted += 4;
                technology.get(i).setPrerequisite2(integer);
                integer = in.readInt();
                dataInputted += 4;
                technology.get(i).setPrerequisite3(integer);
                integer = in.readInt();
                dataInputted += 4;
                technology.get(i).setPrerequisite4(integer);
                integer = in.readInt();
                dataInputted += 4;
                technology.get(i).setFlags(integer);
                if (version == civ3Version.CONQUESTS)
                {
                    integer = in.readInt();
                    dataInputted += 4;
                    technology.get(i).setFlavors(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    technology.get(i).setQuestionMark(integer);
                }
                if (convertToConquests > 0)
                {
                    technology.get(i).convertFromPTWToConquests();
                }
                //logger.info(technology.get(i).toString());
                //logger.info();
                technology.get(i).trim();
            }
        }
        catch (Exception e)
        {
            logger.error("Error in TECH: ", e);
            return false;
        }
        return true;
    }
    
    private boolean inputTFRM(LittleEndianDataInputStream in)
    {
        try
        {
            //TFRM Section
            in.read(inputFour, 0, 4);
            dataInputted += 4;
            String temp = new String(inputFour, currentCharset);
            if (!(temp.equals("TFRM")))
            {
                logger.error("Could not input file - failed at TFRM header.  Header is " + temp + "; bytes inputted: " + dataInputted);
                return false;
            }
            //logger.info(temp);
            //number of difficulties
            Integer integer = in.readInt();
            dataInputted += 4;
            //logger.info(integer);
            numWorkerJobs = integer;

            for (int i = 0; i < numWorkerJobs; i++)
            {
                workerJob.add(new TRFM(this));
                integer = in.readInt();
                dataInputted += 4;
                workerJob.get(i).setDataLength(integer);
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                workerJob.get(i).setName(new String(inputThirtyTwo, currentCharset));
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                workerJob.get(i).setCivilopediaEntry(new String(inputThirtyTwo, currentCharset));
                integer = in.readInt();
                dataInputted += 4;
                workerJob.get(i).setTurnsToComplete(integer);
                integer = in.readInt();
                dataInputted += 4;
                workerJob.get(i).setRequiredAdvance(integer);
                integer = in.readInt();
                dataInputted += 4;
                workerJob.get(i).setRequiredResource1(integer);
                integer = in.readInt();
                dataInputted += 4;
                workerJob.get(i).setRequiredResource2(integer);
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                workerJob.get(i).setOrder(new String(inputThirtyTwo, currentCharset));
                //logger.info(workerJob.get(i).toString());
                //logger.info();
                workerJob.get(i).trim();
            }
        }
        catch (Exception e)
        {
            logger.error("Error in TFRM: " + e);
            return false;
        }
        return true;
    }

    private boolean inputTERR(LittleEndianDataInputStream in)
    {
        try
        {
            //TERR Section
            in.read(inputFour, 0, 4);
            dataInputted += 4;
            String temp = new String(inputFour, currentCharset);
            if (!(temp.equals("TERR")))
            {
                logger.error("Could not input file - failed at TERR header.  Header is " + temp + "; bytes inputted: " + dataInputted);
                return false;
            }
            //logger.info(temp);
            //number of difficulties
            Integer integer = in.readInt();
            dataInputted += 4;
            //logger.info(integer);
            numTerrains = integer;

            for (int i = 0; i < numTerrains; i++)
            {
                terrain.add(new TERR(this));
                integer = in.readInt();
                dataInputted += 4;
                terrain.get(i).setDataLength(integer);
                integer = in.readInt();
                dataInputted += 4;
                terrain.get(i).setNumTotalResources(integer);
                int bytesOfAllowedResourceDataToInput = (integer + 7) / 8;
                logger.trace("Inputting " + bytesOfAllowedResourceDataToInput + " bytes of data on allowed resources.");
                for (int j = 0; j < bytesOfAllowedResourceDataToInput; j++)
                {
                    inputByte = in.readByte();
                    dataInputted++;
                    terrain.get(i).possibleResources.add(inputByte);
                }
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                terrain.get(i).setName(new String(inputThirtyTwo, currentCharset));
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                terrain.get(i).setCivilopediaEntry(new String(inputThirtyTwo, currentCharset));
                integer = in.readInt();
                dataInputted += 4;
                terrain.get(i).setFoodBonus(integer);
                integer = in.readInt();
                dataInputted += 4;
                terrain.get(i).setShieldsBonus(integer);
                integer = in.readInt();
                dataInputted += 4;
                terrain.get(i).setCommerceBonus(integer);
                integer = in.readInt();
                dataInputted += 4;
                terrain.get(i).setDefenceBonus(integer);
                integer = in.readInt();
                dataInputted += 4;
                terrain.get(i).setMovementCost(integer);
                integer = in.readInt();
                dataInputted += 4;
                terrain.get(i).setFood(integer);
                integer = in.readInt();
                dataInputted += 4;
                terrain.get(i).setShields(integer);
                integer = in.readInt();
                dataInputted += 4;
                terrain.get(i).setCommerce(integer);
                integer = in.readInt();
                dataInputted += 4;
                terrain.get(i).setWorkerJob(integer);
                integer = in.readInt();
                dataInputted += 4;
                terrain.get(i).setPollutionEffect(integer);
                if (majorVersionNumber > 4 || majorVersionNumber == 4 && minorVersionNumber >= 1) {
                    inputByte = in.readByte();
                    dataInputted++;
                    terrain.get(i).setAllowCities(inputByte);
                    inputByte = in.readByte();
                    dataInputted++;
                    terrain.get(i).setAllowColonies(inputByte);
                    if (version.ordinal() >= civ3Version.PTW.ordinal()) {
                        inputByte = in.readByte();
                        dataInputted++;
                        terrain.get(i).setImpassable(inputByte);
                        inputByte = in.readByte();
                        dataInputted++;
                        terrain.get(i).setImpassableByWheeled(inputByte);
                        inputByte = in.readByte();
                        dataInputted++;
                        terrain.get(i).setAllowAirfields(inputByte);
                        inputByte = in.readByte();
                        dataInputted++;
                        terrain.get(i).setAllowForts(inputByte);
                        inputByte = in.readByte();
                        dataInputted++;
                        terrain.get(i).setAllowOutposts(inputByte);
                        inputByte = in.readByte();
                        dataInputted++;
                        terrain.get(i).setAllowRadarTowers(inputByte);
                        if (version == civ3Version.CONQUESTS)
                        {
                            integer = in.readInt();
                            dataInputted += 4;
                            terrain.get(i).setQuestionMark(integer);
                            inputByte = in.readByte();
                            dataInputted++;
                            terrain.get(i).setLandmarkEnabled(inputByte);
                            integer = in.readInt();
                            dataInputted += 4;
                            terrain.get(i).setLandmarkFood(integer);
                            integer = in.readInt();
                            dataInputted += 4;
                            terrain.get(i).setLandmarkShields(integer);
                            integer = in.readInt();
                            dataInputted += 4;
                            terrain.get(i).setLandmarkCommerce(integer);
                            integer = in.readInt();
                            dataInputted += 4;
                            terrain.get(i).setLandmarkFoodBonus(integer);
                            integer = in.readInt();
                            dataInputted += 4;
                            terrain.get(i).setLandmarkShieldsBonus(integer);
                            integer = in.readInt();
                            dataInputted += 4;
                            terrain.get(i).setLandmarkCommerceBonus(integer);
                            integer = in.readInt();
                            dataInputted += 4;
                            terrain.get(i).setLandmarkMovementCost(integer);
                            integer = in.readInt();
                            dataInputted += 4;
                            terrain.get(i).setLandmarkDefenceBonus(integer);
                            in.read(inputThirtyTwo, 0, 32);
                            dataInputted += 32;
                            terrain.get(i).setLandmarkName(new String(inputThirtyTwo, currentCharset));
                            in.read(inputThirtyTwo, 0, 32);
                            dataInputted += 32;
                            terrain.get(i).setLandmarkCivilopediaEntry(new String(inputThirtyTwo, currentCharset));
                            integer = in.readInt();
                            dataInputted += 4;
                            terrain.get(i).setQuestionMark2(integer);
                            integer = in.readInt();
                            dataInputted += 4;
                            terrain.get(i).setTerrainFlags(integer);
                            integer = in.readInt();
                            dataInputted += 4;
                            terrain.get(i).setDiseaseStrength(integer);
                        }
                        else {
                            if (convertToConquests > 0) {
                                terrain.get(i).dataLength+=113;
                            }
                        }
                    }
                    else {
                        if (convertToConquests > 0) {
                            terrain.get(i).dataLength+=120; //7 for PTW; 113 for Conquests
                        }
                    }   
                }
                else if (convertToConquests > 0) {
                    terrain.get(i).dataLength+=122; //2 for Vanilla 1.29f, 7 for PTW, 113 for Conquests
                }
                //logger.info(terrain.get(i).toString());
                //logger.info();
                terrain.get(i).trim();
            }
            if (convertToConquests > 0) {
                //Add Marshes and Volcanoes
                /**
                TERR marsh = new TERR(this);
                marsh.setName("Marsh");
                marsh.setCivilopediaEntry("TERR_Marsh");
                marsh.setFood(1);
                marsh.setCommerceBonus(1);
                marsh.setMovementCost(2);
                marsh.setDefenceBonus(20);
                * **/
                
            }
        }
        catch (Exception e)
        {
            logger.error("Error in TERR: ", e);
            return false;
        }
        return true;
    }

    private boolean inputWSIZ(LittleEndianDataInputStream in)
    {
        try
        {
            //WSIZ Section
            in.read(inputFour, 0, 4);
            dataInputted += 4;
            String temp = new String(inputFour, currentCharset);
            if (!(temp.equals("WSIZ")))
            {
                logger.error("Could not input file - failed at WSIZ header.  Header is " + temp + "; bytes inputted: " + dataInputted);
                return false;
            }
            //logger.info(temp);
            //number of difficulties
            Integer integer = in.readInt();
            dataInputted += 4;
            //logger.info(integer);
            numWorldSizes = integer;

            for (int i = 0; i < numWorldSizes; i++)
            {
                worldSize.add(new WSIZ(this));
                integer = in.readInt();
                dataInputted += 4;
                worldSize.get(i).setDataLength(integer);
                integer = in.readInt();
                dataInputted += 4;
                worldSize.get(i).setOptimalNumberOfCities(integer);
                integer = in.readInt();
                dataInputted += 4;
                worldSize.get(i).setTechRate(integer);
                in.read(inputTwentyFour, 0, 24);
                dataInputted += 24;
                worldSize.get(i).setEmpty(new String(inputTwentyFour, currentCharset));
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                worldSize.get(i).setName(new String(inputThirtyTwo, currentCharset));
                integer = in.readInt();
                dataInputted += 4;
                worldSize.get(i).setHeight(integer);
                integer = in.readInt();
                dataInputted += 4;
                worldSize.get(i).setDistanceBetweenCivs(integer);
                integer = in.readInt();
                dataInputted += 4;
                worldSize.get(i).setNumberOfCivs(integer);
                integer = in.readInt();
                dataInputted += 4;
                worldSize.get(i).setWidth(integer);
                //logger.info(worldSize.get(i).toString());
                //logger.info();
                worldSize.get(i).trim();
            }
        }
        catch (Exception e)
        {
            logger.error("Error in WSIZ: ", e);
            return false;
        }
        return true;
    }

    private boolean inputFLAV(LittleEndianDataInputStream in)
    {
        try
        {
            //FLAV Section
            in.read(inputFour, 0, 4);
            dataInputted += 4;
            String temp = new String(inputFour, currentCharset);
            if (!(temp.equals("FLAV")))
            {
                logger.error("Could not input file - failed at FLAV header.  Header is " + temp + "; bytes inputted: " + dataInputted);
                return false;
            }
            //logger.info(temp);
            //number of flavor groups (ONE)
            Integer integer = in.readInt();
            dataInputted += 4;
            //number of flavors
            integer = in.readInt();
            dataInputted += 4;
            //logger.info(integer);
            numFlavors = integer;

            for (int i = 0; i < numFlavors; i++)
            {
                flavor.add(new FLAV(this));
                integer = in.readInt();
                dataInputted += 4;
                flavor.get(i).setQuestionMark(integer);
                in.read(inputTwoFiftySix, 0, 256);
                dataInputted += 256;
                flavor.get(i).setName(new String(inputTwoFiftySix, currentCharset));
                integer = in.readInt();
                dataInputted += 4;
                flavor.get(i).setNumberOfFlavors(integer);
                //logger.info("Number of other flavors: " + flavor.get(i).getNumberOfFlavors());
                for (int j = 0; j < flavor.get(i).getNumberOfFlavors(); j++)
                {
                    //logger.info(j);
                    integer = in.readInt();
                    dataInputted += 4;
                    flavor.get(i).relationWithOtherFlavor.add(new Integer(integer));
                }
                //logger.info(flavor.get(i).toString());
                //logger.info();
                flavor.get(i).trim();
            }
        }
        catch (Exception e)
        {
            logger.error("Error in FLAV section: ", e);
            return false;
        }
        return true;
    }

    private boolean inputWMAP(LittleEndianDataInputStream in)
    {
        try
        {
            //WMAP Section
            in.read(inputFour, 0, 4);
            dataInputted += 4;
            String temp = new String(inputFour, currentCharset);
            if (!(temp.equals("WMAP")))
            {
                logger.error("Could not input file - failed at WMAP header.  Header is " + temp + "; bytes inputted: " + dataInputted);
                return false;
            }
            //logger.info(temp);
            //number of flavors
            Integer integer = in.readInt();
            dataInputted += 4;
            //logger.info(integer);
            numWorldMaps = integer;

            for (int i = 0; i < numWorldMaps; i++)
            {
                worldMap.add(new WMAP(this));
                integer = in.readInt();
                dataInputted += 4;
                worldMap.get(i).setDataLength(integer);
                integer = in.readInt();
                dataInputted += 4;
                worldMap.get(i).setNumResources(integer);
                for (int j = 0; j < worldMap.get(i).getNumResources(); j++)
                {
                    integer = in.readInt();
                    dataInputted += 4;
                    worldMap.get(i).resourceOccurence.add(integer);
                }
                integer = in.readInt();
                dataInputted += 4;
                worldMap.get(i).setNumContinents(integer);
                integer = in.readInt();
                dataInputted += 4;
                worldMap.get(i).setHeight(integer);
                integer = in.readInt();
                dataInputted += 4;
                worldMap.get(i).setDistanceBetweenCivs(integer);
                integer = in.readInt();
                dataInputted += 4;
                worldMap.get(i).setNumCivs(integer);
                integer = in.readInt();
                dataInputted += 4;
                worldMap.get(i).setQuestionMark1(integer);
                integer = in.readInt();
                dataInputted += 4;
                worldMap.get(i).setQuestionMark2(integer);
                integer = in.readInt();
                dataInputted += 4;
                worldMap.get(i).setWidth(integer);
                integer = in.readInt();
                dataInputted += 4;
                worldMap.get(i).setQuestionMark3(integer);
                byte[] oneTwoFour = new byte[124];
                in.read(oneTwoFour, 0, 124);
                dataInputted += 124;
                worldMap.get(i).setUnknown124(new String(oneTwoFour, currentCharset));
                integer = in.readInt();
                dataInputted += 4;
                worldMap.get(i).setMapSeed(integer);
                integer = in.readInt();
                dataInputted += 4;
                worldMap.get(i).setFlags(integer);
                
                worldMap.get(i).extractEnglish();
            }
            return true;
        }
        catch (Exception e)
        {
            logger.error("Error inputting file - WMAP section", e);
            return false;
        }
    }

    private boolean inputTILE(LittleEndianDataInputStream[]ins)
    {
        try
        {
            //TILE Section
            ins[0].read(inputFour, 0, 4);
            dataInputted += 4;
            String temp = new String(inputFour, currentCharset);
            if (!(temp.equals("TILE")))
            {
                logger.error("Could not input file - failed at TILE header.  Header is " + temp + "; bytes inputted: " + dataInputted);
                return false;
            }
            //logger.info(temp);
            //number of flavors
            Integer integer = ins[0].readInt();
            dataInputted += 4;
            //logger.info(integer);
            numTiles = integer;
            //logger.info("numScenarioProperties:  " + numScenarioProperties);
            //This is the crux of the delay in inputting BIQ files.
            //If it can be multithreaded, a significant speedup may be realized
            //Other possibilities of speedup include one giant method call to
            //initialize the tile (although this would require more local variables -
            //if they are Objects, their allocations may eat our time savings)
            //The autoboxing for reversing bytes probably isn't helping us any
            //either.
            //Multithread away, baby!
            if (logger.isDebugEnabled())
                logger.debug("Num tiles: " + numTiles);
            long preAdd = System.nanoTime();
            //Singlethreaded version - the multithreaded version with an array
            //and Arrays.toList not only was either no fast or slower (sometimes
            //significantly), but resulted in a fixed-size array, which will
            //be unacceptable once map editing is possible.
            tile = new ArrayList<TILE>();
            for (int i = 0; i < numTiles; i++)
            {
                tile.add(new TILE(this));
            }
            long add = System.nanoTime() - preAdd;
            if (logger.isInfoEnabled())
                logger.info("It took " + add/1000000 + " ms to add all the TILE objects");
            //This is where the multithreaded version deviates away
            //Creates as many threads and input sources as we'll need
            TILEThread[]tileThreads = new TILEThread[numProcs];
            //Create the new threads, and set up all input streams except the first (which is already ready)
            int skipBytesPerCPU = TILE_LENGTH;
            if (version == version.CONQUESTS) {
                skipBytesPerCPU = TILE_LENGTH;
            }
            else if (version == version.PTW) {
                skipBytesPerCPU = PTW_TILE_LENGTH;
            }
            else if (version == version.VANILLA) {
                if (majorVersionNumber == 2) {
                    skipBytesPerCPU = VANILLA_2_TILE_LENGTH;
                }
                else {
                    skipBytesPerCPU = VANILLA_3_4_TILE_LENGTH;
                }
            }
            for (int i = 0; i < numProcs; i++)
            {
                //Skip the 8 bytes since this method started, plus howevermany tiles to skip
                if (i != 0)
                {
                    ins[i].skipBytes(8 + skipBytesPerCPU * i);
                }
                tileThreads[i] = new TILEThread(ins[i], tile, numTiles, i, numProcs, this);
            }
            if (logger.isDebugEnabled())
                logger.debug("Size of tile array: " + tile.size());
            Integer[]inputFromThread = new Integer[numProcs];
            long preCall = System.nanoTime();
            //Start all the threads working
            for (int i = 0; i < numProcs; i++)
            {
                tileThreads[i].start();
            }
            //Wait for them to finish
            for (int i = 0; i < numProcs; i++)
            {
                tileThreads[i].join();
            }
            long postCall = System.nanoTime();
            long callTime = postCall - preCall;
            if (logger.isInfoEnabled())
                logger.info("It took " + callTime/1000000 + " ms to run the " + numProcs + " threads");
            //Check the amount of data that we inputted
            for (int i = 0; i < numProcs; i++)
            {
                inputFromThread[i] = tileThreads[i].getReturnValue();
                if (logger.isDebugEnabled())
                    logger.debug("in from thread " + i + ": " + inputFromThread[i]);
                dataInputted+=inputFromThread[i];
            }
        }
        catch (java.io.IOException e)
        {
            logger.error("IOException in TILE section - multithreaded", e);
            return false;
        }
        catch (java.lang.InterruptedException e)
        {
            logger.error("InterruptedException in TILE section - multithreaded", e);
            return false;
        }
        return true;
    }

    private boolean inputCONT(LittleEndianDataInputStream[]ins)
    {
        try
        {
            numContinents = inputHeader(ins[0], "CONT");
            for (int i = 0; i < numContinents; i++)
            {
                continent.add(new CONT(this));
            }
            CONTThread[]contThreads = new CONTThread[numProcs];
            //Create the new threads, skip bytes if necessary, and start them
            for (int i = 0; i < numProcs; i++)
            {
                if (i != 0)
                    ins[i].skipBytes(8 + CONT_LENGTH * i);
                contThreads[i] = new CONTThread(ins[i], continent, numContinents, i, numProcs);
                contThreads[i].start();
            }
            //Wait for them to finish, then check the amount of data inputted
            for (int i = 0; i < numProcs; i++)
            {
                contThreads[i].join();
                dataInputted+=contThreads[i].getReturnValue();
            }
        }
        catch (HeaderFailedException e)
        {
            logger.error(e.getMessage(), e);
            return false;
        }
        catch (Exception e)
        {
            logger.error("Error inputting file in CONT: ", e);
            return false;
        }
        return true;
    }

    private boolean inputSLOC(LittleEndianDataInputStream[]ins)
    {
        try
        {
            numStartingLocations = inputHeader(ins[0], "SLOC");
            for (int i = 0; i < numStartingLocations; i++)
            {
                startingLocation.add(new SLOC(this));
            }
            SLOCThread[]startingLocationThreads = new SLOCThread[numProcs];
            //Create the new threads, skip bytes if necessary, and start them
            for (int i = 0; i < numProcs; i++)
            {
                if (i != 0)
                    ins[i].skipBytes(8 + SLOC_LENGTH * i);
                startingLocationThreads[i] = new SLOCThread(ins[i], startingLocation, numStartingLocations, i, numProcs);
                startingLocationThreads[i].start();
            }
            //Wait for them to finish, then check the amount of data inputted
            for (int i = 0; i < numProcs; i++)
            {
                startingLocationThreads[i].join();
                dataInputted+=startingLocationThreads[i].getReturnValue();
            }
        }
        catch (HeaderFailedException e)
        {
            logger.error(e.getMessage(), e);
            return false;
        }
        catch (Exception e)
        {
            logger.error("Error inputting file in SLOC: ", e);
            return false;
        }
        return true;
    }

    /**
     * Due to the variable length of each CITY (depending on the number of
     * buildings), multithreading this is quite tricky, quite possibly
     * not worth the synchronization and threading overhead, and much more
     * likely to be incorrect than sections such as SLOC with constant-length.
     * Thus this method is not multithreaded at this time.
     * When initially setting off all the threads, we wouldn't know how far
     * to skip each of the DataInputStreams (after the first), and couldn't
     * know this until all prior threads had (on their first iteration) read
     * in their data length.  This could be accomplished by making the dataLength
     * field of CITY volatile (but only in Java 1.5), giving it an initial value
     * of zero, and checking it to see whether the data length should be read
     * (a flag and synchronization of setting of the flag and the data length
     * may also work).
     * However, each thread also doesn't know how far to jump for its next
     * iteration until <i>all subsequent threads</i> up to its next invocation
     * have read in their data length.  Thus it must wait at the end of its
     * loop until all of them have read in <i>their</i> data length.
     * Theoretically I think such behavior would ensure correctness, but I am
     * not confident enough in the reasoning to be sure that it would always work
     * in practice.  It also introduces the issues of:
     * <ul>
     *  <li>Not working prior to Java 1.5, should backporting ever be desired.</li>
     *  <li>Whether the performance benefit of multiple threads would still be worth it.</li>
     * </ul>
     * @param in
     * @return
     */
    private boolean inputCITY(LittleEndianDataInputStream in)
    {
        try
        {
            numCities = inputHeader(in, "CITY");
            for (int i = 0; i < numCities; i++)
            {
                city.add(new CITY(this));
                Integer integer = in.readInt();
                dataInputted += 4;
                //city.get(i).setDataLength(integer); //no longer directly setting CITY length
                inputByte = in.readByte();
                dataInputted++;
                city.get(i).setHasWalls(inputByte);
                inputByte = in.readByte();
                dataInputted++;
                city.get(i).setHasPalace(inputByte);
                in.read(inputTwentyFour, 0, 24);
                dataInputted += 24;
                city.get(i).setName(new String(inputTwentyFour, currentCharset));
                integer = in.readInt();
                dataInputted += 4;
                city.get(i).setOwnerType(integer);
                int numBuildings = in.readInt();
                dataInputted += 4;
                //We now (June 2014) just rely on the size of our array rather than keeping a separate int
                //city.get(i).setNumBuildings(integer);
                for (int j = 0; j < numBuildings; j++) {
                    integer = in.readInt();
                    dataInputted += 4;
                    city.get(i).addBuilding(integer);
                }
                city.get(i).updateWallStyleBuildingStatus();
                integer = in.readInt();
                dataInputted += 4;
                city.get(i).setCulture(integer);
                integer = in.readInt();
                dataInputted += 4;
                city.get(i).setOwner(integer);
                integer = in.readInt();
                dataInputted += 4;
                city.get(i).setSize(integer);
                integer = in.readInt();
                dataInputted += 4;
                city.get(i).setX(integer);
                integer = in.readInt();
                dataInputted += 4;
                city.get(i).setY(integer);
                integer = in.readInt();
                dataInputted += 4;
                city.get(i).setCityLevel(integer);
                integer = in.readInt();
                dataInputted += 4;
                city.get(i).setBorderLevel(integer);
                integer = in.readInt();
                dataInputted += 4;
                city.get(i).setUseAutoName(integer);
                city.get(i).trim();
            }
        }
        catch (Exception e)
        {
            logger.error("Error inputting file in CITY: ", e);
            return false;
        }
        return true;
    }

    private boolean inputUNIT(LittleEndianDataInputStream in)
    {
        try
        {
            //UNIT Section
            in.read(inputFour, 0, 4);
            dataInputted += 4;
            String temp = new String(inputFour, currentCharset);
            if (!(temp.equals("UNIT")))
            {
                logger.error("Could not input file - failed at UNIT header.  This often means a city name is too long.  Header is " + temp + "; bytes inputted: " + dataInputted + ".  Data inputted: " + dataInputted);
                return false;
            }
            //logger.info(temp);
            //number of flavors
            Integer integer = in.readInt();
            dataInputted += 4;
            //logger.info(integer);
            numMapUnits = integer;

            for (int i = 0; i < numMapUnits; i++)
            {
                mapUnit.add(new UNIT(this));
                mapUnit.get(i).setIndex(i);
                integer = in.readInt();
                dataInputted += 4;
                mapUnit.get(i).setDataLength(integer);
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                mapUnit.get(i).setName(new String(inputThirtyTwo, currentCharset));
                integer = in.readInt();
                dataInputted += 4;
                mapUnit.get(i).setOwnerType(integer);
                integer = in.readInt();
                dataInputted += 4;
                mapUnit.get(i).setExperienceLevel(integer);
                integer = in.readInt();
                dataInputted += 4;
                mapUnit.get(i).setOwner(integer);
                integer = in.readInt();
                dataInputted += 4;
                mapUnit.get(i).setPRTONumber(integer);
                integer = in.readInt();
                dataInputted += 4;
                mapUnit.get(i).setAIStrategy(integer);
                int x = in.readInt();
                dataInputted += 4;
                mapUnit.get(i).setX(x);
                int y = in.readInt();
                dataInputted += 4;
                mapUnit.get(i).setY(y);
                //Let the tile that this unit is on know about it
                tile.get(this.calculateTileIndex(x, y)).unitsOnTile.add(i);
                if (version.ordinal() >= civ3Version.PTW.ordinal()) {
                    in.read(inputFiftySeven, 0, 57);
                    dataInputted += 57;
                    mapUnit.get(i).setPTWCustomName(new String(inputFiftySeven, currentCharset));
                    integer = in.readInt();
                    dataInputted += 4;
                    mapUnit.get(i).setUseCivilizationKing(integer);
                    mapUnit.get(i).trim();
                }
            }
            for (UNIT unit : mapUnit) {
                unit.setPRTONumber(unit.getPRTONumber());
            }
        }
        catch (Exception e)
        {
            logger.error("Error inputting file in UNIT: ", e);
            return false;
        }
        return true;
    }

    private boolean inputCLNY(LittleEndianDataInputStream in)
    {
        try
        {
            //CLNY Section
            in.read(inputFour, 0, 4);
            dataInputted += 4;
            String temp = new String(inputFour, currentCharset);
            if (!(temp.equals("CLNY")))
            {
                logger.error("Could not input file - failed at CLNY header.  Header is " + temp + "; bytes inputted: " + dataInputted);
                return false;
            }
            //logger.info(temp);
            //number of flavors
            Integer integer = in.readInt();
            dataInputted += 4;
            //logger.info(integer);
            numColonies = integer;

            for (int i = 0; i < numColonies; i++)
            {
                colony.add(new CLNY(this));
                integer = in.readInt();
                dataInputted += 4;
                //Should always be the same data length
                //colony.get(i).setDataLength(integer);
                integer = in.readInt();
                dataInputted += 4;
                colony.get(i).setOwnerType(integer);
                integer = in.readInt();
                dataInputted += 4;
                colony.get(i).setOwner(integer);
                integer = in.readInt();
                dataInputted += 4;
                colony.get(i).setX(integer);
                integer = in.readInt();
                dataInputted += 4;
                colony.get(i).setY(integer);
                if (version.ordinal() >= civ3Version.PTW.ordinal()) {
                    integer = in.readInt();
                    dataInputted += 4;
                    colony.get(i).setImprovementType(integer);
                }
            }
        }
        catch (Exception e)
        {
            logger.error("Error in CLNY: ", e);
            return false;
        }
        return true;
    }

    /**
     * The header ("WCHR") will have already been read if this method is called.
     * Because the "WCHR" may not be present (if there is no custom map), it
     * must be checked before this method is called.
     *
     * @param in - The input stream.
     * @return - True if the input is successful, false otherwise.
     */
    private boolean inputWCHR(LittleEndianDataInputStream in)
    {
        try
        {
            //WCHR Section
            Integer integer = in.readInt();
            dataInputted += 4;
            //logger.info(integer);
            numWorldCharacteristics = integer;

            for (int i = 0; i < numWorldCharacteristics; i++)
            {
                worldCharacteristic.add(new WCHR(this));
                integer = in.readInt();
                dataInputted += 4;
                worldCharacteristic.get(i).setDataLength(integer);
                integer = in.readInt();
                dataInputted += 4;
                worldCharacteristic.get(i).setSelectedClimate(integer);
                integer = in.readInt();
                dataInputted += 4;
                worldCharacteristic.get(i).setActualClimate(integer);
                integer = in.readInt();
                dataInputted += 4;
                worldCharacteristic.get(i).setSelectedBarbarianActivity(integer);
                integer = in.readInt();
                dataInputted += 4;
                worldCharacteristic.get(i).setActualBarbarianActivity(integer);
                integer = in.readInt();
                dataInputted += 4;
                worldCharacteristic.get(i).setSelectedLandform(integer);
                integer = in.readInt();
                dataInputted += 4;
                worldCharacteristic.get(i).setActualLandform(integer);
                integer = in.readInt();
                dataInputted += 4;
                worldCharacteristic.get(i).setSelectedOceanCoverage(integer);
                integer = in.readInt();
                dataInputted += 4;
                worldCharacteristic.get(i).setActualOceanCoverage(integer);
                integer = in.readInt();
                dataInputted += 4;
                worldCharacteristic.get(i).setSelectedTemperature(integer);
                integer = in.readInt();
                dataInputted += 4;
                worldCharacteristic.get(i).setActualTemperature(integer);
                integer = in.readInt();
                dataInputted += 4;
                worldCharacteristic.get(i).setSelectedAge(integer);
                integer = in.readInt();
                dataInputted += 4;
                worldCharacteristic.get(i).setActualAge(integer);
                integer = in.readInt();
                dataInputted += 4;
                worldCharacteristic.get(i).setWorldSize(integer);
            }
        }
        catch (Exception e)
        {
            logger.error("Error in WCHR section: ", e);
            return false;
        }
        return true;
    }

    private boolean inputGAME(LittleEndianDataInputStream in)
    {
        try
        {
            Integer integer = in.readInt();
            dataInputted += 4;
            //logger.info(integer);
            numScenarioProperties = integer;

            for (int i = 0; i < numScenarioProperties; i++)
            {
                scenarioProperty.add(new GAME(this));
                integer = in.readInt();
                dataInputted += 4;
                scenarioProperty.get(i).setDataLength(integer);
                integer = in.readInt();
                dataInputted += 4;
                scenarioProperty.get(i).setUseDefaultRules(integer);
                integer = in.readInt();
                dataInputted += 4;
                scenarioProperty.get(i).setDefaultVictoryConditions(integer);
                integer = in.readInt();
                dataInputted += 4;
                if (logger.isDebugEnabled())
                    logger.debug("Num playable civs: " + integer);
                scenarioProperty.get(i).setNumberOfPlayableCivs(integer);
                for (int j = 0; j < scenarioProperty.get(i).getNumberOfPlayableCivs(); j++)
                {
                    integer = in.readInt();
                    dataInputted += 4;
                    scenarioProperty.get(i).idOfPlayableCivs.add(integer);
                }
                integer = in.readInt();
                dataInputted += 4;
                scenarioProperty.get(i).setVictoryConditionsAndRules(integer);
                if (version.ordinal() >= civ3Version.PTW.ordinal()) {
                    integer = in.readInt();
                    dataInputted += 4;
                    scenarioProperty.get(i).setPlaceCaptureUnits(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    scenarioProperty.get(i).setAutoPlaceKings(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    scenarioProperty.get(i).setAutoPlaceVictoryLocations(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    scenarioProperty.get(i).setDebugMode(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    scenarioProperty.get(i).setUseTimeLimit(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    scenarioProperty.get(i).setBaseTimeUnit(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    scenarioProperty.get(i).setStartMonth(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    scenarioProperty.get(i).setStartWeek(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    scenarioProperty.get(i).setStartYear(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    scenarioProperty.get(i).setMinuteTimeLimit(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    scenarioProperty.get(i).setTurnTimeLimit(integer);
                    for (int j = 0; j < 7; j++)
                    {
                        integer = in.readInt();
                        dataInputted += 4;
                        scenarioProperty.get(i).setTurnsPerTimescalePart(j, integer);
                    }
                    for (int j = 0; j < 7; j++)
                    {
                        integer = in.readInt();
                        dataInputted += 4;
                        scenarioProperty.get(i).setTimeUnitsPerTurn(j, integer);
                    }
                    in.read(inputFiftyTwoHundred, 0, 5200);
                    dataInputted += 5200;
                    scenarioProperty.get(i).setScenarioSearchFolders(new String(inputFiftyTwoHundred, currentCharset));
                    if (version == civ3Version.CONQUESTS)
                    {
                        for (int j = 0; j < scenarioProperty.get(i).getNumberOfPlayableCivs(); j++)
                        {
                            integer = in.readInt();
                            dataInputted += 4;
                            scenarioProperty.get(i).civPartOfWhichAlliance.add(integer);
                        }
                        integer = in.readInt();
                        dataInputted += 4;
                        scenarioProperty.get(i).setVictoryPointLimit(integer);
                        integer = in.readInt();
                        dataInputted += 4;
                        scenarioProperty.get(i).setCityEliminationCount(integer);
                        integer = in.readInt();
                        dataInputted += 4;
                        scenarioProperty.get(i).setOneCityCultureWinLimit(integer);
                        integer = in.readInt();
                        dataInputted += 4;
                        scenarioProperty.get(i).setAllCitiesCultureWinLimit(integer);
                        integer = in.readInt();
                        dataInputted += 4;
                        scenarioProperty.get(i).setDominationTerrainPercent(integer);
                        integer = in.readInt();
                        dataInputted += 4;
                        scenarioProperty.get(i).setDominationPopulationPercent(integer);
                        integer = in.readInt();
                        dataInputted += 4;
                        scenarioProperty.get(i).setWonderVP(integer);
                        integer = in.readInt();
                        dataInputted += 4;
                        scenarioProperty.get(i).setDefeatingOpposingUnitVP(integer);
                        integer = in.readInt();
                        dataInputted += 4;
                        scenarioProperty.get(i).setAdvancementVP(integer);
                        integer = in.readInt();
                        dataInputted += 4;
                        scenarioProperty.get(i).setCityConquestVP(integer);
                        integer = in.readInt();
                        dataInputted += 4;
                        scenarioProperty.get(i).setVictoryPointVP(integer);
                        integer = in.readInt();
                        dataInputted += 4;
                        scenarioProperty.get(i).setCaptureSpecialUnitVP(integer);
                        integer = in.readInt();
                        dataInputted += 4;
                        scenarioProperty.get(i).setQuestionMark1(integer);
                        inputByte = in.readByte();
                        dataInputted++;
                        scenarioProperty.get(i).setQuestionMark2(inputByte);
                        in.read(inputTwoFiftySix, 0, 256);
                        dataInputted += 256;
                        scenarioProperty.get(i).setAlliance0(new String(inputTwoFiftySix, currentCharset));
                        in.read(inputTwoFiftySix, 0, 256);
                        dataInputted += 256;
                        scenarioProperty.get(i).setAlliance1(new String(inputTwoFiftySix, currentCharset));
                        in.read(inputTwoFiftySix, 0, 256);
                        dataInputted += 256;
                        scenarioProperty.get(i).setAlliance2(new String(inputTwoFiftySix, currentCharset));
                        in.read(inputTwoFiftySix, 0, 256);
                        dataInputted += 256;
                        scenarioProperty.get(i).setAlliance3(new String(inputTwoFiftySix, currentCharset));
                        in.read(inputTwoFiftySix, 0, 256);
                        dataInputted += 256;
                        scenarioProperty.get(i).setAlliance4(new String(inputTwoFiftySix, currentCharset));
                        for (int j = 0; j < 5; j++)
                        {   //logger.info(j);
                            integer = in.readInt();
                            dataInputted += 4;
                            //logger.info("Integer: " + integer);
                            scenarioProperty.get(i).warWith0.add(integer);
                            integer = in.readInt();
                            dataInputted += 4;
                            //logger.info("Integer: " + integer);
                            scenarioProperty.get(i).warWith1.add(integer);
                            integer = in.readInt();
                            dataInputted += 4;
                            //logger.info("Integer: " + integer);
                            scenarioProperty.get(i).warWith2.add(integer);
                            integer = in.readInt();
                            dataInputted += 4;
                            //logger.info("Integer: " + integer);
                            scenarioProperty.get(i).warWith3.add(integer);
                            integer = in.readInt();
                            dataInputted += 4;
                            //logger.info("Integer: " + integer);
                            scenarioProperty.get(i).warWith4.add(integer);
                        }
                        integer = in.readInt();
                        dataInputted += 4;
                        scenarioProperty.get(i).setAllianceVictoryType(integer);
                        in.read(inputTwoSixty, 0, 260);
                        dataInputted += 260;
                        scenarioProperty.get(i).setPlaugeName(new String(inputTwoSixty, currentCharset));
                        inputByte = in.readByte();
                        dataInputted++;
                        scenarioProperty.get(i).setPermitPlagues(inputByte);
                        integer = in.readInt();
                        dataInputted += 4;
                        scenarioProperty.get(i).setPlagueEarliestStart(integer);
                        integer = in.readInt();
                        dataInputted += 4;
                        scenarioProperty.get(i).setPlagueVariation(integer);
                        integer = in.readInt();
                        dataInputted += 4;
                        scenarioProperty.get(i).setPlagueDuration(integer);
                        integer = in.readInt();
                        dataInputted += 4;
                        scenarioProperty.get(i).setPlagueStrength(integer);
                        integer = in.readInt();
                        dataInputted += 4;
                        scenarioProperty.get(i).setPlagueGracePeriod(integer);
                        integer = in.readInt();
                        dataInputted += 4;
                        scenarioProperty.get(i).setPlagueMaxOccurance(integer);
                        integer = in.readInt();
                        dataInputted += 4;
                        scenarioProperty.get(i).setQuestionMark3(integer);
                        in.read(inputTwoSixty, 0, 260);
                        dataInputted += 260;
                        scenarioProperty.get(i).setUnknown(new String(inputTwoSixty, currentCharset));
                        integer = in.readInt();
                        dataInputted += 4;
                        scenarioProperty.get(i).setRespawnFlagUnits(integer);
                        inputByte = in.readByte();
                        dataInputted++;
                        scenarioProperty.get(i).setCaptureAnyFlag(inputByte);
                        integer = in.readInt();
                        dataInputted += 4;
                        scenarioProperty.get(i).setGoldForCapture(integer);
                        inputByte = in.readByte();
                        dataInputted++;
                        scenarioProperty.get(i).setMapVisible(inputByte);
                        inputByte = in.readByte();
                        dataInputted++;
                        scenarioProperty.get(i).setRetainCulture(inputByte);
                        integer = in.readInt();
                        dataInputted += 4;
                        scenarioProperty.get(i).setQuestionMark4(integer);
                        integer = in.readInt();
                        dataInputted += 4;
                        scenarioProperty.get(i).setEruptionPeriod(integer);
                        if (majorVersionNumber == 12 && minorVersionNumber >= 7)
                        {
                            integer = in.readInt();
                            dataInputted += 4;
                            scenarioProperty.get(i).setMpBaseTime(integer);
                            integer = in.readInt();
                            dataInputted += 4;
                            scenarioProperty.get(i).setMpCityTime(integer);
                            integer = in.readInt();
                            dataInputted += 4;
                            scenarioProperty.get(i).setMpUnitTime(integer);
                        }
                        else
                        {   //set the default values the same as what we'd have on a
                            //brand-new scenario
                            scenarioProperty.get(i).setMpBaseTime(24);
                            scenarioProperty.get(i).setMpCityTime(1);
                            scenarioProperty.get(i).setMpUnitTime(3);
                        }
                    }
                    else if (version == civ3Version.PTW)
                    {
                        if (minorVersionNumber >= 19)
                        {
                            integer = in.readInt();
                            dataInputted += 4;
                            scenarioProperty.get(i).setRevealMap(integer);
                        }
                        scenarioProperty.get(i).convertFromPTWToConquests();
                    }
                }
                else {
                    scenarioProperty.get(i).convertFromVanillaToPTW();                    
                    scenarioProperty.get(i).convertFromPTWToConquests();
                }
                //logger.info(scenarioProperty.get(i).toString());
                //logger.info();
                scenarioProperty.get(i).trim();
                scenarioProperty.get(i).extractEnglish();
            }
        }
        catch (Exception e)
        {
            logger.error("Error in GAME header: ",e);
            return false;
        }
        return true;
    }

    private boolean inputLEAD(LittleEndianDataInputStream in)
    {
        try
        {
            hasCustomPlayerData = true;
            //logger.info(new String(inputFour, currentCharset));
            //logger.info(integer);
            Integer integer = in.readInt();
            dataInputted += 4;
            numPlayers = integer;

            for (int i = 0; i < numPlayers; i++)
            {
                player.add(new LEAD(this));
                integer = in.readInt();
                dataInputted += 4;
                //player.get(i).setDataLength(integer);
                integer = in.readInt();
                dataInputted += 4;
                player.get(i).setCustomCivData(integer);
                integer = in.readInt();
                dataInputted += 4;
                player.get(i).setHumanPlayer(integer);
                in.read(inputThirtyTwo, 0, 32);
                dataInputted += 32;
                player.get(i).setLeaderName(new String(inputThirtyTwo, currentCharset));
                integer = in.readInt();
                dataInputted += 4;
                player.get(i).setQuestionMark1(integer);
                integer = in.readInt();
                dataInputted += 4;
                player.get(i).setQuestionMark2(integer);
                int numStartUnits = in.readInt();
                dataInputted += 4;
                for (int j = 0; j < numStartUnits; j++)
                {
                    int unitCount = in.readInt();
                    dataInputted += 4;
                    int unitType = in.readInt();
                    dataInputted += 4;
                    
                    if (hasCustomRules) {
                        player.get(i).addStartingUnitType(unit.get(unitType), unitCount);
                    }
                    else {
                        player.get(i).addStartingUnitType(unitType, unitCount);
                    }
                }
                integer = in.readInt();
                dataInputted += 4;
                player.get(i).setGenderOfLeaderName(integer);
                int numStartTechs = in.readInt();
                dataInputted += 4;
                for (int j = 0; j < numStartTechs; j++)
                {
                    integer = in.readInt();
                    dataInputted += 4;
                    player.get(i).startingTechnologyInt.add(integer);
                }
                integer = in.readInt();
                dataInputted += 4;
                player.get(i).setDifficulty(integer);
                integer = in.readInt();
                dataInputted += 4;
                player.get(i).setInitialEra(integer);
                integer = in.readInt();
                dataInputted += 4;
                player.get(i).setStartCash(integer);
                integer = in.readInt();
                dataInputted += 4;
                player.get(i).setGovernment(integer);
                integer = in.readInt();
                dataInputted += 4;
                player.get(i).setCiv(integer);
                integer = in.readInt();
                dataInputted += 4;
                player.get(i).setColor(integer);
                if (version.ordinal() >= version.PTW.ordinal()) {   //conquests?
                    integer = in.readInt();
                    dataInputted += 4;
                    player.get(i).setSkipFirstTurn(integer);
                    integer = in.readInt();
                    dataInputted += 4;
                    player.get(i).setQuestionMark3(integer);
                    inputByte = in.readByte();
                    dataInputted++;
                    player.get(i).setStartEmbassies(inputByte);
                }
                else {
                    //In Vanilla, "Any" difficulty is indicated by it being > the max
                    //difficulty.  In Conquests, this is indicated by the value -2.
                    if (convertToConquests == 1) {
                        if (player.get(i).getDifficulty() >= this.difficulties.size()) {
                            player.get(i).setDifficulty(-2);
                        }
                    }
                }
                //logger.info(player.get(i).toString());
                player.get(i).trim();
            }
            for (LEAD leader : player) {
                leader.setStartingTechLinks();
            }
        }
        catch (Exception e)
        {
            logger.error("Error in LEAD header: ", e);
            return false;
        }
        return true;
    }
    
    public void deletePlayer(int playerId) {
        deletePlayer(playerId, true);
    }
    
    /**
     * Safely deletes a player.  Removes any SLOCs/Cities/Units/colonies owned
     * by that player.  May explore an option to set the owner to none instead,
     * or make that configurable.
     * @param playerId The ID of the player being deleted
     * @param recalcTileOwners True if the tile owners should be recalculated.  Generally they should
     * be, but if multiple players are being deleted, it will be more efficient to send false here,
     * and call it once after all players being deleted are deleted.
     */
    public void deletePlayer(int playerId, boolean recalcTileOwners) {
        for (int i = 0; i < startingLocation.size(); i++) {
            SLOC sloc = startingLocation.get(i);
            if (sloc.getOwnerType() == CITY.OWNER_PLAYER && sloc.getOwner() == playerId) {
                startingLocation.remove(i);
                i--;
            }
            else if (sloc.getOwnerType() == CITY.OWNER_PLAYER && sloc.getOwner() > playerId) {
                sloc.setOwner(sloc.getOwner() - 1);
            }
        }
        for (int i = 0; i < city.size(); i++) {
            CITY theCity = city.get(i);
            if (theCity.getOwnerType() == CITY.OWNER_PLAYER && theCity.getOwner() == playerId) {
                deleteCity(i, false);
                i--;
            }
            else if (theCity.getOwnerType() == CITY.OWNER_PLAYER && theCity.getOwner() > playerId) {
                theCity.setOwner(theCity.getOwner() - 1);
            }
        }
        for (int i = 0; i < mapUnit.size(); i++) {
            UNIT unitOnMap = mapUnit.get(i);
            if (unitOnMap.getOwnerType() == CITY.OWNER_PLAYER && unitOnMap.getOwner() == playerId) {
                mapUnit.remove(i);
                i--;
            }
            else if (unitOnMap.getOwnerType() == CITY.OWNER_PLAYER && unitOnMap.getOwner() > playerId) {
                unitOnMap.setOwner(unitOnMap.getOwner() - 1);
            }
        }
        //Re-initialize the lists of units on tiles.  Since there are likely many units affected, doing this
        //once after all deletions rather than incrementally updating potentially dozens of times.
        for (int i = 0; i < tile.size(); i++) {
            tile.get(i).unitsOnTile.clear();
        }
        for (int i = 0; i < mapUnit.size(); i++) {
            UNIT unitOnMap = mapUnit.get(i);
            tile.get(this.calculateTileIndex(unitOnMap.getX(), unitOnMap.getY())).unitsOnTile.add(i);
        }
        for (int i = 0; i < tile.size(); i++) {
            tile.get(i).calculateUnitWithBestDefence();
        }
        
        for (int i = 0; i < colony.size(); i++) {
            CLNY theColony = colony.get(i);
            if (theColony.getOwnerType() == CITY.OWNER_PLAYER && theColony.getOwner() == playerId) {
                colony.remove(i);
                i--;
                //Also 
            }
            else if (theColony.getOwnerType() == CITY.OWNER_PLAYER && theColony.getOwner() > playerId) {
                theColony.setOwner(theColony.getOwner() - 1);
            }
        }
        player.remove(playerId);
        
        //Recalc tile owners after all cities are deleted, rather than each one, since a lot will be deleted
        //If multiple players being deleted, this will be skipped until the end of the batch deletion.
        //Also must be after the player is deleted, as otherwise, if a player in the middle of the list
        //is deleted, the search for border color will grab the wrong player's color (b/c of non-deleted
        //player).
        if (recalcTileOwners) {
            calculateTileOwners();
        }
    }
    
    public void deleteCity(int cityIndex) {
        deleteCity(cityIndex, true);
    }
    
    /**
     * Deletes a city and recalculates everything required for a city-deletion.  This is taken nearly
     * verbatim from the version in MapTab.java; that one is triggered when the city-deletion functionality
     * on the right-side panel is invoked, and was added in the summer of 2011.
     * @param cityIndex The index of the city being deleted
     * @param recalcTileOwners Whether tile owners should be recalculated.  Generally this should be true,
     * but if you are calling this as part of a batch operation deleting multiple cities (e.g. removing a
     * player and all their cities), it is more efficient to send this as false, and invoke the recalculation
     * once after the batch operation finishes.
     */
    public void deleteCity(int cityIndex, boolean recalcTileOwners) {
        
        //Remove the city from tile influences, *and* decrement indices for all
        //cities to avoid index out of bounds exceptions
        for (int t = 0; t < tile.size(); t++) {
            TILE theTile = tile.get(t);
            int uIndex = 0;
            int numIterations = theTile.citiesWithInfluence.size(); //so it doesn't decrease when removing items from the list
            for (int u = 0; u < numIterations; u++) {
                if (theTile.citiesWithInfluence.get(uIndex) > cityIndex) {
                    theTile.citiesWithInfluence.set(uIndex, theTile.citiesWithInfluence.get(uIndex) - 1);
                }
                else if (theTile.citiesWithInfluence.get(uIndex) == cityIndex) {
                    theTile.citiesWithInfluence.remove(uIndex);
                    uIndex--;
                }
                uIndex++;
            }
        }
        
        //Java is stupid and thinks I want the int -1 if I just put the -1
        //in the setCity call
        short s = -1;
        city.remove(cityIndex);
        //Now update the pointers
        for (int i = 0; i < tile.size(); i++)
        {
            if (tile.get(i).getCity() == cityIndex) {
                tile.get(i).setCity(s);
            }
            if (tile.get(i).getCity() > cityIndex)
                tile.get(i).setCity((short)(tile.get(i).getCity() - 1));
        }
        if (recalcTileOwners) {
            try{
                calculateTileOwners();
            }
            catch(IndexOutOfBoundsException e)
            {
                logger.error("Exception while deleting city!", e);
            }
        }
    }

    public boolean hasCustomRules()
    {
        return hasCustomRules;
    }
    
    public void setCustomRules(boolean customRules) {
        this.hasCustomRules = customRules;
    }

    public boolean hasCustomMap()
    {
        return hasCustomMap;
    }
    
    public void setCustomMap(boolean customMap)
    {
        this.hasCustomMap = customMap;
    }

    public boolean hasCustomPlayerData()
    {
        return hasCustomPlayerData;
    }
    
    public void setCustomPlayerData(boolean customPlayerData) {
        this.hasCustomPlayerData = customPlayerData;
    }

    public int findInstance(Section section, String name)
    {
        List list = getSection(section);
        for (int i = 0; i < numSection(section); i++)
        {
            BIQSection instance = (BIQSection)list.get(i);
            if (instance.getProperty("Name").equals(name))
            {
                return i;
            }
        }
        return -1;
    }
    public int findUnit(String name)
    {
        for (int i = 0; i < numUnits; i++)
        {
            if (unit.get(i).getName().equals(name))
            {
                return i;
            }
        }
        return -1;
    }
    public int findPlayer(int player)
    {
        for (int i = 0; i < numPlayers; i++)
        {
            if (this.player.get(i).getCiv() == player)
            {
                return i;
            }
        }
        return -1;
    }
    
    public boolean outputBIQ(File file) {
        try
        {
            //Buffered output speeds up output by a factor of about 60 - even more than it speeds up input!
            LittleEndianDataOutputStream out = new LittleEndianDataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            boolean result = outputBIQ(out, false);
            out.close();
            return result;
        }
        catch(IOException ex) {
            logger.error("Exception opening file", ex);
            return false;
        }
    }

    public boolean outputBIQ(LittleEndianDataOutputStream out, boolean isEmbeddedInSAV)
    {
        //May need to recalculate values
        if (logger.isDebugEnabled())
            logger.debug("in outputBIQ");
        try
        {

            if (logger.isDebugEnabled())
                logger.debug("about to export BIQ file");
            //put the goods array back into bytes (rather than booleans)
            for (int i = 0; i < terrain.size(); i++)
            {
                terrain.get(i).storeGoods();
            }

            if (isEmbeddedInSAV) {
                out.writeBytes("BICQ");
            }
            else {
                out.writeBytes("BICX");
            }

            out.writeBytes("VER#");

            writeInt(1, out);

            writeInt(720, out);
            writeInt(0, out);
            writeInt(0, out);
            /**
             * Always write out as version 12.08 BIQ.
             * There is no compelling reason not to, Firaxis's editor does this,
             * and if multiple versions were supported, the UI would have to
             * change to reflect capabilities added in 12.08 (12.07, etc.)
             */
            majorVersionNumber = 12;
            minorVersionNumber = 8;
            version = version.CONQUESTS;
            writeInt(majorVersionNumber, out);
            writeInt(minorVersionNumber, out);
            writeString(description, 640, out);
            writeString(title, 64, out);



            if (hasCustomRules)
            {
                outputBLDG(out);
                outputCTZN(out);
                outputCULT(out);
                outputDIFF(out);
                outputERAS(out);
                outputESPN(out);
                outputEXPR(out);
                if (isEmbeddedInSAV) {
                    outputFLAV(out);
                }
                outputGOOD(out);
                outputGOVT(out);
                outputRULE(out);
                outputPRTO(out);
                outputRACE(out);
                outputTECH(out);
                outputTFRM(out);
                out.flush();
                outputTERR(out);
                outputWSIZ(out);
                if (!isEmbeddedInSAV) {
                    outputFLAV(out);
                }
                if (logger.isDebugEnabled())
                    logger.debug("Finished exporting FLAV section");
            }//end if hasCustomRules
            //custom world map
            if (hasCustomMap)
            {
                outputWCHR(out);
                outputWMAP(out);
                outputTILE(out);
                outputCONT(out);
                outputSLOC(out);
                outputCITY(out);
                outputUNIT(out);
                outputCLNY(out);
                //end world map section
            }
            //always have scenario properties
            if (logger.isDebugEnabled())
                logger.debug("About to write GAME data");
            outputGAME(out);

            if (logger.isDebugEnabled())
                logger.debug("Finished exporting GAME section");
            if (hasCustomPlayerData)
            {
                outputLEAD(out);
                if (logger.isDebugEnabled())
                    logger.debug("Finished exporting LEAD section");
            }
            //end of file
        }
        catch (Exception e)
        {
            logger.error("Exception while outputting file", e);
            return false;
        }
        return true;
    }

    public void writeInt(int number, LittleEndianDataOutputStream out)
    {
        try
        {
            out.writeInt(number);
        }
        catch (Exception e)
        {
            logger.error("Error while writing an integer: " + e);
        }
    }

    public void writeShort(short number, LittleEndianDataOutputStream out)
    {
        try
        {
            out.writeShort(number);
        }
        catch (Exception e)
        {
            logger.error(e);
        }
    }

    public void writeString(String string, int length, LittleEndianDataOutputStream out)
    {
        //Got a bug log report with a crash on the len line, apparently string was null
        //That was with units, which do default to "" rather than null.
        //Nevertheless, taking a precaution here
        if (string == null)
            string = "";
        int len = string.length();
        int numExtraBytes = length - len;
        try
        {
            byte[] strBuff = string.getBytes(currentCharset);
            out.write(strBuff);
            for (int i = 0; i < numExtraBytes; i++)
            {
                out.writeByte(0);
            }
        }
        catch (Exception e)
        {
            logger.error("Error while writing a string: " + e);
        }
    }

    private int inputHeader(LittleEndianDataInputStream in, String toMatch) throws HeaderFailedException
    {
        try{
            in.read(inputFour, 0, 4);
            dataInputted += 4;
            String temp = new String(inputFour, currentCharset);
            if (!(temp.equals(toMatch)))
            {
                String f = "";
                for (int i = 0; i < 4; i++)
                    f = f + Integer.toHexString(Character.getNumericValue(temp.charAt(i))).substring(6);
                String e = "Could not input file - failed at " + toMatch + " header.  Header is " + temp + "; bytes inputted: " + dataInputted + ", or " + f + " in hex.  Data inputted: " + dataInputted;
                logger.error(e);
                throw new HeaderFailedException(e);
            }
            //logger.info(temp);
            //number of flavors
            Integer integer = in.readInt();
            dataInputted += 4;
            return integer;
        }
        catch(java.io.IOException e)
        {
            throw new HeaderFailedException("IOException in header for " + toMatch, e);
        }
    }

    /**
     * AKA InputSAV
     * @param file
     * @return 
     */
    public boolean inputBIQFromScenario(File file)
    {
        long fileLength = file.length();
        dataInputted = 0;              //amount of data read in
        try
        {
            LittleEndianDataInputStream inFile = new LittleEndianDataInputStream(new BufferedInputStream(new FileInputStream(file)));
            //the buffer will be able to contain up to 4 GB theoretically - the loss of precision is irrelevant
            byte[] buffer = new byte[(int) file.length()];
            inFile.readFully(buffer);
            inFile.close(); //Once we have the file in memory, we can unlock it
            LittleEndianDataInputStream in = new LittleEndianDataInputStream(new ByteArrayInputStream(buffer));
            //
            //Find the BICQ
            int bytesInput = 0;
            boolean bicqFound = false;
            boolean loopForI = false;
            byte[] input = new byte[4];
            while (!(bicqFound))
            {
                in.read(input, 0, 1);
                bytesInput++;
                String temp = new String(input, currentCharset);
                if (temp.charAt(0) == 'B')
                {
                    in.read(input, 1, 1);
                    bytesInput++;
                    temp = new String(input, currentCharset);
                    loopForI = true;
                    while (loopForI)
                    {
                        in.read(input, 2, 1);
                        bytesInput++;
                        temp = new String(input, currentCharset);
                        if (temp.charAt(1) == 'I')
                        {

                            if (temp.charAt(2) == 'C')
                            {
                                in.read(input, 3, 1);
                                bytesInput++;
                                temp = new String(input, currentCharset);
                                if (temp.charAt(3) == 'Q')
                                {
                                    bicqFound = true;
                                }
                                else{
                                    input[0] = '\0';
                                    input[1] = '\0';
                                    input[2] = '\0';
                                    input[3] = '\0';
                                }
                            }
                            else{
                                input[0] = '\0';
                                input[1] = '\0';
                                input[2] = '\0';
                            }
                            loopForI = false;
                        }
                        else if (temp.charAt(1) == 'B') //might be coming up yet
                        {
                            input[0] = input[1];    //move the B
                            input[1] = '\0';    //set to null
                        }
                        else    //no leads at this point, clear the buffer
                        {
                            input[0] = '\0';
                            input[1] = '\0';
                            loopForI = false;
                        }
                    }
                }
                 else{
                    if (logger.isDebugEnabled())
                        logger.debug("Not B: " + temp);
                    if (bytesInput > 1000)  //try decompressing it
                    {
                        logger.info("Detected compressed file");
                        String[] decomQuery = {"java", "-jar", "./bin/BIQDecompressor.jar", file.getCanonicalPath(), "._tmp.biq"};
                        Process dcp = Runtime.getRuntime().exec(decomQuery);
                        //wait until it's decompressed before continuing or we'll blow up
                        dcp.waitFor();
                        return inputBIQFromScenario(new File("._tmp.biq"));
                    }
                 }
            }//found
            //
            //The below was just inputted
            //in.read(inputFour, 0, 4);
            //dataInputted+=4;
            //temp = new String(inputFour, currentCharset);  //256-character ASCII
            //logger.info(temp);
            
            //6/12 - ALWAYS set version to Conquests.  Not sure quite how it was working before...
            version = version.CONQUESTS;

            in.read(inputFour, 0, 4);
            dataInputted += 4;
            String temp = new String(inputFour, currentCharset); //256-character ASCII
            //logger.info(temp);

            //number of headers (should be 1)
            Integer integer = in.readInt();
            dataInputted += 4;
            //logger.info(integer);

            //FOR EACH HEADER - not sure what happens with more than one
            //length of header (should be 720)
            integer = in.readInt();
            dataInputted += 4;
            //logger.info(integer);

            //zero
            integer = in.readInt();
            dataInputted += 4;
            //logger.info(integer);

            //zero
            integer = in.readInt();
            dataInputted += 4;
            //logger.info(integer);

            //BIC major version number
            integer = in.readInt();
            dataInputted += 4;
            majorVersionNumber = integer;
            //logger.info(integer);

            //BIC minor version number
            integer = in.readInt();
            dataInputted += 4;
            minorVersionNumber = integer;
            //logger.info(integer);

            //next comes a String of length *up to* 640 characters.  Need to be alert for a 0x00 that ends it
            //it appears that all 640 characters are present in the .BIQ anyway for some reason
            in.read(inputSixForty, 0, 640);
            dataInputted += 640;
            description = new String(inputSixForty, currentCharset); //256-character ASCII
            //logger.info(temp);
            //logger.info("title:");
            //title
            in.read(inputSixtyFour, 0, 64);
            dataInputted += 64;
            title = new String(inputSixtyFour, currentCharset); //256-character ASCII
            //logger.info(temp);

            //BLDG
            in.read(inputFour, 0, 4);
            dataInputted += 4;
            temp = new String(inputFour, currentCharset);
            if (temp.equals("BLDG"))
            {
                hasCustomRules = true;
                //logger.info(temp);
                //logger.info(temp);
                //number of buildings
                //input building, then ctzn, etc.]
                inputBLDG(in);
                inputCTZN(in);
                inputCULT(in);
                inputDIFF(in);
                inputERAS(in);
                inputESPN(in);
                inputEXPR(in);
                inputFLAV(in);
                inputGOOD(in);
                inputGOVT(in);
                inputRULE(in);
                inputPRTO(in);
                inputRACE(in);
                inputTECH(in);
                inputTFRM(in);
                inputTERR(in);
                inputWSIZ(in);

                if (!this.performRulePostProcessingFromScenario()) {
                    return false;
                }

                //If there's more, it's either World Map OR Custom Player Data next
                //OR neither of the above and it's GAME time
                if (logger.isDebugEnabled())
                    logger.debug("at world map/game section; " + dataInputted + " bytes inputted.");
                in.read(inputFour, 0, 4);
                dataInputted += 4;
                temp = new String(inputFour, currentCharset);
            }
            else
            {
                hasCustomRules = false;
            }
            if (temp.equals("WCHR"))   //go ahead and do world map
            {   //else, game data comes next anyways
                hasCustomMap = true;
                if (logger.isDebugEnabled())
                    logger.debug("custom map");
                //logger.info(temp);


                inputWCHR(in);
                inputWMAP(in);
                inputTILE(new LittleEndianDataInputStream[]{in});
                inputCONT(new LittleEndianDataInputStream[]{in});
                inputSLOC(new LittleEndianDataInputStream[]{in});
                inputCITY(in);
                inputUNIT(in);
                inputCLNY(in);




                if (logger.isDebugEnabled())
                    logger.debug("finished map data");
                //if wmap was skipped, temp is already "GAME"
                //but it wasn't, so input "GAME"
                in.read(inputFour, 0, 4);
                dataInputted += 4;
                temp = new String(inputFour, currentCharset);
                
                performMapPostProcessing();
            }   //inherent else
            //if there was no map, the inherent else is taken and you end up here
            //if there was a map, that's done now, and you have to import stuff here too

            //GAME Section
            //temp will already be ready
            if (logger.isDebugEnabled())
                logger.debug("Should be finished now");
            if (!(temp.equals("GAME")))
            {
                JOptionPane.showMessageDialog(null, "Could not input file - failed at GAME header.", "File input failed.", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            //logger.info(temp);

            //number of scenario properties
            inputGAME(in);

            if (logger.isDebugEnabled())
            {
                logger.debug(fileLength);
                logger.debug(dataInputted);
            }
            if (fileLength == dataInputted)
            {
                return true;
            }   //inherent else - if otherwise, input custom player data

            //TO BE ADDED
            //Problem: Lead section format in save may not match
            //LEAD Section - MAY NOT EXIST!!!!!!!!!!!!!!!!!!!!!!!!!!
            //inputLEAD();
            if (logger.isDebugEnabled())
                logger.debug("finished custom player data");
            if (logger.isInfoEnabled())
                logger.info("final input:  " + dataInputted);
            if (fileLength == dataInputted)
            {
                return true;
            }
            else
            {
                return true;
            }
        }
        catch (java.io.EOFException e)
        {
            logger.error("file size: " + fileLength);
            logger.error("bytes read at time of crash: " + dataInputted);
            //this means that the end of the file has been reached
            //and there is no LEAD section
            logger.error(e.getCause());
            logger.error(e.toString());
            logger.error(e.getClass());
            logger.error(e);
            return false;
        }
        catch (Exception e)
        {
            logger.error("ERROR:  ", e);
            return false;
        }
        //false case
    }

    private boolean performRulePostProcessingFromScenario() throws HeadlessException {
        //Extract Englishes
        try {
            for (int i = 0; i < numBuildings; i++)
            {
                if (logger.isTraceEnabled())
                    logger.trace("numFlavors: " + numFlavors);
                buildings.get(i).extractEnglish(numFlavors);
            }
            for (int i = 0; i < numTechnologies; i++)
            {
                technology.get(i).extractEnglish(numFlavors);
            }
            for (int i = 0; i < numCivilizations; i++)
            {
                civilization.get(i).extractEnglish(numFlavors);
            }
            for (int i = 0; i < numTerrains; i++)
            {
                terrain.get(i).simplifyGoods();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Could not input file - error extracting binary data on building/technology/civilization headers.", "File input failed.", JOptionPane.ERROR_MESSAGE);
            logger.error("Error on extract Englishes", e);
            return false;
        }
        return true;
    }
    
    /**
     * Does any post-processing/cleanup tasks on the map.
     */
    private void performMapPostProcessing() {
        //Repair SLOCs that are in the SLOC collection, but not on tiles
        for (SLOC sloc : this.startingLocation) {
            int tileIndex = this.calculateTileIndex(sloc.getX(), sloc.getY());
            TILE correspondingTile = tile.get(tileIndex);
            if (!correspondingTile.isStartLocation()) {
                logger.info("Repairing starting location at " + sloc.getX() + ", " + sloc.getY() + ".  Save the BIQ to make this repair permanent.");
                correspondingTile.setStartingLocation(true);
            }
        }
        //Add SLOCs for ancient BIC files that predate SLOC
        if (version == civ3Version.VANILLA && (majorVersionNumber < 4 || (majorVersionNumber == 4 && minorVersionNumber < 1))) {
            for (TILE mapTile : this.tile) {
                if (mapTile.isStartLocation()) {
                    SLOC newSLOC = new SLOC(this);
                    newSLOC.setX(mapTile.getXPos());
                    newSLOC.setY(mapTile.getYPos());
                    newSLOC.setOwnerType(CITY.OWNER_NONE);
                    this.startingLocation.add(newSLOC);
                }
            }
        }
    }

    private void outputBLDG(LittleEndianDataOutputStream out)
    {
        try{
            out.writeBytes("BLDG");
            writeInt(buildings.size(), out);
            for (int i = 0; i < buildings.size(); i++)
            {
                buildings.get(i).createBinary();
                if (logger.isDebugEnabled())
                    logger.debug("Building " + buildings.get(i).getName() + " data length : " + buildings.get(i).getDataLength());
                writeInt(buildings.get(i).getDataLength(), out);
                writeString(buildings.get(i).getDescription(), 64, out);
                writeString(buildings.get(i).getName(), 32, out);
                writeString(buildings.get(i).getCivilopediaEntry(), 32, out);
                writeInt(buildings.get(i).getDoublesHappiness(), out);
                writeInt(buildings.get(i).getGainInEveryCity(), out);
                writeInt(buildings.get(i).getGainOnContinent(), out);
                writeInt(buildings.get(i).getReqImprovement(), out);
                writeInt(buildings.get(i).getCost(), out);
                writeInt(buildings.get(i).getCulture(), out);
                writeInt(buildings.get(i).getBombardDefence(), out);
                writeInt(buildings.get(i).getNavalBombardDefence(), out);
                writeInt(buildings.get(i).getDefenceBonus(), out);
                writeInt(buildings.get(i).getNavalDefenceBonus(), out);
                writeInt(buildings.get(i).getMaintenanceCost(), out);
                writeInt(buildings.get(i).getHappyAll(), out);
                writeInt(buildings.get(i).getHappy(), out);
                writeInt(buildings.get(i).getUnhappyAll(), out);
                writeInt(buildings.get(i).getUnhappy(), out);
                writeInt(buildings.get(i).getNumReqBuildings(), out);
                writeInt(buildings.get(i).getAirPower(), out);
                writeInt(buildings.get(i).getNavalPower(), out);
                writeInt(buildings.get(i).getPollution(), out);
                writeInt(buildings.get(i).getProduction(), out);
                writeInt(buildings.get(i).getReqGovernment(), out);
                writeInt(buildings.get(i).getSpaceshipPart(), out);
                writeInt(buildings.get(i).getReqAdvance(), out);
                writeInt(buildings.get(i).getObsoleteBy(), out);
                writeInt(buildings.get(i).getReqResource1(), out);
                writeInt(buildings.get(i).getReqResource2(), out);
                writeInt(buildings.get(i).getImprovements(), out);
                writeInt(buildings.get(i).getOtherChar(), out);
                writeInt(buildings.get(i).getSmallWonderCharacteristics(), out);
                writeInt(buildings.get(i).getWonderCharacteristics(), out);
                writeInt(buildings.get(i).getArmiesRequired(), out);
                writeInt(buildings.get(i).getFlavors(), out);
                writeInt(buildings.get(i).getQuestionMark(), out);
                writeInt(buildings.get(i).getUnitProduced(), out);
                writeInt(buildings.get(i).getUnitFrequency(), out);
            }
        }
        catch(java.io.IOException e){
            logger.error("IOException", e);
        }
    }

    private void outputCTZN(LittleEndianDataOutputStream out)
    {
        try{
            out.writeBytes("CTZN");
            writeInt(citizens.size(), out);
            for (int i = 0; i < citizens.size(); i++)
            {
                writeInt(citizens.get(i).getDataLength(), out);
                writeInt(citizens.get(i).getDefaultCitizen(), out);
                writeString(citizens.get(i).getName(), 32, out);
                writeString(citizens.get(i).getCivilopediaEntry(), 32, out);
                writeString(citizens.get(i).getPluralName(), 32, out);
                writeInt(citizens.get(i).getPrerequisite(), out);
                writeInt(citizens.get(i).getLuxuries(), out);
                writeInt(citizens.get(i).getResearch(), out);
                writeInt(citizens.get(i).getTaxes(), out);
                writeInt(citizens.get(i).getCorruption(), out);
                writeInt(citizens.get(i).getConstruction(), out);
            }
        }
        catch(java.io.IOException e){
            logger.error("IOException", e);
        }
    }

    private void outputCULT(LittleEndianDataOutputStream out)
    {
        try{
            out.writeBytes("CULT");
            writeInt(culture.size(), out);
            for (int i = 0; i < culture.size(); i++)
            {
                writeInt(culture.get(i).getDataLength(), out);
                writeString(culture.get(i).getName(), 64, out);
                writeInt(culture.get(i).getPropagandaSuccess(), out);
                writeInt(culture.get(i).getCultRatioPercent(), out);
                writeInt(culture.get(i).getRatioDenominator(), out);
                writeInt(culture.get(i).getRatioNumerator(), out);
                writeInt(culture.get(i).getInitResistanceChance(), out);
                writeInt(culture.get(i).getContinuedResistanceChance(), out);
            }
        }
        catch(java.io.IOException e){
            logger.error("IOException", e);
        }
    }
    private void outputDIFF(LittleEndianDataOutputStream out)
    {
        try{
            out.writeBytes("DIFF");
            writeInt(difficulties.size(), out);
            for (int i = 0; i < difficulties.size(); i++)
            {
                writeInt(difficulties.get(i).getDataLength(), out);
                writeString(difficulties.get(i).getName(), 64, out);
                writeInt(difficulties.get(i).getContentCitizens(), out);
                writeInt(difficulties.get(i).getMaxGovtTransition(), out);
                writeInt(difficulties.get(i).getAIDefenceStart(), out);
                writeInt(difficulties.get(i).getAIOffenceStart(), out);
                writeInt(difficulties.get(i).getExtraStart1(), out);
                writeInt(difficulties.get(i).getExtraStart2(), out);
                writeInt(difficulties.get(i).getAdditionalFreeSupport(), out);
                writeInt(difficulties.get(i).getBonusPerCity(), out);
                writeInt(difficulties.get(i).getAttackBarbariansBonus(), out);
                writeInt(difficulties.get(i).getCostFactor(), out);
                writeInt(difficulties.get(i).getPercentOptimal(), out);
                writeInt(difficulties.get(i).getAIAITrade(), out);
                writeInt(difficulties.get(i).getCorruptionPercent(), out);
                writeInt(difficulties.get(i).getMilitaryLaw(), out);
            }
        }
        catch(java.io.IOException e){
            logger.error("IOException", e);
        }
    }

    private void outputERAS(LittleEndianDataOutputStream out)
    {
        try{
            out.writeBytes("ERAS");
            writeInt(eras.size(), out);
            for (int i = 0; i < eras.size(); i++)
            {
                writeInt(eras.get(i).getDataLength(), out);
                writeString(eras.get(i).getName(), 64, out);
                writeString(eras.get(i).getCivilopediaEntry(), 32, out);
                writeString(eras.get(i).getResearcher1(), 32, out);
                writeString(eras.get(i).getResearcher2(), 32, out);
                writeString(eras.get(i).getResearcher3(), 32, out);
                writeString(eras.get(i).getResearcher4(), 32, out);
                writeString(eras.get(i).getResearcher5(), 32, out);
                writeInt(eras.get(i).getUsedResearcherNames(), out);
                writeInt(eras.get(i).getQuestionMark(), out);
            }
        }
        catch(java.io.IOException e){
            logger.error("IOException", e);
        }
    }

    private void outputESPN(LittleEndianDataOutputStream out)
    {
        try{
            out.writeBytes("ESPN");
            writeInt(espionage.size(), out);
            for (int i = 0; i < espionage.size(); i++)
            {
                writeInt(espionage.get(i).getDataLength(), out);
                writeString(espionage.get(i).getDescription(), 128, out);
                writeString(espionage.get(i).getName(), 64, out);
                writeString(espionage.get(i).getCivilopediaEntry(), 32, out);
                writeInt(espionage.get(i).getMissionPerformedBy(), out);
                writeInt(espionage.get(i).getBaseCost(), out);
            }
        }
        catch(java.io.IOException e){
            logger.error("IOException", e);
        }
    }

    private void outputEXPR(LittleEndianDataOutputStream out)
    {
        try{
            out.writeBytes("EXPR");
            writeInt(experience.size(), out);
            for (int i = 0; i < experience.size(); i++)
            {
                writeInt(experience.get(i).getDataLength(), out);
                writeString(experience.get(i).getName(), 32, out);
                writeInt(experience.get(i).getBaseHitPoints(), out);
                writeInt(experience.get(i).getRetreatBonus(), out);
            }
        }
        catch(java.io.IOException e){
            logger.error("IOException", e);
        }
    }

    private void outputGOOD(LittleEndianDataOutputStream out)
    {
        try{
            out.writeBytes("GOOD");
            writeInt(resource.size(), out);
            for (int i = 0; i < resource.size(); i++)
            {
                writeInt(resource.get(i).getDataLength(), out);
                writeString(resource.get(i).getName(), 24, out);
                writeString(resource.get(i).getCivilopediaEntry(), 32, out);
                writeInt(resource.get(i).getType(), out);
                writeInt(resource.get(i).getAppearanceRatio(), out);
                writeInt(resource.get(i).getDisapperanceProbability(), out);
                writeInt(resource.get(i).getIcon(), out);
                writeInt(resource.get(i).getPrerequisite(), out);
                writeInt(resource.get(i).getFoodBonus(), out);
                writeInt(resource.get(i).getShieldsBonus(), out);
                writeInt(resource.get(i).getCommerceBonus(), out);
            }
        }
        catch(java.io.IOException e){
            logger.error("IOException", e);
        }
    }

    private void outputGOVT(LittleEndianDataOutputStream out)
    {
        try{
            out.writeBytes("GOVT");
            writeInt(government.size(), out);
            for (int i = 0; i < government.size(); i++)
            {
                writeInt(government.get(i).getDataLength(), out);
                writeInt(government.get(i).getDefaultType(), out);
                writeInt(government.get(i).getTransitionType(), out);
                writeInt(government.get(i).getRequiresMaintenance(), out);
                writeInt(government.get(i).getQuestionMarkOne(), out);
                writeInt(government.get(i).getTilePenalty(), out);
                writeInt(government.get(i).getCommerceBonus(), out);
                writeString(government.get(i).getName(), 64, out);
                writeString(government.get(i).getCivilopediaEntry(), 32, out);
                writeString(government.get(i).getMaleRulerTitle1(), 32, out);
                writeString(government.get(i).getFemaleRulerTitle1(), 32, out);
                writeString(government.get(i).getMaleRulerTitle2(), 32, out);
                writeString(government.get(i).getFemaleRulerTitle2(), 32, out);
                writeString(government.get(i).getMaleRulerTitle3(), 32, out);
                writeString(government.get(i).getFemaleRulerTitle3(), 32, out);
                writeString(government.get(i).getMaleRulerTitle4(), 32, out);
                writeString(government.get(i).getFemaleRulerTitle4(), 32, out);
                writeInt(government.get(i).getCorruption(), out);
                writeInt(government.get(i).getImmuneTo(), out);
                writeInt(government.get(i).getDiplomatLevel(), out);
                writeInt(government.get(i).getSpyLevel(), out);
                writeInt(government.get(i).getNumberOfGovernments(), out);
                for (int j = 0; j < government.get(i).getNumberOfGovernments(); j++)
                {
                    writeInt(government.get(i).relations.get(j).getCanBribe(), out);
                    writeInt(government.get(i).relations.get(j).getBriberyModifier(), out);
                    writeInt(government.get(i).relations.get(j).getResistanceModifier(), out);
                }
                writeInt(government.get(i).getHurrying(), out);
                writeInt(government.get(i).getAssimilationChance(), out);
                writeInt(government.get(i).getDraftLimit(), out);
                writeInt(government.get(i).getMilitaryPoliceLimit(), out);
                writeInt(government.get(i).getRulerTitlePairsUsed(), out);
                writeInt(government.get(i).getPrerequisiteTechnology(), out);
                writeInt(government.get(i).getScienceCap(), out);
                writeInt(government.get(i).getWorkerRate(), out);
                writeInt(government.get(i).getQuestionMarkTwo(), out);
                writeInt(government.get(i).getQuestionMarkThree(), out);
                writeInt(government.get(i).getQuestionMarkFour(), out);
                writeInt(government.get(i).getFreeUnits(), out);
                writeInt(government.get(i).getFreeUnitsPerTown(), out);
                writeInt(government.get(i).getFreeUnitsPerCity(), out);
                writeInt(government.get(i).getFreeUnitsPerMetropolis(), out);
                writeInt(government.get(i).getCostPerUnit(), out);
                writeInt(government.get(i).getWarWeariness(), out);
                writeInt(government.get(i).getXenophobic(), out);
                writeInt(government.get(i).getForceResettlement(), out);
            }
        }
        catch(java.io.IOException e){
            logger.error("IOException", e);
        }
    }

    private void outputRULE(LittleEndianDataOutputStream out)
    {
        try{
            out.writeBytes("RULE");
            writeInt(rule.size(), out);
            for (int i = 0; i < rule.size(); i++)
            {
                writeInt(rule.get(i).getDataLength(), out);
                writeString(rule.get(i).getTownName(), 32, out);
                writeString(rule.get(i).getCityName(), 32, out);
                writeString(rule.get(i).getMetropolisName(), 32, out);
                writeInt(rule.get(i).getNumSpaceshipParts(), out);
                for (int j = 0; j < rule.get(i).getNumSpaceshipParts(); j++)
                {
                    writeInt(rule.get(i).getNumberOfSpaceshipPartsRequired(j), out);
                }
                writeInt(rule.get(i).getAdvancedBarbarian(), out);
                writeInt(rule.get(i).getBasicBarbarian(), out);
                writeInt(rule.get(i).getBarbarianSeaUnit(), out);
                writeInt(rule.get(i).getCitiesForArmy(), out);
                writeInt(rule.get(i).getChanceOfRioting(), out);
                writeInt(rule.get(i).getDraftTurnPenalty(), out);
                writeInt(rule.get(i).getShieldCostInGold(), out);
                writeInt(rule.get(i).getFortressDefenceBonus(), out);
                writeInt(rule.get(i).getCitizensAffectedByHappyFace(), out);
                writeInt(rule.get(i).getQuestionMark1(), out);
                writeInt(rule.get(i).getQuestionMark2(), out);
                writeInt(rule.get(i).getForestValueInShields(), out);
                writeInt(rule.get(i).getShieldValueInGold(), out);
                writeInt(rule.get(i).getCitizenValueInShields(), out);
                writeInt(rule.get(i).getDefaultDifficultyLevel(), out);
                writeInt(rule.get(i).getBattleCreatedUnit(), out);
                writeInt(rule.get(i).getBuildArmyUnit(), out);
                writeInt(rule.get(i).getBuildingDefensiveBonus(), out);
                writeInt(rule.get(i).getCitizenDefensiveBonus(), out);
                writeInt(rule.get(i).getDefaultMoneyResource(), out);
                writeInt(rule.get(i).getChanceToInterceptAirMissions(), out);
                writeInt(rule.get(i).getChanceToInterceptStealthMissions(), out);
                writeInt(rule.get(i).getStartingTreasury(), out);
                writeInt(rule.get(i).getQuestionMark3(), out);
                writeInt(rule.get(i).getFoodConsumptionPerCitizen(), out);
                writeInt(rule.get(i).getRiverDefensiveBonus(), out);
                writeInt(rule.get(i).getTurnPenaltyForWhip(), out);
                writeInt(rule.get(i).getScout(), out);
                writeInt(rule.get(i).getSlave(), out);
                writeInt(rule.get(i).getRoadMovementRate(), out);
                writeInt(rule.get(i).getStartUnit1(), out);
                writeInt(rule.get(i).getStartUnit2(), out);
                writeInt(rule.get(i).getWLTKDMinimumPop(), out);
                writeInt(rule.get(i).getTownDefenceBonus(), out);
                writeInt(rule.get(i).getCityDefenceBonus(), out);
                writeInt(rule.get(i).getMetropolisDefenceBonus(), out);
                writeInt(rule.get(i).getMaxCity1Size(), out);
                writeInt(rule.get(i).getMaxCity2Size(), out);
                writeInt(rule.get(i).getQuestionMark4(), out);
                writeInt(rule.get(i).getFortificationsDefenceBonus(), out);
                writeInt(rule.get(i).getNumCulturalLevels(), out);
                for (int j = 0; j < rule.get(i).getNumCulturalLevels(); j++)
                {
                    writeString(rule.get(i).getCultureLevelName(j), 64, out);
                }
                writeInt(rule.get(i).getBorderExpansionMultiplier(), out);
                writeInt(rule.get(i).getBorderFactor(), out);
                writeInt(rule.get(i).getFutureTechCost(), out);
                writeInt(rule.get(i).getGoldenAgeDuration(), out);
                writeInt(rule.get(i).getMaximumResearchTime(), out);
                writeInt(rule.get(i).getMinimumResearchTime(), out);
                writeInt(rule.get(i).getFlagUnit(), out);
                writeInt(rule.get(i).getUpgradeCost(), out);
            }
        }
        catch(java.io.IOException e){
            logger.error("IOException", e);
        }
    }

    //TODO: don't forget that the # of strategy maps DOES impact the unit size on output
    private void outputPRTO(LittleEndianDataOutputStream out)
    {
        //logger.setLevel(Level.INFO);
        ArrayList<PRTO>strategyMaps = new ArrayList<PRTO>();
        try{
            out.writeBytes("PRTO");
            //CAREFUL: we must keep track of the # of 'Firaxis' units, not the # of 'logical' units
            //units.size() will be the # of units we see in the editor, but internally the BIQ stores more than that
            writeInt(getNumFiraxisUnits(), out);
            for (int i = 0; i < unit.size(); i++)
            {
                writeInt(unit.get(i).getDataLength(), out);
                writeInt(unit.get(i).getZoneOfControl(), out);
                writeString(unit.get(i).getName(), 32, out);
                writeString(unit.get(i).getCivilopediaEntry(), 32, out);
                writeInt(unit.get(i).getBombardStrength(), out);
                writeInt(unit.get(i).getBombardRange(), out);
                writeInt(unit.get(i).getCapacity(), out);
                writeInt(unit.get(i).getShieldCost(), out);
                writeInt(unit.get(i).getDefence(), out);
                writeInt(unit.get(i).getIconIndex(), out);
                writeInt(unit.get(i).getAttack(), out);
                writeInt(unit.get(i).getOperationalRange(), out);
                writeInt(unit.get(i).getPopulationCost(), out);
                writeInt(unit.get(i).getRateOfFire(), out);
                writeInt(unit.get(i).getMovement(), out);
                writeInt(unit.get(i).getRequiredTech(), out);
                writeInt(unit.get(i).getUpgradeTo(), out);
                writeInt(unit.get(i).getRequiredResource1(), out);
                writeInt(unit.get(i).getRequiredResource2(), out);
                writeInt(unit.get(i).getRequiredResource3(), out);
                writeInt(unit.get(i).unitAbilities, out);
                unit.get(i).writeXthStrategy(1, out);
                writeInt(unit.get(i).availableTo, out);
                writeInt(unit.get(i).standardOrdersSpecialActions, out);
                writeInt(unit.get(i).airMissions, out);
                writeInt(unit.get(i).getUnitClass(), out);
                //Should ALWAYS be zero in my internal memory representation (as the first unit shouldn't be a strategy map)
                assert(unit.get(i).getOtherStrategy() == -1);
                writeInt(unit.get(i).getOtherStrategy(), out);
                writeInt(unit.get(i).getHitPointBonus(), out);
                writeInt(unit.get(i).PTWStandardOrders, out);
                writeInt(unit.get(i).PTWSpecialActions, out);
                writeInt(unit.get(i).PTWWorkerActions, out);
                writeInt(unit.get(i).PTWAirMissions, out);
                writeInt(unit.get(i).PTWActionsMix + 65536, out);
                writeInt(unit.get(i).getBombardEffects(), out);
                for (int j = 0; j < unit.get(i).getNumIgnoreTerrains(); j++)
                {
                    out.writeByte(unit.get(i).getIgnoredMovementCost(j));
                }
                writeInt(unit.get(i).getRequiresSupport(), out);
                writeInt(unit.get(i).getUseExactCost(), out);
                writeInt(unit.get(i).getTelepadRange(), out);
                writeInt(unit.get(i).getQuestionMark3(), out);
                if (logger.isDebugEnabled())
                    logger.debug("NumLegalUnitTelepads: " + unit.get(i).getNumLegalUnitTelepads());
                writeInt(unit.get(i).getNumLegalUnitTelepads(), out);
                List<Integer> unitTelepads = unit.get(i).getUnitTelepads();
                for (int j = 0; j < unitTelepads.size(); j++)
                {
                    writeInt(unitTelepads.get(j), out);
                }
                writeInt(unit.get(i).getEnslaveResultsIn(), out);
                writeInt(unit.get(i).getQuestionMark5(), out);
                writeInt(unit.get(i).getNumStealthTargets(), out);
                List<Integer> stealthTargets = unit.get(i).getStealthTargets();
                for (int j = 0; j < stealthTargets.size(); j++)
                {
                    writeInt(stealthTargets.get(j), out);
                }
                writeInt(unit.get(i).getQuestionMark6(), out);
                writeInt(unit.get(i).getNumLegalBuildingTelepads(), out);
                List<Integer> buildingTelepads = unit.get(i).getBuildingTelepads();
                for (int j = 0; j < buildingTelepads.size(); j++)
                {
                    writeInt(buildingTelepads.get(j), out);
                }
                out.writeByte(unit.get(i).getCreatesCraters());
                out.writeFloat(unit.get(i).getWorkerStrength());
                writeInt(unit.get(i).getQuestionMark8(), out);
                writeInt(unit.get(i).getAirDefence(), out);
                if (unit.get(i).getNumStrategies() > 1)
                    strategyMaps.add(unit.get(i));
            }
            for (int i = 0; i < strategyMaps.size(); i++)
            {
                //If a unit has > 2 strategies, output all (except the 1st) at once
                for (int s = 2; s <= strategyMaps.get(i).getNumStrategies(); s++)
                {
                    writeInt(strategyMaps.get(i).getDataLength(), out);
                    writeInt(strategyMaps.get(i).getZoneOfControl(), out);
                    writeString(strategyMaps.get(i).getName(), 32, out);
                    writeString(strategyMaps.get(i).getCivilopediaEntry(), 32, out);
                    writeInt(strategyMaps.get(i).getBombardStrength(), out);
                    writeInt(strategyMaps.get(i).getBombardRange(), out);
                    writeInt(strategyMaps.get(i).getCapacity(), out);
                    writeInt(strategyMaps.get(i).getShieldCost(), out);
                    writeInt(strategyMaps.get(i).getDefence(), out);
                    writeInt(strategyMaps.get(i).getIconIndex(), out);
                    writeInt(strategyMaps.get(i).getAttack(), out);
                    writeInt(strategyMaps.get(i).getOperationalRange(), out);
                    writeInt(strategyMaps.get(i).getPopulationCost(), out);
                    writeInt(strategyMaps.get(i).getRateOfFire(), out);
                    writeInt(strategyMaps.get(i).getMovement(), out);
                    writeInt(strategyMaps.get(i).getRequiredTech(), out);
                    writeInt(strategyMaps.get(i).getUpgradeTo(), out);
                    writeInt(strategyMaps.get(i).getRequiredResource1(), out);
                    writeInt(strategyMaps.get(i).getRequiredResource2(), out);
                    writeInt(strategyMaps.get(i).getRequiredResource3(), out);
                    writeInt(strategyMaps.get(i).unitAbilities, out);
                    strategyMaps.get(i).writeXthStrategy(s, out);
                    writeInt(strategyMaps.get(i).availableTo, out);
                    writeInt(strategyMaps.get(i).standardOrdersSpecialActions, out);
                    writeInt(strategyMaps.get(i).airMissions, out);
                    writeInt(strategyMaps.get(i).getUnitClass(), out);
                    //NOTE the otherStrategy for the unit map is NOT what I store in memory for it, but the unit's index
                    writeInt(strategyMaps.get(i).index, out);
                    writeInt(strategyMaps.get(i).getHitPointBonus(), out);
                    writeInt(strategyMaps.get(i).PTWStandardOrders, out);
                    writeInt(strategyMaps.get(i).PTWSpecialActions, out);
                    writeInt(strategyMaps.get(i).PTWWorkerActions, out);
                    writeInt(strategyMaps.get(i).PTWAirMissions, out);
                    writeInt(strategyMaps.get(i).PTWActionsMix + 65536, out);
                    writeInt(strategyMaps.get(i).getBombardEffects(), out);
                    for (int j = 0; j < strategyMaps.get(i).getNumIgnoreTerrains(); j++)
                    {
                        out.writeByte(strategyMaps.get(i).getIgnoredMovementCost(j));
                    }
                    writeInt(strategyMaps.get(i).getRequiresSupport(), out);
                    writeInt(strategyMaps.get(i).getUseExactCost(), out);
                    writeInt(strategyMaps.get(i).getTelepadRange(), out);
                    writeInt(strategyMaps.get(i).getQuestionMark3(), out);
                    if (logger.isDebugEnabled())
                        logger.debug("NumLegalUnitTelepads: " + strategyMaps.get(i).getNumLegalUnitTelepads());
                    writeInt(strategyMaps.get(i).getNumLegalUnitTelepads(), out);
                    List<Integer> strategyMapTelepads = strategyMaps.get(i).getUnitTelepads();
                    for (int j = 0; j < strategyMapTelepads.size(); j++)
                    {
                        writeInt(strategyMapTelepads.get(j), out);
                    }
                    writeInt(strategyMaps.get(i).getEnslaveResultsIn(), out);
                    writeInt(strategyMaps.get(i).getQuestionMark5(), out);
                    writeInt(strategyMaps.get(i).getNumStealthTargets(), out);
                    List<Integer> stealthTargets = strategyMaps.get(i).getStealthTargets();
                    for (int j = 0; j < stealthTargets.size(); j++)
                    {
                        writeInt(stealthTargets.get(j), out);
                    }
                    writeInt(strategyMaps.get(i).getQuestionMark6(), out);
                    writeInt(strategyMaps.get(i).getNumLegalBuildingTelepads(), out);
                    List<Integer> buildingTelepads = strategyMaps.get(i).getBuildingTelepads();
                    for (int j = 0; j < buildingTelepads.size(); j++)
                    {
                        writeInt(buildingTelepads.get(j), out);
                    }
                    out.writeByte(strategyMaps.get(i).getCreatesCraters());
                    out.writeFloat(strategyMaps.get(i).getWorkerStrength());
                    writeInt(strategyMaps.get(i).getQuestionMark8(), out);
                    writeInt(strategyMaps.get(i).getAirDefence(), out);
                    //don't add it to the stategy map again
                }
            }
        }
        catch(java.io.IOException e){
            logger.error("IOException", e);
        }
    }
    
    /**
     * Returns the # of Firaxis units.
     * 
     * The if/else is necessary versus simply adding up the # of strategies,
     * because a unit may have 0 strategies.
     * 
     * @return The number of units as counted by Firaxis
     * @deprecated This method should NOT be used except for BIQ/SAV input/output code.  It
     * is very easy to make mistakes when dealing with Strategy Maps and the
     * Firaxis count of units!
     */
    @Deprecated
    public int getNumFiraxisUnits() {
        int numFiraxisUnits = 0;
        for (PRTO prto : unit) {
            if (prto.getNumStrategies() > 1) {
                numFiraxisUnits+= prto.getNumStrategies();
            }
            else {
                numFiraxisUnits++;
            }
        }
        return numFiraxisUnits;
    }
    
    /**
     * Gets the tiles within a certain diameter of a particular tile.
     * For example, suppose the diameter is three.  Then, we get all the tiles
     * marked x or X below:
     *     x
     *   x   x
     * x   X   x
     *   x   x
     *     x
     * Where the capital X is the tile at (x, y).
     * @param x The x coordinate of the starting tile
     * @param y The y coordinate of the starting tile
     * @param diameter The diameter of the tile cross-section
     * @return A list of all tiles within that diameter
     */
    public List<TILE> getTilesInDiameter(int x, int y, int diameter) {
        List<TILE> tiles = new ArrayList<TILE>();
        int index = calculateTileIndex(x, y);
        if (index != -1) {
            TILE thisTile = this.tile.get(index);
            tiles.add(thisTile);
        }
        for (int i = 3; i <= diameter; i+=2) {
            int adjX = x;
            int adjY = y;
            int adjustment = i - 1;   //for diameter 3, west tile is index 2 west.
            adjX = x - adjustment;
            boolean north = true;
            while (adjX <= x + adjustment) {
                index = calculateTileIndex(adjX, adjY);
                if (index != -1) {
                    TILE nextTile = tile.get(index);
                    tiles.add(nextTile);
                }
                if (north) {
                    //Move northeast
                    adjX++;
                    adjY--;
                    if (adjX == x) {
                        north = false;
                    }
                }
                else {
                    //Move southeast
                    adjX++;
                    adjY++;
                }
            }
            //once we hit here, adjX is x + adjustment + 1.
            boolean south = true;
            adjX = x + adjustment - 1; //minus one since we already got the easternmost tile
            adjY = y + 1; //plus one since we already go the easternmost tile
            //for this loop, stop before re-adding the westernmost (x - adjustment) tile
            while (adjX > x - adjustment) {
                index = calculateTileIndex(adjX, adjY);
                if (index != -1) {
                    TILE nextTile = tile.get(index);
                    tiles.add(nextTile);
                }
                if (south) {
                    //Move southwest
                    adjX--;
                    adjY++;
                    if (adjX == x) {
                        south = false;
                    }
                }
                else {
                    //Move northwest
                    adjX--;
                    adjY--;
                }
            }
        }
        return tiles;
    }

    private void outputRACE(LittleEndianDataOutputStream out)
    {
        try{
            out.writeBytes("RACE");
            writeInt(civilization.size(), out);
            for (int i = 0; i < civilization.size(); i++)
            {
                civilization.get(i).createBinary();
                writeInt(civilization.get(i).getDataLength(), out);
                writeInt(civilization.get(i).getNumCityNames(), out);
                List<String>cityNames = civilization.get(i).getCityNames();
                for (String cityName : cityNames)
                {
                    writeString(cityName, 24, out);
                }
                writeInt(civilization.get(i).getNumGreatLeaders(), out);
                List<String>militaryLeaders = civilization.get(i).getMilitaryLeaders();
                for (String militaryLeader : militaryLeaders)
                {
                    writeString(militaryLeader, 32, out);
                }
                writeString(civilization.get(i).getLeaderName(), 32, out);
                writeString(civilization.get(i).getLeaderTitle(), 24, out);
                writeString(civilization.get(i).getCivilopediaEntry(), 32, out);
                writeString(civilization.get(i).getAdjective(), 40, out);
                writeString(civilization.get(i).getCivilizationName(), 40, out);
                writeString(civilization.get(i).getNoun(), 40, out);
                for (int j = 0; j < numEras; j++)
                {
                    writeString(civilization.get(i).getForwardFilename(j), 260, out);
                }
                for (int j = 0; j < numEras; j++)
                {
                    writeString(civilization.get(i).getReverseFilename(j), 260, out);
                }
                writeInt(civilization.get(i).getCultureGroup(), out);
                writeInt(civilization.get(i).getLeaderGender(), out);
                writeInt(civilization.get(i).getCivilizationGender(), out);
                writeInt(civilization.get(i).getAggressionLevel(), out);
                writeInt(civilization.get(i).getUniqueCivilizationCounter(), out);
                writeInt(civilization.get(i).getShunnedGovernment(), out);
                writeInt(civilization.get(i).getFavoriteGovernment(), out);
                writeInt(civilization.get(i).getDefaultColor(), out);
                writeInt(civilization.get(i).getUniqueColor(), out);
                writeInt(civilization.get(i).getFreeTech1Index(), out);
                writeInt(civilization.get(i).getFreeTech2Index(), out);
                writeInt(civilization.get(i).getFreeTech3Index(), out);
                writeInt(civilization.get(i).getFreeTech4Index(), out);
                writeInt(civilization.get(i).getBonuses(), out);
                writeInt(civilization.get(i).getGovernorSettings(), out);
                writeInt(civilization.get(i).getBuildNever(), out);
                writeInt(civilization.get(i).getBuildOften(), out);
                writeInt(civilization.get(i).getPlurality(), out);
                writeInt(civilization.get(i).getKingUnit(), out);
                writeInt(civilization.get(i).getFlavors(), out);
                writeInt(civilization.get(i).getQuestionMark(), out);
                writeInt(civilization.get(i).getDiplomacyTextIndex(), out);
                writeInt(civilization.get(i).getNumScientificLeaders(), out);
                List<String>scientificLeaders = civilization.get(i).getScientificLeaders();
                for (String scientificLeader : scientificLeaders)
                {
                    writeString(scientificLeader, 32, out);
                }
            }
        }
        catch(java.io.IOException e){
            logger.error("IOException", e);
        }
    }

    private void outputTECH(LittleEndianDataOutputStream out)
    {
        try{
            out.writeBytes("TECH");
            writeInt(technology.size(), out);
            for (int i = 0; i < technology.size(); i++)
            {
                technology.get(i).createBinary();
                writeInt(technology.get(i).getDataLength(), out);
                writeString(technology.get(i).getName(), 32, out);
                writeString(technology.get(i).getCivilopediaEntry(), 32, out);
                writeInt(technology.get(i).getCost(), out);
                writeInt(technology.get(i).getEra(), out);
                writeInt(technology.get(i).getAdvanceIcon(), out);
                writeInt(technology.get(i).getX(), out);
                writeInt(technology.get(i).getY(), out);
                writeInt(technology.get(i).getPrerequisite1(), out);
                writeInt(technology.get(i).getPrerequisite2(), out);
                writeInt(technology.get(i).getPrerequisite3(), out);
                writeInt(technology.get(i).getPrerequisite4(), out);
                writeInt(technology.get(i).getFlags(), out);
                writeInt(technology.get(i).getFlavors(), out);
                writeInt(technology.get(i).getQuestionMark(), out);
            }
        }
        catch(java.io.IOException e){
            logger.error("IOException", e);
        }
    }

    private void outputTFRM(LittleEndianDataOutputStream out)
    {
        try{
            out.writeBytes("TFRM");
            writeInt(workerJob.size(), out);
            for (int i = 0; i < workerJob.size(); i++)
            {
                writeInt(workerJob.get(i).getDataLength(), out);
                writeString(workerJob.get(i).getName(), 32, out);
                writeString(workerJob.get(i).getCivilopediaEntry(), 32, out);
                writeInt(workerJob.get(i).getTurnsToComplete(), out);
                writeInt(workerJob.get(i).getRequiredAdvance(), out);
                writeInt(workerJob.get(i).getRequiredResource1(), out);
                writeInt(workerJob.get(i).getRequiredResource2(), out);
                writeString(workerJob.get(i).getOrder(), 32, out);
            }
        }
        catch(java.io.IOException e){
            logger.error("IOException", e);
        }
    }

    private void outputTERR(LittleEndianDataOutputStream out)
    {
        try{
            out.writeBytes("TERR");
            writeInt(terrain.size(), out);
            for (int i = 0; i < terrain.size(); i++)
            {
                writeInt(terrain.get(i).getDataLength(), out);
                writeInt(terrain.get(i).getNumPossibleResources(), out);
                for (int j = 0; j < (terrain.get(i).getNumPossibleResources() + 7) / 8; j++)
                {
                    out.writeByte(terrain.get(i).possibleResources.get(j));
                }
                writeString(terrain.get(i).getName(), 32, out);
                writeString(terrain.get(i).getCivilopediaEntry(), 32, out);
                writeInt(terrain.get(i).getFoodBonus(), out);
                writeInt(terrain.get(i).getShieldsBonus(), out);
                writeInt(terrain.get(i).getCommerceBonus(), out);
                writeInt(terrain.get(i).getDefenceBonus(), out);
                writeInt(terrain.get(i).getMovementCost(), out);
                writeInt(terrain.get(i).getFood(), out);
                writeInt(terrain.get(i).getShields(), out);
                writeInt(terrain.get(i).getCommerce(), out);
                writeInt(terrain.get(i).getWorkerJob(), out);
                writeInt(terrain.get(i).getPollutionEffect(), out);
                out.writeByte(terrain.get(i).getAllowCities());
                out.writeByte(terrain.get(i).getAllowColonies());
                out.writeByte(terrain.get(i).getImpassable());
                out.writeByte(terrain.get(i).getImpassableByWheeled());
                out.writeByte(terrain.get(i).getAllowAirfields());
                out.writeByte(terrain.get(i).getAllowForts());
                out.writeByte(terrain.get(i).getAllowOutposts());
                out.writeByte(terrain.get(i).getAllowRadarTowers());
                writeInt(terrain.get(i).getQuestionMark(), out);
                out.writeByte(terrain.get(i).getLandmarkEnabled());
                writeInt(terrain.get(i).getLandmarkFood(), out);
                writeInt(terrain.get(i).getLandmarkShields(), out);
                writeInt(terrain.get(i).getLandmarkCommerce(), out);
                writeInt(terrain.get(i).getLandmarkFoodBonus(), out);
                writeInt(terrain.get(i).getLandmarkShieldsBonus(), out);
                writeInt(terrain.get(i).getLandmarkCommerceBonus(), out);
                writeInt(terrain.get(i).getLandmarkMovementCost(), out);
                writeInt(terrain.get(i).getLandmarkDefenceBonus(), out);
                writeString(terrain.get(i).getLandmarkName(), 32, out);
                writeString(terrain.get(i).getLandmarkCivilopediaEntry(), 32, out);
                writeInt(terrain.get(i).getQuestionMark2(), out);
                writeInt(terrain.get(i).getTerrainFlags(), out);
                writeInt(terrain.get(i).getDiseaseStrength(), out);
            }
        }
        catch(java.io.IOException e){
            logger.error("IOException", e);
        }
    }

    private void outputWSIZ(LittleEndianDataOutputStream out)
    {
        try{
            out.writeBytes("WSIZ");
            writeInt(worldSize.size(), out);
            for (int i = 0; i < worldSize.size(); i++)
            {
                writeInt(worldSize.get(i).getDataLength(), out);
                writeInt(worldSize.get(i).getOptimalNumberOfCities(), out);
                writeInt(worldSize.get(i).getTechRate(), out);
                writeString(worldSize.get(i).getEmpty(), 24, out);
                writeString(worldSize.get(i).getName(), 32, out);
                writeInt(worldSize.get(i).getHeight(), out);
                writeInt(worldSize.get(i).getDistanceBetweenCivs(), out);
                writeInt(worldSize.get(i).getNumberOfCivs(), out);
                writeInt(worldSize.get(i).getWidth(), out);
            }
        }
        catch(java.io.IOException e){
            logger.error("IOException", e);
        }
    }

    private void outputFLAV(LittleEndianDataOutputStream out)
    {
        try{
            out.writeBytes("FLAV");
            writeInt(1, out);
            //For BIX compatibility
            if (flavor == null)
                flavor = new ArrayList<FLAV>(0);
            writeInt(flavor.size(), out);
            for (int i = 0; i < flavor.size(); i++)
            {
                writeInt(flavor.get(i).getQuestionMark(), out);
                writeString(flavor.get(i).getName(), 256, out);
                writeInt(flavor.get(i).getNumberOfFlavors(), out);
                for (int j = 0; j < flavor.get(i).getNumberOfFlavors(); j++)
                {
                    writeInt(flavor.get(i).relationWithOtherFlavor.get(j), out);
                }
            }
        }
        catch(java.io.IOException e){
            logger.error("IOException", e);
        }
    }

    private void outputWCHR(LittleEndianDataOutputStream out)
    {
        try{
            out.writeBytes("WCHR");
            writeInt(worldCharacteristic.size(), out);
            for (int i = 0; i < worldCharacteristic.size(); i++)
            {
                writeInt(worldCharacteristic.get(i).getDataLength(), out);
                writeInt(worldCharacteristic.get(i).getSelectedClimate(), out);
                writeInt(worldCharacteristic.get(i).getActualClimate(), out);
                writeInt(worldCharacteristic.get(i).getSelectedBarbarianActivity(), out);
                writeInt(worldCharacteristic.get(i).getActualBarbarianActivity(), out);
                writeInt(worldCharacteristic.get(i).getSelectedLandform(), out);
                writeInt(worldCharacteristic.get(i).getActualLandform(), out);
                writeInt(worldCharacteristic.get(i).getSelectedOceanCoverage(), out);
                writeInt(worldCharacteristic.get(i).getActualOceanCoverage(), out);
                writeInt(worldCharacteristic.get(i).getSelectedTemperature(), out);
                writeInt(worldCharacteristic.get(i).getActualTemperature(), out);
                writeInt(worldCharacteristic.get(i).getSelectedAge(), out);
                writeInt(worldCharacteristic.get(i).getActualAge(), out);
                writeInt(worldCharacteristic.get(i).getWorldSize(), out);
            }
        }
        catch(java.io.IOException e){
            logger.error("IOException", e);
        }
    }

    private void outputWMAP(LittleEndianDataOutputStream out)
    {
        try{
            out.writeBytes("WMAP");
            writeInt(worldMap.size(), out);
            for (int i = 0; i < worldMap.size(); i++)
            {
                writeInt(worldMap.get(i).getDataLength(), out);
                writeInt(worldMap.get(i).getNumResources(), out);
                for (int j = 0; j < worldMap.get(i).getNumResources(); j++)
                {
                    writeInt(worldMap.get(i).resourceOccurence.get(j), out);
                }
                writeInt(worldMap.get(i).getNumContinents(), out);
                writeInt(worldMap.get(i).getHeight(), out);
                writeInt(worldMap.get(i).getDistanceBetweenCivs(), out);
                writeInt(worldMap.get(i).getNumCivs(), out);
                writeInt(worldMap.get(i).getQuestionMark1(), out);
                writeInt(worldMap.get(i).getQuestionMark2(), out);
                writeInt(worldMap.get(i).getWidth(), out);
                writeInt(worldMap.get(i).getQuestionMark3(), out);
                writeString(worldMap.get(i).getUnknown124(), 124, out);  //124 bytes unknown
                writeInt(worldMap.get(i).getMapSeed(), out);
                writeInt(worldMap.get(i).getFlags(), out);
            }
        }
        catch(java.io.IOException e){
            logger.error("IOException", e);
        }
    }

    private void outputTILE(LittleEndianDataOutputStream out)
    {
        try{
            out.writeBytes("TILE");
            writeInt(tile.size(), out);
            for (int i = 0; i < tile.size(); i++)
            {
                writeInt(tile.get(i).getDataLength(), out);
                out.writeByte(tile.get(i).getRiverConnectionInfo());
                out.writeByte(tile.get(i).getBorder());
                writeInt(tile.get(i).getResource(), out);
                out.writeByte(tile.get(i).getImage());
                out.writeByte(tile.get(i).getFile());
                writeShort(tile.get(i).getQuestionMark(), out);
                out.writeByte(tile.get(i).getOverlays());
                out.writeByte(tile.get(i).getBaseRealTerrain());
                out.writeByte(tile.get(i).getBonuses());
                out.writeByte(tile.get(i).getRiverCrossingData());
                writeShort(tile.get(i).getBarbarianTribe(), out);
                writeShort(tile.get(i).getCity(), out);
                writeShort(tile.get(i).getColony(), out);
                writeShort(tile.get(i).getContinent(), out);
                out.writeByte(tile.get(i).getQuestionMark2());
                writeShort(tile.get(i).getVictoryPointLocation(), out);
                writeInt(tile.get(i).getRuin(), out);
                writeInt(tile.get(i).getC3COverlays(), out);
                out.writeByte(tile.get(i).getQuestionMark3());
                out.writeByte(tile.get(i).getC3CBaseRealTerrain());
                writeShort(tile.get(i).getQuestionMark4(), out);
                writeShort(tile.get(i).getFogOfWar(), out);
                writeInt(tile.get(i).getC3CBonuses(), out);
                writeShort(tile.get(i).getQuestionMark5(), out);
            }
        }
        catch(java.io.IOException e){
            logger.error("IOException", e);
        }
    }

    private void outputCONT(LittleEndianDataOutputStream out)
    {
        try{
            out.writeBytes("CONT");
            writeInt(continent.size(), out);
            for (int i = 0; i < continent.size(); i++)
            {
                writeInt(continent.get(i).getDataLength(), out);
                writeInt(continent.get(i).getContinentClass(), out);
                writeInt(continent.get(i).getNumTiles(), out);
            }
        }
        catch(java.io.IOException e){
            logger.error("IOException", e);
        }
    }

    private void outputSLOC(LittleEndianDataOutputStream out)
    {
        try{
            out.writeBytes("SLOC");
            writeInt(startingLocation.size(), out);
            for (int i = 0; i < startingLocation.size(); i++)
            {
                writeInt(startingLocation.get(i).getDataLength(), out);
                writeInt(startingLocation.get(i).getOwnerType(), out);
                writeInt(startingLocation.get(i).getOwner(), out);
                writeInt(startingLocation.get(i).getX(), out);
                writeInt(startingLocation.get(i).getY(), out);
            }
        }
        catch(java.io.IOException e){
            logger.error("IOException", e);
        }
    }

    private void outputCITY(LittleEndianDataOutputStream out)
    {
        try{
            out.writeBytes("CITY");
            writeInt(city.size(), out);
            for (int i = 0; i < city.size(); i++)
            {
                writeInt(city.get(i).getDataLength(), out);
                out.writeByte(city.get(i).getHasWalls());
                out.writeByte(city.get(i).getHasPalace());
                writeString(city.get(i).getName(), 24, out);
                writeInt(city.get(i).getOwnerType(), out);
                writeInt(city.get(i).getNumBuildings(), out);
                for (int j = 0; j < city.get(i).getNumBuildings(); j++)
                {
                    writeInt(city.get(i).getBuildingID(j), out);
                }
                writeInt(city.get(i).getCulture(), out);
                writeInt(city.get(i).getOwner(), out);
                writeInt(city.get(i).getSize(), out);
                writeInt(city.get(i).getX(), out);
                writeInt(city.get(i).getY(), out);
                writeInt(city.get(i).getCityLevel(), out);
                writeInt(city.get(i).getBorderLevel(), out);
                writeInt(city.get(i).getUseAutoName(), out);
            }
        }
        catch(java.io.IOException e){
            logger.error("IOException", e);
        }
    }

    private void outputUNIT(LittleEndianDataOutputStream out)
    {
        try{
            out.writeBytes("UNIT");
            writeInt(mapUnit.size(), out);
            for (int i = 0; i < mapUnit.size(); i++)
            {
                writeInt(mapUnit.get(i).getDataLength(), out);
                writeString(mapUnit.get(i).getName(), 32, out);
                writeInt(mapUnit.get(i).getOwnerType(), out);
                writeInt(mapUnit.get(i).getExperienceLevel(), out);
                writeInt(mapUnit.get(i).getOwner(), out);
                writeInt(mapUnit.get(i).getPRTONumber(), out);
                writeInt(mapUnit.get(i).getAIStrategy(), out);
                writeInt(mapUnit.get(i).getX(), out);
                writeInt(mapUnit.get(i).getY(), out);
                writeString(mapUnit.get(i).getPTWCustomName(), 57, out);
                writeInt(mapUnit.get(i).getUseCivilizationKing(), out);
            }
        }
        catch(java.io.IOException e){
            logger.error("IOException", e);
        }
    }

    private void outputCLNY(LittleEndianDataOutputStream out)
    {
        try{
            out.writeBytes("CLNY");
            writeInt(colony.size(), out);
            for (int i = 0; i < colony.size(); i++)
            {
                writeInt(colony.get(i).getDataLength(), out);
                writeInt(colony.get(i).getOwnerType(), out);
                writeInt(colony.get(i).getOwner(), out);
                writeInt(colony.get(i).getX(), out);
                writeInt(colony.get(i).getY(), out);
                writeInt(colony.get(i).getImprovementType(), out);
            }
        }
        catch(java.io.IOException e){
            logger.error("IOException", e);
        }
    }

    private void outputGAME(LittleEndianDataOutputStream out)
    {
        try{
            out.writeBytes("GAME");
            writeInt(scenarioProperty.size(), out);
            for (int i = 0; i < scenarioProperty.size(); i++)
            {
                writeInt(scenarioProperty.get(i).getDataLength(), out);
                writeInt(scenarioProperty.get(i).getUseDefaultRules(), out);
                writeInt(scenarioProperty.get(i).getDefaultVictoryConditions(), out);
                writeInt(scenarioProperty.get(i).getNumberOfPlayableCivs(), out);
                //bug
                for (int j = 0; j < scenarioProperty.get(i).getNumberOfPlayableCivs(); j++)
                {
                    writeInt(scenarioProperty.get(i).idOfPlayableCivs.get(j), out);
                }
                writeInt(scenarioProperty.get(i).getVictoryConditionsAndRules(), out);
                writeInt(scenarioProperty.get(i).getPlaceCaptureUnits(), out);
                writeInt(scenarioProperty.get(i).getAutoPlaceKings(), out);
                writeInt(scenarioProperty.get(i).getAutoPlaceVictoryLocations(), out);
                writeInt(scenarioProperty.get(i).getDebugMode(), out);
                writeInt(scenarioProperty.get(i).getUseTimeLimit(), out);
                writeInt(scenarioProperty.get(i).getBaseTimeUnit(), out);
                writeInt(scenarioProperty.get(i).getStartMonth(), out);
                writeInt(scenarioProperty.get(i).getStartWeek(), out);
                writeInt(scenarioProperty.get(i).getStartYear(), out);
                writeInt(scenarioProperty.get(i).getMinuteTimeLimit(), out);
                writeInt(scenarioProperty.get(i).getTurnTimeLimit(), out);
                for (int j = 0; j < scenarioProperty.get(i).turnsPerTimescalePart.length; j++)
                {
                    writeInt(scenarioProperty.get(i).turnsPerTimescalePart[j], out);
                }
                for (int j = 0; j < scenarioProperty.get(i).timeUnitsPerTurn.length; j++)
                {
                    writeInt(scenarioProperty.get(i).timeUnitsPerTurn[j], out);
                }
                writeString(scenarioProperty.get(i).getScenarioSearchFolders(), 5200, out);
                if (scenarioProperty.get(i).numberOfPlayableCivs > 0)
                {
                    if (logger.isDebugEnabled())
                        logger.debug(scenarioProperty.get(i).numberOfPlayableCivs + " civs checked as playable");
                    for (int j = 0; j < scenarioProperty.get(i).numberOfPlayableCivs; j++)
                    {
                        if (version == civ3Version.CONQUESTS)
                            writeInt(scenarioProperty.get(i).civPartOfWhichAlliance.get(j), out);
                        else
                            writeInt(0, out);
                    }
                }
                else
                {
                    logger.warn("Fewer than one civ checked as playable");
                    logger.warn("civilization.size() - 1: " + (civilization.size() - 1));
                    for (int j = 0; j < civilization.size() - 1; j++)
                    {   //minus one b/c we don't include barbarians
                        if (version == civ3Version.CONQUESTS)
                            writeInt(scenarioProperty.get(i).civPartOfWhichAlliance.get(j), out);
                        else
                            writeInt(0, out);
                    }
                }
                writeInt(scenarioProperty.get(i).getVictoryPointLimit(), out);
                writeInt(scenarioProperty.get(i).getCityEliminationCount(), out);
                writeInt(scenarioProperty.get(i).getOneCityCultureWinLimit(), out);
                writeInt(scenarioProperty.get(i).getAllCitiesCultureWinLimit(), out);
                writeInt(scenarioProperty.get(i).getDominationTerrainPercent(), out);
                writeInt(scenarioProperty.get(i).getDominationPopulationPercent(), out);
                writeInt(scenarioProperty.get(i).getWonderVP(), out);
                writeInt(scenarioProperty.get(i).getDefeatingOpposingUnitVP(), out);
                writeInt(scenarioProperty.get(i).getAdvancementVP(), out);
                writeInt(scenarioProperty.get(i).getCityConquestVP(), out);
                writeInt(scenarioProperty.get(i).getVictoryPointVP(), out);
                writeInt(scenarioProperty.get(i).getCaptureSpecialUnitVP(), out);
                writeInt(scenarioProperty.get(i).getQuestionMark1(), out);
                out.writeByte(scenarioProperty.get(i).getQuestionMark2());
                writeString(scenarioProperty.get(i).getAlliance0(), 256, out);
                writeString(scenarioProperty.get(i).getAlliance1(), 256, out);
                writeString(scenarioProperty.get(i).getAlliance2(), 256, out);
                writeString(scenarioProperty.get(i).getAlliance3(), 256, out);
                writeString(scenarioProperty.get(i).getAlliance4(), 256, out);
                for (int j = 0; j < 5; j++)
                {
                    if (version == civ3Version.CONQUESTS)
                    {
                        writeInt(scenarioProperty.get(i).warWith0.get(j), out);
                        writeInt(scenarioProperty.get(i).warWith1.get(j), out);
                        writeInt(scenarioProperty.get(i).warWith2.get(j), out);
                        writeInt(scenarioProperty.get(i).warWith3.get(j), out);
                        writeInt(scenarioProperty.get(i).warWith4.get(j), out);
                    }
                    else
                    {
                        for (int k = 0; k < 5; k++)
                            writeInt(0, out);
                    }
                }
                writeInt(scenarioProperty.get(i).getAllianceVictoryType(), out);
                writeString(scenarioProperty.get(i).getPlaugeName(), 260, out);
                out.writeByte(scenarioProperty.get(i).getPermitPlagues());
                writeInt(scenarioProperty.get(i).getPlagueEarliestStart(), out);
                writeInt(scenarioProperty.get(i).getPlagueVariation(), out);
                writeInt(scenarioProperty.get(i).getPlagueDuration(), out);
                writeInt(scenarioProperty.get(i).getPlagueStrength(), out);
                writeInt(scenarioProperty.get(i).getPlagueGracePeriod(), out);
                writeInt(scenarioProperty.get(i).getPlagueMaxOccurance(), out);
                writeInt(scenarioProperty.get(i).getQuestionMark3(), out);
                writeString(scenarioProperty.get(i).getUnknown(), 260, out);
                writeInt(scenarioProperty.get(i).getRespawnFlagUnits(), out);
                out.writeByte(scenarioProperty.get(i).getCaptureAnyFlag());
                writeInt(scenarioProperty.get(i).getGoldForCapture(), out);
                out.writeByte(scenarioProperty.get(i).getMapVisible());
                out.writeByte(scenarioProperty.get(i).getRetainCulture());
                writeInt(scenarioProperty.get(i).getQuestionMark4(), out);
                writeInt(scenarioProperty.get(i).getEruptionPeriod(), out);
                writeInt(scenarioProperty.get(i).getMpBaseTime(), out);
                writeInt(scenarioProperty.get(i).getMpCityTime(), out);
                writeInt(scenarioProperty.get(i).getMpUnitTime(), out);
            }
        }
        catch(java.io.IOException e){
            logger.error("IOException", e);
        }
    }

    private void outputLEAD(LittleEndianDataOutputStream out)
    {
        try{
            out.writeBytes("LEAD");
            writeInt(player.size(), out);
            for (int i = 0; i < player.size(); i++)
            {
                writeInt(player.get(i).getDataLength(), out);
                writeInt(player.get(i).getCustomCivData(), out);
                writeInt(player.get(i).getHumanPlayer(), out);
                writeString(player.get(i).getLeaderName(), 32, out);
                writeInt(player.get(i).getQuestionMark1(), out);
                writeInt(player.get(i).getQuestionMark2(), out);
                Map<PRTO, Integer> playerStartUnits = player.get(i).getStartingUnits();
                writeInt(playerStartUnits.size(), out);
                for (Entry<PRTO, Integer> entry : playerStartUnits.entrySet()) {
                    writeInt(entry.getValue(), out);
                    writeInt(entry.getKey().getIndex(), out);
                }
                writeInt(player.get(i).getGenderOfLeaderName(), out);
                writeInt(player.get(i).getNumberOfStartingTechnologies(), out);
                for (int j = 0; j < player.get(i).getNumberOfStartingTechnologies(); j++)
                {
                    writeInt(player.get(i).startingTechnologyInt.get(j), out);
                }
                writeInt(player.get(i).getDifficulty(), out);
                writeInt(player.get(i).getInitialEra(), out);
                writeInt(player.get(i).getStartCash(), out);
                writeInt(player.get(i).getGovernment(), out);
                writeInt(player.get(i).getCiv(), out);
                writeInt(player.get(i).getColor(), out);
                writeInt(player.get(i).getSkipFirstTurn(), out);
                writeInt(player.get(i).getQuestionMark3(), out);
                out.writeByte(player.get(i).getStartEmbassies());
            }
        }
        catch(java.io.IOException e){
            logger.error("IOException", e);
        }
    }


    //Trim stuff in the header of the file
    public void trim()
    {
        description = description.trim();
        title = title.trim();
    }

    public int numSection(Section section)
    {
        switch(section)
        {
            case BLDG:
                return this.numBuildings;
            case CTZN:
                return this.numCitizens;
            case CULT:
                return this.numCulturalOpinions;
            case DIFF:
                return this.numDifficulties;
            case ERAS:
                return this.numEras;
            case ESPN:
                return this.numEspionage;
            case EXPR:
                return this.numExprLevel;
            case GOOD:
                return this.numGoods;
            case GOVT:
                return this.numGovernments;
            case RULE:
                return this.numRules;
            case PRTO:
                return this.unit.size();
            case RACE:
                return this.numCivilizations;
            case TECH:
                return this.numTechnologies;
            case TFRM:
                return this.numWorkerJobs;
            case TERR:
                return this.terrain.size();
            case WSIZ:
                return this.worldSize.size();
            case FLAV:
                return this.flavor.size();
            case WCHR:
                return this.worldCharacteristic.size();
            case WMAP:
                return this.worldMap.size();
            case TILE:
                return this.tile.size();
            case CONT:
                return this.numContinents;
            case SLOC:
                return this.numStartingLocations;
            case CITY:
                return this.numCities;
            case UNIT:
                return this.numUnits;
            case CLNY:
                return this.numColonies;
            case GAME:
                return this.numScenarioProperties;
            case LEAD:
                return this.numPlayers;
            default:
                return 0;
        }
    }

    public List getSection(Section section)
    {
        switch(section)
        {
            case BLDG:
                return this.buildings;
            case CTZN:
                return this.citizens;
            case CULT:
                return this.culture;
            case DIFF:
                return this.difficulties;
            case ERAS:
                return this.eras;
            case ESPN:
                return this.espionage;
            case EXPR:
                return this.experience;
            case GOOD:
                return this.resource;
            case GOVT:
                return this.government;
            case RULE:
                return this.rule;
            case PRTO:
                return this.unit;
            case RACE:
                return this.civilization;
            case TECH:
                return this.technology;
            case TFRM:
                return this.workerJob;
            case TERR:
                return this.terrain;
            case WSIZ:
                return this.worldSize;
            case FLAV:
                return this.flavor;
            case WCHR:
                return this.worldCharacteristic;
            case WMAP:
                return this.worldMap;
            case TILE:
                return this.tile;
            case CONT:
                return this.continent;
            case SLOC:
                return this.startingLocation;
            case CITY:
                return this.city;
            case UNIT:
                return this.mapUnit;
            case CLNY:
                return this.colony;
            case GAME:
                return this.scenarioProperty;
            case LEAD:
                return this.player;
            default:
                return null;
        }
    }
    
    /**
     * New totally correct method that is 1337.
     * @param x
     * @param y
     * @param radius 
     */
    public void calculateTileOwners(int x, int y, List<TILE>tilesToLookAt1)
    {
        if (!this.hasCustomMap)
        {
            return;
        }
        int cityID = tile.get(calculateTileIndex(x, y)).city;
        if (cityID == -1)
            return;
        calculateInfluenceOfCity(cityID);
        List<TILE>tilesToLookAt2 = city.get(cityID).tilesInfluenced;
        if (tilesToLookAt1.size() > tilesToLookAt2.size())
        {
            for (int i = 0; i < tilesToLookAt1.size(); i++)
            {
                calculateTileOwner(tilesToLookAt1.get(i));
            }
        }
        else
        {
            for (int i = 0; i < tilesToLookAt2.size(); i++)
            {
                calculateTileOwner(tilesToLookAt2.get(i));
            }
        }
    }

    /**
     * Calculates who owns every tile on the map.
     * This should be called as rarely as possible
     */
    public void calculateTileOwners()
    {
        if (!this.hasCustomMap)
        {
            return;
        }
        long preCity = System.nanoTime();
        calculateInfluenceOfCities();
        long postCity = System.nanoTime();
        long ms = (postCity-preCity)/1000000;
        if (logger.isInfoEnabled())
            logger.info("It took " + ms + " ms to calculate the city influence");
        //Now we've got all the cities influencing all the tiles.
        //Now we figure out who owns the bloody tile.
        long nearby = 0;
        for (int i = 0; i < tile.size(); i++)
        {
            TILE curTile = tile.get(i);
            nearby = nearby + calculateTileOwner(curTile);
            if (logger.isDebugEnabled())
                logger.debug("Found owner for tile " + i);
        }
        long postOwner = System.nanoTime();
        ms = (postOwner-postCity)/1000000;
        if (logger.isInfoEnabled())
            logger.info("It took " + ms + " ms to who owns tiles");
        ms = nearby/1000000;
        if (logger.isInfoEnabled())
            logger.info("Of that, it took " + ms + " ms to calculate the nearest city");
    }
    
    /**
     * Calculates who the owner of a single, individual tile is.
     * This is a very complicated method.  I don't even want to know what its
     * cyclomatic complexity is.  But it is correct, to the best of my knowledge.
     * 
     * @param curTile The tile whose ownership is in question.
     * @return The number of ms it takes to calculate which cities are nearest to
     * the tile.
     */
    private long calculateTileOwner(TILE curTile)
    {
        
        if (curTile.xPos == 5 && curTile.yPos == 43) {
            logger.info("debug");
        }
        long nearbyTime = 0;
        if (curTile.citiesWithInfluence.isEmpty())
            curTile.owner = -1;
        else
        {
            if (logger.isDebugEnabled())
            {
                logger.debug(curTile.citiesWithInfluence + " cities influencing");
                for (int z = 0; z < curTile.citiesWithInfluence.size(); z++)
                {
                    logger.debug(city.get(curTile.citiesWithInfluence.get(z)).getX() + ", " + city.get(curTile.citiesWithInfluence.get(z)).getY());
                }
                logger.debug("i: " + curTile.index);
            }
            long now = System.nanoTime();
            List<CITY>nearbyCities = findNearestCities(curTile.xPos, curTile.yPos);
            long later = System.nanoTime();
            long time = (later - now);
            nearbyTime = time;
            int maxCultureIndex = -1;
            //logger.info("found nearby cities in: " + time + " nanoseconds");
            if (nearbyCities.size() == 0)
            {
                curTile.owner = -1;
                return nearbyTime;
            }
            else if (nearbyCities.size() == 1)
            {
                if (logger.isDebugEnabled())
                    logger.debug("Only one nearby city");
                curTile.owner = nearbyCities.get(0).getOwner();
                maxCultureIndex = 0;
            }
            else
            {
                int maxCulture = -1;
                if (logger.isDebugEnabled())
                    logger.debug("Nearby cities size: " + nearbyCities.size());
                for (int j = 0; j < nearbyCities.size(); j++)
                {
                    if (logger.isDebugEnabled())
                        logger.debug("City " + j + " culture: " + nearbyCities.get(j).getCulture());
                    if (nearbyCities.get(j).getCulture() > maxCulture)
                    {
                        maxCulture = nearbyCities.get(j).getCulture();
                        maxCultureIndex = j;
                    }
                }
            }
            //Switching our curTile.owner to actual civ messes up saves with
            //only player-owner cities.
            if (logger.isDebugEnabled())
               logger.debug("Max culture index: " +  maxCultureIndex);
            switch (nearbyCities.get(maxCultureIndex).getOwnerType())
            {
                case CITY.OWNER_CIV:
                    curTile.owner = nearbyCities.get(maxCultureIndex).getOwner();
                    boolean foundCustomColor = false;
                    if (hasCustomPlayerData)
                    {
                        for (int j = 0; j < player.size(); j++)
                        {
                            if (player.get(j).civ == curTile.owner && player.get(j).customCivData == 1)
                            {
                                curTile.borderColor = player.get(j).color;
                                foundCustomColor = true;
                            }
                        }
                    }
                    if (!foundCustomColor)
                        curTile.borderColor = civilization.get(curTile.owner).getDefaultColor();
                    curTile.definiteOwner = true;
                    curTile.ownerType = CITY.OWNER_CIV;
                    break;
                case CITY.OWNER_PLAYER:
                    if (nearbyCities.get(maxCultureIndex).getOwner() > -1)
                    {
                        //Leave the owner as the player ID #, but use a temporary civ index for operations
                        //that need to know which civ that player is
                        curTile.owner = nearbyCities.get(maxCultureIndex).getOwner();
                        if (hasCustomPlayerData)
                        {
                            int civIndex = player.get(nearbyCities.get(maxCultureIndex).getOwner()).civ;
                            if (player.get(curTile.owner).customCivData == 1)
                            {
                                curTile.borderColor = player.get(curTile.owner).color;
                                foundCustomColor = true;
                                break;
                            }
                            else if (player.get(curTile.owner).civ > -1)
                            {
                                curTile.borderColor = civilization.get(civIndex).getDefaultColor();
                            }
                            else {
                                //The Firaxis editor seems to use 31 - the owner ID, so I shall do the
                                //same for consistency.  Discovered while looking for correlations; was
                                //about the 3rd thing I checked.
                                curTile.borderColor = 31 - nearbyCities.get(maxCultureIndex).getOwner();
                                break;
                            }
                        }
                        curTile.definiteOwner = true;
                        curTile.ownerType = CITY.OWNER_PLAYER;
                    }
                    else
                    {
                        logger.warn("Warning! " + curTile.xPos + ", " + curTile.yPos);
                        curTile.owner = -1 * player.get(nearbyCities.get(maxCultureIndex).getOwner()).civ;
                        curTile.definiteOwner = false;
                        curTile.borderColor = 32 - nearbyCities.get(maxCultureIndex).getOwner();
                    }
                    break;
                default:
                    logger.warn("City of unrecognized ownership: " + curTile.xPos + ", " + curTile.yPos);
            }
        }
        return nearbyTime;
    }
    
    private void calculateInfluenceOfCity(int i)
    {
        List<TILE>oldTilesInfluenced = city.get(i).tilesInfluenced;
        city.get(i).tilesInfluenced = new ArrayList<TILE>();
        List<Integer>tileIndexes = new ArrayList<Integer>();
        int x = city.get(i).getX();
        int y = city.get(i).getY();
        int cityCulture = city.get(i).getCulture();
        if (cityCulture == 0)
            cityCulture++;
        //levelX indicates how many tiles are in each level of the fat cross
        //if the culture is < 10, we have three (NW->SE) rows with 3 tiles each)
        //if the culture is 11-99, we add an additional two tiles (one in each direction) to the
        //existing levels of the fat cross, plus another row of three on each end.
        int[]levelX = new int[13];  //one for each of the maximal radius of tiles
        if (cityCulture > 0)    //true
        {
            levelX[5]+=3;
            levelX[6]+=3;
            levelX[7]+=3;
            cityCulture/=10;
        }
        if (cityCulture > 0)    //10+ culture
        {
            levelX[4]+=3;
            levelX[5]+=2;
            levelX[6]+=2;
            levelX[7]+=2;
            levelX[8]+=3;
            cityCulture/=10;
        }
        if (cityCulture > 0)    //100+ culture
        {
            levelX[3]+=3;
            levelX[4]+=2;
            levelX[5]+=2;
            levelX[6]+=2;
            levelX[7]+=2;
            levelX[8]+=2;
            levelX[9]+=3;
            cityCulture/=10;
        }
        if (cityCulture > 0)    //1000+ culture
        {
            levelX[2]+=3;
            levelX[3]+=4;
            levelX[4]+=2;
            levelX[5]+=2;
            levelX[6]+=2;
            levelX[7]+=2;
            levelX[8]+=2;
            levelX[9]+=4;
            levelX[10]+=3;
            cityCulture/=10;
        }
        if (cityCulture > 0)    //10,000+ culture
        {
            levelX[1]+=3;
            levelX[2]+=4;
            levelX[3]+=2;
            levelX[4]+=2;
            levelX[5]+=2;
            levelX[6]+=2;
            levelX[7]+=2;
            levelX[8]+=2;
            levelX[9]+=2;
            levelX[10]+=4;
            levelX[11]+=3;
            cityCulture/=10;
        }
        if (cityCulture > 0)    //100,000+ culture
        {
            levelX[0]+=5;   //check
            levelX[1]+=6;   //check
            levelX[2]+=4;   //check
            levelX[3]+=2;   //check
            levelX[4]+=4;   //check
            levelX[5]+=2;
            levelX[6]+=2;
            levelX[7]+=2;
            levelX[8]+=4;
            levelX[9]+=2;
            levelX[10]+=4;
            levelX[11]+=6;
            levelX[12]+=5;
            cityCulture/=10;
        }
        //Okay now let's do it.  start in the center, work out
        for (int j = 0; j < 13; j++)
        {
            //move NW by j from 6.  move SE if j > 6
            int offset = 6 - j; //zero for the center.  may be negative
            int centerX = x - offset;
            int centerY = y - offset;
            //Now we're somewhere along a NW-SE axis from the city center
            //See if the tile on the axis is influenced
            if (levelX[j] != 0)
            {
                if (ok(centerX, centerY))
                {
                    tileIndexes.add(calculateTileIndex(centerX, centerY));
                    city.get(i).tilesInfluenced.add(tile.get(calculateTileIndex(centerX, centerY)));
                }
                levelX[j]--;
            }
            //Now work our way out SW/NE simultaneously
            int distOut = 1;
            while(levelX[j] > 0)
            {
                if (ok(centerX - distOut, centerY + distOut))
                {
                    tileIndexes.add(calculateTileIndex(centerX - distOut, centerY + distOut));
                    city.get(i).tilesInfluenced.add(tile.get(calculateTileIndex(centerX - distOut, centerY + distOut)));
                }
                if (ok(centerX + distOut, centerY - distOut))
                {
                    tileIndexes.add(calculateTileIndex(centerX + distOut, centerY - distOut));
                    city.get(i).tilesInfluenced.add(tile.get(calculateTileIndex(centerX + distOut, centerY - distOut)));
                }
                distOut++;
                levelX[j]-=2;
            }
        }
        //Remove the old influence marks
        //The explicit cast is necessary because we want to make sure Java
        //calls the remove(Object) method, not the remove(int) method
        for (int j = 0; j < oldTilesInfluenced.size(); j++)
        {
            oldTilesInfluenced.get(j).citiesWithInfluence.remove((Integer)i);
        }
        //Now add the influence marks
        for (int j = 0; j < tileIndexes.size(); j++)
            tile.get(tileIndexes.get(j)).citiesWithInfluence.add(i);   
        //Now remove old influence marks
        
    }
    
    private void calculateInfluenceOfCities()
    {
        //Calculate which tiles are influenced by the cities
        for (int i = 0; i < city.size(); i++)
        {
            calculateInfluenceOfCity(i);
        }
    }

    /**
     * This method calculates which tiles all of the cities influence.
     * Postcondition: All the tiles will have an array of the cities that have
     * some influence over them.
     * 
     * TODO: Add a method that allows me to change due to a change in one
     * city, without having to redo stuff everywhere.
     *
     */
    private void calculateInfluenceOfCitiez()
    {
        //Calculate which tiles are influenced by the cities
        for (int i = 0; i < city.size(); i++)
        {
            List<TILE>tilesInfluenced = new ArrayList<TILE>();
            List<Integer>tileIndexes = new ArrayList<Integer>();
            int x = city.get(i).getX();
            int y = city.get(i).getY();
            int cityCulture = city.get(i).getCulture();
            if (cityCulture == 0)
                cityCulture++;
            tile.get(calculateTileIndex(x, y)).citiesWithInfluence.add(i);
            tilesInfluenced.add(tile.get(calculateTileIndex(x, y)));
            tileIndexes.add((calculateTileIndex(x, y)));
            if (okX(x - 2))
            {
                tile.get(calculateTileIndex(x - 2, y)).citiesWithInfluence.add(i);
                tilesInfluenced.add(tile.get(calculateTileIndex(x - 2, y)));
                tileIndexes.add((calculateTileIndex(x - 2, y)));
            }
            if (okX(x + 2))
            {
                tile.get(calculateTileIndex(x + 2, y)).citiesWithInfluence.add(i);
                tilesInfluenced.add(tile.get(calculateTileIndex(x + 2, y)));
                tileIndexes.add((calculateTileIndex(x + 2, y)));
            }
            if (okY(y - 2))
            {
                tile.get(calculateTileIndex(x, y - 2)).citiesWithInfluence.add(i);
                tilesInfluenced.add(tile.get(calculateTileIndex(x, y - 2)));
                tileIndexes.add((calculateTileIndex(x, y - 2)));
            }
            if (okY(y + 2))
            {
                tile.get(calculateTileIndex(x, y + 2)).citiesWithInfluence.add(i);
                tilesInfluenced.add(tile.get(calculateTileIndex(x, y + 2)));
                tileIndexes.add((calculateTileIndex(x, y + 2)));
            }
            if (ok(x + 1, y + 1))
            {
                tile.get(calculateTileIndex(x + 1, y + 1)).citiesWithInfluence.add(i);
                tilesInfluenced.add(tile.get(calculateTileIndex(x + 1, y + 1)));
                tileIndexes.add((calculateTileIndex(x + 1, y + 1)));
            }
            if (ok(x - 1, y + 1))
            {
                tile.get(calculateTileIndex(x - 1, y + 1)).citiesWithInfluence.add(i);
                tilesInfluenced.add(tile.get(calculateTileIndex(x - 1, y + 1)));
                tileIndexes.add((calculateTileIndex(x - 1, y + 1)));
            }
            if (ok(x + 1, y - 1))
            {
                tile.get(calculateTileIndex(x + 1, y - 1)).citiesWithInfluence.add(i);
                tilesInfluenced.add(tile.get(calculateTileIndex(x + 1, y - 1)));
                tileIndexes.add((calculateTileIndex(x + 1, y - 1)));
            }
            if (ok(x - 1, y - 1))
            {
                tile.get(calculateTileIndex(x - 1, y - 1)).citiesWithInfluence.add(i);
                tilesInfluenced.add(tile.get(calculateTileIndex(x - 1, y - 1)));
                tileIndexes.add((calculateTileIndex(x - 1, y - 1)));
            }
            cityCulture = cityCulture/10;
            while (cityCulture > 0)
            {
                if (logger.isDebugEnabled())
                    logger.debug("Entering next stage of quest");
                int max = tilesInfluenced.size();
                //expand the number of tiles influenced
                for (int j = 0; j < max; j++)
                {
                    if (ok(calculateTilePosition(tileIndexes.get(j)).width - 1, calculateTilePosition(tileIndexes.get(j)).height - 1))
                    {
                        if (logger.isTraceEnabled())
                            logger.trace("OK to explore NW from " + calculateTilePosition(tileIndexes.get(j)).width + ", " + calculateTilePosition(tileIndexes.get(j)).height);
                        TILE NW = tile.get(calculateTileIndex(calculateTilePosition(tileIndexes.get(j)).width - 1, calculateTilePosition(tileIndexes.get(j)).height - 1));
                        if (!(tilesInfluenced.contains(NW)))
                        {
                            tileIndexes.add((calculateTileIndex(calculateTilePosition(tileIndexes.get(j)).width - 1, calculateTilePosition(tileIndexes.get(j)).height - 1)));
                            tilesInfluenced.add(NW);
                        }
                    }
                    if (ok(calculateTilePosition(tileIndexes.get(j)).width + 1, calculateTilePosition(tileIndexes.get(j)).height - 1))
                    {
                        if (logger.isTraceEnabled())
                            logger.trace("OK to explore NE from " + calculateTilePosition(tileIndexes.get(j)).width + ", " + calculateTilePosition(tileIndexes.get(j)).height);
                        TILE NE = tile.get(calculateTileIndex(calculateTilePosition(tileIndexes.get(j)).width + 1, calculateTilePosition(tileIndexes.get(j)).height - 1));
                        if (!(tilesInfluenced.contains(NE)))
                        {
                            tileIndexes.add((calculateTileIndex(calculateTilePosition(tileIndexes.get(j)).width + 1, calculateTilePosition(tileIndexes.get(j)).height - 1)));
                            tilesInfluenced.add(NE);
                        }

                    }
                    if (ok(calculateTilePosition(tileIndexes.get(j)).width + 1, calculateTilePosition(tileIndexes.get(j)).height + 1))
                    {
                        if (logger.isTraceEnabled())
                            logger.trace("OK to explore SE from " + calculateTilePosition(tileIndexes.get(j)).width + ", " + calculateTilePosition(tileIndexes.get(j)).height);
                        TILE SE = tile.get(calculateTileIndex(calculateTilePosition(tileIndexes.get(j)).width + 1, calculateTilePosition(tileIndexes.get(j)).height + 1));
                        if (!(tilesInfluenced.contains(SE)))
                        {
                            tileIndexes.add((calculateTileIndex(calculateTilePosition(tileIndexes.get(j)).width + 1, calculateTilePosition(tileIndexes.get(j)).height + 1)));
                            tilesInfluenced.add(SE);
                        }
                    }
                    if (ok(calculateTilePosition(tileIndexes.get(j)).width - 1, calculateTilePosition(tileIndexes.get(j)).height + 1))
                    {
                        if (logger.isTraceEnabled())
                            logger.trace("OK to explore SW from " + calculateTilePosition(tileIndexes.get(j)).width + ", " + calculateTilePosition(tileIndexes.get(j)).height);
                        TILE SW = tile.get(calculateTileIndex(calculateTilePosition(tileIndexes.get(j)).width - 1, calculateTilePosition(tileIndexes.get(j)).height + 1));
                        if (!(tilesInfluenced.contains(SW)))
                        {
                            tileIndexes.add((calculateTileIndex(calculateTilePosition(tileIndexes.get(j)).width - 1, calculateTilePosition(tileIndexes.get(j)).height + 1)));
                            tilesInfluenced.add(SW);
                        }
                    }
                }
                //Now go through all the tiles that are influenced (after the expansion) and add this city to
                //their list of influencing cities, if it isn't already there
                for (int j = 0; j < tilesInfluenced.size(); j++)
                {
                    if (!(tile.get(tileIndexes.get(j)).citiesWithInfluence.contains(i)))
                    {
                        if (logger.isDebugEnabled())
                        {
                            logger.debug("Influenced index: " + tileIndexes.get(j));
                            logger.debug("City " + city.get(i).getName() + " at " + city.get(i).getX() + ", " + city.get(i).getY() + " influences tile at " + calculateTilePosition(tileIndexes.get(j)).width + ", " + calculateTilePosition(tileIndexes.get(j)).height);
                        }
                        tile.get(tileIndexes.get(j)).citiesWithInfluence.add(i);
                    }
                }
                cityCulture = cityCulture/10;
                if (logger.isDebugEnabled())
                    logger.debug("Culture remaining: " + cityCulture);
            }
        }
    }

    /**
     * Note: This finds the nearest city WITH CULTURAL INFLUENCE on the tile in question.
     * This may not be the nearest city.
     * FIXED (leaving note until next todo fixed: There is a definite bug in this method.  To reproduce:
     *  Open AoI 4.1 Full.
     *  Set Thunder Bay's culture to 1.
     *  The lake at 31, 43 is still listed as being owned by Thunder Bay.
     *  In debug mode in Java, we can see that the tile knows only Minneapolis
     *  influences it.
     *  But somehow, this method returns Thunder Bay as the only "nearby" city.
     * This may have something to do with this method being rather archaic.
     * Unfortunately we can't just use the Pythagorean theorem, as seen by 17,41
     * in AoI 4.1 Full.  Thus we'll have to carefully refactor this and fix that
     * bug.
     * TODO: This method uses my old method of expanding radii to expand its search.
     * It should be rewritten to use the new method.  Perhaps this should be in conjunction with a new
     * "distance of city from a certain tile" method, which would give the distance in radii-expansions.
     * @param x
     * @param y
     * @return
     */
    private List<CITY> findNearestCities(int x, int y)
    {
        //long start = System.nanoTime();
        //long nested = 0;
        //long totalLoop=0;
        List<CITY>influencingCities = new ArrayList<CITY>();
        //for (int i = 0; i < city.size(); i++)
        TILE startTile = tile.get(calculateTileIndex(x, y));
        for (int i = 0; i < startTile.citiesWithInfluence.size(); i++)
        {
            if (logger.isDebugEnabled())
                logger.debug("Tile at " + x + ", " + y + " is influenced by " + city.get(startTile.citiesWithInfluence.get(i)).getName());
            if (city.get(startTile.citiesWithInfluence.get(i)).getX() == x && city.get(startTile.citiesWithInfluence.get(i)).getY() == y)
            {   //there's a city right here
                influencingCities.add(city.get(startTile.citiesWithInfluence.get(i)));
                return influencingCities;
            }
        }
        //radius = 1
        List<TILE>tilesChecked = new ArrayList<TILE>();
        List<Integer>tileIndexes = new ArrayList<Integer>();
        tilesChecked.add(startTile);
        tileIndexes.add((calculateTileIndex(x, y)));
        if (okX(x - 2))
        {
            tilesChecked.add(tile.get(calculateTileIndex(x - 2, y)));
            tileIndexes.add((calculateTileIndex(x - 2, y)));
        }
        if (okX(x + 2))
        {
            tilesChecked.add(tile.get(calculateTileIndex(x + 2, y)));
            tileIndexes.add((calculateTileIndex(x + 2, y)));
        }
        if (okY(y - 2))
        {
            tilesChecked.add(tile.get(calculateTileIndex(x, y - 2)));
            tileIndexes.add((calculateTileIndex(x, y - 2)));
        }
        if (okY(y + 2))
        {
            tilesChecked.add(tile.get(calculateTileIndex(x, y + 2)));
            tileIndexes.add((calculateTileIndex(x, y + 2)));
        }
        if (ok(x + 1, y + 1))
        {
            tilesChecked.add(tile.get(calculateTileIndex(x + 1, y + 1)));
            tileIndexes.add((calculateTileIndex(x + 1, y + 1)));
        }
        if (ok(x - 1, y + 1))
        {
            tilesChecked.add(tile.get(calculateTileIndex(x - 1, y + 1)));
            tileIndexes.add((calculateTileIndex(x - 1, y + 1)));
        }
        if (ok(x + 1, y - 1))
        {
            tilesChecked.add(tile.get(calculateTileIndex(x + 1, y - 1)));
            tileIndexes.add((calculateTileIndex(x + 1, y - 1)));
        }
        if (ok(x - 1, y - 1))
        {
            tilesChecked.add(tile.get(calculateTileIndex(x - 1, y - 1)));
            tileIndexes.add((calculateTileIndex(x - 1, y - 1)));
        }
        //Start at 1 b/c if it was the 1st one we'd already know about it
        for (int j = 1; j < tilesChecked.size(); j++)
        {
            for (int i = 0; i < tilesChecked.get(j).citiesWithInfluence.size(); i++)
            {
                Dimension location = calculateTilePosition(tileIndexes.get(j));
                int xPos = location.width;
                int yPos = location.height;
                if (logger.isTraceEnabled())
                    logger.trace("j: " + j + "   i: " + i);
                //System.out.flush();
                CITY thisCity = city.get(tilesChecked.get(j).citiesWithInfluence.get(i));
                //if (city.get(i).x == xPos && city.get(i).y == yPos)
                if (thisCity.getX() == xPos && thisCity.getY() == yPos)
                    influencingCities.add(thisCity);
            }
        }
        if (influencingCities.size() > 0)
            return influencingCities;
        //increasing radii
        int radius = 2;
        if (logger.isDebugEnabled())
            logger.debug("x: " + x + "; y: " + y);
        //long preLoop = (System.nanoTime() - start)/1000;
        //logger.info("Preloop time: " + preLoop + " microseconds");
        //startSearch tells us which tiles haven't been searched out from
        //ex. we don't need to explore out from the center tile on any iteration
        //of this loop
        int startSearch = 1;
        while (true)
        {
            //First, we'll expand our radius of tiles for our nearby city search.
            //long startLoop = System.nanoTime();
            if (logger.isDebugEnabled())
                logger.debug(radius);
            int max = tilesChecked.size();
            for (int j = startSearch; j < max; j++)
            {
                Dimension location = calculateTilePosition(tileIndexes.get(j));
                int tileX = location.width;
                int tileY = location.height;
                if (ok(tileX - 1, tileY - 1))
                {
                    int index = calculateTileIndex(tileX - 1, tileY - 1);
                    TILE NW = tile.get(index);
                    if (!(tilesChecked.contains(NW)))
                    {
                        tileIndexes.add(index);
                        tilesChecked.add(NW);
                    }

                }
                if (ok(tileX + 1, tileY - 1))
                {
                    int index = calculateTileIndex(tileX + 1, tileY - 1);
                    TILE NE = tile.get(index);
                    if (!(tilesChecked.contains(NE)))
                    {
                        tileIndexes.add(index);
                        tilesChecked.add(NE);
                    }
                }
                if (ok(tileX + 1, tileY + 1))
                {
                    int index = calculateTileIndex(tileX + 1, tileY + 1);
                    TILE SE = tile.get(index);
                    if (!(tilesChecked.contains(SE)))
                    {
                        tileIndexes.add(index);
                        tilesChecked.add(SE);
                    }
                }

                if (ok(tileX - 1, tileY + 1))
                {
                    int index = calculateTileIndex(tileX - 1, tileY + 1);
                    TILE SW = tile.get(index);
                    if (!(tilesChecked.contains(SW)))
                    {
                        tileIndexes.add(index);
                        tilesChecked.add(SW);
                    }
                }
            }
            if (logger.isDebugEnabled())
                logger.debug("Tiles checked: " + tilesChecked.size());
            //long now = System.nanoTime();
            //Now we'll see if any of our new tiles contain a city; if so, add
            //them to the list (and we're done with the loop)
            //We start at max b/c that was the old maximum size of tilesChecked.
            //We already checked once if a city was in the tilesChecked list
            //up until position max
            for (int j = max; j < tilesChecked.size(); j++)
            {
                for (int i = 0; i < tilesChecked.get(j).citiesWithInfluence.size(); i++)
                {
                    int xPos = calculateTilePosition(tileIndexes.get(j)).width;
                    int yPos = calculateTilePosition(tileIndexes.get(j)).height;
                    CITY thisCity = city.get(tilesChecked.get(j).citiesWithInfluence.get(i));
                    //if (city.get(i).x == xPos && city.get(i).y == yPos)
                    if (thisCity.getX() == xPos && thisCity.getY() == yPos && startTile.citiesWithInfluence.contains(tilesChecked.get(j).citiesWithInfluence.get(i)))
                        influencingCities.add(thisCity);
                }
            }
            //long later = System.nanoTime();
            //long time = (later - now);
            //nested+=time;
            if (influencingCities.size() > 0)
            {
                //long ms = nested/1000;
                //long totalMS = (System.nanoTime() - start)/1000;
                //long loopTime = (System.nanoTime() - startLoop);
                //totalLoop+=loopTime;
                //totalLoop = totalLoop/1000;
                //if (logger.isDebugEnabled())
                //{
                //    logger.info("Took " + preLoop + " microseconds before loop");
                //    logger.info("Took " + ms + " microseconds in deep loop");
                //    logger.info("Took " + totalLoop + " microseconds in loop");
                //    logger.info("Took " + totalMS + " microseconds total");
                //}
                //logger.info("Finished loop; last iteration: " + loopTime);
                return influencingCities;
            }
            radius++;
            if (radius > 7) //there ain't none
            {
                logger.warn("Hit max for " + x + ", " + y);
                return influencingCities;
            }
            startSearch=max;
            //logger.info("radius: " + radius);
            //if (radius > 5)
            //   logger.info("Long time no victory at " + x + ", " + y);
            //logger.info("New radius; last iteration: " + loopTime);
        }
    }

    private int calculateTileIndex(Dimension dim)
    {
        return calculateTileIndex(dim.width, dim.height);
    }


    public int calculateTileIndex(int xPos, int yPos)
    {
        //TODO: Look at the world wrap settings, don't assume X wrapping
        //Through version 0.82, we simply said if it's off the map, we can't do nothin'
        //However, we should wrap east/west, since the world is not flat (most of the time, anyway)
        //if (xPos >= worldMap.get(0).width || yPos >= worldMap.get(0).height || xPos < 0 || yPos < 0)
        if (yPos >= worldMap.get(0).height || yPos < 0)
            return -1;
        if (xPos >= worldMap.get(0).width)  //wrap it back to the other side of the world
            xPos = xPos - worldMap.get(0).width;
        if (xPos < 0)   //went past the date line going west
            xPos = xPos + worldMap.get(0).width;
        int index = 0;
        //always add in a width-worth * yPos/2 (truncated)
        index+=((yPos/2)*(worldMap.get(0).width));
        if (yPos % 2 == 1)  //add in half a width worth of tiles
            index+=((worldMap.get(0).width)/2);
        index+=xPos/2;
        return index;
    }
    
    /**
     * Convenience method that returns the tile at a given index.  Wraps around
     * the world if need be.
     * @param xPos The xlocation
     * @param yPos The ylocation
     * @return The tile at that location, or null if the location is not valid
     */
    public TILE getTileAt(int xPos, int yPos) {
        int tileIndex = calculateTileIndex(xPos, yPos);
        if (tileIndex == -1) {
            return null;
        }
        return tile.get(tileIndex);
    }
    
    /**
     * Returns the 8 surrounding tiles.
     * If some of them are off the map, it returns fewer.
     * May be expanded to the 21 tiles cross at one point.
     * Could also expand to be the culture borders (out to 6 levels).
     * @return 
     */
    public ArrayList<Integer> getSurroundingTiles(int x, int y)
    {
        ArrayList<Integer>tiles = new ArrayList<Integer>(0);
        int north = calculateTileIndex(x, y - 2);
        if (north != -1)
            tiles.add(north);
        int northEast = calculateTileIndex(x + 1, y - 1);
        if (northEast != -1)
            tiles.add(northEast);
        int east = calculateTileIndex(x + 2, y);
        if (east != -1)
            tiles.add(east);
        int southEast = calculateTileIndex(x + 1, y + 1);
        if (southEast != -1)
            tiles.add(southEast);
        int south = calculateTileIndex(x, y + 2);
        if (south != -1)
            tiles.add(south);
        int southWest = calculateTileIndex(x - 1, y + 1);
        if (southWest != -1)
            tiles.add(southWest);
        int west = calculateTileIndex(x - 2, y);
        if (west != -1)
            tiles.add(west);
        int northWest = calculateTileIndex(x - 1, y - 1);
        if (northWest != -1)
            tiles.add(northWest);
        return tiles;
    }
    
    /**
     * Returns the tiles to the north, NE, and NW of this one.
     * These are the ones that combine to form the graphics image of the given tile.
     * @param x
     * @param y
     * @return 
     */
    public ArrayList<Integer>getNorthNENW(int x, int y)
    {
        ArrayList<Integer>tiles = new ArrayList<Integer>(0);
        int north = calculateTileIndex(x, y - 2);
        if (north != -1)
            tiles.add(north);
        int northEast = calculateTileIndex(x + 1, y - 1);
        if (northEast != -1)
            tiles.add(northEast);
        int northWest = calculateTileIndex(x - 1, y - 1);
        if (northWest != -1)
            tiles.add(northWest);
        return tiles;
    }

    public Dimension calculateTilePosition(int index)
    {
        int row = index/(worldMap.get(0).width/2);
        int column = index%(worldMap.get(0).width/2) * 2;
        if (row % 2 == 1)
            column++;
        return new Dimension(column, row);
    }

    private boolean okX(int x)
    {
        return x < worldMap.get(0).width && x >= 0;
    }

    private boolean okY(int y)
    {
        return y < worldMap.get(0).height && y >= 0;
    }

    public boolean ok(int x, int y)
    {
        return okX(x) && okY(y);
    }
    
    /**
     * TODO: Make sure this is working l33tly.  Previous issues to verify that are fixed.
     *  - FIXED: Crash when loading in game.  Pretty sure it's because of something not being set (or set correctly) that needs to be set.
     *  - Have been comparing Firaxis saves to try to set up things correctly. ??--? Not that it does load OK in Firaxis's editor. Wonder
     *    what'll happen if I save one there and then load it up?
     *  - VERIFY: When I add a city immediately after creating a map things blow up.  WOrks if I reload the editor first.
     * @param width
     * @param height
     * @param baseTerrId 
     */
    public void createMap(int width, int height, int baseTerrId)
    {
        this.tile = new ArrayList<TILE>();
        logger.info("Adding " + ((width * height)/2) + " tiles");
        int tileIndex = 0;
        for (int y = 0; y < height; y++) {
            for (int x = (y % 2 == 0 ? 0 : 1); x < width; x+=2) {
                TILE newTile = new TILE(this, baseTerrId);
                newTile.xPos = x;
                newTile.yPos = y;
                newTile.index = tileIndex;
                tile.add(newTile);
                tileIndex++;
            }
        }
        
        WMAP wmap = new WMAP(this);
        wmap.width = width;
        wmap.height = height;
        wmap.questionMark1 = (int)(0.7 * height);
        wmap.numResources = this.resource.size();
        for (int i = 0; i < wmap.numResources; i++)
            wmap.resourceOccurence.add(0);
        wmap.dataLength+=wmap.numResources*4;
        wmap.flags = 5; //wrap x and have polar ice caps
        wmap.extractEnglish();
        wmap.numCivs = 8;   //TODO: Flexify this
        
        this.continent = new ArrayList<CONT>();
        continent.add(new CONT(this));
        this.city = new ArrayList<CITY>();
        this.colony = new ArrayList<CLNY>();
        this.mapUnit = new ArrayList<UNIT>();
        this.worldCharacteristic = new ArrayList<WCHR>();
        worldCharacteristic.add(new WCHR(this));
        this.worldMap = new ArrayList<WMAP>();
        worldMap.add(wmap);
        this.startingLocation = new ArrayList<SLOC>();
        
        this.numWorldCharacteristics = 1;
        this.numWorldMaps = 1;
        this.numTiles = (width *height)/2;
        
        this.hasCustomMap = true;
        
        logger.info("Added map");
        
    }

    private void setCurrentCharset(String currentCharset)
    {
        this.currentCharset = currentCharset;
    }
    
    public void setLanguage(String language)
    {
        if (languageToCharsetMap.containsKey(language));
            setCurrentCharset(languageToCharsetMap.get(language));
    }
    
    public PediaIconsFile getPediaIcons() {
        return pediaIcons;
    }
    
    public void setPediaIcons(PediaIconsFile file) {
        this.pediaIcons = file;
    }

    public ScriptFile getScriptFile() {
        return scriptFile;
    }

    public void setScriptFile(ScriptFile scriptFile) {
        this.scriptFile = scriptFile;
    }
    
    /**
     * Performs all actions necessary to add a starting location.
     * All code paths to do so should call this method.
     */
    public void addStartingLocation(TILE tile) {
        SLOC newSLOC = new SLOC(this);
        newSLOC.setX(tile.xPos);
        newSLOC.setY(tile.yPos);
        //Who should own it???
        if (tile.owner != -1) {
            newSLOC.setOwner(tile.owner);
            newSLOC.setOwnerType(tile.ownerType);
        }
        else {    //default fallback option
            newSLOC.setOwnerType(CITY.OWNER_CIV);
            newSLOC.setOwner(1);    //first one after barbs
        }
        this.startingLocation.add(newSLOC);
        tile.setStartingLocation(true);
    }
}