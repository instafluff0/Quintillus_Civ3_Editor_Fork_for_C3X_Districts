package com.civfanatics.civ3.biqFile;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Other sections that know about TERR: PRTO, MAP
 * @author Quintillus
 */
import java.util.*;
import org.apache.log4j.*;
public class TERR extends BIQSection{
    Logger logger = Logger.getLogger(this.getClass());
    public int dataLength;
    public int numPossibleResources;
    public ArrayList<Byte>possibleResources;
    //the below is a representation that will make possibleResource much easier to modify
    //the values in resourceAllowed will be converted back into possibleResources when all is said and done
    public ArrayList<Boolean>allowedResources;
    public String name = "";
    public String civilopediaEntry = "";
    public int foodBonus;
    public int shieldsBonus;
    public int commerceBonus;
    public int defenceBonus;
    public int movementCost;
    public int food;
    public int shields;
    public int commerce;
    public int workerJob;
    public int pollutionEffect;
    byte allowCities;
    byte allowColonies;
    byte impassable;
    byte impassableByWheeled;
    byte allowAirfields;
    byte allowForts;
    byte allowOutposts;
    byte allowRadarTowers;
    public int questionMark;
    byte landmarkEnabled;
    public int landmarkFood;
    public int landmarkShields;
    public int landmarkCommerce;
    public int landmarkFoodBonus;
    public int landmarkShieldsBonus;
    public int landmarkCommerceBonus;
    public int landmarkMovementCost;
    public int landmarkDefenceBonus;
    public String landmarkName = "";
    public String landmarkCivilopediaEntry = "";
    public int questionMark2;
    public int terrainFlags;
    public boolean causesDisease;
    public boolean diseaseCanBeCured;
    public int diseaseStrength;
    IO baselink;

    public static final byte DESERT = 0;
    public static final byte PLAINS = 1;
    public static final byte GRASSLAND = 2;
    public static final byte TUNDRA = 3;
    public static final byte FLOODPLAIN = 4;
    public static final byte HILLS = 5;
    public static final byte MOUNTAIN = 6;
    public static final byte FOREST = 7;
    public static final byte JUNGLE = 8;
    public static final byte MARSH = 9;
    public static final byte VOLCANO = 10;
    public static final byte COAST = 11;
    public static final byte SEA = 12;
    public static final byte OCEAN = 13;

    public TERR(IO baselink)
    {
        super(baselink);
        this.baselink = baselink;
        possibleResources = new ArrayList<Byte>();
        allowedResources = new ArrayList<Boolean>();
    }
	public void trim()
	{
		name = name.trim();
		civilopediaEntry = civilopediaEntry.trim();
        if (baselink.version == civ3Version.CONQUESTS)
        {
            //Although in English Civ3, the lenght is always 32, Chinese uses two bytes per character.
            //So, if there are two Chinese characters, the length is 32 - 2 = 30, because those two
            //characters use 4 bytes total (2 beyond what English would), and you still have 28 nulls.
            int lmLength = landmarkName.length();
            if (landmarkName.charAt(lmLength - 1) == 204)
                landmarkName = landmarkName.substring(0, 31);
            //The civilopedia entry is probably still in Roman characters, but we'll .length it to be safe
            int civilopediaLength = landmarkCivilopediaEntry.length();
            if (landmarkCivilopediaEntry.charAt(civilopediaLength - 1) == 204)
                landmarkCivilopediaEntry = landmarkCivilopediaEntry.substring(0, 31);
            landmarkName = landmarkName.trim();
            landmarkCivilopediaEntry = landmarkCivilopediaEntry.trim();
        }
	}

    public void extractEnglish()
    {
        //% will work properly as modulus as terrainFlags cannot have negative value
        //Why bits 26 and 27 from right are where the values are stored, I haven't an idea
        //The values of other bits does not appear to be important
        int terrainCopy = terrainFlags >>> 26;  //get the 27th bit from right on the right
        if (terrainCopy % 2 == 0)
        {
            causesDisease = true;
        }
        else causesDisease = false;
        terrainCopy = terrainFlags >>> 27;
        if (terrainCopy % 2 == 0)
        {
            diseaseCanBeCured = true;
        }
        else diseaseCanBeCured = false;
    }

