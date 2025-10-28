
package com.civfanatics.civ3.xplatformeditor.specialty;

import com.civfanatics.civ3.xplatformeditor.FileIO;
import com.civfanatics.civ3.xplatformeditor.Main;
import static com.civfanatics.civ3.xplatformeditor.Main.settings;
import com.civfanatics.civ3.xplatformeditor.Settings;
import com.civfanatics.civ3.xplatformeditor.utils;
import java.awt.Color;
import java.awt.Container;
import java.awt.FileDialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import org.apache.log4j.Logger;

/**
 * This file contains info about start page widgets.
 * @author Andrew
 */
public class StartupWidgets {
    
    static Logger logger = Logger.getLogger(StartupWidgets.class);
    
    public static JPanel setupStartWidgets(Container pane, GridBagConstraints gc, JPanel pnlTemporary, Consumer<String> fileOpener) {
        //Add a dummy JPanel to get the menus to display at the top.
        gc.gridx = 0;
        gc.gridy++;
        gc.weighty = 1.0;
        gc.weightx = 1.0;
        gc.gridwidth = 12;
        gc.fill = GridBagConstraints.BOTH;
        pnlTemporary = new JPanel();
        GridLayout gl = new GridLayout(1, 2, 5, 0);
        pnlTemporary.setLayout(gl);
        //pnlTemporary.setLayout(new GridBagLayout());
        
        JPanel pnlStartLeft = new JPanel();
        JPanel pnlStartRight = new JPanel();
        
        GridBagConstraints startConstraints = new GridBagConstraints();
        startConstraints.weightx = 1;
        startConstraints.weighty = 1;
        startConstraints.fill = GridBagConstraints.BOTH;
        startConstraints.anchor = GridBagConstraints.NORTH;        
        
        pnlTemporary.add(pnlStartLeft, startConstraints);
        startConstraints.gridx = 1;
        pnlTemporary.add(pnlStartRight, startConstraints);
        pnlTemporary.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Quick Links"));        
        
        //Left
        GridBagLayout leftLayout = new GridBagLayout();
        GridBagConstraints leftConstraints = new GridBagConstraints();
        pnlStartLeft.setLayout(leftLayout);
        leftConstraints.weightx = 1;
        leftConstraints.weighty = 0;
        leftConstraints.gridy = 0;
        leftConstraints.fill = GridBagConstraints.HORIZONTAL;
        leftConstraints.anchor = GridBagConstraints.NORTH;
        leftConstraints.ipadx = 10;
        leftConstraints.ipady = 10;
        
        //Most Recent File quick-link
        JPanel pnlMostRecentFile = new JPanel();
        pnlMostRecentFile.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Resume Editing"));
        pnlMostRecentFile.setLayout(new GridBagLayout());
        GridBagConstraints mrfConstraints = new GridBagConstraints();
        mrfConstraints.fill = GridBagConstraints.NONE;
        mrfConstraints.weightx = 0;
        mrfConstraints.weighty = 1;
        mrfConstraints.gridy = 0;
        mrfConstraints.anchor = GridBagConstraints.NORTH;
        //pnlMostRecentFile.setLayout(new GridLayout(1, 2, 5, 0));
        JButton openMostRecent = new JButton("Open Most Recent File");
        openMostRecent.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                fileOpener.accept(Settings.recentFiles[0]);
            }

        });
        JLabel mostRecentFileName = new JLabel("File: " + Settings.recentFiles[0]);
        pnlMostRecentFile.add(openMostRecent, mrfConstraints);
        mrfConstraints.gridy = 1;
        pnlMostRecentFile.add(mostRecentFileName, mrfConstraints);
        if (Settings.recentFiles[0] == null) {
            pnlMostRecentFile.setVisible(false);
        }
        pnlStartLeft.add(pnlMostRecentFile, leftConstraints);
        
        if (settings.noConfigFileAtStart) {
            JPanel pnlCopySettings = getPnlCopySettings(mostRecentFileName, pnlMostRecentFile);
            pnlStartLeft.add(pnlCopySettings, leftConstraints);
            
            leftConstraints.anchor = GridBagConstraints.NORTH;
            leftConstraints.gridy = leftConstraints.gridy + 1;;
        }
        
        JPanel pnlSpaceDummy = new JPanel();
        leftConstraints.weighty = 1;
        leftConstraints.gridy = leftConstraints.gridy + 1;
        pnlStartLeft.add(pnlSpaceDummy, leftConstraints);
        
        
        //Right
        GridBagLayout rightLayout = new GridBagLayout();
        GridBagConstraints rightConstraints = new GridBagConstraints();
        pnlStartRight.setLayout(rightLayout);
        rightConstraints.weightx = 1;
        rightConstraints.weighty = 0;
        rightConstraints.gridy = 0;
        rightConstraints.fill = GridBagConstraints.HORIZONTAL;
        rightConstraints.anchor = GridBagConstraints.NORTH;
        rightConstraints.ipadx = 10;
        rightConstraints.ipady = 10;
        
        JPanel pnlTipOfTheDay = getPnlTipOfTheDay();
        pnlStartRight.add(pnlTipOfTheDay, rightConstraints);
        
        
        long maxAllocatable = Runtime.getRuntime().maxMemory();
        if (maxAllocatable < 384 * 1024 * 1024 && Main.GRAPHICS_ENABLED) {
            JPanel pnlLowTotalMemory = getPnlLowTotalMemory();
            rightConstraints.gridy = rightConstraints.gridy + 1;
            pnlStartRight.add(pnlLowTotalMemory, rightConstraints);
        }
        
        
        JPanel pnlSpaceDummy2 = new JPanel();
        rightConstraints.weighty = 1;
        rightConstraints.gridy = rightConstraints.gridy + 1;
        pnlStartRight.add(pnlSpaceDummy2, rightConstraints);
        
        //Colors, for debugging layout
        /**
        pnlStartLeft.setBackground(Color.green);
        pnlStartRight.setBackground(Color.magenta);    
        pnlMostRecentFile.setBackground(Color.cyan);
        pnlTemporary.setBackground(Color.orange);
        pnlCopySettings.setBackground(Color.yellow);
        pnlTipOfTheDay.setBackground(Color.WHITE);
        * **/
        
        pnlTemporary.setOpaque(true);
        pane.add(pnlTemporary, gc);
        
        return pnlTemporary;
    }    
    
    private static JPanel getPnlLowTotalMemory() {
        //Copy Settings
        JPanel pnlLowMemory = new JPanel();
        pnlLowMemory.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Low Memory"));
        pnlLowMemory.setLayout(new GridBagLayout());
        GridBagConstraints csConstraints = new GridBagConstraints();
        csConstraints.fill = GridBagConstraints.BOTH;
        csConstraints.weightx = 1;
        csConstraints.weighty = 1;
        csConstraints.gridy = 0;
        //pnlMostRecentFile.setLayout(new GridLayout(1, 2, 5, 0));
        long maxAllocatableMB = Runtime.getRuntime().maxMemory() / 1000 / 1000;
        JLabel lowMemoryInfo = new JLabel("<html><h4><font color='red'>Insufficient Memory for Graphics</font></h4>"
                + "<p>The editor has detected that its maximum available memory is <b><font color='red'>" + maxAllocatableMB + " MB</font></b> of memory.</p>"
                + "<br/>"
                + "<p>It is recommended that this be at least <b>384 MB</b> if you plan to use the map.  If you don't wish to use the map, disable the graphics by unchecking the box at Preferences -> Main -> Map Enabled.</p>"
                + "<br/><p>To fix this, launch the editor via launcher.jar, which automatically increases available memory.</p>");
        
        pnlLowMemory.add(lowMemoryInfo, csConstraints);
        return pnlLowMemory;
    }

    private static JPanel getPnlCopySettings(JLabel mostRecentFileName, JPanel pnlMostRecentFile) {
        //Copy Settings
        JPanel pnlCopySettings = new JPanel();
        pnlCopySettings.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Copy Settings"));
        pnlCopySettings.setLayout(new GridBagLayout());
        GridBagConstraints csConstraints = new GridBagConstraints();
        csConstraints.fill = GridBagConstraints.NONE;
        csConstraints.weightx = 0;
        csConstraints.weighty = 1;
        csConstraints.gridy = 0;
        //pnlMostRecentFile.setLayout(new GridLayout(1, 2, 5, 0));
        JButton copySettings = new JButton("Copy Settings");
        JLabel copySettingsInfo = new JLabel("<html>Copy your settings from a previous editor installation.  This includes everything within Options --> Editor Settings, additional cached preferences, such as BMP import colors, and any bundled JRE for Windows XP.</html>");
        copySettings.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File folder = FileIO.getFile(FileDialog.LOAD, true, null, "Choose Old Install Folder");
                    File[] oldInstallFiles = folder.listFiles();
                    boolean hasBmpColors = false;
                    boolean hasXPJRE = false;
                    for (File oldFile : oldInstallFiles) {
                        if (oldFile.getName().equals("civ3editor.ini")) {
                            Files.copy(oldFile.toPath(), Paths.get("civ3editor.ini"), StandardCopyOption.REPLACE_EXISTING);
                        }
                        else if (oldFile.getName().equals("bmpcolors.txt")) {
                            Files.copy(oldFile.toPath(), Paths.get("bmpcolors.txt"), StandardCopyOption.REPLACE_EXISTING);
                            hasBmpColors = true;
                        }
                        else if (oldFile.getName().equals("XP_launcher.vbs")) {
                            Files.copy(oldFile.toPath(), Paths.get("XP_launcher.vbs"), StandardCopyOption.REPLACE_EXISTING);
                            hasXPJRE = true;
                        }
                        else if (oldFile.getName().equals("Editor_XP.bat")) {
                            Files.copy(oldFile.toPath(), Paths.get("Editor_XP.bat"), StandardCopyOption.REPLACE_EXISTING);
                            hasXPJRE = true;
                        }
                        else if (oldFile.getName().equals("jre1.8_111")) {
                            //Copy recursively
                            utils.copy(oldFile.getCanonicalPath(), "jre1.8_111", false);
                            hasXPJRE = true;
                        }
                        else if (oldFile.getName().equals("bin")) {
                            //Copy 7z if it's there
                            File[] binFiles = oldFile.listFiles();
                            for (File binFile : binFiles) {
                                if (binFile.getName().equals("7z")) {
                                    utils.copy(binFile.getCanonicalPath(), "bin/7z", false);
                                }
                            }
                        }
                    }

                    //Apply the new settings
                    Main.processConfigFile("");  //TODO: Could this be from a BIQ direct opening?
                    copySettings.setEnabled(false);
                    String bmpText = "";
                    String xpText = "";
                    if (hasBmpColors) {
                        bmpText = "<br />Your BMP colors settings have also been successfully imported.";
                    }
                    if (hasXPJRE) {
                        xpText = "<br />Your bundled JRE for Windows XP compatibility has also been successfully imported.  A restart will be required for it to take effect.";
                    }
                    copySettingsInfo.setText("<html>Your settings have been successfully imported from " + folder.getName() + " and are now in effect (except those which require a restart to switch).  You may view them in Options -> Editor Settings (Ctrl+P)." + bmpText + xpText + "</html>");
                    
                    //Restore "Resume Editing" quick link, if applicable
                    if (Settings.recentFiles[0] != null) {
                        mostRecentFileName.setText("File: " + Settings.recentFiles[0]);
                        pnlMostRecentFile.setVisible(true);
                    }
                }
                catch(IOException ex) {
                    logger.error("Error while copying old settings", ex);
                    JOptionPane.showMessageDialog(null, "An error occurred while copying the old install's settings.  See the log for details", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });
        pnlCopySettings.add(copySettings, csConstraints);
        csConstraints.gridy = 1;
        csConstraints.weightx = 1;
        csConstraints.fill = GridBagConstraints.HORIZONTAL;
        pnlCopySettings.add(copySettingsInfo, csConstraints);
        return pnlCopySettings;
    }    

    private static JPanel getPnlTipOfTheDay() {
        JPanel pnlTipOfTheDay = new JPanel();
        pnlTipOfTheDay.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Editor Feature Spotlight"));
        pnlTipOfTheDay.setLayout(new GridBagLayout());
        GridBagConstraints tipConstraints = new GridBagConstraints();
        tipConstraints.fill = GridBagConstraints.BOTH;
        tipConstraints.weightx = 1;
        tipConstraints.weighty = 1;
        tipConstraints.gridy = 0;
        tipConstraints.anchor = GridBagConstraints.NORTH;
        String[] tips = getTips();
        //In Java 8, JTextPane lets us copy text easily.  In Java 5, might need JLabel as it appears JTextPane is using fancier rendering of the HTML.
        //They do require some format changes.  Notably, JTextPane adds extra space around <p> tags, like modern browsers, which JLabel doesn't.  Space after </h4> also makes the initial <p> tag unnecessary.
        //JLabel lblTipOfTheDay = new JLabel("<html><p>Testing Tip of the Day with Label and let's add more text and let's add more text and let's add more text<p>Newline! and let's add more text and let's add more text and let's add more text and let's add more text and let's add more text and let's add more text and let's add more text</p></html>");
        //lblTipOfTheDay.setText(tip7);
        
        JTextPane tipTextPane = new JTextPane();
        tipTextPane.setContentType("text/html");
        tipTextPane.setEditable(false);
        tipTextPane.setBackground(null);
        tipTextPane.setBorder(null);
        int tip = ThreadLocalRandom.current().nextInt(0, tips.length);
        String tipText = tips[tip];
        if (settings.noConfigFileAtStart) {
            String tip0 = "<h4>Welcome to the Editor!</h4>"
                    + "To get started, go to File -> Open, and select a BIQ to open.  The editor will automatically load all required graphics, and you will then be able to edit any scenario properties, or the map."
                    + "<p>When the editor starts in the future, tips pointing out features of the editor that differ from the Firaxis editor, or more efficient ways of using the editor, will appear here.</p>"
                    + "<p>For now, you can choose File -> Open to open a scenario.  Once you've opened a scenario, all the various rule section tabs will appear, including the map.</p>"
                    + "<p>Nearly all features of the Firaxis editor are supported.  Most are fairly easy to find, but a few may be in different places.  Of particular note is that you right-click on a list of units/technologies/etc. to add/delete/rename, rather than having buttons for these.  Screen space was at a premium on some tabs back in the day!</p>"
                    + "";
            tipText = tip0;
        }
        String tipJava11 = "<h4><font color='red'>Java 11 Compatibility</font></h4>"
                + "The editor is currently running an incompatible build of Java 11, which removed some components that the editor depends on.  Unfortunately, this means many parts of the editor will not work."
                + "<p>You can fix this by installing the \"Full\" Liberica JDK, or the \"JDK FX\" build of Azul Java; see the thread for details.</p>"
                + "<p>You can also safely use the 1.19 Legacy version on Java 11, on any operating system, as it does not use any of the components that were removed in Java 11.</p>"
                + "<p>For full information and updates, you may copy this link to your browser to view the latest updates on the Java 11 support status: https://forums.civfanatics.com/threads/cross-platform-editor-for-conquests-now-available.377188/page-52#post-15224365 </p>";
        if (JavaVersion.incompatibleJavaVersion(System.getProperty("java.version"))) {
            tipText = tipJava11;
        }
        else if (JavaVersion.comSunJavaFXWarning()) {
            logger.info("Setting Java 17+ warning");
            String tipJava17 = "<h4><font color='red'>Java 17+ Compatibility</font></h4>"
                    + "Some components of the editor require being launched with the launcher on Java 17 or later in order to function"
                    + "<p>These components are not working currently; please start with the launcher.  Components affected:</p>"
                    + "<ul>"
                    + "<li>Filtering lists (e.g. technology, resource) will not work</li>"
                    + "<li>Histograph charts when SAV files are opened</li>"
                    + "</ul>"
                    + "<p>It is highly recommended to restart the editor with the launcher, especially if modifying tabs with sortable lists, to avoid any issues."
                    ;
            tipText = tipJava17;
        }
        
        String rgb = getTipRGBColor();
        System.out.println(rgb);
        String text = "<html><body style=\"color: " + rgb + "\">" + tipText + "</body></html>";
        tipTextPane.setText(text);
        pnlTipOfTheDay.add(tipTextPane, tipConstraints);
        return pnlTipOfTheDay;
    }

    private static String getTipRGBColor() {
        String red = Integer.toHexString(SystemColor.textText.getRed());
        String green = Integer.toHexString(SystemColor.textText.getGreen());
        String blue = Integer.toHexString(SystemColor.textText.getBlue());
        if (red.length() == 1) {
            red = "0" + red;
        }
        if (green.length() == 1) {
            green = "0" + green;
        }
        if (blue.length() == 1) {
            blue = "0" + blue;
        }
        String rgb = "#" + red + green + blue;
        return rgb;
    }
    
    private static String[] getTips() {
        String tip1 = "<h4>Custom Civ Colors</h4>"
                + "From the CIV tab, you can create custom civ colors by pressing the Edit Civ Colors button.  This allows you to go beyond the default 32 colors that ship with Civ3, and create civ-specific colors limited only by your imagination and a choice of 16.7 million colors.</p>"
                + "<p>And if that's not enough?  There are 35 palette choices per civ, affecting items ranging from the circle around the leader on the diplo screen to the city border colors, for 587 million possible colors per civ!</p>";
        String tip2 = "<h4>Deepwater Harbours</h4>"
                + "When painting Sea tiles on the map, you may optionally add Deepwater Harbors.  These should be placed over coast tiles, and replace the coast with sea, but do not change the graphics.  As the graphics do not change, it is recommended to also add a resource to the tile to give a visual indicator."
                + "<p>When combined with adding ships with the Wheeled attribute, and making Coast impassable to Wheeled units, this lets you create ships that will only pass along certain sea lanes.</p>";
        String tip3 = "<h4>Downloading Units</h4>"
                + "The Download Units button, on the right side of the Units Tab, allows you to download units from CivFanatics directly into your scenario.  Decompression of the units, renaming the .ini file to match your desired name, and placing it in the proper location are all taken care of for you."
                + "<p>This works for single unit downloads (as opposed to unit packs), and uses 7-Zip on Windows to perform the decompression (which can be automatically downloaded for you).  It should work with any unit in the Downloads Database.</p>"
                + "<p>In theory this could be extended to other Downloads Database items as well.  If you'd like to be able to download more types of items (leaderheads, terrain packs, etc.), express interest in the thread!</p>";
        String tip4 = "<h4>Item Filtering</h4>"
                + "The Tech, Unit, Building, and Good tabs support filtering the items in the list by typing into the box above the list of items.  This goes far beyond simply filtering by name, and fairly complex queries, such as \"ships with attack of at least 6 that are available to Persia\" are possible."
                + "<p>To see the list of possible filters, and how to enter them, view the Help sections for these tabs.</p>";
        String tip5 = "<h4>Item Re-Ordering</h4>"
                + "The Tech, Unit, and Good tabs support re-ordering the items in the list.  To re-order items, click and drag the one you wish to move.  As you move the mouse over another item, that iteem will be highlighted in a lighter blue than the one you are dragging.  Releasing will move the dragged item below the one with lighter blue highlighting, as indicated by the black line below that item."
                + "";
        String tip6 = "<h4>No More Arbitrary Limits</h4>"
                + "Some of the limits in the Firaxis editor are necessary due to the BIQ format, but others are arbitrary.  This editor allows you to experiment with going beyond those limits.  By default, a few items where it's known to be safe to go beyond these limits are enabled.  However, you can enable this for more areas by going to Options -> Adjust Safety Level, and setting the limits to Exploratory or Minimal."
                + "<p>This allows possibilities such as negative-maintenance buildings, super-costly units or buildings... and possibly other aspects that no one has tried before.</p>"
                + "";
        String tip7 = "<h4>Importing Maps from Images</h4>"
                + "The Map -> Import Map from BMP option allows you to import a map from a bitmap image.  This is designed to work with sateillite imagery, although it should work with other raw sources of color-coded maps as well.  It will ask you to map colors to Civ3 terrain types, and then automatically analyze the map and create a Civ3 map based on it, while observing Civ3 limitations such as no desert bordering tundra."
                + "<p>It does require that the BMP have specific dimensions, but these are relative to the Civ3 map size - you can import a map into any size of Civ3 map.  The Map -> BMP Size Calculator option will help you with resizing your BMP file to a size the editor can work with.  It likely will be rather large, but you won't need to keep the resized image after the import.</p>"
                + "<p>If the map doesn't look quite right on the first import, you can try again, and the editor will remember which colors you chose to map to which terrains, so you only have to enter those you wish to change on the second try.</p>"
                + "<p>Some manual editing of the map after import is to be expected (adding rivers and resources if nothing else, but likely some tweaking as well), but this is a great way to speed up the initial creation of a map if you have relevant imagery.</p>"
                + "";
        String tip8 = "<h4>City and Leader Editing</h4>"
                + "You can paste newline-separated lists of cities or leaders into the city, scientific leader, or military leader lists on the CIV tab.  After copying, click in the entry area, type Ctrl+V, and the copied entries will be added."
                + "<p>You can also rename cities or leaders by double-clicking them in the list.</p>"
                + "";
        String tip9 = "<h4>Detecting Invalid Settings</h4>"
                + "The editor will notify you when you save if it detects settings that can cause problems in-game."
                + "<p>Currently, it checks for loops in unit upgrade paths, and the Phantom Resource Bug.  These checks can be disabled in the Settings, but are recommended as they have deletrious effects in-game and are difficult to manually detect.</p>"
                + "";
        String tip10 = "<h4>Wonderful Buildings</h4>"
                + "In the Firaxis editor, many building attributes are only available to wonders.  In this editor, however, a regular building can have \"wonder\" attributes."
                + "<p>The effect of these when applied to buildings have not all been verified, and some may make more sense when applied to wonders.  But if you want a regular building to require a victorious army?  Go right ahead!</p>"
                + "";
        String tip11 = "<h4>Autosave</h4>"
                + "Power went out and you hadn't saved for awhile?  Good news - the editor keeps auto-saves of your work, by default up to 10 auto saves, saved every 4 minutes.  They are located in the editor's folder.  This can be modified under Settings -> Autosave.  They're also a nice fallback if you realize you really didn't want to make that most recent change you made, but already saved."
                + "";
        String tip12 = "<h4>Map Settings</h4>"
                + "You can tweak a variety of map settings under Settings -> Map.  This includes what layers are visible (montains, forest, etc.), whether the grid is displayed, and how sensitive the map is when drawing rivers."
                + "";
        String tip13 = "<h4>Keyboard Shortcuts</h4>"
                + "A number of actions have keyboard shortcuts.  Menus and submenus can be accessed via the Alt key, plus the letter of the menu which is underlined.  For example, Alt+M followed by Alt+G will bring up the \"Redistribute Bonus Grassland\" option under the Map menu.  Some menu actions have Ctrl-based shortcuts as well."
                + "<p>In addition, the map supports pressing \"T\" to select terrain type, \"O\" to select overlay type, and the 1, 3, 5, and 7 keys to select terrain radius, similar to the Firaxis editor.</p>"
                + "<p>Another useful shortcut is that after starting the editor, hitting the spacebar will open your most-recently-opened file.</p>"
                + "";
        String tip14 = "<h4>More Intuitive Unit Icon Selection</h4>"
                + "Although you can still select unit icons by entering an index on the Unit tab if you wish, it's no longer necessary to do it that way."
                + "<p>Instead, you can click the \"Select Icon\" button above the icon, and select it visually from your units32.pcx file (including if you have a custom units32.pcx).</p>"
                + "";
        String tip15 = "<h4>Low-End Modding</h4>"
                + "Does your cabin in the forest only have a computer with 64 MB of memory and Windows ME?  That doesn't mean you can't mod Civ3!"
                + "<p>The first post of the forum thread has a link to the most recent version of the editor that will run on old operating systems.  And while editing the map is fairly memory-intensive due to the graphics, if need be you can open the Settings, uncheck \"Map Enabled\", and restart to enable graphics-less rule editing, allowing the editor to run with very low amounts of memory.</p>"
                + "";
        String tip16 = "<h4>Reducing Tedium in City Building</h4>"
                + "Tired of manually adding a building to every city, or ensuring all size 13+ cities have hospitals?  There's a better way now.  For buildings that should appear in most cities, you can go to Options -> Change Special Actions -> Set Buildings in Many Cities at Once."
                + "<p>This does what it suggests.  You can easily add buildings to all cities matching certain criteria - size, being coastal, and being owned by a particular player or civ.  You can always customize more from the Map tab, but this can speed up the initial build-out of buildings.</p>"
                + "";
        String tip17 = "<h4>Moving a Whole City</h4>"
                + "Did you realize you should have placed that city one tile to the northeast, but already spent a bunch of time customize it and its units?  Now moving a city is easier than moving your car!"
                + "<p>Go to the Map Tab, click on the city that needs to be moved, and choose the \"Relocate city\" or \"Relocate city with units\" option below its list of buildings.  Then click on its new location on the map, and it will automatically be moved there.</p>"
                + "";
        String tip18 = "<h4>Comparing BIQ Files</h4>"
                + "The BIC tab allows you to compare two different BIQ files, with the results being put in a text file.  This can allow you to spot differences between BIQs more quickly than manually comparing various BIQ tabs."
                + "<p>To use it in this manner, open two files in the editor at once, check the \"Compare Mode\" option on that tab, and select which sections you'd like to compare.  The select an output file, and click the Export! button.</p>"
                + "";
        String tip19 = "<h4>Found a Bug?</h4>"
                + "Although undesirable, occasionally a bug may occur.  In these situations, you can report the bug to the forum thread.  Particularly useful in debugging it are the steps to reproduce it.  Uploading the BIQ-in-progress can be very useful in debugging it, as many bugs only occur when a particular combinations of rules and actions are put together."
                + "<p>The editor also keeps a file called log.txt in its folder, which may contain additional information about any errors that occur.  If you encounter a bug and wish to report it, it's worth checking the log.txt file for any errors near the end of that file, and uploading them to the forum.  These can be very useful in pinpointing the cause of the bug.</p>"
                + "";
        String tip20 = "<h4>Import Map from BIQ</h4>"
                + "The Menu --> Import Map option allows you to import the map from another BIQ into your BIQ.  It will automatically match resources based on name, when resources in the map BIQ are present are your BIQ, and will leave out other resources, saving you time."
                + "<p>If the map BIQ already has cities or units, but you'd prefer a base terrain map, you can clear them with Map -> Clear -> (Cities or Units).</p>"
                + "<p>This allows you to more easily play your custom rule set with a variety of high-quality maps made by the community.</p>"
                + "";
        String tip21 = "<h4>Opportunities to Improve the Editor</h4>"
                + "The editor has been evolving for the better part of a decade, and has a lot more features today than it did five years ago.  There are a number of ways that you can help make it better."
                + "<p>One of those is testing the boundary conditions that have been unlocked in the editor.  What happens if a unit has negative defence?  Can you have a resource that provides food, but also provides negative shields?  Many of these areas have not been fully explored, and a comprehensive study would be useful, and could be incorporated into the editor, setting useful but not artificially restrictive limits.  I'd recommend focusing on one area at a time if you choose to explore this.</p>"
                + "<p>Other opportunities include writing code for the editor (likely the quickest way to implement your most-desired feature, if you have programming experience), and translating the editor so it's more usable across the world.</p>"
                + "<p>Finally, simply using the editor and providing feedback (both on what's useful, and what could use improvement) helps make it better.  Many of the new ideas and features over the years have come from those making mods with it.</p>"
                + "";
        String tip22 = "<h4>Advanced Filter Queries</h4>"
                + "Tech, unit, building, and resource filter queries allow the use of \"or\" and parentheses to form much more complex queries, such as \"(class=sea or class=air) cost>50\"  This functionality will also extend to all future section filters.."
                + "<p>To view documentation for this functionality, view the Help system and then \"List Filters\" under \"Editor Functionality\".</p>";
        String tip23 = "<h4>Editing Tech Civilopedia Icons</h4>"
                + "The Tech Tab now displays the large civilopedia icon for a tech.  If you click on the icon, it will open in your system's default PCX editor, if you have one specified, and you can edit it there.  The editor will have to be restarted to see the new icon in-editor."
                + "";
        
        String[] tips = {
            tip1, tip2, tip3, tip4, tip5, tip6, tip7, tip8, tip9, tip10, tip11, tip12, tip13, tip14, tip15, tip16, tip17, tip18, tip19, tip20, tip21, tip22
        };
        return tips;
    }
}
