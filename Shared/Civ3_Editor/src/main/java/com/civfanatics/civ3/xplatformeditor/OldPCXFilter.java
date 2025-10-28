package com.civfanatics.civ3.xplatformeditor;

import com.civfanatics.civ3.biqFile.util.LittleEndianDataInputStream;
import com.civfanatics.civ3.biqFile.util.LittleEndianDataOutputStream;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Although Java does not support unsigned data natively, we are going to
 * assume byte means "unsigned byte" and short means "unsigned short" unless
 * otherwise stated.  This requires a few extra acrobatics in Java, but keeps
 * memory overhead minimal.
 *
 * Other options for dealing with this are bumping bytes up to shorts and shorts
 * to ints, or creating a ubyte and ushort Object.  The former uses extra memory;
 * the latter also does and creates several layers of indirection.  Versions
 * with such implementations may later be done and benchmarks run to determine
 * the difference in performance.
 *
 * http://www.qzx.com/pc-gpe/pcx.txt was used as a primary source for the format.
 * Note that they refer to what we here call ushorts and ints.  Nowadays most
 * ints are 4 bytes, not 2, so we use shorts here for what was an "int" in 1991.
 *
 * This class does not support EGA (16 color), 24-bit, or CGA .PCX files.
 *
 * PB = PC Paintbrush, by ZSoft Corporation.  Version numbers assumed to be
 * for PC Paintbrush for DOS.
 * 
 * Originally created on April 15, 2010
 *
 * @author Andrew Jones
 */
public class OldPCXFilter {

    static final boolean printPalette = false;
    static final boolean printBitmap = false;
    boolean civ3TransparencyEnabled = true;
    BufferedImage thisBufferedImage;
    Boolean[]rowEnds;

    static Logger logger = Logger.getLogger("pcxFilter");

    String newline = "\n";

    boolean printDiagnostics = false;

    File pcxFile;
    byte[]buffer;
    public List<Short>transparents;

    /**
     * Should be 10, to indicate ZSoft.
     */
    byte manufacturer;
    /**
        //0 = version 2.5 of PC Paintbruch
        //2 = version 2.8 w/palette information
        //3 = version 2.8 w/o palette information.
        //4 = PC Paintbrush from Windows
        //5 = Version 3.0 and > of PC Paintbrush, includes 24-bit PCX files
        //Note Paintbrush in Windows 3.11 uses version 3 (AJ)
     */
    byte version;
    byte encoding; //1 = .PCX run length encoding
    byte bitsPerPixel;  //per plane; options are 1, 2, 4, and 8
    //next 4 shorts compose "Window"
    //note they also specify width and height
    //XSize = xmax - xmin + 1, YSize = ymax - ymin + 1
    short xmin;
    short ymin;
    short xmax;
    short ymax;
    //resolutions are in DPI - the resolutiosn the image was composed at
    short horizontalResolution;
    short verticalResolution;
    byte[] EGA_Colormap = new byte[48];  //48
    byte reserved;  //should be zero
    byte numColorPlanes;
    /**
     *  bytes to allocate for a scanline plane (that is, each row).
     *  MUST be even.  Do NOT caculcate from xmax-xmin.
     *  Note that this means if our image is 101x100, we must have 102 bytes per
     *  line, not 101.  Only the first 101 will be visible, however.
     */
    short bytesPerLine; 
    short paletteInfo;  //1=color or BW, 2 = grayscale.  Ignored in PB IV+
    short hScreenSize;  //horizontal screen size.  only in PB IV+
    short vScreenSize;  ///vertical screen size.  only in PB IV+
    //end binary format
    short xsize;
    short ysize;
    Bitmap bitmap;

    public Palette256 palette;

    /**
     * Constructor takes a String representation of the PCX file location.
     * @param string - A String representing the PCX file location.
     */
    public OldPCXFilter(String string)
    {
        //logger.setLevel(Level.INFO);
        pcxFile = new File(string);
        transparents = new ArrayList<Short>();
    }

    /**
     * Reads the PCX file into the buffer.  Does not actually do any processing.
     */
    public void readFile()
    {
        LittleEndianDataInputStream inFile = null;
        try{
            inFile = new LittleEndianDataInputStream(new BufferedInputStream(new FileInputStream(pcxFile)));
            buffer = new byte[(int)pcxFile.length()];
            inFile.readFully(buffer);
        }
        catch(java.io.IOException e)
        {
            logger.error(e);
        }
        finally
        {
            try{
                inFile.close();
            }
            catch(java.io.IOException e){
                logger.error(e);
            }
        }
    }

