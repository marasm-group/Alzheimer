package com.marasm.alzheimer;

import java.util.ArrayList;

/**
 * Created by vhq473 on 08.02.2016.
 */
public class Type
{
    public ArrayList<String> allocate(String name){return new ArrayList<>();}
    public ArrayList<String> deallocate(String name){return new ArrayList<>();}
    public ArrayList<String> gallocate(String name){return new ArrayList<>();}
    public ArrayList<String> gdeallocate(String name){return new ArrayList<>();}

    protected void exec(String cmd, ArrayList<String> result)
    {
        if(Alzheimer.LogCPUInstructions){System.out.println(cmd);}
        result.add(cmd + " ;");
    }
}