    public int getDataLength()
    {
        return dataLength;
    }

    public int getNumPossibleResources()
    {
        return numPossibleResources;
    }

    public String getName()
    {
        return name;
    }

    public String getCivilopediaEntry()
    {
        return civilopediaEntry;
    }

    public int getFoodBonus()
    {
        return foodBonus;
    }

    public int getShieldsBonus()
    {
        return shieldsBonus;
    }

    public int getCommerceBonus()
    {
        return commerceBonus;
    }

    public int getDefenceBonus()
    {
        return defenceBonus;
    }

    public int getMovementCost()
    {
        return movementCost;
    }

    public int getFood()
    {
        return food;
    }

    public int getShields()
    {
        return shields;
    }

    public int getCommerce()
    {
        return commerce;
    }

    public int getWorkerJob()
    {
        return workerJob;
    }

    public int getPollutionEffect()
    {
        return pollutionEffect;
    }

    public byte getAllowCities()
    {
        return allowCities;
    }
    
    public boolean allowsColonies() {
        return allowColonies == 1;
    }

    public byte getAllowColonies()
    {
        return allowColonies;
    }

    public byte getImpassable()
    {
        return impassable;
    }

    public byte getImpassableByWheeled()
    {
        return impassableByWheeled;
    }
    
    public boolean allowsAirfields() {
        return allowAirfields == 1;
    }

    public byte getAllowAirfields()
    {
        return allowAirfields;
    }

    public byte getAllowForts()
    {
        return allowForts;
    }
    
    public boolean allowsOutposts() {
        return allowOutposts == 1;
    }

    public byte getAllowOutposts()
    {
        return allowOutposts;
    }
    
    public boolean allowsRadarTowers() {
        return allowRadarTowers == 1;
    }

    public byte getAllowRadarTowers()
    {
        return allowRadarTowers;
    }

    public int getQuestionMark()
    {
        return questionMark;
    }

    public byte getLandmarkEnabled()
    {
        return landmarkEnabled;
    }

    public int getLandmarkFood()
    {
        return landmarkFood;
    }

    public int getLandmarkShields()
    {
        return landmarkShields;
    }

    public int getLandmarkCommerce()
    {
        return landmarkCommerce;
    }

    public int getLandmarkFoodBonus()
    {
        return landmarkFoodBonus;
    }

    public int getLandmarkShieldsBonus()
    {
        return landmarkShieldsBonus;
    }

    public int getLandmarkCommerceBonus()
    {
        return landmarkCommerceBonus;
    }

    public int getLandmarkMovementCost()
    {
        return landmarkMovementCost;
    }

    public int getLandmarkDefenceBonus()
    {
        return landmarkDefenceBonus;
    }

    public String getLandmarkName()
    {
        return landmarkName;
    }

    public String getLandmarkCivilopediaEntry()
    {
        return landmarkCivilopediaEntry;
    }

    public int getQuestionMark2()
    {
        return questionMark2;
    }

    public int getTerrainFlags()
    {
        return terrainFlags;
    }

    public int getDiseaseStrength()
    {
        return diseaseStrength;
    }






    public void setDataLength(int dataLength)
    {
        this.dataLength = dataLength;
    }

