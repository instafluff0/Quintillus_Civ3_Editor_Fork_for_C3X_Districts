
package com.civfanatics.civ3.savFile;

import com.civfanatics.civ3.biqFile.util.LittleEndianDataInputStream;
import java.io.IOException;

/**
 * World Details.
 * @author Andrew
 */
public class WRLD {
    private int dataLength;
    private int[] data;
    
    private short continentCount;
    
    private String secondHeader;
    private int dataLength2;
    private int numLandConts;
    private int height;
    private int civDistance;
    private int numCivs;
    private int percentWater;
    private int unknown4;
    private int width;
    //128 (124?) bytes unknown (Player Map Data)
    private int worldSeed;
    private int mapFlags;
    
    private String thirdHeader;
    private int dataLength3;
    private int climateSelected;
    private int climateActual;
    private int barbariansSelected;
    private int barbariansActual;
    private int landformSelected;
    private int landformActual;
    private int oceanCoverageSelected;
    private int oceanCoverageActual;
    private int temperatureSelected;
    private int temperatureActual;
    private int worldAgeSelected;
    private int worldAgeActual;
    private int worldSize;  //index into WSIZ
    
    public void readDataSection(LittleEndianDataInputStream in) throws IOException {
        if (false) {
            setDataLength(in.readInt());
            for (int i = 0; i < data.length; i++) {
                setData(i, in.readInt());
            }
        }
        else {
            byte[] inputFour = new byte[4];
            
            dataLength = in.readInt();
            continentCount = in.readShort();
            
            in.read(inputFour, 0, 4);
            secondHeader = new String(inputFour, "Windows-1252");
            assert("WRLD".equals(secondHeader));
            dataLength2 = in.readInt();
            numLandConts = in.readInt();
            height = in.readInt();
            civDistance = in.readInt();
            numCivs = in.readInt();
            percentWater = in.readInt();
            unknown4 = in.readInt();
            width = in.readInt();
            in.skip(128);
            worldSeed = in.readInt();
            mapFlags = in.readInt();
            
            in.read(inputFour, 0, 4);
            thirdHeader = new String(inputFour, "Windows-1252");
            assert("WRLD".equals(thirdHeader));
            dataLength3 = in.readInt();
            climateSelected = in.readInt();
            climateActual = in.readInt();
            barbariansSelected = in.readInt();
            barbariansActual = in.readInt();
            landformSelected = in.readInt();
            landformActual = in.readInt();
            oceanCoverageSelected = in.readInt();
            oceanCoverageActual = in.readInt();
            temperatureSelected = in.readInt();
            temperatureActual = in.readInt();
            worldAgeSelected = in.readInt();
            worldAgeActual = in.readInt();
            worldSize = in.readInt();
            
        }
    }
    
    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
        data = new int[dataLength/4];
    }
    
    public void setData(int index, int value) {
        data[index] = value;
    }
    
    public int getData(int index) {
        return data[index];
    }

    public int getDataLength() {
        return dataLength;
    }

    public int[] getData() {
        return data;
    }

    public short getContinentCount() {
        return continentCount;
    }

    public String getSecondHeader() {
        return secondHeader;
    }

    public int getDataLength2() {
        return dataLength2;
    }

    public int getNumLandConts() {
        return numLandConts;
    }

    public int getHeight() {
        return height;
    }

    public int getCivDistance() {
        return civDistance;
    }

    public int getNumCivs() {
        return numCivs;
    }

    public int getPercentWater() {
        return percentWater;
    }

    public int getUnknown4() {
        return unknown4;
    }

    public int getWidth() {
        return width;
    }

    public int getWorldSeed() {
        return worldSeed;
    }

    public int getMapFlags() {
        return mapFlags;
    }

    public String getThirdHeader() {
        return thirdHeader;
    }

    public int getDataLength3() {
        return dataLength3;
    }

    public int getClimateSelected() {
        return climateSelected;
    }

    public int getClimateActual() {
        return climateActual;
    }

    public int getBarbariansSelected() {
        return barbariansSelected;
    }

    public int getBarbariansActual() {
        return barbariansActual;
    }

    public int getLandformSelected() {
        return landformSelected;
    }

    public int getLandformActual() {
        return landformActual;
    }

    public int getOceanCoverageSelected() {
        return oceanCoverageSelected;
    }

    public int getOceanCoverageActual() {
        return oceanCoverageActual;
    }

    public int getTemperatureSelected() {
        return temperatureSelected;
    }

    public int getTemperatureActual() {
        return temperatureActual;
    }

    public int getWorldAgeSelected() {
        return worldAgeSelected;
    }

    public int getWorldAgeActual() {
        return worldAgeActual;
    }

    public int getWorldSize() {
        return worldSize;
    }
    
    
}
