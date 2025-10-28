package com.civfanatics.civ3.biqFile;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * Standard Orders does not appear to be used
 * Why both it and PTW Standard Orders/duplicates in Air Missions exist, I do
 * not know
 *
 * TODO: If a unit has multiple strategies, Firaxis stores a whole 'nother copy
 * of that unit.  This is what the 'Other strategy' references - the PRTO that
 * is that other unit.  Probably ought to emulate this... YACOEFP
 *
 */

/**
 * Other sections that know about PRTO: Bldg, Civ, Rule, MAP
 * @author Quintillus
 */
import com.civfanatics.civ3.biqFile.util.LittleEndianDataOutputStream;
import java.util.*;
import org.apache.log4j.*;
import com.civfanatics.civ3.biqFile.util.Utils;
import java.io.IOException;
public class PRTO extends BIQSection{
    int index;  //index within the BIQ
    Logger logger = Logger.getLogger(this.getClass());
    private int dataLength = 241;  //Is this really the base with no stealth targets nor telepads?
    //zoneOfControl is true when it is equal to 1
    private int zoneOfControl;
    private String name = "";
    private String civilopediaEntry = "";
    private int bombardStrength;
    private int bombardRange;
    private int capacity;
    private int shieldCost;
    private int defence;
    private int iconIndex;
    private int attack;
    private int operationalRange;
    private int populationCost;
    private int rateOfFire;
    private int movement = 1;
    private int requiredTechInt = -1;
    private TECH requiredTech;
    private int upgradeToInt = -1;
    private PRTO upgradeTo;
    private int requiredResource1Int = -1;
    private int requiredResource2Int = -1;
    private int requiredResource3Int = -1;
    private GOOD requiredResource1;
    private GOOD requiredResource2;
    private GOOD requiredResource3;
    int unitAbilities;  //package private - set when importing, but should not generally be accessed
    int AIStrategy;  //package private - set when importing, but should not generally be accessed
    /**
     * Leftmost (highest-value) bit is civ 31, rightmost is civ zero.
     * Remember Java is highest-value, so this is accurate at the time of processing here.
     * In the actual file, civ 0 will be leftmost, 31 rightmost.
     */
    int availableTo;  //package private - set when importing, but should not generally be accessed
    int standardOrdersSpecialActions;  //package private - set when importing, but should not generally be accessed
    int airMissions; //package private - set when importing, but should not generally be accessed
    private int unitClass;
        public static int CLASS_LAND = 0;
        public static int CLASS_SEA = 1;
        public static int CLASS_AIR = 2;
    private int otherStrategy = -1;
    private int hitPointBonus;
    //PTW Modifiers are PACKAGE private.  They need to be importable, but we DON'T
    //want them to have public getters/setters.
    int PTWStandardOrders;
    int PTWSpecialActions;
    int PTWWorkerActions;
    int PTWAirMissions;
    int PTWActionsMix;
    private short unknown;
    private int bombardEffects;
    private byte[]ignoreMovementCost;
    private int requiresSupport;
    private int useExactCost = 7;
    private int telepadRange;
    private int questionMark3 = 1;
    //private int numLegalUnitTelepads; //present in BIQ file
    private ArrayList<Integer>legalUnitTelepadsInt;
    private ArrayList<PRTO>legalUnitTelepads;
    private int enslaveResultsInInt = -1;
    private PRTO enslaveResultsIn;
    private int questionMark5 = 1;
    private List<Integer>stealthTargetsInt;
    private ArrayList<PRTO>stealthTargets;
    private int questionMark6 = 1;
    //private int numLegalBuildingTelepads;  //present in BIQ file
    private ArrayList<Integer>legalBuildingTelepads;
    private byte createsCraters;
    private int workerStrength1;
    private float workerStrengthFloat;
    private int questionMark8 = 0;
    private int airDefence;

    /*
     ******************************************
     * From here onward are derived variables * 
     ******************************************
     */
    private boolean wheeled;
    private boolean footSoldier;
    private boolean blitz;
    private boolean cruiseMissile;
    private boolean allTerrainAsRoads;
    private boolean radar;
    private boolean amphibiousUnit;
    private boolean invisible;
    private boolean transportsOnlyAirUnits;
    private boolean draftable;
    private boolean immobile;
    private boolean sinksInSea;
    private boolean sinksInOcean;
    private boolean flagUnit;
    private boolean transportsOnlyFootUnits;
    private boolean startsGoldenAge;
    private boolean nuclearWeapon;
    private boolean hiddenNationality;
    private boolean army;
    private boolean leader;
    private boolean infiniteBombardRange;
    private boolean stealth;
    private boolean detectInvisible;
    private boolean tacticalMissile;
    private boolean transportsOnlyTacticalMissiles;
    private boolean rangedAttackAnimations;
    private boolean rotateBeforeAttack;
    private boolean lethalLandBombardment;
    private boolean lethalSeaBombardment;
    private boolean king;
    private boolean requiresEscort;
    //AI Strategy
    private Boolean offence = Boolean.FALSE;
    private Boolean defenceStrategy = false;
    private Boolean artillery = false;
    private Boolean exploreStrategy = false;
    private Boolean armyUnit = false;
    private Boolean cruiseMissileUnit = false;
    private Boolean airBombard = false;
    private Boolean airDefenceStrategy = false;
    private Boolean navalPower = false;
    private Boolean airTransport = false;
    private Boolean navalTransport = false;
    private Boolean navalCarrier = false;
    private Boolean terraform = false;
    private Boolean settle = false;
    private Boolean leaderUnit = false;
    private Boolean tacticalNuke = false;
    private Boolean ICBM = false;
    private Boolean navalMissileTransport = false;
    private Boolean flagStrategy = false;
    private Boolean kingStrategy = false;
    Boolean[]strategies = {offence, defenceStrategy, artillery, exploreStrategy, armyUnit, cruiseMissileUnit, airBombard, airDefenceStrategy, navalPower, airTransport, navalTransport, navalCarrier, terraform, settle, leaderUnit, tacticalNuke, ICBM, navalMissileTransport, flagStrategy, kingStrategy};
    public static String[] strategyNames = {"Offence", "Defence", "Artillery", "Exploration", "Army", "Cruise Missile", "Air Bombard", "Air Defence", "Naval Power", "Air Transport", "Naval Transport", "Naval Carrier", "Terraform", "Settle", "Leader", "Tactical Nuke", "ICBM", "Naval Missile Transport", "Flag Unit", "King Unit"};
    private byte numStrategies;
    //civilizations PRTO's can be available to
    private boolean[] availableToArray;
    //Standard Orders/Special Actions - UNUSED
    private boolean skipTurn;
    private boolean wait;
    private boolean fortify;
    private boolean disband;
    private boolean goTo;
    private boolean load;
    private boolean unload;
    private boolean airlift;
    private boolean pillage;
    private boolean bombard;
    private boolean airdrop;
    private boolean buildArmy;
    private boolean finishImprovement;
    private boolean upgrade;
    private boolean buildColony;
    private boolean buildCity;
    private boolean buildRoad;
    private boolean buildRailroad;
    private boolean buildFort;
    private boolean buildMine;
    private boolean irrigate;
    private boolean clearForest;
    private boolean clearJungle;
    private boolean plantForest;
    private boolean clearPollution;
    private boolean automate;
    private boolean joinCity;
    private boolean buildAirfield;
    private boolean buildRadarTower;
    private boolean buildOutpost;
    private boolean buildBarricade;
    //air missions - UNUSED
    private boolean bomb;
    private boolean recon;
    private boolean intercept;
    private boolean rebase;
    private boolean precisionBomb;
    //PTW Standard Orders
    private boolean PTWskipTurn;
    private boolean PTWwait;
    private boolean PTWfortify;
    private boolean PTWdisband;
    private boolean PTWgoTo;
    private boolean PTWexploreOrder;
    private boolean PTWsentry;
    //PTW Special Actions
    private boolean PTWload;
    private boolean PTWunload;
    private boolean PTWairlift;
    private boolean PTWpillage;
    private boolean PTWbombard;
    private boolean PTWairdrop;
    private boolean PTWbuildArmy;
    private boolean PTWfinishImprovements;
    private boolean PTWupgradeUnit;
    private boolean PTWcapture;
    //several empty bytes
    private boolean telepad;
    private boolean teleportable;
    private boolean stealthAttack;
    private boolean charm;
    private boolean enslave;
    private boolean collateralDamage;
    private boolean sacrifice;
    private boolean scienceAge;
    //PTW Worker Actions
    private boolean PTWbuildColony;
    private boolean PTWbuildCity;
    private boolean PTWbuildRoad;
    private boolean PTWbuildRailroad;
    private boolean PTWbuildFort;
    private boolean PTWbuildMine;
    private boolean PTWirrigate;
    private boolean PTWclearForest;
    private boolean PTWclearJungle;
    private boolean PTWplantForest;
    private boolean PTWclearPollution;
    private boolean PTWautomate;
    private boolean PTWjoinCity;
    private boolean PTWbuildAirfield;
    private boolean PTWbuildRadarTower;
    private boolean PTWbuildOutpost;
    private boolean PTWbuildBarricade;
    //PTW Air Missions
    private boolean PTWbomb;
    private boolean PTWrecon;
    private boolean PTWintercept;
    private boolean PTWrebase;
    private boolean PTWprecisionBomb;
    /**
     * The madness that is PTW Action Mix.
     * Although none of these have separate toggles in the Conquests Firaxis
     * editor, some *do* still have an effect.  Notably, sentryMix controls
     * Sentry (Enemy Only), and GoToMix controls Go To City.  Further exploration
     * is warranted on the others.
     * 
     * It may be interesting to re-introduce toggles for these.  Consider a unit
     * that could relocate to cities, but couldn't station itself elsewhere - only
     * traveling between.  A classic city guard that's neither totally immobile,
     * nor teleporting.
     * 
     * For now, however, I'm re-enabling these via the regular togggles as functionality
     * is discovered.  This will allow them to exist for newly added units in this editor.
     */
    private boolean sentryMix;  //Sentry (Enemy Only)
    private boolean bombardMix;
    private boolean colonyMix;
    private boolean roadMix;
    private boolean railroadMix;
    private boolean road2Mix;
    private boolean irrigateMix;
    private boolean clearForestMix;
    private boolean clearJungleMix;
    private boolean clearPollutionMix;
    private boolean automateMix;
    private boolean automate2Mix;
    private boolean bombingMix;
    private boolean precisionBombingMix;
    private boolean automate3;
    private boolean goToMix;    //Go To City
    public PRTO(IO baselink, int index)
    {
        this("", baselink, index);
    }

    public PRTO(String name, IO baselink, int index)
    {
        super(baselink);
        this.name = name;
        this.index = index;
        int numTerrains = baselink.version == civ3Version.CONQUESTS ?  14 : 12;
        ignoreMovementCost = new byte[14];
        stealthTargetsInt = new ArrayList<Integer>();
        stealthTargets = new ArrayList<PRTO>();
        legalUnitTelepadsInt = new ArrayList<Integer>();
        legalUnitTelepads = new ArrayList<PRTO>();
        legalBuildingTelepads = new ArrayList<Integer>();
        availableToArray = new boolean[32];
        
        this.dataLength+=numTerrains;   //for ignore movement - 14 for Conquests, 12 for earlier
    }
    
    @Override
    public PRTO clone() {
        PRTO other = new PRTO(this.baseLink, baseLink.unit.size());
        other.zoneOfControl = this.zoneOfControl;
        other.name = this.name;
        other.civilopediaEntry = this.civilopediaEntry;
        other.bombardStrength = this.bombardStrength;
        other.bombardRange = this.bombardRange;
        other.capacity = this.capacity;
        other.shieldCost = this.shieldCost;
        other.defence = this.defence;
        other.iconIndex = this.iconIndex;
        other.attack = this.attack;
        other.operationalRange = this.operationalRange;
        other.populationCost = this.populationCost;
        other.rateOfFire = this.rateOfFire;
        other.movement = this.movement;
        other.requiredTechInt = this.requiredTechInt;
        other.requiredTech = this.requiredTech;
        other.upgradeToInt = this.upgradeToInt;
        other.upgradeTo = this.upgradeTo;
        other.requiredResource1Int = this.requiredResource1Int;
        other.requiredResource2Int = this.requiredResource2Int;
        other.requiredResource3Int = this.requiredResource3Int;
        other.requiredResource1 = this.requiredResource1;
        other.requiredResource2 = this.requiredResource2;
        other.requiredResource3 = this.requiredResource3;
        other.unitAbilities = this.unitAbilities;
        other.AIStrategy = this.AIStrategy;
        other.availableTo = this.availableTo;
        other.standardOrdersSpecialActions = this.standardOrdersSpecialActions;
        other.airMissions = this.airMissions;
        other.unitClass = this.unitClass;
        other.otherStrategy = this.otherStrategy;
        other.hitPointBonus = this.hitPointBonus;
        other.PTWStandardOrders = this.PTWStandardOrders;
        other.PTWSpecialActions = this.PTWSpecialActions;
        other.PTWWorkerActions = this.PTWWorkerActions;
        other.PTWAirMissions = this.PTWAirMissions;
        other.PTWActionsMix = this.PTWActionsMix;
        other.unknown = this.unknown;
        other.bombardEffects = this.bombardEffects;
        other.ignoreMovementCost = new byte[this.ignoreMovementCost.length];
        System.arraycopy(this.ignoreMovementCost, 0, other.ignoreMovementCost, 0, other.ignoreMovementCost.length);
        other.requiresSupport = this.requiresSupport;
        other.useExactCost = this.useExactCost;
        other.telepadRange = this.telepadRange;
        other.questionMark3 = this.questionMark3;
        other.legalUnitTelepadsInt = new ArrayList<Integer>();
        other.legalUnitTelepadsInt.addAll(this.legalUnitTelepadsInt);
        other.legalUnitTelepads = new ArrayList<PRTO>();
        other.legalUnitTelepads.addAll(this.legalUnitTelepads);
        other.enslaveResultsInInt = this.enslaveResultsInInt;
        other.enslaveResultsIn = this.enslaveResultsIn;
        other.questionMark5 = this.questionMark5;
        other.stealthTargetsInt = new ArrayList<Integer>();
        other.stealthTargetsInt.addAll(this.stealthTargetsInt);
        other.stealthTargets = new ArrayList<PRTO>();
        other.stealthTargets.addAll(this.stealthTargets);
        other.questionMark6 = this.questionMark6;
        other.legalBuildingTelepads = new ArrayList<Integer>();
        other.legalBuildingTelepads.addAll(this.legalBuildingTelepads);
        other.createsCraters = this.createsCraters;
        other.workerStrength1 = this.workerStrength1;
        other.workerStrengthFloat = this.workerStrengthFloat;
        other.questionMark8 = this.questionMark8;
        other.airDefence = this.airDefence;
        
        other.extractEnglish();
        
        other.dataLength = this.dataLength;
        return other;
    }
    
    public void trim()
    {
        name = name.trim();
        civilopediaEntry = civilopediaEntry.trim();
    }
    
    public void setNewUnitDefaults() {
        //Intelligent defaults - set standard actions to all enabled,
        //Load/Pillage/Airlift/Unit Upgrade/Capture among Special Actions,
        //Offence/Defence among strategies,
        //Available To to all except barbarians
        PTWskipTurn = true;
        PTWwait = true;
        PTWfortify = true;
        PTWdisband = true;
        PTWgoTo = true;
        PTWexploreOrder = true;
        PTWsentry = true;
        recalculatePTWStandardOrders();
        
        PTWload = true;
        PTWairlift = true;
        PTWpillage = true;
        PTWupgradeUnit = true;
        PTWcapture = true;
        recalculatePTWSpecialActions();
        
        offence = true;
        defenceStrategy = true;
        recalculateAIStrategies();
        
        for (int i = 1; i < 32; i++) {
            availableToArray[i] = true;
        }
        recalculateAvailableTo();
    }

