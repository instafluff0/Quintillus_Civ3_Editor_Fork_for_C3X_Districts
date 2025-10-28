package com.civfanatics.civ3.xplatformeditor;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Quintillus
 */
import com.civfanatics.civ3.xplatformeditor.tabs.map.Brush;
import com.civfanatics.civ3.biqFile.CITY;
import com.civfanatics.civ3.biqFile.CLNY;
import com.civfanatics.civ3.biqFile.GOOD;
import com.civfanatics.civ3.biqFile.IO;
import com.civfanatics.civ3.biqFile.LEAD;
import com.civfanatics.civ3.biqFile.RACE;
import com.civfanatics.civ3.biqFile.RULE;
import com.civfanatics.civ3.biqFile.SLOC;
import com.civfanatics.civ3.biqFile.TERR;
import com.civfanatics.civ3.biqFile.TILE;
import com.civfanatics.civ3.biqFile.UNIT;
import com.civfanatics.civ3.biqFile.WMAP;
import com.civfanatics.civ3.xplatformeditor.specialty.SafetyLevel;
import static com.civfanatics.civ3.xplatformeditor.tabs.map.Brush.*;
import com.civfanatics.civ3.xplatformeditor.tabs.map.GraphicsAssets;
import com.civfanatics.civ3.xplatformeditor.tabs.map.MapDirection;
import com.civfanatics.civ3.xplatformeditor.tabs.map.MapUtils;
import com.civfanatics.civ3.xplatformeditor.tabs.map.OverlayType;
import com.civfanatics.civ3.xplatformeditor.tabs.map.renderer.ClassicRenderer;
import com.civfanatics.civ3.xplatformeditor.tabs.map.renderer.Renderer;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.apache.log4j.Logger;

/**
 * This class represents a view of the map in the Civilization III editor.  It
 * will allow full map editing, should have smoother animation that Firaxis's
 * editor, and hopefully a more sophisticated interface as well (ex. being able
 * to see all buildings/units on a tile at once).
 *
 * Key is that we'll have to be able to scroll the map.  One way to do this is
 * to render the whole bloomin' map in a buffer, and use adjustments of the x
 * and y positions in the paintScreen method to control which part of the map is
 * visible at what time.  Depending on the overhead of drawing the whole bloomin'
 * map (especially on larger maps), it may be necessary to draw only a portion
 * of the map.
 *
 * TODO:  Implement caching.  The idea is that we don't need to redraw a tile if
 * it hasn't changed - we just save it an paint the <i>whole tile</i> into the
 * current display, not recreating the tile from scratch every time.  Of course,
 * we can't store the image of every single tile - for a 65,000 tile map, times
 * a size of perhaps 90*130 per tile (12,000 pixels), we're talking 65K*12K*4
 * = 3 GB of memory usage.  Thus, we must cache the rendered tiles.
 *
 * We'll want to use some level of set associativity.  We only need to be able
 * store perhaps twice as many tiles each way as we can display - thus four
 * times as many as are displayed at once.  This will accomodate usual scroll
 * speeds (if the user jumps minimap style, caching won't help anyways).  How
 * many ways the set is associative depends on how big the map is.  If we display
 * sixteen rows in height, three-way set associativity would be good with a 100-tile
 * high map, but nine-way would with a 300-tile high map.  Fortunatly, we can
 * adjust the cache associativity depending on the map size since it's a software
 * cache.  We'll also probably want to use 2D set associativity - since we
 * can take both the x and y into account, and only need so much in either
 * direction.  Thus, our 100x100 map map use 3x3 set associativity, whereas a
 * 100x300 map might use 3x9 set associativity.  It appears that a more formal
 * term for this is a '2D associative array', and we could say that it has
 * dynamic associativity.
 *
 * We can also choose multiple methods of controlling the cache - we can either
 * model it as a LRU scheme, which would essential be 'hardware controlled'
 * (with virtual hardware), or we can make it scratchpad, with 'user control'
 * or 'software control' (with virtual software determined by the actual code).
 * The advantage of scratchpad would be that we can preemptively change what's
 * in the cache.  For example, if the user is scrolling up, we can preemptively
 * put rows further up in the cache, knowing that they'll probably be needed.
 * This would give better performance, but be more complicated - as may be the
 * usual case with scratchpad caches over hardware-controlled ones.  It would
 * be possible to start with the 'hardware' scheme and later improve upon it
 * to use the 'scratchpad' scheme.
 * 
 * Update to the above TODO (8/26/2013): Although the above _would_ work, it still
 * hasn't been implemented, as it would be difficult.  The attempt to implement
 * caching where only edge tiles were redrawn was abandoned after it proved not
 * to be working reliably.  Unless that's resurrected (and given how long it's been
 * idle, it's clear I don't really want to try to resurrect that), the caching
 * scheme is the alternative.  Fortunately, it looks like there's a way to implement
 * the cache that will be much easier to implement - implement it via Soft References.
 * The Soft References will be automatically cleared when they need to be for memory
 * use purposes.  Thus, if we cache the whole tile, we can cache as many as memory allows.
 * The garbage collector will reclaim the memory if need be, and if we find a cached
 * item is no longer there, we just re-create it (we'd also re-create it if something
 * had changed).
 * 
 * Now this isn't quite as efficient since the garbage collector may collect the most
 * frequently used, or all of them, or who knows what.  BUT it's much easier, and thus
 * much more likely to actually see the light of day.  And we're a lot less likely to
 * run into the dreaded Out of Memory error.  Which is also good since if we were making
 * our own cache, we'd have to shrink it ourselves when we ran out of memory, and we 
 * probably wouldn't know how much we'd need to shrink it by, which means we may
 * well still be at a situation where we're just clearing everything.  So without
 * basically building our own fancy memory management system, Soft References
 * are probably the way to go.
 *
 * @author Andrew
 */
