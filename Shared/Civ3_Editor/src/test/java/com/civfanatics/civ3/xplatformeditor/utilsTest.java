/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.xplatformeditor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Andrew
 */
public class utilsTest {
    
    @Test
    public void testSpliceInSearchFolderVanilla() {
        String firaxisPath = "D:\\Civilization III\\Art\\Units\\Palettes\\ntp00.pcx";
        String scenarioFolder = "MyAwesomeMod";
        
        //We do need to mock things up because if we rely on Main's settings, the test only
        //succeeds if they match the Firaxis path above.
        String oldInstallDir = Main.settings.civInstallDir;
        String oldname = Main.os.name;
        
        Main.settings.civInstallDir = "D:\\Civilization III";
        Main.os.name = "Windows XP";
        Main.fileSlash = "\\";  //so Windows tests work on Linux
        
        String answer = utils.spliceInSearchFolder(firaxisPath, scenarioFolder);
        
        Main.settings.civInstallDir = oldInstallDir;
        Main.os.name = oldname;
        
        assertEquals("D:\\Civilization III\\Conquests\\Scenarios\\MyAwesomeMod\\Art\\Units\\Palettes\\ntp00.pcx", answer);
    }
    
    @Test
    public void testSpliceInSearchFolderPTW() {
        String firaxisPath = "D:\\Civilization III\\Civ3PTW\\Art\\Espionage\\X_ESPIONAGEbkg.pcx";
        String scenarioFolder = "MyAwesomeMod";
        
        String oldInstallDir = Main.settings.civInstallDir;
        String oldname = Main.os.name;
        
        Main.settings.civInstallDir = "D:\\Civilization III";
        Main.os.name = "Windows XP";
        Main.fileSlash = "\\";  //so Windows tests work on Linux
        
        String answer = utils.spliceInSearchFolder(firaxisPath, scenarioFolder);
        
        Main.settings.civInstallDir = oldInstallDir;
        Main.os.name = oldname;
        
        assertEquals("D:\\Civilization III\\Conquests\\Scenarios\\MyAwesomeMod\\Art\\Espionage\\X_ESPIONAGEbkg.pcx", answer);
    }
    
    @Test
    public void testSpliceInSearchFolderConquests() {
        String firaxisPath = "D:\\Civilization III\\Conquests\\Art\\Terrain\\Volcanos.pcx";
        String scenarioFolder = "MyAwesomeMod";
        
        String oldInstallDir = Main.settings.civInstallDir;
        String oldname = Main.os.name;
        
        Main.settings.civInstallDir = "D:\\Civilization III";
        Main.os.name = "Windows XP";
        Main.fileSlash = "\\";  //so Windows tests work on Linux
        
        String answer = utils.spliceInSearchFolder(firaxisPath, scenarioFolder);
        
        Main.settings.civInstallDir = oldInstallDir;
        Main.os.name = oldname;
        
        assertEquals("D:\\Civilization III\\Conquests\\Scenarios\\MyAwesomeMod\\Art\\Terrain\\Volcanos.pcx", answer);
    }
    
    @Test
    public void testSpliceInSearchFolderConquestsOSX() throws IllegalAccessException, NoSuchFieldException {
        String firaxisPath = "/Applications/Sid Meier's Civilization III/Conquests Game Data/Art/Terrain/Volcanos.pcx";
        String scenarioFolder = "MyAwesomeMod";
        
        Field mainSlash = Main.class.getDeclaredField("fileSlash");
        makeFinalFieldNonFinal(mainSlash);
        
        String oldname = Main.os.name;
        String oldinstall = Main.settings.civInstallDir;
        String oldFileSlash = Main.fileSlash;
        
        Main.os.name = "Mac OS X 10.4 Tiger";
        Main.settings.civInstallDir = "/Applications/Sid Meier's Civilization III";
        mainSlash.set(null, "/");
        
        String answer = utils.spliceInSearchFolder(firaxisPath, scenarioFolder);
        
        //Restore the OS name and install directory from what we're running on
        Main.os.name = oldname;
        Main.settings.civInstallDir = oldinstall;
        mainSlash.set(null, oldFileSlash);
        
        //Restore permissions so they aren't an abomination
        makeNonFinalFieldFinal(mainSlash);
        
        assertEquals("/Applications/Sid Meier's Civilization III/Conquests Game Data/Scenarios/MyAwesomeMod/Art/Terrain/Volcanos.pcx", answer);
    }
    
