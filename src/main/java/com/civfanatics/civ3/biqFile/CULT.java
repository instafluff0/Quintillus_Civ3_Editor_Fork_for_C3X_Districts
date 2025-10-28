package com.civfanatics.civ3.biqFile;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Quintillus
 */
public class CULT extends BIQSection{
    private int dataLength = 88;
    private String name = "";
    private int propagandaSuccess;
    private int cultRatioPercent;
    private int ratioDenominator = 1;
    private int ratioNumerator = 1;
    private int initResistanceChance;
    private int continuedResistanceChance;
    public CULT(IO baselink)
    {
        super(baselink);
    }
    public CULT(String name, IO baselink)
    {
        super(baselink);
        this.name = name;
    }
    public void trim()
    {
        name = name.trim();
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPropagandaSuccess(int propagandaSuccess)
    {
        this.propagandaSuccess = propagandaSuccess;
    }

    public void setCultRatioPercent(int cultRatioPercent)
    {
        this.cultRatioPercent = cultRatioPercent;
    }

    public void setRatioDenominator(int ratioDenominator)
    {
        this.ratioDenominator = ratioDenominator;
    }

    public void setRatioNumerator(int ratioNumerator)
    {
        this.ratioNumerator = ratioNumerator;
    }

    public void setInitResistanceChance(int initResistanceChance)
    {
        this.initResistanceChance = initResistanceChance;
    }

    public void setContinuedResistanceChance(int continuedResistanceChance)
    {
        this.continuedResistanceChance = continuedResistanceChance;
    }

    public int getDataLength()
    {
        return dataLength;
    }

    public String getName()
    {
        return name;
    }

    public int getPropagandaSuccess()
    {
        return propagandaSuccess;
    }

    public int getCultRatioPercent()
    {
        return cultRatioPercent;
    }

    public int getRatioDenominator()
    {
        return ratioDenominator;
    }

    public int getRatioNumerator()
    {
        return ratioNumerator;
    }

    public int getInitResistanceChance()
    {
        return initResistanceChance;
    }

    public int getContinuedResistanceChance()
    {
        return continuedResistanceChance;
    }

    public String toEnglish(){
        return toString();
    }

    public String toString()
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn =  "name: " + name + lineReturn;
        toReturn = toReturn + "dataLength:  " + dataLength + lineReturn;
        toReturn = toReturn + "propagandaSuccess: " + propagandaSuccess + lineReturn;
        toReturn = toReturn + "cultRatioPercent: " + cultRatioPercent + lineReturn;
        toReturn = toReturn + "ratioDenominator: " + ratioDenominator + lineReturn;
        toReturn = toReturn + "ratioNumerator: " + ratioNumerator + lineReturn;
        toReturn = toReturn + "initResistanceChance: " + initResistanceChance + lineReturn;
        toReturn = toReturn + "continuedResistanceChance: " + continuedResistanceChance + lineReturn;
        toReturn = toReturn + lineReturn;
        return toReturn;
    }

    public String compareTo(BIQSection section, String separator)
    {
        if (!(section instanceof CULT))
            return null;
        CULT two = (CULT)section;
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "name: " + name + lineReturn;
        if (!(dataLength == two.getDataLength()))
        {
                toReturn = toReturn + "DataLength: " + dataLength + separator + two.getDataLength() + lineReturn;
        }
        if (!(propagandaSuccess == two.getPropagandaSuccess()))
        {
                toReturn = toReturn + "PropagandaSuccess: " + propagandaSuccess + separator + two.getPropagandaSuccess() + lineReturn;
        }
        if (!(cultRatioPercent == two.getCultRatioPercent()))
        {
                toReturn = toReturn + "CultRatioPercent: " + cultRatioPercent + separator + two.getCultRatioPercent() + lineReturn;
        }
        if (!(ratioDenominator == two.getRatioDenominator()))
        {
                toReturn = toReturn + "RatioDenominator: " + ratioDenominator + separator + two.getRatioDenominator() + lineReturn;
        }
        if (!(ratioNumerator == two.getRatioNumerator()))
        {
                toReturn = toReturn + "RatioNumerator: " + ratioNumerator + separator + two.getRatioNumerator() + lineReturn;
        }
        if (!(initResistanceChance == two.getInitResistanceChance()))
        {
                toReturn = toReturn + "InitResistanceChance: " + initResistanceChance + separator + two.getInitResistanceChance() + lineReturn;
        }
        if (!(continuedResistanceChance == two.getContinuedResistanceChance()))
        {
                toReturn = toReturn + "ContinuedResistanceChance: " + continuedResistanceChance + separator + two.getContinuedResistanceChance() + lineReturn;
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
