package com.marasm.alzheimer.statements;

import com.marasm.alzheimer.Compiler;
import com.marasm.alzheimer.CompilerException;
import com.marasm.alzheimer.Statement;
import com.marasm.alzheimer.Token;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

/**
 * Created by vhq473 on 09.02.2016.
 */
public class IfStatement extends SexprStatement
{
    static long IfTagsCount=0;
    static long ElseTagsCount=0;
    static long EndTagsCount=0;

    static String nextIfTag=null;
    static String nextElseTag=null;
    static String nextEndTag=null;

    static String tagPrefix="@__ALZ_";
    static String varName="__ALZ_IFVAR";

    static Stack<String> openedElses=new Stack<>();
    static Stack<String> closedElses=new Stack<>();
    public IfStatement(ArrayList<Token> _tokens) {
        this(_tokens,":");
    }
    public IfStatement(ArrayList<Token> _tokens,String last)
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
        System.out.println(tokens);
    }
    public ArrayList<String> compile(Compiler compiler) throws Exception
    {
        if(!tokens.get(tokens.size()-1).value.equals(":"))
        {
            Token tmp=tokens.get(tokens.size()-1);
            throw new CompilerException(": expected",tmp.file,tmp.line);
        }
        tokens.remove(tokens.size()-1);
        ArrayList<String> res=new ArrayList<String>();
        exec("var "+varName+" ;"+"if "+statementString()+" :",res);
        res.addAll(super.compile(compiler));

        exec("pop "+varName+" ;",res);
        nextIfTag=tagPrefix+"IF"+IfTagsCount;
        IfTagsCount++;
        exec("jz "+nextIfTag,res);
        nextElseTag=tagPrefix+"ELSE"+ElseTagsCount;
        exec("jnz "+nextElseTag,res);
        openedElses.add(nextElseTag);
        exec(nextIfTag,res);
        exec("delv "+varName,res);
        return res;
    }
    public static ArrayList<String> Else(ArrayList<Token>_tokens,Compiler compiler) throws Exception
    {
        ArrayList<String> res=new ArrayList<>();
        if(openedElses.size()<=0){throw new CompilerException("else without if",_tokens.get(0).file,_tokens.get(0).line);}
        String elseTag=openedElses.pop();
        exec(elseTag,res);
        exec("delv "+varName,res);
        closedElses.push(elseTag);
        return  res;
    }

    public static ArrayList<String> EndIf(ArrayList<Token> _tokens, Compiler compiler) throws Exception
    {
        ArrayList<String> res=new ArrayList<>();
        if(openedElses.size()<=0){throw new CompilerException("endif without if",_tokens.get(0).file,_tokens.get(0).line);}
        if(closedElses.size()>=0){exec(";endif",res);}
        else
        {
            return Else(_tokens,compiler);
        }
        return  res;
    }
}
