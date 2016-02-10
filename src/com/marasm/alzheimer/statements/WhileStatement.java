package com.marasm.alzheimer.statements;

import com.marasm.alzheimer.Compiler;
import com.marasm.alzheimer.CompilerException;
import com.marasm.alzheimer.Token;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by vhq473 on 09.02.2016.
 */
public class WhileStatement extends SexprStatement
{
    static String varNamePrefix="__ALZ_WHILE";
    static String endTagPrefix ="@__ALZ_WEND";
    static String wTagPrefix ="@__ALZ_WHILE";
    static long whileTagsCount =0;
    static Stack<String>endWhiles=new Stack<>();
    static Stack<String>whiles=new Stack<>();
    static Stack<String>variables=new Stack<>();

    public WhileStatement(ArrayList<Token> _tokens)
    {
        this(_tokens,":");
    }
    public WhileStatement(ArrayList<Token> _tokens,String last)
    {
        super();
        Token t=_tokens.remove(0);
        tokens=new ArrayList<>();
        for(int i=0;!t.value.equals(last);i++)
        {
            tokens.add(t);
            if(_tokens.size()<=0){break;}
            t=_tokens.remove(0);
        }
        tokens.add(t);
    }
    public ArrayList<String> compile(Compiler compiler) throws Exception
    {
        if(!tokens.get(tokens.size()-1).value.equals(":"))
        {
            Token tmp=tokens.get(tokens.size()-1);
            throw new CompilerException(": expected",tmp.file,tmp.line);
        }
        tokens.remove(tokens.size()-1);
        ArrayList<String> res=new  ArrayList<String>();
        String varName=varNamePrefix+ whileTagsCount;
        exec("var "+varName+" ;"+"while "+statementString()+" :",res);
        variables.push(varName);
        String wTag=wTagPrefix+ whileTagsCount;
        exec(wTag,res);
        res.addAll(super.compile(compiler));
        exec("pop "+varName+" ;",res);
        String newEndWhileTag= endTagPrefix + whileTagsCount;
        exec("jz "+varName+" "+newEndWhileTag,res);
        whiles.push(wTag);
        endWhiles.push(newEndWhileTag);
        whileTagsCount++;
        return res;
    }

    public static ArrayList<String> end(ArrayList<Token> _tokens, Compiler compiler) throws Exception
    {
        if(endWhiles.size()<=0)
            {throw  new CompilerException("endwhile without while",_tokens.get(0).file,_tokens.get(0).line);}
        ArrayList<String> res=new  ArrayList<String>();
        exec("jmp "+whiles.pop(),res);
        exec(endWhiles.pop(),res);
        exec("delv "+variables.pop(),res);
        return res;
    }
    public static ArrayList<String> Break(ArrayList<Token> _tokens, Compiler compiler) throws Exception
    {
        if(endWhiles.size()<=0)
            {throw  new CompilerException("break without while",_tokens.get(0).file,_tokens.get(0).line);}
        ArrayList<String> res=new  ArrayList<String>();
        exec("jmp "+endWhiles.peek(),res);
        return res;
    }
    public static ArrayList<String> Continue(ArrayList<Token> _tokens, Compiler compiler) throws Exception
    {
        if(endWhiles.size()<=0)
        {throw  new CompilerException("continue without while",_tokens.get(0).file,_tokens.get(0).line);}
        ArrayList<String> res=new  ArrayList<String>();
        exec("jmp "+whiles.peek(),res);
        return res;
    }
}
