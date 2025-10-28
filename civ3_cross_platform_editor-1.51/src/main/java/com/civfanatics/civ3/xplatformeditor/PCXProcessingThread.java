/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.xplatformeditor;

import com.civfanatics.civ3.xplatformeditor.imageSupport.Civ3PCXFilter;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 *
 * @author Andrew
 */
public class PCXProcessingThread implements Callable {
    
    Civ3PCXFilter pcx = null;
    BufferedImage buffered = null;
    
    public Object[] call() {
        processPCX();
        Object[] retVal = new Object[2];
        retVal[0] = pcx.getFileName();
        retVal[1] = buffered;
        return retVal;
    }
    
    public void setPCX(Civ3PCXFilter thePCX) {
        this.pcx = thePCX;
    }
    
    private void processPCX()
    {
        pcx.parse();
        pcx.createBufferedImage();
        buffered = pcx.getBufferedImage();
    }
    
    public BufferedImage getBufferedImage()
    {
        return buffered;
    }
    
    public void setColor(int index, Color c)
    {
        pcx.setColor(index, c);
        pcx.createBufferedImage();
        buffered = pcx.getBufferedImage();
    }
}
