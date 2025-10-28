package com.civfanatics.civ3.biqFile;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Quintillus
 */
public class WCHR extends BIQSection{
    private int dataLength = 52;
    private int selectedClimate = 1;
    private int actualClimate = 1;
    private int selectedBarbarianActivity = 1;
    private int actualBarbarianActivity = 1;
    private int selectedLandform = 1;
    private int actualLandform = 1;
    private int selectedOceanCoverage = 1;
    private int actualOceanCoverage = 1;
    private int selectedTemperature = 1;
    private int actualTemperature = 1;
    private int selectedAge = 1;
    private int actualAge = 1;
    private int worldSize = 2;
    public WCHR(IO baselink)
    {
        super(baselink);
    }
    public int getDataLength()
    {
        return dataLength;
    }

    public int getSelectedClimate()
    {
        return selectedClimate;
    }

    public int getActualClimate()
    {
        return actualClimate;
    }

    public int getSelectedBarbarianActivity()
    {
        return selectedBarbarianActivity;
    }

    public int getActualBarbarianActivity()
    {
        return actualBarbarianActivity;
    }

    public int getSelectedLandform()
    {
        return selectedLandform;
    }

    public int getActualLandform()
    {
        return actualLandform;
    }

    public int getSelectedOceanCoverage()
    {
        return selectedOceanCoverage;
    }

    public int getActualOceanCoverage()
    {
        return actualOceanCoverage;
    }

    public int getSelectedTemperature()
    {
        return selectedTemperature;
    }

    public int getActualTemperature()
    {
        return actualTemperature;
    }

    public int getSelectedAge()
    {
        return selectedAge;
    }

    public int getActualAge()
    {
        return actualAge;
    }

    public int getWorldSize()
    {
        return worldSize;
    }






    public void setDataLength(int dataLength)
    {
        this.dataLength = dataLength;
    }

    public void setSelectedClimate(int selectedClimate)
    {
        this.selectedClimate = selectedClimate;
    }

    public void setActualClimate(int actualClimate)
    {
        this.actualClimate = actualClimate;
    }

    public void setSelectedBarbarianActivity(int selectedBarbarianActivity)
    {
        this.selectedBarbarianActivity = selectedBarbarianActivity;
    }

    public void setActualBarbarianActivity(int actualBarbarianActivity)
    {
        this.actualBarbarianActivity = actualBarbarianActivity;
    }

    public void setSelectedLandform(int selectedLandform)
    {
        this.selectedLandform = selectedLandform;
    }

    public void setActualLandform(int actualLandform)
    {
        this.actualLandform = actualLandform;
    }

    public void setSelectedOceanCoverage(int selectedOceanCoverage)
    {
        this.selectedOceanCoverage = selectedOceanCoverage;
    }

    public void setActualOceanCoverage(int actualOceanCoverage)
    {
        this.actualOceanCoverage = actualOceanCoverage;
    }

    public void setSelectedTemperature(int selectedTemperature)
    {
        this.selectedTemperature = selectedTemperature;
    }

    public void setActualTemperature(int actualTemperature)
    {
        this.actualTemperature = actualTemperature;
    }

    public void setSelectedAge(int selectedAge)
    {
        this.selectedAge = selectedAge;
    }

    public void setActualAge(int actualAge)
    {
        this.actualAge = actualAge;
    }

    public void setWorldSize(int worldSize)
    {
        this.worldSize = worldSize;
    }

    public String toEnglish(){
        return toString();
    }

