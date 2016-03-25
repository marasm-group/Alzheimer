package com.marasm.alzheimer.statements;

import com.marasm.alzheimer.*;
import com.marasm.alzheimer.Compiler;
import com.marasm.alzheimer.Types.NumberType;

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
        if(!tokens.get(tokens.size()-1).isType())
        {
            Token tk=new Token();
            tk.value=":number";
            tk.file=tokens.get(0).file;
            tk.line=tokens.get(0).line;
            tokens.add(tk);
        }
        else
        {
            if(_tokens.size()>0) {
                if(_tokens.get(0).value.equals(";")){_tokens.remove(0);}
            }
        }
    }
    @Override
    public ArrayList<String> compile(Compiler compiler)throws Exception
    {
        Token typeToken=tokens.get(tokens.size()-1);
        type = typeToken.value;

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
            if(T==null){throw new CompilerException("Unknown type "+this.type+"!",typeToken.file,typeToken.line);}
            var.isArray=isArray(v);
            var.name=v;
            if(Alzheimer.variables.exists(v))
            {
                throw new CompilerException("Redeclaration of variable '"+v+"'",typeToken.file,typeToken.line);
            }
            if(compiler.globalScope){
                Alzheimer.variables.addGlobal(var.nameWithoutIndex(),var);
                if(var.isArray)
                {
                    allocateArray(res,T,var,true);
                }else{res.addAll(T.gallocate(v));}
            }
            else
            {
                if(var.isArray)
                {
                    allocateArray(res,T,var,false);
                }else{res.addAll(T.allocate(v));}
            }
            Alzheimer.variables.add(var.nameWithoutIndex(),var);
        }
        return res;
    }

    public static boolean isArray(String v)
    {
        return (v.contains("[")&&v.contains("]"));
    }
    void allocateArray(ArrayList<String>res,Type T,Variable var,boolean global) throws Exception
    {
        String varsizename=var.nameWithoutIndex()+".size";
        //res.addAll(varsize.type.allocate(varsize.nameWithoutIndex(),global));
        //Alzheimer.variables.add(varsize.name,varsize);
        String alz="var "+varsizename+" :number ;\n" +
                ""+varsizename+"=( "+ var.arraySize()+" );\n";
        res.addAll(Alzheimer.compile(alz));
        //exec("mov "+varsize.nameWithoutIndex()+" "+var.arraySize(),res);
        res.addAll(T.allocate(var.nameWithoutIndex()+"["+varsizename+"]",global));
    }
}
