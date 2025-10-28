
package com.civfanatics.civ3.xplatformeditor;

import static com.civfanatics.civ3.xplatformeditor.Main.fileChooserMode;
import static com.civfanatics.civ3.xplatformeditor.Main.logger;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.application.Platform;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javax.swing.JFileChooser;

/**
 * Handles file I/O, including all the various implementations (Swing, JavaFX, and AWT).
 * Centralizes and thus simplifies the code.
 * 
 * Also optimizes performance.  For some reason, initializing the JFileChooser in particular
 * takes a long time.  By only initializing the file choosers when needed, we save about 70 ms
 * of program load time (270 ms to 200 ms with a 4 GHz i5 2500k).  I suspect it is unnecessarily
 * hitting the disk when it initializes.
 * @author Andrew
 */
public class FileIO {
    
    private static boolean firstRequest = true;
    public static JFileChooser jfcBIQChooser;
    public static FileChooser jfxFileChooser;
    public static FileDialog biqFileDialog;
    
    private static File currentDirectory;
    
    static File returnFile;
    
    public static void setSelectedFileName(String fileName) {
        File file = new File(fileName);
        jfcBIQChooser.setSelectedFile(file);
        jfxFileChooser.setInitialFileName(fileName);
        biqFileDialog.setDirectory(file.getPath());
    }
    
    public static File getFile(int mode, boolean chooseDirectory, FileExtensionFilter[]fif) throws IOException {
        return getFile(mode, chooseDirectory, fif, null, false);
    }
    
    
    public static File getFile(int mode, boolean chooseDirectory, FileExtensionFilter[]fif, String buttonLabel) throws IOException {
        return getFile(mode, chooseDirectory, fif, buttonLabel, false);
    }
    
    /**
     * Internally sets the current directory.  Doesn't really do it until needed.
     * @param directory 
     */
    public static void setCurrentDirectory(File directory) {
        currentDirectory = directory;
    }
    
