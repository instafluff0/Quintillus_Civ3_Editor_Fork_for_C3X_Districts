package com.civfanatics.civ3.xplatformeditor;

/**
 *
 * @author Quintillus
 */
import com.civfanatics.civ3.biqFile.BLDG;
import com.civfanatics.civ3.biqFile.GAME;
import com.civfanatics.civ3.biqFile.GOOD;
import com.civfanatics.civ3.biqFile.LEAD;
import com.civfanatics.civ3.biqFile.PRTO;
import com.civfanatics.civ3.biqFile.RACE;
import com.civfanatics.civ3.biqFile.RULE;
import com.civfanatics.civ3.biqFile.TECH;
import com.civfanatics.civ3.biqFile.TERR;
import com.civfanatics.civ3.biqFile.UNIT;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.QueryList;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJList;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJTextField;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperListModel;
import com.civfanatics.civ3.xplatformeditor.specialty.PredicateFactory;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.embed.swing.JFXPanel;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.swing.*;
import javax.swing.event.ChangeEvent;

public class UnitTab extends EditorTab implements java.awt.event.ComponentListener {
    BufferedImage[][] unitIcons;
    BufferedImage currentIcon;
    BufferedImage units32;
    public void componentHidden(ComponentEvent e) {
        if (logger.isDebugEnabled())
            logger.debug(e.getComponent().getClass().getName() + " --- Hidden");
    }

    public void componentMoved(ComponentEvent e) {
        if (logger.isDebugEnabled())
            logger.debug(e.getComponent().getClass().getName() + " --- Moved");
    }

    public void componentResized(ComponentEvent e) {
        if (logger.isDebugEnabled())
            logger.debug(e.getComponent().getClass().getName() + " --- Resized ");
    }

    public void componentShown(ComponentEvent e) {
        if (logger.isDebugEnabled())
            logger.debug(e.getComponent().getClass().getName() + " --- Shown");
        repaintIcon();
    }

    boolean tabCreated;
    public boolean tabActive = false;
    public List<TECH>technology;
    public List<PRTO>unit;
    public List<TERR>terrain;
    public List<GOOD>resource;
    public List<RACE>civilization;
    public List<BLDG>building;
    int unitIndex;
    JMenuItem delete;
    JMenuItem add;

    BldgTab bldgTab;
    RULETab ruleTab;
    CIVTab civTab;
    MapTab mapTab;
    PLYRTab playerTab;
    GAMETab gameTab;
    boolean unit32Loaded;
    java.awt.image.BufferedImage settlerImage;

    private ButtonGroup unitClass;
    private SuperListModel unitList;
    public DefaultListModel civilizationList;
    public DefaultListModel buildingList;
    private DefaultListModel terrainList;
    private JCheckBox chkPRTOAirBombard;
    private JCheckBox chkPRTOAirDefenceStrategy;
    private JCheckBox chkPRTOAirTransport;
    private JCheckBox chkPRTOAirdrop;
    private JCheckBox chkPRTOAirlift;
    private JCheckBox chkPRTOArmyUnit;
    private JCheckBox chkPRTOArtillery;
    private JCheckBox chkPRTOAutomate;
    private JCheckBox chkPRTOBomb;
    private JCheckBox chkPRTOBombard;
    private JCheckBox chkPRTOBuildArmy;
    private JCheckBox chkPRTOBuildCity;
    private JCheckBox chkPRTOBuildColony;
    private JCheckBox chkPRTOBuildFort;
    private JCheckBox chkPRTOBuildMine;
    private JCheckBox chkPRTOBuildRailroad;
    private JCheckBox chkPRTOBuildRoad;
    private JCheckBox chkPRTOClearForest;
    private JCheckBox chkPRTOClearJungle;
    private JCheckBox chkPRTOClearPollution;
    private JCheckBox chkPRTOCruiseMissileUnit;
    private JCheckBox chkPRTODefenceStrategy;
    private JCheckBox chkPRTODisband;
    private JCheckBox chkPRTOExploreStrategy;
    private JCheckBox chkPRTOFinishImprovement;
    private JCheckBox chkPRTOTeleportable;
    private JCheckBox chkPRTOTelepad;
    private JCheckBox chkPRTOCharm = new JCheckBox();
    private JCheckBox chkPRTOFortify;
    private JCheckBox chkPRTOGoTo;
    private JCheckBox chkPRTOICBM;
    private JCheckBox chkPRTOIntercept;
    private JCheckBox chkPRTOIrrigate;
    private JCheckBox chkPRTOJoinCity;
    private JCheckBox chkPRTOLeaderUnit;
    private JCheckBox chkPRTOLoad;
    private JCheckBox chkPRTONavalCarrier;
    private JCheckBox chkPRTONavalMissileTransport;
    private JCheckBox chkPRTONavalPower;
    private JCheckBox chkPRTONavalTransport;
    private JCheckBox chkPRTOOffence;
    private JCheckBox chkPRTOPillage;
    private JCheckBox chkPRTOPlantForest;
    private JCheckBox chkPRTOPrecisionBomb;
    private JCheckBox chkPRTORebase;
    private JCheckBox chkPRTORecon;
    private JCheckBox chkPRTORequiresSupport;
    private JCheckBox chkPRTOSettle;
    private JCheckBox chkPRTOSkipTurn;
    private JCheckBox chkPRTOTacticalNuke;
    private JCheckBox chkPRTOTerraform;
    private JCheckBox chkPRTOUnload;
    private JCheckBox chkPRTOUpgrade;
    private JCheckBox chkPRTOWait;
    private JCheckBox chkPRTOZoneOfControl;
    private JComboBox cmbPRTOEnslaveResultsIn;
    JComboBox cmbPRTORequiredResource1;
    JComboBox cmbPRTORequiredResource2;
    JComboBox cmbPRTORequiredResource3;
    //Maps entries in upgradeTo/enslave lists to unit indices
    Map<Integer, PRTO> upgradeToIndexMap = new HashMap<Integer, PRTO>();
    DefaultComboBoxModel mdlUpgradeTo= new DefaultComboBoxModel();
    DefaultComboBoxModel mdlPrerequisite = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlRequiredResource1 = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlRequiredResource2 = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlRequiredResource3 = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlEnslaveResultsIn = new DefaultComboBoxModel();
    public JComboBox cmbPRTORequiredTech;
    private JComboBox cmbPRTOUpgradeTo;
    private JCheckBox chkPRTOScienceAge;
    private JCheckBox chkPRTOCapture;
    private JCheckBox chkPRTOEnslave;
    private JCheckBox chkPRTOSacrifice;
    private JCheckBox chkPRTOExploreOrder;
    private JCheckBox chkPRTOSentry;
    private JCheckBox chkPRTOBombardAnimation;
    private JCheckBox chkPRTOCollateralDamage;
    private JCheckBox chkPRTOCreatesCraters;
    private JCheckBox chkPRTOKingStrategy;
    private JCheckBox chkPRTOFlagStrategy;
    private JCheckBox chkPRTOBuildBarricade;
    private JCheckBox chkPRTOBuildAirfield;
    private JCheckBox chkPRTOBuildRadarTower;
    private JCheckBox chkPRTOBuildOutpost;
    private JCheckBox chkPRTOStealthAttack;
    private JLabel jLabel296;
    private JLabel jLabel297;
    private JLabel jLabel298;
    private JLabel jLabel299;
    private JLabel jLabel300;
    private JLabel jLabel301;
    private JLabel jLabel302;
    private JLabel jLabel303;
    private JLabel jLabel304;
    private JLabel jLabel305;
    private JLabel jLabel306;
    private JLabel jLabel307;
    private JLabel jLabel308;
    private JLabel jLabel309;
    private JLabel jLabel310;
    private JLabel jLabel311;
    private JLabel jLabel312;
    private JLabel jLabel313;
    private JLabel jLabel314;
    private JLabel jLabel315;
    private JLabel lblPRTOIgnoreMovementCost;
    private JLabel lblIconIndex;
    private JLabel lblUseExactCost;
    private JLabel lblTelepadRange;
    private JLabel lblQuestionMark3;
    private JLabel lblPRTOLegalUnitTelepads;
    private JLabel lblPRTOLegalBuildingTelepads;
    private JList lstPRTOUnitAbilities;
    private JList lstPRTOAvailableTo;
    private JList lstPRTOIgnoreTerrainCost;
    private JList lstPRTOLegalUnitTelepads;
    private JList lstPRTOLegalBuildingTelepads;
    private JPanel jPanel102;
    private JPanel jPanel103;
    private JPanel jPanel104;
    private JPanel jPanel105;
    private JPanel jPanel106;
    private JPanel jPanel107;
    private JPanel jPanel108;
    private JPanel pnlSpecialActions;
    private JPanel pnlStandardOrders;
    private JPanel pnlRequiredResources;
    private JPanel pnlAirMissions;
    private JPanel jPanel114;
    private JScrollPane scrUnits;
    private JScrollPane jScrollPane35;
    private JScrollPane jScrollPane36;
    private JScrollPane jScrollPane37;
    private JScrollPane jScrollPane39;
    private JScrollPane scrPaneLegalUnitTelepads;
    private JScrollPane scrPaneLegalBuildingTelepads;
    private SuperJTextField txtPRTOWorkerStrength;
    private JList lstPRTOStealthTargets;
    private SuperJList lstUnits = new SuperJList(this, "unit", true, true, true);
    private JToggleButton tglPRTOAir;
    private JToggleButton tglPRTOLand;
    private JToggleButton tglPRTOSea;
    private SuperJTextField txtPRTOAirDefence;
    private SuperJTextField txtPRTOAttack;
    private SuperJTextField txtPRTOBombardRange;
    private SuperJTextField txtPRTOBombardStrength;
    private SuperJTextField txtPRTOCapacity;
    private SuperJTextField txtPRTOCivilopediaEntry;
    private SuperJTextField txtPRTODefence;
    private SuperJTextField txtPRTOHitPointBonus;
    private JSpinner spnrPRTOIconIndex;
    private SuperJTextField txtPRTOMovement;
    private SuperJTextField txtPRTOOperationalRange;
    private SuperJTextField txtPRTOPopulationCost;
    private SuperJTextField txtPRTOQuestionMark3;
    private SuperJTextField txtPRTOQuestionMark5;
    private SuperJTextField txtPRTOQuestionMark6;
    private SuperJTextField txtPRTOQuestionMark8;
    private SuperJTextField txtPRTORateOfFire;
    private SuperJTextField txtPRTOShieldCost;
    private SuperJTextField txtPRTOTelepadRange;
    private SuperJTextField txtPRTOUseExactCost;
    
    private JButton cmdDownloadUnits = new JButton(Main.i18n("units.downloadUnits"));

    private JButton cmdSelectIcon = new JButton("Select Icon");
    IconPanel pnlIconDisplay;

    int strengthInt = 0;
    
    //JavaFX stuff
    //This is experimental as of April 2017.  If it goes well, can expand it more generally.
    final JFXPanel jFXPanel = new JFXPanel();
    
    ListView<PRTO> listView = new ListView<>();
    //Not sure if I can do this here or if it will blow up b/c it's JavaFX.  Let's find out.
    ObservableList<PRTO> unitListFX = FXCollections.observableArrayList();
    ContextMenu unitListPopup = new ContextMenu();
    MenuItem menuAdd = new MenuItem("Add");
    MenuItem menuRename = new MenuItem("Rename");
    MenuItem menuCopy = new MenuItem("Copy");
    MenuItem menuDelete = new MenuItem("Delete");
    
    boolean dndUpdateDisabled = false;

    public UnitTab()
    {
        setupJavaFX();
        
        lstType = lstUnits;
        //logger.setLevel(Level.DEBUG);
        tabName = "PRTO";
        textBoxes = new ArrayList<>();
        unit32Loaded = false;
        scrUnits = new JScrollPane();
        jPanel102 = new JPanel();
        jLabel296 = new JLabel();
        jLabel297 = new JLabel();
        jLabel298 = new JLabel();
        jLabel299 = new JLabel();
        jLabel300 = new JLabel();
        jLabel301 = new JLabel();
        jLabel302 = new JLabel();
        jLabel303 = new JLabel();
        jLabel304 = new JLabel();
        jLabel305 = new JLabel();
        jLabel306 = new JLabel();
        jLabel307 = new JLabel();
        jLabel308 = new JLabel();
        chkPRTORequiresSupport = new JCheckBox();
        chkPRTOZoneOfControl = new JCheckBox();
        chkPRTOBombardAnimation = new JCheckBox();
        chkPRTOCollateralDamage = new JCheckBox();
        chkPRTOCreatesCraters = new JCheckBox();
        txtPRTODefence = new SuperJTextField();
        txtPRTOAttack = new SuperJTextField();
        txtPRTOBombardStrength = new SuperJTextField();
        txtPRTOMovement = new SuperJTextField();
        txtPRTOHitPointBonus = new SuperJTextField();
        txtPRTOAirDefence = new SuperJTextField();
        txtPRTORateOfFire = new SuperJTextField();
        txtPRTOBombardRange = new SuperJTextField();
        txtPRTOPopulationCost = new SuperJTextField();
        txtPRTOWorkerStrength = new SuperJTextField();
        txtPRTOCapacity = new SuperJTextField();
        txtPRTOOperationalRange = new SuperJTextField();
        txtPRTOShieldCost = new SuperJTextField();
        jLabel309 = new JLabel();
        cmbPRTOUpgradeTo = new JComboBox();
        jLabel310 = new JLabel();
        cmbPRTORequiredTech = new JComboBox();
        jLabel311 = new JLabel();
        txtPRTOCivilopediaEntry = new SuperJTextField();
        jPanel103 = new JPanel();
        jPanel104 = new JPanel();
        chkPRTOOffence = new JCheckBox();
        chkPRTODefenceStrategy = new JCheckBox();
        chkPRTOExploreStrategy = new JCheckBox();
        chkPRTOTerraform = new JCheckBox();
        chkPRTOSettle = new JCheckBox();
        chkPRTOKingStrategy = new JCheckBox();
        chkPRTOArtillery = new JCheckBox();
        chkPRTOCruiseMissileUnit = new JCheckBox();
        chkPRTOICBM = new JCheckBox();
        chkPRTOTacticalNuke = new JCheckBox();
        chkPRTOLeaderUnit = new JCheckBox();
        chkPRTOArmyUnit = new JCheckBox();
        chkPRTOFlagStrategy = new JCheckBox();
        chkPRTOTeleportable = new JCheckBox();
        chkPRTOTelepad = new JCheckBox();
        jPanel105 = new JPanel();
        chkPRTONavalPower = new JCheckBox();
        chkPRTONavalTransport = new JCheckBox();
        chkPRTONavalCarrier = new JCheckBox();
        chkPRTONavalMissileTransport = new JCheckBox();
        jPanel106 = new JPanel();
        chkPRTOAirBombard = new JCheckBox();
        chkPRTOAirDefenceStrategy = new JCheckBox();
        chkPRTOAirTransport = new JCheckBox();
        jPanel107 = new JPanel();
        tglPRTOAir = new JToggleButton();
        tglPRTOSea = new JToggleButton();
        tglPRTOLand = new JToggleButton();
        jPanel108 = new JPanel();
        chkPRTOBuildCity = new JCheckBox();
        chkPRTOBuildColony = new JCheckBox();
        chkPRTOBuildRoad = new JCheckBox();
        chkPRTOBuildRailroad = new JCheckBox();
        chkPRTOBuildMine = new JCheckBox();
        chkPRTOIrrigate = new JCheckBox();
        chkPRTOBuildFort = new JCheckBox();
        chkPRTOBuildBarricade = new JCheckBox();
        chkPRTOClearForest = new JCheckBox();
        chkPRTOClearJungle = new JCheckBox();
        chkPRTOPlantForest = new JCheckBox();
        chkPRTOClearPollution = new JCheckBox();
        chkPRTOAutomate = new JCheckBox();
        chkPRTOJoinCity = new JCheckBox();
        chkPRTOBuildAirfield = new JCheckBox();
        chkPRTOBuildRadarTower = new JCheckBox();
        chkPRTOBuildOutpost = new JCheckBox();
        pnlSpecialActions = new JPanel();
        chkPRTOLoad = new JCheckBox();
        chkPRTOUnload = new JCheckBox();
        chkPRTOAirlift = new JCheckBox();
        chkPRTOAirdrop = new JCheckBox();
        chkPRTOPillage = new JCheckBox();
        chkPRTOBombard = new JCheckBox();
        chkPRTOStealthAttack = new JCheckBox();
        chkPRTOBuildArmy = new JCheckBox();
        chkPRTOFinishImprovement = new JCheckBox();
        chkPRTOScienceAge = new JCheckBox();
        chkPRTOUpgrade = new JCheckBox();
        chkPRTOCapture = new JCheckBox();
        chkPRTOEnslave = new JCheckBox();
        chkPRTOSacrifice = new JCheckBox();
        pnlStandardOrders = new JPanel();
        chkPRTOSkipTurn = new JCheckBox();
        chkPRTOWait = new JCheckBox();
        chkPRTOGoTo = new JCheckBox();
        chkPRTOExploreOrder = new JCheckBox();
        chkPRTOSentry = new JCheckBox();
        chkPRTOFortify = new JCheckBox();
        chkPRTODisband = new JCheckBox();
        pnlRequiredResources = new JPanel();
        cmbPRTORequiredResource1 = new JComboBox();
        cmbPRTORequiredResource2 = new JComboBox();
        cmbPRTORequiredResource3 = new JComboBox();
        pnlAirMissions = new JPanel();
        chkPRTOBomb = new JCheckBox();
        chkPRTOIntercept = new JCheckBox();
        chkPRTORebase = new JCheckBox();
        chkPRTOPrecisionBomb = new JCheckBox();
        chkPRTORecon = new JCheckBox();
        jPanel114 = new JPanel();
        jLabel312 = new JLabel();
        jScrollPane35 = new JScrollPane();
        lstPRTOUnitAbilities = new JList();
        jLabel313 = new JLabel();
        jScrollPane36 = new JScrollPane();
        lstPRTOAvailableTo = new JList();
        jLabel314 = new JLabel();
        jScrollPane37 = new JScrollPane();
        lstPRTOStealthTargets = new JList();
        lblPRTOIgnoreMovementCost = new JLabel();
        jScrollPane39 = new JScrollPane();
        scrPaneLegalUnitTelepads = new JScrollPane();
        scrPaneLegalBuildingTelepads = new JScrollPane();
        lstPRTOIgnoreTerrainCost = new JList();
        lstPRTOLegalUnitTelepads = new JList();
        lstPRTOLegalBuildingTelepads = new JList();
        jLabel315 = new JLabel();
        cmbPRTOEnslaveResultsIn = new JComboBox();
        lblIconIndex = new JLabel();
        spnrPRTOIconIndex = new JSpinner();
        lblUseExactCost = new JLabel();
        txtPRTOQuestionMark5 = new SuperJTextField();
        lblTelepadRange = new JLabel();
        txtPRTOUseExactCost = new SuperJTextField();
        lblQuestionMark3 = new JLabel();
        lblPRTOLegalUnitTelepads = new JLabel();
        lblPRTOLegalBuildingTelepads = new JLabel();
        txtPRTOTelepadRange = new SuperJTextField();
        txtPRTOQuestionMark3 = new SuperJTextField();
        txtPRTOQuestionMark6 = new SuperJTextField();
        txtPRTOQuestionMark8 = new SuperJTextField();

        pnlIconDisplay = new IconPanel();

        unitClass = new ButtonGroup();
        unitClass.add(this.tglPRTOAir);
        unitClass.add(this.tglPRTOSea);
        unitClass.add(this.tglPRTOLand);

    }
    
