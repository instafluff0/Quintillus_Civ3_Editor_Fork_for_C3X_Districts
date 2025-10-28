
package com.civfanatics.civ3.biqFile;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Andrew
 */
public class TILETest {

    @Test
    public void testRiverConnectionInfo() {
        IO io = new IO();
        TILE tile = new TILE(io);
        tile.riverConnectionInfo = (byte)255;
        
        assertTrue(tile.hasRiverConnection(TILE.RIVER_NORTHEAST));
        assertTrue(tile.hasRiverConnection(TILE.RIVER_SOUTHEAST));
        assertTrue(tile.hasRiverConnection(TILE.RIVER_SOUTHWEST));
        assertTrue(tile.hasRiverConnection(TILE.RIVER_NORTHWEST));
    }
    
    @Test
    public void testRiverConnectionInfoWhenAllFalse() {
        IO io = new IO();
        TILE tile = new TILE(io);
        tile.riverConnectionInfo = (byte)0;
        
        assertFalse(tile.hasRiverConnection(TILE.RIVER_NORTHEAST));
        assertFalse(tile.hasRiverConnection(TILE.RIVER_SOUTHEAST));
        assertFalse(tile.hasRiverConnection(TILE.RIVER_SOUTHWEST));
        assertFalse(tile.hasRiverConnection(TILE.RIVER_NORTHWEST));
    }
}
