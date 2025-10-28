package com.civfanatics.civ3.biqFile;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Other sections that know about FLAV: Tech, Bldg, Civ
 * @author Quintillus
 */
import java.util.*;
public class FLAV extends BIQSection{
    public int questionMark = 1;
    public String name = "";
    public int numberOfFlavors;
    public ArrayList<Integer>relationWithOtherFlavor;
    
    
    public FLAV(IO baselink, String name)
    {
        super(baselink);
        this.name = name;
        relationWithOtherFlavor = new ArrayList<Integer>();
    }

    public FLAV(IO baselink)
    {
        super(baselink);
        relationWithOtherFlavor = new ArrayList<Integer>();
    }
    public void trim()
    {
        name = name.trim();
    }
    public int getQuestionMark()
    {
        return questionMark;
    }

    public String getName()
    {
        return name;
    }

    public int getNumberOfFlavors()
    {
        return numberOfFlavors;
    }






    public void setQuestionMark(int questionMark)
    {
        this.questionMark = questionMark;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setNumberOfFlavors(int numberOfFlavors)
    {
        this.numberOfFlavors = numberOfFlavors;
    }

    public String toEnglish(){
        return toString();
    }

    @Override
    public String toString()
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn =  "name: " + name + lineReturn;
        toReturn =  toReturn + "questionMark: " + questionMark + lineReturn;
        toReturn = toReturn + "numberOfFlavors: " + numberOfFlavors + lineReturn;
        for (int j = 0; j < numberOfFlavors; j++)
        {
            toReturn =  toReturn + "relation with flavor " + j + ": " + relationWithOtherFlavor.get(j) + lineReturn;
        }
        toReturn = toReturn + lineReturn;
        return toReturn;
    }

    public String compareTo(BIQSection section, String separator)
    {
        if (!(section instanceof FLAV))
            return null;
        FLAV two = (FLAV)section;
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "name: " + name + lineReturn;
        if (!(questionMark == two.getQuestionMark()))
        {
                toReturn = toReturn + "QuestionMark: " + questionMark + separator + two.getQuestionMark() + lineReturn;
        }
        if (!(numberOfFlavors == two.getNumberOfFlavors()))
        {
                toReturn = toReturn + "NumberOfFlavors: " + numberOfFlavors + separator + two.getNumberOfFlavors() + lineReturn;
        }
        if (!(relationWithOtherFlavor.equals(two.relationWithOtherFlavor)))
        {
            toReturn = toReturn + "relationWithOtherFlavor:\n" + relationWithOtherFlavor + "\n|relationWithOtherFlavor(2):\n" + two.relationWithOtherFlavor;
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
