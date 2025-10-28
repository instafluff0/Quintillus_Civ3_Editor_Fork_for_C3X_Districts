package com.civfanatics.civ3.biqFile;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Quintillus
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
public class CITY extends BIQSection{
    public static final int OWNER_CIV = 2;
    public static final int OWNER_PLAYER = 3;
    public static final int OWNER_BARBARIANS = 1;
    public static final int OWNER_NONE = 0;
    public static final String[] ownerTypes = {"None", "Barbarians", "Civ", "Player"};
    //Begin data I use in memory (but which isn't stored in the BIQ)
    //TODO: Decide whether this really should be public.  It's used in a lot of places currently...
    public List<TILE>tilesInfluenced;
    
    //Begin what's stored in the BIQ
    private int dataLength = 66;
    private byte hasWalls;
    private byte hasPalace;
    private String name = "";
    private int ownerType = OWNER_CIV;
    private ArrayList<Integer>buildingIDs;
    private int culture = 0;
    private int owner = 1;   //first non-barbarian civilization
    private int size = 1;
    private int x;
    private int y;
    private int cityLevel;
    private int borderLevel = 1;
    private int useAutoName;
    private boolean wallStyleBuilding = false;
    
    public IO baselink;
    
    public CITY(IO baselink, String name, int x, int y)
    {
        this(baselink);
        this.name = name;
        this.x = x;
        this.y = y;
    }
    
    public CITY(IO baselink)
    {
        super(baselink);
        this.baselink = baselink;
        buildingIDs = new ArrayList<Integer>();
        tilesInfluenced = new LinkedList<TILE>();
    }

    public void trim()
    {
        name = name.trim();
    }
    public int getDataLength()
    {
        return dataLength;
    }

    public byte getHasWalls()
    {
        return hasWalls;
    }

    public byte getHasPalace()
    {
        return hasPalace;
    }

    public String getName()
    {
        return name;
    }

    public int getOwnerType()
    {
        return ownerType;
    }

    public int getCulture()
    {
        return culture;
    }

    public int getOwner()
    {
        return owner;
    }