    private void setupJavaFX() {
        unitListPopup.getItems().addAll(menuAdd, menuRename, menuCopy, menuDelete);
        listView.setContextMenu(unitListPopup);
        listView.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>(){
            @Override
            public void handle(ContextMenuEvent event) {
                unitListPopup.show(listView, event.getScreenX(), event.getScreenY());
                event.consume();
            }
        });
        
        listView.setOnDragDetected(new EventHandler(){
            public void handle(Event event) {
                if (logger.isTraceEnabled())
                    logger.trace("Drag detected");  
                
                String name = listView.getSelectionModel().getSelectedItem().getName();
                String index = "" + listView.getSelectionModel().getSelectedItem().getIndex();
                if (logger.isTraceEnabled())
                    logger.trace("Dragging " + name);
                
                //Option 1: Serialize out tech, use the tech itself on the dragboard
                //Option 2: Don't even use the dragboard, or use a dummy object.  Instead use variables
                //to keep track of what's being dragged and what's being dropped (and checking sources to
                //make sure it's valid).
                //One is probably more technically correct, but not sure which makes more sense.
                
                
                
                Dragboard db = listView.startDragAndDrop(TransferMode.MOVE);
                
                ClipboardContent clip = new ClipboardContent();
                clip.putString(index);
                
                db.setContent(clip);
            }
        });
        
        listView.setOnDragOver(new EventHandler<DragEvent>(){
            public void handle(DragEvent event) {                
                event.acceptTransferModes(TransferMode.MOVE);
            }
        });
        
        listView.setOnDragEntered(new EventHandler<DragEvent>(){
            public void handle(DragEvent dragEvent) {
                if (logger.isTraceEnabled())
                    logger.trace("Drag entered");
                
                
            }
        });
        
        listView.setOnDragExited(new EventHandler<DragEvent>(){
            public void handle(DragEvent dragEvent) {
                if (logger.isTraceEnabled())
                    logger.trace("Drag exited");
            }
        });
        
        listView.setOnDragDropped(new EventHandler<DragEvent>(){
            public void handle(DragEvent dragEvent) {
                if (logger.isTraceEnabled())
                    logger.trace("Drag dropped");
                
                String s2pizzaBar = listView.getSelectionModel().getSelectedItem().getName();
                
                if (logger.isTraceEnabled())
                    logger.trace("Selected item name : " + s2pizzaBar);
                
                Dragboard d = dragEvent.getDragboard();
                
                if (logger.isTraceEnabled())
                    logger.trace("Dropped " + d.getString());
            }
        });
        
