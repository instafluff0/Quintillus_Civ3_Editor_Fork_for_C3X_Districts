
package com.civfanatics.civ3.savFile;

import com.civfanatics.civ3.biqFile.util.LittleEndianDataInputStream;

import java.io.IOException;
import org.apache.log4j.Logger;

/**
 * Represents player info in the SAV.
 * @author Andrew
 */
public class SavLEAD {
    
    static Logger logger = Logger.getLogger(SavLEAD.class);
    
    private static int MAX_CULT_SEARCH_SIZE = 0x3000;
    
    private static int THIRTY_TWO = 32;
    
    private String header;
    private int dataLength;
    private int playerID;
    private int raceID;
    private int unknown1;
    private int power;
    private int capitalCity;
    private int difficulty;
    private int unknown2;
    private int unknown2PartTwo;
    private int goldenAgeEndTurn;
    private int playerFlags;
    private int goldPart1;
    private int goldPart2;
    private float score;
    private int[] unknown76 = new int[19];    //76 bytes; 19 ints
    private int turnsOfAnarchy;
    private int government;
    private int mobilization;
    private int[] secondUnknown76 = new int[19];  //76 bytes; 19 ints
    private int era;
    private int beakers;    //towards current research
    private int researching;
    private int turnsResearched;    //on current tech
    private int futureTechsKnown;
    private short[] unitsPerStratOwned = new short[32];
    private short[] unitsPerStratBeingBuilt = new short[32];
    private int numArmies;
    private int numUnits;
    private int unknown3;
    private int numCities;
    private int numColonies;
    private int numConts;
    private int unknown4;
    private int luxuryRate;
    private int scienceRate;
    private int taxRate;
    
    //Attitude
    private int[] attUnknown1 = new int[THIRTY_TWO];
    private int[] attCanceledDeal = new int[THIRTY_TWO];
    private int[] attUnknown2 = new int[THIRTY_TWO];
    private int[] attCaughtSpy = new int[THIRTY_TWO];
    private int[] attUnknown3 = new int[THIRTY_TWO];
    private int[] attTribute = new int[THIRTY_TWO];
    private int[] attUnknown4 = new int[THIRTY_TWO];
    private int[] attGift = new int[THIRTY_TWO];
    private int[] attUnknown5 = new int[THIRTY_TWO];
    private int[] attICBM = new int[THIRTY_TWO];
    private int[] attICBMOther = new int[THIRTY_TWO];
    //skip 24 bytes per LEAD of unknown Attitude
    
    //128 bytes skipped
    private int[] refuseContact = new int[THIRTY_TWO];
    //128 bytes skipped
    private int[] warWearinessPoints = new int[THIRTY_TWO];
    private byte[] warStatus = new byte[THIRTY_TWO];
    private byte[] embassies = new byte[THIRTY_TWO];
    private byte[] spies = new byte[THIRTY_TWO];
    private byte[] failedSpyMission = new byte[THIRTY_TWO]; //1 if failed spy mission vs current LEAD this turn
    private int[] borderViolation = new int[THIRTY_TWO];
    private int[] goldToLead = new int[THIRTY_TWO]; //GPT to each LEAD
    private int[] contact = new int[THIRTY_TWO];
    private int[] agreements = new int[THIRTY_TWO]; //binary mask
    private int[] alliances = new int[THIRTY_TWO];
    private int[] embargoes = new int[THIRTY_TWO];
    //skip 72
    private int color;
    private String leaderName;  //THIRTY_TWO
    private String leaderTitle; //24
    private String name;    //40
    private String noun;    //40
    private String adjective;   //40
    private int gender;   //0 = male, 1 = female
    private int skipped1;
    private int skipped2;
    private int skipped3;
    private int skipped4;
    private int skipped5;
    private int skipped6;
    //skip 24 cumulatively (above).  including to verify place in data)
    
    //save 18 and beyond
    private int unknown18_1;
    private int victoryPoints;
    private int unknown18_2;
    private int vpFromLocation;
    private int vpFromCapture;
    //Skip 40... are there other victory point sources?
    
    //18.06+
    String password;    //228
    //skip 32
    private int foundedCities;
    
    //N.B. Gets tricky here, documentation is a little ambiguous
    private int remainingHeaderLength;  //?????
    private int[] numDiploEntries;  //per player
    private DiploTableEntry[][] diploTableEntries; //array per player
    
