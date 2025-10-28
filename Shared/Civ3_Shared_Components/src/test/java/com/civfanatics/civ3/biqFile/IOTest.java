/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civfanatics.civ3.biqFile;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Andrew
 */
public class IOTest {
    
    /**
     * Verifies that when we open a known-good file, and then save it, the resulting
     * file is byte-for-byte identical.  This ensures we do not do anything improper
     * just in the opening/closing methods.
     * @throws FileNotFoundException
     * @throws IOException 
     */
    @Test
    public void testInputAndOutputIsBinaryEqualTo098() throws FileNotFoundException, IOException{
        File correctFile = new File("src/test/java/com/civfanatics/civ3/biqFile/Standard 098.biq");
        File currentFile = new File("src/test/java/com/civfanatics/civ3/biqFile/Standard Current.biq");
        currentFile.deleteOnExit();
        
        IO newBIQ = new IO();
        boolean successfulInput = newBIQ.inputBIQ(correctFile);
        assertTrue("Input unsuccessful", successfulInput);
        
        boolean successfulOutput = newBIQ.outputBIQ(currentFile);
        assertTrue("Unsuccessful output", successfulOutput);
        
        //Now open them both in binary and compare
        assertEquals("Lengths are not equal", correctFile.length(), currentFile.length());
        
        DataInputStream correct = new DataInputStream(new FileInputStream(correctFile));
        DataInputStream current = new DataInputStream(new FileInputStream(currentFile));
        
        byte[]correctBuffer = new byte[(int)correctFile.length()];
        byte[]currentBuffer = new byte[(int)currentFile.length()];
        
        correct.readFully(correctBuffer, 0, (int)correctFile.length());
        current.readFully(currentBuffer, 0, (int)currentFile.length());
        
        for (int i = 0; i < correctBuffer.length; i++) {
            assertEquals("Byte " + i + " does not match", correctBuffer[i], currentBuffer[i]);
        }
    }
}
