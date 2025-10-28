package com.civfanatics.civ3.xplatformeditor;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Quintillus
 */
import com.civfanatics.civ3.xplatformeditor.specialty.SafetyLevel;
import com.civfanatics.civ3.xplatformeditor.Listeners.BadValueDocumentListener;
import com.civfanatics.civ3.xplatformeditor.Listeners.LengthDocumentListener;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.util.ArrayList;
import java.util.List;
import com.civfanatics.civ3.biqFile.IO;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperJTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.text.JTextComponent;
import org.apache.log4j.Logger;
/**
 * This abstract class represents a generic tab in the editor.  Its purpose is
 * allow common traits to tabs to be generisized, thus decreasing copy-pasting
 * of code (and possible errors), and simplifying logic in the main class.
 *
 * Note that for some tabs, certain actions may not actually do anything - for
 * example, the setSelectedIndex method for the rules tab will not do anything,
 * as there may only be one set of rules.  Nevertheless, the simplification of
 * the abstraction is seen as worth the slight breakage of the abstract class
 * contract.
 *
 * @author Andrew
 */
public abstract class EditorTab extends JPanel{
    protected static final String noneSelected = "None";
    Logger logger = Logger.getLogger(this.getClass());
    public List<JTextComponent>textBoxes;
    public String tabName;
    public IO baselink;
    JList lstType;

    /**
     * Will be set to true when new data is being sent (e.g. a new file is being
     * opened, current file is being switched).  Allows suppression of
     * ActionPerformed actions in this situation.
     */
    public boolean newData = false;
    
    /**
     * Used to control whether this thread should be "active" or "dormant".
     * A tab will be active if the BIQ has relevant data for it.
     * For example, the BLDG tab will only be open if there are custom rules.
     */
    public boolean isActive = true;

    /**
     * Updates the state of the tab.
     * This method should be called whenever:
     *   - The item that is selected is changed
     *   - This tab is no longer the current tab
     *   - The file is saved
     * It stores all tab data to memory (thus saving any changed fields), and then
     * updates the screen based on the new item selected.  It is possible that
     * the new item will be the same one as the one previously selected.
     */
    public abstract void updateTab();

    /**
     * Tells the tab to set item <i>i</i> as the selected item.
     * @param i - The item to be selected.  The first item is i = 0.
     */
    public abstract void setSelectedIndex(int i);

    /**
     * Clears all of the BadValueDocumentListeners on a tab.  This method is
     * called when the safety level is changed and a new set of BadValueDocumentListeners
     * is to be assigned to the SuperJTextFields on a tab.  In the future, it may be more
     * efficient to change the parameters on the BadValueDocumentListeners than to
     * actually create new ones every time.
     */
    protected void clearBadValueDocumentListeners()
    {
        if (textBoxes == null)
            textBoxes = new ArrayList<JTextComponent>();
        for (int h = 0; h < textBoxes.size(); h++)
        {
            javax.swing.event.DocumentListener[]list;
            list = ((javax.swing.text.AbstractDocument)(textBoxes.get(h).getDocument())).getDocumentListeners();
            for (int i = 0; i < list.length; i++)
            {
                if (list[i] instanceof BadValueDocumentListener)
                {
                    if (logger.isDebugEnabled())
                        logger.debug("removing a bad value document listener " + list[i]);
                    textBoxes.get(h).getDocument().removeDocumentListener(list[i]);
                    textBoxes.get(h).setForeground(Color.BLACK);
                }
            }
        }
    }
    /**
     * Clears all of the LengthDocumentListeners on a tab.  This method is
     * called when the safety level is changed and a new set of LengthDocumentListeners
     * is to be assigned to the SuperJTextFields on a tab.  In the future, it may be more
     * efficient to change the parameters on the LengthDocumentListeners than to
     * actually create new ones every time.
     */
    protected void clearLengthDocumentListeners()
    {
        if (textBoxes == null)
            textBoxes = new ArrayList<JTextComponent>();
        for (int h = 0; h < textBoxes.size(); h++)
        {
            javax.swing.event.DocumentListener[]list;
            list = ((javax.swing.text.AbstractDocument)(textBoxes.get(h).getDocument())).getDocumentListeners();
            for (int i = 0; i < list.length; i++)
            {
                if (list[i] instanceof LengthDocumentListener)
                {
                    if (logger.isDebugEnabled())
                        logger.debug("removing a length document listener");
                    textBoxes.get(h).getDocument().removeDocumentListener(list[i]);
                    textBoxes.get(h).setForeground(Color.BLACK);
                }
            }
        }
    }

