/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.xplatformeditor.savFunctionality;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Andrew
 */
public class CustomHistographTest {
        
    CustomHistograph hist = null;

    @Before
    public void setup() {
        hist = new CustomHistograph();
        List<String> names = new ArrayList<>();
        names.add("Civ one");
        names.add("Civ two");
        names.add("Civ three");
        hist.sendData(null, new ArrayList<>(), names, null);
    }
    
    @Test
    public void getScoresUpperBound_roundsUpToNearestThousand() {
        Integer[] scoresArray = { 1882, 709, 1447, 2019, 1204 };
        List<Integer> scores = Arrays.asList(scoresArray);
        List<List<Integer>> allScores = new ArrayList<>();
        allScores.add(scores);
        assertEquals(4000, hist.getScoresUpperBound(allScores));
    }
    
    @Test
    public void getScoresUpperBound_findsScoresFromAllPlayers() {
        Integer[] scoresArray1 = { 1882, 709, 1447, 2019, 1204 };
        List<Integer> scores1 = Arrays.asList(scoresArray1);
        Integer[] scoresArray2 = { 1882, 709, 7447, 2019, 1204 };
        List<Integer> scores2 = Arrays.asList(scoresArray2);
        Integer[] scoresArray3 = { 1882, 709, 1447, 2019, 1204 };
        List<Integer> scores3 = Arrays.asList(scoresArray3);
        
        List<List<Integer>> allScores = new ArrayList<>();
        allScores.add(scores1);
        allScores.add(scores2);
        allScores.add(scores3);
        
        assertEquals(8000, hist.getScoresUpperBound(allScores));
        
    }
}
