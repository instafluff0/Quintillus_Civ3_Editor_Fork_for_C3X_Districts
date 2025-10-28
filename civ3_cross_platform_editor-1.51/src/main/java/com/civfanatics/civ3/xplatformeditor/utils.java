package com.civfanatics.civ3.xplatformeditor;

import com.civfanatics.civ3.biqFile.IO;
import static com.civfanatics.civ3.biqFile.IO.pathToBin;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.SuperListModel;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import org.apache.log4j.Logger;
/**
 * This class houses utility methods used by the editor.
 * 
 * @author Andrew
 */
public final class utils {


    static String autoDetectFail = " does not exist; auto-detect failed";

    static Logger logger = Logger.getLogger(utils.class);

    /**
     * Private constructor.  There is no need for instances of this class.
     */
    private utils(){

    }
    
    //Precondition: The calling class should have set classIndex to -1
    //Failure to do so will result in data corruption where the list's
    //selected index is first changed
    /**
     * This is the same as the regular version, but will intercept any cases
     * where a SuperListModel is used.  This ought to keep things from breaking
     * in the interval between the first SuperListModel implementations and their
     * complete rollout, and thus allow a gradual rollout.
     * 
     * @param index - The index of the item to remove from both <i>storage</i> and <i>model</i>.
     * @param storage - The ArrayList.
     * @param model - The DefaultListModel which backs the JList.
     * @param list - The JList, which is backed by the DefaultListModel.
     */
    public static void removeFromList(int index, List storage, SuperListModel model, JList list)
    {
        storage.remove(index);

        model.slRemove(index);

        if (model.size() > index)
            list.setSelectedIndex(index);
        else
            list.setSelectedIndex(index - 1);
    }
    
    //Precondition: The calling class should have set classIndex to -1
    //Failure to do so will result in data corruption where the list's
    //selected index is first changed
    /**
     * Removes the item at index <i>index</i> from the ArrayList <i>storage</i>,
     * and the DefaultListModel <i>model</i>, and sets the selected item in the
     * JList <i>list</i> to either the item preceding <i>index</i>, or the one
     * preceding it if <i>index</i> was the last item in the JList.
     *
     * This is to be used where there is a list of items that is stored in some
     * ArrayList, and is also in a DefaultListModel that backs some JList.
     *
     * @param index - The index of the item to remove from both <i>storage</i> and <i>model</i>.
     * @param storage - The ArrayList.
     * @param model - The DefaultListModel which backs the JList.
     * @param list - The JList, which is backed by the DefaultListModel.
     * @deprecated - Because we should transition to SuperListModels eventually.
     */
    public static void removeFromList(int index, List storage, DefaultListModel model, JList list)
    {
        storage.remove(index);

        model.remove(index);

        if (model.size() > index)
            list.setSelectedIndex(index);
        else
            list.setSelectedIndex(index - 1);
    }
    /**
     * Removes the item at index <i>index</i> from the ArrayList <i>storage</i>,
     * and the DefaultListModel <i>model</i>, and sets the selected item in the
     * JList <i>list</i> to either the item preceding <i>index</i>, or the one
     * preceding it if <i>index</i> was the last item in the JList.
     * 
     * This version does not update the List from the BIQ section.  Instead, we
     * will call the appropriate method for that section.
     *
     * This is to be used where there is a list of items that is stored in some
     * ArrayList, and is also in a DefaultListModel that backs some JList.
     *
     * @param index - The index of the item to remove from both <i>storage</i> and <i>model</i>.
     * @param storage - The ArrayList.
     * @param model - The DefaultListModel which backs the JList.
     * @param list - The JList, which is backed by the DefaultListModel.
     * @deprecated - Because we should transition to SuperListModels eventually.
     */
    public static void removeFromList(int index, DefaultListModel model, JList list)
    {
        model.remove(index);

        if (model.size() > index)
            list.setSelectedIndex(index);
        else
            list.setSelectedIndex(index - 1);
    }

