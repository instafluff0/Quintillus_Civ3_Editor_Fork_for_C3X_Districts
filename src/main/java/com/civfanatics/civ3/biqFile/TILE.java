package com.civfanatics.civ3.biqFile;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * The tiles are ordered (in the BIQ) from left to right, and then top to bottom.
 * So the top row is finished before the second row starts.
 * @author Quintillus
 */
public class TILE extends BIQSection{
    public int dataLength = 45;
    byte riverConnectionInfo;  //format: NW_??_SW_??_SE?__NE_?? where a direction or ?? is a bit
    byte border;

    public final static int ROAD_MASK = 0x00000001;
    public final static int RAILROAD_MASK = 0x00000002;
    public final static int MINE_MASK = 0x00000004;
    public final static int IRRIGATION_MASK = 0x00000008;
    public final static int FORT_MASK = 0x00000010;
    public final static int GOODY_HUT_MASK = 0x00000020;
    public final static int POLLUTION_MASK = 0x00000040;
    public final static int BARBARIAN_CAMP_MASK = 0x00000080;
    public final static int CRATER_MASK = 0x00000100;
    public final static int BARRICADE_MASK = 0x10000000;
    public final static int AIRFIELD_MASK = 0x20000000;
    public final static int RADAR_TOWER_MASK = 0x40000000;
    public final static int OUTPOST_MASK = 0x80000000;

    public final static int RUINS_PRESENT = 1;
    public final static int RUINS_NOT_PRESENT = 0;
    public final static short VICTORY_POINT_LOCATION_PRESENT = 0;
    public final static short VICTORY_POINT_LOCATION_NOT_PRESENT = -1;
    public final static int PLAYER_START_MASK = 0x00000008;

    public final static byte RIVER_NORTHEAST = 2;
    public final static byte RIVER_SOUTHEAST = 8;
    public final static byte RIVER_SOUTHWEST = 32;
    public final static byte RIVER_NORTHWEST = (byte)128;

    public final static byte XTGC = 0;
    public final static byte XPGC = 1;
    public final static byte XDGC = 2;
    public final static byte XDPC = 3;
    public final static byte XDGP = 4;
    public final static byte XGGC = 5;
    public final static byte WCSO = 6;
    public final static byte WSSS = 7;
    public final static byte WOOO = 8;

    public final static int[]c3cOverlayMasks = {ROAD_MASK, RAILROAD_MASK, MINE_MASK, IRRIGATION_MASK,
        FORT_MASK, GOODY_HUT_MASK, POLLUTION_MASK, BARBARIAN_CAMP_MASK, CRATER_MASK, BARRICADE_MASK};

