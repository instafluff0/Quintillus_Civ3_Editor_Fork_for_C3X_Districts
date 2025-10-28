package com.civfanatics.civ3.xplatformeditor.Listeners;

import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJTextField;
import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.apache.log4j.Logger;
/**
 * A BadValueDocumentListener is a DocumentListener that makes sure the value in
 * a SuperJTextField is between some minimum and maximum values.  If it is not (or it
 * is not a valid number), the font color of that SuperJTextField changes to red.  Once
 * the value is valid again, the font color returns to black.
 * 
 * Right now this only works with one class.  It'd be nice to generalize this.
 * 
 * @author Andrew
 */
public class FireTriggerDocumentListener implements DocumentListener{
    int maxValue;
    int minValue;
    SuperJTextField txt;
    Logger logger = Logger.getLogger(this.getClass());
    
    public Method method;
       
    /**
     * Minimal constructor.  With this constructor, the minimum value is set to
     * zero.
     *
     * @param maxValue - The maximum permissable value of the SuperJTextField
     * @param txt - The SuperJTextField on which this listener listens.
     */
    public FireTriggerDocumentListener(int maxValue, SuperJTextField txt)
    {
        this(maxValue, 0, txt);
    }
    
    public FireTriggerDocumentListener(SuperJTextField txt)
    {
        this.txt = txt;
    }
    
    /**
     * Full constructor.
     *
     * @param maxValue - The minimal permissable value of the SuperJTextField
     * @param minValue - The minimal permissable value of the SuperJTextField
     * @param txt - The SuperJTextField on which this listener listens.
     */
    public FireTriggerDocumentListener(int maxValue, int minValue, SuperJTextField txt)
    {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.txt = txt;
    }
    public void changedUpdate(DocumentEvent e) {
        if (logger.isTraceEnabled())
            logger.trace("Change detected");
    }
    public void removeUpdate(DocumentEvent e) {
        if (logger.isTraceEnabled())
            logger.trace("Removal detected");
        checkValue();
    }
    public void insertUpdate(DocumentEvent e) {
        if (logger.isTraceEnabled())
            logger.trace("Insertion detected");
        checkValue();
    }
    /**
     * Allows for the manual trigger of a value check.
     * Would be useful in the event that the thresholds changed.
     */
    public void checkValue()
    {
        String text = txt.getText();
        if (text.equals(""))
            return;
        if (logger.isTraceEnabled())
            logger.trace(text);
        int intVal;
        //if there is a non-integer value in an integer text box, we need to catch
        //that and indicate that the value is not valid
        //This could result from legitimate means, ex. "-" could be "invalid" 
        //but be inputted when -1 was the intended value
        try{
            intVal = Integer.valueOf(text);
        }
        catch(java.lang.NumberFormatException e)
        {
            badValue();
        }
        //Commented out on Ides of March 2013 - wasn't doing anything
//        try{
//            method.invoke(null, null);
//        }
//        catch(IllegalAccessException e)
//        {
//            logger.error("IllegalAccessException in checkValue", e);
//        }
//        catch(InvocationTargetException e)
//        {
//            logger.error("InvocationTargetException in checkValue", e);
//        }
    }

    private void badValue()
    {
        txt.setForeground(Color.red);
    }

    private void goodValue()
    {
        txt.setForeground(Color.black);
    }
}
