package com.civfanatics.civ3.xplatformeditor.tabs.biqc;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Quintillus
 */
import com.civfanatics.civ3.biqFile.IO;
import com.civfanatics.civ3.biqFile.Section;
import com.civfanatics.civ3.biqFile.BIQSection;
import com.civfanatics.civ3.xplatformeditor.FileIO;
import com.civfanatics.civ3.xplatformeditor.Main;
import java.awt.FileDialog;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 * This class represents a tab that can be used for comparing two BIQ files.  Rather
 * than having to manually compare the BIQ files, you can use this tab to get a
 * text-based report on all the differences.  It allows comparison based on only
 * some sections of the BIQ, or all sections.
 *
 * @author Andrew
 */
public class BIQCTab
{
    String dneInFirst = "DNE in first";
    String dneInSecond = "DNE in second";
    String ioException = "IOException";
    String finishDNESecond = "finished DNE in second";
    String dneIn = " does not exist in ";
    String writingToOutput = "writing to output";
    String fileSeparator = " | ";
    String fileOne = "File 1: ";
    String fileTwo = "File 2: ";
    String newline = "line.separator";
    String lineReturn = java.lang.System.getProperty("line.separator");
    
    Logger logger = Logger.getLogger(this.getClass());
    public List<IO> biqFile;
    public List<checkBoxSettings> checkBoxSettings;        //parallel to biqFile
    int numSelected;
    int biqIndex;
    int biq2Index;
    String separator;
    JFileChooser jFileChooser1;
    JFrame main;    //the main window frame
    private javax.swing.JCheckBox chkAppendTxt;
    private javax.swing.JCheckBox chkBIQ;
    private javax.swing.JCheckBox chkBuildings;
    private javax.swing.JCheckBox chkCities;
    private javax.swing.JCheckBox chkCitizens;
    private javax.swing.JCheckBox chkCivilizations;
    private javax.swing.JCheckBox chkColonies;
    private javax.swing.JCheckBox chkCompareMode;
    private javax.swing.JCheckBox chkContinents;
    private javax.swing.JCheckBox chkCulture;
    private javax.swing.JCheckBox chkDifficulties;
    private javax.swing.JCheckBox chkEnglish;
    private javax.swing.JCheckBox chkEras;
    private javax.swing.JCheckBox chkEspionage;
    private javax.swing.JCheckBox chkExperience;
    private javax.swing.JCheckBox chkFile;
    private javax.swing.JCheckBox chkFlavors;
    private javax.swing.JCheckBox chkGAME;
    private javax.swing.JCheckBox chkGovernments;
    private javax.swing.JCheckBox chkMapUnits;
    private javax.swing.JCheckBox chkPlayerData;
    private javax.swing.JCheckBox chkRULE;
    private javax.swing.JCheckBox chkResources;
    private javax.swing.JCheckBox chkSaveChecks;
    private javax.swing.JCheckBox chkStartingLocations;
    private javax.swing.JCheckBox chkTechnologies;
    private javax.swing.JCheckBox chkTerrains;
    private javax.swing.JCheckBox chkTiles;
    private javax.swing.JCheckBox chkUnits;
    private javax.swing.JCheckBox chkWorkerJobs;
    private javax.swing.JCheckBox chkWorldCharacteristics;
    private javax.swing.JCheckBox chkWorldMap;
    private javax.swing.JCheckBox chkWorldSizes;
    private javax.swing.JComboBox cmbBIQ1;
    private javax.swing.JComboBox cmbBIQ2;
    private javax.swing.JButton cmdBrowse;
    private javax.swing.JButton cmdExport;
    private javax.swing.JButton cmdExportAll;
    private javax.swing.JButton cmdExportAll2;
    private javax.swing.JButton cmdExportNone;
    private javax.swing.JButton cmdExportNone2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JLabel lblSuccessfulOutput;
    private javax.swing.JTextField txtOutputFile;
    private javax.swing.JTextField txtSeparator;
    private javax.swing.JCheckBox chkMultiLetter;

    /**
     * The constructor.
     */
    public BIQCTab()
    {
        checkBoxSettings = new ArrayList<checkBoxSettings>();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        chkBuildings = new javax.swing.JCheckBox();
        chkCitizens = new javax.swing.JCheckBox();
        chkCulture = new javax.swing.JCheckBox();
        chkDifficulties = new javax.swing.JCheckBox();
        chkEras = new javax.swing.JCheckBox();
        chkEspionage = new javax.swing.JCheckBox();
        chkExperience = new javax.swing.JCheckBox();
        chkFlavors = new javax.swing.JCheckBox();
        chkResources = new javax.swing.JCheckBox();
        chkGovernments = new javax.swing.JCheckBox();
        chkUnits = new javax.swing.JCheckBox();
        chkCivilizations = new javax.swing.JCheckBox();
        chkRULE = new javax.swing.JCheckBox();
        chkTechnologies = new javax.swing.JCheckBox();
        chkTerrains = new javax.swing.JCheckBox();
        chkWorkerJobs = new javax.swing.JCheckBox();
        chkWorldSizes = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        cmdExportAll = new javax.swing.JButton();
        cmdExportNone = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        chkBIQ = new javax.swing.JCheckBox();
        chkEnglish = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        cmdExportNone2 = new javax.swing.JButton();
        cmdExportAll2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        chkCities = new javax.swing.JCheckBox();
        chkColonies = new javax.swing.JCheckBox();
        chkContinents = new javax.swing.JCheckBox();
        chkStartingLocations = new javax.swing.JCheckBox();
        chkTiles = new javax.swing.JCheckBox();
        chkMapUnits = new javax.swing.JCheckBox();
        chkWorldCharacteristics = new javax.swing.JCheckBox();
        chkWorldMap = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        cmbBIQ2 = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        chkCompareMode = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        cmbBIQ1 = new javax.swing.JComboBox();
        chkSaveChecks = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        txtSeparator = new javax.swing.JTextField();
        chkMultiLetter = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        chkFile = new javax.swing.JCheckBox();
        txtOutputFile = new javax.swing.JTextField();
        cmdBrowse = new javax.swing.JButton();
        cmdExport = new javax.swing.JButton();
        chkAppendTxt = new javax.swing.JCheckBox();
        lblSuccessfulOutput = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        chkPlayerData = new javax.swing.JCheckBox();
        chkGAME = new javax.swing.JCheckBox();


    }

