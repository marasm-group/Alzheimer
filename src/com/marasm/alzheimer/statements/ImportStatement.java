package com.marasm.alzheimer.statements;

import com.marasm.alzheimer.CompilerException;
import com.marasm.alzheimer.Statement;
import com.marasm.alzheimer.Token;

import java.util.ArrayList;

/**
 * Created by vhq473 on 12.02.2016.
 */
public class ImportStatement extends Statement
{
    public ImportStatement(ArrayList<Token> _tokens)
    {
        super();
        Token t=_tokens.remove(0);
        tokens=new ArrayList<>();
        for(int i=0;!t.value.equals(";");i++)
        {
            tokens.add(t);
            t=_tokens.remove(0);
        }
    }
    public ArrayList<String> compile(com.marasm.alzheimer.Compiler compiler) throws Exception
    {
        ArrayList<String>res=new ArrayList<>();
        for (Token t:tokens)
        {
            if(!t.isString()){throw new CompilerException("String expected!",t.file,t.line);}
            compiler.dependencies.add(t);
        }
        return res;
    }
}
