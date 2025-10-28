
package com.civfanatics.civ3.savFile;

import com.civfanatics.civ3.biqFile.util.LittleEndianDataInputStream;
import java.io.IOException;

/**
 * The PLGI section.  Hypothesized to be Plague Info.
 * @author Andrew
 */
public class PLGI {
    private int dataLength;
    private int[] data;
    
    public void readDataSection(LittleEndianDataInputStream in) throws IOException {
        setDataLength(in.readInt());
        for (int i = 0; i < data.length; i++) {
            setData(i, in.readInt());
        }
    }
    
    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
        data = new int[dataLength/4];
    }
    
    public void setData(int index, int value) {
        data[index] = value;
    }
    
    public int getData(int index) {
        return data[index];
    }
}
