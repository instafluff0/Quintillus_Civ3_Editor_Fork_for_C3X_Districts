/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.civfanatics.civ3.xplatformeditor.CustomComponents;

import java.awt.Component;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Andrew
 */
public class JSpinnerInTableRenderer extends JSpinner implements TableCellRenderer{
    public JSpinnerInTableRenderer()
    {
        super();
        setModel(new SpinnerNumberModel(1, 1, 8192, 1));
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable list,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean cellHasFocus,
                                                   int row,
                                                   int column) {
        int x = (Integer)value;
        setValue(x);
        return this;
    }

}
