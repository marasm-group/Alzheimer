package com.marasm.alzheimer;

import com.marasm.alzheimer.Types.NumberType;

import java.math.BigDecimal;

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
        String[] sizes=arraySizeArr(name);
        return sizes[0];
    }
    public static String[] arraySizeArr(String name)
    {
        int idx = name.indexOf("[");
        if (idx== -1){return new String[]{"0"};}
        String ars="0";
        ars=name.substring(idx+1);
        idx = ars.lastIndexOf("]");
        if (idx== -1){
            System.out.println("No ']' found for '['");return new String[]{"0"};}
        ars=ars.substring(0,idx).trim();
        return ars.split("\\]\\[");
    }
    public String arrayIndex()
    {
        String arraySize=arraySize();
        BigDecimal res=new BigDecimal(arraySize);
        res=res.subtract(BigDecimal.ONE);
        return ""+res;
    }
    public String nameWithoutIndex()
    {
        int idx = name.indexOf("[");
        if (idx== -1){return name;}
        return name.substring(0,idx);
    }
    public String sizeVarName(){return nameWithoutIndex()+".size";}
    public String accessVarName(){return nameWithoutIndex()+".access";}
    public String nameBeforeFirstDot()
    {
        return "";
    }
}