    public int resource = -1;
    /**
     * Documentation for file and image.
     * The file variable selects which file contains the graphics for this tile.
     * There are 9 files used for base terrain
     * 0 = xtgc.pcx, 1 = xgpc.pcx (xpgc - Quint), 2 = xdgc.pcx,
     * 3 = xdpc.pcx, 4 = xdgp.pcx, 5 = xggc.pcx,
     * 6 = wcso.pcx, 7 = wsss.pcx, 8 = wooo.pcx
     * The code is x = land, w = water, t = tundra, g = grassland, c = coast,
     * d = desert, c = coast, s = sea, o = ocean.  Each tile can contain up to
     * three surrounding types of terrain.
     *
     * The image variable then selects which image within the file.
     * Each file is 1152x576 of 128x64 tiles; thus a 9x9 grid for 81 tiles total.
     * So the image variable can go from 0 to 80.
     *
     * The graphics is then positioned on the map such that it is centered at
     * the very top of the isometric tile.  Thus it will overlap into the tiles
     * NW, N, and NE of this tile.  Screenshot of the Day 99 (http://www.civfanatics.com/sotd/sotd99.jpg)
     * includes a grid overlay (in red) showing the grid used for where terrain
     * graphics are placed.
     *
     * Also note that landmark terrain graphics can be different.  The files for
     * those are in Conquests/Art/Terrain, and have the same filenames as the
     * above, but with l (lowercase L) in front, such as lwcso.pcx.  We need to
     * decode how to distinguish these tiles.  ODDLY, messing with these files
     * doesn't seem to cause in-game images to become distorted, so maybe
     * they aren't actually used after all.
     *
     * Decoding guesses: questionMark3 = landmark tile (could also be ?2)
     * questionMark = two bytes for image/file for overlay (ex. forest, hill, etc.)
     *     although not sure, if it's ALWAYS zero, that doesn't work
     */
    byte image;
    byte file;
    short questionMark;
    byte overlays;
    //Begin PTW and Vanilla terrains/bonuses
    byte realBaseTerrain;
    byte realTerrain;
    byte baseTerrain;
    byte bonuses;
    byte riverCrossingData; //appears to always be zero.  can use river connection info to figure
                            //out where rivers are instead
    short barbarianTribe = -1; //-1 is none
    //TODO: It appears that the "city" and "colony" tags in the BIQ documentation
    //on Apolyton are swapped.
    short colony = -1;   //-1 is none
    short city = -1;
    short continent;
    byte questionMark2 = 6; //Unknown.  Crashes Civ if it's zero.  Firaxis's editor seems to default in 6 for new tiles.  However, it IS zero in Gamelord's Euro.bic file.
    short victoryPointLocation = -1; //0 = yes, -1 = no
    public int ruin;
    public int C3COverlays; //see the masks (above) for a decryption
    byte questionMark3;
    //Contrary to Apolyton documentation, the "real" terrain comes first, and the "base" second
    //Base meaning what the features are on.  Base = grassland, real = the forest on the grassland.
    private byte C3CRealBaseTerrain;
    //Real terrain includes forests/jungles/etc.
    private byte c3cRealTerrain;    //9 = Marsh, 8 = Jungle, 7 = Forest, 6 = Mountain, 5 = Hill
    //Base terrain includes whatever the forest/jungles/etc. are on (ex. grassland)
    private byte c3cBaseTerrain;
    short questionMark4;
    short fogOfWar;
    public int C3CBonuses;
    //8192 or 0x2000 = landmark terrain
    //2048 = standalone river to south
    //1024 = standalone river to east
    //512 = standalone river to west
    //256 = standalone river to north
    //32 = Pine Forest
    //16 = Snow-Capped Mts
    //1 = Bonus Grassland
    
    /**
     * .
     * A tile NORTH of a standalone river has:
     *   C3C Bonuses of 2048 -> to south
     * A tile WEST of a standalone river has:
     *   C3C Bonuses of 1024 -> to east
     * A tile EAST of a standalone river has:
     *   C3C Bonuses of 512 -> to west
     * A tile SOUTH of a standalone river has:
     *   C3C Bonuses of 256 -> to north
     * Confirmed suspicions
     * 
     * It appears darnt of Apolyton had wrote about this on Page 3 of the BIC thread, post 76: 
     *   http://apolyton.net/showthread.php/48062-Civilization-III-BIC-file-format-(2nd-thread)/page3
     *  It looks like what used to be the "river info" he writes of had the "river source info" added to C3C Bonuses with Conquests
     * 
     * The second byte of "river info" (the river source info)'s byte from Vanilla is now "borders" in Gramphos's documentation.
     * However, it appears I am not using that byte whatsoever.
     */
    
    short questionMark5;
    int extraInt;   //appears in 7 Sengoku and Intro 3 only.  These are 12.06, but not all 12.06 have the extra int.

    public List<Integer>citiesWithInfluence = new ArrayList<Integer>();
    public int owner;
    public int ownerType;
    boolean definiteOwner = false;
    public int borderColor;
    public int tileID;
    public int xPos;
    public int yPos;
    public int index;   //index in the list of TILEs
    public List<Integer>unitsOnTile = new ArrayList<Integer>();
    public int unitWithBestDefence = -1;

    public TILE(IO baselink)
    {
        super(baselink);
        citiesWithInfluence = new ArrayList<Integer>();
        unitsOnTile = new ArrayList<Integer>();
    }
    
    public TILE(civ3Version version) {
        super(version);
    }
    
    public TILE(IO baselink, int terrain)
    {
        this(baselink);
        if (terrain == TERR.DESERT)
            file = 2;
        else if (terrain == TERR.PLAINS)
            file = 1;
        else if (terrain == TERR.GRASSLAND)
            file = 5;
        else if (terrain == TERR.OCEAN)
            file = 8;
        else if (terrain == TERR.SEA)
            file = 7;
        else if (terrain == TERR.COAST)
            file = 6;
        //default, tundra, leave it at 0
        //Set up C3C basereal terrain
        this.c3cRealTerrain = (byte)terrain;
        this.c3cBaseTerrain = (byte)terrain;
        this.C3CRealBaseTerrain = (byte)(((byte)(c3cRealTerrain) << 4) | (byte)c3cBaseTerrain);
    }
    public int getDataLength()
    {
        return dataLength;
    }

