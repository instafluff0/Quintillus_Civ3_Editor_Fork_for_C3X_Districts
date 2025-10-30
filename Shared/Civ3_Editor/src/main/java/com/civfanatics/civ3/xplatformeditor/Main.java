package com.civfanatics.civ3.xplatformeditor;
/*
 * Totally complete tabs: BLDG, CIV, CTZN, CULT*, DIFF, ERA, ESPN, EXPR, GOOD, GOVT, PLYR, ScnProp*, TECH, TERR, TRFM, Unit*, WSIZ
 * * = not totally complete but good enough for release (very minor stuff)
 * 
 */

//FIX SCIENTIFIC LEADERS

/** 
 * June 30th (2011) notes:
 *  - Finish optimizing the cultural recalculations.  We don't need to recalculate culture for the whole map when a city
 *    changes owners, for instance.
 *  - I think I can make a method that creates a list of cities within a certain radius, and then do a bunch of stuff with
 *    them in methods that take said list as a parameter.  As opposed to now where I have multiple methods that create
 *    lists of cities within a certain radius.
 * 
 * July 12, 2012
 *  - The current way of doing bad value document listeners and stuff is very procedural.  It also makes us have to handle
 *    listeners all over the place.  It works well enough as a way to tack new functionality on to JTextFields.  But now that
 *    we have SuperJTextFields, it makes sense to internalize some of these functions into them.  So we can just tell the
 *    JTextField "hey, here are the upper and lower bounds, add a listener".  And, so that we can actually have an
 *    "are you valid?" method rather than the clunky foreground color one (even if it technically does work).
 *    This will also pave the way for making it easier to indicate required items.  And basically make things a bit less complicated.
 *  Good news.  It looks like I can transition this gradually - I can add a "required" functionality and still leave the old document
 *  listeners, too.  So I can gradually phase out the old listeners.
 */

//TO BE ADDED - indicates incomplete features
//MISSING - Indicates BIQ parts missing, either due to not knowing their function,
// or due to them not being implemented in-game
//Figured out I can use http://www.civfanatics.com/sotd/sotd100.jpg to properly import the TNT files.  Didn't know what they were before,
//but there's the Civ source code explaining exactly what it does... convenient.

/*
 * Main.java
 * TODO: Finish tab support for items with same name, extend that support to when new stuff is added.
 *
 * TODO: 11/1/2011: I'd like to make my lists alphabetizable.  I haven't done so yet since indexing is direct between item # and list #.
 * I'm thinking that perhaps if I extend the DefaultListModel I can solve this little issue.  I'd have a "index" and a "true index".
 * I might also have to extend the JList at the same time.  In particular, the getSelectedIndex method is being used to tie the item and
 * list #'s right now, and I'd need to devise a workaround.  One idea is on the next line:
 * 
 *       buildingIndex = lstBuildings.getModel().trueIndex(lstBuildings.getSelectedIndex());
 * 
 * The "CustomDefaultListModel" would contain the new trueIndex method, which would map the index by appearance to the index in BIQ.
 * Of course it would be simpler to just call myListModel.trueIndex(...).
 * 
 * I plan to get some extra sleep tonight so I don't plan to do this now but barring unexpected events (which is never a safe bet) it
 * sounds like it might possibly work in a nice way?
 * 
 * Another nice thing would be to integrate the add/delete/alphabetize/BIQ-order popup into the JList itself.  That would cut down on a
 * lot of code duplication and make maintenance considerably easier.  Booleans could control whether these items are enabled, or I could
 * use plain old regular run-of-the-mill JLists where I don't want this functionality.  I'd use the addMouseListener in JList (from Component)
 * to add the popup handling in my new super-JList.
 * 
 * Blue Monkey
 * Wish List: 
Utilities like MapTweaker allow a variety of things the Firaxis editor doesn't such as greater freedom in cropping the map and analysis of terrain ratios.
It's also possible to have more unusual map sizes.
It may be asking too much since all that exists now is hexadecimal editing, but apparently there is code buried for additional landmark terrains (http://forums.civfanatics.com/showthread.php?t=232267).
Those are the kinds of things needed for creative scenarios that Firaxis just didn't include.
 * 
 * Created on Apr 4, 2009, 4:33:45 PM
 */
import com.civfanatics.civ3.biqFile.BIQSection;
import com.civfanatics.civ3.biqFile.CITY;
import com.civfanatics.civ3.biqFile.CLNY;
import com.civfanatics.civ3.biqFile.GOOD;
import com.civfanatics.civ3.xplatformeditor.specialty.CSVPanel;
import com.civfanatics.civ3.xplatformeditor.specialty.AboutPanel;
import com.civfanatics.civ3.xplatformeditor.specialty.SafetyLevel;
import com.civfanatics.civ3.xplatformeditor.specialty.FrmSafetyLevel;
import com.civfanatics.civ3.xplatformeditor.tabs.biqc.checkBoxSettings;
import com.civfanatics.civ3.xplatformeditor.tabs.map.CustomMapSetupForm;
import com.civfanatics.civ3.biqFile.IO;
import com.civfanatics.civ3.biqFile.LEAD;
import com.civfanatics.civ3.biqFile.OperatingSystem;
import com.civfanatics.civ3.biqFile.PRTO;
import com.civfanatics.civ3.biqFile.SLOC;
import com.civfanatics.civ3.biqFile.TILE;
import com.civfanatics.civ3.biqFile.UNIT;
import com.civfanatics.civ3.biqFile.civ3Version;
import com.civfanatics.civ3.biqFile.util.DefaultRulesLoader;
import com.civfanatics.civ3.pediaIcons.PediaIconsFile;
import com.civfanatics.civ3.savFile.SAV;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.InterruptThread;
import com.civfanatics.civ3.xplatformeditor.imageSupport.Civ3PCXFilter;
import com.civfanatics.civ3.xplatformeditor.imageSupport.PCXFilter;
import com.civfanatics.civ3.xplatformeditor.specialty.CSVImportPanel;
import static com.civfanatics.civ3.xplatformeditor.specialty.JavaVersion.incompatibleJavaVersion;
import com.civfanatics.civ3.xplatformeditor.specialty.StartupWidgets;
import com.civfanatics.civ3.xplatformeditor.specialty.VersionChecker;
import com.civfanatics.civ3.xplatformeditor.tabs.map.BMPSizeCalculator;
import com.civfanatics.civ3.xplatformeditor.tabs.map.MapFromBMPForm;
import com.civfanatics.civ3.xplatformeditor.tabs.map.PolarBearForm;
import com.civfanatics.civ3.xplatformeditor.undoRedo.UndoStack;
import com.civfanatics.civ3.xplatformeditor.districts.DistrictScenarioSerializer;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.apache.log4j.Appender;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.simplericity.macify.eawt.Application;
import org.simplericity.macify.eawt.ApplicationEvent;
import org.simplericity.macify.eawt.ApplicationListener;
import org.simplericity.macify.eawt.DefaultApplication;
import scriptFile.ScriptFile;
import webbrowserfx.WebBrowserFX;
import webpageviewer.BrowserWindow;
/**
 * The meat and potatoes of the program.  Only high-level stuff should get a
 * place here.  Requires periodic refactoring to keep to a reasonable length.
 * At one point this class had over 10,000 lines.
 * @author Andrew
 */
public class Main extends javax.swing.JFrame implements ApplicationListener{
        
    //Constants
    static final boolean defaultFile = false;
    public static final String newline = System.getProperty("line.separator");
    public static String fileSlash = System.getProperty("file.separator");
    public static final String VERSION = "1.6";
    static final String TITLE = "Conquests Editor " + VERSION + " (Districts version)";
    public static boolean GRAPHICS_ENABLED = true;
    String titleRemainder = "";
    static String fontChoice = "Tahoma";  //also LucidaSansRegular, Tahoma, Trebuchet MS
    String errorWhileSaving = "Error while saving file";
    String[] acceptedExtensions = {"biq", "bic"};
    public static OperatingSystem os = new OperatingSystem();
    static int numProcs = -1;
    public static Settings settings = new Settings();
    private JPanel pnlTemporary;
    boolean canPressF1 = true;
    BrowserWindow helpWindow;
    WebBrowserFX helpFX;
    Stage helpStage = null;
    static String imagePath = "./imgs/";  //intentionally package-level
    
    private File savedFile;
    private static UndoStack currentUndoStack;
    private UndoStack undoStack;
    
    Action allowF1 = new AbstractAction(){
        public void actionPerformed(ActionEvent e)
        {
            canPressF1 = true;
        }
    };
    
    public static ResourceBundle i18n = null;
    
    public String help_ColorPalette = "help/civColors.html";
    String help_Home = "help/civ3editor.html";
    
    public String currentHelp = help_Home;
    
    javax.swing.Timer helpTimer = new javax.swing.Timer(1000, allowF1);

    static Logger logger = Logger.getLogger("Main");

    //Safety level - how many safeguards there are against you making an invalid file
    //the higher the value, the more safe
    //To be used as in, "if safety > SAFETY_STANDARD, check this"
    //Some elements may be not be quantified at release - e.g. the improvements
    //allowable for only Wonders may or may not be quantified at release.
    //The actual variables are now stored in the Map of safety levels in the
    //frmSafetyLevels class

    //input variables
    public static List<IO> biqFile = new ArrayList<>();
    public List<checkBoxSettings>checkBoxSettings;        //parallel to biqFile
    public static int biqIndex = -1;
    int biq2Index = -2;
    int maxBiqIndex = -1;
    static boolean fileOpen = false;

    int numSelected;

    int ruleIndex;

    String separator;

    javax.swing.Timer lblTimer; //need to specify class as java.util.Timer also exists

    Color[] colors;
    BufferedImage[][]unitIcons;
    BufferedImage units32;
    BufferedImage[]resourceIcons;
    
    public static Image icon;

    Timer autoSaveTimer;

    private Application application;

    FrmSafetyLevel safetyLevelWindow;
    SettingsPanel settingsPanel;
    AboutPanel aboutPanel;
    public static Map<String, SafetyLevel> safetyLevels;
    
    GridBagLayout mainLayout = new GridBagLayout();
    GridBagConstraints gc = new GridBagConstraints();
    
    static boolean mapIsLoaded = false;
    
    public static Main mainMain;
    
    LoadAccelerator loadAccelerator = null;
    VersionChecker versionChecker = null;
    
    final static String[]pcxNames = {"ntp00", "ntp01", "ntp02", "ntp03", "ntp04", "ntp05", "ntp06", "ntp07", "ntp08", "ntp09", "ntp10", "ntp11", "ntp12", "ntp13", "ntp14", "ntp15", "ntp16", "ntp17", "ntp18", "ntp19", "ntp20", "ntp21", "ntp22", "ntp23", "ntp24", "ntp25", "ntp26", "ntp27", "ntp28", "ntp29", "ntp30", "ntp31"};    
    
    //Must be one of "JFileChooser", "FileDialog".  May add a JavaFX option at
    //some point, too.  Also must be declared prior to the static initializer,
    //which may change its value.
    public static String fileChooserMode = "FileDialog";
    
    public static JFileChooser jfcBMPChooser = new JFileChooser();
    public static JFileChooser jfcCSVChooser = new JFileChooser();
    
    
    //Also declare file filters here.
    //This way we can ensure we only add them once; otherwise we may wind up with JFileChoosers that have the same filter multiple times
    FileExtensionFilter savFilter = null;
    FileExtensionFilter[]saveFilters = null;
    FileExtensionFilter scenarioFileFilter = null;
    FileExtensionFilter bicFilter = null;
    FileExtensionFilter bixFilter = null;
    FileExtensionFilter biqFilter = null;
    
    MenuSystem menus = null;
    static boolean needToWaitTillArgsProcessed = true;
    static String editorDirectory = "";
    
    public static SAV currentSAV = null;
    
