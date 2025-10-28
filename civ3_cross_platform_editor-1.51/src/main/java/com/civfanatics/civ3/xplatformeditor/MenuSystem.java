
package com.civfanatics.civ3.xplatformeditor;

import com.civfanatics.civ3.biqFile.IO;
import com.civfanatics.civ3.ruleModifier.ui.RuleChangeApplierController;
import com.civfanatics.civ3.savFile.HistTurn;
import com.civfanatics.civ3.savFile.SAV;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.EditMenu;
import static com.civfanatics.civ3.xplatformeditor.Main.icon;
import com.civfanatics.civ3.xplatformeditor.savFunctionality.CustomHistograph;
import com.civfanatics.civ3.xplatformeditor.specialty.JavaVersion;
import com.civfanatics.civ3.xplatformeditor.specialty.UnitStatisticsFrame;
import com.civfanatics.civ3.xplatformeditor.tabs.map.MapExtender;
import com.civfanatics.civ3.xplatformeditor.tabs.map.MapFlipper;
import com.civfanatics.civ3.xplatformeditor.tabs.map.MapStats;
import com.civfanatics.civ3.xplatformeditor.tabs.map.frmBuildingsInCities;
import com.sun.javafx.charts.Legend;
import com.sun.javafx.collections.SortHelper;
import java.awt.Color;
import java.awt.event.ActionEvent;
import static java.awt.event.ActionEvent.SHIFT_MASK;
import java.awt.event.ActionListener;
import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_B;
import static java.awt.event.KeyEvent.VK_C;
import static java.awt.event.KeyEvent.VK_F;
import static java.awt.event.KeyEvent.VK_G;
import static java.awt.event.KeyEvent.VK_H;
import static java.awt.event.KeyEvent.VK_I;
import static java.awt.event.KeyEvent.VK_L;
import static java.awt.event.KeyEvent.VK_M;
import static java.awt.event.KeyEvent.VK_N;
import static java.awt.event.KeyEvent.VK_O;
import static java.awt.event.KeyEvent.VK_P;
import static java.awt.event.KeyEvent.VK_R;
import static java.awt.event.KeyEvent.VK_S;
import static java.awt.event.KeyEvent.VK_Z;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import org.apache.log4j.Logger;

/**
 * Represents the menu system for the editor.  Moved here because Main.java had
 * expanded from 2K to 3.5K lines again, and was becoming unwieldy.  Also might
 * be able to optimize performance this way.
 * @author Andrew
 */
public class MenuSystem {
    
    static Logger logger = Logger.getLogger(MenuSystem.class);
    
    JMenu mapMenu;
    JMenuItem save;
    JMenuItem importMap = new JMenuItem(Main.i18n("menu.map.importMap"));
    JMenuItem importMapFromBMP = new JMenuItem(Main.i18n("menu.map.importFromBMP"));
    JMenuItem bmpSizeCalculator = new JMenuItem(Main.i18n("menu.map.bmpSizeCalculator"));
    JMenuItem adjustSafety = new JMenuItem("Adjust Safety Level");
    JMenuItem addCustomMap = new JMenuItem(Main.i18n("menu.map.addCustom"));
    JMenuItem appendMapToRight = new JMenuItem("Append Map to Right");
    JMenuItem toggleGrid = new JMenuItem("Toggle Grid");
    JMenuItem toggleCoordinates = new JMenuItem("Toggle Coordinates");
    JMenuItem adjustZoom = new JMenuItem(Main.i18n("menu.map.changeZoom"));
    JMenuItem changeBuildingsInManyCities = new JMenuItem("Set Buildings in Many Cities at Once");
    JMenuItem polarWorldWrap = new JMenuItem(Main.i18n("menu.map.polarWrap"));
    JMenuItem redistributeGrassland = new JMenuItem(Main.i18n("menu.map.redistributeGrassland"));
    JMenuItem flipMap = new JMenuItem(Main.i18n("menu.map.flipMap"));
    JMenu stats = new JMenu("Statistics");
    JMenuItem terrainStats = new JMenuItem("Terrain Statistics");
    JMenuItem cityStats = new JMenuItem("City Statistics");
    JMenuItem unitStats = new JMenuItem("Unit Statistics");
    JMenu mapClear = new JMenu("Clear");
    JMenuItem clearCities = new JMenuItem("Cities");
    JMenuItem clearUnits = new JMenuItem("Units");
    
