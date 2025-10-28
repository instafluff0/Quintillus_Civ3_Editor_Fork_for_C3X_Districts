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
public class RULE extends BIQSection{
    private int dataLength;
    private String townName = "";
    private String cityName = "";
    private String metropolisName;
    //In BIQ: int numSpaceshipParts.  Here, we calculate it dynamically.
    private ArrayList<Integer>SSPartsRequired;
    private int advancedBarbarianInt;
    private PRTO advancedBarbarian;
    private int basicBarbarianInt;
    private PRTO basicBarbarian;
    private int barbarianSeaUnitInt;
    private PRTO barbarianSeaUnit;
    private int citiesForArmy;
    private int chanceOfRioting;
    private int draftTurnPenalty;
    private int shieldCostInGold;
    private int fortressDefenceBonus;
    private int citizensAffectedByHappyFace;
    private int questionMark1;
    private int questionMark2;
    private int forestValueInShields;
    private int shieldValueInGold;
    private int citizenValueInShields;
    private int defaultDifficultyLevel;
    private int battleCreatedUnitInt;
    private PRTO battleCreatedUnit;
    private int buildArmyUnitInt;
    private PRTO buildArmyUnit;
    private int buildingDefensiveBonus;
    private int citizenDefensiveBonus;
    private int defaultMoneyResourceInt;
    private GOOD defaultMoneyResource;
    private int chanceToInterceptAirMissions;
    private int chanceToInterceptStealthMissions;
    private int startingTreasury;
    private int questionMark3;
    private int foodConsumptionPerCitizen;
    private int riverDefensiveBonus;
    private int turnPenaltyForWhip;
    private int scoutInt;
    private PRTO scout;
    private int slaveInt;   //aka captured unit
    private PRTO capturedUnit;  //aka slave
    private int roadMovementRate;
    private int startUnit1Int;
    private PRTO startUnit1;
    private int startUnit2Int;
    private PRTO startUnit2;
    private int WLTKDMinimumPop;
    private int townDefenceBonus;
    private int cityDefenceBonus;
    private int metropolisDefenceBonus;
    private int maxCity1Size;
    private int maxCity2Size;
    private int questionMark4;
    private int fortificationsDefenceBonus;
    //In BIQ: int numCultureLevels.  We calculate it dynamically here.
    private ArrayList<String>culturalLevelNames;    
    private int borderExpansionMultiplier;
    private int borderFactor = 10;
    private int futureTechCost;
    private int goldenAgeDuration;
    private int maximumResearchTime;
    private int minimumResearchTime;
    private int flagUnitInt;
    private PRTO flagUnit;
    private int upgradeCost;
    public RULE(IO baselink)
    {
        super(baselink);
        SSPartsRequired = new ArrayList<Integer>();
        culturalLevelNames = new ArrayList<String>();
    }
    public void trim()
    {
            townName = townName.trim();
            cityName = cityName.trim();
            metropolisName = metropolisName.trim();
            for (int j = 0; j < culturalLevelNames.size(); j++)
            {
                    culturalLevelNames.set(j, culturalLevelNames.get(j).trim());
            }
    }
    public int getDataLength()
    {
        return dataLength;
    }

    public String getTownName()
    {
        return townName;
    }

    public String getCityName()
    {
        return cityName;
    }

    public String getMetropolisName()
    {
        return metropolisName;
    }

    public int getNumSpaceshipParts()
    {
        return SSPartsRequired.size();
    }

    public int getAdvancedBarbarian()
    {
        return advancedBarbarianInt;
    }

    public int getBasicBarbarian()
    {
        return basicBarbarianInt;
    }

    public int getBarbarianSeaUnit()
    {
        return barbarianSeaUnitInt;
    }

    public int getCitiesForArmy()
    {
        return citiesForArmy;
    }

    public int getChanceOfRioting()
    {
        return chanceOfRioting;
    }

    public int getDraftTurnPenalty()
    {
        return draftTurnPenalty;
    }

    public int getShieldCostInGold()
    {
        return shieldCostInGold;
    }

    public int getFortressDefenceBonus()
    {
        return fortressDefenceBonus;
    }

    public int getCitizensAffectedByHappyFace()
    {
        return citizensAffectedByHappyFace;
    }

    public int getQuestionMark1()
    {
        return questionMark1;
    }

    public int getQuestionMark2()
    {
        return questionMark2;
    }

    public int getForestValueInShields()
    {
        return forestValueInShields;
    }

    public int getShieldValueInGold()
    {
        return shieldValueInGold;
    }

    public int getCitizenValueInShields()
    {
        return citizenValueInShields;
    }

    public int getDefaultDifficultyLevel()
    {
        return defaultDifficultyLevel;
    }

    public int getBattleCreatedUnit()
    {
        return battleCreatedUnitInt;
    }

    public int getBuildArmyUnit()
    {
        return buildArmyUnitInt;
    }

    public int getBuildingDefensiveBonus()
    {
        return buildingDefensiveBonus;
    }

    public int getCitizenDefensiveBonus()
    {
        return citizenDefensiveBonus;
    }

