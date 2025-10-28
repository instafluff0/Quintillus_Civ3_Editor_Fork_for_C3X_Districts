/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.xplatformeditor.download;

import com.civfanatics.civ3.biqFile.IO;
import com.civfanatics.civ3.xplatformeditor.Main;
import com.civfanatics.civ3.xplatformeditor.UnitTab;
import com.civfanatics.civ3.xplatformeditor.utils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.TimerTask;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.log4j.Logger;

/**
 * FXML Controller class
 *
 * @author Andrew
 */
public class RenameUnitWindowController implements Initializable {
    
    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private TextField txtName;
    
    @FXML
    private ProgressBar prgDownload;
    
    @FXML
    private Button cmdAddToScenario;
    
    @FXML
    private Label lblDownloadStatus;
    
    private String decompressionMethod = "";
    private String initialFileName = "";
    /**
     * The folder to which we will decompress with 7z.  Is not the full path.
     * Used so we know where to copy from (to the final name) later.
     */
    private String sevenZFolderName= "";
    private String scenarioFolder = "";
    private IO biq;
    private UnitTab unitTab;
    
    static Logger logger = Logger.getLogger("RenameUnitWindowController");
    
    public void setDecompressionMethod(String string) {
        decompressionMethod = string;
    }
    
    /**
     * Sets the name of the downloaded file; the "initial name".  This is used
     * by the decompressor to decompress the proper file.
     * @param fileName 
     */
    public void setFileName(String fileName) {
        this.initialFileName = fileName;
        //Set the name to the download, without the extension        
        int dot = fileName.lastIndexOf(".");
        String unitName = fileName.substring(fileName.lastIndexOf(Main.fileSlash) + 1, dot);
        unitName = unitName.substring(0, 1).toUpperCase() + unitName.substring(1);
        this.sevenZFolderName = unitName;
            
        if (txtName.getText().isEmpty()) {
            txtName.setText(unitName);
        }
        
        //Also do other stuff
        cmdAddToScenario.setDisable(false);
        
        lblDownloadStatus.setText("The unit has been successfully downloaded.  Enter the name for your unit below, and it will be added to your scenario folder.");
        
        //Test: Add another row
        /**
         * Seems to basically work.  Will need to:
         * 1.  Figure out how much space we'll need
         * 2.  Make sure we have a scroll area in case there's a lot of 'em
         * 3.  Make sure it scales properly if display scaling is enabled.
         */
        /**
         * Todo: Redo since I've made the layout flexible.  May wait until after
         * initial release, with caveat that unit packs won't work right away.
        Label lblTwo = new Label("Second Label");
        lblTwo.setLayoutX(18.0);
        lblTwo.setLayoutY(161.0);
        TextField txtNameTwo = new TextField();
        txtNameTwo.setLayoutX(248.0);
        txtNameTwo.setLayoutY(157.0);
        cmdAddToScenario.setLayoutX(cmdAddToScenario.getLayoutX() + 20);
        anchorPane.getChildren().add(lblTwo);
        anchorPane.getChildren().add(txtNameTwo);
        * 
        * **/
        
    }
    
    public void setScenarioFolder(String fileName) {
        this.scenarioFolder = fileName;
        File file = new File(fileName);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
    
    public void setBiqLink(IO biq) {
        this.biq = biq;
    }
    
    public void setUnitTab(UnitTab unitTab) {
        this.unitTab = unitTab;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    /**
     * This action takes place when the user approves the new name; the unit is
     * then added to the scenario.
     * @param event 
     */
    @FXML
    private void addToScenario(ActionEvent event) {
        String name = txtName.getText();
        
        String folderName = "";
        try {
            folderName = decompressDownload();
        }
        catch(Exception ex) {
            lblDownloadStatus.setText("An unexpected error, " + ex.getMessage() + " occurred while downloading.  The log will have details.  Please report the error and which unit you were trying to download to the CFC thread.");
            logger.error("Blew up while adding to scenario", ex);
        }
        
        boolean successfulCopy = copyToScenarioFolder(name, folderName);
        
        if (successfulCopy) {
            //Add the unit to the scenario itself
            unitTab.addItem(name, true);

            lblDownloadStatus.setText("The unit has been successfully added.  This window will close in 3 seconds.");

            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(3000), (ev) -> {
                Stage stage = (Stage)lblDownloadStatus.getScene().getWindow();
                stage.close();
            }));
            timeline.play();
        }
    }
    