        menuAdd.setOnAction(new EventHandler<javafx.event.ActionEvent>(){
                        
            @Override
            public void handle(javafx.event.ActionEvent e) {
                
                String unitName = utils.getItemName("Enter unit name", "Unit name", "Enter unit name:", "");
                if (unitName.length() > 0) {
                    addItem(unitName);
                    unitListFX.add(unit.get(unit.size() - 1));
                }
            }
        });
        menuRename.setOnAction(new EventHandler<javafx.event.ActionEvent>(){
            @Override
            public void handle(javafx.event.ActionEvent e) {
                PRTO unit = listView.getSelectionModel().getSelectedItem();
                
                String unitName = utils.getItemName("Rename " + unit.getName(), "Unit name",  "Enter new name for " + unit.getName() + ": ", unit.getName());
                if (unitName.length() > 0) {
                    unit.setName(unitName);
                    listView.refresh();
                }
                
            }
        });
        menuCopy.setOnAction(new EventHandler<javafx.event.ActionEvent>(){
            @Override
            public void handle(javafx.event.ActionEvent e) {
                PRTO selectedUnit = listView.getSelectionModel().getSelectedItem();
                
                String unitName = utils.getItemName("Copy " + selectedUnit.getName(), "Unit name",  "Enter the name for the copy of " + selectedUnit.getName() + ": ", "");
                if (unitName.length() > 0) {
                    storeData();    //Store any pending changes for the previous unit
                    copyItem(unitName, selectedUnit.getIndex());    //Clone the old item, add it to the list
                    unitListFX.add(unit.get(unit.size() - 1));  //Add it to the UI
                    dndUpdateDisabled = true;   //to avoid updates when setting selection on the UI
                    listView.getSelectionModel().select(unit.get(unit.size() - 1)); //update selection on UI
                    unitIndex = unit.size() - 1;    //set proper index
                    displayData();  //display new unit's data
                    dndUpdateDisabled = false;  //so updates work properly in the future
                }
                
            }
        });
        menuDelete.setOnAction( a -> {
            deleteAction();
        });
    }
    
    private void initializeUnitListFX() {
        
        /**
        for (TECH tech : this.technology) {
            list.add("testing this");
        }
        **/
        
        //10/29/2018 - Upgrade attempt.  N.B. Might run into issues with Java 11 due to com.sun.javafx classes
        QueryList<PRTO> queryList = new QueryList<>(unitListFX);
        queryList.setPredicate(PredicateFactory.createPRTOFilter());
        
        TextField filter = new TextField();
        //This seems to cause issues.  Notably, it causes the selected unit to wind up being null,
        //and is suspected of causing the JavaFX UI to freeze up.  Removing for 1.18 to see if that
        //helps with issues.
        /**
        filter.setText("Filter units...");
        filter.setOpacity(0.5);
        
        filter.focusedProperty().addListener(value -> {
            ReadOnlyBooleanProperty v = (ReadOnlyBooleanProperty) value;
            boolean hasFocus = v.getValue();
            if (hasFocus && filter.getText().equals("Filter units...")) {
                filter.setText("");
            }
            else if (filter.getText().equals("")) {
                filter.setText("Filter units...");
                filter.setOpacity(0.5);
            }
        });
        * **/
        
        filter.textProperty().addListener(obs -> {
            queryList.updateSearchQuery(filter.getText());
        });
        
        listView.setCellFactory(new Callback<ListView<PRTO>, ListCell<PRTO>>() {
            @Override
            public ListCell<PRTO> call(ListView<PRTO> param) {
                ListCell<PRTO> cell = new ListCell<PRTO>() {
                    @Override
                    protected void updateItem(PRTO tech, boolean empty) {
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
                
                cell.setOnDragDropped(new EventHandler<DragEvent>(){
                    @Override
                    public void handle(DragEvent event) {
                        
                        if (logger.isTraceEnabled())
                            logger.trace("Cell dropped is " + cell.getText());
                        cell.getItem();
                        
                        Dragboard d = event.getDragboard();
                        if (d.hasString()) {
                            String theString = d.getString();
                            if (logger.isTraceEnabled()) {
                                logger.trace("Dropped item is " + theString);
                                logger.trace("This item is " + cell.getItem().getName());
                            }
                            
                            swapOrder(Integer.parseInt(theString), cell.getItem().getIndex());
                        }
                    }
                });
                
                cell.setOnDragEntered(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {
                        cell.setOpacity(.75);
                        cell.getStyleClass().add("dndBorder");
                    }                    
                });
                
                cell.setOnDragExited(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {
                        cell.setOpacity(1);
                        cell.getStyleClass().remove("dndBorder");
                        //cell.setBorder(new Border);
                    }                    
                });
                
                return cell;
            }
        });
                
        listView.setItems(queryList);
              
        listView.getSelectionModel().selectedItemProperty().addListener(
            new javafx.beans.value.ChangeListener<PRTO>() {
                public void changed(ObservableValue<? extends PRTO> ov, PRTO oldVal, PRTO newVal) {
                    //TODO: Still indexed-based, which blows up in yo' face.  Need to update that.
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
    
    /**
     * Reorders technologies when they are dragged and dropped.  A tech is moved from <i>source</i> to <i>destination + 1</i> in the list.  In other words,
     * it is inserted after the <i>destination</i> tech to which it is dragged.
     * @param source
     * @param destination 
     */
    private void swapOrder(int source, int destination) {
        try {
            if (logger.isDebugEnabled())
                logger.debug("Swapping order");

            if (source == destination) {
                if (logger.isDebugEnabled())
                    logger.debug("Source and destination are the same; not swapping");
                return;
            }

            dndUpdateDisabled = true;

            PRTO initialSourceUnit = unit.get(source);
            PRTO initialDestinationUnit = unit.get(destination);

            if (logger.isDebugEnabled()) {
                logger.debug("Source unit: " + initialSourceUnit.getName());
                logger.debug("Destination unit: " + initialDestinationUnit.getName());
            }


            //initialDestinationTech.setIndex(source);

            if (source < destination) {
                //Moving later in the list
                //TODO: Move into if/else, eliminate duplicates
                this.unit.add(destination + 1, initialSourceUnit);
                this.unit.remove(source);

                //Other techs will have lower indices, moved will take old destination index
                initialSourceUnit.setIndex(destination);
                //Decrement indices of intermediary techs
                for (int i = source; i < destination; i++) {
                    unit.get(i).setIndex(unit.get(i).getIndex() - 1);
                }

                //Update tech list FX - remove source last so our insert + is consistent
                unitListFX.add(destination + 1, initialSourceUnit);
                unitListFX.remove(source);

                //Make sure updates save to the right place
                unitIndex = destination;
                listView.getSelectionModel().select(unitIndex);
                
                //Also update the unit list for stealth/telepad (selected values will be updated later)
                unitList.add(destination + 1, initialSourceUnit.getName());  
                unitList.remove(source);
            }
            else {
                //Moving earlier in the list
                this.unit.add(destination + 1, initialSourceUnit);
                this.unit.remove(source + 1); //plus one b/c inserting it earlier increased the old location's index

                //Other techs will have higher indices, moved will take destination (not + 1, b/c the list is base zero)
                initialSourceUnit.setIndex(destination + 1);
                for (int i = source; i > destination + 1; i--) {
                    unit.get(i).setIndex(unit.get(i).getIndex() + 1);
                }

                //Update tech list FX - remove source first so we don't remove a different one
                unitListFX.remove(source);
                unitListFX.add(destination + 1, initialSourceUnit);

                //Make sure updates save to the right place
                unitIndex = destination + 1;
                listView.getSelectionModel().select(unitIndex);
                
                //Also update the unit list for stealth/telepad (selected values will be updated later)
                unitList.add(destination + 1, initialSourceUnit.getName());  
                unitList.remove(source + 1);
            }

            //update Stealth Attack Targets/Unit Telepads in this tab
            for (PRTO prto : baselink.unit) {
                prto.handleSwappedPRTO();
            }
            //update the selected values on the stealth/telepad lists
            int[] selectedStealthTargets = utils.integerArrayToIntArray(initialSourceUnit.getStealthTargets().toArray(new Integer[initialSourceUnit.getStealthTargets().size()]));
            lstPRTOStealthTargets.setSelectedIndices(selectedStealthTargets);
            int[] selectedUnitTelepads = utils.integerArrayToIntArray(initialSourceUnit.getUnitTelepads().toArray(new Integer[initialSourceUnit.getUnitTelepads().size()]));
            lstPRTOLegalUnitTelepads.setSelectedIndices(selectedUnitTelepads);
                        
            updateOrderInOtherTabs(source, destination, initialSourceUnit.getName());

            dndUpdateDisabled = false;
            if (logger.isDebugEnabled())
                logger.debug("End swapping order");
        }
        catch(Exception ex) {
            logger.error("Unexpected swap-order exception", ex);
        }
    }
    
    
    /**
     * When a unit's order in the list is swapped, this method updates anything
     * that may have been displaying/pointing to the old order.  Notably this
     * includes drop-down lists and int-based pointers in other objects.
     * @param source The index of the item before the update
     * @param destination The index of item that the unit will follow after the update.
     * @param unitName The name of the unit that was swapped
     */
    private void updateOrderInOtherTabs(int source, int destination, String unitName) {

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
        
        //Don't forget stealth attack/telepads/upgrade to/enslave results in on this tab
        
        boolean swapBuildingProduces = bldgTab.cmbBLDGUnitProduced.getSelectedIndex() == cmbSource;        
        bldgTab.mdlUnitProduced.removeElementAt(cmbSource);
        bldgTab.mdlUnitProduced.insertElementAt(unitName, cmbDest);
        if (swapBuildingProduces) {
            bldgTab.cmbBLDGUnitProduced.setSelectedIndex(cmbDest);
        }
        
        boolean setKingUnit = civTab.cmbRACEKingUnit.getSelectedIndex() == cmbSource;
        civTab.mdlKingUnit.removeElementAt(cmbSource);
        civTab.mdlKingUnit.insertElementAt(unitName, cmbDest);
        if (setKingUnit) {
            civTab.cmbRACEKingUnit.setSelectedIndex(cmbDest);
        }
        
        updateComboBoxOrder(ruleTab.cmbRULEAdvancedBarbarian, ruleTab.mdlAdvancedBarbarian, unitName, cmbSource, cmbDest);
        updateComboBoxOrder(ruleTab.cmbRULEBasicBarbarian, ruleTab.mdlBasicBarbarian, unitName, cmbSource, cmbDest);
        updateComboBoxOrder(ruleTab.cmbRULEBarbarianSeaUnit, ruleTab.mdlBarbarianSeaUnit, unitName, cmbSource, cmbDest);
        updateComboBoxOrder(ruleTab.cmbRULEBattleCreatedUnit, ruleTab.mdlBattleCreatedUnit, unitName, cmbSource, cmbDest);
        updateComboBoxOrder(ruleTab.cmbRULEBuildArmyUnit, ruleTab.mdlBuildArmyUnit, unitName, cmbSource, cmbDest);
        updateComboBoxOrder(ruleTab.cmbRULEScout, ruleTab.mdlScout, unitName, cmbSource, cmbDest);
        updateComboBoxOrder(ruleTab.cmbRULESlave, ruleTab.mdlSlave, unitName, cmbSource, cmbDest);
        updateComboBoxOrder(ruleTab.cmbRULEStartUnit1, ruleTab.mdlStartUnit1, unitName, cmbSource, cmbDest);
        updateComboBoxOrder(ruleTab.cmbRULEStartUnit2, ruleTab.mdlStartUnit2, unitName, cmbSource, cmbDest);
        updateComboBoxOrder(ruleTab.cmbRULEFlagUnit, ruleTab.mdlFlagUnit, unitName, cmbSource, cmbDest);
        if (baselink.hasCustomPlayerData()) {
            updateComboBoxOrder(playerTab.cmbLEADStartingUnit, playerTab.mdlPossibleStartingUnits, unitName, source, destination);
            //Starting unit list shall be updated when the tab is viewed again
        }
        
        if (baselink.hasCustomMap()) {
            //Note that there is no "None" option on the Map Tab add unit dropdown.  Hence, we use destination/source.
            boolean setAddUnitDropdown = mapTab.cmbUnitType.getSelectedIndex() == source;
            mapTab.unitModel.removeElementAt(source);
            mapTab.unitModel.insertElementAt(unitName, destination);
            if (setAddUnitDropdown) {
                mapTab.cmbUnitType.setSelectedIndex(destination);
            }
        }
        
        for (BLDG building : baselink.buildings) {
            building.handleSwappedUnit();
        }
        
        for (RACE civ : baselink.civilization) {
            civ.handleSwappedUnit();
        }
        for (RULE ruleSet : baselink.rule) {
            ruleSet.handleSwappedUnit();
        }
        
        for (UNIT mapUnit : baselink.mapUnit) {
            mapUnit.handleSwappedUnit();
        }
        
        //PRTO - has lists
        for (LEAD player : baselink.player) {
            //player.handleSwappedUnit();
        }
    }
    
    /**
     * Helper method to update combo boxes after the unit order changes.  This cuts down on
     * duplicated code, and thus the potential for error.
     * @param comboBox The combo box that needs updating.
     * @param model The model behind the combo box.
     * @param unitName The name of the unit whose order has been swapped.
     * @param source The unit's previous position.
     * @param dest The unit's new position.
     */
    private void updateComboBoxOrder(JComboBox comboBox, DefaultComboBoxModel model, String unitName, int source, int dest) {
        boolean setSelected = comboBox.getSelectedIndex() == source;
        model.removeElementAt(source);
        model.insertElementAt(unitName, dest);
        if (setSelected) {
            comboBox.setSelectedIndex(dest);
        }
    }
    
    /**
     * Creates the GUI of the tab.  This includes button groups, layouts, etc.
     * Logically, this is to occur immediately after the constructor is called.
     *
     * @return - A reference to the UnitTab instance itself.  This reference
     * allows the tab to be added to a tab bar.
     */
    public JPanel createTab()
    {
        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        lstUnits.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                if (!evt.getValueIsAdjusting()) {
                    lstUnitsValueChanged(evt);
                }
            }
        });
        scrUnits.setViewportView(lstUnits);

        if (Main.settings.useJavaFX) {
            this.add(jFXPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 130, 480));
            //frmMainTab.add(jFXPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 170, 670));
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    initializeUnitListFX();
                }
            });
        }
        else {
            this.add(scrUnits, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 130, 480));
        }

        jPanel102.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Unit Statistics"));
        jPanel102.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel296.setText("Attack:");
        jPanel102.add(jLabel296, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jLabel297.setText("Defence:");
        jPanel102.add(jLabel297, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jLabel298.setText("Movement:");
        jPanel102.add(jLabel298, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));

        jLabel299.setText("Bombard Strength:");
        jPanel102.add(jLabel299, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

        jLabel300.setText("Bombard Range:");
        jPanel102.add(jLabel300, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));

        jLabel301.setText("Bombard Rate of Fire:");
        jPanel102.add(jLabel301, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, -1, -1));

        jLabel302.setText("Hitpoint Bonus:");
        jPanel102.add(jLabel302, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, -1));

        jLabel303.setText("Air Defence:");
        jPanel102.add(jLabel303, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, -1));

        jLabel304.setText("Operational Range:");
        jPanel102.add(jLabel304, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, -1, -1));

        jLabel305.setText("Transport Capacity:");
        jPanel102.add(jLabel305, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, -1, -1));

        jLabel306.setText("Population Cost:");
        jPanel102.add(jLabel306, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, -1, -1));

        jLabel307.setText("Shield Cost:");
        jPanel102.add(jLabel307, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, -1, -1));

        jLabel308.setText("Worker Strength:");
        jPanel102.add(jLabel308, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, -1, -1));

        chkPRTORequiresSupport.setText("Requires Support");
        jPanel102.add(chkPRTORequiresSupport, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, -1, -1));

        chkPRTOZoneOfControl.setText("Has Zone of Control");
        jPanel102.add(chkPRTOZoneOfControl, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, -1, -1));

        chkPRTOBombardAnimation.setText("Bombard Animation");
        jPanel102.add(chkPRTOBombardAnimation, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, -1, -1));

        chkPRTOCollateralDamage.setText("Causes Collateral Damage");
        jPanel102.add(chkPRTOCollateralDamage, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, -1, -1));

        chkPRTOCreatesCraters.setText("Can Create Craters");
        jPanel102.add(chkPRTOCreatesCraters, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, -1, -1));
        jPanel102.add(txtPRTODefence, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 40, 60, -1));
        jPanel102.add(txtPRTOAttack, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 60, -1));
        jPanel102.add(txtPRTOBombardStrength, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 80, 60, -1));
        jPanel102.add(txtPRTOMovement, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, 60, -1));
        jPanel102.add(txtPRTOHitPointBonus, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, 60, -1));
        jPanel102.add(txtPRTOAirDefence, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 160, 60, -1));
        jPanel102.add(txtPRTORateOfFire, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 120, 60, -1));
        jPanel102.add(txtPRTOBombardRange, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 60, -1));
        jPanel102.add(txtPRTOPopulationCost, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 220, 60, -1));
        jPanel102.add(txtPRTOWorkerStrength, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 260, 60, -1));
        jPanel102.add(txtPRTOCapacity, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 200, 60, -1));
        jPanel102.add(txtPRTOOperationalRange, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 180, 60, -1));
        jPanel102.add(txtPRTOShieldCost, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 240, 60, -1));

        jLabel309.setText("Upgrades to:");
        jPanel102.add(jLabel309, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, -1, -1));

        cmbPRTOUpgradeTo.setModel(mdlUpgradeTo);
        jPanel102.add(cmbPRTOUpgradeTo, new org.netbeans.lib.awtextra.AbsoluteConstraints(93, 390, 120, -1));

        this.add(jPanel102, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 230, 420));

        jLabel310.setText("Prerequisite:");
        this.add(jLabel310, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 30, -1, -1));

        cmbPRTORequiredTech.setModel(mdlPrerequisite);
        this.add(cmbPRTORequiredTech, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 30, 130, -1));

        jLabel311.setText("Civilopedia Entry:");
        this.add(jLabel311, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, -1, -1));
        this.add(txtPRTOCivilopediaEntry, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, 110, -1));

        jPanel103.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "AI Strategies"));
        jPanel103.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel104.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Land Strategies"));
        jPanel104.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        chkPRTOOffence.setText("Offense");
        jPanel104.add(chkPRTOOffence, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        chkPRTODefenceStrategy.setText("Defence");
        jPanel104.add(chkPRTODefenceStrategy, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, -1, -1));

        chkPRTOExploreStrategy.setText("Explore");
        jPanel104.add(chkPRTOExploreStrategy, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        chkPRTOTerraform.setText("Terraform");
        jPanel104.add(chkPRTOTerraform, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, -1, -1));

        chkPRTOSettle.setText("Settle");
        jPanel104.add(chkPRTOSettle, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));

        chkPRTOKingStrategy.setText("King");
        jPanel104.add(chkPRTOKingStrategy, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 60, -1, -1));

        chkPRTOArtillery.setText("Artillery");
        jPanel104.add(chkPRTOArtillery, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

        chkPRTOCruiseMissileUnit.setText("Cruise Missile");
        jPanel104.add(chkPRTOCruiseMissileUnit, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, -1, -1));

        chkPRTOICBM.setText("ICBM");
        jPanel104.add(chkPRTOICBM, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));

        chkPRTOTacticalNuke.setText("Tactical Nuke");
        jPanel104.add(chkPRTOTacticalNuke, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, -1, -1));

        chkPRTOLeaderUnit.setText("Leader");
        jPanel104.add(chkPRTOLeaderUnit, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, -1, -1));

        chkPRTOArmyUnit.setText("Army");
        jPanel104.add(chkPRTOArmyUnit, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, -1, -1));

        chkPRTOFlagStrategy.setText("Flag Unit");
        jPanel104.add(chkPRTOFlagStrategy, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, -1));

        jPanel103.add(jPanel104, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 260, 170));

        jPanel105.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Sea"));
        jPanel105.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        chkPRTONavalPower.setText("Power");
        jPanel105.add(chkPRTONavalPower, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        chkPRTONavalTransport.setText("Transport");
        jPanel105.add(chkPRTONavalTransport, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, -1, -1));

        chkPRTONavalCarrier.setText("Carrier");
        jPanel105.add(chkPRTONavalCarrier, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        chkPRTONavalMissileTransport.setText("Missile Transport");
        jPanel105.add(chkPRTONavalMissileTransport, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 40, -1, -1));

        jPanel103.add(jPanel105, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 260, 80));

        jPanel106.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Air"));

        chkPRTOAirBombard.setText("Bombard");

        chkPRTOAirDefenceStrategy.setText("Defence");

        chkPRTOAirTransport.setText("Transport");

        org.jdesktop.layout.GroupLayout jPanel106Layout = new org.jdesktop.layout.GroupLayout(jPanel106);
        jPanel106.setLayout(jPanel106Layout);
        jPanel106Layout.setHorizontalGroup(
            jPanel106Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel106Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel106Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel106Layout.createSequentialGroup()
                        .add(chkPRTOAirBombard)
                        .add(27, 27, 27)
                        .add(chkPRTOAirTransport))
                    .add(chkPRTOAirDefenceStrategy))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        jPanel106Layout.setVerticalGroup(
            jPanel106Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel106Layout.createSequentialGroup()
                .add(jPanel106Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chkPRTOAirBombard)
                    .add(chkPRTOAirTransport))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chkPRTOAirDefenceStrategy)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel103.add(jPanel106, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 260, 80));

        this.add(jPanel103, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 0, 280, 360));

        jPanel107.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Class"));
        jPanel107.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tglPRTOAir.setText("Air");
        tglPRTOAir.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tglPRTOAirStateChanged(evt);
            }
        });
        jPanel107.add(tglPRTOAir, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 70, 20));

        tglPRTOSea.setText("Sea");
        tglPRTOSea.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tglPRTOSeaStateChanged(evt);
            }
        });
        jPanel107.add(tglPRTOSea, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 70, 20));

        tglPRTOLand.setText("Land");
        tglPRTOLand.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tglPRTOLandStateChanged(evt);
            }
        });
        jPanel107.add(tglPRTOLand, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 70, 20));

        this.add(jPanel107, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 360, 90, 90));

        jPanel108.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Worker Actions"));
        jPanel108.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        chkPRTOBuildCity.setText("Build City");
        jPanel108.add(chkPRTOBuildCity, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        chkPRTOBuildColony.setText("Build Colony");
        jPanel108.add(chkPRTOBuildColony, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        chkPRTOBuildRoad.setText("Build Road");
        jPanel108.add(chkPRTOBuildRoad, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));

        chkPRTOBuildRailroad.setText("Build Railroad");
        jPanel108.add(chkPRTOBuildRailroad, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

        chkPRTOBuildMine.setText("Build Mine");
        jPanel108.add(chkPRTOBuildMine, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));

        chkPRTOIrrigate.setText("Irrigate");
        jPanel108.add(chkPRTOIrrigate, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, -1, -1));

        chkPRTOBuildFort.setText("Build Fort");
        jPanel108.add(chkPRTOBuildFort, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, -1, -1));

        chkPRTOBuildBarricade.setText("Build Barricade");
        jPanel108.add(chkPRTOBuildBarricade, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 40, -1, -1));

        chkPRTOClearForest.setText("Chop Down Forest");
        jPanel108.add(chkPRTOClearForest, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 60, -1, -1));

        chkPRTOClearJungle.setText("Clear Wetlands");
        jPanel108.add(chkPRTOClearJungle, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 80, -1, -1));

        chkPRTOPlantForest.setText("Plant Forest");
        jPanel108.add(chkPRTOPlantForest, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 100, -1, -1));

        chkPRTOClearPollution.setText("Clear Damage");
        jPanel108.add(chkPRTOClearPollution, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 120, -1, -1));

        chkPRTOAutomate.setText("Automate");
        jPanel108.add(chkPRTOAutomate, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, -1));

        chkPRTOJoinCity.setText("Join City");
        jPanel108.add(chkPRTOJoinCity, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 140, -1, -1));

        chkPRTOBuildAirfield.setText("Build Airfield");
        jPanel108.add(chkPRTOBuildAirfield, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, -1));

        chkPRTOBuildRadarTower.setText("Build Radar Tower");
        jPanel108.add(chkPRTOBuildRadarTower, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 160, -1, -1));

        chkPRTOBuildOutpost.setText("Build Outpost");
        jPanel108.add(chkPRTOBuildOutpost, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, -1, -1));

        this.add(jPanel108, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 0, 280, 210));

        pnlSpecialActions.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Special Orders"));
        pnlSpecialActions.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        chkPRTOLoad.setText("Load");
        pnlSpecialActions.add(chkPRTOLoad, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 17, -1, -1));

        chkPRTOUnload.setText("Unload");
        pnlSpecialActions.add(chkPRTOUnload, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 35, -1, -1));

        chkPRTOAirlift.setText("Airlift");
        pnlSpecialActions.add(chkPRTOAirlift, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 17, -1, -1));

        chkPRTOAirdrop.setText("Airdrop");
        pnlSpecialActions.add(chkPRTOAirdrop, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 35, -1, -1));

        chkPRTOPillage.setText("Pillage");
        pnlSpecialActions.add(chkPRTOPillage, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 53, -1, -1));

        chkPRTOBombard.setText("Bombard");
        pnlSpecialActions.add(chkPRTOBombard, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 71, -1, -1));

        chkPRTOStealthAttack.setText("Stealth Attack");
        pnlSpecialActions.add(chkPRTOStealthAttack, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 89, -1, -1));

        chkPRTOBuildArmy.setText("Build Army");
        pnlSpecialActions.add(chkPRTOBuildArmy, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 53, -1, -1));

        chkPRTOFinishImprovement.setText("Finish Improvement");
        pnlSpecialActions.add(chkPRTOFinishImprovement, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 71, -1, -1));

        chkPRTOScienceAge.setText("Science Age");
        pnlSpecialActions.add(chkPRTOScienceAge, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 89, -1, -1));

        chkPRTOUpgrade.setText("Unit Upgrade");
        pnlSpecialActions.add(chkPRTOUpgrade, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 107, -1, -1));

        chkPRTOCapture.setText("Capture");
        pnlSpecialActions.add(chkPRTOCapture, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 125, -1, -1));

        chkPRTOEnslave.setText("Enslave");
        pnlSpecialActions.add(chkPRTOEnslave, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 107, -1, -1));

        chkPRTOSacrifice.setText("Sacrifice");
        pnlSpecialActions.add(chkPRTOSacrifice, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 125, -1, -1));

        chkPRTOTeleportable.setText("Teleportable");
        pnlSpecialActions.add(chkPRTOTeleportable, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 143, -1, -1));

        chkPRTOTelepad.setText("Telepad");
        pnlSpecialActions.add(chkPRTOTelepad, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 143, -1, -1));
        
        chkPRTOCharm.setText("Charm");
        pnlSpecialActions.add(chkPRTOCharm, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 163, -1, -1));

        this.add(pnlSpecialActions, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 210, 260, 188));

        pnlStandardOrders.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Standard Orders"));
        pnlStandardOrders.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        chkPRTOSkipTurn.setText("Skip Turn");
        pnlStandardOrders.add(chkPRTOSkipTurn, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        chkPRTOWait.setText("Wait");
        pnlStandardOrders.add(chkPRTOWait, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        chkPRTOGoTo.setText("Go To");
        pnlStandardOrders.add(chkPRTOGoTo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));

        chkPRTOExploreOrder.setText("Explore");
        pnlStandardOrders.add(chkPRTOExploreOrder, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, -1, -1));

        chkPRTOSentry.setText("Sentry");
        pnlStandardOrders.add(chkPRTOSentry, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, -1));

        chkPRTOFortify.setText("Fortify");
        pnlStandardOrders.add(chkPRTOFortify, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));

        chkPRTODisband.setText("Disband");
        pnlStandardOrders.add(chkPRTODisband, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

        this.add(pnlStandardOrders, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 0, 100, 170));

        pnlRequiredResources.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Required Resources"));
        pnlRequiredResources.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cmbPRTORequiredResource1.setModel(mdlRequiredResource1);
        pnlRequiredResources.add(cmbPRTORequiredResource1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 170, 20));

        cmbPRTORequiredResource2.setModel(mdlRequiredResource2);
        pnlRequiredResources.add(cmbPRTORequiredResource2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 170, 20));

        cmbPRTORequiredResource3.setModel(mdlRequiredResource3);
        pnlRequiredResources.add(cmbPRTORequiredResource3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 170, 20));

        this.add(pnlRequiredResources, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 360, 190, 90));

        pnlAirMissions.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Air Missions"));
        pnlAirMissions.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        chkPRTOBomb.setText("Bombing");
        pnlAirMissions.add(chkPRTOBomb, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        chkPRTORebase.setText("Re-Base");
        pnlAirMissions.add(chkPRTORebase, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, -1, -1));

        chkPRTOPrecisionBomb.setText("Precision Bombing");
        pnlAirMissions.add(chkPRTOPrecisionBomb, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        chkPRTORecon.setText("Reconnaisance");
        pnlAirMissions.add(chkPRTORecon, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 40, -1, -1));

        chkPRTOIntercept.setText("Interception");
        pnlAirMissions.add(chkPRTOIntercept, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));

        this.add(pnlAirMissions, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 398, 260, 87));

        jPanel114.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Lists"));
        jPanel114.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel312.setText("Unit Abilities");
        jPanel114.add(jLabel312, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        lstPRTOUnitAbilities.setModel(new AbstractListModel() {
            String[] strings = { "All Terrain As Roads", "Amphibious", "Army", "Blitz", "Cruise Missile", "Detect Invisible", "Draft", "Flag Unit", "Foot Unit", "Hidden Nationality", "Immobile", "Infinite Bombard Range", "Invisible", "King", "Leader", "Lethal Land Bombardment", "Lethal Sea Bombardment", "Nuclear Weapon", "Radar", "Ranged Attack Animation", "Requires Escort", "Rotate Before Attack", "Sinks in Ocean", "Sinks in Sea", "Starts Golden Age", "Stealth", "Tactical Missile", "Transports Only Aircraft", "Transports Only Foot Units", "Transports Only Tactical Missiles", "Wheeled" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane35.setViewportView(lstPRTOUnitAbilities);

        jPanel114.add(jScrollPane35, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 180, 140));

        jLabel313.setText("Available To");
        jPanel114.add(jLabel313, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, -1, -1));

        lstPRTOAvailableTo.setModel(new AbstractListModel() {
            String[] strings = { "Item 14", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane36.setViewportView(lstPRTOAvailableTo);

        jPanel114.add(jScrollPane36, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 40, 140, 140));

        jLabel314.setText("Stealth Attack Targets");
        jPanel114.add(jLabel314, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 20, -1, -1));

        lstPRTOStealthTargets.setModel(new AbstractListModel() {
            String[] strings = { "", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane37.setViewportView(lstPRTOStealthTargets);

        jPanel114.add(jScrollPane37, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 40, 150, 140));

        lblPRTOIgnoreMovementCost.setText("Ignore Movement Cost");
        jPanel114.add(lblPRTOIgnoreMovementCost, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 20, -1, -1));

        lstPRTOIgnoreTerrainCost.setModel(new AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item I", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane39.setViewportView(lstPRTOIgnoreTerrainCost);

        jPanel114.add(jScrollPane39, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 40, 140, 140));

        lblPRTOLegalUnitTelepads.setText("Legal Unit Telepads");
        jPanel114.add(lblPRTOLegalUnitTelepads, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 20, -1, -1));

        lstPRTOLegalUnitTelepads.setModel(new AbstractListModel(){
            String[] strings = { "Item 1", "Item 2", "Item I", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        scrPaneLegalUnitTelepads.setViewportView(lstPRTOLegalUnitTelepads);

        jPanel114.add(scrPaneLegalUnitTelepads, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 40, 150, 140));

        lblPRTOLegalBuildingTelepads.setText("Legal Building Telepads");
        jPanel114.add(lblPRTOLegalBuildingTelepads, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 20, -1, -1));

        lstPRTOLegalBuildingTelepads.setModel(new AbstractListModel(){
            String[] strings = { "Item 1", "Item 2", "Item I", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        scrPaneLegalBuildingTelepads.setViewportView(lstPRTOLegalBuildingTelepads);

        jPanel114.add(scrPaneLegalBuildingTelepads, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 40, 150, 140));

        this.add(jPanel114, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 480, 980, 190));

        jLabel315.setText("Enslave Results In:");
        this.add(jLabel315, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 460, -1, -1));

        cmbPRTOEnslaveResultsIn.setModel(mdlEnslaveResultsIn);
        this.add(cmbPRTOEnslaveResultsIn, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 460, 130, 20));

        lblIconIndex.setText("Icon Index:");
        this.add(lblIconIndex, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 180, -1, -1));
        this.add(spnrPRTOIconIndex, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 195, 50, -1));
        
        this.add(cmdSelectIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 220, 140, -1));
        
        cmdSelectIcon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (unitIndex == -1) {
                    //Cannot select icon if no unit selected
                    return;
                }
                UnitSelectionWindow selectionWindow = new UnitSelectionWindow(units32, currentIcon, unitIcons, unit.get(unitIndex));
                selectionWindow.setModal(true);
                selectionWindow.initComponents();
                selectionWindow.pack();
                selectionWindow.drawStuff();
                selectionWindow.setVisible(true);
                
                selectionWindow.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        spnrPRTOIconIndex.setValue(unit.get(unitIndex).getIconIndex());
                        displayIcon(unit.get(unitIndex).getIconIndex());
                    }
                });
            }
        });

        pnlIconDisplay.setVisible(true);
        //pnlIconDisplay.setOpaque(true);
        //pnlIconDisplay.setBackground(Color.red);
        //pnlIconDisplay.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        this.add(pnlIconDisplay, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 245, 34, 34));

        lblUseExactCost.setText("Use Exact Cost");
        this.add(lblUseExactCost, new org.netbeans.lib.awtextra.AbsoluteConstraints(903, 290, -1, -1));
        this.add(txtPRTOUseExactCost, new org.netbeans.lib.awtextra.AbsoluteConstraints(993, 290, 30, -1));

        lblTelepadRange.setText("Telepad Range:");
        this.add(txtPRTOTelepadRange, new org.netbeans.lib.awtextra.AbsoluteConstraints(993, 310, 30, -1));
        this.add(lblTelepadRange, new org.netbeans.lib.awtextra.AbsoluteConstraints(903, 310, -1, -1));

        lblQuestionMark3.setText("?3");
        this.add(lblQuestionMark3, new org.netbeans.lib.awtextra.AbsoluteConstraints(903, 330, -1, -1));
        this.add(txtPRTOQuestionMark3, new org.netbeans.lib.awtextra.AbsoluteConstraints(993, 330, 30, -1));

        this.add(txtPRTOQuestionMark5, new org.netbeans.lib.awtextra.AbsoluteConstraints(993, 350, 30, -1));
        this.add(txtPRTOQuestionMark6, new org.netbeans.lib.awtextra.AbsoluteConstraints(993, 370, 30, -1));
        this.add(txtPRTOQuestionMark8, new org.netbeans.lib.awtextra.AbsoluteConstraints(993, 390, 30, -1));
        
        this.add(cmdDownloadUnits, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 410, 140, -1));
        
        
        cmdDownloadUnits.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cmdDownloadUnitsAction();
            }
        });

        this.setName("PRTO");


        spnrPRTOIconIndex.addChangeListener(new javax.swing.event.ChangeListener(){

            public void stateChanged(ChangeEvent e)
            {
                int i = (Integer)spnrPRTOIconIndex.getValue();
                displayIcon(i);
            }
        });

        this.addComponentListener(this);



        tabCreated = true;
        return this;
    }

    public void sendUnitIcons(BufferedImage[][] unitIcons, BufferedImage units32)
    {
        this.unitIcons = unitIcons;
        this.units32 = units32;
    }

    BufferedImage b;
    public void sendWholeThing(BufferedImage b)
    {
        this.b = b;
    }
    public void displayIcon(int index)
    {
        if (Main.GRAPHICS_ENABLED) {
            if (logger.isDebugEnabled())
                logger.debug("going to try to display the icon");
            //Note: unitIcons.length is its width
            int x = index % unitIcons.length;
            int y = index/unitIcons.length;
            Graphics g = pnlIconDisplay.getGraphics();
            if (g != null)
            {
                try {
                    currentIcon = unitIcons[x][y];
                    //g.drawImage(settlerImage, 0, 0, null);
                    pnlIconDisplay.setImage(currentIcon);
                    //pnlIconDisplay.setImage(b);
                    //pnlIconDisplay.setIgnoreRepaint(true);
                    //pnlIconDisplay.setOpaque(false);
                    //causes the update to be visible immediately, even if this is the
                    //active tab
                    pnlIconDisplay.update(g);
                    g.dispose();
                }
                catch(ArrayIndexOutOfBoundsException ex) {
                    logger.warn("Error while updating icon.  You likely do not have the correct units_32.pcx file in your scenario search folders.", ex);
                }
            }
        }
    }
    public void repaintIcon()
    {
        Graphics g = pnlIconDisplay.getGraphics();
        if (g != null)
        {
            g.drawImage(settlerImage, 0, 0, null);
        }
        g.dispose();
    }

    public void processUnits32()
    {
        
    }

    public void sendTabLinks(BldgTab bldgTab, CIVTab civTab, RULETab ruleTab, MapTab mapTab, GAMETab gameTab)
    {
        this.bldgTab = bldgTab;
        this.civTab = civTab;
        this.ruleTab = ruleTab;
        this.mapTab = mapTab;
        this.gameTab = gameTab;
    }

    private void lstUnitsPopup(java.awt.event.MouseEvent evt){
        if (evt.isPopupTrigger())
        {
            lstUnits.setSelectedIndex(lstUnits.locationToIndex(new java.awt.Point(evt.getX(), evt.getY())));
            JPopupMenu popUp = new JPopupMenu();
            popUp.add(delete);
            popUp.add(add);
            java.awt.Component component = evt.getComponent();
            int x = evt.getX();
            int y = evt.getY();
            popUp.show(component, x, y);
        }
    }

    /**
     * This could become a method in the abstract class.
     * The storeData() and displayData() methods could be called from the
     * abstract class, displaying the Hollywood Principle
     */
    public void updateTab()
    {
        if (dndUpdateDisabled) {
            return;
        }
        storeData();
        if (!Main.settings.useJavaFX) {
            unitIndex = lstUnits.getSelectedIndex();
        }
        else {
            PRTO selectedItem = listView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                unitIndex = listView.getSelectionModel().getSelectedItem().getIndex();
            }
            else {
                unitIndex = -1;
            }
        }
        displayData();
    }

    public void storeData()
    {
        if (!(unitIndex == -1)) {
            if (chkPRTOZoneOfControl.isSelected()) {
                unit.get(unitIndex).setZoneOfControl(1);
            } else {
                unit.get(unitIndex).setZoneOfControl(0);
            }
            unit.get(unitIndex).setCivilopediaEntry(txtPRTOCivilopediaEntry.getText());
            unit.get(unitIndex).setBombardStrength(txtPRTOBombardStrength.getInteger());
            unit.get(unitIndex).setBombardRange(txtPRTOBombardRange.getInteger());
            unit.get(unitIndex).setCapacity(txtPRTOCapacity.getInteger());
            unit.get(unitIndex).setShieldCost(txtPRTOShieldCost.getInteger());
            unit.get(unitIndex).setDefence(txtPRTODefence.getInteger());
            unit.get(unitIndex).setIconIndex(Integer.valueOf((Integer)spnrPRTOIconIndex.getValue()));    //spinner
            unit.get(unitIndex).setAttack(txtPRTOAttack.getInteger());
            unit.get(unitIndex).setOperationalRange(txtPRTOOperationalRange.getInteger());
            unit.get(unitIndex).setPopulationCost(txtPRTOPopulationCost.getInteger());
            unit.get(unitIndex).setRateOfFire(txtPRTORateOfFire.getInteger());
            unit.get(unitIndex).setMovement(txtPRTOMovement.getInteger());
            unit.get(unitIndex).setRequiredTech(cmbPRTORequiredTech.getSelectedIndex() - 1);
            //unit.get(unitIndex).setUpgradeTo(cmbPRTOUpgradeTo.getSelectedIndex() - 1);
            if (upgradeToIndexMap.get(cmbPRTOUpgradeTo.getSelectedIndex()) != null) {
                unit.get(unitIndex).setUpgradeTo(upgradeToIndexMap.get(cmbPRTOUpgradeTo.getSelectedIndex()).getIndex());
            }
            else {
                unit.get(unitIndex).setUpgradeTo(-1);
            }
            unit.get(unitIndex).setRequiredResource1(cmbPRTORequiredResource1.getSelectedIndex() - 1);
            unit.get(unitIndex).setRequiredResource2(cmbPRTORequiredResource2.getSelectedIndex() - 1);
            unit.get(unitIndex).setRequiredResource3(cmbPRTORequiredResource3.getSelectedIndex() - 1);
            storeUnitAbilities();
            unit.get(unitIndex).recalculateUnitAbilities();
            storeAIStrategies();
            //Update the # of Firaxis units based on new stategies selected
            int oldNumStrategies = unit.get(unitIndex).getNumStrategies();
            //The old num strategies is being used to figure out how many Firaxis PRTO entries to add.  Since a unit always has
            //at least one such entry, the minimum is 1.
            if (oldNumStrategies == 0)
                oldNumStrategies = 1;   
            unit.get(unitIndex).recalculateAIStrategies();
            int newNewStrategies = unit.get(unitIndex).getNumStrategies();
            //The old num strategies is being used to figure out how many Firaxis PRTO entries to add.  Since a unit always has
            //at least one such entry, the minimum is 1.
            if (newNewStrategies == 0)
                newNewStrategies = 1;   
            if (logger.isDebugEnabled())
                logger.debug("old strats: " + oldNumStrategies + ", new strats: " + newNewStrategies);
            baselink.numUnits += (newNewStrategies - oldNumStrategies);
            //TODO:  There is a recalculateAvailableTo method in PRTO.java that
            //we are not currently using.  Preferably we'd be using that rather
            //than doing a custom method outside of PRTO.java.  It may also
            //be possible to get better than n^2 time on this.
            storeAvailableTo();
            //auto recalculates
            //standard orders/special actions - UNUSED
            //standard air missions -  UNUSED
            storeClass();   //no recalculate necessary
            //other strategy for unit (-1 if first strategy/not multiplie strategies)
            unit.get(unitIndex).setHitPointBonus(txtPRTOHitPointBonus.getInteger());
            storePTWStandardOrders();
            unit.get(unitIndex).recalculatePTWStandardOrders();
            storePTWSpecialActions();
            unit.get(unitIndex).recalculatePTWSpecialActions();
            storePTWWorkerActions();
            unit.get(unitIndex).recalculatePTWWorkerActions();
            storePTWAirMissions();
            unit.get(unitIndex).recalculatePTWAirMissions();
            //PTW Actions Mix
            unit.get(unitIndex).recalculatePTWActionsMix();
            //short unknown
            if (chkPRTOBombardAnimation.isSelected())
                unit.get(unitIndex).setBombardEffects(1);
            else
                unit.get(unitIndex).setBombardEffects(0);
            storeTerrainIgnore();
            if (chkPRTORequiresSupport.isSelected())
                unit.get(unitIndex).setRequiresSupport(1);
            else
                unit.get(unitIndex).setRequiresSupport(0);
            unit.get(unitIndex).setUseExactCost(txtPRTOUseExactCost.getInteger());
            unit.get(unitIndex).setTelepadRange(txtPRTOTelepadRange.getInteger());
            unit.get(unitIndex).setQuestionMark3(txtPRTOQuestionMark3.getInteger());
            storeLegalUnitTelepads();
            if (upgradeToIndexMap.get(cmbPRTOEnslaveResultsIn.getSelectedIndex()) != null) {
                unit.get(unitIndex).setEnslaveResultsIn(upgradeToIndexMap.get(cmbPRTOEnslaveResultsIn.getSelectedIndex()).getIndex());
            }
            else {
                unit.get(unitIndex).setEnslaveResultsIn(-1);
            }
            unit.get(unitIndex).setQuestionMark5(txtPRTOQuestionMark5.getInteger());
            storeStealthTargets();
            unit.get(unitIndex).setQuestionMark6(txtPRTOQuestionMark6.getInteger());
            storeLegalBuildingTelepads();
            unit.get(unitIndex).setQuestionMark8(txtPRTOQuestionMark8.getInteger());
            unit.get(unitIndex).setAirDefence(txtPRTOAirDefence.getInteger());
            //terrain ignore
            if (chkPRTOCreatesCraters.isSelected())
                unit.get(unitIndex).setCreatesCraters((byte)1);
            else
                unit.get(unitIndex).setCreatesCraters((byte)0);
        }
    }

    private void storePTWStandardOrders()
    {
        unit.get(unitIndex).setSkipTurn(chkPRTOSkipTurn.isSelected());
        unit.get(unitIndex).setWait(chkPRTOWait.isSelected());
        unit.get(unitIndex).setSkipTurn(chkPRTOSkipTurn.isSelected());
        unit.get(unitIndex).setFortify(chkPRTOFortify.isSelected());
        unit.get(unitIndex).setDisband(chkPRTODisband.isSelected());
        unit.get(unitIndex).setGoTo(chkPRTOGoTo.isSelected());
        unit.get(unitIndex).setExploreOrder(chkPRTOExploreOrder.isSelected());
        unit.get(unitIndex).setSentry(chkPRTOSentry.isSelected());

    }

    private void storePTWWorkerActions()
    {
        unit.get(unitIndex).setBuildColony(chkPRTOBuildColony.isSelected());
        unit.get(unitIndex).setBuildCity(chkPRTOBuildCity.isSelected());
        unit.get(unitIndex).setBuildRoad(chkPRTOBuildRoad.isSelected());
        unit.get(unitIndex).setBuildRailroad(chkPRTOBuildRailroad.isSelected());
        unit.get(unitIndex).setBuildFort(chkPRTOBuildFort.isSelected());
        unit.get(unitIndex).setBuildMine(chkPRTOBuildMine.isSelected());
        unit.get(unitIndex).setIrrigate(chkPRTOIrrigate.isSelected());
        unit.get(unitIndex).setClearForest(chkPRTOClearForest.isSelected());
        unit.get(unitIndex).setClearJungle(chkPRTOClearJungle.isSelected());
        unit.get(unitIndex).setPlantForest(chkPRTOPlantForest.isSelected());
        unit.get(unitIndex).setClearPollution(chkPRTOClearPollution.isSelected());
        unit.get(unitIndex).setAutomate(chkPRTOAutomate.isSelected());
        unit.get(unitIndex).setJoinCity(chkPRTOJoinCity.isSelected());
        unit.get(unitIndex).setBuildAirfield(chkPRTOBuildAirfield.isSelected());
        unit.get(unitIndex).setBuildRadarTower(chkPRTOBuildRadarTower.isSelected());
        unit.get(unitIndex).setBuildOutpost(chkPRTOBuildOutpost.isSelected());
        unit.get(unitIndex).setBuildBarricade(chkPRTOBuildBarricade.isSelected());
   }
    
    private void storePTWAirMissions()
    {
        unit.get(unitIndex).setBomb(chkPRTOBomb.isSelected());
        unit.get(unitIndex).setRecon(chkPRTORecon.isSelected());
        unit.get(unitIndex).setIntercept(chkPRTOIntercept.isSelected());
        unit.get(unitIndex).setRebase(chkPRTORebase.isSelected());
        unit.get(unitIndex).setPrecisionBomb(chkPRTOPrecisionBomb.isSelected());
    }

    private void storePTWSpecialActions()
    {
        unit.get(unitIndex).setLoad(chkPRTOLoad.isSelected());
        unit.get(unitIndex).setUnload(chkPRTOUnload.isSelected());
        unit.get(unitIndex).setAirlift(chkPRTOAirlift.isSelected());
        unit.get(unitIndex).setPillage(chkPRTOPillage.isSelected());
        unit.get(unitIndex).setBombard(chkPRTOBombard.isSelected());
        unit.get(unitIndex).setAirdrop(chkPRTOAirdrop.isSelected());
        unit.get(unitIndex).setBuildArmy(chkPRTOBuildArmy.isSelected());
        unit.get(unitIndex).setFinishImprovement(chkPRTOFinishImprovement.isSelected());
        unit.get(unitIndex).setUpgrade(chkPRTOUpgrade.isSelected());
        unit.get(unitIndex).setCollateralDamage(chkPRTOCollateralDamage.isSelected());
        unit.get(unitIndex).setScienceAge(chkPRTOScienceAge.isSelected());
        unit.get(unitIndex).setCapture(chkPRTOCapture.isSelected());
        unit.get(unitIndex).setStealthAttack(chkPRTOStealthAttack.isSelected());
        unit.get(unitIndex).setEnslave(chkPRTOEnslave.isSelected());
        unit.get(unitIndex).setSacrifice(chkPRTOSacrifice.isSelected());
        unit.get(unitIndex).setTelepad(chkPRTOTelepad.isSelected());
        unit.get(unitIndex).setTeleportable(chkPRTOTeleportable.isSelected());
        unit.get(unitIndex).setCharm(chkPRTOCharm.isSelected());
        //worker strength
        int currentStrength = Integer.parseInt(txtPRTOWorkerStrength.getText());
        if (currentStrength != this.strengthInt)
        {
            double strengthD = currentStrength + 0.1;
            strengthD = strengthD * 0.01; //divided by one hundred
            float strengthF = (float)strengthD;
            unit.get(unitIndex).setWorkerStrengthFloat(strengthF);
        }
        if (logger.isDebugEnabled())
            logger.debug(unit.get(unitIndex).getName() + " is teleportable?" + chkPRTOTeleportable.isSelected());
    }

    private void storeClass()
    {
        if (tglPRTOLand.isSelected()) {
            if (logger.isTraceEnabled())
                logger.trace("Setting unit class to 0 [land] for" + unit.get(unitIndex).getName());
            unit.get(unitIndex).setUnitClass(0);
        } else if (tglPRTOSea.isSelected()) {
            if (logger.isTraceEnabled())
                logger.trace("Setting unit class to 1 [sea] for" + unit.get(unitIndex).getName());
            unit.get(unitIndex).setUnitClass(1);
        } else if (tglPRTOAir.isSelected()) {
            if (logger.isTraceEnabled())
                logger.trace("Setting unit class to 2 [air] for" + unit.get(unitIndex).getName());
            unit.get(unitIndex).setUnitClass(2);
        }
    }

    private void storeStealthTargets()
    {
        int[]stealthTargets = lstPRTOStealthTargets.getSelectedIndices();
        unit.get(unitIndex).clearStealthAttackTargets();
        for (int i = 0; i < stealthTargets.length; i++) {
            unit.get(unitIndex).addStealthAttackTarget(stealthTargets[i]);
        }
        unit.get(unitIndex).setStealthTargetPRTOLinks();
    }

    private void storeLegalUnitTelepads()
    {
        int[]legalUnitTelepads = lstPRTOLegalUnitTelepads.getSelectedIndices();
        unit.get(unitIndex).clearUnitTelepads();
        for (int i = 0; i < legalUnitTelepads.length; i++){
            unit.get(unitIndex).addUnitTelepad(legalUnitTelepads[i]);
        }
        unit.get(unitIndex).setLegalUnitTelepadsPRTOLinks();
    }
    private void storeLegalBuildingTelepads()
    {
        int[]legalBuildingTelepads = lstPRTOLegalBuildingTelepads.getSelectedIndices();
        unit.get(unitIndex).clearBuildingTelepads();
        for (int i = 0; i < legalBuildingTelepads.length; i++){
            unit.get(unitIndex).addBuildingTelepad(legalBuildingTelepads[i]);
        }
    }
    
    private void storeAvailableTo()
    {
        unit.get(unitIndex).clearAvailableTo();
        int[]selectedAvailTo = lstPRTOAvailableTo.getSelectedIndices();
        for (int i = 0; i < selectedAvailTo.length; i++) {
            unit.get(unitIndex).setAvailableTo(selectedAvailTo[i]);
        }
    }

    private void storeAIStrategies()
    {
        unit.get(unitIndex).setOffence(chkPRTOOffence.isSelected());
        unit.get(unitIndex).setDefenceStrategy(chkPRTODefenceStrategy.isSelected());
        unit.get(unitIndex).setArtillery(chkPRTOArtillery.isSelected());
        unit.get(unitIndex).setExploreStrategy(chkPRTOExploreStrategy.isSelected());
        unit.get(unitIndex).setArmyUnit(chkPRTOArmyUnit.isSelected());
        unit.get(unitIndex).setCruiseMissileUnit(chkPRTOCruiseMissileUnit.isSelected());
        unit.get(unitIndex).setAirBombard(chkPRTOAirBombard.isSelected());
        unit.get(unitIndex).setAirDefenceStrategy(chkPRTOAirDefenceStrategy.isSelected());
        unit.get(unitIndex).setNavalPower(chkPRTONavalPower.isSelected());
        unit.get(unitIndex).setAirTransport(chkPRTOAirTransport.isSelected());
        unit.get(unitIndex).setNavalTransport(chkPRTONavalTransport.isSelected());
        unit.get(unitIndex).setNavalCarrier(chkPRTONavalCarrier.isSelected());
        unit.get(unitIndex).setTerraform(chkPRTOTerraform.isSelected());
        unit.get(unitIndex).setSettle(chkPRTOSettle.isSelected());
        unit.get(unitIndex).setLeaderUnit(chkPRTOLeaderUnit.isSelected());
        unit.get(unitIndex).setTacticalNuke(chkPRTOTacticalNuke.isSelected());
        unit.get(unitIndex).setICBM(chkPRTOICBM.isSelected());
        unit.get(unitIndex).setNavalMissileTransport(chkPRTONavalMissileTransport.isSelected());
        unit.get(unitIndex).setFlagStrategy(chkPRTOFlagStrategy.isSelected());
        unit.get(unitIndex).setKingStrategy(chkPRTOKingStrategy.isSelected());
    }
    
    private void storeTerrainIgnore()
    {
        int[]ignore = lstPRTOIgnoreTerrainCost.getSelectedIndices();
        java.util.Arrays.sort(ignore);
        for (int i = 0; i < unit.get(unitIndex).getNumIgnoreTerrains(); i++)
        {
            if (java.util.Arrays.binarySearch(ignore, i) >= 0)
            {   //present, do ignore
                unit.get(unitIndex).setIgnoreTerrain(i, true);
            }
            else
                unit.get(unitIndex).setIgnoreTerrain(i, false);
        }
    }
    /**
     * This stores STANDARD orders, which are no longer used.
     * The options contained herein are now stored in PTW Worker Actions,
     * and PTW Special Actions
     *
     * @deprecated
     */
    private void storeOrders()
    {
        unit.get(unitIndex).setSkipTurn(chkPRTOSkipTurn.isSelected());
        unit.get(unitIndex).setWait(chkPRTOWait.isSelected());
        unit.get(unitIndex).setFortify(chkPRTOFortify.isSelected());
        unit.get(unitIndex).setDisband(chkPRTODisband.isSelected());
        unit.get(unitIndex).setGoTo(chkPRTOGoTo.isSelected());
        unit.get(unitIndex).setLoad(chkPRTOLoad.isSelected());
        unit.get(unitIndex).setUnload(chkPRTOUnload.isSelected());
        unit.get(unitIndex).setAirlift(chkPRTOAirlift.isSelected());
        unit.get(unitIndex).setPillage(chkPRTOPillage.isSelected());
        unit.get(unitIndex).setBombard(chkPRTOBombard.isSelected());
        unit.get(unitIndex).setAirdrop(chkPRTOAirdrop.isSelected());
        unit.get(unitIndex).setBuildArmy(chkPRTOBuildArmy.isSelected());
        unit.get(unitIndex).setFinishImprovement(chkPRTOFinishImprovement.isSelected());
        unit.get(unitIndex).setUpgrade(chkPRTOUpgrade.isSelected());
        unit.get(unitIndex).setBuildColony(chkPRTOBuildColony.isSelected());
        unit.get(unitIndex).setBuildCity(chkPRTOBuildCity.isSelected());
        unit.get(unitIndex).setBuildRoad(chkPRTOBuildRoad.isSelected());
        unit.get(unitIndex).setBuildRailroad(chkPRTOBuildRailroad.isSelected());
        unit.get(unitIndex).setBuildFort(chkPRTOBuildFort.isSelected());
        unit.get(unitIndex).setBuildMine(chkPRTOBuildMine.isSelected());
        unit.get(unitIndex).setIrrigate(chkPRTOIrrigate.isSelected());
        unit.get(unitIndex).setClearForest(chkPRTOClearForest.isSelected());
        unit.get(unitIndex).setClearJungle(chkPRTOClearJungle.isSelected());
        unit.get(unitIndex).setPlantForest(chkPRTOPlantForest.isSelected());
        unit.get(unitIndex).setClearPollution(chkPRTOClearPollution.isSelected());
        unit.get(unitIndex).setAutomate(chkPRTOAutomate.isSelected());
        unit.get(unitIndex).setJoinCity(chkPRTOJoinCity.isSelected());
    }

    private void storeUnitAbilities()
    {
        int[]unitAbilities = lstPRTOUnitAbilities.getSelectedIndices();
        //set all ability values to false
        unit.get(unitIndex).setWheeled(false);
        unit.get(unitIndex).setFootSoldier(false);
        unit.get(unitIndex).setBlitz(false);
        unit.get(unitIndex).setCruiseMissile(false);
        unit.get(unitIndex).setAllTerrainAsRoads(false);
        unit.get(unitIndex).setRadar(false);
        unit.get(unitIndex).setAmphibiousUnit(false);
        unit.get(unitIndex).setInvisible(false);
        unit.get(unitIndex).setTransportsOnlyAirUnits(false);
        unit.get(unitIndex).setDraftable(false);
        unit.get(unitIndex).setImmobile(false);
        unit.get(unitIndex).setSinksInSea(false);
        unit.get(unitIndex).setSinksInOcean(false);
        unit.get(unitIndex).setFlagUnit(false);
        unit.get(unitIndex).setTransportsOnlyFootUnits(false);
        unit.get(unitIndex).setStartsGoldenAge(false);
        unit.get(unitIndex).setNuclearWeapon(false);
        unit.get(unitIndex).setHiddenNationality(false);
        unit.get(unitIndex).setArmy(false);
        unit.get(unitIndex).setLeader(false);
        unit.get(unitIndex).setInfiniteBombardRange(false);
        unit.get(unitIndex).setStealth(false);
        unit.get(unitIndex).setDetectInvisible(false);
        unit.get(unitIndex).setTacticalMissile(false);
        unit.get(unitIndex).setTransportsOnlyTacticalMissiles(false);
        unit.get(unitIndex).setRangedAttackAnimations(false);
        unit.get(unitIndex).setRotateBeforeAttack(false);
        unit.get(unitIndex).setLethalLandBombardment(false);
        unit.get(unitIndex).setLethalSeaBombardment(false);
        unit.get(unitIndex).setKing(false);
        unit.get(unitIndex).setRequiresEscort(false);
        for (int i = 0; i < unitAbilities.length; i++)
        {
            int selectedAbility = unitAbilities[i];
            String selectedItem = (String)lstPRTOUnitAbilities.getModel().getElementAt(selectedAbility);
            if (selectedItem.equals("All Terrain As Roads"))
                unit.get(unitIndex).setAllTerrainAsRoads(true);
            if (selectedItem.equals("Amphibious"))
                unit.get(unitIndex).setAmphibiousUnit(true);
            if (selectedItem.equals("Army"))
                unit.get(unitIndex).setArmy(true);
            if (selectedItem.equals("Blitz"))
                unit.get(unitIndex).setBlitz(true);
            if (selectedItem.equals("Cruise Missile"))
                unit.get(unitIndex).setCruiseMissile(true);
            if (selectedItem.equals("Detect Invisible"))
                unit.get(unitIndex).setDetectInvisible(true);
            if (selectedItem.equals("Draft"))
                unit.get(unitIndex).setDraftable(true);
            if (selectedItem.equals("Flag Unit"))
                unit.get(unitIndex).setFlagUnit(true);
            if (selectedItem.equals("Foot Unit"))
                unit.get(unitIndex).setFootSoldier(true);
            if (selectedItem.equals("Hidden Nationality"))
                unit.get(unitIndex).setHiddenNationality(true);
            if (selectedItem.equals("Immobile"))
                unit.get(unitIndex).setImmobile(true);
            if (selectedItem.equals("Infinite Bombard Range"))
                unit.get(unitIndex).setInfiniteBombardRange(true);
            if (selectedItem.equals("Invisible"))
                unit.get(unitIndex).setInvisible(true);
            if (selectedItem.equals("King"))
                unit.get(unitIndex).setKing(true);
            if (selectedItem.equals("Leader"))
                unit.get(unitIndex).setLeader(true);
            if (selectedItem.equals("Lethal Land Bombardment"))
                unit.get(unitIndex).setLethalLandBombardment(true);
            if (selectedItem.equals("Lethal Sea Bombardment"))
                unit.get(unitIndex).setLethalSeaBombardment(true);
            if (selectedItem.equals("Nuclear Weapon"))
                unit.get(unitIndex).setNuclearWeapon(true);
            if (selectedItem.equals("Radar"))
                unit.get(unitIndex).setRadar(true);
            if (selectedItem.equals("Ranged Attack Animation"))
                unit.get(unitIndex).setRangedAttackAnimations(true);
            if (selectedItem.equals("Requires Escort"))
                unit.get(unitIndex).setRequiresEscort(true);
            if (selectedItem.equals("Rotate Before Attack"))
                unit.get(unitIndex).setRotateBeforeAttack(true);
            if (selectedItem.equals("Sinks in Ocean"))
                unit.get(unitIndex).setSinksInOcean(true);
            if (selectedItem.equals("Sinks in Sea"))
                unit.get(unitIndex).setSinksInSea(true);
            if (selectedItem.equals("Starts Golden Age"))
                unit.get(unitIndex).setStartsGoldenAge(true);
            if (selectedItem.equals("Stealth"))
                unit.get(unitIndex).setStealth(true);
            if (selectedItem.equals("Tactical Missile"))
                unit.get(unitIndex).setTacticalMissile(true);
            if (selectedItem.equals("Transports Only Aircraft"))
                unit.get(unitIndex).setTransportsOnlyAirUnits(true);
            if (selectedItem.equals("Transports Only Foot Units"))
                unit.get(unitIndex).setTransportsOnlyFootUnits(true);
            if (selectedItem.equals("Transports Only Tactical Missiles"))
                unit.get(unitIndex).setTransportsOnlyTacticalMissiles(true);
            if (selectedItem.equals("Wheeled"))
                unit.get(unitIndex).setWheeled(true);
        }
    }


    public void displayData()
    {
        if (unitIndex != -1)
        {
            if (logger.isDebugEnabled())
                logger.debug("Unit index: " + unitIndex);
            if (unit.get(unitIndex).getZoneOfControl() == 1) {
                chkPRTOZoneOfControl.setSelected(true);
            } else {
                chkPRTOZoneOfControl.setSelected(false);
            }
            txtPRTOCivilopediaEntry.setText(unit.get(unitIndex).getCivilopediaEntry());
            txtPRTOBombardStrength.setText(Integer.toString(unit.get(unitIndex).getBombardStrength()));
            txtPRTOBombardRange.setText(Integer.toString(unit.get(unitIndex).getBombardRange()));
            txtPRTOCapacity.setText(Integer.toString(unit.get(unitIndex).getCapacity()));
            txtPRTOShieldCost.setText(Integer.toString(unit.get(unitIndex).getShieldCost()));
            txtPRTODefence.setText(Integer.toString(unit.get(unitIndex).getDefence()));
            spnrPRTOIconIndex.setValue(unit.get(unitIndex).getIconIndex());
            txtPRTOAttack.setText(Integer.toString(unit.get(unitIndex).getAttack()));
            txtPRTOOperationalRange.setText(Integer.toString(unit.get(unitIndex).getOperationalRange()));
            txtPRTOPopulationCost.setText(Integer.toString(unit.get(unitIndex).getPopulationCost()));
            txtPRTORateOfFire.setText(Integer.toString(unit.get(unitIndex).getRateOfFire()));
            txtPRTOMovement.setText(Integer.toString(unit.get(unitIndex).getMovement()));
            cmbPRTORequiredTech.setSelectedIndex(unit.get(unitIndex).getRequiredTech() + 1);
            cmbPRTORequiredResource1.setSelectedIndex(unit.get(unitIndex).getRequiredResource1() + 1);
            cmbPRTORequiredResource2.setSelectedIndex(unit.get(unitIndex).getRequiredResource2() + 1);
            cmbPRTORequiredResource3.setSelectedIndex(unit.get(unitIndex).getRequiredResource3() + 1);
            //unit class
            if (logger.isTraceEnabled())
                logger.trace("unit class: " + unit.get(unitIndex).getUnitClass());
            //setting one toggle will automatically select the other two, due
            //to code I added to the toggle's change-state responses
            //not having to manually deselect all here decreases the risk of
            //errors
            switch(unit.get(unitIndex).getUnitClass()) {
                case 0: tglPRTOLand.setSelected(true);
                break;
                case 1: tglPRTOSea.setSelected(true);
                break;
                case 2: tglPRTOAir.setSelected(true);
                break;
                default: logger.warn("Unit class doesn't match air/land/sea for " + unit.get(unitIndex).getName() + "; is " + unit.get(unitIndex).getUnitClass());

            }
            txtPRTOHitPointBonus.setText(Integer.toString(unit.get(unitIndex).getHitPointBonus()));
            if (unit.get(unitIndex).getRequiresSupport() == 1) //true
            {
                chkPRTORequiresSupport.setSelected(true);
            } else {
                chkPRTORequiresSupport.setSelected(false);
            }
            txtPRTOUseExactCost.setText(Integer.toString(unit.get(unitIndex).getUseExactCost()));
            txtPRTOTelepadRange.setText(Integer.toString(unit.get(unitIndex).getTelepadRange()));
            txtPRTOQuestionMark3.setText(Integer.toString(unit.get(unitIndex).getQuestionMark3()));
            //Legal unit telepads
            int[] legalUnitTelepads = utils.integerArrayToIntArray(unit.get(unitIndex).getUnitTelepads().toArray(new Integer[0]));
            lstPRTOLegalUnitTelepads.setSelectedIndices(legalUnitTelepads);
            txtPRTOQuestionMark5.setText(Integer.toString(unit.get(unitIndex).getQuestionMark5()));
            //Stealth Targets
            int[] stealthTargets = utils.integerArrayToIntArray(unit.get(unitIndex).getStealthTargets().toArray(new Integer[0]));
            lstPRTOStealthTargets.setSelectedIndices(stealthTargets);
            txtPRTOQuestionMark6.setText(Integer.toString(unit.get(unitIndex).getQuestionMark6()));
            //Legal building telepads
            int[] legalBuildingTelepads = utils.integerArrayToIntArray(unit.get(unitIndex).getBuildingTelepads().toArray(new Integer[0]));
            lstPRTOLegalBuildingTelepads.setSelectedIndices(legalBuildingTelepads);
            txtPRTOQuestionMark8.setText(Integer.toString(unit.get(unitIndex).getQuestionMark8()));
            txtPRTOAirDefence.setText(Integer.toString(unit.get(unitIndex).getAirDefence()));
            if (unit.get(unitIndex).hasOffenceStrategy()) {
                chkPRTOOffence.setSelected(true);
            } else {
                chkPRTOOffence.setSelected(false);
            }
            if (unit.get(unitIndex).hasDefenceStrategy()) {
                chkPRTODefenceStrategy.setSelected(true);
            } else {
                chkPRTODefenceStrategy.setSelected(false);
            }
            if (unit.get(unitIndex).hasArtilleryStrategy()) {
                chkPRTOArtillery.setSelected(true);
            } else {
                chkPRTOArtillery.setSelected(false);
            }
            if (unit.get(unitIndex).hasExploreStrategy()) {
                chkPRTOExploreStrategy.setSelected(true);
            } else {
                chkPRTOExploreStrategy.setSelected(false);
            }
            if (unit.get(unitIndex).hasArmyStrategy()) {
                chkPRTOArmyUnit.setSelected(true);
            } else {
                chkPRTOArmyUnit.setSelected(false);
            }
            if (unit.get(unitIndex).hasCruiseMissileStrategy()) {
                chkPRTOCruiseMissileUnit.setSelected(true);
            } else {
                chkPRTOCruiseMissileUnit.setSelected(false);
            }
            if (unit.get(unitIndex).hasAirBombardStrategy()) {
                chkPRTOAirBombard.setSelected(true);
            } else {
                chkPRTOAirBombard.setSelected(false);
            }
            if (unit.get(unitIndex).hasAirDefenceStrategy()) {
                chkPRTOAirDefenceStrategy.setSelected(true);
            } else {
                chkPRTOAirDefenceStrategy.setSelected(false);
            }
            if (unit.get(unitIndex).hasNavalPowerStrategy()) {
                chkPRTONavalPower.setSelected(true);
            } else {
                chkPRTONavalPower.setSelected(false);
            }
            if (unit.get(unitIndex).hasAirTransportStrategy()) {
                chkPRTOAirTransport.setSelected(true);
            } else {
                chkPRTOAirTransport.setSelected(false);
            }
            if (unit.get(unitIndex).hasNavalTransportStrategy()) {
                chkPRTONavalTransport.setSelected(true);
            } else {
                chkPRTONavalTransport.setSelected(false);
            }
            if (unit.get(unitIndex).hasNavalCarrierStrategy()) {
                chkPRTONavalCarrier.setSelected(true);
            } else {
                chkPRTONavalCarrier.setSelected(false);
            }
            if (unit.get(unitIndex).hasTerraformStrategy()) {
                chkPRTOTerraform.setSelected(true);
            } else {
                chkPRTOTerraform.setSelected(false);
            }
            if (unit.get(unitIndex).hasSettlerStrategy()) {
                chkPRTOSettle.setSelected(true);
            } else {
                chkPRTOSettle.setSelected(false);
            }
            if (unit.get(unitIndex).hasLeaderUnitStrategy()) {
                chkPRTOLeaderUnit.setSelected(true);
            } else {
                chkPRTOLeaderUnit.setSelected(false);
            }
            if (unit.get(unitIndex).hasTacticalNukeStrategy()) {
                chkPRTOTacticalNuke.setSelected(true);
            } else {
                chkPRTOTacticalNuke.setSelected(false);
            }
            if (unit.get(unitIndex).hasICBMStrategy()) {
                chkPRTOICBM.setSelected(true);
            } else {
                chkPRTOICBM.setSelected(false);
            }
            if (unit.get(unitIndex).hasNavalMissileTransportStrategy()) {
                chkPRTONavalMissileTransport.setSelected(true);
            } else {
                chkPRTONavalMissileTransport.setSelected(false);
            }
            if (unit.get(unitIndex).hasKingStrategy())
                chkPRTOKingStrategy.setSelected(true);
            else
                chkPRTOKingStrategy.setSelected(false);
            if (unit.get(unitIndex).hasFlagStrategy())
                chkPRTOFlagStrategy.setSelected(true);
            else
                chkPRTOFlagStrategy.setSelected(false);
            displayPTWStandardOrders();
            displayPTWSpecialActions();
            displayPTWWorkerActions();
            displayPTWAirMissions();
            displayUnitAbilities();
            //set civilizations available to
            List<Integer>civsAvailableTo = new ArrayList<Integer>();
            for (int i = 0; i < 32; i++)
            {
                if (unit.get(unitIndex).isAvailableTo(i))
                    civsAvailableTo.add(i);
            }
            if (logger.isTraceEnabled())
            {
                logger.trace("# Civs Available to: " + civsAvailableTo.size());
            }
            Integer[]intArray = new Integer[civsAvailableTo.size()];
            int[]availableToArray = ObjectToPrimitive.intToInt(civsAvailableTo.toArray(intArray));
            lstPRTOAvailableTo.setSelectedIndices(availableToArray);

            //terrain ignore
            List<Integer>ignore = new ArrayList<Integer>();
            for (int i = 0; i < unit.get(unitIndex).getNumIgnoreTerrains(); i++)
            {
                if (unit.get(unitIndex).ignoresTerrain(i))
                {
                    ignore.add(i);
                }
            }
            Object[]objectArray = ignore.toArray();
            Integer[]ignoreArray = new Integer[objectArray.length];
            System.arraycopy(objectArray, 0, ignoreArray, 0, objectArray.length);
            lstPRTOIgnoreTerrainCost.setSelectedIndices(ObjectToPrimitive.intToInt(ignoreArray));

            //other
            if (unit.get(unitIndex).getCreatesCraters() == 1)
                chkPRTOCreatesCraters.setSelected(true);
            else
                chkPRTOCreatesCraters.setSelected(false);
            if (unit.get(unitIndex).getBombardEffects() == 1)
                chkPRTOBombardAnimation.setSelected(true);
            else
                chkPRTOBombardAnimation.setSelected(false);

            //worker strength
            double strength = unit.get(unitIndex).getWorkerStrength() * 100.0 + 0.1;
            strengthInt = (int) strength;
            txtPRTOWorkerStrength.setText(Integer.toString(strengthInt));
            
            //Upgrade to/Enslave
            mdlUpgradeTo.removeAllElements();
            mdlEnslaveResultsIn.removeAllElements();
            upgradeToIndexMap.clear();
            int upgradeTo = unit.get(unitIndex).getUpgradeTo();
            int enslaveResultsIn = unit.get(unitIndex).getEnslaveResultsIn();
            int numAddedToList = 0;
            int selectedIndex = 0, enslaveIndex = 0;
            utils.addWithPossibleDuplicates(Main.i18n("general.none"), mdlUpgradeTo);
            utils.addWithPossibleDuplicates(Main.i18n("general.none"), mdlEnslaveResultsIn);
            upgradeToIndexMap.put(0, null);
            for (int i = 0; i < unit.size(); i++) {
                if (unit.get(i).getUnitClass() == unit.get(unitIndex).getUnitClass()) {
                    utils.addWithPossibleDuplicates(unit.get(i).getName(), mdlUpgradeTo);
                    utils.addWithPossibleDuplicates(unit.get(i).getName(), mdlEnslaveResultsIn);
                    numAddedToList++;
                    if (upgradeTo == i) {
                        selectedIndex = numAddedToList;
                    }
                    if (enslaveResultsIn == i) {
                        enslaveIndex = numAddedToList;
                    }
                    upgradeToIndexMap.put(numAddedToList, unit.get(i));
                }
                //utils.addWithPossibleDuplicates(unit.get(i).getName(), mdlEnslaveResultsIn);
            }
            cmbPRTOUpgradeTo.setSelectedIndex(selectedIndex);
            cmbPRTOEnslaveResultsIn.setSelectedIndex(enslaveIndex);
        }
    }

    private void displayUnitAbilities()
    {
            List<Integer>abilities = new ArrayList<Integer>();
            if (unit.get(unitIndex).treatsAllTerrainAsRoads())
                abilities.add(0);
            if (unit.get(unitIndex).isAmphibious())
                abilities.add(1);
            if (unit.get(unitIndex).isArmy())
                abilities.add(2);
            if (unit.get(unitIndex).isBlitz())
                abilities.add(3);
            if (unit.get(unitIndex).isCruiseMissile())
                abilities.add(4);
            if (unit.get(unitIndex).detectsInvisible())
                abilities.add(5);
            if (unit.get(unitIndex).isDraftable())
                abilities.add(6);
            if (unit.get(unitIndex).isFlagUnit())
                abilities.add(7);
            if (unit.get(unitIndex).isFootSoldier())
                abilities.add(8);
            if (unit.get(unitIndex).hasHiddenNationality())
                abilities.add(9);
            if (unit.get(unitIndex).isImmobile())
                abilities.add(10);
            int count = 11;
            if (unit.get(unitIndex).hasInfiniteBombardRange())
                abilities.add(count);
            count++;
            if (unit.get(unitIndex).isInvisible())
                abilities.add(count);
            count++;
            if (unit.get(unitIndex).isKing())
                abilities.add(count);
            count++;
            if (unit.get(unitIndex).isLeader())
                abilities.add(count);
            count++;
            if (unit.get(unitIndex).hasLethalLandBombardment())
                abilities.add(count);
            count++;
            if (unit.get(unitIndex).hasLethalSeaBombardment())
                abilities.add(count);
            count++;
            if (unit.get(unitIndex).isNuclearWeapon())
                abilities.add(count);
            count++;
            if (unit.get(unitIndex).hasRadar())
                abilities.add(count);
            count++;
            if (unit.get(unitIndex).hasRangedAttackAnimation())
                abilities.add(count);
            count++;
            if (unit.get(unitIndex).requiresEscort())
                abilities.add(count);
            count++;
            if (unit.get(unitIndex).rotatesBeforeAttack())
                abilities.add(count);
            count++;
            if (unit.get(unitIndex).sinksInOcean())
                abilities.add(count);
            count++;
            if (unit.get(unitIndex).sinksInSea())
                abilities.add(count);
            count++;
            if (unit.get(unitIndex).startsGoldenAge())
                abilities.add(count);
            count++;
            if (unit.get(unitIndex).isStealth())
                abilities.add(count);
            count++;
            if (unit.get(unitIndex).isTacticalMissile())
                abilities.add(count);
            count++;
            if (unit.get(unitIndex).transportsOnlyAirUnits())
                abilities.add(count);
            count++;
            if (unit.get(unitIndex).transportsOnlyFootUnits())
                abilities.add(count);
            count++;
            if (unit.get(unitIndex).transportsOnlyTacticalMissiles())
                abilities.add(count);
            count++;
            if (unit.get(unitIndex).isWheeled())
                abilities.add(count);
            Integer[]intArray = new Integer[abilities.size()];
            int[]abilitiesArray = ObjectToPrimitive.intToInt(abilities.toArray(intArray));
            lstPRTOUnitAbilities.setSelectedIndices(abilitiesArray);
    }

    private void displayPTWAirMissions()
    {
        if (unit.get(unitIndex).getBomb()) {
            chkPRTOBomb.setSelected(true);
        } else {
            chkPRTOBomb.setSelected(false);
        }
        if (unit.get(unitIndex).getRecon()) {
            chkPRTORecon.setSelected(true);
        } else {
            chkPRTORecon.setSelected(false);
        }
        if (unit.get(unitIndex).getIntercept()) {
            chkPRTOIntercept.setSelected(true);
        } else {
            chkPRTOIntercept.setSelected(false);
        }
        if (unit.get(unitIndex).getRebase()) {
            chkPRTORebase.setSelected(true);
        } else {
            chkPRTORebase.setSelected(false);
        }
        if (unit.get(unitIndex).getPrecisionBomb()) {
            chkPRTOPrecisionBomb.setSelected(true);
        } else {
            chkPRTOPrecisionBomb.setSelected(false);
        }
    }

    private void displayPTWWorkerActions()
    {
        if (unit.get(unitIndex).getBuildColony()) {
            chkPRTOBuildColony.setSelected(true);
        } else {
            chkPRTOBuildColony.setSelected(false);
        }
        if (unit.get(unitIndex).getBuildCity()) {
            chkPRTOBuildCity.setSelected(true);
        } else {
            chkPRTOBuildCity.setSelected(false);
        }
        if (unit.get(unitIndex).getBuildRoad()) {
            chkPRTOBuildRoad.setSelected(true);
        } else {
            chkPRTOBuildRoad.setSelected(false);
        }
        if (unit.get(unitIndex).getBuildRailroad()) {
            chkPRTOBuildRailroad.setSelected(true);
        } else {
            chkPRTOBuildRailroad.setSelected(false);
        }
        if (unit.get(unitIndex).getBuildFort()) {
            chkPRTOBuildFort.setSelected(true);
        } else {
            chkPRTOBuildFort.setSelected(false);
        }
        if (unit.get(unitIndex).getBuildMine()) {
            chkPRTOBuildMine.setSelected(true);
        } else {
            chkPRTOBuildMine.setSelected(false);
        }
        if (unit.get(unitIndex).getIrrigate()) {
            chkPRTOIrrigate.setSelected(true);
        } else {
            chkPRTOIrrigate.setSelected(false);
        }
        if (unit.get(unitIndex).getClearForest()) {
            chkPRTOClearForest.setSelected(true);
        } else {
            chkPRTOClearForest.setSelected(false);
        }
        if (unit.get(unitIndex).getClearJungle()) {
            chkPRTOClearJungle.setSelected(true);
        } else {
            chkPRTOClearJungle.setSelected(false);
        }
        if (unit.get(unitIndex).getPlantForest()) {
            chkPRTOPlantForest.setSelected(true);
        } else {
            chkPRTOPlantForest.setSelected(false);
        }
        if (unit.get(unitIndex).getClearPollution()) {
            chkPRTOClearPollution.setSelected(true);
        } else {
            chkPRTOClearPollution.setSelected(false);
        }
        if (unit.get(unitIndex).getAutomate()) {
            chkPRTOAutomate.setSelected(true);
        } else {
            chkPRTOAutomate.setSelected(false);
        }
        if (unit.get(unitIndex).getJoinCity()) {
            chkPRTOJoinCity.setSelected(true);
        } else {
            chkPRTOJoinCity.setSelected(false);
        }
        chkPRTOBuildAirfield.setSelected(unit.get(unitIndex).getBuildAirfield());
        chkPRTOBuildRadarTower.setSelected(unit.get(unitIndex).getBuildRadarTower());
        chkPRTOBuildOutpost.setSelected(unit.get(unitIndex).getBuildOutpost());
        chkPRTOBuildBarricade.setSelected(unit.get(unitIndex).getBuildBarricade());
    }

    private void displayPTWStandardOrders()
    {
        if (unit.get(unitIndex).getSkipTurn()) {
            chkPRTOSkipTurn.setSelected(true);
        } else {
            chkPRTOSkipTurn.setSelected(false);
        }
        if (unit.get(unitIndex).getWait()) {
            chkPRTOWait.setSelected(true);
        } else {
            chkPRTOWait.setSelected(false);
        }
        if (unit.get(unitIndex).getFortify()) {
            chkPRTOFortify.setSelected(true);
        } else {
            chkPRTOFortify.setSelected(false);
        }
        if (unit.get(unitIndex).getDisband()) {
            chkPRTODisband.setSelected(true);
        } else {
            chkPRTODisband.setSelected(false);
        }
        if (unit.get(unitIndex).getGoTo()) {
            chkPRTOGoTo.setSelected(true);
        } else {
            chkPRTOGoTo.setSelected(false);
        }
        chkPRTOExploreOrder.setSelected(unit.get(unitIndex).getExploreOrder());
        chkPRTOSentry.setSelected(unit.get(unitIndex).getSentry());
    }

    private void displayPTWSpecialActions()
    {
        if (unit.get(unitIndex).getLoad()) {
            chkPRTOLoad.setSelected(true);
        } else {
            chkPRTOLoad.setSelected(false);
        }
        if (unit.get(unitIndex).getUnload()) {
            chkPRTOUnload.setSelected(true);
        } else {
            chkPRTOUnload.setSelected(false);
        }
        if (unit.get(unitIndex).getAirlift()) {
            chkPRTOAirlift.setSelected(true);
        } else {
            chkPRTOAirlift.setSelected(false);
        }
        if (unit.get(unitIndex).getPillage()) {
            chkPRTOPillage.setSelected(true);
        } else {
            chkPRTOPillage.setSelected(false);
        }
        if (unit.get(unitIndex).getBombard()) {
            chkPRTOBombard.setSelected(true);
        } else {
            chkPRTOBombard.setSelected(false);
        }
        if (unit.get(unitIndex).getAirdrop()) {
            chkPRTOAirdrop.setSelected(true);
        } else {
            chkPRTOAirdrop.setSelected(false);
        }
        if (unit.get(unitIndex).getBuildArmy()) {
            chkPRTOBuildArmy.setSelected(true);
        } else {
            chkPRTOBuildArmy.setSelected(false);
        }
        if (unit.get(unitIndex).getFinishImprovement()) {
            chkPRTOFinishImprovement.setSelected(true);
        } else {
            chkPRTOFinishImprovement.setSelected(false);
        }
        if (unit.get(unitIndex).getUpgrade()) {
            chkPRTOUpgrade.setSelected(true);
        } else {
            chkPRTOUpgrade.setSelected(false);
        }
        chkPRTOCapture.setSelected(unit.get(unitIndex).getCapture());
        chkPRTOTelepad.setSelected(unit.get(unitIndex).getTelepad());
        chkPRTOTeleportable.setSelected(unit.get(unitIndex).getTeleportable());
        chkPRTOStealthAttack.setSelected(unit.get(unitIndex).getStealthAttack());
        chkPRTOCharm.setSelected(unit.get(unitIndex).getCharm());
        chkPRTOEnslave.setSelected(unit.get(unitIndex).getEnslave());
        chkPRTOCollateralDamage.setSelected(unit.get(unitIndex).getCollateralDamage());
        chkPRTOSacrifice.setSelected(unit.get(unitIndex).getSacrifice());
        chkPRTOScienceAge.setSelected(unit.get(unitIndex).getScienceAge());
    }

    private void lstUnitsValueChanged(javax.swing.event.ListSelectionEvent evt) {
        updateTab();
    }

    private void tglPRTOAirStateChanged(javax.swing.event.ChangeEvent evt) {
        // :
        tglPRTOLand.setSelected(false);
        tglPRTOSea.setSelected(false);
    }

    private void tglPRTOSeaStateChanged(javax.swing.event.ChangeEvent evt) {
        // :
        tglPRTOLand.setSelected(false);
        tglPRTOAir.setSelected(false);
    }

    private void tglPRTOLandStateChanged(javax.swing.event.ChangeEvent evt) {
        // :
        tglPRTOSea.setSelected(false);
        tglPRTOAir.setSelected(false);
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
     * @param unit - The list of all units.
     * @param technology - The list of all technologies.
     * @param resource - The list of all resources.
     * @param terrain - The list of all terrains.
     * @param civilization - The list of all civilizations.
     * @param building - The list of all buildings.  Used for legal building telepads.
     */
    public void sendData(List<PRTO>unit, List<TECH>technology, List<GOOD>resource, List<TERR>terrain, List<RACE>civilization, List<BLDG>building)
    {
        assert tabCreated:"Tab must be created before data can be sent to it";
        this.unit = unit;
        this.technology = technology;
        this.resource = resource;
        this.terrain = terrain;
        this.civilization = civilization;
        this.building = building;

        unitIndex = -1;
        unitList = new SuperListModel();
        lstUnits.setModel(unitList);

        cmbPRTORequiredTech.removeAllItems();
        cmbPRTORequiredTech.addItem(noneSelected);
        cmbPRTOUpgradeTo.removeAllItems();
        cmbPRTOUpgradeTo.addItem(noneSelected);
        cmbPRTORequiredResource1.removeAllItems();
        cmbPRTORequiredResource1.addItem(noneSelected);
        cmbPRTORequiredResource2.removeAllItems();
        cmbPRTORequiredResource2.addItem(noneSelected);
        cmbPRTORequiredResource3.removeAllItems();
        cmbPRTORequiredResource3.addItem(noneSelected);
        cmbPRTOEnslaveResultsIn.removeAllItems();
        cmbPRTOEnslaveResultsIn.addItem(noneSelected);

        civilizationList = new DefaultListModel();
        civilizationList.clear();
        lstPRTOAvailableTo.setModel(civilizationList);
        lstPRTOStealthTargets.setModel(unitList);
        lstPRTOLegalUnitTelepads.setModel(unitList);

        for (int i = 0; i < civilization.size(); i++)
        {
            civilizationList.addElement(civilization.get(i).getName());
        }

        buildingList = new DefaultListModel();
        buildingList.clear();
        lstPRTOLegalBuildingTelepads.setModel(buildingList);
        for (int i = 0; i < building.size(); i++)
        {
            buildingList.addElement(building.get(i).getName());
        }

        for (int i = 0; i < technology.size(); i++)
        {
            //add technologies to combo lists that need technologies
            utils.addWithPossibleDuplicates(technology.get(i).getName(), mdlPrerequisite);
        }

        for (int i = 0; i < resource.size(); i++)
        {
            //add goods to comfbo lists that need government
            utils.addWithPossibleDuplicates(resource.get(i).getName(), mdlRequiredResource1);
            utils.addWithPossibleDuplicates(resource.get(i).getName(), mdlRequiredResource2);
            utils.addWithPossibleDuplicates(resource.get(i).getName(), mdlRequiredResource3);

        }

        for (int i = 0; i < unit.size(); i++)
        {
            //add unit to comfbo lists that need government
            unitList.addElement(unit.get(i).getName());
            utils.addWithPossibleDuplicates(unit.get(i).getName(), mdlUpgradeTo);
            utils.addWithPossibleDuplicates(unit.get(i).getName(), mdlEnslaveResultsIn);
        }

        //add terrains to ignore movement cost
        terrainList = new DefaultListModel();
        lstPRTOIgnoreTerrainCost.setModel(terrainList);
        terrainList.clear();
        for (int i = 0; i < terrain.size(); i++)
        {
            terrainList.addElement(terrain.get(i).getName());
        }
        
        
        if (Main.settings.useJavaFX) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < unit.size(); i++)
                    {
                       unitListFX.add(unit.get(i));
                    }
                }
            });
        }
    }
    
    public void sendPlayerTab(PLYRTab playerTab)
    {
        this.playerTab = playerTab;
    }

    public void setSelectedIndex(int i)
    {
        unitIndex = i;
    }

    /**
     * Removes all limits on length and values for text fields.
     */
    public void setNoLimits()
    {
        clearBadValueDocumentListeners();
        clearLengthDocumentListeners();
        if (logger.isDebugEnabled())
            logger.debug("SETTING NO LIMITS for UnitTab");
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
            logger.debug("SETTING MINIMAL LIMITS for UnitTab");
        addLengthDocumentListener(31, txtPRTOCivilopediaEntry);
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
            logger.debug("SETTING EXPLORATORY LIMITS for UnitTab");
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
            logger.debug("SETTING SAFE LIMITS for UnitTab");
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
            logger.debug("SETTING FIRAXIS LIMITS for UnitTab");
        setMinimalLimits();
        addBadValueDocumentListener(1000, txtPRTOShieldCost);
        addBadValueDocumentListener(1000, txtPRTOAttack);
        addBadValueDocumentListener(1000, txtPRTODefence);
        addBadValueDocumentListener(1000, txtPRTOAirDefence);
        addBadValueDocumentListener(1000, txtPRTOBombardStrength);
        //txtPRTOWorkerCost
        addBadValueDocumentListener(362, txtPRTOOperationalRange);
        addBadValueDocumentListener(362, txtPRTOBombardRange);
        addBadValueDocumentListener(255, txtPRTOPopulationCost);
        addBadValueDocumentListener(100, 1, txtPRTOMovement);
        addBadValueDocumentListener(100, txtPRTOCapacity);
        addBadValueDocumentListener(20, -20, txtPRTOHitPointBonus);
        addBadValueDocumentListener(10, txtPRTORateOfFire);
        //that's all the text boxes

        //Set the limits on the spinner
        if (Main.GRAPHICS_ENABLED && this.unitIcons != null) {  //unitIcons is null if a .biq file is not yet opened
            spnrPRTOIconIndex.setModel(new javax.swing.SpinnerNumberModel(1, 0, this.unitIcons.length * this.unitIcons[0].length, 1));
        }
        else {
            //Default limit of 511
            spnrPRTOIconIndex.setModel(new javax.swing.SpinnerNumberModel(1, 0, 511, 1));
        }
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
            logger.debug("SETTING TOTAL LIMITS for UnitTab");
        //don't allow any issues to occur, even if Firaxis would have
        //usually this will be the same as the Firaxian settings
        //this call will take care of minimal limits
        setFiraxisLimits();
    }
    
    @Override
    public boolean checkBounds(List<String>invalidValues)
    {
        if (unitIndex == -1) {
            return true;
        }
        return super.checkBounds(invalidValues);
    }
    
    public void renameBIQElement(int index, String name)
    {
        unit.get(index).setName(name);
    }
    
    public void deleteAction()
    {
        int index = lstUnits.getSelectedIndex();
        if (baselink.hasCustomMap())
        {
            JOptionPane.showMessageDialog(null, "Sorry, deleting unit types with a custom map enabled is not currently supported", "Coming soon!", JOptionPane.INFORMATION_MESSAGE);
            return;
            /**
            //TODO: This appears to be messing up, most likely due to the
            //Firaxis Duplicate Units thing.
            //Easy part - don't allow any more of this type of unit
            if (mapTab.cmbUnitType.getSelectedIndex() - 1 == index)
            {
                mapTab.cmbUnitType.setSelectedIndex(0);
            }
            else if (mapTab.cmbUnitType.getSelectedIndex() - 1 > index)
            {
                mapTab.cmbUnitType.setSelectedIndex(mapTab.cmbUnitType.getSelectedIndex() - 1);
            }
            mapTab.unitModel.removeElementAt(index + 1);
            //Hard part - remove all of this type of unit from the map
            //Over all tiles
            ArrayList<Integer>unitsDeleted = new ArrayList<Integer>();
            for (int i = 0; i < baselink.mapUnit.size(); i++)
            {
                System.out.println(i);
                //If the unit is of the deleted type, get rid of it.
                if (baselink.mapUnit.get(i).PRTONumber == index)
                {
                    System.out.println("match");
                    //First take it off the tile it is on
                    int x = baselink.mapUnit.get(i).x;
                    int y = baselink.mapUnit.get(i).y;
                    TILE tileWithUnit = baselink.tile.get(baselink.calculateTileIndex(x, y));
                    for (int j = 0; j < tileWithUnit.unitsOnTile.size(); j++)
                    {
                        if (tileWithUnit.unitsOnTile.get(j) == i)
                        {
                            tileWithUnit.unitsOnTile.remove(j);
                            break;
                        }
                    }
                    //Now delete it
                    baselink.mapUnit.remove(i);
                    unitsDeleted.add(i);
                }
                else
                {
                    System.out.println(baselink.mapUnit.get(i).PRTONumber + " != " + index);
                }
            }
            System.out.println(unitsDeleted.size() + " units were deleted");
            //Now we need to go through the map again, and decrement UNIT references
            for (int i = 0; i < baselink.tile.size(); i++)
            {
                TILE thisTile = baselink.tile.get(i);
                for (int j = 0; j < thisTile.unitsOnTile.size(); j++)
                {
                    System.out.println("Reference to " + thisTile.unitsOnTile.get(j));
                    for (int k = 0; k < unitsDeleted.size(); k++)
                    {
                        //If a unit earlier than the reference was deleted,
                        //decrement the reference by one to account for this
                        if (unitsDeleted.get(k) < thisTile.unitsOnTile.get(j))
                        {
                            thisTile.unitsOnTile.set(j, thisTile.unitsOnTile.get(j) - 1);
                        }
                        else
                        {
                            //Since unitsDeleted will be ordered (b/c we go through the mapUnit list in order)
                            //we know that none of the remaining units deleted will trigger a decrement
                            break;
                        }
                    }
                    System.out.println("Reference is now " + thisTile.unitsOnTile.get(j));
                }
                thisTile.calculateUnitWithBestDefence();
            }
                * **/
        }
        index = -1;
        if (!Main.settings.useJavaFX) {
            index = lstUnits.getSelectedIndex();
        }
        else {
            index = listView.getSelectionModel().getSelectedItem().getIndex();
            unitListFX.remove(listView.getSelectionModel().getSelectedItem());
        }
        civTab.mdlKingUnit.removeElementAt(index + 1);
        for (int i = 0; i < civTab.civilization.size(); i++)
        {
            civilization.get(i).handleDeletedUnit(index);
        }
        //building
        bldgTab.mdlUnitProduced.removeElementAt(index + 1);
        for (int i = 0; i < bldgTab.buildings.size(); i++)
        {
            bldgTab.buildings.get(i).handleDeleteUnit(index);
        }
        //unit.  upgrade to/enslave results in
        for (int i = 0; i < unit.size(); i++) {
            unit.get(i).handleDeletedUnit(index);
        }
        //rule - a lot of dependencies here
        ruleTab.mdlAdvancedBarbarian.removeElementAt(index + 1);
        ruleTab.mdlBarbarianSeaUnit.removeElementAt(index + 1);
        ruleTab.mdlBasicBarbarian.removeElementAt(index + 1);
        ruleTab.mdlBattleCreatedUnit.removeElementAt(index + 1);
        ruleTab.mdlBuildArmyUnit.removeElementAt(index + 1);
        ruleTab.mdlFlagUnit.removeElementAt(index + 1);
        ruleTab.mdlScout.removeElementAt(index + 1);
        ruleTab.mdlSlave.removeElementAt(index + 1);
        ruleTab.mdlStartUnit1.removeElementAt(index + 1);
        ruleTab.mdlStartUnit2.removeElementAt(index + 1);
        for (int i = 0; i < 1; i++)
        {
            ruleTab.rule.get(i).handleDeletedUnit(index);
            //if we don't set the ruleIndex to -1, and the rule tab has
            //not been visited, incorrect data as reflected by the combo lists
            //will be saved.  That would not be good.
            ruleTab.ruleIndex = -1;
            //don't forget to update the # of Firaxis units
            baselink.numUnits -= unit.get(index).getNumStrategies();
        }

        for (UNIT mapUnit : baselink.mapUnit) {
            mapUnit.handleDeletedUnit(index);
        }

        //player tab
        if (baselink.hasCustomPlayerData())
            this.playerTab.deleteUnit(index);

        //end dependencies
        unitIndex = -1;
        utils.removeFromList(index, unit, unitList, lstUnits);
    }
    
    /**
     * This is only used from the Download Unit area.  Should probably be consolidated.
     * TODO: See if Stealth Attack/Telepad are being updated correctly.  They probably are not.
     * @param name
     * @param addToJList 
     */
    public void addItem(String name, boolean addToJList) {
        addItem(name);
        if (addToJList) {
            if (Main.settings.useJavaFX) {
                this.unitListFX.add(unit.get(unit.size() - 1));
            }
            else {
                this.unitList.addIndexedElement(name);   
            }
        }
    }
    
    public void copyItem(String name, int oldIndex) {
        PRTO copiedUnit = unit.get(oldIndex).clone();
        copiedUnit.setName(name);
        copiedUnit.setIndex(unit.size());  //not -1, b/c it is not yet added
        addUnit(name, copiedUnit);
    }
    
    public boolean addItem(String name)
    {
        PRTO newPRTO = new PRTO(name, baselink, unit.size());
        newPRTO.setNewUnitDefaults();
        addUnit(name, newPRTO);
        return true;
    }
    
    public void addUnit(String name, PRTO newPRTO) {
        newPRTO.setName(name);
        unit.add(newPRTO);

        baselink.numUnits++;

        utils.addWithPossibleDuplicates(name, bldgTab.mdlUnitProduced);
        utils.addWithPossibleDuplicates(name, civTab.mdlKingUnit);
        utils.addWithPossibleDuplicates(name, ruleTab.mdlAdvancedBarbarian);
        utils.addWithPossibleDuplicates(name, ruleTab.mdlBarbarianSeaUnit);
        utils.addWithPossibleDuplicates(name, ruleTab.mdlBasicBarbarian);
        utils.addWithPossibleDuplicates(name, ruleTab.mdlBattleCreatedUnit);
        utils.addWithPossibleDuplicates(name, ruleTab.mdlBuildArmyUnit);
        utils.addWithPossibleDuplicates(name, ruleTab.mdlFlagUnit);
        utils.addWithPossibleDuplicates(name, ruleTab.mdlScout);
        utils.addWithPossibleDuplicates(name, ruleTab.mdlSlave);
        utils.addWithPossibleDuplicates(name, ruleTab.mdlStartUnit1);
        utils.addWithPossibleDuplicates(name, ruleTab.mdlStartUnit2);
        utils.addWithPossibleDuplicates(name, mapTab.unitModel);
        if (Main.biqFile.get(Main.biqIndex).hasCustomPlayerData()) {
            utils.addWithPossibleDuplicates(name, playerTab.mdlPossibleStartingUnits);
        }
        //The unit list is automatically updated b/c it's a SuperJList, and so are the stealth/telepad targets, b/c they use the same model.
        //upgrade to/enslave can't use the same model b/c they are combo boxes, not lists
        //Add unit to the upgrade/enslave models
        //Clarification: If using Swing, it's automatic.  Otherwise, it isn't; must do it here.
        if (Main.settings.useJavaFX) {
            this.unitList.addIndexedElement(name);
        }
        utils.addWithPossibleDuplicates(name, mdlUpgradeTo);
        utils.addWithPossibleDuplicates(name, mdlEnslaveResultsIn);
        
    }
    
    private void cmdDownloadUnitsAction() {
        
        //Verify that there is a search folder
        
        if (baselink.scenarioProperty.get(0).getSearchFolderLength() == 0) {
            GAME scenarioProperties = baselink.scenarioProperty.get(0);
            String searchFolder = JOptionPane.showInputDialog(Main.mainMain, "<html>Unit Downloading requires a scenario search folder, to store the downloaded units, but this scenario does not have any."
                    + "<br/>You may add one in the text box below and click OK to download units, or Cancel out and add them via the PROP tab.</html>", "Scenario Search Folder Required", JOptionPane.INFORMATION_MESSAGE);
            if (searchFolder != null && searchFolder.length() > 0) {
                scenarioProperties.setScenarioSearchFolders(searchFolder);
                gameTab.updateSearchFolders();
            }
            else {
                return;
            }
        }
        
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Main.class.getResource("/fxml/UnitDownloadWindow.fxml"));
        
        final UnitTab thisTab = this;
        
        //Load up the JavaFX.  Tell the JavaFX thread to run it when it's read.
        //It will load the XML, then create a stage (window) with a scene, and
        //the FXML we import will form that scene.  Then we'll show the stage (window).
        Platform.runLater(new Runnable() {
            public void run() {
                try {
                    Parent root = fxmlLoader.load();
                    
                    UnitDownloadWindowController controller = fxmlLoader.getController();
                    controller.setBIQLink(baselink);
                    controller.setUnitTabLink(thisTab);
                    
                    root.setVisible(true);
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    
                    //Icon - convert to JavaFX format
                    //Isn't getting set, not really sure why.  Not worth the time to investigate currently.
                    //Image icon = new Image(Main.class.getProtectionDomain().getCodeSource().getLocation().toString() + "imgs/icon.PNG");
                    //stage.getIcons().add(icon);
                    stage.setTitle("Unit Downloading Information");
                    stage.show();
                }                    
                catch(IOException ex) {
                    logger.error("FX IO exception", ex);
                }
                catch(Exception ex) {
                    logger.error("FX exception", ex);
                }
            }
        });
        
//        UnitDownloadWindowController downloadWindow = new UnitDownloadWindowController();
//        downloadWindow.initialize(null, null);
    }

}
