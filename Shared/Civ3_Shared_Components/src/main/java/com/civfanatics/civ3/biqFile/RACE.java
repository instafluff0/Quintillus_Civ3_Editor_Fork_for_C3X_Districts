package com.civfanatics.civ3.biqFile;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Other sections that know about RACE: game, PRTO
 * @author Quintillus
 */
import java.util.*;
public class RACE extends BIQSection{
    private int dataLength = 2388;   //without any optional stuff
    private int numCityNames;
    private ArrayList<String>cityName;
    private int numGreatLeaders;
    private ArrayList<String>greatLeader;
    private String leaderName = "";
    private String leaderTitle = "";
    private String civilopediaEntry = "";
    private String adjective = "";
    private String civilizationName = "";
    private String noun = "";
    //Maximum of 260 characters.  Why?  Probably compatibility with some parts
    //of the Windows API, which limited paths to 256 characters (C:\, 256 chars,
    //and a null, for instance).  This apparently is a carryover from 16-bit
    //Windows.
    private ArrayList<String>forwardFilename;
    private ArrayList<String>reverseFilename;
    private int cultureGroup;
    private int leaderGender;
    private int civilizationGender;
    private int aggressionLevel;
    private int uniqueCivilizationCounter;
    private int shunnedGovernment;
    private int favoriteGovernment;
    private int defaultColor;
    private int uniqueColor;
    private int freeTech1Index;
    private int freeTech2Index;
    private int freeTech3Index;
    private int freeTech4Index;
    private TECH freeTech1;
    private TECH freeTech2;
    private TECH freeTech3;
    private TECH freeTech4;
    private int bonuses;
    private int governorSettings;
    private int buildNever;
    private int buildOften;
    private int plurality;
    private int kingUnitInt;
    private PRTO kingUnit;
    private int flavors;
    private ArrayList<Boolean>flavours;
    private int numFlavors;
    private int questionMark;
    private int diplomacyTextIndex;
    private int numScientificLeaders;
    //civ traits
    private boolean militaristic;
    private boolean commercial;
    private boolean expansionist;
    private boolean scientific;
    private boolean religious;
    private boolean industrious;
    private boolean agricultural;
    private boolean seafaring;
    //governor settings
    private boolean manageCitizens;
    private boolean emphasizeFood;
    private boolean emphasizeShields;
    private boolean emphasizeTrade;
    private boolean manageProduction;
    private boolean noWonders;
    private boolean noSmallWonders;
    //build never
    private boolean noOffensiveLandUnits;
    private boolean noDefensiveLandUnits;
    private boolean noArtillery;
    private boolean noSettlers;
    private boolean noWorkers;
    private boolean noShips;
    private boolean noAirUnits;
    private boolean noGrowth;
    private boolean noProduction;
    private boolean noHappiness;
    private boolean noScience;
    private boolean noWealth;
    private boolean noTrade;
    private boolean noExploration;
    private boolean noCulture;
    //build often
    private boolean manyOffensiveLandUnits;
    private boolean manyDefensiveLandUnits;
    private boolean manyArtillery;
    private boolean manySettlers;
    private boolean manyWorkers;
    private boolean manyShips;
    private boolean manyAirUnits;
    private boolean manyGrowth;
    private boolean manyProduction;
    private boolean manyHappiness;
    private boolean manyScience;
    private boolean manyWealth;
    private boolean manyTrade;
    private boolean manyExploration;
    private boolean manyCulture;
    private ArrayList<String>scientificLeader;

    public RACE(IO baselink)
    {
        super(baselink);
        cityName = new ArrayList<String>();
        greatLeader = new ArrayList<String>();
        forwardFilename = new ArrayList<String>();
        reverseFilename = new ArrayList<String>();
        scientificLeader = new ArrayList<String>();
        flavours = new ArrayList<Boolean>();
    }

    public RACE(String name, int numFlavors, IO baselink)
    {
        super(baselink);
        civilizationName = name;
        cityName = new ArrayList<String>();
        greatLeader = new ArrayList<String>();
        forwardFilename = new ArrayList<String>();
        reverseFilename = new ArrayList<String>();
        for (int i = 0; i < 4; i++)
        {
            forwardFilename.add(new String(""));
            reverseFilename.add(new String(""));
        }
        scientificLeader = new ArrayList<String>();
        flavours = new ArrayList<Boolean>(numFlavors);
        favoriteGovernment = -1;
        shunnedGovernment = -1;
        kingUnitInt = -1;
        freeTech1Index = -1;
        freeTech2Index = -1;
        freeTech3Index = -1;
        freeTech4Index = -1;
        cultureGroup = -1;
    }
	public void trim()
	{
		for (int j = 0; j < cityName.size(); j++)
		{
			cityName.set(j, cityName.get(j).trim());
		}
		for (int j = 0; j < greatLeader.size(); j++)
		{
			greatLeader.set(j, greatLeader.get(j).trim());
		}
		for (int j = 0; j < scientificLeader.size(); j++)
		{
			scientificLeader.set(j, scientificLeader.get(j).trim());
		}
		leaderName = leaderName.trim();
		leaderTitle = leaderTitle.trim();
		civilopediaEntry = civilopediaEntry.trim();
        adjective = adjective.trim();
        civilizationName = civilizationName.trim();
		noun = noun.trim();
		for (int j = 0; j < forwardFilename.size(); j++)
		{
			forwardFilename.set(j, forwardFilename.get(j).trim());
		}
		for (int j = 0; j < reverseFilename.size(); j++)
		{
			reverseFilename.set(j, reverseFilename.get(j).trim());
		}
	}
    public int getDataLength()
    {
        return dataLength;
    }

    public int getNumCityNames()
    {
        return numCityNames;
    }

    public int getNumGreatLeaders()
    {
        return numGreatLeaders;
    }

    public String getLeaderName()
    {
        return leaderName;
    }

    public String getLeaderTitle()
    {
        return leaderTitle;
    }

    public String getCivilopediaEntry()
    {
        return civilopediaEntry;
    }

    public String getAdjective()
    {
        return adjective;
    }
    public String getName()
    {
        return civilizationName;
    }
    public String getCivilizationName()
    {
        return civilizationName;
    }

    public String getNoun()
    {
        return noun;
    }

    public int getCultureGroup()
    {
        return cultureGroup;
    }

    public int getLeaderGender()
    {
        return leaderGender;
    }

    public int getCivilizationGender()
    {
        return civilizationGender;
    }

    public int getAggressionLevel()
    {
        return aggressionLevel;
    }

    public int getUniqueCivilizationCounter()
    {
        return uniqueCivilizationCounter;
    }

    public int getShunnedGovernment()
    {
        return shunnedGovernment;
    }

    public int getFavoriteGovernment()
    {
        return favoriteGovernment;
    }

    public int getDefaultColor()
    {
        return defaultColor;
    }

    public int getUniqueColor()
    {
        return uniqueColor;
    }

    public int getFreeTech1Index()
    {
        return freeTech1Index;
    }

    public int getFreeTech2Index()
    {
        return freeTech2Index;
    }

    public int getFreeTech3Index()
    {
        return freeTech3Index;
    }

    public int getFreeTech4Index()
    {
        return freeTech4Index;
    }

    public int getBonuses()
    {
        return bonuses;
    }

    public int getGovernorSettings()
    {
        return governorSettings;
    }

    public int getBuildNever()
    {
        return buildNever;
    }

    public int getBuildOften()
    {
        return buildOften;
    }

    public int getPlurality()
    {
        return plurality;
    }

    public int getKingUnit()
    {
        return kingUnitInt;
    }

    public int getFlavors()
    {
        return flavors;
    }

    public int getQuestionMark()
    {
        return questionMark;
    }

    public int getDiplomacyTextIndex()
    {
        return diplomacyTextIndex;
    }

    public int getNumScientificLeaders()
    {
        return numScientificLeaders;
    }

    public boolean getMilitaristic()
    {
        return militaristic;
    }

    public boolean getCommercial()
    {
        return commercial;
    }

    public boolean getExpansionist()
    {
        return expansionist;
    }

