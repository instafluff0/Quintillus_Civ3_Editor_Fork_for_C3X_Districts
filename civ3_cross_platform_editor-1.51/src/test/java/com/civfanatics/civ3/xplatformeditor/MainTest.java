
package com.civfanatics.civ3.xplatformeditor;

import com.civfanatics.civ3.biqFile.IO;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import org.junit.After;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * Testing the world.
 * 
 * I'm not sure if this is going to work, or work well.
 * 
 * @author Andrew
 */
public class MainTest {
    
    @After
    public void tearDown() {
        Main.biqFile = new ArrayList<IO>();
        Main.biqIndex = -1;
    }
        
    @Test
    public void canOpenBIQWithAllCustomData() throws Exception {
        String[] args = new String[2];
        args[0] = "";
        args[1] ="src/test/java/com/civfanatics/civ3/xplatformeditor/Custom All.biq"; 
        
        runIntegrationTest(args);
    }
    
    @Test
    public void canOpenBIQWithOnlyCustomMap() throws Exception {
        String[] args = new String[2];
        args[0] = "";
        args[1] ="src/test/java/com/civfanatics/civ3/xplatformeditor/Custom Map Only.biq"; 
        
        runIntegrationTest(args);
    }
    
    @Test
    public void canOpenBIQWithOnlyCustomMapAndUnits() throws Exception {
        String[] args = new String[2];
        args[0] = "";
        args[1] = "src/test/java/com/civfanatics/civ3/xplatformeditor/Custom Map Only with Units.biq";
        
        runIntegrationTest(args);
    }

    private void runIntegrationTest(String[] args) throws InterruptedException, UnsupportedEncodingException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(outputStream, true, "UTF-8");
        System.setOut(ps);
        
        
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        PrintStream es = new PrintStream(errorStream, true, "UTF-8");
        System.setErr(es);
        
        Main.main(args);
        
        sleep(4000);
        
        String output = outputStream.toString();
        String error = errorStream.toString();
        if (output.contains("ERROR") || error.contains("Exception")) {
            fail("An error was thrown");
        }
    }
}
