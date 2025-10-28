
package com.civfanatics.civ3.savFile;

import com.civfanatics.civ3.biqFile.util.LittleEndianDataInputStream;
import java.io.IOException;
import org.apache.log4j.Logger;

public class SavCLNY {
    
    static Logger logger = Logger.getLogger(SavCLNY.class);
    
    private String header;
    private int sectionLength;
    private int uniqueID;
    private int x;
    private int y;
    private int controllingPlayer;
    
    
    public void readDataSection(LittleEndianDataInputStream in) throws IOException {
        byte[] inputFour = new byte[4];
        in.read(inputFour, 0, 4);
        header = new String(inputFour, "Windows-1252");
        
        this.sectionLength = in.readInt();
        this.uniqueID = in.readInt();
        this.x = in.readInt();
        this.y = in.readInt();
        this.controllingPlayer = in.readInt();
    }
}