    /**
     * Parses the PCX that has been read into the buffer.
     */
    public void parse()
    {
        LittleEndianDataInputStream in = new LittleEndianDataInputStream(new ByteArrayInputStream(buffer));
        try{
            //PCX files use little-endian formatting.  Java, inconveniently, uses
            //big-endian formatting.  Thus we must reverse the bytes for shorts
            manufacturer = in.readByte();
            version = in.readByte();
            encoding = in.readByte();
            bitsPerPixel = in.readByte();
            xmin = in.readShort();
            ymin = in.readShort();
            xmax = in.readShort();
            ymax = in.readShort();
            xsize = (short)(uminus(xmax, xmin) + 1);
            ysize = (short)(uminus(ymax, ymin) + 1);
            horizontalResolution = in.readShort();
            verticalResolution = in.readShort();
            //EGA Colormap - support to be added later
            //7-12: Do input it so we can export it later without any changes
            in.read(EGA_Colormap, 0, 48);
            reserved = in.readByte();
            numColorPlanes = in.readByte();
            bytesPerLine = in.readShort();
            paletteInfo = in.readShort();
            hScreenSize = in.readShort();
            vScreenSize = in.readShort();
            //filler - should be all zero
            if (printDiagnostics)
            {
                logger.info("manufacturer: " +  manufacturer);
                logger.info("version: " +  version);
                logger.info("encoding: " +  encoding);
                logger.info("bits per pixel: " +  bitsPerPixel);
                logger.info("xmin: " +  xmin);
                logger.info("ymin: " +  ymin);
                logger.info("xmax: " +  xmax);
                logger.info("ymax: " +  ymax);
                logger.info("xsize: " +  xsize);
                logger.info("ysize: " +  ysize);

            }
            in.skip(54);
            in.mark(buffer.length); //set the mark here, don't let it expire
            if (version == 5)
            {
                //skip the data portion for now - jump to the palette
                in.skip(buffer.length - 128 - 769);
                byte check = in.readByte();
                if (check == 12)
                {
                    palette = new Palette256();
                    for (int i = 0; i < 256; i++)
                    {
                        byte red = in.readByte();
                        byte green = in.readByte();
                        byte blue = in.readByte();
                        palette.addColor(red, green, blue);
                    }
                }
                else
                {
                    logger.error("Incorrect byte prior to expected 256 color palette");
                    logger.error("Byte is " + toString(check));
                    logger.error("Jumped " + (buffer.length - 128 - 769) + " bytes after header");
                }
                //now we have to actually, like, read in the data
                //send the reader back to the end of the header
                in.reset();
                int pixelCount = 0;
                int numPixelsFromRepeats = 0;
                //allocate bitmap
                bitmap = new Bitmap(bytesPerLine, ysize);
                rowEnds = new Boolean[this.ysize];
                while (pixelCount < bytesPerLine * ysize)
                {
                    byte temp = in.readByte();
                    int stemp = (temp & 0xFF);
                    if (stemp >= 192)
                    {
                        int repeats = stemp & 0x3F;
                        if (logger.isTraceEnabled())
                        {
                            logger.trace("Processed " + pixelCount + " pixels.");
                            logger.trace("To repeat " + repeats + " times.");
                        }
                        byte color = in.readByte();
                        repeats = bitmap.addRepeats(repeats, palette.getColor(color), color);
                        pixelCount+=repeats;
                        numPixelsFromRepeats+=repeats;
                    }
                    else
                    {
                        bitmap.add(palette.getColor(temp), temp);
                        pixelCount++;
                    }
                    if (pixelCount%bytesPerLine == 0)
                    {
                        rowEnds[pixelCount/bytesPerLine - 1] = true;
                    }
                    if (logger.isTraceEnabled())
                        logger.trace("Processed " + pixelCount + " pixels.");
                }
                if (printDiagnostics)
                {
                    logger.info("Processed " + pixelCount + " pixels");
                    logger.info(numPixelsFromRepeats + " pixels were from repeats");
                }
                if (logger.isDebugEnabled()){
                    for (int i = 0; i < rowEnds.length; i++)
                        logger.info(i + ": " + rowEnds[i]);
                }
                if (logger.isDebugEnabled())
                    logger.debug(this.toString());
            }
        }
        catch(java.io.IOException e)
        {
            logger.error("Error caught: ", e);
        }
    }
    
