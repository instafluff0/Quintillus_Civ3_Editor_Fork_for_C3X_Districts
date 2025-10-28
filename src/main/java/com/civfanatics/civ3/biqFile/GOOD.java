package com.civfanatics.civ3.biqFile;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Representing resources.
 * Other sections that know about GOODs: BLDG, PRTO, RULE, TERR, TRFM, MAP
 * @author Quintillus
 */
public class GOOD extends BIQSection{
    private final static int BONUS = 0;
    private final static int LUXURY = 1;
    private final static int STRATEGIC = 2;
    
    private int dataLength = 88;
    private String name = "";
    private String civilopediaEntry = "";
    private int type;    //2 = strategic, 1 = luxury, 0 = bonus
    private int appearanceRatio;
    private int disapperanceProbability;
    private int icon;
    private int prerequisiteInt;
    private TECH prerequisite;
    private int foodBonus;
    private int shieldsBonus;
    private int commerceBonus;
    
    public GOOD clone() {
        GOOD other = new GOOD(this.baseLink);
        other.dataLength = dataLength;
        other.name = name;
        other.civilopediaEntry = civilopediaEntry;
        other.type = type;
        other.appearanceRatio = appearanceRatio;
        other.disapperanceProbability = disapperanceProbability;
        other.icon = icon;
        other.prerequisiteInt = prerequisiteInt;
        other.prerequisite = prerequisite;
        other.foodBonus = foodBonus;
        other.shieldsBonus = shieldsBonus;
        other.commerceBonus = commerceBonus;
        return other;
    }
    
    public GOOD(IO baselink)
    {
        super(baselink);
    }

    public GOOD(String name, IO baselink)
    {
        super(baselink);
        this.name = name;
        prerequisiteInt = -1;
    }
    public boolean isBonus() {
        return type == BONUS;
    }
    public boolean isLuxury() {
        return type == LUXURY;
    }
    public boolean isStrategic() {
        return type == STRATEGIC;
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

    public String getName()
    {
        return name;
    }

    public String getCivilopediaEntry()
    {
        return civilopediaEntry;
    }

    public int getType()
    {
        return type;
    }

    public int getAppearanceRatio()
    {
        return appearanceRatio;
    }

    public int getDisapperanceProbability()
    {
        return disapperanceProbability;
    }

    public int getIcon()
    {
        return icon;
    }

    public int getPrerequisite()
    {
        return prerequisiteInt;
    }

    public int getFoodBonus()
    {
        return foodBonus;
    }

    public int getShieldsBonus()
    {
        return shieldsBonus;
    }

    public int getCommerceBonus()
    {
        return commerceBonus;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setCivilopediaEntry(String civilopediaEntry)
    {
        this.civilopediaEntry = civilopediaEntry;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public void setAppearanceRatio(int appearanceRatio)
    {
        this.appearanceRatio = appearanceRatio;
    }

    public void setDisapperanceProbability(int disapperanceProbability)
    {
        this.disapperanceProbability = disapperanceProbability;
    }

    public void setIcon(int icon)
    {
        this.icon = icon;
    }

    public void setPrerequisite(int prerequisite)
    {
        this.prerequisiteInt = prerequisite;
        if (baseLink.technology != null && prerequisiteInt != -1 && baseLink.technology.size() > prerequisiteInt) {
            this.prerequisite = baseLink.technology.get(prerequisiteInt);
        }
    }

    public void setFoodBonus(int foodBonus)
    {
        this.foodBonus = foodBonus;
    }

    public void setShieldsBonus(int shieldsBonus)
    {
        this.shieldsBonus = shieldsBonus;
    }

    public void setCommerceBonus(int commerceBonus)
    {
        this.commerceBonus = commerceBonus;
    }
    
    public void handleDeletedTechnology(int index)
    {
        if (prerequisiteInt == index) {
            prerequisiteInt = -1;
            prerequisite = null;
        }
        else if (prerequisiteInt > index) {
            prerequisiteInt--;
        }
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
        toReturn = toReturn + "type: " + type + lineReturn;
        toReturn = toReturn + "appearanceRatio: " + appearanceRatio + lineReturn;
        toReturn = toReturn + "disapperanceProbability: " + disapperanceProbability + lineReturn;
        toReturn = toReturn + "icon: " + icon + lineReturn;
        toReturn = toReturn + "prerequisite: " + prerequisiteInt + lineReturn;
        toReturn = toReturn + "foodBonus: " + foodBonus + lineReturn;
        toReturn = toReturn + "shieldsBonus: " + shieldsBonus + lineReturn;
        toReturn = toReturn + "commerceBonus: " + commerceBonus + lineReturn;
        toReturn = toReturn + lineReturn;
        return toReturn;
    }

    public String compareTo(BIQSection section, String separator)
    {
        if (!(section instanceof GOOD))
            return null;
        GOOD two = (GOOD)section;
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
        if (!(type == two.getType()))
        {
                toReturn = toReturn + "Type: " + type + separator + two.getType() + lineReturn;
        }
        if (!(appearanceRatio == two.getAppearanceRatio()))
        {
                toReturn = toReturn + "AppearanceRatio: " + appearanceRatio + separator + two.getAppearanceRatio() + lineReturn;
        }
        if (!(disapperanceProbability == two.getDisapperanceProbability()))
        {
                toReturn = toReturn + "DisapperanceProbability: " + disapperanceProbability + separator + two.getDisapperanceProbability() + lineReturn;
        }
        if (!(icon == two.getIcon()))
        {
                toReturn = toReturn + "Icon: " + icon + separator + two.getIcon() + lineReturn;
        }
        if (!(prerequisiteInt == two.getPrerequisite()))
        {
                toReturn = toReturn + "Prerequisite: " + prerequisiteInt + separator + two.getPrerequisite() + lineReturn;
        }
        if (!(foodBonus == two.getFoodBonus()))
        {
                toReturn = toReturn + "FoodBonus: " + foodBonus + separator + two.getFoodBonus() + lineReturn;
        }
        if (!(shieldsBonus == two.getShieldsBonus()))
        {
                toReturn = toReturn + "ShieldsBonus: " + shieldsBonus + separator + two.getShieldsBonus() + lineReturn;
        }
        if (!(commerceBonus == two.getCommerceBonus()))
        {
                toReturn = toReturn + "CommerceBonus: " + commerceBonus + separator + two.getCommerceBonus() + lineReturn;
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
    
    public void handleSwappedTech() {
        if (prerequisiteInt != -1) {
            prerequisiteInt = prerequisite.getIndex();
        }
    }
}
