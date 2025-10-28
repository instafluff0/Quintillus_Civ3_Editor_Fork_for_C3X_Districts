package com.civfanatics.civ3.xplatformeditor;
/*
 * Bugs
 * TODO: Allow changing the number of spaceship parts.
 */

/**
 *
 * @author Quintillus
 */
import com.civfanatics.civ3.biqFile.DIFF;
import com.civfanatics.civ3.biqFile.BLDG;
import com.civfanatics.civ3.biqFile.RULE;
import com.civfanatics.civ3.biqFile.PRTO;
import com.civfanatics.civ3.biqFile.GOOD;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJTextField;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
public class RULETab extends EditorTab {

    short lastSSPart;
    boolean tabCreated;
    public List<RULE>rule;
    public List<PRTO>unit;
    public List<GOOD>resource;
    public List<DIFF>difficulty;
    public List<BLDG>building;
    private DIFFTab diffTab;
    int ruleIndex;

    private JButton btnRULEUpdate;
    public JComboBox cmbRULEAdvancedBarbarian;
    public JComboBox cmbRULEBarbarianSeaUnit;
    public JComboBox cmbRULEBasicBarbarian;
    public JComboBox cmbRULEBattleCreatedUnit;
    public JComboBox cmbRULEBuildArmyUnit;
    public JComboBox cmbRULEDefaultDifficultyLevel;
    public JComboBox cmbRULEDefaultMoneyResource;
    public JComboBox cmbRULEFlagUnit;
    public JComboBox cmbRULEScout;
    public JComboBox cmbRULESlave;
    public JComboBox cmbRULEStartUnit1;
    public JComboBox cmbRULEStartUnit2;
    public JComboBox cmbRULESpaceshipPart;
    DefaultComboBoxModel mdlSlave= new DefaultComboBoxModel();
    DefaultComboBoxModel mdlStartUnit1 = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlStartUnit2 = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlScout = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlBattleCreatedUnit = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlBasicBarbarian = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlAdvancedBarbarian = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlBuildArmyUnit = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlFlagUnit = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlBarbarianSeaUnit = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlDefaultDifficulty = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlDefaultMoneyResource = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlSpaceshipPart = new DefaultComboBoxModel();    
    private JLabel jLabel10;
    private JLabel jLabel11;
    private JLabel jLabel12;
    private JLabel jLabel248;
    private JLabel jLabel249;
    private JLabel jLabel250;
    private JLabel jLabel251;
    private JLabel jLabel252;
    private JLabel jLabel253;
    private JLabel jLabel254;
    private JLabel jLabel255;
    private JLabel jLabel256;
    private JLabel jLabel257;
    private JLabel jLabel258;
    private JLabel jLabel259;
    private JLabel jLabel260;
    private JLabel jLabel261;
    private JLabel jLabel262;
    private JLabel jLabel263;
    private JLabel jLabel264;
    private JLabel jLabel265;
    private JLabel jLabel266;
    private JLabel jLabel267;
    private JLabel jLabel268;
    private JLabel jLabel269;
    private JLabel jLabel270;
    private JLabel jLabel271;
    private JLabel jLabel272;
    private JLabel jLabel273;
    private JLabel jLabel274;
    private JLabel jLabel275;
    private JLabel jLabel276;
    private JLabel jLabel277;
    private JLabel jLabel278;
    private JLabel jLabel279;
    private JLabel jLabel280;
    private JLabel jLabel281;
    private JLabel jLabel282;
    private JLabel jLabel283;
    private JLabel jLabel284;
    private JLabel jLabel285;
    private JLabel jLabel286;
    private JLabel jLabel287;
    private JLabel jLabel288;
    private JLabel jLabel289;
    private JLabel jLabel290;
    private JLabel jLabel291;
    private JLabel jLabel292;
    private JLabel jLabel293;
    private JLabel jLabel294;
    private JLabel jLabel295;
    private JLabel jLabel8;
    private JLabel jLabel9;
    private JPanel jPanel100;
    private JPanel jPanel101;
    private JPanel jPanel6;
    private JPanel jPanel93;
    private JPanel jPanel94;
    private JPanel jPanel95;
    private JPanel jPanel96;
    private JPanel jPanel97;
    private JPanel jPanel98;
    private JPanel jPanel99;
    private SuperJTextField txtRULENumPartRequired;
    private SuperJTextField txtRULEBaseCapitalizationRate;
    private SuperJTextField txtRULEBuildingDefensiveBonus;
    private SuperJTextField txtRULEChanceOfRioting;
    private SuperJTextField txtRULEChanceToInterceptAirMissions;
    private SuperJTextField txtRULEChanceToInterceptStealthMissions;
    private SuperJTextField txtRULECitiesForArmy;
    private SuperJTextField txtRULECitizenDefensiveBonus;
    private SuperJTextField txtRULECitizenValueInShields;
    private SuperJTextField txtRULECitizensAffectedByHappyFace;
    private SuperJTextField txtRULECityDefenceBonus;
    private SuperJTextField txtRULECityName;
    private SuperJTextField txtRULEDraftTurnPenalty;
    private SuperJTextField txtRULEFoodConsumptionPerCitizen;
    private SuperJTextField txtRULEForestValueInShields;
    private SuperJTextField txtRULEFortificationsDefenceBonus;
    private SuperJTextField txtRULEFortressDefenceBonus;
    private SuperJTextField txtRULEFutureTechCost;
    private SuperJTextField txtRULEGoldenAgeDuration;
    private SuperJTextField txtRULEMaxCity1Size;
    private SuperJTextField txtRULEMaxCity2Size;
    private SuperJTextField txtRULEMaximumResearchTime;
    private SuperJTextField txtRULEMetropolisDefenceBonus;
    private SuperJTextField txtRULEMetropolisName;
    private SuperJTextField txtRULEMinimumResearchTime;
    private SuperJTextField txtRULENumSpaceshipParts;
    private SuperJTextField txtRULEQuestionMark1;
    private SuperJTextField txtRULEQuestionMark2;
    private SuperJTextField txtRULEQuestionMark3;
    private SuperJTextField txtRULEQuestionMark4;
    private SuperJTextField txtRULERiverDefensiveBonus;
    private SuperJTextField txtRULERoadMovementRate;
    private SuperJTextField txtRULEShieldCostInGold;
    private SuperJTextField txtRULEShieldValueInGold;
    private SuperJTextField txtRULEStartingTreasury;
    private SuperJTextField txtRULETownDefenceBonus;
    private SuperJTextField txtRULETownName;
    private SuperJTextField txtRULETurnPenaltyForWhip;
    private SuperJTextField txtRULEUpgradeCost;
    private SuperJTextField txtRULEWLTKDMinimumPop;

