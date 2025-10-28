/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.biqFile;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Andrew
 */
public class BLDGTest {
    
    @Test
    public void initialDataLengthIs268() {
        IO biq = new IO();
        BLDG bldg = new BLDG(biq);
        assertEquals("Building data length does not default to 268", 268, bldg.getDataLength());
    }
}
