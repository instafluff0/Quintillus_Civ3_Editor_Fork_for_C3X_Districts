
package com.civfanatics.civ3.xplatformeditor;

/**
 * New launcher.  All it does is set the application name for macOS, and
 * defer to the Main launcher.
 * @author Quintillus
 */
public class MainLauncher {
    
    static final String TITLE = "Conquests Editor";
    
    public static void main(String[] args) {
        
        System.setProperty("apple.awt.application.name", TITLE);

        Main.main(args);
        
    }
}
