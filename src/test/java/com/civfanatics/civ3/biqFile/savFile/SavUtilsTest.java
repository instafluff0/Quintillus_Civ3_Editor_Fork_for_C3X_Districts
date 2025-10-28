
package com.civfanatics.civ3.biqFile.savFile;

import com.civfanatics.civ3.savFile.SAVUtils;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Andrew
 */
public class SavUtilsTest {
    
    @Test
    public void evaluatePlayerBitfield_worksForPlayer0_True() {
        assertTrue(SAVUtils.evaluatePlayerBitfield(0x2, 0));
    }
    
    @Test
    public void evaluatePlayerBitfield_worksForPlayer0_False() {
        assertFalse(SAVUtils.evaluatePlayerBitfield(0, 0));
    }
    
    @Test
    public void evaluatePlayerBitfield_worksForPlayer1_True() {
        assertTrue(SAVUtils.evaluatePlayerBitfield(0x4, 1));
    }
    
    @Test
    public void evaluatePlayerBitfield_worksForPlayer1_False() {
        assertFalse(SAVUtils.evaluatePlayerBitfield(0, 1));
    }
    
    @Test
    public void evaluatePlayerBitfield_worksForPlayer1_and_0_True() {
        assertTrue(SAVUtils.evaluatePlayerBitfield(0x6, 0));
        assertTrue(SAVUtils.evaluatePlayerBitfield(0x6, 1));
    }
    
    @Test
    public void evaluatePlayerBitfield_worksForPlayer29() {
        assertTrue(SAVUtils.evaluatePlayerBitfield(0x40000000, 29));
        assertFalse(SAVUtils.evaluatePlayerBitfield(0, 29));
    }
    
    @Test
    public void evaluatePlayerBitfield_worksForPlayer30() {
        assertTrue(SAVUtils.evaluatePlayerBitfield(0x80000000, 30));
        assertFalse(SAVUtils.evaluatePlayerBitfield(0, 30));
    }
}
