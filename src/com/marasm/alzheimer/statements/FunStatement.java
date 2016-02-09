package com.marasm.alzheimer.statements;

import com.marasm.alzheimer.*;
import com.marasm.alzheimer.Types.NumberType;

import java.util.ArrayList;

/**
 * Created by vhq473 on 08.02.2016.
 */
public class FunStatement extends Statement
{
    public FunStatement(ArrayList<Token> _tokens)
    {
        super();
        Token t=_tokens.remove(0);
        tokens=new ArrayList<>();
        for(int i=0;!t.value.equals(")");i++)
        {
            tokens.add(t);
            t=_tokens.remove(0);
        }
        tokens.add(t);
        t=_tokens.get(0);
        if(t.isType())
        {
            tokens.add(t);
            _tokens.remove(0);
        }
    }
    ArrayList<String> allParams=new ArrayList<>();
    public ArrayList<String> compile(com.marasm.alzheimer.Compiler compiler) throws Exception
    {
        ArrayList<String>res=new ArrayList<>();
        exec("halt 0",res);
        exec("$"+tokens.remove(0).value,res);
        Token tmp=tokens.remove(0);
        if(!tmp.value.equals("(")){throw new CompilerException("( expected!",tmp.file,tmp.line);}
        String type=":number";
        ArrayList<String> params=new ArrayList<>();
        while (tokens.size()>0)
        {
            Token t=tokens.remove(0);
            if(t.isType())
            {
                type=t.value;
                commitParams(params,type,res);
            }
            else
            {
                if (t.value.equals(")"))
                {
                    commitParams(params,type,res);
                    break;
                }
                else
                {
                    params.add(t.value);
                }
            }

        }
        if(tokens.size()>0)
        {
            Token t=tokens.remove(0);
            if(t.isType())
            {
                compiler.returnType=t.value;
            }else {throw  new CompilerException("Unexpected token '"+t+"'!",t.file,t.line);}
        }
        else
        {
            compiler.returnType=null;
        }
        for (int i=allParams.size()-1;i>=0;i--)
        {
            exec("pop "+allParams.get(i),res);
        }
        return res;
    }
    void commitParams(ArrayList<String>params,String type,ArrayList<String>res)
    {
        Type T= Alzheimer.types.get(type);
        while(params.size()>0)
        {
            String p=params.remove(0);
            res.addAll(T.allocate(p));
            if(T.fields==null)
            {
                allParams.add(p);
            }
            else
            {
                for(String field:T.fields)
                {
                    allParams.add(p+"."+field);
                }
            }
        }
    }
}