public class MapPanel extends JPanel implements Runnable{
    Logger logger = Logger.getLogger(this.getClass());
    private static final int PANELWIDTH=1000;
    private static final int PANELHEIGHT=700;
    private static int BUFFERWIDTH=1300;  //1024+64
    private static int BUFFERHEIGHT=1200;  //768+32
    private static int TILE_WIDTH = 128;
    private static int TILE_HEIGHT = 64;

    private Thread animator;    //animates the map

    //The below two are volatile so that the animation thread sees when
    //they are set to true/false by the GUI/UI thread
    private volatile boolean running = false;    //is the map panel running?
    public volatile boolean activeTab = false;
    public int updateCount = 2;
    public boolean firstIteration = true;  //ADJ 04/05/13
    public boolean alwaysUpdate = false;
    
    private boolean isBenchmarking = false;
    private long benchmarkFrames = 0;
    private long totalRenderTime;

    //global variables for off-screen rendering
    private BufferedImage buffer = null;
    private int horizScrollOffset, vertScrollOffset;

    private int period = 30000000;    //how many nanoseconds between updates

    public boolean isPaused = false;
    
    final short CLASSIC_MODE = 1;
    
    static double zoom = 1;
    
    static short renderMode = 1;

    //BIQ sections
    WMAP wmap;
    IO biq;
    List<TILE> tiles;
    List<LEAD> player;
    List<GOOD> resource;
    List<CITY> city;
    List<RULE> rule;
    List<RACE> civ;
    List<SLOC> startingLocation;
    List<CLNY> colony;
    
    GraphicsAssets assets = null;

    MapTab mapTab;

    //locals
    public boolean gridOn = true;
    private boolean customPlayerData = false;

    Settings settings;

    Brush brush;
    
    Renderer renderer = null;
    
    Cursor hand = new Cursor(Cursor.HAND_CURSOR);
    Cursor crosshair = new Cursor(Cursor.CROSSHAIR_CURSOR);
    Cursor standard = new Cursor(Cursor.DEFAULT_CURSOR);
    
    /**
     * Allows placing overlays on any terrain, regardless of what the game supports.
     * Currently only toggleable in code, to allow experimenting on whether the
     * game supports anything not exposed in the Firaxis editor.
     */
    private boolean skipOverlayRestrictions = false;
    
    Random rnd = new Random();  //for display variety
    
    int napTime = 0; //in ms

