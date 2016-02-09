package com.marasm.alzheimer;


import java.text.NumberFormat;
import java.text.ParsePosition;

/**
 * Created by vhq473 on 08.02.2016.
 */
public class Token
{
    public String value="";
    public long line=0;
    public String file="<UNKNOWN>";
    public boolean isNumber()
    {
        if(value.length()==0){return false;}
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(value, pos);
        return value.length() == pos.getIndex();
    }
    public boolean isType()
    {
        if(value.length()==0){return false;}
        return value.startsWith(":");
    }
    public String toString()
    {
        return "'"+value+"'";
    }
    public boolean isKeyword()
    {
        return Alzheimer.keywords.contains(value);
    }
}
