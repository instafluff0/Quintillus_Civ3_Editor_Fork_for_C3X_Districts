package com.civfanatics.civ3.xplatformeditor;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * TODO: Borders Level isn't being auto-checked for compliance with safety level upon loading
 */

/**
 *
 * @author Quintillus
 */
import com.civfanatics.civ3.biqFile.CULT;
import com.civfanatics.civ3.biqFile.RULE;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJList;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJTextField;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperListModel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import java.util.ArrayList;
import java.util.List;
/**
 * This class represents the Culture tab in the Civilization editor.  It also
 * contains the cultural levels (Fledgling through Glorious), Level Mulitplier,
 * and Border Factor found in the General Rules tab of Firaxis's editor.
 *
 * @author Andrew
 */
public class CultTab extends EditorTab {
    public List<RULE>rule;
    JLabel jLabel322;
    JLabel jLabel323;
    JLabel jLabel324;
    JLabel jLabel44;
    JLabel lblSuccessfulPropaganda;
    JLabel lblResistanceInitial;
    JLabel lblResistanceContinuing;
    JList lstRULECulturalLevels;
    JPanel jPanel115;
    JPanel jPanel116;
    JPanel pnlCultureRatio;
    JScrollPane jScrollPane38;
    JScrollPane jScrollPane4;
    SuperJTextField txtRULELevelMultiplier;
    SuperJTextField txtRULEBorderFactor;
    SuperJList lstCultures = new SuperJList(this, "cultural relation");
    //JPanel this;
    SuperJTextField txtCULTContinuedResistanceChance;
    SuperJTextField txtCULTInitResistanceChance;
    SuperJTextField txtCULTPropagandaSuccess;
    SuperJTextField txtCULTRatioDenominator;
    SuperJTextField txtCULTRatioNumerator;
    List<CULT>culture;
    boolean tabCreated;

    private SuperListModel cultureList;
    private DefaultListModel cultureLevelList;

    public int cultureIndex;
    public BldgTab bldgTab;

    JMenuItem delete;
    JMenuItem add;
    String listSelected;

    public void setSelectedIndex(int i)
    {
        cultureIndex = i;
    }

