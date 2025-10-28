package com.civfanatics.civ3.biqFile;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Other sections that know about TECH: good, bldg, civ, unit, ctzn, govt, trfm
 * @author Quintillus
 */
import java.io.Serializable;
import java.util.ArrayList;
public class TECH extends BIQSection implements Serializable {
    private int dataLength = 112; //112 in Conquests; 104 in PTW
    //index inherited from BIQSection
    private String name = "";
    private String civilopediaEntry = "";
    private int cost;
    private int era;
    private int advanceIcon;
    private int x;
    private int y;
    private int prerequisite1Int;
    private int prerequisite2Int;
    private int prerequisite3Int;
    private int prerequisite4Int;
    private TECH prerequisite1;
    private TECH prerequisite2;
    private TECH prerequisite3;
    private TECH prerequisite4;
    private int flags;
        private boolean enablesDiplomats;
        private boolean enablesIrrigationWithoutFreshWater;
        private boolean enablesBridges;
        private boolean disablesFloodPlainDisease;
        private boolean enablesConscription;
        private boolean enablesMobilizationLevels;
        private boolean enablesRecycling;
        private boolean enablesPrecisionBombing;
        private boolean enablesMPP;
        private boolean enablesROP;
        private boolean enablesAlliances;
        private boolean enablesTradeEmbargoes;
        private boolean doublesWealth;
        private boolean enablesSeaTrade;
        private boolean enablesOceanTrade;
        private boolean enablesMapTrading;
        private boolean enablesCommunicationTrading;
        private boolean notRequiredForAdvancement;
        private boolean doublesWorkRate;
        private boolean cannotBeTraded;
        private boolean permitsSacrifice;
        private boolean bonusTech;
        private boolean revealMap;
    //the following is Conquests-only
    private int flavors;
    private int numFlavors;
    private ArrayList<Boolean>flavours;
    private int questionMark = 1;    //must not be zero
    
    public TECH(){};
    
    public TECH(IO baselink)
    {
        super(baselink);
        if (baselink.version != civ3Version.CONQUESTS)
            dataLength-=8;
        flavours = new ArrayList<Boolean>();
    }

    public TECH(String name, int numFlavors, IO baselink)
    {
        super(baselink);
        if (baselink.version != civ3Version.CONQUESTS)
            dataLength-=8;
        this.name = name;
        this.era = -1;
        this.prerequisite1Int = -1;
        this.prerequisite2Int = -1;
        this.prerequisite3Int = -1;
        this.prerequisite4Int = -1;
        flavours = new ArrayList<Boolean>();
        for (int i = 0; i < numFlavors; i++)
            flavours.add(false);

    }
    
    public TECH clone() {
        TECH other = new TECH(this.baseLink);
        other.name = this.name;
        other.civilopediaEntry = this.civilopediaEntry;
        other.cost = this.cost;
        other.era = this.era;
        other.advanceIcon = this.advanceIcon;
        other.x = this.x;
        other.y = this.y;
        other.prerequisite1Int = this.prerequisite1Int;
        other.prerequisite2Int = this.prerequisite2Int;
        other.prerequisite3Int = this.prerequisite3Int;
        other.prerequisite4Int = this.prerequisite4Int;
        other.prerequisite1 = this.prerequisite1;
        other.prerequisite2 = this.prerequisite2;
        other.prerequisite3 = this.prerequisite3;
        other.prerequisite4 = this.prerequisite4;
        other.flags = this.flags;
        other.flavors = this.flavors;
        other.numFlavors = this.numFlavors;
        other.questionMark = this.questionMark;
        
        other.extractEnglish(other.numFlavors);
        return other;
    }
    
    public void trim()
    {
        name = name.trim();
        civilopediaEntry = civilopediaEntry.trim();
    }

    public int getDataLength()
    {
        return dataLength;
    }

    @Override
    public String getName()
    {
        return name;
    }

    public String getCivilopediaEntry()
    {
        return civilopediaEntry;
    }

    public int getCost()
    {
        return cost;
    }

    public int getEra()
    {
        return era;
    }