    //Correct modulus operator
    //Java's % operator is not a real modulus operator
    //Whether C's % operator is depends on the platform (on gcc on Linux is is
    //not and appears to act similarly to Java's)
    //public static byte modulus(byte dividend, byte divisor)
    //{
    //    if (divisor > 0)
    //}

    /**
     * Uses the Windows layout of the Civilization III (and Conquests)
     * directories to search for files.  This method will be called for any
     * Windows operating system.
     * @param name
     * @param folderStruct
     * @return
     */
    private static String findFileWindows(String name, String folderStruct, IO biq) throws FileNotFoundException
    {
        //first search for the file in all the scenario search folders listed
        if (biq.scenarioProperty.size() > 0) {
            for (int i = 0; i < biq.scenarioProperty.get(0).getSearchFolderLength(); i++)
            {
                String fileName = Main.settings.civInstallDir + Main.fileSlash + "Conquests" + Main.fileSlash + "Scenarios" + Main.fileSlash + biq.scenarioProperty.get(0).getSearchFolder(i) + Main.fileSlash + folderStruct + name;
                File file = new File(fileName);
                if (file.exists())
                    return fileName;
            }
        }
        //if it doesn't exist in any of the custom directories, try the Conquests directory
        String fileName = Main.settings.civInstallDir + Main.fileSlash + "Conquests" + Main.fileSlash + folderStruct + name;
        File file = new File(fileName);
        if (file.exists())
            return fileName;

        //if it still doesn't exist, try the PTW directory
        fileName = Main.settings.civInstallDir + Main.fileSlash + "Civ3PTW" + Main.fileSlash + folderStruct + name;
        file = new File(fileName);
        if (file.exists())
            return fileName;
        else
        //if it still doesn't exist, try the base Civ3 directory - last chance
        fileName = Main.settings.civInstallDir + Main.fileSlash + folderStruct + name;
        file = new File(fileName);
        if (file.exists())
            return fileName;
        else
            throw new FileNotFoundException("Could not find file (using Windows method); last hope was " + fileName);
    }
    
    /**
     * Uses the Windows layout of the Civilization III (and Conquests)
     * directories to search for files.  This method will be called for any OS
     * except Windows or Mac, as it will be assumed that anyone with Civ3
     * on Linux will be using the Windows version and Wine.
     * 
     * The difference between this version and the Windows version is that it
     * will try .PCX as well as .pcx, since Unix/Linux OS's tend to be case
     * sensitive.  Though they may succeeded with the Windows version if they
     * are using Wine or an attached NTFS partition to access the files, if
     * they are using their native filesystem they may well fail with the
     * Windows version.
     * @param name
     * @param folderStruct
     * @return
     */
    private static String findFileUnix(String name, String folderStruct, IO biq) throws FileNotFoundException
    {
        //first search for the file in all the scenario search folders listed
        for (int i = 0; i < biq.scenarioProperty.get(0).getSearchFolderLength(); i++)
        {
            String fileName = Main.settings.civInstallDir + Main.fileSlash + "Conquests" + Main.fileSlash + "Scenarios" + Main.fileSlash + biq.scenarioProperty.get(0).getSearchFolder(i) + Main.fileSlash + folderStruct + name;
            File file = new File(fileName);
            if (file.exists())
                return fileName;
            //try it with capital PCX
            fileName = fileName.substring(0, fileName.length() - 3) + "PCX";
            file = new File(fileName);
            if (file.exists())
                return fileName;
        }
        //if it doesn't exist in any of the custom directories, try the Conquests directory
        String fileName = Main.settings.civInstallDir + Main.fileSlash + "Conquests" + Main.fileSlash + folderStruct + name;
        File file = new File(fileName);
        if (file.exists())
            return fileName;
        fileName = fileName.substring(0, fileName.length() - 3) + "PCX";
        file = new File(fileName);
        if (file.exists())
            return fileName;

        //if it still doesn't exist, try the PTW directory
        fileName = Main.settings.civInstallDir + Main.fileSlash + "Civ3PTW" + Main.fileSlash + folderStruct + name;
        file = new File(fileName);
        if (file.exists())
            return fileName;
        fileName = fileName.substring(0, fileName.length() - 3) + "PCX";
        file = new File(fileName);
        if (file.exists())
            return fileName;
        
        
        //if it still doesn't exist, try the base Civ3 directory - last chance
        fileName = Main.settings.civInstallDir + Main.fileSlash + folderStruct + name;
        file = new File(fileName);
        if (file.exists())
            return fileName;
        fileName = fileName.substring(0, fileName.length() - 3) + "PCX";
        file = new File(fileName);
        if (file.exists())
            return fileName;
        //Civ3 vanilla installs its units folder as "units", not "Units".  This trips up Linux.
        //Yes, it's stupid, but it does.  I hate case-sensitivity in opening files.  Whoever
        //wants MyFile to be different than MyFIle, anyway?
        if (fileName.contains("Units"))
        {
            fileName = fileName.replaceAll("Units", "units");
            file = new File(fileName);
            if (file.exists())
                return fileName;
            fileName = fileName.substring(0, fileName.length() - 3) + "pcx";
            file = new File(fileName);
        }
        
        if (file.exists())
            return fileName;                
        else
            throw new FileNotFoundException("Could not find file (using UNIX method); last hope was " + fileName);
    }
    
