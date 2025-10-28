
package com.civfanatics.civ3.xplatformeditor;

/**
 * Thrown if a write is attempted to a ready-only file.
 * @author Andrew
 */
public class ReadOnlyException extends Exception {
    public ReadOnlyException(String message) {
        super(message);
    }
}
