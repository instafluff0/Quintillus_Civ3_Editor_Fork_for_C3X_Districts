
package com.civfanatics.civ3.pediaIcons;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Represents a PediaIcons.txt file.  Allows easy lookup of pedia icons within
 * a program, and eventually shall allow editing and exporting.
 * TODO: Determine if you can mix-and-match pedia icons in practice, e.g. a
 * government and tech share the same Civilopedia entry.  Would be confusing, but
 * may work.  Although doubtful, since then the text would match too and that
 * would really be confusing.
 * @author Andrew
 */
public class PediaIconsFile {
    
    List<PediaFileSection> fileSections = new ArrayList<PediaFileSection>();
    
    //Easy-access maps
    Map<String, String> smallTechIcons = new HashMap();
    Map<String, String> largeTechIcons = new HashMap();
    
    enum NextLineMode {
      REGULAR, TECH  
    };
    
    public void parseFile(String fileName) throws FileNotFoundException {
        Scanner s = new Scanner(new File(fileName));
        while (s.hasNextLine()) {
            String nextLine = s.nextLine();
            if (nextLine.startsWith("#TECH")) {
                String techEntry  = s.nextLine();
                if (nextLine.endsWith("_LARGE")) {
                    largeTechIcons.put(nextLine.substring(1), techEntry);
                }
                else {
                    smallTechIcons.put(nextLine.substring(1), techEntry);
                }
            }
        }
    }
    
    /**
     * Returns the file name of the tech with the specified Civilopedia entry.
     * Allows specifying the small or large icon; returns null if none exists.
     * @param entryName Civilopedia entry name, e.g. TECH_Bronze_Working
     * @param large True if the large icon; false if the small icon
     * @return The file name of the icon, or null if there is none.
     */
    public String getTechIcon(String entryName, boolean large) {
        if (large) {
            return largeTechIcons.get(entryName);
        }
        else {
            return smallTechIcons.get(entryName);
        }
    }
}