    static {
                
        //Set the menu bar name on OSX.  Doesn't work on recent OSX/Java combinations
        //Supposedly the following property would set the title on OSX, but it doesn't seem to actually do so.
        // This may be because our starting class is a Swing class itself
        //Instead, we can launch like: java -Xdock:name="Conquests Editor" -jar "Conquests Editor.jar"
        //Unfortunately, the -Xdock option isn't recognized on Windows, and causes it to fail to start.
        //What we probably need to do it make the launcher add that option if and only if the program is running
        //on OSX.
        //We might also want to have the launcher be smarter about RAM allocation, giving more on x64 and less on
        //RAM-limited systems (I suspect the launcher is failing on Windows 98 because the 128 MB initial heap size
        //is equal to all the RAM in the whole system)
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", TITLE);
        
        //Tell OS X to use the main menu bar in the system menu.  This *does* work on recent versions of OS X 
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        //We need the OS set up before we process the config file, as we
        //do stuff differently depending on the OS
        os.name = System.getProperty("os.name");
        os.version = System.getProperty("os.version");
        os.arch = System.getProperty("os.arch");
        
        //Also set whether we should use FileDialog or JFileChooser.
        //On Intel Macs running the Apple JDK, FileDialog is preferable because
        //there is a bug with disappearing dropdowns on OS X with JFileChooser,
        //and on all Intel Macs, double-clicking on folders doesn't work.
        //On Windows, JFileChooser is preferable because you cannot set filename
        //filters on FileDialogs on Windows (or Solaris).
        //So, we will set FileDialog as the default on Intel Macs, and
        //JFileChooser on all other systems (including PowerPC Macs, which don't
        //exhibit either bug that Intel Macs have).
        if (os.name.toLowerCase().contains("mac") && (os.arch.toLowerCase().contains("x86") || os.arch.toLowerCase().contains("x64"))) {
            fileChooserMode = "FileDialog";
        }
        else {
            fileChooserMode = "JavaFX";
        }
        
        //default safety levels
        safetyLevels = new HashMap<>();
        safetyLevels.put("BLDG", SafetyLevel.Safe);
        safetyLevels.put("CTZN", SafetyLevel.Safe);
        safetyLevels.put("CULT", SafetyLevel.Safe);
        safetyLevels.put("DIFF", SafetyLevel.Safe);
        safetyLevels.put("ERA", SafetyLevel.Safe);
        safetyLevels.put("ESPN", SafetyLevel.Safe);
        safetyLevels.put("EXPR", SafetyLevel.Safe);
        safetyLevels.put("FLAV", SafetyLevel.Safe);
        safetyLevels.put("GOOD", SafetyLevel.Safe);
        safetyLevels.put("GOVT", SafetyLevel.Safe);
        safetyLevels.put("PLYR", SafetyLevel.Safe);
        safetyLevels.put("RULE", SafetyLevel.Safe);
        safetyLevels.put("PRTO", SafetyLevel.Safe);
        safetyLevels.put("TECH", SafetyLevel.Safe);
        safetyLevels.put("TERR", SafetyLevel.Safe);
        safetyLevels.put("TRFM", SafetyLevel.Safe);
        safetyLevels.put("WSIZ", SafetyLevel.Safe);
        safetyLevels.put("RACE", SafetyLevel.Safe);
        safetyLevels.put("PROP", SafetyLevel.Safe);
        safetyLevels.put("Map", SafetyLevel.Safe);
    }
    
    static void setupLogging(String editorDirectory) {
        
        //First make sure the log isn't too big.  If it is, delete it.
        //Setting limit of 1MB log file size.
        String fileName = editorDirectory + "log.txt";
        File lgFile = new File(fileName);
        if (lgFile.length() > 1048576)
        {
            lgFile.delete();
        }
        lgFile = null;

        Logger.getRootLogger().setLevel(Level.INFO);
        BasicConfigurator.configure();
        //set up logging to a file
        Logger root = Logger.getRootLogger();
        Layout layout = new PatternLayout("%-4r [%t] %-5p %c %x - %m%n");
        Appender logFile = new FileAppender();
        try{
            logger.info("User dir = " + System.getProperty("user.dir"));
            if (editorDirectory != null && !editorDirectory.isEmpty()) {
                System.setProperty("user.dir", editorDirectory);
                logger.info("User dir set to = " + System.getProperty("user.dir"));
            }
            logFile = new FileAppender(layout, fileName);
        }
        catch(IOException e)
        {
            //TODO: On Windows 8.1, the dir defaults to something like C:\Windows\System32,
            //and it throws an exception and winds up here.
            //However, on Windows XP, the dir defaults to args[1], which is the scenario folder,
            //so the log gets written without an error to that directory (although that is not
            //the <i>correct</i> directory.
            //Need to evaluate this more when better rested, and also will need to verify that
            //it works as expected on 8.1 and 10 after getting it working as required on XP.
            //I am rather wondering, since I've already updated it so that it works with later initialization
            //on 8.1, should it initialize things later in general?
            needToWaitTillArgsProcessed = true;
            logger.error("Error while setting up the log output to file: ", e);
        }
        root.addAppender(logFile);
        logger.info("Starting program - version " + VERSION);
    }
    
