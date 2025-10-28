/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.savFile;

import com.civfanatics.civ3.biqFile.IO;
import com.civfanatics.civ3.biqFile.util.LittleEndianDataInputStream;
import com.civfanatics.civ3.biqFile.util.LittleEndianDataOutputStream;
import java.io.IOException;

/**
 *
 * @author Andrew
 */
public class EmbeddedRules {
    
    private String currentCharset = "Windows-1252";//ISO-8859-1";
    
    byte[] header = new byte[4];  //"BIC "
    int sectionHeaderLength;    //including the 4 from embeddedRulesLength
    int biqDataLength;
    byte[] embeddedRulesInfo;   //length = sectionHeaderLength - 4.  Usually (always?) 520.
    String searchPath;  //length = 260, likely matches BIQ search path.
    String saveFileName;    //Should match file name if not renamed.
    
    IO embeddedRules;
    
    public void readEmbeddedRules(LittleEndianDataInputStream in) {
        try {
            in.read(header, 0, 4);
            sectionHeaderLength = in.readInt();
            biqDataLength = in.readInt();
            int embeddedRulesInfoLength = sectionHeaderLength - 4;
            embeddedRulesInfo = new byte[embeddedRulesInfoLength];
            in.read(embeddedRulesInfo, 0, embeddedRulesInfoLength);
            if (embeddedRulesInfoLength == 520) {
                byte[] buffer = new byte[260];
                System.arraycopy(embeddedRulesInfo, 0, buffer, 0, 260);
                searchPath = new String(buffer, currentCharset);
                System.arraycopy(embeddedRulesInfo, 260, buffer, 0, 260);
                saveFileName = new String(buffer, currentCharset);
            }
            //TODO: Hand off to IO the input stream
            LittleEndianDataInputStream[] ins = new LittleEndianDataInputStream[1];
            ins[0] = in;
            embeddedRules = new IO();
            embeddedRules.inputBIQ(ins, null, null, true);
        }
        catch(IOException ex) {
            System.err.println("IOException - add improved handling" + ex.getMessage());
        }
        
    }
    
    public void outputEmbeddedRules(LittleEndianDataOutputStream out) {
        try {
            out.write(header);
            out.writeInt(sectionHeaderLength);
            out.writeInt(biqDataLength);
            out.write(embeddedRulesInfo);
            embeddedRules.outputBIQ(out, true);
        }
        catch(IOException ex) {
            System.err.println("IOException - add improved handling" + ex.getMessage());
        }
    }

    public IO getEmbeddedRules() {
        return embeddedRules;
    }
}
