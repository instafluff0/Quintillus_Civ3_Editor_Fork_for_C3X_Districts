
package com.civfanatics.civ3.savFile;

import com.civfanatics.civ3.biqFile.IO;
import com.civfanatics.civ3.biqFile.util.LittleEndianDataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Game Data - GAME in the SAV file.
 * Not to be confused with GAME in the BIQ, which can also be embedded in a SAV.
 * Based on the documentation by Gramphos et. al.
 * @author Quintillus
 */
public class GameData {
    private String currentCharset = "Windows-1252";//ISO-8859-1";
    
    private int dataLength;
    private int unknown1;   //changes a lot, but can be unchanged too
    private int preferences;    //binary
        //enumerate here
    private int rules;  //binary
        //enumerate here
    private int unknown2;
    private int difficulty; //the difficulty level
    private int unknown3;
    private int numberOfUnits;  //total # of units in game
    private int numberOfCities; //total # of cities in game
    private int numberOfColonies;   //total # of colonies in game
    private int nukesUsed;
    private int globalWarmingState; //0 = no sun, 1 = yellow, 2 = yellow with red border, 3 = red
    private int victoryType;    //-1 = game ongoing
    private int winner; //ID of winner (from LEAD); -1 if none yet
    private int unknown4;
    private int turnNumber;
    private int gameYear;   //prior to PTW; unused since (probably)
    private int randomSeed;
    private int unknown5;
    private int humanPlayers;   //bitmap of playernumber.  1 = alive human; 0  AI/barbarian/dead
    private int remainingPlayers;   //bitmap of playernumber of remaining
    private int remainingRaces; //bitmap of civ id of remaining civs
    private String unknownString1;  //24 chars
    private byte powerbarCheck; // > 32 means Powerbar used.  Normally 0; 1 has been seen
    private byte megaTrainerXLCheck;    //> 0 means Mega Trainer used
    private String unknownString2;  //54; might not be a string?
    private int[] civRanking; //32 ints; some sort of ranking of the civs; likely uses RACE index, maybe LEAD
    private int numConts;
    private int numPlayers;
    
    //Begin save version > 18 (0x12)
    private int numAirbases;
    private int numVictoryLocations;
    private int numRadarTowers;
    private int numOutposts;
    private int nextPlayerID;   //for multiplayer
    private byte unknownByte1;
    
    //Begin save version > 18.06
    private String adminPassword;   //228 bytes
    private String unknownString3;  //39 bytes
    private int victoryPointLimit;
    private int turnLimit;
    private int timePlayed; //in milliseconds
    
    //Begin save version > 24.8
    private String unknownString4;  //204 bytes (ints?)
    private int cityEliminationCount;
    private int oneCityCultureWin;  //threshold?
    private int allCitiesCultureWin;    //threshold
    private int dominationTerrain;
    private int dominationPopulation;
    private int wonderCost;
    private int defeatingOpposingUnitCost;
    private int advancementCost;
    private int cityConquestPopulation;
    private int victoryPointScoring;
    private int capturingSpecialUnit;
    
    //Resume all versions
    List<Integer> citiesPerContinent;   //int per CONT
    List<Integer> knownTechs;   //presumably bitmap, of which civs know which techs.  One per TECH
    List<Integer> greatWonderCities; //one per BLDG; CITY ID of city containing that wonder (BLDG)
    List<Byte> greatWonderBuild;    //1 if built; 0 if not.  Would allow preventing rebuilding when destroyed
    List<Integer> buildingData1;    //AI helper values? (Derived from rules?) (may be flags)
    List<Integer> buildingData2;    //AI helper values? (Derived from rules?) (may be flags)
    List<Integer> prototypeStrategy1;   //AI helper values? Is derivablefrom rules
    List<Integer> prototypeStrategy2;   //AI helper values? Is derivablefrom rules (also alternate uses for prototypes)
    List<Integer> techData; //At the end of the GAME data, right before CNSL. (AI helpers?)
    
    //For ref
    private IO biqRules;
    
