package com.civfanatics.civ3.biqFile;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Other sections that know about GOVT: bldg, civ
 * @author Quintillus
 */
import java.util.*;
public class GOVT extends BIQSection {

    private char iWithAccent = '\u00CC';
    public int dataLength = 472;
    public int defaultType;
    public int transitionType;
    public int requiresMaintenance;
    public int questionMarkOne;
    public int tilePenalty;
    public int commerceBonus;
    public String name = "";
    public String civilopediaEntry = "";
    public String maleRulerTitle1 = "";
    public String femaleRulerTitle1 = "";
    public String maleRulerTitle2 = "";
    public String femaleRulerTitle2 = "";
    public String maleRulerTitle3 = "";
    public String femaleRulerTitle3 = "";
    public String maleRulerTitle4 = "";
    public String femaleRulerTitle4 = "";
    public int corruption;
    public int immuneTo;
    public int diplomatLevel;
    public int spyLevel;
    public int numberOfGovernments;
    public ArrayList<GOVTGOVTRelations>relations;
    public int hurrying;
    public int assimilationChance;
    public int draftLimit;
    public int militaryPoliceLimit;
    public int rulerTitlePairsUsed;
    public int prerequisiteInt;
    public TECH prerequisiteTechnology;
    public int scienceCap;
    public int workerRate;
    public int questionMarkTwo;
    public int questionMarkThree;
    public int questionMarkFour;
    public int freeUnits;
    public int freeUnitsPerTown;
    public int freeUnitsPerCity;
    public int freeUnitsPerMetropolis;
    public int costPerUnit;    
    public int warWeariness;
    public int xenophobic;
    public int forceResettlement;
    public GOVT(IO baselink)
    {
        super(baselink);
        relations = new ArrayList<GOVTGOVTRelations>();
    }
    public GOVT(String name, int numGovs, IO baselink)
    {
        super(baselink);
        this.name = name;
        prerequisiteInt = -1;
        immuneTo = -1;
        numberOfGovernments = numGovs;
        dataLength = 472 + numGovs*12;  //to account for the govtgovtrelations varying size
        relations = new ArrayList<GOVTGOVTRelations>();
        for (int i = 0; i <= numGovs; i++)
            relations.add(new GOVTGOVTRelations());
    }
    public void trim()
    {
        name = name.trim();
        civilopediaEntry = civilopediaEntry.trim();
        //Tricky: if you just try to use String.replace(char, char) or String.replaceAll(regex, String) on these
        //strings, it won't consistently work.  That's because the first character is sometimes a null.  When the
        //first character is a null, it won't apply the operation to the remainder of the String.  This may be
        //due to how the underlying implementation works - such behavior would make sense in the C context
        femaleRulerTitle1 = femaleRulerTitle1.charAt(0) + femaleRulerTitle1.substring(1).replace(iWithAccent, '\u0000');
        femaleRulerTitle2 = femaleRulerTitle2.charAt(0) + femaleRulerTitle2.substring(1).replace(iWithAccent, '\u0000');
        femaleRulerTitle3= femaleRulerTitle3.charAt(0) + femaleRulerTitle3.substring(1).replace(iWithAccent, '\u0000');
        femaleRulerTitle4 = femaleRulerTitle4.charAt(0) + femaleRulerTitle4.substring(1).replace(iWithAccent, '\u0000');
        maleRulerTitle1 = maleRulerTitle1.charAt(0) + maleRulerTitle1.substring(1).replace(iWithAccent, '\u0000');
        maleRulerTitle2 = maleRulerTitle2.charAt(0) + maleRulerTitle2.substring(1).replace(iWithAccent, '\u0000');
        maleRulerTitle3 = maleRulerTitle3.charAt(0) + maleRulerTitle3.substring(1).replace(iWithAccent, '\u0000');
        maleRulerTitle4 = maleRulerTitle4.charAt(0) + maleRulerTitle4.substring(1).replace(iWithAccent, '\u0000');
        femaleRulerTitle1 = femaleRulerTitle1.charAt(0) + femaleRulerTitle1.substring(1).trim();
        femaleRulerTitle2 = femaleRulerTitle2.charAt(0) + femaleRulerTitle2.substring(1).trim();
        femaleRulerTitle3 = femaleRulerTitle3.charAt(0) + femaleRulerTitle3.substring(1).trim();
        femaleRulerTitle4 = femaleRulerTitle4.charAt(0) + femaleRulerTitle4.substring(1).trim();
        maleRulerTitle1 = maleRulerTitle1.charAt(0) + maleRulerTitle1.substring(1).trim();
        maleRulerTitle2 = maleRulerTitle2.charAt(0) + maleRulerTitle2.substring(1).trim();
        maleRulerTitle3 = maleRulerTitle3.charAt(0) + maleRulerTitle3.substring(1).trim();
        maleRulerTitle4 = maleRulerTitle4.charAt(0) + maleRulerTitle4.substring(1).trim();
    }
    public int getDataLength()
    {
        return dataLength;
    }

