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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
public class LEAD extends BIQSection{
    
    Logger logger = Logger.getLogger(this.getClass());
    
    public static final int CIV_ANY = -3;
    public static final int CIV_RANDOM = -2;
    public int customCivData;
    public int humanPlayer;
    public String leaderName = "";
    public int questionMark1;
    public int questionMark2;
    public int numberOfDifferentStartUnits;
    
    //Let's try something different.  Rather than a bunch of pointers which will be
    //a mess to keep straight when reordering, let's instead use a map.  On save we'll have
    //to reconstruct the ints, but that should be easier than always keeping them up to date.
    private Map<PRTO, Integer> startUnitsByType = new HashMap<PRTO, Integer>();
    //Need a temp solution for BIQs without custom rules
    private Map<Integer, Integer> startUnitsByTypeInt = new HashMap<Integer, Integer>();
    
    public int genderOfLeaderName;
    public int numberOfStartingTechnologies;
    ArrayList<Integer>startingTechnologyInt;    //intentionally package-level; IO.java can update it but the GUI can't directly
    private ArrayList<TECH>startingTechnology;
    public int difficulty;
    public int initialEra;
    public int startCash;
    public int government;
    public int civ;
    public int color;
    public int skipFirstTurn;
    public int questionMark3;
    byte startEmbassies;

    public LEAD(IO baselink)
    {
        super(baselink);
        startingTechnologyInt = new ArrayList<Integer>();
        startingTechnology = new ArrayList<TECH>();
        government = 1; //despotism
        difficulty = -1; //any
        civ = -3;
        startCash = 10;
    }
    
    /**
     * Adds a couple start units as default ones, similar to the Firaxis editor.
     * This should only be called when a player is added in the editor, not
     * when imported from a BIQ.
     */
    public void addDefaultStartUnits() {
        startUnitsByType.put(baseLink.unit.get(0), 1);
        startUnitsByType.put(baseLink.unit.get(1), 1);
    }
    
    public void trim()
    {
        leaderName = leaderName.trim();
    }
    
    /**
     * Base with no starting units/techs is 0x5D.  Each of those take 8/4 respectively.
     * @return 
     */
    public int getDataLength()
    {
        return 0x5D + startUnitsByType.size() * 8 + startingTechnology.size() * 4;
    }

    public int getCustomCivData()
    {
        return customCivData;
    }

    public int getHumanPlayer()
    {
        return humanPlayer;
    }

    public String getLeaderName()
    {
        return leaderName;
    }

    public int getQuestionMark1()
    {
        return questionMark1;
    }

    public int getQuestionMark2()
    {
        return questionMark2;
    }

    public int getGenderOfLeaderName()
    {
        return genderOfLeaderName;
    }

    public int getNumberOfStartingTechnologies()
    {
        return this.startingTechnologyInt.size();
    }
    
    /**
     * Sets the pointer-based starting techs, from the int-based version.
     * Should be run after file import.
     */
    public void setStartingTechLinks() {
        startingTechnology.clear();
        for (int i = 0; i < startingTechnologyInt.size(); i++) {
            this.startingTechnology.add(baseLink.technology.get(startingTechnologyInt.get(i)));
        }
    }
    
    public void updateStartingTechIndices() {
        this.startingTechnologyInt.clear();
        for (TECH tech : startingTechnology) {
            startingTechnologyInt.add(tech.getIndex());
        }
    }
    
    /**
     * Updates the selected techs.  Also updates TECH links.
     * @param selectedTechs The IDs of the newly-selected techs.
     */
    public void changeStartingTechnologies(int[] selectedTechs) {
        startingTechnologyInt.clear();
        for (int i = 0; i < selectedTechs.length; i++)
            startingTechnologyInt.add(selectedTechs[i]);
        setStartingTechLinks();
    }
    
    public int[] getStartingTechs() {
        int[] startingTechs = new int[startingTechnologyInt.size()];
        for (int i = 0; i < startingTechnologyInt.size(); i++) {
            startingTechs[i] = startingTechnologyInt.get(i);
        }
        return startingTechs;
    }

    public int getDifficulty()
    {
        return difficulty;
    }

