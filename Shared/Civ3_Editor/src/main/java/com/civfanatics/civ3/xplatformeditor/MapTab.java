/*
 * TODO: Decide if I want single-key, control-key shortcuts, or potentially both.
 * VERIFY: Fix the culture recalculating formula.  Turns out for high culture cities
 * it doesn't follow my formula after all.
 * ALSO, note that the Border Factor should be used when calculating boundaries.
 * This Firaxis editor does not always do this, and thus if the border factor isn't 10,
 * it will not display the same borders that are displayed in-game.
1 Culture: 9 tiles, 3x3
10 culture: 21 tiles, 5x3 + 2x3
100 Culture: 37 tiles, 7x3 + 5x2 + 3x2
1000 culture: 61 tiles,9x3 + 7x4 + 3x2
10,000 culture: 89 tiles,  11x3 + 9x4 + 7x2 + 3x2
100,000 culture: 137 tiles,13x5 + 11x4 + 9x2 + 5x2
1 million culture: 137, same as above

I get:


1: 9 tiles
10: 21 tiles
100: 37 tiles
1K: 57 tiles, 9x3 + 7x2 + 5x2 + 3x2
10K: 81 tiles
100K: 109 tiles
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.civfanatics.civ3.xplatformeditor;

import com.civfanatics.civ3.xplatformeditor.CustomComponents.ButtonInTableEditor;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.ButtonInTableRenderer;
import com.civfanatics.civ3.xplatformeditor.tabs.map.ExistingUnitListElement;
import com.civfanatics.civ3.xplatformeditor.Listeners.CustomAdjustmentListener;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.ComboBoxInTableEditor;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.ComboBoxInTableRenderer;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.JSpinnerInTableRenderer;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.JSpinnerInTableEditor;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.JLabelInTableEditor;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.JLabelInTableRenderer;
import com.civfanatics.civ3.xplatformeditor.tabs.map.Brush;
import com.civfanatics.civ3.xplatformeditor.districts.DistrictDefinitions;
import com.civfanatics.civ3.biqFile.CITY;
import com.civfanatics.civ3.biqFile.CLNY;
import com.civfanatics.civ3.biqFile.IO;
import com.civfanatics.civ3.biqFile.SLOC;
import com.civfanatics.civ3.biqFile.TERR;
import com.civfanatics.civ3.biqFile.TILE;
import com.civfanatics.civ3.biqFile.UNIT;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.MyJViewport;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJTextField;
import com.civfanatics.civ3.xplatformeditor.tabs.map.MapDirection;
import com.civfanatics.civ3.xplatformeditor.tabs.map.OverlayType;
import com.civfanatics.civ3.xplatformeditor.tabs.map.UnitNameWindow;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.UpdateableComboBoxModel;
import com.civfanatics.civ3.xplatformeditor.specialty.SafetyLevel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.apache.log4j.Logger;

/**
 *
 *
 * This file uses bitwise operators on ints.  Those used are:
 *
 *  - &, the bitwise and.  The returned int has only those bits set to 1 that were
 *    1 in both its inputs
 *  - ^, the bitwise or.  The returned int has those bits set to 1 that were 1
 *    in either input
 * TODO: the above is the xor.  this might result in some wrong stuff
 *  - ~, the bitwise not.  This unary operator returns the input int with all 1's
 *    set to 0's, and vice versa.
 *
 * @author Andrew
 */
public class MapTab extends JPanel{
    Logger logger = Logger.getLogger(this.getClass());

    TILE selectedTile;

    IO biq;
    private boolean UISetUp = false;
    private boolean mapIsLive = true;
    MapPanel map;
    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints g = new GridBagConstraints();
    JScrollPane mapScroll;
    CustomAdjustmentListener scollListener;
    
    int oldCulture; //used to track changes in the culture value
    /**
     * We set this flag to false when we are setting the value of cmbOwner due
     * to a different square being selected.  If that is the case, we know that
     * we would have already updated the cultural value of any city present,
     * so cmbOwner need not recalculate ownership of tiles.  In any other case,
     * the change of owner will necessitate recalculating ownership of tiles,
     * in which case this flag will be its default value of true.
     */
    boolean cmbOwnerCultureFire = true;

    SuperJTextField tileName = new SuperJTextField("City Name");
    JComboBox cmbResource;
    JLabel lblTribe = new JLabel("Tribe");
    JComboBox cmbTribe;
    DefaultComboBoxModel resourceModel = new DefaultComboBoxModel();
    DefaultComboBoxModel unitModel = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlTerrains = new DefaultComboBoxModel();
    DefaultComboBoxModel exprModel = new DefaultComboBoxModel();
    DefaultComboBoxModel overlayModel = new DefaultComboBoxModel();
    DefaultComboBoxModel tribeModel = new DefaultComboBoxModel();
    boolean overlayModelInitialized = false;

    JCheckBox chkRoad;
    JCheckBox chkRailroad;
    JCheckBox chkIrrigation;
    JCheckBox chkMine;
    JCheckBox chkFortress;
    JCheckBox chkBarricade;
    JCheckBox chkBarbarians;
    JCheckBox chkHut;
    JCheckBox chkAirfield;
    JCheckBox chkRadarTower;
    JCheckBox chkOutpost;
    JCheckBox chkColony;
    JCheckBox chkStartingLocation;
    JCheckBox chkRuins;
    JCheckBox chkVictoryLocation;
    JCheckBox chkPollution;
    JCheckBox chkCraters;
    JCheckBox chkLandmark;
    JCheckBox chkNW = new JCheckBox("NW");
    JCheckBox chkNE = new JCheckBox("NE");
    JCheckBox chkSW = new JCheckBox("SW");
    JCheckBox chkSE = new JCheckBox("SE");

    JSpinner spnrPopulation = new JSpinner();
    JSpinner spnrCulture = new JSpinner();

    JTable existingUnits;
    DefaultTableModel existingUnitModel = new DefaultTableModel();
    ButtonInTableRenderer buttonRenderer = new ButtonInTableRenderer();
    JSpinnerInTableRenderer spinnerRenderer = new JSpinnerInTableRenderer();
    JLabelInTableRenderer labelRenderer = new JLabelInTableRenderer();
    
    JButton unitNameDetails = new JButton("Edit Unit Names");

    ButtonInTableEditor buttonEditor = new ButtonInTableEditor();

    TableColumn numColumn;
    TableColumn typeColumn;
    TableColumn exprColumn;
    TableColumn xColumn;


    JPanel cityPanel = new JPanel();
    JPanel districtPanel = new JPanel();

    JScrollPane buildingsScroll = new JScrollPane();
    JList lstBuildings = new JList();
    DefaultListModel buildingsModel = new DefaultListModel();
    DefaultListModel<DistrictDefinitions.DistrictDefinition> districtListModel = new DefaultListModel<DistrictDefinitions.DistrictDefinition>();
    JList<DistrictDefinitions.DistrictDefinition> lstDistricts = new JList<DistrictDefinitions.DistrictDefinition>(districtListModel);
    JButton btnAssignWonder = new JButton("Assign Wonder");
    JButton btnDeleteDistrict = new JButton("Delete district");
    JLabel lblDistrictHeader = new JLabel();
    JLabel lblWonderAssignment = new JLabel();

    JPanel ownerPanel = new JPanel();
    ButtonGroup ownerType = new ButtonGroup();
    JRadioButton rbtnOwnerNone =  new JRadioButton("None");
    JRadioButton rbtnOwnerBarbarians = new JRadioButton("Barbarians");
    JRadioButton rbtnOwnerPlayer = new JRadioButton("Player");
    JRadioButton rbtnOwnerCiv = new JRadioButton("Civilization");
    JComboBox cmbOwner = new JComboBox();
    UpdateableComboBoxModel mdlPlayers = new UpdateableComboBoxModel();
    DefaultComboBoxModel mdlCivs = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlExprs = new DefaultComboBoxModel();
    ComboBoxInTableRenderer comboBoxRenderer = new ComboBoxInTableRenderer(mdlExprs);
    JButton cmdRelocateCity = new JButton("Relocate city");
    JButton cmdRelocateCityAndUnits = new JButton("Relocate city with units");
    JButton cmdDeleteCity = new JButton("Delete city");

    JPanel unitOwnerPanel = new JPanel();
    final JSpinner spnNumber = new JSpinner();
    final JComboBox cmbUnitType = new JComboBox();
    final JComboBox cmbUnitExpr = new JComboBox();
    JRadioButton rbtnNewUnitOwnerBarbarians = new JRadioButton("Barbarians");
    JRadioButton rbtnNewUnitOwnerPlayer = new JRadioButton("Player");
    JRadioButton rbtnNewUnitOwnerCiv = new JRadioButton("Civilization");
    JComboBox cmbNewUnitOwner = new JComboBox();

    ArrayList<Object[]>dataElements;


    IconPanel tileImage = new IconPanel();
    IconPanel cityIcon;
    IconPanel overlayIcon;
    IconPanel selectIcon;
    IconPanel unitIcon;
    IconPanel terrainIcon;
    IconPanel districtIcon;
    IconPanel fogIcon;
    IconPanel diameterOne;
    IconPanel diameterThree;
    IconPanel diameterFive;
    IconPanel diameterSeven;

    BufferedImage selectPNG;
    BufferedImage terrainPNG;
    BufferedImage unitPNG;
    BufferedImage overlayPNG;
    BufferedImage cityPNG;
    BufferedImage districtPNG;
    BufferedImage fogPNG;
    BufferedImage onePNG;
    BufferedImage threePNG;
    BufferedImage fivePNG;
    BufferedImage sevenPNG;
    BufferedImage oneActivePNG;
    BufferedImage threeActivePNG;
    BufferedImage fiveActivePNG;
    BufferedImage sevenActivePNG;
    BufferedImage selectActive;
    BufferedImage terrainActive;
    BufferedImage overlayActive;
    BufferedImage cityActive;
    BufferedImage districtActive;
    BufferedImage fogActive;

    JFrame homeFrame;
    double rightSideWeight;    //need approx 240 pixels

    JScrollPane rightSidePane = new JScrollPane();
    JPanel showRSP = new JPanel();
    
    boolean borderHidden = false;
    boolean updatingDistrictSelection = false;
    DistrictDefinitions districtDefinitions;
    
    UnitNameWindow unitNameWindow = new UnitNameWindow();
        
    public MapTab(JScrollPane mapScroll, CustomAdjustmentListener scollListener, JFrame homeFrame)
    {

        setLayout(layout);


        int pixels = homeFrame.getWidth();
        rightSideWeight = 250.0/pixels;
        DecimalFormat df = new DecimalFormat("0.0%");
        if (logger.isInfoEnabled())
            logger.info("Weight of " + df.format(rightSideWeight) + " with " + pixels + " pixels in width");

        g.fill = GridBagConstraints.BOTH;
        g.weightx = 0;
        g.gridx = 0;
        g.gridy = 0;
        g.gridwidth = 4;

        map = new MapPanel();


        //The custom viewport gets rid of the annoying white bars
        mapScroll = new JScrollPane();
        MyJViewport mjv = new MyJViewport();
        mapScroll.setViewport(mjv);
        mapScroll.setViewportView(map);

        scollListener = new CustomAdjustmentListener(map);
        mapScroll.getHorizontalScrollBar().addAdjustmentListener(scollListener);
        mapScroll.getVerticalScrollBar().addAdjustmentListener(scollListener);
        mapScroll.getVerticalScrollBar().setUnitIncrement(10);
        mapScroll.getHorizontalScrollBar().setUnitIncrement(10);
        mapScroll.setWheelScrollingEnabled(false);
        logger.info("wheel enabled? " + mapScroll.isWheelScrollingEnabled());
        MouseWheelListener[]mwl = mapScroll.getMouseWheelListeners();
        for (MouseWheelListener m : mwl)
            mapScroll.removeMouseWheelListener(m);
        mapScroll.addMouseWheelListener(new MouseWheelListener(){
            
            public void mouseWheelMoved(MouseWheelEvent e)
            {
                replyToMouseWheel(e);
            }
        });


        mapScroll.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);
        //mapScroll.getViewport().setOpaque(false);

        this.scollListener = scollListener;
        this.mapScroll = mapScroll;
        this.homeFrame = homeFrame;

        ownerType.add(this.rbtnOwnerNone);
        ownerType.add(this.rbtnOwnerBarbarians);
        ownerType.add(this.rbtnOwnerPlayer);
        ownerType.add(this.rbtnOwnerCiv);



