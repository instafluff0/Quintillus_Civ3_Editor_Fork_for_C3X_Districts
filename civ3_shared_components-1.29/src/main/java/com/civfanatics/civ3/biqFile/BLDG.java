package com.civfanatics.civ3.biqFile;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Other sections that know about BLDG: RULE, MAP
 * @author Quintillus
 */
import java.util.ArrayList;
import com.civfanatics.civ3.biqFile.util.Utils;
public class BLDG extends BIQSection {
    //ought to create program that creates program for setter methods

    private int dataLength = 268;     //should be ?268?
    private String description = ""; //64 bytes
    private String name = "";        //32 bytes
    private String civilopediaEntry = "";    //32 bytes
    private int doublesHappiness;   //integer corresponds to the BLDG entry it doubles
    private int gainInEveryCity;
    private int gainOnContinent;
    private int reqImprovement;
    private int cost;
    private int culture;
    private int bombardDefence;
    private int navalBombardDefence;
    private int defenceBonus;
    private int navalDefenceBonus;
    private int maintenanceCost;
    private int happyAll;
    private int happy;
    private int unhappyAll;
    private int unhappy;
    private int numReqBuildings;
    private int airPower;
    private int navalPower;
    private int pollution;
    private int production;
    private int reqGovernment;
    private int spaceshipPart = -1;
    private int reqAdvanceInt;
    private TECH reqAdvance;
    private int obsoleteByInt;
    private TECH obsoleteBy;
    private int reqResource1;
    private int reqResource2;
    private int improvements;   //binary
    private int otherChar;      //binary
    private int smallWonderCharacteristics;    //binary
    private int wonderCharacteristics;         //binary
    private int armiesRequired;
    private int flavors;
    private int questionMark = 4;
    private int unitProducedInt;   //PRTO reference
    private PRTO unitProduced;
    private int unitFrequency;
    //interpretations of binary
    private boolean centerOfEmpire;
    private boolean veteranUnits;
    private boolean increasedResearch;
    private boolean increasedLuxuries;
    private boolean increasedTaxes;
    private boolean removePopPollution;
    private boolean reduceBldgPollution;
    private boolean resistantToBribery; //2^7
    //next byte
    private boolean reducesCorruption;
    private boolean doublesCityGrowthRate;
    private boolean increasesLuxuryTrade;
    private boolean allowCityLevel2;
    private boolean allowCityLevel3;
    private boolean replacesOtherWithThisTag;
    private boolean mustBeNearWater;
    private boolean mustBeNearRiver;    //2^15
    //third byte
    private boolean mayExplodeOrMeltdown;
    private boolean veteranSeaUnits;
    private boolean veteranAirUnits;
    private boolean capitalization;
    private boolean allowWaterTrade;
    private boolean allowAirTrade;
    private boolean reducesWarWeariness;
    private boolean increasesShieldsInWater;    //2^23
    //final byte
    private boolean increasesFoodInWater;       //2^24
    private boolean increasesTradeInWater;
    private boolean charmBarrier;
    private boolean stealthAttackBarrier;       //2^27
    private boolean actsAsGeneralTelepad;
    private boolean doublesSacrifice;
    private boolean canBuildUnits;              //2^30

    //other characteristics
    private boolean coastalInstallation;
    private boolean militaryInstallation;
    private boolean wonder;
    private boolean smallWonder;
    private boolean continentalMoodEffects;
    private boolean researchInstallation;
    private boolean tradeInstallation;
    private boolean explorationInstallation;
    private boolean placeOfWorship;
    private boolean constructionInstallation;
    private boolean agriculturalInstallation;
    private boolean seafaringInstallation;

    //small wonder characteristics
    private boolean increasesChanceOfLeaderAppearance;
    private boolean buildArmiesWithoutLeader;
    private boolean buildLargerArmies;
    private boolean treasuryEarnsInterest;
    private boolean buildSpaceshipParts;
    private boolean forbiddenPalace;
    private boolean decreasesSuccessOfMissiles;
    private boolean allowSpyMissions;
    private boolean allowsHealingInEnemyTerritory;
    private boolean goodsMustBeInCityRadius;
    private boolean requiresVictoriousArmy;
    private boolean requiresEliteShip;

    //wonder characteristics
    private boolean safeSeaTravel;
    private boolean gainAnyTechsKnownByTwoCivs;
    private boolean doubleCombatVsBarbarians;
    private boolean increasedShipMovement;
    private boolean doublesResearchOutput;
    private boolean increasedTrade;
    private boolean cheaperUpgrades;
    private boolean paysTradeMaintenance;
    private boolean allowsNuclearWeapons;
    private boolean doubleCityGrowth;
    private boolean twoFreeAdvances;
    private boolean empireReducesWarWeariness;
    private boolean doubleCityDefences;
    private boolean allowDiplomaticVictory;
    private boolean plusTwoShipMovement;
    private boolean questionMarkWonderTrait;
    private boolean increasedArmyValue;
    private boolean touristAttraction;

    //flavors
    private ArrayList<Boolean>flavours;
    private int numFlavors;
    
    private int bldgIndex;

