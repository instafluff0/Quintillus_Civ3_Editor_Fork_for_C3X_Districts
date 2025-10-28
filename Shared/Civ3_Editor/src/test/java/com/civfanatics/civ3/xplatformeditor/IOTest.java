/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.civfanatics.civ3.xplatformeditor;

import com.civfanatics.civ3.biqFile.*;
import com.civfanatics.civ3.biqFile.util.DefaultRulesLoader;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.FileNotFoundException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

/**
 *
 * @author Andrew
 */
public class IOTest {

    static IO io;

    public IOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        //Intentionally setting an oddball number of processors to maximize the
        //chance of uncovering bugs due to less-than-perfect multithreading.
        IO.setNumProcs(5);
        DefaultRulesLoader.defaultRulesPath = "src/test/java/com/civfanatics/civ3/xplatformeditor/Custom All.biq";
    }
    
    @Before
    public void setUp() throws Exception {
        
        io = new IO();
        io.resetLogConfig();
        Logger.getRootLogger().setLevel(Level.WARN);
    }

    @After
    public void tearDown() throws Exception {
        io = null;
    }

    @Test
    public void testSaveAndReopen() throws FileNotFoundException {
        File original = new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Custom All.biq");
        boolean result = io.inputBIQ(original);
        assertTrue(result);
        File newFile = new File("Export Custom All.biq");
        boolean output = io.outputBIQ(newFile);
        assertTrue(output);
        //make sure it's the same size
        long originalLength = original.length();
        long newLength = newFile.length();
        assertEquals(originalLength, newLength);
        io = new IO();
        File toReopen = new File("Export Custom All.biq");
        boolean reopen = io.inputBIQ(toReopen);
        assertTrue(reopen);
        if (toReopen.exists())
            toReopen.delete();
    }

    @Test
    public void testAddBuildingAndReopen() throws FileNotFoundException {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Custom All.biq"));
        assertTrue(result);
        io.buildings.add(new BLDG("Name", io.flavor.size(), io));
        boolean output = io.outputBIQ(new File("Export Custom All.biq"));
        assertTrue(output);
        io = new IO();
        File toReopen = new File("Export Custom All.biq");
        boolean reopen = io.inputBIQ(toReopen);
        assertTrue(reopen);
        if (toReopen.exists())
            toReopen.delete();
    }

    @Test
    public void testAddAndRemoveBuilding() throws FileNotFoundException {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Custom All.biq"));
        assertTrue(result);
        io.buildings.add(new BLDG("Name", io.flavor.size(), io));
        io.buildings.remove(io.buildings.size() - 1);
        boolean output = io.outputBIQ(new File("Export Custom All.biq"));
        assertTrue(output);
        io = new IO();
        File toReopen = new File("Export Custom All.biq");
        boolean reopen = io.inputBIQ(toReopen);
        assertTrue(reopen);
        if (toReopen.exists())
            toReopen.delete();
    }

    @Test
    public void testAddCivAndReopen() throws FileNotFoundException {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Custom All.biq"));
        assertTrue(result);
        io.civilization.add(new RACE("NewCiv", io.flavor.size(), io));
        boolean output = io.outputBIQ(new File("Export Custom All.biq"));
        assertTrue(output);
        io = new IO();
        File toReopen = new File("Export Custom All.biq");
        boolean reopen = io.inputBIQ(toReopen);
        assertTrue(reopen);
        if (toReopen.exists())
            toReopen.delete();
    }

    @Test
    public void testAddAndRemoveCivilization() throws FileNotFoundException {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Custom All.biq"));
        assertTrue(result);
        io.civilization.add(new RACE("NewCiv", io.flavor.size(), io));
        io.civilization.remove(io.civilization.size() - 1);
        boolean output = io.outputBIQ(new File("Export Custom All.biq"));
        assertTrue(output);
        io = new IO();
        File toReopen = new File("Export Custom All.biq");
        boolean reopen = io.inputBIQ(toReopen);
        assertTrue(reopen);
        if (toReopen.exists())
            toReopen.delete();
    }

    @Test
    public void testAddCitizenAndReopen() throws FileNotFoundException {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Custom All.biq"));
        assertTrue(result);
        io.citizens.add(new CTZN("NewCitizen", io));
        boolean output = io.outputBIQ(new File("Export Custom All.biq"));
        assertTrue(output);
        io = new IO();
        File toReopen = new File("Export Custom All.biq");
        boolean reopen = io.inputBIQ(toReopen);
        assertTrue(reopen);
        if (toReopen.exists())
            toReopen.delete();
    }

    @Test
    public void testAndRemoveCitizen() throws FileNotFoundException {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Custom All.biq"));
        assertTrue(result);
        io.citizens.add(new CTZN("NewCitizen", io));
        io.citizens.remove(io.citizens.size() - 1);
        boolean output = io.outputBIQ(new File("Export Custom All.biq"));
        assertTrue(output);
        io = new IO();
        File toReopen = new File("Export Custom All.biq");
        boolean reopen = io.inputBIQ(toReopen);
        assertTrue(reopen);
        if (toReopen.exists())
            toReopen.delete();
    }

    @Test
    public void testAddCultAndReopen() throws FileNotFoundException {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Custom All.biq"));
        assertTrue(result);
        io.culture.add(new CULT("NewCulture", io));
        boolean output = io.outputBIQ(new File("Export Custom All.biq"));
        assertTrue(output);
        io = new IO();
        File toReopen = new File("Export Custom All.biq");
        boolean reopen = io.inputBIQ(toReopen);
        assertTrue(reopen);
        if (toReopen.exists())
            toReopen.delete();
    }

    @Test
    public void testAddAndRemoveCulture() throws FileNotFoundException {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Custom All.biq"));
        assertTrue(result);
        io.culture.add(new CULT("NewCulture", io));
        io.culture.remove(io.culture.size() - 1);
        boolean output = io.outputBIQ(new File("Export Custom All.biq"));
        assertTrue(output);
        io = new IO();
        File toReopen = new File("Export Custom All.biq");
        boolean reopen = io.inputBIQ(toReopen);
        assertTrue(reopen);
        if (toReopen.exists())
            toReopen.delete();
    }

    @Test
    public void testAddDiffAndReopen()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Custom All.biq"));
        assertTrue(result);
        io.difficulties.add(new DIFF("NewDifficulty", io));
        boolean output = io.outputBIQ(new File("Export Custom All.biq"));
        assertTrue(output);
        io = new IO();
        File toReopen = new File("Export Custom All.biq");
        boolean reopen = io.inputBIQ(toReopen);
        assertTrue(reopen);
        if (toReopen.exists())
            toReopen.delete();
    }
    
    @Test
    public void testAddAndRemoveDiff()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Custom All.biq"));
        assertTrue(result);
        io.difficulties.add(new DIFF("NewDifficulty", io));
        io.difficulties.remove(io.difficulties.size() - 1);
        boolean output = io.outputBIQ(new File("Export Custom All.biq"));
        assertTrue(output);
        io = new IO();
        File toReopen = new File("Export Custom All.biq");
        boolean reopen = io.inputBIQ(toReopen);
        assertTrue(reopen);
        if (toReopen.exists())
            toReopen.delete();
    }

    @Test
    public void testAddGoodAndReopen()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Custom All.biq"));
        assertTrue(result);
        io.resource.add(new GOOD("NewGood", io));
        boolean output = io.outputBIQ(new File("Export Custom All.biq"));
        assertTrue(output);
        io = new IO();
        File toReopen = new File("Export Custom All.biq");
        boolean reopen = io.inputBIQ(toReopen);
        assertTrue(reopen);
        if (toReopen.exists())
            toReopen.delete();
    }

    @Test
    public void testAddAndRemoveGood()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Custom All.biq"));
        assertTrue(result);
        io.resource.add(new GOOD("NewGood", io));
        io.resource.remove(io.resource.size() - 1);
        boolean output = io.outputBIQ(new File("Export Custom All.biq"));
        assertTrue(output);
        io = new IO();
        File toReopen = new File("Export Custom All.biq");
        boolean reopen = io.inputBIQ(toReopen);
        assertTrue(reopen);
        if (toReopen.exists())
            toReopen.delete();
    }

    @Test
    public void testAddGovtAndReopen()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Custom All.biq"));
        assertTrue(result);
        io.government.add(new GOVT("NewGovt", io.government.size(), io));
        boolean output = io.outputBIQ(new File("Export Custom All.biq"));
        assertTrue(output);
        io = new IO();
        File toReopen = new File("Export Custom All.biq");
        boolean reopen = io.inputBIQ(toReopen);
        assertTrue(reopen);
        if (toReopen.exists())
            toReopen.delete();
    }

    @Test
    public void testAddAndRemoveGOVT()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Custom All.biq"));
        assertTrue(result);
        io.government.add(new GOVT("NewGovt", io.government.size(), io));
        io.government.remove(io.government.size() - 1);
        boolean output = io.outputBIQ(new File("Export Custom All.biq"));
        assertTrue(output);
        io = new IO();
        File toReopen = new File("Export Custom All.biq");
        boolean reopen = io.inputBIQ(toReopen);
        assertTrue(reopen);
        if (toReopen.exists())
            toReopen.delete();
    }

    @Test
    public void testAddTechAndReopen()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Custom All.biq"));
        assertTrue(result);
        io.technology.add(new TECH("NewTech", io.flavor.size(), io));
        boolean output = io.outputBIQ(new File("Export Custom All.biq"));
        assertTrue(output);
        io = new IO();
        File toReopen = new File("Export Custom All.biq");
        boolean reopen = io.inputBIQ(toReopen);
        assertTrue(reopen);
        if (toReopen.exists())
            toReopen.delete();
    }
    @Test
    public void testAddAndDeleteTech()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Custom All.biq"));
        assertTrue(result);
        io.technology.add(new TECH("NewTech", io.flavor.size(), io));
        io.technology.remove(io.technology.size() - 1);
        boolean output = io.outputBIQ(new File("Export Custom All.biq"));
        assertTrue(output);
        io = new IO();
        File toReopen = new File("Export Custom All.biq");
        boolean reopen = io.inputBIQ(toReopen);
        assertTrue(reopen);
        if (toReopen.exists())
            toReopen.delete();
    }

    @Test
    public void testAddUnitAndReopen()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Custom All.biq"));
        assertTrue(result);
        io.unit.add(new PRTO("NewUnit", io, io.unit.size()));
        boolean output = io.outputBIQ(new File("Export Custom All.biq"));
        assertTrue(output);
        io = new IO();
        File toReopen = new File("Export Custom All.biq");
        boolean reopen = io.inputBIQ(toReopen);
        assertTrue(reopen);
        if (toReopen.exists())
            toReopen.delete();
    }
    
    @Test
    public void testAddAndDeleteUnitAndReopen()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Custom All.biq"));
        assertTrue(result);
        io.unit.add(new PRTO("NewUnit", io, io.unit.size()));
        io.unit.remove(io.unit.size() - 1);
        boolean output = io.outputBIQ(new File("Export Custom All.biq"));
        assertTrue(output);
        io = new IO();
        File toReopen = new File("Export Custom All.biq");
        boolean reopen = io.inputBIQ(toReopen);
        assertTrue(reopen);
        if (toReopen.exists())
            toReopen.delete();
    }

    @Test
    //This is somewhat of an integration test - there are many things that must
    //be working for this to work.
    public void testInputBIQ_None() throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Custom None.biq"));
        assertTrue(result);
        testSaveAndOpen();
    }

    @Test
    public void testInputBIQ_RulesOnly()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Custom Rules Only.biq"));
        assertTrue(result);
        testSaveAndOpen();
    }
    @Test
    public void testInputBIQ_MapOnly()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Custom Map Only.biq"));
        assertTrue(result);
        testSaveAndOpen();
    }
    @Test
    public void testInputBIQ_PlayerDataOnly()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Custom Player Data Only.biq"));
        assertTrue(result);
        testSaveAndOpen();
    }
    @Test
    public void testInputBIQ_RulesAndPlayerDataOnly()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Custom Rules and Plyr Data.biq"));
        assertTrue(result);
        testSaveAndOpen();
    }
    @Test
    public void testInputBIQ_RulesAndMap()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Custom Rules and Map.biq"));
        assertTrue(result);
        testSaveAndOpen();
    }
    @Test
    public void testInputBIQ_PlayerDataAndMap()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Custom Map and Plyr Data.biq"));
        assertTrue(result);
        testSaveAndOpen();
    }
    @Test
    public void testInputBIQ_AllCustom()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Custom All.biq"));
        assertTrue(result);
        testSaveAndOpen();
    }
    
    /**
     * This test ensures that version 2.05 Vanilla files can be opened.
     * This map is by Mike Breitkreutz, the author of the Firaxis scenario editor.
     * It's the first map posted on CFC (by Thunderfall!), and has an older
     * version than the Earth maps shipping with the game.  This makes it
     * possibly the oldest available scenario (unless 1.00 discs shipped with
     * an older scenario).
     * After some forum archaeology, this appears to be the version shipped with
     * 1.00.
     */
    @Test
    public void testInputBIC_Vanilla_205()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/RingWorld (Large) v2.bic"));
        assertTrue(result);
        testSaveAndOpen();
    }
    
    /**
     * This test ensures that version 2.05 Vanilla files with custom rules can
     * be opened.
     * This map is by Plutarck, and its thread is at:
     * https://forums.civfanatics.com/threads/long-winded-changes-lwc-mod.9078/
     * This is the original version 1 BIC that was removed after 45 downloads.
     * I don't know where I found it, as it wasn't in 2001 whe it was originally
     * on the forum.
     * @throws FileNotFoundException 
     */
    @Test
    public void testInputBIC_Vanilla_205_With_Custom_Rules()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Long Winded Changes v0.01 (BIC 2.05).bic"));
        assertTrue(result);
        testSaveAndOpen();
    }
    
