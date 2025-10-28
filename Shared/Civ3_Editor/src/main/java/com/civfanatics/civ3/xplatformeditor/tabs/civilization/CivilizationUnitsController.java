/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.xplatformeditor.tabs.civilization;

import com.civfanatics.civ3.biqFile.PRTO;
import com.civfanatics.civ3.xplatformeditor.Main;
import com.civfanatics.civ3.xplatformeditor.civilopedia.CivilopediaIcon;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.log4j.Logger;

/**
 * FXML Controller class
 *
 * @author Andrew
 */
public class CivilizationUnitsController implements Initializable {
    
    Logger logger = Logger.getLogger(this.getClass());

    @FXML
    private ListView lstAvailable;
    
    @FXML
    private ListView lstUnavailable;
    
    @FXML
    private Button btnApply;
    
    @FXML
    private Button btnCancel;
    
    private int civId;
    
    Callback<ListView<PRTO>, ListCell<PRTO>> cellFactory = new Callback<ListView<PRTO>, ListCell<PRTO>>() {
        @Override
        public ListCell<PRTO> call(ListView<PRTO> param) {
            ListCell cell = new ListCell<PRTO>() {
                /**
                 * This function is responsible for displaying the name of the
                 * item in the list.  Without it, you can't distinguish one
                 * from the other.
                 * 
                 * See full documentation in JavaFX's Cell.java.
                 * @param unit
                 * @param empty 
                 */
                @Override
                protected void updateItem(PRTO unit, boolean empty) {
                    super.updateItem(unit, empty);
                    if (empty || unit == null) {
                        setText(null);
                        setGraphic(null);
                    }
                    else {
                        setText(unit.getName());
                        if (Main.GRAPHICS_ENABLED) {
                            try {
                                BufferedImage icon = CivilopediaIcon.getSmallCivilopediaIcon(unit);
                                if (icon != null) {
                                    WritableImage fxImg = SwingFXUtils.toFXImage(icon, null);
                                    setGraphic(new ImageView(fxImg));
                                }
                            }
                            catch(Exception ex) {
                                System.err.println(ex.getMessage());
                            }
                        }
                    }
                }
            };
            return cell;
        }
    };
    
    public void sendCivIdAndUnits(int civId, List<PRTO> available, List<PRTO> unavailable) {
        this.civId = civId;
        
        lstAvailable.setCellFactory(cellFactory);
        lstUnavailable.setCellFactory(cellFactory);
        lstAvailable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lstUnavailable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        ObservableList<PRTO> observableAvailable = FXCollections.observableArrayList();
        observableAvailable.addAll(available);
        lstAvailable.setItems(observableAvailable);
        
        ObservableList<PRTO> observableUnavailable = FXCollections.observableArrayList();
        observableUnavailable.addAll(unavailable);
        lstUnavailable.setItems(observableUnavailable);
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void moveUnitsToAvailable() {
        ObservableList<PRTO> selectedItems = lstUnavailable.getSelectionModel().getSelectedItems();
        lstAvailable.getItems().addAll(selectedItems);
        lstUnavailable.getItems().removeAll(selectedItems);
    }
    
    @FXML
    private void moveUnitsToUnavailable() {
        ObservableList<PRTO> selectedItems = lstAvailable.getSelectionModel().getSelectedItems();
        lstUnavailable.getItems().addAll(selectedItems);
        lstAvailable.getItems().removeAll(selectedItems);
    }
    
    @FXML
    private void applyUnitsAvailable() {
        ObservableList<PRTO> availableUnits = lstAvailable.getItems();
        for (PRTO availableUnit : availableUnits) {
            availableUnit.setAvailableTo(civId);
        }
        ObservableList<PRTO> unavailableUnits = lstUnavailable.getItems();
        for (PRTO unavailableUnit : unavailableUnits) {
            unavailableUnit.setUnavailableTo(civId);
        }
        
        ((Stage)(btnApply.getScene().getWindow())).close();
    }
    
    @FXML
    private void cancelUnitsAvailable() {
        ((Stage)(btnApply.getScene().getWindow())).close();
    }    
}
