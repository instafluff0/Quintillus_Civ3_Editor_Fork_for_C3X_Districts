
package com.civfanatics.civ3.xplatformeditor.savFunctionality;

import com.civfanatics.civ3.biqFile.IO;
import com.civfanatics.civ3.savFile.SAV;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * "Patches" a SAV with new data.  First goal is changing the RULE tab info.
 * @author Andrew
 */
public class SavPatcher {
    
    static Logger logger = Logger.getLogger(SavPatcher.class);

    public static void patchSAVWithNewRuleData(File savFile, File newRulesBIQFile, List<String>sectionsToReplace) throws IOException {
        makeBackup(savFile);
        
        SAV sav = new SAV();
        sav.readSAVThroughEmbeddedRules(savFile, false);
        
        IO biq = new IO();
        try {
            boolean readBIQ = biq.inputBIQ(newRulesBIQFile);
            if (!readBIQ) {
                logger.error("Could not read BIQ file for SAV patching");
                throw new IOException("Could not read requested BIQ");
            }
        }
        catch(FileNotFoundException ex) {
            logger.info("Could not find file for " + newRulesBIQFile.getPath());
        }
        
        //Replace the rules!
        IO rules = sav.getEmbeddedRules().getEmbeddedRules();
        if (sectionsToReplace.contains("RULE")) {
            rules.rule.set(0, biq.rule.get(0));
        }
        else if (sectionsToReplace.contains("RACE")) {
            rules.civilization = biq.civilization;
        }
        else if (sectionsToReplace.contains("TERR")) {
            rules.terrain = biq.terrain;
        }
        else if (sectionsToReplace.contains("CTZN")) {
            rules.citizens = biq.citizens;
        }
        
        //Output to file
        //N.B. We may wish to make a backup, especially early on in testing
        sav.writeRulePatchedSAV(savFile);
    }

    private static void makeBackup(File savFile) throws IOException {
        File backup = new File(savFile.getAbsolutePath().substring(0, savFile.getAbsolutePath().length() - 4) + "Copy.biq");
        Files.copy(savFile.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
    }
}
