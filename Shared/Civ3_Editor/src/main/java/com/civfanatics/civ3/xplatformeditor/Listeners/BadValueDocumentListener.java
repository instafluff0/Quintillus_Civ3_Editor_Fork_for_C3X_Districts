package com.civfanatics.civ3.xplatformeditor.Listeners;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Quintillus
 */
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJTextField;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.apache.log4j.Logger;
/**
 * A BadValueDocumentListener is a DocumentListener that makes sure the value in
 * a SuperJTextField is between some minimum and maximum values.  If it is not (or it
 * is not a valid number), the font color of that SuperJTextField changes to red.  Once
 * the value is valid again, the font color returns to black.
 * 
 * @author Andrew
 */
public class BadValueDocumentListener implements DocumentListener{
    int maxValue;
    int minValue;
    SuperJTextField txt;
    Logger logger = Logger.getLogger(this.getClass());
    boolean isNull;
    boolean isValid;
    /**
     * Minimal constructor.  With this constructor, the minimum value is set to
     * zero.
     *
     * @param maxValue - The maximum permissable value of the SuperJTextField
     * @param txt - The SuperJTextField on which this listener listens.
     */
    public BadValueDocumentListener(int maxValue, SuperJTextField txt)
    {
        this(maxValue, 0, txt);
    }
    /**
     * Full constructor.
     *
     * @param maxValue - The minimal permissable value of the SuperJTextField
     * @param minValue - The minimal permissable value of the SuperJTextField
     * @param txt - The SuperJTextField on which this listener listens.
     */
    public BadValueDocumentListener(int maxValue, int minValue, SuperJTextField txt)
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
            return;
        }
        if (intVal > maxValue || intVal < minValue)
            badValue();
        else
            goodValue();
        
    }

    private void badValue()
    {
        txt.setForeground(Color.red);
        isValid = false;
        if (txt.getText().equals(""))
            isNull=true;
    }

    private void goodValue()
    {
        Color systemColor = SystemColor.textText;
        txt.setForeground(systemColor);
        isValid = false;
        isNull = true;
    }
}
