/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.civfanatics.civ3.xplatformeditor.CustomComponents;

import java.awt.Component;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Andrew
 */
public class ComboBoxInTableRenderer extends JComboBox implements TableCellRenderer{
    public ComboBoxInTableRenderer(DefaultComboBoxModel model)
    {
        super();
        setOpaque(true);
        setModel(model);
    }

    public Component getTableCellRendererComponent(JTable list,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean cellHasFocus,
                                                   int row,
                                                   int column) {
        int x = (Integer)value;
        setSelectedIndex(x);
        return this;
    }

}
