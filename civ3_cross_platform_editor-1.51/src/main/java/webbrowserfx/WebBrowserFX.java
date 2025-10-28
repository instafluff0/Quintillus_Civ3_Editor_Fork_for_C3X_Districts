/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webbrowserfx;

import com.civfanatics.civ3.xplatformeditor.UnitDownloadWindowController;
import java.io.File;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javax.swing.JFileChooser;

/**
 *
 * @author Andrew
 */
public class WebBrowserFX extends Application {

    String homepage = "http://www.google.com";

    Button btnBack = new Button("Back");
    Button btnForward = new Button("Forward");
    Button btnReload = new Button("Reload");
    TextField txtAddressBar = new TextField(homepage);
    TabPane tabs = new TabPane();
    BrowserTab currentTab = null;

    @Override
    public void start(Stage primaryStage) {

//        Button btn = new Button();
//        btn.setText("Say 'Hello World'");
//        btn.setOnAction(new EventHandler<ActionEvent>() {
//            
//            @Override
//            public void handle(ActionEvent event) {
//                System.out.println("Hello World!");
//            }
//        });
        tabs.setPrefSize(400, 400);
        tabs.setSide(Side.TOP);
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);

        tabs.getTabs().addListener(new ListChangeListener<Tab>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Tab> change) {
                currentTab = (BrowserTab) tabs.getSelectionModel().getSelectedItem();
            }
        });

        addTab();

        MenuBar menu = new MenuBar();
        Menu file = new Menu("File");

        MenuItem exit = new MenuItem("Close");
        exit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                primaryStage.close();
            }
        });

        MenuItem newTab = new MenuItem("New Tab");
        newTab.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                addTab(homepage, true);
            }
        });
        newTab.setAccelerator(Shortcuts.newTab);
        
        MenuItem open = new MenuItem("Open");
        open.setOnAction(new EventHandler<ActionEvent>() {
           public void handle(ActionEvent t) {
               //there's probably a JavaFX way to do this but it's 11:42 PM and I want
               //to see this work so let's do Swing
               //what really matters is that it opens after all
               JFileChooser chooser = new JFileChooser();
               int rtrnVal = chooser.showOpenDialog(null);
               if (rtrnVal != JFileChooser.APPROVE_OPTION)
                   return;
               File file = chooser.getSelectedFile();
               String path = "file:///" + file.getAbsolutePath();
               addTab(path);
           } 
        });
        open.setAccelerator(Shortcuts.open);

        MenuItem closeTab = new MenuItem("Close Tab");
        closeTab.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                tabs.getTabs().remove(tabs.getSelectionModel().getSelectedIndex());
            }
        });
        closeTab.setAccelerator(Shortcuts.closeTab);

        file.getItems().add(newTab);
        file.getItems().add(open);
        file.getItems().add(closeTab);
        file.getItems().add(exit);
        menu.getMenus().add(file);

        txtAddressBar.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                System.out.println(txtAddressBar.getText());
                System.out.println("Code: " + t.getCharacter());
                System.out.println("Enter: " + KeyCode.ENTER);

                //if (t.getCharacter().equals("\n"))
                System.out.println("Char: " + (int) (t.getCharacter().charAt(0)));
                if (t.getCharacter().charAt(0) == 13) {
                    System.out.println("Navigating...");
                    System.out.println(txtAddressBar.getText());
                    final WebView view = (WebView) currentTab.getContent();

                    view.getEngine().getLoadWorker().stateProperty().addListener(
                            new ChangeListener<State>() {
                                public void changed(ObservableValue ov, State oldState, State newState) {
                                    if (newState == State.SUCCEEDED) {
                                        txtAddressBar.setText(view.getEngine().getLocation());
                                        currentTab.setText(view.getEngine().getTitle());
                                    } else if (newState == State.FAILED) {
                                        System.out.println("Could not navigate to " + txtAddressBar.getText());
                                    }
                                }
                            }
                    );

                    view.getEngine().load(txtAddressBar.getText());
                }
            }

        });

        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                WebView v = (WebView) currentTab.getContent();
                if (v.getEngine().getHistory().getCurrentIndex() != 0) {
                    v.getEngine().getHistory().go(-1);
                }
            }
        });

        btnForward.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                WebView v = (WebView) currentTab.getContent();
                if (v.getEngine().getHistory().getCurrentIndex() != v.getEngine().getHistory().getMaxSize()) {
                    v.getEngine().getHistory().go(1);
                }
            }
        });

        btnReload.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                WebView v = (WebView) currentTab.getContent();
                v.getEngine().reload();
            }
        });

        GridPane controlBar = new GridPane();
        RowConstraints controlRow = new RowConstraints();
        controlRow.setPercentHeight(100);
        ColumnConstraints backConstriants = new ColumnConstraints();
        ColumnConstraints forwardConstraints = new ColumnConstraints();
        ColumnConstraints reloadConstraints = new ColumnConstraints();
        ColumnConstraints addressBarConstraints = new ColumnConstraints();
        addressBarConstraints.setHgrow(Priority.ALWAYS);

        controlBar.getColumnConstraints().addAll(backConstriants, forwardConstraints, reloadConstraints, addressBarConstraints);
        controlBar.getRowConstraints().addAll(controlRow);

        GridPane.setConstraints(btnBack, 0, 0);
        controlBar.getChildren().add(btnBack);
        GridPane.setConstraints(btnForward, 1, 0);
        controlBar.getChildren().add(btnForward);
        GridPane.setConstraints(btnReload, 2, 0);
        controlBar.getChildren().add(btnReload);
        GridPane.setConstraints(txtAddressBar, 3, 0);
        controlBar.getChildren().add(txtAddressBar);

        BorderPane configPane = new BorderPane();
        configPane.setTop(menu);
        configPane.setCenter(controlBar);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(tabs);
        borderPane.setTop(configPane);

        StackPane root = new StackPane();
        root.getChildren().add(borderPane);

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Web Browser!");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        primaryStage.getScene().setOnKeyPressed(new EventHandler(){
            @Override
            public void handle(Event t) {
                KeyEvent ke = (KeyEvent) t;
                System.out.println(ke.getText());
                if (ke.getText().length() > 0)
                {
                    if (ke.getText().charAt(0) == '2' && !currentTab.getFocusOnInput())
                    {
                        System.out.println("Next tab");
                        tabs.getSelectionModel().selectNext();
                    }
                    else if (ke.getText().charAt(0) == '1' && !currentTab.getFocusOnInput())
                    {
                        tabs.getSelectionModel().selectPrevious();
                    }
                }
            }
        });
    }
    
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }
    
    private void addTab()
    {
        addTab(homepage);
    }

    void addTab(String url) {
        addTab(url, true);
    }
    
    void addTab(String url, boolean makeActive)
    {
        final BrowserTab tab = new BrowserTab(this, url);
        tab.setController(controller);
        //tab.setGraphic(webKit);
        if (makeActive)
            currentTab = tab;

        tab.setOnSelectionChanged(new EventHandler() {
            @Override
            public void handle(Event t) {
                TabChange(t, tab);
            }

        });

        tabs.getTabs().addAll(tab);
        
        if (makeActive)
            tabs.getSelectionModel().select(tabs.getTabs().size() - 1);
    }

    private void TabChange(Event t, BrowserTab tab) {
        if (tab.isSelected()) {
            currentTab = tab;
            txtAddressBar.setText(((WebView) currentTab.getContent()).getEngine().getLocation());
            tab.setText(((WebView) currentTab.getContent()).getEngine().getTitle());
        }
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    void setWindowTitle(String title)
    {
        //currentTab.setTitle?
    }
    
    void setAddressBar(String url)
    {
        txtAddressBar.setText(url);
    }

    //Below here are civ-specific extensions
    UnitDownloadWindowController controller = null;
    
    public void setMaster(UnitDownloadWindowController controller) {
        this.controller = controller;
    }
}
