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
import com.civfanatics.civ3.biqFile.TRFM;
import com.civfanatics.civ3.biqFile.GOOD;
import com.civfanatics.civ3.biqFile.TECH;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJList;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJTextField;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperListModel;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
/**
 * A tab for terraforms (worker jobs).
 * @author Andrew
 */
public class TRFMTab extends EditorTab {
    boolean tabCreated;    
    public List<TRFM>workerJob;
    public List<TECH>technology;
    public List<GOOD>resource;
    int workerJobIndex;
    
    JComboBox cmbTRFMRequiredAdvance;
    JComboBox cmbTRFMRequiredResource1;
    JComboBox cmbTRFMRequiredResource2;
    private JLabel jLabel85;
    private JLabel jLabel86;
    private JLabel jLabel87;
    private JLabel jLabel88;
    //private JPanel this;
    private JPanel jPanel55;
    private JScrollPane jScrollPane15;
    private SuperJList lstWorkerJobs = new SuperJList(this, "worker job", false, false);
    private SuperJTextField txtTRFMCivilopediaEntry;
    private SuperJTextField txtTRFMOrder;
    private SuperJTextField txtTRFMTurnsToComplete;
    private SuperListModel terraformList;
    DefaultComboBoxModel mdlPrerequisite = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlRequiredResource1 = new DefaultComboBoxModel();
    DefaultComboBoxModel mdlRequiredResource2 = new DefaultComboBoxModel();

