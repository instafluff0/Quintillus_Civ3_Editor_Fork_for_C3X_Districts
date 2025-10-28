/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.civfanatics.civ3.xplatformeditor.imageSupport;

/**
 *
 * @author Andrew
 */
public class IllegalDataException extends Exception{
    public IllegalDataException(String message)
    {
        super(message);
    }
    public IllegalDataException(String message, Throwable t)
    {
        super(message, t);
    }
}
