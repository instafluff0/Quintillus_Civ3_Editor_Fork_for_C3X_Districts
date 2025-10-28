package com.civfanatics.civ3.xplatformeditor.tabs.map;

import com.civfanatics.civ3.biqFile.IO;
import com.civfanatics.civ3.biqFile.TERR;
import com.civfanatics.civ3.biqFile.TILE;
import com.civfanatics.civ3.xplatformeditor.Main;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

/**
 *
 * @author Andrew
 */
public class MapUtils {
    
    static Logger logger = Logger.getLogger(MapUtils.class);
    
    /**
     * We have the x, y offset within the 128x64 area corresponding to the greater area around (1, 1) or whichever odd tile is nearest.
     * to determine whether to move NW/NE/SE/SW such that one or both tiles was even, we examined if we were NW/NE/SE/SW of a boundary line
     * for rivers, we shall instead determine if we are _close enough_ to a boundary line, and not too close to the edge of the greater (1,1) region.
     * Math determined with the help of a TI-83+ and Zilog Z83, as visualization eases the math.  Paths of the edge lines are noted, however;
     * this also helps to determine if the formula is correct.
     * TODO: Determine if this feels right in practice.  May make the proximity/radius configurable, but need to verify the feel at least somewhat regardless
     * @param xOffset The x offset within a 128x64 area; 0 is the leftmost pixel
     * @param yOffset The y offset within a 128x64 area; 0 is the topmost pixel
     * @param riverX The x coordinate of the 128x64 tile; used for logging only
     * @param riverY The y coordinate of the 128x64 tile; used for logging only
     * @return The direction in which a river should be modified, or MapDirection.NONE if no river should
     * be modified.
     */
    public static MapDirection determineRiverDirection(int xOffset, int yOffset, int riverX, int riverY) {
        logger.info("mapDir xOffset: " + xOffset);
        logger.info("mapDir yOffset: " + yOffset);
        logger.info("riverX, riverY: " + riverX + ", " + riverY);
        
        //handling for river
        final int riverProximityMax = Main.settings.riverProximityMax;
        final int riverCornerRadius = Main.settings.riverCornerRadius; //use half as severely with y due to more gradual slope
        if (Math.abs(2*yOffset + (xOffset - 64)) < riverProximityMax && yOffset > riverCornerRadius >>> 1 && xOffset > riverCornerRadius) {
            //Line extends from (0, 32) to (64, 0)
            //if (logger.is())
                logger.warn("NW river toggle on " + riverX + ", " + riverY);
            return MapDirection.NORTHWEST;
        }
        else if (Math.abs(-2*yOffset + (xOffset - 64)) < riverProximityMax && xOffset < (128 - riverCornerRadius) && yOffset > riverCornerRadius >>> 1) { //NE river
            //Line extends from (64, 0) to (128, 32)
             //if (logger.is())
                logger.warn("NE river toggle on " + riverX + ", " + riverY);
            return MapDirection.NORTHEAST;
        }
        else if (Math.abs(-2*(yOffset - 32) + xOffset) < riverProximityMax && xOffset > riverCornerRadius && yOffset < (64 - riverCornerRadius)) {
            //Line extends from (0, 32) to (64, 64).
             //if (logger.is())
                logger.warn("SW river toggle on " + riverX + ", " + riverY);
            return MapDirection.SOUTHWEST;
        }
        else if (Math.abs(2*(yOffset - 64) + (xOffset - 64)) < riverProximityMax && xOffset < (128 - riverCornerRadius) && yOffset < (64 - riverCornerRadius)) {
            //Line extends from (64, 64) to (128, 32) 
            //if (logger.is())
                logger.warn("SE river toggle on " + riverX + ", " + riverY);
            return MapDirection.SOUTHEAST;
        }
        return MapDirection.NONE;
    }
    
    
    public static void recalculateFileAndIndex(IO biq, int x, int y) {
        recalculateFileAndIndex(biq, x, y, false);
    }
    
