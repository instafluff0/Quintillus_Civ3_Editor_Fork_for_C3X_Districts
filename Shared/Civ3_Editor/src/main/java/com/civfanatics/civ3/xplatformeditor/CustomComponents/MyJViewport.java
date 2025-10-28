/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.xplatformeditor.CustomComponents;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JViewport;
import javax.swing.RepaintManager;

/**
 * This class overrides the viewport.  The benefit is that it gets rid of
 * the annoying white bars seen in versions 0.78 and earlier.
 * 
 * Adopted from the regular Java 1.5 JViewport
 * @since 0.79
 * @author Quintillus
 */
public class MyJViewport extends JViewport{
    
    
    @Override
    public void setViewPosition(Point p) 
    {
        Component view = getView();
        if (view == null) {
            return;
        }
        
        int oldX, oldY, x = p.x, y = p.y;

        /* Collect the old x,y values for the views location
         * and do the song and dance to avoid allocating 
         * a Rectangle object if we don't have to.
         */
        if (view instanceof JComponent) {
            JComponent c = (JComponent)view;
            oldX = c.getX();
            oldY = c.getY();
        }
        else {
            Rectangle r = view.getBounds();
            oldX = r.x;
            oldY = r.y;
        }

        /* The view scrolls in the opposite direction to mouse 
         * movement.
         */
        int newX = -x;
        int newY = -y;
        
        if ((oldX != newX) || (oldY != newY)) {
            if (true) {
                Graphics g = getGraphics();
//              flushViewDirtyRegion(g);
                // This calls setBounds(), and then repaint().
                view.setLocation(newX, newY);
                // The cast to JComponent here is valid, if view is not 
                // a JComponent, isBlitting will return false.
                g.setClip(0,0,getWidth(), Math.min(getHeight(),
                                 ((JComponent)view).getHeight()));
                // Repaint the complete component if the blit succeeded
                // and needsRepaintAfterBlit returns true.
//              repaintAll = (windowBlitPaint(g) &&
//                            needsRepaintAfterBlit());
                g.dispose();
                RepaintManager rm = RepaintManager.currentManager(this);
                rm.markCompletelyClean((JComponent)getParent());
                rm.markCompletelyClean(this);
                rm.markCompletelyClean((JComponent)view);
            }
            else {
                scrollUnderway = true;
                // This calls setBounds(), and then repaint().
                view.setLocation(newX, newY);
//              repaintAll = false;
            }
            fireStateChanged();
        }
    }
    
}