    public int getInitialEra()
    {
        return initialEra;
    }

    public int getStartCash()
    {
        return startCash;
    }

    public int getGovernment()
    {
        return government;
    }

    public int getCiv()
    {
        return civ;
    }

    public int getColor()
    {
        return color;
    }

    public int getSkipFirstTurn()
    {
        return skipFirstTurn;
    }

    public int getQuestionMark3()
    {
        return questionMark3;
    }

    public byte getStartEmbassies()
    {
        return startEmbassies;
    }

    public void addStartingUnitType(PRTO type, Integer number) {
        this.startUnitsByType.put(type, number);
    }

    void addStartingUnitType(Integer type, Integer number) {
        this.startUnitsByTypeInt.put(type, number);
    }
    
    public Map<PRTO, Integer> getStartingUnits() {
        return Collections.unmodifiableMap(startUnitsByType);
    }

    public Integer getNumOfStartingUnitByType(PRTO type) {
        if (!startUnitsByType.containsKey(type)) {
            return 0;
        }
        else {
            return startUnitsByType.get(type);
        }
    }
    
    public void updateStartUnitCount(PRTO type, Integer number) {
        if (logger.isDebugEnabled()) {
            logger.debug("Updating unit count for " + type.getName() + " to " + number);
        }
        if (number == 0) {
            startUnitsByType.remove(type);
        }
        else {
            startUnitsByType.put(type, number);
        }
    }

    /**
     * Special: This method must be called *after* the tech indices have been updated.
     * Why?  Because it grabs the new tech indices to update its list, and if they
     * aren't updated yet, that won't be correct.
     * @param index 
     */
    public void handleDeletedTech(int index) {
        for (int i = 0; i < startingTechnology.size(); i++) {
            if (startingTechnology.get(i).getIndex() == -1) {
                startingTechnology.remove(i);
                break;  //it can only be in the list once
            }
        }
        this.updateStartingTechIndices();
    }
    
    public void handleDeletedUnit(PRTO prto) {
        startUnitsByType.remove(prto);
    }

    public void setCustomCivData(int customCivData)
    {
        this.customCivData = customCivData;
    }

    public void setHumanPlayer(int humanPlayer)
    {
        this.humanPlayer = humanPlayer;
    }

    public void setLeaderName(String leaderName)
    {
        this.leaderName = leaderName;
    }

    public void setQuestionMark1(int questionMark1)
    {
        this.questionMark1 = questionMark1;
    }

    public void setQuestionMark2(int questionMark2)
    {
        this.questionMark2 = questionMark2;
    }

    public void setGenderOfLeaderName(int genderOfLeaderName)
    {
        this.genderOfLeaderName = genderOfLeaderName;
    }

    public void setDifficulty(int difficulty)
    {
        this.difficulty = difficulty;
    }

    public void setInitialEra(int initialEra)
    {
        this.initialEra = initialEra;
    }

    public void setStartCash(int startCash)
    {
        this.startCash = startCash;
    }

    public void setGovernment(int government)
    {
        this.government = government;
    }

    public void setCiv(int civ)
    {
        this.civ = civ;
    }

    public void setColor(int color)
    {
        this.color = color;
    }

    public void setSkipFirstTurn(int skipFirstTurn)
    {
        this.skipFirstTurn = skipFirstTurn;
    }

    public void setQuestionMark3(int questionMark3)
    {
        this.questionMark3 = questionMark3;
    }

    public void setStartEmbassies(byte startEmbassies)
    {
        this.startEmbassies = startEmbassies;
    }

