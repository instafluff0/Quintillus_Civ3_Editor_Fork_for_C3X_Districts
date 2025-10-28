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
import com.civfanatics.civ3.biqFile.ESPN;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJList;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJTextField;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperListModel;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import java.util.List;
/**
 * A tab for espionage missions.
 * @author Andrew
 */
public class ESPNTab extends EditorTab {

    int espionageIndex;
    public List<ESPN>espionage;
    boolean tabCreated = true;

    private SuperListModel espionageList;
    private JCheckBox chkESPNDiplomats;
    private JCheckBox chkESPNSpies;
    private JLabel lblCivilopedia;
    private JLabel lblDescription;
    private JLabel lblCost;
    //private JPanel this;
    private JPanel jPanel56;
    private JScrollPane jScrollPane7;
    private SuperJTextField txtESPNCivilopediaEntry;
    private SuperJTextField txtESPNDescription;
    private SuperJTextField txtESPNBaseCost;
    private SuperJList lstEspionages = new SuperJList(this, "act of espionage", false, false);

    public ESPNTab()
    {
        lstType = lstEspionages;
        tabName = "ESPN";
        textBoxes = new ArrayList<>();
        //this = new JPanel();
        jScrollPane7 = new JScrollPane();
        lblCivilopedia = new JLabel();
        lblDescription = new JLabel();
        txtESPNCivilopediaEntry = new SuperJTextField();
        txtESPNDescription = new SuperJTextField();
        lblCost = new JLabel();
        txtESPNBaseCost = new SuperJTextField();
        jPanel56 = new JPanel();
        chkESPNDiplomats = new JCheckBox();
        chkESPNSpies = new JCheckBox();
        espionageList = new SuperListModel();
        lstEspionages.setModel(espionageList);
        espionageIndex = -1;
    }

    public void setSelectedIndex(int i)
    {
        espionageIndex = i;
    }

    /**
     * Triggers an update of the tab when the select espionage mission changes.
     * @param evt
     */
    public void lstEspionagesValueChanged(javax.swing.event.ListSelectionEvent evt)
    {
        updateTab();
    }
    /**
     * {@inheritDoc}
     */
    public void updateTab()
    {
        if (!(espionageIndex == -1))
        {
            espionage.get(espionageIndex).setDescription(txtESPNDescription.getText());
            espionage.get(espionageIndex).setCivilopediaEntry(txtESPNCivilopediaEntry.getText());
            if (chkESPNDiplomats.isSelected() && chkESPNSpies.isSelected())
                espionage.get(espionageIndex).setMissionPerformedBy(3);
            else if (chkESPNSpies.isSelected())
                espionage.get(espionageIndex).setMissionPerformedBy(2);
            else if (chkESPNDiplomats.isSelected())
                espionage.get(espionageIndex).setMissionPerformedBy(1);
            else
                espionage.get(espionageIndex).setMissionPerformedBy(0);
            espionage.get(espionageIndex).setBaseCost(txtESPNBaseCost.getInteger());
        }
        espionageIndex = lstEspionages.getSelectedIndex();
        if (espionageIndex != -1)
        {
            txtESPNDescription.setText(espionage.get(espionageIndex).getDescription());
            txtESPNCivilopediaEntry.setText(espionage.get(espionageIndex).getCivilopediaEntry());
            switch (espionage.get(espionageIndex).getMissionPerformedBy())
            {
                case 3: chkESPNDiplomats.setSelected(true);
                        chkESPNSpies.setSelected(true);
                        break;
                case 2: chkESPNDiplomats.setSelected(false);
                        chkESPNSpies.setSelected(true);
                        break;
                case 1: chkESPNDiplomats.setSelected(true);
                        chkESPNSpies.setSelected(false);
                        break;
                case 0: chkESPNDiplomats.setSelected(false);
                        chkESPNSpies.setSelected(false);
                        break;
                default: logger.warn("Invalid data for mission performed by for espionage "
                         + "mission " + espionage.get(espionageIndex).getName() + "; integer value was "
                         + espionage.get(espionageIndex).getMissionPerformedBy() + " and maximum value "
                         + "should be 3 (minimum should be 0).");
            }

            txtESPNBaseCost.setText(Integer.toString(espionage.get(espionageIndex).getBaseCost()));
        }
    }
    /**
     * Creates the GUI of the tab.  This includes button groups, layouts, etc.
     * Logically, this is to occur immediately after the constructor is called.
     *
     * @return - A reference to the ESPNTab instance itself.  This reference
     * allows the tab to be added to a tab bar.
     */
    public JPanel createTab()
    {
        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lstEspionages.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstEspionages.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstEspionagesValueChanged(evt);
            }
        });
        jScrollPane7.setViewportView(lstEspionages);

        this.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 170, 700));

        lblDescription.setText("Description:");
        this.add(lblDescription, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 40, -1, -1));

        lblCivilopedia.setText("Civilopedia Entry:");
        this.add(lblCivilopedia, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, -1, -1));

        txtESPNCivilopediaEntry.setText("                                           ");
        this.add(txtESPNCivilopediaEntry, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 20, 170, -1));

        txtESPNDescription.setText("                                           ");
        this.add(txtESPNDescription, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 40, 570, -1));

        lblCost.setText("Cost:");
        this.add(lblCost, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 60, -1, -1));

        txtESPNBaseCost.setText("                   ");
        this.add(txtESPNBaseCost, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 60, 50, -1));

        jPanel56.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Performed By"));

        chkESPNDiplomats.setText("Diplomats");

        chkESPNSpies.setText("Spies");

        org.jdesktop.layout.GroupLayout jPanel56Layout = new org.jdesktop.layout.GroupLayout(jPanel56);
        jPanel56.setLayout(jPanel56Layout);
        jPanel56Layout.setHorizontalGroup(
            jPanel56Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel56Layout.createSequentialGroup()
                .add(jPanel56Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(chkESPNDiplomats)
                    .add(chkESPNSpies))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        jPanel56Layout.setVerticalGroup(
            jPanel56Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel56Layout.createSequentialGroup()
                .add(chkESPNDiplomats)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(chkESPNSpies)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        this.add(jPanel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 90, -1, -1));
        this.setName("ESPN");

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
     * @param espionage - The list of all espionage missions.
     */
    public void sendData(List<ESPN>espionage)
    {
        this.espionage = espionage;
        espionageIndex = -1;
        espionageList.removeAllElements();
        for (int i = 0; i < espionage.size(); i++)
        {
            espionageList.addElement(espionage.get(i).getName());
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
        if(logger.isDebugEnabled())
            logger.debug("SETTING MINIMAL LIMITS");
        addLengthDocumentListener(127, txtESPNDescription);
        addLengthDocumentListener(31, txtESPNCivilopediaEntry);
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
        addBadValueDocumentListener(1000, txtESPNBaseCost);
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
        espionage.get(index).setName(name);
    }
}
