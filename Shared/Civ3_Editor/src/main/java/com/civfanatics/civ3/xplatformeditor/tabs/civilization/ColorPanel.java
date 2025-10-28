/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.xplatformeditor.tabs.civilization;

import com.civfanatics.civ3.xplatformeditor.tabs.civilization.BalanceAdjuster;
import com.civfanatics.civ3.xplatformeditor.tabs.civilization.HueAdjuster;
import com.civfanatics.civ3.xplatformeditor.tabs.civilization.SaturationAdjuster;
import com.civfanatics.civ3.biqFile.IO;
import com.civfanatics.civ3.xplatformeditor.tabs.map.ColourChooser;
import com.civfanatics.civ3.xplatformeditor.Main;
import com.civfanatics.civ3.xplatformeditor.OldPCXFilter;
import com.civfanatics.civ3.xplatformeditor.utils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.border.LineBorder;

/**
 * This is a form that allows changing Civ colours.
 * @since 0.81
 * @author Andrew
 */
public class ColorPanel extends JFrame {
    
    static final String fileSlash = System.getProperty("file.separator");
    GridBagConstraints gbc = new GridBagConstraints();
    JScrollPane scrollPane = new JScrollPane();
    JPanel scrollable = new JPanel();
    JPanel pnlThingsThatScroll = new JPanel();
    static ColourChooser cc = new ColourChooser(null, true);
    final String strShouldRemainSame = "Should remain the same for all colors - do not change";
    final String strShadeOfGray = "Shade of gray - changing not recommended";
    final String strColor = "Color ";
    boolean existingCopy = false;
    String pcxName = "";
    OldPCXFilter pcx;
    
    private int MAX_WIDTH = 6;
    
    JCheckBox chkRhye = new JCheckBox("Rhye's Recommendations");
    JCheckBox chkShadesOfGray = new JCheckBox("Shades of Gray");
    JCheckBox chkShouldntChange = new JCheckBox("Shouldn't Change");
    JCheckBox chkMainColor = new JCheckBox("Shades of main color");
    JCheckBox chkOtherUsefulColors = new JCheckBox("Other useful colors");
    JCheckBox chkAllOthers = new JCheckBox("All others");
    
    JButton cmdUndo = new JButton("Undo");
    
    Integer[]mainColors = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    Integer[]otherColors = {64,65,66,67,68,69};
    Integer[]rhyes = {10, 11, 12, 13, 14, 15, 20, 24, 26, 30, 32, 38, 44, 48, 50, 56, 60, 62};
    Integer[]grays = {17, 19, 21, 23, 25, 27, 29, 31};
    Integer[]shouldnt = {31, 33, 35, 37, 39, 41, 43, 45, 47, 49, 51, 53, 55, 57, 59, 61, 63};
    Integer[]allOthers = {16, 18, 22, 28, 34, 36, 40, 42, 46, 52, 54, 58};
    
    String[]pcxNames = {"ntp00", "ntp01", "ntp02", "ntp03", "ntp04", "ntp05", "ntp06", "ntp07", "ntp08", "ntp09", "ntp10", "ntp11", "ntp12", "ntp13", "ntp14", "ntp15", "ntp16", "ntp17", "ntp18", "ntp19", "ntp20", "ntp21", "ntp22", "ntp23", "ntp24", "ntp25", "ntp26", "ntp27", "ntp28", "ntp29", "ntp30", "ntp31"};
    
    int[][]backup = new int[70][3];
    int[][]rgb = new int[70][3];    //teh colours
    JLabel[]lblLabels = new JLabel[70];
    JTextField[][]txtFields = new JTextField[70][3];
    JLabel[]lblColors = new JLabel[70];
    Color[]palette;
    
    String pcxLocation;
    
    static IO biq; //might make it unstatic l8r
    
