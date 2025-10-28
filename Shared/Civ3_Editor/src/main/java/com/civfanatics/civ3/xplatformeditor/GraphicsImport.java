/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.civfanatics.civ3.xplatformeditor;

import com.civfanatics.civ3.biqFile.IO;
import com.civfanatics.civ3.xplatformeditor.imageSupport.Civ3PCXFilter;
import com.civfanatics.civ3.xplatformeditor.tabs.map.GraphicsAssets;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.Arrays;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import org.apache.log4j.*;

/**
 *
 * @author Andrew
 */
public class GraphicsImport extends Thread{
    IO biq;
    static Logger logger = Logger.getLogger("GraphicsImport");
    static final String newline = System.getProperty("line.separator");
    static final String fileSlash = System.getProperty("file.separator");
    Settings settings;
    MapPanel mapPanel;
    MapTab mapTab;
    Color[]colors;
    JTabbedPane tabs;
    BufferedImage[][]unitIcons;
    static final int NUM_TNT_DUPLICATES = 3;
    
    static int JOIN_TIMEOUT = 0;
    
    public GraphicsImport(IO biq, Settings settings, MapPanel mapPanel, Color[]colors, JTabbedPane tabs, MapTab mapTab, BufferedImage[][]unitIcons)
    {
        this.biq = biq;
        this.settings = settings;
        this.mapPanel = mapPanel;
        this.colors = colors;
        this.tabs = tabs;
        this.mapTab = mapTab;
        this.unitIcons = unitIcons;
    }