    /**
     * Helper method to find a file when you have the full (civ-relative) path,
     * including file name and extension.  Calls out to the version that uses
     * separate path and extension.
     * TODO: Enhance to convert slashes as need be.
     * @param fullName The full name, e.g. art\leaderheads\AL_ANG.pcx
     * @param biq The BIQ file, used for the scenario search path
     * @return The name of the file, including via scenario search path.
     * @throws FileNotFoundException 
     */
    public static String findFile(String fullName, IO biq) throws FileNotFoundException {
        int slashDex = fullName.lastIndexOf(Main.fileSlash);
        return findFile(fullName.substring(slashDex + 1), fullName.substring(0, slashDex + 1), biq);
    }

        /**
     * This method finds a file that is somewhere within the Civ3 heirarchy.  It
     * takes into account a BIQ's scenario search folders, and with those in mind,
     * will first search the Conquests Scenarios folder for the file, then the base
     * Conquests install, the the base PTW install, and finally the base Civ3
     * install.
     * @param name - The file name, including extension (ex: ntp00.pcx)
     * @param folderStruct - The structure of the folder where this file should
     * reside.  This begins with the subfolder within the scenario folder (or
     * possibly within the base Civ3 install), and ends with a slash.  For
     * example, Art/Units/Palettes/ would be an example of a legal folderStruct.
     * This method will take care of searching for the file within all possible
     * locations.
     * @param biq - A handle to the BIQ file in question, so this method can
     * look in all its scenario search folders.
     * @return - The full name of the file, which the caller of this method can
     * then use to open said file.
     * @throws FileNotFoundException
     */
    public static String findFile(String name, String folderStruct, IO biq) throws FileNotFoundException
    {
        if (Main.os.name.toLowerCase().contains("mac") || Main.os.name.toLowerCase().contains("os x"))
        {
            try{
                return findFileOSX(name, folderStruct, biq);
            }
            catch(FileNotFoundException e){
                throw e;
            }
        }
        else if (Main.os.name.toLowerCase().contains("windows"))
        {
            try{
                return findFileWindows(name, folderStruct, biq);
            }
            catch (FileNotFoundException e){
                throw e;
            }
        }
        else    //currently Linux and Unix, but was added for SunOS/OpenSolaris in particular 
        {
            
            try{
                return findFileUnix(name, folderStruct, biq);
            }
            catch (FileNotFoundException e){
                throw e;
            }
        }
    }
        /**
     * Uses the OSX layout of the Civilization III (and Conquests)
     * directories to search for files.  This method will be called only for
     * OSX.
     * @param name
     * @param folderStruct
     * @return
     */
    private static String findFileOSX(String name, String folderStruct, IO biq) throws FileNotFoundException
    {
        //first search for the file in all the scenario search folders listed
        for (int i = 0; i < biq.scenarioProperty.get(0).getSearchFolderLength(); i++)
        {
            String fileName = Main.settings.civInstallDir + Main.fileSlash + "Conquests Game Data" + Main.fileSlash + "Scenarios" + Main.fileSlash + biq.scenarioProperty.get(0).getSearchFolder(i) + Main.fileSlash + folderStruct + name;
            File file = new File(fileName);
            if (file.exists())
                return fileName;
        }
        //however, some users may have their scenario search folders in a different
        //location (this appears to be due to installing only for one user)
        for (int i = 0; i < biq.scenarioProperty.get(0).getSearchFolderLength(); i++)
        {
            String fileName = "~/Documents/Civilization 3 Conquests/Scenarios" + Main.fileSlash + biq.scenarioProperty.get(0).getSearchFolder(i) + Main.fileSlash + folderStruct + name;
            File file = new File(fileName);
            if (file.exists())
                return fileName;
        }
        //if it doesn't exist in any of the custom directories, try the Conquests directory
        String fileName = Main.settings.civInstallDir + Main.fileSlash + "Conquests Game Data" + Main.fileSlash + folderStruct + name;
        File file = new File(fileName);
        if (file.exists())
            return fileName;

        //if it still doesn't exist, try the PTW directory
        fileName = Main.settings.civInstallDir + Main.fileSlash + "Play the World Game Data" + Main.fileSlash + folderStruct + name;
        file = new File(fileName);
        if (file.exists())
            return fileName;
        //if it still doesn't exist, try the base Civ3 directory - last chance
        fileName = Main.settings.civInstallDir + Main.fileSlash + "Civilization 3 Game Data" + Main.fileSlash + folderStruct + name;
        file = new File(fileName);
        if (file.exists())
            return fileName;
        else
            throw new FileNotFoundException("Could not find file (using OSX version); last hope was " + fileName);
    }

