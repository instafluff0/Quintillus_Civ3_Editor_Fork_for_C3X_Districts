/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CSVPanel.java
 *Ex
 * Created on Nov 8, 2010, 11:23:11 PM
 */

package com.civfanatics.civ3.xplatformeditor.specialty;

import com.civfanatics.civ3.biqFile.BLDG;
import com.civfanatics.civ3.biqFile.GOOD;
import com.civfanatics.civ3.biqFile.IO;
import com.civfanatics.civ3.biqFile.PRTO;
import com.civfanatics.civ3.biqFile.TECH;
import com.civfanatics.civ3.xplatformeditor.Main;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import org.apache.log4j.Logger;

/**
 *
 * @author Andrew
 */
public class CSVImportPanel extends javax.swing.JFrame {

    Logger logger = Logger.getLogger(this.getClass());

    static final int NUM_UNIT_ATTRIBUTES = 11;
    static final int NUM_BLDG_ATTRIBUTES = 20;
    static final int NUM_TECH_ATTRIBUTES = 7;
    static final int NUM_RESOURCE_ATTRIBUTES = 6;
    static final int SPNR_WIDTH = 16;
    static final String NONE = "None";
    int[]unitSpinnerValues;
    JSpinner[]unitSpinners;
    String[]unitAttributes;
    JCheckBox[]unitChecks;

    int[]buildingSpinnerValues;
    String[]buildingAttributes;
    JCheckBox[]buildingChecks;
    JSpinner[]buildingSpinners;

    int[]techSpinnerValues;
    String[]techAttributes;
    JSpinner[]techSpinners;
    JCheckBox[]techChecks;

    int[]resourceSpinnerValues;
    String[]resourceAttributes;
    JSpinner[]resourceSpinners;
    JCheckBox[]resourceChecks;
    JFrame thisFrame = this;

    IO biqFile;

    public CSVImportPanel(IO biqFile)
    {
        this();
        this.biqFile = biqFile;
    }

    /** Creates new form CSVPanel */
    public CSVImportPanel() {
        initComponents();
        unitSpinnerValues = new int[NUM_UNIT_ATTRIBUTES];
        for (int i = 0; i < NUM_UNIT_ATTRIBUTES; i++)
            unitSpinnerValues[i] = 0;
        unitSpinners = new JSpinner[NUM_UNIT_ATTRIBUTES];
        unitSpinners[0] = spnrUnit0;
        unitSpinners[1] = spnrUnit1;
        unitSpinners[2] = spnrUnit2;
        unitSpinners[3] = spnrUnit3;
        unitSpinners[4] = spnrUnit4;
        unitSpinners[5] = spnrUnit5;
        unitSpinners[6] = spnrUnit6;
        unitSpinners[7] = spnrUnit7;
        unitSpinners[8] = spnrUnit8;
        unitSpinners[9] = spnrUnit9;
        unitSpinners[10] = spnrUnit10;
        unitAttributes = new String[NUM_UNIT_ATTRIBUTES];
        unitAttributes[0] = "Unit Name";
        unitAttributes[1] = "Attack";
        unitAttributes[2] = "Defence";
        unitAttributes[3] = "Movement";
        unitAttributes[4] = "Hitpoint Bonus";
        unitAttributes[5] = "Bombard Strength";
        unitAttributes[6] = "Range";
        unitAttributes[7] = "Rate of Fire";
        unitAttributes[8] = "Cost";
        unitAttributes[9] = "Zone of Control";
        unitAttributes[10] = "Prerequisites";
        unitChecks = new JCheckBox[NUM_UNIT_ATTRIBUTES];
        
        unitChecks[0] = chkUnit0;
        unitChecks[1] = chkUnit1;
        unitChecks[2] = chkUnit2;
        unitChecks[3] = chkUnit3;
        unitChecks[4] = chkUnit4;
        unitChecks[5] = chkUnit5;
        unitChecks[6] = chkUnit6;
        unitChecks[7] = chkUnit7;
        unitChecks[8] = chkUnit8;
        unitChecks[9] = chkUnit9;
        unitChecks[10] = chkUnit10;

        for (int i = 0; i < NUM_UNIT_ATTRIBUTES; i++)
        {
            unitChecks[i].setSelected(true);
            unitChecks[i].setText(unitAttributes[i]);
        }

        //Buildings

        buildingAttributes = new String[NUM_BLDG_ATTRIBUTES];
        buildingChecks = new JCheckBox[NUM_BLDG_ATTRIBUTES];
        buildingSpinners = new JSpinner[NUM_BLDG_ATTRIBUTES];
        buildingSpinnerValues = new int[NUM_BLDG_ATTRIBUTES];

        buildingAttributes[0] = "Building Name";
        buildingAttributes[1] = "Type of Building";
        buildingAttributes[2] = "Cost";
        buildingAttributes[3] = "Maintenance";
        buildingAttributes[4] = "Pollution";
        buildingAttributes[5] = "Culture";
        buildingAttributes[6] = "Production Bonus";
        buildingAttributes[7] = "Autoproduces";
        buildingAttributes[8] = "Era";
        buildingAttributes[9] = "Required Technology";
        buildingAttributes[10] = "Required Resources";
        buildingAttributes[11] = "Required Government";
        buildingAttributes[12] = "Required Building";
        buildingAttributes[13] = "Happiness Bonus (city)";
        buildingAttributes[14] = "Happiness Bonus (empire)";
        buildingAttributes[15] = "Unhappiness (city)";
        buildingAttributes[16] = "Unhappiness (empire)";
        buildingAttributes[17] = "Air Attack";
        buildingAttributes[18] = "Defence Bonus";
        buildingAttributes[19] = "Available To";



        for (int i = 0; i < NUM_BLDG_ATTRIBUTES; i++)
        {
            buildingChecks[i] = new JCheckBox();
            buildingSpinners[i] = new JSpinner();
            buildingChecks[i].setSelected(true);
            buildingChecks[i].setText(buildingAttributes[i]);
        }
        //Techs
        techAttributes = new String[NUM_TECH_ATTRIBUTES];
        techSpinners = new JSpinner[NUM_TECH_ATTRIBUTES];
        techSpinnerValues = new int[NUM_TECH_ATTRIBUTES];
        techChecks = new JCheckBox[NUM_TECH_ATTRIBUTES];

        techAttributes[0] = "Tech Name";
        techAttributes[1] = "Era";
        techAttributes[2] = "Prerequisites";
        techAttributes[3] = "Cost";
        techAttributes[4] = "X position";
        techAttributes[5] = "Y position";
        techAttributes[6] = "Available To";

        for (int i = 0; i < NUM_TECH_ATTRIBUTES; i++)
        {
            techChecks[i] = new JCheckBox();
            techChecks[i].setSelected(true);
            techChecks[i].setText(techAttributes[i]);
            techSpinners[i] = new JSpinner();
            techSpinnerValues[i] = 0;
        }

        //Resources
        resourceAttributes = new String[NUM_RESOURCE_ATTRIBUTES];
        resourceSpinners = new JSpinner[NUM_RESOURCE_ATTRIBUTES];
        resourceSpinnerValues = new int[NUM_RESOURCE_ATTRIBUTES];
        resourceChecks = new JCheckBox[NUM_RESOURCE_ATTRIBUTES];

        resourceAttributes[0] = "Resource Name";
        resourceAttributes[1] = "Type of resource";
        resourceAttributes[2] = "Food bonus";
        resourceAttributes[3] = "Shield bonus";
        resourceAttributes[4] = "Commerce bonus";
        resourceAttributes[5] = "Prerequisites";

        for (int i = 0; i < NUM_RESOURCE_ATTRIBUTES; i++)
        {
            resourceChecks[i] = new JCheckBox();
            resourceChecks[i].setSelected(true);
            resourceChecks[i].setText(resourceAttributes[i]);
            resourceSpinners[i] = new JSpinner();
            resourceSpinnerValues[i] = 0;
        }

        setItUp();
    }