    public boolean getScientific()
    {
        return scientific;
    }

    public boolean getReligious()
    {
        return religious;
    }

    public boolean getIndustrious()
    {
        return industrious;
    }

    public boolean getAgricultural()
    {
        return agricultural;
    }

    public boolean getSeafaring()
    {
        return seafaring;
    }
    public boolean getManageCitizens()
    {
        return manageCitizens;
    }

    public boolean getEmphasizeFood()
    {
        return emphasizeFood;
    }

    public boolean getEmphasizeShields()
    {
        return emphasizeShields;
    }

    public boolean getEmphasizeTrade()
    {
        return emphasizeTrade;
    }

    public boolean getManageProduction()
    {
        return manageProduction;
    }

    public boolean getNoWonders()
    {
        return noWonders;
    }

    public boolean getNoSmallWonders()
    {
        return noSmallWonders;
    }
    public boolean getNoOffensiveLandUnits()
    {
        return noOffensiveLandUnits;
    }

    public boolean getNoDefensiveLandUnits()
    {
        return noDefensiveLandUnits;
    }

    public boolean getNoArtillery()
    {
        return noArtillery;
    }

    public boolean getNoSettlers()
    {
        return noSettlers;
    }

    public boolean getNoWorkers()
    {
        return noWorkers;
    }

    public boolean getNoShips()
    {
        return noShips;
    }

    public boolean getNoAirUnits()
    {
        return noAirUnits;
    }

    public boolean getNoGrowth()
    {
        return noGrowth;
    }

    public boolean getNoProduction()
    {
        return noProduction;
    }

    public boolean getNoHappiness()
    {
        return noHappiness;
    }

    public boolean getNoScience()
    {
        return noScience;
    }

    public boolean getNoWealth()
    {
        return noWealth;
    }

    public boolean getNoTrade()
    {
        return noTrade;
    }

    public boolean getNoExploration()
    {
        return noExploration;
    }

    public boolean getNoCulture()
    {
        return noCulture;
    }
    public boolean getManyOffensiveLandUnits()
    {
        return manyOffensiveLandUnits;
    }

    public boolean getManyDefensiveLandUnits()
    {
        return manyDefensiveLandUnits;
    }

    public boolean getManyArtillery()
    {
        return manyArtillery;
    }

    public boolean getManySettlers()
    {
        return manySettlers;
    }

    public boolean getManyWorkers()
    {
        return manyWorkers;
    }

    public boolean getManyShips()
    {
        return manyShips;
    }

    public boolean getManyAirUnits()
    {
        return manyAirUnits;
    }

    public boolean getManyGrowth()
    {
        return manyGrowth;
    }

    public boolean getManyProduction()
    {
        return manyProduction;
    }

    public boolean getManyHappiness()
    {
        return manyHappiness;
    }

    public boolean getManyScience()
    {
        return manyScience;
    }

    public boolean getManyWealth()
    {
        return manyWealth;
    }

    public boolean getManyTrade()
    {
        return manyTrade;
    }

    public boolean getManyExploration()
    {
        return manyExploration;
    }

    public boolean getManyCulture()
    {
        return manyCulture;
    }

    public void setLeaderName(String leaderName)
    {
        this.leaderName = leaderName;
    }

    public void setLeaderTitle(String leaderTitle)
    {
        this.leaderTitle = leaderTitle;
    }

    public void setCivilopediaEntry(String civilopediaEntry)
    {
        this.civilopediaEntry = civilopediaEntry;
    }

    public void setAdjective(String adjective)
    {
        this.adjective = adjective;
    }

    public void setCivilizationName(String civilizationName)
    {
        this.civilizationName = civilizationName;
    }

    public void setNoun(String noun)
    {
        this.noun = noun;
    }

    public void setCultureGroup(int cultureGroup)
    {
        this.cultureGroup = cultureGroup;
    }

    public void setLeaderGender(int leaderGender)
    {
        this.leaderGender = leaderGender;
    }

    public void setCivilizationGender(int civilizationGender)
    {
        this.civilizationGender = civilizationGender;
    }

    public void setAggressionLevel(int aggressionLevel)
    {
        this.aggressionLevel = aggressionLevel;
    }

    public void setUniqueCivilizationCounter(int uniqueCivilizationCounter)
    {
        this.uniqueCivilizationCounter = uniqueCivilizationCounter;
    }

    public void setShunnedGovernment(int shunnedGovernment)
    {
        this.shunnedGovernment = shunnedGovernment;
    }

    public void setFavoriteGovernment(int favoriteGovernment)
    {
        this.favoriteGovernment = favoriteGovernment;
    }

    public void setDefaultColor(int defaultColor)
    {
        this.defaultColor = defaultColor;
    }

    public void setUniqueColor(int uniqueColor)
    {
        this.uniqueColor = uniqueColor;
    }
    
    public void refreshFreeTechs() {
        setFreeTech1Index(freeTech1Index);
        setFreeTech2Index(freeTech2Index);
        setFreeTech3Index(freeTech3Index);
        setFreeTech4Index(freeTech4Index);
    }

    public void setFreeTech1Index(int freeTech1Index)
    {
        this.freeTech1Index = freeTech1Index;
        if (baseLink.technology != null && freeTech1Index != -1 && baseLink.technology.size() > freeTech1Index) {
            this.freeTech1 = baseLink.technology.get(freeTech1Index);
        }
    }

    public void setFreeTech2Index(int freeTech2Index)
    {
        this.freeTech2Index = freeTech2Index;
        if (baseLink.technology != null && freeTech2Index != -1 && baseLink.technology.size() > freeTech2Index) {
            this.freeTech2 = baseLink.technology.get(freeTech2Index);
        }
    }

    public void setFreeTech3Index(int freeTech3Index)
    {
        this.freeTech3Index = freeTech3Index;
        if (baseLink.technology != null && freeTech3Index != -1 && baseLink.technology.size() > freeTech3Index) {
            this.freeTech3 = baseLink.technology.get(freeTech3Index);
        }
    }

    public void setFreeTech4Index(int freeTech4Index)
    {
        this.freeTech4Index = freeTech4Index;
        if (baseLink.technology != null && freeTech4Index != -1 && baseLink.technology.size() > freeTech4Index) {
            this.freeTech4 = baseLink.technology.get(freeTech4Index);
        }
    }

    public void setBonuses(int bonuses)
    {
        this.bonuses = bonuses;
    }

    public void setGovernorSettings(int governorSettings)
    {
        this.governorSettings = governorSettings;
    }

    public void setBuildNever(int buildNever)
    {
        this.buildNever = buildNever;
    }

    public void setBuildOften(int buildOften)
    {
        this.buildOften = buildOften;
    }

    public void setPlurality(int plurality)
    {
        this.plurality = plurality;
    }

    public void setKingUnit(int kingUnitInt)
    {
        this.kingUnitInt = kingUnitInt;
        if (baseLink.unit != null && kingUnitInt != -1 && baseLink.unit.size() > kingUnitInt) {
            this.kingUnit = baseLink.unit.get(kingUnitInt);
        }
    }

    public void setFlavors(int flavors)
    {
        this.flavors = flavors;
    }

    public void setQuestionMark(int questionMark)
    {
        this.questionMark = questionMark;
    }

    public void setDiplomacyTextIndex(int diplomacyTextIndex)
    {
        this.diplomacyTextIndex = diplomacyTextIndex;
    }

    public void setMilitaristic(boolean militaristic)
    {
        this.militaristic = militaristic;
    }

    public void setCommercial(boolean commercial)
    {
        this.commercial = commercial;
    }

    public void setExpansionist(boolean expansionist)
    {
        this.expansionist = expansionist;
    }

    public void setScientific(boolean scientific)
    {
        this.scientific = scientific;
    }

    public void setReligious(boolean religious)
    {
        this.religious = religious;
    }

    public void setIndustrious(boolean industrious)
    {
        this.industrious = industrious;
    }

    public void setAgricultural(boolean agricultural)
    {
        this.agricultural = agricultural;
    }

    public void setSeafaring(boolean seafaring)
    {
        this.seafaring = seafaring;
    }

