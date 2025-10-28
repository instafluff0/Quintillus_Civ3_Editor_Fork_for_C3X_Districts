
package com.civfanatics.civ3.savFile;

import com.civfanatics.civ3.biqFile.util.LittleEndianDataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Represents an in-game city.
 * This is one of the more involved
 * constructs in the game.
 * @author Andrew
 */
public class SavCITY {
    
    Logger logger = Logger.getLogger(this.getClass());
    
    private SAV baselink;
    private byte[] inputFour = new byte[4];
    private String header;
    private int dataLength; //should be 0x88.  if not, skip? - per Dianthus
    private int ID; //cities are not stored ordered by ID
    private short x;    //coords of city name box?
    private short y;
    private byte owner;
    private byte[] unknownThreeBytes = new byte[3];
    private int maintenanceGPT;
    private int cityFlags;
    private int governorSettings;
    private long unknownLong;   //may or may not actually be a long
    private int totalFood;
    private int shieldsCollected;
    private int pollution;
    private int constructing;
    private int constructingType;   //0 = wealth, 1 = building, 2 = unit
    private int yearBuilt;  //vanilla only, otherwise in YEAR section
    private int unknown1;
    private int culturalInfluence;
    private int militaryPolice;
    private int luxConnectedCount;
    private int luxConnectedBits;
    private int unknown2;
    private int draftTurnsLeft;
    private byte[] unknown52Bytes = new byte[52];
    
    private String cityHeaderTwo;
    private int dataLength2;
    private byte unhappyNoReasonPercent;
    private byte unhappyCrowdedPercent;
    private byte unhappyWarWearinessPercent;
    private byte unhappyAggressionPercent;
    private byte unhappyPropagandaPercent;
    private byte unhappyDraftPercent;
    private byte unhappyOppressionPercent;
    private byte unhappyThisCityImprovementsPercent;
    private byte unhappyOtherCityImprovementsPercent;
    private byte[] sevenBytes = new byte[7];
    
    private String cityHeaderThree;
    private int dataLength3;
    private byte[] thirtySixBytes = new byte[36];
    
    private String cityHeaderFour;
    private int dataLength4;
    private int culturePerTurn;
    private int[] culturePerLEAD = new int[32];
    private long unknownLongInSection4;
    private int foodPerTurn;
    private int shieldsPerTurn;
    private int commercePerTurn;
    private byte[] twelveBytes = new byte[12];
    
    private String cityHeaderFive;
    private int dataLength5;
    private byte[] nameBuffer = new byte[24];
    private String name;
    private int queueSlotsUsed;
    private QueueConstructing[] queueConstructing = new QueueConstructing[9];
    private int foodPerTurnForPopulation;
    private int corruptShieldsPerTurn;
    private int corruptGoldPerTurn;
    private int excessFoodPerTurn;
    private int unwastedFoodPerTurn;
    private int uncorruptGoldPerTurn;
    private int luxGoldPerTurn;
    private int scienceGoldPerTurn;
    private int treasuryGoldPerTurn;
    private int entertainerCount;   //not PTW/Conquests
    private int scientistCount;   //not PTW/Conquests
    private int taxManCount;   //not PTW/Conquests
    
    private String popdHeader;
    private int popdDataLength;
    private int specialistCount;
    private int citizenCount;
    private List<POPD> citizens = new ArrayList<POPD>();
    
    private String binfHeader;
    private int binfLength;
    private int buildingCount;
    private List<BINF> buildingInfo = new ArrayList<BINF>();
    
    private String bitmHeader;
    private int dataLengthBitm;
    private byte[] usableBuildingBits = new byte[32];   //see note 1
    private int buildingCountBitm;
    private int buildingBytes;
    
    //If >= PTW
    private String dateHeader;
    private int dateDataLength;
    private byte[] yearTextBytes = new byte[64];
    private String yearText;
    private int baseUnit;   //0 = years, 1 = months, 2 = weeks
    private int month;
    private int week;
    private int year;
    private byte[] extraDATEBytes;
    //Endif
    
    private byte[] dummyCityData;
    
