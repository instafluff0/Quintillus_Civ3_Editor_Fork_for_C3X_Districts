/*
 * 
 * 
 */
package com.civfanatics.civ3.xplatformeditor.CustomComponents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.DefaultListModel;
import org.apache.log4j.Logger;

/**
 * TODO: Building delete, generally probably fix delete.
 * 
 * This class is to be used to make an alphabetizable JList that will not forget
 * the original order of its elements.  To do this it will keep a mapping of
 * the "true" (original) order to the displayed order, and will include
 * custom access methods (add, get, remove) that will take this into account.
 * 
 * In the initial development version the list will always be alphabetized.
 * Once that functionality is operational, the next step will be to make it
 * toggleable.
 * 
 * We'll want to be able to add without caring about order.
 * We'll want to be able to get based on the Index (not the display value).
 * We'll also want to be able to remove based on the Index (not the display value).
 * 
 * November 6, 2011
 * 
 * @author Andrew
 */
public class SuperListModel extends DefaultListModel{
    List<Integer>mapOfIndexToDisplay;
    Logger logger = Logger.getLogger(this.getClass());
    private boolean isAlphabetic = false;
    public SuperListModel()
    {
        super();
        mapOfIndexToDisplay = new ArrayList<Integer>();
    }
    
    public void toggle()
    {
        logger.info("dealphabetizing");
        if (isAlphabetic)
            dealphabetize();
        else
            alphabetize();
    }
    
    public void alphabetize()
    {
        logger.info("alphabetizing");
        //Get the elements.  Assume it is in BIQ order.
        Object[] myElements = this.toArray();
        this.clear();
        List<Integer>newMap = new ArrayList<Integer>(myElements.length); 
        Object[]newElements = new Object[myElements.length];  
        //Sort a copy
        System.arraycopy(myElements,0,newElements,0,myElements.length);
        Arrays.sort(newElements);
        //OK now we just gotta figure out the mapping.
        for (int n = 0; n < myElements.length; n++)
        {
            for (int j = 0; j < myElements.length;j++)
            {
                if (myElements[j] == newElements[n])
                {
                    newMap.add(n);
                }
            }
            this.addElement(newElements[n]);   //yes i am indexing over the other array, but they are the same length
        }
        mapOfIndexToDisplay = newMap;
        isAlphabetic = true;
    }
    
    public void dealphabetize()
    {
        //Get the elements.  They will be in alphabetic order.
        Object[] myElements = this.toArray();
        logger.info("Array length: " + myElements.length);
        this.clear();
        List<Integer>newMap = new ArrayList<Integer>(myElements.length);
        //Loop over them, adding them in _non_ alphabetic order.
        for (int i = 0; i < myElements.length; i++)
        {
            logger.info("iteration " + i + "; will seek item " + mapOfIndexToDisplay.get(i));
            this.addElement(myElements[mapOfIndexToDisplay.get(i)]);
            newMap.add(i);  //just needs to count
        }
        mapOfIndexToDisplay = newMap;
        isAlphabetic = false;
    }
    
    /**
     * First we add the element in alphabetically.  Then we update our mappings.
     * @param o 
     */
    public void addIndexedElement(Comparable o)
    {
        if (this.isAlphabetic)
        {
            int newElementPosition;
            //Possible enhancement: binary search.  Might not be worth it with this small of lists.
            if (this.size() == 0)
            {
                this.addElement(o);
                newElementPosition = 0;
                mapOfIndexToDisplay.add(newElementPosition);
            }
            else
            {
                if (logger.isDebugEnabled())
                    logger.debug("this.size() is + " + this.size());
                int max = this.size();
                boolean found = false;
                for (newElementPosition = 0; newElementPosition < max; newElementPosition++)
                {
                    if (logger.isDebugEnabled())
                        logger.debug("i = " + newElementPosition + "; Comparing " + o + " to " + this.elementAt(newElementPosition) + "; get " + o.compareTo(this.elementAt(newElementPosition)));
                    if (o.compareTo(this.elementAt(newElementPosition)) < 0)
                    {   //put it here
                        logger.info("putting " + o + " before " + this.elementAt(newElementPosition));
                        this.add(newElementPosition, o);
                        found = true;
                        break;
                    }
                }
                //check and make sure it's still put in if it's the last element
                if (!(found))
                {
                    logger.debug("adding" + o + " at the end of the list");
                    this.addElement(o);
                    //newElementPosition++;    //it
                    logger.info("newElementPosition: " + newElementPosition + "; size: " + this.size());
                }
                //the new indexed item (the last) is at position i
                logger.debug("adding the new item's index as " + newElementPosition);
                mapOfIndexToDisplay.add(newElementPosition);
                //the below won't run if the element is the 1st one, which is okay.
                int stopAt = this.size();
                for (int j = 0; j < stopAt - 1; j++)   //-1 b/c we can must skip this element
                {
                    //If the display position was at i or higher, now it is 1 above that.
                    if (mapOfIndexToDisplay.get(j) >= newElementPosition)
                    {
                        //logger.info("Adding one to object " + this.getElementAt(mapOfIndexToDisplay.get(j + 1)) + 1 + " due to adding " + o);
                        mapOfIndexToDisplay.set(j, mapOfIndexToDisplay.get(j) + 1);
                    }
                }
            }
        }
        else
        {
            this.addElement(o);
            mapOfIndexToDisplay.add(this.getSize() - 1);
        }
    }
    
    /**
     * Converts the index that will be return by JList.getSelectedIndex (and
     * will be determined by the alphabetical order) into a "true" indexed, as
     * converted by our index.
     * @param position
     * @return 
     */
    public int getTrueIndex(int position)
    {
        //Same method works regardless of alphabetic or not.
        if (position == -1) 
            return -1;
        if (!isAlphabetic)
            return position;
        for (int i = 0; i < mapOfIndexToDisplay.size(); i++)
        {
            if (mapOfIndexToDisplay.get(i) == position)
                return i;
        }
        return -1;
    }
    
    /**
     * Remove an item, by its true index.
     * @param trueIndex 
     */
    public void removeByTrueIndex(int trueIndex)
    {
        //Same method works whether alphabetic or not.
        this.remove(mapOfIndexToDisplay.get(trueIndex));
        mapOfIndexToDisplay.remove(trueIndex);
    }
    
    /**
     * SuperList remove.
     * Intelligently removes from either an alphabetic or non-alphabetic list.
     * Assumes we are getting the true index.
     * @param index 
     */
    public void slRemove(int index)
    {
        //JOptionPane.showMessageDialog(null,"Requested to remove item " + index);
        if (isAlphabetic)
            removeByTrueIndex(index);
        else
            remove(index);
    }
    /**
     * NOT ready yet.
     * @param index 
     */
    public void slRemove(int index, boolean trueIndex)
    {
        return;
//        if (isAlphabetic)
//            removeByTrueIndex(index);
//        else
//            remove(index);
    }
}
