package com.marasm.alzheimer;

import com.marasm.alzheimer.statements.*;

import java.util.ArrayList;

/**
 * Created by vhq473 on 08.02.2016.
 */
public class Compiler
{
    public boolean globalScope=true;
    public String returnType=null;
    String author=null;
    public  Compiler(){this(null);}
    public Compiler(String author)
    {
        this.author=author;
        if(this.author==null)
        {
            this.author=System.getProperty("user.name");
            if(this.author==null)
            {
                this.author="<Anonymous>";
            }
        }
    }

    public ArrayList<String> compile(ArrayList<Token> tokens) throws  Exception
    {
        try{
            return compile_internal(tokens);
        }
        catch (CompilerException ce)
        {
            System.out.println("In file "+ce.file+" line "+ce.line+" "+ce.getLocalizedMessage());
            throw new Exception("compilation error",ce);
        }
    }
    private ArrayList<String> compile_internal(ArrayList<Token> tokens) throws Exception
    {
        ArrayList<String>cpuCode=new ArrayList<>();
        exec("#json\n" +
             "{\n" +
             "\"author\":\""+author+"\",\n" +
             "\"dependencies\":[],\n" +
             "\"compiler\":\"Alzheimer\",\n" +
             "\"init\":\"$__ALZ_INIT\"\n" +
             "}\n" +
             "#end",cpuCode);
        exec(";;; Alzheimer generated code ;;;",cpuCode);
        while (!tokens.isEmpty())
        {
            Token t=pop(tokens);
            if(t.isKeyword())
            {
                boolean gs;
                switch (t.value)
                {
                    case "gvar":
                        gs=globalScope;
                        globalScope=true;
                        cpuCode.addAll(new VarStatement(tokens).compile(this));
                        globalScope=gs;
                        break;
                    case "var":
                        gs = globalScope;
                        globalScope=false;
                        cpuCode.addAll(new VarStatement(tokens).compile(this));
                        globalScope=gs;
                        break;
                    case "$:":
                        cpuCode.addAll(new SexprStatement(tokens).compile(this));
                        break;
                    case "asm:":
                        cpuCode.addAll(new AsmStatement(tokens).compile(this));
                        break;
                    case "fun:":
                        cpuCode.addAll(new FunStatement(tokens).compile(this));
                        globalScope=false;
                        break;
                    case "end":
                        exec("ret",cpuCode);
                        globalScope=true;
                        break;
                    case "return":
                        cpuCode.addAll(new ReturnStatement(tokens).compile(this));
                        break;
                    case "if":
                        cpuCode.addAll(new IfStatement(tokens).compile(this));
                        break;
                    case "else":
                        cpuCode.addAll(IfStatement.Else(tokens,this));
                        break;
                    case "endif":
                        cpuCode.addAll(IfStatement.EndIf(tokens,this));
                        break;
                    case "while":
                        cpuCode.addAll(new WhileStatement(tokens).compile(this));
                        break;
                    case "break":
                        cpuCode.addAll(WhileStatement.Break(tokens,this));
                        break;
                    case "continue":
                        cpuCode.addAll(WhileStatement.Continue(tokens,this));
                        break;
                    case "endwhile":
                        cpuCode.addAll(WhileStatement.end(tokens,this));
                        break;
                    default:
                        break;
                }
            }
        }
        exec("halt 0; additional utilities",cpuCode);
        exec("$__ALZ_INIT ; alzheimer initialization",cpuCode);
        exec("gvar True",cpuCode);
        exec("gvar False",cpuCode);
        exec("mov True 1",cpuCode);
        exec("mov False 0",cpuCode);
        exec("ret",cpuCode);
        cpuCode.addAll(IfStatement.endGeneration());
        exec("halt 0 ; end of code generation",cpuCode);
        return cpuCode;
    }
    private void exec(String cmd,ArrayList<String>cpuCode)
    {
        if(Alzheimer.LogCPUInstructions){System.out.println(cmd);}
        cpuCode.add(cmd);
    }
    private Token pop(ArrayList<Token> tokens)
    {
        Token t=tokens.get(0);
        tokens.remove(0);
        return t;
    }
}
