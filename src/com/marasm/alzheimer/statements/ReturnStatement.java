package com.marasm.alzheimer.statements;

import com.marasm.alzheimer.*;
import com.marasm.alzheimer.Compiler;

import java.util.ArrayList;

/**
 * Created by vhq473 on 08.02.2016.
 */
public class ReturnStatement extends SexprStatement
{

    public ReturnStatement(ArrayList<Token> _tokens) {
        super(_tokens);
    }
    public ArrayList<String> compile(Compiler compiler) throws Exception
    {
        ArrayList<String> res=new ArrayList<String>();
        if(compiler.returnType==null)
        {
            exec("ret ;",res);
            return res;
        }
        if(tokens.size()==1)
        {
            exec("push "+tokens.get(0).value,res);
            exec("ret ;",res);
            return res;
        }
        res=super.compile(compiler);
        return res;
    }
}
