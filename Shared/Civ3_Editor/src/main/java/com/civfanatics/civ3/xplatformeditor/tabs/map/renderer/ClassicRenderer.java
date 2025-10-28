package com.civfanatics.civ3.xplatformeditor.tabs.map.renderer;

import com.civfanatics.civ3.biqFile.BLDG;
import com.civfanatics.civ3.biqFile.CITY;
import com.civfanatics.civ3.biqFile.CLNY;
import com.civfanatics.civ3.biqFile.GOOD;
import com.civfanatics.civ3.biqFile.IO;
import com.civfanatics.civ3.biqFile.LEAD;
import com.civfanatics.civ3.biqFile.PRTO;
import com.civfanatics.civ3.biqFile.RACE;
import com.civfanatics.civ3.biqFile.RULE;
import com.civfanatics.civ3.biqFile.SLOC;
import com.civfanatics.civ3.biqFile.TERR;
import com.civfanatics.civ3.biqFile.TILE;
import static com.civfanatics.civ3.biqFile.TILE.RIVER_NORTHEAST;
import static com.civfanatics.civ3.biqFile.TILE.RIVER_NORTHWEST;
import static com.civfanatics.civ3.biqFile.TILE.RIVER_SOUTHEAST;
import static com.civfanatics.civ3.biqFile.TILE.RIVER_SOUTHWEST;
import com.civfanatics.civ3.biqFile.UNIT;
import com.civfanatics.civ3.biqFile.WMAP;
import com.civfanatics.civ3.biqFile.util.MapDirection;
import static com.civfanatics.civ3.biqFile.util.MapDirection.NORTHEAST;
import static com.civfanatics.civ3.biqFile.util.MapDirection.NORTHWEST;
import static com.civfanatics.civ3.biqFile.util.MapDirection.SOUTHEAST;
import static com.civfanatics.civ3.biqFile.util.MapDirection.SOUTHWEST;
import com.civfanatics.civ3.xplatformeditor.Main;
import static com.civfanatics.civ3.xplatformeditor.Main.settings;
import com.civfanatics.civ3.xplatformeditor.Settings;
import com.civfanatics.civ3.xplatformeditor.districts.DistrictDefinitions;
import com.civfanatics.civ3.xplatformeditor.districts.DistrictDefinitions.DistrictDefinition;
import com.civfanatics.civ3.xplatformeditor.imageSupport.Units32Supplier;
import com.civfanatics.civ3.xplatformeditor.tabs.map.GraphicsAssets;
import com.civfanatics.civ3.xplatformeditor.tabs.map.GraphicsAssets.DistrictSpriteSet;
import com.civfanatics.civ3.xplatformeditor.tabs.map.GraphicsAssets.WonderDistrictSpriteSet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

/**
 * The original 2010 era renderer.
 * @author Andrew
 */
public class ClassicRenderer extends Renderer {

    Logger logger = Logger.getLogger(this.getClass());
    
    Random rnd = new Random();  //for display variety
    private static final Color[] DISTRICT_COLORS = new Color[] {
        new Color(70, 130, 180),
        new Color(218, 112, 214),
        new Color(60, 179, 113),
        new Color(255, 165, 0),
        new Color(106, 90, 205),
        new Color(176, 196, 222)
    };
    private static final int[][] WORK_RADIUS_OFFSETS = new int[][] {
        {0, 0},
        {1, -1}, {2, 0}, {1, 1}, {0, 2}, {-1, 1}, {-2, 0}, {-1, -1}, {0, -2},
        {1, -3}, {2, -2}, {3, -1}, {3, 1}, {2, 2}, {1, 3}, {-1, 3}, {-2, 2}, {-3, 1}, {-3, -1}, {-2, -2}, {-1, -3}
    };
    private final Map<String, Integer> buildingNameToIndex = new HashMap<String, Integer>();
    private final Map<Integer, int[]> districtDependentBuildingCache = new HashMap<Integer, int[]>();
    private final Map<Integer, Integer> civEraCache = new HashMap<Integer, Integer>();
        
    private List<TILE> tiles = null;
    private WMAP wmap = null;
    List<LEAD> player;
    List<CITY> city;
    List<SLOC> startingLocation;
    List<RACE> civ;
    List<GOOD> resource;
    List<CLNY> colony;
    List<RULE> rule;
    IO biq;
    
    public ClassicRenderer(Image buffer, GraphicsAssets assets, IO biq) {
        BUFFERWIDTH = Main.mainMain.getWidth();
        this.assets = assets;
        
        this.biq = biq;
        this.buffer = (BufferedImage)buffer;
        this.tiles = biq.tile;
        this.wmap = biq.worldMap.get(0);
        this.player = biq.player;
        this.city = biq.city;
        this.startingLocation = biq.startingLocation;
        this.civ = biq.civilization;
        this.resource = biq.resource;
        this.colony = biq.colony;
        this.rule = biq.rule;
        buildingNameToIndex.clear();
        civEraCache.clear();
        for (int i = 0; i < biq.buildings.size(); i++) {
            BLDG building = biq.buildings.get(i);
            if (building != null && building.getName() != null) {
                buildingNameToIndex.put(building.getName().trim().toLowerCase(Locale.ENGLISH), Integer.valueOf(building.getIndex()));
            }
        }
    }
    
    public void setViewportSize(int x, int y) {
        BUFFERWIDTH = x;
        BUFFERHEIGHT = y;
        
        buffer = new BufferedImage(BUFFERWIDTH, BUFFERHEIGHT, BufferedImage.TYPE_INT_ARGB);
    }
    
    public void render() {
        if (buffer == null) {
            buffer = new BufferedImage(BUFFERWIDTH, BUFFERHEIGHT, BufferedImage.TYPE_INT_ARGB);
        }
        Graphics bufferGraphics = buffer.createGraphics();
        
        int xIndex = 0, yIndex = 0;
        
        List<TILE> visibleTiles = new ArrayList<>();
        for (TILE t : tiles) {
            if (tileIsVisible(t.xPos, t.yPos)) {
                visibleTiles.add(t);
            }
        }
        
        //Rewiring this for proper isometric effects
        //first we draw the base terrain, and then we draw stuff on it
        for (TILE t : visibleTiles) {
            drawBaseTerrain(t, horizScrollOffset, vertScrollOffset, bufferGraphics);
        }
        
        if (assets.largeRuinGraphics && settings.ruinsEnabled) {
            drawLargeRuinGraphics(visibleTiles, bufferGraphics);
        }
        
        drawTileElements(visibleTiles, bufferGraphics);
        drawStartingLocations(bufferGraphics);
        drawCityNames(bufferGraphics);
        drawXYCoordinates(bufferGraphics);

        if (settings.gridEnabled) {
            drawGrid(xIndex, yIndex, bufferGraphics);
        }
        
    }

    private boolean drawDistrict(TILE tile, int defaultXPosition, int defaultYPosition, Graphics canvas)
    {
        if (assets == null || assets.districtSprites == null || assets.districtSprites.length == 0)
            return false;

        TILE.DistrictData data = tile.getDistrictData();
        if (data == null || data.districtType < 0)
            return false;

        int districtId = data.districtType;
        if (districtId < 0 || districtId >= assets.districtSprites.length)
            return false;

        DistrictSpriteSet set = assets.districtSprites[districtId];
        if (set == null)
            return false;

        boolean completed = (data.state == TILE.DISTRICT_STATE_COMPLETED);

        // Wonder districts use a dedicated sprite sheet
        if (districtId == DistrictDefinitions.WONDER_DISTRICT_ID) {
            BufferedImage wonderSprite = resolveWonderSprite(tile, completed);
            if (wonderSprite != null) {
                canvas.drawImage(wonderSprite, defaultXPosition, defaultYPosition, null);
                return true;
            }
        }

        if (!completed && districtId != DistrictDefinitions.NEIGHBORHOOD_DISTRICT_ID)
            return false;

        DistrictDefinitions definitions = assets.districtDefinitions;
        DistrictDefinition def = (definitions != null) ? definitions.getDistrict(districtId) : null;

        int ownerId = tile.owner;
        int variant = 0;
        int era = 0;

        int civIdForTerritory = resolveCivilizationId(tile);
        if (def != null && civIdForTerritory >= 0 && civIdForTerritory < civ.size()) {
            if (def.isVaryByCulture()) {
                int cultureGroup = civ.get(civIdForTerritory).getCultureGroup();
                variant = clamp(cultureGroup, 0, Math.max(0, set.getVariantCount() - 1));
            }
            if (def.isVaryByEra()) {
                int resolvedEra = resolveEraForCivilization(civIdForTerritory);
                era = clamp(resolvedEra, 0, Math.max(0, set.getEraCount() - 1));
            }
        } else if (districtId == DistrictDefinitions.NEIGHBORHOOD_DISTRICT_ID && set.getVariantCount() > 1) {
            variant = set.getVariantCount() - 1; // abandoned art
        }

        int column = 0;
        if (districtId == DistrictDefinitions.NEIGHBORHOOD_DISTRICT_ID) {
            column = getNeighborhoodColumn(tile.xPos, tile.yPos, Math.max(1, set.getColumnCount()));
        } else if (def != null && def.getDependentImprovementCount() > 0) {
            column = countDependentBuildings(tile, ownerId, def, Math.max(1, set.getColumnCount()));
        }

        BufferedImage sprite = set.getSprite(variant, era, column);
        if (sprite == null) {
            sprite = fallbackSprite(set);
        }
        if (sprite == null)
            return false;

        canvas.drawImage(sprite, defaultXPosition, defaultYPosition, null);
        return true;
    }

