
package com.civfanatics.civ3.xplatformeditor.specialty;

import com.civfanatics.civ3.biqFile.BLDG;
import com.civfanatics.civ3.biqFile.ERAS;
import com.civfanatics.civ3.biqFile.IO;
import com.civfanatics.civ3.biqFile.TECH;
import com.civfanatics.civ3.xplatformeditor.Main;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiPredicate;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Andrew
 */
public class PredicateFactoryTest {
    TECH tech;
    BLDG bldg;
    BiPredicate<TECH, String> techPredicate = PredicateFactory.createTECHFilter();
    BiPredicate<BLDG, String> bldgPredicate = PredicateFactory.createBLDGFilter();
    
    
    @Before
    public void setup() {
        IO baselink = new IO();
        tech = new TECH();
        bldg = new BLDG(baselink);
    }
    
    @Test
    public void canary(){}
    
    @Test
    public void testBiPredicateNameMatch() {
        tech.setName("Test");
        
        boolean result = techPredicate.test(tech, "Test");
        assertTrue(result);
    }
    
    @Test
    public void testBiPredicateWithNameMismatch() {
        tech.setName("Test");
        
        boolean result = techPredicate.test(tech, "Masonry");
        assertFalse(result);
    }
    
    @Test
    public void testBiPredicateRecyclingTrueWhenItIsTrue() {
        tech.setEnablesRecycling(true);
        
        boolean result = techPredicate.test(tech, "enablesRecycling=true");
        assertTrue(result);
    }
    
    @Test
    public void testBiPredicateRecyclingFalseWhenItIsTrue() {
        tech.setEnablesRecycling(true);
        
        boolean result = techPredicate.test(tech, "enablesRecycling=false");
        assertFalse(result);
    }
    
    @Test
    public void testBiPredicateRecyclingTrueWhenItIsFalse() {
        tech.setEnablesRecycling(false);
        
        boolean result = techPredicate.test(tech, "enablesRecycling=true");
        assertFalse(result);
    }
    
    @Test
    public void testBiPredicateRecyclingFalseWhenItIsFalse() {
        tech.setEnablesRecycling(false);
        
        boolean result = techPredicate.test(tech, "enablesRecycling=false");
        assertTrue(result);
    }
    
    @Test
    public void testEraAncientTrue() {
        tech.setEra(0);    //zero-based; first era
        
        boolean result = techPredicate.test(tech, "era=1");
        assertTrue(result);
    }
    
    @Test
    public void testEraAncientFalse() {
        tech.setEra(0);    //zerobased; first era
        
        boolean result = techPredicate.test(tech, "era=2");
        assertFalse(result);
    }
    
    @Test
    public void testEraAncientByName() {
        setSampleERASData();
        tech.setEra(0);    //zerobased; first era
        
        boolean result = techPredicate.test(tech, "era=\"Ancient Times\"");
        assertTrue(result);
    }
    
    @Test
    public void testEraAncientByNameWhenNotAMatch() {
        setSampleERASData();
        tech.setEra(0);    //zerobased; first era
        
        boolean result = techPredicate.test(tech, "era=\"Middle Ages\"");
        assertFalse(result);
    }
    
    private void setSampleERASData() {
        IO dummyBIQFile = new IO();
        List<ERAS> eras = new LinkedList();
        ERAS ancientTimes = new ERAS(dummyBIQFile);
        ancientTimes.setEraName("Ancient Times");
        ERAS middleAges = new ERAS(dummyBIQFile);
        middleAges.setEraName("Middle Ages");
        eras.add(ancientTimes);
        eras.add(middleAges);
        dummyBIQFile.eras = eras;
        Main.setBIQForTesting(dummyBIQFile);
    }
    
    
    BiPredicate<TECH, String> truePredicate = (t, s) -> {
        return true;
    };
    
    BiPredicate<TECH, String> falsePredicate = (t, s) -> {
        return false;
    };
    
    @Test
    public void testEvalutePredicateReturnsFalseWhenOneOfThreeIsFalse() {
        List<BiPredicate> trueTrueFalse = Arrays.asList(truePredicate, truePredicate, falsePredicate);
        List<String> dummyTokens = Arrays.asList("", "", "");
        assertFalse(PredicateCommonFunctions.evaluatePredicates(tech, trueTrueFalse, dummyTokens));
    }
    
