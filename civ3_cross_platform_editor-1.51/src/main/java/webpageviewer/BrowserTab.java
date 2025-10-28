/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webpageviewer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 *
 * @author Andrew
    */
public class BrowserTab extends JPanel{
    JEditorPane jep = new JEditorPane();
    JScrollPane scrollPane = new JScrollPane(jep);
    BrowserWindow main;
    public BrowserTab(BrowserWindow m)
    {
        this.main = m;
        jep.setEditable(false);
        /**
         * This listener will let use follow links.
         */
        jep.addHyperlinkListener(new HyperlinkListener(){
            public void hyperlinkUpdate(HyperlinkEvent evt)
            {
                if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
                {
                    try{
                        System.out.println("Going to " + evt.getURL().toString());
                        jep.setPage(evt.getURL());
                        main.addressBar.setText(evt.getURL().toString());
                    }
                    catch(IOException e)
                    {
                        System.out.println(e);
                    }
                }
            }
        });
        this.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = g.gridy = 0;
        g.weightx = g.weighty = 1;
        g.fill = GridBagConstraints.BOTH;
        this.add(scrollPane, g);
    }

    public void goToPage(URL page)
    {
        try{
            jep.setPage(page);
        }
        catch(IOException e)
        {
            jep.setContentType("text/html");
            jep.setText("Could not load " + page);
        }
    }

    public void goToPage(String string)
    {
        try{
            URL url = new URL(string);
            goToPage(url);
        }
        catch(MalformedURLException ex)
        {

        }
    }
}