    public void setDownloadTask(Task task) {
        prgDownload.progressProperty().bind(task.progressProperty());
    }
    
    public static boolean sevenZipBin = false;
    
    public static boolean test7Zip() {
        try {
            /**
            Stream<Path> files = Files.walk(Paths.get("./bin/7z/"), FileVisitOption.FOLLOW_LINKS);
            Iterator<Path> i = files.iterator();
            while (i.hasNext()) {
                Path p = i.next();
                logger.info("path info: " + p.getFileName());
            }
            * **/
            Process sevenZipTest = null;
            try {
                sevenZipTest = Runtime.getRuntime().exec("7z");
            }
            catch(IOException ex) {
                logger.info("Could not run 7-Zip from path; trying from bin folder");
                try {
                    sevenZipTest = Runtime.getRuntime().exec("bin/7z/7z");
                    sevenZipBin = true;
                    Main.settings.decompressionMethod = "bin/7z/7z x [filename] -y -o";
                }
                catch(IOException ex2) {
                    logger.warn("Could not run 7-zip from path or bin folder");
                    return false;
                }
            }
            InputStream decompressResults = sevenZipTest.getInputStream();
            sevenZipTest.waitFor();
            
             //Read the output from our decompression utility.
            int bytes = decompressResults.available();
            byte[] decompressBytes = new byte[bytes];
            decompressResults.read(decompressBytes);
            String decompressionResults = new String(decompressBytes, "ISO-8859-1");
            
            if (decompressionResults.contains("not recognized as an internal or external command")) {
                return false;
            }
            else if (decompressionResults.contains("7-Zip")) {
                return true;
            }
            //Else, did something else that we didn't expect
            return false;
        }
        catch(IOException|InterruptedException ex) {
            logger.error("Error while testing 7zip", ex);
            return false;
        }
    }
        