    public int getDefaultMoneyResource()
    {
        return defaultMoneyResourceInt;
    }

    public int getChanceToInterceptAirMissions()
    {
        return chanceToInterceptAirMissions;
    }

    public int getChanceToInterceptStealthMissions()
    {
        return chanceToInterceptStealthMissions;
    }

    public int getStartingTreasury()
    {
        return startingTreasury;
    }

    public int getQuestionMark3()
    {
        return questionMark3;
    }

    public int getFoodConsumptionPerCitizen()
    {
        return foodConsumptionPerCitizen;
    }

    public int getRiverDefensiveBonus()
    {
        return riverDefensiveBonus;
    }

    public int getTurnPenaltyForWhip()
    {
        return turnPenaltyForWhip;
    }

    public int getScout()
    {
        return scoutInt;
    }

    public int getSlave()
    {
        return slaveInt;
    }

    public int getRoadMovementRate()
    {
        return roadMovementRate;
    }

    public int getStartUnit1()
    {
        return startUnit1Int;
    }

    public int getStartUnit2()
    {
        return startUnit2Int;
    }

    public int getWLTKDMinimumPop()
    {
        return WLTKDMinimumPop;
    }

    public int getTownDefenceBonus()
    {
        return townDefenceBonus;
    }

    public int getCityDefenceBonus()
    {
        return cityDefenceBonus;
    }

    public int getMetropolisDefenceBonus()
    {
        return metropolisDefenceBonus;
    }

    public int getMaxCity1Size()
    {
        return maxCity1Size;
    }

    public int getMaxCity2Size()
    {
        return maxCity2Size;
    }

    public int getQuestionMark4()
    {
        return questionMark4;
    }

    public int getFortificationsDefenceBonus()
    {
        return fortificationsDefenceBonus;
    }

    public int getNumCulturalLevels()
    {
        return culturalLevelNames.size();
    }

    public int getBorderExpansionMultiplier()
    {
        return borderExpansionMultiplier;
    }

    public int getBorderFactor()
    {
        return borderFactor;
    }

    public int getFutureTechCost()
    {
        return futureTechCost;
    }

    public int getGoldenAgeDuration()
    {
        return goldenAgeDuration;
    }

    public int getMaximumResearchTime()
    {
        return maximumResearchTime;
    }

    public int getMinimumResearchTime()
    {
        return minimumResearchTime;
    }

    public int getFlagUnit()
    {
        return flagUnitInt;
    }

    public int getUpgradeCost()
    {
        return upgradeCost;
    }






    public void setDataLength(int dataLength)
    {
        this.dataLength = dataLength;
    }