    public RULETab()
    {
        tabName = "RULE";
        textBoxes = new ArrayList<>();
        jPanel93 = new JPanel();
        jLabel248 = new JLabel();
        cmbRULESlave = new JComboBox();
        cmbRULEStartUnit1 = new JComboBox();
        jLabel249 = new JLabel();
        cmbRULEStartUnit2 = new JComboBox();
        jLabel250 = new JLabel();
        cmbRULEScout = new JComboBox();
        jLabel251 = new JLabel();
        jLabel252 = new JLabel();
        cmbRULEBattleCreatedUnit = new JComboBox();
        jLabel253 = new JLabel();
        cmbRULEBasicBarbarian = new JComboBox();
        cmbRULEAdvancedBarbarian = new JComboBox();
        jLabel254 = new JLabel();
        jLabel255 = new JLabel();
        cmbRULEBuildArmyUnit = new JComboBox();
        jLabel256 = new JLabel();
        cmbRULEFlagUnit = new JComboBox();
        cmbRULEBarbarianSeaUnit = new JComboBox();
        jLabel257 = new JLabel();
        jPanel94 = new JPanel();
        jLabel258 = new JLabel();
        jLabel259 = new JLabel();
        jLabel260 = new JLabel();
        txtRULEMetropolisName = new SuperJTextField();
        txtRULECityName = new SuperJTextField();
        jLabel261 = new JLabel();
        txtRULETownName = new SuperJTextField();
        txtRULEMaxCity1Size = new SuperJTextField();
        txtRULEMaxCity2Size = new SuperJTextField();
        jLabel262 = new JLabel();
        jPanel95 = new JPanel();
        jLabel263 = new JLabel();
        txtRULEChanceOfRioting = new SuperJTextField();
        jLabel264 = new JLabel();
        txtRULEWLTKDMinimumPop = new SuperJTextField();
        txtRULECitizensAffectedByHappyFace = new SuperJTextField();
        txtRULETurnPenaltyForWhip = new SuperJTextField();
        txtRULEDraftTurnPenalty = new SuperJTextField();
        jLabel265 = new JLabel();
        jLabel266 = new JLabel();
        jLabel267 = new JLabel();
        jPanel96 = new JPanel();
        jLabel268 = new JLabel();
        jLabel269 = new JLabel();
        jLabel270 = new JLabel();
        txtRULEFutureTechCost = new SuperJTextField();
        txtRULEMinimumResearchTime = new SuperJTextField();
        txtRULEMaximumResearchTime = new SuperJTextField();
        jPanel97 = new JPanel();
        jLabel271 = new JLabel();
        txtRULEChanceToInterceptStealthMissions = new SuperJTextField();
        txtRULEChanceToInterceptAirMissions = new SuperJTextField();
        jLabel272 = new JLabel();
        jLabel273 = new JLabel();
        txtRULECitiesForArmy = new SuperJTextField();
        jPanel98 = new JPanel();
        jLabel274 = new JLabel();
        txtRULECitizenValueInShields = new SuperJTextField();
        jLabel275 = new JLabel();
        txtRULEShieldCostInGold = new SuperJTextField();
        jLabel276 = new JLabel();
        txtRULEBaseCapitalizationRate = new SuperJTextField();
        jLabel277 = new JLabel();
        txtRULEForestValueInShields = new SuperJTextField();
        jLabel8 = new JLabel();
        txtRULEShieldValueInGold = new SuperJTextField();
        jPanel99 = new JPanel();
        jLabel278 = new JLabel();
        jLabel279 = new JLabel();
        cmbRULESpaceshipPart = new JComboBox();
        jLabel280 = new JLabel();
        txtRULENumPartRequired = new SuperJTextField();
        txtRULENumSpaceshipParts = new SuperJTextField();
        jPanel100 = new JPanel();
        jLabel281 = new JLabel();
        txtRULEGoldenAgeDuration = new SuperJTextField();
        jLabel282 = new JLabel();
        txtRULERoadMovementRate = new SuperJTextField();
        jLabel283 = new JLabel();
        txtRULEUpgradeCost = new SuperJTextField();
        jLabel284 = new JLabel();
        txtRULEFoodConsumptionPerCitizen = new SuperJTextField();
        jLabel285 = new JLabel();
        txtRULEStartingTreasury = new SuperJTextField();
        jLabel286 = new JLabel();
        cmbRULEDefaultDifficultyLevel = new JComboBox();
        jLabel287 = new JLabel();
        cmbRULEDefaultMoneyResource = new JComboBox();
        jPanel101 = new JPanel();
        jLabel288 = new JLabel();
        jLabel289 = new JLabel();
        jLabel290 = new JLabel();
        jLabel291 = new JLabel();
        jLabel292 = new JLabel();
        jLabel293 = new JLabel();
        jLabel294 = new JLabel();
        jLabel295 = new JLabel();
        txtRULETownDefenceBonus = new SuperJTextField();
        txtRULECityDefenceBonus = new SuperJTextField();
        txtRULEFortressDefenceBonus = new SuperJTextField();
        txtRULEMetropolisDefenceBonus = new SuperJTextField();
        txtRULEBuildingDefensiveBonus = new SuperJTextField();
        txtRULECitizenDefensiveBonus = new SuperJTextField();
        txtRULEFortificationsDefenceBonus = new SuperJTextField();
        txtRULERiverDefensiveBonus = new SuperJTextField();
        btnRULEUpdate = new JButton();
        jPanel6 = new JPanel();
        jLabel9 = new JLabel();
        txtRULEQuestionMark1 = new SuperJTextField();
        txtRULEQuestionMark2 = new SuperJTextField();
        jLabel10 = new JLabel();
        txtRULEQuestionMark3 = new SuperJTextField();
        jLabel11 = new JLabel();
        txtRULEQuestionMark4 = new SuperJTextField();
        jLabel12 = new JLabel();
        ruleIndex = -1;
        lastSSPart = -1;
    }
    /**
     * Tells the tab to set rule set <i>i</i> as the selected item.
     *
     * <B>Note:</B> As there may only logically be one rule set, this method
     * should not be called on this tab, except when switching between files
     * (and in this case EditorTab.java should call this).
     *
     * @param i - The rule set to be selected.  The first rule set is i = 0.
     */
    public void setSelectedIndex(int i)
    {
        ruleIndex = i;
    }
    /**
     * Creates the GUI of the tab.  This includes button groups, layouts, etc.
     * Logically, this is to occur immediately after the constructor is called.
     *
     * @return - A reference to the RULETab instance itself.  This reference
     * allows the tab to be added to a tab bar.
     */
    public JPanel createTab()
    {
        jPanel93.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Default Units"));

        jLabel248.setText("Captured Unit:");

        cmbRULESlave.setModel(mdlSlave);

        cmbRULEStartUnit1.setModel(mdlStartUnit1);

        jLabel249.setText("Start Unit 1:");

        cmbRULEStartUnit2.setModel(mdlStartUnit2);

        jLabel250.setText("Start Unit 2:");

        cmbRULEScout.setModel(mdlScout);

        jLabel251.setText("Scout:");

        jLabel252.setText("Battle-Created:");

        cmbRULEBattleCreatedUnit.setModel(mdlBattleCreatedUnit);

        jLabel253.setText("Adv. Barbarian:");

        cmbRULEBasicBarbarian.setModel(mdlBasicBarbarian);

        cmbRULEAdvancedBarbarian.setModel(mdlAdvancedBarbarian);

        jLabel254.setText("Basic Barbarian:");

        jLabel255.setText("Build-Army:");

        cmbRULEBuildArmyUnit.setModel(mdlBuildArmyUnit);

        jLabel256.setText("Flag Unit:");

        cmbRULEFlagUnit.setModel(mdlFlagUnit);

        cmbRULEBarbarianSeaUnit.setModel(mdlBarbarianSeaUnit);

        jLabel257.setText("Barbarian Ship:");

        org.jdesktop.layout.GroupLayout jPanel93Layout = new org.jdesktop.layout.GroupLayout(jPanel93);
        jPanel93.setLayout(jPanel93Layout);
        jPanel93Layout.setHorizontalGroup(
            jPanel93Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel93Layout.createSequentialGroup()
                .add(jPanel93Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel249)
                    .add(jLabel248)
                    .add(jLabel250)
                    .add(jLabel251)
                    .add(jLabel252)
                    .add(jLabel255)
                    .add(jLabel254)
                    .add(jLabel253)
                    .add(jLabel256)
                    .add(jLabel257))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel93Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(cmbRULEBarbarianSeaUnit, 0, 172, Short.MAX_VALUE)
                    .add(cmbRULEAdvancedBarbarian, 0, 172, Short.MAX_VALUE)
                    .add(cmbRULEBuildArmyUnit, 0, 172, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, cmbRULEBasicBarbarian, 0, 172, Short.MAX_VALUE)
                    .add(cmbRULEBattleCreatedUnit, 0, 172, Short.MAX_VALUE)
                    .add(cmbRULEScout, 0, 172, Short.MAX_VALUE)
                    .add(cmbRULEStartUnit2, 0, 172, Short.MAX_VALUE)
                    .add(cmbRULESlave, 0, 172, Short.MAX_VALUE)
                    .add(cmbRULEStartUnit1, 0, 172, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, cmbRULEFlagUnit, 0, 172, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel93Layout.setVerticalGroup(
            jPanel93Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel93Layout.createSequentialGroup()
                .add(jPanel93Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel248)
                    .add(cmbRULESlave, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel93Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel249)
                    .add(cmbRULEStartUnit1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel93Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel250)
                    .add(cmbRULEStartUnit2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel93Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cmbRULEScout, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel251))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel93Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cmbRULEBattleCreatedUnit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel252))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel93Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cmbRULEBuildArmyUnit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel255))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel93Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cmbRULEBasicBarbarian, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel254))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel93Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cmbRULEAdvancedBarbarian, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel253))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel93Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cmbRULEBarbarianSeaUnit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel257))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel93Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cmbRULEFlagUnit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel256))
                .add(34, 34, 34))
        );

        jPanel94.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "City Size Limits"));
        jPanel94.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel258.setText("Level One:");
        jPanel94.add(jLabel258, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

        jLabel259.setText("Level Two:");
        jPanel94.add(jLabel259, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));

        jLabel260.setText("Level Three:");
        jPanel94.add(jLabel260, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));
        jPanel94.add(txtRULEMetropolisName, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 90, 80, -1));
        jPanel94.add(txtRULECityName, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 70, 80, -1));

        jLabel261.setText("Maximum Size");
        jPanel94.add(jLabel261, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 30, -1, -1));
        jPanel94.add(txtRULETownName, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, 80, -1));
        jPanel94.add(txtRULEMaxCity1Size, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 50, 80, -1));
        jPanel94.add(txtRULEMaxCity2Size, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 70, 80, -1));

        jLabel262.setText("Name");
        jPanel94.add(jLabel262, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, -1, -1));

        jPanel95.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Citizen Mood"));
        jPanel95.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel263.setText("Minimum Population for We Love the King:");
        jPanel95.add(jLabel263, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));
        jPanel95.add(txtRULEChanceOfRioting, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 100, 70, -1));

        jLabel264.setText("Citizens affected by each Happy Face:");
        jPanel95.add(jLabel264, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));
        jPanel95.add(txtRULEWLTKDMinimumPop, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, 70, -1));
        jPanel95.add(txtRULECitizensAffectedByHappyFace, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 40, 70, -1));
        jPanel95.add(txtRULETurnPenaltyForWhip, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 60, 70, -1));
        jPanel95.add(txtRULEDraftTurnPenalty, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 80, 70, -1));

        jLabel265.setText("Turn Penalty for Each Hurry Sacrifice:");
        jPanel95.add(jLabel265, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, -1, -1));

        jLabel266.setText("Turn Penalty for Each Drafted Citizen:");
        jPanel95.add(jLabel266, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 210, -1));

        jLabel267.setText("Chance of Rioting with Unhappiness:");
        jPanel95.add(jLabel267, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, -1, -1));

        jPanel96.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Technology"));
        jPanel96.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel268.setText("Future Tech Cost:");
        jPanel96.add(jLabel268, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jLabel269.setText("Minimum Research Time:");
        jPanel96.add(jLabel269, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jLabel270.setText("Maximum Research Time:");
        jPanel96.add(jLabel270, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));
        jPanel96.add(txtRULEFutureTechCost, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 70, -1));
        jPanel96.add(txtRULEMinimumResearchTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 40, 70, -1));
        jPanel96.add(txtRULEMaximumResearchTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 60, 70, -1));

        jPanel97.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Various Unit Abilities"));
        jPanel97.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel271.setText("Chance of Intercepting Stealth Missions:");
        jPanel97.add(jLabel271, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));
        jPanel97.add(txtRULEChanceToInterceptStealthMissions, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 40, 70, -1));
        jPanel97.add(txtRULEChanceToInterceptAirMissions, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 20, 70, -1));

        jLabel272.setText("Chance of Intercepting Air Missions:");
        jPanel97.add(jLabel272, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jLabel273.setText("Cities Needed to Support an Army:");
        jPanel97.add(jLabel273, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));
        jPanel97.add(txtRULECitiesForArmy, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 60, 70, -1));

        jPanel98.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Hurry Production/Wealth"));
        jPanel98.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel274.setText("Citizen Value in Shields:");
        jPanel98.add(jLabel274, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));
        jPanel98.add(txtRULECitizenValueInShields, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 20, 70, -1));

        jLabel275.setText("Shield Cost in Gold:");
        jPanel98.add(jLabel275, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));
        jPanel98.add(txtRULEShieldCostInGold, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 40, 70, -1));

        jLabel276.setText("Base Capitalization Rate:");
        jPanel98.add(jLabel276, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));
        jPanel98.add(txtRULEBaseCapitalizationRate, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 60, 70, -1));

        jLabel277.setText("Forest Value in Shields:");
        jPanel98.add(jLabel277, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));
        jPanel98.add(txtRULEForestValueInShields, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 80, 70, -1));

        jLabel8.setText("Shield Value in Gold:");
        jPanel98.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));
        jPanel98.add(txtRULEShieldValueInGold, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 100, 70, -1));

        jPanel99.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Spaceship Parts"));
        jPanel99.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel278.setText("Number of Parts:");
        jPanel99.add(jLabel278, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jLabel279.setText("Number of: ");
        jPanel99.add(jLabel279, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, 20));

        cmbRULESpaceshipPart.setModel(mdlSpaceshipPart);
        cmbRULESpaceshipPart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbRULESpaceshipPartActionPerformed(evt);
            }
        });
        jPanel99.add(cmbRULESpaceshipPart, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 50, 100, 20));

        jLabel280.setText("needed:");
        jPanel99.add(jLabel280, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 50, -1, -1));
        jPanel99.add(txtRULENumPartRequired, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 50, 50, -1));
        jPanel99.add(txtRULENumSpaceshipParts, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 70, -1));

        jPanel100.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Other"));
        jPanel100.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel281.setText("Road movement rate:");
        jPanel100.add(jLabel281, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));
        jPanel100.add(txtRULEGoldenAgeDuration, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 100, 70, -1));

        jLabel282.setText("Upgrade Cost:");
        jPanel100.add(jLabel282, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));
        jPanel100.add(txtRULERoadMovementRate, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, 70, -1));

        jLabel283.setText("Food Consumption Per Citizen:");
        jPanel100.add(jLabel283, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));
        jPanel100.add(txtRULEUpgradeCost, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 40, 70, -1));

        jLabel284.setText("Starting Treasury:");
        jPanel100.add(jLabel284, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));
        jPanel100.add(txtRULEFoodConsumptionPerCitizen, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 60, 70, -1));

        jLabel285.setText("Golden Age Duration:");
        jPanel100.add(jLabel285, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));
        jPanel100.add(txtRULEStartingTreasury, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 80, 70, -1));

        jLabel286.setText("Default AI Difficulty:");
        jPanel100.add(jLabel286, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, -1));

        cmbRULEDefaultDifficultyLevel.setModel(mdlDefaultDifficulty);
        jPanel100.add(cmbRULEDefaultDifficultyLevel, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 128, 140, -1));

        jLabel287.setText("Default Money Resource:");
        jPanel100.add(jLabel287, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, -1, -1));

        cmbRULEDefaultMoneyResource.setModel(mdlDefaultMoneyResource);
        jPanel100.add(cmbRULEDefaultMoneyResource, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 160, 140, -1));

        jPanel101.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Defensive Bonuses"));
        jPanel101.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel288.setText("Town:");
        jPanel101.add(jLabel288, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel289.setText("City:");
        jPanel101.add(jLabel289, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        jLabel290.setText("Metropolis:");
        jPanel101.add(jLabel290, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        jLabel291.setText("Fortress:");
        jPanel101.add(jLabel291, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        jLabel292.setText("River:");
        jPanel101.add(jLabel292, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));

        jLabel293.setText("Fortification:");
        jPanel101.add(jLabel293, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, -1));

        jLabel294.setText("Citizen:");
        jPanel101.add(jLabel294, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, -1));

        jLabel295.setText("Building:");
        jPanel101.add(jLabel295, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, -1, -1));
        jPanel101.add(txtRULETownDefenceBonus, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 70, -1));
        jPanel101.add(txtRULECityDefenceBonus, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 40, 70, -1));
        jPanel101.add(txtRULEFortressDefenceBonus, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 70, -1));
        jPanel101.add(txtRULEMetropolisDefenceBonus, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 70, -1));
        jPanel101.add(txtRULEBuildingDefensiveBonus, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 160, 70, -1));
        jPanel101.add(txtRULECitizenDefensiveBonus, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, 70, -1));
        jPanel101.add(txtRULEFortificationsDefenceBonus, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 120, 70, -1));
        jPanel101.add(txtRULERiverDefensiveBonus, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 100, 70, -1));

        btnRULEUpdate.setText("Update");
        btnRULEUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRULEUpdateMouseClicked(evt);
            }
        });

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Unknowns"));

        jLabel9.setText("Unknown 1:");

        jLabel10.setText("Unknown 2:");

        jLabel11.setText("Unknown 3:");

        jLabel12.setText("Unknown 4:");

        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel6Layout.createSequentialGroup()
                        .add(jLabel9)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(txtRULEQuestionMark1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel6Layout.createSequentialGroup()
                        .add(jLabel10)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(txtRULEQuestionMark2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel6Layout.createSequentialGroup()
                        .add(jLabel11)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(txtRULEQuestionMark3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel6Layout.createSequentialGroup()
                        .add(jLabel12)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(txtRULEQuestionMark4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(122, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel9)
                    .add(txtRULEQuestionMark1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel10)
                    .add(txtRULEQuestionMark2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel11)
                    .add(txtRULEQuestionMark3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 7, Short.MAX_VALUE)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel12)
                    .add(txtRULEQuestionMark4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout thisLayout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(thisLayout);
        thisLayout.setHorizontalGroup(
            thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(thisLayout.createSequentialGroup()
                .addContainerGap()
                .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel93, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel99, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 302, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(thisLayout.createSequentialGroup()
                        .add(jPanel94, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 277, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel96, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 253, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnRULEUpdate))
                    .add(thisLayout.createSequentialGroup()
                        .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel98, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
                            .add(jPanel95, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 334, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jPanel100, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel101, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 267, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jPanel97, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 320, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .add(45, 45, 45))
        );
        thisLayout.setVerticalGroup(
            thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(thisLayout.createSequentialGroup()
                .addContainerGap()
                .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(thisLayout.createSequentialGroup()
                        .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(jPanel96, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(jPanel94, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE))
                            .add(btnRULEUpdate))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(thisLayout.createSequentialGroup()
                                .add(jPanel95, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 129, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jPanel98, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 129, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jPanel100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 201, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(thisLayout.createSequentialGroup()
                                .add(jPanel97, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jPanel101, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 196, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                    .add(thisLayout.createSequentialGroup()
                        .add(jPanel93, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 336, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel99, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 85, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(60, 60, 60))
        );

        this.setName("RULE");

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
     * @param rule - The list of all the rules.
     * @param unit - The list of all units.
     * @param difficulty - The list of all difficulty levels.
     * @param resource - The list of all resources.
     * @param building - The list of all buildings.
     */
    public void sendData(List<RULE>rule, List<PRTO>unit, List<DIFF>difficulty, List<GOOD>resource, List<BLDG>building)
    {
        assert tabCreated:"Tab must be created before data can be sent";
        this.rule = rule;
        this.unit = unit;
        this.difficulty = difficulty;
        this.resource = resource;
        this.building = building;

        cmbRULEScout.removeAllItems();
        cmbRULEScout.addItem(noneSelected);
        cmbRULESlave.removeAllItems();
        cmbRULESlave.addItem(noneSelected);
        cmbRULEStartUnit1.removeAllItems();
        cmbRULEStartUnit1.addItem(noneSelected);
        cmbRULEStartUnit2.removeAllItems();
        cmbRULEStartUnit2.addItem(noneSelected);
        cmbRULEBasicBarbarian.removeAllItems();
        cmbRULEBasicBarbarian.addItem(noneSelected);
        cmbRULEAdvancedBarbarian.removeAllItems();
        cmbRULEAdvancedBarbarian.addItem(noneSelected);
        cmbRULEBattleCreatedUnit.removeAllItems();
        cmbRULEBattleCreatedUnit.addItem(noneSelected);
        cmbRULEFlagUnit.removeAllItems();
        cmbRULEFlagUnit.addItem(noneSelected);
        cmbRULEBuildArmyUnit.removeAllItems();
        cmbRULEBuildArmyUnit.addItem(noneSelected);
        cmbRULEBarbarianSeaUnit.removeAllItems();
        cmbRULEBarbarianSeaUnit.addItem(noneSelected);
        cmbRULEDefaultMoneyResource.removeAllItems();
        cmbRULEDefaultMoneyResource.addItem(noneSelected);
        cmbRULEDefaultDifficultyLevel.removeAllItems();
        cmbRULESpaceshipPart.removeAllItems();
        //do not add noneSelected to diff levels

        for (int i = 0; i < unit.size(); i++)
        {
            utils.addWithPossibleDuplicates(unit.get(i).getName(), mdlScout);
            utils.addWithPossibleDuplicates(unit.get(i).getName(), mdlSlave);
            utils.addWithPossibleDuplicates(unit.get(i).getName(), mdlStartUnit1);
            utils.addWithPossibleDuplicates(unit.get(i).getName(), mdlStartUnit2);
            utils.addWithPossibleDuplicates(unit.get(i).getName(), mdlAdvancedBarbarian);
            utils.addWithPossibleDuplicates(unit.get(i).getName(), mdlBasicBarbarian);
            utils.addWithPossibleDuplicates(unit.get(i).getName(), mdlBarbarianSeaUnit);
            utils.addWithPossibleDuplicates(unit.get(i).getName(), mdlFlagUnit);
            utils.addWithPossibleDuplicates(unit.get(i).getName(), mdlBattleCreatedUnit);
            utils.addWithPossibleDuplicates(unit.get(i).getName(), mdlBuildArmyUnit);
        }

        for (int i = 0; i < resource.size(); i++)
        {
            utils.addWithPossibleDuplicates(resource.get(i).getName(), mdlDefaultMoneyResource);
        }

        for (int i = 0; i < difficulty.size(); i++)
        {
            utils.addWithPossibleDuplicates(difficulty.get(i).getName(), mdlDefaultDifficulty);
        }

        //update tab once all data is here
        ruleIndex = -1;
        updateTab();
    }

    //when the spaceship part combo box is updated, must also update the "number required" text box
   private void cmbRULESpaceshipPartActionPerformed(java.awt.event.ActionEvent evt) {
       if (newData)
           return;
       //save the last SSpart
       if (logger.isDebugEnabled())
           logger.debug("Old last part: " + lastSSPart);
       try{
           if (lastSSPart != -1)
               rule.get(ruleIndex).updateNumPartsRequired(lastSSPart, txtRULENumPartRequired.getInteger());
       }
       catch(NumberFormatException e)
       {
           ;    //just leave it as the old value if it ain't a valid integer
       }
       if (logger.isDebugEnabled())
           logger.debug("Selected index: " + (short)cmbRULESpaceshipPart.getSelectedIndex());
       lastSSPart = (short)cmbRULESpaceshipPart.getSelectedIndex();
       if (lastSSPart != -1)
           txtRULENumPartRequired.setText(String.valueOf(rule.get(ruleIndex).getNumberOfSpaceshipPartsRequired(lastSSPart)));
       if (logger.isDebugEnabled())
           logger.debug("New SS Part: " + lastSSPart);
    }

    private void btnRULEUpdateMouseClicked(java.awt.event.MouseEvent evt) {
            // :
            updateTab();
    }

    //TODO: NumberFormatException catching
    public void updateTab()
    {
        if (logger.isDebugEnabled())
            logger.debug(" updating tab for RULE");
        if (!(ruleIndex == -1))
        {
            rule.get(ruleIndex).setTownName(txtRULETownName.getText());
            rule.get(ruleIndex).setCityName(txtRULECityName.getText());
            rule.get(ruleIndex).setMetropolisName(txtRULEMetropolisName.getText());
            if (lastSSPart != -1)
                rule.get(ruleIndex).updateNumPartsRequired(lastSSPart, txtRULENumPartRequired.getInteger());
            rule.get(ruleIndex).setAdvancedBarbarian(cmbRULEAdvancedBarbarian.getSelectedIndex() - 1);
            rule.get(ruleIndex).setBasicBarbarian(cmbRULEBasicBarbarian.getSelectedIndex() - 1);
            rule.get(ruleIndex).setBarbarianSeaUnit(cmbRULEBarbarianSeaUnit.getSelectedIndex() - 1);
            rule.get(ruleIndex).setCitiesForArmy(txtRULECitiesForArmy.getInteger());
            rule.get(ruleIndex).setChanceOfRioting(txtRULEChanceOfRioting.getInteger());
            rule.get(ruleIndex).setDraftTurnPenalty(txtRULEDraftTurnPenalty.getInteger());
            rule.get(ruleIndex).setShieldCostInGold(txtRULEShieldCostInGold.getInteger());
            rule.get(ruleIndex).setFortressDefenceBonus(txtRULEFortressDefenceBonus.getInteger());
            rule.get(ruleIndex).setCitizensAffectedByHappyFace(txtRULECitizensAffectedByHappyFace.getInteger());
            rule.get(ruleIndex).setQuestionMark1(txtRULEQuestionMark1.getInteger());
            rule.get(ruleIndex).setQuestionMark2(txtRULEQuestionMark2.getInteger());
            rule.get(ruleIndex).setForestValueInShields(txtRULEForestValueInShields.getInteger());
            rule.get(ruleIndex).setShieldValueInGold(txtRULEShieldValueInGold.getInteger());
            rule.get(ruleIndex).setCitizenValueInShields(txtRULECitizenValueInShields.getInteger());
            rule.get(ruleIndex).setDefaultDifficultyLevel(cmbRULEDefaultDifficultyLevel.getSelectedIndex());
            rule.get(ruleIndex).setBattleCreatedUnit(cmbRULEBattleCreatedUnit.getSelectedIndex() - 1);
            rule.get(ruleIndex).setBuildArmyUnit(cmbRULEBuildArmyUnit.getSelectedIndex() - 1);
            rule.get(ruleIndex).setBuildingDefensiveBonus(txtRULEBuildingDefensiveBonus.getInteger());
            rule.get(ruleIndex).setCitizenDefensiveBonus(txtRULECitizenDefensiveBonus.getInteger());
            rule.get(ruleIndex).setDefaultMoneyResource(cmbRULEDefaultMoneyResource.getSelectedIndex() - 1);
            rule.get(ruleIndex).setChanceToInterceptAirMissions(txtRULEChanceToInterceptAirMissions.getInteger());
            rule.get(ruleIndex).setChanceToInterceptStealthMissions(txtRULEChanceToInterceptStealthMissions.getInteger());
            rule.get(ruleIndex).setStartingTreasury(txtRULEStartingTreasury.getInteger());
            rule.get(ruleIndex).setQuestionMark3(txtRULEQuestionMark3.getInteger());
            rule.get(ruleIndex).setFoodConsumptionPerCitizen(txtRULEFoodConsumptionPerCitizen.getInteger());
            rule.get(ruleIndex).setRiverDefensiveBonus(txtRULERiverDefensiveBonus.getInteger());
            rule.get(ruleIndex).setTurnPenaltyForWhip(txtRULETurnPenaltyForWhip.getInteger());
            if (logger.isTraceEnabled())
                logger.trace(" saving scout");
            rule.get(ruleIndex).setScout(cmbRULEScout.getSelectedIndex() - 1);
            rule.get(ruleIndex).setSlave(cmbRULESlave.getSelectedIndex() - 1);
            rule.get(ruleIndex).setRoadMovementRate(txtRULERoadMovementRate.getInteger());
            rule.get(ruleIndex).setStartUnit1(cmbRULEStartUnit1.getSelectedIndex() - 1);
            rule.get(ruleIndex).setStartUnit2(cmbRULEStartUnit2.getSelectedIndex() - 1);
            rule.get(ruleIndex).setWLTKDMinimumPop(txtRULEWLTKDMinimumPop.getInteger());
            rule.get(ruleIndex).setTownDefenceBonus(txtRULETownDefenceBonus.getInteger());
            rule.get(ruleIndex).setCityDefenceBonus(txtRULECityDefenceBonus.getInteger());
            rule.get(ruleIndex).setMetropolisDefenceBonus(txtRULEMetropolisDefenceBonus.getInteger());
            rule.get(ruleIndex).setMaxCity1Size(txtRULEMaxCity1Size.getInteger());
            rule.get(ruleIndex).setMaxCity2Size(txtRULEMaxCity2Size.getInteger());
            rule.get(ruleIndex).setQuestionMark4(txtRULEQuestionMark4.getInteger());
            rule.get(ruleIndex).setFortificationsDefenceBonus(txtRULEFortificationsDefenceBonus.getInteger());
            //cultural values within RULE section updated on culture tab
            rule.get(ruleIndex).setFutureTechCost(txtRULEFutureTechCost.getInteger());
            rule.get(ruleIndex).setGoldenAgeDuration(txtRULEGoldenAgeDuration.getInteger());
            rule.get(ruleIndex).setMaximumResearchTime(txtRULEMaximumResearchTime.getInteger());
            rule.get(ruleIndex).setMinimumResearchTime(txtRULEMinimumResearchTime.getInteger());
            rule.get(ruleIndex).setFlagUnit(cmbRULEFlagUnit.getSelectedIndex() - 1);
            rule.get(ruleIndex).setUpgradeCost(txtRULEUpgradeCost.getInteger());
            
            diffTab.updateRuleBasedLabels(rule.get(ruleIndex));
        }
        ruleIndex = 0;
        if (ruleIndex != -1)
        {
            //ruleIndex = lstrule.getSelectedIndex();
            txtRULETownName.setText(rule.get(ruleIndex).getTownName());
            txtRULECityName.setText(rule.get(ruleIndex).getCityName());
            txtRULEMetropolisName.setText(rule.get(ruleIndex).getMetropolisName());
            txtRULENumSpaceshipParts.setText(Integer.toString(rule.get(ruleIndex).getNumSpaceshipParts()));
            if (lastSSPart > -1)
                txtRULENumPartRequired.setText(Integer.toString(rule.get(ruleIndex).getNumberOfSpaceshipPartsRequired(lastSSPart)));
            else
                txtRULENumPartRequired.setText(Integer.toString(rule.get(ruleIndex).getNumberOfSpaceshipPartsRequired(0)));
            cmbRULEAdvancedBarbarian.setSelectedIndex(rule.get(ruleIndex).getAdvancedBarbarian() + 1);
            cmbRULEBasicBarbarian.setSelectedIndex(rule.get(ruleIndex).getBasicBarbarian() + 1);
            cmbRULEBarbarianSeaUnit.setSelectedIndex(rule.get(ruleIndex).getBarbarianSeaUnit() + 1);
            txtRULECitiesForArmy.setText(Integer.toString(rule.get(ruleIndex).getCitiesForArmy()));
            txtRULEChanceOfRioting.setText(Integer.toString(rule.get(ruleIndex).getChanceOfRioting()));
            txtRULEDraftTurnPenalty.setText(Integer.toString(rule.get(ruleIndex).getDraftTurnPenalty()));
            txtRULEShieldCostInGold.setText(Integer.toString(rule.get(ruleIndex).getShieldCostInGold()));
            txtRULEFortressDefenceBonus.setText(Integer.toString(rule.get(ruleIndex).getFortressDefenceBonus()));
            txtRULECitizensAffectedByHappyFace.setText(Integer.toString(rule.get(ruleIndex).getCitizensAffectedByHappyFace()));
            txtRULEQuestionMark1.setText(Integer.toString(rule.get(ruleIndex).getQuestionMark1()));
            txtRULEQuestionMark2.setText(Integer.toString(rule.get(ruleIndex).getQuestionMark2()));
            txtRULEForestValueInShields.setText(Integer.toString(rule.get(ruleIndex).getForestValueInShields()));
            txtRULEShieldValueInGold.setText(Integer.toString(rule.get(ruleIndex).getShieldValueInGold()));
            txtRULECitizenValueInShields.setText(Integer.toString(rule.get(ruleIndex).getCitizenValueInShields()));
            cmbRULEDefaultDifficultyLevel.setSelectedIndex(rule.get(ruleIndex).getDefaultDifficultyLevel());
            cmbRULEBattleCreatedUnit.setSelectedIndex(rule.get(ruleIndex).getBattleCreatedUnit() + 1);
            cmbRULEBuildArmyUnit.setSelectedIndex(rule.get(ruleIndex).getBuildArmyUnit() + 1);
            txtRULEBuildingDefensiveBonus.setText(Integer.toString(rule.get(ruleIndex).getBuildingDefensiveBonus()));
            txtRULECitizenDefensiveBonus.setText(Integer.toString(rule.get(ruleIndex).getCitizenDefensiveBonus()));
            cmbRULEDefaultMoneyResource.setSelectedIndex(rule.get(ruleIndex).getDefaultMoneyResource() + 1);
            txtRULEChanceToInterceptAirMissions.setText(Integer.toString(rule.get(ruleIndex).getChanceToInterceptAirMissions()));
            txtRULEChanceToInterceptStealthMissions.setText(Integer.toString(rule.get(ruleIndex).getChanceToInterceptStealthMissions()));
            txtRULEStartingTreasury.setText(Integer.toString(rule.get(ruleIndex).getStartingTreasury()));
            txtRULEQuestionMark3.setText(Integer.toString(rule.get(ruleIndex).getQuestionMark3()));
            txtRULEFoodConsumptionPerCitizen.setText(Integer.toString(rule.get(ruleIndex).getFoodConsumptionPerCitizen()));
            txtRULERiverDefensiveBonus.setText(Integer.toString(rule.get(ruleIndex).getRiverDefensiveBonus()));
            txtRULETurnPenaltyForWhip.setText(Integer.toString(rule.get(ruleIndex).getTurnPenaltyForWhip()));
            if (logger.isTraceEnabled())
                logger.trace(" Setting Scout: " + rule.get(ruleIndex).getScout());
            cmbRULEScout.setSelectedIndex(rule.get(ruleIndex).getScout() + 1);
            cmbRULESlave.setSelectedIndex(rule.get(ruleIndex).getSlave() + 1);
            txtRULERoadMovementRate.setText(Integer.toString(rule.get(ruleIndex).getRoadMovementRate()));
            cmbRULEStartUnit1.setSelectedIndex(rule.get(ruleIndex).getStartUnit1() + 1);
            cmbRULEStartUnit2.setSelectedIndex(rule.get(ruleIndex).getStartUnit2() + 1);
            txtRULEWLTKDMinimumPop.setText(Integer.toString(rule.get(ruleIndex).getWLTKDMinimumPop()));
            txtRULETownDefenceBonus.setText(Integer.toString(rule.get(ruleIndex).getTownDefenceBonus()));
            txtRULECityDefenceBonus.setText(Integer.toString(rule.get(ruleIndex).getCityDefenceBonus()));
            txtRULEMetropolisDefenceBonus.setText(Integer.toString(rule.get(ruleIndex).getMetropolisDefenceBonus()));
            txtRULEMaxCity1Size.setText(Integer.toString(rule.get(ruleIndex).getMaxCity1Size()));
            txtRULEMaxCity2Size.setText(Integer.toString(rule.get(ruleIndex).getMaxCity2Size()));
            txtRULEQuestionMark4.setText(Integer.toString(rule.get(ruleIndex).getQuestionMark4()));
            txtRULEFortificationsDefenceBonus.setText(Integer.toString(rule.get(ruleIndex).getFortificationsDefenceBonus()));
            //txtRULENumCulturalLevels.setText(Integer.toString(rule.get(ruleIndex).getNumCulturalLevels()));
            //txtRULEBorderExpansionMultiplier.setText(Integer.toString(rule.get(ruleIndex).getBorderExpansionMultiplier()));
            //txtRULEBorderFactor.setText(Integer.toString(rule.get(ruleIndex).getBorderFactor()));
            txtRULEFutureTechCost.setText(Integer.toString(rule.get(ruleIndex).getFutureTechCost()));
            txtRULEGoldenAgeDuration.setText(Integer.toString(rule.get(ruleIndex).getGoldenAgeDuration()));
            txtRULEMaximumResearchTime.setText(Integer.toString(rule.get(ruleIndex).getMaximumResearchTime()));
            txtRULEMinimumResearchTime.setText(Integer.toString(rule.get(ruleIndex).getMinimumResearchTime()));
            cmbRULEFlagUnit.setSelectedIndex(rule.get(ruleIndex).getFlagUnit() + 1);
            txtRULEUpgradeCost.setText(Integer.toString(rule.get(ruleIndex).getUpgradeCost()));

            //store the spaceship parts into the ssparts model
            //first store them in a temporary array, then add them into the model in the correct order
            List<String>ssPartsList = new ArrayList<String>();
            for (int i = 0; i < building.size(); i++)
            {
                if (building.get(i).getSpaceshipPart() > -1)
                {
                    ssPartsList.add(building.get(i).getName());
                }
            }
            mdlSpaceshipPart.removeAllElements();
            for (int i = 0; i < ssPartsList.size(); i++)
            {
                //JOptionPane.showMessageDialog(null, ssParts[i]);
                utils.addWithPossibleDuplicates(ssPartsList.get(i), mdlSpaceshipPart);
            }
            lastSSPart = 0; //first one now selected
        }
    }
    
    public void sendDiffTab(DIFFTab diffTab) {
        this.diffTab = diffTab;
    }

    /**
     * Removes all limits on length and values for text fields.
     */
    public void setNoLimits()
    {
        clearBadValueDocumentListeners();
        clearLengthDocumentListeners();
        if(logger.isDebugEnabled())
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
        //as this only watches for overfuls 'n stuff, don't set any value-based limits
        //do set documen.t listeners on the char array fields (civilopedia and the like)
        addLengthDocumentListener(31, txtRULETownName);
        addLengthDocumentListener(31, txtRULECityName);
        addLengthDocumentListener(31, txtRULEMetropolisName);
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
        //Max Firaxis value = 1 million
        addBadValueDocumentListener(1000000, txtRULEStartingTreasury);
        //Max Firaxis value = 1000
        addBadValueDocumentListener(1000, 1, txtRULERoadMovementRate);
        addBadValueDocumentListener(1000, 1, txtRULEShieldCostInGold);
        addBadValueDocumentListener(1000, 1, txtRULEShieldValueInGold);
        addBadValueDocumentListener(1000, 1, txtRULECitizenValueInShields);
        addBadValueDocumentListener(1000, 1, txtRULECitizensAffectedByHappyFace);
        addBadValueDocumentListener(1000, 1, txtRULEMaximumResearchTime);
        addBadValueDocumentListener(1000, 1, txtRULEMinimumResearchTime);
        addBadValueDocumentListener(1000, 1, txtRULECitiesForArmy);
        addBadValueDocumentListener(1000, txtRULEUpgradeCost);
        addBadValueDocumentListener(1000, txtRULEWLTKDMinimumPop);
        addBadValueDocumentListener(1000, txtRULETurnPenaltyForWhip);
        addBadValueDocumentListener(1000, txtRULEDraftTurnPenalty);
        addBadValueDocumentListener(1000, txtRULEFutureTechCost);
        addBadValueDocumentListener(1000, txtRULEForestValueInShields);
        addBadValueDocumentListener(1000, txtRULEMaxCity1Size);
        addBadValueDocumentListener(1000, txtRULEMaxCity2Size);
        addBadValueDocumentListener(1000, txtRULETownDefenceBonus);
        addBadValueDocumentListener(1000, txtRULECityDefenceBonus);
        addBadValueDocumentListener(1000, txtRULEMetropolisDefenceBonus);
        addBadValueDocumentListener(1000, txtRULEFortressDefenceBonus);
        addBadValueDocumentListener(1000, txtRULERiverDefensiveBonus);
        addBadValueDocumentListener(1000, txtRULEFortificationsDefenceBonus);
        addBadValueDocumentListener(1000, txtRULECitizenDefensiveBonus);
        addBadValueDocumentListener(1000, txtRULEBuildingDefensiveBonus);
        //Max Firaxis value = 100
        addBadValueDocumentListener(100, 1, txtRULEFoodConsumptionPerCitizen);
        addBadValueDocumentListener(100, txtRULEChanceOfRioting);
        addBadValueDocumentListener(100, txtRULEGoldenAgeDuration);
        addBadValueDocumentListener(100, txtRULEChanceToInterceptAirMissions);
        addBadValueDocumentListener(100, txtRULEChanceToInterceptStealthMissions);
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
