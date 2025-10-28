package com.civfanatics.civ3.biqFile;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Quintillus
 */

public class CTZN extends BIQSection{
    private int dataLength = 124;
    private int defaultCitizen;
    private String name = "";
    private String civilopediaEntry = "";
    private String pluralName = "";
    private int prerequisiteInt;
    private TECH prerequisite;
    private int luxuries;
    private int research;
    private int taxes;
    private int corruption;
    private int construction;

    public CTZN(IO baselink)
    {
        super(baselink);
    }

    public CTZN(String name, IO baselink)
    {
        super(baselink);
        this.name = name;
        this.prerequisiteInt = -1;
    }

    public void trim()
    {
        name = name.trim();
        civilopediaEntry = civilopediaEntry.trim();
        pluralName = pluralName.trim();
    }

    public int getDataLength()
    {
        return dataLength;
    }
    
    public void increaseDataLength(int change)
    {
        dataLength+=change;
    }

    public int getDefaultCitizen()
    {
        return defaultCitizen;
    }

    public String getName()
    {
        return name;
    }

    public String getCivilopediaEntry()
    {
        return civilopediaEntry;
    }

    public String getPluralName()
    {
        return pluralName;
    }

    public int getPrerequisite()
    {
        return prerequisiteInt;
    }

    public int getLuxuries()
    {
        return luxuries;
    }

    public int getResearch()
    {
        return research;
    }

    public int getTaxes()
    {
        return taxes;
    }

    public int getCorruption()
    {
        return corruption;
    }

    public int getConstruction()
    {
        return construction;
    }

    public void setDefaultCitizen(int defaultCitizen)
    {
        this.defaultCitizen = defaultCitizen;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setCivilopediaEntry(String civilopediaEntry)
    {
        this.civilopediaEntry = civilopediaEntry;
    }

    public void setPluralName(String pluralName)
    {
        this.pluralName = pluralName;
    }

    public void setPrerequisite(int prerequisite)
    {
        this.prerequisiteInt = prerequisite;
        if (baseLink.technology != null && prerequisiteInt != -1 && baseLink.technology.size() > prerequisiteInt) {
            this.prerequisite = baseLink.technology.get(prerequisiteInt);
        }
    }

    public void setLuxuries(int luxuries)
    {
        this.luxuries = luxuries;
    }

    public void setResearch(int research)
    {
        this.research = research;
    }

    public void setTaxes(int taxes)
    {
        this.taxes = taxes;
    }

    public void setCorruption(int corruption)
    {
        this.corruption = corruption;
    }

    public void setConstruction(int construction)
    {
        this.construction = construction;
    }
    
    public void handleDeletedTech(int index) {
        if (prerequisiteInt == index) {
            prerequisiteInt = -1;
            prerequisite = null;
        }
        else if (prerequisiteInt > index) {
            prerequisiteInt--;
        }
    }
    
    public void handleSwappedTech() {
        if (prerequisiteInt != -1) {
            prerequisiteInt = prerequisite.getIndex();
        }
    }

    public String toEnglish(){
        return toString();
    }

    public String toString()
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "name: " + name + lineReturn;
        toReturn = toReturn + "civilopediaEntry: " + civilopediaEntry + lineReturn;
        toReturn = toReturn + "dataLength: " + dataLength + lineReturn;
        toReturn = toReturn + "defaultCitizen: " + defaultCitizen + lineReturn;
        toReturn = toReturn + "pluralName: " + pluralName + lineReturn;
        toReturn = toReturn + "prerequisite: " + prerequisiteInt + lineReturn;
        toReturn = toReturn + "luxuries: " + luxuries + lineReturn;
        toReturn = toReturn + "research: " + research + lineReturn;
        toReturn = toReturn + "taxes: " + taxes + lineReturn;
        toReturn = toReturn + "corruption: " + corruption + lineReturn;
        toReturn = toReturn + "construction: " + construction + lineReturn;
        toReturn = toReturn + lineReturn;
        return toReturn;
    }

