/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.xplatformeditor.tabs.map;

import com.civfanatics.civ3.biqFile.EXPR;
import com.civfanatics.civ3.biqFile.PRTO;
import com.civfanatics.civ3.biqFile.UNIT;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.ButtonInTableRenderer;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.JLabelInTableRenderer;
import com.civfanatics.civ3.xplatformeditor.CustomComponents.JSpinnerInTableRenderer;
import com.civfanatics.civ3.xplatformeditor.Main;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Andrew
 */
public class UnitNameWindow extends JDialog {
    
    DefaultComboBoxModel mdlExprs = new DefaultComboBoxModel();
    
    JTable existingUnits;
    DefaultTableModel existingUnitModel = new DefaultTableModel();
    ButtonInTableRenderer buttonRenderer = new ButtonInTableRenderer();
    JSpinnerInTableRenderer spinnerRenderer = new JSpinnerInTableRenderer();
    JLabelInTableRenderer labelRenderer = new JLabelInTableRenderer();
    //List of units.  We'll update it in this list and it'll be updated everywhere
    List<UNIT>units;
    
    List<EXPR>exprs;
    List<PRTO>prtos;
    
    JLabel[] unitTypes;
    JComboBox[] exprDropdowns;
    JTextField[] unitName;
    JComboBox[] aiStrategies;
    Map<Integer, Integer>[] comboToStrategy;
    
    JScrollPane scrUnits = new JScrollPane();
    JPanel pnlExistingUnits = new JPanel();
    
    GridBagLayout existingUnitsLayout = new GridBagLayout();
    
    public UnitNameWindow() {
        super(Main.mainMain, true);
        setLocationRelativeTo(Main.mainMain);
        setLocation(this.getParent().getWidth()/2, this.getParent().getHeight()/2);
        setTitle("Update Unit Names");
        
        scrUnits.setViewportView(pnlExistingUnits);
        pnlExistingUnits.setLayout(existingUnitsLayout);
        this.add(scrUnits);
        
        GridBagConstraints euc = new GridBagConstraints();
        euc.weighty = 0.1;
        euc.gridheight = 1;
        euc.gridwidth = 1;

        mdlExprs.addElement("Conscript");
        mdlExprs.addElement("Regular");
        mdlExprs.addElement("Veteran");
        mdlExprs.addElement("Elite");
        
        this.setPreferredSize(new Dimension(640, 480));
        this.pack();
    }
    
    public void setPrtoList(List<PRTO> prtos) {
        this.prtos = prtos;
    }
    
    public void setUnits(List<UNIT> units) {
        this.units = units;
        updateUI();
    }
    
    public void setExpr(List<EXPR> exprs) {
        this.exprs = exprs;
    }
    
