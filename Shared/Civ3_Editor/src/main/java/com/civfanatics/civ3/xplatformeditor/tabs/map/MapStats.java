/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.xplatformeditor.tabs.map;

import com.civfanatics.civ3.biqFile.CITY;
import com.civfanatics.civ3.biqFile.IO;
import com.civfanatics.civ3.biqFile.TERR;
import com.civfanatics.civ3.biqFile.TILE;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Andrew
 */
public class MapStats {
    public static String calculateMapStats(IO biq) {
        int numDesert = 0, numPlains = 0, numGrassland = 0, numTundra = 0, numFloodplain = 0, numHills = 0, numMountains = 0, numForest = 0, numJungle = 0, numMarsh = 0, numVolcano = 0, numCoast = 0, numSea = 0, numOcean = 0;
        int numBonusGrassland = 0;
        
        for (TILE tile : biq.tile) {
            byte terr = tile.getRealTerrain();
            switch(terr) {
                case TERR.DESERT:
                    numDesert++;
                    break;
                case TERR.PLAINS:
                    numPlains++;
                    break;
                case TERR.GRASSLAND:
                    numGrassland++;
                    if (tile.hasBonusGrassland()) {
                        numBonusGrassland++;
                    }
                    break;
                case TERR.TUNDRA:
                    numTundra++;
                    break;
                case TERR.FLOODPLAIN:
                    numFloodplain++;
                    break;
                case TERR.HILLS:
                    numHills++;
                    break;
                case TERR.MOUNTAIN:
                    numMountains++;
                    break;
                case TERR.FOREST:
                    numForest++;
                    break;
                case TERR.JUNGLE:
                    numJungle++;
                    break;
                case TERR.MARSH:
                    numMarsh++;
                    break;
                case TERR.VOLCANO:
                    numVolcano++;
                    break;
                case TERR.COAST:
                    numCoast++;
                    break;
                case TERR.SEA:
                    numSea++;
                    break;
                case TERR.OCEAN:
                    numOcean++;
                    break;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Terrain Statistics\n\n");
        double totalTiles = biq.tile.size();
        DecimalFormat df = new DecimalFormat("0.0%");
        sb.append("Total tiles: " + biq.tile.size()).append("\n\n");
        sb.append("Desert tiles: " + numDesert).append(" (").append(df.format(numDesert/totalTiles)).append(")\n");
        sb.append("Plains tiles: " + numPlains).append(" (").append(df.format(numPlains/totalTiles)).append(")\n");
        sb.append("Grassland tiles: " + numGrassland).append(" (").append(df.format(numGrassland/totalTiles)).append(")\n");
        sb.append("  Bonus Grassland tiles: " + numBonusGrassland).append(" (").append(df.format(numBonusGrassland/totalTiles)).append(")\n");
        sb.append("  Bonus as % of Grassland: " + df.format(numBonusGrassland/(numGrassland + 0.0d))).append("\n");
        sb.append("Tundra tiles: " + numTundra).append(" (").append(df.format(numTundra/totalTiles)).append(")\n");
        sb.append("Floodplain tiles: " + numFloodplain).append(" (").append(df.format(numFloodplain/totalTiles)).append(")\n");
        sb.append("Hills tiles: " + numHills).append(" (").append(df.format(numHills/totalTiles)).append(")\n");
        sb.append("Mountains tiles: " + numMountains).append(" (").append(df.format(numMountains/totalTiles)).append(")\n");
        sb.append("Forest tiles: " + numForest).append(" (").append(df.format(numForest/totalTiles)).append(")\n");
        sb.append("Jungle tiles: " + numJungle).append(" (").append(df.format(numJungle/totalTiles)).append(")\n");
        sb.append("Marsh tiles: " + numMarsh).append(" (").append(df.format(numMarsh/totalTiles)).append(")\n");
        sb.append("Volcano tiles: " + numVolcano).append(" (").append(df.format(numVolcano/totalTiles)).append(")\n");
        sb.append("Coast tiles: " + numCoast).append(" (").append(df.format(numCoast/totalTiles)).append(")\n");
        sb.append("Sea tiles: " + numSea).append(" (").append(df.format(numSea/totalTiles)).append(")\n");
        sb.append("Ocean tiles: " + numOcean).append(" (").append(df.format(numOcean/totalTiles)).append(")\n");
        return sb.toString();
    }
    
    public static String getCityStats(IO biq) {
        int totalNumber = biq.city.size();
        Map<String, Integer> countByOwner = new HashMap<>();
        
        for (CITY city : biq.city) {
            String ownerKey = "";
            if (city.getOwnerType() == CITY.OWNER_PLAYER) {
                if (biq.hasCustomPlayerData() && biq.player.get(city.getOwner()).civ > 0) { //0 = barbs
                    ownerKey = biq.civilization.get(biq.player.get(city.getOwner()).civ).getName();
                }
                else {
                    ownerKey = "Player " + (city.getOwner() + 1);
                }
            }
            else if (city.getOwnerType() == CITY.OWNER_CIV) {
                ownerKey = biq.civilization.get(city.getOwner()).getName();
            }
            if (countByOwner.containsKey(ownerKey)) {
                countByOwner.put(ownerKey, countByOwner.get(ownerKey) + 1);
            }
            else {
                countByOwner.put(ownerKey, 1);
            }
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("City statistics\n\n");
        sb.append("Total number of cities: " + totalNumber + "\n\n");
        List<Map.Entry<String, Integer>> countByOwnerArray = new ArrayList(countByOwner.entrySet());
        Collections.sort(countByOwnerArray, new Comparator<Entry<String, Integer>>(){
            public int compare(Entry<String, Integer> a, Entry<String, Integer> b) {
                return b.getValue().compareTo(a.getValue());
            }
        });
        for (Entry<String, Integer> entry : countByOwnerArray) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}
