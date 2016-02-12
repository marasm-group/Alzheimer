package com.marasm.alzheimer.statements;

import com.marasm.alzheimer.*;
import com.marasm.alzheimer.Compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SR3u on 08.02.2016.
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
            Variable var=new Variable();
            var.type=T;
            var.isArray=isArray(v);
            var.name=v;
            if(compiler.globalScope){
                res.addAll(T.gallocate(v));
                Alzheimer.globalVariables.put(var.nameWithoutIndex(),var);
            }
            else{res.addAll(T.allocate(v));}
            Alzheimer.variables.put(var.nameWithoutIndex(),var);
        }
        return res;
    }

    public static boolean isArray(String v)
    {
        return (v.contains("[")&&v.contains("]"));
    }
}
