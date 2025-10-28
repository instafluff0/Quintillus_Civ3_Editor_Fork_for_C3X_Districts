
package com.civfanatics.civ3.xplatformeditor.specialty;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Reads in a CSV file.  Stores it as a list of list of strings.
 * Other classes can then process the imported CSV as needed; this one simply
 * reads it in from the flat file into Java.
 * @author Andrew
 */
public class CSVReader {
    
    public static List<List<String>> readCSVFile(Scanner scanner) {
        List<List<String>> spreadsheet = new ArrayList<List<String>>();
        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();
            
            //Doesn't handle commas properly yet.  That's for version 1.1
            String[]items = nextLine.split(",");
            
            List<String> newRow = new ArrayList<String>();
            for (String item : items) {
                newRow.add(item);
            }
            spreadsheet.add(newRow);
        }
        return spreadsheet;
    }
}