    public byte getRiverConnectionInfo()
    {
        return riverConnectionInfo;
    }

    public byte getBorder()
    {
        return border;
    }

    public int getResource()
    {
        return resource;
    }

    public byte getImage()
    {
        return image;
    }

    public byte getFile()
    {
        return file;
    }

    public short getQuestionMark()
    {
        return questionMark;
    }

    public byte getOverlays()
    {
        return overlays;
    }

    public byte getBaseRealTerrain()
    {
        return realBaseTerrain;
    }

    public byte getBonuses()
    {
        return bonuses;
    }

    public byte getRiverCrossingData()
    {
        return riverCrossingData;
    }

    public short getBarbarianTribe()
    {
        return barbarianTribe;
    }

    public short getColony()
    {
        return colony;
    }

    public short getCity()
    {
        return city;
    }

    public short getContinent()
    {
        return continent;
    }

    public byte getQuestionMark2()
    {
        return questionMark2;
    }

    public short getVictoryPointLocation()
    {
        return victoryPointLocation;
    }

    public int getRuin()
    {
        return ruin;
    }

    public int getC3COverlays()
    {
        return C3COverlays;
    }

    public byte getQuestionMark3()
    {
        return questionMark3;
    }

    public byte getC3CBaseRealTerrain()
    {
        return C3CRealBaseTerrain;
    }

    public short getQuestionMark4()
    {
        return questionMark4;
    }

    public short getFogOfWar()
    {
        return fogOfWar;
    }

    public int getC3CBonuses()
    {
        return C3CBonuses;
    }

    public short getQuestionMark5()
    {
        return questionMark5;
    }

    public void setUpNibbles()
    {
        //System.out.println("Both: " + C3CBaseRealTerrain);
        this.c3cRealTerrain = (byte)((this.C3CRealBaseTerrain & 0xF0) >>> 4);
        //System.out.println("Base: " + c3cBaseTerrain);
        this.c3cBaseTerrain = (byte)(this.C3CRealBaseTerrain & 0x0F);
        //System.out.println("Real: " + c3cRealTerrain);
    }
    
    public byte getBaseTerrain()
    {
        return c3cBaseTerrain;
    }
    public byte getRealTerrain()
    {
        return c3cRealTerrain;
    }

    public void setDataLength(int dataLength)
    {
        this.dataLength = dataLength;
    }

    public void setRiverConnectionInfo(byte riverConnectionInfo)
    {
        this.riverConnectionInfo = riverConnectionInfo;
    }
    
    /**
     * Sets whether there is a river on a particular border of this tile.  The
     * direction should be one of RIVER_NORTHWEST, RIVER_NORTHEAST, etc. - that
     * is, a bitmask for the direction of the river.
     * @param isRiver Whether the tile should have a river on this border
     * @param direction Bitmask indicating which border the river is/is not on
     */
    public void setRiverAtBorder(boolean isRiver, byte direction) {
        if (isRiver) {
            this.riverConnectionInfo = (byte)(this.riverConnectionInfo | direction);
        }
        else {
            this.riverConnectionInfo = (byte)(this.riverConnectionInfo & ~direction);
        }
    }

    public void setBorder(byte border)
    {
        this.border = border;
    }

    public void setResource(int resource)
    {
        this.resource = resource;
    }

    public void setImage(byte image) 
   {
        this.image = image;
    }

    public void setFile(byte file)
    {
        this.file = file;
    }

    public void setQuestionMark(short questionMark)
    {
        this.questionMark = questionMark;
    }

    public void setOverlays(byte overlays)
    {
        this.overlays = overlays;
    }

    /**
     * Use the C3C version with Conquests
     * @param realBaseTerrain
     * @deprecated
     */
    @Deprecated
    public void setBaseRealTerrain(byte baseRealTerrain)
    {
        //System.out.println("inval: " + realBaseTerrain);
        this.realBaseTerrain = baseRealTerrain;
        //System.out.println("outval: " +  this.realBaseTerrain);
    }

