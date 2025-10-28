/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.civfanatics.civ3.xplatformeditor;

import com.civfanatics.civ3.xplatformeditor.specialty.SafetyLevel;
import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * This class contains all the settings for the editor in one place, which we
 * can then pass around easily.
 * @author Andrew
 */
public class Settings {
    String openDir = "";
    public String bmpDir = "";
    public String civInstallDir = "";
    /**
     * numProcs is used as a manual override for the number of processors to be
     * used.  The "0" default value used here indicates that all available
     * processors should be used.  This may be overridden if problems are
     * occurring or for testing purposes.
     */
    String numProcs = "0";
    /**
     * Debugging level.  Default is info.
     */
    String debugLevel = "info";
    /**
     * Will be set to false either by reading in the "false" value, or by
     * it being changed by the first-time setup.
     */
    String firstRun = "true";
    public boolean noConfigFileAtStart = false;


    public boolean woodlandsEnabled = true;
    public boolean hillsEnabled = true;
    public boolean marshEnabled = true;
    public boolean irrigationEnabled = true;
    public boolean minesEnabled = true;
    public boolean fortificationsEnabled = true;
    public boolean roadsEnabled = true;
    public boolean resourcesEnabled = true;
    public boolean citiesEnabled = true;
    public boolean unitsEnabled = true;
    public boolean colonialBuildingsEnabled = true;
    public boolean riversEnabled = true;
    public boolean pollutionEnabled = true;
    public boolean bordersEnabled = true;
    public boolean barbarianCampsEnabled = true;
    public boolean goodyHutsEnabled = true;
    public boolean cratersEnabled = true;
    public boolean ruinsEnabled = true;
    public boolean vplsEnabled = true;
    public boolean cityNamesEnabled = true;
    public boolean xyOnMapEnabled = false;
    public boolean gridEnabled = false;
    public boolean showFog = false;
    int zoomQuality = Image.SCALE_DEFAULT;
    int drawReps = 5;
    
    boolean validateInfiniteUnitUpgrade = true;
    boolean validatePhantomResourceBug = true;
    
    int nextAutosave = 0;
    int maxAutosaves = 10;
    long autoSaveInterval = 240L;
    
    boolean autoArchive = false;

    String fontChoice = "Tahoma";
    boolean confirmQuit = true;


    static Logger logger = Logger.getLogger("Settings");
    
    static boolean horizontalScrolling = false;
    
    public static boolean showFoodAndShields = false;
    
    public String biqLanguage = "English";
    String editorLanguage = "English";
    
    public static String[] recentFiles = new String[5];
    
    int fontSize = 12;
    String lookAndFeel = "Metal";
    
    int height = 768;
    int width = 1030;
    
    public int riverProximityMax = 10;
    public int riverCornerRadius = 14;
    
    boolean useSwing = false;
    boolean useJavaFX = true;
    boolean forceSwing = false; //used to determine if using Swing by mandatory requirement due to incompatibility
    boolean forceSwingDueToJavaBug = false;
    
    //Version checking
    public boolean checkForUpdates = true;
    public String suppressedUpdateVersion = "";
    
    public String decompressionMethod = "7z x [filename] -y -o";
    public boolean sevenZipVerified = false;
    
