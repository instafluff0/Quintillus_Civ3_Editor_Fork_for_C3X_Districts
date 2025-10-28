package com.civfanatics.civ3.biqFile;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Quintillus
 */
public class CONT extends BIQSection {
    public int dataLength = 8;
    public int continentClass = 1;  //1 = land
    public int numTiles;
    public CONT(IO baselink)
    {
        super(baselink);
    }

    public int getDataLength()
    {
        return dataLength;
    }

    public int getContinentClass()
    {
        return continentClass;
    }

    public int getNumTiles()
    {
        return numTiles;
    }

    public void setContinentClass(int continentClass)
    {
        this.continentClass = continentClass;
    }

    public void setNumTiles(int numTiles)
    {
        this.numTiles = numTiles;
    }

    public String toEnglish(){
        return toString();
    }

    public String toString()
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn =  "dataLength: " + dataLength + lineReturn;
        toReturn = toReturn + "continentClass: " + continentClass + lineReturn;
        toReturn = toReturn + "numTiles: " + numTiles + lineReturn;
        toReturn = toReturn + lineReturn;
        return toReturn;
    }

    public String compareTo(BIQSection section, String separator)
    {
        if (!(section instanceof CONT))
            return null;
        CONT two = (CONT)section;
        String toReturn = "";
        String lineReturn = java.lang.System.getProperty("line.separator");
        if (!(dataLength == two.getDataLength()))
        {
                toReturn = toReturn + "DataLength: " + dataLength + separator + two.getDataLength() + lineReturn;
        }
        if (!(continentClass == two.getContinentClass()))
        {
                toReturn = toReturn + "ContinentClass: " + continentClass + separator + two.getContinentClass() + lineReturn;
        }
        if (!(numTiles == two.getNumTiles()))
        {
                toReturn = toReturn + "NumTiles: " + numTiles + separator + two.getNumTiles() + lineReturn;
        }
        return toReturn;
    }
    public Object getProperty(String string) throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }
}
