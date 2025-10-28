package com.civfanatics.civ3.biqFile;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Quintillus
 */
import java.util.*;
public class GAME extends BIQSection{
    public int dataLength;
    public int useDefaultRules;
    public int defaultVictoryConditions;
    public int numberOfPlayableCivs;
    public ArrayList<Integer>idOfPlayableCivs;
    public int victoryConditionsAndRules;
    public boolean dominationEnabled;
    public boolean spaceRaceEnabled;
    public boolean diplomacticEnabled;
    public boolean conquestEnabled;
    public boolean culturalEnabled;
    public boolean civSpecificAbilitiesEnabled;
    public boolean culturallyLinkedStart;
    public boolean restartPlayersEnabled;
    public boolean preserveRandomSeed;
    public boolean acceleratedProduction;
    public boolean eliminationEnabled;
    public boolean regicideEnabled;
    public boolean massRegicideEnabled;
    public boolean victoryLocationsEnabled;
    public boolean captureTheFlag;
    public boolean allowCulturalConversions;
    public boolean wonderVictoryEnabled;
    public boolean reverseCaptureTheFlag;
    public boolean scientificLeaders;
    public int placeCaptureUnits = 1;
    public int autoPlaceKings = 1;
    public int autoPlaceVictoryLocations = 1;
    public int debugMode;
    public int useTimeLimit;
    public int baseTimeUnit;
    public int startMonth = 1;
    public int startWeek = 1;
    public int startYear = -4000;
    public int minuteTimeLimit;
    public int turnTimeLimit = 540;
    public int[]turnsPerTimescalePart;
    public int[]timeUnitsPerTurn;
    public String scenarioSearchFolders = "";
    private String[]searchFolder = new String[0];
    public int revealMap = 0;  //PTW 11.19 OR GREATER ONLY.  Not part of Conquests
    
    //Begin Conquests Only
    public ArrayList<Integer>civPartOfWhichAlliance;
    public int victoryPointLimit = 50000;
    public int cityEliminationCount = 1;
    public int oneCityCultureWinLimit = 20000;
    public int allCitiesCultureWinLimit = 100000;
    public int dominationTerrainPercent = 66;
    public int dominationPopulationPercent = 66;
    public int wonderVP = 10;
    public int defeatingOpposingUnitVP = 10;
    public int advancementVP = 5;
    public int cityConquestVP = 100;
    public int victoryPointVP = 25;
    public int captureSpecialUnitVP = 1000;
    public int questionMark1;
    byte questionMark2;
    public String alliance0 = "";
    public String alliance1 = "";
    public String alliance2 = "";
    public String alliance3 = "";
    public String alliance4 = "";
    public ArrayList<Integer>warWith0;
    public ArrayList<Integer>warWith1;
    public ArrayList<Integer>warWith2;
    public ArrayList<Integer>warWith3;
    public ArrayList<Integer>warWith4;
    public int allianceVictoryType;
    public String plaugeName = "Black Death";
    byte permitPlagues;
    public int plagueEarliestStart;
    public int plagueVariation;
    public int plagueDuration;
    public int plagueStrength;
    public int plagueGracePeriod = 1;
    public int plagueMaxOccurance = 1;
    public int questionMark3;
    public String unknown = "Unknown";
    public int respawnFlagUnits = 1;
    byte captureAnyFlag;
    public int goldForCapture = 0;
    byte mapVisible;
    byte retainCulture;
    public int questionMark4 = -1;
    public int eruptionPeriod = 5000;
    public int mpBaseTime = 24;
    public int mpCityTime = 3;
    public int mpUnitTime = 1;
    IO baselink;

    public GAME(IO baselink)
    {
        super(baselink);
        this.baselink = baselink;
        idOfPlayableCivs = new ArrayList<Integer>();
        turnsPerTimescalePart = new int[7];
        timeUnitsPerTurn = new int[7];
        civPartOfWhichAlliance = new ArrayList<Integer>();
        warWith0 = new ArrayList<Integer>();
        warWith1 = new ArrayList<Integer>();
        warWith2 = new ArrayList<Integer>();
        warWith3 = new ArrayList<Integer>();
        warWith4 = new ArrayList<Integer>();
     }
    public void trim()
    {
        scenarioSearchFolders = scenarioSearchFolders.trim();
        if (baselink.version == civ3Version.CONQUESTS)
        {
            alliance0 = alliance0.trim();
            alliance1 = alliance1.trim();
            alliance2 = alliance2.trim();
            alliance3 = alliance3.trim();
            alliance4 = alliance4.trim();
            //get rid of extraneous i's with accents aigu - not grave as in ERA
            plaugeName = plaugeName.charAt(0) + plaugeName.substring(1).replace('\u00cd', '\u0000');
            plaugeName = plaugeName.trim();
            unknown = unknown.trim();
        }
    }

    public int getDataLength()
    {
        return dataLength;
    }
    public int getUseDefaultRules()
    {
        return useDefaultRules;
    }

    public int getDefaultVictoryConditions()
    {
        return defaultVictoryConditions;
    }

    public int getNumberOfPlayableCivs()
    {
        return numberOfPlayableCivs;
    }

    public int getVictoryConditionsAndRules()
    {
        return victoryConditionsAndRules;
    }

    public boolean getDominationEnabled()
    {
        return dominationEnabled;
    }

    public boolean getSpaceRaceEnabled()
    {
        return spaceRaceEnabled;
    }

    public boolean getDiplomacticEnabled()
    {
        return diplomacticEnabled;
    }

    public boolean getConquestEnabled()
    {
        return conquestEnabled;
    }

    public boolean getCulturalEnabled()
    {
        return culturalEnabled;
    }

    public boolean getCivSpecificAbilitiesEnabled()
    {
        return civSpecificAbilitiesEnabled;
    }

    public boolean getCulturallyLinkedStart()
    {
        return culturallyLinkedStart;
    }

    public boolean getRestartPlayersEnabled()
    {
        return restartPlayersEnabled;
    }

    public boolean getPreserveRandomSeed()
    {
        return preserveRandomSeed;
    }

    public boolean getAcceleratedProduction()
    {
        return acceleratedProduction;
    }

    public boolean getEliminationEnabled()
    {
        return eliminationEnabled;
    }

    public boolean getRegicideEnabled()
    {
        return regicideEnabled;
    }

    public boolean getMassRegicideEnabled()
    {
        return massRegicideEnabled;
    }

    public boolean getVictoryLocationsEnabled()
    {
        return victoryLocationsEnabled;
    }

    public boolean getCaptureTheFlag()
    {
        return captureTheFlag;
    }

    public boolean getAllowCulturalConversions()
    {
        return allowCulturalConversions;
    }

    public boolean getWonderVictoryEnabled()
    {
        return wonderVictoryEnabled;
    }

    public boolean getReverseCaptureTheFlag()
    {
        return reverseCaptureTheFlag;
    }

    public int getPlaceCaptureUnits()
    {
        return placeCaptureUnits;
    }

    public int getAutoPlaceKings()
    {
        return autoPlaceKings;
    }

