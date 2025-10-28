package com.civfanatics.civ3.xplatformeditor;
/**
 * Question: Investigate how much Conquests cares about advanceIcons
 * They appear to be pretty much deprecated, but if you set Advanced Armor
 * to icon 467, will it look for 467-AdvancedArmor-large.pcx?
 *
 * @Completed
 * 
 * TODO: It appears that deleting techs doesn't update prerequisites properly
 * when other techs are dependent on the deleted tech
**/
import com.civfanatics.civ3.biqFile.BLDG;
import com.civfanatics.civ3.biqFile.CTZN;
import com.civfanatics.civ3.biqFile.ERAS;
import com.civfanatics.civ3.biqFile.GOOD;
import com.civfanatics.civ3.biqFile.GOVT;
import com.civfanatics.civ3.biqFile.LEAD;
import com.civfanatics.civ3.biqFile.PRTO;
import com.civfanatics.civ3.biqFile.RACE;
import com.civfanatics.civ3.biqFile.TECH;
import com.civfanatics.civ3.biqFile.TRFM;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.ImprovedListView;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJList;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJTextField;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperListModel;
import com.civfanatics.civ3.xplatformeditor.civilopedia.CivilopediaIcon;
import com.civfanatics.civ3.xplatformeditor.specialty.PredicateFactory;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import static java.awt.Image.SCALE_SMOOTH;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.input.DataFormat;
import javafx.scene.paint.Color;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.netbeans.lib.awtextra.AbsoluteConstraints;

/**
 * A tab for technologies.
 * @author Andrew
 */
public class TechTab extends EditorTab {

    boolean tabCreated;    
    public List<TECH>technology;
    public List<ERAS>eras;
    int technologyIndex;
    private SuperListModel technologyList;
    DefaultComboBoxModel mdlEra = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlPrerequisite1 = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlPrerequisite2 = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlPrerequisite3 = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlPrerequisite4 = new DefaultComboBoxModel();    

    private JCheckBox chkTECHBonusTech;
    private JCheckBox chkTECHCannotBeTraded;
    private JCheckBox chkTECHDisablesFloodPlainDisease;
    private JCheckBox chkTECHDoublesWealth;
    private JCheckBox chkTECHDoublesWorkRate;
    private JCheckBox chkTECHEnablesAlliances;
    private JCheckBox chkTECHEnablesBridges;
    private JCheckBox chkTECHEnablesCommunicationTrading;
    private JCheckBox chkTECHEnablesConscription;
    private JCheckBox chkTECHEnablesDiplomats;
    private JCheckBox chkTECHEnablesIrrigationWithoutFreshWater;
    private JCheckBox chkTECHEnablesMPP;
    private JCheckBox chkTECHEnablesMapTrading;
    private JCheckBox chkTECHEnablesMobilizationLevels;
    private JCheckBox chkTECHEnablesOceanTrade;
    private JCheckBox chkTECHEnablesPrecisionBombing;
    private JCheckBox chkTECHEnablesROP;
    private JCheckBox chkTECHEnablesRecycling;
    private JCheckBox chkTECHEnablesSeaTrade;
    private JCheckBox chkTECHEnablesTradeEmbargoes;
    private JCheckBox chkTECHNotRequiredForAdvancement;
    private JCheckBox chkTECHPermitsSacrifice;
    private JCheckBox chkTECHRevealMap;
    private JComboBox cmbTECHEra;
    private JComboBox cmbTECHPrerequisite1;
    private JComboBox cmbTECHPrerequisite2;
    private JComboBox cmbTECHPrerequisite3;
    private JComboBox cmbTECHPrerequisite4;
    private JLabel jLabel72;
    private JLabel jLabel73;
    private JLabel jLabel74;
    private JLabel jLabel75;
    private JLabel jLabel76;
    private JLabel jLabel77;
    private JPanel jPanel48;
    private JPanel jPanel49;
    private JPanel pnlFlags;
    private JPanel pnlDiplomacy;
    private JPanel pnlTrade;
    private JPanel pnlMilitary;
    private JPanel pnlTerrain;
    private JPanel pnlFlavors;
    private JScrollPane jScrollPaneFlavors;
    JList lstFlavors;
    private JScrollPane scrTechs;
    private SuperJList lstTechs = new SuperJList(this, "technology");
    private SuperJTextField txtTECHAdvanceIcon;
    private IconPanel pnlLargeIcon = new IconPanel();
    private SuperJTextField txtTECHCivilopediaEntry;
    private SuperJTextField txtTECHCost;
    private SuperJTextField txtTECHX;
    private SuperJTextField txtTECHY;
    private JTextArea txtScienceAdvisorBlurb = new JTextArea();
    BldgTab bldgTab;
    GoodTab goodTab;
    CIVTab civTab;
    UnitTab unitTab;
    CTZNTab ctznTab;
    GOVTTab govtTab;
    TRFMTab trfmTab;
    PLYRTab playerTab;
    
    //JavaFX stuff
    //This is experimental as of April 2017.  If it goes well, can expand it more generally.
    final JFXPanel jFXPanel = new JFXPanel();    
    ImprovedListView<TECH> techListUI;
    ObservableList<TECH> observableTechList = FXCollections.observableArrayList();
    
    DataFormat techFormat = null;
    
