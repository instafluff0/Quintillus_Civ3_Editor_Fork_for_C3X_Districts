
package com.civfanatics.civ3.xplatformeditor.CustomComponents;

import com.civfanatics.civ3.xplatformeditor.undoRedo.UndoStack;
import java.awt.event.ActionEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * A custom edit menu.  It's able to interact with an UndoStack that keeps track
 * of things that can be redone or undone.
 * @author Quintillus
 */
public class EditMenu extends JMenu {
    
    public JMenuItem undo = new JMenuItem("Undo");
    JMenuItem redo = new JMenuItem("Redo");
    public UndoStack undoStack;
    
    public EditMenu(String name) {
        super(name);
        this.add(undo);
        
        undo.addActionListener((ActionEvent e) -> {
            undoAction();
        });
    }
    
    public void undoAction() {
        undoStack.pop();
    }
    
}
