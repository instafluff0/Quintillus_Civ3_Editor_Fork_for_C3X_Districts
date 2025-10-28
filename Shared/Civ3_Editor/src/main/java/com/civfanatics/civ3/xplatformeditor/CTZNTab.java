package com.civfanatics.civ3.xplatformeditor;
/*
 * @Completed
 */
import com.civfanatics.civ3.biqFile.BIQSection;
import com.civfanatics.civ3.biqFile.CTZN;
import com.civfanatics.civ3.biqFile.TECH;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJList;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJTextField;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
/**
 * This class represents the Citizen tab within the Civilization editor.
 * @author Andrew
 */
public class CTZNTab extends EditorTab {

    boolean tabCreated;    
    public List<CTZN>citizens;
    public List<TECH>technology;
    int citizenIndex;
    private SuperListModel citizenList;

    private JCheckBox chkCTZNDefaultCitizen;
    public JComboBox cmbCTZNPrerequisite;
    private JLabel jLabel36;
    private JLabel jLabel37;
    private JLabel jLabel38;
    private JLabel jLabel39;
    private JLabel jLabel40;
    private JLabel jLabel41;
    private JLabel jLabel42;
    private JLabel jLabel43;
    private JPanel jPanel24;
    private JScrollPane jScrollPane3;
    private SuperJList lstCitizens = new SuperJList(this, "citizen", false);
    //private JPanel this;
    private SuperJTextField txtCTZNCivilopediaEntry;
    private SuperJTextField txtCTZNConstruction;
    private SuperJTextField txtCTZNCorruption;
    private SuperJTextField txtCTZNLuxuries;
    private SuperJTextField txtCTZNPluralName;
    private SuperJTextField txtCTZNResearch;
    private SuperJTextField txtCTZNTaxes;
    
    DefaultComboBoxModel mdlPrerequisite = new DefaultComboBoxModel();

    JMenuItem delete;
    JMenuItem add;