    public ColorPanel()
    {
        super();
        
        
        this.addWindowListener(new WindowListener(){
            public void windowDeactivated(WindowEvent e)
            {
                
            }
            public void windowOpened(WindowEvent e)
            {
                ;
            }

            public void windowClosing(WindowEvent e)
            {
                savePCX();
            }

            public void windowClosed(WindowEvent e)
            {
                ;
            }

            public void windowIconified(WindowEvent e)
            {
                ;
            }

            public void windowDeiconified(WindowEvent e)
            {
                ;
            }

            public void windowActivated(WindowEvent e)
            {
                ;
            }
        });
        
        setPreferredSize(new Dimension(812, 740));
        GridBagLayout gbl = new GridBagLayout();
        
        JViewport jv = new JViewport();
        scrollPane.setViewport(jv);
        scrollPane.setViewportView(scrollable);
        
        scrollable.setLayout(gbl);
        //getContentPane().add(scrollPane);
        pack();
        setTitle("Color Palette Editor");
        initComponents();
        Main.mainMain.currentHelp = Main.mainMain.help_ColorPalette;
    }
    
    //can't use BIQ b/c can't use this - use bic instead
    public static void setBIQ(IO bic)
    {
        biq = bic;
    }
    
    public ColorPanel(int color)
    {
        String suffix = ((Integer)color).toString();
        if (suffix.length() == 0)
        {
            suffix = "0" + suffix + ".pcx";
        }
        //now search for the PCX
        try
        {
            pcxName = utils.findFile(suffix, "Art" + fileSlash + "units" + fileSlash + "palettes" + fileSlash, biq);
        }
        catch(FileNotFoundException e)
        {
            
        }
        JOptionPane.showMessageDialog(null, pcxName, "Title", JOptionPane.INFORMATION_MESSAGE);
    }
    
    //So the GUI can be tested w/o the main proggie
    public static void main(String[]args)
    {
        new ColorPanel();
    }
    
    private void savePCX()
    {
        for (int i = 0; i < 70; i++)
        {
            palette[i] = new Color(rgb[i][0], rgb[i][1], rgb[i][2]);
        }
        pcx.palette.palette = palette;
        
        pcx.exportPCX(pcxLocation);
    }
    