    //If not barbarians...
    //Per building...
    private short[] buildingCount;
    private short[] buildingsInConstruction;
    private short[] buildingData;   //unknown what this represents
    private int[] SWCity;
    private byte[] SWBuilt;
    //Per PRTO...
    private short[] unitCount;
    private short[] unitsInConstruction;
    private short[] unitData;   //unknown what this represents
    //Per Spaceship Part
    private short[] ssPartsData;
    
    //Trade Network Data
    private TradeNetworkData tradeNetworkData;
    private byte[] goodsAvailable;  //# available for trade w/o losing supply
    
    //Skip 16 * numCONT
    private int[] citiesPerCont;
    
    private String cultHeader;  //This will allow us to verify that things are working!
    private int cultLength;
    private int cultUnknown;
    private int totalCulture;
    private int cultureGainedLastTurn;
    private int cultUnknown2;
    
    //ESPN is not at all documented beyond header length, but I suspect it has something
    //to do with successful espionage missions.  There's gotta be somewhere to keep track
    //of stealing unit positions, right?
    private String espnIndicator;
    private int espnLength;
    private int espnUnknown;
    private String espn2;
    private int espn2Length;
    private int espn2Unknown;
    
    private int scienceQueueSize;
    private int[] scienceQueue;
    
    private int[] numUnknownTriplet1Entries = new int[THIRTY_TWO];
    private DiploTableEntry[][] unknownDiploStruct;
    private int[] numUnknownTriplet2Entries = new int[THIRTY_TWO];
    private DiploTableEntry[][] unknownDiploStruct2;
    
    //Save version >= 24
    //Skip 36 bytes
    