    public int getSize()
    {
        return size;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getCityLevel()
    {
        return cityLevel;
    }

    public int getBorderLevel()
    {
        return borderLevel;
    }

    public int getUseAutoName()
    {
        return useAutoName;
    }

    public void setHasWalls(byte hasWalls)
    {
        this.hasWalls = hasWalls;
    }

    public void setHasPalace(byte hasPalace)
    {
        this.hasPalace = hasPalace;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setOwnerType(int ownerType)
    {
        this.ownerType = ownerType;
    }

    public void setCulture(int culture)
    {
        this.culture = culture;
    }

    public void setOwner(int owner)
    {
        this.owner = owner;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setCityLevel(int cityLevel)
    {
        this.cityLevel = cityLevel;
    }

    public void setBorderLevel(int borderLevel)
    {
        this.borderLevel = borderLevel;
    }

    public void setUseAutoName(int useAutoName)
    {
        this.useAutoName = useAutoName;
    }
    
    public void addBuilding(int newBuildingID)
    {
        this.buildingIDs.add(newBuildingID);
        dataLength+=4;
    }
    
    /**
     * Get the building at index <i>index</i> for this city.
     * This is used to iterate through the buildings.
     * @param index The index among the city's buildings.
     * @return The BLDG id of that building.
     */
    public int getBuildingID(int index)
    {
        return this.buildingIDs.get(index);
    }
    
    public int getNumBuildings()
    {
        return this.buildingIDs.size();
    }
    
    /**
     * Removes the building whose building ID is id from this city.
     * @param id The ID of the building.
     */
    public void removeBuilding(int id)
    {
        //Don't iterate over the collection or we'll get a concurrent modification exception
        int max = buildingIDs.size();
        for (int i = 0; i < max; i++)
        {
            if (buildingIDs.get(i) == id)
            {
                buildingIDs.remove(i);
                this.dataLength-=4;
                break;
            }
        }
    }
    
    /**
     * Removes all buildings from the city.
     */
    public void removeAllBuildings()
    {
        dataLength-=buildingIDs.size()*4;
        buildingIDs.clear();
    }
    
    /**
     * Updates the city's buildings after a building has been deleted.
     * Removes that building if it exists, and updates indexes of other buildings
     * as needed.
     * @param buildingID The ID of the building that was deleted.
     */
    public void handleDeletedBuilding(int buildingID)
    {
        //Cast to an object so that we can remove the index which matches the ID (rather than whatever is at a specific index)
        if (buildingIDs.remove((Integer)buildingID))
            dataLength-=4;
        for (int i = 0; i < buildingIDs.size(); i++)
        {
            int currentID = buildingIDs.get(i);
            if (currentID > buildingID)
                buildingIDs.set(i, currentID - 1);
        }
        //If Java supported arithmetic if statements, we could have written this whole method as so:
        //for (int i = 0; i < buildingIDs.size(); i++)
        //{
        //    int currentID = buildingIDs.get(i);
        //    if (currentID - buildingID) , buildingIDs.remove(i--), buildingIDs.set(i, currentID - 1);
        //}
        //Who says the arithmetic if isn't great?  Real programmers like arithmetic if statements - 
        //they make the code more interesting (and sometimes even more concise!).
    }
    
    public boolean hasBuilding(int buildingID)
    {
        return buildingIDs.contains(buildingID);
    }

    public boolean hasWallStyleBuilding()
    {
        return wallStyleBuilding;
    }

    public void setWallStyleBuilding(boolean wallStyleBuilding)
    {
        this.wallStyleBuilding = wallStyleBuilding;
    }
    
    /**
     * Calculates the defensive bonus for a city.  This is across all sources - 
     * buildings, size, terrain, and anything else I remember in the future.
     * 
     * TODO: Verify that Citizen/Building is part of it.  Also decide, should we include that?
     * @return 
     */
    public int calculateDefensiveBonus() {
        int defenseBonus = 0;
        for (int i = 0; i < buildingIDs.size(); i++) {
            BLDG building = baseLink.buildings.get(buildingIDs.get(i));
            defenseBonus += building.getDefenceBonus();
        }
        
        RULE rules = baseLink.rule.get(0);
        if (this.size > rules.getMaxCity2Size()) {
            defenseBonus += rules.getMetropolisDefenceBonus();
        }
        else if (this.size > rules.getMaxCity1Size()) {
            defenseBonus += rules.getCityDefenceBonus();
        }
        else {
            defenseBonus += rules.getTownDefenceBonus();
        }
        
        int tileIndex = baseLink.calculateTileIndex(x, y);
        TILE cityTile = baseLink.tile.get(tileIndex);
        TERR cityTerrain = baseLink.terrain.get(cityTile.getRealTerrain());
        if (cityTile.isLandmark()) {
            defenseBonus += cityTerrain.getLandmarkDefenceBonus();
        }
        else {
            defenseBonus += cityTerrain.getDefenceBonus();
        }
        return defenseBonus;
    }
    
    /**
     * Returns whether or not a city is coastal (borders a water tile)
     * @return True if the city is coastal.
     */
    public boolean isCoastal()
    {
        ArrayList<Integer>surroundingTiles = baselink.getSurroundingTiles(x, y);
        for (int tile:surroundingTiles)
            if (baselink.tile.get(tile).isWater())
                return true;
        return false;
    }

    public String toEnglish(){
        return toString();
    }

    @Override
    public String toString()
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn =  "name: " + name + lineReturn;
        toReturn = toReturn + "dataLength: " + dataLength + lineReturn;
        toReturn = toReturn + "hasWalls: " + hasWalls + lineReturn;
        toReturn = toReturn + "hasPalace: " + hasPalace + lineReturn;
        toReturn = toReturn + "ownerType: " + ownerType + lineReturn;
        toReturn = toReturn + "numBuildings: " + getNumBuildings() + lineReturn;
        for (int j = 0; j < getNumBuildings(); j++)
        {
            toReturn = toReturn + "  building:  " + buildingIDs.get(j) + lineReturn;
        }
        toReturn = toReturn + "culture: " + culture + lineReturn;
        toReturn = toReturn + "owner: " + owner + lineReturn;
        toReturn = toReturn + "size: " + size + lineReturn;
        toReturn = toReturn + "x: " + x + lineReturn;
        toReturn = toReturn + "y: " + y + lineReturn;
        toReturn = toReturn + "cityLevel: " + cityLevel + lineReturn;
        toReturn = toReturn + "borderLevel: " + borderLevel + lineReturn;
        toReturn = toReturn + "useAutoName: " + useAutoName + lineReturn;
        toReturn = toReturn + lineReturn;
        return toReturn;
    }

    public String compareTo(BIQSection section, String separator)
    {
        if (!(section instanceof CITY))
            return null;
        CITY two = (CITY)section;
        String toReturn = "";
        String lineReturn = java.lang.System.getProperty("line.separator");
        if (!(dataLength == two.getDataLength()))
        {
                toReturn = toReturn + "DataLength: " + dataLength + separator + two.getDataLength() + lineReturn;
        }
        if (!(hasWalls == two.getHasWalls()))
        {
                toReturn = toReturn + "HasWalls: " + hasWalls + separator + two.getHasWalls() + lineReturn;
        }
        if (!(hasPalace == two.getHasPalace()))
        {
                toReturn = toReturn + "HasPalace: " + hasPalace + separator + two.getHasPalace() + lineReturn;
        }
        if (name.compareTo(two.getName()) != 0)
        {
                toReturn = toReturn + "Name: " + name + separator + two.getName() + lineReturn;
        }
        if (!(ownerType == two.getOwnerType()))
        {
                toReturn = toReturn + "OwnerType: " + ownerType + separator + two.getOwnerType() + lineReturn;
        }
        if (!(getNumBuildings() == two.getNumBuildings()))
        {
                toReturn = toReturn + "NumBuildings: " + getNumBuildings() + separator + two.getNumBuildings() + lineReturn;
        }
        else  //see if a flavor is checked for a building in only one file
        {
            for (int i = 0; i < getNumBuildings(); i++)
            {
                if (!(buildingIDs.get(i) == two.buildingIDs.get(i)))
                {
                    toReturn = toReturn + "  building " + i + ": " + buildingIDs.get(i) + separator + two.buildingIDs.get(i) + lineReturn;
                }
            }
        }
        if (!(culture == two.getCulture()))
        {
                toReturn = toReturn + "Culture: " + culture + separator + two.getCulture() + lineReturn;
        }
        if (!(owner == two.getOwner()))
        {
                toReturn = toReturn + "Owner: " + owner + separator + two.getOwner() + lineReturn;
        }
        if (!(size == two.getSize()))
        {
                toReturn = toReturn + "Size: " + size + separator + two.getSize() + lineReturn;
        }
        if (!(x == two.getX()))
        {
                toReturn = toReturn + "X: " + x + separator + two.getX() + lineReturn;
        }
        if (!(y == two.getY()))
        {
                toReturn = toReturn + "Y: " + y + separator + two.getY() + lineReturn;
        }
        if (!(cityLevel == two.getCityLevel()))
        {
                toReturn = toReturn + "CityLevel: " + cityLevel + separator + two.getCityLevel() + lineReturn;
        }
        if (!(borderLevel == two.getBorderLevel()))
        {
                toReturn = toReturn + "BorderLevel: " + borderLevel + separator + two.getBorderLevel() + lineReturn;
        }
        if (!(useAutoName == two.getUseAutoName()))
        {
                toReturn = toReturn + "UseAutoName: " + useAutoName + separator + two.getUseAutoName() + lineReturn;
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
