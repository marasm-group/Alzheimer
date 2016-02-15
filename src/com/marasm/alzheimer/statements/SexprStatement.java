package com.marasm.alzheimer.statements;

import com.marasm.alzheimer.*;
import com.marasm.alzheimer.Compiler;

import java.util.ArrayList;

/**
 * Created by SR3u on 08.02.2016.
 */
public class SexprStatement extends Statement
{
    //static String A="__ALZ_A";
    //static String B="__ALZ_B";

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
        //exec("var "+A+" ; "+statementString()+" \n"+"var "+B,res);
        exec(";"+statementString(),res);
        String eq=tokens.get(1).value;
        Token result=tokens.get(0);
        if(eq.equals("=")){tokens.remove(0);tokens.remove(0);}
        res.addAll(expression(tokens));
        if(eq.equals("="))
        {
            Variable v=Alzheimer.variables.get(result.value);
            if(v==null)
            {
                v=Alzheimer.variables.get(result.valueWithoutIndex());
                if(v==null){
                    throw new CompilerException("Unknown variable "+result.value,result.file,result.line);}
                res.addAll(v.type.pop(result.value));
            }
            else
            {
                if(v.isArray)
                {
                    for(int i=v.arraySize()-1;i>=0;i--)
                    {
                        res.addAll(v.type.pop(v.nameWithoutIndex()+"["+i+"]"));
                    }
                }
                else{res.addAll(v.type.pop(result.value));}
            }
        }
        exec("; end of statement",res);
        //exec("delv "+A+" \n"+"delv "+B+" ; end of statement",res);
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
                    if(t.value.equals(")"))
                    {
                        break;
                    }
                    if(t.isString()){exec(t.pushString(),res);}
                    else{
                        Variable v=Alzheimer.variables.get(t.valueWithoutIndex());
                        if(v==null){
                            if (!t.isNumber()&&!t.isString()&&!t.isCharacter()){
                                throw new CompilerException("Unknown variable "+t.value,t.file,t.line);}
                            exec("push "+t.value+";",res);
                        }else {
                            if (v.isArray && !t.isArray()) {
                                for (int i = 0; i < v.arraySize(); i++) {
                                    res.addAll(v.type.push(v.nameWithoutIndex() + "[" + i + "]"));
                                }
                            } else {
                                res.addAll(v.type.push(t.value));
                            }
                        }
                    }
                }
            }
            else
            {
                t=tokens.remove(0);
                if(t.value.equals(")")){break;}
                if(t.isString()){exec(t.pushString(),res);}
                else{
                    Variable v=Alzheimer.variables.get(t.valueWithoutIndex());
                    if(v==null)
                    {
                        if (!t.isNumber()&&!t.isString()&&!t.isCharacter()){
                            throw new CompilerException("Unknown variable "+t.value,t.file,t.line);}
                        exec("push "+t.value+";",res);
                    }else {
                        if (v.isArray && !t.isArray()) {
                            for (int i = 0; i < v.arraySize(); i++) {
                                res.addAll(v.type.push(v.nameWithoutIndex() + "[" + i + "]"));
                            }
                        } else {
                            res.addAll(v.type.push(t.value));
                        }
                    }
                }
            }

        }
        switch (op)
        {
            case "(":
                break;
            case "+":
            case "add":
                exec("call $add",res);
                break;
            case "-":
            case "sub":
                exec("call $sub",res);
                break;
            case "*":
            case "mul":
                exec("call $mul",res);
                break;
            case "/":
            case "div":
                exec("call $div",res);
                break;
            case "floor":
                exec("call $floor",res);
                break;
            case "ceil":
                exec("call $ceil",res);
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
        return res+";";
    }
}
