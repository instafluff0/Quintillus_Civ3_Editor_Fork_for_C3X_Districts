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
import com.civfanatics.civ3.biqFile.ERAS;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJList;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJTextField;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.util.ArrayList;
import java.util.List;
/**
 * This class represents the tab for Eras.
 * 
 * @author Andrew
 */
public class EraTab extends EditorTab {

    int eraIndex;
    public List<ERAS>era;
    boolean tabCreated;
    private JLabel jLabel92;
    private JLabel jLabel93;
    private JPanel jPanel57;
    private JScrollPane scrEras;
    private SuperJList lstera = new SuperJList(this, "era", false, false);
    private SuperJTextField txtERACivilopediaEntry;
    private SuperJTextField txtERAQuestionMark;
    private SuperJTextField txtERAResearcher1;
    private SuperJTextField txtERAResearcher2;
    private SuperJTextField txtERAResearcher3;
    private SuperJTextField txtERAResearcher4;
    private SuperJTextField txtERAResearcher5;

    SuperListModel eraList;

    /**
     * The constructor.
     */
    public EraTab()
    {
        lstType = lstera;
        tabName = "ERA";
        textBoxes = new ArrayList<>();
        scrEras = new JScrollPane();
        jPanel57 = new JPanel();
        txtERAResearcher1 = new SuperJTextField();
        txtERAResearcher2 = new SuperJTextField();
        txtERAResearcher3 = new SuperJTextField();
        txtERAResearcher4 = new SuperJTextField();
        txtERAResearcher5 = new SuperJTextField();
        jLabel92 = new JLabel();
        txtERACivilopediaEntry = new SuperJTextField();
        jLabel93 = new JLabel();
        txtERAQuestionMark = new SuperJTextField();
        eraList = new SuperListModel();
        lstera.setModel(eraList);

        eraIndex = -1;
    }

