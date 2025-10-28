package com.civfanatics.civ3.xplatformeditor.tabs.map;

import java.awt.image.BufferedImage;

/**
 *
 * @author Andrew
 */
public class GraphicsAssets {

    public BufferedImage[][]baseTerrainGraphics;
    public BufferedImage[][]lmTerrainGraphics;
    public BufferedImage[]mountainGraphics;
    public BufferedImage[]snowMountainGraphics;
    public BufferedImage[]lmMountainGraphics;
    public BufferedImage[]forestMountainGraphics;
    public BufferedImage[]jungleMountainGraphics;
    public BufferedImage[]hillGraphics;
    public BufferedImage[]lmHillGraphics;
    public BufferedImage[]forestHillGraphics;
    public BufferedImage[]jungleHillGraphics;
    public BufferedImage[]volcanoGraphics;
    public BufferedImage[]forestVolcanoGraphics;
    public BufferedImage[]jungleVolcanoGraphics;
    public BufferedImage[]largeJungle;
    public BufferedImage[]smallJungle;
    public BufferedImage[]largeGrassForest;
    public BufferedImage[]smallGrassForest;
    public BufferedImage[]grassPineForest;
    public BufferedImage[]largePlainsForest;
    public BufferedImage[]smallPlainsForest;
    public BufferedImage[]plainsPineForest;
    public BufferedImage[]largeTundraForest;
    public BufferedImage[]smallTundraForest;
    public BufferedImage[]tundraPineForest;
    public BufferedImage[]largeMarsh;
    public BufferedImage[]pineMarsh;
    public BufferedImage[]roadGraphics;
    public BufferedImage[]railroadGraphics;
    public BufferedImage[]irrigationGraphics;
    public BufferedImage[]plainsIrrigationGraphics;
    public BufferedImage[]desertIrrigationGraphics;
    public BufferedImage[]tundraIrrigationGraphics;
    public BufferedImage[]buildingGraphics;
    public BufferedImage[][]borderGraphics;
    public BufferedImage[]resourceGraphics;
    public BufferedImage[][][]noWallCityGraphics;
    public BufferedImage[][]wallCityGraphics;
    public BufferedImage[]riverGraphics;
    public BufferedImage[]deltaGraphics;
    public BufferedImage[]goodyhutGraphics;
    public BufferedImage[]pollutionGraphics;
    public BufferedImage[]startLocGraphics;
    public BufferedImage[]airfieldGraphics;
    public BufferedImage[]outpostGraphics;
    public BufferedImage radarTowerGraphics;
    public BufferedImage victoryPointGraphics;
    public BufferedImage[]craterGraphics;
    public BufferedImage[]ruinGraphics;
    public BufferedImage landmarkOverlay;
    public BufferedImage[][]unitIcons;
    public BufferedImage[]tntGrass;
    public BufferedImage[]tntGrassShield;
    public BufferedImage[]tntPlains;
    public BufferedImage[]tntDesert;
    public BufferedImage[]tntTundra;
    public BufferedImage[]tntFloodPlains;
    public BufferedImage fog;
    
    public boolean largeRuinGraphics = false;
    
    public void sendGraphics(BufferedImage[][]baseTerrainGraphics, BufferedImage[][]lmTerrainGraphics, BufferedImage[]roadGraphics,           BufferedImage[]railroadGraphics,   BufferedImage[]buildingGraphics,
                             BufferedImage[][]borderGraphics,      BufferedImage[]resourceGraphics,    BufferedImage[][][]noWallCityGraphics, BufferedImage[][]wallCityGraphics, BufferedImage[]goodyhutGraphics,
                             BufferedImage[]pollutionGraphics,     BufferedImage[]startLocGraphics,    BufferedImage[]airfieldGraphics,       BufferedImage[]outpostGraphics,    BufferedImage radarTowerGraphics,
                             BufferedImage victoryPointGraphics,   BufferedImage[]craterGraphics,      BufferedImage[]ruinGraphics,           BufferedImage landmarkOverlay,     BufferedImage[][]unitIcons)
    {
        this.baseTerrainGraphics = baseTerrainGraphics;
        this.lmTerrainGraphics = lmTerrainGraphics;
        this.roadGraphics = roadGraphics;
        this.railroadGraphics = railroadGraphics;
        this.buildingGraphics = buildingGraphics;
        this.borderGraphics = borderGraphics;
        this.resourceGraphics = resourceGraphics;
        this.noWallCityGraphics = noWallCityGraphics;
        this.wallCityGraphics = wallCityGraphics;
        this.goodyhutGraphics = goodyhutGraphics;
        this.pollutionGraphics = pollutionGraphics;
        this.startLocGraphics = startLocGraphics;
        this.airfieldGraphics = airfieldGraphics;
        this.outpostGraphics = outpostGraphics;
        this.radarTowerGraphics = radarTowerGraphics;
        this.victoryPointGraphics = victoryPointGraphics;
        this.craterGraphics = craterGraphics;
        this.ruinGraphics = ruinGraphics;
        if (ruinGraphics[0].getWidth() > 300 || ruinGraphics[0].getHeight() > 200) //standard size is 167x95, give a fair buffer before we switch rendering modes
            largeRuinGraphics = true;
        this.landmarkOverlay = landmarkOverlay;
        this.unitIcons = unitIcons;
    }
    