    public TechTab()
    {
        if (Main.settings.useJavaFX) {
            setupJavaFX();
            techFormat = DataFormat.lookupMimeType("com.civfanatics.civ3.biqFile.TECH");
            if (techFormat == null) {
                techFormat = new DataFormat("com.civfanatics.civ3.biqFile.TECH");
            }
        }
        
        lstType = lstTechs;
        tabName = "TECH";
        jLabel72 = new JLabel();
        txtTECHCivilopediaEntry = new SuperJTextField();
        //txtTECHCivilopediaEntry.setRecommended(true);
        jLabel73 = new JLabel();
        cmbTECHEra = new JComboBox();
        jPanel48 = new JPanel();
        cmbTECHPrerequisite1 = new JComboBox();
        cmbTECHPrerequisite2 = new JComboBox();
        cmbTECHPrerequisite3 = new JComboBox();
        cmbTECHPrerequisite4 = new JComboBox();
        jLabel74 = new JLabel();
        txtTECHCost = new SuperJTextField();
        jPanel49 = new JPanel();
        jLabel75 = new JLabel();
        jLabel76 = new JLabel();
        txtTECHX = new SuperJTextField();
        txtTECHY = new SuperJTextField();
        pnlFlags = new JPanel();
        chkTECHNotRequiredForAdvancement = new JCheckBox();
        chkTECHBonusTech = new JCheckBox();
        chkTECHEnablesRecycling = new JCheckBox();
        chkTECHPermitsSacrifice = new JCheckBox();
        chkTECHRevealMap = new JCheckBox();
        pnlDiplomacy = new JPanel();
        chkTECHEnablesAlliances = new JCheckBox();
        chkTECHEnablesROP = new JCheckBox();
        chkTECHEnablesDiplomats = new JCheckBox();
        chkTECHEnablesMPP = new JCheckBox();
        chkTECHEnablesTradeEmbargoes = new JCheckBox();
        chkTECHEnablesMapTrading = new JCheckBox();
        chkTECHEnablesCommunicationTrading = new JCheckBox();
        pnlTrade = new JPanel();
        chkTECHCannotBeTraded = new JCheckBox();
        chkTECHDoublesWealth = new JCheckBox();
        chkTECHEnablesSeaTrade = new JCheckBox();
        chkTECHEnablesOceanTrade = new JCheckBox();
        pnlMilitary = new JPanel();
        chkTECHEnablesBridges = new JCheckBox();
        chkTECHEnablesMobilizationLevels = new JCheckBox();
        chkTECHEnablesConscription = new JCheckBox();
        chkTECHEnablesPrecisionBombing = new JCheckBox();
        pnlTerrain = new JPanel();
        chkTECHEnablesIrrigationWithoutFreshWater = new JCheckBox();
        chkTECHDisablesFloodPlainDisease = new JCheckBox();
        chkTECHDoublesWorkRate = new JCheckBox();
        jLabel77 = new JLabel();
        pnlFlavors = new JPanel();
        jScrollPaneFlavors = new JScrollPane();
        lstFlavors = new JList();
        txtTECHAdvanceIcon = new SuperJTextField();
        scrTechs = new JScrollPane();
        technologyList = new SuperListModel();
        technologyIndex = -1;
    }
    
    private void setupJavaFX() {
        techListUI = new ImprovedListView<>("technology", this.technology, observableTechList, PredicateFactory.createTECHFilter());
        techListUI.setAddAction(this::addItem);
        techListUI.setRenameAction(this::renameItem);
        techListUI.setDeleteAction(this::deleteAction);
        techListUI.setChangeFunction(this::updateTab);
        techListUI.setSwapFunction(this::swapOrder);
    }
    