    /**
     * Exports the settings to a configuration file.  The name of this file is
     * hardcoded as civ3editor.ini.
     * @throws ReadOnlyException If the file is read-only
     */
    public void exportConfigFile() throws ReadOnlyException
    {
        File config = new File("civ3editor.ini");
        if (!config.exists())
            try{
                config.createNewFile();
            }
            catch(java.io.IOException e)
            {
                logger.error("Could not create file for export - check disk space");
            }
        if (config.exists())    //should always exist at this point
        {
            if (!config.canWrite()) {
                logger.warn("Config file is read-only");
                throw new ReadOnlyException("civ3editor.ini is read-only");
            }
            FileWriter out = null;
            try{
                out = new FileWriter(config);
                out.write("openDir=" + openDir + Main.newline);
                out.write("civInstallDir=" + civInstallDir + Main.newline);
                out.write("bmpDir=" + bmpDir + Main.newline);
                out.write("debugLevel=" + debugLevel + Main.newline);
                out.write("numProcs=" + numProcs + Main.newline);
                out.write("firstRun=" + firstRun + Main.newline);
                out.write("nextAutosave=" + String.valueOf(nextAutosave) + Main.newline);
                out.write("maxAutosaves=" + String.valueOf(maxAutosaves) + Main.newline);
                out.write("autoSaveInterval=" + String.valueOf(autoSaveInterval) + Main.newline);
                out.write("autoArchive=" + String.valueOf(autoArchive) + Main.newline);
                out.write("fontChoice=" + fontChoice + Main.newline);
                out.write("confirmQuit=" + String.valueOf(confirmQuit) + Main.newline);
                out.write("graphicsEnabled=" + Boolean.toString(Main.GRAPHICS_ENABLED) + Main.newline);
                out.write("horizontalScrolling=" + Boolean.toString(Settings.horizontalScrolling) + Main.newline);
                out.write("biqLanguage=" + biqLanguage + Main.newline);
                out.write("editorLanguage=" + editorLanguage + Main.newline);
                
                out.write("validateInfiniteUnitUpgrade=" + Boolean.toString(validateInfiniteUnitUpgrade) + Main.newline);
                out.write("validatePhantomResourceBug=" + Boolean.toString(validatePhantomResourceBug) + Main.newline);
                
                //map zoom quality
                String zoomStr = "";
                if (this.zoomQuality == Image.SCALE_DEFAULT)
                    zoomStr = "balanced";
                else if (this.zoomQuality == Image.SCALE_FAST)
                    zoomStr = "quick";
                else if (this.zoomQuality == Image.SCALE_SMOOTH)
                    zoomStr = "quality";
                
                out.write("zoomQuality=" + zoomStr + Main.newline);
                
                for (int i = 0; i < 5; i++) {
                    if (recentFiles[i] != null) {
                        out.write("recent" + i + "=" + recentFiles[i] + Main.newline);
                    }
                }
                
                out.write("fontSize=" + fontSize + Main.newline);
                out.write("lookAndFeel=" + lookAndFeel + Main.newline);
                out.write("width=" + width + Main.newline);
                out.write("height=" + height + Main.newline);
                out.write("riverProximity=" + riverProximityMax + Main.newline);
                out.write("riverCornerRadius=" + riverCornerRadius + Main.newline);
                out.write("useSwing=" + Boolean.toString(useSwing) + Main.newline);
                out.write("checkForUpdates=" + Boolean.toString(checkForUpdates) + Main.newline);
                out.write("suppressedUpdateVersion=" + suppressedUpdateVersion + Main.newline);
                
                //safety levels
                out.write("safetyBLDG=" +((SafetyLevel)Main.safetyLevels.get("BLDG")).toString() + Main.newline);
                out.write("safetyCTZN=" +((SafetyLevel)Main.safetyLevels.get("CTZN")).toString() + Main.newline);
                out.write("safetyCULT=" +((SafetyLevel)Main.safetyLevels.get("CULT")).toString() + Main.newline);
                out.write("safetyDIFF=" +((SafetyLevel)Main.safetyLevels.get("DIFF")).toString() + Main.newline);
                out.write("safetyERA=" +((SafetyLevel)Main.safetyLevels.get("ERA")).toString() + Main.newline);
                out.write("safetyESPN=" +((SafetyLevel)Main.safetyLevels.get("ESPN")).toString() + Main.newline);
                out.write("safetyEXPR=" +((SafetyLevel)Main.safetyLevels.get("EXPR")).toString() + Main.newline);
                out.write("safetyFLAV=" +((SafetyLevel)Main.safetyLevels.get("FLAV")).toString() + Main.newline);
                out.write("safetyGOOD=" +((SafetyLevel)Main.safetyLevels.get("GOOD")).toString() + Main.newline);
                out.write("safetyGOVT=" +((SafetyLevel)Main.safetyLevels.get("GOVT")).toString() + Main.newline);
                out.write("safetyPLYR=" +((SafetyLevel)Main.safetyLevels.get("PLYR")).toString() + Main.newline);
                out.write("safetyRULE=" +((SafetyLevel)Main.safetyLevels.get("RULE")).toString() + Main.newline);
                out.write("safetyPRTO=" +((SafetyLevel)Main.safetyLevels.get("PRTO")).toString() + Main.newline);
                out.write("safetyTECH=" +((SafetyLevel)Main.safetyLevels.get("TECH")).toString() + Main.newline);
                out.write("safetyTERR=" +((SafetyLevel)Main.safetyLevels.get("TERR")).toString() + Main.newline);
                out.write("safetyTRFM=" +((SafetyLevel)Main.safetyLevels.get("TRFM")).toString() + Main.newline);
                out.write("safetyWSIZ=" +((SafetyLevel)Main.safetyLevels.get("WSIZ")).toString() + Main.newline);
                out.write("safetyRACE=" +((SafetyLevel)Main.safetyLevels.get("RACE")).toString() + Main.newline);
                out.write("safetyPROP=" +((SafetyLevel)Main.safetyLevels.get("PROP")).toString() + Main.newline);
                out.write("safetyMap=" + ((SafetyLevel)Main.safetyLevels.get("Map")).toString() + Main.newline);
                
                out.write("decompressionMethod=" + decompressionMethod + Main.newline);
                out.write("sevenZipVerified=" + Boolean.toString(sevenZipVerified) + Main.newline);
            }
            catch(java.io.IOException e){
                logger.error("Error exporting config file", e);
            }
            if (out != null) {
                try{
                    out.close();
                }
                catch(java.io.IOException e){
                    logger.error("Failed to close config file output", e);
                }
            }
        }
    }

