package com.civfanatics.civ3.xplatformeditor;

import com.civfanatics.civ3.xplatformeditor.specialty.SafetyLevel;
import com.civfanatics.civ3.biqFile.RACE;
import com.civfanatics.civ3.biqFile.ERAS;
import com.civfanatics.civ3.biqFile.DIFF;
import com.civfanatics.civ3.biqFile.PRTO;
import com.civfanatics.civ3.biqFile.GOVT;
import com.civfanatics.civ3.biqFile.GAME;
import com.civfanatics.civ3.biqFile.IO;
import com.civfanatics.civ3.biqFile.LEAD;
import com.civfanatics.civ3.biqFile.TECH;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.ComboBoxColorRenderer;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJTextField;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
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
import javax.swing.border.TitledBorder;
/**
 * This class represents the Player tab.  This is the tab used to configure player-specific settings within the game.  AKA the "LEAD" tab.
 * @author Andrew
 */
public class PLYRTab extends EditorTab {

    //int prevUnit = -1;   //the unit most recently selected
    PRTO prevUnit = null;
    public DefaultListModel startingUnits;
    public DefaultComboBoxModel mdlPossibleStartingUnits;
    DefaultComboBoxModel mdlDifficulties = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlCivilizations = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlGovernments = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlInitialEra = new DefaultComboBoxModel();
    ComboBoxColorRenderer colorRenderer;    
    public List<GAME>prop;
    public List<LEAD>player;
    public List<TECH>technology;
    public List<PRTO>unit;
    public List<DIFF>difficulty;
    public List<GOVT>government;
    public List<ERAS>era;
    public List<RACE>civilization;
    //public IO baselink;
    Color[]colors;
    int playerIndex;
    boolean tabCreated;
    boolean resetCivData;
    boolean initialListAdd = false;
    MapTab mapTab;    //link to map tab, to update players model
    GAMETab gameTab;    //link to scnprop tab, to update alliance list models
    
    JMenuItem delete = new JMenuItem("Delete");
    
    private JCheckBox chkLEADHumanPlayer;
    private JCheckBox chkLEADSkipFirstTurn;
    private JCheckBox chkLEADStartEmbassies;
    private JComboBox cmbLEADCiv;
    private JComboBox cmbLEADColor;
    private JComboBox cmbLEADDifficulty;
    private JComboBox cmbLEADGovernment;
    private JComboBox cmbLEADInitialEra;
    private SuperJTextField txtLEADStartCash;
    JComboBox cmbLEADStartingUnit;
    private JCheckBox chkLEADCivDefaults;
    private JLabel lblNumberOfPlayers;
    private JLabel lblPlayableCivs;
    private JLabel jLabel237;
    private JLabel jLabel238;
    private JLabel jLabel239;
    private JLabel jLabel240;
    private JLabel jLabel241;
    private JLabel jLabel242;
    private JLabel jLabel243;
    private JLabel lblAtLeastOneOf;
    private JLabel jLabel245;
    private JLabel jLabel246;
    private JLabel jLabel247;
    private JPanel jPanel88;
    private JPanel jPanel89;
    private JPanel jPanel90;
    private JPanel pnlCivSettings;
    private JPanel jPanel92;
    private JScrollPane scrPlayableCivs;
    private JScrollPane jScrollPane26;
    private JScrollPane jScrollPane27;
    private JScrollPane jScrollPane33;
    private JList lstGAMEPlayableCivs;
    private JList lstLEADPlayerSelect = new JList();    //uses playersInGame
    private JList lstLEADStartingTechnology;
    private JList lstLEADTypeOfStartingUnit;
    private JRadioButton rbtnLEADFemaleLeader;
    private JRadioButton rbtnLEADMaleLeader;
    private SuperJTextField txtGAMENumplayersInGame;
    private SuperJTextField txtLEADLeaderName;
    private SuperJTextField txtLEADUnitsOfThisType;
    private JButton btnUpdateNumPlayers = new JButton("Update # of Players");

    private ButtonGroup btnGroupLeaderGender;

    DefaultListModel startingTechnologyModel;
    DefaultListModel playersInGame;
    DefaultListModel playableCivs;
    
    boolean allowComboChanges = true;

    /**
     * The constructor.  Takes no arguments.
     */
    public PLYRTab()
    {
        lstType = lstLEADPlayerSelect;
        tabName = "PLYR";
        textBoxes = new ArrayList<>();
        jPanel88 = new JPanel();
        lblNumberOfPlayers = new JLabel();
        txtGAMENumplayersInGame = new SuperJTextField();
        lblPlayableCivs = new JLabel();
        scrPlayableCivs = new JScrollPane();
        lstGAMEPlayableCivs = new JList();
        jPanel89 = new JPanel();
        jScrollPane26 = new JScrollPane();
        jLabel237 = new JLabel();
        cmbLEADDifficulty = new JComboBox();
        jLabel238 = new JLabel();
        cmbLEADCiv = new JComboBox();
        jLabel239 = new JLabel();
        cmbLEADGovernment = new JComboBox();
        cmbLEADInitialEra = new JComboBox();
        jLabel240 = new JLabel();
        jLabel241 = new JLabel();
        txtLEADStartCash = new SuperJTextField();
        chkLEADHumanPlayer = new JCheckBox();
        chkLEADCivDefaults = new JCheckBox();
        chkLEADStartEmbassies = new JCheckBox();
        chkLEADSkipFirstTurn = new JCheckBox();
        jPanel90 = new JPanel();
        jLabel242 = new JLabel();
        cmbLEADStartingUnit = new JComboBox();
        jLabel243 = new JLabel();
        txtLEADUnitsOfThisType = new SuperJTextField();
        txtLEADUnitsOfThisType.setText("0");
        jScrollPane27 = new JScrollPane();
        lstLEADTypeOfStartingUnit = new JList();
        lblAtLeastOneOf = new JLabel();
        pnlCivSettings = new JPanel();
        jLabel245 = new JLabel();
        cmbLEADColor = new JComboBox();
        jLabel246 = new JLabel();
        txtLEADLeaderName = new SuperJTextField();
        jPanel92 = new JPanel();
        rbtnLEADMaleLeader = new JRadioButton();
        rbtnLEADFemaleLeader = new JRadioButton();
        jLabel247 = new JLabel();
        jScrollPane33 = new JScrollPane();
        lstLEADStartingTechnology = new JList();
        btnGroupLeaderGender = new ButtonGroup();

        playerIndex = -1;

        startingTechnologyModel = new DefaultListModel();
        playersInGame = new DefaultListModel();
        playableCivs = new DefaultListModel();
    }

    
    /**
     * sendTabLinks methods are used to send references to other tabs to a tab
     * that needs access to GUI elements on those other tabs.
     * The PLYR tab needs access to the MapTab (for the player list).
     *
     * @param mapTab - Reference to the map tab.
     */
    public void sendTabLinks(MapTab mapTab, GAMETab gameTab)
    {
        this.mapTab = mapTab;
        this.gameTab = gameTab;
    }
    
