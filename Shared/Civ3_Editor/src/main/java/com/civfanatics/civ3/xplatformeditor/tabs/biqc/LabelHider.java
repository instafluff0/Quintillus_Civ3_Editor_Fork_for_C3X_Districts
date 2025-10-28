package com.civfanatics.civ3.xplatformeditor.tabs.biqc;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Quintillus
 */
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.Timer;

public class LabelHider {
    JLabel label;
    public LabelHider(JLabel label)
    {
        this.label = label;
        Timer timer = new Timer(8000, hideLabel);
        timer.start();
    }

    Action hideLabel = new AbstractAction(){
        public void actionPerformed(ActionEvent e)
        {
            label.setVisible(false);
        }
    };
}