    /**
     * Decompresses the download.  This is handed off to an external program - 
     * currently, 7zip (or p7zip on Linux/OS X) is supported.
     * 
     * @return The name of the folder of the download.  This allows the copy method to copy the correct folder.
     */
    private String decompressDownload() throws Exception {
        //Need to figure out how to decompress it
        //  7z x D:\temp\koben_class_ss.rar -oD:\Civilization III\Conquests\Scenarios\My Scenarios\Art\Units\[name]
        String[] commands = decompressionMethod.split(" ");
        String full7zFolder = "";
        for (int i = 0; i < commands.length; i++) {
            if (commands[i].equals("[filename]")) {
                commands[i] = initialFileName;
            }
            else if (commands[i].equals("-o")) {
                //Escape the output path since it's very possible that there
                //will be spaces in the path.
                //We actually have to double-quote it twice for it to work when there
                //are spaces in the name.  Not 100% sure why, but it appears to be specific to the
                //way the command processor works.  This *may* vary by platform.
                if (Main.os.name.toLowerCase().contains("windows")) {
                    full7zFolder = scenarioFolder + "art" + Main.fileSlash + "Units" + Main.fileSlash + sevenZFolderName + Main.fileSlash;
                    File fileFolder = new File(full7zFolder);
                    if (!fileFolder.exists()) {
                        fileFolder.mkdirs();
                    }
                    commands[i] = commands[i] + "\"\"" + full7zFolder +"\"\"";
                }
                else {
                    full7zFolder = scenarioFolder + "art" + Main.fileSlash + "Units" + Main.fileSlash + sevenZFolderName + Main.fileSlash;
                    commands[i] = commands[i] + "\"" + full7zFolder + "\"";
                }
            }
        }
        logger.debug("Full 7-Zip folder: " + full7zFolder);
        
        //TODO: Need to either make 7zip execute in the directory where
        //we want it extracted to, or figure out the output parameter... would be
        //nice to make it automatic to the scenario directory, since that's
        //easier for the user to set up.
        
        //Note: On OS X, the following may work:
        // open sms_adler_class.rar (depends on whether supporting software is installed; launches GUI; not ideal)
        // http://superuser.com/questions/548349/how-can-i-install-7zip-so-i-can-run-it-from-terminal-on-os-x/548387#548387
        //   answer two with homebrew and 7zip
        // The Superuser route is highly recommended.  It's both easier to set up than keka/unar, and guaranteed to have a wide
        // variety of format support.  In practice, I was unable to get keka working properly, not could I get unar working from
        // the command line (though someone with more experience in OS X may have been able to).  The brew method, however, worked
        // quite well.  Full comment from Superuser:
        
        /**
         * To install p7zip using Homebrew, first update your brew formulae to be sure you are getting the latest p7zip.
            $ brew update

            Use Homebrew to install p7zip:
            $ brew install p7zip

            Add all files in the sputnik directory to the compressed file heed.7z:
            $ 7z a heed.7z sputnik

            Unzip heed.7z:
            $ 7z x heed.7z
         * 
         * And, to install brew:
         * ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
         */
        //With that, the same command and same format support can be achieved on OS X as on Windows.
        
        try{
            Process decompression = Runtime.getRuntime().exec(commands, null, null);
            InputStream decompressResults = decompression.getInputStream();
            decompression.waitFor();
            
            //Read the output from our decompression utility.
            int bytes = decompressResults.available();
            byte[] decompressBytes = new byte[bytes];
            decompressResults.read(decompressBytes);
            String decompressionResults = new String(decompressBytes, "ISO-8859-1");
            
            
            System.out.println("Decompression command: ");
            System.out.println(commands[0] + " " + commands[1] + " " + commands[2] + " " + commands[3]);
            
            System.out.println("Decompression results: " + decompressionResults);
            return full7zFolder;
        }
        catch(IOException ex) {
            lblDownloadStatus.setText("An IO error, " + ex.getMessage() + " occurred while decompressing the download.  The log will have details.  Please report the error and which unit you were trying to download to the CFC thread.");
            logger.error("Blew up while decompressing", ex);
            throw ex;
        }
        catch(InterruptedException ex) {
            lblDownloadStatus.setText("An Interrupted Exception, " + ex.getMessage() + " occurred while decompressing the download.  The log will have details.  Please report the error and which unit you were trying to download to the CFC thread.");
            logger.error("Interrupted exception while decompressing", ex);
            throw ex;
        }
    }
    