    public void setTownName(String townName)
    {
        this.townName = townName;
    }

    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }

    public void setMetropolisName(String metropolisName)
    {
        this.metropolisName = metropolisName;
    }

    public void setAdvancedBarbarian(int advancedBarbarianInt)
    {
        this.advancedBarbarianInt = advancedBarbarianInt;
        if (baseLink.unit != null && advancedBarbarianInt != -1 && baseLink.unit.size() > advancedBarbarianInt) {
            this.advancedBarbarian = baseLink.unit.get(advancedBarbarianInt);
        }
    }

    public void setBasicBarbarian(int basicBarbarianInt)
    {
        this.basicBarbarianInt = basicBarbarianInt;
        if (baseLink.unit != null && basicBarbarianInt != -1 && baseLink.unit.size() > basicBarbarianInt) {
            this.basicBarbarian = baseLink.unit.get(basicBarbarianInt);
        }
    }

    public void setBarbarianSeaUnit(int barbarianSeaUnitInt)
    {
        this.barbarianSeaUnitInt = barbarianSeaUnitInt;
        if (baseLink.unit != null && barbarianSeaUnitInt != -1 && baseLink.unit.size() > barbarianSeaUnitInt) {
            this.barbarianSeaUnit = baseLink.unit.get(barbarianSeaUnitInt);
        }
    }

    public void setCitiesForArmy(int citiesForArmy)
    {
        this.citiesForArmy = citiesForArmy;
    }

    public void setChanceOfRioting(int chanceOfRioting)
    {
        this.chanceOfRioting = chanceOfRioting;
    }

    public void setDraftTurnPenalty(int draftTurnPenalty)
    {
        this.draftTurnPenalty = draftTurnPenalty;
    }

    public void setShieldCostInGold(int shieldCostInGold)
    {
        this.shieldCostInGold = shieldCostInGold;
    }

    public void setFortressDefenceBonus(int fortressDefenceBonus)
    {
        this.fortressDefenceBonus = fortressDefenceBonus;
    }

    public void setCitizensAffectedByHappyFace(int citizensAffectedByHappyFace)
    {
        this.citizensAffectedByHappyFace = citizensAffectedByHappyFace;
    }

    public void setQuestionMark1(int questionMark1)
    {
        this.questionMark1 = questionMark1;
    }

    public void setQuestionMark2(int questionMark2)
    {
        this.questionMark2 = questionMark2;
    }

    public void setForestValueInShields(int forestValueInShields)
    {
        this.forestValueInShields = forestValueInShields;
    }

    public void setShieldValueInGold(int shieldValueInGold)
    {
        this.shieldValueInGold = shieldValueInGold;
    }

    public void setCitizenValueInShields(int citizenValueInShields)
    {
        this.citizenValueInShields = citizenValueInShields;
    }

    public void setDefaultDifficultyLevel(int defaultDifficultyLevel)
    {
        this.defaultDifficultyLevel = defaultDifficultyLevel;
    }

    public void setBattleCreatedUnit(int battleCreatedUnitInt)
    {
        this.battleCreatedUnitInt = battleCreatedUnitInt;
        if (baseLink.unit != null && battleCreatedUnitInt != -1 && baseLink.unit.size() > battleCreatedUnitInt) {
            this.battleCreatedUnit = baseLink.unit.get(battleCreatedUnitInt);
        }
    }

    public void setBuildArmyUnit(int buildArmyUnitInt)
    {
        this.buildArmyUnitInt = buildArmyUnitInt;
        if (baseLink.unit != null && buildArmyUnitInt != -1 && baseLink.unit.size() > buildArmyUnitInt) {
            this.buildArmyUnit = baseLink.unit.get(buildArmyUnitInt);
        }
    }

    public void setBuildingDefensiveBonus(int buildingDefensiveBonus)
    {
        this.buildingDefensiveBonus = buildingDefensiveBonus;
    }

    public void setCitizenDefensiveBonus(int citizenDefensiveBonus)
    {
        this.citizenDefensiveBonus = citizenDefensiveBonus;
    }

    public void setDefaultMoneyResource(int defaultMoneyResource)
    {
        this.defaultMoneyResourceInt = defaultMoneyResource;
        if (baseLink.resource != null && defaultMoneyResourceInt != -1 && baseLink.resource.size() > defaultMoneyResourceInt) {
            this.defaultMoneyResource = baseLink.resource.get(defaultMoneyResourceInt);
        }
    }

    public void setChanceToInterceptAirMissions(int chanceToInterceptAirMissions)
    {
        this.chanceToInterceptAirMissions = chanceToInterceptAirMissions;
    }

    public void setChanceToInterceptStealthMissions(int chanceToInterceptStealthMissions)
    {
        this.chanceToInterceptStealthMissions = chanceToInterceptStealthMissions;
    }

    public void setStartingTreasury(int startingTreasury)
    {
        this.startingTreasury = startingTreasury;
    }

    public void setQuestionMark3(int questionMark3)
    {
        this.questionMark3 = questionMark3;
    }

    public void setFoodConsumptionPerCitizen(int foodConsumptionPerCitizen)
    {
        this.foodConsumptionPerCitizen = foodConsumptionPerCitizen;
    }

    public void setRiverDefensiveBonus(int riverDefensiveBonus)
    {
        this.riverDefensiveBonus = riverDefensiveBonus;
    }

    public void setTurnPenaltyForWhip(int turnPenaltyForWhip)
    {
        this.turnPenaltyForWhip = turnPenaltyForWhip;
    }

    public void setScout(int scoutInt)
    {
        this.scoutInt = scoutInt;
        if (baseLink.unit != null && scoutInt != -1 && baseLink.unit.size() > scoutInt) {
            this.scout = baseLink.unit.get(scoutInt);
        }
    }

    public void setSlave(int slaveInt)
    {
        this.slaveInt = slaveInt;
        if (baseLink.unit != null && slaveInt != -1 && baseLink.unit.size() > slaveInt) {
            this.capturedUnit = baseLink.unit.get(slaveInt);
        }
    }

    public void setRoadMovementRate(int roadMovementRate)
    {
        this.roadMovementRate = roadMovementRate;
    }

    public void setStartUnit1(int startUnit1Int)
    {
        this.startUnit1Int = startUnit1Int;
        if (baseLink.unit != null && startUnit1Int != -1 && baseLink.unit.size() > startUnit1Int) {
            this.startUnit1 = baseLink.unit.get(startUnit1Int);
        }
    }

    public void setStartUnit2(int startUnit2Int)
    {
        this.startUnit2Int = startUnit2Int;
        if (baseLink.unit != null && startUnit2Int != -1 && baseLink.unit.size() > startUnit2Int) {
            this.startUnit2 = baseLink.unit.get(startUnit2Int);
        }
    }

    public void setWLTKDMinimumPop(int WLTKDMinimumPop)
    {
        this.WLTKDMinimumPop = WLTKDMinimumPop;
    }

    public void setTownDefenceBonus(int townDefenceBonus)
    {
        this.townDefenceBonus = townDefenceBonus;
    }

    public void setCityDefenceBonus(int cityDefenceBonus)
    {
        this.cityDefenceBonus = cityDefenceBonus;
    }

    public void setMetropolisDefenceBonus(int metropolisDefenceBonus)
    {
        this.metropolisDefenceBonus = metropolisDefenceBonus;
    }

    public void setMaxCity1Size(int maxCity1Size)
    {
        this.maxCity1Size = maxCity1Size;
    }

    public void setMaxCity2Size(int maxCity2Size)
    {
        this.maxCity2Size = maxCity2Size;
    }

    public void setQuestionMark4(int questionMark4)
    {
        this.questionMark4 = questionMark4;
    }

    public void setFortificationsDefenceBonus(int fortificationsDefenceBonus)
    {
        this.fortificationsDefenceBonus = fortificationsDefenceBonus;
    }

    public void setBorderExpansionMultiplier(int borderExpansionMultiplier)
    {
        this.borderExpansionMultiplier = borderExpansionMultiplier;
    }

    public void setBorderFactor(int borderFactor)
    {
        this.borderFactor = borderFactor;
    }

    public void setFutureTechCost(int futureTechCost)
    {
        this.futureTechCost = futureTechCost;
    }

    public void setGoldenAgeDuration(int goldenAgeDuration)
    {
        this.goldenAgeDuration = goldenAgeDuration;
    }

    public void setMaximumResearchTime(int maximumResearchTime)
    {
        this.maximumResearchTime = maximumResearchTime;
    }

    public void setMinimumResearchTime(int minimumResearchTime)
    {
        this.minimumResearchTime = minimumResearchTime;
    }

    public void setFlagUnit(int flagUnitInt)
    {
        this.flagUnitInt = flagUnitInt;
        if (baseLink.unit != null && flagUnitInt != -1 && baseLink.unit.size() > flagUnitInt) {
            this.flagUnit = baseLink.unit.get(flagUnitInt);
        }
    }

    public void setUpgradeCost(int upgradeCost)
    {
        this.upgradeCost = upgradeCost;
    }
    
    /**
     * Only the main import can add spaceship parts.
     * @param numberRequired 
     */
    public void addSpaceshipPart(int numberRequired, boolean sameDataSize)
    {
        this.SSPartsRequired.add(numberRequired);
        if (!sameDataSize) {
            this.dataLength+=4;
        }
    }
    
    public int getNumberOfSpaceshipPartsRequired(int index)
    {
        return this.SSPartsRequired.get(index);
    }
    
    public void updateNumPartsRequired(int ssPart, int newNumRequired)
    {
        this.SSPartsRequired.set(ssPart, newNumRequired);
    }
    
    public void removeSSPart(int ssPartIndex) {
        this.SSPartsRequired.remove(ssPartIndex);
        this.dataLength-=4;
    }
    
    public void addCultureLevelName(String name)
    {
        this.culturalLevelNames.add(name);
    }
    
    public void removeCultureLevel(int index)
    {
        this.culturalLevelNames.remove(index);
    }
    
    String getCultureLevelName(int index)
    {
        return this.culturalLevelNames.get(index);
    }
    
    /**
     * Returns an unmodifiable copy of the culture level names list.
     * This can be used to display them elsewhere.
     * @return 
     */
    public List<String> getCultureLevels()
    {
        return Collections.unmodifiableList(culturalLevelNames);
    }
    
    public void convertToPTWFromVanilla() {
        this.setFlagUnit(-1);
        dataLength+=4;
    }
    
    public void convertToConquestsFromPTW()
    {
        this.setUpgradeCost(3);
        dataLength+=4;
    }
    
    public void handleDeletedUnit(int index)
    {
        if (advancedBarbarianInt  == index) {
            advancedBarbarianInt = -1;
            advancedBarbarian = null;
        }
        else if (advancedBarbarianInt  > index)
            advancedBarbarianInt--;
        
        if (barbarianSeaUnitInt  == index) {
            barbarianSeaUnitInt = -1;
            barbarianSeaUnit = null;
        }
        else if (barbarianSeaUnitInt  > index)
            barbarianSeaUnitInt--;
        
        if (basicBarbarianInt  == index) {
            basicBarbarianInt = -1;
            basicBarbarian = null;
        }
        else if (basicBarbarianInt  > index)
            basicBarbarianInt--;
        
        if (battleCreatedUnitInt  == index) {
            battleCreatedUnitInt = -1;
            battleCreatedUnit = null;
        }
        else if (battleCreatedUnitInt  > index)
            battleCreatedUnitInt--;
        
        if (buildArmyUnitInt  == index) {
            buildArmyUnitInt = -1;
            buildArmyUnit = null;
        }
        else if (buildArmyUnitInt  > index)
            buildArmyUnitInt--;
        
        if (flagUnitInt  == index) {
            flagUnitInt = -1;
            flagUnit = null;
        }
        else if (flagUnitInt  > index)
            flagUnitInt--;
        
        if (scoutInt  == index) {
            scoutInt = -1;
            scout = null;
        }
        else if (scoutInt  > index)
            scoutInt--;
        
        if (slaveInt  == index) {
            slaveInt = -1;
            capturedUnit = null;
        }
        else if (slaveInt  > index)
            slaveInt--;
        
        if (startUnit1Int  == index) {
            startUnit1Int = -1;
            startUnit1 = null;
        }
        else if (startUnit1Int  > index)
            startUnit1Int--;
        
        if (startUnit2Int  == index) {
            startUnit2Int = -1;
            startUnit2 = null;
        }
        else if (startUnit2Int  > index)
            startUnit2Int--;
    }
    
    public void handleSwappedUnit() {
        if (advancedBarbarian != null) {
            advancedBarbarianInt = advancedBarbarian.getIndex();
        }
        if (basicBarbarian != null) {
            basicBarbarianInt = basicBarbarian.getIndex();
        }
        if (barbarianSeaUnit != null) {
            barbarianSeaUnitInt = barbarianSeaUnit.getIndex();
        }
        if (battleCreatedUnit != null) {
            battleCreatedUnitInt = battleCreatedUnit.getIndex();
        }
        if (buildArmyUnit != null) {
            buildArmyUnitInt = buildArmyUnit.getIndex();
        }
        if (scout != null) {
            scoutInt = scout.getIndex();
        }
        if (capturedUnit != null) {
            slaveInt = capturedUnit.getIndex();
        }
        if (startUnit1 != null) {
            startUnit1Int = startUnit1.getIndex();
        }
        if (startUnit2 != null) {
            startUnit2Int = startUnit2.getIndex();
        }
        if (flagUnit != null) {
            flagUnitInt = flagUnit.getIndex();
        }
    }
    
    public void handleSwappedGood() {
        if (defaultMoneyResource != null) {
            defaultMoneyResourceInt = defaultMoneyResource.getIndex();
        }
    }
    
    public void handleDeletedDifficulty(int index)
    {
        if (index != 0 && index <= defaultDifficultyLevel)   //decrement the default difficulty
            defaultDifficultyLevel--;
    }

    public String toEnglish()
    {
        return toString();
    }

    @Override
    public String toString()
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn =  "dataLength: " + dataLength + lineReturn;
        toReturn = toReturn + "townName: " + townName + lineReturn;
        toReturn = toReturn + "cityName: " + cityName + lineReturn;
        toReturn = toReturn + "metropolisName: " + metropolisName + lineReturn;
        toReturn = toReturn + "numSpaceshipParts: " + getNumSpaceshipParts() + lineReturn;
        for (int i = 0; i < getNumSpaceshipParts(); i++)
        {
            toReturn = toReturn + "number of parts " + i + " required: "  + SSPartsRequired.get(i) + lineReturn;
        }
        toReturn = toReturn + "advancedBarbarian: " + advancedBarbarianInt + lineReturn;
        toReturn = toReturn + "basicBarbarian: " + basicBarbarianInt + lineReturn;
        toReturn = toReturn + "barbarianSeaUnit: " + barbarianSeaUnitInt + lineReturn;
        toReturn = toReturn + "citiesForArmy: " + citiesForArmy + lineReturn;
        toReturn = toReturn + "chanceOfRioting: " + chanceOfRioting + lineReturn;
        toReturn = toReturn + "draftTurnPenalty: " + draftTurnPenalty + lineReturn;
        toReturn = toReturn + "shieldCostInGold: " + shieldCostInGold + lineReturn;
        toReturn = toReturn + "fortressDefenceBonus: " + fortressDefenceBonus + lineReturn;
        toReturn = toReturn + "citizensAffectedByHappyFace: " + citizensAffectedByHappyFace + lineReturn;
        toReturn = toReturn + "questionMark1: " + questionMark1 + lineReturn;
        toReturn = toReturn + "questionMark2: " + questionMark2 + lineReturn;
        toReturn = toReturn + "forestValueInShields: " + forestValueInShields + lineReturn;
        toReturn = toReturn + "shieldValueInGold: " + shieldValueInGold + lineReturn;
        toReturn = toReturn + "citizenValueInShields: " + citizenValueInShields + lineReturn;
        toReturn = toReturn + "defaultDifficultyLevel: " + defaultDifficultyLevel + lineReturn;
        toReturn = toReturn + "battleCreatedUnit: " + battleCreatedUnitInt + lineReturn;
        toReturn = toReturn + "buildArmyUnit: " + buildArmyUnitInt + lineReturn;
        toReturn = toReturn + "buildingDefensiveBonus: " + buildingDefensiveBonus + lineReturn;
        toReturn = toReturn + "citizenDefensiveBonus: " + citizenDefensiveBonus + lineReturn;
        toReturn = toReturn + "defaultMoneyResource: " + defaultMoneyResourceInt + lineReturn;
        toReturn = toReturn + "chanceToInterceptAirMissions: " + chanceToInterceptAirMissions + lineReturn;
        toReturn = toReturn + "chanceToInterceptStealthMissions: " + chanceToInterceptStealthMissions + lineReturn;
        toReturn = toReturn + "startingTreasury: " + startingTreasury + lineReturn;
        toReturn = toReturn + "questionMark3: " + questionMark3 + lineReturn;
        toReturn = toReturn + "foodConsumptionPerCitizen: " + foodConsumptionPerCitizen + lineReturn;
        toReturn = toReturn + "riverDefensiveBonus: " + riverDefensiveBonus + lineReturn;
        toReturn = toReturn + "turnPenaltyForWhip: " + turnPenaltyForWhip + lineReturn;
        toReturn = toReturn + "scout: " + scoutInt + lineReturn;
        toReturn = toReturn + "slave: " + slaveInt + lineReturn;
        toReturn = toReturn + "roadMovementRate: " + roadMovementRate + lineReturn;
        toReturn = toReturn + "startUnit1: " + startUnit1Int + lineReturn;
        toReturn = toReturn + "startUnit2: " + startUnit2Int + lineReturn;
        toReturn = toReturn + "WLTKDMinimumPop: " + WLTKDMinimumPop + lineReturn;
        toReturn = toReturn + "townDefenceBonus: " + townDefenceBonus + lineReturn;
        toReturn = toReturn + "cityDefenceBonus: " + cityDefenceBonus + lineReturn;
        toReturn = toReturn + "metropolisDefenceBonus: " + metropolisDefenceBonus + lineReturn;
        toReturn = toReturn + "maxCity1Size: " + maxCity1Size + lineReturn;
        toReturn = toReturn + "maxCity2Size: " + maxCity2Size + lineReturn;
        toReturn = toReturn + "questionMark4: " + questionMark4 + lineReturn;
        toReturn = toReturn + "fortificationsDefenceBonus: " + fortificationsDefenceBonus + lineReturn;
        toReturn = toReturn + "numCulturalLevels: " + getNumCulturalLevels() + lineReturn;
        for (int i = 0; i < getNumCulturalLevels(); i++)
        {
            toReturn = toReturn + culturalLevelNames.get(i) + lineReturn;
        }
        toReturn = toReturn + "borderExpansionMultiplier: " + borderExpansionMultiplier + lineReturn;
        toReturn = toReturn + "borderFactor: " + borderFactor + lineReturn;
        toReturn = toReturn + "futureTechCost: " + futureTechCost + lineReturn;
        toReturn = toReturn + "goldenAgeDuration: " + goldenAgeDuration + lineReturn;
        toReturn = toReturn + "maximumResearchTime: " + maximumResearchTime + lineReturn;
        toReturn = toReturn + "minimumResearchTime: " + minimumResearchTime + lineReturn;
        toReturn = toReturn + "flagUnit: " + flagUnitInt + lineReturn;
        toReturn = toReturn + "upgradeCost: " + upgradeCost + lineReturn;
        toReturn = toReturn + lineReturn;
        return toReturn;
    }

    public String compareTo(BIQSection section, String separator)
    {
        if (!(section instanceof RULE))
            return null;
        RULE two = (RULE)section;
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "";
        if (!(dataLength == two.getDataLength()))
        {
                toReturn = toReturn + "DataLength: " + dataLength + separator + two.getDataLength() + lineReturn;
        }
        if (townName.compareTo(two.getTownName()) != 0)
        {
                toReturn = toReturn + "TownName: " + townName + separator + two.getTownName() + lineReturn;
        }
        if (cityName.compareTo(two.getCityName()) != 0)
        {
                toReturn = toReturn + "CityName: " + cityName + separator + two.getCityName() + lineReturn;
        }
        if (metropolisName.compareTo(two.getMetropolisName()) != 0)
        {
                toReturn = toReturn + "MetropolisName: " + metropolisName + separator + two.getMetropolisName() + lineReturn;
        }
        if (!(getNumSpaceshipParts() == two.getNumSpaceshipParts()))
        {
                toReturn = toReturn + "NumSpaceshipParts: " + getNumSpaceshipParts() + separator + two.getNumSpaceshipParts() + lineReturn;
        }
        if (!(advancedBarbarianInt == two.getAdvancedBarbarian()))
        {
                toReturn = toReturn + "AdvancedBarbarian: " + advancedBarbarianInt + separator + two.getAdvancedBarbarian() + lineReturn;
        }
        if (!(basicBarbarianInt == two.getBasicBarbarian()))
        {
                toReturn = toReturn + "BasicBarbarian: " + basicBarbarianInt + separator + two.getBasicBarbarian() + lineReturn;
        }
        if (!(barbarianSeaUnitInt == two.getBarbarianSeaUnit()))
        {
                toReturn = toReturn + "BarbarianSeaUnit: " + barbarianSeaUnitInt + separator + two.getBarbarianSeaUnit() + lineReturn;
        }
        if (!(citiesForArmy == two.getCitiesForArmy()))
        {
                toReturn = toReturn + "CitiesForArmy: " + citiesForArmy + separator + two.getCitiesForArmy() + lineReturn;
        }
        if (!(chanceOfRioting == two.getChanceOfRioting()))
        {
                toReturn = toReturn + "ChanceOfRioting: " + chanceOfRioting + separator + two.getChanceOfRioting() + lineReturn;
        }
        if (!(draftTurnPenalty == two.getDraftTurnPenalty()))
        {
                toReturn = toReturn + "DraftTurnPenalty: " + draftTurnPenalty + separator + two.getDraftTurnPenalty() + lineReturn;
        }
        if (!(shieldCostInGold == two.getShieldCostInGold()))
        {
                toReturn = toReturn + "ShieldCostInGold: " + shieldCostInGold + separator + two.getShieldCostInGold() + lineReturn;
        }
        if (!(fortressDefenceBonus == two.getFortressDefenceBonus()))
        {
                toReturn = toReturn + "FortressDefenceBonus: " + fortressDefenceBonus + separator + two.getFortressDefenceBonus() + lineReturn;
        }
        if (!(citizensAffectedByHappyFace == two.getCitizensAffectedByHappyFace()))
        {
                toReturn = toReturn + "CitizensAffectedByHappyFace: " + citizensAffectedByHappyFace + separator + two.getCitizensAffectedByHappyFace() + lineReturn;
        }
        if (!(questionMark1 == two.getQuestionMark1()))
        {
                toReturn = toReturn + "QuestionMark1: " + questionMark1 + separator + two.getQuestionMark1() + lineReturn;
        }
        if (!(questionMark2 == two.getQuestionMark2()))
        {
                toReturn = toReturn + "QuestionMark2: " + questionMark2 + separator + two.getQuestionMark2() + lineReturn;
        }
        if (!(forestValueInShields == two.getForestValueInShields()))
        {
                toReturn = toReturn + "ForestValueInShields: " + forestValueInShields + separator + two.getForestValueInShields() + lineReturn;
        }
        if (!(shieldValueInGold == two.getShieldValueInGold()))
        {
                toReturn = toReturn + "ShieldValueInGold: " + shieldValueInGold + separator + two.getShieldValueInGold() + lineReturn;
        }
        if (!(citizenValueInShields == two.getCitizenValueInShields()))
        {
                toReturn = toReturn + "CitizenValueInShields: " + citizenValueInShields + separator + two.getCitizenValueInShields() + lineReturn;
        }
        if (!(defaultDifficultyLevel == two.getDefaultDifficultyLevel()))
        {
                toReturn = toReturn + "DefaultDifficultyLevel: " + defaultDifficultyLevel + separator + two.getDefaultDifficultyLevel() + lineReturn;
        }
        if (!(battleCreatedUnitInt == two.getBattleCreatedUnit()))
        {
                toReturn = toReturn + "BattleCreatedUnit: " + battleCreatedUnitInt + separator + two.getBattleCreatedUnit() + lineReturn;
        }
        if (!(buildArmyUnitInt == two.getBuildArmyUnit()))
        {
                toReturn = toReturn + "BuildArmyUnit: " + buildArmyUnitInt + separator + two.getBuildArmyUnit() + lineReturn;
        }
        if (!(buildingDefensiveBonus == two.getBuildingDefensiveBonus()))
        {
                toReturn = toReturn + "BuildingDefensiveBonus: " + buildingDefensiveBonus + separator + two.getBuildingDefensiveBonus() + lineReturn;
        }
        if (!(citizenDefensiveBonus == two.getCitizenDefensiveBonus()))
        {
                toReturn = toReturn + "CitizenDefensiveBonus: " + citizenDefensiveBonus + separator + two.getCitizenDefensiveBonus() + lineReturn;
        }
        if (!(defaultMoneyResourceInt == two.getDefaultMoneyResource()))
        {
                toReturn = toReturn + "DefaultMoneyResource: " + defaultMoneyResourceInt + separator + two.getDefaultMoneyResource() + lineReturn;
        }
        if (!(chanceToInterceptAirMissions == two.getChanceToInterceptAirMissions()))
        {
                toReturn = toReturn + "ChanceToInterceptAirMissions: " + chanceToInterceptAirMissions + separator + two.getChanceToInterceptAirMissions() + lineReturn;
        }
        if (!(chanceToInterceptStealthMissions == two.getChanceToInterceptStealthMissions()))
        {
                toReturn = toReturn + "ChanceToInterceptStealthMissions: " + chanceToInterceptStealthMissions + separator + two.getChanceToInterceptStealthMissions() + lineReturn;
        }
        if (!(startingTreasury == two.getStartingTreasury()))
        {
                toReturn = toReturn + "StartingTreasury: " + startingTreasury + separator + two.getStartingTreasury() + lineReturn;
        }
        if (!(questionMark3 == two.getQuestionMark3()))
        {
                toReturn = toReturn + "QuestionMark3: " + questionMark3 + separator + two.getQuestionMark3() + lineReturn;
        }
        if (!(foodConsumptionPerCitizen == two.getFoodConsumptionPerCitizen()))
        {
                toReturn = toReturn + "FoodConsumptionPerCitizen: " + foodConsumptionPerCitizen + separator + two.getFoodConsumptionPerCitizen() + lineReturn;
        }
        if (!(riverDefensiveBonus == two.getRiverDefensiveBonus()))
        {
                toReturn = toReturn + "RiverDefensiveBonus: " + riverDefensiveBonus + separator + two.getRiverDefensiveBonus() + lineReturn;
        }
        if (!(turnPenaltyForWhip == two.getTurnPenaltyForWhip()))
        {
                toReturn = toReturn + "TurnPenaltyForWhip: " + turnPenaltyForWhip + separator + two.getTurnPenaltyForWhip() + lineReturn;
        }
        if (!(scoutInt == two.getScout()))
        {
                toReturn = toReturn + "Scout: " + scoutInt + separator + two.getScout() + lineReturn;
        }
        if (!(slaveInt == two.getSlave()))
        {
                toReturn = toReturn + "Slave: " + slaveInt + separator + two.getSlave() + lineReturn;
        }
        if (!(roadMovementRate == two.getRoadMovementRate()))
        {
                toReturn = toReturn + "RoadMovementRate: " + roadMovementRate + separator + two.getRoadMovementRate() + lineReturn;
        }
        if (!(startUnit1Int == two.getStartUnit1()))
        {
                toReturn = toReturn + "StartUnit1: " + startUnit1Int + separator + two.getStartUnit1() + lineReturn;
        }
        if (!(startUnit2Int == two.getStartUnit2()))
        {
                toReturn = toReturn + "StartUnit2: " + startUnit2Int + separator + two.getStartUnit2() + lineReturn;
        }
        if (!(WLTKDMinimumPop == two.getWLTKDMinimumPop()))
        {
                toReturn = toReturn + "WLTKDMinimumPop: " + WLTKDMinimumPop + separator + two.getWLTKDMinimumPop() + lineReturn;
        }
        if (!(townDefenceBonus == two.getTownDefenceBonus()))
        {
                toReturn = toReturn + "TownDefenceBonus: " + townDefenceBonus + separator + two.getTownDefenceBonus() + lineReturn;
        }
        if (!(cityDefenceBonus == two.getCityDefenceBonus()))
        {
                toReturn = toReturn + "CityDefenceBonus: " + cityDefenceBonus + separator + two.getCityDefenceBonus() + lineReturn;
        }
        if (!(metropolisDefenceBonus == two.getMetropolisDefenceBonus()))
        {
                toReturn = toReturn + "MetropolisDefenceBonus: " + metropolisDefenceBonus + separator + two.getMetropolisDefenceBonus() + lineReturn;
        }
        if (!(maxCity1Size == two.getMaxCity1Size()))
        {
                toReturn = toReturn + "MaxCity1Size: " + maxCity1Size + separator + two.getMaxCity1Size() + lineReturn;
        }
        if (!(maxCity2Size == two.getMaxCity2Size()))
        {
                toReturn = toReturn + "MaxCity2Size: " + maxCity2Size + separator + two.getMaxCity2Size() + lineReturn;
        }
        if (!(questionMark4 == two.getQuestionMark4()))
        {
                toReturn = toReturn + "QuestionMark4: " + questionMark4 + separator + two.getQuestionMark4() + lineReturn;
        }
        if (!(fortificationsDefenceBonus == two.getFortificationsDefenceBonus()))
        {
                toReturn = toReturn + "FortificationsDefenceBonus: " + fortificationsDefenceBonus + separator + two.getFortificationsDefenceBonus() + lineReturn;
        }
        if (!(getNumCulturalLevels() == two.getNumCulturalLevels()))
        {
                toReturn = toReturn + "NumCulturalLevels: " + getNumCulturalLevels() + separator + two.getNumCulturalLevels() + lineReturn;
        }
        if (!(borderExpansionMultiplier == two.getBorderExpansionMultiplier()))
        {
                toReturn = toReturn + "BorderExpansionMultiplier: " + borderExpansionMultiplier + separator + two.getBorderExpansionMultiplier() + lineReturn;
        }
        if (!(borderFactor == two.getBorderFactor()))
        {
                toReturn = toReturn + "BorderFactor: " + borderFactor + separator + two.getBorderFactor() + lineReturn;
        }
        if (!(futureTechCost == two.getFutureTechCost()))
        {
                toReturn = toReturn + "FutureTechCost: " + futureTechCost + separator + two.getFutureTechCost() + lineReturn;
        }
        if (!(goldenAgeDuration == two.getGoldenAgeDuration()))
        {
                toReturn = toReturn + "GoldenAgeDuration: " + goldenAgeDuration + separator + two.getGoldenAgeDuration() + lineReturn;
        }
        if (!(maximumResearchTime == two.getMaximumResearchTime()))
        {
                toReturn = toReturn + "MaximumResearchTime: " + maximumResearchTime + separator + two.getMaximumResearchTime() + lineReturn;
        }
        if (!(minimumResearchTime == two.getMinimumResearchTime()))
        {
                toReturn = toReturn + "MinimumResearchTime: " + minimumResearchTime + separator + two.getMinimumResearchTime() + lineReturn;
        }
        if (!(flagUnitInt == two.getFlagUnit()))
        {
                toReturn = toReturn + "FlagUnit: " + flagUnitInt + separator + two.getFlagUnit() + lineReturn;
        }
        if (!(upgradeCost == two.getUpgradeCost()))
        {
                toReturn = toReturn + "UpgradeCost: " + upgradeCost + separator + two.getUpgradeCost() + lineReturn;
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