    public void setBonuses(byte bonuses)
    {
        this.bonuses = bonuses;
    }

    public void setRiverCrossingData(byte riverCrossingData)
    {
        this.riverCrossingData = riverCrossingData;
    }

    public void setBarbarianTribe(short barbarianTribe)
    {
        this.barbarianTribe = barbarianTribe;
    }

    public void setColony(short colony)
    {
        this.colony = colony;
    }

    public void setCity(short city)
    {
        this.city = city;
    }

    public void setContinent(short continent)
    {
        this.continent = continent;
    }

    public void setQuestionMark2(byte questionMark2)
    {
        this.questionMark2 = questionMark2;
    }

    public void setVictoryPointLocation(short victoryPointLocation)
    {
        this.victoryPointLocation = victoryPointLocation;
    }

    public void setRuin(int ruin)
    {
        this.ruin = ruin;
    }

    public void setC3COverlays(int C3COverlays)
    {
        this.C3COverlays = C3COverlays;
    }

    public void setQuestionMark3(byte questionMark3)
    {
        this.questionMark3 = questionMark3;
    }

    /**
     * Sets the Civ3 Conquests real and base terrains at the same time.
     * This is INTENTIONALLY package-level access; it should only be called when
     * a BIQ is being imported.  Elsewhere, the real/base terrain methods should
     * be used instead.
     * @param C3CRealBaseTerrain 
     */
    void setC3CBaseRealTerrain(byte C3CRealBaseTerrain)
    {
        this.C3CRealBaseTerrain = C3CRealBaseTerrain;
        this.c3cRealTerrain = (byte)((C3CRealBaseTerrain & 0xF0) >>> 4);
        this.c3cBaseTerrain = (byte)(C3CRealBaseTerrain & 0x0F);
    }

    public void setQuestionMark4(short questionMark4)
    {
        this.questionMark4 = questionMark4;
    }

    public void setFogOfWar(short fogOfWar)
    {
        this.fogOfWar = fogOfWar;
    }

    public void setC3CBonuses(int C3CBonuses)
    {
        this.C3CBonuses = C3CBonuses;
    }

    public void setQuestionMark5(short questionMark5)
    {
        this.questionMark5 = questionMark5;
    }
    
    public void setExtraInt(int extraInt) {
        this.extraInt = extraInt;
    }

    public String toEnglish(){
        return toString();
    }
    
    /**
     * Returns whether or not this is a water tile.
     * @return True if the terrain is coast, sea, or ocean.
     */
    public boolean isWater()
    {
        return c3cBaseTerrain >= 11;
    }
    
    public boolean isLandmark()
    {
        return (this.C3CBonuses & 0x2000) == 0x2000;
    }
    
    public boolean isPineForest() {
        return (this.C3CBonuses & 32) == 32;
    }
    
    public boolean isSnowCappedMountain() {
        return (this.C3CBonuses & 16) == 16;
    }
    
    public boolean riverSouth() {
        return (this.C3CBonuses & 2048) == 2048;
    }
    
    public boolean riverEast() {
        return (this.C3CBonuses & 1024) == 1024;
    }
    
    public boolean riverWest() {
        return (this.C3CBonuses & 512) == 512;
    }
    
    public boolean riverNorth() {
        return (this.C3CBonuses & 256) == 256;
    }
    
    public void setBonusGrassland(boolean flag)
    {
        if (flag)
        {
            this.C3CBonuses = this.C3CBonuses | 0x0001;
        }
        else
        {
            this.C3CBonuses = this.C3CBonuses & 0xFFFFFFFE;  //everything except the last bit
        }
    }
    
    public boolean hasBonusGrassland()
    {
        return (this.C3CBonuses & 0x0001) == 1;
    }
    
    /**
     * Sets the base terrain of a tile.
     * Will handle nibbles and PTW/Conquests differences for you.
     * @param newBaseTerrain The new base terrain
     */
    public void setBaseTerrain(int newBaseTerrain)
    {
        byte newByte = (byte)newBaseTerrain;
        if (baseLink.version == civ3Version.CONQUESTS || baseLink.convertToConquests == 1)
        {
            c3cBaseTerrain = newByte;
            C3CRealBaseTerrain = (byte)((0xF0 & C3CRealBaseTerrain) + newByte);
        }
        else
        {
            baseTerrain = newByte;
            realBaseTerrain = (byte)((0xF0 & realBaseTerrain) + newByte);
        }
    }
    
