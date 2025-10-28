/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package webbrowserfx;

import com.civfanatics.civ3.xplatformeditor.UnitDownloadWindowController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.control.Tab;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.swing.JOptionPane;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MouseEvent;

/**
 * Represents tab within the browser.
 * In addition to the graphical component, stores information such as tab history.
 * 
 * @author Andrew
 */
public class BrowserTab extends Tab {
    
    private volatile boolean isActive = false;
    private String address = "";
    private String title = "";
    private boolean focusOnInput = false;
    
    WebBrowserFX hostWindow; //eventually will change to window
        
    public BrowserTab(WebBrowserFX host, String startUrl)
    {
        hostWindow = host;
        
        WebView webKit = new WebView();
        final WebEngine engine = webKit.getEngine();

        engine.getLoadWorker().stateProperty().addListener(
                new ChangeListener<Worker.State>() {
                    public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                        if (newState == Worker.State.SUCCEEDED) {
                            setAddress(engine.getLocation());
                            setTitle(engine.getTitle());

                            //experiment with background tabs
                            final NodeList nl = engine.getDocument().getElementsByTagName("a");
                            for (int i = 0; i < nl.getLength(); i++) {
                                final EventTarget et = (EventTarget) nl.item(i);
                                String href = ((com.sun.webkit.dom.HTMLAnchorElementImpl)et).getHref();
                                if (href == null || !href.contains("resources")) {
                                    //no need to add extraneous events
                                    continue;
                                }
                                
                                et.addEventListener("click", new EventListener() {
                                    @Override
                                    public void handleEvent(org.w3c.dom.events.Event evt) {
                                        MouseEvent me = (MouseEvent) evt;
                                        System.out.println("Shift? " + me.getShiftKey());
                                        System.out.println("A HREF event");
                                        System.out.println("Type: " + evt.getType());
                                        
                                        if (me.getCtrlKey())
                                        {
                                            evt.preventDefault();
                                            System.out.println("Background tab");
                                            hostWindow.addTab(evt.getTarget().toString(), false);
                                            System.out.println("Target: " + evt.getTarget().toString());
                                            System.out.println("Engine location: " + engine.getLocation());
                                        }
                                        else if (me.getShiftKey())
                                        {
                                            evt.preventDefault();
                                            hostWindow.addTab(evt.getTarget().toString(), true);
                                        }
                                        else {
                                            System.out.println("Same tab");
                                            String target = evt.getCurrentTarget().toString();
                                            //if (target.endsWith("&act=down")) {
                                            if (target.contains("/download?")) {
                                                evt.preventDefault();
                                                System.out.println("Download!");
                                                controller.downloadUnit(target);
                                            }
                                        }
                                             
                                        
                                    }

                                }, true);
                            }
                            final NodeList n2 = engine.getDocument().getElementsByTagName("input");
                            System.out.println(n2.getLength());
                            for (int i = 0; i < n2.getLength(); i++) {
                                System.out.println("Input " + i);
                                Node node = n2.item(i);
                                NamedNodeMap nnm = node.getAttributes();
                                for (int j = 0; j < nnm.getLength(); j++)
                                {
                                    Node attribute = nnm.item(j);
                                    System.out.println(attribute.getNodeName() + " = " + attribute.getNodeValue());
                                }
//                                System.out.println("Node " + i + ": " + node.getNodeValue());
//                                System.out.println("Node " + i + ": " + node.getNodeName());
//                                System.out.println("Node " + i + ": " + node.getNodeType());
//                                System.out.println("Node " + i + ": " + node.getTextContent());
                                System.out.println();
                                final EventTarget et = (EventTarget) n2.item(i);
                                et.addEventListener("click", new EventListener() {
                                    @Override
                                    public void handleEvent(org.w3c.dom.events.Event evt) {
                                        System.out.println("Clicked on an input");
                                        
                                    }

                                }, true);
                                
                                et.addEventListener("focus", new EventListener(){

                                    @Override
                                    public void handleEvent(Event evt) {
                                        System.out.println("Focus event");
                                        focusOnInput = true;
                                    }
                                    
                                }, true);
                                
                                et.addEventListener("blur", new EventListener(){

                                    @Override
                                    public void handleEvent(Event evt) {
                                        System.out.println("Lost focus event");
                                        focusOnInput = false;
                                    }
                                    
                                }, true);
                            }
                        }
                    }
                }
        );

        engine.load(startUrl);
        setContent(webKit);
        setText("Tab One");
    }
    
    private void setTitle(String title)
    {
        this.title = title;
        this.setText(title);
        if (isActive)
            hostWindow.setWindowTitle(title);
    }
    
    private void setAddress(String address)
    {
        this.address = address;
        if (isActive)
            hostWindow.setAddressBar(address);
    }
    
    boolean getFocusOnInput()
    {
        return focusOnInput;
    }
    
    private UnitDownloadWindowController controller = null;
    
    public void setController(UnitDownloadWindowController controller) {
        this.controller = controller;
    }
}