    /**
     * Exports a PCX based on the current info stored in the PCXFilter.
     * This is not yet complete, it will not work for many PCXs.
     * @param filename 
     */
    public void exportPCX(String filename)
    {
        File file = new File(filename);
        try
        {
            LittleEndianDataOutputStream dos = new LittleEndianDataOutputStream(new FileOutputStream(file));
            dos.writeByte(this.manufacturer);
            dos.writeByte(this.version);
            dos.writeByte(this.encoding);
            dos.writeByte(this.bitsPerPixel);
            dos.writeShort(this.xmin);
            dos.writeShort(this.ymin);
            dos.writeShort(xmax);
            dos.writeShort(ymax);
            dos.writeShort(horizontalResolution);
            dos.writeShort(verticalResolution);
            dos.write(EGA_Colormap, 0, 48);
            dos.writeByte(reserved);
            dos.writeByte(numColorPlanes);
            dos.writeShort(bytesPerLine);
            dos.writeShort(paletteInfo);
            dos.writeShort(hScreenSize);
            dos.writeShort(vScreenSize);
            for (int i = 0; i < 54; i++)
            {   //54 blanks in the format
                dos.writeByte((byte)0);
            }
            //end header
            //Bitmap
            
            for (int y = 0; y < ysize; y++)
            {
                for (int x = 0; x < xsize; x++)
                {
                    int color = bitmap.colorInts.get(bitmap.xsize * y + x);
                    short repeats = 0;
                    boolean repeat = false;
                    do
                    {
                        int newColor = bitmap.colorInts.get(bitmap.xsize * y + x + repeats + 1);
                        if (newColor == color)
                        {
                            repeats++;
                            repeat = true;
                        }
                    }while(repeat && repeats < 192);
                    //remember that there must be an even # per scanline
                    if (repeat)
                    {
                        ;
                    }
                    else  //TODO: handle #'s greater than 192 with a repeat of 1
                    {
                        dos.writeByte(bitmap.colorInts.get(bitmap.xsize * y + x));
                    }
                }
                //add a line finisher
                if (xsize % 1 == 0)
                {
                    dos.writeByte(0xC1); //we want unsigned!  this had better work!
                    dos.writeByte(0xFF);
                }
            }
            dos.writeByte(0x0C);  //version # for 256-color palette
            //Palette exporting
            for (int i = 0; i < palette.paletteSize; i++)
            {
                //This might blow up since we're using ints, or it might work
                dos.writeByte(palette.palette[i].getRed());
                dos.writeByte(palette.palette[i].getGreen());
                dos.writeByte(palette.palette[i].getBlue());
            }
            dos.close();
        }
        catch(FileNotFoundException e)
        {
            logger.error("File not found", e);
        }
        catch(IOException e)
        {
            logger.error("IOException", e);
        }
    }

    /**
     * Creates a BufferedImage from the PCX image.
     */
    public void createBufferedImage()
    {
        //type 1 is TYPE_INT_RGB
        thisBufferedImage = new BufferedImage(this.xsize, this.ysize, BufferedImage.TYPE_INT_ARGB);
        int[]rgbArray = new int[this.xsize * this.ysize];
        for (int y = 0; y < ysize; y++)
        {
            for (int x = 0; x < xsize; x++)
            {
                rgbArray[y*xsize + x] = bitmap.getPixelRGB(x, y);
            }
        }
        //setRGB
        thisBufferedImage.setRGB(0, 0, xsize, ysize, rgbArray, 0, xsize);
    }