    /**
     * Verifies that there appears to be a legit Civ3 install at a specified
     * location.
     * This is mainly to allow for auto-detect when reg query is not available
     * (e.g. on Mac, Linux, Solaris).
     * @return
     */
    public static boolean verifyGoodInstall(String installDir)
    {
        String path = installDir + Main.fileSlash;
        logger.info("path for install: " + path);
        File f;
        if (Main.os.name.toLowerCase().contains("mac"))
        {
            String civ3BasePath = path + "Civilization 3 Game Data";
            f = new File(civ3BasePath);
            if (!f.exists())
            {
                logger.warn(civ3BasePath + autoDetectFail);
                return false;
            }
            String ptwBasePath = path + "Play the World Game Data";
            f = new File(ptwBasePath);
            if (!f.exists())
            {
                logger.warn(ptwBasePath + autoDetectFail);
                return false;
            }
            String conquestsBasePath = path + "Conquests Game Data";
            f = new File(conquestsBasePath);
            if (!f.exists())
            {
                logger.warn(conquestsBasePath + autoDetectFail);
                return false;
            }
            String mainArtPath = civ3BasePath + Main.fileSlash + "Art";
            f = new File(mainArtPath);
            if (!f.exists())
            {
                logger.warn(mainArtPath + autoDetectFail);
                return false;
            }
            String mainSoundsPath = civ3BasePath + Main.fileSlash + "Sounds";
            f = new File(mainSoundsPath);
            if (!f.exists())
            {
                logger.warn(mainSoundsPath + autoDetectFail);
                return false;
            }
            String mainTextPath = civ3BasePath + Main.fileSlash + "Text";
            f = new File(mainTextPath);
            if (!f.exists())
            {
                logger.warn(mainTextPath + autoDetectFail);
                return false;
            }
        }
        else
        {
            String ptwBasePath = path + "Civ3PTW";
            f = new File(ptwBasePath);
            if (!f.exists())
            {
                logger.warn(ptwBasePath + autoDetectFail);
                return false;
            }
            String conquestsBasePath = path + "Conquests";
            f = new File(conquestsBasePath);
            if (!f.exists())
            {
                logger.warn(conquestsBasePath + autoDetectFail);
                return false;
            }
            String mainArtPath = path + "Art";
            f = new File(mainArtPath);
            if (!f.exists())
            {
                logger.warn(mainArtPath + autoDetectFail);
                return false;
            }
            String mainSoundsPath = path + "Sounds";
            f = new File(mainSoundsPath);
            if (!f.exists())
            {
                logger.warn(mainSoundsPath + autoDetectFail);
                return false;
            }
            String mainTextPath = path + "Text";
            f = new File(mainTextPath);
            if (!f.exists())
            {
                logger.warn(mainTextPath + autoDetectFail);
                return false;
            }
        }
        return true;
    }
    
