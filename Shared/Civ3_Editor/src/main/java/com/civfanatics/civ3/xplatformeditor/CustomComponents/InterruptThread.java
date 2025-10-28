/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.xplatformeditor.CustomComponents;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Andrew
 */
public class InterruptThread extends Thread{
    
    HttpURLConnection conn;
    Thread runner;
    boolean offlineHelp = false;
    boolean offlineStatus = false;
    
    public InterruptThread() {
    }
    
    public void run() {
        runner = new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL("http://quintillus.warpmail.net/civ3editor/civ3editor.html");
                    conn = (HttpURLConnection)url.openConnection();
                    conn.setInstanceFollowRedirects(false);
                    conn.setConnectTimeout(1000);
                    conn.setReadTimeout(1000);

                    //Fetch the response code.  If there is none, it will throw an IOException.
                    //Note that some other methods, such as conn.getHeaderFields(), will not throw
                    //an exception if we are offline, and thus should not be used.
                    int responseCode = conn.getResponseCode();
                    System.out.println("Response code: " + responseCode);
                }
                
                catch(MalformedURLException ex) {
                    //Couldn't load CFC
                    System.out.println("Online help not available");
                    offlineHelp = true;
                }
                catch(SocketTimeoutException ex) {
                    System.out.println("Online help not available");
                    offlineHelp = true;
                }
                catch(IOException ex) {
                    //URL not available
                    System.out.println("Online help not available");
                    offlineHelp = true;
                }
                System.out.println("Done checking for online status");
            }
        };
        runner.start();
        
        try {
            Thread.sleep(1000);
        }
        catch(InterruptedException ex) {
            
        }
        //Check if the thread is still alive.  It shouldn't be; if it is count
        //it as having timed out.
        boolean alive = runner.isAlive();
        if (alive) {
            offlineStatus = true;
        }
        else {
            //Use the value that the connection thread calculated.
            offlineStatus = offlineHelp;
        }
        runner.interrupt();
    }
    
    public boolean getOfflineStatus() {
        return offlineStatus;
    }
}
