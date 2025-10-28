/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.xplatformeditor.tabs.map;

import com.civfanatics.civ3.biqFile.CITY;
import com.civfanatics.civ3.biqFile.CLNY;
import com.civfanatics.civ3.biqFile.IO;
import com.civfanatics.civ3.biqFile.SLOC;
import com.civfanatics.civ3.biqFile.TERR;
import com.civfanatics.civ3.biqFile.TILE;
import com.civfanatics.civ3.biqFile.UNIT;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

/**
 *
 * @author Andrew
 */
public class MapFlipper
{
    static Logger logger = Logger.getLogger(MapFlipper.class);
    
    public MapFlipper(File in, File out)
    {
        IO biq = new IO();
        try {
            biq.inputBIQ(in);
        }
        catch(FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "The specified file (" + in.getPath() +  ") could not be found. \nCheck if it has been moved or deleted.");
            return;
        }
        
        biq = flip(biq);
       
        boolean successfulExport = biq.outputBIQ(out);
        
    }
     
    public static IO flip(IO biq)
    {
        List<TILE>newTileOrder = new LinkedList<TILE>();
        int maxWidth = biq.worldMap.get(0).width - 1;
        int maxHeight = biq.worldMap.get(0).height - 1;
        for (int i = biq.tile.size() - 1; i > -1; i--)
        {
            TILE tileToFlip = biq.tile.get(i);
            tileToFlip.index = (biq.tile.size() - i) - 1;
            tileToFlip.tileID = (biq.tile.size() - i) - 1;
            tileToFlip.xPos = maxWidth - tileToFlip.xPos;
            tileToFlip.yPos = maxHeight - tileToFlip.yPos;
            tileToFlip.citiesWithInfluence.clear();
            tileToFlip.owner = 0;
            tileToFlip.ownerType = 0;
            newTileOrder.add(tileToFlip);
        }
        
        logger.info("Tile order reversed");
        
        //Set the new tile.  Keep the pointer pointing to the same place so that existing references aren't broken
        biq.tile.clear();
        biq.tile.addAll(newTileOrder);
        
        //recalculate files and index stuffs
        //note that since we're isometric we need some fancy indices, so we use a ternary operator.
        for (int j = 0; j < biq.worldMap.get(0).height; j++)
        {
            for (int i = (j%2==0 ?  0 : 1); i < biq.worldMap.get(0).width; i+=2)
            {
                MapUtils.recalculateFileAndIndex(biq, i, j);
            }
        }
        
        logger.info("Tile file and index recalculated");
        
        //recalculate SLOCs
        for (int i = 0; i < biq.startingLocation.size(); i++)
        {
            SLOC sloc = biq.startingLocation.get(i);
            sloc.setX((biq.worldMap.get(0).width - 1) - sloc.getX()); //note that on a 100-width map, 99 is teh maximum
            sloc.setY((biq.worldMap.get(0).height - 1) - sloc.getY());
        }
        
        logger.info("SLOCs recalculated");
        
        //flip rivers
        for (int i = 0; i < biq.tile.size(); i++)
        {
            byte rci = biq.tile.get(i).getRiverConnectionInfo();
            byte newInfo = (byte)((rci & 0xFF) >>> 4);  //We have to include 0xFF b/c Java is an idiot and sign-extends the byte to an int before moving it.  
            byte otherHalf = (byte)(rci << 4);
            newInfo = (byte)(newInfo | otherHalf);
            biq.tile.get(i).setRiverConnectionInfo(newInfo);
            
            
            //flip river crossing data
            byte rcd = biq.tile.get(i).getRiverCrossingData();
            newInfo = (byte)((rcd & 0xFF) >>> 4);
            otherHalf = (byte) (rcd << 4);
            newInfo = (byte)(newInfo | otherHalf);
            biq.tile.get(i).setRiverCrossingData(newInfo);
            
            //flip the river portion of c3c bonuses
            //note that it's only one NIBBLE that we need to flip
            //ex. we have 0b0010 0100 for LM terrain with a source river in the east
            //desire 0b0010 0010 in the end (i.e. we MIRROR the last nibble.  Go.  Figure.)
            int c3cbonuses = biq.tile.get(i).C3CBonuses;
            byte riverSourceInfo = (byte) ((c3cbonuses >>> 8) & 0x0F);  //in ex., now have 0b0000 0100
            byte otherNibble = (byte) ((c3cbonuses >>> 8) & 0xF0);
            byte newRiverSourceInfo = 0;
            //Mirror the nibble!
            if ((byte)(riverSourceInfo & 0x08) == 0x08)
            {
                //flip south to north
                newRiverSourceInfo = (byte)(newRiverSourceInfo | 0x01);
            }
            if ((byte)(riverSourceInfo & 0x01) == 0x01)
            {
                //flip north to sorth
                newRiverSourceInfo = (byte)(newRiverSourceInfo | 0x08);
            }
            if ((byte)(riverSourceInfo & 0x04) == 0x04)
            {
                //flip east to west
                newRiverSourceInfo = (byte)(newRiverSourceInfo | 0x02);
            }
            if ((byte)(riverSourceInfo & 0x02) == 0x02)
            {
                //flip west to east
                newRiverSourceInfo = (byte)(newRiverSourceInfo | 0x04);
            }
            //Now reconstruct
            int mask = 0xFFFF00FF;
            c3cbonuses = c3cbonuses & mask;
            byte newByte = (byte)(otherNibble | newRiverSourceInfo);
            int secondByte = newByte << 8;
            c3cbonuses = c3cbonuses | secondByte;
            biq.tile.get(i).C3CBonuses = c3cbonuses;
        }
        
        //flip cities
        for (int i = 0; i < biq.city.size(); i++)
        {
            CITY city = biq.city.get(i);
            city.setX((biq.worldMap.get(0).width - 1) - city.getX()); //note that on a 100-width map, 99 is teh maximum
            city.setY((biq.worldMap.get(0).height - 1) - city.getY());
        }
        
        //flip colonies
        for (int i = 0; i < biq.colony.size(); i++)
        {
            CLNY colony = biq.colony.get(i);
            colony.setX((biq.worldMap.get(0).width - 1) - colony.getX()); //note that on a 100-width map, 99 is teh maximum
            colony.setY((biq.worldMap.get(0).height - 1) - colony.getY());
        }
        
        //flip unit
        for (int i = 0; i < biq.mapUnit.size(); i++)
        {
            UNIT unit = biq.mapUnit.get(i);
            unit.setX((biq.worldMap.get(0).width - 1) - unit.getX()); //note that on a 100-width map, 99 is teh maximum
            unit.setY((biq.worldMap.get(0).height - 1) - unit.getY());
        }
        
        //calculate tile ownership
        biq.calculateTileOwners();
        
        logger.info("Rivers  recalculated");
        
        return biq;
    }
        
    public static void flipMap(File in, File out)
    {
        new MapFlipper(in, out);
    }
}
