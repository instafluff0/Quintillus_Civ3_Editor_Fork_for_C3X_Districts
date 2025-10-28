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
import com.civfanatics.civ3.biqFile.WSIZ;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJList;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJTextField;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperListModel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;

/**
 * This class represents the World Size tab in the Civilization editor.
 * 
 * @author Andrew
 */
public class WSIZTab extends EditorTab {

    int worldSizeIndex;
    public List<WSIZ>worldSize;
    boolean tabCreated;

    private JLabel jLabel85;
    private JLabel jLabel86;
    private JLabel jLabel87;
    private JLabel jLabel88;
    private JLabel jLabel89;
    private JLabel jLabel90;
    private JLabel jLabel91;
    private JScrollPane jScrollPane17;
    private SuperJList lstWorldSizes = new SuperJList(this, "world size", false, false);
    private SuperJTextField txtWSIZDistanceBetweenCivs;
    private SuperJTextField txtWSIZEmpty;
    private SuperJTextField txtWSIZHeight;
    private SuperJTextField txtWSIZNumberOfCivs;
    private SuperJTextField txtWSIZOptimalNumberOfCities;
    private SuperJTextField txtWSIZTechRate;
    private SuperJTextField txtWSIZWidth;

    private SuperListModel worldSizeList;

    /**
     * The constructor.
     */
    public WSIZTab()
    {
        lstType = lstWorldSizes;
        tabName = "WSIZ";
        textBoxes = new ArrayList<>();
        jScrollPane17 = new JScrollPane();
        jLabel85 = new JLabel();
        txtWSIZWidth = new SuperJTextField();
        jLabel86 = new JLabel();
        txtWSIZHeight = new SuperJTextField();
        jLabel87 = new JLabel();
        jLabel88 = new JLabel();
        jLabel89 = new JLabel();
        jLabel90 = new JLabel();
        txtWSIZNumberOfCivs = new SuperJTextField();
        txtWSIZDistanceBetweenCivs = new SuperJTextField();
        txtWSIZTechRate = new SuperJTextField();
        txtWSIZOptimalNumberOfCities = new SuperJTextField();
        jLabel91 = new JLabel();
        txtWSIZEmpty = new SuperJTextField();

        worldSizeList = new SuperListModel();
        lstWorldSizes.setModel(worldSizeList);

        worldSizeIndex = -1;
    }
    /**
     * Creates the GUI of the tab.  This includes button groups, layouts, etc.
     * Logically, this is to occur immediately after the constructor is called.
     *
     * @return - A reference to the WSIZTab instance itself.  This reference
     * allows the tab to be added to a tab bar.
     */
    public JPanel createTab()
    {
        lstWorldSizes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstWorldSizes.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstWorldSizesValueChanged(evt);
            }
        });
        jScrollPane17.setViewportView(lstWorldSizes);

        jLabel85.setText("Width:");

        txtWSIZWidth.setText("                   ");

        jLabel86.setText("Height:");

        txtWSIZHeight.setText("                   ");

        jLabel87.setText("Number of Civs:");

        jLabel88.setText("Tech Rate:");

        jLabel89.setText("Optimal City Number:");

        jLabel90.setText("Distance between Civs:");

        txtWSIZNumberOfCivs.setText("                   ");

        txtWSIZDistanceBetweenCivs.setText("                   ");

        txtWSIZTechRate.setText("                   ");

        txtWSIZOptimalNumberOfCities.setText("                   ");

        jLabel91.setText("Unknown String (24 characters maximum):");

        txtWSIZEmpty.setText("                                                         ");

        org.jdesktop.layout.GroupLayout thisLayout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(thisLayout);
        thisLayout.setHorizontalGroup(
            thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(thisLayout.createSequentialGroup()
                .add(jScrollPane17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 162, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, thisLayout.createSequentialGroup()
                            .add(jLabel85)
                            .add(108, 108, 108)
                            .add(txtWSIZWidth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(thisLayout.createSequentialGroup()
                            .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(thisLayout.createSequentialGroup()
                                    .add(jLabel86)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .add(org.jdesktop.layout.GroupLayout.TRAILING, thisLayout.createSequentialGroup()
                                    .add(jLabel87)
                                    .add(57, 57, 57)))
                            .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(txtWSIZNumberOfCivs, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(txtWSIZHeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                    .add(thisLayout.createSequentialGroup()
                        .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel90)
                            .add(jLabel88)
                            .add(jLabel89))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(txtWSIZOptimalNumberOfCities, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(txtWSIZTechRate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(txtWSIZDistanceBetweenCivs, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(thisLayout.createSequentialGroup()
                        .add(jLabel91)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtWSIZEmpty, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 185, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(410, Short.MAX_VALUE))
        );
        thisLayout.setVerticalGroup(
            thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane17, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE)
            .add(thisLayout.createSequentialGroup()
                .addContainerGap()
                .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel85)
                    .add(txtWSIZWidth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel86)
                    .add(txtWSIZHeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel87)
                    .add(txtWSIZNumberOfCivs, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel90)
                    .add(txtWSIZDistanceBetweenCivs, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel88)
                    .add(txtWSIZTechRate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel89)
                    .add(txtWSIZOptimalNumberOfCities, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(51, 51, 51)
                .add(thisLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel91)
                    .add(txtWSIZEmpty, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(439, Short.MAX_VALUE))
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
     * @param worldSize - The list of all world sizes.
     */
    public void sendData(List<WSIZ>worldSize)
    {
        assert tabCreated:"Tab must be created before data is sent";
        this.worldSize = worldSize;
        worldSizeList.removeAllElements();
        for (int i = 0; i < worldSize.size(); i++)
        {
            worldSizeList.addElement(worldSize.get(i).getName());
        }
    }
    /**
     * Method that stores all tab data to memory (in case any fields have changed
     * on-screen), and then updates the screen based on the new world size
     * selected.  As this method may be invoked manually (for example, prior
     * to the file being saved to disk), it is possible that the new world size
     * selected will be the same one as the one previously selected.
     *
     */
    public void updateTab()
    {
        if (!(worldSizeIndex == -1)) {
            worldSize.get(worldSizeIndex).setOptimalNumberOfCities(txtWSIZOptimalNumberOfCities.getInteger());
            worldSize.get(worldSizeIndex).setTechRate(txtWSIZTechRate.getInteger());
            worldSize.get(worldSizeIndex).setEmpty(txtWSIZEmpty.getText());
            worldSize.get(worldSizeIndex).setHeight(txtWSIZHeight.getInteger());
            worldSize.get(worldSizeIndex).setDistanceBetweenCivs(txtWSIZDistanceBetweenCivs.getInteger());
            worldSize.get(worldSizeIndex).setNumberOfCivs(txtWSIZNumberOfCivs.getInteger());
            worldSize.get(worldSizeIndex).setWidth(txtWSIZWidth.getInteger());
        }
        worldSizeIndex = lstWorldSizes.getSelectedIndex();
        if (worldSizeIndex != -1)
        {
            txtWSIZOptimalNumberOfCities.setText(Integer.toString(worldSize.get(worldSizeIndex).getOptimalNumberOfCities()));
            txtWSIZTechRate.setText(Integer.toString(worldSize.get(worldSizeIndex).getTechRate()));
            txtWSIZEmpty.setText(worldSize.get(worldSizeIndex).getEmpty());
            txtWSIZHeight.setText(Integer.toString(worldSize.get(worldSizeIndex).getHeight()));
            txtWSIZDistanceBetweenCivs.setText(Integer.toString(worldSize.get(worldSizeIndex).getDistanceBetweenCivs()));
            txtWSIZNumberOfCivs.setText(Integer.toString(worldSize.get(worldSizeIndex).getNumberOfCivs()));
            txtWSIZWidth.setText(Integer.toString(worldSize.get(worldSizeIndex).getWidth()));
        }

    }

    private void lstWorldSizesValueChanged(javax.swing.event.ListSelectionEvent evt) {
        // :
        updateTab();
    }

    public void setSelectedIndex(int i)
    {
        worldSizeIndex = i;
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
        addLengthDocumentListener(23, txtWSIZEmpty);
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
        //Max Firaxis Value = 1000
        addBadValueDocumentListener(1000, 1, txtWSIZOptimalNumberOfCities);
        addBadValueDocumentListener(1000, txtWSIZTechRate);
        //Max Firaxis value = 362
        addBadValueDocumentListener(362, 1, txtWSIZDistanceBetweenCivs);
        addBadValueDocumentListener(362, 16, txtWSIZHeight);
        addBadValueDocumentListener(362, 16, txtWSIZWidth);
        //Max Firaxis Value = 31
        addBadValueDocumentListener(31, 1, txtWSIZNumberOfCivs);
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
        worldSize.get(index).setName(name);
    }
}
