package com.civfanatics.civ3.xplatformeditor.tabs.map.renderer;

import com.civfanatics.civ3.xplatformeditor.tabs.map.GraphicsAssets;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 *
 * @author Andrew
 */
public abstract class Renderer {    
    protected static int BUFFERWIDTH=1300;  //1024+64
    protected static int BUFFERHEIGHT=1200;  //768+32
    protected static int TILE_WIDTH = 128;
    protected static int TILE_HEIGHT = 64;
    
    protected BufferedImage buffer = null;
    protected GraphicsAssets assets = null;
    
    protected int horizScrollOffset, vertScrollOffset;

    public abstract void render();
    
    /**
     * Returns the offscreen buffered image that is ready to be drawn.
     * @return The offscreen buffered image.
     */
    public BufferedImage getBuffer() {
        return buffer;
    }
    
    protected boolean tileIsVisible(int xIndex, int yIndex)
    {
        int tileLeftBoundary = xIndex*64 - 64;
        int tileTopBoundary = yIndex*32;
        int leftBoundaryOnCanvas = tileLeftBoundary - horizScrollOffset + TILE_WIDTH;
        int topBoundaryOnCanvas = tileTopBoundary - vertScrollOffset + TILE_HEIGHT;
        if ((leftBoundaryOnCanvas > 0 && leftBoundaryOnCanvas < BUFFERWIDTH) && (topBoundaryOnCanvas > 0 && topBoundaryOnCanvas < BUFFERHEIGHT))
            return true;
        return false;
    }
    
    
    public void setHorizPosition(int x) {
        this.horizScrollOffset = x;
    }

    public void setVertPosition(int y) {
        this.vertScrollOffset = y;
    }
    
    public abstract void setViewportSize(int x, int y);
    
    /**
     * TODO: Implement in renderers
     * @param x
     * @param y
     * @return 
     */
    public abstract Image drawTile(int x, int y);
}
