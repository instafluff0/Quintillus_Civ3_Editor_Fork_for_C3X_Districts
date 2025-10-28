
package com.civfanatics.civ3.biqFile;
import java.util.List;
import com.civfanatics.civ3.biqFile.util.LittleEndianDataInputStream;
import org.apache.log4j.Logger;

public class SLOCThread extends Thread{

    LittleEndianDataInputStream in;
    LittleEndianDataInputStream[]inputArray;
    List<SLOC>startingLocation;
    int numStartingLocations;
    int start;
    Integer cpus;
    Integer returnVal;
    Logger logger = Logger.getLogger(this.getClass());
    public SLOCThread(LittleEndianDataInputStream in, List<SLOC>startingLocation, int numStartingLocations, int start, Integer cpus)
    {
        this.in = in;
        this.startingLocation = startingLocation;
        this.numStartingLocations = numStartingLocations;
        this.start = start;
        this.cpus = cpus;
    }

    public SLOCThread(LittleEndianDataInputStream[]inputArray, List<SLOC>startingLocation, int numStartingLocations)
    {
        this.inputArray = inputArray;
        this.startingLocation = startingLocation;
        this.numStartingLocations = numStartingLocations;

    }

    public void run()
    {
        multiThreadInputSLOC();
    }
    public Integer getReturnValue()
    {
        return returnVal;
    }

    private void multiThreadInputSLOC()
    {
        //intentionally hiding fields - this would not be threadsafe if I
        //didn't, because the other thread could change the field before I
        //was finished storing my copy
        int bytesOfInput = 0;
        try{
            Integer integer;
            Short inputShort;
            byte inputByte;

            for (int i = start; i < numStartingLocations; i+=cpus)
            {
                integer = in.readInt();
                bytesOfInput += 4;
                //SLOCs always have the same length.  No need to set it explicitly
                //(and I'd like to get away from setting data lengths explicitly).
                //startingLocation.get(i).setDataLength(integer);
                integer = in.readInt();
                bytesOfInput += 4;
                startingLocation.get(i).setOwnerType(integer);
                integer = in.readInt();
                bytesOfInput += 4;
                startingLocation.get(i).setOwner(integer);
                integer = in.readInt();
                bytesOfInput += 4;
                startingLocation.get(i).setX(integer);
                integer = in.readInt();
                bytesOfInput += 4;
                startingLocation.get(i).setY(integer);
                if (i + cpus < numStartingLocations)   //this isn't the last startingLocation; move along
                {
                    in.skipBytes(IO.SLOC_LENGTH * (cpus - 1));
                }
                else
                {
                    //we're about to exit the loop
                    //make every thread end up at the end of the SLOC section (exactly)
                    //this synchronization will allow the reuse of LittleEndianDataInputStreams if
                    //we end up multithreading the whole thing
                    //to do this, we will skip by the difference between i and the last
                    //possible legal i (numStartingLocations - 1).
                    //ex: there are 8 startingLocations.  i=5 should be the last iteration for
                    //the thread that started at i=2; it jumps by (7-5=2) to get to the
                    //end of SLOC.  i=6 should jump by 2 (7-6=1).  i=7 should jump by
                    //(7-7=0).
                    int toJump = (numStartingLocations - 1)-i;
                    in.skipBytes(IO.SLOC_LENGTH * toJump);
                }
            }
        }
        catch(java.io.IOException e)
        {
            logger.error("IO Exception in SLOCThread",e);
            System.err.println(e + " in SLOCThread");
        }
        //System.out.println("Bytes input by this thread: " + bytesOfInput);
        returnVal = (Integer)bytesOfInput;
    }
}
