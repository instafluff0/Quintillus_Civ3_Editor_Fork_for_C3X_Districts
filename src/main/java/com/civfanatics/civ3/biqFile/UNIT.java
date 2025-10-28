package com.civfanatics.civ3.biqFile;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Quintillus
 */
public class UNIT extends BIQSection{
    private int index = 0;
    private int dataLength = 121;
    private String name = "";
    private int ownerType;
    private int experienceLevel;
    private int owner;
    private int PRTONumber;
    private PRTO prto;
    private int AIStrategy;
    private int x;
    private int y;
    private String PTWCustomName = "";
    private int useCivilizationKing;
    public UNIT(IO baselink)
    {
        super(baselink);
    }
    public void trim()
    {
            name = name.trim();
            PTWCustomName = PTWCustomName.trim();
    }

    public int getDataLength()
    {
        return dataLength;
    }

    public String getName()
    {
        return name;
    }

    public int getOwnerType()
    {
        return ownerType;
    }

    public int getExperienceLevel()
    {
        return experienceLevel;
    }

    public int getOwner()
    {
        return owner;
    }

    public int getPRTONumber()
    {
        return PRTONumber;
    }

    public int getAIStrategy()
    {
        return AIStrategy;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public String getPTWCustomName()
    {
        return PTWCustomName;
    }

    public int getUseCivilizationKing()
    {
        return useCivilizationKing;
    }
    
    public void setDataLength(int dataLength)
    {
        this.dataLength = dataLength;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setOwnerType(int ownerType)
    {
        this.ownerType = ownerType;
    }

    public void setExperienceLevel(int experienceLevel)
    {
        this.experienceLevel = experienceLevel;
    }

    public void setOwner(int owner)
    {
        this.owner = owner;
    }

    public void setPRTONumber(int PRTONumber)
    {
        this.PRTONumber = PRTONumber;
        if (baseLink != null && baseLink.unit != null && PRTONumber != -1 && baseLink.unit.size() > PRTONumber) {
            this.prto = baseLink.unit.get(PRTONumber);
        }
    }

    public void setAIStrategy(int AIStrategy)
    {
        this.AIStrategy = AIStrategy;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setPTWCustomName(String PTWCustomName)
    {
        this.PTWCustomName = PTWCustomName;
    }

    public void setUseCivilizationKing(int useCivilizationKing)
    {
        this.useCivilizationKing = useCivilizationKing;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
    public void handleSwappedUnit() {
        if (prto != null) {
            PRTONumber = prto.getIndex();
        }
    }
    
    public void handleDeletedUnit(int index) {
        if (PRTONumber  == index) {
            PRTONumber = -1;
            prto = null;
        }
    }

    public String toEnglish(){
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn =  "unit index: " + index + lineReturn;
        toReturn = toReturn + "dataLength: " + dataLength + lineReturn;
        toReturn = toReturn + "name: " + name + lineReturn;
        toReturn = toReturn + "ownerType: " + CITY.ownerTypes[ownerType] + lineReturn;
        toReturn = toReturn + "experienceLevel: " + baseLink.experience.get(experienceLevel).getName() + lineReturn;
        if (ownerType == CITY.OWNER_CIV) {
            toReturn = toReturn + "owner: " + baseLink.civilization.get(owner).getName() + lineReturn;
        }
        else {
            toReturn = toReturn + "owner: " + owner + lineReturn;            
        }
        toReturn = toReturn + "Unit Type: " + baseLink.unit.get(PRTONumber).getName() + lineReturn;
        if (AIStrategy == -1) {
            toReturn = toReturn + "AIStrategy: " + "Random" + lineReturn;
        }
        else {
            toReturn = toReturn + "AIStrategy: " + PRTO.strategyNames[AIStrategy] + lineReturn;
        }
        toReturn = toReturn + "x: " + x + lineReturn;
        toReturn = toReturn + "y: " + y + lineReturn;
        toReturn = toReturn + "PTWCustomName: " + PTWCustomName + lineReturn;
        toReturn = toReturn + "useCivilizationKing: " + useCivilizationKing + lineReturn;
        toReturn = toReturn + lineReturn;
        return toReturn;
    }

    @Override
    public String toString()
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn =  "dataLength: " + dataLength + lineReturn;
        toReturn = toReturn + "name: " + name + lineReturn;
        toReturn = toReturn + "ownerType: " + ownerType + lineReturn;
        toReturn = toReturn + "experienceLevel: " + experienceLevel + lineReturn;
        toReturn = toReturn + "owner: " + owner + lineReturn;
        toReturn = toReturn + "PRTONumber: " + PRTONumber + lineReturn;
        toReturn = toReturn + "AIStrategy: " + AIStrategy + lineReturn;
        toReturn = toReturn + "x: " + x + lineReturn;
        toReturn = toReturn + "y: " + y + lineReturn;
        toReturn = toReturn + "PTWCustomName: " + PTWCustomName + lineReturn;
        toReturn = toReturn + "useCivilizationKing: " + useCivilizationKing + lineReturn;
        toReturn = toReturn + lineReturn;
        return toReturn;
    }

    public String compareTo(BIQSection section, String separator)
    {
        if (!(section instanceof UNIT))
            return null;
        UNIT two = (UNIT)section;
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "name: " + name + lineReturn;
        if (!(dataLength == two.getDataLength()))
        {
                toReturn = toReturn + "DataLength: " + dataLength + separator + two.getDataLength() + lineReturn;
        }
        if (name.compareTo(two.getName()) != 0)
        {
                toReturn = toReturn + "Name: " + name + separator + two.getName() + lineReturn;
        }
        if (!(ownerType == two.getOwnerType()))
        {
                toReturn = toReturn + "OwnerType: " + ownerType + separator + two.getOwnerType() + lineReturn;
        }
        if (!(experienceLevel == two.getExperienceLevel()))
        {
                toReturn = toReturn + "ExperienceLevel: " + experienceLevel + separator + two.getExperienceLevel() + lineReturn;
        }
        if (!(owner == two.getOwner()))
        {
                toReturn = toReturn + "Owner: " + owner + separator + two.getOwner() + lineReturn;
        }
        if (!(PRTONumber == two.getPRTONumber()))
        {
                toReturn = toReturn + "PRTONumber: " + PRTONumber + separator + two.getPRTONumber() + lineReturn;
        }
        if (!(AIStrategy == two.getAIStrategy()))
        {
                toReturn = toReturn + "AIStrategy: " + AIStrategy + separator + two.getAIStrategy() + lineReturn;
        }
        if (!(x == two.getX()))
        {
                toReturn = toReturn + "X: " + x + separator + two.getX() + lineReturn;
        }
        if (!(y == two.getY()))
        {
                toReturn = toReturn + "Y: " + y + separator + two.getY() + lineReturn;
        }
        if (PTWCustomName.compareTo(two.getPTWCustomName()) != 0)
        {
                toReturn = toReturn + "PTWCustomName: " + PTWCustomName + separator + two.getPTWCustomName() + lineReturn;
        }
        if (!(useCivilizationKing == two.getUseCivilizationKing()))
        {
                toReturn = toReturn + "UseCivilizationKing: " + useCivilizationKing + separator + two.getUseCivilizationKing() + lineReturn;
        }
        return toReturn;
    }

    public Object getProperty(String string) throws UnsupportedOperationException
    {
        if (string.equals("Name"))
            return this.name;
        throw new UnsupportedOperationException();
    }

    /**
     * Clones the unit, as a deep copy.
     * @return 
     */
    @Override
    public UNIT clone()
    {
        UNIT other = new UNIT(this.baseLink);
        other.setAIStrategy(AIStrategy);
        other.setPRTONumber(PRTONumber);
        other.setPTWCustomName(new String(PTWCustomName));
        other.setDataLength(dataLength);
        other.setExperienceLevel(experienceLevel);
        other.setName(new String(name));
        other.setOwner(owner);
        other.setOwnerType(ownerType);
        other.setUseCivilizationKing(useCivilizationKing);
        other.setX(x);
        other.setY(y);
        return other;
    }
}
