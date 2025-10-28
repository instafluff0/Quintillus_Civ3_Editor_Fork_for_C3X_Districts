
package com.civfanatics.civ3.xplatformeditor.specialty;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CSVWriter extends DataOutputStream{

    public CSVWriter(OutputStream out) {
        super(out);
    }

    boolean atBeginningOfLine = true;

    public void write(String string)
    {
        try{
            if (!atBeginningOfLine)
                writeBytes(",");
            writeBytes(string);
            atBeginningOfLine = false;
        }
        catch(IOException e)
        {
            System.err.println(e);
        }
    }

    public void writeInteger(Integer number)
    {
        write(Integer.toString(number));
    }

    public void writeShort(Short number)
    {
        write(Short.toString(number));
    }

    public void writeByte(Byte number)
    {
        write(Byte.toString(number));
    }

    public void writeLong(Long number)
    {
        write(Long.toString(number));
    }

    public void writeBool(Boolean number)
    {
        write(Boolean.toString(number));
    }

    public void write(Object o)
    {
        if (o instanceof String)
            write((String)o);
        else if(o instanceof Integer)
            write((Integer)o);
        else if(o instanceof Short)
            write((Short)o);
        else if(o instanceof Byte)
            write((Byte)o);
        else if(o instanceof Boolean)
            write((Boolean)o);
        else
            throw new UnsupportedOperationException("Cannot write that type");
    }

    /**
     * Ends a record, that is, a row, in the CSV file.  This method uses \r\n as
     * the newline.
     */
    public void endRecord()
    {
        try{
            writeBytes("\r\n");
            atBeginningOfLine = true;
        }
        catch(IOException e)
        {
            System.err.println(e);
        }
    }

}