    JMenuItem exportToCSV = new JMenuItem("Export to CSV");
    JMenuItem importFromCSV = new JMenuItem("Import from CSV");
    JMenuItem changeSettings = new JMenuItem(Main.i18n("menu.options.settings"));
    JMenuItem enableCustomRules = new JMenuItem("Enable Custom Rules");
    JMenuItem enableCustomPlayerData = new JMenuItem("Enable Custom Player Data");
    JMenuItem changeSpecialActions = new JMenuItem("Change special actions");
    JMenuItem viewHelp = new JMenuItem(Main.i18n("menu.help.view"));
    JMenuItem memoryInfo = new JMenuItem("Java and Memory Info");
    JMenuItem benchmarkMap = new JMenuItem("Benchmark Map");
    JMenuItem about = new JMenuItem(Main.i18n("menu.help.about"));
    JMenu recent;
    JMenuItem[]recentFiles = new JMenuItem[5];
    Main main = null;
    
    JMenu savActions = new JMenu("SAV Actions");
    JMenuItem applyRuleChanges = new JMenuItem("Apply Rule Changes");
    JMenu histograph = new JMenu("Histograph");
    JMenuItem powerHistograph = new JMenuItem("Power");
    JMenuItem scoreHistograph = new JMenuItem("Score");
    JMenuItem cultureHistograph = new JMenuItem("Culture");
    
    EditMenu editMenu;
    
    private static AtomicInteger menusOpen = new AtomicInteger();
    
    static boolean editEnabled = false;
    
    public MenuSystem(Main container) {
        this.main = container;
        setupMenus();
    }
    
