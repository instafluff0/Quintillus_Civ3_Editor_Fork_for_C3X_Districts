package com.civfanatics.civ3.biqFile;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Quintillus
 */
public class SLOC extends BIQSection{
    private int dataLength = 16;
    private int ownerType;
    private int owner;
    private int x;
    private int y;
    public SLOC(IO baselink)
    {
        super(baselink);
    }

    public int getDataLength()
    {
        return dataLength;
    }

    public int getOwnerType()
    {
        return ownerType;
    }

    public int getOwner()
    {
        return owner;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public void setOwnerType(int ownerType)
    {
        this.ownerType = ownerType;
    }

    public void setOwner(int owner)
    {
        this.owner = owner;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public String toEnglish(){
        return toString();
    }

    @Override
    public String toString()
    {
        String lineReturn = java.lang.System.getProperty("line.separator");
        String toReturn =  "dataLength: " + dataLength + lineReturn;
        toReturn = toReturn + "ownerType: " + ownerType + lineReturn;
        toReturn = toReturn + "owner: " + owner + lineReturn;
        toReturn = toReturn + "x: " + x + lineReturn;
        toReturn = toReturn + "y: " + y + lineReturn;
        toReturn = toReturn + lineReturn;
        return toReturn;
    }

    public String compareTo(BIQSection section, String separator)
    {
        if (!(section instanceof SLOC))
            return null;
        SLOC two = (SLOC)section;
        String toReturn = "";
        String lineReturn = java.lang.System.getProperty("line.separator");
        if (!(dataLength == two.getDataLength()))
        {
                toReturn = toReturn + "DataLength: " + dataLength + separator + two.getDataLength() + lineReturn;
        }
        if (!(ownerType == two.getOwnerType()))
        {
                toReturn = toReturn + "OwnerType: " + ownerType + separator + two.getOwnerType() + lineReturn;
        }
        if (!(owner == two.getOwner()))
        {
                toReturn = toReturn + "Owner: " + owner + separator + two.getOwner() + lineReturn;
        }
        if (!(x == two.getX()))
        {
                toReturn = toReturn + "X: " + x + separator + two.getX() + lineReturn;
        }
        if (!(y == two.getY()))
        {
                toReturn = toReturn + "Y: " + y + separator + two.getY() + lineReturn;
        }
        return toReturn;
    }
    public Object getProperty(String string) throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }

}
