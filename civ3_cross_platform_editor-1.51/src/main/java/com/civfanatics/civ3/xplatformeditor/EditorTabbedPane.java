/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.civfanatics.civ3.xplatformeditor;

import com.civfanatics.civ3.xplatformeditor.specialty.SafetyLevel;
import com.civfanatics.civ3.xplatformeditor.tabs.biqc.BIQCTab;
import com.civfanatics.civ3.xplatformeditor.Listeners.CustomAdjustmentListener;
import com.civfanatics.civ3.biqFile.civ3Version;
import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import org.apache.log4j.Logger;

/**
 *
 * @author Andrew
 */
public class EditorTabbedPane extends JTabbedPane{


    EditorTab previousTab;

    private static EditorTab[]editorTabs;

    public BIQCTab biqcTab;
    private CultTab cultTab;
    private BldgTab bldgTab;
    private GoodTab goodTab;
    private CIVTab civTab;
    private DIFFTab diffTab;
    private UnitTab unitTab;
    private TechTab techTab;
    private CTZNTab ctznTab;
    private RULETab ruleTab;
    private GOVTTab govtTab;
    private TERRTab terrTab;
    public GAMETab gameTab;
    private EraTab eraTab;
    private ESPNTab espnTab;
    private TRFMTab trfmTab;
    private WSIZTab wsizTab;
    private EXPRTab exprTab;
    PLYRTab plyrTab;
    private FLAVTab flavTab;
    
    
    JPanel pnlRULE;
    JPanel pnlBLDG;


    //EXpERImENTaL - Map panel\
    private JScrollPane mapScroll;
    MapTab mapTab;
    private CustomAdjustmentListener scollListener;

    Color[]colors;
    BufferedImage[][]unitIcons;

    Logger logger = Logger.getLogger(EditorTabbedPane.class);

    public EditorTabbedPane()
    {
        super();
    }

    public void loadInterfaceElements(Color[]colors, BufferedImage[][]unitIcons, BufferedImage[]resourceIcons, BufferedImage units32)
    {
        boolean flavRemoved = false;
        
        this.colors = colors;
        this.unitIcons = unitIcons;
        unitTab.sendUnitIcons(unitIcons, units32);
        //may need to set indices to -1

        //Send IO links first
        //This is important, because the GAME tab will reference the base IO
        //to get the title and description.
        for (EditorTab e: editorTabs){
            e.sendIOLink(Main.biqFile.get(Main.biqIndex));
            e.newData = true;
            e.setSelectedIndex(-1);
        }
        if (logger.isInfoEnabled())
            logger.info("IO links sent");
        
        //We need to divvy this up into four sections - one for always send, one for send rules, one for sent player data, one for send map
        if (Main.biqFile.get(Main.biqIndex).hasCustomMap())
        {
            gameTab.sendMapData(Main.biqFile.get(Main.biqIndex).worldCharacteristic);
        }
        
        if (Main.biqFile.get(Main.biqIndex).hasCustomRules())
        {
            sendRuleData();
        }
        else
        {          
            sendRuleData();
            for (int i = 1; i < 20; i++) {
                //Tab 12 is PLYR
                if (i != 12 && i != 14) {
                    this.setEnabledAt(i, false);
                }
            }
            flavRemoved = true;  //so we don't try to twice if it ain't PTW
        }
        
        //Safety Limits
        
        
        if (Main.biqFile.get(Main.biqIndex).hasCustomPlayerData())
        {
            sendPlayerData();
            diffTab.sendTabLinks(ruleTab, plyrTab);
            govtTab.sendTabLinks(bldgTab, civTab, plyrTab);
            techTab.sendPlayerTab(plyrTab);
            unitTab.sendPlayerTab(plyrTab);
        }
        else
        {
            if (logger.isInfoEnabled())
                logger.info("Detected no custom player data");
            plyrTab.setAllEnabled(false);
            this.setEnabledAt(12, false);
            diffTab.sendTabLinks(ruleTab);
            govtTab.sendTabLinks(bldgTab, civTab);
        }
        ruleTab.sendDiffTab(diffTab);
        
        if (logger.isInfoEnabled())
            logger.info("data links sent to tabs");
        
        //no data yet send to player tab
        
        //Send cross-tab links for easier communication
        civTab.sendTabLinks(unitTab, gameTab);
        goodTab.sendTabLinks(terrTab, trfmTab, unitTab, bldgTab, ruleTab, mapTab);
        unitTab.sendTabLinks(bldgTab, civTab, ruleTab, mapTab, gameTab);
        techTab.sendTabLinks(goodTab, bldgTab, civTab, unitTab, ctznTab, govtTab, trfmTab);
        if (Main.biqFile.get(Main.biqIndex).version == civ3Version.CONQUESTS)
            flavTab.sendTabLinks(bldgTab, civTab, techTab);
        bldgTab.sendTabLinks(unitTab, mapTab);
        if (Main.biqFile.get(Main.biqIndex).hasCustomPlayerData() && Main.biqFile.get(Main.biqIndex).hasCustomMap()) {
            plyrTab.sendTabLinks(mapTab, gameTab);
        }
        if (logger.isInfoEnabled())
            logger.info("crosstab links sent");
        
        //New data is finished being sent.
        for (EditorTab e: editorTabs){
            e.newData = false;
        }
        if (!flavRemoved && Main.biqFile.get(Main.biqIndex).version != civ3Version.CONQUESTS)
        {
            remove(flavTab);
        }
        if (Main.GRAPHICS_ENABLED)
        {
            goodTab.sendGoodIcons(resourceIcons);
        }
    }
    
