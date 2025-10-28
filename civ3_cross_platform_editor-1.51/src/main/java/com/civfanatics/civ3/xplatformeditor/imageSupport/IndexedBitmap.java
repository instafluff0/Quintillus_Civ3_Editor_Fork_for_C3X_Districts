/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.xplatformeditor.imageSupport;

import java.awt.Color;

/**
 *
 * @author Andrew
 */
public class IndexedBitmap extends Bitmap
{
    
    Palette256 palette;
    byte[][]colorIndices;
    
    public IndexedBitmap(int xsize, int ysize, boolean startAtTop)
    {
        super(xsize, ysize, startAtTop);
        colorIndices = new byte[ysize][xsize];
    }
    
    public IndexedBitmap(int xsize, int ysize)
    {
        super(xsize, ysize, true);
        colorIndices = new byte[ysize][xsize];
    }
    
        /**
     * Add one (or more) pixels of the same color to the image.
     * The colors will be added left to right,
     * and then top to bottom.
     * This method will automatically handle
     * the ends of rows.
     * @param repeats - The number of pixels to be added.
     * @param color - The color of those pixels.
     * @return - How many pixels were added (should equal repeats).
     */
    public int addRepeats(int repeats, int index)
    {
        int count = 0;
        int rgb = palette.getColor((byte)index).getRGB();
        byte idx = (byte)index;
        for (int i = 0; i < repeats; i++)
        {
            map[yindex][xindex] = rgb;
            colorIndices[yindex][xindex] = idx;
            xindex++;
            if (xindex >= xsize)
            {
                goToNextScanLine();
            }
            count++;
        }
        return count;
    }
    
    public void add(int index)
    {
        map[yindex][xindex] = palette.getColor((byte)index).getRGB();
        colorIndices[yindex][xindex] = (byte)index;
        xindex++;
        if (xindex >= xsize) //always do so if condition met
        {
            goToNextScanLine();
        }
    }
    
    public byte getPixelIndex(int x, int y)
    {
        return colorIndices[y][x];
    }
}
