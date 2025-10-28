package com.civfanatics.civ3.biqFile;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Other sections that know about ERAS: Civ, Tech
 * @author Quintillus
 */
public class ERAS extends BIQSection{
    //Todo: Handle the additional question mark in Conquests
    private int dataLength = 264;
    private String name = "";
    private String civilopediaEntry = "";
    private String researcher1 = "";
    private String researcher2 = "";
    private String researcher3 = "";
    private String researcher4 = "";
    private String researcher5 = "";
    private int usedResearcherNames;
    private int questionMark = 1;
    public ERAS(IO baselink)
    {
        super(baselink);
    }
    public void trim()
    {
        name = name.trim();
        civilopediaEntry = civilopediaEntry.trim();
        researcher1 = researcher1.trim();
        researcher2 = researcher2.trim();
        researcher3 = researcher3.substring(0, 1) + researcher3.substring(1).trim();
        researcher4 = researcher4.substring(0, 1) + researcher4.substring(1).trim();
        researcher5 = researcher5.substring(0, 1) + researcher5.substring(1).trim();
    }
    public int getDataLength()
    {
        return dataLength;
    }

    public String getName()
    {
        return name;
    }
    public String getEraName()
    {
        return name;
    }

    public String getCivilopediaEntry()
    {
        return civilopediaEntry;
    }

    public String getResearcher1()
    {
        return researcher1;
    }

    public String getResearcher2()
    {
        return researcher2;
    }

    public String getResearcher3()
    {
        return researcher3;
    }

    public String getResearcher4()
    {
        return researcher4;
    }

    public String getResearcher5()
    {
        return researcher5;
    }

    public int getUsedResearcherNames()
    {
        return usedResearcherNames;
    }

    public int getQuestionMark()
    {
        return questionMark;
    }

    public void setEraName(String name)
    {
        this.name = name;
    }

    public void setCivilopediaEntry(String civilopediaEntry)
    {
        this.civilopediaEntry = civilopediaEntry;
    }

    public void setResearcher1(String researcher1)
    {
        this.researcher1 = researcher1;
    }

    public void setResearcher2(String researcher2)
    {
        this.researcher2 = researcher2;
    }

    public void setResearcher3(String researcher3)
    {
        this.researcher3 = researcher3;
    }

    public void setResearcher4(String researcher4)
    {
        this.researcher4 = researcher4;
    }

    public void setResearcher5(String researcher5)
    {
        this.researcher5 = researcher5;
    }

    public void setUsedResearcherNames(int usedResearcherNames)
    {
        this.usedResearcherNames = usedResearcherNames;
    }

    public void setQuestionMark(int questionMark)
    {
        this.questionMark = questionMark;
    }

    public String toEnglish(){
        return toString();
    }

    public String toString()
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn =  "name: " + name + lineReturn;
        toReturn = toReturn + "civilopediaEntry: " + civilopediaEntry + lineReturn;
        toReturn = toReturn + "dataLength: " + dataLength + lineReturn;
        toReturn = toReturn + "researcher1: " + researcher1 + lineReturn;
        toReturn = toReturn + "researcher2: " + researcher2 + lineReturn;
        toReturn = toReturn + "researcher3: " + researcher3 + lineReturn;
        toReturn = toReturn + "researcher4: " + researcher4 + lineReturn;
        toReturn = toReturn + "researcher5: " + researcher5 + lineReturn;
        toReturn = toReturn + "usedResearcherNames: " + usedResearcherNames + lineReturn;
        toReturn = toReturn + "questionMark: " + questionMark + lineReturn;
        toReturn = toReturn + lineReturn;
        return toReturn;
    }
    public String compareTo(BIQSection section, String separator)
    {
        if (!(section instanceof ERAS))
            return null;
        ERAS two = (ERAS)section;
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
        if (researcher1.compareTo(two.getResearcher1()) != 0)
        {
                toReturn = toReturn + "Researcher1: " + researcher1 + separator + two.getResearcher1() + lineReturn;
        }
        if (researcher2.compareTo(two.getResearcher2()) != 0)
        {
                toReturn = toReturn + "Researcher2: " + researcher2 + separator + two.getResearcher2() + lineReturn;
        }
        if (researcher3.compareTo(two.getResearcher3()) != 0)
        {
                toReturn = toReturn + "Researcher3: " + researcher3 + separator + two.getResearcher3() + lineReturn;
        }
        if (researcher4.compareTo(two.getResearcher4()) != 0)
        {
                toReturn = toReturn + "Researcher4: " + researcher4 + separator + two.getResearcher4() + lineReturn;
        }
        if (researcher5.compareTo(two.getResearcher5()) != 0)
        {
                toReturn = toReturn + "Researcher5: " + researcher5 + separator + two.getResearcher5() + lineReturn;
        }
        if (!(usedResearcherNames == two.getUsedResearcherNames()))
        {
                toReturn = toReturn + "UsedResearcherNames: " + usedResearcherNames + separator + two.getUsedResearcherNames() + lineReturn;
        }
        if (!(questionMark == two.getQuestionMark()))
        {
                toReturn = toReturn + "QuestionMark: " + questionMark + separator + two.getQuestionMark() + lineReturn;
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