    void enableCustomRules() {
        for (int i = 1; i < 20; i++) {
            //Tab 12 is PLYR, 14 is PROP
            if (i != 12 && i != 14) {
                this.setEnabledAt(i, true);
            }
        }
        biqcTab.updateCheckBoxes();
    }
    
    void enableCustomPlayerData() {
        this.sendPlayerData();
        plyrTab.setAllEnabled(true);
        this.setEnabledAt(12, true);
        biqcTab.updateCheckBoxes();
    }
    
    public void sendRuleData()
    {
        //Load other tabs with separate rules (not stored in main Main class)
        logger.debug("About to send to CULT");
        cultTab.sendData(Main.biqFile.get(Main.biqIndex).culture, Main.biqFile.get(Main.biqIndex).rule);
        logger.debug("About to send to GOOD");
        goodTab.sendData(Main.biqFile.get(Main.biqIndex).resource, Main.biqFile.get(Main.biqIndex).technology);
        logger.debug("About to send to BLDG");
        bldgTab.sendData(Main.biqFile.get(Main.biqIndex).buildings, Main.biqFile.get(Main.biqIndex).resource, Main.biqFile.get(Main.biqIndex).government, Main.biqFile.get(Main.biqIndex).technology, Main.biqFile.get(Main.biqIndex).unit, Main.biqFile.get(Main.biqIndex).rule);
        logger.debug("Sent to BLDG< now to CIV");
        civTab.sendData(Main.biqFile.get(Main.biqIndex).civilization, Main.biqFile.get(Main.biqIndex).unit, Main.biqFile.get(Main.biqIndex).government, Main.biqFile.get(Main.biqIndex).eras, Main.biqFile.get(Main.biqIndex).technology, colors);
        diffTab.sendData(Main.biqFile.get(Main.biqIndex).difficulties);
        unitTab.sendData(Main.biqFile.get(Main.biqIndex).unit, Main.biqFile.get(Main.biqIndex).technology, Main.biqFile.get(Main.biqIndex).resource, Main.biqFile.get(Main.biqIndex).terrain, Main.biqFile.get(Main.biqIndex).civilization, Main.biqFile.get(Main.biqIndex).buildings);
        techTab.sendData(Main.biqFile.get(Main.biqIndex).technology, Main.biqFile.get(Main.biqIndex).eras);
        ctznTab.sendData(Main.biqFile.get(Main.biqIndex).citizens, Main.biqFile.get(Main.biqIndex).technology);
        ruleTab.sendData(Main.biqFile.get(Main.biqIndex).rule, Main.biqFile.get(Main.biqIndex).unit, Main.biqFile.get(Main.biqIndex).difficulties, Main.biqFile.get(Main.biqIndex).resource, Main.biqFile.get(Main.biqIndex).buildings);
        govtTab.sendData(Main.biqFile.get(Main.biqIndex).government, Main.biqFile.get(Main.biqIndex).experience, Main.biqFile.get(Main.biqIndex).espionage, Main.biqFile.get(Main.biqIndex).technology);
        terrTab.sendData(Main.biqFile.get(Main.biqIndex).terrain, Main.biqFile.get(Main.biqIndex).workerJob, Main.biqFile.get(Main.biqIndex).resource);
        gameTab.sendRuleData(Main.biqFile.get(Main.biqIndex).scenarioProperty, Main.biqFile.get(Main.biqIndex).civilization);
        eraTab.sendData(Main.biqFile.get(Main.biqIndex).eras);
        espnTab.sendData(Main.biqFile.get(Main.biqIndex).espionage);
        trfmTab.sendData(Main.biqFile.get(Main.biqIndex).workerJob, Main.biqFile.get(Main.biqIndex).technology, Main.biqFile.get(Main.biqIndex).resource);
        wsizTab.sendData(Main.biqFile.get(Main.biqIndex).worldSize);
        exprTab.sendData(Main.biqFile.get(Main.biqIndex).experience);

        if (Main.biqFile.get(Main.biqIndex).version == civ3Version.CONQUESTS)
            flavTab.sendData(Main.biqFile.get(Main.biqIndex).flavor);
    }
    
