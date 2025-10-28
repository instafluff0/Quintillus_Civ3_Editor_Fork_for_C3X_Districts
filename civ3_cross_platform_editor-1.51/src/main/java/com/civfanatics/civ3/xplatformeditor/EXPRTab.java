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
import com.civfanatics.civ3.biqFile.EXPR;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJList;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJTextField;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperListModel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
/**
 * This class represents the Experience tab in the Civilization editor.
 * 
 * @author Andrew
 */
public class EXPRTab extends EditorTab {
    public List<EXPR>experience;
    int experienceIndex;
    boolean tabCreated;
    private SuperListModel experienceList;

    private JLabel jLabel53;
    private JLabel jLabel63;
    //private JPanel this;
    private JScrollPane jScrollPane8;
    private SuperJList lstExperiences = new SuperJList(this, "experience level", false, false);
    private SuperJTextField txtEXPRBaseHitPoints;
    private SuperJTextField txtEXPRRetreatBonus;

    /**
     * Constructor.  Initializes all the GUI elements and some variables.
     */
    public EXPRTab()
    {
        lstType = lstExperiences;
        tabName = "EXPR";
        textBoxes = new ArrayList<>();
        //this = new JPanel();
        jScrollPane8 = new JScrollPane();
        jLabel53 = new JLabel();
        jLabel63 = new JLabel();
        txtEXPRBaseHitPoints = new SuperJTextField();
        txtEXPRRetreatBonus = new SuperJTextField();
        experienceList = new SuperListModel();
        lstExperiences.setModel(experienceList);

        experienceIndex = -1;
    }

    public void setSelectedIndex(int i)
    {
        experienceIndex = i;
    }
    /**
     * Creates the GUI of the tab.  This includes button groups, layouts, etc.
     * Logically, this is to occur immediately after the constructor is called.
     *
     * @return - A reference to the EXPRTab instance itself.  This reference
     * allows the tab to be added to a tab bar.
     */
    public JPanel createTab()
    {
        lstExperiences.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstExperiences.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstExperiencesValueChanged(evt);
            }
        });
        jScrollPane8.setViewportView(lstExperiences);

        jLabel53.setText("Base hit points:");

        jLabel63.setText("Retreat chance:");

        txtEXPRBaseHitPoints.setText("                   ");

        txtEXPRRetreatBonus.setText("                   ");

        org.jdesktop.layout.GroupLayout thisLayout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(thisLayout);
        thisLayout.setHorizontalGroup(
            thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(thisLayout.createSequentialGroup()
                .add(jScrollPane8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 162, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel53)
                    .add(jLabel63))
                .add(9, 9, 9)
                .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(txtEXPRRetreatBonus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtEXPRBaseHitPoints, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(691, Short.MAX_VALUE))
        );
        thisLayout.setVerticalGroup(
            thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 666, Short.MAX_VALUE)
            .add(thisLayout.createSequentialGroup()
                .addContainerGap()
                .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel53)
                    .add(txtEXPRBaseHitPoints, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel63)
                    .add(txtEXPRRetreatBonus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(614, Short.MAX_VALUE))
        );
        this.setName(tabName);

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
     * @param experience - The list of all the experience levels.
     */
    public void sendData(List<EXPR>experience)
    {
        this.experience = experience;
        experienceIndex = -1;

        experienceList.removeAllElements();
        for (int i = 0; i < experience.size(); i++)
        {
            experienceList.addElement(experience.get(i).getName());
        }
    }

    private void lstExperiencesValueChanged(javax.swing.event.ListSelectionEvent evt) {
        // :
        updateTab();
    }
    /**
     * {@inheritDoc}
     */
    public void updateTab()
    {
        if (!(experienceIndex == -1)) {
            experience.get(experienceIndex).setBaseHitPoints(txtEXPRBaseHitPoints.getInteger());
            experience.get(experienceIndex).setRetreatBonus(txtEXPRRetreatBonus.getInteger());
        }

        experienceIndex = lstExperiences.getSelectedIndex();
        if (experienceIndex != -1)
        {
            txtEXPRBaseHitPoints.setText(Integer.toString(experience.get(experienceIndex).getBaseHitPoints()));
            txtEXPRRetreatBonus.setText(Integer.toString(experience.get(experienceIndex).getRetreatBonus()));
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
        addBadValueDocumentListener(100, txtEXPRRetreatBonus);
        addBadValueDocumentListener(20, 1, txtEXPRBaseHitPoints);
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
        experience.get(index).name = name;
    }
}
