
package com.civfanatics.civ3.xplatformeditor.imageSupport;

import com.civfanatics.civ3.biqFile.util.LittleEndianDataInputStream;
import com.civfanatics.civ3.biqFile.util.LittleEndianDataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.log4j.Level;
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
 * This class does not support EGA (16 color) or CGA .PCX files.
 *
 * PB = PC Paintbrush, by ZSoft Corporation.  Version numbers assumed to be
 * for PC Paintbrush for DOS.
 *
 * Originally created on April 15, 2010
 * 24-bit PCX support added March 28, 2011
 *
 * @author Andrew Jones
 */
public class PCXFilter implements com.civfanatics.civ3.xplatformeditor.imageSupport.ImageFilter {

    
    static long readTime = 0;
    static long parseTime = 0;
    static long bufferedTime = 0;
    
    static final boolean printPalette = false;
    static final boolean printBitmap = false;
    boolean civ3TransparencyEnabled = false;
    BufferedImage thisBufferedImage;
    boolean[]rowEnds;

    static Logger logger = Logger.getLogger("pcxFilter");

    String newline = "\n";

    boolean printDiagnostics = false;

    File pcxFile;
    byte[]buffer;

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
    public short ysize;  //TODO: resolve publicity
    Bitmap bitmap;

    public Palette256 palette;

    /**
     * Constructor takes a String representation of the PCX file location.
     * @param string - A String representing the PCX file location.
     */
    public PCXFilter(String string)
    {
        logger.setLevel(Level.INFO);
        pcxFile = new File(string);
    }

    public void processFile()
    {
        readFile();
        parse();
        createBufferedImage();
    }

    /**
     * Reads the PCX file into the buffer.  Does not actually do any processing.
     */
    public void readFile()
    {
        long start = System.nanoTime();
        LittleEndianDataInputStream inFile = null;
        try{
            inFile = new LittleEndianDataInputStream(new BufferedInputStream(new FileInputStream(pcxFile)));
            buffer = new byte[(int)pcxFile.length()];
            inFile.readFully(buffer);
        }
        catch(java.io.IOException e)
        {
            logger.error("IO exception while reading", e);
        }
        finally
        {
            try{
                inFile.close();
            }
            catch(java.io.IOException e){
                logger.error("IO exception while closing", e);
            }
        }
        readTime += (System.nanoTime() - start);
    }

