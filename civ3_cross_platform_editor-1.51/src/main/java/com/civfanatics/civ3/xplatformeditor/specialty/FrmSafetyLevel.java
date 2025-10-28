/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FrmSafetyLevel.java
 *
 * Created on Jun 26, 2010, 10:37:35 AM
 */

package com.civfanatics.civ3.xplatformeditor.specialty;

import com.civfanatics.civ3.xplatformeditor.Main;
import java.awt.Color;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.event.ListDataListener;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class FrmSafetyLevel extends javax.swing.JFrame {
    Logger logger = Logger.getLogger(this.getClass());


    public Map safetyLevels;
    public Main mainWindow;


        public void setSelectedItem(Object anItem)
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Object getSelectedItem()
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Object getElementAt(int index)
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void addListDataListener(ListDataListener l)
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void removeListDataListener(ListDataListener l)
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    /** Creates new form FrmSafetyLevel */
    public FrmSafetyLevel() {
        initComponents();
        logger.setLevel(Level.DEBUG);
        cmbSL_BLDG.setModel(new javax.swing.DefaultComboBoxModel(SafetyLevel.values()));
        cmbSL_RACE.setModel(new javax.swing.DefaultComboBoxModel(SafetyLevel.values()));
        cmbSL_CTZN.setModel(new javax.swing.DefaultComboBoxModel(SafetyLevel.values()));
        cmbSL_CULT.setModel(new javax.swing.DefaultComboBoxModel(SafetyLevel.values()));
        cmbSL_DIFF.setModel(new javax.swing.DefaultComboBoxModel(SafetyLevel.values()));
        cmbSL_ERA.setModel(new javax.swing.DefaultComboBoxModel(SafetyLevel.values()));
        cmbSL_ESPN.setModel(new javax.swing.DefaultComboBoxModel(SafetyLevel.values()));
        cmbSL_EXPR.setModel(new javax.swing.DefaultComboBoxModel(SafetyLevel.values()));
        cmbSL_FLAV.setModel(new javax.swing.DefaultComboBoxModel(SafetyLevel.values()));
        cmbSL_GOOD.setModel(new javax.swing.DefaultComboBoxModel(SafetyLevel.values()));
        cmbSL_GOVT.setModel(new javax.swing.DefaultComboBoxModel(SafetyLevel.values()));
        cmbSL_PLYR.setModel(new javax.swing.DefaultComboBoxModel(SafetyLevel.values()));
        cmbSL_RULE.setModel(new javax.swing.DefaultComboBoxModel(SafetyLevel.values()));
        cmbSL_GAME.setModel(new javax.swing.DefaultComboBoxModel(SafetyLevel.values()));
        cmbSL_TECH.setModel(new javax.swing.DefaultComboBoxModel(SafetyLevel.values()));
        cmbSL_TERR.setModel(new javax.swing.DefaultComboBoxModel(SafetyLevel.values()));
        cmbSL_TRFM.setModel(new javax.swing.DefaultComboBoxModel(SafetyLevel.values()));
        cmbSL_PRTO.setModel(new javax.swing.DefaultComboBoxModel(SafetyLevel.values()));
        cmbSL_WSIZ.setModel(new javax.swing.DefaultComboBoxModel(SafetyLevel.values()));
        cmbSL_Map.setModel(new javax.swing.DefaultComboBoxModel(SafetyLevel.values()));
        cmbSL_ALL.setModel(new javax.swing.DefaultComboBoxModel(SafetyLevel.values()));

        JComboBox[] temp = {cmbSL_BLDG, cmbSL_CTZN, cmbSL_CULT, cmbSL_DIFF, cmbSL_EXPR, cmbSL_ERA, cmbSL_BLDG, cmbSL_ESPN, cmbSL_GOOD, cmbSL_FLAV, cmbSL_GAME, cmbSL_GOVT, cmbSL_PLYR, cmbSL_PRTO, cmbSL_RACE, cmbSL_RULE, cmbSL_WSIZ, cmbSL_TECH, cmbSL_TRFM, cmbSL_TERR};
        this.comboBoxes = temp;

        //Todo: Get text areas to start at top.  For some infuriating reason they don't, even when I set
        //the vertical scrollbar value to either zero or minimum.  Oh well.

        cmbSL_ALL.setSelectedIndex(SafetyLevel.Safe.ordinal());
    }

    public void syncWithMain(Map map, Main main)
    {
        this.safetyLevels = map;
        this.mainWindow = main;
        
        //Set safety levels
        jLabel19.setText("PROP");
        
        cmbSL_BLDG.setSelectedIndex(Main.safetyLevels.get("BLDG").ordinal());
        cmbSL_RACE.setSelectedIndex(Main.safetyLevels.get("RACE").ordinal());
        cmbSL_CTZN.setSelectedIndex(Main.safetyLevels.get("CTZN").ordinal());
        cmbSL_CULT.setSelectedIndex(Main.safetyLevels.get("CULT").ordinal());
        cmbSL_DIFF.setSelectedIndex(Main.safetyLevels.get("DIFF").ordinal());
        cmbSL_ERA.setSelectedIndex(Main.safetyLevels.get("ERA").ordinal());
        cmbSL_ESPN.setSelectedIndex(Main.safetyLevels.get("ESPN").ordinal());
        cmbSL_EXPR.setSelectedIndex(Main.safetyLevels.get("EXPR").ordinal());
        cmbSL_FLAV.setSelectedIndex(Main.safetyLevels.get("FLAV").ordinal());
        cmbSL_GOOD.setSelectedIndex(Main.safetyLevels.get("GOOD").ordinal());
        cmbSL_GOVT.setSelectedIndex(Main.safetyLevels.get("GOVT").ordinal());
        cmbSL_PLYR.setSelectedIndex(Main.safetyLevels.get("PLYR").ordinal());
        cmbSL_RULE.setSelectedIndex(Main.safetyLevels.get("RULE").ordinal());
        cmbSL_GAME.setSelectedIndex(Main.safetyLevels.get("PROP").ordinal());
        cmbSL_TECH.setSelectedIndex(Main.safetyLevels.get("TECH").ordinal());
        cmbSL_TERR.setSelectedIndex(Main.safetyLevels.get("TERR").ordinal());
        cmbSL_TRFM.setSelectedIndex(Main.safetyLevels.get("TRFM").ordinal());
        cmbSL_PRTO.setSelectedIndex(Main.safetyLevels.get("PRTO").ordinal());
        cmbSL_WSIZ.setSelectedIndex(Main.safetyLevels.get("WSIZ").ordinal());
        cmbSL_Map.setSelectedIndex(Main.safetyLevels.get("Map").ordinal());
        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtTotal = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        scrFiraxis = new javax.swing.JScrollPane();
        txtFiraxis = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        scrSafe = new javax.swing.JScrollPane();
        txtSafe = new javax.swing.JTextArea();
        lblSafe = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        lblSafe1 = new javax.swing.JLabel();
        scrSafe1 = new javax.swing.JScrollPane();
        txtExploratory = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        scrSafe2 = new javax.swing.JScrollPane();
        txtNone = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        lblSafe2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        scrSafe3 = new javax.swing.JScrollPane();
        txtMinimal = new javax.swing.JTextArea();
        jLabel10 = new javax.swing.JLabel();
        lblSafe3 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbSL_BLDG = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        cmbSL_CTZN = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        cmbSL_DIFF = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        cmbSL_ESPN = new javax.swing.JComboBox();
        cmbSL_EXPR = new javax.swing.JComboBox();
        cmbSL_ERA = new javax.swing.JComboBox();
        cmbSL_CULT = new javax.swing.JComboBox();
        cmbSL_RACE = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        cmbSL_GAME = new javax.swing.JComboBox();
        cmbSL_TERR = new javax.swing.JComboBox();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        cmbSL_PLYR = new javax.swing.JComboBox();
        cmbSL_GOOD = new javax.swing.JComboBox();
        jLabel21 = new javax.swing.JLabel();
        cmbSL_TECH = new javax.swing.JComboBox();
        cmbSL_RULE = new javax.swing.JComboBox();
        cmbSL_GOVT = new javax.swing.JComboBox();
        cmbSL_FLAV = new javax.swing.JComboBox();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        cmbSL_PRTO = new javax.swing.JComboBox();
        cmbSL_WSIZ = new javax.swing.JComboBox();
        cmbSL_TRFM = new javax.swing.JComboBox();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        cmdUpdate = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        cmbSL_ALL = new javax.swing.JComboBox();
        cmbSL_Map = new javax.swing.JComboBox();
        jLabel32 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Safety Levels");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("When Firaxis introduced their Civilization III editors, they also introduced\na set of maximal/minimal values for most variables within the BIQ.  And with\nreason - there were areas where setting the values outside of Firaxis's limits\nwould cause significant problems at some point in a scenario.  These\nlimitations served to protect modders from many (though not all) of the\nscenarios that would render their scenarios unplayable.  Leading editors have\ncontinued to follow that trend.\n\nHowever, though some of the limitations are made for real technical\nreasons, many are fairly arbitrary.  Exactly which ones are arbitrary is an open\nquestion, but early exploration indicates many may be arbitrary.  This editor\nhopes to make exploration of these boundaries much easier to the Civ3\ncommunity at large, eventually making richer scenarios possible.  To that\nend, it introduces a set of safety levels - you can have it enforce Firaxis's\nlimitations if you want to be (almost) certain your scenario will run, or you can\nchoose any of several lesser levels of safety to allow you to explore the\nboundaries of what is possible with the Conquests engine itself.\n\nSee the description of each level on the right.");
        jScrollPane1.setViewportView(jTextArea1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 47, 432, 310));

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Safety Level");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(78, 12, 290, 29));

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtTotal.setColumns(20);
        txtTotal.setRows(5);
        txtTotal.setText("At this level, the editor will attempt to check for problems that\nmay cause an error, but would not be caught by the Firaxis \neditor.  This includes, for example, errors due to an art file \nmissing.");
        jScrollPane2.setViewportView(txtTotal);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 350, -1));

        jLabel5.setBackground(new java.awt.Color(0, 255, 0));
        jLabel5.setOpaque(true);
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 61, 93));

        jLabel4.setText("Safety Level: Total");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 0, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 0, 450, 120));

        jPanel2.setPreferredSize(new java.awt.Dimension(448, 121));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtFiraxis.setColumns(20);
        txtFiraxis.setRows(5);
        txtFiraxis.setText("At this level, the capabilities of the editor should be equal to \nthat of the Firaxis editor.\n\nThis is recommended for those who wish to be sure that\ntheir scenarios will not fall victim to any unofficial capability\nthat appears to work but does not actually work all the time.  \n\nFiles saved with SAFETY_FIRAXIS should work with both\nConquests and the official Firaxis editor.");
        scrFiraxis.setViewportView(txtFiraxis);

        jPanel2.add(scrFiraxis, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 350, -1));

        jLabel2.setText("Safety Level: Firaxis");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 0, -1, -1));

        jLabel3.setBackground(new java.awt.Color(85, 255, 0));
        jLabel3.setOpaque(true);
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, 61, 100));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 120, -1, -1));

        jPanel3.setPreferredSize(new java.awt.Dimension(448, 121));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtSafe.setColumns(20);
        txtSafe.setRows(5);
        txtSafe.setText("At this level, only those settings which are known to be \nsupported by Conquests will be enabled.  This includes \nsome settings that aren't permissable in Firaxis' editor, \nsuch as however many difficulty levels you want, and some\nnumber of flavors other than 7.  \n\nThis level is recommended for general-purpose use, \nespecially for those interested in new developments in \nCiv3 modding possibilities. Files saved with \nSAFETY_SAFE should be perfectly playable by \nConquests, although it is possible Firaxis' editor would\ncomplain about them or not be able to properly render all \nthe options (e.g. it couldn't render 8 flavors properly, even \nif Conquests can handle that many).");
        scrSafe.setViewportView(txtSafe);

        jPanel3.add(scrSafe, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 350, -1));

        lblSafe.setText("Safety Level: Safe");
        jPanel3.add(lblSafe, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 0, -1, -1));

        jLabel7.setBackground(new java.awt.Color(170, 255, 0));
        jLabel7.setOpaque(true);
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, 61, 100));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 240, -1, -1));

        jPanel4.setPreferredSize(new java.awt.Dimension(448, 121));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblSafe1.setText("Safety Level: Exploratory");
        jPanel4.add(lblSafe1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 0, -1, -1));

        txtExploratory.setColumns(20);
        txtExploratory.setRows(5);
        txtExploratory.setText("Anything that is known to cause a problem with \nCiv3Conquests (Windows version 1.22) will be disabled, \nsuch as 4 options for world sizes. If something isn't allowed\nby Firaxis's editor but has not been shown to cause \nproblems in Civ3, it will still be allowed on the chance that \na new capability will be uncovered. \n\nThis level is recommended for those looking for new ways\nto push the Civ3 engine. Files saved with \nSAFETY_EXPLORATORY should be readable by this editor,\nbut might not be with Civ3ConquestsEdit or Conquests \nitself.");
        scrSafe1.setViewportView(txtExploratory);

        jPanel4.add(scrSafe1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 350, 98));

        jLabel8.setBackground(new java.awt.Color(255, 255, 0));
        jLabel8.setOpaque(true);
        jPanel4.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 61, 100));

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 360, -1, -1));

        jPanel5.setPreferredSize(new java.awt.Dimension(448, 121));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtNone.setColumns(20);
        txtNone.setRows(5);
        txtNone.setText("No safety - no protections against messing up the file.\n\nIt will be possible to modify a file with SAFETY_NONE, save \nit, and not be able to reopen it again.\n\nIn some circumstances, the ability to exceed the maximum\nlength of a field may allow you to add an extra character\nto a name without adverse effect.  If you have checked the \nBIQ format and are confident your changes will not cause\nany problems, feel free to experiment!  Backing up the\nBIQ first is highly recommended.");
        scrSafe2.setViewportView(txtNone);

        jPanel5.add(scrSafe2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 350, 98));

        jLabel9.setBackground(new java.awt.Color(255, 0, 0));
        jLabel9.setOpaque(true);
        jPanel5.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 61, 100));

        lblSafe2.setText("Safety Level: None");
        jPanel5.add(lblSafe2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 0, -1, -1));

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 600, -1, -1));

        jPanel6.setPreferredSize(new java.awt.Dimension(448, 121));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scrSafe3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtMinimal.setColumns(20);
        txtMinimal.setRows(5);
        txtMinimal.setText("Minimal safety protections - protects against too long of \nstrings going into the next file and making the BIQ unusable \n(even by the editor). Anything that doesn't make the BIQ \ntechnically illegal will be allowed, even if it will not be accepted\nby Civ3Conquests or Civ3ConquestsEdit. \nAny file saved with SAFETY_MINIMAL changes should be \nreadable by this editor.\n\nThis level is only recommended if you suspect a boundary\ncondition is unduly constrained in Exploratory and would\nactually work just fine in Conquests.");
        scrSafe3.setViewportView(txtMinimal);

        jPanel6.add(scrSafe3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 350, 98));

        jLabel10.setBackground(new java.awt.Color(255, 128, 0));
        jLabel10.setOpaque(true);
        jPanel6.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 61, 100));

        lblSafe3.setText("Safety Level: Minimal");
        jPanel6.add(lblSafe3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 0, -1, -1));

        getContentPane().add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 480, -1, -1));

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Settings"));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("BLDG");
        jPanel7.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 16, -1, 30));

        cmbSL_BLDG.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel7.add(cmbSL_BLDG, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 150, -1));

        jLabel11.setText("CTZN");
        jPanel7.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 46, -1, 30));

        cmbSL_CTZN.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel7.add(cmbSL_CTZN, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 150, -1));

        jLabel12.setText("DIFF");
        jPanel7.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, 20));

        cmbSL_DIFF.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel7.add(cmbSL_DIFF, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 150, -1));

        jLabel13.setText("ESPN");
        jPanel7.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, 20));

        cmbSL_ESPN.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel7.add(cmbSL_ESPN, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, 150, -1));

        cmbSL_EXPR.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel7.add(cmbSL_EXPR, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 110, 150, -1));

        cmbSL_ERA.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel7.add(cmbSL_ERA, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 80, 150, -1));

        cmbSL_CULT.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel7.add(cmbSL_CULT, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 50, 150, -1));

        cmbSL_RACE.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel7.add(cmbSL_RACE, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, 150, -1));

        jLabel14.setText("Civs");
        jPanel7.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, -1, 30));

        jLabel15.setText("CULT");
        jPanel7.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, -1, 30));

        jLabel16.setText("ERA");
        jPanel7.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, -1, 20));

        jLabel17.setText("EXPR");
        jPanel7.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, -1, 20));

        jLabel18.setText("PLYR");
        jPanel7.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 170, -1, 30));

        cmbSL_GAME.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel7.add(cmbSL_GAME, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 200, 150, -1));

        cmbSL_TERR.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel7.add(cmbSL_TERR, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 230, 150, -1));

        jLabel19.setText("SCPR");
        jPanel7.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 200, -1, 20));

        jLabel20.setText("GOOD");
        jPanel7.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 140, -1, 30));

        cmbSL_PLYR.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel7.add(cmbSL_PLYR, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 170, 150, -1));

        cmbSL_GOOD.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel7.add(cmbSL_GOOD, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 140, 150, -1));

        jLabel21.setText("TERR");
        jPanel7.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 230, -1, 20));

        cmbSL_TECH.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel7.add(cmbSL_TECH, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 230, 150, -1));

        cmbSL_RULE.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel7.add(cmbSL_RULE, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, 150, -1));

        cmbSL_GOVT.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel7.add(cmbSL_GOVT, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 170, 150, -1));

        cmbSL_FLAV.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel7.add(cmbSL_FLAV, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, 150, -1));

        jLabel22.setText("FLAV");
        jPanel7.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, 30));

        jLabel23.setText("GOVT");
        jPanel7.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, -1, 30));

        jLabel24.setText("RULE");
        jPanel7.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, -1, 20));

        jLabel25.setText("TECH");
        jPanel7.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, -1, 20));

        jLabel28.setText("Unit");
        jPanel7.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 260, -1, 30));

        cmbSL_PRTO.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel7.add(cmbSL_PRTO, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 260, 150, -1));

        cmbSL_WSIZ.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel7.add(cmbSL_WSIZ, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 290, 150, -1));

        cmbSL_TRFM.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel7.add(cmbSL_TRFM, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 260, 150, -1));

        jLabel30.setText("TRFM");
        jPanel7.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, -1, 30));

        jLabel31.setText("WSIZ");
        jPanel7.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, -1, 30));

        cmdUpdate.setText("Update");
        cmdUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdUpdateActionPerformed(evt);
            }
        });
        jPanel7.add(cmdUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 350, -1, -1));

        jLabel29.setText("ALL:");
        jPanel7.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 290, -1, 30));

        cmbSL_ALL.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbSL_ALL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSL_ALLActionPerformed(evt);
            }
        });
        jPanel7.add(cmbSL_ALL, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 290, 150, -1));

        cmbSL_Map.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel7.add(cmbSL_Map, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 320, 150, -1));

        jLabel32.setText("Map");
        jPanel7.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 30, 20));

        getContentPane().add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 430, 390));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdUpdateActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmdUpdateActionPerformed
    {//GEN-HEADEREND:event_cmdUpdateActionPerformed
        //If you get an uncompilable source code error when updating the safety level from source,
        //clean and build the shared components and the main project.  The error should then go away.
        safetyLevels.put("BLDG", SafetyLevel.valueOf(cmbSL_BLDG.getSelectedItem().toString()));
        safetyLevels.put("CTZN", SafetyLevel.valueOf(cmbSL_CTZN.getSelectedItem().toString()));
        safetyLevels.put("CULT", SafetyLevel.valueOf(cmbSL_CULT.getSelectedItem().toString()));
        safetyLevels.put("DIFF", SafetyLevel.valueOf(cmbSL_DIFF.getSelectedItem().toString()));
        safetyLevels.put("ERA", SafetyLevel.valueOf(cmbSL_ERA.getSelectedItem().toString()));
        safetyLevels.put("ESPN", SafetyLevel.valueOf(cmbSL_ESPN.getSelectedItem().toString()));
        safetyLevels.put("EXPR", SafetyLevel.valueOf(cmbSL_EXPR.getSelectedItem().toString()));
        safetyLevels.put("FLAV", SafetyLevel.valueOf(cmbSL_FLAV.getSelectedItem().toString()));
        safetyLevels.put("GOOD", SafetyLevel.valueOf(cmbSL_GOOD.getSelectedItem().toString()));
        safetyLevels.put("GOVT", SafetyLevel.valueOf(cmbSL_GOVT.getSelectedItem().toString()));
        safetyLevels.put("PLYR", SafetyLevel.valueOf(cmbSL_PLYR.getSelectedItem().toString()));
        safetyLevels.put("PRTO", SafetyLevel.valueOf(cmbSL_PRTO.getSelectedItem().toString()));
        safetyLevels.put("RACE", SafetyLevel.valueOf(cmbSL_RACE.getSelectedItem().toString()));
        safetyLevels.put("TECH", SafetyLevel.valueOf(cmbSL_TECH.getSelectedItem().toString()));
        safetyLevels.put("TERR", SafetyLevel.valueOf(cmbSL_TERR.getSelectedItem().toString()));
        safetyLevels.put("TRFM", SafetyLevel.valueOf(cmbSL_TRFM.getSelectedItem().toString()));
        safetyLevels.put("GAME", SafetyLevel.valueOf(cmbSL_GAME.getSelectedItem().toString()));
        safetyLevels.put("RULE", SafetyLevel.valueOf(cmbSL_RULE.getSelectedItem().toString()));
        safetyLevels.put("WSIZ", SafetyLevel.valueOf(cmbSL_WSIZ.getSelectedItem().toString()));
        safetyLevels.put("PROP", SafetyLevel.valueOf(cmbSL_GAME.getSelectedItem().toString()));
        safetyLevels.put("Map", SafetyLevel.valueOf(cmbSL_Map.getSelectedItem().toString()));
        //This window automatically updates the map as it has a reference to it,
        //not a copy of it.
        mainWindow.alertToSafetyUpdate();
        this.setVisible(false);
    }//GEN-LAST:event_cmdUpdateActionPerformed

    private SafetyLevel min(Map safetyLevels)
    {
        java.util.Collection values = safetyLevels.values();
        SafetyLevel minimal = SafetyLevel.Total;
        java.util.Iterator i = values.iterator();
        while (i.hasNext())
        {
            SafetyLevel next = (SafetyLevel)i.next();
            if (next.compareTo(minimal) < 0)
                minimal = next;
        }
        return minimal;
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setVisible(false);
    }//GEN-LAST:event_formWindowClosing

    private void cmbSL_ALLActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbSL_ALLActionPerformed
    {//GEN-HEADEREND:event_cmbSL_ALLActionPerformed
        for (int i = 0; i < comboBoxes.length; i++)
        {
            comboBoxes[i].setSelectedIndex(cmbSL_ALL.getSelectedIndex());
        }
    }//GEN-LAST:event_cmbSL_ALLActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String[]args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmSafetyLevel().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbSL_ALL;
    private javax.swing.JComboBox cmbSL_BLDG;
    private javax.swing.JComboBox cmbSL_CTZN;
    private javax.swing.JComboBox cmbSL_CULT;
    private javax.swing.JComboBox cmbSL_DIFF;
    private javax.swing.JComboBox cmbSL_ERA;
    private javax.swing.JComboBox cmbSL_ESPN;
    private javax.swing.JComboBox cmbSL_EXPR;
    private javax.swing.JComboBox cmbSL_FLAV;
    private javax.swing.JComboBox cmbSL_GAME;
    private javax.swing.JComboBox cmbSL_GOOD;
    private javax.swing.JComboBox cmbSL_GOVT;
    private javax.swing.JComboBox cmbSL_Map;
    private javax.swing.JComboBox cmbSL_PLYR;
    private javax.swing.JComboBox cmbSL_PRTO;
    private javax.swing.JComboBox cmbSL_RACE;
    private javax.swing.JComboBox cmbSL_RULE;
    private javax.swing.JComboBox cmbSL_TECH;
    private javax.swing.JComboBox cmbSL_TERR;
    private javax.swing.JComboBox cmbSL_TRFM;
    private javax.swing.JComboBox cmbSL_WSIZ;
    private javax.swing.JButton cmdUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel lblSafe;
    private javax.swing.JLabel lblSafe1;
    private javax.swing.JLabel lblSafe2;
    private javax.swing.JLabel lblSafe3;
    private javax.swing.JScrollPane scrFiraxis;
    private javax.swing.JScrollPane scrSafe;
    private javax.swing.JScrollPane scrSafe1;
    private javax.swing.JScrollPane scrSafe2;
    private javax.swing.JScrollPane scrSafe3;
    private javax.swing.JTextArea txtExploratory;
    private javax.swing.JTextArea txtFiraxis;
    private javax.swing.JTextArea txtMinimal;
    private javax.swing.JTextArea txtNone;
    private javax.swing.JTextArea txtSafe;
    private javax.swing.JTextArea txtTotal;
    // End of variables declaration//GEN-END:variables

    JComboBox[] comboBoxes;
}
