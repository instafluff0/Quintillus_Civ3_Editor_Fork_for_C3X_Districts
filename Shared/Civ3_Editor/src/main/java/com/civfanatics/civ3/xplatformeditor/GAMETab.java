package com.civfanatics.civ3.xplatformeditor;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * TO BE ADDED:  Support for modifying the unknown 260-character field
 */

/**
 *
 * @author Quintillus
 */
import com.civfanatics.civ3.biqFile.RACE;
import com.civfanatics.civ3.biqFile.GAME;
import com.civfanatics.civ3.biqFile.WCHR;
import com.civfanatics.civ3.biqFile.civ3Version;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJTextField;
import com.civfanatics.civ3.xplatformeditor.Listeners.LengthDocumentListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JList;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JMenuItem;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.JScrollPane;
import javax.swing.DefaultListModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.JPopupMenu;
public class GAMETab extends EditorTab {
    
    String peace = "Peace";
    String war = "WAR";
    String enabled = "Enabled";
    String turnsEquals = "turns = ";
    String unitsEach = "units each";
    String next = "Next";

    boolean tabCreated;
    public List<GAME>prop;
    public List<RACE>civ;
    public List<WCHR>worldChar;
    int propIndex;

    JList sourceList;
    int selectedCiv;
    ButtonGroup victoryType;
    JMenuItem moveToAlliance0;
    JMenuItem moveToAlliance1;
    JMenuItem moveToAlliance2;
    JMenuItem moveToAlliance3;
    JMenuItem moveToAlliance4;
    JCheckBox chkGAMEAutoPlaceKings;
    JCheckBox chkGAMECaptureAnyFlag;
    JCheckBox chkGAMEDebugMode;
    JCheckBox chkGAMEDefaultVictoryConditions;
    JCheckBox chkGAMEMapVisible;
    JCheckBox chkGAMEPermitPlagues;
    JCheckBox chkGAMEPlaceCaptureUnits;
    JCheckBox chkGAMERespawnFlagUnits;
    JCheckBox chkGAMERetainCulture;
    JCheckBox chkGAMEUseDefaultRules;
    JCheckBox chkGAMEUseTimeLimit;
    JComboBox cmbGAMEBaseTimeUnit;
    JComboBox cmbGAMEMonthWeek;
    JComboBox cmbGAME_ADBC;
    JComboBox cmbWCHRBarbarianActivity;
    JCheckBox chkGAMEAutoPlaceVictoryLocations;
    JLabel jLabel171;
    JLabel jLabel172;
    JLabel jLabel173;
    JLabel jLabel174;
    JLabel jLabel175;
    JLabel jLabel176;
    JLabel jLabel177;
    JLabel jLabel178;
    JLabel jLabel179;
    JLabel jLabel180;
    JLabel jLabel181;
    JLabel jLabel182;
    JLabel jLabel183;
    JLabel jLabel184;
    JLabel jLabel185;
    JLabel jLabel186;
    JLabel jLabel187;
    JLabel jLabel188;
    JLabel jLabel189;
    JLabel jLabel190;
    JLabel jLabel191;
    JLabel jLabel192;
    JLabel jLabel193;
    JLabel jLabel194;
    JLabel jLabel195;
    JLabel jLabel196;
    JLabel jLabel197;
    JLabel jLabel198;
    JLabel jLabel199;
    JLabel jLabel200;
    JLabel jLabel201;
    JLabel jLabel202;
    JLabel jLabel203;
    JLabel jLabel204;
    JLabel jLabel205;
    JLabel jLabel206;
    JLabel jLabel207;
    JLabel jLabel208;
    JLabel jLabel209;
    JLabel jLabel210;
    JLabel jLabel211;
    JLabel jLabel212;
    JLabel jLabel213;
    JLabel jLabel214;
    JLabel jLabel215;
    JLabel jLabel216;
    JLabel jLabel217;
    JLabel jLabel218;
    JLabel jLabel219;
    JLabel jLabel220;
    JLabel jLabel221;
    JLabel jLabel222;
    JLabel jLabel223;
    JLabel jLabel224;
    SuperJTextField lblGAMEAlliance0Name;
    SuperJTextField lblGAMEAlliance1Name;
    SuperJTextField lblGAMEAlliance2Name;
    SuperJTextField lblGAMEAlliance3Name;
    SuperJTextField lblGAMEAlliance4Name;
    JLabel jLabel230;
    JLabel jLabel231;
    JLabel jLabel232;
    JLabel jLabel233;
    JLabel jLabel234;
    JList lstGAMEVictoryConditions;
    JList lstGAMERulesSelected;
    //JPanel this;
    JScrollPane jScrollPane22;
    JScrollPane jScrollPane23;
    JScrollPane jScrollPane24;
    JScrollPane scrGAMENoAlliances;
    JScrollPane scrGAMEAlliance1;
    JScrollPane scrGAMEAlliance2;
    JScrollPane scrGAMEAlliance3;
    JScrollPane scrGAMEAlliance4;
    JToggleButton jToggleButton1;
    JToggleButton jToggleButton11;
    JToggleButton jToggleButton16;
    JToggleButton jToggleButton6;
    JLabel lblGAMEMonthWeek;
    JList lstGAMEAlliance1;
    JList lstGAMEAlliance2;
    JList lstGAMEAlliance3;
    JList lstGAMEAlliance4;
    JList lstGAMENoAlliances;
    JPanel thisGameRules;
    JPanel thisLockedAlliances;
    JPanel thisMPTurnTimeLimits;
    JPanel thisPlague;
    JPanel thisTime;
    JPanel thisTimeLimit;
    JPanel thisVictoryConditions;
    JPanel thisVictoryLimits;
    JPanel thisVictoryPoints;
    JPanel thisVictoryType;
    JPanel thisWar;
    JRadioButton rbtnGAMECoalitionVictory;
    JRadioButton rbtnGAMEIndividualVictory;
    JToggleButton tglGAME1v2;
    JToggleButton tglGAME1v3;
    JToggleButton tglGAME1v4;
    JToggleButton tglGAME2v1;
    JToggleButton tglGAME2v3;
    JToggleButton tglGAME2v4;
    JToggleButton tglGAME3v1;
    JToggleButton tglGAME3v2;
    JToggleButton tglGAME3v4;
    JToggleButton tglGAME4v1;
    JToggleButton tglGAME4v2;
    JToggleButton tglGAME4v3;
    SuperJTextField txtGAMEAdvancementVP;
    SuperJTextField txtGAMEAllCitiesCultureWinLimit;
    SuperJTextField txtGAMECaptureSpecialUnitVP;
    SuperJTextField txtGAMECityConquestVP;
    SuperJTextField txtGAMECityEliminationCount;
    SuperJTextField txtGAMEDefeatingOpposingUnitVP;
    SuperJTextField txtGAMEDominationPopulationPercent;
    SuperJTextField txtGAMEDominationTerrainPercent;
    SuperJTextField txtGAMEEruptionPeriod;
    SuperJTextField txtGAMEGoldForCapture;
    SuperJTextField txtGAMEMinuteTimeLimit;
    SuperJTextField txtGAMEMpBaseTime;
    SuperJTextField txtGAMEMpCityTime;
    SuperJTextField txtGAMEMpUnitTime;
    SuperJTextField txtGAMENumTurns1;
    SuperJTextField txtGAMENumTurns2;
    SuperJTextField txtGAMENumTurns3;
    SuperJTextField txtGAMENumTurns4;
    SuperJTextField txtGAMENumTurns5;
    SuperJTextField txtGAMENumTurns6;
    SuperJTextField txtGAMENumTurns7;
    SuperJTextField txtGAMEOneCityCultureWinLimit;
    SuperJTextField txtGAMEPlagueDuration;
    SuperJTextField txtGAMEPlagueEarliestStart;
    SuperJTextField txtGAMEPlagueGracePeriod;
    SuperJTextField txtGAMEPlagueMaxOccurance;
    SuperJTextField txtGAMEPlagueName;
    SuperJTextField txtGAMEPlagueStrength;
    SuperJTextField txtGAMEPlagueVariation;
    SuperJTextField txtGAMEScenarioSearchFolders;
    SuperJTextField txtGAMEStartYear;
    SuperJTextField txtGAMETurnGroup1;
    SuperJTextField txtGAMETurnGroup2;
    SuperJTextField txtGAMETurnGroup3;
    SuperJTextField txtGAMETurnGroup4;
    SuperJTextField txtGAMETurnGroup5;
    SuperJTextField txtGAMETurnGroup6;
    SuperJTextField txtGAMETurnGroup7;
    SuperJTextField txtGAMETurnTimeLimit;
    SuperJTextField txtGAMEVictoryPointLimit;
    SuperJTextField txtGAMEVictoryPointVP;
    SuperJTextField txtGAMEWonderVP;
    JTextArea txtVERDescription;
    SuperJTextField txtVERTitle;
    private DefaultComboBoxModel monthModel = new DefaultComboBoxModel(new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September","October", "November", "December"});
    private DefaultComboBoxModel weekModel = new DefaultComboBoxModel(new Integer[0]);

    public DefaultListModel alliance0Model = new DefaultListModel();
    public DefaultListModel alliance1Model = new DefaultListModel();
    public DefaultListModel alliance2Model = new DefaultListModel();
    public DefaultListModel alliance3Model = new DefaultListModel();
    public DefaultListModel alliance4Model = new DefaultListModel();

    /**
     * The constructor.  Initializes the GUI, list models, etc.
     */
    public GAMETab()
    {
        tabName = "GAME";
        textBoxes = new ArrayList<>();
        for (int i = 1; i < 53; i++)
        {
            if (logger.isDebugEnabled())
                logger.debug("Adding to week model");
            weekModel.addElement(i);
        }
         //this = new JPanel();
        victoryType = new ButtonGroup();
        jLabel171 = new JLabel();
        txtVERTitle = new SuperJTextField();
        jScrollPane22 = new JScrollPane();
        txtVERDescription = new JTextArea();
        jLabel172 = new JLabel();
        jLabel173 = new JLabel();
        txtGAMEScenarioSearchFolders = new SuperJTextField();
        thisTime = new JPanel();
        thisTimeLimit = new JPanel();
        chkGAMEUseTimeLimit = new JCheckBox();
        jLabel174 = new JLabel();
        txtGAMETurnTimeLimit = new SuperJTextField();
        jLabel175 = new JLabel();
        txtGAMEMinuteTimeLimit = new SuperJTextField();
        thisMPTurnTimeLimits = new JPanel();
        jLabel176 = new JLabel();
        txtGAMEMpBaseTime = new SuperJTextField();
        jLabel177 = new JLabel();
        jLabel178 = new JLabel();
        txtGAMEMpCityTime = new SuperJTextField();
        txtGAMEMpUnitTime = new SuperJTextField();
        jLabel179 = new JLabel();
        txtGAMETurnGroup1 = new SuperJTextField();
        jLabel180 = new JLabel();
        txtGAMENumTurns1 = new SuperJTextField();
        jLabel181 = new JLabel();
        jLabel182 = new JLabel();
        txtGAMETurnGroup2 = new SuperJTextField();
        jLabel183 = new JLabel();
        txtGAMENumTurns2 = new SuperJTextField();
        jLabel184 = new JLabel();
        jLabel185 = new JLabel();
        txtGAMETurnGroup3 = new SuperJTextField();
        jLabel186 = new JLabel();
        txtGAMENumTurns3 = new SuperJTextField();
        jLabel187 = new JLabel();
        jLabel188 = new JLabel();
        txtGAMETurnGroup4 = new SuperJTextField();
        jLabel189 = new JLabel();
        txtGAMENumTurns4 = new SuperJTextField();
        jLabel190 = new JLabel();
        jLabel191 = new JLabel();
        txtGAMETurnGroup5 = new SuperJTextField();
        jLabel192 = new JLabel();
        txtGAMENumTurns5 = new SuperJTextField();
        jLabel193 = new JLabel();
        jLabel194 = new JLabel();
        txtGAMETurnGroup6 = new SuperJTextField();
        jLabel195 = new JLabel();
        txtGAMENumTurns6 = new SuperJTextField();
        jLabel196 = new JLabel();
        jLabel197 = new JLabel();
        txtGAMETurnGroup7 = new SuperJTextField();
        jLabel198 = new JLabel();
        txtGAMENumTurns7 = new SuperJTextField();
        jLabel199 = new JLabel();
        jLabel200 = new JLabel();
        txtGAMEStartYear = new SuperJTextField();
        jLabel201 = new JLabel();
        cmbGAME_ADBC = new JComboBox();
        lblGAMEMonthWeek = new JLabel();
        cmbGAMEMonthWeek = new JComboBox();
        jLabel202 = new JLabel();
        cmbGAMEBaseTimeUnit = new JComboBox();
        chkGAMEDebugMode = new JCheckBox();
        jLabel203 = new JLabel();
        cmbWCHRBarbarianActivity = new JComboBox();
        thisVictoryConditions = new JPanel();
        chkGAMEDefaultVictoryConditions = new JCheckBox();
        jScrollPane23 = new JScrollPane();
        lstGAMEVictoryConditions = new JList();
        thisGameRules = new JPanel();
        chkGAMEUseDefaultRules = new JCheckBox();
        jScrollPane24 = new JScrollPane();
        lstGAMERulesSelected = new JList();
        chkGAMEAutoPlaceKings = new JCheckBox();
        chkGAMEPlaceCaptureUnits = new JCheckBox();
        chkGAMEAutoPlaceVictoryLocations = new JCheckBox();
        chkGAMEMapVisible = new JCheckBox();
        chkGAMERetainCulture = new JCheckBox();
        thisVictoryLimits = new JPanel();
        jLabel204 = new JLabel();
        txtGAMEVictoryPointLimit = new SuperJTextField();
        jLabel205 = new JLabel();
        txtGAMECityEliminationCount = new SuperJTextField();
        jLabel206 = new JLabel();
        txtGAMEOneCityCultureWinLimit = new SuperJTextField();
        jLabel207 = new JLabel();
        txtGAMEAllCitiesCultureWinLimit = new SuperJTextField();
        txtGAMEDominationTerrainPercent = new SuperJTextField();
        txtGAMEDominationPopulationPercent = new SuperJTextField();
        jLabel208 = new JLabel();
        jLabel209 = new JLabel();
        thisVictoryPoints = new JPanel();
        jLabel210 = new JLabel();
        txtGAMEWonderVP = new SuperJTextField();
        jLabel211 = new JLabel();
        txtGAMEAdvancementVP = new SuperJTextField();
        jLabel212 = new JLabel();
        txtGAMEDefeatingOpposingUnitVP = new SuperJTextField();
        jLabel213 = new JLabel();
        txtGAMECityConquestVP = new SuperJTextField();
        jLabel214 = new JLabel();
        txtGAMEVictoryPointVP = new SuperJTextField();
        jLabel215 = new JLabel();
        txtGAMECaptureSpecialUnitVP = new SuperJTextField();
        jLabel216 = new JLabel();
        txtGAMEGoldForCapture = new SuperJTextField();
        chkGAMERespawnFlagUnits = new JCheckBox();
        chkGAMECaptureAnyFlag = new JCheckBox();
        thisPlague = new JPanel();
        chkGAMEPermitPlagues = new JCheckBox();
        jLabel217 = new JLabel();
        txtGAMEPlagueName = new SuperJTextField();
        jLabel218 = new JLabel();
        txtGAMEPlagueEarliestStart = new SuperJTextField();
        jLabel219 = new JLabel();
        txtGAMEPlagueVariation = new SuperJTextField();
        jLabel220 = new JLabel();
        txtGAMEPlagueDuration = new SuperJTextField();
        jLabel221 = new JLabel();
        txtGAMEPlagueStrength = new SuperJTextField();
        jLabel222 = new JLabel();
        txtGAMEPlagueGracePeriod = new SuperJTextField();
        jLabel223 = new JLabel();
        txtGAMEPlagueMaxOccurance = new SuperJTextField();
        jLabel224 = new JLabel();
        txtGAMEEruptionPeriod = new SuperJTextField();
        thisLockedAlliances = new JPanel();
        lblGAMEAlliance0Name = new SuperJTextField();
        lblGAMEAlliance1Name = new SuperJTextField();
        lblGAMEAlliance2Name = new SuperJTextField();
        lblGAMEAlliance3Name = new SuperJTextField();
        lblGAMEAlliance4Name = new SuperJTextField();
        scrGAMENoAlliances = new JScrollPane();
        lstGAMENoAlliances = new JList();
        scrGAMEAlliance1 = new JScrollPane();
        lstGAMEAlliance1 = new JList();
        thisWar = new JPanel();
        jLabel230 = new JLabel();
        jLabel231 = new JLabel();
        jLabel232 = new JLabel();
        jLabel233 = new JLabel();
        jLabel234 = new JLabel();
        jToggleButton1 = new JToggleButton();
        tglGAME2v1 = new JToggleButton();
        tglGAME3v1 = new JToggleButton();
        tglGAME4v1 = new JToggleButton();
        tglGAME1v2 = new JToggleButton();
        jToggleButton6 = new JToggleButton();
        tglGAME3v2 = new JToggleButton();
        tglGAME4v2 = new JToggleButton();
        tglGAME1v3 = new JToggleButton();
        tglGAME2v3 = new JToggleButton();
        jToggleButton11 = new JToggleButton();
        tglGAME4v3 = new JToggleButton();
        tglGAME1v4 = new JToggleButton();
        tglGAME2v4 = new JToggleButton();
        tglGAME3v4 = new JToggleButton();
        jToggleButton16 = new JToggleButton();
        scrGAMEAlliance2 = new JScrollPane();
        lstGAMEAlliance2 = new JList();
        scrGAMEAlliance3 = new JScrollPane();
        lstGAMEAlliance3 = new JList();
        scrGAMEAlliance4 = new JScrollPane();
        lstGAMEAlliance4 = new JList();
        thisVictoryType = new JPanel();
        rbtnGAMEIndividualVictory = new JRadioButton();
        rbtnGAMECoalitionVictory = new JRadioButton();
    }
    /**
     * Creates the GUI of the tab.  This includes button groups, layouts, etc.
     * Logically, this is to occur immediately after the constructor is called.
     *
     * @return - A reference to the GAMETab instance itself.  This reference
     * allows the tab to be added to a tab bar.
     */
    public JPanel createTab()
    {
        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        
        victoryType.add(this.rbtnGAMECoalitionVictory);
        victoryType.add(this.rbtnGAMEIndividualVictory);

        jLabel171.setText("Scenario Title:");
        this.add(jLabel171, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));
        this.add(txtVERTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 170, -1));

