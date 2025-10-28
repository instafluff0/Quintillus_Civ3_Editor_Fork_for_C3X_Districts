package com.civfanatics.civ3.xplatformeditor;

/**
 *
 * @author Quintillus
 */
import com.civfanatics.civ3.biqFile.BLDG;
import com.civfanatics.civ3.biqFile.TERR;
import com.civfanatics.civ3.biqFile.PRTO;
import com.civfanatics.civ3.biqFile.RULE;
import com.civfanatics.civ3.biqFile.TRFM;
import com.civfanatics.civ3.biqFile.GOOD;
import com.civfanatics.civ3.biqFile.TECH;
import com.civfanatics.civ3.biqFile.TILE;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.ImprovedListView;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJList;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJTextField;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperListModel;
import static com.civfanatics.civ3.xplatformeditor.Main.i18n;
import com.civfanatics.civ3.xplatformeditor.civilopedia.CivilopediaIcon;
import com.civfanatics.civ3.xplatformeditor.specialty.PredicateFactory;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javax.swing.*;
import static org.jdesktop.layout.LayoutStyle.*;
import static org.jdesktop.layout.GroupLayout.*;

public class GoodTab extends EditorTab {
    public List<GOOD>resource;    
    public List<TECH>technology;
    public List<BLDG>building;
    public List<PRTO>unit;
    public List<TERR>terrain;
    public List<TRFM>workerJob;
    public List<RULE>rule;
    //ArrayLists on which this tab depends    public List<BLDG>building;    public List<PRTO>unit;    public List<RULE>rule;    public List<TERR>terrain;    public List<TRFM>workerJob;
    //end array lists on which this tab depends
    int goodIndex;
    boolean tabCreated;
    private SuperListModel goodList;
    //tabs that depend on this one
    TERRTab terrTab;
    RULETab ruleTab;
    UnitTab unitTab;
    TRFMTab trfmTab;
    BldgTab bldgTab;
    MapTab mapTab;
    private javax.swing.ButtonGroup btnGroupGOODType;

    BufferedImage[]goodIcons;
    private IconPanel pnlLargeIcon = new IconPanel();
    
    DefaultComboBoxModel mdlPrerequisite = new DefaultComboBoxModel();

    JMenuItem delete;
    JMenuItem add;
    
    //JavaFX section
    final JFXPanel jFXPanel = new JFXPanel();    
    ImprovedListView<GOOD> goodListUI;
    ObservableList<GOOD> observableTechList = FXCollections.observableArrayList();
    
