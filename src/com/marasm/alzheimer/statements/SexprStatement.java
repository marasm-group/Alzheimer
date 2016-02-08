package com.marasm.alzheimer.statements;

import com.marasm.alzheimer.Alzheimer;
import com.marasm.alzheimer.Statement;
import com.marasm.alzheimer.Token;
import com.marasm.alzheimer.Compiler;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by vhq473 on 08.02.2016.
 */
public class SexprStatement extends Statement
{
    public SexprStatement(ArrayList<Token> _tokens)
    {
        super();
        Token t=_tokens.remove(0);
        tokens=new ArrayList<>();
        for(int i=0;!t.value.equals(";");i++)
        {
            tokens.add(t);
            t=_tokens.remove(0);
        }
    }

    public ArrayList<String> compile(Compiler compiler) throws Exception
    {
        ArrayList<String>res=new ArrayList<>();
        exec("var __lisp_a \n"+"var __lisp_b ; "+statementString(),res);
        String eq=tokens.get(1).value;
        String result=tokens.get(0).value;
        if(eq.equals("=")){tokens.remove(0);tokens.remove(0);}
        res.addAll(expression(tokens));
        if(eq.equals("="))
        {
            exec("pop "+result,res);
        }
        exec("delv __lisp_a \n"+"delv __lisp_b ; end of statement",res);
        return res;
    }

    private ArrayList<String> expression(ArrayList<Token> tokens)throws Exception
    {
        Stack<String> res=new Stack<>();
        String op=tokens.remove(0).value;
        if(!op.equals("("))
        {
            if(!tokens.remove(0).value.equals("("))
            {
                throw new Exception("( expected");
            }
        }
        if(!tokens.remove(tokens.size()-1).value.equals(")")){throw new Exception(") expected");}
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
            case "add":
                exec("pop __lisp_b\n" +
                     "pop __lisp_a\n" +
                     "add __lisp_a __lisp_a __lisp_b\n" +
                     "push __lisp_a ", res);
                break;
            case "sub":
                exec("pop __lisp_b\n" +
                     "pop __lisp_a\n" +
                     "sub __lisp_a __lisp_a __lisp_b\n" +
                     "push __lisp_a ",res);
                break;
            case "mul":
                exec("pop __lisp_b\n" +
                     "pop __lisp_a\n" +
                     "mul __lisp_a __lisp_a __lisp_b\n" +
                     "push __lisp_a ",res);
                break;
            case "div":
                exec("pop __lisp_b\n" +
                     "pop __lisp_a\n" +
                     "div __lisp_a __lisp_a __lisp_b\n" +
                     "push __lisp_a ",res);
                break;

        }
        ArrayList<String>result=new ArrayList<>();
        while(res.size()>0)
        {
            result.add(res.pop());
        }
        return result;
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
