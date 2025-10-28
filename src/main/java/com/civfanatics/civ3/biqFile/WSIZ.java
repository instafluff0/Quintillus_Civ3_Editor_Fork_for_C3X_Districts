package com.civfanatics.civ3.biqFile;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Quintillus
 */
public class WSIZ extends BIQSection {
    private int dataLength;
    private int optimalNumberOfCities;
    private int techRate;
    private String empty = "";
    private String name = "";
    private int height;
    private int distanceBetweenCivs;
    private int numberOfCivs;
    private int width;
    public WSIZ(IO baselink)
    {
        super(baselink);
    }
    public void trim()
    {
            empty = empty.trim();
            name = name.trim();
    }
    public int getDataLength()
    {
        return dataLength;
    }

    public int getOptimalNumberOfCities()
    {
        return optimalNumberOfCities;
    }

    public int getTechRate()
    {
        return techRate;
    }

    public String getEmpty()
    {
        return empty;
    }

    public String getName()
    {
        return name;
    }

    public int getHeight()
    {
        return height;
    }

    public int getDistanceBetweenCivs()
    {
        return distanceBetweenCivs;
    }

    public int getNumberOfCivs()
    {
        return numberOfCivs;
    }

    public int getWidth()
    {
        return width;
    }






    public void setDataLength(int dataLength)
    {
        this.dataLength = dataLength;
    }

    public void setOptimalNumberOfCities(int optimalNumberOfCities)
    {
        this.optimalNumberOfCities = optimalNumberOfCities;
    }

    public void setTechRate(int techRate)
    {
        this.techRate = techRate;
    }

    public void setEmpty(String empty)
    {
        this.empty = empty;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public void setDistanceBetweenCivs(int distanceBetweenCivs)
    {
        this.distanceBetweenCivs = distanceBetweenCivs;
    }

    public void setNumberOfCivs(int numberOfCivs)
    {
        this.numberOfCivs = numberOfCivs;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public String toEnglish(){
        return toString();
    }

    public String toString()
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn =  "name: " + name + lineReturn;
        toReturn = toReturn + "dataLength: " + dataLength + lineReturn;
        toReturn = toReturn + "optimalNumberOfCities: " + optimalNumberOfCities + lineReturn;
        toReturn = toReturn + "techRate: " + techRate + lineReturn;
        toReturn = toReturn + "empty: " + empty + lineReturn;
        toReturn = toReturn + "height: " + height + lineReturn;
        toReturn = toReturn + "distanceBetweenCivs: " + distanceBetweenCivs + lineReturn;
        toReturn = toReturn + "numberOfCivs: " + numberOfCivs + lineReturn;
        toReturn = toReturn + "width: " + width + lineReturn;
        toReturn = toReturn + lineReturn;
        return toReturn;
    }

    public String compareTo(BIQSection section, String separator)
    {
        if (!(section instanceof WSIZ))
            return null;
        WSIZ two = (WSIZ)section;
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "name: " + name + lineReturn;
        if (!(dataLength == two.getDataLength()))
        {
                toReturn = toReturn + "DataLength: " + dataLength + separator + two.getDataLength() + lineReturn;
        }
        if (!(optimalNumberOfCities == two.getOptimalNumberOfCities()))
        {
                toReturn = toReturn + "OptimalNumberOfCities: " + optimalNumberOfCities + separator + two.getOptimalNumberOfCities() + lineReturn;
        }
        if (!(techRate == two.getTechRate()))
        {
                toReturn = toReturn + "TechRate: " + techRate + separator + two.getTechRate() + lineReturn;
        }
        if (empty.compareTo(two.getEmpty()) != 0)
        {
                toReturn = toReturn + "Empty: " + empty + separator + two.getEmpty() + lineReturn;
        }
        if (!(height == two.getHeight()))
        {
                toReturn = toReturn + "Height: " + height + separator + two.getHeight() + lineReturn;
        }
        if (!(distanceBetweenCivs == two.getDistanceBetweenCivs()))
        {
                toReturn = toReturn + "DistanceBetweenCivs: " + distanceBetweenCivs + separator + two.getDistanceBetweenCivs() + lineReturn;
        }
        if (!(numberOfCivs == two.getNumberOfCivs()))
        {
                toReturn = toReturn + "NumberOfCivs: " + numberOfCivs + separator + two.getNumberOfCivs() + lineReturn;
        }
        if (!(width == two.getWidth()))
        {
                toReturn = toReturn + "Width: " + width + separator + two.getWidth() + lineReturn;
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
