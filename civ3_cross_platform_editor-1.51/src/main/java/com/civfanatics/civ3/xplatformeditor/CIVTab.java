package com.civfanatics.civ3.xplatformeditor;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * @Completed
 */

/**
 *
 * @author Quintillus
 */
import com.civfanatics.civ3.xplatformeditor.tabs.civilization.ColorPanel;
import com.civfanatics.civ3.biqFile.RACE;
import com.civfanatics.civ3.biqFile.ERAS;
import com.civfanatics.civ3.biqFile.GAME;
import com.civfanatics.civ3.biqFile.PRTO;
import com.civfanatics.civ3.biqFile.GOVT;
import com.civfanatics.civ3.biqFile.TECH;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.ComboBoxColorRenderer;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJList;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJTextField;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperListModel;
import com.civfanatics.civ3.xplatformeditor.tabs.civilization.CivilizationUnitsController;
import java.awt.Color;
import java.awt.Component;
import java.awt.Event;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import org.netbeans.lib.awtextra.AbsoluteConstraints;

/**
 * This class represents the Civilization/RACE tab for the editor.
 *
 * @author Andrew
 */
public class CIVTab extends EditorTab {
    public List<RACE>civilization;    
    public List<PRTO>unit;
    public List<GOVT>government;
    public List<ERAS>era;
    public List<TECH>technology;
    UnitTab unitTab;    //link to unit tab, to update AvailableTo list model
    GAMETab gameTab;    //link to scnprop tab, to update alliance list models
    int civilizationIndex;
    int eraSelected = -1;
    
    JMenuItem delete;
    JMenuItem add;
    
    static final String fileSlash = System.getProperty("file.separator");

    private DefaultListModel listModelToDeleteFrom;  //enables delete to work with multiple models
    //That is, you can delete either a civ, or a scientific leader, or a city, etc., all with the same menu item
    private JList listToDeleteFrom;     //also enables delete to work
    private DefaultListModel cityList;
    private DefaultListModel militaryLeaderList;
    private DefaultListModel scientificLeaderList;
    private SuperListModel civilizationList;
    private JCheckBox chkRACEAgricultural;
    private JCheckBox chkRACECommercial;
    private JCheckBox chkRACEEmphasizeFood;
    private JCheckBox chkRACEEmphasizeShields;
    private JCheckBox chkRACEEmphasizeTrade;
    private JCheckBox chkRACEExpansionist;
    private JCheckBox chkRACEIndustrious;
    private JCheckBox chkRACEManageCitizens;
    private JCheckBox chkRACEManageProduction;
    private JCheckBox chkRACEManyAirUnits;
    private JCheckBox chkRACEManyArtillery;
    private JCheckBox chkRACEManyCulture;
    private JCheckBox chkRACEManyDefensiveLandUnits;
    private JCheckBox chkRACEManyExploration;
    private JCheckBox chkRACEManyGrowth;
    private JCheckBox chkRACEManyHappiness;
    private JCheckBox chkRACEManyOffensiveLandUnits;
    private JCheckBox chkRACEManyProduction;
    private JCheckBox chkRACEManyScience;
    private JCheckBox chkRACEManySettlers;
    private JCheckBox chkRACEManyShips;
    private JCheckBox chkRACEManyTrade;
    private JCheckBox chkRACEManyWealth;
    private JCheckBox chkRACEManyWorkers;
    private JCheckBox chkRACEMilitaristic;
    private JCheckBox chkRACENoAirUnits;
    private JCheckBox chkRACENoArtillery;
    private JCheckBox chkRACENoCulture;
    private JCheckBox chkRACENoDefensiveLandUnits;
    private JCheckBox chkRACENoExploration;
    private JCheckBox chkRACENoGrowth;
    private JCheckBox chkRACENoHappiness;
    private JCheckBox chkRACENoOffensiveLandUnits;
    private JCheckBox chkRACENoProduction;
    private JCheckBox chkRACENoScience;
    private JCheckBox chkRACENoSettlers;
    private JCheckBox chkRACENoShips;
    private JCheckBox chkRACENoSmallWonders;
    private JCheckBox chkRACENoTrade;
    private JCheckBox chkRACENoWealth;
    private JCheckBox chkRACENoWonders;
    private JCheckBox chkRACENoWorkers;
    private JCheckBox chkRACEReligious;
    private JCheckBox chkRACEScientific;
    private JCheckBox chkRACESeafaring;
    private JComboBox cmbRACEAnimationEra;
    private JComboBox cmbRACECultureGroup;
    JComboBox cmbRACEFavoriteGovernment;
    public JComboBox cmbRACEFreeTech1Index;
    public JComboBox cmbRACEFreeTech2Index;
    public JComboBox cmbRACEFreeTech3Index;
    public JComboBox cmbRACEFreeTech4Index;
    public JComboBox cmbRACEKingUnit;
    JComboBox cmbRACEShunnedGovernment;
    private JButton btnAddMilitaryLeader;
    private JButton btnAddCity;
    private JButton btnAddScientificLeader;
    private JLabel jLabel100;
    private JLabel jLabel101;
    private JLabel jLabel102;
    private JLabel jLabel103;
    private JLabel jLabel104;
    private JLabel jLabel105;
    private JLabel jLabel106;
    private JLabel jLabel107;
    private JLabel jLabel108;
    private JLabel jLabel109;
    private JLabel jLabel110;
    private JLabel jLabel111;
    private JLabel jLabel112;
    private JLabel jLabel113;
    private JLabel jLabel114;
    private JLabel jLabel115;
    private JLabel jLabel116;
    private JLabel jLabel117;
    private JLabel jLabel118;
    private JLabel jLabel119;
    private JLabel jLabel120;
    private JLabel lblCities;
    private JLabel lblMilitaryLeaders;
    private JLabel lblScientificLeaders;
    private JLabel jLabel124;
    private JLabel jLabel125;
    private JLabel jLabel126;
    private JLabel lblDiplomacyTextIndex;
    private JLabel lblDefaultColor;
    private JLabel lblUniqueColor;
    private JLabel lblUnknown;
    private JLabel jLabel94;
    private JLabel jLabel95;
    private JLabel jLabel96;
    private JLabel jLabel97;
    private JLabel jLabel98;
    private JLabel jLabel99;
    private JPanel jPanel33;
    private JPanel pnlPersonality;
    private JPanel jPanel59;
    private JPanel jPanel60;
    private JPanel pnlCitiesAndLeaders;
    private JPanel jPanel62;
    private JPanel jPanel63;
    private JPanel jPanel64;
    private JPanel jPanel65;
    private JScrollPane jScrollPane12;
    private JScrollPane scrCities;
    private JScrollPane scrMilitaryLeaders;
    private JScrollPane scrScientificLeaders;
    private JScrollPane jScrollPaneFlav;
    private SuperJTextField txtNewCity;
    private SuperJTextField txtMilitaryLeader;
    private SuperJTextField txtScienceLeader;
    private SuperJTextField txtRACEForwardFilename;
    private SuperJTextField txtRACEBackwardFilename;
    private SuperJList lstCivilizations = new SuperJList(this, "civilization");
    private JList lstRACECities;
    private JList lstMilitaryLeaders;
    private JList lstScientificLeaders;
    //private JPanel this;
    private JRadioButton rbtnRACEFemaleLeader;
    private JRadioButton rbtnRACEFeminine;
    private JRadioButton rbtnRACEMaleLeader;
    private JRadioButton rbtnRACEMasculine;
    private JRadioButton rbtnRACENeuter;
    private JRadioButton rbtnRACEPlural;
    private JRadioButton rbtnRACESingular;
    private JSlider sldRACEAggressionLevel;
    private SuperJTextField txtRACEAdjective;
    private SuperJTextField txtRACECivilopediaEntry;
    private JComboBox cmbRACEDefaultColor;
    private SuperJTextField txtRACEDiplomacyTextIndex;
    private SuperJTextField txtRACELeaderName;
    private SuperJTextField txtRACELeaderTitle;
    private SuperJTextField txtRACENoun;
    private SuperJTextField txtRACEQuestionMark;
    private JComboBox cmbRACEUniqueColor;
    private boolean hasCustomCivColors = false;
    private JButton cmdCivColors = new JButton("Edit Civ Colors");
    private JButton cmdSetUnitsAvailable = new JButton("Set Available Units");
    JList lstFlavors;
    
    DefaultComboBoxModel mdlKingUnit = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlFavoriteGovernment = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlShunnedGovernment = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlAnimationEra = new DefaultComboBoxModel();

    DefaultComboBoxModel mdlFreeTech1 = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlFreeTech2 = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlFreeTech3 = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlFreeTech4 = new DefaultComboBoxModel();

    private javax.swing.ButtonGroup btnGroupCivilizationGender;
    private javax.swing.ButtonGroup btnGroupCivilizationPlurality;
    private javax.swing.ButtonGroup btnGroupLeaderGender;
    ComboBoxColorRenderer colorRenderer;

    boolean tabCreated;

    public void setSelectedIndex(int i)
    {
        civilizationIndex = i;
    }

    /**
     * sendTabLinks methods are used to send references to other tabs to a tab
     * that needs access to GUI elements on those other tabs.
     * The CIV tab needs access to the UnitTab (for the Available To property)
     * and the GAMETab (Scenario Properties) for the Alliance properties.
     *
     * @param unitTab - Reference to the unit tab.
     * @param gameTab - Reference to the scenario property tab.
     */
    public void sendTabLinks(UnitTab unitTab, GAMETab gameTab)
    {
        this.unitTab = unitTab;
        this.gameTab = gameTab;
    }

