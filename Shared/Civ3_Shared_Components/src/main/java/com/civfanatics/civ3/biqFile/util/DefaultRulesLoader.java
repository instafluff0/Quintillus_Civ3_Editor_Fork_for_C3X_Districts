
package com.civfanatics.civ3.biqFile.util;

import com.civfanatics.civ3.biqFile.IO;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * This is a utility class to help with loading default rules for BIQs that don't
 * have any.  For this to be used successfully, the consuming application will
 * need to set the defaultRulesPath to the location of conquests.biq, and will
 * optionally need to set the defaultRulesLanguage if it is not English or
 * another language within the Windows-1252 character set.
 * @author Andrew
 */
public class DefaultRulesLoader {
    public static String defaultRulesPath = "";
    public static String defaultRulesLanguage = "English";
    
    public static IO getDefaultRules() throws FileNotFoundException {
        File file = new File(defaultRulesPath);
        IO biqInput = new IO();
        biqInput.fileName = file.getName();
        
        biqInput.setLanguage(defaultRulesLanguage);
        try {
            biqInput.inputBIQ(file);
        }
        catch(FileNotFoundException ex) {
            throw ex;
        }
        return biqInput;
    }
}
