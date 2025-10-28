/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.civfanatics.civ3.xplatformeditor;

import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Andrew
 */
public class SettingsPanel extends JFrame {


    static Logger logger = Logger.getLogger("Settings Panel");

    final Settings settings;
    //Design: a text box
    //        folder settings
    //        advanced settings
    //        OK/Cancel
    //Nifty - I can use HTML with JLabels!
    JLabel lblWhatToDo = new JLabel("<HTML>Use these options to configure the editor, and then click Save<BR>to save the new settings or Cancel to cancel.<BR>Hover the mouse over the labels for help.</HTML>");
    JButton cmdSave = new JButton("Save");
    JButton cmdCancel = new JButton("Cancel");


    JPanel folders = new JPanel();
    JLabel lblOpenDir = new JLabel("Default opening directory");
    SuperJTextField openDir = new SuperJTextField();
    JButton openDirBrowse = new JButton("Browse");
    JLabel lblCivDir = new JLabel("Civ3 Install Directory");
    SuperJTextField civInstallDir = new SuperJTextField();
    JButton civInstallBrowse = new JButton("Browse");
    JPanel advanced = new JPanel();
    JPanel biqValidation = new JPanel();
    JLabel lblDebugLevel = new JLabel("Debug Level");
    Vector<String>debugLevels;
    JComboBox cmbDebugLevel;
    JLabel numProcs = new JLabel("Number of processors");
    JSpinner spnrNumProcs = new javax.swing.JSpinner();

    JTabbedPane tabs = new JTabbedPane();
    JPanel pnlMain = new JPanel();
    JPanel pnlMap = new JPanel();
    JPanel pnlAutoSave = new JPanel();
    JLabel lblInterval = new JLabel("Autosave Interval (seconds):");
    SpinnerNumberModel intervalModel = new SpinnerNumberModel(240, 1, Integer.MAX_VALUE, 1);
    JSpinner spnrInterval = new JSpinner(intervalModel);
    JLabel lblNumSaves = new JLabel("Number of autosaves");
    SpinnerNumberModel numSavesModel = new SpinnerNumberModel(10, 1, Integer.MAX_VALUE, 1);
    JSpinner spnrNumSaves = new JSpinner(numSavesModel);
    JLabel lblAutoArchive = new JLabel("<HTML>Auto archive is a feature that will automatically<BR>save a copy of your previous save when you save again,<BR>with _Archive_ and the date and time as part of the<BR>name.  That way you can go back to an older version if<BR>need be.</HTML>");
    JCheckBox chkAutoArchive = new JCheckBox("Auto Archive");
    
    JCheckBox chkInfiniteUpgrade = new JCheckBox("Infinite Upgrade Loop");
    JCheckBox chkPhantomResources = new JCheckBox("Phantom Resource Bug");

