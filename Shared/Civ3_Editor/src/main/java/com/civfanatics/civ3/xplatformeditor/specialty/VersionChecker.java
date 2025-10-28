
package com.civfanatics.civ3.xplatformeditor.specialty;

import com.civfanatics.civ3.xplatformeditor.Main;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

/**
 *
 * @author Andrew
 */ 
public class VersionChecker extends Thread
{
    Logger logger = Logger.getLogger(this.getClass());
    
    public VersionChecker(Main main, String version)
    {
        this.main = main;
        this.version = version;
    }

    Main main;
    String version;
    public void run()
    {
        try {

            HttpURLConnection conn;
            URL url = new URL("http://quintillus.warpmail.net/civ3editor/latestVersion.txt");
            conn = (HttpURLConnection)url.openConnection();
            conn.setInstanceFollowRedirects(true);
            conn.setConnectTimeout(1000);
            conn.setReadTimeout(1000);

            //Fetch the response code.  If there is none, it will throw an IOException.
            //Note that some other methods, such as conn.getHeaderFields(), will not throw
            //an exception if we are offline, and thus should not be used.
            int responseCode = conn.getResponseCode();
            if (logger.isDebugEnabled()) {
                logger.debug("Response code: " + responseCode);
            }

            StringBuilder sb = new StringBuilder();

            int available = conn.getInputStream().available();
            while (available > 0) {
                byte[] b = new byte[available];
                conn.getInputStream().read(b);
                String inputString = new String(b, "Windows-1252");
                if (logger.isDebugEnabled()) {
                    logger.debug("version input string: " + inputString);
                }
                sb.append(inputString);
                available = conn.getInputStream().available();
                if (logger.isTraceEnabled()) {
                    logger.trace("available bytes: " + available);
                }
            }

            //Only process and potentially alert if the version isn't one that is suppressed
            if (!sb.toString().equals(main.settings.suppressedUpdateVersion)) {
                String[] majorMinorVersion = sb.toString().split("\\.");
                String[] currentMajorMinorVersion = version.toString().split("\\.");
                Integer[] majorMinorInt = new Integer[majorMinorVersion.length];
                Integer[] currentInt = new Integer[currentMajorMinorVersion.length];
                for (int i = 0; i < majorMinorVersion.length; i++) {
                    majorMinorInt[i] = Integer.parseInt(majorMinorVersion[i]);
                }
                for (int i = 0; i < currentMajorMinorVersion.length; i++) {
                    currentInt[i] = Integer.parseInt(currentMajorMinorVersion[i]);
                }
                if (majorMinorInt[0] > currentInt[0] || majorMinorInt[1] > currentInt[1]) {
                    logger.warn("New version available: " + sb.toString() + "; current is " + version);
                    String[] options = { "Remind me again later", "Don't remind me until the next version", "Stop checking for updates" };
                    int option = JOptionPane.showOptionDialog(null, "Version " + sb.toString() + " is now available; you are running " + version + ".", "New version available", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options , "Remind me again later");
                    if (option == JOptionPane.NO_OPTION) {
                        //skip this one
                        main.settings.suppressedUpdateVersion = sb.toString();

                    }
                    else if (option == JOptionPane.CANCEL_OPTION) {
                        //stop checking for updates
                        main.settings.checkForUpdates = false;
                    }
                }
            }                
        }
        catch(MalformedURLException | NumberFormatException ex) {
            System.out.println("Could not check version");
        }
        catch(SocketTimeoutException ex) {
            System.out.println("Could not check version");
        }
        catch(IOException ex) {
            //URL not available
            System.out.println("Could not check version");
        }
    }
}