    public void setSelectedIndex(int i)
    {
        eraIndex = i;
    }
    /**
     * Creates the GUI of the tab.  This includes button groups, layouts, etc.
     * Logically, this is to occur immediately after the constructor is called.
     *
     * @return - A reference to the EraTab instance itself.  This reference
     * allows the tab to be added to a tab bar.
     */
    public JPanel createTab()
    {
        lstera.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstera.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lsteraValueChanged(evt);
            }
        });
        scrEras.setViewportView(lstera);

        jPanel57.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Researcher Names"));

        txtERAResearcher1.setText("                                           ");

        txtERAResearcher2.setText("                                           ");

        txtERAResearcher3.setText("                                           ");

        txtERAResearcher4.setText("                                           ");

        org.jdesktop.layout.GroupLayout jPanel57Layout = new org.jdesktop.layout.GroupLayout(jPanel57);
        jPanel57.setLayout(jPanel57Layout);
        jPanel57Layout.setHorizontalGroup(
            jPanel57Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel57Layout.createSequentialGroup()
                .add(jPanel57Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(txtERAResearcher2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, txtERAResearcher1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                    .add(txtERAResearcher3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                    .add(txtERAResearcher4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, txtERAResearcher5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel57Layout.setVerticalGroup(
            jPanel57Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel57Layout.createSequentialGroup()
                .add(txtERAResearcher1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtERAResearcher2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtERAResearcher3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtERAResearcher4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtERAResearcher5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel92.setText("Civilopedia Entry:");

        txtERACivilopediaEntry.setText("                                       ");

        jLabel93.setText("Unknown Variable:");

        org.jdesktop.layout.GroupLayout thisLayout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(thisLayout);
        thisLayout.setHorizontalGroup(
            thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(thisLayout.createSequentialGroup()
                .add(scrEras, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 161, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(thisLayout.createSequentialGroup()
                        .add(jLabel92)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtERACivilopediaEntry, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(thisLayout.createSequentialGroup()
                        .add(jLabel93)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(txtERAQuestionMark, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 85, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(611, Short.MAX_VALUE))
        );
        thisLayout.setVerticalGroup(
            thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scrEras, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 666, Short.MAX_VALUE)
            .add(thisLayout.createSequentialGroup()
                .addContainerGap()
                .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel92)
                    .add(txtERACivilopediaEntry, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(17, 17, 17)
                .add(jPanel57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel93)
                    .add(txtERAQuestionMark, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(428, Short.MAX_VALUE))
        );
        this.setName("ERAS");

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
     * @param era - The list of all the eras.
     */
    public void sendData(List<ERAS>era)
    {
        assert tabCreated:"Tab must be created before data can be sent to it";
        this.era = era;
        eraIndex = -1;
        eraList.removeAllElements();
        for (int i = 0; i < era.size(); i++)
        {
            eraList.addElement(era.get(i).getName());
        }

    }

    private void lsteraValueChanged(javax.swing.event.ListSelectionEvent evt) {
        updateTab();
    }
    /**
     * Method that stores all tab data to memory (in case any fields have changed
     * on-screen), and then updates the screen based on the new item
     * selected.  As this method may be invoked manually (for example, prior
     * to the file being saved to disk), it is possible that the new item
     * selected will be the same one as the one previously selected.
     *
     */
    public void updateTab()
    {
        if (!(eraIndex == -1)) {
            era.get(eraIndex).setCivilopediaEntry(txtERACivilopediaEntry.getText());
            era.get(eraIndex).setResearcher1(txtERAResearcher1.getText());
            era.get(eraIndex).setResearcher2(txtERAResearcher2.getText());
            era.get(eraIndex).setResearcher3(txtERAResearcher3.getText());
            era.get(eraIndex).setResearcher4(txtERAResearcher4.getText());
            era.get(eraIndex).setResearcher5(txtERAResearcher5.getText());
            era.get(eraIndex).setQuestionMark(txtERAQuestionMark.getInteger());
            if (txtERAResearcher5.getText().length() > 0 && txtERAResearcher5.getText().charAt(0) != '\0')
                era.get(eraIndex).setUsedResearcherNames(5);
            else if (txtERAResearcher4.getText().length() > 0 && txtERAResearcher4.getText().charAt(0) != '\0')
                era.get(eraIndex).setUsedResearcherNames(4);
            else if (txtERAResearcher3.getText().length() > 0 && txtERAResearcher3.getText().charAt(0) != '\0')
                era.get(eraIndex).setUsedResearcherNames(3);
            else if (txtERAResearcher2.getText().length() > 0 && txtERAResearcher2.getText().charAt(0) != '\0')
                era.get(eraIndex).setUsedResearcherNames(2);
            else if (txtERAResearcher1.getText().length() > 0 && txtERAResearcher1.getText().charAt(0) != '\0')
                era.get(eraIndex).setUsedResearcherNames(1);
            else
                era.get(eraIndex).setUsedResearcherNames(0);
        }
        eraIndex = lstera.getSelectedIndex();
        if (eraIndex != -1)
        {
            txtERACivilopediaEntry.setText(era.get(eraIndex).getCivilopediaEntry());
            txtERAResearcher1.setText(era.get(eraIndex).getResearcher1());
            txtERAResearcher2.setText(era.get(eraIndex).getResearcher2());
            txtERAResearcher3.setText(era.get(eraIndex).getResearcher3());
            txtERAResearcher4.setText(era.get(eraIndex).getResearcher4());
            txtERAResearcher5.setText(era.get(eraIndex).getResearcher5());
            txtERAQuestionMark.setText(Integer.toString(era.get(eraIndex).getQuestionMark()));
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
        addLengthDocumentListener(31, txtERAResearcher1);
        addLengthDocumentListener(31, txtERAResearcher2);
        addLengthDocumentListener(31, txtERAResearcher3);
        addLengthDocumentListener(31, txtERAResearcher4);
        addLengthDocumentListener(31, txtERAResearcher5);
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
        era.get(index).setEraName(name);
    }

}
