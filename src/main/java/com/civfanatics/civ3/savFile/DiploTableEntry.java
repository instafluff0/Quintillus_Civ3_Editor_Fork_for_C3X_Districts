
package com.civfanatics.civ3.savFile;

/**
 *
 * @author Andrew
 */
public class DiploTableEntry {

    private int entryType;
    private int data1;
    private int data2;
    
    private static final int AGREEMENT = 0;
    private static final int ALLIANCE = 1;
    private static final int EMBARGO = 2;
    private static final int STRAT_RESOURCE = 5;
    private static final int LUX_RESOURCE = 6;
    private static final int GOLD_PER_TURN = 7;
    private static final int START_OF_GROUP = -1;   //for grouped deals

    public int getEntryType() {
        return entryType;
    }

    public void setEntryType(int entryType) {
        this.entryType = entryType;
    }

    public int getData1() {
        return data1;
    }

    public void setData1(int data1) {
        this.data1 = data1;
    }

    public int getData2() {
        return data2;
    }

    public void setData2(int data2) {
        this.data2 = data2;
    }
    
    
}