    public void setManageCitizens(boolean manageCitizens)
    {
        this.manageCitizens = manageCitizens;
    }

    public void setEmphasizeFood(boolean emphasizeFood)
    {
        this.emphasizeFood = emphasizeFood;
    }

    public void setEmphasizeShields(boolean emphasizeShields)
    {
        this.emphasizeShields = emphasizeShields;
    }

    public void setEmphasizeTrade(boolean emphasizeTrade)
    {
        this.emphasizeTrade = emphasizeTrade;
    }

    public void setManageProduction(boolean manageProduction)
    {
        this.manageProduction = manageProduction;
    }

    public void setNoWonders(boolean noWonders)
    {
        this.noWonders = noWonders;
    }

    public void setNoSmallWonders(boolean noSmallWonders)
    {
        this.noSmallWonders = noSmallWonders;
    }

    public void setNoOffensiveLandUnits(boolean noOffensiveLandUnits)
    {
        this.noOffensiveLandUnits = noOffensiveLandUnits;
    }

    public void setNoDefensiveLandUnits(boolean noDefensiveLandUnits)
    {
        this.noDefensiveLandUnits = noDefensiveLandUnits;
    }

    public void setNoArtillery(boolean noArtillery)
    {
        this.noArtillery = noArtillery;
    }

    public void setNoSettlers(boolean noSettlers)
    {
        this.noSettlers = noSettlers;
    }

    public void setNoWorkers(boolean noWorkers)
    {
        this.noWorkers = noWorkers;
    }

    public void setNoShips(boolean noShips)
    {
        this.noShips = noShips;
    }

    public void setNoAirUnits(boolean noAirUnits)
    {
        this.noAirUnits = noAirUnits;
    }

    public void setNoGrowth(boolean noGrowth)
    {
        this.noGrowth = noGrowth;
    }

    public void setNoProduction(boolean noProduction)
    {
        this.noProduction = noProduction;
    }

    public void setNoHappiness(boolean noHappiness)
    {
        this.noHappiness = noHappiness;
    }

    public void setNoScience(boolean noScience)
    {
        this.noScience = noScience;
    }

    public void setNoWealth(boolean noWealth)
    {
        this.noWealth = noWealth;
    }

    public void setNoTrade(boolean noTrade)
    {
        this.noTrade = noTrade;
    }

    public void setNoExploration(boolean noExploration)
    {
        this.noExploration = noExploration;
    }

    public void setNoCulture(boolean noCulture)
    {
        this.noCulture = noCulture;
    }

    public void setManyOffensiveLandUnits(boolean manyOffensiveLandUnits)
    {
        this.manyOffensiveLandUnits = manyOffensiveLandUnits;
    }

    public void setManyDefensiveLandUnits(boolean manyDefensiveLandUnits)
    {
        this.manyDefensiveLandUnits = manyDefensiveLandUnits;
    }

    public void setManyArtillery(boolean manyArtillery)
    {
        this.manyArtillery = manyArtillery;
    }

    public void setManySettlers(boolean manySettlers)
    {
        this.manySettlers = manySettlers;
    }

    public void setManyWorkers(boolean manyWorkers)
    {
        this.manyWorkers = manyWorkers;
    }

    public void setManyShips(boolean manyShips)
    {
        this.manyShips = manyShips;
    }

    public void setManyAirUnits(boolean manyAirUnits)
    {
        this.manyAirUnits = manyAirUnits;
    }

    public void setManyGrowth(boolean manyGrowth)
    {
        this.manyGrowth = manyGrowth;
    }

    public void setManyProduction(boolean manyProduction)
    {
        this.manyProduction = manyProduction;
    }

    public void setManyHappiness(boolean manyHappiness)
    {
        this.manyHappiness = manyHappiness;
    }

    public void setManyScience(boolean manyScience)
    {
        this.manyScience = manyScience;
    }

    public void setManyWealth(boolean manyWealth)
    {
        this.manyWealth = manyWealth;
    }

    public void setManyTrade(boolean manyTrade)
    {
        this.manyTrade = manyTrade;
    }

    public void setManyExploration(boolean manyExploration)
    {
        this.manyExploration = manyExploration;
    }

    public void setManyCulture(boolean manyCulture)
    {
        this.manyCulture = manyCulture;
    }
    
    public void handleConvertFromPTWToConquests()
    {
        if (IO.convertDataLengthsForConquests)
            this.dataLength+=16;
    }
    
    public List<String> getMilitaryLeaders()
    {
        return Collections.unmodifiableList(this.greatLeader);
    }
    
    public void addMilitaryLeader(String name)
    {
        this.numGreatLeaders++;
        this.greatLeader.add(name);
        this.dataLength+=32;
    }
    
    public void renameMilitaryLeader(int index, String newName) {
        this.greatLeader.set(index, newName);
    }
    
    public void removeMilitaryLeader(int index)
    {
        this.numGreatLeaders--;
        this.greatLeader.remove(index);
        this.dataLength-=32;
    }
    
    public List<String> getScientificLeaders()
    {
        return Collections.unmodifiableList(this.scientificLeader);
    }
    
    public void addScientificLeader(String name)
    {
        this.numScientificLeaders++;
        this.scientificLeader.add(name);
        this.dataLength+=32;
    }
    
    public void renameScientificLeader(int index, String newName) {
        this.scientificLeader.set(index, newName);
    }
    
    public void removeScientificLeader(int index)
    {
        this.numScientificLeaders--;
        this.scientificLeader.remove(index);
        this.dataLength-=32;
    }
    
    public List<String> getCityNames()
    {
        return Collections.unmodifiableList(this.cityName);
    }
    
    public void addCityName(String name)
    {
        this.numCityNames++;
        this.cityName.add(name);
        this.dataLength+=24;
    }
    
    public void renameCity(int index, String newName) {
        this.cityName.set(index, newName);
    }
    
    public void removeCityName(int index)
    {
        this.cityName.remove(index);
        this.numCityNames--;
        this.dataLength-=24;
    }
    
    public void handleAddedFlavour()
    {
        this.flavours.add(false);
        this.numFlavors++;
        //don't need to create binary since the new one is false
    }
    
    public void handleDeletedFlavour(int index)
    {
        this.flavours.remove(index);
        this.numFlavors--;
        //re-create the binary flavour item
        this.createBinary();
    }
    
    public int getNumFlavours()
    {
        return this.numFlavors;
    }
    
    /**
     * Sets all flavours to false.
     */
    public void resetFlavours()
    {
        for (int i = 0; i < flavours.size(); i++)
        {
            flavours.set(i, false);
        }
        createBinary();
    }
    
    public void setFlavour(int index, Boolean value)
    {
        flavours.set(index, value);
        createBinary();
    }
    
    public boolean getFlavour(int index)
    {
        return flavours.get(index);
    }
    
    public void handleDeletedUnit(int index)
    {
        if (kingUnitInt == index)
        {
            kingUnitInt = -1;
            kingUnit = null;
        }
        else if (kingUnitInt > index)
            kingUnitInt--;
    }
    
    public void handleSwappedUnit() {
        if (kingUnitInt != -1) {
            kingUnitInt = kingUnit.getIndex();
        }
    }
    
    public void handleDeletedTechnology(int index)
    {
        if (freeTech1Index == index) {
            freeTech1Index = -1;
            freeTech1 = null;
        }
        else if (freeTech1Index > index)
            freeTech1Index--;
        
        if (freeTech2Index == index) {
            freeTech2Index = -1;
            freeTech2 = null;
        }
        else if (freeTech2Index > index)
            freeTech2Index--;
        
        if (freeTech3Index == index) {
            freeTech3Index = -1;
            freeTech3 = null;
        }
        else if (freeTech3Index > index)
            freeTech3Index--;
        
        if (freeTech4Index == index) {
            freeTech4Index = -1;
            freeTech4 = null;
        }
        else if (freeTech4Index > index)
            freeTech4Index--;
    }
    
