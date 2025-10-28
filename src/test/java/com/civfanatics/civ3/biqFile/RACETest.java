/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.biqFile;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Andrew
 */
public class RACETest {
    
    private RACE race;
    
    @Before
    public void setUp() {
        race = new RACE(null);
    }
    
    @Test
    public void DefaultLengthIs2388() {
        assertEquals(2388, race.getDataLength());
    }
    
    @Test
    public void AddingCityNameIncreasesDataLengthBy24() {
        race.addCityName("Rome");
        assertEquals(2412, race.getDataLength());
    }
    
    @Test public void RemovingCityNameDecreasesDataLengthBy24() {
        race.addCityName("Rome");
        race.removeCityName(0);
        assertEquals(2388, race.getDataLength());
    }
}
