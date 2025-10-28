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
import com.civfanatics.civ3.biqFile.DIFF;
import com.civfanatics.civ3.biqFile.RULE;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJList;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJTextField;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperListModel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
/**
 * A tab for difficulty levels.
 * @author Andrew
 */
public class DIFFTab extends EditorTab {
    public List<DIFF>difficulties;
    boolean tabCreated;
    private SuperListModel difficultyList;
    int difficultyIndex;

    RULETab ruleTab;
    PLYRTab playerTab;

    JMenuItem delete;
    JMenuItem add;
    private JLabel jLabel48;
    private JLabel jLabel49;
    private JLabel jLabel50;
    private JLabel jLabel51;
    private JLabel jLabel52;
    private JLabel jLabel54;
    private JLabel jLabel55;
    private String startLabel = "Start Unit Type ";
    private JLabel lblStartUnitType1;
    private JLabel lblStartUnitType2;
    private JLabel jLabel58;
    private JLabel jLabel59;
    private JLabel jLabel60;
    private JLabel jLabel61;
    private JLabel jLabel62;
    private JPanel jPanel25;
    //private JPanel this;
    private JPanel jPanel43;
    private JPanel jPanel44;
    private JPanel jPanel45;
    private JScrollPane jScrollPane5;
    private SuperJList lstDifficulties = new SuperJList(this, "difficulty level");
    private SuperJTextField txtDIFFAIAITrade;
    private SuperJTextField txtDIFFAIDefenceStart;
    private SuperJTextField txtDIFFAIOffenceStart;
    private SuperJTextField txtDIFFAdditionalFreeSupport;
    private SuperJTextField txtDIFFAttackBarbariansBonus;
    private SuperJTextField txtDIFFBonusPerCity;
    private SuperJTextField txtDIFFContentCitizens;
    private SuperJTextField txtDIFFCorruptionPercent;
    private SuperJTextField txtDIFFCostFactor;
    private SuperJTextField txtDIFFExtraStart1;
    private SuperJTextField txtDIFFExtraStart2;
    private SuperJTextField txtDIFFMaxGovtTransition;
    private SuperJTextField txtDIFFMilitaryLaw;
    private SuperJTextField txtDIFFPercentOptimal;

    public void setSelectedIndex(int i)
    {
        difficultyIndex = i;
    }