    public void importConfigFile(File config)
    {
        Scanner in = null;
        try{
            in = new java.util.Scanner(config);
        }
        catch(FileNotFoundException e){
            logger.error("Config file not found", e);
        }
        while (in.hasNextLine())
        {
            String nextLine = in.nextLine();
            StringTokenizer tokenizer = new java.util.StringTokenizer(nextLine, "=\n");
            String ident = tokenizer.nextToken();
            String value = "";
            try{
                value = tokenizer.nextToken();
            }
            catch(NoSuchElementException e)
            {
                logger.warn("Missing value for key " + ident + " in config file.");
            }
            if (logger.isDebugEnabled())
            {
                logger.debug("ident: " + ident);
                logger.debug("value: " + value);
            }
            if (ident.equals("openDir"))
                openDir = value;
            else if (ident.equals("civInstallDir"))
                civInstallDir = value;
            else if (ident.equals("bmpDir"))
                bmpDir = value;
            else if (ident.equals("firstRun"))
                firstRun = value;
            else if (ident.equals("debugLevel"))
            {
                if (value.equals("trace"))
                    logger.getRootLogger().setLevel(Level.TRACE);
                if (value.equals("debug"))
                    logger.getRootLogger().setLevel(Level.DEBUG);
                else if (value.equals("info"))
                    logger.getRootLogger().setLevel(Level.INFO);
                else if (value.equals("warn"))
                    logger.getRootLogger().setLevel(Level.WARN);
                else if (value.equals("error"))
                    logger.getRootLogger().setLevel(Level.ERROR);
                debugLevel  = value;
            }
            else if (ident.equals("numProcs"))
            {
                try{
                    numProcs = value;
                }
                catch(NumberFormatException e){
                    numProcs = "0";
                }
            }
            else if (ident.equals("autoSaveInterval"))
            {
                try{
                    autoSaveInterval = Long.parseLong(value);
                }
                catch(NumberFormatException e){
                    autoSaveInterval = 240L;
                }
            }
            else if (ident.equals("maxAutosaves"))
            {
                try{
                    maxAutosaves = Integer.parseInt(value);
                }
                catch(NumberFormatException e){
                    maxAutosaves = 10;
                }
            }
            else if (ident.equals("nextAutosave"))
            {
                try{
                    nextAutosave = Integer.parseInt(value);
                }
                catch(NumberFormatException e){
                    nextAutosave = 0;
                }
            }
            else if (ident.equals("autoArchive"))
            {
                autoArchive = Boolean.valueOf(value);
            }
            else if (ident.equals("fontChoice"))
            {
                fontChoice = value;
            }
            else if (ident.equals("confirmQuit"))
            {
                confirmQuit = Boolean.valueOf(value);
            }
            else if (ident.equals("graphicsEnabled"))
            {
                Main.GRAPHICS_ENABLED = Boolean.valueOf(value);
            }
            else if (ident.equals("horizontalScrolling"))
            {
                Settings.horizontalScrolling = Boolean.valueOf(value);
            }
            else if (ident.equals("zoomQuality"))
            {
                if (value.equalsIgnoreCase("quick"))
                    zoomQuality = Image.SCALE_FAST;
                else if (value.equalsIgnoreCase("balanced"))
                    zoomQuality = Image.SCALE_DEFAULT;
                else if (value.equalsIgnoreCase("quality"))
                    zoomQuality = Image.SCALE_SMOOTH;
            }
            else if (ident.equals("biqLanguage"))
            {
                biqLanguage = value;
            }
            else if (ident.equals("editorLanguage")) {
                editorLanguage = value;
            }
            else if (ident.equals("validateInfiniteUnitUpgrade")) {
                validateInfiniteUnitUpgrade = Boolean.valueOf(value);
            }
            else if (ident.equals("validatePhantomResourceBug")) {
                validatePhantomResourceBug = Boolean.valueOf(value);
            }
            else if (ident.equals("recent0")) {
                recentFiles[0] = value;
            }
            else if (ident.equals("recent1")) {
                recentFiles[1] = value;
            }
            else if (ident.equals("recent2")) {
                recentFiles[2] = value;
            }
            else if (ident.equals("recent3")) {
                recentFiles[3] = value;
            }
            else if (ident.equals("recent4")) {
                recentFiles[4] = value;
            }
            else if (ident.equals("fontSize")) {
                fontSize = Integer.parseInt(value);
            }
            else if (ident.equals("lookAndFeel")) {
                lookAndFeel = value;
            }
            else if (ident.equals("width"))
            {
                try{
                    width = Integer.parseInt(value);
                }
                catch(NumberFormatException e){
                    width = 1030;
                }
            }
            else if (ident.equals("height"))
            {
                try{
                    height = Integer.parseInt(value);
                }
                catch(NumberFormatException e){
                    height = 768;
                }
            }
            else if (ident.equals("riverProximity")) {
                try {
                    riverProximityMax = Integer.parseInt(value);
                }
                catch(NumberFormatException e) {
                    riverProximityMax = 10;
                }
            }
            else if (ident.equals("riverCornerRadius")) {
                try {
                    riverCornerRadius = Integer.parseInt(value);
                }
                catch(NumberFormatException e) {
                    riverCornerRadius = 14;
                }
            }
            else if (ident.equals("useSwing")) {
                useSwing = Boolean.valueOf(value);
                if (useSwing) {
                    //Don't use JavaFX.  Note that even if useSwing is false,
                    //useJavaFX may also be false due to incompatibilites; in
                    //that case it falls back to Swing anyway.
                    useJavaFX = false;
                }
            }
            else if (ident.equals("checkForUpdates")) {
                checkForUpdates = Boolean.valueOf(value);
            }
            else if (ident.equals("suppressedUpdateVersion")) {
                suppressedUpdateVersion = value;
            }
            else if (ident.startsWith("safety")) {
                String safetyLevel = ident.substring(6);
                Main.safetyLevels.put(safetyLevel, SafetyLevel.valueOf(value));
            }
            else if (ident.equals("decompressionMethod")) {
                decompressionMethod = value;
            }
            else if (ident.equals("sevenZipVerified")) {
                sevenZipVerified = Boolean.valueOf(value);
            }
        }
        in.close();
        //do once-per-run setup
    }
}
