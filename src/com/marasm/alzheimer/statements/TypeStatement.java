package com.marasm.alzheimer.statements;

import com.marasm.alzheimer.*;
import com.marasm.alzheimer.Compiler;
import com.marasm.alzheimer.Types.CustomType;

import java.util.ArrayList;


/**
 * Created by vhq473 on 16.02.2016.
 */
public class TypeStatement extends Statement
{
    Token typeName;
    public TypeStatement(ArrayList<Token> _tokens)
    {
        super();
        Token t=_tokens.remove(0);
        tokens=new ArrayList<>();
        for(int i=0;!t.value.equals("end");i++)
        {
            tokens.add(t);
            t=_tokens.remove(0);
        }
    }
    public ArrayList<String> compile(Compiler compiler)throws Exception
    {
        ArrayList<String> res=new ArrayList<>();
        typeName=tokens.get(0);
        if(!typeName.isType())
        {
            throw new CompilerException("Expected type name",typeName.file,typeName.line);
        }
        CustomType customType=new CustomType();
        tokens.remove(0);
        Token type=defaultType();
        while(tokens.size()>0)
        {
            Token t=tokens.get(0);
            tokens.remove(0);
            if(t.isType())
            {
                type=t;
                continue;
            }
            if(t.value.equals("var"))
            {
                type=defaultType();
                continue;
            }
            CustomType.Field f=new CustomType.Field(t.value,type.value);
            customType.fields.add(f);
        }
        Alzheimer.types.put(typeName.value,customType);
        return res;
    }

    private Token defaultType()
    {
        Token type=new Token();
        type.value=":number";
        type.file=typeName.file;
        type.line=typeName.line;
        return type;
    }
}
