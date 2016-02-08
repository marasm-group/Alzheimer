package com.marasm.alzheimer;

import com.marasm.alzheimer.statements.SexprStatement;
import com.marasm.alzheimer.statements.VarStatement;

import java.util.ArrayList;

/**
 * Created by vhq473 on 08.02.2016.
 */
public class Compiler
{
    public boolean globalScope=true;
    public void compile(ArrayList<Token> tokens) throws Exception {
        while (!tokens.isEmpty())
        {
            Token t=pop(tokens);
            if(t.isKeyword())
            {
                switch (t.value)
                {
                    case "gvar":
                        globalScope=true;
                        new VarStatement(tokens).compile(this);
                        break;
                    case "var":
                        globalScope=false;
                        new VarStatement(tokens).compile(this);
                        break;
                    case "#:":
                        new SexprStatement(tokens).compile(this);
                        break;
                    default:
                        break;
                }
            }
            globalScope=false;
        }
    }
    private Token pop(ArrayList<Token> tokens)
    {
        Token t=tokens.get(0);
        tokens.remove(0);
        return t;
    }
}