    /**
     * Handles the situation where you want a combo box to be able to handle
     * duplicate elements properly.  What it does is adds spaces to the duplicate
     * elements.  This will require the GUI to set a maximum size if there are
     * a large number of duplicates.
     * Not quite as handy as rewiring the entire JComboBox, but also less drastic.
     * @param toAdd - The String to be added to the combo box
     * @param dcbm - The combo box's model
     */
    public static void addWithPossibleDuplicates(String toAdd, DefaultComboBoxModel dcbm)
    {
        while (dcbm.getIndexOf(toAdd) != -1)
        {
            toAdd = toAdd + " ";
        }
        dcbm.addElement(toAdd);
    }
    
    public static void renameItemInComboBox(int indexInModel, String newName, DefaultComboBoxModel dcbm) {
        boolean renamingSelectedItem = dcbm.getElementAt(indexInModel).equals(dcbm.getSelectedItem());
        dcbm.removeElementAt(indexInModel);
        dcbm.insertElementAt(newName, indexInModel);
        if (renamingSelectedItem) {
            dcbm.setSelectedItem(newName);
        }
    }
    
    /**
     * "Trims" a string in the manner that C would, cutting off everything after
     * the first null character.
     * @param s
     * @return 
     */
    public static String cTrim(String s)
    {
        StringBuilder newString = new StringBuilder();
        for (int i =0; i < s.length(); i++)
        {
            if (!(s.charAt(i) == '\0'))
                newString.append(s.charAt(i));
            else
                break;
        }
        return newString.toString();
    }
    
    /**
     * Converts an Integer array to a primitive int array
     * @param array Integer array
     * @return int version of the same array
     */
    public static int[] integerArrayToIntArray(Integer[] array)
    {
        int[] newArray = new int[array.length];
        for (int i = 0; i < array.length; i++)
        {
            newArray[i] = array[i];
        }
        return newArray;
    }
    
    /**
     * "Splices" a search folder into a Firaxis path.  As an example, consider if it
     * takes D:\Civilization III\Art\Units\Palettes\ntp00.pcx, and MyAwesomeMod as
     * parameters.  It would then return D:\Civilization III\Conquests\Scenarios\MyAwesomeMod\Art\Units\Palettes\ntp00.pcx,
     * since this would be the path where that file should be stored for that mod.
     * This probably will require Mac and Windows versions.
     * @param firaxisPath
     * @return 
     */
    public static String spliceInSearchFolder(String firaxisPath, String searchFolder) {
        //First, see if we have a Conquests path
        String conquestsFolder = getConquestsFolder(Main.settings.civInstallDir);
        int index = firaxisPath.indexOf(conquestsFolder);
        if (index == 0) {
            //Conquests Firaxis path
            int introLength = conquestsFolder.length();
            String firaxisFileSpecificPath = firaxisPath.substring(introLength);
            return conquestsFolder + "Scenarios" + Main.fileSlash + searchFolder + Main.fileSlash + firaxisFileSpecificPath;
        }
        String ptwFolder = getPTWFolder(Main.settings.civInstallDir);
        index = firaxisPath.indexOf(ptwFolder);
        if (index == 0) {
            //PTW Firaxis path
            int introLength = ptwFolder.length();
            String firaxisFileSpecificPath = firaxisPath.substring(introLength);
            return conquestsFolder + "Scenarios" + Main.fileSlash + searchFolder + Main.fileSlash + firaxisFileSpecificPath;
        }
        String vanillaFolder = getVanillaFolder(Main.settings.civInstallDir);
        index = firaxisPath.indexOf(vanillaFolder);
        if (index == 0) {
            //Vanilla Firaxis path
            int introLength = vanillaFolder.length();
            String firaxisFileSpecificPath = firaxisPath.substring(introLength);
            return conquestsFolder + "Scenarios" + Main.fileSlash + searchFolder + Main.fileSlash + firaxisFileSpecificPath;          
        }
        return firaxisPath;
    }
    