    @Test
    public void testEvalutePredicateReturnsFalseWhenMiddleOfThreeIsFalse() {
        List<BiPredicate> trueFalseTrue = Arrays.asList(truePredicate, falsePredicate, truePredicate);
        List<String> dummyTokens = Arrays.asList("", "", "");
        assertFalse(PredicateCommonFunctions.evaluatePredicates(tech, trueFalseTrue, dummyTokens));
    }
    @Test
    public void testEvalutePredicateReturnsTrueWhenAllThreeAreTrue() {
        List<BiPredicate> trueTrueTrue = Arrays.asList(truePredicate, truePredicate, truePredicate);
        List<String> dummyTokens = Arrays.asList("", "", "");
        assertTrue(PredicateCommonFunctions.evaluatePredicates(tech, trueTrueTrue, dummyTokens));
    }
    
    @Test
    public void testOrWorks() {
        List<BiPredicate> falseTrue = Arrays.asList(falsePredicate, truePredicate);
        List<String> dummyTokens = Arrays.asList("", "or", "");
        assertTrue(PredicateCommonFunctions.evaluatePredicates(tech, falseTrue, dummyTokens));
    }
    
    @Test
    public void testA_and_B_or_C() {
        List<BiPredicate> predicates = Arrays.asList(falsePredicate, falsePredicate, truePredicate);
        List<String> dummyTokens = Arrays.asList("", "", "or", "");
        assertTrue(PredicateCommonFunctions.evaluatePredicates(tech, predicates, dummyTokens));
    }
    
    @Test
    public void testA_and_B_or_C_only_B_true() {
        List<BiPredicate> predicates = Arrays.asList(falsePredicate, truePredicate, falsePredicate);
        List<String> dummyTokens = Arrays.asList("", "", "or", "");
        assertFalse(PredicateCommonFunctions.evaluatePredicates(tech, predicates, dummyTokens));
    }
    
    @Test
    public void testA_and_B_or_C_and_D_one_of_each_half_true() {
        List<BiPredicate> predicates = Arrays.asList(falsePredicate, truePredicate, falsePredicate, truePredicate);
        List<String> dummyTokens = Arrays.asList("", "", "or", "", "");
        assertFalse(PredicateCommonFunctions.evaluatePredicates(tech, predicates, dummyTokens));
    }
    
    @Test
    public void testA_and_B_or_C_and_D_one_half_true() {
        List<BiPredicate> predicates = Arrays.asList(falsePredicate, falsePredicate, truePredicate, truePredicate);
        List<String> dummyTokens = Arrays.asList("", "", "or", "", "");
        assertTrue(PredicateCommonFunctions.evaluatePredicates(tech, predicates, dummyTokens));
    }
    
    @Test
    public void testA_or_B_and_C_or_D_only_a_true() {
        List<BiPredicate> predicates = Arrays.asList(truePredicate, falsePredicate, falsePredicate, truePredicate);
        List<String> dummyTokens = Arrays.asList("true", "or", "false", "false", "or", "false");
        assertTrue(PredicateCommonFunctions.evaluatePredicates(tech, predicates, dummyTokens));
    }
    
    @Test
    public void testA_or_B_and_C_or_D_only_b_true() {
        List<BiPredicate> predicates = Arrays.asList(falsePredicate, truePredicate, falsePredicate, falsePredicate);
        List<String> dummyTokens = Arrays.asList("false", "or", "true", "false", "or", "false");
        assertFalse(PredicateCommonFunctions.evaluatePredicates(tech, predicates, dummyTokens));
    }
    
    @Test
    public void testA_or_B_and_C_or_D_b_and_c_true() {
        List<BiPredicate> predicates = Arrays.asList(falsePredicate, truePredicate, truePredicate, falsePredicate);
        List<String> dummyTokens = Arrays.asList("false", "or", "true", "true", "or", "false");
        assertTrue(PredicateCommonFunctions.evaluatePredicates(tech, predicates, dummyTokens));
    }
    
    @Test
    public void testA_or_B_and_C_or_D_only_d_true() {
        List<BiPredicate> predicates = Arrays.asList(falsePredicate, falsePredicate, falsePredicate, truePredicate);
        List<String> dummyTokens = Arrays.asList("false", "or", "false", "false", "or", "true");
        assertTrue(PredicateCommonFunctions.evaluatePredicates(tech, predicates, dummyTokens));
    }
    
