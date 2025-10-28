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

/**
 * This class represents the PCX as a traditional bitmap - an array of
 * colors.
 */
public class Bitmap
{
    int[][]map;
    int xsize;
    int ysize;
    int xindex;
    int yindex;
    int yStep;

    public Bitmap(int xsize, int ysize, boolean startAtTop)
    {
        if (startAtTop)
        {
            yindex = 0;
            yStep = 1;
        }
        else
        {
            yindex = ysize - 1;
            yStep = -1;
        }
        this.xsize = xsize;
        this.ysize = ysize;
        xindex = 0;
        //System.out.println("ysize: " + ysize);
        map = new int[ysize][xsize];
    }

    /**
     * The constructor for the new image.
     * @param xsize - The horizontal size of the image.
     * @param ysize - The vertical size of the image.
     */
    public Bitmap(int xsize, int ysize)
    {
        this(xsize, ysize, true);
    }

    
    /**
     * Adds a pixel to the image.  The colors must be added left to right,
     * and then top to bottom (or vice versa if startAtTop is false).
     * This method will automatically handle
     * carriage return/line feeds.
     * @param color - The color of the new pixel to be added.
     */
    public void add(Color color)
    {
        add(color, true);
    }

    private void add(Color color, boolean goToNextLine)
    {
        //map[xindex][yindex] = color.getRGB();
        map[yindex][xindex] = color.getRGB();
        xindex++;
        if (xindex >= xsize && goToNextLine)
        {
            goToNextScanLine();
        }
    }

    /**
     * Goes to the next line, filling any any missing places with black
     * If we're already at the beginning of the line, ignore the request.
     */
    public void goToNextScanLine()
    {
        if (xindex == 0)
            return;
        //System.out.println("xindex; " + xindex + " yindex: " + yindex);
        //System.out.flush();
        for (int x = xindex; x < xsize; x++)
            add(Color.BLACK, false);
        xindex = 0;
        yindex+=yStep;
        //System.out.println("new xindex; " + xindex + " yindex: " + yindex);
        //System.out.flush();
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
    public int addRepeats(int repeats, Color color)
    {
        int count = 0;
        for (int i = 0; i < repeats; i++)
        {
            map[yindex][xindex] = color.getRGB();
            xindex++;
            if (xindex >= xsize)
            {
                goToNextScanLine();
            }
            count++;
        }
        return count;
    }

    //@Override
    /**
     * Returns the PCX image as a String, with nice array formatting
     * @return - A text representation of the PCX image as an array.
     */
    public String toString()
    {
        StringBuffer str = new java.lang.StringBuffer();
        for (int i = 0; i < xsize; i++)
        {
            str.append("[");
            for (int j = 0; j < ysize; j++)
            {
                str.append("[");
                str.append(map[j][i]);
                str.append("]");
            }
            str.append("]\n");
        }
        return str.toString();
    }
    /**
     * Returns the entire PCX image as a 2-D array of Colors.
     * @return - The entire PCX image as a 2-D array of Colors
     */
    public int[][] getMap()
    {
        return map;
    }

    /**
     * Returns the color of a requested pixel.
     * @param x - The x (horizontal) position of the requested pixel.
     * @param y - The y (vertical) position of the requested pixel.
     * @return - The Color of the requested pixel.
     */
    public Color getPixel(int x, int y)
    {
        return new Color(map[y][x]);
    }

    public int getPixelRed(int x, int y)
    {
        return new Color(map[y][x]).getRed();
    }

    public int getPixelGreen(int x, int y)
    {
        return new Color(map[y][x]).getGreen();
    }

    public int getPixelBlue(int x, int y)
    {
        return new Color(map[y][x]).getBlue();
    }

    /**
     * Returns the (integer) RGB value representing the color in the default sRGB
     * color model for a given pixel in the PCX file.
     *
     * Bits 24-31 are alpha, 16-23 are red, 8-15 are green, 0-7 are
     * blue.
     * @param x - The x (horizontal) position of the requested pixel.
     * @param y - The y (vertical) position of the requested pixel.
     * @return the RGB value of the color in the default sRGB color model for
     * the requested pixel.
     */
    public int getPixelRGB(int x, int y)
    {
        return map[y][x];
    }
}