    /**
     * General-purpose file-fetching method that will call out to Swing or AWT
     * appropriately to fetch the file.  This compartmentalizes the Swing/AWT
     * differences to this method.
     * @param mode Either FileDialog.LOAD or FileDialog.SAVE. This method will
     * apply it to either FileDialog or JFileChooser as needed.
     * @param chooseDirectory true if the user should select a directory; false if
     * they should select a file.  On some configurations (ex. Windows + FileDialog),
     * selecting directories is not possible.
     * @param fif The file extension filter(s) that should be set.  On
     * some configurations (ex. Windows + FileDialog), file extension filters do
     * not have any effect.  The last file extension filter will be the default.
     * Ignored if chooseDirectory is true.
     * @param buttonText Available for overriding the default button text.  May be null to use the toolkit-specific default.
     * @param fromJavaFX If the fileChooserMode is JavaFX, and this method is being
     * called from JavaFX, this must be set to true.  Defaults to false (called from Swing),
     * which uses synchronization to allow calling a JavaFX dialog from Swing.  Unfortunately
     * that also results in freezing the UI if called from JavaFX; hence the need for this parameter.
     * @return The file that was selected, or null if none was chosen.
     */
    public static File getFile(int mode, boolean chooseDirectory, FileExtensionFilter[]fif, String buttonText, boolean fromJavaFX) throws IOException {
        
        if (firstRequest) {
            jfcBIQChooser = new JFileChooser();
            jfxFileChooser = new FileChooser();
            biqFileDialog = new FileDialog((Frame)null, "Select Scenario");
            firstRequest = false;
        }
        
        if (currentDirectory.exists()) {
            jfcBIQChooser.setCurrentDirectory(currentDirectory);
            jfxFileChooser.setInitialDirectory(currentDirectory);
        }
        File file = null;
        
        if (fileChooserMode.equals("JFileChooser")|| (fileChooserMode.equals("JavaFX") && chooseDirectory)) {
            jfcBIQChooser.resetChoosableFileFilters();
            if (chooseDirectory) {
                jfcBIQChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            }
            else {
                jfcBIQChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                if (fif != null) {
                    for (FileExtensionFilter filter : fif) {
                        jfcBIQChooser.setFileFilter(filter);
                    }
                }
            }
            
            AtomicInteger fileChoiceAtomic = new AtomicInteger(0);
            
            //Must run on the Swing event thread.  On Mac in particular, but
            //perhaps some other systems as well, the dialog will never appear
            //if it is run on another thread.
            try {
                EventQueue.invokeAndWait(() -> {
                    System.out.println("Showing Swing file chooser on thread " + Thread.currentThread().getName());
                    if (mode == FileDialog.SAVE) {
                        fileChoiceAtomic.set(jfcBIQChooser.showSaveDialog(null));
                    }
                    else if (buttonText == null) {
                        fileChoiceAtomic.set(jfcBIQChooser.showOpenDialog(null));
                    }
                    else {
                        fileChoiceAtomic.set(jfcBIQChooser.showDialog(null, buttonText));
                    }
                });
            }
            catch(InterruptedException | InvocationTargetException ex) {
                logger.error("File chooser interrupted");
            }
            
            if (fileChoiceAtomic.get() == JFileChooser.APPROVE_OPTION) {
                file = jfcBIQChooser.getSelectedFile();
                if (!chooseDirectory) {
                    if (!(file.getName().toLowerCase().endsWith(".biq")) && !(file.getName().toLowerCase().endsWith(".sav")))
                        file = new File(file.getCanonicalPath() + ".biq");
                }
            }
        }
        else if (fileChooserMode.equals("JavaFX")) {
            if (chooseDirectory) {
                /**
                 * Note: I think this will work, but there's a prominent case of choosing the directory on first load, where it will fail with JavaFX because
                 * of an IllegalStateException: Toolkit not initialized.  JavaFX is not initialized until the LoadAccelerator starts.
                 * Thus in practice, we're falling back to Swing when choosing directories.
                 */
                DirectoryChooser dc = new DirectoryChooser();
                Runnable runnable = new Runnable(){
                    @Override
                    public void run() {
                        returnFile = dc.showDialog(null);
                        if (logger.isDebugEnabled()) {
                            logger.debug("Got input file");
                        }
                        synchronized(this) {
                            this.notifyAll();
                        }
                    } 
                };
                Platform.runLater(runnable);
                try {
                    logger.info("Waiting for JavaFX file chooser...");
                    synchronized(runnable) {
                        runnable.wait();
                    }
                    logger.info("File chooser has completed...");
                }
                catch(InterruptedException ex) {
                    logger.error("File dialog interrupted", ex);
                }
                return returnFile;
            }
            else {
                jfxFileChooser.getExtensionFilters().clear();
                if (fif != null) {
                    for (int f = fif.length - 1; f >= 0; f--) {
                        FileExtensionFilter filter = fif[f];
                        //Convert from Swing to JavaFX extensions
                        String[] extensions = new String[filter.extensions.length];
                        for (int e = 0; e < extensions.length; e++) {
                            extensions[e] = "*." + filter.extensions[e];
                        }
                        jfxFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(filter.getDescription(), extensions));
                    }
                }

                if (mode == FileDialog.SAVE) {
                    jfxFileChooser.setTitle("Save");
                    if (fromJavaFX) {
                        return getFileFromJavaFX(true);
                    }
                    else {
                        return getFileFromJavaFXInSynchronizedContext(true);
                    }
                }
                else {
                    if (buttonText == null) {
                        jfxFileChooser.setTitle("Open");
                    }
                    else {
                        jfxFileChooser.setTitle("buttonText");
                    }
                    if (fromJavaFX) {
                        return getFileFromJavaFX(false);
                    }
                    else {
                        return getFileFromJavaFXInSynchronizedContext(false);
                    }
                }
            }
        }
        else {
            if (chooseDirectory) {
                System.setProperty("apple.awt.fileDialogForDirectories", "true");
            }
            else {
                for (FileExtensionFilter filter : fif) {
                    biqFileDialog.setFilenameFilter(filter);
                }
            }
            biqFileDialog.setMode(mode);
            biqFileDialog.setVisible(true);

            String fileName = biqFileDialog.getFile();
            String directory = biqFileDialog.getDirectory();

            fileName = directory + fileName;
            if (!chooseDirectory) {
                if (!(fileName.toLowerCase().endsWith(".biq")) && !(fileName.toLowerCase().endsWith(".sav")))
                    fileName = fileName + ".biq";
            }

            file = new File(fileName);
            if (chooseDirectory) {
                System.setProperty("apple.awt.fileDialogForDirectories", "false");
            }
        }
        return file;
    }
    
    private static File getFileFromJavaFX(boolean saveMode) {
        if (saveMode) {
            returnFile = jfxFileChooser.showSaveDialog(null);
        }
        else {
            returnFile = jfxFileChooser.showOpenDialog(null);
        }
        return returnFile;
    }
    
    private static File getFileFromJavaFXInSynchronizedContext(boolean saveMode) {
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                if (saveMode) {
                    returnFile = jfxFileChooser.showSaveDialog(null);
                }
                else {
                    returnFile = jfxFileChooser.showOpenDialog(null);
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("Got input file");
                }
                synchronized(this) {
                    this.notifyAll();
                }
            } 
        };
        Platform.runLater(runnable);
        try {
            logger.info("Waiting for JavaFX file chooser...");
            synchronized(runnable) {
                runnable.wait();
            }
            logger.info("File chooser has completed...");
        }
        catch(InterruptedException ex) {
            logger.error("File dialog interrupted", ex);
        }
        return returnFile;
    }
}
