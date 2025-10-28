/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.civfanatics.civ3.xplatformeditor.imageSupport;

/**
 *
 * @author Andrew
 */
public class UnsupportedVariantException extends Exception{
    public UnsupportedVariantException(String message)
    {
        super(message);
    }
    public UnsupportedVariantException(String message, Throwable t)
    {
        super(message, t);
    }

}