    public int getAutoPlaceVictoryLocations()
    {
        return autoPlaceVictoryLocations;
    }
    public int getDebugMode()
    {
        return debugMode;
    }

    public int getUseTimeLimit()
    {
        return useTimeLimit;
    }

    public int getBaseTimeUnit()
    {
        return baseTimeUnit;
    }

    public int getStartMonth()
    {
        return startMonth;
    }

    public int getStartWeek()
    {
        return startWeek;
    }

    public int getStartYear()
    {
        return startYear;
    }

    public int getMinuteTimeLimit()
    {
        return minuteTimeLimit;
    }

    public int getTurnTimeLimit()
    {
        return turnTimeLimit;
    }

    public String getScenarioSearchFolders()
    {
        return scenarioSearchFolders;
    }
    
    public String getSearchFolder(int index) {
        return searchFolder[index];
    }
    
    public int getSearchFolderLength() {
        return searchFolder.length;
    }

    public int getVictoryPointLimit()
    {
        return victoryPointLimit;
    }

    public int getCityEliminationCount()
    {
        return cityEliminationCount;
    }

    public int getOneCityCultureWinLimit()
    {
        return oneCityCultureWinLimit;
    }

    public int getAllCitiesCultureWinLimit()
    {
        return allCitiesCultureWinLimit;
    }

    public int getDominationTerrainPercent()
    {
        return dominationTerrainPercent;
    }

    public int getDominationPopulationPercent()
    {
        return dominationPopulationPercent;
    }

    public int getWonderVP()
    {
        return wonderVP;
    }

    public int getDefeatingOpposingUnitVP()
    {
        return defeatingOpposingUnitVP;
    }

    public int getAdvancementVP()
    {
        return advancementVP;
    }

    public int getCityConquestVP()
    {
        return cityConquestVP;
    }

    public int getVictoryPointVP()
    {
        return victoryPointVP;
    }

    public int getCaptureSpecialUnitVP()
    {
        return captureSpecialUnitVP;
    }

    public int getQuestionMark1()
    {
        return questionMark1;
    }

    public byte getQuestionMark2()
    {
        return questionMark2;
    }

    public String getAlliance0()
    {
        return alliance0;
    }

    public String getAlliance1()
    {
        return alliance1;
    }

    public String getAlliance2()
    {
        return alliance2;
    }

    public String getAlliance3()
    {
        return alliance3;
    }

    public String getAlliance4()
    {
        return alliance4;
    }

    public int getAllianceVictoryType()
    {
        return allianceVictoryType;
    }

    public String getPlaugeName()
    {
        return plaugeName;
    }

    public byte getPermitPlagues()
    {
        return permitPlagues;
    }

    public int getPlagueEarliestStart()
    {
        return plagueEarliestStart;
    }

    public int getPlagueVariation()
    {
        return plagueVariation;
    }

    public int getPlagueDuration()
    {
        return plagueDuration;
    }

    public int getPlagueStrength()
    {
        return plagueStrength;
    }

    public int getPlagueGracePeriod()
    {
        return plagueGracePeriod;
    }

    public int getPlagueMaxOccurance()
    {
        return plagueMaxOccurance;
    }

    public int getQuestionMark3()
    {
        return questionMark3;
    }

    public String getUnknown()
    {
        return unknown;
    }

    public int getRespawnFlagUnits()
    {
        return respawnFlagUnits;
    }

    public byte getCaptureAnyFlag()
    {
        return captureAnyFlag;
    }

    public int getGoldForCapture()
    {
        return goldForCapture;
    }

    public byte getMapVisible()
    {
        return mapVisible;
    }
    
    public int getRevealMap()
    {
        return revealMap;
    }

    public byte getRetainCulture()
    {
        return retainCulture;
    }

    public int getQuestionMark4()
    {
        return questionMark4;
    }

    public int getEruptionPeriod()
    {
        return eruptionPeriod;
    }

    public int getMpBaseTime()
    {
        return mpBaseTime;
    }

    public int getMpCityTime()
    {
        return mpCityTime;
    }

    public int getMpUnitTime()
    {
        return mpUnitTime;
    }






    public void setDataLength(int dataLength)
    {
        this.dataLength = dataLength;
    }

    public void setUseDefaultRules(int useDefaultRules)
    {
        this.useDefaultRules = useDefaultRules;
    }

    public void setDefaultVictoryConditions(int defaultVictoryConditions)
    {
        this.defaultVictoryConditions = defaultVictoryConditions;
    }

    public void setNumberOfPlayableCivs(int numberOfPlayableCivs)
    {
        this.numberOfPlayableCivs = numberOfPlayableCivs;
    }

    public void setVictoryConditionsAndRules(int victoryConditionsAndRules)
    {
        this.victoryConditionsAndRules = victoryConditionsAndRules;
    }

    public void setPlaceCaptureUnits(int placeCaptureUnits)
    {
        this.placeCaptureUnits = placeCaptureUnits;
    }

    public void setAutoPlaceKings(int autoPlaceKings)
    {
        this.autoPlaceKings = autoPlaceKings;
    }

    public void setAutoPlaceVictoryLocations(int autoPlaceVictoryLocations)
    {
        this.autoPlaceVictoryLocations = autoPlaceVictoryLocations;
    }

    public void setDebugMode(int debugMode)
    {
        this.debugMode = debugMode;
    }

    public void setUseTimeLimit(int useTimeLimit)
    {
        this.useTimeLimit = useTimeLimit;
    }

    public void setBaseTimeUnit(int baseTimeUnit)
    {
        this.baseTimeUnit = baseTimeUnit;
    }

    public void setStartMonth(int startMonth)
    {
        this.startMonth = startMonth;
    }

    public void setStartWeek(int startWeek)
    {
        this.startWeek = startWeek;
    }

    public void setStartYear(int startYear)
    {
        this.startYear = startYear;
    }

    public void setMinuteTimeLimit(int minuteTimeLimit)
    {
        this.minuteTimeLimit = minuteTimeLimit;
    }

    public void setTurnTimeLimit(int turnTimeLimit)
    {
        this.turnTimeLimit = turnTimeLimit;
    }

    public void setScenarioSearchFolders(String scenarioSearchFolders)
    {
        this.scenarioSearchFolders = scenarioSearchFolders;
        //Tokenize them
        StringTokenizer tokenizer = new StringTokenizer(scenarioSearchFolders, ";");
        searchFolder = new String[tokenizer.countTokens()];
        int count = 0;
        while (tokenizer.hasMoreTokens())
        {
            searchFolder[count] = tokenizer.nextToken();
            count++;
        }
    }

    public void setVictoryPointLimit(int victoryPointLimit)
    {
        this.victoryPointLimit = victoryPointLimit;
    }

    public void setCityEliminationCount(int cityEliminationCount)
    {
        this.cityEliminationCount = cityEliminationCount;
    }

    public void setOneCityCultureWinLimit(int oneCityCultureWinLimit)
    {
        this.oneCityCultureWinLimit = oneCityCultureWinLimit;
    }

    public void setAllCitiesCultureWinLimit(int allCitiesCultureWinLimit)
    {
        this.allCitiesCultureWinLimit = allCitiesCultureWinLimit;
    }

