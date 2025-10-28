/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.xplatformeditor.tabs.map;

import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJTextField;
import com.civfanatics.civ3.xplatformeditor.Listeners.FireTriggerDocumentListener;
import com.civfanatics.civ3.xplatformeditor.Main;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * This form lets you create a new, custom map.
 * @author Administrator
 */
public class CustomMapSetupForm extends JFrame{
    JLabel lblMapWidth;
    JLabel lblMapHeight;
    static SuperJTextField txtMapWidth;
    static SuperJTextField txtMapHeight;
    String totalTiles = "Total Tiles: ";
    JLabel lblTotalTiles;
    static JLabel lblTotalTiles2;
    JButton cmdOK;
    JButton cmdCancel;
    FireTriggerDocumentListener ftdl;
    FireTriggerDocumentListener ftdl2;
    static int numTiles = 5000;
    static int width = 100;
    static int height = 100;
    JLabel lblTerrain = new JLabel("Base terrain");
    static JComboBox cmbTerrain = new JComboBox();
    DefaultComboBoxModel mdlTerrain = new DefaultComboBoxModel();
    public CustomMapSetupForm()
    {
        lblMapWidth = new JLabel("Map width");
        lblMapHeight = new JLabel("Map height");
        txtMapWidth = new SuperJTextField("100");
        txtMapHeight = new SuperJTextField("100");
        lblTotalTiles = new JLabel(totalTiles);
        lblTotalTiles2 = new JLabel("5000");
        cmdOK = new JButton("OK");
        cmdCancel = new JButton("Cancel");
        mdlTerrain.addElement("Desert");
        mdlTerrain.addElement("Plains");
        mdlTerrain.addElement("Grassland");
        mdlTerrain.addElement("Tundra");
        mdlTerrain.addElement("Coast");
        mdlTerrain.addElement("Sea");
        mdlTerrain.addElement("Ocean");
        cmbTerrain.setModel(mdlTerrain);
        cmdOK.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                if (width < 16 || height < 16)
                {
                    JOptionPane.showMessageDialog(null, "Please make sure both the width and height are at least 16.", "Too small a map", JOptionPane.ERROR_MESSAGE);
                }
                else if (numTiles > 65536)
                {
                    JOptionPane.showMessageDialog(null, "Sorry, you can have at most 65,536 tiles.", "Too big a map", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    createMap();
                    setVisible(false);
                }
            }
        });
        cmdCancel.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        
        try{
            ftdl = new FireTriggerDocumentListener(txtMapWidth);
            ftdl2 = new FireTriggerDocumentListener(txtMapHeight);
            Class[] argument = new Class[0];
            ftdl.method = CustomMapSetupForm.class.getMethod("updateNumTiles", argument);
            ftdl2.method = CustomMapSetupForm.class.getMethod("updateNumTiles", argument);
        }
        catch(NoSuchMethodException e)
        {
            System.err.println("No such method");
        }
        txtMapWidth.getDocument().addDocumentListener(ftdl);
        txtMapHeight.getDocument().addDocumentListener(ftdl);
        txtMapHeight.addFocusListener(new FocusListener(){
            public void focusLost(FocusEvent e)
            {
                updateNumTiles();
            }
            public void focusGained(FocusEvent e)
            {
                updateNumTiles();
            }
        });
        
        GridLayout gl = new GridLayout(5,2);
        this.setLayout(gl);
        this.add(lblTerrain);
        this.add(cmbTerrain);
        this.add(lblMapWidth);
        this.add(txtMapWidth);
        this.add(lblMapHeight);
        this.add(txtMapHeight);
        this.add(lblTotalTiles);
        this.add(lblTotalTiles2);
        this.add(cmdOK);
        this.add(cmdCancel);
        this.setTitle("Size");
        this.setResizable(false);
        this.pack();
    }
    
    public static void main(String[]args)
    {
        new CustomMapSetupForm().setVisible(true);
    }
    
    public static void updateNumTiles()
    {   
        int one = 0;
        int two = 0;
        try{
            one = txtMapWidth.getInteger();
            two = txtMapHeight.getInteger();
            if (one % 2 == 1)
            {
                one = one + 1;  //oh, correct math!
                txtMapWidth.setText(((Integer)one).toString());
                return;
            }
            if (two % 2 == 1)
            {
                two = two + 1;  //oh, correct math!
                txtMapHeight.setText(((Integer)two).toString());
                return;
            }
        }
        catch(java.lang.NumberFormatException e)
        {
            System.err.println("NFE");
        }   //TODO: Catch the IllegalStateException that can be thrown here when changing the text in the listener.  I'm pretty sure it's harmless, seems to be working as intended without side effects.
        int three = (one * two)/2;
        lblTotalTiles2.setText(((Integer)three).toString());
        numTiles = three;
        width = one;
        height = two;
    }
    
    private static void createMap()
    {
        int terrId = 0;
        if (cmbTerrain.getSelectedIndex() <= 3)
            terrId = cmbTerrain.getSelectedIndex();
        else
            terrId = cmbTerrain.getSelectedIndex() + 7;
        
        Main.biqFile.get(Main.biqIndex).createMap(width, height, terrId);
        
        Main.enableMapTab();
    }
    
}