    /**
     * Sets up the GUI of this tab.
     * 
     * @return - A link to this tab.
     */
    public JPanel createTab()
    {
        jPanel1.setMaximumSize(new java.awt.Dimension(1024, 768));
        jPanel1.setPreferredSize(new java.awt.Dimension(1024, 768));

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        chkBuildings.setText("Buildings");
        chkBuildings.setEnabled(false);
        chkBuildings.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkBuildingsActionPerformed(evt);
            }
        });

        chkCitizens.setText("Citizens");
        chkCitizens.setEnabled(false);
        chkCitizens.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkCitizensActionPerformed(evt);
            }
        });

        chkCulture.setText("Culture");
        chkCulture.setEnabled(false);
        chkCulture.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkCultureActionPerformed(evt);
            }
        });

        chkDifficulties.setText("Difficulty Levels");
        chkDifficulties.setEnabled(false);
        chkDifficulties.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkDifficultiesActionPerformed(evt);
            }
        });

        chkEras.setText("Eras");
        chkEras.setEnabled(false);
        chkEras.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkErasActionPerformed(evt);
            }
        });

        chkEspionage.setFont(chkEspionage.getFont());
        chkEspionage.setText("Espionage Missions");
        chkEspionage.setEnabled(false);
        chkEspionage.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkEspionageActionPerformed(evt);
            }
        });

        chkExperience.setFont(chkExperience.getFont());
        chkExperience.setText("Experience Levels");
        chkExperience.setEnabled(false);
        chkExperience.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkExperienceActionPerformed(evt);
            }
        });

        chkFlavors.setText("Flavors");
        chkFlavors.setEnabled(false);
        chkFlavors.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkFlavorsActionPerformed(evt);
            }
        });

        chkResources.setText("Resources");
        chkResources.setEnabled(false);
        chkResources.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkResourcesActionPerformed(evt);
            }
        });

        chkGovernments.setText("Governments");
        chkGovernments.setEnabled(false);
        chkGovernments.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkGovernmentsActionPerformed(evt);
            }
        });

        chkUnits.setText("Units");
        chkUnits.setEnabled(false);
        chkUnits.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkUnitsActionPerformed(evt);
            }
        });

        chkCivilizations.setText("Civilizations");
        chkCivilizations.setEnabled(false);
        chkCivilizations.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkCivilizationsActionPerformed(evt);
            }
        });

        chkRULE.setText("General Settings");
        chkRULE.setEnabled(false);
        chkRULE.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkRULEActionPerformed(evt);
            }
        });

        chkTechnologies.setText("Technologies");
        chkTechnologies.setEnabled(false);
        chkTechnologies.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkTechnologiesActionPerformed(evt);
            }
        });

        chkTerrains.setText("Terrains");
        chkTerrains.setEnabled(false);
        chkTerrains.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkTerrainsActionPerformed(evt);
            }
        });

        chkWorkerJobs.setText("Worker Jobs");
        chkWorkerJobs.setEnabled(false);
        chkWorkerJobs.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkWorkerJobsActionPerformed(evt);
            }
        });

        chkWorldSizes.setText("World Sizes");
        chkWorldSizes.setEnabled(false);
        chkWorldSizes.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkWorldSizesActionPerformed(evt);
            }
        });

        jLabel1.setText("Select rule sections to export");

        cmdExportAll.setText("All");
        cmdExportAll.setEnabled(false);
        cmdExportAll.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmdExportAllActionPerformed(evt);
            }
        });

        cmdExportNone.setText("None");
        cmdExportNone.setEnabled(false);
        cmdExportNone.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmdExportNoneActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().add(26, 26, 26).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false).add(jPanel2Layout.createSequentialGroup().add(cmdExportAll, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(cmdExportNone, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(org.jdesktop.layout.GroupLayout.LEADING, jLabel1))).add(jPanel2Layout.createSequentialGroup().addContainerGap().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(chkDifficulties).add(chkCulture).add(chkCivilizations).add(chkBuildings).add(chkCitizens).add(chkEras).add(chkEspionage).add(chkExperience).add(chkFlavors).add(chkRULE).add(chkGovernments).add(chkResources).add(chkTechnologies).add(chkTerrains).add(chkUnits).add(chkWorkerJobs).add(chkWorldSizes)))).addContainerGap(36, Short.MAX_VALUE)));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().addContainerGap().add(jLabel1).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(cmdExportAll).add(cmdExportNone)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkBuildings).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkCitizens).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkCivilizations).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkCulture).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkDifficulties).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkEras).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkEspionage).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkExperience).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkFlavors).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkRULE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkGovernments).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkResources).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkTechnologies).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkTerrains).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkUnits).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkWorkerJobs).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkWorldSizes).addContainerGap(126, Short.MAX_VALUE)));

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(jLabel2.getFont());
        jLabel2.setText("BIQ variables or English?");

        chkBIQ.setText("BIQ variables");
        chkBIQ.setToolTipText("Prints out the variables as they are stored in the BIQ.  \nFor example, the abilities a unit has, stored in binary\n in the BIQ, will be printed out as one integer that\n represents all of its abilities.");
        chkBIQ.setEnabled(false);

        chkEnglish.setText("English");
        chkEnglish.setToolTipText("Prints out the variables in comprehensible English. \n For example, the abilities a unit has will be printed\n out as displayed in the editor - whether it is wheeled\n or not, whether it's a foot soldier, etc.");
        chkEnglish.setEnabled(false);

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel3Layout.createSequentialGroup().addContainerGap().add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(chkEnglish).add(chkBIQ)).addContainerGap()));
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel3Layout.createSequentialGroup().addContainerGap().add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(chkBIQ).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(chkEnglish).addContainerGap(142, Short.MAX_VALUE)));

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setPreferredSize(new java.awt.Dimension(218, 308));

        cmdExportNone2.setText("None");
        cmdExportNone2.setEnabled(false);
        cmdExportNone2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmdExportNone2ActionPerformed(evt);
            }
        });

        cmdExportAll2.setText("All");
        cmdExportAll2.setEnabled(false);
        cmdExportAll2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmdExportAll2ActionPerformed(evt);
            }
        });

        jLabel4.setText("Select map sections to export");

        chkCities.setText("Cities");
        chkCities.setEnabled(false);
        chkCities.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkCitiesActionPerformed(evt);
            }
        });

        chkColonies.setText("Colonies");
        chkColonies.setEnabled(false);
        chkColonies.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkColoniesActionPerformed(evt);
            }
        });

        chkContinents.setText("Continents");
        chkContinents.setEnabled(false);
        chkContinents.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkContinentsActionPerformed(evt);
            }
        });

        chkStartingLocations.setText("Starting Locations");
        chkStartingLocations.setEnabled(false);
        chkStartingLocations.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkStartingLocationsActionPerformed(evt);
            }
        });

        chkTiles.setText("Tiles");
        chkTiles.setEnabled(false);
        chkTiles.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkTilesActionPerformed(evt);
            }
        });

        chkMapUnits.setText("Units");
        chkMapUnits.setEnabled(false);
        chkMapUnits.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkMapUnitsActionPerformed(evt);
            }
        });

        chkWorldCharacteristics.setText("World Characteristics");
        chkWorldCharacteristics.setEnabled(false);
        chkWorldCharacteristics.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkWorldCharacteristicsActionPerformed(evt);
            }
        });

        chkWorldMap.setText("World Map");
        chkWorldMap.setEnabled(false);
        chkWorldMap.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkWorldMapActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel4Layout.createSequentialGroup().add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel4Layout.createSequentialGroup().add(17, 17, 17).add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false).add(jPanel4Layout.createSequentialGroup().add(cmdExportAll2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(cmdExportNone2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(org.jdesktop.layout.GroupLayout.LEADING, jLabel4))).add(chkCities).add(chkColonies).add(chkContinents).add(chkStartingLocations).add(chkMapUnits).add(chkTiles).add(chkWorldCharacteristics).add(chkWorldMap)).addContainerGap(30, Short.MAX_VALUE)));
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel4Layout.createSequentialGroup().addContainerGap().add(jLabel4).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(cmdExportAll2).add(cmdExportNone2)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkCities).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkColonies).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkContinents).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkStartingLocations).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkTiles).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkMapUnits).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkWorldCharacteristics).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkWorldMap).addContainerGap(46, Short.MAX_VALUE)));

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        cmbBIQ2.setEnabled(false);
        cmbBIQ2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbBIQ2ActionPerformed(evt);
            }
        });

        jLabel6.setText("Compare to:");
        jLabel6.setEnabled(false);

        chkCompareMode.setText("Compare Mode");
        chkCompareMode.setEnabled(false);
        chkCompareMode.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkCompareModeActionPerformed(evt);
            }
        });

        jLabel5.setText("Current BIQ File:  ");

        cmbBIQ1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmbBIQ1ActionPerformed(evt);
            }
        });
        cmbBIQ1.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                cmbBIQ1PropertyChange(evt);
            }
        });

        chkSaveChecks.setSelected(true);
        chkSaveChecks.setText("Save check boxes when switching files");
        chkSaveChecks.setEnabled(false);
        jLabel8.setText("Separate with:");

        txtSeparator.setText("|");
        txtSeparator.setMinimumSize(new java.awt.Dimension(44, 20));
        txtSeparator.addKeyListener(new java.awt.event.KeyAdapter()
        {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt)
            {
                txtSeparatorKeyTyped(evt);
            }
        });

        chkMultiLetter.setText("Multi");

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel5Layout.createSequentialGroup().addContainerGap().add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel5Layout.createSequentialGroup().add(jLabel5).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 33, Short.MAX_VALUE).add(cmbBIQ1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 193, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel5Layout.createSequentialGroup().add(jLabel6).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 58, Short.MAX_VALUE).add(cmbBIQ2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 196, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(jPanel5Layout.createSequentialGroup().addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(chkCompareMode).add(2, 2, 2).add(jLabel8).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(txtSeparator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkMultiLetter)).add(jPanel5Layout.createSequentialGroup().add(12, 12, 12).add(chkSaveChecks).add(63, 63, 63))).addContainerGap()));
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel5Layout.createSequentialGroup().addContainerGap().add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel5).add(cmbBIQ1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(chkCompareMode).add(jLabel8).add(chkMultiLetter).add(txtSeparator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel6).add(cmbBIQ2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkSaveChecks).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder()));

        jLabel3.setText("Output");

        chkFile.setText("File");
        chkFile.setEnabled(false);

        txtOutputFile.setEnabled(false);

        cmdBrowse.setText("Browse");
        cmdBrowse.setEnabled(false);
        cmdBrowse.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmdBrowseActionPerformed(evt);
            }
        });

        cmdExport.setText("Export!");
        cmdExport.setEnabled(false);
        cmdExport.addMouseListener(new java.awt.event.MouseAdapter()
        {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                cmdExportMouseClicked(evt);
            }
        });
        cmdExport.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cmdExportActionPerformed(evt);
            }
        });

        chkAppendTxt.setSelected(true);
        chkAppendTxt.setText("Append .txt if not present");
        chkAppendTxt.setEnabled(false);

        lblSuccessfulOutput.setVisible(false);
        lblSuccessfulOutput.setText("Successful Output");
        lblSuccessfulOutput.addComponentListener(new java.awt.event.ComponentAdapter()
        {
            @Override
            public void componentShown(java.awt.event.ComponentEvent evt)
            {
                lblSuccessfulOutputComponentShown(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel6Layout.createSequentialGroup().add(jLabel3).addContainerGap(314, Short.MAX_VALUE)).add(jPanel6Layout.createSequentialGroup().addContainerGap().add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel6Layout.createSequentialGroup().add(cmdExport).addContainerGap()).add(jPanel6Layout.createSequentialGroup().add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel6Layout.createSequentialGroup().add(chkFile).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 81, Short.MAX_VALUE).add(txtOutputFile, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 193, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(jPanel6Layout.createSequentialGroup().add(21, 21, 21).add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(lblSuccessfulOutput).add(jPanel6Layout.createSequentialGroup().add(cmdBrowse).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 52, Short.MAX_VALUE).add(chkAppendTxt))))).add(26, 26, 26)))));
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel6Layout.createSequentialGroup().add(jLabel3).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(chkFile).add(txtOutputFile, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(cmdBrowse).add(chkAppendTxt)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(cmdExport).add(lblSuccessfulOutput)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel7.setText("Select other sections to export");

        chkPlayerData.setText("Player Data");
        chkPlayerData.setEnabled(false);
        chkPlayerData.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkPlayerDataActionPerformed(evt);
            }
        });

        chkGAME.setText("Additonal Scenario Properties");
        chkGAME.setEnabled(false);
        chkGAME.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                chkGAMEActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
                jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel7Layout.createSequentialGroup().addContainerGap().add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel7Layout.createSequentialGroup().add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(chkPlayerData).add(jLabel7)).addContainerGap()).add(jPanel7Layout.createSequentialGroup().add(chkGAME, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE).add(2, 2, 2)))));
        jPanel7Layout.setVerticalGroup(
                jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel7Layout.createSequentialGroup().addContainerGap().add(jLabel7).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkGAME).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(chkPlayerData).addContainerGap(27, Short.MAX_VALUE)));

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().addContainerGap().add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 163, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(jPanel6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap(29, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 630, Short.MAX_VALUE).add(jPanel1Layout.createSequentialGroup().addContainerGap().add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jPanel1Layout.createSequentialGroup().add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jPanel1Layout.createSequentialGroup().add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 310, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        return jPanel1;
    }

    /**
     * Sends links to data that this tab will need to complete its operations.
     *
     * @param biqFile - The array of BIQ files.
     * @param fileChooser - The file chooser dialog, which this tab will use to
     * allow the user to select their output file.
     * @param main - The link to the Main window.
     */
    public void sendData(List<IO> biqFile, JFileChooser fileChooser, JFrame main)
    {
        this.biqFile = biqFile;
        this.jFileChooser1 = fileChooser;
        this.main = main;
    }

    /**
     * Lets this tab know that a new tab has been opened.  This will cause it to
     * save the check boxes for the old file (if enabled), and add the new file
     * to its lists of files to compare.  If it is the first file to be opened,
     * all the actions will be enabled as well.
     * 
     * @param file - The new file that has been opened.
     */
    public void alertToNewlyOpenedFile(File file)
    {
        checkBoxSettings.add(new checkBoxSettings());
        //may require check that biqIndex isn't -1
        if (chkSaveChecks.isSelected())
        {
            //update
            //logger.trace("saving check boxes");
            saveCheckBoxes();
        }

        cmbBIQ1.addItem(file.getName());
        cmbBIQ1.setSelectedItem(file.getName());

        cmbBIQ2.addItem(file.getName());
        updateCheckBoxes();

        chkBIQ.setEnabled(true);
        txtOutputFile.setEnabled(true);
        cmdBrowse.setEnabled(true);
        //cmdExport.setEnabled(true);
        chkEnglish.setEnabled(true);
        chkFile.setEnabled(true);
        chkAppendTxt.setEnabled(true);
        chkCompareMode.setEnabled(true);
        //cmbBIQ2.setEnabled(true);
        jLabel6.setEnabled(true);
        chkSaveChecks.setEnabled(true);

        chkAppendTxt.setSelected(true);
        chkEnglish.setSelected(true);
        chkFile.setSelected(true);
    }

    private void chkBuildingsActionPerformed(java.awt.event.ActionEvent evt)
    {
        // :
        if (chkBuildings.isSelected())
        {
            cmdExport.setEnabled(true);
            numSelected++;
        }
        else
        {
            numSelected--;
            if (numSelected == 0)
            {
                cmdExport.setEnabled(false);
            }
        }
        //logger.trace(numSelected);
    }

    private void chkCitizensActionPerformed(java.awt.event.ActionEvent evt)
    {
        // :
        if (chkCitizens.isSelected())
        {
            cmdExport.setEnabled(true);
            numSelected++;
        }
        else
        {
            numSelected--;
            if (numSelected == 0)
            {
                cmdExport.setEnabled(false);
            }
        }
    }

    private void chkCultureActionPerformed(java.awt.event.ActionEvent evt)
    {
        // :
        if (chkCulture.isSelected())
        {
            cmdExport.setEnabled(true);
            numSelected++;
        }
        else
        {
            numSelected--;
            if (numSelected == 0)
            {
                cmdExport.setEnabled(false);
            }
        }
    }

    private void chkDifficultiesActionPerformed(java.awt.event.ActionEvent evt)
    {
        // :
        if (chkDifficulties.isSelected())
        {
            cmdExport.setEnabled(true);
            numSelected++;
        }
        else
        {
            numSelected--;
            if (numSelected == 0)
            {
                cmdExport.setEnabled(false);
            }
        }
    }

    private void chkErasActionPerformed(java.awt.event.ActionEvent evt)
    {
        // :
        if (chkEras.isSelected())
        {
            cmdExport.setEnabled(true);
            numSelected++;
        }
        else
        {
            numSelected--;
            if (numSelected == 0)
            {
                cmdExport.setEnabled(false);
            }
        }
    }

    private void chkEspionageActionPerformed(java.awt.event.ActionEvent evt)
    {

        if (chkEspionage.isSelected())
        {
            cmdExport.setEnabled(true);
            numSelected++;
        }
        else
        {
            numSelected--;
            if (numSelected == 0)
            {
                cmdExport.setEnabled(false);
            }
        }
    }

    private void chkExperienceActionPerformed(java.awt.event.ActionEvent evt)
    {
        // :
        if (chkExperience.isSelected())
        {
            cmdExport.setEnabled(true);
            numSelected++;
        }
        else
        {
            numSelected--;
            if (numSelected == 0)
            {
                cmdExport.setEnabled(false);
            }
        }
    }

    private void chkFlavorsActionPerformed(java.awt.event.ActionEvent evt)
    {
        // :
        if (chkFlavors.isSelected())
        {
            cmdExport.setEnabled(true);
            numSelected++;
        }
        else
        {
            numSelected--;
            if (numSelected == 0)
            {
                cmdExport.setEnabled(false);
            }
        }
    }

    private void chkResourcesActionPerformed(java.awt.event.ActionEvent evt)
    {
        // :
        if (chkResources.isSelected())
        {
            cmdExport.setEnabled(true);
            numSelected++;
        }
        else
        {
            numSelected--;
            if (numSelected == 0)
            {
                cmdExport.setEnabled(false);
            }
        }
    }

    private void chkGovernmentsActionPerformed(java.awt.event.ActionEvent evt)
    {
        // :
        if (chkGovernments.isSelected())
        {
            cmdExport.setEnabled(true);
            numSelected++;
        }
        else
        {
            numSelected--;
            if (numSelected == 0)
            {
                cmdExport.setEnabled(false);
            }
        }
    }

    private void chkUnitsActionPerformed(java.awt.event.ActionEvent evt)
    {
        // :
        if (chkUnits.isSelected())
        {
            cmdExport.setEnabled(true);
            numSelected++;
        }
        else
        {
            numSelected--;
            if (numSelected == 0)
            {
                cmdExport.setEnabled(false);
            }
        }
    }

    private void chkCivilizationsActionPerformed(java.awt.event.ActionEvent evt)
    {
        // :
        if (chkCivilizations.isSelected())
        {
            cmdExport.setEnabled(true);
            numSelected++;
        }
        else
        {
            numSelected--;
            if (numSelected == 0)
            {
                cmdExport.setEnabled(false);
            }
        }
    }

    private void chkRULEActionPerformed(java.awt.event.ActionEvent evt)
    {
        // :
        if (chkRULE.isSelected())
        {
            cmdExport.setEnabled(true);
            numSelected++;
        }
        else
        {
            numSelected--;
            if (numSelected == 0)
            {
                cmdExport.setEnabled(false);
            }
        }
    }

    private void chkTechnologiesActionPerformed(java.awt.event.ActionEvent evt)
    {
        // :
        if (chkTechnologies.isSelected())
        {
            cmdExport.setEnabled(true);
            numSelected++;
        }
        else
        {
            numSelected--;
            if (numSelected == 0)
            {
                cmdExport.setEnabled(false);
            }
        }
    }

    private void chkTerrainsActionPerformed(java.awt.event.ActionEvent evt)
    {
        // :
        if (chkTerrains.isSelected())
        {
            cmdExport.setEnabled(true);
            numSelected++;
        }
        else
        {
            numSelected--;
            if (numSelected == 0)
            {
                cmdExport.setEnabled(false);
            }
        }
    }

    private void chkWorkerJobsActionPerformed(java.awt.event.ActionEvent evt)
    {
        // :
        if (chkWorkerJobs.isSelected())
        {
            cmdExport.setEnabled(true);
            numSelected++;
        }
        else
        {
            numSelected--;
            if (numSelected == 0)
            {
                cmdExport.setEnabled(false);
            }
        }
    }

    private void chkWorldSizesActionPerformed(java.awt.event.ActionEvent evt)
    {
        // :
        if (chkWorldSizes.isSelected())
        {
            cmdExport.setEnabled(true);
            numSelected++;
        }
        else
        {
            numSelected--;
            if (numSelected == 0)
            {
                cmdExport.setEnabled(false);
            }
        }
    }

    private void cmdExportAllActionPerformed(java.awt.event.ActionEvent evt)
    {
        // :
        if (!(chkBuildings.isSelected()))
        {
            numSelected++;
        }
        if (!(chkCitizens.isSelected()))
        {
            numSelected++;
        }
        if (!(chkCivilizations.isSelected()))
        {
            numSelected++;
        }
        if (!(chkCulture.isSelected()))
        {
            numSelected++;
        }
        if (!(chkDifficulties.isSelected()))
        {
            numSelected++;
        }
        if (!(chkEras.isSelected()))
        {
            numSelected++;
        }
        if (!(chkEspionage.isSelected()))
        {
            numSelected++;
        }
        if (!(chkExperience.isSelected()))
        {
            numSelected++;
        }
        if (!(chkFlavors.isSelected()))
        {
            numSelected++;
        }
        if (!(chkGovernments.isSelected()))
        {
            numSelected++;
        }
        if (!(chkRULE.isSelected()))
        {
            numSelected++;
        }
        if (!(chkResources.isSelected()))
        {
            numSelected++;
        }
        if (!(chkTechnologies.isSelected()))
        {
            numSelected++;
        }
        if (!(chkTerrains.isSelected()))
        {
            numSelected++;
        }
        if (!(chkUnits.isSelected()))
        {
            numSelected++;
        }
        if (!(chkWorkerJobs.isSelected()))
        {
            numSelected++;
        }
        if (!(chkWorldSizes.isSelected()))
        {
            numSelected++;
        }
        chkBuildings.setSelected(true);
        chkCitizens.setSelected(true);
        chkCivilizations.setSelected(true);
        chkCulture.setSelected(true);
        chkDifficulties.setSelected(true);
        chkEras.setSelected(true);
        chkEspionage.setSelected(true);
        chkExperience.setSelected(true);
        chkFlavors.setSelected(true);
        chkGovernments.setSelected(true);
        chkRULE.setSelected(true);
        chkResources.setSelected(true);
        chkTechnologies.setSelected(true);
        chkTerrains.setSelected(true);
        chkUnits.setSelected(true);
        chkWorkerJobs.setSelected(true);
        chkWorldSizes.setSelected(true);
        cmdExport.setEnabled(true);
    }

    private void cmdExportNoneActionPerformed(java.awt.event.ActionEvent evt)
    {
        // :
        if (chkBuildings.isSelected())
        {
            numSelected--;
        }
        if (chkCitizens.isSelected())
        {
            numSelected--;
        }
        if (chkCivilizations.isSelected())
        {
            numSelected--;
        }
        if (chkCulture.isSelected())
        {
            numSelected--;
        }
        if (chkDifficulties.isSelected())
        {
            numSelected--;
        }
        if (chkEras.isSelected())
        {
            numSelected--;
        }
        if (chkEspionage.isSelected())
        {
            numSelected--;
        }
        if (chkExperience.isSelected())
        {
            numSelected--;
        }
        if (chkFlavors.isSelected())
        {
            numSelected--;
        }
        if (chkGovernments.isSelected())
        {
            numSelected--;
        }
        if (chkRULE.isSelected())
        {
            numSelected--;
        }
        if (chkResources.isSelected())
        {
            numSelected--;
        }
        if (chkTechnologies.isSelected())
        {
            numSelected--;
        }
        if (chkTerrains.isSelected())
        {
            numSelected--;
        }
        if (chkUnits.isSelected())
        {
            numSelected--;
        }
        if (chkWorkerJobs.isSelected())
        {
            numSelected--;
        }
        if (chkWorldSizes.isSelected())
        {
            numSelected--;
        }
        chkBuildings.setSelected(false);
        chkCitizens.setSelected(false);
        chkCivilizations.setSelected(false);
        chkCulture.setSelected(false);
        chkDifficulties.setSelected(false);
        chkEras.setSelected(false);
        chkEspionage.setSelected(false);
        chkExperience.setSelected(false);
        chkFlavors.setSelected(false);
        chkGovernments.setSelected(false);
        chkRULE.setSelected(false);
        chkResources.setSelected(false);
        chkTechnologies.setSelected(false);
        chkTerrains.setSelected(false);
        chkUnits.setSelected(false);
        chkWorkerJobs.setSelected(false);
        chkWorldSizes.setSelected(false);
        if (numSelected == 0)
        {
            cmdExport.setEnabled(false);
        }
    }

    private void cmdExportNone2ActionPerformed(java.awt.event.ActionEvent evt)
    {
        if (chkCities.isSelected())
        {
            numSelected--;
        }
        if (chkColonies.isSelected())
        {
            numSelected--;
        }
        if (chkContinents.isSelected())
        {
            numSelected--;
        }
        if (chkStartingLocations.isSelected())
        {
            numSelected--;
        }
        if (chkTiles.isSelected())
        {
            numSelected--;
        }
        if (chkMapUnits.isSelected())
        {
            numSelected--;
        }
        if (chkWorldCharacteristics.isSelected())
        {
            numSelected--;
        }
        if (chkWorldMap.isSelected())
        {
            numSelected--;
        }
        if (biqFile.get(biqIndex).hasCustomMap())
        {
            chkCities.setSelected(false);
            chkColonies.setSelected(false);
            chkContinents.setSelected(false);
            chkStartingLocations.setSelected(false);
            chkTiles.setSelected(false);
            chkMapUnits.setSelected(false);
            chkWorldCharacteristics.setSelected(false);
            chkWorldMap.setSelected(false);
        }
        if (numSelected == 0)
        {
            cmdExport.setEnabled(false);
        }
    }

    private void cmdExportAll2ActionPerformed(java.awt.event.ActionEvent evt)
    {
        if (!(chkCities.isSelected()))
        {
            numSelected++;
        }
        if (!(chkColonies.isSelected()))
        {
            numSelected++;
        }
        if (!(chkContinents.isSelected()))
        {
            numSelected++;
        }
        if (!(chkStartingLocations.isSelected()))
        {
            numSelected++;
        }
        if (!(chkTiles.isSelected()))
        {
            numSelected++;
        }
        if (!(chkMapUnits.isSelected()))
        {
            numSelected++;
        }
        if (!(chkWorldCharacteristics.isSelected()))
        {
            numSelected++;
        }
        if (!(chkWorldMap.isSelected()))
        {
            numSelected++;
        }
        if (biqFile.get(biqIndex).hasCustomMap())
        {
            chkCities.setSelected(true);
            chkColonies.setSelected(true);
            chkContinents.setSelected(true);
            chkStartingLocations.setSelected(true);
            chkTiles.setSelected(true);
            chkMapUnits.setSelected(true);
            chkWorldCharacteristics.setSelected(true);
            chkWorldMap.setSelected(true);
        }
        cmdExport.setEnabled(true);
    }

    private void chkCitiesActionPerformed(java.awt.event.ActionEvent evt)
    {
        // :
        if (chkCities.isSelected())
        {
            cmdExport.setEnabled(true);
            numSelected++;
        }
        else
        {
            numSelected--;
            if (numSelected == 0)
            {
                cmdExport.setEnabled(false);
            }
        }
    }

    private void chkColoniesActionPerformed(java.awt.event.ActionEvent evt)
    {
        // :
        if (chkColonies.isSelected())
        {
            cmdExport.setEnabled(true);
            numSelected++;
        }
        else
        {
            numSelected--;
            if (numSelected == 0)
            {
                cmdExport.setEnabled(false);
            }
        }
    }

    private void chkContinentsActionPerformed(java.awt.event.ActionEvent evt)
    {
        // :
        if (chkContinents.isSelected())
        {
            cmdExport.setEnabled(true);
            numSelected++;
        }
        else
        {
            numSelected--;
            if (numSelected == 0)
            {
                cmdExport.setEnabled(false);
            }
        }
    }

    private void chkStartingLocationsActionPerformed(java.awt.event.ActionEvent evt)
    {
        // :
        if (chkStartingLocations.isSelected())
        {
            cmdExport.setEnabled(true);
            numSelected++;
        }
        else
        {
            numSelected--;
            if (numSelected == 0)
            {
                cmdExport.setEnabled(false);
            }
        }
    }

    private void chkTilesActionPerformed(java.awt.event.ActionEvent evt)
    {
        // :
        if (chkTiles.isSelected())
        {
            cmdExport.setEnabled(true);
            numSelected++;
        }
        else
        {
            numSelected--;
            if (numSelected == 0)
            {
                cmdExport.setEnabled(false);
            }
        }
    }

    private void chkMapUnitsActionPerformed(java.awt.event.ActionEvent evt)
    {
        // :
        if (chkMapUnits.isSelected())
        {
            cmdExport.setEnabled(true);
            numSelected++;
        }
        else
        {
            numSelected--;
            if (numSelected == 0)
            {
                cmdExport.setEnabled(false);
            }
        }
    }

    private void chkWorldCharacteristicsActionPerformed(java.awt.event.ActionEvent evt)
    {
        // :
        if (chkWorldCharacteristics.isSelected())
        {
            cmdExport.setEnabled(true);
            numSelected++;
        }
        else
        {
            numSelected--;
            if (numSelected == 0)
            {
                cmdExport.setEnabled(false);
            }
        }
    }

    private void chkWorldMapActionPerformed(java.awt.event.ActionEvent evt)
    {
        // :
        if (chkWorldMap.isSelected())
        {
            cmdExport.setEnabled(true);
            numSelected++;
        }
        else
        {
            numSelected--;
            if (numSelected == 0)
            {
                cmdExport.setEnabled(false);
            }
        }
    }

    private void cmbBIQ2ActionPerformed(java.awt.event.ActionEvent evt)
    {
        if (chkSaveChecks.isSelected())
        {
            //update
            //logger.trace("saving check boxes");
            saveCheckBoxes();
        }
        String fileName = (String) cmbBIQ1.getSelectedItem();
        for (int i = 0; i < biqFile.size(); i++)
        {
            if (biqFile.get(i).fileName.equals(fileName))
            {
                biq2Index = i;
                break;
            }
        }
        //logger.trace(biqIndex);
        if (!(biqIndex == -1))
        {
            updateCheckBoxes();
            if (chkSaveChecks.isSelected())
            {
                //update
                //logger.trace("loading check boxes");
                loadCheckBoxes();
            }
            main.setTitle(biqFile.get(biqIndex).fileName);
        }
    }

    private void chkCompareModeActionPerformed(java.awt.event.ActionEvent evt)
    {
        // :
        if (chkCompareMode.isSelected())
        {
            cmbBIQ2.setEnabled(true);
        }
        else
        {
            cmbBIQ2.setEnabled(false);
        }
    }

    private void cmbBIQ1ActionPerformed(java.awt.event.ActionEvent evt)
    {
        // :
        if (chkSaveChecks.isSelected())
        {
            //update
            //logger.trace("saving check boxes");
            saveCheckBoxes();
        }
        //logger.trace("selected item: " + (String)cmbBIQ1.getSelectedItem());
        String fileName = (String) cmbBIQ1.getSelectedItem();
        for (int i = 0; i < biqFile.size(); i++)
        {
            if (biqFile.get(i).fileName.equals(fileName))
            {
                biqIndex = i;
                break;
            }
        }
        //logger.trace(biqIndex);
        if (!(biqIndex == -1))
        {
            updateCheckBoxes();
            if (chkSaveChecks.isSelected())
            {
                //update
                //logger.trace("loading check boxes");
                loadCheckBoxes();
            }
            main.setTitle(biqFile.get(biqIndex).fileName);
        }
    }

    private void cmbBIQ1PropertyChange(java.beans.PropertyChangeEvent evt)
    {
        // :
        //JOptionPane.showMessageDialog(null, "Property Changed");
    }

    private void txtSeparatorKeyTyped(java.awt.event.KeyEvent evt)
    {
        if (!(chkMultiLetter.isSelected()))
        {
            txtSeparator.setText(txtSeparator.getText().substring(txtSeparator.getText().length()));
            if (txtSeparator.getText().equals(" "))
            {
                txtSeparator.setText("[space]");
            }
        }
    }

    private void cmdBrowseActionPerformed(java.awt.event.ActionEvent evt)
    {
        try {
            File file = FileIO.getFile(FileDialog.SAVE, false, null);
            txtOutputFile.setText(file.getPath());
        }
        catch(IOException ex) {
            logger.error("Error on browse", ex);
        }
    }

    private void cmdExportMouseClicked(java.awt.event.MouseEvent evt)
    {
        try
        {
            //JOptionPane.showMessageDialog(null, txtOutputFile.get, separatorText());
            String outFile = txtOutputFile.getText();
            if (chkAppendTxt.isSelected())
            {
                if (outFile.length() < 4)
                {
                    outFile = outFile + ".txt";
                }
                else
                    if (!(outFile.substring(outFile.length() - 4).equals(".txt")))
                    {
                        outFile = outFile + ".txt";
                    }
            }
            File checkExists = new File(outFile);
            int overwrite = 1;  //true
            if (checkExists.exists())
            {   //int cot = JOptionPane.show
                Object[] options =
                {
                    "Cancel", "Overwrite"
                };
                overwrite = JOptionPane.showOptionDialog(null, "File already exists.  Overwrite?", "Overwrite file?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            }
            if (logger.isInfoEnabled())
            {
                logger.info("write approved: " + overwrite);
            }
            if (logger.isInfoEnabled())
            {
                logger.info("Compare mode enabled: " + chkCompareMode.isSelected());
            }
            if (overwrite == 1 && (!chkCompareMode.isSelected()))
            {
                exportWithoutComparisons(outFile);
            }//end write section
            //COMPARISON SECTION
            //COMPARISON SECTION
            //COMPARISON SECTION
            //COMPARISON SECTION
            //COMPARISON SECTION
            //COMPARISON SECTION
            else if (overwrite == 1 && (chkCompareMode.isSelected()))
            {
                checkForFolderExistance(outFile);
                //get comparisons
                FileWriter output = new FileWriter(outFile);
                if (txtSeparator.getText().equals("[space]"))
                {
                    separator = "   ";
                }
                else
                {
                    separator = " " + txtSeparator.getText() + " ";
                }
                if (chkBuildings.isSelected())
                {
                    compareSections(output, Section.BLDG);
                }
                //CITIZENS
                if (chkCitizens.isSelected())
                {
                    compareSections(output, Section.CTZN);
                }
                //CULTURAL LEVELS
                if (chkCulture.isSelected())
                {
                    compareSections(output, Section.CULT);
                }
                //DIFFICULTIES
                if (chkDifficulties.isSelected())
                {
                    compareSections(output, Section.DIFF);
                }
                //ERAS
                if (chkEras.isSelected())
                {
                    compareSections(output, Section.ERAS);
                }
                //Espionage missions
                if (chkEspionage.isSelected())
                {
                    compareSections(output, Section.ESPN);
                }
                //EXPERIENCE LEVELS
                if (chkExperience.isSelected())
                {
                    compareSections(output, Section.EXPR);
                }
                //FLAVORS
                if (chkFlavors.isSelected())
                {
                    compareSections(output, Section.FLAV);
                }
                //GOODS
                if (chkResources.isSelected())
                {
                    compareSections(output, Section.GOOD);
                }
                //GOVERNMENTS
                if (chkGovernments.isSelected())
                {
                    compareSections(output, Section.GOVT);
                }
                //UNITS
                if (chkUnits.isSelected())
                {
                    compareSections(output, Section.PRTO);
                }
                //CIVILIZATIONS
                if (chkCivilizations.isSelected())
                {
                    compareSections(output, Section.RACE);
                }
                //GENERAL SETTINGS (RULES)
                if (chkRULE.isSelected())
                {
                    compareRULE(output);
                }
                //TECHNOLOGIES
                if (chkTechnologies.isSelected())
                {
                    compareSections(output, Section.TECH);
                }
                //TERRAINS
                if (chkTerrains.isSelected())
                {
                    compareSections(output, Section.TERR);
                }
                //TERRAFORMS
                if (chkWorkerJobs.isSelected())
                {
                    compareSections(output, Section.TFRM);
                }
                //WORLD SIZES
                if (chkWorldSizes.isSelected())
                {
                    compareSections(output, Section.WSIZ);
                }
                //WORLD CHACTERISTICS
                if (chkWorldCharacteristics.isSelected())
                {
                    compareWCHR(output);
                }
                //WORLD MAPS
                if (chkWorldMap.isSelected())
                {
                    compareWMAP(output);
                }
                //STARTING LOCATION
                if (chkStartingLocations.isSelected())
                {
                    compareSLOC(output);
                }
                //CITIES
                if (chkCities.isSelected())
                {
                    compareSections(output, Section.CITY);
                }
                //TILES
                if (chkTiles.isSelected())
                {
                    compareSections(output, Section.TILE);
                }
                //LEAD section
                if (chkPlayerData.isSelected())
                {
                    compareLEAD(output);
                }
                //SCENARIO PROPERTIES (GAME)
                if (chkGAME.isSelected())
                {
                    compareGAME(output);
                }
                output.close();
                lblSuccessfulOutput.setVisible(true);
                LabelHider lblHide = new LabelHider(lblSuccessfulOutput);
            }
        }//end try
        catch (java.io.IOException e)
        {
            logger.error(ioException, e);
            JOptionPane.showMessageDialog(null, e);
        }
    }
    private void compareSections(FileWriter output, Section section)
    {
        try
        {
            output.write("--> " + Section.values()[section.ordinal()] + " <--");

            Boolean[] biq1ExistsInSecond = new Boolean[biqFile.get(biqIndex).numSection(section)];
            Boolean[] biq2ExistsInFirst = new Boolean[biqFile.get(biq2Index).numSection(section)];
            for (int i = 0; i < biqFile.get(biq2Index).numSection(section); i++)
            {   //initialize; Boolean doesn't automatically go to false
                biq2ExistsInFirst[i] = false;
            }
            output.write(biqFile.get(biqIndex).fileName + fileSeparator + biqFile.get(biq2Index).fileName + lineReturn + lineReturn);
            for (int i = 0; i < biqFile.get(biqIndex).numSection(section); i++)
            {
                BIQSection instance = (BIQSection)biqFile.get(biqIndex).getSection(section).get(i);
                String name = (String)instance.getProperty("Name");
                int ID = biqFile.get(biq2Index).findInstance(section, name);
                if (ID == -1)
                {
                    biq1ExistsInSecond[i] = false;
                }
                else
                {
                    biq1ExistsInSecond[i] = true;
                    biq2ExistsInFirst[ID] = true; //if it exists here, I'll find it eventually
                    BIQSection otherInstance = (BIQSection)biqFile.get(biq2Index).getSection(section).get(ID);
                    String toWrite = instance.compareTo(otherInstance, separator);
                    if (!toWrite.equals(""))
                    {
                        if (logger.isTraceEnabled())
                        {
                            logger.trace("writing to output in " + section);
                        }
                        output.write(toWrite + lineReturn);
                    }

                }
            }
            for (int i = 0; i < biqFile.get(biqIndex).numSection(section); i++)
            {
                if (!biq1ExistsInSecond[i])
                {
                    if (logger.isTraceEnabled())
                    {
                        logger.trace(i + dneInSecond);
                    }
                    BIQSection instance = (BIQSection)biqFile.get(biqIndex).getSection(section).get(i);
                    output.write(section + " " + instance.getProperty("Name") + dneIn + biqFile.get(biq2Index).fileName + "." + lineReturn);
                }
            }
            if (logger.isTraceEnabled())
            {
                logger.trace(finishDNESecond);
            }
            output.write(lineReturn);
            for (int i = 0; i < biqFile.get(biq2Index).numSection(section); i++)
            {
                if (logger.isTraceEnabled())
                {
                    logger.trace(i);
                }
                if (!biq2ExistsInFirst[i])
                {
                    if (logger.isTraceEnabled())
                    {
                        logger.trace(i + dneInFirst);
                    }
                    BIQSection instance = (BIQSection)biqFile.get(biq2Index).getSection(section).get(i);
                    output.write(section + " " + instance.getProperty("Name") + dneIn + biqFile.get(biqIndex).fileName + "." + lineReturn);
                }
            }
        }
        catch(java.io.IOException e){
            logger.error(ioException, e);
        }
    }
    private void compareRULE(FileWriter output)
    {
        try
        {
            Boolean[] biq1ExistsInSecond = new Boolean[biqFile.get(biqIndex).numRules];
            Boolean[] biq2ExistsInFirst = new Boolean[biqFile.get(biq2Index).numRules];
            for (int i = 0; i < biqFile.get(biq2Index).numRules; i++)
            {   //initialize; Boolean doesn't automatically go to false
                biq2ExistsInFirst[i] = false;
            }
            if (logger.isTraceEnabled())
            {
                logger.trace(fileOne + biqIndex);
            }
            if (logger.isTraceEnabled())
            {
                logger.trace(fileTwo + biq2Index);
            }
            output.write(biqFile.get(biqIndex).fileName + fileSeparator + biqFile.get(biq2Index).fileName + lineReturn + lineReturn);
            for (int i = 0; i < biqFile.get(biqIndex).numRules; i++)
            {

                //assume there is exactly one General Settings page per BIQ
                //if it actually can support two, change this later
                biq1ExistsInSecond[i] = true;
                biq2ExistsInFirst[i] = true; //if it exists here, I'll find it eventually
                String toWrite = biqFile.get(biqIndex).rule.get(i).compareTo(biqFile.get(biq2Index).rule.get(i), separator);
                if (!toWrite.equals(""))
                {
                    if (logger.isTraceEnabled())
                    {
                        logger.trace(writingToOutput);
                    }
                    output.write(toWrite + lineReturn);
                }
            }
            for (int i = 0; i < biqFile.get(biqIndex).numRules; i++)
            {
                if (!biq1ExistsInSecond[i])
                {   //this is impossible
                    if (logger.isTraceEnabled())
                    {
                        logger.trace(i + dneInSecond);
                    }
                    output.write("General settings " + i + dneIn + biqFile.get(biq2Index).fileName + "." + lineReturn);
                }
            }
            if (logger.isTraceEnabled())
            {
                logger.trace(finishDNESecond);
            }
            output.write(lineReturn);
            for (int i = 0; i < biqFile.get(biq2Index).numRules; i++)
            {
                if (logger.isTraceEnabled())
                {
                    logger.trace(i);
                }
                if (!biq2ExistsInFirst[i])
                {   //impossible
                    if (logger.isTraceEnabled())
                    {
                        logger.trace(i + dneInFirst);
                    }
                    output.write("General settings " + i + dneIn + biqFile.get(biqIndex).fileName + "." + lineReturn);
                }
            }
        }
        catch(java.io.IOException e){
            logger.error(ioException, e);
        }
    }
    private void compareWCHR(FileWriter output)
    {
        try{
            Boolean[] biq1ExistsInSecond = new Boolean[biqFile.get(biqIndex).numWorldCharacteristics];
            Boolean[] biq2ExistsInFirst = new Boolean[biqFile.get(biq2Index).numWorldCharacteristics];
            for (int i = 0; i < biqFile.get(biq2Index).numWorldCharacteristics; i++)
            {   //initialize; Boolean doesn't automatically go to false
                biq2ExistsInFirst[i] = false;
            }
            if (logger.isTraceEnabled())
            {
                logger.trace(fileOne + biqIndex);
            }
            if (logger.isTraceEnabled())
            {
                logger.trace(fileTwo + biq2Index);
            }
            output.write(biqFile.get(biqIndex).fileName + fileSeparator + biqFile.get(biq2Index).fileName + lineReturn + lineReturn);
            for (int i = 0; i < biqFile.get(biqIndex).numWorldCharacteristics; i++)
            {
                String toWrite = biqFile.get(biqIndex).worldCharacteristic.get(i).compareTo(biqFile.get(biq2Index).worldCharacteristic.get(i), separator);
                if (!toWrite.equals(""))
                {
                    if (logger.isTraceEnabled())
                    {
                        logger.trace(writingToOutput);
                    }
                    output.write(toWrite + lineReturn);
                }
            }
        }
        catch(java.io.IOException e){
            logger.error(ioException, e);
        }
    }    
    private void compareWMAP(FileWriter output)
    {
        try
        {
            Boolean[] biq1ExistsInSecond = new Boolean[biqFile.get(biqIndex).numWorldMaps];
            Boolean[] biq2ExistsInFirst = new Boolean[biqFile.get(biq2Index).numWorldMaps];
            for (int i = 0; i < biqFile.get(biq2Index).numWorldMaps; i++)
            {   //initialize; Boolean doesn't automatically go to false
                biq2ExistsInFirst[i] = false;
            }
            if (logger.isTraceEnabled())
            {
                logger.trace(fileOne + biqIndex);
            }
            if (logger.isTraceEnabled())
            {
                logger.trace(fileTwo + biq2Index);
            }
            output.write(biqFile.get(biqIndex).fileName + fileSeparator + biqFile.get(biq2Index).fileName + lineReturn + lineReturn);
            for (int i = 0; i < biqFile.get(biqIndex).numWorldMaps; i++)
            {
                String toWrite = biqFile.get(biqIndex).worldMap.get(i).compareTo(biqFile.get(biq2Index).worldMap.get(i), separator);
                if (!toWrite.equals(""))
                {
                    if (logger.isTraceEnabled())
                    {
                        logger.trace(writingToOutput);
                    }
                    output.write(toWrite + lineReturn);
                }
            }           
        }
        catch(java.io.IOException e){
            logger.error(ioException, e);
        }
    }    
    private void compareSLOC(FileWriter output)
    {
        try
        {
            Boolean[] biq1ExistsInSecond = new Boolean[biqFile.get(biqIndex).numStartingLocations];
            Boolean[] biq2ExistsInFirst = new Boolean[biqFile.get(biq2Index).numStartingLocations];
            for (int i = 0; i < biqFile.get(biq2Index).numStartingLocations; i++)
            {   //initialize; Boolean doesn't automatically go to false
                biq2ExistsInFirst[i] = false;
            }
            if (logger.isTraceEnabled())
            {
                logger.trace(fileOne + biqIndex);
            }
            if (logger.isTraceEnabled())
            {
                logger.trace(fileTwo + biq2Index);
            }
            output.write(biqFile.get(biqIndex).fileName + fileSeparator + biqFile.get(biq2Index).fileName + lineReturn + lineReturn);
            for (int i = 0; i < biqFile.get(biqIndex).numStartingLocations; i++)
            {
                if (i <  biqFile.get(biq2Index).numStartingLocations)
                {
                    String toWrite = biqFile.get(biqIndex).startingLocation.get(i).compareTo(biqFile.get(biq2Index).startingLocation.get(i), separator);
                    if (!toWrite.equals(""))
                    {
                        if (logger.isTraceEnabled())
                        {
                            logger.trace(writingToOutput);
                        }
                        output.write(toWrite + lineReturn);
                    }
                }
                else
                {
                    output.write("StartingLocation " + i + dneIn + biqFile.get(biq2Index).fileName + "." + lineReturn);
                }
            }
            if (logger.isTraceEnabled())
            {
                logger.trace(finishDNESecond);
            }
            for (int i = 0; i < biqFile.get(biq2Index).numStartingLocations; i++)
            {
                if (i > biqFile.get(biqIndex).numStartingLocations)
                {
                    output.write("StartingLocation " + i + dneIn + biqFile.get(biqIndex).fileName + "." + lineReturn);
                }
            }
        }
        catch(java.io.IOException e){
            logger.error(ioException, e);
        }
    }

    private void compareLEAD(FileWriter output)
    {
        try{
            Boolean[] biq1ExistsInSecond = new Boolean[biqFile.get(biqIndex).numPlayers];
            Boolean[] biq2ExistsInFirst = new Boolean[biqFile.get(biq2Index).numPlayers];
            for (int i = 0; i < biqFile.get(biq2Index).numPlayers; i++)
            {   //initialize; Boolean doesn't automatically go to false
                biq2ExistsInFirst[i] = false;
            }
            if (logger.isTraceEnabled())
            {
                logger.trace(fileOne + biqIndex);
            }
            if (logger.isTraceEnabled())
            {
                logger.trace(fileTwo + biq2Index);
            }
            output.write(biqFile.get(biqIndex).fileName + fileSeparator + biqFile.get(biq2Index).fileName + lineReturn + lineReturn);
            for (int i = 0; i < biqFile.get(biqIndex).numPlayers; i++)
            {
                int name = biqFile.get(biqIndex).player.get(i).getCiv();
                int playerID = biqFile.get(biq2Index).findPlayer(name);
                if (playerID == -1)
                {
                    biq1ExistsInSecond[i] = false;
                }
                else
                {
                    biq1ExistsInSecond[i] = true;
                    biq2ExistsInFirst[playerID] = true; //if it exists here, I'll find it eventually
                    String toWrite = biqFile.get(biqIndex).player.get(i).compareTo(biqFile.get(biq2Index).player.get(playerID), separator);
                    if (!toWrite.equals(""))
                    {
                        if (logger.isTraceEnabled())
                        {
                            logger.trace(writingToOutput);
                        }
                        output.write(toWrite + lineReturn);
                    }
                }
            }
            for (int i = 0; i < biqFile.get(biqIndex).numPlayers; i++)
            {
                if (!biq1ExistsInSecond[i])
                {
                    if (logger.isTraceEnabled())
                    {
                        logger.trace(i + dneInSecond);
                    }
                    output.write("Player " + biqFile.get(biqIndex).player.get(i).getCiv() + dneIn + biqFile.get(biq2Index).fileName + "." + lineReturn);
                }
            }
            if (logger.isTraceEnabled())
            {
                logger.trace(finishDNESecond);
            }
            output.write(lineReturn);
            for (int i = 0; i < biqFile.get(biq2Index).numPlayers; i++)
            {
                if (logger.isTraceEnabled())
                {
                    logger.trace(i);
                }
                if (!biq2ExistsInFirst[i])
                {
                    if (logger.isTraceEnabled())
                    {
                        logger.trace(i + dneInFirst);
                    }
                    output.write("Player " + biqFile.get(biq2Index).player.get(i).getCiv() + dneIn + biqFile.get(biqIndex).fileName + "." + lineReturn);
                }
            }
        }
        catch(java.io.IOException e){
            logger.error("IOException while exporting LEAD", e);
        }
    }

    private void compareGAME(FileWriter output)
    {
        try{
            Boolean[] biq1ExistsInSecond = new Boolean[biqFile.get(biqIndex).numScenarioProperties];
            Boolean[] biq2ExistsInFirst = new Boolean[biqFile.get(biq2Index).numScenarioProperties];
            for (int i = 0; i < biqFile.get(biq2Index).numScenarioProperties; i++)
            {   //initialize; Boolean doesn't automatically go to false
                biq2ExistsInFirst[i] = false;
            }
            if (logger.isTraceEnabled())
            {
                logger.trace(fileOne + biqIndex);
            }
            if (logger.isTraceEnabled())
            {
                logger.trace(fileTwo + biq2Index);
            }
            output.write(biqFile.get(biqIndex).fileName + fileSeparator + biqFile.get(biq2Index).fileName + lineReturn + lineReturn);
            for (int i = 0; i < biqFile.get(biqIndex).numScenarioProperties; i++)
            {

                //assume there is exactly one General Settings page per BIQ
                //if it actually can support two, change this later
                biq1ExistsInSecond[i] = true;
                biq2ExistsInFirst[i] = true; //if it exists here, I'll find it eventually
                String toWrite = biqFile.get(biqIndex).scenarioProperty.get(i).compareTo(biqFile.get(biq2Index).scenarioProperty.get(i), separator);
                if (!toWrite.equals(""))
                {
                    if (logger.isTraceEnabled())
                    {
                        logger.trace(writingToOutput);
                    }
                    output.write(toWrite + lineReturn);
                }
            }
            for (int i = 0; i < biqFile.get(biqIndex).numScenarioProperties; i++)
            {
                if (!biq1ExistsInSecond[i])
                {
                    //this is impossible
                    if (logger.isTraceEnabled())
                    {
                        logger.trace(i + dneInSecond);
                    }
                    output.write("Ruleset " + i + dneIn + biqFile.get(biq2Index).fileName + "." + lineReturn);
                }
            }
            if (logger.isTraceEnabled())
            {
                logger.trace(finishDNESecond);
            }
            output.write(lineReturn);
            for (int i = 0; i < biqFile.get(biq2Index).numScenarioProperties; i++)
            {
                if (logger.isTraceEnabled())
                {
                    logger.trace(i);
                }
                if (!biq2ExistsInFirst[i])
                {   //impossible
                    if (logger.isTraceEnabled())
                    {
                        logger.trace(i + dneInFirst);
                    }
                    output.write("Ruleset " + i + dneIn + biqFile.get(biqIndex).fileName + "." + lineReturn);
                }
            }
        }
        catch(java.io.IOException e){
            logger.error("IO exception while comparing GAME", e);
        }
    }

    private void cmdExportActionPerformed(java.awt.event.ActionEvent evt)
    {
        // :
    }

    private void lblSuccessfulOutputComponentShown(java.awt.event.ComponentEvent evt)
    {
        // :
    }

    private void chkPlayerDataActionPerformed(java.awt.event.ActionEvent evt)
    {
        // :
        if (chkPlayerData.isSelected())
        {
            cmdExport.setEnabled(true);
            numSelected++;
        }
        else
        {
            numSelected--;
            if (numSelected == 0)
            {
                cmdExport.setEnabled(false);
            }
        }
    }

    private void chkGAMEActionPerformed(java.awt.event.ActionEvent evt)
    {
        if (chkGAME.isSelected())
        {
            cmdExport.setEnabled(true);
            numSelected++;
        }
        else
        {
            numSelected--;
            if (numSelected == 0)
            {
                cmdExport.setEnabled(false);
            }
        }
    }

    /**
     * Stores the current values of the check boxes on this tab for later
     * retrieval.  They are stored in an instance of checkBoxSettings.java.
     */
    public void saveCheckBoxes()
    {
        checkBoxSettings.get(biqIndex).storeBuildings(chkBuildings);
        checkBoxSettings.get(biqIndex).storeCitizens(chkCitizens);
        checkBoxSettings.get(biqIndex).storeCulture(chkCulture);
        checkBoxSettings.get(biqIndex).storeDifficulties(chkDifficulties);
        checkBoxSettings.get(biqIndex).storeEras(chkEras);
        checkBoxSettings.get(biqIndex).storeExperience(chkExperience);
        checkBoxSettings.get(biqIndex).storeEspionage(chkEspionage);
        checkBoxSettings.get(biqIndex).storeFlavors(chkFlavors);
        checkBoxSettings.get(biqIndex).storeResources(chkResources);
        checkBoxSettings.get(biqIndex).storeGovernments(chkGovernments);
        checkBoxSettings.get(biqIndex).storeGeneralSettings(chkRULE);
        checkBoxSettings.get(biqIndex).storeUnits(chkUnits);
        checkBoxSettings.get(biqIndex).storeTechnologies(chkTechnologies);
        checkBoxSettings.get(biqIndex).storeTerrains(chkTerrains);
        checkBoxSettings.get(biqIndex).storeWorkerJobs(chkWorkerJobs);
        checkBoxSettings.get(biqIndex).storeWorldSizes(chkWorldSizes);
        checkBoxSettings.get(biqIndex).storeCivilizations(chkCivilizations);
        checkBoxSettings.get(biqIndex).storeCities(chkCities);
        checkBoxSettings.get(biqIndex).storeColonies(chkColonies);
        checkBoxSettings.get(biqIndex).storeContinents(chkContinents);
        checkBoxSettings.get(biqIndex).storeStartingLocations(chkStartingLocations);
        checkBoxSettings.get(biqIndex).storeTiles(chkTiles);
        checkBoxSettings.get(biqIndex).storeMapUnits(chkMapUnits);
        checkBoxSettings.get(biqIndex).storeWorldMap(chkWorldMap);
        checkBoxSettings.get(biqIndex).storeWorldCharacteristics(chkWorldCharacteristics);
        checkBoxSettings.get(biqIndex).storeAdditionalProperties(chkGAME);
        checkBoxSettings.get(biqIndex).storePlayerData(chkPlayerData);
        checkBoxSettings.get(biqIndex).storeNumSelected(numSelected);
    }

    /**
     * Sets the check boxes on this tab based on the saved values for them.
     * These values are stored in the checkBoxSettings.java class.
     */
    public void loadCheckBoxes()
    {
        checkBoxSettings.get(biqIndex).setBuildings(chkBuildings);
        checkBoxSettings.get(biqIndex).setCitizens(chkCitizens);
        checkBoxSettings.get(biqIndex).setCulture(chkCulture);
        checkBoxSettings.get(biqIndex).setDifficulties(chkDifficulties);
        checkBoxSettings.get(biqIndex).setEras(chkEras);
        checkBoxSettings.get(biqIndex).setEspionage(chkEspionage);
        checkBoxSettings.get(biqIndex).setExperience(chkExperience);
        checkBoxSettings.get(biqIndex).setFlavors(chkFlavors);
        checkBoxSettings.get(biqIndex).setResources(chkResources);
        checkBoxSettings.get(biqIndex).setGovernments(chkGovernments);
        checkBoxSettings.get(biqIndex).setGeneralSettings(chkRULE);
        checkBoxSettings.get(biqIndex).setUnits(chkUnits);
        checkBoxSettings.get(biqIndex).setTechnologies(chkTechnologies);
        checkBoxSettings.get(biqIndex).setTerrains(chkTerrains);
        checkBoxSettings.get(biqIndex).setWorkerJobs(chkWorkerJobs);
        checkBoxSettings.get(biqIndex).setWorldSizes(chkWorldSizes);
        checkBoxSettings.get(biqIndex).setCivilizations(chkCivilizations);
        checkBoxSettings.get(biqIndex).setCities(chkCities);
        checkBoxSettings.get(biqIndex).setColonies(chkColonies);
        checkBoxSettings.get(biqIndex).setContinents(chkContinents);
        checkBoxSettings.get(biqIndex).setStartingLocations(chkStartingLocations);
        checkBoxSettings.get(biqIndex).setTiles(chkTiles);
        checkBoxSettings.get(biqIndex).setMapUnits(chkMapUnits);
        checkBoxSettings.get(biqIndex).setWorldMap(chkWorldMap);
        checkBoxSettings.get(biqIndex).setWorldCharacteristics(chkWorldCharacteristics);
        checkBoxSettings.get(biqIndex).setAdditionalProperties(chkGAME);
        checkBoxSettings.get(biqIndex).setPlayerData(chkPlayerData);
        numSelected = checkBoxSettings.get(biqIndex).getNumSelected();
    }

    private void exportWithoutComparisons(String outFile)
    {
        try{
            checkForFolderExistance(outFile);
            FileWriter output = new FileWriter(outFile);
            if (chkBIQ.isSelected())
            {
                biqFile.get(biqIndex).exportEnglish = false;
            }
            else
            {
                biqFile.get(biqIndex).exportEnglish = true;
            }
            if (chkGAME.isSelected())
            {
                writeSectionHeader(output, "SCENARIO PROPERTIES");
                for (int i = 0; i < biqFile.get(biqIndex).numScenarioProperties; i++)
                {
                    output.write(biqFile.get(biqIndex).scenarioProperty.get(i).toFile());
                }
            }
            if (chkBuildings.isSelected())
            {
                writeSectionHeader(output, "BUILDINGS");
                for (int i = 0; i < biqFile.get(biqIndex).numBuildings; i++)
                {
                    output.write("index: " + i + java.lang.System.getProperty("line.separator"));
                    output.write(biqFile.get(biqIndex).buildings.get(i).toFile());
                }
            }
            if (chkCitizens.isSelected())
            {
                writeSectionHeader(output, "CITIZENS");
                for (int i = 0; i < biqFile.get(biqIndex).numCitizens; i++)
                {
                    output.write(biqFile.get(biqIndex).citizens.get(i).toFile());
                }
            }
            if (chkCivilizations.isSelected())
            {
                writeSectionHeader(output, "CIVILIZATIONS");
                for (int i = 0; i < biqFile.get(biqIndex).numCivilizations; i++)
                {
                    output.write(biqFile.get(biqIndex).civilization.get(i).toFile());
                }
            }
            if (chkCulture.isSelected())
            {
                writeSectionHeader(output, "CULTURE LEVELS");
                for (int i = 0; i < biqFile.get(biqIndex).numCulturalOpinions; i++)
                {
                    output.write(biqFile.get(biqIndex).culture.get(i).toFile());
                }
            }
            if (chkDifficulties.isSelected())
            {
                writeSectionHeader(output, "DIFFICULTIES");
                for (int i = 0; i < biqFile.get(biqIndex).numDifficulties; i++)
                {
                    output.write(biqFile.get(biqIndex).difficulties.get(i).toFile());
                }
            }
            if (chkEras.isSelected())
            {
                writeSectionHeader(output, "ERAS");
                for (int i = 0; i < biqFile.get(biqIndex).numEras; i++)
                {
                    output.write(biqFile.get(biqIndex).eras.get(i).toFile());
                }
            }
            if (chkEspionage.isSelected())
            {
                writeSectionHeader(output, "ESPIONAGE MISSIONS");
                for (int i = 0; i < biqFile.get(biqIndex).numEspionage; i++)
                {
                    output.write(biqFile.get(biqIndex).espionage.get(i).toFile());
                }
            }
            if (chkExperience.isSelected())
            {
                writeSectionHeader(output, "EXPERIENCE LEVELS");
                for (int i = 0; i < biqFile.get(biqIndex).numExprLevel; i++)
                {
                    output.write(biqFile.get(biqIndex).experience.get(i).toFile());
                }
            }
            if (chkFlavors.isSelected())
            {
                writeSectionHeader(output, "FLAVORS");
                for (int i = 0; i < biqFile.get(biqIndex).numFlavors; i++)
                {
                    output.write(biqFile.get(biqIndex).flavor.get(i).toFile());
                }
            }
            if (chkRULE.isSelected())
            {
                writeSectionHeader(output, "GENERAL SETTINGS");
                for (int i = 0; i < biqFile.get(biqIndex).numRules; i++)
                {
                    output.write(biqFile.get(biqIndex).rule.get(i).toFile());
                }
            }
            if (chkGovernments.isSelected())
            {
                writeSectionHeader(output, "GOVERNMENTS");
                for (int i = 0; i < biqFile.get(biqIndex).numGovernments; i++)
                {
                    output.write(biqFile.get(biqIndex).government.get(i).toFile());
                }
            }
            if (chkResources.isSelected())
            {
                writeSectionHeader(output, "RESOURCES");
                for (int i = 0; i < biqFile.get(biqIndex).numGoods; i++)
                {
                    output.write(biqFile.get(biqIndex).resource.get(i).toFile());
                }
            }
            if (chkTechnologies.isSelected())
            {
                writeSectionHeader(output, "TECHNOLOGIES");
                for (int i = 0; i < biqFile.get(biqIndex).numTechnologies; i++)
                {
                    output.write(biqFile.get(biqIndex).technology.get(i).toFile());
                }
            }
            if (chkTerrains.isSelected())
            {
                writeSectionHeader(output, "TERRAINS");
                for (int i = 0; i < biqFile.get(biqIndex).numTerrains; i++)
                {
                    output.write(biqFile.get(biqIndex).terrain.get(i).toFile());
                }
            }
            if (chkUnits.isSelected())
            {
                writeSectionHeader(output, "UNITS");
                for (int i = 0; i < biqFile.get(biqIndex).unit.size(); i++)
                {
                    output.write(biqFile.get(biqIndex).unit.get(i).toFile());
                }
            }
            if (chkWorkerJobs.isSelected())
            {
                writeSectionHeader(output, "WORKER JOBS");
                for (int i = 0; i < biqFile.get(biqIndex).numWorkerJobs; i++)
                {
                    output.write(biqFile.get(biqIndex).workerJob.get(i).toFile());
                }
            }
            if (chkWorldSizes.isSelected())
            {
                writeSectionHeader(output, "WORLD SIZES");
                for (int i = 0; i < biqFile.get(biqIndex).numWorldSizes; i++)
                {
                    output.write(biqFile.get(biqIndex).worldSize.get(i).toFile());
                }
            }
            if (chkPlayerData.isSelected())
            {
                writeSectionHeader(output, "CUSTOM PLAYER DATA");
                for (int i = 0; i < biqFile.get(biqIndex).numPlayers; i++)
                {
                    output.write(biqFile.get(biqIndex).player.get(i).toFile());
                }
            }
            if (chkCities.isSelected())
            {
                writeSectionHeader(output, "CITIES");
                for (int i = 0; i < biqFile.get(biqIndex).numCities; i++)
                {
                    output.write(biqFile.get(biqIndex).city.get(i).toFile());
                }
            }
            if (chkColonies.isSelected())
            {
                writeSectionHeader(output, "COLONIES");
                for (int i = 0; i < biqFile.get(biqIndex).numColonies; i++)
                {
                    output.write(biqFile.get(biqIndex).colony.get(i).toFile());
                }
            }
            if (chkContinents.isSelected())
            {
                writeSectionHeader(output, "CONTINENTS");
                for (int i = 0; i < biqFile.get(biqIndex).numContinents; i++)
                {
                    output.write(biqFile.get(biqIndex).continent.get(i).toFile());
                }
            }
            if (chkStartingLocations.isSelected())
            {
                writeSectionHeader(output, "STARTING LOCATIONS");
                for (int i = 0; i < biqFile.get(biqIndex).numStartingLocations; i++)
                {
                    output.write(biqFile.get(biqIndex).startingLocation.get(i).toFile());
                }
            }
            if (chkTiles.isSelected())
            {
                writeSectionHeader(output, "TILES");
                for (int i = 0; i < biqFile.get(biqIndex).numTiles; i++)
                {
                    output.write(biqFile.get(biqIndex).tile.get(i).toFile());
                }
            }
            if (chkMapUnits.isSelected())
            {
                writeSectionHeader(output, "UNITS on the MAP");
                for (int i = 0; i < biqFile.get(biqIndex).numMapUnits; i++)
                {
                    output.write(biqFile.get(biqIndex).mapUnit.get(i).toFile());
                }
            }
            if (chkWorldCharacteristics.isSelected())
            {
                writeSectionHeader(output, "WORLD CHARACTERISTICS");
                for (int i = 0; i < biqFile.get(biqIndex).numWorldCharacteristics; i++)
                {
                    output.write(biqFile.get(biqIndex).worldCharacteristic.get(i).toFile());
                }
            }
            if (chkWorldMap.isSelected())
            {
                writeSectionHeader(output, "MAP CHARACTERISTICS");
                for (int i = 0; i < biqFile.get(biqIndex).numWorldMaps; i++)
                {
                    output.write(biqFile.get(biqIndex).worldMap.get(i).toFile());
                }
            }
            output.close();
            lblSuccessfulOutput.setVisible(true);
            LabelHider lblHide = new LabelHider(lblSuccessfulOutput);
        }
        catch(java.io.IOException e){
            logger.error("IOException while exporting", e);
            JOptionPane.showMessageDialog(null, "<html>Something went wrong when exporting the BIQ info.  A partial output may be available in the file " + this.txtOutputFile.getText() + ".\nIt is recommended to check the log and report the error to the CFC thread.", "Error While Outputting BIQ Info", JOptionPane.ERROR_MESSAGE);
        }
        catch(Exception e) {
            logger.error("Unexpected exception while exporting", e);
            JOptionPane.showMessageDialog(null, "<html>Something went wrong when exporting the BIQ info.  A partial output may be available in the file " + this.txtOutputFile.getText() + ".\nIt is recommended to check the log and report the error to the CFC thread.", "Error While Outputting BIQ Info", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void checkForFolderExistance(String outFile) {        
            int slashDex = outFile.lastIndexOf(Main.fileSlash);
            String path = outFile.substring(0, slashDex);
            File folder = new File(path);
            if (!folder.exists()) {
                boolean directoriesCreated = folder.mkdirs();
                if (!directoriesCreated) {
                    logger.error("Failed to create nonexistant directories " + path);
                }
            }
    }

    private void writeSectionHeader(FileWriter output, String header) throws java.io.IOException
    {
        try{
            output.write(lineReturn);
            output.write("***" + header + "***");
            output.write(lineReturn);
            output.write(lineReturn);
        }
        catch(java.io.IOException e)
        {
            throw e;
        }
    }

    public void updateCheckBoxes()
    {
        if (biqIndex == -1)
            return;
        chkGAME.setEnabled(true);
        boolean oneRules = false;
        boolean twoRules = false;
        boolean onePlayer = false;
        boolean twoPlayer = false;
        boolean oneMap = false;
        boolean twoMap = false;
        if (cmbBIQ1.getItemCount() > 0)
        {
            //logger.trace("cmbBIQ1.getSelectedIndex: " + cmbBIQ1.getSelectedIndex());
            if (biqFile.get(cmbBIQ1.getSelectedIndex()).hasCustomRules())
            {
                oneRules = true;
            }
            if (biqFile.get(cmbBIQ1.getSelectedIndex()).hasCustomPlayerData())
            {
                onePlayer = true;
            }
            if (biqFile.get(cmbBIQ1.getSelectedIndex()).hasCustomMap())
            {
                oneMap = true;
            }
        }
        if (cmbBIQ2.getItemCount() > 0)
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("cmbBIQ2.getSelectedIndex: " + cmbBIQ2.getSelectedIndex());
            }
            if (biqFile.get(cmbBIQ2.getSelectedIndex()).hasCustomRules())
            {
                twoRules = true;
            }
            if (biqFile.get(cmbBIQ2.getSelectedIndex()).hasCustomPlayerData())
            {
                twoPlayer = true;
            }
            if (biqFile.get(cmbBIQ2.getSelectedIndex()).hasCustomMap())
            {
                twoMap = true;
            }
        }

        if (oneRules && twoRules)
        {
            chkBuildings.setEnabled(true);
            chkCitizens.setEnabled(true);
            chkCivilizations.setEnabled(true);
            chkCulture.setEnabled(true);
            chkDifficulties.setEnabled(true);
            chkEras.setEnabled(true);
            chkEspionage.setEnabled(true);
            chkExperience.setEnabled(true);
            chkFlavors.setEnabled(true);
            chkGovernments.setEnabled(true);
            chkRULE.setEnabled(true);
            chkResources.setEnabled(true);
            chkTechnologies.setEnabled(true);
            chkTerrains.setEnabled(true);
            chkUnits.setEnabled(true);
            chkWorkerJobs.setEnabled(true);
            chkWorldSizes.setEnabled(true);
            cmdExportAll.setEnabled(true);
            cmdExportNone.setEnabled(true);
        }
        else
        {
            chkBuildings.setEnabled(false);
            chkBuildings.setSelected(false);
            chkCitizens.setEnabled(false);
            chkCitizens.setSelected(false);
            chkCivilizations.setEnabled(false);
            chkCivilizations.setSelected(false);
            chkCulture.setEnabled(false);
            chkCulture.setSelected(false);
            chkDifficulties.setEnabled(false);
            chkDifficulties.setSelected(false);
            chkEras.setEnabled(false);
            chkEras.setSelected(false);
            chkEspionage.setEnabled(false);
            chkEspionage.setSelected(false);
            chkExperience.setEnabled(false);
            chkExperience.setSelected(false);
            chkFlavors.setEnabled(false);
            chkFlavors.setSelected(false);
            chkGovernments.setEnabled(false);
            chkGovernments.setSelected(false);
            chkRULE.setEnabled(false);
            chkRULE.setSelected(false);
            chkResources.setEnabled(false);
            chkResources.setSelected(false);
            chkTechnologies.setEnabled(false);
            chkTechnologies.setSelected(false);
            chkTerrains.setEnabled(false);
            chkTerrains.setSelected(false);
            chkUnits.setEnabled(false);
            chkUnits.setSelected(false);
            chkWorkerJobs.setEnabled(false);
            chkWorkerJobs.setSelected(false);
            chkWorldSizes.setEnabled(false);
            chkWorldSizes.setSelected(false);
            cmdExportAll.setEnabled(false);
            cmdExportNone.setEnabled(false);
        }

        if (onePlayer && twoPlayer)
        {
            chkPlayerData.setEnabled(true);
        }
        else
        {
            chkPlayerData.setEnabled(false);
            chkPlayerData.setSelected(false);
        }

        if (oneMap && twoMap)
        {
            chkCities.setEnabled(true);
            chkColonies.setEnabled(true);
            chkContinents.setEnabled(true);
            chkStartingLocations.setEnabled(true);
            chkTiles.setEnabled(true);
            chkMapUnits.setEnabled(true);
            chkWorldCharacteristics.setEnabled(true);
            chkWorldMap.setEnabled(true);
            cmdExportAll2.setEnabled(true);
            cmdExportNone2.setEnabled(true);
        }
        else
        {
            chkCities.setEnabled(false);
            chkCities.setSelected(false);
            chkColonies.setEnabled(false);
            chkColonies.setSelected(false);
            chkContinents.setEnabled(false);
            chkContinents.setSelected(false);
            chkStartingLocations.setEnabled(false);
            chkStartingLocations.setSelected(false);
            chkTiles.setEnabled(false);
            chkTiles.setSelected(false);
            chkMapUnits.setEnabled(false);
            chkMapUnits.setSelected(false);
            chkWorldCharacteristics.setEnabled(false);
            chkWorldCharacteristics.setSelected(false);
            chkWorldMap.setEnabled(false);
            chkWorldMap.setSelected(false);
            cmdExportAll2.setEnabled(false);
            cmdExportNone2.setEnabled(false);
        }
    }
}
