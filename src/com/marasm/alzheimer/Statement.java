package com.marasm.alzheimer;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by SR3u on 08.02.2016.
 */
public class Statement
{
    protected ArrayList<Token> tokens;
    public Statement(){}
    public Statement(ArrayList<Token> _tokens)
    {
        tokens=_tokens;
    }
    public ArrayList<String> compile(Compiler compiler) throws Exception {
        return new ArrayList<>();
    }
    protected static void exec(String cmd, Stack<String> result)
    {
        if(Alzheimer.LogCPUInstructions){System.out.println(cmd);}
        result.push(cmd+" ;");
    }
    protected static void exec(String cmd, ArrayList<String> result)
    {
        if(Alzheimer.LogCPUInstructions){System.out.println(cmd);}
        result.add(cmd + " ;");
    }

}
