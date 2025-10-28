/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.savFile;

import com.civfanatics.civ3.biqFile.util.LittleEndianDataInputStream;
import java.io.IOException;

/**
 *
 * @author Andrew
 */
public class SavUNIT {
    private SAV baselink;
    private byte[] inputFour = new byte[4];
    private String header;
    private int dataLength;
    private int ID; //can be reused?
    private int x;
    private int y;
    private int previousX;
    private int previousY;
    private int owner;  //index to LEAD
    private int nationality;    //index to RACE
    private int barbTribe;
    private int unitType;   //index to PRTO
    private int experienceLevel;    //index to EXPR
    private int flags1;
    private int damage;
    private int movementUsed;   //in thirds of a percent
    private int unknown;
    private int workerJob;  //index into TRFM
    private int unknown2;
    private int loadedOnUnitID; //ID of unit loaded onto; -1 if not loaded
    private int flags2;
    private long unknown3;
    private int unknown4;
    private int useName;
    private byte[] nameBuffer = new byte[60];
    String name;    //60
    private int goToX;
    private int goToY;
    /**
     * 268 bytes.  Guessing some are like "Road to X", "Road to Y", etc.
     * Note this was 265 in the documentation, but per Gramphos and Quintillus, hex investigations
     * confirm 3 additional somewhere prior to c3cunitFlags.  I've added them here.
     */
    private byte[] someBytes = new byte[268];
    
    //C3C
    /**
     * Gramphos reports that the 41st byte here should be 0xFF, otherwise the unit
     * text is red, and there are 3 exclamation marks after the name, e.g. Veteran!!!
     * I've seen this somewhere before, but cannot remember where.  I kind of want
     * to say charm, but am not sure about that.  If that can be established, another
     * field would be decoded.
     */
    private byte[] fortyFourBytes = new byte[44];
    private int c3cUnitFlags;
    /**
     * This is not in Dianthus's documentation, but both my own investigation and
     * Gramphos's indicate an additional 4 bytes exist.
     */
    private int unknown5;
    private IDLS idls;
    
    //If IDLS section (c3c unit flags = 0x20000000)
    
    public SavUNIT(SAV baselink) {
        this.baselink = baselink;
    }
    
    public void readDataSection(LittleEndianDataInputStream in) throws IOException {
        in.read(inputFour, 0, 4);
        header = new String(inputFour, "Windows-1252");
        
        this.dataLength = in.readInt();
        this.ID = in.readInt();
        this.x = in.readInt();
        this.y = in.readInt();
        this.previousX = in.readInt();
        this.previousY = in.readInt();
        this.owner = in.readInt();
        this.nationality = in.readInt();
        this.barbTribe = in.readInt();
        this.unitType = in.readInt();
        this.experienceLevel = in.readInt();
        this.flags1 = in.readInt();
        this.damage = in.readInt();
        this.movementUsed = in.readInt();
        this.unknown = in.readInt();
        this.workerJob = in.readInt();
        this.unknown2 = in.readInt();
        this.loadedOnUnitID = in.readInt();
        this.flags2 = in.readInt();
        this.unknown3 = in.readLong();
        this.useName = in.readInt();
        in.read(nameBuffer, 0, 60);
        name = new String(nameBuffer, "Windows-1252");
        this.goToX = in.readInt();
        this.goToY = in.readInt();
        in.read(someBytes, 0, 268);
        
        if (this.baselink.majorVersion == SavVERSION.SAV_CONQUESTS) {
            in.read(fortyFourBytes, 0, 44);
            c3cUnitFlags = in.readInt();
            unknown5 = in.readInt();
            
            if ((c3cUnitFlags & 2) != 0) {
                idls = new IDLS();
                in.read(inputFour, 0, 4);
                idls.header = new String(inputFour, "Windows-1252");
                idls.headerLength = in.readInt();
                idls.unknown = in.readInt();
                idls.numDataItems = in.readInt();
                idls.data = new int[idls.numDataItems];
                for (int i = 0; i < idls.numDataItems; i++) {
                    idls.data[i] = in.readInt();
                }
            }
        }
    }
    
    class IDLS {
        private String header;
        private int headerLength;
        private int unknown;
        private int numDataItems;
        private int[] data;
    }
    
    @Override
    public String toString() {
        return "SAV Unit at " + x + ", " + y + "; IDLS length = " + idls.numDataItems;
    }
}
