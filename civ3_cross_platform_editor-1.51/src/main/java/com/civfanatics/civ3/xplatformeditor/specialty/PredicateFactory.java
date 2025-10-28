
package com.civfanatics.civ3.xplatformeditor.specialty;

import com.civfanatics.civ3.biqFile.BIQSection;
import com.civfanatics.civ3.biqFile.BLDG;
import com.civfanatics.civ3.biqFile.ERAS;
import com.civfanatics.civ3.biqFile.FLAV;
import com.civfanatics.civ3.biqFile.GOOD;
import com.civfanatics.civ3.biqFile.GOVT;
import com.civfanatics.civ3.biqFile.PRTO;
import com.civfanatics.civ3.biqFile.RACE;
import com.civfanatics.civ3.biqFile.TECH;
import com.civfanatics.civ3.biqFile.TERR;
import com.civfanatics.civ3.xplatformeditor.Main;
import com.civfanatics.civ3.xplatformeditor.utils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import org.apache.log4j.Logger;

/**
 * A Predicate Factory so that we can keep our predicates in one place, and not have
 * them take up lots of our main tab files.  With only a fraction of fields filterable,
 * the PRTO one was already up to 300 lines or about 1/10th the unit tab, so reaching
 * half was not outside the realm of possibility.
 * Credit to https://stackoverflow.com/questions/26020561/pass-an-extra-second-argument-to-guava-predicate
 * for the idea of the factory, solving the quandry of how to get context into a non-inline predicate.
 * 
 * I explored caching the predicates (which took after-first invocation time down from 2 microseconds to
 * about 150 nanoseconds), but they no longer updated properly based on the filter text.  With only 2 microseconds
 * per creation, let's just go with that.  The actual filtering takes about 300 microseconds, so we would be
 * saving less than 1% anyway, and besides, no one can type at even 1000 characters per second.  Measurements
 * on a Core i5 2500k at 3.6 GHz.
 * @author Andrew
 */
public class PredicateFactory {
    
    static Logger logger = Logger.getLogger(PredicateFactory.class);
    
    static List<String> eqLtGt = Arrays.asList("=", ">", "<");
    
    static BiPredicate<BLDG, String> reqBuilding = (bldg, buildingName) -> {
        buildingName = buildingName.substring(1);
        if (bldg.getReqImprovement() == -1) {
            return buildingName.equalsIgnoreCase("None");
        }
        else {
            BLDG theBuilding = Main.getCurrentBIQ().buildings.get(bldg.getReqImprovement());
            return theBuilding.getName().equalsIgnoreCase(buildingName);
        }
    };
    
    @Deprecated
    static BiPredicate<BLDG, String> reqBuildingInefficient = (bldg, buildingName) -> {
        buildingName = buildingName.substring(1);
        if (buildingName.equalsIgnoreCase("None")) {
            return bldg.getReqImprovement() == -1;
        }
        else {
            int b = 0;
            for (BLDG building : Main.getCurrentBIQ().buildings) {
                if (building.getName().equalsIgnoreCase(buildingName)) {
                    return bldg.getGainInEveryCity() == b;
                }
                b++;
            }
            return false;
        }
    };
    