    /**
     * Parses the PCX that has been read into the buffer.
     */
    public void parse()
    {
        long start = System.nanoTime();
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
            //EGA Colormap - read it in so we don't overwrite it
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
            if (version == 5 && bitsPerPixel == 8 && this.numColorPlanes == 1)
            {
                import256ColorPCX(in);
            }
            else if (version == 5 && bitsPerPixel == 8 && this.numColorPlanes == 3)
            {
                import24BitPCX(in);
            }
            else if (version == 5 && bitsPerPixel == 1 && this.numColorPlanes == 1)
            {
                import1BitPCX(in);
            }
        }
        catch(java.io.IOException e)
        {
            logger.error("Error caught: ", e);
        }
        parseTime += (System.nanoTime() - start);
    }

    /**
     * Creates a BufferedImage from the PCX image.
     */
    public void createBufferedImage()
    {
        long start = System.nanoTime();
        //type 1 is TYPE_INT_RGB
        thisBufferedImage = new BufferedImage(this.xsize, this.ysize, BufferedImage.TYPE_INT_ARGB);
//        int[]rgbArray = new int[this.xsize * this.ysize];
        for (int y = 0; y < ysize; y++)
        {
//            System.arraycopy(bitmap.getMap()[y], 0, rgbArray, y *xsize , xsize);
//            for (int x = 0; x < xsize; x++)
//            {
//                if (bitmap == null)
//                    System.out.println("like whoa!");
//                rgbArray[y*xsize + x] = bitmap.getPixelRGB(x, y);
//            }
            thisBufferedImage.setRGB(0, y, xsize, 1, bitmap.getMap()[y], 0, xsize);
        }
        //setRGB
//        thisBufferedImage.setRGB(0, 0, xsize, ysize, rgbArray, 0, xsize);
        bufferedTime += (System.nanoTime() - start);
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

    public int getWidth()
    {
        return xsize;
    }
    public int getHeight()
    {
        return ysize;
    }

    //@Override
    /**
     * @return - A string representation of the PCX file, including metadata.
     */
    public String toString()
    {
        StringBuffer toStr = new java.lang.StringBuffer(1000);
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


    protected void import256ColorPCX(LittleEndianDataInputStream in)
    {
        try{
            //skip the data portion for now - jump to the palette
            in.skip(buffer.length - 128 - 769);
            byte check = in.readByte();
            if (check == 12)
            {
                importPalette(in);
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
            bitmap = new IndexedBitmap(bytesPerLine, ysize);
            ((IndexedBitmap)(bitmap)).palette = this.palette;
            rowEnds = new boolean[this.ysize];
            //Can print out bytesPerLine * ysize for debugging
            int maxSize = bytesPerLine * ysize;
            while (pixelCount < maxSize)
            {
                byte temp = in.readByte();
                int stemp = (temp & 0xFF);
                if (stemp >= 192)
                {
                    int repeats = stemp & 0x3F;
                    if (logger.isDebugEnabled())
                    {
                        logger.debug("Processed " + pixelCount + " pixels.");
                        logger.debug("To repeat " + repeats + " times.");
                    }
                    byte color = in.readByte();
                    repeats = ((IndexedBitmap)(bitmap)).addRepeats(repeats, color);
                    pixelCount+=repeats;
                    numPixelsFromRepeats+=repeats;
                }
                else
                {
                    ((IndexedBitmap)(bitmap)).add(temp);
                    pixelCount++;
                }
//                if (pixelCount%bytesPerLine == 0)
//                {
//                    rowEnds[pixelCount/bytesPerLine - 1] = true;
//                }
                if (logger.isDebugEnabled() && pixelCount > 1400000)
                    logger.debug("Processed " + pixelCount + " pixels.");
            }
            if (printDiagnostics)
            {
                //logger.info("Processed " + pixelCount + " pixels");
                //logger.info(numPixelsFromRepeats + " pixels were from repeats");
            }
            if (logger.isDebugEnabled()){
                for (int i = 0; i < rowEnds.length; i++)
                    logger.info(i + ": " + rowEnds[i]);
            }
            if (logger.isDebugEnabled())
                logger.debug(this.toString());
        }
        catch(IOException e)
        {
            logger.error("IOException", e);
        }
    }

    private void import24BitPCX(LittleEndianDataInputStream in)
    {
        try{
            int pixelCount = 0;
            int numPixelsFromRepeats = 0;
            int row = 0;
            //allocate bitmap
            bitmap = new Bitmap(bytesPerLine, ysize);
            rowEnds = new boolean[this.ysize];
            while (pixelCount < bytesPerLine * ysize * 3)
            {
                int xCount = 0;
                int rgbArray = 0;
                Bitmap[]rgbBitmaps = new Bitmap[3];
                rgbBitmaps[0] = new Bitmap(bytesPerLine, 1);
                rgbBitmaps[1] = new Bitmap(bytesPerLine, 1);
                rgbBitmaps[2] = new Bitmap(bytesPerLine, 1);
                while (xCount < bytesPerLine * 3)
                {
                    byte temp = in.readByte();
                    int stemp = (temp & 0xFF);
                    if (stemp >= 192)
                    {
                        int repeats = stemp & 0x3F;
                        if (logger.isDebugEnabled())
                        {
                            logger.debug("Processed " + pixelCount + " pixels.");
                            logger.debug("To repeat " + repeats + " times.");
                        }
                        byte color = in.readByte();
                        int sColor = (color & 0xFF);
                        if (rgbArray == 0)
                            repeats = rgbBitmaps[0].addRepeats(repeats, new Color(sColor, 0, 0));
                        else if (rgbArray == 1)
                            repeats = rgbBitmaps[1].addRepeats(repeats, new Color(0, sColor, 0));
                        else if (rgbArray == 2)
                            repeats = rgbBitmaps[2].addRepeats(repeats, new Color(0, 0, sColor));
                        pixelCount+=repeats;
                        xCount+=repeats;
                        numPixelsFromRepeats+=repeats;
                    }
                    else
                    {
                        if (rgbArray == 0)
                        {
                            try{
                                rgbBitmaps[0].add(new Color(stemp, 0, 0));
                            }
                            catch(IllegalArgumentException e)
                            {
                                logger.error("red: " + stemp, e);
                                System.exit(1);
                            }
                        }
                        else if (rgbArray == 1)
                            rgbBitmaps[1].add(new Color(0, stemp, 0));
                        else if (rgbArray == 2)
                            rgbBitmaps[2].add(new Color(0, 0, stemp));
                        pixelCount++;
                        xCount++;
                    }
                    if (pixelCount%bytesPerLine == 0)
                    {
                        //rowEnds[pixelCount/bytesPerLine - 1] = true;
                        rgbArray++;
                    }
                    //if (logger.isInfoEnabled() && pixelCount > 1400000)
                    //    logger.info("Processed " + pixelCount + " pixels.");
                }

                //Okay now make a real row
                for (int i = 0; i < bytesPerLine; i++)
                {
                    Color color = new Color(rgbBitmaps[0].getPixelRed(i, 0), rgbBitmaps[1].getPixelGreen(i, 0), rgbBitmaps[2].getPixelBlue(i, 0));
                    bitmap.add(color);
                }
                System.out.println("row: " + row);
                row++;
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
        catch(IOException e)
        {
            logger.error("IOException", e);
        }
    }
    
    private void import1BitPCX(LittleEndianDataInputStream in)
    {
        try{
            int pixelCount = 0;
            int numPixelsFromRepeats = 0;
            int row = 0;
            //allocate bitmap
            //times 8 for the x since each byte contains 8 bits
            bitmap = new Bitmap(bytesPerLine * 8, ysize);
            rowEnds = new boolean[this.ysize];
            System.out.println(bytesPerLine * ysize);
            while (pixelCount < (bytesPerLine * ysize * 8))
            {
                byte temp = in.readByte();
                int stemp = (temp & 0xFF);
                if (stemp >= 192)
                {
                    int repeats = stemp & 0x3F;
                    repeats = repeats << 3; //times 8, more efficiently
                    if (logger.isDebugEnabled())
                    {
                        logger.debug("Processed " + pixelCount + " pixels.");
                        logger.debug("To repeat " + repeats + " times.");
                    }
                    byte color = in.readByte();
                    int color256 = color * 255; //so it's white if 1, black if zero
                    Color nextColor = new Color(color256, color256, color256);
                    for (int j = 0; j < repeats; j++)
                        bitmap.add(nextColor);
                    pixelCount+=repeats;
                    numPixelsFromRepeats+=repeats;
                }
                else
                {
                    byte color = in.readByte();
                    int color256 = color * 255; //so it's white if 1, black if zero
                    Color nextColor = new Color(color256, color256, color256);
                    bitmap.add(nextColor);
                    pixelCount++;
                }
                if (pixelCount%(bytesPerLine *8) == 0)
                {
                    rowEnds[pixelCount/(bytesPerLine*8) - 1] = true;
                }
                if (logger.isInfoEnabled() && pixelCount > 1400000)
                    logger.info("Processed " + pixelCount + " pixels.");
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
        catch(IOException e)
        {
            logger.error("IOException", e);
        }
    }
    
    /**
     * Saves a PCX, assuming that we already have a PCX.
     * @param bitmap 
     */
    public void saveExistingPCX(String filename)
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
            
            //8-4-2012... realized that one does not simply ignore the bitmap and expect things will be okay
            //while it may be theoretically possible for this to work, if you've got a bitmap, you might care
            //about what is where in the bitmap, so thou should use the existing bitmap
           
            //8-4-2012... incrementally fixed this via hex editing and debugging.  manuals are for wimps
            //or at least, manuals are for people who have access to them... real programmers can make 
            //things without them if a hex editor is available

            //Export the bitmap
            //have to do each row at a time, then go on to next row
            for (int y = 0 ; y < bitmap.ysize; y++)
            {
                for (int x = 0; x < bitmap.xsize;)
                {
                    //int color = bitmap.getPixelRGB(x, y);
                    byte color = ((IndexedBitmap)(bitmap)).getPixelIndex(x, y);
                    //check for repeats
                    int repeats = 0;
                    //on first iteration, want to look at _at most_ byte 62 (base 0)
                    //that way we have _at most_ 63 pixels that are the same - 0xFF-0xC0 = 63
                    for (int xx = x + 1; xx < bitmap.xsize && xx < (63 + x); xx++)  //at most 0b111111 repeats allowed
                    {
                        if (((IndexedBitmap)(bitmap)).getPixelIndex(xx, y) == color)
                        {
                            repeats++;
                        }
                        else  //if any don't match, end our search, there are no repeats.
                        {
                            break;
                        }
                    }
                    if (repeats > 0)
                        repeats++;  //to account for the initial repeated one
                    byte colorInPalette = color;
                    //figure out the bytes
                    byte firstByte;
                    byte secondByte = 0;
                    boolean writeTwoBytes = false;
                    if (repeats > 0)
                    {
                        firstByte = (byte)(0xC0 | repeats);
                        secondByte = (byte) colorInPalette;
                        //second byte is where it is in the palette
                        writeTwoBytes = true;
                    }
                    else
                    {
                        //I'm not quite sure why... you'd think it wouldn't matter.
                        //But we really probably ought to break an indexed bitmap off from a plain old palette
                        byte toCompare = (byte)0xBF;
                        if ((colorInPalette < 0) && colorInPalette > toCompare)  //my BFF BF is 191
                        {
                            firstByte = (byte)0xC1; //AKA, 0xC1 - one repeat
                            secondByte = (byte)colorInPalette;
                            //second byte is where it is in the palette
                            writeTwoBytes = true;
                        }
                        else
                        {
                            firstByte = (byte)colorInPalette;
                        }
                    }
                    dos.writeByte(firstByte);
                    if (writeTwoBytes)
                    {
                        dos.writeByte(secondByte);
                    }
                    //increment
                    x++;
                    if (repeats > 0)
                        x--;
                    x+=repeats;
                }
                //The bitmap is aware of the "hidden" pixel in the PCX.
                //Thus, we needn't this block
//                if (bytesPerLine > this.xsize)
//                {
//                    //We've got an extra scanline here.  Save a blank pixel
//                    dos.writeByte(0xC1);
//                    dos.writeByte(0xFF);
//                }
            }
            
            
            //now export the colorz
            dos.writeByte(0x0C);
            for (int i = 0; i < 256; i++)
            {
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
     * Returns the index of the color in the palette.
     * Must provide an RGB combo that is in the palette.
     * @param color The RGB color combo thou art looking for
     * @return An unsigned byte of the index of the color in the palette
     */
    private byte findInPalette(int color)  //unsigned
    {
        for (int i = 0; i < palette.paletteSize; i++)
        {
            if (color == palette.palette[i].getRGB())
            {
                return (byte)(0xFF & i);
            }
        }
        return (byte)0xFF;
    }
    
    /**
    * Changes the color that was at position <i>index</i> in the palette to
    * a new color, <i>color</i>.
    * @param index - The index in the palette of the color to be replaced.
    * @param color - The new color.
    */
    public void setColor(int index, Color color)
    {
        Color oldColor = palette.palette[index];
        palette.palette[index] = color;
        for (int x = 0; x < bitmap.xsize; x++)
        {
            for (int y = 0; y < bitmap.ysize; y++)
            {
                if (bitmap.map[x][y] == color.getRGB())
                    bitmap.map[x][y] = color.getRGB();
            }
        }
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
        }
        catch(IOException e)
        {
            logger.error("IOException", e);
        }
    }
    
    public Color getColorAt(int x, int y)
    {
        return bitmap.getPixel(x, y);
    }
    
    public static void printStats() {
        System.out.println("Read time : " + (readTime/1000000) + " ms");
        System.out.println("Parse time : " + (parseTime/1000000) + " ms");
        System.out.println("Buffered time : " + (bufferedTime/1000000) + " ms");
    }

}
