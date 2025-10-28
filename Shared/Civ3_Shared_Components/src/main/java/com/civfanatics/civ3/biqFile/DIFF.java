package com.civfanatics.civ3.biqFile;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Other sections that know about DIFF: rule
 * @author Quintillus
 */
public class DIFF extends BIQSection{
    private int dataLength = 120;
    private String name = "";
    private int contentCitizens;
    private int maxGovtTransition;
    private int AIDefenceStart;
    private int AIOffenceStart;
    private int extraStart1;
    private int extraStart2;
    private int additionalFreeSupport;
    private int bonusPerCity;
    private int attackBarbariansBonus;
    private int costFactor = 10;
    private int percentOptimal;
    private int AIAITrade = 100;
    private int corruptionPercent;
    private int militaryLaw = 1;    //citizens quelled per military unit
    public DIFF(IO baselink)
    {
        super(baselink);
    }

    public DIFF(String name, IO baselink)
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

    public void setContentCitizens(int contentCitizens)
    {
        this.contentCitizens = contentCitizens;
    }

    public void setMaxGovtTransition(int maxGovtTransition)
    {
        this.maxGovtTransition = maxGovtTransition;
    }

    public void setAIDefenceStart(int AIDefenceStart)
    {
        this.AIDefenceStart = AIDefenceStart;
    }

    public void setAIOffenceStart(int AIOffenceStart)
    {
        this.AIOffenceStart = AIOffenceStart;
    }

    public void setExtraStart1(int extraStart1)
    {
        this.extraStart1 = extraStart1;
    }

    public void setExtraStart2(int extraStart2)
    {
        this.extraStart2 = extraStart2;
    }

    public void setAdditionalFreeSupport(int additionalFreeSupport)
    {
        this.additionalFreeSupport = additionalFreeSupport;
    }

    public void setBonusPerCity(int bonusPerCity)
    {
        this.bonusPerCity = bonusPerCity;
    }

    public void setAttackBarbariansBonus(int attackBarbariansBonus)
    {
        this.attackBarbariansBonus = attackBarbariansBonus;
    }

    public void setCostFactor(int costFactor)
    {
        this.costFactor = costFactor;
    }

    public void setPercentOptimal(int percentOptimal)
    {
        this.percentOptimal = percentOptimal;
    }

    public void setAIAITrade(int AIAITrade)
    {
        this.AIAITrade = AIAITrade;
    }

    public void setCorruptionPercent(int corruptionPercent)
    {
        this.corruptionPercent = corruptionPercent;
    }

    public void setMilitaryLaw(int militaryLaw)
    {
        this.militaryLaw = militaryLaw;
    }

    public int getDataLength()
    {
        return dataLength;
    }

    public String getName()
    {
        return name;
    }

    public int getContentCitizens()
    {
        return contentCitizens;
    }

    public int getMaxGovtTransition()
    {
        return maxGovtTransition;
    }

    public int getAIDefenceStart()
    {
        return AIDefenceStart;
    }

    public int getAIOffenceStart()
    {
        return AIOffenceStart;
    }

    public int getExtraStart1()
    {
        return extraStart1;
    }

    public int getExtraStart2()
    {
        return extraStart2;
    }

    public int getAdditionalFreeSupport()
    {
        return additionalFreeSupport;
    }

    public int getBonusPerCity()
    {
        return bonusPerCity;
    }

    public int getAttackBarbariansBonus()
    {
        return attackBarbariansBonus;
    }

    public int getCostFactor()
    {
        return costFactor;
    }

    public int getPercentOptimal()
    {
        return percentOptimal;
    }

    public int getAIAITrade()
    {
        return AIAITrade;
    }

    public int getCorruptionPercent()
    {
        return corruptionPercent;
    }

    public int getMilitaryLaw()
    {
        return militaryLaw;
    }

    public String toEnglish(){
        return toString();
    }

