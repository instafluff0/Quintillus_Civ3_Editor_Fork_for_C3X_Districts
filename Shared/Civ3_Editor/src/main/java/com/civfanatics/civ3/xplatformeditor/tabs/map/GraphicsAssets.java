package com.civfanatics.civ3.xplatformeditor.tabs.map;

import com.civfanatics.civ3.xplatformeditor.districts.DistrictDefinitions;
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
    public DistrictSpriteSet[] districtSprites = new DistrictSpriteSet[0];
    public WonderDistrictSpriteSet[] wonderDistrictSprites = new WonderDistrictSpriteSet[0];
    public DistrictDefinitions districtDefinitions;

    public static class DistrictSpriteSet {
        private final BufferedImage[][][] sprites;

        public DistrictSpriteSet(int variants, int eras, int columns) {
            sprites = new BufferedImage[Math.max(1, variants)][Math.max(1, eras)][Math.max(1, columns)];
        }

        public void setSprite(int variant, int era, int column, BufferedImage image) {
            if (variant < 0 || variant >= sprites.length) {
                return;
            }
            if (era < 0 || era >= sprites[variant].length) {
                return;
            }
            if (column < 0 || column >= sprites[variant][era].length) {
                return;
            }
            sprites[variant][era][column] = image;
        }

        public BufferedImage getSprite(int variant, int era, int column) {
            if (sprites.length == 0) {
                return null;
            }
            int clampedVariant = clamp(variant, 0, sprites.length - 1);
            BufferedImage[][] eraArray = sprites[clampedVariant];
            if (eraArray.length == 0) {
                return null;
            }
            int clampedEra = clamp(era, 0, eraArray.length - 1);
            BufferedImage[] columns = eraArray[clampedEra];
            if (columns.length == 0) {
                return null;
            }
            int clampedColumn = clamp(column, 0, columns.length - 1);
            return columns[clampedColumn];
        }

        public int getVariantCount() {
            return sprites.length;
        }

        public int getEraCount() {
            return sprites.length == 0 ? 0 : sprites[0].length;
        }

        public int getColumnCount() {
            return (sprites.length == 0 || sprites[0].length == 0) ? 0 : sprites[0][0].length;
        }
    }

    public static class WonderDistrictSpriteSet {
        private BufferedImage completed;
        private BufferedImage constructing;

        public void setCompleted(BufferedImage completed) {
            this.completed = completed;
        }

        public BufferedImage getCompleted() {
            return completed;
        }

        public void setConstructing(BufferedImage constructing) {
            this.constructing = constructing;
        }

        public BufferedImage getConstructing() {
            return constructing;
        }
    }
    
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
    
    public void sendDistrictGraphics(DistrictSpriteSet[] districtSprites, WonderDistrictSpriteSet[] wonderDistrictSprites) {
        this.districtSprites = (districtSprites != null) ? districtSprites : new DistrictSpriteSet[0];
        this.wonderDistrictSprites = (wonderDistrictSprites != null) ? wonderDistrictSprites : new WonderDistrictSpriteSet[0];
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

    public void setDistrictDefinitions(DistrictDefinitions definitions) {
        this.districtDefinitions = definitions;
    }

    private static int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }
}
