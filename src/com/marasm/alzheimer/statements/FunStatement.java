package com.marasm.alzheimer.statements;

import com.marasm.alzheimer.*;
import com.marasm.alzheimer.Types.NumberType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

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
    Stack<Variable> allParams=new Stack<>();
    public ArrayList<String> compile(com.marasm.alzheimer.Compiler compiler) throws Exception
    {
        Alzheimer.variablesStack.push(Alzheimer.variables);
        Alzheimer.variables=new HashMap<>();
        Alzheimer.variables.putAll(Alzheimer.globalVariables);
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
        while(allParams.size()>0)
        {
            Variable v=allParams.pop();
            if(v.isArray)
            {
                for(int i=v.arraySize()-1;i>=0;i--)
                {
                    res.addAll(v.type.pop(v.nameWithoutIndex()+"["+i+"]"));
                }
            }
            else{res.addAll(v.type.pop(v.name));}
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
            Variable var=new Variable();
            var.type=T;
            var.isArray=VarStatement.isArray(p);
            var.name=p;
            Alzheimer.variables.put(var.nameWithoutIndex(),var);
            allParams.push(var);
        }
    }
}
