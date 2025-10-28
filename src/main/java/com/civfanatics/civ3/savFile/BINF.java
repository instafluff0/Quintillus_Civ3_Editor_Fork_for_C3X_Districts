
package com.civfanatics.civ3.savFile;

import com.civfanatics.civ3.biqFile.util.LittleEndianDataInputStream;
import java.io.IOException;

/**
 * Building Info.  Info about buildings
 * in a city.
 * @author Andrew
 */
public class BINF {
    private int year;
    private int builtByPlayer;
    private int culture;
    
    public void readDataSection(LittleEndianDataInputStream in) throws IOException, HeaderException {
        this.year = in.readInt();
        this.builtByPlayer = in.readInt();
        this.culture = in.readInt();
    }    
}
