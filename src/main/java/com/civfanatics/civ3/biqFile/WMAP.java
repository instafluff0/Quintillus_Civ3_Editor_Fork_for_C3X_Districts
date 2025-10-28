package com.civfanatics.civ3.biqFile;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Quintillus
 */
import java.util.ArrayList;
public class WMAP extends BIQSection{
    public int dataLength = 168; //with no resourceOccurence
    public int numResources;
    public ArrayList<Integer>resourceOccurence;    
    public int numContinents = 0;  //Changed on 3/30/12
    public int height;
    public int distanceBetweenCivs = 12; //Set on 3/30/12.  Might cause crash with 0?
    public int numCivs;
    public int questionMark1;  //seems to be 0.7 * height on new (blank) Firaxis maps.  Is 0.75*height on WWII.
    public int questionMark2 = 0;  //always seems to be zero.
    public int width;
    public int questionMark3 = -1;  //seems to be -1 in Firaxis maps.
    public String unknown124 = "";
    public int mapSeed;
    public int flags;
    public boolean xWrapping;
    public boolean yWrapping;
    public boolean polarIceCaps;
    public WMAP(IO baselink)
    {
        super(baselink);
        resourceOccurence = new ArrayList<Integer>();
    }

    public int getDataLength()
    {
        return dataLength;
    }

    public int getNumResources()
    {
        return numResources;
    }

    public int getNumContinents()
    {
        return numContinents;
    }

    public int getHeight()
    {
        return height;
    }

    public int getDistanceBetweenCivs()
    {
        return distanceBetweenCivs;
    }

    public int getNumCivs()
    {
        return numCivs;
    }

    public int getQuestionMark1()
    {
        return questionMark1;
    }

    public int getQuestionMark2()
    {
        return questionMark2;
    }

    public int getWidth()
    {
        return width;
    }

    public int getQuestionMark3()
    {
        return questionMark3;
    }

    public String getUnknown124()
    {
        return unknown124;
    }

    public int getMapSeed()
    {
        return mapSeed;
    }

    public int getFlags()
    {
        return flags;
    }
    public boolean getXWrapping()
    {
        return xWrapping;
    }

    public boolean getYWrapping()
    {
        return yWrapping;
    }

    public boolean getPolarIceCaps()
    {
        return polarIceCaps;
    }


    public void setDataLength(int dataLength)
    {
        this.dataLength = dataLength;
    }

    public void setNumResources(int numResources)
    {
        this.numResources = numResources;
    }

    public void setNumContinents(int numContinents)
    {
        this.numContinents = numContinents;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public void setDistanceBetweenCivs(int distanceBetweenCivs)
    {
        this.distanceBetweenCivs = distanceBetweenCivs;
    }

    public void setNumCivs(int numCivs)
    {
        this.numCivs = numCivs;
    }

    public void setQuestionMark1(int questionMark1)
    {
        this.questionMark1 = questionMark1;
    }

    public void setQuestionMark2(int questionMark2)
    {
        this.questionMark2 = questionMark2;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public void setQuestionMark3(int questionMark3)
    {
        this.questionMark3 = questionMark3;
    }

    public void setUnknown124(String unknown)
    {
        unknown124 = unknown;
    }

    public void setMapSeed(int mapSeed)
    {
        this.mapSeed = mapSeed;
    }

    public void setFlags(int flags)
    {
        this.flags = flags;
    }

    @Override
    public String toString()
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "dataLength: " + dataLength + lineReturn;
        toReturn = toReturn + "numResources: " + numResources + lineReturn;
        toReturn = toReturn + "numContinents: " + numContinents + lineReturn;
        toReturn = toReturn + "height: " + height + lineReturn;
        toReturn = toReturn + "distanceBetweenCivs: " + distanceBetweenCivs + lineReturn;
        toReturn = toReturn + "numCivs: " + numCivs + lineReturn;
        toReturn = toReturn + "questionMark1: " + questionMark1 + lineReturn;
        toReturn = toReturn + "questionMark2: " + questionMark2 + lineReturn;
        toReturn = toReturn + "width: " + width + lineReturn;
        toReturn = toReturn + "questionMark3: " + questionMark3 + lineReturn;
        toReturn = toReturn + "mapSeed: " + mapSeed + lineReturn;
        toReturn = toReturn + "flags: " + flags + lineReturn;
        toReturn = toReturn + lineReturn;
        return toReturn;
    }

    public String toEnglish()
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "dataLength: " + dataLength + lineReturn;
        toReturn = toReturn + "numResources: " + numResources + lineReturn;
        toReturn = toReturn + "numContinents: " + numContinents + lineReturn;
        toReturn = toReturn + "height: " + height + lineReturn;
        toReturn = toReturn + "distanceBetweenCivs: " + distanceBetweenCivs + lineReturn;
        toReturn = toReturn + "numCivs: " + numCivs + lineReturn;
        toReturn = toReturn + "questionMark1: " + questionMark1 + lineReturn;
        toReturn = toReturn + "questionMark2: " + questionMark2 + lineReturn;
        toReturn = toReturn + "width: " + width + lineReturn;
        toReturn = toReturn + "questionMark3: " + questionMark3 + lineReturn;
        toReturn = toReturn + "mapSeed: " + mapSeed + lineReturn;
        toReturn = toReturn + "flags: " + flags + lineReturn;
        toReturn = toReturn + "  xWrapping: " + xWrapping + lineReturn;
        toReturn = toReturn + "  yWrapping: " + yWrapping + lineReturn;
        toReturn = toReturn + "  polar ice caps: " + polarIceCaps + lineReturn;
        toReturn = toReturn + lineReturn;
        return toReturn;
    }

