
package com.civfanatics.civ3.biqFile.util;
/**
 * Houses micsellaneous utilities used by the BIQ sections
 * @author Andrew
 */
public class Utils {
    /**
     * Recalculates a bitfield from its constituent parts
     * @param bits - The bitfield, in order from most significant (first) to least significant (last) bit.
     * This corresponds with the traditional big-endian way of thinking.
     * @return - The sum of the bits as an int.
     */
    public static int recalculateBitfield(final Boolean[] bits)
    {
        int sum = 0;
        for (int i = 0; i < bits.length; i++)
        {
            if (bits[i])
                sum++;
            if (i == bits.length - 1)
                break;
            sum = sum << 1;
        }
        return sum;
    }
    
    public static int calcNumTrue(final Boolean[]bits)
    {
        int sum = 0;
        for (int i = 0; i < bits.length; i++)
        {
            if (bits[i])
                sum++;
        }
        return sum;
    }
}