        tileName.setEditable(false);
        tileName.setHorizontalAlignment(JTextField.CENTER);

    }
    
    void setPlayerName(int index, String newName) {
        boolean repaint = false;
        if (cmbOwner.getSelectedIndex() == index) {
            repaint = true;
        }
        mdlPlayers.set(index, newName);
        if (repaint) {
            //TODO: This does not work.  Probably because it is not visible.
            //Might need to have this happen on map tab activation.
            //For now, it means if you change the civ assigned to a player which
            //is currently selected, it shows up instantly in the dropdown but
            //not with the dropdown collapsed.
            cmbOwner.revalidate();
            cmbOwner.repaint();
        }
    }

    public void setUp(IO biq)
    {
        this.biq = biq;
        districtDefinitions = null;
        districtListModel.clear();
        ensureDistrictDefinitions();
        districtPanel.setVisible(false);
        btnAssignWonder.setVisible(false);
        btnAssignWonder.setEnabled(false);
        btnDeleteDistrict.setEnabled(false);
        lblWonderAssignment.setText("");
        mapIsLive = false;  //combo boxes shouldn't fire events when actions occur
        if (biq.player.size() > 0)
        {
            mdlPlayers.removeAllElements();
            for (int i = 0; i < biq.player.size();) {    //always different so okay with duplicates
                int owner = biq.player.get(i).getCiv();
                if (owner >= 0) {
                    mdlPlayers.addElement("Player " + ++i + " (" + biq.civilization.get(owner).getName() + ")");
                }
                else {
                    mdlPlayers.addElement("Player " + ++i);
                }
            }
        }
        mdlCivs.removeAllElements();
        for (int i = 0; i < biq.civilization.size(); i++)
            utils.addWithPossibleDuplicates(biq.civilization.get(i).getNoun(), mdlCivs);
        buildingsModel.removeAllElements();
        for (int i = 0; i < biq.buildings.size(); i++)  //list model so okay with duplicates
            buildingsModel.addElement(biq.buildings.get(i).getName());
        mdlExprs.removeAllElements();
        for (int i = 0; i < biq.experience.size(); i++)
            utils.addWithPossibleDuplicates(biq.experience.get(i).name, mdlExprs);
        resourceModel.removeAllElements();
        resourceModel.addElement("None");
        for (int i = 0; i < biq.resource.size();i++) {
            //HACK
            //JComboBoxes don't let you select elements properly if multiple
            //entries have the same name.  Thus, we append some garbage to the
            //name to make them not identical.  Below, we set the maximum size
            //so we don't see all those spaces
            utils.addWithPossibleDuplicates(biq.resource.get(i).getName(), resourceModel);
        }
        unitModel.removeAllElements();
        for (int i = 0; i < biq.unit.size(); i++)
        {
            utils.addWithPossibleDuplicates(biq.unit.get(i).getName(), unitModel);
        }
        exprModel.removeAllElements();
        for (int i = 0; i < biq.experience.size(); i++)
        {
            utils.addWithPossibleDuplicates(biq.experience.get(i).name, exprModel);
        }
        tribeModel.removeAllElements();
        tribeModel.addElement("Random");
        for (String tribeName : biq.civilization.get(0).getCityNames()) {
            utils.addWithPossibleDuplicates(tribeName, tribeModel);
        }
        
        if (!UISetUp) {
            setUpUI();
            UISetUp = true;
        }
        mapIsLive = true;
    }
    
    public void setUpUI() {
        
        g.gridx = 0;
        g.gridy = 0;
        g.gridwidth = 1;
        g.weightx = 1 - rightSideWeight;
        g.weighty = 0.5;
        add(mapScroll, g);
        
        JButton cmdShow = new JButton("<");
        cmdShow.setToolTipText("Expand Sidebar");
        cmdShow.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                cmdShowActionPerformed();
            }
        });
        cmdShow.setPreferredSize(new Dimension(50, 20));
        showRSP.add(cmdShow);
        g.weightx = 0;
        g.gridx = 1;
        add(showRSP, g);
        showRSP.setVisible(false);


        //Now the right-side panel
        JPanel rightSidePanel = new JPanel();
        rightSidePane.setViewportView(rightSidePanel);
        GridBagLayout rightSideLayout = new GridBagLayout();
        rightSidePanel.setLayout(rightSideLayout);
        GridBagConstraints rsc = new GridBagConstraints();
        rsc.gridx = 0;
        rsc.gridy = 0;
        rsc.gridwidth = 5;
        rsc.gridheight = 1;
        rsc.weighty = 0.1;
        rsc.weightx = 0.1;
        rsc.ipadx = 4;
        rsc.fill = GridBagConstraints.NONE;
        rsc.anchor = GridBagConstraints.WEST;
        
        JButton cmdHide = new JButton(">");
        cmdHide.setToolTipText("Hide Sidebar");
        cmdHide.setPreferredSize(new Dimension(50, 15));
        cmdHide.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                cmdHideActionPerformed();
            }
        });
        rsc.insets = new Insets(1, 1, 1, 1);
        rightSidePanel.add(cmdHide, rsc);
        rsc.insets = new Insets(0, 0, 0, 0);
        rsc.anchor = GridBagConstraints.CENTER;

        rsc.gridy++;
        
        JPanel pnlIcons = new JPanel();
        pnlIcons.setBorder(new EmptyBorder(0, 5, 0, 5));
        GridBagLayout panelLayout = new GridBagLayout();
        pnlIcons.setLayout(panelLayout);
        GridBagConstraints iconConstraints = new GridBagConstraints();
        iconConstraints.gridx = 0;
        iconConstraints.gridy = 0;
        iconConstraints.gridwidth = 1;
        iconConstraints.gridheight = 1;
        iconConstraints.weighty = 0.1;
        iconConstraints.weightx = 0.1;
        iconConstraints.ipadx = 2;
        iconConstraints.ipady = 2;
        iconConstraints.fill = GridBagConstraints.HORIZONTAL;
        iconConstraints.anchor = GridBagConstraints.CENTER;
        
        selectIcon = new IconPanel();
        selectIcon.setToolTipText("Select (S) - View tile info");
        selectIcon.setVisible(true);
        selectIcon.setMinimumSize(new Dimension(24, 24));
        selectIcon.setPreferredSize(new Dimension(24, 24));
        selectIcon.setMaximumSize(new Dimension(24, 24));
        selectIcon.setSize(new Dimension(24, 24));
        selectIcon.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e)
            {
                hideDistrictPanel();
                map.brush.mode = Brush.MODE_SELECT;
                map.setMapCursor(map.brush.mode);
                setBrushImages(1);
            }
        });
        //The "0" means that this happens when purely T is pressed.  In the actionPerformed I make sure a text box
        //isn't selected.  Thus I get single-key shortcuts, booyeah!
        selectIcon.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('S', 0), "select");
        selectIcon.getActionMap().put("select", new AbstractAction(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() instanceof SuperJTextField)
                    return;
                hideDistrictPanel();
                map.brush.mode = Brush.MODE_SELECT;
                map.setMapCursor(map.brush.mode);
                setBrushImages(1);
            }
        });
        pnlIcons.add(selectIcon, iconConstraints);
        try{
            selectPNG = ImageIO.read(new File(Main.imagePath + "Select.PNG"));
            selectActive = ImageIO.read(new File(Main.imagePath + "SelectActive.PNG"));
        }
        catch(IOException e)
        {
            logger.error("ImageIO error on Select.PNG", e);
        }

        //TERRAINS
        iconConstraints.gridx++;
        terrainIcon = new IconPanel();
        terrainIcon.setToolTipText("Terrain (T) - change the terrain");
        terrainIcon.setVisible(true);
        terrainIcon.setMinimumSize(new Dimension(24, 24));
        terrainIcon.setPreferredSize(new Dimension(24, 24));
        terrainIcon.setMaximumSize(new Dimension(24, 24));
        terrainIcon.setSize(new Dimension(24, 24));
        terrainIcon.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e)
            {
                terrainIconAction();
            }
        });
        //The "0" means that this happens when purely T is pressed.  In the actionPerformed I make sure a text box
        //isn't selected.  Thus I get single-key shortcuts, booyeah!
        terrainIcon.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('T', 0), "terrainSelect");
        terrainIcon.getActionMap().put("terrainSelect", new AbstractAction(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() instanceof SuperJTextField)
                    return;
                terrainIconAction();
            }
        });
        pnlIcons.add(terrainIcon, iconConstraints);
        
        try{
            terrainPNG = ImageIO.read(new File(Main.imagePath + "Terrain.PNG"));
            terrainActive = ImageIO.read(new File(Main.imagePath + "TerrainActive.PNG"));
        }
        catch(IOException e)
        {
            logger.error("ImageIO error on Terrain.PNG", e);
        }
        
        //UNITS
        iconConstraints.gridx++;
        unitIcon = new IconPanel();
        unitIcon.setToolTipText("Units - Not Implemented");
        unitIcon.setVisible(true);
        unitIcon.setMinimumSize(new Dimension(24, 24));
        unitIcon.setPreferredSize(new Dimension(24, 24));
        unitIcon.setMaximumSize(new Dimension(24, 24));
        unitIcon.setSize(new Dimension(24, 24));
