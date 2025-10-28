
package com.civfanatics.civ3.biqFile;

import java.util.ArrayList;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Quintillus
 */
public class PRTOTest {
    
    IO biq;
    PRTO prto;
    
    @Before
    public void setup() {
        biq = new IO();
        biq.unit = new ArrayList<PRTO>();
        biq.version = civ3Version.CONQUESTS;
        prto = new PRTO(biq, 0);
    }
    
    @After
    public void tearDown() {
        biq = null;
        prto = null;
    }
    
    @Test
    public void testJoinCityFunctionality() {
        prto.setJoinCity(false);
        assertFalse(prto.getJoinCity());
        prto.setJoinCity(true);
        assertTrue(prto.getJoinCity());
        //Test false one more time in case the first assert succeeded due to defaults
        prto.setJoinCity(false);
        assertFalse(prto.getJoinCity());
    }
    
    @Test
    public void testDeleteUpgradeToUnit() {
        prto.setUpgradeTo(42);
        prto.handleDeletedUnit(42);
        assertEquals("Unit should no longer upgrade", -1, prto.getUpgradeTo());
    }
    
    @Test
    public void testDeleteUpgradeToUnitWhenLowerIndexDeleted() {
        prto.setUpgradeTo(42);
        prto.handleDeletedUnit(27);
        assertEquals("Unit upgrade to should be lower", 41, prto.getUpgradeTo());
    }
    @Test
    public void testDeleteUpgradeToUnitWhenHigherIndexDeleted() {
        prto.setUpgradeTo(42);
        prto.handleDeletedUnit(73);
        assertEquals("Unit upgrade to should be the same", 42, prto.getUpgradeTo());
    }
    
    @Test
    public void testDeleteEnslaveUnit() {
        prto.setEnslaveResultsIn(42);
        prto.handleDeletedUnit(42);
        assertEquals("Enslave results in should no longer be set", -1, prto.getEnslaveResultsIn());
    }
    
    @Test
    public void testDeleteEnslaveUnitWhenLowerIndexDeleted() {
        prto.setEnslaveResultsIn(42);
        prto.handleDeletedUnit(27);
        assertEquals("Enslave results in should be lower", 41, prto.getEnslaveResultsIn());
    }
    @Test
    public void testDeleteEnslaveUnitWhenHigherIndexDeleted() {
        prto.setEnslaveResultsIn(42);
        prto.handleDeletedUnit(73);
        assertEquals("Enslave results in should be the same", 42, prto.getEnslaveResultsIn());
    }
    
    @Test
    public void infiniteUpgradeLoopIsFalseWhenNoUpgrade() {
        biq.unit.add(prto);
        prto.setUpgradeTo(-1);
        assertFalse(prto.hasInfiniteUpgradePath());
    }
    
    @Test
    public void infiniteUpgradeLoopIsFalseWhenLimitedUpgrade() {
        biq.unit.add(prto);
        PRTO upgrade = new PRTO(biq, 1);
        biq.unit.add(upgrade);
        prto.setUpgradeTo(1);
        assertFalse(prto.hasInfiniteUpgradePath());
    }
    
    @Test
    public void infiniteUpgradeLoopIsTrueWhenInfiniteUpgrade() {
        biq.unit.add(prto);
        PRTO upgrade = new PRTO(biq, 1);
        biq.unit.add(upgrade);
        prto.setUpgradeTo(1);
        PRTO betterUpgrade = new PRTO(biq, 2);
        biq.unit.add(betterUpgrade);
        upgrade.setUpgradeTo(2);
        betterUpgrade.setUpgradeTo(0);
        assertTrue(prto.hasInfiniteUpgradePath());
        
    }
    
    /**
     * Verifies both that when unit A upgrades to unit B, and unit B has an infinite
     * loop, that unit A returns true for infinite upgrade loop; AND that this
     * scenario does not cause an infinite loop itself.  Even though unit A may
     * not be part of the loop, if it comes earlier in the unit list, we would
     * encounter this first.  And initially this did cause an infinite loop in
     * the infinite-loop-checking code, so we should check for this.
     * 
     * Timeout of 1 second because this should be very quick, but if it has an
     * infinite loop it will not be quick.
     */
    @Test(timeout=1000)
    public void infiniteUpgradeLoopDoesntLoopForeverWhenAUnitUpgadesToAnInfiniteLoop() {
        biq.unit.add(prto);
        PRTO upgrade = new PRTO(biq, 1);
        biq.unit.add(upgrade);
        prto.setUpgradeTo(1);
        PRTO betterUpgrade = new PRTO(biq, 2);
        biq.unit.add(betterUpgrade);
        upgrade.setUpgradeTo(2);
        betterUpgrade.setUpgradeTo(0);
        PRTO unitThatUpgradesToALoop = new PRTO(biq, 3);
        unitThatUpgradesToALoop.setUpgradeTo(0);
        assertTrue(unitThatUpgradesToALoop.hasInfiniteUpgradePath());
        
    }
    
    @Test
    public void upgradePathIsNoneIfNoUpgradePath() {
        biq.unit.add(prto);
        prto.setUpgradeTo(-1);
        prto.setName("Warrior");
        assertEquals("None", prto.getUpgradePath());
    }
    
    @Test
    public void upgradePathIsCorrectInNormalCase() {
        biq.unit.add(prto);
        PRTO upgrade = new PRTO(biq, 1);
        biq.unit.add(upgrade);
        prto.setUpgradeTo(1);
        prto.setName("Warrior");
        upgrade.setName("Swordsman");
        PRTO betterUpgrade = new PRTO(biq, 2);
        biq.unit.add(betterUpgrade);
        upgrade.setUpgradeTo(2);
        betterUpgrade.setName("Medieval Infantry");
        assertEquals("Warrior --> Swordsman --> Medieval Infantry", prto.getUpgradePath());   
    }
    
    @Test
    public void upgradePathIsCorrectInLoopCase() {
        biq.unit.add(prto);
        PRTO upgrade = new PRTO(biq, 1);
        biq.unit.add(upgrade);
        prto.setUpgradeTo(1);
        prto.setName("Warrior");
        upgrade.setName("Swordsman");
        PRTO betterUpgrade = new PRTO(biq, 2);
        biq.unit.add(betterUpgrade);
        upgrade.setUpgradeTo(2);
        betterUpgrade.setName("Medieval Infantry");
        betterUpgrade.setUpgradeTo(0);
        assertEquals("Warrior --> Swordsman --> Medieval Infantry --> Warrior", prto.getUpgradePath());   
    }
    
    @Test
    public void upgradePathIsCorrectInLoopWithDifferentStartUnitCase() {
        biq.unit.add(prto);
        PRTO upgrade = new PRTO(biq, 1);
        biq.unit.add(upgrade);
        prto.setUpgradeTo(1);
        prto.setName("Warrior");
        upgrade.setName("Swordsman");
        PRTO betterUpgrade = new PRTO(biq, 2);
        biq.unit.add(betterUpgrade);
        upgrade.setUpgradeTo(2);
        betterUpgrade.setName("Medieval Infantry");
        betterUpgrade.setUpgradeTo(0);
        
        PRTO startUnit = new PRTO(biq, 3);
        startUnit.setUpgradeTo(0);
        startUnit.setName("Jaguar Warrior");
        assertEquals("Jaguar Warrior --> Warrior --> Swordsman --> Medieval Infantry --> Warrior", startUnit.getUpgradePath());   
    }
}
