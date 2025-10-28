
package com.civfanatics.civ3.biqFile;

import java.io.Serializable;

/**
 * This class contains methods and variables that should be present on all BIQ
 * sections.  By this it is not meant that every BIQ section contains a field
 * with a certain name, but rather that some metadata fields not actually
 * present in the BIQ may be present in all the Java representations of the
 * BIQ sections.
 *
 * @author Andrew
 */
public abstract class BIQSection implements Serializable {
    /**
     * The baseLink is a link to the base BIQ from this section.
     */
    transient public IO baseLink;
    transient int index = -1;
    
    public BIQSection(){};

    public BIQSection(IO baseLink)
    {
        this.baseLink = baseLink;
    }
    
    public BIQSection(civ3Version version) {
        IO base = new IO();
        base.version = version;
        this.baseLink = base;
    }

    /**
     * Dispatches to either toEnglish or toString, depending on whether the
     * English option is selected or not.
     * @return - The result of either toEnglish() or toString()
     */
    public String toFile()
    {
        if (baseLink.exportEnglish)
            return toEnglish();
        else
            return toString();
    }

    /**
     * The "toString" version that should expose easier-to-read English.
     * @return String - The "English" representation of the section.
     */
    abstract public String toEnglish();
    
    /**
     * Utility method for making true/false values which are 1/0 easy in toEnglish.
     * Assumes 1 is true, and 0 is false.  Cannot be used with other cases (-1 as "None", for example)
     * unless those cases have been handled first.
     * @param integer
     * @return 
     */
    protected String trueOrFalse(int integer) {
        if (integer == 0) {
            return "false";
        }
        else if (integer == 1) {
            return "true";
        }
        return "";
    }

    @Override
    /**
     * The toString method.  This one is to be called when the "BIQ" format is
     * to be exported.
     * @return String - The "BIQ" (not so English) representation of the section.
     */
    abstract public String toString();

    /**
     * Returns the value of some property of an instance of the section, for
     * example, the name of an instance of the section ("Leonardo's Workshop",
     * perhaps).  The sections will implement this and choose which properties
     * they allow to be exposed in this manner.
     * @return
     */
    abstract public Object getProperty(String propertyName) throws UnsupportedOperationException;

    abstract public String compareTo(BIQSection section, String separator);
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int newIndex) {
        this.index = newIndex;
    }
    
    public String getName() {
        return "";
    }
    
    public String getCivilopediaEntry() {
        return "";
    }
}