    public DIFFTab()
    {
        lstType = lstDifficulties;
        //logger.setLevel(Level.DEBUG);
        tabName = "DIFF";
        textBoxes = new ArrayList<>();
        tabCreated = false;
        //this = new JPanel();
        jScrollPane5 = new JScrollPane();
        jPanel25 = new JPanel();
        jLabel48 = new JLabel();
        jLabel49 = new JLabel();
        jLabel50 = new JLabel();
        jLabel51 = new JLabel();
        jLabel52 = new JLabel();
        txtDIFFContentCitizens = new SuperJTextField();
        txtDIFFMilitaryLaw = new SuperJTextField();
        txtDIFFAttackBarbariansBonus = new SuperJTextField();
        txtDIFFPercentOptimal = new SuperJTextField();
        txtDIFFCorruptionPercent = new SuperJTextField();
        jPanel43 = new JPanel();
        jPanel44 = new JPanel();
        jLabel54 = new JLabel();
        jLabel55 = new JLabel();
        lblStartUnitType1 = new JLabel();
        lblStartUnitType2 = new JLabel();
        txtDIFFAIOffenceStart = new SuperJTextField();
        txtDIFFAIDefenceStart = new SuperJTextField();
        txtDIFFExtraStart1 = new SuperJTextField();
        txtDIFFExtraStart2 = new SuperJTextField();
        jPanel45 = new JPanel();
        jLabel58 = new JLabel();
        jLabel59 = new JLabel();
        txtDIFFAdditionalFreeSupport = new SuperJTextField();
        txtDIFFBonusPerCity = new SuperJTextField();
        jLabel60 = new JLabel();
        jLabel61 = new JLabel();
        jLabel62 = new JLabel();
        txtDIFFMaxGovtTransition = new SuperJTextField();
        txtDIFFCostFactor = new SuperJTextField();
        txtDIFFAIAITrade = new SuperJTextField();
    }
    /**
     * Creates the GUI of the tab.  This includes button groups, layouts, etc.
     * Logically, this is to occur immediately after the constructor is called.
     *
     * @return - A reference to the DIFFTab instance itself.  This reference
     * allows the tab to be added to a tab bar.
     */
    public JPanel createTab()
    {
        lstDifficulties.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstDifficulties.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstDifficultiesValueChanged(evt);
            }
        });
        jScrollPane5.setViewportView(lstDifficulties);

        jPanel25.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Universal Traits"));

        jLabel48.setText("Citizens Born Content:");

        jLabel49.setText("Citizens Quelled Per Military Unit:");

        jLabel50.setText("Attack Bonus vs. Barbarians:");

        jLabel51.setText("Percentage of Optimal Cities:");

        jLabel52.setText("Corruption:");

        org.jdesktop.layout.GroupLayout jPanel25Layout = new org.jdesktop.layout.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel25Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel25Layout.createSequentialGroup()
                        .add(jLabel48)
                        .add(74, 74, 74)
                        .add(txtDIFFContentCitizens, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 63, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel25Layout.createSequentialGroup()
                        .add(jPanel25Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel49)
                            .add(jLabel50)
                            .add(jLabel51)
                            .add(jLabel52))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel25Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(txtDIFFCorruptionPercent)
                            .add(txtDIFFPercentOptimal)
                            .add(txtDIFFMilitaryLaw)
                            .add(txtDIFFAttackBarbariansBonus, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel25Layout.createSequentialGroup()
                .add(jPanel25Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel48)
                    .add(txtDIFFContentCitizens, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel25Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel49)
                    .add(txtDIFFMilitaryLaw, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel25Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel50)
                    .add(txtDIFFAttackBarbariansBonus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel25Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel51)
                    .add(txtDIFFPercentOptimal, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel25Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel52)
                    .add(txtDIFFCorruptionPercent, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel43.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "AI Bonuses"));

        jPanel44.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Additional Starting Units"));

        jLabel54.setText("Offensive Land Units:");

        jLabel55.setText("Defensive Land Units:");

        lblStartUnitType1.setText(startLabel + "1:");

        lblStartUnitType2.setText(startLabel + "2:");

        txtDIFFAIOffenceStart.setText("                   ");

        txtDIFFAIDefenceStart.setText("                   ");

        txtDIFFExtraStart1.setText("                   ");

        txtDIFFExtraStart2.setText("                   ");

        org.jdesktop.layout.GroupLayout jPanel44Layout = new org.jdesktop.layout.GroupLayout(jPanel44);
        jPanel44.setLayout(jPanel44Layout);
        jPanel44Layout.setHorizontalGroup(
            jPanel44Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel44Layout.createSequentialGroup()
                .add(jPanel44Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel44Layout.createSequentialGroup()
                        .add(jLabel54)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(txtDIFFAIOffenceStart, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel44Layout.createSequentialGroup()
                        .add(lblStartUnitType2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(txtDIFFExtraStart2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel44Layout.createSequentialGroup()
                        .add(lblStartUnitType1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(txtDIFFExtraStart1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel44Layout.createSequentialGroup()
                        .add(jLabel55)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(txtDIFFAIDefenceStart, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel44Layout.setVerticalGroup(
            jPanel44Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel44Layout.createSequentialGroup()
                .add(jPanel44Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(txtDIFFAIOffenceStart, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel54))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel44Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel55)
                    .add(txtDIFFAIDefenceStart, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel44Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblStartUnitType1)
                    .add(txtDIFFExtraStart1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel44Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblStartUnitType2)
                    .add(txtDIFFExtraStart2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel45.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Unit Support"));

        jLabel58.setText("Base Bonus:");

        jLabel59.setText("Per Settlement:");

        txtDIFFAdditionalFreeSupport.setText("                   ");

        txtDIFFBonusPerCity.setText("                   ");

        org.jdesktop.layout.GroupLayout jPanel45Layout = new org.jdesktop.layout.GroupLayout(jPanel45);
        jPanel45.setLayout(jPanel45Layout);
        jPanel45Layout.setHorizontalGroup(
            jPanel45Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel45Layout.createSequentialGroup()
                .add(jPanel45Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel45Layout.createSequentialGroup()
                        .add(jLabel58)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 81, Short.MAX_VALUE)
                        .add(txtDIFFAdditionalFreeSupport, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel45Layout.createSequentialGroup()
                        .add(jLabel59)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 64, Short.MAX_VALUE)
                        .add(txtDIFFBonusPerCity, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel45Layout.setVerticalGroup(
            jPanel45Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel45Layout.createSequentialGroup()
                .add(jPanel45Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel58)
                    .add(txtDIFFAdditionalFreeSupport, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel45Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel59)
                    .add(txtDIFFBonusPerCity, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        jLabel60.setText("Max Gov't Transition Time:");

        jLabel61.setText("Cost Factor:");

        jLabel62.setText("AI to AI Trade:");

        txtDIFFMaxGovtTransition.setText("                   ");

        txtDIFFCostFactor.setText("                   ");

        txtDIFFAIAITrade.setText("                   ");

        org.jdesktop.layout.GroupLayout jPanel43Layout = new org.jdesktop.layout.GroupLayout(jPanel43);
        jPanel43.setLayout(jPanel43Layout);
        jPanel43Layout.setHorizontalGroup(
            jPanel43Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel43Layout.createSequentialGroup()
                .add(jPanel43Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel44, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel43Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(jPanel43Layout.createSequentialGroup()
                            .add(jPanel43Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(jLabel60)
                                .add(jLabel61)
                                .add(jLabel62))
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jPanel43Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(txtDIFFAIAITrade, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(txtDIFFCostFactor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(txtDIFFMaxGovtTransition, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .add(jPanel45, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel43Layout.setVerticalGroup(
            jPanel43Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel43Layout.createSequentialGroup()
                .add(jPanel44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 133, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel43Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel60)
                    .add(txtDIFFMaxGovtTransition, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel43Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel61)
                    .add(txtDIFFCostFactor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel43Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel62)
                    .add(txtDIFFAIAITrade, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        org.jdesktop.layout.GroupLayout thisLayout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(thisLayout);
        thisLayout.setHorizontalGroup(
            thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(thisLayout.createSequentialGroup()
                .add(jScrollPane5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 163, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel43, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(558, Short.MAX_VALUE))
        );
        thisLayout.setVerticalGroup(
            thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 679, Short.MAX_VALUE)
            .add(thisLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel43, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(324, 324, 324))
        );

        this.setName("DIFF");

        tabCreated = true;
        return this;
    }

    
    @Override
    public boolean addItem(String name)
    {
        difficulties.add(new DIFF(name, baselink));   
        utils.addWithPossibleDuplicates(name, ruleTab.mdlDefaultDifficulty);
        return true;
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
     * @param difficulties - The list of all the difficulty levels.
     */
    public void sendData(List<DIFF>difficulties)
    {
        assert tabCreated:"Tab must be created before data can be sent to it";
        this.difficulties = difficulties;
        difficultyIndex = -1;

        difficultyList = new SuperListModel();
        lstDifficulties.setModel(difficultyList);


        for (int i = 0; i < difficulties.size(); i++)
        {
            difficultyList.addElement(difficulties.get(i).getName());
        }
    }

    public void sendTabLinks(RULETab ruleTab, PLYRTab playerTab)
    {
        this.ruleTab = ruleTab;
        this.playerTab = playerTab;
    }

    public void sendTabLinks(RULETab ruleTab)
    {
        this.ruleTab = ruleTab;
    }

    private void lstDifficultiesValueChanged(javax.swing.event.ListSelectionEvent evt) {
        updateTab();
    }
    
    public void updateRuleBasedLabels(RULE rules) {
        if (rules.getStartUnit1() != -1) {
            String startUnitType1Name = baselink.unit.get(rules.getStartUnit1()).getName();
            lblStartUnitType1.setText(startLabel + "1 (" + startUnitType1Name + "):");
        }
        if (rules.getStartUnit2() != -1) {
            String startUnitType2Name = baselink.unit.get(rules.getStartUnit2()).getName();
            lblStartUnitType2.setText(startLabel + "2 (" + startUnitType2Name + "):");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void updateTab()
    {
        if (!(difficultyIndex == -1))
        {
            difficulties.get(difficultyIndex).setContentCitizens(txtDIFFContentCitizens.getInteger());
            difficulties.get(difficultyIndex).setMaxGovtTransition(txtDIFFMaxGovtTransition.getInteger());
            difficulties.get(difficultyIndex).setAIDefenceStart(txtDIFFAIDefenceStart.getInteger());
            difficulties.get(difficultyIndex).setAIOffenceStart(txtDIFFAIOffenceStart.getInteger());
            difficulties.get(difficultyIndex).setExtraStart1(txtDIFFExtraStart1.getInteger());
            difficulties.get(difficultyIndex).setExtraStart2(txtDIFFExtraStart2.getInteger());
            difficulties.get(difficultyIndex).setAdditionalFreeSupport(txtDIFFAdditionalFreeSupport.getInteger());
            difficulties.get(difficultyIndex).setBonusPerCity(txtDIFFBonusPerCity.getInteger());
            difficulties.get(difficultyIndex).setAttackBarbariansBonus(txtDIFFAttackBarbariansBonus.getInteger());
            difficulties.get(difficultyIndex).setCostFactor(txtDIFFCostFactor.getInteger());
            difficulties.get(difficultyIndex).setPercentOptimal(txtDIFFPercentOptimal.getInteger());
            difficulties.get(difficultyIndex).setAIAITrade(txtDIFFAIAITrade.getInteger());
            difficulties.get(difficultyIndex).setCorruptionPercent(txtDIFFCorruptionPercent.getInteger());
            difficulties.get(difficultyIndex).setMilitaryLaw(txtDIFFMilitaryLaw.getInteger());

            //Object b = javax.lang.model.util.Elements.getAllMembers(a);
        }
        difficultyIndex = lstDifficulties.getSelectedIndex();
        if (difficultyIndex != -1)
        {
            txtDIFFContentCitizens.setText(Integer.toString(difficulties.get(difficultyIndex).getContentCitizens()));
            txtDIFFMaxGovtTransition.setText(Integer.toString(difficulties.get(difficultyIndex).getMaxGovtTransition()));
            txtDIFFAIDefenceStart.setText(Integer.toString(difficulties.get(difficultyIndex).getAIDefenceStart()));
            txtDIFFAIOffenceStart.setText(Integer.toString(difficulties.get(difficultyIndex).getAIOffenceStart()));
            txtDIFFExtraStart1.setText(Integer.toString(difficulties.get(difficultyIndex).getExtraStart1()));
            txtDIFFExtraStart2.setText(Integer.toString(difficulties.get(difficultyIndex).getExtraStart2()));
            txtDIFFAdditionalFreeSupport.setText(Integer.toString(difficulties.get(difficultyIndex).getAdditionalFreeSupport()));
            txtDIFFBonusPerCity.setText(Integer.toString(difficulties.get(difficultyIndex).getBonusPerCity()));
            txtDIFFAttackBarbariansBonus.setText(Integer.toString(difficulties.get(difficultyIndex).getAttackBarbariansBonus()));
            txtDIFFCostFactor.setText(Integer.toString(difficulties.get(difficultyIndex).getCostFactor()));
            txtDIFFPercentOptimal.setText(Integer.toString(difficulties.get(difficultyIndex).getPercentOptimal()));
            txtDIFFAIAITrade.setText(Integer.toString(difficulties.get(difficultyIndex).getAIAITrade()));
            txtDIFFCorruptionPercent.setText(Integer.toString(difficulties.get(difficultyIndex).getCorruptionPercent()));
            txtDIFFMilitaryLaw.setText(Integer.toString(difficulties.get(difficultyIndex).getMilitaryLaw()));
            updateRuleBasedLabels(baselink.rule.get(0));
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
        if (logger.isDebugEnabled())
            logger.debug("SETTING MINIMAL LIMITS");

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
        addBadValueDocumentListener(1000, txtDIFFPercentOptimal);
        addBadValueDocumentListener(1000, txtDIFFAttackBarbariansBonus);
        addBadValueDocumentListener(1000, 100, txtDIFFAIAITrade);
        addBadValueDocumentListener(255, txtDIFFContentCitizens);
        addBadValueDocumentListener(255, txtDIFFMilitaryLaw);
        addBadValueDocumentListener(200, txtDIFFCorruptionPercent);
        addBadValueDocumentListener(100, txtDIFFExtraStart1);
        addBadValueDocumentListener(100, txtDIFFExtraStart2);
        addBadValueDocumentListener(100, txtDIFFAIOffenceStart);
        addBadValueDocumentListener(100, txtDIFFAIDefenceStart);
        addBadValueDocumentListener(100, txtDIFFBonusPerCity);
        addBadValueDocumentListener(100, txtDIFFAdditionalFreeSupport);
        addBadValueDocumentListener(100, txtDIFFMaxGovtTransition);
        addBadValueDocumentListener(100, txtDIFFCostFactor);
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
    
    public void deleteAction()
    {
        int index = lstDifficulties.getSelectedIndex();
        //Citizen index must be set to -1, or lstCitizens will copy the
        //deleted item's values into the next item
        difficultyIndex = -1;
        //remove dependencies

        //Decrement the default difficulty if we deleted a difficulty below the default one
        for (int i = 0; i < ruleTab.rule.size(); i++)
        {
            ruleTab.rule.get(i).handleDeletedDifficulty(difficultyIndex);
        }
        ruleTab.mdlDefaultDifficulty.removeElementAt(index);
        
        //player tab dependencies
        if (baselink.hasCustomPlayerData())
        {
            playerTab.deleteDifficulty(index);
        }

        utils.removeFromList(index, difficulties, difficultyList, lstDifficulties);
    }
    
    
    public void renameBIQElement(int index, String name)
    {
        difficulties.get(index).setName(name);
    }
}
