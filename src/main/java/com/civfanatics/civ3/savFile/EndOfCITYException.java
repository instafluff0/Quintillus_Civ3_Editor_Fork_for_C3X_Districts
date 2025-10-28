
package com.civfanatics.civ3.savFile;

/**
 * Indicates that the end of the CITY section has been reached.
 * Since we don't know how many Firaxis cities there are, this is used to
 * indicate that the end of the city section has been reached.  Thus it is a
 * "good" exception.
 * @author Andrew
 */
public class EndOfCITYException extends Throwable {

}
