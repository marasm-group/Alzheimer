package com.marasm.alzheimer.statements;

import com.marasm.alzheimer.*;
import com.marasm.alzheimer.Compiler;

import java.util.ArrayList;

/**
 * Created by vhq473 on 08.02.2016.
 */
public class VarStatement extends Statement
{
    ArrayList<String> variables=new ArrayList<>();
    String type="";
    public VarStatement(ArrayList<Token> _tokens)
    {
        super();
        Token t= new Token();
        tokens=new ArrayList<>();
        for(int i=0;!(t.isType()||t.isKeyword());i++)
        {
            t=_tokens.get(0);
            tokens.add(t);
            _tokens.remove(0);
        }
        if(tokens.get(tokens.size()-1).isKeyword())
        {
            tokens.remove(tokens.size()-1);
        }
    }
    @Override
    public ArrayList<String> compile(Compiler compiler)throws Exception
    {
        type=tokens.get(tokens.size()-1).value;
        for(int i=0;i<tokens.size()-1;i++)
        {
            variables.add(tokens.get(i).value);
        }
        ArrayList<String> res=new ArrayList<>();
        Type T= Alzheimer.types.get(this.type);
        for (String v:variables)
        {
            if(compiler.globalScope){res.addAll(T.gallocate(v));}
            else{res.addAll(T.allocate(v));}
        }
        return res;
    }
}