    public CTZNTab()
    {
        lstType = lstCitizens;
        tabName = "CTZN";
        textBoxes = new ArrayList<>();
        //this = new JPanel();
        jScrollPane3 = new JScrollPane();
        jPanel24 = new JPanel();
        txtCTZNLuxuries = new SuperJTextField();
        txtCTZNResearch = new SuperJTextField();
        txtCTZNTaxes = new SuperJTextField();
        txtCTZNCorruption = new SuperJTextField();
        txtCTZNConstruction = new SuperJTextField();
        jLabel36 = new JLabel();
        jLabel37 = new JLabel();
        jLabel38 = new JLabel();
        jLabel39 = new JLabel();
        jLabel40 = new JLabel();
        jLabel41 = new JLabel();
        jLabel42 = new JLabel();
        txtCTZNPluralName = new SuperJTextField();
        txtCTZNCivilopediaEntry = new SuperJTextField();
        chkCTZNDefaultCitizen = new JCheckBox();
        jLabel43 = new JLabel();
        cmbCTZNPrerequisite = new JComboBox();
        citizenIndex = -1;
    }
    /**
     * Creates the GUI of the tab.  This includes button groups, layouts, etc.
     * Logically, this is to occur immediately after the constructor is called.
     *
     * @return - A reference to the CTZNTab instance itself.  This reference
     * allows the tab to be added to a tab bar.
     */
    public JPanel createTab()
    {
        lstCitizens.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstCitizens.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstCitizensValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(lstCitizens);

        jPanel24.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Bonuses"));


        jLabel36.setText("Entertainment");

        jLabel37.setText("Science");

        jLabel38.setText("Taxes");

        jLabel39.setText("Corruption");

        jLabel40.setText("Construction");

        org.jdesktop.layout.GroupLayout jPanel24Layout = new org.jdesktop.layout.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel24Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel24Layout.createSequentialGroup()
                        .add(jPanel24Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel40)
                            .add(jLabel37)
                            .add(jLabel38)
                            .add(jLabel39))
                        .add(44, 44, 44))
                    .add(jPanel24Layout.createSequentialGroup()
                        .add(jLabel36)
                        .add(24, 24, 24)))
                .add(jPanel24Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(txtCTZNResearch)
                    .add(txtCTZNTaxes)
                    .add(txtCTZNCorruption)
                    .add(txtCTZNConstruction, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel24Layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtCTZNLuxuries, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel24Layout.createSequentialGroup()
                .add(jPanel24Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel24Layout.createSequentialGroup()
                        .add(120, 120, 120)
                        .add(jLabel40))
                    .add(jPanel24Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jPanel24Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(txtCTZNLuxuries, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel36))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel24Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(txtCTZNResearch, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel37))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel24Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(txtCTZNTaxes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel38))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel24Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(txtCTZNCorruption, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel39))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtCTZNConstruction, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel41.setText("Plural Name:");

        jLabel42.setText("Civilopedia Entry:");

        chkCTZNDefaultCitizen.setText("Default Citizen");

        jLabel43.setText("Prerequisite:");

        cmbCTZNPrerequisite.setModel(mdlPrerequisite);

        org.jdesktop.layout.GroupLayout thisLayout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(thisLayout);
        thisLayout.setHorizontalGroup(
            thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(thisLayout.createSequentialGroup()
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 161, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(thisLayout.createSequentialGroup()
                        .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(jLabel41, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(jLabel42, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .add(jLabel43))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(cmbCTZNPrerequisite, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(txtCTZNCivilopediaEntry)
                            .add(txtCTZNPluralName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)))
                    .add(chkCTZNDefaultCitizen, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 139, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(378, Short.MAX_VALUE))
        );
        thisLayout.setVerticalGroup(
            thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE)
            .add(thisLayout.createSequentialGroup()
                .addContainerGap()
                .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel41)
                    .add(txtCTZNPluralName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel42)
                    .add(txtCTZNCivilopediaEntry, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel43)
                    .add(cmbCTZNPrerequisite, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(chkCTZNDefaultCitizen)
                .addContainerGap(541, Short.MAX_VALUE))
            .add(thisLayout.createSequentialGroup()
                .add(jPanel24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(496, Short.MAX_VALUE))
        );
        this.setName("CTZN");

        tabCreated = true;
        return this;
    }
    
    public void deleteAction()
    {
        int index = lstCitizens.getSelectedIndex();
        //Citizen index must be set to -1, or lstCitizens will copy the
        //deledeleteted item's values into the next item
        citizenIndex = -1;
        utils.removeFromList(index, citizens, citizenList, lstCitizens);        
    }
    
    public boolean addItem(String name)
    {
        
        citizens.add(new CTZN(name, baselink));
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
     * @param citizens - The list of all the citizens.
     * @param technology - The list of all the technologies.
     */
    public void sendData(List<CTZN>citizens, List<TECH>technology)
    {
        assert tabCreated:"Tab must be created before data can be sent";
        this.citizens = citizens;
        this.technology = technology;

        citizenList = new SuperListModel();
        lstCitizens.setModel(citizenList);

        cmbCTZNPrerequisite.removeAllItems();
        cmbCTZNPrerequisite.addItem(noneSelected);

        for (int i = 0; i < technology.size(); i++)
        {
            //add technologies to combo lists that need technologies
            utils.addWithPossibleDuplicates(technology.get(i).getName(), mdlPrerequisite);
        }
        for (int i = 0; i < citizens.size(); i++)
        {
            citizenList.addElement(citizens.get(i).getName());
        }
    }

    private void lstCitizensValueChanged(javax.swing.event.ListSelectionEvent evt) {

        //only adjust after the user has released the mouse, or to an adjustment
        //caused by another user action (adding/deleting an item, for instance)
        if (evt.getValueIsAdjusting())
            return;
        updateTab();
    }

    public void setSelectedIndex(int i)
    {
        citizenIndex = i;
    }
    /**
     * {@inheritDoc}
     */
    public void updateTab()
    {
        if (citizenIndex != -1) {   //back up the current values
            CTZN clone = (CTZN)citizens.get(citizenIndex).clone();
            
            if (chkCTZNDefaultCitizen.isSelected()) //it's the default citizen
            {   //1 is True for this value
                citizens.get(citizenIndex).setDefaultCitizen(1);
            } else {
                citizens.get(citizenIndex).setDefaultCitizen(0);
            }
            citizens.get(citizenIndex).setCivilopediaEntry(txtCTZNCivilopediaEntry.getText());
            citizens.get(citizenIndex).setPluralName(txtCTZNPluralName.getText());
            citizens.get(citizenIndex).setPrerequisite(cmbCTZNPrerequisite.getSelectedIndex() - 1);
            citizens.get(citizenIndex).setLuxuries(txtCTZNLuxuries.getInteger());
            citizens.get(citizenIndex).setResearch(txtCTZNResearch.getInteger());
            citizens.get(citizenIndex).setTaxes(txtCTZNTaxes.getInteger());
            citizens.get(citizenIndex).setCorruption(txtCTZNCorruption.getInteger());
            citizens.get(citizenIndex).setConstruction(txtCTZNConstruction.getInteger());
            
            if (!clone.equals(citizens.get(citizenIndex))) {
                Main.undoPush(clone, citizenIndex);
            }
        }
        citizenIndex = lstCitizens.getSelectedIndex();

        if (citizenIndex != -1)
        {
            //set the UI for the new citizen
            if (citizens.get(citizenIndex).getDefaultCitizen() == 1) {
                chkCTZNDefaultCitizen.setSelected(true);
            } else {
                chkCTZNDefaultCitizen.setSelected(false);
            }
            txtCTZNCivilopediaEntry.setText(citizens.get(citizenIndex).getCivilopediaEntry());
            txtCTZNPluralName.setText(citizens.get(citizenIndex).getPluralName());
            cmbCTZNPrerequisite.setSelectedIndex(citizens.get(citizenIndex).getPrerequisite() + 1);
            txtCTZNLuxuries.setText(Integer.toString(citizens.get(citizenIndex).getLuxuries()));
            txtCTZNResearch.setText(Integer.toString(citizens.get(citizenIndex).getResearch()));
            txtCTZNTaxes.setText(Integer.toString(citizens.get(citizenIndex).getTaxes()));
            txtCTZNCorruption.setText(Integer.toString(citizens.get(citizenIndex).getCorruption()));
            txtCTZNConstruction.setText(Integer.toString(citizens.get(citizenIndex).getConstruction()));
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
        addLengthDocumentListener(31, txtCTZNCivilopediaEntry);
        addLengthDocumentListener(31, txtCTZNPluralName);
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
        //Max Firaxis value = 10, min = 10
        addBadValueDocumentListener(10, -10, txtCTZNLuxuries);
        addBadValueDocumentListener(10, -10, txtCTZNResearch);
        addBadValueDocumentListener(10, -10, txtCTZNTaxes);
        addBadValueDocumentListener(10, -10, txtCTZNConstruction);
        addBadValueDocumentListener(10, -10, txtCTZNCorruption);
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
    
    
    
    public void renameBIQElement(int index, String name)
    {
        citizens.get(index).setName(name);
    }
}