    //Map components
    JLabel lblAboutMapSettings = new JLabel("<HTML>You may optionally disable some layers of the map<BR>from displaying, just as you can in regular Civ3.</HMTL>");
    JCheckBox chkWoodlandsEnabled = new JCheckBox("Forest/Jungle");
    JCheckBox chkHllsEnabled = new JCheckBox("Hills/Mountains");
    JCheckBox chkMarshEnabled = new JCheckBox("Marsh");
    JCheckBox chkResourcesEnabled = new JCheckBox("Resources");
    JCheckBox chkRiversEnabled = new JCheckBox("Rivers");
    JCheckBox chkRoadsEnabled = new JCheckBox("Roads/Railroads");
    JCheckBox chkIrrigationEnabled = new JCheckBox("Irrigation");
    JCheckBox chkMinesEnabled = new JCheckBox("Mines");
    JCheckBox chkFortificationsEnabled = new JCheckBox("Fortifications");
    JCheckBox chkUnitsEnabled = new JCheckBox("Units");
    JCheckBox chkCitiesEnabled = new JCheckBox("Cities");
    JCheckBox chkColonialBuildingsEnabled = new JCheckBox("Colonies");
    JCheckBox chkBordersEnabled = new JCheckBox("Borders");
    JCheckBox chkBarbarianCampsEnabled = new JCheckBox("Barbarian Camps");
    JCheckBox chkGoodyHutsEnabled = new JCheckBox("Goody Huts");
    JCheckBox chkPollutionEnabled = new JCheckBox("Pollution");
    JCheckBox chkRuinsEnabled = new JCheckBox("Ruins");
    JCheckBox chkCratersEnabled = new JCheckBox("Craters");
    JCheckBox chkVplsEnabled = new JCheckBox("Victory Points");
    JCheckBox chkCityNamesEnabled = new JCheckBox("City Names");
    JCheckBox chkXYOnMapEnabled = new JCheckBox("Coordinates on Map");
    JCheckBox chkGridOn = new JCheckBox("Grid");
    JCheckBox chkFoodAndShields = new JCheckBox("Food and Shields on Map");
    JCheckBox chkFogOfWar = new JCheckBox("Fog of War");
    JLabel lblDrawMode = new JLabel("<HTML>Select the draw mode.  This affects how high-quality<br>the map is when you zoom.  Default is \"Balanced\"</HTML>");
    JRadioButton rbtnQuick = new JRadioButton("Quick");
    JRadioButton rbtnBalanced = new JRadioButton("Balanced");
    JRadioButton rbtnQuality = new JRadioButton("Quality");
    ButtonGroup btnGroup = new ButtonGroup();
    JLabel lblRiverSensitivity = new JLabel("<HTML>River sensitivity options - river proximity determines<br>how close you must be to a tile edge to paint a river.<br>Corner radius is how far you must be from a tile corner.</HTML>");
    JLabel lblRiverProximity = new JLabel("River Proximity:");
    SpinnerModel mdlProximity = new SpinnerNumberModel(Main.settings.riverProximityMax, 2, 24, 2);
    JSpinner spnrRiverProximity = new JSpinner(mdlProximity);
    JLabel lblRiverCornerRadius = new JLabel("Corner Radius:");
    SpinnerModel mdlCornerRadius = new SpinnerNumberModel(Main.settings.riverCornerRadius, 2, 24, 2);
    JSpinner spnrCornerRadius = new JSpinner(mdlCornerRadius);
    
    JLabel lblFont = new JLabel("Font (requires restart)");
    Object[]mdlItems = {"Tahoma", "Arial", "LucidaSansRegular", "Trebuchet MS", "SimSun"};
    DefaultComboBoxModel mdlFonts = new DefaultComboBoxModel(mdlItems);
    JComboBox cmbFont = new JComboBox(mdlFonts);

    JLabel lblQuit = new JLabel("Confirm quit");
    JCheckBox chkQuit = new JCheckBox();

    JLabel lblGraphicsEnabled = new JLabel("Map enabled");
    JCheckBox chkGraphicsEnabled = new JCheckBox();
    
    JLabel lblSwingLists = new JLabel("Use old lists (req. restart)");
    JCheckBox chkSwingLists = new JCheckBox();
    
    JLabel lblHorizontalScroll = new JLabel("Horizontal mousewheel scrolling");
    JCheckBox chkHorizontalScrolling = new JCheckBox();
    
    JLabel lblBiqLanguage = new JLabel("BIQ Language");
    Object[]mdlItems2 = {"English", "Russian", "Chinese"};
    DefaultComboBoxModel mdlLanguages = new DefaultComboBoxModel(mdlItems2);
    JComboBox cmbBiqLanguage = new JComboBox(mdlLanguages);
    
    JLabel lblEditorLanguage = new JLabel("Editor Language");
    Object[]mdlItems3 = {"English", "Russian", "French"};
    DefaultComboBoxModel mdlEditorLangs = new DefaultComboBoxModel(mdlItems3);
    JComboBox cmbEditorLanguage = new JComboBox(mdlEditorLangs);
    
