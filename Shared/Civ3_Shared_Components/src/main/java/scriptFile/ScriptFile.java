
package scriptFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Andrew
 */
public class ScriptFile {
    Map<Integer, String> techBlurbs = new HashMap();
    
    public void parseFile(String fileName) throws FileNotFoundException {
        Scanner s = new Scanner(new File(fileName));
        while (s.hasNextLine()) {
            String nextLine = s.nextLine();
            if (nextLine.startsWith("#SCIENCEADVICETECH")) {
                String blurb = s.nextLine();
                try {
                    Integer techID = Integer.parseInt(nextLine.substring(18));
                    techBlurbs.put(techID, blurb);
                }
                catch(NumberFormatException ex) {
                    // some other science advice...
                }
            }
        }
    }
    
    public String getTechBlurb(int techID) {
        if (techBlurbs.containsKey(techID)) {
            return techBlurbs.get(techID);
        }
        return "";
    }
}