    /**
     * Returns the subimage of this PCX image.
     * This method does <b>not</b> do bounds-checking to make sure the subimage
     * does not go off the edge of this image.
     * @param xStart - The x coordinate (in this image) of the leftmost part of the subimage.
     * @param yStart - The y coordinate (in this image) of the uppermost part of the subimage.
     * @param width - The width of the subimage.
     * @param height - The height of the subimage.
     * @return - The subimage, as a BufferedImage.
     */
    public BufferedImage getSubimage(int xStart, int yStart, int width, int height)
    {
        BufferedImage subimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int[]rgbArray = new int[width*height];
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                rgbArray[y*width + x] = bitmap.getPixelRGB((x+xStart), (y+yStart));
            }
        }
        subimage.setRGB(0, 0, width, height, rgbArray, 0, width);
        return subimage;
    }

    /**
     * Returns the PCX image as a BufferedImage.  Note that the BufferedImage
     * must already have been created.
     * @return The PCX image as a BufferedImage
     */
    public BufferedImage getBufferedImage()
    {
        return thisBufferedImage;
    }

    @Override
    /**
     * @return - A string representation of the PCX file, including metadata.
     */
    public String toString()
    {
        StringBuilder toStr = new java.lang.StringBuilder(1000);
        toStr.append("Manufacturer: " + toString(manufacturer) + newline);
        toStr.append("Version: " + toString(version) + newline);
        toStr.append("Encoding: " + toString(encoding) + newline);
        toStr.append("BitsPerPixel: " + toString(bitsPerPixel) + newline);
        toStr.append("Xmin: " + toString(xmin) + newline);
        toStr.append("Ymin: " + toString(ymin) + newline);
        toStr.append("Xmax: " + toString(xmax) + newline);
        toStr.append("Ymax: " + toString(ymax) + newline);
        toStr.append("XSize: " + toString(xsize) + newline);
        toStr.append("YSize: " + toString(ysize) + newline);
        toStr.append("horizontalResolution: " + toString(horizontalResolution) + newline);
        toStr.append("verticalResolution: " + toString(verticalResolution) + newline);
        toStr.append("numColorPlanes: " + toString(numColorPlanes) + newline);
        toStr.append("bytesPerScanLine: " + toString(bytesPerLine) + newline);
        toStr.append("paletteInfo: " + toString(paletteInfo) + newline);
        toStr.append("hScreenSize: " + toString(hScreenSize) + newline);
        toStr.append("vScreenSize: " + toString(vScreenSize) + newline);
        if (printPalette)
        {
            toStr.append(palette.toString());
        }
        if (printBitmap)
        {
            toStr.append(bitmap.toString());
        }
        return toStr.toString();
    }

    /**
     * Performs unsigned subtraction on two unsigned ints.
     * ex: one = 3, two = 1.  uminus(one, two) returns 2.
     *
     * This method depends on the fact that the larger of two unsigned ints
     * that don't fit into a signed ints will still be larger even when
     * interpreted as a signed ints.
     *
     * Also note that this performs saturating arithemetic.
     *
     * @param one - The first number.
     * @param two - The second number.
     * @return The difference of the two numbers, or zero if the difference is
     * negative.
     */
    private int uminus(int one, int two)
    {
        if (two > one)  //underflow
            return 0;
        long oneI, twoI, aI;
        oneI = 0xFFFFFFFFL & one;
        twoI = 0xFFFFFFFFL & two;
        aI = oneI - twoI;//(short(aI - 65536))
        int toReturn;
        toReturn = aI > java.lang.Integer.MAX_VALUE ? (int)(aI - 4294967296L) : (int)aI;
        return toReturn;
    }

    /**
     * Performs unsigned subtraction on two unsigned shorts.
     * ex: one = 3, two = 1.  uminus(one, two) returns 2.
     *
     * This method depends on the fact that the larger of two unsigned shorts
     * that don't fit into a signed shorts will still be larger even when
     * interpreted as a signed shorts.
     *
     * Also note that this performs saturating arithemetic.
     *
     * @param one - The first number.
     * @param two - The second number.
     * @return The difference of the two numbers, or zero if the difference is
     * negative.
     */
    private short uminus(short one, short two)
    {
        if (two > one)  //underflow
            return 0;
        int oneI, twoI, aI;
        oneI = one & 0xFFFF;
        twoI = two & 0xFFFF;
        aI = oneI - twoI;//(short(aI - 65536))
        short toReturn;
        toReturn = aI > java.lang.Short.MAX_VALUE ? (short)(aI - 65536) : (short)aI;
        return toReturn;
    }
    /**
     * Performs unsigned subtraction on two unsigned bytes.
     * ex: one = 3, two = 1.  uminus(one, two) returns 2.
     *
     * This method depends on the fact that the larger of two unsigned bytes
     * that don't fit into a signed byte will still be larger even when
     * interpreted as a signed byte (ex. 200 > 150 when unsigned, and the
     * same bytes imply -56 > -106 when signed).
     *
     * Also note that this performs saturating arithemetic.
     *
     * @param one - The first number.
     * @param two - The second number.
     * @return The difference of the two numbers, or zero if the difference is
     * negative.
     */
    private byte uminus(byte one, byte two)
    {
        if (two > one)  //underflow
            return 0;
        int oneI, twoI, aI;
        oneI = one & 0xFF;
        twoI = two & 0xFF;
        aI = oneI - twoI;//(short(aI - 65536))
        byte toReturn;
        toReturn = aI > java.lang.Byte.MAX_VALUE ? (byte)(aI - 256) : (byte)aI;
        return toReturn;
    }
    /**
     * Provides proper to string method for unsigned ints.
     * @param uint - The unsigned int.
     * @return - A string representation of the unsigned int.
     */
    private static String toString(int uint)
    {
        if (uint >= 0)
            return Integer.toString(uint);
        else
            return Long.toString(uint + 4294967296L);
    }
    /**
     * Provides proper to string method for unsigned shorts.
     * @param ushort - The unsigned short.
     * @return - A string representation of the unsigned short.
     */
    private static String toString(short ushort)
    {
        if (ushort >= 0)
            return Short.toString(ushort);
        else
            return Integer.toString(ushort + 65536);
    }
    /**
     * Provides proper to string method for unsigned bytes.
     * @param ubyte - The unsigned byte.
     * @return - A string representation of the unsigned byte.
     */
    private static String toString(byte ubyte)
    {
        if (ubyte >= 0)
            return Byte.toString(ubyte);
        else
            return Short.toString((short)(ubyte + 256));
    }

    /**
     * Class that represents the palette for a 256-color PCX file (alternately
     * called a VGA 256 Color Palette).
     */
    public class Palette256{
        public Color[]palette;
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
            if ((civ3TransparencyEnabled &&(paletteSize == 254 || paletteSize == 255)) || transparents.contains(paletteSize))    //it's Civ3 transparent!
            {
                if (logger.isDebugEnabled())
                    logger.debug("color " + paletteSize + " is transparent");
                palette[paletteSize] = new Color(sRed, sGreen, sBlue, 0);
                paletteSize++;
                return true;
            }
            palette[paletteSize] = new Color(sRed, sGreen, sBlue);
            paletteSize++;
            return true;
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
         * Changes the color that was at position <i>index</i> in the palette to
         * a new color, <i>color</i>.
         * @param index - The index in the palette of the color to be replaced.
         * @param color - The new color.
         */
        public void setColor(int index, Color color)
        {
            Color oldColor = palette[index];
            palette[index] = color;
            for (int x = 0; x < bitmap.xsize; x++)
            {
                for (int y = 0; y < bitmap.ysize; y++)
                {
                    if (bitmap.map[x][y].equals(oldColor))
                        bitmap.map[x][y] = color;
                }
            }
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

        @Override
        public String toString()
        {
            StringBuilder toRtn = new java.lang.StringBuilder();
            for (int i = 0; i < paletteSize; i++)
            {
                toRtn.append(palette[i].toString() + newline);
            }
            return toRtn.toString();
        }

    }

    /**
     * This class represents the PCX as a traditional bitmap - an array of
     * colors.
     */
    public static class Bitmap
    {
        Color[][]map;
        //optimize later, i'm tired
        List<Integer>colorInts = new LinkedList<Integer>();
        int xsize;
        int ysize;
        int xindex;
        int yindex;

        /**
         * The constructor for the new image.
         * @param xsize - The horizontal size of the image.
         * @param ysize - The vertical size of the image.
         */
        public Bitmap(int xsize, int ysize)
        {
            this.xsize = xsize;
            this.ysize = ysize;
            xindex = 0;
            yindex = 0;
            if (logger.isDebugEnabled())
                logger.debug("New bitmap with size " + xsize + " by " + ysize);
            map = new Color[xsize][ysize];
        }

        /**
         * Adds a pixel to the image.  The colors must be added left to right,
         * and then top to bottom.  This method will automatically handle
         * carriage return/line feeds.
         * @param color - The color of the new pixel to be added.
         */
        public void add(Color color, int index)
        {
            map[xindex][yindex] = color;
            colorInts.add(index);
            xindex++;
            if (xindex >= xsize)
            {
                xindex = 0;
                yindex++;
            }
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
        public int addRepeats(int repeats, Color color, int index)
        {
            int count = 0;
            for (int i = 0; i < repeats; i++)
            {
                map[xindex][yindex] = color;
                xindex++;
                if (xindex >= xsize)
                {
                    xindex = 0;
                    yindex++;
                }
                colorInts.add(index);
                count++;
            }
            return count;
        }

        @Override
        /**
         * Returns the PCX image as a String, with nice array formatting
         * @return - A text representation of the PCX image as an array.
         */
        public String toString()
        {
            StringBuilder str = new java.lang.StringBuilder();
            for (int i = 0; i < xsize; i++)
            {
                str.append("[");
                for (int j = 0; j < ysize; j++)
                {
                    str.append("[");
                    str.append(map[i][j].toString());
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
        public Color[][] getMap()
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
            return map[x][y];
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
            return map[x][y].getRGB();
        }
    }
}
