
package com.civfanatics.civ3.xplatformeditor.specialty;

import com.civfanatics.civ3.biqFile.CITY;
import com.civfanatics.civ3.biqFile.IO;
import com.civfanatics.civ3.biqFile.LEAD;
import com.civfanatics.civ3.biqFile.PRTO;
import com.civfanatics.civ3.biqFile.RACE;
import com.civfanatics.civ3.biqFile.UNIT;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import static javax.swing.BoxLayout.Y_AXIS;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Andrew
 */
public class UnitStatisticsFrame extends JFrame {
    JComboBox cmbCivilization = new JComboBox();

    JTextArea txtStats = new JTextArea();
    
    IO biq = null;
    
    public UnitStatisticsFrame(IO biq) {
        this.biq = biq;
        
        cmbCivilization.setMaximumSize(new Dimension(1024, 24));
        JScrollPane scrollPane = new JScrollPane();
        
        JPanel thePanel = new JPanel();
        thePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        BoxLayout layout = new BoxLayout(thePanel, Y_AXIS);
        thePanel.setLayout(layout);
        
        thePanel.add(cmbCivilization);
        thePanel.add(Box.createRigidArea(new Dimension(50, 10)));
        thePanel.add(scrollPane);
        scrollPane.setViewportView(txtStats);
        this.add(thePanel);
        this.setTitle("Unit Stats");
        this.setMinimumSize(new Dimension(320, 320));
        
        txtStats.setEditable(false);
        
        //Dummy data for display test
        DefaultComboBoxModel mdlCivilizations = new DefaultComboBoxModel();
        cmbCivilization.setModel(mdlCivilizations);
        mdlCivilizations.addElement("All civilizations");
        for (RACE civ : biq.civilization) {
            mdlCivilizations.addElement(civ.getName());
        }
        
        cmbCivilization.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                String newStats = getUnitStats();
                txtStats.setText(newStats);
                txtStats.setCaretPosition(0);
            }
        });
                
        txtStats.setBackground(SystemColor.control);
        String text = getUnitStats();
        txtStats.setText(text);
        txtStats.setCaretPosition(0);
    }
    
    public String getUnitStats() {
        Map<String, Integer> unitsByType = getUnitsByType();
            
        List<Map.Entry<String, Integer>> countByTypeArray = new ArrayList(unitsByType.entrySet());
        Collections.sort(countByTypeArray, new Comparator<Map.Entry<String, Integer>>(){
            public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
                return b.getValue().compareTo(a.getValue());
            }
        });
        int total = 0;
        for (Map.Entry<String, Integer> entry : countByTypeArray) {
            total += entry.getValue();
        }
        
        StringBuilder sb = new StringBuilder("Total number of units: ");
        sb.append(total).append("\n\n");
        for (Map.Entry<String, Integer> entry : countByTypeArray) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

    private Map<String, Integer> getUnitsByType() {
        Map<String, Integer> unitsByType = new HashMap<>();
        for (UNIT mapUnit : biq.mapUnit) {
            if (cmbCivilization.getSelectedIndex() != 0) {
                switch (mapUnit.getOwnerType()) {
                    case CITY.OWNER_PLAYER:
                        int owner = mapUnit.getOwner();
                        if (owner < 0) { //random/any
                            continue;
                        }
                        else {
                            LEAD player = biq.player.get(owner);
                            int civOwner = player.getCiv();
                            if (civOwner != (cmbCivilization.getSelectedIndex() - 1)) {
                                continue;
                            }
                        }
                        break;
                    case CITY.OWNER_CIV:
                        if (mapUnit.getOwner() != (cmbCivilization.getSelectedIndex() - 1)) {
                            continue;
                        }
                        break;
                    default:
                        continue;
                }
            }
            PRTO unitPrototype = biq.unit.get(mapUnit.getPRTONumber());
            String name = unitPrototype.getName();
            if (unitsByType.containsKey(name)) {
                unitsByType.put(name, unitsByType.get(name) + 1);
            }
            else {
                unitsByType.put(name, 1);
            }
        }
        return unitsByType;
    }
}
