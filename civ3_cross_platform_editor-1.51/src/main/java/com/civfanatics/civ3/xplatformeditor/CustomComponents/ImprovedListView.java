
package com.civfanatics.civ3.xplatformeditor.CustomComponents;

import com.civfanatics.civ3.biqFile.BIQSection;
import com.civfanatics.civ3.xplatformeditor.Main;
import com.civfanatics.civ3.xplatformeditor.civilopedia.CivilopediaIcon;
import com.civfanatics.civ3.xplatformeditor.utils;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

public class ImprovedListView<T extends BIQSection> extends VBox {
    
    Logger logger = Logger.getLogger(this.getClass());
    
    List<T> baseItemList;
    ObservableList<T> observableList;
    
    String itemTypeName = "";
    
    ContextMenu techListPopup = new ContextMenu();
    MenuItem menuAdd = new MenuItem("Add");
    MenuItem menuCopy = new MenuItem("Copy");
    MenuItem menuRename = new MenuItem("Rename");
    MenuItem menuDelete = new MenuItem("Delete");
    
    BiConsumer<Integer, Integer> swapFunction;
    
    boolean dndUpdateDisabled = false;
    
    TextField filter = new TextField();
    private ListView<T> listView = new ListView<>();
    
    public ImprovedListView(String itemTypeName, List<T> baseItemList, ObservableList<T> items, BiPredicate predicate) {
        super();
        this.itemTypeName = itemTypeName;
        this.baseItemList = baseItemList;
        this.observableList = items;
        
        setupFilter(items, predicate);
        setupUI();
        
        setupCellFactory();
        setupContextMenu();
        setupDragAndDrop();
    }
    
    /**
     * This is a bit of a kludge for the fact that we don't sent the data to tabs
     * until after they are initialized.  Thus at the time the list is created, it
     * receives null for baseItemList.
     * 
     * TODO: That should be re-evaluated, but as I'm in the middle of refactoring the swap method,
     * that can wait.
     * @param baseItemList 
     */
    public void setBaseItems(List<T> baseItemList) {
        this.baseItemList = baseItemList;
    }

    private void setupUI() {
        //Odd context menu border/size bug if we add a CSS file to the whole scene
        //So just apply it here since that's all that really needs it.
        listView.getStylesheets().add("styles/styles.css");
        
        VBox.setVgrow(listView, Priority.ALWAYS);
        getChildren().add(filter);
        getChildren().add(listView);
    }

    private void setupFilter(ObservableList<T> items, BiPredicate predicate) {
        QueryList<T> queryList = new QueryList<>(items);
        queryList.setPredicate(predicate);
        filter.textProperty().addListener(obs -> {
            queryList.updateSearchQuery(filter.getText());
        });
        listView.setItems(queryList);
    }
    