    public void readGameDataSection(LittleEndianDataInputStream in, int majorVersion, int minorVersion, IO biqRules) throws IOException {
        this.biqRules = biqRules;
        byte[]buffer;
        dataLength = in.readInt();
        unknown1 = in.readInt();
        preferences = in.readInt();
        rules = in.readInt();
        unknown2 = in.readInt();
        difficulty = in.readInt();
        unknown3 = in.readInt();
        numberOfUnits = in.readInt();
        numberOfCities = in.readInt();
        numberOfColonies = in.readInt();
        nukesUsed = in.readInt();
        globalWarmingState = in.readInt();
        victoryType = in.readInt();
        winner = in.readInt();
        unknown4 = in.readInt();
        turnNumber = in.readInt();
        gameYear = in.readInt();
        randomSeed = in.readInt();
        unknown5 = in.readInt();
        humanPlayers = in.readInt();
        remainingPlayers = in.readInt();
        remainingRaces = in.readInt();
        
        unknownString1 = IOUtils.importString(in, 24, currentCharset);
        powerbarCheck = in.readByte();
        megaTrainerXLCheck = in.readByte();
        unknownString2 = IOUtils.importString(in, 54, currentCharset);
        civRanking = new int[32];
        for (int i = 0; i < 32; i++) {
            civRanking[i] = in.readInt();
        }
        
        numConts = in.readInt();
        numPlayers = in.readInt();
        
        if (majorVersion >= 18) {
            numAirbases = in.readInt();
            numVictoryLocations = in.readInt();
            numRadarTowers = in.readInt();
            numOutposts = in.readInt();
            nextPlayerID = in.readInt();
            unknownByte1 = in.readByte();
            if (minorVersion >= 6 || majorVersion > 18) {
                adminPassword = IOUtils.importString(in, 228, currentCharset);
                unknownString3 = IOUtils.importString(in, 39, currentCharset);
                victoryPointLimit = in.readInt();
                turnLimit = in.readInt();
                timePlayed = in.readInt();
            }
        }
        if (majorVersion >= 24) {
            if (minorVersion >= 8 || majorVersion > 25) {
                unknownString4 = IOUtils.importString(in, 204, currentCharset);
                cityEliminationCount = in.readInt();
                oneCityCultureWin = in.readInt();
                allCitiesCultureWin = in.readInt();
                dominationTerrain = in.readInt();
                dominationPopulation = in.readInt();
                wonderCost = in.readInt();
                defeatingOpposingUnitCost = in.readInt();
                advancementCost = in.readInt();
                cityConquestPopulation = in.readInt();
                victoryPointScoring = in.readInt();
                capturingSpecialUnit = in.readInt();
            }
        }
        citiesPerContinent = new ArrayList<Integer>(numConts);
        for (int i = 0; i < numConts; i++) {
            citiesPerContinent.add(in.readInt());
        }
        knownTechs = new ArrayList<Integer>(biqRules.technology.size());
        for (int t = 0; t < biqRules.technology.size(); t++) {
            knownTechs.add(in.readInt());
        }
        greatWonderCities = new ArrayList<Integer>(biqRules.buildings.size());
        for (int b = 0; b < biqRules.buildings.size(); b++) {
            greatWonderCities.add(in.readInt());
        }
        greatWonderBuild = new ArrayList<Byte>(biqRules.buildings.size());
        for (int b = 0; b < biqRules.buildings.size(); b++) {
            greatWonderBuild.add(in.readByte());
        }
        buildingData1 = new ArrayList<Integer>(biqRules.buildings.size());
        for (int b = 0; b < biqRules.buildings.size(); b++) {
            buildingData1.add(in.readInt());
        }
        buildingData2 = new ArrayList<Integer>(biqRules.buildings.size());
        for (int b = 0; b < biqRules.buildings.size(); b++) {
            buildingData2.add(in.readInt());
        }
        prototypeStrategy1 = new ArrayList<Integer>(biqRules.getNumFiraxisUnits());
        for (int u = 0; u < biqRules.getNumFiraxisUnits(); u++) {
            prototypeStrategy1.add(in.readInt());
        }
        prototypeStrategy2 = new ArrayList<Integer>(biqRules.getNumFiraxisUnits());
        for (int u = 0; u < biqRules.getNumFiraxisUnits(); u++) {
            prototypeStrategy2.add(in.readInt());
        }
        techData = new ArrayList<Integer>(biqRules.technology.size());
        for (int t = 0; t < biqRules.technology.size(); t++) {
            techData.add(in.readInt());
        }
    }
    