    /**
     * Convenience method for adding BadValueDocumentListeners
     * This gives two benefits:
     *   - Combines the intialization and document listener adding steps into
     *     one, reducing the chance of forgetting one
     *   - Simplifies the syntax for adding a document listener
     * The tradeoff is the form of the statement is more procedural and less
     * object-oriented; this is seen as a small cost for the benefit.
     * @param maxValue - The maximum value for the BadValueDocumentListener
     * @param minValue - The minimum value for the BadValueDocumentListener
     * @param txt - The SuperJTextField whose values are monitored by this BadValueDocumentListener
     */
    protected void addBadValueDocumentListener(int maxValue, int minValue, SuperJTextField txt)
    {
        initializeTextBox(txt);
        txt.getDocument().addDocumentListener(new BadValueDocumentListener(maxValue, minValue, txt));
    }
        /**
     * Convenience method for adding BadValueDocumentListeners
     * This gives two benefits:
     *   - Combines the intialization and document listener adding steps into
     *     one, reducing the chance of forgetting one
     *   - Simplifies the syntax for adding a document listener
     * The tradeoff is the form of the statement is more procedural and less
     * object-oriented; this is seen as a small cost for the benefit.
     * @param maxValue - The maximum value for the BadValueDocumentListener
     * @param txt - The SuperJTextField whose values are monitored by this BadValueDocumentListener
     */
    protected void addBadValueDocumentListener(int maxValue, SuperJTextField txt)
    {
        initializeTextBox(txt);
        txt.getDocument().addDocumentListener(new BadValueDocumentListener(maxValue, txt));
    }
    /**
     * Convenience method for adding LengthDocumentListener
     * This gives two benefits:
     *   - Combines the intialization and document listener adding steps into
     *     one, reducing the chance of forgetting one
     *   - Simplifies the syntax for adding a document listener
     * The tradeoff is the form of the statement is more procedural and less
     * object-oriented; this is seen as a small cost for the benefit.
     * @param length - The maximum length for the LengthDocumentListener
     * @param txt - The SuperJTextField whose values are monitored by this LengthDocumentListener
     */
    protected void addLengthDocumentListener(int length, JTextComponent txt)
    {
        initializeTextBox(txt);
        txt.getDocument().addDocumentListener(new LengthDocumentListener(length, txt));
    }

    //Initializes this text box into the ArrayList of text boxes on this tab
    //only if it has not already been added to that list.
    private void initializeTextBox(JTextComponent txt)
    {
        //logger.setLevel(Level.DEBUG);
        if (textBoxes == null)
            textBoxes = new ArrayList<>();
        if (!(textBoxes.contains(txt)))
            textBoxes.add(txt);
    }

    /**
     * Removes all limits on length and values for text fields.
     */
    public abstract void setNoLimits();
    /**
     * Sets 'minimal' limits for text fields - in practices, only length limits.
     */
    public abstract void setMinimalLimits();
    /**
     * Sets 'exporatory' limits for text fields - anything not known to cause
     * a problem is allowed.
     */
    public abstract void setExploratoryLimits();
    /**
     * Sets 'safe' limits for text fields - anything known not to cause a problem
     * is allowed.
     */
    public abstract void setSafeLimits();
    /**
     * Sets 'Firaxis' limits for text fields - attempts to match the limits set
     * by Firaxis's editor as closely as possible.
     */
    public abstract void setFiraxisLimits();
    /**
     * Sets 'Firaxis' limits for text fields, and attempts to prevent the user
     * from any other problematic settings they may inadvertantly cause.
     */
    public abstract void setTotalLimits();

