/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.civfanatics.civ3.biqFile;

import static org.junit.Assert.assertNotSame;
import org.junit.Test;

/**
 *
 * @author Andrew
 */
public class UNITTest {
    
    @Test
    public void testCloneCreatesDeepCopy() {
        UNIT unit = new UNIT(null);
        UNIT otherUnit = unit.clone();
        assertNotSame(unit, otherUnit);
    }
}