    public int getAdvanceIcon()
    {
        return advanceIcon;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getPrerequisite1()
    {
        return prerequisite1Int;
    }

    public int getPrerequisite2()
    {
        return prerequisite2Int;
    }

    public int getPrerequisite3()
    {
        return prerequisite3Int;
    }

    public int getPrerequisite4()
    {
        return prerequisite4Int;
    }

    public int getFlags()
    {
        return flags;
    }

    public int getFlavors()
    {
        return flavors;
    }
    public int getQuestionMark()
    {
        return questionMark;
    }

    public boolean getEnablesDiplomats()
{
    return enablesDiplomats;
}

    public boolean getEnablesIrrigationWithoutFreshWater()
    {
        return enablesIrrigationWithoutFreshWater;
    }

    public boolean getEnablesBridges()
    {
        return enablesBridges;
    }

    public boolean getDisablesFloodPlainDisease()
    {
        return disablesFloodPlainDisease;
    }

    public boolean getEnablesConscription()
    {
        return enablesConscription;
    }

    public boolean getEnablesMobilizationLevels()
    {
        return enablesMobilizationLevels;
    }

    public boolean getEnablesRecycling()
    {
        return enablesRecycling;
    }

    public boolean getEnablesPrecisionBombing()
    {
        return enablesPrecisionBombing;
    }

    public boolean getEnablesMPP()
    {
        return enablesMPP;
    }

    public boolean getEnablesROP()
    {
        return enablesROP;
    }

    public boolean getEnablesAlliances()
    {
        return enablesAlliances;
    }

    public boolean getEnablesTradeEmbargoes()
    {
        return enablesTradeEmbargoes;
    }

    public boolean getDoublesWealth()
    {
        return doublesWealth;
    }

    public boolean getEnablesSeaTrade()
    {
        return enablesSeaTrade;
    }

    public boolean getEnablesOceanTrade()
    {
        return enablesOceanTrade;
    }

    public boolean getEnablesMapTrading()
    {
        return enablesMapTrading;
    }

    public boolean getEnablesCommunicationTrading()
    {
        return enablesCommunicationTrading;
    }

    public boolean getNotRequiredForAdvancement()
    {
        return notRequiredForAdvancement;
    }

    public boolean getDoublesWorkRate()
    {
        return doublesWorkRate;
    }

    public boolean getCannotBeTraded()
    {
        return cannotBeTraded;
    }

    public boolean getPermitsSacrifice()
    {
        return permitsSacrifice;
    }

    public boolean getBonusTech()
    {
        return bonusTech;
    }

    public boolean getRevealMap()
    {
        return revealMap;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setCivilopediaEntry(String civilopediaEntry)
    {
        this.civilopediaEntry = civilopediaEntry;
    }

    public void setCost(int cost)
    {
        this.cost = cost;
    }

    public void setEra(int era)
    {
        this.era = era;
    }

    public void setAdvanceIcon(int advanceIcon)
    {
        this.advanceIcon = advanceIcon;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }
    
    /**
     * "Refreshes" prereqs - i.e. updates the pointers.  This makes sure they
     * exist in the case where they can't be up-to-date on import due to a tech
     * depending on one that is not yet imported.
     */
    void refreshPrerequisites() {
        setPrerequisite1(prerequisite1Int);
        setPrerequisite2(prerequisite2Int);
        setPrerequisite3(prerequisite3Int);
        setPrerequisite4(prerequisite4Int);
    }

    public void setPrerequisite1(int prerequisite1) {
        this.prerequisite1Int = prerequisite1;
        if (baseLink.technology != null && prerequisite1Int != -1 && baseLink.technology.size() > prerequisite1Int) {
            this.prerequisite1 = baseLink.technology.get(prerequisite1Int);
        }
    }

    public void setPrerequisite2(int prerequisite2) {
        this.prerequisite2Int = prerequisite2;
        if (baseLink.technology != null && prerequisite2Int != -1 && baseLink.technology.size() > prerequisite2Int) {
            this.prerequisite2 = baseLink.technology.get(prerequisite2Int);
        }
    }

    public void setPrerequisite3(int prerequisite3) {
        this.prerequisite3Int = prerequisite3;
        if (baseLink.technology != null && prerequisite3Int != -1 && baseLink.technology.size() > prerequisite3Int) {
            this.prerequisite3 = baseLink.technology.get(prerequisite3Int);
        }
    }

    public void setPrerequisite4(int prerequisite4) {
        this.prerequisite4Int = prerequisite4;
        if (baseLink.technology != null && prerequisite4Int != -1 && baseLink.technology.size() > prerequisite4Int) {
            this.prerequisite4 = baseLink.technology.get(prerequisite4Int);
        }
    }

    public void setFlags(int flags)
    {
        this.flags = flags;
    }

    public void setFlavors(int flavors)
    {
        this.flavors = flavors;
    }
    public void setQuestionMark(int questionMark)
    {
        this.questionMark = questionMark;
    }

    public void setEnablesDiplomats(boolean enablesDiplomats)
    {
        this.enablesDiplomats = enablesDiplomats;
    }

    public void setEnablesIrrigationWithoutFreshWater(boolean enablesIrrigationWithoutFreshWater)
    {
        this.enablesIrrigationWithoutFreshWater = enablesIrrigationWithoutFreshWater;
    }

    public void setEnablesBridges(boolean enablesBridges)
    {
        this.enablesBridges = enablesBridges;
    }

    public void setDisablesFloodPlainDisease(boolean disablesFloodPlainDisease)
    {
        this.disablesFloodPlainDisease = disablesFloodPlainDisease;
    }

    public void setEnablesConscription(boolean enablesConscription)
    {
        this.enablesConscription = enablesConscription;
    }

    public void setEnablesMobilizationLevels(boolean enablesMobilizationLevels)
    {
        this.enablesMobilizationLevels = enablesMobilizationLevels;
    }

    public void setEnablesRecycling(boolean enablesRecycling)
    {
        this.enablesRecycling = enablesRecycling;
    }

    public void setEnablesPrecisionBombing(boolean enablesPrecisionBombing)
    {
        this.enablesPrecisionBombing = enablesPrecisionBombing;
    }

    public void setEnablesMPP(boolean enablesMPP)
    {
        this.enablesMPP = enablesMPP;
    }

    public void setEnablesROP(boolean enablesROP)
    {
        this.enablesROP = enablesROP;
    }

    public void setEnablesAlliances(boolean enablesAlliances)
    {
        this.enablesAlliances = enablesAlliances;
    }

    public void setEnablesTradeEmbargoes(boolean enablesTradeEmbargoes)
    {
        this.enablesTradeEmbargoes = enablesTradeEmbargoes;
    }

    public void setDoublesWealth(boolean doublesWealth)
    {
        this.doublesWealth = doublesWealth;
    }

    public void setEnablesSeaTrade(boolean enablesSeaTrade)
    {
        this.enablesSeaTrade = enablesSeaTrade;
    }

    public void setEnablesOceanTrade(boolean enablesOceanTrade)
    {
        this.enablesOceanTrade = enablesOceanTrade;
    }

    public void setEnablesMapTrading(boolean enablesMapTrading)
    {
        this.enablesMapTrading = enablesMapTrading;
    }

    public void setEnablesCommunicationTrading(boolean enablesCommunicationTrading)
    {
        this.enablesCommunicationTrading = enablesCommunicationTrading;
    }

    public void setNotRequiredForAdvancement(boolean notRequiredForAdvancement)
    {
        this.notRequiredForAdvancement = notRequiredForAdvancement;
    }

    public void setDoublesWorkRate(boolean doublesWorkRate)
    {
        this.doublesWorkRate = doublesWorkRate;
    }

    public void setCannotBeTraded(boolean cannotBeTraded)
    {
        this.cannotBeTraded = cannotBeTraded;
    }

    public void setPermitsSacrifice(boolean permitsSacrifice)
    {
        this.permitsSacrifice = permitsSacrifice;
    }

    public void setBonusTech(boolean bonusTech)
    {
        this.bonusTech = bonusTech;
    }

    public void setRevealMap(boolean revealMap)
    {
        this.revealMap = revealMap;
    }
    
    public int getNumFlavours()
    {
        return this.flavours.size();
    }
    
    public void setFlavour(int index, boolean value)
    {
        this.flavours.set(index, value);
    }
    
    public boolean getFlavour(int index)
    {
        return flavours.get(index);
    }
    
    public void handleAddedFlavour()
    {
        flavours.add(false);
        numFlavors++;
    }
    
    public void handleDeletedFlavour(int index)
    {
        flavours.remove(index);
        numFlavors--;
        createBinary();
        //do need to recreate binary since the flavor got removed
    }
    
    public void handleDeletedTechnology(int deletedTechIndex)
    {
        //Note that we only need to update the TECH pointers if
        //they were deleted; otherwise they are still valid.
        if (prerequisite1Int == deletedTechIndex) {
            prerequisite1Int = -1;
            prerequisite1 = null;
        }
        else if (prerequisite1Int > deletedTechIndex)
            prerequisite1Int--;
        
        if (prerequisite2Int == deletedTechIndex) {
            prerequisite2Int = -1;
            prerequisite2 = null;
        }
        else if (prerequisite2Int > deletedTechIndex)
            prerequisite2Int--;
        
        if (prerequisite3Int == deletedTechIndex) {
            prerequisite3Int = -1;
            prerequisite3 = null;
        }
        else if (prerequisite3Int > deletedTechIndex)
            prerequisite3Int--;
        
        if (prerequisite4Int == deletedTechIndex) {
            prerequisite4Int = -1;
            prerequisite4 = null;
        }
        else if (prerequisite4Int > deletedTechIndex)
            prerequisite4Int--;
        
        if (this.index > deletedTechIndex) {
            this.index--;
        }
        else if (this.index == deletedTechIndex) {
            //getting deleted soon anyway, but just to be safe...
            this.index = -1;
        }
    }
    
    public void handleSwappedTech() {
        if (prerequisite1Int != -1) {
            prerequisite1Int = prerequisite1.getIndex();
        }
        if (prerequisite2Int != -1) {
            prerequisite2Int = prerequisite2.getIndex();
        }
        if (prerequisite3Int != -1) {
            prerequisite3Int = prerequisite3.getIndex();
        }
        if (prerequisite4Int != -1) {
            prerequisite4Int = prerequisite4.getIndex();
        }
    }
    
    public void convertFromPTWToConquests()
    {
        this.dataLength+=8;
    }
	
    public void extractEnglish(int numFlavours)
    {
        int flagCopy = flags;
        int divBy = 0;
        divBy = flagCopy/(int)(Math.pow(2, 22));
        if (divBy == 1)
        {
            revealMap = true;
            flagCopy-=4194304;
        }
        divBy = flagCopy/(int)(Math.pow(2,21));
        if (divBy == 1)
        {
            bonusTech = true;
            flagCopy-=2097152;
        }
        divBy = flagCopy/(int)(Math.pow(2, 20));
        if (divBy == 1)
        {
            permitsSacrifice = true;
            flagCopy-=1048576;
        }
        divBy = flagCopy/(int)(Math.pow(2, 19));
        if (divBy == 1)
        {
            cannotBeTraded = true;
            flagCopy-=524288;
        }
        divBy = flagCopy/(int)(Math.pow(2, 18));
        if (divBy == 1)
        {
            doublesWorkRate = true;
            flagCopy-=262144;
        }
        divBy = flagCopy/(int)(Math.pow(2, 17));
        if (divBy == 1)
        {
            notRequiredForAdvancement = true;
            flagCopy-=131072;
        }
        divBy = flagCopy/(int)(Math.pow(2, 16));
        if (divBy == 1)
        {
            enablesCommunicationTrading = true;
            flagCopy-=65536;
        }
        divBy = flagCopy/(int)(Math.pow(2, 15));
        if (divBy == 1)
        {
            enablesMapTrading = true;
            flagCopy-=32768;
        }
        divBy = flagCopy/(int)(Math.pow(2, 14));
        if (divBy == 1)
        {
            enablesOceanTrade = true;
            flagCopy-=16384;
        }
        divBy = flagCopy/(int)(Math.pow(2, 13));
        if (divBy == 1)
        {
            enablesSeaTrade = true;
            flagCopy-=8192;
        }
        divBy = flagCopy/(int)(Math.pow(2, 12));
        if (divBy == 1)
        {
            doublesWealth = true;
            flagCopy-=4096;
        }
        divBy = flagCopy/(int)(Math.pow(2, 11));
        if (divBy == 1)
        {
            enablesTradeEmbargoes = true;
            flagCopy-=2048;
        }
        divBy = flagCopy/(int)(Math.pow(2, 10));
        if (divBy == 1)
        {
            enablesAlliances = true;
            flagCopy-=1024;
        }
        divBy = flagCopy/(int)(Math.pow(2, 9));
        if (divBy == 1)
        {
            enablesROP = true;
            flagCopy-=512;
        }
        divBy = flagCopy/(int)(Math.pow(2, 8));
        if (divBy == 1)
        {
            enablesMPP = true;
            flagCopy-=256;
        }
        divBy = flagCopy/(int)(Math.pow(2, 7));
        if (divBy == 1)
        {
            enablesPrecisionBombing = true;
            flagCopy-=128;
        }
        divBy = flagCopy/(int)(Math.pow(2, 6));
        if (divBy == 1)
        {
            enablesRecycling = true;
            flagCopy-=64;
        }
        divBy = flagCopy/(int)(Math.pow(2, 5));
        if (divBy == 1)
        {
            enablesMobilizationLevels = true;
            flagCopy-=32;
        }
        divBy = flagCopy/(int)(Math.pow(2, 4));
        if (divBy == 1)
        {
            enablesConscription = true;
            flagCopy-=16;
        }
        divBy = flagCopy/(int)(Math.pow(2, 3));
        if (divBy == 1)
        {
            disablesFloodPlainDisease = true;
            flagCopy-=8;
        }
        divBy = flagCopy/(int)(Math.pow(2, 2));
        if (divBy == 1)
        {
            enablesBridges = true;
            flagCopy-=4;
        }
        divBy = flagCopy/(int)(Math.pow(2, 1));
        if (divBy == 1)
        {
            enablesIrrigationWithoutFreshWater = true;
            flagCopy-=2;
        }
        divBy = flagCopy/(int)(Math.pow(2, 0));
        if (divBy == 1)
        {
            enablesDiplomats = true;
            flagCopy-=1;
        }
        //flavours
        int flavorCopy = flavors;
        for (int i = 0; i < numFlavours; i++)
        {
            flavours.add(false);
        }
        //System.out.println("numFlavours: " + numFlavours);
        //System.out.println("flavours.size(): " + flavours.size());
        for (int i = numFlavours - 1; i > -1; i--)
        {
            //System.out.println("i : " + i);
            divBy = flavorCopy/(int)(Math.pow(2, i));
            if (divBy == 1)
            {
                flavours.set(i, true);
                flavorCopy-=Math.pow(2, i);
            }
        }
        this.numFlavors = numFlavours;
    }

    public void createBinary()
    {
        //flags
        long sum = 0;
        if (enablesDiplomats)
            sum++;
        if (this.enablesIrrigationWithoutFreshWater)
            sum+=2;
        if (this.enablesBridges)
            sum+=4;
        if (this.disablesFloodPlainDisease)
            sum+=8;
        if (this.enablesConscription)
            sum+=16;
        if (this.enablesMobilizationLevels)
            sum+=32;
        if (this.enablesRecycling)
            sum+=64;
        if (this.enablesPrecisionBombing)
            sum+=128;
        if (this.enablesMPP)
            sum+=256;
        if (this.enablesROP)
            sum+=512;
        if (this.enablesAlliances)
            sum+=1024;
        if (this.enablesTradeEmbargoes)
            sum+=2048;
        if (this.doublesWealth)
            sum+=4096;
        if (this.enablesSeaTrade)
            sum+=8192;
        if (this.enablesOceanTrade)
            sum+=16384;
        if (this.enablesMapTrading)
            sum+=32768;
        if (this.enablesCommunicationTrading)
            sum+=65536;
        if (this.notRequiredForAdvancement)
            sum+=131072;
        if (this.doublesWorkRate)
            sum+=262144;
        if (this.cannotBeTraded)
            sum+=524288;
        if (this.permitsSacrifice)
            sum+=1048576;
        if (this.bonusTech)
            sum+=2097152;
        if (this.revealMap)
            sum+=4194304;
        this.flags = (int)sum;
        //flavours
        sum = 0;
        for (int i = 0; i < this.flavours.size(); i++)
            if (flavours.get(i))
                sum+=(int)Math.pow(2, i);
        flavors = (int)sum;
    }

    @Override
    public String toString()
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "name: " + name + lineReturn;
        toReturn = toReturn + "index: " + index + lineReturn;
        toReturn = toReturn + "civilopediaEntry: " + civilopediaEntry + lineReturn;
        toReturn = toReturn + "dataLength: " + dataLength + lineReturn;
        toReturn = toReturn + "cost: " + cost + lineReturn;
        toReturn = toReturn + "era: " + era + lineReturn;
        toReturn = toReturn + "advanceIcon: " + advanceIcon + lineReturn;
        toReturn = toReturn + "x: " + x + lineReturn;
        toReturn = toReturn + "y: " + y + lineReturn;
        toReturn = toReturn + "prerequisite1: " + prerequisite1Int + lineReturn;
        toReturn = toReturn + "prerequisite2: " + prerequisite2Int + lineReturn;
        toReturn = toReturn + "prerequisite3: " + prerequisite3Int + lineReturn;
        toReturn = toReturn + "prerequisite4: " + prerequisite4Int + lineReturn;
        toReturn = toReturn + "flags: " + flags + lineReturn;
        toReturn = toReturn + "flavors: " + flavors + lineReturn;
        toReturn = toReturn + "questionMark: " + questionMark + lineReturn;
        toReturn = toReturn + lineReturn;
        return toReturn;
    }
    public String toEnglish()
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "name: " + name + lineReturn;
        toReturn = toReturn + "index: " + index + lineReturn;
        toReturn = toReturn + "civilopediaEntry: " + civilopediaEntry + lineReturn;
        toReturn = toReturn + "dataLength: " + dataLength + lineReturn;
        toReturn = toReturn + "cost: " + cost + lineReturn;
        toReturn = toReturn + "era: " + era + lineReturn;
        toReturn = toReturn + "advanceIcon: " + advanceIcon + lineReturn;
        toReturn = toReturn + "x: " + x + lineReturn;
        toReturn = toReturn + "y: " + y + lineReturn;
        String prereq1Eng = prerequisite1Int == -1 ? "None" : this.baseLink.technology.get(prerequisite1Int).getName();
        String prereq2Eng = prerequisite2Int == -1 ? "None" : this.baseLink.technology.get(prerequisite2Int).getName();
        String prereq3Eng = prerequisite3Int == -1 ? "None" : this.baseLink.technology.get(prerequisite3Int).getName();
        String prereq4Eng = prerequisite4Int == -1 ? "None" : this.baseLink.technology.get(prerequisite4Int).getName();
        toReturn = toReturn + "prerequisite1: " + prereq1Eng + lineReturn;
        toReturn = toReturn + "prerequisite2: " + prereq2Eng + lineReturn;
        toReturn = toReturn + "prerequisite3: " + prereq3Eng + lineReturn;
        toReturn = toReturn + "prerequisite4: " + prereq4Eng + lineReturn;
        toReturn = toReturn + "flags: " + flags + lineReturn;
        toReturn = toReturn + "  enablesDiplomats: " + enablesDiplomats + lineReturn;
        toReturn = toReturn + "  enablesIrrigationWithoutFreshWater: " + enablesIrrigationWithoutFreshWater + lineReturn;
        toReturn = toReturn + "  enablesBridges: " + enablesBridges + lineReturn;
        toReturn = toReturn + "  disablesFloodPlainDisease: " + disablesFloodPlainDisease + lineReturn;
        toReturn = toReturn + "  enablesConscription: " + enablesConscription + lineReturn;
        toReturn = toReturn + "  enablesMobilizationLevels: " + enablesMobilizationLevels + lineReturn;
        toReturn = toReturn + "  enablesRecycling: " + enablesRecycling + lineReturn;
        toReturn = toReturn + "  enablesPrecisionBombing: " + enablesPrecisionBombing + lineReturn;
        toReturn = toReturn + "  enablesMPP: " + enablesMPP + lineReturn;
        toReturn = toReturn + "  enablesROP: " + enablesROP + lineReturn;
        toReturn = toReturn + "  enablesAlliances: " + enablesAlliances + lineReturn;
        toReturn = toReturn + "  enablesTradeEmbargoes: " + enablesTradeEmbargoes + lineReturn;
        toReturn = toReturn + "  doublesWealth: " + doublesWealth + lineReturn;
        toReturn = toReturn + "  enablesSeaTrade: " + enablesSeaTrade + lineReturn;
        toReturn = toReturn + "  enablesOceanTrade: " + enablesOceanTrade + lineReturn;
        toReturn = toReturn + "  enablesMapTrading: " + enablesMapTrading + lineReturn;
        toReturn = toReturn + "  enablesCommunicationTrading: " + enablesCommunicationTrading + lineReturn;
        toReturn = toReturn + "  notRequiredForAdvancement: " + notRequiredForAdvancement + lineReturn;
        toReturn = toReturn + "  doublesWorkRate: " + doublesWorkRate + lineReturn;
        toReturn = toReturn + "  cannotBeTraded: " + cannotBeTraded + lineReturn;
        toReturn = toReturn + "  permitsSacrifice: " + permitsSacrifice + lineReturn;
        toReturn = toReturn + "  bonusTech: " + bonusTech + lineReturn;
        toReturn = toReturn + "  revealMap: " + revealMap + lineReturn;
        toReturn = toReturn + "flavors: " + flavors + lineReturn;
        for (int i = 0; i < numFlavors; i++)
        {
            toReturn = toReturn + "  flavor " + (i + 1) + ": " + flavours.get(i) + lineReturn;
        }
        toReturn = toReturn + lineReturn;
        return toReturn;
    }