    public GoodTab()
    {
        if (Main.settings.useJavaFX) {
            setupJavaFX();
        }
        lstType = lstGoods;
        tabName = "GOOD";
    }
    /**
     * Creates the GUI of the tab.  This includes button groups, layouts, etc.
     * Logically, this is to occur immediately after the constructor is called.
     *
     * @return - A reference to the GoodTab instance itself.  This reference
     * allows the tab to be added to a tab bar.
     */
    public JPanel createTab()
    {
        textBoxes = new ArrayList<>();
        scrGoods = new JScrollPane();
        lblCivilopediaEntry = new JLabel();
        txtGOODCivilopediaEntry = new SuperJTextField();
        txtGOODCivilopediaEntry.setDescription(i18n("general.civilopediaEntry"));
        lblPrerequisites = new JLabel();
        cmbGOODPrerequisite = new JComboBox();
        pnlBonuses = new JPanel();
        jLabel66 = new JLabel();
        lblShields = new JLabel();
        lblCommerce = new JLabel();
        txtGOODFoodBonus = new SuperJTextField();
        txtGOODShieldsBonus = new SuperJTextField();
        txtGOODCommerceBonus = new SuperJTextField();
        txtGOODFoodBonus.setDescription(i18n("good.food"));
        txtGOODShieldsBonus.setDescription(i18n("good.shields"));
        txtGOODCommerceBonus.setDescription(i18n("good.commerce"));
        pnlType = new JPanel();
        rbtnGOODStrategic = new JRadioButton();
        rbtnGOODLuxury = new JRadioButton();
        rbtnGOODBonus = new JRadioButton();
        lblappearanceRatio = new JLabel();
        lblDisappearanceRatio = new JLabel();
        txtGOODAppearanceRatio = new SuperJTextField();
        txtGOODDisapperanceProbability = new SuperJTextField();
        txtGOODAppearanceRatio.setDescription(i18n("good.appearanceRatio"));
        txtGOODDisapperanceProbability.setDescription(i18n("good.disappearanceRatio"));
        lblIcon = new JLabel();
        txtGOODIcon = new SuperJTextField();
        txtGOODIcon.setDescription(i18n("good.icon"));
        btnGroupGOODType = new ButtonGroup();

        lstGoods.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstGoods.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstGoodsValueChanged(evt);
            }
        });
        scrGoods.setViewportView(lstGoods);

        lblCivilopediaEntry.setText(i18n("general.civilopediaEntry") + ":");

        lblPrerequisites.setText(i18n("general.prerequisite") +  ":");

        cmbGOODPrerequisite.setModel(mdlPrerequisite);

        pnlBonuses.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), i18n("good.bonuses")));

        jLabel66.setText(i18n("good.food") + ":");

        lblShields.setText(i18n("good.shields") + ":");

        lblCommerce.setText(i18n("good.commerce") + ":");
        
        setupPnlLargeIcon();
        pnlLargeIcon.setPreferredSize(new Dimension(128, 128));

        org.jdesktop.layout.GroupLayout pnlBonusesLayout = new org.jdesktop.layout.GroupLayout(pnlBonuses);
        pnlBonuses.setLayout(pnlBonusesLayout);
        pnlBonusesLayout.setHorizontalGroup(
            pnlBonusesLayout.createParallelGroup(LEADING)
            .add(TRAILING, pnlBonusesLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnlBonusesLayout.createParallelGroup(LEADING)
                    .add(lblCommerce)
                    .add(lblShields)
                    .add(jLabel66))
                .addPreferredGap(RELATED)
                .add(pnlBonusesLayout.createParallelGroup(LEADING)
                    .add(TRAILING, txtGOODShieldsBonus, DEFAULT_SIZE, 43, Short.MAX_VALUE)
                    .add(TRAILING, txtGOODCommerceBonus, DEFAULT_SIZE, 43, Short.MAX_VALUE)
                    .add(TRAILING, txtGOODFoodBonus, DEFAULT_SIZE, 43, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlBonusesLayout.setVerticalGroup(
            pnlBonusesLayout.createParallelGroup(LEADING)
            .add(pnlBonusesLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnlBonusesLayout.createParallelGroup(LEADING)
                    .add(TRAILING, jLabel66)
                    .add(TRAILING, pnlBonusesLayout.createSequentialGroup()
                        .add(txtGOODFoodBonus, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                        .add(1, 1, 1)))
                .addPreferredGap(RELATED)
                .add(pnlBonusesLayout.createParallelGroup(BASELINE)
                    .add(lblShields)
                    .add(txtGOODShieldsBonus, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                .addPreferredGap(RELATED)
                .add(pnlBonusesLayout.createParallelGroup(BASELINE)
                    .add(lblCommerce)
                    .add(txtGOODCommerceBonus, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                .addContainerGap(DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlType.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), i18n("good.type")));

        rbtnGOODStrategic.setText(i18n("good.strategic"));

        rbtnGOODLuxury.setText(i18n("good.luxury"));

        rbtnGOODBonus.setText(i18n("good.bonus"));

        org.jdesktop.layout.GroupLayout pnlTypeLayout = new org.jdesktop.layout.GroupLayout(pnlType);
        pnlType.setLayout(pnlTypeLayout);
        pnlTypeLayout.setHorizontalGroup(
            pnlTypeLayout.createParallelGroup(LEADING)
            .add(pnlTypeLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnlTypeLayout.createParallelGroup(LEADING)
                    .add(rbtnGOODStrategic)
                    .add(rbtnGOODLuxury)
                    .add(rbtnGOODBonus))
                .addContainerGap(9, Short.MAX_VALUE))
        );
        pnlTypeLayout.setVerticalGroup(
            pnlTypeLayout.createParallelGroup(LEADING)
            .add(pnlTypeLayout.createSequentialGroup()
                .add(rbtnGOODStrategic)
                .addPreferredGap(UNRELATED)
                .add(rbtnGOODLuxury)
                .addPreferredGap(UNRELATED)
                .add(rbtnGOODBonus)
                .addContainerGap(9, Short.MAX_VALUE))
        );

        lblappearanceRatio.setText(i18n("good.appearanceRatio") + ":");

        lblDisappearanceRatio.setText(i18n("good.disappearanceRatio") + ":");

        lblAppearancesOnMap.setText("Appearances on Map: 0");

        lblIcon.setText(i18n("good.icon") + ":");

        txtGOODIcon.setText("             ");

        org.jdesktop.layout.GroupLayout thisLayout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(thisLayout);
        
        Component listComponent = Main.settings.useJavaFX ? jFXPanel : scrGoods;
        
        thisLayout.setHorizontalGroup(
            thisLayout.createParallelGroup(LEADING)
            .add(thisLayout.createSequentialGroup()
                .add(listComponent, PREFERRED_SIZE, 165, PREFERRED_SIZE)
                .addPreferredGap(RELATED)
                .add(thisLayout.createParallelGroup(TRAILING)
                    .add(LEADING, thisLayout.createSequentialGroup()
                        .add(lblDisappearanceRatio)
                        .add(25, 25, 25)
                        .add(thisLayout.createParallelGroup(TRAILING, false)
                            .add(LEADING, txtGOODAppearanceRatio)
                            .add(LEADING, txtGOODDisapperanceProbability, DEFAULT_SIZE, 48, Short.MAX_VALUE)))
                    .add(LEADING, thisLayout.createSequentialGroup()
                        .add(thisLayout.createParallelGroup(TRAILING, false)
                            .add(LEADING, thisLayout.createSequentialGroup()
                                .add(pnlBonuses, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                .addPreferredGap(RELATED)
                                .add(pnlType, DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(RELATED)
                                .add(pnlLargeIcon, DEFAULT_SIZE, DEFAULT_SIZE, Short.MAX_VALUE))
                            .add(LEADING, thisLayout.createSequentialGroup()
                                .add(thisLayout.createParallelGroup(LEADING)
                                    .add(lblCivilopediaEntry)
                                    .add(lblPrerequisites))
                                .addPreferredGap(RELATED)
                                .add(thisLayout.createParallelGroup(LEADING, false)
                                    .add(cmbGOODPrerequisite, 0, DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(txtGOODCivilopediaEntry, DEFAULT_SIZE, 161, Short.MAX_VALUE)))
                                .add(thisLayout.createParallelGroup(LEADING, false)
                                    .add(cmbGOODPrerequisite, 0, DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(txtGOODCivilopediaEntry, DEFAULT_SIZE, 161, Short.MAX_VALUE)))
                        .addPreferredGap(RELATED)
                        .add(lblIcon)
                        .addPreferredGap(UNRELATED)
                        .add(txtGOODIcon, PREFERRED_SIZE, 40, PREFERRED_SIZE))
                    .add(LEADING, lblappearanceRatio)
                    .add(LEADING, lblAppearancesOnMap))
                .addContainerGap())
        );
        thisLayout.setVerticalGroup(
            thisLayout.createParallelGroup(LEADING)
            .add(listComponent, DEFAULT_SIZE, 679, Short.MAX_VALUE)
            .add(thisLayout.createSequentialGroup()
                .addContainerGap()
                .add(thisLayout.createParallelGroup(BASELINE)
                    .add(lblCivilopediaEntry)
                    .add(txtGOODCivilopediaEntry, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                    .add(lblIcon)
                    .add(txtGOODIcon, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                .addPreferredGap(RELATED)
                .add(thisLayout.createParallelGroup(BASELINE)
                    .add(lblPrerequisites)
                    .add(cmbGOODPrerequisite, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                .addPreferredGap(RELATED)
                .add(thisLayout.createParallelGroup(LEADING)
                    .add(pnlType, DEFAULT_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                    .add(pnlBonuses, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                    .add(pnlLargeIcon, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                .add(10, 10, 10)
                .add(thisLayout.createParallelGroup(BASELINE)
                    .add(lblappearanceRatio)
                    .add(txtGOODAppearanceRatio, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                .addPreferredGap(RELATED)
                .add(thisLayout.createParallelGroup(BASELINE)
                    .add(lblDisappearanceRatio)
                    .add(txtGOODDisapperanceProbability, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                .add(10, 10, 10)
                .add(thisLayout.createParallelGroup(BASELINE)
                    .add(lblAppearancesOnMap))
                .addContainerGap(DEFAULT_SIZE, Short.MAX_VALUE))
        );
        
        if (Main.settings.useJavaFX) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Scene scene = new Scene(goodListUI, 170, 670);        
                    jFXPanel.setScene(scene);
                }
            });
        }


        btnGroupGOODType.add(rbtnGOODStrategic);
        btnGroupGOODType.add(rbtnGOODLuxury);
        btnGroupGOODType.add(rbtnGOODBonus);

        this.setName("GOOD");

        tabCreated = true;
        return this;
    }
    
    private void setupJavaFX() {
        goodListUI = new ImprovedListView<>("resource", this.resource, observableTechList, PredicateFactory.createGOODFilter());
        goodListUI.setAddAction(this::addItem);
        goodListUI.setCopyAction(this::copyItem);
        goodListUI.setRenameAction(this::renameItem);
        goodListUI.setDeleteAction(this::deleteAction);
        goodListUI.setChangeFunction(this::updateTab);
        goodListUI.setSwapFunction(this::swapOrder);
    }

    public void sendGoodIcons(BufferedImage[]goodIcons)
    {
        this.goodIcons = goodIcons;
    }
    
    private void setupPnlLargeIcon() {
        pnlLargeIcon.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    File f = CivilopediaIcon.getScenarioSpecificFile(resource.get(goodIndex), true);
                    Desktop.getDesktop().edit(f);
                }
                catch(IOException ex) {
                    //The "edit" option may not be present on all OSes (notably Windows).
                    //Thus, try "open" as well.
                    if (ex.getMessage().contains("No application is associated")) {
                        try {
                            File f = CivilopediaIcon.getScenarioSpecificFile(resource.get(goodIndex), true);
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
     * @param resource - The list of all resources.
     * @param technology - The list of all technologies.
     */
    public void sendData(List<GOOD>resource, List<TECH>technology)
    {
        assert  tabCreated:"The tab must be created before data is send to it.";
        this.resource = resource;
        this.technology = technology;
        goodIndex = -1;
        goodList = new SuperListModel();
        lstGoods.setModel(goodList);
        cmbGOODPrerequisite.removeAllItems();
        cmbGOODPrerequisite.addItem(noneSelected);

        for (int i = 0; i < technology.size(); i++)
        {
            utils.addWithPossibleDuplicates(technology.get(i).getName(), mdlPrerequisite);
        }
        for (int i = 0; i < resource.size(); i++)
        {
            goodList.addElement(resource.get(i).getName());
        }
        if (Main.settings.useJavaFX) {
            goodListUI.setBaseItems(this.resource);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < resource.size(); i++)
                    {
                        observableTechList.add(resource.get(i));
                    }
                }
            });
        }
    }

    public void setSelectedIndex(int i)
    {
        goodIndex = i;
    }

    public void sendTabLinks(TERRTab terrTab, TRFMTab trfmTab, UnitTab unitTab, BldgTab bldgTab, RULETab ruleTab, MapTab mapTab)
    {
        this.terrTab = terrTab;
        this.trfmTab = trfmTab;
        this.unitTab = unitTab;
        this.bldgTab = bldgTab;
        this.ruleTab = ruleTab;
        this.mapTab = mapTab;
    }

    //Set up links to other tabs that need to be alerted when a member of this class is added or deleted
    public void setDependencies(List<BLDG>building, List<PRTO>unit, List<RULE>rule, List<TERR>terrain, List<TRFM>workerJob)
    {
        this.building = building;
        this.unit = unit;
        this.rule = rule;
        this.terrain = terrain;
        this.workerJob = workerJob;
    }
    
    public void updateTab() {
        updateTab(null);
    }

    public void updateTab(GOOD newlySelectedGood)
    {
        if (!(goodIndex == -1)) {
            resource.get(goodIndex).setCivilopediaEntry(txtGOODCivilopediaEntry.getText());
            resource.get(goodIndex).setAppearanceRatio(txtGOODAppearanceRatio.getInteger());
            resource.get(goodIndex).setDisapperanceProbability(txtGOODDisapperanceProbability.getInteger());
            resource.get(goodIndex).setIcon(txtGOODIcon.getInteger());
            resource.get(goodIndex).setPrerequisite(cmbGOODPrerequisite.getSelectedIndex() - 1);
            resource.get(goodIndex).setFoodBonus(txtGOODFoodBonus.getInteger());
            resource.get(goodIndex).setShieldsBonus(txtGOODShieldsBonus.getInteger());
            resource.get(goodIndex).setCommerceBonus(txtGOODCommerceBonus.getInteger());
            if (rbtnGOODStrategic.isSelected()) {
                resource.get(goodIndex).setType(2);
            } else if (rbtnGOODLuxury.isSelected()) {
                resource.get(goodIndex).setType(1);
            } else    //bonus resoruce
            {
                resource.get(goodIndex).setType(0);
            }
        }
        if (Main.settings.useJavaFX) {
            if (newlySelectedGood != null) {
                goodIndex = newlySelectedGood.getIndex();
            }
            else {
                goodIndex = -1;
            }
        }
        else {
            goodIndex = lstGoods.getSelectedIndex();
        }
        if (goodIndex != -1)
        {
            txtGOODCivilopediaEntry.setText(resource.get(goodIndex).getCivilopediaEntry());
            txtGOODAppearanceRatio.setText(Integer.toString(resource.get(goodIndex).getAppearanceRatio()));
            txtGOODDisapperanceProbability.setText(Integer.toString(resource.get(goodIndex).getDisapperanceProbability()));
            txtGOODIcon.setText(Integer.toString(resource.get(goodIndex).getIcon()));
            cmbGOODPrerequisite.setSelectedIndex(resource.get(goodIndex).getPrerequisite() + 1);
            txtGOODFoodBonus.setText(Integer.toString(resource.get(goodIndex).getFoodBonus()));
            txtGOODShieldsBonus.setText(Integer.toString(resource.get(goodIndex).getShieldsBonus()));
            txtGOODCommerceBonus.setText(Integer.toString(resource.get(goodIndex).getCommerceBonus()));
            if (resource.get(goodIndex).getType() == 0) {
                rbtnGOODBonus.setSelected(true);
                rbtnGOODLuxury.setSelected(false);
                rbtnGOODStrategic.setSelected(false);
            }
            if (resource.get(goodIndex).getType() == 1) {
                rbtnGOODBonus.setSelected(false);
                rbtnGOODLuxury.setSelected(true);
                rbtnGOODStrategic.setSelected(false);
            }
            if (resource.get(goodIndex).getType() == 2) {
                rbtnGOODBonus.setSelected(false);
                rbtnGOODLuxury.setSelected(false);
                rbtnGOODStrategic.setSelected(true);
            }
            
            if (baselink.hasCustomMap()) {
                int resourceCount = 0;
                for (TILE tile : baselink.tile) {
                    if (tile.getResource() == goodIndex) {
                        resourceCount++;
                    }
                }
                
                lblAppearancesOnMap.setText("Appearances on Map: " + resourceCount);
            }
            updateResourceIcon();
        }
    }
    
    private void updateResourceIcon() {
       if (goodIndex != -1) {
            //Tech Icon
            //Performance: Seems to be 80% instant, 16% 15ms, 4% slightly slower
            //Corresponding to number of HDD accesses - either the head is already on the right track,
            //or it has to search once, or rarely multiple times on the search path.
            //Either way, going to favor memory conversation over loading speed since speed is perfectly
            //adequate.  Test device: Toshiba 3.5" 2 TB 7200 RPM HDD (though developed with fine results on
            //Seagate 2.5" 160 GB 5400 RPM HDD).
            if (Main.GRAPHICS_ENABLED) {
                try {
                    Image img = CivilopediaIcon.getLargeCivilopediaIcon(resource.get(goodIndex));
                    if (img != null) {
                        pnlLargeIcon.setVisible(true);
                        pnlLargeIcon.setImage(img);
                        
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


    private void lstGoodsValueChanged(javax.swing.event.ListSelectionEvent evt) {

        updateTab();
    }
    public JComboBox cmbGOODPrerequisite;
    private JLabel lblCivilopediaEntry;
    private JLabel lblPrerequisites;
    private JLabel jLabel66;
    private JLabel lblShields;
    private JLabel lblCommerce;
    private JLabel lblappearanceRatio;
    private JLabel lblDisappearanceRatio;
    private JLabel lblAppearancesOnMap = new JLabel();
    private JLabel lblIcon;
    private JPanel pnlBonuses;
    private JPanel pnlType;
    private JScrollPane scrGoods;
    private SuperJList lstGoods = new SuperJList(this, "good", true, true, true);
    private JRadioButton rbtnGOODBonus;
    private JRadioButton rbtnGOODLuxury;
    private JRadioButton rbtnGOODStrategic;
    private SuperJTextField txtGOODAppearanceRatio;
    private SuperJTextField txtGOODCivilopediaEntry;
    private SuperJTextField txtGOODCommerceBonus;
    private SuperJTextField txtGOODDisapperanceProbability;
    private SuperJTextField txtGOODFoodBonus;
    private SuperJTextField txtGOODIcon;
    private SuperJTextField txtGOODShieldsBonus;

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
        addLengthDocumentListener(31, txtGOODCivilopediaEntry);
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
        addBadValueDocumentListener(10000, txtGOODDisapperanceProbability);
        addBadValueDocumentListener(900, txtGOODAppearanceRatio);
        addBadValueDocumentListener(35, txtGOODIcon);
        addBadValueDocumentListener(25, -25, txtGOODFoodBonus);
        addBadValueDocumentListener(25, -25, txtGOODCommerceBonus);
        addBadValueDocumentListener(25, -25, txtGOODShieldsBonus);
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
    
    public void renameItem(GOOD good) {
        String name = utils.getItemName("Rename " + good.getName(), "Good Name", "Enter new name for " + good.getName() + ": ", good.getName());

        if (name.length() > 0) {
            good.setName(name);
            
            int cmbIndex = good.getIndex() + 1;
            //TODO: Refactor this to be nice.
//            for (DefaultComboBoxModel model : getModelsToUpdate()) {
//                utils.renameItemInComboBox(cmbIndex, good.getName(), model);
//            }
        }
    }
    
    public void swapOrder(int source, int destination) {
        if (source < destination) {
            goodIndex = destination;
        }
        else {
            goodIndex = destination + 1;
        }
        
        updateOrderInOtherTabs(source, destination, resource.get(goodIndex).getName());
    }
    
    private void updateOrderInOtherTabs(int source, int destination, String goodName) {
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
        
        if (baselink.hasCustomMap()) {
            comboBoxSwap(cmbSource, cmbDest, mapTab.cmbResource);
            for (TILE t : baselink.tile) {
                t.handleSwappedGood();
            }
        }
        
        if (baselink.hasCustomRules()) {
            comboBoxSwap(cmbSource, cmbDest, ruleTab.cmbRULEDefaultMoneyResource);
            baselink.rule.get(0).handleSwappedGood();
            
            comboBoxSwap(cmbSource, cmbDest, trfmTab.cmbTRFMRequiredResource1);
            comboBoxSwap(cmbSource, cmbDest, trfmTab.cmbTRFMRequiredResource2);
            for (TRFM trfm : baselink.workerJob) {
                trfm.handleSwappedGood();
            }
            
            comboBoxSwap(cmbSource, cmbDest, bldgTab.cmbBLDGReqResource1);
            comboBoxSwap(cmbSource, cmbDest, bldgTab.cmbBLDGReqResource2);
            for (BLDG bldg : baselink.buildings) {
                bldg.handleSwappedGood();
            }
            
            comboBoxSwap(cmbSource, cmbDest, unitTab.cmbPRTORequiredResource1);
            comboBoxSwap(cmbSource, cmbDest, unitTab.cmbPRTORequiredResource2);
            comboBoxSwap(cmbSource, cmbDest, unitTab.cmbPRTORequiredResource3);
            for (PRTO prto : baselink.unit) {
                prto.handleSwappedGOOD();
            }
            
            for (TERR terr : baselink.terrain) {
                terr.handleSwappedGOOD(source, destination);
            }
            terrTab.initializeResourceList();
            terrTab.displayPossibleResources(terrTab.lstTerrains.getSelectedIndex());
        }
    }
    
    @Override
    public boolean checkBounds(List<String>invalidValues) {
        if (goodIndex == -1) {
            return true;
        }
        return super.checkBounds(invalidValues);
    }
    
    public void renameBIQElement(int index, String name)
    {
        resource.get(index).setName(name);
    }
    
    public void deleteAction(GOOD deletedGood)
    {
        int index = -1;
        if (!Main.settings.useJavaFX) {
            index = lstGoods.getSelectedIndex();
        }
        else {
            index = deletedGood.getIndex();
        }
        //Work with dependencies
        if (baselink.hasCustomMap())
        {
            if (mapTab.cmbResource.getSelectedIndex() - 1 == index) {
                mapTab.cmbResource.setSelectedIndex(0);
            }
            else if (mapTab.cmbResource.getSelectedIndex() - 1 > index) {
                mapTab.cmbResource.setSelectedIndex(mapTab.cmbResource.getSelectedIndex() - 1);
            }
            mapTab.resourceModel.removeElementAt(index + 1);
            //And update it for all tiles
            for (int i = 0; i < baselink.tile.size(); i++)
            {
                if (baselink.tile.get(i).resourceInt == index)
                    baselink.tile.get(i).resourceInt = -1;
                else if (baselink.tile.get(i).resourceInt > index)
                    baselink.tile.get(i).resourceInt--;
            }
        }
        //-1 in the combo boxes with noneSelected as an option
        if (ruleTab.cmbRULEDefaultMoneyResource.getSelectedIndex() - 1 == index)
        {
            ruleTab.rule.get(0).setDefaultMoneyResource(-1);
            ruleTab.cmbRULEDefaultMoneyResource.setSelectedIndex(0);
        }
        else if (ruleTab.cmbRULEDefaultMoneyResource.getSelectedIndex() - 1 > index)
        {
            ruleTab.rule.get(0).setDefaultMoneyResource(ruleTab.rule.get(0).getDefaultMoneyResource() - 1);
            ruleTab.cmbRULEDefaultMoneyResource.setSelectedIndex(ruleTab.cmbRULEDefaultMoneyResource.getSelectedIndex() - 1);
        }
        //Should automatically keep the proper index selected
        ruleTab.mdlDefaultMoneyResource.removeElementAt(index + 1);
        //terraform tab updates
        if (trfmTab.cmbTRFMRequiredResource1.getSelectedIndex() - 1 == index)
            trfmTab.cmbTRFMRequiredResource1.setSelectedIndex(0);
        if (trfmTab.cmbTRFMRequiredResource2.getSelectedIndex() - 1 == index)
            trfmTab.cmbTRFMRequiredResource2.setSelectedIndex(0);
        trfmTab.mdlRequiredResource1.removeElementAt(index + 1);
        trfmTab.mdlRequiredResource2.removeElementAt(index + 1);
        //now update values stored for all terraforms
        for (int i = 0; i < trfmTab.workerJob.size(); i++)
        {
            trfmTab.workerJob.get(i).handleDeletedGood(index);
        }
        //building tab updates
        //first, update the tab
        if (bldgTab.cmbBLDGReqResource1.getSelectedIndex() - 1 == index)
            bldgTab.cmbBLDGReqResource1.setSelectedIndex(0);
        if (bldgTab.cmbBLDGReqResource2.getSelectedIndex() - 1 == index)
            bldgTab.cmbBLDGReqResource2.setSelectedIndex(0);
        bldgTab.mdlReqResource1.removeElementAt(index + 1);
        bldgTab.mdlReqResource2.removeElementAt(index + 1);
        //then, update values stored for all buildings
        for (int i = 0; i < bldgTab.buildings.size(); i++)
        {
            if (bldgTab.buildings.get(i).getReqResource1() == index)
                bldgTab.buildings.get(i).setReqResource1(-1);
            else if (bldgTab.buildings.get(i).getReqResource1() > index)
                bldgTab.buildings.get(i).setReqResource1(bldgTab.buildings.get(i).getReqResource1() - 1);
            if (bldgTab.buildings.get(i).getReqResource2() == index)
                bldgTab.buildings.get(i).setReqResource2(-1);
            else if (bldgTab.buildings.get(i).getReqResource2() > index)
                bldgTab.buildings.get(i).setReqResource2(bldgTab.buildings.get(i).getReqResource2() - 1);
        }
        //unit tab updates
        if (unitTab.cmbPRTORequiredResource1.getSelectedIndex() - 1 == index)
            unitTab.cmbPRTORequiredResource1.setSelectedIndex(0);
        if (unitTab.cmbPRTORequiredResource2.getSelectedIndex() - 1 == index)
            unitTab.cmbPRTORequiredResource2.setSelectedIndex(0);
        if (unitTab.cmbPRTORequiredResource3.getSelectedIndex() - 1 == index)
            unitTab.cmbPRTORequiredResource3.setSelectedIndex(0);
        unitTab.mdlRequiredResource1.removeElementAt(index + 1);
        unitTab.mdlRequiredResource2.removeElementAt(index + 1);
        unitTab.mdlRequiredResource3.removeElementAt(index + 1);
        //now update values stored for all units
        for (PRTO prto : unitTab.unit)
        {
            prto.handleDeletedResource(index);
        }
        //finally, update the terrain data
        //for the currently selected item, update the selection
        int[]selected = terrTab.lstTERRResources.getSelectedIndices();
        boolean thisOneFound = false;
        for (int i = 0; i < selected.length; i++)
        {
            if (selected[i] == index)
            {
                thisOneFound = true;
            }
        }
        int len = selected.length;
        if (thisOneFound)
        {
            len--;
        }
        int[]newSelected = new int[len];
        int count = 0;
            for (int j = 0; j < selected.length; j++)
            {
                if (selected[j] < index)
                {
                    newSelected[count] = selected[j];
                    count++;
                }
                else if (selected[j] > index)
                {
                    newSelected[count] = selected[j] - 1;
                    count++;
                }
                //if selected[j] == i & i = index, don't add it
            }
        terrTab.resourceList.removeElementAt(index);
        terrTab.lstTERRResources.setSelectedIndices(newSelected);
        //now update data for all terrains
        //setting terrIndex to 0 not necessary as visuals have been updated
        for (int j = 0; j < terrTab.terrain.size(); j++)
        {
            logger.trace("Removing good " + index + " from terrain " + j);
            terrTab.terrain.get(j).allowedResources.remove(index);
            terrTab.terrain.get(j).numPossibleResources--;
        }
        //terrTab.terrain

        //Good index must be set to -1, or lstGoods will copy the
        //deleted item's values into the next item
        goodIndex = -1;
        utils.removeFromList(index, resource, goodList, lstGoods);
        
        for (GOOD good : resource) {
            if (good.getIndex() > index) {
                good.setIndex(good.getIndex() - 1);
            }
        }
    }
    
    public boolean addItem(String name)
    {
        addGood(name, new GOOD(name, baselink));
        return true;
    }
    
    public void copyItem(GOOD resource, String name) {
        GOOD copiedGood = resource.clone();
        copiedGood.setName(name);
        addGood(name, copiedGood);
    }
    
    /**
     * Shared among add/copy code.  Only difference is whether the new good is totally new, or copied from another.
     * @param name
     * @param newGood 
     */
    public void addGood(String name, GOOD newGood) {
        newGood.setIndex(resource.size());
        resource.add(newGood);

        utils.addWithPossibleDuplicates(name, bldgTab.mdlReqResource1);
        utils.addWithPossibleDuplicates(name, bldgTab.mdlReqResource2);

        utils.addWithPossibleDuplicates(name, unitTab.mdlRequiredResource1);
        utils.addWithPossibleDuplicates(name, unitTab.mdlRequiredResource2);
        utils.addWithPossibleDuplicates(name, unitTab.mdlRequiredResource3);

        utils.addWithPossibleDuplicates(name, ruleTab.mdlDefaultMoneyResource);

        utils.addWithPossibleDuplicates(name, mapTab.resourceModel);

        terrTab.resourceList.addElement(name);
        //have to add another possible resource to the data struct for terrain
        for (int i = 0; i < terrTab.terrain.size(); i++)
            terrTab.terrain.get(i).allowedResources.add(false);

        utils.addWithPossibleDuplicates(name, trfmTab.mdlRequiredResource1);
        utils.addWithPossibleDuplicates(name, trfmTab.mdlRequiredResource2);
    }
}
