/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.xplatformeditor;

import com.civfanatics.civ3.xplatformeditor.imageSupport.Civ3PCXFilter;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.lang.Thread.UncaughtExceptionHandler;
import java.time.Instant;
import java.time.Duration;
import org.apache.log4j.Logger;

/**
 * This class is used to multithread PCX imports.
 * In this model, we'll send out a thread for each PCX we want to import, and
 * let them all work at once.
 * 
 * We could also try having separate threads for reading from file, and parsing.
 * In that model, we'd have a queue for parsing and add stuff to it as we read
 * in files.  The downside would be we'd have at most as many threads as stages
 * in the pipeline - two or three, most likely.  On AMD hexacores, that'd be
 * inefficient.
 * 
 * This method allows more potential parallelization, but does come with a
 * potential drawback of trying to access lots of stuff on the disk at once.
 * On SSDs or unfragmented hard drives this shouldn't be too much of an issue,
 * but on fragmented HDDs the benefits may be rather light.
 * 
 * @author Andrew
 */
public class PCXImportThread extends Thread
{
    String fileName = "";
    boolean keepPCX = false;
    BufferedImage buffered = null;
    Civ3PCXFilter pcx = null;
    static Logger logger = Logger.getLogger("PCX Import Thread");
    
    int rows = 0;
    int cols = 0;
    int rowsSkipped = 0;
    int width = 0;
    int height = 0;
    
    boolean subdivisionsSet = false;
    BufferedImage[] subdivisions = null;
    
    static PCXExceptionHandler pcxExceptionHandler = new PCXExceptionHandler();
    
    public PCXImportThread(String fileName)
    {
        this.setName("PCX Import Thread for " + fileName);
        this.fileName = fileName;
    }
    
    public PCXImportThread(String fileName, boolean keepPCX)
    {
        this.setName("PCX Import Thread for " + fileName);
        this.fileName = fileName;
        this.keepPCX = true;
    }
    
    public void setSubdivisions(int rows, int cols, int rowsSkipped, int width, int height) {
        this.rows = rows;
        this.cols = cols;
        this.rowsSkipped = rowsSkipped;
        this.width = width;
        this.height = height;
        
        this.subdivisions = new BufferedImage[rows * cols];
        this.subdivisionsSet = true;
    }
    
    public void run()
    {
        this.setUncaughtExceptionHandler(pcxExceptionHandler);
        try{
            importPCX();   
        }
        catch(NullPointerException ex) {
            logger.info("NPE with file " + fileName, ex);
        }
        catch(java.lang.OutOfMemoryError ex) {
            logger.error("Out of memory with file " + fileName, ex);
            throw ex;
        }
    }
    
    private void processSubdivisions() {
        processSubdivisions(rows, cols, rowsSkipped, width, height, subdivisions);
    }
    
    public BufferedImage[] processSubdivisions(int rows, int cols, int rowsSkipped, int width, int height) {
        return processSubdivisions(rows, cols, rowsSkipped, width, height, new BufferedImage[rows * cols]);
    }
    
    private BufferedImage[] processSubdivisions(int rows, int cols, int rowsSkipped, int width, int height, BufferedImage[] subdivisions) {
        int rowOffset = rowsSkipped * height;
        
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                subdivisions[r * cols + c] = buffered.getSubimage(c * width, r * height + rowOffset, width, height);
            }
        }
        return subdivisions;
    }
    
    private BufferedImage[] processSubdivisionsWithoutIntermediary() {
        int rowOffset = rowsSkipped * height;
        
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                subdivisions[r * cols + c] = pcx.getSubimage(c * width, r * height + rowOffset, width, height);
            }
        }
        return subdivisions;
    }
    
    private void importPCX()
    {
        pcx = new Civ3PCXFilter(fileName);
        pcx.readFile();
        pcx.parse();
        if (subdivisionsSet) {
            //Don't bother creating a BufferedImage of the whole, just create
            //the constituent parts.
            processSubdivisionsWithoutIntermediary();
        }
        else {
            //In some cases, multiple subdivisions are created later.
            //For such cases, we create a buffered image here as a convenience
            pcx.createBufferedImage();
            buffered = pcx.getBufferedImage();
        }
        if (!keepPCX) {
            pcx = null;
        }
    }
    
    public BufferedImage getBufferedImage()
    {
        return buffered;
    }
    
    public BufferedImage[] getSubdivisions() {
        return subdivisions;
    }
    
    public void setColor(int index, Color c)
    {
        if (keepPCX) {
            pcx.setColor(index, c);
            pcx.createBufferedImage();
            buffered = pcx.getBufferedImage();
        }
        else {
            logger.error("Cannot set color on " + fileName + " as raw PCX data was not kept");
        }
    }
    
    public BufferedImage getCivColoredSubimage(int index, Color c, int x, int y, int width, int height) {
        
        if (keepPCX) {
            pcx.setColor(index, c);
            return pcx.getSubimage(x, y, width, height);
        }
        else {
            logger.error("Cannot set color on " + fileName + " as raw PCX data was not kept");
            return new BufferedImage(0, 0, BufferedImage.TYPE_4BYTE_ABGR);
        }
    }
}

class PCXExceptionHandler implements UncaughtExceptionHandler {
    
    static boolean alreadyAlertedToOOM = false;

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        
        if (e instanceof OutOfMemoryError) {
            if (!alreadyAlertedToOOM) {
                Main.displayOOMMessage();
                alreadyAlertedToOOM = true;
            }
        }
    }
}
