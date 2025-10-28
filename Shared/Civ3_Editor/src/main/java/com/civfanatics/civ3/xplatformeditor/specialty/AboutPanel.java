package com.civfanatics.civ3.xplatformeditor.specialty;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.civfanatics.civ3.xplatformeditor.Main;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Displays information about the program.
 *
 * @author Andrew
 */
public class AboutPanel extends JFrame{
    JLabel txtDescription = new JLabel();
    /**
     * The constructor.  Does everything.
     */
    public AboutPanel()
    {
        setIconImage(Main.icon);
        setTitle("About");
        setPreferredSize(new Dimension(340, 120));
        GridBagLayout layout = new GridBagLayout();
        getContentPane().setLayout(layout);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.BOTH;
        g.gridx = 0;
        g.gridy = 0;
        g.gridheight = 1;
        g.gridwidth = 1;
        g.weighty = 0.3;
        g.weightx = 0.1;
        txtDescription.setText("<html><b>Conquests Editor " + Main.VERSION + "</b><br>By Quintillus<br>https://forums.civfanatics.com/showthread.php?t=377188<br>Developed 2009 - 2023<br></html>");
        this.add(txtDescription, g);
        g.weighty = 0.1;
        g.fill = GridBagConstraints.NONE;
        JButton okay = new JButton("OK");
        okay.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                setVisible(false);
            }
        });
        g.gridy++;
        add(okay, g);
        pack();
    }

    /**
     * Allows standalone running of the About window.
     * @param args Not used.
     */
    public static void main(String[]args)
    {
        new AboutPanel().setVisible(true);
    }
}