    /**
     * sendData is used to send the tab the information it needs to fill in all
     * the fields it contains.  That way, rather than referencing "civilization
     * x", it can reference "the French", etc.
     *
     * This function also will do initialization tasks - i.e. loading the elements
     * into lists and combo boxes as needed.
     *
     * Requirements: The createTab method must be called first
     * 
     * @param civilization - The list of civilizations.
     * @param unit - The list of units.
     * @param government - The list of governments.
     * @param era - The list of era.
     * @param technology - The list of technologies.
     * @param colors - All the possible colors that can represent a civilization.
     */
    public void sendData(List<RACE>civilization, List<PRTO>unit, List<GOVT>government, List<ERAS>era, List<TECH>technology, Color[]colors)
    {
        assert tabCreated:"Tab must be created before it is sent data";
        this.civilization = civilization;
        this.technology = technology;
        this.unit = unit;
        this.government = government;
        this.era = era;
        civilizationIndex = -1;


        cmbRACEKingUnit.removeAllItems();
        cmbRACEKingUnit.addItem(noneSelected);
        cmbRACEFavoriteGovernment.removeAllItems();
        cmbRACEFavoriteGovernment.addItem(noneSelected);
        cmbRACEShunnedGovernment.removeAllItems();
        cmbRACEShunnedGovernment.addItem(noneSelected);
        cmbRACEAnimationEra.removeAllItems();
        //no "None" for era animations
        cmbRACEFreeTech1Index.removeAllItems();
        cmbRACEFreeTech1Index.addItem(noneSelected);
        cmbRACEFreeTech2Index.removeAllItems();
        cmbRACEFreeTech2Index.addItem(noneSelected);
        cmbRACEFreeTech3Index.removeAllItems();
        cmbRACEFreeTech3Index.addItem(noneSelected);
        cmbRACEFreeTech4Index.removeAllItems();
        cmbRACEFreeTech4Index.addItem(noneSelected);

        for (int i = 0; i < era.size(); i++)
        {
            utils.addWithPossibleDuplicates(era.get(i).getName(), mdlAnimationEra);
        }
        lstCivilizations.setModel(civilizationList);
        civilizationList.removeAllElements();
        for (int i = 0; i < civilization.size(); i++)
        {
            civilizationList.addElement(civilization.get(i).getName());
        }
        for (int i = 0; i < government.size(); i++)
        {
            //add government to combo lists that need government
            utils.addWithPossibleDuplicates(government.get(i).getName(), mdlFavoriteGovernment);
            utils.addWithPossibleDuplicates(government.get(i).getName(), mdlShunnedGovernment);
        }
        for (int i = 0; i < unit.size(); i++)
        {
            //add government to comfbo lists that need government
            utils.addWithPossibleDuplicates(unit.get(i).getName(), mdlKingUnit);
        }

        for (int i = 0; i < technology.size(); i++)
        {
            utils.addWithPossibleDuplicates(technology.get(i).getName(), mdlFreeTech1);
            utils.addWithPossibleDuplicates(technology.get(i).getName(), mdlFreeTech2);
            utils.addWithPossibleDuplicates(technology.get(i).getName(), mdlFreeTech3);
            utils.addWithPossibleDuplicates(technology.get(i).getName(), mdlFreeTech4);
        }

        if (true) {
            colorRenderer = new ComboBoxColorRenderer(colors);
            cmbRACEDefaultColor.setRenderer(colorRenderer);
            cmbRACEDefaultColor.setOpaque(true);
            cmbRACEUniqueColor.setRenderer(colorRenderer);
            cmbRACEUniqueColor.setOpaque(true);
        }
        
        if (utils.isInScenarioFolder("ntp00.pcx", "Art" + fileSlash + "units" + fileSlash + "palettes" + fileSlash, baselink))
        {
            cmdCivColors.setEnabled(true);
            hasCustomCivColors = true;
            cmdCivColors.setToolTipText("Customize civ colors");
        }
        else
        {
            cmdCivColors.setEnabled(true);
            hasCustomCivColors = false;
            cmdCivColors.setToolTipText("Enable editing of custom Civ colors.  This will copy the Firaxis colors for you to start editing.");
        }
    }

    /**
     * Constructor.  Initializes all the GUI elements and some variables.
     */
    public CIVTab(){
        lstType = lstCivilizations;
        //this = new JPanel();
        tabName = "RACE";
        textBoxes = new ArrayList<>();
        civilizationList = new SuperListModel();
        jScrollPane12 = new JScrollPane();
        jScrollPaneFlav = new JScrollPane();
        jLabel94 = new JLabel();
        txtRACECivilopediaEntry = new SuperJTextField();
        //txtRACECivilopediaEntry.setMandatory(true);
        jLabel95 = new JLabel();
        txtRACENoun = new SuperJTextField();
        jLabel96 = new JLabel();
        txtRACEAdjective = new SuperJTextField();
        rbtnRACEMasculine = new JRadioButton();
        rbtnRACEFeminine = new JRadioButton();
        rbtnRACENeuter = new JRadioButton();
        rbtnRACESingular = new JRadioButton();
        rbtnRACEPlural = new JRadioButton();
        jPanel33 = new JPanel();
        jLabel97 = new JLabel();
        txtRACELeaderTitle = new SuperJTextField();
        jLabel98 = new JLabel();
        txtRACELeaderName = new SuperJTextField();
        rbtnRACEMaleLeader = new JRadioButton();
        rbtnRACEFemaleLeader = new JRadioButton();
        jLabel99 = new JLabel();
        cmbRACEKingUnit = new JComboBox();
        jLabel100 = new JLabel();
        cmbRACECultureGroup = new JComboBox();
        pnlPersonality = new JPanel();
        jLabel101 = new JLabel();
        jLabel102 = new JLabel();
        cmbRACEFavoriteGovernment = new JComboBox();
        cmbRACEShunnedGovernment = new JComboBox();
        jLabel103 = new JLabel();
        sldRACEAggressionLevel = new JSlider();
        jPanel59 = new JPanel();
        chkRACEMilitaristic = new JCheckBox();
        chkRACEReligious = new JCheckBox();
        chkRACEExpansionist = new JCheckBox();
        chkRACECommercial = new JCheckBox();
        chkRACEIndustrious = new JCheckBox();
        chkRACEScientific = new JCheckBox();
        chkRACEAgricultural = new JCheckBox();
        chkRACESeafaring = new JCheckBox();
        jPanel60 = new JPanel();
        jLabel104 = new JLabel();
        jLabel105 = new JLabel();
        chkRACEManyOffensiveLandUnits = new JCheckBox();
        jLabel106 = new JLabel();
        jLabel107 = new JLabel();
        jLabel108 = new JLabel();
        jLabel109 = new JLabel();
        jLabel110 = new JLabel();
        jLabel111 = new JLabel();
        jLabel112 = new JLabel();
        jLabel113 = new JLabel();
        jLabel114 = new JLabel();
        jLabel115 = new JLabel();
        jLabel116 = new JLabel();
        jLabel117 = new JLabel();
        jLabel118 = new JLabel();
        jLabel119 = new JLabel();
        jLabel120 = new JLabel();
        chkRACEManyDefensiveLandUnits = new JCheckBox();
        chkRACEManyArtillery = new JCheckBox();
        chkRACEManySettlers = new JCheckBox();
        chkRACEManyWorkers = new JCheckBox();
        chkRACEManyShips = new JCheckBox();
        chkRACEManyAirUnits = new JCheckBox();
        chkRACEManyGrowth = new JCheckBox();
        chkRACEManyProduction = new JCheckBox();
        chkRACEManyHappiness = new JCheckBox();
        chkRACEManyScience = new JCheckBox();
        chkRACEManyWealth = new JCheckBox();
        chkRACEManyTrade = new JCheckBox();
        chkRACEManyExploration = new JCheckBox();
        chkRACEManyCulture = new JCheckBox();
        chkRACENoOffensiveLandUnits = new JCheckBox();
        chkRACENoDefensiveLandUnits = new JCheckBox();
        chkRACENoArtillery = new JCheckBox();
        chkRACENoSettlers = new JCheckBox();
        chkRACENoWorkers = new JCheckBox();
        chkRACENoShips = new JCheckBox();
        chkRACENoAirUnits = new JCheckBox();
        chkRACENoGrowth = new JCheckBox();
        chkRACENoProduction = new JCheckBox();
        chkRACENoHappiness = new JCheckBox();
        chkRACENoScience = new JCheckBox();
        chkRACENoWealth = new JCheckBox();
        chkRACENoTrade = new JCheckBox();
        chkRACENoExploration = new JCheckBox();
        chkRACENoCulture = new JCheckBox();
        jPanel62 = new JPanel();
        jLabel124 = new JLabel();
        cmbRACEAnimationEra = new JComboBox();
        jLabel125 = new JLabel();
        jLabel126 = new JLabel();
        txtRACEForwardFilename = new SuperJTextField();
        txtRACEBackwardFilename = new SuperJTextField();
        jPanel63 = new JPanel();
        cmbRACEFreeTech1Index = new JComboBox();
        cmbRACEFreeTech2Index = new JComboBox();
        cmbRACEFreeTech3Index = new JComboBox();
        cmbRACEFreeTech4Index = new JComboBox();
        jPanel64 = new JPanel();
        chkRACEManageCitizens = new JCheckBox();
        chkRACEManageProduction = new JCheckBox();
        chkRACENoWonders = new JCheckBox();
        chkRACENoSmallWonders = new JCheckBox();
        chkRACEEmphasizeFood = new JCheckBox();
        chkRACEEmphasizeShields = new JCheckBox();
        chkRACEEmphasizeTrade = new JCheckBox();
        jPanel65 = new JPanel();
        lblDiplomacyTextIndex = new JLabel();
        txtRACEDiplomacyTextIndex = new SuperJTextField();
        lblDefaultColor = new JLabel();
        cmbRACEDefaultColor = new JComboBox();
        lblUniqueColor = new JLabel();
        cmbRACEUniqueColor = new JComboBox();
        lblUnknown = new JLabel();
        txtRACEQuestionMark = new SuperJTextField();
        pnlCitiesAndLeaders = new JPanel();
        lblCities = new JLabel();
        scrCities = new JScrollPane();
        lstRACECities = new JList();
        txtNewCity = new SuperJTextField();
        btnAddMilitaryLeader = new JButton();
        lblMilitaryLeaders = new JLabel();
        txtMilitaryLeader = new SuperJTextField();
        btnAddCity = new JButton();
        scrMilitaryLeaders = new JScrollPane();
        lstMilitaryLeaders = new JList();
        lblScientificLeaders = new JLabel();
        txtScienceLeader = new SuperJTextField();
        btnAddScientificLeader = new JButton();
        scrScientificLeaders = new JScrollPane();
        lstScientificLeaders = new JList();
        lstFlavors = new JList();

        civilizationList = new SuperListModel();
        cityList = new DefaultListModel();
        militaryLeaderList = new DefaultListModel();
        scientificLeaderList = new DefaultListModel();
        lstRACECities.setModel(cityList);
        lstMilitaryLeaders.setModel(militaryLeaderList);
        lstScientificLeaders.setModel(scientificLeaderList);


        btnGroupCivilizationPlurality = new ButtonGroup();
        btnGroupCivilizationGender = new ButtonGroup();
        btnGroupLeaderGender = new ButtonGroup();

    }

