package com.civfanatics.civ3.xplatformeditor;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Quintillus
 */
import com.civfanatics.civ3.biqFile.BLDG;
import com.civfanatics.civ3.biqFile.FLAV;
import com.civfanatics.civ3.biqFile.RACE;
import com.civfanatics.civ3.biqFile.TECH;
import com.civfanatics.civ3.biqFile.civ3Version;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
/**
 * This class represents the Flavor tab in the Civilization editor.
 * 
 * @author Andrew
 */
public class FLAVTab extends EditorTab {

    FlavTable tableModel;
    JMenuItem add;
    JMenuItem delete;
    JMenuItem rename;

    /**
     * The constructor.  Performs initialization of the GUI.
     */
    public FLAVTab()
    {
        flavListModel = new DefaultListModel();
        tabName = "FLAV";
        tableModel = new FlavTable();
        scrFlavorRelations = new javax.swing.JScrollPane();
        tblFlavors = new javax.swing.JTable(tableModel);
        scrFlavorList = new javax.swing.JScrollPane();
        lstFlavors = new javax.swing.JList();
        flavIndex = -1;
        this.setName(tabName);
        if (logger.isDebugEnabled())
            logger.debug("intiailized FLAVTab");
        add = new JMenuItem();
        add.setText("Add");
        delete = new JMenuItem();  
        delete.setText("Delete");
        rename = new JMenuItem();
        rename.setText("Rename");
        lstFlavors.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lstFlavorsPopup(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lstFlavorsPopup(evt);
            }
        });
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String name = JOptionPane.showInputDialog("Please choose a name for the new flavor");
                addFlavor(name);

                lstFlavors.setSelectedIndex(flavListModel.size() - 1);
            }
        });
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteFlavor();
            }
        });
        
        rename.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renameFlavor();
            }
        });
    }
    
    private void renameFlavor()
    {
        String existingName = lstFlavors.getSelectedValue().toString();
        String newName = (String) JOptionPane.showInputDialog(null,"Please enter the new name for this flavor","New Flavor Name",JOptionPane.PLAIN_MESSAGE, null, null, existingName);
        if (newName == null || newName.equals(""))
            return;
        if (newName.length() > 255)
        {
            JOptionPane.showMessageDialog(null, "The flavor name cannot exceed 255 characters", "Too long",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int index = lstFlavors.getSelectedIndex();
        flavors.get(index).name = newName;
        
        Vector<String>names = new Vector<String>(flavors.size());
        for (int i = 0; i < flavors.size(); i++)
        {
            names.add(utils.cTrim(flavors.get(i).getName()));
        }
        tableModel.setHeaders(names);
        
        flavListModel.set(index,newName);

        tableModel.updateGUI();

        updateTab();
    }
    
    /**
     * This method is used to delete flavors.
     * Dec 8, 2011
     */
    private void deleteFlavor()
    {
        if (flavors.size() == 1)
        {
            JOptionPane.showMessageDialog(null, "You cannot delete the last flavor.", "Cannot delete", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int index = lstFlavors.getSelectedIndex();
        for (BLDG bldg : baselink.buildings)
        {
            bldg.handleDeletedFlavour(index);
        }
        for (TECH tech : baselink.technology)
        {
            tech.handleDeletedFlavour(index);
        }        
        for (RACE civilization : baselink.civilization)
        {
            civilization.handleDeletedFlavour(index);
        }        
        flavors.remove(index);
        for (int i = 0; i < flavors.size(); i++)
        {
            flavors.get(i).relationWithOtherFlavor.remove(index);
            flavors.get(i).numberOfFlavors--;
        }
        //and the table
        tableModel.removeFlavor(index);
        //and also the list
        flavListModel.removeAllElements();
        lstFlavors.setModel(flavListModel);
        for (int i = 0; i < flavors.size(); i++)
        {
            flavListModel.addElement(utils.cTrim(flavors.get(i).name));
        }

        //and the headers
        Vector<String>names = new Vector<String>(flavors.size());
        for (int i = 0; i < flavors.size(); i++)
        {
            names.add(utils.cTrim(flavors.get(i).getName()));
        }
        tableModel.setHeaders(names);
        updateTab();
        
    }
    
    private void addFlavor(String name)
    {
        //set up relations w/other flavors
        for (int i = 0; i < flavors.size(); i++)
        {
            flavors.get(i).numberOfFlavors++;
            flavors.get(i).relationWithOtherFlavor.add(50); //default relation with new flavor
        }
        flavors.add(new FLAV(baselink, name));
        int newsize = flavors.size();   //save some calls with a local
        for (int i = 0; i < newsize; i++)
            flavors.get(newsize - 1).relationWithOtherFlavor.add(50);
        flavors.get(newsize - 1).numberOfFlavors = newsize;
        //Now we need to address GUI concerns
        updateData(flavors);
        
        
        //TODO: Add flavors to each BLDG, CIV, TECH.  This probably will fail miserably without that.
        //Although maybe not since they are stored in binary
        for (BLDG bldg : baselink.buildings)
        {
            bldg.handleAddedFlavour();
        }
        for (TECH tech : baselink.technology)
        {
            tech.handleAddedFlavour();
        }        
        for (RACE civilization : baselink.civilization)
        {
            civilization.handleAddedFlavour();
        }
    }
    
    private void lstFlavorsPopup(java.awt.event.MouseEvent evt){
        if (evt.isPopupTrigger())
        {
            lstFlavors.setSelectedIndex(lstFlavors.locationToIndex(new java.awt.Point(evt.getX(), evt.getY())));
            JPopupMenu popUp = new JPopupMenu();
            popUp.add(add);
            popUp.add(rename);
            popUp.add(delete);
            Component component = evt.getComponent();
            int x = evt.getX();
            int y = evt.getY();
            popUp.show(component, x, y);
        }
    }
    /**
     * Tells the tab to set flavor <i>i</i> as the selected flavor.
     * @param i - The flavor to be selected.  The first item is i = 0.
     */
    public void setSelectedIndex(int i)
    {
        flavIndex = i;
    }

    public void updateTab()
    {   
        //JOptionPane.showMessageDialog(null, "Updating flavor tab");
        //If this tab isn't supposed to exist, return
        if (baselink.version != civ3Version.CONQUESTS)
            return;
        //may be implemented later (most likely, *should* be implemented)
        if (!(flavIndex == -1)) {
        //store data from table's model
            if (logger.isDebugEnabled())
                logger.debug("saving flavor data");
             for (int i = 0; i < flavors.size(); i++)
             {
                 for (int j = 0; j < flavors.size(); j++)
                 {
                    if (logger.isDebugEnabled())
                        logger.debug("i: " + i + ", j: " + j);
                     if (i == 7 && j == 0)
                        if (logger.isDebugEnabled())
                            logger.debug("debug')");
                     String valueAt = String.valueOf(tableModel.getValueAt(i, j+1));
                     flavors.get(i).relationWithOtherFlavor.set(j, Integer.valueOf(valueAt));
                 }
             }
        }
        flavIndex = 0;
        displayData();
    }
    private void displayData()
    {
        if (flavIndex != -1)
        {
            if (logger.isDebugEnabled())
                logger.debug("updating table");
            tableModel.sendUpdate(flavors);
        }
    }
    /**
     * Creates the GUI of the tab.  This includes button groups, layouts, etc.
     * Logically, this is to occur immediately after the constructor is called.
     *
     * @return - A reference to the FLAVTab instance itself.  This reference
     * allows the tab to be added to a tab bar.
     */
    public JPanel createTab()
    {
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints g = new GridBagConstraints();
        
        this.setLayout(gbl);
        
        tblFlavors.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblFlavors.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblFlavors.setCellSelectionEnabled(true);
        scrFlavorRelations.setViewportView(tblFlavors);
        scrFlavorRelations.setPreferredSize(new java.awt.Dimension(850, 600));
        scrFlavorRelations.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
        
        scrFlavorList.setViewportView(lstFlavors);
        
        g.gridwidth=1;
        g.gridheight=1;
        g.gridx=0;
        g.gridy=0;
        g.weighty=1;
        g.weightx=0.125;
        g.fill = GridBagConstraints.BOTH;
        
        this.add(scrFlavorList,g);
        g.gridx++;
        g.weightx=0.875;
        //g.insets = new Insets(5,5,5,5);
        this.add(scrFlavorRelations,g);
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
     * @param flavors - The list of all flavors.
     */
    public void sendData(List<FLAV>flavors)
    {
        this.flavors = flavors;
        //reset table
        tableModel.clear();
        for (int i = 0; i < flavors.size(); i++)
        {
            tableModel.addRow();
        }
        for (int i = -1; i < flavors.size(); i++)
        {
            tableModel.addColumn();
        }

        if (logger.isDebugEnabled())
            logger.debug(tableModel.getRowCount() + ", col: " + tableModel.getColumnCount());
        //set headers
        Vector<String>names = new Vector<String>(flavors.size());
        for (int i = 0; i < flavors.size(); i++)
        {
            names.add(utils.cTrim(flavors.get(i).getName()));
        }
        tableModel.setHeaders(names);

        tableModel.updateGUI();

        flavListModel.removeAllElements();
        lstFlavors.setModel(flavListModel);
        for (int i = 0; i < flavors.size(); i++)
        {
            flavListModel.addElement(utils.cTrim(flavors.get(i).name));
        }

        updateTab();

    }
    
    /**
     * Assumes one new flavor.
     * @param flavors 
     */
    public void updateData(List<FLAV>flavors)
    {
        this.flavors = flavors;
        //add to table
        tableModel.addFullRow();
        tableModel.addColumn(50);
        if (logger.isDebugEnabled())
            logger.debug(tableModel.getRowCount() + ", col: " + tableModel.getColumnCount());
        //set headers
        Vector<String>names = new Vector<String>(flavors.size());
        for (int i = 0; i < flavors.size(); i++)
        {
            names.add(utils.cTrim(flavors.get(i).getName()));
        }
        tableModel.setHeaders(names);

        tableModel.updateGUI();

        flavListModel.removeAllElements();
        lstFlavors.setModel(flavListModel);
        for (int i = 0; i < flavors.size(); i++)
        {
            flavListModel.addElement(utils.cTrim(flavors.get(i).name));
        }

        updateTab();

    }

    public void sendTabLinks(BldgTab bldgTab, CIVTab civTab, TechTab techTab)
    {
        //auto-updated l33tness!
        bldgTab.lstBLDGFlavors.setModel(flavListModel);
        civTab.lstFlavors.setModel(flavListModel);
        techTab.lstFlavors.setModel(flavListModel);
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
    //TODO: implement non-none limits in Flavor tab
    /**
     * Sets 'minimal' limits for text fields - in practices, only length limits.
     */
    public void setMinimalLimits()
    {
        setNoLimits();
    }
    /**
     * Sets 'exporatory' limits for text fields - anything not known to cause
     * a problem is allowed.
     */
    public void setExploratoryLimits()
    {
        setNoLimits();
    }
    /**
     * Sets 'safe' limits for text fields - anything known not to cause a problem
     * is allowed.
     */
    public void setSafeLimits()
    {
        setNoLimits();
    }
    /**
     * Sets 'Firaxis' limits for text fields - attempts to match the limits set
     * by Firaxis's editor as closely as possible.
     */
    public void setFiraxisLimits()
    {
        setNoLimits();
    }
    /**
     * Sets 'Firaxis' limits for text fields, and attempts to prevent the user
     * from any other problematic settings they may inadvertantly cause.
     */
    public void setTotalLimits()
    {
        setNoLimits();
    }

    private DefaultListModel flavListModel;
    public List<FLAV>flavors = new ArrayList<FLAV>();
    int flavIndex;
    private javax.swing.JList lstFlavors;
    private javax.swing.JScrollPane scrFlavorList;
    private javax.swing.JScrollPane scrFlavorRelations;
    private javax.swing.JTable tblFlavors;
}