    public void handleSwappedTech() {
        if (freeTech1Index != -1) {
            freeTech1Index = freeTech1.getIndex();
        }
        if (freeTech2Index != -1) {
            freeTech2Index = freeTech2.getIndex();
        }
        if (freeTech3Index != -1) {
            freeTech3Index = freeTech3.getIndex();
        }
        if (freeTech4Index != -1) {
            freeTech4Index = freeTech4.getIndex();
        }
    }
    
    public void handleDeletedGovernment(int index)
    {
        if (favoriteGovernment == index)
            favoriteGovernment = -1;
        else if (favoriteGovernment > index)
            favoriteGovernment--;
        if (shunnedGovernment == index)
            shunnedGovernment = -1;
        else if (shunnedGovernment > index)
            shunnedGovernment--;
    }
    
    /**
     * Adds a forward filename.  This happens when an era is added.  Since Civ3
     * only supports 4 eras, this is of limited use.
     * @param index
     * @param filename 
     */
    public void addForwardFilename(String filename)
    {
        forwardFilename.add(filename);
    }
    
    public void setForwardFilename(int index, String filename)
    {
        forwardFilename.set(index, filename);
    }
    
    public String getForwardFilename(int index)
    {
        return forwardFilename.get(index);
    }
    
    /**
     * Adds a reverse filename.  This happens when an era is added.  Since Civ3
     * only supports 4 eras, this is of limited use.
     * @param index
     * @param filename 
     */
    public void addReverseFilename(String filename)
    {
        reverseFilename.add(filename);
    }
    
    public void setReverseFilename(int index, String filename)
    {
        reverseFilename.set(index, filename);
    }
    
    public String getReverseFilename(int index)
    {
        return reverseFilename.get(index);
    }