    private void setupMenus() {
        
        boolean mac = Main.os.name.toLowerCase().contains("mac");
        int CTRL_CMD_MASK = mac ? ActionEvent.META_MASK : ActionEvent.CTRL_MASK;
        
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu(Main.i18n("menu.file"));
        JMenuItem createNew = new JMenuItem(Main.i18n("menu.file.new"));
        JMenuItem open = new JMenuItem(Main.i18n("menu.file.open"));
        JMenuItem inputSAV = new JMenuItem(Main.i18n("menu.file.inputFromSAV"));
        inputSAV.setToolTipText("Allows you to extract the BIQ rules from a SAV.  Useful for recovering lost BIQs.");
        JMenuItem openSAV = new JMenuItem(Main.i18n("menu.file.openSAV"));
        openSAV.setToolTipText("This is currently a development-only feature.  Only use where permitted by law.");
        save = new JMenuItem(Main.i18n("menu.file.save"));
        save.setEnabled(false);
        exportToCSV.setEnabled(false);
        importFromCSV.setEnabled(false);
        changeSpecialActions.setEnabled(false);
        enableCustomRules.setEnabled(false);
        enableCustomPlayerData.setEnabled(false);
        recent = new JMenu(Main.i18n.getString("menu.file.recent"));
        recent.addMenuListener(new MenuListener(){
            @Override
            public void menuSelected(MenuEvent e) {
                logger.info("Recent menu selected");
            }

            @Override
            public void menuDeselected(MenuEvent e) {
                logger.info("Recent menu deselected");
            }

            @Override
            public void menuCanceled(MenuEvent e) {
                logger.info("Recent menu canceled");
            }
            
        });
        file.add(createNew);
        file.add(open);
        file.add(inputSAV);
        file.add(openSAV);
        file.add(save);
        
        file.setMnemonic(VK_F);
        createNew.setMnemonic(VK_N);
        createNew.setAccelerator(KeyStroke.getKeyStroke(VK_N, CTRL_CMD_MASK));
        open.setMnemonic(VK_O);
        open.setAccelerator(KeyStroke.getKeyStroke(VK_O, CTRL_CMD_MASK));
        inputSAV.setMnemonic(VK_I);
        save.setMnemonic(VK_S);
        save.setAccelerator(KeyStroke.getKeyStroke(VK_S, CTRL_CMD_MASK));
        recent.setMnemonic(VK_R);
        
        for (int i = 0; i < 5; i++) {
            if (Settings.recentFiles[i] != null) {
                JMenuItem rf = new JMenuItem(Settings.recentFiles[i]);
                rf.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        main.openFile(e.getActionCommand());
                    }
                });
                recentFiles[i] = rf;
                recent.add(rf);
            }
        }
        //Add recent
        file.add(recent);
        menuBar.add(file);
        
        
        editMenu = new EditMenu("Edit");
        if (editEnabled) {
            menuBar.add(editMenu);
        }
        
        mapMenu = new JMenu(Main.i18n.getString("menu.map"));
        mapMenu.addMenuListener(new MenuListener(){
            @Override
            public void menuSelected(MenuEvent e) {
                logger.info("Map menu selected");
            }

            @Override
            public void menuDeselected(MenuEvent e) {
                logger.info("Map menu deselected");
            }

            @Override
            public void menuCanceled(MenuEvent e) {
                logger.info("Map menu canceled");
            }
            
        });
        mapMenu.add(importMap);
        importMap.setMnemonic(VK_I);
        mapMenu.add(importMapFromBMP);
        importMapFromBMP.setMnemonic(VK_B);
        mapMenu.add(bmpSizeCalculator);
        bmpSizeCalculator.setMnemonic(VK_S);
        mapMenu.add(addCustomMap);
        addCustomMap.setMnemonic(VK_A);
        mapMenu.add(appendMapToRight);
        mapMenu.addSeparator();
        
        mapMenu.add(toggleGrid);
        toggleGrid.setMnemonic(VK_G);
        toggleGrid.setAccelerator(KeyStroke.getKeyStroke(VK_G, CTRL_CMD_MASK));
        mapMenu.add(toggleCoordinates);
        toggleCoordinates.setAccelerator(KeyStroke.getKeyStroke(VK_G, CTRL_CMD_MASK | ActionEvent.SHIFT_MASK));
        mapMenu.add(adjustZoom);
        adjustZoom.setMnemonic(VK_Z);
        adjustZoom.setAccelerator(KeyStroke.getKeyStroke(VK_Z, CTRL_CMD_MASK | ActionEvent.SHIFT_MASK));
        mapMenu.add(changeBuildingsInManyCities);
        changeBuildingsInManyCities.setMnemonic(VK_C);
        mapMenu.add(polarWorldWrap);
        polarWorldWrap.setMnemonic(VK_P);
        mapMenu.add(redistributeGrassland);
        redistributeGrassland.setMnemonic(VK_R);
        mapMenu.add(flipMap);
        stats.add(terrainStats);
        stats.add(cityStats);
        stats.add(unitStats);
        mapMenu.add(stats);
        flipMap.setMnemonic(VK_F);
        mapClear.add(clearCities);
        mapClear.add(clearUnits);
        mapMenu.add(mapClear);
        mapMenu.setEnabled(false);
        mapMenu.setMnemonic(VK_M);
        menuBar.add(mapMenu);
        
        JMenu options = new JMenu(Main.i18n.getString("menu.options"));
        options.add(adjustSafety);
        adjustSafety.setMnemonic(VK_L);
        options.add(exportToCSV);
        exportToCSV.setMnemonic(VK_C);
        options.add(importFromCSV);
        importFromCSV.setMnemonic(VK_I);
        options.add(changeSettings);
        changeSettings.setMnemonic(VK_S);
        changeSettings.setAccelerator(KeyStroke.getKeyStroke(VK_P, CTRL_CMD_MASK));
        options.add(enableCustomRules);
        enableCustomRules.setMnemonic(VK_R);
        enableCustomRules.setAccelerator(KeyStroke.getKeyStroke(VK_R, CTRL_CMD_MASK));
        options.add(enableCustomPlayerData);
        enableCustomPlayerData.setMnemonic(VK_P);
        enableCustomPlayerData.setAccelerator(KeyStroke.getKeyStroke(VK_P, SHIFT_MASK | CTRL_CMD_MASK));
