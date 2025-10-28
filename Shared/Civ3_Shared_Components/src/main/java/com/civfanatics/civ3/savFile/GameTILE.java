
package com.civfanatics.civ3.savFile;

import com.civfanatics.civ3.biqFile.util.LittleEndianDataInputStream;
import java.io.IOException;

/**
 * Represents a tile in the game (as opposed to tile in the scenario).
 * @author Andrew
 */
public class GameTILE {
    private String header1;
    private int dataLength;
    private byte riverInfo;
    private byte owner;
    private short unknown;
    private int resources;
    private int topUnitID;
    private byte image;
    private int file;
    private short unknown2;
    private byte overlayBits;   //See TILE2 for C3C
    private byte terrainBits;   //real/base
    private byte bonusBits;
    private byte riverData;
    private short barbCampID;
    private short cityID;
    private short colonyID;
    private short continent;
    private short unknown3;
    private short unknown4;
    private int hasRuins;
    
    private String header2;
    private int dataLength2;
    private int overlayBitsC3C;
    private byte unknown5;
    private byte terrainBitsC3C;    //base/real
    private short unknown6;
    private int bonusBitsC3C;
    
    private String header3;
    private int dataLength3;
    private int unknown7;
    
    private String header4;
    private int dataLength4;
    private int exploredBy;
    private int visibleToByUnits;
    private int visibleTo2; //maybe recon
    private int visibleToByCities;
    private int unknown8;
    private short cityWithWorkers;
    private short[]tradeRoutes = new short[32]; //"The trade route number for the tile as seen by each LEAD."
    private byte[]bonusBitsArray = new byte[32]; //"The bonus bits as seen by each LEAD. Doesn't seem to update for owner."
    byte[] unknownBytes = new byte[10]; //10
    
    byte[] inputFour = new byte[4];
    
    public void readDataSection(LittleEndianDataInputStream in) throws IOException {
        in.read(inputFour, 0, 4);
        header1 = new String(inputFour, "Windows-1252");
        assert("TILE".equals(header1));
        dataLength = in.readInt();
        riverInfo = in.readByte();
        owner = in.readByte();
        unknown = in.readShort();
        resources = in.readInt();
        topUnitID = in.readInt();
        image = in.readByte();
        file = in.readByte();
        unknown2 = in.readShort();
        overlayBits = in.readByte();
        terrainBits = in.readByte();
        bonusBits = in.readByte();
        riverData = in.readByte();
        barbCampID = in.readShort();
        cityID = in.readShort();
        colonyID = in.readShort();
        continent = in.readShort();
        unknown3 = in.readShort();
        unknown4 = in.readShort();
        hasRuins = in.readInt();
        
        //C3C
        in.read(inputFour, 0, 4);
        header2 = new String(inputFour, "Windows-1252");
        assert("TILE".equals(header2));
        dataLength2 = in.readInt();
        overlayBitsC3C = in.readInt();
        unknown5 = in.readByte();
        terrainBitsC3C = in.readByte();
        unknown6 = in.readShort();
        bonusBitsC3C = in.readInt();
        
        in.read(inputFour, 0, 4);
        header3 = new String(inputFour, "Windows-1252");
        assert("TILE".equals(header3));
        dataLength3 = in.readInt();
        unknown7 = in.readInt();
        
        //End CFC
        
        in.read(inputFour, 0, 4);
        header4 = new String(inputFour, "Windows-1252");
        assert("TILE".equals(header4));
        dataLength4 = in.readInt();
        exploredBy = in.readInt();
        visibleToByUnits = in.readInt();
        visibleTo2 = in.readInt();
        visibleToByCities = in.readInt();
        unknown8 = in.readInt();
        cityID = in.readShort();
        for (int i = 0; i < 32; i++) {
            tradeRoutes[i] = in.readShort();
        }
        for (int i = 0; i < 32; i++) {
            bonusBitsArray[i] = in.readByte();
        }
        for (int i = 0; i < 10; i++) {
            unknownBytes[i] = in.readByte();
        }
    }
}
