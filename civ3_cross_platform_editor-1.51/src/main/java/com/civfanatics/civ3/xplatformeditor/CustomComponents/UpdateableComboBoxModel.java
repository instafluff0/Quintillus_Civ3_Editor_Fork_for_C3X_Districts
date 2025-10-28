/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.xplatformeditor.CustomComponents;

import java.io.Serializable;
import java.util.Vector;
import javax.swing.AbstractListModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.MutableComboBoxModel;

/**
 * Implements a ComboBoxModel that allows updating elements.  Most of this is
 * copied from DefaultComboBoxModel (from JDK 1.8); all I really wanted to do
 * was allow updating a method, but the variables were package-level private,
 * and removing/updating the object caused the selected index to change when
 * the list was active, which would be a bug.
 * @author Andrew
 */
public class UpdateableComboBoxModel<E> extends AbstractListModel<E> implements MutableComboBoxModel<E>, Serializable {
    Vector<E> objects = new Vector<E>();
    Object selectedObject;
    
    /**
     * Allows you to update the object at <i>index</i> to now be <i>object</i>.
     * @param index
     * @param object 
     */
    public void set(int index, E object) {
        objects.set(index, object);
    }

    @Override
    public int getSize() {
        return objects.size();
    }

    @Override
    public E getElementAt(int index) {
        if ( index >= 0 && index < objects.size() )
            return objects.elementAt(index);
        else
            return null;
    }

    @Override
    public void addElement(E anObject) {
        objects.addElement(anObject);
        fireIntervalAdded(this,objects.size()-1, objects.size()-1);
        if ( objects.size() == 1 && selectedObject == null && anObject != null ) {
            setSelectedItem( anObject );
        }
    }

    @Override
    public void removeElement(Object anObject) {
        int index = objects.indexOf(anObject);
        if ( index != -1 ) {
            removeElementAt(index);
        }
    }

    @Override
    public void insertElementAt(E anObject, int index) {
        objects.insertElementAt(anObject,index);
        fireIntervalAdded(this, index, index);
    }

    @Override
    public void removeElementAt(int index) {
        if ( getElementAt( index ) == selectedObject ) {
            if ( index == 0 ) {
                setSelectedItem( getSize() == 1 ? null : getElementAt( index + 1 ) );
            }
            else {
                setSelectedItem( getElementAt( index - 1 ) );
            }
        }

        objects.removeElementAt(index);

        fireIntervalRemoved(this, index, index);
    }

    @Override
    public void setSelectedItem(Object anObject) {
        if ((selectedObject != null && !selectedObject.equals( anObject )) ||
            selectedObject == null && anObject != null) {
            selectedObject = anObject;
            fireContentsChanged(this, -1, -1);
        }
    }

    @Override
    public Object getSelectedItem() {
        return selectedObject;
    }
    
    /**
     * Empties the list.
     */
    public void removeAllElements() {
        if ( objects.size() > 0 ) {
            int firstIndex = 0;
            int lastIndex = objects.size() - 1;
            objects.removeAllElements();
            selectedObject = null;
            fireIntervalRemoved(this, firstIndex, lastIndex);
        } else {
            selectedObject = null;
        }
    }
}
