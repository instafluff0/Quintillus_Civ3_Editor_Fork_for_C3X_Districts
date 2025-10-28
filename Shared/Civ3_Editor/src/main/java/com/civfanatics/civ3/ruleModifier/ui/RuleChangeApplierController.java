/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.ruleModifier.ui;

import com.civfanatics.civ3.xplatformeditor.FileExtensionFilter;
import com.civfanatics.civ3.xplatformeditor.FileIO;
import com.civfanatics.civ3.xplatformeditor.Main;
import com.civfanatics.civ3.xplatformeditor.savFunctionality.SavPatcher;
import java.awt.FileDialog;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.log4j.Logger;

/**
 * FXML Controller class
 *
 * @author Andrew
 */
public class RuleChangeApplierController implements Initializable {

    static Logger logger = Logger.getLogger(RuleChangeApplierController.class);
    
    FileExtensionFilter savFilter = null;
    FileExtensionFilter[]saveFilters = null;
    FileExtensionFilter scenarioFileFilter = null;
    FileExtensionFilter bicFilter = null;
    FileExtensionFilter biqFilter = null;
    String[] acceptedExtensions = {"biq"};
    
    @FXML private Button cmdApplyNewRules;
    @FXML private Button cmdSelectSAV;
    @FXML private Button cmdSelectBIQ;
    @FXML private Label lblInstructions;
    @FXML private Label lblRulesApplied;
    @FXML private TextField txtSelectedSAV;
    @FXML private TextField txtSelectedBIQ;
    @FXML private CheckBox chkReplaceCitizens;
    @FXML private CheckBox chkReplaceRules;
    @FXML private CheckBox chkReplaceCivs;
    @FXML private CheckBox chkReplaceTerrains;
    
    final private String instructions = 
        "This functionality allows you to replace sections of the rules in a SAV with rules from an updated BIQ.  This can be used in a scenario to allow, "+ 
        "for example, barbarians to become more advanced over time, or the output of tiles to change over time.  " + 
        "\nThis functionality is not yet 'smart', meaning it should only be used with BIQs that do not add or remove items from BIQ sections." + 
        "\nYou should also only use this functionality as advised by the scenario designer - combining random BIQs and SAVs is likely to produce unusable results." + 
        "\nThis operation will render the .sav ineligible for the Hall of Fame, due to the change in rules.";
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        lblInstructions.setText(instructions);
    }    
    
    @FXML
    private void selectSAVFile() {
        String[] biqExtension ={"sav", "SAV"};
            if (savFilter == null) {
                savFilter = new FileExtensionFilter(biqExtension, "SAV files");
                saveFilters = new FileExtensionFilter[1];
                saveFilters[0] = savFilter;
            }
        try {
            File savFile = FileIO.getFile(FileDialog.LOAD, false, saveFilters, null, true);
            if (savFile != null) {
                txtSelectedSAV.setText(savFile.getCanonicalPath());
            }
        }
        catch(IOException ex) {
            logger.error("IOException for selecting SAV file");
        }
    }
    
    @FXML
    private void selectBIQFile() {
        File file;
        if (bicFilter == null) {
            String[] bic = {"bic"};
            String[] biq = {"biq"};
            bicFilter = new FileExtensionFilter(bic, Main.i18n("save.bicFiles"));
            biqFilter = new FileExtensionFilter(biq, Main.i18n("save.biqFiles"));
            scenarioFileFilter = new FileExtensionFilter(acceptedExtensions, Main.i18n("save.allScenarioFiles"));
        }
        FileExtensionFilter[]filters = new FileExtensionFilter[3];
        filters[0] = bicFilter;
        filters[1] = biqFilter;
        filters[2] = scenarioFileFilter;

        try {
            file = FileIO.getFile(FileDialog.LOAD, false, filters, null, true);
            if (file != null) {
                txtSelectedBIQ.setText(file.getCanonicalPath());
            }
        }
        catch(IOException ex) {
            logger.error("IOException", ex);
        }
    }
    
    @FXML
    private void applyNewRules() {
        logger.info("Applying new rules");
        try {
            File savFile = new File(txtSelectedSAV.getText());
            File biqFile = new File(txtSelectedBIQ.getText());
            List<String> sectionsToReplace = new ArrayList<>();
            if (chkReplaceCivs.isSelected()) {
                sectionsToReplace.add("RACE");
            }
            if (chkReplaceRules.isSelected()) {
                sectionsToReplace.add("RULE");
            }
            if (chkReplaceTerrains.isSelected()) {
                sectionsToReplace.add("TERR");
            }
            if (chkReplaceCitizens.isSelected()) {
                sectionsToReplace.add("CTZN");
            }
            SavPatcher.patchSAVWithNewRuleData(savFile, biqFile, sectionsToReplace);
            lblRulesApplied.setVisible(true);
            cmdApplyNewRules.setDisable(true);

            logger.info("New rules applied");
        }
        catch(IOException ex) {
            lblRulesApplied.setText("An error occurred while patching.  Please check the logs.");
            lblRulesApplied.setVisible(true);
        }
    }
}
