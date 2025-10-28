package com.civfanatics.civ3.biqFile;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Other sections that know about EXPR: Govt, Map
 * @author Quintillus
 */
public class EXPR extends BIQSection{
    public int dataLength = 40;
    public String name = "";
    public int baseHitPoints;
    public int retreatBonus;
    public EXPR(IO baselink)
    {
        super(baselink);
    }
    public void trim()
    {
        name = name.trim();
    }
    public int getDataLength()
    {
        return dataLength;
    }

    public String getName()
    {
        return name;
    }

    public int getBaseHitPoints()
    {
        return baseHitPoints;
    }

    public int getRetreatBonus()
    {
        return retreatBonus;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setBaseHitPoints(int baseHitPoints)
    {
        this.baseHitPoints = baseHitPoints;
    }

    public void setRetreatBonus(int retreatBonus)
    {
        this.retreatBonus = retreatBonus;
    }

    public String toEnglish(){
        return toString();
    }

    @Override
    public String toString()
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn =  "name: " + name + lineReturn;
        toReturn =  toReturn + "dataLength: " + dataLength + lineReturn;
        toReturn = toReturn + "baseHitPoints: " + baseHitPoints + lineReturn;
        toReturn = toReturn + "retreatBonus: " + retreatBonus + lineReturn;
        toReturn = toReturn + lineReturn;
        return toReturn;
    }
    public String compareTo(BIQSection section, String separator)
    {
        if (!(section instanceof EXPR))
            return null;
        EXPR two = (EXPR)section;
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "name: " + name + lineReturn;
        if (!(dataLength == two.getDataLength()))
        {
                toReturn = toReturn + "DataLength: " + dataLength + separator + two.getDataLength() + lineReturn;
        }
        if (!(baseHitPoints == two.getBaseHitPoints()))
        {
                toReturn = toReturn + "BaseHitPoints: " + baseHitPoints + separator + two.getBaseHitPoints() + lineReturn;
        }
        if (!(retreatBonus == two.getRetreatBonus()))
        {
                toReturn = toReturn + "RetreatBonus: " + retreatBonus + separator + two.getRetreatBonus() + lineReturn;
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