    public static BiPredicate<BLDG, String> createBLDGFilter() {
        BiPredicate<BLDG, String> nameContainsQuery = (bldg, query) -> {
          return bldg.getName().toLowerCase().contains(query.toLowerCase());
        };
        BiPredicate<BLDG, String> cost = (bldg, query) -> {
          return evaluateIntegerQuery(bldg.getCost(), query);
        };
        BiPredicate<BLDG, String> maintenance = (bldg, query) -> {
          return evaluateIntegerQuery(bldg.getMaintenanceCost(), query);
        };
        BiPredicate<BLDG, String> culture = (bldg, query) -> {
          return evaluateIntegerQuery(bldg.getCulture(), query);
        };
        BiPredicate<BLDG, String> productionBonus = (bldg, query) -> {
          return evaluateIntegerQuery(bldg.getProduction(), query);
        };
        BiPredicate<BLDG, String> pollution = (bldg, query) -> {
          return evaluateIntegerQuery(bldg.getPollution(), query);
        };
        BiPredicate<BLDG, String> bldgType = (bldg, query) -> {
          if (!query.startsWith("=")) {
              return false;
          }
          query = query.substring(1);
          if (query.equalsIgnoreCase("Wonder")) {
              return bldg.isWonder();
          }
          else if (query.equalsIgnoreCase("Small Wonder")) {
              return bldg.isSmallWonder();
          }
          else if (query.equalsIgnoreCase("Improvement")) {
              return !bldg.isWonder() && !bldg.isSmallWonder();
          }
          return false;
        };
        BiPredicate<BLDG, String> landBombard = (bldg, query) -> {
          return evaluateIntegerQuery(bldg.getBombardDefence(), query);
        };
        BiPredicate<BLDG, String> navalBombard = (bldg, query) -> {
          return evaluateIntegerQuery(bldg.getNavalBombardDefence(), query);
        };
        BiPredicate<BLDG, String> airAttack = (bldg, query) -> {
          return evaluateIntegerQuery(bldg.getAirPower(), query);
        };
        BiPredicate<BLDG, String> seaAttack = (bldg, query) -> {
            //AKA Naval Power.  I am confused over Sea Bombard, Sea Attack, and Naval Defence.
          return evaluateIntegerQuery(bldg.getNavalPower(), query);
        };
        BiPredicate<BLDG, String> defenceBonus = (bldg, query) -> {
          return evaluateIntegerQuery(bldg.getDefenceBonus(), query);
        };
        BiPredicate<BLDG, String> navalDefence = (bldg, query) -> {
          return evaluateIntegerQuery(bldg.getNavalDefenceBonus(), query);
        };
        BiPredicate<BLDG, String> veteranLand = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getVeteranUnits(), query);
        };
        BiPredicate<BLDG, String> veteranSea = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getVeteranSeaUnits(), query);
        };
        BiPredicate<BLDG, String> veteranAir = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getVeteranAirUnits(), query);
        };
        BiPredicate<BLDG, String> stealthBarrier = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getStealthAttackBarrier(), query);
        };
        BiPredicate<BLDG, String> nukes = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getAllowsNuclearWeapons(), query);
        };
        BiPredicate<BLDG, String> icbmDefence = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getDecreasesSuccessOfMissiles(), query);
        };
        BiPredicate<BLDG, String> doubleVsBarbs = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getDoubleCombatVsBarbarians(), query);
        };
        BiPredicate<BLDG, String> armiesWithoutLeader = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getBuildArmiesWithoutLeader(), query);
        };
        BiPredicate<BLDG, String> largerArmies = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getBuildLargerArmies(), query);
        };
        BiPredicate<BLDG, String> moreLeaders = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getIncreasesChanceOfLeaderAppearance(), query);
        };
        BiPredicate<BLDG, String> safeAtSea = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getSafeSeaTravel(), query);
        };
        BiPredicate<BLDG, String> plusOneSea = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getIncreasedShipMovement(), query);
        };
        BiPredicate<BLDG, String> plusTwoSea = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getPlusTwoShipMovement(), query);
        };
        BiPredicate<BLDG, String> halfCostUpgrades = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getCheaperUpgrades(), query);
        };
        BiPredicate<BLDG, String> healInEnemyTerritory = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getAllowsHealingInEnemyTerritory(), query);
        };
        BiPredicate<BLDG, String> strongerArmies = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getIncreasedArmyValue(), query);
        };
        BiPredicate<BLDG, String> doubleDefences = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getDoubleCityDefences(), query);
        };
        BiPredicate<BLDG, String> extraFoodInWater = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getIncreasesFoodInWater(), query);
        };
        BiPredicate<BLDG, String> storeHalfOfFood = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getDoublesCityGrowthRate(), query);
        };
        BiPredicate<BLDG, String> gainTwoPop = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getDoubleCityGrowth(), query);
        };
        BiPredicate<BLDG, String> allowCitySize2 = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getAllowCityLevel2(), query);
        };
        BiPredicate<BLDG, String> allowCitySize3 = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getAllowCityLevel3(), query);
        };
        BiPredicate<BLDG, String> plus50Science = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getIncreasedResearch(), query);
        };
        BiPredicate<BLDG, String> plus100Science = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getDoublesResearchOutput(), query);
        };
        BiPredicate<BLDG, String> twoFreeAdvances = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getTwoFreeAdvances(), query);
        };
        BiPredicate<BLDG, String> gainKnownTechs = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getGainAnyTechsKnownByTwoCivs(), query);
        };	
        BiPredicate<BLDG, String> gainInEveryCity = (bldg, buildingName) -> {
            buildingName = buildingName.substring(1);
            if (buildingName.equalsIgnoreCase("None")) {
                return bldg.getGainInEveryCity() == -1;
            }
            else {
                int b = 0;
                for (BLDG building : Main.getCurrentBIQ().buildings) {
                    if (building.getName().equalsIgnoreCase(buildingName)) {
                        return bldg.getGainInEveryCity() == b;
                    }
                    b++;
                }
                return false;
            }
        };
        BiPredicate<BLDG, String> gainInContinentCity = (bldg, buildingName) -> {
            buildingName = buildingName.substring(1);
            if (buildingName.equalsIgnoreCase("None")) {
                return bldg.getGainOnContinent()== -1;
            }
            else {
                int b = 0;
                for (BLDG building : Main.getCurrentBIQ().buildings) {
                    if (building.getName().equalsIgnoreCase(buildingName)) {
                        return bldg.getGainOnContinent() == b;
                    }
                    b++;
                }
                return false;
            }
        };
        BiPredicate<BLDG, String> gainUnit = (bldg, unitName) -> {
            unitName = unitName.substring(1);
            if (unitName.equalsIgnoreCase("None")) {
                return bldg.getUnitProduced() == -1;
            }
            else {
                int u = 0;
                for (PRTO prto : Main.getCurrentBIQ().unit) {
                    if (prto.getName().equalsIgnoreCase(unitName)) {
                        return bldg.getUnitProduced() == u;
                    }
                    u++;
                }
                return false;
            }
        };
        BiPredicate<BLDG, String> gainUnitFrequency = (bldg, frequency) -> {
            return evaluateIntegerQuery(bldg.getUnitFrequency(), frequency);
        };
        BiPredicate<BLDG, String> madeObsoleteBy = (bldg, techName) -> {
            techName = techName.substring(1);
            if (techName.equalsIgnoreCase("None")) {
                return bldg.getObsoleteBy() == -1;
            }
            else {
                int t = 0;
                for (TECH tech : Main.getCurrentBIQ().technology) {
                    if (tech.getName().equalsIgnoreCase(techName)) {
                        return bldg.getObsoleteBy()== t;
                    }
                    t++;
                }
                return false;
            }
        };
        BiPredicate<BLDG, String> reqGovernment = (bldg, govtName) -> {
            govtName = govtName.substring(1);
            if (bldg.getReqGovernment()== -1) {
                return govtName.equalsIgnoreCase("None");
            }
            else {
                GOVT theGovt = Main.getCurrentBIQ().government.get(bldg.getReqGovernment());
                return theGovt.getName().equalsIgnoreCase(govtName);
            }
        };
        BiPredicate<BLDG, String> reqTech = (bldg, techName) -> {
            techName = techName.substring(1);
            if (bldg.getReqAdvance()== -1) {
                return techName.equalsIgnoreCase("None");
            }
            else {
                TECH theTech = Main.getCurrentBIQ().technology.get(bldg.getReqAdvance());
                return theTech.getName().equalsIgnoreCase(techName);
            }
        };
        BiPredicate<BLDG, String> reqGood = (bldg, goodName) -> {
            goodName = goodName.substring(1);
            if (bldg.getReqResource1() == -1 && bldg.getReqResource2() == -1) {
                return goodName.equalsIgnoreCase("None");
            }
            else {
                if (bldg.getReqResource1() != -1) {
                    GOOD theGood = Main.getCurrentBIQ().resource.get(bldg.getReqResource1());
                    if (theGood != null && theGood.getName().equalsIgnoreCase(goodName)) {
                        return true;
                    }
                }
                if (bldg.getReqResource2() != -1) {
                    GOOD theGood = Main.getCurrentBIQ().resource.get(bldg.getReqResource2());
                    if (theGood != null && theGood.getName().equalsIgnoreCase(goodName)) {
                        return true;
                    }
                }
                return false;   //doesn't match either
            }
        };
        BiPredicate<BLDG, String> numReqBuildings = (bldg, query) -> {
          return evaluateIntegerQuery(bldg.getNumReqBuildings(), query);
        };
        BiPredicate<BLDG, String> numArmies = (bldg, query) -> {
          return evaluateIntegerQuery(bldg.getArmiesRequired(), query);
        };
        BiPredicate<BLDG, String> goodsInCityRadius = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getGoodsMustBeInCityRadius(), query);
        };
        BiPredicate<BLDG, String> nearRiver = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getMustBeNearRiver(), query);
        };
        BiPredicate<BLDG, String> victoriousArmy = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getRequiresVictoriousArmy(), query);
        };
        BiPredicate<BLDG, String> coastal = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getCoastalInstallation(), query);
        };
        BiPredicate<BLDG, String> byWater = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getMustBeNearWater(), query);
        };
        BiPredicate<BLDG, String> eliteShip = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.requiresEliteShip(), query);
        };
        BiPredicate<BLDG, String> airTrade = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getAllowAirTrade(), query);
        };
        BiPredicate<BLDG, String> waterTrade = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getAllowWaterTrade(), query);
        };
        BiPredicate<BLDG, String> capitalization = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getCapitalization(), query);
        };
        BiPredicate<BLDG, String> plusFiftyTax = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getIncreasedTaxes(), query);
        };
        BiPredicate<BLDG, String> increasedWaterTrade = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getIncreasesTradeInWater(), query);
        };
        BiPredicate<BLDG, String> increasedTrade = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getIncreasedTrade(), query);
        };
        BiPredicate<BLDG, String> paysTradeMaintenance = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getPaysTradeMaintenance(), query);
        };
        BiPredicate<BLDG, String> earnsInterest = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getTreasuryEarnsInterest(), query);
        };
        BiPredicate<BLDG, String> reducesCityCorruption = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getReducesCorruption(), query);
        };
        BiPredicate<BLDG, String> reducesEmpireCorruption = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getForbiddenPalace(), query);
        };
        BiPredicate<BLDG, String> continentalHappiness = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getContinentalMoodEffects(), query);
        };
        BiPredicate<BLDG, String> happyCity = (bldg, query) -> {
          return evaluateIntegerQuery(bldg.getHappy(), query);
        };
        BiPredicate<BLDG, String> happyGlobal = (bldg, query) -> {
          return evaluateIntegerQuery(bldg.getHappyAll(), query);
        };
        BiPredicate<BLDG, String> unhappyCity = (bldg, query) -> {
          return evaluateIntegerQuery(bldg.getUnhappy(), query);
        };
        BiPredicate<BLDG, String> unhappyGlobal = (bldg, query) -> {
          return evaluateIntegerQuery(bldg.getUnhappyAll(), query);
        };
        BiPredicate<BLDG, String> plusFiftyLuxury = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getIncreasedLuxuries(), query);
        };
        BiPredicate<BLDG, String> moreLuxTrade = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getIncreasesLuxuryTrade(), query);
        };
        BiPredicate<BLDG, String> reducesWarWeariness = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getReducesWarWeariness(), query);
        };
        BiPredicate<BLDG, String> reducesWarWearinessEmpire = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getReducesWarWearinessEmpire(), query);
        };
        BiPredicate<BLDG, String> doublesHappiness = (bldg, bldgName) -> {
            bldgName = bldgName.substring(1);
            if (bldg.getDoublesHappiness() == -1) {
                return bldgName.equalsIgnoreCase("None");
            }
            else {
                BLDG theBLDG = Main.getCurrentBIQ().buildings.get(bldg.getDoublesHappiness());
                return theBLDG.getName().equalsIgnoreCase(bldgName);
            }
        };
        BiPredicate<BLDG, String> centerOfEmpire = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getCenterOfEmpire(), query);
        };
        BiPredicate<BLDG, String> noPopulationPollution = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getRemovePopPollution(), query);
        };
        BiPredicate<BLDG, String> canMeltdown = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getMayExplodeOrMeltdown(), query);
        };
        BiPredicate<BLDG, String> moreShieldsInWater = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getIncreasesShieldsInWater(), query);
        };
        BiPredicate<BLDG, String> spaceshipPart = (bldg, query) -> {
          return evaluateIntegerQuery(bldg.getSpaceshipPart(), query);
        };
        BiPredicate<BLDG, String> touristAttraction = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getTouristAttraction(), query);
        };
        BiPredicate<BLDG, String> allowsDiplomaticVictory = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getAllowDiplomaticVictory(), query);
        };
        BiPredicate<BLDG, String> replacesOthersWithFlag = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getReplacesOtherWithThisTag(), query);
        };
        BiPredicate<BLDG, String> doublesSacrifice = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getDoublesSacrifice(), query);
        };
        BiPredicate<BLDG, String> propagandaResistance = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getResistantToBribery(), query);
        };
        BiPredicate<BLDG, String> canBuildSpaceship = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getBuildSpaceshipParts(), query);
        };
        BiPredicate<BLDG, String> allowsSpies = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getAllowSpyMissions(), query);
        };
        BiPredicate<BLDG, String> militaristic = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getMilitaryInstallation(), query);
        };
        BiPredicate<BLDG, String> religious = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getPlaceOfWorship(), query);
        };
        BiPredicate<BLDG, String> commercial = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getTradeInstallation(), query);
        };
        BiPredicate<BLDG, String> industrial = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getConstructionInstallation(), query);
        };
        BiPredicate<BLDG, String> expansionist = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getExplorationInstallation(), query);
        };
        BiPredicate<BLDG, String> scientific = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getResearchInstallation(), query);
        };
        BiPredicate<BLDG, String> agricultural = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getAgriculturalInstallation(), query);
        };
        BiPredicate<BLDG, String> seafaring = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getSeafaringInstallation(), query);
        };
        BiPredicate<BLDG, String> charmBarrier = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getCharmBarrier(), query);
        };
        BiPredicate<BLDG, String> generalTelepad = (bldg, query) -> {
          return evaluateBooleanQuery(bldg.getActsAsGeneralTelepad(), query);
        };
        BiPredicate<BLDG, String> flavor = (bldg, flavName) -> {
            flavName = flavName.substring(1);
            if (flavName.equalsIgnoreCase("None")) {
                //num flavors always returns the number of FLAVs, not the number
                //that are true for the building.  hence this roundabout way
                //of checking.
                for (int i = 0; i < bldg.getNumFlavors(); i++) {
                    boolean flavPresent = bldg.getFlavour(i);
                    if (flavPresent) {
                        return false;
                    }
                }
                return true;
            }
            else {
                for (int i = 0; i < bldg.getNumFlavors(); i++) {
                    boolean flavPresent = bldg.getFlavour(i);
                    if (flavPresent) {
                        String flavour = utils.cTrim(Main.getCurrentBIQ().flavor.get(i).getName());
                        if (flavour.equalsIgnoreCase(flavName)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        };
        
        Map<String, BiPredicate<BLDG, String>> keyToPredicateMap = new HashMap<>();
        keyToPredicateMap.put("cost".toLowerCase(), cost);
        keyToPredicateMap.put("maintenance".toLowerCase(), maintenance);
        keyToPredicateMap.put("culture".toLowerCase(), culture);
        keyToPredicateMap.put("productionBonus".toLowerCase(), productionBonus);
        keyToPredicateMap.put("pollution".toLowerCase(), pollution);
        keyToPredicateMap.put("type".toLowerCase(), bldgType);
        keyToPredicateMap.put("landBombard".toLowerCase(), landBombard);
        keyToPredicateMap.put("seaBombard".toLowerCase(), navalBombard);
        keyToPredicateMap.put("airAttack".toLowerCase(), airAttack);
        keyToPredicateMap.put("seaAttack".toLowerCase(), seaAttack);
        keyToPredicateMap.put("defenceBonus".toLowerCase(), defenceBonus);
        keyToPredicateMap.put("navalDefence".toLowerCase(), navalDefence);
        keyToPredicateMap.put("defenseBonus".toLowerCase(), defenceBonus);  //US English
        keyToPredicateMap.put("navalDefense".toLowerCase(), navalDefence);  //US English
        keyToPredicateMap.put("veteranLand".toLowerCase(), veteranLand);
        keyToPredicateMap.put("veteranSea".toLowerCase(), veteranSea);
        keyToPredicateMap.put("veteranAir".toLowerCase(), veteranAir);
        keyToPredicateMap.put("stealthBarrier".toLowerCase(), stealthBarrier);
        keyToPredicateMap.put("allowsNukes".toLowerCase(), nukes);
        keyToPredicateMap.put("nukes".toLowerCase(), nukes);    //alias
        keyToPredicateMap.put("icbmDefence".toLowerCase(), icbmDefence);
        keyToPredicateMap.put("icbmDefense".toLowerCase(), icbmDefence);  //US English
        keyToPredicateMap.put("doubleVsBarbs".toLowerCase(), doubleVsBarbs);
        keyToPredicateMap.put("armiesWithoutLeader".toLowerCase(), armiesWithoutLeader);
        keyToPredicateMap.put("largerArmies".toLowerCase(), largerArmies);
        keyToPredicateMap.put("moreLeaders".toLowerCase(), moreLeaders);
        keyToPredicateMap.put("safeAtSea".toLowerCase(), safeAtSea);
        keyToPredicateMap.put("plusOneSea".toLowerCase(), plusOneSea);
        keyToPredicateMap.put("plusTwoSea".toLowerCase(), plusTwoSea);
        keyToPredicateMap.put("halfCostUpgrades".toLowerCase(), halfCostUpgrades);
        keyToPredicateMap.put("healInEnemyTerritory".toLowerCase(), healInEnemyTerritory);
        keyToPredicateMap.put("strongerArmies".toLowerCase(), strongerArmies);
        keyToPredicateMap.put("doubleDefences".toLowerCase(), doubleDefences);
        keyToPredicateMap.put("doubleDefenses".toLowerCase(), doubleDefences);  //US English
        keyToPredicateMap.put("extraFoodInWater".toLowerCase(), extraFoodInWater);
        keyToPredicateMap.put("storeHalfOfFood".toLowerCase(), storeHalfOfFood);
        keyToPredicateMap.put("gainTwoPop".toLowerCase(), gainTwoPop);
        keyToPredicateMap.put("allowCitySize2".toLowerCase(), allowCitySize2);
        keyToPredicateMap.put("allowCitySize3".toLowerCase(), allowCitySize3);
        keyToPredicateMap.put("plus50Science".toLowerCase(), plus50Science);
        keyToPredicateMap.put("plus100Science".toLowerCase(), plus100Science);
        keyToPredicateMap.put("twoFreeAdvances".toLowerCase(), twoFreeAdvances);
        keyToPredicateMap.put("gainKnownTechs".toLowerCase(), gainKnownTechs);
        keyToPredicateMap.put("gainInEveryCity".toLowerCase(), gainInEveryCity);
        keyToPredicateMap.put("gainInContinentCity".toLowerCase(), gainInContinentCity);
        keyToPredicateMap.put("gainUnit".toLowerCase(), gainUnit);
        keyToPredicateMap.put("gainUnitFrequency".toLowerCase(), gainUnitFrequency);
        keyToPredicateMap.put("madeObsoleteBy".toLowerCase(), madeObsoleteBy);
        keyToPredicateMap.put("reqBuilding".toLowerCase(), reqBuilding);
        keyToPredicateMap.put("reqTech".toLowerCase(), reqTech);
        keyToPredicateMap.put("reqGovernment".toLowerCase(), reqGovernment);
        keyToPredicateMap.put("reqGood".toLowerCase(), reqGood);
        keyToPredicateMap.put("numReqBuildings".toLowerCase(), numReqBuildings);
        keyToPredicateMap.put("numArmies".toLowerCase(), numArmies);
        keyToPredicateMap.put("goodsInCityRadius".toLowerCase(), goodsInCityRadius);
        keyToPredicateMap.put("nearRiver".toLowerCase(), nearRiver);
        keyToPredicateMap.put("victoriousArmy".toLowerCase(), victoriousArmy);
        keyToPredicateMap.put("coastal".toLowerCase(), coastal);
        keyToPredicateMap.put("byWater".toLowerCase(), byWater);
        keyToPredicateMap.put("eliteShip".toLowerCase(), eliteShip);
        keyToPredicateMap.put("airTrade".toLowerCase(), airTrade);
        keyToPredicateMap.put("waterTrade".toLowerCase(), waterTrade);
        keyToPredicateMap.put("capitalization".toLowerCase(), capitalization);
        keyToPredicateMap.put("plusFiftyTax".toLowerCase(), plusFiftyTax);
        keyToPredicateMap.put("increasedWaterTrade".toLowerCase(), increasedWaterTrade);
        keyToPredicateMap.put("increasedTrade".toLowerCase(), increasedTrade);
        keyToPredicateMap.put("paysTradeMaintenance".toLowerCase(), paysTradeMaintenance);
        keyToPredicateMap.put("earnsInterest".toLowerCase(), earnsInterest);
        keyToPredicateMap.put("reducesCityCorruption".toLowerCase(), reducesCityCorruption);
        keyToPredicateMap.put("reducesEmpireCorruption".toLowerCase(), reducesEmpireCorruption);
        keyToPredicateMap.put("continentalHappiness".toLowerCase(), continentalHappiness);
        keyToPredicateMap.put("happyCity".toLowerCase(), happyCity);
        keyToPredicateMap.put("happyGlobal".toLowerCase(), happyGlobal);
        keyToPredicateMap.put("unhappyCity".toLowerCase(), unhappyCity);
        keyToPredicateMap.put("unhappyGlobal".toLowerCase(), unhappyGlobal);
        keyToPredicateMap.put("plusFiftyLuxury".toLowerCase(), plusFiftyLuxury);
        keyToPredicateMap.put("moreLuxTrade".toLowerCase(), moreLuxTrade);
        keyToPredicateMap.put("reducesWarWeariness".toLowerCase(), reducesWarWeariness);
        keyToPredicateMap.put("reducesWarWearinessEmpire".toLowerCase(), reducesWarWearinessEmpire);
        keyToPredicateMap.put("doublesHappiness".toLowerCase(), doublesHappiness);
        keyToPredicateMap.put("centerOfEmpire".toLowerCase(), centerOfEmpire);
        keyToPredicateMap.put("noPopulationPollution".toLowerCase(), noPopulationPollution);
        keyToPredicateMap.put("canMeltdown".toLowerCase(), canMeltdown);
        keyToPredicateMap.put("moreShieldsInWater".toLowerCase(), moreShieldsInWater);
        keyToPredicateMap.put("spaceshipPart".toLowerCase(), spaceshipPart);
        keyToPredicateMap.put("touristAttraction".toLowerCase(), touristAttraction);
        keyToPredicateMap.put("allowsDiplomaticVictory".toLowerCase(), allowsDiplomaticVictory);
        keyToPredicateMap.put("replacesOthersWithFlag".toLowerCase(), replacesOthersWithFlag);
        keyToPredicateMap.put("doublesSacrifice".toLowerCase(), doublesSacrifice);
        keyToPredicateMap.put("propagandaResistance".toLowerCase(), propagandaResistance);
        keyToPredicateMap.put("canBuildSpaceship".toLowerCase(), canBuildSpaceship);
        keyToPredicateMap.put("allowsSpies".toLowerCase(), allowsSpies);
        keyToPredicateMap.put("militaristic".toLowerCase(), militaristic);
        keyToPredicateMap.put("religious".toLowerCase(), religious);
        keyToPredicateMap.put("commercial".toLowerCase(), commercial);
        keyToPredicateMap.put("industrial".toLowerCase(), industrial);
        keyToPredicateMap.put("expansionist".toLowerCase(), expansionist);
        keyToPredicateMap.put("scientific".toLowerCase(), scientific);
        keyToPredicateMap.put("agricultural".toLowerCase(), agricultural);
        keyToPredicateMap.put("seafaring".toLowerCase(), seafaring);
        keyToPredicateMap.put("charmBarrier".toLowerCase(), charmBarrier);
        keyToPredicateMap.put("generalTelepad".toLowerCase(), generalTelepad);
        keyToPredicateMap.put("flavor".toLowerCase(), flavor);
        keyToPredicateMap.put("flavour".toLowerCase(), flavor);  //UK English
        
        Map<String, BiPredicate> genericPredicateMap = new HashMap<>();
        for (Map.Entry e : keyToPredicateMap.entrySet()) {
            genericPredicateMap.put((String)e.getKey(), (BiPredicate)e.getValue());
        }
        return constructSectionBiPredicate(genericPredicateMap, nameContainsQuery);
    }
    
    
    /**
     * Creates a predicate for filtering PRTOs.  Will use the supplied filter text in the predicate.
     * Takes about 550 microseconds (0.5ms) the first time, and about 2 microseconds subsequent times.
     * @param prtoFilterText
     * @param civilization
     * @param technology
     * @return 
     */
    public static BiPredicate<PRTO, String> createPRTOFilter() {
        BiPredicate<PRTO, String> nameContainsQuery = (prto, query) -> {
          return prto.getName().toLowerCase().contains(query.toLowerCase());
        };
        BiPredicate<PRTO, String> attack = (prto, query) -> {
            return evaluateIntegerQuery(prto.getAttack(), query);
        };
        BiPredicate<PRTO, String> defence = (prto, query) -> {
            return evaluateIntegerQuery(prto.getDefence(), query);
        };
        BiPredicate<PRTO, String> movement = (prto, query) -> {
            return evaluateIntegerQuery(prto.getMovement(), query);
        };
        BiPredicate<PRTO, String> cost = (prto, query) -> {
            return evaluateIntegerQuery(prto.getShieldCost(), query);
        };
        BiPredicate<PRTO, String> popCost = (prto, query) -> {
            return evaluateIntegerQuery(prto.getPopulationCost(), query);
        };
        BiPredicate<PRTO, String> bombardStrength = (prto, query) -> {
            return evaluateIntegerQuery(prto.getBombardStrength(), query);
        };
        BiPredicate<PRTO, String> bombardRange = (prto, query) -> {
            return evaluateIntegerQuery(prto.getBombardRange(), query);
        };
        BiPredicate<PRTO, String> bombardRate = (prto, query) -> {
            return evaluateIntegerQuery(prto.getRateOfFire(), query);
        };
        BiPredicate<PRTO, String> hpBonus = (prto, query) -> {
            return evaluateIntegerQuery(prto.getHitPointBonus(), query);
        };
        BiPredicate<PRTO, String> airDefence = (prto, query) -> {
            return evaluateIntegerQuery(prto.getAirDefence(), query);
        };
        BiPredicate<PRTO, String> operatingRange = (prto, query) -> {
            return evaluateIntegerQuery(prto.getOperationalRange(), query);
        };
        BiPredicate<PRTO, String> transportCapacity = (prto, query) -> {
            return evaluateIntegerQuery(prto.getCapacity(), query);
        };
        //TODO:    reqTech, reqGood, whole bunch booleans starting on 369
        BiPredicate<PRTO, String> era = (prto, query) -> {
            if (!query.startsWith("=")) {
                return false;
            }
            List<TECH> technology = Main.getCurrentBIQ().technology;
            List<ERAS> eras = Main.getCurrentBIQ().eras;
            try {
                Integer value = Integer.parseInt(query.substring(1));
                if (value == 0) {   //"None"
                    return prto.getRequiredTech() == -1;
                }
                else {
                    if (prto.getRequiredTech() == -1) {
                        //unit's era is none, filtering on era other than "None"
                        return false;
                    }
                    TECH reqTech = technology.get(prto.getRequiredTech());
                    return reqTech.getEra() + 1 == value;
                }
            }
            catch(NumberFormatException ex) {
                //Use string value
                String eraName = query.substring(1);
                if (eraName.equalsIgnoreCase("None")) {
                    return prto.getRequiredTech() == -1;
                }
                else {
                    for (int i = 0; i < eras.size(); i++) {
                        ERAS age = eras.get(i);
                        if (age.getName().equalsIgnoreCase(eraName)) {
                            //False if there isn't a required tech, as it can't be in the right era
                            if (prto.getRequiredTech() == -1) {
                                return false;
                            }
                            TECH reqTech = technology.get(prto.getRequiredTech());
                            return reqTech.getEra() == i;
                        }
                    }
                }
            }
            return false;
        };
        BiPredicate<PRTO, String> reqTech = (prto, query) -> {
            if (!query.startsWith("=")) {
                return false;
            }
            String techName = query.substring(1);
            if (techName.equalsIgnoreCase("None")) {
                return prto.getRequiredTech() == -1;
            }
            else {
                List<TECH> technology = Main.getCurrentBIQ().technology;
                for (int t = 0; t < technology.size(); t++) {
                    TECH tech = technology.get(t);
                    if (tech.getName().toLowerCase().equals(techName.toLowerCase())) {
                        return prto.getRequiredTech() == t;
                    }
                }
            }
            return false;
        };
        BiPredicate<PRTO, String> reqGood = (prto, query) -> {
            if (!query.startsWith("=")) {
                return false;
            }
            String goodName = query.substring(1);
            if (goodName.equalsIgnoreCase("None")) {
                return prto.getRequiredResource1() == -1 && prto.getRequiredResource2() == -1 && prto.getRequiredResource3() == -1;
            }
            else {
                List<GOOD> goods = Main.getCurrentBIQ().resource;
                for (int g = 0; g < goods.size(); g++) {
                    GOOD good = goods.get(g);
                    if (good.getName().toLowerCase().equals(goodName.toLowerCase())) {
                        return prto.getRequiredResource1() == g || prto.getRequiredResource2() == g || prto.getRequiredResource3() == g;
                    }
                }
            }
            return false;
        };
        BiPredicate<PRTO, String> classPredicate = (prto, query) -> {
            if (!query.startsWith("=")) {
                return false;
            }
            switch(query.substring(1).toLowerCase()) {
                case "land" :
                    return prto.getUnitClass() == PRTO.CLASS_LAND;
                case "sea" :
                    return prto.getUnitClass() == PRTO.CLASS_SEA;
                case "air" :
                    return prto.getUnitClass() == PRTO.CLASS_AIR;
                default :
                    return false;
            }
        };
        
        BiPredicate<PRTO, String> availableTo = (prto, query) -> {
            List<RACE> civilization = Main.getCurrentBIQ().civilization;
            int numCivsAvailableTo = 0;
            Integer value = null;
            try {
                value = Integer.parseInt(query.substring(1));
                for (int c = 0; c < civilization.size(); c++) {
                    if (prto.isAvailableTo(c)) {
                        numCivsAvailableTo++;
                    }
                }
            }
            catch(NumberFormatException ex) {
                //Search by civ name
                String civName = query.substring(1);
                for (int c = 0; c < civilization.size(); c++) {
                    RACE civ = civilization.get(c);
                    if (civ.getName().toLowerCase().equals(civName.toLowerCase())) {
                        return prto.isAvailableTo(c);
                    }
                }
            }
            if (query.startsWith(">")) {
                return numCivsAvailableTo > value;
            }
            if (query.startsWith("<")) {
                return numCivsAvailableTo < value;
            }
            if (query.startsWith("=")) {
                return numCivsAvailableTo == value;
            }
            return false;
        };
        BiPredicate<PRTO, String> uniqueTo = (prto, query) -> {
            if (!prto.isUniqueUnit()) {
                return false;
            }
            List<RACE> civilization = Main.getCurrentBIQ().civilization;
            String civName = query.substring(1);
            for (int c = 0; c < civilization.size(); c++) {
                RACE civ = civilization.get(c);
                if (civ.getName().toLowerCase().equals(civName.toLowerCase())) {
                    return prto.isAvailableTo(c);
                }
            }
            return false;
        };
        BiPredicate<PRTO, String> ignoreMovementCost = (prto, query) -> {
            if (!query.startsWith("=")) {
                return false;
            }
            List<TERR> terrains = Main.getCurrentBIQ().terrain;
            String terrName = query.substring(1);
            for (int c = 0; c < terrains.size(); c++) {
                TERR terr = terrains.get(c);
                if (terr.getName().toLowerCase().equals(terrName.toLowerCase())) {
                    return prto.ignoresTerrain(c);
                }
            }
            return false;
        };
        BiPredicate<PRTO, String> allRoads = (prto, query) -> {
            return evaluateBooleanQuery(prto.treatsAllTerrainAsRoads(), query);
        };
        BiPredicate<PRTO, String> amphibious = (prto, query) -> {
            return evaluateBooleanQuery(prto.isAmphibious(), query);
        };
        BiPredicate<PRTO, String> army = (prto, query) -> {
            return evaluateBooleanQuery(prto.isArmy(), query);
        };
        BiPredicate<PRTO, String> blitz = (prto, query) -> {
            return evaluateBooleanQuery(prto.isBlitz(), query);
        };
        BiPredicate<PRTO, String> cruiseMissile = (prto, query) -> {
            return evaluateBooleanQuery(prto.isCruiseMissile(), query);
        };
        BiPredicate<PRTO, String> detectInvisible = (prto, query) -> {
            return evaluateBooleanQuery(prto.detectsInvisible(), query);
        };
        BiPredicate<PRTO, String> draft = (prto, query) -> {
            return evaluateBooleanQuery(prto.isDraftable(), query);
        };
        BiPredicate<PRTO, String> flagUnit = (prto, query) -> {
            return evaluateBooleanQuery(prto.isFlagUnit(), query);
        };
        BiPredicate<PRTO, String> footUnit = (prto, query) -> {
            return evaluateBooleanQuery(prto.isFootSoldier(), query);
        };
        BiPredicate<PRTO, String> hiddenNationality = (prto, query) -> {
            return evaluateBooleanQuery(prto.hasHiddenNationality(), query);
        };
        BiPredicate<PRTO, String> immobile = (prto, query) -> {
            return evaluateBooleanQuery(prto.isImmobile(), query);
        };
        BiPredicate<PRTO, String> infiniteBombard = (prto, query) -> {
            return evaluateBooleanQuery(prto.hasInfiniteBombardRange(), query);
        };
        BiPredicate<PRTO, String> invisible = (prto, query) -> {
            return evaluateBooleanQuery(prto.isInvisible(), query);
        };
        BiPredicate<PRTO, String> king = (prto, query) -> {
            return evaluateBooleanQuery(prto.isKing(), query);
        };
        BiPredicate<PRTO, String> leader = (prto, query) -> {
            return evaluateBooleanQuery(prto.isLeader(), query);
        };
        BiPredicate<PRTO, String> lethalLand = (prto, query) -> {
            return evaluateBooleanQuery(prto.hasLethalLandBombardment(), query);
        };
        BiPredicate<PRTO, String> lethalSea = (prto, query) -> {
            return evaluateBooleanQuery(prto.hasLethalSeaBombardment(), query);
        };
        BiPredicate<PRTO, String> nuclearWeapon = (prto, query) -> {
            return evaluateBooleanQuery(prto.isNuclearWeapon(), query);
        };
        BiPredicate<PRTO, String> radar = (prto, query) -> {
            return evaluateBooleanQuery(prto.hasRadar(), query);
        };
        BiPredicate<PRTO, String> rangedAttack = (prto, query) -> {
            return evaluateBooleanQuery(prto.hasRangedAttackAnimation(), query);
        };
        BiPredicate<PRTO, String> requiresEscort = (prto, query) -> {
            return evaluateBooleanQuery(prto.requiresEscort(), query);
        };
        BiPredicate<PRTO, String> rotateBeforeAttack = (prto, query) -> {
            return evaluateBooleanQuery(prto.rotatesBeforeAttack(), query);
        };
        BiPredicate<PRTO, String> sinksInOcean = (prto, query) -> {
            return evaluateBooleanQuery(prto.sinksInOcean(), query);
        };
        BiPredicate<PRTO, String> sinksInSea = (prto, query) -> {
            return evaluateBooleanQuery(prto.sinksInSea(), query);
        };
        BiPredicate<PRTO, String> goldenAge = (prto, query) -> {
            return evaluateBooleanQuery(prto.startsGoldenAge(), query);
        };
        BiPredicate<PRTO, String> stealth = (prto, query) -> {
            return evaluateBooleanQuery(prto.isStealth(), query);
        };
        BiPredicate<PRTO, String> tacticalMissile = (prto, query) -> {
            return evaluateBooleanQuery(prto.isTacticalMissile(), query);
        };
        BiPredicate<PRTO, String> transportsAircraft = (prto, query) -> {
            return evaluateBooleanQuery(prto.transportsOnlyAirUnits(), query);
        };
        BiPredicate<PRTO, String> transportsFoot = (prto, query) -> {
            return evaluateBooleanQuery(prto.transportsOnlyFootUnits(), query);
        };
        BiPredicate<PRTO, String> transportsMissiles = (prto, query) -> {
            return evaluateBooleanQuery(prto.transportsOnlyTacticalMissiles(), query);
        };
        
        Map<String, BiPredicate<PRTO, String>> keyToPredicateMap = new HashMap<>();
        keyToPredicateMap.put("attack".toLowerCase(), attack);
        keyToPredicateMap.put("defence".toLowerCase(), defence);
        keyToPredicateMap.put("defense".toLowerCase(), defence);
        keyToPredicateMap.put("movement".toLowerCase(), movement);
        keyToPredicateMap.put("cost".toLowerCase(), cost);
        keyToPredicateMap.put("popCost".toLowerCase(), popCost);
        keyToPredicateMap.put("availableTo".toLowerCase(), availableTo);
        keyToPredicateMap.put("bombardStrength".toLowerCase(), bombardStrength);
        keyToPredicateMap.put("bombardRange".toLowerCase(), bombardRange);
        keyToPredicateMap.put("bombardRate".toLowerCase(), bombardRate);
        keyToPredicateMap.put("hpBonus".toLowerCase(), hpBonus);
        keyToPredicateMap.put("airDefence".toLowerCase(), airDefence);
        keyToPredicateMap.put("airDefense".toLowerCase(), airDefence);
        keyToPredicateMap.put("opRange".toLowerCase(), operatingRange);
        keyToPredicateMap.put("transportCapacity".toLowerCase(), transportCapacity);
        keyToPredicateMap.put("class".toLowerCase(), classPredicate);
        keyToPredicateMap.put("era".toLowerCase(), era);
        keyToPredicateMap.put("uniqueTo".toLowerCase(), uniqueTo);
        keyToPredicateMap.put("reqTech".toLowerCase(), reqTech);
        keyToPredicateMap.put("reqGood".toLowerCase(), reqGood);
        keyToPredicateMap.put("allRoads".toLowerCase(), allRoads);
        keyToPredicateMap.put("amphibious".toLowerCase(), amphibious);
        keyToPredicateMap.put("army".toLowerCase(), army);
        keyToPredicateMap.put("blitz".toLowerCase(), blitz);
        keyToPredicateMap.put("cruiseMissile".toLowerCase(), cruiseMissile);
        keyToPredicateMap.put("detectInvisible".toLowerCase(), detectInvisible);
        keyToPredicateMap.put("draft".toLowerCase(), draft);
        keyToPredicateMap.put("flagUnit".toLowerCase(), flagUnit);
        keyToPredicateMap.put("footUnit".toLowerCase(), footUnit);
        keyToPredicateMap.put("hiddenNationality".toLowerCase(), hiddenNationality);
        keyToPredicateMap.put("immobile".toLowerCase(), immobile);
        keyToPredicateMap.put("infiniteBombard".toLowerCase(), infiniteBombard);
        keyToPredicateMap.put("invisible".toLowerCase(), invisible);
        keyToPredicateMap.put("king".toLowerCase(), king);
        keyToPredicateMap.put("leader".toLowerCase(), leader);
        keyToPredicateMap.put("lethalLand".toLowerCase(), lethalLand);
        keyToPredicateMap.put("lethalSea".toLowerCase(), lethalSea);
        keyToPredicateMap.put("nuclearWeapon".toLowerCase(), nuclearWeapon);
        keyToPredicateMap.put("radar".toLowerCase(), radar);
        keyToPredicateMap.put("rangedAttack".toLowerCase(), rangedAttack);
        keyToPredicateMap.put("requiresEscort".toLowerCase(), requiresEscort);
        keyToPredicateMap.put("rotateBeforeAttack".toLowerCase(), rotateBeforeAttack);
        keyToPredicateMap.put("sinksInOcean".toLowerCase(), sinksInOcean);
        keyToPredicateMap.put("sinksInSea".toLowerCase(), sinksInSea);
        keyToPredicateMap.put("goldenAge".toLowerCase(), goldenAge);
        keyToPredicateMap.put("stealth".toLowerCase(), stealth);
        keyToPredicateMap.put("tacticalMissile".toLowerCase(), tacticalMissile);
        keyToPredicateMap.put("transportsAircraft".toLowerCase(), transportsAircraft);
        keyToPredicateMap.put("transportsFoot".toLowerCase(), transportsFoot);
        keyToPredicateMap.put("transportsMissiles".toLowerCase(), transportsMissiles);
        keyToPredicateMap.put("wheeled".toLowerCase(), createPRTOBoolean(PRTO::isWheeled));
        keyToPredicateMap.put("ignoreMovementCost".toLowerCase(), ignoreMovementCost);        
        
        Map<String, BiPredicate> genericPredicateMap = new HashMap<>();
        for (Map.Entry e : keyToPredicateMap.entrySet()) {
            genericPredicateMap.put((String)e.getKey(), (BiPredicate)e.getValue());
        }
        return constructSectionBiPredicate(genericPredicateMap, nameContainsQuery);
    }
    
    public static BiPredicate<GOOD, String> createGOODFilter() {
        BiPredicate<GOOD, String> nameContainsQuery = (good, query) -> {
          return good.getName().toLowerCase().contains(query.toLowerCase());
        };
        BiPredicate<GOOD, String> food = (good, query) -> {
            return evaluateIntegerQuery(good.getFoodBonus(), query);
        };
        BiPredicate<GOOD, String> shields = (good, query) -> {
            return evaluateIntegerQuery(good.getShieldsBonus(), query);
        };
        BiPredicate<GOOD, String> commerce = (good, query) -> {
            return evaluateIntegerQuery(good.getCommerceBonus(), query);
        };
        BiPredicate<GOOD, String> appearanceRatio = (good, query) -> {
            return evaluateIntegerQuery(good.getAppearanceRatio(), query);
        };
        BiPredicate<GOOD, String> disappearanceRatio = (good, query) -> {
            return evaluateIntegerQuery(good.getDisapperanceProbability(), query);
        };
        BiPredicate<GOOD, String> icon = (good, query) -> {
            return evaluateIntegerQuery(good.getIcon(), query);
        };
        BiPredicate<GOOD, String> typePredicate = (good, query) -> {
            if (!query.startsWith("=")) {
                return false;
            }
            switch(query.substring(1).toLowerCase()) {
                case "strategic" :
                    return good.getType() == GOOD.STRATEGIC;
                case "luxury" :
                    return good.getType() == GOOD.LUXURY;
                case "bonus" :
                    return good.getType() == GOOD.BONUS;
                default :
                    return false;
            }
        };
        BiPredicate<GOOD, String> prerequisite = (good, query) -> {
            if (!query.startsWith("=")) {
                return false;
            }
            String techName = query.substring(1);
            if (techName.equalsIgnoreCase("None")) {
                return good.getPrerequisite()== -1;
            }
            else {
                TECH prereq = Main.getCurrentBIQ().technology.get(good.getPrerequisite());
                return prereq.getName().equalsIgnoreCase(techName);
            }
        };
        
        BiPredicate<GOOD, String> era = (good, query) -> {
            if (!query.startsWith("=")) {
                return false;
            }
            List<TECH> technology = Main.getCurrentBIQ().technology;
            List<ERAS> eras = Main.getCurrentBIQ().eras;
            try {
                Integer value = Integer.parseInt(query.substring(1));
                if (value == 0) {   //"None"
                    return good.getPrerequisite()== -1;
                }
                else {
                    if (good.getPrerequisite() == -1) {
                        //era is none, filtering on era other than "None"
                        return false;
                    }
                    TECH reqTech = technology.get(good.getPrerequisite());
                    return reqTech.getEra() + 1 == value;
                }
            }
            catch(NumberFormatException ex) {
                //Use string value
                String eraName = query.substring(1);
                if (eraName.equalsIgnoreCase("None")) {
                    return good.getPrerequisite() == -1;
                }
                else {
                    for (int i = 0; i < eras.size(); i++) {
                        ERAS age = eras.get(i);
                        if (age.getName().equalsIgnoreCase(eraName)) {
                            //False if there isn't a required tech, as it can't be in the right era
                            if (good.getPrerequisite() == -1) {
                                return false;
                            }
                            TECH reqTech = technology.get(good.getPrerequisite());
                            return reqTech.getEra() == i;
                        }
                    }
                }
            }
            return false;
        };
        
        Map<String, BiPredicate> genericPredicateMap = new HashMap<>();
        Map<String, BiPredicate<GOOD, String>> keyToPredicateMap = new HashMap<>();
        keyToPredicateMap.put("type".toLowerCase(), typePredicate);
        keyToPredicateMap.put("food".toLowerCase(), food);
        keyToPredicateMap.put("shields".toLowerCase(), shields);
        keyToPredicateMap.put("production".toLowerCase(), shields);
        keyToPredicateMap.put("commerce".toLowerCase(), commerce);
        keyToPredicateMap.put("appearance".toLowerCase(), appearanceRatio);
        keyToPredicateMap.put("appearanceRatio".toLowerCase(), appearanceRatio);
        keyToPredicateMap.put("disappearance".toLowerCase(), disappearanceRatio);
        keyToPredicateMap.put("disappearanceRatio".toLowerCase(), disappearanceRatio);
        keyToPredicateMap.put("icon".toLowerCase(), icon);
        keyToPredicateMap.put("prerequisite".toLowerCase(), prerequisite);
        keyToPredicateMap.put("era".toLowerCase(), era);
        for (Map.Entry e : keyToPredicateMap.entrySet()) {
            genericPredicateMap.put((String)e.getKey(), (BiPredicate)e.getValue());
        }
        return constructSectionBiPredicate(genericPredicateMap, nameContainsQuery);
    }
    
    public static BiPredicate createTECHFilter() {
        BiPredicate<TECH, String> nameContainsQuery = (tech, query) -> {
          return tech.getName().toLowerCase().contains(query.toLowerCase());
        };
        BiPredicate<TECH, String> enablesRecycling = (tech, query) -> {
            return evaluateBooleanQuery(tech.getEnablesRecycling(), query);
        };
        BiPredicate<TECH, String> notReqForAdvancement = (tech, query) -> {
            return evaluateBooleanQuery(tech.getNotRequiredForAdvancement(), query);
        };
        BiPredicate<TECH, String> bonusTechAwarded = (tech, query) -> {
            return evaluateBooleanQuery(tech.getBonusTech(), query);
        };
        BiPredicate<TECH, String> permitsSacrifice = (tech, query) -> {
            return evaluateBooleanQuery(tech.getPermitsSacrifice(), query);
        };
        BiPredicate<TECH, String> revealsWorldMap = (tech, query) -> {
            return evaluateBooleanQuery(tech.getRevealMap(), query);
        };
        BiPredicate<TECH, String> diplomats = (tech, query) -> {
            return evaluateBooleanQuery(tech.getEnablesDiplomats(), query);
        };
        BiPredicate<TECH, String> alliances = (tech, query) -> {
            return evaluateBooleanQuery(tech.getEnablesAlliances(), query);
        };
        BiPredicate<TECH, String> rightOfPassage = (tech, query) -> {
            return evaluateBooleanQuery(tech.getEnablesROP(), query);
        };
        BiPredicate<TECH, String> enablesMPP = (tech, query) -> {
            return evaluateBooleanQuery(tech.getEnablesMPP(), query);
        };
        BiPredicate<TECH, String> tradeEmbargoes = (tech, query) -> {
            return evaluateBooleanQuery(tech.getEnablesTradeEmbargoes(), query);
        };
        BiPredicate<TECH, String> mapTrading = (tech, query) -> {
            return evaluateBooleanQuery(tech.getEnablesMapTrading(), query);
        };
        BiPredicate<TECH, String> communicationTrading = (tech, query) -> {
            return evaluateBooleanQuery(tech.getEnablesCommunicationTrading(), query);
        };
        BiPredicate<TECH, String> irrigationWithoutWater = (tech, query) -> {
            return evaluateBooleanQuery(tech.getEnablesIrrigationWithoutFreshWater(), query);
        };
        BiPredicate<TECH, String> stopsDisease = (tech, query) -> {
            return evaluateBooleanQuery(tech.getDisablesFloodPlainDisease(), query);
        };
        BiPredicate<TECH, String> doublesWorkRate = (tech, query) -> {
            return evaluateBooleanQuery(tech.getDoublesWorkRate(), query);
        };
        BiPredicate<TECH, String> notTradeable = (tech, query) -> {
            return evaluateBooleanQuery(tech.getCannotBeTraded(), query);
        };
        BiPredicate<TECH, String> doublesWealth = (tech, query) -> {
            return evaluateBooleanQuery(tech.getDoublesWealth(), query);
        };
        BiPredicate<TECH, String> tradeBySea = (tech, query) -> {
            return evaluateBooleanQuery(tech.getEnablesSeaTrade(), query);
        };
        BiPredicate<TECH, String> tradeByOcean = (tech, query) -> {
            return evaluateBooleanQuery(tech.getEnablesOceanTrade(), query);
        };
        BiPredicate<TECH, String> bridges = (tech, query) -> {
            return evaluateBooleanQuery(tech.getEnablesBridges(), query);
        };
        BiPredicate<TECH, String> conscription = (tech, query) -> {
            return evaluateBooleanQuery(tech.getEnablesConscription(), query);
        };
        BiPredicate<TECH, String> mobilizationLevels = (tech, query) -> {
            return evaluateBooleanQuery(tech.getEnablesMobilizationLevels(), query);
        };
        BiPredicate<TECH, String> precisionBombing = (tech, query) -> {
            return evaluateBooleanQuery(tech.getEnablesPrecisionBombing(), query);
        };
        BiPredicate<TECH, String> era = (tech, query) -> {
            if (!query.startsWith("=")) {
                return false;
            }
            query = query.substring(1); //remove the =
            try {
                Integer value = Integer.parseInt(query);                            
                if (value == 0) {   //"None"
                    if (tech.getEra() != -1) {
                        return false;
                    }
                }
                else {
                    if ((tech.getEra() + 1) != value) {
                        return false;
                    }
                }
            }
            catch(NumberFormatException ex) {
                //Use string value
                if (query.equalsIgnoreCase("None") && tech.getEra() != -1) {
                    return false;
                }

                List<ERAS> eras = Main.getCurrentBIQ().eras;
                for (int i = 0; i < eras.size(); i++) {
                    ERAS age = eras.get(i);
                    if (age.getName().equalsIgnoreCase(query)) {
                        if ((tech.getEra()) != i) {
                            return false;
                        }
                    }
                }
            }
            return true;
        };
        BiPredicate<TECH, String> costPredicate = (tech, query) -> {
            Integer value = null;
            try { 
                value = Integer.parseInt(query.substring(1));
            }
            catch(NumberFormatException ex) {
                return false;
            }
            if (query.startsWith(">")) {
                return tech.getCost() > value;
            }
            else if (query.startsWith("<")) {
                return tech.getCost() < value;
            }
            else if (query.startsWith("=")) {
                return tech.getCost() == value;
            }
            return false;
        };
        BiPredicate<TECH, String> reqTech = (tech, query) -> {
            if (!query.startsWith("=")) {
                return false;
            }
            String techName = query.substring(1);
            if (techName.equalsIgnoreCase("None")) {
                if (tech.getPrerequisite1() != -1 || tech.getPrerequisite2() != -1 || tech.getPrerequisite3() != -1 || tech.getPrerequisite4() != -1) {
                    return false;
                }
            }
            else {
                List<TECH> technologies = Main.getCurrentBIQ().technology;
                for (int t = 0; t < technologies.size(); t++) {
                    TECH iterTech = technologies.get(t);
                    if (iterTech.getName().toLowerCase().equals(techName.toLowerCase())) {
                        if (!(tech.getPrerequisite1() == t || tech.getPrerequisite2() == t || tech.getPrerequisite3() == t || tech.getPrerequisite4() == t))
                            return false;
                    }
                }
            }
            return true;
        };
        BiPredicate<TECH, String> flavor = (tech, query) -> {
            if (!query.startsWith("=")) {
                return false;
            }
            List<FLAV> flavors = Main.getCurrentBIQ().flavor;
            for (int i = 0; i < tech.getFlavours().size(); i++) {
                //Go through all the flavors the tech doesn't have.  If one of them matches what the query
                //requires them to have, return false.
                if (!tech.getFlavour(i)) {
                    FLAV flavour = flavors.get(i);
                    if (utils.cTrim(flavour.name).equalsIgnoreCase(query.substring(1))) {
                        return false;
                    }
                }
            }
            return true;
        };
        
        Map<String, BiPredicate<TECH, String>> keyToPredicateMap = new HashMap<>();
        keyToPredicateMap.put("enablesRecycling".toLowerCase(), enablesRecycling);
        keyToPredicateMap.put("notReqForAdvancement".toLowerCase(), notReqForAdvancement);
        keyToPredicateMap.put("bonusTechAwarded".toLowerCase(), bonusTechAwarded);
        keyToPredicateMap.put("permitsSacrifice".toLowerCase(), permitsSacrifice);
        keyToPredicateMap.put("revealsWorldMap".toLowerCase(), revealsWorldMap);
        keyToPredicateMap.put("diplomats".toLowerCase(), diplomats);
        keyToPredicateMap.put("alliances".toLowerCase(), alliances);
        keyToPredicateMap.put("rightOfPassage".toLowerCase(), rightOfPassage);
        keyToPredicateMap.put("enablesMPP".toLowerCase(), enablesMPP);
        keyToPredicateMap.put("tradeEmbargoes".toLowerCase(), tradeEmbargoes);
        keyToPredicateMap.put("mapTrading".toLowerCase(), mapTrading);
        keyToPredicateMap.put("communicationTrading".toLowerCase(), communicationTrading);
        keyToPredicateMap.put("irrigationWithoutWater".toLowerCase(), irrigationWithoutWater);
        keyToPredicateMap.put("stopsDisease".toLowerCase(), stopsDisease);
        keyToPredicateMap.put("doublesWorkRate".toLowerCase(), doublesWorkRate);
        keyToPredicateMap.put("notTradeable".toLowerCase(), notTradeable);
        keyToPredicateMap.put("doublesWealth".toLowerCase(), doublesWealth);
        keyToPredicateMap.put("tradeBySea".toLowerCase(), tradeBySea);
        keyToPredicateMap.put("tradeByOcean".toLowerCase(), tradeByOcean);
        keyToPredicateMap.put("bridges".toLowerCase(), bridges);
        keyToPredicateMap.put("conscription".toLowerCase(), conscription);
        keyToPredicateMap.put("mobilizationLevels".toLowerCase(), mobilizationLevels);
        keyToPredicateMap.put("precisionBombing".toLowerCase(), precisionBombing);
        keyToPredicateMap.put("era".toLowerCase(), era);
        keyToPredicateMap.put("cost".toLowerCase(), costPredicate);
        keyToPredicateMap.put("reqTech".toLowerCase(), reqTech);
        keyToPredicateMap.put("flavor".toLowerCase(), flavor);
        keyToPredicateMap.put("flavour".toLowerCase(), flavor);
        
        //This is a hack to work around generics.  Someday I should master their finer points.
        Map<String, BiPredicate> genericPredicateMap = new HashMap<>();
        for (Map.Entry e : keyToPredicateMap.entrySet()) {
            genericPredicateMap.put((String)e.getKey(), (BiPredicate)e.getValue());
        }
        return constructSectionBiPredicate(genericPredicateMap, nameContainsQuery);
    }
    
    /**
     * Common method for constructing predicates.  This keeps all the common handling in one method, and makes it easier to add
     * things such as new logical operators.
     * @param keyToPredicateMap
     * @param nameContainsQuery
     * @return 
     */
    private static BiPredicate constructSectionBiPredicate(Map<String, BiPredicate> keyToPredicateMap, BiPredicate nameContainsQuery) {
        BiPredicate predicate = (itemBeingEvaluated, userQuery) -> {
            BIQSection tech = (BIQSection) itemBeingEvaluated;
            String query = (String) userQuery;
            List<BiPredicate> thePredicates = new LinkedList<>();
                
            List<String> tokens = PredicateCommonFunctions.tokenize(query);
            List<String> validatedTokens = new LinkedList<>();
            for (String token : tokens) {
                if (token.isEmpty()) {      //eventually tokenize should handle this...
                    continue;
                }
                if (token.equals("(") || token.equals(")") || token.equals("or")) {
                    validatedTokens.add(token);
                    continue;
                }
                if (!token.contains("=") && !token.contains("<") && !token.contains(">")) {
                    thePredicates.add(nameContainsQuery);
                    validatedTokens.add(token);
                    continue;
                }
                String splitter = token.contains("!=") ? "!=" : token.contains("=") ? "=" : token.contains("<") ? "<" : ">";
                String[]split = token.split(splitter);
                if (split.length < 2) {
                    continue;
                }
                String key = split[0];
                if (keyToPredicateMap.containsKey(key.toLowerCase())) {
                    if (splitter.equals("!=")) {
                        thePredicates.add(keyToPredicateMap.get(key.toLowerCase()).negate());
                    }
                    else {
                        thePredicates.add(keyToPredicateMap.get(key.toLowerCase()));
                    }
                    //Include <, >, or = so we can have one predicate per key that
                    //distinguishes on its own, rather than up to three separate ones.
                    //Negation handled above, so don't include !
                    if (splitter.equals("!=")) {
                        validatedTokens.add(splitter.substring(1) + split[1]);
                    }
                    else {
                        validatedTokens.add(splitter + split[1]);
                    }
                }
            }
            return PredicateCommonFunctions.evaluatePredicates(tech, thePredicates, validatedTokens);
        };
        return predicate;
    }
    
    /**
     * Evaluates a >, <, or = integer comparison query.
     * E.G. pass in a value of 4 (maybe a unit attack/bombard/etc.), and >2,
     * will return true.
     * @param comparisonValue The value being compared.
     * @param query The query that the value is compared against.
     * @return true if the value passes the query; false otherwise or if query is invalid.
     */
    private static boolean evaluateIntegerQuery(int comparisonValue, String query) {
        Integer value = null;
        try { 
            value = Integer.parseInt(query.substring(1));
        }
        catch(NumberFormatException ex) {
            return false;
        }
        if (query.startsWith(">")) {
            return comparisonValue> value;
        }
        else if (query.startsWith("<")) {
            return comparisonValue < value;
        }
        else if (query.startsWith("=")) {
            return comparisonValue == value;
        }
        return false;
    }
    
    
    private static boolean evaluateBooleanQuery(boolean comparisonValue, String query) {
        if (!query.startsWith("=")) {
            return false;
        }
        query = query.substring(1);
        if (query.equalsIgnoreCase("true")) {
            return comparisonValue == true;
        }
        else if (query.equalsIgnoreCase("false")) {
            return comparisonValue == false;
        }
        else {
            return false;
        }
    }
    
    static BiPredicate<PRTO, String> createPRTOBoolean(Function<PRTO, Boolean> propertyFunction) {
       return (biqItem, query) -> {
            return evaluateBooleanQuery(propertyFunction.apply(biqItem), query);
        };
    }
}
