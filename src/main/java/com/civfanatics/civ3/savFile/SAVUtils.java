
package com.civfanatics.civ3.savFile;

/**
 *
 * @author Andrew
 */
public class SAVUtils {
    
    /**
     * Returns whether the bitfield indicates a player's value is true for this bitfield.
     * The bitfield must be indexed by player, and some caution should be taken to
     * verify that player 0 being true has a value of "2", player 1 of "4", etc.  This
     * seems to be the way things are done, but only a cursory check on one field has
     * been done, not a thorough check of many players/scenarios/bitfields.  In other
     * words, when you use this on a new bitfield, make sure it's behaving as you expect!
     * 
     * N.B. For some reason player 31 wraps around 0.  This is the barbarians IIRC, so that
     * should be fine.  But I'm still a bit puzzled why Firaxis programmed it this way.
     * @param bitfield The bitfield being evaluated.
     * @param playerIndex The index of the player.  Note it is <b>player</b> index, not civ index!
     * @return true if the player's position in this bitfield has a 1 value; false if a 0 value
     */
    public static boolean evaluatePlayerBitfield(Integer bitfield, int playerIndex) {
        int one = 2;
        one = one << playerIndex;
        return (bitfield & one) != 0;   //for leftmost bit, will be < 0 when true
    }
}