    byte[] inputFour = new byte[4];
    public void readDataSection(LittleEndianDataInputStream in, int numPrtos, int numBuildings, int numSSParts, int numGoods, int numPlayers) throws IOException {
        in.mark(6000);
        //THIRTY_TWO = numPlayers;
        in.read(inputFour, 0, 4);
        header = new String(inputFour, "Windows-1252");
        assert header.equals("LEAD") : header;
        
        dataLength = in.readInt();
        playerID = in.readInt();
        raceID = in.readInt();
        unknown1 = in.readInt();
        power = in.readInt();
        capitalCity = in.readInt();
        difficulty = in.readInt();
        unknown2 = in.readInt();
        unknown2PartTwo = in.readInt();
        goldenAgeEndTurn = in.readInt();
        playerFlags = in.readInt(); //N.B. Documented in thread
        goldPart1 = in.readInt();
        goldPart2 = in.readInt();
        score = in.readFloat();
        for (int i = 0; i < 19; i++) {
            unknown76[i] = in.readInt();
        }
        turnsOfAnarchy = in.readInt();
        government = in.readInt();
        mobilization = in.readInt();
        for (int i= 0; i < 19; i++) {
            secondUnknown76[i] = in.readInt();
        }
        era = in.readInt();
        beakers = in.readInt();
        researching = in.readInt();
        turnsResearched = in.readInt();
        futureTechsKnown = in.readInt();
        for (int i = 0; i < 32; i++) {
            unitsPerStratOwned[i] = in.readShort();
            unitsPerStratBeingBuilt[i] = in.readShort();
        }
        numArmies = in.readInt();
        numUnits = in.readInt();
        unknown3 = in.readInt();
        numCities = in.readInt();
        numColonies = in.readInt();
        numConts = in.readInt();
        unknown4 = in.readInt();
        luxuryRate = in.readInt();
        scienceRate = in.readInt();
        taxRate = in.readInt();
        for (int i = 0; i < THIRTY_TWO; i++) {
            attUnknown1[i] = in.readInt();
            attCanceledDeal[i] = in.readInt();
            attUnknown2[i] = in.readInt();
            in.skipBytes(4);
            attCaughtSpy[i] = in.readInt();
            attUnknown3[i] = in.readInt();
            attTribute[i] = in.readInt();
            attUnknown4[i] = in.readInt();
            in.skipBytes(4);
            attGift[i] = in.readInt();
            attUnknown5[i] = in.readInt();
            attICBM[i] = in.readInt();
            attICBMOther[i] = in.readInt();
            in.skipBytes(24);
        }
        in.skipBytes(128);
        for (int i = 0; i < THIRTY_TWO; i++) {
            refuseContact[i] = in.readInt();
        }
        in.skipBytes(128);
        for (int i =0; i < THIRTY_TWO; i++) {
            warWearinessPoints[i] = in.readInt();
        }
        for (int i = 0; i < THIRTY_TWO; i++) {
            warStatus[i] = in.readByte();
        }
        for (int i = 0; i < THIRTY_TWO; i++) {
            embassies[i] = in.readByte();
        }
        for (int i = 0; i < THIRTY_TWO; i++) {
            spies[i] = in.readByte();
        }
        for (int i = 0; i < THIRTY_TWO; i++) {
            failedSpyMission[i] = in.readByte();
        }
        for (int i = 0; i < THIRTY_TWO; i++) {
            borderViolation[i] = in.readInt();
        }
        for (int i = 0; i < THIRTY_TWO; i++) {
            goldToLead[i] = in.readInt();
        }
        for (int i = 0; i < THIRTY_TWO; i++) {
            contact[i] = in.readInt();
        }
        for (int i = 0; i < THIRTY_TWO; i++) {
            agreements[i] = in.readInt();
        }
        for (int i = 0; i < THIRTY_TWO; i++) {
            alliances[i] = in.readInt();
        }
        for (int i = 0; i < THIRTY_TWO; i++) {
            embargoes[i] = in.readInt();
        }
        in.skipBytes(72);
        color = in.readInt();
        byte[] tempBuffer = new byte[32];
        in.read(tempBuffer, 0, 32);
        leaderName = new String(tempBuffer, "Windows-1252");
        tempBuffer = new byte[24];
        in.read(tempBuffer, 0, 24);
        leaderTitle = new String(tempBuffer, "Windows-1252");
        tempBuffer = new byte[40];
        in.read(tempBuffer, 0, 40);
        name = new String(tempBuffer, "Windows-1252");
        in.read(tempBuffer, 0, 40);
        noun = new String(tempBuffer, "Windows-1252");
        in.read(tempBuffer, 0, 40);
        adjective = new String(tempBuffer, "Windows-1252");
        gender = in.readInt();
        skipped1 = in.readInt();
        skipped2 = in.readInt();
        skipped3 = in.readInt();
        skipped4 = in.readInt();
        skipped5 = in.readInt();
        skipped6 = in.readInt();
        
        //if 18+
        unknown18_1 = in.readInt();
        victoryPoints = in.readInt();
        unknown18_2 = in.readInt();
        vpFromLocation = in.readInt();
        vpFromCapture = in.readInt();
        in.skipBytes(40);
        
        //18.06+
        tempBuffer = new byte[228];
        in.read(tempBuffer, 0, 228);
        password = new String(tempBuffer, "Windows-1252");
        in.skipBytes(32);
        foundedCities = in.readInt();
        //Skip remaining header
        in.reset();
        in.skip(dataLength - 1);
        byte FF = in.readByte();
        
        
        if (false) {
            //TODO: Length seems to be variable and unpredictable
            //Can't reliably predict how many there will be; have seen both 0 - 30 and 0 - 33 (31/34 total)
            //Skipping for now.
            numDiploEntries = new int[THIRTY_TWO - 1];
            diploTableEntries = new DiploTableEntry[THIRTY_TWO - 1][];
            for (int i = 0; i < (THIRTY_TWO - 1); i++) {
                numDiploEntries[i] = in.readInt();
                logger.debug("Num diplo entries for LEAD " + i + " is " + numDiploEntries[i]);
                diploTableEntries[i] = new DiploTableEntry[numDiploEntries[i]];
                for (int d = 0; d < numDiploEntries[i]; d++) {
                    logger.trace("LEAD: " + i + "; diplo table entry: " + d);
                    diploTableEntries[i][d] = new DiploTableEntry();
                    diploTableEntries[i][d].setEntryType(in.readInt());
                    diploTableEntries[i][d].setData1(in.readInt());
                    diploTableEntries[i][d].setData2(in.readInt());
                }
            }
        }
        
        //Need to move this inside our CULT search b/c we're jumping forward
        //if (raceID != -1) {
            //Search for a CULT and join it
            byte[] preCult = new byte[MAX_CULT_SEARCH_SIZE];
            in.mark(MAX_CULT_SEARCH_SIZE + 1);
            in.read(preCult, 0, MAX_CULT_SEARCH_SIZE);
            int c = 0;
            for (c = 0; c < MAX_CULT_SEARCH_SIZE - 3; c++) {
                String string = new String(preCult, c, 4);
                if ("CULT".equals(string)) {
                    break;
                }
            }
            in.reset();
            in.skipBytes(c);
            
            
            //Search backwards for this eventually.  For now, see if the rest works.
            //readNonBarbStuff(in, numPrtos, numBuildings, numSSParts, numGoods, numPlayers);
        //}   //End if not barbarians
        
        in.read(inputFour, 0, 4);
        cultHeader = new String(inputFour, "Windows-1252");
        logger.debug("Cult header: " + cultHeader);
        assert cultHeader.equals("CULT") : cultHeader;
        cultLength = in.readInt();
        cultUnknown = in.readInt();
        totalCulture = in.readInt();
        cultureGainedLastTurn = in.readInt();
        cultUnknown2 = in.readInt();
        
        in.read(inputFour, 0, 4);
        espnIndicator = new String(inputFour, "Windows-1252");
        assert(espnIndicator.equals("ESPN"));
        espnLength = in.readInt();
        in.skipBytes(espnLength);
        //espnUnknown = in.readInt();
        in.read(inputFour, 0, 4);
        espn2 = new String(inputFour, "Windows-1252");
        assert(espn2.equals("ESPN"));
        espn2Length = in.readInt();
        in.skipBytes(espn2Length);
        //espn2Unknown = in.readInt();
        
        scienceQueueSize = in.readInt();
        scienceQueue = new int[scienceQueueSize];
        for (int s = 0; s < scienceQueueSize; s++) {
            scienceQueue[s] = in.readInt();
        }
        
        //Unknown triplets
        unknownDiploStruct = new DiploTableEntry[THIRTY_TWO][];
        for (int i = 0; i < THIRTY_TWO; i++) {
            numUnknownTriplet1Entries[i] = in.readInt();
            unknownDiploStruct[i] = new DiploTableEntry[numUnknownTriplet1Entries[i]];
            for (int d = 0; d < numUnknownTriplet1Entries[i]; d++) {
                unknownDiploStruct[i][d].setEntryType(in.readInt());
                unknownDiploStruct[i][d].setData1(in.readInt());
                unknownDiploStruct[i][d].setData2(in.readInt());
            }
        }
        unknownDiploStruct2 = new DiploTableEntry[THIRTY_TWO][];
        for (int i = 0; i < THIRTY_TWO; i++) {
            numUnknownTriplet2Entries[i] = in.readInt();
            unknownDiploStruct2[i] = new DiploTableEntry[numUnknownTriplet2Entries[i]];
            for (int d = 0; d < numUnknownTriplet2Entries[i]; d++) {
                unknownDiploStruct2[i][d].setEntryType(in.readInt());
                unknownDiploStruct2[i][d].setData1(in.readInt());
                unknownDiploStruct2[i][d].setData2(in.readInt());
            }
        }
        
        //Version > 24
        in.skipBytes(36);
    }
    
