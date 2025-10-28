package com.civfanatics.civ3.xplatformeditor;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;
import org.apache.log4j.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Quintillus
 */


public class IconPanel extends JPanel{
    Logger logger = Logger.getLogger(this.getClass());
    Image myImage;

    public void setImage(Image image)
    {
        myImage = image;
    }
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        if (this.isVisible())
        {
            if (logger.isDebugEnabled())
                logger.debug("Icon image painting");
            g.drawImage(myImage, 0, 0, null);
        }
        //paint my contents
    }

}