    public int getDefaultType()
    {
        return defaultType;
    }

    public int getTransitionType()
    {
        return transitionType;
    }

    public int getRequiresMaintenance()
    {
        return requiresMaintenance;
    }

    public int getQuestionMarkOne()
    {
        return questionMarkOne;
    }

    public int getTilePenalty()
    {
        return tilePenalty;
    }

    public int getCommerceBonus()
    {
        return commerceBonus;
    }

    public String getName()
    {
        return name;
    }

    public String getCivilopediaEntry()
    {
        return civilopediaEntry;
    }

    public String getMaleRulerTitle1()
    {
        return maleRulerTitle1;
    }

    public String getFemaleRulerTitle1()
    {
        return femaleRulerTitle1;
    }

    public String getMaleRulerTitle2()
    {
        return maleRulerTitle2;
    }

    public String getFemaleRulerTitle2()
    {
        return femaleRulerTitle2;
    }

    public String getMaleRulerTitle3()
    {
        return maleRulerTitle3;
    }

    public String getFemaleRulerTitle3()
    {
        return femaleRulerTitle3;
    }

    public String getMaleRulerTitle4()
    {
        return maleRulerTitle4;
    }

    public String getFemaleRulerTitle4()
    {
        return femaleRulerTitle4;
    }

    public int getCorruption()
    {
        return corruption;
    }

    public int getImmuneTo()
    {
        return immuneTo;
    }

    public int getDiplomatLevel()
    {
        return diplomatLevel;
    }

    public int getSpyLevel()
    {
        return spyLevel;
    }

    public int getNumberOfGovernments()
    {
        return numberOfGovernments;
    }

    public int getHurrying()
    {
        return hurrying;
    }

    public int getAssimilationChance()
    {
        return assimilationChance;
    }

    public int getDraftLimit()
    {
        return draftLimit;
    }

    public int getMilitaryPoliceLimit()
    {
        return militaryPoliceLimit;
    }

    public int getRulerTitlePairsUsed()
    {
        return rulerTitlePairsUsed;
    }

    public int getPrerequisiteTechnology()
    {
        return prerequisiteInt;
    }

    public int getScienceCap()
    {
        return scienceCap;
    }

    public int getWorkerRate()
    {
        return workerRate;
    }

    public int getQuestionMarkTwo()
    {
        return questionMarkTwo;
    }

    public int getQuestionMarkThree()
    {
        return questionMarkThree;
    }

    public int getQuestionMarkFour()
    {
        return questionMarkFour;
    }

    public int getFreeUnits()
    {
        return freeUnits;
    }

    public int getFreeUnitsPerTown()
    {
        return freeUnitsPerTown;
    }

    public int getFreeUnitsPerCity()
    {
        return freeUnitsPerCity;
    }