    /**
     * Recalculates the graphics files used on a tile, based on the tile's location
     * and neighbors.
     * @param biq The BIQ file to be used.
     * @param x X coordiante of the tile being recalculated
     * @param y Y coordinate of the tile being recalculated
     * @param suppressErrors If true, does not display an alert should an error occur.
     * Generally we want to know about errors, but you may wish to set this to true if
     * merging maps with incongruent terrain at the seams.
     */
    public static void recalculateFileAndIndex(IO biq, int x, int y, boolean suppressErrors)
    {
        int southID = biq.calculateTileIndex(x, y);
        TILE south = (southID == -1) ? null : biq.tile.get(southID);
        int westID = biq.calculateTileIndex(x - 1, y - 1);
        TILE west = (westID == -1) ? null : biq.tile.get(westID);
        int northID = biq.calculateTileIndex(x, y - 2);
        TILE north = (northID == -1) ? null : biq.tile.get(northID);
        int eastID = biq.calculateTileIndex(x + 1, y - 1);
        TILE east = (eastID == -1) ? null : biq.tile.get(eastID);

        //next three are the 'abc' in xabc.pcx
        byte terr1 = 0;
        byte terr2 = 0;
        byte terr3 = 0;

        boolean needToCalculateImage = true;

        if (south == null)
        {
            south = new TILE(biq.version);
            south.setBaseTerrain(TERR.COAST);
            south.setRealTerrain(TERR.COAST);
            south.setUpNibbles();
        }
        if (east == null)
        {
            east = new TILE(biq.version);
            east.setBaseTerrain(TERR.COAST);
            east.setRealTerrain(TERR.COAST);
            east.setUpNibbles();
        }
        if (north == null)
        {
            north = new TILE(biq.version);
            north.setBaseTerrain(TERR.COAST);
            north.setRealTerrain(TERR.COAST);
            north.setUpNibbles();
        }
        if (west == null)
        {
            west = new TILE(biq.version);
            west.setBaseTerrain(TERR.COAST);
            west.setRealTerrain(TERR.COAST);
            west.setUpNibbles();
        }

        //Let's do the ocean/sea cases first
        if (south.getBaseTerrain() == TERR.OCEAN && east.getBaseTerrain() == TERR.OCEAN && west.getBaseTerrain() == TERR.OCEAN && north.getBaseTerrain() == TERR.OCEAN)
        {
            if (logger.isDebugEnabled())
                logger.debug("WOOO");
            south.setFile(TILE.WOOO);
            south.setImage((byte)0);
            needToCalculateImage = false;
        }
        else if(south.getBaseTerrain() == TERR.SEA && east.getBaseTerrain() == TERR.SEA && west.getBaseTerrain() == TERR.SEA && north.getBaseTerrain() == TERR.SEA)
        {
            if (logger.isDebugEnabled())
                logger.debug("WSSS");
            south.setFile(TILE.WSSS);
            south.setImage((byte)0);
            needToCalculateImage = false;
        }
        else if (south.getBaseTerrain() == TERR.TUNDRA || east.getBaseTerrain() == TERR.TUNDRA || west.getBaseTerrain() == TERR.TUNDRA || north.getBaseTerrain() == TERR.TUNDRA)
        {
            if (logger.isDebugEnabled())
                logger.debug("XTGC");
            south.setFile(TILE.XTGC);
            terr1 = TERR.TUNDRA;
            terr2 = TERR.GRASSLAND;
            terr3 = TERR.COAST;
        }
        else if (south.getBaseTerrain() == TERR.SEA || east.getBaseTerrain() == TERR.SEA || west.getBaseTerrain() == TERR.SEA || north.getBaseTerrain() == TERR.SEA)
        {
            if (logger.isDebugEnabled())
                logger.debug("WCSO");
            south.setFile(TILE.WCSO);
            terr1 = TERR.COAST;
            terr2 = TERR.SEA;
            terr3 = TERR.OCEAN;
        }//at this point we should have all sea/ocean/tundra covered
        else if (south.getBaseTerrain() != TERR.COAST && east.getBaseTerrain() != TERR.COAST && west.getBaseTerrain() != TERR.COAST && north.getBaseTerrain() != TERR.COAST)
        {
            if (logger.isDebugEnabled())
                logger.debug("XDGP");
            south.setFile(TILE.XDGP);
            terr1 = TERR.DESERT;
            terr2 = TERR.GRASSLAND;
            terr3 = TERR.PLAINS;
        }   //all other cases should have coast
        else
        {
            if (south.getBaseTerrain() == TERR.DESERT || east.getBaseTerrain() == TERR.DESERT || west.getBaseTerrain() == TERR.DESERT || north.getBaseTerrain() == TERR.DESERT)
            {
                if (south.getBaseTerrain() == TERR.PLAINS || east.getBaseTerrain() == TERR.PLAINS || west.getBaseTerrain() == TERR.PLAINS || north.getBaseTerrain() == TERR.PLAINS)
                {
                    if (logger.isDebugEnabled())
                        logger.debug("XDPC");
                    south.setFile(TILE.XDPC);
                    terr1 = TERR.DESERT;
                    terr2 = TERR.PLAINS;
                    terr3 = TERR.COAST;
                }
                else if (south.getBaseTerrain() == TERR.GRASSLAND || east.getBaseTerrain() == TERR.GRASSLAND || west.getBaseTerrain() == TERR.GRASSLAND || north.getBaseTerrain() == TERR.GRASSLAND)
                {
                    if (logger.isDebugEnabled())
                        logger.debug("XDGC");
                    south.setFile(TILE.XDGC);
                    terr1 = TERR.DESERT;
                    terr2 = TERR.GRASSLAND;
                    terr3 = TERR.COAST;
                }
                else if (south.getBaseTerrain() == TERR.COAST || east.getBaseTerrain() == TERR.COAST || west.getBaseTerrain() == TERR.COAST || north.getBaseTerrain() == TERR.COAST)
                {
                    if (logger.isDebugEnabled())
                        logger.debug("XDPC");
                    south.setFile(TILE.XDGC);
                    terr1 = TERR.DESERT;
                    terr2 = TERR.PLAINS;
                    terr3 = TERR.COAST;
                }
                else
                {
                    logger.error("XKCD1: No valid graphics for this tile.  X: " + x + ", Y: " + y + ", North: " + north.getBaseTerrain() + ", East: " + east.getBaseTerrain() + ", South: " + south.getBaseTerrain() + ", West: " + west.getBaseTerrain());
                    if (!suppressErrors) {
                        JOptionPane.showMessageDialog(null, "Error XKCD, type 1 - error calculating terrain image file.  Please report.", "Error XKCD", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            else if (south.getBaseTerrain() == TERR.PLAINS || east.getBaseTerrain() == TERR.PLAINS || west.getBaseTerrain() == TERR.PLAINS || north.getBaseTerrain() == TERR.PLAINS)
            {
                if (logger.isDebugEnabled())
                    logger.debug("XPGC");
                south.setFile(TILE.XPGC);
                terr1 = TERR.PLAINS;
                terr2 = TERR.GRASSLAND;
                terr3 = TERR.COAST;
            }
            else if (south.getBaseTerrain() == TERR.GRASSLAND || east.getBaseTerrain() == TERR.GRASSLAND || west.getBaseTerrain() == TERR.GRASSLAND || north.getBaseTerrain() == TERR.GRASSLAND)
            {
                if (logger.isDebugEnabled())
                    logger.debug("XGGC");
                south.setFile(TILE.XGGC);
                terr1 = TERR.GRASSLAND;
                terr2 = TERR.GRASSLAND;
                terr3 = TERR.COAST;
            }   //TODO: Forgot the all coast case, it's XKCD'ing
            else if (south.getBaseTerrain() == TERR.COAST || east.getBaseTerrain() == TERR.COAST || west.getBaseTerrain() == TERR.COAST || north.getBaseTerrain() == TERR.COAST)
            {
                if (logger.isDebugEnabled())
                    logger.debug("WSCO Final");
                south.setFile(TILE.WCSO);
                terr1 = TERR.COAST;
                terr2 = TERR.SEA;
                terr3 = TERR.OCEAN;

            }
            else
            {
                logger.error("XKCD2: No valid graphics for this tile. X: " + x + ", Y: " + y + ", North: " + north.getBaseTerrain() + ", East: " + east.getBaseTerrain() + ", South: " + south.getBaseTerrain() + ", West: " + west.getBaseTerrain());
                if (!suppressErrors) {
                    JOptionPane.showMessageDialog(null, "Error XKCD, type 2 - error calculating terrain image file.  Please report.", "Error XKCD", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        if (needToCalculateImage)
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("Calculating image");
                logger.debug("north real: " + north.getBaseTerrain());
                logger.debug("west real: " + west.getBaseTerrain());
                logger.debug("east real: " + east.getBaseTerrain());
                logger.debug("south real: " + south.getBaseTerrain());
            }
            byte sum = 0;
            if (north.getBaseTerrain() == terr2)
                sum+=1;
            if (north.getBaseTerrain() == terr3)
                sum+=2;
            if (west.getBaseTerrain() == terr2)
                sum+=3;
            if (west.getBaseTerrain() == terr3)
                sum+=6;
            if (east.getBaseTerrain() == terr2)
                sum+=9;
            if (east.getBaseTerrain() == terr3)
                sum+=18;
            if (south.getBaseTerrain() == terr2)
                sum+=27;
            if (south.getBaseTerrain() == terr3)
                sum+=54;
//            logger.info("north.real: " + north.getBaseTerrain());
//            logger.info("west.real: " + west.getBaseTerrain());
//            logger.info("east.real: " + east.getBaseTerrain());
//            logger.info("south.real: " + south.getBaseTerrain());
//            logger.info("north.base: " + north.getRealTerrain());
//            logger.info("west.base: " + west.getRealTerrain());
//            logger.info("east.base: " + east.getRealTerrain());
//            logger.info("south.base: " + south.getRealTerrain());
//            logger.info("Sum: " + sum);
            south.setImage(sum);
        }
    }
    
    
    public static boolean validChangeOnTiles(IO biq, int one, int two, int three, int four)
    {
        TILE tileOne = (one == -1) ? null : biq.tile.get(one);
        TILE tileTwo = (two == -1) ? null : biq.tile.get(two);
        TILE tileThree = (three == -1) ? null : biq.tile.get(three);
        TILE tileFour = (four == -1) ? null : biq.tile.get(four);
        TILE[]tiles = {tileOne, tileTwo, tileThree, tileFour};

        //The sea/ocean case - if one is sea/ocean, all most be water
        boolean seaOrOcean = false;
        for (TILE t : tiles)
        {
            if (t != null && t.getBaseTerrain() >= TERR.SEA)
            {
                seaOrOcean = true;
                break;
            }
        }
        if (seaOrOcean)
        {
            for (TILE t : tiles)
            {
                if (t != null && t.getBaseTerrain() < TERR.COAST)
                {
                    return false;
                }
            }
            return true;
        }
        //Now just the other cases
        //The tundra case - can only have grassland/coast/tundra around it
        boolean tundra = false;
        for (TILE t : tiles)
        {
            if (t != null && t.getBaseTerrain() == TERR.TUNDRA)
            {
                tundra = true;
                break;
            }
        }
        if (tundra)
        {
            for (TILE t : tiles)
            {
                if (t != null && t.getBaseTerrain() != TERR.COAST && t.getBaseTerrain() != TERR.TUNDRA && t.getBaseTerrain() != TERR.GRASSLAND)
                {
                    return false;
                }
            }
            return true;
        }
        //At this point we may have:
        //Grassland
        //Desert
        //Plains
        //Coast
        //Others (hills, mountain, floodplain) aren't basic
        //We can't have all four
        boolean grassland = false;
        boolean desert = false;
        boolean plains = false;
        boolean coast = false;
        for (TILE t : tiles)
        {
            if (t != null && t.getBaseTerrain() == TERR.GRASSLAND)
            {
                grassland = true;
            }
            if (t != null && t.getBaseTerrain() == TERR.DESERT)
            {
                desert = true;
            }
            if (t != null && t.getBaseTerrain() == TERR.PLAINS)
            {
                plains = true;
            }
            if (t != null && t.getBaseTerrain() == TERR.COAST)
            {
                coast = true;
            }
        }
        if (grassland && plains && desert && coast)
            return false;
        return true;
    }
}