    public void setDominationTerrainPercent(int dominationTerrainPercent)
    {
        this.dominationTerrainPercent = dominationTerrainPercent;
    }

    public void setDominationPopulationPercent(int dominationPopulationPercent)
    {
        this.dominationPopulationPercent = dominationPopulationPercent;
    }

    public void setWonderVP(int wonderVP)
    {
        this.wonderVP = wonderVP;
    }

    public void setDefeatingOpposingUnitVP(int defeatingOpposingUnitVP)
    {
        this.defeatingOpposingUnitVP = defeatingOpposingUnitVP;
    }

    public void setAdvancementVP(int advancementVP)
    {
        this.advancementVP = advancementVP;
    }

    public void setCityConquestVP(int cityConquestVP)
    {
        this.cityConquestVP = cityConquestVP;
    }

    public void setVictoryPointVP(int victoryPointVP)
    {
        this.victoryPointVP = victoryPointVP;
    }

    public void setCaptureSpecialUnitVP(int captureSpecialUnitVP)
    {
        this.captureSpecialUnitVP = captureSpecialUnitVP;
    }

    public void setQuestionMark1(int questionMark1)
    {
        this.questionMark1 = questionMark1;
    }

    public void setQuestionMark2(byte questionMark2)
    {
        this.questionMark2 = questionMark2;
    }

    public void setRevealMap(int revealMap)
    {
        this.revealMap = revealMap;
    }

    public void setAlliance0(String alliance0)
    {
        this.alliance0 = alliance0;
    }

    public void setAlliance1(String alliance1)
    {
        this.alliance1 = alliance1;
    }

    public void setAlliance2(String alliance2)
    {
        this.alliance2 = alliance2;
    }

    public void setAlliance3(String alliance3)
    {
        this.alliance3 = alliance3;
    }

    public void setAlliance4(String alliance4)
    {
        this.alliance4 = alliance4;
    }

    public void setAllianceVictoryType(int allianceVictoryType)
    {
        this.allianceVictoryType = allianceVictoryType;
    }

    public void setPlaugeName(String plaugeName)
    {
        this.plaugeName = plaugeName;
    }

    public void setPermitPlagues(byte permitPlagues)
    {
        this.permitPlagues = permitPlagues;
    }

    public void setPlagueEarliestStart(int plagueEarliestStart)
    {
        this.plagueEarliestStart = plagueEarliestStart;
    }

    public void setPlagueVariation(int plagueVariation)
    {
        this.plagueVariation = plagueVariation;
    }

    public void setPlagueDuration(int plagueDuration)
    {
        this.plagueDuration = plagueDuration;
    }

    public void setPlagueStrength(int plagueStrength)
    {
        this.plagueStrength = plagueStrength;
    }

    public void setPlagueGracePeriod(int plagueGracePeriod)
    {
        this.plagueGracePeriod = plagueGracePeriod;
    }

    public void setPlagueMaxOccurance(int plagueMaxOccurance)
    {
        this.plagueMaxOccurance = plagueMaxOccurance;
    }

    public void setQuestionMark3(int questionMark3)
    {
        this.questionMark3 = questionMark3;
    }

    public void setUnknown(String unknown)
    {
        this.unknown = unknown;
    }

    public void setRespawnFlagUnits(int respawnFlagUnits)
    {
        this.respawnFlagUnits = respawnFlagUnits;
    }

    public void setCaptureAnyFlag(byte captureAnyFlag)
    {
        this.captureAnyFlag = captureAnyFlag;
    }

    public void setGoldForCapture(int goldForCapture)
    {
        this.goldForCapture = goldForCapture;
    }

    public void setMapVisible(byte mapVisible)
    {
        this.mapVisible = mapVisible;
    }

    public void setRetainCulture(byte retainCulture)
    {
        this.retainCulture = retainCulture;
    }

    public void setQuestionMark4(int questionMark4)
    {
        this.questionMark4 = questionMark4;
    }

    public void setEruptionPeriod(int eruptionPeriod)
    {
        this.eruptionPeriod = eruptionPeriod;
    }

    public void setMpBaseTime(int mpBaseTime)
    {
        this.mpBaseTime = mpBaseTime;
    }

    public void setMpCityTime(int mpCityTime)
    {
        this.mpCityTime = mpCityTime;
    }

    public void setMpUnitTime(int mpUnitTime)
    {
        this.mpUnitTime = mpUnitTime;
    }

    public void setTurnsPerTimescalePart(int part, int turns)
    {
        this.turnsPerTimescalePart[part] = turns;
    }

    public void setTimeUnitsPerTurn(int part, int timeUnits)
    {
        this.timeUnitsPerTurn[part] = timeUnits;
    }
    public void setWarWithAlliance(int alliance1, int alliance2, int war)
    {
        //System.out.println(alliance1 + "  " + alliance2 + "  " + war);
        //this.atWarWith[alliance1].warWith[alliance2] = war;
    }
    
    public void convertFromVanillaToPTW() {
        dataLength+=44; //11 * 4
        dataLength+=56; //turn limits, 7 sets of 4 + 4 bytes each
        dataLength+=5200;   //scenario search folders
    }
    
    public void convertFromPTWToConquests()
    {
        dataLength+=2017;  //gained stuff
        dataLength+=getNumberOfPlayableCivs() * 4;  //variable amt of gained stuff
        //minor version 19 already dealt with and inputted
        if (numberOfPlayableCivs > 0)
        {
            for (int c = 0; c < numberOfPlayableCivs; c++)
            {
                civPartOfWhichAlliance.add(0);
            }
        }
        else  //all civs (except barbs) playable
        {
            for (int c = 0; c < baselink.civilization.size() - 1; c++)
            {   //minus one b/c we don't include barbarians
                civPartOfWhichAlliance.add(0);
            }
        }
        //war with info                    
        for (int j = 0; j < 5; j++)
        {
            warWith0.add(0);
            warWith1.add(0);
            warWith2.add(0);
            warWith3.add(0);
            warWith4.add(0);
        }
    }

