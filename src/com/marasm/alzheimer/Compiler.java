package com.marasm.alzheimer;

import com.marasm.alzheimer.statements.AsmStatement;
import com.marasm.alzheimer.statements.SexprStatement;
import com.marasm.alzheimer.statements.VarStatement;

import java.util.ArrayList;

/**
 * Created by vhq473 on 08.02.2016.
 */
public class Compiler
{
    public boolean globalScope=true;
    public void compile(ArrayList<Token> tokens) throws Exception
    {
        ArrayList<String>cpuCode=new ArrayList<>();
        while (!tokens.isEmpty())
        {
            Token t=pop(tokens);
            if(t.isKeyword())
            {
                switch (t.value)
                {
                    case "gvar":
                        globalScope=true;
                        cpuCode.addAll(new VarStatement(tokens).compile(this));
                        break;
                    case "var":
                        globalScope=false;
                        cpuCode.addAll(new VarStatement(tokens).compile(this));
                        break;
                    case "#:":
                        cpuCode.addAll(new SexprStatement(tokens).compile(this));
                        break;
                    case "asm:":
                        cpuCode.addAll(new AsmStatement(tokens).compile(this));
                        break;
                    default:
                        break;
                }
            }
            globalScope=false;
        }
        exec("halt 0 ; end of code generation",cpuCode);
        exec("#json\n" +
             "{\n" +
             "\"author\":\"SR3u\",\n" +
             "\"dependencies\":[],\n" +
             "\"compiler\":\"Alzheimer\"\n" +
             "}\n" +
             "#end",cpuCode);
    }
    private void exec(String cmd,ArrayList<String>cpuCode)
    {
        if(Alzheimer.LogCPUInstructions){System.out.println(cmd);}
        cpuCode.add(cmd);
    }
    private Token pop(ArrayList<Token> tokens)
    {
        Token t=tokens.get(0);
        tokens.remove(0);
        return t;
    }
}