    /**
     * Creates the GUI of the tab.  This includes button groups, layouts, etc.
     * Logically, this is to occur immediately after the constructor is called.
     *
     * @return - A reference to the CIVTab instance itself.  This reference
     * allows the tab to be added to a tab bar.
     */
    public JPanel createTab()
    {

        btnGroupCivilizationGender.add(rbtnRACEMasculine);
        btnGroupCivilizationGender.add(rbtnRACEFeminine);
        btnGroupCivilizationGender.add(rbtnRACENeuter);


        btnGroupCivilizationPlurality.add(rbtnRACESingular);
        btnGroupCivilizationPlurality.add(rbtnRACEPlural);

        btnGroupLeaderGender.add(rbtnRACEMaleLeader);
        btnGroupLeaderGender.add(rbtnRACEFemaleLeader);

        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lstCivilizations.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstCivilizations.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstCivilizationsValueChanged(evt);
            }
        });
        
        jScrollPane12.setViewportView(lstCivilizations);

        this.add(jScrollPane12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 160, 670));

        jLabel94.setText("Civilopedia Entry:");

        this.add(jLabel94, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 0, -1, -1));

        txtRACECivilopediaEntry.setText("                                        ");
        this.add(txtRACECivilopediaEntry, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 0, 110, -1));

        jLabel95.setText("Noun:");
        this.add(jLabel95, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 20, -1, -1));

        txtRACENoun.setText("                       ");
        this.add(txtRACENoun, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 20, 110, -1));

        jLabel96.setText("Adjective:");
        this.add(jLabel96, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 40, -1, -1));

        txtRACEAdjective.setText("                       ");
        this.add(txtRACEAdjective, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 40, 110, -1));

        rbtnRACEMasculine.setText("Masculine");
        this.add(rbtnRACEMasculine, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 0, -1, -1));

        rbtnRACEFeminine.setText("Feminine");
        this.add(rbtnRACEFeminine, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 20, -1, -1));

        rbtnRACENeuter.setText("Neuter");
        this.add(rbtnRACENeuter, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 40, -1, -1));

        rbtnRACESingular.setText("Singular");
        this.add(rbtnRACESingular, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 60, -1, -1));

        rbtnRACEPlural.setText("Plural");
        this.add(rbtnRACEPlural, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 80, -1, -1));

        jPanel33.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Leader"));
        jPanel33.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel97.setText("Title:");
        jPanel33.add(jLabel97, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));
        jPanel33.add(txtRACELeaderTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 90, -1));

        jLabel98.setText("Name:");
        jPanel33.add(jLabel98, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));
        jPanel33.add(txtRACELeaderName, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 90, -1));

        rbtnRACEMaleLeader.setText("Male");
        jPanel33.add(rbtnRACEMaleLeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, -1, -1));

        rbtnRACEFemaleLeader.setText("Female");
        jPanel33.add(rbtnRACEFemaleLeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, -1, -1));

        jLabel99.setText("Monarch unit:");
        jPanel33.add(jLabel99, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

        cmbRACEKingUnit.setModel(mdlKingUnit);
        jPanel33.add(cmbRACEKingUnit, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 80, 150, -1));

        this.add(jPanel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 110, 260, 120));

        jLabel100.setText("Culture Group:");
        this.add(jLabel100, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 60, -1, 30));

        cmbRACECultureGroup.setModel(new javax.swing.DefaultComboBoxModel(new String[] { noneSelected, "American", "European", "Mediterranean", "Mid East", "Asian" }));
        this.add(cmbRACECultureGroup, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 70, -1, 20));

        pnlPersonality.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Personality"));
        pnlPersonality.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel101.setText("Favorite Govt:");
        pnlPersonality.add(jLabel101, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jLabel102.setText("Shunned Govt:");
        pnlPersonality.add(jLabel102, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        cmbRACEFavoriteGovernment.setModel(mdlFavoriteGovernment);
        pnlPersonality.add(cmbRACEFavoriteGovernment, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 150, -1));

        cmbRACEShunnedGovernment.setModel(mdlShunnedGovernment);
        pnlPersonality.add(cmbRACEShunnedGovernment, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, 150, -1));

        jLabel103.setText("Aggression:");
        pnlPersonality.add(jLabel103, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 76, -1, 20));

        sldRACEAggressionLevel.setMajorTickSpacing(1);
        sldRACEAggressionLevel.setMaximum(2);
        sldRACEAggressionLevel.setMinimum(-2);
        sldRACEAggressionLevel.setPaintTicks(true);
        sldRACEAggressionLevel.setSnapToTicks(true);
        sldRACEAggressionLevel.setValue(0);
        pnlPersonality.add(sldRACEAggressionLevel, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 70, 160, -1));

        this.add(pnlPersonality, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 230, 260, 110));

        jPanel59.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Traits"));
        jPanel59.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        chkRACEMilitaristic.setText("Militaristic");
        jPanel59.add(chkRACEMilitaristic, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        chkRACEReligious.setText("Religious");
        jPanel59.add(chkRACEReligious, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        chkRACEExpansionist.setText("Expansionist");
        jPanel59.add(chkRACEExpansionist, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));

        chkRACECommercial.setText("Commercial");
        jPanel59.add(chkRACECommercial, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, -1, -1));

        chkRACEIndustrious.setText("Industrious");
        jPanel59.add(chkRACEIndustrious, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, -1, -1));

        chkRACEScientific.setText("Scientific");
        jPanel59.add(chkRACEScientific, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 60, -1, -1));

        chkRACEAgricultural.setText("Agricultural");
        jPanel59.add(chkRACEAgricultural, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

        chkRACESeafaring.setText("Seafaring");
        jPanel59.add(chkRACESeafaring, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, -1, -1));

        this.add(jPanel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 0, 220, 110));

        jPanel60.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Build Often/Never"));
        jPanel60.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel104.setText(" Never");
        jPanel60.add(jLabel104, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, -1, -1));

        jLabel105.setText("Often");
        jPanel60.add(jLabel105, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, -1, -1));
        jPanel60.add(chkRACEManyOffensiveLandUnits, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 40, -1, -1));

        jLabel106.setText("Offensive Land Units");
        jPanel60.add(jLabel106, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jLabel107.setText("Defensive Land Units");
        jPanel60.add(jLabel107, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));

        jLabel108.setText("Artillery Units");
        jPanel60.add(jLabel108, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

        jLabel109.setText("Settlers");
        jPanel60.add(jLabel109, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));

        jLabel110.setText("Workers");
        jPanel60.add(jLabel110, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, -1, -1));

        jLabel111.setText("Naval Units");
        jPanel60.add(jLabel111, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, -1));

        jLabel112.setText("Air Units");
        jPanel60.add(jLabel112, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, -1));

        jLabel113.setText("Growth");
        jPanel60.add(jLabel113, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, -1, -1));

        jLabel114.setText("Production");
        jPanel60.add(jLabel114, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, -1, -1));

        jLabel115.setText("Happiness");
        jPanel60.add(jLabel115, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, -1, -1));

        jLabel116.setText("Science");
        jPanel60.add(jLabel116, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, -1, -1));

        jLabel117.setText("Capitalization");
        jPanel60.add(jLabel117, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, -1, -1));

        jLabel118.setText("Trade");
        jPanel60.add(jLabel118, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, -1, -1));

        jLabel119.setText("Exploration");
        jPanel60.add(jLabel119, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, -1, -1));

        jLabel120.setText("Culture");
        jPanel60.add(jLabel120, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, -1, -1));
        jPanel60.add(chkRACEManyDefensiveLandUnits, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 60, 20, -1));
        jPanel60.add(chkRACEManyArtillery, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 80, -1, -1));
        jPanel60.add(chkRACEManySettlers, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 100, -1, -1));
        jPanel60.add(chkRACEManyWorkers, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 120, -1, -1));
        jPanel60.add(chkRACEManyShips, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 140, -1, -1));
        jPanel60.add(chkRACEManyAirUnits, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 160, -1, -1));
        jPanel60.add(chkRACEManyGrowth, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 180, -1, -1));
        jPanel60.add(chkRACEManyProduction, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 200, -1, -1));
        jPanel60.add(chkRACEManyHappiness, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 220, -1, -1));
        jPanel60.add(chkRACEManyScience, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 240, -1, -1));
        jPanel60.add(chkRACEManyWealth, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 260, -1, -1));
        jPanel60.add(chkRACEManyTrade, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 280, -1, -1));
        jPanel60.add(chkRACEManyExploration, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 300, -1, -1));
        jPanel60.add(chkRACEManyCulture, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 320, -1, -1));
        jPanel60.add(chkRACENoOffensiveLandUnits, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 40, -1, 20));
        jPanel60.add(chkRACENoDefensiveLandUnits, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 60, 20, 20));
        jPanel60.add(chkRACENoArtillery, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, -1, 20));
        jPanel60.add(chkRACENoSettlers, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, -1, 20));
        jPanel60.add(chkRACENoWorkers, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 120, -1, 20));
        jPanel60.add(chkRACENoShips, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 140, -1, 20));
        jPanel60.add(chkRACENoAirUnits, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 160, -1, 20));
        jPanel60.add(chkRACENoGrowth, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 180, -1, 20));
        jPanel60.add(chkRACENoProduction, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 200, -1, 20));
        jPanel60.add(chkRACENoHappiness, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 220, -1, 20));
        jPanel60.add(chkRACENoScience, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 240, -1, 20));
        jPanel60.add(chkRACENoWealth, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 260, -1, 20));
        jPanel60.add(chkRACENoTrade, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 280, -1, 20));
        jPanel60.add(chkRACENoExploration, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 300, -1, 20));
        jPanel60.add(chkRACENoCulture, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 320, -1, 20));

        this.add(jPanel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 0, 230, 350));

        jPanel62.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Animations"));

        jLabel124.setText("Era:");

        cmbRACEAnimationEra.setModel(mdlAnimationEra);
        cmbRACEAnimationEra.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbRACEAnimationEraChange(evt);
            }
        });

        jLabel125.setText("Fwd:");

        jLabel126.setText("Bwd:");

        org.jdesktop.layout.GroupLayout jPanel62Layout = new org.jdesktop.layout.GroupLayout(jPanel62);
        jPanel62.setLayout(jPanel62Layout);
        jPanel62Layout.setHorizontalGroup(
            jPanel62Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel62Layout.createSequentialGroup()
                .add(jPanel62Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel62Layout.createSequentialGroup()
                        .add(jLabel124)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(cmbRACEAnimationEra, 0, 213, Short.MAX_VALUE))
                    .add(jPanel62Layout.createSequentialGroup()
                        .add(jPanel62Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel125)
                            .add(jLabel126))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel62Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(txtRACEForwardFilename, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                            .add(txtRACEBackwardFilename, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE))))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel62Layout.setVerticalGroup(
            jPanel62Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel62Layout.createSequentialGroup()
                .add(jPanel62Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel124)
                    .add(cmbRACEAnimationEra, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel62Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel125)
                    .add(txtRACEForwardFilename, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel62Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel126)
                    .add(txtRACEBackwardFilename, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        this.add(jPanel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 340, -1, -1));

        jPanel63.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Free Technologies"));
        jPanel63.setPreferredSize(new java.awt.Dimension(178, 100));

        cmbRACEFreeTech1Index.setModel(mdlFreeTech1);

        cmbRACEFreeTech2Index.setModel(mdlFreeTech2);

        cmbRACEFreeTech3Index.setModel(mdlFreeTech3);

        cmbRACEFreeTech4Index.setModel(mdlFreeTech4);

        org.jdesktop.layout.GroupLayout jPanel63Layout = new org.jdesktop.layout.GroupLayout(jPanel63);
        jPanel63.setLayout(jPanel63Layout);
        jPanel63Layout.setHorizontalGroup(
            jPanel63Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel63Layout.createSequentialGroup()
                .add(jPanel63Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(cmbRACEFreeTech1Index, 0, 154, Short.MAX_VALUE)
                    .add(cmbRACEFreeTech2Index, 0, 154, Short.MAX_VALUE)
                    .add(cmbRACEFreeTech3Index, 0, 154, Short.MAX_VALUE)
                    .add(cmbRACEFreeTech4Index, 0, 154, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel63Layout.setVerticalGroup(
            jPanel63Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel63Layout.createSequentialGroup()
                .add(cmbRACEFreeTech1Index, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cmbRACEFreeTech2Index, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cmbRACEFreeTech3Index, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cmbRACEFreeTech4Index, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        this.add(jPanel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 440, -1, 150));
        
        cmdCivColors.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                cmdCivColorsAction();
            } 
       });
        
        //TODO: Flesh this out
        this.add(cmdCivColors, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 600, -1, 25));
        this.add(cmdSetUnitsAvailable, new AbsoluteConstraints(170, 600, -1, 25));
        
        
        cmdSetUnitsAvailable.addActionListener(e -> {
            
            int civIndex = this.civilizationIndex == -1 ? this.lstCivilizations.getSelectedIndex() : this.civilizationIndex;
            List<PRTO> availableUnits = new ArrayList<>();
            List<PRTO> unavailableUnits = new ArrayList<>();
            for (PRTO prto : baselink.unit) {
                if (prto.isAvailableTo(civIndex)) {
                    availableUnits.add(prto);
                }
                else {
                    unavailableUnits.add(prto);
                }
            }
            
            Platform.runLater(new Runnable() {
                public void run() {
                    try {
                        //When it's all ready, load up the rename window
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(Main.class.getResource("/fxml/CivilizationUnits.fxml"));
                        Parent root = fxmlLoader.load();

                        //We must load the FXML before we can access its controller
                        CivilizationUnitsController controller = fxmlLoader.getController();
                        controller.sendCivIdAndUnits(civIndex, availableUnits, unavailableUnits);

                        root.setVisible(true);
                        Stage stage = new Stage();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.setTitle("Select Available Units");
                        stage.getIcons().add(Main.javaFXImage);
                        stage.show();
                    }
                    catch(IOException ex) {
                        logger.error("Error loading FXML for rule changes", ex);
                    }
                }
            });
        });

        jPanel64.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Governor Settings"));

        chkRACEManageCitizens.setText("Manage Citizens");

        chkRACEManageProduction.setText("Manage Production");

        chkRACENoWonders.setText("No Wonders");

        chkRACENoSmallWonders.setText("No Small Wonders");

        chkRACEEmphasizeFood.setText("Emphasize Food");

        chkRACEEmphasizeShields.setText("Emphasize Production");

        chkRACEEmphasizeTrade.setText("Emphasize Trade");

        org.jdesktop.layout.GroupLayout jPanel64Layout = new org.jdesktop.layout.GroupLayout(jPanel64);
        jPanel64.setLayout(jPanel64Layout);
        jPanel64Layout.setHorizontalGroup(
            jPanel64Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel64Layout.createSequentialGroup()
                .add(jPanel64Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(chkRACEManageCitizens)
                    .add(chkRACEManageProduction)
                    .add(chkRACENoWonders)
                    .add(chkRACENoSmallWonders)
                    .add(chkRACEEmphasizeFood)
                    .add(chkRACEEmphasizeShields)
                    .add(chkRACEEmphasizeTrade))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        jPanel64Layout.setVerticalGroup(
            jPanel64Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel64Layout.createSequentialGroup()
                .add(chkRACEManageCitizens)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chkRACEManageProduction)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chkRACENoWonders)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chkRACENoSmallWonders)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chkRACEEmphasizeFood)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chkRACEEmphasizeShields)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chkRACEEmphasizeTrade))
        );

        this.add(jPanel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 340, -1, -1));

        jPanel65.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Flavors"));


        jPanel65.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jScrollPaneFlav.setViewportView(lstFlavors);
        jPanel65.add(jScrollPaneFlav, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 150, 200));

        this.add(jPanel65, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 350, 180, 240));

        //set up colours
        Integer[]intArray = new Integer[32];
        for (int i = 0; i < intArray.length; i++)
            intArray[i] = i;

        cmbRACEDefaultColor = new JComboBox(intArray);
        cmbRACEUniqueColor = new JComboBox(intArray);
        cmbRACEDefaultColor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbRACEDefaultColor.setBackground(colorRenderer.color[cmbRACEDefaultColor.getSelectedIndex()]);
            }}
        );
        cmbRACEUniqueColor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbRACEUniqueColor.setBackground(colorRenderer.color[cmbRACEUniqueColor.getSelectedIndex()]);
            }}
        );
        
        lblDiplomacyTextIndex.setText("Diplomacy Text Index");
        this.add(lblDefaultColor, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 565, -1, -1));

        txtRACEDiplomacyTextIndex.setText("                                       ");
        this.add(cmbRACEDefaultColor, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 565, 100, -1));
        
        lblDefaultColor.setText("Default Color:");
        this.add(lblDiplomacyTextIndex, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 540, -1, -1));

        //cmbRACEDefaultColor.setText("                     ");
        this.add(txtRACEDiplomacyTextIndex, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 540, 100, -1));

        lblUniqueColor.setText("Unique Color:");
        this.add(lblUniqueColor, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 540, -1, -1));

        //cmbRACEUniqueColor.setText("                     ");
        this.add(cmbRACEUniqueColor, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 540, 100, -1));
        //color boxes set up

        lblUnknown.setText("Unknown:");
        this.add(lblUnknown, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 565, -1, -1));

        txtRACEQuestionMark.setText("                     ");
        this.add(txtRACEQuestionMark, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 565, 100, -1));

        pnlCitiesAndLeaders.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder()));
        pnlCitiesAndLeaders.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblCities.setText("Cities");
        pnlCitiesAndLeaders.add(lblCities, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        
        lstRACECities.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        lstRACECities.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lstRACECitiesMouseAction(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lstRACECitiesMouseAction(evt);
            }
        });
        scrCities.setViewportView(lstRACECities);
        
        txtNewCity.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK), new AbstractAction(){
            public void actionPerformed(ActionEvent e) {
                Clipboard clipboard = getToolkit().getSystemClipboard();
                Transferable t = clipboard.getContents(null);
                try {
                    String text = (String)t.getTransferData(DataFlavor.stringFlavor);
                    if (text.contains("\n")) {
                        String[] cityNames = text.split("\n");
                        for (String city : cityNames) {
                            civilization.get(civilizationIndex).addCityName(city);
                            cityList.addElement(city);
                        }
                        txtNewCity.setText("");
                    }
                    else {
                        performRegularPaste(txtNewCity, text);
                    }
                }
                catch(UnsupportedFlavorException ex) {
                    if (logger.isTraceEnabled()) {
                        logger.trace("Attempted to paste non-text for city name");
                    }
                }
                catch(IOException ex) {
                    if (logger.isTraceEnabled()) {
                        logger.trace("Attempted to paste non-text for city name");
                    }
                }
            }
        });
        txtMilitaryLeader.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK), new AbstractAction(){
            public void actionPerformed(ActionEvent e) {
                Clipboard clipboard = getToolkit().getSystemClipboard();
                Transferable t = clipboard.getContents(null);
                try {
                    String text = (String)t.getTransferData(DataFlavor.stringFlavor);
                    if (text.contains("\n")) {
                        String[] leaderNames = text.split("\n");
                        for (String militaryLeader : leaderNames) {
                            civilization.get(civilizationIndex).addMilitaryLeader(militaryLeader);
                            militaryLeaderList.addElement(militaryLeader);
                        }
                        txtMilitaryLeader.setText("");
                    }
                    else {
                        performRegularPaste(txtMilitaryLeader, text);
                    }
                }
                catch(UnsupportedFlavorException ex) {
                    if (logger.isTraceEnabled()) {
                        logger.trace("Attempted to paste non-text for city name");
                    }
                }
                catch(IOException ex) {
                    if (logger.isTraceEnabled()) {
                        logger.trace("Attempted to paste non-text for city name");
                    }
                }
            }
        });
        txtScienceLeader.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK), new AbstractAction(){
            public void actionPerformed(ActionEvent e) {
                Clipboard clipboard = getToolkit().getSystemClipboard();
                Transferable t = clipboard.getContents(null);
                try {
                    String text = (String)t.getTransferData(DataFlavor.stringFlavor);
                    if (text.contains("\n")) {
                        String[] leaderNames = text.split("\n");
                        for (String scienceLeader : leaderNames) {
                            civilization.get(civilizationIndex).addScientificLeader(scienceLeader);
                            scientificLeaderList.addElement(scienceLeader);
                        }
                        txtScienceLeader.setText("");
                    }
                    else {
                        performRegularPaste(txtScienceLeader, text);
                    }
                }
                catch(UnsupportedFlavorException ex) {
                    if (logger.isTraceEnabled()) {
                        logger.trace("Attempted to paste non-text for city name");
                    }
                }
                catch(IOException ex) {
                    if (logger.isTraceEnabled()) {
                        logger.trace("Attempted to paste non-text for city name");
                    }
                }
            }
        });
        pnlCitiesAndLeaders.add(scrCities, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 150, 160));
        pnlCitiesAndLeaders.add(txtNewCity, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 100, -1));
        btnAddMilitaryLeader.setText("+");
        btnAddMilitaryLeader.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                if (logger.isInfoEnabled())
                    logger.info("To add leader: " + txtMilitaryLeader.getText());
                civilization.get(civilizationIndex).addMilitaryLeader(txtMilitaryLeader.getText());
                militaryLeaderList.addElement(txtMilitaryLeader.getText());
                txtMilitaryLeader.setText("");
            }
        });
        pnlCitiesAndLeaders.add(btnAddMilitaryLeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 30, 50, 20));

        lblMilitaryLeaders.setText("Military Leaders");
        pnlCitiesAndLeaders.add(lblMilitaryLeaders, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 10, -1, -1));

        txtMilitaryLeader.setText("");
        pnlCitiesAndLeaders.add(txtMilitaryLeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 30, 80, -1));

        btnAddCity.setText("+");
        btnAddCity.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                if (logger.isInfoEnabled())
                    logger.info("To add city: " + txtNewCity.getText());
                civilization.get(civilizationIndex).addCityName(txtNewCity.getText());
                cityList.addElement(txtNewCity.getText());
                txtNewCity.setText("");
            }
        });//wwww
        pnlCitiesAndLeaders.add(btnAddCity, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 30, 50, 20));

        
        scrMilitaryLeaders.setViewportView(lstMilitaryLeaders);

        pnlCitiesAndLeaders.add(scrMilitaryLeaders, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 60, 130, 60));

        lblScientificLeaders.setText("Scientific Leaders");
        pnlCitiesAndLeaders.add(lblScientificLeaders, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 130, -1, -1));

        txtScienceLeader.setText("");
        pnlCitiesAndLeaders.add(txtScienceLeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 150, 80, -1));

        btnAddScientificLeader.setText("+");
        btnAddScientificLeader.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                if (logger.isInfoEnabled())
                    logger.info("To add leader: " + txtScienceLeader.getText());
                civilization.get(civilizationIndex).addScientificLeader(txtScienceLeader.getText());
                scientificLeaderList.addElement(txtScienceLeader.getText());
                txtScienceLeader.setText("");
            }
        });
        pnlCitiesAndLeaders.add(btnAddScientificLeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 150, 50, 20));

        scrScientificLeaders.setViewportView(lstScientificLeaders);

        pnlCitiesAndLeaders.add(scrScientificLeaders, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 180, 130, 40));

        this.add(pnlCitiesAndLeaders, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 110, 310, 230));
        
        lstMilitaryLeaders.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        lstMilitaryLeaders.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lstRACEMilitaryLeadersMouseAction(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lstRACEMilitaryLeadersMouseAction(evt);
            }
        });

        lstScientificLeaders.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        lstScientificLeaders.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lstRACEScientificLeadersMouseAction(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lstRACEScientificLeadersMouseAction(evt);
            }
        });

        //"Delete" menu item
        delete = new JMenuItem("Delete");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //deleteItem(evt);
                //additional stuff necessary
                int[] selectedIndices = listToDeleteFrom.getSelectedIndices();
                if (listToDeleteFrom.equals(lstRACECities)) {
                    for (int i = selectedIndices.length - 1; i >= 0; i--) {
                        civilization.get(civilizationIndex).removeCityName(selectedIndices[i]);
                    }
                }
                if (listToDeleteFrom.equals(lstMilitaryLeaders)) {
                    for (int i = selectedIndices.length - 1; i >= 0; i--) {
                        civilization.get(civilizationIndex).removeMilitaryLeader(selectedIndices[i]);
                    }
                }
                if (listToDeleteFrom.equals(lstScientificLeaders)) {
                    for (int i = selectedIndices.length - 1; i >= 0; i--) {
                        civilization.get(civilizationIndex).removeScientificLeader(selectedIndices[i]);
                    }
                }
                if (!(listToDeleteFrom.equals(lstCivilizations))) {   //remove element from the model
                    for (int i = selectedIndices.length - 1; i >= 0; i--) {
                        listModelToDeleteFrom.removeElementAt(selectedIndices[i]);
                    }
                }
                else    //removing a civilization from the game - this case should't happen anymore
                {
                    logger.warn("Unexpected case - deleting civ");
                    deleteAction();
                }
                if (logger.isInfoEnabled())
                    logger.info("Delete event performed");
            }
        });
        //Add stuff now entirely encompassed by overriden addItem method
