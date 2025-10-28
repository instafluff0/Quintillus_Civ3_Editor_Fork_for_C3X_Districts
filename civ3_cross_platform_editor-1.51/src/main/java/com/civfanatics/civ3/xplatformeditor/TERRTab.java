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
import com.civfanatics.civ3.biqFile.GOOD;
import com.civfanatics.civ3.biqFile.TERR;
import com.civfanatics.civ3.biqFile.TRFM;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJList;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJTextField;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperListModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class TERRTab extends EditorTab {

    boolean tabCreated;
    public List<TERR>terrain;
    public List<TRFM>workerJob;
    public List<GOOD>resource;
    int terrainIndex;
    private SuperListModel terrainList;
    DefaultListModel resourceList;

    public TERRTab()
    {
        lstType = lstTerrains;
        tabName = "TERR";
        terrainIndex = -1;
            terrainList = new SuperListModel();
            resourceList = new DefaultListModel();
        //this = new JPanel();
        jScrollPane14 = new JScrollPane();
        jPanel66 = new JPanel();
        jLabel131 = new JLabel();
        jLabel132 = new JLabel();
        jLabel133 = new JLabel();
        txtTERRCivilopediaEntry = new SuperJTextField();
        txtTERRLandmarkCivilopediaEntry = new SuperJTextField();
        jLabel134 = new JLabel();
        jLabel135 = new JLabel();
        jLabel136 = new JLabel();
        txtTERRLandmarkFood = new SuperJTextField();
        txtTERRFood = new SuperJTextField();
        txtTERRLandmarkShields = new SuperJTextField();
        txtTERRShields = new SuperJTextField();
        txtTERRLandmarkCommerce = new SuperJTextField();
        txtTERRCommerce = new SuperJTextField();
        txtTERRLandmarkFoodBonus = new SuperJTextField();
        txtTERRFoodBonus = new SuperJTextField();
        txtTERRLandmarkShieldsBonus = new SuperJTextField();
        txtTERRShieldsBonus = new SuperJTextField();
        txtTERRLandmarkCommerceBonus = new SuperJTextField();
        txtTERRCommerceBonus = new SuperJTextField();
        jLabel137 = new JLabel();
        jLabel138 = new JLabel();
        jLabel139 = new JLabel();
        txtTERRLandmarkMovementCost = new SuperJTextField();
        txtTERRMovementCost = new SuperJTextField();
        jLabel140 = new JLabel();
        txtTERRLandmarkName = new SuperJTextField();
        txtTERRDefenceBonus = new SuperJTextField();
        jLabel141 = new JLabel();
        jLabel142 = new JLabel();
        txtTERRLandmarkDefenceBonus = new SuperJTextField();
        chkTERRLandmarkEnabled = new JCheckBox();
        jLabel143 = new JLabel();
        cmbTERRPollutionEffect = new JComboBox();
        jLabel144 = new JLabel();
        cmbTERRWorkerJob = new JComboBox();
        jPanel67 = new JPanel();
        chkTERRAllowCities = new JCheckBox();
        chkTERRAllowColonies = new JCheckBox();
        chkTERRAllowAirfields = new JCheckBox();
        chkTERRAllowOutposts = new JCheckBox();
        chkTERRAllowRadarTowers = new JCheckBox();
        chkTERRAllowForts = new JCheckBox();
        chkTERRImpassable = new JCheckBox();
        chkTERRImpassableByWheeled = new JCheckBox();
        chkTERRCausesDisease = new JCheckBox();
        chkTERRDiseaseCanBeCured = new JCheckBox();
        txtTERRDiseaseStrength = new SuperJTextField();
        jLabel145 = new JLabel();
        jLabel168 = new JLabel();
        jLabel169 = new JLabel();
        txtTERRQuestionMark2 = new SuperJTextField();
        txtTERRQuestionMark = new SuperJTextField();
        jLabel170 = new JLabel();
        jScrollPane21 = new JScrollPane();
        lstTERRResources = new JList();
    }
    /**
     * Creates the GUI of the tab.  This includes button groups, layouts, etc.
     * Logically, this is to occur immediately after the constructor is called.
     *
     * @return - A reference to the TERRTab instance itself.  This reference
     * allows the tab to be added to a tab bar.
     */
    public JPanel createTab()
    {
        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lstTerrains.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstTerrains.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstTerrainsValueChanged(evt);
            }
        });
        jScrollPane14.setViewportView(lstTerrains);

        this.add(jScrollPane14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 160, 670));

        jPanel66.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel66.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel131.setText("Regular Terrain");
        jPanel66.add(jLabel131, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, -1, -1));

        jLabel132.setText("Landmark Terrain");
        jPanel66.add(jLabel132, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 10, -1, -1));

        jLabel133.setText("Civilopedia Entry");
        jPanel66.add(jLabel133, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, -1));
        jPanel66.add(txtTERRCivilopediaEntry, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 30, 100, -1));
        jPanel66.add(txtTERRLandmarkCivilopediaEntry, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 30, 110, -1));

        jLabel134.setText("Food");
        jPanel66.add(jLabel134, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

        jLabel135.setText("Shields");
        jPanel66.add(jLabel135, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));

        jLabel136.setText("Commerce");
        jPanel66.add(jLabel136, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));
        jPanel66.add(txtTERRLandmarkFood, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 50, 70, -1));
        jPanel66.add(txtTERRFood, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 50, 70, -1));
        jPanel66.add(txtTERRLandmarkShields, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 70, 70, -1));
        jPanel66.add(txtTERRShields, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, 70, -1));
        jPanel66.add(txtTERRLandmarkCommerce, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 90, 70, -1));
        jPanel66.add(txtTERRCommerce, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 90, 70, -1));
        jPanel66.add(txtTERRLandmarkFoodBonus, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 110, 70, -1));
        jPanel66.add(txtTERRFoodBonus, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, 70, -1));
        jPanel66.add(txtTERRLandmarkShieldsBonus, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 130, 70, -1));
        jPanel66.add(txtTERRShieldsBonus, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 130, 70, -1));
        jPanel66.add(txtTERRLandmarkCommerceBonus, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 150, 70, -1));
        jPanel66.add(txtTERRCommerceBonus, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 150, 70, -1));

        jLabel137.setText("Irrigation Bonus");
        jPanel66.add(jLabel137, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, -1));

        jLabel138.setText("Mining Bonus");
        jPanel66.add(jLabel138, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, -1));

        jLabel139.setText("Trade Bonus");
        jPanel66.add(jLabel139, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, -1, -1));
        jPanel66.add(txtTERRLandmarkMovementCost, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 170, 70, -1));
        jPanel66.add(txtTERRMovementCost, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 170, 70, -1));

        jLabel140.setText("Movement Cost");
        jPanel66.add(jLabel140, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, -1, -1));
        jPanel66.add(txtTERRLandmarkName, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 210, 110, -1));
        jPanel66.add(txtTERRDefenceBonus, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 190, 70, -1));

        jLabel141.setText("Defence Bonus");
        jPanel66.add(jLabel141, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, -1, -1));

        jLabel142.setText("Landmark Terrain Name");
        jPanel66.add(jLabel142, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, -1, -1));
        jPanel66.add(txtTERRLandmarkDefenceBonus, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 190, 70, -1));

        chkTERRLandmarkEnabled.setText("Landmark Enabled");
        jPanel66.add(chkTERRLandmarkEnabled, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 230, -1, -1));

        this.add(jPanel66, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 0, 370, 270));

        jLabel143.setText("Pollution Yields:");
        this.add(jLabel143, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 280, -1, -1));

        cmbTERRPollutionEffect.setModel(new javax.swing.DefaultComboBoxModel(new String[] { noneSelected, "Desert", "Plains", "Grassland", "Tundra", "Flood Plain", "Hills", "Mountains", "Forest", "Jungle", "March", "Volcano", "Coast", "Sea", "Ocean", "Base Terrain Type" }));

        this.add(cmbTERRPollutionEffect, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 280, 180, -1));

        jLabel144.setText("Worker Terraform Action:");
        this.add(jLabel144, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 310, -1, -1));

        cmbTERRWorkerJob.setModel(new javax.swing.DefaultComboBoxModel(new String[] { noneSelected, "Plant Forest", "Clear Forest", "Clear Wetlands" }));
        this.add(cmbTERRWorkerJob, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 310, 180, -1));

        jPanel67.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Flags"));

        chkTERRAllowCities.setText("Allow Cities");

        chkTERRAllowColonies.setText("Allow Colonies");

        chkTERRAllowAirfields.setText("Allow Airfields");

        chkTERRAllowOutposts.setText("Allow Outposts");

        chkTERRAllowRadarTowers.setText("Allow Radar Towers");

        chkTERRAllowForts.setText("Allow Forts");

        chkTERRImpassable.setText("Impassable");

        chkTERRImpassableByWheeled.setText("Impassable by Wheeled Units");

        chkTERRCausesDisease.setText("Causes Disease");

        chkTERRDiseaseCanBeCured.setText("Disease cured by sanitation");

        jLabel145.setText("Strength:");

        org.jdesktop.layout.GroupLayout jPanel67Layout = new org.jdesktop.layout.GroupLayout(jPanel67);
        jPanel67.setLayout(jPanel67Layout);
        jPanel67Layout.setHorizontalGroup(
            jPanel67Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel67Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel67Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel67Layout.createSequentialGroup()
                        .add(jPanel67Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(chkTERRAllowCities)
                            .add(chkTERRAllowColonies)
                            .add(chkTERRAllowAirfields)
                            .add(chkTERRAllowOutposts))
                        .add(25, 25, 25)
                        .add(jPanel67Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(chkTERRImpassableByWheeled)
                            .add(chkTERRImpassable)
                            .add(chkTERRAllowForts)
                            .add(chkTERRAllowRadarTowers)))
                    .add(jPanel67Layout.createSequentialGroup()
                        .add(chkTERRCausesDisease)
                        .add(18, 18, 18)
                        .add(chkTERRDiseaseCanBeCured))
                    .add(jPanel67Layout.createSequentialGroup()
                        .add(21, 21, 21)
                        .add(jLabel145)
                        .add(4, 4, 4)
                        .add(txtTERRDiseaseStrength, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 52, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel67Layout.setVerticalGroup(
            jPanel67Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel67Layout.createSequentialGroup()
                .add(jPanel67Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chkTERRAllowCities)
                    .add(chkTERRAllowRadarTowers))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel67Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chkTERRAllowColonies)
                    .add(chkTERRAllowForts))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel67Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chkTERRAllowAirfields)
                    .add(chkTERRImpassable))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel67Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chkTERRAllowOutposts)
                    .add(chkTERRImpassableByWheeled))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel67Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chkTERRCausesDisease)
                    .add(chkTERRDiseaseCanBeCured))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel67Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel145)
                    .add(txtTERRDiseaseStrength, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(100, Short.MAX_VALUE))
        );

        this.add(jPanel67, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 0, 390, 270));

        jLabel168.setText("Unknown 1:");
        this.add(jLabel168, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 280, -1, -1));

        jLabel169.setText("Unknown 2:");
        this.add(jLabel169, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 300, -1, -1));
        this.add(txtTERRQuestionMark2, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 300, 80, -1));
        this.add(txtTERRQuestionMark, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 280, 80, -1));

        jLabel170.setText("Possible Resources");
        this.add(jLabel170, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 340, -1, -1));

        jScrollPane21.setViewportView(lstTERRResources);

        this.add(jScrollPane21, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 360, 170, 300));
        this.setName("TERR");

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
     * @param terrain - The list of all terrains.
     * @param workerJob - The list of all worker jobs.
     * @param resource - The list of all resources.
     */
    public void sendData(List<TERR>terrain, List<TRFM>workerJob, List<GOOD>resource)
    {
        assert tabCreated:"Tab must be created before data can be sent";
        this.terrain = terrain;
        this.workerJob = workerJob;
        this.resource = resource;
        lstTerrains.setModel(terrainList);
        lstTERRResources.setModel(resourceList);
        terrainList.removeAllElements();
        for (int i = 0; i < terrain.size(); i++)
        {
            terrainList.addElement(terrain.get(i).getName());
        }
        initializeResourceList();
    }
    
    void initializeResourceList() {
        resourceList.removeAllElements();
        for (int i = 0; i < resource.size(); i++)
        {
            resourceList.addElement(resource.get(i).getName());
        }
    }

    private void lstTerrainsValueChanged(javax.swing.event.ListSelectionEvent evt) {
        updateTab();
    }

    public void updateTab()
    {
        Byte one = 1;
        Byte zero = 0;
        if (!(terrainIndex == -1)) {
            //Num possible resources - updated from resources page
            terrain.get(terrainIndex).setCivilopediaEntry(txtTERRCivilopediaEntry.getText());
            terrain.get(terrainIndex).setFoodBonus(txtTERRFoodBonus.getInteger());
            terrain.get(terrainIndex).setShieldsBonus(txtTERRShieldsBonus.getInteger());
            terrain.get(terrainIndex).setCommerceBonus(txtTERRCommerceBonus.getInteger());
            terrain.get(terrainIndex).setDefenceBonus(txtTERRDefenceBonus.getInteger());
            terrain.get(terrainIndex).setMovementCost(txtTERRMovementCost.getInteger());
            terrain.get(terrainIndex).setFood(txtTERRFood.getInteger());
            terrain.get(terrainIndex).setShields(txtTERRShields.getInteger());
            terrain.get(terrainIndex).setCommerce(txtTERRCommerce.getInteger());
            if (cmbTERRWorkerJob.getSelectedIndex() == 0) {
                terrain.get(terrainIndex).setWorkerJob(-1);
            } else
                terrain.get(terrainIndex).setWorkerJob(cmbTERRWorkerJob.getSelectedIndex() + 4);
            //Pollution Effect: -1 = None, 15 = Base Type, others correspond to terrain
            terrain.get(terrainIndex).setPollutionEffect(cmbTERRPollutionEffect.getSelectedIndex() - 1);
            if (chkTERRAllowCities.isSelected())
                terrain.get(terrainIndex).setAllowCities(one);
            else
                terrain.get(terrainIndex).setAllowCities(zero);
            if (chkTERRAllowColonies.isSelected())
                terrain.get(terrainIndex).setAllowColonies(one);
            else
                terrain.get(terrainIndex).setAllowColonies(zero);
            if (chkTERRImpassable.isSelected())
                terrain.get(terrainIndex).setImpassable(one);
            else
                terrain.get(terrainIndex).setImpassable(zero);
            if (chkTERRImpassableByWheeled.isSelected())
                terrain.get(terrainIndex).setImpassableByWheeled(one);
            else
                terrain.get(terrainIndex).setImpassableByWheeled(zero);
            if (chkTERRAllowAirfields.isSelected())
                terrain.get(terrainIndex).setAllowAirfields(one);
            else
                terrain.get(terrainIndex).setAllowAirfields(zero);
            if (chkTERRAllowForts.isSelected())
                terrain.get(terrainIndex).setAllowForts(one);
            else
                terrain.get(terrainIndex).setAllowForts(zero);
            if (chkTERRAllowOutposts.isSelected())
                terrain.get(terrainIndex).setAllowOutposts(one);
            else
                terrain.get(terrainIndex).setAllowOutposts(zero);
            if (chkTERRAllowRadarTowers.isSelected())
                terrain.get(terrainIndex).setAllowRadarTowers(one);
            else
                terrain.get(terrainIndex).setAllowRadarTowers(zero);
            terrain.get(terrainIndex).setQuestionMark(txtTERRQuestionMark.getInteger());
            if (chkTERRLandmarkEnabled.isSelected()) {
                terrain.get(terrainIndex).setLandmarkEnabled(one);
            } else {
                terrain.get(terrainIndex).setLandmarkEnabled(zero);
            }
            terrain.get(terrainIndex).setLandmarkFood(txtTERRLandmarkFood.getInteger());
            terrain.get(terrainIndex).setLandmarkShields(txtTERRLandmarkShields.getInteger());
            terrain.get(terrainIndex).setLandmarkCommerce(txtTERRLandmarkCommerce.getInteger());
            terrain.get(terrainIndex).setLandmarkFoodBonus(txtTERRLandmarkFoodBonus.getInteger());
            terrain.get(terrainIndex).setLandmarkShieldsBonus(txtTERRLandmarkShieldsBonus.getInteger());
            terrain.get(terrainIndex).setLandmarkCommerceBonus(txtTERRLandmarkCommerceBonus.getInteger());
            terrain.get(terrainIndex).setLandmarkMovementCost(txtTERRLandmarkMovementCost.getInteger());
            terrain.get(terrainIndex).setLandmarkDefenceBonus(txtTERRLandmarkDefenceBonus.getInteger());
            terrain.get(terrainIndex).setLandmarkName(txtTERRLandmarkName.getText());
            terrain.get(terrainIndex).setLandmarkCivilopediaEntry(txtTERRLandmarkCivilopediaEntry.getText());
            terrain.get(terrainIndex).setQuestionMark2(txtTERRQuestionMark2.getInteger());
            int terrainFlags = 0;
            if (chkTERRCausesDisease.isSelected())
                terrainFlags+=4;
            if (chkTERRDiseaseCanBeCured.isSelected())
                terrainFlags+=8;
            terrain.get(terrainIndex).setTerrainFlags(terrainFlags);
            terrain.get(terrainIndex).setDiseaseStrength(txtTERRDiseaseStrength.getInteger());
            //STORE POSSIBLE RESOURCES!!!!!
            int a = lstTERRResources.getMinSelectionIndex();
            if (a == -1) {   //case for none selected
                if (logger.isDebugEnabled())
                    logger.debug("No resources selected");
                for (int i = 0; i < terrain.get(terrainIndex).allowedResources.size(); i++)
                {
                    terrain.get(terrainIndex).allowedResources.set(i, false);
                }
            } else {
                int[] newResourcesAllowed = lstTERRResources.getSelectedIndices();
                Arrays.sort(newResourcesAllowed);
                for (int i = 0; i < terrain.get(terrainIndex).allowedResources.size(); i++)
                {
                    if (Arrays.binarySearch(newResourcesAllowed, i) >= 0)
                    {
                        terrain.get(terrainIndex).allowedResources.set(i, true);
                    }
                    else
                        terrain.get(terrainIndex).allowedResources.set(i, false);
                }
            }
        }
        terrainIndex = lstTerrains.getSelectedIndex();
        if (terrainIndex != -1)
        {
            txtTERRCivilopediaEntry.setText(terrain.get(terrainIndex).getCivilopediaEntry());
            txtTERRFoodBonus.setText(Integer.toString(terrain.get(terrainIndex).getFoodBonus()));
            txtTERRShieldsBonus.setText(Integer.toString(terrain.get(terrainIndex).getShieldsBonus()));
            txtTERRCommerceBonus.setText(Integer.toString(terrain.get(terrainIndex).getCommerceBonus()));
            txtTERRDefenceBonus.setText(Integer.toString(terrain.get(terrainIndex).getDefenceBonus()));
            txtTERRMovementCost.setText(Integer.toString(terrain.get(terrainIndex).getMovementCost()));
            txtTERRFood.setText(Integer.toString(terrain.get(terrainIndex).getFood()));
            txtTERRShields.setText(Integer.toString(terrain.get(terrainIndex).getShields()));
            txtTERRCommerce.setText(Integer.toString(terrain.get(terrainIndex).getCommerce()));
            switch(terrain.get(terrainIndex).getWorkerJob()) {
                case 5:
                case 6:
                case 7: cmbTERRWorkerJob.setSelectedIndex(terrain.get(terrainIndex).getWorkerJob() - 4); break;
                default: cmbTERRWorkerJob.setSelectedIndex(0); break;
            }
            cmbTERRPollutionEffect.setSelectedIndex(terrain.get(terrainIndex).getPollutionEffect() + 1);
            if (terrain.get(terrainIndex).getAllowCities() == 1) {
                chkTERRAllowCities.setSelected(true);
            } else {
                chkTERRAllowCities.setSelected(false);
            }
            if (terrain.get(terrainIndex).getAllowColonies() == 1) {
                chkTERRAllowColonies.setSelected(true);
            } else {
                chkTERRAllowColonies.setSelected(false);
            }
            if (terrain.get(terrainIndex).getImpassable() == 1) {
                chkTERRImpassable.setSelected(true);
            } else {
                chkTERRImpassable.setSelected(false);
            }
            if (terrain.get(terrainIndex).getImpassableByWheeled() == 1) {
                chkTERRImpassableByWheeled.setSelected(true);
            } else {
                chkTERRImpassableByWheeled.setSelected(false);
            }
            if (terrain.get(terrainIndex).getAllowAirfields() == 1) {
                chkTERRAllowAirfields.setSelected(true);
            } else {
                chkTERRAllowAirfields.setSelected(false);
            }
            if (terrain.get(terrainIndex).getAllowForts() == 1) {
                chkTERRAllowForts.setSelected(true);
            } else {
                chkTERRAllowForts.setSelected(false);
            }
            if (terrain.get(terrainIndex).getAllowOutposts() == 1) {
                chkTERRAllowOutposts.setSelected(true);
            } else {
                chkTERRAllowOutposts.setSelected(false);
            }
            if (terrain.get(terrainIndex).getAllowRadarTowers() == 1) {
                chkTERRAllowRadarTowers.setSelected(true);
            } else {
                chkTERRAllowRadarTowers.setSelected(false);
            }
            txtTERRQuestionMark.setText(Integer.toString(terrain.get(terrainIndex).getQuestionMark()));
            if (terrain.get(terrainIndex).getLandmarkEnabled() == 1) {
                chkTERRLandmarkEnabled.setSelected(true);
            } else {
                chkTERRLandmarkEnabled.setSelected(false);
            }
            txtTERRLandmarkFood.setText(Integer.toString(terrain.get(terrainIndex).getLandmarkFood()));
            txtTERRLandmarkShields.setText(Integer.toString(terrain.get(terrainIndex).getLandmarkShields()));
            txtTERRLandmarkCommerce.setText(Integer.toString(terrain.get(terrainIndex).getLandmarkCommerce()));
            txtTERRLandmarkFoodBonus.setText(Integer.toString(terrain.get(terrainIndex).getLandmarkFoodBonus()));
            txtTERRLandmarkShieldsBonus.setText(Integer.toString(terrain.get(terrainIndex).getLandmarkShieldsBonus()));
            txtTERRLandmarkCommerceBonus.setText(Integer.toString(terrain.get(terrainIndex).getLandmarkCommerceBonus()));
            txtTERRLandmarkMovementCost.setText(Integer.toString(terrain.get(terrainIndex).getLandmarkMovementCost()));
            txtTERRLandmarkDefenceBonus.setText(Integer.toString(terrain.get(terrainIndex).getLandmarkDefenceBonus()));
            txtTERRLandmarkName.setText(terrain.get(terrainIndex).getLandmarkName());
            txtTERRLandmarkCivilopediaEntry.setText(terrain.get(terrainIndex).getLandmarkCivilopediaEntry());
            txtTERRQuestionMark2.setText(Integer.toString(terrain.get(terrainIndex).getQuestionMark2()));
            int terrainFlags = terrain.get(terrainIndex).getTerrainFlags();
            terrainFlags = terrainFlags >>> 2;  //shift logical right by 2
            //As the terrainFlags cannot hold a negative value, % will work properly as modulus
            if (terrainFlags % 2 != 0)
                chkTERRCausesDisease.setSelected(true);
            else
                chkTERRCausesDisease.setSelected(false);
            terrainFlags = terrainFlags >>> 1;
            if (terrainFlags % 2 != 0)
                chkTERRDiseaseCanBeCured.setSelected(true);
            else
                chkTERRDiseaseCanBeCured.setSelected(false);
            txtTERRDiseaseStrength.setText(Integer.toString(terrain.get(terrainIndex).getDiseaseStrength()));
            displayPossibleResources(terrainIndex);
        }
    }

    void displayPossibleResources(int terrainIndex) {
        //LOAD POSSIBLE RESOURCES
        Integer[] indices = new Integer[0];    //size zero ensures it will always be set to the right size
        //after the toArray call
        List<Integer> ind = new ArrayList<Integer>();
        for (int i = 0; i < terrain.get(terrainIndex).allowedResources.size(); i++)
        {
            if (terrain.get(terrainIndex).allowedResources.get(i))
            {
                if (logger.isTraceEnabled())
                    logger.trace("resource " + i + " is allowed");
                ind.add(i);
            }
        }
        //convert array list to array
        indices = (Integer[])ind.toArray(indices);
        //at this point, indices is an array of the type Integer, which is an object
        //We need it to be of type int, a primitive, to set the selected indices
        //This must be done manually - System.arraycopy throws an ArrayStoreException
        //(lame, I know)
        int numSelectedIndices = indices.length;
        int[] indicesPrimitive = new int[numSelectedIndices];
        for (int i = 0; i < numSelectedIndices; i++)
        {
            indicesPrimitive[i] = indices[i];
        }
        if (logger.isDebugEnabled())
            logger.debug("RESOURCES for this TERRAIN: " + terrain.get(terrainIndex).getName());
        for (int i = 0; i < indicesPrimitive.length; i++) {
            if (logger.isTraceEnabled())
                logger.trace(indicesPrimitive[i]);
        }
        if (logger.isDebugEnabled())
            logger.debug("End of terrain tab update");
        lstTERRResources.setSelectedIndices(indicesPrimitive);
    }

    public void setSelectedIndex(int i)
    {
        terrainIndex = i;
    }

    private JCheckBox chkTERRAllowAirfields;
    private JCheckBox chkTERRAllowCities;
    private JCheckBox chkTERRAllowColonies;
    private JCheckBox chkTERRAllowForts;
    private JCheckBox chkTERRAllowOutposts;
    private JCheckBox chkTERRAllowRadarTowers;
    private JCheckBox chkTERRCausesDisease;
    private JCheckBox chkTERRDiseaseCanBeCured;
    private JCheckBox chkTERRImpassable;
    private JCheckBox chkTERRImpassableByWheeled;
    private JCheckBox chkTERRLandmarkEnabled;
    private JComboBox cmbTERRPollutionEffect;
    private JComboBox cmbTERRWorkerJob;
    private JLabel jLabel131;
    private JLabel jLabel132;
    private JLabel jLabel133;
    private JLabel jLabel134;
    private JLabel jLabel135;
    private JLabel jLabel136;
    private JLabel jLabel137;
    private JLabel jLabel138;
    private JLabel jLabel139;
    private JLabel jLabel140;
    private JLabel jLabel141;
    private JLabel jLabel142;
    private JLabel jLabel143;
    private JLabel jLabel144;
    private JLabel jLabel145;
    private JLabel jLabel168;
    private JLabel jLabel169;
    private JLabel jLabel170;
    private JPanel jPanel66;
    private JPanel jPanel67;
    private JScrollPane jScrollPane14;
    private JScrollPane jScrollPane21;
    JList lstTERRResources;
    SuperJList lstTerrains = new SuperJList(this, "terrain", false, false);
    //private JPanel this;
    private SuperJTextField txtTERRCivilopediaEntry;
    private SuperJTextField txtTERRCommerce;
    private SuperJTextField txtTERRCommerceBonus;
    private SuperJTextField txtTERRDefenceBonus;
    private SuperJTextField txtTERRDiseaseStrength;
    private SuperJTextField txtTERRFood;
    private SuperJTextField txtTERRFoodBonus;
    private SuperJTextField txtTERRLandmarkCivilopediaEntry;
    private SuperJTextField txtTERRLandmarkCommerce;
    private SuperJTextField txtTERRLandmarkCommerceBonus;
    private SuperJTextField txtTERRLandmarkDefenceBonus;
    private SuperJTextField txtTERRLandmarkFood;
    private SuperJTextField txtTERRLandmarkFoodBonus;
    private SuperJTextField txtTERRLandmarkMovementCost;
    private SuperJTextField txtTERRLandmarkName;
    private SuperJTextField txtTERRLandmarkShields;
    private SuperJTextField txtTERRLandmarkShieldsBonus;
    private SuperJTextField txtTERRMovementCost;
    private SuperJTextField txtTERRQuestionMark;
    private SuperJTextField txtTERRQuestionMark2;
    private SuperJTextField txtTERRShields;
    private SuperJTextField txtTERRShieldsBonus;


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
        //do set document listeners on the char array fields (civilopedia and the like)
        addLengthDocumentListener(31, txtTERRCivilopediaEntry);
        addLengthDocumentListener(31, txtTERRLandmarkCivilopediaEntry);
        addLengthDocumentListener(31, txtTERRLandmarkName);
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
        //Max Firaxis value = 1000
        addBadValueDocumentListener(1000, txtTERRMovementCost);
        addBadValueDocumentListener(1000, -100, txtTERRDefenceBonus);
        addBadValueDocumentListener(1000, txtTERRLandmarkMovementCost);
        addBadValueDocumentListener(1000, -100, txtTERRLandmarkDefenceBonus);
        addBadValueDocumentListener(100, txtTERRDiseaseStrength);
        addBadValueDocumentListener(25, txtTERRShields);
        addBadValueDocumentListener(25, txtTERRShieldsBonus);
        addBadValueDocumentListener(25, txtTERRFood);
        addBadValueDocumentListener(25, txtTERRFoodBonus);
        addBadValueDocumentListener(25, txtTERRCommerce);
        addBadValueDocumentListener(25, txtTERRCommerceBonus);
        addBadValueDocumentListener(25, txtTERRLandmarkShields);
        addBadValueDocumentListener(25, txtTERRLandmarkShieldsBonus);
        addBadValueDocumentListener(25, txtTERRLandmarkFood);
        addBadValueDocumentListener(25, txtTERRLandmarkFoodBonus);
        addBadValueDocumentListener(25, txtTERRLandmarkCommerce);
        addBadValueDocumentListener(25, txtTERRLandmarkCommerceBonus);

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
    
    
    
    public void renameBIQElement(int index, String name)
    {
        terrain.get(index).name = name;
    }
}
