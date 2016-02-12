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
    public ArrayList<Token> dependencies=new ArrayList<>();
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
        exec(";;; Alzheimer generated code ;;;",cpuCode);
        while (!tokens.isEmpty())
        {
            Token t=pop(tokens);
            if(t.isKeyword())
            {
                boolean gs;
                switch (t.value)
                {
                    case "import":
                        cpuCode.addAll(new ImportStatement(tokens).compile(this));
                        break;
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
                        if(Alzheimer.variablesStack.size()<=0){
                            throw new CompilerException("end without fun:",t.file,t.line);}
                        Alzheimer.variables=Alzheimer.variablesStack.pop();
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
        exec_begin("#json\n" +
                   "{\n" +
                   "\"author\":\""+author+"\",\n" +
                   "\"dependencies\":["+dependenciesStr()+"],\n" +
                   "\"compiler\":\"Alzheimer\",\n" +
                   "\"init\":\"$__ALZ_INIT\"\n" +
                   "}\n" +
                   "#end",cpuCode);
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
    private void exec_begin(String cmd,ArrayList<String>cpuCode)
    {
        if(Alzheimer.LogCPUInstructions){System.out.println(cmd);}
        ArrayList<String>res=new ArrayList<>();
        res.add(cmd);
        res.addAll(cpuCode);
        cpuCode.clear();
        cpuCode.addAll(res);
    }
    private String dependenciesStr()throws Exception
    {
        String res="";
        for(int i=0;i<dependencies.size();i++)
        {
            Token d=dependencies.get(i);
            if(!d.isString()){
                throw new CompilerException("String expected!",d.file,d.line);}
            res+=d.value;
            if(i<dependencies.size()-1){res+=",";}
        }
        return res;
    }
    private Token pop(ArrayList<Token> tokens)
    {
        Token t=tokens.get(0);
        tokens.remove(0);
        return t;
    }
}
