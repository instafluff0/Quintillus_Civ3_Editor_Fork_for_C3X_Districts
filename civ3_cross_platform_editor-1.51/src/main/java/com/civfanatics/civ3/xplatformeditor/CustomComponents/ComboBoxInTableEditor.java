
package com.civfanatics.civ3.xplatformeditor.CustomComponents;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTable;
import org.apache.log4j.Logger;


public class ComboBoxInTableEditor extends DefaultCellEditor {
  protected JComboBox comboBox;
  Logger logger = Logger.getLogger(this.getClass());
  public ComboBoxInTableEditor(DefaultComboBoxModel model) {
    super(new JCheckBox());
    comboBox = new JComboBox();
    comboBox.setOpaque(true);
    comboBox.setModel(model);
    comboBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        fireEditingStopped();
      }
    });
  }

  public Component getTableCellEditorComponent(JTable table, Object value,
                   boolean isSelected, int row, int column) {
        if (logger.isDebugEnabled())
            logger.debug("Num items: " + comboBox.getItemCount());
        comboBox.setSelectedIndex((Integer)value);
        return comboBox;
  }

  public Object getCellEditorValue() {
    return comboBox.getSelectedIndex();
  }
}