    private void setupCellFactory() {
        listView.setCellFactory(new Callback<ListView<T>, ListCell<T>>() {
            @Override
            public ListCell<T> call(ListView<T> param) {
                ListCell<T> cell = new ListCell<T>() {
                    /**
                     * This function is responsible for displaying the name of the
                     * item in the list.  Without it, you can't distinguish one
                     * from the other.
                     * 
                     * See full documentation in JavaFX's Cell.java.
                     * @param tech
                     * @param empty 
                     */
                    @Override
                    protected void updateItem(T tech, boolean empty) {
                        super.updateItem(tech, empty);
                        if (empty || tech == null) {
                            setText(null);
                            setGraphic(null);
                        }
                        else {
                            setText(tech.getName());
                            if (Main.GRAPHICS_ENABLED) {
                                try {
                                    BufferedImage icon = CivilopediaIcon.getSmallCivilopediaIcon(tech);
                                    if (icon != null) {
                                        WritableImage fxImg = SwingFXUtils.toFXImage(icon, null);
                                        setGraphic(new ImageView(fxImg));
                                        if (logger.isTraceEnabled())
                                            logger.trace("graphic set");
                                    }
                                }
                                catch(Exception ex) {
                                    logger.error("Boom", ex);
                                }
                            }
                        }
                    }
                };
                
                cell.setOnDragDropped(new EventHandler<DragEvent>(){
                    @Override
                    public void handle(DragEvent event) {
                        try {
                            Dragboard d = event.getDragboard();
                            if (d.hasString()) {
                                String theString = d.getString();
                                dndUpdateDisabled = true;
                                
                                int source = Integer.parseInt(theString);
                                int destination = cell.getItem().getIndex();
                                boolean keepGoing = localSwapFunction(source, destination);
                                if (keepGoing) {
                                    swapFunction.accept(source, destination);
                                }
                                
                                dndUpdateDisabled = false;
                            }
                        }
                        catch(Exception ex) {
                            logger.error("Unexpected error while dragging and dropping", ex);
                            String errType = ex.toString();
                            Alert a = new Alert(Alert.AlertType.ERROR, "Unexpected error while dragging and dropping: " + errType + ".  See the log for details and post info on the CFC thread for assistance.");
                            a.setHeaderText("Unexpected error");
                            a.setTitle("Unexpected error");
                            a.show();
                        }
                    }
                });
                
                cell.setOnDragEntered(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {
                        cell.setOpacity(.75);
                        cell.getStyleClass().add("dndBorder");
                    }                    
                });
                
                cell.setOnDragExited(new EventHandler<DragEvent>() {
                    @Override
                    public void handle(DragEvent event) {
                        cell.setOpacity(1);
                        cell.getStyleClass().remove("dndBorder");
                    }                    
                });
                
                return cell;
            }
        });
    }
    
    private void setupDragAndDrop() {
        listView.setOnDragDetected(new EventHandler(){
            public void handle(Event event) {
                if (logger.isTraceEnabled())
                    logger.trace("Drag detected");  
                
                String index = "" + listView.getSelectionModel().getSelectedItem().getIndex();
                
                Dragboard db = listView.startDragAndDrop(TransferMode.MOVE);
                
                ClipboardContent clip = new ClipboardContent();
                clip.putString(index);
                db.setContent(clip);
            }
        });
        
        listView.setOnDragOver((DragEvent event) -> {
            event.acceptTransferModes(TransferMode.MOVE);
        });
    }

    private void setupContextMenu() {
        listView.setContextMenu(techListPopup);
        
        listView.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>(){
            @Override
            public void handle(ContextMenuEvent event) {
                techListPopup.show(listView, event.getScreenX(), event.getScreenY());
                event.consume();
            }
        });
    }
    
    public void setAddAction(Consumer<String> addAction) {
        menuAdd.setOnAction( a -> {
            String capitalized = itemTypeName.substring(0, 1).toUpperCase() + itemTypeName.substring(1);
            String name = utils.getItemName("Enter " + itemTypeName + " name", capitalized + " Name", "Enter " + itemTypeName + " name:", "");
            
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "You cannot add nameless " + itemTypeName, "Cannot add", JOptionPane.ERROR_MESSAGE);
                return;
            }
            addAction.accept(name);
            observableList.add(baseItemList.get(baseItemList.size() - 1));
        });
        techListPopup.getItems().add(menuAdd);
    }
    
    public void setDeleteAction(Consumer<T> deleteAction) {
        menuDelete.setOnAction( a -> {
            T deletedItem = listView.getSelectionModel().getSelectedItem();
            deleteAction.accept(deletedItem);
            observableList.remove(deletedItem);
        });
        techListPopup.getItems().add(menuDelete);
    }
    
    public void setRenameAction(Consumer<T> renameAction) {
        menuRename.setOnAction(a -> {
            renameAction.accept(listView.getSelectionModel().getSelectedItem());
            listView.refresh();
        });
        techListPopup.getItems().add(menuRename);
    }
    
    public void setCopyAction(BiConsumer<T, String> copyAction) {
        menuCopy.setOnAction(a -> {  
            String capitalized = itemTypeName.substring(0, 1).toUpperCase() + itemTypeName.substring(1);
            String name = utils.getItemName("Enter " + itemTypeName + " name", capitalized + " Name", 
                "Enter the name for the copied " + itemTypeName + ":", "");
            copyAction.accept(listView.getSelectionModel().getSelectedItem(), name);
            observableList.add(baseItemList.get(baseItemList.size() - 1));
            listView.refresh();
        });
        techListPopup.getItems().add(menuCopy);
    }
    
    public void setChangeFunction(Consumer<T> changeFunction) {
        listView.getSelectionModel().selectedItemProperty().addListener(
            new javafx.beans.value.ChangeListener<T>() {
                public void changed(ObservableValue<? extends T> ov, T oldVal, T newVal) {
                    if (!dndUpdateDisabled) {
                        if (logger.isDebugEnabled())
                            logger.debug("Accepting change event");
                        changeFunction.accept(newVal);
                    }
                    else {
                        logger.info("Discarding save event due to DND being disabled");
                    }
                }
            }
        );
    }
    
    public void setSwapFunction(BiConsumer<Integer, Integer> swapFunction) {
        this.swapFunction = swapFunction;
    }
    
    /**
     * Swaps two items in the list.
     * @param source The initial index of the item being swapped.
     * @param destination The destination index of the item being swapped.
     * @return true if non-local swap functions should be invoked; false if source == destination (no swap)
     */
    private boolean localSwapFunction(int source, int destination) {
        if (logger.isDebugEnabled())
            logger.debug("Swapping order");
        logger.info("Swapping...");
        
        if (source == destination) {
            if (logger.isDebugEnabled())
                logger.debug("Source and destination are the same; not swapping");
            return false;
        }
        
        T sourceItem = baseItemList.get(source);
        T initialDestinationTech = baseItemList.get(destination);
        
        if (logger.isDebugEnabled()) {
            logger.debug("Source tech: " + sourceItem.getName());
            logger.debug("Destination tech: " + initialDestinationTech.getName());
        }
        
        
        //initialDestinationTech.setIndex(source);
        
        if (source < destination) {
            //Moving later in the list
            //TODO: Move into if/else, eliminate duplicates
            baseItemList.add(destination + 1, sourceItem);
            baseItemList.remove(source);
        
            //Other techs will have lower indices, moved will take old destination index
            sourceItem.setIndex(destination);
            //Decrement indices of intermediary techs
            for (int i = source; i < destination; i++) {
                baseItemList.get(i).setIndex(baseItemList.get(i).getIndex() - 1);
            }
            
            //Update tech list FX - remove source last so our insert + is consistent
            observableList.remove(sourceItem);
            observableList.add(destination, sourceItem);
            
            //Make sure updates save to the right place
            //technologyIndex = destination;
            listView.getSelectionModel().select(destination);
        }
        else {
            //Moving earlier in the list
            baseItemList.add(destination + 1, sourceItem);
            baseItemList.remove(source + 1); //plus one b/c inserting it earlier increased the old location's index
            
            //Other techs will have higher indices, moved will take destination (not + 1, b/c the list is base zero)
            sourceItem.setIndex(destination + 1);
            for (int i = source; i > destination + 1; i--) {
                baseItemList.get(i).setIndex(baseItemList.get(i).getIndex() + 1);
            }
            
            //Update tech list FX - remove source first so we don't remove a different one
            observableList.remove(sourceItem);
            observableList.add(destination + 1, sourceItem);
            
            //Make sure updates save to the right place
            //technologyIndex = destination + 1;
            listView.getSelectionModel().select(destination + 1);
        }
        
        //updateOrderInOtherTabs(source, destination, sourceItem.getName());
        
        if (logger.isTraceEnabled())
            logger.trace("End swapping order");       
        return true;
    }
}
