package com.civfanatics.civ3.xplatformeditor;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * @Gotcha: If you don't move out of the last box in the table that you updated,
 * that box won't be saved
 * TODO: Fix that Gotcha.
 * @Completed
 */

/**
 *
 * @author Quintillus
 */
import com.civfanatics.civ3.biqFile.BLDG;
import com.civfanatics.civ3.biqFile.ESPN;
import com.civfanatics.civ3.biqFile.EXPR;
import com.civfanatics.civ3.biqFile.GOVT;
import com.civfanatics.civ3.biqFile.GOVTGOVTRelations;
import com.civfanatics.civ3.biqFile.TECH;
import static com.civfanatics.civ3.xplatformeditor.Main.i18n;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJList;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJTextField;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperListModel;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
public class GOVTTab extends EditorTab {


    private javax.swing.ButtonGroup btnGroupCorruptionWaste;
    private javax.swing.ButtonGroup btnGroupWarWeariness;
    private javax.swing.ButtonGroup btnGroupHurryLabor;

    BldgTab bldgTab;
    CIVTab civTab;
    PLYRTab playerTab;

    boolean tabCreated;    
    public List<GOVT>government;    
    public List<EXPR>experience;    
    public List<ESPN>espionage;    
    public List<TECH>technology;
    int governmentIndex;
    private SuperListModel governmentList;

    private JCheckBox chkGOVTDefaultType;
    private JCheckBox chkGOVTForcedResettlement;
    private JCheckBox chkGOVTRequiresMaintenance;
    private JCheckBox chkGOVTRulerTitle1;
    private JCheckBox chkGOVTRulerTitle2;
    private JCheckBox chkGOVTRulerTitle3;
    private JCheckBox chkGOVTRulerTitle4;
    private JCheckBox chkGOVTTilePenalty;
    private JCheckBox chkGOVTTradeBonus;
    private JCheckBox chkGOVTTransitionType;
    private JCheckBox chkGOVTXenophobia;
    private JComboBox cmbGOVTDiplomats;
    private JComboBox cmbGOVTImmuneTo;
    public JComboBox cmbGOVTPrerequisiteTechnology;
    private JComboBox cmbGOVTSpies;
    private JLabel lblCivilopediaEntry;
    private JLabel lblPreqrequisite;
    private JLabel lblSciTaxEntCap;
    private JLabel lblWorkerRate;
    private JLabel lblAssimilationChance;
    private JLabel lblDraftLimit;
    private JLabel lblMilitaryPolice;
    private JLabel lblCostPerUnit;
    private JLabel lblFreeUnits;
    private JLabel lblFreeTown;
    private JLabel lblFreePerCity;
    private JLabel lblFreePerMetro;
    private JLabel lblRulerTitlesEnabled;
    private JLabel lblMasculine;
    private JLabel lblFeminine;
    private JLabel lblDiplomatSkill;
    private JLabel lblSpySkill;
    private JLabel lblImmuneTo;
    private JLabel lblUnknown1;
    private JLabel lblUnknown2;
    private JLabel lblUnknown3;
    private JLabel lblUnknown4;
    private JPanel pnlCorruptionWaste;
    private JPanel jPanel69;
    private JPanel pnlUnitSupport;
    private JPanel pnlFreeUnitsPer;
    private JPanel jPanel72;
    private JPanel jPanel73;
    private JPanel pnlRulerTitles;
    private JPanel pnlFlags;
    private JPanel pnlEspionage;
    private JScrollPane jScrollPane10;
    private JScrollPane jScrollPane20;
    private SuperJList lstGovernments = new SuperJList(this, "government");
    //private JPanel this;
    private JRadioButton rbtnGOVTCorruptionCatastrophic;
    private JRadioButton rbtnGOVTCorruptionCommunal;
    private JRadioButton rbtnGOVTCorruptionMinimal;
    private JRadioButton rbtnGOVTCorruptionNuisance;
    private JRadioButton rbtnGOVTCorruptionOff;
    private JRadioButton rbtnGOVTCorruptionProblematic;
    private JRadioButton rbtnGOVTCorruptionRampant;
    private JRadioButton rbtnGOVTHurryForced;
    private JRadioButton rbtnGOVTHurryImpossible;
    private JRadioButton rbtnGOVTHurryPaid;
    private JRadioButton rbtnGOVTWWHigh;
    private JRadioButton rbtnGOVTWWLow;
    private JRadioButton rbtnGOVTWWNone;
    private JSpinner spnrRateCap;
    private JSpinner spnrWorkerRate;
    private JTable tblModifiers;
    private JCheckBox chkGOVTAllUnitsFree;
    private SuperJTextField txtGOVTAssimilationChance;
    private SuperJTextField txtGOVTCivilopediaEntry;
    private SuperJTextField txtGOVTCostPerUnit;
    private SuperJTextField txtGOVTDraftLimit;
    private SuperJTextField txtGOVTFemaleRulerTitle1;
    private SuperJTextField txtGOVTFemaleRulerTitle2;
    private SuperJTextField txtGOVTFemaleRulerTitle3;
    private SuperJTextField txtGOVTFemaleRulerTitle4;
    private SuperJTextField txtGOVTFreeUnits;
    private SuperJTextField txtGOVTFreeUnitsPerCity;
    private SuperJTextField txtGOVTFreeUnitsPerMetropolis;
    private SuperJTextField txtGOVTFreeUnitsPerTown;
    private SuperJTextField txtGOVTMaleRulerTitle1;
    private SuperJTextField txtGOVTMaleRulerTitle2;
    private SuperJTextField txtGOVTMaleRulerTitle3;
    private SuperJTextField txtGOVTMaleRulerTitle4;
    private SuperJTextField txtGOVTMilitaryPoliceLimit;
    private SuperJTextField txtGOVTQuestionMarkFour;
    private SuperJTextField txtGOVTQuestionMarkOne;
    private SuperJTextField txtGOVTQuestionMarkThree;
    private SuperJTextField txtGOVTQuestionMarkTwo;
    DefaultTableModel tblModifiersModel;
    SpinnerNumberModel rateCapModel;
    SpinnerNumberModel workerRateModel;
    
    DefaultComboBoxModel mdlPrerequisite = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlDiplomats = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlSpies = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlImmuneTo = new DefaultComboBoxModel();

