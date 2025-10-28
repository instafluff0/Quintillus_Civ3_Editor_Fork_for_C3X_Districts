
package com.civfanatics.civ3.xplatformeditor.CustomComponents;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class JSpinnerInTableEditor extends DefaultCellEditor {
  protected JSpinner spinner;

  public JSpinnerInTableEditor() {
    super(new JCheckBox());
    spinner = new JSpinner();
    spinner.setOpaque(true);
    spinner.setModel(new SpinnerNumberModel(1, 1, 8192, 1));
    spinner.addChangeListener(new ChangeListener(){
        public void stateChanged(ChangeEvent e)
        {
            //Fire editing stopped will let the table model know that there's an update
            fireEditingStopped();
        }
    });
  }

  public Component getTableCellEditorComponent(JTable table, Object value,
                   boolean isSelected, int row, int column) {
    spinner.setValue((Integer)value);
    return spinner;
  }

  public Object getCellEditorValue() {
    return spinner.getValue();
  }
}