    private void initComponents()
    {
        pnlUnits = new javax.swing.JPanel();
        chkUnit1 = new javax.swing.JCheckBox();
        chkUnit0 = new javax.swing.JCheckBox();
        chkUnit4 = new javax.swing.JCheckBox();
        chkUnit5 = new javax.swing.JCheckBox();
        chkUnit6 = new javax.swing.JCheckBox();
        chkUnit7 = new javax.swing.JCheckBox();
        chkUnit8 = new javax.swing.JCheckBox();
        chkUnit9 = new javax.swing.JCheckBox();
        chkUnit10 = new javax.swing.JCheckBox();
        spnrUnit10 = new javax.swing.JSpinner();
        spnrUnit0 = new javax.swing.JSpinner();
        spnrUnit1 = new javax.swing.JSpinner();
        spnrUnit4 = new javax.swing.JSpinner();
        spnrUnit5 = new javax.swing.JSpinner();
        spnrUnit6 = new javax.swing.JSpinner();
        spnrUnit7 = new javax.swing.JSpinner();
        spnrUnit8 = new javax.swing.JSpinner();
        spnrUnit9 = new javax.swing.JSpinner();
        chkUnit2 = new javax.swing.JCheckBox();
        spnrUnit2 = new javax.swing.JSpinner();
        chkUnit3 = new javax.swing.JCheckBox();
        spnrUnit3 = new javax.swing.JSpinner();
        pnlBuildings = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        pnlTechnologies = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        pnlResources = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtDescription = new javax.swing.JTextArea();
        btnImport = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        chkIncludeUnits = new javax.swing.JCheckBox();
        chkIncludeBuildings = new javax.swing.JCheckBox();
        chkIncludeTechnologies = new javax.swing.JCheckBox();
        chkIncludeResources = new javax.swing.JCheckBox();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void setItUp() {



        getContentPane().setPreferredSize(new Dimension(900, 732));
        getContentPane().setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.gridheight = 2;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.5;
        c.weighty = 0.25;
        txtDescription.setColumns(20);
        txtDescription.setRows(5);
        txtDescription.setText("This utility allows importing unit stats from a CSV file.\nThis will replace the statistics of existing units with the same name as the imported ones, with the statistics in the CSV file.\n\n"
                + "The format of the CSV should match that which is exported from the CSV export utility.\nThe editor will match up column names with those seen in the CSV exporter\nwhen importing statistics.");
        jScrollPane4.setViewportView(txtDescription);
        getContentPane().add(jScrollPane4, c);



        //setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Comma Separated Value Import Tool");


        /**
         * UNITS PANEL
         */
        pnlUnits.setBorder(javax.swing.BorderFactory.createTitledBorder("Units"));
        if (logger.isDebugEnabled())
            logger.debug(c.weightx);



        chkUnit1.setText("Attack");

        chkUnit0.setText("Unit Name");

        chkUnit4.setText("Hitpoint Bonus");

        chkUnit5.setText("Bombard Stength");

        chkUnit6.setText("Range");

        chkUnit7.setText("Rate of Fire");

        chkUnit8.setText("Cost");

        chkUnit9.setText("Zone of Control");

        chkUnit10.setText("Prerequisites");


        for (int i = 0; i < NUM_UNIT_ATTRIBUTES; i++)
        {
            unitSpinners[i].setPreferredSize(new java.awt.Dimension(SPNR_WIDTH, 20));
            unitSpinners[i].addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent evt) {
                    handleSpinnerEvent(evt, NUM_UNIT_ATTRIBUTES, unitSpinners, unitChecks, unitSpinnerValues, unitAttributes);
                }
            });
        }
        //add the sub-components

        pnlUnits.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 0;
        c.weighty = 0.04;
        //top, left, bottom, right
        c.insets = new Insets(2, 0, 2, 0);
        //Give some nonzero x weight to at least one check box
        c.weightx = 0.5;
        c.fill = GridBagConstraints.BOTH;
        pnlUnits.add(chkUnit0, c);
        c.gridy++;
        pnlUnits.add(chkUnit1, c);
        c.gridy++;
        pnlUnits.add(chkUnit2, c);
        c.gridy++;
        pnlUnits.add(chkUnit3, c);
        c.gridy++;
        pnlUnits.add(chkUnit4, c);
        c.gridy++;
        pnlUnits.add(chkUnit5, c);
        c.gridy++;
        pnlUnits.add(chkUnit6, c);
        c.gridy++;
        pnlUnits.add(chkUnit7, c);
        c.gridy++;
        pnlUnits.add(chkUnit8, c);
        c.gridy++;
        pnlUnits.add(chkUnit9, c);
        c.gridy++;
        pnlUnits.add(chkUnit10, c);
        for (int i = NUM_UNIT_ATTRIBUTES; i < 20; i++)
        {
            JLabel dummyPanel = new JLabel("");
            dummyPanel.setPreferredSize(new Dimension(1, 20));
            c.gridx = 0;
            c.gridy++;
            pnlUnits.add(dummyPanel, c);
        }

        //Now add the unit panel
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        c.gridheight = 2;
        c.weightx = 0.5;
        c.weighty = 0.1;
        c.fill = GridBagConstraints.BOTH;
        getContentPane().add(pnlUnits, c);

        /**
         * THE OTHER PANELS
         */
        
        //BUILDINGS
        
        for (int i = 0; i < NUM_BLDG_ATTRIBUTES; i++)
        {
            buildingSpinners[i].setPreferredSize(new java.awt.Dimension(SPNR_WIDTH, 20));
            buildingSpinners[i].addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent evt) {
                    handleSpinnerEvent(evt, NUM_BLDG_ATTRIBUTES, buildingSpinners, buildingChecks, buildingSpinnerValues, buildingAttributes);
                }
            });
        }
        pnlBuildings.setLayout(new GridBagLayout());
        GridBagConstraints b = new GridBagConstraints();
        b.gridx = 0;
        b.gridy = 0;
        b.gridheight = 1;
        b.gridwidth = 1;
        b.weighty = 0.04;
        b.anchor = GridBagConstraints.LINE_START;
        //First we add the first check box, then the first spinner, etc.
        b.fill = GridBagConstraints.VERTICAL;
        b.insets = new Insets(0, 0, 0, 0);
        pnlBuildings.add(buildingSpinners[0], b);
        b.gridx++;
        b.fill = GridBagConstraints.BOTH;
        b.weightx = 0.5;
        pnlBuildings.add(buildingChecks[0], b);
        b.weightx = 0;
        for (int i = 1; i < NUM_BLDG_ATTRIBUTES; i++)
        {
            b.gridx--;
            b.gridy++;
            b.fill = GridBagConstraints.VERTICAL;
            pnlBuildings.add(buildingSpinners[i], b);
            b.gridx++;
            b.fill = GridBagConstraints.BOTH;
            pnlBuildings.add(buildingChecks[i], b);
        }




        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 1;
        c.gridheight = 2;
        c.fill = GridBagConstraints.BOTH;
        pnlBuildings.setBorder(javax.swing.BorderFactory.createTitledBorder("Buildings"));
        getContentPane().add(pnlBuildings, c);

        //TECHNOLOGIES

        for (int i = 0; i < NUM_TECH_ATTRIBUTES; i++)
        {
            techSpinners[i].setPreferredSize(new java.awt.Dimension(SPNR_WIDTH, 20));
            techSpinners[i].addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent evt) {
                    handleSpinnerEvent(evt, NUM_TECH_ATTRIBUTES, techSpinners, techChecks, techSpinnerValues, techAttributes);
                }
            });
        }
        pnlTechnologies.setLayout(new GridBagLayout());
        GridBagConstraints t = new GridBagConstraints();
        t.gridx = 0;
        t.gridy = 0;
        t.gridheight = 1;
        t.gridwidth = 1;
        t.weighty = 0.04;
        t.anchor = GridBagConstraints.FIRST_LINE_START;
        //First we add the first check box, then the first spinner, etc.
        t.fill = GridBagConstraints.VERTICAL;
        t.insets = new Insets(2, 0, 2, 0);
        pnlTechnologies.add(techSpinners[0], t);
        t.gridx++;
        t.fill = GridBagConstraints.BOTH;
        t.weightx = 0.5;
        pnlTechnologies.add(techChecks[0], t);
        t.weightx = 0;
        for (int i = 1; i < NUM_TECH_ATTRIBUTES; i++)
        {
            t.gridx--;
            t.gridy++;
            t.fill = GridBagConstraints.VERTICAL;
            pnlTechnologies.add(techSpinners[i], t);
            t.gridx++;
            t.fill = GridBagConstraints.BOTH;
            pnlTechnologies.add(techChecks[i], t);
        }
        for (int i = NUM_TECH_ATTRIBUTES; i < 20; i++)
        {
            JLabel dummyPanel = new JLabel("");
            dummyPanel.setPreferredSize(new Dimension(1, 20));
            //dummyPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
            t.gridx = 0;
            t.gridy++;
            pnlTechnologies.add(dummyPanel, t);
        }


        c.gridx = 2;
        c.gridy = 3;
        c.gridwidth = 1;
        c.gridheight = 2;
        c.fill = GridBagConstraints.BOTH;
        pnlTechnologies.setBorder(javax.swing.BorderFactory.createTitledBorder("Technologies"));
        getContentPane().add(pnlTechnologies, c);

        //RESOURCES

        for (int i = 0; i < NUM_RESOURCE_ATTRIBUTES; i++)
        {
            resourceSpinners[i].setPreferredSize(new java.awt.Dimension(SPNR_WIDTH, 20));
            resourceSpinners[i].addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent evt) {
                    handleSpinnerEvent(evt, NUM_RESOURCE_ATTRIBUTES, resourceSpinners, resourceChecks, resourceSpinnerValues, resourceAttributes);
                }
            });
        }
        pnlResources.setLayout(new GridBagLayout());
        GridBagConstraints r = new GridBagConstraints();
        r.gridx = 0;
        r.gridy = 0;
        r.gridheight = 1;
        r.gridwidth = 1;
        r.weighty = 0.04;
        r.anchor = GridBagConstraints.FIRST_LINE_START;
        //First we add the first check box, then the first spinner, etc.
        r.fill = GridBagConstraints.VERTICAL;
        r.insets = new Insets(2, 0, 2, 0);
        pnlResources.add(resourceSpinners[0], r);
        r.gridx++;
        r.fill = GridBagConstraints.BOTH;
        r.weightx = 0.5;
        pnlResources.add(resourceChecks[0], r);
        r.weightx = 0;
        for (int i = 1; i < NUM_RESOURCE_ATTRIBUTES; i++)
        {
            r.gridx--;
            r.gridy++;
            r.fill = GridBagConstraints.VERTICAL;
            pnlResources.add(resourceSpinners[i], r);
            r.gridx++;
            r.fill = GridBagConstraints.BOTH;
            pnlResources.add(resourceChecks[i], r);
        }
        for (int i = NUM_RESOURCE_ATTRIBUTES; i < 20; i++)
        {
            JLabel dummyPanel = new JLabel("");
            dummyPanel.setPreferredSize(new Dimension(1, 20));
            r.gridx = 0;
            r.gridy++;
            pnlResources.add(dummyPanel, r);
        }

        c.gridx = 3;
        c.gridy = 3;
        c.gridwidth = 1;
        c.gridheight = 2;
        c.fill = GridBagConstraints.BOTH;
        pnlResources.setBorder(javax.swing.BorderFactory.createTitledBorder("(Possible) Resources"));
        getContentPane().add(pnlResources, c);

        //And now the buttons

        btnImport.setText("Import");
        btnImport.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                int errorOnStage = 0;   //0 = good
                JFileChooser chooser = Main.jfcCSVChooser;
                if (chkIncludeUnits.isSelected())
                {
                    chooser.setDialogTitle("Select the import file for the unit spreadsheet");
                    int rtrnVal = chooser.showSaveDialog(null);
                    if (rtrnVal != JFileChooser.APPROVE_OPTION)
                        return;
                    File file = chooser.getSelectedFile();
                    importUnitStatsFromCSV(file, biqFile.unit);
                }   //end units section
                if (chkIncludeBuildings.isSelected())
                {
                    chooser.setDialogTitle("Select the output file for the building spreadsheet");
                    int rtrnVal = chooser.showSaveDialog(null);
                    if (rtrnVal != JFileChooser.APPROVE_OPTION)
                        return;
                    File file = chooser.getSelectedFile();
                    if (!file.getPath().endsWith(".csv")) {
                        file = new File(file.getPath() + ".csv");
                    }
                    if (file.exists())
                    {
                        int response = JOptionPane.showOptionDialog(null, "File already exists.  Overwrite?", "Existing File", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                        if (response != JOptionPane.YES_OPTION)
                            return;
                    }
                    try{
                        CSVWriter writer = new CSVWriter(new BufferedOutputStream(new FileOutputStream(file)));
                        //First output the header row
                        for (int i = 0; i < NUM_BLDG_ATTRIBUTES; i++)
                        {
                            if (buildingAttributes[i].equals("Building Name") && buildingChecks[i].isSelected())
                                writer.write("Building Name");
                            else if (buildingAttributes[i].equals("Type of Building") && buildingChecks[i].isSelected())
                                writer.write("Type of Building");
                            else if (buildingAttributes[i].equals("Cost") && buildingChecks[i].isSelected())
                                writer.write("Cost");
                            else if (buildingAttributes[i].equals("Maintenance") && buildingChecks[i].isSelected())
                                writer.write("Maintenance");
                            else if (buildingAttributes[i].equals("Pollution") && buildingChecks[i].isSelected())
                                writer.write("Pollution");
                            else if (buildingAttributes[i].equals("Culture") && buildingChecks[i].isSelected())
                                writer.write("Culture");
                            else if (buildingAttributes[i].equals("Production Bonus") && buildingChecks[i].isSelected())
                                writer.write("Production Bonus");
                            else if (buildingAttributes[i].equals("Autoproduces") && buildingChecks[i].isSelected())
                                writer.write("Autoproduces");
                            else if (buildingAttributes[i].equals("Era") && buildingChecks[i].isSelected())
                                writer.write("Era");
                            else if (buildingAttributes[i].equals("Required Technology") && buildingChecks[i].isSelected())
                                writer.write("Required Technology");
                            else if (buildingAttributes[i].equals("Required Resources") && buildingChecks[i].isSelected())
                                writer.write("Required Resources");
                            else if (buildingAttributes[i].equals("Required Government") && buildingChecks[i].isSelected())
                                writer.write("Required Government");
                            else if (buildingAttributes[i].equals("Required Building") && buildingChecks[i].isSelected())
                                writer.write("Required Building");
                            else if (buildingAttributes[i].equals("Happiness Bonus (city)") && buildingChecks[i].isSelected())
                                writer.write("Happiness Bonus (city)");
                            else if (buildingAttributes[i].equals("Happiness Bonus (empire)") && buildingChecks[i].isSelected())
                                writer.write("Happiness Bonus (empire)");
                            else if (buildingAttributes[i].equals("Unhappiness (city)") && buildingChecks[i].isSelected())
                                writer.write("Unhappiness (city)");
                            else if (buildingAttributes[i].equals("Unhappiness (empire)") && buildingChecks[i].isSelected())
                                writer.write("Unhappiness (empire)");
                            else if (buildingAttributes[i].equals("Air Attack") && buildingChecks[i].isSelected())
                                writer.write("Air Attack");
                            else if (buildingAttributes[i].equals("Defence Bonus") && buildingChecks[i].isSelected())
                                writer.write("Defence Bonus");
                            else if (buildingAttributes[i].equals("Available To") && buildingChecks[i].isSelected())
                                ; //writer.write("Available To"); not yet implemented
                            //TODO: Add in required techs
                        }   //end attribute loop
                        writer.endRecord();
                        //TODO: output building stuff
                        //Now we need to figure out in which order we output these record
                        for (int n = 0; n < biqFile.buildings.size(); n++)
                        {
                            for (int i = 0; i < NUM_BLDG_ATTRIBUTES; i++)
                            {
                                BLDG building = biqFile.buildings.get(n);
                                if (buildingAttributes[i].equals("Building Name") && buildingChecks[i].isSelected())
                                    writer.write(building.getName());
                                else if (buildingAttributes[i].equals("Type of Building") && buildingChecks[i].isSelected())
                                {
                                    if (building.isWonder())
                                        writer.write("Wonder");
                                    else if (building.isSmallWonder())
                                        writer.write("Small wonder");
                                    else
                                        writer.write("Improvement");
                                }
                                else if (buildingAttributes[i].equals("Cost") && buildingChecks[i].isSelected())
                                    writer.writeInteger(building.getCost());
                                else if (buildingAttributes[i].equals("Maintenance") && buildingChecks[i].isSelected())
                                    writer.writeInteger(building.getMaintenanceCost());
                                else if (buildingAttributes[i].equals("Pollution") && buildingChecks[i].isSelected())
                                    writer.writeInteger(building.getPollution());
                                else if (buildingAttributes[i].equals("Culture") && buildingChecks[i].isSelected())
                                    writer.writeInteger(building.getCulture());
                                else if (buildingAttributes[i].equals("Production Bonus") && buildingChecks[i].isSelected())
                                    writer.writeInteger(building.getProduction() * 25);
                                else if (buildingAttributes[i].equals("Autoproduces") && buildingChecks[i].isSelected())
                                {
                                    if (building.getUnitProduced() == -1)
                                        writer.write(NONE);
                                    else
                                        writer.write(biqFile.unit.get(building.getUnitProduced()).getName());
                                }
                                else if (buildingAttributes[i].equals("Era") && buildingChecks[i].isSelected())
                                {
                                    //We need to know the era
                                    int era = -1;
                                    if (building.getReqAdvance() == -1)
                                        era = 0;
                                    else
                                        era = biqFile.technology.get(building.getReqAdvance()).getEra();
                                    if (era == -1)  //tech required no era
                                        era = 0;    //by default available in earliest era
                                    writer.write(biqFile.eras.get(era).getName());
                                }
                                else if (buildingAttributes[i].equals("Required Technology") && buildingChecks[i].isSelected())
                                {
                                    if (building.getReqAdvance() == -1)
                                        writer.write(NONE);
                                    else
                                        writer.write(biqFile.technology.get(building.getReqAdvance()).getName());
                                }
                                else if (buildingAttributes[i].equals("Required Resources") && buildingChecks[i].isSelected())
                                {
                                    if (building.getReqResource1() == -1 && building.getReqResource2() == -1)
                                        writer.write(NONE);
                                    else if(building.getReqResource2() == -1)
                                        writer.write(biqFile.resource.get(building.getReqResource1()).getName());
                                    else if(building.getReqResource1() == -1)
                                        writer.write(biqFile.resource.get(building.getReqResource2()).getName());
                                    else    //two required resources
                                    {
                                        String s = "";
                                        //Alphabetize
                                        if (biqFile.resource.get(building.getReqResource1()).getName().charAt(0) < biqFile.resource.get(building.getReqResource2()).getName().charAt(0))
                                            s = biqFile.resource.get(building.getReqResource1()).getName() + " and " + biqFile.resource.get(building.getReqResource2()).getName();
                                        else
                                            s = biqFile.resource.get(building.getReqResource2()).getName() + " and " + biqFile.resource.get(building.getReqResource1()).getName();
                                        writer.write(s);
                                    }
                                }
                                else if (buildingAttributes[i].equals("Required Government") && buildingChecks[i].isSelected())
                                    if (building.getReqGovernment() == -1)
                                        writer.write(NONE);
                                    else
                                        writer.write(biqFile.government.get(building.getReqGovernment()).name);
                                else if (buildingAttributes[i].equals("Required Building") && buildingChecks[i].isSelected())
                                    if (building.getReqImprovement() == -1)
                                        writer.write(NONE);
                                    else
                                        writer.write(biqFile.buildings.get(building.getReqImprovement()).getName());
                                else if (buildingAttributes[i].equals("Happiness Bonus (city)") && buildingChecks[i].isSelected())
                                    writer.writeInteger(building.getHappy());
                                else if (buildingAttributes[i].equals("Happiness Bonus (empire)") && buildingChecks[i].isSelected())
                                    writer.writeInteger(building.getHappyAll());
                                else if (buildingAttributes[i].equals("Unhappiness (city)") && buildingChecks[i].isSelected())
                                    writer.writeInteger(building.getUnhappy());
                                else if (buildingAttributes[i].equals("Unhappiness (empire)") && buildingChecks[i].isSelected())
                                    writer.writeInteger(building.getUnhappyAll());
                                else if (buildingAttributes[i].equals("Air Attack") && buildingChecks[i].isSelected())
                                    writer.writeInteger(building.getAirPower());
                                else if (buildingAttributes[i].equals("Defence Bonus") && buildingChecks[i].isSelected())
                                    writer.writeInteger(building.getDefenceBonus());
                                else if (buildingAttributes[i].equals("Available To") && buildingChecks[i].isSelected())
                                    ; //writer.write("Available To");   //not yet implemented
                                //TODO: Add in required techs
                            }   //end attribute loop
                            writer.endRecord();
                        }   //end unit loop
                        try{
                            writer.close();
                        }
                        catch(IOException f)
                        {
                            System.err.println("Unable to close file");
                            errorOnStage = 2;
                            JOptionPane.showMessageDialog(null, "Failed to export file for buildings");
                        }
                    }
                    catch(FileNotFoundException f){
                        System.err.println(f);
                        errorOnStage = 2;
                        JOptionPane.showMessageDialog(null, "Failed to export file for buildings");
                    }
                    catch(Exception g)  //to placate the compiler.
                    {
                        System.err.println(g);
                        g.printStackTrace();
                        errorOnStage = 2;
                        JOptionPane.showMessageDialog(null, "Failed to export file for buildings");
                    }
                }   //end building section
                if (chkIncludeTechnologies.isSelected())
                {
                    chooser.setDialogTitle("Select the output file for the technology spreadsheet");
                    int rtrnVal = chooser.showSaveDialog(null);
                    if (rtrnVal != JFileChooser.APPROVE_OPTION)
                        return;
                    File file = chooser.getSelectedFile();
                    if (!file.getPath().endsWith(".csv")) {
                        file = new File(file.getPath() + ".csv");
                    }
                    if (file.exists())
                    {
                        int response = JOptionPane.showOptionDialog(null, "File already exists.  Overwrite?", "Existing File", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                        if (response != JOptionPane.YES_OPTION)
                            return;
                    }
                    try{
                        CSVWriter writer = new CSVWriter(new BufferedOutputStream(new FileOutputStream(file)));
                        //First output the header row
                        for (int i = 0; i < NUM_TECH_ATTRIBUTES; i++)
                        {
                            if (techAttributes[i].equals("Tech Name") && techChecks[i].isSelected())
                                writer.write(techAttributes[i]);
                            else if (techAttributes[i].equals("Era") && techChecks[i].isSelected())
                                writer.write(techAttributes[i]);
                            else if (techAttributes[i].equals("Prerequisites") && techChecks[i].isSelected())
                                writer.write(techAttributes[i]);
                            else if (techAttributes[i].equals("Cost") && techChecks[i].isSelected())
                                writer.write(techAttributes[i]);
                            else if (techAttributes[i].equals("X position") && techChecks[i].isSelected())
                                writer.write(techAttributes[i]);
                            else if (techAttributes[i].equals("Y position") && techChecks[i].isSelected())
                                writer.write(techAttributes[i]);
                            //Available to, possibly
                            //TODO: Add in required techs
                        }   //end attribute loop
                        writer.endRecord();
                        //TODO: output building stuff
                        //Now we need to figure out in which order we output these record
                        for (int n = 0; n < biqFile.technology.size(); n++)
                        {
                            for (int i = 0; i < NUM_TECH_ATTRIBUTES; i++)
                            {
                                TECH tech = biqFile.technology.get(n);

                                if (techAttributes[i].equals("Tech Name") && techChecks[i].isSelected())
                                    writer.write(tech.getName());
                                else if (techAttributes[i].equals("Era") && techChecks[i].isSelected())
                                    writer.write(biqFile.eras.get(tech.getEra()).getName());
                                else if (techAttributes[i].equals("Prerequisites") && techChecks[i].isSelected())
                                {
                                    //Assume that prereqs are done in order - won't have
                                    //prereq 4 w/o 1.
                                    String s = "";
                                    if (tech.getPrerequisite1() == -1)
                                        writer.write(NONE);
                                    else
                                    {
                                        s = s + biqFile.technology.get(tech.getPrerequisite1()).getName();
                                        if (tech.getPrerequisite2() == -1)
                                            writer.write(s);
                                        else
                                        {
                                            s = s + " and " + biqFile.technology.get(tech.getPrerequisite2()).getName();
                                            if (tech.getPrerequisite3() == -1)
                                                writer.write(s);
                                            else
                                            {
                                                s = s + " and " + biqFile.technology.get(tech.getPrerequisite3()).getName();
                                                if (tech.getPrerequisite4() == -1)
                                                    writer.write(s);
                                                else
                                                {
                                                    s = s + " and " + biqFile.technology.get(tech.getPrerequisite4()).getName();
                                                    writer.write(s);
                                                }
                                            }
                                        }
                                    }
                                }
                                else if (techAttributes[i].equals("Cost") && techChecks[i].isSelected())
                                    writer.writeInteger(tech.getCost());
                                else if (techAttributes[i].equals("X position") && techChecks[i].isSelected())
                                    writer.writeInteger(tech.getX());
                                else if (techAttributes[i].equals("Y position") && techChecks[i].isSelected())
                                    writer.writeInteger(tech.getY());
                                //TODO: Add in required techs
                            }   //end attribute loop
                            writer.endRecord();
                        }   //end unit loop
                        try{
                            writer.close();
                        }
                        catch(IOException f)
                        {
                            System.err.println("Unable to close file");
                            errorOnStage = 3;
                            JOptionPane.showMessageDialog(null, "Failed to export file for technologies");
                        }
                    }
                    catch(FileNotFoundException f){
                        System.err.println(f);
                        errorOnStage = 3;
                        JOptionPane.showMessageDialog(null, "Failed to export file for technologies");
                    }
                    catch(Exception g)  //to placate the compiler.
                    {
                        System.err.println(g);
                        g.printStackTrace();
                        errorOnStage = 3;
                        JOptionPane.showMessageDialog(null, "Failed to export file for technologies");
                    }
                }   //end technology section
                if (chkIncludeResources.isSelected())
                {
                    chooser.setDialogTitle("Select the output file for the resource spreadsheet");
                    int rtrnVal = chooser.showSaveDialog(null);
                    if (rtrnVal != JFileChooser.APPROVE_OPTION)
                        return;
                    File file = chooser.getSelectedFile();
                    if (!file.getPath().endsWith(".csv")) {
                        file = new File(file.getPath() + ".csv");
                    }
                    if (file.exists())
                    {
                        int response = JOptionPane.showOptionDialog(null, "File already exists.  Overwrite?", "Existing File", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                        if (response != JOptionPane.YES_OPTION)
                            return;
                    }
                    try{
                        CSVWriter writer = new CSVWriter(new BufferedOutputStream(new FileOutputStream(file)));
                        //First output the header row
                        for (int i = 0; i < NUM_RESOURCE_ATTRIBUTES; i++)
                        {
                            if (resourceAttributes[i].equals("Resource Name") && resourceChecks[i].isSelected())
                                writer.write(resourceAttributes[i]);
                            else if (resourceAttributes[i].equals("Type of resource") && resourceChecks[i].isSelected())
                                writer.write(resourceAttributes[i]);
                            else if (resourceAttributes[i].equals("Food bonus") && resourceChecks[i].isSelected())
                                writer.write(resourceAttributes[i]);
                            else if (resourceAttributes[i].equals("Shield bonus") && resourceChecks[i].isSelected())
                                writer.write(resourceAttributes[i]);
                            else if (resourceAttributes[i].equals("Commerce bonus") && resourceChecks[i].isSelected())
                                writer.write(resourceAttributes[i]);
                            else if (resourceAttributes[i].equals("Prerequisites") && resourceChecks[i].isSelected())
                                writer.write(resourceAttributes[i]);
                            //Available to, possibly
                            //TODO: Add in required techs
                        }   //end attribute loop
                        writer.endRecord();
                        //TODO: output building stuff
                        //Now we need to figure out in which order we output these record
                        for (int n = 0; n < biqFile.resource.size(); n++)
                        {
                            for (int i = 0; i < NUM_RESOURCE_ATTRIBUTES; i++)
                            {
                                GOOD good = biqFile.resource.get(n);

                                if (resourceAttributes[i].equals("Resource Name") && resourceChecks[i].isSelected())
                                    writer.write(good.getName());
                                else if (resourceAttributes[i].equals("Type of resource") && resourceChecks[i].isSelected())
                                {
                                    if (good.getType() == 2)
                                        writer.write("Strategic");
                                    else if (good.getType() == 1)
                                        writer.write("Luxury");
                                    else
                                        writer.write("Bonus");
                                }
                                else if (resourceAttributes[i].equals("Food bonus") && resourceChecks[i].isSelected())
                                {
                                    writer.writeInteger(good.getFoodBonus());
                                }
                                else if (resourceAttributes[i].equals("Shield bonus") && resourceChecks[i].isSelected())
                                    writer.writeInteger(good.getShieldsBonus());
                                else if (resourceAttributes[i].equals("Commerce bonus") && resourceChecks[i].isSelected())
                                    writer.writeInteger(good.getCommerceBonus());
                                else if (resourceAttributes[i].equals("Prerequisites") && resourceChecks[i].isSelected())
                                {
                                    if (good.getPrerequisite() == -1)
                                        writer.write(NONE);
                                    else
                                        writer.write(biqFile.technology.get(good.getPrerequisite()).getName());
                                }
                                //TODO: Add in required techs
                            }   //end attribute loop
                            writer.endRecord();
                        }   //end unit loop
                        try{
                            writer.close();
                        }
                        catch(IOException f)
                        {
                            logger.error("Unable to close file", f);
                            errorOnStage = 4;
                            JOptionPane.showMessageDialog(null, "Failed to export file for resources");
                        }
                    }
                    catch(FileNotFoundException f){
                        logger.error("File not found exception", f);
                        errorOnStage = 4;
                        JOptionPane.showMessageDialog(null, "Failed to export file for resources");
                    }
                    catch(Exception g)  //to placate the compiler.
                    {
                        logger.error("Failed to expert file for resources", g);
                        errorOnStage = 4;
                        JOptionPane.showMessageDialog(null, "Failed to export file for resources");
                    }
                }   //end resource section
                //Let them know it finished, successfully or not
                if (errorOnStage == 0)
                    JOptionPane.showMessageDialog(null, "Successfully imported data from all CSV files");
            }
        });
        c.gridx = 3;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.NONE;
        getContentPane().add(btnImport, c);
        c.gridy++;
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    thisFrame.setVisible(false);
                }
            });
        getContentPane().add(btnCancel, c);

        //Don't forget the other check boxes
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weighty = 0;
        chkIncludeUnits.setText("Include Units");
        chkIncludeUnits.setSelected(true);
        getContentPane().add(chkIncludeUnits, c);

        c.gridx++;
        chkIncludeBuildings.setText("Include Buildings");
        chkIncludeBuildings.setSelected(false);
        chkIncludeBuildings.setEnabled(false);
        getContentPane().add(chkIncludeBuildings, c);

        c.gridx++;
        chkIncludeTechnologies.setText("Include Technologies");
        chkIncludeTechnologies.setSelected(false);
        chkIncludeTechnologies.setEnabled(false);
        getContentPane().add(chkIncludeTechnologies, c);

        c.gridx++;
        chkIncludeResources.setText("Include Resources");
        chkIncludeResources.setSelected(false);
        chkIncludeResources.setEnabled(false);
        getContentPane().add(chkIncludeResources, c);

