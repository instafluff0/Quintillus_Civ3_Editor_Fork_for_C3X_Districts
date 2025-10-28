/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.xplatformeditor.tabs.civilization;

import com.civfanatics.civ3.xplatformeditor.imageSupport.PCXFilter;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Andrew
 */
public class BalanceAdjuster extends JDialog
{
    JLabel lblDescription = new JLabel("<html>You can adjust many colors of a palette at once by adjusting the saturation.<br>Changing the hue for the first color will change it for all<br>civ-specific colors by the same amount, thus making it easy to change<br>the civ's color from, for example, red-focused to yellow-focused.<br>A high balance is brighter, whereas a low balance is darker.</html>");
    JLabel lblColor = new JLabel();
    JSlider sldColor = new JSlider(0, 99);
    JLabel lblCurrent = new JLabel("Color: " );
    JLabel lblAdjust = new JLabel("Adjust: ");
    JButton cmdAccept = new JButton("Accept");
    JButton cmdCancel = new JButton("Cancel");
    
    int hue;
    int saturation;
    int balance;
    
    boolean okay = false;
    boolean closed = false;
    
    ColorPanel parent = null;
    
    public BalanceAdjuster(int hue, int saturation, int balance, ColorPanel c)
    {
        //javax.swing.colorchooser.ColorChooserComponentFactory
        this.parent= c;
        this.setTitle("Civ Color Hue Adjuster");
        this.hue = hue;
        this.saturation = saturation;
        this.balance = balance;
        lblColor.setOpaque(true);
        updateColor();
        sldColor.setValue(balance);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        sldColor.addChangeListener(new ChangeListener(){

            public void stateChanged(ChangeEvent e)
            {
                sliderChangedAction();
            }
            
        });
        
        cmdAccept.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                close(true);
            }
        });
        
        cmdCancel.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                close(false);
            }
        });
        
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.gridwidth = 4;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        this.add(lblDescription, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy++;
        this.add(lblCurrent, gbc);
        gbc.gridy++;
        this.add(lblAdjust, gbc);
        
        gbc.gridx++;
        gbc.gridwidth = 3;
        gbc.gridy++;
        gbc.weightx = 0.9;
        gbc.gridy = 1;
        this.add(lblColor, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(0,0,0,0);
        sldColor.setPaintTrack(false);
        this.add(sldColor, gbc);
        
        
        gbc.gridy++;
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        this.add(cmdAccept, gbc);
        gbc.gridx++;
        this.add(cmdCancel, gbc);
        
        
        this.pack();
    }
    
    private void sliderChangedAction()
    {
        balance = sldColor.getValue();
        updateColor();
    }
    
    private void updateColor()
    {
        int rgb = Color.HSBtoRGB(hue/360.0F, saturation/100.0F, balance/100.0F);
        Color hsb = new Color(rgb);
        lblColor.setBackground(hsb);
    }
    
    private void close(boolean save)
    {
        okay = save;
        this.setVisible(false);
        closed = true;
        parent.keepGoingBalance(save, balance);
    }
}
