/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.xplatformeditor;

import org.apache.log4j.Logger;

/**
 *
 * @author Andrew
 */
public class Dummy {
    
    Logger logger = Logger.getLogger(this.getClass());
    
    public Dummy() {
        this("");
    }
    
    public Dummy(String string) {
        logger.info("Initialized a Dummy " + string);
    }
}
