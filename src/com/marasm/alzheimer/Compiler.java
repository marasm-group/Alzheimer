package com.marasm.alzheimer;

import com.marasm.alzheimer.statements.*;

import java.util.ArrayList;

/**
 * Created by vhq473 on 08.02.2016.
 */
public class Compiler
{
    public boolean globalScope=true;
    public String returnType=null;
    public void compile(ArrayList<Token> tokens) throws Exception
    {
        ArrayList<String>cpuCode=new ArrayList<>();
        exec("#json\n" +
                "{\n" +
                "\"author\":\"SR3u\",\n" +
                "\"dependencies\":[],\n" +
                "\"compiler\":\"Alzheimer\"\n" +
                "}\n" +
                "#end",cpuCode);
        while (!tokens.isEmpty())
        {
            Token t=pop(tokens);
            if(t.isKeyword())
            {
                boolean gs;
                switch (t.value)
                {
                    case "gvar":
                        gs=globalScope;
                        globalScope=true;
                        cpuCode.addAll(new VarStatement(tokens).compile(this));
                        globalScope=gs;
                        break;
                    case "var":
                        gs = globalScope;
                        globalScope=false;
                        cpuCode.addAll(new VarStatement(tokens).compile(this));
                        globalScope=gs;
                        break;
                    case "#:":
                        cpuCode.addAll(new SexprStatement(tokens).compile(this));
                        break;
                    case "asm:":
                        cpuCode.addAll(new AsmStatement(tokens).compile(this));
                        break;
                    case "fun:":
                        cpuCode.addAll(new FunStatement(tokens).compile(this));
                        globalScope=false;
                        break;
                    case "end":
                        exec("ret",cpuCode);
                        globalScope=true;
                        break;
                    case "return":
                        cpuCode.addAll(new ReturnStatement(tokens).compile(this));
                        break;
                    case "if":
                        cpuCode.addAll(new IfStatement(tokens).compile(this));
                        break;
                    case "else":
                        cpuCode.addAll(IfStatement.Else(tokens,this));
                        break;
                    case "endif":
                        cpuCode.addAll(IfStatement.EndIf(tokens,this));
                    default:
                        break;
                }
            }
        }
        exec("halt 0 ; end of code generation",cpuCode);
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
