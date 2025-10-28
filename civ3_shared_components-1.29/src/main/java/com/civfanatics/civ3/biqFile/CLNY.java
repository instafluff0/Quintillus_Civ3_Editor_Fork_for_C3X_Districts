package com.civfanatics.civ3.biqFile;
/*
 * The CLNY section is used both for storing information about actual colonies,
 * and storing information about airfields and radar towers, which can thus be
 * considered a variety of colonies.
 */

/**
 *
 * @author Quintillus
 */
public class CLNY extends BIQSection{

    public static final byte COLONY = 0;
    public static final byte AIRFIELD = 1;
    public static final byte RADAR_TOWER = 2;
    public static final byte OUTPOST = 3;

    private int dataLength = 20;  //not 16 as in the documentation.  Only 16 in Vanilla.
    private int ownerType;
    private int owner;
    private int x;
    private int y;
    private int improvementType; //0 = colony, 1 = airfield, 2 = radar tower, 3 = outpost, other -> none (blank graphics)
    /**
     * The graphics file for airfields and colonies is not particularly
     * straightforward.  Airfields have one look for the ancient/medieval eras,
     * and one for industrial/modern.  Outposts have one look for ancient,
     * another for medieval, and look the same for industrial/modern.  Radar
     * towers always look the same no matter what.
     *
     * I do not yet know if the 128x95 dimensions mentioned in the file
     * 'x_airfields and detect.pcx' is a hard-coded limit on the dimensions
     * of outposts and radar towers or not.
     *
     * Question: The AI makes an effort to destroy colonies.  I wonder if a 'type 4' colony could
     * be used as an AI lure in scenarios?  Of course, we don't know if this would even remain in play
     * if in a player's territory (outposts/colonies don't, airfield/radar tower do).
     */

    public CLNY(IO baselink)
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

    public int getImprovementType()
    {
        return improvementType;
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

    public void setImprovementType(int improvementType)
    {
        this.improvementType = improvementType;
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
        toReturn = toReturn + "improvementType: " + improvementType + lineReturn;
        toReturn = toReturn + lineReturn;
        return toReturn;
    }

    public String compareTo(BIQSection section, String separator)
    {
        if (!(section instanceof CLNY))
            return null;
        CLNY two = (CLNY)section;
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
        if (!(improvementType == two.getImprovementType()))
        {
                toReturn = toReturn + "ImprovementType: " + improvementType + separator + two.getImprovementType() + lineReturn;
        }
        return toReturn;
    }
    public Object getProperty(String string) throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }
}