    public static boolean copyFile(String source, String destination) {
        //Directory-building
        int lastSlash = destination.lastIndexOf(Main.fileSlash);
        String destinationFolder = destination.substring(0, lastSlash);
        File folder = new File(destinationFolder);
        if (!folder.exists()) {
            boolean directoriesMade = folder.mkdirs();
            if (!directoriesMade) {
                logger.error("Could not create directories for scenario PCX files");
                return false;
            }
        }
        //Main code
        File in = new File(source);
        File out = new File(destination);
        try {
            FileInputStream fis = new FileInputStream(in);
            FileOutputStream fos = new FileOutputStream(out);
            byte[]buffer = new byte[1024];
            int bytesRead = 0;
            while ((bytesRead = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, bytesRead);
            }
            fis.close();
            fos.close();
        }
        catch(FileNotFoundException ex) {
            logger.error("File not found", ex);
            return false;
        }
        catch(IOException ex) {
            logger.error("IOException in file copying", ex);
        }
        return true;
    }
    
    /**
     * Returns the path to which Conquests is located.  Includes a trailing file slash
     * of system-appropriate direction.  Ex. D:\Civilization III\Conquests\
     * Ex. /Applications/Sid Meier's Civilization III/Conquests Game Data/
     * @param installDir The path where the base Civ3 is installed.
     * @return The path where the Conquests data is located.
     */
    public static String getConquestsFolder(String installDir) {
        if (Main.os.name.toLowerCase().contains("mac") || Main.os.name.toLowerCase().contains("os x"))
        {
            return installDir + Main.fileSlash + "Conquests Game Data" + Main.fileSlash;
        }
        else if (Main.os.name.toLowerCase().contains("windows"))
        {
            return installDir + Main.fileSlash + "Conquests" + Main.fileSlash;
        }
        else    //currently Linux and Unix, but was added for SunOS/OpenSolaris in particular 
        {
            return installDir + Main.fileSlash + "Conquests" + Main.fileSlash;
        }
    }
    
    /**
     * Returns the path to which PTW is located.  Includes a trailing file slash
     * of system-appropriate direction.  Ex. D:\Civilization III\Civ3PTW\
     * Ex. /Applications/Sid Meier's Civilization III/Play the World Game Data/
     * @param installDir The path where the base Civ3 is installed.
     * @return The path where the PTW data is located.
     */
    public static String getPTWFolder(String installDir) {
        if (Main.os.name.toLowerCase().contains("mac") || Main.os.name.toLowerCase().contains("os x"))
        {
            return installDir + Main.fileSlash + "Play the World Game Data" + Main.fileSlash;
        }
        else if (Main.os.name.toLowerCase().contains("windows"))
        {
            return installDir + Main.fileSlash + "Civ3PTW" + Main.fileSlash;
        }
        else    //currently Linux and Unix, but was added for SunOS/OpenSolaris in particular 
        {
            return installDir + Main.fileSlash + "Civ3PTW" + Main.fileSlash;
        }
    }
    
    
    /**
     * Returns the path to which Vanilla is located.  Includes a trailing file slash
     * of system-appropriate direction.  Ex. D:\Civilization III\
     * Ex. /Applications/Sid Meier's Civilization III/Civilization 3 Game Data/
     * @param installDir The path where the base Civ3 is installed.
     * @return The path where the vanilla data is located.
     */
    public static String getVanillaFolder(String installDir) {
        if (Main.os.name.toLowerCase().contains("mac") || Main.os.name.toLowerCase().contains("os x"))
        {
            return installDir + Main.fileSlash + "Civilization 3 Game Data" + Main.fileSlash;
        }
        else if (Main.os.name.toLowerCase().contains("windows"))
        {
            return installDir + Main.fileSlash;
        }
        else    //currently Linux and Unix, but was added for SunOS/OpenSolaris in particular 
        {
            return installDir + Main.fileSlash;
        }
    }
    