    /**
     * Friendly output.
     */
    public String toEnglish() {
        StringBuilder sb = new StringBuilder();
        
        //TODO: Initial stuff
        sbAppend(sb, "Number of CONTs: " + numConts);
        sbAppend(sb, "Number of players: " + numPlayers);
        sbAppend(sb, "Begin save version 18 items");
        sbAppend(sb, "Number of airbases: " + numAirbases);
        sbAppend(sb, "Number of VP Locations: " + numVictoryLocations);
        sbAppend(sb, "Number of radar towers: " + numRadarTowers);
        sbAppend(sb, "Number of outposts: " + numOutposts);
        sbAppend(sb, "ID of next player (MP): " + nextPlayerID);
        sbAppend(sb, "Unknown byte: " + unknownByte1);
        sbAppend(sb, "Begin save version 18.06 items");
        sbAppend(sb, "Admin password: " + adminPassword);
        sbAppend(sb, "Unknown string (39): " + unknownString3);
        sbAppend(sb, "VP Limit: " + victoryPointLimit);
        sbAppend(sb, "Turn limit: " + turnLimit);
        sbAppend(sb, "Time played: " + timePlayed/60000 + " minutes");
        sbAppend(sb, "Begin save version 24.8 items");
        sbAppend(sb, "Unknown String (204): " + unknownString4);
        sbAppend(sb, "City elimination count: " + cityEliminationCount);
        sbAppend(sb, "One-city culture win: " + oneCityCultureWin);
        sbAppend(sb, "All cities culture win: " + allCitiesCultureWin);
        sbAppend(sb, "Domination terrain: " + dominationTerrain);
        sbAppend(sb, "Wonder cost: " + wonderCost);
        sbAppend(sb, "Defeat opposing unit cost: " + defeatingOpposingUnitCost);
        sbAppend(sb, "Advancement cost: " + advancementCost);
        sbAppend(sb, "City conquest population: " + cityConquestPopulation);
        sbAppend(sb, "Victory point scoring: " + victoryPointScoring);
        sbAppend(sb, "Capturing special unit: " + capturingSpecialUnit);
        sbAppend(sb, "---- DATA AREA ---");
        sbAppend(sb, "-> Cities per continent");
        for (int i = 0; i < citiesPerContinent.size(); i++) {
            sbAppend(sb, citiesPerContinent.get(i).toString());
        }
        sbAppend(sb, "-> Known techs");
        for (int i = 0; i < knownTechs.size(); i++) {
            sbAppend(sb, biqRules.technology.get(i).getName() + ": " + knownTechs.get(i));
        }
        sbAppend(sb, "-> Great Wonder Cities");
        for (int i = 0; i < greatWonderCities.size(); i++) {
            sbAppend(sb, biqRules.buildings.get(i).getName() + ": " + greatWonderCities.get(i));
        }
        sbAppend(sb, "-> Great Wonder Built");
        for (int i = 0; i < greatWonderBuild.size(); i++) {
            sbAppend(sb, biqRules.buildings.get(i).getName() + ": " + greatWonderBuild.get(i));
        }
        sbAppend(sb, "-> Building Data 1");
        for (int i = 0; i < buildingData1.size(); i++) {
            sbAppend(sb, biqRules.buildings.get(i).getName() + ": " +  buildingData1.get(i));
        }
        sbAppend(sb, "-> Building Data 2");
        for (int i = 0; i < buildingData2.size(); i++) {
            sbAppend(sb, biqRules.buildings.get(i).getName() + ": " +  buildingData2.get(i));
        }
        sbAppend(sb, "-> Prototype Strategy 1");
        for (int i = 0; i < prototypeStrategy1.size(); i++) {
            sbAppend(sb, prototypeStrategy1.get(i).toString());
        }
        sbAppend(sb, "-> Prototype Strategy 2");
        for (int i = 0; i < prototypeStrategy2.size(); i++) {
            sbAppend(sb, prototypeStrategy2.get(i).toString());
        }
        sbAppend(sb, "-> Tech Data");
        for (int i = 0; i < techData.size(); i++) {
            sbAppend(sb, biqRules.technology.get(i).getName() + ": " + techData.get(i));
        }
        
        return sb.toString();
    }
    
    static final String lineReturn = java.lang.System.getProperty("line.separator");
    
    private void sbAppend(StringBuilder sb, String string) {
        sb.append(string).append(lineReturn);
    }
    
    // <editor-fold desc="Getters/Setters">

