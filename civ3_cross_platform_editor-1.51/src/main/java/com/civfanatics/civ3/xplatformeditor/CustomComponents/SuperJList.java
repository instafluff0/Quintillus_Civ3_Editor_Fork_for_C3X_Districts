/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.xplatformeditor.CustomComponents;

import com.civfanatics.civ3.xplatformeditor.EditorTab;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

/**
 * REQUIREMENT: The model must be a SuperListModel.  You don't go mixing Super
 * with un-super.
 * 
 * July 2, 2012 - This still needs some work before it's plug-n-play.
 * Namely, I'm getting some weird stuffs happening when adding new civs, it's
 * always selecting one.
 * I think this is due to something bugged in SuperListModel's alphabetizing
 * code.
 * 
 * @date November 12, 2011
 * @author Andrew
 */
public class SuperJList extends JList{
    
    EditorTab host;
    JPopupMenu popUp = new JPopupMenu();
    JMenuItem delete;
    JMenuItem add;
    JMenuItem rename;
    JMenuItem copy;
    String typeOfItem;
    
    public SuperJList(EditorTab host, String typeOfItem)
    {
        this(host, typeOfItem, true, true);
    }
    
    public SuperJList(EditorTab host, String typeOfItem, boolean deleteable)
    {
        this(host,typeOfItem,deleteable,true);
    }
    
    public SuperJList(EditorTab host, String typeOfItem, boolean deleteable, boolean addable)
    {
        this(host,typeOfItem,deleteable, addable, false);
    }
    
    public SuperJList(EditorTab host, String typeOfItem, boolean deleteable, boolean addable, boolean copyable)
    {
        super();    //because this, like, IS super!!!
        this.setSelectionMode(SINGLE_SELECTION);
        this.host = host;
        this.typeOfItem = typeOfItem;
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                checkForPopUp(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                checkForPopUp(evt);
            }
        });     
        
        //Add the items to the popup
        delete = new JMenuItem("Delete");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteAction();
                //TODO after testing the add
            }
        });
        add = new JMenuItem("Add");
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAction();
            }
        });
        rename = new JMenuItem("Rename");
        rename.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renameAction();
            }
        });
        copy = new JMenuItem("Copy");
        copy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                copyAction();
            }
        });
        if (deleteable)
            popUp.add(delete);
        if (addable)
            popUp.add(add);
        popUp.add(rename);
        if (copyable)
            popUp.add(copy);
    }
    
    private void checkForPopUp(MouseEvent evt)
    {
        if (evt.isPopupTrigger())
        {
            this.setSelectedIndex(this.locationToIndex(new java.awt.Point(evt.getX(), evt.getY())));
            java.awt.Component component = evt.getComponent();
            int x = evt.getX();
            int y = evt.getY();
            popUp.show(component, x, y);
        }
    }
    
    /**
     * The JList will handle getting the name, checking for Cancel, and adding
     * this item to the model.  It will also set the selected index.  Anything
     * else will be done by calling out to the host.
     * 
     * TODO: Set the selected index correctly when alphabetized.
     */
    private boolean addAction()
    {
        String name = JOptionPane.showInputDialog("Please choose a name for the new " + typeOfItem);
        if (name == null) {
            return false;
        }
                
        //Tell the tab that we are hosted in to do anything special it needs to do
        //07/02/12 Moved this before setting the index b/c setting the index was causing the tabs to look for new items (buildings, etc.) that weren't
        //added to the BIQ yet
        //08/29/24 Moved before addIndexedItem so the child tab can reject the addition
        boolean hostResult = host.addItem(name);
        if (hostResult == false) {
            return false;
        }
        
        ((SuperListModel)this.getModel()).addIndexedElement(name);
        
        //TODO: extensive testing
        
        //Only NOW update the index
        this.setSelectedIndex(this.getModel().getSize() - 1);
        return true;
    }
    
    /**
     * It looks as if the "delete" option will be very complicated, and there
     * may be little in common between tabs.  Thus, for now, we are leaving that
     * to the hosts to implement.
     */
    private void deleteAction()
    {
        host.deleteAction();
    }
    
    /**
     * 
     */
    private void renameAction()
    {
        String currentName = this.getSelectedValue().toString();
        String name = JOptionPane.showInputDialog("Please choose a new name for your " + typeOfItem, currentName);
        if (name == null)
        {
            //buildingList.toggle();
            return;
        }
        else if (name.trim().equals(""))
        {
            return;
        }
        int index = this.getSelectedIndex();
        index = ((SuperListModel)this.getModel()).getTrueIndex(index);
        ((SuperListModel)this.getModel()).set(index,name);
        
        host.renameBIQElement(index, name);
    }
    
    private void copyAction() {
        String name = JOptionPane.showInputDialog("Please enter a name for the copied  " + typeOfItem);
        if (name == null || name.trim().equals("")) {
            return;
        }
        //Not sure all this is necessary since alphabetizing isn't on, but to be safe...
        int index = this.getSelectedIndex();
        index = ((SuperListModel)this.getModel()).getTrueIndex(index);
        
        ((SuperListModel)this.getModel()).addIndexedElement(name);
        
        //Tell the tab that we are hosted in to do anything special it needs to do.
        host.copyItem(name, index);
        
        //Update the index
        this.setSelectedIndex(this.getModel().getSize() - 1);
    }
}