    private void readNonBarbStuff(LittleEndianDataInputStream in, int numPrtos, int numBuildings, int numSSParts, int numGoods, int numPlayers) throws IOException {
        buildingCount = new short[numBuildings];
        buildingsInConstruction = new short[numBuildings];
        buildingData = new short[numBuildings];
        SWCity = new int[numBuildings];
        SWBuilt = new byte[numBuildings];
        for (int i = 0; i < numBuildings; i++) {
            buildingCount[i] = in.readShort();
        }
        for (int i = 0; i < numBuildings; i++) {
            buildingsInConstruction[i] = in.readShort();
        }
        for (int i = 0; i < numBuildings; i++) {
            buildingData[i] = in.readShort();
        }
        for (int i = 0; i < numBuildings; i++) {
            SWCity[i] = in.readInt();
        }
        for (int i = 0; i < numBuildings; i++) {
            SWBuilt[i] = in.readByte();
        }
        unitCount = new short[numPrtos];
        unitsInConstruction = new short[numPrtos];
        unitData = new short[numPrtos];
        for (int i = 0; i < numPrtos; i++) {
            unitCount[i] = in.readShort();
        }
        for (int i = 0; i < numPrtos; i++) {
            unitsInConstruction[i] = in.readShort();
        }
        for (int i = 0; i < numPrtos; i++) {
            unitData[i] = in.readShort();
        }
        ssPartsData = new short[numSSParts];
        for (int i = 0; i < numSSParts; i++) {
            ssPartsData[i] = in.readShort();
        }
        tradeNetworkData = new TradeNetworkData();
        tradeNetworkData.initialize(numGoods, THIRTY_TWO);
        for (int g = 0; g < numGoods; g++) {
            for (int p = 0; p < THIRTY_TWO; p++) {
                byte hasResouce = in.readByte();
                byte importExport = in.readByte();
                byte tradeable = in.readByte();
                tradeNetworkData.setInfo(g, p, hasResouce, importExport, tradeable);
            }
        }
        goodsAvailable = new byte[numGoods];
        for (int g = 0; g < numGoods; g++) {
            goodsAvailable[g] = in.readByte();
        }
        for (int c = 0; c < numConts; c++) {
            in.skipBytes(16);
        }
        citiesPerCont = new int[numConts];
        for (int c = 0; c < numConts; c++) {
            citiesPerCont[c] = in.readInt();
        }        
    }

