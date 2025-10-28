
package com.civfanatics.civ3.biqFile.util;
import com.civfanatics.civ3.biqFile.IO;
import com.civfanatics.civ3.biqFile.TILE;

public class TILEArrayInitializer extends Thread{
    Object[]array;
    int start;
    IO baselink;
    public void TILEArrayInitializer()
    {

    }

    public void TILEArrayInitializer(com.civfanatics.civ3.biqFile.IO baselink)
    {
        this.array = array;
        this.start = start;
        this.baselink = baselink;
    }

    public void setArray(TILE[]array)
    {
        this.array = array;
    }

    public void setStart(int start)
    {
        this.start = start;
    }

    public void setIO(IO baselink)
    {
        this.baselink = baselink;
    }

    public void run()
    {
        for (int i = start; i < array.length; i+=2)
        {
            array[i] = new TILE(baselink);
        }
    }
}