    public static boolean isInScenarioFolder(String name, String folderStruct, IO biq)
    {
        if (Main.os.name.toLowerCase().contains("mac") || Main.os.name.toLowerCase().contains("os x"))
        {
            return isInScenarioFolderOSX(name, folderStruct, biq);
        }
        else if (Main.os.name.toLowerCase().contains("windows"))
        {
            return isInScenarioFolderWindows(name, folderStruct, biq);
        }
        else    //currently Linux and Unix, but was added for SunOS/OpenSolaris in particular 
        {
            return isInScenarioFolderUnix(name, folderStruct, biq);
        }
    }
    
    private static boolean isInScenarioFolderWindows(String name, String folderStruct, IO biq)
    {
        //first search for the file in all the scenario search folders listed
        for (int i = 0; i < biq.scenarioProperty.get(0).getSearchFolderLength(); i++)
        {
            String fileName = Main.settings.civInstallDir + Main.fileSlash + "Conquests" + Main.fileSlash + "Scenarios" + Main.fileSlash + biq.scenarioProperty.get(0).getSearchFolder(i) + Main.fileSlash + folderStruct + name;
            File file = new File(fileName);
            if (file.exists())
                return true;
        }
        return false;
    }
    
    private static boolean isInScenarioFolderOSX(String name, String folderStruct, IO biq)
    {
        //first search for the file in all the scenario search folders listed
        for (int i = 0; i < biq.scenarioProperty.get(0).getSearchFolderLength(); i++)
        {
            String fileName = Main.settings.civInstallDir + Main.fileSlash + "Conquests Game Data" + Main.fileSlash + "Scenarios" + Main.fileSlash + biq.scenarioProperty.get(0).getSearchFolder(i) + Main.fileSlash + folderStruct + name;
            File file = new File(fileName);
            if (file.exists())
                return true;
        }
        //however, some users may have their scenario search folders in a different
        //location (this appears to be due to installing only for one user)
        for (int i = 0; i < biq.scenarioProperty.get(0).getSearchFolderLength(); i++)
        {
            String fileName = "~/Documents/Civilization 3 Conquests/Scenarios" + Main.fileSlash + biq.scenarioProperty.get(0).getSearchFolder(i) + Main.fileSlash + folderStruct + name;
            File file = new File(fileName);
            if (file.exists())
                return true;
        }
        return false;
    }
    