    public MapPanel()
    {
        this.settings = Main.settings;
        brush = new Brush();
        setBackground(Color.white);
        setPreferredSize(new Dimension(PANELWIDTH, PANELHEIGHT));

        //Comments
        setFocusable(true);

        //create map components

        //listen for mouse presses
        addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e)
            {
                getGraphics().drawOval(e.getX(), e.getY(), 3, 3);
                mousePress(e.getX(), e.getY());
            }
        });
        this.addMouseMotionListener(new MouseMotionListener(){
            public void mouseDragged(MouseEvent e)
            {
                //getGraphics().drawOval(e.getX(), e.getY(), 3, 3);
                mousePress(e.getX(), e.getY());
            }
            public void mouseMoved(MouseEvent e)
            {
                
            }
        });
        
        this.addMouseListener(new MouseListener(){
            public void mouseEntered(MouseEvent e) {
                setMapCursor(brush.mode);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

    }
    
    public void setMapCursor(int brushMode) {
        switch (brush.mode) {
            case MODE_SELECT:
                setCursor(hand);
                Main.pnlTabs.mapTab.setBrushImages(1);
                break;
            case MODE_SETTLEMENT:
                setCursor(crosshair);
                Main.pnlTabs.mapTab.setBrushImages(4);
                break;
            case MODE_DISTRICT:
                setCursor(crosshair);
                Main.pnlTabs.mapTab.setBrushImages(5);
                break;
            case MODE_OVERLAY:
                Main.pnlTabs.mapTab.setBrushImages(3);
            case MODE_RELOCATE:
            case MODE_RELOCATE_CITY_AND_UNITS:
                setCursor(crosshair);
                break;
            case MODE_TERRAIN:
                Main.pnlTabs.mapTab.setBrushImages(2);
                setCursor(standard);
                break;
            default:
                //Fog
                Main.pnlTabs.mapTab.setBrushImages(6);
                setCursor(standard);
                break;
        }
    }
    
    public void setAssets(GraphicsAssets assets) {
        this.assets = assets;
        Image image = createImage(BUFFERWIDTH, BUFFERHEIGHT);
        renderer = new ClassicRenderer(image, assets, biq);
    }
    
    /**
     * Benchmarks our performance.
     */
    public void benchmark() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    benchmarkFrames = 0;
                    totalRenderTime = 0;
                    alwaysUpdate = true;
                    isBenchmarking = true;
                    
                    synchronized (this) {
                        this.wait(10000);
                    }
                    
                    isBenchmarking = false;
                    alwaysUpdate = false;
                    long avgTimePerFrame = totalRenderTime/benchmarkFrames; //microseconds
                    long fps = 1000000/avgTimePerFrame;
                    logger.info("The average FPS is " + fps + " frames per second");
                    JOptionPane.showMessageDialog(null, "The average FPS is " + fps + " frames per second");
                }
                catch(Exception ex) {
                    logger.error("Exception in benchmarking thread", ex);
                }            
            }
        });
    }

    public void sendData(WMAP wmap, List<TILE> tile, List<GOOD>resource, List<CITY>city, List<RULE> rule, List<RACE>civ, IO biq)
    {
        this.tiles = tile;
        this.wmap = wmap;
        this.resource = resource;
        this.city = city;
        this.rule = rule;
        this.civ = civ;
        this.biq = biq;
        this.startingLocation = biq.startingLocation;
        this.colony = biq.colony;
        //this.setPreferredSize(new Dimension(PANELWIDTH, PANELHEIGHT));
        this.setPreferredSize(new Dimension(wmap.width * 64, wmap.height * 32));
        logger.info("Size of canvas: " + wmap.width * 64 + " x " + wmap.height * 32);
    }

    public void sendPlayerData(List<LEAD>player)
    {
        this.customPlayerData = true;
        this.player = player;
    }

    public void receiveHorizPosition(int x)
    {
        if (true)
        {
            this.horizScrollOffset = (int)(x/zoom);
            renderer.setHorizPosition(this.horizScrollOffset);
            if (logger.isDebugEnabled())
            {
                logger.debug("New x value: " + x);
                logger.debug("horizScrollPosition: " + this.horizScrollOffset);
            }
            this.triggerUpdates();
        }
    }

    public void receiveVertPosition(int y)
    {
        if (true)
        {
            if (logger.isDebugEnabled())
                logger.debug("Receiving vertical position");
            this.vertScrollOffset = (int)(y/zoom);
            renderer.setVertPosition(this.vertScrollOffset);
            if (logger.isDebugEnabled())
                logger.debug("New y value: " + y);
            this.triggerUpdates();
        }
    }

    private void mousePress(int xPixel, int yPixel)
  // is (x,y) important to the game?
    {
        int northPolarYHeight_32 = 32;
        if (!isPaused)
        {
           //this.x = x;
           //this.y = y;
           if (logger.isDebugEnabled())
               logger.debug("Mouse press at " + xPixel + ", " + yPixel + ".");
           //Convert it into Civ3 coordinates.  We'll start by taking the first fully visible tile, (1, 1),
           //and the 128x64 block it is part of
           //These offsets seem to be very close to perfect.  Remember that to make the gpx look fluid (rather than abrupt transitions),
           //it will sometimes look like the panel says there is plains where it looks like grassland.  By looking
           //at where borders are, we can tell how close we are to what we should be.
           if (logger.isTraceEnabled())
           {
                logger.trace("2*(xPixel/128) + 1 = " + (2*(xPixel/128) + 1));
                logger.trace("(2*((int)(((yPixel - 32)/64)/zoom))) + 1 = " + ((2*((int)(((yPixel - northPolarYHeight_32)/64)/zoom))) + 1));
                logger.trace("yPixel: " + yPixel);
                logger.trace("mapY w/o zoom: " + (((2*((yPixel - northPolarYHeight_32)/64))) + 1));
                logger.trace("mapY with zoom: " + ((2*((int)(((yPixel - northPolarYHeight_32)/64)/zoom))) + 1));
                logger.trace("mapY with zoom with double: " + ((2*((int)(((yPixel - northPolarYHeight_32)/64.0)/zoom))) + 1));
           }
           //int mapY = (2*((int)(((yPixel - northPolarYOffset)/64)/zoom))) + 1;
           int mapY = (2*((int)((yPixel - (northPolarYHeight_32*zoom))/(64*zoom)))) + 1;
           int mapX = (2*((int)(xPixel/(128 * zoom)))) + 1;
           if (logger.isTraceEnabled())
           {
                logger.trace("mapX with zoom: " + ((2*((int)(xPixel/(128 * zoom)))) + 1));
                logger.trace("mapX with zoom with extra double: " + ((2*((int)(xPixel/(128.0 * zoom)))) + 1));
                logger.trace("mapX before int conversion (reg): " + (xPixel/(128 * zoom)));
                logger.trace("mapX: " + mapX);
           }
           
           //mapX and mapY should ALWAYS be odd.  We get even tiles by moving NW/NE/SE/SW
           if (mapX % 2 != 1)
               logger.warn("mapX is even at a point when it should always be odd.  mapX is " + mapX + ".  Mouse press was at (" + xPixel + ", " + yPixel + ")");
           if (mapY % 2 != 1)
               logger.warn("mapY is even at a point when it should always be odd.  mapY is " + mapY + ".  Mouse press was at (" + xPixel + ", " + yPixel + ")");
           
           //do we move NW?  we do if 2*y < x and x < 64, on the first 64x64 grid
           int xOffset = (int)((xPixel)%((128*zoom))); //get the x base 128
           int yOffset = (int)((yPixel-(northPolarYHeight_32*zoom))%((64*zoom))); //get the y base 64
           
           if (logger.isTraceEnabled())
           {
                logger.info("xOffset: " + xOffset);
                logger.info("yOffset: " + yOffset);
           }
           
           //Cache the current (always odd) map X/Y for rivers.  A river affects the boundary between two tiles and affects them both;
           //as a result we can always calculate which tiles are affected based on the always-odd tile.
           int riverX = mapX;
           int riverY = mapY;

           //The forumulai used in the following if statements was derived with
           //help from pens, paper, and graphing software.
           //System.out.println("X offset: " + xOffset + ", YOffset: " + yOffset);
           if (2*yOffset + xOffset < (int)(64*zoom) && yOffset < (int)(32*zoom) && xOffset < (int)(64*zoom)) //move NW
           {
               if (logger.isDebugEnabled())
                    logger.debug("Move NW from " + mapX + ", " + mapY);
               mapX--;
               mapY--;
           }
           else if (-2*yOffset + xOffset > (int)(64*zoom) && xOffset > (int)(64*zoom) && yOffset < (int)(32*zoom)) //Move NE
           {
                if (logger.isDebugEnabled())
                    logger.debug("Move NE from " + mapX + ", " + mapY);
                mapX++;
                mapY--;
           }
           else if (-2*yOffset + xOffset < (int)(-64*zoom) && xOffset < (int)(64*zoom) && yOffset > (int)(32*zoom)) //Move SW
           {
                if (logger.isDebugEnabled())
                    logger.debug("Move SW from " + mapX + ", " + mapY);
                mapX--;
                mapY++;
           }
           else if (2*yOffset + xOffset > (int)(192*zoom) && yOffset > (int)(32*zoom) && xOffset > (int)(64*zoom)) //Move SE
           {
               if (logger.isDebugEnabled())
                    logger.debug("Move SE from " + mapX + ", " + mapY);
                mapX++;
                mapY++;
           }
           if (logger.isDebugEnabled())
               logger.debug("Square pressed = " + mapX + ", " + mapY);
           //Bounds checking
           if (mapX >= wmap.width || mapX < 0 || mapY > wmap.height || mapY < 0)
               return;
                      
           //Now we do something with it
           if (logger.isDebugEnabled())
               logger.debug("Brush mode: " + brush.mode);
           switch (brush.mode)
           {
               case Brush.MODE_SELECT:
                   mapTab.alertToSquarePress(mapX, mapY);
                   break;
               case Brush.MODE_TERRAIN:
                    List<TILE> tilesInDiameter = biq.getTilesInDiameter(mapX, mapY, brush.diameter);
                    for (TILE tile : tilesInDiameter) {
                     handleTerrainEvent(tile, false);
                    }
                    triggerUpdates();
                    mapTab.alertToSquarePress(mapX, mapY);
                    break;
               case Brush.MODE_OVERLAY:
                    if (brush.overlayType == OverlayType.RIVER) {
                        MapDirection direction = MapUtils.determineRiverDirection(xOffset, yOffset, mapX, mapY);
                        if (direction != MapDirection.NONE) {
                            handleRiverEvent(riverX, riverY, direction);
                        }
                    }
                    else {
                        handleOverlayEvent(mapX, mapY);    
                    }
               mapTab.alertToSquarePress(mapX, mapY);
               break;
               case Brush.MODE_DISTRICT:
                   mapTab.handleDistrictClick(mapX, mapY);
                   break;
               case Brush.MODE_SETTLEMENT:
                   handleCityEvent(mapX, mapY);
                   mapTab.alertToSquarePress(mapX, mapY);
                   break;
               case Brush.MODE_RELOCATE:
                   brush.mode = Brush.MODE_SELECT; //so user can't immediately re-locate again
                   handleCityRelocateEvent(mapX, mapY);
                   mapTab.alertToSquarePress(mapX, mapY);
                   break;
               case Brush.MODE_RELOCATE_CITY_AND_UNITS:
                   brush.mode = Brush.MODE_SELECT; //so user can't immediately re-locate again
                   boolean cityMoved = handleCityRelocateEvent(mapX, mapY);
                   if (cityMoved)
                       handleUnitRelocationEvent(mapX, mapY);
                   mapTab.alertToSquarePress(mapX, mapY);
                   break;
               case Brush.MODE_ADD_FOG:
                    tilesInDiameter = biq.getTilesInDiameter(mapX, mapY, brush.diameter);
                    for (TILE tile : tilesInDiameter) {
                        handleFogEvent(tile.xPos, tile.yPos, true);
                    }
                   mapTab.alertToSquarePress(mapX, mapX);
                   break;
               case Brush.MODE_REMOVE_FOG:
                    tilesInDiameter = biq.getTilesInDiameter(mapX, mapY, brush.diameter);
                    for (TILE tile : tilesInDiameter) {
                        handleFogEvent(tile.xPos, tile.yPos, false);
                    }
                    break;
                default:
                    logger.warn("Unexpected brush mode");
           }

        }
    }

    private void handleTerrainEvent(TILE tile, boolean triggerUpdates)
    {
        int x = tile.xPos;
        int y = tile.yPos;
        int terrainType = brush.terrainType;
        //this ain't simple.  we have to take into account base terrain and all sorts of madness.
        //Oh great, the file and image stuff needs updating too.  blimely.
        //also we can't have certain bordaries.  like desert-tundra.  blimely.
        //I wonder: what would happen if we specified a file greater than 8?  oh, it probably
        //wouldn't know what the file named.  probably more likely to work is if we specified
        //an index > 80 and expanded the pcx
        if (terrainType > 6 && terrainType < 11)    //forest/jungle/marsh/volcano
        {
            tile.setRealTerrain(terrainType);
            tile.setUpNibbles();
            if (brush.isLandmark)
                tile.C3CBonuses = tile.C3CBonuses | 0x2000;
            else
                tile.C3CBonuses = tile.C3CBonuses & ~0x2000;
            //forests - check for pine
            if (terrainType == TERR.FOREST)
            {
                if (brush.isPine)
                    tile.C3CBonuses = tile.C3CBonuses | 32;
                else
                    tile.C3CBonuses = tile.C3CBonuses & ~32;
            }
            if (triggerUpdates)
                triggerUpdates();
        }
        else if (terrainType < 14) //not sea/ocean - is base terrain
        {            
            boolean wasLandmark = (tile.C3CBonuses & 0x2000) == 0x2000;
            
            if (brush.isLandmark)
                tile.C3CBonuses = tile.C3CBonuses | 0x2000;
            else
                tile.C3CBonuses = tile.C3CBonuses & ~0x2000;
            
            //Deepwater harbour
            //This is essentially a hack - we can't use our standard calculations about
            //whether to allow the terrain modification or not.
            if (terrainType == 12 && brush.isDeepwaterHarbour)
            {
                if (tile.getBaseTerrain() == 11) //was coast; valid harbour
                {
                    tile.setBaseTerrain(terrainType);
                    tile.setRealTerrain(terrainType);
                    tile.setUpNibbles();
                    //Do NOT recalculate graphics
                    JOptionPane.showMessageDialog(null, "Deepwater harbour created");
                    //don't do the rest; exit early
                    return;
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Can only create deepwater harbours on coast");
                }
            }
            
            if (makeTerrainChange(x, y, terrainType))//, nwIndex, nIndex, neIndex, terrainType))
            {
                //apply bonus grassland/snow-capped mountains NOW (TODO: why are we doing LM earlier, anyway???)
                
                if (terrainType == TERR.MOUNTAIN)
                {
                    if (brush.isSnowCapped)
                        tile.C3CBonuses = tile.C3CBonuses | 16;
                    else
                        tile.C3CBonuses = tile.C3CBonuses & ~16;
                }
                if (terrainType == TERR.GRASSLAND)
                {
                    if (brush.isBonusGrassland)
                        tile.C3CBonuses = tile.C3CBonuses | 1;
                    else
                        tile.C3CBonuses = tile.C3CBonuses & ~1;
                }
                if (triggerUpdates)
                    triggerUpdates();
                //int file = calculateFile(tileSelected, nwIndex, nIndex, neIndex, terrainType));
            }
            else
            {
                //revert LM
                if (wasLandmark)
                {
                    logger.info("Making landmark");
                    tile.C3CBonuses = tile.C3CBonuses | 0x2000;
                }
                else
                {
                    logger.info("Making unlandmark");
                    tile.C3CBonuses = tile.C3CBonuses & ~0x2000;
                }
            }
        }
    }
    
    /**
     * Handles clicking on the map with the fog brush mode.  Add or removes fog
     * to the desired tile.
     * @param mapX The x coordinate of the tile.
     * @param mapY The y coordinate of the tile.
     * @param addFog Whether to add fog (true) or remove it (false)
     */
    private void handleFogEvent(int mapX, int mapY, boolean addFog)
    {
        int tileID = biq.calculateTileIndex(mapX, mapY);
        tiles.get(tileID).setFogOfWar(addFog ? (short)0 : (short)1);
        triggerUpdates();
    }

    /**
     * Makes sure you don't have:
     * tundra and either plains or grassland in the same four-square area
     * all of grassland, plains, desert and coast in the same four-square area (to come)
     * @param firstTile
     * @param nwTile
     * @param nTile
     * @param neTile
     * @param terrainType
     * @return
     */
    private boolean makeTerrainChange(int x, int y, int terrainType)//, int nwTile, int nTile, int neTile, int terrainType)
    {
        boolean okay = true;
        //get the surroundinng tiles
        int north = biq.calculateTileIndex(x, y - 2);
        int northEast = biq.calculateTileIndex(x + 1, y - 1);
        int east = biq.calculateTileIndex(x + 2, y);
        int southEast = biq.calculateTileIndex(x + 1, y + 1);
        int south = biq.calculateTileIndex(x, y + 2);
        int southWest = biq.calculateTileIndex(x - 1, y + 1);
        int west = biq.calculateTileIndex(x - 2, y);
        int northWest = biq.calculateTileIndex(x - 1, y - 1);
        int center = biq.calculateTileIndex(x, y);

        boolean canMakeChange = true;
        //TODO: Decide if this is what I want.  Will let change the terrain under forests
        byte backupReal = tiles.get(center).getRealTerrain();
        byte backupBase = tiles.get(center).getBaseTerrain();
        //Discovery: You can have grassland graphics on desert - the graphics display based on
        //file ID/index
        //check for hillz/mountainz
        if (terrainType == 5 || terrainType == 6)
        {
            //TODO: Experiment with desert/tundra bases.  Probably won't look uber-pretty
            biq.tile.get(center).setRealTerrain(terrainType);
            biq.tile.get(center).setBaseTerrain(2);
        }
        else {
            biq.tile.get(center).setRealTerrain(terrainType);
            biq.tile.get(center).setBaseTerrain(terrainType);
        }
        tiles.get(center).setUpNibbles();
        if (!MapUtils.validChangeOnTiles(biq, northWest, north, northEast, center))
            canMakeChange = false;
        if (!MapUtils.validChangeOnTiles(biq, center, northEast, east, southEast))
            canMakeChange = false;
        if (!MapUtils.validChangeOnTiles(biq, southWest, center, southEast, south))
            canMakeChange = false;
        if (!MapUtils.validChangeOnTiles(biq, west, northWest, center, southWest))
            canMakeChange = false;
        
        if (!canMakeChange)
        {
            if (logger.isDebugEnabled())
                logger.debug("Invalid change");
            biq.tile.get(center).setRealTerrain(backupReal);
            biq.tile.get(center).setBaseTerrain(backupBase);
            tiles.get(center).setUpNibbles();
            return false;
        }
        else
        {
            if (logger.isDebugEnabled())
                logger.debug("Valid change");
            //leave the change
            //tile.get(center).setUpNibbles();
            //now we need to recalculate files/indices
            //Which tiles will need changed?  See
            //BaseTerrainModImage.png in CivIII folder, based on SOTD 99
            //It assumes 7,11 is changed to grassland
            //Our extragrid lets us see what needs changed - oh thank heaven!
            //The center tile, south tile, southeast tile, and southwest tile
            //need recalculating (rem the red grid corresponds to the tile 0.5 tiles S of it)
            MapUtils.recalculateFileAndIndex(biq, x, y);
            MapUtils.recalculateFileAndIndex(biq, x - 1, y + 1);
            MapUtils.recalculateFileAndIndex(biq, x + 1, y + 1);
            MapUtils.recalculateFileAndIndex(biq, x, y + 2);
            //testing 123
            //TODO: Figure out what madness Firaxis uses in determining which tiles to change the graphics of
            //In Random Map.biq, changing 58, 20 to tundra changes the tile NW to xtgc graphics
            //But then changing 57, 19 (the original NW tile) does to tundra does NOT do the same to the tile NW of it
            //recalculateFileAndIndex(x, y - 2);
            //recalculateFileAndIndex(x + 2, y);
            //recalculateFileAndIndex(x - 2, y);
            //recalculateFileAndIndex(x + 1, y -1);
            //recalculateFileAndIndex(x - 1, y -1);

        }
//        int firstTerrain, nwTerrain, nTerrain, neTerrain;
//        if (nwTile == -1)
//            nwTerrain = 11;
//        else nwTerrain = tile.get(nwTile).getBaseTerrain();
//        if (neTile == -1)
//            neTerrain = 11;
//        else neTerrain = tile.get(neTile).getBaseTerrain();
//        if (nTile == -1)
//            nTerrain = 11;
//        else nTerrain = tile.get(nTile).getBaseTerrain();
//        if (firstTile == -1)
//            firstTerrain = 11;
//        else firstTerrain = tile.get(firstTile).getBaseTerrain();
//        //Tundra checks - can't border desert or plains
//        if (nwTerrain == 3)
//        {
//            if (nTerrain < 2)
//                return false;
//            if (neTerrain < 2)
//                return false;
//            if (firstTerrain < 2)
//                return false;
//        }
//        if (nTerrain == 3)
//        {
//            if (neTerrain < 2)
//                return false;
//            if (firstTerrain < 2)
//                return false;
//        }
//        if (neTerrain == 3)
//        {
//            if (firstTerrain < 2)
//                return false;
//        }
//        return true;
        return true;
    }

    /**
     * Handles updating the map when a tile is painted in overlay mode.
     * @param x The x coordinate on the map
     * @param y The y coordinate on the map
     */
    private void handleOverlayEvent(int x, int y)
    {
        int tileSelected = biq.calculateTileIndex(x, y);
        TILE tile = tiles.get(tileSelected);
        int terrainType = brush.terrainType;
        //this ain't simple.  we have to take into account base terrain and all sorts of madness.
        int realTerrain = tiles.get(tileSelected).getRealTerrain();
        TERR terrain = biq.terrain.get(realTerrain);
        switch (brush.overlayType) {
            case IRRIGATION: 
                if (terrain.getFoodBonus() > 0 || skipOverlayRestrictions) {
                    tiles.get(tileSelected).setIrrigated(true);
                    tiles.get(tileSelected).setMined(false);
                }
                break;
            case MINE:
                if (terrain.getShieldsBonus() > 0 || skipOverlayRestrictions) {
                    tiles.get(tileSelected).setMined(true);
                    tiles.get(tileSelected).setIrrigated(false);
                }
                break;
            case ROAD:
                if (terrain.getCommerceBonus() > 0 || skipOverlayRestrictions) {
                    tiles.get(tileSelected).setRoad(true);
                }
                break;
            case RAILROAD:
                if (terrain.getCommerceBonus() > 0 || skipOverlayRestrictions) {
                    tiles.get(tileSelected).setRailroad(true);
                }
                break;
            case FORT:
                if (terrain.getAllowForts() == 1 || skipOverlayRestrictions) {
                    tiles.get(tileSelected).setFort(true);
                }
                break;
            case BARRICADE:
                if (terrain.getAllowForts() == 1 || skipOverlayRestrictions) {
                    tiles.get(tileSelected).setBarricade(true);
                }
                break;
            case BARBARIAN_CAMP:
                if (!tiles.get(tileSelected).isWater() || skipOverlayRestrictions) {
                    tiles.get(tileSelected).setBarbarianCamp(true);
                }
                break;
            case GOODY_HUT:
                if (!tiles.get(tileSelected).isWater() || skipOverlayRestrictions) {
                    tiles.get(tileSelected).setGoodyHut(true);
                }
                break;
            case POLLUTION:
                //You can have pollution on water tiles
                tiles.get(tileSelected).setPollution(true);
                break;
            case CRATERS:
                //Oddly, you can crater water tiles despite the logistical challenges of doing so.
                tiles.get(tileSelected).setCrater(true);
                break;
            case VICTORY_POINT_LOCATION:
                tiles.get(tileSelected).setVictoryPointLocation(TILE.VICTORY_POINT_LOCATION_PRESENT);
                break;
            case RUINS:
                //Can only have ruins where cities are allowed
                if (terrain.getAllowCities() == 1 || skipOverlayRestrictions) {
                    tiles.get(tileSelected).setRuin(TILE.RUINS_PRESENT);
                }
                break;
            case STARTING_LOCATION:
                //Can start anywhere that isn't water (even if cities not allowed)
                if (!tile.isWater() || skipOverlayRestrictions) {
                    biq.addStartingLocation(tile);
                }
                break;
            case RADAR_TOWER:
                if (terrain.allowsRadarTowers() || skipOverlayRestrictions) {
                    handlePaintedColony(tile, CLNY.RADAR_TOWER);
                }
                break;
            case AIRFIELD:
                if (terrain.allowsAirfields() || skipOverlayRestrictions) {
                    handlePaintedColony(tile, CLNY.AIRFIELD);
                }
                break;
            case COLONY:
                boolean allowColony = false;
                if (terrain.allowsColonies()) {
                    int goodIndex = tile.getResource();
                    if (goodIndex != -1) {
                        GOOD good = biq.resource.get(goodIndex);
                        if (good.isLuxury() || good.isStrategic()) {
                            allowColony = true;
                        }
                    }
                }
                if (allowColony || skipOverlayRestrictions) {
                    handlePaintedColony(tile, CLNY.COLONY);
                }
                break;
            case OUTPOST:
                if (terrain.allowsOutposts() || skipOverlayRestrictions) {
                    handlePaintedColony(tile, CLNY.OUTPOST);
                }
                break;
        }
        triggerUpdates();
    }
    
    private void handleRiverEvent(int mapX, int mapY, MapDirection direction) {
        mapTab.toggleRiver(mapX, mapY, direction, true);
        triggerUpdates();
    }
    
    private void handlePaintedColony(TILE tile, int COLONY_TYPE) {
        if (tile.getColony() == -1) {
            CLNY newColony = new CLNY(biq);
            newColony.setImprovementType(COLONY_TYPE);
            newColony.setX(tile.xPos);
            newColony.setY(tile.yPos);
            //Who should own it???
            if (tile.owner != -1)
            {
                newColony.setOwner(tile.owner);
                newColony.setOwnerType(tile.ownerType);
            }
            else    //default fallback option
            {
                newColony.setOwnerType(CITY.OWNER_CIV);
                newColony.setOwner(1);    //first one after barbs
            }
            biq.colony.add(newColony);
            tile.setColony((short)(biq.colony.size() - 1));
        }
        else {
            CLNY curColony = biq.colony.get(tile.getColony());
            if (curColony.getImprovementType() != COLONY_TYPE)
                curColony.setImprovementType(COLONY_TYPE);
        }
    }
    
    private void handleCityEvent(int x, int y)
    {
        int tileSelected = biq.calculateTileIndex(x, y);
        if (tiles.get(tileSelected).getCity() != -1)
            return;
        if (biq.city.size() >= 512 && Main.safetyLevels.get("Map").ordinal() >= SafetyLevel.Safe.ordinal()) {
            int confirmation = JOptionPane.showConfirmDialog(null, "<html>Your BIQ already has the maximum of 512 cities.  Do you really want to add another?<br/><br/>"
                    + "Civ3 only supports 512 buildings, so adding more is not recommended.</html>", 
                    "Add more than 512 buildings?", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.WARNING_MESSAGE);
            if (confirmation == JOptionPane.NO_OPTION) {
                return;
            }
        }
        logger.info("Handling city event");
        
        String name = JOptionPane.showInputDialog(null, "Enter a name for the new city.", "City name", JOptionPane.PLAIN_MESSAGE);
        if (name == null) 
            return;
        CITY newCity = new CITY(biq, name, x, y);
        biq.city.add(newCity);
        tiles.get(tileSelected).setCity((short)(biq.city.size() - 1));
        biq.calculateTileOwners();
    }
    
    /**
     * Relocates the city at brush.tileBeingRelocated to x, y, if x, y does not already have a city
     * @param x
     * @param y
     * @return True if the city is actually moved (false if the target location already has a city).
     */
    private boolean handleCityRelocateEvent(int x, int y)
    {
        int tileSelected = biq.calculateTileIndex(x, y);
        if (tiles.get(tileSelected).getCity() != -1)
            return false;
        
        int cityIndex = brush.tileBeingRelocated.getCity();
        
        //save old values
        int oldX = biq.city.get(cityIndex).getX();
        int oldY = biq.city.get(cityIndex).getY();
        List<TILE> oldInfluences = biq.city.get(cityIndex).tilesInfluenced;
        
        //update with new ones
        biq.city.get(cityIndex).setX(x);
        biq.city.get(cityIndex).setY(y);
        
        //Also remove the old tile's city index, and add a city index to the new tile
        int oldIndex = biq.calculateTileIndex(oldX, oldY);
        tiles.get(oldIndex).setCity((short)-1);
        tiles.get(tileSelected).setCity((short)cityIndex);
        
        //update culture - remove old influence, add new
        //this could probably be done more efficiently by only updating certain tiles,
        //but the time to make it more efficient is likely more than it's worth
        biq.calculateTileOwners();
        return true;
    }
    
    /**
     * Moves all units on brush.tileBeingRelocated to the tile at x, y
     * @param x
     * @param y 
     */
    private void handleUnitRelocationEvent(int x, int y)
    {
        List<Integer> units = brush.tileBeingRelocated.unitsOnTile;
        int newIndex = biq.calculateTileIndex(x, y);
        logger.info("x, y works out to: " + x + ", " + y + " = " + newIndex);
        
        for (int i = 0; i < units.size(); i++)
        {
            int unitIndex = units.get(i);
            UNIT unit = biq.mapUnit.get(unitIndex);
            unit.setX(x);
            unit.setY(y);
            //also add it to the new tile
            logger.info("new tile has x, y of " + biq.tile.get(newIndex).xPos + ", " + biq.tile.get(newIndex).yPos);
            biq.tile.get(newIndex).unitsOnTile.add(unitIndex);
        }
        biq.tile.get(newIndex).calculateUnitWithBestDefence(); //Will be either the best new unit, or the best old, depending
        brush.tileBeingRelocated.unitsOnTile.clear();
        brush.tileBeingRelocated.calculateUnitWithBestDefence(); //will set it to -1 now that there are no units there
    }

    public void sendTab(MapTab tab)
    {
        this.mapTab = tab;
    }

    /**
     * Wait until we are added to a container before starting
     */
    public void addNotify()
    {   //waits for the panel to be added to the frame
        //The addNotify will cause this to be alerted when it is added to a container
        super.addNotify();
        //Once it's added, we call the startMapPanel method
        //startMapPanel();
    }

    /**
     * Called when we are ready to start.  Spin up a thread for ourselves and
     * start it.
     *
     * Hack that it is public.  addNotify isn't delaying it like needed.
     */
    public void startMapPanel()
    {
        if (animator == null || !running)
        {
            animator = new Thread(this);
            animator.start();
        }
        logger.info("Map panel started");
    }

    /**
     * Called in case we ever want to stop the map panel.  For instance, this is
     * called when the user exits the program.
     */
    public void stopMapPanel()
    {
        running = false;
    }

    /**
     * The main animation loop.
     * Note that System.nanoTime() requies J2SE 1.5.  An alternative for J2SE 1.4.2 is
     * java.misc.Perf (see Davison, page 26).  
     * I have gone with System.nanoTime() as it is standard.
     */
    public void run()
    {
        //beforeTime is the time before updating, rendering, and painting.
        //timeDiff is how long updating/rendering/painting takes
        //sleep time is how long the thread should wait to keep a consistent FPS
        long beforeTime, timeDiff, sleepTime;
        beforeTime = System.nanoTime();

        running = true;
        //running will be true until the user exits the program
        //however, we only want this tab actually doing work if it's the active
        //tab.  So we check that inside the loop.  If it isn't active, it waits
        //50 ms and checks again.
        while (running)
        {
            if (shouldRedraw()) {
                if (logger.isTraceEnabled())
                    logger.trace("update: " + updateCount);
                beforeTime = System.nanoTime();
                //mapUpdate();  //6/12 - comment out; method doesn't do anything
                //ADJ 04/05/13 - Only render to the buffer once after the user does something that
                //might have changed the graphics.  Don't re-draw the buffer when you don't have to
                if (firstIteration || isBenchmarking)
                {
                    firstIteration = false; //set it to false first so that if there's an update partway through the rendering, we'll
                                            //do a full draw again
                    renderer.render();
                }
                buffer = renderer.getBuffer();
                paintScreen();  //paint from buffer to screen
                //Figure out how long it took to update/render/paint, then sleep the
                //appropriate amount to get steady FPS
                timeDiff = System.nanoTime() - beforeTime;
                //Interesting observation: On the laptop at 1024x768, with Maximal Performance
                //(2.2 GHz locked), timeDiff is about 40 ms consistently
                //With performance on demand, it is about 150 ms the first time, then as CPU speed
                //ramps up, gets down to 40 ms by the 4th or 5th time.  So you do have a considerable
                //difference in responsiveness, and the CPU takes 0.2-0.35 seconds to ramp up
                if (logger.isTraceEnabled() || isBenchmarking) {
                    logger.trace("time to render: " + timeDiff/1000000);
                    benchmarkFrames++;
                    totalRenderTime = totalRenderTime + timeDiff/1000;
                }
                sleepTime = period - timeDiff;   // time left in this loop
                //the thread is le tired
                if (logger.isTraceEnabled())
                    logger.trace("Sleep time: " + sleepTime/1000000 + " ms");
                if (sleepTime <= 0)  // update/render took longer than period
                    sleepTime = 5;    // sleep a bit anyway
                try {
                    Thread.sleep(sleepTime/1000000);  // in ms
                }
                catch(InterruptedException ex){
                    logger.error("Interrupted exception in map loop", ex);
                }

                beforeTime = System.nanoTime();
                updateCount--;
            }
            else {
                try {
                    Thread.sleep(30);  // in ms
                    napTime += 30;
                    if (napTime > 5000)  //update at least once ever 5 seconds
                    {
                        updateCount = 2;
                        napTime = 0;
                    }
                }
                catch(InterruptedException ex){
                    logger.error("Interrupted exception in map loop", ex);
                }
            }
        }
    }

    private boolean shouldRedraw() {
        return activeTab && (updateCount > 0 || alwaysUpdate) && !MenuSystem.hasMenuOpen();
    }

    public void triggerUpdates() {
        this.updateCount = Main.settings.drawReps;
        this.firstIteration = true;
    }

    /**
     * Actively render the buffer image to the screen.
     * Do not place any state logic in here.
     */
    private void paintScreen() {
        try  {
            Graphics2D g = (Graphics2D) this.getGraphics();  // get the panel’s graphic context
            if ((g != null) && (buffer != null) && activeTab) {
                //Only scale it if the user has set a zoom, as there is a performance penalty
                if (zoom != 1) {
                    Image i = buffer.getScaledInstance((int) (buffer.getWidth() * zoom), (int) (buffer.getHeight() * zoom), Main.settings.zoomQuality);
                    g.drawImage(i, (int)(horizScrollOffset*zoom), (int)(vertScrollOffset*zoom), null);
                } 
                else {
                    g.drawImage(buffer, horizScrollOffset, vertScrollOffset, null);
                }
            }
            //Toolkit.getDefaultToolkit().sync();    //May be necessary on Linux - test
            g.dispose();
        } 
        catch (Exception e) {
            logger.error("Graphics context error: ", e);
        }
    }
    
    public void updateBufferSize(int width)
    {
        if (logger.isTraceEnabled())
            logger.trace("Got new buffer width");
        BUFFERWIDTH = width + 64; //some extra
        buffer = (BufferedImage)createImage(BUFFERWIDTH, BUFFERHEIGHT);
        setZoom(100 * zoom);
    }

    /**
     * This is only used for drawing the tile view that is on the MapTab
     * sidebar.
     * @param x
     * @param y
     * @return 
     */
    public Image drawTile(int x, int y) {
        return renderer.drawTile(x, y);
    }
    
    Set encounteredExceptions = new HashSet();
    
    public int getVertPosition()
    {
        return vertScrollOffset;
    }
    
    void setZoom(double d)
    {
        if (wmap != null)
        {
            zoom = d * 0.01;
            BUFFERWIDTH = (int)(Main.mainMain.getWidth() / zoom);
            BUFFERHEIGHT = (int) (Main.mainMain.getHeight() / zoom);
            buffer = (BufferedImage)createImage(BUFFERWIDTH, BUFFERHEIGHT);
            
            renderer.setViewportSize(BUFFERWIDTH, BUFFERHEIGHT);

            int scrollBarWidth = (int)(wmap.width * 64 * zoom);
            int scrollBarHeight = (int)(wmap.height * 32 * zoom);

            if (logger.isTraceEnabled())
                logger.trace("New width: " + scrollBarWidth + ", scrollBarHeight: " + scrollBarHeight);

            this.setPreferredSize(new Dimension(scrollBarWidth, scrollBarHeight));
            Main.pnlTabs.mapTab.setZoom(zoom, scrollBarWidth, scrollBarHeight);
        }
    }
}
