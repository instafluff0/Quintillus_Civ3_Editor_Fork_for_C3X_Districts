
package com.civfanatics.civ3.xplatformeditor.tabs.map;

import com.civfanatics.civ3.biqFile.CITY;
import com.civfanatics.civ3.biqFile.CLNY;
import com.civfanatics.civ3.biqFile.IO;
import com.civfanatics.civ3.biqFile.SLOC;
import com.civfanatics.civ3.biqFile.TILE;
import com.civfanatics.civ3.biqFile.UNIT;
import com.civfanatics.civ3.xplatformeditor.Main;
import static com.civfanatics.civ3.xplatformeditor.Main.biqFile;
import static com.civfanatics.civ3.xplatformeditor.Main.biqIndex;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 * Contains functionality related to extending (and in the future, perhaps also
 * cropping) the map.
 * @author Andrew
 */
public class MapExtender {
    public static void appendMapToRight() {
        String sharableString = 
            "<html>"
                + "This option allows you to append a map to the right of this map.<br/>"
                + "This lets you power up two Firaxis randomly-generated maps, to create maps that are wider than any map createable in the Firaxis editor.<br/>"
                + "Please note that the height of the two maps <b>must</b> be identical, and the engine's lambda core is still limited to 65,535 tiles maximum.<br/>"
                + "Resonance cascades <i>may</i> occur if the widths of the two maps are not identical.  Proceed with apprehension.<br/>"
                + "In addition, only the terrain will be copied over.  If this facility reaches a state of Xen, additional capabilities may be added.<br/>"
                + "This feature is highly experimental and classified, and unforeseen consequences may ensue when playing CivIII with maps of unusual size.<br/>"
                + "Should you decide this feature has quesitonable ethics, you may cancel out of the file chooser dialog to avoid potential friendly fire."
                + "</html>";
        
        
        String topSecretString = 
            "<html>"
                + "This option allows you to [REDACTED].<br/>"
                + "This lets you [CLASSIFIED], to [ALSO CLASSIFIED].<br/>"
                + "Please note that [REDACTED] <b>must</b> be identical, and the engine's lambda core is still limited to [CLASSIFIED].<br/>"
                + "Resonance cascades <i>may</i> occur if [SECRET].  Proceed with apprehension.<br/>"
                + "In addition, only [ILLEGIBLE].  If this facility reaches a state of Xen, additional capabilities may be added.<br/>"
                + "This feature is highly experimental and classified, and unforeseen consequences may ensue when playing CivIII with [TOP SECRET].<br/>"
                + "Should you decide this feature has quesitonable ethics, you may cancel out of [ITEM_UNAVAILABLE] to avoid potential friendly fire."
                + "</html>";

        JOptionPane.showMessageDialog(Main.mainMain, topSecretString, "Black Mesa [UNKNOWN] Facility!", JOptionPane.WARNING_MESSAGE);
                
        
        File file = Main.mainMain.getInputFile();
        if (file == null) {
            return;
        }
        
        boolean successfulInput = false;
        try {
            successfulInput = Main.mainMain.openBIQ(file);
        }
        catch(FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "The specified file (" + file.getPath() +  ") could not be found. \nCheck if it has been moved or deleted.");
            return;
        }
        if (successfulInput)
        {
            IO rightMap = biqFile.get(biqIndex);
            //BIQ index now points to the new file, which is not desirable
            biqIndex--;
            
            IO leftMap = biqFile.get(biqIndex);
            
            int yMax = leftMap.worldMap.get(0).height;
            int rightYMax = rightMap.worldMap.get(0).height;
            int leftMapWidth = leftMap.worldMap.get(0).width;
            int rightMapWidth = rightMap.worldMap.get(0).width;
            
            //Validate that the files are mergeable
            if (yMax != rightYMax) {
                JOptionPane.showMessageDialog(Main.mainMain,
                    "These two maps do not have the same height, and thus cannot be merged",
                    "Map Heights Must Match", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int newTileCount = (yMax * (leftMapWidth + rightMapWidth)) / 2;
            if (newTileCount > 65535) {
                int response = JOptionPane.showConfirmDialog(Main.mainMain, 
                    "Merging these maps will result in a map with " + newTileCount + " tiles, which is too many to be playable in Civ3.  Continue anyway?",
                    "Too many tiles",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (response == JOptionPane.NO_OPTION) {
                    return;
                }
            }
            
            //Continents
            Map<Short, Short> continentIDMap = new HashMap<>();
            if ((leftMap.continent.size() + rightMap.continent.size()) > Short.MAX_VALUE) {
                //More than 32K continents.  Should never happen as that would be a continent for every two tiles,
                //but if it does, fail gracefully
                JOptionPane.showMessageDialog(Main.mainMain,
                    "Cannot merge due to the two maps (combined) containing more than " + Short.MAX_VALUE + " continents.");
                return;
            }
            short mergedIndex = (short)leftMap.continent.size();
            for (short rightMapIndex = 0; rightMapIndex < rightMap.continent.size(); ) {
                continentIDMap.put(rightMapIndex, mergedIndex);
                leftMap.continent.add(rightMap.continent.get(rightMapIndex));
                rightMapIndex++;
                mergedIndex++;
            }
            
            //The TILE is a 1D array
            //We need to interweave the two 1D arrays in 2D order
            List<TILE> combinedTiles = new ArrayList<TILE>();
            
            for (int y = 0; y < yMax; y++) {
                int start = y % 2 == 0 ? 0 : 1;
                for (int leftX = start; leftX < leftMapWidth; leftX+=2) {
                    int idx = leftMap.calculateTileIndex(leftX, y);
                    combinedTiles.add(leftMap.tile.get(idx));
                }
                for (int rightX = start; rightX < rightMapWidth; rightX+=2) {
                    int idx = rightMap.calculateTileIndex(rightX, y);
                    TILE rightTile = rightMap.getTileAt(rightX, y);
                    rightTile.setContinent(continentIDMap.get(rightTile.getContinent()));
                    combinedTiles.add(rightMap.tile.get(idx));
                }
            }
            
            leftMap.tile = combinedTiles;
            leftMap.worldMap.get(0).width *= 2;
            
            //Also move over other map-related features, with x locations adjusted
            for (SLOC rightSloc : rightMap.startingLocation) {
                rightSloc.setX(rightSloc.getX() + leftMapWidth);
            }
            leftMap.startingLocation.addAll(rightMap.startingLocation);
            
            for (CITY city : rightMap.city) {
                city.setX(city.getX() + leftMapWidth);
            }
            leftMap.city.addAll(rightMap.city);
            
            for (UNIT mapUnit : rightMap.mapUnit) {
                mapUnit.setX(mapUnit.getX() + leftMapWidth);
            }
            leftMap.mapUnit.addAll(rightMap.mapUnit);
            
            for (CLNY colony : rightMap.colony) {
                colony.setX(colony.getX() + leftMapWidth);
            }
            leftMap.colony.addAll(rightMap.colony);
            
            //Smooth out the graphics along the seams, to the extent possible
            //This will need to be done at XMIN (0), XMAX, as well as the original left's XMAX and the original left's XMAX + 1
            for (int y = 0; y < yMax; y+=2) {
                //Case 1: Zero
                MapUtils.recalculateFileAndIndex(leftMap, 0, y, true);
                //Case 2: XMAX.  Note the y starts at 1 as these are odd tiles
                MapUtils.recalculateFileAndIndex(leftMap, leftMap.worldMap.get(0).width - 1, y + 1, true);
                //Case 3: Original left's XMAX
                MapUtils.recalculateFileAndIndex(leftMap, leftMapWidth - 1, y + 1, true);
                //Case 4: Original left's XMAX + 1.  This is an even tile
                MapUtils.recalculateFileAndIndex(leftMap, leftMapWidth, y, true);
            }
        }
    }
}