    @Override
    public String toString()
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "dataLength: " + dataLength + lineReturn;
        toReturn = toReturn + "selectedClimate: " + selectedClimate + lineReturn;
        toReturn = toReturn + "actualClimate: " + actualClimate + lineReturn;
        toReturn = toReturn + "selectedBarbarianActivity: " + selectedBarbarianActivity + lineReturn;
        toReturn = toReturn + "actualBarbarianActivity: " + actualBarbarianActivity + lineReturn;
        toReturn = toReturn + "selectedLandform: " + selectedLandform + lineReturn;
        toReturn = toReturn + "actualLandform: " + actualLandform + lineReturn;
        toReturn = toReturn + "selectedOceanCoverage: " + selectedOceanCoverage + lineReturn;
        toReturn = toReturn + "actualOceanCoverage: " + actualOceanCoverage + lineReturn;
        toReturn = toReturn + "selectedTemperature: " + selectedTemperature + lineReturn;
        toReturn = toReturn + "actualTemperature: " + actualTemperature + lineReturn;
        toReturn = toReturn + "selectedAge: " + selectedAge + lineReturn;
        toReturn = toReturn + "actualAge: " + actualAge + lineReturn;
        toReturn = toReturn + "worldSize: " + worldSize + lineReturn;
        toReturn = toReturn + lineReturn;
        return toReturn;
    }

    public String compareTo(BIQSection section, String separator)
    {
        if (!(section instanceof WCHR))
            return null;
        WCHR two = (WCHR)section;
        String toReturn = "";
        String lineReturn = java.lang.System.getProperty("line.separator");
        if (!(dataLength == two.getDataLength()))
        {
                toReturn = toReturn + "DataLength: " + dataLength + separator + two.getDataLength() + lineReturn;
        }
        if (!(selectedClimate == two.getSelectedClimate()))
        {
                toReturn = toReturn + "SelectedClimate: " + selectedClimate + separator + two.getSelectedClimate() + lineReturn;
        }
        if (!(actualClimate == two.getActualClimate()))
        {
                toReturn = toReturn + "ActualClimate: " + actualClimate + separator + two.getActualClimate() + lineReturn;
        }
        if (!(selectedBarbarianActivity == two.getSelectedBarbarianActivity()))
        {
                toReturn = toReturn + "SelectedBarbarianActivity: " + selectedBarbarianActivity + separator + two.getSelectedBarbarianActivity() + lineReturn;
        }
        if (!(actualBarbarianActivity == two.getActualBarbarianActivity()))
        {
                toReturn = toReturn + "ActualBarbarianActivity: " + actualBarbarianActivity + separator + two.getActualBarbarianActivity() + lineReturn;
        }
        if (!(selectedLandform == two.getSelectedLandform()))
        {
                toReturn = toReturn + "SelectedLandform: " + selectedLandform + separator + two.getSelectedLandform() + lineReturn;
        }
        if (!(actualLandform == two.getActualLandform()))
        {
                toReturn = toReturn + "ActualLandform: " + actualLandform + separator + two.getActualLandform() + lineReturn;
        }
        if (!(selectedOceanCoverage == two.getSelectedOceanCoverage()))
        {
                toReturn = toReturn + "SelectedOceanCoverage: " + selectedOceanCoverage + separator + two.getSelectedOceanCoverage() + lineReturn;
        }
        if (!(actualOceanCoverage == two.getActualOceanCoverage()))
        {
                toReturn = toReturn + "ActualOceanCoverage: " + actualOceanCoverage + separator + two.getActualOceanCoverage() + lineReturn;
        }
        if (!(selectedTemperature == two.getSelectedTemperature()))
        {
                toReturn = toReturn + "SelectedTemperature: " + selectedTemperature + separator + two.getSelectedTemperature() + lineReturn;
        }
        if (!(actualTemperature == two.getActualTemperature()))
        {
                toReturn = toReturn + "ActualTemperature: " + actualTemperature + separator + two.getActualTemperature() + lineReturn;
        }
        if (!(selectedAge == two.getSelectedAge()))
        {
                toReturn = toReturn + "SelectedAge: " + selectedAge + separator + two.getSelectedAge() + lineReturn;
        }
        if (!(actualAge == two.getActualAge()))
        {
                toReturn = toReturn + "ActualAge: " + actualAge + separator + two.getActualAge() + lineReturn;
        }
        if (!(worldSize == two.getWorldSize()))
        {
                toReturn = toReturn + "WorldSize: " + worldSize + separator + two.getWorldSize() + lineReturn;
        }
        return toReturn;
    }
    public Object getProperty(String string) throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }
}