/**




**/
        pack();
    }// </editor-fold>

    


    private void handleSpinnerEvent(ChangeEvent evt, final int MAX, JSpinner[]spinners, JCheckBox[]checkBoxes, int[]spinnerValues, String[]attributes)
    {
        if (logger.isDebugEnabled())
            logger.debug("Got spinner event");
        JSpinner source = (JSpinner)evt.getSource();
        int sourceSpinnerID = -1;
        for (int i = 0; i < MAX; i++)
        {
            if (spinners[i] == source)
            {
                sourceSpinnerID = i;
                break;
            }
        }
        if (logger.isDebugEnabled())
            logger.debug("Source spinner ID: " + sourceSpinnerID);
        //First case - they hit the up button
        if ((Integer)spinners[sourceSpinnerID].getValue() > spinnerValues[sourceSpinnerID])
        {
            //Update the value no matter what
            spinnerValues[sourceSpinnerID] = (Integer)spinners[sourceSpinnerID].getValue();
            if (sourceSpinnerID > 0)  //make sure it isn't already at the top
            {
                //Move it up
                String temp = attributes[sourceSpinnerID - 1];
                boolean checked = checkBoxes[sourceSpinnerID - 1].isSelected();
                checkBoxes[sourceSpinnerID - 1].setSelected(checkBoxes[sourceSpinnerID].isSelected());
                checkBoxes[sourceSpinnerID].setSelected(checked);
                attributes[sourceSpinnerID - 1] = attributes[sourceSpinnerID];
                attributes[sourceSpinnerID] = temp;
                checkBoxes[sourceSpinnerID - 1].setText(attributes[sourceSpinnerID - 1]);
                checkBoxes[sourceSpinnerID].setText(attributes[sourceSpinnerID]);
            }
        }
        else    //move it down
        {
            //Update the value no matter what
            spinnerValues[sourceSpinnerID] = (Integer)spinners[sourceSpinnerID].getValue();
            if (sourceSpinnerID < MAX - 1)  //make sure it isn't the furthest down
            {
                //Move it down (higher # = farther down)
                String temp = attributes[sourceSpinnerID + 1];
                boolean checked = checkBoxes[sourceSpinnerID + 1].isSelected();
                checkBoxes[sourceSpinnerID + 1].setSelected(checkBoxes[sourceSpinnerID].isSelected());
                checkBoxes[sourceSpinnerID].setSelected(checked);
                attributes[sourceSpinnerID + 1] = attributes[sourceSpinnerID];
                attributes[sourceSpinnerID] = temp;
                checkBoxes[sourceSpinnerID + 1].setText(attributes[sourceSpinnerID + 1]);
                checkBoxes[sourceSpinnerID].setText(attributes[sourceSpinnerID]);
            }

        }

        int newVal = (Integer)(((JSpinner)evt.getSource()).getValue());
    }
    /**
    * @param args the command line arguments
    */
    public static void main(String[]args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CSVImportPanel().setVisible(true);
            }
        });
    }

    // Variables declaration - do modify
    private javax.swing.JCheckBox chkUnit0;
    private javax.swing.JCheckBox chkUnit1;
    private javax.swing.JCheckBox chkUnit10;
    private javax.swing.JCheckBox chkUnit2;
    private javax.swing.JCheckBox chkUnit3;
    private javax.swing.JCheckBox chkUnit4;
    private javax.swing.JCheckBox chkUnit5;
    private javax.swing.JCheckBox chkUnit6;
    private javax.swing.JCheckBox chkUnit7;
    private javax.swing.JCheckBox chkUnit8;
    private javax.swing.JCheckBox chkUnit9;
    private javax.swing.JButton btnImport;
    private javax.swing.JButton btnCancel;
    private javax.swing.JCheckBox chkIncludeUnits;
    private javax.swing.JCheckBox chkIncludeBuildings;
    private javax.swing.JCheckBox chkIncludeTechnologies;
    private javax.swing.JCheckBox chkIncludeResources;
    private javax.swing.JPanel pnlUnits;
    private javax.swing.JPanel pnlBuildings;
    private javax.swing.JPanel pnlTechnologies;
    private javax.swing.JPanel pnlResources;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextArea txtDescription;
    private javax.swing.JSpinner spnrUnit0;
    private javax.swing.JSpinner spnrUnit1;
    private javax.swing.JSpinner spnrUnit10;
    private javax.swing.JSpinner spnrUnit2;
    private javax.swing.JSpinner spnrUnit3;
    private javax.swing.JSpinner spnrUnit4;
    private javax.swing.JSpinner spnrUnit5;
    private javax.swing.JSpinner spnrUnit6;
    private javax.swing.JSpinner spnrUnit7;
    private javax.swing.JSpinner spnrUnit8;
    private javax.swing.JSpinner spnrUnit9;
    // End of variables declaration
    
    private void importUnitStatsFromCSV(File file, List<PRTO> units) {
        try {
            Scanner scanner = new Scanner(file);
            List<List<String>> spreadsheet = CSVReader.readCSVFile(scanner);
            
            //Stuff to match:
            /**
             * Unit Name
             * Attack
             * Defence
             * Movement
             * Hitpoint Bonus
             * Bombard Strength
             * Range
             * Rate of Fire
             * Cost
             * 
             * But not, for now:
             * Zone of Control
             * Prerequisites
             */
            List<String> headerRow = spreadsheet.get(0);
            
            Map<String, Integer> indexMap = new HashMap<String, Integer>();
            for (int i = 0; i < headerRow.size(); i++) {
                String text = headerRow.get(i);
                if (text.equalsIgnoreCase("Unit Name")) {
                    indexMap.put("Unit Name", i);
                }
                else if (text.equalsIgnoreCase("Attack")) {
                    indexMap.put("Attack", i);
                }
                else if (text.equalsIgnoreCase("Defence")) {
                    indexMap.put("Defence", i);
                }
                else if (text.equalsIgnoreCase("Movement")) {
                    indexMap.put("Movement", i);
                }
                else if (text.equalsIgnoreCase("Hitpoint Bonus")) {
                    indexMap.put("Hitpoint Bonus", i);
                }
                else if (text.equalsIgnoreCase("Bombard Strength")) {
                    indexMap.put("Bombard Strength", i);
                }
                else if (text.equalsIgnoreCase("Range")) {
                    indexMap.put("Range", i);
                }
                else if (text.equalsIgnoreCase("Rate of Fire")) {
                    indexMap.put("Rate of Fire", i);
                }
                else if (text.equalsIgnoreCase("Cost")) {
                    indexMap.put("Cost", i);
                }
            }
            
            //Now read in the rest.  Match 'em up with units, and update the stats as needed.
            
            for (int i = 1; i < spreadsheet.size(); i++) {
                List<String> rowData = spreadsheet.get(i);
                
                String unitName = rowData.get(indexMap.get("Unit Name"));
                
                //Find the unit with that name.  Todo: Optimize this.
                PRTO unit = null;
                for (int u = 0; u < units.size(); u++) {
                    if (units.get(u).getName().equals(unitName)) {
                        unit = units.get(u);
                        break;
                    }
                }
                
                //If we didn't find the unit, go to the next row
                if (unit == null) {
                    continue;
                }
                
                //Now update stuff
                try {
                    Integer attack = Integer.parseInt(rowData.get(indexMap.get("Attack")));
                    unit.setAttack(attack);
                }
                catch(NumberFormatException ex) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Could not update attack for " + unit.getName());
                    }
                }
                
                try {
                    Integer defence = Integer.parseInt(rowData.get(indexMap.get("Defence")));
                    unit.setDefence(defence);
                }
                catch(NumberFormatException ex) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Could not update defence for " + unit.getName());
                    }
                }
                
                try {
                    Integer movement = Integer.parseInt(rowData.get(indexMap.get("Movement")));
                    unit.setMovement(movement);
                }
                catch(NumberFormatException ex) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Could not update movement for " + unit.getName());
                    }
                }
                
                try {
                    Integer hitpointBonus = Integer.parseInt(rowData.get(indexMap.get("Hitpoint Bonus")));
                    unit.setHitPointBonus(hitpointBonus);
                }
                catch(NumberFormatException ex) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Could not update hitpoint bonus for " + unit.getName());
                    }
                }
                
                try {
                    Integer bombardStrength = Integer.parseInt(rowData.get(indexMap.get("Bombard Strength")));
                    unit.setBombardStrength(bombardStrength);
                }
                catch(NumberFormatException ex) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Could not update bombard strength for " + unit.getName());
                    }
                }
                
                try {
                    Integer range = Integer.parseInt(rowData.get(indexMap.get("Range")));
                    unit.setBombardRange(range);
                }
                catch(NumberFormatException ex) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Could not update range for " + unit.getName());
                    }
                }
                
                try {
                    Integer rateOfFire = Integer.parseInt(rowData.get(indexMap.get("Rate of Fire")));
                    unit.setRateOfFire(rateOfFire);
                }
                catch(NumberFormatException ex) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Could not update rate of fire for " + unit.getName());
                    }
                }
                
                try {
                    Integer cost = Integer.parseInt(rowData.get(indexMap.get("Cost")));
                    unit.setShieldCost(cost);
                }
                catch(NumberFormatException ex) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Could not update cost for " + unit.getName());
                    }
                }
            }
        }
        catch(FileNotFoundException ex) {
            logger.error("Import file not found", ex);
        }
    }

}
