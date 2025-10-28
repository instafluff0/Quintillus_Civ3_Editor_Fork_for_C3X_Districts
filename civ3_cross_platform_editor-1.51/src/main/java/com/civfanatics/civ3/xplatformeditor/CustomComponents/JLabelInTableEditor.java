
package com.civfanatics.civ3.xplatformeditor.CustomComponents;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;


public class JLabelInTableEditor extends DefaultCellEditor {
  protected JLabel label;

  public JLabelInTableEditor() {
    super(new JCheckBox());
    label = new JLabel();
    label.setOpaque(true);
  }

  public Component getTableCellEditorComponent(JTable table, Object value,
                   boolean isSelected, int row, int column) {
    label.setText((String)value);
    return label;
  }

  public Object getCellEditorValue() {
    return label.getText();
  }
}