    public void extractEnglish()
    {
        int victoryConditionCopy = victoryConditionsAndRules;
        int divBy = victoryConditionCopy/(int)(Math.pow(2, 18));
        if (divBy == 1)
        {
            scientificLeaders = true;
        }
        divBy = victoryConditionCopy/(int)(Math.pow(2, 17));
        if (divBy == 1)
        {
            reverseCaptureTheFlag = true;
            victoryConditionCopy-=131072;
        }
        divBy = victoryConditionCopy/(int)(Math.pow(2, 16));
        if (divBy == 1)
        {
            wonderVictoryEnabled = true;
            victoryConditionCopy-=65536;
        }
        divBy = victoryConditionCopy/(int)(Math.pow(2, 15));
        if (divBy == 1)
        {
            allowCulturalConversions = true;
            victoryConditionCopy-=32768;
        }
        divBy = victoryConditionCopy/(int)(Math.pow(2, 14));
        if (divBy == 1)
        {
            captureTheFlag = true;
            victoryConditionCopy-=16384;
        }
        divBy = victoryConditionCopy/(int)(Math.pow(2, 13));
        if (divBy == 1)
        {
            victoryLocationsEnabled = true;
            victoryConditionCopy-=8192;
        }
        divBy = victoryConditionCopy/(int)(Math.pow(2, 12));
        if (divBy == 1)
        {
            massRegicideEnabled = true;
            victoryConditionCopy-=4096;
        }
        divBy = victoryConditionCopy/(int)(Math.pow(2, 11));
        if (divBy == 1)
        {
            regicideEnabled = true;
            victoryConditionCopy-=2048;
        }
        divBy = victoryConditionCopy/(int)(Math.pow(2, 10));
        if (divBy == 1)
        {
            eliminationEnabled = true;
            victoryConditionCopy-=1024;
        }
        divBy = victoryConditionCopy/(int)(Math.pow(2, 9));
        if (divBy == 1)
        {
            acceleratedProduction = true;
            victoryConditionCopy-=512;
        }
        divBy = victoryConditionCopy/(int)(Math.pow(2, 8));
        if (divBy == 1)
        {
            preserveRandomSeed = true;
            victoryConditionCopy-=256;
        }
        divBy = victoryConditionCopy/(int)(Math.pow(2, 7));
        if (divBy == 1)
        {
            restartPlayersEnabled = true;
            victoryConditionCopy-=128;
        }
        divBy = victoryConditionCopy/(int)(Math.pow(2, 6));
        if (divBy == 1)
        {
            culturallyLinkedStart = true;
            victoryConditionCopy-=64;
        }
        divBy = victoryConditionCopy/(int)(Math.pow(2, 5));
        if (divBy == 1)
        {
            civSpecificAbilitiesEnabled = true;
            victoryConditionCopy-=32;
        }
        divBy = victoryConditionCopy/(int)(Math.pow(2, 4));
        if (divBy == 1)
        {
            culturalEnabled = true;
            victoryConditionCopy-=16;
        }
        divBy = victoryConditionCopy/(int)(Math.pow(2, 3));
        if (divBy == 1)
        {
            conquestEnabled = true;
            victoryConditionCopy-=8;
        }
        divBy = victoryConditionCopy/(int)(Math.pow(2, 2));
        if (divBy == 1)
        {
            diplomacticEnabled = true;
            victoryConditionCopy-=4;
        }
        divBy = victoryConditionCopy/(int)(Math.pow(2, 1));
        if (divBy == 1)
        {
            spaceRaceEnabled = true;
            victoryConditionCopy-=2;
        }
        divBy = victoryConditionCopy/(int)(Math.pow(2, 0));
        if (divBy == 1)
        {
            dominationEnabled = true;
            victoryConditionCopy-=1;
        }
        //scenario search folders
        StringTokenizer tokenizer = new StringTokenizer(scenarioSearchFolders, ";");
        searchFolder = new String[tokenizer.countTokens()];
        int count = 0;
        while (tokenizer.hasMoreTokens())
        {
            searchFolder[count] = tokenizer.nextToken();
            count++;
        }
    }