    private void updateUI() {
        setTitle("Update Unit Names");
        
        pnlExistingUnits.setLayout(existingUnitsLayout);
        
        GridBagConstraints euc = new GridBagConstraints();
        euc.weighty = 0.1;
        euc.gridheight = 1;
        euc.gridwidth = 1;
        euc.gridx = 0;
        euc.gridy = 0;
        euc.weightx = 0.1;
        euc.fill = GridBagConstraints.HORIZONTAL;
        euc.insets = new Insets(5, 5, 5, 5);
        
        //Todo: Make unit type selectable?  That'd be kinda cool.
        JLabel typeLabel = new JLabel("Unit Type");
        JLabel exprLabel = new JLabel("Experience");
        JLabel nameLabel = new JLabel("Unit Name");
        JLabel strategyLabel = new JLabel("AI Strategy");
        
        pnlExistingUnits.add(typeLabel, euc);
        euc.gridx++;
        pnlExistingUnits.add(exprLabel, euc);
        euc.gridx++;
        pnlExistingUnits.add(nameLabel, euc);
        euc.gridx++;
        pnlExistingUnits.add(strategyLabel, euc);
        euc.gridx = 0;
        euc.gridy++;
        
        JScrollPane existingUnitsScroll = new JScrollPane();
        
        //TODO: Make it scrolly
        
        unitTypes = new JLabel[units.size()];
        exprDropdowns = new JComboBox[units.size()];
        unitName = new JTextField[units.size()];
        aiStrategies = new JComboBox[units.size()];
        comboToStrategy = new HashMap[units.size()];
        //Todo: Maybe add delete, I dunno.
        JButton[] deleteButton = new JButton[units.size()];
        
        for (int i = 0; i < units.size(); i++) {
            UNIT unit = units.get(i);
            //Show unit type label, expr dropdown, unit name text box, delete button
            unitTypes[i] = new JLabel();
            unitTypes[i].setText(prtos.get(unit.getPRTONumber()).getName());
            //TODO: Update experience model appropriately
            exprDropdowns[i] = new JComboBox();
            DefaultComboBoxModel mdl = new DefaultComboBoxModel();
            for (EXPR expr : exprs) {
                mdl.addElement(expr.getName());
            }
            exprDropdowns[i].setModel(mdl);
            exprDropdowns[i].setSelectedIndex(unit.getExperienceLevel());
            unitName[i] = new JTextField();
            unitName[i].setText(unit.getPTWCustomName());
            deleteButton[i] = new JButton();
            deleteButton[i].setText("X");
            //AI Strategy is more complex than experience, since the options vary
            //by unit.  If there is only one option, it's locked to that option.
            //If there is more than one, there are the enumerated options, plus
            //"Random", which has a value of -1.
            DefaultComboBoxModel stratMdl = new DefaultComboBoxModel();
            PRTO unitPrto = prtos.get(unit.getPRTONumber());
            //So strategySet is the list of all the strategies that the PRTO has.
            //strategySet[s] being true means the unit CAN have that strategy,
            //in which case we add it to the options.
            //We will also need to know:
            //  1.  Combo box index --> Strategy index, so we can update the BIQ storage
            //  2.  Strategy index --> Combo index, so we can set the correct index (?)
            comboToStrategy[i] = new HashMap<Integer, Integer>();
            Map<Integer, Integer> strategyToCombo = new HashMap<Integer, Integer>();
            Boolean[] strategySet = unitPrto.getAIStrategySet();
            int comboCount = 0;
            for (int s = 0; s < strategySet.length; s++) {
                if (strategySet[s]) {
                    stratMdl.addElement(PRTO.strategyNames[s]);
                    comboToStrategy[i].put(comboCount, s);
                    strategyToCombo.put(s, comboCount);
                    comboCount++;
                }
            }
            if (stratMdl.getSize() > 1) {
                stratMdl.addElement("Random");
                comboToStrategy[i].put(comboCount, -1);
                strategyToCombo.put(-1, comboCount);
            }
            aiStrategies[i] = new JComboBox();
            aiStrategies[i].setModel(stratMdl);
            if (unit.getAIStrategy() == -1) {
                aiStrategies[i].setSelectedItem("Random");
            }
            else {
                //TODO: The index here is to the AI strategy map, not the index in the list of options.
                aiStrategies[i].setSelectedIndex(strategyToCombo.get(unit.getAIStrategy()));
            }
            
            pnlExistingUnits.add(unitTypes[i], euc);
            euc.gridx++;
            pnlExistingUnits.add(exprDropdowns[i], euc);
            euc.gridx++;
            pnlExistingUnits.add(unitName[i], euc);
            euc.gridx++;
            pnlExistingUnits.add(aiStrategies[i], euc);
            euc.gridx = 0;
            euc.gridy++;
            //if (i == 2)
            //    break;
        }
        //Add some buttons
        euc.gridx = 1;
        JButton btnUpdate = new JButton("Update");
        euc.fill = GridBagConstraints.NONE;
        pnlExistingUnits.add(btnUpdate, euc);
        euc.gridx++;
        JButton btnCancel = new JButton("Cancel");
        pnlExistingUnits.add(btnCancel, euc);
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelAction();
            }            
        });
        btnUpdate.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAction();
            }
            
        });
        this.pack();
    }
    
    private void cancelAction() {
        dispose();
    }
    
    private void updateAction() {
        for (int i = 0; i < exprDropdowns.length; i++) {
            units.get(i).setExperienceLevel(exprDropdowns[i].getSelectedIndex());
            units.get(i).setPTWCustomName(unitName[i].getText());
            units.get(i).setAIStrategy(comboToStrategy[i].get(aiStrategies[i].getSelectedIndex()));
        }
        dispose();
    }
    
    public static void main(String[]args) {
        new UnitNameWindow().setVisible(true);
        
    }
    
    
}