    /**
     * Copies the downloaded unit into the scenario folder.  We rely on the folder name
     * to be passed in (from processing 7zip results), since neither creation nor
     * modified time is necessarily accurate in telling which folder is the newest.
     * Both creation and modified time may be saved in the archive; if so, that's
     * what the 7zip-created folder receives.  And in such a case, it is not unlikely
     * that the newly-unzipped folder does not actually have the most recent
     * modified and/or created time stamp.
     * @param name The user's desired unit name
     * @param folderName The folder to which the unit has been decompressed
     */
    private boolean copyToScenarioFolder(String name, String folderName) {
        //At this point the unit will have been downloaded to a folder in the sceario's search path,
        //but the name will be based on its original name, not the desired name.  We need to rename
        //both the folder, and the files within it, so everything will work properly.
        //We shall identify the folder to rename by figuring out the file with the most recent last-modified date
        try {
            Path desiredPath = Paths.get(scenarioFolder + "art" + Main.fileSlash + "Units" + Main.fileSlash + name + Main.fileSlash);

            //TODO: The old name should really be based on the folder within the RAR, which we'd have to use WinRAR to figure out... or the output of 
            //the WinRAR decompression call
//            int lastSlash = initialFileName.lastIndexOf(Main.fileSlash);
//            int lastDot = initialFileName.lastIndexOf(".");
//            String oldName = initialFileName.substring(lastSlash + 1, lastDot);
            Path tempPath = Paths.get(folderName);
            
            Path toCopy = null;
            FileTime mostRecentModified = FileTime.fromMillis(0);
            
            DirectoryStream<Path> stream = Files.newDirectoryStream(tempPath);
            Iterator<Path> i = stream.iterator();
            int count = 0;
            boolean nestedFolder = true;
            while (i.hasNext()) {
                Path file = i.next();
                count++;
                if (Files.isDirectory(file)) {
                    nestedFolder = true;
                    toCopy = file;
                    /**
                        //note modification time
                        BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);
                        FileTime modificationTime = attrs.lastModifiedTime();

                        if (modificationTime.compareTo(mostRecentModified) > 0) {
                            mostRecentModified = modificationTime;
                            toCopy = file;
                            System.out.println("Most recent file: " + toCopy);
                        }
                     **/
                }
            }
            stream.close();
            
            //If the .7z/.rar/.zip had a nested folder, detect that; we'll need to remove a level of nesting.
            if (count == 1 && nestedFolder) {
                toCopy = toCopy;
            }
            else {
                //There was no folder nesting.
                toCopy = tempPath;
            }
            
            if (toCopy == null) {
                logger.error("Was not able to find extracted unit, decompression may have failed");
                return false;
            }
            
            //If the source and destination are the same, that means the desired
            //name is the same as what was decompressed by default; nothing else
            //to do.
            //TODO: Verify the INI file matches.  It should, if the unit was
            //well-packaged, but it can't hurt to verify.
            if (toCopy.equals(desiredPath)) {
                return true;
            }
            
            //We now have the Path that was most recently modified
            //Only now should we create our new directory (otherwise, it will be the most recently created)
            if (!desiredPath.toFile().exists()) {
                Files.createDirectory(desiredPath);
            }
            
            String originalUnitName = toCopy.getFileName().toString();
            String originalINI = originalUnitName + ".ini";
            
            DirectoryStream<Path> copyStream = Files.newDirectoryStream(toCopy);
            i = copyStream.iterator();
            while (i.hasNext()) {
                Path file = i.next();
                if (!file.toFile().isDirectory()) {
                    if (file.getFileName().toString().toLowerCase().equals(originalINI.toLowerCase())) {
                        //Rename the INI file.
                        Path target = Paths.get(desiredPath.toString() + Main.fileSlash + name + ".ini");
                        if (!Files.isDirectory(file)) {
                            if (!target.toFile().exists()) {
                                Files.copy(file, target);
                            }
                            Files.delete(file);
                        }
                    }
                    else {
                        Path target = Paths.get(desiredPath.toString() + Main.fileSlash + file.getFileName());
                        if (!Files.isDirectory(file)) {
                            if (!target.toFile().exists()) {
                                Files.copy(file, target);
                            }
                            try {
                                Files.delete(file);
                            }
                            catch(IOException ex) {
                                //Probably read-only
                                DosFileAttributes dosAttributes = Files.readAttributes(file, DosFileAttributes.class);
                                if (dosAttributes.isReadOnly()) {
                                    Files.setAttribute(file, "dos:readonly", false);
                                    Files.delete(file);
                                }
                            }
                        }
                    }
                }
                else {
                    Path target = Paths.get(desiredPath.toString() + Main.fileSlash + file.getFileName());
                    utils.copy(file.toString(), target.toString(), true);
                }
            }
            copyStream.close();
            
            //Finally, delete the source folder
            if (count == 1 && nestedFolder) {
                Files.delete(toCopy);
                //Only delete the parent folder if we aren't copying to the parent folder.
                //We copy to the parent folder when there's a nested folder in the download, and the user chooses to stick with the default unit name
                if (!toCopy.getParent().equals(desiredPath)) {
                    Files.delete(toCopy.getParent());
                }
            }
            else {
                Files.delete(toCopy);
            }
        }
        catch(IOException ex) {
            lblDownloadStatus.setText("An IO error, " + ex.getMessage() + " occurred while copying files to the scenario folder.  The log will have details.  Please report the error and which unit you were trying to download to the CFC thread.");
            logger.error("Error when copying files", ex);
            return false;
        }
        return true;
    }
    
}
