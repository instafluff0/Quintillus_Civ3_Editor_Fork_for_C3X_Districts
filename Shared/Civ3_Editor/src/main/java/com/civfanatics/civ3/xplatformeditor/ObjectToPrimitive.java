package com.civfanatics.civ3.xplatformeditor;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Quintillus
 */
public class ObjectToPrimitive {

    /**
     * Converts an Object Integer array to a primitive int array.
     *
     * @param array - The Integer array.
     * @return - The equivalent int array.
     */
    public static int[] intToInt(Integer[] array)
    {
        int[]toReturn = new int[array.length];
        //System.arraycopy throws a java.lang.ArrayStoreException if this is
        //attempted with it.  
        for (int i = 0; i < array.length; i++)
        {
            toReturn[i] = array[i];
        }
        return toReturn;
    }
}