    private void drawDistrictPlaceholder(TILE tile, int defaultXPosition, int defaultYPosition, Graphics canvas)
    {
        TILE.DistrictData data = tile.getDistrictData();
        if (data == null || data.districtType < 0)
            return;

        Color old = canvas.getColor();
        Color color = DISTRICT_COLORS[data.districtType % DISTRICT_COLORS.length];
        int ovalX = defaultXPosition + 48;
        int ovalY = defaultYPosition + 12;
        canvas.setColor(new Color(0, 0, 0, 120));
        canvas.fillOval(ovalX - 2, ovalY - 2, 20, 14);
        canvas.setColor(color);
        canvas.fillOval(ovalX, ovalY, 16, 10);
        canvas.setColor(Color.BLACK);
        canvas.drawOval(ovalX, ovalY, 16, 10);
        canvas.setColor(old);
    }

    private BufferedImage resolveWonderSprite(TILE tile, boolean completed) {
        if (assets.wonderDistrictSprites == null || assets.wonderDistrictSprites.length == 0)
            return null;
        TILE.DistrictData data = tile.getDistrictData();
        if (data == null || data.wonderInfo == null)
            return null;
        TILE.WonderDistrictInfo info = data.wonderInfo;
        if (info.wonderIndex < 0 || info.wonderIndex >= assets.wonderDistrictSprites.length)
            return null;
        WonderDistrictSpriteSet set = assets.wonderDistrictSprites[info.wonderIndex];
        if (set == null)
            return null;
        if (completed && info.state == TILE.WDS_COMPLETED) {
            BufferedImage img = set.getCompleted();
            if (img != null)
                return img;
        }
        if (set.getConstructing() != null)
            return set.getConstructing();
        return set.getCompleted();
    }

    private int resolveCivilizationId(TILE tile) {
        if (tile == null)
            return -1;
        if (tile.ownerType == CITY.OWNER_CIV) {
            if (tile.owner >= 0 && tile.owner < civ.size())
                return tile.owner;
        } else if (tile.ownerType == CITY.OWNER_PLAYER) {
            int playerId = tile.owner;
            if (playerId >= 0 && playerId < player.size()) {
                LEAD leader = player.get(playerId);
                if (leader != null && leader.getCiv() >= 0)
                    return leader.getCiv();
            }
        }

        short cityId = tile.getCity();
        if (cityId >= 0 && cityId < city.size()) {
            CITY cityOnTile = city.get(cityId);
            if (cityOnTile != null) {
                int ownerType = cityOnTile.getOwnerType();
                if (ownerType == CITY.OWNER_CIV) {
                    int cityCiv = cityOnTile.getOwner();
                    if (cityCiv >= 0 && cityCiv < civ.size())
                        return cityCiv;
                } else if (ownerType == CITY.OWNER_PLAYER) {
                    int playerId = cityOnTile.getOwner();
                    if (playerId >= 0 && playerId < player.size()) {
                        LEAD leader = player.get(playerId);
                        if (leader != null && leader.getCiv() >= 0)
                            return leader.getCiv();
                    }
                }
            }
        }
        return -1;
    }

    private int resolveEraForCivilization(int civId) {
        if (civId < 0)
            return 0;
        Integer cached = civEraCache.get(Integer.valueOf(civId));
        if (cached != null)
            return cached.intValue();
        int era = 0;
        for (int i = 0; i < player.size(); i++) {
            LEAD leader = player.get(i);
            if (leader != null && leader.getCiv() == civId) {
                if (leader.initialEra >= 0)
                    era = leader.initialEra;
                break;
            }
        }
        civEraCache.put(Integer.valueOf(civId), Integer.valueOf(era));
        return era;
    }

    private int getNeighborhoodColumn(int tileX, int tileY, int columnCount) {
        if (columnCount <= 1) {
            return 0;
        }
        long v = ((long)tileX * 0x9E3779B1L) + ((long)tileY * 0x85EBCA6BL);
        v ^= (v >> 16);
        v *= 0x7FEB352DL;
        v ^= (v >> 15);
        v *= 0x846CA68BL;
        v ^= (v >> 16);
        long positive = v & 0x7FFFFFFFL;
        return (int)(positive % columnCount);
    }

    private int countDependentBuildings(TILE tile, int ownerId, DistrictDefinition def, int columnCount) {
        int[] buildingIndices = getDependentBuildingIndices(def);
        if (buildingIndices.length == 0)
            return 0;
        int completed = 0;
        for (int buildingIndex : buildingIndices) {
            if (tileHasCityWithBuilding(tile.xPos, tile.yPos, ownerId, buildingIndex))
                completed++;
        }
        return clamp(completed, 0, columnCount - 1);
    }

