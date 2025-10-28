
package com.civfanatics.civ3.biqFile.util;

/**
 * Represents a direction on a map.  Initially used for river placement, and thus
 * is NW/NE/etc.; may eventually also be used for cardinal directions.  Intended
 * as a general-purpose direction class, not specifically for rivers.
 * Includes the NONE option because an event may not correspond with a particular
 * direction (e.g. a click is not clearly in any one direction, and thus no
 * action should be taken).
 * @author Andrew
 */
public enum MapDirection {
    NONE,
    NORTHWEST,
    NORTHEAST,
    SOUTHEAST,
    SOUTHWEST,
    NORTH,
    EAST,
    SOUTH,
    WEST,
}