//    @Test
//    public void testInputAndOutputBIC_Vanilla_205_With_Custom_Rules()  throws FileNotFoundException  {
//        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Long Winded Changes v0.01 (BIC 2.05).bic"));
//        File output = new File("output.biq");
//        output.deleteOnExit();
//        io.outputBIQ(output);
//        IO reinputted = new IO();
//        result = reinputted.inputBIQ(output);
//        assertTrue(result);
//    }
    
    /**
     * This test ensures that version 2.07 Vanilla files can be opened.
     * This is the version of Zach Wilson's Earth map that shipped with Vanilla.
     * In my install, this BIC is dated May 29, 2002, although the later version
     * 2.10 from February 2002 suggests it may have been made earlier, and
     * simply packaged with a 1.07 patched Vanilla CD in May 2002.
     * I have not located any other 2.07 files on the forums.
     */
    @Test
    public void testInputBIC_Vanilla_207()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Earth (Standard).bic"));
        assertTrue(result);
        testSaveAndOpen();
    }
    /**
     * This test ensures that version 2.10 Vanilla files can be opened.
     * This test uses Soufie77's Earth map, a modification of Zach Wilson's.
     * This map was posted on CFC on February 11, 2002, at http://forums.civfanatics.com/showthread.php?t=11114.
     * This appears to correlate with the 1.16f patch on December 7, 2001.
     * 1.17f appears to use this version, too, though I'm not 100% on that.
     * TE Turkhan's 12/12/2001 map is this version, and was just after the patch.
     */
    @Test
    public void testInputBIC_Vanilla_210()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Earth (Standard) v5.1.bic"));
        assertTrue(result);
        testSaveAndOpen();
    }
    /**
     * This test ensures that version 3.08 Vanilla files can be opened.
     * This test uses Gamelord's Euro map.
     * This map was posted on CFC on June 12, 2002, at http://forums.civfanatics.com/showthread.php?t=24679
     * This appears to correlate with the 1.21f patch, on April 18, 2002.
     */
    @Test
    public void testInputBIC_Vanilla_308()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Euro.bic"));
        assertTrue(result);
        testSaveAndOpen();
    }
    
    /**
     * This test ensures that version 4.01 Vanilla files can be opened.
     * This BIC is dated October 29, 2002
     * See http://forums.civfanatics.com/showthread.php?t=9829 for Marla Singer's thread.
     * This is adopted for Civ Vanilla 1.27f
     */
    @Test
    public void testInputBIC_Vanilla_401()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Marla Singer's World Map 1.29f.bic"));
        assertTrue(result);
        testSaveAndOpen();
    }
    
    /**
     * This test ensures that version 11.18 PTW files can be opened.
     * This BIX is dated June 22, 2003.  It is version 1.93 of TETurkhan's scenario.
     * See https://forums.civfanatics.com/threads/teturkhan-test-of-time-map-mod.29279/ for TETurkhan's thread.
     * TETurkhan's version 1.66 scenario, dated November 11, 2002, uses the same BIX format.
     * @throws FileNotFoundException 
     */
    @Test
    public void testInputBIX_PTW_1118()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/TETurkhan Test of Time (World Map & Cities).bix"));
        assertTrue(result);
        testSaveAndOpen();
    }
    
    @Test
    public void testRhyes()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Rhye's of Civilization Expanded v1.23.biq"));
        assertTrue(result);
        testSaveAndOpen();
    }
    @Test
    public void testTheGreatWar()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/The Great War 2.1.biq"));
        assertTrue(result);
        testSaveAndOpen();
    }
    @Test
    public void testMEMEuropeSmall()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/MEM Europe (Small).biq"));
        assertTrue(result);
        testSaveAndOpen();
    }
    @Test
    public void testRFRE275Hard()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/RFRE 275BC Hard.biq"));
        assertTrue(result);
        testSaveAndOpen();
    }
    @Test
    public void testAOI41()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/AoI4.1-31civs-FULL.biq"));
        assertTrue(result);
        testSaveAndOpen();
    }
    @Test
    public void testTwilight()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Twillight of Byzantium.biq"));
        assertTrue(result);
        testSaveAndOpen();
    }
    @Test(timeout = 1000)
    public void testTCW()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/TCW1.6.biq"));
        assertTrue(result);
        testSaveAndOpen();
    }
    @Test
    public void testRood()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Rood.biq"));
        assertTrue(result);
        testSaveAndOpen();
    }
    @Test
    public void testManhattan()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Manhattan.biq"));
        assertTrue(result);
        testSaveAndOpen();
    }
    @Test
    public void testMesoamerica()  throws FileNotFoundException  {
        assertTrue(io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/5 Mesoamerica.biq")));
        testSaveAndOpen();
    }
    
    @Test
    public void testFewerThanTenSpaceshipParts() throws FileNotFoundException {
        assertTrue(io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/7 MP Sengoku - Sword of the Shogun.biq")));
        testSaveAndOpen();
    }
    
    @Test
    public void testFewerThanSixCultureLevels() throws FileNotFoundException {
        assertTrue(io.inputBIQ(new File("src/test/java/com/civfanatics/civ3/xplatformeditor/Five Culture Levels.biq")));
        testSaveAndOpen();
    }

    private void testSaveAndOpen() throws FileNotFoundException {
        File output = new File("output.biq");
        output.deleteOnExit();
        io.outputBIQ(output);
        IO reinputted = new IO();
        assertTrue(reinputted.inputBIQ(output));
    }
    
    @Ignore
    @Test
    public void testBadInput()  throws FileNotFoundException  {
        boolean result = io.inputBIQ(new File("D:\\Civilization III\\Conquests\\Scenarios\\All Zeroes.biq"));
        //Note that this test should fail, but not error out.
        assertFalse(result);
    }

}