    @Override
    public String toString()
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn =  "dataLength: " + dataLength + lineReturn;
        toReturn = toReturn + "useDefaultRules: " + useDefaultRules + lineReturn;
        toReturn = toReturn + "defaultVictoryConditions: " + defaultVictoryConditions + lineReturn;
        toReturn = toReturn + "numberOfPlayableCivs: " + numberOfPlayableCivs + lineReturn;
        for (int j = 0; j < numberOfPlayableCivs; j++)
        {
            toReturn = toReturn + "playable civ: " + idOfPlayableCivs.get(j) + lineReturn;
        }
        toReturn = toReturn + "victoryConditionsAndRules: " + victoryConditionsAndRules + lineReturn;
        toReturn = toReturn + "placeCaptureUnits: " + placeCaptureUnits + lineReturn;
        toReturn = toReturn + "autoPlaceKings: " + autoPlaceKings + lineReturn;
        toReturn = toReturn + "autoPlaceVictoryLocations: " + autoPlaceVictoryLocations + lineReturn;
        toReturn = toReturn + "debugMode: " + debugMode + lineReturn;
        toReturn = toReturn + "useTimeLimit: " + useTimeLimit + lineReturn;
        toReturn = toReturn + "baseTimeUnit: " + baseTimeUnit + lineReturn;
        toReturn = toReturn + "startMonth: " + startMonth + lineReturn;
        toReturn = toReturn + "startWeek: " + startWeek + lineReturn;
        toReturn = toReturn + "startYear: " + startYear + lineReturn;
        toReturn = toReturn + "minuteTimeLimit: " + minuteTimeLimit + lineReturn;
        toReturn = toReturn + "turnTimeLimit: " + turnTimeLimit + lineReturn;
        for (int j = 0; j < 7; j++)
        {
            toReturn = toReturn + "turns in time section " + j + ": " + turnsPerTimescalePart[j] + lineReturn;
            toReturn = toReturn + "time per turn in time section " + j + ": " + timeUnitsPerTurn[j] + lineReturn;
        }
        toReturn = toReturn + "scenarioSearchFolders: " + scenarioSearchFolders + lineReturn;
        for (int j = 0; j < numberOfPlayableCivs; j++)
        {
            toReturn = toReturn + "civ " + j + " is in alliance " + civPartOfWhichAlliance.get(j) + lineReturn;
        }
        toReturn = toReturn + "victoryPointLimit: " + victoryPointLimit + lineReturn;
        toReturn = toReturn + "cityEliminationCount: " + cityEliminationCount + lineReturn;
        toReturn = toReturn + "oneCityCultureWinLimit: " + oneCityCultureWinLimit + lineReturn;
        toReturn = toReturn + "allCitiesCultureWinLimit: " + allCitiesCultureWinLimit + lineReturn;
        toReturn = toReturn + "dominationTerrainPercent: " + dominationTerrainPercent + lineReturn;
        toReturn = toReturn + "dominationPopulationPercent: " + dominationPopulationPercent + lineReturn;
        toReturn = toReturn + "wonderVP: " + wonderVP + lineReturn;
        toReturn = toReturn + "defeatingOpposingUnitVP: " + defeatingOpposingUnitVP + lineReturn;
        toReturn = toReturn + "advancementVP: " + advancementVP + lineReturn;
        toReturn = toReturn + "cityConquestVP: " + cityConquestVP + lineReturn;
        toReturn = toReturn + "victoryPointVP: " + victoryPointVP + lineReturn;
        toReturn = toReturn + "captureSpecialUnitVP: " + captureSpecialUnitVP + lineReturn;
        toReturn = toReturn + "questionMark1: " + questionMark1 + lineReturn;
        toReturn = toReturn + "questionMark2: " + questionMark2 + lineReturn;
        toReturn = toReturn + "alliance0: " + alliance0 + lineReturn;
        toReturn = toReturn + "alliance1: " + alliance1 + lineReturn;
        toReturn = toReturn + "alliance2: " + alliance2 + lineReturn;
        toReturn = toReturn + "alliance3: " + alliance3 + lineReturn;
        toReturn = toReturn + "alliance4: " + alliance4 + lineReturn;
        for (int j = 0; j < 5; j++)
        {
            toReturn = toReturn + "alliance" + j + " is at war with alliance0?" + warWith0.get(j) + lineReturn;
            toReturn = toReturn + "alliance" + j + " is at war with alliance1?" + warWith1.get(j) + lineReturn;
            toReturn = toReturn + "alliance" + j + " is at war with alliance2?" + warWith2.get(j) + lineReturn;
            toReturn = toReturn + "alliance" + j + " is at war with alliance3?" + warWith3.get(j) + lineReturn;
            toReturn = toReturn + "alliance" + j + " is at war with alliance4?" + warWith4.get(j) + lineReturn;
        }
        toReturn = toReturn + "allianceVictoryType: " + allianceVictoryType + lineReturn;
        toReturn = toReturn + "plaugeName: " + plaugeName + lineReturn;
        toReturn = toReturn + "permitPlagues: " + permitPlagues + lineReturn;
        toReturn = toReturn + "plagueEarliestStart: " + plagueEarliestStart + lineReturn;
        toReturn = toReturn + "plagueVariation: " + plagueVariation + lineReturn;
        toReturn = toReturn + "plagueDuration: " + plagueDuration + lineReturn;
        toReturn = toReturn + "plagueStrength: " + plagueStrength + lineReturn;
        toReturn = toReturn + "plagueGracePeriod: " + plagueGracePeriod + lineReturn;
        toReturn = toReturn + "plagueMaxOccurance: " + plagueMaxOccurance + lineReturn;
        toReturn = toReturn + "questionMark3: " + questionMark3 + lineReturn;
        toReturn = toReturn + "unknown: " + unknown + lineReturn;
        toReturn = toReturn + "respawnFlagUnits: " + respawnFlagUnits + lineReturn;
        toReturn = toReturn + "captureAnyFlag: " + captureAnyFlag + lineReturn;
        toReturn = toReturn + "goldForCapture: " + goldForCapture + lineReturn;
        toReturn = toReturn + "mapVisible: " + mapVisible + lineReturn;
        toReturn = toReturn + "retainCulture: " + retainCulture + lineReturn;
        toReturn = toReturn + "questionMark4: " + questionMark4 + lineReturn;
        toReturn = toReturn + "eruptionPeriod: " + eruptionPeriod + lineReturn;
        toReturn = toReturn + "mpBaseTime: " + mpBaseTime + lineReturn;
        toReturn = toReturn + "mpCityTime: " + mpCityTime + lineReturn;
        toReturn = toReturn + "mpUnitTime: " + mpUnitTime + lineReturn;
        toReturn = toReturn + lineReturn;
        return toReturn;
    }

    public String toEnglish()
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn =  "dataLength: " + dataLength + lineReturn;
        toReturn = toReturn + "useDefaultRules: " + useDefaultRules + lineReturn;
        toReturn = toReturn + "defaultVictoryConditions: " + defaultVictoryConditions + lineReturn;
        toReturn = toReturn + "numberOfPlayableCivs: " + numberOfPlayableCivs + lineReturn;
        for (int j = 0; j < numberOfPlayableCivs; j++)
        {
            toReturn = toReturn + "playable civ: " + idOfPlayableCivs.get(j) + lineReturn;
        }
        toReturn = toReturn + "victoryConditionsAndRules: " + victoryConditionsAndRules + lineReturn;
            toReturn = toReturn + "  dominationEnabled: " + dominationEnabled + lineReturn;
            toReturn = toReturn + "  spaceRaceEnabled: " + spaceRaceEnabled + lineReturn;
            toReturn = toReturn + "  diplomacticEnabled: " + diplomacticEnabled + lineReturn;
            toReturn = toReturn + "  conquestEnabled: " + conquestEnabled + lineReturn;
            toReturn = toReturn + "  culturalEnabled: " + culturalEnabled + lineReturn;
            toReturn = toReturn + "  civSpecificAbilitiesEnabled: " + civSpecificAbilitiesEnabled + lineReturn;
            toReturn = toReturn + "  culturallyLinkedStart: " + culturallyLinkedStart + lineReturn;
            toReturn = toReturn + "  restartPlayersEnabled: " + restartPlayersEnabled + lineReturn;
            toReturn = toReturn + "  preserveRandomSeed: " + preserveRandomSeed + lineReturn;
            toReturn = toReturn + "  acceleratedProduction: " + acceleratedProduction + lineReturn;
            toReturn = toReturn + "  eliminationEnabled: " + eliminationEnabled + lineReturn;
            toReturn = toReturn + "  regicideEnabled: " + regicideEnabled + lineReturn;
            toReturn = toReturn + "  massRegicideEnabled: " + massRegicideEnabled + lineReturn;
            toReturn = toReturn + "  victoryLocationsEnabled: " + victoryLocationsEnabled + lineReturn;
            toReturn = toReturn + "  captureTheFlag: " + captureTheFlag + lineReturn;
            toReturn = toReturn + "  allowCulturalConversions: " + allowCulturalConversions + lineReturn;
            toReturn = toReturn + "  wonderVictoryEnabled: " + wonderVictoryEnabled + lineReturn;
            toReturn = toReturn + "  reverseCaptureTheFlag: " + reverseCaptureTheFlag + lineReturn;
        toReturn = toReturn + "placeCaptureUnits: " + placeCaptureUnits + lineReturn;
        toReturn = toReturn + "autoPlaceKings: " + autoPlaceKings + lineReturn;
        toReturn = toReturn + "autoPlaceVictoryLocations: " + autoPlaceVictoryLocations + lineReturn;
        toReturn = toReturn + "debugMode: " + debugMode + lineReturn;
        toReturn = toReturn + "useTimeLimit: " + useTimeLimit + lineReturn;
        toReturn = toReturn + "baseTimeUnit: " + baseTimeUnit + lineReturn;
        toReturn = toReturn + "startMonth: " + startMonth + lineReturn;
        toReturn = toReturn + "startWeek: " + startWeek + lineReturn;
        toReturn = toReturn + "startYear: " + startYear + lineReturn;
        toReturn = toReturn + "minuteTimeLimit: " + minuteTimeLimit + lineReturn;
        toReturn = toReturn + "turnTimeLimit: " + turnTimeLimit + lineReturn;
        for (int j = 0; j < 7; j++)
        {
            toReturn = toReturn + "turns in time section " + j + ": " + turnsPerTimescalePart[j] + lineReturn;
            toReturn = toReturn + "time per turn in time section " + j + ": " + timeUnitsPerTurn[j] + lineReturn;
        }
        toReturn = toReturn + "scenarioSearchFolders: " + scenarioSearchFolders + lineReturn;
        for (int j = 0; j < numberOfPlayableCivs; j++)
        {
            toReturn = toReturn + "civ " + j + " is in alliance " + civPartOfWhichAlliance.get(j) + lineReturn;
        }
        toReturn = toReturn + "victoryPointLimit: " + victoryPointLimit + lineReturn;
        toReturn = toReturn + "cityEliminationCount: " + cityEliminationCount + lineReturn;
        toReturn = toReturn + "oneCityCultureWinLimit: " + oneCityCultureWinLimit + lineReturn;
        toReturn = toReturn + "allCitiesCultureWinLimit: " + allCitiesCultureWinLimit + lineReturn;
        toReturn = toReturn + "dominationTerrainPercent: " + dominationTerrainPercent + lineReturn;
        toReturn = toReturn + "dominationPopulationPercent: " + dominationPopulationPercent + lineReturn;
        toReturn = toReturn + "wonderVP: " + wonderVP + lineReturn;
        toReturn = toReturn + "defeatingOpposingUnitVP: " + defeatingOpposingUnitVP + lineReturn;
        toReturn = toReturn + "advancementVP: " + advancementVP + lineReturn;
        toReturn = toReturn + "cityConquestVP: " + cityConquestVP + lineReturn;
        toReturn = toReturn + "victoryPointVP: " + victoryPointVP + lineReturn;
        toReturn = toReturn + "captureSpecialUnitVP: " + captureSpecialUnitVP + lineReturn;
        toReturn = toReturn + "questionMark1: " + questionMark1 + lineReturn;
        toReturn = toReturn + "questionMark2: " + questionMark2 + lineReturn;
        toReturn = toReturn + "alliance0: " + alliance0 + lineReturn;
        toReturn = toReturn + "alliance1: " + alliance1 + lineReturn;
        toReturn = toReturn + "alliance2: " + alliance2 + lineReturn;
        toReturn = toReturn + "alliance3: " + alliance3 + lineReturn;
        toReturn = toReturn + "alliance4: " + alliance4 + lineReturn;
        for (int j = 0; j < 5; j++)
        {
            toReturn = toReturn + "alliance" + j + " is at war with alliance0?" + warWith0.get(j) + lineReturn;
            toReturn = toReturn + "alliance" + j + " is at war with alliance1?" + warWith1.get(j) + lineReturn;
            toReturn = toReturn + "alliance" + j + " is at war with alliance2?" + warWith2.get(j) + lineReturn;
            toReturn = toReturn + "alliance" + j + " is at war with alliance3?" + warWith3.get(j) + lineReturn;
            toReturn = toReturn + "alliance" + j + " is at war with alliance4?" + warWith4.get(j) + lineReturn;
        }
        toReturn = toReturn + "allianceVictoryType: " + allianceVictoryType + lineReturn;
        toReturn = toReturn + "plaugeName: " + plaugeName + lineReturn;
        toReturn = toReturn + "permitPlagues: " + permitPlagues + lineReturn;
        toReturn = toReturn + "plagueEarliestStart: " + plagueEarliestStart + lineReturn;
        toReturn = toReturn + "plagueVariation: " + plagueVariation + lineReturn;
        toReturn = toReturn + "plagueDuration: " + plagueDuration + lineReturn;
        toReturn = toReturn + "plagueStrength: " + plagueStrength + lineReturn;
        toReturn = toReturn + "plagueGracePeriod: " + plagueGracePeriod + lineReturn;
        toReturn = toReturn + "plagueMaxOccurance: " + plagueMaxOccurance + lineReturn;
        toReturn = toReturn + "questionMark3: " + questionMark3 + lineReturn;
        toReturn = toReturn + "unknown: " + unknown + lineReturn;
        toReturn = toReturn + "respawnFlagUnits: " + respawnFlagUnits + lineReturn;
        toReturn = toReturn + "captureAnyFlag: " + captureAnyFlag + lineReturn;
        toReturn = toReturn + "goldForCapture: " + goldForCapture + lineReturn;
        toReturn = toReturn + "mapVisible: " + mapVisible + lineReturn;
        toReturn = toReturn + "retainCulture: " + retainCulture + lineReturn;
        toReturn = toReturn + "questionMark4: " + questionMark4 + lineReturn;
        toReturn = toReturn + "eruptionPeriod: " + eruptionPeriod + lineReturn;
        toReturn = toReturn + "mpBaseTime: " + mpBaseTime + lineReturn;
        toReturn = toReturn + "mpCityTime: " + mpCityTime + lineReturn;
        toReturn = toReturn + "mpUnitTime: " + mpUnitTime + lineReturn;
        toReturn = toReturn + lineReturn;
        return toReturn;
    }

    public String compareTo(BIQSection section, String separator)
    {
        if (!(section instanceof GAME))
            return null;
        GAME two = (GAME)section;
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "";
        if (!(dataLength == two.getDataLength()))
        {
                toReturn = toReturn + "DataLength: " + dataLength + separator + two.getDataLength() + lineReturn;
        }
        if (!(useDefaultRules == two.getUseDefaultRules()))
        {
                toReturn = toReturn + "UseDefaultRules: " + useDefaultRules + separator + two.getUseDefaultRules() + lineReturn;
        }
        if (!(defaultVictoryConditions == two.getDefaultVictoryConditions()))
        {
                toReturn = toReturn + "DefaultVictoryConditions: " + defaultVictoryConditions + separator + two.getDefaultVictoryConditions() + lineReturn;
        }
        if (!(numberOfPlayableCivs == two.getNumberOfPlayableCivs()))
        {
                toReturn = toReturn + "NumberOfPlayableCivs: " + numberOfPlayableCivs + separator + two.getNumberOfPlayableCivs() + lineReturn;
        }
        if (!(victoryConditionsAndRules == two.getVictoryConditionsAndRules()))
        {
                toReturn = toReturn + "VictoryConditionsAndRules: " + victoryConditionsAndRules + separator + two.getVictoryConditionsAndRules() + lineReturn;
        }
        if (!(dominationEnabled == two.getDominationEnabled()))
        {
                toReturn = toReturn + "DominationEnabled: " + dominationEnabled + separator + two.getDominationEnabled() + lineReturn;
        }
        if (!(spaceRaceEnabled == two.getSpaceRaceEnabled()))
        {
                toReturn = toReturn + "SpaceRaceEnabled: " + spaceRaceEnabled + separator + two.getSpaceRaceEnabled() + lineReturn;
        }
        if (!(diplomacticEnabled == two.getDiplomacticEnabled()))
        {
                toReturn = toReturn + "DiplomacticEnabled: " + diplomacticEnabled + separator + two.getDiplomacticEnabled() + lineReturn;
        }
        if (!(conquestEnabled == two.getConquestEnabled()))
        {
                toReturn = toReturn + "ConquestEnabled: " + conquestEnabled + separator + two.getConquestEnabled() + lineReturn;
        }
        if (!(culturalEnabled == two.getCulturalEnabled()))
        {
                toReturn = toReturn + "CulturalEnabled: " + culturalEnabled + separator + two.getCulturalEnabled() + lineReturn;
        }
        if (!(civSpecificAbilitiesEnabled == two.getCivSpecificAbilitiesEnabled()))
        {
                toReturn = toReturn + "CivSpecificAbilitiesEnabled: " + civSpecificAbilitiesEnabled + separator + two.getCivSpecificAbilitiesEnabled() + lineReturn;
        }
        if (!(culturallyLinkedStart == two.getCulturallyLinkedStart()))
        {
                toReturn = toReturn + "CulturallyLinkedStart: " + culturallyLinkedStart + separator + two.getCulturallyLinkedStart() + lineReturn;
        }
        if (!(restartPlayersEnabled == two.getRestartPlayersEnabled()))
        {
                toReturn = toReturn + "RestartPlayersEnabled: " + restartPlayersEnabled + separator + two.getRestartPlayersEnabled() + lineReturn;
        }
        if (!(preserveRandomSeed == two.getPreserveRandomSeed()))
        {
                toReturn = toReturn + "PreserveRandomSeed: " + preserveRandomSeed + separator + two.getPreserveRandomSeed() + lineReturn;
        }
        if (!(acceleratedProduction == two.getAcceleratedProduction()))
        {
                toReturn = toReturn + "AcceleratedProduction: " + acceleratedProduction + separator + two.getAcceleratedProduction() + lineReturn;
        }
        if (!(eliminationEnabled == two.getEliminationEnabled()))
        {
                toReturn = toReturn + "EliminationEnabled: " + eliminationEnabled + separator + two.getEliminationEnabled() + lineReturn;
        }
        if (!(regicideEnabled == two.getRegicideEnabled()))
        {
                toReturn = toReturn + "RegicideEnabled: " + regicideEnabled + separator + two.getRegicideEnabled() + lineReturn;
        }
        if (!(massRegicideEnabled == two.getMassRegicideEnabled()))
        {
                toReturn = toReturn + "MassRegicideEnabled: " + massRegicideEnabled + separator + two.getMassRegicideEnabled() + lineReturn;
        }
        if (!(victoryLocationsEnabled == two.getVictoryLocationsEnabled()))
        {
                toReturn = toReturn + "VictoryLocationsEnabled: " + victoryLocationsEnabled + separator + two.getVictoryLocationsEnabled() + lineReturn;
        }
        if (!(captureTheFlag == two.getCaptureTheFlag()))
        {
                toReturn = toReturn + "CaptureTheFlag: " + captureTheFlag + separator + two.getCaptureTheFlag() + lineReturn;
        }
        if (!(allowCulturalConversions == two.getAllowCulturalConversions()))
        {
                toReturn = toReturn + "AllowCulturalConversions: " + allowCulturalConversions + separator + two.getAllowCulturalConversions() + lineReturn;
        }
        if (!(wonderVictoryEnabled == two.getWonderVictoryEnabled()))
        {
                toReturn = toReturn + "WonderVictoryEnabled: " + wonderVictoryEnabled + separator + two.getWonderVictoryEnabled() + lineReturn;
        }
        if (!(reverseCaptureTheFlag == two.getReverseCaptureTheFlag()))
        {
                toReturn = toReturn + "ReverseCaptureTheFlag: " + reverseCaptureTheFlag + separator + two.getReverseCaptureTheFlag() + lineReturn;
        }
        if (!(placeCaptureUnits == two.getPlaceCaptureUnits()))
        {
                toReturn = toReturn + "PlaceCaptureUnits: " + placeCaptureUnits + separator + two.getPlaceCaptureUnits() + lineReturn;
        }
        if (!(autoPlaceKings == two.getAutoPlaceKings()))
        {
                toReturn = toReturn + "AutoPlaceKings: " + autoPlaceKings + separator + two.getAutoPlaceKings() + lineReturn;
        }
        if (!(autoPlaceVictoryLocations == two.getAutoPlaceVictoryLocations()))
        {
                toReturn = toReturn + "AutoPlaceVictoryLocations: " + autoPlaceVictoryLocations + separator + two.getAutoPlaceVictoryLocations() + lineReturn;
        }
        if (!(debugMode == two.getDebugMode()))
        {
                toReturn = toReturn + "DebugMode: " + debugMode + separator + two.getDebugMode() + lineReturn;
        }
        if (!(useTimeLimit == two.getUseTimeLimit()))
        {
                toReturn = toReturn + "UseTimeLimit: " + useTimeLimit + separator + two.getUseTimeLimit() + lineReturn;
        }
        if (!(baseTimeUnit == two.getBaseTimeUnit()))
        {
                toReturn = toReturn + "BaseTimeUnit: " + baseTimeUnit + separator + two.getBaseTimeUnit() + lineReturn;
        }
        if (!(startMonth == two.getStartMonth()))
        {
                toReturn = toReturn + "StartMonth: " + startMonth + separator + two.getStartMonth() + lineReturn;
        }
        if (!(startWeek == two.getStartWeek()))
        {
                toReturn = toReturn + "StartWeek: " + startWeek + separator + two.getStartWeek() + lineReturn;
        }
        if (!(startYear == two.getStartYear()))
        {
                toReturn = toReturn + "StartYear: " + startYear + separator + two.getStartYear() + lineReturn;
        }
        if (!(minuteTimeLimit == two.getMinuteTimeLimit()))
        {
                toReturn = toReturn + "MinuteTimeLimit: " + minuteTimeLimit + separator + two.getMinuteTimeLimit() + lineReturn;
        }
        if (!(turnTimeLimit == two.getTurnTimeLimit()))
        {
                toReturn = toReturn + "TurnTimeLimit: " + turnTimeLimit + separator + two.getTurnTimeLimit() + lineReturn;
        }
        if (scenarioSearchFolders.compareTo(two.getScenarioSearchFolders()) != 0)
        {
                toReturn = toReturn + "ScenarioSearchFolders: " + scenarioSearchFolders + separator + two.getScenarioSearchFolders() + lineReturn;
        }
        if (!(victoryPointLimit == two.getVictoryPointLimit()))
        {
                toReturn = toReturn + "VictoryPointLimit: " + victoryPointLimit + separator + two.getVictoryPointLimit() + lineReturn;
        }
        if (!(cityEliminationCount == two.getCityEliminationCount()))
        {
                toReturn = toReturn + "CityEliminationCount: " + cityEliminationCount + separator + two.getCityEliminationCount() + lineReturn;
        }
        if (!(oneCityCultureWinLimit == two.getOneCityCultureWinLimit()))
        {
                toReturn = toReturn + "OneCityCultureWinLimit: " + oneCityCultureWinLimit + separator + two.getOneCityCultureWinLimit() + lineReturn;
        }
        if (!(allCitiesCultureWinLimit == two.getAllCitiesCultureWinLimit()))
        {
                toReturn = toReturn + "AllCitiesCultureWinLimit: " + allCitiesCultureWinLimit + separator + two.getAllCitiesCultureWinLimit() + lineReturn;
        }
        if (!(dominationTerrainPercent == two.getDominationTerrainPercent()))
        {
                toReturn = toReturn + "DominationTerrainPercent: " + dominationTerrainPercent + separator + two.getDominationTerrainPercent() + lineReturn;
        }
        if (!(dominationPopulationPercent == two.getDominationPopulationPercent()))
        {
                toReturn = toReturn + "DominationPopulationPercent: " + dominationPopulationPercent + separator + two.getDominationPopulationPercent() + lineReturn;
        }
        if (!(wonderVP == two.getWonderVP()))
        {
                toReturn = toReturn + "WonderVP: " + wonderVP + separator + two.getWonderVP() + lineReturn;
        }
        if (!(defeatingOpposingUnitVP == two.getDefeatingOpposingUnitVP()))
        {
                toReturn = toReturn + "DefeatingOpposingUnitVP: " + defeatingOpposingUnitVP + separator + two.getDefeatingOpposingUnitVP() + lineReturn;
        }
        if (!(advancementVP == two.getAdvancementVP()))
        {
                toReturn = toReturn + "AdvancementVP: " + advancementVP + separator + two.getAdvancementVP() + lineReturn;
        }
        if (!(cityConquestVP == two.getCityConquestVP()))
        {
                toReturn = toReturn + "CityConquestVP: " + cityConquestVP + separator + two.getCityConquestVP() + lineReturn;
        }
        if (!(victoryPointVP == two.getVictoryPointVP()))
        {
                toReturn = toReturn + "VictoryPointVP: " + victoryPointVP + separator + two.getVictoryPointVP() + lineReturn;
        }
        if (!(captureSpecialUnitVP == two.getCaptureSpecialUnitVP()))
        {
                toReturn = toReturn + "CaptureSpecialUnitVP: " + captureSpecialUnitVP + separator + two.getCaptureSpecialUnitVP() + lineReturn;
        }
        if (!(questionMark1 == two.getQuestionMark1()))
        {
                toReturn = toReturn + "QuestionMark1: " + questionMark1 + separator + two.getQuestionMark1() + lineReturn;
        }
        if (!(questionMark2 == two.getQuestionMark2()))
        {
                toReturn = toReturn + "QuestionMark2: " + questionMark2 + separator + two.getQuestionMark2() + lineReturn;
        }
        if (alliance0.compareTo(two.getAlliance0()) != 0)
        {
                toReturn = toReturn + "Alliance0: " + alliance0 + separator + two.getAlliance0() + lineReturn;
        }
        if (alliance1.compareTo(two.getAlliance1()) != 0)
        {
                toReturn = toReturn + "Alliance1: " + alliance1 + separator + two.getAlliance1() + lineReturn;
        }
        if (alliance2.compareTo(two.getAlliance2()) != 0)
        {
                toReturn = toReturn + "Alliance2: " + alliance2 + separator + two.getAlliance2() + lineReturn;
        }
        if (alliance3.compareTo(two.getAlliance3()) != 0)
        {
                toReturn = toReturn + "Alliance3: " + alliance3 + separator + two.getAlliance3() + lineReturn;
        }
        if (alliance4.compareTo(two.getAlliance4()) != 0)
        {
                toReturn = toReturn + "Alliance4: " + alliance4 + separator + two.getAlliance4() + lineReturn;
        }
        if (!(allianceVictoryType == two.getAllianceVictoryType()))
        {
                toReturn = toReturn + "AllianceVictoryType: " + allianceVictoryType + separator + two.getAllianceVictoryType() + lineReturn;
        }
        if (plaugeName.compareTo(two.getPlaugeName()) != 0)
        {
                toReturn = toReturn + "PlaugeName: " + plaugeName + separator + two.getPlaugeName() + lineReturn;
        }
        if (!(permitPlagues == two.getPermitPlagues()))
        {
                toReturn = toReturn + "PermitPlagues: " + permitPlagues + separator + two.getPermitPlagues() + lineReturn;
        }
        if (!(plagueEarliestStart == two.getPlagueEarliestStart()))
        {
                toReturn = toReturn + "PlagueEarliestStart: " + plagueEarliestStart + separator + two.getPlagueEarliestStart() + lineReturn;
        }
        if (!(plagueVariation == two.getPlagueVariation()))
        {
                toReturn = toReturn + "PlagueVariation: " + plagueVariation + separator + two.getPlagueVariation() + lineReturn;
        }
        if (!(plagueDuration == two.getPlagueDuration()))
        {
                toReturn = toReturn + "PlagueDuration: " + plagueDuration + separator + two.getPlagueDuration() + lineReturn;
        }
        if (!(plagueStrength == two.getPlagueStrength()))
        {
                toReturn = toReturn + "PlagueStrength: " + plagueStrength + separator + two.getPlagueStrength() + lineReturn;
        }
        if (!(plagueGracePeriod == two.getPlagueGracePeriod()))
        {
                toReturn = toReturn + "PlagueGracePeriod: " + plagueGracePeriod + separator + two.getPlagueGracePeriod() + lineReturn;
        }
        if (!(plagueMaxOccurance == two.getPlagueMaxOccurance()))
        {
                toReturn = toReturn + "PlagueMaxOccurance: " + plagueMaxOccurance + separator + two.getPlagueMaxOccurance() + lineReturn;
        }
        if (!(questionMark3 == two.getQuestionMark3()))
        {
                toReturn = toReturn + "QuestionMark3: " + questionMark3 + separator + two.getQuestionMark3() + lineReturn;
        }
        if (unknown.compareTo(two.getUnknown()) != 0)
        {
                toReturn = toReturn + "Unknown: " + unknown + separator + two.getUnknown() + lineReturn;
        }
        if (!(respawnFlagUnits == two.getRespawnFlagUnits()))
        {
                toReturn = toReturn + "RespawnFlagUnits: " + respawnFlagUnits + separator + two.getRespawnFlagUnits() + lineReturn;
        }
        if (!(captureAnyFlag == two.getCaptureAnyFlag()))
        {
                toReturn = toReturn + "CaptureAnyFlag: " + captureAnyFlag + separator + two.getCaptureAnyFlag() + lineReturn;
        }
        if (!(goldForCapture == two.getGoldForCapture()))
        {
                toReturn = toReturn + "GoldForCapture: " + goldForCapture + separator + two.getGoldForCapture() + lineReturn;
        }
        if (!(mapVisible == two.getMapVisible()))
        {
                toReturn = toReturn + "MapVisible: " + mapVisible + separator + two.getMapVisible() + lineReturn;
        }
        if (!(retainCulture == two.getRetainCulture()))
        {
                toReturn = toReturn + "RetainCulture: " + retainCulture + separator + two.getRetainCulture() + lineReturn;
        }
        if (!(questionMark4 == two.getQuestionMark4()))
        {
                toReturn = toReturn + "QuestionMark4: " + questionMark4 + separator + two.getQuestionMark4() + lineReturn;
        }
        if (!(eruptionPeriod == two.getEruptionPeriod()))
        {
                toReturn = toReturn + "EruptionPeriod: " + eruptionPeriod + separator + two.getEruptionPeriod() + lineReturn;
        }
        if (!(mpBaseTime == two.getMpBaseTime()))
        {
                toReturn = toReturn + "MpBaseTime: " + mpBaseTime + separator + two.getMpBaseTime() + lineReturn;
        }
        if (!(mpCityTime == two.getMpCityTime()))
        {
                toReturn = toReturn + "MpCityTime: " + mpCityTime + separator + two.getMpCityTime() + lineReturn;
        }
        if (!(mpUnitTime == two.getMpUnitTime()))
        {
                toReturn = toReturn + "MpUnitTime: " + mpUnitTime + separator + two.getMpUnitTime() + lineReturn;
        }
        if (toReturn.equals(""))
        {
            toReturn = "";
        }
        return toReturn;
    }
    public Object getProperty(String string) throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }
}
