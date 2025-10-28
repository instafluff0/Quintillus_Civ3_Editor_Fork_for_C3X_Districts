package com.civfanatics.civ3.xplatformeditor;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * @Completed
 * 
 * TODO: Check whether the default of 0 for NumBuildings is safe or not.  Either
 * way probably ought to make it 1 for Firaxis editor compatibility.
 * TODO: Checks with SuperLists.  Deletes in particular but also add would be
 * nice.
 */

/**
 *
 * @author Quintillus
 */
import com.civfanatics.civ3.biqFile.BLDG;
import com.civfanatics.civ3.biqFile.CITY;
import com.civfanatics.civ3.biqFile.PRTO;
import com.civfanatics.civ3.biqFile.GOOD;
import com.civfanatics.civ3.biqFile.GOVT;
import com.civfanatics.civ3.biqFile.FLAV;
import com.civfanatics.civ3.biqFile.RULE;
import com.civfanatics.civ3.biqFile.TECH;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.QueryList;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJList;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJTextField;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperListModel;
import com.civfanatics.civ3.xplatformeditor.specialty.PredicateFactory;
import com.civfanatics.civ3.xplatformeditor.specialty.SafetyLevel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JMenuItem;

/**
 * This class represents the Building tab in a Civ3 editor.
 * 
 * @author Andrew
 */
public class BldgTab extends EditorTab {

    public List<BLDG>buildings;
    public List<PRTO>unit;
    public List<GOOD>resource;
    public List<GOVT>government;
    public List<TECH>technology;
    public List<FLAV>flavor;
    public List<RULE>rule;
    private UnitTab unitTab;
    private MapTab mapTab;

    int buildingIndex;
    private SuperListModel buildingList;
    DefaultComboBoxModel mdlReqImprovement = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlGainInEveryCity = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlReqResource2 = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlReqResource1 = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlReqGovernment = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlReqAdvance = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlGainOnContinent = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlUnitProduced = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlObsolete = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlDoublesHappiness = new DefaultComboBoxModel();
    boolean tabCreated;

    public int communicationReceived;
    private javax.swing.ButtonGroup btnGroupCategory;

    JMenuItem delete;
    JMenuItem add;
    
    //JavaFX stuff
    final JFXPanel jFXPanel = new JFXPanel();
    
    ListView<BLDG> listView = new ListView<>();
    //Not sure if I can do this here or if it will blow up b/c it's JavaFX.  Let's find out.
    ObservableList<BLDG> bldgListFX = FXCollections.observableArrayList();
    ContextMenu bldgListPopup = new ContextMenu();
    MenuItem menuAdd = new MenuItem("Add");
    MenuItem menuRename = new MenuItem("Rename");
    MenuItem menuDelete = new MenuItem("Delete");