    /**
     * Creates the GUI of the tab.  This includes button groups, layouts, etc.
     * Logically, this is to occur immediately after the constructor is called.
     *
     * @return - A reference to the TechTab instance itself.  This reference
     * allows the tab to be added to a tab bar.
     */
   public JPanel createTab()
    {
        //Now that the left side will have a resizeable list, sequester the absolute layout to the right side
        //(eventually it will be removed).
        JPanel pnlMainTab = new JPanel();
        
        pnlMainTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel72.setText("Civilopedia Entry:");
        pnlMainTab.add(jLabel72, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, -1, -1));
        pnlMainTab.add(txtTECHCivilopediaEntry, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 160, -1));
        
        JLabel lblScienceAdvisorBlurb = new JLabel("Science Advisor Blurb:");
        txtScienceAdvisorBlurb.setEditable(false);
        txtScienceAdvisorBlurb.setWrapStyleWord(true);
        txtScienceAdvisorBlurb.setLineWrap(true);
        java.awt.Color grey = new java.awt.Color(240, 240, 240);
        txtScienceAdvisorBlurb.setBackground(grey);
        pnlMainTab.add(lblScienceAdvisorBlurb, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 540, -1, -1));
        pnlMainTab.add(txtScienceAdvisorBlurb, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 540, 640, 40));

        jLabel73.setText("Era:");
        pnlMainTab.add(jLabel73, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 30, -1));

        cmbTECHEra.setModel(mdlEra);
        pnlMainTab.add(cmbTECHEra, new org.netbeans.lib.awtextra.AbsoluteConstraints(103, 30, 160, -1));

        jPanel48.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Prerequisites"));

        cmbTECHPrerequisite1.setModel(mdlPrerequisite1);

        cmbTECHPrerequisite2.setModel(mdlPrerequisite2);

        cmbTECHPrerequisite3.setModel(mdlPrerequisite3);

        cmbTECHPrerequisite4.setModel(mdlPrerequisite4);

        org.jdesktop.layout.GroupLayout jPanel48Layout = new org.jdesktop.layout.GroupLayout(jPanel48);
        jPanel48.setLayout(jPanel48Layout);
        jPanel48Layout.setHorizontalGroup(
            jPanel48Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel48Layout.createSequentialGroup()
                .add(jPanel48Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, cmbTECHPrerequisite4, 0, 259, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, cmbTECHPrerequisite3, 0, 259, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, cmbTECHPrerequisite2, 0, 259, Short.MAX_VALUE)
                    .add(cmbTECHPrerequisite1, 0, 259, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel48Layout.setVerticalGroup(
            jPanel48Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel48Layout.createSequentialGroup()
                .add(cmbTECHPrerequisite1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cmbTECHPrerequisite2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cmbTECHPrerequisite3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cmbTECHPrerequisite4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlMainTab.add(jPanel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 10, -1, 150));

        jLabel74.setText("Cost:");
        pnlMainTab.add(jLabel74, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, -1, -1));

        txtTECHCost.setText("                   ");
        pnlMainTab.add(txtTECHCost, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 40, -1));

        jPanel49.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Tech Tree Position"));

        jLabel75.setText("X:");

        jLabel76.setText("Y:");

        txtTECHX.setText("                   ");

        org.jdesktop.layout.GroupLayout jPanel49Layout = new org.jdesktop.layout.GroupLayout(jPanel49);
        jPanel49.setLayout(jPanel49Layout);
        jPanel49Layout.setHorizontalGroup(
            jPanel49Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel49Layout.createSequentialGroup()
                .add(jPanel49Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel49Layout.createSequentialGroup()
                        .add(jLabel75)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(txtTECHX, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel49Layout.createSequentialGroup()
                        .add(jLabel76)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(txtTECHY, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel49Layout.setVerticalGroup(
            jPanel49Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel49Layout.createSequentialGroup()
                .add(jPanel49Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel75)
                    .add(txtTECHX, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel49Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel76)
                    .add(txtTECHY, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        pnlMainTab.add(jPanel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, -1, -1));

        pnlFlags.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Flags"));
        GridBagLayout flagLayout = new GridBagLayout();
        pnlFlags.setLayout(flagLayout);
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        g.gridwidth = 1;
        g.gridheight = 1;
        g.fill = GridBagConstraints.BOTH;

        pnlDiplomacy.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Diplomacy"));

        chkTECHEnablesAlliances.setText("Enables Alliances");
        chkTECHEnablesROP.setText("Enables Right of Passage");
        chkTECHEnablesDiplomats.setText("Enables Diplomats");
        chkTECHEnablesMPP.setText("Enables Mutual Protection Pacts");
        chkTECHEnablesTradeEmbargoes.setText("Enables Trade Embargos");
        chkTECHEnablesMapTrading.setText("Enables Map Trading");

        chkTECHEnablesCommunicationTrading.setText("Enables Communication Trading");
        
        GridLayout pnlDiplomacyLayout = new GridLayout(7,1);
        pnlDiplomacy.setLayout(pnlDiplomacyLayout);
        pnlDiplomacy.add(chkTECHEnablesDiplomats);
        pnlDiplomacy.add(chkTECHEnablesAlliances);
        pnlDiplomacy.add(chkTECHEnablesROP);
        pnlDiplomacy.add(chkTECHEnablesMPP);
        pnlDiplomacy.add(chkTECHEnablesTradeEmbargoes);
        pnlDiplomacy.add(chkTECHEnablesMapTrading);
        pnlDiplomacy.add(chkTECHEnablesCommunicationTrading);
        
        chkTECHCannotBeTraded.setText("Cannot Be Traded");
        chkTECHDoublesWealth.setText("Doubles effect of capitalization");
        chkTECHEnablesSeaTrade.setText("Enables Trade over Seas");
        chkTECHEnablesOceanTrade.setText("Enables Trade over Oceans");

        pnlTrade.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Trade"));
        GridLayout pnlTradeLayout = new GridLayout(4,1);
        pnlTrade.setLayout(pnlTradeLayout);
        pnlTrade.add(chkTECHCannotBeTraded);
        pnlTrade.add(chkTECHDoublesWealth);
        pnlTrade.add(chkTECHEnablesSeaTrade);
        pnlTrade.add(chkTECHEnablesOceanTrade);

        pnlMilitary.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Military"));

        chkTECHEnablesBridges.setText("Enables Bridges");
        chkTECHEnablesMobilizationLevels.setText("Enables Mobilization Levels");
        chkTECHEnablesConscription.setText("Enables Conscription");
        chkTECHEnablesPrecisionBombing.setText("Enables Precision Bombing");

        GridLayout militaryLayout = new GridLayout(4,1);
        pnlMilitary.setLayout(militaryLayout);
        pnlMilitary.add(chkTECHEnablesBridges);
        pnlMilitary.add(chkTECHEnablesConscription);
        pnlMilitary.add(chkTECHEnablesMobilizationLevels);
        pnlMilitary.add(chkTECHEnablesPrecisionBombing);
        
        pnlTerrain.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Terrain"));

        chkTECHEnablesIrrigationWithoutFreshWater.setText("Enables irrigation without water");
        chkTECHDisablesFloodPlainDisease.setText("Disables Flood Plain Disease");
        chkTECHDoublesWorkRate.setText("Doubles worker work rate");
        
        GridLayout terrainLayout = new GridLayout(3,1);
        pnlTerrain.setLayout(terrainLayout);
        pnlTerrain.add(chkTECHEnablesIrrigationWithoutFreshWater);
        pnlTerrain.add(chkTECHDisablesFloodPlainDisease);
        pnlTerrain.add(chkTECHDoublesWorkRate);
        
        chkTECHNotRequiredForAdvancement.setText("Not Required for Era Advancement");
        chkTECHBonusTech.setText("Bonus Technology Awarded");
        chkTECHEnablesRecycling.setText("Enables Recycling");
        chkTECHPermitsSacrifice.setText("Permits Sacrifice");
        chkTECHRevealMap.setText("Reveals World Map");
        
        addComponentRowFirst(pnlFlags,chkTECHNotRequiredForAdvancement,0,0,1,1);
        addComponentRowFirst(pnlFlags,chkTECHBonusTech,0,1,1,1);
        addComponentRowFirst(pnlFlags,chkTECHEnablesRecycling,1,0,1,1);
        addComponentRowFirst(pnlFlags,chkTECHPermitsSacrifice,1,1,1,1);
        addComponentRowFirst(pnlFlags,chkTECHRevealMap,2,1,1,1);
        addComponentRowFirst(pnlFlags,pnlTrade,3,1,5,1);
        addComponentRowFirst(pnlFlags,pnlMilitary,8,1,5,1);
        addComponentRowFirst(pnlFlags,pnlDiplomacy,2,0,8,1);
        addComponentRowFirst(pnlFlags,pnlTerrain,10,0,4,1);
        

        //Set the weights here.  This avoids possible contradictions.
        //Weightx and weighty "don't apply to the component to which they are added to;
        //they apply to that component's column and row."
        flagLayout.columnWeights = new double[] { 0.5,0.5 };
        flagLayout.rowWeights = new double[] { 0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1 };
        
        pnlMainTab.add(pnlFlags, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 490, 370));

        pnlFlavors.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Flavors"));
        GridLayout flavorLayout = new GridLayout(1, 1);
        pnlFlavors.setLayout(flavorLayout);
        jScrollPaneFlavors.setViewportView(lstFlavors);
        pnlFlavors.add(jScrollPaneFlavors);
        
        pnlMainTab.add(pnlFlavors,new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 160, 200, 370));

        jLabel77.setText("Icon:");
        pnlMainTab.add(jLabel77, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, -1, -1));

        txtTECHAdvanceIcon.setText("                   ");
        pnlMainTab.add(txtTECHAdvanceIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 90, 40, -1));
        
        setupPnlLargeIcon();
        pnlMainTab.add(pnlLargeIcon, new AbsoluteConstraints(110, 60, 96, 96));

        lstTechs.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstTechs.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                updateTab();
            }
        });
        scrTechs.setViewportView(lstTechs);

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.WEST;

        if (Main.settings.useJavaFX) {
            this.add(jFXPanel, gbc);
            //frmMainTab.add(jFXPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 170, 670));
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    initializeTechListFX();
                }
            });
        }
        else {
            scrTechs.setPreferredSize(new Dimension(170, 670));
            this.add(scrTechs, gbc);
            //frmMainTab.add(scrTechs, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 170, 670));
        }
        
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 2;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 10, 0, 0);
        this.add(pnlMainTab, gbc);
		
        this.setName("TECH");

        tabCreated = true;
        return this;
    }

    private void setupPnlLargeIcon() {
        pnlLargeIcon.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    File f = CivilopediaIcon.getScenarioSpecificFile(technology.get(technologyIndex), true);
                    Desktop.getDesktop().edit(f);
                }
                catch(IOException ex) {
                    //The "edit" option may not be present on all OSes (notably Windows).
                    //Thus, try "open" as well.
                    if (ex.getMessage().contains("No application is associated")) {
                        try {
                            File f = CivilopediaIcon.getScenarioSpecificFile(technology.get(technologyIndex), true);
                            Desktop.getDesktop().open(f);
                        }
                        catch(IOException ex2) {
                            logger.warn("Could not open tech icon file for editing", ex2);
                        }
                    }
                }
            }
        });
        
        Cursor hand = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        pnlLargeIcon.setCursor(hand);
        pnlLargeIcon.setToolTipText("Click to edit the PCX in your default PCX editing program");
    }
    
    private void initializeTechListFX() {
        Scene scene = new Scene(techListUI, 170, 670);        
        jFXPanel.setScene(scene);
    }
    
    /**
     * Reorders technologies when they are dragged and dropped.  A tech is moved from <i>source</i> to <i>destination + 1</i> in the list.  In other words,
     * it is inserted after the <i>destination</i> tech to which it is dragged.
     * @param source
     * @param destination 
     */
    private void swapOrder(int source, int destination) {
        if (source < destination) {
            technologyIndex = destination;
        }
        else {
            technologyIndex = destination + 1;
        }
        //Because this is invoked after the swap has already happened, the order is different now
        updateOrderInOtherTabs(source, destination, technology.get(technologyIndex).getName());
    }
    
    /**
     * When a tech's order in the list is swapped, this method updates anything
     * that may have been displaying/pointing to the old order.  Notably this
     * includes drop-down lists and int-based pointers in other objects.
     * @param source The index of the item before the update
     * @param destination The index of item that the tech will follow after the update.
     * @param techName The name of the technology that was swapped
     */
    private void updateOrderInOtherTabs(int source, int destination, String techName) {

        /**
         * Most drop-downs have a "None" option, which these are used for.
         * If one does not, it can use the regular source/destination.
         */
        int cmbSource = source + 1;
        int cmbDest = destination + 1;
        
        /**
         * Note that destination is the item our item shall follow.  If we're moving our
         * item later in the list, the others move earlier, so our new index winds up being
         * the destination's old index (it now has its old index minus one).  But if we're moving
         * ours earlier, then the destination's index stays the same, so ours will be its
         * index plus one.
         */
        if (source > destination) {
            cmbDest = cmbDest + 1;
            destination = destination + 1;
        }
        
        //Don't forget the dropdowns on our own tab
        mdlPrerequisite1.removeElementAt(cmbSource);
        mdlPrerequisite1.insertElementAt(techName, cmbDest);
        mdlPrerequisite2.removeElementAt(cmbSource);
        mdlPrerequisite2.insertElementAt(techName, cmbDest);
        mdlPrerequisite3.removeElementAt(cmbSource);
        mdlPrerequisite3.insertElementAt(techName, cmbDest);
        mdlPrerequisite4.removeElementAt(cmbSource);
        mdlPrerequisite4.insertElementAt(techName, cmbDest);
        
        comboBoxSwap(cmbSource, cmbDest, bldgTab.cmbBLDGReqAdvance);
        
        boolean swapBldgObsolete = bldgTab.cmbBLDGObsoleteBy.getSelectedIndex() == cmbSource;
        bldgTab.mdlObsolete.removeElementAt(cmbSource);
        bldgTab.mdlObsolete.insertElementAt(techName, cmbDest);
        if (swapBldgObsolete) {
            bldgTab.cmbBLDGObsoleteBy.setSelectedIndex(cmbDest);
        }
        
        boolean setResourcePrereq = goodTab.cmbGOODPrerequisite.getSelectedIndex() == cmbSource;
        goodTab.mdlPrerequisite.removeElementAt(cmbSource);
        goodTab.mdlPrerequisite.insertElementAt(techName, cmbDest);
        if (setResourcePrereq) {
            goodTab.cmbGOODPrerequisite.setSelectedIndex(cmbDest);
        }
        
        boolean swapFreeTech1 = civTab.cmbRACEFreeTech1Index.getSelectedIndex() == cmbSource;
        boolean swapFreeTech2 = civTab.cmbRACEFreeTech2Index.getSelectedIndex() == cmbSource;
        boolean swapFreeTech3 = civTab.cmbRACEFreeTech3Index.getSelectedIndex() == cmbSource;
        boolean swapFreeTech4 = civTab.cmbRACEFreeTech4Index.getSelectedIndex() == cmbSource;
        civTab.mdlFreeTech1.removeElementAt(cmbSource);
        civTab.mdlFreeTech1.insertElementAt(techName, cmbDest);
        civTab.mdlFreeTech2.removeElementAt(cmbSource);
        civTab.mdlFreeTech2.insertElementAt(techName, cmbDest);
        civTab.mdlFreeTech3.removeElementAt(cmbSource);
        civTab.mdlFreeTech3.insertElementAt(techName, cmbDest);
        civTab.mdlFreeTech4.removeElementAt(cmbSource);
        civTab.mdlFreeTech4.insertElementAt(techName, cmbDest);
        if (swapFreeTech1) {
            civTab.cmbRACEFreeTech1Index.setSelectedIndex(cmbDest);
        }
        if (swapFreeTech2) {
            civTab.cmbRACEFreeTech2Index.setSelectedIndex(cmbDest);
        }
        if (swapFreeTech3) {
            civTab.cmbRACEFreeTech3Index.setSelectedIndex(cmbDest);
        }
        if (swapFreeTech4) {
            civTab.cmbRACEFreeTech4Index.setSelectedIndex(cmbDest);
        }
        
        boolean setCitizenPrereq = ctznTab.cmbCTZNPrerequisite.getSelectedIndex() == cmbSource;
        ctznTab.mdlPrerequisite.removeElementAt(cmbSource);
        ctznTab.mdlPrerequisite.insertElementAt(techName, cmbDest);
        if (setCitizenPrereq) {
            ctznTab.cmbCTZNPrerequisite.setSelectedIndex(cmbDest);
        }
        
        boolean setGovtPrereq = govtTab.cmbGOVTPrerequisiteTechnology.getSelectedIndex() == cmbSource;
        govtTab.mdlPrerequisite.removeElementAt(cmbSource);
        govtTab.mdlPrerequisite.insertElementAt(techName, cmbDest);
        if (setGovtPrereq) {
            govtTab.cmbGOVTPrerequisiteTechnology.setSelectedIndex(cmbDest);
        }
        
        boolean setTerraformPrereq = trfmTab.cmbTRFMRequiredAdvance.getSelectedIndex() == cmbSource;
        trfmTab.mdlPrerequisite.removeElementAt(cmbSource);
        trfmTab.mdlPrerequisite.insertElementAt(techName, cmbDest);
        if (setTerraformPrereq) {
            trfmTab.cmbTRFMRequiredAdvance.setSelectedIndex(cmbDest);
        }
        
        boolean setUnitPrereq = unitTab.cmbPRTORequiredTech.getSelectedIndex() == cmbSource;
        unitTab.mdlPrerequisite.removeElementAt(cmbSource);
        unitTab.mdlPrerequisite.insertElementAt(techName, cmbDest);
        if (setUnitPrereq) {
            unitTab.cmbPRTORequiredTech.setSelectedIndex(cmbDest);
        }
        
        for (BLDG building : baselink.buildings) {
            building.handleSwappedTech();
        }
        for (TECH tech : baselink.technology) {
            tech.handleSwappedTech();
        }
        for (RACE civ : baselink.civilization) {
            civ.handleSwappedTech();
        }
        for (GOOD good : baselink.resource) {
            good.handleSwappedTech();
        }
        for (GOVT govt : baselink.government) {
            govt.handleSwappedTech();
        }
        for (CTZN citizen : baselink.citizens) {
            citizen.handleSwappedTech();
        }
        for (TRFM terraform : baselink.workerJob) {
            terraform.handleSwappedTech();
        }
        for (PRTO prto : baselink.unit) {
            prto.handleSwappedTech();
        }
        
        if (baselink.hasCustomPlayerData()) {
            for (LEAD player : baselink.player) {
                player.updateStartingTechIndices();
                
                playerTab.startingTechnologyModel.remove(source);
                playerTab.startingTechnologyModel.add(destination, techName);
                //player tab tech indexes will be updated when the player next views the tab
                //int[] selectedFreeTechs = utils.integerArrayToIntArray(initialSourceUnit.getStealthTargets().toArray(new Integer[initialSourceUnit.getStealthTargets().size()]));
            }
        }

    }
    
    private void addComponentRowFirst(Container host, Component member, int gridy, int gridx, int height, int width)
    {
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = gridx;
        g.gridy = gridy;
        g.gridheight = height;
        g.gridwidth = width;
        g.fill = GridBagConstraints.BOTH;
        host.add(member,g);
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
     * @param technology - The list of all technologies.
     * @param eras - The list of all eras.
     */
    public void sendData(List<TECH>technology, List<ERAS>eras)
    {
        assert tabCreated:"Tab must be created before data can be sent to it";
        this.technology = technology;
        if (Main.settings.useJavaFX)
            techListUI.setBaseItems(this.technology);
        this.eras = eras;


        lstTechs.setModel(technologyList);

        cmbTECHPrerequisite1.removeAllItems();
        cmbTECHPrerequisite1.addItem(noneSelected);
        cmbTECHPrerequisite2.removeAllItems();
        cmbTECHPrerequisite2.addItem(noneSelected);
        cmbTECHPrerequisite3.removeAllItems();
        cmbTECHPrerequisite3.addItem(noneSelected);
        cmbTECHPrerequisite4.removeAllItems();
        cmbTECHPrerequisite4.addItem(noneSelected);
        cmbTECHEra.removeAllItems();
        cmbTECHEra.addItem(noneSelected);

        for (int i = 0; i < eras.size(); i++)
        {
            utils.addWithPossibleDuplicates(eras.get(i).getName(), mdlEra);
        }

        technologyList.removeAllElements();
        for (int i = 0; i < technology.size(); i++)
        {
            //add technologies to combo lists that need technologies
            technologyList.addElement(technology.get(i).getName());
            
            utils.addWithPossibleDuplicates(technology.get(i).getName(), mdlPrerequisite1);
            utils.addWithPossibleDuplicates(technology.get(i).getName(), mdlPrerequisite2);
            utils.addWithPossibleDuplicates(technology.get(i).getName(), mdlPrerequisite3);
            utils.addWithPossibleDuplicates(technology.get(i).getName(), mdlPrerequisite4);
        }
        
        if (Main.settings.useJavaFX) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < technology.size(); i++)
                    {
                        observableTechList.add(technology.get(i));
                    }
                }
            });
        }
        
        
        //this.dependencyTree();
    }

    public void sendTabLinks(GoodTab resource, BldgTab building, CIVTab civilization, UnitTab unit, CTZNTab citizen, GOVTTab government, TRFMTab terraform)
    {
        this.goodTab = resource;
        this.bldgTab = building;
        this.civTab = civilization;
        this.unitTab = unit;
        this.ctznTab = citizen;
        this.govtTab = government;
        this.trfmTab = terraform;
    }
    
    public void sendPlayerTab(PLYRTab playerTab)
    {
        this.playerTab = playerTab;
    }
   
   public void updateTab() {
       updateTab(null);
   }

    /**
     * {@inheritDoc}
     */
   public void updateTab(TECH newlySelectedTech)
   {
        if (logger.isDebugEnabled()) {
            logger.debug("Updating tech tab");
        }
       
        if (!(technologyIndex == -1)) {
            TECH techToUpdate = technology.get(technologyIndex);
            techToUpdate.setCivilopediaEntry(txtTECHCivilopediaEntry.getText());
            techToUpdate.setCost(txtTECHCost.getInteger());
            techToUpdate.setEra(cmbTECHEra.getSelectedIndex() - 1);
            techToUpdate.setAdvanceIcon(txtTECHAdvanceIcon.getInteger());
            techToUpdate.setX(txtTECHX.getInteger());
            techToUpdate.setY(txtTECHY.getInteger());
            techToUpdate.setPrerequisite1(cmbTECHPrerequisite1.getSelectedIndex() - 1);
            techToUpdate.setPrerequisite2(cmbTECHPrerequisite2.getSelectedIndex() - 1);
            techToUpdate.setPrerequisite3(cmbTECHPrerequisite3.getSelectedIndex() - 1);
            techToUpdate.setPrerequisite4(cmbTECHPrerequisite4.getSelectedIndex() - 1);
            techToUpdate.setEnablesDiplomats(chkTECHEnablesDiplomats.isSelected());
            techToUpdate.setEnablesIrrigationWithoutFreshWater(chkTECHEnablesIrrigationWithoutFreshWater.isSelected());
            techToUpdate.setEnablesBridges(chkTECHEnablesBridges.isSelected());
            techToUpdate.setDisablesFloodPlainDisease(chkTECHDisablesFloodPlainDisease.isSelected());
            techToUpdate.setEnablesConscription(chkTECHEnablesConscription.isSelected());
            techToUpdate.setEnablesMobilizationLevels(chkTECHEnablesMobilizationLevels.isSelected());
            techToUpdate.setEnablesRecycling(chkTECHEnablesRecycling.isSelected());
            techToUpdate.setEnablesPrecisionBombing(chkTECHEnablesPrecisionBombing.isSelected());
            techToUpdate.setEnablesMPP(chkTECHEnablesMPP.isSelected());
            techToUpdate.setEnablesROP(chkTECHEnablesROP.isSelected());
            techToUpdate.setEnablesAlliances(chkTECHEnablesAlliances.isSelected());
            techToUpdate.setEnablesTradeEmbargoes(chkTECHEnablesTradeEmbargoes.isSelected());
            techToUpdate.setDoublesWealth(chkTECHDoublesWealth.isSelected());
            techToUpdate.setEnablesSeaTrade(chkTECHEnablesSeaTrade.isSelected());
            techToUpdate.setEnablesOceanTrade(chkTECHEnablesOceanTrade.isSelected());
            techToUpdate.setEnablesMapTrading(chkTECHEnablesMapTrading.isSelected());
            techToUpdate.setEnablesCommunicationTrading(chkTECHEnablesCommunicationTrading.isSelected());
            techToUpdate.setNotRequiredForAdvancement(chkTECHNotRequiredForAdvancement.isSelected());
            techToUpdate.setDoublesWorkRate(chkTECHDoublesWorkRate.isSelected());
            techToUpdate.setCannotBeTraded(chkTECHCannotBeTraded.isSelected());
            techToUpdate.setPermitsSacrifice(chkTECHPermitsSacrifice.isSelected());
            techToUpdate.setBonusTech(chkTECHBonusTech.isSelected());
            techToUpdate.setRevealMap(chkTECHRevealMap.isSelected());
            //save flavour data
            int[]selectedFlavs = lstFlavors.getSelectedIndices();
            for (int i = 0; i < techToUpdate.getNumFlavours(); i++)
                techToUpdate.setFlavour(i, false);
            for (int i = 0; i < selectedFlavs.length; i++)
                techToUpdate.setFlavour(selectedFlavs[i], true);
        }
        if (!Main.settings.useJavaFX) {
            technologyIndex = lstTechs.getSelectedIndex();
        }
        else {
            if (newlySelectedTech != null) {
                technologyIndex = newlySelectedTech.getIndex();
            }
            else {
                technologyIndex = -1;
            }
        }
        if (technologyIndex != -1)
        {
            TECH displayedTech = technology.get(technologyIndex);
            txtTECHCivilopediaEntry.setText(displayedTech.getCivilopediaEntry());
            txtTECHCost.setText(Integer.toString(displayedTech.getCost()));
            cmbTECHEra.setSelectedIndex(displayedTech.getEra() + 1);
            txtTECHAdvanceIcon.setText(Integer.toString(displayedTech.getAdvanceIcon()));
            txtTECHX.setText(Integer.toString(displayedTech.getX()));
            txtTECHY.setText(Integer.toString(displayedTech.getY()));
            cmbTECHPrerequisite1.setSelectedIndex(displayedTech.getPrerequisite1() + 1);
            cmbTECHPrerequisite2.setSelectedIndex(displayedTech.getPrerequisite2() + 1);
            cmbTECHPrerequisite3.setSelectedIndex(displayedTech.getPrerequisite3() + 1);
            cmbTECHPrerequisite4.setSelectedIndex(displayedTech.getPrerequisite4() + 1);
            //txtTECHFlags.setText(Integer.toString(displayedTech.getFlags()));
            if (displayedTech.getEnablesDiplomats()) {
                chkTECHEnablesDiplomats.setSelected(true);
            } else {
                chkTECHEnablesDiplomats.setSelected(false);
            }
            if (displayedTech.getEnablesIrrigationWithoutFreshWater()) {
                chkTECHEnablesIrrigationWithoutFreshWater.setSelected(true);
            } else {
                chkTECHEnablesIrrigationWithoutFreshWater.setSelected(false);
            }
            if (displayedTech.getEnablesBridges()) {
                chkTECHEnablesBridges.setSelected(true);
            } else {
                chkTECHEnablesBridges.setSelected(false);
            }
            if (displayedTech.getDisablesFloodPlainDisease()) {
                chkTECHDisablesFloodPlainDisease.setSelected(true);
            } else {
                chkTECHDisablesFloodPlainDisease.setSelected(false);
            }
            if (displayedTech.getEnablesConscription()) {
                chkTECHEnablesConscription.setSelected(true);
            } else {
                chkTECHEnablesConscription.setSelected(false);
            }
            if (displayedTech.getEnablesMobilizationLevels()) {
                chkTECHEnablesMobilizationLevels.setSelected(true);
            } else {
                chkTECHEnablesMobilizationLevels.setSelected(false);
            }
            if (displayedTech.getEnablesRecycling()) {
                chkTECHEnablesRecycling.setSelected(true);
            } else {
                chkTECHEnablesRecycling.setSelected(false);
            }
            if (displayedTech.getEnablesPrecisionBombing()) {
                chkTECHEnablesPrecisionBombing.setSelected(true);
            } else {
                chkTECHEnablesPrecisionBombing.setSelected(false);
            }
            if (displayedTech.getEnablesMPP()) {
                chkTECHEnablesMPP.setSelected(true);
            } else {
                chkTECHEnablesMPP.setSelected(false);
            }
            if (displayedTech.getEnablesROP()) {
                chkTECHEnablesROP.setSelected(true);
            } else {
                chkTECHEnablesROP.setSelected(false);
            }
            if (displayedTech.getEnablesAlliances()) {
                chkTECHEnablesAlliances.setSelected(true);
            } else {
                chkTECHEnablesAlliances.setSelected(false);
            }
            if (displayedTech.getEnablesTradeEmbargoes()) {
                chkTECHEnablesTradeEmbargoes.setSelected(true);
            } else {
                chkTECHEnablesTradeEmbargoes.setSelected(false);
            }
            if (displayedTech.getDoublesWealth()) {
                chkTECHDoublesWealth.setSelected(true);
            } else {
                chkTECHDoublesWealth.setSelected(false);
            }
            if (displayedTech.getEnablesSeaTrade()) {
                chkTECHEnablesSeaTrade.setSelected(true);
            } else {
                chkTECHEnablesSeaTrade.setSelected(false);
            }
            if (displayedTech.getEnablesOceanTrade()) {
                chkTECHEnablesOceanTrade.setSelected(true);
            } else {
                chkTECHEnablesOceanTrade.setSelected(false);
            }
            if (displayedTech.getEnablesMapTrading()) {
                chkTECHEnablesMapTrading.setSelected(true);
            } else {
                chkTECHEnablesMapTrading.setSelected(false);
            }
            if (displayedTech.getEnablesCommunicationTrading()) {
                chkTECHEnablesCommunicationTrading.setSelected(true);
            } else {
                chkTECHEnablesCommunicationTrading.setSelected(false);
            }
            if (displayedTech.getNotRequiredForAdvancement()) {
                chkTECHNotRequiredForAdvancement.setSelected(true);
            } else {
                chkTECHNotRequiredForAdvancement.setSelected(false);
            }
            if (displayedTech.getDoublesWorkRate()) {
                chkTECHDoublesWorkRate.setSelected(true);
            } else {
                chkTECHDoublesWorkRate.setSelected(false);
            }
            if (displayedTech.getCannotBeTraded()) {
                chkTECHCannotBeTraded.setSelected(true);
            } else {
                chkTECHCannotBeTraded.setSelected(false);
            }
            if (displayedTech.getPermitsSacrifice()) {
                chkTECHPermitsSacrifice.setSelected(true);
            } else {
                chkTECHPermitsSacrifice.setSelected(false);
            }
            if (displayedTech.getBonusTech()) {
                chkTECHBonusTech.setSelected(true);
            } else {
                chkTECHBonusTech.setSelected(false);
            }
            if (displayedTech.getRevealMap()) {
                chkTECHRevealMap.setSelected(true);
            } else {
                chkTECHRevealMap.setSelected(false);
            }
            //display flavor data
            char numTrue = 0;
            for (int i = 0; i < displayedTech.getNumFlavours(); i++)
                if (displayedTech.getFlavour(i))
                    numTrue++;
            int[]selInd = new int[numTrue];
            int index = 0;
            for (int i = 0; i < displayedTech.getNumFlavours(); i++)
                if (displayedTech.getFlavour(i))
                {
                    selInd[index] = i;
                    index++;
                }
            lstFlavors.setSelectedIndices(selInd);
            txtScienceAdvisorBlurb.setText(baselink.getScriptFile().getTechBlurb(technologyIndex));
        }
        
        updateTechIcon();
   }
   
   private void updateTechIcon() {
       if (technologyIndex != -1) {
            //Tech Icon
            //Performance: Seems to be 80% instant, 16% 15ms, 4% slightly slower
            //Corresponding to number of HDD accesses - either the head is already on the right track,
            //or it has to search once, or rarely multiple times on the search path.
            //Either way, going to favor memory conversation over loading speed since speed is perfectly
            //adequate.  Test device: Toshiba 3.5" 2 TB 7200 RPM HDD (though developed with fine results on
            //Seagate 2.5" 160 GB 5400 RPM HDD).
            if (Main.GRAPHICS_ENABLED) {
                try {
                    Image img = CivilopediaIcon.getLargeCivilopediaIcon(technology.get(technologyIndex));
                    if (img != null) {
                        pnlLargeIcon.setVisible(true);
                        pnlLargeIcon.setImage(img.getScaledInstance(96, 96, SCALE_SMOOTH));
                        
                        Graphics g = pnlLargeIcon.getGraphics();
                        pnlLargeIcon.update(g);
                        g.dispose();                        
                    }
                    else {
                        pnlLargeIcon.setVisible(false);
                    }
                }
                catch(FileNotFoundException ex) {
                    logger.warn("Scenario file not found", ex);
                }
            }
       }
    }

   public void setSelectedIndex(int i)
   {
       technologyIndex = i;
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
        //do set document listeners on the char array fields (civilopedia and the like)
        addLengthDocumentListener(31, txtTECHCivilopediaEntry);
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
        addBadValueDocumentListener(5000, -5000, txtTECHX);
        addBadValueDocumentListener(5000, -5000, txtTECHY);
        addBadValueDocumentListener(1000, txtTECHCost);
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
        if (technologyIndex == -1) {
            return true;
        }
        return super.checkBounds(invalidValues);
    }
   
    public void renameBIQElement(int index, String name)
    {
        technology.get(index).setName(name);
    }
    
    public void handleReorderOnDependencies(int destination, int source) {
    }
    
    @Override
    public void deleteAction() {
        deleteAction(null);
    }
    
    public void deleteAction(TECH deletedTech)
    {
        //deleteItem(evt);
        int index = -1;
        if (!Main.settings.useJavaFX) {
            index = lstTechs.getSelectedIndex();
        }
        else {
            index = deletedTech.getIndex();
        }
        int indexWithNone = index + 1;  //for removing from models containing "None"; makes it a little easier to see that the right one is removed
        if (logger.isInfoEnabled())
            logger.info("index to be deleted: " + index);
        //good dependencies
        if (goodTab.cmbGOODPrerequisite.getSelectedIndex() - 1 == index)
            goodTab.cmbGOODPrerequisite.setSelectedIndex(0);
        goodTab.mdlPrerequisite.removeElementAt(indexWithNone);
        for (int i = 0; i < goodTab.resource.size(); i++)
        {
            goodTab.resource.get(i).handleDeletedTechnology(index);
        }
        //building dependencies
        if (bldgTab.cmbBLDGReqAdvance.getSelectedIndex() - 1 == index)
            bldgTab.cmbBLDGReqAdvance.setSelectedIndex(0);
        bldgTab.mdlReqAdvance.removeElementAt(indexWithNone);
        if (bldgTab.cmbBLDGObsoleteBy.getSelectedIndex() - 1 == index)
            bldgTab.cmbBLDGObsoleteBy.setSelectedIndex(0);
        bldgTab.mdlObsolete.removeElementAt(indexWithNone);
        //building update at end
        
        //civilization dependencies
        if (civTab.cmbRACEFreeTech1Index.getSelectedIndex() - 1 == index)
            civTab.cmbRACEFreeTech1Index.setSelectedIndex(0);
        civTab.mdlFreeTech1.removeElementAt(indexWithNone);
        if (civTab.cmbRACEFreeTech2Index.getSelectedIndex() - 1 == index)
            civTab.cmbRACEFreeTech2Index.setSelectedIndex(0);
        civTab.mdlFreeTech2.removeElementAt(indexWithNone);
        if (civTab.cmbRACEFreeTech3Index.getSelectedIndex() - 1 == index)
            civTab.cmbRACEFreeTech3Index.setSelectedIndex(0);
        civTab.mdlFreeTech3.removeElementAt(indexWithNone);
        if (civTab.cmbRACEFreeTech4Index.getSelectedIndex() - 1 == index)
            civTab.cmbRACEFreeTech4Index.setSelectedIndex(0);
        civTab.mdlFreeTech4.removeElementAt(indexWithNone);
        for (int i = 0; i < civTab.civilization.size(); i++)
        {
            civTab.civilization.get(i).handleDeletedTechnology(index);
        }
        //unit dependencies
        //working
        if (unitTab.cmbPRTORequiredTech.getSelectedIndex() - 1 == index)
            unitTab.cmbPRTORequiredTech.setSelectedIndex(0);
        unitTab.mdlPrerequisite.removeElementAt(indexWithNone);
        for (PRTO prto : unitTab.unit)
        {
            prto.handleDeletedTechnology(index);
        }
        
        //ctzn dependencies
        if (ctznTab.cmbCTZNPrerequisite.getSelectedIndex() - 1 == index)
            ctznTab.cmbCTZNPrerequisite.setSelectedIndex(0);
        ctznTab.mdlPrerequisite.removeElementAt(indexWithNone);
        for (CTZN citizen : baselink.citizens) {
            citizen.handleDeletedTech(index);
        }
        
        //govt dependencies
        if (govtTab.cmbGOVTPrerequisiteTechnology.getSelectedIndex() - 1 == index)
            govtTab.cmbGOVTPrerequisiteTechnology.setSelectedIndex(0);
        govtTab.mdlPrerequisite.removeElementAt(indexWithNone);
        for (GOVT government : baselink.government) {
            government.handleDeletedTech(index);
        }
        
        //terraform dependencies
        //buggy
        if (trfmTab.cmbTRFMRequiredAdvance.getSelectedIndex() - 1 == index)
            trfmTab.cmbTRFMRequiredAdvance.setSelectedIndex(0);
        trfmTab.mdlPrerequisite.removeElementAt(indexWithNone);
        for (int i = 0; i < trfmTab.workerJob.size(); i++)
        {
            trfmTab.workerJob.get(i).handleDeletedTech(index);
        }
        
        //Golly gee, forgetting about updating tech dependencies on itself...
        //kinda embarrassing
        //Before this, you could potentially get a CTD due to circular dependencies
        //when deleting technologies
        for (int i = 0; i < technology.size(); i++)
        {
            technology.get(i).handleDeletedTechnology(index);
        }
        //Also handle the prereq model's elements
        mdlPrerequisite1.removeElementAt(indexWithNone);
        mdlPrerequisite2.removeElementAt(indexWithNone);
        mdlPrerequisite3.removeElementAt(indexWithNone);
        mdlPrerequisite4.removeElementAt(indexWithNone);
        technologyIndex = -1;
        
        //oddity: the below line does not work if I put this in sendData/2
        utils.removeFromList(index, technology, technologyList, lstTechs);
        
        //buildings - must be after tech removal b/c of updated tech IDs
        //TODO: Update the BLDG code to be like the CTZN code, which will allow
        //this to happen prior to removal.
        for (BLDG building : bldgTab.buildings)
        {
            building.handleDeletedTech(index);
        }
        
        //player tab dependencies
        //Must be after tech update b/c grabs updated tech IDs from techs
        if (baselink.hasCustomPlayerData()) {
            for (int i = 0; i < playerTab.player.size(); i++) {
                playerTab.player.get(i).handleDeletedTech(index);
            }
            playerTab.deleteTechnology(index);
        }
    }
    
    /**
     * Utility method to print out a dependency tree.
     * Later, will used something of a similar concept to check for circular
     * dependencies, which crash the game.
     */
    private void dependencyTree()
    {
        for (int i = 0; i < technology.size(); i++)
        {
            dependencyTree(technology.get(i),"");
        }
    }
    
    private void dependencyTree(TECH tech, String curSpaces)
    {
        if (curSpaces.equals(""))
        {
            System.out.println("\nDependency tree for " + tech.getName());
        }
        if (tech.getPrerequisite1() != -1)
        {
            String spaces = curSpaces + " ";
            TECH dep = technology.get(tech.getPrerequisite1());
            System.out.println(spaces + dep.getName());
            dependencyTree(dep, spaces);
        }
        if (tech.getPrerequisite2() != -1)
        {
            String spaces = curSpaces + " ";
            TECH dep = technology.get(tech.getPrerequisite2());
            System.out.println(spaces + dep.getName());
            dependencyTree(dep, spaces);
        }
        if (tech.getPrerequisite3() != -1)
        {
            String spaces = curSpaces + " ";
            TECH dep = technology.get(tech.getPrerequisite3());
            System.out.println(spaces + dep.getName());
            dependencyTree(dep, spaces);
        }
        if (tech.getPrerequisite4() != -1)
        {
            String spaces = curSpaces + " ";
            TECH dep = technology.get(tech.getPrerequisite4());
            System.out.println(spaces + dep.getName());
            dependencyTree(dep, spaces);
        }
    }
    
    /**
     * This is a method because not all the other tabs are necessarily created
     * at the time this tab is created.
     * @return 
     */
    private DefaultComboBoxModel[] getModelsToUpdate() {
        DefaultComboBoxModel[] modelsToUpdate = { 
            goodTab.mdlPrerequisite, 
            bldgTab.mdlObsolete, bldgTab.mdlReqAdvance,
            civTab.mdlFreeTech1, civTab.mdlFreeTech2, civTab.mdlFreeTech3, civTab.mdlFreeTech4,
            unitTab.mdlPrerequisite,
            ctznTab.mdlPrerequisite,
            govtTab.mdlPrerequisite,
            trfmTab.mdlPrerequisite,
            mdlPrerequisite1, mdlPrerequisite2, mdlPrerequisite3, mdlPrerequisite4
        };
        return modelsToUpdate;
    }
    
    public void renameItem(TECH tech) {
        String name = utils.getItemName("Rename " + tech.getName(), "Technology Name", "Enter new name for " + tech.getName() + ": ", tech.getName());

        if (name.length() > 0) {
            tech.setName(name);
            
            int cmbIndex = tech.getIndex() + 1;
            for (DefaultComboBoxModel model : getModelsToUpdate()) {
                utils.renameItemInComboBox(cmbIndex, tech.getName(), model);
            }
        }
    }
    
    public boolean addItem(String name)
    {  
        if (name.length() > 0) {
            technology.add(new TECH(name, technology.get(0).getNumFlavours(), baselink));
            technology.get(technology.size() - 1).setIndex(technology.size() - 1);
            
            for (DefaultComboBoxModel model : getModelsToUpdate()) {
                utils.addWithPossibleDuplicates(name, model);
            }
            return true;
        }
        return false;
    }
}
