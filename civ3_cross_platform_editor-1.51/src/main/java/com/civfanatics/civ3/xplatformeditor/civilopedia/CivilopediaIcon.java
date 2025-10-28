package com.civfanatics.civ3.xplatformeditor.civilopedia;

import com.civfanatics.civ3.biqFile.BIQSection;
import com.civfanatics.civ3.xplatformeditor.imageSupport.Civ3PCXFilter;
import com.civfanatics.civ3.xplatformeditor.utils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * Utilities for interacting with Civilopedia Icon.
 * 
 * @author Andrew
 */
public class CivilopediaIcon {
    
    static Logger logger = Logger.getLogger(CivilopediaIcon.class);
    
    static Map<String, BufferedImage> iconCache = new HashMap<>();
    
    public static BufferedImage getLargeCivilopediaIcon(BIQSection item) throws FileNotFoundException {
        String fileName = item.baseLink.getPediaIcons().getIconFileName(item.getCivilopediaEntry(), true);
        return getIcon(fileName, item);
    }
    
    public static BufferedImage getSmallCivilopediaIcon(BIQSection item) throws FileNotFoundException {
        String fileName = item.baseLink.getPediaIcons().getIconFileName(item.getCivilopediaEntry(), false);
        return getIcon(fileName, item);
    }
    
    public static File getScenarioSpecificFile(BIQSection item, boolean isLarge) throws FileNotFoundException {
        String fileName = item.baseLink.getPediaIcons().getIconFileName(item.getCivilopediaEntry(), isLarge);
        String scenarioFileName = utils.findFile(fileName, item.baseLink);
        return new File(scenarioFileName);
    }
    
    private static BufferedImage getIcon(String fileName, BIQSection item) throws FileNotFoundException {
        BufferedImage img = null;
        if (logger.isDebugEnabled())
            logger.debug("Icon file name: " + fileName);
        if (fileName != null) {
            if (iconCache.containsKey(fileName)) {
                return iconCache.get(fileName);
            }
            else {
                String scenarioFileName = utils.findFile(fileName, item.baseLink);

                long pcxStart = System.nanoTime();
                Civ3PCXFilter iconFilter = new Civ3PCXFilter(scenarioFileName);
                iconFilter.processFile();
                long pcxEnd = System.nanoTime();
                if (logger.isDebugEnabled())
                    logger.debug("PCX time: " + (pcxEnd - pcxStart)/1000000 + " ms");
                img = iconFilter.getBufferedImage();
                iconCache.put(fileName, img);
            }
        }
        return img;
    }
}
