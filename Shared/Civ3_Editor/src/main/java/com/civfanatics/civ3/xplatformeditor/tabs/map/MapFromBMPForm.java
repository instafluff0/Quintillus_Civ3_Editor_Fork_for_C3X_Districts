/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.xplatformeditor.tabs.map;

import com.civfanatics.civ3.biqFile.IO;
import com.civfanatics.civ3.biqFile.TERR;
import com.civfanatics.civ3.biqFile.TILE;
import com.civfanatics.civ3.xplatformeditor.FileExtensionFilter;
import com.civfanatics.civ3.xplatformeditor.Main;
import static com.civfanatics.civ3.xplatformeditor.Main.biqFile;
import static com.civfanatics.civ3.xplatformeditor.Main.biqIndex;
import com.civfanatics.civ3.xplatformeditor.imageSupport.WindowsBMPFilter;
import static com.civfanatics.civ3.xplatformeditor.tabs.map.MapFromBMPForm.logger;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.apache.log4j.Logger;

/**
 * Map dimensions must be:
 *   32*mapWidth+32 by 16*mapHeight+16.
 * 
 * NOTE: You will get considerably different results in the GIMP from resizing the
 * image first and then indexing it, versus indexing and then resizing.  I've
 * seen both of them end up "better", so I recommend trying both.
 * 
 * @author Andrew
 */
public class MapFromBMPForm extends JFrame
{
    
    static Logger logger = Logger.getLogger("Map From BMP");
    JLabel lblValkommen = new JLabel("<HTML>Welcome.  This allows you to import BIQ maps from 16-colour or 256-colour bitmaps.<br>The width BMP should be 32 times the width desired in the BIQ, plus 32,<br>and the height should be 16 times the height desired in the BIQ, plus 16.<br>You can use the calculator from the previous window to calculate these dimensions.<br>Click the button to start.");
    JCheckBox chkAdvanced = new JCheckBox("Advanced Mode");
    JButton cmdSelectFile = new JButton("Select 16 or 256 color BMP File");
    String[] bmpExtension ={"bmp", "BMP"};
    FileExtensionFilter bmpFilter = new FileExtensionFilter(bmpExtension, "BMP files");
    JFileChooser jfc = Main.jfcBMPChooser;
    GridBagConstraints c = new GridBagConstraints();
    
    WindowsBMPFilter bitmap = null;
    
    Color[]paletteColors = new Color[1];
    
    private JTextField[] colorFields;  //initialize once we know how many palette colors there are
    private JComboBox[] mapTo;
    DefaultComboBoxModel[] mdlMapToModel;
    
    public static int WATER_DOMINANT = 0;
    public static int GRASSLAND_DOMINANT = 1;
    public static int PLAINS_DOMINANT = 2;
    public static int DESERT_DOMINANT = 3;
    public static int TUNDRA_DOMINANT = 4;
    public static int FOREST_DOMINANT = 5;
    public static int JUNGLE_DOMINANT = 6;
    public static int HILL_DOMINANT = 7;
    public static int MOUNTAIN_DOMINANT = 8;
    public static int MARSH_DOMINANT = 9;
    public static int VOLCANO_DOMINANT = 10;
    public static int FLOOD_PLAIN_DOMINANT = 11;
    public static int PLAINS_FOREST = 12;
    public static int TUNDRA_FOREST = 13;
    public static int PINE_FOREST = 14;
    public static int TUNDRA_PINE_FOREST = 15;
    public static int PLAINS_PINE_FOREST = 16;
    public static int SNOW_MOUNTAINS = 17;
    public static int PLAINS_JUNGLE = 18;
    public static int TUNDRA_JUNGLE = 19;
    
    //WEIRD TERRAIN INFO
    // - PLAINS JUNGLES sometimes display as mountains
    // - PLAINS MARSHES sometimes display as volcanoes
    // - TUNDRA JUNGLES sometimes display as mountains
    // - TUNDRA MARSHES sometimes display as voclanoes
    // - DESERT JUNGLES crashes
    // - DESERT MARSHES crashes
    
    public static int NUM_TERR_CHOICES = 16;
    
    public static String[]terrainNames = {"Water", "Grassland", "Plains", "Desert", "Tundra", "Forest", "Jungle", "Hills", "Mountains", "Marsh", "Volcano", "Flood Plain", "Plains Forest", "Tundra Forest", "Pine Forest", "Tundra Pine Forest", "Plains Pine Forest", "Snow-Capped Mountains", "Plains Jungle", "Tundra Jungle"};
    
    private int paletteSize = 0;
    private int[]mappings; //map of color in palette -> dominant terrain (see statics above)
    
