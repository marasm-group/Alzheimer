package com.marasm.alzheimer.statements;

import com.marasm.alzheimer.*;
import com.marasm.alzheimer.Compiler;

import java.util.ArrayList;

/**
 * Created by vhq473 on 08.02.2016.
 */
public class SexprStatement extends Statement
{
    static String A="__ALZ_A";
    static String B="__ALZ_B";

    public SexprStatement(ArrayList<Token> _tokens)
    {
        this(_tokens,";");
    }
    public SexprStatement(ArrayList<Token> _tokens,String last)
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
    }

    public SexprStatement() {}

    public ArrayList<String> compile(Compiler compiler) throws Exception
    {
        ArrayList<String>res=new ArrayList<>();
        exec("var "+A+" \n"+"var "+B+" ; "+statementString(),res);
        String eq=tokens.get(1).value;
        String result=tokens.get(0).value;
        if(eq.equals("=")){tokens.remove(0);tokens.remove(0);}
        res.addAll(expression(tokens));
        if(eq.equals("="))
        {
            exec("pop "+result,res);
        }
        exec("delv "+A+" \n"+"delv "+B+" ; end of statement",res);
        return res;
    }

    private ArrayList<String> expression(ArrayList<Token> tokens)throws Exception
    {
        ArrayList<String> res=new ArrayList<>();
        String op=tokens.remove(0).value;
        if(!op.equals("("))
        {
            Token tmp=tokens.remove(0);
            if(!tmp.value.equals("("))
            {
                throw new CompilerException("( expected",tmp.file,tmp.line);
            }
        }
        Token tmp=tokens.remove(tokens.size()-1);
        if(!tmp.value.equals(")"))
            {throw new CompilerException(") expected",tmp.file,tmp.line);}
        ArrayList<String> args=new ArrayList<>();
        while (tokens.size()!=0)
        {
            Token t;
            if(tokens.size()>1)
            {
                t=tokens.get(1);
                if(t.value.equals("("))
                {
                    res.addAll(expression(tokens));
                    args.add("");
                }
                else
                {
                    t=tokens.remove(0);
                    //args.add(t.value);
                    exec("push "+t.value,res);
                }
            }
            else
            {
                t=tokens.remove(0);
                //args.add(t.value);
                exec("push "+t.value,res);
            }

        }
        switch (op)
        {
            case "(":
                break;
            case "+":
            case "add":
                exec("pop "+B+"\n" +
                     "pop "+A+"\n" +
                     "add "+A+" "+A+" "+B+"\n" +
                     "push "+A+" ", res);
                break;
            case "-":
            case "sub":
                exec("pop "+B+"\n" +
                     "pop "+A+"\n" +
                     "sub "+A+" "+A+" "+B+"\n" +
                     "push "+A+" ",res);
                break;
            case "*":
            case "mul":
                exec("pop "+B+"\n" +
                     "pop "+A+"\n" +
                     "mul "+A+" "+A+" "+B+"\n" +
                     "push "+A+" ",res);
                break;
            case "/":
            case "div":
                exec("pop "+B+"\n" +
                     "pop "+A+"\n" +
                     "div "+A+" "+A+" "+B+"\n" +
                     "push "+A+" ",res);
                break;
            default:
                exec("call $"+op,res);

        }
        return res;
    }
    public String statementString()
    {
        String res="";
        for (Token t:tokens)
        {
            res+=t.value+" ";
        }
        return res;
    }
}