    JLabel lblFontSize = new JLabel("Font Size");
    Object[]mdlFontSizes = {"12", "14", "16"};
    DefaultComboBoxModel cmbFontSizesModel = new DefaultComboBoxModel(mdlFontSizes);
    JComboBox cmbFontSizes = new JComboBox(cmbFontSizesModel);
    
    JLabel lblLookAndFeel = new JLabel("Look and Feel");
    Object[]mdlLAF = {"Metal", "System", "Nimbus"};
    DefaultComboBoxModel cmbLAFModel = new DefaultComboBoxModel(mdlLAF);
    JComboBox cmbLAF = new JComboBox(cmbLAFModel);
    
    
    /**
     * The constructor.
     * @param s - The settings.
     * @param main - A link to the main panel.  This is so the SettingsPanel can
     * set the autosave interval if that settings is changed.
     */
    public SettingsPanel(Settings s, final Main main)
    {

        debugLevels = new Vector<String>();
        debugLevels.add("trace");
        debugLevels.add("debug");
        debugLevels.add("info");
        debugLevels.add("warn");
        debugLevels.add("error");
        cmbDebugLevel = new JComboBox(debugLevels);

        this.settings = s;
        this.setTitle("Settings");
        folders.setBorder(new TitledBorder("Folders used"));
        folders.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.01;
        c.weighty = 0.0;
        c.gridx = 0;
        c.gridy = 0;
        folders.add(lblCivDir, c);
        c.gridy++;
        folders.add(civInstallDir, c);
        c.gridx++;
        c.weightx = 0.0;
        folders.add(civInstallBrowse, c);
        c.weightx = 0.01;
        c.gridy++;
        c.gridx--;
        folders.add(lblOpenDir, c);
        c.gridy++;
        folders.add(openDir, c);
        c.gridx++;
        c.weightx = 0.0;
        folders.add(openDirBrowse, c);
        
        initializeAdvancedSection();
        initializeValidationSection();
        
        //Do not set a preferred size, and it will autoshrink to the size of its interior components
        //getContentPane().setPreferredSize(new Dimension(640, 480));
        pnlMain.setLayout(new GridBagLayout());

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 7;    //5 -> centered stuff
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.9;
        c.weighty = 0.0;

        //ok, top one is the label
        c.weightx = 0.9;
        c.weighty = 0.1;
        c.insets = new Insets(0, 5, 0, 5);
        pnlMain.add(lblWhatToDo, c);
        //ok, now the panels
        c.gridy = 1;
        c.weighty = 0.5;
        c.insets = new Insets(0, 0, 0, 0);
        pnlMain.add(folders, c);
        //next panel
        c.gridy++;
        pnlMain.add(advanced, c);
        
        c.gridy++;
        pnlMain.add(biqValidation, c);

        //Now set up the GUI for usefulness

        this.openDir.setText(settings.openDir);
        this.civInstallDir.setText((settings.civInstallDir));
        //set up the spinner
        spnrNumProcs.setModel(new SpinnerNumberModel(0, 0, 1048576, 1));
        try{
            this.spnrNumProcs.setValue(Integer.parseInt((settings.numProcs)));
        }
        catch(NumberFormatException e)
        {
            this.spnrNumProcs.setValue(-1);
        }
        cmbDebugLevel.setSelectedItem(settings.debugLevel);
        cmbFont.setSelectedItem(settings.fontChoice);
        cmbBiqLanguage.setSelectedItem(settings.biqLanguage);
        cmbEditorLanguage.setSelectedItem(settings.editorLanguage);
        if (settings.fontSize == 12) {
            cmbFontSizes.setSelectedItem("12");
        }
        else if (settings.fontSize == 14) {
            cmbFontSizes.setSelectedItem("14");
        }
        else if (settings.fontSize == 16) {
            cmbFontSizes.setSelectedItem("16");
        }
        cmbLAF.setSelectedItem(settings.lookAndFeel);


        //and add an action listener
        cmdSave.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                settings.openDir = openDir.getText();
                settings.civInstallDir = civInstallDir.getText();
                settings.debugLevel = (String)cmbDebugLevel.getSelectedItem();
                if (settings.debugLevel.equals("trace")) {
                    Logger.getRootLogger().setLevel(Level.TRACE);
                }
                else if (settings.debugLevel.equals("debug")) {
                    Logger.getRootLogger().setLevel(Level.DEBUG);
                }
                else if (settings.debugLevel.equals("info")) {
                    Logger.getRootLogger().setLevel(Level.INFO);
                }
                else if (settings.debugLevel.equals("warn")) {
                    Logger.getRootLogger().setLevel(Level.WARN);
                }
                else if (settings.debugLevel.equals("error")) {
                    Logger.getRootLogger().setLevel(Level.ERROR);
                }
                settings.numProcs = String.valueOf(((SpinnerNumberModel)spnrNumProcs.getModel()).getNumber().intValue());
                settings.fontChoice = cmbFont.getSelectedItem().toString();
                settings.confirmQuit = chkQuit.isSelected();
                settings.biqLanguage = cmbBiqLanguage.getSelectedItem().toString();
                settings.editorLanguage = cmbEditorLanguage.getSelectedItem().toString();
                
                settings.validateInfiniteUnitUpgrade = chkInfiniteUpgrade.isSelected();
                settings.validatePhantomResourceBug = chkPhantomResources.isSelected();

                settings.barbarianCampsEnabled = chkBarbarianCampsEnabled.isSelected();
                settings.bordersEnabled = chkBordersEnabled.isSelected();
                settings.citiesEnabled = chkCitiesEnabled.isSelected();
                settings.colonialBuildingsEnabled = chkColonialBuildingsEnabled.isSelected();
                settings.cratersEnabled = chkCratersEnabled.isSelected();
                settings.fortificationsEnabled = chkFortificationsEnabled.isSelected();
                settings.goodyHutsEnabled = chkGoodyHutsEnabled.isSelected();
                settings.hillsEnabled = chkHllsEnabled.isSelected();
                settings.irrigationEnabled = chkIrrigationEnabled.isSelected();
                settings.marshEnabled = chkMarshEnabled.isSelected();
                settings.minesEnabled = chkMinesEnabled.isSelected();
                settings.pollutionEnabled = chkPollutionEnabled.isSelected();
                settings.resourcesEnabled = chkResourcesEnabled.isSelected();
                settings.riversEnabled = chkRiversEnabled.isSelected();
                settings.roadsEnabled = chkRoadsEnabled.isSelected();
                settings.ruinsEnabled = chkRuinsEnabled.isSelected();
                settings.unitsEnabled = chkUnitsEnabled.isSelected();
                settings.vplsEnabled = chkVplsEnabled.isSelected();
                settings.woodlandsEnabled = chkWoodlandsEnabled.isSelected();
                settings.cityNamesEnabled = chkCityNamesEnabled.isSelected();
                settings.xyOnMapEnabled = chkXYOnMapEnabled.isSelected();
                settings.gridEnabled = chkGridOn.isSelected();
                Settings.showFoodAndShields = chkFoodAndShields.isSelected();
                settings.showFog = chkFogOfWar.isSelected();

                settings.maxAutosaves = ((SpinnerNumberModel)spnrNumSaves.getModel()).getNumber().intValue();
                if (settings.nextAutosave > settings.maxAutosaves)
                    settings.nextAutosave = 0;
                intervalModel.getNumber().intValue();
                settings.autoSaveInterval = (((SpinnerNumberModel)spnrInterval.getModel()).getNumber().intValue());
                main.setAutoSaveTimer();
                settings.autoArchive = chkAutoArchive.isSelected();

                main.GRAPHICS_ENABLED = chkGraphicsEnabled.isSelected();
                
                if (!Main.settings.forceSwing) {
                    settings.useSwing = chkSwingLists.isSelected();
                }
                
                Settings.horizontalScrolling = chkHorizontalScrolling.isSelected();
                
                if (rbtnBalanced.isSelected())
                    settings.zoomQuality = Image.SCALE_DEFAULT;
                else if (rbtnQuick.isSelected())
                    settings.zoomQuality = Image.SCALE_FAST;
                else if (rbtnQuality.isSelected())
                    settings.zoomQuality = Image.SCALE_SMOOTH;
                
                settings.riverProximityMax = (Integer)mdlProximity.getValue();
                settings.riverCornerRadius = (Integer)mdlCornerRadius.getValue();
                
                settings.fontSize = Integer.parseInt(cmbFontSizes.getSelectedItem().toString());
                settings.lookAndFeel = cmbLAF.getSelectedItem().toString();

                try {
                    settings.exportConfigFile();
                    Main.updateFileChooserDirectories();
                    dispose();
                }
                catch(ReadOnlyException ex) {
                    JOptionPane.showMessageDialog(null, "Cannot save because " + ex.getMessage() , "Cannot save settings", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        cmdCancel.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                dispose();
            }
        });
        //add a couple more
        this.openDirBrowse.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent a)
            {
                JFileChooser jFileChooser1 = new JFileChooser();
                File curLoc = new File(settings.openDir);
                if (curLoc.exists())
                    jFileChooser1.setCurrentDirectory(curLoc);
                jFileChooser1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int response = jFileChooser1.showOpenDialog(null);
                if (response == JFileChooser.APPROVE_OPTION)
                {
                    File file = jFileChooser1.getSelectedFile();
                    try{
                        settings.openDir = file.getCanonicalPath();
                        openDir.setText(settings.openDir);
                    }
                    catch(java.io.IOException e){
                        logger.error("Exception while trying to get canonical path of default opening dir", e);
                    }
                }
            }
        });
        this.civInstallBrowse.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent a)
            {
                JFileChooser jFileChooser1 = new JFileChooser();
                File curLoc = new File(settings.civInstallDir);
                if (curLoc.exists())
                    jFileChooser1.setCurrentDirectory(curLoc);
                jFileChooser1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int response = jFileChooser1.showOpenDialog(null);
                if (response == JFileChooser.APPROVE_OPTION)
                {
                    File file = jFileChooser1.getSelectedFile();
                    try{
                        settings.civInstallDir = file.getCanonicalPath();
                        civInstallDir.setText(settings.civInstallDir);
                        //Later, add some fancy schmancy checking to see if the civ install dir looks sensible
                    }
                    catch(java.io.IOException e){
                        logger.error("Exception while trying to get canonical path of default install dir", e);
                    }
                }
            }
        });

        //add some helpful tooltips
        javax.swing.ToolTipManager.sharedInstance().setDismissDelay(10000);
        lblDebugLevel.setToolTipText("<HTML>A log is kept in the file log.txt, in the same folder where the editor is, to help debug any bugs that are encountered.<BR>Requires restart to take effect.<BR>Warning: debug or trace may noticeably slow down program.</HTML>");
        numProcs.setToolTipText("<HTML>Number of processors to use.  0 = all available.</HTML>");
        lblCivDir.setToolTipText("<HTML>Where Civ3 (the base game, not Conquests, on Windows; Complete on Mac) is intalled.</HTML>");
        lblOpenDir.setToolTipText("<HTML>Where you'd like the program to use as your default BIQ file location.</HTML>");


        //Now the map half - first set the selected states
        chkWoodlandsEnabled.setSelected(settings.woodlandsEnabled);
        chkHllsEnabled.setSelected(settings.hillsEnabled);
        chkMarshEnabled.setSelected(settings.marshEnabled);
        chkResourcesEnabled.setSelected(settings.resourcesEnabled);
        chkRiversEnabled.setSelected(settings.riversEnabled);
        chkRoadsEnabled.setSelected(settings.roadsEnabled);
        chkIrrigationEnabled.setSelected(settings.irrigationEnabled);
        chkMinesEnabled.setSelected(settings.minesEnabled);
        chkFortificationsEnabled.setSelected(settings.fortificationsEnabled);
        chkUnitsEnabled.setSelected(settings.unitsEnabled);
        chkCitiesEnabled.setSelected(settings.citiesEnabled);
        chkColonialBuildingsEnabled.setSelected(settings.colonialBuildingsEnabled);
        chkBordersEnabled.setSelected(settings.bordersEnabled);
        chkBarbarianCampsEnabled.setSelected(settings.barbarianCampsEnabled);
        chkGoodyHutsEnabled.setSelected(settings.goodyHutsEnabled);
        chkPollutionEnabled.setSelected(settings.pollutionEnabled);
        chkRuinsEnabled.setSelected(settings.ruinsEnabled);
        chkCratersEnabled.setSelected(settings.cratersEnabled);
        chkVplsEnabled.setSelected(settings.vplsEnabled);
        chkCityNamesEnabled.setSelected(settings.cityNamesEnabled);
        chkXYOnMapEnabled.setSelected(settings.xyOnMapEnabled);
        chkGridOn.setSelected(settings.gridEnabled);
        chkFoodAndShields.setSelected(Settings.showFoodAndShields);
        chkFogOfWar.setSelected(settings.showFog);
        pnlMap.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.gridy = 0;
        c.gridx = 0;
        c.gridheight = 1;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        pnlMap.add(this.lblAboutMapSettings, c);
        c.gridy++;
        c.gridwidth = 1;
        pnlMap.add(this.chkWoodlandsEnabled, c);
        c.gridx++;
        pnlMap.add(this.chkHllsEnabled, c);
        c.gridy++;
        c.gridx--;
        pnlMap.add(this.chkMarshEnabled, c);
        c.gridx++;
        pnlMap.add(this.chkResourcesEnabled, c);
        c.gridy++;
        c.gridx--;
        pnlMap.add(this.chkRiversEnabled, c);
        c.gridx++;
        pnlMap.add(this.chkRoadsEnabled, c);
        c.gridy++;
        c.gridx--;
        pnlMap.add(this.chkIrrigationEnabled, c);
        c.gridx++;
        pnlMap.add(this.chkMinesEnabled, c);
        c.gridy++;
        c.gridx--;
        pnlMap.add(this.chkFortificationsEnabled, c);
        c.gridx++;
        pnlMap.add(this.chkUnitsEnabled, c);
        c.gridy++;
        c.gridx--;
        pnlMap.add(this.chkCitiesEnabled, c);
        c.gridx++;
        pnlMap.add(this.chkColonialBuildingsEnabled, c);
        c.gridy++;
        c.gridx--;
        pnlMap.add(this.chkBordersEnabled, c);
        c.gridx++;
        pnlMap.add(this.chkBarbarianCampsEnabled, c);
        c.gridy++;
        c.gridx--;
        pnlMap.add(this.chkGoodyHutsEnabled, c);
        c.gridx++;
        pnlMap.add(this.chkPollutionEnabled, c);
        c.gridy++;
        c.gridx--;
        pnlMap.add(this.chkRuinsEnabled, c);
        c.gridx++;
        pnlMap.add(this.chkCratersEnabled, c);
        c.gridy++;
        c.gridx--;
        pnlMap.add(this.chkVplsEnabled, c);
        c.gridx++;
        pnlMap.add(this.chkCityNamesEnabled, c);
        c.gridy++;
        c.gridx--;
        pnlMap.add(this.chkGridOn, c);
        c.gridx++;
        pnlMap.add(this.chkFoodAndShields, c);
        c.gridy++;
        c.gridx--;
        pnlMap.add(this.chkFogOfWar, c);
        
        //Map draw options
        c.gridx=0;
        c.gridy++;
        c.gridwidth = 2;
        pnlMap.add(this.lblDrawMode, c);
        
        if (settings.zoomQuality == Image.SCALE_DEFAULT)
            rbtnBalanced.setSelected(true);
        else if (settings.zoomQuality == Image.SCALE_FAST)
            rbtnQuick.setSelected(true);
        else if (settings.zoomQuality == Image.SCALE_SMOOTH)
            rbtnQuality.setSelected(true);
        
        rbtnQuick.setToolTipText("Less internsive, but doesn't look as good.  Recommended with less powerful processors, or with < 50% zoom.");
        rbtnBalanced.setToolTipText("Acceptable performance at 50% zoom with recent processors, and the default.");
        rbtnQuality.setToolTipText("Looks the best, but is slow.");
        btnGroup.add(rbtnQuick);
        btnGroup.add(rbtnBalanced);
        btnGroup.add(rbtnQuality);
        
        c.gridy++;
        c.gridwidth = 1;
        pnlMap.add(this.rbtnQuick, c);
        c.gridx++;
        pnlMap.add(this.rbtnBalanced, c);
        c.gridx--;
        c.gridy++;
        pnlMap.add(this.rbtnQuality, c);
        
        spnrRiverProximity.setValue(settings.riverProximityMax);
        spnrCornerRadius.setValue(settings.riverCornerRadius);
        c.gridy++;
        c.gridwidth = 2;
        pnlMap.add(this.lblRiverSensitivity, c);
        c.gridy++;
        c.gridwidth = 1;
        pnlMap.add(this.lblRiverProximity, c);
        c.gridx++;
        pnlMap.add(this.spnrRiverProximity, c);
        c.gridx--;
        c.gridy++;
        pnlMap.add(this.lblRiverCornerRadius, c);
        c.gridx++;
        pnlMap.add(this.spnrCornerRadius, c);
        
        //And now the autosaves part

        spnrNumSaves.setValue(settings.maxAutosaves);
        spnrInterval.setValue(settings.autoSaveInterval);
        chkAutoArchive.setSelected(settings.autoArchive);

        pnlAutoSave.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.gridy = 0;
        c.gridx = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        pnlAutoSave.add(lblNumSaves, c);
        c.gridx++;
        pnlAutoSave.add(spnrNumSaves, c);
        c.gridx--;
        c.gridy++;
        pnlAutoSave.add(lblInterval, c);
        c.gridx++;
        pnlAutoSave.add(spnrInterval, c);
        c.gridx--;
        c.gridy++;
        c.gridheight = 4;
        c.gridwidth = 2;
        c.ipady = 5;
        pnlAutoSave.add(lblAutoArchive, c);
        c.gridy+=4;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx++;
        c.ipady = 0;
        pnlAutoSave.add(chkAutoArchive, c);



        
        tabs.add(pnlMain, "Main");
        if (Main.GRAPHICS_ENABLED)
            tabs.add(pnlMap, "Map");
        tabs.add(pnlAutoSave, "Autosave");

        getContentPane().setLayout(new GridBagLayout());

        c = new GridBagConstraints();
        c.gridy = 0;
        c.weighty = 1;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.gridx = 0;
        getContentPane().add(tabs, c);
        //and the buttons
        c.gridy++;
        c.weighty = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        getContentPane().add(cmdSave, c);
        c.gridx++;
        getContentPane().add(cmdCancel, c);
        
        lblQuit.setLabelFor(chkQuit);
        lblGraphicsEnabled.setLabelFor(chkGraphicsEnabled);
        lblSwingLists.setLabelFor(chkSwingLists);
        lblHorizontalScroll.setLabelFor(chkHorizontalScrolling);

        this.pack();
        this.setResizable(false);
    }

    private void initializeAdvancedSection() {
        //Actions        
        cmbBiqLanguage.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent e)
            {
                if (e.getStateChange() != ItemEvent.SELECTED)
                    return;
                if (e.getItem().equals("Chinese"))
                {
                    cmbFont.setSelectedIndex(4);
                }
            }
        });
        
        //This'd look spiffier with GridBagLayout, but isn't bad with GridLayout
        advanced.setBorder(new TitledBorder("Advanced options"));
        advanced.setLayout(new GridLayout(12, 2));
        advanced.add(lblDebugLevel);
        advanced.add(cmbDebugLevel);
        advanced.add(numProcs);
        advanced.add(spnrNumProcs);
        advanced.add(lblFont);
        advanced.add(cmbFont);
        chkQuit.setSelected(settings.confirmQuit);
        lblQuit.setToolTipText("If checked, the program will ask you if you really want to quit when you exit.");
        advanced.add(lblQuit);
        advanced.add(chkQuit);
        chkGraphicsEnabled.setSelected(Main.GRAPHICS_ENABLED);
        lblGraphicsEnabled.setToolTipText("<HTML>If checked, map editing will be enabled.  Requires restart.<BR>Note: the map will take a few more seconds to load than the rest of the BIQ.<BR>It will be grayed out until it finishes loading.</HTML>");
        chkHorizontalScrolling.setSelected(Settings.horizontalScrolling);
        lblHorizontalScroll.setToolTipText("If selected, you can scroll horizontally on the map by holding down shift and scrolling with the mouse wheel.");
        advanced.add(lblGraphicsEnabled);
        advanced.add(chkGraphicsEnabled);
        chkSwingLists.setSelected(Main.settings.useSwing);
        if (Main.settings.forceSwing) {
            chkSwingLists.setSelected(true);
            chkSwingLists.setEnabled(false);
            if (Main.settings.forceSwingDueToJavaBug) {
                chkSwingLists.setToolTipText("Force-enabled due to bug on XP with recent Java update.");
            }
            else {
                chkSwingLists.setToolTipText("Force-enabled due to old Java version.");
            }
        }
        lblSwingLists.setToolTipText("<HTML>Use the old pre-1.10 lists on tabs such as TECH.  Note this is a temporary option,<br>which will eventually be removed.  Requires restart.</HTML>");
        advanced.add(lblSwingLists);
        advanced.add(chkSwingLists);
        advanced.add(lblHorizontalScroll);
        advanced.add(chkHorizontalScrolling);
        advanced.add(lblBiqLanguage);
        advanced.add(cmbBiqLanguage);
        advanced.add(lblEditorLanguage);
        advanced.add(cmbEditorLanguage);
        advanced.add(lblFontSize);
        advanced.add(cmbFontSizes);
        advanced.add(lblLookAndFeel);
        advanced.add(cmbLAF);
    }

    private void initializeValidationSection() {
        biqValidation.setBorder(new TitledBorder("BIQ Validation"));
        biqValidation.setLayout(new GridLayout(3, 1));
        
        JLabel lblBIQValidation = new JLabel("<html>The editor can validate BIQs upon save for certain types of bugs.<br/>These checks can be enabled or disabled from this section.<br/></html>");
        chkInfiniteUpgrade.setToolTipText("Upgrade loops for units will cause infinite AI turn times.");
        chkPhantomResources.setToolTipText("If two strategic/luxury resources are spaced 32 resources apart, odd behavior occurs in-game.  See Help for full details.");
        
        chkInfiniteUpgrade.setSelected(settings.validateInfiniteUnitUpgrade);
        chkPhantomResources.setSelected(settings.validatePhantomResourceBug);
        
        biqValidation.add(lblBIQValidation);
        biqValidation.add(chkInfiniteUpgrade);
        biqValidation.add(chkPhantomResources);
    }
}