    /**
     * Sets the real terrain of a tile (forest, hills, etc.).
     * Will handle nibbles and PTW/Conquests differences for you.
     * @param newRealTerrain 
     */
    public void setRealTerrain(int newRealTerrain)
    {
        
        byte newByte = (byte)newRealTerrain;
        if (baseLink.version == civ3Version.CONQUESTS || baseLink.convertToConquests == 1)
        {
            c3cRealTerrain = newByte;
            C3CRealBaseTerrain = (byte)((0x0F & C3CRealBaseTerrain) + (newByte << 4));
        }
        else
        {
            realTerrain = newByte;
            realBaseTerrain = (byte)((0x0F & realBaseTerrain) + (newByte << 4));
        }
    }
    
    /**
     * Sets both the base and real terrain
     * @param newTerrain 
     */
    public void setTerrain(int newTerrain)
    {
        
    }

    public String toString()
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "Tile at " + xPos + ", " + yPos + lineReturn;
        toReturn = toReturn + "tileID: " + tileID + lineReturn;
        toReturn = toReturn + "dataLength: " + dataLength + lineReturn;
        toReturn = toReturn + "riverConnectionInfo: " + riverConnectionInfo + lineReturn;
        toReturn = toReturn + "border: " + border + lineReturn;
        toReturn = toReturn + "resource: " + resource + lineReturn;
        toReturn = toReturn + "image: " + image + lineReturn;
        toReturn = toReturn + "file: " + file + lineReturn;
        toReturn = toReturn + "questionMark: " + questionMark + lineReturn;
        toReturn = toReturn + "overlays: " + overlays + lineReturn;
        toReturn = toReturn + "baseRealTerrain: " + realBaseTerrain + lineReturn;
        toReturn = toReturn + "bonuses: " + bonuses + lineReturn;
        toReturn = toReturn + "riverCrossingData: " + riverCrossingData + lineReturn;
        toReturn = toReturn + "barbarianTribe: " + barbarianTribe + lineReturn;
        toReturn = toReturn + "colony: " + colony + lineReturn;
        toReturn = toReturn + "city: " + city + lineReturn;
        toReturn = toReturn + "continent: " + continent + lineReturn;
        toReturn = toReturn + "questionMark2: " + questionMark2 + lineReturn;
        toReturn = toReturn + "victoryPointLocation: " + victoryPointLocation + lineReturn;
        toReturn = toReturn + "ruin: " + ruin + lineReturn;
        toReturn = toReturn + "C3COverlays: " + C3COverlays + lineReturn;
        toReturn = toReturn + "questionMark3: " + questionMark3 + lineReturn;
        toReturn = toReturn + "C3CBaseRealTerrain: " + C3CRealBaseTerrain + lineReturn;
        toReturn = toReturn + "questionMark4: " + questionMark4 + lineReturn;
        toReturn = toReturn + "fogOfWar: " + fogOfWar + lineReturn;
        toReturn = toReturn + "C3CBonuses: " + C3CBonuses + lineReturn;
        toReturn = toReturn + "questionMark5: " + questionMark5 + lineReturn;
        toReturn = toReturn + "owner: " + owner + lineReturn;
        toReturn = toReturn + "borderColor: " + borderColor + lineReturn;
        toReturn = toReturn + "definiteOwner? " + definiteOwner + lineReturn;
        for (int i = 0; i < this.citiesWithInfluence.size(); i++)
            toReturn = toReturn + "influenced by city: " + citiesWithInfluence.get(i) + lineReturn;
        for (int i = 0; i < this.unitsOnTile.size(); i++)
            toReturn = toReturn + "unit on tile: " + baseLink.unit.get(baseLink.mapUnit.get(unitsOnTile.get(i)).getPRTONumber()).getName() + lineReturn;
        toReturn = toReturn + lineReturn;
        return toReturn;
    }

    public String compareTo(BIQSection section, String separator)
    {
        if (!(section instanceof TILE))
            return null;
        TILE two = (TILE)section;
        String toReturn = "";
        String lineReturn = java.lang.System.getProperty("line.separator");
        if (!(dataLength == two.getDataLength()))
        {
                toReturn = toReturn + "DataLength: " + dataLength + separator + two.getDataLength() + lineReturn;
        }
        if (!(riverConnectionInfo == two.getRiverConnectionInfo()))
        {
                toReturn = toReturn + "RiverConnectionInfo: " + riverConnectionInfo + separator + two.getRiverConnectionInfo() + lineReturn;
        }
        if (!(border == two.getBorder()))
        {
                toReturn = toReturn + "Border: " + border + separator + two.getBorder() + lineReturn;
        }
        if (!(resource == two.getResource()))
        {
                toReturn = toReturn + "Resource: " + resource + separator + two.getResource() + lineReturn;
        }
        if (!(image == two.getImage()))
        {
                toReturn = toReturn + "Image: " + image + separator + two.getImage() + lineReturn;
        }
        if (!(file == two.getFile()))
        {
                toReturn = toReturn + "File: " + file + separator + two.getFile() + lineReturn;
        }
        if (!(questionMark == two.getQuestionMark()))
        {
                toReturn = toReturn + "QuestionMark: " + questionMark + separator + two.getQuestionMark() + lineReturn;
        }
        if (!(overlays == two.getOverlays()))
        {
                toReturn = toReturn + "Overlays: " + overlays + separator + two.getOverlays() + lineReturn;
        }
        if (!(realBaseTerrain == two.getBaseRealTerrain()))
        {
                toReturn = toReturn + "BaseRealTerrain: " + realBaseTerrain + separator + two.getBaseRealTerrain() + lineReturn;
        }
        if (!(bonuses == two.getBonuses()))
        {
                toReturn = toReturn + "Bonuses: " + bonuses + separator + two.getBonuses() + lineReturn;
        }
        if (!(riverCrossingData == two.getRiverCrossingData()))
        {
                toReturn = toReturn + "RiverCrossingData: " + riverCrossingData + separator + two.getRiverCrossingData() + lineReturn;
        }
        if (!(barbarianTribe == two.getBarbarianTribe()))
        {
                toReturn = toReturn + "BarbarianTribe: " + barbarianTribe + separator + two.getBarbarianTribe() + lineReturn;
        }
        if (!(colony == two.getColony()))
        {
                toReturn = toReturn + "Colony: " + colony + separator + two.getColony() + lineReturn;
        }
        if (!(city == two.getCity()))
        {
                toReturn = toReturn + "City: " + city + separator + two.getCity() + lineReturn;
        }
        if (!(continent == two.getContinent()))
        {
                toReturn = toReturn + "Continent: " + continent + separator + two.getContinent() + lineReturn;
        }
        if (!(questionMark2 == two.getQuestionMark2()))
        {
                toReturn = toReturn + "QuestionMark2: " + questionMark2 + separator + two.getQuestionMark2() + lineReturn;
        }
        if (!(victoryPointLocation == two.getVictoryPointLocation()))
        {
                toReturn = toReturn + "VictoryPointLocation: " + victoryPointLocation + separator + two.getVictoryPointLocation() + lineReturn;
        }
        if (!(ruin == two.getRuin()))
        {
                toReturn = toReturn + "Ruin: " + ruin + separator + two.getRuin() + lineReturn;
        }
        if (!(C3COverlays == two.getC3COverlays()))
        {
                toReturn = toReturn + "C3COverlays: " + C3COverlays + separator + two.getC3COverlays() + lineReturn;
        }
        if (!(questionMark3 == two.getQuestionMark3()))
        {
                toReturn = toReturn + "QuestionMark3: " + questionMark3 + separator + two.getQuestionMark3() + lineReturn;
        }
        if (!(C3CRealBaseTerrain == two.getC3CBaseRealTerrain()))
        {
                toReturn = toReturn + "C3CBaseRealTerrain: " + C3CRealBaseTerrain + separator + two.getC3CBaseRealTerrain() + lineReturn;
        }
        if (!(c3cRealTerrain == two.c3cRealTerrain))
        {
                toReturn = toReturn + "c3cRealTerrain: " + c3cRealTerrain + separator + two.c3cRealTerrain + lineReturn;
        }
        if (!(c3cBaseTerrain == two.c3cBaseTerrain))
        {
                toReturn = toReturn + "c3cBaseTerrain: " + c3cBaseTerrain + separator + two.c3cBaseTerrain + lineReturn;
        }
        if (!(questionMark4 == two.getQuestionMark4()))
        {
                toReturn = toReturn + "QuestionMark4: " + questionMark4 + separator + two.getQuestionMark4() + lineReturn;
        }
        if (!(fogOfWar == two.getFogOfWar()))
        {
                toReturn = toReturn + "FogOfWar: " + fogOfWar + separator + two.getFogOfWar() + lineReturn;
        }
        if (!(C3CBonuses == two.getC3CBonuses()))
        {
                toReturn = toReturn + "C3CBonuses: " + C3CBonuses + separator + two.getC3CBonuses() + lineReturn;
        }
        if (isLandmark() != two.isLandmark()) {
            toReturn = toReturn + "  isLandmark: " + isLandmark() + separator + two.isLandmark() + lineReturn;
        }
        if (isPineForest()!= two.isPineForest()) {
            toReturn = toReturn + "  isPineForest: " + isPineForest() + separator + two.isPineForest() + lineReturn;
        }
        if (isSnowCappedMountain()!= two.isSnowCappedMountain()) {
            toReturn = toReturn + "  snowCappedMountain: " + isSnowCappedMountain() + separator + two.isSnowCappedMountain() + lineReturn;
        }
        if (hasBonusGrassland()!= two.hasBonusGrassland()) {
            toReturn = toReturn + "  bonusGrassland: " + hasBonusGrassland() + separator + two.hasBonusGrassland()+ lineReturn;
        }
        if (riverSouth()!= two.riverSouth()) {
            toReturn = toReturn + "  riverSouth: " + riverSouth() + separator + two.riverSouth() + lineReturn;
        }
        if (riverEast()!= two.riverEast()) {
            toReturn = toReturn + "  riverEast: " + riverEast() + separator + two.riverEast() + lineReturn;
        }
        if (riverWest()!= two.riverWest()) {
            toReturn = toReturn + "  riverWest: " + riverWest() + separator + two.riverWest() + lineReturn;
        }
        if (riverNorth()!= two.riverNorth()) {
            toReturn = toReturn + "  riverNorth: " + riverNorth() + separator + two.riverNorth() + lineReturn;
        }
        if (!(questionMark5 == two.getQuestionMark5()))
        {
                toReturn = toReturn + "QuestionMark5: " + questionMark5 + separator + two.getQuestionMark5() + lineReturn;
        }
        if (!toReturn.equals(""))
            toReturn = xPos + ", " + yPos + lineReturn + toReturn;
        return toReturn;
    }
    public Object getProperty(String string) throws UnsupportedOperationException
    {
        if (string.equals("Name"))
            return this.xPos + ", " + this.yPos;
        throw new UnsupportedOperationException();
    }

    public void calculateUnitWithBestDefence()
    {
        if (unitsOnTile.size() == 0)
        {
            unitWithBestDefence = -1;
            return;
        }
        int bestDefenceValue = -1;
        unitWithBestDefence = -1;
        for (int i = 0; i < unitsOnTile.size(); i++)
        {
            UNIT unit = baseLink.mapUnit.get(unitsOnTile.get(i));
            PRTO unitType = baseLink.unit.get(unit.getPRTONumber());
            EXPR exprLevel = baseLink.experience.get(unit.getExperienceLevel());
            int numHitpoints = exprLevel.baseHitPoints + unitType.getHitPointBonus();
            int unitDefence = unitType.getDefence() * numHitpoints;
            if (unitDefence > bestDefenceValue)
            {
                bestDefenceValue = unitDefence;
                unitWithBestDefence = i;
            }
        }
    }
    
    /**
     * NOTE: base is the "base" as in, the basic thing.  Real is the thing on top, as in, the forest or hills or whatever.
     * THIS IS SWAPPED FROM HOW BASE AND REAL ARE DEFINED ELSEWHERE IN THIS CLASS.
     * This is how it logically makes sense; this class's definitions of them are borked.
     * This sets up the nibbles and bytes and whatnot in one place.
     * @param base
     * @param real 
     */
    public void setTerrain(int base, int real)
    {
        //INTENTIONALLY WHOPPERJAWED.  See note above.
        this.c3cBaseTerrain = (byte)(base & 0xFF);
        this.c3cRealTerrain = (byte)(real & 0xFF);
        
        byte compositeTerrain = (byte)(real << 4 | (base & 0xFF));
        this.C3CRealBaseTerrain = compositeTerrain;
        //System.out.println("Composite terrain for tile " + this.xPos + ", " + this.yPos + " is " + compositeTerrain + " out of base of " + base + " and real of " + real);
    }
    
    /**
     * Utility method to set whether the tile is a pine forest or not, and take
     * care of the binary stuff for you.
     * Note this only change the pine parameter, not the forest parameter.
     * @param pine - Whether the tile should be pined or not.
     */
    public void setPine(boolean pine)
    {
        if (pine)
            C3CBonuses = C3CBonuses | 32;
        else
            C3CBonuses = C3CBonuses & ~32;
    }
    
    /**
     * Utility method to set whether the tile is a snowy or not, and take
     * care of the binary stuff for you.
     * Note this only change the snow parameter, not the mountain parameter.
     * @param snow - Whether the tile should be pined or not.
     */
    public void setSnow(boolean snow)
    {
        if (snow)
            C3CBonuses = C3CBonuses | 16;
        else
            C3CBonuses = C3CBonuses & ~16;
    }
    
    /**
     * Utility method to set whether the tile is a landmark or not, and take
     * care of the binary stuff for you.
     * @param landmark - Whether the tile should be pined or not.
     */
    public void setLandmark(boolean landmark)
    {
        if (landmark)
            C3CBonuses = C3CBonuses | 0x2000;
        else
            C3CBonuses = C3CBonuses & ~0x2000;
    }
    
    /**
     * Utility method to set whether a tile is irrigated.
     * Eventually may be enhanced to handle pre-Conquests.
     * @param irrigated 
     */
    public void setIrrigated(boolean irrigated) {
        setOverlay(irrigated, IRRIGATION_MASK);
    }
    
    /**
     * Utility method to set whether a tile is mined.
     * Eventually may be enhanced to handle pre-Conquests.
     * @param mined 
     */
    public void setMined(boolean mined) {
        setOverlay(mined, MINE_MASK);
    }
    
    /**
     * Utility method to set whether a tile is mined.
     * Eventually may be enhanced to handle pre-Conquests.
     * @param road 
     */
    public void setRoad(boolean road) {
        setOverlay(road, ROAD_MASK);
    }
    
    /**
     * Utility method to set whether a tile is mined.
     * Eventually may be enhanced to handle pre-Conquests.
     * @param railroad 
     */
    public void setRailroad(boolean railroad) {
        setOverlay(railroad, RAILROAD_MASK);
    }
    
    /**
     * Utility method to set whether a tile is mined.
     * Eventually may be enhanced to handle pre-Conquests.
     * @param fort 
     */
    public void setFort(boolean fort) {
        setOverlay(fort, FORT_MASK);
    }
    
    /**
     * Utility method or setting whether a tile has a barricade.
     * @param barricade 
     */
    public void setBarricade(boolean barricade) {
        setOverlay(barricade, BARRICADE_MASK);
    }
    
    /**
     * Utility method or setting whether a tile has a barbarian camp.
     * @param barbarianCamp 
     */
    public void setBarbarianCamp(boolean barbarianCamp) {
        setOverlay(barbarianCamp, BARBARIAN_CAMP_MASK);
    }
    
    /**
     * Utility method or setting whether a tile has a goody hut.
     * @param goodyHut 
     */
    public void setGoodyHut(boolean goodyHut) {
        setOverlay(goodyHut, GOODY_HUT_MASK);
    }
    
    /**
     * Utility method or setting whether a tile has pollution.
     * @param pollution 
     */
    public void setPollution(boolean pollution) {
        setOverlay(pollution, POLLUTION_MASK);
    }
    
    /**
     * Utility method for setting whether a tile has craters.
     * @param crater 
     */
    public void setCrater(boolean crater) {
        setOverlay(crater, CRATER_MASK);
    }
    
    /**
     * General-purpose overlay-setting method to reduce code duplication.
     * Will allow updating to allow pre-Conquests in one location only.
     * @param present
     * @param MASK 
     */
    private void setOverlay(boolean present, int MASK) {
        if (present)
            C3COverlays = C3COverlays | MASK;
        else
            C3COverlays = C3COverlays & ~MASK;
    }
}