    public String toEnglish(){
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "dataLength: " + getDataLength() + lineReturn;
        toReturn = toReturn + "customCivData: " + trueOrFalse(customCivData) + lineReturn;
        toReturn = toReturn + "humanPlayer: " + trueOrFalse(humanPlayer) + lineReturn;
        toReturn = toReturn + "leaderName: " + leaderName + lineReturn;
        toReturn = toReturn + "questionMark1: " + questionMark1 + lineReturn;
        toReturn = toReturn + "questionMark2: " + questionMark2 + lineReturn;
        toReturn = toReturn + "numberOfDifferentStartUnits: " + numberOfDifferentStartUnits + lineReturn;
        for (Map.Entry<PRTO, Integer> entry : startUnitsByType.entrySet()) {
            toReturn = toReturn + "starting units of type " + entry.getKey().getName() + ": " + entry.getValue() + lineReturn;
        }
        if (genderOfLeaderName == 0) {
            toReturn = toReturn + "genderOfLeaderName: " + "Male" + lineReturn;
        }
        else if (genderOfLeaderName == 1) {
            toReturn = toReturn + "genderOfLeaderName: " + "Female" + lineReturn;
        }
        toReturn = toReturn + "numberOfStartingTechnologies: " + numberOfStartingTechnologies + lineReturn;
        for (int j = 0; j < numberOfStartingTechnologies; j++)
        {
            toReturn = toReturn + "startingTechnology: " + super.baseLink.technology.get(startingTechnologyInt.get(j)).getName() + lineReturn;
        }
        if (difficulty == -2) {
            toReturn = toReturn + "difficulty: " + "Any" + lineReturn;
        }
        else {
            toReturn = toReturn + "difficulty: " + super.baseLink.difficulties.get(difficulty).getName() + lineReturn;
        }
        if (initialEra < 4) {
            toReturn = toReturn + "initialEra: " + super.baseLink.eras.get(initialEra).getName() + lineReturn;
        }
        else {
            toReturn = toReturn + "initialEra: " + "Future Erea" + lineReturn;
        }
        toReturn = toReturn + "startCash: " + startCash + lineReturn;
        toReturn = toReturn + "government: " + super.baseLink.government.get(government).getName() + lineReturn;
        if (civ > -1) {
            toReturn = toReturn + "civ: " + super.baseLink.civilization.get(civ).getName() + lineReturn;
        }
        else if (civ == -3) {
            toReturn = toReturn + "civ: " + "Any" + lineReturn;
        }
        else if (civ == -2) {
            toReturn = toReturn + "civ: " + "Random" + lineReturn;
        }
        toReturn = toReturn + "color: " + color + lineReturn;
        toReturn = toReturn + "skipFirstTurn: " + trueOrFalse(skipFirstTurn) + lineReturn;
        toReturn = toReturn + "questionMark3: " + questionMark3 + lineReturn;
        toReturn = toReturn + "startEmbassies: " + trueOrFalse(startEmbassies) + lineReturn;
        toReturn = toReturn + lineReturn;
        return toReturn;
    }
    
