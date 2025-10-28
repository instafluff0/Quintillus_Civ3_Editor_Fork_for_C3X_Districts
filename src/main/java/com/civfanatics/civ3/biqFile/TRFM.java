package com.civfanatics.civ3.biqFile;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Other sections that know about TRFM: Terr
 * @author Quintillus
 */
public class TRFM extends BIQSection{
    private int dataLength = 112;
    private String name = "";
    private String civilopediaEntry = "";
    private int turnsToComplete;
    private int requiredAdvanceInt;
    private TECH requiredAdvance;
    private int requiredResource1;
    private int requiredResource2;
    private String order = "";
    public TRFM(IO baselink)
    {
        super(baselink);
    }
    public void trim()
    {
            name = name.trim();
            civilopediaEntry = civilopediaEntry.trim();
            order = order.trim();
    }
    public int getDataLength()
    {
        return dataLength;
    }

    public String getName()
    {
        return name;
    }

    public String getCivilopediaEntry()
    {
        return civilopediaEntry;
    }

    public int getTurnsToComplete()
    {
        return turnsToComplete;
    }

    public int getRequiredAdvance()
    {
        return requiredAdvanceInt;
    }

    public int getRequiredResource1()
    {
        return requiredResource1;
    }

    public int getRequiredResource2()
    {
        return requiredResource2;
    }

    public String getOrder()
    {
        return order;
    }

    public void setDataLength(int dataLength)
    {
        this.dataLength = dataLength;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setCivilopediaEntry(String civilopediaEntry)
    {
        this.civilopediaEntry = civilopediaEntry;
    }

    public void setTurnsToComplete(int turnsToComplete)
    {
        this.turnsToComplete = turnsToComplete;
    }

    public void setRequiredAdvance(int requiredAdvance)
    {
        this.requiredAdvanceInt = requiredAdvance;
        if (baseLink.technology != null && requiredAdvanceInt != -1 && baseLink.technology.size() > requiredAdvanceInt) {
            this.requiredAdvance = baseLink.technology.get(requiredAdvanceInt);
        }
    }
    
    public void handleSwappedTech() {
        if (requiredAdvanceInt != -1) {
            this.requiredAdvanceInt = requiredAdvance.getIndex();
        }
    }

    public void setRequiredResource1(int requiredResource1)
    {
        this.requiredResource1 = requiredResource1;
    }

    public void setRequiredResource2(int requiredResource2)
    {
        this.requiredResource2 = requiredResource2;
    }

    public void setOrder(String order)
    {
        this.order = order;
    }
    
    /**
     * Updates the TRFM item when a tech is deleted
     * @param techID The ID of the deleted tech
     */
    public void handleDeletedTech(int techID)
    {
        if (this.requiredAdvanceInt == techID) {
            this.requiredAdvanceInt = -1;
            this.requiredAdvance = null;
        }
        else if (this.requiredAdvanceInt > techID)
            this.requiredAdvanceInt--;
    }
    
    /**
     * Updates the TRFM when a good is deleted
     * @param goodID The ID of the deleted good
     */
    public void handleDeletedGood(int goodID)
    {
        if (this.requiredResource1 == goodID)
            this.requiredResource1 = -1;
        else if (this.requiredResource1 > goodID)
            this.requiredResource1--;
        if (this.requiredResource2 == goodID)
            this.requiredResource1 = -1;
        else if (this.requiredResource2 > goodID)
            this.requiredResource2--;
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
        toReturn = toReturn + "turnsToComplete: " + turnsToComplete + lineReturn;
        toReturn = toReturn + "requiredAdvance: " + requiredAdvanceInt + lineReturn;
        toReturn = toReturn + "requiredResource1: " + requiredResource1 + lineReturn;
        toReturn = toReturn + "requiredResource2: " + requiredResource2 + lineReturn;
        toReturn = toReturn + "order: " + order + lineReturn;
        toReturn = toReturn + lineReturn;
        return toReturn;

    }

    public String compareTo(BIQSection section, String separator)
    {
        if (!(section instanceof TRFM))
            return null;
        TRFM two = (TRFM)section;
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "name: " + name + lineReturn;
        if (!(dataLength == two.getDataLength()))
        {
                toReturn = toReturn + "DataLength: " + dataLength + separator + two.getDataLength() + lineReturn;
        }
        if (civilopediaEntry.compareTo(two.getCivilopediaEntry()) != 0)
        {
                toReturn = toReturn + "CivilopediaEntry: " + civilopediaEntry + separator + two.getCivilopediaEntry() + lineReturn;
        }
        if (!(turnsToComplete == two.getTurnsToComplete()))
        {
                toReturn = toReturn + "TurnsToComplete: " + turnsToComplete + separator + two.getTurnsToComplete() + lineReturn;
        }
        if (!(requiredAdvanceInt == two.getRequiredAdvance()))
        {
                toReturn = toReturn + "RequiredAdvance: " + requiredAdvanceInt + separator + two.getRequiredAdvance() + lineReturn;
        }
        if (!(requiredResource1 == two.getRequiredResource1()))
        {
                toReturn = toReturn + "RequiredResource1: " + requiredResource1 + separator + two.getRequiredResource1() + lineReturn;
        }
        if (!(requiredResource2 == two.getRequiredResource2()))
        {
                toReturn = toReturn + "RequiredResource2: " + requiredResource2 + separator + two.getRequiredResource2() + lineReturn;
        }
        if (order.compareTo(two.getOrder()) != 0)
        {
                toReturn = toReturn + "Order: " + order + separator + two.getOrder() + lineReturn;
        }
        if (toReturn.equals("name: " + name + lineReturn))
        {
            toReturn = "";
        }
        return toReturn;
    }

    public Object getProperty(String string) throws UnsupportedOperationException
    {
        if (string.equals("Name"))
            return this.name;
        throw new UnsupportedOperationException();
    }
}