    //this is the constructor that will be used when loading a game
    public BLDG(IO baselink)
    {
        super(baselink);
        bldgIndex = baselink.buildings.size();
        //set defaults to proper values
        this.reqImprovement = -1;   //doesn't require a building prereq
        this.reqGovernment = -1;
        this.reqAdvanceInt = -1;
        this.reqAdvance = null;
        this.reqResource1 = -1;
        this.reqResource2 = -1;
        this.doublesHappiness = -1;
        this.gainOnContinent = -1;
        this.gainInEveryCity = -1;
        this.unitProducedInt = -1;
        this.obsoleteByInt = -1;
        this.unitFrequency = 1;
        //System.out.println(2^15);
        //System.out.println(Math.pow(2, 15));
        flavours = new ArrayList<Boolean>();
    }
    //This is the constructor that will be used when the human adds a bldg in
    //the editor.
    public BLDG(String name, int numFlavors, IO baseLink)
    {
        super(baseLink);
        this.bldgIndex = baseLink.buildings.size();
        this.name = name;
        //set defaults to proper values
        this.reqImprovement = -1;   //doesn't require a building prereq
        this.reqGovernment = -1;
        this.reqAdvanceInt = -1;
        this.reqAdvance = null;
        this.reqResource1 = -1;
        this.reqResource2 = -1;
        this.doublesHappiness = -1;
        this.gainOnContinent = -1;
        this.gainInEveryCity = -1;
        this.unitProducedInt = -1;
        this.obsoleteByInt = -1;
        this.unitFrequency = 1;
        flavours = new ArrayList<Boolean>(numFlavors);
    }
    public void trim()
    {
        //description = description.trim();
        name = name.trim();
        civilopediaEntry = civilopediaEntry.trim();
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setCanBuildUnits(boolean value)
    {
        this.canBuildUnits = value;
    }
    public void setStealthAttackBarrier(boolean value)
    {
        this.stealthAttackBarrier = value;
    }
    public void setDoublesSacrifice(boolean value)
    {
        this.doublesSacrifice = value;
    }
    public void setCharmBarrier(boolean value)
    {
        this.charmBarrier = value;
    }
    public void setActsAsGeneralTelepad(boolean value)
    {
        this.actsAsGeneralTelepad = value;
    }

    public boolean getCanBuildUnits()
    {
        return this.canBuildUnits;
    }
    public boolean getStealthAttackBarrier()
    {
        return this.stealthAttackBarrier;
    }
    public boolean getDoublesSacrifice()
    {
        return this.doublesSacrifice;
    }
    public boolean getCharmBarrier()
    {
        return this.charmBarrier;
    }
    public boolean getActsAsGeneralTelepad()
    {
        return this.actsAsGeneralTelepad;
    }

    public void setCivilopediaEntry(String civilopediaEntry)
    {
        this.civilopediaEntry = civilopediaEntry;
    }

    public void setDoublesHappiness(int doublesHappiness)
    {
        this.doublesHappiness = doublesHappiness;
    }

    public void setGainInEveryCity(int gainInEveryCity)
    {
        this.gainInEveryCity = gainInEveryCity;
    }

    public void setGainOnContinent(int gainOnContinent)
    {
        this.gainOnContinent = gainOnContinent;
    }

    public void setIncreasesTradeInWater(boolean value)
    {
        this.increasesTradeInWater = value;
    }

    public boolean getIncreasesTradeInWater()
    {
        return increasesTradeInWater;
    }

    public void setReqImprovement(int reqImprovement)
    {
        this.reqImprovement = reqImprovement;
    }

    public void setCost(int cost)
    {
        this.cost = cost;
    }

    public void setCulture(int culture)
    {
        this.culture = culture;
    }

    public void setBombardDefence(int bombardDefence)
    {
        this.bombardDefence = bombardDefence;
    }

    public void setNavalBombardDefence(int navalBombardDefence)
    {
        this.navalBombardDefence = navalBombardDefence;
    }

    public void setDefenceBonus(int defenceBonus)
    {
        this.defenceBonus = defenceBonus;
    }

    public void setNavalDefenceBonus(int navalDefenceBonus)
    {
        this.navalDefenceBonus = navalDefenceBonus;
    }

    public void setMaintenanceCost(int maintenanceCost)
    {
        this.maintenanceCost = maintenanceCost;
    }

    public void setHappyAll(int happyAll)
    {
        this.happyAll = happyAll;
    }

    public void setHappy(int happy)
    {
        this.happy = happy;
    }

    public void setUnhappyAll(int unhappyAll)
    {
        this.unhappyAll = unhappyAll;
    }

    public void setUnhappy(int unhappy)
    {
        this.unhappy = unhappy;
    }

    public void setNumReqBuildings(int numReqBuildings)
    {
        this.numReqBuildings = numReqBuildings;
    }

    public void setAirPower(int airPower)
    {
        this.airPower = airPower;
    }

    public void setNavalPower(int navalPower)
    {
        this.navalPower = navalPower;
    }

    public void setPollution(int pollution)
    {
        this.pollution = pollution;
    }

    public void setProduction(int production)
    {
        this.production = production;
    }

    public void setReqGovernment(int reqGovernment)
    {
        this.reqGovernment = reqGovernment;
    }

    public void setSpaceshipPart(int spaceshipPart)
    {
        this.spaceshipPart = spaceshipPart;
    }

    public void setReqAdvance(int reqAdvanceInt)
    {
        this.reqAdvanceInt = reqAdvanceInt;
        if (baseLink.technology != null && reqAdvanceInt != -1 && baseLink.technology.size() > reqAdvanceInt) {
            this.reqAdvance = baseLink.technology.get(reqAdvanceInt);
        }
    }

    public void setObsoleteBy(int obsoleteBy)
    {
        this.obsoleteByInt = obsoleteBy;
        if (baseLink.technology != null && obsoleteByInt != -1 && baseLink.technology.size() > obsoleteByInt) {
            this.obsoleteBy = baseLink.technology.get(obsoleteByInt);
        }
    }

    public void setReqResource1(int reqResource1)
    {
        this.reqResource1 = reqResource1;
    }

    public void setReqResource2(int reqResource2)
    {
        this.reqResource2 = reqResource2;
    }

    public void setImprovements(int improvements)
    {
        this.improvements = improvements;
    }

    public void setOtherChar(int otherChar)
    {
        this.otherChar = otherChar;
    }

    public void setSmallWonderCharacteristics(int smallWonderCharacteristics)
    {
        this.smallWonderCharacteristics = smallWonderCharacteristics;
    }

    public void setWonderCharacteristics(int wonderCharacteristics)
    {
        this.wonderCharacteristics = wonderCharacteristics;
    }

    public void setArmiesRequired(int armiesRequired)
    {
        this.armiesRequired = armiesRequired;
    }

    public void setFlavors(int flavors)
    {
        this.flavors = flavors;
    }

    public void setQuestionMark(int questionMark)
    {
        this.questionMark = questionMark;
    }

    public void setUnitProduced(int unitProducedInt)
    {
        this.unitProducedInt = unitProducedInt;
        if (baseLink.unit != null && unitProducedInt != -1 && baseLink.unit.size() > unitProducedInt) {
            this.unitProduced = baseLink.unit.get(unitProducedInt);
        }
    }

    public void setUnitFrequency(int unitFrequency)
    {
        this.unitFrequency = unitFrequency;
    }

    public void setCenterOfEmpire(boolean centerOfEmpire)
    {
        this.centerOfEmpire = centerOfEmpire;
    }

    public void setVeteranUnits(boolean veteranUnits)
    {
        this.veteranUnits = veteranUnits;
    }

    public void setIncreasedResearch(boolean increasedResearch)
    {
        this.increasedResearch = increasedResearch;
    }

    public void setIncreasedLuxuries(boolean increasedLuxuries)
    {
        this.increasedLuxuries = increasedLuxuries;
    }

    public void setIncreasedTaxes(boolean increasedTaxes)
    {
        this.increasedTaxes = increasedTaxes;
    }

    public void setRemovePopPollution(boolean removePopPollution)
    {
        this.removePopPollution = removePopPollution;
    }

    public void setReduceBldgPollution(boolean reduceBldgPollution)
    {
        this.reduceBldgPollution = reduceBldgPollution;
    }

    public void setResistantToBribery(boolean resistantToBribery)
    {
        this.resistantToBribery = resistantToBribery;
    }

    public void setReducesCorruption(boolean reducesCorruption)
    {
        this.reducesCorruption = reducesCorruption;
    }

    public void setDoublesCityGrowthRate(boolean doublesCityGrowthRate)
    {
        this.doublesCityGrowthRate = doublesCityGrowthRate;
    }

    public void setIncreasesLuxuryTrade(boolean increasesLuxuryTrade)
    {
        this.increasesLuxuryTrade = increasesLuxuryTrade;
    }

    public void setAllowCityLevel2(boolean allowCityLevel2)
    {
        this.allowCityLevel2 = allowCityLevel2;
    }

    public void setAllowCityLevel3(boolean allowCityLevel3)
    {
        this.allowCityLevel3 = allowCityLevel3;
    }

    public void setReplacesOtherWithThisTag(boolean replacesOtherWithThisTag)
    {
        this.replacesOtherWithThisTag = replacesOtherWithThisTag;
    }

    public void setMustBeNearWater(boolean mustBeNearWater)
    {
        this.mustBeNearWater = mustBeNearWater;
    }

    public void setMustBeNearRiver(boolean mustBeNearRiver)
    {
        this.mustBeNearRiver = mustBeNearRiver;
    }

    public void setMayExplodeOrMeltdown(boolean mayExplodeOrMeltdown)
    {
        this.mayExplodeOrMeltdown = mayExplodeOrMeltdown;
    }

    public void setVeteranSeaUnits(boolean veteranSeaUnits)
    {
        this.veteranSeaUnits = veteranSeaUnits;
    }

    public void setVeteranAirUnits(boolean veteranAirUnits)
    {
        this.veteranAirUnits = veteranAirUnits;
    }

    public void setCapitalization(boolean capitalization)
    {
        this.capitalization = capitalization;
    }

    public void setAllowWaterTrade(boolean allowWaterTrade)
    {
        this.allowWaterTrade = allowWaterTrade;
    }

    public void setAllowAirTrade(boolean allowAirTrade)
    {
        this.allowAirTrade = allowAirTrade;
    }

    public void setReducesWarWeariness(boolean reducesWarWeariness)
    {
        this.reducesWarWeariness = reducesWarWeariness;
    }

    public void setIncreasesShieldsInWater(boolean increasesShieldsInWater)
    {
        this.increasesShieldsInWater = increasesShieldsInWater;
    }

    public void setIncreasesFoodInWater(boolean increasesFoodInWater)
    {
        this.increasesFoodInWater = increasesFoodInWater;
    }

    public void setCoastalInstallation(boolean coastalInstallation)
    {
        this.coastalInstallation = coastalInstallation;
    }

    public void setMilitaryInstallation(boolean militaryInstallation)
    {
        this.militaryInstallation = militaryInstallation;
    }

    public void setWonder(boolean wonder)
    {
        this.wonder = wonder;
    }

    public void setSmallWonder(boolean smallWonder)
    {
        this.smallWonder = smallWonder;
    }

    public void setContinentalMoodEffects(boolean continentalMoodEffects)
    {
        this.continentalMoodEffects = continentalMoodEffects;
    }

    public void setResearchInstallation(boolean researchInstallation)
    {
        this.researchInstallation = researchInstallation;
    }

    public void setTradeInstallation(boolean tradeInstallation)
    {
        this.tradeInstallation = tradeInstallation;
    }

    public void setExplorationInstallation(boolean explorationInstallation)
    {
        this.explorationInstallation = explorationInstallation;
    }

    public void setPlaceOfWorship(boolean placeOfWorship)
    {
        this.placeOfWorship = placeOfWorship;
    }

    public void setConstructionInstallation(boolean constructionInstallation)
    {
        this.constructionInstallation = constructionInstallation;
    }

    public void setIncreasesChanceOfLeaderAppearance(boolean increasesChanceOfLeaderAppearance)
    {
        this.increasesChanceOfLeaderAppearance = increasesChanceOfLeaderAppearance;
    }

    public void setBuildArmiesWithoutLeader(boolean buildArmiesWithoutLeader)
    {
        this.buildArmiesWithoutLeader = buildArmiesWithoutLeader;
    }

    public void setBuildLargerArmies(boolean buildLargerArmies)
    {
        this.buildLargerArmies = buildLargerArmies;
    }

    public void setTreasuryEarnsInterest(boolean treasuryEarnsInterest)
    {
        this.treasuryEarnsInterest = treasuryEarnsInterest;
    }

    public void setBuildSpaceshipParts(boolean buildSpaceshipParts)
    {
        this.buildSpaceshipParts = buildSpaceshipParts;
    }

    public void setForbiddenPalace(boolean forbiddenPalace)
    {
        this.forbiddenPalace = forbiddenPalace;
    }

    public void setDecreasesSuccessOfMissiles(boolean decreasesSuccessOfMissiles)
    {
        this.decreasesSuccessOfMissiles = decreasesSuccessOfMissiles;
    }

    public void setAllowSpyMissions(boolean allowSpyMissions)
    {
        this.allowSpyMissions = allowSpyMissions;
    }

    public void setAllowsHealingInEnemyTerritory(boolean allowsHealingInEnemyTerritory)
    {
        this.allowsHealingInEnemyTerritory = allowsHealingInEnemyTerritory;
    }

    public void setGoodsMustBeInCityRadius(boolean goodsMustBeInCityRadius)
    {
        this.goodsMustBeInCityRadius = goodsMustBeInCityRadius;
    }

    public void setRequiresVictoriousArmy(boolean requiresVictoriousArmy)
    {
        this.requiresVictoriousArmy = requiresVictoriousArmy;
    }

    public void setSafeSeaTravel(boolean safeSeaTravel)
    {
        this.safeSeaTravel = safeSeaTravel;
    }

    public void setGainAnyTechsKnownByTwoCivs(boolean gainAnyTechsKnownByTwoCivs)
    {
        this.gainAnyTechsKnownByTwoCivs = gainAnyTechsKnownByTwoCivs;
    }

    public void setDoubleCombatVsBarbarians(boolean doubleCombatVsBarbarians)
    {
        this.doubleCombatVsBarbarians = doubleCombatVsBarbarians;
    }

    public void setIncreasedShipMovement(boolean increasedShipMovement)
    {
        this.increasedShipMovement = increasedShipMovement;
    }

    public void setDoublesResearchOutput(boolean doublesResearchOutput)
    {
        this.doublesResearchOutput = doublesResearchOutput;
    }

    public void setIncreasedTrade(boolean increasedTrade)
    {
        this.increasedTrade = increasedTrade;
    }

    public void setCheaperUpgrades(boolean cheaperUpgrades)
    {
        this.cheaperUpgrades = cheaperUpgrades;
    }

    public void setPaysTradeMaintenance(boolean paysTradeMaintenance)
    {
        this.paysTradeMaintenance = paysTradeMaintenance;
    }

    public void setAllowsNuclearWeapons(boolean allowsNuclearWeapons)
    {
        this.allowsNuclearWeapons = allowsNuclearWeapons;
    }

    public void setDoubleCityGrowth(boolean doubleCityGrowth)
    {
        this.doubleCityGrowth = doubleCityGrowth;
    }

    public void setTwoFreeAdvances(boolean twoFreeAdvances)
    {
        this.twoFreeAdvances = twoFreeAdvances;
    }

    public void setReduceWarWeariness(boolean empireReducesWarWeariness)
    {
        this.empireReducesWarWeariness = empireReducesWarWeariness;
    }

    public void setDoubleCityDefences(boolean doubleCityDefences)
    {
        this.doubleCityDefences = doubleCityDefences;
    }

    public void setAllowDiplomaticVictory(boolean allowDiplomaticVictory)
    {
        this.allowDiplomaticVictory = allowDiplomaticVictory;
    }

    public void setPlusTwoShipMovement(boolean plusTwoShipMovement)
    {
        this.plusTwoShipMovement = plusTwoShipMovement;
    }

    public void setQuestionMarkWonderTrait(boolean questionMarkWonderTrait)
    {
        this.questionMarkWonderTrait = questionMarkWonderTrait;
    }

    public void setIncreasedArmyValue(boolean increasedArmyValue)
    {
        this.increasedArmyValue = increasedArmyValue;
    }

    public void setTouristAttraction(boolean touristAttraction)
    {
        this.touristAttraction = touristAttraction;
    }

    public String getCivilopediaEntry()
    {
        return civilopediaEntry;
    }
    public String getName()
    {
        return name;
    }
    public String getDescription()
    {
        return description;
    }

    //Getter methods
    public int getDataLength()
    {
        return dataLength;
    }


    public int getDoublesHappiness()
    {
        return doublesHappiness;
    }

    public int getGainInEveryCity()
    {
        return gainInEveryCity;
    }

    public int getGainOnContinent()
    {
        return gainOnContinent;
    }

    public int getReqImprovement()
    {
        return reqImprovement;
    }

    public int getCost()
    {
        return cost;
    }

    public int getCulture()
    {
        return culture;
    }

    public int getBombardDefence()
    {
        return bombardDefence;
    }

    public int getNavalBombardDefence()
    {
        return navalBombardDefence;
    }

    public int getDefenceBonus()
    {
        return defenceBonus;
    }

    public int getNavalDefenceBonus()
    {
        return navalDefenceBonus;
    }

    public int getMaintenanceCost()
    {
        return maintenanceCost;
    }

    public int getHappyAll()
    {
        return happyAll;
    }

    public int getHappy()
    {
        return happy;
    }

    public int getUnhappyAll()
    {
        return unhappyAll;
    }

    public int getUnhappy()
    {
        return unhappy;
    }

    public int getNumReqBuildings()
    {
        return numReqBuildings;
    }

    public int getAirPower()
    {
        return airPower;
    }

    public int getNavalPower()
    {
        return navalPower;
    }

    public int getPollution()
    {
        return pollution;
    }

    public int getProduction()
    {
        return production;
    }

    public int getReqGovernment()
    {
        return reqGovernment;
    }

    public int getSpaceshipPart()
    {
        return spaceshipPart;
    }

    public int getReqAdvance()
    {
        return reqAdvanceInt;
    }

    public int getObsoleteBy()
    {
        return obsoleteByInt;
    }

    public int getReqResource1()
    {
        return reqResource1;
    }

    public int getReqResource2()
    {
        return reqResource2;
    }

    public int getImprovements()
    {
        return improvements;
    }

    public int getOtherChar()
    {
        return otherChar;
    }

    public int getSmallWonderCharacteristics()
    {
        return smallWonderCharacteristics;
    }

    public int getWonderCharacteristics()
    {
        return wonderCharacteristics;
    }

    public int getArmiesRequired()
    {
        return armiesRequired;
    }

    public int getFlavors()
    {
        return flavors;
    }

    public int getQuestionMark()
    {
        return questionMark;
    }

    public int getUnitProduced()
    {
        return unitProducedInt;
    }

    public int getUnitFrequency()
    {
        return unitFrequency;
    }

    public boolean getCenterOfEmpire()
    {
        return centerOfEmpire;
    }

    public boolean getVeteranUnits()
    {
        return veteranUnits;
    }

    public boolean getIncreasedResearch()
    {
        return increasedResearch;
    }

    public boolean getIncreasedLuxuries()
    {
        return increasedLuxuries;
    }

    public boolean getIncreasedTaxes()
    {
        return increasedTaxes;
    }

    public boolean getRemovePopPollution()
    {
        return removePopPollution;
    }

    public boolean getReduceBldgPollution()
    {
        return reduceBldgPollution;
    }

    public boolean getResistantToBribery()
    {
        return resistantToBribery;
    }

    public boolean getReducesCorruption()
    {
        return reducesCorruption;
    }

    public boolean getDoublesCityGrowthRate()
    {
        return doublesCityGrowthRate;
    }

    public boolean getIncreasesLuxuryTrade()
    {
        return increasesLuxuryTrade;
    }

    public boolean getAllowCityLevel2()
    {
        return allowCityLevel2;
    }

    public boolean getAllowCityLevel3()
    {
        return allowCityLevel3;
    }

    public boolean getReplacesOtherWithThisTag()
    {
        return replacesOtherWithThisTag;
    }

    public boolean getMustBeNearWater()
    {
        return mustBeNearWater;
    }

    public boolean getMustBeNearRiver()
    {
        return mustBeNearRiver;
    }

    public boolean getMayExplodeOrMeltdown()
    {
        return mayExplodeOrMeltdown;
    }

    public boolean getVeteranSeaUnits()
    {
        return veteranSeaUnits;
    }

    public boolean getVeteranAirUnits()
    {
        return veteranAirUnits;
    }

    public boolean getCapitalization()
    {
        return capitalization;
    }

    public boolean getAllowWaterTrade()
    {
        return allowWaterTrade;
    }

    public boolean getAllowAirTrade()
    {
        return allowAirTrade;
    }

    public boolean getReducesWarWeariness()
    {
        return reducesWarWeariness;
    }

    public boolean getIncreasesShieldsInWater()
    {
        return increasesShieldsInWater;
    }

    public boolean getIncreasesFoodInWater()
    {
        return increasesFoodInWater;
    }

    public boolean getCoastalInstallation()
    {
        return coastalInstallation;
    }

    public boolean getMilitaryInstallation()
    {
        return militaryInstallation;
    }

    public boolean isWonder()
    {
        return wonder;
    }

    public boolean isSmallWonder()
    {
        return smallWonder;
    }

    public boolean getContinentalMoodEffects()
    {
        return continentalMoodEffects;
    }

    public boolean getResearchInstallation()
    {
        return researchInstallation;
    }

    public boolean getTradeInstallation()
    {
        return tradeInstallation;
    }

    public boolean getExplorationInstallation()
    {
        return explorationInstallation;
    }

    public boolean getPlaceOfWorship()
    {
        return placeOfWorship;
    }

    public boolean getConstructionInstallation()
    {
        return constructionInstallation;
    }

    public boolean getIncreasesChanceOfLeaderAppearance()
    {
        return increasesChanceOfLeaderAppearance;
    }

    public boolean getBuildArmiesWithoutLeader()
    {
        return buildArmiesWithoutLeader;
    }

    public boolean getBuildLargerArmies()
    {
        return buildLargerArmies;
    }

    public boolean getTreasuryEarnsInterest()
    {
        return treasuryEarnsInterest;
    }

    public boolean getBuildSpaceshipParts()
    {
        return buildSpaceshipParts;
    }

    public boolean getForbiddenPalace()
    {
        return forbiddenPalace;
    }

    public boolean getDecreasesSuccessOfMissiles()
    {
        return decreasesSuccessOfMissiles;
    }

    public boolean getAllowSpyMissions()
    {
        return allowSpyMissions;
    }

    public boolean getAllowsHealingInEnemyTerritory()
    {
        return allowsHealingInEnemyTerritory;
    }

    public boolean getGoodsMustBeInCityRadius()
    {
        return goodsMustBeInCityRadius;
    }

    public boolean getRequiresVictoriousArmy()
    {
        return requiresVictoriousArmy;
    }

    public boolean getSafeSeaTravel()
    {
        return safeSeaTravel;
    }

    public void setSeafaringInstallation(boolean value)
    {
        this.seafaringInstallation = value;
    }
    public void setAgriculturalInstallation(boolean value)
    {
        this.agriculturalInstallation = value;
    }
    public boolean getSeafaringInstallation()
    {
        return seafaringInstallation;
    }

    public boolean getAgriculturalInstallation()
    {
        return agriculturalInstallation;
    }

    public boolean getGainAnyTechsKnownByTwoCivs()
    {
        return gainAnyTechsKnownByTwoCivs;
    }

    public boolean getDoubleCombatVsBarbarians()
    {
        return doubleCombatVsBarbarians;
    }

    public boolean getIncreasedShipMovement()
    {
        return increasedShipMovement;
    }
    public boolean getDoublesResearchOutput()
    {
        return doublesResearchOutput;
    }

    public boolean getIncreasedTrade()
    {
        return increasedTrade;
    }

    public boolean getCheaperUpgrades()
    {
        return cheaperUpgrades;
    }

    public boolean getPaysTradeMaintenance()
    {
        return paysTradeMaintenance;
    }

    public boolean getAllowsNuclearWeapons()
    {
        return allowsNuclearWeapons;
    }

    public boolean getDoubleCityGrowth()
    {
        return doubleCityGrowth;
    }

    public boolean getTwoFreeAdvances()
    {
        return twoFreeAdvances;
    }

    public boolean getReducesWarWearinessEmpire()
    {
        return empireReducesWarWeariness;
    }

    public boolean getDoubleCityDefences()
    {
        return doubleCityDefences;
    }

    public boolean getAllowDiplomaticVictory()
    {
        return allowDiplomaticVictory;
    }
    public boolean getTouristAttraction()
    {
        return touristAttraction;
    }
    public boolean getIncreasedArmyValue()
    {
        return increasedArmyValue;
    }
    public boolean getQuestionMarkWonderTrait()
    {
        return questionMarkWonderTrait;
    }
    public boolean getPlusTwoShipMovement()
    {
        return plusTwoShipMovement;
    }

    public boolean requiresEliteShip() {
        return requiresEliteShip;
    }

    public void setRequiresEliteShip(boolean requiresEliteShip) {
        this.requiresEliteShip = requiresEliteShip;
    }

    public int getNumFlavors() {
        return numFlavors;
    }

    public void setNumFlavors(int numFlavors) {
        this.numFlavors = numFlavors;
    }
    
    public void setFlavour(int index, boolean value)
    {
        flavours.set(index, value);
    }
    
    public boolean getFlavour(int index)
    {
        return flavours.get(index);
    }
    
    public void handleConvertToConquests()
    {
        if (IO.convertDataLengthsForConquests)
            this.dataLength+=16;
    }
    
    public void handleDeleteUnit(int index)
    {
        if (unitProducedInt == index) {
            unitProducedInt = -1;
            unitProduced = null;
        }
        else if (unitProducedInt > index)
            unitProducedInt--;
    }
    
    public void handleSwappedUnit() {
        if (unitProducedInt != -1) {
            unitProducedInt = unitProduced.getIndex();
        }
    }
    
    public void handleDeletedTech(int index)
    {
        if (reqAdvanceInt == index) {
            reqAdvanceInt = -1;
            reqAdvance = null;
        }
        else if (reqAdvanceInt > index) {
            reqAdvanceInt--;
            reqAdvance = baseLink.technology.get(reqAdvanceInt);
        }
        
        if (obsoleteByInt == index) {
            obsoleteByInt = -1;
            obsoleteBy = null;
        }
        else if (obsoleteByInt > index) {
            obsoleteByInt--;
            obsoleteBy = baseLink.technology.get(obsoleteByInt);
        }
    }
    
    public void handleSwappedTech(int source, int destination) {
        if (reqAdvanceInt != -1) {
            reqAdvanceInt = reqAdvance.getIndex();
        }
        if (obsoleteByInt != -1) {
            obsoleteByInt = obsoleteBy.getIndex();
        }
    }
    
    public void handleDeletedGovernment(int index)
    {
        if (reqGovernment == index)
            reqGovernment = -1;
        else if (reqGovernment > index)
            reqGovernment--;
    }
    
    public void handleDeletedFlavour(int index)
    {
        flavours.remove(index);
        numFlavors--;
        createBinary();
        //do need to recreate binary since the flavor got removed
    }
    
    public void handleAddedFlavour()
    {
        flavours.add(false);
        numFlavors++;
        //don't need to recreate binary since the new flavor is false
    }
    
    public void handleDeletedBuilding(int deletedBuildingIndex)
    {
        if (getGainInEveryCity() == deletedBuildingIndex)
            setGainInEveryCity(0);
        else if (getGainInEveryCity() > deletedBuildingIndex)
            gainInEveryCity--;
        if (gainOnContinent == deletedBuildingIndex)
            gainOnContinent = 0;
        else if (gainOnContinent > deletedBuildingIndex)
            gainOnContinent--;
        if (reqImprovement == deletedBuildingIndex)
            reqImprovement = 0;
        else if (reqImprovement > deletedBuildingIndex)
            reqImprovement--;
        if (doublesHappiness == deletedBuildingIndex)
            doublesHappiness = 0;
        else if (doublesHappiness > deletedBuildingIndex)
            doublesHappiness--;
    }

    @Override
    public String toString()
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "name: " + name + lineReturn;
        toReturn = toReturn + "civilopediaEntry: " + civilopediaEntry + lineReturn;
        toReturn = toReturn + "dataLength: " + dataLength + lineReturn;
        toReturn = toReturn + "description: " + description + lineReturn;
        toReturn = toReturn + "gainInEveryCity: " + gainInEveryCity + lineReturn;
        toReturn = toReturn + "gainOnContinent: " + gainOnContinent + lineReturn;
        toReturn = toReturn + "reqImprovement: " + reqImprovement + lineReturn;
        toReturn = toReturn + "cost: " + cost + lineReturn;
        toReturn = toReturn + "culture: " + culture + lineReturn;
        toReturn = toReturn + "bombardDefence: " + bombardDefence + lineReturn;
        toReturn = toReturn + "navalBombardDefence: " + navalBombardDefence + lineReturn;
        toReturn = toReturn + "defenceBonus: " + defenceBonus + lineReturn;
        toReturn = toReturn + "navalDefenceBonus: " + navalDefenceBonus + lineReturn;
        toReturn = toReturn + "maintenanceCost: " + maintenanceCost + lineReturn;
        toReturn = toReturn + "happyAll: " + happyAll + lineReturn;
        toReturn = toReturn + "happy: " + happy + lineReturn;
        toReturn = toReturn + "unhappyAll: " + unhappyAll + lineReturn;
        toReturn = toReturn + "unhappy: " + unhappy + lineReturn;
        toReturn = toReturn + "numReqBuildings: " + numReqBuildings + lineReturn;
        toReturn = toReturn + "airPower: " + airPower + lineReturn;
        toReturn = toReturn + "navalPower: " + navalPower + lineReturn;
        toReturn = toReturn + "pollution: " + pollution + lineReturn;
        toReturn = toReturn + "production: " + production + lineReturn;
        toReturn = toReturn + "reqGovernment: " + reqGovernment + lineReturn;
        toReturn = toReturn + "spaceshipPart: " + spaceshipPart + lineReturn;
        toReturn = toReturn + "reqAdvance: " + reqAdvance + lineReturn;
        toReturn = toReturn + "obsoleteBy: " + obsoleteBy + lineReturn;
        toReturn = toReturn + "reqResource1: " + reqResource1 + lineReturn;
        toReturn = toReturn + "reqResource2: " + reqResource2 + lineReturn;
        toReturn = toReturn + "improvements: " + improvements + lineReturn;
        toReturn = toReturn + "otherChar: " + otherChar + lineReturn;
        toReturn = toReturn + "smallWonder: " + smallWonder + lineReturn;
        toReturn = toReturn + "wonder: " + wonder + lineReturn;
        toReturn = toReturn + "armiesRequired: " + armiesRequired + lineReturn;
        toReturn = toReturn + "flavors: " + flavors + lineReturn;
        toReturn = toReturn + "questionMark: " + questionMark + lineReturn;
        toReturn = toReturn + "unitProduced: " + unitProducedInt + lineReturn;
        toReturn = toReturn + "unitFrequency: " + unitFrequency + lineReturn;
        return toReturn;
    }