    public String compareTo(BIQSection section, String separator)
    {
        if (!(section instanceof TECH))
            return null;
        TECH two = (TECH)section;
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "name: " + name + lineReturn;
        if (!(dataLength == two.getDataLength()))
        {
                toReturn = toReturn + "DataLength: " + dataLength + separator + two.getDataLength() + lineReturn;
        }
        if (index != two.getIndex()) {
            toReturn = toReturn = "index: " + index + separator + two.getIndex() + lineReturn;
        }
        if (civilopediaEntry.compareTo(two.getCivilopediaEntry()) != 0)
        {
                toReturn = toReturn + "CivilopediaEntry: " + civilopediaEntry + separator + two.getCivilopediaEntry() + lineReturn;
        }
        if (!(cost == two.getCost()))
        {
                toReturn = toReturn + "Cost: " + cost + separator + two.getCost() + lineReturn;
        }
        if (!(era == two.getEra()))
        {
                toReturn = toReturn + "Era: " + era + separator + two.getEra() + lineReturn;
        }
        if (!(advanceIcon == two.getAdvanceIcon()))
        {
                toReturn = toReturn + "AdvanceIcon: " + advanceIcon + separator + two.getAdvanceIcon() + lineReturn;
        }
        if (!(x == two.getX()))
        {
                toReturn = toReturn + "X: " + x + separator + two.getX() + lineReturn;
        }
        if (!(y == two.getY()))
        {
                toReturn = toReturn + "Y: " + y + separator + two.getY() + lineReturn;
        }
        if (!(prerequisite1Int == two.getPrerequisite1()))
        {
                toReturn = toReturn + "Prerequisite1: " + prerequisite1Int + separator + two.getPrerequisite1() + lineReturn;
        }
        if (!(prerequisite2Int == two.getPrerequisite2()))
        {
                toReturn = toReturn + "Prerequisite2: " + prerequisite2Int + separator + two.getPrerequisite2() + lineReturn;
        }
        if (!(prerequisite3Int == two.getPrerequisite3()))
        {
                toReturn = toReturn + "Prerequisite3: " + prerequisite3Int + separator + two.getPrerequisite3() + lineReturn;
        }
        if (!(prerequisite4Int == two.getPrerequisite4()))
        {
                toReturn = toReturn + "Prerequisite4: " + prerequisite4Int + separator + two.getPrerequisite4() + lineReturn;
        }
        if (!(flags == two.getFlags()))
        {
                toReturn = toReturn + "Flags: " + flags + separator + two.getFlags() + lineReturn;
        }
        if (!(enablesDiplomats == two.getEnablesDiplomats()))
        {
                toReturn = toReturn + "  EnablesDiplomats: " + enablesDiplomats + separator + two.getEnablesDiplomats() + lineReturn;
        }
        if (!(enablesIrrigationWithoutFreshWater == two.getEnablesIrrigationWithoutFreshWater()))
        {
                toReturn = toReturn + "  EnablesIrrigationWithoutFreshWater: " + enablesIrrigationWithoutFreshWater + separator + two.getEnablesIrrigationWithoutFreshWater() + lineReturn;
        }
        if (!(enablesBridges == two.getEnablesBridges()))
        {
                toReturn = toReturn + "  EnablesBridges: " + enablesBridges + separator + two.getEnablesBridges() + lineReturn;
        }
        if (!(disablesFloodPlainDisease == two.getDisablesFloodPlainDisease()))
        {
                toReturn = toReturn + "  DisablesFloodPlainDisease: " + disablesFloodPlainDisease + separator + two.getDisablesFloodPlainDisease() + lineReturn;
        }
        if (!(enablesConscription == two.getEnablesConscription()))
        {
                toReturn = toReturn + "  EnablesConscription: " + enablesConscription + separator + two.getEnablesConscription() + lineReturn;
        }
        if (!(enablesMobilizationLevels == two.getEnablesMobilizationLevels()))
        {
                toReturn = toReturn + "  EnablesMobilizationLevels: " + enablesMobilizationLevels + separator + two.getEnablesMobilizationLevels() + lineReturn;
        }
        if (!(enablesRecycling == two.getEnablesRecycling()))
        {
                toReturn = toReturn + "  EnablesRecycling: " + enablesRecycling + separator + two.getEnablesRecycling() + lineReturn;
        }
        if (!(enablesPrecisionBombing == two.getEnablesPrecisionBombing()))
        {
                toReturn = toReturn + "  EnablesPrecisionBombing: " + enablesPrecisionBombing + separator + two.getEnablesPrecisionBombing() + lineReturn;
        }
        if (!(enablesMPP == two.getEnablesMPP()))
        {
                toReturn = toReturn + "  EnablesMPP: " + enablesMPP + separator + two.getEnablesMPP() + lineReturn;
        }
        if (!(enablesROP == two.getEnablesROP()))
        {
                toReturn = toReturn + "  EnablesROP: " + enablesROP + separator + two.getEnablesROP() + lineReturn;
        }
        if (!(enablesAlliances == two.getEnablesAlliances()))
        {
                toReturn = toReturn + "  EnablesAlliances: " + enablesAlliances + separator + two.getEnablesAlliances() + lineReturn;
        }
        if (!(enablesTradeEmbargoes == two.getEnablesTradeEmbargoes()))
        {
                toReturn = toReturn + "  EnablesTradeEmbargoes: " + enablesTradeEmbargoes + separator + two.getEnablesTradeEmbargoes() + lineReturn;
        }
        if (!(doublesWealth == two.getDoublesWealth()))
        {
                toReturn = toReturn + "  DoublesWealth: " + doublesWealth + separator + two.getDoublesWealth() + lineReturn;
        }
        if (!(enablesSeaTrade == two.getEnablesSeaTrade()))
        {
                toReturn = toReturn + "  EnablesSeaTrade: " + enablesSeaTrade + separator + two.getEnablesSeaTrade() + lineReturn;
        }
        if (!(enablesOceanTrade == two.getEnablesOceanTrade()))
        {
                toReturn = toReturn + "  EnablesOceanTrade: " + enablesOceanTrade + separator + two.getEnablesOceanTrade() + lineReturn;
        }
        if (!(enablesMapTrading == two.getEnablesMapTrading()))
        {
                toReturn = toReturn + "  EnablesMapTrading: " + enablesMapTrading + separator + two.getEnablesMapTrading() + lineReturn;
        }
        if (!(enablesCommunicationTrading == two.getEnablesCommunicationTrading()))
        {
                toReturn = toReturn + "  EnablesCommunicationTrading: " + enablesCommunicationTrading + separator + two.getEnablesCommunicationTrading() + lineReturn;
        }
        if (!(notRequiredForAdvancement == two.getNotRequiredForAdvancement()))
        {
                toReturn = toReturn + "  NotRequiredForAdvancement: " + notRequiredForAdvancement + separator + two.getNotRequiredForAdvancement() + lineReturn;
        }
        if (!(doublesWorkRate == two.getDoublesWorkRate()))
        {
                toReturn = toReturn + "  DoublesWorkRate: " + doublesWorkRate + separator + two.getDoublesWorkRate() + lineReturn;
        }
        if (!(cannotBeTraded == two.getCannotBeTraded()))
        {
                toReturn = toReturn + "  CannotBeTraded: " + cannotBeTraded + separator + two.getCannotBeTraded() + lineReturn;
        }
        if (!(permitsSacrifice == two.getPermitsSacrifice()))
        {
                toReturn = toReturn + "  PermitsSacrifice: " + permitsSacrifice + separator + two.getPermitsSacrifice() + lineReturn;
        }
        if (!(bonusTech == two.getBonusTech()))
        {
                toReturn = toReturn + "  BonusTech: " + bonusTech + separator + two.getBonusTech() + lineReturn;
        }
        if (!(revealMap == two.getRevealMap()))
        {
                toReturn = toReturn + "  RevealMap: " + revealMap + separator + two.getRevealMap() + lineReturn;
        }
        if (!(flavors == two.getFlavors()))
        {
                toReturn = toReturn + "Flavors: " + flavors + separator + two.getFlavors() + lineReturn;
        }
        else  //see if a flavor is checked for a technology in only one file
        {
            for (int i = 0; i < numFlavors; i++)
            {
                if (!(flavours.get(i) == two.flavours.get(i)))
                {
                    toReturn = toReturn + "  flavor " + i + ": " + flavours.get(i) + separator + two.flavours.get(i) + lineReturn;
                }
            }
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

    public int getNumFlavors() {
        return numFlavors;
    }

    public void setNumFlavors(int numFlavors) {
        this.numFlavors = numFlavors;
    }

    public ArrayList<Boolean> getFlavours() {
        return flavours;
    }

    public void setFlavours(ArrayList<Boolean> flavours) {
        this.flavours = flavours;
    }
    
}