    /**
     * Convenience method that allows the setting of the safety level of a tab
     * with one method, rather than having to know the names of all six separate
     * methods.  This also allows this method to be used to set safety levels
     * in the future in case additional safety levels are added.
     *
     * This method is public so that the safety level form can set safety levels
     * on tabs.
     *
     * @param level - The SafetyLevel that is to be set for this tab.
     */
    public void setSafetyLevel(SafetyLevel level)
    {
        if (level.equals(SafetyLevel.None))
            setNoLimits();
        else if (level.equals(SafetyLevel.Minimal))
            setMinimalLimits();
        else if (level.equals(SafetyLevel.Exploratory))
            setExploratoryLimits();
        else if (level.equals(SafetyLevel.Safe))
            setSafeLimits();
        else if (level.equals(SafetyLevel.Firaxis))
            setFiraxisLimits();
        else if (level.equals(SafetyLevel.Total))
            setTotalLimits();
        updateAllListeners();
    }

    /**
     * Calls the checkValue() or checkLength() method on all BadValueDocumentListeners
     * and LengthDocumentListeners (respectively).  This is to be called whenever
     * the safety level is changed, as changing the safety level does not change
     * the value in the SuperJTextFields (as changing the selected index of a tab does).
     * It is called directly by the setSafetyLevel method within EditorTab.java,
     * and thus has private scope.
     */
    private void updateAllListeners()
    {
        for (JTextComponent txt : textBoxes)
        {
            javax.swing.event.DocumentListener[]list;
            list = ((javax.swing.text.AbstractDocument)(txt.getDocument())).getDocumentListeners();
            for (int i = 0; i < list.length; i++)
            {
                if (list[i] instanceof BadValueDocumentListener)
                {
                    ((BadValueDocumentListener)list[i]).checkValue();
                }
                if (list[i] instanceof LengthDocumentListener)
                {
                    ((LengthDocumentListener)list[i]).checkLength();
                }
            }
        }
    }

    /**
     * Sends links to the base IO for tabs that may create new BIQSection instances, which
     * need a link to the base IO in case a comparison is made.  If a comparison
     * is made, a BIQ section looks to the base IO to see if it should export
     * in fancy English or simple BIQ style.  Thus if a new BIQ section instance
     * is created, the creator (the tab) must know where the IO is.
     *
     * This is also useful in the case of the GAME tab, which must modify
     * Strings in the VER section (stored on the base IO).  As Strings are
     * immutable, it cannot directly change them via its reference to their
     * pointer, and must actually change the pointer to the String on the IO
     * tab to make a lasting change.  For details on why this is, see:
     * http://javadude.com/articles/passbyvalue.htm
     *
     * If a tab cannot creates new BIQSections, this functionality is simply
     * irrelevant.
     * @param io - The link to the BIQ file.
     */
    public void sendIOLink(IO io){
        this.baselink = io;
    }

    /**
     * Checks to make sure all the values in text boxes are within the bounds
     * prescribed by the safety level.
     *
     * @return true if all values are within bounds, false otherwise
     */
    public boolean checkBounds(List<String> valuesWithErrors)
    {
        if (textBoxes == null)  //no text boxes to check
            return true;
        boolean returnValue = true;
        for (int i = 0; i < textBoxes.size(); i++)
        {
            if (textBoxes.get(i).getForeground() == Color.red) {
                if (textBoxes.get(i) instanceof SuperJTextField) {
                    if (((SuperJTextField)textBoxes.get(i)).getDescription() != null) {
                        valuesWithErrors.add(((SuperJTextField)textBoxes.get(i)).getDescription());
                    }
                }
                returnValue = false;
            }
        }
        return returnValue;
    }
    
    public boolean addItem(String name)
    {
        return false;
    }
    
    public void deleteAction()
    {}
    
    public void renameBIQElement(int id, String newName)
    {
        
    }
    
    public void copyItem(String copyName, int oldIndex) 
    {}
    
    /**
     * Requires that the combo box's model be a DefaultComboBoxModel.
     * That is because the plain-old ComboBoxModel doesn't allow alteration.
     * @param cmbSource
     * @param cmbDest
     * @param comboBox 
     */
    protected void comboBoxSwap(int cmbSource, int cmbDest, JComboBox comboBox) {
        DefaultComboBoxModel model = (DefaultComboBoxModel)comboBox.getModel();
        
        boolean isSelected = comboBox.getSelectedIndex() == cmbSource;
        Object swappedItem = model.getElementAt(cmbSource);
        model.removeElementAt(cmbSource);
        model.insertElementAt(swappedItem, cmbDest);
        if (isSelected) {
            comboBox.setSelectedIndex(cmbDest);
        }
    }
}
