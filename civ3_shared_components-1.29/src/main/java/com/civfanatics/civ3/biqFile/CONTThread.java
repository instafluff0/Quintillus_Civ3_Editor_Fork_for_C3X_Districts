
package com.civfanatics.civ3.biqFile;
import java.util.List;
import com.civfanatics.civ3.biqFile.util.LittleEndianDataInputStream;
public class CONTThread extends Thread{

    LittleEndianDataInputStream in;
    LittleEndianDataInputStream[]inputArray;
    List<CONT>cont;
    int numConts;
    int start;
    Integer cpus;
    Integer returnVal;
    public CONTThread(LittleEndianDataInputStream in, List<CONT>cont, int numConts, int start, Integer cpus)
    {
        this.in = in;
        this.cont = cont;
        this.numConts = numConts;
        this.start = start;
        this.cpus = cpus;
    }

    public CONTThread(LittleEndianDataInputStream[]inputArray, List<CONT>cont, int numConts)
    {
        this.inputArray = inputArray;
        this.cont = cont;
        this.numConts = numConts;

    }

    public void run()
    {
        multiThreadInputCONT();
    }
    public Integer getReturnValue()
    {
        return returnVal;
    }

    private void multiThreadInputCONT()
    {
        //intentionally hiding fields - this would not be threadsafe if I
        //didn't, because the other thread could change the field before I
        //was finished storing my copy
        int bytesOfInput = 0;
        try{
            Integer integer;
            Short inputShort;
            byte inputByte;

            for (int i = start; i < numConts; i+=cpus)
            {
                integer = in.readInt();
                bytesOfInput += 4;
                //Should always be length 8
                //cont.get(i).setDataLength(integer);
                integer = in.readInt();
                bytesOfInput += 4;
                cont.get(i).setContinentClass(integer);
                integer = in.readInt();
                bytesOfInput += 4;
                cont.get(i).setNumTiles(integer);
                if (i + cpus < numConts)   //this isn't the last cont; move along
                {
                    in.skipBytes(IO.CONT_LENGTH * (cpus - 1));
                }
                else
                {
                    //we're about to exit the loop
                    //make every thread end up at the end of the CONT section (exactly)
                    //this synchronization will allow the reuse of LittleEndianDataInputStreams if
                    //we end up multithreading the whole thing
                    //to do this, we will skip by the difference between i and the last
                    //possible legal i (numConts - 1).
                    //ex: there are 8 conts.  i=5 should be the last iteration for
                    //the thread that started at i=2; it jumps by (7-5=2) to get to the
                    //end of CONT.  i=6 should jump by 2 (7-6=1).  i=7 should jump by
                    //(7-7=0).
                    int toJump = (numConts - 1)-i;
                    in.skipBytes(IO.CONT_LENGTH * toJump);
                }
            }
        }
        catch(java.io.IOException e)
        {
            System.err.println(e + " in CONTThread");
        }
        //System.out.println("Bytes input by this thread: " + bytesOfInput);
        returnVal = (Integer)bytesOfInput;
    }
}
