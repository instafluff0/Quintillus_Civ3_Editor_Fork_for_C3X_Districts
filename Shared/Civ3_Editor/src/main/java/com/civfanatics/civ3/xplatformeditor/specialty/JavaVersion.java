/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.xplatformeditor.specialty;

import com.sun.javafx.charts.Legend;
import com.sun.javafx.collections.SortHelper;
import org.apache.log4j.Logger;

/**
 *
 * @author Andrew
 */
public class JavaVersion {
    
    static Logger logger = Logger.getLogger(JavaVersion.class);
    
    public static boolean incompatibleJavaVersion(String javaRuntime) {
        try {
            Class.forName("javafx.scene.image.Image");
        }
        catch(ClassNotFoundException ex) {
            return true;
        }
        return false;
    }
    
    public static boolean comSunJavaFXWarning() {
        try {
            SortHelper sh = new com.sun.javafx.collections.SortHelper();
            Legend legend = new com.sun.javafx.charts.Legend();
        }
        catch(IllegalAccessError ex) {
            logger.info("Caught exception while checking for Java 17 incompatibilities.  Should not crash!");
            return true;
        }
        return false;
    }
    
    /**
     * Checks for whether the editor is running on Java 11 or later.  Java 11 does not include JavaFX by default.
     * @param javaRuntime The java runtime, ex. 1.8.0_u151, or 11 (note that the initial release may not include a dot).
     */
    private static boolean hasJava11(String javaRuntime) {
        int dotIndex = javaRuntime.indexOf(".");
        String majorVersion = "1";
        if (dotIndex > -1) {
            majorVersion = javaRuntime.substring(0, dotIndex);
        }
        else {
            majorVersion = javaRuntime;
        }
        int majorVersionInt = 1;
        try {
            majorVersionInt = Integer.parseInt(majorVersion);
        }
        catch(NumberFormatException ex) {
            logger.error("Could not detect Java version from value " + javaRuntime);
        }
        if (majorVersionInt > 10) {
            return true;
        }
        return false;
    }
}
