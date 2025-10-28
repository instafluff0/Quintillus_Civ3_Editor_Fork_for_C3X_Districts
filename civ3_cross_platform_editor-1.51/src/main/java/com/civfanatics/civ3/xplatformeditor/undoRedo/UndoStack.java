/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.xplatformeditor.undoRedo;

import com.civfanatics.civ3.biqFile.BIQSection;
import com.civfanatics.civ3.biqFile.CTZN;
import com.civfanatics.civ3.biqFile.IO;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.EditMenu;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 *
 * @author Andrew
 */
public class UndoStack {
    
    IO biq;
    EditMenu editMenu;
    
    Deque<UndoElement> elements = new ArrayDeque();
    
    public UndoStack(IO biq, EditMenu editMenu) {
        this.biq = biq;
        this.editMenu = editMenu;
        editMenu.undoStack = this;
    }
    
    public void push(BIQSection biqElement, int itemIndex) {
        UndoElement u = new UndoElement();
        u.indexWithinSection = itemIndex;
        u.item = biqElement;
        elements.push(u);
        editMenu.undo.setText("Undo editing " + biqElement.getProperty("name"));
        editMenu.undo.setEnabled(true);
    }
    
    public void pop(){
        UndoElement u = elements.pop();
        if (u.item instanceof CTZN) {
            CTZN citizen = (CTZN)u.item;
            System.out.println("Undoing changes for " + citizen.getName());
            biq.citizens.set(u.indexWithinSection, citizen);
        }
        if (elements.peek() != null) {
            System.out.println("Setting label to " + elements.peek().item.getProperty("name"));
            editMenu.undo.setText("Undo editing " + elements.peek().item.getProperty("name"));
        }
        else {
            editMenu.undo.setText("Undo");
            editMenu.undo.setEnabled(false);
        }
    }
    
    class UndoElement {
        BIQSection item;
        int indexWithinSection;
    }
}
