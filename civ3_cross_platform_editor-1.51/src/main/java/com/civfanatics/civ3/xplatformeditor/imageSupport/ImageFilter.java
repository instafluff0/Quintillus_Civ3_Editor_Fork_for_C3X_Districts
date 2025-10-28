/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.civfanatics.civ3.xplatformeditor.imageSupport;

import java.awt.image.BufferedImage;

/**
 *
 * @author Andrew
 */
public interface ImageFilter {
    public void processFile() throws IllegalDataException, UnsupportedVariantException;
    public BufferedImage getBufferedImage();
    public int getWidth();
    public int getHeight();
}
