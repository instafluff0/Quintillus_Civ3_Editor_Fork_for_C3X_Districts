
package com.civfanatics.civ3.savFile;

import com.civfanatics.civ3.biqFile.util.LittleEndianDataInputStream;
import java.io.IOException;

/**
 * Continents
 * @author Andrew
 */
public class CONT {
    private String header;
    private int dataLength;
    private int type;   //0 = land, 1 = water
    private int tileCount;
    //TODO: This is not actually per-CONT, but an additional un-headered section after CONT.  Need to move this out.
    private int[]goodCounts; //count of each GOOD on this CONT
    byte[]inputFour = new byte[4];
    
    public void readDataSection(LittleEndianDataInputStream in, int numGoodTypes) throws IOException {
        in.read(inputFour, 0, 4);
        header = new String(inputFour, "Windows-1252");
        
        dataLength = in.readInt();
        type = in.readInt();
        tileCount = in.readInt();
    }
    
}
