/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.civfanatics.civ3.xplatformeditor.tabs.map;

import com.civfanatics.civ3.biqFile.TILE;

/**
 * This class represents a brush with which you can paint a Civ map.
 *
 *
 * @author Andrew
 */
public class Brush {

    public static final int MODE_SELECT = 0;
    public static final int MODE_TERRAIN = 1;
    public static final int MODE_UNIT = 2;
    public static final int MODE_OVERLAY = 3;
    public static final int MODE_SETTLEMENT = 4;
    public static final int MODE_RELOCATE = 5;
    public static final int MODE_RELOCATE_CITY_AND_UNITS = 6;
    public static final int MODE_ADD_FOG = 7;
    public static final int MODE_REMOVE_FOG = 8;
    public static final int MODE_DISTRICT = 9;
    



    public int mode;
    public int terrainType;
    public boolean isLandmark;  //for use with terrains
    public boolean isBonusGrassland;
    public boolean isSnowCapped;
    public boolean isPine;
    public boolean isDeepwaterHarbour;
    public OverlayType overlayType;
    public int diameter;
    
    public TILE tileBeingRelocated = null;
    //perhaps an option to 'spread' like a paint bucket?

    /**
     * The constructor.  Defaults mode to MODE_SELECT, terrainType to 0, radius to 1.
     */
    public Brush()
    {
        mode = 0;
        terrainType = 0;
        diameter = 1;
    }
}