    /**
     * The constructor.  Performs initialization of the GUI.
     */
    public CultTab()
    {
        lstType = lstCultures;
        //logger.setLevel(Level.DEBUG);
        tabName = "CULT";
        textBoxes = new ArrayList<>();
        //this = new JPanel();
        jScrollPane4 = new JScrollPane();
        jPanel115 = new JPanel();
        txtCULTContinuedResistanceChance = new SuperJTextField();
        txtCULTInitResistanceChance = new SuperJTextField();
        txtCULTPropagandaSuccess = new SuperJTextField();
        lblSuccessfulPropaganda = new JLabel();
        lblResistanceInitial = new JLabel();
        lblResistanceContinuing = new JLabel();
        pnlCultureRatio = new JPanel();
        txtCULTRatioNumerator = new SuperJTextField();
        jLabel44 = new JLabel();
        txtCULTRatioDenominator = new SuperJTextField();
        jPanel116 = new JPanel();
        jScrollPane38 = new JScrollPane();
        lstRULECulturalLevels = new JList();
        jLabel322 = new JLabel();
        jLabel323 = new JLabel();
        jLabel324 = new JLabel();
        txtRULELevelMultiplier = new SuperJTextField();
        txtRULEBorderFactor = new SuperJTextField();
        cultureIndex = -1;
    }
    public JPanel createTab()
    {
        this.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lstCultures.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstCultures.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstCulturesValueChanged(evt);
            }
        });
        jScrollPane4.setViewportView(lstCultures);

        this.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 150, 670));

        lblSuccessfulPropaganda.setText("Chance of Successful Propaganda:");

        lblResistanceInitial.setText("Chance of Resistance (initial):");

        lblResistanceContinuing.setText("Chance of Resistance (continued):");

        pnlCultureRatio.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Culture Ratio"));

        txtCULTRatioNumerator.setText("                   ");

        jLabel44.setText(":");

        txtCULTRatioDenominator.setText("                   ");

        org.jdesktop.layout.GroupLayout cultureLayout = new org.jdesktop.layout.GroupLayout(pnlCultureRatio);
        pnlCultureRatio.setLayout(cultureLayout);
        cultureLayout.setHorizontalGroup(
            cultureLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(cultureLayout.createSequentialGroup()
                .add(txtCULTRatioNumerator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel44)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                //SOLUTION to SHRINKING JTextBoxes
                //Third parameter is width; don't leave it at the "preferred" size
                .add(txtCULTRatioDenominator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        cultureLayout.setVerticalGroup(
            cultureLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(cultureLayout.createSequentialGroup()
                .add(cultureLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(txtCULTRatioNumerator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel44)
                    .add(txtCULTRatioDenominator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout jPanel115Layout = new org.jdesktop.layout.GroupLayout(jPanel115);
        jPanel115.setLayout(jPanel115Layout);
        jPanel115Layout.setHorizontalGroup(jPanel115Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel115Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel115Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(pnlCultureRatio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel115Layout.createSequentialGroup()
                        .add(jPanel115Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblSuccessfulPropaganda)
                            .add(lblResistanceInitial))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel115Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(txtCULTInitResistanceChance)
                            .add(txtCULTPropagandaSuccess, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jPanel115Layout.createSequentialGroup()
                        .add(lblResistanceContinuing)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(txtCULTContinuedResistanceChance, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 61, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel115Layout.setVerticalGroup(jPanel115Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel115Layout.createSequentialGroup()
                .add(pnlCultureRatio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel115Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblSuccessfulPropaganda)
                    .add(txtCULTPropagandaSuccess, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel115Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblResistanceInitial)
                    .add(txtCULTInitResistanceChance, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel115Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblResistanceContinuing)
                    .add(txtCULTContinuedResistanceChance, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        this.add(jPanel115, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 0, -1, -1));

        lstRULECulturalLevels.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane38.setViewportView(lstRULECulturalLevels);

        jLabel322.setText("Cultural Levels");

        jLabel323.setText("Level Multiplier:");

        jLabel324.setText("Border Factor:");

        org.jdesktop.layout.GroupLayout jPanel116Layout = new org.jdesktop.layout.GroupLayout(jPanel116);
        jPanel116.setLayout(jPanel116Layout);
        jPanel116Layout.setHorizontalGroup(
            jPanel116Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel116Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel116Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel116Layout.createSequentialGroup()
                        .add(jScrollPane38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 132, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel116Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel323)
                            .add(jLabel324))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel116Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(txtRULEBorderFactor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 61, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(txtRULELevelMultiplier, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 61, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jLabel322))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        jPanel116Layout.setVerticalGroup(
            jPanel116Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel116Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel322)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel116Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel116Layout.createSequentialGroup()
                        .add(jPanel116Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel323)
                            .add(txtRULELevelMultiplier, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel116Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel324)
                            .add(txtRULEBorderFactor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jScrollPane38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        this.add(jPanel116, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 0, 350, 210));


        lstRULECulturalLevels.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lstLevelsPopup(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lstLevelsPopup(evt);
            }
        });

        delete = new JMenuItem("Delete");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (listSelected.equals("levels"))
                {
                    int index = lstRULECulturalLevels.getSelectedIndex();
                    rule.get(0).removeCultureLevel(index);
                    utils.removeFromList(index, cultureLevelList, lstRULECulturalLevels);
                }
            }
        });
        add = new JMenuItem("Add");
        add.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                if (listSelected.equals("levels"))
                {
                    String name = JOptionPane.showInputDialog("Please choose a name for the new cultural level");
                    rule.get(0).addCultureLevelName(name);
                    cultureLevelList.addElement(name);

                    lstRULECulturalLevels.setSelectedIndex(cultureLevelList.size() - 1);
                }
            }
        });
        this.setName("CULT");

        cultureIndex = -1;
        tabCreated = true;
        if (logger.isDebugEnabled())
            logger.debug("Cult tab created");
        return this;
        //pnlTabs.addTab("CULT", this);
    }
    
    @Override
    public boolean addItem(String name)
    {
        culture.add(new CULT(name, baselink));
        return true;
    }
    
    public void deleteAction()
    {
        int index = lstCultures.getSelectedIndex();
        //Citizen index must be set to -1, or lstCitizens will copy the
        //deleted item's values into the next item
        cultureIndex = -1;
        utils.removeFromList(index, culture, cultureList, lstCultures);
    }

    private void lstLevelsPopup(java.awt.event.MouseEvent evt){
        if (evt.isPopupTrigger())
        {

            lstRULECulturalLevels.setSelectedIndex(lstRULECulturalLevels.locationToIndex(new java.awt.Point(evt.getX(), evt.getY())));
            JPopupMenu popUp = new JPopupMenu();
            popUp.add(delete);
            popUp.add(add);
            java.awt.Component component = evt.getComponent();
            int x = evt.getX();
            int y = evt.getY();
            listSelected = "levels";
            popUp.show(component, x, y);
        }
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
     * @param culture - The list of all cultures.
     * @param rule - The list of all the rules.
     */
    public void sendData(List<CULT>culture, List<RULE>rule)
    {
        assert tabCreated:"The tab must be created before data is send to it.";
        this.culture = culture;
        this.rule = rule;
        cultureIndex = -1;

        cultureList = new SuperListModel();
        cultureLevelList = new DefaultListModel();
        lstCultures.setModel(cultureList);
        lstRULECulturalLevels.setModel(cultureLevelList);
        for (int i = 0; i < culture.size(); i++)
        {
            cultureList.addElement(culture.get(i).getName());
        }
        List<String>cultureLevels = rule.get(0).getCultureLevels();
        for (int i = 0; i < cultureLevels.size(); i++)
        {
            cultureLevelList.addElement(cultureLevels.get(i));
        }
        //add rule values here
        txtRULEBorderFactor.setText(Integer.toString(rule.get(0).getBorderFactor()));
        txtRULELevelMultiplier.setText(Integer.toString(rule.get(0).getBorderExpansionMultiplier()));
        if (logger.isTraceEnabled())
            logger.trace("CultTab - Data received");
    }

    private void lstCulturesValueChanged(javax.swing.event.ListSelectionEvent evt) {

        //only adjust after the user has released the mouse, or to an adjustment
        //caused by another user action (adding/deleting an item, for instance)
        if (evt.getValueIsAdjusting())
            return;
        updateTab();

    }

    /**
     * {@inheritDoc}
     */
    public void updateTab()
    {
        if (!(cultureIndex == -1))
        {
            culture.get(cultureIndex).setPropagandaSuccess(txtCULTPropagandaSuccess.getInteger());
            culture.get(cultureIndex).setRatioDenominator(txtCULTRatioDenominator.getInteger());
            culture.get(cultureIndex).setRatioNumerator(txtCULTRatioNumerator.getInteger());
            culture.get(cultureIndex).setInitResistanceChance(txtCULTInitResistanceChance.getInteger());
            culture.get(cultureIndex).setContinuedResistanceChance(txtCULTContinuedResistanceChance.getInteger());
            rule.get(0).setBorderExpansionMultiplier(txtRULELevelMultiplier.getInteger());
            rule.get(0).setBorderFactor(txtRULEBorderFactor.getInteger());
        }

        cultureIndex = lstCultures.getSelectedIndex();
        if (cultureIndex > -1)
        {
            txtCULTPropagandaSuccess.setText(Integer.toString(culture.get(cultureIndex).getPropagandaSuccess()));
            txtCULTRatioDenominator.setText(Integer.toString(culture.get(cultureIndex).getRatioDenominator()));
            txtCULTRatioNumerator.setText(Integer.toString(culture.get(cultureIndex).getRatioNumerator()));
            txtCULTInitResistanceChance.setText(Integer.toString(culture.get(cultureIndex).getInitResistanceChance()));
            txtCULTContinuedResistanceChance.setText(Integer.toString(culture.get(cultureIndex).getContinuedResistanceChance()));
        }
        //Do not need to update the culture list every time you change a culture, as it doesn't depend on that
        //Do update the other rule values above as there is no specific method for doing so

    }

    /**
     * Removes all limits on length and values for text fields.
     */
    public void setNoLimits()
    {
        clearBadValueDocumentListeners();
        clearLengthDocumentListeners();
        if (logger.isDebugEnabled())
            logger.debug("SETTING NO LIMITS for CULT tab");
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
        addBadValueDocumentListener(1000000, 1, txtRULELevelMultiplier);
        addBadValueDocumentListener(100, txtCULTInitResistanceChance);
        addBadValueDocumentListener(100, txtCULTContinuedResistanceChance);
        addBadValueDocumentListener(100, txtCULTPropagandaSuccess);
        addBadValueDocumentListener(100, 1, txtRULEBorderFactor);
        addBadValueDocumentListener(10, 1, txtCULTRatioDenominator);
        addBadValueDocumentListener(10, 1, txtCULTRatioNumerator);
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
        culture.get(index).setName(name);
    }
}
