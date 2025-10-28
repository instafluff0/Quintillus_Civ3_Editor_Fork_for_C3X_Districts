
package com.civfanatics.civ3.xplatformeditor.imageSupport;
import com.civfanatics.civ3.biqFile.util.LittleEndianDataInputStream;
import com.civfanatics.civ3.biqFile.util.LittleEndianDataOutputStream;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 * This class allows you to open many .bmp images.
 * Currently supported .bmps are:
 *  - Windows 3 bitmaps.  This is most of the bitmaps you'll encounter today.
 *  - 1, 4, 8, and 24-bit are all supported.
 * Additionally, some variants are supported:
 *  - Partial support of compressed bitmaps (all Windows 3 except 4-bit RLE and some bitmasks; unsure about Windows 4)
 *  - Partial support of NT 3.1 bitmaps (doesn't support all versions of bitmasks)
 *  - Support for Photoshop bitmaps with alpha channel, assuming alpha is to be used as transparency
 *  - Will open Windows 4 bitmaps, although it can't use the new Windows 4 features
 * The following are not at all supported:
 *  - Windows 98 bitmaps (bitmap version 5)
 * @author Andrew
 * @since March 28, 2011
 */
public class WindowsBMPFilter implements ImageFilter {
    
    int bmpVersion;  //not actually in the file; used for internal processing.  Use bmp header length as default value.  If I were using Java 1.5, I'd use enums
        public static final int WINDOWS_2_BMP = 14;
        public static final int WINDOWS_3_BMP = 40;
        public static final int WINDOWS_NT_BMP = 52;
        public static final int WINDOWS_NT_ALPHA_BMP = 56;
        public static final int OS2_2_BMP = 64;  //Isn't always length 64, but that's the most usual one
        public static final int WINDOWS_95_BMP = 108;

    Logger logger = Logger.getLogger(WindowsBMPFilter.class);
    String newline = "\n";
    File bmpFile;
    byte[]buffer;

    Bitmap bitmap;
    BufferedImage thisBufferedImage;
    byte[]rawImageData;

    //File header.
    short type;
        public final static short TYPE_WIN1 = 0;
        public final static short TYPE_WIN234 = 19778;  //0x424D little-endian
    //Windows 1 only
    short win1_width;
    short win1_height;
    short byteWidth;
    byte win1_planes;
    byte win1_bitsPerPixel;
    //Windows 2 or later only
    int fileLength;     //in bytes
    short xHotSpot;     //these are simply "reserved1" and "reserved2" in Windows 3 and later bitmaps, and are not used (they are zero)
    short yHotSpot;     //In OS/2 bitmaps, only used for icons and pointers
    int bitmapOffset;   //start of image in bytes
    //end file header

    //bitmap header - Windows 2 or later bitmaps only
    //The size can have the following valid values:
    // 12 - Windows 2 bitmap (Windows 2.x) and OS/2 1.x
    // 40 - Windows 3 bitmap (Windows 3.x)
    // 52 - Windows NT 3.1 bitmap
    // 56 - Photoshop bitmap with alpha bitfield (NT 3.1 plus alpha)
    // 64 - OS/2 2.x
    // 108 - Windows 4 bitmap (Windows 95)
    int size;   //size of this header
    //In Windows 2, width/height is two bytes, in Win3+ it is four bytes
    int width;
    int height;
    int widthInt;
    int heightInt;
    short widthShort;  //Windows 2 bitmaps use shorts for width/height
    short heightShort; //Signed in Windows 2.x; unsigned in OS/2 1.x
    //If height is positive, the bitmap starts at the bottom row.  If it is
    //negative, the bitmap starts at the top row (which is what PCX, MSP, Sun Icon do).
    short planes;   //always one
    short bitsPerPixel; //only 1,4,8,24 considered legal by Win2/3 API
    
    //The following fields are Windows 3 or later only
    //Note that all this is also used in OS/2 2.x, although sometimes with slightly different names in the documentation
    int compression;    //0 = none, 1 = 8-bit RLE, 2 = 4-bit RLE, 3 = bitfields encoding (WinNT only)
      public static final int COMPRESSION_NONE = 0;
      public static final int COMPRESSION_8BIT_RLE = 1;
      public static final int COMPRESSION_4BIT_RLE = 2;
      public static final int COMPRESSION_BITFIELDS = 3;
    int sizeOfBitmap; //size of bitmap in bytes - usually 0 when uncompressed.
    int horizontalResolution;   //pixels per meter
    int verticalResolution;     //pixels per meter
    int colorsUsed;     //# colors in palette.  if 0 and less than 16 bits of color, then maximum possible entries in colormap
                        //in that case, ColorsUsed = 1 << BitsPerPixel.  In Win2, always use this formula.
    int colorsImportant;      //minimum # of 'important' colors.  for use when you have fewer colors in hardware than in image

    //Windows 4 stuff
    int redMask;  //GIMP 2.8 appears to mis-use this to display the text "BGRs", presumably for "blue green reds"
    int greenMask;
    int blueMask;
    int alphaMask;
    //Note that I haven't added support for these variations yet (4/13)
    int colorSpaceType;
        public static final int COLORSPACE_CALIBRATED_RGB = 0;
        public static final int COLORSPACE_DEVICE_DEPENDENT_RGB = 1;
        public static final int COLORSPACE_DEVICE_DEPENDENT_CYMK = 2;
    int redX;   //x of red endpoint
    int redY;
    int redZ;
    int greenX;
    int greenY;
    int greenZ;
    int blueX;
    int blueY;
    int blueZ;
    int gammaRed;
    int gammaGreen;
    int gammaBlue;
    
    //OS/2 2.x features
    short units;    //type of units used to measure resolution
    short reserved; //pads to 4-byte boundary
    short recording;    //recording algorithm
    short rendering;    //halftoning algorithm
    int size1;      //halftoning algorithm use
    int size2;
    int colorEncoding;  //color model used
    int identifier;  //reserved for app use
    

    //Then there's version 5.  We'll start with Windows 3 as a good target

    //Note on how palettes are stored:
    // They are stored 1 byte at a time, as Blue, Green, Red palettes
    // In Win3+, there is also a fourth Reserved byte that is always 0 (padding).

    Palette palette;    //won't exist for 24-bit BMPs

    public WindowsBMPFilter()
    {
        
    }

    public WindowsBMPFilter(String string)
    {
        bmpFile = new File(string);
    }
    
    /**
     * Allows us to process the file and <i>not</i> create an image.
     * This could be useful for saving memory.
     * @param createImage 
     */
    public void processFile(boolean createImage)
    {
        try
        {
            if (createImage)
                processFile();
            else
            {
                readFile();
                try{
                    parse();
                }
                catch(UnsupportedVariantException e)
                {
                    throw e;
                }
            }
        }
        catch(UnsupportedVariantException e)
        {
            
        }
        
    }

    public void processFile() throws UnsupportedVariantException
    {
        readFile();
        try{
            parse();
        }
        catch(UnsupportedVariantException e)
        {
            throw e;
        }
        createBufferedImage();
    }

    public void readFile()
    {
        LittleEndianDataInputStream inFile = null;
        try{
            inFile = new LittleEndianDataInputStream(new BufferedInputStream(new FileInputStream(bmpFile)));
            buffer = new byte[(int)bmpFile.length()];
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
    }

    public void parse() throws UnsupportedVariantException
    {
        LittleEndianDataInputStream in = new LittleEndianDataInputStream(new ByteArrayInputStream(buffer));
        try{
            in.mark(buffer.length);
            type = in.readShort();
            if (type == 0)
                throw new UnsupportedVariantException("Windows 1 bitmaps are not currently supported");
            else if (type == 0x4142)  //"BA"
                throw new UnsupportedVariantException("OS/2 bitmap arrrays are not currently supported");
            else if (type == 0x4349)  //"IC"
                throw new UnsupportedVariantException("OS/2 bitmap icons are not currently supported");
            else if (type == 0x4943)  //"CI"
                throw new UnsupportedVariantException("OS/2 bitmap color icons are not currently supported");
            else if (type == 0x5450)  //"PT"
                throw new UnsupportedVariantException("OS/2 bitmap pointers are not currently supported");
            else if (type == 0x5043)  //"CP"
                throw new UnsupportedVariantException("OS/2 bitmap color pointers are not currently supported");
            else if (type != 0x4D42)
                throw new UnsupportedVariantException("Unrecgonized bitmap format");
            //mayhaps check that it equals 4D42)
            fileLength = in.readInt();
            xHotSpot = in.readShort();
            yHotSpot = in.readShort();
            bitmapOffset = in.readInt();

            /**
             * BEGIN BITMAP HEADER
             */
            
            //Bitmap header
            size = in.readInt();
            if (size == 12)   //4/21/2013 - Add support for Windows 2, OS/2 1 bitmaps
            {
                //throw new UnsupportedVariantException("Windows 2 bitmaps are not currently supported");
                widthShort = in.readShort();
                heightShort = in.readShort();
                width = widthShort;
                height = heightShort;
            }
            else  //Windows 3 or later
            {
                widthInt = in.readInt();
                heightInt = in.readInt();
                width = widthInt;
                height = heightInt;
            }
            boolean rightSideUp = height < 0;
            planes = in.readShort();
            bitsPerPixel = in.readShort();
            
            /**
             * END WINDOWS 2 STUFF
             */
            
            if (size > 12)
            {
                //Win3 stuff follows
                compression = in.readInt();
                sizeOfBitmap = in.readInt();
                horizontalResolution = in.readInt();
                verticalResolution = in.readInt();
                colorsUsed = in.readInt();
                colorsImportant = in.readInt();
                //End Win3 stuff.  What would follow would be Win4 stuff.
                System.out.println(this);

                //Where can size ever equal 56?
                //4/13 - Commented out so work can be done on Windows 95 bitmaps (some of which actually work quite well)
                //if (size != 40 && !(size == 56 && compression == 3))
                //    throw new UnsupportedVariantException("Windows 95 or later bitmaps are not currently supported");

                //4/18/2013 - Simplified the expression below, allow Windows 4
                //if ((compression != 0 && compression != COMPRESSION_8BIT_RLE) && !(compression == COMPRESSION_BITFIELDS && (size == 56 || size == 40)))
                if (compression == COMPRESSION_4BIT_RLE)  //4-bit RLE is a value of 2
                    throw new UnsupportedVariantException("Compressed bitmaps (4-bit RLE) are not currently supported");
            }

            /**
             * END WINDOWS 3 STUFF
             */
            
            
            int numBytesSoFar = 40;
            /**
             * Operating System Two 2.x Stuff
             */
            if (size == 64) //OS/2
            {
                bmpVersion = OS2_2_BMP;
                units = in.readShort();
                reserved = in.readShort();
                recording = in.readShort();
                rendering = in.readShort();
                size1 = in.readInt();
                size2 = in.readInt();
                colorEncoding = in.readInt();
                identifier = in.readInt();
                numBytesSoFar = 64;
            }
            
            //Windows NT and later stuff
            int size = this.size;
            if (size == 40 && compression == 3) //true NT 3 bitmap
                size = size + 12;
            if (numBytesSoFar < size)
            {
                redMask = in.readInt();
                numBytesSoFar+=4;
            }
            if (numBytesSoFar < size)
            {
                greenMask = in.readInt();
                numBytesSoFar+=4;
            }
            if (numBytesSoFar < size)
            {
                blueMask = in.readInt();
                numBytesSoFar+=4;
            }
            System.out.println("NT ends here");
            
            /**
             * END WINDOWS NT 3.1 STUFF
             */
            
            if (numBytesSoFar < size)
            {
                alphaMask = in.readInt();
                numBytesSoFar+=4;
            }
            System.out.println("NT Photoshop (with alpha) ends here");
            
            /**
             * END NT 3.1 + Photoshop Alpha Stuff
             */

            if (numBytesSoFar < size)
            {
                colorSpaceType = in.readInt();
                numBytesSoFar+=4;
            }
            if (numBytesSoFar < size)
            {
                redX = in.readInt();
                numBytesSoFar+=4;
            }
            if (numBytesSoFar < size)
            {
                redY = in.readInt();
                numBytesSoFar+=4;
            }
            if (numBytesSoFar < size)
            {
                redZ = in.readInt();
                numBytesSoFar+=4;
            }
            if (numBytesSoFar < size)
            {
                greenX = in.readInt();
                numBytesSoFar+=4;
            }
            if (numBytesSoFar < size)
            {
                greenY = in.readInt();
                numBytesSoFar+=4;
            }
            if (numBytesSoFar < size)
            {
                greenZ = in.readInt();
                numBytesSoFar+=4;
            }
            if (numBytesSoFar < size)
            {
                blueX = in.readInt();
                numBytesSoFar+=4;
            }
            if (numBytesSoFar < size)
            {
                blueY = in.readInt();
                numBytesSoFar+=4;
            }
            if (numBytesSoFar < size)
            {
                blueZ = in.readInt();
                numBytesSoFar+=4;
            }
            if (numBytesSoFar < size)
            {
                gammaRed = in.readInt();
                numBytesSoFar+=4;
            }
            if (numBytesSoFar < size)
            {
                gammaGreen = in.readInt();
                numBytesSoFar+=4;
            }
            if (numBytesSoFar < size)
            {
                gammaBlue = in.readInt();
                numBytesSoFar+=4;
            }
            //Windows 4 (full) ends here
            
            /**
             * END WINDOWS 4
             */
            
            //Figure out how many colors are actually present
            int totalColorsUsed = 0;
            if (colorsUsed != 0)
                totalColorsUsed = colorsUsed;
            else if (bitsPerPixel < 16)
                totalColorsUsed = 1 << bitsPerPixel;

            if (bitsPerPixel <= 8)
            {
                palette = new Palette(totalColorsUsed);
                for (int i = 0; i < totalColorsUsed; i++)
                {
                    byte blue = in.readByte();
                    byte green = in.readByte();
                    byte red = in.readByte();
                    //If win3 or later - note this is only in the palette, the padding isn't used in 24-bit BMPs
                    byte reserved;
                    if (size > 12)  //Only present in Windows 3 and later
                        reserved = in.readByte();  //intentionally not used
                    palette.addColor(red, green, blue);
                }
                //now the actual bitmap.  it might be in an unusual position, so
                //make sure we start at the right place.
                in.reset();
                in.skip(bitmapOffset);

                bitmap = new Bitmap(width, Math.abs(height), rightSideUp);

                if (compression == 0)
                {
                    if (bitsPerPixel == 1)
                    {
                        for (int i = 0; i < Math.abs(height); i++)
                        {
                            int bytesReadThisRow = 0;
                            for (int h = 0; h < width;)
                            {
                                byte next = in.readByte();
                                bytesReadThisRow++;
                                for (int j = 0; j < 8; j++)
                                {
                                    //kinda neat bug: (byte)(next & 0x80).  makes stuff whopperjawed
                                    //kinda neat bug: (byte)(next & 0x80) >>> 7.  makes only 1/8 of cols possibly white
                                    byte bit = (byte)((next & 0x80) >>> 7);
                                    bitmap.add(palette.getColor(bit));
                                    next = (byte)(next << 1);
                                    h++;
                                    //This will in effect break out of two for loops
                                    if (h == width)
                                        break;
                                }
                            }
                            int skipBytes = 4-(bytesReadThisRow%4);
                            if (skipBytes == 4)
                                skipBytes = 0;
                            in.skip(skipBytes);
                        }
                    }
                    else if (bitsPerPixel == 8)
                    {
                        for (int i = 0; i < Math.abs(height);i++)
                        {
                            //TODO: Fix odd-width
                            for (int j = 0; j < width;)
                            {
                                byte next = in.readByte();
                                bitmap.add(palette.getColor(next));
                                j++;
                            }
                            int skipBytes = 4-(width%4);
                            if (skipBytes == 4)
                                skipBytes = 0;
                            in.skip(skipBytes);
                        }

                    }
                    else if (bitsPerPixel == 4)
                    {
                        for (int i = 0; i < Math.abs(height);i++)
                        {
                            //TODO: Fix odd-width
                            int bytesReadThisRow = 0;
                            for (int j = 0; j < width;)
                            {
                                byte next = in.readByte();
                                bytesReadThisRow++;
                                byte nibble = (byte)(next >> 4 & 0xF);
                                bitmap.add(palette.getColor(nibble));
                                j++;
                                if (j == width)
                                    break;
                                nibble = (byte)(next & 0xF);
                                bitmap.add(palette.getColor(nibble));
                                j++;
                            }
                            int skipBytes = 4-(bytesReadThisRow%4);
                            if (skipBytes == 4)
                                skipBytes = 0;
                            in.skip(skipBytes);
                        }
                    }
                }
                else if (compression == 1 && (bitsPerPixel == 4 || bitsPerPixel == 8))
                {
                    while(true)
                    {
                        byte nextByte = in.readByte();
                        if (nextByte == 0)
                        {
                            byte code = in.readByte();
                            if (code == 1)  //end RLE data
                                break;
                            else if (code == 0)
                                bitmap.goToNextScanLine();
                            else if (code == 2)
                                throw new UnsupportedVariantException("Don't yet support delta code in encoded BMPs");
                            else    //unencoded data
                            {
                                short uCode = (short)(code & 0xFF);
                                System.out.println(uCode + " bytes of unencoded data");
                                for (int i = 0; i < uCode; i++)
                                {
                                    byte data = in.readByte();
                                    bitmap.add(palette.getColor(data));
                                }
                            }
                        }
                        else
                        {   //an encoded run
                            byte data = in.readByte();
                            short uNum = (short)(nextByte & 0xFF);
                            System.out.println(uNum + " bytes of encoded data");
                            for (int i = 0; i < uNum; i++)
                            {
                                bitmap.add(palette.getColor(data));
                            }
                        }
                    }
                }
            }
            else if (compression == 0 && this.bitsPerPixel == 24)    //no palette
            {
                //now the actual bitmap.  it might be in an unusual position, so
                //make sure we start at the right place.
                in.reset();
                in.skip(bitmapOffset);
                bitmap = new Bitmap(width, Math.abs(height), rightSideUp);
                //TODO: height
                for (int i = 0; i < Math.abs(height); i++)
                {
                    for (int j = 0; j < width; j++)
                    {
                        byte blue = in.readByte();
                        byte green = in.readByte();
                        byte red = in.readByte();
                        int sBlue = blue & 0xFF;
                        int sGreen = green & 0xFF;
                        int sRed = red & 0xFF;
                        //If win3 or later
                        bitmap.add(new Color(sRed, sGreen, sBlue));
                    }
                    //there must be 4x bytes per row, x an integer
                    int numThrowawayBytes = 4-((width*3)%4);
                    if (numThrowawayBytes == 4)
                        numThrowawayBytes = 0;
                    in.skip(numThrowawayBytes);
                }
            }
            else if (compression == 3 && this.bitsPerPixel == 32)
            {
                in.reset();
                in.skip(bitmapOffset);
                bitmap = new Bitmap(width, Math.abs(height), rightSideUp);
                //TODO: height
                int maskHunt = 1;
                int redShift = 0;
                for (;;redShift++)
                {
                    //System.out.println("Hunting");
                    if (!((redMask & maskHunt) == maskHunt))
                        maskHunt = maskHunt << 1;
                    else
                        break;
                }
                maskHunt = 1;
                int greenShift = 0;
                for (;;greenShift++)
                {
                    //System.out.println("Hunting");
                    if (!((greenMask & maskHunt) == maskHunt))
                        maskHunt = maskHunt << 1;
                    else
                        break;
                }
                int blueShift = 0;  //half-life
                maskHunt = 1;
                for (;;blueShift++)
                {
                    //System.out.println("Hunting");
                    if (!((blueMask & maskHunt) == maskHunt))
                        maskHunt = maskHunt << 1;
                    else
                        break;
                }
                int alphaShift = 0;  //half-life
                maskHunt = 1;
                for (;;alphaShift++)
                {
                    //System.out.println("Hunting");
                    if (!((alphaMask & maskHunt) == maskHunt))
                        maskHunt = maskHunt << 1;
                    else
                        break;
                }
                System.out.println("redShift: " + redShift + ", greenShift: " + greenShift + ", blueShift: " + blueShift + ", alphaShift: " + alphaShift);
                for (int i = 0; i < Math.abs(height); i++)
                {
                    //System.out.println("row: " + i);
                    for (int j = 0; j < width; j++)
                    {
                        //System.out.println("j: " + j);
                        //System.out.println("NT 3 bitmap");
                        //So far, assuming 8888, RGB color space
                        //not assuming where they start, except on a byte boundary
                        int nextPixel = in.readInt();
                        int red = nextPixel & this.redMask;
                        red = red >>> redShift;
                        int green = nextPixel & this.greenMask;
                        green = green >>> greenShift;
                        int blue = nextPixel & this.blueMask;
                        blue = blue >>> blueShift;
                        int alpha = nextPixel & this.alphaMask;
                        alpha = alpha >>> alphaShift;
                        //System.out.println("Adding" + red + ", " + green + ", " + blue + ", " + (255 - alpha));
                        bitmap.add(new Color(red, green, blue, 255 - alpha));
                    }
                    //there must be 4x bytes per row, x an integer
                    //int numThrowawayBytes = 4-((width*3)%4);
                    //if (numThrowawayBytes == 4)
                    //    numThrowawayBytes = 0;
                    //in.skip(numThrowawayBytes);
                }
            }
            else
            {
                System.out.println("Shouldn't get here");
            }

        }
        catch(IOException e)
        {
            logger.error("IOException", e);
        }
    }

    public void createBufferedImage()
    {
        //type 1 is TYPE_INT_RGB
        thisBufferedImage = new BufferedImage(width, Math.abs(height), BufferedImage.TYPE_INT_ARGB);
        int[]rgbArray = new int[width * Math.abs(height)];
        for (int y = 0; y < Math.abs(height); y++)
        {
            for (int x = 0; x < width; x++)
            {
                rgbArray[y*width + x] = bitmap.getPixelRGB(x, y);
            }
        }
        //setRGB
        thisBufferedImage.setRGB(0, 0, width, Math.abs(height), rgbArray, 0, width);
    }

    public BufferedImage getBufferedImage()
    {
        return thisBufferedImage;
    }


    /**
     * Class that represents the palette for a bitmap file.
     */
    public class Palette{
        Color[]palette;
        int paletteSize;
        int maxSize;
        /**
         * Creates a new palette
         */
        public Palette(int size)
        {
            palette = new Color[size];
            maxSize = size;
            paletteSize = 0;
        }
        /**
         * Adds a color to the PCX's palette.
         * @param red - The red value of the new color (unsigned byte).
         * @param green - The green value of the new color (unsigned byte).
         * @param blue - The blue value of the new color (unsigned byte).
         * @return True if the color can be added, false if there are already
         * the maximum number colors.
         */
        public boolean addColor(byte red, byte green, byte blue)
        {
            if (paletteSize >= maxSize)
                return false;   //cannot add a 257th color
            //else, we need to create a color
            int sRed, sGreen, sBlue;
            sRed = red < 0 ? red + 256 : red;
            sGreen = green < 0 ? green + 256 : green;
            sBlue = blue < 0 ? blue + 256 : blue;
            if (logger.isDebugEnabled())
                logger.debug("sRed: " + sRed + " sGreen: " + sGreen + " sBlue: " + sBlue);
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
                    if (bitmap.map[x][y] == (oldColor.getRGB()))
                        bitmap.map[x][y] = color.getRGB();
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

    //TODO: change these when win1/2 support added
    public int getWidth()
    {
        return width;
    }
    public int getHeight()
    {
        return height;
    }

    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append("type: " + type + newline);
        buf.append("fileLength: " + fileLength + newline);
        buf.append("reserved1: " + xHotSpot + newline);
        buf.append("reserved2: " + yHotSpot + newline);
        buf.append("bitmapOffset: " + bitmapOffset + newline);

        buf.append("size: " + size + newline);
        buf.append("width: " + width + newline);
        buf.append("height: " + height + newline);
        buf.append("planes: " + planes + newline);
        buf.append("bitsPerPixel: " + bitsPerPixel + newline);
        buf.append("compression: " + compression + newline);
        buf.append("sizeOfBitmap: " + sizeOfBitmap + newline);
        buf.append("horizontalResolution: " + horizontalResolution + newline);
        buf.append("verticalResolution: " + verticalResolution + newline);
        buf.append("compressUsed: " + colorsUsed + newline);
        buf.append("colorsImportant: " + colorsImportant + newline);
        
        if (this.size >= 52)
        {
            buf.append("redMask: " + redMask + newline);
            buf.append("greenMask: " + greenMask + newline);
            buf.append("blueMask: " + blueMask + newline);
        }
        if (this.size >= 56)
            buf.append("alphaMask: " + alphaMask + newline);
        
        if (this.size >= 108)
        {
            buf.append("colorSpaceType: " + colorSpaceType + newline);
            buf.append("redX: " + redX + newline);
            buf.append("redY: " + redY + newline);
            buf.append("redZ: " + redZ + newline);
            buf.append("greenX: " + greenX + newline);
            buf.append("greenY: " + greenY + newline);
            buf.append("greenZ: " + greenZ + newline);
            buf.append("blueX: " + blueX + newline);
            buf.append("blueY: " + blueY + newline);
            buf.append("blueZ: " + blueZ + newline);
            buf.append("gammaRed: " + gammaRed + newline);
            buf.append("gammaGreen: " + gammaGreen + newline);
            buf.append("gammaBlue: " + gammaBlue + newline);
        }
        
        if (palette != null)
        {
            for (int i = 0; i < palette.paletteSize; i++)
            {
                buf.append("palette color " + i + ": " + palette.getColor((byte)i).toString() + "\n");
            }
        }

        return buf.toString();
    }

    /**
     * Creates a bitmap from the image.
     * Right now creates a 24-bit uncompressed Windows 3 bitmap.
     * @param image
     * @param fileName
     */
    public void createBitmapFilter(BufferedImage image)
    {
        //file header
        this.type = TYPE_WIN234;
        fileLength = 14 + 40;
        int sizeOfRow = 3*image.getWidth();
        int additionalBytes = 4-((sizeOfRow)%4);
        if (additionalBytes == 4)
            additionalBytes = 0;
        sizeOfRow = sizeOfRow + additionalBytes;
        fileLength = fileLength + (sizeOfRow * image.getHeight());
        xHotSpot = 0;
        yHotSpot = 0;
        bitmapOffset = 54;

        //begin bitmap header
        size = 40;
        width = image.getWidth();
        height = image.getHeight();
        planes = 1;
        bitsPerPixel = 24;
        compression = COMPRESSION_NONE;
        sizeOfBitmap = sizeOfRow * height;
        horizontalResolution = 0;
        verticalResolution = 0;
        colorsUsed = 1 << bitsPerPixel;
        colorsImportant = 0;
        //End Win3 header

        //image data
        bitmap = new Bitmap(width, height, false);
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                bitmap.add(new Color(image.getRGB(j, ((height - 1) - i))));
            }
        }
    }

    /**
     * Creates raw binary data for the image portion of the file
     */
    public void createRawData()
    {
        int additionalBytes = 4-((width*3)%4);
        if (additionalBytes == 4)
            additionalBytes = 0;
        System.out.println("Additional bytes per row: " + additionalBytes);

        rawImageData = new byte[sizeOfBitmap];
        int ptr = 0;
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                rawImageData[ptr] = (byte)(bitmap.getPixelBlue(x, (height - 1) - y) & 0xFF);
                ptr++;
                rawImageData[ptr] = (byte)(bitmap.getPixelGreen(x, (height - 1) - y) & 0xFF);
                ptr++;
                rawImageData[ptr] = (byte)(bitmap.getPixelRed(x, (height - 1) - y) & 0xFF);
                ptr++;
            }
            for (int i = 0; i < additionalBytes; i++)
            {
                rawImageData[ptr] = 0;
                ptr++;
            }
        }
    }

    public void exportFile(String fileName)
    {
        File outFile = new File(fileName);
        LittleEndianDataOutputStream fos = null;
        try
        {
            fos = new LittleEndianDataOutputStream(new BufferedOutputStream(new FileOutputStream(outFile)));
        }
        catch(FileNotFoundException e)
        {
            logger.error("File not found", e);
        }
        try{
            fos.writeShort(type);
            fos.writeInt(fileLength);
            fos.writeShort(xHotSpot);
            fos.writeShort(yHotSpot);
            fos.writeInt(bitmapOffset);
            //end file header
            fos.writeInt(size);
            fos.writeInt(width);
            fos.writeInt(height);
            fos.writeShort(planes);
            fos.writeShort(bitsPerPixel);
            fos.writeInt(compression);
            fos.writeInt(sizeOfBitmap);
            fos.writeInt(horizontalResolution);
            fos.writeInt(verticalResolution);
            fos.writeInt(colorsUsed);
            fos.writeInt(colorsImportant);
            //end bitmap header
            fos.write(rawImageData);
            fos.close();
        }
        catch(IOException e)
        {
            logger.error("", e);
        }
    }

    public void exportImage(BufferedImage image, String fileName)
    {
        createBitmapFilter(image);
        createRawData();
        exportFile(fileName);
    }
    
    public int getPaletteSize()
    {
        return this.palette.paletteSize;
    }
    
    /**
     * This would follow better standards if it made a copy, but that can wait 4 later.
     * @return 
     */
    public Color[] getPaletteColors()
    {
        return this.palette.palette;
    }
    
    /**
     * Returns the color's palette index.
     * @param x - Color's x coordinate
     * @param y - Color's y coordinate
     * @return 
     */
    public int getPaletteIndexAt(int x, int y)
    {
        Color c = bitmap.getPixel(x, y);
        for (int i = 0; i < palette.paletteSize; i++)
        {
            if (palette.palette[i].equals(c))
            {
                return i;
            }
        }
        return 0;
    }
    
    public int getBitsPerPixel()
    {
        return this.bitsPerPixel;
    }

}
