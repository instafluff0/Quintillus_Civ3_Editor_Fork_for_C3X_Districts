/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.xplatformeditor.tabs.map;

import com.civfanatics.civ3.biqFile.CITY;
import com.civfanatics.civ3.biqFile.IO;
import com.civfanatics.civ3.xplatformeditor.Main;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Administrator
 */
public class frmBuildingsInCities extends JFrame{
    
    JList lstBuildings = new JList();
    JList lstCivilizations = new JList();
    JList lstPlayers = new JList();
    JList lstAffected = new JList();
    
    JScrollPane jsp1 = new JScrollPane(lstBuildings);
    JScrollPane jsp2 = new JScrollPane(lstCivilizations);
    JScrollPane jsp3 = new JScrollPane(lstPlayers);
    JScrollPane jsp4 = new JScrollPane(lstAffected);
    
    JLabel lblCitySize = new JLabel("Min size: ");
    JSpinner spnrCitySize = new JSpinner();
    JLabel lblMaxCitySize = new JLabel("Max size: ");
    JSpinner spnrMaxCitySize = new JSpinner();
    int minSize = 1;
    int maxSize = 99;
    
    SpinnerModel spnrMin = new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1);
    SpinnerModel spnrMax = new SpinnerNumberModel(99, 1, Integer.MAX_VALUE, 1);
    
    DefaultListModel mdlBLDG = new DefaultListModel();
    DefaultListModel mdlCIV = new DefaultListModel();
    DefaultListModel mdlPLYR = new DefaultListModel();
    DefaultListModel mdlAffectedCities = new DefaultListModel();
    
    JCheckBox chkCoastal = new JCheckBox("Coastal only");
    JCheckBox chkLeaveExisting = new JCheckBox("Leave existing buildings");
    
    JLabel lblInstructions = new JLabel("Instructions");
    JButton cmdOK = new JButton("Add Buildings");
    JButton cmdCancel = new JButton("Cancel");
    
    IO biqFile;
    
    public frmBuildingsInCities(IO biq)
    {
        setTitle("Add Buildings to Many Cities at Once");
        setIconImage(Main.icon);
        
        biqFile = biq;
        
        lblInstructions.setText("<html>This utility allows you to add buildings to many cities at once.  You can use the multi-select lists to choose buildings, and ownership of cities, as well as the coastal check box and<br>city size parameters.  The Affected City lists helps double-check which cities are affected.  Then Click OK to apply the changes.</html>");
        
        GridBagLayout gbl = new GridBagLayout();
        this.setLayout(gbl);
        GridBagConstraints g = new GridBagConstraints();
        
        for (int i = 0; i < biq.buildings.size(); i++)
        {
            mdlBLDG.addElement(biq.buildings.get(i).getName());
        }
        for (int i = 0; i < biq.civilization.size(); i++)
        {
            mdlCIV.addElement(biq.civilization.get(i).getName());
        }
        if (biq.hasCustomPlayerData())
        {
            for (int i = 0; i < biq.player.size(); i++)
            {
                mdlPLYR.addElement("Player " + (i + 1));
            }
        }
        lstBuildings.setModel(mdlBLDG);
        lstPlayers.setModel(mdlPLYR);
        lstCivilizations.setModel(mdlCIV);
        lstAffected.setEnabled(false);
        lstAffected.setModel(mdlAffectedCities);
        
        g.fill = GridBagConstraints.BOTH;
        g.insets = new Insets(2, 2, 2, 2);
        
        g.gridwidth = 8;
        g.gridheight = 1;
        g.gridx = 0;
        g.gridy = 0;
        g.weightx = 0.5;
        g.weighty = 0.05;
        getContentPane().add(lblInstructions, g);
        
        spnrCitySize.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e)
            {
                minSize = (Integer)spnrCitySize.getModel().getValue();
                selectionChanged();
            }
        });
        
        spnrMaxCitySize.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e)
            {
                maxSize = (Integer)spnrMaxCitySize.getModel().getValue();
                selectionChanged();
            }
        });
        
        spnrCitySize.setModel(spnrMin);
        spnrMaxCitySize.setModel(spnrMax);
        g.gridy++;
        g.gridwidth = 2;
        g.fill = GridBagConstraints.HORIZONTAL;
        getContentPane().add(chkCoastal, g);
        g.gridx+=2;
        g.gridwidth = 1;
        getContentPane().add(lblCitySize, g);
        g.gridx+=1;
        getContentPane().add(spnrCitySize, g);
        g.gridx+=1;
        g.gridwidth = 1;
        getContentPane().add(lblMaxCitySize, g);
        g.gridx+=1;
        g.gridwidth = 1;
        getContentPane().add(spnrMaxCitySize, g);
        
        JLabel lblBuildings = new JLabel("Add Buildings");
        JLabel lblCivs = new JLabel("Owned By Civ");
        JLabel lblPlayers = new JLabel("Owned by Player");
        JLabel lblAffected = new JLabel("Affected Cities");
        
        g.weighty = 0;
        g.gridx=0;
        g.gridy++;
        g.weighty=0.05;
        g.gridwidth=2;
        getContentPane().add(lblBuildings, g);
        g.gridx+=2;
        getContentPane().add(lblCivs, g);
        g.gridx+=2; 
        getContentPane().add(lblPlayers, g);
        g.gridx+=2; 
        getContentPane().add(lblAffected, g);
        
        g.gridx=0;
        g.gridy++;
        g.weighty = 0.9;
        g.gridwidth = 2;
        g.fill = GridBagConstraints.BOTH;
        getContentPane().add(jsp1, g);
        g.gridx+=2;
        getContentPane().add(jsp2, g);
        g.gridx+=2;
        getContentPane().add(jsp3, g);
        g.gridx+=2;
        getContentPane().add(jsp4, g);
        
        g.weighty = 0;
        g.gridx=0;
        g.gridy++;
        g.weighty=0.05;
        g.gridwidth=4;
        chkLeaveExisting.setSelected(true);
        getContentPane().add(chkLeaveExisting, g);
        g.gridwidth = 1;
        g.gridx+=6;
        getContentPane().add(cmdCancel, g);
        g.gridx++;
        g.weightx = 0.75;
        getContentPane().add(cmdOK, g);
        
        cmdCancel.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent e)
           {
               cmdCancelAction();
           }
        });
        
        cmdOK.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent e)
           {
               cmdOKAction();
           }
        });
        
        lstCivilizations.addListSelectionListener(new ListSelectionListener (){
            public void valueChanged(ListSelectionEvent e)
            {
                if (!e.getValueIsAdjusting())
                {
                    selectionChanged();
                }
            }
        });
        
        lstPlayers.addListSelectionListener(new ListSelectionListener (){
            public void valueChanged(ListSelectionEvent e)
            {
                if (!e.getValueIsAdjusting())
                {
                    selectionChanged();
                }
            }
        });
        
        chkCoastal.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                selectionChanged();
            }
        });
        
        pack();
    }
    
    private void cmdOKAction()
    {
        List<CITY>affected = findAffectedCities();
        int[]buildings = lstBuildings.getSelectedIndices();
        
        for (CITY city : affected)
        {
            if (!chkLeaveExisting.isSelected())
            {
                city.removeAllBuildings();
                for (int i = 0; i < buildings.length; i++)
                    city.addBuilding(buildings[i]);
            }
            else
            {
                for (int i = 0; i < buildings.length; i++)
                {
                    if (!city.hasBuilding(buildings[i]))
                    {
                        city.addBuilding(buildings[i]);
                    }
                }
            }
        }
        JOptionPane.showMessageDialog(this, "Applied changes", "Buildings Added", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void cmdCancelAction()
    {
        this.dispose();
    }
    
    private List<CITY> findAffectedCities()
    {
        List<CITY>affected = new LinkedList<CITY>();
        
        int[]civs = lstCivilizations.getSelectedIndices();
        Arrays.sort(civs);
        int[]players = lstPlayers.getSelectedIndices();
        Arrays.sort(players);
        boolean mustBeCoastal = chkCoastal.isSelected();
        
        for (CITY city : biqFile.city)
        {
            //Boolean first for short-circuiting, isCoastal, is much more intensive
            if (!mustBeCoastal || city.isCoastal())
            {
                if (city.getSize() >= minSize && city.getSize() <= maxSize)
                {
                    if (city.getOwnerType() == CITY.OWNER_CIV)
                    {
                        if (Arrays.binarySearch(civs, city.getOwner()) >= 0)
                        {
                            affected.add(city);
                        }
                    }
                    else if (city.getOwner() == CITY.OWNER_PLAYER)
                    {
                        if (Arrays.binarySearch(players, city.getOwner()) >= 0)
                        {
                            affected.add(city);
                        }
                    }
                }
            }
        }
        
        return affected;
    }
    
    public static  void main(String[]args)
    {
        //Intentionally passing null even though the parameter shouldn't be null
        //Only to be used for testing the GUI
        new frmBuildingsInCities(null);
    }
    
    private void selectionChanged()
    {
        List<CITY> affected = findAffectedCities();
        mdlAffectedCities.clear();
        for (CITY c : affected)
        {
            mdlAffectedCities.addElement(c.getName());
        }
    }
}
