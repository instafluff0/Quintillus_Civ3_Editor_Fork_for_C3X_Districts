
package com.civfanatics.civ3.savFile;

import com.civfanatics.civ3.biqFile.util.LittleEndianDataInputStream;
import java.io.IOException;
import org.apache.log4j.Logger;

public class PALV {
    
    static Logger logger = Logger.getLogger(PALV.class);

    private SAV baselink;
    private byte[] inputFour = new byte[4];
    private String header;
    private int dataLength;
    private int unknown1;
    private int[] sectionCulture = new int[32];
    private int unknown2;
    private int unknown3;
    private int sectionMask;
    
    //If PTW/Conquests (Sav version >= 18)
    private int unusedUpgrades; //number of unused

    public PALV(SAV baselink) {
        this.baselink = baselink;
    }

    public void readDataSection(LittleEndianDataInputStream in) throws IOException {
        in.read(inputFour, 0, 4);
        header = new String(inputFour, "Windows-1252");
        
        this.dataLength = in.readInt();
        this.unknown1 = in.readInt();
        for (int i = 0; i < 32; i++) {
            this.sectionCulture[i] = in.readInt();
        }
        this.unknown2 = in.readInt();
        this.unknown3 = in.readInt();
        this.sectionMask = in.readInt();
        
        if (baselink.majorVersion >= SavVERSION.SAV_PTW) {
            this.unusedUpgrades = in.readInt();
        }
    }
}