    public int getDataLength() {
        return dataLength;
    }

    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }

    public int getUnknown1() {
        return unknown1;
    }

    public void setUnknown1(int unknown1) {
        this.unknown1 = unknown1;
    }

    public int getPreferences() {
        return preferences;
    }

    public void setPreferences(int preferences) {
        this.preferences = preferences;
    }

    public int getRules() {
        return rules;
    }

    public void setRules(int rules) {
        this.rules = rules;
    }

    public int getUnknown2() {
        return unknown2;
    }

    public void setUnknown2(int unknown2) {
        this.unknown2 = unknown2;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getUnknown3() {
        return unknown3;
    }

    public void setUnknown3(int unknown3) {
        this.unknown3 = unknown3;
    }

    public int getNumberOfUnits() {
        return numberOfUnits;
    }

    public void setNumberOfUnits(int numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    public int getNumberOfCities() {
        return numberOfCities;
    }

    public void setNumberOfCities(int numberOfCities) {
        this.numberOfCities = numberOfCities;
    }

    public int getNumberOfColonies() {
        return numberOfColonies;
    }

    public void setNumberOfColonies(int numberOfColonies) {
        this.numberOfColonies = numberOfColonies;
    }

    public int getNukesUsed() {
        return nukesUsed;
    }

    public void setNukesUsed(int nukesUsed) {
        this.nukesUsed = nukesUsed;
    }

    public int getGlobalWarmingState() {
        return globalWarmingState;
    }

    public void setGlobalWarmingState(int globalWarmingState) {
        this.globalWarmingState = globalWarmingState;
    }

    public int getVictoryType() {
        return victoryType;
    }

    public void setVictoryType(int victoryType) {
        this.victoryType = victoryType;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public int getUnknown4() {
        return unknown4;
    }

    public void setUnknown4(int unknown4) {
        this.unknown4 = unknown4;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public int getGameYear() {
        return gameYear;
    }

    public void setGameYear(int gameYear) {
        this.gameYear = gameYear;
    }

    public int getRandomSeed() {
        return randomSeed;
    }

    public void setRandomSeed(int randomSeed) {
        this.randomSeed = randomSeed;
    }

    public int getUnknown5() {
        return unknown5;
    }

    public void setUnknown5(int unknown5) {
        this.unknown5 = unknown5;
    }

    public int getHumanPlayers() {
        return humanPlayers;
    }

    public void setHumanPlayers(int humanPlayers) {
        this.humanPlayers = humanPlayers;
    }

    public int getRemainingPlayers() {
        return remainingPlayers;
    }

    public void setRemainingPlayers(int remainingPlayers) {
        this.remainingPlayers = remainingPlayers;
    }

    public int getRemainingRaces() {
        return remainingRaces;
    }

    public void setRemainingRaces(int remainingRaces) {
        this.remainingRaces = remainingRaces;
    }

    public String getUnknownString1() {
        return unknownString1;
    }

    public void setUnknownString1(String unknownString1) {
        this.unknownString1 = unknownString1;
    }

    public byte getPowerbarCheck() {
        return powerbarCheck;
    }

    public void setPowerbarCheck(byte powerbarCheck) {
        this.powerbarCheck = powerbarCheck;
    }

    public byte getMegaTrainerXLCheck() {
        return megaTrainerXLCheck;
    }

    public void setMegaTrainerXLCheck(byte megaTrainerXLCheck) {
        this.megaTrainerXLCheck = megaTrainerXLCheck;
    }

    public String getUnknownString2() {
        return unknownString2;
    }

    public void setUnknownString2(String unknownString2) {
        this.unknownString2 = unknownString2;
    }

    public int[] getCivRanking() {
        return civRanking;
    }

    public void setCivRanking(int[] civRanking) {
        this.civRanking = civRanking;
    }

    public int getNumConts() {
        return numConts;
    }

    public void setNumConts(int numConts) {
        this.numConts = numConts;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public int getNumAirbases() {
        return numAirbases;
    }

    public void setNumAirbases(int numAirbases) {
        this.numAirbases = numAirbases;
    }

    public int getNumVictoryLocations() {
        return numVictoryLocations;
    }

    public void setNumVictoryLocations(int numVictoryLocations) {
        this.numVictoryLocations = numVictoryLocations;
    }

    public int getNumRadarTowers() {
        return numRadarTowers;
    }

    public void setNumRadarTowers(int numRadarTowers) {
        this.numRadarTowers = numRadarTowers;
    }

    public int getNumOutposts() {
        return numOutposts;
    }

    public void setNumOutposts(int numOutposts) {
        this.numOutposts = numOutposts;
    }

    public int getNextPlayerID() {
        return nextPlayerID;
    }

    public void setNextPlayerID(int nextPlayerID) {
        this.nextPlayerID = nextPlayerID;
    }

    public byte getUnknownByte1() {
        return unknownByte1;
    }

    public void setUnknownByte1(byte unknownByte1) {
        this.unknownByte1 = unknownByte1;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getUnknownString3() {
        return unknownString3;
    }

    public void setUnknownString3(String unknownString3) {
        this.unknownString3 = unknownString3;
    }

    public int getVictoryPointLimit() {
        return victoryPointLimit;
    }

    public void setVictoryPointLimit(int victoryPointLimit) {
        this.victoryPointLimit = victoryPointLimit;
    }

    public int getTurnLimit() {
        return turnLimit;
    }

    public void setTurnLimit(int turnLimit) {
        this.turnLimit = turnLimit;
    }

    public int getTimePlayed() {
        return timePlayed;
    }

    public void setTimePlayed(int timePlayed) {
        this.timePlayed = timePlayed;
    }

    public String getUnknownString4() {
        return unknownString4;
    }

    public void setUnknownString4(String unknownString4) {
        this.unknownString4 = unknownString4;
    }

    public int getCityEliminationCount() {
        return cityEliminationCount;
    }

    public void setCityEliminationCount(int cityEliminationCount) {
        this.cityEliminationCount = cityEliminationCount;
    }

    public int getOneCityCultureWin() {
        return oneCityCultureWin;
    }

    public void setOneCityCultureWin(int oneCityCultureWin) {
        this.oneCityCultureWin = oneCityCultureWin;
    }

    public int getAllCitiesCultureWin() {
        return allCitiesCultureWin;
    }

    public void setAllCitiesCultureWin(int allCitiesCultureWin) {
        this.allCitiesCultureWin = allCitiesCultureWin;
    }

    public int getDominationTerrain() {
        return dominationTerrain;
    }

    public void setDominationTerrain(int dominationTerrain) {
        this.dominationTerrain = dominationTerrain;
    }

    public int getDominationPopulation() {
        return dominationPopulation;
    }

    public void setDominationPopulation(int dominationPopulation) {
        this.dominationPopulation = dominationPopulation;
    }

    public int getWonderCost() {
        return wonderCost;
    }

    public void setWonderCost(int wonderCost) {
        this.wonderCost = wonderCost;
    }

    public int getDefeatingOpposingUnitCost() {
        return defeatingOpposingUnitCost;
    }

    public void setDefeatingOpposingUnitCost(int defeatingOpposingUnitCost) {
        this.defeatingOpposingUnitCost = defeatingOpposingUnitCost;
    }

    public int getAdvancementCost() {
        return advancementCost;
    }

    public void setAdvancementCost(int advancementCost) {
        this.advancementCost = advancementCost;
    }

    public int getCityConquestPopulation() {
        return cityConquestPopulation;
    }

    public void setCityConquestPopulation(int cityConquestPopulation) {
        this.cityConquestPopulation = cityConquestPopulation;
    }

    public int getVictoryPointScoring() {
        return victoryPointScoring;
    }

    public void setVictoryPointScoring(int victoryPointScoring) {
        this.victoryPointScoring = victoryPointScoring;
    }

    public int getCapturingSpecialUnit() {
        return capturingSpecialUnit;
    }

    public void setCapturingSpecialUnit(int capturingSpecialUnit) {
        this.capturingSpecialUnit = capturingSpecialUnit;
    }

    public List<Integer> getCitiesPerContinent() {
        return citiesPerContinent;
    }

    public void setCitiesPerContinent(List<Integer> citiesPerContinent) {
        this.citiesPerContinent = citiesPerContinent;
    }

    public List<Integer> getKnownTechs() {
        return knownTechs;
    }

    public void setKnownTechs(List<Integer> knownTechs) {
        this.knownTechs = knownTechs;
    }

    public List<Integer> getGreatWonderCities() {
        return greatWonderCities;
    }

    public void setGreatWonderCities(List<Integer> greatWonderCities) {
        this.greatWonderCities = greatWonderCities;
    }

    public List<Byte> getGreatWonderBuild() {
        return greatWonderBuild;
    }

    public void setGreatWonderBuild(List<Byte> greatWonderBuild) {
        this.greatWonderBuild = greatWonderBuild;
    }

    public List<Integer> getBuildingData1() {
        return buildingData1;
    }

    public void setBuildingData1(List<Integer> buildingData1) {
        this.buildingData1 = buildingData1;
    }

    public List<Integer> getBuildingData2() {
        return buildingData2;
    }

    public void setBuildingData2(List<Integer> buildingData2) {
        this.buildingData2 = buildingData2;
    }

    public List<Integer> getPrototypeStrategy1() {
        return prototypeStrategy1;
    }

    public void setPrototypeStrategy1(List<Integer> prototypeStrategy1) {
        this.prototypeStrategy1 = prototypeStrategy1;
    }

    public List<Integer> getPrototypeStrategy2() {
        return prototypeStrategy2;
    }

    public void setPrototypeStrategy2(List<Integer> prototypeStrategy2) {
        this.prototypeStrategy2 = prototypeStrategy2;
    }

    public List<Integer> getTechData() {
        return techData;
    }

    public void setTechData(List<Integer> techData) {
        this.techData = techData;
    }
    
    // </editor-fold>
}
