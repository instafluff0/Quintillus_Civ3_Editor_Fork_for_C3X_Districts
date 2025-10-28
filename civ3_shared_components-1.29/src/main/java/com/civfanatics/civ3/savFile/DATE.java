
package com.civfanatics.civ3.savFile;

import com.civfanatics.civ3.biqFile.util.LittleEndianDataInputStream;
import java.io.IOException;

/**
 * The DATE structure.  What it represents is unknown.
 * @author Andrew
 */
public class DATE {
    private int dataLength;
    private int[] data;
    
    public void readDataSection(LittleEndianDataInputStream in) throws IOException {
        setDataLength(in.readInt());
        for (int i = 0; i < data.length; i++) {
            setData(i, in.readInt());
        }
    }
    
    static final String lineReturn = java.lang.System.getProperty("line.separator");
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        //sb.append("DATE length: ").append(dataLength).append(lineReturn);
        for (int i = 0; i < data.length; i++) {
            //sb.append("  data[").append(i).append("]: ").append(data[i]);
            sb.append(data[i]).append(", ");
        }
        return sb.toString();
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