    @Test
    public void testSpliceInSearchFolderPTWOSX() throws IllegalAccessException, NoSuchFieldException {
        String firaxisPath = "/Applications/Sid Meier's Civilization III/Play the World Game Data/Art/Espionage/X_ESPIONAGEbkg.pcx";
        String scenarioFolder = "MyAwesomeMod";
        
        Field mainSlash = Main.class.getDeclaredField("fileSlash");
        makeFinalFieldNonFinal(mainSlash);
        
        String oldname = Main.os.name;
        String oldinstall = Main.settings.civInstallDir;
        String oldFileSlash = Main.fileSlash;
        
        Main.os.name = "Mac OS X 10.4 Tiger";
        Main.settings.civInstallDir = "/Applications/Sid Meier's Civilization III";
        mainSlash.set(null, "/");
        
        String answer = utils.spliceInSearchFolder(firaxisPath, scenarioFolder);
        
        //Restore the OS name and install directory from what we're running on
        Main.os.name = oldname;
        Main.settings.civInstallDir = oldinstall;
        mainSlash.set(null, oldFileSlash);
        
        //Restore permissions so they aren't an abomination
        makeNonFinalFieldFinal(mainSlash);
        
        assertEquals("/Applications/Sid Meier's Civilization III/Conquests Game Data/Scenarios/MyAwesomeMod/Art/Espionage/X_ESPIONAGEbkg.pcx", answer);
    }
    
    @Test
    public void testSpliceInSearchFolderVanillaOSX() throws IllegalAccessException, NoSuchFieldException {
        String firaxisPath = "/Applications/Sid Meier's Civilization III/Civilization 3 Game Data/Art/Units/Palettes/ntp00.pcx";
        String scenarioFolder = "MyAwesomeMod";
        
        Field mainSlash = Main.class.getDeclaredField("fileSlash");
        makeFinalFieldNonFinal(mainSlash);
        
        String oldname = Main.os.name;
        String oldinstall = Main.settings.civInstallDir;
        String oldFileSlash = Main.fileSlash;
        
        Main.os.name = "Mac OS X 10.4 Tiger";
        Main.settings.civInstallDir = "/Applications/Sid Meier's Civilization III";
        mainSlash.set(null, "/");
        
        String answer = utils.spliceInSearchFolder(firaxisPath, scenarioFolder);
        
        //Restore the OS name and install directory from what we're running on
        Main.os.name = oldname;
        Main.settings.civInstallDir = oldinstall;
        mainSlash.set(null, oldFileSlash);
        
        //Restore permissions so they aren't an abomination
        makeNonFinalFieldFinal(mainSlash);
        
        assertEquals("/Applications/Sid Meier's Civilization III/Conquests Game Data/Scenarios/MyAwesomeMod/Art/Units/Palettes/ntp00.pcx", answer);
    }
    
    /**
     * Converts a field from final to nonfinal, so it can be modified.  Use with
     * extreme caution!  It's probably better to make the final field non-final
     * where possible.  In this test class, we're modifying a value that is
     * expected to be set once by OS, so that we can set it to various values
     * for testing.  This allows testing as if we were on multiple systems while
     * on one.  Refactoring would probably still be safer.
     * @param field
     * @throws IllegalAccessException
     * @throws NoSuchFieldException 
     */
    private void makeFinalFieldNonFinal(Field field) throws IllegalAccessException, NoSuchFieldException {
        Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.set(field, field.getModifiers() & ~Modifier.FINAL);
        modifiers.setAccessible(false);
    }
    
    /**
     * Converts a field from nonfinal to final.  Use with extreme caution!
     * Should only be used to reverse makeFinalFieldNonFinal.  Otherwise
     * you'll likely introduce very unexpected errors since no one expects
     * an error from trying to modify a final field when the field isn't
     * marked as final in the source code.
     * @param field
     * @throws IllegalAccessException
     * @throws NoSuchFieldException 
     */
    private void makeNonFinalFieldFinal(Field field) throws IllegalAccessException, NoSuchFieldException {
        Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.set(field, field.getModifiers() | Modifier.FINAL);
        modifiers.setAccessible(false);
    }
}