    JButton cmdShazam = new JButton("Create!");
    
    private int mapHeight = 0;
    private int mapWidth = 0;
    
    int[][]dominantTerrain;
    
    JPanel panel = new JPanel();
    JScrollPane scrollPane = new JScrollPane(panel);
    
    public MapFromBMPForm()
    {
        this.add(scrollPane);
        
        
       this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                //clear memory references
                //in theory i'd expect the dispose on close to take care of this
                //but it appears that isn't freeing the memory
                //so move in a C-like direction instead and deref it here when closed
                bitmap = null;
                Runtime.getRuntime().gc();
            }
        });
        
        this.setTitle("Import Map From BMP");
        jfc.setFileFilter(bmpFilter);
        panel.setLayout(new GridBagLayout());
        c.gridy=0;
        c.gridx=0;
        c.gridwidth=2;
        c.gridheight=1;
        c.weighty = 0.3;
        c.weightx = 0.1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(lblValkommen, c);
        
        cmdSelectFile.addActionListener(new ActionListener()
        {
           public void actionPerformed(ActionEvent e)
           {
               cmdSelectFileActionPerformed();
           }
        });
        
        c.gridy=1;
        c.gridx=0;
        c.gridwidth=1;
        c.weighty = 0.1;
        panel.add(cmdSelectFile, c);
        
        chkAdvanced.setVisible(false);
        c.gridy++;
        panel.add(chkAdvanced, c);
        
        this.setMaximumSize(new Dimension(1680, 1050));
        
        //now we gotta process stuff 'n' stuff
        //at the very least we need to figure out what the colours are
        
        //and then we need the transformer
        
    }
    
    private void cmdSelectFileActionPerformed()
    {
        int response = jfc.showOpenDialog(null);
        if (response == JFileChooser.APPROVE_OPTION)
        {
            String fileName = "";
            try{
                fileName = jfc.getSelectedFile().getCanonicalPath();
                Main.settings.bmpDir = jfc.getSelectedFile().getPath();
            }
            catch(IOException e)
            {
                logger.error("IOException when getting canonical path", e);
            }
            bitmap = new WindowsBMPFilter(fileName);
            try{
                this.setTitle("Importing BMP........");
                bitmap.processFile(false);
            }
            catch(OutOfMemoryError e)
            {
                logger.error("Out of memory", e);
                logger.error("Memory in use: " + Runtime.getRuntime().totalMemory()/1024 + " KB");
                logger.error("Memory available: " + Runtime.getRuntime().maxMemory()/1024 + " KB");
                JOptionPane.showMessageDialog(null, "<html>Out of memory.  This happened while importing the bitmap, and is is less than ideal.<br>Maybe the bitmap is too big to handle.<br>"
                    + "Memory in use was " + Runtime.getRuntime().totalMemory()/1024 + " KB out of " + Runtime.getRuntime().maxMemory()/1024 + " KB available.</html>", "Out of memory", JOptionPane.ERROR_MESSAGE);
                return;
            }
            finally
            {
                //always reset the title
                this.setTitle("Import Map From BMP");
                System.out.println("Reached finally after processing file");
            }
            
            if (bitmap.getBitsPerPixel() != 4 && bitmap.getBitsPerPixel() != 8)
            {
                //Can't use this bitmap; quit early
                JOptionPane.showMessageDialog(null, "The bitmap chosen must be a 16-colour  or 256-colour bitmap, but this one is a " + (int)Math.pow(2, bitmap.getBitsPerPixel()) + " colour bitmap.  You can shrink/expand the palette with the GIMP.");
                bitmap = null;
                return;
            }
            
            boolean keepGoing = checkWidthAndHeight();
            
            if (!keepGoing)
                return;
            
            //uh, i guess at this point, we've got the bitmap imported
            //so now we gotta get those colours
            paletteColors = bitmap.getPaletteColors();
            paletteSize = bitmap.getPaletteSize();
            
            colorFields = new JTextField[paletteSize];
            mapTo = new JComboBox[paletteSize];
            mdlMapToModel = new DefaultComboBoxModel[paletteSize];
            mappings = new int[paletteSize];
            
            for (int i = 0; i < paletteSize; i++)
            {
                colorFields[i] = new JTextField("");
                colorFields[i].setBackground(paletteColors[i]);
                colorFields[i].setForeground(paletteColors[i].brighter().brighter());
                colorFields[i].setEnabled(false);
                colorFields[i].setText("Color: (" + paletteColors[i].getRed() + ", " + paletteColors[i].getGreen() + ", " + paletteColors[i].getBlue() + ")");
                
                mapTo[i] = new JComboBox();
                
                mdlMapToModel[i] = new DefaultComboBoxModel();
                mdlMapToModel[i].addElement("Water");
                mdlMapToModel[i].addElement("Grassland");
                mdlMapToModel[i].addElement("Plains");
                mdlMapToModel[i].addElement("Desert");
                mdlMapToModel[i].addElement("Tundra");
                mdlMapToModel[i].addElement("Forest");
                mdlMapToModel[i].addElement("Jungle");
                mdlMapToModel[i].addElement("Hills");
                mdlMapToModel[i].addElement("Mountains");
                mdlMapToModel[i].addElement("Marsh");
                mdlMapToModel[i].addElement("Volcano");
                mdlMapToModel[i].addElement("Flood Plain");
                mdlMapToModel[i].addElement("Plains Forest");
                mdlMapToModel[i].addElement("Tundra Forest");
                mdlMapToModel[i].addElement("Pine Forest");
                mdlMapToModel[i].addElement("Tundra Pine Forest");
                
                mapTo[i].setModel(mdlMapToModel[i]);
                
                c.gridy++;
                c.gridwidth=1;
                c.gridx=0;
                panel.add(colorFields[i], c);
                c.gridx++;
                System.out.println("Adding to Gooey");
                panel.add(mapTo[i], c);
                
            }
            
            //set up pre-existing mappings (from previous sessions)
            try
            {
                Scanner s = new Scanner(new File("bmpcolors.txt"));
                while (s.hasNextLine())
                {
                    String str = s.nextLine();
                    if (!str.contains(","))  //empty line, probably at end of file
                        continue;
                    String[]sections = str.split(",");
                    int red = Integer.parseInt(sections[0]);
                    int green = Integer.parseInt(sections[1]);
                    int blue = Integer.parseInt(sections[2]);
                    int match = Integer.parseInt(sections[3]);
                    for (int i = 0; i < paletteSize; i++)
                    {
                        if (paletteColors[i].getRed() == red && paletteColors[i].getGreen() == green && paletteColors[i].getBlue() == blue)
                        {
                            if (match < NUM_TERR_CHOICES)
                                mapTo[i].setSelectedIndex(match);
                        }
                    }
                }
                s.close();
            }
            catch(Exception e)
            {   //todo: improve style so we finally close s; will need to declare it earlier
                logger.error("Error when inputting color history data");
            }
            
            //add advanced mode check box
            chkAdvanced.setVisible(true);
            chkAdvanced.addActionListener(new ActionListener()
            {
               public void actionPerformed(ActionEvent e)
               {
                    chkAdvancedMode();
               }
            });
            
            //and a button for the next step
            c.gridy++;
            c.gridx = 0;
            panel.add(cmdShazam, c);
            
            cmdShazam.addActionListener(new ActionListener()
            {
               public void actionPerformed(ActionEvent e)
               {
                   Instant startShazam = Instant.now();
                   cmdShazamAction();
                   Duration shazamTime = Duration.between(startShazam, Instant.now());
                   logger.info("Overall map creation time: " + shazamTime.toMillis() + " ms");
               }
            });
            
            cmdSelectFile.setEnabled(false);
            cmdSelectFile.setVisible(false);
            
            this.setPreferredSize(new Dimension(600, 900));
            this.pack();
        }
    }
    
    public static void main(String[]args)
    {
        new MapFromBMPForm().setVisible(true);
    }
    
    /**
     * Do the magic.  What do you expect with a method name like this???
     */
    private void Shazam()
    {
        //Export the settings to a file
        //Eventually we could spin this off to a separate thread, but it should be
        //fairly low-cost as-is
        try
        {
            FileOutputStream fos = new FileOutputStream("bmpcolors.txt");
            String s = "";
            for (int i = 0; i < this.paletteSize; i++)
            {
                s = "";
                s = s + paletteColors[i].getRed();
                s = s + ",";
                s = s + paletteColors[i].getGreen();
                s = s + ",";
                s = s + paletteColors[i].getBlue();
                s = s + ",";
                s = s + mapTo[i].getSelectedIndex();
                s = s + Main.newline;
                fos.write(s.getBytes());  //TODO: Specify charset for better platform-independence
            }
            fos.close();
        }
        catch(Exception e)
        {
            logger.error("Error when exporting color mappings", e);
        }
        
        int xCoordinate = 0, yCoordinate = 0;
        
        dominantTerrain = new int[mapWidth][mapHeight];  //we'll take care of the isometric stuff l8r

        long start = System.nanoTime();

        List<Coordinate> coordinates = new ArrayList<>();
        for (yCoordinate = 0; yCoordinate < mapHeight; yCoordinate++)
        {
            for (xCoordinate = (yCoordinate%2==0 ? 0 : 1); xCoordinate < mapWidth; xCoordinate+=2)
            {

                //If we get all the x/y coordinates, maybe we can lambdafy this
                Coordinate tile = new Coordinate();
                tile.x = xCoordinate;
                tile.y = yCoordinate;
                coordinates.add(tile);
            }
        }

        List<Integer> dominantTerrains = coordinates.parallelStream(). map(futureTile -> {
                    
            int top = 0;
            int stepsDown = 0, stepsOutwards = 0, stepsDownCount = 0;
            int leftStart = 0, rightStart = 0;
            int y = 0, x1 = 0, x2 = 0;

            if (logger.isDebugEnabled())
                logger.debug("X, Y is (" + futureTile.x + ", " + futureTile.y + ").  MapHeight is " + mapHeight);
            stepsDown = 31;
            stepsOutwards = 31;
            //stepsDownCount; //changed within inner loop

            top = futureTile.y * 16;  //top row should start at zero
            leftStart = futureTile.x * 32 + 31;
            rightStart = leftStart + 1;

            //so at 1, 1, we will be starting at (63, 16)

            int[]colorCount = new int[paletteSize];

            boolean firstAtThisLevel = false;

            while (stepsOutwards >= 0)
            {
                stepsDownCount = stepsDown;
                if (stepsDownCount == -1)  //special case to get the extra pixel outwards @ center
                {
                    stepsDownCount = 1;
                    firstAtThisLevel = false; //would be the ternary option if the bit had 3 options
                }
                while (stepsDownCount >= 0)
                {
                    y = top + (31 - stepsDownCount);
                    x1 = leftStart - (31 - stepsOutwards);
                    x2 = -1;  //indicates where we are if error occurs
                    int colorAtThisPixel = bitmap.getPaletteIndexAt(x1, y);
                    colorCount[colorAtThisPixel]++;
                    x2 = rightStart + (31 - stepsOutwards);
                    colorAtThisPixel = bitmap.getPaletteIndexAt(x2, y);
                    colorCount[colorAtThisPixel]++;

                    //now get the pixels below these two
                    stepsDownCount--;
                }
                if (!firstAtThisLevel)
                {
                    stepsOutwards--;  //first iter: will now be 30
                    stepsDown-=2;     //decrease twice b/c otherwise we'd always wind up at the bottom
                    firstAtThisLevel = true;
                }
                else
                {
                    stepsOutwards--;
                    //leave stepsDown the same
                    firstAtThisLevel = false;
                }
            }
            //the last iteration, stepsDown will be 1 and stepsOutwards will be 1.
            //Then we need one more iteration for the 3rd @ center.
            //So override it.

            //we're still at the per-tile level

            //now figure out the dominant color

            int dominantTerrainCount[] = new int[NUM_TERR_CHOICES];

            //we should account for teh mappings

            //Add up the count from each color, according to its mapping, to see which type of terrain is most common in this block
            for (int i = 0; i < paletteSize; i++)
            {
                dominantTerrainCount[mappings[i]] += colorCount[i];
            }

            int maxCount = 0;
            int maxIndex = -1;
            for (int i = 0; i < dominantTerrainCount.length; i++)
            {
                if (dominantTerrainCount[i] > maxCount)
                {
                    maxCount = dominantTerrainCount[i];
                    maxIndex = i;
                }
            }

            //now assign it
            //dominantTerrain[xCoordinate][yCoordinate] = maxIndex;
            return maxIndex;
        }).collect(Collectors.toList());
            
        int i = 0;
        for (yCoordinate = 0; yCoordinate < mapHeight; yCoordinate++)
        {
            for (xCoordinate = (yCoordinate%2==0 ? 0 : 1); xCoordinate < mapWidth; xCoordinate+=2)
            {
                dominantTerrain[xCoordinate][yCoordinate] = dominantTerrains.get(i);
                i++;
            }
        }
            
        long end = System.nanoTime();
        logger.info("Dominant terrain determination time: " + (end-start)/1000000 + " ms");
    }
    
    private void cmdShazamAction()
    {
        //get the mappings
        for (int i = 0; i < mappings.length; i++)
        {
            mappings[i] = mapTo[i].getSelectedIndex();
        }
        
        int bmpWidth = bitmap.getWidth();
        int bmpHeight = bitmap.getHeight();
        
        bmpWidth-=32;
        bmpHeight-=16;
        
        if (bmpWidth%32 != 0)
        {
            logger.error("Bitmap width - 32 is not a multiple of 32");
            JOptionPane.showMessageDialog(null, "The bitmap width of " + bitmap.getWidth() + " minus 32, is not a multiple of 32");
        }
        if (bmpHeight%16 != 0)
        {
            logger.error("Bitmap height - 16 is not a multiple of 16");
            JOptionPane.showMessageDialog(null, "The bitmap height of " + bitmap.getHeight() + " minus 16, is not a multiple of 16");
        }
        
        mapWidth = bmpWidth/32;
        mapHeight = bmpHeight/16;
        
        logger.info("Map width calculated as " + mapWidth + " based on bitmap width of " + bitmap.getWidth());
        logger.info("Map height calculated as " + mapHeight + " based on bitmap height of " + bitmap.getHeight());
        
        this.setTitle("Reticulating splines...");
        Shazam();
        
        //umm so now we got our dominant terrains
        //I think what that what's left is transferring it to the BIQ
        
        //but for now, let's print it out instead
        //for the bmp, we'll still have to take into account when you simply cannot have terrains next to each other due to Civ3 limitations
        
        //printOutTerrains();
        
        printOutStats();
        
        //Now create it
        //Assume we already gots ourself a BIQ
        this.setTitle("Creating map data...");
        Instant startBIQ = Instant.now();
        createBIQMap();
        Instant endBIQ = Instant.now();
        Duration biqTime = Duration.between(startBIQ, endBIQ);
        logger.info("BIQ creation time: " + biqTime.toMillis() + " ms");
        this.setTitle("Map created!");
        
        Main.enableMapTab();
        Main.pnlTabs.biqcTab.updateCheckBoxes();
        Main.pnlTabs.gameTab.sendMapData(biqFile.get(biqIndex).worldCharacteristic);
    }
    
    private void printOutTerrains()
    {        
        for (int yCoordinate = 0; yCoordinate < mapHeight; yCoordinate++)
        {
            for (int xCoordinate = (yCoordinate%2==0 ? 0 : 1); xCoordinate < mapWidth; xCoordinate+=2)
            {
                String terrainName = terrainNames[dominantTerrain[xCoordinate][yCoordinate]];
                System.out.println("Dominant terrain at (" + xCoordinate + ", " + yCoordinate + ") is " + terrainName + ".");
            }    
        }
    }
    
    private void printOutStats()
    {
            
        int count[] = new int[NUM_TERR_CHOICES];
        
        for (int yCoordinate = 0;yCoordinate < mapHeight; yCoordinate++)
        {
            for (int xCoordinate = (yCoordinate%2==0 ? 0 : 1); xCoordinate < mapWidth; xCoordinate+=2)
            {
                int terr = dominantTerrain[xCoordinate][yCoordinate];
                count[terr]++;
            }    
        }
        
        for (int i = 0; i < count.length; i++)
        {
            double percent = (count[i]*100.0)/(mapWidth*mapHeight*0.5);
            String percentFormat = String.format("0.00", percent);
            if (!chkAdvanced.isSelected())
                System.out.println("There were " + count[i] + " tiles of type " + terrainNames[i] + " for a percentage of " + percentFormat + "(" + percent + ")");
        }
    }
    
    private void createBIQMap()
    {
        IO biqFile = Main.biqFile.get(0);
        biqFile.createMap(mapWidth, mapHeight, 0);  //base terrain doesn't matter since we're just gonna overwrite it anyway
        
        int yCoordinate, xCoordinate;
        for (yCoordinate = 0; yCoordinate < mapHeight; yCoordinate++)
        {
            for (xCoordinate = (yCoordinate%2==0 ? 0 : 1); xCoordinate < mapWidth; xCoordinate+=2)
            {
                int terrain = dominantTerrain[xCoordinate][yCoordinate];  
                //adj+if 5/21/13 - Mod 18 b/c we'll tack landmark on later
                if (this.chkAdvanced.isSelected())
                    terrain = terrain % 18;
                int base = 0;  //NOTE: Using sensible base/real here.  They are flipped in most other places because I had 'em flipped three years ago and I've never fixed that since it's only confusing, not broken.
                int real = 0;  //Don't fix what ain't broken.  Unless you need to.
                boolean pine = false;  //Flag for whether the forest is a pine forest
                boolean snow = false;  //Flag for snow-capped mountains
                if (terrain == WATER_DOMINANT)
                {
                    base = TERR.COAST;
                    real = TERR.COAST;
                }
                else if (terrain == GRASSLAND_DOMINANT)
                {
                    base = TERR.GRASSLAND;
                    real = TERR.GRASSLAND;
                }
                else if (terrain == PLAINS_DOMINANT)
                {
                    base = TERR.PLAINS;
                    real = TERR.PLAINS;
                }
                else if (terrain == DESERT_DOMINANT)
                {
                    base = TERR.DESERT;
                    real = TERR.DESERT;
                }
                else if (terrain == TUNDRA_DOMINANT)
                {
                    base = TERR.TUNDRA;
                    real = TERR.TUNDRA;
                }
                else if (terrain == FOREST_DOMINANT)
                {
                    base = TERR.GRASSLAND;
                    real = TERR.FOREST;
                }
                else if (terrain == JUNGLE_DOMINANT)
                {
                    base = TERR.GRASSLAND;
                    real = TERR.JUNGLE;
                }
                else if (terrain == HILL_DOMINANT)
                {
                    base = TERR.GRASSLAND;
                    real = TERR.HILLS;
                }
                else if (terrain == MOUNTAIN_DOMINANT)
                {
                    base = TERR.GRASSLAND;
                    real = TERR.MOUNTAIN;
                }
                else if (terrain == MARSH_DOMINANT)
                {
                    base = TERR.GRASSLAND;
                    real = TERR.MARSH;
                }
                else if (terrain == VOLCANO_DOMINANT)
                {
                    base = TERR.GRASSLAND;
                    real = TERR.VOLCANO;
                }
                else if (terrain == FLOOD_PLAIN_DOMINANT)
                {
                    base = TERR.DESERT;
                    real = TERR.FLOODPLAIN;
                }
                else if (terrain == PLAINS_FOREST)
                {
                    base = TERR.PLAINS;
                    real = TERR.FOREST;
                }
                else if (terrain == TUNDRA_FOREST)
                {
                    base = TERR.TUNDRA;
                    real = TERR.FOREST;
                }
                else if (terrain == PINE_FOREST)
                {
                    base = TERR.GRASSLAND;
                    real = TERR.FOREST;
                    pine = true;
                }
                else if (terrain == TUNDRA_PINE_FOREST)
                {
                    base = TERR.TUNDRA;
                    real = TERR.FOREST;
                    pine = true;
                }
                else if (terrain == PLAINS_PINE_FOREST)
                {
                    base = TERR.PLAINS;
                    real = TERR.FOREST;
                    pine = true;
                }
                else if (terrain == SNOW_MOUNTAINS)
                {
                    base = TERR.GRASSLAND;
                    real = TERR.MOUNTAIN;
                    snow = true;
                }
                else if (terrain == PLAINS_JUNGLE)
                {
                    base = TERR.PLAINS;
                    real = TERR.JUNGLE;
                }
                else if (terrain == TUNDRA_JUNGLE)
                {
                    base = TERR.TUNDRA;
                    real = TERR.JUNGLE;
                }

                int tileIndex = biqFile.calculateTileIndex(xCoordinate, yCoordinate);
                biqFile.tile.get(tileIndex).setTerrain(base, real);
                biqFile.tile.get(tileIndex).setPine(pine);
                biqFile.tile.get(tileIndex).setSnow(snow);
                if (chkAdvanced.isSelected() && dominantTerrain[xCoordinate][yCoordinate] >= 18)
                    biqFile.tile.get(tileIndex).setLandmark(true);
            }
        }
        
        coastToSea();
        seaToOcean();
        
        resolveTundra();
        
        for (yCoordinate = 0; yCoordinate < mapHeight; yCoordinate++)
        {
            for (xCoordinate = (yCoordinate%2==0 ? 0 : 1); xCoordinate < mapWidth; xCoordinate+=2)
            {
                MapUtils.recalculateFileAndIndex(Main.biqFile.get(0), xCoordinate, yCoordinate);
            }
        }
    }
    
    private void coastToSea()
    {
        int xCoordinate, yCoordinate;
        IO biq = Main.biqFile.get(Main.biqIndex);
        for (yCoordinate = 0; yCoordinate < mapHeight; yCoordinate++)
        {
            for (xCoordinate = (yCoordinate%2==0 ? 0 : 1); xCoordinate < mapWidth; xCoordinate+=2)
            {
                int index = biq.calculateTileIndex(xCoordinate, yCoordinate);
                if (biq.tile.get(index).getBaseTerrain() == TERR.COAST)
                {
                    //see if it's surrounded by coast
                    List<Integer>surroundingTiles = biq.getSurroundingTiles(xCoordinate, yCoordinate);
                    boolean allCoast = true;
                    for (int i = 0; i < surroundingTiles.size(); i++)
                    {
                        int terr = biq.tile.get(surroundingTiles.get(i)).getBaseTerrain();
                        if ((terr != TERR.COAST) && (terr != TERR.SEA))
                        {
                            allCoast = false;
                        }
                    }
                    
                    //
                    if (allCoast)
                    {   //convert to sea
                        biq.tile.get(index).setTerrain(TERR.SEA, TERR.SEA);
                    }
                }
            }
        }
    }
    
    private void seaToOcean()
    {
        int xCoordinate, yCoordinate;
        IO biq = Main.biqFile.get(Main.biqIndex);
        for (yCoordinate = 0; yCoordinate < mapHeight; yCoordinate++)
        {
            for (xCoordinate = (yCoordinate%2==0 ? 0 : 1); xCoordinate < mapWidth; xCoordinate+=2)
            {
                int index = biq.calculateTileIndex(xCoordinate, yCoordinate);
                if (biq.tile.get(index).getBaseTerrain() == TERR.SEA)
                {
                    //see if it's surrounded by coast
                    List<Integer>surroundingTiles = biq.getSurroundingTiles(xCoordinate, yCoordinate);
                    boolean allCoast = true;
                    for (int i = 0; i < surroundingTiles.size(); i++)
                    {
                        int terr = biq.tile.get(surroundingTiles.get(i)).getBaseTerrain();
                        if ((terr != TERR.OCEAN) && (terr != TERR.SEA))
                        {
                            allCoast = false;
                        }
                    }
                    
                    //
                    if (allCoast)
                    {   //convert to sea
                        biq.tile.get(index).setTerrain(TERR.OCEAN, TERR.OCEAN);
                    }
                }
            }
        }
    }
    
    /**
     * Tundra can only be placed in certain places.
     * This method makes sure we don't have any ugly tundrii
     * When we encounter a tundra that borders either plains or desert, we shall
     * change said tundra to grassland.
     */
    private void resolveTundra()
    {
        IO biq = Main.biqFile.get(Main.biqIndex);
        int yCoordinate = 0, xCoordinate = 0;
        for (yCoordinate = 0; yCoordinate < mapHeight; yCoordinate++)
        {
            for (xCoordinate = (yCoordinate%2==0 ? 0 : 1); xCoordinate < mapWidth; xCoordinate+=2)
            {
                MapUtils.recalculateFileAndIndex(Main.biqFile.get(0), xCoordinate, yCoordinate);
                int idx = biq.calculateTileIndex(xCoordinate, yCoordinate);
                if (biq.tile.get(idx).getBaseTerrain() == 3)
                {
                    List<Integer> neighbors = biq.getSurroundingTiles(xCoordinate, yCoordinate);
                    for (int i : neighbors)
                    {
                        int terrain = biq.tile.get(i).getBaseTerrain();
                        if (terrain == 1 || terrain == 0)  //plains or desert
                        {
                            biq.tile.get(idx).setTerrain(2, 2);
                            break;
                        }
                    }
                }
                
                //also check for if this tile and N/NW/NE are desert, grassland, plains, *and* coast.
                //if that is the case, then we will change this tile to plains, or if it is plains, to desert
                //question: might that *still* result in a problem?  TODO: determine fo' sho
                boolean hasGrassland = false;
                boolean hasPlains = false;
                boolean hasDesert = false;
                boolean hasCoast = false;
                byte hasCount = 0;
                if (biq.tile.get(idx).getBaseTerrain() == 11)
                {
                    hasCoast = true;
                    hasCount++;
                }
                else if (biq.tile.get(idx).getBaseTerrain() == 0)
                {
                    hasDesert = true;
                    hasCount++;
                }
                else if (biq.tile.get(idx).getBaseTerrain() == 1)
                {
                    hasPlains = true;
                    hasCount++;
                }
                else if (biq.tile.get(idx).getBaseTerrain() == 2)
                {
                    hasGrassland = true;
                    hasCount++;
                }
                if (hasCount == 1)
                {
                    
                    List<Integer> northTiles = biq.getNorthNENW(xCoordinate, yCoordinate);
                    for (int i : northTiles)
                    {
                        int terrain = biq.tile.get(i).getRealTerrain();
                        if (terrain == 0 && !hasDesert)
                        {
                            hasDesert = true;
                            hasCount++;
                        }
                        if (terrain == 1 && !hasPlains)
                        {
                            hasPlains = true;
                            hasCount++;
                        }
                        if (terrain == 2 && !hasGrassland)
                        {
                            hasGrassland = true;
                            hasCount++;
                        }
                        if (terrain == 11 && !hasCoast)
                        {
                            hasCoast = true;
                            hasCount++;
                        }
                    }
                }
                if (hasCount == 4)
                {
                    if (biq.tile.get(idx).getRealTerrain() == 0 || (biq.tile.get(idx).getRealTerrain() == 2))
                    {
                        biq.tile.get(idx).setTerrain(1, 1);
                    }
                    else if (biq.tile.get(idx).getRealTerrain() == 1)
                    {
                        biq.tile.get(idx).setTerrain(0, 0);
                    }
                    //TODO: else for the coast case... 
                }
            }
        }
    }
    
    private boolean checkWidthAndHeight()
    {
        boolean widthOK = true;
        boolean heightOK = true;
        
        int bmpWidth = bitmap.getWidth();
        int bmpHeight = bitmap.getHeight();
        int mapWidth = 0;
        int mapHeight = 0;

        bmpWidth-=32;
        bmpHeight-=16;

        if (bmpWidth%32 != 0)
            widthOK = false;
        if (bmpHeight%16 != 0)
            heightOK = false;
        
        if (!widthOK || !heightOK)
        {
            JOptionPane.showMessageDialog(null, "<html>The width of the bitmap must be divisible by 32, and the height by 16, but this is not the case.<br>"
                + "The width should be 32*Civ3MapWidth + 32, and the height should be 16*Civ3MapWidth + 16<br>"
                + "Please modify the bitmap so this is the case.</html>");
            return false;
        }
        
        mapWidth = bmpWidth/32;
        mapHeight = bmpHeight/16;
        if (mapWidth % 2 == 1 || mapHeight % 2 == 1)
        {
            JOptionPane.showMessageDialog(null, "<html>Either the width of the height of the Civ3 map would be an odd number, which Civ3 doesn't allow.<br>"
                + "The width should be 32*Civ3MapWidth + 32, and the height should be 16*Civ3MapWidth + 16<br>"
                + "Please adjust the bitmap so the Civ3 map will have an even width and height.</html>");
            return false;
        }
        if ((mapWidth * mapHeight)/2 > 65536)
        {
            JOptionPane.showMessageDialog(null, "<html>The bitmap is too big - the Civ3 map would have " + ((mapWidth*mapHeight)/2) + " tiles, which is bigger than the 65536 that Civ3 allows.<br>"
                + "The width should be 32*Civ3MapWidth + 32, and the height should be 16*Civ3MapWidth + 16<br>"
                + "Please make the bitmap smaller, so it can be used to create a Civ3 map.</html>");
            return false;
        }
        return true;
    }

    public void chkAdvancedMode()
    {
        chkAdvanced.setEnabled(false);
        NUM_TERR_CHOICES = 40;  //REM: update as more options added here
        for (int i = 0; i < paletteSize; i++)
        {
//            mdlMapToModel[i].removeAllElements();
//            mdlMapToModel[i].addElement("Water");
//            mdlMapToModel[i].addElement("Grassland");
//            mdlMapToModel[i].addElement("Plains");
//            mdlMapToModel[i].addElement("Desert");
//            mdlMapToModel[i].addElement("Tundra");
//            mdlMapToModel[i].addElement("Forest");
//            mdlMapToModel[i].addElement("Jungle");
//            mdlMapToModel[i].addElement("Hills");
//            mdlMapToModel[i].addElement("Mountains");
//            mdlMapToModel[i].addElement("Marsh");
//            mdlMapToModel[i].addElement("Volcano");
//            mdlMapToModel[i].addElement("Flood Plain");
//            mdlMapToModel[i].addElement("Plains Forest");
//            mdlMapToModel[i].addElement("Tundra Forest");
//            mdlMapToModel[i].addElement("Pine Forest (on Grassland)");
//            mdlMapToModel[i].addElement("Tundra Pine Forest");
            //17th below
            mdlMapToModel[i].addElement("Plains Pine Forest");
            mdlMapToModel[i].addElement("Snow-Capped Mountains");
            
            
            mdlMapToModel[i].addElement("LM Water");
            mdlMapToModel[i].addElement("LM Grassland");
            mdlMapToModel[i].addElement("LM Plains");
            mdlMapToModel[i].addElement("LM Desert");
            mdlMapToModel[i].addElement("LM Tundra");
            mdlMapToModel[i].addElement("LM Forest");
            mdlMapToModel[i].addElement("LM Jungle");
            mdlMapToModel[i].addElement("LM Hills");
            mdlMapToModel[i].addElement("LM Mountains");
            mdlMapToModel[i].addElement("LM Marsh");
            mdlMapToModel[i].addElement("LM Volcano");
            mdlMapToModel[i].addElement("LM Flood Plain");
            mdlMapToModel[i].addElement("LM Plains Forest");
            mdlMapToModel[i].addElement("LM Tundra Forest");
            mdlMapToModel[i].addElement("LM Pine Forest");
            mdlMapToModel[i].addElement("LM Tundra Pine Forest");
            mdlMapToModel[i].addElement("LM Plains Pine Forest");
            mdlMapToModel[i].addElement("LM Snow-Capped Mountains");
            //the above is the 40th
        }
    }
}