    public void sendMapTabLink(MapTab mapTab) {
        this.mapTab = mapTab;
    }
    
    /**
     * Enables or disables all GUI elements of the tab.  This makes it easy to
     * turn the tab 'on' or 'off' depending on whether there is custom player
     * data or not.
     * @param toSet - true to enable, false to disable
     */
    public void setAllEnabled(boolean toSet)
    {
        chkLEADCivDefaults.setEnabled(toSet);
        chkLEADHumanPlayer.setEnabled(toSet);
        chkLEADSkipFirstTurn.setEnabled(toSet);
        chkLEADStartEmbassies.setEnabled(toSet);
        cmbLEADCiv.setEnabled(toSet);
        cmbLEADColor.setEnabled(toSet);
        cmbLEADDifficulty.setEnabled(toSet);
        cmbLEADGovernment.setEnabled(toSet);
        cmbLEADInitialEra.setEnabled(toSet);
        cmbLEADStartingUnit.setEnabled(toSet);
        lstGAMEPlayableCivs.setEnabled(toSet);
        lstLEADPlayerSelect.setEnabled(toSet);
        lstLEADStartingTechnology.setEnabled(toSet);
        lstLEADTypeOfStartingUnit.setEnabled(toSet);
        txtGAMENumplayersInGame.setEnabled(toSet);
        btnUpdateNumPlayers.setEnabled(toSet);
        txtLEADLeaderName.setEnabled(toSet);
        txtLEADStartCash.setEnabled(toSet);
        txtLEADUnitsOfThisType.setEnabled(toSet);
        rbtnLEADFemaleLeader.setEnabled(toSet);
        rbtnLEADMaleLeader.setEnabled(toSet);
    }
    /**
     * Creates the GUI of the tab.  This includes button groups, layouts, etc.
     * Logically, this is to occur immediately after the constructor is called.
     *
     * @return - A reference to the PLYRTab instance itself.  This reference
     * allows the tab to be added to a tab bar.
     */
    public JPanel createTab()
    {
        btnGroupLeaderGender.add(rbtnLEADMaleLeader);
        btnGroupLeaderGender.add(rbtnLEADFemaleLeader);
        jPanel88.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Player Options"));

        lblNumberOfPlayers.setText("# of Players:");

        lblPlayableCivs.setText("Playable Civilizations");

        lstGAMEPlayableCivs.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lstGAMEPlayableCivs.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt){
                if (!(initialListAdd))
                {
                    allowComboChanges = false;
                    
                    //Backup playable civs so we can see which changed
                    Integer[]oldPlayableCivs = new Integer[prop.get(0).idOfPlayableCivs.size()];
                    oldPlayableCivs = prop.get(0).idOfPlayableCivs.toArray(oldPlayableCivs);
                    
                    int[]playableCivs = lstGAMEPlayableCivs.getSelectedIndices();
                    prop.get(0).numberOfPlayableCivs = playableCivs.length;
                    Set<Integer> playableCivIds = new HashSet<Integer>();
                    Set<String> playableCivNames = new HashSet<String>();
                    prop.get(0).idOfPlayableCivs.clear();
                    for (int i = 0; i < playableCivs.length; i++)
                    {
                        prop.get(0).idOfPlayableCivs.add(playableCivs[i]);
                        playableCivIds.add(playableCivs[i]);
                        playableCivNames.add(baselink.civilization.get(playableCivs[i]).getName());
                    }
                    //if any player had the now-unplayable civ set their player to Any
                    for (int i = 0; i < player.size(); i++)
                    {
                        if (resetCivData && !playableCivIds.contains(player.get(i).civ)) {
                            player.get(i).civ = -3; //set to any
                        }
                    }
                    
                    //Update the combo list on the player tab, including making sure
                    //the current selection remains accurate
                    int playableCount = 0;
                    for (int i = 0; i < baselink.civilization.size(); i++) {
                        String civName = baselink.civilization.get(i).getName();
                        if (playableCivNames.contains(civName)) {
                            //insert it if it doesn't exist
                            if (mdlCivilizations.getIndexOf(civName) == -1) {
                                mdlCivilizations.insertElementAt(civName, playableCount + 2);
                            }
                            playableCount++;
                        }
                        else {
                            if (mdlCivilizations.getIndexOf(civName) != -1) {
                                //If it's also the selected one, set the selected one to Any
                                if (mdlCivilizations.getSelectedItem().equals(civName)) {
                                    mdlCivilizations.setSelectedItem("Any");
                                }
                                mdlCivilizations.removeElement(civName);
                            }
                        }
                    }
                    
                    //Update Alliances
                    for (int i = 0; i < oldPlayableCivs.length; i++) {
                        int oldCivId = oldPlayableCivs[i];
                        if (playableCivIds.contains(oldCivId)) {
                            //we're good?
                        }
                        else {
                            //need to remove it from alliances
                            baselink.scenarioProperty.get(0).civPartOfWhichAlliance.remove(i);
                        }
                    }
                    //Also need to add any new ones
                    for (int i = 0; i < playableCivs.length; i++) {
                        int newCivId = playableCivs[i];
                        boolean wasOldPlayableCiv = false;
                        for (int j = 0; j < oldPlayableCivs.length; j++) {
                            if (oldPlayableCivs[j] == newCivId) {
                                wasOldPlayableCiv = true;
                                break;
                            }
                        }
                        
                        if (wasOldPlayableCiv) {
                            //we're good?
                        }
                        else {
                            //Add it to the "None" alliance
                            baselink.scenarioProperty.get(0).civPartOfWhichAlliance.add(i, 0);
                        }
                    }
                    
                    //alert game tab that we've updated
                    gameTab.updateAllianceMembers();
                    
                    allowComboChanges = true;
                }
            }
        });
        //Question:  What's the down-low on using AWT versus Swing mouse events?
        //And other than interface/abstract class, does it matter if we use
        //MouseListener or MouseAdapter (the latter requiring less code)?
