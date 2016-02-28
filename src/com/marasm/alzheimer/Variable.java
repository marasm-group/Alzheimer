package com.marasm.alzheimer;

import com.marasm.alzheimer.Types.NumberType;

/**
 * Created by SR3u on 11.02.2016.
 */
public class Variable
{
    public Type type= new NumberType();
    public boolean isArray=false;
    public String name=new String();
    public String arraySize()
    {
        int idx = name.indexOf("[");
        if (idx== -1){return "0";}
        String ars="0";
        ars=name.substring(idx);
        idx = ars.lastIndexOf("]");
        if (idx== -1){
            System.out.println("No ']' found for '['");return "0";}
        ars=ars.substring(0,idx).trim();
        ars=ars.replace("[","");
        ars=ars.replace("]","");
        return ars;
    }
    public String nameWithoutIndex()
    {
        int idx = name.indexOf("[");
        if (idx== -1){return name;}
        return name.substring(0,idx);
    }
}
