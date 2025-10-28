/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.xplatformeditor.CustomComponents;

import javax.swing.JTextField;

/**
 * This is an improved SuperJTextField.  It primarily adds the ability to add value
 * filters and handle them gracefully, so you can avoid unpleasant problems
 * such as NumberFormatExceptions with less boilerplate code.
 * @author Administrator
 */
public class SuperJTextField extends JTextField{
    int PLAIN_TEXT = 0;
    int INTEGER = 1;
    int mode;
    
    String description;
    
    int intVal;
    public SuperJTextField()
    {
        super();
    }
    /**
     * {@inheritDoc}
     */
    public SuperJTextField(String text)
    {
        super(text);
    }
    
    @Override
    public void setText(String t)
    {
        super.setText(t);
        if (mode == INTEGER)
        {
            try{
                intVal=Integer.valueOf(t);
            }
            catch(NumberFormatException e)
            {
                //don't change intValue, but don't blow up, either
            }
        }
    }
    
    /**
     * Should only be called if mode is Integer.
     * Does not check the mode.
     * @return 
     */
    public int getInteger()
    {
        int newVal;
        try{
            newVal = Integer.valueOf(getText());
        }
        catch(NumberFormatException e)
        {
            if (getText().equals(""))
                newVal = 0;
            else
                newVal = intVal;
        }
        intVal = newVal;
        return intVal;
    }
    
    /**
     * Returns the specific type that is in this text field.
     * Less efficient than calling the specific type, but will always work.
     * @return 
     */
    public Object getValue()
    {
        if (mode == INTEGER)
            return getInteger();
        return getText();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
