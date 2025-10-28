/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webpageviewer;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *  use
        jep.scrollToReference() to scroll to a reference within the same page
 * HTML code stuff from Ch. 8 of Java Network Programming, 3rd Edition.
 * Read up to the HTML parsing part
 * @author Andrew
 */
public class BrowserWindow {

    /**
     * @param args the command line arguments
     */
    JTextField addressBar = new JTextField();
    JTabbedPane browserTabs = new JTabbedPane();
    JButton newTab = new JButton();
    ArrayList<BrowserTab>tabs = new ArrayList<BrowserTab>(2);
    BrowserWindow main;
    
    String defaultURL = "help/civ3editor.html";
    boolean showNavigationBar = true;
    boolean tabbedInterface = true;

    JFrame f;
    
    int currentTab = 0;

    public static void main(String[] args) {
        // TODO code application logic here
        new BrowserWindow();
    }
    
    public BrowserWindow()
    {
        this("Web Viewer", true, true);
    }
    
    public BrowserWindow(String title, boolean tabbedInterface, boolean showNavigationBar)
    {
        this.tabbedInterface = tabbedInterface;
        this.showNavigationBar = showNavigationBar;
        //JOptionPane.showMessageDialog(null, System.getProperty("user.dir"));
        defaultURL = "file:////" + System.getProperty("user.dir") + "/" + defaultURL;
        main = this;
        tabs.add(new BrowserTab(main));
        browserTabs.add("Tab 1", tabs.get(0));
        tabs.get(0).goToPage(defaultURL);
        f = new JFrame(title);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        //GUI
        f.getContentPane().setLayout(new GridBagLayout());
        Container cp = f.getContentPane();
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = g.gridy = 0;
        g.weightx = 0.9;
        g.weighty = 0.0;
        g.fill = GridBagConstraints.HORIZONTAL;
        addressBar.addKeyListener(new KeyListener()
        {
           public void keyReleased(KeyEvent e)
            {
                System.out.println(e.getKeyChar());
                if (e.getKeyCode() == 10)    //enter
               {
                    System.out.println(e.getKeyCode());
                   try{
                    URL whereTo = new URL(addressBar.getText());
                    System.out.println("Request to go to : " + whereTo.toString());
                    tabs.get(currentTab).goToPage(whereTo);
                   }
                   catch(MalformedURLException ex)
                   {
                       System.err.println(ex);
                    //jep.setText("Can't go to " + addressBar.getText());
                   }
                   catch(IOException ex)
                   {
                       System.err.println(ex);

                   }
                   catch(Exception ex)
                   {
                       System.err.println(ex);
                   }
               }
           }
           public void keyPressed(KeyEvent e)
            {

           }
           public void keyTyped(KeyEvent e)
            {

           }
        });
        if (showNavigationBar)
        {
            cp.add(addressBar, g);
            g.gridx+=1;
            g.weightx = 0.1;
            newTab.setText("New tab");
            newTab.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    tabs.add(new BrowserTab(main));
                    browserTabs.add("New Tab", tabs.get(tabs.size() - 1));
                    browserTabs.setSelectedIndex(tabs.size() - 1);
                    currentTab = tabs.size() - 1;
                }
            });
            cp.add(newTab, g);
        }
        g.gridy++;
        g.gridwidth = 2;
        g.gridx = 0;
        g.weighty = 1.0;
        g.fill = GridBagConstraints.BOTH;
        if (tabbedInterface)
            cp.add(browserTabs, g);
        else
            cp.add(tabs.get(0), g);
        
        f.setSize(800, 600);


        //more actions

        browserTabs.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
                currentTab = browserTabs.getSelectedIndex();
            }
        });
    }
    
    public void goHome()
    {
        tabs.get(0).goToPage(defaultURL);
    }
    
    public void goToUrl(String url)
    {
        tabs.get(0).goToPage(url);
    }
    
    public void setVisible(boolean value)
    {
        f.setVisible(value);
    }
    
    public void setLocationRelativeTo(Component c)
    {
        f.setLocationRelativeTo(c);
    }

}