//txtRACENewCity.setText("5");txtRACENewMilitaryLeader.setText("6");txtRACENewScienceLeader.setText("7");txtRACEForwardFilename.setText("8");txtRACEBackwardFilename.setText("9");

        this.setName("RACE");

        tabCreated = true;
        return this;
    }
    
    private void performRegularPaste(JTextField textArea, String text) {
        int dot = textArea.getCaret().getDot();
        int mark = textArea.getCaret().getMark();
        String newText = "";
        if (textArea.getCaret().getDot() != textArea.getCaret().getMark()) {
            if (textArea.getCaret().getDot() > textArea.getCaret().getMark()) {
                newText = textArea.getText().substring(0, mark) + text + textArea.getText().substring(dot);
            }
            else {
                newText = textArea.getText().substring(0, dot) + text + textArea.getText().substring(mark);
            }
        }
        else {
            newText = textArea.getText().substring(0, dot) + text + textArea.getText().substring(dot);
        }
        textArea.setText(newText);
        
    }

    @Override
    public boolean addItem(String name)
    {
        if (civilization.size() >= 32) {
            int option = JOptionPane.showConfirmDialog(null, 
                    "You already have 32 civs.  Adding more will make the BIQ unplayable in Civ III.  Continue?", 
                    "Too many civs!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (option == JOptionPane.NO_OPTION) {
                return false;
            }
        }
        
        civilization.add(new RACE(name, lstFlavors.getModel().getSize(), baselink));
        civilization.get(civilization.size() - 1).setUniqueCivilizationCounter(civilization.size() - 1);
        //civilizationList.addElement(name);  //the list is smart now and does this itself
        //update other tabs that need to know about changes - PRTO and GAME
        //PRTO tab doesn't need anything reset
        unitTab.civilizationList.addElement(name);
        //update alliance info
        gameTab.prop.get(0).numberOfPlayableCivs++;
        gameTab.prop.get(0).idOfPlayableCivs.add(civilization.size() - 1);
        gameTab.prop.get(0).civPartOfWhichAlliance.add(0);
        gameTab.prop.get(0).dataLength+=8;
        gameTab.alliance0Model.addElement(name);

        if (logger.isInfoEnabled())
            logger.info("Civilization " + name + " added");
        return true;

    }

    private void lstRACEMilitaryLeadersMouseAction(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            int index = lstMilitaryLeaders.getSelectedIndex();
            if (index != -1) {  //should never happen in theory, just in case
                String leaderName = this.militaryLeaderList.get(index).toString();
                String newName = JOptionPane.showInputDialog(Main.mainMain, "Enter the new name for " + leaderName, "Rename military leader", JOptionPane.QUESTION_MESSAGE);
                if (newName != null && !newName.equals("")) {
                    militaryLeaderList.set(index, newName);
                    civilization.get(civilizationIndex).renameMilitaryLeader(index, newName);
                }
            }
            return;
        }
        if (evt.isPopupTrigger()) {
            JPopupMenu popUp = new JPopupMenu();
            popUp.add(delete);
            Component component = evt.getComponent();
            listModelToDeleteFrom = militaryLeaderList;
            listToDeleteFrom = lstMilitaryLeaders;
            int x = evt.getX();
            int y = evt.getY();
            popUp.show(component, x, y);
        }
    }
    
    private void cmbRACEAnimationEraChange(java.awt.event.ItemEvent evt)
    {
        updateEraSelection();
    }

    /**
     * Method that handles business logic when the selected era is changed.
     * It is subdivided so that when a different civilization is selected,
     * the data can be saved with the old civilizationIndex and loaded with the
     * new one.
     */
    private void updateEraSelection()
    {
        saveEraData();
        loadEraData();
    }
    
    private void loadEraData()
    {
        if (logger.isDebugEnabled())
            logger.debug("beginning load of era data");
        eraSelected = cmbRACEAnimationEra.getSelectedIndex();
        if (eraSelected != -1 && civilizationIndex != -1)
        {
            txtRACEForwardFilename.setText(civilization.get(civilizationIndex).getForwardFilename(eraSelected));
            txtRACEBackwardFilename.setText(civilization.get(civilizationIndex).getReverseFilename(eraSelected));
        }
        if (logger.isDebugEnabled())
            logger.debug("loading era data successful");
    }

    private void saveEraData()
    {
        if (logger.isDebugEnabled())
            logger.debug("beginning save of era data");
        if (eraSelected != -1 && civilizationIndex != -1)
        {
            civilization.get(civilizationIndex).setForwardFilename(eraSelected, txtRACEForwardFilename.getText());
            civilization.get(civilizationIndex).setReverseFilename(eraSelected, txtRACEBackwardFilename.getText());
        }
        if (logger.isDebugEnabled())
            logger.debug("saving era data successful");
    }

    private void lstRACEScientificLeadersMouseAction(java.awt.event.MouseEvent evt) {
        // :
        if (evt.getClickCount() == 2) {
            int index = lstScientificLeaders.getSelectedIndex();
            if (index != -1) {  //should never happen in theory, just in case
                String leaderName = this.scientificLeaderList.get(index).toString();
                String newName = JOptionPane.showInputDialog(Main.mainMain, "Enter the new name for " + leaderName, "Rename scientific leader", JOptionPane.QUESTION_MESSAGE);
                if (newName != null && !newName.equals("")) {
                    scientificLeaderList.set(index, newName);
                    civilization.get(civilizationIndex).renameScientificLeader(index, newName);
                }
            }
            return;
        }
        if (evt.isPopupTrigger()) {
            JPopupMenu popUp = new JPopupMenu();
            popUp.add(delete);
            Component component = evt.getComponent();
            listModelToDeleteFrom = scientificLeaderList;
            listToDeleteFrom = lstScientificLeaders;
            int x = evt.getX();
            int y = evt.getY();
            popUp.show(component, x, y);
        }
    }

    private void lstRACECitiesMouseAction(java.awt.event.MouseEvent evt) {
        // :
        if (logger.isDebugEnabled())
            logger.debug("Mouse action on city list");
        if (evt.getClickCount() == 2) {
            int index = lstRACECities.getSelectedIndex();
            if (index != -1) {  //should never happen in theory, just in case
                String cityName = this.cityList.get(index).toString();
                String newName = JOptionPane.showInputDialog(Main.mainMain, "Enter the new name for " + cityName, "Rename city", JOptionPane.QUESTION_MESSAGE);
                if (newName != null && !newName.equals("")) {
                    cityList.set(index, newName);
                    civilization.get(civilizationIndex).renameCity(index, newName);
                }
            }
            return;
        }
        if (evt.isPopupTrigger()) {
            JPopupMenu popUp = new JPopupMenu();
            popUp.add(delete);
            Component component = evt.getComponent();
            listModelToDeleteFrom = cityList;
            listToDeleteFrom = lstRACECities;
            int x = evt.getX();
            int y = evt.getY();
            popUp.show(component, x, y);
        }
    }

    private void lstCivilizationsValueChanged(javax.swing.event.ListSelectionEvent evt) {
        // :
        updateTab();
    }

    /**
     * Method that stores all tab data to memory (in case any fields have changed
     * on-screen), and then updates the screen based on the new civilization
     * selected.  As this method may be invoked manually (for example, prior
     * to the file being saved to disk), it is possible that the new civilization
     * selected will be the same one as the one previously selected.
     */
    public void updateTab()
    {
        storeData();
        civilizationIndex = lstCivilizations.getSelectedIndex();
        civilizationIndex = civilizationList.getTrueIndex(civilizationIndex);
        displayData();
    }

    /**
     * Stores the on-screen values of fields to memory.  This method is to be
     * called whenever the BIQ-in-memory must be updated (i.e., when the user
     * selects a different civilization or saves the file).
     * It currently is called directly through updateTab, although allowing
     * Main to call this method directly before a file save may increase
     * efficiency slightly.
     */
    private void storeData()
    {
        if (!(civilizationIndex == -1)) {
            civilization.get(civilizationIndex).setLeaderName(txtRACELeaderName.getText());
            civilization.get(civilizationIndex).setLeaderTitle(txtRACELeaderTitle.getText());
            civilization.get(civilizationIndex).setCivilopediaEntry(txtRACECivilopediaEntry.getText());
            civilization.get(civilizationIndex).setAdjective(txtRACEAdjective.getText());
            civilization.get(civilizationIndex).setNoun(txtRACENoun.getText());
            civilization.get(civilizationIndex).setCultureGroup(cmbRACECultureGroup.getSelectedIndex() - 1);
            if (rbtnRACEFemaleLeader.isSelected()) {
                civilization.get(civilizationIndex).setLeaderGender(1);
            } else {
                civilization.get(civilizationIndex).setLeaderGender(0);
            }
            if (rbtnRACEMasculine.isSelected()) {
                civilization.get(civilizationIndex).setCivilizationGender(0);
            } else if (rbtnRACEFeminine.isSelected()) {
                civilization.get(civilizationIndex).setCivilizationGender(1);
            } else if (rbtnRACENeuter.isSelected()) {
                civilization.get(civilizationIndex).setCivilizationGender(2);
            }
            civilization.get(civilizationIndex).setAggressionLevel(sldRACEAggressionLevel.getValue());
            civilization.get(civilizationIndex).setShunnedGovernment(cmbRACEShunnedGovernment.getSelectedIndex() - 1);
            civilization.get(civilizationIndex).setFavoriteGovernment(cmbRACEFavoriteGovernment.getSelectedIndex() - 1);
            if (true) {
                civilization.get(civilizationIndex).setDefaultColor(cmbRACEDefaultColor.getSelectedIndex());
                civilization.get(civilizationIndex).setUniqueColor(cmbRACEUniqueColor.getSelectedIndex());
            }
            civilization.get(civilizationIndex).setFreeTech1Index(cmbRACEFreeTech1Index.getSelectedIndex() - 1);
            civilization.get(civilizationIndex).setFreeTech2Index(cmbRACEFreeTech2Index.getSelectedIndex() - 1);
            civilization.get(civilizationIndex).setFreeTech3Index(cmbRACEFreeTech3Index.getSelectedIndex() - 1);
            civilization.get(civilizationIndex).setFreeTech4Index(cmbRACEFreeTech4Index.getSelectedIndex() - 1);
            if (rbtnRACESingular.isSelected()) {
                civilization.get(civilizationIndex).setPlurality(0);
            } else if (rbtnRACEPlural.isSelected()) {
                civilization.get(civilizationIndex).setPlurality(1);
            }
            civilization.get(civilizationIndex).setKingUnit(cmbRACEKingUnit.getSelectedIndex() - 1);
            civilization.get(civilizationIndex).setQuestionMark(txtRACEQuestionMark.getInteger());
            civilization.get(civilizationIndex).setDiplomacyTextIndex(txtRACEDiplomacyTextIndex.getInteger());
            civilization.get(civilizationIndex).setMilitaristic(chkRACEMilitaristic.isSelected());
            civilization.get(civilizationIndex).setCommercial(chkRACECommercial.isSelected());
            civilization.get(civilizationIndex).setExpansionist(chkRACEExpansionist.isSelected());
            civilization.get(civilizationIndex).setScientific(chkRACEScientific.isSelected());
            civilization.get(civilizationIndex).setReligious(chkRACEReligious.isSelected());
            civilization.get(civilizationIndex).setIndustrious(chkRACEIndustrious.isSelected());
            civilization.get(civilizationIndex).setAgricultural(chkRACEAgricultural.isSelected());
            civilization.get(civilizationIndex).setSeafaring(chkRACESeafaring.isSelected());
            civilization.get(civilizationIndex).setManageCitizens(chkRACEManageCitizens.isSelected());
            civilization.get(civilizationIndex).setEmphasizeFood(chkRACEEmphasizeFood.isSelected());
            civilization.get(civilizationIndex).setEmphasizeShields(chkRACEEmphasizeShields.isSelected());
            civilization.get(civilizationIndex).setEmphasizeTrade(chkRACEEmphasizeTrade.isSelected());
            civilization.get(civilizationIndex).setManageProduction(chkRACEManageProduction.isSelected());
            civilization.get(civilizationIndex).setNoWonders(chkRACENoWonders.isSelected());
            civilization.get(civilizationIndex).setNoSmallWonders(chkRACENoSmallWonders.isSelected());
            civilization.get(civilizationIndex).setNoOffensiveLandUnits(chkRACENoOffensiveLandUnits.isSelected());
            civilization.get(civilizationIndex).setNoDefensiveLandUnits(chkRACENoDefensiveLandUnits.isSelected());
            civilization.get(civilizationIndex).setNoArtillery(chkRACENoArtillery.isSelected());
            civilization.get(civilizationIndex).setNoSettlers(chkRACENoSettlers.isSelected());
            civilization.get(civilizationIndex).setNoWorkers(chkRACENoWorkers.isSelected());
            civilization.get(civilizationIndex).setNoShips(chkRACENoShips.isSelected());
            civilization.get(civilizationIndex).setNoAirUnits(chkRACENoAirUnits.isSelected());
            civilization.get(civilizationIndex).setNoGrowth(chkRACENoGrowth.isSelected());
            civilization.get(civilizationIndex).setNoProduction(chkRACENoProduction.isSelected());
            civilization.get(civilizationIndex).setNoHappiness(chkRACENoHappiness.isSelected());
            civilization.get(civilizationIndex).setNoScience(chkRACENoScience.isSelected());
            civilization.get(civilizationIndex).setNoWealth(chkRACENoWealth.isSelected());
            civilization.get(civilizationIndex).setNoTrade(chkRACENoTrade.isSelected());
            civilization.get(civilizationIndex).setNoExploration(chkRACENoExploration.isSelected());
            civilization.get(civilizationIndex).setNoCulture(chkRACENoCulture.isSelected());
            civilization.get(civilizationIndex).setManyOffensiveLandUnits(chkRACEManyOffensiveLandUnits.isSelected());
            civilization.get(civilizationIndex).setManyDefensiveLandUnits(chkRACEManyDefensiveLandUnits.isSelected());
            civilization.get(civilizationIndex).setManyArtillery(chkRACEManyArtillery.isSelected());
            civilization.get(civilizationIndex).setManySettlers(chkRACEManySettlers.isSelected());
            civilization.get(civilizationIndex).setManyWorkers(chkRACEManyWorkers.isSelected());
            civilization.get(civilizationIndex).setManyShips(chkRACEManyShips.isSelected());
            civilization.get(civilizationIndex).setManyAirUnits(chkRACEManyAirUnits.isSelected());
            civilization.get(civilizationIndex).setManyGrowth(chkRACEManyGrowth.isSelected());
            civilization.get(civilizationIndex).setManyProduction(chkRACEManyProduction.isSelected());
            civilization.get(civilizationIndex).setManyHappiness(chkRACEManyHappiness.isSelected());
            civilization.get(civilizationIndex).setManyScience(chkRACEManyScience.isSelected());
            civilization.get(civilizationIndex).setManyWealth(chkRACEManyWealth.isSelected());
            civilization.get(civilizationIndex).setManyTrade(chkRACEManyTrade.isSelected());
            civilization.get(civilizationIndex).setManyExploration(chkRACEManyExploration.isSelected());
            civilization.get(civilizationIndex).setManyCulture(chkRACEManyCulture.isSelected());
            
            //save flavour data
            int[]selectedFlavs = lstFlavors.getSelectedIndices();
            civilization.get(civilizationIndex).resetFlavours();
            for (int i = 0; i < selectedFlavs.length; i++)
                civilization.get(civilizationIndex).setFlavour(selectedFlavs[i], true);
            //City/leader names are saved as they are modified.  Not necessary to update them here
            saveEraData();
        }
    }

    /**
     * Displays data from memory onto the screen.  This method is to be invoked
     * whenever the selected civilization is changed (indirectly, through
     * updateTab()).
     */
    private void displayData()
    {
        if (!(civilizationIndex == -1))
        {
            txtRACELeaderName.setText(civilization.get(civilizationIndex).getLeaderName());
            txtRACELeaderTitle.setText(civilization.get(civilizationIndex).getLeaderTitle());
            txtRACECivilopediaEntry.setText(civilization.get(civilizationIndex).getCivilopediaEntry());
            txtRACEAdjective.setText(civilization.get(civilizationIndex).getAdjective());
            txtRACENoun.setText(civilization.get(civilizationIndex).getNoun());
            cmbRACECultureGroup.setSelectedIndex(civilization.get(civilizationIndex).getCultureGroup() + 1);
            if (civilization.get(civilizationIndex).getLeaderGender() == 1) {
                rbtnRACEFemaleLeader.setSelected(true);
                rbtnRACEMaleLeader.setSelected(false);
            } else if (civilization.get(civilizationIndex).getLeaderGender() == 0) {
                rbtnRACEFemaleLeader.setSelected(false);
                rbtnRACEMaleLeader.setSelected(true);
            }
            if (civilization.get(civilizationIndex).getCivilizationGender() == 0) {
                rbtnRACEMasculine.setSelected(true);
                rbtnRACEFeminine.setSelected(false);
                rbtnRACENeuter.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getCivilizationGender() == 1) {
                rbtnRACEMasculine.setSelected(false);
                rbtnRACEFeminine.setSelected(true);
                rbtnRACENeuter.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getCivilizationGender() == 2) {
                rbtnRACEMasculine.setSelected(false);
                rbtnRACEFeminine.setSelected(false);
                rbtnRACENeuter.setSelected(true);
            }

            sldRACEAggressionLevel.setValue(civilization.get(civilizationIndex).getAggressionLevel());
            cmbRACEShunnedGovernment.setSelectedIndex(civilization.get(civilizationIndex).getShunnedGovernment() + 1);
            cmbRACEFavoriteGovernment.setSelectedIndex(civilization.get(civilizationIndex).getFavoriteGovernment() + 1);
            if (true) {
                cmbRACEDefaultColor.setSelectedIndex(civilization.get(civilizationIndex).getDefaultColor());
                cmbRACEUniqueColor.setSelectedIndex(civilization.get(civilizationIndex).getUniqueColor());
            }
            cmbRACEFreeTech1Index.setSelectedIndex(civilization.get(civilizationIndex).getFreeTech1Index() + 1);
            cmbRACEFreeTech2Index.setSelectedIndex(civilization.get(civilizationIndex).getFreeTech2Index() + 1);
            cmbRACEFreeTech3Index.setSelectedIndex(civilization.get(civilizationIndex).getFreeTech3Index() + 1);
            cmbRACEFreeTech4Index.setSelectedIndex(civilization.get(civilizationIndex).getFreeTech4Index() + 1);
            if (civilization.get(civilizationIndex).getPlurality() == 1) {
                rbtnRACESingular.setSelected(false);
                rbtnRACEPlural.setSelected(true);
            }
            if (civilization.get(civilizationIndex).getPlurality() == 0) {
                rbtnRACEPlural.setSelected(false);
                rbtnRACESingular.setSelected(true);
            }
            cmbRACEKingUnit.setSelectedIndex(civilization.get(civilizationIndex).getKingUnit() + 1);
            txtRACEQuestionMark.setText(Integer.toString(civilization.get(civilizationIndex).getQuestionMark()));
            txtRACEDiplomacyTextIndex.setText(Integer.toString(civilization.get(civilizationIndex).getDiplomacyTextIndex()));
            if (civilization.get(civilizationIndex).getMilitaristic()) {
                chkRACEMilitaristic.setSelected(true);
            } else {
                chkRACEMilitaristic.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getCommercial()) {
                chkRACECommercial.setSelected(true);
            } else {
                chkRACECommercial.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getExpansionist()) {
                chkRACEExpansionist.setSelected(true);
            } else {
                chkRACEExpansionist.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getScientific()) {
                chkRACEScientific.setSelected(true);
            } else {
                chkRACEScientific.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getReligious()) {
                chkRACEReligious.setSelected(true);
            } else {
                chkRACEReligious.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getIndustrious()) {
                chkRACEIndustrious.setSelected(true);
            } else {
                chkRACEIndustrious.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getAgricultural()) {
                chkRACEAgricultural.setSelected(true);
            } else {
                chkRACEAgricultural.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getSeafaring()) {
                chkRACESeafaring.setSelected(true);
            } else {
                chkRACESeafaring.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getManageCitizens()) {
                chkRACEManageCitizens.setSelected(true);
            } else {
                chkRACEManageCitizens.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getEmphasizeFood()) {
                chkRACEEmphasizeFood.setSelected(true);
            } else {
                chkRACEEmphasizeFood.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getEmphasizeShields()) {
                chkRACEEmphasizeShields.setSelected(true);
            } else {
                chkRACEEmphasizeShields.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getEmphasizeTrade()) {
                chkRACEEmphasizeTrade.setSelected(true);
            } else {
                chkRACEEmphasizeTrade.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getManageProduction()) {
                chkRACEManageProduction.setSelected(true);
            } else {
                chkRACEManageProduction.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getNoWonders()) {
                chkRACENoWonders.setSelected(true);
            } else {
                chkRACENoWonders.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getNoSmallWonders()) {
                chkRACENoSmallWonders.setSelected(true);
            } else {
                chkRACENoSmallWonders.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getNoOffensiveLandUnits()) {
                chkRACENoOffensiveLandUnits.setSelected(true);
            } else {
                chkRACENoOffensiveLandUnits.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getNoDefensiveLandUnits()) {
                chkRACENoDefensiveLandUnits.setSelected(true);
            } else {
                chkRACENoDefensiveLandUnits.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getNoArtillery()) {
                chkRACENoArtillery.setSelected(true);
            } else {
                chkRACENoArtillery.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getNoSettlers()) {
                chkRACENoSettlers.setSelected(true);
            } else {
                chkRACENoSettlers.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getNoWorkers()) {
                chkRACENoWorkers.setSelected(true);
            } else {
                chkRACENoWorkers.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getNoShips()) {
                chkRACENoShips.setSelected(true);
            } else {
                chkRACENoShips.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getNoAirUnits()) {
                chkRACENoAirUnits.setSelected(true);
            } else {
                chkRACENoAirUnits.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getNoGrowth()) {
                chkRACENoGrowth.setSelected(true);
            } else {
                chkRACENoGrowth.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getNoProduction()) {
                chkRACENoProduction.setSelected(true);
            } else {
                chkRACENoProduction.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getNoHappiness()) {
                chkRACENoHappiness.setSelected(true);
            } else {
                chkRACENoHappiness.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getNoScience()) {
                chkRACENoScience.setSelected(true);
            } else {
                chkRACENoScience.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getNoWealth()) {
                chkRACENoWealth.setSelected(true);
            } else {
                chkRACENoWealth.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getNoTrade()) {
                chkRACENoTrade.setSelected(true);
            } else {
                chkRACENoTrade.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getNoExploration()) {
                chkRACENoExploration.setSelected(true);
            } else {
                chkRACENoExploration.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getNoCulture()) {
                chkRACENoCulture.setSelected(true);
            } else {
                chkRACENoCulture.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getManyOffensiveLandUnits()) {
                chkRACEManyOffensiveLandUnits.setSelected(true);
            } else {
                chkRACEManyOffensiveLandUnits.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getManyDefensiveLandUnits()) {
                chkRACEManyDefensiveLandUnits.setSelected(true);
            } else {
                chkRACEManyDefensiveLandUnits.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getManyArtillery()) {
                chkRACEManyArtillery.setSelected(true);
            } else {
                chkRACEManyArtillery.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getManySettlers()) {
                chkRACEManySettlers.setSelected(true);
            } else {
                chkRACEManySettlers.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getManyWorkers()) {
                chkRACEManyWorkers.setSelected(true);
            } else {
                chkRACEManyWorkers.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getManyShips()) {
                chkRACEManyShips.setSelected(true);
            } else {
                chkRACEManyShips.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getManyAirUnits()) {
                chkRACEManyAirUnits.setSelected(true);
            } else {
                chkRACEManyAirUnits.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getManyGrowth()) {
                chkRACEManyGrowth.setSelected(true);
            } else {
                chkRACEManyGrowth.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getManyProduction()) {
                chkRACEManyProduction.setSelected(true);
            } else {
                chkRACEManyProduction.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getManyHappiness()) {
                chkRACEManyHappiness.setSelected(true);
            } else {
                chkRACEManyHappiness.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getManyScience()) {
                chkRACEManyScience.setSelected(true);
            } else {
                chkRACEManyScience.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getManyWealth()) {
                chkRACEManyWealth.setSelected(true);
            } else {
                chkRACEManyWealth.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getManyTrade()) {
                chkRACEManyTrade.setSelected(true);
            } else {
                chkRACEManyTrade.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getManyExploration()) {
                chkRACEManyExploration.setSelected(true);
            } else {
                chkRACEManyExploration.setSelected(false);
            }
            if (civilization.get(civilizationIndex).getManyCulture()) {
                chkRACEManyCulture.setSelected(true);
            } else {
                chkRACEManyCulture.setSelected(false);
            }

            //cler city/leader/sci leader lists before adding new stuff, or all sorts of things will go haywire
            cityList.clear();
            militaryLeaderList.clear();
            scientificLeaderList.clear();
            List<String>cityNames = civilization.get(civilizationIndex).getCityNames();
            for (String cityName : cityNames) {
                cityList.addElement(cityName);
            }
            List<String>militaryLeaders = civilization.get(civilizationIndex).getMilitaryLeaders();
            for (String militaryLeader : militaryLeaders) {
                militaryLeaderList.addElement(militaryLeader);
            }
            List<String>scientificLeaders = civilization.get(civilizationIndex).getScientificLeaders();
            for (String scientificLeader : scientificLeaders) {
                scientificLeaderList.addElement(scientificLeader);
            }
            //display flavour data
            char numTrue = 0;
            for (int i = 0; i < civilization.get(civilizationIndex).getNumFlavours(); i++)
                if (civilization.get(civilizationIndex).getFlavour(i))
                    numTrue++;
            int[]selInd = new int[numTrue];
            int index = 0;
            for (int i = 0; i < civilization.get(civilizationIndex).getNumFlavours(); i++)
                if (civilization.get(civilizationIndex).getFlavour(i))
                {
                    selInd[index] = i;
                    index++;
                }
            lstFlavors.setSelectedIndices(selInd);


            loadEraData();
        }
    }

    //wwwww
    /**
     * Removes all limits on length and values for text fields.
     */
    public void setNoLimits()
    {
        clearBadValueDocumentListeners();
        clearLengthDocumentListeners();
        if (logger.isDebugEnabled())
            logger.debug("SETTING NO LIMITS");
        //don't put in any document listeners, accept any value
    }
    /**
     * Sets 'minimal' limits for text fields - in practices, only length limits.
     */
    public void setMinimalLimits()
    {
        clearBadValueDocumentListeners();
        clearLengthDocumentListeners();
        if (logger.isDebugEnabled())
            logger.debug("SETTING MINIMAL LIMITS");
        //as this only watches for overfuls 'n stuff, don't set any value-based limits
        //do set document listeners on the char array fields (civilopedia and the like)
        addLengthDocumentListener(23, txtNewCity);
        addLengthDocumentListener(31, txtMilitaryLeader);
        addLengthDocumentListener(31, txtRACELeaderName);
        addLengthDocumentListener(23, txtRACELeaderTitle);
        addLengthDocumentListener(31, txtRACECivilopediaEntry);
        addLengthDocumentListener(39, txtRACEAdjective);
        addLengthDocumentListener(39, txtRACENoun);
        addLengthDocumentListener(259, txtRACEForwardFilename);
        addLengthDocumentListener(259, txtRACEBackwardFilename);
        addLengthDocumentListener(31, txtScienceLeader);

    }
    /**
     * Sets 'exporatory' limits for text fields - anything not known to cause
     * a problem is allowed.
     */
    public void setExploratoryLimits()
    {
        clearBadValueDocumentListeners();
        clearLengthDocumentListeners();
        if (logger.isDebugEnabled())
            logger.debug("SETTING EXPLORATORY LIMITS");
        //set limits only for things that it is known do not work + length limits
        setMinimalLimits();

    }
    /**
     * Sets 'safe' limits for text fields - anything known not to cause a problem
     * is allowed.
     */
    public void setSafeLimits()
    {
        clearBadValueDocumentListeners();
        clearLengthDocumentListeners();
        if (logger.isDebugEnabled())
            logger.debug("SETTING SAFE LIMITS");
        //ensure that only things known to be okay are permissable
        //the call on Firaxis Limits will take care of minimal limits
        setFiraxisLimits();
    }

    /**
     * Sets 'Firaxis' limits for text fields - attempts to match the limits set
     * by Firaxis's editor as closely as possible.
     */
    public void setFiraxisLimits()
    {
        clearBadValueDocumentListeners();
        clearLengthDocumentListeners();
        if (logger.isDebugEnabled())
            logger.debug("SETTING FIRAXIS LIMITS");
        setMinimalLimits();
        //Max Firaxis value = 40
        addBadValueDocumentListener(40, -1, txtRACEDiplomacyTextIndex);
        //that's all the text boxes
    }

    /**
     * Sets 'Firaxis' limits for text fields, and attempts to prevent the user
     * from any other problematic settings they may inadvertantly cause.
     */
    public void setTotalLimits()
    {
        clearBadValueDocumentListeners();
        clearLengthDocumentListeners();
        if (logger.isDebugEnabled())
            logger.debug("SETTING FIRAXIS LIMITS");
        //don't allow any issues to occur, even if Firaxis would have
        //usually this will be the same as the Firaxian settings
        //this call will take care of minimal limits
        setFiraxisLimits();
    }
    
    public void deleteAction()
    {
        if (baselink.hasCustomMap())
        {
            JOptionPane.showMessageDialog(null, "Sorry, deleting civilizations with a custom map enabled is not currently supported", "Coming soon!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (logger.isInfoEnabled())
            logger.info("removing " + civilizationIndex + " " + civilization.get(civilizationIndex).getName());
        String oldName = civilization.get(civilizationIndex).getName();
        civilization.remove(civilizationIndex);
        int temp = civilizationIndex;
        if (logger.isDebugEnabled())
            logger.debug("Civ removed; id: " + temp);
        civilizationIndex = -1;
        civilizationList.remove(temp);
        if (logger.isDebugEnabled())
            logger.debug("Civ removed from list");
        //reset the list index - move on to the next item
        //if the last item was the one deleted, move to the previous one
        if (civilizationList.size() > temp)
            lstCivilizations.setSelectedIndex(temp); //lstBuildings updates, new index goes up by one.  This line then increments it again
        else
            lstCivilizations.setSelectedIndex(temp - 1);
        //update other tabs that need to know about changes - PRTO and RULE
        for (int j = 0; j < unit.size(); j++)
        {
            unit.get(j).handleDeletedCivilization(temp, civilization.size());
        }
        unitTab.civilizationList.remove(temp);
        //update alliance info
        for (int i = 0; i < gameTab.alliance0Model.size(); i++)
        {
            if (gameTab.alliance0Model.get(i).equals(oldName))
                gameTab.alliance0Model.removeElementAt(i);
        }
        for (int i = 0; i < gameTab.alliance1Model.size(); i++)
        {
            if (gameTab.alliance1Model.get(i).equals(oldName))
                gameTab.alliance1Model.removeElementAt(i);
        }
        for (int i = 0; i < gameTab.alliance2Model.size(); i++)
        {
            if (gameTab.alliance2Model.get(i).equals(oldName))
                gameTab.alliance2Model.removeElementAt(i);
        }
        for (int i = 0; i < gameTab.alliance3Model.size(); i++)
        {
            if (gameTab.alliance3Model.get(i).equals(oldName))
                gameTab.alliance3Model.removeElementAt(i);
        }
        for (int i = 0; i < gameTab.alliance4Model.size(); i++)
        {
            if (gameTab.alliance4Model.get(i).equals(oldName))
                gameTab.alliance4Model.removeElementAt(i);
        }
        //remove old index - minus one because the barbarians are NOT a civ in the civ alliance part of scenario properties
        if (temp > 0)   //if the barbs were deleted, don't do this
        {
            gameTab.prop.get(gameTab.propIndex).civPartOfWhichAlliance.remove(temp - 1);
            gameTab.prop.get(gameTab.propIndex).numberOfPlayableCivs--;
            gameTab.prop.get(gameTab.propIndex).dataLength-=8;
        }
        for (int i = temp; i < civilization.size(); i++)
        {
            civilization.get(i).setUniqueCivilizationCounter(civilization.get(i).getUniqueCivilizationCounter() - 1);
        }
    }

    
    public void renameBIQElement(int index, String name)
    {
        civilization.get(index).setCivilizationName(name);
        
        //alert game tab that we've updated
        gameTab.updateAllianceMembers();
    }
    
    public void cmdCivColorsAction()
    {
        if (hasCustomCivColors) {
            ColorPanel.setBIQ(baselink);
            ColorPanel cp = new ColorPanel();
            cp.setLocationRelativeTo(this);
            cp.setVisible(true);
        }
        else {
            //Determine if there are search folders
            GAME scenarioProperties = baselink.scenarioProperty.get(0);
            if (scenarioProperties.getSearchFolderLength() > 0) {
                copyCivColorsAfterScenarioFolderCheck(scenarioProperties.getSearchFolder(0));
            }
            else {
                String searchFolder = JOptionPane.showInputDialog(Main.mainMain, "<html>Civ Color Editing requires a scenario search folder, so the original colors can be copied over prior to editing, but this scenario does not have any."
                        + "<br/>You may add one in the text box below and click OK to edit Civ colors, or Cancel out and add them via the PROP tab.</html>", "Scenario Search Folder Required", JOptionPane.INFORMATION_MESSAGE);
                if (searchFolder != null && searchFolder.length() > 0) {
                    scenarioProperties.setScenarioSearchFolders(searchFolder);
                    gameTab.updateSearchFolders();
                    copyCivColorsAfterScenarioFolderCheck(searchFolder);
                }
            }
        }
    }
    
    private void copyCivColorsAfterScenarioFolderCheck(String firstFolder) {
        int response = JOptionPane.showConfirmDialog(Main.mainMain, "Would you like to copy the Firaxis colors into the " + firstFolder + " folder and start editing Civ colors?", "Copy Civ Colors?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            //copy
            //First create the folders
            boolean success = true;
            for (String s : Main.pcxNames) {
                try {
                    String firaxisLocation = utils.findFile(s + ".pcx", "Art" + Main.fileSlash + "units" + Main.fileSlash +  "Palettes" + Main.fileSlash, baselink);
                    String newLocation = utils.spliceInSearchFolder(firaxisLocation, firstFolder);

                    boolean pcxSuccess = utils.copyFile(firaxisLocation, newLocation);
                    if (!pcxSuccess) {
                        success = false;
                    }
                }
                catch(FileNotFoundException ex) {
                    logger.error("PCX file not found", ex);
                    JOptionPane.showMessageDialog(Main.mainMain, "Civ color editing could not be enabled due to not being able to locate the Firaxis PCX files", "Error copying PCX files", JOptionPane.ERROR_MESSAGE);
                }
            }

            //show
            if (success) {
                ColorPanel.setBIQ(baselink);
                ColorPanel cp = new ColorPanel();
                cp.setLocationRelativeTo(this);
                cp.setVisible(true);
                hasCustomCivColors = true;
            }
            else {
               JOptionPane.showMessageDialog(Main.mainMain, "Civ color editing could not be enabled due to not being able to locate the Firaxis PCX files", "Error copying PCX files", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