    public String getHeader() {
        return header;
    }

    public int getDataLength() {
        return dataLength;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getRaceID() {
        return raceID;
    }

    public int getUnknown1() {
        return unknown1;
    }

    public int getPower() {
        return power;
    }

    public int getCapitalCity() {
        return capitalCity;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getUnknown2() {
        return unknown2;
    }

    public int getUnknown2PartTwo() {
        return unknown2PartTwo;
    }

    public int getGoldenAgeEndTurn() {
        return goldenAgeEndTurn;
    }

    public int getPlayerFlags() {
        return playerFlags;
    }

    public int getGoldPart1() {
        return goldPart1;
    }

    public int getGoldPart2() {
        return goldPart2;
    }

    public float getScore() {
        return score;
    }

    public int[] getUnknown76() {
        return unknown76;
    }

    public int getTurnsOfAnarchy() {
        return turnsOfAnarchy;
    }

    public int getGovernment() {
        return government;
    }

    public int getMobilization() {
        return mobilization;
    }

    public int[] getSecondUnknown76() {
        return secondUnknown76;
    }

    public int getEra() {
        return era;
    }

    public int getBeakers() {
        return beakers;
    }

    public int getResearching() {
        return researching;
    }

    public int getTurnsResearched() {
        return turnsResearched;
    }

    public int getFutureTechsKnown() {
        return futureTechsKnown;
    }

    public short[] getUnitsPerStratOwned() {
        return unitsPerStratOwned;
    }

    public short[] getUnitsPerStratBeingBuilt() {
        return unitsPerStratBeingBuilt;
    }

    public int getNumArmies() {
        return numArmies;
    }

    public int getNumUnits() {
        return numUnits;
    }

    public int getUnknown3() {
        return unknown3;
    }

    public int getNumCities() {
        return numCities;
    }

    public int getNumColonies() {
        return numColonies;
    }

    public int getNumConts() {
        return numConts;
    }

    public int getUnknown4() {
        return unknown4;
    }

    public int getLuxuryRate() {
        return luxuryRate;
    }

    public int getScienceRate() {
        return scienceRate;
    }

    public int getTaxRate() {
        return taxRate;
    }

    public int[] getAttUnknown1() {
        return attUnknown1;
    }

    public int[] getAttCanceledDeal() {
        return attCanceledDeal;
    }

    public int[] getAttUnknown2() {
        return attUnknown2;
    }

    public int[] getAttCaughtSpy() {
        return attCaughtSpy;
    }

    public int[] getAttUnknown3() {
        return attUnknown3;
    }

    public int[] getAttTribute() {
        return attTribute;
    }

    public int[] getAttUnknown4() {
        return attUnknown4;
    }

    public int[] getAttGift() {
        return attGift;
    }

    public int[] getAttUnknown5() {
        return attUnknown5;
    }

    public int[] getAttICBM() {
        return attICBM;
    }

    public int[] getAttICBMOther() {
        return attICBMOther;
    }

    public int[] getRefuseContact() {
        return refuseContact;
    }

    public int[] getWarWearinessPoints() {
        return warWearinessPoints;
    }

    public byte[] getWarStatus() {
        return warStatus;
    }

    public byte[] getEmbassies() {
        return embassies;
    }

    public byte[] getSpies() {
        return spies;
    }

    public byte[] getFailedSpyMission() {
        return failedSpyMission;
    }

    public int[] getBorderViolation() {
        return borderViolation;
    }

    public int[] getGoldToLead() {
        return goldToLead;
    }

    public int[] getContact() {
        return contact;
    }

    public int[] getAgreements() {
        return agreements;
    }

    public int[] getAlliances() {
        return alliances;
    }

    public int[] getEmbargoes() {
        return embargoes;
    }

    public int getColor() {
        return color;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public String getLeaderTitle() {
        return leaderTitle;
    }

    public String getName() {
        return name;
    }

    public String getNoun() {
        return noun;
    }

    public String getAdjective() {
        return adjective;
    }

    public int getGender() {
        return gender;
    }

    public int getSkipped1() {
        return skipped1;
    }

    public int getSkipped2() {
        return skipped2;
    }

    public int getSkipped3() {
        return skipped3;
    }

    public int getSkipped4() {
        return skipped4;
    }

    public int getSkipped5() {
        return skipped5;
    }

    public int getSkipped6() {
        return skipped6;
    }

    public int getUnknown18_1() {
        return unknown18_1;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public int getUnknown18_2() {
        return unknown18_2;
    }

    public int getVpFromLocation() {
        return vpFromLocation;
    }

    public int getVpFromCapture() {
        return vpFromCapture;
    }

    public String getPassword() {
        return password;
    }

    public int getFoundedCities() {
        return foundedCities;
    }

    public int getRemainingHeaderLength() {
        return remainingHeaderLength;
    }

    public int[] getNumDiploEntries() {
        return numDiploEntries;
    }

    public DiploTableEntry[][] getDiploTableEntries() {
        return diploTableEntries;
    }

    public short[] getBuildingCount() {
        return buildingCount;
    }

    public short[] getBuildingsInConstruction() {
        return buildingsInConstruction;
    }

    public short[] getBuildingData() {
        return buildingData;
    }

    public int[] getSWCity() {
        return SWCity;
    }

    public byte[] getSWBuilt() {
        return SWBuilt;
    }

    public short[] getUnitCount() {
        return unitCount;
    }

    public short[] getUnitsInConstruction() {
        return unitsInConstruction;
    }

    public short[] getUnitData() {
        return unitData;
    }

    public short[] getSsPartsData() {
        return ssPartsData;
    }

    public TradeNetworkData getTradeNetworkData() {
        return tradeNetworkData;
    }

    public byte[] getGoodsAvailable() {
        return goodsAvailable;
    }

    public int[] getCitiesPerCont() {
        return citiesPerCont;
    }

    public String getCultHeader() {
        return cultHeader;
    }

    public int getCultLength() {
        return cultLength;
    }

    public int getCultUnknown() {
        return cultUnknown;
    }

    public int getTotalCulture() {
        return totalCulture;
    }

    public int getCultureGainedLastTurn() {
        return cultureGainedLastTurn;
    }

    public int getCultUnknown2() {
        return cultUnknown2;
    }

    public String getEspnIndicator() {
        return espnIndicator;
    }

    public int getEspnLength() {
        return espnLength;
    }

    public int getEspnUnknown() {
        return espnUnknown;
    }

    public String getEspn2() {
        return espn2;
    }

    public int getEspn2Length() {
        return espn2Length;
    }

    public int getEspn2Unknown() {
        return espn2Unknown;
    }

    public int getScienceQueueSize() {
        return scienceQueueSize;
    }

    public int[] getScienceQueue() {
        return scienceQueue;
    }

    public int[] getNumUnknownTriplet1Entries() {
        return numUnknownTriplet1Entries;
    }

    public DiploTableEntry[][] getUnknownDiploStruct() {
        return unknownDiploStruct;
    }

    public int[] getNumUnknownTriplet2Entries() {
        return numUnknownTriplet2Entries;
    }

    public DiploTableEntry[][] getUnknownDiploStruct2() {
        return unknownDiploStruct2;
    }

    public byte[] getInputFour() {
        return inputFour;
    }
    
    
}
