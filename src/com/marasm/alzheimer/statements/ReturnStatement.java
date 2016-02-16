package com.marasm.alzheimer.statements;

import com.marasm.alzheimer.*;
import com.marasm.alzheimer.Compiler;

import java.util.ArrayList;

/**
 * Created by SR3u on 08.02.2016.
 */
public class ReturnStatement extends SexprStatement
{

    public ReturnStatement(ArrayList<Token> _tokens) {
        super(_tokens);
    }
    public ArrayList<String> compile(Compiler compiler) throws Exception
    {
        ArrayList<String> res=new ArrayList<String>();
        if(compiler.globalScope)
            {throw new CompilerException("return in global scope!",tokens.get(0).file,tokens.get(0).line);}
        if(compiler.returnType==null)
        {
            exec("ret ;",res);
            return res;
        }
        if(tokens.size()==1)
        {
            Variable v=Alzheimer.variables.get(tokens.get(0).value);
            if(v==null){
                throw new CompilerException("Varialbe or statement expected",tokens.get(0).file,tokens.get(0).line);}
            res.addAll(v.type.push(v.name));
            exec("ret ;",res);
            return res;
        }
        res=super.compile(compiler);
        exec("ret ;",res);
        return res;
    }
}