    public void sendPlayerData()
    {
        plyrTab.sendData(Main.biqFile.get(Main.biqIndex).scenarioProperty, Main.biqFile.get(Main.biqIndex).player, colors, Main.biqFile.get(Main.biqIndex).technology, Main.biqFile.get(Main.biqIndex).unit, Main.biqFile.get(Main.biqIndex).difficulties, Main.biqFile.get(Main.biqIndex).government, Main.biqFile.get(Main.biqIndex).eras, Main.biqFile.get(Main.biqIndex).civilization, Main.biqFile.get(Main.biqIndex));
        if (logger.isInfoEnabled())
            logger.info("custom player data sent");
    }

    public void alertBIQCTab(File file)
    {
        biqcTab.alertToNewlyOpenedFile(file);
    }

    public void initialTabUpdate()
    {
        for (int i = 0; i < editorTabs.length; i++)
        {
            if (editorTabs[i].isActive && editorTabs[i].lstType != null)
            {
                editorTabs[i].updateTab();
                if (editorTabs[i] != plyrTab || Main.biqFile.get(Main.biqIndex).hasCustomPlayerData())
                    editorTabs[i].lstType.setSelectedIndex(0);
            }

        }
    }

    public void setup(Main main)
    {

        addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                pnlTabsStateChanged(evt);
            }
        });

        setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        setVisible(false);
        biqcTab = new BIQCTab();
        cultTab = new CultTab();
        bldgTab = new BldgTab();
        civTab = new CIVTab();
        goodTab = new GoodTab();
        diffTab = new DIFFTab();
        unitTab = new UnitTab();
        techTab = new TechTab();
        ctznTab = new CTZNTab();
        ruleTab = new RULETab();
        govtTab = new GOVTTab();
        terrTab = new TERRTab();
        gameTab = new GAMETab();
        eraTab = new EraTab();
        espnTab = new ESPNTab();
        trfmTab = new TRFMTab();
        wsizTab = new WSIZTab();
        exprTab = new EXPRTab();
        plyrTab = new PLYRTab();
        flavTab = new FLAVTab();
        mapTab = new MapTab(mapScroll, scollListener, main);
        JPanel pnlBIQC = biqcTab.createTab();
        JPanel pnlCULT = cultTab.createTab();
        pnlBLDG = bldgTab.createTab();
        JPanel pnlGOOD = goodTab.createTab();
        JPanel pnlCIV = civTab.createTab();
        JPanel pnlDIFF = diffTab.createTab();
        JPanel pnlPRTO = unitTab.createTab();
        JPanel pnlTECH = techTab.createTab();
        JPanel pnlCTZN = ctznTab.createTab();
        pnlRULE = ruleTab.createTab();
        JPanel pnlGOVT = govtTab.createTab();
        JPanel pnlTERR = terrTab.createTab();
        JPanel pnlGAME = gameTab.createTab();
        JPanel pnlERA = eraTab.createTab();
        JPanel pnlESPN = espnTab.createTab();
        JPanel pnlTRFM = trfmTab.createTab();
        JPanel pnlWSIZ = wsizTab.createTab();
        JPanel pnlEXPR = exprTab.createTab();
        JPanel pnlPLYR = plyrTab.createTab();
        JPanel pnlFLAV = flavTab.createTab();
        try{
            //addTabb("BIQC", pnlBIQC);
            addTabb("BIC", pnlBIQC);
            addTabb("BLDG", pnlBLDG);
            addTabb("CIV", pnlCIV);
            addTabb("CTZN", pnlCTZN);
            addTabb("CULT", pnlCULT);
            addTabb("DIFF", pnlDIFF);
            addTabb("ERA", pnlERA);
            addTabb("ESPN", pnlESPN);
            addTabb("EXPR", pnlEXPR);
            addTabb("FLAV", pnlFLAV);
            addTabb("GOOD", pnlGOOD);
            addTabb("GOVT", pnlGOVT);
            addTabb("PLYR", pnlPLYR);    //not finished
            addTabb("RULE", pnlRULE);
            //addTabb("ScnProp", pnlGAME);
            addTabb("PROP", pnlGAME); //some work left
            addTabb("TECH", pnlTECH);
            addTabb("TERR", pnlTERR);
            addTabb("TFRM", pnlTRFM);
            addTabb("Unit", pnlPRTO);
            addTabb("WSIZ", pnlWSIZ);
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            logger.error("Caught!", e);
            logger.error(e.getCause());
        }
        //ExPeRiMeNtAl
        if (Main.GRAPHICS_ENABLED)
        {
            addTab("MAP", mapTab);
            //and now disable it; it will be the last tab
            int i = getTabCount();
            setEnabledAt(i - 1, false);
        }

        //Most tabs are updated when a file is opened.
        //The biqc tab needs to have access to the Main.biqFile ArrayList before
        //all the data is loaded
        biqcTab.sendData(Main.biqFile, null, main);

        editorTabs = new EditorTab[19];
        editorTabs[0] = bldgTab;
        editorTabs[1] = ctznTab;
        editorTabs[2] = cultTab;
        editorTabs[3] = diffTab;
        editorTabs[4] = eraTab;
        editorTabs[5] = espnTab;
        editorTabs[6] = exprTab;
        editorTabs[7] = flavTab;
        editorTabs[8] = goodTab;
        editorTabs[9] = govtTab;
        editorTabs[10] = gameTab;
        editorTabs[11] = unitTab;
        editorTabs[12] = techTab;
        editorTabs[13] = terrTab;
        editorTabs[14] = trfmTab;
        editorTabs[15] = wsizTab;
        editorTabs[16] = plyrTab;
        editorTabs[17] = ruleTab;
        editorTabs[18] = civTab;
    }

    private void pnlTabsStateChanged(javax.swing.event.ChangeEvent evt) {
        if (Main.fileOpen)
        {
            if (logger.isDebugEnabled())
                logger.debug("State of tab panel changed");
            Object selectedComponent = getSelectedComponent();
            if (selectedComponent instanceof JScrollPane) {
                selectedComponent = ((JScrollPane)selectedComponent).getViewport().getView();
            }
            if (selectedComponent instanceof JPanel)
            {
                if (logger.isTraceEnabled())
                    logger.trace("instaneof JPanel");
                if (previousTab != null)
                {
                    if (logger.isDebugEnabled())
                        logger.debug("tab changed.  sending update notice to previous tab " + ((JPanel)previousTab).getName());
                    previousTab.updateTab();
                    previousTab.setSelectedIndex(-1);
                }

                JPanel tmpPanel = (JPanel)selectedComponent;
                String[]editorTabPanels = {"BLDG", "RACE", "CTZN", "CULT", "DIFF", "ERAS", "ESPN", "EXPR", "FLAV", "GOVT", "GOOD", "RULE", "GAME", "TECH", "TERR", "TRFM", "PRTO", "WSIZ", "PLYR"};
                String pnlName = tmpPanel.getName();
                boolean instanceofEditorTab = false;
                //Todo: Updated this clumsy check method
                for (int i = 0; i < editorTabPanels.length; i++)
                    if (editorTabPanels[i].equals(pnlName))
                        instanceofEditorTab = true;
                //set the new tab as the "previous" one, so the next time a new tab is selected, this one can be updated
                if (instanceofEditorTab)
                {
                    if (logger.isDebugEnabled())
                        logger.debug("new tab is instanceof EditorPanel.  updating appropriately");
                    //hack
                    if (previousTab == null && (EditorTab)tmpPanel == unitTab)
                        unitTab.displayIcon(Main.biqFile.get(Main.biqIndex).unit.get(0).getIconIndex());
                    previousTab = (EditorTab)tmpPanel;
                    if (tmpPanel == pnlRULE)
                        ruleTab.updateTab();
                    //hack
                    if (tmpPanel == gameTab)
                        gameTab.setSelectedIndex(0);
                    if (pnlName.equals("PLYR")) {
                        plyrTab.updateTab();
                    }

                }
                //otherwise, it's a special tab that doesn't include those methods
                if (logger.isDebugEnabled())
                    logger.debug("Selected component: " + tmpPanel.getName() + "\n" + tmpPanel.toString());
                //psuedocode
                //tmpPanel.saveData(); (that is, updateTab();
                //tmpPanel.selectedIndex = -1;
            }
            //if it's a map panel - only do this stuff if the map panel is present
            if (Main.GRAPHICS_ENABLED)
            {
                if(getSelectedComponent() == mapTab)
                {
                    if (logger.isDebugEnabled())
                        logger.debug("Map panel selected");
                    mapTab.map.activeTab = true;
                    mapTab.map.triggerUpdates();
                    mapTab.showGraphics();
                }
                else
                {
                    if (logger.isDebugEnabled())
                        logger.debug("Map panel not selected");
                    mapTab.map.activeTab = false;
                }
            }
        }

    }
    
    public void addTabb(String title, Component component)
    {
        JScrollPane scrPane = new JScrollPane(component);
        scrPane.getHorizontalScrollBar().setUnitIncrement(10);
        scrPane.getVerticalScrollBar().setUnitIncrement(10);
        addTab(title, scrPane);
    }

    public boolean checkBounds()
    {
        List<String> valuesWithErrors = new ArrayList<String>();
        for (EditorTab e : editorTabs)
        {
            if (!e.checkBounds(valuesWithErrors))
            {
                if (valuesWithErrors.size() == 0) {
                    JOptionPane.showMessageDialog(null, "At least one value on tab " + e.getName() + " does not fall within the bounds of your safety level.\nPlease change safety levels, or change the value(s) such that they fall within the bounds of the safety level.", "Unsafe values!", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("The follow values on the ").append(e.getName()).append(" tab are not valid:\n\n\t");
                    for (String value : valuesWithErrors) {
                        sb.append(value).append("\n");
                    }
                    sb.append("\nPlease change safety levels, or change the value(s) such that they fall within the bounds of the safety level.");
                    JOptionPane.showMessageDialog(null, sb.toString(), "Unsafe values!", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
            }
        }
        return true;
    }

    public void updateAllTabs()
    {
        for (int i = 0; i < editorTabs.length; i++)
            editorTabs[i].updateTab();
    }

    public void triggerMapUpdates()
    {
        if (Main.GRAPHICS_ENABLED && mapTab != null)
            mapTab.map.triggerUpdates();
    }

    public void importMapGraphics()
    {
        GraphicsImport gi = new GraphicsImport(Main.biqFile.get(Main.biqIndex), Main.settings, mapTab.map, colors, this, mapTab, unitIcons);
        gi.start();
    }

    public void alertToSafetyUpdate(Map safetyLevels)
    {
        if (logger.isInfoEnabled())
            logger.info("The user requested a change in the safety levels");
        for (EditorTab e : editorTabs)
        {
            if (logger.isDebugEnabled())
                logger.debug("Safety level for " + e.tabName + ": " + (SafetyLevel)safetyLevels.get(e.tabName));
            String tabName = e.tabName.equals("GAME") ? "PROP" : e.tabName;
            e.setSafetyLevel((SafetyLevel)safetyLevels.get(tabName));
        }
    }
}
