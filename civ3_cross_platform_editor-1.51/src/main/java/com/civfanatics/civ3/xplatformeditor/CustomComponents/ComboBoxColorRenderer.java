package com.civfanatics.civ3.xplatformeditor.CustomComponents;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Quintillus
 */
import com.civfanatics.civ3.xplatformeditor.Main;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
/**
 * Allows components of a ComboBox to be rendered as unicolor labels without text.
 * This is useful if you want to allow the user to choose from a limited number of
 * colors but don't have the space to display all of them at once.
 * @author Andrew
 */
public class ComboBoxColorRenderer extends JLabel implements ListCellRenderer{
    public java.awt.Color[]color;
    /**
     * The constructor.
     *
     * @param color - An array of java.awt.Color's.  These are the colors that will
     * be used as the backgrounds for the items in the ComboBox.  There should be
     * as many colors as there will be elements in the ComboBox.  Having more colors
     * won't be a problem, but having fewer could cause exceptions.
     */
    public ComboBoxColorRenderer(java.awt.Color[]color)
    {
        setOpaque(true);
        this.color = color.clone();
    }

    /**
     * Sets the color of this item in the combo box to the appropriate color, and then
     * returns it.
     * @param list
     * @param value
     * @param index
     * @param isSelected
     * @param cellHasFocus
     * @return - This component (a JLabel).
     */
    public java.awt.Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        int selectedIndex = ((Integer)value).intValue();
        //note that we display the color regardless of the selected state
        if (index != -1)
        {   
            this.setOpaque(true);
            setBackground(color[selectedIndex]);
            setForeground(color[selectedIndex]);
        }
        //this.setMinimumSize(new java.awt.Dimension(50, 20));
        if (Main.GRAPHICS_ENABLED) {
            this.setText(" ");
        }
        else {
            this.setText("" + selectedIndex);
        }
        return this;
    }
}