    public void run()
    {
        this.setName("Graphics Import Thread");
        this.setUncaughtExceptionHandler(new PCXExceptionHandler());
        
        try{
            long allocatedToJVM = Runtime.getRuntime().totalMemory();
            long allocatedButFree = Runtime.getRuntime().freeMemory();

            long inUseKB = (allocatedToJVM - allocatedButFree)/1024;

            logger.info("Pre-graphics import.  Memory in use: " + inUseKB/1024 + " MB");
            logger.info("Pre-graphics import.  Memory available: " + Runtime.getRuntime().maxMemory()/1024/1024 + " MB");
            importGraphicsMultithreaded();
            System.gc();
            
            
            allocatedToJVM = Runtime.getRuntime().totalMemory();
            allocatedButFree = Runtime.getRuntime().freeMemory();

            inUseKB = (allocatedToJVM - allocatedButFree)/1024;
            
            logger.info("Garbage Collected.  Memory in use: " + inUseKB/1024 + " MB");
            logger.info("Garbage Collected.  Memory available: " + Runtime.getRuntime().maxMemory()/1024/1024 + " MB");
        }
        catch(java.lang.OutOfMemoryError e)
        {
            logger.error("Out of memory", e);
            logger.error("Memory in use: " + Runtime.getRuntime().totalMemory()/1024 + " KB");
            logger.error("Memory available: " + Runtime.getRuntime().maxMemory()/1024 + " KB");
            JOptionPane.showMessageDialog(null, "<html>Out of memory.  This is likely to occur with graphics (map) enabled when not using the launcher.  Please check that<br><ul><li>You are using the launcher if you are using the map</li><li>You are not trying to open multiple BIQs at once</li></ul><br>If you aren't doing either of these, please make a post on the forum including what scenario you were trying to open.<br>You will still be able to edit the non-map parts of the BIQ, although disabling the map in Settings (Main tab) and restarting is recommended.</html>", "Out of memory", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void importGraphicsMultithreaded()
    {
        //first figure out the file names that we'll need
        long startGraphics = System.nanoTime();
        
        //<editor-fold desc="Figure Out File Names">
        String[]baseTerrainNames = new String[9];
        try{
            baseTerrainNames[0] = utils.findFile("xtgc.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            baseTerrainNames[1] = utils.findFile("xpgc.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            baseTerrainNames[2] = utils.findFile("xdgc.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            baseTerrainNames[3] = utils.findFile("xdpc.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            baseTerrainNames[4] = utils.findFile("xdgp.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            baseTerrainNames[5] = utils.findFile("xggc.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            baseTerrainNames[6] = utils.findFile("wCSO.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            baseTerrainNames[7] = utils.findFile("wSSS.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            baseTerrainNames[8] = utils.findFile("wOOO.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
        }
        catch(FileNotFoundException e){
            logger.error("Could not find one of the terrain graphics; civInstallDir = " + settings.civInstallDir, e);
            JOptionPane.showMessageDialog(null, "Could not find one of the terrain graphics.  The civ install dir (" + settings.civInstallDir + ") being incorrect may cause this problem.  This can be changed by clicking on the Settings button.\nError: " + e.getMessage());
            return;
        }
        
        String[]lmTerrainNames = new String[9];
        try{
            lmTerrainNames[0] = utils.findFile("lxtgc.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            lmTerrainNames[1] = utils.findFile("lxpgc.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            lmTerrainNames[2] = utils.findFile("lxdgc.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            lmTerrainNames[3] = utils.findFile("lxdpc.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            lmTerrainNames[4] = utils.findFile("lxdgp.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            lmTerrainNames[5] = utils.findFile("lxggc.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            lmTerrainNames[6] = utils.findFile("lwCSO.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            lmTerrainNames[7] = utils.findFile("lwSSS.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            lmTerrainNames[8] = utils.findFile("lwOOO.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
        }
        catch(FileNotFoundException e){
            logger.error("Could not find one of the terrain graphics; civInstallDir = " + settings.civInstallDir, e);
            JOptionPane.showMessageDialog(null, "Could not find one of the terrain graphics.  The civ install dir (" + settings.civInstallDir + ") being incorrect may cause this problem.  This can be changed by clicking on the Settings button.\nError: " + e.getMessage());
            return;
        }
        
        String grassForestName = null;
        String plainsForestName = null;
        String tundraForestName = null;
        String marshName = null;
        String roadName = null;
        String railroadName = null;
        String irrigationName = null;
        String plainsIrrigationName = null;
        String desertIrrigationName = null;
        String tundraIrrigationName = null;
        String riverName = null;
        String deltaName = null;
        String buildingName = null;
        String tntName = null;
        String goodyhutName = null;
        String startingLocationName = null;
        String resourceName = null;
        String lmTerrOverlayName = null;
        String americanName = null;
        String americanWName = null;
        String europeanName = null;
        String europeanWName = null;
        String mediterraneanName = null;
        String mediterraneanWName = null;
        String midEastName = null;
        String midEastWName = null;
        String asianName = null;
        String asianWName = null;
        String ruinsName = null;
        String pollutionName = null;
        String airfieldsName = null;
        String victoryPointName = null;
        String craterName = null;
        
        try{
            grassForestName = utils.findFile("grassland forests.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            plainsForestName = utils.findFile("plains forests.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            tundraForestName = utils.findFile("tundra forests.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            marshName = utils.findFile("marsh.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            roadName = utils.findFile("roads.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            railroadName = utils.findFile("railroads.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            irrigationName = utils.findFile("irrigation.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            plainsIrrigationName = utils.findFile("irrigation PLAINS.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            desertIrrigationName = utils.findFile("irrigation DESETT.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            tundraIrrigationName = utils.findFile("irrigation TUNDRA.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            riverName = utils.findFile("mtnRivers.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            deltaName = utils.findFile("deltaRivers.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            buildingName = utils.findFile("TerrainBuildings.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            tntName = utils.findFile("tnt.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            goodyhutName = utils.findFile("goodyhuts.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            startingLocationName = utils.findFile("StartLoc.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            resourceName = utils.findFile("resources.pcx", "Art" + fileSlash, biq);
            lmTerrOverlayName = utils.findFile("landmark_terrain.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            
            americanName = utils.findFile("rAMER.pcx", "Art" + fileSlash + "Cities" + fileSlash, biq);
            americanWName = utils.findFile("AMERWALL.pcx", "Art" + fileSlash + "Cities" + fileSlash, biq);

            europeanName = utils.findFile("rEURO.pcx", "Art" + fileSlash + "Cities" + fileSlash, biq);
            europeanWName = utils.findFile("EUROWALL.pcx", "Art" + fileSlash + "Cities" + fileSlash, biq);

            mediterraneanName = utils.findFile("rROMAN.pcx", "Art" + fileSlash + "Cities" + fileSlash, biq);
            mediterraneanWName = utils.findFile("ROMANWALL.pcx", "Art" + fileSlash + "Cities" + fileSlash, biq);

            midEastName = utils.findFile("rMIDEAST.pcx", "Art" + fileSlash + "Cities" + fileSlash, biq);
            midEastWName = utils.findFile("MIDEASTWALL.pcx", "Art" + fileSlash + "Cities" + fileSlash, biq);

            asianName = utils.findFile("rASIAN.pcx", "Art" + fileSlash + "Cities" + fileSlash, biq);
            asianWName = utils.findFile("ASIANWALL.pcx", "Art" + fileSlash + "Cities" + fileSlash, biq);
            
            ruinsName = utils.findFile("DESTROY.pcx", "Art" + fileSlash + "Cities" + fileSlash, biq);
            pollutionName = utils.findFile("pollution.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            airfieldsName = utils.findFile("x_airfields and detect.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            victoryPointName = utils.findFile("x_victory.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            craterName = utils.findFile("craters.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
        }
        catch(FileNotFoundException e){
            logger.error("Could not find one of the PCX files; civInstallDir = " + settings.civInstallDir, e);
            JOptionPane.showMessageDialog(null, "Could not find grassland pcx " + e.getMessage() + ".  The civ install dir (" + settings.civInstallDir + ") being incorrect may cause this problem.  This can be changed by clicking on the Settings button.\nError: " + e.getMessage());
            return;
        }
        
//        String territoryName = null;
//        try{
//            territoryName = utils.findFile("Territory.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
//        }
//        catch(FileNotFoundException e){
//            logger.error("Could not find Territory.pcx; civInstallDir = " + settings.civInstallDir, e);
//            JOptionPane.showMessageDialog(null, "Could not find Territory.pcx.  The civ install dir (" + settings.civInstallDir + ") being incorrect may cause this problem.  This can be changed by clicking on the Settings button.\nError: " + e.getMessage());
//            return;
//        }
        //</editor-fold>
        
        
        long endFilenames = System.nanoTime();
        long filenamesTime = (endFilenames - startGraphics)/1000000;
        logger.info("It took " + filenamesTime + " milliseconds to determine the filenames.");
        
        //<editor-fold desc="Start the threads">
        //now start the threads
        PCXImportThread[]baseTerrainsThreads = new PCXImportThread[9];
        for (int i = 0; i < 9; i++)
        {
            baseTerrainsThreads[i] = new PCXImportThread(baseTerrainNames[i]);
            baseTerrainsThreads[i].setSubdivisions(9, 9, 0, 128, 64);
            baseTerrainsThreads[i].start();
        }
        
        PCXImportThread[]lmTerrainsThreads = new PCXImportThread[9];
        for (int i = 0; i < 9; i++)
        {
            lmTerrainsThreads[i] = new PCXImportThread(lmTerrainNames[i]);
            lmTerrainsThreads[i].setSubdivisions(9, 9, 0, 128, 64);
            lmTerrainsThreads[i].start();
        }
        
        PCXImportThread grassForestThread = new PCXImportThread(grassForestName);
        grassForestThread.start();
        
        PCXImportThread plainsForestThread = new PCXImportThread(plainsForestName);
        plainsForestThread.start();
        
        PCXImportThread tundraForestThread = new PCXImportThread(tundraForestName);
        tundraForestThread.start();
        
        PCXImportThread marshThread = new PCXImportThread(marshName);
        marshThread.start();
        
        
        PCXImportThread roadThread = new PCXImportThread(roadName);
        roadThread.setSubdivisions(16, 16, 0, 128, 64);
        roadThread.start();
        
        
        PCXImportThread railroadThread = new PCXImportThread(railroadName);
        railroadThread.setSubdivisions(16, 16, 0, 128, 64);
        railroadThread.start();
        
        PCXImportThread irrigationThread = new PCXImportThread(irrigationName);
        irrigationThread.setSubdivisions(4, 4, 0, 128, 64);
        irrigationThread.start();
        
        PCXImportThread plainsIrrigationThread = new PCXImportThread(plainsIrrigationName);
        plainsIrrigationThread.setSubdivisions(4, 4, 0, 128, 64);
        plainsIrrigationThread.start();
        
        PCXImportThread desertIrrigationThread = new PCXImportThread(desertIrrigationName);
        desertIrrigationThread.setSubdivisions(4, 4, 0, 128, 64);
        desertIrrigationThread.start();
        
        PCXImportThread tundraIrrigationThread = new PCXImportThread(tundraIrrigationName);
        tundraIrrigationThread.setSubdivisions(4, 4, 0, 128, 64);
        tundraIrrigationThread.start();
        
        PCXImportThread riverThread = new PCXImportThread(riverName);
        riverThread.setSubdivisions(4, 4, 0, 128, 64);
        riverThread.start();
        
        PCXImportThread deltaThread = new PCXImportThread(deltaName);
        deltaThread.setSubdivisions(4, 4, 0, 128, 64);
        deltaThread.start();
        
        PCXImportThread buildingThread = new PCXImportThread(buildingName);
        buildingThread.start();
        
        PCXImportThread tntThread = new PCXImportThread(tntName);
        tntThread.start();
        
        PCXImportThread goodyhutThread = new PCXImportThread(goodyhutName);
        goodyhutThread.start();
        
//        PCXImportThread territoryThread = new PCXImportThread(territoryName);
//        territoryThread.start();
        
        PCXImportThread startingLocationThread = new PCXImportThread(startingLocationName, true);
        startingLocationThread.start();
        
        PCXImportThread resourceThread = new PCXImportThread(resourceName);
        resourceThread.start();
        
        PCXImportThread lmTerrOverlayThread = new PCXImportThread(lmTerrOverlayName);
        lmTerrOverlayThread.start();
        
        String[]noWallNames = {americanName, europeanName, mediterraneanName, midEastName, asianName};
        String[]wallNames = {americanWName, europeanWName, mediterraneanWName, midEastWName, asianWName};
        PCXImportThread[] noWallThreads = new PCXImportThread[5];
        PCXImportThread[] wallThreads = new PCXImportThread[5];
        for (int i = 0; i < 5; i++)
        {
            noWallThreads[i] = new PCXImportThread(noWallNames[i]);
            wallThreads[i] = new PCXImportThread(wallNames[i]);
            noWallThreads[i].start();
            wallThreads[i].start();
        }
        
        PCXImportThread ruinsThread = new PCXImportThread(ruinsName);
        ruinsThread.start();
        
        PCXImportThread pollutionThread = new PCXImportThread(pollutionName);
        pollutionThread.start();
        
        PCXImportThread airfieldsThread = new PCXImportThread(airfieldsName);
        airfieldsThread.start();
        
        PCXImportThread victoryPointThread = new PCXImportThread(victoryPointName);
        victoryPointThread.start();
        
        PCXImportThread craterThread = new PCXImportThread(craterName);
        craterThread.start();
        
        
        long endSpawning = System.nanoTime();
        long spawningTime = (endSpawning - startGraphics)/1000000;
        logger.info("It took " + spawningTime + " milliseconds to spawn the threads.");
        //</editor-fold>
        
        //<editor-fold desc="Wait for things to process...">
        try
        {
            for (int i = 0; i < 9; i++)
            {
                baseTerrainsThreads[i].join(JOIN_TIMEOUT);
                lmTerrainsThreads[i].join(JOIN_TIMEOUT);
            }
            grassForestThread.join(JOIN_TIMEOUT);
            plainsForestThread.join(JOIN_TIMEOUT);
            tundraForestThread.join(JOIN_TIMEOUT);
            marshThread.join(JOIN_TIMEOUT);
            roadThread.join(JOIN_TIMEOUT);
            railroadThread.join(JOIN_TIMEOUT);
            irrigationThread.join(JOIN_TIMEOUT);
            plainsIrrigationThread.join(JOIN_TIMEOUT);
            desertIrrigationThread.join(JOIN_TIMEOUT);
            tundraIrrigationThread.join(JOIN_TIMEOUT);
            riverThread.join(JOIN_TIMEOUT);
            deltaThread.join(JOIN_TIMEOUT);
            buildingThread.join(JOIN_TIMEOUT);
            tntThread.join(JOIN_TIMEOUT);
            goodyhutThread.join(JOIN_TIMEOUT);
//            territoryThread.join(JOIN_TIMEOUT);
            startingLocationThread.join(JOIN_TIMEOUT);
            resourceThread.join(JOIN_TIMEOUT);
            lmTerrOverlayThread.join(JOIN_TIMEOUT);
            for (int i = 0; i < 5; i++)
            {
                noWallThreads[i].join(JOIN_TIMEOUT);
                wallThreads[i].join(JOIN_TIMEOUT);
            }
            ruinsThread.join(JOIN_TIMEOUT);
            pollutionThread.join(JOIN_TIMEOUT);
            airfieldsThread.join(JOIN_TIMEOUT);
            victoryPointThread.join(JOIN_TIMEOUT);
            craterThread.join(JOIN_TIMEOUT);
        }
        catch(InterruptedException e)
        {
            logger.error(e);
        }
        //</editor-fold>
        
        
        long endThreads = System.nanoTime();
        long threadsTime = (endThreads - startGraphics)/1000000;
        logger.info("It took " + threadsTime + " milliseconds to get to the end of the threads.");
        
        //<editor-fold desc="Get graphics">
        
        //9 for the files, 81 for the images within a file
        BufferedImage[][]baseTerrainGraphics = new BufferedImage[9][];
        for (int i = 0; i < 9; i++) {
            baseTerrainGraphics[i] = baseTerrainsThreads[i].getSubdivisions();
            baseTerrainsThreads[i] = null;
        }
        
        //9 for the files, 81 for the images within a file
        BufferedImage[][]lmTerrainGraphics = new BufferedImage[9][];
        for (int i = 0; i < 9; i++) {
            lmTerrainGraphics[i] = lmTerrainsThreads[i].getSubdivisions();
            lmTerrainsThreads[i] = null;
        }
        
        BufferedImage[]largeJungle = grassForestThread.processSubdivisions(2, 4, 0, 128, 88);
        BufferedImage[]smallJungle = grassForestThread.processSubdivisions(2, 6, 2, 128, 88);
        BufferedImage[]largeGrassForest = grassForestThread.processSubdivisions(2, 4, 4, 128, 88);
        BufferedImage[]smallGrassForest = grassForestThread.processSubdivisions(2, 5, 6, 128, 88);
        BufferedImage[]grassPineForest = grassForestThread.processSubdivisions(2, 6, 8, 128, 88);
        grassForestThread = null;
        
        BufferedImage[]largePlainsForest = plainsForestThread.processSubdivisions(2, 4, 4, 128, 88);
        BufferedImage[]smallPlainsForest = plainsForestThread.processSubdivisions(2, 5, 6, 128, 88);
        BufferedImage[]plainsPineForest = plainsForestThread.processSubdivisions(2, 6, 8, 128, 88);
        plainsForestThread = null;
        
        BufferedImage[]largeTundraForest = tundraForestThread.processSubdivisions(2, 4, 4, 128, 88);
        BufferedImage[]smallTundraForest = tundraForestThread.processSubdivisions(2, 5, 6, 128, 88);
        BufferedImage[]tundraPineForest = tundraForestThread.processSubdivisions(2, 6, 8, 128, 88);
        tundraForestThread = null;
        
        BufferedImage[]largeMarsh = new BufferedImage[8];
        //j is rows of PCX files
        for (int j = 0; j < 2; j++)
        {
            //k is columns of PCX files
            for (int k = 0; k < 4; k++)
            {
                largeMarsh[j*4 + k] = marshThread.getBufferedImage().getSubimage(k*128, j*88, 128, 88);
            }
        }
        BufferedImage[]pineMarsh = new BufferedImage[12];
        //j is rows of PCX files
        //NOTE:  Haven't done small marsh b/c idk when large marsh/jungle/etc. are used and when small ones are
        //NOTE:  I don't know exactly what the trigger for how much pines around is enough, or why the last image is not actually
        //a marsh at all
        for (int j = 0; j < 2; j++)
        {
            //k is columns of PCX files
            for (int k = 0; k < 5; k++)
            {
                pineMarsh[j*5 + k] = marshThread.getBufferedImage().getSubimage(k*128, j*88 + 704, 128, 88);
            }
        }
        marshThread = null;
        
        BufferedImage[]roadGraphics = roadThread.getSubdivisions();
        roadThread = null;
        
        BufferedImage[]railroadGraphics = railroadThread.getSubdivisions();
        railroadThread = null;
        
        BufferedImage[]irrigationGraphics = irrigationThread.getSubdivisions();
        irrigationThread = null;
        
        BufferedImage[]plainsIrrigationGraphics = plainsIrrigationThread.getSubdivisions();
        plainsIrrigationThread = null;
        
        BufferedImage[]desertIrrigationGraphics = desertIrrigationThread.getSubdivisions();
        desertIrrigationThread = null;
        
        BufferedImage[]tundraIrrigationGraphics = tundraIrrigationThread.getSubdivisions();
        tundraIrrigationThread = null;
        
        BufferedImage[]riverGraphics = riverThread.getSubdivisions();
        riverThread = null;
        
        BufferedImage[]deltaGraphics = deltaThread.getSubdivisions();
        deltaThread = null;
        
        //col 1 = fortresses, 2 = colonies, 3 = miscellaneous, 4 = barricades
        //If there are no barricades, don't try to import them
        int buildingColumns = buildingThread.getBufferedImage().getWidth() == 512 ? 4 : 3;
        BufferedImage[]buildingGraphics = buildingThread.processSubdivisions(4, buildingColumns, 0, 128, 64);
        buildingThread = null;
        
        BufferedImage[]tntGrass = tntThread.processSubdivisions(1, 3, 0, 128, 64);
        BufferedImage[]tntGrassShield = tntThread.processSubdivisions(1, 3, 1, 128, 64);
        BufferedImage[]tntPlains = tntThread.processSubdivisions(1, 3, 2, 128, 64);
        BufferedImage[]tntDesert = tntThread.processSubdivisions(1, 3, 3, 128, 64);
        BufferedImage[]tntTundra = tntThread.processSubdivisions(1, 3, 4, 128, 64);
        BufferedImage[]tntFloodPlains = tntThread.processSubdivisions(1, 3, 5, 128, 64);
        tntThread = null;
        
        BufferedImage[] goodyHutWithExtra = goodyhutThread.processSubdivisions(3, 3, 0, 128, 64);
        //There are three graphics in each of the first two rows but only two in the last row
        BufferedImage[]goodyhutGraphics = Arrays.copyOf(goodyHutWithExtra, 8);
        goodyHutWithExtra = null;
        goodyhutThread = null;
        
        //TODO: Fancy border stuff.  For now I'm gonna leave it single-threaded
        //Now territory borders
        long borderStart = System.nanoTime();
        String territoryName = null;
        try{
            territoryName = utils.findFile("Territory.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
        }
        catch(FileNotFoundException e){
            logger.error("Could not find Territory.pcx; civInstallDir = " + settings.civInstallDir, e);
            JOptionPane.showMessageDialog(null, "Could not find Territory.pcx.  The civ install dir (" + settings.civInstallDir + ") being incorrect may cause this problem.  This can be changed by clicking on the Settings button.\nError: " + e.getMessage());
            return;
        }
        Civ3PCXFilter borders = new Civ3PCXFilter(territoryName);
        borders.civ3TransparencyEnabled = false;
        Short one = 255;
        Short two = 1;
        borders.resetTransparents();
        borders.addTransparent(one);
        borders.addTransparent(two);
        borders.readFile();
        borders.parse();
        //TODO: Make this configurable in the settings
        //This controls whether the borders look like they do in-game or in Firaxis's editor
        //presently it always uses in-game borders
        if (false)
        {
            borders.setColor(249, new java.awt.Color(0, 93, 0));
            borders.setColor(252, new java.awt.Color(0, 36, 0));
        }
        else
        {

            borders.setColor(249, new java.awt.Color(0, 93, 0, 112));
            borders.setColor(252, new java.awt.Color(0, 36, 0, 112));
        }
        //Now borders
        BufferedImage[][]borderGraphics;
        borderGraphics = new BufferedImage[32][8];
        for (int i = 0; i < 32; i++)
        {
            Color color = colors[i];
            borders.setColor(64, color);
            borders.setColor(65, color.brighter());
            for (int j = 0; j < 4; j++)
            {
                //k is columns of PCX files
                for (int k = 0; k < 2; k++)
                {
                    //System.out.println("i: " + i + "; j:" + j + "; k: " + k);
                    borderGraphics[i][j*2 + k] = borders.getSubimage(k*128, j*72, 128, 72);
                }
            }
        }
        
        //TODO: Integrate better
        BufferedImage[]startLocGraphics;
        startLocGraphics = new BufferedImage[32];
        for (int i = 0; i < 32; i++)
        {
            Color color = colors[i];
            startingLocationThread.setColor(6, color);
            startLocGraphics[i] = startingLocationThread.getCivColoredSubimage(6, color, 0, 0, 128, 64);
        }
        startingLocationThread = null;
        
        long borderEnd = System.nanoTime();
        
        int resourceGridHeight = (resourceThread.getBufferedImage().getHeight()/50);
        BufferedImage[] resourceGraphics = resourceThread.processSubdivisions(resourceGridHeight, 6, 0, 50, 50);
        resourceThread = null;
        
        BufferedImage landmarkOverlay;
        landmarkOverlay = lmTerrOverlayThread.getBufferedImage().getSubimage(0, 0, 128, 64);
        lmTerrOverlayThread = null;
        
        //1st dimension = culture, 2nd = era, 3rd = city size
        BufferedImage[][][]noWallCityGraphics = new BufferedImage[5][4][3];
        //167 pixels width per image
        //95 pixels height per image
        //we'll want this centered, so... pixel 84 (x) and 48 (y)
        for (int i = 0; i < 5; i++) //per culture
        {
            //now per-row, which is per era
            for (int j = 0; j < 4; j++)
            {
                //and finally per size
                for (int k = 0; k < 3; k++)
                {
                    int widthPer = noWallThreads[i].getBufferedImage().getWidth()/3;
                    int heightPer = noWallThreads[i].getBufferedImage().getHeight()/4;
                    noWallCityGraphics[i][j][k] = noWallThreads[i].getBufferedImage().getSubimage(k*widthPer, heightPer*j, widthPer, heightPer);
                }
            }
            noWallThreads[i] = null;
        }
        
        //1st dimension = culture, 2nd = era
        BufferedImage[][]wallCityGraphics = new BufferedImage[5][4];
        //167 pixels width per image
        //95 pixels height per image
        //we'll want this centered, so... pixel 84 (x) and 48 (y)
        for (int i = 0; i < 5; i++) //per culture
        {
            System.out.println(i);
            //Some scenarios (ex. The Human Body) have cities with less than maximal width.  The next line accounts for them
            int maxWidth = wallThreads[i].getBufferedImage().getWidth() >= 167 ? 167 : wallThreads[i].getBufferedImage().getWidth();
            //Some scenarios may have unusual city heights
            int heightPer = wallThreads[i].getBufferedImage().getHeight()/4;
            //now per-row, which is per era
            for (int j = 0; j < 4; j++)
            {
                if (logger.isDebugEnabled())
                    logger.debug("Image " + j + " for culture " + i + ".  Image height is: " + wallThreads[i].getBufferedImage().getHeight());
//                int curHeight = 95;
//                if (95 * j + 95 > wallCities[i].getBufferedImage().getHeight())
//                {
//                    curHeight = wallCities[i].getBufferedImage().getHeight() - 95 * j;
//                }
                wallCityGraphics[i][j]= wallThreads[i].getBufferedImage().getSubimage(0, heightPer*j, maxWidth, heightPer);
                if (logger.isDebugEnabled())
                    logger.debug("OK");
            }
            wallThreads[i] = null;
        }
        
        //1st dimension = culture, 2nd = era
        BufferedImage[]ruinGraphics = new BufferedImage[3];
        //167 pixels width per image
        //95 pixels height per image
        int ruinWidth = ruinsThread.getBufferedImage().getWidth()/3;
        int ruinHeight = ruinsThread.getBufferedImage().getHeight();
        for (int j = 0; j < 3; j++)
        {
            ruinGraphics[j]= ruinsThread.getBufferedImage().getSubimage(ruinWidth*j, 0, ruinWidth, ruinHeight);
        }
        ruinsThread = null;
        
        BufferedImage[]pollutionGraphics = new BufferedImage[25];
        //j is rows of PCX files
        //row 1 = ancient age, 2 = mideival, etc.
        for (int j = 0; j < 5; j++)
        {
            //k is columns of PCX files
            //col 1 = fortresses, 2 = colonies, 3 = miscellaneous, 4 = barricades
            for (int k = 0; k < 5; k++)
            {
                pollutionGraphics[j*5 + k] = pollutionThread.getBufferedImage().getSubimage(k*128, j*64, 128, 64);
            }
        }
        pollutionThread = null;
        
        BufferedImage[]airfieldGraphics = new BufferedImage[2];
        BufferedImage[]outpostGraphics = new BufferedImage[3];
        BufferedImage radarTowerGraphics;
        //j is rows of PCX files
        for (int j = 0; j < 2; j++)
        {
            airfieldGraphics[j] = airfieldsThread.getBufferedImage().getSubimage(j*128, 0, 128, 64);
        }
        //Outpost graphics are double-height
        for (int j = 0; j < 3; j++)
        {
            outpostGraphics[j] = airfieldsThread.getBufferedImage().getSubimage(j*128, 64, 128, 128);
        }
        radarTowerGraphics = airfieldsThread.getBufferedImage().getSubimage(0, 192, 128, 128);
        airfieldsThread = null;
        
        BufferedImage victoryPointGraphics = null;
        //j is rows of PCX files
        victoryPointGraphics = victoryPointThread.getBufferedImage();  //Use the whole image, some VPLs like the Statue 
        victoryPointThread = null;
        
        BufferedImage[]craterGraphics = new BufferedImage[25];
        //j is rows of PCX files
        //row 1 = ancient age, 2 = mideival, etc.
        for (int j = 0; j < 5; j++)
        {
            //k is columns of PCX files
            //col 1 = fortresses, 2 = colonies, 3 = miscellaneous, 4 = barricades
            for (int k = 0; k < 5; k++)
            {
                //Some scenarios may have unusual-height craters
                int heightPer = craterThread.getBufferedImage().getHeight()/5;
                int widthPer = craterThread.getBufferedImage().getWidth()/5;
                if (logger.isTraceEnabled())
                    logger.trace(k*128 + " " + j*64);
                craterGraphics[j*5 + k] = craterThread.getBufferedImage().getSubimage(k*widthPer, j*heightPer, widthPer, heightPer);
            }
        }
        craterThread = null;
        //</editor-fold>
        
        //<editor-fold desc="Other stuff">
        
        //Now get the mountain terrains
        BufferedImage[]mountainGraphics = importGraphics("Mountains.pcx", 4, 4, 88);
        if (mountainGraphics == null)
            return;
        long endMountains = System.nanoTime();

        //Now get the snow mountain terrains
        BufferedImage[]snowMountainGraphics = importGraphics("Mountains-snow.pcx", 4, 4, 88);
        if (snowMountainGraphics == null)
            return;
        long endSnow = System.nanoTime();
        
        //Now get the LM mountain terrains
        BufferedImage[]lmMountainGraphics = importGraphics("LMMountains.pcx", 4, 4, 88);
        if (lmMountainGraphics == null)
            return;
        
        BufferedImage[]forestMountainGraphics = importGraphics("mountain forests.pcx", 4, 4, 88);
        if (lmMountainGraphics == null)
            return;
        
        BufferedImage[]jungleMountainGraphics = importGraphics("mountain jungles.pcx", 4, 4, 88);
        if (lmMountainGraphics == null)
            return;

        //Now get the hill terrains
        BufferedImage[]hillGraphics = importGraphics("xhills.pcx", 4, 4, 72);
        if (hillGraphics == null)
            return;
        
        //Now get the LM hills
        BufferedImage[]lmHillGraphics = importGraphics("LMHills.pcx", 4, 4, 72);
        if (lmHillGraphics == null)
            return;
        
        BufferedImage[]forestHillGraphics = importGraphics("hill forests.pcx", 4, 4, 72);
        if (hillGraphics == null)
            return;
        
        BufferedImage[]jungleHillGraphics = importGraphics("hill jungle.pcx", 4, 4, 72);
        if (hillGraphics == null)
            return;
        
        long endHills = System.nanoTime();
        
        //Now get the volcanos - boom!
        BufferedImage[]volcanoGraphics = importGraphics("Volcanos.pcx", 4, 4, 88);
        if (volcanoGraphics == null)
            return;
        
        BufferedImage[]forestVolcanoGraphics = importGraphics("Volcanos forests.pcx", 4, 4, 88);
        if (forestVolcanoGraphics == null)
            return;
        
        BufferedImage[]jungleVolcanoGraphics = importGraphics("Volcanos jungles.pcx", 4, 4, 88);
        if (jungleVolcanoGraphics == null)
            return;
        //</editor-fold>
        
        //Now get the Fog of War
        BufferedImage fog = null;
        try{
            String fogFile = utils.findFile("EditFog.pcx", "Art" + fileSlash + "Terrain" + fileSlash, biq);
            PCXImportThread fogThread = new PCXImportThread(fogFile);
            fogThread.start();
            fogThread.join(JOIN_TIMEOUT);
            fog = fogThread.getBufferedImage();
        }
        catch(FileNotFoundException ex)
        {
            
        }
        catch(InterruptedException ex)
        {
            
        }
        

        long endGraphics = System.nanoTime();
        long grxTime = (endGraphics - startGraphics)/1000000;
        logger.info("It took " + grxTime + " milliseconds to import all the graphics.");
        grxTime = (borderEnd - borderStart)/1000000;
        logger.info(grxTime + " milliseconds of that was for borders and start location");
        

        //Tile ownership - necessary for proper boundaries and ages of cities
        long start = System.nanoTime();
        biq.calculateTileOwners();
        long finish = System.nanoTime();
        long ms = (finish - start)/1000000;
        if (logger.isDebugEnabled())
            logger.debug("It took " + ms + " milliseconds to calculate all the tile owners.");


        //Experimental
        mapPanel.sendData(biq.worldMap.get(0), biq.tile, biq.resource, biq.city, biq.rule, biq.civilization, biq);
        //Send player data no matter what - will be starter data if custom data is not enabled
        mapPanel.sendPlayerData(biq.player);
        GraphicsAssets assets = new GraphicsAssets();
        assets.sendGraphics(baseTerrainGraphics, lmTerrainGraphics, roadGraphics, railroadGraphics, buildingGraphics, borderGraphics, resourceGraphics, noWallCityGraphics, wallCityGraphics, goodyhutGraphics, pollutionGraphics, startLocGraphics, airfieldGraphics, outpostGraphics, radarTowerGraphics, victoryPointGraphics, craterGraphics, ruinGraphics, landmarkOverlay, unitIcons);
        assets.sendHills(mountainGraphics, snowMountainGraphics, lmMountainGraphics, forestMountainGraphics, jungleMountainGraphics, hillGraphics, lmHillGraphics, forestHillGraphics, jungleHillGraphics, volcanoGraphics, forestVolcanoGraphics, jungleVolcanoGraphics);
        assets.sendWoodlands(largeJungle, smallJungle, largeGrassForest, smallGrassForest, grassPineForest, largePlainsForest, smallPlainsForest, plainsPineForest, largeTundraForest, smallTundraForest, tundraPineForest, largeMarsh, pineMarsh);
        assets.sendIrrigation(irrigationGraphics, plainsIrrigationGraphics, desertIrrigationGraphics, tundraIrrigationGraphics, riverGraphics, deltaGraphics);
        assets.sendTNT(tntGrass, tntGrassShield, tntPlains, tntDesert, tntTundra, tntFloodPlains);
        assets.sendFog(fog);
        mapPanel.setAssets(assets);
        
        mapTab.setUp(biq);
        mapPanel.sendTab(mapTab);
        mapPanel.startMapPanel();
        //Enable the map tab once it's all loaded
        tabs.setEnabledAt(tabs.getTabCount() - 1, true);
        Main.mapIsLoaded = true;
        
        long allocatedToJVM = Runtime.getRuntime().totalMemory();
        long allocatedButFree = Runtime.getRuntime().freeMemory();

        long inUseKB = (allocatedToJVM - allocatedButFree)/1024;
        
        logger.info("Graphics loaded.  Memory in use: " + inUseKB/1024 + " MB");
        logger.info("Graphics loaded.  Memory available: " + Runtime.getRuntime().maxMemory()/1024/1024 + " MB");
    }
    
    private BufferedImage[] importGraphics(String fileName, int xDim, int yDim, int heightEach, int heightOffset)
    {

        String mtnName = null;
        try{
            mtnName = utils.findFile(fileName, "Art" + fileSlash + "Terrain" + fileSlash, biq);
        }
        catch(FileNotFoundException e){
            logger.error("Could not find " + fileName + "; civInstallDir = " + settings.civInstallDir, e);
            JOptionPane.showMessageDialog(null, "Could not find " + fileName + ".  The civ install dir (" + settings.civInstallDir + ") being incorrect may cause this problem.  This can be changed by clicking on the Settings button.\nError: " + e.getMessage());
            return null;
        }
        Civ3PCXFilter mtns = new Civ3PCXFilter(mtnName);
        mtns.readFile();
        mtns.parse();
        mtns.createBufferedImage();
        BufferedImage[]mountainGraphics = new BufferedImage[xDim * yDim];
        //j is rows of PCX files
        for (int j = 0; j < 4; j++)
        {
            //k is columns of PCX files
            for (int k = 0; k < 4; k++)
            {
                mountainGraphics[j*4 + k] = mtns.getBufferedImage().getSubimage(k*128, j*heightEach + heightOffset, 128, heightEach);
            }
        }
        return mountainGraphics;
    }

    private BufferedImage[] importGraphics(String fileName, int xDim, int yDim, int heightEach)
    {
        return importGraphics(fileName, xDim, yDim, heightEach, 0);
    }
}