    public void setNumTotalResources(int numPossibleResources)
    {
        this.numPossibleResources = numPossibleResources;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setCivilopediaEntry(String civilopediaEntry)
    {
        this.civilopediaEntry = civilopediaEntry;
    }

    public void setFoodBonus(int foodBonus)
    {
        this.foodBonus = foodBonus;
    }

    public void setShieldsBonus(int shieldsBonus)
    {
        this.shieldsBonus = shieldsBonus;
    }

    public void setCommerceBonus(int commerceBonus)
    {
        this.commerceBonus = commerceBonus;
    }

    public void setDefenceBonus(int defenceBonus)
    {
        this.defenceBonus = defenceBonus;
    }

    public void setMovementCost(int movementCost)
    {
        this.movementCost = movementCost;
    }

    public void setFood(int food)
    {
        this.food = food;
    }

    public void setShields(int shields)
    {
        this.shields = shields;
    }

    public void setCommerce(int commerce)
    {
        this.commerce = commerce;
    }

    public void setWorkerJob(int workerJob)
    {
        this.workerJob = workerJob;
    }

    public void setPollutionEffect(int pollutionEffect)
    {
        this.pollutionEffect = pollutionEffect;
    }

    public void setAllowCities(byte allowCities)
    {
        this.allowCities = allowCities;
    }

    public void setAllowColonies(byte allowColonies)
    {
        this.allowColonies = allowColonies;
    }

    public void setImpassable(byte impassable)
    {
        this.impassable = impassable;
    }

    public void setImpassableByWheeled(byte impassableByWheeled)
    {
        this.impassableByWheeled = impassableByWheeled;
    }

    public void setAllowAirfields(byte allowAirfields)
    {
        this.allowAirfields = allowAirfields;
    }

    public void setAllowForts(byte allowForts)
    {
        this.allowForts = allowForts;
    }

    public void setAllowOutposts(byte allowOutposts)
    {
        this.allowOutposts = allowOutposts;
    }

    public void setAllowRadarTowers(byte allowRadarTowers)
    {
        this.allowRadarTowers = allowRadarTowers;
    }

    public void setQuestionMark(int questionMark)
    {
        this.questionMark = questionMark;
    }

    public void setLandmarkEnabled(byte landmarkEnabled)
    {
        this.landmarkEnabled = landmarkEnabled;
    }

    public void setLandmarkFood(int landmarkFood)
    {
        this.landmarkFood = landmarkFood;
    }

    public void setLandmarkShields(int landmarkShields)
    {
        this.landmarkShields = landmarkShields;
    }

    public void setLandmarkCommerce(int landmarkCommerce)
    {
        this.landmarkCommerce = landmarkCommerce;
    }

    public void setLandmarkFoodBonus(int landmarkFoodBonus)
    {
        this.landmarkFoodBonus = landmarkFoodBonus;
    }

    public void setLandmarkShieldsBonus(int landmarkShieldsBonus)
    {
        this.landmarkShieldsBonus = landmarkShieldsBonus;
    }

    public void setLandmarkCommerceBonus(int landmarkCommerceBonus)
    {
        this.landmarkCommerceBonus = landmarkCommerceBonus;
    }

    public void setLandmarkMovementCost(int landmarkMovementCost)
    {
        this.landmarkMovementCost = landmarkMovementCost;
    }

    public void setLandmarkDefenceBonus(int landmarkDefenceBonus)
    {
        this.landmarkDefenceBonus = landmarkDefenceBonus;
    }

    public void setLandmarkName(String landmarkName)
    {
        this.landmarkName = landmarkName;
    }

    public void setLandmarkCivilopediaEntry(String landmarkCivilopediaEntry)
    {
        this.landmarkCivilopediaEntry = landmarkCivilopediaEntry;
    }

    public void setQuestionMark2(int questionMark2)
    {
        this.questionMark2 = questionMark2;
    }

    public void setTerrainFlags(int terrainFlags)
    {
        this.terrainFlags = terrainFlags;
    }

    public void setDiseaseStrength(int diseaseStrength)
    {
        this.diseaseStrength = diseaseStrength;
    }
    public void simplifyGoods()
    {
        this.allowedResources.clear();
        int maxVal = this.numPossibleResources;
        short curByte = -1;
        byte tempVal = 0;
        for (int i = 0; i < maxVal; i++)
        {
            if (i%8 == 0)
            {
                if (i != 0)
                    possibleResources.set(curByte, tempVal);
                curByte++;
                tempVal = possibleResources.get(curByte);
                //System.out.println("tempVal: " + tempVal);
            }
            if (Math.abs(tempVal % 2) == 1)
            {
                allowedResources.add(true);
                //System.out.println("adding true for terrain " + this.name);
            }
            else
            {
                allowedResources.add(false);
                //System.out.println("adding false for terrain " + this.name);
            }
            tempVal = (byte)(tempVal >>> 1);

        }
        possibleResources.set(curByte, tempVal);
    }

    //Convert from boolean array to signed (sigh) byte array
    public void storeGoods()
    {
        this.possibleResources.clear();
        if (logger.isDebugEnabled())
            logger.debug("Size of allowed resources: " + allowedResources.size());
        //We want to subtract one from the size because we want element 9, not
        //element 8, to trigger the use of a second byte
        for (int i = 0; i < (allowedResources.size() - 1)/8 + 1; i++)
        {
            if (logger.isTraceEnabled())
                logger.trace("Byte: " + i);
            byte temp = 0;
            possibleResources.add(temp);
            int bitIndex = 0;
            byte toSet = 0;
            byte toAdd = 1;
            if (allowedResources.get(i*8+bitIndex))
            {
                toSet = (byte)(possibleResources.get(i) + toAdd);
                possibleResources.set(i,toSet);
            }
            toAdd*=2;
            bitIndex+=1;
            //numPossibleResources is in base 1 (for 1 resource); bitIndex starts at 0
            if (numPossibleResources < i*8 + 1 + bitIndex)
                break;
            if (allowedResources.get(i*8+bitIndex))
            {
                toSet = (byte)(possibleResources.get(i) + toAdd);
                possibleResources.set(i,toSet);
            }
            toAdd*=2;
            bitIndex+=1;
            //numPossibleResources is in base 1 (for 1 resource); bitIndex starts at 0
            if (numPossibleResources < i*8 + 1 + bitIndex)
                break;
            if (allowedResources.get(i*8+bitIndex))
            {
                toSet = (byte)(possibleResources.get(i) + toAdd);
                possibleResources.set(i,toSet);
            }
            toAdd*=2;
            bitIndex+=1;
            //numPossibleResources is in base 1 (for 1 resource); bitIndex starts at 0
            if (numPossibleResources < i*8 + 1 + bitIndex)
                break;
            if (allowedResources.get(i*8+bitIndex))
            {
                toSet = (byte)(possibleResources.get(i) + toAdd);
                possibleResources.set(i,toSet);
            }
            toAdd*=2;
            bitIndex+=1;
            //numPossibleResources is in base 1 (for 1 resource); bitIndex starts at 0
            if (numPossibleResources < i*8 + 1 + bitIndex)
                break;
            if (allowedResources.get(i*8+bitIndex))
            {
                toSet = (byte)(possibleResources.get(i) + toAdd);
                possibleResources.set(i,toSet);
            }
            toAdd*=2;
            bitIndex+=1;
            //numPossibleResources is in base 1 (for 1 resource); bitIndex starts at 0
            if (numPossibleResources < i*8 + 1 + bitIndex)
                break;
            if (allowedResources.get(i*8+bitIndex))
            {
                toSet = (byte)(possibleResources.get(i) + toAdd);
                possibleResources.set(i,toSet);
            }
            toAdd*=2;
            bitIndex+=1;
            //numPossibleResources is in base 1 (for 1 resource); bitIndex starts at 0
            if (numPossibleResources < i*8 + 1 + bitIndex)
                break;
            if (allowedResources.get(i*8+bitIndex))
            {
                toSet = (byte)(possibleResources.get(i) + toAdd);
                possibleResources.set(i,toSet);
            }
            toAdd*=2;
            bitIndex+=1;
            //numPossibleResources is in base 1 (for 1 resource); bitIndex starts at 0
            if (numPossibleResources < i*8 + 1 + bitIndex)
                break;
            //special case: 2^8:  subtract 128 instead of adding 128, due to unsignedness
            if (allowedResources.get(i*8+bitIndex))
            {
                toSet = (byte)(possibleResources.get(i) -128);
                possibleResources.set(i,toSet);
            }
            //end this byte
        }
    }

    public String toEnglish(){
        return toString();
    }

    @Override
    public String toString()
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn =  "name: " + name + lineReturn;
        toReturn = toReturn + "civilopediaEntry: " + civilopediaEntry + lineReturn;
        toReturn = toReturn + "dataLength: " + dataLength + lineReturn;
        toReturn = toReturn + "numPossibleResources: " + numPossibleResources + lineReturn;
        for (int j = 0; j < (numPossibleResources + 7)/8; j++)
        {
            toReturn = toReturn + "  " + possibleResources.get(j) + lineReturn;
        }
        toReturn = toReturn + lineReturn;
        toReturn = toReturn + "foodBonus: " + foodBonus + lineReturn;
        toReturn = toReturn + "shieldsBonus: " + shieldsBonus + lineReturn;
        toReturn = toReturn + "commerceBonus: " + commerceBonus + lineReturn;
        toReturn = toReturn + "defenceBonus: " + defenceBonus + lineReturn;
        toReturn = toReturn + "movementCost: " + movementCost + lineReturn;
        toReturn = toReturn + "food: " + food + lineReturn;
        toReturn = toReturn + "shields: " + shields + lineReturn;
        toReturn = toReturn + "commerce: " + commerce + lineReturn;
        toReturn = toReturn + "workerJob: " + workerJob + lineReturn;
        toReturn = toReturn + "pollutionEffect: " + pollutionEffect + lineReturn;
        toReturn = toReturn + "allowCities: " + allowCities + lineReturn;
        toReturn = toReturn + "allowColonies: " + allowColonies + lineReturn;
        toReturn = toReturn + "impassable: " + impassable + lineReturn;
        toReturn = toReturn + "impassableByWheeled: " + impassableByWheeled + lineReturn;
        toReturn = toReturn + "allowAirfields: " + allowAirfields + lineReturn;
        toReturn = toReturn + "allowForts: " + allowForts + lineReturn;
        toReturn = toReturn + "allowOutposts: " + allowOutposts + lineReturn;
        toReturn = toReturn + "allowRadarTowers: " + allowRadarTowers + lineReturn;
        toReturn = toReturn + "questionMark: " + questionMark + lineReturn;
        toReturn = toReturn + "landmarkEnabled: " + landmarkEnabled + lineReturn;
        toReturn = toReturn + "landmarkFood: " + landmarkFood + lineReturn;
        toReturn = toReturn + "landmarkShields: " + landmarkShields + lineReturn;
        toReturn = toReturn + "landmarkCommerce: " + landmarkCommerce + lineReturn;
        toReturn = toReturn + "landmarkFoodBonus: " + landmarkFoodBonus + lineReturn;
        toReturn = toReturn + "landmarkShieldsBonus: " + landmarkShieldsBonus + lineReturn;
        toReturn = toReturn + "landmarkCommerceBonus: " + landmarkCommerceBonus + lineReturn;
        toReturn = toReturn + "landmarkMovementCost: " + landmarkMovementCost + lineReturn;
        toReturn = toReturn + "landmarkDefenceBonus: " + landmarkDefenceBonus + lineReturn;
        toReturn = toReturn + "landmarkName: " + landmarkName + lineReturn;
        toReturn = toReturn + "landmarkCivilopediaEntry: " + landmarkCivilopediaEntry + lineReturn;
        toReturn = toReturn + "questionMark2: " + questionMark2 + lineReturn;
        toReturn = toReturn + "terrainFlags: " + terrainFlags + lineReturn;
        toReturn = toReturn + "diseaseStrength: " + diseaseStrength + lineReturn;
        toReturn = toReturn + lineReturn;
        return toReturn;
    }