    public int getFreeUnitsPerMetropolis()
    {
        return freeUnitsPerMetropolis;
    }

    public int getCostPerUnit()
    {
        return costPerUnit;
    }

    public int getWarWeariness()
    {
        return warWeariness;
    }

    public int getXenophobic()
    {
        return xenophobic;
    }

    public int getForceResettlement()
    {
        return forceResettlement;
    }

    public void setDefaultType(int defaultType)
    {
        this.defaultType = defaultType;
    }

    public void setTransitionType(int transitionType)
    {
        this.transitionType = transitionType;
    }

    public void setRequiresMaintenance(int requiresMaintenance)
    {
        this.requiresMaintenance = requiresMaintenance;
    }

    public void setQuestionMarkOne(int questionMarkOne)
    {
        this.questionMarkOne = questionMarkOne;
    }

    public void setTilePenalty(int tilePenalty)
    {
        this.tilePenalty = tilePenalty;
    }

    public void setCommerceBonus(int commerceBonus)
    {
        this.commerceBonus = commerceBonus;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setCivilopediaEntry(String civilopediaEntry)
    {
        this.civilopediaEntry = civilopediaEntry;
    }

    public void setMaleRulerTitle1(String maleRulerTitle1)
    {
        this.maleRulerTitle1 = maleRulerTitle1;
    }

    public void setFemaleRulerTitle1(String femaleRulerTitle1)
    {
        this.femaleRulerTitle1 = femaleRulerTitle1;
    }

    public void setMaleRulerTitle2(String maleRulerTitle2)
    {
        this.maleRulerTitle2 = maleRulerTitle2;
    }

    public void setFemaleRulerTitle2(String femaleRulerTitle2)
    {
        this.femaleRulerTitle2 = femaleRulerTitle2;
    }

    public void setMaleRulerTitle3(String maleRulerTitle3)
    {
        this.maleRulerTitle3 = maleRulerTitle3;
    }

    public void setFemaleRulerTitle3(String femaleRulerTitle3)
    {
        this.femaleRulerTitle3 = femaleRulerTitle3;
    }

    public void setMaleRulerTitle4(String maleRulerTitle4)
    {
        this.maleRulerTitle4 = maleRulerTitle4;
    }

    public void setFemaleRulerTitle4(String femaleRulerTitle4)
    {
        this.femaleRulerTitle4 = femaleRulerTitle4;
    }

    public void setCorruption(int corruption)
    {
        this.corruption = corruption;
    }

    public void setImmuneTo(int immuneTo)
    {
        this.immuneTo = immuneTo;
    }

    public void setDiplomatLevel(int diplomatLevel)
    {
        this.diplomatLevel = diplomatLevel;
    }

    public void setSpyLevel(int spyLevel)
    {
        this.spyLevel = spyLevel;
    }

    public void setNumberOfGovernments(int numberOfGovernments)
    {
        //For each new government, add 12 to the data length
        this.dataLength += ((numberOfGovernments - this.numberOfGovernments)*12);
        this.numberOfGovernments = numberOfGovernments;
    }

    public void setHurrying(int hurrying)
    {
        this.hurrying = hurrying;
    }

    public void setAssimilationChance(int assimilationChance)
    {
        this.assimilationChance = assimilationChance;
    }

    public void setDraftLimit(int draftLimit)
    {
        this.draftLimit = draftLimit;
    }

    public void setMilitaryPoliceLimit(int militaryPoliceLimit)
    {
        this.militaryPoliceLimit = militaryPoliceLimit;
    }

    public void setRulerTitlePairsUsed(int rulerTitlePairsUsed)
    {
        this.rulerTitlePairsUsed = rulerTitlePairsUsed;
    }

    public void setPrerequisiteTechnology(int prerequisiteTechnology)
    {
        this.prerequisiteInt = prerequisiteTechnology;
        if (baseLink.technology != null && prerequisiteInt != -1 && baseLink.technology.size() > prerequisiteInt) {
            this.prerequisiteTechnology = baseLink.technology.get(prerequisiteInt);
        }
    }

    public void setScienceCap(int scienceCap)
    {
        this.scienceCap = scienceCap;
    }

    public void setWorkerRate(int workerRate)
    {
        this.workerRate = workerRate;
    }

    public void setQuestionMarkTwo(int questionMarkTwo)
    {
        this.questionMarkTwo = questionMarkTwo;
    }

    public void setQuestionMarkThree(int questionMarkThree)
    {
        this.questionMarkThree = questionMarkThree;
    }

    public void setQuestionMarkFour(int questionMarkFour)
    {
        this.questionMarkFour = questionMarkFour;
    }

    public void setFreeUnits(int freeUnits)
    {
        this.freeUnits = freeUnits;
    }

    public void setFreeUnitsPerTown(int freeUnitsPerTown)
    {
        this.freeUnitsPerTown = freeUnitsPerTown;
    }

    public void setFreeUnitsPerCity(int freeUnitsPerCity)
    {
        this.freeUnitsPerCity = freeUnitsPerCity;
    }

    public void setFreeUnitsPerMetropolis(int freeUnitsPerMetropolis)
    {
        this.freeUnitsPerMetropolis = freeUnitsPerMetropolis;
    }

    public void setCostPerUnit(int costPerUnit)
    {
        this.costPerUnit = costPerUnit;
    }

    public void setWarWeariness(int warWeariness)
    {
        this.warWeariness = warWeariness;
    }

    public void setXenophobic(int xenophobic)
    {
        this.xenophobic = xenophobic;
    }

    public void setForceResettlement(int forceResettlement)
    {
        this.forceResettlement = forceResettlement;
    }
    
    public void handleDeletedTech(int index) {
        if (prerequisiteInt == index) {
            prerequisiteInt = -1;
            prerequisiteTechnology = null;
        }
        else if (prerequisiteInt > index) {
            prerequisiteInt--;
        }
    }
    
    public void handleSwappedTech() {
        if (prerequisiteInt != -1) {
            prerequisiteInt = prerequisiteTechnology.getIndex();
        }
    }

    public String toEnglish(){
        return toString();
    }

    @Override
    public String toString()
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn =  "name: " + name + lineReturn;
        toReturn = toReturn + "civilopediaEntry: " + civilopediaEntry + lineReturn;
        toReturn = toReturn + "dataLength: " + dataLength + lineReturn;
        toReturn = toReturn + "defaultType: " + defaultType + lineReturn;
        toReturn = toReturn + "transitionType: " + transitionType + lineReturn;
        toReturn = toReturn + "requiresMaintenance: " + requiresMaintenance + lineReturn;
        toReturn = toReturn + "questionMarkOne: " + questionMarkOne + lineReturn;
        toReturn = toReturn + "tilePenalty: " + tilePenalty + lineReturn;
        toReturn = toReturn + "commerceBonus: " + commerceBonus + lineReturn;
        toReturn = toReturn + "maleRulerTitle1: " + maleRulerTitle1 + lineReturn;
        toReturn = toReturn + "femaleRulerTitle1: " + femaleRulerTitle1 + lineReturn;
        toReturn = toReturn + "maleRulerTitle2: " + maleRulerTitle2 + lineReturn;
        toReturn = toReturn + "femaleRulerTitle2: " + femaleRulerTitle2 + lineReturn;
        toReturn = toReturn + "maleRulerTitle3: " + maleRulerTitle3 + lineReturn;
        toReturn = toReturn + "femaleRulerTitle3: " + femaleRulerTitle3 + lineReturn;
        toReturn = toReturn + "maleRulerTitle4: " + maleRulerTitle4 + lineReturn;
        toReturn = toReturn + "femaleRulerTitle4: " + femaleRulerTitle4 + lineReturn;
        toReturn = toReturn + "corruption: " + corruption + lineReturn;
        toReturn = toReturn + "immuneTo: " + immuneTo + lineReturn;
        toReturn = toReturn + "diplomatLevel: " + diplomatLevel + lineReturn;
        toReturn = toReturn + "spyLevel: " + spyLevel + lineReturn;
        toReturn = toReturn + "numberOfGovernments: " + numberOfGovernments + lineReturn;
        for (int j = 0; j < numberOfGovernments; j++)
        {
            toReturn = toReturn + "performance of this government versus government " + j + ": " + lineReturn + relations.get(j);
        }
        toReturn = toReturn + "hurrying: " + hurrying + lineReturn;
        toReturn = toReturn + "assimilationChance: " + assimilationChance + lineReturn;
        toReturn = toReturn + "draftLimit: " + draftLimit + lineReturn;
        toReturn = toReturn + "militaryPoliceLimit: " + militaryPoliceLimit + lineReturn;
        toReturn = toReturn + "rulerTitlePairsUsed: " + rulerTitlePairsUsed + lineReturn;
        toReturn = toReturn + "prerequisiteTechnology: " + prerequisiteInt + lineReturn;
        toReturn = toReturn + "scienceCap: " + scienceCap + lineReturn;
        toReturn = toReturn + "workerRate: " + workerRate + lineReturn;
        toReturn = toReturn + "questionMarkTwo: " + questionMarkTwo + lineReturn;
        toReturn = toReturn + "questionMarkThree: " + questionMarkThree + lineReturn;
        toReturn = toReturn + "questionMarkFour: " + questionMarkFour + lineReturn;
        toReturn = toReturn + "freeUnits: " + freeUnits + lineReturn;
        toReturn = toReturn + "freeUnitsPerTown: " + freeUnitsPerTown + lineReturn;
        toReturn = toReturn + "freeUnitsPerCity: " + freeUnitsPerCity + lineReturn;
        toReturn = toReturn + "freeUnitsPerMetropolis: " + freeUnitsPerMetropolis + lineReturn;
        toReturn = toReturn + "costPerUnit: " + costPerUnit + lineReturn;
        toReturn = toReturn + "warWeariness: " + warWeariness + lineReturn;
        toReturn = toReturn + "xenophobic: " + xenophobic + lineReturn;
        toReturn = toReturn + "forceResettlement: " + forceResettlement + lineReturn;
        toReturn = toReturn + lineReturn;
        return toReturn;

    }