        txtVERDescription.setColumns(20);
        txtVERDescription.setRows(5);
        txtVERDescription.setWrapStyleWord(true);   //wrap at word, not character, boundaries
        jScrollPane22.setViewportView(txtVERDescription);

        this.add(jScrollPane22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 260, 140));

        jLabel172.setText("Description");
        this.add(jLabel172, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, -1));

        jLabel173.setText("Search Folders:");
        this.add(jLabel173, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, -1, -1));
        this.add(txtGAMEScenarioSearchFolders, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 190, 160, -1));

        thisTime.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Time"));
        thisTime.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        thisTimeLimit.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Time Limit"));
        thisTimeLimit.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        chkGAMEUseTimeLimit.setText(enabled);
        thisTimeLimit.add(chkGAMEUseTimeLimit, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jLabel174.setText("Turns:");
        thisTimeLimit.add(jLabel174, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));
        thisTimeLimit.add(txtGAMETurnTimeLimit, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, 70, 20));

        jLabel175.setText("Minutes:");
        thisTimeLimit.add(jLabel175, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));
        thisTimeLimit.add(txtGAMEMinuteTimeLimit, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 70, 70, -1));

        thisTime.add(thisTimeLimit, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 170, 100));

        thisMPTurnTimeLimits.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "MP Turn Time Limits"));
        thisMPTurnTimeLimits.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel176.setText("Base:");
        thisMPTurnTimeLimits.add(jLabel176, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));
        thisMPTurnTimeLimits.add(txtGAMEMpBaseTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 70, -1));

        jLabel177.setText("Per City:");
        thisMPTurnTimeLimits.add(jLabel177, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jLabel178.setText("Per Unit:");
        thisMPTurnTimeLimits.add(jLabel178, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));
        thisMPTurnTimeLimits.add(txtGAMEMpCityTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, 70, -1));
        thisMPTurnTimeLimits.add(txtGAMEMpUnitTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, 70, -1));

        thisTime.add(thisMPTurnTimeLimits, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 170, 90));

        jLabel179.setText("First");
        thisTime.add(jLabel179, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, -1, -1));
        thisTime.add(txtGAMETurnGroup1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 20, 50, -1));

        jLabel180.setText(turnsEquals);
        thisTime.add(jLabel180, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 20, -1, -1));
        thisTime.add(txtGAMENumTurns1, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 20, 40, -1));

        jLabel181.setText(unitsEach);
        thisTime.add(jLabel181, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 20, -1, -1));

        jLabel182.setText(next);
        thisTime.add(jLabel182, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 40, -1, -1));
        thisTime.add(txtGAMETurnGroup2, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 40, 50, -1));

        jLabel183.setText(turnsEquals);
        thisTime.add(jLabel183, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 40, -1, -1));
        thisTime.add(txtGAMENumTurns2, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 40, 40, -1));

        jLabel184.setText(unitsEach);
        thisTime.add(jLabel184, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 40, -1, -1));

        jLabel185.setText(next);
        thisTime.add(jLabel185, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 60, -1, -1));
        thisTime.add(txtGAMETurnGroup3, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 60, 50, -1));

        jLabel186.setText(turnsEquals);
        thisTime.add(jLabel186, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 60, -1, -1));
        thisTime.add(txtGAMENumTurns3, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 60, 40, -1));

        jLabel187.setText(unitsEach);
        thisTime.add(jLabel187, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 60, -1, -1));

        jLabel188.setText(next);
        thisTime.add(jLabel188, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 80, -1, -1));
        thisTime.add(txtGAMETurnGroup4, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 80, 50, -1));

        jLabel189.setText(turnsEquals);
        thisTime.add(jLabel189, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 80, -1, -1));
        thisTime.add(txtGAMENumTurns4, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 80, 40, -1));

        jLabel190.setText(unitsEach);
        thisTime.add(jLabel190, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 80, -1, -1));

        jLabel191.setText(next);
        thisTime.add(jLabel191, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 100, -1, -1));
        thisTime.add(txtGAMETurnGroup5, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 100, 50, -1));

        jLabel192.setText(turnsEquals);
        thisTime.add(jLabel192, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 100, -1, -1));
        thisTime.add(txtGAMENumTurns5, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 100, 40, -1));

        jLabel193.setText(unitsEach);
        thisTime.add(jLabel193, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 100, -1, -1));

        jLabel194.setText(next);
        thisTime.add(jLabel194, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 120, -1, -1));
        thisTime.add(txtGAMETurnGroup6, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 120, 50, -1));

        jLabel195.setText(turnsEquals);
        thisTime.add(jLabel195, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 120, -1, -1));
        thisTime.add(txtGAMENumTurns6, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 120, 40, -1));

        jLabel196.setText(unitsEach);
        thisTime.add(jLabel196, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 120, -1, -1));

        jLabel197.setText(next);
        thisTime.add(jLabel197, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 140, -1, -1));
        thisTime.add(txtGAMETurnGroup7, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 140, 50, -1));

        jLabel198.setText(turnsEquals);
        thisTime.add(jLabel198, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 140, -1, -1));
        thisTime.add(txtGAMENumTurns7, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 140, 40, -1));

        jLabel199.setText(unitsEach);
        thisTime.add(jLabel199, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 140, -1, -1));

        jLabel200.setText("Start Date:");
        thisTime.add(jLabel200, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 160, -1, -1));
        thisTime.add(txtGAMEStartYear, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 160, 70, -1));

        jLabel201.setText(" Year");
        thisTime.add(jLabel201, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 160, -1, -1));

        cmbGAME_ADBC.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "BC", "AD" }));
        thisTime.add(cmbGAME_ADBC, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 160, 70, 20));

        lblGAMEMonthWeek.setText("Month");
        thisTime.add(lblGAMEMonthWeek, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 180, -1, -1));

        cmbGAMEMonthWeek.setModel(monthModel);
        thisTime.add(cmbGAMEMonthWeek, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 180, 140, 20));

        jLabel202.setText("Base unit of Time:");
        thisTime.add(jLabel202, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, -1, -1));

        cmbGAMEBaseTimeUnit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Years", "Months", "Weeks" }));
        thisTime.add(cmbGAMEBaseTimeUnit, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 220, 150, 20));
        cmbGAMEBaseTimeUnit.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e){
                if (cmbGAMEBaseTimeUnit.getSelectedItem().equals("Months"))
                {
                    lblGAMEMonthWeek.setText("Month");
                    lblGAMEMonthWeek.setVisible(true);
                    cmbGAMEMonthWeek.setVisible(true);
                    cmbGAMEMonthWeek.setSelectedIndex(0);
                    cmbGAMEMonthWeek.setModel(monthModel);
                }
                else if (cmbGAMEBaseTimeUnit.getSelectedItem().equals("Weeks"))
                {
                    lblGAMEMonthWeek.setText("Week");
                    lblGAMEMonthWeek.setVisible(true);
                    cmbGAMEMonthWeek.setVisible(true);
                    cmbGAMEMonthWeek.setSelectedIndex(0);
                    cmbGAMEMonthWeek.setModel(weekModel);
                }
                else
                {
                    lblGAMEMonthWeek.setVisible(false);
                    cmbGAMEMonthWeek.setVisible(false);
                }
            }
        });

        this.add(thisTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, 450, 250));

        chkGAMEDebugMode.setText("Debug Mode");
        this.add(chkGAMEDebugMode, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, -1, -1));

        jLabel203.setText("Barbarian Activity:");
        this.add(jLabel203, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, -1, -1));

        cmbWCHRBarbarianActivity.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No Barbarians", "Sedentary", "Roaming", "Restless", "Raging", "Random" }));
        cmbWCHRBarbarianActivity.setEnabled(false);
        this.add(cmbWCHRBarbarianActivity, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 250, 150, -1));

        thisVictoryConditions.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Victory Conditions"));

        chkGAMEDefaultVictoryConditions.setText("Default Conditions");

        lstGAMEVictoryConditions.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Domination", "Space Race", "Diplomatic", "Conquest", "Cultural", "Wonder" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane23.setViewportView(lstGAMEVictoryConditions);

        org.jdesktop.layout.GroupLayout thisVictoryConditionsLayout = new org.jdesktop.layout.GroupLayout(thisVictoryConditions);
        thisVictoryConditions.setLayout(thisVictoryConditionsLayout);
        thisVictoryConditionsLayout.setHorizontalGroup(
            thisVictoryConditionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(thisVictoryConditionsLayout.createSequentialGroup()
                .add(thisVictoryConditionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(chkGAMEDefaultVictoryConditions)
                    .add(jScrollPane23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 112, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        thisVictoryConditionsLayout.setVerticalGroup(
            thisVictoryConditionsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(thisVictoryConditionsLayout.createSequentialGroup()
                .add(chkGAMEDefaultVictoryConditions)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 117, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        this.add(thisVictoryConditions, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 140, 170));

        thisGameRules.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Game Rules"));

        chkGAMEUseDefaultRules.setText("Default Rules");

        lstGAMERulesSelected.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Civ specific abilities", "Culturally Linked Start", "Respawn AI Players", "Preserve Random Seed", "Accelerated Production", "City Elimination", "Regicide", "Regicide (all Kings)", "Victory Point Scoring", "Capture the Unit", "Allow Cultural Conversions", "Reverse Capture the Flag", "Allow Scientific Leaders" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane24.setViewportView(lstGAMERulesSelected);

        org.jdesktop.layout.GroupLayout thisGameRulesLayout = new org.jdesktop.layout.GroupLayout(thisGameRules);
        thisGameRules.setLayout(thisGameRulesLayout);
        thisGameRulesLayout.setHorizontalGroup(
            thisGameRulesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(thisGameRulesLayout.createSequentialGroup()
                .add(chkGAMEUseDefaultRules)
                .addContainerGap(78, Short.MAX_VALUE))
            .add(jScrollPane24, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
        );
        thisGameRulesLayout.setVerticalGroup(
            thisGameRulesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(thisGameRulesLayout.createSequentialGroup()
                .add(chkGAMEUseDefaultRules)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 115, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        this.add(thisGameRules, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 280, 190, 170));

        chkGAMEAutoPlaceKings.setText("Auto Place King Units");
        this.add(chkGAMEAutoPlaceKings, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 20, -1, -1));

        chkGAMEPlaceCaptureUnits.setText("Auto Place Capture Units");
        this.add(chkGAMEPlaceCaptureUnits, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 40, -1, -1));

        chkGAMEAutoPlaceVictoryLocations.setText("Auto Place Victory Locations");
        this.add(chkGAMEAutoPlaceVictoryLocations, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 60, -1, -1));

        chkGAMEMapVisible.setText("Reveal Entire Map");
        this.add(chkGAMEMapVisible, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 80, -1, -1));

        chkGAMERetainCulture.setText("Retain Culture on Capture");
        this.add(chkGAMERetainCulture, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 100, -1, -1));

        thisVictoryLimits.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Victory Limits"));

        jLabel204.setText("Victory Points:");

        jLabel205.setText("City Elimination:");

        jLabel206.setText("1-City Culture:");

        jLabel207.setText("Empire Culture:");

        jLabel208.setText("% Land - Domin.");

        jLabel209.setText("% Pop - Domin.");

        org.jdesktop.layout.GroupLayout thisVictoryLimitsLayout = new org.jdesktop.layout.GroupLayout(thisVictoryLimits);
        thisVictoryLimits.setLayout(thisVictoryLimitsLayout);
        thisVictoryLimitsLayout.setHorizontalGroup(
            thisVictoryLimitsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(thisVictoryLimitsLayout.createSequentialGroup()
                .add(thisVictoryLimitsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel204)
                    .add(jLabel205)
                    .add(jLabel206)
                    .add(jLabel207)
                    .add(jLabel208)
                    .add(jLabel209))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(thisVictoryLimitsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, txtGAMEDominationPopulationPercent, 0, 0, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, txtGAMEDominationTerrainPercent, 0, 0, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, txtGAMEAllCitiesCultureWinLimit, 0, 0, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, txtGAMEOneCityCultureWinLimit, 0, 0, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, txtGAMECityEliminationCount, 0, 0, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, txtGAMEVictoryPointLimit, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        thisVictoryLimitsLayout.setVerticalGroup(
            thisVictoryLimitsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(thisVictoryLimitsLayout.createSequentialGroup()
                .add(thisVictoryLimitsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel204)
                    .add(txtGAMEVictoryPointLimit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(thisVictoryLimitsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel205)
                    .add(txtGAMECityEliminationCount, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(thisVictoryLimitsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel206)
                    .add(txtGAMEOneCityCultureWinLimit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(thisVictoryLimitsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel207)
                    .add(txtGAMEAllCitiesCultureWinLimit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(thisVictoryLimitsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(txtGAMEDominationTerrainPercent, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel208))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(thisVictoryLimitsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(txtGAMEDominationPopulationPercent, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel209))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        this.add(thisVictoryLimits, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 270, 180, 180));

        thisVictoryPoints.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Victory Points"));

        jLabel210.setText("Wonder Multiplier:");

        jLabel211.setText("Advancement Pts:");

        jLabel212.setText("Defeating Unit:");

        jLabel213.setText("City Conquest Pts:");

        jLabel214.setText("Victory Pt Loc:");

        jLabel215.setText("Capture Princess:");

        jLabel216.setText("Gold for Capture:");

        org.jdesktop.layout.GroupLayout thisVictoryPointsLayout = new org.jdesktop.layout.GroupLayout(thisVictoryPoints);
        thisVictoryPoints.setLayout(thisVictoryPointsLayout);
        thisVictoryPointsLayout.setHorizontalGroup(
            thisVictoryPointsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(thisVictoryPointsLayout.createSequentialGroup()
                .add(thisVictoryPointsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(thisVictoryPointsLayout.createSequentialGroup()
                        .add(thisVictoryPointsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel210)
                            .add(jLabel211)
                            .add(jLabel212))
                        .add(3, 3, 3)
                        .add(thisVictoryPointsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(txtGAMEAdvancementVP, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                            .add(txtGAMEWonderVP, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                            .add(txtGAMEDefeatingOpposingUnitVP, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)))
                    .add(thisVictoryPointsLayout.createSequentialGroup()
                        .add(jLabel213)
                        .add(5, 5, 5)
                        .add(txtGAMECityConquestVP, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE))
                    .add(thisVictoryPointsLayout.createSequentialGroup()
                        .add(jLabel214)
                        .add(26, 26, 26)
                        .add(txtGAMEVictoryPointVP, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE))
                    .add(thisVictoryPointsLayout.createSequentialGroup()
                        .add(thisVictoryPointsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel215)
                            .add(jLabel216))
                        .add(6, 6, 6)
                        .add(thisVictoryPointsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(txtGAMEGoldForCapture, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                            .add(txtGAMECaptureSpecialUnitVP, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE))))
                .addContainerGap())
        );
        thisVictoryPointsLayout.setVerticalGroup(
            thisVictoryPointsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(thisVictoryPointsLayout.createSequentialGroup()
                .add(thisVictoryPointsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel210)
                    .add(txtGAMEWonderVP, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(thisVictoryPointsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel211)
                    .add(txtGAMEAdvancementVP, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(thisVictoryPointsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel212)
                    .add(txtGAMEDefeatingOpposingUnitVP, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(thisVictoryPointsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel213)
                    .add(txtGAMECityConquestVP, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(thisVictoryPointsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel214)
                    .add(txtGAMEVictoryPointVP, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(thisVictoryPointsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel215)
                    .add(txtGAMECaptureSpecialUnitVP, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(thisVictoryPointsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(txtGAMEGoldForCapture, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel216))
                .addContainerGap())
        );

        this.add(thisVictoryPoints, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 260, 190, 210));

        chkGAMERespawnFlagUnits.setText("Respawn Flag Unit on Capture");
        this.add(chkGAMERespawnFlagUnits, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 120, -1, -1));

        chkGAMECaptureAnyFlag.setText("Allow anyone to capture any flag");
        this.add(chkGAMECaptureAnyFlag, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 140, -1, -1));

        thisPlague.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Plague"));

        chkGAMEPermitPlagues.setText(enabled);

        jLabel217.setText("Name:");

        jLabel218.setText("Earliest Start:");

        jLabel219.setText(" Variance:");

        jLabel220.setText("Duration:");

        jLabel221.setText("Strength:");

        jLabel222.setText("Grace Period:");

        jLabel223.setText("Max Times:");

        org.jdesktop.layout.GroupLayout thisPlagueLayout = new org.jdesktop.layout.GroupLayout(thisPlague);
        thisPlague.setLayout(thisPlagueLayout);
        thisPlagueLayout.setHorizontalGroup(
            thisPlagueLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(thisPlagueLayout.createSequentialGroup()
                .add(thisPlagueLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(thisPlagueLayout.createSequentialGroup()
                        .add(thisPlagueLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(chkGAMEPermitPlagues)
                            .add(jLabel218)
                            .add(jLabel220))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(thisPlagueLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(thisPlagueLayout.createSequentialGroup()
                                .add(thisPlagueLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(thisPlagueLayout.createSequentialGroup()
                                        .add(jLabel217)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(txtGAMEPlagueName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE))
                                    .add(thisPlagueLayout.createSequentialGroup()
                                        .add(txtGAMEPlagueEarliestStart, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 53, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(2, 2, 2)
                                        .add(jLabel219)))
                                .add(3, 3, 3))
                            .add(thisPlagueLayout.createSequentialGroup()
                                .add(txtGAMEPlagueDuration, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 53, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(2, 2, 2)
                                .add(jLabel221)
                                .add(8, 8, 8))))
                    .add(thisPlagueLayout.createSequentialGroup()
                        .add(jLabel222)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtGAMEPlagueGracePeriod, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 53, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(4, 4, 4)
                        .add(jLabel223)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                .add(thisPlagueLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(txtGAMEPlagueMaxOccurance, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 53, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtGAMEPlagueStrength, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 53, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtGAMEPlagueVariation, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 54, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        thisPlagueLayout.setVerticalGroup(
            thisPlagueLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(thisPlagueLayout.createSequentialGroup()
                .add(thisPlagueLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chkGAMEPermitPlagues)
                    .add(jLabel217)
                    .add(txtGAMEPlagueName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(thisPlagueLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel218)
                    .add(txtGAMEPlagueEarliestStart, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel219)
                    .add(txtGAMEPlagueVariation, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(thisPlagueLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel220)
                    .add(thisPlagueLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jLabel221)
                        .add(txtGAMEPlagueStrength, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(txtGAMEPlagueDuration, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(thisPlagueLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel222)
                    .add(txtGAMEPlagueGracePeriod, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel223)
                    .add(txtGAMEPlagueMaxOccurance, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        this.add(thisPlague, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 170, 280, -1));

        jLabel224.setText("Volcanos: Maximum Eruption Period:");
        this.add(jLabel224, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 320, -1, -1));
        this.add(txtGAMEEruptionPeriod, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 320, 60, -1));

        thisLockedAlliances.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Locked Alliances"));
        thisLockedAlliances.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblGAMEAlliance0Name.setText("No Alliances");
        thisLockedAlliances.add(lblGAMEAlliance0Name, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 120, -1));

        lblGAMEAlliance1Name.setText("Alliance 1");
        thisLockedAlliances.add(lblGAMEAlliance1Name, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 120, -1));

        lstGAMENoAlliances.setModel(alliance0Model);
        scrGAMENoAlliances.setViewportView(lstGAMENoAlliances);

        thisLockedAlliances.add(scrGAMENoAlliances, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 120, 150));

        lstGAMEAlliance1.setModel(alliance1Model);
        scrGAMEAlliance1.setViewportView(lstGAMEAlliance1);

        thisLockedAlliances.add(scrGAMEAlliance1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 40, 120, 150));

        lblGAMEAlliance2Name.setText("Alliance 2");
        thisLockedAlliances.add(lblGAMEAlliance2Name, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, 120, -1));

        lstGAMEAlliance2.setModel(alliance2Model);
        scrGAMEAlliance2.setViewportView(lstGAMEAlliance2);

        thisLockedAlliances.add(scrGAMEAlliance2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 40, 120, 150));

        lblGAMEAlliance3Name.setText("Alliance 3");
        thisLockedAlliances.add(lblGAMEAlliance3Name, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 20, 120, -1));

        lstGAMEAlliance3.setModel(alliance3Model);
        scrGAMEAlliance3.setViewportView(lstGAMEAlliance3);

        thisLockedAlliances.add(scrGAMEAlliance3, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 40, 120, 150));

        lstGAMEAlliance4.setModel(alliance4Model);
        scrGAMEAlliance4.setViewportView(lstGAMEAlliance4);

        thisLockedAlliances.add(scrGAMEAlliance4, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 40, 120, 150));

        lblGAMEAlliance4Name.setText("Alliance 4");
        thisLockedAlliances.add(lblGAMEAlliance4Name, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 20, 120, -1));


        thisWar.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "War - # Represents Alliance"));
        thisWar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel230.setText("1                 2               3               4");
        thisWar.add(jLabel230, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, -1, -1));

        jLabel231.setText("1");
        thisWar.add(jLabel231, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jLabel232.setText("2");
        thisWar.add(jLabel232, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));

        jLabel233.setText("3");
        thisWar.add(jLabel233, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

        jLabel234.setText("4");
        thisWar.add(jLabel234, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));

        jToggleButton1.setText(peace);
        jToggleButton1.setEnabled(false);
        thisWar.add(jToggleButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, 20));

        tglGAME2v1.setText(peace);
        thisWar.add(tglGAME2v1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 67, 20));

        tglGAME3v1.setText(peace);
        thisWar.add(tglGAME3v1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 67, 20));

        tglGAME4v1.setText(peace);
        thisWar.add(tglGAME4v1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 67, 20));

        tglGAME1v2.setText(peace);
        thisWar.add(tglGAME1v2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, 67, 20));

        jToggleButton6.setText(peace);
        jToggleButton6.setEnabled(false);
        thisWar.add(jToggleButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, -1, 20));

        tglGAME3v2.setText(peace);
        thisWar.add(tglGAME3v2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 80, 67, 20));

        tglGAME4v2.setText(peace);
        thisWar.add(tglGAME4v2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, 67, 20));

        tglGAME1v3.setText(peace);
        thisWar.add(tglGAME1v3, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 40, 67, 20));

        tglGAME2v3.setText(peace);
        thisWar.add(tglGAME2v3, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 60, 67, 20));

        jToggleButton11.setText(peace);
        jToggleButton11.setEnabled(false);
        thisWar.add(jToggleButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, -1, 20));

        tglGAME4v3.setText(peace);
        thisWar.add(tglGAME4v3, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 100, 67, 20));

        tglGAME1v4.setText(peace);
        thisWar.add(tglGAME1v4, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 40, 67, 20));

        tglGAME2v4.setText(peace);
        thisWar.add(tglGAME2v4, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 60, 67, 20));

        tglGAME3v4.setText(peace);
        thisWar.add(tglGAME3v4, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 80, 67, 20));

        jToggleButton16.setText(peace);
        jToggleButton16.setEnabled(false);
        thisWar.add(jToggleButton16, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 100, -1, 20));

        thisLockedAlliances.add(thisWar, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 10, 320, 130));
        //end war section
        thisVictoryType.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Victory Type"));

        rbtnGAMEIndividualVictory.setText("Individual");

        rbtnGAMECoalitionVictory.setText("Coalition");

        org.jdesktop.layout.GroupLayout thisVictoryTypeLayout = new org.jdesktop.layout.GroupLayout(thisVictoryType);
        thisVictoryType.setLayout(thisVictoryTypeLayout);
        thisVictoryTypeLayout.setHorizontalGroup(
            thisVictoryTypeLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(thisVictoryTypeLayout.createSequentialGroup()
                .add(rbtnGAMEIndividualVictory)
                .add(18, 18, 18)
                .add(rbtnGAMECoalitionVictory)
                .addContainerGap(38, Short.MAX_VALUE))
        );
        thisVictoryTypeLayout.setVerticalGroup(
            thisVictoryTypeLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(thisVictoryTypeLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(rbtnGAMEIndividualVictory)
                .add(rbtnGAMECoalitionVictory))
        );
        
        //add action listeners for toggle buttonsA
        tglGAME1v2.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                if (tglGAME1v2.isSelected())    //it's war now!
                {
                    tglGAME1v2.setText(war);
                    tglGAME2v1.setText(war);
                    tglGAME2v1.setSelected(true);
                }
                else    //why can't we all live in peace?
                {
                    tglGAME1v2.setText(peace);
                    tglGAME2v1.setText(peace);
                    tglGAME2v1.setSelected(false);
                }
            }
        });
        tglGAME1v3.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                if (tglGAME1v3.isSelected())    //it's war now!
                {
                    tglGAME1v3.setText(war);
                    tglGAME3v1.setText(war);
                    tglGAME3v1.setSelected(true);
                }
                else    //why can't we all live in peace?
                {
                    tglGAME1v3.setText(peace);
                    tglGAME3v1.setText(peace);
                    tglGAME3v1.setSelected(false);
                }
            }
        });
        tglGAME1v4.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                if (tglGAME1v4.isSelected())    //it's war now!
                {
                    tglGAME1v4.setText(war);
                    tglGAME4v1.setText(war);
                    tglGAME4v1.setSelected(true);
                }
                else    //why can't we all live in peace?
                {
                    tglGAME1v4.setText(peace);
                    tglGAME4v1.setText(peace);
                    tglGAME4v1.setSelected(false);
                }
            }
        });
        tglGAME2v1.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                if (tglGAME2v1.isSelected())    //it's war now!
                {
                    tglGAME2v1.setText(war);
                    tglGAME1v2.setText(war);
                    tglGAME1v2.setSelected(true);
                }
                else    //why can't we all live in peace?
                {
                    tglGAME2v1.setText(peace);
                    tglGAME1v2.setText(peace);
                    tglGAME1v2.setSelected(false);
                }
            }
        });
        tglGAME2v3.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                if (tglGAME2v3.isSelected())    //it's war now!
                {
                    tglGAME2v3.setText(war);
                    tglGAME3v2.setText(war);
                    tglGAME3v2.setSelected(true);
                }
                else    //why can't we all live in peace?
                {
                    tglGAME2v3.setText(peace);
                    tglGAME3v2.setText(peace);
                    tglGAME3v2.setSelected(false);
                }
            }
        });
        tglGAME2v4.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                if (tglGAME2v4.isSelected())    //it's war now!
                {
                    tglGAME2v4.setText(war);
                    tglGAME4v2.setText(war);
                    tglGAME4v2.setSelected(true);
                }
                else    //why can't we all live in peace?
                {
                    tglGAME2v4.setText(peace);
                    tglGAME4v2.setText(peace);
                    tglGAME4v2.setSelected(false);
                }
            }
        });
        tglGAME3v1.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                if (tglGAME3v1.isSelected())    //it's war now!
                {
                    tglGAME3v1.setText(war);
                    tglGAME1v3.setText(war);
                    tglGAME1v3.setSelected(true);
                }
                else    //why can't we all live in peace?
                {
                    tglGAME3v1.setText(peace);
                    tglGAME1v3.setText(peace);
                    tglGAME1v3.setSelected(false);
                }
            }
        });
        tglGAME3v2.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                if (tglGAME3v2.isSelected())    //it's war now!
                {
                    tglGAME3v2.setText(war);
                    tglGAME2v3.setText(war);
                    tglGAME2v3.setSelected(true);
                }
                else    //why can't we all live in peace?
                {
                    tglGAME3v2.setText(peace);
                    tglGAME2v3.setText(peace);
                    tglGAME2v3.setSelected(false);
                }
            }
        });
        tglGAME3v4.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                if (tglGAME3v4.isSelected())    //it's war now!
                {
                    tglGAME3v4.setText(war);
                    tglGAME4v3.setText(war);
                    tglGAME4v3.setSelected(true);
                }
                else    //why can't we all live in peace?
                {
                    tglGAME3v4.setText(peace);
                    tglGAME4v3.setText(peace);
                    tglGAME4v3.setSelected(false);
                }
            }
        });
        tglGAME4v1.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                if (tglGAME4v1.isSelected())    //it's war now!
                {
                    tglGAME4v1.setText(war);
                    tglGAME1v4.setText(war);
                    tglGAME1v4.setSelected(true);
                }
                else    //why can't we all live in peace?
                {
                    tglGAME4v1.setText(peace);
                    tglGAME1v4.setText(peace);
                    tglGAME1v4.setSelected(false);
                }
            }
        });
        tglGAME4v2.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                if (tglGAME4v2.isSelected())    //it's war now!
                {
                    tglGAME4v2.setText(war);
                    tglGAME2v4.setText(war);
                    tglGAME2v4.setSelected(true);
                }
                else    //why can't we all live in peace?
                {
                    tglGAME4v2.setText(peace);
                    tglGAME2v4.setText(peace);
                    tglGAME2v4.setSelected(false);
                }
            }
        });
        tglGAME4v3.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                if (tglGAME4v3.isSelected())    //it's war now!
                {
                    tglGAME4v3.setText(war);
                    tglGAME3v4.setText(war);
                    tglGAME3v4.setSelected(true);
                }
                else    //why can't we all live in peace?
                {
                    tglGAME4v3.setText(peace);
                    tglGAME3v4.setText(peace);
                    tglGAME3v4.setSelected(false);
                }
            }
        });

        lstGAMENoAlliances.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                moveToAlliancePopup(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                moveToAlliancePopup(evt);
            }
        });
        lstGAMEAlliance1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                moveToAlliancePopup(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                moveToAlliancePopup(evt);
            }
        });
        lstGAMEAlliance2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                moveToAlliancePopup(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                moveToAlliancePopup(evt);
            }
        });
        lstGAMEAlliance3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                moveToAlliancePopup(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                moveToAlliancePopup(evt);
            }
        });
        lstGAMEAlliance4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                moveToAlliancePopup(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                moveToAlliancePopup(evt);
            }
        });

        moveToAlliance0 = new JMenuItem("Remove from all alliances");
        moveToAlliance0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String civName = (String)sourceList.getModel().getElementAt(selectedCiv);
                if (logger.isInfoEnabled())
                    logger.info("Civ to remove: " + civName);
                removeFromList(sourceList, selectedCiv);
                alliance0Model.addElement(civName);
            }
        });
        moveToAlliance1 = new JMenuItem("Add to alliance 1");
        moveToAlliance1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String civName = (String)sourceList.getModel().getElementAt(selectedCiv);
                if (logger.isInfoEnabled())
                    logger.info("Civ to add to alliance 1: " + civName);
                removeFromList(sourceList, selectedCiv);
                alliance1Model.addElement(civName);
            }
        });
        moveToAlliance2 = new JMenuItem("Add to alliance 2");
        moveToAlliance2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String civName = (String)sourceList.getModel().getElementAt(selectedCiv);
                if (logger.isInfoEnabled())
                    logger.info("Civ to add to alliance 2: " + civName);
                removeFromList(sourceList, selectedCiv);
                alliance2Model.addElement(civName);
            }
        });
        moveToAlliance3 = new JMenuItem("Add to alliance 3");
        moveToAlliance3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String civName = (String)sourceList.getModel().getElementAt(selectedCiv);
                if (logger.isInfoEnabled())
                    logger.info("Civ to add to alliance 3: " + civName);
                removeFromList(sourceList, selectedCiv);
                alliance3Model.addElement(civName);
            }
        });
        moveToAlliance4 = new JMenuItem("Add to alliance 4");
        moveToAlliance4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String civName = (String)sourceList.getModel().getElementAt(selectedCiv);
                if (logger.isInfoEnabled())
                    logger.info("Civ to add to alliance 4: " + civName);
                removeFromList(sourceList, selectedCiv);
                alliance4Model.addElement(civName);
            }
        });
        /**
         *
         * Code for a custom JOptionPane
                final JPanel frame = new JPanel();
                frame.setLayout(new BoxLayout(frame,BoxLayout.X_AXIS));
                frame.setSize(300, 20);
                frame.setPreferredSize(new java.awt.Dimension(300, 20));
                SuperJTextField entry = new SuperJTextField();
                entry.setPreferredSize(new java.awt.Dimension(200, 20));
                addLengthDocumentListener(255, entry);
                frame.add(entry);
                Object[]option = {frame, "Okay"};
                int name = JOptionPane.showOptionDialog(null, "Enter the new name of the aliance", "New Alliance Name", JOptionPane.PLAIN_MESSAGE, JOptionPane.QUESTION_MESSAGE, null, option, null);
                //JOptionPane.showMessageDialog(null, name);
                //JOptionPane.showMessageDialog(null, entry.getText());
                if (name != -1)
                    lblToRename.setText(entry.getText());
         */
        lstGAMENoAlliances.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstGAMEAlliance1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstGAMEAlliance2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstGAMEAlliance3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstGAMEAlliance4.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        thisLockedAlliances.add(thisVictoryType, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 140, -1, -1));

        this.add(thisLockedAlliances, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 470, 1020, 200));

        txtVERDescription.setLineWrap(true);

        this.setName(tabName);

        tabCreated = true;
        return this;
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
     * This version is only called if a custom map exists.
     *
     * @param worldCharacteristics - The world characteristics.
     */    
    public void sendMapData(List<WCHR>worldCharacteristics)
    {
        this.worldChar = worldCharacteristics;
        //displayData();  //TODO: Make displayData smarter so it displays only map-specific stuff when appropriate
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
     * @param prop - The list of all scenario properties.
     * @param civ - The list of all civilizations.
     */
    public void sendRuleData(List<GAME>prop, List<RACE>civ)
    {
        this.prop = prop;
        this.civ = civ;
        propIndex = 0;
        displayData();
    }

    public void updateTab()
    {
        if (logger.isDebugEnabled())
            logger.debug("propIndex: " + propIndex);
        storeData();
        propIndex = 0;  //just to be sure we do save the data later
        displayData();
    }
    
    public void updateSearchFolders() {
        txtGAMEScenarioSearchFolders.setText(prop.get(0).getScenarioSearchFolders());
    }

    private void storeData()
    {

        if (!(propIndex == -1))
        {
            if (logger.isDebugEnabled())
                logger.debug("UPDATING GAME TAB");
            //We must use the baselink to change the title.  As the pointer to
            //the String is *copied* into this class, creating a new String here
            //doesn't change the pointer in the base IO class.  By accessing the
            //title via the actual IO instance, we can change its pointer to
            //the title.  The same applies to the description.
            baselink.title = txtVERTitle.getText();
            baselink.description = txtVERDescription.getText();
            if (chkGAMEUseDefaultRules.isSelected())
                prop.get(propIndex).setUseDefaultRules(1);  //use defaults
            else
                prop.get(propIndex).setUseDefaultRules(0);  //not defaults
            if (chkGAMEDefaultVictoryConditions.isSelected())
                prop.get(propIndex).setDefaultVictoryConditions(1);  //use defaults
            else
                prop.get(propIndex).setDefaultVictoryConditions(0);  //not defaults
            //num playable civs done on players tab
            //get victory condition settings
            //first, set all to false
            prop.get(propIndex).victoryConditionsAndRules = 0;
            prop.get(propIndex).dominationEnabled = false;
            prop.get(propIndex).spaceRaceEnabled = false;
            prop.get(propIndex).diplomacticEnabled = false;
            prop.get(propIndex).conquestEnabled = false;
            prop.get(propIndex).culturalEnabled = false;
            prop.get(propIndex).wonderVictoryEnabled = false;
            Object[] victoryConditions = {"Hi!"};
            try{
                victoryConditions = lstGAMEVictoryConditions.getSelectedValues();
            }
            catch(Exception e)
            {
                logger.error("Exception! : ", e);
                logger.error("When trying to get selected values");
            }
            for (int i = 0; i < victoryConditions.length; i++)
            {
                if (victoryConditions[i].equals("Domination"))
                {
                    prop.get(propIndex).dominationEnabled = true;
                    prop.get(propIndex).victoryConditionsAndRules+=1;
                }
                if (victoryConditions[i].equals("Space Race"))
                {
                    prop.get(propIndex).spaceRaceEnabled = true;
                    prop.get(propIndex).victoryConditionsAndRules+=2;
                }
                if (victoryConditions[i].equals("Diplomatic"))
                {
                    prop.get(propIndex).diplomacticEnabled = true;
                    prop.get(propIndex).victoryConditionsAndRules+=4;
                }
                if (victoryConditions[i].equals("Conquest"))
                {
                    prop.get(propIndex).conquestEnabled = true;
                    prop.get(propIndex).victoryConditionsAndRules+=8;
                }
                if (victoryConditions[i].equals("Cultural"))
                {
                    prop.get(propIndex).culturalEnabled = true;
                    prop.get(propIndex).victoryConditionsAndRules+=16;
                }
                if (victoryConditions[i].equals("Wonder"))
                {
                    prop.get(propIndex).wonderVictoryEnabled = true;
                    prop.get(propIndex).victoryConditionsAndRules+=65536;
                }
            }
            //get rule settings
            //first, set all to false
            prop.get(propIndex).civSpecificAbilitiesEnabled = false;
            prop.get(propIndex).culturallyLinkedStart = false;
            prop.get(propIndex).restartPlayersEnabled = false;
            prop.get(propIndex).preserveRandomSeed = false;
            prop.get(propIndex).acceleratedProduction = false;
            prop.get(propIndex).eliminationEnabled = false;
            prop.get(propIndex).regicideEnabled = false;
            prop.get(propIndex).massRegicideEnabled = false;
            prop.get(propIndex).victoryLocationsEnabled = false;
            prop.get(propIndex).captureTheFlag = false;
            prop.get(propIndex).allowCulturalConversions = false;
            prop.get(propIndex).reverseCaptureTheFlag = false;
            Object[]rules = lstGAMERulesSelected.getSelectedValues();
            for (int i = 0; i < rules.length; i++)
            {
                if (rules[i].equals("Civ specific abilities"))
                {
                    prop.get(propIndex).civSpecificAbilitiesEnabled = true;
                    prop.get(propIndex).victoryConditionsAndRules+=32;
                }
                if (rules[i].equals("Culturally Linked Start"))
                {
                    prop.get(propIndex).culturallyLinkedStart = true;
                    prop.get(propIndex).victoryConditionsAndRules+=64;
                }
                if (rules[i].equals("Respawn AI Players"))
                {
                    prop.get(propIndex).restartPlayersEnabled = true;
                    prop.get(propIndex).victoryConditionsAndRules+=128;
                }
                if (rules[i].equals("Preserve Random Seed"))
                {
                    prop.get(propIndex).preserveRandomSeed = true;
                    prop.get(propIndex).victoryConditionsAndRules+=256;
                }
                if (rules[i].equals("Accelerated Production"))
                {
                    prop.get(propIndex).acceleratedProduction = true;
                    prop.get(propIndex).victoryConditionsAndRules+=512;
                }
                if (rules[i].equals("City Elimination"))
                {
                    prop.get(propIndex).eliminationEnabled = true;
                    prop.get(propIndex).victoryConditionsAndRules+=1024;
                }
                if (rules[i].equals("Regicide"))
                {
                    prop.get(propIndex).regicideEnabled = true;
                    prop.get(propIndex).victoryConditionsAndRules+=2048;
                }
                if (rules[i].equals("Regicide (all Kings)"))
                {
                    prop.get(propIndex).massRegicideEnabled = true;
                    prop.get(propIndex).victoryConditionsAndRules+=4096;
                }
                if (rules[i].equals("Victory Point Scoring"))
                {
                    prop.get(propIndex).victoryLocationsEnabled = true;
                    prop.get(propIndex).victoryConditionsAndRules+=8192;
                }
                if (rules[i].equals("Capture the Unit"))
                {
                    prop.get(propIndex).captureTheFlag = true;
                    prop.get(propIndex).victoryConditionsAndRules+=16384;
                }
                if (rules[i].equals("Allow Cultural Conversions"))
                {
                    prop.get(propIndex).allowCulturalConversions = true;
                    prop.get(propIndex).victoryConditionsAndRules+=32768;
                }
                if (rules[i].equals("Reverse Capture the Flag"))
                {
                    prop.get(propIndex).regicideEnabled = true;
                    prop.get(propIndex).victoryConditionsAndRules+=131072;
                }
                if (rules[i].equals("Allow Scientific Leaders"))
                {
                    prop.get(propIndex).scientificLeaders = true;
                    prop.get(propIndex).victoryConditionsAndRules+=262144;
                }
            }
            if (chkGAMEPlaceCaptureUnits.isSelected())
            {
                prop.get(propIndex).setPlaceCaptureUnits(1);
            }
            else
            {
                prop.get(propIndex).setPlaceCaptureUnits(0);
            }
            if (chkGAMEAutoPlaceKings.isSelected())
            {
                prop.get(propIndex).setAutoPlaceKings(1);
            }
            else
            {
                prop.get(propIndex).setAutoPlaceKings(0);
            }
            if (chkGAMEAutoPlaceVictoryLocations.isSelected())
            {
                prop.get(propIndex).setAutoPlaceVictoryLocations(1);
            }
            else
            {
                prop.get(propIndex).setAutoPlaceVictoryLocations(0);
            }
            if (chkGAMEDebugMode.isSelected())
            {
                prop.get(propIndex).setDebugMode(1);
            }
            else
            {
                prop.get(propIndex).setDebugMode(0);
            }
            if (chkGAMEUseTimeLimit.isSelected())
            {
                prop.get(propIndex).setUseTimeLimit(1);
            }
            else
            {
                prop.get(propIndex).setUseTimeLimit(0);
            }
            prop.get(propIndex).setBaseTimeUnit(cmbGAMEBaseTimeUnit.getSelectedIndex());
            //TO BE CHECKED: That this won't cause problems, as only one should logically be selected at a time
            if (cmbGAMEBaseTimeUnit.getSelectedIndex() == 1)   //month
                prop.get(propIndex).setStartMonth(cmbGAMEMonthWeek.getSelectedIndex() + 1);
            else if (cmbGAMEBaseTimeUnit.getSelectedIndex() == 2)   //week
                prop.get(propIndex).setStartWeek(cmbGAMEMonthWeek.getSelectedIndex() + 1);
            if (this.cmbGAME_ADBC.getSelectedIndex() == 0) //BC
                prop.get(propIndex).setStartYear(-1 * txtGAMEStartYear.getInteger());
            else
                prop.get(propIndex).setStartYear(txtGAMEStartYear.getInteger());
            prop.get(propIndex).setMinuteTimeLimit(txtGAMEMinuteTimeLimit.getInteger());
            prop.get(propIndex).setTurnTimeLimit(txtGAMETurnTimeLimit.getInteger());
            prop.get(propIndex).setScenarioSearchFolders(txtGAMEScenarioSearchFolders.getText());
            prop.get(propIndex).setVictoryPointLimit(txtGAMEVictoryPointLimit.getInteger());
            prop.get(propIndex).setCityEliminationCount(txtGAMECityEliminationCount.getInteger());
            prop.get(propIndex).setOneCityCultureWinLimit(txtGAMEOneCityCultureWinLimit.getInteger());
            prop.get(propIndex).setAllCitiesCultureWinLimit(txtGAMEAllCitiesCultureWinLimit.getInteger());
            prop.get(propIndex).setDominationTerrainPercent(txtGAMEDominationTerrainPercent.getInteger());
            prop.get(propIndex).setDominationPopulationPercent(txtGAMEDominationPopulationPercent.getInteger());
            prop.get(propIndex).setWonderVP(txtGAMEWonderVP.getInteger());
            prop.get(propIndex).setDefeatingOpposingUnitVP(txtGAMEDefeatingOpposingUnitVP.getInteger());
            prop.get(propIndex).setAdvancementVP(txtGAMEAdvancementVP.getInteger());
            prop.get(propIndex).setCityConquestVP(txtGAMECityConquestVP.getInteger());
            prop.get(propIndex).setVictoryPointVP(txtGAMEVictoryPointVP.getInteger());
            prop.get(propIndex).setCaptureSpecialUnitVP(txtGAMECaptureSpecialUnitVP.getInteger());
            //TO BE ADDED: Question Mark Support
            //prop.get(propIndex).setQuestionMark1(txtGAMEQuestionMark1.getText()));
            //prop.get(propIndex).setQuestionMark2(chkGAMEQuestionMark2.isSelected());
            if (rbtnGAMEIndividualVictory.isSelected())
                prop.get(propIndex).setAllianceVictoryType(0);
            else    //coalition victory
                prop.get(propIndex).setAllianceVictoryType(1);
            prop.get(propIndex).setPlaugeName(txtGAMEPlagueName.getText());
            if (chkGAMEPermitPlagues.isSelected())
            {
                prop.get(propIndex).setPermitPlagues((byte)1);
            }
            else
            {
                prop.get(propIndex).setPermitPlagues((byte)0);
            }
            prop.get(propIndex).setPlagueEarliestStart(txtGAMEPlagueEarliestStart.getInteger());
            prop.get(propIndex).setPlagueVariation(txtGAMEPlagueVariation.getInteger());
            prop.get(propIndex).setPlagueDuration(txtGAMEPlagueDuration.getInteger());
            prop.get(propIndex).setPlagueStrength(txtGAMEPlagueStrength.getInteger());
            prop.get(propIndex).setPlagueGracePeriod(txtGAMEPlagueGracePeriod.getInteger());
            prop.get(propIndex).setPlagueMaxOccurance(txtGAMEPlagueMaxOccurance.getInteger());
            //prop.get(propIndex).setQuestionMark3(txtGAMEQuestionMark3.getText()));
            //prop.get(propIndex).setUnknown(txtGAMEUnknown.getText());
            if (chkGAMERespawnFlagUnits.isSelected())
            {
                prop.get(propIndex).setRespawnFlagUnits(1);
            }
            else
            {
                prop.get(propIndex).setRespawnFlagUnits(0);
            }
            if (chkGAMECaptureAnyFlag.isSelected())
            {
                prop.get(propIndex).setCaptureAnyFlag((byte)1);
            }
            else
            {
                prop.get(propIndex).setCaptureAnyFlag((byte)0);
            }
            if (chkGAMEMapVisible.isSelected())
            {
                prop.get(propIndex).setMapVisible((byte)1);
            }
            else
            {
                prop.get(propIndex).setMapVisible((byte)0);
            }
            if (chkGAMERetainCulture.isSelected())
            {
                prop.get(propIndex).setRetainCulture((byte)1);
            }
            else
            {
                prop.get(propIndex).setRetainCulture((byte)0);
            }
            prop.get(propIndex).setGoldForCapture(txtGAMEGoldForCapture.getInteger());
            //prop.get(propIndex).setQuestionMark4(txtGAMEQuestionMark4.getText()));
            prop.get(propIndex).setEruptionPeriod(txtGAMEEruptionPeriod.getInteger());
            prop.get(propIndex).setMpBaseTime(txtGAMEMpBaseTime.getInteger());
            prop.get(propIndex).setMpCityTime(txtGAMEMpCityTime.getInteger());
            prop.get(propIndex).setMpUnitTime(txtGAMEMpUnitTime.getInteger());
            //save game turn sections
            prop.get(propIndex).turnsPerTimescalePart[0] = txtGAMETurnGroup1.getInteger();
            prop.get(propIndex).turnsPerTimescalePart[1] = txtGAMETurnGroup2.getInteger();
            prop.get(propIndex).turnsPerTimescalePart[2] = txtGAMETurnGroup3.getInteger();
            prop.get(propIndex).turnsPerTimescalePart[3] = txtGAMETurnGroup4.getInteger();
            prop.get(propIndex).turnsPerTimescalePart[4] = txtGAMETurnGroup5.getInteger();
            prop.get(propIndex).turnsPerTimescalePart[5] = txtGAMETurnGroup6.getInteger();
            prop.get(propIndex).turnsPerTimescalePart[6] = txtGAMETurnGroup7.getInteger();
            prop.get(propIndex).timeUnitsPerTurn[0] = txtGAMENumTurns1.getInteger();
            prop.get(propIndex).timeUnitsPerTurn[1] = txtGAMENumTurns2.getInteger();
            prop.get(propIndex).timeUnitsPerTurn[2] = txtGAMENumTurns3.getInteger();
            prop.get(propIndex).timeUnitsPerTurn[3] = txtGAMENumTurns4.getInteger();
            prop.get(propIndex).timeUnitsPerTurn[4] = txtGAMENumTurns5.getInteger();
            prop.get(propIndex).timeUnitsPerTurn[5] = txtGAMENumTurns6.getInteger();
            prop.get(propIndex).timeUnitsPerTurn[6] = txtGAMENumTurns7.getInteger();

            //save alliance information
            prop.get(propIndex).civPartOfWhichAlliance = new ArrayList<Integer>();
            //numberOfPlayableCivs is zero if all are playable
            int numCivsInScenario = prop.get(propIndex).numberOfPlayableCivs == 0 ? civ.size() - 1: prop.get(propIndex).numberOfPlayableCivs;
            if (logger.isDebugEnabled())
                logger.debug("Number of playable civs: " + numCivsInScenario);
            if (prop.get(propIndex).numberOfPlayableCivs != 0)
            {
                for (int i = 0; i < numCivsInScenario; i++)
                {
                    String name = civ.get(prop.get(propIndex).idOfPlayableCivs.get(i)).getName();
                    if (logger.isDebugEnabled())
                        logger.debug("Searching for " + name + "...");
                    if (alliance0Model.contains(name))
                    {
                        prop.get(propIndex).civPartOfWhichAlliance.add(0);
                        continue;
                    }
                    if (alliance1Model.contains(name))
                    {
                        prop.get(propIndex).civPartOfWhichAlliance.add(1);
                        continue;
                    }
                    if (alliance2Model.contains(name))
                    {
                        prop.get(propIndex).civPartOfWhichAlliance.add(2);
                        continue;
                    }
                    if (alliance3Model.contains(name))
                    {
                        prop.get(propIndex).civPartOfWhichAlliance.add(3);
                        continue;
                    }
                    if (alliance4Model.contains(name))
                    {
                        prop.get(propIndex).civPartOfWhichAlliance.add(4);
                        continue;
                    }
                    logger.warn("Didn't find " + name + " in any alliances!");
                }
            }
            else
            {
                for (int i = 0; i < numCivsInScenario; i++)
                {
                    String name = civ.get(i + 1).getName();
                    if (logger.isDebugEnabled())
                        logger.debug("Searching for " + name + "...");
                    if (alliance0Model.contains(name))
                    {
                        if (logger.isDebugEnabled())
                            logger.debug("Found " + name + " in alliance 0");
                        prop.get(propIndex).civPartOfWhichAlliance.add(0);
                        continue;
                    }
                    if (alliance1Model.contains(name))
                    {
                        if (logger.isDebugEnabled())
                            logger.debug("Found " + name + " in alliance 1");
                        prop.get(propIndex).civPartOfWhichAlliance.add(1);
                        continue;
                    }
                    if (alliance2Model.contains(name))
                    {
                        if (logger.isDebugEnabled())
                            logger.debug("Found " + name + " in alliance 2");
                        prop.get(propIndex).civPartOfWhichAlliance.add(2);
                        continue;
                    }
                    if (alliance3Model.contains(name))
                    {
                        if (logger.isDebugEnabled())
                            logger.debug("Found " + name + " in alliance 3");
                        prop.get(propIndex).civPartOfWhichAlliance.add(3);
                        continue;
                    }
                    if (alliance4Model.contains(name))
                    {
                        if (logger.isDebugEnabled())
                            logger.debug("Found " + name + " in alliance 4");
                        prop.get(propIndex).civPartOfWhichAlliance.add(4);
                        continue;
                    }
                    //Note the continues above - this only gets excuted if none
                    //of the cases fire
                    logger.warn("Didn't find " + name + " in any alliances!");
                }
            }
            if (baselink.hasCustomMap())
            {
                worldChar.get(0).setSelectedBarbarianActivity(cmbWCHRBarbarianActivity.getSelectedIndex() - 1);
            }
            if (baselink.version == civ3Version.CONQUESTS)
            {
                //store war/peace alliance info
                //war is 1, peace is 0
                if (tglGAME1v2.isSelected())    //war
                {
                    prop.get(propIndex).warWith1.set(2, 1);
                    prop.get(propIndex).warWith2.set(1, 1);
                }
                else
                {
                    prop.get(propIndex).warWith1.set(2, 0);
                    prop.get(propIndex).warWith2.set(1, 0);
                }
                if (tglGAME1v3.isSelected())    //war
                {
                    prop.get(propIndex).warWith1.set(3, 1);
                    prop.get(propIndex).warWith3.set(1, 1);
                }
                else
                {
                    prop.get(propIndex).warWith1.set(3, 0);
                    prop.get(propIndex).warWith3.set(1, 0);
                }
                if (tglGAME1v4.isSelected())    //war
                {
                    prop.get(propIndex).warWith1.set(4, 1);
                    prop.get(propIndex).warWith4.set(1, 1);
                }
                else
                {
                    prop.get(propIndex).warWith1.set(4, 0);
                    prop.get(propIndex).warWith4.set(1, 0);
                }
                if (tglGAME2v3.isSelected())    //war
                {
                    prop.get(propIndex).warWith2.set(3, 1);
                    prop.get(propIndex).warWith3.set(2, 1);
                }
                else
                {
                    prop.get(propIndex).warWith2.set(3, 0);
                    prop.get(propIndex).warWith3.set(2, 0);
                }
                if (tglGAME2v4.isSelected())    //war
                {
                    prop.get(propIndex).warWith2.set(4, 1);
                    prop.get(propIndex).warWith4.set(2, 1);
                }
                else
                {
                    prop.get(propIndex).warWith2.set(4, 0);
                    prop.get(propIndex).warWith4.set(2, 0);
                }
                if (tglGAME3v4.isSelected())    //war
                {
                    prop.get(propIndex).warWith3.set(4, 1);
                    prop.get(propIndex).warWith4.set(3, 1);
                }
                else
                {
                    prop.get(propIndex).warWith3.set(4, 0);
                    prop.get(propIndex).warWith4.set(3, 0);
                }
                prop.get(propIndex).alliance0 = lblGAMEAlliance0Name.getText();
                prop.get(propIndex).alliance1 = lblGAMEAlliance1Name.getText();
                prop.get(propIndex).alliance2 = lblGAMEAlliance2Name.getText();
                prop.get(propIndex).alliance3 = lblGAMEAlliance3Name.getText();
                prop.get(propIndex).alliance4 = lblGAMEAlliance4Name.getText();
            }
        }
    }

    private void displayData()
    {
        txtVERTitle.setText(baselink.title);
        txtVERDescription.setText(baselink.description);
        if (prop.get(propIndex).getUseDefaultRules() == 1)
        {
            chkGAMEUseDefaultRules.setSelected(true);
        }
        else
        {
            chkGAMEUseDefaultRules.setSelected(false);
        }
        if (prop.get(propIndex).getDefaultVictoryConditions() == 1)
        {
            chkGAMEDefaultVictoryConditions.setSelected(true);
        }
        else
        {
            chkGAMEDefaultVictoryConditions.setSelected(false);
        }
        //TO BE ADDED - perhaps rule-sorting.  Perhaps - not
        //% will work properly as modulus here - if the number were negative, it
        //would return -1 or 0.  Note that our number should never go negative
        //Note: Very similar code appears twice in short succession here.  This
        //is not a copy-paste error; rather, the victoryConditionsAndRules are
        //accessed once for the victory conditions, and then quickly again for
        //the rules.
        int victCondCopy = prop.get(propIndex).getVictoryConditionsAndRules();
        List<Integer>toSelect = new ArrayList<Integer>(0);
        if (victCondCopy % 2 != 0)
        {
            toSelect.add(0);
        }
        victCondCopy = victCondCopy >>> 1;   //shift right by one
        if (victCondCopy % 2 != 0)
        {
            toSelect.add(1);
        }
        victCondCopy = victCondCopy >>> 1;   //shift right by one
        if (victCondCopy % 2 != 0)
        {
            toSelect.add(2);
        }
        victCondCopy = victCondCopy >>> 1;   //shift right by one
        if (victCondCopy % 2 != 0)
        {
            toSelect.add(3);
        }
        victCondCopy = victCondCopy >>> 1;   //shift right by one
        if (victCondCopy % 2 != 0)
        {
            toSelect.add(4);
        }
        victCondCopy = victCondCopy >>> 12;   //shift right by twelve
        if (victCondCopy % 2 != 0)            //wonder victory
        {
            toSelect.add(5);
        }
        //victory conditions ascertained
        Object[]objArray = toSelect.toArray();
        if (objArray.length > 0)
        {
            Integer[]toSelectArray = new Integer[objArray.length];
            System.arraycopy(objArray, 0, toSelectArray, 0, objArray.length);
            //must be manually converted to primitives
            lstGAMEVictoryConditions.setSelectedIndices(ObjectToPrimitive.intToInt(toSelectArray));
        }
        else
            lstGAMEVictoryConditions.clearSelection();
        //Now set the rules list
        victCondCopy = prop.get(propIndex).getVictoryConditionsAndRules();
        toSelect = new ArrayList<Integer>(0);
        victCondCopy = victCondCopy >>> 5;  //skip first 5 bits, go to rules section
        if (victCondCopy % 2 != 0)          //first is civ-specific abilities
        {
            toSelect.add(0);
        }
        victCondCopy = victCondCopy >>> 1;   //shift right by one
        if (victCondCopy % 2 != 0)          //culturally linked start
        {
            toSelect.add(1);
        }
        victCondCopy = victCondCopy >>> 1;   //shift right by one
        if (victCondCopy % 2 != 0)          //restart players
        {
            toSelect.add(2);
        }
        victCondCopy = victCondCopy >>> 1;   //shift right by one
        if (victCondCopy % 2 != 0)          //preserve random seed
        {
            toSelect.add(3);
        }
        victCondCopy = victCondCopy >>> 1;   //shift right by one
        if (victCondCopy % 2 != 0)          //accelerated production
        {
            toSelect.add(4);
        }
        victCondCopy = victCondCopy >>> 1;   //shift right by one
        if (victCondCopy % 2 != 0)          //elimination enabled
        {
            toSelect.add(5);
        }
        victCondCopy = victCondCopy >>> 1;   //shift right by one
        if (victCondCopy % 2 != 0)          //regicide
        {
            toSelect.add(6);
        }
        victCondCopy = victCondCopy >>> 1;   //shift right by one
        if (victCondCopy % 2 != 0)
        {                                   //mass regicide
            toSelect.add(7);
        }
        victCondCopy = victCondCopy >>> 1;   //shift right by one
        if (victCondCopy % 2 != 0)          //victory locations
        {
            toSelect.add(8);
        }
        victCondCopy = victCondCopy >>> 1;   //shift right by one
        if (victCondCopy % 2 != 0)          //capture the flag
        {
            toSelect.add(9);
        }
        victCondCopy = victCondCopy >>> 1;   //shift right by one
        if (victCondCopy % 2 != 0)          //cultural conversions
        {
            toSelect.add(10);
        }
        victCondCopy = victCondCopy >>> 2;   //shift right by two (skip wonder victory)
        if (victCondCopy % 2 != 0)          //reverse capture the flag
        {
            toSelect.add(11);
        }
        victCondCopy = victCondCopy >>> 1;   //shift right by one
        if (victCondCopy % 2 != 0)          //scientific leaders
        {
            toSelect.add(12);
        }
        objArray = toSelect.toArray();
        if (objArray.length > 0)
        {
            Integer[]toSelectArray = new Integer[objArray.length];
            System.arraycopy(objArray, 0, toSelectArray, 0, objArray.length);
            //must be manually converted to primitives
            lstGAMERulesSelected.setSelectedIndices(ObjectToPrimitive.intToInt(toSelectArray));
        }
        else
            lstGAMERulesSelected.clearSelection();
        //finishes this section
        if (prop.get(propIndex).getPlaceCaptureUnits() == 1)
        {
            chkGAMEPlaceCaptureUnits.setSelected(true);
        }
        else
        {
            chkGAMEPlaceCaptureUnits.setSelected(false);
        }
        if (prop.get(propIndex).getAutoPlaceKings() == 1)
        {
            chkGAMEAutoPlaceKings.setSelected(true);
        }
        else
        {
            chkGAMEAutoPlaceKings.setSelected(false);
        }
        if (prop.get(propIndex).getAutoPlaceVictoryLocations() == 1)
        {
            chkGAMEAutoPlaceVictoryLocations.setSelected(true);
        }
        else
        {
            chkGAMEAutoPlaceVictoryLocations.setSelected(false);
        }
        if (prop.get(propIndex).getDebugMode() == 1)
        {
            chkGAMEDebugMode.setSelected(true);
        }
        else
        {
            chkGAMEDebugMode.setSelected(false);
        }
        if (prop.get(propIndex).getUseTimeLimit() == 1)
        {
            chkGAMEUseTimeLimit.setSelected(true);
        }
        else
        {
            chkGAMEUseTimeLimit.setSelected(false);
        }
        cmbGAMEBaseTimeUnit.setSelectedIndex(prop.get(propIndex).getBaseTimeUnit());
        //CHECK: This won't cause a problem with weeks
        if (prop.get(propIndex).getBaseTimeUnit() == 1)
        {
            lblGAMEMonthWeek.setText("Month");
            cmbGAMEMonthWeek.setModel(monthModel);
            cmbGAMEMonthWeek.setSelectedIndex(prop.get(propIndex).getStartMonth() - 1);
        }
        else if (prop.get(propIndex).getBaseTimeUnit() == 2)
        {
            lblGAMEMonthWeek.setText("Week");
            cmbGAMEMonthWeek.setModel(weekModel);
            cmbGAMEMonthWeek.setSelectedIndex(prop.get(propIndex).getStartWeek() - 1);
        }
        txtGAMEStartYear.setText(Integer.toString(Math.abs(prop.get(propIndex).getStartYear())));
        if (prop.get(propIndex).getStartYear() < 0)
            cmbGAME_ADBC.setSelectedIndex(0);
        else
            cmbGAME_ADBC.setSelectedIndex(1);
        txtGAMEMinuteTimeLimit.setText(Integer.toString(prop.get(propIndex).getMinuteTimeLimit()));
        txtGAMETurnTimeLimit.setText(Integer.toString(prop.get(propIndex).getTurnTimeLimit()));
        txtGAMEScenarioSearchFolders.setText(prop.get(propIndex).getScenarioSearchFolders());
        txtGAMEVictoryPointLimit.setText(Integer.toString(prop.get(propIndex).getVictoryPointLimit()));
        txtGAMECityEliminationCount.setText(Integer.toString(prop.get(propIndex).getCityEliminationCount()));
        txtGAMEOneCityCultureWinLimit.setText(Integer.toString(prop.get(propIndex).getOneCityCultureWinLimit()));
        txtGAMEAllCitiesCultureWinLimit.setText(Integer.toString(prop.get(propIndex).getAllCitiesCultureWinLimit()));
        txtGAMEDominationTerrainPercent.setText(Integer.toString(prop.get(propIndex).getDominationTerrainPercent()));
        txtGAMEDominationPopulationPercent.setText(Integer.toString(prop.get(propIndex).getDominationPopulationPercent()));
        txtGAMEWonderVP.setText(Integer.toString(prop.get(propIndex).getWonderVP()));
        txtGAMEDefeatingOpposingUnitVP.setText(Integer.toString(prop.get(propIndex).getDefeatingOpposingUnitVP()));
        txtGAMEAdvancementVP.setText(Integer.toString(prop.get(propIndex).getAdvancementVP()));
        txtGAMECityConquestVP.setText(Integer.toString(prop.get(propIndex).getCityConquestVP()));
        txtGAMEVictoryPointVP.setText(Integer.toString(prop.get(propIndex).getVictoryPointVP()));
        txtGAMECaptureSpecialUnitVP.setText(Integer.toString(prop.get(propIndex).getCaptureSpecialUnitVP()));
        //txtGAMEQuestionMark1.setText(Integer.toString(prop.get(propIndex).getQuestionMark1()));
        /*
         if (prop.get(propIndex).getQuestionMark2() == 1)
        {
            chkGAMEQuestionMark2.setSelected(true);
        }
        else
        {
            chkGAMEQuestionMark2.setSelected(false);
        }
         **/
        if (prop.get(propIndex).getAllianceVictoryType() == 0)
            rbtnGAMEIndividualVictory.setSelected(true);
        else
            rbtnGAMECoalitionVictory.setSelected(true);
        txtGAMEPlagueName.setText(prop.get(propIndex).getPlaugeName());
        if (prop.get(propIndex).getPermitPlagues() == 1)
        {
            chkGAMEPermitPlagues.setSelected(true);
        }
        else
        {
            chkGAMEPermitPlagues.setSelected(false);
        }
        txtGAMEPlagueEarliestStart.setText(Integer.toString(prop.get(propIndex).getPlagueEarliestStart()));
        txtGAMEPlagueVariation.setText(Integer.toString(prop.get(propIndex).getPlagueVariation()));
        txtGAMEPlagueDuration.setText(Integer.toString(prop.get(propIndex).getPlagueDuration()));
        txtGAMEPlagueStrength.setText(Integer.toString(prop.get(propIndex).getPlagueStrength()));
        txtGAMEPlagueGracePeriod.setText(Integer.toString(prop.get(propIndex).getPlagueGracePeriod()));
        txtGAMEPlagueMaxOccurance.setText(Integer.toString(prop.get(propIndex).getPlagueMaxOccurance()));
        //txtGAMEQuestionMark3.setText(Integer.toString(prop.get(propIndex).getQuestionMark3()));
        //txtGAMEUnknown.setText(prop.get(propIndex).Unknown;);
        if (prop.get(propIndex).getRespawnFlagUnits() == 1) //true
            chkGAMERespawnFlagUnits.setSelected(true);
        else
            chkGAMERespawnFlagUnits.setSelected(false);
        if (prop.get(propIndex).getCaptureAnyFlag() == 1)   //true
        {
            chkGAMECaptureAnyFlag.setSelected(true);
        }
        else
        {
            chkGAMECaptureAnyFlag.setSelected(false);
        }
        txtGAMEGoldForCapture.setText(Integer.toString(prop.get(propIndex).getGoldForCapture()));
        if (prop.get(propIndex).getMapVisible() == 1)
        {
            chkGAMEMapVisible.setSelected(true);
        }
        else
        {
            chkGAMEMapVisible.setSelected(false);
        }
        if (prop.get(propIndex).getRetainCulture() == 1)
        {
            chkGAMERetainCulture.setSelected(true);
        }
        else
        {
            chkGAMERetainCulture.setSelected(false);
        }
        //txtGAMEQuestionMark4.setText(Integer.toString(prop.get(propIndex).getQuestionMark4()));
        txtGAMEEruptionPeriod.setText(Integer.toString(prop.get(propIndex).getEruptionPeriod()));
        txtGAMEMpBaseTime.setText(Integer.toString(prop.get(propIndex).getMpBaseTime()));
        txtGAMEMpCityTime.setText(Integer.toString(prop.get(propIndex).getMpCityTime()));
        txtGAMEMpUnitTime.setText(Integer.toString(prop.get(propIndex).getMpUnitTime()));
        //turns per timescale part stuff
        txtGAMETurnGroup1.setText(Integer.toString(prop.get(propIndex).turnsPerTimescalePart[0]));
        txtGAMETurnGroup2.setText(Integer.toString(prop.get(propIndex).turnsPerTimescalePart[1]));
        txtGAMETurnGroup3.setText(Integer.toString(prop.get(propIndex).turnsPerTimescalePart[2]));
        txtGAMETurnGroup4.setText(Integer.toString(prop.get(propIndex).turnsPerTimescalePart[3]));
        txtGAMETurnGroup5.setText(Integer.toString(prop.get(propIndex).turnsPerTimescalePart[4]));
        txtGAMETurnGroup6.setText(Integer.toString(prop.get(propIndex).turnsPerTimescalePart[5]));
        txtGAMETurnGroup7.setText(Integer.toString(prop.get(propIndex).turnsPerTimescalePart[6]));
        txtGAMENumTurns1.setText(Integer.toString(prop.get(propIndex).timeUnitsPerTurn[0]));
        txtGAMENumTurns2.setText(Integer.toString(prop.get(propIndex).timeUnitsPerTurn[1]));
        txtGAMENumTurns3.setText(Integer.toString(prop.get(propIndex).timeUnitsPerTurn[2]));
        txtGAMENumTurns4.setText(Integer.toString(prop.get(propIndex).timeUnitsPerTurn[3]));
        txtGAMENumTurns5.setText(Integer.toString(prop.get(propIndex).timeUnitsPerTurn[4]));
        txtGAMENumTurns6.setText(Integer.toString(prop.get(propIndex).timeUnitsPerTurn[5]));
        txtGAMENumTurns7.setText(Integer.toString(prop.get(propIndex).timeUnitsPerTurn[6]));

        //set up alliance boxes
        //remove all elements from models first, or you get WAAAAAAAAAAYYYY too many
        alliance0Model.removeAllElements();
        alliance1Model.removeAllElements();
        alliance2Model.removeAllElements();
        alliance3Model.removeAllElements();
        alliance4Model.removeAllElements();
        if (prop.get(propIndex).getNumberOfPlayableCivs() != prop.get(propIndex).civPartOfWhichAlliance.size())
        {
            if (logger.isDebugEnabled())
                logger.debug("Num Playable civs not equal to civPartOfWhichAlliance " + prop.get(propIndex).getNumberOfPlayableCivs() + "  " + prop.get(propIndex).civPartOfWhichAlliance.size());
        }

        updateAllianceMembers();

        if (baselink.hasCustomMap())
        {
            if (logger.isDebugEnabled())
                logger.debug("Has custom map");
            cmbWCHRBarbarianActivity.setEnabled(true);
            cmbWCHRBarbarianActivity.setSelectedIndex(worldChar.get(0).getSelectedBarbarianActivity() + 1);
        }
        else
        {
            if (logger.isDebugEnabled())
                logger.debug("No custom map");
            cmbWCHRBarbarianActivity.setEnabled(false);
            cmbWCHRBarbarianActivity.setSelectedIndex(2);
        }


        //Information for alliances at peace/war
        //WAR = 1, Peace = 0
        if (baselink.version == civ3Version.CONQUESTS)
        {
            if (prop.get(propIndex).warWith1.get(2) == 1)
            {
                tglGAME1v2.setSelected(true);
                tglGAME1v2.setText(war);
                tglGAME2v1.setSelected(true);
                tglGAME2v1.setText(war);
            }
            else
            {
                tglGAME1v2.setSelected(false);
                tglGAME1v2.setText(peace);
                tglGAME2v1.setSelected(false);
                tglGAME2v1.setText(peace);
            }
            if (prop.get(propIndex).warWith1.get(3) == 1)
            {
                tglGAME1v3.setSelected(true);
                tglGAME1v3.setText(war);
                tglGAME3v1.setSelected(true);
                tglGAME3v1.setText(war);
            }
            else
            {
                tglGAME1v3.setSelected(false);
                tglGAME1v3.setText(peace);
                tglGAME3v1.setSelected(false);
                tglGAME3v1.setText(peace);
            }
            if (prop.get(propIndex).warWith1.get(4) == 1)
            {
                tglGAME1v4.setSelected(true);
                tglGAME1v4.setText(war);
                tglGAME4v1.setSelected(true);
                tglGAME4v1.setText(war);
            }
            else
            {
                tglGAME1v4.setSelected(false);
                tglGAME1v4.setText(peace);
                tglGAME4v1.setSelected(false);
                tglGAME4v1.setText(peace);
            }
            if (prop.get(propIndex).warWith2.get(3) == 1)
            {
                tglGAME2v3.setSelected(true);
                tglGAME2v3.setText(war);
                tglGAME3v2.setSelected(true);
                tglGAME3v2.setText(war);
            }
            else
            {
                tglGAME2v3.setSelected(false);
                tglGAME2v3.setText(peace);
                tglGAME3v2.setSelected(false);
                tglGAME3v2.setText(peace);
            }
            if (prop.get(propIndex).warWith2.get(4) == 1)
            {
                tglGAME2v4.setSelected(true);
                tglGAME2v4.setText(war);
                tglGAME4v2.setSelected(true);
                tglGAME4v2.setText(war);
            }
            else
            {
                tglGAME2v4.setSelected(false);
                tglGAME2v4.setText(peace);
                tglGAME4v2.setSelected(false);
                tglGAME4v2.setText(peace);
            }
            if (prop.get(propIndex).warWith3.get(4) == 1)
            {
                tglGAME3v4.setSelected(true);
                tglGAME3v4.setText(war);
                tglGAME4v3.setSelected(true);
                tglGAME4v3.setText(war);
            }
            else
            {
                tglGAME3v4.setSelected(false);
                tglGAME3v4.setText(peace);
                tglGAME4v3.setSelected(false);
                tglGAME4v3.setText(peace);
            }
            lblGAMEAlliance0Name.setText(prop.get(propIndex).alliance0);
            lblGAMEAlliance1Name.setText(prop.get(propIndex).alliance1);
            lblGAMEAlliance2Name.setText(prop.get(propIndex).alliance2);
            lblGAMEAlliance3Name.setText(prop.get(propIndex).alliance3);
            lblGAMEAlliance4Name.setText(prop.get(propIndex).alliance4);
        }
    }
    
    /**
     * Updates the members in each alliance based on the latest data in the RACE BIQ section.
     * Intentionally package-level private, so other tabs can call it to update this tab.
     */
    void updateAllianceMembers()
    {
        if (baselink.version == civ3Version.CONQUESTS)
        {
            GAME properties = prop.get(0);
            if (properties.getNumberOfPlayableCivs() != 0)
            {
                alliance0Model.clear();
                alliance1Model.clear();
                alliance2Model.clear();
                alliance3Model.clear();
                alliance4Model.clear();
                //for (int i = 0; i < prop.get(propIndex).getNumberOfPlayableCivs(); i++)
                for (int i = 0; i < properties.civPartOfWhichAlliance.size(); i++)
                {
                    //this switch statement amounts to adding the names of the civs in each alliance into
                    //the correct boxes
                    switch (properties.civPartOfWhichAlliance.get(i))
                    {
                        case 0: alliance0Model.addElement(civ.get(properties.idOfPlayableCivs.get(i)).getName());
                                break;
                        case 1: alliance1Model.addElement(civ.get(properties.idOfPlayableCivs.get(i)).getName());
                                break;
                        case 2: alliance2Model.addElement(civ.get(properties.idOfPlayableCivs.get(i)).getName());
                                break;
                        case 3: alliance3Model.addElement(civ.get(properties.idOfPlayableCivs.get(i)).getName());
                                break;
                        case 4: alliance4Model.addElement(civ.get(properties.idOfPlayableCivs.get(i)).getName());
                                break;
                        default: logger.warn("Civilization " + i + " not part of any alliances!");
                    }
                }
            }
            else
            {
                for (int i = 0; i < prop.get(propIndex).civPartOfWhichAlliance.size(); i++)
                {
                    //this switch statement amounts to adding the names of the civs in each alliance into
                    //the correct boxes
                    switch (prop.get(propIndex).civPartOfWhichAlliance.get(i))
                    {
                        case 0: alliance0Model.addElement(civ.get(i + 1).getName());
                                break;
                        case 1: alliance1Model.addElement(civ.get(i + 1).getName());
                                break;
                        case 2: alliance2Model.addElement(civ.get(i + 1).getName());
                                break;
                        case 3: alliance3Model.addElement(civ.get(i + 1).getName());
                                break;
                        case 4: alliance4Model.addElement(civ.get(i + 1).getName());
                                break;
                        default: logger.warn("Civilization " + i + " not part of any alliances!");
                    }
                }
            }
        }
        else
        {
            //PTW stuff
        }
    }

    public void setSelectedIndex(int i)
    {
        propIndex = i;
    }

    private void moveToAlliancePopup(java.awt.event.MouseEvent evt){
        if (evt.isPopupTrigger())
        {
            JPopupMenu popUp = new JPopupMenu();
            popUp.add(moveToAlliance0);
            popUp.add(moveToAlliance1);
            popUp.add(moveToAlliance2);
            popUp.add(moveToAlliance3);
            popUp.add(moveToAlliance4);
            java.awt.Component component = evt.getComponent();
            sourceList = (JList)evt.getSource();
            sourceList.setSelectedIndex(sourceList.locationToIndex(new java.awt.Point(evt.getX(), evt.getY())));
            if (sourceList.getSelectedIndex() == -1)    //nothing in the list
                return;
            selectedCiv = sourceList.getSelectedIndex();
            if (sourceList == lstGAMENoAlliances)
                popUp.remove(moveToAlliance0);
            else if (sourceList == lstGAMEAlliance1)
                popUp.remove(moveToAlliance1);
            else if (sourceList == lstGAMEAlliance2)
                popUp.remove(moveToAlliance2);
            else if (sourceList == lstGAMEAlliance3)
                popUp.remove(moveToAlliance3);
            else if (sourceList == lstGAMEAlliance4)
                popUp.remove(moveToAlliance4);
            popUp.show(component, evt.getX(), evt.getY());
        }
    }

    private void removeFromList(JList list, int index)
    {
        if (list == lstGAMENoAlliances)
            alliance0Model.removeElementAt(index);
        if (list == lstGAMEAlliance1)
            alliance1Model.removeElementAt(index);
        if (list == lstGAMEAlliance2)
            alliance2Model.removeElementAt(index);
        if (list == lstGAMEAlliance3)
            alliance3Model.removeElementAt(index);
        if (list == lstGAMEAlliance4)
            alliance4Model.removeElementAt(index);
    }
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
        if(logger.isDebugEnabled())
            logger.debug("SETTING MINIMAL LIMITS");
        addLengthDocumentListener(5199, txtGAMEScenarioSearchFolders);
        //ADD IN NAME MODIFIERS FOR ALLIANCE NAMES
        addLengthDocumentListener(259, txtGAMEPlagueName);
        addLengthDocumentListener(639, txtVERDescription);
        //as this only watches for overfuls 'n stuff, don't set any value-based limits
        //do set documen.t listeners on the char array fields (civilopedia and the like)
    }
    /**
     * Sets 'exporatory' limits for text fields - anything not known to cause
     * a problem is allowed.
     */
    public void setExploratoryLimits()
    {
        clearBadValueDocumentListeners();
        clearLengthDocumentListeners();
        if(logger.isDebugEnabled())
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
        if(logger.isDebugEnabled())
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
        if(logger.isDebugEnabled())
            logger.debug("SETTING FIRAXIS LIMITS");
        setMinimalLimits();
        addBadValueDocumentListener(1000000, 1000, txtGAMEVictoryPointLimit);
        addBadValueDocumentListener(500000, 5000, txtGAMEAllCitiesCultureWinLimit);
        addBadValueDocumentListener(100000, 100, txtGAMEEruptionPeriod);
        addBadValueDocumentListener(50000, 2000, txtGAMEOneCityCultureWinLimit);
        addBadValueDocumentListener(10000, 1, txtGAMEStartYear);
        addBadValueDocumentListener(10000, txtGAMECaptureSpecialUnitVP);
        addBadValueDocumentListener(10000, txtGAMEGoldForCapture);
        addBadValueDocumentListener(5000, -5000, txtGAMEPlagueEarliestStart);
        addBadValueDocumentListener(1440, txtGAMEMinuteTimeLimit);
        addBadValueDocumentListener(1000, txtGAMENumTurns1);
        addBadValueDocumentListener(1000, txtGAMENumTurns2);
        addBadValueDocumentListener(1000, txtGAMENumTurns3);
        addBadValueDocumentListener(1000, txtGAMENumTurns4);
        addBadValueDocumentListener(1000, txtGAMENumTurns5);
        addBadValueDocumentListener(1000, txtGAMENumTurns6);
        addBadValueDocumentListener(1000, txtGAMENumTurns7);
        addBadValueDocumentListener(1000, txtGAMETurnGroup1);
        addBadValueDocumentListener(1000, txtGAMETurnGroup2);
        addBadValueDocumentListener(1000, txtGAMETurnGroup3);
        addBadValueDocumentListener(1000, txtGAMETurnGroup4);
        addBadValueDocumentListener(1000, txtGAMETurnGroup5);
        addBadValueDocumentListener(1000, txtGAMETurnGroup6);
        addBadValueDocumentListener(1000, txtGAMETurnGroup7);
        addBadValueDocumentListener(1000, 1, txtGAMETurnTimeLimit);
        addBadValueDocumentListener(1000, 1, txtGAMEPlagueGracePeriod);
        addBadValueDocumentListener(1000, txtGAMECityConquestVP);
        addBadValueDocumentListener(500, txtGAMEPlagueVariation);
        addBadValueDocumentListener(100, txtGAMEMpBaseTime);
        addBadValueDocumentListener(100, txtGAMEMpCityTime);
        addBadValueDocumentListener(100, txtGAMEMpUnitTime);
        addBadValueDocumentListener(100, txtGAMEWonderVP);
        addBadValueDocumentListener(100, txtGAMEDefeatingOpposingUnitVP);
        addBadValueDocumentListener(100, txtGAMEAdvancementVP);
        addBadValueDocumentListener(100, txtGAMEVictoryPointVP);
        addBadValueDocumentListener(100, txtGAMEPlagueDuration);
        addBadValueDocumentListener(100, txtGAMEPlagueStrength);
        addBadValueDocumentListener(100, 1, txtGAMEPlagueMaxOccurance);
        addBadValueDocumentListener(100, 15, txtGAMEDominationPopulationPercent);
        addBadValueDocumentListener(100, 15, txtGAMEDominationTerrainPercent);
        addBadValueDocumentListener(10, 1, txtGAMECityEliminationCount);

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
        if(logger.isDebugEnabled())
            logger.debug("SETTING TOTAL LIMITS");
        //don't allow any issues to occur, even if Firaxis would have
        //usually this will be the same as the Firaxian settings
        //this call will take care of minimal limits
        setFiraxisLimits();
    }
}
