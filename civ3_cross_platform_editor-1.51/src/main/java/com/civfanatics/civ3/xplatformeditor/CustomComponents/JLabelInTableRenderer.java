/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.civfanatics.civ3.xplatformeditor.CustomComponents;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Andrew
 */
public class JLabelInTableRenderer extends JLabel implements TableCellRenderer{
    public JLabelInTableRenderer()
    {
        super();
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable list,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean cellHasFocus,
                                                   int row,
                                                   int column) {
        String x = (String)value;
        setText(x);
        return this;
    }

}