    public String compareTo(BIQSection section, String separator)
    {
        if (!(section instanceof GOVT))
            return null;
        GOVT two = (GOVT)section;
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "name: " + name + lineReturn;
        if (!(dataLength == two.getDataLength()))
        {
                toReturn = toReturn + "DataLength: " + dataLength + separator + two.getDataLength() + lineReturn;
        }
        if (!(defaultType == two.getDefaultType()))
        {
                toReturn = toReturn + "DefaultType: " + defaultType + separator + two.getDefaultType() + lineReturn;
        }
        if (!(transitionType == two.getTransitionType()))
        {
                toReturn = toReturn + "TransitionType: " + transitionType + separator + two.getTransitionType() + lineReturn;
        }
        if (!(requiresMaintenance == two.getRequiresMaintenance()))
        {
                toReturn = toReturn + "RequiresMaintenance: " + requiresMaintenance + separator + two.getRequiresMaintenance() + lineReturn;
        }
        if (!(questionMarkOne == two.getQuestionMarkOne()))
        {
                toReturn = toReturn + "QuestionMarkOne: " + questionMarkOne + separator + two.getQuestionMarkOne() + lineReturn;
        }
        if (!(tilePenalty == two.getTilePenalty()))
        {
                toReturn = toReturn + "TilePenalty: " + tilePenalty + separator + two.getTilePenalty() + lineReturn;
        }
        if (!(commerceBonus == two.getCommerceBonus()))
        {
                toReturn = toReturn + "CommerceBonus: " + commerceBonus + separator + two.getCommerceBonus() + lineReturn;
        }
        if (civilopediaEntry.compareTo(two.getCivilopediaEntry()) != 0)
        {
                toReturn = toReturn + "CivilopediaEntry: " + civilopediaEntry + separator + two.getCivilopediaEntry() + lineReturn;
        }
        if (maleRulerTitle1.compareTo(two.getMaleRulerTitle1()) != 0)
        {
                toReturn = toReturn + "MaleRulerTitle1: " + maleRulerTitle1 + separator + two.getMaleRulerTitle1() + lineReturn;
        }
        if (femaleRulerTitle1.compareTo(two.getFemaleRulerTitle1()) != 0)
        {
                toReturn = toReturn + "FemaleRulerTitle1: " + femaleRulerTitle1 + separator + two.getFemaleRulerTitle1() + lineReturn;
        }
        if (maleRulerTitle2.compareTo(two.getMaleRulerTitle2()) != 0)
        {
                toReturn = toReturn + "MaleRulerTitle2: " + maleRulerTitle2 + separator + two.getMaleRulerTitle2() + lineReturn;
        }
        if (femaleRulerTitle2.compareTo(two.getFemaleRulerTitle2()) != 0)
        {
                toReturn = toReturn + "FemaleRulerTitle2: " + femaleRulerTitle2 + separator + two.getFemaleRulerTitle2() + lineReturn;
        }
        if (maleRulerTitle3.compareTo(two.getMaleRulerTitle3()) != 0)
        {
                toReturn = toReturn + "MaleRulerTitle3: " + maleRulerTitle3 + separator + two.getMaleRulerTitle3() + lineReturn;
        }
        if (femaleRulerTitle3.compareTo(two.getFemaleRulerTitle3()) != 0)
        {
                toReturn = toReturn + "FemaleRulerTitle3: " + femaleRulerTitle3 + separator + two.getFemaleRulerTitle3() + lineReturn;
        }
        if (maleRulerTitle4.compareTo(two.getMaleRulerTitle4()) != 0)
        {
                toReturn = toReturn + "MaleRulerTitle4: " + maleRulerTitle4 + separator + two.getMaleRulerTitle4() + lineReturn;
        }
        if (femaleRulerTitle4.compareTo(two.getFemaleRulerTitle4()) != 0)
        {
                toReturn = toReturn + "FemaleRulerTitle4: " + femaleRulerTitle4 + separator + two.getFemaleRulerTitle4() + lineReturn;
        }
        if (!(corruption == two.getCorruption()))
        {
                toReturn = toReturn + "Corruption: " + corruption + separator + two.getCorruption() + lineReturn;
        }
        if (!(immuneTo == two.getImmuneTo()))
        {
                toReturn = toReturn + "ImmuneTo: " + immuneTo + separator + two.getImmuneTo() + lineReturn;
        }
        if (!(diplomatLevel == two.getDiplomatLevel()))
        {
                toReturn = toReturn + "DiplomatLevel: " + diplomatLevel + separator + two.getDiplomatLevel() + lineReturn;
        }
        if (!(spyLevel == two.getSpyLevel()))
        {
                toReturn = toReturn + "SpyLevel: " + spyLevel + separator + two.getSpyLevel() + lineReturn;
        }
        if (!(numberOfGovernments == two.getNumberOfGovernments()))
        {
                toReturn = toReturn + "NumberOfGovernments: " + numberOfGovernments + separator + two.getNumberOfGovernments() + lineReturn;
        }
        //ArrayListComapre
        //
        if (!(hurrying == two.getHurrying()))
        {
                toReturn = toReturn + "Hurrying: " + hurrying + separator + two.getHurrying() + lineReturn;
        }
        if (!(assimilationChance == two.getAssimilationChance()))
        {
                toReturn = toReturn + "AssimilationChance: " + assimilationChance + separator + two.getAssimilationChance() + lineReturn;
        }
        if (!(draftLimit == two.getDraftLimit()))
        {
                toReturn = toReturn + "DraftLimit: " + draftLimit + separator + two.getDraftLimit() + lineReturn;
        }
        if (!(militaryPoliceLimit == two.getMilitaryPoliceLimit()))
        {
                toReturn = toReturn + "MilitaryPoliceLimit: " + militaryPoliceLimit + separator + two.getMilitaryPoliceLimit() + lineReturn;
        }
        if (!(rulerTitlePairsUsed == two.getRulerTitlePairsUsed()))
        {
                toReturn = toReturn + "RulerTitlePairsUsed: " + rulerTitlePairsUsed + separator + two.getRulerTitlePairsUsed() + lineReturn;
        }
        if (!(prerequisiteInt == two.getPrerequisiteTechnology()))
        {
                toReturn = toReturn + "PrerequisiteTechnology: " + prerequisiteInt + separator + two.getPrerequisiteTechnology() + lineReturn;
        }
        if (!(scienceCap == two.getScienceCap()))
        {
                toReturn = toReturn + "ScienceCap: " + scienceCap + separator + two.getScienceCap() + lineReturn;
        }
        if (!(workerRate == two.getWorkerRate()))
        {
                toReturn = toReturn + "WorkerRate: " + workerRate + separator + two.getWorkerRate() + lineReturn;
        }
        if (!(questionMarkTwo == two.getQuestionMarkTwo()))
        {
                toReturn = toReturn + "QuestionMarkTwo: " + questionMarkTwo + separator + two.getQuestionMarkTwo() + lineReturn;
        }
        if (!(questionMarkThree == two.getQuestionMarkThree()))
        {
                toReturn = toReturn + "QuestionMarkThree: " + questionMarkThree + separator + two.getQuestionMarkThree() + lineReturn;
        }
        if (!(questionMarkFour == two.getQuestionMarkFour()))
        {
                toReturn = toReturn + "QuestionMarkFour: " + questionMarkFour + separator + two.getQuestionMarkFour() + lineReturn;
        }
        if (!(freeUnits == two.getFreeUnits()))
        {
                toReturn = toReturn + "FreeUnits: " + freeUnits + separator + two.getFreeUnits() + lineReturn;
        }
        if (!(freeUnitsPerTown == two.getFreeUnitsPerTown()))
        {
                toReturn = toReturn + "FreeUnitsPerTown: " + freeUnitsPerTown + separator + two.getFreeUnitsPerTown() + lineReturn;
        }
        if (!(freeUnitsPerCity == two.getFreeUnitsPerCity()))
        {
                toReturn = toReturn + "FreeUnitsPerCity: " + freeUnitsPerCity + separator + two.getFreeUnitsPerCity() + lineReturn;
        }
        if (!(freeUnitsPerMetropolis == two.getFreeUnitsPerMetropolis()))
        {
                toReturn = toReturn + "FreeUnitsPerMetropolis: " + freeUnitsPerMetropolis + separator + two.getFreeUnitsPerMetropolis() + lineReturn;
        }
        if (!(costPerUnit == two.getCostPerUnit()))
        {
                toReturn = toReturn + "CostPerUnit: " + costPerUnit + separator + two.getCostPerUnit() + lineReturn;
        }
        if (!(warWeariness == two.getWarWeariness()))
        {
                toReturn = toReturn + "WarWeariness: " + warWeariness + separator + two.getWarWeariness() + lineReturn;
        }
        if (!(xenophobic == two.getXenophobic()))
        {
                toReturn = toReturn + "Xenophobic: " + xenophobic + separator + two.getXenophobic() + lineReturn;
        }
        if (!(forceResettlement == two.getForceResettlement()))
        {
                toReturn = toReturn + "ForceResettlement: " + forceResettlement + separator + two.getForceResettlement() + lineReturn;
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
