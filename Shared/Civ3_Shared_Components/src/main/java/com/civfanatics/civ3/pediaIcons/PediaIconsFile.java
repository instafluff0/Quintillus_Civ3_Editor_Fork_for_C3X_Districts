
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
    Map<String, String> smallIcons = new HashMap();
    Map<String, String> largeIcons = new HashMap();
    
    enum NextLineMode {
      REGULAR, TECH  
    };
    
    public void parseFile(String fileName) throws FileNotFoundException {
        Scanner s = new Scanner(new File(fileName));
        while (s.hasNextLine()) {
            String nextLine = s.nextLine();
            if (nextLine.startsWith("#TECH")) {
                String techIconPath  = s.nextLine();
                techIconPath = techIconPath.replaceAll("\\\\", "/");
                if (nextLine.endsWith("_LARGE")) {
                    largeIcons.put(nextLine.substring(1, nextLine.length() - 6), techIconPath);
                }
                else {
                    smallIcons.put(nextLine.substring(1), techIconPath);
                }
            }
            else if (nextLine.startsWith("#start resources")) {
                for (;;) {
                    String key = s.nextLine();
                    while (key.isEmpty()) {
                        key = s.nextLine();
                    }
                    if (key.equals("#End Resources")) {
                        break;
                    }
                    String largeLine = s.nextLine();
                    String smallLine = s.nextLine();
                    largeLine = largeLine.replaceAll("\\\\", "/");
                    smallLine = smallLine.replaceAll("\\\\", "/");
                    
                    largeIcons.put(key.substring(6), largeLine);
                    smallIcons.put(key.substring(6), smallLine);
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
    public String getIconFileName(String entryName, boolean large) {
        if (large) {
            return largeIcons.get(entryName);
        }
        else {
            return smallIcons.get(entryName);
        }
    }
}
