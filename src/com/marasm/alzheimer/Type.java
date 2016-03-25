package com.marasm.alzheimer;

import java.util.ArrayList;

/**
 * Created by SR3u on 08.02.2016.
 */
public class Type
{
    public ArrayList<String> allocate(String name,boolean global)
    {
        if(global){return gallocate(name);}
        else{return allocate(name);}
    }
    public ArrayList<String> deallocate(String name,boolean global)
    {
        if(global){return gdeallocate(name);}
        else{return deallocate(name);}
    }
    public ArrayList<String> allocate(String name){return new ArrayList<>();}
    public ArrayList<String> deallocate(String name){return new ArrayList<>();}
    public ArrayList<String> gallocate(String name){return new ArrayList<>();}
    public ArrayList<String> gdeallocate(String name){return new ArrayList<>();}
    public ArrayList<String> push(String name){return new ArrayList<>();}
    public ArrayList<String> pop(String name){return new ArrayList<>();}

    protected void exec(String cmd, ArrayList<String> result)
    {
        if(Alzheimer.LogCPUInstructions){System.out.println(cmd);}
        result.add(cmd + " ;");
    }
    public String[]fields=null;
}
