/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.xplatformeditor;

import com.civfanatics.civ3.biqFile.IO;
import com.civfanatics.civ3.xplatformeditor.download.RenameUnitWindowController;
import javafx.scene.control.TextArea;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.LoadException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.log4j.Logger;
import webbrowserfx.WebBrowserFX;

/**
 * FXML Controller class
 *
 * @author Andrew
 */
public class UnitDownloadWindowController implements Initializable {
    
    static Logger logger = Logger.getLogger("UnitDownloadWindowController");

    @FXML
    private TextField txtDecompressionMethod;
    
    @FXML
    private TextArea txtDescription;
    
    private IO biq;
    private UnitTab unitTab;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtDescription.setText("Welcome to the Unit Download Area.  This area uses a customized web browser to allow you to download units from the CFC downloads database, and have them automatically added to your scenario (while being optionally renamed).  All you have to do is find a unit you'd like to add on the Downloads Database, click the download button, and the editor will take care of adding it to your scenario.\n\nTo help with decompression, the editor will call 7-Zip to perform decompression.  You can test that it is available with the Test 7-Zip button below, and on Windows, the editor can automatically download it if need be.  Then click the Browse for Units button to begin downloading units.\n\nAfter 7-Zip is verified to be working, on future uses of the editor you'll be taken directly to the Downloads Database.");
        if (Main.settings.sevenZipVerified) {
            browse();
        }
    }
    
    @FXML
    protected void browse() {
        Stage browserStage = new Stage();
        WebBrowserFX browser = new WebBrowserFX();
        browser.setMaster(this);
        //Old vBulletin forum - remove once downloader is updated
        //browser.setHomepage("http://forums.civfanatics.com/downloads.php?do=cat&id=23");
        browser.setHomepage("https://forums.civfanatics.com/resources/categories/civ3-unit-graphics.23/");
        browser.start(browserStage);
        //Close this stage
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), (ev) -> {
            Scene scene = txtDescription.getScene();
            if (scene != null) {
                Stage stage = (Stage)txtDescription.getScene().getWindow();
                if (stage != null) {
                    stage.close();
                }
            }
        }));
        timeline.play();
        //Show the new stage
        browserStage.show();
    }
    
    @FXML
    protected void test7z() {
        boolean sevenZResult = RenameUnitWindowController.test7Zip();
        if (sevenZResult) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "7-zip testing successful; you may download units.", ButtonType.OK);
            a.setTitle("7-Zip Testing");
            a.setHeaderText("");
            a.showAndWait();
            Main.settings.sevenZipVerified = true;
        }
        else {
            if (Main.os.name.toLowerCase().contains("windows")) {
                Alert b = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to download 7-Zip (1.3 MB download) so the editor can download units?", ButtonType.YES, ButtonType.NO);
                b.setTitle("7-Zip Testing");
                b.setHeaderText("7-Zip Not Detected");
                Optional<ButtonType> button = b.showAndWait();
                if (button.isPresent()) {
                    ButtonType type = button.get();
                    if (type.equals(ButtonType.YES)) {
                        try {
                            File folder = new File("bin/7z");
                            if (!folder.exists()) {
                                Files.createDirectory(Paths.get("bin/7z"));
                            }
                            HttpURLConnection conn = utils.getHttpConnection("http://quintillus.warpmail.net/civ3editor/bin/7zip/7z.exe");
                            utils.downloadFromHttpConnection(conn, "bin/7z/7z.exe");
                            HttpURLConnection conn2 = utils.getHttpConnection("http://quintillus.warpmail.net/civ3editor/bin/7zip/7z.dll");
                            utils.downloadFromHttpConnection(conn2, "bin/7z/7z.dll");
                            Alert y = new Alert(Alert.AlertType.INFORMATION, "7-Zip has been successfully downloaded and you can now download units.", ButtonType.OK);
                            y.setTitle("Successful download");
                            y.setHeaderText("");
                            y.showAndWait();
                            Main.settings.sevenZipVerified = true;
                        }
                        catch(IOException ex) {
                            logger.error("Exception while downloading 7-zip", ex);
                            Alert e = new Alert(Alert.AlertType.ERROR, "7-zip could not be downloaded.  Check your network connection.  You may also manually place 7z.exe and 7z.dll in the bin/7z folder of the editor.", ButtonType.OK);
                            e.setHeaderText("");
                            e.setTitle("Error Downloading");
                            e.showAndWait();
                        }
                    }
                }
            }
            else {
                Alert a = new Alert(Alert.AlertType.INFORMATION, "7-zip testing failed.  7-zip must be available on your command line, or in the editor's bin/7z folder to download units.", ButtonType.OK);
                a.setTitle("7-Zip Testing");
                a.setHeaderText("");
                a.showAndWait();
            }
            
        }
    }
    
    public void setBIQLink(IO biq) {
        this.biq = biq;
    }
    
    public void setUnitTabLink(UnitTab unitTab) {
        this.unitTab = unitTab;
    }
    
    public void downloadUnit(String url) {
        try {
            final StringBuffer initialFileName = new StringBuffer();
            
            Task<Integer> downloadTask = new Task() {
                @Override
                protected Integer call() {
                    
                    try {
                        URL theUrl = new URL(url);
                        HttpURLConnection conn = (HttpURLConnection)theUrl.openConnection();
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        BufferedInputStream reader = new BufferedInputStream(is);
                        
                        long downloadSize = conn.getContentLengthLong();
                        long bytesDownloaded = 0;
                        
                        updateProgress(bytesDownloaded, downloadSize);

                        //Grab the name of the file (including extension) from the headers
                        //String contentType = conn.getHeaderField("Content-Type");
                        //String name = contentType.substring(contentType.indexOf("name=") + 6);  //5 for name=, one for double-quote
                        //Header changed with move to XenForo
                        String contentType = conn.getHeaderField("Content-Disposition");
                        String name = contentType.substring(contentType.indexOf("name=") + 6);  //5 for name=, one for double-quote
                        int endQuote = name.indexOf("\"");
                        name = name.substring(0, endQuote);

                        //Also set up our output stream
                        File unitsFolder = null;
                        try {
                            File f = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
                            unitsFolder = new File(f.getParent() + "/units");
                            if (!Files.exists(unitsFolder.toPath())) {
                                Files.createDirectory(unitsFolder.toPath());
                            }
                        }
                        catch(URISyntaxException ex) {
                            System.err.println("URISyntaxException: " + ex);
                        }
                        initialFileName.append(unitsFolder.getCanonicalPath() + Main.fileSlash + name);
                        File file = new File(initialFileName.toString());
                        FileOutputStream fos = new FileOutputStream(file);

                        byte[] buffer = new byte[1024];
                        for (;;) {
                            int bytesRead = reader.read(buffer);
                            if (bytesRead > -1) {
                                fos.write(buffer, 0, bytesRead);
                                bytesDownloaded+=bytesRead;
                                updateProgress(bytesDownloaded, downloadSize);
                            }
                            else {
                                break;
                            }
                        }
                        
                        updateProgress(1, 1);
                        
                        //Close our resources.  The really important one is the FileOutputStream.
                        //If we don't do that, our external decompression utility will
                        //fail due to another process - the editor - using the file.
                        reader.close();
                        fos.close();
                    }
                    catch(IOException ex) {
                        logger.error("IOException while downloading", ex);
                    }
                    
                    return 0;
                }
            };
                        
            //When it's all ready, load up the rename window
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Main.class.getResource("/fxml/RenameUnitWindow.fxml"));
            Parent root = fxmlLoader.load();
            
            //We must load the FXML before we can access its controller
            RenameUnitWindowController controller = fxmlLoader.getController();
            controller.setDecompressionMethod(Main.settings.decompressionMethod);
            controller.setBiqLink(biq);
            controller.setUnitTab(unitTab);
            
            String firstFolder = "";
            if (biq.scenarioProperty.get(0).getSearchFolderLength() > 0) {
                firstFolder = biq.scenarioProperty.get(0).getSearchFolder(0);
            }
                
            //TODO: Figure out if the folder we put the unit in should be the Civ
            //install Scenarios folder, or local to the BIQ file.  I'm not sure
            //off the top of my head if the editor looks relative to the BIQ file,
            //or only relative to the install folder, for graphics.  Usually I
            //put the BIQs in the scenarios folder since that's where they need
            //to be for the game to find them.  I'm pretty sure I've done the same
            //thing when looking for files everywhere else in this editor, so I'm
            //going to go with that here for now as well.
            String testing = biq.fileName;
                
            String scenarioFolder = utils.getConquestsFolder(Main.settings.civInstallDir);
            scenarioFolder = scenarioFolder + "Scenarios" + Main.fileSlash + firstFolder + Main.fileSlash;
            
            controller.setScenarioFolder(scenarioFolder);
            
            root.setVisible(true);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Add Unit to Scenario");
            stage.show();
            
            downloadTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

                @Override
                public void handle(WorkerStateEvent event) {
                    //Things that must wait for the download
                    controller.setFileName(initialFileName.toString());
                }
                
            });
            
            controller.setDownloadTask(downloadTask);
            
            ExecutorService es = Executors.newFixedThreadPool(1);
            es.execute(downloadTask);
            
        }
        catch(LoadException ex) {
            logger.error("Whoops!" + ex);
        }
        catch(MalformedURLException ex) {
            logger.error("Whoops!" + ex);
        }
        catch(IOException ex) {
            logger.error("Whoops! " + ex);
        }
        
    }
    
}