    public String toString()
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn =  "name: "+ name + lineReturn;
        toReturn = toReturn + "dataLength: " + dataLength + lineReturn;
        toReturn = toReturn + "contentCitizens: "+ contentCitizens + lineReturn;
        toReturn = toReturn + "maxGovtTransition: "+ maxGovtTransition + lineReturn;
        toReturn = toReturn + "AIDefenceStart: "+ AIDefenceStart + lineReturn;
        toReturn = toReturn + "AIOffenceStart: "+ AIOffenceStart + lineReturn;
        toReturn = toReturn + "extraStart1: "+ extraStart1 + lineReturn;
        toReturn = toReturn + "extraStart2: "+ extraStart2 + lineReturn;
        toReturn = toReturn + "additionalFreeSupport: "+ additionalFreeSupport + lineReturn;
        toReturn = toReturn + "bonusPerCity: "+ bonusPerCity + lineReturn;
        toReturn = toReturn + "attackBarbariansBonus: "+ attackBarbariansBonus + lineReturn;
        toReturn = toReturn + "costFactor: "+ costFactor + lineReturn;
        toReturn = toReturn + "percentOptimal: "+ percentOptimal + lineReturn;
        toReturn = toReturn + "AIAITrade: "+ AIAITrade + lineReturn;
        toReturn = toReturn + "corruptionPercent: "+ corruptionPercent + lineReturn;
        toReturn = toReturn + "militaryLaw: "+ militaryLaw + lineReturn;
        toReturn = toReturn + lineReturn;
        return toReturn;
    }

    public String compareTo(BIQSection section, String separator)
    {
        if (!(section instanceof DIFF))
            return null;
        DIFF two = (DIFF)section;
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "name: " + name + lineReturn;
        if (!(dataLength == two.getDataLength()))
        {
                toReturn = toReturn + "DataLength: " + dataLength + separator + two.getDataLength() + lineReturn;
        }
        if (!(contentCitizens == two.getContentCitizens()))
        {
                toReturn = toReturn + "ContentCitizens: " + contentCitizens + separator + two.getContentCitizens() + lineReturn;
        }
        if (!(maxGovtTransition == two.getMaxGovtTransition()))
        {
                toReturn = toReturn + "MaxGovtTransition: " + maxGovtTransition + separator + two.getMaxGovtTransition() + lineReturn;
        }
        if (!(AIDefenceStart == two.getAIDefenceStart()))
        {
                toReturn = toReturn + "AIDefenceStart: " + AIDefenceStart + separator + two.getAIDefenceStart() + lineReturn;
        }
        if (!(AIOffenceStart == two.getAIOffenceStart()))
        {
                toReturn = toReturn + "AIOffenceStart: " + AIOffenceStart + separator + two.getAIOffenceStart() + lineReturn;
        }
        if (!(extraStart1 == two.getExtraStart1()))
        {
                toReturn = toReturn + "ExtraStart1: " + extraStart1 + separator + two.getExtraStart1() + lineReturn;
        }
        if (!(extraStart2 == two.getExtraStart2()))
        {
                toReturn = toReturn + "ExtraStart2: " + extraStart2 + separator + two.getExtraStart2() + lineReturn;
        }
        if (!(additionalFreeSupport == two.getAdditionalFreeSupport()))
        {
                toReturn = toReturn + "AdditionalFreeSupport: " + additionalFreeSupport + separator + two.getAdditionalFreeSupport() + lineReturn;
        }
        if (!(bonusPerCity == two.getBonusPerCity()))
        {
                toReturn = toReturn + "BonusPerCity: " + bonusPerCity + separator + two.getBonusPerCity() + lineReturn;
        }
        if (!(attackBarbariansBonus == two.getAttackBarbariansBonus()))
        {
                toReturn = toReturn + "AttackBarbariansBonus: " + attackBarbariansBonus + separator + two.getAttackBarbariansBonus() + lineReturn;
        }
        if (!(costFactor == two.getCostFactor()))
        {
                toReturn = toReturn + "CostFactor: " + costFactor + separator + two.getCostFactor() + lineReturn;
        }
        if (!(percentOptimal == two.getPercentOptimal()))
        {
                toReturn = toReturn + "PercentOptimal: " + percentOptimal + separator + two.getPercentOptimal() + lineReturn;
        }
        if (!(AIAITrade == two.getAIAITrade()))
        {
                toReturn = toReturn + "AIAITrade: " + AIAITrade + separator + two.getAIAITrade() + lineReturn;
        }
        if (!(corruptionPercent == two.getCorruptionPercent()))
        {
                toReturn = toReturn + "CorruptionPercent: " + corruptionPercent + separator + two.getCorruptionPercent() + lineReturn;
        }
        if (!(militaryLaw == two.getMilitaryLaw()))
        {
                toReturn = toReturn + "MilitaryLaw: " + militaryLaw + separator + two.getMilitaryLaw() + lineReturn;
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