	public void extractEnglish(int numFlavours)
	{
                //TO BE ADDED: More efficient version with bitwise and operator
                //Test that the bitwise and of 2^x (preferably written without Math.pow
                //is not equal to zero, if so, trait is true
                //In effect, equivalent to Intercal ~ (squiggle/select) operator,
                //in detail, the Intercal version would always result in the squiggle
                //being 1 or 0 in cases of squiggling something with 2^n
		int improvementsCopy = improvements;
        //System.out.println("improvementCopy:  " + improvementsCopy);
		int divBy = 0;
                if (improvementsCopy < 0)
                {
                    //System.out.println("SIGN BIT: " + improvementsCopy);
                    improvementsCopy = improvementsCopy << 1;   //left shift
                    improvementsCopy = improvementsCopy >>> 1;  //shift logical right
                    //the above should first knock the 1 off the end, then shift
                    //everything back, with a 0 there
                    //effectively, this should negate the sign bit forced by Java's
                    //signed integer system
                    goodsMustBeInCityRadius = true;
                    //System.out.println("New value: " + improvementsCopy);
                }
		divBy = improvementsCopy/(int)(Math.pow(2, 30));
		if (divBy == 1)
		{
			canBuildUnits = true;
			improvementsCopy-=1073741824;
		}
		divBy = improvementsCopy/(int)(Math.pow(2, 29));
		if (divBy == 1)
		{
			doublesSacrifice = true;
			improvementsCopy-=536870912;
		}
		divBy = improvementsCopy/(int)(Math.pow(2, 28));
		if (divBy == 1)
		{
			actsAsGeneralTelepad = true;
			improvementsCopy-=268435456;
		}
		divBy = improvementsCopy/(int)(Math.pow(2, 27));
		if (divBy == 1)
		{
			stealthAttackBarrier = true;
			improvementsCopy-=134217728;
		}
		divBy = improvementsCopy/(int)(Math.pow(2, 26));
		if (divBy == 1)
		{
			charmBarrier = true;
			improvementsCopy-=67108864;
		}
		divBy = improvementsCopy/(int)(Math.pow(2, 25));
		if (divBy == 1)
		{
			increasesTradeInWater = true;
			improvementsCopy-=33554432;
		}
		divBy = improvementsCopy/(int)(Math.pow(2, 24));
		if (divBy == 1)
		{
			increasesFoodInWater = true;
			improvementsCopy-=16777216;
		}
		divBy = improvementsCopy/(int)(Math.pow(2, 23));
		if (divBy == 1)
		{
			increasesShieldsInWater = true;
			improvementsCopy-=8388608;
		}
		divBy = improvementsCopy/(int)(Math.pow(2, 22));
		if (divBy == 1)
		{
			reducesWarWeariness = true;
			improvementsCopy-=4194304;
		}
		divBy = improvementsCopy/(int)(Math.pow(2, 21));
		if (divBy == 1)
		{
			allowAirTrade = true;
			improvementsCopy-=2097152;
		}
		divBy = improvementsCopy/(int)(Math.pow(2, 20));
		if (divBy == 1)
		{
			allowWaterTrade = true;
			improvementsCopy-=1048576;
		}
		divBy = improvementsCopy/(int)(Math.pow(2, 19));
		if (divBy == 1)
		{
			capitalization = true;
			improvementsCopy-=524288;
		}
		divBy = improvementsCopy/(int)(Math.pow(2, 18));
		if (divBy == 1)
		{
			veteranAirUnits = true;
			improvementsCopy-=262144;
		}
		divBy = improvementsCopy/(int)(Math.pow(2, 17));
		if (divBy == 1)
		{
			veteranSeaUnits = true;
			improvementsCopy-=131072;
		}
		divBy = improvementsCopy/(int)(Math.pow(2, 16));
		if (divBy == 1)
		{
			mayExplodeOrMeltdown = true;
			improvementsCopy-=65536;
		}
		divBy = improvementsCopy/(int)(Math.pow(2, 15));
		if (divBy == 1)
		{
			mustBeNearRiver = true;
			improvementsCopy-=32768;
		}
		divBy = improvementsCopy/(int)(Math.pow(2, 14));
		if (divBy == 1)
		{
			mustBeNearWater = true;
			improvementsCopy-=16384;
		}
		divBy = improvementsCopy/(int)(Math.pow(2, 13));
		if (divBy == 1)
		{
			replacesOtherWithThisTag = true;
			improvementsCopy-=8192;
		}
		divBy = improvementsCopy/(int)(Math.pow(2, 12));
		if (divBy == 1)
		{
			allowCityLevel3 = true;
			improvementsCopy-=4096;
		}
		divBy = improvementsCopy/(int)(Math.pow(2, 11));
		if (divBy == 1)
		{
			allowCityLevel2 = true;
			improvementsCopy-=2048;
		}
		divBy = improvementsCopy/(int)(Math.pow(2, 10));
		if (divBy == 1)
		{
			increasesLuxuryTrade = true;
			improvementsCopy-=1024;
		}
		divBy = improvementsCopy/(int)(Math.pow(2, 9));
		if (divBy == 1)
		{
			doublesCityGrowthRate = true;
			improvementsCopy-=512;
		}
		divBy = improvementsCopy/(int)(Math.pow(2, 8));
		if (divBy == 1)
		{
			reducesCorruption = true;
			improvementsCopy-=256;
		}
		divBy = improvementsCopy/(int)(Math.pow(2, 7));
		if (divBy == 1)
		{
			resistantToBribery = true;
			improvementsCopy-=128;
		}
		divBy = improvementsCopy/(int)(Math.pow(2, 6));
		if (divBy == 1)
		{
			reduceBldgPollution = true;
			improvementsCopy-=64;
		}
		divBy = improvementsCopy/(int)(Math.pow(2, 5));
		if (divBy == 1)
		{
			removePopPollution = true;
			improvementsCopy-=32;
		}
		divBy = improvementsCopy/(int)(Math.pow(2, 4));
		if (divBy == 1)
		{
			increasedTaxes = true;
			improvementsCopy-=16;
		}
		divBy = improvementsCopy/(int)(Math.pow(2, 3));
		if (divBy == 1)
		{
			increasedLuxuries = true;
			improvementsCopy-=8;
		}
		divBy = improvementsCopy/(int)(Math.pow(2, 2));
		if (divBy == 1)
		{
			increasedResearch = true;
			improvementsCopy-=4;
		}
        //System.out.println(name);
        //System.out.println("improvementCopy: " + improvementsCopy);
		divBy = improvementsCopy/(int)(Math.pow(2, 1));
        //System.out.println(divBy);
		if (divBy == 1)
		{
			veteranUnits = true;
			improvementsCopy-=2;
		}
        //System.out.println("improvementCopy: " + improvementsCopy);
		divBy = improvementsCopy/(int)(Math.pow(2, 0));
        //System.out.println(divBy);
		if (divBy == 1)
		{
			centerOfEmpire = true;
			improvementsCopy-=1;
		}
        //Begin Other Characteristics
        int otherCharCopy = otherChar;
        divBy = otherCharCopy/(int)(Math.pow(2, 11));
        if (divBy == 1)
        {
            seafaringInstallation = true;
            otherCharCopy-=2048;
        }
        divBy = otherCharCopy/(int)(Math.pow(2, 10));
        if (divBy == 1)
        {
            agriculturalInstallation = true;
            otherCharCopy-=1024;
        }
        divBy = otherCharCopy/(int)(Math.pow(2, 9));
        if (divBy == 1)
        {
            constructionInstallation = true;
            otherCharCopy-=512;
        }
        divBy = otherCharCopy/(int)(Math.pow(2, 8));
        if (divBy == 1)
        {
            placeOfWorship = true;
            otherCharCopy-=256;
        }
        divBy = otherCharCopy/(int)(Math.pow(2, 7));
        if (divBy == 1)
        {
            explorationInstallation = true;
            otherCharCopy-=128;
        }
        divBy = otherCharCopy/(int)(Math.pow(2, 6));
        if (divBy == 1)
        {
            tradeInstallation = true;
            otherCharCopy-=64;
        }
        divBy = otherCharCopy/(int)(Math.pow(2, 5));
        if (divBy == 1)
        {
            researchInstallation = true;
            otherCharCopy-=32;
        }
        divBy = otherCharCopy/(int)(Math.pow(2, 4));
        if (divBy == 1)
        {
            continentalMoodEffects = true;
            otherCharCopy-=16;
        }
        divBy = otherCharCopy/(int)(Math.pow(2, 3));
        if (divBy == 1)
        {
            smallWonder = true;
            otherCharCopy-=8;
        }
        divBy = otherCharCopy/(int)(Math.pow(2, 2));
        if (divBy == 1)
        {
            wonder = true;
            otherCharCopy-=4;
        }
        divBy = otherCharCopy/(int)(Math.pow(2, 1));
        if (divBy == 1)
        {
            militaryInstallation = true;
            otherCharCopy-=2;
        }
        divBy = otherCharCopy/(int)(Math.pow(2, 0));
        if (divBy == 1)
        {
            coastalInstallation = true;
            otherCharCopy-=1;
        }
        int smallWonderCopy = smallWonderCharacteristics;
        divBy = smallWonderCopy/(int)(Math.pow(2, 11));
        if (divBy == 1)
        {
            requiresEliteShip = true;
            smallWonderCopy-=2048;
        }
        divBy = smallWonderCopy/(int)(Math.pow(2, 10));
        if (divBy == 1)
        {
            requiresVictoriousArmy = true;
            smallWonderCopy-=1024;
        }
        divBy = smallWonderCopy/(int)(Math.pow(2, 9));
        if (divBy == 1)
        {
            goodsMustBeInCityRadius = true;
            smallWonderCopy-=512;
        }
        divBy = smallWonderCopy/(int)(Math.pow(2, 8));
        if (divBy == 1)
        {
            allowsHealingInEnemyTerritory = true;
            smallWonderCopy-=256;
        }
        divBy = smallWonderCopy/(int)(Math.pow(2, 7));
        if (divBy == 1)
        {
            allowSpyMissions = true;
            smallWonderCopy-=128;
        }
        divBy = smallWonderCopy/(int)(Math.pow(2, 6));
        if (divBy == 1)
        {
            decreasesSuccessOfMissiles = true;
            smallWonderCopy-=64;
        }
        divBy = smallWonderCopy/(int)(Math.pow(2, 5));
        if (divBy == 1)
        {
            forbiddenPalace = true;
            smallWonderCopy-=32;
        }
        divBy = smallWonderCopy/(int)(Math.pow(2, 4));
        if (divBy == 1)
        {
            buildSpaceshipParts = true;
            smallWonderCopy-=16;
        }
        divBy = smallWonderCopy/(int)(Math.pow(2, 3));
        if (divBy == 1)
        {
            treasuryEarnsInterest = true;
            smallWonderCopy-=8;
        }
        divBy = smallWonderCopy/(int)(Math.pow(2, 2));
        if (divBy == 1)
        {
            buildLargerArmies = true;
            smallWonderCopy-=4;
        }
        divBy = smallWonderCopy/(int)(Math.pow(2, 1));
        if (divBy == 1)
        {
            buildArmiesWithoutLeader = true;
            smallWonderCopy-=2;
        }
        divBy = smallWonderCopy/(int)(Math.pow(2, 0));
        if (divBy == 1)
        {
            increasesChanceOfLeaderAppearance = true;
            smallWonderCopy-=1;
        }
        //wonder characteristics
        int wonderCopy = wonderCharacteristics;
        divBy = wonderCopy/(int)(Math.pow(2, 17));
        if (divBy == 1)
        {
            touristAttraction = true;
            wonderCopy-=131072;
        }
        divBy = wonderCopy/(int)(Math.pow(2, 16));
        if (divBy == 1)
        {
            increasedArmyValue = true;
            wonderCopy-=65536;
        }
        divBy = wonderCopy/(int)(Math.pow(2, 15));
        if (divBy == 1)
        {
            questionMarkWonderTrait = true;
            wonderCopy-=32768;
        }
        divBy = wonderCopy/(int)(Math.pow(2, 14));
        if (divBy == 1)
        {
            plusTwoShipMovement = true;
            wonderCopy-=16384;
        }
        divBy = wonderCopy/(int)(Math.pow(2, 13));
        if (divBy == 1)
        {
            allowDiplomaticVictory = true;
            wonderCopy-=8192;
        }
        divBy = wonderCopy/(int)(Math.pow(2, 12));
        if (divBy == 1)
        {
            doubleCityDefences = true;
            wonderCopy-=4096;
        }
        divBy = wonderCopy/(int)(Math.pow(2, 11));
        if (divBy == 1)
        {
            empireReducesWarWeariness = true;
            wonderCopy-=2048;
        }
        divBy = wonderCopy/(int)(Math.pow(2, 10));
        if (divBy == 1)
        {
            twoFreeAdvances = true;
            wonderCopy-=1024;
        }
        divBy = wonderCopy/(int)(Math.pow(2, 9));
        if (divBy == 1)
        {
            doubleCityGrowth = true;
            wonderCopy-=512;
        }
        divBy = wonderCopy/(int)(Math.pow(2, 8));
        if (divBy == 1)
        {
            allowsNuclearWeapons = true;
            wonderCopy-=256;
        }
        divBy = wonderCopy/(int)(Math.pow(2, 7));
        if (divBy == 1)
        {
            paysTradeMaintenance = true;
            wonderCopy-=128;
        }
        divBy = wonderCopy/(int)(Math.pow(2, 6));
        if (divBy == 1)
        {
            cheaperUpgrades = true;
            wonderCopy-=64;
        }
        divBy = wonderCopy/(int)(Math.pow(2, 5));
        if (divBy == 1)
        {
            increasedTrade = true;
            wonderCopy-=32;
        }
        divBy = wonderCopy/(int)(Math.pow(2, 4));
        if (divBy == 1)
        {
            doublesResearchOutput = true;
            wonderCopy-=16;
        }
        divBy = wonderCopy/(int)(Math.pow(2, 3));
        if (divBy == 1)
        {
            increasedShipMovement = true;
            wonderCopy-=8;
        }
        divBy = wonderCopy/(int)(Math.pow(2, 2));
        if (divBy == 1)
        {
            doubleCombatVsBarbarians = true;
            wonderCopy-=4;
        }
        divBy = wonderCopy/(int)(Math.pow(2, 1));
        if (divBy == 1)
        {
            gainAnyTechsKnownByTwoCivs = true;
            wonderCopy-=2;
        }
        divBy = wonderCopy/(int)(Math.pow(2, 0));
        if (divBy == 1)
        {
            safeSeaTravel = true;
            wonderCopy-=1;
        }
        int flavorCopy = flavors;
        for (int i = 0; i < numFlavours; i++)
        {
            flavours.add(false);
        }
        //System.out.println("numFlavours: " + numFlavours);
        //System.out.println("flavours.size(): " + flavours.size());
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


    public void createBinary()
    {
        //System.out.println(this.name);
        long sum = 0;
        if (goodsMustBeInCityRadius)
            sum = sum + (long)(Math.pow(2, 31));
        if (canBuildUnits)
            sum = sum + (long)(Math.pow(2, 30));
        if (doublesSacrifice)
            sum = sum + (long)(Math.pow(2, 29));
        if (actsAsGeneralTelepad)
            sum = sum + (long)(Math.pow(2, 28));
        if (stealthAttackBarrier)
            sum = sum + (long)(Math.pow(2, 27));
        if (charmBarrier)
            sum = sum + (long)(Math.pow(2, 26));
        if (increasesTradeInWater)
            sum = sum + (long)(Math.pow(2, 25));
        if (increasesFoodInWater)
            sum = sum + (long)(Math.pow(2, 24));
        if (increasesShieldsInWater)
            sum = sum + (long)(Math.pow(2, 23));
        if (reducesWarWeariness)
            sum = sum + (long)(Math.pow(2, 22));
        if (allowAirTrade)
            sum = sum + (long)(Math.pow(2, 21));
        if (allowWaterTrade)
            sum = sum + (long)(Math.pow(2, 20));
        if (capitalization)
            sum = sum + (long)(Math.pow(2, 19));
        if (veteranAirUnits)
            sum = sum + (long)(Math.pow(2, 18));
        if (veteranSeaUnits)
            sum = sum + (long)(Math.pow(2, 17));
        if (mayExplodeOrMeltdown)
            sum = sum + (long)(Math.pow(2, 16));
        if (mustBeNearRiver)
            sum = sum + (long)(Math.pow(2, 15));
        if (mustBeNearWater)
            sum = sum + (long)(Math.pow(2, 14));
        if (replacesOtherWithThisTag)
            sum = sum + (long)(Math.pow(2, 13));
        if (allowCityLevel3)
            sum = sum + (long)(Math.pow(2, 12));
        if (allowCityLevel2)
            sum = sum + (long)(Math.pow(2, 11));
        if (increasesLuxuryTrade)
            sum = sum + (long)(Math.pow(2, 10));
        if (doublesCityGrowthRate)
            sum = sum + (long)(Math.pow(2, 9));
        if (reducesCorruption)
            sum = sum + (long)(Math.pow(2, 8));
        if (resistantToBribery)
            sum = sum + (long)(Math.pow(2, 7));
        if (reduceBldgPollution)
            sum = sum + (long)(Math.pow(2, 6));
        if (removePopPollution)
            sum = sum + (long)(Math.pow(2, 5));
        if (increasedTaxes)
            sum = sum + (long)(Math.pow(2, 4));
        if (increasedLuxuries)
            sum = sum + (long)(Math.pow(2, 3));
        if (increasedResearch)
            sum = sum + (long)(Math.pow(2, 2));
        if (veteranUnits)
            sum = sum + (long)(Math.pow(2, 1));
        if (centerOfEmpire)
            sum = sum + (long)(Math.pow(2, 0));
        improvements = (int)sum;
        Boolean[]list = {seafaringInstallation, agriculturalInstallation, constructionInstallation, placeOfWorship, explorationInstallation, tradeInstallation, researchInstallation, continentalMoodEffects, smallWonder, wonder, militaryInstallation, coastalInstallation};
        otherChar = Utils.recalculateBitfield(list);
        Boolean[]list2 = {requiresEliteShip, requiresVictoriousArmy, goodsMustBeInCityRadius, allowsHealingInEnemyTerritory, allowSpyMissions, decreasesSuccessOfMissiles, forbiddenPalace, buildSpaceshipParts, treasuryEarnsInterest, buildLargerArmies, buildArmiesWithoutLeader, increasesChanceOfLeaderAppearance};
        smallWonderCharacteristics = Utils.recalculateBitfield(list2);
        sum = 0;
        if (touristAttraction)
            sum = sum + (long)(Math.pow(2, 17));
        if (increasedArmyValue)
            sum = sum + (long)(Math.pow(2, 16));
        if (questionMarkWonderTrait)
            sum = sum + (long)(Math.pow(2, 15));
        if (plusTwoShipMovement)
            sum = sum + (long)(Math.pow(2, 14));
        if (allowDiplomaticVictory)
            sum = sum + (long)(Math.pow(2, 13));
        if (doubleCityDefences)
            sum = sum + (long)(Math.pow(2, 12));
        if (empireReducesWarWeariness)
            sum = sum + (long)(Math.pow(2, 11));
        if (twoFreeAdvances)
            sum = sum + (long)(Math.pow(2, 10));
        if (doubleCityGrowth)
            sum = sum + (long)(Math.pow(2, 9));
        if (allowsNuclearWeapons)
            sum = sum + (long)(Math.pow(2, 8));
        if (paysTradeMaintenance)
            sum = sum + (long)(Math.pow(2, 7));
        if (cheaperUpgrades)
            sum = sum + (long)(Math.pow(2, 6));
        if (increasedTrade)
            sum = sum + (long)(Math.pow(2, 5));
        if (doublesResearchOutput)
            sum = sum + (long)(Math.pow(2, 4));
        if (increasedShipMovement)
            sum = sum + (long)(Math.pow(2, 3));
        if (doubleCombatVsBarbarians)
            sum = sum + (long)(Math.pow(2, 2));
        if (gainAnyTechsKnownByTwoCivs)
            sum = sum + (long)(Math.pow(2, 1));
        if (safeSeaTravel)
            sum = sum + (long)(Math.pow(2, 0));
        wonderCharacteristics = (int)sum;
        //flavours
        sum = 0;
        for (int i = 0; i < this.flavours.size(); i++)
            if (flavours.get(i))
                sum+=(int)Math.pow(2, i);
        flavors = (int)sum;

        //System.out.println("BINARY CREATED");
    }

    public String toEnglish()
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "name: " + name + lineReturn;
        toReturn = toReturn + "civilopediaEntry: " + civilopediaEntry + lineReturn;
        toReturn = toReturn + "dataLength: " + dataLength + lineReturn;
        toReturn = toReturn + "description: " + description + lineReturn;
        toReturn = toReturn + "gainInEveryCity: " + gainInEveryCity + lineReturn;
        toReturn = toReturn + "gainOnContinent: " + gainOnContinent + lineReturn;
        toReturn = toReturn + "reqImprovement: " + reqImprovement + lineReturn;
        toReturn = toReturn + "cost: " + cost + lineReturn;
        toReturn = toReturn + "culture: " + culture + lineReturn;
        toReturn = toReturn + "bombardDefence: " + bombardDefence + lineReturn;
        toReturn = toReturn + "navalBombardDefence: " + navalBombardDefence + lineReturn;
        toReturn = toReturn + "defenceBonus: " + defenceBonus + lineReturn;
        toReturn = toReturn + "navalDefenceBonus: " + navalDefenceBonus + lineReturn;
        toReturn = toReturn + "maintenanceCost: " + maintenanceCost + lineReturn;
        toReturn = toReturn + "happyAll: " + happyAll + lineReturn;
        toReturn = toReturn + "happy: " + happy + lineReturn;
        toReturn = toReturn + "unhappyAll: " + unhappyAll + lineReturn;
        toReturn = toReturn + "unhappy: " + unhappy + lineReturn;
        toReturn = toReturn + "numReqBuildings: " + numReqBuildings + lineReturn;
        toReturn = toReturn + "airPower: " + airPower + lineReturn;
        toReturn = toReturn + "navalPower: " + navalPower + lineReturn;
        toReturn = toReturn + "pollution: " + pollution + lineReturn;
        toReturn = toReturn + "production: " + production + lineReturn;
        toReturn = toReturn + "reqGovernment: " + reqGovernment + lineReturn;
        toReturn = toReturn + "spaceshipPart: " + spaceshipPart + lineReturn;
        toReturn = toReturn + "reqAdvance: " + reqAdvance + lineReturn;
        toReturn = toReturn + "obsoleteBy: " + obsoleteBy + lineReturn;
        toReturn = toReturn + "reqResource1: " + reqResource1 + lineReturn;
        toReturn = toReturn + "reqResource2: " + reqResource2 + lineReturn;
        toReturn = toReturn + "improvements" + improvements + lineReturn;
        toReturn = toReturn + "  centerOfEmpire: " + centerOfEmpire + lineReturn;
        toReturn = toReturn + "  veteranUnits: " + veteranUnits + lineReturn;
        toReturn = toReturn + "  increasedResearch: " + increasedResearch + lineReturn;
        toReturn = toReturn + "  increasedLuxuries: " + increasedLuxuries + lineReturn;
        toReturn = toReturn + "  increasedTaxes: " + increasedTaxes + lineReturn;
        toReturn = toReturn + "  removePopPollution: " + removePopPollution + lineReturn;
        toReturn = toReturn + "  reduceBldgPollution: " + reduceBldgPollution + lineReturn;
        toReturn = toReturn + "  resistantToBribery: " + resistantToBribery + lineReturn;
        toReturn = toReturn + "  reducesCorruption: " + reducesCorruption + lineReturn;
        toReturn = toReturn + "  doublesCityGrowthRate: " + doublesCityGrowthRate + lineReturn;
        toReturn = toReturn + "  increasesLuxuryTrade: " + increasesLuxuryTrade + lineReturn;
        toReturn = toReturn + "  allowCityLevel2: " + allowCityLevel2 + lineReturn;
        toReturn = toReturn + "  allowCityLevel3: " + allowCityLevel3 + lineReturn;
        toReturn = toReturn + "  replacesOtherWithThisTag: " + replacesOtherWithThisTag + lineReturn;
        toReturn = toReturn + "  mustBeNearWater: " + mustBeNearWater + lineReturn;
        toReturn = toReturn + "  mustBeNearRiver: " + mustBeNearRiver + lineReturn;
        toReturn = toReturn + "  mayExplodeOrMeltdown: " + mayExplodeOrMeltdown + lineReturn;
        toReturn = toReturn + "  veteranSeaUnits: " + veteranSeaUnits + lineReturn;
        toReturn = toReturn + "  veteranAirUnits: " + veteranAirUnits + lineReturn;
        toReturn = toReturn + "  capitalization: " + capitalization + lineReturn;
        toReturn = toReturn + "  allowWaterTrade: " + allowWaterTrade + lineReturn;
        toReturn = toReturn + "  allowAirTrade: " + allowAirTrade + lineReturn;
        toReturn = toReturn + "  reducesWarWeariness: " + reducesWarWeariness + lineReturn;
        toReturn = toReturn + "  increasesShieldsInWater: " + increasesShieldsInWater + lineReturn;
        toReturn = toReturn + "  increasesFoodInWater: " + increasesFoodInWater + lineReturn;
        toReturn = toReturn + "  increasesTradeInWater: " + increasesTradeInWater + lineReturn;
        toReturn = toReturn + "  charmBarrier: " + charmBarrier + lineReturn;
        toReturn = toReturn + "  stealthAttackBarrier: " + stealthAttackBarrier + lineReturn;
        toReturn = toReturn + "  actsAsGeneralTelepad: " + actsAsGeneralTelepad + lineReturn;
        toReturn = toReturn + "  doublesSacrifice: " + doublesSacrifice + lineReturn;
        toReturn = toReturn + "  canProduceUnits: " + canBuildUnits + lineReturn;
        toReturn = toReturn + "  goodsMustBeInCityRadius: " + goodsMustBeInCityRadius + lineReturn;
        toReturn = toReturn + "other characteristics: " + otherChar + lineReturn;
        toReturn = toReturn + "  coastalInstallation: " + coastalInstallation + lineReturn;
        toReturn = toReturn + "  militaristic: " + militaryInstallation + lineReturn;
        toReturn = toReturn + "  wonder: " + wonder + lineReturn;
        toReturn = toReturn + "  smallWonder: " + smallWonder + lineReturn;
        toReturn = toReturn + "  continentalMoodEffects: " + continentalMoodEffects + lineReturn;
        toReturn = toReturn + "  scientific: " + researchInstallation + lineReturn;
        toReturn = toReturn + "  commercial: " + tradeInstallation + lineReturn;
        toReturn = toReturn + "  expansionist: " + explorationInstallation + lineReturn;
        toReturn = toReturn + "  religious: " + placeOfWorship + lineReturn;
        toReturn = toReturn + "  industrious: " + constructionInstallation + lineReturn;
        toReturn = toReturn + "  agricultural: " + agriculturalInstallation + lineReturn;
        toReturn = toReturn + "  seafaring: " + seafaringInstallation + lineReturn;
        toReturn = toReturn + "small wonder characteristics: " + smallWonderCharacteristics + lineReturn;
        toReturn = toReturn + "  increasesChanceOfLeaderAppearance: " + increasesChanceOfLeaderAppearance + lineReturn;
        toReturn = toReturn + "  buildArmiesWithoutLeader: " + buildArmiesWithoutLeader + lineReturn;
        toReturn = toReturn + "  buildLargerArmies: " + buildLargerArmies + lineReturn;
        toReturn = toReturn + "  treasuryEarnsInterest: " + treasuryEarnsInterest + lineReturn;
        toReturn = toReturn + "  buildSpaceshipParts: " + buildSpaceshipParts + lineReturn;
        toReturn = toReturn + "  forbiddenPalace: " + forbiddenPalace + lineReturn;
        toReturn = toReturn + "  decreasesSuccessOfMissiles: " + decreasesSuccessOfMissiles + lineReturn;
        toReturn = toReturn + "  allowSpyMissions: " + allowSpyMissions + lineReturn;
        toReturn = toReturn + "  allowsHealingInEnemyTerritory: " + allowsHealingInEnemyTerritory + lineReturn;
        toReturn = toReturn + "  goodsMustBeInCityRadius: " + goodsMustBeInCityRadius + lineReturn;
        toReturn = toReturn + "  requiresVictoriousArmy: " + requiresVictoriousArmy + lineReturn;
        toReturn = toReturn + "wonder characteristics: " + wonderCharacteristics + lineReturn;
        toReturn = toReturn + "  safeSeaTravel: " + safeSeaTravel + lineReturn;
        toReturn = toReturn + "  gainAnyTechsKnownByTwoCivs: " + gainAnyTechsKnownByTwoCivs + lineReturn;
        toReturn = toReturn + "  doubleCombatVsBarbarians: " + doubleCombatVsBarbarians + lineReturn;
        toReturn = toReturn + "  increasedShipMovement: " + increasedShipMovement + lineReturn;
        toReturn = toReturn + "  doublesResearchOutput: " + doublesResearchOutput + lineReturn;
        toReturn = toReturn + "  increasedTrade: " + increasedTrade + lineReturn;
        toReturn = toReturn + "  cheaperUpgrades: " + cheaperUpgrades + lineReturn;
        toReturn = toReturn + "  paysTradeMaintenance: " + paysTradeMaintenance + lineReturn;
        toReturn = toReturn + "  allowsNuclearWeapons: " + allowsNuclearWeapons + lineReturn;
        toReturn = toReturn + "  doubleCityGrowth: " + doubleCityGrowth + lineReturn;
        toReturn = toReturn + "  twoFreeAdvances: " + twoFreeAdvances + lineReturn;
        toReturn = toReturn + "  empireReducesWarWeariness: " + empireReducesWarWeariness + lineReturn;
        toReturn = toReturn + "  doubleCityDefences: " + doubleCityDefences + lineReturn;
        toReturn = toReturn + "  allowDiplomaticVictory: " + allowDiplomaticVictory + lineReturn;
        toReturn = toReturn + "  plusTwoShipMovement: " + plusTwoShipMovement + lineReturn;
        toReturn = toReturn + "  questionMarkWonderTrait: " + questionMarkWonderTrait + lineReturn;
        toReturn = toReturn + "  increasedArmyValue: " + increasedArmyValue + lineReturn;
        toReturn = toReturn + "  touristAttraction: " + touristAttraction + lineReturn;
        toReturn = toReturn + "armiesRequired: " + armiesRequired + lineReturn;
        toReturn = toReturn + "flavors: " + flavors + lineReturn;
        for (int i = 0; i < numFlavors; i++)
        {
            toReturn = toReturn + "  flavor " + (i + 1) + ": " + flavours.get(i) + lineReturn;
        }
        toReturn = toReturn + "questionMark: " + questionMark + lineReturn;
        toReturn = toReturn + "unitProduced: " + unitProducedInt + lineReturn;
        toReturn = toReturn + "unitFrequency: " + unitFrequency + lineReturn;
        toReturn = toReturn + lineReturn;
        return toReturn;
    }