    @Override
    public String toString()
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "dataLength: " + getDataLength() + lineReturn;
        toReturn = toReturn + "customCivData: " + customCivData + lineReturn;
        toReturn = toReturn + "humanPlayer: " + humanPlayer + lineReturn;
        toReturn = toReturn + "leaderName: " + leaderName + lineReturn;
        toReturn = toReturn + "questionMark1: " + questionMark1 + lineReturn;
        toReturn = toReturn + "questionMark2: " + questionMark2 + lineReturn;
        toReturn = toReturn + "numberOfDifferentStartUnits: " + numberOfDifferentStartUnits + lineReturn;
        for (Map.Entry<PRTO, Integer> entry : startUnitsByType.entrySet()) {
            toReturn = toReturn + "starting units of type " + entry.getKey().getIndex() + ": " + entry.getValue() + lineReturn;
        }
        toReturn = toReturn + "genderOfLeaderName: " + genderOfLeaderName + lineReturn;
        toReturn = toReturn + "numberOfStartingTechnologies: " + numberOfStartingTechnologies + lineReturn;
        for (int j = 0; j < numberOfStartingTechnologies; j++)
        {
            toReturn = toReturn + "startingTechnology: " + startingTechnologyInt.get(j) + lineReturn;
        }
        toReturn = toReturn + "difficulty: " + difficulty + lineReturn;
        toReturn = toReturn + "initialEra: " + initialEra + lineReturn;
        toReturn = toReturn + "startCash: " + startCash + lineReturn;
        toReturn = toReturn + "government: " + government + lineReturn;
        toReturn = toReturn + "civ: " + civ + lineReturn;
        toReturn = toReturn + "color: " + color + lineReturn;
        toReturn = toReturn + "skipFirstTurn: " + skipFirstTurn + lineReturn;
        toReturn = toReturn + "questionMark3: " + questionMark3 + lineReturn;
        toReturn = toReturn + "startEmbassies: " + startEmbassies + lineReturn;
        toReturn = toReturn + lineReturn;
        return toReturn;
    }

    public String compareTo(BIQSection section, String separator)
    {
        if (!(section instanceof LEAD))
            return null;
        LEAD two = (LEAD)section;
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "civ: " + civ + lineReturn;
        if (!(getDataLength() == two.getDataLength()))
        {
                toReturn = toReturn + "DataLength: " + getDataLength() + separator + two.getDataLength() + lineReturn;
        }
        if (!(customCivData == two.getCustomCivData()))
        {
                toReturn = toReturn + "CustomCivData: " + customCivData + separator + two.getCustomCivData() + lineReturn;
        }
        if (!(humanPlayer == two.getHumanPlayer()))
        {
                toReturn = toReturn + "HumanPlayer: " + humanPlayer + separator + two.getHumanPlayer() + lineReturn;
        }
        if (leaderName.compareTo(two.getLeaderName()) != 0)
        {
                toReturn = toReturn + "LeaderName: " + leaderName + separator + two.getLeaderName() + lineReturn;
        }
        if (!(questionMark1 == two.getQuestionMark1()))
        {
                toReturn = toReturn + "QuestionMark1: " + questionMark1 + separator + two.getQuestionMark1() + lineReturn;
        }
        if (!(questionMark2 == two.getQuestionMark2()))
        {
                toReturn = toReturn + "QuestionMark2: " + questionMark2 + separator + two.getQuestionMark2() + lineReturn;
        }
        if (!(startUnitsByType.size() == two.startUnitsByType.size()))
        {
                toReturn = toReturn + "NumberOfDifferentStartUnits: " + numberOfDifferentStartUnits + separator + two.startUnitsByType.size() + lineReturn;
        }
        if (!(genderOfLeaderName == two.getGenderOfLeaderName()))
        {
                toReturn = toReturn + "GenderOfLeaderName: " + genderOfLeaderName + separator + two.getGenderOfLeaderName() + lineReturn;
        }
        if (!(numberOfStartingTechnologies == two.getNumberOfStartingTechnologies()))
        {
                toReturn = toReturn + "NumberOfStartingTechnologies: " + numberOfStartingTechnologies + separator + two.getNumberOfStartingTechnologies() + lineReturn;
        }
        if (!(difficulty == two.getDifficulty()))
        {
                toReturn = toReturn + "Difficulty: " + difficulty + separator + two.getDifficulty() + lineReturn;
        }
        if (!(initialEra == two.getInitialEra()))
        {
                toReturn = toReturn + "InitialEra: " + initialEra + separator + two.getInitialEra() + lineReturn;
        }
        if (!(startCash == two.getStartCash()))
        {
                toReturn = toReturn + "StartCash: " + startCash + separator + two.getStartCash() + lineReturn;
        }
        if (!(government == two.getGovernment()))
        {
                toReturn = toReturn + "Government: " + government + separator + two.getGovernment() + lineReturn;
        }
        if (!(color == two.getColor()))
        {
                toReturn = toReturn + "Color: " + color + separator + two.getColor() + lineReturn;
        }
        if (!(skipFirstTurn == two.getSkipFirstTurn()))
        {
                toReturn = toReturn + "SkipFirstTurn: " + skipFirstTurn + separator + two.getSkipFirstTurn() + lineReturn;
        }
        if (!(questionMark3 == two.getQuestionMark3()))
        {
                toReturn = toReturn + "QuestionMark3: " + questionMark3 + separator + two.getQuestionMark3() + lineReturn;
        }
        if (!(startEmbassies == two.getStartEmbassies()))
        {
                toReturn = toReturn + "StartEmbassies: " + startEmbassies + separator + two.getStartEmbassies() + lineReturn;
        }
        if (toReturn.equals("civ: " + civ + lineReturn))
        {
            toReturn = "";
        }
        return toReturn;
    }
    public Object getProperty(String string) throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }
}