    private int[] getDependentBuildingIndices(DistrictDefinition def) {
        if (def == null)
            return new int[0];
        int districtId = def.getId();
        int[] cached = districtDependentBuildingCache.get(districtId);
        if (cached != null)
            return cached;
        List<String> names = def.getDependentImprovementNames();
        if (names.isEmpty()) {
            districtDependentBuildingCache.put(districtId, new int[0]);
            return new int[0];
        }
        List<Integer> indices = new ArrayList<Integer>();
        for (String name : names) {
            if (name == null)
                continue;
            Integer index = buildingNameToIndex.get(name.trim().toLowerCase(Locale.ENGLISH));
            if (index != null && index.intValue() >= 0) {
                indices.add(index);
            } else {
                logger.debug("Unable to resolve dependent improvement '" + name + "' for district " + def.getName());
            }
        }
        int[] result = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            result[i] = indices.get(i).intValue();
        }
        districtDependentBuildingCache.put(districtId, result);
        return result;
    }

    private boolean tileHasCityWithBuilding(int tileX, int tileY, int ownerId, int buildingIndex) {
        if (buildingIndex < 0 || biq == null)
            return false;
        for (int[] offset : WORK_RADIUS_OFFSETS) {
            int nx = tileX + offset[0];
            int ny = tileY + offset[1];
            int tileIndex = biq.calculateTileIndex(nx, ny);
            if (tileIndex < 0 || tileIndex >= tiles.size())
                continue;
            TILE neighbor = tiles.get(tileIndex);
            if (neighbor == null)
                continue;
            short cityId = neighbor.getCity();
            if (cityId >= 0 && cityId < city.size()) {
                CITY targetCity = city.get(cityId);
                if (targetCity != null && targetCity.getOwner() == ownerId && targetCity.hasBuilding(buildingIndex)) {
                    return true;
                }
            }
        }
        return false;
    }

    private BufferedImage fallbackSprite(DistrictSpriteSet set) {
        for (int variant = 0; variant < set.getVariantCount(); variant++) {
            for (int era = 0; era < set.getEraCount(); era++) {
                for (int column = 0; column < set.getColumnCount(); column++) {
                    BufferedImage candidate = set.getSprite(variant, era, column);
                    if (candidate != null) {
                        return candidate;
                    }
                }
            }
        }
        return null;
    }

    private static int clamp(int value, int min, int max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    private void drawLargeRuinGraphics(List<TILE> visibleTiles, Graphics bufferGraphics) {
        for (TILE t : visibleTiles) {
            drawRuins(t, horizScrollOffset, vertScrollOffset, bufferGraphics);
        }
    }

    private void drawTileElements(List<TILE> visibleTiles, Graphics bufferGraphics) {
        //Now draw the stuff that goes on top
        for (TILE t : visibleTiles) {
            //TODO: De-couple the graphics overlays from drawing directly to the buffer
            //This should hopefully make it easier to implement zoom, as I will be able to simply draw everything at half-scale onto the buffer
            //That may prove easier than the current approach of shrinking the buffer

            try {
                drawTileElements(t, horizScrollOffset, vertScrollOffset, bufferGraphics);
            }
            catch(Exception ex) {
                logger.error("Unexpected exception while rendering tile " + t.getXPos()+ ", " + t.getYPos(), ex);
                alertUserToError(ex, t.xPos, t.yPos);
            }
        }
    }

    private void drawGrid(int xIndex, int yIndex, Graphics bufferGraphics) {
        xIndex = 0;
        yIndex = 0;
        //t3h Grid, take two
        for (int i = 0; i < tiles.size(); i++)
        {
            if (tileIsVisible(xIndex, yIndex))
                drawGrid(i, xIndex, yIndex, horizScrollOffset, vertScrollOffset, bufferGraphics);
            
            //increment the tile, taking the isometric grid into account
            xIndex+=2;
            if (xIndex > wmap.width - 1)
            {
                if (xIndex%2 == 0)
                    xIndex = 1;
                else
                    xIndex = 0;
                yIndex++;
            }
        }
    }

    private void drawCityNames(Graphics bufferGraphics) {
        int xIndex;
        int yIndex;
        //City names
        if (settings.cityNamesEnabled) {
            for (int i = 0; i < city.size(); i++) {
                xIndex = city.get(i).getX();
                yIndex = city.get(i).getY();
                
                if (tileIsVisible(xIndex, yIndex)) {
                    drawCityNames(i, xIndex, yIndex, horizScrollOffset, vertScrollOffset, bufferGraphics);
                }
            }
        }
    }
    
    private void drawXYCoordinates(Graphics bufferGraphics) {
        int xIndex;
        int yIndex;
        if (settings.xyOnMapEnabled) {
            for (TILE t : tiles) {
                xIndex = t.xPos;
                yIndex = t.yPos;
                
                if (tileIsVisible(xIndex, yIndex)) {
                    drawXYCoordinates(xIndex, yIndex, horizScrollOffset, vertScrollOffset, bufferGraphics);
                }
            }
        }
    }

    private void drawStartingLocations(Graphics bufferGraphics) {
        int xIndex;
        int yIndex;
        //STARTING LOCATIONS
        for (int i = 0; i < startingLocation.size(); i++)
        {
            xIndex = startingLocation.get(i).getX();
            yIndex = startingLocation.get(i).getY();
            
            int tileLeftBoundary = xIndex*64 - 64;
            int tileRightBoundary = yIndex*32;
            
            if (tileIsVisible(xIndex, yIndex))
            {
                //TODO: Fix colors
                int cultGroup = 0;
                int owner = -1;
                int cityAge = 0;
                int color = 0;  //if it's assigned to no one, use the 1st color
                switch (startingLocation.get(i).getOwnerType())
                {
                    case CITY.OWNER_CIV:
                        owner = startingLocation.get(i).getOwner();
                        color = civ.get(owner).getDefaultColor();
                        break;
                    case CITY.OWNER_PLAYER:
                        if (startingLocation.get(i).getOwner() > -1)
                        {
                            //player.get(owner).civ will give me the civ, from which I can
                            //get the culture group
                            //If there is no custom player data, use the default color
                            if (player == null) {
                                color = 0;
                            }
                            else {
                                owner = player.get(startingLocation.get(i).getOwner()).civ;
                                if (owner != -3 && owner != -2)
                                    color = civ.get(owner).getDefaultColor();
                                else
                                    color = 0;  //default of gray or w/e 0 is
                            }
                        }
                        else
                        {
                            logger.warn("Warning! On owner of sloc " + startingLocation.get(i).toEnglish());
                        }
                        break;
                    default:
                        logger.debug("Starting Location of unrecognized ownership: " + startingLocation.get(i).toEnglish());
                }
                bufferGraphics.drawImage(assets.startLocGraphics[color], tileLeftBoundary - horizScrollOffset, tileRightBoundary - vertScrollOffset, null);
            }
        }
    }
    
    private void drawBaseTerrain(TILE tile, int horizScrollPosition, int vertScrollPosition, Graphics canvas)
    {

        int tileLeftBoundary = tile.xPos*64 - 64;
        int tileTopBoundary = tile.yPos*32 - 32;

        //So for (1, 1), the top-left corner is 0, 0.  Then the center is (64, 32)
        //figure out if it's on the buffer or not.  if a single pixel is on the buffer, draw it (hence the +128, +64)
        //base terrain
        
        int xDrawingCoordinate = tileLeftBoundary - horizScrollPosition;
        int yDrawingCoordinate = tileTopBoundary - vertScrollPosition;
        
        //logger.info("Drawing base terrain at (" + xIndex + ", " + yIndex + "); xDrawingCoordinate: " + xDrawingCoordinate + "; yDrawingCoordinate: " + yDrawingCoordinate);
        
        //logger.info("Drawing base terrain; offset: " + (gpxX - x) + ", " + (gpxY - y));
        if (!tile.isLandmark())
            canvas.drawImage(assets.baseTerrainGraphics[tile.getFile()][tile.getImage()], xDrawingCoordinate, yDrawingCoordinate, null);
        else
            canvas.drawImage(assets.lmTerrainGraphics[tile.getFile()][tile.getImage()], xDrawingCoordinate, yDrawingCoordinate, null);

    }
    
    private void drawCityNames(int i, int xpos, int ypos, int x, int y, Graphics g)
    {
        int gpxX = xpos*64 - 64;
        int gpxY = ypos*32;
        
        if (logger.isTraceEnabled())
            logger.trace("Drawing city name for " + city.get(i).getName());
        if ((gpxX - x + 50 > 0 && ((gpxX - x) + 50) < BUFFERWIDTH) && (gpxY - y + 50 > 0 && ((gpxY - y)+50) < BUFFERHEIGHT))
        {
            //Defensive bonus
            CITY thisCity = city.get(i);
            int defenseBonus = thisCity.calculateDefensiveBonus();
            
            //Length represents how much whitespace we need behind the city name
            //For English and Russian, this is straightforward enough
            //For Chinese, some characters take up two bytes, and the city name length may be less than expected
            //TODO: Tweak the rectangle length again.
            String cityInfo = city.get(i).getName() + " - " + city.get(i).getSize() + " (+" + defenseBonus + "%)";
            int length = city.get(i).getName().length()*2;
            if (settings.biqLanguage.equals("Chinese"))
            {
                length*=2;
            }
            length = length + 14;
            if (defenseBonus > 1000)
                length += 4;
            
            g.setColor(Color.WHITE);
            g.fillRect(gpxX - (x + length) + 32, gpxY - y + 48, 40 + length * 3, 20);
            g.setColor(Color.BLACK);
            g.drawString(cityInfo, gpxX - (x + length)  + 40, gpxY - y + 64);
        }
            
    }
    
    private void drawXYCoordinates(int xpos, int ypos, int xOffset, int yOffset, Graphics g)
    {
        int gpxX = xpos*64 - 64;
        int gpxY = ypos*32;
        
        if ((gpxX - xOffset + 50 > 0 && ((gpxX - xOffset) + 50) < BUFFERWIDTH) && (gpxY - yOffset + 50 > 0 && ((gpxY - yOffset)+50) < BUFFERHEIGHT))
        {
            int length = 41;
            if (xpos < 10)
                length -=6;
            if (ypos < 10)
                length -=6;
            if (xpos > 99)
                length +=8;
            if (ypos > 99)
                length +=8;
            
            g.setColor(Color.WHITE);
            g.setColor(new Color(255, 255, 255, 128));
            g.fillRect(gpxX - xOffset + 40, gpxY - yOffset + 28, length, 20);
            g.setColor(Color.BLACK);
            g.drawString(xpos + ", " + ypos, gpxX - xOffset + 44, gpxY - yOffset + 15 + 28);
        }
    }
    
    private void drawGrid(int i, int xpos, int ypos, int x, int y, Graphics g)
    {
        int gpxX = xpos*64 - 64;
        int gpxY = ypos*32;

        //if ((gpxX - x + 50 > 0 && ((gpxX - x) + 50) < BUFFERWIDTH) && (gpxY - y + 50 > 0 && ((gpxY - y)+50) < BUFFERHEIGHT))
        //{
            //g.drawChars(city.get(tile.get(i).getCity()).getName().toCharArray(),city.get(tile.get(i).getCity()).getName().length() , 0, gpxX, gpxY);
            g.setColor(Color.DARK_GRAY);
            //Left mid to mid lower
            g.drawLine(gpxX - x, gpxY - 32 -y, gpxX+64 - x, gpxY - y);
            //left mid to mid upper
            g.drawLine(gpxX - x, gpxY - 32-y, gpxX+64 - x, gpxY-64 -y);
            //g.fillRect(gpxX - (x + city.get(i).getName().length() * 2) + 32, gpxY - y + 48, 40 + city.get(i).getName().length() * 6, 20);
            //g.setColor(Color.BLACK);
            //g.drawString(city.get(i).getName() + " - " + city.get(i).getSize(), gpxX - (x + city.get(i).getName().length() * 2)  + 40, gpxY - y + 64);
        //}
    }
    
    Set encounteredExceptions = new HashSet();
    
    private void alertUserToError(Exception ex, int xindex, int yindex) {
        if (!encounteredExceptions.contains(ex.getStackTrace()[0])) {
            JOptionPane.showMessageDialog(null, "<html>An unexpected map rendering error was encountered.  "
                    + "<br/>Though the editor will generally keep working, you may encounter issues on the tile (" + xindex + ", " + yindex + ") that caused the error.  "
                    + "<br/>This error may occur on other tiles as well, and those will be logged, but pop-ups will be suppressed unless a different error occurs, so you can still get some work done.  "
                    + "<br/>The details are in log.txt, and reporting them would be appreciated.", "Unexpected rendering error", JOptionPane.ERROR_MESSAGE);
            encounteredExceptions.add(ex.getStackTrace()[0]);
        }
    }
    
    /**
     * Draws terrain features that appear above the base terrain.
     * @param tileIndex - The tile index.
     * @param xIndex - The x position of the tile within the drawing as a whole.  0 indicates the leftmost tile.
     *     Note that while this could be derived from the tile index, if we are just drawing one tile by itself, we may want to pass in 
     *     a fake value of zero for this parameter.
     * @param yIndex - The y position of the tile within the graphics.  0 indicates the topmost tile.
     *     Note that while this could be derived from the tile index, if we are just drawing one tile by itself, we may want to pass in 
     *     a fake value of zero for this parameter.
     * @param horizScrollPosition - How far to the right the map is currently scrolled in pixels.
     * @param vertScrollPosition - How far down the map is currently scrolled in pixels
     * @param canvas - Where we are drawing the forest/jungle.
     */
    private void drawTileElements(TILE tile, int horizScrollPosition, int vertScrollPosition, Graphics canvas)
    {
        int defaultXPosition = tile.xPos*64 - 64 - horizScrollPosition;
        int defaultYPosition = tile.yPos*32 - vertScrollPosition;
        
        if (settings.woodlandsEnabled)
            drawForestJungle(tile, defaultXPosition, defaultYPosition, canvas);

        if (settings.hillsEnabled)
            drawHillyTerrain(tile, defaultXPosition, defaultYPosition, canvas);

        if (settings.marshEnabled) {
            if (tile.getRealTerrain() == TERR.MARSH) {
                BufferedImage randomMarsh = assets.largeMarsh[(tile.xPos + tile.yPos)%8];
                canvas.drawImage(randomMarsh, defaultXPosition, defaultYPosition, null);
            }
        }

        if (settings.irrigationEnabled) {
            if (maskSet(tile.C3COverlays, TILE.IRRIGATION_MASK)) {
                drawIrrigationGraphics(tile, defaultXPosition, defaultYPosition, canvas);
            }
        }

        if (settings.roadsEnabled) {
            if (maskSet(tile.C3COverlays, TILE.ROAD_MASK)) {
                int roadIndex = calculateRoadImageIndex(tile.xPos, tile.yPos, TILE.ROAD_MASK);
                BufferedImage roadGraphic = assets.roadGraphics[roadIndex];
                canvas.drawImage(roadGraphic, defaultXPosition, defaultYPosition, null);
            }

            if (maskSet(tile.C3COverlays, TILE.RAILROAD_MASK)) {
                int railIndex = calculateRoadImageIndex(tile.xPos, tile.yPos, TILE.RAILROAD_MASK);
                BufferedImage railGraphic = assets.roadGraphics[railIndex];
                canvas.drawImage(railGraphic, defaultXPosition, defaultYPosition, null);
            }
        }

        if (settings.colonialBuildingsEnabled)
            drawColonies(tile, defaultXPosition, defaultYPosition, canvas);

        if (settings.minesEnabled) {
            if (maskSet(tile.C3COverlays, TILE.MINE_MASK)) {
                canvas.drawImage(assets.buildingGraphics[6], defaultXPosition, defaultYPosition, null);
            }
        }

        if (settings.riversEnabled)
            drawRivers(tile, horizScrollPosition, vertScrollPosition, canvas);

        if (settings.barbarianCampsEnabled) {
            if (maskSet(tile.C3COverlays, TILE.BARBARIAN_CAMP_MASK)) {
                canvas.drawImage(assets.buildingGraphics[2], defaultXPosition, defaultYPosition, null);
            }
        }

        if (settings.goodyHutsEnabled) {
            if (maskSet(tile.C3COverlays, TILE.GOODY_HUT_MASK)) {
                canvas.drawImage(assets.goodyhutGraphics[(tile.xPos + tile.yPos)%8], defaultXPosition, defaultYPosition, null);
            }
        }
        
        if (true)  //TODO: Condition
            drawTNT(tile, horizScrollPosition, vertScrollPosition, canvas);

        if (settings.fortificationsEnabled)
            drawFortifications(tile, defaultXPosition, defaultYPosition, canvas);

        if (settings.pollutionEnabled)
            drawPollution(tile, defaultXPosition, defaultYPosition, canvas);

        if (settings.cratersEnabled)
            drawCraters(tile, defaultXPosition, defaultYPosition, canvas);

        if (settings.ruinsEnabled)
            if (!assets.largeRuinGraphics)
                drawRuins(tile, horizScrollPosition, vertScrollPosition, canvas);

        if (settings.bordersEnabled)
            drawBorders(tile, horizScrollPosition, vertScrollPosition, canvas);

        if (settings.citiesEnabled) {
            if (tile.getCity() > -1) {
                drawCities(tile, defaultXPosition, defaultYPosition, canvas);
            }
        }

        if (settings.vplsEnabled) {
            if (tile.getVictoryPointLocation() == 0) {
                canvas.drawImage(assets.victoryPointGraphics, defaultXPosition, defaultYPosition, null);
            }
        }
        
        if (true) { //landmark marker 
            if (tile.isLandmark()) {
                canvas.drawImage(assets.landmarkOverlay, defaultXPosition, defaultYPosition, null);
            }
        }

        if (settings.resourcesEnabled) {
            if (tile.resourceInt != -1) {
                //Not entirely sure why 40/9 is the offset, but have verified that is is.
                canvas.drawImage(assets.resourceGraphics[resource.get(tile.resourceInt).getIcon()], defaultXPosition + 40, defaultYPosition + 9, null);
            }
        }

        if (tile.hasDistrict()) {
            if (!drawDistrict(tile, defaultXPosition, defaultYPosition, canvas)) {
                drawDistrictPlaceholder(tile, defaultXPosition, defaultYPosition, canvas);
            }
        }

        if (settings.unitsEnabled)
            drawUnits(tile, horizScrollPosition, vertScrollPosition, canvas);
        
        if (settings.showFog) {
            if (tile.getFogOfWar() == 0) {
                canvas.drawImage(assets.fog, defaultXPosition, defaultYPosition, null);
            }
        }
    }

    private static boolean maskSet(int field, int MASK) {
        return (field & MASK) == MASK;
    }

    private void drawIrrigationGraphics(TILE tile, int defaultXPosition, int defaultYPosition, Graphics canvas) {
        int irrigationIndex = getIrrigrationIndex(tile.xPos, tile.yPos);
        
        if (tile.getBaseTerrain() == TERR.DESERT)
            canvas.drawImage(assets.desertIrrigationGraphics[irrigationIndex], defaultXPosition, defaultYPosition, null);
        else if (tile.getBaseTerrain() == TERR.PLAINS)
            canvas.drawImage(assets.plainsIrrigationGraphics[irrigationIndex], defaultXPosition, defaultYPosition, null);
        else if(tile.getBaseTerrain() == TERR.GRASSLAND)
            canvas.drawImage(assets.irrigationGraphics[irrigationIndex], defaultXPosition, defaultYPosition, null);
        else if(tile.getBaseTerrain() == TERR.TUNDRA)
            canvas.drawImage(assets.tundraIrrigationGraphics[irrigationIndex], defaultXPosition, defaultYPosition, null);
        //else we're irrigating the sea.  d'oh!
        else
            logger.warn("Water tile being irrigated.  Position: " + tile.xPos + ", " + tile.yPos);
    }
    /**
     * Draws forests and jungles on the map.
     * @param tileIndex - The tile index.
     * @param xIndex - The x position of the tile within the drawing as a whole.  0 indicates the leftmost tile.
     *     Note that while this could be derived from the tile index, if we are just drawing one tile by itself, we may want to pass in 
     *     a fake value of zero for this parameter.
     * @param yIndex - The y position of the tile within the graphics.  0 indicates the topmost tile.
     *     Note that while this could be derived from the tile index, if we are just drawing one tile by itself, we may want to pass in 
     *     a fake value of zero for this parameter.
     * @param horizScrollPosition - How far to the right the map is currently scrolled in pixels.
     * @param vertScrollPosition - How far down the map is currently scrolled in pixels
     * @param canvas - Where we are drawing the forest/jungle.
     */
    private void drawForestJungle(TILE tile, int xPosition, int yPosition, Graphics canvas) {
        int xpos = tile.xPos;
        int ypos = tile.yPos;
        
        if (tile.getRealTerrain() == TERR.FOREST || tile.getRealTerrain() == TERR.JUNGLE) {            
            Image toDraw = null;
            
            if (tile.getRealTerrain() == TERR.JUNGLE)
                toDraw = assets.largeJungle[(xpos + ypos)%8];
            else if(maskSet(tile.C3CBonuses, TILE.PINE_FOREST)) {
                if (tile.getBaseTerrain() == TERR.PLAINS)
                    toDraw = assets.plainsPineForest[(xpos + ypos)%12];
                else if(tile.getBaseTerrain() == TERR.GRASSLAND)
                    toDraw = assets.grassPineForest[(xpos + ypos)%12];
                else if(tile.getBaseTerrain() == TERR.TUNDRA)
                    toDraw = assets.tundraPineForest[(xpos + ypos)%12];
            }
            else if(tile.getBaseTerrain() == TERR.PLAINS)
                toDraw = assets.largePlainsForest[(xpos + ypos)%8];
            else if(tile.getBaseTerrain() == TERR.GRASSLAND)
                toDraw = assets.largeGrassForest[(xpos + ypos)%8];
            else if(tile.getBaseTerrain() == TERR.TUNDRA)
                toDraw = assets.largeTundraForest[(xpos + ypos)%8];
            
            
            canvas.drawImage(toDraw, xPosition, yPosition, null);
        }
    }

    private void drawHillyTerrain(TILE tile, int defaultXPosition, int defaultYPosition, Graphics canvas) {
        final int HILLS_Y_POS = defaultYPosition - 12;
        final int MOUNTAINS_Y_POS = defaultYPosition - 24;
        
        if (tile.getRealTerrain() == TERR.MOUNTAIN || tile.getRealTerrain() == TERR.HILLS || tile.getRealTerrain() == TERR.VOLCANO) {
            int graphicsIndex = getMountainIndex(tile.xPos, tile.yPos);
            int forestJungleCheck = useForestOrJungleHillVariant(tile);

            if (tile.getRealTerrain() == TERR.HILLS) {
                if (tile.isLandmark()) {
                    canvas.drawImage(assets.lmHillGraphics[graphicsIndex], defaultXPosition, HILLS_Y_POS, null);
                }
                else {
                    if (forestJungleCheck == TERR.FOREST) {
                        canvas.drawImage(assets.forestHillGraphics[graphicsIndex], defaultXPosition, HILLS_Y_POS, null);
                    }
                    else if (forestJungleCheck == TERR.JUNGLE) {
                        canvas.drawImage(assets.jungleHillGraphics[graphicsIndex], defaultXPosition, HILLS_Y_POS, null);
                    }
                    else {
                        canvas.drawImage(assets.hillGraphics[graphicsIndex], defaultXPosition, HILLS_Y_POS, null);
                    }
                }
            }
            else if (tile.getRealTerrain() == TERR.VOLCANO) {
                if (forestJungleCheck == TERR.FOREST) {
                    canvas.drawImage(assets.forestVolcanoGraphics[graphicsIndex], defaultXPosition, MOUNTAINS_Y_POS, null);
                }
                else if (forestJungleCheck == TERR.JUNGLE) {
                    canvas.drawImage(assets.jungleVolcanoGraphics[graphicsIndex], defaultXPosition, MOUNTAINS_Y_POS, null);
                }
                else {
                    canvas.drawImage(assets.volcanoGraphics[graphicsIndex], defaultXPosition, MOUNTAINS_Y_POS, null);
                }
            }
            else if (tile.getRealTerrain() == TERR.MOUNTAIN) {
                if (tile.isLandmark())
                    canvas.drawImage(assets.lmMountainGraphics[graphicsIndex], defaultXPosition, MOUNTAINS_Y_POS, null);
                else if (maskSet(tile.C3CBonuses, TILE.SNOW_CAPPED_MOUNTAIN))
                    canvas.drawImage(assets.snowMountainGraphics[graphicsIndex], defaultXPosition, MOUNTAINS_Y_POS, null);
                else if (forestJungleCheck == TERR.FOREST)
                    canvas.drawImage(assets.forestMountainGraphics[graphicsIndex], defaultXPosition, MOUNTAINS_Y_POS, null);
                else if (forestJungleCheck == TERR.JUNGLE)
                    canvas.drawImage(assets.jungleMountainGraphics[graphicsIndex], defaultXPosition, MOUNTAINS_Y_POS, null);
                else
                    canvas.drawImage(assets.mountainGraphics[graphicsIndex], defaultXPosition, MOUNTAINS_Y_POS, null);
            }
        }
    }
    
    private int useForestOrJungleHillVariant(TILE tile) {
        TILE northEast = tile.neighbor(MapDirection.NORTHEAST);
        TILE southEast = tile.neighbor(MapDirection.SOUTHEAST);
        TILE southWest = tile.neighbor(MapDirection.SOUTHWEST);
        TILE northWest = tile.neighbor(MapDirection.NORTHWEST);
        
        int hillyNeighborCount = 0;
        int forestNeighborCount = 0;
        int jungleNeighborCount = 0;
        
        List<TILE> neighbors = new ArrayList<>();
        neighbors.add(northEast);
        neighbors.add(southEast);
        neighbors.add(southWest);
        neighbors.add(northWest);
        
        for (TILE t : neighbors) {
            if (t == null) {    //edge of map
                continue;
            }
            if (t.getRealTerrain() == TERR.HILLS || t.getRealTerrain() == TERR.MOUNTAIN || t.getRealTerrain() == TERR.VOLCANO) {
                hillyNeighborCount++;
            }
            else if (t.getRealTerrain() == TERR.FOREST) {
                forestNeighborCount++;
            }
            else if (t.getRealTerrain() == TERR.JUNGLE) {
                jungleNeighborCount++;
            }
        }
        
        if (forestNeighborCount == 0 && jungleNeighborCount == 0) {
            return -1;
        }
        if (forestNeighborCount + jungleNeighborCount + hillyNeighborCount < 4) {
            return -1;
        }
        if (forestNeighborCount > jungleNeighborCount) {
            return TERR.FOREST;
        }
        if (jungleNeighborCount > forestNeighborCount) {
            return TERR.JUNGLE;
        }
        //If we get here, it's a tie between forest and jungle.  Deterministically choose one so it doesn't change on every render
        if (tile.xPos % 2 == 0) {
            return TERR.FOREST;
        }
        return TERR.JUNGLE;
    }

    private int getMountainIndex(int xIndex, int yIndex) {
        //TODO: This can probably be combined with getIrrigationIndex with the help of functional operations
        int mtnIndex = 0;
        //check for nulls first.
        //This would be nice and easy with Groovy's nullsafe dot operator
        //Unfortunately Civ seems to only use this as an occasionally way to do things
        //At first it appeared Snow Mtns didn't count hills as neighbors, but that didn't solve it either
        //Annoying Mountains.biq illustrates the madness of deciding which mtn is which
        TILE center = getTile(xIndex, yIndex);
        TILE northEast = center.neighbor(MapDirection.NORTHEAST);
        TILE southEast = center.neighbor(MapDirection.SOUTHEAST);
        TILE southWest = center.neighbor(MapDirection.SOUTHWEST);
        TILE northWest = center.neighbor(MapDirection.NORTHWEST);
        if (northEast == null)
            ;
        else if(northEast.getRealTerrain() == TERR.MOUNTAIN || northEast.getRealTerrain() == TERR.HILLS  || northEast.getRealTerrain() == TERR.VOLCANO)
            mtnIndex+=2;     //Northeast
        if (southWest == null)
            ;
        else if(southWest.getRealTerrain() == TERR.MOUNTAIN || southWest.getRealTerrain() == TERR.HILLS || southWest.getRealTerrain() == TERR.VOLCANO)
            mtnIndex+=4;     //Southwest
        if (southEast == null)
            ;
        else if(southEast.getRealTerrain() == TERR.MOUNTAIN || southEast.getRealTerrain() == TERR.HILLS || southEast.getRealTerrain() == TERR.VOLCANO)
            mtnIndex+=8;
        if (northWest == null)
            ;
        else if(northWest.getRealTerrain() == TERR.MOUNTAIN || northWest.getRealTerrain() == TERR.HILLS || northWest.getRealTerrain() == TERR.VOLCANO)
            mtnIndex++;
        return mtnIndex;
    }

    private int getIrrigrationIndex(int xIndex, int yIndex) {
        int irrigationIndex = 0;
        
        TILE center = getTile(xIndex, yIndex);
        TILE northEast = center.neighbor(MapDirection.NORTHEAST);
        TILE southEast = center.neighbor(MapDirection.SOUTHEAST);
        TILE southWest = center.neighbor(MapDirection.SOUTHWEST);
        TILE northWest = center.neighbor(MapDirection.NORTHWEST);
        
        if (northEast == null)
            ;
        else if((northEast.C3COverlays & 0x00000008) == 8)
            irrigationIndex+=2;     //Northeast
        
        if (southWest == null)
            ;
        else if((southWest.C3COverlays & 0x00000008) == 8)
            irrigationIndex+=4;     //Southwest
        
        if (southEast == null)
            ;
        else if((southEast.C3COverlays & 0x00000008) != 8)
            irrigationIndex+=8;
        
        if (northWest == null)
            ;
        else if((northWest.C3COverlays & 0x00000008) != 8)
            irrigationIndex++;
        return irrigationIndex;
    }

    private int calculateRoadImageIndex(int xpos, int ypos, int ROAD_MASK) {
        int roadIndex = 0;
        if (getTile(xpos + 1, ypos - 1) == null)
            ;
        else if((getTile(xpos + 1, ypos - 1).C3COverlays & ROAD_MASK) == ROAD_MASK)
            roadIndex++;
        if (getTile(xpos + 2, ypos) == null)
            ;
        else if((getTile(xpos + 2, ypos).C3COverlays & ROAD_MASK) == ROAD_MASK)
            roadIndex+=2;
        if (getTile(xpos + 1, ypos + 1) == null)
            ;
        else if ((getTile(xpos + 1, ypos + 1).C3COverlays & ROAD_MASK) == ROAD_MASK)
            roadIndex+=4;
        if (getTile(xpos, ypos + 2) == null)
            ;
        else if ((getTile(xpos, ypos + 2).C3COverlays & ROAD_MASK) == ROAD_MASK)
            roadIndex+=8;
        if (getTile(xpos - 1, ypos + 1) == null)
            ;
        else if ((getTile(xpos - 1, ypos + 1).C3COverlays & ROAD_MASK) == ROAD_MASK)
            roadIndex+=16;
        if (getTile(xpos -2, ypos) == null)
            ;
        else if ((getTile(xpos -2, ypos).C3COverlays & ROAD_MASK) == ROAD_MASK)
            roadIndex+=32;
        if (getTile(xpos - 1, ypos - 1) == null)
            ;
        else if ((getTile(xpos - 1, ypos - 1).C3COverlays & ROAD_MASK) == ROAD_MASK)
            roadIndex+=64;
        if (getTile(xpos, ypos - 2) == null)
            ;
        else if ((getTile(xpos, ypos - 2).C3COverlays & ROAD_MASK) == ROAD_MASK)
            roadIndex+=128;
        return roadIndex;
    }
    private void drawColonies(TILE tile, int defaultXPosition, int defaultYPosition, Graphics g)
    {
        //Colonies/radar towers/airfields/outposts
        if (tile.getColony() != -1)
        {
            int colIndex = tile.getColony();
            
            int owner = -1;
            int colonyAge = 0;
            //figure out which player is t3e ownzzor
            //TODO: Merge this code with the code that does the same thing for cities
            switch (colony.get(colIndex).getOwnerType())
            {
                case CITY.OWNER_CIV:
                    owner = colony.get(colIndex).getOwner();
                    //see if there is a player assigned to that owner
                    if (biq.hasCustomPlayerData())
                    {
                        for (int p = 0; p < player.size(); p++)
                        {
                            if (player.get(p).civ == owner)
                            {
                                //If they start in the Future Era, use Modern Era graphics
                                colonyAge = player.get(p).initialEra == 4? 3 : player.get(p).initialEra;
                                break;
                            }
                        }
                    }
                    break;
                case CITY.OWNER_PLAYER:
                    if (colony.get(colIndex).getOwner() > -1)
                    {
                        //player.get(owner).civ will give me the civ, from which I can
                        //get the culture group
                        colonyAge = player.get(colony.get(colIndex).getOwner()).initialEra;
                    }
                    else
                    {
                        logger.warn("Warning! On owner of CLNY " + colony.get(colIndex).toEnglish());
                    }
                    break;
                default:
                    logger.warn("Colony of unrecognized ownership: " + colony.get(colIndex).toEnglish());
            }
            int colonyType = colony.get(colIndex).getImprovementType();
            //Add one to get to the colony column
            if (colonyType == CLNY.COLONY)
                g.drawImage(assets.buildingGraphics[4*colonyAge + 1], defaultXPosition, defaultYPosition, null);
            else if (colonyType == CLNY.RADAR_TOWER)
                g.drawImage(assets.radarTowerGraphics, defaultXPosition, defaultYPosition - 64, null);
            else if (colonyType == CLNY.OUTPOST) {
                //We only have 3 graphics, we use the 3rd for indust/mod/future
                if (colonyAge > 2)
                    colonyAge = 2;
                g.drawImage(assets.outpostGraphics[colonyAge], defaultXPosition, defaultYPosition - 64, null);
            }
            else if (colonyType == CLNY.AIRFIELD)
            {
                //ancient/medieval have one airfield, indust. or later another
                if (colonyAge < 2)
                    g.drawImage(assets.airfieldGraphics[0], defaultXPosition, defaultYPosition, null);
                else
                    g.drawImage(assets.airfieldGraphics[1], defaultXPosition, defaultYPosition, null);

            }
        }
    }

    /**
     * It's kind of bloody complicated to figure out how rivers are stored.  Let's start with them 'normal case',
     * that is, not if (tile.get(i).xPos == 1).  This will be used for all tiles that aren't the westernmost on the
     * map.
     * What we're actually doing is looking at where the rivers go from the very EASTERNMOST part of the tile.  So, in
     * illustration, we are looking at where rivers go from here:
     *   <b> Image 1 </b>
     *         ^
     *        / \
     *       <   X      <we're where the x is, relative to tile.get(i)
     *        \ /
     *         .
     * So we first look at whether the tile to the north of the X
     * has a connection to the SW.  This tells us whether there is a river portion here:
     *  <b> Image 2 </b>
     *         ^
     *        / X
     *       <   >      <-- relative to our original tile.get(i)
     *        \ /
     *         .
     * If so, this means the river "goes northwest" from the X in Image 1.
     * We now look at whether the tile directly east of tile.get(i) has a connection to <i>its</i> northwest.  Thus,
     *  <b> Image 3 </b>
     *         ^    ^
     *        / \  X \
     *       <   ><   >   <-- left tile is tile.get(i)
     *        \ /  \ /
     *         .    .
     * If so, this means the river "goes northeast" from the X in Image 1.
     * And so on for whether it "goes southwest" and "goes southeast".
     *
     * What has not been accounted for is rivers that 'go nowhere'.  These can still exist in C3CEdit,
     * but this method does not detect them.  I think part of C3CBonuses accounts for this.
     */
    private void drawRivers(TILE tile, int x, int y, Graphics g)
    {
        int gpxX = tile.xPos*64 - 64;
        int gpxY = tile.yPos*32;

        //rivers
        //TODO: Deltas
        //TODO: Use neighbor functionality
        //ok, we'll start with the intersection at the E of east tile - if this is a tile row 1, also do the W.
        if (tile.xPos == 1)  //if this is an xPos = 0, the tile W isn't visible
        {
            int northTile = biq.calculateTileIndex(tile.xPos - 1, tile.yPos - 1);
            int eastTile = tile.index;   //this tile
            int southTile = biq.calculateTileIndex(tile.xPos - 1, tile.yPos + 1);
            int westTile = -1;
            //there is no west tile, as this is the westernmost tile
            int riverImageIndex = getRiverImageIndex(northTile, eastTile, westTile, southTile);
            //Now draw it.  Remember, we're 64 pixels W of normal
            if (riverImageIndex != 0) {
                g.drawImage(assets.riverGraphics[riverImageIndex], gpxX - x - 64, gpxY - y, null);
            }
        }
        //Now the normal case, where we look to the east
        
        //TODO: This looks suspicious...
        {
            int northTile = -1;
            if (tile.yPos > 1)
                northTile = biq.calculateTileIndex(tile.xPos + 1, tile.yPos - 1);
            int eastTile = -1;
            if (tile.xPos < wmap.width - 2)  //if width = 136, we need < 134 to have an E tile
                eastTile = biq.calculateTileIndex(tile.xPos + 2, tile.yPos);
            int southTile = -1;
            if (tile.yPos < wmap.height -2)
                southTile = biq.calculateTileIndex(tile.xPos + 1, tile.yPos + 1);
            int westTile = tile.index;
            int riverImageIndex = getRiverImageIndex(northTile, eastTile, westTile, southTile);
            //Now draw it.  Remember, we're 64 pixels E of normal
            if (riverImageIndex != 0) {
                if (isADelta(riverImageIndex, westTile, northTile, eastTile, southTile)) {
                    g.drawImage(assets.deltaGraphics[riverImageIndex], gpxX - x + 64, gpxY - y, null);
                }
                else {
                    g.drawImage(assets.riverGraphics[riverImageIndex], gpxX - x + 64, gpxY - y, null);
                }
            }
        }
    }
    
    private boolean isADelta(int riverImageIndex, int westTile, int northTile, int eastTile, int southTile) {
        List<Integer> possibleDeltaIndices = Arrays.asList(1, 2, 4, 8);
        if (!possibleDeltaIndices.contains(riverImageIndex)) {
            return false;
        }
        if (westTile == -1 || northTile == -1 || eastTile == -1 || southTile == -1) {
            return false;
        }
        TILE west = tiles.get(westTile);
        TILE north = tiles.get(northTile);
        TILE east = tiles.get(eastTile);
        TILE south = tiles.get(southTile);
        switch(riverImageIndex) {
            case 1:
                return east.isWater() && south.isWater();
            case 2:
                return west.isWater() && south.isWater();
            case 4:
                return north.isWater() && east.isWater();
            case 8:
                return west.isWater() && north.isWater();
            default:
                return false;
        }
    }

    private int getRiverImageIndex(int northTile, int eastTile, int westTile, int southTile) {
        int riverImageIndex = 0;
        //Tell if it goes NW by whether the N tile has a SW connection
        if (northTile != -1 && (tiles.get(northTile).hasRiverConnection(RIVER_SOUTHWEST)))
            riverImageIndex++;
        //Tell if it goes to the NE by whether the east tile has a NW
        if (eastTile != -1 && (tiles.get(eastTile).hasRiverConnection(RIVER_NORTHWEST)))
            riverImageIndex+=2;
        //Tell if it goes to the SW by whether the W tile has a SE
        if (westTile != -1 && (tiles.get(westTile).hasRiverConnection(RIVER_SOUTHEAST)))
            riverImageIndex+=4;
        //Tell if it goes to the SE by whether the S tile has a NE
        if (southTile != -1 && (tiles.get(southTile).hasRiverConnection(RIVER_NORTHEAST)))
            riverImageIndex+=8;
        return riverImageIndex;
    }

    private void drawFortifications(TILE tile, int defaultXPosition, int defaultYPosition, Graphics g) {        
        //forts
        if (maskSet(tile.C3COverlays, TILE.FORT_MASK)) {
            int age = getFortAge(tile);
            g.drawImage(assets.buildingGraphics[age*4], defaultXPosition, defaultYPosition, null);
        }

        //Barricades
        if (maskSet(tile.C3COverlays, TILE.BARRICADE_MASK)) {
            int age = getFortAge(tile);
            g.drawImage(assets.buildingGraphics[age*4 + 3], defaultXPosition, defaultYPosition, null);
        }

    }

    private int getFortAge(TILE tile) {
        int owner = tile.owner;
        //figure out which player is t3e ownzzor
        int playerOwner = -1;
        //if there are custom rules
        if (biq.hasCustomPlayerData()) {
            for (int j = 0; j < player.size(); j++) {
                if (player.get(j).civ == owner) {
                    playerOwner = j;
                    break;
                }
            }
        }
        int age = (playerOwner == -1)? 0 : player.get(playerOwner).initialEra;
        //4 era max.  There aren't graphics for the Future Era, so return the last one instead.
        if (age > 3) {
            return 3;
        }
        return age;
    }

    private void drawPollution(TILE tile, int defaultXPosition, int defaultYPosition, Graphics g) {
        int xpos = tile.xPos, ypos = tile.yPos;
        if (maskSet(tile.C3COverlays, TILE.POLLUTION_MASK)) {
            int pollutionIndex = getNeighborInfluencedOverlayGraphicsIndex(xpos, ypos, TILE.POLLUTION_MASK);
            //There are 10 "no-surrounding" pollution images; hence the offset of 9, plus at least one more
            if (pollutionIndex != 0) {
                g.drawImage(assets.pollutionGraphics[pollutionIndex + 9], defaultXPosition, defaultYPosition, null);
            }
            else {
                g.drawImage(assets.pollutionGraphics[(xpos + ypos)%10], defaultXPosition, defaultYPosition, null);
            }
        }
    }

    private void drawBorders(TILE tile, int x, int y, Graphics g)
    {
        int gpxX = tile.xPos*64 - 64;
        int gpxY = tile.yPos*32;

        if (tile.owner != -1) {
            int owner = tile.owner;
            int borderColors = tile.borderColor;
            boolean nwBorder = false, neBorder = false, swBorder = false, seBorder = false;
            if (tile.neighbor(NORTHWEST) != null)
                nwBorder = tile.neighbor(NORTHWEST).owner != owner;
            if (tile.neighbor(NORTHEAST) != null)
                neBorder = tile.neighbor(NORTHEAST).owner != owner;
            if (tile.neighbor(SOUTHWEST) != null)
                swBorder = tile.neighbor(SOUTHWEST).owner != owner;
            if (tile.neighbor(SOUTHEAST) != null)
                seBorder = tile.neighbor(SOUTHEAST).owner != owner;
            if (nwBorder)
                g.drawImage(assets.borderGraphics[borderColors][0], gpxX - x, gpxY - y - 12, null);
            if (neBorder)
                g.drawImage(assets.borderGraphics[borderColors][2], gpxX - x, gpxY - y - 12, null);
            if (swBorder)
                g.drawImage(assets.borderGraphics[borderColors][4], gpxX - x, gpxY - y - 12, null);
            if (seBorder)
                g.drawImage(assets.borderGraphics[borderColors][6], gpxX - x, gpxY - y - 12, null);
        }
    }

    private void drawCities(TILE tile, int defaultXPosition, int defaultYPosition, Graphics g) {
        //figure out if it's walled or not
        CITY thisCity = city.get(tile.getCity());

        //We need to figure out who the owner is and what their culture group is
        int cultGroup = 0;
        int owner = -1;
        int cityAge = 0;
        switch (thisCity.getOwnerType())
        {
            case CITY.OWNER_CIV:
                owner = thisCity.getOwner();
                cultGroup = civ.get(owner).getCultureGroup();
                //TODO: no custom player data support
                //If we've got custom player data, go until the player who is that civ and use their era
                //If no such player is found, the age is implicitly the lowest possible
                if (biq.hasCustomPlayerData()) {
                    for (int p = 0; p < player.size(); p++)
                    {
                        if (player.get(p).civ == owner)
                        {
                            cityAge = player.get(p).initialEra == 4? 3 : player.get(p).initialEra;
                            break;
                        }
                    }
                }
                break;
            case CITY.OWNER_PLAYER:
                if (thisCity.getOwner() > -1) {
                    //player.get(owner).civ will give me the civ, from which I can
                    //get the culture group
                    if (biq.hasCustomPlayerData()) {
                        owner = player.get(thisCity.getOwner()).civ;
                        //If Any or Random, default to two (Mediterranean) to match Firaxis
                        if (owner > -1) {
                            cultGroup = civ.get(owner).getCultureGroup();
                        }
                        else {
                            cultGroup = 2;
                        }
                        cityAge = player.get(thisCity.getOwner()).initialEra == 4? 3 : player.get(thisCity.getOwner()).initialEra;
                    }
                }
                else {
                    logger.warn("Warning! On owner of city " + thisCity.getName());
                }
                break;
            default:
                logger.warn("City of unrecognized ownership: " + thisCity.getName());
        }
        
        //Set cultGroup to 0 if the culture is none to avoid annoying error messages
        if (cultGroup == -1) {
            cultGroup = 0;
        }
        
        BufferedImage cityGraphics = null;
        if (cityGraphicsHaveWalls(thisCity)) {   
            //positioning not quite perfect yet but within a couple pixels each direction
            cityGraphics = assets.wallCityGraphics[cultGroup][cityAge];
        }
        else {
            int size = -1;
            if (thisCity.getSize() <= rule.get(0).getMaxCity1Size())
                size = 0;
            else if (thisCity.getSize() <= rule.get(0).getMaxCity2Size())
                size = 1;
            else
                size = 2;
            if (size < 2 && thisCity.isCapital()) {
                size++;
            }
            cityGraphics = assets.noWallCityGraphics[cultGroup][cityAge][size];
        }
        
        int heightOffset = 0;
        int widthOffset = 0;
        if (cityGraphics.getHeight() > 95) {
            heightOffset = (cityGraphics.getHeight() - 95)/2;
        }
        if (cityGraphics.getWidth() > 167) {
            widthOffset = (cityGraphics.getWidth() - 167)/2;
        }
        g.drawImage(cityGraphics, defaultXPosition - 18 - widthOffset, defaultYPosition - 18 - heightOffset, null);
    }

    private boolean cityGraphicsHaveWalls(CITY thisCity) {
        return thisCity.getSize() <= rule.get(0).getMaxCity1Size() && thisCity.hasWallStyleBuilding();
    }
    
    private void drawTNT(TILE tile, int horizScrollPosition, int vertScrollPosition, Graphics canvas)
    {
        int tileLeftBoundary = tile.xPos*64 - 64;
        int tileTopBoundary = tile.yPos*32 - 32;

        //So for (1, 1), the top-left corner is 0, 0.  Then the center is (64, 32)
        //figure out if it's on the buffer or not.  if a single pixel is on the buffer, draw it (hence the +128, +64)
        //base terrain
        
        int xDrawingCoordinate = tileLeftBoundary - horizScrollPosition;
        int yDrawingCoordinate = tileTopBoundary - vertScrollPosition + 32;
        
        //logger.info("Drawing base terrain at (" + xIndex + ", " + yIndex + "); xDrawingCoordinate: " + xDrawingCoordinate + "; yDrawingCoordinate: " + yDrawingCoordinate);
        
        //logger.info("Drawing base terrain; offset: " + (gpxX - x) + ", " + (gpxY - y));
        
        int idx = tile.index % 3;
        
        if (Settings.showFoodAndShields) {
            if (tile.getRealTerrain() == TERR.PLAINS)
                canvas.drawImage(assets.tntPlains[idx], xDrawingCoordinate, yDrawingCoordinate, null);
            else if (tile.getRealTerrain() == TERR.DESERT)
                canvas.drawImage(assets.tntDesert[idx], xDrawingCoordinate, yDrawingCoordinate, null);
            else if (tile.getRealTerrain() == TERR.FLOODPLAIN)
                canvas.drawImage(assets.tntFloodPlains[idx], xDrawingCoordinate, yDrawingCoordinate, null);
            else if (tile.getRealTerrain() == TERR.TUNDRA)
                canvas.drawImage(assets.tntTundra[idx], xDrawingCoordinate, yDrawingCoordinate, null);
            else if (tile.getRealTerrain() == TERR.GRASSLAND) {
                if (tile.hasBonusGrassland())
                    canvas.drawImage(assets.tntGrassShield[idx], xDrawingCoordinate, yDrawingCoordinate, null);
                else
                    canvas.drawImage(assets.tntGrass[idx], xDrawingCoordinate, yDrawingCoordinate, null);
            }
        }
        else {
            if (tile.getRealTerrain() == TERR.GRASSLAND && tile.hasBonusGrassland()) {
                canvas.drawImage(assets.tntDesert[idx], xDrawingCoordinate, yDrawingCoordinate, null);
            }
        }
    }

    private void drawCraters(TILE tile, int defaultXPosition, int defaultYPosition, Graphics g) {
        if (maskSet(tile.C3COverlays, TILE.CRATER_MASK)) {
            int craterIndex = getNeighborInfluencedOverlayGraphicsIndex(tile.xPos, tile.yPos, TILE.CRATER_MASK);
            //There are 10 "no-surrounding" pollution images; hence the offset of 9, plus at least one more
            if (craterIndex != 0) {
                g.drawImage(assets.craterGraphics[craterIndex + 9], defaultXPosition, defaultYPosition, null);
            }
            else {
                g.drawImage(assets.craterGraphics[(tile.xPos + tile.yPos)%10], defaultXPosition, defaultYPosition, null);
            }
        }
    }

    private int getNeighborInfluencedOverlayGraphicsIndex(int xpos, int ypos, int MASK) {
        int index = 0;
        //check for nulls first.
        //This would be nice and easy with Groovy's nullsafe dot operator
        if (getTile(xpos + 1, ypos - 1) == null)
            ;
        else if((getTile(xpos + 1, ypos - 1).C3COverlays & MASK) == MASK)
            index+=2;     //Northeast
        if (getTile(xpos - 1, ypos + 1) == null)
            ;
        else if((getTile(xpos - 1, ypos + 1).C3COverlays & MASK) == MASK)
            index+=8;     //Southwest
        //Southeast
        if (getTile(xpos + 1, ypos + 1) == null)
            ;
        else if((getTile(xpos + 1, ypos + 1).C3COverlays & MASK) == MASK)
            index+=4;
        //Does the tile connect to the northwest?  Assume yes, then figure out if it doesn't.
        if (getTile(xpos - 1, ypos - 1) == null)
            ;
        else if((getTile(xpos - 1, ypos - 1).C3COverlays & MASK) == MASK)
            index+=1;
        return index;
    }

    private void drawRuins(TILE tile, int x, int y, Graphics g)
    {
        int gpxX = tile.xPos*64 - 64;
        int gpxY = tile.yPos*32;
        
        int xOffset = 20;
        int yOffset = 15;
        
        if (assets.largeRuinGraphics)
        {
            //Exact formula may vary.  This works well with Age of Titans, which is the only test case so far
            xOffset=assets.ruinGraphics[0].getWidth()/2 - 60;
            yOffset=assets.ruinGraphics[0].getHeight()/2 - 45;
        }

        //Ruins
        if (tile.ruin == TILE.RUINS_PRESENT)
        {
            //AFAIK, we have no way of knowing if it's city, town, or metropolis ruins.  Go with town
            g.drawImage(assets.ruinGraphics[0], gpxX - x - xOffset, gpxY - y - yOffset, null);
        }
    }

    private void drawUnits(TILE tile, int x, int y, Graphics g)
    {
        int gpxX = tile.xPos*64 - 64;
        int gpxY = tile.yPos*32;
        
        if (tile.unitWithBestDefence != -1) {
            if ((gpxX - x + 50 > 0 && ((gpxX - x) + 50) < BUFFERWIDTH) && (gpxY - y + 50 > 0 && ((gpxY - y)+50) < BUFFERHEIGHT))
            {
                //figure out which icon
                UNIT mapUnit = biq.mapUnit.get(tile.unitsOnTile.get(tile.unitWithBestDefence));
                PRTO unitType = biq.unit.get(mapUnit.getPRTONumber());
                //it'll be the unitType's icon
                int index = unitType.getIconIndex();
                if (logger.isTraceEnabled())
                    logger.trace("Drawing a " + unitType.getName() + " on tile " + tile.xPos + ", " + tile.yPos);
                
                try {
                    BufferedImage civColoredImage = Units32Supplier.getUnit32Image(index, tile.borderColor);
                    g.drawImage(civColoredImage, (gpxX - x) + 48, (gpxY - y) + 9, null);
                }
                catch(RasterFormatException ex) {
                    logger.error("Could not fetch units32 for unit " + unitType.getName() + " at " + tile.getXPos() + ", " + tile.getYPos() + "; icon index = " + index);
                    alertUserToError(ex, tile.getXPos(), tile.getYPos());
                }
            }
        }
    }
    
    /**
     * Returns the TILE specified by its x and y positions.  This involves
     * converting the 2D location into a 1D location in the TILE array.
     * @param xPos - The x position (isometric) of the tile
     * @param yPos - The y position (isometric) of the tile
     * @return - The TILE requested.
     */
    private TILE getTile(int xPos, int yPos)
    {
        int index = 0;
        //always add in a width-worth * yPos/2 (truncated)
        index+=((yPos/2)*(wmap.width));
        if (yPos % 2 == 1)  //add in half a width worth of tiles
            index+=((wmap.width)/2);
        index+=xPos/2;
        try{
            return tiles.get(index);
        }
        catch(java.lang.IndexOutOfBoundsException e){
            return null;
        }
    }
    
    
    /**
     * Returns true if and only if, in an infinitely extending NW-SE chain of
     * mountains/hills, the mountain at (xpos, ypos) will visually
     * connect to the mountain directly to its southeast.
     *
     * This is determined by figuring out if the mountain at (xpos, ypos)
     * will *NOT* connect to the mountain directly to the southeast, as this
     * is the rarer condition, and then returning the opposite of what that
     * calculation determines.
     * 
     * TODO: Where should this method live?
     *
     * @param xpos - The x position of the mountain/hill in question.
     * @param ypos - The y position of the mountain/hill in question.
     * @return - Whether, in a chain of mountains, that mountain/hill will
     * connect to the mountain/hill to its southeast.
     */
    private boolean southeastConnection(int xpos, int ypos)
    {
        //check for top of map.
        if (ypos < 0 || ypos >= wmap.height - 1)
            return false;
        //NW/SE
        int numberOfFives = ypos/5; //(to nearest integer)
        int baseX = xpos;
        int baseY = ypos;
        //But we need baseX to be >= baseY if this is going to work.  So if it's less, add the width to it.
        if (baseX < baseY)
            baseX = baseX + this.wmap.width;
        baseX = baseX - 5*numberOfFives;
        baseY = baseY - 5*numberOfFives;
        //(BaseX, BaseY) will be in the first 5 rows and will be 5z tiles NW of the one in question, z an integer
        //Now, there are 5 key tiles - (0, 0), (4, 2), (8, 4), (7, 1), and (11, 3).  These are the first tiles where
        //a "No SE" block occurs
        boolean noSE = false;
        if (logger.isTraceEnabled())
            logger.trace("baseX, baseY: " + baseX + ", " + baseY);
        while (baseX >= 0)
        {
            if (baseX == 0 && baseY == 0)
                noSE = true;
            if (baseX == 4 && baseY == 2)
                noSE = true;
            if (baseX == 8 && baseY == 4)
                noSE = true;
            if (baseX == 7 && baseY == 1)
                noSE = true;
            if (baseX == 11 && baseY == 3)
                noSE = true;
            baseX-=5;
        }
        if (logger.isTraceEnabled())
            logger.trace("X: " + xpos + ", Y: " + ypos + " connects SE? " + !noSE);
        return !noSE;
    }
    
    public Image drawTile(int x, int y)
    {
        int tileIndex = biq.calculateTileIndex(x, y);
        BufferedImage image = new BufferedImage(196, 88, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        if (g != null && tileIndex != -1)
        {
            TILE tile = tiles.get(tileIndex);
            int realTileX = tile.xPos;
            int realTileY = tile.yPos;
            
            tile.xPos = 0;
            tile.yPos = 0;
            
            //TODO: We really need the amalgamation of four base terrains from the surrounding tiles
            drawBaseTerrain(tiles.get(tileIndex), -64, -56, g);
            //This part is good
            drawTileElements(tiles.get(tileIndex), -64, -24, g);
            
            tile.xPos = realTileX;
            tile.yPos = realTileY;
        }
        else
            logger.error("Graphics null in drawTile(x,y)");
        return image.getSubimage(0, 0, 128, image.getHeight());
    }
}