    public void sendHills(BufferedImage[]mountainGraphics, BufferedImage[]snowMountainGraphics, BufferedImage[]lmMountainGraphics, BufferedImage[]forestMountainGraphics, BufferedImage[]jungleMountainGraphics, 
                          BufferedImage[]hillGraphics, BufferedImage[]lmHillGraphics, BufferedImage[]forestHillGraphics, BufferedImage[]jungleHillGraphics, 
                          BufferedImage[]volcanoGraphics, BufferedImage[]forestVolcanoGraphics, BufferedImage[]jungleVolcanoGraphics) {
        this.mountainGraphics = mountainGraphics;
        this.snowMountainGraphics = snowMountainGraphics;
        this.lmMountainGraphics = lmMountainGraphics;
        this.forestMountainGraphics = forestMountainGraphics;
        this.jungleMountainGraphics = jungleMountainGraphics;
        this.hillGraphics = hillGraphics;
        this.lmHillGraphics = lmHillGraphics;
        this.forestHillGraphics = forestHillGraphics;
        this.jungleHillGraphics = jungleHillGraphics;
        this.volcanoGraphics = volcanoGraphics;
        this.forestVolcanoGraphics = forestVolcanoGraphics;
        this.jungleVolcanoGraphics = jungleVolcanoGraphics;
    }

    public void sendWoodlands(BufferedImage[] largeJungle, BufferedImage[] smallJungle, BufferedImage[] largeGrassForest, BufferedImage[] smallGrassForest, BufferedImage[] grassPineForest, BufferedImage[] largePlainsForest, BufferedImage[] smallPlainsForest, BufferedImage[] plainsPineForest, BufferedImage[] largeTundraForest, BufferedImage[] smallTundraForest, BufferedImage[] tundraPineForest, BufferedImage[] largeMarsh, BufferedImage[] pineMarsh)
    {
        this.largeJungle = largeJungle;
        this.smallJungle = smallJungle;
        this.largeGrassForest = largeGrassForest;
        this.smallGrassForest = smallGrassForest;
        this.grassPineForest = grassPineForest;
        this.largePlainsForest = largePlainsForest;
        this.smallPlainsForest = smallPlainsForest;
        this.plainsPineForest = plainsPineForest;
        this.largeTundraForest = largeTundraForest;
        this.smallTundraForest = smallTundraForest;
        this.tundraPineForest = tundraPineForest;
        this.largeMarsh = largeMarsh;
        this.pineMarsh = pineMarsh;
    }

    public void sendIrrigation(BufferedImage[] irrigationGraphics, BufferedImage[] plainsIrrigationGraphics, BufferedImage[] desertIrrigationGraphics, BufferedImage[] tundraIrrigationGraphics, BufferedImage[]riverGraphics, BufferedImage[] deltaGraphics)
    {
        this.irrigationGraphics = irrigationGraphics;
        this.plainsIrrigationGraphics = plainsIrrigationGraphics;
        this.desertIrrigationGraphics = desertIrrigationGraphics;
        this.tundraIrrigationGraphics = tundraIrrigationGraphics;
        this.riverGraphics = riverGraphics;
        this.deltaGraphics = deltaGraphics;
    }
    
    public void sendTNT(BufferedImage[]tntGrass, BufferedImage[]tntGrassShield, BufferedImage[]tntPlains, BufferedImage[]tntDesert, BufferedImage[]tntTundra, BufferedImage[]tntFloodPlains)
    {
        this.tntGrass = tntGrass;
        this.tntGrassShield = tntGrassShield;
        this.tntPlains = tntPlains;
        this.tntDesert = tntDesert;
        this.tntTundra = tntTundra;
        this.tntFloodPlains = tntFloodPlains;
    }
    
    public void sendFog(BufferedImage fog)
    {
        this.fog = fog;
    }
}
