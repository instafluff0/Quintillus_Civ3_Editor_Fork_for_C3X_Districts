/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.xplatformeditor;

import com.civfanatics.civ3.biqFile.PRTO;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

/**
 *
 * @author Andrew
 */
public class UnitSelectionWindow extends JDialog {
    
    GridBagConstraints gbc = new GridBagConstraints();
    private final int MAX_WIDTH = 6;
    private final BufferedImage units32;
    private BufferedImage currentIcon;
    int currentIconIndex = 0;
    private final BufferedImage[][] unitIcons;
    private int maxInRow = 0;
    IconPanel units32Panel = new IconPanel();
    IconPanel currentIconPanel = new IconPanel();
    JButton cmdCancel = new JButton("Cancel");
    JButton cmdApply = new JButton("Apply");
    PRTO unit;
    
    Thread animator;    //thread for "animating" the units32 area
    
    public UnitSelectionWindow(BufferedImage units32, BufferedImage currentIcon, BufferedImage[][] unitIcons, PRTO unit) {
        super();
        this.units32 = units32;
        this.currentIcon = currentIcon;
        this.unitIcons = unitIcons;
        maxInRow = (units32.getWidth() - 1)/33;
        this.unit = unit;
    }
    
    public void initComponents() {
        
        this.setTitle("Select Unit Icon");
        this.setIconImage(Main.icon);
        this.setSize(500, 900);
        
        this.setLayout(new GridBagLayout());
        
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.1;
        gbc.weighty = 0;
        
        JTextArea txtInstructions = new JTextArea("Select the icon for the unit here.  Click on an icon to choose it, and then click Apply\n to confirm your choice, or Cancel to revert to the icon selected when this window\n was opened.");
        txtInstructions.setEditable(false);
        txtInstructions.setRows(2);
        txtInstructions.setWrapStyleWord(true);
        
        gbc.gridwidth = MAX_WIDTH;
        this.add(txtInstructions, gbc);
        
        gbc.weighty = 0.01;
        JLabel lblCurrentIcon = new JLabel("Current icon:");
        gbc.gridwidth = 1;
        gbc.gridy+=2;
        gbc.gridx = 1;
        this.add(lblCurrentIcon, gbc);
        
        gbc.gridx++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(5, 5, 5, 5);
        currentIconPanel.setMinimumSize(new Dimension(33, 33));
        currentIconPanel.setPreferredSize(new Dimension(33, 33));
        currentIconPanel.setBorder(new LineBorder(Color.BLACK));
        this.add(currentIconPanel, gbc);
                
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 6;
        gbc.weighty = 1;
        JScrollPane scrUnits = new JScrollPane(units32Panel);
        scrUnits.setPreferredSize(new Dimension(525, 600));
        this.add(scrUnits, gbc);
        
        gbc.gridy++;
        gbc.gridx = 4;
        gbc.gridwidth = 1;
        gbc.weighty = 0.01;
        this.add(cmdApply, gbc);
        
        gbc.gridx++;
        this.add(cmdCancel, gbc);
        
        cmdApply.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                unit.setIconIndex(currentIconIndex);
                closeWindow();
            }
        });
        
        cmdCancel.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                closeWindow();
            }
        });
        
        units32Panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //TODO: Determine if there's special code for "this is the only true icon sheet.  all others are a mistake or a delusion."
                //Probably not?  Maybe determine that there aren't enough pixels for a full row.
                int x = e.getX() - 1;
                int y = e.getY() - 1;
                //Determine the position
                //Formula: Subtract one for the left border.  Divide by 33 since each image (including the right border)
                //takes up 33 pixels (32 + 1 for border).  Ditch if modulus is 32 (border).
                //Same for y.
                int xDiv33 = x/33;
                int xMod33 = x%33;
                int yDiv33 = y/33;
                int yMod33 = y%33;
                if (xMod33 == 32 || yMod33 == 32) {
                    //clicked border
                    return;
                }
                else {
                    selectIcon(xDiv33, yDiv33);
                }
            }
        });
        this.setLocationRelativeTo(null);
    }
    
    private void closeWindow() {
        this.dispose();
    }
    
    private void selectIcon(int xGrid, int yGrid) {
        //convert to index in images
        if (xGrid < unitIcons.length && yGrid < unitIcons[0].length) {
            currentIcon = unitIcons[xGrid][yGrid];
            currentIconIndex = yGrid * this.maxInRow + xGrid;
            drawCurrentIcon();
        }
    }
    
    public void drawStuff() {
        drawCurrentIcon();
        //Draw the units32 PCX
        units32Panel.setPreferredSize(new Dimension(500, units32.getHeight()));
        units32Panel.setVisible(true);
        units32Panel.setImage(units32);
        Graphics panelGraphics = units32Panel.getGraphics();
        units32Panel.update(panelGraphics);
        panelGraphics.dispose();
    }
    
    private void drawCurrentIcon() {
        currentIconPanel.setVisible(true);
        currentIconPanel.setImage(currentIcon);
        Graphics iconGraphics = currentIconPanel.getGraphics();
        currentIconPanel.update(iconGraphics);
        iconGraphics.dispose();        
    }
}