    /**
     * (A or B) and C
     * Should be true when A and C are true.
     */
    @Test
    public void testParenA_or_BCloseParen_and_C__a_and_c_true__equals_true() {
        List<BiPredicate> predicates = Arrays.asList(truePredicate, falsePredicate, truePredicate);
        List<String> dummyTokens = Arrays.asList("", "or", "", "");
        assertTrue(PredicateCommonFunctions.evaluatePredicates(tech, predicates, dummyTokens));
    }
    
    /**
     * (A or B) and C
     * Should be false when only A is true.
     */
    @Test
    public void testParenA_or_BCloseParen_and_C__a_true__equals_false() {
        List<BiPredicate> predicates = Arrays.asList(truePredicate, falsePredicate, falsePredicate);
        List<String> dummyTokens = Arrays.asList("(", "A", "or", "B", ")", "C");
        assertFalse(PredicateCommonFunctions.evaluatePredicates(tech, predicates, dummyTokens));
    }
    
    /**
     * (A or B) and (C or D)
     * Should be false when A and B are true.
     */
    @Test
    public void testParenA_or_BParen_and_ParenC_or_DParen__trueTrueFalseFalse__false() {
        List<BiPredicate> predicates = Arrays.asList(truePredicate, truePredicate, falsePredicate, falsePredicate);
        List<String> dummyTokens = Arrays.asList("(", "A", "or", "B", ")", "(", "C", "or", "D", ")");
        assertFalse(PredicateCommonFunctions.evaluatePredicates(tech, predicates, dummyTokens));
    }
    
    /**
     * A and (B or C) and D
     * Should be false when C and D are true.
     */
    @Test
    public void testA_and_ParenB_or_CParen_and_D__falseFalseTrueTrue__false() {
        List<BiPredicate> predicates = Arrays.asList(falsePredicate, falsePredicate, truePredicate, truePredicate);
        List<String> dummyTokens = Arrays.asList("A", "(", "B", "or", "C", ")", "D");
        assertFalse(PredicateCommonFunctions.evaluatePredicates(tech, predicates, dummyTokens));
    }
    
    @Test
    public void testBuildingVeteranUnits() {
        bldg.setVeteranUnits(true);
        assertTrue(bldgPredicate.test(bldg, "veteranLand=true"));
        assertFalse(bldgPredicate.test(bldg, "veteranLand=false"));
        assertFalse(bldgPredicate.test(bldg, "veteranLand=invalidValue"));
        bldg.setVeteranUnits(false);
        assertTrue(bldgPredicate.test(bldg, "veteranLand=false"));
        assertFalse(bldgPredicate.test(bldg, "veteranLand=true"));
        assertFalse(bldgPredicate.test(bldg, "veteranLand=invalidValue"));
    }
    
    /**
     * Microbenchmark for predicates.  Shows that the "efficient" way of checking
     * whether a pointer/name match occurs is 8-12 times as efficient as the
     * "inefficient" way.  This amounts to saving about 300 microseconds per
     * iteration through an 80-item building list at 1.2 GHz on a T7500.  This
     * occurs per-character search, and thus on a slow computer, a typist whose 
     * fingers move so fast they set the place ablaze could probably out-type the
     * inefficient version.  Add in more complex queries, and it's not that far-fetched.
     */
    @Test
    public void testReqBuildingPerformance() {
        IO biq = new IO();
        biq.buildings = new ArrayList<BLDG>();
        for (int i = 0; i < 80; i++) {
            BLDG newBuilding = new BLDG(biq);
            newBuilding.setName("");
            biq.buildings.add(newBuilding);
        }
        Main.biqFile = Arrays.asList(biq);
        Main.biqIndex = 0;
        
        int iterations = 1000;
        
        long startEfficient = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            for (BLDG bldg : biq.buildings) {
                PredicateFactory.reqBuilding.test(bldg, "some name");
            }
        }
        long endEfficient = System.nanoTime();
        System.out.println("Efficient time for " + iterations + " iterations: " + (endEfficient - startEfficient)/1000 + " microseconds");
        
        
        long startInefficient = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            for (BLDG bldg : biq.buildings) {
                PredicateFactory.reqBuildingInefficient.test(bldg, "some name");
            }
        }
        long endInefficient = System.nanoTime();
        System.out.println("Inefficient time for " + iterations + " iterations: " + (endInefficient - startInefficient)/1000 + " microseconds");
    }
}
