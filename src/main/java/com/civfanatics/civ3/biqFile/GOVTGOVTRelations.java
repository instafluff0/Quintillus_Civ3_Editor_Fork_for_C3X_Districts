package com.civfanatics.civ3.biqFile;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Quintillus
 */
public class GOVTGOVTRelations {
    public int canBribe;
    public int briberyModifier;
    public int resistanceModifier;
    public GOVTGOVTRelations()
    {

    }

    public void setCanBribe(int canBribe)
    {
        this.canBribe = canBribe;
    }
    public void setBriberyModifier(int briberyModifier)
    {
        this.briberyModifier = briberyModifier;
    }
    public void setResistanceModifier(int resistanceModifier)
    {
        this.resistanceModifier = resistanceModifier;
    }

    public int getCanBribe()
    {
        return canBribe;
    }
    public int getBriberyModifier()
    {
        return briberyModifier;
    }
    public int getResistanceModifier()
    {
        return resistanceModifier;
    }

    @Override
    public String toString()
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "  canBribe: " + canBribe + lineReturn;
        toReturn = toReturn + "  briberyModifier: " + briberyModifier + lineReturn;
        toReturn = toReturn + "  resistanceModifier: " + resistanceModifier + lineReturn;
        return toReturn;
    }

    public String compareTo(GOVTGOVTRelations two, String separator)
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn = "";
        if (!(canBribe == two.getCanBribe()))
        {
                toReturn = toReturn + "CanBribe: " + canBribe + separator + two.getCanBribe() + lineReturn;
        }
        if (!(briberyModifier == two.getBriberyModifier()))
        {
                toReturn = toReturn + "BriberyModifier: " + briberyModifier + separator + two.getBriberyModifier() + lineReturn;
        }
        if (!(resistanceModifier == two.getResistanceModifier()))
        {
                toReturn = toReturn + "ResistanceModifier: " + resistanceModifier + separator + two.getResistanceModifier() + lineReturn;
        }
        if (toReturn.equals(""))
        {
            toReturn = "";
        }
        return toReturn;
    }
}
