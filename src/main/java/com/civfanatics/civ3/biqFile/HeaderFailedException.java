
package com.civfanatics.civ3.biqFile;

public class HeaderFailedException extends Exception{
    public HeaderFailedException(String msg){
      super(msg);
    } 

    public HeaderFailedException(String msg, Throwable t){
      super(msg,t);
    }
}