package com.civfanatics.civ3.xplatformeditor;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Quintillus
 */
import com.civfanatics.civ3.biqFile.FLAV;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import org.apache.log4j.Logger;
public class FlavTable extends AbstractTableModel{
    private String[]columnNames;
    Logger logger = Logger.getLogger(this.getClass());
    //Vector of vectors.  First vector shall represent rows, second columns
    private Vector<Vector<Object>>data;
    boolean addingFlavor;

    public FlavTable()
    {
        data = new Vector<Vector<Object>>();
        addingFlavor = false;
    }

    public int getColumnCount()
    {
        if (data.size() == 0)
            return 0;
        return data.get(0).size();
    }

    public int getRowCount()
    {
        return data.size();
    }

    public Object getValueAt(int x, int y)
    {
        return data.get(x).get(y);
    }

    public String getColumnName(int col)
    {
        if (col > columnNames.length)
            return "";
        return columnNames[col];
    }

    public boolean isCellEditable(int row, int col)
    {
        if (col == 0 && !(addingFlavor))
            return false;
        return true;
    }

    public void setValueAt(Object value, int x, int y)
    {
        data.get(x).set(y, Integer.valueOf((String)value));
        fireTableCellUpdated(x, y);
    }

    public void addRow()
    {
        //System.out.println("ADDING ROW TO FLAVTABLE");
        data.add(new Vector<Object>());
    }
    
    public void addFullRow()
    {
        if (logger.isDebugEnabled())
            logger.debug("ADDING FULL ROW TO FLAVTABLE");
        Vector<Object>newRow = new Vector<Object>();
        for (int i = 0; i <= data.size(); i++)
        {
            if (logger.isDebugEnabled())
                logger.debug("i: " + i);
            newRow.add(50);
        }
        data.add(newRow);
    }

    public void addColumn()
    {
        if (logger.isDebugEnabled())
            logger.debug("ADDING COLUMN TO FLAVTABLE");
        for (int i = 0; i < data.size(); i++)
        {
            if (logger.isDebugEnabled())
                logger.debug("i: " + i);
            data.get(i).add(0);
        }
        if (logger.isDebugEnabled())
            logger.debug("ADDING COLUMN TO FLAVTABLE");
    }

    public void addColumn(int start)
    {
        if (logger.isDebugEnabled())
            logger.debug("ADDING COLUMN TO FLAVTABLE");
        for (int i = 0; i < data.size(); i++)
        {
            if (logger.isDebugEnabled())
                logger.debug("i: " + i);
            data.get(i).add(start);
        }
        if (logger.isDebugEnabled())
            logger.debug("ADDING COLUMN TO FLAVTABLE");
    }
    
    public void removeFlavor(int flavor)
    {
        for (int i = 0; i < data.size(); i++)
        {
            data.get(i).remove(flavor);
        }
        data.remove(flavor);
        this.updateGUI();
    }

    public void clear()
    {
        data = new Vector<Vector<Object>>();
    }

    public void updateGUI()
    {
        fireTableStructureChanged();
    }

    public void setHeaders(Vector<String>headers)
    {
        addingFlavor = true;
        columnNames = new String[headers.size() + 1];
        columnNames[0] = "";
        for (int i = 0; i < data.size(); i++)
        {
            data.get(i).set(0, headers.get(i));
            columnNames[i+1] = headers.get(i);
        }
        addingFlavor = false;
    }

    public void sendUpdate(java.util.List<FLAV>flavors)
    {
        for (int i = 0; i < flavors.size(); i++)
        {
            for (int j = 0; j < flavors.get(i).relationWithOtherFlavor.size(); j++)
            {   //j+1 since first column is labels
                data.get(i).set(j + 1, flavors.get(i).relationWithOtherFlavor.get(j));
            }
        }
        fireTableStructureChanged();
    }
}