    public GOVTTab()
    {
        lstType = lstGovernments;
        tabName = "GOVT";
        textBoxes = new ArrayList<>();
        rateCapModel = new SpinnerNumberModel();
        workerRateModel = new SpinnerNumberModel();
        jScrollPane10 = new JScrollPane();
        lblCivilopediaEntry = new JLabel();
        txtGOVTCivilopediaEntry = new SuperJTextField();
        txtGOVTCivilopediaEntry.setDescription(i18n("general.civilopediaEntry"));
        lblPreqrequisite = new JLabel();
        cmbGOVTPrerequisiteTechnology = new JComboBox();
        pnlRulerTitles = new JPanel();
        chkGOVTRulerTitle1 = new JCheckBox();
        lblRulerTitlesEnabled = new JLabel();
        chkGOVTRulerTitle2 = new JCheckBox();
        chkGOVTRulerTitle3 = new JCheckBox();
        chkGOVTRulerTitle4 = new JCheckBox();
        lblMasculine = new JLabel();
        lblFeminine = new JLabel();
        txtGOVTMaleRulerTitle1 = new SuperJTextField();
        txtGOVTFemaleRulerTitle1 = new SuperJTextField();
        txtGOVTFemaleRulerTitle2 = new SuperJTextField();
        txtGOVTMaleRulerTitle2 = new SuperJTextField();
        txtGOVTFemaleRulerTitle3 = new SuperJTextField();
        txtGOVTMaleRulerTitle3 = new SuperJTextField();
        txtGOVTFemaleRulerTitle4 = new SuperJTextField();
        txtGOVTMaleRulerTitle4 = new SuperJTextField();
        txtGOVTMaleRulerTitle1.setDescription(i18n("govt.masculineRulerTitle") + " 1");
        txtGOVTMaleRulerTitle2.setDescription(i18n("govt.masculineRulerTitle") + " 2");
        txtGOVTMaleRulerTitle3.setDescription(i18n("govt.masculineRulerTitle") + " 3");
        txtGOVTMaleRulerTitle4.setDescription(i18n("govt.masculineRulerTitle") + " 4");
        txtGOVTFemaleRulerTitle1.setDescription(i18n("govt.feminineRulerTitle") + " 1");
        txtGOVTFemaleRulerTitle2.setDescription(i18n("govt.feminineRulerTitle") + " 2");
        txtGOVTFemaleRulerTitle3.setDescription(i18n("govt.feminineRulerTitle") + " 3");
        txtGOVTFemaleRulerTitle4.setDescription(i18n("govt.feminineRulerTitle") + " 4");
        pnlCorruptionWaste = new JPanel();
        rbtnGOVTCorruptionMinimal = new JRadioButton();
        rbtnGOVTCorruptionNuisance = new JRadioButton();
        rbtnGOVTCorruptionProblematic = new JRadioButton();
        rbtnGOVTCorruptionRampant = new JRadioButton();
        rbtnGOVTCorruptionCatastrophic = new JRadioButton();
        rbtnGOVTCorruptionCommunal = new JRadioButton();
        rbtnGOVTCorruptionOff = new JRadioButton();
        jPanel69 = new JPanel();
        lblSciTaxEntCap = new JLabel();
        spnrRateCap = new JSpinner();
        lblWorkerRate = new JLabel();
        spnrWorkerRate = new JSpinner();
        lblAssimilationChance = new JLabel();
        txtGOVTAssimilationChance = new SuperJTextField();
        txtGOVTAssimilationChance.setDescription(i18n("govt.assimilationPercent"));
        lblDraftLimit = new JLabel();
        txtGOVTDraftLimit = new SuperJTextField();
        txtGOVTDraftLimit.setDescription(i18n("govt.draftLimit"));
        lblMilitaryPolice = new JLabel();
        txtGOVTMilitaryPoliceLimit = new SuperJTextField();
        txtGOVTMilitaryPoliceLimit.setDescription(i18n("govt.militaryPolice"));
        pnlUnitSupport = new JPanel();
        chkGOVTAllUnitsFree = new JCheckBox();
        lblCostPerUnit = new JLabel();
        txtGOVTCostPerUnit = new SuperJTextField();
        txtGOVTCostPerUnit.setDescription(i18n("govt.costPerUnit"));
        lblFreeUnits = new JLabel();
        txtGOVTFreeUnits = new SuperJTextField();
        txtGOVTFreeUnits.setDescription(i18n("govt.freeUnits"));
        pnlFreeUnitsPer = new JPanel();
        lblFreeTown = new JLabel();
        lblFreePerCity = new JLabel();
        lblFreePerMetro = new JLabel();
        txtGOVTFreeUnitsPerTown = new SuperJTextField();
        txtGOVTFreeUnitsPerCity = new SuperJTextField();
        txtGOVTFreeUnitsPerMetropolis = new SuperJTextField();
        txtGOVTFreeUnitsPerTown.setDescription(i18n("govt.freePerTown"));
        txtGOVTFreeUnitsPerCity.setDescription(i18n("govt.freePerCity"));
        txtGOVTFreeUnitsPerMetropolis.setDescription(i18n("govt.freePerMetropolis"));
        jPanel72 = new JPanel();
        rbtnGOVTWWNone = new JRadioButton();
        rbtnGOVTWWLow = new JRadioButton();
        rbtnGOVTWWHigh = new JRadioButton();
        jPanel73 = new JPanel();
        rbtnGOVTHurryImpossible = new JRadioButton();
        rbtnGOVTHurryForced = new JRadioButton();
        rbtnGOVTHurryPaid = new JRadioButton();
        pnlFlags = new JPanel();
        chkGOVTDefaultType = new JCheckBox();
        chkGOVTTransitionType = new JCheckBox();
        chkGOVTRequiresMaintenance = new JCheckBox();
        chkGOVTTilePenalty = new JCheckBox();
        chkGOVTTradeBonus = new JCheckBox();
        chkGOVTXenophobia = new JCheckBox();
        chkGOVTForcedResettlement = new JCheckBox();
        jScrollPane20 = new JScrollPane();
        tblModifiers = new JTable();
        pnlEspionage = new JPanel();
        lblDiplomatSkill = new JLabel();
        cmbGOVTDiplomats = new JComboBox();
        cmbGOVTSpies = new JComboBox();
        lblSpySkill = new JLabel();
        cmbGOVTImmuneTo = new JComboBox();
        lblImmuneTo = new JLabel();
        lblUnknown1 = new JLabel();
        txtGOVTQuestionMarkOne = new SuperJTextField();
        txtGOVTQuestionMarkTwo = new SuperJTextField();
        lblUnknown2 = new JLabel();
        txtGOVTQuestionMarkThree = new SuperJTextField();
        lblUnknown3 = new JLabel();
        txtGOVTQuestionMarkFour = new SuperJTextField();
        txtGOVTQuestionMarkOne.setDescription("govt.unknown1");
        txtGOVTQuestionMarkOne.setDescription("govt.unknown2");
        txtGOVTQuestionMarkOne.setDescription("govt.unknown3");
        txtGOVTQuestionMarkOne.setDescription("govt.unknown4");
        lblUnknown4 = new JLabel();
        governmentIndex = -1;
        btnGroupCorruptionWaste = new ButtonGroup();
        btnGroupWarWeariness = new ButtonGroup();
        btnGroupHurryLabor = new ButtonGroup();
        tblModifiersModel = new DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                i18n("govt.government"), i18n("govt.resistanceModifier"), i18n("govt.propaganda")
            });
        spnrRateCap.setModel(rateCapModel);
        spnrWorkerRate.setModel(workerRateModel);

    }
    
    public void setSelectedIndex(int i)
    {
        governmentIndex = i;
    }
    /**
     * Creates the GUI of the tab.  This includes button groups, layouts, etc.
     * Logically, this is to occur immediately after the constructor is called.
     *
     * @return - A reference to the GOVTTab instance itself.  This reference
     * allows the tab to be added to a tab bar.
     */
    public JPanel createTab()
    {
        btnGroupCorruptionWaste.add(rbtnGOVTCorruptionCatastrophic);
        btnGroupCorruptionWaste.add(rbtnGOVTCorruptionCommunal);
        btnGroupCorruptionWaste.add(rbtnGOVTCorruptionMinimal);
        btnGroupCorruptionWaste.add(rbtnGOVTCorruptionNuisance);
        btnGroupCorruptionWaste.add(rbtnGOVTCorruptionOff);
        btnGroupCorruptionWaste.add(rbtnGOVTCorruptionProblematic);
        btnGroupCorruptionWaste.add(rbtnGOVTCorruptionRampant);

        btnGroupWarWeariness.add(rbtnGOVTWWHigh);
        btnGroupWarWeariness.add(rbtnGOVTWWLow);
        btnGroupWarWeariness.add(rbtnGOVTWWNone);

        btnGroupHurryLabor.add(rbtnGOVTHurryForced);
        btnGroupHurryLabor.add(rbtnGOVTHurryImpossible);
        btnGroupHurryLabor.add(rbtnGOVTHurryPaid);

        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lstGovernments.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstGovernments.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstGovernmentsValueChanged(evt);
            }
        });
        jScrollPane10.setViewportView(lstGovernments);

        this.add(jScrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 180, 670));

        lblCivilopediaEntry.setText(i18n("general.civilopediaEntry") + ":");
        this.add(lblCivilopediaEntry, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 10, -1, -1));
        this.add(txtGOVTCivilopediaEntry, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 10, 140, -1));

        lblPreqrequisite.setText(i18n("general.prerequisite") + ":");
        this.add(lblPreqrequisite, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 40, -1, -1));

        cmbGOVTPrerequisiteTechnology.setModel(mdlPrerequisite);
        this.add(cmbGOVTPrerequisiteTechnology, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 30, 140, -1));

        pnlRulerTitles.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), i18n("govt.rulerTitles")));

        chkGOVTRulerTitle1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkGOVTRulerTitle1ItemStateChanged(evt);
            }
        });

        lblRulerTitlesEnabled.setText(i18n("govt.enabled"));

        chkGOVTRulerTitle2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkGOVTRulerTitle2ItemStateChanged(evt);
            }
        });

        chkGOVTRulerTitle3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkGOVTRulerTitle3ItemStateChanged(evt);
            }
        });

        chkGOVTRulerTitle4.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                chkGOVTRulerTitle4StateChanged(evt);
            }
        });

        lblMasculine.setText(i18n("govt.masculine"));

        lblFeminine.setText(i18n("govt.feminine"));

        org.jdesktop.layout.GroupLayout jPanel74Layout = new org.jdesktop.layout.GroupLayout(pnlRulerTitles);
        pnlRulerTitles.setLayout(jPanel74Layout);
        jPanel74Layout.setHorizontalGroup(
            jPanel74Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel74Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel74Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel74Layout.createSequentialGroup()
                        .add(lblRulerTitlesEnabled)
                        .add(48, 48, 48)
                        .add(lblMasculine)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 73, Short.MAX_VALUE)
                        .add(lblFeminine)
                        .add(553, 553, 553))
                    .add(jPanel74Layout.createSequentialGroup()
                        .add(12, 12, 12)
                        .add(jPanel74Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel74Layout.createSequentialGroup()
                                .add(chkGOVTRulerTitle2)
                                .add(18, 18, 18)
                                .add(txtGOVTMaleRulerTitle2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 136, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(txtGOVTFemaleRulerTitle2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 136, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanel74Layout.createSequentialGroup()
                                .add(chkGOVTRulerTitle1)
                                .add(18, 18, 18)
                                .add(txtGOVTMaleRulerTitle1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 136, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(txtGOVTFemaleRulerTitle1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 136, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanel74Layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(chkGOVTRulerTitle3)
                                .add(18, 18, 18)
                                .add(txtGOVTMaleRulerTitle3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 136, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(txtGOVTFemaleRulerTitle3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 136, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanel74Layout.createSequentialGroup()
                                .add(chkGOVTRulerTitle4)
                                .add(18, 18, 18)
                                .add(txtGOVTMaleRulerTitle4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 136, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(txtGOVTFemaleRulerTitle4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 136, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .add(499, 499, 499))))
        );
        jPanel74Layout.setVerticalGroup(
            jPanel74Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel74Layout.createSequentialGroup()
                .add(jPanel74Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblRulerTitlesEnabled)
                    .add(lblMasculine)
                    .add(lblFeminine))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel74Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(chkGOVTRulerTitle1)
                    .add(jPanel74Layout.createSequentialGroup()
                        .add(jPanel74Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(txtGOVTMaleRulerTitle1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(txtGOVTFemaleRulerTitle1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(1, 1, 1)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel74Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel74Layout.createSequentialGroup()
                        .add(chkGOVTRulerTitle2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(chkGOVTRulerTitle3))
                    .add(jPanel74Layout.createSequentialGroup()
                        .add(jPanel74Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(txtGOVTMaleRulerTitle2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(txtGOVTFemaleRulerTitle2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jPanel74Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(txtGOVTMaleRulerTitle3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(txtGOVTFemaleRulerTitle3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel74Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(chkGOVTRulerTitle4)
                    .add(jPanel74Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(txtGOVTMaleRulerTitle4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(txtGOVTFemaleRulerTitle4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(395, 395, 395))
        );

        this.add(pnlRulerTitles, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 480, 360, 150));

        pnlCorruptionWaste.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), Main.i18n("govt.corruptionWaste")));

        rbtnGOVTCorruptionMinimal.setText(Main.i18n("govt.minimal"));

        rbtnGOVTCorruptionNuisance.setText(Main.i18n("govt.nuisance"));

        rbtnGOVTCorruptionProblematic.setText(Main.i18n("govt.problematic"));

        rbtnGOVTCorruptionRampant.setText(Main.i18n("govt.rampant"));

        rbtnGOVTCorruptionCatastrophic.setText(Main.i18n("govt.catastrophic"));

        rbtnGOVTCorruptionCommunal.setText(Main.i18n("govt.communal"));

        rbtnGOVTCorruptionOff.setText(Main.i18n("govt.off"));

        org.jdesktop.layout.GroupLayout jPanel68Layout = new org.jdesktop.layout.GroupLayout(pnlCorruptionWaste);
        pnlCorruptionWaste.setLayout(jPanel68Layout);
        jPanel68Layout.setHorizontalGroup(
            jPanel68Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel68Layout.createSequentialGroup()
                .add(jPanel68Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(rbtnGOVTCorruptionMinimal)
                    .add(rbtnGOVTCorruptionNuisance)
                    .add(rbtnGOVTCorruptionProblematic)
                    .add(rbtnGOVTCorruptionRampant)
                    .add(rbtnGOVTCorruptionCatastrophic)
                    .add(rbtnGOVTCorruptionCommunal)
                    .add(rbtnGOVTCorruptionOff))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel68Layout.setVerticalGroup(
            jPanel68Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel68Layout.createSequentialGroup()
                .add(rbtnGOVTCorruptionMinimal)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(rbtnGOVTCorruptionNuisance)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(rbtnGOVTCorruptionProblematic)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(rbtnGOVTCorruptionRampant)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(rbtnGOVTCorruptionCatastrophic)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(rbtnGOVTCorruptionCommunal)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(rbtnGOVTCorruptionOff))
        );

        this.add(pnlCorruptionWaste, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 70, -1, -1));

        jPanel69.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblSciTaxEntCap.setText(Main.i18n("govt.sciTaxEntCap") + ":");

        lblWorkerRate.setText(Main.i18n("govt.workerRate") + ":");

        lblAssimilationChance.setText(Main.i18n("govt.assimilationPercent") + ":");

        lblDraftLimit.setText(Main.i18n("govt.draftLimit") + ":");

        lblMilitaryPolice.setText(Main.i18n("govt.militaryPolice") + ":");

        org.jdesktop.layout.GroupLayout jPanel69Layout = new org.jdesktop.layout.GroupLayout(jPanel69);
        jPanel69.setLayout(jPanel69Layout);
        jPanel69Layout.setHorizontalGroup(
            jPanel69Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel69Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel69Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel69Layout.createSequentialGroup()
                        .add(jPanel69Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblSciTaxEntCap)
                            .add(lblWorkerRate))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel69Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(spnrWorkerRate)
                            .add(spnrRateCap, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)))
                    .add(jPanel69Layout.createSequentialGroup()
                        .add(jPanel69Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblAssimilationChance)
                            .add(lblDraftLimit)
                            .add(lblMilitaryPolice))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel69Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(txtGOVTMilitaryPoliceLimit, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                            .add(txtGOVTDraftLimit, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                            .add(txtGOVTAssimilationChance, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel69Layout.setVerticalGroup(
            jPanel69Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel69Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel69Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblSciTaxEntCap)
                    .add(spnrRateCap, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel69Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblWorkerRate)
                    .add(spnrWorkerRate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel69Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblAssimilationChance)
                    .add(txtGOVTAssimilationChance, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel69Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblDraftLimit)
                    .add(txtGOVTDraftLimit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel69Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblMilitaryPolice)
                    .add(txtGOVTMilitaryPoliceLimit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        this.add(jPanel69, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 80, 230, 180));

        pnlUnitSupport.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), Main.i18n("govt.unitSupport")));

        chkGOVTAllUnitsFree.setText(Main.i18n("govt.allUnitsFree"));
        chkGOVTAllUnitsFree.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if (chkGOVTAllUnitsFree.isSelected())
                {
                    txtGOVTFreeUnits.setText("-1");
                    txtGOVTFreeUnits.setEnabled(false);
                    txtGOVTFreeUnitsPerMetropolis.setEnabled(false);
                    txtGOVTFreeUnitsPerCity.setEnabled(false);
                    txtGOVTFreeUnitsPerTown.setEnabled(false);
                }
                else
                {
                    txtGOVTFreeUnits.setEnabled(true);
                    txtGOVTFreeUnitsPerMetropolis.setEnabled(true);
                    txtGOVTFreeUnitsPerCity.setEnabled(true);
                    txtGOVTFreeUnitsPerTown.setEnabled(true);
                    if (txtGOVTFreeUnits.getText().equals("-1"))
                        txtGOVTFreeUnits.setText("0");
                }
            }
        });

        lblCostPerUnit.setText(Main.i18n("govt.costPerUnit") + ":");

        lblFreeUnits.setText(Main.i18n("govt.freeUnits") + ":");

        pnlFreeUnitsPer.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), i18n("govt.freeUnitsPer") + "..."));

        lblFreeTown.setText(Main.i18n("govt.freePerTown") + ":");

        lblFreePerCity.setText(Main.i18n("govt.freePerCity") + ":");

        lblFreePerMetro.setText(Main.i18n("govt.freePerMetropolis") + ":");

        org.jdesktop.layout.GroupLayout jPanel71Layout = new org.jdesktop.layout.GroupLayout(pnlFreeUnitsPer);
        pnlFreeUnitsPer.setLayout(jPanel71Layout);
        jPanel71Layout.setHorizontalGroup(
            jPanel71Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel71Layout.createSequentialGroup()
                .add(jPanel71Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel71Layout.createSequentialGroup()
                        .add(lblFreeTown)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 63, Short.MAX_VALUE)
                        .add(txtGOVTFreeUnitsPerTown, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel71Layout.createSequentialGroup()
                        .add(lblFreePerCity)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 73, Short.MAX_VALUE)
                        .add(txtGOVTFreeUnitsPerCity, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel71Layout.createSequentialGroup()
                        .add(lblFreePerMetro)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 34, Short.MAX_VALUE)
                        .add(txtGOVTFreeUnitsPerMetropolis, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel71Layout.setVerticalGroup(
            jPanel71Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel71Layout.createSequentialGroup()
                .add(jPanel71Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblFreeTown)
                    .add(txtGOVTFreeUnitsPerTown, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel71Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblFreePerCity)
                    .add(txtGOVTFreeUnitsPerCity, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel71Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblFreePerMetro)
                    .add(txtGOVTFreeUnitsPerMetropolis, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        org.jdesktop.layout.GroupLayout jPanel70Layout = new org.jdesktop.layout.GroupLayout(pnlUnitSupport);
        pnlUnitSupport.setLayout(jPanel70Layout);
        jPanel70Layout.setHorizontalGroup(
            jPanel70Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel70Layout.createSequentialGroup()
                .add(jPanel70Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, pnlFreeUnitsPer, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel70Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jPanel70Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(chkGOVTAllUnitsFree)
                            .add(jPanel70Layout.createSequentialGroup()
                                .add(jPanel70Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(lblCostPerUnit)
                                    .add(lblFreeUnits))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jPanel70Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(txtGOVTFreeUnits, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                                    .add(txtGOVTCostPerUnit, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))))))
                .addContainerGap())
        );
        jPanel70Layout.setVerticalGroup(
            jPanel70Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel70Layout.createSequentialGroup()
                .add(chkGOVTAllUnitsFree)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel70Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblCostPerUnit)
                    .add(txtGOVTCostPerUnit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel70Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblFreeUnits)
                    .add(txtGOVTFreeUnits, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(pnlFreeUnitsPer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(43, 43, 43))
        );

        this.add(pnlUnitSupport, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 270, -1, 210));

        jPanel72.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), i18n("govt.warWeariness")));

        rbtnGOVTWWNone.setText(i18n("govt.none"));

        rbtnGOVTWWLow.setText(i18n("govt.low"));

        rbtnGOVTWWHigh.setText(i18n("govt.high"));

        org.jdesktop.layout.GroupLayout jPanel72Layout = new org.jdesktop.layout.GroupLayout(jPanel72);
        jPanel72.setLayout(jPanel72Layout);
        jPanel72Layout.setHorizontalGroup(
            jPanel72Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel72Layout.createSequentialGroup()
                .add(jPanel72Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(rbtnGOVTWWNone)
                    .add(rbtnGOVTWWLow)
                    .add(rbtnGOVTWWHigh))
                .addContainerGap(74, Short.MAX_VALUE))
        );
        jPanel72Layout.setVerticalGroup(
            jPanel72Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel72Layout.createSequentialGroup()
                .add(rbtnGOVTWWNone)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(rbtnGOVTWWLow)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(rbtnGOVTWWHigh)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        this.add(jPanel72, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 370, 140, 110));

        jPanel73.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), i18n("govt.hurryingLabor")));

        rbtnGOVTHurryImpossible.setText(i18n("govt.impossible"));

        rbtnGOVTHurryForced.setText(i18n("govt.forcedLabor"));

        rbtnGOVTHurryPaid.setText(i18n("govt.paidLabor"));

        org.jdesktop.layout.GroupLayout jPanel73Layout = new org.jdesktop.layout.GroupLayout(jPanel73);
        jPanel73.setLayout(jPanel73Layout);
        jPanel73Layout.setHorizontalGroup(
            jPanel73Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel73Layout.createSequentialGroup()
                .add(jPanel73Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(rbtnGOVTHurryImpossible)
                    .add(rbtnGOVTHurryForced)
                    .add(rbtnGOVTHurryPaid))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel73Layout.setVerticalGroup(
            jPanel73Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel73Layout.createSequentialGroup()
                .add(rbtnGOVTHurryImpossible)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(rbtnGOVTHurryForced)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(rbtnGOVTHurryPaid))
        );

        this.add(jPanel73, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 270, 140, -1));

        pnlFlags.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), i18n("govt.flags")));

        chkGOVTDefaultType.setText(i18n("govt.defaultType"));

        chkGOVTTransitionType.setText(i18n("govt.transitionType"));

        chkGOVTRequiresMaintenance.setText(i18n("govt.requiresMaintenance"));

        chkGOVTTilePenalty.setText(i18n("govt.minusOnePenalty"));

        chkGOVTTradeBonus.setText(i18n("govt.plusOneCommerce"));

        chkGOVTXenophobia.setText(i18n("govt.xenophobia"));

        chkGOVTForcedResettlement.setText(i18n("govt.forcedResettlement"));

        org.jdesktop.layout.GroupLayout jPanel75Layout = new org.jdesktop.layout.GroupLayout(pnlFlags);
        pnlFlags.setLayout(jPanel75Layout);
        jPanel75Layout.setHorizontalGroup(
            jPanel75Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel75Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel75Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(chkGOVTDefaultType)
                    .add(chkGOVTTransitionType)
                    .add(chkGOVTRequiresMaintenance)
                    .add(chkGOVTTilePenalty)
                    .add(chkGOVTTradeBonus)
                    .add(chkGOVTXenophobia)
                    .add(chkGOVTForcedResettlement))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel75Layout.setVerticalGroup(
            jPanel75Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel75Layout.createSequentialGroup()
                .add(chkGOVTDefaultType)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chkGOVTTransitionType)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chkGOVTRequiresMaintenance)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chkGOVTTilePenalty)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chkGOVTTradeBonus)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chkGOVTXenophobia)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(chkGOVTForcedResettlement)
                .addContainerGap())
        );

        this.add(pnlFlags, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 10, 230, 200));

        tblModifiers.setModel(tblModifiersModel);
        /**tblModifiers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Government", "Resistance Modifier", "Propaganda Modifier"
            }
        ));
        **/
        jScrollPane20.setViewportView(tblModifiers);

        this.add(jScrollPane20, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 220, -1, 80));

        pnlEspionage.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), i18n("govt.espionage")));

        lblDiplomatSkill.setText(i18n("govt.diplomatsAre") + ":");

        cmbGOVTDiplomats.setModel(mdlDiplomats);

        cmbGOVTSpies.setModel(mdlSpies);

        lblSpySkill.setText(i18n("govt.spiesAre") + ":");

        cmbGOVTImmuneTo.setModel(mdlImmuneTo);

        lblImmuneTo.setText(i18n("govt.immuneTo") + ":");

        org.jdesktop.layout.GroupLayout jPanel76Layout = new org.jdesktop.layout.GroupLayout(pnlEspionage);
        pnlEspionage.setLayout(jPanel76Layout);
        jPanel76Layout.setHorizontalGroup(
            jPanel76Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel76Layout.createSequentialGroup()
                .add(jPanel76Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblDiplomatSkill)
                    .add(cmbGOVTDiplomats, 0, 186, Short.MAX_VALUE)
                    .add(lblSpySkill)
                    .add(cmbGOVTSpies, 0, 186, Short.MAX_VALUE)
                    .add(lblImmuneTo)
                    .add(cmbGOVTImmuneTo, 0, 186, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel76Layout.setVerticalGroup(
            jPanel76Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel76Layout.createSequentialGroup()
                .add(lblDiplomatSkill)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cmbGOVTDiplomats, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lblSpySkill)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cmbGOVTSpies, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(lblImmuneTo)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cmbGOVTImmuneTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        this.add(pnlEspionage, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 20, 210, 180));

        lblUnknown1.setText(Main.i18n("govt.unknown1") + ":");
        this.add(lblUnknown1, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 320, -1, -1));
        this.add(txtGOVTQuestionMarkOne, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 320, 80, -1));
        this.add(txtGOVTQuestionMarkTwo, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 340, 80, -1));

        lblUnknown2.setText(Main.i18n("govt.unknown2") + ":");
        this.add(lblUnknown2, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 340, -1, -1));
        this.add(txtGOVTQuestionMarkThree, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 360, 80, -1));

        lblUnknown3.setText(Main.i18n("govt.unknown3") + ":");
        this.add(lblUnknown3, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 360, -1, -1));
        this.add(txtGOVTQuestionMarkFour, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 380, 80, -1));

        lblUnknown4.setText(Main.i18n("govt.unknown4") + ":");
        this.add(lblUnknown4, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 380, -1, -1));


        this.setName("GOVT");

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
     * @param government - The list of all governments.
     * @param experience - The list of all experience levels.
     * @param espionage - The list of all espionage missions.
     * @param technology - The list of all technologies.
     */
    public void sendData(List<GOVT>government, List<EXPR>experience, List<ESPN>espionage, List<TECH>technology)
    {
        assert tabCreated:"Tab must be created before data can be sent to it";
        this.government = government;
        this.experience = experience;
        this.espionage = espionage;
        this.technology = technology;


            governmentList = new SuperListModel();
            lstGovernments.setModel(governmentList);

            cmbGOVTImmuneTo.removeAllItems();
            cmbGOVTImmuneTo.addItem(noneSelected);
            cmbGOVTPrerequisiteTechnology.removeAllItems();
            cmbGOVTPrerequisiteTechnology.addItem(noneSelected);
            cmbGOVTDiplomats.removeAllItems();
            cmbGOVTSpies.removeAllItems();

            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel)tblModifiers.getModel();
            int toRemove = model.getRowCount();
            for (int i = 0; i < toRemove; i++)
            {
                model.removeRow(0);
            }

            for (int i = 0; i < government.size(); i++)
            {
                //add government to combo lists that need government
                governmentList.addElement(government.get(i).getName());
                model.addRow(new Vector());
            }


            for (int i = 0; i < espionage.size(); i++)
            {
                utils.addWithPossibleDuplicates(espionage.get(i).getName(), mdlImmuneTo);
            }

            for (int i = 0; i < experience.size(); i++)
            {
                utils.addWithPossibleDuplicates(experience.get(i).getName(), mdlDiplomats);
                utils.addWithPossibleDuplicates(experience.get(i).getName(), mdlSpies);
            }
            for (int i = 0; i < technology.size(); i++)
            {
                //add technologies to combo lists that need technologies
                utils.addWithPossibleDuplicates(technology.get(i).getName(), mdlPrerequisite);
            }
    }

    public void sendTabLinks(BldgTab bldgTab, CIVTab civTab, PLYRTab playerTab)
    {
        this.bldgTab = bldgTab;
        this.civTab = civTab;
        this.playerTab = playerTab;
    }

    public void sendTabLinks(BldgTab bldgTab, CIVTab civTab)
    {
        this.bldgTab = bldgTab;
        this.civTab = civTab;
    }

    private void lstGovernmentsValueChanged(javax.swing.event.ListSelectionEvent evt) {
        updateTab();
    }


    public void updateTab()
    {
        if (!(governmentIndex == -1)) {
            if (chkGOVTDefaultType.isSelected()) {
                government.get(governmentIndex).setDefaultType(1);
            } else {
                government.get(governmentIndex).setDefaultType(0);
            }
            if (chkGOVTTransitionType.isSelected()) {
                government.get(governmentIndex).setTransitionType(1);
            } else {
                government.get(governmentIndex).setTransitionType(0);
            }
            if (chkGOVTRequiresMaintenance.isSelected()) {
                government.get(governmentIndex).setRequiresMaintenance(1);
            } else {
                government.get(governmentIndex).setRequiresMaintenance(0);
            }
            if (chkGOVTTilePenalty.isSelected()) {
                government.get(governmentIndex).setTilePenalty(1);
            } else {
                government.get(governmentIndex).setTilePenalty(0);
            }
            if (chkGOVTTradeBonus.isSelected()) {
                government.get(governmentIndex).setCommerceBonus(1);
            } else {
                government.get(governmentIndex).setCommerceBonus(0);
            }
            if (chkGOVTXenophobia.isSelected()) {
                government.get(governmentIndex).setXenophobic(1);
            } else {
                government.get(governmentIndex).setXenophobic(0);
            }
            if (chkGOVTForcedResettlement.isSelected()) {
                government.get(governmentIndex).setForceResettlement(1);
            } else {
                government.get(governmentIndex).setForceResettlement(0);
            }
            government.get(governmentIndex).setQuestionMarkOne(txtGOVTQuestionMarkOne.getInteger());
            government.get(governmentIndex).setCivilopediaEntry(txtGOVTCivilopediaEntry.getText());
            government.get(governmentIndex).setMaleRulerTitle1(txtGOVTMaleRulerTitle1.getText());
            government.get(governmentIndex).setFemaleRulerTitle1(txtGOVTFemaleRulerTitle1.getText());
            government.get(governmentIndex).setMaleRulerTitle2(txtGOVTMaleRulerTitle2.getText());
            government.get(governmentIndex).setFemaleRulerTitle2(txtGOVTFemaleRulerTitle2.getText());
            government.get(governmentIndex).setMaleRulerTitle3(txtGOVTMaleRulerTitle3.getText());
            government.get(governmentIndex).setFemaleRulerTitle3(txtGOVTFemaleRulerTitle3.getText());
            government.get(governmentIndex).setMaleRulerTitle4(txtGOVTMaleRulerTitle4.getText());
            government.get(governmentIndex).setFemaleRulerTitle4(txtGOVTFemaleRulerTitle4.getText());
            if (rbtnGOVTCorruptionMinimal.isSelected()) {
                government.get(governmentIndex).setCorruption(0);
            } else if (rbtnGOVTCorruptionNuisance.isSelected()) {
                government.get(governmentIndex).setCorruption(1);
            } else if (rbtnGOVTCorruptionProblematic.isSelected()) {
                government.get(governmentIndex).setCorruption(2);
            } else if (rbtnGOVTCorruptionRampant.isSelected()) {
                government.get(governmentIndex).setCorruption(3);
            } else if (rbtnGOVTCorruptionCatastrophic.isSelected()) {
                government.get(governmentIndex).setCorruption(4);
            } else if (rbtnGOVTCorruptionCommunal.isSelected()) {
                government.get(governmentIndex).setCorruption(5);
            } else if (rbtnGOVTCorruptionOff.isSelected()) {
                government.get(governmentIndex).setCorruption(6);
            }
            government.get(governmentIndex).setImmuneTo(cmbGOVTImmuneTo.getSelectedIndex() - 1);
            government.get(governmentIndex).setDiplomatLevel(cmbGOVTDiplomats.getSelectedIndex());
            government.get(governmentIndex).setSpyLevel(cmbGOVTSpies.getSelectedIndex());
            government.get(governmentIndex).setNumberOfGovernments(governmentList.getSize());
            if (rbtnGOVTHurryImpossible.isSelected()) {
                government.get(governmentIndex).setHurrying(0);
            } else if (rbtnGOVTHurryForced.isSelected()) {
                government.get(governmentIndex).setHurrying(1);
            } else if (rbtnGOVTHurryPaid.isSelected()) {
                government.get(governmentIndex).setHurrying(2);
            }
            government.get(governmentIndex).setAssimilationChance(txtGOVTAssimilationChance.getInteger());
            government.get(governmentIndex).setDraftLimit(txtGOVTDraftLimit.getInteger());
            government.get(governmentIndex).setMilitaryPoliceLimit(txtGOVTMilitaryPoliceLimit.getInteger());
            if (chkGOVTRulerTitle4.isSelected())
                government.get(governmentIndex).setRulerTitlePairsUsed(4);
            else if (chkGOVTRulerTitle3.isSelected())
                government.get(governmentIndex).setRulerTitlePairsUsed(3);
            else if (chkGOVTRulerTitle2.isSelected())
                government.get(governmentIndex).setRulerTitlePairsUsed(2);
            else if (chkGOVTRulerTitle1.isSelected())
                government.get(governmentIndex).setRulerTitlePairsUsed(1);
            else
                government.get(governmentIndex).setRulerTitlePairsUsed(0);
            government.get(governmentIndex).setPrerequisiteTechnology(cmbGOVTPrerequisiteTechnology.getSelectedIndex() - 1);
            government.get(governmentIndex).setScienceCap(((Integer)spnrRateCap.getValue())/10);
            government.get(governmentIndex).setWorkerRate(((Integer)spnrWorkerRate.getValue())/50);
            government.get(governmentIndex).setQuestionMarkTwo(txtGOVTQuestionMarkTwo.getInteger());
            government.get(governmentIndex).setQuestionMarkThree(txtGOVTQuestionMarkThree.getInteger());
            government.get(governmentIndex).setQuestionMarkFour(txtGOVTQuestionMarkFour.getInteger());
            government.get(governmentIndex).setFreeUnits(txtGOVTFreeUnits.getInteger());
            if (chkGOVTAllUnitsFree.isSelected())
                government.get(governmentIndex).setFreeUnits(-1);
            government.get(governmentIndex).setFreeUnitsPerTown(txtGOVTFreeUnitsPerTown.getInteger());
            government.get(governmentIndex).setFreeUnitsPerCity(txtGOVTFreeUnitsPerCity.getInteger());
            government.get(governmentIndex).setFreeUnitsPerMetropolis(txtGOVTFreeUnitsPerMetropolis.getInteger());
            government.get(governmentIndex).setCostPerUnit(txtGOVTCostPerUnit.getInteger());
            if (rbtnGOVTWWNone.isSelected()) {
                government.get(governmentIndex).setWarWeariness(0);
            } else if (rbtnGOVTWWLow.isSelected()) {
                government.get(governmentIndex).setWarWeariness(1);
            } else if (rbtnGOVTWWHigh.isSelected()) {
                government.get(governmentIndex).setWarWeariness(2);
            }

            //Modifiers (aka GOVTGOVTRelations
            for (int i = 0; i < tblModifiers.getRowCount(); i++) 
            {    
                //TODO: Update this to a SuperJTable so I don't need all this boilerplate (priority: low)
                int modifier = 0;
                boolean update = true;
                try{
                    modifier = Integer.valueOf(String.valueOf(tblModifiers.getValueAt(i, 1)));
                }
                catch(NumberFormatException f)
                {
                    if (String.valueOf(tblModifiers.getValueAt(i, 1)).equals(""))
                    {
                        modifier=0;
                    }
                    else
                    {
                        tblModifiers.setValueAt(government.get(governmentIndex).relations.get(i).getResistanceModifier(), i, 1);
                        update = false;
                    }
                }
                if (update)
                    government.get(governmentIndex).relations.get(i).setResistanceModifier(modifier);
                //now bribery modifier
                update = true;
                try{
                    modifier = Integer.valueOf(String.valueOf(tblModifiers.getValueAt(i, 2)));
                }
                catch(NumberFormatException f)
                {
                    if (String.valueOf(tblModifiers.getValueAt(i, 2)).equals(""))
                    {
                        modifier=0;
                    }
                    else
                    {
                        tblModifiers.setValueAt(government.get(governmentIndex).relations.get(i).getBriberyModifier(), i, 2);
                        update = false;
                    }
                }
                if (update)
                    government.get(governmentIndex).relations.get(i).setBriberyModifier(modifier);
            }
        }
        governmentIndex = lstGovernments.getSelectedIndex();
        if (governmentIndex != -1)
        {
            if (government.get(governmentIndex).getDefaultType() == 0) {
                chkGOVTDefaultType.setSelected(false);
            } else {
                chkGOVTDefaultType.setSelected(true);
            }
            if (government.get(governmentIndex).getTransitionType() == 0) {
                chkGOVTTransitionType.setSelected(false);
            } else {
                chkGOVTTransitionType.setSelected(true);
            }
            if (government.get(governmentIndex).getRequiresMaintenance() == 0) {
                chkGOVTRequiresMaintenance.setSelected(false);
            } else {
                chkGOVTRequiresMaintenance.setSelected(true);
            }
            if (government.get(governmentIndex).getTilePenalty() == 0) {
                chkGOVTTilePenalty.setSelected(false);
            } else {
                chkGOVTTilePenalty.setSelected(true);
            }
            if (government.get(governmentIndex).getCommerceBonus() == 0) {
                chkGOVTTradeBonus.setSelected(false);
            } else {
                chkGOVTTradeBonus.setSelected(true);
            }
            if (government.get(governmentIndex).getXenophobic() == 0) {
                chkGOVTXenophobia.setSelected(false);
            } else {
                chkGOVTXenophobia.setSelected(true);
            }
            if (government.get(governmentIndex).getForceResettlement() == 0) {
                chkGOVTForcedResettlement.setSelected(false);
            } else {
                chkGOVTForcedResettlement.setSelected(true);
            }
            txtGOVTQuestionMarkOne.setText(Integer.toString(government.get(governmentIndex).getQuestionMarkOne()));
            txtGOVTCivilopediaEntry.setText(government.get(governmentIndex).getCivilopediaEntry());
            txtGOVTMaleRulerTitle1.setText(government.get(governmentIndex).getMaleRulerTitle1());
            txtGOVTFemaleRulerTitle1.setText(government.get(governmentIndex).getFemaleRulerTitle1());
            txtGOVTMaleRulerTitle2.setText(government.get(governmentIndex).getMaleRulerTitle2());
            txtGOVTFemaleRulerTitle2.setText(government.get(governmentIndex).getFemaleRulerTitle2());
            txtGOVTMaleRulerTitle3.setText(government.get(governmentIndex).getMaleRulerTitle3());
            txtGOVTFemaleRulerTitle3.setText(government.get(governmentIndex).getFemaleRulerTitle3());
            txtGOVTMaleRulerTitle4.setText(government.get(governmentIndex).getMaleRulerTitle4());
            txtGOVTFemaleRulerTitle4.setText(government.get(governmentIndex).getFemaleRulerTitle4());
            switch (government.get(governmentIndex).getCorruption()) {
                case 0: rbtnGOVTCorruptionMinimal.setSelected(true); break;
                case 1: rbtnGOVTCorruptionNuisance.setSelected(true); break;
                case 2: rbtnGOVTCorruptionProblematic.setSelected(true); break;
                case 3: rbtnGOVTCorruptionRampant.setSelected(true); break;
                case 4: rbtnGOVTCorruptionCatastrophic.setSelected(true); break;
                case 5: rbtnGOVTCorruptionCommunal.setSelected(true); break;
                case 6: rbtnGOVTCorruptionOff.setSelected(true); break;
                default: logger.warn("Type of corruption for government " + government.get(governmentIndex).name + " is not recognized; is " + government.get(governmentIndex).getCorruption());
            }
            cmbGOVTImmuneTo.setSelectedIndex(government.get(governmentIndex).getImmuneTo() + 1);
            cmbGOVTDiplomats.setSelectedIndex(government.get(governmentIndex).getDiplomatLevel());
            cmbGOVTSpies.setSelectedIndex(government.get(governmentIndex).getSpyLevel());
            switch (government.get(governmentIndex).getHurrying()) {
                case 0: rbtnGOVTHurryImpossible.setSelected(true); break;
                case 1: rbtnGOVTHurryForced.setSelected(true); break;
                case 2: rbtnGOVTHurryPaid.setSelected(true); break;
                default: logger.warn("Type of spies for government " + government.get(governmentIndex).name + " is not recognized; is " + government.get(governmentIndex).getSpyLevel());
            }
            txtGOVTAssimilationChance.setText(Integer.toString(government.get(governmentIndex).getAssimilationChance()));
            txtGOVTDraftLimit.setText(Integer.toString(government.get(governmentIndex).getDraftLimit()));
            txtGOVTMilitaryPoliceLimit.setText(Integer.toString(government.get(governmentIndex).getMilitaryPoliceLimit()));
            //reset check boxes
            chkGOVTRulerTitle4.setSelected(false);
            chkGOVTRulerTitle4.setEnabled(false);
            chkGOVTRulerTitle1.setSelected(false);
            chkGOVTRulerTitle1.setEnabled(false);
            chkGOVTRulerTitle2.setSelected(false);
            chkGOVTRulerTitle2.setEnabled(false);
            chkGOVTRulerTitle3.setSelected(false);
            chkGOVTRulerTitle3.setEnabled(false);
            //the lack of break statements in this switch is intentional - the
            //fallthrough behavior should exist
            switch (government.get(governmentIndex).getRulerTitlePairsUsed()) {
                case 4: chkGOVTRulerTitle4.setSelected(true);
                case 3: chkGOVTRulerTitle4.setEnabled(true);
                chkGOVTRulerTitle3.setSelected(true);
                case 2: chkGOVTRulerTitle3.setEnabled(true);
                chkGOVTRulerTitle2.setSelected(true);
                case 1: chkGOVTRulerTitle2.setEnabled(true);
                chkGOVTRulerTitle1.setSelected(true);
                default: chkGOVTRulerTitle1.setEnabled(true);
            }
            cmbGOVTPrerequisiteTechnology.setSelectedIndex(government.get(governmentIndex).getPrerequisiteTechnology() + 1);
            spnrRateCap.setValue(government.get(governmentIndex).getScienceCap() * 10);
            spnrWorkerRate.setValue(government.get(governmentIndex).getWorkerRate() * 50);
            txtGOVTQuestionMarkTwo.setText(Integer.toString(government.get(governmentIndex).getQuestionMarkTwo()));
            txtGOVTQuestionMarkThree.setText(Integer.toString(government.get(governmentIndex).getQuestionMarkThree()));
            txtGOVTQuestionMarkFour.setText(Integer.toString(government.get(governmentIndex).getQuestionMarkFour()));
            txtGOVTFreeUnits.setText(Integer.toString(government.get(governmentIndex).getFreeUnits()));
            if (government.get(governmentIndex).getFreeUnits() == -1)
                chkGOVTAllUnitsFree.setSelected(true);
            else
                chkGOVTAllUnitsFree.setSelected(false);
            txtGOVTFreeUnitsPerTown.setText(Integer.toString(government.get(governmentIndex).getFreeUnitsPerTown()));
            txtGOVTFreeUnitsPerCity.setText(Integer.toString(government.get(governmentIndex).getFreeUnitsPerCity()));
            txtGOVTFreeUnitsPerMetropolis.setText(Integer.toString(government.get(governmentIndex).getFreeUnitsPerMetropolis()));
            txtGOVTCostPerUnit.setText(Integer.toString(government.get(governmentIndex).getCostPerUnit()));
            switch(government.get(governmentIndex).getWarWeariness()) {
                case 0: rbtnGOVTWWNone.setSelected(true); break;
                case 1: rbtnGOVTWWLow.setSelected(true); break;
                case 2: rbtnGOVTWWHigh.setSelected(true); break;
                default: logger.warn("Type of war weariness for government " + government.get(governmentIndex).name + " is not recognized; is " + government.get(governmentIndex).getWarWeariness());

            }
            //Set Modifiers
            //top-left of table is (0, 0)
            for (int i = 0; i < tblModifiers.getRowCount(); i++) {
                //if (logger.isDebugEnabled())
                //  logger.debug(tblModifiers.getValueAt(1, 1));
                tblModifiers.setValueAt(government.get(i).getName(), i, 0);
                tblModifiers.setValueAt(government.get(governmentIndex).relations.get(i).getResistanceModifier(), i, 1);
                tblModifiers.setValueAt(government.get(governmentIndex).relations.get(i).getBriberyModifier(), i, 2);
            }
            //all free, or not all free, that is the question
            if (chkGOVTAllUnitsFree.isSelected())
            {
                txtGOVTFreeUnits.setEnabled(false);
                txtGOVTFreeUnitsPerMetropolis.setEnabled(false);
                txtGOVTFreeUnitsPerCity.setEnabled(false);
                txtGOVTFreeUnitsPerTown.setEnabled(false);
            }
            else
            {
                txtGOVTFreeUnits.setEnabled(true);
                txtGOVTFreeUnitsPerMetropolis.setEnabled(true);
                txtGOVTFreeUnitsPerCity.setEnabled(true);
                txtGOVTFreeUnitsPerTown.setEnabled(true);
            }
        }
    }

    private void chkGOVTRulerTitle1ItemStateChanged(java.awt.event.ItemEvent evt) {
        // :
        if (chkGOVTRulerTitle1.isSelected()) {
            chkGOVTRulerTitle2.setEnabled(true);
        } else {
            chkGOVTRulerTitle2.setEnabled(false);
            chkGOVTRulerTitle2.setSelected(false);
            chkGOVTRulerTitle3.setEnabled(false);
            chkGOVTRulerTitle3.setSelected(false);
            chkGOVTRulerTitle4.setEnabled(false);
            chkGOVTRulerTitle4.setSelected(false);
        }
}

    private void chkGOVTRulerTitle2ItemStateChanged(java.awt.event.ItemEvent evt) {
        // :
        if (chkGOVTRulerTitle2.isSelected()) {
            chkGOVTRulerTitle3.setEnabled(true);
        } else {
            chkGOVTRulerTitle3.setEnabled(false);
            chkGOVTRulerTitle3.setSelected(false);
            chkGOVTRulerTitle4.setEnabled(false);
            chkGOVTRulerTitle4.setSelected(false);
        }
}

    private void chkGOVTRulerTitle3ItemStateChanged(java.awt.event.ItemEvent evt) {
        // :
        if (chkGOVTRulerTitle3.isSelected()) {
            chkGOVTRulerTitle4.setEnabled(true);
        } else {
            chkGOVTRulerTitle4.setEnabled(false);
            chkGOVTRulerTitle4.setSelected(false);
        }
}

    private void chkGOVTRulerTitle4StateChanged(javax.swing.event.ChangeEvent evt) {
        // :
        if (chkGOVTRulerTitle4.isSelected()) {
            txtGOVTMaleRulerTitle4.setEnabled(true);
            txtGOVTFemaleRulerTitle4.setEnabled(true);
        } else {
            txtGOVTMaleRulerTitle4.setEnabled(false);
            txtGOVTFemaleRulerTitle4.setEnabled(false);
        }
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
        addLengthDocumentListener(31, txtGOVTCivilopediaEntry);
        addLengthDocumentListener(31, txtGOVTFemaleRulerTitle1);
        addLengthDocumentListener(31, txtGOVTFemaleRulerTitle2);
        addLengthDocumentListener(31, txtGOVTFemaleRulerTitle3);
        addLengthDocumentListener(31, txtGOVTFemaleRulerTitle4);
        addLengthDocumentListener(31, txtGOVTMaleRulerTitle1);
        addLengthDocumentListener(31, txtGOVTMaleRulerTitle2);
        addLengthDocumentListener(31, txtGOVTMaleRulerTitle3);
        addLengthDocumentListener(31, txtGOVTMaleRulerTitle4);
        //as this only watches for overfuls 'n stuff, don't set any value-based limits
        //do set documen.t listeners on the char array fields (civilopedia and the like)
        int negativeOneK = -1000;
        rateCapModel = new SpinnerNumberModel((Integer)spnrRateCap.getValue(), (Integer)negativeOneK, (Integer)1000, (Integer)10);
        spnrRateCap.setModel(rateCapModel);
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
        //Override the things we know are safe but are outside Firaxis limits
        rateCapModel = new SpinnerNumberModel((Integer)spnrRateCap.getValue(), (Integer)0, (Integer)100, (Integer)10);
        spnrRateCap.setModel(rateCapModel);
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
        //Why am I casting ints to Integers just to have them unboxed again?
        //Because if I don't cast the first one, it complains about an Object
        //And if I cast only that one, the SpinnerNumberModel constructor to
        //be used is ambiguous
        //Thus all parameters must be cast.
        int rateCapValue = (Integer)spnrRateCap.getValue() < 50 ? 50: (Integer)spnrRateCap.getValue();
        rateCapModel = new SpinnerNumberModel((Integer)rateCapValue, (Integer)50, (Integer)100, (Integer)10);
        int workerRateValue = (Integer)spnrWorkerRate.getValue() < 50 ? 50: (Integer)spnrWorkerRate.getValue();
        workerRateModel = new SpinnerNumberModel((Integer)workerRateValue, (Integer)50, (Integer)1000, (Integer)50);
        spnrRateCap.setModel(rateCapModel);
        spnrWorkerRate.setModel(workerRateModel);
        addBadValueDocumentListener(1000, txtGOVTCostPerUnit);
        addBadValueDocumentListener(1000, txtGOVTFreeUnits);
        addBadValueDocumentListener(255, txtGOVTDraftLimit);
        addBadValueDocumentListener(100, txtGOVTMilitaryPoliceLimit);
        addBadValueDocumentListener(100, txtGOVTAssimilationChance);
        addBadValueDocumentListener(100, -100, txtGOVTFreeUnitsPerTown);
        addBadValueDocumentListener(100, -100, txtGOVTFreeUnitsPerCity);
        addBadValueDocumentListener(100, -100, txtGOVTFreeUnitsPerMetropolis);

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
    
    @Override
    public boolean checkBounds(List<String>invalidValues)
    {
        boolean superResult = super.checkBounds(invalidValues);
        if (superResult) {
            return true;
        }
        else if (chkGOVTAllUnitsFree.isSelected() && invalidValues.contains(i18n("govt.freeUnits"))) {
            invalidValues.remove(i18n("govt.freeUnits"));
            if (invalidValues.size() == 0) {
                return true;
            }
        }
        return false;
    }
    
    
    
    public void renameBIQElement(int index, String name)
    {
        government.get(index).name = name;
    }
    
    public void deleteAction()
    {
        //deleteItem(evt);
        int index = lstGovernments.getSelectedIndex();
        if (logger.isInfoEnabled())
            logger.info("index to be deleted: " + index);
        //building dependencies
        if (bldgTab.cmbBLDGReqGovernment.getSelectedIndex() - 1 == index)
            bldgTab.cmbBLDGReqGovernment.setSelectedIndex(0);
        bldgTab.mdlReqGovernment.removeElementAt(index + 1);
        for (BLDG building : bldgTab.buildings)
        {
            building.handleDeletedGovernment(index);
        }
        //civilization dependencies
        if (civTab.cmbRACEFavoriteGovernment.getSelectedIndex() - 1 == index)
            civTab.cmbRACEFavoriteGovernment.setSelectedIndex(0);
        civTab.mdlFavoriteGovernment.removeElementAt(index + 1);
        if (civTab.cmbRACEShunnedGovernment.getSelectedIndex() - 1 == index)
            civTab.cmbRACEShunnedGovernment.setSelectedIndex(0);
        civTab.mdlShunnedGovernment.removeElementAt(index + 1);
        for (int i = 0; i < civTab.civilization.size(); i++)
        {
            civTab.civilization.get(i).handleDeletedGovernment(index);
        }
        governmentIndex = -1;
        
        //player tab dependencies
        if (baselink.hasCustomPlayerData())
        {
            playerTab.deleteGovernment(index);
        }

        //also must update the govt-govt relations table
        tblModifiersModel.removeRow(index);
        //and update govt-govt relations for each govt
        for (int i = 0; i < government.size(); i++)
        {
            //minus one because we haven't deleted the gov't yets
            government.get(i).setNumberOfGovernments(government.size() - 1);
            government.get(i).dataLength-=12;   //one less in govtgovtrelationtable
            GOVTGOVTRelations throwaway = government.get(i).relations.remove(index);
        }
        governmentIndex = -1;

        utils.removeFromList(index, government, governmentList, lstGovernments);
    }
    
    @Override
    public boolean addItem(String name)
    {
        for (int i = 0; i < government.size(); i++)
        {
            //plus one b/c we haven't added the govt yet
            government.get(i).setNumberOfGovernments(government.size() + 1);
            government.get(i).dataLength+=12;   //one less in govtgovtrelationtable
            government.get(i).relations.add(new GOVTGOVTRelations());
        }
        GOVT newGovt = new GOVT(name, government.size() + 1, baselink);
        government.add(newGovt);
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel)tblModifiers.getModel();
        model.addRow(new Object[]{name, 0, 0});

        utils.addWithPossibleDuplicates(name, bldgTab.mdlReqGovernment);
        utils.addWithPossibleDuplicates(name, civTab.mdlFavoriteGovernment);
        utils.addWithPossibleDuplicates(name, civTab.mdlShunnedGovernment);
        return true;
    }
}