    public void setSelectedIndex(int i)
    {
        buildingIndex = i;
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
     * @param buildings - The list of all the buildings.
     * @param resource - The list of all the resources.
     * @param government - The list of all the governments.
     * @param technology - The list of all the technologies.
     * @param unit - The list of all the units.
     */
    public void sendData(List<BLDG>buildings, List<GOOD>resource, List<GOVT>government, List<TECH>technology, List<PRTO>unit, List<RULE> rule)
    {
        assert tabCreated:"The tab must be created before data is send to it.";
        this.buildings = buildings;
        this.resource = resource;
        this.government = government;
        this.technology = technology;
        this.unit = unit;
        this.rule = rule;
        buildingIndex = -1;

        buildingList = new SuperListModel();
        lstBuildings.setModel(buildingList);

        //initialize all check boxes to contain a noneSelected element
        cmbBLDGReqImprovement.removeAllItems();
        cmbBLDGReqImprovement.addItem(noneSelected);
        cmbBLDGReqGovernment.removeAllItems();
        cmbBLDGReqGovernment.addItem(noneSelected);

        cmbBLDGReqAdvance.removeAllItems();
        cmbBLDGReqAdvance.addItem(noneSelected);

        cmbBLDGReqResource1.removeAllItems();
        cmbBLDGReqResource1.addItem(noneSelected);
        cmbBLDGReqResource2.removeAllItems();
        cmbBLDGReqResource2.addItem(noneSelected);

        cmbBLDGDoublesHappiness.removeAllItems();
        cmbBLDGDoublesHappiness.addItem(noneSelected);

        cmbBLDGGainInEveryCity.removeAllItems();
        cmbBLDGGainInEveryCity.addItem(noneSelected);

        cmbBLDGGainOnContinent.removeAllItems();
        cmbBLDGGainOnContinent.addItem(noneSelected);

        cmbBLDGUnitProduced.removeAllItems();
        cmbBLDGUnitProduced.addItem(noneSelected);

        cmbBLDGObsoleteBy.removeAllItems();
        cmbBLDGObsoleteBy.addItem(noneSelected);

        for (int i = 0; i < buildings.size(); i++)
        {
            logger.debug("size of building: " + buildings.size());
            logger.debug("adding building: " + buildings.get(i).getName());
            buildingList.addIndexedElement(buildings.get(i).getName());
            //add buildings to combo lists that need buildings
            logger.debug("putting in req improvement combo: " + buildings.get(i).getName());
            utils.addWithPossibleDuplicates(buildings.get(i).getName(), mdlReqImprovement);
            logger.debug("putting in every city combo: " + buildings.get(i).getName());
            utils.addWithPossibleDuplicates(buildings.get(i).getName(), mdlGainInEveryCity);
            logger.debug("putting in on continent combo: " + buildings.get(i).getName());
            utils.addWithPossibleDuplicates(buildings.get(i).getName(), mdlGainOnContinent);
            logger.debug("putting in doubles happiness combo: " + buildings.get(i).getName());
            utils.addWithPossibleDuplicates(buildings.get(i).getName(), mdlDoublesHappiness);
        }

        for (int i = 0; i < technology.size(); i++)
        {
            utils.addWithPossibleDuplicates(technology.get(i).getName(), mdlReqAdvance);
            utils.addWithPossibleDuplicates(technology.get(i).getName(), mdlObsolete);
        }
        for (int i = 0; i < government.size(); i++)
        {
            utils.addWithPossibleDuplicates(government.get(i).getName(), mdlReqGovernment);
        }
        for (int i = 0; i < resource.size(); i++)
        {
            utils.addWithPossibleDuplicates(resource.get(i).getName(), mdlReqResource1);
            utils.addWithPossibleDuplicates(resource.get(i).getName(), mdlReqResource2);
        }
        for (int i = 0; i < unit.size(); i++)
        {
            utils.addWithPossibleDuplicates(unit.get(i).getName(), mdlUnitProduced);
        }
        /**
        flavorModel.clear();
        for (int i = 0; i < flavor.size(); i++)
            flavorModel.addElement(flavor.get(i).getName());
          **/
        if (logger.isTraceEnabled())
            logger.trace("BLDG tab - Data received");
        
        if (Main.settings.useJavaFX) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < buildings.size(); i++)
                    {
                        bldgListFX.add(buildings.get(i));
                    }
                }
            });
        }
    }

    /**
     * sendTabLinks methods are used to send references to other tabs to a tab
     * that needs access to GUI elements on those other tabs.
     * The BLDG tab needs access to the UnitTab (for the Legal Building
     * Telepad property).
     *
     * @param unitTab - Reference to the unit tab.
     */
    public void sendTabLinks(UnitTab unitTab, MapTab mapTab)
    {
        this.unitTab = unitTab;
        this.mapTab = mapTab;
    }



    /**
     * Gets building <i>index</i> from the list of buildings.
     * @param index
     * @return
     */
    public BLDG getBuilding(int index)
    {
        return buildings.get(index);
    }

    /**
     * The constructor.  Initializes the GUI elements.
     */
    public BldgTab()
    {
        if (Main.settings.useJavaFX) {
            setupJavaFX();
        }
        lstType = lstBuildings;
        tabName = "BLDG";
        textBoxes = new ArrayList<>();
        //this = new JPanel();
        scrPaneBuildings = new JScrollPane();
        pnlProperties = new JPanel();
        lblCivilopediaEntry = new JLabel();
        txtBLDGCivilopediaEntry = new SuperJTextField();
        rbtnBLDGWonder = new JRadioButton();
        rbtnBLDGSmallWonder = new JRadioButton();
        rbtnBLDGImprovement = new JRadioButton();
        lblCost = new JLabel();
        txtBLDGCost = new SuperJTextField();
        lblMaintenance = new JLabel();
        lblCulture = new JLabel();
        lblProductionBonus = new JLabel();
        lblPollution = new JLabel();
        txtBLDGMaintenanceCost = new SuperJTextField();
        txtBLDGCulture = new SuperJTextField();
        txtBLDGProduction = new SuperJTextField();
        txtBLDGPollution = new SuperJTextField();
        pnlRequirements = new JPanel();
        pnlResourcesRequirements = new JPanel();
        cmbBLDGReqResource2 = new JComboBox();
        chkBLDGGoodsMustBeInCityRadius = new JCheckBox();
        cmbBLDGReqResource1 = new JComboBox();
        lblBuilding = new JLabel();
        cmbBLDGReqImprovement = new JComboBox();
        lblGovernment = new JLabel();
        cmbBLDGReqGovernment = new JComboBox();
        cmbBLDGReqAdvance = new JComboBox();
        lblTechnology = new JLabel();
        chkBLDGMustBeNearWater = new JCheckBox();
        chkBLDGMustBeNearRiver = new JCheckBox();
        chkBLDGCoastalInstallation = new JCheckBox();
        txtBLDGNumReqBuildings = new SuperJTextField();
        lblNumBuildingsRequired = new JLabel();
        chkEliteShip = new JCheckBox();
        txtBLDGArmiesRequired = new SuperJTextField();
        jLabel18 = new JLabel();
        chkBLDGRequiresVictoriousArmy = new JCheckBox();
        pnlMilitary = new JPanel();
        lblSeaBombard = new JLabel();
        lblLandBombard = new JLabel();
        txtBLDGNavalDefenceBonus = new SuperJTextField();
        txtBLDGBombardDefence = new SuperJTextField();
        lblAirAttack = new JLabel();
        lblSeaAttack = new JLabel();
        txtBLDGNavalBombardDefence = new SuperJTextField();
        txtBLDGNavalPower = new SuperJTextField();
        lblDefenceBonus = new JLabel();
        txtBLDGAirPower = new SuperJTextField();
        lblNavalDefence = new JLabel();
        txtBLDGDefenceBonus = new SuperJTextField();
        jPanel12 = new JPanel();
        chkBLDGVeteranAirUnits = new JCheckBox();
        chkBLDGVeteranUnits = new JCheckBox();
        chkBLDGVeteranSeaUnits = new JCheckBox();
        chkStealthBarrier = new JCheckBox();
        chkBLDGDecreasesSuccessOfMissiles = new JCheckBox();
        chkBLDGBuildArmiesWithoutLeader = new JCheckBox();
        chkBLDGBuildLargerArmies = new JCheckBox();
        chkBLDGAllowsNuclearWeapons = new JCheckBox();
        chkBLDGDoubleCombatVsBarbarians = new JCheckBox();
        chkBLDGIncreasedShipMovement = new JCheckBox();
        chkBLDGPlusTwoShipMovement = new JCheckBox();
        chkBLDGSafeSeaTravel = new JCheckBox();
        chkBLDGCheaperUpgrades = new JCheckBox();
        chkBLDGAllowsHealingInEnemyTerritory = new JCheckBox();
        chkBLDGIncreasedArmyValue = new JCheckBox();
        chkBLDGDoubleCityDefences = new JCheckBox();
        chkBLDGIncreasesChanceOfLeaderAppearance = new JCheckBox();
        pnlTrade = new JPanel();
        chkBLDGAllowAirTrade = new JCheckBox();
        chkBLDGAllowWaterTrade = new JCheckBox();
        chkBLDGCapitalization = new JCheckBox();
        chkBLDGIncreasedTaxes = new JCheckBox();
        chkBLDGIncreasesTradeInWater = new JCheckBox();
        chkBLDGTreasuryEarnsInterest = new JCheckBox();
        chkBLDGIncreasedTrade = new JCheckBox();
        pnlReducesCorruption = new JPanel();
        chkBLDGReducesCorruption = new JCheckBox();
        chkBLDGForbiddenPalace = new JCheckBox();
        chkBLDGPaysTradeMaintenance = new JCheckBox();
        pnlHappiness = new JPanel();
        jLabel25 = new JLabel();
        jLabel26 = new JLabel();
        jLabel27 = new JLabel();
        jLabel28 = new JLabel();
        chkBLDGContinentalMoodEffects = new JCheckBox();
        txtBLDGUnhappyAll = new SuperJTextField();
        txtBLDGHappy = new SuperJTextField();
        txtBLDGUnhappy = new SuperJTextField();
        txtBLDGHappyAll = new SuperJTextField();
        jPanel16 = new JPanel();
        chkBLDGReducesWarWeariness = new JCheckBox();
        chkBLDGReduceWarWeariness = new JCheckBox();
        chkBLDGIncreasesLuxTax = new JCheckBox();
        chkBLDGMarketplaceLuxEffect = new JCheckBox();
        jLabel29 = new JLabel();
        cmbBLDGDoublesHappiness = new JComboBox();
        pnlFood = new JPanel();
        jPanel18 = new JPanel();
        chkBLDGAllowCityLevel2 = new JCheckBox();
        chkBLDGAllowCityLevel3 = new JCheckBox();
        chkBLDGIncreasesFoodInWater = new JCheckBox();
        chkBLDGDoublesCityGrowthRate = new JCheckBox();
        chkBLDGDoubleCityGrowth = new JCheckBox();
        pnlScience = new JPanel();
        chkBLDGIncreasedResearch = new JCheckBox();
        chkBLDGDoublesResearchOutput = new JCheckBox();
        chkBLDGTwoFreeAdvances = new JCheckBox();
        chkBLDGGainAnyTechsKnownByTwoCivs = new JCheckBox();
        pnlGain = new JPanel();
        jLabel30 = new JLabel();
        cmbBLDGGainInEveryCity = new JComboBox();
        jLabel31 = new JLabel();
        cmbBLDGGainOnContinent = new JComboBox();
        jLabel32 = new JLabel();
        cmbBLDGUnitProduced = new JComboBox();
        jLabel33 = new JLabel();
        txtBLDGUnitFrequency = new SuperJTextField();
        pnlOther = new JPanel();
        chkBLDGCenterOfEmpire = new JCheckBox();
        chkBLDGReplacesOtherWithThisTag = new JCheckBox();
        chkBLDGRemovePopPollution = new JCheckBox();
        chkBLDGReduceBldgPollution = new JCheckBox();
        chkBLDGMayExplodeOrMeltdown = new JCheckBox();
        chkDoublesSacrifice = new JCheckBox();
        chkBLDGIncreasesShieldsInWater = new JCheckBox();
        chkBLDGResistantToBribery = new JCheckBox();
        jLabel34 = new JLabel();
        txtBLDGSpaceshipPart = new SuperJTextField();
        chkBLDGBuildSpaceshipParts = new JCheckBox();
        chkBLDGTouristAttraction = new JCheckBox();
        chkBLDGAllowSpyMissions = new JCheckBox();
        chkBLDGAllowDiplomaticVictory = new JCheckBox();
        pnlCharacteristics = new JPanel();
        chkBLDGMilitaryInstallation = new JCheckBox();
        chkBLDGPlaceOfWorship = new JCheckBox();
        chkBLDGTradeInstallation = new JCheckBox();
        chkBLDGConstructionInstallation = new JCheckBox();
        chkBLDGAgricultural = new JCheckBox();
        chkBLDGSeafaring = new JCheckBox();
        chkBLDGResearchInstallation = new JCheckBox();
        chkBLDGExplorationInstallation = new JCheckBox();
        pnlFlavours = new JPanel();
        jScrollPane2 = new JScrollPane();
        lstBLDGFlavors = new JList();
        cmbBLDGObsoleteBy = new JComboBox();
        lblObsoleteBy = new JLabel();
        btnAdd = new JButton();
        btnDel = new JButton();
        chkBLDGCharmBarrier = new JCheckBox();
        chkBLDGGeneralTelepad = new JCheckBox();

        btnGroupCategory = new javax.swing.ButtonGroup();
        //flavorModel = new DefaultListModel();

        //set up Firaxis flag set
        //Max values: Cost=Land Bombard=Sea Bombard=Air Attack=Sea Attack=Defence1000, Happy/Unhappy/Happy All/Unhappy All = 255, Main=Culture=Production=Pollution=100, Armies Required = 50, Buildings Required = 25
        //The flags were to be used with the Flag Set by L. Barnett
        //As the implementation was already finished, a possible refactoring with the FlagSet was tabled
        //String[]firaxisFlagsIds = {"Cost", "Maintenance", "Culture", "Production", "Pollution", "Buildings Required", "Armies Required", "Land Bombard", "Naval Bombard", "Air Attack", "Sea Attack", "Defence Bonus", "Happy", "Unhappy", "Happy All", "Unhappy All"};
    }
    /**
     * Creates the GUI of the tab.  This includes button groups, layouts, etc.
     * Logically, this is to occur immediately after the constructor is called.
     *
     * @return - A reference to the BldgTab instance itself.  This reference
     * allows the tab to be added to a tab bar.
     */
    public JPanel createTab()
    {
        //this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        this.setLayout(new GridBagLayout());

        lstBuildings.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstBuildings.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstBuildingsValueChanged(evt);
            }
        });
        scrPaneBuildings.setViewportView(lstBuildings);


        pnlProperties.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Properties"));
        pnlProperties.setMaximumSize(new java.awt.Dimension(355, 175));

        txtBLDGCivilopediaEntry.setToolTipText("The identifier for this building that you'll use when setting up the Civilopedia.");
        lblCivilopediaEntry.setText("Civilopedia Entry");

        btnGroupCategory.add(rbtnBLDGWonder);
        rbtnBLDGWonder.setText("Wonder");
        rbtnBLDGWonder.setToolTipText("A Wonder can only be built one time in the whole game.");
        btnGroupCategory.add(rbtnBLDGSmallWonder);
        rbtnBLDGSmallWonder.setToolTipText("A Small Wonder can be built once per civilization.");
        rbtnBLDGSmallWonder.setText("Small Wonder");
        btnGroupCategory.add(rbtnBLDGImprovement);
        rbtnBLDGImprovement.setToolTipText("An Improvement can be built in every city.");
        rbtnBLDGImprovement.setText("Improvement");

        txtBLDGCost.setToolTipText("The value here times 10 will be the cost of this building in shields.");
        lblCost.setText("Cost:");

        txtBLDGMaintenanceCost.setToolTipText("Per-turn maintenance cost.");
        lblMaintenance.setText("Maintenance: ");

        txtBLDGCulture.setToolTipText("Per-turn culture production.  Doubles after 1000 years.");
        lblCulture.setText("Culture: ");

        txtBLDGProduction.setToolTipText("Will increase production in this city by 25% times this value.  A value of 2 means a 50% increase.");
        lblProductionBonus.setText("Production bonus: ");

        txtBLDGPollution.setToolTipText("Increases the chance of pollution by this percentage.  \nAdding a building with a value of 2 means a city that used to have a 7% chance now has a 9% chance.");
        lblPollution.setText("Pollution:");

        org.jdesktop.layout.GroupLayout pnlPropertiesLayout = new org.jdesktop.layout.GroupLayout(pnlProperties);
        pnlProperties.setLayout(pnlPropertiesLayout);
        pnlPropertiesLayout.setHorizontalGroup(
            pnlPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlPropertiesLayout.createSequentialGroup()
                .add(pnlPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(pnlPropertiesLayout.createSequentialGroup()
                        .add(pnlPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblCost)
                            .add(lblMaintenance)
                            .add(lblCulture)
                            .add(lblCivilopediaEntry))
                        .add(25, 25, 25)
                        .add(pnlPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(txtBLDGCivilopediaEntry, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 213, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(pnlPropertiesLayout.createSequentialGroup()
                                .add(pnlPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, pnlPropertiesLayout.createSequentialGroup()
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(txtBLDGProduction))
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, txtBLDGCost, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, txtBLDGMaintenanceCost, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, txtBLDGCulture))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(pnlPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(pnlPropertiesLayout.createSequentialGroup()
                                        .add(lblPollution)
                                        .add(3, 3, 3)
                                        .add(txtBLDGPollution, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
                                    .add(rbtnBLDGWonder)
                                    .add(rbtnBLDGSmallWonder)
                                    .add(rbtnBLDGImprovement)))))
                    .add(lblProductionBonus))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        pnlPropertiesLayout.setVerticalGroup(
            pnlPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlPropertiesLayout.createSequentialGroup()
                .add(pnlPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(txtBLDGCivilopediaEntry, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblCivilopediaEntry))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(pnlPropertiesLayout.createSequentialGroup()
                        .add(pnlPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(lblCost)
                            .add(txtBLDGCost, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(pnlPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(lblMaintenance)
                            .add(txtBLDGMaintenanceCost, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(pnlPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(lblCulture)
                            .add(txtBLDGCulture, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(pnlPropertiesLayout.createSequentialGroup()
                        .add(rbtnBLDGWonder)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(rbtnBLDGSmallWonder, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(rbtnBLDGImprovement)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(lblProductionBonus)
                    .add(pnlPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(txtBLDGProduction, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(lblPollution)
                        .add(txtBLDGPollution, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
        );

        pnlRequirements.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Requirements"));
        pnlRequirements.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlResourcesRequirements.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Resources"));
        pnlResourcesRequirements.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cmbBLDGReqResource2.setModel(mdlReqResource2);
        pnlResourcesRequirements.add(cmbBLDGReqResource2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 90, -1));

        chkBLDGGoodsMustBeInCityRadius.setToolTipText("If checked, the above resources must be within the city's 21-tile radius for the city to build this building.");
        chkBLDGGoodsMustBeInCityRadius.setText("In city radius");
        pnlResourcesRequirements.add(chkBLDGGoodsMustBeInCityRadius, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 100, -1));

        cmbBLDGReqResource1.setModel(mdlReqResource1);
        pnlResourcesRequirements.add(cmbBLDGReqResource1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 90, -1));

        pnlRequirements.add(pnlResourcesRequirements, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, 120, 120));

        lblBuilding.setText("Building: ");
        pnlRequirements.add(lblBuilding, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        cmbBLDGReqImprovement.setToolTipText("If Number is 1, this city must already have indicated building to build this one.  If Number is > 1, the empire must have at least Number of indicated building for a city to build this one.");
        cmbBLDGReqImprovement.setModel(mdlReqImprovement);
        pnlRequirements.add(cmbBLDGReqImprovement, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, 100, -1));

        lblGovernment.setText("Government:");
        pnlRequirements.add(lblGovernment, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));

        cmbBLDGReqGovernment.setToolTipText("A civilization must have this government to build this building and for it to be effective.");
        cmbBLDGReqGovernment.setModel(mdlReqGovernment);
        pnlRequirements.add(cmbBLDGReqGovernment, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, 100, -1));

        cmbBLDGReqAdvance.setToolTipText("A civilization must have researched this technology to build this building.");
        cmbBLDGReqAdvance.setModel(mdlReqAdvance);
        pnlRequirements.add(cmbBLDGReqAdvance, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 85, 100, -1));

        lblTechnology.setText("Technology: ");
        pnlRequirements.add(lblTechnology, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        chkBLDGMustBeNearWater.setToolTipText("NO TOOLTIP YET");
        chkBLDGMustBeNearWater.setText("By water");
        pnlRequirements.add(chkBLDGMustBeNearWater, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 130, -1, -1));

        chkBLDGMustBeNearRiver.setToolTipText("There must be a river in the city's radius for it to built this building.");
        chkBLDGMustBeNearRiver.setText("Near river");
        pnlRequirements.add(chkBLDGMustBeNearRiver, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, -1));

        chkBLDGCoastalInstallation.setToolTipText("This building can only be built in coastal cities.");
        chkBLDGCoastalInstallation.setText("Coastal");
        pnlRequirements.add(chkBLDGCoastalInstallation, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, -1));
        pnlRequirements.add(txtBLDGNumReqBuildings, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, 100, -1));

        lblNumBuildingsRequired.setText("Number:");
        pnlRequirements.add(lblNumBuildingsRequired, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        chkEliteShip.setToolTipText("The civilization must have an Elite ship somewhere to build this improvement.");
        chkEliteShip.setText("Elite Ship");
        pnlRequirements.add(chkEliteShip, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 130, -1, -1));
        pnlRequirements.add(txtBLDGArmiesRequired, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 130, 20, 20));

        jLabel18.setText("Armies:");
        pnlRequirements.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 130, -1, -1));

        chkBLDGRequiresVictoriousArmy.setToolTipText("The civilization must have had an army win a battle to build this improvement.");
        chkBLDGRequiresVictoriousArmy.setText("Victorious army");
        pnlRequirements.add(chkBLDGRequiresVictoriousArmy, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 110, -1, -1));

        pnlMilitary.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Military"));
        pnlMilitary.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());


        lblLandBombard.setText("Land Bombard:");
        pnlMilitary.add(lblLandBombard, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));
        pnlMilitary.add(txtBLDGBombardDefence, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 35, -1));

        lblAirAttack.setText("Air attack: ");
        pnlMilitary.add(lblAirAttack, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, -1, -1));
        pnlMilitary.add(txtBLDGAirPower, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 35, -1));

        lblDefenceBonus.setText("Defence Bonus:");
        pnlMilitary.add(lblDefenceBonus, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, -1, -1));
        pnlMilitary.add(txtBLDGDefenceBonus, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, 35, -1));
        
        lblSeaBombard.setText("Sea Bombard:");
        pnlMilitary.add(lblSeaBombard, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));
        pnlMilitary.add(txtBLDGNavalBombardDefence, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, 35, -1));
        
        lblSeaAttack.setText("Sea attack:");
        pnlMilitary.add(lblSeaAttack, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 40, -1, -1));
        pnlMilitary.add(txtBLDGNavalPower, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 40, 35, -1));

        lblNavalDefence.setText("Naval defence:");
        pnlMilitary.add(lblNavalDefence, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 40, 90, -1));
        pnlMilitary.add(txtBLDGNavalDefenceBonus, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 40, 35, -1));

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Veteran"));

        chkBLDGVeteranAirUnits.setToolTipText("Host city will now produce veteran air units.");
        chkBLDGVeteranAirUnits.setText("Air");

        chkBLDGVeteranUnits.setToolTipText("Host city will now produce veteran land units and will heal units completely in one turn.");
        chkBLDGVeteranUnits.setText("Land");

        chkBLDGVeteranSeaUnits.setToolTipText("Host city will now produce veteran sea units.");
        chkBLDGVeteranSeaUnits.setText("Sea");

        org.jdesktop.layout.GroupLayout jPanel12Layout = new org.jdesktop.layout.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel12Layout.createSequentialGroup()
                .add(chkBLDGVeteranAirUnits)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chkBLDGVeteranUnits)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chkBLDGVeteranSeaUnits)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel12Layout.createSequentialGroup()
                .add(jPanel12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chkBLDGVeteranAirUnits)
                    .add(chkBLDGVeteranUnits)
                    .add(chkBLDGVeteranSeaUnits))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        pnlMilitary.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 160, 60));

        chkStealthBarrier.setText("Stealth Barrier");
        chkStealthBarrier.setToolTipText("NO TOOLTIP TEXT YET");
        pnlMilitary.add(chkStealthBarrier, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 60, -1, -1));

        chkBLDGDecreasesSuccessOfMissiles.setText("75% defence vs ICBM's");
        chkBLDGDecreasesSuccessOfMissiles.setToolTipText("Provides building civilization a 75% chance of shooting down ICBMs");
        pnlMilitary.add(chkBLDGDecreasesSuccessOfMissiles, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, -1, -1));

        chkBLDGBuildArmiesWithoutLeader.setText("Armies without leader");
        chkBLDGBuildArmiesWithoutLeader.setToolTipText("This city can build armies with no leader required.");
        pnlMilitary.add(chkBLDGBuildArmiesWithoutLeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, -1, -1));

        chkBLDGBuildLargerArmies.setText("Larger armies");
        chkBLDGBuildLargerArmies.setToolTipText("NO TOOLTIP YET.");
        pnlMilitary.add(chkBLDGBuildLargerArmies, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 120, -1, -1));

        chkBLDGAllowsNuclearWeapons.setText("Nukes");
        chkBLDGAllowsNuclearWeapons.setToolTipText("Allows ANY civilization to build nuclear weapons.");
        pnlMilitary.add(chkBLDGAllowsNuclearWeapons, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 60, -1, -1));

        chkBLDGDoubleCombatVsBarbarians.setText("Double combat vs Barbarians");
        chkBLDGDoubleCombatVsBarbarians.setToolTipText("Building civilization has double combat values against barbarians.");
        pnlMilitary.add(chkBLDGDoubleCombatVsBarbarians, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 100, -1, -1));

        chkBLDGIncreasedShipMovement.setText("+1 sea moves");
        chkBLDGIncreasedShipMovement.setToolTipText("Increases water movement rate by one.  Building two of these will still only increase rate by 1.");
        pnlMilitary.add(chkBLDGIncreasedShipMovement, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 140, -1, -1));

        chkBLDGPlusTwoShipMovement.setText("+2 sea moves");
        chkBLDGPlusTwoShipMovement.setToolTipText("Increases water movement rate by two.");
        pnlMilitary.add(chkBLDGPlusTwoShipMovement, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 140, -1, -1));

        chkBLDGSafeSeaTravel.setText("Safe at sea");
        chkBLDGSafeSeaTravel.setToolTipText("The building civilization's ships will no longer sink at sea.");
        pnlMilitary.add(chkBLDGSafeSeaTravel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, -1));

        chkBLDGCheaperUpgrades.setText("Half cost upgrades");
        chkBLDGCheaperUpgrades.setToolTipText("The building civilization can upgrade units for half price.");
        pnlMilitary.add(chkBLDGCheaperUpgrades, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, -1));

        chkBLDGAllowsHealingInEnemyTerritory.setText("Can heal in enemy territory");
        chkBLDGAllowsHealingInEnemyTerritory.setToolTipText("Allows troops to heal one hitpoint per turn in enemy territory.");
        pnlMilitary.add(chkBLDGAllowsHealingInEnemyTerritory, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 160, -1, -1));

        chkBLDGIncreasedArmyValue.setText("Stonger armies");
        chkBLDGIncreasedArmyValue.setToolTipText("NO TOOLTIP YET.");
        pnlMilitary.add(chkBLDGIncreasedArmyValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, -1, -1));

        chkBLDGDoubleCityDefences.setText("Double city defences (global)");
        chkBLDGDoubleCityDefences.setToolTipText("Doubles city defences throughout the empire.");
        pnlMilitary.add(chkBLDGDoubleCityDefences, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 180, -1, -1));

        chkBLDGIncreasesChanceOfLeaderAppearance.setText("More leaders");
        chkBLDGIncreasesChanceOfLeaderAppearance.setToolTipText("Increases the chance of a leader appearing from combat.");
        pnlMilitary.add(chkBLDGIncreasesChanceOfLeaderAppearance, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 120, -1, -1));

        pnlTrade.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Trade"));
        //pnlTrade.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        
        GridBagLayout tradeLayout = new GridBagLayout();
        pnlTrade.setLayout(tradeLayout);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;

        chkBLDGAllowAirTrade.setText("Air trade");
        chkBLDGAllowAirTrade.setToolTipText("Allows host city to participate in air trade.");
        pnlTrade.add(chkBLDGAllowAirTrade, gbc);

        chkBLDGAllowWaterTrade.setText("Water trade");
        chkBLDGAllowWaterTrade.setToolTipText("Allows host city to participate in water trade.");
        gbc.gridx+=2;
        pnlTrade.add(chkBLDGAllowWaterTrade, gbc);

        chkBLDGCapitalization.setText("Capitalization");
        chkBLDGCapitalization.setToolTipText("When a city is building this improvement, all shields are converted into gold.");
        gbc.gridx+=2;
        pnlTrade.add(chkBLDGCapitalization, gbc);

        chkBLDGIncreasedTaxes.setText("+50% tax revenue");
        chkBLDGIncreasedTaxes.setToolTipText("Increases host city tax revenue by 50% (from base rate)");
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        pnlTrade.add(chkBLDGIncreasedTaxes, gbc);

        chkBLDGIncreasesTradeInWater.setText("Increased water trade");
        chkBLDGIncreasesTradeInWater.setToolTipText("NO TOOLTIP YET");
        gbc.gridx+=3;
        pnlTrade.add(chkBLDGIncreasesTradeInWater, gbc);

        chkBLDGIncreasedTrade.setToolTipText("Increases the amount of trade/commerce by 1 for every commerce-producing tile worked by its host city");
        chkBLDGIncreasedTrade.setText("+1 trade per tile");
        gbc.gridy++;
        pnlTrade.add(chkBLDGIncreasedTrade, gbc);

        chkBLDGPaysTradeMaintenance.setToolTipText("All player-owned buildings with the Commercial characteristic will no longer require any maintenance.");
        chkBLDGPaysTradeMaintenance.setText("Pays trade maintenance");
        gbc.gridy++;
        pnlTrade.add(chkBLDGPaysTradeMaintenance, gbc);

        chkBLDGTreasuryEarnsInterest.setText("5% treasury interest");
        chkBLDGTreasuryEarnsInterest.setToolTipText("Causes the civilization who built this building to gain 5% interest on their treasure per turn.");
        gbc.gridy++;
        gbc.gridheight = 3;
        pnlTrade.add(chkBLDGTreasuryEarnsInterest, gbc);

        pnlReducesCorruption.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Reduces corruption"));

        chkBLDGReducesCorruption.setToolTipText("Decreases corruption in host city.");
        chkBLDGReducesCorruption.setText("City");

        chkBLDGForbiddenPalace.setToolTipText("Decreases corruption throughout the empire (Forbidden Palace effect).");
        chkBLDGForbiddenPalace.setText("Empire");

        org.jdesktop.layout.GroupLayout jPanel14Layout = new org.jdesktop.layout.GroupLayout(pnlReducesCorruption);
        pnlReducesCorruption.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel14Layout.createSequentialGroup()
                .add(chkBLDGReducesCorruption)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(chkBLDGForbiddenPalace, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel14Layout.createSequentialGroup()
                .add(jPanel14Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chkBLDGReducesCorruption)
                    .add(chkBLDGForbiddenPalace))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        gbc.gridx = 0;
        gbc.gridy = 2;
        pnlTrade.add(pnlReducesCorruption, gbc);

        pnlHappiness.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Happiness"));
        pnlHappiness.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel25.setText("City");
        pnlHappiness.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, -1, -1));

        jLabel26.setText("Global");
        pnlHappiness.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, -1, -1));

        jLabel27.setText("Happy faces");
        pnlHappiness.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jLabel28.setText("Unhappy faces");
        pnlHappiness.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));

        chkBLDGContinentalMoodEffects.setToolTipText("Happiness effects apply to all cities owned by player with a land connection to this city, but none on other landmasses (only applies to Global happy/unhappy faces).");
        chkBLDGContinentalMoodEffects.setText("Continental");
        pnlHappiness.add(chkBLDGContinentalMoodEffects, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));
        txtBLDGUnhappyAll.setToolTipText("Increases the number of unhappy citizens throughout the empire (or continentally if checked)");
        txtBLDGHappyAll.setToolTipText("Increases the number of happy citizens throughout the empire (or continentally if checked)");
        txtBLDGHappy.setToolTipText("Increases the number of happy citizens in host city (cumulative with global effects)");
        txtBLDGUnhappy.setToolTipText("Increases the number of unhappy citizens in host city (cumulative with global effects)");
        pnlHappiness.add(txtBLDGUnhappyAll, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 60, 30, -1));
        pnlHappiness.add(txtBLDGHappy, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, 30, -1));
        pnlHappiness.add(txtBLDGUnhappy, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, 30, -1));
        pnlHappiness.add(txtBLDGHappyAll, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 40, 30, -1));

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Reduces war weariness"));

        chkBLDGReducesWarWeariness.setToolTipText("Reduces war weariness in the host city.");
        chkBLDGReducesWarWeariness.setText("City");

        chkBLDGReduceWarWeariness.setToolTipText("Reduces war weariness throughout the empire.");
        chkBLDGReduceWarWeariness.setText("Empire");

        org.jdesktop.layout.GroupLayout jPanel16Layout = new org.jdesktop.layout.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel16Layout.createSequentialGroup()
                .add(chkBLDGReducesWarWeariness)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(chkBLDGReduceWarWeariness, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel16Layout.createSequentialGroup()
                .add(jPanel16Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chkBLDGReducesWarWeariness)
                    .add(chkBLDGReduceWarWeariness))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        pnlHappiness.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, 140, 61));

        chkBLDGIncreasesLuxTax.setToolTipText("The city will now get +2 happiness from luxuries 3 and 4, +3 from luxuries 5 and 6, etc.");
        chkBLDGIncreasesLuxTax.setText("More luxury happiness");
        pnlHappiness.add(chkBLDGIncreasesLuxTax, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

        chkBLDGMarketplaceLuxEffect.setToolTipText("Each gold invested in luxury happiness for this city now produces 1.5 gold instead of 1 (rounded down).");
        chkBLDGMarketplaceLuxEffect.setText("+50% luxury tax");
        pnlHappiness.add(chkBLDGMarketplaceLuxEffect, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 80, -1, -1));

        jLabel29.setText("Doubles happiness of:");
        pnlHappiness.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, 30));

        cmbBLDGDoublesHappiness.setModel(mdlDoublesHappiness);
        pnlHappiness.add(cmbBLDGDoublesHappiness, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 110, 170, -1));

        pnlFood.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder()), "Food"));
        pnlFood.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Allows city size"));

        chkBLDGAllowCityLevel2.setToolTipText("Allows cities of size level two (see RULE tab, City Size Limits)");
        chkBLDGAllowCityLevel2.setText("2");

        chkBLDGAllowCityLevel3.setToolTipText("Allows cities of size level three (see RULE tab, City Size Limits)");
        chkBLDGAllowCityLevel3.setText("3");

        org.jdesktop.layout.GroupLayout jPanel18Layout = new org.jdesktop.layout.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel18Layout.createSequentialGroup()
                .add(chkBLDGAllowCityLevel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(chkBLDGAllowCityLevel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel18Layout.createSequentialGroup()
                .add(jPanel18Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chkBLDGAllowCityLevel2)
                    .add(chkBLDGAllowCityLevel3))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        pnlFood.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 10, 100, 60));

        chkBLDGIncreasesFoodInWater.setText("+1 food in water");
        pnlFood.add(chkBLDGIncreasesFoodInWater, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        chkBLDGDoublesCityGrowthRate.setText("Cities store food");
        pnlFood.add(chkBLDGDoublesCityGrowthRate, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, -1, -1));

        chkBLDGDoubleCityGrowth.setText("Cities gain +2 population instead of +1");
        pnlFood.add(chkBLDGDoubleCityGrowth, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        pnlScience.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Science"));

        chkBLDGIncreasedResearch.setToolTipText("Increases the amount of research by 50% (from the base level) in host city.");
        chkBLDGIncreasedResearch.setText("+50% in city");

        chkBLDGDoublesResearchOutput.setToolTipText("Increases the amount of research by 100% (from the base level) in host city.");
        chkBLDGDoublesResearchOutput.setText("+100% in city");

        chkBLDGTwoFreeAdvances.setToolTipText("The civilization instantly discovers the technology they are researching plus one additonal technology of their choice.");
        chkBLDGTwoFreeAdvances.setText("2 free advances");

        chkBLDGGainAnyTechsKnownByTwoCivs.setToolTipText("Any civ with such a building will automatically gain any tech that has already been researched by any two other civilizations that they have met.  This also applies to civilization who capture this building.");
        chkBLDGGainAnyTechsKnownByTwoCivs.setText("Gain any technology known by two civs");

        org.jdesktop.layout.GroupLayout jPanel19Layout = new org.jdesktop.layout.GroupLayout(pnlScience);
        pnlScience.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel19Layout.createSequentialGroup()
                .add(jPanel19Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel19Layout.createSequentialGroup()
                        .add(chkBLDGIncreasedResearch)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(chkBLDGDoublesResearchOutput)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(chkBLDGTwoFreeAdvances))
                    .add(chkBLDGGainAnyTechsKnownByTwoCivs))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel19Layout.createSequentialGroup()
                .add(jPanel19Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chkBLDGIncreasedResearch)
                    .add(chkBLDGDoublesResearchOutput)
                    .add(chkBLDGTwoFreeAdvances))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chkBLDGGainAnyTechsKnownByTwoCivs)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlGain.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Gain"));

        jLabel30.setText("In every city:");

        cmbBLDGGainInEveryCity.setModel(mdlGainInEveryCity);

        jLabel31.setText("In every city on this continent:");

        cmbBLDGGainOnContinent.setModel(mdlGainOnContinent);

        jLabel32.setText("Unit:");

        cmbBLDGUnitProduced.setModel(mdlUnitProduced);

        jLabel33.setText("Frequency:");

        org.jdesktop.layout.GroupLayout jPanel20Layout = new org.jdesktop.layout.GroupLayout(pnlGain);
        pnlGain.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel20Layout.createSequentialGroup()
                .add(jPanel20Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel20Layout.createSequentialGroup()
                        .add(jLabel32)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cmbBLDGUnitProduced, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 141, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel33)
                        .add(4, 4, 4)
                        .add(txtBLDGUnitFrequency, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE))
                    .add(jPanel20Layout.createSequentialGroup()
                        .add(jPanel20Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel30)
                            .add(jLabel31))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jPanel20Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(cmbBLDGGainOnContinent, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(cmbBLDGGainInEveryCity, 0, 172, Short.MAX_VALUE))))
                .add(20, 20, 20))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel20Layout.createSequentialGroup()
                .add(jPanel20Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel30)
                    .add(cmbBLDGGainInEveryCity, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel20Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel31)
                    .add(cmbBLDGGainOnContinent, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel20Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel32)
                    .add(jPanel20Layout.createSequentialGroup()
                        .add(1, 1, 1)
                        .add(jPanel20Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(cmbBLDGUnitProduced, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel33)
                            .add(txtBLDGUnitFrequency, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlOther.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Other"));

        chkBLDGCenterOfEmpire.setText("Center of empire");

        chkBLDGReplacesOtherWithThisTag.setText("Replaces others with this flag");

        chkBLDGRemovePopPollution.setText("No population pollution");

        chkBLDGReduceBldgPollution.setText("Less building pollution");

        chkBLDGMayExplodeOrMeltdown.setText("Can meltdown");

        chkDoublesSacrifice.setText("Doubles sacrifice");

        chkBLDGIncreasesShieldsInWater.setText("More shields in water");

        chkBLDGResistantToBribery.setText("Propaganda Resistance");

        jLabel34.setText("Spaceship part:");

        chkBLDGBuildSpaceshipParts.setText("Can build spaceship parts");

        chkBLDGTouristAttraction.setText("Tourist Attraction");

        chkBLDGAllowSpyMissions.setText("Allows spies");

        chkBLDGAllowDiplomaticVictory.setText("Allows diplomatic victory");

        org.jdesktop.layout.GroupLayout jPanel21Layout = new org.jdesktop.layout.GroupLayout(pnlOther);
        pnlOther.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel21Layout.createSequentialGroup()
                .add(jPanel21Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel21Layout.createSequentialGroup()
                        .add(chkBLDGCenterOfEmpire)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(chkBLDGReplacesOtherWithThisTag))
                    .add(jPanel21Layout.createSequentialGroup()
                        .add(jPanel21Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel21Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, chkBLDGIncreasesShieldsInWater)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel21Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .add(jLabel34)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(txtBLDGSpaceshipPart, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                            .add(chkBLDGMayExplodeOrMeltdown)
                            .add(chkBLDGRemovePopPollution)
                            .add(chkBLDGTouristAttraction))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel21Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(chkBLDGAllowSpyMissions)
                            .add(chkBLDGBuildSpaceshipParts)
                            .add(chkBLDGReduceBldgPollution)
                            .add(chkDoublesSacrifice)
                            .add(chkBLDGResistantToBribery)))
                    .add(chkBLDGAllowDiplomaticVictory))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel21Layout.createSequentialGroup()
                .add(jPanel21Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chkBLDGCenterOfEmpire)
                    .add(chkBLDGReplacesOtherWithThisTag))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel21Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chkBLDGRemovePopPollution)
                    .add(chkBLDGReduceBldgPollution))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel21Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chkBLDGMayExplodeOrMeltdown)
                    .add(chkDoublesSacrifice))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel21Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chkBLDGIncreasesShieldsInWater, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(chkBLDGResistantToBribery))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel21Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(txtBLDGSpaceshipPart, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(chkBLDGBuildSpaceshipParts)
                    .add(jLabel34))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel21Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chkBLDGTouristAttraction)
                    .add(chkBLDGAllowSpyMissions))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(chkBLDGAllowDiplomaticVictory)
                .addContainerGap())
        );

        pnlCharacteristics.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Characteristics"));

        chkBLDGMilitaryInstallation.setText("Militaristic");

        chkBLDGPlaceOfWorship.setText("Religious");

        chkBLDGTradeInstallation.setText("Commercial");

        chkBLDGConstructionInstallation.setText("Industrial");

        chkBLDGAgricultural.setText("Agricultural");

        chkBLDGSeafaring.setText("Seafaring");

        chkBLDGResearchInstallation.setText("Scientific");

        chkBLDGExplorationInstallation.setText("Expansionist");

        org.jdesktop.layout.GroupLayout jPanel22Layout = new org.jdesktop.layout.GroupLayout(pnlCharacteristics);
        pnlCharacteristics.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel22Layout.createSequentialGroup()
                .add(jPanel22Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(chkBLDGMilitaryInstallation)
                    .add(chkBLDGPlaceOfWorship)
                    .add(chkBLDGTradeInstallation)
                    .add(chkBLDGConstructionInstallation)
                    .add(chkBLDGExplorationInstallation)
                    .add(chkBLDGResearchInstallation)
                    .add(chkBLDGAgricultural)
                    .add(chkBLDGSeafaring))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel22Layout.createSequentialGroup()
                .add(chkBLDGMilitaryInstallation)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chkBLDGPlaceOfWorship)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chkBLDGTradeInstallation)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chkBLDGConstructionInstallation)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chkBLDGExplorationInstallation)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chkBLDGResearchInstallation)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chkBLDGAgricultural)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chkBLDGSeafaring)
                .addContainerGap(91, Short.MAX_VALUE))
        );
        //Manually
        btnAdd.setText("Add");
        btnDel.setText("Delete");
        btnAdd.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                //new frmBuildingsInCities(baselink);
                addBuildingAction();
            }
        });
        btnDel.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                deleteAction();
            }
        });

        chkBLDGCharmBarrier.setText("Charm Barrier");
        chkBLDGGeneralTelepad.setText("General Telepad");

        pnlFlavours.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Flavors"));

        //lstBLDGFlavors.setModel(flavorModel);
        jScrollPane2.setViewportView(lstBLDGFlavors);

        org.jdesktop.layout.GroupLayout jPanel23Layout = new org.jdesktop.layout.GroupLayout(pnlFlavours);
        pnlFlavours.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
        );
        
        JPanel pnlObsolete = new JPanel();
        pnlObsolete.setLayout(new GridLayout(1, 2));
        cmbBLDGObsoleteBy.setModel(mdlObsolete);
        lblObsoleteBy.setText("Made obsolete by:");
        pnlObsolete.add(lblObsoleteBy);
        pnlObsolete.add(cmbBLDGObsoleteBy);
        
        //add all components
        
        //First the big list on the left
        GridBagConstraints mainConstraints = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.15;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTH;
        if (Main.settings.useJavaFX) {
            this.add(jFXPanel, gbc);
            //frmMainTab.add(jFXPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 170, 670));
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    initializeBldgListFX();
                }
            });
        }
        else {
            this.add(scrPaneBuildings, gbc);
        }
        
        //Second the leftmost column
        
        JPanel pnlLeft = new JPanel();
        pnlLeft.setLayout(new GridBagLayout());
        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.gridx = 0;
        gbcLeft.gridy = 0;
        gbcLeft.weightx = 0.1;
        gbcLeft.weighty = 0.1;
        gbcLeft.anchor = GridBagConstraints.WEST;
        gbcLeft.fill = GridBagConstraints.BOTH;
        pnlLeft.add(pnlProperties, gbcLeft);
        gbcLeft.gridy++;
        pnlLeft.add(pnlMilitary, gbcLeft);
        gbcLeft.gridy++;
        pnlLeft.add(pnlFood, gbcLeft);
        gbcLeft.gridy++;
        pnlLeft.add(pnlScience, gbcLeft);
        gbcLeft.gridy++;
        pnlLeft.add(pnlGain, gbcLeft);
        gbcLeft.gridy++;
        pnlLeft.add(pnlObsolete, gbcLeft);
        
        gbc.gridx++;
        gbc.weightx = 0.377;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(pnlLeft, gbc);
        
        //Third the next column to the right
        JPanel pnlMiddle = new JPanel();
        pnlMiddle.setLayout(new GridBagLayout());
        GridBagConstraints gbcMiddle = new GridBagConstraints();
        gbcMiddle.gridx = 0;
        gbcMiddle.gridy = 0;
        gbcMiddle.weightx = 0.1;
        gbcMiddle.weighty = 0.1;
        gbcMiddle.anchor = GridBagConstraints.WEST;
        gbcMiddle.fill = GridBagConstraints.BOTH;
        pnlMiddle.add(pnlRequirements, gbcMiddle);
        gbcMiddle.gridy++;
        pnlMiddle.add(pnlTrade, gbcMiddle);
        gbcMiddle.gridy++;
        pnlMiddle.add(pnlHappiness, gbcMiddle);
        gbcMiddle.gridy++;
        pnlMiddle.add(pnlOther, gbcMiddle);
        
        gbc.gridx++;
        gbc.weightx = 0.316;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(pnlMiddle, gbc);
        
        //Fourth the rightmost column
        JPanel pnlRight = new JPanel();
        pnlRight.setLayout(new GridBagLayout());
        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.gridx = 0;
        gbcRight.gridy = 0;
        gbcRight.weightx = 0.1;
        gbcRight.weighty = 0.0;
        gbcRight.anchor = GridBagConstraints.WEST;
        gbcRight.fill = GridBagConstraints.NONE;
        pnlRight.add(pnlCharacteristics, gbcRight);
        gbcRight.gridy++;
        pnlRight.add(chkBLDGCharmBarrier, gbcRight);
        gbcRight.gridy++;
        pnlRight.add(chkBLDGGeneralTelepad, gbcRight);
        gbcRight.gridy++;
        pnlRight.add(pnlFlavours, gbcRight);
        
        gbc.gridx++;
        gbc.weightx = 0.125;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(pnlRight, gbc);
        
        
        this.setName(tabName);

        tabCreated = true;
        return this;
    }
    private JButton btnAdd;
    private JButton btnDel;
    private JCheckBox chkBLDGAgricultural;
    private JCheckBox chkBLDGAllowAirTrade;
    private JCheckBox chkBLDGAllowCityLevel2;
    private JCheckBox chkBLDGAllowCityLevel3;
    private JCheckBox chkBLDGAllowDiplomaticVictory;
    private JCheckBox chkBLDGAllowSpyMissions;
    private JCheckBox chkBLDGAllowWaterTrade;
    private JCheckBox chkBLDGAllowsHealingInEnemyTerritory;
    private JCheckBox chkBLDGAllowsNuclearWeapons;
    private JCheckBox chkBLDGBuildArmiesWithoutLeader;
    private JCheckBox chkBLDGBuildLargerArmies;
    private JCheckBox chkBLDGBuildSpaceshipParts;
    private JCheckBox chkBLDGCapitalization;
    private JCheckBox chkBLDGCenterOfEmpire;
    private JCheckBox chkBLDGCheaperUpgrades;
    private JCheckBox chkBLDGCoastalInstallation;
    private JCheckBox chkBLDGConstructionInstallation;
    private JCheckBox chkBLDGContinentalMoodEffects;
    private JCheckBox chkBLDGDecreasesSuccessOfMissiles;
    private JCheckBox chkBLDGDoubleCityDefences;
    private JCheckBox chkBLDGDoubleCityGrowth;
    private JCheckBox chkBLDGDoubleCombatVsBarbarians;
    private JCheckBox chkBLDGDoublesCityGrowthRate;
    private JCheckBox chkBLDGDoublesResearchOutput;
    private JCheckBox chkBLDGExplorationInstallation;
    private JCheckBox chkBLDGForbiddenPalace;
    private JCheckBox chkBLDGGainAnyTechsKnownByTwoCivs;
    private JCheckBox chkBLDGGoodsMustBeInCityRadius;
    private JCheckBox chkBLDGIncreasedArmyValue;
    private JCheckBox chkBLDGMarketplaceLuxEffect;
    private JCheckBox chkBLDGIncreasedResearch;
    private JCheckBox chkBLDGIncreasedShipMovement;
    private JCheckBox chkBLDGIncreasedTaxes;
    private JCheckBox chkBLDGIncreasedTrade;
    private JCheckBox chkBLDGIncreasesChanceOfLeaderAppearance;
    private JCheckBox chkBLDGIncreasesFoodInWater;
    private JCheckBox chkBLDGIncreasesLuxTax;
    private JCheckBox chkBLDGIncreasesShieldsInWater;
    private JCheckBox chkBLDGIncreasesTradeInWater;
    private JCheckBox chkBLDGMayExplodeOrMeltdown;
    private JCheckBox chkBLDGMilitaryInstallation;
    private JCheckBox chkBLDGMustBeNearRiver;
    private JCheckBox chkBLDGMustBeNearWater;
    private JCheckBox chkBLDGPaysTradeMaintenance;
    private JCheckBox chkBLDGPlaceOfWorship;
    private JCheckBox chkBLDGPlusTwoShipMovement;
    private JCheckBox chkBLDGReduceBldgPollution;
    private JCheckBox chkBLDGReduceWarWeariness;
    private JCheckBox chkBLDGReducesCorruption;
    private JCheckBox chkBLDGReducesWarWeariness;
    private JCheckBox chkBLDGRemovePopPollution;
    private JCheckBox chkBLDGReplacesOtherWithThisTag;
    private JCheckBox chkBLDGRequiresVictoriousArmy;
    private JCheckBox chkBLDGResearchInstallation;
    private JCheckBox chkBLDGResistantToBribery;
    private JCheckBox chkBLDGSafeSeaTravel;
    private JCheckBox chkBLDGSeafaring;
    private JCheckBox chkBLDGTouristAttraction;
    private JCheckBox chkBLDGTradeInstallation;
    private JCheckBox chkBLDGTreasuryEarnsInterest;
    private JCheckBox chkBLDGTwoFreeAdvances;
    private JCheckBox chkBLDGVeteranAirUnits;
    private JCheckBox chkBLDGVeteranSeaUnits;
    private JCheckBox chkBLDGVeteranUnits;
    private JCheckBox chkStealthBarrier;
    private JComboBox cmbBLDGDoublesHappiness;
    private JComboBox cmbBLDGGainInEveryCity;
    private JComboBox cmbBLDGGainOnContinent;
    public JComboBox cmbBLDGObsoleteBy;
    public JComboBox cmbBLDGReqAdvance;
    JComboBox cmbBLDGReqGovernment;
    private JComboBox cmbBLDGReqImprovement;
    JComboBox cmbBLDGReqResource1;
    JComboBox cmbBLDGReqResource2;
    public JComboBox cmbBLDGUnitProduced;
    private JCheckBox chkEliteShip;
    private JCheckBox chkDoublesSacrifice;
    private JLabel lblMaintenance;
    private JLabel lblCulture;
    private JLabel lblProductionBonus;
    private JLabel lblPollution;
    private JLabel lblTechnology;
    private JLabel lblGovernment;
    private JLabel lblBuilding;
    private JLabel lblNumBuildingsRequired;
    private JLabel jLabel18;
    private JLabel lblSeaBombard;
    private JLabel lblLandBombard;
    private JLabel lblAirAttack;
    private JLabel lblSeaAttack;
    private JLabel lblDefenceBonus;
    private JLabel lblNavalDefence;
    private JLabel jLabel25;
    private JLabel jLabel26;
    private JLabel jLabel27;
    private JLabel jLabel28;
    private JLabel jLabel29;
    private JLabel jLabel30;
    private JLabel jLabel31;
    private JLabel jLabel32;
    private JLabel jLabel33;
    private JLabel jLabel34;
    private JLabel lblObsoleteBy;
    private JLabel lblCivilopediaEntry;
    private JLabel lblCost;
    private JPanel pnlResourcesRequirements;
    private JPanel pnlMilitary;
    private JPanel jPanel12;
    private JPanel pnlTrade;
    private JPanel pnlReducesCorruption;
    private JPanel pnlHappiness;
    private JPanel jPanel16;
    private JPanel pnlFood;
    private JPanel jPanel18;
    private JPanel pnlScience;
    private JPanel pnlGain;
    private JPanel pnlOther;
    private JPanel pnlCharacteristics;
    private JPanel pnlFlavours;
    //private JPanel this;
    private JPanel pnlProperties;
    private JPanel pnlRequirements;
    private JScrollPane scrPaneBuildings;
    private JScrollPane jScrollPane2;
    JList lstBLDGFlavors;
    private SuperJList lstBuildings = new SuperJList(this, "building");
    private JRadioButton rbtnBLDGImprovement;
    private JRadioButton rbtnBLDGSmallWonder;
    private JRadioButton rbtnBLDGWonder;
    private SuperJTextField txtBLDGAirPower;
    private SuperJTextField txtBLDGArmiesRequired;
    private SuperJTextField txtBLDGBombardDefence;
    private SuperJTextField txtBLDGCivilopediaEntry;
    private SuperJTextField txtBLDGCost;
    private SuperJTextField txtBLDGCulture;
    private SuperJTextField txtBLDGDefenceBonus;
    private SuperJTextField txtBLDGHappy;
    private SuperJTextField txtBLDGHappyAll;
    private SuperJTextField txtBLDGMaintenanceCost;
    private SuperJTextField txtBLDGNavalBombardDefence;
    private SuperJTextField txtBLDGNavalDefenceBonus;
    private SuperJTextField txtBLDGNavalPower;
    private SuperJTextField txtBLDGNumReqBuildings;
    private SuperJTextField txtBLDGPollution;
    private SuperJTextField txtBLDGProduction;
    private SuperJTextField txtBLDGSpaceshipPart;
    private SuperJTextField txtBLDGUnhappy;
    private SuperJTextField txtBLDGUnhappyAll;
    private SuperJTextField txtBLDGUnitFrequency;
    private JCheckBox chkBLDGGeneralTelepad;
    private JCheckBox chkBLDGCharmBarrier;

    private void setupJavaFX() {
        bldgListPopup.getItems().addAll(menuAdd, menuRename, menuDelete);
        listView.setContextMenu(bldgListPopup);
        listView.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>(){
            @Override
            public void handle(ContextMenuEvent event) {
                bldgListPopup.show(listView, event.getScreenX(), event.getScreenY());
                event.consume();
            }
        });
        
        menuAdd.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e) {
                
                if (Main.safetyLevels.get("BLDG").ordinal() >= SafetyLevel.Minimal.ordinal()) {
                    if (buildings.size() >= 256) {
                        int confirmation = JOptionPane.showConfirmDialog(null, "<html>Your BIQ already has the maximum of 256 buildings.  Do you really want to add another?<br/><br/>"
                                + "Civ3 only supports 256 buildings, so adding more is not recommended.</html>", 
                                "Add more than 256 buildings?", 
                                JOptionPane.YES_NO_OPTION, 
                                JOptionPane.WARNING_MESSAGE);
                        if (confirmation == JOptionPane.NO_OPTION) {
                            return;
                        }
                    }
                }
                
                String name = utils.getItemName("Enter building name", "Building Name", "Enter building name:", "");
                
                if (name.length() > 0) {
                    addItem(name);
                }
            }
        });
        menuRename.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e) {
                BLDG bldg = listView.getSelectionModel().getSelectedItem();
                String name = utils.getItemName("Rename " + bldg.getName(), "Building Name", "Enter new name for " + bldg.getName() + ": ", bldg.getName());
                if (name.length() > 0) {
                    bldg.setName(name);
                    listView.refresh();
                }
            }
        });
        menuDelete.setOnAction( a -> {
            deleteAction();
        });        
    }
    
    /**
     * {@inheritDoc}
     */
    public void updateTab()
    {
        // :
        //building index is a global variable
        //will have the value of the old selection right now
        //use that old value to update the stored values
        if (logger.isDebugEnabled())
            logger.debug("Old index: " + buildingIndex);

        if (buildingIndex != -1)
        {
            storeData();
        }


        //now set it to the new value
        buildingIndex = lstBuildings.getSelectedIndex();
        buildingIndex = this.buildingList.getTrueIndex(buildingIndex);
        
        if (Main.settings.useJavaFX) {
            BLDG selectedItem = listView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                buildingIndex = listView.getSelectionModel().getSelectedItem().getIndex();
            }
            else {
                buildingIndex = -1;
            }
        }
        
        if (buildingIndex != -1)
        {
            loadData();
        }
    }
    
    /**
     * Stores data from the tab to memory.
     */
    private void storeData()
    {
        int hasWallBefore = buildings.get(buildingIndex).getBombardDefence();
        
        buildings.get(buildingIndex).setDoublesHappiness(cmbBLDGDoublesHappiness.getSelectedIndex() - 1);
        buildings.get(buildingIndex).setGainInEveryCity(cmbBLDGGainInEveryCity.getSelectedIndex() - 1);
        buildings.get(buildingIndex).setGainOnContinent(cmbBLDGGainOnContinent.getSelectedIndex() - 1);
        buildings.get(buildingIndex).setReqImprovement(cmbBLDGReqImprovement.getSelectedIndex() - 1);
        buildings.get(buildingIndex).setCost(txtBLDGCost.getInteger());
        buildings.get(buildingIndex).setCulture((txtBLDGCulture.getInteger()));
        buildings.get(buildingIndex).setBombardDefence((txtBLDGBombardDefence.getInteger()));
        buildings.get(buildingIndex).setNavalBombardDefence((txtBLDGNavalBombardDefence.getInteger()));
        buildings.get(buildingIndex).setDefenceBonus((txtBLDGDefenceBonus.getInteger()));
        buildings.get(buildingIndex).setNavalDefenceBonus((txtBLDGNavalDefenceBonus.getInteger()));
        buildings.get(buildingIndex).setMaintenanceCost((txtBLDGMaintenanceCost.getInteger()));
        buildings.get(buildingIndex).setHappyAll((txtBLDGHappyAll.getInteger()));
        buildings.get(buildingIndex).setHappy((txtBLDGHappy.getInteger()));
        buildings.get(buildingIndex).setUnhappyAll((txtBLDGUnhappyAll.getInteger()));
        buildings.get(buildingIndex).setUnhappy((txtBLDGUnhappy.getInteger()));
        buildings.get(buildingIndex).setNumReqBuildings((txtBLDGNumReqBuildings.getInteger()));
        buildings.get(buildingIndex).setAirPower((txtBLDGAirPower.getInteger()));
        buildings.get(buildingIndex).setNavalPower((txtBLDGNavalPower.getInteger()));
        buildings.get(buildingIndex).setPollution((txtBLDGPollution.getInteger()));
        buildings.get(buildingIndex).setProduction((txtBLDGProduction.getInteger()));
        buildings.get(buildingIndex).setReqGovernment(cmbBLDGReqGovernment.getSelectedIndex() - 1);
        int oldSSPart = buildings.get(buildingIndex).getSpaceshipPart();
        buildings.get(buildingIndex).setSpaceshipPart((txtBLDGSpaceshipPart.getInteger()));
        if (oldSSPart == -1 && buildings.get(buildingIndex).getSpaceshipPart() != -1) {
            //New spaceship part
            rule.get(0).addSpaceshipPart(1, false);
        }
        else if (oldSSPart != -1 && buildings.get(buildingIndex).getSpaceshipPart() == -1) {
            //removed SS part
            rule.get(0).removeSSPart(oldSSPart);
        }
        buildings.get(buildingIndex).setReqAdvance(cmbBLDGReqAdvance.getSelectedIndex() - 1);
        buildings.get(buildingIndex).setObsoleteBy(cmbBLDGObsoleteBy.getSelectedIndex() - 1);
        buildings.get(buildingIndex).setReqResource1(cmbBLDGReqResource1.getSelectedIndex() - 1);
        buildings.get(buildingIndex).setReqResource2(cmbBLDGReqResource2.getSelectedIndex() - 1);
        buildings.get(buildingIndex).setArmiesRequired((txtBLDGArmiesRequired.getInteger()));
        buildings.get(buildingIndex).setUnitProduced(cmbBLDGUnitProduced.getSelectedIndex() - 1);
        buildings.get(buildingIndex).setUnitFrequency((txtBLDGUnitFrequency.getInteger()));
        //begin improvements integer
        buildings.get(buildingIndex).setCenterOfEmpire(chkBLDGCenterOfEmpire.isSelected());
        buildings.get(buildingIndex).setVeteranUnits(chkBLDGVeteranUnits.isSelected());
        buildings.get(buildingIndex).setIncreasedResearch(chkBLDGIncreasedResearch.isSelected());
        buildings.get(buildingIndex).setIncreasedLuxuries(chkBLDGMarketplaceLuxEffect.isSelected());
        buildings.get(buildingIndex).setIncreasedTaxes(chkBLDGIncreasedTaxes.isSelected());
        buildings.get(buildingIndex).setRemovePopPollution(chkBLDGRemovePopPollution.isSelected());
        buildings.get(buildingIndex).setReduceBldgPollution(chkBLDGReduceBldgPollution.isSelected());
        buildings.get(buildingIndex).setResistantToBribery(chkBLDGResistantToBribery.isSelected());
        buildings.get(buildingIndex).setReducesCorruption(chkBLDGReducesCorruption.isSelected());
        buildings.get(buildingIndex).setDoublesCityGrowthRate(chkBLDGDoublesCityGrowthRate.isSelected());
        buildings.get(buildingIndex).setIncreasesLuxuryTrade(chkBLDGIncreasesLuxTax.isSelected());
        buildings.get(buildingIndex).setAllowCityLevel2(chkBLDGAllowCityLevel2.isSelected());
        buildings.get(buildingIndex).setAllowCityLevel3(chkBLDGAllowCityLevel3.isSelected());
        buildings.get(buildingIndex).setReplacesOtherWithThisTag(chkBLDGReplacesOtherWithThisTag.isSelected());
        buildings.get(buildingIndex).setMustBeNearWater(chkBLDGMustBeNearWater.isSelected());
        buildings.get(buildingIndex).setMustBeNearRiver(chkBLDGMustBeNearRiver.isSelected());
        buildings.get(buildingIndex).setMayExplodeOrMeltdown(chkBLDGMayExplodeOrMeltdown.isSelected());
        buildings.get(buildingIndex).setVeteranSeaUnits(chkBLDGVeteranSeaUnits.isSelected());
        buildings.get(buildingIndex).setVeteranAirUnits(chkBLDGVeteranAirUnits.isSelected());
        buildings.get(buildingIndex).setCapitalization(chkBLDGCapitalization.isSelected());
        buildings.get(buildingIndex).setAllowWaterTrade(chkBLDGAllowWaterTrade.isSelected());
        buildings.get(buildingIndex).setAllowAirTrade(chkBLDGAllowAirTrade.isSelected());
        buildings.get(buildingIndex).setReducesWarWeariness(chkBLDGReducesWarWeariness.isSelected());
        buildings.get(buildingIndex).setIncreasesShieldsInWater(chkBLDGIncreasesShieldsInWater.isSelected());
        buildings.get(buildingIndex).setIncreasesFoodInWater(chkBLDGIncreasesFoodInWater.isSelected());
        buildings.get(buildingIndex).setIncreasesTradeInWater(chkBLDGIncreasesTradeInWater.isSelected());
        buildings.get(buildingIndex).setCharmBarrier(chkBLDGCharmBarrier.isSelected());
        buildings.get(buildingIndex).setStealthAttackBarrier(chkStealthBarrier.isSelected());
        buildings.get(buildingIndex).setActsAsGeneralTelepad(chkBLDGGeneralTelepad.isSelected());
        buildings.get(buildingIndex).setDoublesSacrifice(chkDoublesSacrifice.isSelected());
        if (cmbBLDGUnitProduced.getSelectedIndex() != 0)
            buildings.get(buildingIndex).setCanBuildUnits(true);
        else
            buildings.get(buildingIndex).setCanBuildUnits(false);
        //end improvements integer
        buildings.get(buildingIndex).setCoastalInstallation(chkBLDGCoastalInstallation.isSelected());
        buildings.get(buildingIndex).setMilitaryInstallation(chkBLDGMilitaryInstallation.isSelected());
        //if it is an Improvement, neither Wonder nor SmallWonder is true
        buildings.get(buildingIndex).setWonder(rbtnBLDGWonder.isSelected());
        buildings.get(buildingIndex).setSmallWonder(rbtnBLDGSmallWonder.isSelected());
        buildings.get(buildingIndex).setContinentalMoodEffects(chkBLDGContinentalMoodEffects.isSelected());
        buildings.get(buildingIndex).setResearchInstallation(chkBLDGResearchInstallation.isSelected());
        buildings.get(buildingIndex).setTradeInstallation(chkBLDGTradeInstallation.isSelected());
        buildings.get(buildingIndex).setExplorationInstallation(chkBLDGExplorationInstallation.isSelected());
        buildings.get(buildingIndex).setPlaceOfWorship(chkBLDGPlaceOfWorship.isSelected());
        buildings.get(buildingIndex).setConstructionInstallation(chkBLDGConstructionInstallation.isSelected());
        buildings.get(buildingIndex).setAgriculturalInstallation(chkBLDGAgricultural.isSelected());
        buildings.get(buildingIndex).setSeafaringInstallation(chkBLDGSeafaring.isSelected());
        buildings.get(buildingIndex).setIncreasesChanceOfLeaderAppearance(chkBLDGIncreasesChanceOfLeaderAppearance.isSelected());
        buildings.get(buildingIndex).setBuildArmiesWithoutLeader(chkBLDGBuildArmiesWithoutLeader.isSelected());
        buildings.get(buildingIndex).setBuildLargerArmies(chkBLDGBuildLargerArmies.isSelected());
        buildings.get(buildingIndex).setTreasuryEarnsInterest(chkBLDGTreasuryEarnsInterest.isSelected());
        buildings.get(buildingIndex).setBuildSpaceshipParts(chkBLDGBuildSpaceshipParts.isSelected());
        buildings.get(buildingIndex).setForbiddenPalace(chkBLDGForbiddenPalace.isSelected());
        buildings.get(buildingIndex).setDecreasesSuccessOfMissiles(chkBLDGDecreasesSuccessOfMissiles.isSelected());
        buildings.get(buildingIndex).setAllowSpyMissions(chkBLDGAllowSpyMissions.isSelected());
        buildings.get(buildingIndex).setAllowsHealingInEnemyTerritory(chkBLDGAllowsHealingInEnemyTerritory.isSelected());
        buildings.get(buildingIndex).setGoodsMustBeInCityRadius(chkBLDGGoodsMustBeInCityRadius.isSelected());
        buildings.get(buildingIndex).setRequiresVictoriousArmy(chkBLDGRequiresVictoriousArmy.isSelected());
        buildings.get(buildingIndex).setRequiresEliteShip(chkEliteShip.isSelected());
        //end small wonder
        buildings.get(buildingIndex).setSafeSeaTravel(chkBLDGSafeSeaTravel.isSelected());
        buildings.get(buildingIndex).setGainAnyTechsKnownByTwoCivs(chkBLDGGainAnyTechsKnownByTwoCivs.isSelected());
        buildings.get(buildingIndex).setDoubleCombatVsBarbarians(chkBLDGDoubleCombatVsBarbarians.isSelected());
        buildings.get(buildingIndex).setIncreasedShipMovement(chkBLDGIncreasedShipMovement.isSelected());
        buildings.get(buildingIndex).setDoublesResearchOutput(chkBLDGDoublesResearchOutput.isSelected());
        buildings.get(buildingIndex).setIncreasedTrade(chkBLDGIncreasedTrade.isSelected());
        buildings.get(buildingIndex).setCheaperUpgrades(chkBLDGCheaperUpgrades.isSelected());
        buildings.get(buildingIndex).setPaysTradeMaintenance(chkBLDGPaysTradeMaintenance.isSelected());
        buildings.get(buildingIndex).setAllowsNuclearWeapons(chkBLDGAllowsNuclearWeapons.isSelected());
        buildings.get(buildingIndex).setDoubleCityGrowth(chkBLDGDoubleCityGrowth.isSelected());
        buildings.get(buildingIndex).setTwoFreeAdvances(chkBLDGTwoFreeAdvances.isSelected());
        buildings.get(buildingIndex).setReduceWarWeariness(chkBLDGReduceWarWeariness.isSelected());
        buildings.get(buildingIndex).setDoubleCityDefences(chkBLDGDoubleCityDefences.isSelected());
        buildings.get(buildingIndex).setAllowDiplomaticVictory(chkBLDGAllowDiplomaticVictory.isSelected());
        buildings.get(buildingIndex).setTouristAttraction(chkBLDGTouristAttraction.isSelected());
        buildings.get(buildingIndex).setIncreasedArmyValue(chkBLDGIncreasedArmyValue.isSelected());
        //buildings.get(buildingIndex).setQuestionMarkWonderTrait(chkBLDGQuestionMarkWonderTrait.isSelected());
            //MISSING The above does not exist, as its function is unknown
        buildings.get(buildingIndex).setPlusTwoShipMovement(chkBLDGPlusTwoShipMovement.isSelected());
        //buildings.get(buildingIndex).setDescription(txtBLDGDescription.getText());
            //MISSING The above does not exist,  as it is an unused property
        //buildings.get(buildingIndex).setName(txtBLDGName.getText());
            //TO BE ADDED: Ability to change name of building in browser
        buildings.get(buildingIndex).setCivilopediaEntry(txtBLDGCivilopediaEntry.getText());

        if (cmbBLDGUnitProduced.getSelectedIndex() != 0)
        {
            buildings.get(buildingIndex).setCanBuildUnits(true);
        }
        else
        {
            buildings.get(buildingIndex).setCanBuildUnits(false);
        }
        //flavours
        int[]selectedFlavs = lstBLDGFlavors.getSelectedIndices();
        for (int i = 0; i < buildings.get(buildingIndex).getNumFlavors(); i++)
            buildings.get(buildingIndex).setFlavour(i, false);
        for (int i = 0; i < selectedFlavs.length; i++)
            buildings.get(buildingIndex).setFlavour(selectedFlavs[i], true);
        //end backing up of data
        
        int hasWallNow = buildings.get(buildingIndex).getBombardDefence();
        if (hasWallNow != hasWallBefore) {
            for (CITY city : baselink.city) {
                city.updateWallStyleBuildingStatus();
            }
        }
    }
    
    /**
     * Loads data from RAM into the tab.
     */
    private void loadData()
    {
        BLDG newBuilding = buildings.get(buildingIndex);    //Why I didn't add this years ago, I don't know
        if (logger.isDebugEnabled())
            logger.debug("new building selected: " + buildingIndex);
        txtBLDGCivilopediaEntry.setText(newBuilding.getCivilopediaEntry());
        txtBLDGCost.setText(Integer.toString(newBuilding.getCost()));
        txtBLDGMaintenanceCost.setText(Integer.toString(newBuilding.getMaintenanceCost()));
        txtBLDGCulture.setText(Integer.toString(newBuilding.getCulture()));
        txtBLDGProduction.setText(Integer.toString(newBuilding.getProduction()));
        txtBLDGPollution.setText(Integer.toString(newBuilding.getPollution()));
        if (newBuilding.isWonder())
        {
            rbtnBLDGWonder.setSelected(true);
        }
        if (newBuilding.isSmallWonder())
        {
            rbtnBLDGSmallWonder.setSelected(true);
        }
        else
        {
            rbtnBLDGImprovement.setSelected(true);
        }
        //military section
        txtBLDGBombardDefence.setText(Integer.toString(newBuilding.getBombardDefence()));
        txtBLDGNavalBombardDefence.setText(Integer.toString(newBuilding.getNavalBombardDefence()));
        txtBLDGAirPower.setText(Integer.toString(newBuilding.getAirPower()));
        txtBLDGNavalPower.setText(Integer.toString(newBuilding.getNavalPower()));
        txtBLDGDefenceBonus.setText(Integer.toString(newBuilding.getDefenceBonus()));
        txtBLDGNavalDefenceBonus.setText(Integer.toString(newBuilding.getNavalDefenceBonus()));
        //ADJ 06/12 Use the new, one-line version instead of the 8-line version for booleans
        chkBLDGVeteranAirUnits.setSelected(newBuilding.getVeteranAirUnits());
        chkBLDGVeteranUnits.setSelected(newBuilding.getVeteranUnits());
        chkBLDGVeteranSeaUnits.setSelected(newBuilding.getVeteranSeaUnits());
        //Stealth - done elsewhere
        chkBLDGAllowsNuclearWeapons.setSelected(newBuilding.getAllowsNuclearWeapons());
        chkBLDGDecreasesSuccessOfMissiles.setSelected(newBuilding.getDecreasesSuccessOfMissiles());
        chkBLDGDoubleCombatVsBarbarians.setSelected(newBuilding.getDoubleCombatVsBarbarians());
        chkBLDGBuildArmiesWithoutLeader.setSelected(newBuilding.getBuildArmiesWithoutLeader());
        chkBLDGBuildLargerArmies.setSelected(newBuilding.getBuildLargerArmies());
        chkBLDGIncreasesChanceOfLeaderAppearance.setSelected(newBuilding.getIncreasesChanceOfLeaderAppearance());
        chkBLDGSafeSeaTravel.setSelected(newBuilding.getSafeSeaTravel());
        chkBLDGIncreasedShipMovement.setSelected(newBuilding.getIncreasedShipMovement());
        chkBLDGCheaperUpgrades.setSelected(newBuilding.getCheaperUpgrades());
        chkBLDGAllowsHealingInEnemyTerritory.setSelected(newBuilding.getAllowsHealingInEnemyTerritory());
        chkBLDGDoubleCityDefences.setSelected(newBuilding.getDoubleCityDefences());
        //end military section
        cmbBLDGReqImprovement.setSelectedIndex(newBuilding.getReqImprovement() + 1);

        //attempt to do a whole bunch of this with auto-generated code
        cmbBLDGDoublesHappiness.setSelectedIndex(newBuilding.getDoublesHappiness() + 1);
        cmbBLDGGainInEveryCity.setSelectedIndex(newBuilding.getGainInEveryCity() + 1);
        cmbBLDGGainOnContinent.setSelectedIndex(newBuilding.getGainOnContinent() + 1);
        cmbBLDGReqImprovement.setSelectedIndex(newBuilding.getReqImprovement() + 1);
        txtBLDGCost.setText(Integer.toString(newBuilding.getCost()));
        txtBLDGCulture.setText(Integer.toString(newBuilding.getCulture()));
        txtBLDGBombardDefence.setText(Integer.toString(newBuilding.getBombardDefence()));
        txtBLDGNavalBombardDefence.setText(Integer.toString(newBuilding.getNavalBombardDefence()));
        txtBLDGDefenceBonus.setText(Integer.toString(newBuilding.getDefenceBonus()));
        txtBLDGNavalDefenceBonus.setText(Integer.toString(newBuilding.getNavalDefenceBonus()));
        txtBLDGMaintenanceCost.setText(Integer.toString(newBuilding.getMaintenanceCost()));
        txtBLDGHappyAll.setText(Integer.toString(newBuilding.getHappyAll()));
        txtBLDGHappy.setText(Integer.toString(newBuilding.getHappy()));
        txtBLDGUnhappyAll.setText(Integer.toString(newBuilding.getUnhappyAll()));
        txtBLDGUnhappy.setText(Integer.toString(newBuilding.getUnhappy()));
        txtBLDGNumReqBuildings.setText(Integer.toString(newBuilding.getNumReqBuildings()));
        txtBLDGAirPower.setText(Integer.toString(newBuilding.getAirPower()));
        txtBLDGNavalPower.setText(Integer.toString(newBuilding.getNavalPower()));
        txtBLDGPollution.setText(Integer.toString(newBuilding.getPollution()));
        txtBLDGProduction.setText(Integer.toString(newBuilding.getProduction()));
        cmbBLDGReqGovernment.setSelectedIndex(newBuilding.getReqGovernment() + 1);
        txtBLDGSpaceshipPart.setText(Integer.toString(newBuilding.getSpaceshipPart()));
        cmbBLDGReqAdvance.setSelectedIndex(newBuilding.getReqAdvance() + 1);
        cmbBLDGObsoleteBy.setSelectedIndex(newBuilding.getObsoleteBy() + 1);
        cmbBLDGReqResource1.setSelectedIndex(newBuilding.getReqResource1() + 1);
        cmbBLDGReqResource2.setSelectedIndex(newBuilding.getReqResource2() + 1);
        txtBLDGArmiesRequired.setText(Integer.toString(newBuilding.getArmiesRequired()));
        cmbBLDGUnitProduced.setSelectedIndex(newBuilding.getUnitProduced() + 1);
        txtBLDGUnitFrequency.setText(Integer.toString(newBuilding.getUnitFrequency()));

        chkBLDGCenterOfEmpire.setSelected(newBuilding.getCenterOfEmpire());
        //6/12 Deleted a duplicate veteran (land) units set (and later duplicate sea/air, too)
        chkBLDGIncreasedResearch.setSelected(newBuilding.getIncreasedResearch());
        chkBLDGMarketplaceLuxEffect.setSelected(newBuilding.getIncreasedLuxuries());
        chkBLDGIncreasedTaxes.setSelected(newBuilding.getIncreasedTaxes());
        chkBLDGRemovePopPollution.setSelected(newBuilding.getRemovePopPollution());
        chkBLDGReduceBldgPollution.setSelected(newBuilding.getReduceBldgPollution());
        chkBLDGResistantToBribery.setSelected(newBuilding.getResistantToBribery());
        chkBLDGReducesCorruption.setSelected(newBuilding.getReducesCorruption());
        chkBLDGDoublesCityGrowthRate.setSelected(newBuilding.getDoublesCityGrowthRate());
        chkBLDGIncreasesLuxTax.setSelected(newBuilding.getIncreasesLuxuryTrade());
        chkBLDGAllowCityLevel2.setSelected(newBuilding.getAllowCityLevel2());
        chkBLDGAllowCityLevel3.setSelected(newBuilding.getAllowCityLevel3());
        chkBLDGReplacesOtherWithThisTag.setSelected(newBuilding.getReplacesOtherWithThisTag());
        chkBLDGMustBeNearWater.setSelected(newBuilding.getMustBeNearWater());
        chkBLDGMustBeNearRiver.setSelected(newBuilding.getMustBeNearRiver());
        chkBLDGMayExplodeOrMeltdown.setSelected(newBuilding.getMayExplodeOrMeltdown());
        chkBLDGCapitalization.setSelected(newBuilding.getCapitalization());
        chkBLDGAllowWaterTrade.setSelected(newBuilding.getAllowWaterTrade());
        chkBLDGAllowAirTrade.setSelected(newBuilding.getAllowAirTrade());
        chkBLDGReducesWarWeariness.setSelected(newBuilding.getReducesWarWeariness());
        chkBLDGIncreasesShieldsInWater.setSelected(newBuilding.getIncreasesShieldsInWater());
        chkBLDGIncreasesFoodInWater.setSelected(newBuilding.getIncreasesFoodInWater());
        //The below added slightly later.
        chkBLDGIncreasesTradeInWater.setSelected(newBuilding.getIncreasesTradeInWater());
        chkBLDGCharmBarrier.setSelected(newBuilding.getCharmBarrier());
        chkStealthBarrier.setSelected(newBuilding.getStealthAttackBarrier());
        chkBLDGGeneralTelepad.setSelected(newBuilding.getActsAsGeneralTelepad());
        chkDoublesSacrifice.setSelected(newBuilding.getDoublesSacrifice());
        //no check box for "can build units"
        //end improvements bytes
        chkBLDGCoastalInstallation.setSelected(newBuilding.getCoastalInstallation());
        chkBLDGMilitaryInstallation.setSelected(newBuilding.getMilitaryInstallation());
        rbtnBLDGWonder.setSelected(newBuilding.isWonder());
        //Wonders set above, don't need to again down here
        chkBLDGContinentalMoodEffects.setSelected(newBuilding.getContinentalMoodEffects());
        chkBLDGResearchInstallation.setSelected(newBuilding.getResearchInstallation());
        chkBLDGTradeInstallation.setSelected(newBuilding.getTradeInstallation());
        chkBLDGExplorationInstallation.setSelected(newBuilding.getExplorationInstallation());
        chkBLDGPlaceOfWorship.setSelected(newBuilding.getPlaceOfWorship());
        chkBLDGConstructionInstallation.setSelected(newBuilding.getConstructionInstallation());
        chkBLDGAgricultural.setSelected(newBuilding.getAgriculturalInstallation());
        chkBLDGSeafaring.setSelected(newBuilding.getSeafaringInstallation());
        
        //duplicate: increases leader chance, armies w/o leader, larger armies
        chkBLDGTreasuryEarnsInterest.setSelected(newBuilding.getTreasuryEarnsInterest());
        chkBLDGBuildSpaceshipParts.setSelected(newBuilding.getBuildSpaceshipParts());
        chkBLDGForbiddenPalace.setSelected(newBuilding.getForbiddenPalace());
        chkBLDGDecreasesSuccessOfMissiles.setSelected(newBuilding.getDecreasesSuccessOfMissiles());
        chkBLDGAllowSpyMissions.setSelected(newBuilding.getAllowSpyMissions());
        //duplicate healing in enemy territory, double vs barbs, safe sea, increased ship move, cheaper upgrades
        chkBLDGGoodsMustBeInCityRadius.setSelected(newBuilding.getGoodsMustBeInCityRadius());
        chkBLDGRequiresVictoriousArmy.setSelected(newBuilding.getRequiresVictoriousArmy());
        chkEliteShip.setSelected(newBuilding.requiresEliteShip());
        chkBLDGGainAnyTechsKnownByTwoCivs.setSelected(newBuilding.getGainAnyTechsKnownByTwoCivs());
        chkBLDGDoublesResearchOutput.setSelected(newBuilding.getDoublesResearchOutput());
        chkBLDGIncreasedTrade.setSelected(newBuilding.getIncreasedTrade());
        chkBLDGPaysTradeMaintenance.setSelected(newBuilding.getPaysTradeMaintenance());
        chkBLDGAllowsNuclearWeapons.setSelected(newBuilding.getAllowsNuclearWeapons());
        chkBLDGDoubleCityGrowth.setSelected(newBuilding.getDoubleCityGrowth());
        chkBLDGTwoFreeAdvances.setSelected(newBuilding.getTwoFreeAdvances());
        chkBLDGReduceWarWeariness.setSelected(newBuilding.getReducesWarWearinessEmpire());
        //duplicate double city defence
        chkBLDGAllowDiplomaticVictory.setSelected(newBuilding.getAllowDiplomaticVictory());
        chkBLDGTouristAttraction.setSelected(newBuilding.getTouristAttraction());
        chkBLDGIncreasedArmyValue.setSelected(newBuilding.getIncreasedArmyValue());
        if (newBuilding.getQuestionMarkWonderTrait())
        {
            //TO BE ADDED
            //chkBLDGQuestionMarkWonderTrait.setSelected(true);
        }
        else
        {
            //TO BE ADDED
            //chkBLDGQuestionMarkWonderTrait.setSelected(false);
        }
        chkBLDGPlusTwoShipMovement.setSelected(newBuilding.getPlusTwoShipMovement());
        //display flavour data
        char numTrue = 0;
        for (int i = 0; i < newBuilding.getNumFlavors(); i++)
            if (newBuilding.getFlavour(i))
                numTrue++;
        int[]selInd = new int[numTrue];
        int index = 0;
        for (int i = 0; i < newBuilding.getNumFlavors(); i++)
            if (newBuilding.getFlavour(i))
            {
                selInd[index] = i;
                index++;
            }
        lstBLDGFlavors.setSelectedIndices(selInd);
    }
    
    
    private void initializeBldgListFX() {
        
        QueryList<BLDG> queryList = new QueryList<>(bldgListFX);
        queryList.setPredicate(PredicateFactory.createBLDGFilter());
        TextField filter = new TextField();
        filter.textProperty().addListener(obs -> {
            long startL = System.nanoTime();
            queryList.updateSearchQuery(filter.getText());
            long endL = System.nanoTime();
            if (false) {
                logger.info("Time to update search query nanoTime: " + (endL - startL)/1000 + " microseconds");
            }
        });
        
        listView.setCellFactory(new Callback<ListView<BLDG>, ListCell<BLDG>>() {
            @Override
            public ListCell<BLDG> call(ListView<BLDG> param) {
                ListCell<BLDG> cell = new ListCell<BLDG>() {
                    @Override
                    protected void updateItem(BLDG tech, boolean empty) {
                        super.updateItem(tech, empty);
                        if (tech != null) {
                            //System.out.println("Updating item " + tech.getName());
                            setText(tech.getName());
                        }
                        else {
                            setText("");
                        }
                    }
                };
                
                //Removed reorder functionality as it is not ready yet.
                
                return cell;
            }
        });
                
        listView.setItems(queryList);
              
        listView.getSelectionModel().selectedItemProperty().addListener(
            new javafx.beans.value.ChangeListener<BLDG>() {
                public void changed(ObservableValue<? extends BLDG> ov, BLDG oldVal, BLDG newVal) {
                    updateTab();
            }
        });
        
        VBox.setVgrow(listView, Priority.ALWAYS);
        VBox vbox = new VBox(filter, listView);
        
        Scene scene = new Scene(vbox, 170, 670);
        //Odd context menu border/size bug if we add a CSS file to the whole scene
        //So just apply it to the list view since that's all that really needs it.
        listView.getStylesheets().add("styles/styles.css");
        
        jFXPanel.setScene(scene);
    }

    private void lstBuildingsValueChanged(javax.swing.event.ListSelectionEvent evt) {
        updateTab();
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
        //do set document listeners on the char array fields (civilopedia and the like)
        addLengthDocumentListener(31, txtBLDGCivilopediaEntry);
        addBadValueDocumentListener(Integer.MAX_VALUE, -1, txtBLDGSpaceshipPart);
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
        //Max Firaxis value = 1000
        addBadValueDocumentListener(1000, txtBLDGCost);
        addBadValueDocumentListener(1000, txtBLDGAirPower);
        addBadValueDocumentListener(1000, txtBLDGNavalPower);
        addBadValueDocumentListener(1000, txtBLDGNavalBombardDefence);
        addBadValueDocumentListener(1000, txtBLDGBombardDefence);
        addBadValueDocumentListener(1000, txtBLDGDefenceBonus);
        //Max Firaxis value = 255
        addBadValueDocumentListener(255, txtBLDGHappy);
        addBadValueDocumentListener(255, txtBLDGHappyAll);
        addBadValueDocumentListener(255, txtBLDGUnhappy);
        addBadValueDocumentListener(255, txtBLDGUnhappyAll);
        //Max Firaxis value = 100
        addBadValueDocumentListener(100, -100, txtBLDGCulture);
        addBadValueDocumentListener(100, txtBLDGProduction);
        addBadValueDocumentListener(100, txtBLDGMaintenanceCost);
        addBadValueDocumentListener(100, -100, txtBLDGPollution);
        addBadValueDocumentListener(100, 1, txtBLDGUnitFrequency);
        //Max Firaxis value = 50
        addBadValueDocumentListener(50, txtBLDGArmiesRequired);
        //Max Firaxis value = 25
        addBadValueDocumentListener(25, txtBLDGNumReqBuildings);
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
            logger.debug("SETTING TOTAL LIMITS");
        //don't allow any issues to occur, even if Firaxis would have
        //usually this will be the same as the Firaxian settings
        //this call will take care of minimal limits
        setFiraxisLimits();
    }
    
    /**
     * Handles all the input.
     * This should be done by the JList instead.
     * This is kept on the tab solely so the Add button still works, and will
     * be removed when that button is removed.
     * @deprecated 
     */
    private void addBuildingAction()
    {
        
        String name = JOptionPane.showInputDialog("Please choose a name for the new building");
        if (name == null)
        {
            //buildingList.toggle();
            return;
        }
        buildings.add(new BLDG(name, lstBLDGFlavors.getModel().getSize(), baselink));
        buildingList.addIndexedElement(name);
        utils.addWithPossibleDuplicates(name, mdlReqImprovement);
        mapTab.buildingsModel.addElement(name);

        lstBuildings.setSelectedIndex(buildingList.size() - 1);
    }
    
    public void deleteAction()
    {
        if (Main.GRAPHICS_ENABLED && baselink.hasCustomMap() && !Main.mapIsLoaded)
        {
            JOptionPane.showMessageDialog(null, "Please wait for the map tab to finish loading before deleting items");
            return;
        }
        
        int deletedBuildingIndex = -1;
        if (!Main.settings.useJavaFX) {
            deletedBuildingIndex = lstBuildings.getSelectedIndex();
            deletedBuildingIndex = this.buildingList.getTrueIndex(deletedBuildingIndex);
        }
        else {
            deletedBuildingIndex = listView.getSelectionModel().getSelectedItem().getIndex();
            bldgListFX.remove(listView.getSelectionModel().getSelectedItem());
        }
        //JOptionPane.showMessageDialog(null, index);
        //JOptionPane.showMessageDialog(null, "Deleting the " + buildings.get(index).name);
        
        //Deleting buildings while map open was fixed on May 13, 2011
        if (baselink.hasCustomMap())
        {
            if (logger.isDebugEnabled())
                logger.debug("Index to be deleted: " + deletedBuildingIndex);
            //Update the backend FIRST
            //Else the GUI update will trigger the backend update on only the city
            //that is selected (if any), and will mess up the selected buildings after the to-be-deleted one
            //in that city.
            for (int i = 0; i < baselink.city.size(); i++)
            {
                baselink.city.get(i).handleDeletedBuilding(deletedBuildingIndex);
            }
            //And the GUI
            //June 6, 2012 - If the map GUI isn't showing, don't update that part.
            if (Main.GRAPHICS_ENABLED)
            {
                int[]selected = mapTab.lstBuildings.getSelectedIndices();
                mapTab.buildingsModel.remove(deletedBuildingIndex);
                //else the deletion will do the update for us
                //check if we need to update the gooey
                boolean needToUpdateGUI = false;
                for (int i = 0; i < selected.length; i++)
                {
                    if (selected[i] == deletedBuildingIndex)
                    {
                        needToUpdateGUI = true;
                        break;
                    }
                }
                if (needToUpdateGUI)
                {
                    int[]newSelected = new int[selected.length - 1];
                    int curIndex = 0;
                    for (int i = 0; i < selected.length; i++)
                    {
                        if (selected[i] < deletedBuildingIndex)
                        {
                            newSelected[curIndex] = selected[i];
                            curIndex++;
                        }
                        else if (selected[i] > deletedBuildingIndex)
                        {
                            newSelected[curIndex] = selected[i] - 1;
                            curIndex++;
                        }
                        //else it equals the deleted item, don't add it
                    }
                    mapTab.lstBuildings.setSelectedIndices(newSelected);
                }
            }
        }
        //remove all dependencies
        for (int i = 0; i < buildings.size(); i++)
        {
            buildings.get(i).handleDeletedBuilding(deletedBuildingIndex);
        }
        //legal building telepads
        unitTab.buildingList.remove(deletedBuildingIndex);
        //Also remove it from the required improvement list
        //Thankfully we can use the index, rather than the string, to remove it
        mdlReqImprovement.removeElementAt(deletedBuildingIndex + 1);   //+1 because of the 'none' option
        //so lstBuildings doesn't copy over the deleted values to the next building
        buildingIndex = -1;
        utils.removeFromList(deletedBuildingIndex, buildings, buildingList, lstBuildings);
    }
    
    
    public boolean addItem(String name)
    {
        buildings.add(new BLDG(name, lstBLDGFlavors.getModel().getSize(), baselink));
        if (!Main.settings.useJavaFX) {
            utils.addWithPossibleDuplicates(name, mdlReqImprovement);
        }
        else {
            bldgListFX.add(buildings.get(buildings.size() - 1));
        }
        mapTab.buildingsModel.addElement(name);
        return true;
    }
    
    @Override
    public boolean checkBounds(List<String>invalidValues)
    {
        if (buildingIndex == -1) {
            return true;
        }
        return super.checkBounds(invalidValues);
    }
    
    public void renameBIQElement(int index, String name)
    {
        buildings.get(index).setName(name);
    }
}