    public SavCITY(SAV baselink) {
        this.baselink = baselink;
    }
    
    private class QueueConstructing {
        private int constructing;
        private int constructingType;   //0 = wealth, 1 = building, 2 = unit
    }
    
    public void readDataSection(LittleEndianDataInputStream in) throws IOException, EndOfCITYException, HeaderException {
        in.mark(5);
        in.read(inputFour, 0, 4);
        header = new String(inputFour, "Windows-1252");
        if (!header.equals("CITY") && !header.equals("CTPG")) {
            in.reset();
            throw new EndOfCITYException();
        }
        
        this.dataLength = in.readInt();
        if (dataLength != 0x88) {
            readDummyCitySection(in);
            return;
        }
        
        this.ID = in.readInt();
        this.x = in.readShort();
        this.y = in.readShort();
        this.owner = in.readByte();
        in.read(unknownThreeBytes, 0, 3);
        this.maintenanceGPT = in.readInt();
        this.cityFlags = in.readInt();
        this.governorSettings = in.readInt();
        this.unknownLong = in.readLong();
        this.totalFood = in.readInt();
        this.shieldsCollected = in.readInt();
        this.pollution = in.readInt();
        this.constructing = in.readInt();
        this.constructingType = in.readInt();
        this.yearBuilt = in.readInt();
        this.unknown1 = in.readInt();
        this.culturalInfluence = in.readInt();
        this.militaryPolice = in.readInt();
        this.luxConnectedCount = in.readInt();
        this.luxConnectedBits = in.readInt();
        this.unknown2 = in.readInt();
        this.draftTurnsLeft = in.readInt();
        in.read(unknown52Bytes, 0, 52);
        
        in.read(inputFour, 0, 4);
        cityHeaderTwo = new String(inputFour, "Windows-1252");
        if (!"CITY".equals(cityHeaderTwo)) {
            String message = "Second header did not matched expected (CITY) for city at " + x + ", " + y + "; was " + cityHeaderTwo;
            logger.error(message);
            throw new HeaderException(message);
        }
        this.dataLength2 = in.readInt();
        this.unhappyNoReasonPercent = in.readByte();
        this.unhappyCrowdedPercent = in.readByte();
        this.unhappyWarWearinessPercent = in.readByte();
        this.unhappyAggressionPercent = in.readByte();
        this.unhappyPropagandaPercent = in.readByte();
        this.unhappyDraftPercent = in.readByte();
        this.unhappyOppressionPercent = in.readByte();
        this.unhappyThisCityImprovementsPercent = in.readByte();
        this.unhappyOtherCityImprovementsPercent = in.readByte();
        in.read(sevenBytes, 0, 7);
        
        in.read(inputFour, 0, 4);
        cityHeaderThree = new String(inputFour, "Windows-1252");
        if (!"CITY".equals(cityHeaderThree)) {
            String message = "Third header did not matched expected (CITY) for city at " + x + ", " + y + "; was " + cityHeaderThree;
            logger.error(message);
            throw new HeaderException(message);
        }
        this.dataLength3 = in.readInt();
        in.read(thirtySixBytes, 0, 36);
        
        in.read(inputFour, 0, 4);
        cityHeaderFour = new String(inputFour, "Windows-1252");
        if (!"CITY".equals(cityHeaderFour)) {
            String message = "Fourth header did not matched expected (CITY) for city at " + x + ", " + y + "; was " + cityHeaderFour;
            logger.error(message);
            throw new HeaderException(message);
        }
        this.dataLength4 = in.readInt();
        this.culturePerTurn = in.readInt();
        for (int c = 0; c < 32; c++) {
            this.culturePerLEAD[c] = in.readInt();
        }
        this.unknownLongInSection4 = in.readLong();
        this.foodPerTurn = in.readInt();
        this.shieldsPerTurn = in.readInt();
        this.commercePerTurn = in.readInt();
        in.read(twelveBytes, 0, 12);
        
        in.read(inputFour, 0, 4);
        cityHeaderFive = new String(inputFour, "Windows-1252");
        if (!"CITY".equals(cityHeaderFive)) {
            String message = "Fifth header did not matched expected (CITY) for city at " + x + ", " + y + "; was " + cityHeaderFive;
            logger.error(message);
            throw new HeaderException(message);
        }
        this.dataLength5 = in.readInt();
        in.read(nameBuffer, 0, 24);
        this.name = new String(nameBuffer, "Windows-1252");
        this.queueSlotsUsed = in.readInt();
        for (int q = 0; q < 9; q++) {
            this.queueConstructing[q] = new QueueConstructing();
            this.queueConstructing[q].constructing = in.readInt();
            this.queueConstructing[q].constructingType = in.readInt();
        }
        this.foodPerTurnForPopulation = in.readInt();
        this.corruptShieldsPerTurn = in.readInt();
        this.corruptGoldPerTurn = in.readInt();
        this.excessFoodPerTurn = in.readInt();
        this.unwastedFoodPerTurn = in.readInt();
        this.uncorruptGoldPerTurn = in.readInt();
        this.luxGoldPerTurn = in.readInt();
        this.scienceGoldPerTurn = in.readInt();
        this.treasuryGoldPerTurn = in.readInt();
        this.entertainerCount = in.readInt();
        this.scientistCount = in.readInt();
        this.taxManCount = in.readInt();
        
        in.read(inputFour, 0, 4);
        popdHeader = new String(inputFour, "Windows-1252");
        if (!"POPD".equals(popdHeader)) {
            String message = "POPD header did not matched expected; was " + popdHeader;
            logger.error(message);
            throw new HeaderException(message);
        }
        this.popdDataLength = in.readInt();
        this.specialistCount = in.readInt();
        this.citizenCount = in.readInt();
        for (int c = 0; c < citizenCount; c++) {
            POPD newPop = new POPD();
            newPop.readDataSection(in);
            citizens.add(newPop);
        }
        
        in.read(inputFour, 0, 4);
        binfHeader = new String(inputFour, "Windows-1252");
        if (!"BINF".equals(binfHeader)) {
            String message = "BINF header did not matched expected; was " + popdHeader;
            logger.error(message);
            throw new HeaderException(message);
        }
        this.binfLength = in.readInt();
        this.buildingCount = in.readInt();
        for (int b = 0; b < buildingCount; b++) {
            BINF newBinf = new BINF();
            newBinf.readDataSection(in);
            buildingInfo.add(newBinf);
        }
        
        in.read(inputFour, 0, 4);
        bitmHeader = new String(inputFour, "Windows-1252");
        if (!"BITM".equals(bitmHeader)) {
            String message = "BITM header did not matched expected; was " + popdHeader;
            logger.error(message);
            throw new HeaderException(message);
        }
        this.dataLengthBitm = in.readInt();
        in.read(usableBuildingBits, 0, 32);
        this.buildingCount = in.readInt();
        this.buildingBytes = in.readInt();
        
        if (this.baselink.majorVersion >= SavVERSION.SAV_PTW) {
            in.read(inputFour, 0, 4);
            dateHeader = new String(inputFour, "Windows-1252");
            if (!"DATE".equals(dateHeader)) {
                String message = "DATE header did not matched expected for " + this.name + "; was " + popdHeader;
                logger.error(message);
                throw new HeaderException(message);
            }
            this.dateDataLength = in.readInt();
            in.read(yearTextBytes, 0, 64);
            this.yearText = new String(yearTextBytes, "Windows-1252");
            this.baseUnit = in.readInt();
            this.month = in.readInt();
            this.week = in.readInt();
            this.year = in.readInt();
            
            if (dateDataLength > 0x50) {
                this.extraDATEBytes = new byte[dateDataLength - 0x50];
                in.read(extraDATEBytes, 0, dateDataLength - 0x50);
            }
        }
    }
    
    public void readDummyCitySection(LittleEndianDataInputStream in) throws IOException {
        dummyCityData = new byte[dataLength];
        in.read(dummyCityData, 0, dataLength);
    }
    
    public boolean isRealCity() {
        return dataLength == 0x88;
    }
}
