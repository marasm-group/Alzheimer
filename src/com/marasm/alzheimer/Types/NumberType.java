package com.marasm.alzheimer.Types;

import com.marasm.alzheimer.Type;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by vhq473 on 08.02.2016.
 */
public class NumberType extends Type
{
    public ArrayList<String> allocate(String name)
    {
        ArrayList<String> res=new ArrayList<>();
        exec("var "+name+" ;",res);
        return res;
    }
    public ArrayList<String> deallocate(String name)
    {
        ArrayList<String> res=new ArrayList<>();
        exec("delv "+name+" ;",res);
        return res;
    }
    public ArrayList<String> gallocate(String name)
    {
        ArrayList<String> res=new ArrayList<>();
        exec("gvar "+name+" ;",res);
        return res;
    }
    public ArrayList<String> gdeallocate(String name)
    {
        ArrayList<String> res=new ArrayList<>();
        exec("delg "+name+" ;",res);
        return res;
    }
}