    @Override
    public String toString()
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn =  "dataLength: " + dataLength + lineReturn;
        toReturn = toReturn + "numCityNames: " + numCityNames + lineReturn;
        for (int j = 0; j < cityName.size(); j++)
        {
            toReturn = toReturn + cityName.get(j) + lineReturn;
        }
        toReturn = toReturn + "numGreatLeaders: " + numGreatLeaders + lineReturn;
        for (int j = 0; j < numGreatLeaders; j++)
        {
            toReturn = toReturn + greatLeader.get(j) + lineReturn;
        }
        toReturn = toReturn + "leaderName: " + leaderName + lineReturn;
        toReturn = toReturn + "leaderTitle: " + leaderTitle + lineReturn;
        toReturn = toReturn + "civilopediaEntry: " + civilopediaEntry + lineReturn;
        toReturn = toReturn + "adjective: " + adjective + lineReturn;
        toReturn = toReturn + "civilizationName: " + civilizationName + lineReturn;
        toReturn = toReturn + "noun: " + noun + lineReturn;
        for (int j = 0; j < forwardFilename.size(); j++)
        {
            toReturn = toReturn + "forwardFilename for era " + j + ": " + forwardFilename.get(j) + lineReturn;
        }
        for (int j = 0; j < reverseFilename.size(); j++)
        {
            toReturn = toReturn + "reverseFilename for era " + j + ": " + reverseFilename.get(j) + lineReturn;
        }
        toReturn = toReturn + "cultureGroup: " + cultureGroup + lineReturn;
        toReturn = toReturn + "leaderGender: " + leaderGender + lineReturn;
        toReturn = toReturn + "civilizationGender: " + civilizationGender + lineReturn;
        toReturn = toReturn + "aggressionLevel: " + aggressionLevel + lineReturn;
        toReturn = toReturn + "uniqueCivilizationCounter: " + uniqueCivilizationCounter + lineReturn;
        toReturn = toReturn + "shunnedGovernment: " + shunnedGovernment + lineReturn;
        toReturn = toReturn + "favoriteGovernment: " + favoriteGovernment + lineReturn;
        toReturn = toReturn + "defaultColor: " + defaultColor + lineReturn;
        toReturn = toReturn + "uniqueColor: " + uniqueColor + lineReturn;
        toReturn = toReturn + "freeTech1Index: " + freeTech1Index + lineReturn;
        toReturn = toReturn + "freeTech2Index: " + freeTech2Index + lineReturn;
        toReturn = toReturn + "freeTech3Index: " + freeTech3Index + lineReturn;
        toReturn = toReturn + "freeTech4Index: " + freeTech4Index + lineReturn;
        toReturn = toReturn + "civilization traits: " + bonuses + lineReturn;
        toReturn = toReturn + "governorSettings: " + governorSettings + lineReturn;
        toReturn = toReturn + "buildNever: " + buildNever + lineReturn;
        toReturn = toReturn + "buildOften: " + buildOften + lineReturn;
        toReturn = toReturn + "plurality: " + plurality + lineReturn;
        toReturn = toReturn + "kingUnit: " + kingUnitInt + lineReturn;
        toReturn = toReturn + "flavors: " + flavors + lineReturn;
        toReturn = toReturn + "questionMark: " + questionMark + lineReturn;
        toReturn = toReturn + "diplomacyTextIndex: " + diplomacyTextIndex + lineReturn;
        toReturn = toReturn + "numScientificLeaders: " + numScientificLeaders + lineReturn;
        for (int j = 0; j < numScientificLeaders; j++)
        {
            toReturn = toReturn + scientificLeader.get(j) + lineReturn;
        }
        toReturn = toReturn + lineReturn;
        return toReturn;
    }

    public void createBinary()
    {
        //Traits
        bonuses = 0;
        if (militaristic)
            bonuses+=1;
        if (commercial)
            bonuses+=2;
        if (expansionist)
            bonuses+=4;
        if (scientific)
            bonuses+=8;
        if (religious)
            bonuses+=16;
        if (industrious)
            bonuses+=32;
        if (agricultural)
            bonuses+=64;
        if (seafaring)
            bonuses+=128;
        //Governor Settings
        this.governorSettings = 0;
        if (manageCitizens)
            governorSettings+=1;
        if (emphasizeFood)
            governorSettings+=2;
        if (emphasizeShields)
            governorSettings+=4;
        if (emphasizeTrade)
            governorSettings+=8;
        if (manageProduction)
            governorSettings+=16;
        if (noWonders)
            governorSettings+=32;
        if (noSmallWonders)
            governorSettings+=64;
        //Build Often
        this.buildOften = 0;
        if (this.manyOffensiveLandUnits)
            buildOften+=1;
        if (this.manyDefensiveLandUnits)
            buildOften+=2;
        if (this.manyArtillery)
            buildOften+=4;
        if (this.manySettlers)
            buildOften+=8;
        if (this.manyWorkers)
            buildOften+=16;
        if (this.manyShips)
            buildOften+=32;
        if (this.manyAirUnits)
            buildOften+=64;
        if (this.manyGrowth)
            buildOften+=128;
        if (this.manyProduction)
            buildOften+=256;
        if (this.manyHappiness)
            buildOften+=512;
        if (this.manyScience)
            buildOften+=1024;
        if (this.manyWealth)
            buildOften+=2048;
        if (this.manyTrade)
            buildOften+=4096;
        if (this.manyExploration)
            buildOften+=8192;
        if (this.manyCulture)
            buildOften+=16384;
        //Build Never
        this.buildNever = 0;
        if (this.noOffensiveLandUnits)
            buildNever+=1;
        if (this.noDefensiveLandUnits)
            buildNever+=2;
        if (this.noArtillery)
            buildNever+=4;
        if (this.noSettlers)
            buildNever+=8;
        if (this.noWorkers)
            buildNever+=16;
        if (this.noShips)
            buildNever+=32;
        if (this.noAirUnits)
            buildNever+=64;
        if (this.noGrowth)
            buildNever+=128;
        if (this.noProduction)
            buildNever+=256;
        if (this.noHappiness)
            buildNever+=512;
        if (this.noScience)
            buildNever+=1024;
        if (this.noWealth)
            buildNever+=2048;
        if (this.noTrade)
            buildNever+=4096;
        if (this.noExploration)
            buildNever+=8192;
        if (this.noCulture)
            buildNever+=16384;
        //flavours
        long sum = 0;
        for (int i = 0; i < this.flavours.size(); i++)
            if (flavours.get(i))
                sum+=(int)Math.pow(2, i);
        flavors = (int)sum;
    }

    public void extractEnglish(int numFlavours)
    {
        int bonusCopy = bonuses;
        int divBy = 0;
        divBy = bonusCopy/(int)(Math.pow(2, 7));
        if (divBy == 1)
        {
            seafaring = true;
            bonusCopy-=128;
        }
        divBy = bonusCopy/(int)(Math.pow(2, 6));
        if (divBy == 1)
        {
            agricultural = true;
            bonusCopy-=64;
        }
        divBy = bonusCopy/(int)(Math.pow(2, 5));
        if (divBy == 1)
        {
            industrious = true;
            bonusCopy-=32;
        }
        divBy = bonusCopy/(int)(Math.pow(2, 4));
        if (divBy == 1)
        {
            religious = true;
            bonusCopy-=16;
        }
        divBy = bonusCopy/(int)(Math.pow(2, 3));
        if (divBy == 1)
        {
            scientific = true;
            bonusCopy-=8;
        }
        divBy = bonusCopy/(int)(Math.pow(2, 2));
        if (divBy == 1)
        {
            expansionist = true;
            bonusCopy-=4;
        }
        divBy = bonusCopy/(int)(Math.pow(2, 1));
        if (divBy == 1)
        {
            commercial = true;
            bonusCopy-=2;
        }
        divBy = bonusCopy/(int)(Math.pow(2, 0));
        if (divBy == 1)
        {
            militaristic = true;
            bonusCopy-=1;
        }
        int govCopy = governorSettings;
        divBy = govCopy/(int)(Math.pow(2, 6));
        if (divBy == 1)
        {
            noSmallWonders = true;
            govCopy-=64;
        }
        divBy = govCopy/(int)(Math.pow(2, 5));
        if (divBy == 1)
        {
            noWonders = true;
            govCopy-=32;
        }
        divBy = govCopy/(int)(Math.pow(2, 4));
        if (divBy == 1)
        {
            manageProduction = true;
            govCopy-=16;
        }
        divBy = govCopy/(int)(Math.pow(2, 3));
        if (divBy == 1)
        {
            emphasizeTrade = true;
            govCopy-=8;
        }
        divBy = govCopy/(int)(Math.pow(2, 2));
        if (divBy == 1)
        {
            emphasizeShields = true;
            govCopy-=4;
        }
        divBy = govCopy/(int)(Math.pow(2, 1));
        if (divBy == 1)
        {
            emphasizeFood = true;
            govCopy-=2;
        }
        divBy = govCopy/(int)(Math.pow(2, 0));
        if (divBy == 1)
        {
            manageCitizens = true;
            govCopy-=1;
        }
        int buildNeverCopy = buildNever;
        divBy = buildNeverCopy/(int)(Math.pow(2, 14));
        if (divBy == 1)
        {
            noCulture = true;
            buildNeverCopy-=16384;
        }
        divBy = buildNeverCopy/(int)(Math.pow(2, 13));
        if (divBy == 1)
        {
            noExploration = true;
            buildNeverCopy-=8192;
        }
        divBy = buildNeverCopy/(int)(Math.pow(2, 12));
        if (divBy == 1)
        {
            noTrade = true;
            buildNeverCopy-=4096;
        }
        divBy = buildNeverCopy/(int)(Math.pow(2, 11));
        if (divBy == 1)
        {
            noWealth = true;
            buildNeverCopy-=2048;
        }
        divBy = buildNeverCopy/(int)(Math.pow(2, 10));
        if (divBy == 1)
        {
            noScience = true;
            buildNeverCopy-=1024;
        }
        divBy = buildNeverCopy/(int)(Math.pow(2, 9));
        if (divBy == 1)
        {
            noHappiness = true;
            buildNeverCopy-=512;
        }
        divBy = buildNeverCopy/(int)(Math.pow(2, 8));
        if (divBy == 1)
        {
            noProduction = true;
            buildNeverCopy-=256;
        }
        divBy = buildNeverCopy/(int)(Math.pow(2, 7));
        if (divBy == 1)
        {
            noGrowth = true;
            buildNeverCopy-=128;
        }
        divBy = buildNeverCopy/(int)(Math.pow(2, 6));
        if (divBy == 1)
        {
            noAirUnits = true;
            buildNeverCopy-=64;
        }
        divBy = buildNeverCopy/(int)(Math.pow(2, 5));
        if (divBy == 1)
        {
            noShips = true;
            buildNeverCopy-=32;
        }
        divBy = buildNeverCopy/(int)(Math.pow(2, 4));
        if (divBy == 1)
        {
            noWorkers = true;
            buildNeverCopy-=16;
        }
        divBy = buildNeverCopy/(int)(Math.pow(2, 3));
        if (divBy == 1)
        {
            noSettlers = true;
            buildNeverCopy-=8;
        }
        divBy = buildNeverCopy/(int)(Math.pow(2, 2));
        if (divBy == 1)
        {
            noArtillery = true;
            buildNeverCopy-=4;
        }
        divBy = buildNeverCopy/(int)(Math.pow(2, 1));
        if (divBy == 1)
        {
            noDefensiveLandUnits = true;
            buildNeverCopy-=2;
        }
        divBy = buildNeverCopy/(int)(Math.pow(2, 0));
        if (divBy == 1)
        {
            noOffensiveLandUnits = true;
            buildNeverCopy-=1;
        }
            int buildOftenCopy = buildOften;
        divBy = buildOftenCopy/(int)(Math.pow(2, 14));
        if (divBy == 1)
        {
            manyCulture = true;
            buildOftenCopy-=16384;
        }
        divBy = buildOftenCopy/(int)(Math.pow(2, 13));
        if (divBy == 1)
        {
            manyExploration = true;
            buildOftenCopy-=8192;
        }
        divBy = buildOftenCopy/(int)(Math.pow(2, 12));
        if (divBy == 1)
        {
            manyTrade = true;
            buildOftenCopy-=4096;
        }
        divBy = buildOftenCopy/(int)(Math.pow(2, 11));
        if (divBy == 1)
        {
            manyWealth = true;
            buildOftenCopy-=2048;
        }
        divBy = buildOftenCopy/(int)(Math.pow(2, 10));
        if (divBy == 1)
        {
            manyScience = true;
            buildOftenCopy-=1024;
        }
        divBy = buildOftenCopy/(int)(Math.pow(2, 9));
        if (divBy == 1)
        {
            manyHappiness = true;
            buildOftenCopy-=512;
        }
        divBy = buildOftenCopy/(int)(Math.pow(2, 8));
        if (divBy == 1)
        {
            manyProduction = true;
            buildOftenCopy-=256;
        }
        divBy = buildOftenCopy/(int)(Math.pow(2, 7));
        if (divBy == 1)
        {
            manyGrowth = true;
            buildOftenCopy-=128;
        }
        divBy = buildOftenCopy/(int)(Math.pow(2, 6));
        if (divBy == 1)
        {
            manyAirUnits = true;
            buildOftenCopy-=64;
        }
        divBy = buildOftenCopy/(int)(Math.pow(2, 5));
        if (divBy == 1)
        {
            manyShips = true;
            buildOftenCopy-=32;
        }
        divBy = buildOftenCopy/(int)(Math.pow(2, 4));
        if (divBy == 1)
        {
            manyWorkers = true;
            buildOftenCopy-=16;
        }
        divBy = buildOftenCopy/(int)(Math.pow(2, 3));
        if (divBy == 1)
        {
            manySettlers = true;
            buildOftenCopy-=8;
        }
        divBy = buildOftenCopy/(int)(Math.pow(2, 2));
        if (divBy == 1)
        {
            manyArtillery = true;
            buildOftenCopy-=4;
        }
        divBy = buildOftenCopy/(int)(Math.pow(2, 1));
        if (divBy == 1)
        {
            manyDefensiveLandUnits = true;
            buildOftenCopy-=2;
        }
        divBy = buildOftenCopy/(int)(Math.pow(2, 0));
        if (divBy == 1)
        {
            manyOffensiveLandUnits = true;
            buildOftenCopy-=1;
        }
        int flavorCopy = flavors;
        for (int i = 0; i < numFlavours; i++)
        {
            flavours.add(false);
        }
        for (int i = numFlavours - 1; i > -1; i--)
        {
            //System.out.println("i : " + i);
            divBy = flavorCopy/(int)(Math.pow(2, i));
            if (divBy == 1)
            {
                flavours.set(i, true);
                flavorCopy-=Math.pow(2, i);
            }
        }
        this.numFlavors = numFlavours;
    }

    public String toEnglish()
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn =  "name: " + civilizationName + lineReturn;
        toReturn = toReturn + "civilopediaEntry: " + civilopediaEntry + lineReturn;
        toReturn = toReturn + "dataLength: " + dataLength + lineReturn;
        toReturn = toReturn + "numCityNames: " + numCityNames + lineReturn;
        for (int j = 0; j < cityName.size(); j++)
        {
            toReturn = toReturn + "  " + cityName.get(j) + lineReturn;
        }
        toReturn = toReturn + "numGreatLeaders: " + numGreatLeaders + lineReturn;
        for (int j = 0; j < numGreatLeaders; j++)
        {
            toReturn = toReturn + "  " + greatLeader.get(j) + lineReturn;
        }
        toReturn = toReturn + "leaderName: " + leaderName + lineReturn;
        toReturn = toReturn + "leaderTitle: " + leaderTitle + lineReturn;
        toReturn = toReturn + "adjective: " + adjective + lineReturn;
        toReturn = toReturn + "noun: " + noun + lineReturn;
        for (int j = 0; j < forwardFilename.size(); j++)
        {
            toReturn = toReturn + "forwardFilename for era " + j + ": " + forwardFilename.get(j) + lineReturn;
        }
        for (int j = 0; j < reverseFilename.size(); j++)
        {
            toReturn = toReturn + "reverseFilename for era " + j + ": " + reverseFilename.get(j) + lineReturn;
        }
        toReturn = toReturn + "cultureGroup: " + cultureGroup + lineReturn;
        toReturn = toReturn + "leaderGender: " + leaderGender + lineReturn;
        toReturn = toReturn + "civilizationGender: " + civilizationGender + lineReturn;
        toReturn = toReturn + "aggressionLevel: " + aggressionLevel + lineReturn;
        toReturn = toReturn + "uniqueCivilizationCounter: " + uniqueCivilizationCounter + lineReturn;
        toReturn = toReturn + "shunnedGovernment: " + shunnedGovernment + lineReturn;
        toReturn = toReturn + "favoriteGovernment: " + favoriteGovernment + lineReturn;
        toReturn = toReturn + "defaultColor: " + defaultColor + lineReturn;
        toReturn = toReturn + "uniqueColor: " + uniqueColor + lineReturn;
        toReturn = toReturn + "freeTech1Index: " + freeTech1Index + lineReturn;
        toReturn = toReturn + "freeTech2Index: " + freeTech2Index + lineReturn;
        toReturn = toReturn + "freeTech3Index: " + freeTech3Index + lineReturn;
        toReturn = toReturn + "freeTech4Index: " + freeTech4Index + lineReturn;
        toReturn = toReturn + "civilization traits: " + bonuses + lineReturn;
        toReturn = toReturn + "  militaristic: " + militaristic + lineReturn;
        toReturn = toReturn + "  commercial: " + commercial + lineReturn;
        toReturn = toReturn + "  expansionist: " + expansionist + lineReturn;
        toReturn = toReturn + "  scientific: " + scientific + lineReturn;
        toReturn = toReturn + "  religious: " + religious + lineReturn;
        toReturn = toReturn + "  industrious: " + industrious + lineReturn;
        toReturn = toReturn + "  agricultural: " + agricultural + lineReturn;
        toReturn = toReturn + "  seafaring: " + seafaring + lineReturn;
        toReturn = toReturn + "governor settings: " + governorSettings + lineReturn;
        toReturn = toReturn + "  manageCitizens: " + manageCitizens + lineReturn;
        toReturn = toReturn + "  emphasizeFood: " + emphasizeFood + lineReturn;
        toReturn = toReturn + "  emphasizeShields: " + emphasizeShields + lineReturn;
        toReturn = toReturn + "  emphasizeTrade: " + emphasizeTrade + lineReturn;
        toReturn = toReturn + "  manageProduction: " + manageProduction + lineReturn;
        toReturn = toReturn + "  noWonders: " + noWonders + lineReturn;
        toReturn = toReturn + "  noSmallWonders: " + noSmallWonders + lineReturn;
        toReturn = toReturn + "never build: " + buildNever + lineReturn;
        toReturn = toReturn + "  noOffensiveLandUnits: " + noOffensiveLandUnits + lineReturn;
        toReturn = toReturn + "  noDefensiveLandUnits: " + noDefensiveLandUnits + lineReturn;
        toReturn = toReturn + "  noArtillery: " + noArtillery + lineReturn;
        toReturn = toReturn + "  noSettlers: " + noSettlers + lineReturn;
        toReturn = toReturn + "  noWorkers: " + noWorkers + lineReturn;
        toReturn = toReturn + "  noShips: " + noShips + lineReturn;
        toReturn = toReturn + "  noAirUnits: " + noAirUnits + lineReturn;
        toReturn = toReturn + "  noGrowth: " + noGrowth + lineReturn;
        toReturn = toReturn + "  noProduction: " + noProduction + lineReturn;
        toReturn = toReturn + "  noHappiness: " + noHappiness + lineReturn;
        toReturn = toReturn + "  noScience: " + noScience + lineReturn;
        toReturn = toReturn + "  noWealth: " + noWealth + lineReturn;
        toReturn = toReturn + "  noTrade: " + noTrade + lineReturn;
        toReturn = toReturn + "  noExploration: " + noExploration + lineReturn;
        toReturn = toReturn + "  noCulture: " + noCulture + lineReturn;
        toReturn = toReturn + "build often: " + buildOften + lineReturn;
        toReturn = toReturn + "  manyOffensiveLandUnits: " + manyOffensiveLandUnits + lineReturn;
        toReturn = toReturn + "  manyDefensiveLandUnits: " + manyDefensiveLandUnits + lineReturn;
        toReturn = toReturn + "  manyArtillery: " + manyArtillery + lineReturn;
        toReturn = toReturn + "  manySettlers: " + manySettlers + lineReturn;
        toReturn = toReturn + "  manyWorkers: " + manyWorkers + lineReturn;
        toReturn = toReturn + "  manyShips: " + manyShips + lineReturn;
        toReturn = toReturn + "  manyAirUnits: " + manyAirUnits + lineReturn;
        toReturn = toReturn + "  manyGrowth: " + manyGrowth + lineReturn;
        toReturn = toReturn + "  manyProduction: " + manyProduction + lineReturn;
        toReturn = toReturn + "  manyHappiness: " + manyHappiness + lineReturn;
        toReturn = toReturn + "  manyScience: " + manyScience + lineReturn;
        toReturn = toReturn + "  manyWealth: " + manyWealth + lineReturn;
        toReturn = toReturn + "  manyTrade: " + manyTrade + lineReturn;
        toReturn = toReturn + "  manyExploration: " + manyExploration + lineReturn;
        toReturn = toReturn + "  manyCulture: " + manyCulture + lineReturn;
        toReturn = toReturn + "plurality: " + plurality + lineReturn;
        toReturn = toReturn + "kingUnit: " + kingUnitInt + lineReturn;
        toReturn = toReturn + "flavors: " + flavors + lineReturn;
        for (int i = 0; i < numFlavors; i++)
        {
            toReturn = toReturn + "  flavor " + (i + 1) + ": " + flavours.get(i) + lineReturn;
        }
        toReturn = toReturn + "questionMark: " + questionMark + lineReturn;
        toReturn = toReturn + "diplomacyTextIndex: " + diplomacyTextIndex + lineReturn;
        toReturn = toReturn + "numScientificLeaders: " + numScientificLeaders + lineReturn;
        for (int j = 0; j < numScientificLeaders; j++)
        {
            toReturn = toReturn + "  " + scientificLeader.get(j) + lineReturn;
        }
        toReturn = toReturn + lineReturn;
        return toReturn;

    }

    public String compareTo(BIQSection section, String separator)
    {
        if (!(section instanceof RACE))
            return null;
        RACE two = (RACE)section;
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "name: " + civilizationName + lineReturn;
        if (!(dataLength == two.getDataLength()))
        {
                toReturn = toReturn + "DataLength: " + dataLength + separator + two.getDataLength() + lineReturn;
        }
        if (!(numCityNames == two.getNumCityNames()))
        {
                toReturn = toReturn + "NumCityNames: " + numCityNames + separator + two.getNumCityNames() + lineReturn;
        }
        if (!(numGreatLeaders == two.getNumGreatLeaders()))
        {
                toReturn = toReturn + "NumGreatLeaders: " + numGreatLeaders + separator + two.getNumGreatLeaders() + lineReturn;
        }
        if (leaderName.compareTo(two.getLeaderName()) != 0)
        {
                toReturn = toReturn + "LeaderName: " + leaderName + separator + two.getLeaderName() + lineReturn;
        }
        if (leaderTitle.compareTo(two.getLeaderTitle()) != 0)
        {
                toReturn = toReturn + "LeaderTitle: " + leaderTitle + separator + two.getLeaderTitle() + lineReturn;
        }
        if (civilopediaEntry.compareTo(two.getCivilopediaEntry()) != 0)
        {
                toReturn = toReturn + "CivilopediaEntry: " + civilopediaEntry + separator + two.getCivilopediaEntry() + lineReturn;
        }
        if (adjective.compareTo(two.getAdjective()) != 0)
        {
                toReturn = toReturn + "Adjective: " + adjective + separator + two.getAdjective() + lineReturn;
        }
        if (noun.compareTo(two.getNoun()) != 0)
        {
                toReturn = toReturn + "Noun: " + noun + separator + two.getNoun() + lineReturn;
        }
        if (!(cultureGroup == two.getCultureGroup()))
        {
                toReturn = toReturn + "CultureGroup: " + cultureGroup + separator + two.getCultureGroup() + lineReturn;
        }
        if (!(leaderGender == two.getLeaderGender()))
        {
                toReturn = toReturn + "LeaderGender: " + leaderGender + separator + two.getLeaderGender() + lineReturn;
        }
        if (!(civilizationGender == two.getCivilizationGender()))
        {
                toReturn = toReturn + "CivilizationGender: " + civilizationGender + separator + two.getCivilizationGender() + lineReturn;
        }
        if (!(aggressionLevel == two.getAggressionLevel()))
        {
                toReturn = toReturn + "AggressionLevel: " + aggressionLevel + separator + two.getAggressionLevel() + lineReturn;
        }
        if (!(uniqueCivilizationCounter == two.getUniqueCivilizationCounter()))
        {
                toReturn = toReturn + "UniqueCivilizationCounter: " + uniqueCivilizationCounter + separator + two.getUniqueCivilizationCounter() + lineReturn;
        }
        if (!(shunnedGovernment == two.getShunnedGovernment()))
        {
                toReturn = toReturn + "ShunnedGovernment: " + shunnedGovernment + separator + two.getShunnedGovernment() + lineReturn;
        }
        if (!(favoriteGovernment == two.getFavoriteGovernment()))
        {
                toReturn = toReturn + "FavoriteGovernment: " + favoriteGovernment + separator + two.getFavoriteGovernment() + lineReturn;
        }
        if (!(defaultColor == two.getDefaultColor()))
        {
                toReturn = toReturn + "DefaultColor: " + defaultColor + separator + two.getDefaultColor() + lineReturn;
        }
        if (!(uniqueColor == two.getUniqueColor()))
        {
                toReturn = toReturn + "UniqueColor: " + uniqueColor + separator + two.getUniqueColor() + lineReturn;
        }
        if (!(freeTech1Index == two.getFreeTech1Index()))
        {
                toReturn = toReturn + "FreeTech1Index: " + freeTech1Index + separator + two.getFreeTech1Index() + lineReturn;
        }
        if (!(freeTech2Index == two.getFreeTech2Index()))
        {
                toReturn = toReturn + "FreeTech2Index: " + freeTech2Index + separator + two.getFreeTech2Index() + lineReturn;
        }
        if (!(freeTech3Index == two.getFreeTech3Index()))
        {
                toReturn = toReturn + "FreeTech3Index: " + freeTech3Index + separator + two.getFreeTech3Index() + lineReturn;
        }
        if (!(freeTech4Index == two.getFreeTech4Index()))
        {
                toReturn = toReturn + "FreeTech4Index: " + freeTech4Index + separator + two.getFreeTech4Index() + lineReturn;
        }
        if (!(bonuses == two.getBonuses()))
        {
                toReturn = toReturn + "Civilization traits: " + bonuses + separator + two.getBonuses() + lineReturn;
        }
        if (!(militaristic == two.getMilitaristic()))
        {
                toReturn = toReturn + "  Militaristic: " + militaristic + separator + two.getMilitaristic() + lineReturn;
        }
        if (!(commercial == two.getCommercial()))
        {
                toReturn = toReturn + "  Commercial: " + commercial + separator + two.getCommercial() + lineReturn;
        }
        if (!(expansionist == two.getExpansionist()))
        {
                toReturn = toReturn + "  Expansionist: " + expansionist + separator + two.getExpansionist() + lineReturn;
        }
        if (!(scientific == two.getScientific()))
        {
                toReturn = toReturn + "  Scientific: " + scientific + separator + two.getScientific() + lineReturn;
        }
        if (!(religious == two.getReligious()))
        {
                toReturn = toReturn + "  Religious: " + religious + separator + two.getReligious() + lineReturn;
        }
        if (!(industrious == two.getIndustrious()))
        {
                toReturn = toReturn + "  Industrious: " + industrious + separator + two.getIndustrious() + lineReturn;
        }
        if (!(agricultural == two.getAgricultural()))
        {
                toReturn = toReturn + "  Agricultural: " + agricultural + separator + two.getAgricultural() + lineReturn;
        }
        if (!(seafaring == two.getSeafaring()))
        {
                toReturn = toReturn + "  Seafaring: " + seafaring + separator + two.getSeafaring() + lineReturn;
        }
        if (!(governorSettings == two.getGovernorSettings()))
        {
                toReturn = toReturn + "GovernorSettings: " + governorSettings + separator + two.getGovernorSettings() + lineReturn;
        }
        if (!(manageCitizens == two.getManageCitizens()))
        {
                toReturn = toReturn + "  ManageCitizens: " + manageCitizens + separator + two.getManageCitizens() + lineReturn;
        }
        if (!(emphasizeFood == two.getEmphasizeFood()))
        {
                toReturn = toReturn + "  EmphasizeFood: " + emphasizeFood + separator + two.getEmphasizeFood() + lineReturn;
        }
        if (!(emphasizeShields == two.getEmphasizeShields()))
        {
                toReturn = toReturn + "  EmphasizeShields: " + emphasizeShields + separator + two.getEmphasizeShields() + lineReturn;
        }
        if (!(emphasizeTrade == two.getEmphasizeTrade()))
        {
                toReturn = toReturn + "  EmphasizeTrade: " + emphasizeTrade + separator + two.getEmphasizeTrade() + lineReturn;
        }
        if (!(manageProduction == two.getManageProduction()))
        {
                toReturn = toReturn + "  ManageProduction: " + manageProduction + separator + two.getManageProduction() + lineReturn;
        }
        if (!(noWonders == two.getNoWonders()))
        {
                toReturn = toReturn +   "NoWonders: " + noWonders + separator + two.getNoWonders() + lineReturn;
        }
        if (!(noSmallWonders == two.getNoSmallWonders()))
        {
                toReturn = toReturn + "  NoSmallWonders: " + noSmallWonders + separator + two.getNoSmallWonders() + lineReturn;
        }
        if (!(buildNever == two.getBuildNever()))
        {
                toReturn = toReturn + "BuildNever: " + buildNever + separator + two.getBuildNever() + lineReturn;
        }
        if (!(noOffensiveLandUnits == two.getNoOffensiveLandUnits()))
        {
                toReturn = toReturn + "  NoOffensiveLandUnits: " + noOffensiveLandUnits + separator + two.getNoOffensiveLandUnits() + lineReturn;
        }
        if (!(noDefensiveLandUnits == two.getNoDefensiveLandUnits()))
        {
                toReturn = toReturn + "  NoDefensiveLandUnits: " + noDefensiveLandUnits + separator + two.getNoDefensiveLandUnits() + lineReturn;
        }
        if (!(noArtillery == two.getNoArtillery()))
        {
                toReturn = toReturn + "  NoArtillery: " + noArtillery + separator + two.getNoArtillery() + lineReturn;
        }
        if (!(noSettlers == two.getNoSettlers()))
        {
                toReturn = toReturn + "  NoSettlers: " + noSettlers + separator + two.getNoSettlers() + lineReturn;
        }
        if (!(noWorkers == two.getNoWorkers()))
        {
                toReturn = toReturn + "  NoWorkers: " + noWorkers + separator + two.getNoWorkers() + lineReturn;
        }
        if (!(noShips == two.getNoShips()))
        {
                toReturn = toReturn + "  NoShips: " + noShips + separator + two.getNoShips() + lineReturn;
        }
        if (!(noAirUnits == two.getNoAirUnits()))
        {
                toReturn = toReturn + "  NoAirUnits: " + noAirUnits + separator + two.getNoAirUnits() + lineReturn;
        }
        if (!(noGrowth == two.getNoGrowth()))
        {
                toReturn = toReturn + "  NoGrowth: " + noGrowth + separator + two.getNoGrowth() + lineReturn;
        }
        if (!(noProduction == two.getNoProduction()))
        {
                toReturn = toReturn + "  NoProduction: " + noProduction + separator + two.getNoProduction() + lineReturn;
        }
        if (!(noHappiness == two.getNoHappiness()))
        {
                toReturn = toReturn + "  NoHappiness: " + noHappiness + separator + two.getNoHappiness() + lineReturn;
        }
        if (!(noScience == two.getNoScience()))
        {
                toReturn = toReturn + "  NoScience: " + noScience + separator + two.getNoScience() + lineReturn;
        }
        if (!(noWealth == two.getNoWealth()))
        {
                toReturn = toReturn + "  NoWealth: " + noWealth + separator + two.getNoWealth() + lineReturn;
        }
        if (!(noTrade == two.getNoTrade()))
        {
                toReturn = toReturn + "  NoTrade: " + noTrade + separator + two.getNoTrade() + lineReturn;
        }
        if (!(noExploration == two.getNoExploration()))
        {
                toReturn = toReturn + "  NoExploration: " + noExploration + separator + two.getNoExploration() + lineReturn;
        }
        if (!(noCulture == two.getNoCulture()))
        {
                toReturn = toReturn + "  NoCulture: " + noCulture + separator + two.getNoCulture() + lineReturn;
        }
        if (!(buildOften == two.getBuildOften()))
        {
                toReturn = toReturn + "BuildOften: " + buildOften + separator + two.getBuildOften() + lineReturn;
        }
        if (!(manyOffensiveLandUnits == two.getManyOffensiveLandUnits()))
        {
                toReturn = toReturn + "  ManyOffensiveLandUnits: " + manyOffensiveLandUnits + separator + two.getManyOffensiveLandUnits() + lineReturn;
        }
        if (!(manyDefensiveLandUnits == two.getManyDefensiveLandUnits()))
        {
                toReturn = toReturn + "  ManyDefensiveLandUnits: " + manyDefensiveLandUnits + separator + two.getManyDefensiveLandUnits() + lineReturn;
        }
        if (!(manyArtillery == two.getManyArtillery()))
        {
                toReturn = toReturn + "  ManyArtillery: " + manyArtillery + separator + two.getManyArtillery() + lineReturn;
        }
        if (!(manySettlers == two.getManySettlers()))
        {
                toReturn = toReturn + "  ManySettlers: " + manySettlers + separator + two.getManySettlers() + lineReturn;
        }
        if (!(manyWorkers == two.getManyWorkers()))
        {
                toReturn = toReturn + "  ManyWorkers: " + manyWorkers + separator + two.getManyWorkers() + lineReturn;
        }
        if (!(manyShips == two.getManyShips()))
        {
                toReturn = toReturn + "  ManyShips: " + manyShips + separator + two.getManyShips() + lineReturn;
        }
        if (!(manyAirUnits == two.getManyAirUnits()))
        {
                toReturn = toReturn + "  ManyAirUnits: " + manyAirUnits + separator + two.getManyAirUnits() + lineReturn;
        }
        if (!(manyGrowth == two.getManyGrowth()))
        {
                toReturn = toReturn + "  ManyGrowth: " + manyGrowth + separator + two.getManyGrowth() + lineReturn;
        }
        if (!(manyProduction == two.getManyProduction()))
        {
                toReturn = toReturn + "  ManyProduction: " + manyProduction + separator + two.getManyProduction() + lineReturn;
        }
        if (!(manyHappiness == two.getManyHappiness()))
        {
                toReturn = toReturn + "  ManyHappiness: " + manyHappiness + separator + two.getManyHappiness() + lineReturn;
        }
        if (!(manyScience == two.getManyScience()))
        {
                toReturn = toReturn + "  ManyScience: " + manyScience + separator + two.getManyScience() + lineReturn;
        }
        if (!(manyWealth == two.getManyWealth()))
        {
                toReturn = toReturn + "  ManyWealth: " + manyWealth + separator + two.getManyWealth() + lineReturn;
        }
        if (!(manyTrade == two.getManyTrade()))
        {
                toReturn = toReturn + "  ManyTrade: " + manyTrade + separator + two.getManyTrade() + lineReturn;
        }
        if (!(manyExploration == two.getManyExploration()))
        {
                toReturn = toReturn + "  ManyExploration: " + manyExploration + separator + two.getManyExploration() + lineReturn;
        }
        if (!(manyCulture == two.getManyCulture()))
        {
                toReturn = toReturn + "  ManyCulture: " + manyCulture + separator + two.getManyCulture() + lineReturn;
        }
        if (!(plurality == two.getPlurality()))
        {
                toReturn = toReturn + "Plurality: " + plurality + separator + two.getPlurality() + lineReturn;
        }
        if (!(kingUnitInt == two.getKingUnit()))
        {
                toReturn = toReturn + "KingUnit: " + kingUnitInt + separator + two.getKingUnit() + lineReturn;
        }
        if (!(flavors == two.getFlavors()))
        {
                toReturn = toReturn + "Flavors: " + flavors + separator + two.getFlavors() + lineReturn;
        }
        else  //see if a flavor is checked for a civilization in only one file
        {
            for (int i = 0; i < numFlavors; i++)
            {
                if (!(flavours.get(i) == two.flavours.get(i)))
                {
                    toReturn = toReturn + "  flavor " + i + ": " + flavours.get(i) + separator + two.flavours.get(i) + lineReturn;
                }
            }
        }
        if (!(questionMark == two.getQuestionMark()))
        {
                toReturn = toReturn + "QuestionMark: " + questionMark + separator + two.getQuestionMark() + lineReturn;
        }
        if (!(diplomacyTextIndex == two.getDiplomacyTextIndex()))
        {
                toReturn = toReturn + "DiplomacyTextIndex: " + diplomacyTextIndex + separator + two.getDiplomacyTextIndex() + lineReturn;
        }
        if (!(numScientificLeaders == two.getNumScientificLeaders()))
        {
                toReturn = toReturn + "numScientificLeaders: " + numScientificLeaders + separator + two.getNumScientificLeaders() + lineReturn;
        }
        if (toReturn.equals("name: " + civilizationName + lineReturn))
        {
            toReturn = "";
        }
        return toReturn;
    }

    public Object getProperty(String string) throws UnsupportedOperationException
    {
        if (string.equals("Name") || string.equals("CivilizationName"))
            return this.civilizationName;
        throw new UnsupportedOperationException();
    }
}
