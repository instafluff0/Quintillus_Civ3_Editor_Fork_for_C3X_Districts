
package com.civfanatics.civ3.xplatformeditor.CustomComponents;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;


public class ButtonInTableEditor extends DefaultCellEditor {
  protected JButton button;

  /**
   * The constructor.  Creates the new button, makes it opaque, and adds a dummy
   * listener.  Later this editor will be modified to remove the unit(s).
   */
  public ButtonInTableEditor() {
    super(new JCheckBox());
    button = new JButton();
    button.setOpaque(true);
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        fireEditingStopped();
      }
    });
  }

  public Component getTableCellEditorComponent(JTable table, Object value,
                   boolean isSelected, int row, int column) {
    button.setText((String)value);
    return button;
  }

  public Object getCellEditorValue() {
    return button.getText();
  }

  //We can also override stopCellEditing and fireEditingStopped if need be;
  //otherwise, they'll just super.
}