    private static boolean isInScenarioFolderUnix(String name, String folderStruct, IO biq)
    {
        //first search for the file in all the scenario search folders listed
        for (int i = 0; i < biq.scenarioProperty.get(0).getSearchFolderLength(); i++)
        {
            String fileName = Main.settings.civInstallDir + Main.fileSlash + "Conquests" + Main.fileSlash + "Scenarios" + Main.fileSlash + biq.scenarioProperty.get(0).getSearchFolder(i) + Main.fileSlash + folderStruct + name;
            File file = new File(fileName);
            if (file.exists())
                return true;
            //try it with capital PCX
            fileName = fileName.substring(0, fileName.length() - 3) + "PCX";
            file = new File(fileName);
            if (file.exists())
                return true;
        }
        return false;
    }
    
    
    /**
     * Recursively copies a directory, including any sub-directories.  Ex.
     * copy("C:\\Windows\\", "M:\\WindowsBackup");
     * @param source The source directory to be copied.
     * @param destination The destination directory.
     * @param deleteSource Whether the source folder should be deleted.
     * @throws IOException 
     */
    public static void copy(String source, String destination, boolean deleteSource) throws IOException {
        System.out.println("\nLocation: " + source);
        File destFile = new File(destination);
        if (!destFile.exists()) {
            Files.createDirectory(Paths.get(destination));
        }
        Path path = Paths.get(source);
        if (Files.isDirectory(path)) {
            //Create target directory
            
            DirectoryStream<Path> stream = Files.newDirectoryStream(path);
            Iterator<Path> i = stream.iterator();
            while (i.hasNext()) {
                Path file = i.next();
                Path target = Paths.get(destination + "\\" + file.getFileName());
                if (!Files.isDirectory(file)) {
                    //copy
                    System.out.print(".");
                    Files.copy(file, target, StandardCopyOption.REPLACE_EXISTING);
                    if (deleteSource) {
                        Files.delete(file);
                    }
                }
                else {
                    //create target directory
                    if (!Files.exists(Paths.get(destination + "\\" + file.getFileName()))) {
                        Files.createDirectory(Paths.get(destination + "\\" + file.getFileName()));
                    }
                    //recurse
                    copy(source + "\\" + file.getFileName(), destination + "\\" + file.getFileName(), deleteSource);
                }
            }
        }
        else {
            
        }
        
        if (deleteSource) {
            Files.delete(Paths.get(source));
        }
    }
    
    
    public static String getItemName(String title, String header, String instructions, String defaultText) {
        
        //Icon.  See http://code.makery.ch/blog/javafx-dialogs-official/
        //Note that we need a JavaFX Icon :(
        /**
        Stage s = (Stage)addUnitPopup.getDialogPane().getScene().getWindow();
        Image dialogIcon;
        SwingFXUtils.toFXImage(Main.icon, dialogIcon);
        s.getIcons().add(dialogIcon);
        * **/
        
        TextInputDialog addUnitPopup = new TextInputDialog(defaultText);
        addUnitPopup.setTitle(title);
        addUnitPopup.setHeaderText(header);
        addUnitPopup.setContentText(instructions);

        for (;;) {
            Optional<String> result = addUnitPopup.showAndWait();

            if (result.isPresent()) {
                String unitName = result.get();
                if (unitName.length() > 31) {
                    //too long
                    Alert tooLongAlert = new Alert(Alert.AlertType.ERROR, "The name is longer than the max length of 31 characters.  Please enter a shorter name.");
                    tooLongAlert.setHeaderText("Name too long");
                    tooLongAlert.setTitle("Name too long");
                    tooLongAlert.showAndWait();
                }
                else {
                    if (unitName.length() > 0) {
                        return unitName;
                    }
                    break;
                }
            }
            else {
                break;
            }
        }
        return "";
    }
    
    public static HttpURLConnection getHttpConnection(String url) throws IOException {
        URL theUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection)theUrl.openConnection();
        conn.connect();
        return conn;
    }
    
    public static void downloadFromHttpConnection(HttpURLConnection conn, String fileName) throws IOException {
        
        InputStream is = conn.getInputStream();
        BufferedInputStream reader = new BufferedInputStream(is);
        File file = new File(fileName);
                        FileOutputStream fos = new FileOutputStream(file);
        int bytesDownloaded = 0;

        byte[] buffer = new byte[1024];
        for (;;) {
            int bytesRead = reader.read(buffer);
            if (bytesRead > -1) {
                fos.write(buffer, 0, bytesRead);
                bytesDownloaded+=bytesRead;
                //updateProgress(bytesDownloaded, downloadSize);
            }
            else {
                break;
            }
        }
        fos.close();
        is.close();
        conn.disconnect();
    }
    
    public static void addFileToRecentFiles(String pathToBin, String biq) {
        if (Main.os.name.contains("Windows")) {
            try {
                String[] query = {pathToBin + "./bin/win/AddToRecentDocs.exe", biq};
                Process dcp = Runtime.getRuntime().exec(query);
                dcp.waitFor();
            }
            catch(InterruptedException | IOException ex) {
                logger.info("Unable to add BIQ " + biq + " to recent documents list");
            }
        }
    }
}