//        options.add(changeSpecialActions);
//        changeSpecialActions.setMnemonic(VK_A);
        options.setMnemonic(VK_O);
        menuBar.add(options);
        
        applyRuleChanges.addActionListener(e -> {
            Platform.runLater(new Runnable() {
                public void run() {
                    try {
                        //When it's all ready, load up the rename window
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(Main.class.getResource("/fxml/RuleChangeApplier.fxml"));
                        Parent root = fxmlLoader.load();

                        //We must load the FXML before we can access its controller
                        RuleChangeApplierController controller = fxmlLoader.getController();

                        root.setVisible(true);
                        Stage stage = new Stage();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.setTitle("Update Scenario Rules");
                        stage.getIcons().add(Main.javaFXImage);
                        stage.show();
                    }
                    catch(IOException ex) {
                        logger.error("Error loading FXML for rule changes", ex);
                    }
                }
            });
        });
        savActions.add(applyRuleChanges);
        
        powerHistograph.addActionListener(e -> {
            Platform.runLater(new HistographDataThread("Power", HistTurn::getPower));
        });
        scoreHistograph.addActionListener(e -> {
            Platform.runLater(new HistographDataThread("Score", HistTurn::getScore));
        });
        cultureHistograph.addActionListener(e -> {
            Platform.runLater(new HistographDataThread("Culture", HistTurn::getCulture));
        });
        histograph.add(powerHistograph);
        histograph.add(scoreHistograph);
        histograph.add(cultureHistograph);
        histograph.setEnabled(false);
        savActions.add(histograph);
        menuBar.add(savActions);
        
        JMenu help = new JMenu(Main.i18n.getString("menu.help"));
        help.add(viewHelp);
        viewHelp.setMnemonic(VK_H);
        help.add(memoryInfo);
        memoryInfo.setMnemonic(VK_M);
        help.add(benchmarkMap);
        benchmarkMap.setMnemonic(VK_B);
        help.add(about);
        about.setMnemonic(VK_A);
        help.setMnemonic(VK_H);
        menuBar.add(help);
        
        
        //Menu actions
        createNew.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                main.createNewBIQ();
            }
        });
        
        open.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                main.open(evt);
            }
        });
        
        save.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                main.save(evt);
            }
        });
        
        inputSAV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                main.inputBIQRulesFromSAV(evt);
            }
        });
       
        openSAV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                main.openSAVFile(evt);
            }
        });
        
        main.setJMenuBar(menuBar);
        
        //Menu state listeners
        MenuListener statusChecker = new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                menusOpen.incrementAndGet();
            }

            @Override
            public void menuDeselected(MenuEvent e) {
                menusOpen.decrementAndGet();
            }

            @Override
            public void menuCanceled(MenuEvent e) {
                
            }
        };
        file.addMenuListener(statusChecker);
        mapMenu.addMenuListener(statusChecker);
        options.addMenuListener(statusChecker);
        help.addMenuListener(statusChecker);
    }
    
    public static boolean hasMenuOpen() {
        return menusOpen.get() > 0;
    }
    
    public void setupDelayedActions() {
        
        importMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                main.cmdImportMapActionPerformed(evt);
            }
        });
        importMapFromBMP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                main.importMapFromBMP();
            }
        });
        bmpSizeCalculator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                main.showBMPSizeCalculator();
            }
        });
        adjustSafety.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                main.cmdSafetyLevelActionPerformed(evt);
            }
        });
        exportToCSV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                main.cmdExportToCSVActionPerformed(evt);
            }
        });
        importFromCSV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                main.cmdImportFromCSVActionPerformed(evt);
            }
        });
        changeSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                main.openSettingsWindow(evt);
            }
        });
        
        addCustomMap.addActionListener(new ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                main.enableCustomMap(evt);
            }
        });
        
        appendMapToRight.addActionListener(new ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MapExtender.appendMapToRight();
            }
        });
        
        toggleGrid.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.settings.gridEnabled = !Main.settings.gridEnabled;
                Main.triggerMapUpdates();
            }
        });
        
        toggleCoordinates.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.settings.xyOnMapEnabled = !Main.settings.xyOnMapEnabled;
                Main.triggerMapUpdates();
            }
        });
        adjustZoom.addActionListener(new ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                main.changeZoomPercentage(evt);
            }
        });
        changeBuildingsInManyCities.addActionListener(new ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frmBuildingsInCities bldgForm = new frmBuildingsInCities(Main.biqFile.get(Main.biqIndex));
                bldgForm.setLocationRelativeTo(Main.mainMain);
                bldgForm.setVisible(true);
            }
        });
        polarWorldWrap.addActionListener(new ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                main.showPolarOptions(evt);
            }
        });
        redistributeGrassland.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                main.showRedistributeGrassland();
            }
        });
        flipMap.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                MapFlipper.flip(Main.biqFile.get(Main.biqIndex));
            }
        });
        terrainStats.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                String stats = MapStats.calculateMapStats(Main.biqFile.get(Main.biqIndex));
                JOptionPane.showMessageDialog(null, stats);
            }
        });
        cityStats.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                String moreStats = MapStats.getCityStats(Main.biqFile.get(Main.biqIndex));
                JOptionPane.showMessageDialog(null, moreStats);
            }
        });
        unitStats.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                UnitStatisticsFrame usf = new UnitStatisticsFrame(Main.biqFile.get(Main.biqIndex));
                usf.setIconImage(icon);
                usf.setLocationRelativeTo(Main.mainMain);
                usf.setVisible(true);
            }
            
        });
        clearCities.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirmation = JOptionPane.showConfirmDialog(null, "This will clear all cities from the map.  Do you really wish to do this?.", "Clear All Cities", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    main.clearAllCities();
                }
            }
        });
        clearUnits.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirmation = JOptionPane.showConfirmDialog(null, "This will clear all units from the map.  Do you really wish to do this?.", "Clear All Units", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    main.clearAllUnits();
                }
            }
        });
        memoryInfo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {        
                long maxAllocatable = Runtime.getRuntime().maxMemory();
                long allocatedToJVM = Runtime.getRuntime().totalMemory();
                long allocatedButFree = Runtime.getRuntime().freeMemory();

                long inUse = (allocatedToJVM - allocatedButFree)/1024;
                long max = maxAllocatable/1024;
                long allocated = allocatedToJVM/1024;
                allocatedButFree = allocatedButFree/1024;

                double percent = (inUse*100.)/max;
                
                String unit = " KB";
                
                if (inUse > 16384) {
                    inUse = inUse/1024;
                    max = max/1024;
                    allocated = allocated/1024;
                    allocatedButFree = allocatedButFree/1024;
                    unit = " MB";
                }
                
                StringBuilder sb = new StringBuilder();
                sb.append("<html>Current memory usage:<br><br>").append(inUse).append(unit).append(" is currently being used.<br>");
                sb.append(max).append(unit).append(" is the maximum the program can use.<br>");
                sb.append(String.format("%1.1f", percent)).append("% of the maximum memory is in use.<br><br>");
                sb.append(allocated + unit + " of your system memory is allocated to Java.<br>");
                sb.append(allocatedButFree + unit + " of the above is free, giving the current memory usage.");
                sb.append("<br><br>Java Vendor: ").append(System.getProperty("java.vm.vendor"));
                sb.append("<br>").append("Java runtime version: ").append(System.getProperty("java.version"));
                if (JavaVersion.comSunJavaFXWarning()) {
                    sb.append("<br>").append("Cannot filter lists with this Java version.  Please launch with the launcher.");
                    sb.append("<br>").append("If you are running with the launcher, report the issue to the CivFanatics thread.");
                }
                sb.append("</html>");
                
                JOptionPane.showMessageDialog(null, sb.toString(), "Java and Memory Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        benchmarkMap.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "<html>The program will now benchmark the map.  It will be unresponsive for 10 seconds while doing so.<br/>Then it will report the average FPS, when rendering full tilt.<br/>This is equivalent to how fast it renders when you are scrolling constantly.<br/>Due to optimizations, the map usually spends most of its time sleeping.");
                Main.pnlTabs.mapTab.map.benchmark();
            }
        });
        enableCustomRules.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {      
                Main.addCustomRules();
                enableCustomRules.setEnabled(false);
            }
        });
        enableCustomPlayerData.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {      
                Main.addCustomPlayerData();
                enableCustomPlayerData.setEnabled(false);
            }
        });
    }
    
    void enablePostOpenMenus(IO biq) {        
        save.setEnabled(true);
        exportToCSV.setEnabled(true);
        importFromCSV.setEnabled(true);
        changeSpecialActions.setEnabled(true);
        mapMenu.setEnabled(true);
        enableCustomRules.setEnabled(!biq.hasCustomRules());
        enableCustomPlayerData.setEnabled(!biq.hasCustomPlayerData());
    }
    
    void enableSavMenus() {
        histograph.setEnabled(true);
    }
    
    void enableMapEditingOptions() {
        adjustZoom.setEnabled(true);
        polarWorldWrap.setEnabled(true);
        redistributeGrassland.setEnabled(true);
        flipMap.setEnabled(true);
        changeBuildingsInManyCities.setEnabled(true);
        stats.setEnabled(true);
    }
    
    void disableMapEditingOptions() {
        adjustZoom.setEnabled(false);
        //Polar/wrapping is always enabled
        polarWorldWrap.setEnabled(true);
        redistributeGrassland.setEnabled(false);
        flipMap.setEnabled(false);
        changeBuildingsInManyCities.setEnabled(false);
        stats.setEnabled(false);
    }
    
    void updateRecentFiles(String newBIQName, int max) {
        recent.removeAll();
        for (int i = max - 1; i > 0; i--) {
            recentFiles[i] = recentFiles[i - 1];
        }
        recentFiles[0] = new JMenuItem(newBIQName);
        recentFiles[0].addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                main.openFile(e.getActionCommand());
            }
        });
        for (int i = 0; i < 5; i++) {
            if (recentFiles[i] != null) {
                recent.add(recentFiles[i]);
            }
        }
    }
    
    public JMenuItem getHelp() {
        return viewHelp;
    }
    
    public JMenuItem getAbout() {
        return about;
    }
    
    public JMenuItem getSpecialActions() {
        return changeSpecialActions;
    }
    
    class HistographDataThread extends Thread {
        BiFunction<HistTurn, Integer, Integer> histographExtractorFunction;
        String label;
        
        public HistographDataThread(String label, BiFunction<HistTurn, Integer, Integer> histographExtractorFunction) {
            this.histographExtractorFunction = histographExtractorFunction;
            this.label = label;
        }
        
        public void run() {            
            try {
                Stage stage = new Stage();

                SAV sav = Main.currentSAV;

                int numPlayers = sav.getGameData().getNumPlayers();
                List<String> players = new ArrayList<>();
                List<Color> colors = new ArrayList<>();
                for (int i = 1; i <= numPlayers; i++) {
                    int civIndex = sav.players[i].getRaceID();
                    int color = sav.players[i].getColor();
                    colors.add(Main.mainMain.colors[color]);
                    if (civIndex > -1) {
                        String civName = sav.getEmbeddedRules().getEmbeddedRules().civilization.get(civIndex).getName();
                        players.add(civName);
                    }
                }

                CustomHistograph histograph = new CustomHistograph();
                List<List<Integer>> allScores = new ArrayList<>();
                for (int i = 1; i <= numPlayers; i++) {
                    allScores.add(sav.histograph.getHistographScoresForPlayer(i, histographExtractorFunction));
                }
                histograph.sendData(label, allScores, players, colors);
                histograph.start(stage);
            }
            catch(Exception ex) {
                logger.error("Failed to start histograph", ex);
            }
        }
    }
}