    public void initComponents()
    {
        
        //pnlThingsThatScroll.add(scrollPane);
        
        this.setLayout(new GridBagLayout());
        
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.1;
        
        detectExistingCopy();
        
        JTextArea txtInstructions = new JTextArea("Change colours here!  Click a color to change it, or use Adjust Hue/Saturation/Balance to change all colors at once.\nSee http://forums.civfanatics.com/showpost.php?p=1756301&postcount=14 for more info!");
        txtInstructions.setEditable(false);
        txtInstructions.setRows(2);
        txtInstructions.setWrapStyleWord(true);
        //scrollable.setBackground(Color.red);
        
        gbc.gridwidth = MAX_WIDTH - 1;
        this.add(txtInstructions, gbc);
        
        gbc.gridx = 5;
        gbc.gridwidth = 1;
        this.add(cmdUndo, gbc);
        
        JLabel lblSelectColor = new JLabel("Select color: ");
        final JComboBox cmbColors = new JComboBox();
        
        JButton cmdHue = new JButton("Adjust Hue");
        JButton cmdSaturation = new JButton("Adjust Saturation");
        JButton cmdBalance = new JButton("Adjust Balance");
        JButton cmdMonotone = new JButton("Set Monotone");
        
        cmbColors.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent e)
           {
                System.out.println("loading color");
                savePCX();
                loadColor(cmbColors.getSelectedIndex());
           }
        });
        
        cmdHue.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent e)
           {
                cmdHueAction();
           }
        });
        
        cmdSaturation.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent e)
           {
                cmdSaturationAction();
           }
        });
        
        cmdBalance.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent e)
           {
                cmdBalanceAction();
           }
        });
        
        cmdMonotone.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent e)
           {
                cmdMonotoneAction();
           }
        });
        
        cmdUndo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 70; i++) {
                    System.arraycopy(backup[i], 0, rgb[i], 0, 3);
                }
                
                for (int i = 0; i < 70; i++) {
                    txtFields[i][0].setText(rgb[i][0] + "");
                    txtFields[i][1].setText(rgb[i][1] + "");
                    txtFields[i][2].setText(rgb[i][2] + "");
                    Color c = new Color(rgb[i][0], rgb[i][1], rgb[i][2]);
                    lblColors[i].setBackground(c);
                }
                cmdUndo.setEnabled(false);
            }
        });
        
        DefaultComboBoxModel cmbChooseColor = new DefaultComboBoxModel();
        for (int i = 0; i < 32 ; i++)
        {
            cmbChooseColor.addElement(i);
        }
        cmbColors.setModel(cmbChooseColor);
        
        gbc.gridwidth = 1;
        gbc.gridy+=2;
        gbc.gridx = 0;
        
        this.add(lblSelectColor, gbc);
        gbc.gridx++;
        this.add(cmbColors, gbc);
        gbc.gridx+=1;
        this.add(cmdHue, gbc);
        gbc.gridx+=1;
        this.add(cmdSaturation, gbc);
        gbc.gridx+=1;
        this.add(cmdBalance, gbc);
        gbc.gridx+=1;
        this.add(cmdMonotone, gbc);
        
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 1;
        this.add(chkRhye, gbc);
        gbc.gridx++;
        this.add(chkMainColor, gbc);
        gbc.gridx++;
        this.add(chkOtherUsefulColors, gbc);
        gbc.gridx++;
        this.add(chkShadesOfGray, gbc);
        gbc.gridx++;
        this.add(chkShouldntChange, gbc);
        gbc.gridx++;
        this.add(chkAllOthers, gbc);
        chkMainColor.setSelected(true);
        chkOtherUsefulColors.setSelected(true);
        
        chkRhye.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                updateVisibles();
            }
        });
        chkMainColor.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                updateVisibles();
            }
        });
        chkOtherUsefulColors.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                updateVisibles();
            }
        });
        chkShadesOfGray.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                updateVisibles();
            }
        });
        chkShouldntChange.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                updateVisibles();
            }
        });
        chkAllOthers.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                updateVisibles();
            }
        });
        
        
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weighty = 0.9;
        gbc.weightx = 0.1;
        gbc.gridwidth = MAX_WIDTH;
        this.add(scrollPane, gbc);
        
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        
        addColours();
        loadColor(0);
        
        
        updateVisibles();
        //include an indicator of which file
        pack();
    }
    
    private void loadColor(int index)
    {
        String pcxString = null;
        try{
            pcxString = utils.findFile(this.pcxNames[index] + ".pcx", "Art" + fileSlash + "units" + fileSlash + "palettes" + fileSlash,  biq);
        }
        catch(FileNotFoundException e){
            JOptionPane.showMessageDialog(null, "Couldn't find it "+ e.getMessage());
        }
        this.pcxLocation = pcxString;
        
        pcx = new OldPCXFilter(pcxString);
        pcx.readFile();
        pcx.parse();
        
        System.out.println("loaded");
        palette = pcx.palette.palette;
        for (int i = 0; i < 70; i++)
        {
            System.out.println("RGB: " + palette[i].getRed() + ", " + palette[i].getGreen() + ", " + palette[i].getBlue());
            rgb[i][0] = palette[i].getRed();
            rgb[i][1] = palette[i].getGreen();
            rgb[i][2] = palette[i].getBlue();
            txtFields[i][0].setText(rgb[i][0] + "");
            txtFields[i][1].setText(rgb[i][1] + "");
            txtFields[i][2].setText(rgb[i][2] + "");
            lblColors[i].setBackground(new Color(palette[i].getRGB()));
        }
        cmdUndo.setEnabled(false);
    }
    
    private void updateVisibles()
    {
        Set<Integer>makeVisible = new HashSet<Integer>();
        if (chkMainColor.isSelected())
            makeVisible.addAll(Arrays.asList(mainColors));
        if (chkOtherUsefulColors.isSelected())
            makeVisible.addAll(Arrays.asList(otherColors));
        if (chkRhye.isSelected())
            makeVisible.addAll(Arrays.asList(rhyes));
        if (chkShadesOfGray.isSelected())
            makeVisible.addAll(Arrays.asList(grays));
        if (chkShouldntChange.isSelected())
            makeVisible.addAll(Arrays.asList(shouldnt));
        if (chkAllOthers.isSelected())
            makeVisible.addAll(Arrays.asList(allOthers));
        
        for (int i = 0; i < 70; i++)
        {
            if (makeVisible.contains(i))
            {
                lblLabels[i].setVisible(true);
                lblColors[i].setVisible(true);
                txtFields[i][0].setVisible(true);
                txtFields[i][1].setVisible(true);
                txtFields[i][2].setVisible(true);
            }
            else
            {
                lblLabels[i].setVisible(false);
                lblColors[i].setVisible(false);
                txtFields[i][0].setVisible(false);
                txtFields[i][1].setVisible(false);
                txtFields[i][2].setVisible(false);
            }
        }
    }
    
    private void detectExistingCopy()
    {
        
    }
    
    private void addColours()
    {
        gbc = new GridBagConstraints();
        int id = 0;
        gbc.gridy++;
        addRow(id + ": " + strColor, id);  //0
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);  //2
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);  //4
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);  //5
        gbc.gridy++;
        id++;
        addRow(id + ": " +  "Color of starting location/civ color in editor", id);    //id = 6
        gbc.gridy++;
        id++;
        addRow(id + ": " +  "Color of pixel in PCX file", id);
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);
        gbc.gridy++;
        id++;
        addRow(id + ": " +  "Color of circle around leaderhead on Diplomacy screen", id);  //id = 9
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);  //12
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);  //15
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);    //n = 16, base 0
        gbc.gridy++;
        id++;
        addRow(id + ": " + strShadeOfGray, id);  //17
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);
        gbc.gridy++;
        id++;
        addRow(id + ": " + strShadeOfGray, id); //19
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);
        gbc.gridy++;
        id++;
        addRow(id + ": " + strShadeOfGray, id);  //21
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);
        gbc.gridy++;
        id++;
        addRow(id + ": " + strShadeOfGray, id);   //23
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);  //24
        gbc.gridy++;
        id++;
        addRow(id + ": " + strShadeOfGray, id);
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);  //26
        gbc.gridy++;
        id++;
        addRow(id + ": " + strShadeOfGray, id);
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);  //28
        gbc.gridy++;
        id++;
        addRow(id + ": " + strShadeOfGray, id);
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);  //n = 30
        gbc.gridy++;
        id++;
        addRow(id + ": " + strShadeOfGray, id);  //n = 31
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);    //32
        gbc.gridy++;
        id++;
        addRow(id + ": " + strShouldRemainSame, id); //33
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);   //34
        gbc.gridy++;
        id++;
        addRow(id + ": " + strShouldRemainSame, id); //35
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);
        gbc.gridy++;
        id++;
        addRow(id + ": " + strShouldRemainSame, id);  //37
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);
        gbc.gridy++;
        id++;
        addRow(id + ": " + strShouldRemainSame, id);  //39
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);
        gbc.gridy++;
        id++;
        addRow(id + ": " + strShouldRemainSame, id);  //41
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);
        gbc.gridy++;
        id++;
        addRow(id + ": " + strShouldRemainSame, id);  //43
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);
        gbc.gridy++;
        id++;
        addRow(id + ": " + strShouldRemainSame, id);  //45
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);
        gbc.gridy++;
        id++;
        addRow(id + ": " + strShouldRemainSame, id);  //47
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);
        gbc.gridy++;
        id++;
        addRow(id + ": " + strShouldRemainSame, id);  //49
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);
        gbc.gridy++;
        id++;
        addRow(id + ": " + strShouldRemainSame, id);  //51
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);
        gbc.gridy++;
        id++;
        addRow(id + ": " + strShouldRemainSame, id);  //53
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);
        gbc.gridy++;
        id++;
        addRow(id + ": " + strShouldRemainSame, id);  //55
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);
        gbc.gridy++;
        id++;
        addRow(id + ": " + strShouldRemainSame, id);  //57
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);
        gbc.gridy++;
        id++;
        addRow(id + ": " + strShouldRemainSame, id);  //59
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);
        gbc.gridy++;
        id++;
        addRow(id + ": " + strShouldRemainSame, id);  //61
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor, id);
        gbc.gridy++;
        id++;
        addRow(id + ": " + strShouldRemainSame, id);  //63
        gbc.gridy++;
        id++;
        addRow(id + ": " +  "Inner pixels of city borders (in-game)", id);  //n-64
        gbc.gridy++;
        id++;
        addRow(id + ": " +  "Outer pixel of city borders (in-game)", id);
        gbc.gridy++;
        id++;
        addRow(id + ": " +  "City for unit discs, histographs, and mini-map", id);  //66
        gbc.gridy++;
        id++;
        addRow(id + ": " + strColor + " - unknown", id);
        gbc.gridy++;
        id++;
        addRow(id + ": " +  "City color", id);  //68
        gbc.gridy++;
        id++;
        addRow(id + ": " +  "Civ name in histograph", id);
    }
    
    private void addRow(String title, final int id)
    {
        JLabel label = new JLabel(title);
        lblLabels[id] = label;
        final JTextField txtRed = new JTextField(((Integer)this.rgb[id][0]).toString());
        final JTextField txtGreen = new JTextField(((Integer)this.rgb[id][1]).toString());
        final JTextField txtBlue = new JTextField(((Integer)this.rgb[id][2]).toString());
        txtRed.setEditable(false);
        txtGreen.setEditable(false);
        txtBlue.setEditable(false);
        txtFields[id][0] = txtRed;
        txtFields[id][1] = txtGreen;
        txtFields[id][2] = txtBlue;
        final JLabel txtColor = new JLabel("");
        lblColors[id] = txtColor;
        int red = Integer.parseInt(txtRed.getText());
        int green = Integer.parseInt(txtGreen.getText());
        int blue = Integer.parseInt(txtBlue.getText());     
        txtColor.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseReleased(MouseEvent e)
            {
                cc.setTitle("Color Chooser - Color " + id);
                int red = Integer.parseInt(txtRed.getText());
                int green = Integer.parseInt(txtGreen.getText());
                int blue = Integer.parseInt(txtBlue.getText());                
                cc.setColor(new Color(red, green, blue));
                cc.setVisible(true);
                Color newColor = cc.getColor();
                txtRed.setText(((Integer)newColor.getRed()).toString());
                txtGreen.setText(((Integer)newColor.getGreen()).toString());
                txtBlue.setText(((Integer)newColor.getBlue()).toString());
                txtColor.setBackground(newColor);
                rgb[id][0] = newColor.getRed();
                rgb[id][1] = newColor.getGreen();
                rgb[id][2] = newColor.getBlue();
            }
        });
        txtColor.setBackground(new Color(red, green, blue));
        txtColor.setOpaque(true);
        gbc.gridx = 0;
        gbc.weightx = 0.2;
        gbc.weighty = 0.01;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        label.setBorder(new LineBorder(Color.black));
        scrollable.add(label, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        scrollable.add(txtRed, gbc);
        gbc.gridx++;
        scrollable.add(txtGreen, gbc);
        gbc.gridx++;
        scrollable.add(txtBlue, gbc);
        gbc.gridx++;
        scrollable.add(txtColor, gbc);
    }
    
    public void cmdHueAction()
    {
        final float[]hsb = Color.RGBtoHSB(rgb[0][0], rgb[0][1], rgb[0][2], null);
        //JOptionPane.showMessageDialog(null, "Hue: " + hue);
        HueAdjuster adjuster = new HueAdjuster((int)(hsb[0] * 360 + 0.5), (int)(hsb[1] * 100 + 0.5), (int)(hsb[2] * 100 + 0.5), this);
        adjuster.setLocationRelativeTo(this);
        adjuster.setVisible(true);
    }
    
    public void cmdSaturationAction()
    {
        final float[]hsb = Color.RGBtoHSB(rgb[0][0], rgb[0][1], rgb[0][2], null);
        //JOptionPane.showMessageDialog(null, "Hue: " + hue);
        SaturationAdjuster adjuster = new SaturationAdjuster((int)(hsb[0] * 360 + 0.5), (int)(hsb[1] * 100 + 0.5), (int)(hsb[2] * 100 + 0.5), this);
        adjuster.setLocationRelativeTo(this);
        adjuster.setVisible(true);
    }
    
    public void cmdBalanceAction()
    {
        final float[]hsb = Color.RGBtoHSB(rgb[0][0], rgb[0][1], rgb[0][2], null);
        //JOptionPane.showMessageDialog(null, "Hue: " + hue);
        BalanceAdjuster adjuster = new BalanceAdjuster((int)(hsb[0] * 360 + 0.5), (int)(hsb[1] * 100 + 0.5), (int)(hsb[2] * 100 + 0.5), this);
        adjuster.setLocationRelativeTo(this);
        adjuster.setVisible(true);
    }
    
    public void cmdMonotoneAction() {
        cc.setTitle("Color Chooser - Monotone");
        int red = rgb[0][0];
        int green = rgb[0][1];
        int blue = rgb[0][2];
        cc.setColor(new Color(red, green, blue));
        cc.setVisible(true);
        Color newColor = cc.getColor();
        keepGoingMonotone(true, newColor.getRGB());
    }
    
    public void keepGoingHue(boolean save, int newHue)
    {
        final float[]hsb = Color.RGBtoHSB(rgb[0][0], rgb[0][1], rgb[0][2], null);
        final int hue = (int)(hsb[0] * 360 + 0.5);
        if (save)
        {
            System.out.println("Old hue: " + hue + ", new hue: " + newHue);
            int diff = newHue - hue;
            Set<Integer>applyHue = new HashSet<Integer>();
            applyHue.addAll(Arrays.asList(rhyes));
            applyHue.addAll(Arrays.asList(mainColors));
            applyHue.addAll(Arrays.asList(otherColors));
            applyHue.addAll(Arrays.asList(allOthers));
            
            for (int i = 0; i < 70; i++) {
                System.arraycopy(rgb[i], 0, backup[i], 0, 3);
            }


            for (int i =0; i < rgb.length; i++)
            {
                if (applyHue.contains(i))
                {
                    float[] oldHSB = Color.RGBtoHSB(rgb[i][0], rgb[i][1], rgb[i][2], null);
                    int oldHue = (int)(hsb[0] * 360 + 0.5);
                    oldHue += diff;
                    float newFloat = (oldHue - 0.5F)/360.0F;
                    if (newFloat < 0)
                        newFloat = 0.0f;
                    else if (newFloat > 1)
                        newFloat = 1.0f;
                    oldHSB[0] = newFloat;
                    int newColor = Color.HSBtoRGB(oldHSB[0], oldHSB[1], oldHSB[2]);
                    Color c = new Color(newColor);
                    rgb[i][0] = c.getRed();
                    rgb[i][1] = c.getGreen();
                    rgb[i][2] = c.getBlue();
                    
                    txtFields[i][0].setText(((Integer)c.getRed()).toString());
                    txtFields[i][1].setText(((Integer)c.getGreen()).toString());
                    txtFields[i][2].setText(((Integer)c.getBlue()).toString());
                    lblColors[i].setBackground(c);
                }
            }
            cmdUndo.setEnabled(true);
        }
        else
        {
            System.out.println("Keeping old hue");
        }
    }
    
    
    
    public void keepGoingSaturation(boolean save, int newSaturation)
    {
        final float[]hsb = Color.RGBtoHSB(rgb[0][0], rgb[0][1], rgb[0][2], null);
        final int saturation = (int)(hsb[1] * 100 + 0.5);
        if (save)
        {
            System.out.println("Old hue: " + saturation + ", new hue: " + newSaturation);
            int diff = newSaturation - saturation;
            Set<Integer>applyHue = new HashSet<Integer>();
            applyHue.addAll(Arrays.asList(rhyes));
            applyHue.addAll(Arrays.asList(mainColors));
            applyHue.addAll(Arrays.asList(otherColors));
            applyHue.addAll(Arrays.asList(allOthers));
            
            for (int i = 0; i < 70; i++) {
                System.arraycopy(rgb[i], 0, backup[i], 0, 3);
            }


            for (int i =0; i < rgb.length; i++)
            {
                if (applyHue.contains(i))
                {
                    float[] oldHSB = Color.RGBtoHSB(rgb[i][0], rgb[i][1], rgb[i][2], null);
                    int oldSaturation = (int)(hsb[1] * 100 + 0.5);
                    oldSaturation += diff;
                    float newFloat = (oldSaturation - 0.5F)/100.0F;
                    if (newFloat < 0)
                        newFloat = 0.0f;
                    else if (newFloat > 1)
                        newFloat = 1.0f;
                    oldHSB[1] = newFloat; //saturation is second in hsb
                    int newColor = Color.HSBtoRGB(oldHSB[0], oldHSB[1], oldHSB[2]);
                    Color c = new Color(newColor);
                    rgb[i][0] = c.getRed();
                    rgb[i][1] = c.getGreen();
                    rgb[i][2] = c.getBlue();
                    
                    txtFields[i][0].setText(((Integer)c.getRed()).toString());
                    txtFields[i][1].setText(((Integer)c.getGreen()).toString());
                    txtFields[i][2].setText(((Integer)c.getBlue()).toString());
                    lblColors[i].setBackground(c);
                }
            }
            cmdUndo.setEnabled(true);
        }
        else
        {
            System.out.println("Keeping old saturation");
        }
    }
    
    public void keepGoingBalance(boolean save, int newBalance)
    {
        final float[]hsb = Color.RGBtoHSB(rgb[0][0], rgb[0][1], rgb[0][2], null);
        final int balance = (int)(hsb[2] * 100 + 0.5);
        if (save)
        {
            System.out.println("Old balance: " + balance + ", new balance: " + newBalance);
            int diff = newBalance - balance;
            Set<Integer>applyBalance = new HashSet<Integer>();
            applyBalance.addAll(Arrays.asList(rhyes));
            applyBalance.addAll(Arrays.asList(mainColors));
            applyBalance.addAll(Arrays.asList(otherColors));
            applyBalance.addAll(Arrays.asList(allOthers));
            
            for (int i = 0; i < 70; i++) {
                System.arraycopy(rgb[i], 0, backup[i], 0, 3);
            }


            for (int i =0; i < rgb.length; i++)
            {
                if (applyBalance.contains(i))
                {
                    float[] oldHSB = Color.RGBtoHSB(rgb[i][0], rgb[i][1], rgb[i][2], null);
                    int oldBalance = (int)(hsb[2] * 100 + 0.5);
                    oldBalance += diff;
                    float newFloat = (oldBalance - 0.5F)/100.0F;
                    if (newFloat < 0)
                        newFloat = 0.0f;
                    else if (newFloat > 1)
                        newFloat = 1.0f;
                    oldHSB[2] = newFloat; //saturation is second in hsb
                    int newColor = Color.HSBtoRGB(oldHSB[0], oldHSB[1], oldHSB[2]);
                    Color c = new Color(newColor);
                    rgb[i][0] = c.getRed();
                    rgb[i][1] = c.getGreen();
                    rgb[i][2] = c.getBlue();
                    
                    txtFields[i][0].setText(((Integer)c.getRed()).toString());
                    txtFields[i][1].setText(((Integer)c.getGreen()).toString());
                    txtFields[i][2].setText(((Integer)c.getBlue()).toString());
                    lblColors[i].setBackground(c);
                }
            }
            cmdUndo.setEnabled(true);
        }
        else
        {
            System.out.println("Keeping old saturation");
        }
    }
    
    public void keepGoingMonotone(boolean save, int newMonotone) {
        if (save) {
            Set<Integer>applyHue = new HashSet<Integer>();
            applyHue.addAll(Arrays.asList(rhyes));
            applyHue.addAll(Arrays.asList(mainColors));
            applyHue.addAll(Arrays.asList(otherColors));
            applyHue.addAll(Arrays.asList(allOthers));
            
            for (int i = 0; i < 70; i++) {
                System.arraycopy(rgb[i], 0, backup[i], 0, 3);
            }

            for (int i = 0; i < rgb.length; i++) {
                if (applyHue.contains(i)) {
                    Color c = new Color(newMonotone);
                    rgb[i][0] = c.getRed();
                    rgb[i][1] = c.getGreen();
                    rgb[i][2] = c.getBlue();
                    
                    txtFields[i][0].setText(((Integer)c.getRed()).toString());
                    txtFields[i][1].setText(((Integer)c.getGreen()).toString());
                    txtFields[i][2].setText(((Integer)c.getBlue()).toString());
                    lblColors[i].setBackground(c);
                }
            }
            cmdUndo.setEnabled(true);
        }
        else {
            System.out.println("Keeping old colors");
        }
    }
}
