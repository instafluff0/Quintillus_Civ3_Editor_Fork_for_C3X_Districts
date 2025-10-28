/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.xplatformeditor.imageSupport;

import com.civfanatics.civ3.biqFile.util.LittleEndianDataInputStream;
import java.awt.Color;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Andrew
 */
public class Civ3PCXFilter extends PCXFilter
{
    private String fileName = "";
    //TODO: Add functionality that is Civ3-specific, in a friendly fashion
    public boolean civ3TransparencyEnabled = true;
    public List<Short>transparents;
    
    public Civ3PCXFilter(String string)
    {
        super(string);
        this.fileName = string;
        transparents = new LinkedList<Short>();  //Todo: Do I want array or linked, or do I care?
        transparents.add(Short.parseShort("254"));
        transparents.add(Short.parseShort("255"));
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void resetTransparents()
    {
        transparents = new LinkedList<Short>();  //Todo: Do I want array or linked, or do I care?
    }
    
    public void addTransparent(Short num)
    {
        transparents.add(num);
    }
    
    protected void importPalette(LittleEndianDataInputStream in)
    {
        try
        {
            palette = new Palette256();
            for (int i = 0; i < 256; i++)
            {
                byte red = in.readByte();
                byte green = in.readByte();
                byte blue = in.readByte();
                palette.addColor(red, green, blue);
            }
            for (int i =0; i < transparents.size(); i++)
            {
                palette.setColor(transparents.get(i), new Color(0, 0, 0, 0));
            }
        }
        catch(IOException e)
        {
            logger.error("IOException", e);
        }
    }
    
    public void setColor(int paletteIndex, Color color)
    {
        Color oldColor = palette.getColor((byte)(0xFF & paletteIndex));
        palette.setColor(paletteIndex, color);
        for (int y = 0; y < bitmap.ysize; y++)
        {
            for (int x = 0; x < bitmap.xsize; x++)
            {
                if (bitmap.map[y][x] == (oldColor.getRGB()))
                    bitmap.map[y][x] = color.getRGB();
            }
        }
    }
}