//        lstLEADPlayerSelect.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(MouseEvent e)
//            {
//                updateTab();
//            }
//        });
        lstLEADPlayerSelect.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                updateTab();
            }
        });
        lstLEADPlayerSelect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lstPlayerSelectAction(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lstPlayerSelectAction(evt);
            }
        });
        cmbLEADCiv.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if (allowComboChanges) {
                    int playerSelected = lstLEADPlayerSelect.getSelectedIndex();
                    if (baselink.hasCustomMap()) {
                        if (cmbLEADCiv.getSelectedIndex() > 1) {
                            //Calculations must take into account not all civs being playable
                            int civToSet = prop.get(0).idOfPlayableCivs.get(cmbLEADCiv.getSelectedIndex()  - 2);
                            RACE civ = civilization.get(civToSet); //-2 b/c of Any/Random; +1 because of barbarians
                            mapTab.setPlayerName(playerSelected, "Player " + (playerSelected + 1) + " (" + civ.getName() + ")");
                        }
                        else if (cmbLEADCiv.getSelectedIndex() == 1) {
                            mapTab.setPlayerName(playerSelected, "Player " + (playerSelected + 1) + " (Random)");
                        }
                        else if (cmbLEADCiv.getSelectedIndex() == 0) {
                            mapTab.setPlayerName(playerSelected, "Player " + (playerSelected + 1) + " (Any)");
                        }
                    }
                }
            }
        });
        
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int index = lstLEADPlayerSelect.getSelectedIndex();
                baselink.deletePlayer(index);
                
                int temp = playerIndex;
                playerIndex = -1;   //prevents saving the deleted player's data to the next one in the list
                playersInGame.remove(index);
                
                if (playersInGame.size() > temp) {
                    lstLEADPlayerSelect.setSelectedIndex(temp);
                }
                else {
                    lstLEADPlayerSelect.setSelectedIndex(temp - 1);
                }
                lstLEADPlayerSelect.setSelectedIndex(playerIndex);
                
                if (baselink.hasCustomMap()) {
                    mapTab.removePlayerFromModel(index);
                }
            }
        });
        
        scrPlayableCivs.setViewportView(lstGAMEPlayableCivs);

        org.jdesktop.layout.GroupLayout jPanel88Layout = new org.jdesktop.layout.GroupLayout(jPanel88);
        jPanel88.setLayout(jPanel88Layout);
        jPanel88Layout.setHorizontalGroup(
            jPanel88Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel88Layout.createSequentialGroup()
                .add(jPanel88Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, scrPlayableCivs, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel88Layout.createSequentialGroup()
                        .add(lblNumberOfPlayers)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(txtGAMENumplayersInGame, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 58, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, lblPlayableCivs)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, btnUpdateNumPlayers))
                .addContainerGap())
        );
        jPanel88Layout.setVerticalGroup(
            jPanel88Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel88Layout.createSequentialGroup()
                .add(jPanel88Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblNumberOfPlayers)
                    .add(txtGAMENumplayersInGame, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnUpdateNumPlayers)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lblPlayableCivs)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(scrPlayableCivs, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE))
        );

        jPanel89.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Player Information"));
        jPanel89.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lstLEADPlayerSelect.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane26.setViewportView(lstLEADPlayerSelect);

        jPanel89.add(jScrollPane26, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 110, 620));

        jLabel237.setText("Civilization:");
        jPanel89.add(jLabel237, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, -1, -1));

        cmbLEADDifficulty.setModel(mdlDifficulties);
        jPanel89.add(cmbLEADDifficulty, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, 140, 20));

        jLabel238.setText("Government:");
        jPanel89.add(jLabel238, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, -1, -1));

        cmbLEADCiv.setModel(mdlCivilizations);
        jPanel89.add(cmbLEADCiv, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 140, 20));

        jLabel239.setText("Difficulty:");
        jPanel89.add(jLabel239, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, -1, -1));

        cmbLEADGovernment.setModel(mdlGovernments);
        jPanel89.add(cmbLEADGovernment, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, 140, 20));

        cmbLEADInitialEra.setModel(mdlInitialEra);
        jPanel89.add(cmbLEADInitialEra, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, 140, 20));

        jLabel240.setText("Initial Era:");
        jPanel89.add(jLabel240, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, -1, -1));

        jLabel241.setText("Starting Treasury:");
        jPanel89.add(jLabel241, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, -1, -1));
        jPanel89.add(txtLEADStartCash, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 140, 110, -1));

        chkLEADHumanPlayer.setText("Human Player");
        jPanel89.add(chkLEADHumanPlayer, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 170, -1, -1));

        chkLEADCivDefaults.setText("Civilization Defaults");
        jPanel89.add(chkLEADCivDefaults, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 190, -1, -1));
        chkLEADCivDefaults.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (chkLEADCivDefaults.isSelected()) {
                    enabledCivDefaults();
                }
                else {
                    disableCivDefaults();
                }
            }
        });

        chkLEADStartEmbassies.setText("Start with Embassies");
        jPanel89.add(chkLEADStartEmbassies, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 230, -1, -1));

        chkLEADSkipFirstTurn.setText("Skip First Turn");
        jPanel89.add(chkLEADSkipFirstTurn, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 210, -1, -1));

        jPanel90.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Starting Units"));

        jLabel242.setText("Unit:");

        mdlPossibleStartingUnits = new javax.swing.DefaultComboBoxModel();

        cmbLEADStartingUnit.setModel(mdlPossibleStartingUnits);
        cmbLEADStartingUnit.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if (e == null)  //this was via a player selected switch!
                {
                    logger.debug("Machine invocation of actionPerformed");
                }
                if (playerIndex == -1)
                    return;
                if (prevUnit == null)
                {
                    prevUnit = baselink.unit.get(cmbLEADStartingUnit.getSelectedIndex());
                    Integer count = player.get(playerIndex).getNumOfStartingUnitByType(prevUnit);
                    
                    txtLEADUnitsOfThisType.setText(String.valueOf(count));
                    return;
                }
                //store the data
                int newCount = txtLEADUnitsOfThisType.getInteger();
                
                boolean unitAlreadyInList = player.get(playerIndex).getNumOfStartingUnitByType(prevUnit) != 0;
                
                if (unitAlreadyInList)    //it's already there, update
                {
                    if (newCount == 0) { //take it out
                        startingUnits.removeElement(prevUnit.getName());
                    }
                }
                else if (newCount != 0) {   //it ain't there already, add it in
                    startingUnits.addElement(prevUnit.getName());
                }
                //The below method will handle all add/remove needs in the PLYR
                player.get(playerIndex).updateStartUnitCount(prevUnit, newCount);
                
                //The "previous" unit is now the new one
                prevUnit = baselink.unit.get(cmbLEADStartingUnit.getSelectedIndex());

                //Update the unit count to be based on the newly-selected unit
                PRTO newUnit = baselink.unit.get(cmbLEADStartingUnit.getSelectedIndex());
                int newUnitCount = player.get(playerIndex).getNumOfStartingUnitByType(newUnit);
                txtLEADUnitsOfThisType.setText(String.valueOf(newUnitCount));
            }
        });

        jLabel243.setText("Amount:");

        startingUnits = new javax.swing.DefaultListModel();

        lstLEADTypeOfStartingUnit.setModel(startingUnits);
        lstLEADTypeOfStartingUnit.setEnabled(false);
        jScrollPane27.setViewportView(lstLEADTypeOfStartingUnit);

        lblAtLeastOneOf.setText("At least one of... (edit above)");

        org.jdesktop.layout.GroupLayout jPanel90Layout = new org.jdesktop.layout.GroupLayout(jPanel90);
        jPanel90.setLayout(jPanel90Layout);
        jPanel90Layout.setHorizontalGroup(
            jPanel90Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel90Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel90Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane27, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                    .add(jPanel90Layout.createSequentialGroup()
                        .add(jLabel242)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cmbLEADStartingUnit, 0, 147, Short.MAX_VALUE))
                    .add(jPanel90Layout.createSequentialGroup()
                        .add(jLabel243)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtLEADUnitsOfThisType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(lblAtLeastOneOf))
                .addContainerGap())
        );
        jPanel90Layout.setVerticalGroup(
            jPanel90Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel90Layout.createSequentialGroup()
                .add(12, 12, 12)
                .add(jPanel90Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cmbLEADStartingUnit, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel242))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel90Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel243)
                    .add(txtLEADUnitsOfThisType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lblAtLeastOneOf)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jScrollPane27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 116, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(32, 32, 32))
        );

        jPanel89.add(jPanel90, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 10, 220, 240));

        pnlCivSettings.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Civilization Settings"));

        jLabel245.setText("Player Color:");
        Integer[]intArray = new Integer[32];
        for (int i = 0; i < intArray.length; i++)
            intArray[i] = i;

        cmbLEADColor = new JComboBox(intArray);
        cmbLEADColor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbLEADColor.setBackground(colorRenderer.color[cmbLEADColor.getSelectedIndex()]);
            }}
        );

        jLabel246.setText("Leader Name:");

        jPanel92.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Gender"));

        rbtnLEADMaleLeader.setText("Male");

        rbtnLEADFemaleLeader.setText("Female");

        org.jdesktop.layout.GroupLayout jPanel92Layout = new org.jdesktop.layout.GroupLayout(jPanel92);
        jPanel92.setLayout(jPanel92Layout);
        jPanel92Layout.setHorizontalGroup(
            jPanel92Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel92Layout.createSequentialGroup()
                .add(rbtnLEADMaleLeader)
                .add(49, 49, 49)
                .add(rbtnLEADFemaleLeader)
                .addContainerGap(57, Short.MAX_VALUE))
        );
        jPanel92Layout.setVerticalGroup(
            jPanel92Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel92Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(rbtnLEADMaleLeader)
                .add(rbtnLEADFemaleLeader))
        );

        jLabel247.setText("Free Techs");

        jScrollPane33.setViewportView(lstLEADStartingTechnology);

        org.jdesktop.layout.GroupLayout jPanel91Layout = new org.jdesktop.layout.GroupLayout(pnlCivSettings);
        pnlCivSettings.setLayout(jPanel91Layout);
        jPanel91Layout.setHorizontalGroup(
            jPanel91Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel91Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel91Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel91Layout.createSequentialGroup()
                        .add(jPanel91Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel245)
                            .add(jLabel246))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel91Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(txtLEADLeaderName)
                            .add(cmbLEADColor, 0, 125, Short.MAX_VALUE)))
                    .add(jPanel92, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel91Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel247)
                    .add(jScrollPane33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 166, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel91Layout.setVerticalGroup(
            jPanel91Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel91Layout.createSequentialGroup()
                .add(jPanel91Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel91Layout.createSequentialGroup()
                        .add(jPanel91Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel245)
                            .add(cmbLEADColor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel91Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel246)
                            .add(txtLEADLeaderName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel91Layout.createSequentialGroup()
                        .add(jLabel247)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jScrollPane33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel89.add(pnlCivSettings, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 260, 450, 210));

        org.jdesktop.layout.GroupLayout thisLayout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(thisLayout);
        thisLayout.setHorizontalGroup(
            thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(thisLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel88, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel89, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 831, Short.MAX_VALUE)
                .addContainerGap())
        );
        thisLayout.setVerticalGroup(
            thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(thisLayout.createSequentialGroup()
                .addContainerGap()
                .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel89, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
                    .add(jPanel88, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        
        txtGAMENumplayersInGame.setToolTipText("Press Enter to set the number of players");
        txtGAMENumplayersInGame.addKeyListener(new KeyListener(){
            public void keyTyped(KeyEvent e){
                
            }
            public void keyPressed(KeyEvent e){
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    updateNumPlayers();
                }
            }
            public void keyReleased(KeyEvent e){
            }
        });
        
        btnUpdateNumPlayers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateNumPlayers();
            }
        });

        this.setName("PLYR");

        tabCreated = true;

        return this;
    }
    
    private void updateNumPlayers() {
        SafetyLevel plyrSafeLvl = (SafetyLevel)(Main.safetyLevels.get("PLYR"));
        if (txtGAMENumplayersInGame.getInteger() > 31 && (plyrSafeLvl.ordinal()>=SafetyLevel.Safe.ordinal()))
        {
            JOptionPane.showMessageDialog(null, "You cannot have more than 31 players.  Leaving number of players the same.","Too many players",JOptionPane.ERROR_MESSAGE);
            txtGAMENumplayersInGame.setText(String.valueOf(player.size()));
        }
        else if (txtGAMENumplayersInGame.getInteger() > player.size())
        {
            //actually update
            addPlayers(txtGAMENumplayersInGame.getInteger() - player.size());
            if (baselink.hasCustomMap())
            {
                baselink.worldMap.get(0).numCivs = player.size();
            }
        }
        else if (txtGAMENumplayersInGame.getInteger() == player.size())
            ;
        else
        {
            int diff = player.size() - txtGAMENumplayersInGame.getInteger();
            if (diff > player.size()) {
                JOptionPane.showMessageDialog(null, "You cannot have fewer than zero players.  Leaving number of players the same.","Too few players",JOptionPane.ERROR_MESSAGE);
                txtGAMENumplayersInGame.setText(String.valueOf(player.size()));
                return;
            }
            for (int i = 0; i < diff; i++) {
                //UI updates.  Ensure the to-be-deleted player is not selected in the list.
                int playerToBeDeletedIndex = player.size() - 1;
                if (lstLEADPlayerSelect.getSelectedIndex() == player.size() - 1) {
                    lstLEADPlayerSelect.setSelectedIndex(player.size() - 2);
                }
                //Always need to remove a player from the list.
                playersInGame.removeElementAt(playersInGame.size() - 1);
                baselink.deletePlayer(player.size() - 1, false);
                if (baselink.hasCustomMap()) {
                    mapTab.removePlayerFromModel(playerToBeDeletedIndex);
                }
            }
            baselink.calculateTileOwners();
            if (baselink.hasCustomMap())
            {
                baselink.worldMap.get(0).numCivs = player.size();
            }
        }
    }
    
    private void lstPlayerSelectAction(MouseEvent evt) {
        if (evt.isPopupTrigger()) {
            JPopupMenu popUp = new JPopupMenu();
            popUp.add(delete);
            Component component = evt.getComponent();
            int x = evt.getX();
            int y = evt.getY();
            popUp.show(component, x, y);
        }
    }
    
    private void addPlayers(int numPlayers)
    {
        LEAD newPlayer = new LEAD(baselink);
        newPlayer.addDefaultStartUnits();
        player.add(newPlayer);
        playersInGame.addElement("Player " + player.size());
        if (baselink.hasCustomMap()) {
            mapTab.addPlayerToModel("Player " + player.size());
        }
        if (numPlayers > 1)
            addPlayers(numPlayers - 1);
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
     * @param player - The list of all (custom-specified) players in the scenario.
     * @param colors - All the colors that can be used to represent a civilization.
     * @param technology - The list of all technologies.
     * @param unit - The list of all units.
     * @param difficulty - The list of all difficulty levels.
     * @param government - The list of all governments.
     * @param era - The list of all eras.
     * @param civilization - The list of all civilizations.
     * @param baselink A link back to the main BIQ itself.
     */
    public void sendData(List<GAME>prop, List<LEAD>player, Color[]colors, List<TECH>technology, List<PRTO>unit, List<DIFF>difficulty, List<GOVT>government, List<ERAS>era, List<RACE>civilization, IO baselink)
    {
        allowComboChanges = false;
        
        this.prop = prop;
        this.player = player;
        this.colors = colors;
        this.technology = technology;
        this.unit = unit;
        this.difficulty = difficulty;
        this.government = government;
        this.era = era;
        this.civilization = civilization;
        this.baselink = baselink;

        startingTechnologyModel.clear();
        cmbLEADDifficulty.removeAllItems();
        cmbLEADCiv.removeAllItems();
        cmbLEADGovernment.removeAllItems();
        cmbLEADInitialEra.removeAllItems();

        for (int i = 0; i < technology.size(); i++)
        {
            startingTechnologyModel.addElement(technology.get(i).getName());
        }
        cmbLEADDifficulty.addItem("Any");
        for (int i = 0; i < difficulty.size(); i++)
        {
            utils.addWithPossibleDuplicates(difficulty.get(i).getName(), mdlDifficulties);
        }
        for (int i = 0; i < government.size(); i++)
        {
            utils.addWithPossibleDuplicates(government.get(i).getName(), mdlGovernments);
        }
        for (int i = 0; i < era.size(); i++)
        {
            utils.addWithPossibleDuplicates(era.get(i).getName(), mdlInitialEra);
        }
        cmbLEADInitialEra.addItem("Future Era");
        lstLEADStartingTechnology.setModel(startingTechnologyModel);

        colorRenderer = new ComboBoxColorRenderer(colors);
        cmbLEADColor.setRenderer(colorRenderer);
        cmbLEADColor.setOpaque(true);
        //cmbLEADColor.setForeground(Color.red);

        txtGAMENumplayersInGame.setText(Integer.toString(player.size()));
        playersInGame.clear();
        int pIndex = 1;
        for (int i = 0; i < player.size(); i++)
        {
            playersInGame.addElement("Player " + Integer.toString(pIndex));
            pIndex++;
        }
        lstLEADPlayerSelect.setModel(playersInGame);

        playableCivs.clear();
        for (int i = 0; i < civilization.size(); i++)
        {
            playableCivs.addElement(civilization.get(i).getName());
        }
        lstGAMEPlayableCivs.setModel(playableCivs);

        if (logger.isDebugEnabled())
            logger.debug("X civs can be played" + prop.get(0).idOfPlayableCivs.size());
        int[]civsThatCanBePlayed = new int[prop.get(0).idOfPlayableCivs.size()];
        for (int i = 0; i < civsThatCanBePlayed.length; i++)
        {
            civsThatCanBePlayed[i] = prop.get(0).idOfPlayableCivs.get(i);
        }

        //all _possible_ starting units - this will be the same across all players
        for (int i = 0; i < unit.size(); i++)
        {
            utils.addWithPossibleDuplicates(unit.get(i).getName(), mdlPossibleStartingUnits);
        }
        resetCivData = false;
        initialListAdd = true;
        lstGAMEPlayableCivs.setSelectedIndices(civsThatCanBePlayed);
        initialListAdd = false;
        resetCivData = true;


        updateCMBLEADCivs();

        allowComboChanges = true;
    }
    /**
     * TODO: Refactor this so that it calls the other version and the code needn't be maintained in two places (low priority).
     * @param player A reference to the list of all players (LEAD) in the BIQ.
     * @param colors A reference to an array of all the civ colors (raw RGB, not the whole PCX).
     */
    public void sendData(List<LEAD>player, Color[]colors)
    {
        allowComboChanges = false;
        
        this.player = player;
        this.colors = colors;

        startingTechnologyModel.clear();
        cmbLEADDifficulty.removeAllItems();
        cmbLEADCiv.removeAllItems();
        cmbLEADGovernment.removeAllItems();
        cmbLEADInitialEra.removeAllItems();

        for (int i = 0; i < technology.size(); i++)
        {
            startingTechnologyModel.addElement(technology.get(i).getName());
        }
        cmbLEADDifficulty.addItem("Any");
        for (int i = 0; i < difficulty.size(); i++)
        {
            utils.addWithPossibleDuplicates(difficulty.get(i).getName(), mdlDifficulties);
        }
        for (int i = 0; i < government.size(); i++)
        {
            utils.addWithPossibleDuplicates(government.get(i).getName(), mdlGovernments);
        }
        for (int i = 0; i < era.size(); i++)
        {
            utils.addWithPossibleDuplicates(era.get(i).getName(), mdlInitialEra);
        }
        cmbLEADInitialEra.addItem("Future Era");
        lstLEADStartingTechnology.setModel(startingTechnologyModel);

        colorRenderer = new ComboBoxColorRenderer(colors);
        cmbLEADColor.setRenderer(colorRenderer);
        cmbLEADColor.setOpaque(true);
        //cmbLEADColor.setForeground(Color.red);

        txtGAMENumplayersInGame.setText(Integer.toString(player.size()));
        playersInGame.clear();
        int pIndex = 1;
        for (int i = 0; i < player.size(); i++)
        {
            playersInGame.addElement("Player " + Integer.toString(pIndex));
            pIndex++;
        }
        lstLEADPlayerSelect.setModel(playersInGame);

        playableCivs.clear();
        for (int i = 0; i < civilization.size(); i++)
        {
            playableCivs.addElement(civilization.get(i).getName());
        }
        lstGAMEPlayableCivs.setModel(playableCivs);

        if (logger.isDebugEnabled())
            logger.debug("Second X civs can be played: " + prop.get(0).idOfPlayableCivs.size());
        int[]civsThatCanBePlayed = new int[prop.get(0).idOfPlayableCivs.size()];
        for (int i = 0; i < civsThatCanBePlayed.length; i++)
        {
            civsThatCanBePlayed[i] = prop.get(0).idOfPlayableCivs.get(i);
        }
        resetCivData = false;
        lstGAMEPlayableCivs.setSelectedIndices(civsThatCanBePlayed);
        resetCivData = true;


        updateCMBLEADCivs();

        allowComboChanges = true;
    }

    /**
     * Updates the list of civilizations in the combo box that is used to choose which civ is assigned to which player.
     */
    public void updateCMBLEADCivs()
    {
        cmbLEADCiv.removeAllItems();
        cmbLEADCiv.addItem("Any");
        cmbLEADCiv.addItem("Random");
        for (int i = 0; i < prop.get(0).idOfPlayableCivs.size(); i++)
        {
            utils.addWithPossibleDuplicates(civilization.get(prop.get(0).idOfPlayableCivs.get(i)).getName(), mdlCivilizations);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void updateTab()
    {
        if (baselink == null)
            return;
        if (!baselink.hasCustomPlayerData())
            return;
        storeData();
        playerIndex = lstLEADPlayerSelect.getSelectedIndex();
        displayData();
    }

    /**
     * Saves data after the tab has been modified.  Automatically called by updateTab.
     */
    public void storeData()
    {
        if (playerIndex != -1)
        {
            if (chkLEADCivDefaults.isSelected())
                player.get(playerIndex).setCustomCivData(0);
            else
                player.get(playerIndex).setCustomCivData(1);
            if (chkLEADHumanPlayer.isSelected())
                player.get(playerIndex).setHumanPlayer(1);
            else
                player.get(playerIndex).setHumanPlayer(0);
            player.get(playerIndex).setLeaderName(txtLEADLeaderName.getText());
            //player.get(playerIndex).setQuestionMark1(txtLEADQuestionMark1.getText()));
            //player.get(playerIndex).setQuestionMark2(txtLEADQuestionMark2.getText()));
            if (rbtnLEADMaleLeader.isSelected())
                player.get(playerIndex).setGenderOfLeaderName(0);
            else if (rbtnLEADFemaleLeader.isSelected())
                player.get(playerIndex).setGenderOfLeaderName(1);
            int[]selectedTechs = lstLEADStartingTechnology.getSelectedIndices();
            player.get(playerIndex).changeStartingTechnologies(selectedTechs);
            if (cmbLEADDifficulty.getSelectedIndex() == 0)
                player.get(playerIndex).setDifficulty(-2);
            else
                player.get(playerIndex).setDifficulty(cmbLEADDifficulty.getSelectedIndex() - 1);    //could be any
            //TODO: verify: not sure how future era is handled - guessing it is era 4 (base 0)
            player.get(playerIndex).setInitialEra(cmbLEADInitialEra.getSelectedIndex());
            player.get(playerIndex).setStartCash(txtLEADStartCash.getInteger());
            player.get(playerIndex).setGovernment(cmbLEADGovernment.getSelectedIndex());
            //TODO: Bug: Sometimes the wrong civ is set.
            //Methinks this is also due to a disconnect between the playable civs and all the civs
            if (cmbLEADCiv.getSelectedIndex() < 2)
                player.get(playerIndex).setCiv(cmbLEADCiv.getSelectedIndex() - 3);
            else
            {
                int civToSet = prop.get(0).idOfPlayableCivs.get(cmbLEADCiv.getSelectedIndex()  - 2);
                player.get(playerIndex).setCiv(civToSet);
                //player.get(playerIndex).setCiv(cmbLEADCiv.getSelectedIndex() - 1);  //could be "any" or "random"
                if (logger.isDebugEnabled())
                    logger.debug(civilization.get(player.get(playerIndex).getCiv()).getName());
            }
            player.get(playerIndex).setColor(cmbLEADColor.getSelectedIndex());
            if (chkLEADSkipFirstTurn.isSelected())
                player.get(playerIndex).setSkipFirstTurn(1);
            else
                player.get(playerIndex).setSkipFirstTurn(0);
            //player.get(playerIndex).setQuestionMark3(txtLEADQuestionMark3.getText()));
            if (chkLEADStartEmbassies.isSelected())
                player.get(playerIndex).setStartEmbassies((byte)1);
            else
                player.get(playerIndex).setStartEmbassies((byte)0);

            //Civs that can be played is updated instantaneously when that data
            //is changed - don't need to again here
            
            //Units - the call on actionPerformed will update any data that
            //needs to be stored, and after that we must update the # of units
            //that the player starts with.
            if (logger.isDebugEnabled())
                logger.debug("cmbLEADStartingUnit.getActionListeners().length: " + cmbLEADStartingUnit.getActionListeners().length);
            for (int i = 0; i < cmbLEADStartingUnit.getActionListeners().length; i++)
            {
                cmbLEADStartingUnit.getActionListeners()[i].actionPerformed(null);
            }
            player.get(playerIndex).numberOfDifferentStartUnits = this.lstLEADTypeOfStartingUnit.getModel().getSize();

        }
    }

    /**
     * Called upon to display the data when the tab is refreshed.
     */
    public void displayData()
    {
        if (playerIndex != -1)
        {
            allowComboChanges = false;
            
            if (logger.isDebugEnabled())
                logger.debug("playerIndex: " + playerIndex);
            if(player.get(playerIndex).getCustomCivData() == 0) {
                chkLEADCivDefaults.setSelected(true);
                enabledCivDefaults();
            }
            else {
                chkLEADCivDefaults.setSelected(false);
                disableCivDefaults();
            }
            if(player.get(playerIndex).getHumanPlayer() == 1)
                chkLEADHumanPlayer.setSelected(true);
            else
                chkLEADHumanPlayer.setSelected(false);
            txtLEADLeaderName.setText(player.get(playerIndex).getLeaderName());
            //txtLEADQuestionMark1.setText(Integer.toString(player.get(playerIndex).getQuestionMark1()));
            //txtLEADQuestionMark2.setText(Integer.toString(player.get(playerIndex).getQuestionMark2()));
            if(player.get(playerIndex).getGenderOfLeaderName() == 0)
                rbtnLEADMaleLeader.setSelected(true);
            else
                rbtnLEADFemaleLeader.setSelected(true);
            lstLEADStartingTechnology.setSelectedIndices(player.get(playerIndex).getStartingTechs());
            //handle case-by-case
            //difficulty: -2 = any
            if (player.get(playerIndex).getDifficulty() == -2)
                cmbLEADDifficulty.setSelectedIndex(0);
            else
                cmbLEADDifficulty.setSelectedIndex(player.get(playerIndex).getDifficulty() + 1);
            cmbLEADInitialEra.setSelectedIndex(player.get(playerIndex).getInitialEra());
            txtLEADStartCash.setText(Integer.toString(player.get(playerIndex).getStartCash()));
            cmbLEADGovernment.setSelectedIndex(player.get(playerIndex).getGovernment());
            if (logger.isDebugEnabled())
                logger.debug("Civ: " + player.get(playerIndex).getCiv());
            if (player.get(playerIndex).getCiv() == LEAD.CIV_ANY)
                cmbLEADCiv.setSelectedIndex(0);
            else if (player.get(playerIndex).getCiv() == LEAD.CIV_RANDOM)
                cmbLEADCiv.setSelectedIndex(1);
            else
            {
                int whichCiv = player.get(playerIndex).getCiv();
                for (int i = 0; i < prop.get(0).idOfPlayableCivs.size(); i++)
                {
                    if (prop.get(0).idOfPlayableCivs.get(i) == whichCiv)
                    {
                        cmbLEADCiv.setSelectedIndex(i + 2); //+2 for any/random
                        break;
                    }
                }
            }
            cmbLEADColor.setSelectedIndex(player.get(playerIndex).getColor());
            if (player.get(playerIndex).getSkipFirstTurn() == 1)
                chkLEADSkipFirstTurn.setSelected(true);
            else
                chkLEADSkipFirstTurn.setSelected(false);
            //txtLEADQuestionMark3.setText(Integer.toString(player.get(playerIndex).getQuestionMark3()));
            if (player.get(playerIndex).getStartEmbassies() == 1)
            {
                chkLEADStartEmbassies.setSelected(true);
            }
            else
            {
                chkLEADStartEmbassies.setSelected(false);
            }

            populateStartingUnitList();
            
            //set the drop-down box and text box GUI
            int selectedUnit = cmbLEADStartingUnit.getSelectedIndex();
            
            Integer unitCount = player.get(playerIndex).getNumOfStartingUnitByType(baselink.unit.get(selectedUnit));
            txtLEADUnitsOfThisType.setText(String.valueOf(unitCount));
            
            allowComboChanges = true;
        }
    }

    private void disableCivDefaults() {
        pnlCivSettings.setEnabled(true);
        rbtnLEADFemaleLeader.setEnabled(true);
        rbtnLEADMaleLeader.setEnabled(true);
        cmbLEADColor.setEnabled(true);
        txtLEADLeaderName.setEnabled(true);
        lstLEADStartingTechnology.setEnabled(true);
        ((TitledBorder)pnlCivSettings.getBorder()).setTitle("Civilization Settings");
    }

    private void enabledCivDefaults() {
        pnlCivSettings.setEnabled(false);
        rbtnLEADFemaleLeader.setEnabled(false);
        rbtnLEADMaleLeader.setEnabled(false);
        cmbLEADColor.setEnabled(false);
        txtLEADLeaderName.setEnabled(false);
        lstLEADStartingTechnology.setEnabled(false);
        ((TitledBorder)pnlCivSettings.getBorder()).setTitle("Uncheck 'Civilization Defaults' to Modify These");
    }
    
    public void populateStartingUnitList() {
        startingUnits.removeAllElements();

        Map<PRTO, Integer> playerStartUnits  = player.get(playerIndex).getStartingUnits();
        Iterator unitNameIterator = playerStartUnits.keySet().stream().sorted(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                PRTO unit1 = (PRTO) o1;
                PRTO unit2 = (PRTO) o2;
                return unit1.getIndex() - unit2.getIndex();
            }

        }).iterator();

        while (unitNameIterator.hasNext()) {
            PRTO nextUnit = (PRTO)unitNameIterator.next();
            startingUnits.addElement(nextUnit.getName());
        }
    }
    
    void deleteDifficulty(int index)
    {
        //minus/plus one for difficulties due to the "any" option
        if (cmbLEADDifficulty.getSelectedIndex() - 1 == index)
        {
            cmbLEADDifficulty.setSelectedIndex(0);
        }
        this.mdlDifficulties.removeElementAt(index + 1);        
        for (int i = 0; i < player.size(); i++)
        {
            if (player.get(i).difficulty > index)
            {
                player.get(i).difficulty--;
            }
            else if (player.get(i).difficulty == index)
            {
                player.get(i).difficulty = 0;
            }
        }        
    }
    
    void deleteGovernment(int index)
    {
        if (cmbLEADGovernment.getSelectedIndex() == index)
        {
            cmbLEADGovernment.setSelectedIndex(0);
        }
        this.mdlGovernments.removeElementAt(index);
        for (int i = 0; i < player.size(); i++)
        {
            if (player.get(i).government > index)
            {
                player.get(i).government--;
            }
            else if (player.get(i).government == index)
            {
                player.get(i).government = 0;
            }
        }
    }
    
    //TODO: The GUI isn't updating quite right
    //It looks not-quite-right until you select another player and come back
    //But the data is saving okay
    void deleteUnit(int index)
    {
        //System.err.println("Deleting index " + index);
        //remove it from the list list - failzoring
        startingUnits.removeElement(unit.get(index).getName());
        //remove it from the drop-down list
        this.mdlPossibleStartingUnits.removeElementAt(index);
        
        for (int i = 0; i < player.size(); i++) {
            player.get(i).handleDeletedUnit(unit.get(index));
        }
        //display, but do not store the old data
        displayData();
    }
    
    void deleteTechnology(int index)
    {   
        startingTechnologyModel.removeElementAt(index);
        //indexes will be updated when user next views this tab
    }

    /**
     * Should only be called by EditorTab.java.
     * @param i - The index that should be set.
     */
    public void setSelectedIndex(int i)
    {
        playerIndex = i;
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
        addLengthDocumentListener(31, txtLEADLeaderName);
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
        addBadValueDocumentListener(1000000, txtLEADStartCash);
        addBadValueDocumentListener(25, txtLEADUnitsOfThisType);
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
    public boolean checkBounds(List<String> valuesWithErrors)
    {
        if (!baselink.hasCustomPlayerData())
            return true;
        return super.checkBounds(valuesWithErrors);
    }
}