    public void extractEnglish()
    {
        int flagCopy = flags;
        int divBy = flagCopy/(int)(Math.pow(2, 2));
        if (divBy == 1)
        {
            polarIceCaps = true;
            flagCopy-=4;
        }
        divBy = flagCopy/(int)(Math.pow(2, 1));
        if (divBy == 1)
        {
            yWrapping = true;
            flagCopy-=2;
        }
        divBy = flagCopy/(int)(Math.pow(2, 0));
        if (divBy == 1)
        {
            xWrapping = true;
            flagCopy-=1;
        }
    }
    
    //Takes booleans and constructs composite of them
    public void calculateFlags()
    {
        this.flags = 0;
        if (polarIceCaps)
            flags+=4;
        if (yWrapping)
            flags+=2;
        if (xWrapping)
            flags++;
    }

    public String compareTo(BIQSection section, String separator)
    {
        if (!(section instanceof WMAP))
            return null;
        WMAP two = (WMAP)section;
        String toReturn = "";
        String lineReturn = java.lang.System.getProperty("line.separator");
        if (!(dataLength == two.getDataLength()))
        {
                toReturn = toReturn + "DataLength: " + dataLength + separator + two.getDataLength() + lineReturn;
        }
        if (!(numResources == two.getNumResources()))
        {
                toReturn = toReturn + "NumResources: " + numResources + separator + two.getNumResources() + lineReturn;
        }
        else  //see if a flavor is checked for a building in only one file
        {
            for (int i = 0; i < numResources; i++)
            {
                if (!(resourceOccurence.get(i) == two.resourceOccurence.get(i)))
                {
                    toReturn = toReturn + "  occurence of resource " + i + ": " + resourceOccurence.get(i) + separator + two.resourceOccurence.get(i) + lineReturn;
                }
            }
        }
        if (!(numContinents == two.getNumContinents()))
        {
                toReturn = toReturn + "NumContinents: " + numContinents + separator + two.getNumContinents() + lineReturn;
        }
        if (!(height == two.getHeight()))
        {
                toReturn = toReturn + "Height: " + height + separator + two.getHeight() + lineReturn;
        }
        if (!(distanceBetweenCivs == two.getDistanceBetweenCivs()))
        {
                toReturn = toReturn + "DistanceBetweenCivs: " + distanceBetweenCivs + separator + two.getDistanceBetweenCivs() + lineReturn;
        }
        if (!(numCivs == two.getNumCivs()))
        {
                toReturn = toReturn + "NumCivs: " + numCivs + separator + two.getNumCivs() + lineReturn;
        }
        if (!(questionMark1 == two.getQuestionMark1()))
        {
                toReturn = toReturn + "QuestionMark1: " + questionMark1 + separator + two.getQuestionMark1() + lineReturn;
        }
        if (!(questionMark2 == two.getQuestionMark2()))
        {
                toReturn = toReturn + "QuestionMark2: " + questionMark2 + separator + two.getQuestionMark2() + lineReturn;
        }
        if (!(width == two.getWidth()))
        {
                toReturn = toReturn + "Width: " + width + separator + two.getWidth() + lineReturn;
        }
        if (!(questionMark3 == two.getQuestionMark3()))
        {
                toReturn = toReturn + "QuestionMark3: " + questionMark3 + separator + two.getQuestionMark3() + lineReturn;
        }
        if (!(mapSeed == two.getMapSeed()))
        {
                toReturn = toReturn + "MapSeed: " + mapSeed + separator + two.getMapSeed() + lineReturn;
        }
        if (!(flags == two.getFlags()))
        {
                toReturn = toReturn + "Flags: " + flags + separator + two.getFlags() + lineReturn;
        }
        if (!(xWrapping == two.getXWrapping()))
        {
                toReturn = toReturn + "  XWrapping: " + xWrapping + separator + two.getXWrapping() + lineReturn;
        }
        if (!(yWrapping == two.getYWrapping()))
        {
                toReturn = toReturn + "  YWrapping: " + yWrapping + separator + two.getYWrapping() + lineReturn;
        }
        if (!(polarIceCaps == two.getPolarIceCaps()))
        {
                toReturn = toReturn + "  PolarIceCaps: " + polarIceCaps + separator + two.getPolarIceCaps() + lineReturn;
        }
        return toReturn;
    }
    public Object getProperty(String string) throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }
}
