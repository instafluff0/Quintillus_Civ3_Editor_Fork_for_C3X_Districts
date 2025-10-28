package com.civfanatics.civ3.xplatformeditor.Listeners;

import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJTextField;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.apache.log4j.Logger;
/**
 * A LengthDocumentListener is a DocumentListener that makes sure there are no
 * more than <i>i</i> characters in a SuperJTextField.  If there are too many
 * characters, the font color of that SuperJTextField changes to red.  Once
 * there are again sufficiently few characters, the font color returns to black.
 *
 * @author Andrew
 */
public class LengthDocumentListener implements DocumentListener{
    Logger logger = Logger.getLogger(this.getClass());
    //max length should not include the null terminator
    int maxLength;
    JTextComponent txt;
    /**
     * Constructor.
     *
     * @param maxLength - The maximum number of characters permissable.
     * @param txt - The SuperJTextField that is affected.
     */
    public LengthDocumentListener(int maxLength, JTextComponent txt)
    {
        this.maxLength = maxLength;
        this.txt = txt;
    }
    
    public void changedUpdate(DocumentEvent e) {
        if (logger.isTraceEnabled())
            logger.trace("Change detected");
    }
    public void removeUpdate(DocumentEvent e) {
        if (logger.isTraceEnabled())
            logger.trace("Removal detected");
        checkLength();
    }
    public void insertUpdate(DocumentEvent e) {
        if (logger.isTraceEnabled())
            logger.trace("Insertion detected");
        checkLength();
    }
    /**
     * Allows for the manual trigger of a value check.
     * Would be useful in the event that the thresholds changed.
     */
    public void checkLength()
    {
        String s = txt.getText();
        if (logger.isTraceEnabled())
            logger.trace(s);
        if (s.length() > maxLength)
            badValue();
        else
            goodValue();
    }

    private void badValue()
    {
        txt.setForeground(Color.red);
    }

    private void goodValue()
    {
        Color systemColor = SystemColor.textText;
        txt.setForeground(systemColor);
    }
}