    public String compareTo(BIQSection section, String separator)
    {
        if (!(section instanceof BLDG))
            return null;
        BLDG two = (BLDG)section;
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "name: " + name + lineReturn;
        if (!(dataLength == two.getDataLength()))
        {
                toReturn = toReturn + "DataLength: " + dataLength+ separator + two.getDataLength() + lineReturn;
        }
        if (civilopediaEntry.compareTo(two.getCivilopediaEntry()) != 0)
        {
                toReturn = toReturn + "CivilopediaEntry: " + civilopediaEntry + separator + two.getCivilopediaEntry() + lineReturn;
        }
        if (description.compareTo(two.getDescription()) != 0)
        {
                toReturn = toReturn + "Description: " + description + separator + two.getDescription() + lineReturn;
        }
        if (!(doublesHappiness == two.getDoublesHappiness()))
        {
                toReturn = toReturn + "DoublesHappiness: " + doublesHappiness+ separator + two.getDoublesHappiness() + lineReturn;
        }
        if (!(gainInEveryCity == two.getGainInEveryCity()))
        {
                toReturn = toReturn + "GainInEveryCity: " + gainInEveryCity+ separator + two.getGainInEveryCity() + lineReturn;
        }
        if (!(gainOnContinent == two.getGainOnContinent()))
        {
                toReturn = toReturn + "GainOnContinent: " + gainOnContinent+ separator + two.getGainOnContinent() + lineReturn;
        }
        if (!(reqImprovement == two.getReqImprovement()))
        {
                toReturn = toReturn + "ReqImprovement: " + reqImprovement+ separator + two.getReqImprovement() + lineReturn;
        }
        if (!(cost == two.getCost()))
        {
                toReturn = toReturn + "Cost: " + cost+ separator + two.getCost() + lineReturn;
        }
        if (!(culture == two.getCulture()))
        {
                toReturn = toReturn + "Culture: " + culture+ separator + two.getCulture() + lineReturn;
        }
        if (!(bombardDefence == two.getBombardDefence()))
        {
                toReturn = toReturn + "BombardDefence: " + bombardDefence+ separator + two.getBombardDefence() + lineReturn;
        }
        if (!(navalBombardDefence == two.getNavalBombardDefence()))
        {
                toReturn = toReturn + "NavalBombardDefence: " + navalBombardDefence+ separator + two.getNavalBombardDefence() + lineReturn;
        }
        if (!(defenceBonus == two.getDefenceBonus()))
        {
                toReturn = toReturn + "DefenceBonus: " + defenceBonus+ separator + two.getDefenceBonus() + lineReturn;
        }
        if (!(navalDefenceBonus == two.getNavalDefenceBonus()))
        {
                toReturn = toReturn + "NavalDefenceBonus: " + navalDefenceBonus+ separator + two.getNavalDefenceBonus() + lineReturn;
        }
        if (!(maintenanceCost == two.getMaintenanceCost()))
        {
                toReturn = toReturn + "MaintenanceCost: " + maintenanceCost+ separator + two.getMaintenanceCost() + lineReturn;
        }
        if (!(happyAll == two.getHappyAll()))
        {
                toReturn = toReturn + "HappyAll: " + happyAll+ separator + two.getHappyAll() + lineReturn;
        }
        if (!(happy == two.getHappy()))
        {
                toReturn = toReturn + "Happy: " + happy+ separator + two.getHappy() + lineReturn;
        }
        if (!(unhappyAll == two.getUnhappyAll()))
        {
                toReturn = toReturn + "UnhappyAll: " + unhappyAll+ separator + two.getUnhappyAll() + lineReturn;
        }
        if (!(unhappy == two.getUnhappy()))
        {
                toReturn = toReturn + "Unhappy: " + unhappy+ separator + two.getUnhappy() + lineReturn;
        }
        if (!(numReqBuildings == two.getNumReqBuildings()))
        {
                toReturn = toReturn + "NumReqBuildings: " + numReqBuildings+ separator + two.getNumReqBuildings() + lineReturn;
        }
        if (!(airPower == two.getAirPower()))
        {
                toReturn = toReturn + "AirPower: " + airPower+ separator + two.getAirPower() + lineReturn;
        }
        if (!(navalPower == two.getNavalPower()))
        {
                toReturn = toReturn + "NavalPower: " + navalPower+ separator + two.getNavalPower() + lineReturn;
        }
        if (!(pollution == two.getPollution()))
        {
                toReturn = toReturn + "Pollution: " + pollution+ separator + two.getPollution() + lineReturn;
        }
        if (!(production == two.getProduction()))
        {
                toReturn = toReturn + "Production: " + production+ separator + two.getProduction() + lineReturn;
        }
        if (!(reqGovernment == two.getReqGovernment()))
        {
                toReturn = toReturn + "ReqGovernment: " + reqGovernment+ separator + two.getReqGovernment() + lineReturn;
        }
        if (!(spaceshipPart == two.getSpaceshipPart()))
        {
                toReturn = toReturn + "SpaceshipPart: " + spaceshipPart+ separator + two.getSpaceshipPart() + lineReturn;
        }
        if (!(reqAdvanceInt == two.getReqAdvance()))
        {
                toReturn = toReturn + "ReqAdvance: " + reqAdvanceInt+ separator + two.getReqAdvance() + lineReturn;
        }
        if (!(obsoleteByInt == two.getObsoleteBy()))
        {
                toReturn = toReturn + "ObsoleteBy: " + obsoleteByInt + separator + two.getObsoleteBy() + lineReturn;
        }
        if (!(reqResource1 == two.getReqResource1()))
        {
                toReturn = toReturn + "ReqResource1: " + reqResource1+ separator + two.getReqResource1() + lineReturn;
        }
        if (!(reqResource2 == two.getReqResource2()))
        {
                toReturn = toReturn + "ReqResource2: " + reqResource2+ separator + two.getReqResource2() + lineReturn;
        }
        if (!(improvements == two.getImprovements()))
        {
                toReturn = toReturn + "Improvements: " + improvements+ separator + two.getImprovements() + lineReturn;
        }
        if (!(centerOfEmpire == two.getCenterOfEmpire()))
        {
                toReturn = toReturn + "  CenterOfEmpire: " + centerOfEmpire+ separator + two.getCenterOfEmpire() + lineReturn;
        }
        if (!(veteranUnits == two.getVeteranUnits()))
        {
                toReturn = toReturn + "  VeteranUnits: " + veteranUnits+ separator + two.getVeteranUnits() + lineReturn;
        }
        if (!(increasedResearch == two.getIncreasedResearch()))
        {
                toReturn = toReturn + "  IncreasedResearch: " + increasedResearch+ separator + two.getIncreasedResearch() + lineReturn;
        }
        if (!(increasedLuxuries == two.getIncreasedLuxuries()))
        {
                toReturn = toReturn + "  IncreasedLuxuries: " + increasedLuxuries+ separator + two.getIncreasedLuxuries() + lineReturn;
        }
        if (!(increasedTaxes == two.getIncreasedTaxes()))
        {
                toReturn = toReturn + "  IncreasedTaxes: " + increasedTaxes+ separator + two.getIncreasedTaxes() + lineReturn;
        }
        if (!(removePopPollution == two.getRemovePopPollution()))
        {
                toReturn = toReturn + "  RemovePopPollution: " + removePopPollution+ separator + two.getRemovePopPollution() + lineReturn;
        }
        if (!(reduceBldgPollution == two.getReduceBldgPollution()))
        {
                toReturn = toReturn + "  ReduceBldgPollution: " + reduceBldgPollution+ separator + two.getReduceBldgPollution() + lineReturn;
        }
        if (!(resistantToBribery == two.getResistantToBribery()))
        {
                toReturn = toReturn + "  ResistantToBribery: " + resistantToBribery+ separator + two.getResistantToBribery() + lineReturn;
        }
        if (!(reducesCorruption == two.getReducesCorruption()))
        {
                toReturn = toReturn + "  ReducesCorruption: " + reducesCorruption+ separator + two.getReducesCorruption() + lineReturn;
        }
        if (!(doublesCityGrowthRate == two.getDoublesCityGrowthRate()))
        {
                toReturn = toReturn + "  DoublesCityGrowthRate: " + doublesCityGrowthRate+ separator + two.getDoublesCityGrowthRate() + lineReturn;
        }
        if (!(increasesLuxuryTrade == two.getIncreasesLuxuryTrade()))
        {
                toReturn = toReturn + "  IncreasesLuxuryTrade: " + increasesLuxuryTrade+ separator + two.getIncreasesLuxuryTrade() + lineReturn;
        }
        if (!(allowCityLevel2 == two.getAllowCityLevel2()))
        {
                toReturn = toReturn + "  AllowCityLevel2: " + allowCityLevel2+ separator + two.getAllowCityLevel2() + lineReturn;
        }
        if (!(allowCityLevel3 == two.getAllowCityLevel3()))
        {
                toReturn = toReturn + "  AllowCityLevel3: " + allowCityLevel3+ separator + two.getAllowCityLevel3() + lineReturn;
        }
        if (!(replacesOtherWithThisTag == two.getReplacesOtherWithThisTag()))
        {
                toReturn = toReturn + "  ReplacesOtherWithThisTag: " + replacesOtherWithThisTag+ separator + two.getReplacesOtherWithThisTag() + lineReturn;
        }
        if (!(mustBeNearWater == two.getMustBeNearWater()))
        {
                toReturn = toReturn + "  MustBeNearWater: " + mustBeNearWater+ separator + two.getMustBeNearWater() + lineReturn;
        }
        if (!(mustBeNearRiver == two.getMustBeNearRiver()))
        {
                toReturn = toReturn + "  MustBeNearRiver: " + mustBeNearRiver+ separator + two.getMustBeNearRiver() + lineReturn;
        }
        if (!(mayExplodeOrMeltdown == two.getMayExplodeOrMeltdown()))
        {
                toReturn = toReturn + "  MayExplodeOrMeltdown: " + mayExplodeOrMeltdown+ separator + two.getMayExplodeOrMeltdown() + lineReturn;
        }
        if (!(veteranSeaUnits == two.getVeteranSeaUnits()))
        {
                toReturn = toReturn + "  VeteranSeaUnits: " + veteranSeaUnits+ separator + two.getVeteranSeaUnits() + lineReturn;
        }
        if (!(veteranAirUnits == two.getVeteranAirUnits()))
        {
                toReturn = toReturn + "  VeteranAirUnits: " + veteranAirUnits+ separator + two.getVeteranAirUnits() + lineReturn;
        }
        if (!(capitalization == two.getCapitalization()))
        {
                toReturn = toReturn + "  Capitalization: " + capitalization+ separator + two.getCapitalization() + lineReturn;
        }
        if (!(allowWaterTrade == two.getAllowWaterTrade()))
        {
                toReturn = toReturn + "  AllowWaterTrade: " + allowWaterTrade+ separator + two.getAllowWaterTrade() + lineReturn;
        }
        if (!(allowAirTrade == two.getAllowAirTrade()))
        {
                toReturn = toReturn + "  AllowAirTrade: " + allowAirTrade+ separator + two.getAllowAirTrade() + lineReturn;
        }
        if (!(reducesWarWeariness == two.getReducesWarWeariness()))
        {
                toReturn = toReturn + "  ReducesWarWeariness: " + reducesWarWeariness+ separator + two.getReducesWarWeariness() + lineReturn;
        }
        if (!(increasesShieldsInWater == two.getIncreasesShieldsInWater()))
        {
                toReturn = toReturn + "  IncreasesShieldsInWater: " + increasesShieldsInWater+ separator + two.getIncreasesShieldsInWater() + lineReturn;
        }
        if (!(increasesFoodInWater == two.getIncreasesFoodInWater()))
        {
                toReturn = toReturn + "  IncreasesFoodInWater: " + increasesFoodInWater+ separator + two.getIncreasesFoodInWater() + lineReturn;
        }
        if (!(increasesTradeInWater == two.getIncreasesTradeInWater()))
        {
                toReturn = toReturn + "  IncreasesTradeInWater: " + increasesTradeInWater+ separator + two.getIncreasesTradeInWater() + lineReturn;
        }
        if (!(charmBarrier == two.getCharmBarrier()))
        {
                toReturn = toReturn + "  CharmBarrier: " + charmBarrier+ separator + two.getCharmBarrier() + lineReturn;
        }
        if (!(stealthAttackBarrier == two.getStealthAttackBarrier()))
        {
                toReturn = toReturn + "  StealthAttackBarrier: " + stealthAttackBarrier+ separator + two.getStealthAttackBarrier() + lineReturn;
        }
        if (!(actsAsGeneralTelepad == two.getActsAsGeneralTelepad()))
        {
                toReturn = toReturn + "  ActsAsGeneralTelepad: " + actsAsGeneralTelepad+ separator + two.getActsAsGeneralTelepad() + lineReturn;
        }
        if (!(doublesSacrifice == two.getDoublesSacrifice()))
        {
                toReturn = toReturn + "  DoublesSacrifice: " + doublesSacrifice+ separator + two.getDoublesSacrifice() + lineReturn;
        }
        if (!(canBuildUnits == two.getCanBuildUnits()))
        {
                toReturn = toReturn + "  CanBuildUnits: " + canBuildUnits+ separator + two.getCanBuildUnits() + lineReturn;
        }
        if (!(goodsMustBeInCityRadius == two.getGoodsMustBeInCityRadius()))
        {
                toReturn = toReturn + "  GoodsMustBeInCityRadius: " + goodsMustBeInCityRadius+ separator + two.getGoodsMustBeInCityRadius() + lineReturn;
        }
        if (!(otherChar == two.getOtherChar()))
        {
                toReturn = toReturn + "OtherChar: " + otherChar+ separator + two.getOtherChar() + lineReturn;
        }
        if (!(coastalInstallation == two.getCoastalInstallation()))
        {
                toReturn = toReturn + "  CoastalInstallation: " + coastalInstallation+ separator + two.getCoastalInstallation() + lineReturn;
        }
        if (!(militaryInstallation == two.getMilitaryInstallation()))
        {
                toReturn = toReturn + "  MilitaryInstallation: " + militaryInstallation+ separator + two.getMilitaryInstallation() + lineReturn;
        }
        if (!(wonder == two.isWonder()))
        {
                toReturn = toReturn + "  Wonder: " + wonder+ separator + two.isWonder() + lineReturn;
        }
        if (!(smallWonder == two.isSmallWonder()))
        {
                toReturn = toReturn + "  SmallWonder: " + smallWonder+ separator + two.isSmallWonder() + lineReturn;
        }
        if (!(continentalMoodEffects == two.getContinentalMoodEffects()))
        {
                toReturn = toReturn + "  ContinentalMoodEffects: " + continentalMoodEffects+ separator + two.getContinentalMoodEffects() + lineReturn;
        }
        if (!(researchInstallation == two.getResearchInstallation()))
        {
                toReturn = toReturn + "  ResearchInstallation: " + researchInstallation+ separator + two.getResearchInstallation() + lineReturn;
        }
        if (!(tradeInstallation == two.getTradeInstallation()))
        {
                toReturn = toReturn + "  TradeInstallation: " + tradeInstallation+ separator + two.getTradeInstallation() + lineReturn;
        }
        if (!(explorationInstallation == two.getExplorationInstallation()))
        {
                toReturn = toReturn + "  ExplorationInstallation: " + explorationInstallation+ separator + two.getExplorationInstallation() + lineReturn;
        }
        if (!(placeOfWorship == two.getPlaceOfWorship()))
        {
                toReturn = toReturn + "  PlaceOfWorship: " + placeOfWorship+ separator + two.getPlaceOfWorship() + lineReturn;
        }
        if (!(constructionInstallation == two.getConstructionInstallation()))
        {
                toReturn = toReturn + "  ConstructionInstallation: " + constructionInstallation+ separator + two.getConstructionInstallation() + lineReturn;
        }
        if (!(smallWonderCharacteristics == two.getSmallWonderCharacteristics()))
        {
                toReturn = toReturn + "SmallWonderCharacteristics: " + smallWonderCharacteristics+ separator + two.getSmallWonderCharacteristics() + lineReturn;
        }
        if (!(increasesChanceOfLeaderAppearance == two.getIncreasesChanceOfLeaderAppearance()))
        {
                toReturn = toReturn + "  IncreasesChanceOfLeaderAppearance: " + increasesChanceOfLeaderAppearance+ separator + two.getIncreasesChanceOfLeaderAppearance() + lineReturn;
        }
        if (!(buildArmiesWithoutLeader == two.getBuildArmiesWithoutLeader()))
        {
                toReturn = toReturn + "  BuildArmiesWithoutLeader: " + buildArmiesWithoutLeader+ separator + two.getBuildArmiesWithoutLeader() + lineReturn;
        }
        if (!(buildLargerArmies == two.getBuildLargerArmies()))
        {
                toReturn = toReturn + "  BuildLargerArmies: " + buildLargerArmies+ separator + two.getBuildLargerArmies() + lineReturn;
        }
        if (!(treasuryEarnsInterest == two.getTreasuryEarnsInterest()))
        {
                toReturn = toReturn + "  TreasuryEarnsInterest: " + treasuryEarnsInterest+ separator + two.getTreasuryEarnsInterest() + lineReturn;
        }
        if (!(buildSpaceshipParts == two.getBuildSpaceshipParts()))
        {
                toReturn = toReturn + "  BuildSpaceshipParts: " + buildSpaceshipParts+ separator + two.getBuildSpaceshipParts() + lineReturn;
        }
        if (!(forbiddenPalace == two.getForbiddenPalace()))
        {
                toReturn = toReturn + "  ForbiddenPalace: " + forbiddenPalace+ separator + two.getForbiddenPalace() + lineReturn;
        }
        if (!(decreasesSuccessOfMissiles == two.getDecreasesSuccessOfMissiles()))
        {
                toReturn = toReturn + "  DecreasesSuccessOfMissiles: " + decreasesSuccessOfMissiles+ separator + two.getDecreasesSuccessOfMissiles() + lineReturn;
        }
        if (!(allowSpyMissions == two.getAllowSpyMissions()))
        {
                toReturn = toReturn + "  AllowSpyMissions: " + allowSpyMissions+ separator + two.getAllowSpyMissions() + lineReturn;
        }
        if (!(allowsHealingInEnemyTerritory == two.getAllowsHealingInEnemyTerritory()))
        {
                toReturn = toReturn + "  AllowsHealingInEnemyTerritory: " + allowsHealingInEnemyTerritory+ separator + two.getAllowsHealingInEnemyTerritory() + lineReturn;
        }
        if (!(goodsMustBeInCityRadius == two.getGoodsMustBeInCityRadius()))
        {
                toReturn = toReturn + "  GoodsMustBeInCityRadius: " + goodsMustBeInCityRadius+ separator + two.getGoodsMustBeInCityRadius() + lineReturn;
        }
        if (!(requiresVictoriousArmy == two.getRequiresVictoriousArmy()))
        {
                toReturn = toReturn + "  RequiresVictoriousArmy: " + requiresVictoriousArmy+ separator + two.getRequiresVictoriousArmy() + lineReturn;
        }
        if (!(wonderCharacteristics == two.getWonderCharacteristics()))
        {
                toReturn = toReturn + "WonderCharacteristics: " + wonderCharacteristics+ separator + two.getWonderCharacteristics() + lineReturn;
        }
        if (!(safeSeaTravel == two.getSafeSeaTravel()))
        {
                toReturn = toReturn + "  SafeSeaTravel: " + safeSeaTravel+ separator + two.getSafeSeaTravel() + lineReturn;
        }
        if (!(gainAnyTechsKnownByTwoCivs == two.getGainAnyTechsKnownByTwoCivs()))
        {
                toReturn = toReturn + "  GainAnyTechsKnownByTwoCivs: " + gainAnyTechsKnownByTwoCivs+ separator + two.getGainAnyTechsKnownByTwoCivs() + lineReturn;
        }
        if (!(doubleCombatVsBarbarians == two.getDoubleCombatVsBarbarians()))
        {
                toReturn = toReturn + "  DoubleCombatVsBarbarians: " + doubleCombatVsBarbarians+ separator + two.getDoubleCombatVsBarbarians() + lineReturn;
        }
        if (!(increasedShipMovement == two.getIncreasedShipMovement()))
        {
                toReturn = toReturn + "  IncreasedShipMovement: " + increasedShipMovement+ separator + two.getIncreasedShipMovement() + lineReturn;
        }
        if (!(doublesResearchOutput == two.getDoublesResearchOutput()))
        {
                toReturn = toReturn + "  DoublesResearchOutput: " + doublesResearchOutput+ separator + two.getDoublesResearchOutput() + lineReturn;
        }
        if (!(increasedTrade == two.getIncreasedTrade()))
        {
                toReturn = toReturn + "  IncreasedTrade: " + increasedTrade+ separator + two.getIncreasedTrade() + lineReturn;
        }
        if (!(cheaperUpgrades == two.getCheaperUpgrades()))
        {
                toReturn = toReturn + "  CheaperUpgrades: " + cheaperUpgrades+ separator + two.getCheaperUpgrades() + lineReturn;
        }
        if (!(paysTradeMaintenance == two.getPaysTradeMaintenance()))
        {
                toReturn = toReturn + "  PaysTradeMaintenance: " + paysTradeMaintenance+ separator + two.getPaysTradeMaintenance() + lineReturn;
        }
        if (!(allowsNuclearWeapons == two.getAllowsNuclearWeapons()))
        {
                toReturn = toReturn + "  AllowsNuclearWeapons: " + allowsNuclearWeapons+ separator + two.getAllowsNuclearWeapons() + lineReturn;
        }
        if (!(doubleCityGrowth == two.getDoubleCityGrowth()))
        {
                toReturn = toReturn + "  DoubleCityGrowth: " + doubleCityGrowth+ separator + two.getDoubleCityGrowth() + lineReturn;
        }
        if (!(twoFreeAdvances == two.getTwoFreeAdvances()))
        {
                toReturn = toReturn + "  TwoFreeAdvances: " + twoFreeAdvances+ separator + two.getTwoFreeAdvances() + lineReturn;
        }
        if (!(empireReducesWarWeariness == two.getReducesWarWearinessEmpire()))
        {
                toReturn = toReturn + "  ReduceWarWeariness: " + empireReducesWarWeariness+ separator + two.getReducesWarWearinessEmpire() + lineReturn;
        }
        if (!(doubleCityDefences == two.getDoubleCityDefences()))
        {
                toReturn = toReturn + "  DoubleCityDefences: " + doubleCityDefences+ separator + two.getDoubleCityDefences() + lineReturn;
        }
        if (!(allowDiplomaticVictory == two.getAllowDiplomaticVictory()))
        {
                toReturn = toReturn + "  AllowDiplomaticVictory: " + allowDiplomaticVictory+ separator + two.getAllowDiplomaticVictory() + lineReturn;
        }
        if (!(plusTwoShipMovement == two.getPlusTwoShipMovement()))
        {
                toReturn = toReturn + "  PlusTwoShipMovement: " + plusTwoShipMovement+ separator + two.getPlusTwoShipMovement() + lineReturn;
        }
        if (!(questionMarkWonderTrait == two.getQuestionMarkWonderTrait()))
        {
                toReturn = toReturn + "  QuestionMarkWonderTrait: " + questionMarkWonderTrait+ separator + two.getQuestionMarkWonderTrait() + lineReturn;
        }
        if (!(increasedArmyValue == two.getIncreasedArmyValue()))
        {
                toReturn = toReturn + "  IncreasedArmyValue: " + increasedArmyValue+ separator + two.getIncreasedArmyValue() + lineReturn;
        }
        if (!(touristAttraction == two.getTouristAttraction()))
        {
                toReturn = toReturn + "  TouristAttraction: " + touristAttraction+ separator + two.getTouristAttraction() + lineReturn;
        }
        if (!(armiesRequired == two.getArmiesRequired()))
        {
                toReturn = toReturn + "ArmiesRequired: " + armiesRequired+ separator + two.getArmiesRequired() + lineReturn;
        }
        if (!(flavors == two.getFlavors()))
        {
                toReturn = toReturn + "Flavors: " + flavors + separator + two.getFlavors() + lineReturn;
        }
        else  //see if a flavor is checked for a building in only one file
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
                toReturn = toReturn + "QuestionMark: " + questionMark+ separator + two.getQuestionMark() + lineReturn;
        }
        if (!(unitProducedInt == two.getUnitProduced()))
        {
                toReturn = toReturn + "UnitProduced: " + unitProducedInt+ separator + two.getUnitProduced() + lineReturn;
        }
        if (!(unitFrequency == two.getUnitFrequency()))
        {
                toReturn = toReturn + "UnitFrequency: " + unitFrequency+ separator + two.getUnitFrequency() + lineReturn;
        }
        if (toReturn.equals("name: " + name + lineReturn))
        {
            toReturn = "";
        }
        return toReturn;
    }

    public Object getProperty(String string) throws UnsupportedOperationException
    {
        if (string.equals("Name"))
            return this.name;
        throw new UnsupportedOperationException();
    }
    
    public int getIndex() {
        return this.bldgIndex;
    }
}