//        pnlIcons.add(unitIcon, iconConstraints);
        try{
            unitPNG = ImageIO.read(new File(Main.imagePath + "Unit.PNG"));
        }
        catch(IOException e)
        {
            logger.error("ImageIO error on Unit.PNG", e);
        }

        //OVERLAYS
        iconConstraints.gridx++;
        overlayIcon = new IconPanel();
        overlayIcon.setToolTipText("Overlays (O) - Mines, Forts, Etc.");
        overlayIcon.setVisible(true);
        overlayIcon.setMinimumSize(new Dimension(24, 24));
        overlayIcon.setPreferredSize(new Dimension(24, 24));
        overlayIcon.setMaximumSize(new Dimension(24, 24));
        overlayIcon.setSize(new Dimension(24, 24));
        overlayIcon.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e)
            {
                overlayIconAction();
            }
        });
        overlayIcon.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('O', 0), "overlaySelect");
        overlayIcon.getActionMap().put("overlaySelect", new AbstractAction(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() instanceof SuperJTextField)
                    return;
                overlayIconAction();
            }
        });
        pnlIcons.add(overlayIcon, iconConstraints);
        try{
            overlayPNG = ImageIO.read(new File(Main.imagePath + "Overlay.PNG"));
            overlayActive = ImageIO.read(new File(Main.imagePath + "OverlayActive.PNG"));
        }
        catch(IOException e)
        {
            logger.error("ImageIO error on Overlay.PNG", e);
        }

        //CITIES
        iconConstraints.gridx++;
        cityIcon = new IconPanel();
        cityIcon.setToolTipText("City (C) - Add New City");
        cityIcon.setVisible(true);
        cityIcon.setMinimumSize(new Dimension(24, 24));
        cityIcon.setPreferredSize(new Dimension(24, 24));
        cityIcon.setMaximumSize(new Dimension(24, 24));
        cityIcon.setSize(new Dimension(24, 24));
        cityIcon.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e)
            {
                hideDistrictPanel();
                map.brush.mode = Brush.MODE_SETTLEMENT;
                map.setMapCursor(map.brush.mode);
                setBrushImages(4);
            }
        });
        //The "0" means that this happens when purely T is pressed.  In the actionPerformed I make sure a text box
        //isn't selected.  Thus I get single-key shortcuts, booyeah!
        cityIcon.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('C', 0), "citySelect");
        cityIcon.getActionMap().put("citySelect", new AbstractAction(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() instanceof SuperJTextField)
                    return;
                hideDistrictPanel();
                map.brush.mode = Brush.MODE_SETTLEMENT;
                map.setMapCursor(map.brush.mode);
                setBrushImages(4);
            }
        });
        pnlIcons.add(cityIcon, iconConstraints);
        try{
            cityPNG = ImageIO.read(new File(Main.imagePath + "City_2.PNG"));
            cityActive = ImageIO.read(new File(Main.imagePath + "City_2Active.PNG"));
        }
        catch(IOException e)
        {
            logger.error("ImageIO error on City_2.png", e);
        }

        //DISTRICTS
        iconConstraints.gridx++;
        districtIcon = new IconPanel();
        districtIcon.setToolTipText("Districts (D) - Manage Districts");
        districtIcon.setVisible(true);
        districtIcon.setMinimumSize(new Dimension(24, 24));
        districtIcon.setPreferredSize(new Dimension(24, 24));
        districtIcon.setMaximumSize(new Dimension(24, 24));
        districtIcon.setSize(new Dimension(24, 24));
        districtIcon.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e)
            {
                activateDistrictMode();
            }
        });
        districtIcon.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('D', 0), "districtSelect");
        districtIcon.getActionMap().put("districtSelect", new AbstractAction(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() instanceof SuperJTextField)
                    return;
                activateDistrictMode();
            }
        });
        pnlIcons.add(districtIcon, iconConstraints);
        districtPNG = createDistrictIcon(false);
        districtActive = createDistrictIcon(true);
        districtIcon.setImage(districtPNG);

        
        //FOG
        iconConstraints.gridx++;
        fogIcon = new IconPanel();
        fogIcon.setToolTipText("Fog (F) - Add/Remove Fog of War");
        fogIcon.setVisible(true);
        fogIcon.setMinimumSize(new Dimension(24, 24));
        fogIcon.setPreferredSize(new Dimension(24, 24));
        fogIcon.setMaximumSize(new Dimension(24, 24));
        fogIcon.setSize(new Dimension(24, 24));
        fogIcon.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e)
            {
                fogIconAction();
            }
        });
        //The "0" means that this happens when purely T is pressed.  In the actionPerformed I make sure a text box
        //isn't selected.  Thus I get single-key shortcuts, booyeah!
        fogIcon.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('F', 0), "fogSelect");
        fogIcon.getActionMap().put("fogSelect", new AbstractAction(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() instanceof SuperJTextField)
                    return;
                fogIconAction();
            }
        });
        pnlIcons.add(fogIcon, iconConstraints);
        
        try{
            fogPNG = ImageIO.read(new File(Main.imagePath + "Fog.PNG"));
            fogActive = ImageIO.read(new File(Main.imagePath + "FogActive.PNG"));
        }
        catch(IOException e)
        {
            logger.error("ImageIO error on Fog.PNG", e);
        }
        
        rsc.gridwidth = 5;
        rsc.gridx = 0;
        rsc.fill = GridBagConstraints.HORIZONTAL;
        rightSidePanel.add(pnlIcons, rsc);
        
        JPanel pnlDiameter = new JPanel();
        GridBagLayout diameterLayout = new GridBagLayout();
        //pnlDiameter.setLayout(diameterLayout);
        GridBagConstraints diameterConstraints = new GridBagConstraints();
        diameterConstraints.gridx = 0;
        diameterConstraints.gridy = 0;
        diameterConstraints.gridwidth = 1;
        diameterConstraints.gridheight = 1;
        diameterConstraints.weighty = 0.1;
        diameterConstraints.weightx = 0.1;
        diameterConstraints.ipadx = 2;
        diameterConstraints.ipady = 2;
        diameterConstraints.fill = GridBagConstraints.HORIZONTAL;
        diameterConstraints.anchor = GridBagConstraints.CENTER;
        
        //Radius One
        diameterOne = new IconPanel();
        diameterOne.setToolTipText("Diameter (1) - One Tile at a Time");
        diameterOne.setVisible(true);
        diameterOne.setMinimumSize(new Dimension(24, 24));
        diameterOne.setPreferredSize(new Dimension(24, 24));
        diameterOne.setMaximumSize(new Dimension(24, 24));
        diameterOne.setSize(new Dimension(24, 24));
        diameterOne.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e)
            {
                setDiameterImages(1);
            }
        });
        //The "0" means that this happens when purely T is pressed.  In the actionPerformed I make sure a text box
        //isn't selected.  Thus I get single-key shortcuts, booyeah!
        diameterOne.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('1', 0), "diameterOne");
        diameterOne.getActionMap().put("diameterOne", new AbstractAction(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() instanceof SuperJTextField)
                    return;
                setDiameterImages(1);
            }
        });
        pnlDiameter.add(diameterOne, diameterConstraints);
        
        try{
            onePNG = ImageIO.read(new File(Main.imagePath + "RadiusOne.PNG"));
            oneActivePNG = ImageIO.read(new File(Main.imagePath + "RadiusOneActive.PNG"));
        }
        catch(IOException e)
        {
            logger.error("ImageIO error on RadiusOne.PNG", e);
        }
        
        
        //Radius Three
        diameterConstraints.gridx = 1;
        diameterThree = new IconPanel();
        diameterThree.setToolTipText("Diameter (3) - Nine tiles at a Time");
        diameterThree.setVisible(true);
        diameterThree.setMinimumSize(new Dimension(24, 24));
        diameterThree.setPreferredSize(new Dimension(24, 24));
        diameterThree.setMaximumSize(new Dimension(24, 24));
        diameterThree.setSize(new Dimension(24, 24));
        diameterThree.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e)
            {
                setDiameterImages(3);
            }
        });
        //The "0" means that this happens when purely T is pressed.  In the actionPerformed I make sure a text box
        //isn't selected.  Thus I get single-key shortcuts, booyeah!
        diameterThree.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('3', 0), "diameterThree");
        diameterThree.getActionMap().put("diameterThree", new AbstractAction(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() instanceof SuperJTextField)
                    return;
                setDiameterImages(3);
            }
        });
        pnlDiameter.add(diameterThree, diameterConstraints);
        
        try{
            threePNG = ImageIO.read(new File(Main.imagePath + "RadiusThree.PNG"));
            threeActivePNG = ImageIO.read(new File(Main.imagePath + "RadiusThreeActive.PNG"));
        }
        catch(IOException e)
        {
            logger.error("ImageIO error on RadiusThree.PNG", e);
        }
        
        
        //Radius Five
        diameterConstraints.gridx = 2;
        diameterFive = new IconPanel();
        diameterFive.setToolTipText("Diameter (5) - 25 Tiles at a Time");
        diameterFive.setVisible(true);
        diameterFive.setMinimumSize(new Dimension(24, 24));
        diameterFive.setPreferredSize(new Dimension(24, 24));
        diameterFive.setMaximumSize(new Dimension(24, 24));
        diameterFive.setSize(new Dimension(24, 24));
        diameterFive.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e)
            {
                setDiameterImages(5);
            }
        });
        //The "0" means that this happens when purely T is pressed.  In the actionPerformed I make sure a text box
        //isn't selected.  Thus I get single-key shortcuts, booyeah!
        diameterFive.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('5', 0), "diameterFive");
        diameterFive.getActionMap().put("diameterFive", new AbstractAction(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() instanceof SuperJTextField)
                    return;
                setDiameterImages(5);
            }
        });
        pnlDiameter.add(diameterFive, diameterConstraints);
        
        try{
            fivePNG = ImageIO.read(new File(Main.imagePath + "RadiusFive.PNG"));
            fiveActivePNG = ImageIO.read(new File(Main.imagePath + "RadiusFiveActive.PNG"));
        }
        catch(IOException e)
        {
            logger.error("ImageIO error on FiveThree.PNG", e);
        }
        
        
        //Radius Three
        diameterConstraints.gridx = 3;
        diameterSeven = new IconPanel();
        diameterSeven.setToolTipText("Diameter (7) - 49 Tiles at a Time");
        diameterSeven.setVisible(true);
        diameterSeven.setMinimumSize(new Dimension(24, 24));
        diameterSeven.setPreferredSize(new Dimension(24, 24));
        diameterSeven.setMaximumSize(new Dimension(24, 24));
        diameterSeven.setSize(new Dimension(24, 24));
        diameterSeven.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e)
            {
                setDiameterImages(7);
            }
        });
        //The "0" means that this happens when purely T is pressed.  In the actionPerformed I make sure a text box
        //isn't selected.  Thus I get single-key shortcuts, booyeah!
        diameterSeven.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('7', 0), "diameterSeven");
        diameterSeven.getActionMap().put("diameterSeven", new AbstractAction(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() instanceof SuperJTextField)
                    return;
                setDiameterImages(7);
            }
        });
        pnlDiameter.add(diameterSeven, diameterConstraints);
        
        try{
            sevenPNG = ImageIO.read(new File(Main.imagePath + "RadiusSeven.PNG"));
            sevenActivePNG = ImageIO.read(new File(Main.imagePath + "RadiusSevenActive.PNG"));
        }
        catch(IOException e)
        {
            logger.error("ImageIO error on RadiusSeven.PNG", e);
        }
        
        rsc.gridwidth = 5;
        rsc.gridx = 0;
        rsc.gridy = 2;
        rsc.fill = GridBagConstraints.HORIZONTAL;
        rightSidePanel.add(pnlDiameter, rsc);

        rsc.gridx = 0;
        rsc.gridy = 3;
        rsc.gridwidth = 5;
        rsc.weightx = 0;
        rsc.fill = GridBagConstraints.NONE;
        tileImage.setMinimumSize(new Dimension(128, 64));
        tileImage.setPreferredSize(new Dimension(128, 88));
        tileImage.setMaximumSize(new Dimension(128, 88));
        tileImage.setSize(new Dimension(128, 64));
        tileImage.setBorder(new LineBorder(Color.BLACK));
        tileImage.setVisible(true);
        rightSidePanel.add(tileImage, rsc);

        rsc.ipadx = 0;
        rsc.gridy++;
        rsc.fill = GridBagConstraints.HORIZONTAL;
        rightSidePanel.add(tileName, rsc);
        rsc.fill = GridBagConstraints.NONE;

        //OWNER PANEL

        GridBagConstraints opc = new GridBagConstraints();
        opc.gridx = opc.gridy = 0;
        opc.gridwidth = 1;
        opc.gridheight = 1;
        opc.weighty = 0.1;
        opc.weightx = 0;
        opc.ipadx = 4;
        opc.fill = GridBagConstraints.NONE;
        cityPanel.setBorder(BorderFactory.createTitledBorder("Owner"));
        GridBagLayout ownerPanelLayout = new GridBagLayout();
        ownerPanel.setLayout(ownerPanelLayout);
        
        rbtnOwnerNone.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbtnOwnerNoneAction();
            }
        });
        ownerPanel.add(rbtnOwnerNone, opc);
        opc.gridy++;

        rbtnOwnerBarbarians.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbtnOwnerBarbariansAction();
            }
        });
        ownerPanel.add(rbtnOwnerBarbarians, opc);
        opc.gridy++;
        rbtnOwnerPlayer.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbtnOwnerPlayerAction();
            }
        });
        ownerPanel.add(rbtnOwnerPlayer, opc);
        opc.gridy++;
        rbtnOwnerCiv.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbtnOwnerCivAction();
            }
        });
        ownerPanel.add(rbtnOwnerCiv, opc);
        opc.gridy++;
        cmbOwner.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbOwnerAction();
            }
        });
        ownerPanel.add(cmbOwner, opc);
        //add it to panel
        rsc.gridx = 0;
        rsc.gridy++;
        rsc.gridheight = 3;  //TO BE ADJUSTED AS CITY PANEL EXPANDS
        rsc.weighty = 0.5;
        rsc.fill = GridBagConstraints.VERTICAL;     //MAY NEED ADJUSTED TO BOTH
        rightSidePanel.add(ownerPanel, rsc);

        //CITY-SPECIFIC STUFFS
        GridBagConstraints cpc = new GridBagConstraints();
        cpc.gridx = 0;
        cpc.gridy = 0;
        cpc.gridwidth = 1;
        cpc.gridheight = 1;
        cpc.weighty = 0.1;
        cpc.weightx = 0;
        cpc.ipadx = 4;
        cpc.fill = GridBagConstraints.NONE;

        cityPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        GridBagLayout cityPanelLayout = new GridBagLayout();
        cityPanel.setLayout(cityPanelLayout);

        JLabel population = new JLabel("Population:");
        cpc.gridwidth = 2;
        cpc.gridheight = 1;
        cpc.gridy = 0;
        cpc.gridx = 0;
        cityPanel.add(population, cpc);

        cpc.gridwidth = 3;
        cpc.gridheight = 1;
        cpc.gridx = 2;
        //For the population spinner, if we change cities (the null event),
        //we need to grab any potential typed value before updating the population.
        spnrPopulation.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent evt)
            {
                if (evt == null)    //city change
                {
                    try{
                        spnrPopulation.commitEdit();
                        updatePopulation();
                    }
                    catch(ParseException e)
                    {
                        logger.error("Parse error - user probably intered a non-integer population value", e);
                    }
                }
                else    //arrows or enter key
                {
                    updatePopulation();
                }
            }
        });
        cityPanel.add(spnrPopulation, cpc);

        JLabel cultLabel = new JLabel("Culture:");
        cpc.gridwidth = 2;
        cpc.gridheight = 1;
        cpc.gridy++;
        cpc.gridx = 0;
        cityPanel.add(cultLabel, cpc);

        cpc.gridwidth = 3;
        cpc.gridheight = 1;
        cpc.gridx = 2;
        /**
         * Our change listener is a bit wonky, on purpose.  Here's what it does:
         * If the user changes cities, it stores any TYPED value and recalculates
         * culture (ALWAYS).
         * If the user uses the arrows to change culture or hits the enter key
         * after typing a value, it only recalculates the culture if the
         * logarithm (base 10) of the culture changes from the previous value.
         * Note that although changing the culture with arrows/enter (without
         * changing the logarithm of the culture) MAY change borders, that
         * sort of change will only be recalculated after the city is changed.
         * This is to increase efficiency.  Recalculating after every single
         * change with up/down arrows makes them too slow to be useful, but
         * a change of logarithm for the culture almost surely changes the
         * cultural boundaries, so we do recalculate then.
         * I have not done border-testing with non-10 border changes (if that
         * is possible).
         */
        spnrCulture.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent evt)
            {
                if (evt == null)    //city change
                {
                    logger.info("Change of cities, culture calculating");
                    try{
                        spnrCulture.commitEdit();
                        updateCulture(((SpinnerNumberModel)spnrCulture.getModel()).getNumber().intValue());
                    }
                    catch(ParseException e)
                    {
                        logger.error("Parse error - user probably intered a non-integer culture value", e);
                    }
                }
                else    //arrows or enter key
                {
                    int nCulture = ((SpinnerNumberModel)spnrCulture.getModel()).getNumber().intValue();
                    int nLog = log10(nCulture);
                    int oCulture = oldCulture;
                    int oLog = log10(oCulture);
                    if (nLog != oLog)
                        updateCulture(((SpinnerNumberModel)spnrCulture.getModel()).getNumber().intValue());
                    oldCulture = ((SpinnerNumberModel)spnrCulture.getModel()).getNumber().intValue();
                }
            }
        });
        cityPanel.add(spnrCulture, cpc);

        JLabel lblBuildings = new JLabel("Buildings");
        cpc.gridx = 0;
        cpc.gridwidth = 5;
        cpc.gridy++;
        cityPanel.add(lblBuildings, cpc);

        lstBuildings.setModel(buildingsModel);
        lstBuildings.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        cpc.gridy++;
        lstBuildings.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e)
            {
                updateCityBuildings();
            }
        });
        buildingsScroll.setViewportView(lstBuildings);
        cpc.weighty = 0.5;
        cpc.gridheight = 5;
        cpc.gridx = 0;
        cpc.fill = GridBagConstraints.BOTH;
        cpc.anchor = GridBagConstraints.WEST;
        cityPanel.add(buildingsScroll, cpc);
        cpc.gridy+=5;
        cpc.gridheight=1;
        
        cmdRelocateCity.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                cmdRelocateCityAction(selectedTile);
            }
        });
        cityPanel.add(cmdRelocateCity, cpc);
        cpc.gridy++;
        
        cmdRelocateCityAndUnits.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                cmdRelocateCityAndUnitsAction(selectedTile);
            }
        });
        cityPanel.add(cmdRelocateCityAndUnits, cpc);
        
        
        cmdDeleteCity.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmdDeleteCityAction();
            }
        });

        cpc.gridy++;
        cityPanel.add(cmdDeleteCity, cpc);

        //Just do cityPanel.setVisible(false) to hide the city panel.  Magic!
        //cityPanel.setVisible(false);

        //Add the city panel to the right-side panel
        rsc.gridx = 0;
        rsc.gridy+=3;
        rsc.gridheight = 9;  //TO BE ADJUSTED AS CITY PANEL EXPANDS
        rsc.gridwidth = 5;
        rsc.weighty = 0.8;
        rsc.fill = GridBagConstraints.VERTICAL;     //MAY NEED ADJUSTED TO BOTH
        rightSidePanel.add(cityPanel, rsc);

        rsc.gridy++;
        rsc.gridwidth = 5;
        districtPanel.setBorder(BorderFactory.createTitledBorder("Districts"));
        GridBagLayout districtLayout = new GridBagLayout();
        districtPanel.setLayout(districtLayout);
        GridBagConstraints dgc = new GridBagConstraints();
        dgc.gridx = 0;
        dgc.gridy = 0;
        dgc.gridwidth = 1;
        dgc.insets = new Insets(2, 2, 2, 2);
        dgc.anchor = GridBagConstraints.WEST;
        lblDistrictHeader.setText("Districts");
        districtPanel.add(lblDistrictHeader, dgc);

        lstDistricts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstDistricts.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() || updatingDistrictSelection)
                    return;
                DistrictDefinitions.DistrictDefinition def = lstDistricts.getSelectedValue();
                if (def != null)
                    setDistrictForSelectedTile(def.id);
            }
        });

        JScrollPane districtScroll = new JScrollPane(lstDistricts);
        districtScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        dgc.gridy++;
        dgc.fill = GridBagConstraints.BOTH;
        dgc.weightx = 1;
        dgc.weighty = 1;
        districtPanel.add(districtScroll, dgc);

        lblWonderAssignment.setText("");
        dgc.gridy++;
        dgc.weighty = 0;
        dgc.fill = GridBagConstraints.HORIZONTAL;
        districtPanel.add(lblWonderAssignment, dgc);

        JPanel districtButtons = new JPanel(new GridBagLayout());
        GridBagConstraints dbc = new GridBagConstraints();
        dbc.gridx = 0;
        dbc.gridy = 0;
        dbc.insets = new Insets(2, 2, 2, 2);
        dbc.anchor = GridBagConstraints.WEST;
        btnAssignWonder.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                handleWonderAssignment();
            }
        });
        districtButtons.add(btnAssignWonder, dbc);

        dbc.gridx++;
        btnDeleteDistrict.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                deleteSelectedDistrict();
            }
        });
        districtButtons.add(btnDeleteDistrict, dbc);

        dgc.gridy++;
        districtPanel.add(districtButtons, dgc);

        btnAssignWonder.setEnabled(false);
        btnAssignWonder.setVisible(false);
        btnDeleteDistrict.setEnabled(false);
        districtPanel.setVisible(false);
        rightSidePanel.add(districtPanel, rsc);
        
        cmbTribe = new JComboBox();
        cmbTribe.setModel(tribeModel);
        //Part of above hack - prevents jcombobox from becoming too big
        cmbTribe.setPreferredSize(new Dimension(150, 20));
        rsc.gridy+=9;
        rsc.gridx = 0;
        rsc.gridheight = 1;
        rsc.gridwidth = 2;
        rsc.weighty = 0.05;
        rightSidePanel.add(lblTribe, rsc);
        rsc.gridwidth = 3;
        rsc.gridx = 2;
        rsc.fill = GridBagConstraints.NONE;
        cmbTribe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                if (mapIsLive) {
                    if (cmbTribe.getSelectedIndex() == 0) {
                        selectedTile.setBarbarianTribe((short)75);
                    }
                    else {
                        selectedTile.setBarbarianTribe((short)(cmbTribe.getSelectedIndex() - 1));
                    }
                    map.triggerUpdates();
                }
            }
        });
        rightSidePanel.add(cmbTribe, rsc);

        JLabel lblResource = new JLabel("Resource");
        cmbResource = new JComboBox();
        cmbResource.setModel(resourceModel);
        //Part of above hack - prevents jcombobox from becoming too big
        cmbResource.setPreferredSize(new Dimension(150, 20));
        rsc.gridy++;
        rsc.gridx = 0;
        rsc.gridheight = 1;
        rsc.gridwidth = 2;
        rsc.weighty = 0.05;
        rightSidePanel.add(lblResource, rsc);
        rsc.gridwidth = 3;
        rsc.gridx = 2;
        rsc.fill = GridBagConstraints.NONE;
        cmbResource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                if (mapIsLive) {
                    //minus one b/c of the "None"
                    selectedTile.resourceInt = cmbResource.getSelectedIndex() - 1;
                    map.triggerUpdates();
                }
            }
        });
        rightSidePanel.add(cmbResource, rsc);

        JLabel lblRivers = new JLabel("Rivers");
        rsc.gridy++;
        rsc.gridx = 0;
        rightSidePanel.add(lblRivers, rsc);
        rsc.gridy++;
        chkNW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkNWAction();
            }
        });
        rightSidePanel.add(chkNW, rsc);
        rsc.gridx+=2;
        chkNE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkNEAction();
            }
        });
        rightSidePanel.add(chkNE, rsc);
        rsc.gridy++;
        rsc.gridx-=2;
        chkSW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkSWAction();
            }
        });
        rightSidePanel.add(chkSW, rsc);
        rsc.gridx+=2;
        chkSE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkSEAction();
            }
        });
        rightSidePanel.add(chkSE, rsc);


        JLabel lblOverlays = new JLabel("Overlays");
        chkRoad = new JCheckBox("Road");
        addActionListener(chkRoad, TILE.ROAD_MASK);
        chkRailroad = new JCheckBox("Railroad");
        addActionListener(chkRailroad, TILE.RAILROAD_MASK);
        chkIrrigation = new JCheckBox("Irrigation");
        addActionListener(chkIrrigation, TILE.IRRIGATION_MASK);
        chkMine = new JCheckBox("Mine");
        addActionListener(chkMine, TILE.MINE_MASK);
        chkFortress = new JCheckBox("Fortress");
        addActionListener(chkFortress, TILE.FORT_MASK);
        chkBarricade = new JCheckBox("Barricade");
        addActionListener(chkBarricade, TILE.BARRICADE_MASK);
        chkBarbarians = new JCheckBox("Barbarian Camp");
        chkBarbarians.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lblTribe.setVisible(chkBarbarians.isSelected());
                cmbTribe.setVisible(chkBarbarians.isSelected());
                selectedTile.setBarbarianCamp(chkBarbarians.isSelected());
                map.triggerUpdates();
            }
        });
        addActionListener(chkBarbarians, TILE.BARBARIAN_CAMP_MASK);
        chkHut = new JCheckBox("Goody Hut");
        addActionListener(chkHut, TILE.GOODY_HUT_MASK);
        chkPollution = new JCheckBox("Pollution");
        addActionListener(chkPollution, TILE.POLLUTION_MASK);
        chkCraters = new JCheckBox("Craters");
        addActionListener(chkCraters, TILE.CRATER_MASK);
        chkLandmark = new JCheckBox("Landmark");
        //swing a custom one
        chkLandmark.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                if (chkLandmark.isSelected())
                    selectedTile.C3CBonuses = selectedTile.C3CBonuses ^ 0x2000;
                else
                {
                    selectedTile.C3CBonuses = selectedTile.C3CBonuses & (~0x2000);
                }
                map.triggerUpdates();
                alertToSquarePress(selectedTile.xPos, selectedTile.yPos);
            }
        });
        chkAirfield = new JCheckBox("Airfield");
        chkRadarTower = new JCheckBox("Radar Tower");
        chkOutpost = new JCheckBox("Outpost");
        chkColony = new JCheckBox("Colony");
        chkStartingLocation = new JCheckBox("Starting Location");
        chkRuins = new JCheckBox("Ruins");
        chkVictoryLocation = new JCheckBox("Victory Location");
        rsc.gridy++;
        rsc.gridx = 0;
        rightSidePanel.add(lblOverlays, rsc);
        rsc.fill = GridBagConstraints.HORIZONTAL;
        int secondColOffset = 3;

        rsc.weighty = 0;
        rsc.gridy++;
        rightSidePanel.add(chkRoad, rsc);
        rsc.gridx+=secondColOffset;
        rightSidePanel.add(chkRailroad, rsc);
        rsc.gridy++;
        rsc.gridx-=secondColOffset;
        rightSidePanel.add(chkIrrigation, rsc);
        rsc.gridx+=secondColOffset;
        rightSidePanel.add(chkMine, rsc);
        rsc.gridy++;
        rsc.gridx-=secondColOffset;
        rightSidePanel.add(chkFortress, rsc);
        rsc.gridx+=secondColOffset;
        rightSidePanel.add(chkBarricade, rsc);
        rsc.gridy++;
        rsc.gridx-=secondColOffset;
        rightSidePanel.add(chkBarbarians, rsc);
        rsc.gridx+=secondColOffset;
        rightSidePanel.add(chkHut, rsc);
        rsc.gridy++;
        rsc.gridx-=secondColOffset;
        chkAirfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                colonyCheckAction(CLNY.AIRFIELD);
            }
        });
        rightSidePanel.add(chkAirfield, rsc);
        rsc.gridx+=secondColOffset;
        chkRadarTower.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                colonyCheckAction(CLNY.RADAR_TOWER);
            }
        });
        rightSidePanel.add(chkRadarTower, rsc);
        rsc.gridy++;
        rsc.gridx-=secondColOffset;
        chkOutpost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                colonyCheckAction(CLNY.OUTPOST);
            }
        });
        rightSidePanel.add(chkOutpost, rsc);
        rsc.gridx+=secondColOffset;
        chkColony.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                colonyCheckAction(CLNY.COLONY);
            }
        });
        rightSidePanel.add(chkColony, rsc);
        rsc.gridy++;
        rsc.gridx-=secondColOffset;
        chkStartingLocation.addActionListener(new java.awt.event.ActionListener()
        {

            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkStartingLocationAction();
            }
        });
        rightSidePanel.add(chkStartingLocation, rsc);
        rsc.gridx+=secondColOffset;
        chkRuins.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                if (chkRuins.isSelected())
                    selectedTile.ruin = TILE.RUINS_PRESENT;
                else
                {
                    selectedTile.ruin = TILE.RUINS_NOT_PRESENT;
                }
                map.triggerUpdates();
            }
        });
        rightSidePanel.add(chkRuins, rsc);
        rsc.gridy++;
        rsc.gridx-=secondColOffset;
        chkVictoryLocation.addActionListener(new java.awt.event.ActionListener()
        {

            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                if (chkVictoryLocation.isSelected())
                    selectedTile.setVictoryPointLocation(TILE.VICTORY_POINT_LOCATION_PRESENT);
                else
                    selectedTile.setVictoryPointLocation(TILE.VICTORY_POINT_LOCATION_NOT_PRESENT);
                map.triggerUpdates();
            }
        });
        rightSidePanel.add(chkVictoryLocation, rsc);
        rsc.gridx+=secondColOffset;
        rightSidePanel.add(chkPollution, rsc);
        rsc.gridy++;
        rsc.gridx-=secondColOffset;
        rightSidePanel.add(chkCraters, rsc);
        rsc.gridx+=secondColOffset;
        rightSidePanel.add(chkLandmark, rsc);

        JLabel lblUnits = new JLabel("Units");
        rsc.gridy++;
        rsc.gridx = 0;
        rsc.fill = GridBagConstraints.NONE;
        rsc.weighty = 0.05;
        //rightSidePanel.add(lblUnits, rsc);

        JPanel pnlAddUnits = new JPanel();
        TitledBorder unitPanelBorder = new TitledBorder("Add Units");
        pnlAddUnits.setBorder(unitPanelBorder);
        GridBagLayout newUnitLayout = new GridBagLayout();
        pnlAddUnits.setLayout(newUnitLayout);
        GridBagConstraints newUnitConstraints = new GridBagConstraints();
        newUnitConstraints.weighty = 0.1;
        newUnitConstraints.gridheight = 1;
        newUnitConstraints.gridwidth = 1;
        //newUnitConstraints.fill = GridBagConstraints.VERTICAL;


        JButton cmdAdd = new JButton("Add");
        newUnitConstraints.gridx = 0;
        newUnitConstraints.gridy = 0;
        newUnitConstraints.gridwidth = 1;
        cmdAdd.addActionListener(new java.awt.event.ActionListener()
        {
            /**
             * This is more complex than I thought b/c we can have invaders,
             * units in areas that there civ doesn't own.  We need to specify
             * this when adding units, too.
             */
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmdAddAction();
            }
        });
        pnlAddUnits.add(cmdAdd, newUnitConstraints);
        SpinnerNumberModel numberModel = new SpinnerNumberModel(1, 1, 8192, 1);
        spnNumber.setModel(numberModel);
        cmbUnitType.setModel(unitModel);
        cmbUnitExpr.setModel(exprModel);
        //rsc.fill = GridBagConstraints.HORIZONTAL;
        newUnitConstraints.gridx+=1;
        newUnitConstraints.gridwidth = 4;
        pnlAddUnits.add(spnNumber, newUnitConstraints);
        newUnitConstraints.gridx=0;
        newUnitConstraints.gridy++;
        newUnitConstraints.gridwidth = 2;
        cmbUnitExpr.setPreferredSize(new Dimension(80, 24));
        cmbUnitExpr.setMaximumSize(new Dimension(80, 24));
        pnlAddUnits.add(cmbUnitExpr, newUnitConstraints);
        newUnitConstraints.gridx+=2;
        newUnitConstraints.gridwidth = 3;
        cmbUnitType.setPreferredSize(new Dimension(150, 24));
        cmbUnitType.setMaximumSize(new Dimension(150, 24));
        pnlAddUnits.add(cmbUnitType, newUnitConstraints);

        //For owner
        unitOwnerPanel.setLayout(new GridBagLayout());
        GridBagConstraints uopc = new GridBagConstraints();
        ButtonGroup newUnitOwnerType = new ButtonGroup();
        newUnitOwnerType.add(rbtnNewUnitOwnerBarbarians);
        newUnitOwnerType.add(rbtnNewUnitOwnerPlayer);
        newUnitOwnerType.add(rbtnNewUnitOwnerCiv);

        uopc.gridy = 0;
        uopc.gridheight = 1;
        uopc.gridx = 0;
        uopc.gridwidth = 5;
        uopc.weighty = 0.1;
        unitOwnerPanel.add(rbtnNewUnitOwnerBarbarians, uopc);
        uopc.gridy++;
        unitOwnerPanel.add(rbtnNewUnitOwnerPlayer, uopc);
        uopc.gridy++;
        unitOwnerPanel.add(rbtnNewUnitOwnerCiv, uopc);
        uopc.gridy++;
        cmbNewUnitOwner.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                //cmbOwnerAction();
            }
        });
        cmbNewUnitOwner.setModel(mdlCivs);
        rbtnNewUnitOwnerBarbarians.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbNewUnitOwner.setVisible(false);
            }
        });
        rbtnNewUnitOwnerCiv.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbNewUnitOwner.setModel(mdlCivs);
                cmbNewUnitOwner.setSelectedIndex(1);
                cmbNewUnitOwner.setVisible(true);
            }
        });
        rbtnNewUnitOwnerPlayer.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbNewUnitOwner.setModel(mdlPlayers);
                cmbNewUnitOwner.setSelectedIndex(0);
                cmbNewUnitOwner.setVisible(true);
            }
        });
        rbtnNewUnitOwnerCiv.setSelected(true);
        unitOwnerPanel.add(cmbNewUnitOwner, uopc);
        newUnitConstraints.gridy++;
        newUnitConstraints.gridx = 0;
        newUnitConstraints.gridheight = 4;
        newUnitConstraints.weighty = 0.4;
        newUnitConstraints.gridwidth = 5;
        pnlAddUnits.add(unitOwnerPanel, newUnitConstraints);

        //and add that panel
        rsc.gridy++;
        rsc.gridx = 0;
        rsc.gridheight = 6;
        rsc.gridwidth = 5;
        rsc.weighty = 0.6;
        rsc.fill = GridBagConstraints.VERTICAL;
        rightSidePanel.add(pnlAddUnits, rsc);


        JPanel pnlExistingUnits = new JPanel();
        TitledBorder existingUnitsBorder = new TitledBorder("Add Units");
        pnlAddUnits.setBorder(existingUnitsBorder);
        GridBagLayout existingUnitsLayout = new GridBagLayout();
        pnlExistingUnits.setLayout(existingUnitsLayout);
        GridBagConstraints euc = new GridBagConstraints();
        euc.weighty = 0.1;
        euc.gridheight = 1;
        euc.gridwidth = 1;

        JScrollPane existingUnitsScroll = new JScrollPane();


        existingUnitModel.addColumn("NumUnits");  //num units
        existingUnitModel.addColumn("UnitType");  //unit type
        existingUnitModel.addColumn("Expr");  //expr
        existingUnitModel.addColumn("X");  //remove


        existingUnits = new JTable(existingUnitModel);
        existingUnits.setBackground(new Color(0,0,0,0));  //transparent
        existingUnits.getColumn("X").setCellRenderer(new ButtonInTableRenderer());
        existingUnits.getColumn("X").setCellEditor(new ButtonInTableEditor());
        existingUnits.getColumn("NumUnits").setCellRenderer(new JSpinnerInTableRenderer());
        existingUnits.getColumn("NumUnits").setCellEditor(new JSpinnerInTableEditor());
        existingUnits.getColumn("UnitType").setCellRenderer(new JLabelInTableRenderer());
        existingUnits.getColumn("UnitType").setCellEditor(new JLabelInTableEditor());
        existingUnits.getColumn("Expr").setCellRenderer(new ComboBoxInTableRenderer(mdlExprs));
        existingUnits.getColumn("Expr").setCellEditor(new ComboBoxInTableEditor(mdlExprs));

        TableColumnModel tcm = existingUnits.getColumnModel();
        TableColumn col = tcm.getColumn(0);
        col.setPreferredWidth(40);
        col = tcm.getColumn(1);
        col.setPreferredWidth(65);
        col = tcm.getColumn(2);
        col.setPreferredWidth(75);
        col = tcm.getColumn(3);
        col.setPreferredWidth(45);


        existingUnitsScroll.setViewportView(existingUnits);
        pnlExistingUnits.add(existingUnitsScroll, euc);
        //and add that panel
        rsc.gridy+=6;
        rsc.gridx = 0;
        rsc.gridheight = 2;
        rsc.gridwidth = 5;
        rsc.weighty = 0.1;
        rsc.fill = GridBagConstraints.VERTICAL;
        existingUnitModel.addTableModelListener(new TableModelListener(){
            public void tableChanged(TableModelEvent e) {
                tableChangedAction(e);

            }

        });
        rightSidePanel.add(existingUnits, rsc); //TODO: Maybe the scroll instead
        
        unitNameDetails.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUnitNameDetails();
            }
            
        });
        
        rsc.fill = GridBagConstraints.NONE;
        rsc.gridy+=2;
        rightSidePanel.add(unitNameDetails, rsc);
        
        //And now add the right-side panel
        rightSidePane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        g.weightx = rightSideWeight;
        g.weighty = 0.5;
        g.gridx = 2;
        g.ipadx = 4;
        add(rightSidePane, g);

        addActionListeners();
    }
    
    private void showUnitNameDetails() {
        
        List<UNIT> unitsOnTile = new ArrayList<UNIT>();
        if (selectedTile.unitsOnTile.size() > 0)
        {

            //to start with, to see if the display works, let's just go with each
            //unit gets an entry.  then we'll bucketize.
            for (int i = 0; i < selectedTile.unitsOnTile.size(); i++)
            {
                UNIT mapUnit = biq.mapUnit.get(selectedTile.unitsOnTile.get(i));
                unitsOnTile.add(mapUnit);
            }
        }
        
        //TODO: Do this just once
        unitNameWindow = new UnitNameWindow();
        unitNameWindow.setExpr(this.biq.experience);
        unitNameWindow.setPrtoList(this.biq.unit);
        
        unitNameWindow.setUnits(unitsOnTile);
        unitNameWindow.setVisible(true);
    }

    private void addActionListeners()
    {
        tileName.addKeyListener(new KeyListener(){
            //We want the 'released' event for key listeners.  The others fire
            //too early and don't pick up new letters
            public void keyTyped(KeyEvent e)
            {
            }
            public void keyPressed(KeyEvent e)
            {
            }
            public void keyReleased(KeyEvent e)
            {

                CITY city = biq.city.get(selectedTile.getCity());
                if (tileName.getText().length() < 24) {
                    city.setName(tileName.getText());
                }
                else {
                    String newName = tileName.getText().substring(0, 23);   //allow a null at the end
                    city.setName(newName);
                    tileName.setText(newName);
                }
            }
        });
    }

    /**
     * Shows permanent graphics on the right side panel.  So far this just includes
     * the icons for selecting different brushes.
     */
    public void showGraphics()
    {
        Graphics graph = cityIcon.getGraphics();
        if (graph != null)
        {
            if (logger.isDebugEnabled())
                logger.debug("Doing stuff with cityPNG - in ShowGraphics");
            cityIcon.setImage(cityPNG);
            cityIcon.update(graph);
            graph.dispose();
        }


        graph = overlayIcon.getGraphics();
        if (graph != null)
        {
            overlayIcon.setImage(overlayPNG);
            overlayIcon.update(graph);  
            graph.dispose();
        }


        graph = unitIcon.getGraphics();
        if (graph != null)
        {
            unitIcon.setImage(unitPNG);
            unitIcon.update(graph); 
            graph.dispose();
        }


        graph = terrainIcon.getGraphics();
        if (graph != null)
        {
            terrainIcon.setImage(terrainPNG);
            terrainIcon.update(graph);
            graph.dispose();
        }
        
        
        graph = fogIcon.getGraphics();
        if (graph != null)
        {
            fogIcon.setImage(fogPNG);
            fogIcon.update(graph);
            graph.dispose();
        }
        
        graph = diameterOne.getGraphics();
        if (graph != null)
        {
            diameterOne.setImage(oneActivePNG);
            diameterOne.update(graph);
            graph.dispose();
        }
        
        graph = diameterThree.getGraphics();
        if (graph != null)
        {
            diameterThree.setImage(threePNG);
            diameterThree.update(graph);
            graph.dispose();
        }
        
        graph = diameterFive.getGraphics();
        if (graph != null)
        {
            diameterFive.setImage(fivePNG);
            diameterFive.update(graph);
            graph.dispose();
        }
        
        graph = diameterSeven.getGraphics();
        if (graph != null)
        {
            diameterSeven.setImage(sevenPNG);
            diameterSeven.update(graph);
            graph.dispose();
        }

        graph = selectIcon.getGraphics();
        if (graph != null)
        {
            selectIcon.setImage(selectActive);
            selectIcon.update(graph);
            graph.dispose();
        }
    }

    /**
     * Lets the map <i>tab</i> know that a tile was pressed.  This method is
     * called by the map <i>panel</i>, which will be the first to know that
     * a tile was pressed.  The tab itself needs to know so that the right-side
     * panel can be updated as appropriate.
     * @param x - The x position of the tile that was pressed.
     * @param y - The y position of the tile that was pressed.y
     */
    public void alertToSquarePress(int x, int y)
    {
        /**
         * True if someone has definite control of the tile.  Thus, in this
         * sense, a tile can be owned by someone other than the civ who controls
         * the political boundaries there (i.e. foreign troops can claim ownership
         * of a tile).
         */
        boolean tileOwnedBySomeone = false;


        if (logger.isDebugEnabled())
            logger.debug("X: " + x + ", Y: " + y);
        tileImage.setImage(map.drawTile(x, y));
        tileImage.repaint();
        if (!borderHidden)
        {
            tileImage.setBorder(BorderFactory.createEmptyBorder());
            borderHidden = true;
        }
        //Update the culture and population spinners.  THIS IS COMPLICATED!!!
        //Before we change the selected tile, we need to update the culture value
        //for the current city.  If the user was using the up/down arrows, the
        //new value will already be updated (via the spinner's stateChanged
        //method).  If not, we need to update.  To keep this all in the stateChanged
        //method, we send a null state changed.  Continue reading at the
        //action listener for the spinner.
        if (selectedTile != null)
            if (selectedTile.getCity() != -1)
            {
                spnrCulture.getChangeListeners()[0].stateChanged(null);
                spnrPopulation.getChangeListeners()[0].stateChanged(null);
            }

        logger.info("x: " + x + ", y: " + y);
        int calculatedIndex = biq.calculateTileIndex(x, y);
        logger.info("Calculated index: " + calculatedIndex);
        selectedTile = biq.tile.get(calculatedIndex);

        String terrainType = biq.terrain.get(selectedTile.getRealTerrain()).name;
        //1/26/13 Landmark terrain now shows up on map tab.
        if (selectedTile.isLandmark())
        {
            terrainType = biq.terrain.get(selectedTile.getRealTerrain()).landmarkName;
        }
        tileName.setText(terrainType + " at " + x + ", " + y);
        
        if (selectedTile.getBarbarianTribe() != -1) {
            if (selectedTile.getBarbarianTribe() == TILE.RANDOM_BARBARIAN_TRIBE) {
                cmbTribe.setSelectedIndex(0);
            }
            else {
                cmbTribe.setSelectedIndex(selectedTile.getBarbarianTribe() + 1);
            }
            cmbTribe.setVisible(true);
            lblTribe.setVisible(true);
        }
        else {
            cmbTribe.setVisible(false);
            lblTribe.setVisible(false);
        }

        cmbResource.setSelectedIndex(selectedTile.resourceInt + 1);    //+ 1 b/c -1 in tile.resource indicates "None"

        //NECESSARY: That the check boxes are in the same order as the masks are
        //in TILE.java
        JCheckBox[]c3cOverlayChecks = {chkRoad, chkRailroad, chkMine, chkIrrigation, chkFortress, chkHut, chkPollution, chkBarbarians,
            chkCraters, chkBarricade};

        //Svelte check-setting: if the condition is true, it will be set, otherwise, unset
        for (int i = 0; i < c3cOverlayChecks.length; i++)
        {
            c3cOverlayChecks[i].setSelected((selectedTile.C3COverlays & TILE.c3cOverlayMasks[i]) == TILE.c3cOverlayMasks[i]);
        }

        chkRuins.setSelected(selectedTile.ruin == TILE.RUINS_PRESENT);

        if (selectedTile.getColony() != -1) //then there's a colony/airfield/radar tower/outpost
        {   //TODO: Add owner changeability
            int colIndex = selectedTile.getColony();
            int colonyType = biq.colony.get(colIndex).getImprovementType();
            chkColony.setSelected(colonyType == CLNY.COLONY);
            chkOutpost.setSelected(colonyType == CLNY.OUTPOST);
            chkRadarTower.setSelected(colonyType == CLNY.RADAR_TOWER);
            chkAirfield.setSelected(colonyType == CLNY.AIRFIELD);
        }
        else
        {
            chkColony.setSelected(false);
            chkOutpost.setSelected(false);
            chkRadarTower.setSelected(false);
            chkAirfield.setSelected(false);
        }

        chkVictoryLocation.setSelected(selectedTile.getVictoryPointLocation() == TILE.VICTORY_POINT_LOCATION_PRESENT);

        chkStartingLocation.setSelected((selectedTile.C3CBonuses & TILE.PLAYER_START_MASK) == TILE.PLAYER_START_MASK);

        //River connections
        /**
         * Note that this is different than what we have in MapPanel.java.  There,
         * we were calculating what image to use at the INTERSECTION of four tiles.  Here, we only care
         * about the rivers at one individual tile.  This actually makes it much simpler.
         * 
         * Do note, however, that we'll have to update two tiles for every change.  For example, if
         * we delete a river to the NW of this tile, we'll also have to delete the SE connection in the
         * tile NW of this tile.  Otherwise the results will be unpredictable.
         */
        boolean riverNW = false;
        boolean riverNE = false;
        boolean riverSE = false;
        boolean riverSW = false;
        if ((selectedTile.getRiverConnectionInfo() & TILE.RIVER_SOUTHWEST) == TILE.RIVER_SOUTHWEST)
            riverSW = true;
        if ((selectedTile.getRiverConnectionInfo() & TILE.RIVER_NORTHWEST) == TILE.RIVER_NORTHWEST)
            riverNW = true;
        if ((selectedTile.getRiverConnectionInfo() & TILE.RIVER_SOUTHEAST) == TILE.RIVER_SOUTHEAST)
            riverSE = true;
        if ((selectedTile.getRiverConnectionInfo() & TILE.RIVER_NORTHEAST) == TILE.RIVER_NORTHEAST)
            riverNE = true;
        
        chkNW.setSelected(riverNW);
        chkNE.setSelected(riverNE);
        chkSW.setSelected(riverSW);
        chkSE.setSelected(riverSE);

        if (selectedTile.getCity() != -1)
        {
            CITY city = biq.city.get(selectedTile.getCity());
            spnrPopulation.setValue(city.getSize());
            spnrCulture.setValue(city.getCulture());
            oldCulture = city.getCulture();
            this.tileName.setText(city.getName());
            switch(city.getOwnerType())
            {
                case CITY.OWNER_CIV:
                    rbtnOwnerCiv.setSelected(true);
                    cmbOwner.setVisible(true);
                    cmbOwner.setModel(mdlCivs);
                    cmbOwnerCultureFire = false;
                    cmbOwner.setSelectedIndex(selectedTile.owner);
                    cmbOwnerCultureFire = true;
                    break;
                case CITY.OWNER_PLAYER:
                    //FIXED: Fix case where this can be out of bounds, because the list only contains playable players
                    //Bug exhibited in MEM Crusader States
                    //^^June 30, 2011: Previous fixes in version 0.73 (beta) seem to have fixed this vs. 0.72 in testing done today.
                    rbtnOwnerPlayer.setSelected(true);
                    cmbOwner.setVisible(true);
                    cmbOwner.setModel(mdlPlayers);
                    cmbOwnerCultureFire = false;
                    cmbOwner.setSelectedIndex(selectedTile.owner);
                    cmbOwnerCultureFire = true;
                    break;
                case CITY.OWNER_BARBARIANS:
                    rbtnOwnerBarbarians.setSelected(true);
                    cmbOwner.setVisible(false);
                    break;
                default:
                    logger.warn("Invalid city owner");
                    break;
            }
            //buildings
            int[]buildingsPresent = new int[city.getNumBuildings()];
            for (int i = 0; i < city.getNumBuildings(); i++)
            {
                buildingsPresent[i] = city.getBuildingID(i);
            }
            lstBuildings.setSelectedIndices(buildingsPresent);

            tileOwnedBySomeone = true;
            tileName.setEditable(true);
            cityPanel.setVisible(true);
            rbtnOwnerNone.setVisible(false);
            unitOwnerPanel.setVisible(false);
        }
        else if (selectedTile.getColony() != -1)
        {
            CLNY colony = biq.colony.get(selectedTile.getColony());
            switch(colony.getOwnerType())
            {
                case CITY.OWNER_CIV:
                    rbtnOwnerCiv.setSelected(true);
                    cmbOwner.setVisible(true);
                    cmbOwner.setModel(mdlCivs);
                    cmbOwner.setSelectedIndex(colony.getOwner());
                    break;
                case CITY.OWNER_PLAYER:
                    rbtnOwnerPlayer.setSelected(true);
                    cmbOwner.setVisible(true);
                    cmbOwner.setModel(mdlPlayers);
                    cmbOwner.setSelectedIndex(colony.getOwner());
                    break;
                case CITY.OWNER_BARBARIANS:
                    rbtnOwnerBarbarians.setSelected(true);
                    cmbOwner.setVisible(false);
                    break;
                default:
                    logger.warn("Invalid city owner");
                    break;
            }
            tileOwnedBySomeone = true;
            tileName.setEditable(false);
            cityPanel.setVisible(false);
            rbtnOwnerNone.setVisible(false);
            unitOwnerPanel.setVisible(false);
        }
        else if (selectedTile.unitsOnTile.size() > 0)   //it has units
        {
            UNIT firstUnit = biq.mapUnit.get(selectedTile.unitsOnTile.get(0));
            switch(firstUnit.getOwnerType())
            {
                case CITY.OWNER_CIV:
                    rbtnOwnerCiv.setSelected(true);
                    cmbOwner.setVisible(true);
                    cmbOwner.setModel(mdlCivs);
                    cmbOwner.setSelectedIndex(firstUnit.getOwner());
                    break;
                case CITY.OWNER_PLAYER:
                    rbtnOwnerPlayer.setSelected(true);
                    cmbOwner.setVisible(true);
                    cmbOwner.setModel(mdlPlayers);
                    cmbOwner.setSelectedIndex(firstUnit.getOwner());
                    break;
                case CITY.OWNER_BARBARIANS:
                    rbtnOwnerBarbarians.setSelected(true);
                    cmbOwner.setVisible(false);
                    break;
                default:
                    logger.warn("Invalid city owner");
                    break;
            }
            tileOwnedBySomeone = true;
            tileName.setEditable(false);
            cityPanel.setVisible(false);
            rbtnOwnerNone.setVisible(false);
            unitOwnerPanel.setVisible(false);

        }
        else    //see if it has slocs
        {
            for (int i = 0; i < biq.startingLocation.size(); i++)
            {
                if (biq.startingLocation.get(i).getX() == selectedTile.xPos)
                    if (biq.startingLocation.get(i).getY() == selectedTile.yPos)
                    {
                        SLOC startingLocation = biq.startingLocation.get(i);
                        switch(startingLocation.getOwnerType())
                        {
                            case CITY.OWNER_CIV:
                                rbtnOwnerCiv.setSelected(true);
                                cmbOwner.setVisible(true);
                                cmbOwner.setModel(mdlCivs);
                                cmbOwner.setSelectedIndex(startingLocation.getOwner());
                                break;
                            case CITY.OWNER_PLAYER:
                                rbtnOwnerPlayer.setSelected(true);
                                cmbOwner.setVisible(true);
                                cmbOwner.setModel(mdlPlayers);
                                //Handle the case where there is no custom player data, but
                                //it's player-owned (i.e. the model will be empty)
                                int index = startingLocation.getOwner();
                                if (index < cmbOwner.getModel().getSize()) {
                                    cmbOwner.setSelectedIndex(startingLocation.getOwner());
                                }
                                break;
                            case CITY.OWNER_BARBARIANS:
                                rbtnOwnerBarbarians.setSelected(true);
                                cmbOwner.setVisible(false);
                                break;
                            case CITY.OWNER_NONE:
                                rbtnOwnerNone.setSelected(true);
                                cmbOwner.setVisible(false);
                                break;
                            default:
                                logger.warn("Invalid city owner");
                                break;
                        }
                        tileOwnedBySomeone = true;
                        rbtnOwnerNone.setVisible(true);
                        ownerPanel.setVisible(true);
                        unitOwnerPanel.setVisible(false);
                    }
            }
            tileName.setEditable(false);
            cityPanel.setVisible(false);
            unitOwnerPanel.setVisible(false);
        }
        
        //3/27/2023 - Allow city ownership by barbarians if the safety level is sufficiently low.
        if (selectedTile.getCity() != -1) {
            if (Main.safetyLevels.get("Map").ordinal() <= SafetyLevel.Minimal.ordinal()) {
                rbtnOwnerBarbarians.setEnabled(true);
            }
            else {
                rbtnOwnerBarbarians.setEnabled(false);
            }
        }
        else
            rbtnOwnerBarbarians.setEnabled(true);

        //landmark
        chkLandmark.setSelected(selectedTile.isLandmark());
        //TODO: see if we should be able to change that
        
        //units

        updateExistingUnitModel();
        
        ownerPanel.setVisible(tileOwnedBySomeone);
        unitOwnerPanel.setVisible(!tileOwnedBySomeone);
        
        unitNameDetails.setVisible(selectedTile.unitsOnTile.size() > 0);

    }

    private void updateExistingUnitModel()
    {
        int numRows = existingUnitModel.getRowCount();
        for (int i = 0; i < numRows; i++)
        {
            existingUnitModel.removeRow(0);
        }
        dataElements = new ArrayList<Object[]>();
        if (selectedTile.unitsOnTile.size() > 0)
        {

            //to start with, to see if the display works, let's just go with each
            //unit gets an entry.  then we'll bucketize.
            for (int i = 0; i < selectedTile.unitsOnTile.size(); i++)
            {
                UNIT mapUnit = biq.mapUnit.get(selectedTile.unitsOnTile.get(i));
                int alreadyThere = findMatchingUnitTypeOnTile(dataElements, biq.unit.get(mapUnit.getPRTONumber()).getName(), mapUnit.getExperienceLevel());
                if (alreadyThere == -1)
                {
                    ExistingUnitListElement element = new ExistingUnitListElement();
                    element.number = 1;
                    element.type = biq.unit.get(mapUnit.getPRTONumber()).getName();
                    element.experienceLevelSelected = mapUnit.getExperienceLevel();
                    Object[]data = new Object[5];
                    data[0] = element.number;
                    data[1] = element.type;
                    data[2] = element.experienceLevelSelected;
                    data[3] = "X";
                    //data[4] is a list of indices of the units on this tile
                    //Forget the <> type thing, we've already got an ArrayList of Arrays of Objects which are in part ArrayLists
                    ArrayList data4 = new ArrayList();
                    data[4] = data4;
                    data4.add(selectedTile.unitsOnTile.get(i));
                    if (logger.isTraceEnabled())
                        logger.trace("Adding row");
                    dataElements.add(data);
                }
                else
                {   //add one, with awkward syntax
                    //A few days later:  this is really bloody awkward syntax.
                    //clearly whoever made Java 1.2 didn't expect people to use
                    //array lists of arrays of Objects very often
                    (dataElements.get(alreadyThere))[0] = (Integer)((dataElements.get(alreadyThere))[0]) + 1;
                    ArrayList data4 = (ArrayList) (dataElements.get(alreadyThere))[4];
                    data4.add(selectedTile.unitsOnTile.get(i));
                }
            }
            for (int i = 0; i < dataElements.size(); i++)
            {
                existingUnitModel.addRow(dataElements.get(i));
            }
        }
        if (districtPanel.isVisible())
            updateDistrictPanel();
    }

    public void addPlayerToModel(String player) {
        mdlPlayers.addElement(player);
    }
    
    public void removePlayerFromModel(int index) {
        mdlPlayers.removeElementAt(index);
    }

    private int findMatchingUnitTypeOnTile(ArrayList<Object[]>dataElements, String name, int exprLevel)
    {

        for (int i = 0; i < dataElements.size(); i++)
        {
            Object[]element = dataElements.get(i);
            String type = (String)element[1];
            int num = (Integer)element[2];
            if (type.equals(name) && num == exprLevel)
                return i;
        }
        return -1;
    }

    private void addActionListener(final JCheckBox check, final int MASK)
    {
        check.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                if (check.isSelected())
                    selectedTile.C3COverlays = selectedTile.C3COverlays | MASK;
                else
                {
                    selectedTile.C3COverlays = selectedTile.C3COverlays & (~MASK);
                }
                map.triggerUpdates();
            }
        });
    }

    //TODO: Fix the situation where the update doesn't stick for population or culture if the user
    //types it in, and then clicks on another tile
    private void updatePopulation()
    {
        if (selectedTile.getCity() == -1)
            return;
        biq.city.get(selectedTile.getCity()).setSize(((SpinnerNumberModel)spnrPopulation.getModel()).getNumber().intValue());
        map.triggerUpdates();
    }

    /**
     * Update the cultural value for the city on the selected tile.
     * Note that our "null" call on the culture spinner's stateChanged method is
     * *NECESSARY* so that selectedTile does not change before we store the value
     * of the culture of the city (check whether 0.72 fixed this or 0.73).
     * @param newValue
     * @param oldValue 
     */
    private void updateCulture(int newValue)
    {
        if (selectedTile.getCity() == -1)
            return;
        long start = System.nanoTime();
        //TODO: Only calculate the owners of affected tiles, not the whole map
        List<TILE> tilesToLookAt1 = biq.city.get(selectedTile.getCity()).tilesInfluenced;
        biq.city.get(selectedTile.getCity()).setCulture(newValue);
        //Make this bloody efficient
        biq.calculateTileOwners(selectedTile.xPos, selectedTile.yPos, tilesToLookAt1);
        long end = System.nanoTime();
        long ms = (end - start)/1000000;
        if (logger.isDebugEnabled())
            logger.debug("Took " + ms + " milliseconds to update culture in " + biq.city.get(selectedTile.getCity()).getName());
        //biq.calculateTileOwners();
        map.triggerUpdates();
    }

    private void updateCityBuildings()
    {
        if (selectedTile.getCity() == -1)
            return;
        biq.city.get(selectedTile.getCity()).removeAllBuildings();
        int[]array = lstBuildings.getSelectedIndices();
        for (int i = 0; i < array.length; i++) {
            biq.city.get(selectedTile.getCity()).addBuilding(array[i]);
        }
        map.triggerUpdates();
    }

    private void cmbOwnerAction()
    {
        if (selectedTile.getCity() != -1)
        {
            //Only allow cities owned by barbs if the safety level is Minimal or lower
            if (rbtnOwnerCiv.isSelected() && cmbOwner.getSelectedIndex() == 0 && Main.safetyLevels.get("Map").ordinal() >= SafetyLevel.Safe.ordinal())
            {
                JOptionPane.showMessageDialog(null, "Cities owned by barbarians are not yet supported.", "Barbarians not yet that civilized!", JOptionPane.ERROR_MESSAGE);
                cmbOwner.setSelectedIndex(1);
            }
            biq.city.get(selectedTile.getCity()).setOwner(cmbOwner.getSelectedIndex());
            if (cmbOwnerCultureFire)
                updateCulture(biq.city.get(selectedTile.getCity()).getCulture());
        }
        else if (selectedTile.getColony() != -1)
        {
            biq.colony.get(selectedTile.getColony()).setOwner(cmbOwner.getSelectedIndex());
            //doesn't change borders
        }
        //there can be both a city and a SLOC on the tile
        for (int i = 0; i < biq.startingLocation.size(); i++)
        {
            if (biq.startingLocation.get(i).getX() == selectedTile.xPos)
                if (biq.startingLocation.get(i).getY() == selectedTile.yPos)
                {
                    SLOC startingLocation = biq.startingLocation.get(i);
                    startingLocation.setOwner(cmbOwner.getSelectedIndex());
                }
        }
        for (int i = 0; i < selectedTile.unitsOnTile.size(); i++)
        {
            biq.mapUnit.get(selectedTile.unitsOnTile.get(i)).setOwner(cmbOwner.getSelectedIndex());
        }
        map.triggerUpdates();
    }
    
    /**
     * Change ownership to Barbarians.
     * being the new owner.
     */
    private void rbtnOwnerNoneAction()
    {
        if (selectedTile == null)
            return;
        cmbOwner.setVisible(false);
        
        //there can be both a city and a SLOC on the tile
        for (int i = 0; i < biq.startingLocation.size(); i++)
        {
            if (biq.startingLocation.get(i).getX() == selectedTile.xPos)
                if (biq.startingLocation.get(i).getY() == selectedTile.yPos)
                {
                    SLOC startingLocation = biq.startingLocation.get(i);
                    startingLocation.setOwnerType(CITY.OWNER_NONE);
                }
        }
        map.triggerUpdates();
    }
    
    /**
     * Change ownership from Civilization to Player.  Default to Player 1 (base 1)
     * being the new owner.
     */
    private void rbtnOwnerPlayerAction()
    {
        if (selectedTile == null)
            return;
        if (selectedTile.getCity() != -1)
        {
            biq.city.get(selectedTile.getCity()).setOwner(0);
            biq.city.get(selectedTile.getCity()).setOwnerType(CITY.OWNER_PLAYER);
            //Note that the click will already trigger a culture-recalculate
        }
        else if (selectedTile.getColony() != -1)
        {
            biq.colony.get(selectedTile.getColony()).setOwner(0);
            biq.colony.get(selectedTile.getColony()).setOwnerType(CITY.OWNER_PLAYER);
            //doesn't change borders
        }
        //there can be both a city and a SLOC on the tile
        for (int i = 0; i < biq.startingLocation.size(); i++)
        {
            if (biq.startingLocation.get(i).getX() == selectedTile.xPos)
                if (biq.startingLocation.get(i).getY() == selectedTile.yPos)
                {
                    SLOC startingLocation = biq.startingLocation.get(i);
                    startingLocation.setOwner(0);
                    startingLocation.setOwnerType(CITY.OWNER_PLAYER);
                }
        }
        for (int i = 0; i < selectedTile.unitsOnTile.size(); i++)
        {
            biq.mapUnit.get(selectedTile.unitsOnTile.get(i)).setOwner(0);
            biq.mapUnit.get(selectedTile.unitsOnTile.get(i)).setOwnerType(CITY.OWNER_PLAYER);
        }
        cmbOwner.setVisible(true);
        cmbOwner.setModel(mdlPlayers);
        cmbOwner.setSelectedIndex(0);
        map.triggerUpdates();
    }
    
    /**
     * Change ownership to Barbarians.
     * being the new owner.
     */
    private void rbtnOwnerBarbariansAction()
    {
        if (selectedTile == null)
            return;
        cmbOwner.setVisible(false);
        
        if (selectedTile.getCity() != -1)
        {
            biq.city.get(selectedTile.getCity()).setOwnerType(CITY.OWNER_BARBARIANS);
            biq.calculateTileOwners();
        }
        else if (selectedTile.getColony() != -1)
        {
            biq.colony.get(selectedTile.getColony()).setOwnerType(CITY.OWNER_BARBARIANS);
            //doesn't change borders
        }
        //there can be both a city and a SLOC on the tile
        for (int i = 0; i < biq.startingLocation.size(); i++)
        {
            if (biq.startingLocation.get(i).getX() == selectedTile.xPos)
                if (biq.startingLocation.get(i).getY() == selectedTile.yPos)
                {
                    SLOC startingLocation = biq.startingLocation.get(i);
                    startingLocation.setOwnerType(CITY.OWNER_BARBARIANS);
                }
        }
        for (int i = 0; i < selectedTile.unitsOnTile.size(); i++)
        {
            biq.mapUnit.get(selectedTile.unitsOnTile.get(i)).setOwnerType(CITY.OWNER_BARBARIANS);
        }
        map.triggerUpdates();
    }
    
    /**
     * Change ownership from Player to Civilization.  Default to Player 2 (base 1)
     * being the new owner (the first civ after the barbarians).
     */
    private void rbtnOwnerCivAction()
    {
        if (selectedTile == null)
            return;
        if (selectedTile.getCity() != -1)
        {
            //BAD
            biq.city.get(selectedTile.getCity()).setOwner(1);
            biq.city.get(selectedTile.getCity()).setOwnerType(CITY.OWNER_CIV);
            //Note that the click will already trigger a culture-recalculate
        }
        else if (selectedTile.getColony() != -1)
        {
            biq.colony.get(selectedTile.getColony()).setOwner(1);
            biq.colony.get(selectedTile.getColony()).setOwnerType(CITY.OWNER_CIV);
            //doesn't change borders
        }
        //there can be both a city and a SLOC on the tile
        for (int i = 0; i < biq.startingLocation.size(); i++)
        {
            if (biq.startingLocation.get(i).getX() == selectedTile.xPos)
                if (biq.startingLocation.get(i).getY() == selectedTile.yPos)
                {
                    SLOC startingLocation = biq.startingLocation.get(i);
                    startingLocation.setOwner(1);
                    startingLocation.setOwnerType(CITY.OWNER_CIV);
                }
        }
        for (int i = 0; i < selectedTile.unitsOnTile.size(); i++)
        {
            biq.mapUnit.get(selectedTile.unitsOnTile.get(i)).setOwner(1);
            biq.mapUnit.get(selectedTile.unitsOnTile.get(i)).setOwnerType(CITY.OWNER_CIV);
        }
        cmbOwner.setVisible(true);
        cmbOwner.setModel(mdlCivs);
        cmbOwner.setSelectedIndex(1);
        map.triggerUpdates();
    }

    /**
     * TODO: If we get unlucky about the update time, this could result in a race
     * condition (if we've partially updated the city list).  That is a problem.
     */
    private void cmdDeleteCityAction()
    {
        int response = JOptionPane.showConfirmDialog(null, "Permanently delete " + biq.city.get(selectedTile.getCity()).getName() + "?", "Delete city?", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION)
        {
            int cityIndex = selectedTile.getCity();
            
            biq.deleteCity(cityIndex);
            
            map.triggerUpdates();
            //Now update this panel
            alertToSquarePress(selectedTile.xPos, selectedTile.yPos);
        }
    }
    
    private void cmdRelocateCityAction(TILE selectedTile)
    {
        map.brush.tileBeingRelocated = selectedTile;
        map.brush.mode = Brush.MODE_RELOCATE;
    }
    
    private void cmdRelocateCityAndUnitsAction(TILE selectedTile)
    {
        map.brush.tileBeingRelocated = selectedTile;
        map.brush.mode = Brush.MODE_RELOCATE_CITY_AND_UNITS;
    }

    /**
     * Handles check box actions for any colony type
     * TODO: Prevent race condition, make colonies non-invisible in Firaxis's editor.
     * @param colonyType - The type of colony that the check box represents
     */
    private void colonyCheckAction(byte colonyType)
    {
        //Okay, first the easy case - if another colony is here, convert it.
        //
        int colIndex = selectedTile.getColony();
        if (colIndex != -1)
        {
            CLNY curColony = biq.colony.get(colIndex);
            if (curColony.getImprovementType() != colonyType)
                curColony.setImprovementType(colonyType);
            else    //delete the colony
            {
                selectedTile.setColony((short)-1);
                for (int i = 0; i < biq.tile.size(); i++)
                {
                    if (biq.tile.get(i).getColony() > colIndex)
                    {
                        biq.tile.get(i).setColony((short)(biq.tile.get(i).getColony() - 1));
                    }
                }
            }
        }
        else    //create the colony
        {
            CLNY newColony = new CLNY(biq);
            newColony.setImprovementType(colonyType);
            newColony.setX(selectedTile.xPos);
            newColony.setY(selectedTile.yPos);
            //Who should own it???
            if (selectedTile.owner != -1)
            {
                newColony.setOwner(selectedTile.owner);
                newColony.setOwnerType(selectedTile.ownerType);
            }
            else    //default fallback option
            {
                newColony.setOwnerType(CITY.OWNER_CIV);
                newColony.setOwner(1);    //first one after barbs
            }
            biq.colony.add(newColony);
            selectedTile.setColony((short)(biq.colony.size() - 1));
        }
        alertToSquarePress(selectedTile.xPos, selectedTile.yPos);
        map.triggerUpdates();
    }

    private void chkStartingLocationAction()
    {
        if (!chkStartingLocation.isSelected())
        {
            for (int i = 0; i < biq.startingLocation.size(); i++)
            {
                if (biq.startingLocation.get(i).getX() == selectedTile.xPos && biq.startingLocation.get(i).getY() == selectedTile.yPos)
                {
                    biq.startingLocation.remove(i);
                    selectedTile.C3CBonuses = selectedTile.C3CBonuses & ~TILE.PLAYER_START_MASK;
                    break;
                }
            }
        }
        else    //create the sloc
        {
            biq.addStartingLocation(selectedTile);
        }
        if (logger.isDebugEnabled())
            logger.debug("SLOC size: " + biq.startingLocation.size());
        alertToSquarePress(selectedTile.xPos, selectedTile.yPos);
        map.triggerUpdates();
    }
    
    /**
     * General-purpose method for toggle whether rivers exist on a tile, and its neighbor.
     * Once provided with the tile coordinates, a direction, and whether you are adding or removing
     * the river, it figures out everything else.
     * @param tileX The x coordinate of the primary tile
     * @param tileY The y coordinate of the primary tile
     * @param direction The direction on which the river is being modified (NW, NE, SW, or SE)
     * @param addingRiver Whether a river is being added (true) or removed (false)
     */
    public void toggleRiver(int tileX, int tileY, MapDirection direction, boolean addingRiver) {
        int adjustmentX = 0, adjustmentY = 0;
        byte BITFIELD_MAIN = 0;
        byte BITFIELD_NEIGHBOR = 0;
        if (direction == MapDirection.NORTHWEST) {
            adjustmentX = -1;
            adjustmentY = -1;
            BITFIELD_MAIN = TILE.RIVER_NORTHWEST;
            BITFIELD_NEIGHBOR = TILE.RIVER_SOUTHEAST;
        }
        else if (direction == MapDirection.NORTHEAST) {
            adjustmentX = +1;
            adjustmentY = -1;
            BITFIELD_MAIN = TILE.RIVER_NORTHEAST;
            BITFIELD_NEIGHBOR = TILE.RIVER_SOUTHWEST;
        }
        else if (direction == MapDirection.SOUTHEAST) {
            adjustmentX = +1;
            adjustmentY = +1;
            BITFIELD_MAIN = TILE.RIVER_SOUTHEAST;
            BITFIELD_NEIGHBOR = TILE.RIVER_NORTHWEST;
        }
        else if (direction == MapDirection.SOUTHWEST) {
            adjustmentX = -1;
            adjustmentY = +1;
            BITFIELD_MAIN = TILE.RIVER_SOUTHWEST;
            BITFIELD_NEIGHBOR = TILE.RIVER_NORTHEAST;
        }
        TILE mainTile = biq.tile.get(biq.calculateTileIndex(tileX, tileY));
        TILE neighboringTile = biq.tile.get(biq.calculateTileIndex(tileX + adjustmentX, tileY + adjustmentY));
        mainTile.setRiverAtBorder(addingRiver, BITFIELD_MAIN);
        neighboringTile.setRiverAtBorder(addingRiver, BITFIELD_NEIGHBOR);
    }

    public void chkNWAction()
    {
        TILE nwTile = biq.tile.get(biq.calculateTileIndex(selectedTile.xPos - 1, selectedTile.yPos - 1));
        boolean hasRiver = chkNW.isSelected();
        selectedTile.setRiverAtBorder(hasRiver, TILE.RIVER_NORTHWEST);
        nwTile.setRiverAtBorder(hasRiver, TILE.RIVER_SOUTHEAST);
        alertToSquarePress(selectedTile.xPos, selectedTile.yPos);
        map.triggerUpdates();
    }

    public void chkSEAction()
    {
        TILE seTile = biq.tile.get(biq.calculateTileIndex(selectedTile.xPos + 1, selectedTile.yPos + 1));
        boolean hasRiver = chkSE.isSelected();
        selectedTile.setRiverAtBorder(hasRiver, TILE.RIVER_SOUTHEAST);
        seTile.setRiverAtBorder(hasRiver, TILE.RIVER_NORTHWEST);
        alertToSquarePress(selectedTile.xPos, selectedTile.yPos);
        map.triggerUpdates();
    }

    public void chkNEAction()
    {
        TILE neTile = biq.tile.get(biq.calculateTileIndex(selectedTile.xPos + 1, selectedTile.yPos - 1));
        boolean hasRiver = chkNE.isSelected();
        selectedTile.setRiverAtBorder(hasRiver, TILE.RIVER_NORTHEAST);
        neTile.setRiverAtBorder(hasRiver, TILE.RIVER_SOUTHWEST);
        alertToSquarePress(selectedTile.xPos, selectedTile.yPos);
        map.triggerUpdates();
    }

    public void chkSWAction()
    {
        TILE swTile = biq.tile.get(biq.calculateTileIndex(selectedTile.xPos - 1, selectedTile.yPos + 1));
        boolean hasRiver = chkSW.isSelected();
        selectedTile.setRiverAtBorder(hasRiver, TILE.RIVER_SOUTHWEST);
        swTile.setRiverAtBorder(hasRiver, TILE.RIVER_NORTHEAST);
        alertToSquarePress(selectedTile.xPos, selectedTile.yPos);
        map.triggerUpdates();
    }

    private void tableChangedAction(TableModelEvent e)
    {

        //This seems to work for the combo boxes - the value we get back
        //is the index that is selected, which is good enough
        //It also works with the spinners
        //It doesn't work with the buttons
        //The buttons is okay we've already got listeners for them
        int row = e.getFirstRow();
        int column = e.getColumn();
        if (row == -1 || column == -1)
            return;
        TableModel model = (TableModel)e.getSource();
        String columnName = model.getColumnName(column);
        Object data = model.getValueAt(row, column);

        //Okay now we need to know what in the world to do with this.
        //if (logger.isDebugEnabled())
        //    logger.debug(existingUnitModel.getValueAt(row, column));
        //It looks like we've got exactly the new values here.
        //We don't have the old ones anymore.
        //So, do we try to remember the old ones and update immediately,
        //or do we wait until the tile is moved off of similar to how
        //we do in the other tabs?
        //Or do we just re-do the whole blooming thing?

        //Well, each row corresponds to an entry in the ArrayList of Arrays of Objects dataElements.
        //We've got the row that is being modified so we can ID that to the old one in dataElements
        //Okay but we still have to update the actual units these refer to.  Do we keep track of that?
        //... no...
        Object[]modifiedRow = dataElements.get(row);
        if (column == 0)    //Add or remove units
        {
            UNIT existingUnit = biq.mapUnit.get((Integer)((ArrayList)modifiedRow[4]).get(0));
            int numToAdd = (Integer)data - (Integer)modifiedRow[0];
            if (numToAdd > 0)
            {
                for (;numToAdd > 0; numToAdd--)
                {
                    UNIT newUnit = existingUnit.clone();
                    biq.mapUnit.add(newUnit);
                    selectedTile.unitsOnTile.add(biq.mapUnit.size() - 1);
                    ((ArrayList)modifiedRow[4]).add(biq.mapUnit.size() - 1);
                }
            }
            else if (numToAdd < 0)
            {
                for (;numToAdd < 0; numToAdd++)
                {
                    //It would be uber inefficient to move thousands of units
                    //every time one is deleted.  Alternatives: keep track of
                    //which slots are null and de-null them before saving,
                    //de-null them right away, perhaps with the 'last' unit
                    int toRemove = (Integer)((ArrayList)modifiedRow[4]).get(((ArrayList)modifiedRow[4]).size() - 1);
                    if (toRemove == biq.mapUnit.size() - 1) //removing hte last unit
                    {
                        //remove it from the tile
                        for (int i = 0; i < selectedTile.unitsOnTile.size(); i++)
                        {
                            if (selectedTile.unitsOnTile.get(i) == toRemove)
                            {
                                selectedTile.unitsOnTile.remove(i);
                                break;
                            }
                        }
                        //Remove it from the BIQ
                        biq.mapUnit.remove(toRemove);
                        //No collateral damage b/c it was the last unit
                        //Remove it from our array
                        ((ArrayList)modifiedRow[4]).remove(((ArrayList)modifiedRow[4]).size() - 1);
                    }
                    else
                    {

                        //remove it from the tile
                        for (int i = 0; i < selectedTile.unitsOnTile.size(); i++)
                        {
                            if (selectedTile.unitsOnTile.get(i) == toRemove)
                            {
                                selectedTile.unitsOnTile.remove(i);
                                break;
                            }
                        }
                        //Remove it from the BIQ
                        //We don't want to have to move thousands of units, so
                        //we'll swap the last unit into its place
                        biq.mapUnit.set(toRemove, biq.mapUnit.get(biq.mapUnit.size() - 1));
                        //Now we have to update the pointer from the tile that has this unit
                        int x = biq.mapUnit.get(toRemove).getX();
                        int y = biq.mapUnit.get(toRemove).getY();
                        TILE tile = biq.tile.get(biq.calculateTileIndex(x, y));
                        //okay, got the tile
                        for (int i = 0; i < tile.unitsOnTile.size(); i++)
                        {
                            if (tile.unitsOnTile.get(i) == biq.mapUnit.size() - 1)
                            {
                                tile.unitsOnTile.set(i, toRemove);
                                break;
                            }
                        }
                        //Okay updated the link to it.  Now we can remove the duplicate
                        biq.mapUnit.remove(biq.mapUnit.size() - 1);
                        //Remove it from our array
                        ((ArrayList)modifiedRow[4]).remove(((ArrayList)modifiedRow[4]).size() - 1);
                    }
                }
            }
            modifiedRow[0] = (Integer)data; //TODO: Make sure this won't blow up
            selectedTile.calculateUnitWithBestDefence();
            map.triggerUpdates();
        }
        else if (column == 2)   //Change experience levels
        {
            ArrayList units = (ArrayList)modifiedRow[4];
            for (int i = 0; i < units.size(); i++)
            {
                int x = (Integer)units.get(i);
                biq.mapUnit.get(x).setExperienceLevel((Integer)data);
            }
            modifiedRow[2] = (Integer)data;
            selectedTile.calculateUnitWithBestDefence();
            map.triggerUpdates();
        }
        else if (column == 3)   //remove all units
        {
            int numToAdd = (Integer)modifiedRow[0];
            for (;numToAdd > 0; numToAdd--)
            {
                //It would be uber inefficient to move thousands of units
                //every time one is deleted.  Alternatives: keep track of
                //which slots are null and de-null them before saving,
                //de-null them right away, perhaps with the 'last' unit
                int toRemove = (Integer)((ArrayList)modifiedRow[4]).get(((ArrayList)modifiedRow[4]).size() - 1);
                if (toRemove == biq.mapUnit.size() - 1) //removing hte last unit
                {
                    //remove it from the tile
                    for (int i = 0; i < selectedTile.unitsOnTile.size(); i++)
                    {
                        if (selectedTile.unitsOnTile.get(i) == toRemove)
                        {
                            selectedTile.unitsOnTile.remove(i);
                            break;
                        }
                    }
                    //Remove it from the BIQ
                    biq.mapUnit.remove(toRemove);
                    //No collateral damage b/c it was the last unit
                    //Remove it from our array
                    ((ArrayList)modifiedRow[4]).remove(((ArrayList)modifiedRow[4]).size() - 1);
                }
                else
                {

                    //remove it from the tile
                    for (int i = 0; i < selectedTile.unitsOnTile.size(); i++)
                    {
                        if (selectedTile.unitsOnTile.get(i) == toRemove)
                        {
                            selectedTile.unitsOnTile.remove(i);
                            break;
                        }
                    }
                    //Remove it from the BIQ
                    //We don't want to have to move thousands of units, so
                    //we'll swap the last unit into its place
                    biq.mapUnit.set(toRemove, biq.mapUnit.get(biq.mapUnit.size() - 1));
                    //Now we have to update the pointer from the tile that has this unit
                    int x = biq.mapUnit.get(toRemove).getX();
                    int y = biq.mapUnit.get(toRemove).getY();
                    TILE tile = biq.tile.get(biq.calculateTileIndex(x, y));
                    //okay, got the tile
                    for (int i = 0; i < tile.unitsOnTile.size(); i++)
                    {
                        if (tile.unitsOnTile.get(i) == biq.mapUnit.size() - 1)
                        {
                            tile.unitsOnTile.set(i, toRemove);
                            break;
                        }
                    }
                    //Okay updated the link to it.  Now we can remove the duplicate
                    biq.mapUnit.remove(biq.mapUnit.size() - 1);
                    //Remove it from our array
                    ((ArrayList)modifiedRow[4]).remove(((ArrayList)modifiedRow[4]).size() - 1);
                }
            }
            modifiedRow[0] = 0;
            dataElements.remove(row);
            //And also let's see... remove it from the table model
            existingUnitModel.removeRow(row);
            selectedTile.unitWithBestDefence = -1;
        }
        selectedTile.calculateUnitWithBestDefence();
        map.triggerUpdates();
    }

    private void cmdAddAction()
    {

        String name = cmbUnitType.getSelectedItem().toString();
        int num = Integer.parseInt(spnNumber.getValue().toString());
        int exprLevel = cmbUnitExpr.getSelectedIndex();
        int existingIndex = findMatchingUnitTypeOnTile(dataElements, name, exprLevel);
        if (noMatchingUnitOnTile(existingIndex))
        {
            Object[]element = new Object[4];
            element[0] = num;
            element[1] = name;
            element[2] = exprLevel;
            element[3] = "X";
            dataElements.add(element);
            existingUnitModel.addRow(dataElements.get(dataElements.size() - 1));
            //and now add the unit to the tile in the BIQ
            for (int i = 0; i < num; i++) {
                createNewUnit(exprLevel);
            }
        }
        else
        {
            Object[]element = dataElements.get(existingIndex);
            element[0] = (Integer)element[0] + num;
            for (int i = 0; i < num; i++) {
                createNewUnit(exprLevel);
            }
            //The below line ensures that the list of units is immediately updated
            //to show these new unit(s).
            //It could probably be done more efficiently, but this gets the job
            //done.
            this.updateExistingUnitModel();
        }
        selectedTile.calculateUnitWithBestDefence();
        map.triggerUpdates();
        //Now update this panel
        alertToSquarePress(selectedTile.xPos, selectedTile.yPos);
    }

    private void createNewUnit(int exprLevel) {
        UNIT unit = new UNIT(biq);
        unit.setExperienceLevel(exprLevel);
        unit.setX(selectedTile.xPos);
        unit.setY(selectedTile.yPos);
        unit.setPRTONumber(cmbUnitType.getSelectedIndex());
        //Owner
        if (selectedTile.getHardOwnerType() != CITY.OWNER_NONE) {
            unit.setOwnerType(selectedTile.getHardOwnerType());
            unit.setOwner(selectedTile.getHardOwner());
        }
        else {
            if (this.rbtnNewUnitOwnerBarbarians.isSelected())
                unit.setOwnerType(CITY.OWNER_BARBARIANS);
            else if (this.rbtnNewUnitOwnerCiv.isSelected())
                unit.setOwnerType(CITY.OWNER_CIV);
            else if (this.rbtnNewUnitOwnerPlayer.isSelected())
                unit.setOwnerType(CITY.OWNER_PLAYER);
            else
                logger.warn("No valid owner type!");
            if (!rbtnNewUnitOwnerBarbarians.isSelected())
                unit.setOwner(this.cmbNewUnitOwner.getSelectedIndex());
            else
                unit.setOwner(0);
        }
        unit.setPTWCustomName("");
        unit.setName("");
        unit.setUseCivilizationKing(0);
        setNewUnitStrategy(unit);
        biq.mapUnit.add(unit);
        selectedTile.unitsOnTile.add(biq.mapUnit.size() - 1);
    }

    private void setNewUnitStrategy(UNIT unit) {
        //choose the first strategy that is valid for this unit
        //TODO: It appears that the documentation is incorrect; Firaxis's editor
        //is using 3 (rather than 000..00001000) for exploration, for instance.
        int numToUse = 0;
        int thirtyTwoBits = biq.unit.get(cmbUnitType.getSelectedIndex()).getAIStrategy();
        if (logger.isDebugEnabled())
            logger.debug("AI strategy for this unit: " + biq.unit.get(cmbUnitType.getSelectedIndex()).getAIStrategy());
        while(true)
        {
            if ((thirtyTwoBits | 1) == 1)
                break;
            numToUse++;
            thirtyTwoBits = thirtyTwoBits >>> 1;
            if (numToUse == 32)
            {
                numToUse = -1;  //random
                break;
            }
        }
        unit.setAIStrategy(numToUse);
    }

    private static boolean noMatchingUnitOnTile(int existingIndex) {
        return existingIndex == -1;
    }

    private void terrainIconAction()
    {
        hideDistrictPanel();
        map.brush.mode = Brush.MODE_TERRAIN;
        final JDialog selectedTerrainFrame = new JDialog((JFrame)null, "Select terrain", true);
        selectedTerrainFrame.setPreferredSize(new Dimension(240, 100));
        selectedTerrainFrame.setMinimumSize(new Dimension(240, 100));
        selectedTerrainFrame.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.gridwidth = 1;
        g.gridheight = 1;
        g.gridx = 0;
        g.gridy = 0;
        g.weighty = 0.1;
        g.weightx = 0.1;
        g.anchor = GridBagConstraints.WEST;
        g.ipadx = 4;
        g.ipady = 4;
        JLabel lblTerrain = new JLabel("Select terrain:");
        selectedTerrainFrame.add(lblTerrain, g);
        mdlTerrains.removeAllElements();
        for (int i = 0; i < biq.terrain.size(); i++)
            mdlTerrains.addElement(biq.terrain.get(i).name);
        final JComboBox cmbTerrainSelect = new JComboBox(mdlTerrains);
        g.gridx++;
        selectedTerrainFrame.add(cmbTerrainSelect, g);
        //landmark
        final JCheckBox chkLMTerrain = new JCheckBox("Landmark");
        g.gridy++;
        g.gridx--;
        selectedTerrainFrame.add(chkLMTerrain, g);
        //pine bonus l33tness
        final JCheckBox chkCoolStuff = new JCheckBox("Bonus Grassland?");
        g.gridy++;
        selectedTerrainFrame.add(chkCoolStuff, g);
        chkCoolStuff.setVisible(false);
        
        //ok
        JButton btnOK = new JButton("OK");
        g.gridy++;
        g.gridx++;
        selectedTerrainFrame.add(btnOK, g);
        //Make OK be selected by the enter key
        selectedTerrainFrame.getRootPane().setDefaultButton(btnOK);
        //selectedTerrainFrame.setIconImage(Main.icon);
        
        cmbTerrainSelect.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                chkCoolStuff.setVisible(false);
                if (cmbTerrainSelect.getSelectedIndex() == TERR.GRASSLAND)
                {
                    chkCoolStuff.setText("Bonus Grassland?");
                    chkCoolStuff.setVisible(true);
                }
                else if (cmbTerrainSelect.getSelectedIndex() == TERR.FOREST)
                {
                    chkCoolStuff.setText("Pine Forest?");
                    chkCoolStuff.setVisible(true);
                }
                else if (cmbTerrainSelect.getSelectedIndex() == TERR.MOUNTAIN)
                {
                    chkCoolStuff.setText("Snow-Capped?");
                    chkCoolStuff.setVisible(true);
                }
                else if (cmbTerrainSelect.getSelectedIndex() == TERR.SEA)
                {
                    chkCoolStuff.setText("Deepwater harbour?");
                    chkCoolStuff.setVisible(true);
                }
            }
        });

        //need a way to get stuff back
        btnOK.addActionListener(new java.awt.event.ActionListener()
        {

            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                map.brush.terrainType = cmbTerrainSelect.getSelectedIndex();
                map.brush.isLandmark = chkLMTerrain.isSelected();
                if (map.brush.terrainType == TERR.GRASSLAND && chkCoolStuff.isSelected())
                    map.brush.isBonusGrassland = true;
                else
                    map.brush.isBonusGrassland = false;
                if (map.brush.terrainType == TERR.FOREST && chkCoolStuff.isSelected())
                    map.brush.isPine = true;
                else
                    map.brush.isPine = false;
                if (map.brush.terrainType == TERR.MOUNTAIN && chkCoolStuff.isSelected())
                    map.brush.isSnowCapped = true;
                else
                    map.brush.isSnowCapped = false;
                if (map.brush.terrainType == TERR.SEA && chkCoolStuff.isSelected())
                    map.brush.isDeepwaterHarbour = true;
                else
                    map.brush.isDeepwaterHarbour = false;
                selectedTerrainFrame.setVisible(false);
                
                map.setMapCursor(map.brush.mode);
                setBrushImages(2);
            }
        });

        selectedTerrainFrame.setLocation(450, 350);
        selectedTerrainFrame.pack();
        selectedTerrainFrame.setVisible(true);
    }
    
    private void overlayIconAction()
    {
        hideDistrictPanel();
        map.brush.mode = Brush.MODE_OVERLAY;
        final JDialog selectedOverlayFrame = new JDialog((JFrame)null, "Select overlay", true);
        selectedOverlayFrame.setPreferredSize(new Dimension(240, 100));
        selectedOverlayFrame.setMinimumSize(new Dimension(240, 100));
        selectedOverlayFrame.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.gridwidth = 1;
        g.gridheight = 1;
        g.gridx = 0;
        g.gridy = 0;
        g.weighty = 0.1;
        g.weightx = 0.1;
        g.anchor = GridBagConstraints.WEST;
        g.ipadx = 4;
        g.ipady = 4;
        JLabel lblTerrain = new JLabel("Select overlay:");
        selectedOverlayFrame.add(lblTerrain, g);
        if (!overlayModelInitialized) {
            overlayModel.addElement("Irrigation");
            overlayModel.addElement("Mine");
            overlayModel.addElement("Road");
            overlayModel.addElement("Railroad");
            overlayModel.addElement("Fort");
            overlayModel.addElement("Barricade");
            overlayModel.addElement("Barbarian Camp");
            overlayModel.addElement("Goody Hut");
            overlayModel.addElement("Pollution");
            overlayModel.addElement("Craters");
            overlayModel.addElement("Victory Point Location");
            overlayModel.addElement("Ruins");
            overlayModel.addElement("Starting Location");
            overlayModel.addElement("Radar Tower");
            overlayModel.addElement("Airfield");
            overlayModel.addElement("Colony");
            overlayModel.addElement("Outpost");
            overlayModel.addElement("River");
            overlayModelInitialized = true;
        }
        final JComboBox cmbOverlaySelect = new JComboBox(overlayModel);
        g.gridx++;
        selectedOverlayFrame.add(cmbOverlaySelect, g);
        //landmark
        final JCheckBox chkLMTerrain = new JCheckBox("Landmark");
        g.gridy++;
        g.gridx--;
        selectedOverlayFrame.add(chkLMTerrain, g);
        chkLMTerrain.setVisible(false);
        //pine bonus l33tness
        final JCheckBox chkCoolStuff = new JCheckBox("Bonus Grassland?");
        g.gridy++;
        selectedOverlayFrame.add(chkCoolStuff, g);
        chkCoolStuff.setVisible(false);
        
        //ok
        JButton btnOK = new JButton("OK");
        g.gridy++;
        g.gridx++;
        selectedOverlayFrame.add(btnOK, g);
        //Make OK be selected by the enter key
        selectedOverlayFrame.getRootPane().setDefaultButton(btnOK);
        //selectedTerrainFrame.setIconImage(Main.icon);
        
        cmbOverlaySelect.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                //Possible option or road incl. on rails or not, or leave/overwrite
                //previous overlay?
            }
        });

        //need a way to get stuff back
        btnOK.addActionListener(new java.awt.event.ActionListener()
        {

            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                map.brush.overlayType = OverlayType.values()[cmbOverlaySelect.getSelectedIndex()];
                selectedOverlayFrame.setVisible(false);
                map.setMapCursor(map.brush.mode);
                setBrushImages(3);
            }
        });

        selectedOverlayFrame.setLocation(450, 350);
        selectedOverlayFrame.pack();
        selectedOverlayFrame.setVisible(true);
    }
    
    private void fogIconAction()
    {
        hideDistrictPanel();
        //map.brush.mode = Brush.MODE_TERRAIN;
        final JDialog fogFrame = new JDialog((JFrame)null, "Add or remove fog?", true);
        fogFrame.setPreferredSize(new Dimension(240, 100));
        fogFrame.setMinimumSize(new Dimension(240, 100));
        fogFrame.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.gridwidth = 1;
        g.gridheight = 1;
        g.gridx = 0;
        g.gridy = 0;
        g.weighty = 0.1;
        g.weightx = 0.1;
        g.anchor = GridBagConstraints.WEST;
        g.ipadx = 4;
        g.ipady = 4;
        JButton cmdAddFog = new JButton("Add Fog");
        fogFrame.add(cmdAddFog, g);
        final JButton cmdRemoveFog = new JButton("Remove Fog");
        g.gridx++;
        fogFrame.add(cmdRemoveFog, g);
        
        //need a way to get stuff back
        cmdAddFog.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                map.brush.mode = Brush.MODE_ADD_FOG;
                Main.settings.showFog = true;
                map.triggerUpdates();
                fogFrame.setVisible(false);
                map.setMapCursor(map.brush.mode);
                setBrushImages(6);
            }
        });
        cmdRemoveFog.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                map.brush.mode = Brush.MODE_REMOVE_FOG;
                Main.settings.showFog = true;
                map.triggerUpdates();
                fogFrame.setVisible(false);
                map.setMapCursor(map.brush.mode);
                setBrushImages(6);
            }
        });

        fogFrame.setLocation(450, 350);
        fogFrame.pack();
        fogFrame.setVisible(true);
    }
    
    private void setDiameterImages(int newDiameter) {
        BufferedImage[]inactivePNGs = {onePNG, threePNG, fivePNG, sevenPNG};
        BufferedImage[]activePNGs = {oneActivePNG, threeActivePNG, fiveActivePNG, sevenActivePNG};
        IconPanel[]iconPanels = {diameterOne, diameterThree, diameterFive, diameterSeven};
        
        map.brush.diameter = newDiameter;
        int activeIndex = newDiameter/2;    //round down; seven to three gets right index
        for (int i = 0; i < 4; i++) {
            BufferedImage image = (i == activeIndex ? activePNGs[i] : inactivePNGs[i]);
            IconPanel panel = iconPanels[i];
            Graphics graph = panel.getGraphics();
            if (graph != null) {
                panel.setImage(image);
                panel.update(graph);
                graph.dispose();
            }
        }
    }
    
    public void handleDistrictClick(int x, int y)
    {
        alertToSquarePress(x, y);
        ensureDistrictDefinitions();
        if (selectedTile == null)
            return;
        if (!selectedTile.hasDistrict()) {
            selectedTile.setDistrict(DistrictDefinitions.NEIGHBORHOOD_DISTRICT_ID, TILE.DISTRICT_STATE_COMPLETED);
            if (selectedTile.getDistrictData() != null && selectedTile.getDistrictData().wonderInfo != null)
                selectedTile.getDistrictData().wonderInfo.state = TILE.WDS_COMPLETED;
            map.triggerUpdates();
        }
        districtPanel.setVisible(true);
        updateDistrictPanel();
    }

    private void ensureDistrictDefinitions()
    {
        if (districtDefinitions != null)
            return;
        File installDir = null;
        try {
            if (Main.settings != null && Main.settings.civInstallDir != null && Main.settings.civInstallDir.length() > 0)
                installDir = new File(Main.settings.civInstallDir);
        }
        catch (Exception e) {
            logger.debug("Unable to resolve civ install dir", e);
        }
        districtDefinitions = DistrictDefinitions.load(installDir);
        districtListModel.clear();
        for (DistrictDefinitions.DistrictDefinition def : districtDefinitions.getDistricts()) {
            districtListModel.addElement(def);
        }
    }

    private void activateDistrictMode()
    {
        ensureDistrictDefinitions();
        map.brush.mode = Brush.MODE_DISTRICT;
        map.setMapCursor(map.brush.mode);
        setBrushImages(5);
        districtPanel.setVisible(true);
        updateDistrictPanel();
    }

    private void hideDistrictPanel()
    {
        if (districtPanel != null) {
            districtPanel.setVisible(false);
        }
    }

    private void updateDistrictPanel()
    {
        if (!districtPanel.isVisible()) {
            updatingDistrictSelection = true;
            lstDistricts.clearSelection();
            updatingDistrictSelection = false;
            btnAssignWonder.setEnabled(false);
            btnDeleteDistrict.setEnabled(false);
            lblDistrictHeader.setText("Districts");
            lblWonderAssignment.setText("");
            return;
        }

        ensureDistrictDefinitions();
        if (selectedTile == null) {
            updatingDistrictSelection = true;
            lstDistricts.clearSelection();
            updatingDistrictSelection = false;
            btnAssignWonder.setEnabled(false);
            btnDeleteDistrict.setEnabled(false);
            lblDistrictHeader.setText("Districts");
            lblWonderAssignment.setText("No tile selected");
            return;
        }

        lblDistrictHeader.setText("District @ " + selectedTile.xPos + ", " + selectedTile.yPos);
        TILE.DistrictData data = selectedTile.getDistrictData();
        if (data == null || data.districtType < 0 || data.districtType >= districtListModel.size()) {
            updatingDistrictSelection = true;
            lstDistricts.clearSelection();
            updatingDistrictSelection = false;
            btnAssignWonder.setEnabled(false);
            btnAssignWonder.setVisible(false);
            btnDeleteDistrict.setEnabled(false);
            lblWonderAssignment.setText("No district");
            return;
        }

        updatingDistrictSelection = true;
        lstDistricts.setSelectedIndex(data.districtType);
        lstDistricts.ensureIndexIsVisible(data.districtType);
        updatingDistrictSelection = false;
        btnDeleteDistrict.setEnabled(true);

        if (data.districtType == DistrictDefinitions.WONDER_DISTRICT_ID) {
            btnAssignWonder.setVisible(true);
            btnAssignWonder.setEnabled(true);
            if (data.wonderInfo == null)
                data.wonderInfo = new TILE.WonderDistrictInfo();
            String wonderName = getWonderName(data.wonderInfo.wonderIndex);
            if (data.wonderInfo.wonderIndex >= 0 && wonderName != null) {
                String cityName = (data.wonderInfo.cityId >= 0 && data.wonderInfo.cityId < biq.city.size()) ? biq.city.get(data.wonderInfo.cityId).getName() : "Unknown City";
                lblWonderAssignment.setText("Wonder: " + wonderName + " (" + cityName + ")");
            } else {
                lblWonderAssignment.setText("Wonder: None");
            }
        } else {
            btnAssignWonder.setEnabled(false);
            btnAssignWonder.setVisible(false);
            lblWonderAssignment.setText("");
            if (data.wonderInfo != null)
                data.wonderInfo = new TILE.WonderDistrictInfo();
        }
    }

    private void setDistrictForSelectedTile(int districtId)
    {
        if (selectedTile == null)
            return;
        ensureDistrictDefinitions();
        if (districtId < 0 || districtId >= districtListModel.size())
            return;
        TILE.DistrictData data = selectedTile.ensureDistrictData();
        data.districtType = districtId;
        data.state = TILE.DISTRICT_STATE_COMPLETED;
        if (data.wonderInfo == null)
            data.wonderInfo = new TILE.WonderDistrictInfo();
        if (districtId == DistrictDefinitions.WONDER_DISTRICT_ID) {
            data.wonderInfo.state = TILE.WDS_COMPLETED;
        } else {
            data.wonderInfo = new TILE.WonderDistrictInfo();
        }
        map.triggerUpdates();
        updateDistrictPanel();
    }

    private void deleteSelectedDistrict()
    {
        if (selectedTile == null)
            return;
        selectedTile.clearDistrict();
        map.triggerUpdates();
        updateDistrictPanel();
    }

    private void handleWonderAssignment()
    {
        if (selectedTile == null)
            return;
        ensureDistrictDefinitions();
        List<WonderOption> options = collectWonderOptions(selectedTile);
        if (options.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No eligible wonders found near this district.", "Assign Wonder", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        WonderOption selection = (WonderOption)JOptionPane.showInputDialog(this,
                "Select a wonder to associate with this district:",
                "Assign Wonder",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options.toArray(new WonderOption[0]),
                options.get(0));
        if (selection == null)
            return;

        TILE.DistrictData data = selectedTile.ensureDistrictData();
        if (data.wonderInfo == null)
            data.wonderInfo = new TILE.WonderDistrictInfo();
        data.wonderInfo.state = TILE.WDS_COMPLETED;
        if (selection.isNone) {
            data.wonderInfo.cityId = -1;
            data.wonderInfo.wonderIndex = -1;
        } else {
            data.wonderInfo.cityId = selection.cityIndex;
            data.wonderInfo.wonderIndex = selection.definition.index;
        }
        map.triggerUpdates();
        updateDistrictPanel();
    }

    private List<WonderOption> collectWonderOptions(TILE tile)
    {
        List<WonderOption> options = new ArrayList<WonderOption>();
        options.add(WonderOption.noneOption());
        if (tile == null)
            return options;

        Set<Integer> usedWonders = new HashSet<Integer>();
        for (TILE t : biq.tile) {
            if (t == null || t == tile)
                continue;
            TILE.DistrictData data = t.getDistrictData();
            if (data != null && data.districtType == DistrictDefinitions.WONDER_DISTRICT_ID && data.wonderInfo != null && data.wonderInfo.wonderIndex >= 0)
                usedWonders.add(Integer.valueOf(data.wonderInfo.wonderIndex));
        }

        Set<String> seenPairs = new HashSet<String>();
        if (tile.citiesWithInfluence != null) {
            for (Integer indexObj : tile.citiesWithInfluence) {
                if (indexObj == null)
                    continue;
                int cityIndex = indexObj.intValue();
                if (cityIndex < 0 || cityIndex >= biq.city.size())
                    continue;
                CITY city = biq.city.get(cityIndex);
                for (DistrictDefinitions.WonderDefinition wonder : districtDefinitions.getWonders()) {
                    if (usedWonders.contains(Integer.valueOf(wonder.index)))
                        continue;
                    int buildingId = findBuildingIdByName(wonder.name);
                    String key = wonder.name + ":" + cityIndex;
                    if (buildingId >= 0 && city.hasBuilding(buildingId) && seenPairs.add(key)) {
                        options.add(new WonderOption(wonder, city, cityIndex));
                    }
                }
            }
        }
        return options;
    }

    private int findBuildingIdByName(String name)
    {
        if (name == null)
            return -1;
        for (int i = 0; i < biq.buildings.size(); i++) {
            String bName = biq.buildings.get(i).getName();
            if (bName != null && bName.equalsIgnoreCase(name))
                return i;
        }
        return -1;
    }

    private String getWonderName(int wonderIndex)
    {
        if (districtDefinitions == null)
            return null;
        if (wonderIndex < 0 || wonderIndex >= districtDefinitions.getWonders().size())
            return null;
        return districtDefinitions.getWonders().get(wonderIndex).name;
    }

    private BufferedImage createDistrictIcon(boolean active)
    {
        BufferedImage img = new BufferedImage(24, 24, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        try {
            Color background = active ? new Color(70, 130, 180) : new Color(96, 96, 96);
            g2.setColor(background);
            g2.fillRect(0, 0, 24, 24);
            g2.setColor(Color.WHITE);
            Font base = g2.getFont();
            if (base != null)
                g2.setFont(base.deriveFont(Font.BOLD, 14f));
            FontMetrics fm = g2.getFontMetrics();
            String label = "D";
            int tx = (24 - fm.stringWidth(label)) / 2;
            int ty = (24 - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(label, tx, ty);
        }
        finally {
            g2.dispose();
        }
        return img;
    }

    private static class WonderOption {
        final DistrictDefinitions.WonderDefinition definition;
        final CITY city;
        final int cityIndex;
        final boolean isNone;

        WonderOption(DistrictDefinitions.WonderDefinition def, CITY city, int cityIndex) {
            this.definition = def;
            this.city = city;
            this.cityIndex = cityIndex;
            this.isNone = false;
        }

        WonderOption() {
            this.definition = null;
            this.city = null;
            this.cityIndex = -1;
            this.isNone = true;
        }

        static WonderOption noneOption() {
            return new WonderOption();
        }

        @Override
        public String toString() {
            if (isNone)
                return "None";
            String wonderName = (definition != null) ? definition.name : "";
            String cityName = (city != null && city.getName() != null) ? city.getName() : "";
            if (cityName.length() == 0)
                return wonderName;
            return wonderName + " (" + cityName + ")";
        }
    }

    void setBrushImages(int active) {
        active = active - 1;
        BufferedImage[]inactivePNGs = {selectPNG, terrainPNG, overlayPNG, cityPNG, districtPNG, fogPNG};
        BufferedImage[]activePNGs = {selectActive, terrainActive, overlayActive, cityActive, districtActive, fogActive};
        IconPanel[]iconPanels = {selectIcon, terrainIcon, overlayIcon, cityIcon, districtIcon, fogIcon};
        
        for (int i = 0; i < iconPanels.length; i++) {
            BufferedImage image = (i == active ? activePNGs[i] : inactivePNGs[i]);
            IconPanel panel = iconPanels[i];
            Graphics graph = panel.getGraphics();
            if (graph != null) {
                panel.setImage(image);
                panel.update(graph);
                graph.dispose();
            }
        }
    }
    
    /**
     * Returns the log base 10 of a number.  Clobbers the number.
     * @param number 
     */
    private int log10(int number)
    {
        int toReturn = 0;
        while (number > 0)
        {
            number/=10;
            toReturn++;
        }
        return toReturn;
    }
    
    private void cmdShowActionPerformed()
    {
        rightSidePane.setVisible(true);
        showRSP.setVisible(false);
        map.triggerUpdates();
    }
    
    private void cmdHideActionPerformed()
    {
        rightSidePane.setVisible(false);
        showRSP.setVisible(true);
        map.triggerUpdates();
    }
    
    //down or right = 0 (move +), up or left = 1 (move -)
    private byte lastDirection = 0;
    
    /**
     * This method is fired when the user scrolls on the map using the mouse
     * wheel.  If they scrolled horizontally, this method corrects the built-in
     * Java methods.  Built-in, all mouse-wheel scrolls are vertical.  This
     * undoes vertical ones (if Shift is held down) and converts them into
     * horizontal ones.
     * @hack
     * @param e 
     */
    private void replyToMouseWheel(MouseWheelEvent e)
    {
        int amtToMove;
        amtToMove = 30;
        if (e.getWheelRotation() < 0)
        {   
            amtToMove = 0 - amtToMove;
            lastDirection = 1;
        }
        else if (e.getWheelRotation() > 0)
        {
            lastDirection = 0;
        }
        else if (e.getWheelRotation() == 0)
        {
            //switch directions
            if (lastDirection == 0) //go -
            {
                amtToMove = 0 - amtToMove;
                lastDirection = 1;
            }
            else
            {
                lastDirection = 0;
            }
        }
        if (logger.isTraceEnabled())
            logger.trace("wheel rotation: " + e.getWheelRotation());
        amtToMove*=Math.abs(e.getWheelRotation());
        //todo: use precision wheel rotation if JDK >= 1.7
        if (e.isShiftDown())
        {
            mapScroll.getHorizontalScrollBar().setValue(mapScroll.getHorizontalScrollBar().getValue()+amtToMove);
            }
        else
        {
            int val = mapScroll.getVerticalScrollBar().getValue();
            if (logger.isDebugEnabled())
            {
                logger.debug(amtToMove > 0 ? "Going down" : "Going up");
                logger.debug("New value: " + (val+amtToMove));
            }
            mapScroll.getVerticalScrollBar().setValue(val+amtToMove);            
        }
    }
    
    /**
     * Update the width of the right side panel.  To be called when the window is resized.
     * @param width - How wide, in pixels, the window now is.
     * @since 0.82
     */
    public void updateWidth(int width)
    {
        int pixels = homeFrame.getWidth();
        rightSideWeight=250.0/width;
        DecimalFormat df = new DecimalFormat("0.0%");
        if (logger.isDebugEnabled())
            logger.debug("Weight of " + df.format(rightSideWeight) + " with " + pixels + " pixels in width");
        g.weightx = rightSideWeight;
        g.weighty = 0.5;
        g.gridx = 2;
        g.ipadx = 4;
        layout.setConstraints(rightSidePane, g);
        
        //also update the map section's weight
        g.gridx = 0;
        g.gridy = 0;
        g.gridwidth = 1;
        g.weightx = 1 - rightSideWeight;
        g.weighty = 0.5;
        layout.setConstraints(mapScroll, g);
    }
    
    public void setZoom(double zoom, int newWidth, int newHeight)
    {
        mapScroll.getViewport().setViewSize(new Dimension(newWidth, newHeight));
        
        this.mapScroll.getHorizontalScrollBar().setMaximum(newWidth);
        this.mapScroll.getVerticalScrollBar().setMaximum(newHeight);
        
        if (logger.isDebugEnabled())
            logger.debug("Max scrollbar width: " + newWidth + " Max Height: " + newHeight);
        
    }
}