    public String compareTo(BIQSection section, String separator)
    {
        if (!(section instanceof TERR))
            return null;
        TERR two = (TERR)section;
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "name: " + name + lineReturn;
        if (!(dataLength == two.getDataLength()))
        {
                toReturn = toReturn + "DataLength: " + dataLength + separator + two.getDataLength() + lineReturn;
        }
        if (!(numPossibleResources == two.getNumPossibleResources()))
        {
                toReturn = toReturn + "NumPossibleResources: " + numPossibleResources + separator + two.getNumPossibleResources() + lineReturn;
        }
        //possible resources
        if (civilopediaEntry.compareTo(two.getCivilopediaEntry()) != 0)
        {
                toReturn = toReturn + "CivilopediaEntry: " + civilopediaEntry + separator + two.getCivilopediaEntry() + lineReturn;
        }
        if (!(foodBonus == two.getFoodBonus()))
        {
                toReturn = toReturn + "FoodBonus: " + foodBonus + separator + two.getFoodBonus() + lineReturn;
        }
        if (!(shieldsBonus == two.getShieldsBonus()))
        {
                toReturn = toReturn + "ShieldsBonus: " + shieldsBonus + separator + two.getShieldsBonus() + lineReturn;
        }
        if (!(commerceBonus == two.getCommerceBonus()))
        {
                toReturn = toReturn + "CommerceBonus: " + commerceBonus + separator + two.getCommerceBonus() + lineReturn;
        }
        if (!(defenceBonus == two.getDefenceBonus()))
        {
                toReturn = toReturn + "DefenceBonus: " + defenceBonus + separator + two.getDefenceBonus() + lineReturn;
        }
        if (!(movementCost == two.getMovementCost()))
        {
                toReturn = toReturn + "MovementCost: " + movementCost + separator + two.getMovementCost() + lineReturn;
        }
        if (!(food == two.getFood()))
        {
                toReturn = toReturn + "Food: " + food + separator + two.getFood() + lineReturn;
        }
        if (!(shields == two.getShields()))
        {
                toReturn = toReturn + "Shields: " + shields + separator + two.getShields() + lineReturn;
        }
        if (!(commerce == two.getCommerce()))
        {
                toReturn = toReturn + "Commerce: " + commerce + separator + two.getCommerce() + lineReturn;
        }
        if (!(workerJob == two.getWorkerJob()))
        {
                toReturn = toReturn + "WorkerJob: " + workerJob + separator + two.getWorkerJob() + lineReturn;
        }
        if (!(pollutionEffect == two.getPollutionEffect()))
        {
                toReturn = toReturn + "PollutionEffect: " + pollutionEffect + separator + two.getPollutionEffect() + lineReturn;
        }
        if (!(allowCities == two.getAllowCities()))
        {
                toReturn = toReturn + "AllowCities: " + allowCities + separator + two.getAllowCities() + lineReturn;
        }
        if (!(allowColonies == two.getAllowColonies()))
        {
                toReturn = toReturn + "AllowColonies: " + allowColonies + separator + two.getAllowColonies() + lineReturn;
        }
        if (!(impassable == two.getImpassable()))
        {
                toReturn = toReturn + "Impassable: " + impassable + separator + two.getImpassable() + lineReturn;
        }
        if (!(impassableByWheeled == two.getImpassableByWheeled()))
        {
                toReturn = toReturn + "ImpassableByWheeled: " + impassableByWheeled + separator + two.getImpassableByWheeled() + lineReturn;
        }
        if (!(allowAirfields == two.getAllowAirfields()))
        {
                toReturn = toReturn + "AllowAirfields: " + allowAirfields + separator + two.getAllowAirfields() + lineReturn;
        }
        if (!(allowForts == two.getAllowForts()))
        {
                toReturn = toReturn + "AllowForts: " + allowForts + separator + two.getAllowForts() + lineReturn;
        }
        if (!(allowOutposts == two.getAllowOutposts()))
        {
                toReturn = toReturn + "AllowOutposts: " + allowOutposts + separator + two.getAllowOutposts() + lineReturn;
        }
        if (!(allowRadarTowers == two.getAllowRadarTowers()))
        {
                toReturn = toReturn + "AllowRadarTowers: " + allowRadarTowers + separator + two.getAllowRadarTowers() + lineReturn;
        }
        if (!(questionMark == two.getQuestionMark()))
        {
                toReturn = toReturn + "QuestionMark: " + questionMark + separator + two.getQuestionMark() + lineReturn;
        }
        if (!(landmarkEnabled == two.getLandmarkEnabled()))
        {
                toReturn = toReturn + "LandmarkEnabled: " + landmarkEnabled + separator + two.getLandmarkEnabled() + lineReturn;
        }
        if (!(landmarkFood == two.getLandmarkFood()))
        {
                toReturn = toReturn + "LandmarkFood: " + landmarkFood + separator + two.getLandmarkFood() + lineReturn;
        }
        if (!(landmarkShields == two.getLandmarkShields()))
        {
                toReturn = toReturn + "LandmarkShields: " + landmarkShields + separator + two.getLandmarkShields() + lineReturn;
        }
        if (!(landmarkCommerce == two.getLandmarkCommerce()))
        {
                toReturn = toReturn + "LandmarkCommerce: " + landmarkCommerce + separator + two.getLandmarkCommerce() + lineReturn;
        }
        if (!(landmarkFoodBonus == two.getLandmarkFoodBonus()))
        {
                toReturn = toReturn + "LandmarkFoodBonus: " + landmarkFoodBonus + separator + two.getLandmarkFoodBonus() + lineReturn;
        }
        if (!(landmarkShieldsBonus == two.getLandmarkShieldsBonus()))
        {
                toReturn = toReturn + "LandmarkShieldsBonus: " + landmarkShieldsBonus + separator + two.getLandmarkShieldsBonus() + lineReturn;
        }
        if (!(landmarkCommerceBonus == two.getLandmarkCommerceBonus()))
        {
                toReturn = toReturn + "LandmarkCommerceBonus: " + landmarkCommerceBonus + separator + two.getLandmarkCommerceBonus() + lineReturn;
        }
        if (!(landmarkMovementCost == two.getLandmarkMovementCost()))
        {
                toReturn = toReturn + "LandmarkMovementCost: " + landmarkMovementCost + separator + two.getLandmarkMovementCost() + lineReturn;
        }
        if (!(landmarkDefenceBonus == two.getLandmarkDefenceBonus()))
        {
                toReturn = toReturn + "LandmarkDefenceBonus: " + landmarkDefenceBonus + separator + two.getLandmarkDefenceBonus() + lineReturn;
        }
        if (landmarkName.compareTo(two.getLandmarkName()) != 0)
        {
                toReturn = toReturn + "LandmarkName: " + landmarkName + separator + two.getLandmarkName() + lineReturn;
        }
        if (landmarkCivilopediaEntry.compareTo(two.getLandmarkCivilopediaEntry()) != 0)
        {
                toReturn = toReturn + "LandmarkCivilopediaEntry: " + landmarkCivilopediaEntry + separator + two.getLandmarkCivilopediaEntry() + lineReturn;
        }
        if (!(questionMark2 == two.getQuestionMark2()))
        {
                toReturn = toReturn + "QuestionMark2: " + questionMark2 + separator + two.getQuestionMark2() + lineReturn;
        }
        if (!(terrainFlags == two.getTerrainFlags()))
        {
                toReturn = toReturn + "TerrainFlags: " + terrainFlags + separator + two.getTerrainFlags() + lineReturn;
        }
        if (!(diseaseStrength == two.getDiseaseStrength()))
        {
                toReturn = toReturn + "DiseaseStrength: " + diseaseStrength + separator + two.getDiseaseStrength() + lineReturn;
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

}
