package com.civfanatics.civ3.biqFile;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Other sections that know about ESPN: GOVT
 * @author Quintillus
 */
public class ESPN extends BIQSection{
    private int dataLength = 232;
    private String description = "";
    private String name = "";
    private String civilopediaEntry = "";
    private int missionPerformedBy;
    private int baseCost;

    public ESPN(IO baselink)
    {
        super(baselink);
    }
    public void trim()
    {
        description = description.trim();
        name = name.trim();
        civilopediaEntry = civilopediaEntry.trim();
    }
    public int getDataLength()
    {
        return dataLength;
    }

    public String getDescription()
    {
        return description;
    }

    public String getName()
    {
        return name;
    }

    public String getCivilopediaEntry()
    {
        return civilopediaEntry;
    }

    public int getMissionPerformedBy()
    {
        return missionPerformedBy;
    }

    public int getBaseCost()
    {
        return baseCost;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setCivilopediaEntry(String civilopediaEntry)
    {
        this.civilopediaEntry = civilopediaEntry;
    }

    public void setMissionPerformedBy(int missionPerformedBy)
    {
        this.missionPerformedBy = missionPerformedBy;
    }

    public void setBaseCost(int baseCost)
    {
        this.baseCost = baseCost;
    }

    public String toString()
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn =  "name: " + name + lineReturn;
        toReturn = toReturn + "civilopediaEntry: " + civilopediaEntry + lineReturn;
        toReturn = toReturn +  "dataLength: " + dataLength + lineReturn;
        toReturn = toReturn + "description: " + description + lineReturn;
        toReturn = toReturn + "missionPerformedBy: " + missionPerformedBy + lineReturn;
        toReturn = toReturn + "baseCost: " + baseCost + lineReturn;
        toReturn = toReturn + lineReturn;
        return toReturn;
    }

    public String toEnglish()
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn =  "name: " + name + lineReturn;
        toReturn = toReturn + "civilopediaEntry: " + civilopediaEntry + lineReturn;
        toReturn = toReturn +  "dataLength: " + dataLength + lineReturn;
        toReturn = toReturn + "description: " + description + lineReturn;
        if (missionPerformedBy == 1)
        {
            toReturn = toReturn + "missionPerformedBy: diplomat" + lineReturn;
        }
        if (missionPerformedBy == 2)
        {
            toReturn = toReturn + "missionPerformedBy: spy" + lineReturn;
        }
        if (missionPerformedBy == 3)
        {
            toReturn = toReturn + "missionPerformedBy: diplomat and spy" + lineReturn;
        }
        if (missionPerformedBy == 0)
        {
            toReturn = toReturn + "missionPerformedBy: no one" + lineReturn;
        }
        toReturn = toReturn + "baseCost: " + baseCost + lineReturn;
        toReturn = toReturn + lineReturn;
        return toReturn;
    }
    public String compareTo(BIQSection section, String separator)
    {
        if (!(section instanceof ESPN))
            return null;
        ESPN two = (ESPN)section;
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "name: " + name + lineReturn;
        if (!(dataLength == two.getDataLength()))
        {
                toReturn = toReturn + "DataLength: " + dataLength + separator + two.getDataLength() + lineReturn;
        }
        if (description.compareTo(two.getDescription()) != 0)
        {
                toReturn = toReturn + "Description: " + description + separator + two.getDescription() + lineReturn;
        }
        if (civilopediaEntry.compareTo(two.getCivilopediaEntry()) != 0)
        {
                toReturn = toReturn + "CivilopediaEntry: " + civilopediaEntry + separator + two.getCivilopediaEntry() + lineReturn;
        }
        if (!(missionPerformedBy == two.getMissionPerformedBy()))
        {
                toReturn = toReturn + "MissionPerformedBy: " + missionPerformedBy + separator + two.getMissionPerformedBy() + lineReturn;
        }
        if (!(baseCost == two.getBaseCost()))
        {
                toReturn = toReturn + "BaseCost: " + baseCost + separator + two.getBaseCost() + lineReturn;
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
