
package com.civfanatics.civ3.xplatformeditor.imageSupport;

import com.civfanatics.civ3.biqFile.IO;
import com.civfanatics.civ3.xplatformeditor.Main;
import static com.civfanatics.civ3.xplatformeditor.Main.biqFile;
import static com.civfanatics.civ3.xplatformeditor.Main.biqIndex;
import static com.civfanatics.civ3.xplatformeditor.Main.fileSlash;
import com.civfanatics.civ3.xplatformeditor.OldPCXFilter;
import com.civfanatics.civ3.xplatformeditor.utils;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

/**
 * Retrieves civ-colored units_32.pcx icons.  Uses caching to improve performance.
 * N.B. Not fully optimized; notably will re-set colors more than necessary.  Though
 * I suppose that does help reduce memory overhead.
 * @author Andrew
 */
public class Units32Supplier {
    
    static Logger logger = Logger.getLogger(Units32Supplier.class);
    
    static Civ3PCXFilter units_32;
    static Map<String, BufferedImage> iconCache = new HashMap<>();
    
    public static BufferedImage getUnit32Image(int iconIndex, int colorIndex) {
        if (units_32 == null) {
            loadUnits32();
        }
        String key = iconIndex + " " + colorIndex;
        if (iconCache.containsKey(key)) {
            return iconCache.get(key);
        }
        
        setCivSpecificColors(colorIndex);
        BufferedImage unit32Icon = getUnitImageFromUnits32(iconIndex);
        
        iconCache.put(key, unit32Icon);
        return unit32Icon;
    }
    
    
    private static void loadUnits32() {
        
        String units32Name = null;
        try{
            units32Name = utils.findFile("units_32.pcx", "Art" + fileSlash + "Units" + fileSlash, biqFile.get(biqIndex));
        }
        catch(FileNotFoundException e){
            logger.error("Could not find units_32.pcx; civInstallDir = " + Main.settings.civInstallDir, e);
            return;
        }
        units_32 = new Civ3PCXFilter(units32Name);
        if (logger.isDebugEnabled())
            logger.debug("about to read the units32");
        units_32.readFile();
        if (logger.isDebugEnabled())
            logger.debug("about to parse the units32");
        units_32.parse();
        if (logger.isDebugEnabled())
            logger.debug("about to buffer the units32");
        units_32.createBufferedImage();
    }
    
    /**
     * Grabs a specific unit's image out a units_32.pcx file.  Assumes colors have
     * already been set.
     * @return The unit's icon.
     */
    private static BufferedImage getUnitImageFromUnits32(int iconIndex) {
        
        units_32.createBufferedImage();
        BufferedImage units32 = units_32.getBufferedImage();
        int iconWidth = (units32.getWidth()-1)/32;
        int iconX = iconIndex % iconWidth;
        int iconY = iconIndex/iconWidth;
        BufferedImage image = units32.getSubimage((iconX*32)+1+iconX, (iconY*32)+1+iconY, 32, 32);
        return image;
    }
    
    /**
     * Sets specific Civ colors on the units_32.pcx image.
     * @param index The index of the Civ <b>color</b>.  Not the civ's index, but its color's index.
     */
    private static void setCivSpecificColors(int index) {
        Color[]palette = loadColorPalette(index);
        for (int i = 0; i < 15; i++) {
            units_32.setColor(i, palette[i]);
        }
        for (int i = 16; i < 64; i+=2) {
            units_32.setColor(i, palette[i]);
        }
    }
    
    /**
     * Loads a color palette from a ntp##.pcx file.
     * @param index The index of the NTP file.
     * @return The NTP file's color palette.
     */
    private static Color[] loadColorPalette(int index)
    {
        String pcxString = null;
        try{
            IO biq = Main.biqFile.get(Main.biqIndex);
            pcxString = utils.findFile(ImageResources.ntpNames[index] + ".pcx", "Art" + fileSlash + "units" + fileSlash + "palettes" + fileSlash,  biq);
        }
        catch(FileNotFoundException e){
            JOptionPane.showMessageDialog(null, "Couldn't find it "+ e.getMessage());
        }
        
        OldPCXFilter pcx = new OldPCXFilter(pcxString);
        pcx.readFile();
        pcx.parse();
        
        Color[] palette = pcx.palette.palette;
        return palette;
    }
}