    public TRFMTab()
    {
        lstType = lstWorkerJobs;
        tabName = "TRFM";
        //this = new JPanel();
        jScrollPane15 = new JScrollPane();
        jLabel85 = new JLabel();
        jLabel86 = new JLabel();
        txtTRFMCivilopediaEntry = new SuperJTextField();
        txtTRFMOrder = new SuperJTextField();
        jLabel87 = new JLabel();
        txtTRFMTurnsToComplete = new SuperJTextField();
        jLabel88 = new JLabel();
        cmbTRFMRequiredAdvance = new JComboBox();
        jPanel55 = new JPanel();
        cmbTRFMRequiredResource1 = new JComboBox();
        cmbTRFMRequiredResource2 = new JComboBox();
            terraformList = new SuperListModel();
            lstWorkerJobs.setModel(terraformList);

        workerJobIndex = -1;
    }
    /**
     * Creates the GUI of the tab.  This includes button groups, layouts, etc.
     * Logically, this is to occur immediately after the constructor is called.
     *
     * @return - A reference to the TRFMTab instance itself.  This reference
     * allows the tab to be added to a tab bar.
     */
    public JPanel createTab()
    {
        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lstWorkerJobs.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstWorkerJobs.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstWorkerJobsValueChanged(evt);
            }
        });
        jScrollPane15.setViewportView(lstWorkerJobs);

        this.add(jScrollPane15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 170, 670));

        jLabel85.setText("Civilopedia Entry:");
        this.add(jLabel85, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 10, -1, -1));

        jLabel86.setText("Order:");
        this.add(jLabel86, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 30, -1, -1));

        txtTRFMCivilopediaEntry.setText("                   ");
        this.add(txtTRFMCivilopediaEntry, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, 190, -1));

        txtTRFMOrder.setText("                   ");
        this.add(txtTRFMOrder, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 30, 190, -1));

        jLabel87.setText("Turns to Complete:");
        this.add(jLabel87, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 50, -1, -1));

        txtTRFMTurnsToComplete.setText("                   ");
        this.add(txtTRFMTurnsToComplete, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 50, 60, 20));
        //SOLUTION TO SHRINKING JLABEL PROBLEM
        //Set the width/height in the org.netbeans.lib.awtextra.AbsoluteConstraints constructor
        //The third and fourth firelds are width/height, respectively.  In an awful lots of the
        //NetBeans-created constructors, they are -1 and -1, giving "default" sizes.  This doesn't
        //work well with integers as the values that are stored therein

        jLabel88.setText("Prerequisite:");
        this.add(jLabel88, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 80, -1, -1));

        cmbTRFMRequiredAdvance.setModel(mdlPrerequisite);
        this.add(cmbTRFMRequiredAdvance, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 70, 190, -1));

        jPanel55.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Required Resources"));

        cmbTRFMRequiredResource1.setModel(mdlRequiredResource1);

        cmbTRFMRequiredResource2.setModel(mdlRequiredResource2);

        org.jdesktop.layout.GroupLayout jPanel55Layout = new org.jdesktop.layout.GroupLayout(jPanel55);
        jPanel55.setLayout(jPanel55Layout);
        jPanel55Layout.setHorizontalGroup(
            jPanel55Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel55Layout.createSequentialGroup()
                .add(jPanel55Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, cmbTRFMRequiredResource2, 0, 279, Short.MAX_VALUE)
                    .add(cmbTRFMRequiredResource1, 0, 279, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel55Layout.setVerticalGroup(
            jPanel55Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel55Layout.createSequentialGroup()
                .add(cmbTRFMRequiredResource1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cmbTRFMRequiredResource2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        this.add(jPanel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 100, -1, -1));
        this.setName("TRFM");

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
     * @param workerJob - The list of all worker jobs.
     * @param technology - The list of all technologies.
     * @param resource - The list of all resources.
     */
    public void sendData(List<TRFM>workerJob, List<TECH>technology, List<GOOD>resource)
    {
        this.workerJob = workerJob;
        this.technology = technology;
        this.resource = resource;

        cmbTRFMRequiredAdvance.removeAllItems();
        cmbTRFMRequiredResource1.removeAllItems();
        cmbTRFMRequiredResource2.removeAllItems();
        cmbTRFMRequiredAdvance.addItem(noneSelected);
        cmbTRFMRequiredResource1.addItem(noneSelected);
        cmbTRFMRequiredResource2.addItem(noneSelected);
        terraformList.removeAllElements();
        for (int i = 0; i < workerJob.size(); i++)
        {
            terraformList.addElement(workerJob.get(i).getName());
        }
        for (int i = 0; i < technology.size(); i++)
        {
            //add technologies to combo lists that need technologies
            utils.addWithPossibleDuplicates(technology.get(i).getName(), mdlPrerequisite);
        }
        for (int i = 0; i < resource.size(); i++)
        {
            //add goods to combo lists that need government
            utils.addWithPossibleDuplicates(resource.get(i).getName(), mdlRequiredResource1);
            utils.addWithPossibleDuplicates(resource.get(i).getName(), mdlRequiredResource2);
        }
    }
    
    public void setSelectedIndex(int i)
    {
        workerJobIndex = i;
    }

    /**
     * {@inheritDoc}
     */
    public void updateTab()
    {
        if (workerJobIndex != -1) {
            workerJob.get(workerJobIndex).setCivilopediaEntry(txtTRFMCivilopediaEntry.getText());
            workerJob.get(workerJobIndex).setTurnsToComplete(txtTRFMTurnsToComplete.getInteger());
            workerJob.get(workerJobIndex).setRequiredAdvance(cmbTRFMRequiredAdvance.getSelectedIndex() - 1);
            workerJob.get(workerJobIndex).setRequiredResource1(cmbTRFMRequiredResource1.getSelectedIndex() - 1);
            workerJob.get(workerJobIndex).setRequiredResource2(cmbTRFMRequiredResource2.getSelectedIndex() - 1);
            workerJob.get(workerJobIndex).setOrder(txtTRFMOrder.getText());
        }
        workerJobIndex = lstWorkerJobs.getSelectedIndex();
        if (workerJobIndex != -1)
        {
            txtTRFMCivilopediaEntry.setText(workerJob.get(workerJobIndex).getCivilopediaEntry());
            txtTRFMTurnsToComplete.setText(Integer.toString(workerJob.get(workerJobIndex).getTurnsToComplete()));
            cmbTRFMRequiredAdvance.setSelectedIndex(workerJob.get(workerJobIndex).getRequiredAdvance() + 1);
            cmbTRFMRequiredResource1.setSelectedIndex(workerJob.get(workerJobIndex).getRequiredResource1() + 1);
            cmbTRFMRequiredResource2.setSelectedIndex(workerJob.get(workerJobIndex).getRequiredResource2() + 1);
            txtTRFMOrder.setText(workerJob.get(workerJobIndex).getOrder());
        }
    }

    private void lstWorkerJobsValueChanged(javax.swing.event.ListSelectionEvent evt) {
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
        addLengthDocumentListener(31, txtTRFMCivilopediaEntry);
        addLengthDocumentListener(31, txtTRFMOrder);
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
        setMinimalLimits();
        //2/19/2022 - Arcangelus confirmed that 0-turn terraforming is safe.  Allow that, though not negative.
        addBadValueDocumentListener(1000, 0, txtTRFMTurnsToComplete);
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
        addBadValueDocumentListener(1000, 1, txtTRFMTurnsToComplete);
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
        workerJob.get(index).setName(name);
    }
}
