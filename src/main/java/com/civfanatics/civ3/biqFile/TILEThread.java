
package com.civfanatics.civ3.biqFile;
import java.util.List;
import com.civfanatics.civ3.biqFile.util.LittleEndianDataInputStream;
import org.apache.log4j.*;
public class TILEThread extends Thread{

    LittleEndianDataInputStream in;
    LittleEndianDataInputStream[]inputArray;
    List<TILE>tile;
    int numTiles;
    int start;
    Integer cpus;
    Integer returnVal;
    Logger logger = Logger.getLogger(this.getClass());
    IO baselink;
    public TILEThread(LittleEndianDataInputStream in, List<TILE>tile, int numTiles, int start, Integer cpus, IO baselink)
    {
        this.in = in;
        this.tile = tile;
        this.numTiles = numTiles;
        this.start = start;
        this.cpus = cpus;
        this.baselink = baselink;
    }

    public TILEThread(LittleEndianDataInputStream[]inputArray, List<TILE>tile, int numTiles)
    {
        this.inputArray = inputArray;
        this.tile = tile;
        this.numTiles = numTiles;
        this.baselink = baselink;

    }

    public void run()
    {
        multiThreadInputTILE();
    }
    public Integer getReturnValue()
    {
        return returnVal;
    }

    private void multiThreadInputTILE()
    {
        //intentionally hiding fields - this would not be threadsafe if I
        //didn't, because the other thread could change the field before I
        //was finished storing my copy
        int bytesOfInput = 0;
        try{
            Integer integer;
            Short inputShort;
            byte inputByte;

            for (int i = start; i < numTiles; i+=cpus)
            {
                tile.get(i).index = i;
                integer = in.readInt();
                bytesOfInput += 4;
                tile.get(i).setDataLength(integer);
                inputByte = in.readByte();
                bytesOfInput++;
                tile.get(i).setRiverConnectionInfo(inputByte);
                inputByte = in.readByte();
                bytesOfInput++;
                tile.get(i).setBorder(inputByte);
                integer = in.readInt();
                bytesOfInput += 4;
                tile.get(i).setResource(integer);
                inputByte = in.readByte();
                bytesOfInput++;
                tile.get(i).setImage(inputByte);
                inputByte = in.readByte();
                bytesOfInput++;
                if (inputByte == -1)
                    System.err.println("This should not happen");
                tile.get(i).setFile(inputByte);
                inputShort = in.readShort();
                bytesOfInput += 2;
                tile.get(i).setQuestionMark(inputShort);
                inputByte = in.readByte();
                bytesOfInput++;
                tile.get(i).setOverlays(inputByte);
                inputByte = in.readByte();
                bytesOfInput++;
                tile.get(i).setBaseRealTerrain(inputByte);
                inputByte = in.readByte();
                bytesOfInput++;
                tile.get(i).setBonuses(inputByte);
                inputByte = in.readByte();
                bytesOfInput++;
                tile.get(i).setRiverCrossingData(inputByte);
                inputShort = in.readShort();
                bytesOfInput += 2;
                tile.get(i).setBarbarianTribe(inputShort);
                inputShort = in.readShort();
                bytesOfInput += 2;
                tile.get(i).setCity(inputShort);
                inputShort = in.readShort();
                bytesOfInput += 2;
                tile.get(i).setColony(inputShort);
                inputShort = in.readShort();
                bytesOfInput += 2;
                tile.get(i).setContinent(inputShort);
                //In early versions of vanilla, the TILE length was 22.  In
                //patched versions, it was 23.  Not sure exactly when it changed
                //but QuestionMark2 is the new byte.
                if (tile.get(i).getDataLength() >= 0x17) {
                    inputByte = in.readByte();
                    bytesOfInput++;
                    tile.get(i).setQuestionMark2(inputByte);
                    if (baselink.version.ordinal() >= civ3Version.PTW.ordinal()) {
                        inputShort = in.readShort();
                        bytesOfInput += 2;
                        tile.get(i).setVictoryPointLocation(inputShort);
                        integer = in.readInt();
                        bytesOfInput += 4;
                        tile.get(i).setRuin(integer);
                        if (baselink.version == civ3Version.CONQUESTS)
                        {
                            integer = in.readInt();
                            bytesOfInput += 4;
                            tile.get(i).setC3COverlays(integer);
                            inputByte = in.readByte();
                            bytesOfInput++;
                            tile.get(i).setQuestionMark3(inputByte);
                            inputByte = in.readByte();
                            bytesOfInput++;
                            tile.get(i).setC3CBaseRealTerrain(inputByte);
                            inputShort = in.readShort();
                            bytesOfInput += 2;
                            tile.get(i).setQuestionMark4(inputShort);
                            inputShort = in.readShort();
                            bytesOfInput += 2;
                            tile.get(i).setFogOfWar(inputShort);
                            integer = in.readInt();
                            bytesOfInput += 4;
                            tile.get(i).setC3CBonuses(integer);
                            inputShort = in.readShort();
                            bytesOfInput += 2;
                            tile.get(i).setQuestionMark5(inputShort);
                            tile.get(i).setUpNibbles();
                            if (tile.get(i).dataLength == 49) {
                                integer = in.readInt();
                                bytesOfInput += 4;
                                tile.get(i).setExtraInt(integer);
                            }
                        }
                    }
                }
                if (baselink.convertToConquests == 1) {
                    tile.get(i).setDataLength(0x2D);
                    tile.get(i).setQuestionMark2((byte)6);
                    tile.get(i).setC3CBaseRealTerrain(tile.get(i).getBaseRealTerrain());
                    tile.get(i).setC3COverlays(tile.get(i).getOverlays());
                    tile.get(i).setC3CBonuses(tile.get(i).getBonuses());
                    int baseTerrain = tile.get(i).getBaseTerrain();
                    if (baseTerrain >= TERR.MARSH) {
                        tile.get(i).setBaseTerrain(baseTerrain + 2);
                    }
                    int realTerrain = tile.get(i).getRealTerrain();
                    if (realTerrain >= TERR.MARSH) {
                        tile.get(i).setRealTerrain(realTerrain + 2);
                    }
                    if (baselink.version == civ3Version.VANILLA) {
                        //Ensure no barricades/craters
                        tile.get(i).setCrater(false);
                        tile.get(i).setBarricade(false);
                    }
                }
                tile.get(i).tileID = i;
                //if this isn't the last tile for this thread, skip ahead
                //the + 1 is key for the "unithreaded" input stream
                //Key: the * cpus.  Otherwise it only works with dual core
                //Thanks to Sun's VirtualBox for making testing triple-cores
                //feasible
                if (i + cpus < numTiles)   //this isn't the last tile; move along
                {
                    if (baselink.version == civ3Version.CONQUESTS)
                        in.skipBytes(IO.TILE_LENGTH * (cpus - 1));
                    else if (baselink.version == civ3Version.PTW)
                        in.skipBytes(IO.PTW_TILE_LENGTH * (cpus - 1));
                    else if (baselink.version == civ3Version.VANILLA) {
                        if (baselink.majorVersionNumber == 2) {
                            in.skipBytes(IO.VANILLA_2_TILE_LENGTH * (cpus - 1));
                        }
                        else {
                            in.skipBytes(IO.VANILLA_3_4_TILE_LENGTH * (cpus - 1));
                        }                        
                    }
                }
                else
                {
                    //we're about to exit the loop
                    //make every thread end up at the end of the TILE section (exactly)
                    //this synchronization will allow the reuse of LittleEndianDataInputStreams if
                    //we end up multithreading the whole thing
                    //to do this, we will skip by the difference between i and the last
                    //possible legal i (numTiles - 1).
                    //ex: there are 8 tiles.  i=5 should be the last iteration for
                    //the thread that started at i=2; it jumps by (7-5=2) to get to the
                    //end of TILE.  i=6 should jump by 2 (7-6=1).  i=7 should jump by
                    //(7-7=0).
                    int toJump = (numTiles - 1)-i;
                    if (baselink.version == civ3Version.CONQUESTS)
                        in.skipBytes(IO.TILE_LENGTH * toJump);
                    else if (baselink.version == civ3Version.PTW)
                        in.skipBytes(IO.PTW_TILE_LENGTH * toJump);
                    else if (baselink.version == civ3Version.VANILLA) {
                        if (baselink.majorVersionNumber == 2) {
                            in.skipBytes(IO.VANILLA_2_TILE_LENGTH * toJump);
                        }
                        else {  //verison 3 or 4
                            in.skipBytes(IO.VANILLA_3_4_TILE_LENGTH * toJump);
                        }
                    }
                }
            }
        }
        catch(java.io.IOException e)
        {
            logger.error("IOException", e);
            System.err.println(e + " in TILEThread");
        }
        //System.out.println("Bytes input by this thread: " + bytesOfInput);
        returnVal = (Integer)bytesOfInput;
    }
}