    static void setupLocale() {
        
        //Set the locale.  This may vary by config file.
        Locale locale = new Locale("en", "US");
        if (settings.editorLanguage.equals("Russian")) {
            locale = new Locale("ru", "RU");
        }
        else if (settings.editorLanguage.equals("French")) {
            locale = new Locale("fr", "FR");
        }
        else if (settings.editorLanguage.equals("Esperanto")) {
            locale = new Locale("eo", "US");
        }
        else if (settings.editorLanguage.equals("zzTest")) {
            locale = new Locale("zz", "US");
        }

        try {
            File f = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            logger.info("Locale path: " + Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            f = new File(f.getParent() + "/langs");
            URL[]urls = new URL[1];
            try {
                urls[0] = f.toURL();
            }
            catch(MalformedURLException ex) {

            }
            ClassLoader cl = new URLClassLoader(urls);
            try {
                logger.info("i18n URL: " + f.getCanonicalPath());
            }
            catch(IOException ex) {
                logger.error("IOException", ex);
            }
            i18n = ResourceBundle.getBundle("EditorStrings", locale, cl);
        }
        catch(URISyntaxException ex) {
            logger.error("URISyntaxException", ex);
        }

        //Set Java components
        JComponent.setDefaultLocale(locale);
    }

    public Main(String fileToOpen) {
        
        if (logger.isDebugEnabled()) {
            logger.info("In constructor");
        }

        mainMain = this;
        application = new DefaultApplication();
        application.addApplicationListener(this);
        
        icon = Toolkit.getDefaultToolkit().getImage(Main.imagePath + "icon.PNG");
        if (os.name.toLowerCase().contains("mac")) {
            integrateWithOSX();
        }

        setIconImage(icon);

        if (logger.isInfoEnabled())
            logger.info("Running " + os.name + " " + os.version + " on " + os.arch);
        if (logger.isInfoEnabled())
            logger.info("Java VM vendor: " + System.getProperty("java.vm.vendor"));
        if (logger.isInfoEnabled())
            logger.info("Java spec vendor: " + System.getProperty("java.specification.vendor"));
        if (logger.isInfoEnabled())
            logger.info("Java runtime version: " + System.getProperty("java.version"));
        if (logger.isInfoEnabled())
            logger.info("Java runtime name: " + System.getProperty("java.runtime.name"));
        
        //java.version : 1.8.0_45
        String javaVersion = System.getProperty("java.version");
        if (incompatibleJavaVersion(javaVersion)) {
            alertAboutJava11(System.getProperty("java.vm.vendor"), System.getProperty("java.version"));
        }
        if (javaVersion.startsWith("1.8.0")) {
            String update = javaVersion.substring(javaVersion.indexOf("_") + 1);
            Integer minorVersion = Integer.valueOf(update);
            if (minorVersion < 60) {
                settings.useJavaFX = false;
                settings.forceSwing = true;
                JOptionPane.showMessageDialog(null, "<html>You are currently running Java 8 update " + minorVersion + ".<br />"
                        + "Version 1.10 of the editor introduces some features that require update 60 or later.<br />"
                        + "Please download and install the current update of Java from http://www.oracle.com/technetwork/java/javase/downloads/index.html.<br /><br />"
                        + "Alternately, old versions of the editor or a version that bundles Java can be downloaded from the \"Old Versions\" section of the editor thread's first post,<br />"
                        + "at https://forums.civfanatics.com/threads/cross-platform-editor-for-conquests-now-available.377188/.", 
                        "Java Update Required", JOptionPane.ERROR_MESSAGE);
                System.exit(60);
            }
            else if (minorVersion >= 151 && os.name.equals("Windows XP")) {
                //There is a bug in Java update 151 on XP that causes almost any JavaFX action to crash
                //I suspect a Windows method that was introduced in Vista is being called
                settings.useJavaFX = false;
                settings.forceSwing = true;
                settings.forceSwingDueToJavaBug = true;
                logger.warn("Enforcing use of old lists to avoid Java bug on update 151+ on Windows XP");
                //In the future should check whether this affects updates beyond 152,
                //and also if it affects XP x64 (which includes some newer APIs than x86).
            }
        }

        checkBoxSettings = new ArrayList<checkBoxSettings>();

        initComponents();
        
        logger.info("Is hardware enabled? " + System.getProperty("sun.java2d.opengl", "true"));
        
        //now change the title
        super.setTitle(TITLE);
        loadAccelerator = new LoadAccelerator(this);
        loadAccelerator.start();
        
        if (settings.checkForUpdates) {
            versionChecker = new VersionChecker(this, VERSION);
            versionChecker.start();
        }
        
        if (!fileToOpen.isEmpty()) {
            openFile(fileToOpen);
        }
    }
     
    void alertAboutJava11(String vendor, String version) {
        JOptionPane.showMessageDialog(null, 
                "<html>You have a version of Java (" + vendor + " " + version + ") that doesn't include components the editor depends on.<br/>"
                        + "It is recommended to switch to the Liberica version (choose Full JRE as the Package):<br/>"
                        + "https://bell-sw.com/pages/downloads/#/java-8-lts"
                        + "Alternately, there is a Windows-specific build that bundles Java in post #1033 of the thread.<br/>",
                "Java 11+ Compatibility", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Sets the title to the base title (e.g. Conquests Editor 0.79), plus the
     * given string.
     * @param string The remainder of the title.
     */
    @Override
    public void setTitle(String string) {
        super.setTitle(TITLE + " - " + string);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {
        if (logger.isDebugEnabled()) {
            logger.info("Starting initialize method now");
        }
        
        menus = new MenuSystem(this);
        
        //A couple are set up here to work around compilation issues.
        menus.getAbout().addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               handleAbout(null);
           } 
        });

        pnlTabs = new EditorTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Conquests Editor Version Here");
        
        //Do not set maximized bounds.  That makes maximization not work well.
        //Remember the screen size, unless it was larger than the screen resolution currently is,
        //in which case fall back to about 1024x768
        GraphicsDevice screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int screenWidth = screen.getDisplayMode().getWidth();
        int screenHeight = screen.getDisplayMode().getHeight();
        Insets uiInsets = Toolkit.getDefaultToolkit().getScreenInsets(screen.getDefaultConfiguration());
        int widthInsets = uiInsets.left + uiInsets.right;
        int heightInsets = uiInsets.bottom + uiInsets.top;
        int maxWidth = screenWidth - widthInsets;
        int maxHeight = screenHeight - heightInsets;
        //Add a margin of error of 10 pixels in either direction.
        //On XP with an ATI card, maximizing gives bounds 10 pixels in excess of the screen in either direction.
        //Not sure why, could be to hide window borders and so forth; at any rate should account for it
        maxWidth+=10;
        maxHeight+=10;
        if (settings.width <= maxWidth && settings.height <= maxHeight) {
            setSize(new java.awt.Dimension(settings.width, settings.height));
        }
        else {
            logger.info("Screen dimensions of " + settings.width + " x " + settings.height + " exceed maximum of " + maxWidth + " x " + maxHeight);
            setSize(new java.awt.Dimension(1030, 768));
        }
        setMinimumSize(new java.awt.Dimension(800, 600));
        setName("frmMain"); // NOI18N
        setResizable(true);
        this.addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent e)
            {
                pnlTabs.setSize(getWidth(), getHeight()-50);
                if (pnlTabs.mapTab != null && pnlTabs.mapTab.map != null)  //since the mapTab is loaded in a different thread, check 1st
                {
                    pnlTabs.mapTab.map.updateBufferSize(getWidth());
                    pnlTabs.mapTab.updateWidth(getWidth());
                }
                settings.width = getWidth();
                settings.height = getHeight();
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowDeactivated(java.awt.event.WindowEvent evt) {
                formWindowDeactivated(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                formComponentHidden(evt);
            }
        });
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        getContentPane().setLayout(mainLayout);
        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int button = e.getButton();
                System.out.println("Clicked mouse button " + button);
            }
        });
        
        long eventMask = AWTEvent.MOUSE_EVENT_MASK;
        Toolkit.getDefaultToolkit().addAWTEventListener( new AWTEventListener()
        {
            public void eventDispatched(AWTEvent e)
            {
                e.toString();
                if (e instanceof MouseEvent) {
                    MouseEvent m = (MouseEvent)e;
                    if (m.getID() == MouseEvent.MOUSE_CLICKED) {
                        if (m.getButton() == 4) {
                            System.out.println("Button 4 clicked");
                        }
                        else if (m.getButton() == 5) {
                            System.out.println("Button 5 clicked");
                        }
                    }
                }
            }
        }, eventMask);

        KeyEventDispatcher ked = new KeyEventDispatcher() {
            public boolean dispatchKeyEvent(KeyEvent ke) {
                if (canPressF1 && ke.getKeyCode() == KeyEvent.VK_F1) {
                    displayHelp();
                    return true;
                }
                else {
                    return false;   //don't swallow anything else
                }
            }
        };
        
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(ked);

        
        AbstractButton helpComponent;
        helpComponent = menus.getHelp();
        helpComponent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                displayHelp();
            }
        });
        
        //Can't pass static methods by reference; get "unexpected static method found in bound lookup"
        //error.  So, made it public static instead.
        pnlTemporary = StartupWidgets.setupStartWidgets(this.getContentPane(), gc, pnlTemporary, this::openFile);
        
        //JFileChooser setup
        String[] csv = {"csv"};
        FileExtensionFilter csvFilter = new FileExtensionFilter(csv, "CSV Files");
        jfcCSVChooser.setFileFilter(csvFilter);

    }

    // </editor-fold>
    
    // Witnessed on OS X: The JavaFX browser failing to initialize
    // due to an ArrayIndexOutOfBounds in sun.nio.fs.UnixFileSystem
    // Also have seen NoClassDefFound.  If that happens, set this to true
    // and use Offline Help.
    boolean forceOfflineDueToError = false;
    
    private void displayHelp() {
        
        canPressF1 = false;  //must be first thing

        //Determine if we should use offline help
        boolean offlineHelp = false;
        
        //Windows XP check - if JDK is Java 8 Update 112 or higher, must use offline
        //because the WebKit version is too new for the OS to make use of
        String javaVersion = System.getProperty("java.version");
        if (javaVersion.startsWith("1.8.0")) {
            String update = javaVersion.substring(javaVersion.indexOf("_") + 1);
            Integer minorVersion = Integer.valueOf(update);
            if (minorVersion >= 112 && os.name.equals("Windows XP")) {
                logger.info("Using offline help due to WebKit in Java 8 update " + minorVersion + " being too new for Windows XP");
                offlineHelp = true;
            }
        }
        
        if (!offlineHelp) {
            InterruptThread it = new InterruptThread();
            it.start();
            try {
                synchronized(it) {
                    it.wait();
                }
            }
            catch(InterruptedException ex) {

            }
            offlineHelp = it.getOfflineStatus();
        }
        
        if (forceOfflineDueToError) {
            offlineHelp = true;
        }

        if (offlineHelp) {
            if (logger.isDebugEnabled())
                logger.debug("Displaying offline help");
            if (helpWindow == null) {
                helpWindow = new BrowserWindow("Editor Help", false, false);
                helpWindow.setLocationRelativeTo(Main.mainMain);
            }
            helpWindow.goToUrl(currentHelp);
            helpWindow.setVisible(true);
        }
        else {
            Platform.runLater(new Runnable() {
                public void run() {
                    if (logger.isDebugEnabled())
                        logger.debug("Displaying online help");
                    if (helpFX == null) {
                        helpStage = new Stage();
                        helpFX = new WebBrowserFX();
                        //Boolean to allow me to test updates to online help in-editor before releasing them
                        boolean testingUpdates = false;
                        if (testingUpdates) {
                            helpFX.setHomepage("file:////" + System.getProperty("user.dir") + "/" + "onlineHelp/civ3editor.html");
                        }
                        else {
                            helpFX.setHomepage("http://quintillus.warpmail.net/civ3editor/onlineHelp/civ3editor.html");
                        }
                    }
                    try {
                        helpFX.start(helpStage);
                    }
                    catch(ExceptionInInitializerError | NoClassDefFoundError er) {
                        //TODO: Additional verificaton needed on Help status on OS X
                        //JavaFX WebView is blowing up... but it might work on Oracle's JDK
                        //Offline Help is blowing up with a zillion forward slashes... the user.dir thing might be wrong
                        //I am not 100% sure that it ever worked on OS X, and far less sure yet that it did on OS X plus
                        //Java 8.  So before implementing the fallback, need more testing.  I'll leave the catch here as
                        //a reminder.
                        
                        logger.error("Could not initialize help system", er);
                        
//                        forceOfflineDueToError = true;
//                        SwingUtilities.invokeLater(new Runnable() {
//                            @Override
//                            public void run() {
//                                System.out.println("Forcing offline help...");
//                                displayHelp();
//                            }
//                            
//                        });
                    }
                    helpStage.getIcons().add(javaFXImage);
                    helpStage.show();
                }
            });
        }
        helpTimer.start();
    }
    
    public void createNewBIQ() {
        if (biqFile.size() > 0) {
            int choice = JOptionPane.showConfirmDialog(null, "<html>The editor only supports opening one file at a time, other than for BIQ Compare mode.<br>You can try opening more than one in other modes, but it is recommended to open a second instance of the editor instead.<br>Continue opening additional file?</html>", "Open multiple files?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (choice == JOptionPane.NO_OPTION)
                return;
        }
        
        boolean successfulInput = newBIQ();
        if (successfulInput)//successfulInput)
        {
            IO currentBIQ = biqFile.get(biqIndex);
            performPostOpeningActions(currentBIQ, new File("Untitled"), false);
        }
    }
    
    
    private void performPostSAVOpenActions(File file) {
        titleRemainder = " - " + file.getName();
        Main.super.setTitle(TITLE + titleRemainder);
        menus.enableSavMenus();
    }
    
    /**
     * Actions to be performed after a BIQ is successfully opened
     */
    private void performPostOpeningActions(IO currentBIQ, File file, boolean customRules) {  
        //add a minimal superstructure if there aren't custom rules
        if (!currentBIQ.hasCustomRules()) {
            setUpRuleInfrastructure();
        }
        if (!currentBIQ.hasCustomPlayerData()) {
            setUpPlayerDataInfrastructure();
        }
        
        //make sure our UI is ready before we start sending it stuff
        if (!loadAcceleratorDone)
        {
            try{
                loadAccelerator.join();
            }
            catch(InterruptedException e)
            {
                logger.error("Interrupted", e);
            }
        }
        //handle updates that are local to the BIQC tab
        pnlTabs.alertBIQCTab(file);
        pnlTabs.setEnabled(true);
        
        titleRemainder = " - " + file.getName();
        Main.super.setTitle(TITLE + titleRemainder);
        
        currentBIQ.fileName = file.getName();
        currentBIQ.trim();
        loadInterfaceElements();
        menus.enablePostOpenMenus(currentBIQ);

        pnlTabs.initialTabUpdate();
        //If the unit tab is active, update its icon right now

        setAutoSaveTimer();
        pnlTabs.setVisible(true);
        pnlTemporary.setVisible(false);

        //Put it in our recent files queue
        String newBIQName = "";
        try {
            newBIQName = file.getCanonicalPath();
        }
        catch(IOException ex) {
            newBIQName = file.getAbsolutePath();
        }
        updateRecentFilesList(newBIQName);
        
        //Add it to the Recent Documents list
        utils.addFileToRecentFiles(editorDirectory, newBIQName);

        if (!currentBIQ.hasCustomMap()) {
            menus.disableMapEditingOptions();
        }
        
        undoStack = new UndoStack(currentBIQ, menus.editMenu);
        currentUndoStack = undoStack;
    }

    void open(java.awt.event.ActionEvent evt) {

        //fileName = "D:/Firaxis/Civilization III/Conquests/Scenarios/Wrath of the AI.biq";
        openFile(null);
        //END OF THREAD
    }

    /**
     * Handles the opening of files.  This is includes processing the config file
     * the first time a file is opened, checking for the default file,
     * calling the actual inputFile method in the IO class, and alerting the GUI
     * to the newly-opened file.
     */
    public void openFile(String fileName)
    {
        long maxAllocatableMB = Runtime.getRuntime().maxMemory() / 1000 / 1000;
        if (maxAllocatableMB < 384 && Main.GRAPHICS_ENABLED) {
            int choice = JOptionPane.showConfirmDialog(null, "<html>The editor is limited to " + maxAllocatableMB + " MB of memory; it recommends 384 MB or greater.<br/><br/>"
                    + "Would you like to attempt to open the file anyway?  The editor may run out of memory and become unresponsive, or perform with errors.<br/><br/>"
                    + "Success is possible but not guaranteed with at least 200 MB of memory, depending on the map size.<br/><br/>"
                    + "To fix this, relaunch the editor via launcher.jar.</html>", "Continue despite low memory?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (choice == JOptionPane.NO_OPTION)
                return;
        }
        
        if (biqFile.size() > 0) {
            int choice = JOptionPane.showConfirmDialog(null, "<html>The editor only supports opening one file at a time, other than for BIQ Compare mode.<br>You can try opening more than one in other modes, but it is recommended to open a second instance of the editor instead.<br>Continue opening additional file?</html>", "Open multiple files?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (choice == JOptionPane.NO_OPTION)
                return;
        }
        File file = null;
        if (fileName == null) {
            file = getInputFile();
            if (file == null)
                return;
        }
        else {
            file = new File(fileName);
        }
        boolean successfulInput = false;
        try {
            successfulInput = openBIQ(file);
        }
        catch(FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "The specified file (" + file.getPath() +  ") could not be found. \nCheck if it has been moved or deleted.", "File Not Found", JOptionPane.ERROR_MESSAGE);
            biqFile.remove(biqFile.size() - 1);
            biqIndex--;
            maxBiqIndex--;
            return;
        }
        if (successfulInput)//successfulInput)
        {
            IO currentBIQ = biqFile.get(biqIndex);
            performPostOpeningActions(currentBIQ, file, currentBIQ.hasCustomRules());
        }
        else
        {
            //See if the error was likely due to an unsupported version.
            if (biqFile.get(biqIndex).majorVersionNumber == 0 && biqFile.get(biqIndex).minorVersionNumber == 0)
            {  //This also happens if UAC interferes with the opening process, so include that snippet, too.
                JOptionPane.showMessageDialog(null, "The specified file (" + file.getPath() +  ") could not be opened. \nIt is an unsupported version " + biqFile.get(biqIndex).majorVersionNumber + "." + biqFile.get(biqIndex).minorVersionNumber + " BIQ file, which likely resulted in the error.\n\nIt is also possible that this is due to running Windows Vista or later with UAC enabled and Civ3 installed in a protected location (such as C:\\Program Files),\nresulting in the editor being denied permission to open the file.  Turning off UAC or reinstalling Civ3 to somewhere such as C:\\Civilization III should solve this problem.\n\nPlease make a forum post at http://forums.civfanatics.com/showthread.php?t=377188 with this BIQ attached if you would like to see support added.");
            }
            else if (!(biqFile.get(biqIndex).majorVersionNumber == 12 && (biqFile.get(biqIndex).minorVersionNumber == 8 || biqFile.get(biqIndex).minorVersionNumber == 6)))
            {   //This also happens if UAC interferes with the opening process, so include that snippet, too.
                JOptionPane.showMessageDialog(null, "The specified file (" + file.getPath() +  ") could not be opened. \nIt is an unsupported version " + biqFile.get(biqIndex).majorVersionNumber + "." + biqFile.get(biqIndex).minorVersionNumber + " BIQ file, which likely resulted in the error.\n\nPlease make a forum post at http://forums.civfanatics.com/showthread.php?t=377188 with this BIQ attached if you would like to see support added.");
            }
            else
            {
                JOptionPane.showMessageDialog(null, "The specified file (" + file.getPath() +  ") could not be opened. \nCheck that it is correctly spelled, and that it is an uncompressed BIQ 12.08 that no other programs are using.\n\nThe log file will contain more information about where the error occured.");
            }
        }
    }
    
    private void updateRecentFilesList(String newBIQName) {
        if (newBIQName.endsWith("Untitled")) {
            return; //don't add a new unsaved file to the list
        }
        int listPosition = -1;
        for (int i = 0; i < 5; i++) {
            if (Settings.recentFiles[i] == null) {
                break;
            }
            if (Settings.recentFiles[i].equals(newBIQName)) {
                listPosition = i;
            }
        }
        int max = listPosition == -1 ? 5 : (listPosition + 1);
        for (int i = max - 1; i > 0; i--) {
            Settings.recentFiles[i] = Settings.recentFiles[i - 1];
        }
        Settings.recentFiles[0] = newBIQName;

        //Update the live menus
        menus.updateRecentFiles(newBIQName, max);
    }

    /**
     * Sets or resets the auto save timer.  The existence of this method makes
     * it easy to change the interval of the timer.
     */
    public void setAutoSaveTimer()
    {
        if (!fileOpen)
            return;
        if (autoSaveTimer != null)
            autoSaveTimer.cancel();
        autoSaveTimer = new Timer("Auto-Save Timer");
        autoSaveTimer.scheduleAtFixedRate(new TimerTask(){
            public void run(){
                autoSave();
            }
        }, 1000*settings.autoSaveInterval, 1000*settings.autoSaveInterval);
    }

    private void autoSave()
    {
        //Save any outstanding changes
        pnlTabs.updateAllTabs();
        
        String fileName = "autoSave" + String.valueOf(settings.nextAutosave) + ".biq";
        File file = new File(fileName);
        saveFile(file);
        settings.nextAutosave++;
        if (settings.nextAutosave >= settings.maxAutosaves)
        {
            settings.nextAutosave = 00;
        }
    }

    /**
     * Gets an input file from the jFileChooser.
     * @return
     */
    public File getInputFile()
    {
        File file;
        if (defaultFile)
        {
            file = new File("C:/Documents and Settings/Andrew/My Documents/WWII Uncompressed.biq");
        }
        else
        {
            if (bicFilter == null) {
                String[] bic = {"bic"};
                String[] bix = {"bix"};
                String[] biq = {"biq"};
                bicFilter = new FileExtensionFilter(bic, Main.i18n("save.bicFiles"));
                bixFilter = new FileExtensionFilter(bix, "BIX files");
                biqFilter = new FileExtensionFilter(biq, Main.i18n("save.biqFiles"));
                scenarioFileFilter = new FileExtensionFilter(acceptedExtensions, Main.i18n("save.allScenarioFiles"));
            }
            FileExtensionFilter[]filters = new FileExtensionFilter[4];
            filters[0] = bicFilter;
            filters[1] = bixFilter;
            filters[2] = biqFilter;
            filters[3] = scenarioFileFilter;
            
            try {
                file = FileIO.getFile(FileDialog.LOAD, false, filters);
            }
            catch(IOException ex) {
                logger.error("IOException", ex);
                return null;
            }
            savedFile = file;
            return file;
        }
        return null;
    }

    private File getCivInstallDirFile() {
        try {
            if (settings != null && settings.civInstallDir != null && settings.civInstallDir.length() > 0)
                return new File(settings.civInstallDir);
        }
        catch (Exception ex) {
            logger.debug("Unable to resolve civ install dir", ex);
        }
        return null;
    }

    /**
     * Handles the low-level inputting of the BIQ file, and updating of indices.
     * Does not do anything with the GUI.
     * @param file
     * @return
     */
    public boolean openBIQ(File file) throws FileNotFoundException
    {
        if (logger.isInfoEnabled())
            logger.info("Input file: " + file.getAbsolutePath());

        biqFile.add(new IO());
        checkBoxSettings.add(new checkBoxSettings());
        maxBiqIndex++;
        if (!(biqIndex == -1))
        {
            //may require this for whether to save check boxes
        }
        biqIndex = maxBiqIndex;
        biqFile.get(biqIndex).fileName = file.getName();
        //set the language
        biqFile.get(biqIndex).setLanguage(settings.biqLanguage);
        long start = System.nanoTime();
        boolean successfulInput = biqFile.get(biqIndex).inputBIQ(file);
        long end = System.nanoTime();
        if (logger.isInfoEnabled())
            logger.info("Time to input file: " + (end - start)/1000000 + " milliseconds.");
        
        //Pedia Icons (and eventually other resources)
        //Note this may be moved into another thread for performance
        try {
            String pediaFile = utils.findFile("PediaIcons.txt", "Text/", biqFile.get(biqIndex));
            PediaIconsFile pediaIcons = new PediaIconsFile();
            pediaIcons.parseFile(pediaFile);
            biqFile.get(biqIndex).setPediaIcons(pediaIcons);
        }
        catch(FileNotFoundException ex) {
            logger.warn("Could not find PediaIcons.txt file", ex);
        }
        catch(Exception ex) {
            logger.error("Error reading pedia icons file", ex);
            JOptionPane.showMessageDialog(null, "<html>The pedia icons file could not be read.  While the editor will still work, the icons will not show up as intended.<br/><br/>It is recommended to upload your scenario (including the Text folder) to the CFC thread so the issue can be diagnosed.</html>",
                    "Error reading pedia icons", JOptionPane.WARNING_MESSAGE);
        }
        
        try {
            String scriptFile = utils.findFile("script.txt", "Text/", biqFile.get(biqIndex));
            ScriptFile script = new ScriptFile();
            script.parseFile(scriptFile);
            biqFile.get(biqIndex).setScriptFile(script);
        }
        catch(FileNotFoundException ex) {
            logger.warn("Could not find script.txt file", ex);
        }
        catch(Exception ex) {
            logger.error("Error reading script.txt file", ex);
            JOptionPane.showMessageDialog(null, "<html>The script file could not be read.  While the editor will still work, tech blurbs cannot be displayed.<br/><br/>It is recommended to upload your scenario (including the Text folder) to the CFC thread so the issue can be diagnosed.</html>",
                    "Error reading script file", JOptionPane.WARNING_MESSAGE);
        }

        if (successfulInput) {
            File civInstallDirFile = getCivInstallDirFile();
            DistrictScenarioSerializer.loadDistrictScenario(biqFile.get(biqIndex), file, civInstallDirFile);
        }

        return successfulInput;
    }
    
    private boolean newBIQ() {
        IO newBIQ = new IO();
        newBIQ.version = civ3Version.CONQUESTS;
        biqFile.add(newBIQ);
                
        checkBoxSettings.add(new checkBoxSettings());
        maxBiqIndex++;
        
        biqIndex = maxBiqIndex;        
        biqFile.get(biqIndex).fileName = "Untitled";
        //set the language
        biqFile.get(biqIndex).setLanguage(settings.biqLanguage);
        
        //Import from conquests.biq        
        String baseBIQ = utils.getConquestsFolder(settings.civInstallDir) + "conquests.biq";
        File file = new File(baseBIQ);
        IO baseRules = openSpecificFile(file);
        
        newBIQ.scenarioProperty = baseRules.scenarioProperty;
        
        return true;
    }
    
    private IO openSpecificFile(File file) {
        if (logger.isInfoEnabled())
            logger.info("Input file: " + file.getAbsolutePath());

        IO biqInput = new IO();
        checkBoxSettings.add(new checkBoxSettings());
        //maxBiqIndex++;
        if (!(biqIndex == -1))
        {
            //may require this for whether to save check boxes
        }
        //biqIndex = maxBiqIndex;
        biqInput.fileName = file.getName();
        //set the language
        biqInput.setLanguage(settings.biqLanguage);
        long start = System.nanoTime();
        boolean successfulInput = false;
        try {
            successfulInput = biqInput.inputBIQ(file);
        }
        catch(FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "The specified file (" + file.getPath() +  ") could not be found. \nCheck if it has been moved or deleted.");
        }
        long end = System.nanoTime();
        if (logger.isInfoEnabled())
            logger.info("Time to input file: " + (end - start)/1000000 + " milliseconds.");
        return biqInput;
    }

    public static void processConfigFile(String directory)
    {
        logger.info("Looking for config file at " + (directory + "civ3editor.ini"));
        File config = new File(directory + "civ3editor.ini");
        if (config.exists())
        {
            settings.importConfigFile(config);
            numProcs = Integer.parseInt(settings.numProcs);
            if (!(settings.firstRun.equals("false")))
            {
                firstRunStuff();
            }
        }
        else
        {
            logger.warn("Couldn't find config file");
            firstRunStuff();
        }
        //Stuff we do whether the config file was found or not
        //if the number of processors wasn't specified, or was specified with an invalid value,
        //go with as many cores as the user has
        if (numProcs < 1)
        {
            numProcs = Runtime.getRuntime().availableProcessors();
        }
        //For some reason this one line seems to take almost 50 ms.
        IO.setNumProcs(numProcs);
        
        DefaultRulesLoader.defaultRulesPath = utils.getConquestsFolder(settings.civInstallDir) + "conquests.biq";
        DefaultRulesLoader.defaultRulesLanguage = settings.biqLanguage;
    }

    private static void firstRunStuff()
    {
        settings.noConfigFileAtStart = true;
        //The FileDialog will always have been initialized
        boolean autoFound = false;
        String installPath = null;
        if (os.name.toLowerCase().contains("mac") || os.name.toLowerCase().contains("os x"))
        {
            File defLoc = new File("/Applications/");
            //probably /Applications/Civ III Complete/
            String likelyInstall = "/Applications/Civ III Complete";
            if (utils.verifyGoodInstall(likelyInstall))
            {
                autoFound = true;
                installPath = likelyInstall;
            }
             else{
                if (defLoc.exists()) {
                    FileIO.setCurrentDirectory(defLoc);
                }
                JOptionPane.showMessageDialog(null, "Welcome!  As part of the first-time setup, please choose where you have Civilization III Complete\ninstalled (this is the folder above the \"Conquests Game Data\" folder).\n  This will be used to find PCX image files, as well as set up the default scenario location.", "OS X setup", JOptionPane.PLAIN_MESSAGE);
             }
        }
        else
        {
            if (os.name.toLowerCase().contains("windows"))
            {
                installPath = regKeyValue("\"HKEY_LOCAL_MACHINE\\SOFTWARE\\Infogrames Interactive\\Civilization III\"", "REG_SZ", "Install_Path");
                if (installPath.equals(""))
                {
                    installPath = regKeyValue("\"HKEY_LOCAL_MACHINE\\SOFTWARE\\Wow6432Node\\Infogrames Interactive\\Civilization III\"", "REG_SZ", "Install_Path");
                    if (installPath.equals("")) {
                        autoFound = false;
                        if (logger.isInfoEnabled())
                            logger.info("Could not find install path");
                    } else {
                        autoFound = true;
                    }
                }
                else {
                    autoFound = true;
                }
            }
            if (autoFound)
            {   //make sure it really was found; if not, ask for pointer
                if (!utils.verifyGoodInstall(installPath))
                {
                    autoFound = false;  //guess we didn't find it after all
                    File defLoc = null;
                    if (os.arch.toLowerCase().contains("x86"))
                        defLoc = new File("C:/Program Files/");
                    else    //must be Windows 64-bit, probably not Itanium
                        defLoc = new File("C:/Program Files (x86)/");
                    if (defLoc.exists()) {
                        FileIO.setCurrentDirectory(defLoc);
                    }
                    JOptionPane.showMessageDialog(null, "Welcome!  As part of the first-time setup, please choose where you have Civilization III (the base game, not the\n expansions) installed.  This will be used to find PCX image files, as well as set up the default scenario location.", "Windows/Linux Setup", JOptionPane.PLAIN_MESSAGE);
                }
            }
            else
            {   //ask the user where it is
                File defLoc = null;
                if (os.arch.toLowerCase().contains("x86"))
                    defLoc = new File("C:/Program Files/");
                else    //must be Windows 64-bit, probably not Itanium
                    defLoc = new File("C:/Program Files (x86)/");
                if (defLoc.exists()) {
                    FileIO.setCurrentDirectory(defLoc);
                }
                JOptionPane.showMessageDialog(null, "Welcome!  As part of the first-time setup, please choose where you have Civilization III (the base game, not the\n expansions) installed.  This will be used to find PCX image files, as well as set up the default scenario location.", "Windows/Linux setup", JOptionPane.PLAIN_MESSAGE);
            }
        }   //end Windows section (automation -> long time)
        if (!autoFound)
        {
            File file = null;
            try {
                file = FileIO.getFile(FileDialog.LOAD, true, null);
            }
            catch(IOException ex) {
                logger.error("IOException while getting file", ex);
            }
            if (file != null) {
                //TODO: Verify that this works correctly (expected path) on Mac OS X
                try{
                    settings.civInstallDir = file.getCanonicalPath();
                    if (os.name.toLowerCase().contains("mac"))
                        settings.openDir = settings.civInstallDir + fileSlash + "Conquests Game Data" + fileSlash + "Scenarios";
                    else
                        settings.openDir = settings.civInstallDir + fileSlash + "Conquests" + fileSlash + "Scenarios";

                }
                catch(java.io.IOException e){
                    logger.error("Exception while trying to get canonical path of civ install dir", e);
                }
                settings.firstRun = "false";
                try {
                    settings.exportConfigFile();
                }
                catch(ReadOnlyException ex) {
                    if (logger.isInfoEnabled()) {
                        logger.info("Cannot save settings configuration", ex);
                    }
                }
            }
        }
        else{
            //Windows should jump to here with no user intervention, assuming
            //reg query works properly.  Mac should, assuming default civ install
            //path.
            settings.civInstallDir = installPath;
            if (!os.name.toLowerCase().contains("mac"))
                settings.openDir = installPath + fileSlash + "Conquests" + fileSlash + "Scenarios";
            else
                settings.openDir = installPath + fileSlash + "Conquests Game Data" + fileSlash + "Scenarios";
            settings.firstRun = "false";
            try {
                settings.exportConfigFile();
            }
            catch(ReadOnlyException ex) {
                if (logger.isInfoEnabled()) {
                    logger.info("Cannot save settings configuration");
                }
            }
        }
        //July 2012 - add UAC check
        if (os.name.toLowerCase().contains("windows"))
        {
            int majorVersion = Integer.parseInt(os.version.substring(0, 1), 10);
            if (majorVersion >= 6)   //Vista or later, give UAC warning
            {
                if (settings.civInstallDir.contains("Program Files"))
                {
                    JOptionPane.showMessageDialog(null, "<html>It appears that you are using Windows Vista or later and have Civ3 installed to a Program Files folder.<br>  If you have UAC on, it's highly advisable to reinstall Civ3 to somewhere else (such as C:\\Civilization III) before modding.<br>After reinstalling Civ3, you'll need to go to Settings and re-specify where it's installed.</html>");
                }

                  //For some reason the UAC check call never returns.
                  //Not sure why, it works at the command line (tested w/standard user account)
                  //Didn't even work partially... even though Civ3 query works.
//                int uacEnable = regParseIntValue(regKeyValue("\"HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Policies\\System\"", "REG_DWORD", "EnableLUA"));
//                if (uacEnable > 0)
//                {
//                    JOptionPane.showMessageDialog(null, "UAC Enabled :(");
//                }
//                else
//                {
//                    JOptionPane.showMessageDialog(null, "UAC disabled");
//                }
            }
        }
    }

    void save(java.awt.event.ActionEvent evt) {
        //Note that this method intentionally hangs the editor while saving.
        //This prevents the user from exiting mid-save, and thus getting a
        //corrupted save file.
        
        //Save any outstanding changes on the tabs to memory first
        pnlTabs.updateAllTabs();
        
        if (!validateBIQFile()) {
            return;
        }

        //Check to see if boundary conditions are met
        if (!pnlTabs.checkBounds())
            return;

        try{
            String[] biqExtension = acceptedExtensions;
            //Set the filter - the old assumption that we must have done Open first is no
            //longer valid due to the recently-used files list.
            biqFilter = new FileExtensionFilter(biqExtension, Main.i18n("save.biqFiles"));

            FileExtensionFilter[] biqSaveFilter = new FileExtensionFilter[1];
            biqSaveFilter[0] = biqFilter;
            File file = FileIO.getFile(FileDialog.SAVE, false, biqSaveFilter);
            if (file == null) {
                return;
            }
            if (!file.getPath().endsWith(".biq")) {
                file = new File(file.getPath() + ".biq");
            }
            if (file.exists() && !fileChooserMode.equals("JavaFX"))
            {
                int response = JOptionPane.showOptionDialog(null, "File already exists.  Overwrite?", "Existing File", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (response != JOptionPane.YES_OPTION)
                    return;
            }

            //archive
            if (settings.autoArchive && savedFile != null)
            {
                try {
                    File sourceFile = savedFile;
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month  = calendar.get(Calendar.MONTH) + 1;
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);

                    DecimalFormat two = new DecimalFormat("00");

                    String sMonth = two.format(month);
                    String sDay = two.format(day);
                    String sHour = two.format(hour);
                    String sMinute = two.format(minute);

                    String absolutePath = sourceFile.getAbsolutePath();
                    String newName = absolutePath.substring(0, absolutePath.length() - 4) + "_Archive_" + year + "_" + sMonth + "_" + sDay + " " + sHour + "" + sMinute + ".biq";
                    File destFile = new File(newName);
                    if(!destFile.exists()) {
                        destFile.createNewFile();
                    }

                    FileChannel source = null;
                    FileChannel destination = null;

                    try {
                        source = new FileInputStream(sourceFile).getChannel();
                        destination = new FileOutputStream(destFile).getChannel();
                        boolean transferred = false;
                        int bytesMoved = 0;
                        while (!transferred)
                        {
                            bytesMoved += destination.transferFrom(source, bytesMoved, source.size() - bytesMoved);
                            if (bytesMoved == source.size())
                                transferred = true;
                        }
                    }
                    finally {
                        if(source != null) {
                            source.close();
                        }
                        if(destination != null) {
                            destination.close();
                        }
                    }
                }
                catch(NullPointerException ex) {
                    logger.error("Null pointer exception while saving archived version.  Is savedFile null? " + (savedFile == null), ex);
                }
            }

            boolean success = saveFile(file);
            if (success) {
                savedFile = file; //so if another save is made, the new archive will be with the new name
                titleRemainder = " - " + file.getName();
                updateRecentFilesList(file.getCanonicalPath());
            }
            restoreTitle();
            //yields question marks :(, 2^24 when I input the first integer
            
        }
        catch(ArrayIndexOutOfBoundsException e){
            logger.error(errorWhileSaving, e);
            String badNews = "Don't panic!  This error is likely due to setting a value that the editor can't handle.\nFortunately, the data in the editor won't be lost.  Look at the number that was\nout of bounds in this error message.  You likely entered that number somewhere it\ncan't be.  Setting it to  a more conventional value should allow you to save your file.\n     Error: ";
            JOptionPane.showMessageDialog(null, badNews + e, errorWhileSaving, JOptionPane.ERROR_MESSAGE);
        }
        catch(Exception e){
            //Leaving this ubergeneral catch here so that the user knows about any unforeseen exceptions that may occur.
            logger.error(errorWhileSaving, e);
            JOptionPane.showMessageDialog(null, e, errorWhileSaving, JOptionPane.ERROR_MESSAGE);
        }        // :
    }
    
    private boolean validateBIQFile() {
        if (settings.validateInfiniteUnitUpgrade) {
            for (PRTO prto : biqFile.get(biqIndex).unit) {
                if (prto.hasInfiniteUpgradePath()) {
                    String upgradePath = prto.getUpgradePath();
                    JOptionPane.showMessageDialog(null, "<html>There is a loop in a unit upgrade path:<br/><br/>" + upgradePath + "<br/><br/>This will cause infinite turn times in Civ; please change the upgrade path before saving.</html>", "Loop in Unit Upgrade Path", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }
        //Phantom Resource Bug
        if (settings.validatePhantomResourceBug) {
            boolean[]hasStrategicOrLuxResource = new boolean[32];
            int[]stratOrLuxResource = new int[32];   //so we can look up which ones trigger the bug
            List<GOOD>resources = biqFile.get(biqIndex).resource;
            for (int i = 0; i < resources.size(); i++) {
                GOOD resource = resources.get(i);
                int modulus = i % 32;
                if (resource.isStrategic() || resource.isLuxury()) {
                    if (hasStrategicOrLuxResource[modulus]) {
                        //Phantom Resource Bug
                        String firstName = resources.get(stratOrLuxResource[modulus]).getName();
                        String secondName = resources.get(i).getName();
                        
                        Object[] options = { "Cancel Save", "Save Anyway" };
                        int userChoice = JOptionPane.showOptionDialog(
                            null,
                            "<html>The resources " + firstName + " and " + secondName + " will trigger the Phantom Resource Bug.<br/>"
                            + "This occurs when you have strategic or luxury resources positioned 32, 64, etc. spaces away from each other.<br/>"
                            + "You can resolve this by reordering (dragging and dropping) resources on the GOOD tab, and can disable this check in the editor preferences.<br/>"
                            + "See the Help for the GOOD tab (and related CFC links) for more information</html>",
                            "Phantom Resource Bug", 
                            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, "Cancel Save");
                        if (userChoice == JOptionPane.YES_OPTION) {
                            return false;
                        }
                        else {
                            //don't spam alerts about all the phantom resources if the user overrides the warning
                            break;
                        }
                    }
                    hasStrategicOrLuxResource[modulus] = true;
                    stratOrLuxResource[modulus] = i;
                }
            }
        }
        return true;
    }

    /**
     * Saves the file.  This method does NOT include any verification that the file
     * is OK to write to, although it will throw an error if it fails.  It also does
     * not store tab data to memory prior to writing to file; this should be done
     * prior to calling this method (which allows verification of that data being
     * "good" to also occur prior to exporting to file).
     *
     * This method is intended to be used both by regular and automatic saves.
     * @param file
     */
    private boolean saveFile(File file)
    {
        //Update the size vars in the biq file
        biqFile.get(biqIndex).numBuildings = biqFile.get(biqIndex).buildings.size();
        biqFile.get(biqIndex).numCivilizations = biqFile.get(biqIndex).civilization.size();
        biqFile.get(biqIndex).numCitizens = biqFile.get(biqIndex).citizens.size();
        biqFile.get(biqIndex).numCulturalOpinions = biqFile.get(biqIndex).culture.size();
        biqFile.get(biqIndex).numDifficulties = biqFile.get(biqIndex).difficulties.size();
        biqFile.get(biqIndex).numEras = biqFile.get(biqIndex).eras.size();
        biqFile.get(biqIndex).numEspionage = biqFile.get(biqIndex).espionage.size();
        biqFile.get(biqIndex).numExprLevel = biqFile.get(biqIndex).experience.size();
        if (biqFile.get(biqIndex).version == civ3Version.CONQUESTS)
            biqFile.get(biqIndex).numFlavors = biqFile.get(biqIndex).flavor.size();
        else
            biqFile.get(biqIndex).numFlavors = 0;
        biqFile.get(biqIndex).numGoods = biqFile.get(biqIndex).resource.size();
        biqFile.get(biqIndex).numGovernments = biqFile.get(biqIndex).government.size();
        biqFile.get(biqIndex).numRules= biqFile.get(biqIndex).rule.size();
        biqFile.get(biqIndex).numScenarioProperties = biqFile.get(biqIndex).scenarioProperty.size();
        biqFile.get(biqIndex).numTechnologies = biqFile.get(biqIndex).technology.size();
        biqFile.get(biqIndex).numTerrains = biqFile.get(biqIndex).terrain.size();
        biqFile.get(biqIndex).numWorkerJobs = biqFile.get(biqIndex).workerJob.size();
        biqFile.get(biqIndex).numWorldSizes = biqFile.get(biqIndex).worldSize.size();

        //Tabs must already have been updated

        setTitle("Saving...");
        long start = System.nanoTime();
        boolean successfulExport = biqFile.get(biqIndex).outputBIQ(file);
        long end = System.nanoTime();
        if (logger.isInfoEnabled())
        {
            logger.info("Time to export file: " + (end - start)/1000000 + " milliseconds.");
        }
        if (!successfulExport)
        {
            logger.error("Failed to successfully output file");
            JOptionPane.showMessageDialog(null, "Failed to successfully output file");
        }
        else
        {
            File civInstallDirFile = getCivInstallDirFile();
            DistrictScenarioSerializer.saveDistrictScenario(biqFile.get(biqIndex), file, civInstallDirFile);
        }
        restoreTitle();
        return successfulExport;
    }
    
    /**
     * Sets the title back to the standard title after a temporary one (such as
     * to indicate that a file is being saved).
     */
    private void restoreTitle()
    {
        super.setTitle(TITLE + titleRemainder);
    }

    void inputBIQRulesFromSAV(java.awt.event.ActionEvent evt) {
        try{
            //JOptionPane.showMessageDialog(null, "Warning!  This feature sometimes fails.  If it takes more than a few seconds, it cannot open the file you requested.\nIf that occurs, you may need to restart the editor to open subsequent BIQs", "Warning!", JOptionPane.WARNING_MESSAGE);
            String[] biqExtension ={"sav", "SAV"};
            if (savFilter == null) {
                savFilter = new FileExtensionFilter(biqExtension, "SAV files");
                saveFilters = new FileExtensionFilter[1];
                saveFilters[0] = savFilter;
            }

            File file = FileIO.getFile(FileDialog.LOAD, false, saveFilters);
            if (file != null)
            {
                if (logger.isInfoEnabled())
                    logger.info("SAV file name: " + file.getCanonicalPath());

                biqFile.add(new IO());
                checkBoxSettings.add(new checkBoxSettings());
                maxBiqIndex++;
                biqIndex = maxBiqIndex;
                biqFile.get(biqIndex).fileName = file.getName();
                if (logger.isInfoEnabled())
                    logger.info("searching for biq...: ");
                boolean successfulInput = biqFile.get(biqIndex).inputBIQFromScenario(file);
                if (logger.isInfoEnabled())
                    logger.info("successful input: " + successfulInput);
                //yields question marks :(, 2^24 when I input the first integer

                if (successfulInput)//successfulInput)
                {
                    //handle updates local to biqc tab
                    pnlTabs.alertBIQCTab(file);
                    pnlTabs.setEnabled(true);
                    titleRemainder = " - " + file.getName();
                    restoreTitle();
                    biqFile.get(biqIndex).fileName = file.getName();
                    loadInterfaceElements();
                    pnlTabs.initialTabUpdate();
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "The specified file (" + file.getPath() +  ") could not be opened. \nCheck that it is correctly spelled, and that it is an uncompressed BIQ 12.08 that no other programs are using.");
                }
                pnlTabs.setVisible(true);
                pnlTemporary.setVisible(false);
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    
    void openSAVFile(java.awt.event.ActionEvent evt) {
        try{
            //JOptionPane.showMessageDialog(null, "Warning!  This feature sometimes fails.  If it takes more than a few seconds, it cannot open the file you requested.\nIf that occurs, you may need to restart the editor to open subsequent BIQs", "Warning!", JOptionPane.WARNING_MESSAGE);
            String[] biqExtension ={"sav", "SAV"};
            if (savFilter == null) {
                savFilter = new FileExtensionFilter(biqExtension, "SAV files");
                saveFilters = new FileExtensionFilter[1];
                saveFilters[0] = savFilter;
            }

            File savDir = new File(new File(settings.openDir).getParentFile().getCanonicalPath() + "/Saves");
            FileIO.setCurrentDirectory(savDir);
            
            File file = FileIO.getFile(FileDialog.LOAD, false, saveFilters);
            if (file != null)
            {
                if (logger.isInfoEnabled())
                    logger.info("SAV file name: " + file.getCanonicalPath());
                
                openSAV(file);
                
//                File[] saveFiles = file.listFiles();
//                
//                for (File f : saveFiles) {
//                    //if (f.getName().contains("Catherine of the Russians")) {
//                    try {
//                        openSAV(f);
//                    }
//                    catch(Exception e){
//                        logger.error("Unexpected error when inputting SAV", e);
//                        //JOptionPane.showMessageDialog(null, e);
//                    }
//                    //}
//                }
            }
            logger.info("Finish SAV opening");
        }
        catch(Exception e){
            logger.error("Unexpected error when inputting SAV", e);
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void openSAV(File file) throws IOException {
        
        SAV sav = new SAV();

        System.out.println("File name: " + file.getName());
        
        boolean successfulInput = sav.inputSAV(file);
        if (logger.isInfoEnabled())
            logger.info("successful input: " + successfulInput);
        //yields question marks :(, 2^24 when I input the first integer

        if (successfulInput)//successfulInput)
        {
            loadNTPColors(sav.getEmbeddedRules().getEmbeddedRules());
            logger.info("Successful SAV input");
            currentSAV = sav;
            
            performPostSAVOpenActions(file);
            
            JOptionPane.showMessageDialog(null, "This feature is still in development, and not ready yet.  However, no errors were encountered!");
        }
        else
        {
            logger.info("Unsuccessful SAV input");
            JOptionPane.showMessageDialog(null, "This feature is still in development, and not ready yet.  An error did occur while opening (" + file.getPath() +  "), and there may be more info in the log files.");
        }
    }

    private void formFocusGained(java.awt.event.FocusEvent evt) {
        if (logger.isDebugEnabled())
            logger.debug("Gained focus");
    }

    private void formComponentHidden(java.awt.event.ComponentEvent evt) {
        if (logger.isDebugEnabled())
            logger.debug("Component hidden");
    }

    private void formWindowDeactivated(java.awt.event.WindowEvent evt) {
        if (logger.isDebugEnabled())
            logger.debug("Window deactivated");
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        quitAction();
    }

    private void quitAction()
    {
        boolean quit = true;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (settings.confirmQuit)
        {
            int yesno = JOptionPane.showConfirmDialog(null, "Do you really want to exit?", "Exit?", JOptionPane.YES_NO_OPTION);
            if (yesno == JOptionPane.NO_OPTION)
            {   //disable quitting
                quit = false;
                this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            }
        }
        try {
            settings.exportConfigFile();    //so that we save the next auto save correctly
            logger.info("Successful exit");
        }
        catch(ReadOnlyException ex) {
            if (logger.isInfoEnabled()) {
                logger.warn("Cannot save settings configuration");
            }
        }
        if (os.name.toLowerCase().contains("mac") && quit)
            System.exit(0);
    }

    void cmdSafetyLevelActionPerformed(java.awt.event.ActionEvent evt)
    {
        safetyLevelWindow.setLocationRelativeTo(this);
        safetyLevelWindow.setVisible(true);
    }
    
    void importMapFromBMP() {
        MapFromBMPForm bmpForm = new MapFromBMPForm();
        bmpForm.pack();
        bmpForm.setLocationRelativeTo(this);
        bmpForm.setVisible(true);
        menus.enableMapEditingOptions();
    }
    
    void showBMPSizeCalculator() {
        BMPSizeCalculator bmpSize = new BMPSizeCalculator();
        bmpSize.setLocationRelativeTo(this);
        bmpSize.setVisible(true);
    }

    void cmdImportMapActionPerformed(java.awt.event.ActionEvent evt)
    {
        int curBIQIndex = biqIndex;
        //The below warning should no longer be necessary.  If no issues are found, will remove this code in a future version.
        //JOptionPane.showMessageDialog(null, "Please make sure the resources align between this BIQ and the map source BIQ.\nFailure to ensure this will result in the wrong resources appearing on the map and could result in an invalid BIQ.\nUnits, buildings, etc. should also align if they are present on the source map.", "Warning!", JOptionPane.WARNING_MESSAGE);
        File file = getInputFile();
        if (file == null)
            return;
        boolean successfulInput = false;
        try {
            successfulInput = openBIQ(file);
        }
        catch(FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "The specified file (" + file.getPath() +  ") could not be found. \nCheck if it has been moved or deleted.");
            return;
        }
        if (successfulInput)
        {
            if (biqFile.get(biqIndex).hasCustomRules()) {
                //It would be a good idea to make sure that resources and such aren't totally messed up.  Otherwise, things could be screwy.
                //Create a map of old resource to new resource by name.
                //So, let's say the map has Gems, and there are also Gems in our resources.  We want Gems to map to our ID, such as Gems, 5.
                //Although when we're processing the map, it's the ID we'll have, so it should be more like 7, 5, if Gems is 7 in the new and 5 in
                //the old.  If it doesn't exist, it should be mapped to -1.
                Map<Integer, Integer> resourceMapping = new HashMap<Integer, Integer>();
                for (int newResourceId = 0; newResourceId < biqFile.get(biqIndex).resource.size(); newResourceId++) {
                    String name = biqFile.get(biqIndex).resource.get(newResourceId).getName();
                    //Now find it in the old
                    boolean found = false;
                    for (int oldResourceId = 0; oldResourceId < biqFile.get(curBIQIndex).resource.size(); oldResourceId++) {
                        if (biqFile.get(curBIQIndex).resource.get(oldResourceId).getName().equals(name)) {
                            resourceMapping.put(newResourceId, oldResourceId);
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        resourceMapping.put(newResourceId, -1);
                    }
                }
                //Now go through and update the resources
                for (TILE tile : biqFile.get(biqIndex).tile) {
                    if (tile.getResource() != -1) {
                        int newResourceId = resourceMapping.get(tile.getResource());
                        tile.setResource(newResourceId);
                    }
                }
            }
            
            
            //fortuitously, as it turns out, we store references to goods and whatnot in tiles (the world map sections)
            //simply as integers (or shorts, accordingly), not as actual references to the good or whatever it
            //represents.  Thus, we shouldn't have to redirect any references
            biqFile.get(curBIQIndex).worldCharacteristic = biqFile.get(biqIndex).worldCharacteristic;
            biqFile.get(curBIQIndex).worldMap = biqFile.get(biqIndex).worldMap;
            biqFile.get(curBIQIndex).tile = biqFile.get(biqIndex).tile;
            biqFile.get(curBIQIndex).continent = biqFile.get(biqIndex).continent;
            biqFile.get(curBIQIndex).startingLocation = biqFile.get(biqIndex).startingLocation;
            biqFile.get(curBIQIndex).city = biqFile.get(biqIndex).city;
            biqFile.get(curBIQIndex).mapUnit = biqFile.get(biqIndex).mapUnit;
            biqFile.get(curBIQIndex).colony = biqFile.get(biqIndex).colony;
            biqFile.get(curBIQIndex).setCustomMap(true);
            biqIndex = curBIQIndex;
            //And the GUI should be left alone, completely unawares of the change
            //Not true; the Game Tab at least needs to know about it
            pnlTabs.gameTab.sendMapData(biqFile.get(curBIQIndex).worldCharacteristic);
            enableMapTab();
            JOptionPane.showMessageDialog(null, "Map successfully imported.", "Success!", JOptionPane.INFORMATION_MESSAGE);
            menus.enableMapEditingOptions();
        }
        else
            JOptionPane.showMessageDialog(null, "Error inputting the new map", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void formWindowActivated(java.awt.event.WindowEvent evt)
    {
        if (logger.isDebugEnabled())
            logger.debug("Window activated");
        //pnlTabs.checkForMapUpdates();
    }
    
    public static void triggerMapUpdates() {
        pnlTabs.triggerMapUpdates();
    }

    void cmdExportToCSVActionPerformed(java.awt.event.ActionEvent evt)
    {
        CSVPanel pnlCSV = new CSVPanel(biqFile.get(biqIndex));
        pnlCSV.setIconImage(icon);
        pnlCSV.setLocationRelativeTo(this);
        pnlCSV.setVisible(true);
    }
    

    void cmdImportFromCSVActionPerformed(java.awt.event.ActionEvent evt)
    {
        CSVImportPanel pnlCSV = new CSVImportPanel(biqFile.get(biqIndex));
        pnlCSV.setIconImage(icon);
        pnlCSV.setLocationRelativeTo(this);
        pnlCSV.setVisible(true);
    }

    void openSettingsWindow(java.awt.event.ActionEvent evt) {
        if (settingsPanel == null)
            settingsPanel = new SettingsPanel(settings, this);
        settingsPanel.setIconImage(icon);
        settingsPanel.setLocationRelativeTo(this);  //make visible
        settingsPanel.setVisible(true);
    }
    
    
    void enableCustomMap(java.awt.event.ActionEvent evt) {
        CustomMapSetupForm mapSetupForm = new CustomMapSetupForm();
        mapSetupForm.setLocationRelativeTo(this);
        mapSetupForm.setVisible(true);
        menus.enableMapEditingOptions();
    }
    
    void showPolarOptions(java.awt.event.ActionEvent evt) {        
        PolarBearForm polar = new PolarBearForm();
        polar.setLocationRelativeTo(this);
        polar.setVisible(true);
    }
    
    void showRedistributeGrassland() {
        String percent = JOptionPane.showInputDialog(this, "Enter the percentage of grasslands that should be bonus, ex. 5 for 5%:", "Bonus Grassland Percentage", JOptionPane.PLAIN_MESSAGE);
        int iPercent = Integer.parseInt(percent);
        
        IO biq = Main.biqFile.get(Main.biqIndex);
        List<TILE>tiles = biq.tile;
        
        Random random = new Random();
        
        for (TILE t : tiles)
        {
            if (t.getRealTerrain() == 2) //grassland
            {
                int rnd = random.nextInt(100);
                t.setBonusGrassland(rnd < iPercent);  //ex. if they enter 5, we want 4 or less to result in bonus, otherwise not bonus
            }
        }
        JOptionPane.showMessageDialog(this, "The bonus grassland has been redistributed", "Success!", JOptionPane.INFORMATION_MESSAGE);
    }
    
    void clearAllCities() {
        biqFile.get(biqIndex).city.clear();
        for (TILE t : biqFile.get(biqIndex).tile) {
            t.setCity((short)-1);
            t.citiesWithInfluence.clear();
        }
        biqFile.get(biqIndex).calculateTileOwners();
    }
    
    void clearAllUnits() {
        biqFile.get(biqIndex).mapUnit.clear();
        for (TILE t : biqFile.get(biqIndex).tile) {
            t.unitsOnTile.clear();
            t.unitWithBestDefence = -1;
        }
    }
    
    void changeZoomPercentage(ActionEvent evt)
    {
        String s = JOptionPane.showInputDialog(this, "Enter the zoom percent (1 to 1000)", "Zoom percent", JOptionPane.QUESTION_MESSAGE);
        if (s == null) //cancel
            return;
        try{
            Double d = Double.parseDouble(s);
            pnlTabs.mapTab.map.setZoom(d);
        }
        catch (NumberFormatException e){
            JOptionPane.showMessageDialog(null, s + " is not a valid zoom.  Leaving zoom at " + MapPanel.zoom);
        }
        catch(OutOfMemoryError e)
        {
            JOptionPane.showMessageDialog(null, "Whoops!  There's not enough memory to zoom out that far!  Set a lower zoom, and you'll be back in business", "Satellites can't orbit that far out!", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void setFontRecursive(java.awt.Component[] components, java.awt.Font font) {
        for (java.awt.Component c : components) {
            c.setFont(font);
            if (c instanceof java.awt.Container)
                setFontRecursive(((java.awt.Container)c).getComponents(), font);
        }
    }

    /**
     * Loads the interface (GUI) elements after a successful file opening.  This
     * includes:
     *
     * <ul>
     *      <li>Opening PCX files, and extracting civilization colors from PCX files.</li>
     *      <li>Sending data to the appropriate tabs, so they can update their interfaces</li>
     *      <li>Setting the final fileOpened variable, as this is the last step in opening a file</li>
     * </ul>
     *
     * This method should only be called by the openFile method, and only if
     * a file is successfully opened.  This may be modified if a "partial recovery"
     * option is implemented in the future for corrupted BIQ files.
     */
    private void loadInterfaceElements()
    {
        try{
            //if (biqFile.get(biqIndex).hasCustomRules())
            if (true)
            {
                if (logger.isInfoEnabled())
                            logger.info("civInstallDir: " + settings.civInstallDir);
                if (GRAPHICS_ENABLED) {
                    if (!loadNTPColors(biqFile.get(biqIndex)))
                        return;
                }
                else {
                    colors = new Color[32];
                    for (int i = 0; i < 32; i++) {
                        colors[i] = Color.MAGENTA;
                    }
                }
                if (GRAPHICS_ENABLED) {
                    PCXFilter units_32;
                    String units32Name = null;
                    try{
                        units32Name = utils.findFile("units_32.pcx", "Art" + fileSlash + "Units" + fileSlash, biqFile.get(biqIndex));
                    }
                    catch(FileNotFoundException e){
                        logger.error("Could not find units_32.pcx; civInstallDir = " + settings.civInstallDir, e);
                        JOptionPane.showMessageDialog(null, "Could not find units_32.pcx.  The civ install dir (" + settings.civInstallDir + ") being incorrect may cause this problem.  This can be changed by clicking on the Settings button.\nError: " + e.getMessage());
                        return;
                    }
                    units_32 = new Civ3PCXFilter(units32Name);
                    if (logger.isDebugEnabled())
                        logger.debug("about to read the units32");
                    units_32.readFile();
                    if (logger.isDebugEnabled())
                        logger.debug("about to parse the units32");
                    units_32.parse();
                    if (logger.isDebugEnabled())
                        logger.debug("about to buffer the units32");
                    units_32.createBufferedImage();
                    units32 = units_32.getBufferedImage();
                    //figure out how many images there are in the units32
                    int width = units32.getWidth();
                    if (width != 463 && logger.isInfoEnabled())
                        logger.info("Nonstandard height of units_32.pcx - should be okay in editor 0.57+");
                    int height = units32.getHeight();
                    int localWidth = units32.getWidth();
                    unitIcons = new BufferedImage[(localWidth-1)/32][(height-1)/32];
                    for (int i = 0; i < (localWidth-1)/32; i++)
                    {
                        for (int j = 0; j < (height-1)/33; j++)
                        {
                            unitIcons[i][j] = units32.getSubimage((i*32)+1+i, (j*32)+1+j, 32, 32);
                        }
                    }
                    if (logger.isDebugEnabled())
                        logger.debug("got the units32 determined");
                                    //graphics
                }
                if (GRAPHICS_ENABLED)
                {
                    PCXFilter resources;
                    String resourcesName = null;
                    try{
                        resourcesName = utils.findFile("resources.pcx", "Art" + fileSlash, biqFile.get(biqIndex));
                    }
                    catch(FileNotFoundException e){
                        logger.error("Could not find resources.pcx; civInstallDir = " + settings.civInstallDir, e);
                        JOptionPane.showMessageDialog(null, "Could not find resources.pcx.  The civ install dir (" + settings.civInstallDir + ") being incorrect may cause this problem.  This can be changed by clicking on the Settings button.\nError: " + e.getMessage());
                        return;
                    }
                    resources = new PCXFilter(resourcesName);
                    resources.readFile();
                    resources.parse();
                    resources.createBufferedImage();
                    BufferedImage resourcesBI = resources.getBufferedImage();
                    //figure out how many images there are in the resources.pcx
                    int width = resourcesBI.getWidth();
                    int height = resourcesBI.getHeight();
                    resourceIcons = new BufferedImage[(width)/50 * (height)/50];
                    //j iterates over rows, i iterates over columns
                    for (int j = 0; j < (height)/50; j++)
                    {
                        for (int i = 0; i < (width)/50; i++)
                        {
                            resourceIcons[i + j*(width/50)] = resourcesBI.getSubimage((i*50), (j*50), 50, 50);
                        }
                    }
                }
                pnlTabs.loadInterfaceElements(colors, unitIcons, resourceIcons, units32);
                
                alertToSafetyUpdate();
            }
            else
            {
                titleRemainder = "";
                setTitle(TITLE);
                JOptionPane.showMessageDialog(null, "Only BIQs with custom rules supported.  This is due to not having any native data built into the editor from which to know what the default rules are supposed to be.  Support to be added later.  \n\nSorry.");
                return;
            }
            //The Import Graphics call should only be made if the graphics are enabled
            //It works in its own thread
            if (GRAPHICS_ENABLED && biqFile.get(biqIndex).hasCustomMap()) {
                pnlTabs.importMapGraphics();
            }           
            //TODO
            //if (biqFile.get(biqIndex).hasCustomPlayerData())
            //     plyrTab.sendData(biqFile.get(biqIndex).player, colors);
            fileOpen = true;
        }
        catch(Exception e)
        {   //there appears to be a possible NPE exception or something going on; need to know what it is
            logger.error("Unexpected exception: ", e);
            JOptionPane.showMessageDialog(null, "An unexpected exception occured: " + e.toString(), "Unexpected exception", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Loads colors from the ntpXX.pcx files.
     * @return true if successful
     * @throws HeadlessException
     */
    private boolean loadNTPColors(IO biq) throws HeadlessException {
        if (logger.isTraceEnabled())
            logger.trace("pcxNames.length: " + pcxNames.length);
        PCXFilter[]pcxFiles = new PCXFilter[pcxNames.length];
        colors = new Color[pcxNames.length];
        for (int i = 0; i < pcxNames.length; i++) {
            String pcxName = "";
            try {
                pcxName = findPaletteFile(pcxNames[i] + ".pcx", biq);
            } catch (FileNotFoundException e) {
                logger.error("PCX file not found for " + pcxName + "; civInstallDir = " + settings.civInstallDir, e);
                JOptionPane.showMessageDialog(null, "Could not find the pcx file " + pcxName + ".  The civ install dir (" + settings.civInstallDir + ") being incorrect may cause this problem.  This can be changed by clicking on the Settings button.\nError: " + e.getMessage());
                return false;
            }
            pcxFiles[i] = new PCXFilter(pcxName);
            pcxFiles[i].readFile();
            pcxFiles[i].parse();
            //we know there's only one pixel
            colors[i] = pcxFiles[i].getColorAt(0, 0);
        }
        if (logger.isInfoEnabled())
            logger.info("colors determined");
        long endColors = System.nanoTime();
        return true;
    }

    private static String findPaletteFile(String name, IO biq) throws java.io.FileNotFoundException
    {
        return utils.findFile(name, "Art" + fileSlash + "Units" + fileSlash + "Palettes" + fileSlash, biq);
    }
    
    /**
     * Updates the directory for the file chooser.  Should occur on load and when
     * the settings are updated.
     */
    public static void updateFileChooserDirectories() {
        FileIO.setCurrentDirectory(new File(settings.openDir));
    }

    /**
     * Note that thanks to the static initializer, main is not the first thing to run.
     * So the settings are already available.
     * @param args 
     */
    public static void main(String[]args) {
        final String fileToOpen;
        
        if (args.length > 0) {
            editorDirectory = args[0];
        }
        if (args.length > 1) {
            fileToOpen = args[1];
        }
        else {
            fileToOpen = "";
        }
        
        IO.pathToBin = editorDirectory;
        imagePath = editorDirectory + imagePath;
        setupLogging(editorDirectory);
        processConfigFile(editorDirectory);
        setupLocale();
        
        long allocatedToJVM = Runtime.getRuntime().totalMemory();
        long allocatedButFree = Runtime.getRuntime().freeMemory();

        long inUseKB = (allocatedToJVM - allocatedButFree)/1024;
        
        logger.info("Program launch.  Memory in use: " + inUseKB/1024 + " MB");
        logger.info("Program launch.  Memory available: " + Runtime.getRuntime().maxMemory()/1024/1024 + " MB");
        
        if (false) {
            logger.info("Number of args = " + args.length);
            for (int i = 0; i < args.length; i++) {
                logger.info("args[" + i + "] = " + args[i]);
            }
        }
        
        updateFileChooserDirectories();
        jfcBMPChooser.setCurrentDirectory(new File(settings.bmpDir));

        //TODO: If Tahoma not present, use a fallback
        //Should not cause a problem except for Linux, as Apple also bundles Trebuchet MS and Tahoma with Mac OS 10.5 and higher
        setUIFont(new javax.swing.plaf.FontUIResource(settings.fontChoice, java.awt.Font.PLAIN, settings.fontSize));

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try{
                    if (settings.lookAndFeel.equals("System")) {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    }
                    else if (settings.lookAndFeel.equals("Metal")) {
                        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                    }
                }
                catch(java.lang.ClassNotFoundException e){
                    logger.error("Could not find the class javax.swing.plaf.metal.MetalLookAndFeel.  This should be included in your Java distribution.", e);
                }
                catch(java.lang.InstantiationException e){
                    logger.error("Could not instantiate the Metal look and feel", e);
                }
                catch(java.lang.IllegalAccessException e){
                    logger.error("Illegal access on the Metal look and feel", e);
                }
                catch(javax.swing.UnsupportedLookAndFeelException e){
                    logger.error("The look and feel is not present and/or supported on your system", e);
                }
                //It takes about 140 ms for the setVisible method all by itself.  Keep that in mind when trying to optimize.
                Main main = new Main(fileToOpen);
                main.setLocationRelativeTo(null); //Make it centered
                main.setVisible(true);
                if (logger.isInfoEnabled())
                    logger.info("Took this many ms to get window visible");
                //setUIFont(new javax.swing.plaf.FontUIResource(fontChoice, java.awt.Font.PLAIN, 12));
            }
        });
    }

    /**
     * Sets the default font for all Swing components.
     *
     * @param f - The font resource to use.
     *
     * Example:  setUIFont(new javax.swing.plaf.FontUIResource("Serif",Font.ITALIC,12));
     */
    public static void setUIFont(javax.swing.plaf.FontUIResource f){
        UIManager.put("Button.font", f);
        UIManager.put("CheckBox.font", f);
        UIManager.put("CheckBoxMenuItem.font", f);
        UIManager.put("ColorChooser.font", f);
        UIManager.put("ComboBox.font", f);
        UIManager.put("EditorPane.font", f);
        UIManager.put("FormattedTextField.font", f);
        UIManager.put("IconButton.font", f);
        UIManager.put("Label.font", f);
        UIManager.put("List.font", f);
        UIManager.put("Menu.font", f);
        UIManager.put("MenuBar.font", f);
        UIManager.put("MenuItem.font", f);
        UIManager.put("OptionPane.font", f);
        UIManager.put("Panel.font", f);
        UIManager.put("PasswordField.font", f);
        UIManager.put("PopupMenu.font", f);
        UIManager.put("ProgressBar.font", f);
        UIManager.put("RadioButton.font", f);
        UIManager.put("RadioButtonMenuItem.font", f);
        UIManager.put("ScrollPane.font", f);
        UIManager.put("Slider.font", f);
        UIManager.put("Spinner.font", f);
        UIManager.put("TabbedPane.font", f);
        UIManager.put("Table.font", f);
        UIManager.put("TableHeader.font", f);
        UIManager.put("TextArea.font", f);
        UIManager.put("TextField.font", f);
        UIManager.put("TextPane.font", f);
        UIManager.put("TitledBorder.font", f);
        UIManager.put("ToggleButton.font", f);
        UIManager.put("ToolBar.font", f);
        UIManager.put("ToolTip.font", f);
        UIManager.put("Tree.font", f);
        UIManager.put("Viewport.font", f);
    }

    /**
     * This method is called by FrmSafetyLevels when the user updates the safety
     * levels.  The Main class, once alerted to the change, will tell all the
     * EditorTabs about the update.
     */
    public void alertToSafetyUpdate()
    {
        pnlTabs.alertToSafetyUpdate(safetyLevels);
    }
    
    private volatile boolean loadAcceleratorDone = false;
    
    public static String i18n(String key) {
        return i18n.getString(key);
    }

    /**
     * This class is used to speed up the load.  The basic theory is that
     * since not everything is visible at the start, we don't need to have it
     * loaded right away, either.  So we load what we need to have right away,
     * show the form, and then spin off a thread to do the work in this class
     * in the background.  Chances are the user won't request anything that this
     * class loads before it is ready, but if they do there's just a slight 
     * wait until this class finishes its work.
     */
    public class LoadAccelerator extends Thread
    {
        public LoadAccelerator(Main main)
        {
            this.main = main;
        }

        Main main;
        public void run()
        {
            //Initialize JavaFX
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new JFXPanel();
                    Platform.setImplicitExit(false);
                }
            });
            
            if (logger.isDebugEnabled())
                logger.debug("running the load accelerator");
            //First we'll add action listeners to the buttons that are disabled at start.
            //This shaves a few ms off the load time.
            menus.setupDelayedActions();
            
            pnlTabs.setup(main);

            gc.gridy = 1;
            gc.gridx = 0;
            gc.weighty = 1;
            gc.weightx = 1;
            gc.gridwidth = 12;
            gc.fill = GridBagConstraints.BOTH;
            getContentPane().add(pnlTabs, gc);
            
            //JavaFX Icon
            javaFXImage = new javafx.scene.image.Image("file:imgs/icon.PNG");
                    
            safetyLevelWindow = new FrmSafetyLevel();
            safetyLevelWindow.setIconImage(icon);
            //safety levels set up prior to config file read-in

            safetyLevelWindow.syncWithMain(safetyLevels, main);
            logger.info("load accelerator done");
            loadAcceleratorDone = true;
        }
    }
    
    public static javafx.scene.image.Image javaFXImage = null;
    
    public static EditorTabbedPane pnlTabs;

    public void integrateWithOSX() {
        try {
            Class appClass = Class.forName("com.apple.eawt.Application");
            Object editor = appClass.getMethod("getApplication", new Class[0]).invoke(null, new Object[0]);
            
            Method setDockImage = appClass.getMethod("setDockIconImage", Image.class);
            setDockImage.invoke(editor, icon);

            // Todo: Reflectify
//            editor.setPreferencesHandler((preferencesEvent) -> {
//                cmdSettingsActionPerformed(null);
//            });
        }
        catch(ClassNotFoundException ex) {
            logger.error("com.apple.eawt.Application class not found", ex);
        }
        catch(NoSuchMethodException ex) {
            logger.error("Apple methods not found", ex);
        }
        catch(IllegalAccessException | InvocationTargetException ex) {
            logger.error("IllegalAccess or InvocationTargetException (unexpected)", ex);
        }
    }
    
    //Make it look native on OSX
    @Override
    public void handleAbout(ApplicationEvent event) {
        //aboutAction.actionPerformed(null);
        if (aboutPanel == null)
            aboutPanel = new AboutPanel();
        aboutPanel.setLocationRelativeTo(this);
        aboutPanel.setVisible(true);
        if (event != null) {    //On Windows, when invoked manually, event will be null
            event.setHandled(true);
        }
    }

    public void handleOpenApplication(ApplicationEvent event) {
        // Ok, we know our application started
        // Not much to do about that..
    }

    public void handleOpenFile(ApplicationEvent event) {
        //openFileInEditor(new File(event.getFilename()));
    }

    public void handlePreferences(ApplicationEvent event) {
        //preferencesAction.actionPerformed(null);
    }

    public void handlePrintFile(ApplicationEvent event) {
        //JOptionPane.showMessageDialog(this, "Sorry, printing not implemented");
    }

    public void handleQuit(ApplicationEvent event) {
        quitAction();
    }

    public void handleReOpenApplication(ApplicationEvent event) {
        setVisible(true);
    }
    
    public static  void enableMapTab() {
        pnlTabs.importMapGraphics();
        pnlTabs.plyrTab.sendMapTabLink(pnlTabs.mapTab);
        pnlTabs.biqcTab.updateCheckBoxes();
        pnlTabs.gameTab.worldChar = biqFile.get(biqIndex).worldCharacteristic;
    }
    
    public static void addCustomRules() {
        IO biq = biqFile.get(biqIndex);
        if (biq.hasCustomRules() == false) {
            biq.setCustomRules(true);
            //Editor tab enablement
            pnlTabs.enableCustomRules();
        }
    }
    
    public static void addCustomPlayerData() {
        IO biq = biqFile.get(biqIndex);
        if (biq.hasCustomPlayerData() == false) {
            biq.setCustomPlayerData(true);
            //Editor tab enablement
            pnlTabs.enableCustomPlayerData();
        }
    }
    
    /**
     * 
     * @param key
     * @param value
     * @return The integer value of a key.
     */
    private static int regParseIntValue(String regString)
    {
        String hex = regString.substring(2);
        return Integer.parseInt(hex, 16);
    }
    
    /**
     * 
     * @param key
     * @param type - REG_GZ, REG_DWORD, etc.
     * @param value
     * @return value if found, "" otherwise
     */
    private static String regKeyValue(String key, String type, String value)
    {
        String result = "";
        String[]registryQuery = {"reg", "query", key};
        java.io.InputStream i = null;
        Process reg = null;
        int numBytes = 0;
        try{
            reg = Runtime.getRuntime().exec(registryQuery);
            i = reg.getInputStream();
            reg.waitFor();
            //Now we should have the query
            numBytes = i.available();
        }
        catch(IOException e)
        {
            logger.warn("Registry query failed", e);
            return result; //return nothing; since our reg query failed we won't be able to auto-detect
            //This can happen on systems that don't have reg query, namely Windows 98 and ME
        }
        catch(java.lang.InterruptedException e)
        {
            logger.warn("Exception", e);
        }
        if (logger.isDebugEnabled())
            logger.debug("Got " + numBytes + " bytes from the registry query");
        byte[] regBytes = new byte[numBytes];
        String queryResult = null;
        try{
            i.read(regBytes);
            queryResult = new String(regBytes, "ISO-8859-1");
        }
        catch(java.io.UnsupportedEncodingException e)
        {
            logger.warn("Unsupported encoding", e);
        }
        catch(IOException e)
        {
            logger.warn("Registry query failed", e);
        }
        if (logger.isDebugEnabled())
            logger.debug(queryResult);
        //OK, at this point we've got the result of the query.  Now we see if it contains what we want.
        //If it says invalid key, it doesn't
        if (!queryResult.contains("Invalid key name"))
        {
            //so far so good, parse it
            StringTokenizer t = new StringTokenizer(queryResult, "\r\n");
            while (t.hasMoreTokens())
            {
                String nextLine = t.nextToken();
                if (logger.isDebugEnabled())
                    logger.debug("Line: " + nextLine);
                if (!nextLine.contains(value))
                    continue;
                StringTokenizer lineTokenizer = new StringTokenizer(nextLine, "\t");
                while (lineTokenizer.hasMoreTokens())
                {
                    String nextToken = lineTokenizer.nextToken();
                    if (logger.isDebugEnabled())
                        logger.debug("Token: " + nextToken);
                    if (!nextToken.contains(type))
                        continue;
                    if (nextToken.equals(nextLine)) //Vista/7 - tabs inconveniently expanded to spaces
                    {
                        int w = nextToken.indexOf(type);
                        w+=(4 + type.length());  //length of type, and four spaces
                        result = nextToken.substring(w);
                    }
                    else{   //XP or earlier, works nice and easily
                        try{
                            result = lineTokenizer.nextToken();
                        }
                        catch(NoSuchElementException e)
                        {
                            break;
                        }
                    }
                    break;
                }
                break;
            }
        }
        return result;
    }
    
    /**
     * Sets up minimal rules in case custom rules are disabled.
     * For example, names will be present, but stats may be missing.
     */
    private void setUpRuleInfrastructure()
    {
        String baseBIQ = utils.getConquestsFolder(settings.civInstallDir) + "conquests.biq";
        File file = new File(baseBIQ);
        IO baseRules = openSpecificFile(file);
       
        //Merge stuff
        IO biq = biqFile.get(biqIndex);
        biqFile.get(biqIndex).buildings = baseRules.buildings;
        biqFile.get(biqIndex).citizens = baseRules.citizens;
        biqFile.get(biqIndex).culture = baseRules.culture;
        biqFile.get(biqIndex).difficulties = baseRules.difficulties;
        biqFile.get(biqIndex).eras = baseRules.eras;
        biqFile.get(biqIndex).espionage = baseRules.espionage;
        biqFile.get(biqIndex).experience = baseRules.experience;
        biqFile.get(biqIndex).resource = baseRules.resource;
        biqFile.get(biqIndex).government = baseRules.government;
        biqFile.get(biqIndex).rule = baseRules.rule;
        biqFile.get(biqIndex).unit = baseRules.unit;
        biqFile.get(biqIndex).numUnits = baseRules.numUnits;
        biqFile.get(biqIndex).civilization = baseRules.civilization;
        biqFile.get(biqIndex).technology = baseRules.technology;
        biqFile.get(biqIndex).workerJob = baseRules.workerJob;
        biqFile.get(biqIndex).terrain = baseRules.terrain;
        biqFile.get(biqIndex).worldSize = baseRules.worldSize;
        biqFile.get(biqIndex).flavor = baseRules.flavor;
        
        if (biq.convertToConquests == 1 && biq.scenarioProperty.get(0).civPartOfWhichAlliance.size() == 0) {
            //Converted from early vanilla (BIC version 2.x) without custom rules.
            for (int i = 1; i < baseRules.civilization.size(); i++) {
                biq.scenarioProperty.get(0).civPartOfWhichAlliance.add(0);
            }
        }
        
        //Make sure we aren't depending on this reference.
        baseRules = null;
        
        
//        List<BLDG> buildings = biqFile.get(biqIndex).buildings;
//        int count = 0;
//        buildings.add(count, new BLDG("Palace", 7, biqFile.get(biqIndex)));
//        count++;
//        buildings.add(count, new BLDG("Barracks", 7, biqFile.get(biqIndex)));
//        count++;
    }
    
    private void setUpPlayerDataInfrastructure() {
        for (int i = 0; i < 8; i++) {
            LEAD player = new LEAD(biqFile.get(biqIndex));
            player.addDefaultStartUnits();
            if (i == 0) {
                player.humanPlayer = 1;
            }
            biqFile.get(biqIndex).player.add(player);
        }
    }
    
    public void updateBIQLinks()
    {
//        if (biqFile.get(biqIndex).hasCustomRules())
//            pnlTabs.sendRuleData();
//        if (biqFile.get(biqIndex).hasCustomPlayerData())
//            pnlTabs.sendPlayerData();
        if (biqFile.get(biqIndex).hasCustomMap())
            pnlTabs.mapTab.map.sendData(biqFile.get(biqIndex).worldMap.get(0), biqFile.get(biqIndex).tile, biqFile.get(biqIndex).resource, biqFile.get(biqIndex).city, biqFile.get(biqIndex).rule, biqFile.get(biqIndex).civilization, biqFile.get(biqIndex));
    }
    
    public static IO getCurrentBIQ() {
        return biqFile.get(biqIndex);
    }
    
    /**
     * Allows setting a test BIQ file for unit testing.
     * This is not intended to be used for "real" feature development.
     * @param testBiqFile The test BIQ file.  May be partial.
     */
    public static void setBIQForTesting(IO testBiqFile) {
        biqFile = new ArrayList<IO>();
        biqFile.add(testBiqFile);
        biqIndex = 0;
    }
    
    public static void undoPush(BIQSection b, int index) {
        currentUndoStack.push(b, index);
    }
    
    static JOptionPane oomErrorMessage = new JOptionPane(
            "Ran out of memory while importing graphics.  Please start the editor via launcher.jar, which allows the editor to use more memory.",
            JOptionPane.ERROR_MESSAGE,
            JOptionPane.OK_OPTION
        );
    
    public static void displayOOMMessage() {
        Main.mainMain.setTitle("Out of memory.  Please restart via launcher.jar to allow the editor to use more memory.");
        pnlTabs.setEnabled(false);
        oomErrorMessage.setVisible(true);
    }
}