    public String compareTo(BIQSection section, String separator)
    {
        if (!(section instanceof CTZN))
            return null;
        CTZN two = (CTZN)section;
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "name: " + name + lineReturn;
        if (!(dataLength == two.getDataLength()))
        {
                toReturn = toReturn + "DataLength: " + dataLength + separator + two.getDataLength() + lineReturn;
        }
        if (!(defaultCitizen == two.getDefaultCitizen()))
        {
                toReturn = toReturn + "DefaultCitizen: " + defaultCitizen + separator + two.getDefaultCitizen() + lineReturn;
        }
        if (civilopediaEntry.compareTo(two.getCivilopediaEntry()) != 0)
        {
                toReturn = toReturn + "CivilopediaEntry: " + civilopediaEntry + separator + two.getCivilopediaEntry() + lineReturn;
        }
        if (pluralName.compareTo(two.getPluralName()) != 0)
        {
                toReturn = toReturn + "PluralName: " + pluralName + separator + two.getPluralName() + lineReturn;
        }
        if (!(prerequisiteInt == two.getPrerequisite()))
        {
                toReturn = toReturn + "Prerequisite: " + prerequisiteInt + separator + two.getPrerequisite() + lineReturn;
        }
        if (!(luxuries == two.getLuxuries()))
        {
                toReturn = toReturn + "Luxuries: " + luxuries + separator + two.getLuxuries() + lineReturn;
        }
        if (!(research == two.getResearch()))
        {
                toReturn = toReturn + "Research: " + research + separator + two.getResearch() + lineReturn;
        }
        if (!(taxes == two.getTaxes()))
        {
                toReturn = toReturn + "Taxes: " + taxes + separator + two.getTaxes() + lineReturn;
        }
        if (!(corruption == two.getCorruption()))
        {
                toReturn = toReturn + "Corruption: " + corruption + separator + two.getCorruption() + lineReturn;
        }
        if (!(construction == two.getConstruction()))
        {
                toReturn = toReturn + "Construction: " + construction + separator + two.getConstruction() + lineReturn;
        }

        if (toReturn.equals("name: " + name + lineReturn))
        {
            toReturn = "";
        }
        return toReturn;
    }

    public Object getProperty(String string) throws UnsupportedOperationException
    {
        if (string.equalsIgnoreCase("Name"))
            return this.name;
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CTZN other = (CTZN) obj;
        if (this.dataLength != other.dataLength) {
            return false;
        }
        if (this.defaultCitizen != other.defaultCitizen) {
            return false;
        }
        if (this.prerequisiteInt != other.prerequisiteInt) {
            return false;
        }
        if (this.luxuries != other.luxuries) {
            return false;
        }
        if (this.research != other.research) {
            return false;
        }
        if (this.taxes != other.taxes) {
            return false;
        }
        if (this.corruption != other.corruption) {
            return false;
        }
        if (this.construction != other.construction) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.civilopediaEntry == null) ? (other.civilopediaEntry != null) : !this.civilopediaEntry.equals(other.civilopediaEntry)) {
            return false;
        }
        if ((this.pluralName == null) ? (other.pluralName != null) : !this.pluralName.equals(other.pluralName)) {
            return false;
        }
        //Shallow compare tech.  Avoids comparing potentially everything and we only care about the reference.
        if (this.prerequisite != other.prerequisite) {
            return false;
        }
        return true;
    }
    
    @Override
    public Object clone() {
        CTZN clone = new CTZN(this.baseLink);
        clone.defaultCitizen = this.defaultCitizen;
        clone.name = this.name;
        clone.civilopediaEntry = this.civilopediaEntry;
        clone.pluralName = this.pluralName;
        clone.prerequisiteInt = this.prerequisiteInt;
        clone.prerequisite = this.prerequisite;
        clone.luxuries = this.luxuries;
        clone.research = this.research;
        clone.taxes = this.taxes;
        clone.corruption = this.corruption;
        clone.construction = this.construction;
        return clone;
    }
}

