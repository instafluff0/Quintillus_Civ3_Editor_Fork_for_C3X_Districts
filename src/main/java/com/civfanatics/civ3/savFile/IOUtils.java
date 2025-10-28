
package com.civfanatics.civ3.savFile;

import com.civfanatics.civ3.biqFile.util.LittleEndianDataInputStream;
import java.io.IOException;

/**
 *
 * @author Quintillus
 */
public class IOUtils {
    public static String importString(LittleEndianDataInputStream in, int length, String charset) throws IOException{
        byte[] buffer = new byte[length];
        in.read(buffer, 0, length);
        return new String(buffer, charset);
    }
}
