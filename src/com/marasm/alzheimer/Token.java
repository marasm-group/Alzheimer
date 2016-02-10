package com.marasm.alzheimer;


import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Stack;

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
    public boolean isString()
    {
        return value.length()>1 && value.startsWith("\"") && value.endsWith("\"");
    }
    public String pushString()
    {
        if(!isString()){return "";}
        String value=this.value.replaceFirst("\"","");
        StringBuilder b = new StringBuilder(value);
        b.replace(value.lastIndexOf("\""), value.lastIndexOf("\"") + 1, "" );
        value = b.toString();
        Stack<String> reverse=new Stack<>();
        String res="";
        res+="push 0 ; push "+this.value+" \n";
        for (int i=0;i<value.length();i++)
        {
            String strchr=Character.toString(value.charAt(i));
            if(strchr.equals("\\"))
            {
                if(i<value.length()-1)
                {
                    i++;
                    strchr+=Character.toString(value.charAt(i));
                }
            }
            reverse.push(strchr);
        }
        while (reverse.size()>0)
        {
            res+="push '"+reverse.pop()+"'\n";
        }
        return res;
    }
}
