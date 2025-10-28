
package com.civfanatics.civ3.savFile;

import com.civfanatics.civ3.biqFile.util.LittleEndianDataInputStream;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 * POP Details.  For citizens living
 * in a city.
 * @author Andrew
 */
public class POPD {

    Logger logger = Logger.getLogger(this.getClass());

    private byte[] inputFour = new byte[4];
    private String header;
    private int dataLength;
    private byte unknown;
    private byte tileWorked;    //see diagram
    private byte[] twoSixtyTwo = new byte[262];
    private String name;
    private int type;
    private int sex;
    private int birthDate;
    private int cityID;
    private int id;
    private int specialistType;
    private int nationality;
    private int affectedBy;
    private int affectedSince;  //converstion; year in Vanilla, turn # in PTW/Conquests
    
    
    public void readDataSection(LittleEndianDataInputStream in) throws IOException, HeaderException {
        in.read(inputFour, 0, 4);
        header = new String(inputFour, "Windows-1252");
        if (!"CTZN".equals(header)) {
            String message = "POPD CITZN header did not matched expected; was " + header;
            logger.error(message);
            throw new HeaderException(message);
        }
        this.dataLength = in.readInt();
        this.unknown = in.readByte();
        this.tileWorked = in.readByte();
        in.read(twoSixtyTwo, 0, 262);
        this.name = new String(twoSixtyTwo, "Windows-1252");
        this.type = in.readInt();
        this.sex = in.readInt();
        this.birthDate = in.readInt();
        this.cityID = in.readInt();
        this.id = in.readInt();
        this.specialistType = in.readInt();
        this.nationality = in.readInt();
        this.affectedBy = in.readInt();
        this.affectedSince = in.readInt();
    }
}