    public void setZoneOfControl(int zoneOfControl)
    {
        this.zoneOfControl = zoneOfControl;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setCivilopediaEntry(String civilopediaEntry)
    {
        this.civilopediaEntry = civilopediaEntry;
    }

    public void setBombardStrength(int bombardStrength)
    {
        this.bombardStrength = bombardStrength;
    }

    public void setBombardRange(int bombardRange)
    {
        this.bombardRange = bombardRange;
    }

    public void setCapacity(int capacity)
    {
        this.capacity = capacity;
    }

    public void setShieldCost(int shieldCost)
    {
        this.shieldCost = shieldCost;
    }

    public void setDefence(int defence)
    {
        this.defence = defence;
    }

    public void setIconIndex(int iconIndex)
    {
        this.iconIndex = iconIndex;
    }

    public void setAttack(int attack)
    {
        this.attack = attack;
    }

    public void setOperationalRange(int operationalRange)
    {
        this.operationalRange = operationalRange;
    }

    public void setPopulationCost(int populationCost)
    {
        this.populationCost = populationCost;
    }

    public void setRateOfFire(int rateOfFire)
    {
        this.rateOfFire = rateOfFire;
    }

    public void setMovement(int movement)
    {
        this.movement = movement;
    }

    public void setRequiredTech(int requiredTech)
    {
        this.requiredTechInt = requiredTech;
        if (baseLink.technology != null && requiredTechInt != -1 && baseLink.technology.size() > requiredTechInt) {
            this.requiredTech = baseLink.technology.get(requiredTechInt);
        }
    }
    
    /**
     * Sets the pointer-based stealth targets, from the int-based version.
     * Should be run after file import.
     */
    public void setStealthTargetPRTOLinks() {
        stealthTargets.clear();
        for (int i = 0; i < stealthTargetsInt.size(); i++) {
            this.stealthTargets.add(baseLink.unit.get(stealthTargetsInt.get(i)));
        }
    }
    
    /**
     * Sets the pointer-based legal unit telepads, from the int-based version.
     * Should be run after file import.
     */
    public void setLegalUnitTelepadsPRTOLinks() {
        legalUnitTelepads.clear();
        for (int i = 0; i < legalUnitTelepadsInt.size(); i++) {
            this.legalUnitTelepads.add(baseLink.unit.get(legalUnitTelepadsInt.get(i)));
        }
    }

    public void setUpgradeTo(int upgradeTo)
    {
        this.upgradeToInt = upgradeTo;
        if (baseLink.unit != null && upgradeToInt != -1 && baseLink.unit.size() > upgradeToInt) {
            this.upgradeTo = baseLink.unit.get(upgradeToInt);
        }
        else if (upgradeToInt == -1) {
            this.upgradeTo = null;
        }
    }

    public void setRequiredResource1(int requiredResource1Int)
    {
        this.requiredResource1Int = requiredResource1Int;
        if (baseLink.resource != null && requiredResource1Int != -1 && baseLink.resource.size() > requiredResource1Int) {
            this.requiredResource1 = baseLink.resource.get(requiredResource1Int);
        }
    }

    public void setRequiredResource2(int requiredResource2)
    {
        this.requiredResource2Int = requiredResource2;
        if (baseLink.resource != null && requiredResource2Int != -1 && baseLink.resource.size() > requiredResource2Int) {
            this.requiredResource2 = baseLink.resource.get(requiredResource2Int);
        }
    }

    public void setRequiredResource3(int requiredResource3)
    {
        this.requiredResource3Int = requiredResource3;
        if (baseLink.resource != null && requiredResource3Int != -1 && baseLink.resource.size() > requiredResource3Int) {
            this.requiredResource3 = baseLink.resource.get(requiredResource3Int);
        }
    }

    public void setUnitAbilities(int unitAbilities)
    {
        this.unitAbilities = unitAbilities;
    }
    
    void setAIStrategy(int AIStrategy)
    {
        this.AIStrategy = AIStrategy;
    }
    
    /**
     * Returns the AI strategy bitfield.
     * Using this is discouraged, as it's all strategies in one bitfield and
     * the user must make sense of it themselves.
     * @return 
     */
    public int getAIStrategy()
    {
        return AIStrategy;
    }
    
    /**
     * Returns a list of the unit's AI strategies in true/false list form.  The
     * strategies are in the same order as the static String strategyNames indicates.
     * @return 
     */
    public Boolean[]getAIStrategySet() {
        return strategies;
    }
    
    public void handleSwappedPRTO() {
        updateStealthTargetIndices();
        updateUnitTelepadIndices();
        if (enslaveResultsInInt != -1) {
            this.enslaveResultsInInt = enslaveResultsIn.getIndex();
        }
        if (upgradeToInt != -1) {
            this.upgradeToInt = upgradeTo.getIndex();
        }
    }
    
    public void handleSwappedGOOD() {
        if (requiredResource1Int != -1) {
            this.requiredResource1Int = requiredResource1.getIndex();
        }
        if (requiredResource2Int != -1) {
            this.requiredResource2Int = requiredResource2.getIndex();
        }
        if (requiredResource3Int != -1) {
            this.requiredResource3Int = requiredResource3.getIndex();
        }
    }
    
    /**
     * Clears out the availableTo variable, in effect setting the unit available to no civs.
     * Typically used when re-saving which civs a unit is available to.
     */
    public void clearAvailableTo()
    {
        this.availableTo = 0;
    }
    
    /**
     * Makes the unit available to a particular civilization.
     * @param index The civ to whom the unit should be available.
     */
    public void setAvailableTo(int index)
    {
        int add = 1;
        add = add << index;
        this.availableTo = this.availableTo | add;
    }
    
    public void setUnavailableTo(int index) {
        int add = 1;
        add = add << index;
        this.availableTo = this.availableTo & ~add;
    }
    
    /**
     * Sets the availableTo for all civilizations at once.  This should only be used
     * by IO.java.
     * @param index
     */
    void setAvailableToBinary(int availableToBinary)
    {
        this.availableTo = availableToBinary;
    }
    
    /**
     * Returns whether the unit is available to the given civilization.  Uses
     * bitwise operations to determine this.
     * @param civIndex The index of the civilization.
     * @return Whether the unit is available to the given civilization.
     */
    public boolean isAvailableTo(int civIndex)
    {
        int value = 1;
        value = value << civIndex;  //remains 1 if civIndex is zero
        return ((this.availableTo & value) == value);
    }
    
    public boolean isUniqueUnit() {
        int trueCount = 0;
        for (int i = 0; i < availableToArray.length; i++) {
            if (availableToArray[i]) {
                trueCount++;
            }
            if (trueCount > 1) {
                return false;
            }
        }
        return trueCount == 1;
    }

    public void setStandardOrdersSpecialActions(int standardOrdersSpecialActions)
    {
        this.standardOrdersSpecialActions = standardOrdersSpecialActions;
    }

    public void setAirMissions(int airMissions)
    {
        this.airMissions = airMissions;
    }

    public void setUnitClass(int unitClass)
    {
        this.unitClass = unitClass;
    }

    public void setOtherStrategy(int otherStrategy)
    {
        this.otherStrategy = otherStrategy;
    }

    public void setHitPointBonus(int hitPointBonus)
    {
        this.hitPointBonus = hitPointBonus;
    }

    public void setPTWStandardOrders(int PTWStandardOrders)
    {
        this.PTWStandardOrders = PTWStandardOrders;
    }

    public void setPTWSpecialActions(int PTWSpecialActions)
    {
        this.PTWSpecialActions = PTWSpecialActions;
    }

    public void setPTWWorkerActions(int PTWWorkerActions)
    {
        this.PTWWorkerActions = PTWWorkerActions;
    }

    public void setPTWAirMissions(int PTWAirMissions)
    {
        this.PTWAirMissions = PTWAirMissions;
    }

    public void setPTWActionsMix(int PTWActionsMix)
    {
        this.PTWActionsMix = PTWActionsMix;
    }

    public void setUnknown(short unknown)
    {
        this.unknown = unknown;
    }

    public void setBombardEffects(int bombardEffects)
    {
        this.bombardEffects = bombardEffects;
    }

    public void setRequiresSupport(int requiresSupport)
    {
        this.requiresSupport = requiresSupport;
    }

    public void setUseExactCost(int useExactCost)
    {
        this.useExactCost = useExactCost;
    }

    public void setTelepadRange(int telepadRange)
    {
        this.telepadRange = telepadRange;
    }

    public void setQuestionMark3(int questionMark3)
    {
        this.questionMark3 = questionMark3;
    }

    public void setEnslaveResultsIn(int enslaveResultsIn)
    {
        this.enslaveResultsInInt = enslaveResultsIn;
        if (baseLink.unit != null && enslaveResultsInInt != -1 && baseLink.unit.size() > enslaveResultsInInt) {
            this.enslaveResultsIn = baseLink.unit.get(enslaveResultsInInt);
        }
    }

    public void setQuestionMark5(int questionMark5)
    {
        this.questionMark5 = questionMark5;
    }

    public void setQuestionMark6(int questionMark6)
    {
        this.questionMark6 = questionMark6;
    }

    public void setCreatesCraters(byte createsCraters)
    {
        this.createsCraters = createsCraters;
    }

    public void setWorkerStrength1(int workerStrength1)
    {
        this.workerStrength1 = workerStrength1;
    }

    public void setWorkerStrengthFloat(float workerStrengthFloat)
    {
        this.workerStrengthFloat = workerStrengthFloat;
    }

    public void setQuestionMark8(int questionMark8)
    {
        this.questionMark8 = questionMark8;
        }

    public void setAirDefence(int airDefence)
    {
        this.airDefence = airDefence;
    }

    public void setOffence(boolean offence)
    {
        this.offence = offence;
    }

    public void setDefenceStrategy(boolean defenceStrategy)
    {
        this.defenceStrategy = defenceStrategy;
    }
    public void setArtillery(boolean artillery)
    {
        this.artillery = artillery;
    }
    
    public boolean getExploreOrder()
    {
        if (this.isPTWOrConquests())
            return PTWexploreOrder;
        else
            return false;
    }
    
    public void setExploreOrder(boolean explore)
    {
        if (this.isPTWOrConquests())
            this.PTWexploreOrder = explore;
    }
    
    public boolean getSentry()
    {
        if (this.isPTWOrConquests())
            return PTWsentry;
        else
            return false;
    }
    
    /**
     * Not present in vanilla.
     * @param sentry 
     */
    public void setSentry(boolean sentry)
    {
        if (isPTWOrConquests()) {
            this.PTWsentry = sentry;
            this.sentryMix = sentry;    //the Firaxis editor sets both of these at once.  for now we shall as well.
        }
    }
    public void setArmyUnit(boolean armyUnit)
    {
        this.armyUnit = armyUnit;
    }
    public void setCruiseMissileUnit(boolean cruiseMissileUnit)
    {
        this.cruiseMissileUnit = cruiseMissileUnit;
    }
    public void setAirBombard(boolean airBombard)
    {
        this.airBombard = airBombard;
    }
    public void setAirDefenceStrategy(boolean airDefenceStrategy)
    {
        this.airDefenceStrategy = airDefenceStrategy;
    }
    public void setExploreStrategy(boolean exploreStrategy) {
        this.exploreStrategy = exploreStrategy;
    }
    public void setNavalPower(boolean navalPower)
    {
        this.navalPower = navalPower;
    }
    public void setAirTransport(boolean airTransport)
    {
        this.airTransport = airTransport;
    }
    public void setNavalTransport(boolean navalTransport)
    {
        this.navalTransport = navalTransport;
    }
    public void setNavalCarrier(boolean navalCarrier)
    {
        this.navalCarrier = navalCarrier;
    }
    public void setTerraform(boolean terraform)
    {
        this.terraform = terraform;
    }
    public void setSettle(boolean settle)
    {
        this.settle = settle;
    }
    public void setLeaderUnit(boolean leaderUnit)
    {
        this.leaderUnit = leaderUnit;
    }
    public void setTacticalNuke(boolean tacticalNuke)
    {
        this.tacticalNuke = tacticalNuke;
    }
    public void setICBM(boolean ICBM)
    {
        this.ICBM = ICBM;
    }
    public void setNavalMissileTransport(boolean navalMissileTransport)
    {
        this.navalMissileTransport = navalMissileTransport;
    }
    
    public void setFlagStrategy(boolean flagStrategy)
    {
        this.flagStrategy = flagStrategy;
    }
    
    public void setKingStrategy(boolean kingStrategy)
    {
        this.kingStrategy = kingStrategy;
    }
    
    public boolean getSkipTurn()
    {
        if (isPTWOrConquests())
            return PTWskipTurn;
        else
            return skipTurn;
    }

    public void setSkipTurn(boolean skipTurn)
    {
        if (isPTWOrConquests())
            this.PTWskipTurn = skipTurn;
        else
            this.skipTurn = skipTurn;
    }
    
    public boolean getWait()
    {
        if (isPTWOrConquests())
            return PTWwait;
        else
            return wait;
    }

    public void setWait(boolean wait)
    {
        if (isPTWOrConquests())
            this.PTWwait = wait;
        else
            this.wait = wait;
    }
    
    public boolean getFortify()
    {
        if (isPTWOrConquests())
            return PTWfortify;
        else
            return fortify;
    }

    public void setFortify(boolean fortify)
    {
        if (isPTWOrConquests())
            this.PTWfortify = fortify;
        else
            this.fortify = fortify;
    }
    
    public boolean getDisband()
    {
        if (isPTWOrConquests())
            return PTWdisband;
        else
            return disband;
    }

    public void setDisband(boolean disband)
    {
        if (isPTWOrConquests())
            this.PTWdisband = disband;
        else
            this.disband = disband;
    }
    
    public boolean getGoTo()
    {
        if (isPTWOrConquests())
            return PTWgoTo;
        else
            return goTo;
    }

    public void setGoTo(boolean goTo)
    {
        if (isPTWOrConquests()) {
            PTWgoTo = goTo;
            goToMix = goTo; //the Firaxis editor sets both at once.  we shall as well.
        }
        else
            this.goTo = goTo;
    }
    
    public boolean getLoad()
    {
        if (isPTWOrConquests())
            return PTWload;
        else
            return load;
    }

    public void setLoad(boolean load)
    {
        if (isPTWOrConquests())
            PTWload = load;
        else
            this.load = load;
    }
    
    public boolean getUnload()
    {
        if (isPTWOrConquests())
            return PTWunload;
        else
            return unload;
    }

    public void setUnload(boolean unload)
    {
        if (isPTWOrConquests())
            PTWunload = unload;
        else
            this.unload = unload;
    }
    
    public boolean getAirlift()
    {
        if (isPTWOrConquests())
            return PTWairlift;
        else
            return airlift;
    }

    public void setAirlift(boolean airlift)
    {
        if (isPTWOrConquests())
            PTWairlift = airlift;
        else
            this.airlift = airlift;
    }
    
    public boolean getPillage()
    {
        if (isPTWOrConquests())
            return PTWpillage;
        else
            return pillage;
    }

    public void setPillage(boolean pillage)
    {
        if (isPTWOrConquests())
            PTWpillage = pillage;
        else
            this.pillage = pillage;
    }
    
    public boolean getBombard()
    {
        if (isPTWOrConquests())
            return PTWbombard;
        else
            return bombard;
    }

    public void setBombard(boolean bombard)
    {
        if (isPTWOrConquests())
            PTWbombard = bombard;
        else
            this.bombard = bombard;
    }
    
    public boolean getAirdrop()
    {
        if (isPTWOrConquests())
            return PTWairdrop;
        else
            return airdrop;
    }

    public void setAirdrop(boolean airdrop)
    {
        if (isPTWOrConquests())
            PTWairdrop = airdrop;
        else
            this.airdrop = airdrop;
    }
    
    public boolean getBuildArmy()
    {
        if (isPTWOrConquests())
            return PTWbuildArmy;
        else
            return buildArmy;
    }

    public void setBuildArmy(boolean buildArmy)
    {
        if (isPTWOrConquests())
            PTWbuildArmy = buildArmy;
        else
            this.buildArmy = buildArmy;
    }
    
    public boolean getFinishImprovement()
    {
        if (isPTWOrConquests())
            return PTWfinishImprovements;
        else
            return finishImprovement;
    }

    public void setFinishImprovement(boolean finishImprovement)
    {
        if (isPTWOrConquests())
            PTWfinishImprovements = finishImprovement;
        else
            this.finishImprovement = finishImprovement;
    }
    
    public boolean getUpgrade()
    {
        if (isPTWOrConquests())
            return PTWupgradeUnit;
        else
            return upgrade;
    }

    public void setUpgrade(boolean upgrade)
    {
        if (isPTWOrConquests())
            PTWupgradeUnit = upgrade;
        else
            this.upgrade = upgrade;
    }
    
    /**
     * TODO: Verify collateral damage is PTW+ only
     * @return 
     */
    public boolean getCollateralDamage()
    {
        if (isPTWOrConquests())
            return collateralDamage;
        else
            return false;
    }
    
    public void setCollateralDamage(boolean collateralDamage)
    {
        this.collateralDamage = collateralDamage;
    }
    
    public boolean getScienceAge()
    {
        if (isPTWOrConquests())
            return scienceAge;
        else
            return false;
    }
    
    public void setScienceAge(boolean scienceAge)
    {
        this.scienceAge = scienceAge;
    }
    
    public boolean getCapture()
    {
        if (isPTWOrConquests())
            return PTWcapture;
        else
            return false;
    }
    
    public void setCapture(boolean capture)
    {
        if (isPTWOrConquests())
            this.PTWcapture = capture;
    }
    
    public boolean getStealthAttack()
    {
        if (isPTWOrConquests())
            return stealthAttack;
        else
            return false;
    }
    
    public void setStealthAttack(boolean stealthAttack)
    {
        this.stealthAttack = stealthAttack;
    }
    
    /**
     * TODO: Verify PTW+ only
     * @return 
     */
    public boolean getEnslave()
    {
        if (isPTWOrConquests())
            return enslave;
        else
            return false;
    }
    
    public void setEnslave(boolean enslave)
    {
        this.enslave = enslave;
    }
    
    public boolean getSacrifice()
    {
        if (isPTWOrConquests())
            return sacrifice;
        else
            return false;
    }
    
    public void setSacrifice(boolean sacrifice)
    {
        this.sacrifice = sacrifice;
    }
    
    public boolean getTelepad()
    {
        if (isPTWOrConquests())
            return telepad;
        else
            return false;
    }
    
    public void setTelepad(boolean telepad)
    {
        this.telepad = telepad;
    }
    
    public boolean getTeleportable()
    {
        if (isPTWOrConquests())
            return teleportable;
        else
            return false;
    }
    
    public boolean getCharm() {
        if (isPTWOrConquests())
            return charm;
        return false;
    }
    
    public void setTeleportable(boolean teleportable)
    {
        this.teleportable = teleportable;
    }
    
    public void setCharm(boolean charm) {
        this.charm = charm;
    }
    
    public boolean getBuildColony()
    {
        if (isPTWOrConquests())
            return PTWbuildColony;
        else
            return buildColony;
    }

    public void setBuildColony(boolean buildColony)
    {
        if (isPTWOrConquests())
            PTWbuildColony = buildColony;
        else
            this.buildColony = buildColony;
    }
    
    public boolean getBuildCity()
    {
        if (isPTWOrConquests())
            return PTWbuildCity;
        else
            return buildCity;
    }

    public void setBuildCity(boolean buildCity)
    {
        if (isPTWOrConquests())
            PTWbuildCity = buildCity;
        else
            this.buildCity = buildCity;
    }
    
    public boolean getBuildRoad()
    {
        if (isPTWOrConquests())
            return PTWbuildRoad;
        else
            return buildRoad;
    }

    public void setBuildRoad(boolean buildRoad)
    {
        if (isPTWOrConquests())
            PTWbuildRoad = buildRoad;
        else
            this.buildRoad = buildRoad;
    }
    
    public boolean getBuildRailroad()
    {
        if (isPTWOrConquests())
            return PTWbuildRailroad;
        else
            return buildRailroad;
    }

    public void setBuildRailroad(boolean buildRailroad)
    {
        if (isPTWOrConquests())
            PTWbuildRailroad = buildRailroad;
        else
            this.buildRailroad = buildRailroad;
    }
    
    public boolean getBuildFort()
    {
        if (isPTWOrConquests())
            return PTWbuildFort;
        else
            return buildFort;
    }

    public void setBuildFort(boolean buildFort)
    {
        if (isPTWOrConquests())
            PTWbuildFort = buildFort;
        else
            this.buildFort = buildFort;
    }
    
    public boolean getBuildMine()
    {
        if (isPTWOrConquests())
            return PTWbuildMine;
        else
            return buildMine;
    }

    public void setBuildMine(boolean buildMine)
    {
        if (isPTWOrConquests())
            PTWbuildMine = buildMine;
        else
            this.buildMine = buildMine;
    }
    
    public boolean getIrrigate()
    {
        if (isPTWOrConquests())
            return PTWirrigate;
        else
            return irrigate;
    }

    public void setIrrigate(boolean irrigate)
    {
        if (isPTWOrConquests())
            PTWirrigate = irrigate;
        else
            this.irrigate = irrigate;
    }
    
    public boolean getClearForest()
    {
        if (isPTWOrConquests())
            return PTWclearForest;
        else
            return clearForest;
    }

    public void setClearForest(boolean clearForest)
    {
        if (isPTWOrConquests())
            PTWclearForest = clearForest;
        else
            this.clearForest = clearForest;
    }
    
    public boolean getClearJungle()
    {
        if (isPTWOrConquests())
            return PTWclearJungle;
        else
            return clearJungle;
    }

    public void setClearJungle(boolean clearJungle)
    {
        if (isPTWOrConquests())
            PTWclearJungle = clearJungle;
        else
            this.clearJungle = clearJungle;
    }
    
    public boolean getPlantForest()
    {
        if (isPTWOrConquests())
            return PTWplantForest;
        else
            return plantForest;
    }

    public void setPlantForest(boolean plantForest)
    {
        if (isPTWOrConquests())
            PTWplantForest = plantForest;
        else
            this.plantForest = plantForest;
    }
    
    public boolean getClearPollution()
    {
        if (isPTWOrConquests())
            return PTWclearPollution;
        else
            return clearPollution;
    }

    public void setClearPollution(boolean clearPollution)
    {
        if (isPTWOrConquests())
            PTWclearPollution = clearPollution;
        else
            this.clearPollution = clearPollution;
    }
    
    public boolean getAutomate()
    {
        if (isPTWOrConquests())
            return PTWautomate;
        else
            return automate;
    }

    public void setAutomate(boolean automate)
    {
        if (isPTWOrConquests())
            PTWautomate = automate;
        else
            this.automate = automate;
    }
    
    public boolean getJoinCity()
    {
        if (isPTWOrConquests())
            return PTWjoinCity;
        else
            return joinCity;
    }

    public void setJoinCity(boolean joinCity)
    {
        if (isPTWOrConquests())
            PTWjoinCity = joinCity;
        else
            this.joinCity = joinCity;
    }
    
    public boolean getBuildAirfield()
    {
        if (isPTWOrConquests())
            return PTWbuildAirfield;
        else
            return false;
    }
    
    public void setBuildAirfield(boolean buildAirfield)
    {
        if (isPTWOrConquests())
            PTWbuildAirfield = buildAirfield;
    }
    
    public boolean getBuildRadarTower()
    {
        if (isPTWOrConquests())
            return PTWbuildRadarTower;
        else
            return false;
    }
    
    public void setBuildRadarTower(boolean buildRadarTower)
    {
        if (isPTWOrConquests())
            PTWbuildRadarTower = buildRadarTower;
    }
    
    public boolean getBuildOutpost()
    {
        if (isPTWOrConquests())
            return PTWbuildOutpost;
        else
            return false;
    }
    
    public void setBuildOutpost(boolean buildOutpost)
    {
        if (isPTWOrConquests())
            PTWbuildOutpost = buildOutpost;
    }
    
    public boolean getBuildBarricade()
    {
        if (isPTWOrConquests())
            return PTWbuildBarricade;
        else
            return false;
    }
    
    public void setBuildBarricade(boolean buildBarricade)
    {
        if (isPTWOrConquests())
            PTWbuildBarricade = buildBarricade;
    }
    
    public boolean getBomb()
    {
        if (isPTWOrConquests())
            return PTWbomb;
        else
            return bomb;
    }

    public void setBomb(boolean bomb)
    {
        if (isPTWOrConquests())
            PTWbomb = bomb;
        else
            this.bomb = bomb;
    }
    
    public boolean getRecon()
    {
        if (isPTWOrConquests())
            return PTWrecon;
        else
            return recon;
    }

    public void setRecon(boolean recon)
    {
        if (isPTWOrConquests())
            PTWrecon = recon;
        else
            this.recon = recon;
    }
    
    public boolean getIntercept()
    {
        if (isPTWOrConquests())
            return PTWintercept;
        else
            return intercept;
    }

    public void setIntercept(boolean intercept)
    {
        if (isPTWOrConquests())
            PTWintercept = intercept;
        else
            this.intercept = intercept;
    }
    
    public boolean getRebase()
    {
        if (isPTWOrConquests())
            return PTWrebase;
        else
            return rebase;
    }

    public void setRebase(boolean rebase)
    {
        if (isPTWOrConquests())
            PTWrebase = rebase;
        else
            this.rebase = rebase;
    }
    
    public boolean getPrecisionBomb()
    {
        if (isPTWOrConquests())
            return PTWprecisionBomb;
        else
            return precisionBomb;
    }

    public void setPrecisionBomb(boolean precisionBomb)
    {
        if (isPTWOrConquests())
            PTWprecisionBomb = precisionBomb;
        else
            this.precisionBomb = precisionBomb;
    }
    
    public void setWheeled(boolean wheeled)
    {
        this.wheeled = wheeled;
    }
    
    public void setFootSoldier(boolean footSoldier)
    {
        this.footSoldier = footSoldier;
    }
    
    public void setBlitz(boolean blitz)
    {
        this.blitz = blitz;
    }
    
    public void setCruiseMissile(boolean cruiseMissile)
    {
        this.cruiseMissile = cruiseMissile;
    }
    
    public void setAllTerrainAsRoads(boolean allTerrainAsRoads)
    {
        this.allTerrainAsRoads = allTerrainAsRoads;
    }
    
    public void setRadar(boolean radar)
    {
        this.radar = radar;
    }
    
    public void setAmphibiousUnit(boolean amphibiousUnit)
    {
        this.amphibiousUnit = amphibiousUnit;
    }
    
    public void setInvisible(boolean invisible)
    {
        this.invisible = invisible;
    }
    
    public void setTransportsOnlyAirUnits(boolean transportsOnlyAirUnits)
    {
        this.transportsOnlyAirUnits = transportsOnlyAirUnits;
    }
    
    public void setDraftable(boolean draftable)
    {
        this.draftable = draftable;
    }
    
    public void setImmobile(boolean immobile)
    {
        this.immobile = immobile;
    }
    
    public void setSinksInSea(boolean sinksInSea)
    {
        this.sinksInSea = sinksInSea;
    }
    
    public void setSinksInOcean(boolean sinksInOcean)
    {
        this.sinksInOcean = sinksInOcean;
    }
    
    public void setFlagUnit(boolean flagUnit)
    {
        this.flagUnit = flagUnit;
    }
    
    public void setTransportsOnlyFootUnits(boolean transportsOnlyFootUnits)
    {
        this.transportsOnlyFootUnits = transportsOnlyFootUnits;
    }
    
    public void setStartsGoldenAge(boolean startsGoldenAge)
    {
        this.startsGoldenAge = startsGoldenAge;
    }
    
    public void setNuclearWeapon(boolean nuclearWeapon)
    {
        this.nuclearWeapon = nuclearWeapon;
    }
    
    public void setHiddenNationality(boolean hiddenNationality)
    {
        this.hiddenNationality = hiddenNationality;
    }
    
    public void setArmy(boolean army)
    {
        this.army = army;
    }
    
    public void setLeader(boolean leader)
    {
        this.leader = leader;
    }
    
    public void setInfiniteBombardRange(boolean infiniteBombardRange)
    {
        this.infiniteBombardRange = infiniteBombardRange;
    }
    
    public void setStealth(boolean stealth)
    {
        this.stealth = stealth;
    }
    
    public void setDetectInvisible(boolean detectInvisible)
    {
        this.detectInvisible = detectInvisible;
    }
    
    public void setTacticalMissile(boolean tacticalMissile)
    {
        this.tacticalMissile = tacticalMissile;
    }
    
    public void setTransportsOnlyTacticalMissiles(boolean transportsOnlyTacticalMissiles)
    {
        this.transportsOnlyTacticalMissiles = transportsOnlyTacticalMissiles;
    }
    
    public void setRangedAttackAnimations(boolean rangedAttackAnimations)
    {
        this.rangedAttackAnimations = rangedAttackAnimations;
    }
    
    public void setRotateBeforeAttack(boolean rotateBeforeAttack)
    {
        this.rotateBeforeAttack = rotateBeforeAttack;
    }
    
    public void setLethalLandBombardment(boolean lethalLandBombardment)
    {
        this.lethalLandBombardment = lethalLandBombardment;
    }
    
    public void setLethalSeaBombardment(boolean lethalSeaBombardment)
    {
        this.lethalSeaBombardment = lethalSeaBombardment;
    }
    
    public void setKing(boolean king)
    {
        this.king = king;
    }
    
    public void setRequiresEscort(boolean requiresEscort)
    {
        this.requiresEscort = requiresEscort;
    }
    
    public boolean hasOffenceStrategy()
    {
        return this.offence;
    }
    
    public boolean hasDefenceStrategy()
    {
        return this.defenceStrategy;
    }
    
    public boolean hasArtilleryStrategy()
    {
        return this.artillery;
    }
    
    public boolean hasExploreStrategy()
    {
        return this.exploreStrategy;
    }
    
    public boolean hasArmyStrategy()
    {
        return this.armyUnit;
    }
    
    public boolean hasCruiseMissileStrategy()
    {
        return this.cruiseMissileUnit;
    }
    
    public boolean hasAirBombardStrategy()
    {
        return this.airBombard;
    }
    
    public boolean hasAirDefenceStrategy()
    {
        return this.airDefenceStrategy;
    }
    
    public boolean hasNavalPowerStrategy()
    {
        return this.navalPower;
    }
    
    public boolean hasAirTransportStrategy()
    {
        return this.airTransport;
    }
    
    public boolean hasNavalTransportStrategy()
    {
        return this.navalTransport;
    }
    
    public boolean hasNavalCarrierStrategy()
    {
        return this.navalCarrier;
    }
    
    public boolean hasTerraformStrategy()
    {
        return this.terraform;
    }
    
    public boolean hasSettlerStrategy()
    {
        return this.settle;
    }
    
    public boolean hasLeaderUnitStrategy()
    {
        return this.leaderUnit;
    }
    
    public boolean hasKingStrategy()
    {
        return this.kingStrategy;
    }
    
    public boolean hasTacticalNukeStrategy()
    {
        return this.tacticalNuke;
    }
    
    public boolean hasICBMStrategy()
    {
        return this.ICBM;
    }
    
    public boolean hasNavalMissileTransportStrategy()
    {
        return this.navalMissileTransport;
    }
    
    public boolean hasFlagStrategy()
    {
        return this.flagStrategy;
    }
    
    /**
     * Sets whether the movement cost for the terrain at index <i>terrainIndex</i> is ignored.
     * @param terrainIndex The index
     * @param value Whether the terrain's movement cost should be ignored.
     */
    public void setIgnoreTerrain(int terrainIndex, boolean value)
    {
        this.ignoreMovementCost[terrainIndex] = (byte)(value ? 1 : 0);
    }
    
    /**
     * Sets the ignored movement cost by byte.  This is package-private because
     * it is only to be used by IO.java.
     * @param terrainIndex The terrain index
     * @param value The byte value
     */
    void setIgnoredMovementCost(int terrainIndex, byte value)
    {
        this.ignoreMovementCost[terrainIndex] = value;
    }
    
    byte getIgnoredMovementCost(int terrainIndex)
    {
        return this.ignoreMovementCost[terrainIndex];
    }
    
    public boolean ignoresTerrain(int terrainIndex)
    {
        return this.ignoreMovementCost[terrainIndex] > 0;
    }
    
    public int getNumIgnoreTerrains()
    {
        return this.ignoreMovementCost.length;
    }
    
    public String getName()
    {
        return this.name;
    }

    public int getIconIndex() {
        return iconIndex;
    }
    
    public int getZoneOfControl() {
        return zoneOfControl;
    }

    public String getCivilopediaEntry() {
        return civilopediaEntry;
    }

    public int getBombardStrength() {
        return bombardStrength;
    }

    public int getBombardRange() {
        return bombardRange;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public int getShieldCost() {
        return shieldCost;
    }
    
    public int getDefence() {
        return defence;
    }
    
    public int getHitPointBonus() {
        return hitPointBonus;
    }
    
    public int getAttack() {
        return attack;
    }
    
    public int getOperationalRange() {
        return operationalRange;
    }
    
    public int getRateOfFire() {
        return rateOfFire;
    }
    
    public int getPopulationCost() {
        return populationCost;
    }
    
    public int getMovement() {
        return movement;
    }
    
    public int getRequiredTech() {
        return requiredTechInt;
    }
    
    public int getUpgradeTo() {
        return upgradeToInt;
    }
    
    public int getRequiredResource1() {
        return requiredResource1Int;
    }
    
    public int getRequiredResource2() {
        return requiredResource2Int;
    }
    
    public int getRequiredResource3() {
        return requiredResource3Int;
    }
    
    public int getUnitClass() {
        return unitClass;
    }
    
    public int getOtherStrategy() {
        return otherStrategy;
    }
    
    public int getBombardEffects() {
        return bombardEffects;
    }
    
    public int getRequiresSupport() {
        return requiresSupport;
    }
    
    public int getUseExactCost() {
        return useExactCost;
    }
    
    public int getTelepadRange() {
        return telepadRange;
    }
    
    public int getQuestionMark3() {
        return questionMark3;
    }
    
    public int getEnslaveResultsIn() {
        return enslaveResultsInInt;
    }
    
    public int getQuestionMark5() {
        return questionMark5;
    }
    
    public int getQuestionMark6() {
        return questionMark6;
    }
    
    public int getCreatesCraters() {
        return createsCraters;
    }
    
    public float getWorkerStrength() {
        return workerStrengthFloat;
    }
    
    public int getAirDefence() {
        return airDefence;
    }
    
    public int getQuestionMark8() {
        return questionMark8;
    }
    
    public int getNumStrategies() {
        return numStrategies;
    }
    
    public boolean isWheeled() {
        return wheeled;
    }
    
    public boolean isFootSoldier() {
        return footSoldier;
    }
    
    public boolean isBlitz() {
        return blitz;
    }
    
    public boolean isCruiseMissile() {
        return cruiseMissile;
    }
    
    public boolean treatsAllTerrainAsRoads() {
        return allTerrainAsRoads;
    }
    
    public boolean hasRadar() {
        return radar;
    }
    
    public boolean isAmphibious() {
        return amphibiousUnit;
    }
    
    public boolean isInvisible() {
        return invisible;
    }
    
    public boolean transportsOnlyAirUnits() {
        return transportsOnlyAirUnits;
    }
    
    public boolean isDraftable() {
        return draftable;
    }
    
    public boolean isImmobile() {
        return immobile;
    }
    
    public boolean sinksInSea() {
        return sinksInSea;
    }
    
    public boolean sinksInOcean() {
        return sinksInOcean;
    }
    
    public boolean isFlagUnit() {
        return flagUnit;
    }
    
    public boolean transportsOnlyFootUnits() {
        return transportsOnlyFootUnits;
    }
    
    public boolean startsGoldenAge() {
        return startsGoldenAge;
    }
    
    public boolean isNuclearWeapon() {
        return nuclearWeapon;
    }
    
    public boolean hasHiddenNationality() {
        return hiddenNationality;
    }
    
    public boolean isArmy() {
        return army;
    }
    
    public boolean isLeader() {
        return leader;
    }
    
    public boolean hasInfiniteBombardRange() {
        return infiniteBombardRange;
    }
    
    public boolean isStealth() {
        return stealth;
    }
    
    public boolean detectsInvisible() {
        return detectInvisible;
    }
    
    public boolean isTacticalMissile() {
        return tacticalMissile;
    }
    
    public boolean transportsOnlyTacticalMissiles() {
        return transportsOnlyTacticalMissiles;
    }
    
    public boolean hasRangedAttackAnimation() {
        return rangedAttackAnimations;
    }
    
    public boolean rotatesBeforeAttack() {
        return rotateBeforeAttack;
    }
    
    public boolean hasLethalLandBombardment() {
        return lethalLandBombardment;
    }
    
    public boolean hasLethalSeaBombardment() {
        return lethalSeaBombardment;
    }
    
    public boolean isKing() {
        //return isElvis();
        return king;
    }
    
    public boolean requiresEscort() {
        return requiresEscort;
    }

    public int getDataLength()
    {
        return dataLength;
    }
    
    public int getNumLegalUnitTelepads()
    {
        return legalUnitTelepadsInt.size();
    }
    
    public int getNumLegalBuildingTelepads()
    {
        return legalBuildingTelepads.size();
    }
    
    public int getNumStealthTargets()
    {
        return stealthTargetsInt.size();
    }
    
    public void clearUnitTelepads()
    {
        this.dataLength = this.dataLength - 4 * legalUnitTelepadsInt.size();
        this.legalUnitTelepadsInt.clear();
    }
    
    public void addUnitTelepad(int unitID)
    {
        this.dataLength+=4;
        this.legalUnitTelepadsInt.add(unitID);
    }
    
    public void clearBuildingTelepads()
    {
        this.dataLength = this.dataLength - 4 * legalBuildingTelepads.size();
        this.legalBuildingTelepads.clear();
    }
    
    public void addBuildingTelepad(int buildingID)
    {
        this.dataLength+=4;
        this.legalBuildingTelepads.add(buildingID);
    }
    
    /**
     * Clears out the list of stealth targets.  To be used when changing the
     * collection of targets.
     */
    public void clearStealthAttackTargets()
    {
        this.dataLength = this.dataLength - 4 * stealthTargetsInt.size();
        this.stealthTargetsInt.clear();
    }
    
    public void addStealthAttackTarget(int unitID)
    {
        this.dataLength+=4;
        this.stealthTargetsInt.add(unitID);
    }
    
    /**
     * Returns an unmodifiable copy of the unit telepads.
     * Examples use cases include saving the file and displaying the telepads in
     * an editor.
     * @return 
     */
    public List<Integer> getUnitTelepads()
    {
        return Collections.unmodifiableList(legalUnitTelepadsInt);
    }
    
    /**
     * Returns an unmodifiable copy of the building telepads.
     * Examples use cases include saving the file and displaying the telepads in
     * an editor.
     * @return 
     */
    public List<Integer>getBuildingTelepads()
    {
        return Collections.unmodifiableList(legalBuildingTelepads);
    }
    
    /**
     * Returns an unmodifiable copy of the stealth targets.
     * Examples use cases include saving the file and displaying the telepads in
     * an editor.
     * @return 
     */
    public List<Integer> getStealthTargets()
    {
        return Collections.unmodifiableList(stealthTargetsInt);
    }
    
    /**
     * Updates the stealth attack list indexes, in case a unit is swapped.
     * Note that the stealthTargets array has a size equal to the # of stealth targets, not all units.
     * Thus the index within that list differs from the overall index of the swapped unit.
     */
    public void updateStealthTargetIndices() {
        this.stealthTargetsInt.clear();
        for (PRTO target : stealthTargets) {
            stealthTargetsInt.add(target.getIndex());
        }
    }
    
    /**
     * Updates the legal unit telepad indexes, in case a unit is swapped.
     * Note that the legal telepads array has a size equal to the # of legal unit telepads, not all units.
     * Thus the index within that list differs from the overall index of the swapped unit.
     */
    public void updateUnitTelepadIndices() {
        this.legalUnitTelepadsInt.clear();
        for (PRTO target : legalUnitTelepads) {
            legalUnitTelepadsInt.add(target.getIndex());
        }
    }
    
    /**
     * Converts the BIQ from Vanilla to Conquests.  This involves moving from
     * the Vanilla PRTO aspects to PTW/Conquests ones.
     * 
     * Unit Abilities stay the same.
     * AI Strategy stays the same.
     * Standard Orders/Special Actions splits into PTW Standard Orders, PTW Special Actions,
     * and PTW Work Actions.
     * Air Missions becomes PTW Air Missions
     * I haven't found a case where PTW Actions Mix is necessary (maybe PTW itself?), so
     * I'm not bothering with it currently.
     */
    public void convertFromVanillaToConquests() {     
        //Maybe: Add two bytes for Marsh/Volcano terrains, before Coast
        //Not sure yet; Firaxis's editor adds those terrains when opening a .bic,
        //but this one doesn't.  So the correct answer is not clear.
        //The question is, will chaos ensue if Civ3 opens a .biq with only 12 terrains?
        //The answer is probably "yes", but let's find out.
        
        //Air Missions are easy since they don't change.
        this.PTWAirMissions = this.airMissions;
        this.extractPTWAirMissions();
        
        //Standard Orders - The PTW standard orders are more numerous than Vanilla,
        //so we can't just copy it over.
        this.PTWskipTurn = this.skipTurn;
        this.PTWwait = this.wait;
        this.PTWfortify = this.fortify;
        this.PTWdisband = this.disband;
        this.PTWgoTo = this.goTo;
        this.PTWexploreOrder = false;    //new
        this.PTWsentry = false;     //new
        this.recalculatePTWStandardOrders();    //compute the full one
        
        //Special Actions - These start mid-int in Vanilla, but in a fresh byte in PTW
        this.PTWload = this.load;
        this.PTWunload = this.unload;
        this.PTWairlift = this.airlift;
        this.PTWpillage = this.pillage;
        this.PTWbombard = this.bombard;
        this.PTWairdrop = this.airdrop;
        this.PTWbuildArmy = this.buildArmy;
        this.PTWfinishImprovements = this.finishImprovement;
        this.PTWupgradeUnit = this.upgrade;
        this.PTWcapture = false;
        this.recalculatePTWSpecialActions();
        
        //Worker Actions - These also start mid-int in Vanilla, but fresh in PTW
        this.PTWbuildColony = buildColony;
        this.PTWbuildCity = buildCity;
        this.PTWbuildRoad = buildRoad;
        this.PTWbuildRailroad = buildRailroad;
        this.PTWbuildFort = buildFort;
        this.PTWbuildMine = buildMine;
        this.PTWirrigate = irrigate;
        this.PTWclearForest = clearForest;
        this.PTWclearJungle = clearJungle;
        this.PTWplantForest = plantForest;
        this.PTWclearPollution = clearPollution;
        this.PTWautomate = automate;
        this.PTWjoinCity = joinCity;
        this.PTWbuildAirfield = false;
        this.PTWbuildRadarTower = false;
        this.PTWbuildOutpost = false;
        this.recalculatePTWWorkerActions();
    }
    
    public void convertFromPTWToConquests()
    {
        //TODO: Add conversion code, if necessary
    }
    
    public void handleDeletedCivilization(int index, int civSize)
    {
        for (int i = index; i < civSize; i++)
        {
            availableToArray[i] = availableToArray[i + 1];
        }
        availableToArray[civSize] = false;
        recalculateAvailableTo();
    }
    
    public void handleDeletedResource(int index)
    {
        if (requiredResource1Int == index)
            requiredResource1Int = -1;
        else if (requiredResource1Int > index)
            requiredResource1Int--;
        if (requiredResource2Int == index)
            requiredResource2Int = -1;
        else if (requiredResource2Int > index)
            requiredResource2Int--;
        if (requiredResource3Int == index)
            requiredResource3Int = -1;
        else if (requiredResource3Int > index)
            requiredResource3Int--;
    }
    
    public void handleDeletedTechnology(int index)
    {
        if (requiredTechInt == index) {
            requiredTechInt = -1;
            requiredTech = null;
        }
        else if (requiredTechInt > index)
            requiredTechInt--;
    }
    
    public void handleSwappedTech() {
        if (this.requiredTechInt != -1) {
            this.requiredTechInt = requiredTech.getIndex();
        }
    }
    
    public void handleDeletedUnit(int index) {
        if (upgradeToInt == index) {
            upgradeToInt = -1;
            upgradeTo = null;
        }
        else if (upgradeToInt > index)
            upgradeToInt--;
        
        if (enslaveResultsInInt == index) {
            enslaveResultsInInt = -1;
            enslaveResultsIn = null;
        }
        else if (enslaveResultsInInt > index) {
            enslaveResultsInInt--;
        }
        
        List<Integer> newStealthTargetList = new ArrayList<Integer>();
        int stealthIndex = 0;
        for (int stealthTarget : this.stealthTargetsInt) {
            if (stealthTarget < index) {
                newStealthTargetList.add(stealthTarget);
            }
            else if (stealthTarget == index) {
                //no longer a stealth target, don't carry over
                this.stealthTargets.remove(stealthIndex);
            }
            else {
                newStealthTargetList.add(stealthTarget - 1);
            }
            stealthIndex++;
        }
        this.stealthTargetsInt = newStealthTargetList;
        
        if (this.index > index) {
            this.index--;
        }
        else if (this.index == index) {
            this.index = -1;
        }
    }
    
    /**
     * Convenience method that returns whether the BIQ is PTW/Conquests (true), or vanilla (false)
     * @return True if PTW or Conquests
     */
    private boolean isPTWOrConquests()
    {
        return baseLink.version.ordinal() >= civ3Version.PTW.ordinal();
    }

    @Override
    public String toString()
    {
            String lineReturn = java.lang.System.getProperty("line.separator");
            String toReturn =  "dataLength: " + dataLength + lineReturn;
            toReturn = toReturn + "index: " + index + lineReturn;
            toReturn = toReturn + "zoneOfControl: " + zoneOfControl + lineReturn;
            toReturn = toReturn + "name: " + name + lineReturn;
            toReturn = toReturn + "civilopediaEntry: " + civilopediaEntry + lineReturn;
            toReturn = toReturn + "bombardStrength: " + bombardStrength + lineReturn;
            toReturn = toReturn + "bombardRange: " + bombardRange + lineReturn;
            toReturn = toReturn + "capacity: " + capacity + lineReturn;
            toReturn = toReturn + "shieldCost: " + shieldCost + lineReturn;
            toReturn = toReturn + "defence: " + defence + lineReturn;
            toReturn = toReturn + "iconIndex: " + iconIndex + lineReturn;
            toReturn = toReturn + "attack: " + attack + lineReturn;
            toReturn = toReturn + "operationalRange: " + operationalRange + lineReturn;
            toReturn = toReturn + "populationCost: " + populationCost + lineReturn;
            toReturn = toReturn + "rateOfFire: " + rateOfFire + lineReturn;
            toReturn = toReturn + "movement: " + movement + lineReturn;
            toReturn = toReturn + "requiredTech: " + requiredTechInt + lineReturn;
            toReturn = toReturn + "upgradeTo: " + upgradeToInt + lineReturn;
            toReturn = toReturn + "requiredResource1: " + requiredResource1Int + lineReturn;
            toReturn = toReturn + "requiredResource2: " + requiredResource2Int + lineReturn;
            toReturn = toReturn + "requiredResource3: " + requiredResource3Int + lineReturn;
            toReturn = toReturn + "unitAbilities: " + unitAbilities + lineReturn;
            toReturn = toReturn + "AIStrategy: " + AIStrategy + lineReturn;
            toReturn = toReturn + "availableTo: " + availableTo + lineReturn;
            toReturn = toReturn + "standardOrdersSpecialActions: " + standardOrdersSpecialActions + lineReturn;
            toReturn = toReturn + "airMissions: " + airMissions + lineReturn;
            toReturn = toReturn + "unitClass: " + unitClass + lineReturn;
            toReturn = toReturn + "otherStrategy: " + otherStrategy + lineReturn;
            toReturn = toReturn + "hitPointBonus: " + hitPointBonus + lineReturn;
            toReturn = toReturn + "PTWStandardOrders: " + PTWStandardOrders + lineReturn;
            toReturn = toReturn + "PTWSpecialActions: " + PTWSpecialActions + lineReturn;
            toReturn = toReturn + "PTWWorkerActions: " + PTWWorkerActions + lineReturn;
            toReturn = toReturn + "PTWAirMissions: " + PTWAirMissions + lineReturn;
            toReturn = toReturn + "PTWActionsMix: " + PTWActionsMix + lineReturn;
            toReturn = toReturn + "bombardEffects: " + bombardEffects + lineReturn;
            for (int j = 0; j < 14; j++)
            {
                toReturn = toReturn + "ignore movement cost of terrain " + j + "?  " + ignoreMovementCost[j] + lineReturn;
            }
            toReturn = toReturn + "requiresSupport: " + requiresSupport + lineReturn;
            toReturn = toReturn + "useExactCost: " + useExactCost + lineReturn;
            toReturn = toReturn + "telepadRange: " + telepadRange + lineReturn;
            toReturn = toReturn + "questionMark3: " + questionMark3 + lineReturn;
            toReturn = toReturn + "numLegalUnitTelepads: " + getNumLegalUnitTelepads() + lineReturn;
            for (int j = 0; j < getNumLegalUnitTelepads(); j++)
            {
                toReturn = toReturn + "  legal unit telepad: " +legalUnitTelepadsInt.get(j) + lineReturn;
            }
            toReturn = toReturn + "enslaveResultsIn: " + enslaveResultsInInt + lineReturn;
            toReturn = toReturn + "questionMark5: " + questionMark5 + lineReturn;
            toReturn = toReturn + "numStealthTargets: " + stealthTargetsInt.size() + lineReturn;
            for (int j = 0; j < stealthTargetsInt.size(); j++)
            {
                toReturn = toReturn + "stealth target: " + stealthTargetsInt.get(j) + lineReturn;
            }
            toReturn = toReturn + "questionMark6: " + questionMark6 + lineReturn;
            toReturn = toReturn + "numLegalBuildingTelepads: " + getNumLegalBuildingTelepads() + lineReturn;
            for (int j = 0; j < getNumLegalBuildingTelepads(); j++)
            {
                toReturn = toReturn + "  legal building telepad: " +legalBuildingTelepads.get(j) + lineReturn;
            }
            toReturn = toReturn + "createsCraters: " + createsCraters + lineReturn;
            toReturn = toReturn + "workerStrength1: " + workerStrength1 + lineReturn;
            toReturn = toReturn + "workerStrengthFloat: " + workerStrengthFloat + lineReturn;
            toReturn = toReturn + "questionMark8: " + questionMark8 + lineReturn;
            toReturn = toReturn + "airDefence: " + airDefence + lineReturn;
            toReturn = toReturn + lineReturn;
        return toReturn;
    }

    /**
     * This method takes the binary representation of data and extracts it into
     * human-comprehensible variables (often booleans).  It is broken down into
     * several subsections to make modification and reference quicker.
     */
    public void extractEnglish()
    {
        extractUnitAbilities();
        extractAIStrategy();
        extractCivsAvailableTo();
        extractOrders();
        extractAirMissions();
        extractPTWStandardOrders();
        extractPTWSpecialActions();
        extractPTWWorkerActions();
        extractPTWAirMissions();
        extractPTWActionMix();
    }
    
    /**
     * Writes ONLY the xth strategy to file
     * @param out 
     */
    void writeXthStrategy(int x, LittleEndianDataOutputStream out)
    {
        int number = 1;
        int numStratsFound = 0;
        for (int i = 0; i < strategies.length; i++)
        {
            //System.out.printf("strategies[%d] = ", i);
            //System.out.println(strategies[i]);
            if (strategies[i])
            {
                numStratsFound++;
                if (numStratsFound == x)
                {
                    try{
                        out.writeInt(number);
                    }
                    catch(IOException e)
                    {
                        //TODO: Throw
                    }
                    return;
                }
            }
            number = number << 1;
        }
        //If a unit has no strategies, write a 0
        try{
            out.writeInt(0);
        }
        catch(IOException e)
        {
            //TODO: Throw
        }
    }
    
    /**
     * Intentionally package-level to allow IO.java to call it when a
     * strategy map unit is imported.
     */
    void extractAIStrategy()
    {
        int AIStrategyCopy = AIStrategy;
        numStrategies = 0;
        int divBy = AIStrategyCopy/(int)(Math.pow(2, 19));
        if (divBy == 1)
        {
            kingStrategy = true;
            AIStrategyCopy-=524288;
            numStrategies++;
        }
        divBy = AIStrategyCopy/(int)(Math.pow(2, 18));
        if (divBy == 1)
        {
            flagStrategy = true;
            AIStrategyCopy-=262144;
            numStrategies++;
        }
        divBy = AIStrategyCopy/(int)(Math.pow(2, 17));
        if (divBy == 1)
        {
            navalMissileTransport = true;
            AIStrategyCopy-=131072;
            numStrategies++;
        }
        divBy = AIStrategyCopy/(int)(Math.pow(2, 16));
        if (divBy == 1)
        {
            ICBM = true;
            AIStrategyCopy-=65536;
            numStrategies++;
        }
        divBy = AIStrategyCopy/(int)(Math.pow(2, 15));
        if (divBy == 1)
        {
            tacticalNuke = true;
            AIStrategyCopy-=32768;
            numStrategies++;
        }
        divBy = AIStrategyCopy/(int)(Math.pow(2, 14));
        if (divBy == 1)
        {
            leaderUnit = true;
            AIStrategyCopy-=16384;
            numStrategies++;
        }
        divBy = AIStrategyCopy/(int)(Math.pow(2, 13));
        if (divBy == 1)
        {
            settle = true;
            AIStrategyCopy-=8192;
            numStrategies++;
        }
        divBy = AIStrategyCopy/(int)(Math.pow(2, 12));
        if (divBy == 1)
        {
            terraform = true;
            AIStrategyCopy-=4096;
            numStrategies++;
        }
        divBy = AIStrategyCopy/(int)(Math.pow(2, 11));
        if (divBy == 1)
        {
            navalCarrier = true;
            AIStrategyCopy-=2048;
            numStrategies++;
        }
        divBy = AIStrategyCopy/(int)(Math.pow(2, 10));
        if (divBy == 1)
        {
            navalTransport = true;
            AIStrategyCopy-=1024;
            numStrategies++;
        }
        divBy = AIStrategyCopy/(int)(Math.pow(2, 9));
        if (divBy == 1)
        {
            airTransport = true;
            AIStrategyCopy-=512;
            numStrategies++;
        }
        divBy = AIStrategyCopy/(int)(Math.pow(2, 8));
        if (divBy == 1)
        {
            navalPower = true;
            AIStrategyCopy-=256;
            numStrategies++;
        }
        divBy = AIStrategyCopy/(int)(Math.pow(2, 7));
        if (divBy == 1)
        {
            airDefenceStrategy = true;
            AIStrategyCopy-=128;
            numStrategies++;
        }
        divBy = AIStrategyCopy/(int)(Math.pow(2, 6));
        if (divBy == 1)
        {
            airBombard = true;
            AIStrategyCopy-=64;
            numStrategies++;
        }
        divBy = AIStrategyCopy/(int)(Math.pow(2, 5));
        if (divBy == 1)
        {
            cruiseMissileUnit = true;
            AIStrategyCopy-=32;
            numStrategies++;
        }
        divBy = AIStrategyCopy/(int)(Math.pow(2, 4));
        if (divBy == 1)
        {
            armyUnit = true;
            AIStrategyCopy-=16;
            numStrategies++;
        }
        divBy = AIStrategyCopy/(int)(Math.pow(2, 3));
        if (divBy == 1)
        {
            exploreStrategy = true;
            AIStrategyCopy-=8;
            numStrategies++;
        }
        divBy = AIStrategyCopy/(int)(Math.pow(2, 2));
        if (divBy == 1)
        {
            artillery = true;
            AIStrategyCopy-=4;
            numStrategies++;
        }
        divBy = AIStrategyCopy/(int)(Math.pow(2, 1));
        if (divBy == 1)
        {
            defenceStrategy = true;
            AIStrategyCopy-=2;
            numStrategies++;
        }
        divBy = AIStrategyCopy/(int)(Math.pow(2, 0));
        if (divBy == 1)
        {
            offence = true;
            AIStrategyCopy-=1;
            numStrategies++;
        }
        //now get the list put together again
        Boolean[]strategies2 = {offence, defenceStrategy, artillery, exploreStrategy, armyUnit, cruiseMissileUnit, airBombard, airDefenceStrategy, navalPower, airTransport, navalTransport, navalCarrier, terraform, settle, leaderUnit, tacticalNuke, ICBM, navalMissileTransport, flagStrategy, kingStrategy};
        strategies = strategies2;
    }

    private void extractCivsAvailableTo()
    {
        long availableToCopy = availableTo;
        if (availableToCopy < 0)
            availableToCopy += (long)Math.pow(2, 32);
        int divBy = (int)(availableToCopy/(long)(Math.pow(2, 31)));
        int availableToCount = 31;
        long subtractNum = 2147483648L;
        int power = 30;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        subtractNum/=2;
        divBy = (int)(availableToCopy/(long)(Math.pow(2, power)));
        power--;
        availableToCount--;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        subtractNum/=2;
        divBy = (int)(availableToCopy/(long)(Math.pow(2, power)));
        power--;
        availableToCount--;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        subtractNum/=2;
        divBy = (int)(availableToCopy/(long)(Math.pow(2, power)));
        power--;
        availableToCount--;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        subtractNum/=2;
        divBy = (int)(availableToCopy/(long)(Math.pow(2, power)));
        power--;
        availableToCount--;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        subtractNum/=2;
        divBy = (int)(availableToCopy/(long)(Math.pow(2, power)));
        power--;
        availableToCount--;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        subtractNum/=2;
        divBy = (int)(availableToCopy/(long)(Math.pow(2, power)));
        power--;
        availableToCount--;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        subtractNum/=2;
        divBy = (int)(availableToCopy/(long)(Math.pow(2, power)));
        power--;
        availableToCount--;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        subtractNum/=2;
        divBy = (int)(availableToCopy/(long)(Math.pow(2, power)));
        power--;
        availableToCount--;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        subtractNum/=2;
        divBy = (int)(availableToCopy/(long)(Math.pow(2, power)));
        power--;
        availableToCount--;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        subtractNum/=2;
        divBy = (int)(availableToCopy/(long)(Math.pow(2, power)));
        power--;
        availableToCount--;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        subtractNum/=2;
        divBy = (int)(availableToCopy/(long)(Math.pow(2, power)));
        power--;
        availableToCount--;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        subtractNum/=2;
        divBy = (int)(availableToCopy/(long)(Math.pow(2, power)));
        power--;
        availableToCount--;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        subtractNum/=2;
        divBy = (int)(availableToCopy/(long)(Math.pow(2, power)));
        power--;
        availableToCount--;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        subtractNum/=2;
        divBy = (int)(availableToCopy/(long)(Math.pow(2, power)));
        power--;
        availableToCount--;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        subtractNum/=2;
        divBy = (int)(availableToCopy/(long)(Math.pow(2, power)));
        power--;
        availableToCount--;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        subtractNum/=2;
        divBy = (int)(availableToCopy/(long)(Math.pow(2, power)));
        power--;
        availableToCount--;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        subtractNum/=2;
        divBy = (int)(availableToCopy/(long)(Math.pow(2, power)));
        power--;
        availableToCount--;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        subtractNum/=2;
        divBy = (int)(availableToCopy/(long)(Math.pow(2, power)));
        power--;
        availableToCount--;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        subtractNum/=2;
        divBy = (int)(availableToCopy/(long)(Math.pow(2, power)));
        power--;
        availableToCount--;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        subtractNum/=2;
        divBy = (int)(availableToCopy/(long)(Math.pow(2, power)));
        power--;
        availableToCount--;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        subtractNum/=2;
        divBy = (int)(availableToCopy/(long)(Math.pow(2, power)));
        power--;
        availableToCount--;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        subtractNum/=2;
        divBy = (int)(availableToCopy/(long)(Math.pow(2, power)));
        power--;
        availableToCount--;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        subtractNum/=2;
        divBy = (int)(availableToCopy/(long)(Math.pow(2, power)));
        power--;
        availableToCount--;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        subtractNum/=2;
        divBy = (int)(availableToCopy/(long)(Math.pow(2, power)));
        power--;
        availableToCount--;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        subtractNum/=2;
        divBy = (int)(availableToCopy/(long)(Math.pow(2, power)));
        power--;
        availableToCount--;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        subtractNum/=2;
        divBy = (int)(availableToCopy/(long)(Math.pow(2, power)));
        power--;
        availableToCount--;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        subtractNum/=2;
        divBy = (int)(availableToCopy/(long)(Math.pow(2, power)));
        power--;
        availableToCount--;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        subtractNum/=2;
        divBy = (int)(availableToCopy/(long)(Math.pow(2, power)));
        power--;
        availableToCount--;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        subtractNum/=2;
        divBy = (int)(availableToCopy/(long)(Math.pow(2, power)));
        power--;
        availableToCount--;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        subtractNum/=2;
        divBy = (int)(availableToCopy/(long)(Math.pow(2, power)));
        power--;
        availableToCount--;
        if (divBy == 1)
        {
            availableToArray[availableToCount] = true;
            availableToCopy-=subtractNum;
        }
        power--;
        availableToCount--;
        assert(availableToCount == 0):"Wrong number of iterations of unrolled loop";
    }

    /**
     * This extracts the STANDARD orders/special actions
     * These are no longer used by the game (as of PTW?)
     *
     * @deprecated 
     */
    private void extractOrders()
    {
        int ordersCopy = standardOrdersSpecialActions;
        int divBy = ordersCopy/(int)(Math.pow(2, 26));
        if (divBy == 1)
        {
            joinCity = true;
            ordersCopy-=67108864;
        }
        divBy = ordersCopy/(int)(Math.pow(2, 25));
        if (divBy == 1)
        {
            automate = true;
            ordersCopy-=33554432;
        }
        divBy = ordersCopy/(int)(Math.pow(2, 24));
        if (divBy == 1)
        {
            clearPollution = true;
            ordersCopy-=16777216;
        }
        divBy = ordersCopy/(int)(Math.pow(2, 23));
        if (divBy == 1)
        {
            plantForest = true;
            ordersCopy-=8388608;
        }
        divBy = ordersCopy/(int)(Math.pow(2, 22));
        if (divBy == 1)
        {
            clearJungle = true;
            ordersCopy-=4194304;
        }
        divBy = ordersCopy/(int)(Math.pow(2, 21));
        if (divBy == 1)
        {
            clearForest = true;
            ordersCopy-=2097152;
        }
        divBy = ordersCopy/(int)(Math.pow(2, 20));
        if (divBy == 1)
        {
            irrigate = true;
            ordersCopy-=1048576;
        }
        divBy = ordersCopy/(int)(Math.pow(2, 19));
        if (divBy == 1)
        {
            buildMine = true;
            ordersCopy-=524288;
        }
        divBy = ordersCopy/(int)(Math.pow(2, 18));
        if (divBy == 1)
        {
            buildFort = true;
            ordersCopy-=262144;
        }
        divBy = ordersCopy/(int)(Math.pow(2, 17));
        if (divBy == 1)
        {
            buildRailroad = true;
            ordersCopy-=131072;
        }
        divBy = ordersCopy/(int)(Math.pow(2, 16));
        if (divBy == 1)
        {
            buildRoad = true;
            ordersCopy-=65536;
        }
        divBy = ordersCopy/(int)(Math.pow(2, 15));
        if (divBy == 1)
        {
            buildCity = true;
            ordersCopy-=32768;
        }
        divBy = ordersCopy/(int)(Math.pow(2, 14));
        if (divBy == 1)
        {
            buildColony = true;
            ordersCopy-=16384;
        }
        divBy = ordersCopy/(int)(Math.pow(2, 13));
        if (divBy == 1)
        {
            upgrade = true;
            ordersCopy-=8192;
        }
        divBy = ordersCopy/(int)(Math.pow(2, 12));
        if (divBy == 1)
        {
            finishImprovement = true;
            ordersCopy-=4096;
        }
        divBy = ordersCopy/(int)(Math.pow(2, 11));
        if (divBy == 1)
        {
            buildArmy = true;
            ordersCopy-=2048;
        }
        divBy = ordersCopy/(int)(Math.pow(2, 10));
        if (divBy == 1)
        {
            airdrop = true;
            ordersCopy-=1024;
        }
        divBy = ordersCopy/(int)(Math.pow(2, 9));
        if (divBy == 1)
        {
            bombard = true;
            ordersCopy-=512;
        }
        divBy = ordersCopy/(int)(Math.pow(2, 8));
        if (divBy == 1)
        {
            pillage = true;
            ordersCopy-=256;
        }
        divBy = ordersCopy/(int)(Math.pow(2, 7));
        if (divBy == 1)
        {
            airlift = true;
            ordersCopy-=128;
        }
        divBy = ordersCopy/(int)(Math.pow(2, 6));
        if (divBy == 1)
        {
            unload = true;
            ordersCopy-=64;
        }
        divBy = ordersCopy/(int)(Math.pow(2, 5));
        if (divBy == 1)
        {
            load = true;
            ordersCopy-=32;
        }
        divBy = ordersCopy/(int)(Math.pow(2, 4));
        if (divBy == 1)
        {
            goTo = true;
            ordersCopy-=16;
        }
        divBy = ordersCopy/(int)(Math.pow(2, 3));
        if (divBy == 1)
        {
            disband = true;
            ordersCopy-=8;
        }
        divBy = ordersCopy/(int)(Math.pow(2, 2));
        if (divBy == 1)
        {
            fortify = true;
            ordersCopy-=4;
        }
        divBy = ordersCopy/(int)(Math.pow(2, 1));
        if (divBy == 1)
        {
            wait = true;
            ordersCopy-=2;
        }
        divBy = ordersCopy/(int)(Math.pow(2, 0));
        if (divBy == 1)
        {
            skipTurn = true;
            ordersCopy-=1;
        }
    }

    /**
     * This extracts the STANDARD air missions
     * These are no longer used (as of PTW?)
     *
     * @deprecated 
     */
    private void extractAirMissions()
    {
        int airMissionCopy = airMissions;
        int divBy = airMissionCopy/(int)(Math.pow(2, 4));
        if (divBy == 1)
        {
            precisionBomb = true;
            airMissionCopy-=16;
        }
        divBy = airMissionCopy/(int)(Math.pow(2, 3));
        if (divBy == 1)
        {
            rebase = true;
            airMissionCopy-=8;
        }
        divBy = airMissionCopy/(int)(Math.pow(2, 2));
        if (divBy == 1)
        {
            intercept = true;
            airMissionCopy-=4;
        }
        divBy = airMissionCopy/(int)(Math.pow(2, 1));
        if (divBy == 1)
        {
            recon = true;
            airMissionCopy-=2;
        }
        divBy = airMissionCopy/(int)(Math.pow(2, 0));
        if (divBy == 1)
        {
            bomb = true;
            airMissionCopy-=1;
        }
    }

    private void extractPTWStandardOrders()
    {
        int PTWStandardOdersCopy = PTWStandardOrders;
        int divBy = PTWStandardOdersCopy/(int)(Math.pow(2, 6));
        if (divBy == 1)
        {
            PTWsentry = true;
            PTWStandardOdersCopy-=64;
        }
        divBy = PTWStandardOdersCopy/(int)(Math.pow(2, 5));
        if (divBy == 1)
        {
            PTWexploreOrder = true;
            PTWStandardOdersCopy-=32;
        }
        divBy = PTWStandardOdersCopy/(int)(Math.pow(2, 4));
        if (divBy == 1)
        {
            PTWgoTo = true;
            PTWStandardOdersCopy-=16;
        }
        divBy = PTWStandardOdersCopy/(int)(Math.pow(2, 3));
        if (divBy == 1)
        {
            PTWdisband = true;
            PTWStandardOdersCopy-=8;
        }
        divBy = PTWStandardOdersCopy/(int)(Math.pow(2, 2));
        if (divBy == 1)
        {
            PTWfortify = true;
            PTWStandardOdersCopy-=4;
        }
        divBy = PTWStandardOdersCopy/(int)(Math.pow(2, 1));
        if (divBy == 1)
        {
            PTWwait = true;
            PTWStandardOdersCopy-=2;
        }
        divBy = PTWStandardOdersCopy/(int)(Math.pow(2, 0));
        if (divBy == 1)
        {
            PTWskipTurn = true;
            PTWStandardOdersCopy-=1;
        }
    }

    private void extractPTWSpecialActions()
    {
        int PTWSpecialActionsCopy = PTWSpecialActions;
        int divBy;
        divBy = PTWSpecialActionsCopy/(int)(Math.pow(2, 21));
        if (divBy == 1)
        {
            scienceAge = true;
            PTWSpecialActionsCopy-=2097152;
        }
        divBy = PTWSpecialActionsCopy/(int)(Math.pow(2, 20));
        if (divBy == 1)
        {
            sacrifice = true;
            PTWSpecialActionsCopy-=1048576;
        }
        divBy = PTWSpecialActionsCopy/(int)(Math.pow(2, 19));
        if (divBy == 1)
        {
            collateralDamage = true;
            PTWSpecialActionsCopy-=524288;
        }
        divBy = PTWSpecialActionsCopy/(int)(Math.pow(2, 18));
        if (divBy == 1)
        {
            enslave = true;
            PTWSpecialActionsCopy-=262144;
        }
        divBy = PTWSpecialActionsCopy/(int)(Math.pow(2, 17));
        if (divBy == 1)
        {
            charm = true;
            PTWSpecialActionsCopy-=131072;
        }
        divBy = PTWSpecialActionsCopy/(int)(Math.pow(2, 16));
        if (divBy == 1)
        {
            stealthAttack = true;
            PTWSpecialActionsCopy-=65536;
        }
        divBy = PTWSpecialActionsCopy/(int)(Math.pow(2, 15));
        if (divBy == 1)
        {
            teleportable = true;
            PTWSpecialActionsCopy-=32768;
        }
        divBy = PTWSpecialActionsCopy/(int)(Math.pow(2, 14));
        if (divBy == 1)
        {
            telepad = true;
            PTWSpecialActionsCopy-=16384;
        }
        divBy = PTWSpecialActionsCopy/(int)(Math.pow(2, 9));
        if (divBy == 1)
        {
            PTWcapture = true;
            PTWSpecialActionsCopy-=512;
        }
        divBy = PTWSpecialActionsCopy/(int)(Math.pow(2, 8));
        if (divBy == 1)
        {
            PTWupgradeUnit = true;
            PTWSpecialActionsCopy-=256;
        }
        divBy = PTWSpecialActionsCopy/(int)(Math.pow(2, 7));
        if (divBy == 1)
        {
            PTWfinishImprovements = true;
            PTWSpecialActionsCopy-=128;
        }
        divBy = PTWSpecialActionsCopy/(int)(Math.pow(2, 6));
        if (divBy == 1)
        {
            PTWbuildArmy = true;
            PTWSpecialActionsCopy-=64;
        }
        divBy = PTWSpecialActionsCopy/(int)(Math.pow(2, 5));
        if (divBy == 1)
        {
            PTWairdrop = true;
            PTWSpecialActionsCopy-=32;
        }
        divBy = PTWSpecialActionsCopy/(int)(Math.pow(2, 4));
        if (divBy == 1)
        {
            PTWbombard = true;
            PTWSpecialActionsCopy-=16;
        }
        divBy = PTWSpecialActionsCopy/(int)(Math.pow(2, 3));
        if (divBy == 1)
        {
            PTWpillage = true;
            PTWSpecialActionsCopy-=8;
        }
        divBy = PTWSpecialActionsCopy/(int)(Math.pow(2, 2));
        if (divBy == 1)
        {
            PTWairlift = true;
            PTWSpecialActionsCopy-=4;
        }
        divBy = PTWSpecialActionsCopy/(int)(Math.pow(2, 1));
        if (divBy == 1)
        {
            PTWunload = true;
            PTWSpecialActionsCopy-=2;
        }
        divBy = PTWSpecialActionsCopy/(int)(Math.pow(2, 0));
        if (divBy == 1)
        {
            PTWload = true;
            PTWSpecialActionsCopy-=1;
        }
    }

    private void extractPTWWorkerActions()
    {
        int PTWWorkerActionsCopy = PTWWorkerActions;
        int divBy = PTWWorkerActionsCopy/(int)(Math.pow(2, 16));
        if (divBy == 1)
        {
            PTWbuildBarricade = true;
            PTWWorkerActionsCopy-=65536;
        }
        divBy = PTWWorkerActionsCopy/(int)(Math.pow(2, 15));
        if (divBy == 1)
        {
            PTWbuildOutpost = true;
            PTWWorkerActionsCopy-=32768;
        }
        divBy = PTWWorkerActionsCopy/(int)(Math.pow(2, 14));
        if (divBy == 1)
        {
            PTWbuildRadarTower = true;
            PTWWorkerActionsCopy-=16384;
        }
        divBy = PTWWorkerActionsCopy/(int)(Math.pow(2, 13));
        if (divBy == 1)
        {
            PTWbuildAirfield = true;
            PTWWorkerActionsCopy-=8192;
        }
        divBy = PTWWorkerActionsCopy/(int)(Math.pow(2, 12));
        if (divBy == 1)
        {
            PTWjoinCity = true;
            PTWWorkerActionsCopy-=4096;
        }
        divBy = PTWWorkerActionsCopy/(int)(Math.pow(2, 11));
        if (divBy == 1)
        {
            PTWautomate = true;
            PTWWorkerActionsCopy-=2048;
        }
        divBy = PTWWorkerActionsCopy/(int)(Math.pow(2, 10));
        if (divBy == 1)
        {
            PTWclearPollution = true;
            PTWWorkerActionsCopy-=1024;
        }
        divBy = PTWWorkerActionsCopy/(int)(Math.pow(2, 9));
        if (divBy == 1)
        {
            PTWplantForest = true;
            PTWWorkerActionsCopy-=512;
        }
        divBy = PTWWorkerActionsCopy/(int)(Math.pow(2, 8));
        if (divBy == 1)
        {
            PTWclearJungle = true;
            PTWWorkerActionsCopy-=256;
        }
        divBy = PTWWorkerActionsCopy/(int)(Math.pow(2, 7));
        if (divBy == 1)
        {
            PTWclearForest = true;
            PTWWorkerActionsCopy-=128;
        }
        divBy = PTWWorkerActionsCopy/(int)(Math.pow(2, 6));
        if (divBy == 1)
        {
            PTWirrigate = true;
            PTWWorkerActionsCopy-=64;
        }
        divBy = PTWWorkerActionsCopy/(int)(Math.pow(2, 5));
        if (divBy == 1)
        {
            PTWbuildMine = true;
            PTWWorkerActionsCopy-=32;
        }
        divBy = PTWWorkerActionsCopy/(int)(Math.pow(2, 4));
        if (divBy == 1)
        {
            PTWbuildFort = true;
            PTWWorkerActionsCopy-=16;
        }
        divBy = PTWWorkerActionsCopy/(int)(Math.pow(2, 3));
        if (divBy == 1)
        {
            PTWbuildRailroad = true;
            PTWWorkerActionsCopy-=8;
        }
        divBy = PTWWorkerActionsCopy/(int)(Math.pow(2, 2));
        if (divBy == 1)
        {
            PTWbuildRoad = true;
            PTWWorkerActionsCopy-=4;
        }
        divBy = PTWWorkerActionsCopy/(int)(Math.pow(2, 1));
        if (divBy == 1)
        {
            PTWbuildCity = true;
            PTWWorkerActionsCopy-=2;
        }
        divBy = PTWWorkerActionsCopy/(int)(Math.pow(2, 0));
        if (divBy == 1)
        {
            PTWbuildColony = true;
            PTWWorkerActionsCopy-=1;
        }
    }

    private void extractPTWAirMissions()
    {
        int PTWairMissionCopy = PTWAirMissions;
        int divBy = PTWairMissionCopy/(int)(Math.pow(2, 4));
        if (divBy == 1)
        {
            PTWprecisionBomb = true;
            PTWairMissionCopy-=16;
        }
        divBy = PTWairMissionCopy/(int)(Math.pow(2, 3));
        if (divBy == 1)
        {
            PTWrebase = true;
            PTWairMissionCopy-=8;
        }
        divBy = PTWairMissionCopy/(int)(Math.pow(2, 2));
        if (divBy == 1)
        {
            PTWintercept = true;
            PTWairMissionCopy-=4;
        }
        divBy = PTWairMissionCopy/(int)(Math.pow(2, 1));
        if (divBy == 1)
        {
            PTWrecon = true;
            PTWairMissionCopy-=2;
        }
        divBy = PTWairMissionCopy/(int)(Math.pow(2, 0));
        if (divBy == 1)
        {
            PTWbomb = true;
            PTWairMissionCopy-=1;
        }
    }

    private void extractPTWActionMix()
    {
        int PTWActionMixCopy = PTWActionsMix;
        int divBy = PTWActionMixCopy/(int)(Math.pow(2, 15));
        if (divBy == 1)
        {
            goToMix = true;
            PTWActionMixCopy-=32768;
        }
        divBy = PTWActionMixCopy/(int)(Math.pow(2, 14));
        if (divBy == 1)
        {
            automate3 = true;
            PTWActionMixCopy-=16384;
        }
        divBy = PTWActionMixCopy/(int)(Math.pow(2, 13));
        if (divBy == 1)
        {
            precisionBombingMix = true;
            PTWActionMixCopy-=8192;
        }
        divBy = PTWActionMixCopy/(int)(Math.pow(2, 12));
        if (divBy == 1)
        {
            bombingMix = true;
            PTWActionMixCopy-=4096;
        }
        divBy = PTWActionMixCopy/(int)(Math.pow(2, 11));
        if (divBy == 1)
        {
            automate2Mix = true;
            PTWActionMixCopy-=2048;
        }
        divBy = PTWActionMixCopy/(int)(Math.pow(2, 10));
        if (divBy == 1)
        {
            automateMix = true;
            PTWActionMixCopy-=1024;
        }
        divBy = PTWActionMixCopy/(int)(Math.pow(2, 9));
        if (divBy == 1)
        {
            clearPollutionMix = true;
            PTWActionMixCopy-=512;
        }
        divBy = PTWActionMixCopy/(int)(Math.pow(2, 8));
        if (divBy == 1)
        {
            clearJungleMix = true;
            PTWActionMixCopy-=256;
        }
        divBy = PTWActionMixCopy/(int)(Math.pow(2, 7));
        if (divBy == 1)
        {
            clearForestMix = true;
            PTWActionMixCopy-=128;
        }
        divBy = PTWActionMixCopy/(int)(Math.pow(2, 6));
        if (divBy == 1)
        {
            irrigateMix = true;
            PTWActionMixCopy-=64;
        }
        divBy = PTWActionMixCopy/(int)(Math.pow(2, 5));
        if (divBy == 1)
        {
            road2Mix = true;
            PTWActionMixCopy-=32;
        }
        divBy = PTWActionMixCopy/(int)(Math.pow(2, 4));
        if (divBy == 1)
        {
            railroadMix = true;
            PTWActionMixCopy-=16;
        }
        divBy = PTWActionMixCopy/(int)(Math.pow(2, 3));
        if (divBy == 1)
        {
            roadMix = true;
            PTWActionMixCopy-=8;
        }
        divBy = PTWActionMixCopy/(int)(Math.pow(2, 2));
        if (divBy == 1)
        {
            colonyMix = true;
            PTWActionMixCopy-=4;
        }
        divBy = PTWActionMixCopy/(int)(Math.pow(2, 1));
        if (divBy == 1)
        {
            bombardMix = true;
            PTWActionMixCopy-=2;
        }
        divBy = PTWActionMixCopy/(int)(Math.pow(2, 0));
        if (divBy == 1)
        {
            sentryMix = true;
            PTWActionMixCopy-=1;
        }
    }

    private void extractUnitAbilities()
    {
        int unitAbilityCopy = unitAbilities;
        int divBy = unitAbilityCopy/(int)(Math.pow(2, 30));
        if (divBy == 1)
        {
            requiresEscort = true;
            unitAbilityCopy-=1073741824;
        }
        divBy = unitAbilityCopy/(int)(Math.pow(2, 29));
        if (divBy == 1)
        {
            king = true;
            unitAbilityCopy-=536870912;
        }
        divBy = unitAbilityCopy/(int)(Math.pow(2, 28));
        if (divBy == 1)
        {
            lethalSeaBombardment = true;
            unitAbilityCopy-=268435456;
        }
        divBy = unitAbilityCopy/(int)(Math.pow(2, 27));
        if (divBy == 1)
        {
            lethalLandBombardment = true;
            unitAbilityCopy-=134217728;
        }
        divBy = unitAbilityCopy/(int)(Math.pow(2, 26));
        if (divBy == 1)
        {
            rotateBeforeAttack = true;
            unitAbilityCopy-=67108864;
        }
        divBy = unitAbilityCopy/(int)(Math.pow(2, 25));
        if (divBy == 1)
        {
            rangedAttackAnimations = true;
            unitAbilityCopy-=33554432;
        }
        divBy = unitAbilityCopy/(int)(Math.pow(2, 24));
        if (divBy == 1)
        {
            transportsOnlyTacticalMissiles = true;
            unitAbilityCopy-=16777216;
        }
        divBy = unitAbilityCopy/(int)(Math.pow(2, 23));
        if (divBy == 1)
        {
            tacticalMissile = true;
            unitAbilityCopy-=8388608;
        }
        divBy = unitAbilityCopy/(int)(Math.pow(2, 22));
        if (divBy == 1)
        {
            detectInvisible = true;
            unitAbilityCopy-=4194304;
        }
        divBy = unitAbilityCopy/(int)(Math.pow(2, 21));
        if (divBy == 1)
        {
            stealth = true;
            unitAbilityCopy-=2097152;
        }
        divBy = unitAbilityCopy/(int)(Math.pow(2, 20));
        if (divBy == 1)
        {
            infiniteBombardRange = true;
            unitAbilityCopy-=1048576;
        }
        divBy = unitAbilityCopy/(int)(Math.pow(2, 19));
        if (divBy == 1)
        {
            leader = true;
            unitAbilityCopy-=524288;
        }
        divBy = unitAbilityCopy/(int)(Math.pow(2, 18));
        if (divBy == 1)
        {
            army = true;
            unitAbilityCopy-=262144;
        }
        divBy = unitAbilityCopy/(int)(Math.pow(2, 17));
        if (divBy == 1)
        {
            hiddenNationality = true;
            unitAbilityCopy-=131072;
        }
        divBy = unitAbilityCopy/(int)(Math.pow(2, 16));
        if (divBy == 1)
        {
            nuclearWeapon = true;
            unitAbilityCopy-=65536;
        }
        divBy = unitAbilityCopy/(int)(Math.pow(2, 15));
        if (divBy == 1)
        {
            startsGoldenAge = true;
            unitAbilityCopy-=32768;
        }
        divBy = unitAbilityCopy/(int)(Math.pow(2, 14));
        if (divBy == 1)
        {
            transportsOnlyFootUnits = true;
            unitAbilityCopy-=16384;
        }
        divBy = unitAbilityCopy/(int)(Math.pow(2, 13));
        if (divBy == 1)
        {
            flagUnit = true;
            unitAbilityCopy-=8192;
        }
        divBy = unitAbilityCopy/(int)(Math.pow(2, 12));
        if (divBy == 1)
        {
            sinksInOcean = true;
            unitAbilityCopy-=4096;
        }
        divBy = unitAbilityCopy/(int)(Math.pow(2, 11));
        if (divBy == 1)
        {
            sinksInSea = true;
            unitAbilityCopy-=2048;
        }
        divBy = unitAbilityCopy/(int)(Math.pow(2, 10));
        if (divBy == 1)
        {
            immobile = true;
            unitAbilityCopy-=1024;
        }
        divBy = unitAbilityCopy/(int)(Math.pow(2, 9));
        if (divBy == 1)
        {
            draftable = true;
            unitAbilityCopy-=512;
        }
        divBy = unitAbilityCopy/(int)(Math.pow(2, 8));
        if (divBy == 1)
        {
            transportsOnlyAirUnits = true;
            unitAbilityCopy-=256;
        }
        divBy = unitAbilityCopy/(int)(Math.pow(2, 7));
        if (divBy == 1)
        {
            invisible = true;
            unitAbilityCopy-=128;
        }
        divBy = unitAbilityCopy/(int)(Math.pow(2, 6));
        if (divBy == 1)
        {
            amphibiousUnit = true;
            unitAbilityCopy-=64;
        }
        divBy = unitAbilityCopy/(int)(Math.pow(2, 5));
        if (divBy == 1)
        {
            radar = true;
            unitAbilityCopy-=32;
        }
        divBy = unitAbilityCopy/(int)(Math.pow(2, 4));
        if (divBy == 1)
        {
            allTerrainAsRoads = true;
            unitAbilityCopy-=16;
        }
        divBy = unitAbilityCopy/(int)(Math.pow(2, 3));
        if (divBy == 1)
        {
            cruiseMissile = true;
            unitAbilityCopy-=8;
        }
        divBy = unitAbilityCopy/(int)(Math.pow(2, 2));
        if (divBy == 1)
        {
            blitz = true;
            unitAbilityCopy-=4;
        }
        divBy = unitAbilityCopy/(int)(Math.pow(2, 1));
        if (divBy == 1)
        {
            footSoldier = true;
            unitAbilityCopy-=2;
        }
        divBy = unitAbilityCopy/(int)(Math.pow(2, 0));
        if (divBy == 1)
        {
            wheeled = true;
            unitAbilityCopy-=1;
        }
    }

    public String toEnglish()
    {
            String lineReturn = java.lang.System.getProperty("line.separator");
            String toReturn = "name: " + name + lineReturn;
            toReturn = toReturn + "civilopediaEntry: " + civilopediaEntry + lineReturn;
            toReturn = toReturn + "index: " + index + lineReturn;
            toReturn = toReturn + "dataLength: " + dataLength + lineReturn;
            toReturn = toReturn + "zoneOfControl: " + zoneOfControl + lineReturn;
            toReturn = toReturn + "bombardStrength: " + bombardStrength + lineReturn;
            toReturn = toReturn + "bombardRange: " + bombardRange + lineReturn;
            toReturn = toReturn + "capacity: " + capacity + lineReturn;
            toReturn = toReturn + "shieldCost: " + shieldCost + lineReturn;
            toReturn = toReturn + "defence: " + defence + lineReturn;
            toReturn = toReturn + "iconIndex: " + iconIndex + lineReturn;
            toReturn = toReturn + "attack: " + attack + lineReturn;
            toReturn = toReturn + "operationalRange: " + operationalRange + lineReturn;
            toReturn = toReturn + "populationCost: " + populationCost + lineReturn;
            toReturn = toReturn + "rateOfFire: " + rateOfFire + lineReturn;
            toReturn = toReturn + "movement: " + movement + lineReturn;
            toReturn = toReturn + "requiredTech: " + requiredTechInt + lineReturn;
            toReturn = toReturn + "upgradeTo: " + upgradeToInt + lineReturn;
            toReturn = toReturn + "requiredResource1: " + requiredResource1Int + lineReturn;
            toReturn = toReturn + "requiredResource2: " + requiredResource2Int + lineReturn;
            toReturn = toReturn + "requiredResource3: " + requiredResource3Int + lineReturn;
            toReturn = toReturn + "unitAbilities: " + unitAbilities + lineReturn;
                toReturn = toReturn + "  wheeled: " + wheeled + lineReturn;
                toReturn = toReturn + "  footSoldier: " + footSoldier + lineReturn;
                toReturn = toReturn + "  blitz: " + blitz + lineReturn;
                toReturn = toReturn + "  cruiseMissile: " + cruiseMissile + lineReturn;
                toReturn = toReturn + "  allTerrainAsRoads: " + allTerrainAsRoads + lineReturn;
                toReturn = toReturn + "  radar: " + radar + lineReturn;
                toReturn = toReturn + "  amphibiousUnit: " + amphibiousUnit + lineReturn;
                toReturn = toReturn + "  invisible: " + invisible + lineReturn;
                toReturn = toReturn + "  transportsOnlyAirUnits: " + transportsOnlyAirUnits + lineReturn;
                toReturn = toReturn + "  draftable: " + draftable + lineReturn;
                toReturn = toReturn + "  immobile: " + immobile + lineReturn;
                toReturn = toReturn + "  sinksInSea: " + sinksInSea + lineReturn;
                toReturn = toReturn + "  sinksInOcean: " + sinksInOcean + lineReturn;
                toReturn = toReturn + "  flagUnit: " + flagUnit + lineReturn;
                toReturn = toReturn + "  transportsOnlyFootUnits: " + transportsOnlyFootUnits + lineReturn;
                toReturn = toReturn + "  startsGoldenAge: " + startsGoldenAge + lineReturn;
                toReturn = toReturn + "  nuclearWeapon: " + nuclearWeapon + lineReturn;
                toReturn = toReturn + "  hiddenNationality: " + hiddenNationality + lineReturn;
                toReturn = toReturn + "  army: " + army + lineReturn;
                toReturn = toReturn + "  leader: " + leader + lineReturn;
                toReturn = toReturn + "  infiniteBombardRange: " + infiniteBombardRange + lineReturn;
                toReturn = toReturn + "  stealth: " + stealth + lineReturn;
                toReturn = toReturn + "  detectInvisible: " + detectInvisible + lineReturn;
                toReturn = toReturn + "  tacticalMissile: " + tacticalMissile + lineReturn;
                toReturn = toReturn + "  transportsOnlyTacticalMissiles: " + transportsOnlyTacticalMissiles + lineReturn;
                toReturn = toReturn + "  rangedAttackAnimations: " + rangedAttackAnimations + lineReturn;
                toReturn = toReturn + "  rotateBeforeAttack: " + rotateBeforeAttack + lineReturn;
                toReturn = toReturn + "  lethalLandBombardment: " + lethalLandBombardment + lineReturn;
                toReturn = toReturn + "  lethalSeaBombardment: " + lethalSeaBombardment + lineReturn;
                toReturn = toReturn + "  king: " + king + lineReturn;
                toReturn = toReturn + "  requiresEscort: " + requiresEscort + lineReturn;
            toReturn = toReturn + "AIStrategy: " + AIStrategy + lineReturn;
                toReturn = toReturn + "  offence: " + offence + lineReturn;
                toReturn = toReturn + "  defenceStrategy: " + defenceStrategy + lineReturn;
                toReturn = toReturn + "  artillery: " + artillery + lineReturn;
                toReturn = toReturn + "  exploreStrategy: " + exploreStrategy + lineReturn;
                toReturn = toReturn + "  armyUnit: " + armyUnit + lineReturn;
                toReturn = toReturn + "  cruiseMissileUnit: " + cruiseMissileUnit + lineReturn;
                toReturn = toReturn + "  airBombard: " + airBombard + lineReturn;
                toReturn = toReturn + "  airDefenceStrategy: " + airDefenceStrategy + lineReturn;
                toReturn = toReturn + "  navalPower: " + navalPower + lineReturn;
                toReturn = toReturn + "  airTransport: " + airTransport + lineReturn;
                toReturn = toReturn + "  navalTransport: " + navalTransport + lineReturn;
                toReturn = toReturn + "  navalCarrier: " + navalCarrier + lineReturn;
                toReturn = toReturn + "  terraform: " + terraform + lineReturn;
                toReturn = toReturn + "  settle: " + settle + lineReturn;
                toReturn = toReturn + "  leaderUnit: " + leaderUnit + lineReturn;
                toReturn = toReturn + "  tacticalNuke: " + tacticalNuke + lineReturn;
                toReturn = toReturn + "  ICBM: " + ICBM + lineReturn;
                toReturn = toReturn + "  navalMissileTransport: " + navalMissileTransport + lineReturn;
            toReturn = toReturn + "availableTo: " + availableTo + lineReturn;
            toReturn = toReturn + "standardOrdersSpecialActions: " + standardOrdersSpecialActions + lineReturn;
                toReturn = toReturn + "  skipTurn: " + skipTurn + lineReturn;
                toReturn = toReturn + "  wait: " + wait + lineReturn;
                toReturn = toReturn + "  fortify: " + fortify + lineReturn;
                toReturn = toReturn + "  disband: " + disband + lineReturn;
                toReturn = toReturn + "  goTo: " + goTo + lineReturn;
                toReturn = toReturn + "  load: " + load + lineReturn;
                toReturn = toReturn + "  unload: " + unload + lineReturn;
                toReturn = toReturn + "  airlift: " + airlift + lineReturn;
                toReturn = toReturn + "  pillage: " + pillage + lineReturn;
                toReturn = toReturn + "  bombard: " + bombard + lineReturn;
                toReturn = toReturn + "  airdrop: " + airdrop + lineReturn;
                toReturn = toReturn + "  buildArmy: " + buildArmy + lineReturn;
                toReturn = toReturn + "  finishImprovement: " + finishImprovement + lineReturn;
                toReturn = toReturn + "  upgrade: " + upgrade + lineReturn;
                toReturn = toReturn + "  buildColony: " + buildColony + lineReturn;
                toReturn = toReturn + "  buildCity: " + buildCity + lineReturn;
                toReturn = toReturn + "  buildRoad: " + buildRoad + lineReturn;
                toReturn = toReturn + "  buildRailroad: " + buildRailroad + lineReturn;
                toReturn = toReturn + "  buildFort: " + buildFort + lineReturn;
                toReturn = toReturn + "  buildMine: " + buildMine + lineReturn;
                toReturn = toReturn + "  irrigate: " + irrigate + lineReturn;
                toReturn = toReturn + "  clearForest: " + clearForest + lineReturn;
                toReturn = toReturn + "  clearJungle: " + clearJungle + lineReturn;
                toReturn = toReturn + "  plantForest: " + plantForest + lineReturn;
                toReturn = toReturn + "  clearPollution: " + clearPollution + lineReturn;
                toReturn = toReturn + "  automate: " + automate + lineReturn;
                toReturn = toReturn + "  joinCity: " + joinCity + lineReturn;
            toReturn = toReturn + "airMissions: " + airMissions + lineReturn;
                toReturn = toReturn + "  bomb: " + bomb + lineReturn;
                toReturn = toReturn + "  recon: " + recon + lineReturn;
                toReturn = toReturn + "  intercept: " + intercept + lineReturn;
                toReturn = toReturn + "  rebase: " + rebase + lineReturn;
                toReturn = toReturn + "  precisionBombing: " + precisionBomb + lineReturn;
            toReturn = toReturn + "unitClass: " + unitClass + lineReturn;
            toReturn = toReturn + "otherStrategy: " + otherStrategy + lineReturn;
            toReturn = toReturn + "hitPointBonus: " + hitPointBonus + lineReturn;
            toReturn = toReturn + "PTWStandardOrders: " + PTWStandardOrders + lineReturn;
                toReturn = toReturn + "  PTWskipTurn: " + PTWskipTurn + lineReturn;
                toReturn = toReturn + "  PTWwait: " + PTWwait + lineReturn;
                toReturn = toReturn + "  PTWfortify: " + PTWfortify + lineReturn;
                toReturn = toReturn + "  PTWdisband: " + PTWdisband + lineReturn;
                toReturn = toReturn + "  PTWgoTo: " + PTWgoTo + lineReturn;
                toReturn = toReturn + "  PTWexplore: " + PTWexploreOrder + lineReturn;
                toReturn = toReturn + "  PTWsentry: " + PTWsentry + lineReturn;
            toReturn = toReturn + "PTWSpecialActions: " + PTWSpecialActions + lineReturn;
                toReturn = toReturn + "  PTWload: " + PTWload + lineReturn;
                toReturn = toReturn + "  PTWunload: " + PTWunload + lineReturn;
                toReturn = toReturn + "  PTWairlift: " + PTWairlift + lineReturn;
                toReturn = toReturn + "  PTWpillage: " + PTWpillage + lineReturn;
                toReturn = toReturn + "  PTWbombard: " + PTWbombard + lineReturn;
                toReturn = toReturn + "  PTWairdrop: " + PTWairdrop + lineReturn;
                toReturn = toReturn + "  PTWbuildArmy: " + PTWbuildArmy + lineReturn;
                toReturn = toReturn + "  PTWfinishImprovements: " + PTWfinishImprovements + lineReturn;
                toReturn = toReturn + "  PTWupgradeUnit: " + PTWupgradeUnit + lineReturn;
                toReturn = toReturn + "  PTWcapture: " + PTWcapture + lineReturn;
            toReturn = toReturn + "PTWWorkerActions: " + PTWWorkerActions + lineReturn;
                toReturn = toReturn + "  PTWbuildColony: " + PTWbuildColony + lineReturn;
                toReturn = toReturn + "  PTWbuildCity: " + PTWbuildCity + lineReturn;
                toReturn = toReturn + "  PTWbuildRoad: " + PTWbuildRoad + lineReturn;
                toReturn = toReturn + "  PTWbuildRailroad: " + PTWbuildRailroad + lineReturn;
                toReturn = toReturn + "  PTWbuildFort: " + PTWbuildFort + lineReturn;
                toReturn = toReturn + "  PTWbuildMine: " + PTWbuildMine + lineReturn;
                toReturn = toReturn + "  PTWirrigate: " + PTWirrigate + lineReturn;
                toReturn = toReturn + "  PTWclearForest: " + PTWclearForest + lineReturn;
                toReturn = toReturn + "  PTWclearJungle: " + PTWclearJungle + lineReturn;
                toReturn = toReturn + "  PTWplantForest: " + PTWplantForest + lineReturn;
                toReturn = toReturn + "  PTWclearPollution: " + PTWclearPollution + lineReturn;
                toReturn = toReturn + "  PTWautomate: " + PTWautomate + lineReturn;
                toReturn = toReturn + "  PTWjoinCity: " + PTWjoinCity + lineReturn;
                toReturn = toReturn + "  PTWbuildAirfield: " + PTWbuildAirfield + lineReturn;
                toReturn = toReturn + "  PTWbuildRadarTower: " + PTWbuildRadarTower + lineReturn;
                toReturn = toReturn + "  PTWbuildOutpost: " + PTWbuildOutpost + lineReturn;
            toReturn = toReturn + "PTWAirMissions: " + PTWAirMissions + lineReturn;
                toReturn = toReturn + "  PTWbomb: " + PTWbomb + lineReturn;
                toReturn = toReturn + "  PTWrecon: " + PTWrecon + lineReturn;
                toReturn = toReturn + "  PTWintercept: " + PTWintercept + lineReturn;
                toReturn = toReturn + "  PTWrebase: " + PTWrebase + lineReturn;
                toReturn = toReturn + "  PTWprecisionBomb: " + PTWprecisionBomb + lineReturn;
            toReturn = toReturn + "PTWActionsMix: " + PTWActionsMix + lineReturn;
                toReturn = toReturn + "  sentryMix: " + sentryMix + lineReturn;
                toReturn = toReturn + "  bombardMix: " + bombardMix + lineReturn;
                toReturn = toReturn + "  colonyMix: " + colonyMix + lineReturn;
                toReturn = toReturn + "  roadMix: " + roadMix + lineReturn;
                toReturn = toReturn + "  railroadMix: " + railroadMix + lineReturn;
                toReturn = toReturn + "  road2Mix: " + road2Mix + lineReturn;
                toReturn = toReturn + "  irrigateMix: " + irrigateMix + lineReturn;
                toReturn = toReturn + "  clearForestMix: " + clearForestMix + lineReturn;
                toReturn = toReturn + "  clearJungleMix: " + clearJungleMix + lineReturn;
                toReturn = toReturn + "  clearPollutionMix: " + clearPollutionMix + lineReturn;
                toReturn = toReturn + "  automateMix: " + automateMix + lineReturn;
                toReturn = toReturn + "  automate2Mix: " + automate2Mix + lineReturn;
                toReturn = toReturn + "  bombingMix: " + bombingMix + lineReturn;
                toReturn = toReturn + "  precisionBombingMix: " + precisionBombingMix + lineReturn;
                toReturn = toReturn + "  automate3: " + automate3 + lineReturn;
                toReturn = toReturn + "  goToMix: " + goToMix + lineReturn;
            toReturn = toReturn + "bombardEffects: " + bombardEffects + lineReturn;
            for (int j = 0; j < 14; j++)
            {
                toReturn = toReturn + "ignore movement cost of terrain " + j + "?  " + ignoreMovementCost[j] + lineReturn;
            }
            toReturn = toReturn + "requiresSupport: " + requiresSupport + lineReturn;
            toReturn = toReturn + "useExactCost: " + useExactCost + lineReturn;
            toReturn = toReturn + "telepadRange: " + telepadRange + lineReturn;
            toReturn = toReturn + "questionMark3: " + questionMark3 + lineReturn;
            toReturn = toReturn + "numLegalUnitTelepads: " + legalUnitTelepadsInt.size() + lineReturn;
            for (int j = 0; j < legalUnitTelepadsInt.size(); j++)
            {
                toReturn = toReturn + "  legal unit telepad: " +legalUnitTelepadsInt.get(j) + lineReturn;
            }
            toReturn = toReturn + "enslaveResultsIn: " + enslaveResultsInInt + lineReturn;
            toReturn = toReturn + "questionMark5: " + questionMark5 + lineReturn;
            toReturn = toReturn + "numStealthTargets: " + stealthTargetsInt.size() + lineReturn;
            for (int j = 0; j < stealthTargets.size(); j++)
            {
                toReturn = toReturn + "stealth target: " + stealthTargets.get(j).getName() + lineReturn;
            }
            toReturn = toReturn + "questionMark6: " + questionMark6 + lineReturn;
            toReturn = toReturn + "numLegalBuildingTelepads: " + legalBuildingTelepads.size() + lineReturn;
            for (int j = 0; j < legalBuildingTelepads.size(); j++)
            {
                toReturn = toReturn + "  legal building telepad: " +legalBuildingTelepads.get(j) + lineReturn;
            }
            toReturn = toReturn + "createsCraters: " + createsCraters + lineReturn;
            toReturn = toReturn + "workerStrength1: " + workerStrength1 + lineReturn;
            toReturn = toReturn + "workerStrengthFloat: " + workerStrengthFloat + lineReturn;
            toReturn = toReturn + "questionMark8: " + questionMark8 + lineReturn;
            toReturn = toReturn + "airDefence: " + airDefence + lineReturn;
        toReturn = toReturn + lineReturn;
        return toReturn;
    }

    public String compareTo(BIQSection section, String separator)
    {
        if (!(section instanceof PRTO))
            return null;
        PRTO two = (PRTO)section;
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "name: " + name + lineReturn;
        if (!(dataLength == two.dataLength))
        {
                toReturn = toReturn + "DataLength: " + dataLength + separator + two.dataLength + lineReturn;
        }
        if (!(zoneOfControl == two.zoneOfControl))
        {
                toReturn = toReturn + "ZoneOfControl: " + zoneOfControl + separator + two.zoneOfControl + lineReturn;
        }
        if (name.compareTo(two.name) != 0)
        {
                toReturn = toReturn + "Name: " + name + separator + two.name + lineReturn;
        }
        if (civilopediaEntry.compareTo(two.civilopediaEntry) != 0)
        {
                toReturn = toReturn + "CivilopediaEntry: " + civilopediaEntry + separator + two.civilopediaEntry + lineReturn;
        }
        if (!(bombardStrength == two.bombardStrength))
        {
                toReturn = toReturn + "BombardStrength: " + bombardStrength + separator + two.bombardStrength + lineReturn;
        }
        if (!(bombardRange == two.bombardRange))
        {
                toReturn = toReturn + "BombardRange: " + bombardRange + separator + two.bombardRange + lineReturn;
        }
        if (!(capacity == two.capacity))
        {
                toReturn = toReturn + "Capacity: " + capacity + separator + two.capacity + lineReturn;
        }
        if (!(shieldCost == two.shieldCost))
        {
                toReturn = toReturn + "ShieldCost: " + shieldCost + separator + two.shieldCost + lineReturn;
        }
        if (!(defence == two.defence))
        {
                toReturn = toReturn + "Defence: " + defence + separator + two.defence + lineReturn;
        }
        if (!(iconIndex == two.iconIndex))
        {
                toReturn = toReturn + "IconIndex: " + iconIndex + separator + two.iconIndex + lineReturn;
        }
        if (!(attack == two.attack))
        {
                toReturn = toReturn + "Attack: " + attack + separator + two.attack + lineReturn;
        }
        if (!(operationalRange == two.operationalRange))
        {
                toReturn = toReturn + "OperationalRange: " + operationalRange + separator + two.operationalRange + lineReturn;
        }
        if (!(populationCost == two.populationCost))
        {
                toReturn = toReturn + "PopulationCost: " + populationCost + separator + two.populationCost + lineReturn;
        }
        if (!(rateOfFire == two.rateOfFire))
        {
                toReturn = toReturn + "RateOfFire: " + rateOfFire + separator + two.rateOfFire + lineReturn;
        }
        if (!(movement == two.movement))
        {
                toReturn = toReturn + "Movement: " + movement + separator + two.movement + lineReturn;
        }
        if (!(requiredTechInt == two.requiredTechInt))
        {
                toReturn = toReturn + "RequiredTech: " + requiredTechInt + separator + two.requiredTechInt + lineReturn;
        }
        if (!(upgradeToInt == two.upgradeToInt))
        {
                toReturn = toReturn + "UpgradeTo: " + upgradeToInt + separator + two.upgradeToInt + lineReturn;
        }
        if (!(requiredResource1Int == two.requiredResource1Int))
        {
                toReturn = toReturn + "RequiredResource1: " + requiredResource1Int + separator + two.requiredResource1Int + lineReturn;
        }
        if (!(requiredResource2Int == two.requiredResource2Int))
        {
                toReturn = toReturn + "RequiredResource2: " + requiredResource2Int + separator + two.requiredResource2Int + lineReturn;
        }
        if (!(requiredResource3Int == two.requiredResource3Int))
        {
                toReturn = toReturn + "RequiredResource3: " + requiredResource3Int + separator + two.requiredResource3Int + lineReturn;
        }
        if (!(unitAbilities == two.unitAbilities))
        {
                toReturn = toReturn + "UnitAbilities: " + unitAbilities + separator + two.unitAbilities + lineReturn;
        }
        if (!(wheeled == two.wheeled))
        {
                toReturn = toReturn + "Wheeled: " + wheeled + separator + two.wheeled + lineReturn;
        }
        if (!(footSoldier == two.footSoldier))
        {
                toReturn = toReturn + "FootSoldier: " + footSoldier + separator + two.footSoldier + lineReturn;
        }
        if (!(blitz == two.blitz))
        {
                toReturn = toReturn + "Blitz: " + blitz + separator + two.blitz + lineReturn;
        }
        if (!(cruiseMissile == two.cruiseMissile))
        {
                toReturn = toReturn + "CruiseMissile: " + cruiseMissile + separator + two.cruiseMissile + lineReturn;
        }
        if (!(allTerrainAsRoads == two.allTerrainAsRoads))
        {
                toReturn = toReturn + "AllTerrainAsRoads: " + allTerrainAsRoads + separator + two.allTerrainAsRoads + lineReturn;
        }
        if (!(radar == two.radar))
        {
                toReturn = toReturn + "Radar: " + radar + separator + two.radar + lineReturn;
        }
        if (!(amphibiousUnit == two.amphibiousUnit))
        {
                toReturn = toReturn + "AmphibiousUnit: " + amphibiousUnit + separator + two.amphibiousUnit + lineReturn;
        }
        if (!(invisible == two.invisible))
        {
                toReturn = toReturn + "Invisible: " + invisible + separator + two.invisible + lineReturn;
        }
        if (!(transportsOnlyAirUnits == two.transportsOnlyAirUnits))
        {
                toReturn = toReturn + "TransportsOnlyAirUnits: " + transportsOnlyAirUnits + separator + two.transportsOnlyAirUnits + lineReturn;
        }
        if (!(draftable == two.draftable))
        {
                toReturn = toReturn + "Draftable: " + draftable + separator + two.draftable + lineReturn;
        }
        if (!(immobile == two.immobile))
        {
                toReturn = toReturn + "Immobile: " + immobile + separator + two.immobile + lineReturn;
        }
        if (!(sinksInSea == two.sinksInSea))
        {
                toReturn = toReturn + "SinksInSea: " + sinksInSea + separator + two.sinksInSea + lineReturn;
        }
        if (!(sinksInOcean == two.sinksInOcean))
        {
                toReturn = toReturn + "SinksInOcean: " + sinksInOcean + separator + two.sinksInOcean + lineReturn;
        }
        if (!(flagUnit == two.flagUnit))
        {
                toReturn = toReturn + "FlagUnit: " + flagUnit + separator + two.flagUnit + lineReturn;
        }
        if (!(transportsOnlyFootUnits == two.transportsOnlyFootUnits))
        {
                toReturn = toReturn + "TransportsOnlyFootUnits: " + transportsOnlyFootUnits + separator + two.transportsOnlyFootUnits + lineReturn;
        }
        if (!(startsGoldenAge == two.startsGoldenAge))
        {
                toReturn = toReturn + "StartsGoldenAge: " + startsGoldenAge + separator + two.startsGoldenAge + lineReturn;
        }
        if (!(nuclearWeapon == two.nuclearWeapon))
        {
                toReturn = toReturn + "NuclearWeapon: " + nuclearWeapon + separator + two.nuclearWeapon + lineReturn;
        }
        if (!(hiddenNationality == two.hiddenNationality))
        {
                toReturn = toReturn + "HiddenNationality: " + hiddenNationality + separator + two.hiddenNationality + lineReturn;
        }
        if (!(army == two.army))
        {
                toReturn = toReturn + "Army: " + army + separator + two.army + lineReturn;
        }
        if (!(leader == two.leader))
        {
                toReturn = toReturn + "Leader: " + leader + separator + two.leader + lineReturn;
        }
        if (!(infiniteBombardRange == two.infiniteBombardRange))
        {
                toReturn = toReturn + "InfiniteBombardRange: " + infiniteBombardRange + separator + two.infiniteBombardRange + lineReturn;
        }
        if (!(stealth == two.stealth))
        {
                toReturn = toReturn + "Stealth: " + stealth + separator + two.stealth + lineReturn;
        }
        if (!(detectInvisible == two.detectInvisible))
        {
                toReturn = toReturn + "DetectInvisible: " + detectInvisible + separator + two.detectInvisible + lineReturn;
        }
        if (!(tacticalMissile == two.tacticalMissile))
        {
                toReturn = toReturn + "TacticalMissile: " + tacticalMissile + separator + two.tacticalMissile + lineReturn;
        }
        if (!(transportsOnlyTacticalMissiles == two.transportsOnlyTacticalMissiles))
        {
                toReturn = toReturn + "TransportsOnlyTacticalMissiles: " + transportsOnlyTacticalMissiles + separator + two.transportsOnlyTacticalMissiles + lineReturn;
        }
        if (!(rangedAttackAnimations == two.rangedAttackAnimations))
        {
                toReturn = toReturn + "RangedAttackAnimations: " + rangedAttackAnimations + separator + two.rangedAttackAnimations + lineReturn;
        }
        if (!(rotateBeforeAttack == two.rotateBeforeAttack))
        {
                toReturn = toReturn + "RotateBeforeAttack: " + rotateBeforeAttack + separator + two.rotateBeforeAttack + lineReturn;
        }
        if (!(lethalLandBombardment == two.lethalLandBombardment))
        {
                toReturn = toReturn + "LethalLandBombardment: " + lethalLandBombardment + separator + two.lethalLandBombardment + lineReturn;
        }
        if (!(lethalSeaBombardment == two.lethalSeaBombardment))
        {
                toReturn = toReturn + "LethalSeaBombardment: " + lethalSeaBombardment + separator + two.lethalSeaBombardment + lineReturn;
        }
        if (!(king == two.king))
        {
                toReturn = toReturn + "King: " + king + separator + two.king + lineReturn;
        }
        if (!(requiresEscort == two.requiresEscort))
        {
                toReturn = toReturn + "RequiresEscort: " + requiresEscort + separator + two.requiresEscort + lineReturn;
        }
        if (!(AIStrategy == two.AIStrategy))
        {
                toReturn = toReturn + "AIStrategy: " + AIStrategy + separator + two.AIStrategy + lineReturn;
        }
        if (!(offence == two.offence))
        {
                toReturn = toReturn + "Offence: " + offence + separator + two.offence + lineReturn;
        }
        if (!(defenceStrategy == two.defenceStrategy))
        {
                toReturn = toReturn + "DefenceStrategy: " + defenceStrategy + separator + two.defenceStrategy + lineReturn;
        }
        if (!(artillery == two.artillery))
        {
                toReturn = toReturn + "Artillery: " + artillery + separator + two.artillery + lineReturn;
        }
        if (!(exploreStrategy == two.exploreStrategy))
        {
                toReturn = toReturn + "ExploreStrategy: " + exploreStrategy + separator + two.exploreStrategy + lineReturn;
        }
        if (!(armyUnit == two.armyUnit))
        {
                toReturn = toReturn + "ArmyUnit: " + armyUnit + separator + two.armyUnit + lineReturn;
        }
        if (!(cruiseMissileUnit == two.cruiseMissileUnit))
        {
                toReturn = toReturn + "CruiseMissileUnit: " + cruiseMissileUnit + separator + two.cruiseMissileUnit + lineReturn;
        }
        if (!(airBombard == two.airBombard))
        {
                toReturn = toReturn + "AirBombard: " + airBombard + separator + two.airBombard + lineReturn;
        }
        if (!(airDefenceStrategy == two.airDefenceStrategy))
        {
                toReturn = toReturn + "AirDefenceStrategy: " + airDefenceStrategy + separator + two.airDefenceStrategy + lineReturn;
        }
        if (!(navalPower == two.navalPower))
        {
                toReturn = toReturn + "NavalPower: " + navalPower + separator + two.navalPower + lineReturn;
        }
        if (!(airTransport == two.airTransport))
        {
                toReturn = toReturn + "AirTransport: " + airTransport + separator + two.airTransport + lineReturn;
        }
        if (!(navalTransport == two.navalTransport))
        {
                toReturn = toReturn + "NavalTransport: " + navalTransport + separator + two.navalTransport + lineReturn;
        }
        if (!(navalCarrier == two.navalCarrier))
        {
                toReturn = toReturn + "NavalCarrier: " + navalCarrier + separator + two.navalCarrier + lineReturn;
        }
        if (!(terraform == two.terraform))
        {
                toReturn = toReturn + "Terraform: " + terraform + separator + two.terraform + lineReturn;
        }
        if (!(settle == two.settle))
        {
                toReturn = toReturn + "Settle: " + settle + separator + two.settle + lineReturn;
        }
        if (!(leaderUnit == two.leaderUnit))
        {
                toReturn = toReturn + "LeaderUnit: " + leaderUnit + separator + two.leaderUnit + lineReturn;
        }
        if (!(tacticalNuke == two.tacticalNuke))
        {
                toReturn = toReturn + "TacticalNuke: " + tacticalNuke + separator + two.tacticalNuke + lineReturn;
        }
        if (!(ICBM == two.ICBM))
        {
                toReturn = toReturn + "ICBM: " + ICBM + separator + two.ICBM + lineReturn;
        }
        if (!(navalMissileTransport == two.navalMissileTransport))
        {
                toReturn = toReturn + "NavalMissileTransport: " + navalMissileTransport + separator + two.navalMissileTransport + lineReturn;
        }
        if (!(availableTo == two.availableTo))
        {
                toReturn = toReturn + "AvailableTo: " + availableTo + separator + two.availableTo + lineReturn;
        }
        if (!(standardOrdersSpecialActions == two.standardOrdersSpecialActions))
        {
                toReturn = toReturn + "StandardOrdersSpecialActions: " + standardOrdersSpecialActions + separator + two.standardOrdersSpecialActions + lineReturn;
        }
        if (!(skipTurn == two.skipTurn))
        {
                toReturn = toReturn + "SkipTurn: " + skipTurn + separator + two.skipTurn + lineReturn;
        }
        if (!(wait == two.wait))
        {
                toReturn = toReturn + "Wait: " + wait + separator + two.wait + lineReturn;
        }
        if (!(fortify == two.fortify))
        {
                toReturn = toReturn + "Fortify: " + fortify + separator + two.fortify + lineReturn;
        }
        if (!(disband == two.disband))
        {
                toReturn = toReturn + "Disband: " + disband + separator + two.disband + lineReturn;
        }
        if (!(goTo == two.goTo))
        {
                toReturn = toReturn + "GoTo: " + goTo + separator + two.goTo + lineReturn;
        }
        if (!(load == two.load))
        {
                toReturn = toReturn + "Load: " + load + separator + two.load + lineReturn;
        }
        if (!(unload == two.unload))
        {
                toReturn = toReturn + "Unload: " + unload + separator + two.unload + lineReturn;
        }
        if (!(airlift == two.airlift))
        {
                toReturn = toReturn + "Airlift: " + airlift + separator + two.airlift + lineReturn;
        }
        if (!(pillage == two.pillage))
        {
                toReturn = toReturn + "Pillage: " + pillage + separator + two.pillage + lineReturn;
        }
        if (!(bombard == two.bombard))
        {
                toReturn = toReturn + "Bombard: " + bombard + separator + two.bombard + lineReturn;
        }
        if (!(airdrop == two.airdrop))
        {
                toReturn = toReturn + "Airdrop: " + airdrop + separator + two.airdrop + lineReturn;
        }
        if (!(buildArmy == two.buildArmy))
        {
                toReturn = toReturn + "BuildArmy: " + buildArmy + separator + two.buildArmy + lineReturn;
        }
        if (!(finishImprovement == two.finishImprovement))
        {
                toReturn = toReturn + "FinishImprovement: " + finishImprovement + separator + two.finishImprovement + lineReturn;
        }
        if (!(upgrade == two.upgrade))
        {
                toReturn = toReturn + "Upgrade: " + upgrade + separator + two.upgrade + lineReturn;
        }
        if (!(buildColony == two.buildColony))
        {
                toReturn = toReturn + "BuildColony: " + buildColony + separator + two.buildColony + lineReturn;
        }
        if (!(buildCity == two.buildCity))
        {
                toReturn = toReturn + "BuildCity: " + buildCity + separator + two.buildCity + lineReturn;
        }
        if (!(buildRoad == two.buildRoad))
        {
                toReturn = toReturn + "BuildRoad: " + buildRoad + separator + two.buildRoad + lineReturn;
        }
        if (!(buildRailroad == two.buildRailroad))
        {
                toReturn = toReturn + "BuildRailroad: " + buildRailroad + separator + two.buildRailroad + lineReturn;
        }
        if (!(buildFort == two.buildFort))
        {
                toReturn = toReturn + "BuildFort: " + buildFort + separator + two.buildFort + lineReturn;
        }
        if (!(buildMine == two.buildMine))
        {
                toReturn = toReturn + "BuildMine: " + buildMine + separator + two.buildMine + lineReturn;
        }
        if (!(irrigate == two.irrigate))
        {
                toReturn = toReturn + "Irrigate: " + irrigate + separator + two.irrigate + lineReturn;
        }
        if (!(clearForest == two.clearForest))
        {
                toReturn = toReturn + "ClearForest: " + clearForest + separator + two.clearForest + lineReturn;
        }
        if (!(clearJungle == two.clearJungle))
        {
                toReturn = toReturn + "ClearJungle: " + clearJungle + separator + two.clearJungle + lineReturn;
        }
        if (!(plantForest == two.plantForest))
        {
                toReturn = toReturn + "PlantForest: " + plantForest + separator + two.plantForest + lineReturn;
        }
        if (!(clearPollution == two.clearPollution))
        {
                toReturn = toReturn + "ClearPollution: " + clearPollution + separator + two.clearPollution + lineReturn;
        }
        if (!(automate == two.automate))
        {
                toReturn = toReturn + "Automate: " + automate + separator + two.automate + lineReturn;
        }
        if (!(joinCity == two.joinCity))
        {
                toReturn = toReturn + "JoinCity: " + joinCity + separator + two.joinCity + lineReturn;
        }
        if (!(airMissions == two.airMissions))
        {
                toReturn = toReturn + "AirMissions: " + airMissions + separator + two.airMissions + lineReturn;
        }
        if (!(bomb == two.bomb))
        {
                toReturn = toReturn + "Bomb: " + bomb + separator + two.bomb + lineReturn;
        }
        if (!(recon == two.recon))
        {
                toReturn = toReturn + "Recon: " + recon + separator + two.recon + lineReturn;
        }
        if (!(intercept == two.intercept))
        {
                toReturn = toReturn + "Intercept: " + intercept + separator + two.intercept + lineReturn;
        }
        if (!(rebase == two.rebase))
        {
                toReturn = toReturn + "Rebase: " + rebase + separator + two.rebase + lineReturn;
        }
        if (!(precisionBomb == two.precisionBomb))
        {
                toReturn = toReturn + "PrecisionBomb: " + precisionBomb + separator + two.precisionBomb + lineReturn;
        }
        if (!(unitClass == two.unitClass))
        {
                toReturn = toReturn + "UnitClass: " + unitClass + separator + two.unitClass + lineReturn;
        }
        if (!(otherStrategy == two.otherStrategy))
        {
                toReturn = toReturn + "OtherStrategy: " + otherStrategy + separator + two.otherStrategy + lineReturn;
        }
        if (!(hitPointBonus == two.hitPointBonus))
        {
                toReturn = toReturn + "HitPointBonus: " + hitPointBonus + separator + two.hitPointBonus + lineReturn;
        }
        if (!(PTWStandardOrders == two.PTWStandardOrders))
        {
                toReturn = toReturn + "PTWStandardOrders: " + PTWStandardOrders + separator + two.PTWStandardOrders + lineReturn;
        }
        if (!(PTWskipTurn == two.PTWskipTurn))
        {
                toReturn = toReturn + "PTWskipTurn: " + PTWskipTurn + separator + two.PTWskipTurn + lineReturn;
        }
        if (!(PTWwait == two.PTWwait))
        {
                toReturn = toReturn + "PTWwait: " + PTWwait + separator + two.PTWwait + lineReturn;
        }
        if (!(PTWfortify == two.PTWfortify))
        {
                toReturn = toReturn + "PTWfortify: " + PTWfortify + separator + two.PTWfortify + lineReturn;
        }
        if (!(PTWdisband == two.PTWdisband))
        {
                toReturn = toReturn + "PTWdisband: " + PTWdisband + separator + two.PTWdisband + lineReturn;
        }
        if (!(PTWgoTo == two.PTWgoTo))
        {
                toReturn = toReturn + "PTWgoTo: " + PTWgoTo + separator + two.PTWgoTo + lineReturn;
        }
        if (!(PTWexploreOrder == two.PTWexploreOrder))
        {
                toReturn = toReturn + "PTWexplore: " + PTWexploreOrder + separator + two.PTWexploreOrder + lineReturn;
        }
        if (!(PTWsentry == two.PTWsentry))
        {
                toReturn = toReturn + "PTWsentry: " + PTWsentry + separator + two.PTWsentry + lineReturn;
        }
        if (!(PTWSpecialActions == two.PTWSpecialActions))
        {
                toReturn = toReturn + "PTWSpecialActions: " + PTWSpecialActions + separator + two.PTWSpecialActions + lineReturn;
        }
        if (!(PTWload == two.PTWload))
        {
                toReturn = toReturn + "PTWload: " + PTWload + separator + two.PTWload + lineReturn;
        }
        if (!(PTWunload == two.PTWunload))
        {
                toReturn = toReturn + "PTWunload: " + PTWunload + separator + two.PTWunload + lineReturn;
        }
        if (!(PTWairlift == two.PTWairlift))
        {
                toReturn = toReturn + "PTWairlift: " + PTWairlift + separator + two.PTWairlift + lineReturn;
        }
        if (!(PTWpillage == two.PTWpillage))
        {
                toReturn = toReturn + "PTWpillage: " + PTWpillage + separator + two.PTWpillage + lineReturn;
        }
        if (!(PTWbombard == two.PTWbombard))
        {
                toReturn = toReturn + "PTWbombard: " + PTWbombard + separator + two.PTWbombard + lineReturn;
        }
        if (!(PTWairdrop == two.PTWairdrop))
        {
                toReturn = toReturn + "PTWairdrop: " + PTWairdrop + separator + two.PTWairdrop + lineReturn;
        }
        if (!(PTWbuildArmy == two.PTWbuildArmy))
        {
                toReturn = toReturn + "PTWbuildArmy: " + PTWbuildArmy + separator + two.PTWbuildArmy + lineReturn;
        }
        if (!(PTWfinishImprovements == two.PTWfinishImprovements))
        {
                toReturn = toReturn + "PTWfinishImprovements: " + PTWfinishImprovements + separator + two.PTWfinishImprovements + lineReturn;
        }
        if (!(PTWupgradeUnit == two.PTWupgradeUnit))
        {
                toReturn = toReturn + "PTWupgradeUnit: " + PTWupgradeUnit + separator + two.PTWupgradeUnit + lineReturn;
        }
        if (!(PTWcapture == two.PTWcapture))
        {
                toReturn = toReturn + "PTWcapture: " + PTWcapture + separator + two.PTWcapture + lineReturn;
        }
        if (!(PTWWorkerActions == two.PTWWorkerActions))
        {
                toReturn = toReturn + "PTWWorkerActions: " + PTWWorkerActions + separator + two.PTWWorkerActions + lineReturn;
        }
        if (!(PTWbuildColony == two.PTWbuildColony))
        {
                toReturn = toReturn + "PTWbuildColony: " + PTWbuildColony + separator + two.PTWbuildColony + lineReturn;
        }
        if (!(PTWbuildCity == two.PTWbuildCity))
        {
                toReturn = toReturn + "PTWbuildCity: " + PTWbuildCity + separator + two.PTWbuildCity + lineReturn;
        }
        if (!(PTWbuildRoad == two.PTWbuildRoad))
        {
                toReturn = toReturn + "PTWbuildRoad: " + PTWbuildRoad + separator + two.PTWbuildRoad + lineReturn;
        }
        if (!(PTWbuildRailroad == two.PTWbuildRailroad))
        {
                toReturn = toReturn + "PTWbuildRailroad: " + PTWbuildRailroad + separator + two.PTWbuildRailroad + lineReturn;
        }
        if (!(PTWbuildFort == two.PTWbuildFort))
        {
                toReturn = toReturn + "PTWbuildFort: " + PTWbuildFort + separator + two.PTWbuildFort + lineReturn;
        }
        if (!(PTWbuildMine == two.PTWbuildMine))
        {
                toReturn = toReturn + "PTWbuildMine: " + PTWbuildMine + separator + two.PTWbuildMine + lineReturn;
        }
        if (!(PTWirrigate == two.PTWirrigate))
        {
                toReturn = toReturn + "PTWirrigate: " + PTWirrigate + separator + two.PTWirrigate + lineReturn;
        }
        if (!(PTWclearForest == two.PTWclearForest))
        {
                toReturn = toReturn + "PTWclearForest: " + PTWclearForest + separator + two.PTWclearForest + lineReturn;
        }
        if (!(PTWclearJungle == two.PTWclearJungle))
        {
                toReturn = toReturn + "PTWclearJungle: " + PTWclearJungle + separator + two.PTWclearJungle + lineReturn;
        }
        if (!(PTWplantForest == two.PTWplantForest))
        {
                toReturn = toReturn + "PTWplantForest: " + PTWplantForest + separator + two.PTWplantForest + lineReturn;
        }
        if (!(PTWclearPollution == two.PTWclearPollution))
        {
                toReturn = toReturn + "PTWclearPollution: " + PTWclearPollution + separator + two.PTWclearPollution + lineReturn;
        }
        if (!(PTWautomate == two.PTWautomate))
        {
                toReturn = toReturn + "PTWautomate: " + PTWautomate + separator + two.PTWautomate + lineReturn;
        }
        if (!(PTWjoinCity == two.PTWjoinCity))
        {
                toReturn = toReturn + "PTWjoinCity: " + PTWjoinCity + separator + two.PTWjoinCity + lineReturn;
        }
        if (!(PTWbuildAirfield == two.PTWbuildAirfield))
        {
                toReturn = toReturn + "PTWbuildAirfield: " + PTWbuildAirfield + separator + two.PTWbuildAirfield + lineReturn;
        }
        if (!(PTWbuildRadarTower == two.PTWbuildRadarTower))
        {
                toReturn = toReturn + "PTWbuildRadarTower: " + PTWbuildRadarTower + separator + two.PTWbuildRadarTower + lineReturn;
        }
        if (!(PTWbuildOutpost == two.PTWbuildOutpost))
        {
                toReturn = toReturn + "PTWbuildOutpost: " + PTWbuildOutpost + separator + two.PTWbuildOutpost + lineReturn;
        }
        if (!(PTWAirMissions == two.PTWAirMissions))
        {
                toReturn = toReturn + "PTWAirMissions: " + PTWAirMissions + separator + two.PTWAirMissions + lineReturn;
        }
        if (!(PTWbomb == two.PTWbomb))
        {
                toReturn = toReturn + "PTWbomb: " + PTWbomb + separator + two.PTWbomb + lineReturn;
        }
        if (!(PTWrecon == two.PTWrecon))
        {
                toReturn = toReturn + "PTWrecon: " + PTWrecon + separator + two.PTWrecon + lineReturn;
        }
        if (!(PTWintercept == two.PTWintercept))
        {
                toReturn = toReturn + "PTWintercept: " + PTWintercept + separator + two.PTWintercept + lineReturn;
        }
        if (!(PTWrebase == two.PTWrebase))
        {
                toReturn = toReturn + "PTWrebase: " + PTWrebase + separator + two.PTWrebase + lineReturn;
        }
        if (!(PTWprecisionBomb == two.PTWprecisionBomb))
        {
                toReturn = toReturn + "PTWprecisionBomb: " + PTWprecisionBomb + separator + two.PTWprecisionBomb + lineReturn;
        }
        if (!(PTWActionsMix == two.PTWActionsMix))
        {
                toReturn = toReturn + "PTWActionsMix: " + PTWActionsMix + separator + two.PTWActionsMix + lineReturn;
        }
        if (!(sentryMix == two.sentryMix))
        {
                toReturn = toReturn + "SentryMix: " + sentryMix + separator + two.sentryMix + lineReturn;
        }
        if (!(bombardMix == two.bombardMix))
        {
                toReturn = toReturn + "BombardMix: " + bombardMix + separator + two.bombardMix + lineReturn;
        }
        if (!(colonyMix == two.colonyMix))
        {
                toReturn = toReturn + "ColonyMix: " + colonyMix + separator + two.colonyMix + lineReturn;
        }
        if (!(roadMix == two.roadMix))
        {
                toReturn = toReturn + "RoadMix: " + roadMix + separator + two.roadMix + lineReturn;
        }
        if (!(railroadMix == two.railroadMix))
        {
                toReturn = toReturn + "RailroadMix: " + railroadMix + separator + two.railroadMix + lineReturn;
        }
        if (!(road2Mix == two.road2Mix))
        {
                toReturn = toReturn + "Road2Mix: " + road2Mix + separator + two.road2Mix + lineReturn;
        }
        if (!(irrigateMix == two.irrigateMix))
        {
                toReturn = toReturn + "IrrigateMix: " + irrigateMix + separator + two.irrigateMix + lineReturn;
        }
        if (!(clearForestMix == two.clearForestMix))
        {
                toReturn = toReturn + "ClearForestMix: " + clearForestMix + separator + two.clearForestMix + lineReturn;
        }
        if (!(clearJungleMix == two.clearJungleMix))
        {
                toReturn = toReturn + "ClearJungleMix: " + clearJungleMix + separator + two.clearJungleMix + lineReturn;
        }
        if (!(clearPollutionMix == two.clearPollutionMix))
        {
                toReturn = toReturn + "ClearPollutionMix: " + clearPollutionMix + separator + two.clearPollutionMix + lineReturn;
        }
        if (!(automateMix == two.automateMix))
        {
                toReturn = toReturn + "AutomateMix: " + automateMix + separator + two.automateMix + lineReturn;
        }
        if (!(automate2Mix == two.automate2Mix))
        {
                toReturn = toReturn + "Automate2Mix: " + automate2Mix + separator + two.automate2Mix + lineReturn;
        }
        if (!(bombingMix == two.bombingMix))
        {
                toReturn = toReturn + "BombingMix: " + bombingMix + separator + two.bombingMix + lineReturn;
        }
        if (!(precisionBombingMix == two.precisionBombingMix))
        {
                toReturn = toReturn + "PrecisionBombingMix: " + precisionBombingMix + separator + two.precisionBombingMix + lineReturn;
        }
        if (!(automate3 == two.automate3))
        {
                toReturn = toReturn + "Automate3: " + automate3 + separator + two.automate3 + lineReturn;
        }
        if (!(goToMix == two.goToMix))
        {
                toReturn = toReturn + "GoToMix: " + goToMix + separator + two.goToMix + lineReturn;
        }
        if (!(bombardEffects == two.bombardEffects))
        {
                toReturn = toReturn + "BombardEffects: " + bombardEffects + separator + two.bombardEffects + lineReturn;
        }
        if (!(requiresSupport == two.requiresSupport))
        {
                toReturn = toReturn + "RequiresSupport: " + requiresSupport + separator + two.requiresSupport + lineReturn;
        }
        if (!(useExactCost == two.useExactCost))
        {
                toReturn = toReturn + "UseExactCost: " + useExactCost + separator + two.useExactCost + lineReturn;
        }
        if (!(telepadRange == two.telepadRange))
        {
                toReturn = toReturn + "TelepadRange: " + telepadRange + separator + two.telepadRange + lineReturn;
        }
        if (!(questionMark3 == two.questionMark3))
        {
                toReturn = toReturn + "QuestionMark3: " + questionMark3 + separator + two.questionMark3 + lineReturn;
        }
        if (!(legalUnitTelepadsInt.size() == two.getNumLegalUnitTelepads()))
        {
                toReturn = toReturn + "NumLegalUnitTelepads: " + getNumLegalUnitTelepads() + separator + two.getNumLegalUnitTelepads() + lineReturn;
        }
        if (!(enslaveResultsInInt == two.enslaveResultsInInt))
        {
                toReturn = toReturn + "EnslaveResultsIn: " + enslaveResultsInInt + separator + two.enslaveResultsInInt + lineReturn;
        }
        if (!(questionMark5 == two.questionMark5))
        {
                toReturn = toReturn + "QuestionMark5: " + questionMark5 + separator + two.questionMark5 + lineReturn;
        }
        if (!(stealthTargetsInt.size() == two.stealthTargetsInt.size()))
        {
                toReturn = toReturn + "NumStealthTargets: " + stealthTargetsInt.size() + separator + two.stealthTargetsInt.size() + lineReturn;
        }
        if (!(questionMark6 == two.questionMark6))
        {
                toReturn = toReturn + "QuestionMark6: " + questionMark6 + separator + two.questionMark6 + lineReturn;
        }
        if (!(getNumLegalBuildingTelepads() == two.getNumLegalBuildingTelepads()))
        {
                toReturn = toReturn + "NumLegalBuildingTelepads: " + getNumLegalBuildingTelepads() + separator + two.getNumLegalBuildingTelepads() + lineReturn;
        }
        if (!(createsCraters == two.createsCraters))
        {
                toReturn = toReturn + "CreatesCraters: " + createsCraters + separator + two.createsCraters + lineReturn;
        }
        if (!(workerStrength1 == two.workerStrength1))
        {
                toReturn = toReturn + "WorkerStrength1: " + workerStrength1 + separator + two.workerStrength1 + lineReturn;
        }
        if (!(questionMark8 == two.questionMark8))
        {
                toReturn = toReturn + "QuestionMark8: " + questionMark8 + separator + two.questionMark8 + lineReturn;
        }
        if (!(airDefence == two.airDefence))
        {
                toReturn = toReturn + "AirDefence: " + airDefence + separator + two.airDefence + lineReturn;
        }
        if (toReturn.equals("name: " + name + lineReturn))
        {
            toReturn = "";
        }
        return toReturn;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }

    //Recalculate unit abilities from its constituent values
    public void recalculateUnitAbilities()
    {
        Boolean[]list = {requiresEscort, king, lethalSeaBombardment, lethalLandBombardment, rotateBeforeAttack, rangedAttackAnimations, transportsOnlyTacticalMissiles, tacticalMissile, detectInvisible, stealth, infiniteBombardRange, leader, army, hiddenNationality, nuclearWeapon, startsGoldenAge, transportsOnlyFootUnits, flagUnit, sinksInOcean, sinksInSea, immobile, draftable, transportsOnlyAirUnits, invisible, amphibiousUnit, radar, allTerrainAsRoads, cruiseMissile, blitz, footSoldier, wheeled};
        unitAbilities = Utils.recalculateBitfield(list);
    }
    /**
     * Recalculates the Integer variable that stores the AIStrategies in binary
     * in a BIQ file from the boolean values that store it during the program's
     * execution.
     * <p>
     * With best practices, this method would only need to be called prior to
     * saving a file, as the Integer AIStrategies field shouldn't have to be
     * accessed during editing.  It also should only <i>have</i> to be called if
     * the AI strategies for a unit has changed.
     *
     */
    public void recalculateAIStrategies()
    {
        Boolean[]list = {kingStrategy, flagStrategy, navalMissileTransport, ICBM, tacticalNuke, leaderUnit, settle, terraform, navalCarrier, navalTransport, airTransport, navalPower, airDefenceStrategy, airBombard, cruiseMissileUnit, armyUnit, exploreStrategy, artillery, defenceStrategy, offence};
        AIStrategy = Utils.recalculateBitfield(list);
        this.numStrategies = (byte)Utils.calcNumTrue(list);
        if (logger.isTraceEnabled())
            logger.trace("The " + name + " has " + numStrategies + " strategies.");
        //there's got to be a better way to get this to cascade
        Boolean[]strategies2 = {offence, defenceStrategy, artillery, exploreStrategy, armyUnit, cruiseMissileUnit, airBombard, airDefenceStrategy, navalPower, airTransport, navalTransport, navalCarrier, terraform, settle, leaderUnit, tacticalNuke, ICBM, navalMissileTransport, flagStrategy, kingStrategy};
        strategies = strategies2;
        
    }
    public void recalculateAvailableTo()
    {
        if (logger.isDebugEnabled())
            logger.debug("initial available to for " + this.name + " " + availableTo);
        availableTo = 0;
        long toAdd = 1;
        for (int i = 0; i < 32; i++)
        {
            if (availableToArray[i])
            {
                availableTo+=toAdd;
                //System.out.println(availableTo);
            }
            toAdd*=2;
        }
        if (logger.isDebugEnabled())
            logger.debug("final available to for " + this.name + " " + availableTo);
    }

    /**
     * Recalculates the PTW Worker Actions
     * These are the worker actions that are actually used, not those in
     * Standard Orders/Special Actions
     */
    public void recalculatePTWWorkerActions()
    {
        Boolean[]list = {PTWbuildBarricade, PTWbuildOutpost, PTWbuildRadarTower, PTWbuildAirfield, PTWjoinCity, PTWautomate, PTWclearPollution, PTWplantForest, PTWclearJungle, PTWclearForest, PTWirrigate, PTWbuildMine, PTWbuildFort, PTWbuildRailroad, PTWbuildRoad, PTWbuildCity, PTWbuildColony};
        PTWWorkerActions = Utils.recalculateBitfield(list);
    }

    public void recalculatePTWSpecialActions()
    {
        Boolean[]list = {scienceAge, sacrifice, collateralDamage, enslave, charm, stealthAttack, teleportable, telepad, false, false, false, false, PTWcapture, PTWupgradeUnit, PTWfinishImprovements, PTWbuildArmy, PTWairdrop, PTWbombard, PTWpillage, PTWairlift, PTWunload, PTWload};
        PTWSpecialActions = Utils.recalculateBitfield(list);
    }

    public void recalculatePTWAirMissions()
    {
        Boolean[]list = {PTWprecisionBomb, PTWrebase, PTWintercept, PTWrecon, PTWbomb};
        PTWAirMissions = Utils.recalculateBitfield(list);
    }
    
    public void recalculatePTWActionsMix() {
        Boolean[] list = {  goToMix, automate3, precisionBombingMix, bombingMix, automate2Mix, automateMix, clearPollutionMix, clearJungleMix,
                            clearForestMix, irrigateMix, road2Mix, railroadMix, roadMix, colonyMix, bombardMix, sentryMix};
        PTWActionsMix = Utils.recalculateBitfield(list);
    }

    public void recalculatePTWStandardOrders()
    {
        Boolean[]bits = {PTWsentry, PTWexploreOrder, PTWgoTo, PTWdisband, PTWfortify, PTWwait, PTWskipTurn};
        PTWStandardOrders = Utils.recalculateBitfield(bits);
    }
    
    public boolean hasInfiniteUpgradePath() {
        if (upgradeTo == null) {
            return false;
        }
        //Must stored encountered PRTOs, since if we have A -> B -> C -> D -> B, we will have an infinite loop,
        //but will not run into the initial unit twice.
        List<PRTO>encounteredPRTOs = new ArrayList<PRTO>();
        encounteredPRTOs.add(this);
        //What better way to check for an infinite upgrade path than with an infinite loop?
        PRTO unitBeingChecked = this;
        while (true) {
            unitBeingChecked = unitBeingChecked.upgradeTo;
            if (unitBeingChecked.upgradeTo == null) {
                return false;
            }
            else if (encounteredPRTOs.contains(unitBeingChecked)) {
                return true;
            }
            encounteredPRTOs.add(unitBeingChecked);
        }
    }
    
    /**
     * Returns a string representation of the unit's upgrade path.
     * For example, Horseman --> Knight --> Cavalry.
     * This can be useful in debugging upgrade path loops or simply more quickly 
     * stating the upgrade path that viewing each unit individually.
     * @return The upgrade path, or None if the unit does not upgrade.
     */
    public String getUpgradePath() {
        if (upgradeTo == null) {
            return "None";
        }
        List<PRTO>encounteredPRTOs = new ArrayList<PRTO>();
        encounteredPRTOs.add(this);
        //What better way to check for an infinite upgrade path than with an infinite loop?
        PRTO unitBeingChecked = this;
        String upgradePath = this.name + " --> ";
        int unitsCounted = 0;
        while (true) {
            unitBeingChecked = unitBeingChecked.upgradeTo;
            upgradePath = upgradePath + unitBeingChecked.name;
            if (unitBeingChecked.upgradeTo == null) {
                return upgradePath;
            }
            else if (encounteredPRTOs.contains(unitBeingChecked)) {
                return upgradePath;
            }
            upgradePath = upgradePath + " --> ";
            encounteredPRTOs.add(unitBeingChecked);
            unitsCounted++;
            if (unitsCounted % 5 == 0) {
                upgradePath = upgradePath + "<br/>";
            }
        }
    }

    public Object getProperty(String string) throws UnsupportedOperationException
    {
        if (string.equals("Name"))
            return this.name;
        throw new UnsupportedOperationException();
    }
}
