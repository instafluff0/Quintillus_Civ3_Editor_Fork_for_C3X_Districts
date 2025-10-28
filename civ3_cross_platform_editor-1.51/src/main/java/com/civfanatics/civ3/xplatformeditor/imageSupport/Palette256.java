/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.xplatformeditor.imageSupport;

import java.awt.Color;
import org.apache.log4j.Logger;

/**
 *
 * @author Andrew
 */

/**
    * Class that represents the palette for a 256-color PCX file (alternately
    * called a VGA 256 Color Palette).
    */
public class Palette256{
    
    static Logger logger = Logger.getLogger("Palette256");
    String newline = "\n";
    
    Color[]palette;
    short paletteSize;
    /**
        * Creates a new PCX palette, with 256 colors.
        */
    public Palette256()
    {
        palette = new Color[256];
        paletteSize = 0;
    }
    /**
        * Adds a color to the PCX's palette.
        * @param red - The red value of the new color (unsigned byte).
        * @param green - The green value of the new color (unsigned byte).
        * @param blue - The blue value of the new color (unsigned byte).
        * @return True if the color can be added, false if there are already
        * 256 colors.
        */
    public boolean addColor(byte red, byte green, byte blue)
    {
        if (paletteSize >= 256)
            return false;   //cannot add a 257th color
        //else, we need to create a color
        int sRed, sGreen, sBlue;
        sRed = red < 0 ? red + 256 : red;
        sGreen = green < 0 ? green + 256 : green;
        sBlue = blue < 0 ? blue + 256 : blue;
        if (logger.isTraceEnabled())
            logger.trace("sRed: " + sRed + " sGreen: " + sGreen + " sBlue: " + sBlue);
        palette[paletteSize] = new Color(sRed, sGreen, sBlue);
        paletteSize++;
        return true;
    }
    
    
        /**
         * Changes the color that was at position <i>index</i> in the palette to
         * a new color, <i>color</i>.
         * @param index - The index in the palette of the color to be replaced.
         * @param color - The new color.
         * @return The previous color.
         */
        public Color setColor(int index, Color color)
        {
            Color oldColor = palette[index];
            palette[index] = color;
            return oldColor;
            //Cannot update the bitmap from here
        }

    /**
        * Makes a certain color in the palette transparent.
        * TODO: Ensure that this actually works.  Going by setColor, it looks
        * like we'll have to manually change all the pointers in the bitmap
        * array to point to the new color.  It'd be nice if they just pointed to
        * the place in the palette rather than at the actual color.
        * @param index - The color that is to become transparent.
        * @return - True if the index was valid, false otherwise
        */
    public boolean makeTransparent(int index)
    {
        if (index > 255)
            return false;
        Color currentColor = palette[index];
        Color newColor = new Color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), 0);
        palette[index] = newColor;
        return true;
    }

    /**
        * Returns the color of a color within the PCX's palette.
        * @param index - The index within the palette (unsigned byte).
        * @return - The color corresponding to that index
        */
    public Color getColor(byte index)
    {
        int sIndex = index & 0xFF;
        return palette[sIndex];
    }

    //@Override
    public String toString()
    {
        StringBuffer toRtn = new java.lang.StringBuffer();
        for (int i = 0; i < paletteSize; i++)
        {
            toRtn.append(palette[i].toString() + newline);
        }
        return toRtn.toString();
    }

}