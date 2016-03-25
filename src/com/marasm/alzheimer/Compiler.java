package com.marasm.alzheimer;

import com.marasm.alzheimer.Types.CustomType;
import com.marasm.alzheimer.Types.NumberType;
import com.marasm.alzheimer.statements.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by SR3u on 08.02.2016.
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
    public ArrayList<String> compile(ArrayList<Token> tokens) throws  Exception{return compile(tokens,true);}
    public ArrayList<String> compile(ArrayList<Token> tokens,boolean addHeader) throws  Exception
    {
        try{
            ArrayList<String>cpuCode=new ArrayList<>();
            cpuCode.addAll(compileStatements(tokens));
            if(Alzheimer.trimMarasmComments)
            {
                ArrayList<String> res=new ArrayList<>();
                for(int i=0;cpuCode.size()>0;i++)
                {
                    String cmd=cpuCode.remove(0);
                    String[]cmds=cmd.split("\n");
                    for(String newCmd:cmds)
                    {
                        String tmp=""+newCmd;
                        newCmd=newCmd.replaceAll("\\n","");
                        newCmd=newCmd.replaceAll(";[^']*.*$","");
                        newCmd=newCmd.replaceAll("[\\ ]*$","");
                        if(newCmd.length()>0){res.add(newCmd);}
                    }
                }
                cpuCode=res;
            }
            if(addHeader) {
                exec_begin(";;; Alzheimer generated code ;;;", cpuCode);
                exec_begin("#json\n" +
                        "{\n" +
                        "\"author\":\"" + author + "\",\n" +
                        "\"dependencies\":[" + dependenciesStr() + "],\n" +
                        "\"compiler\":\"Alzheimer\",\n" +
                        "\"Alzheimer\":" + alzheimerStr() + "\n" +
                        "}\n" +
                        "#end", cpuCode);
                exec("halt 0 ; end of code generation", cpuCode);
            }
            return cpuCode;
        }
        catch (CompilerException ce)
        {
            System.out.println("In file "+ce.file+" line "+ce.line+" "+ce.getLocalizedMessage());
            throw new Exception("compilation error",ce);
        }
    }
    ArrayList<String> compileStatements(ArrayList<Token> tokens) throws Exception
    {
        ArrayList<String>cpuCode=new ArrayList<>();
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
                        if(!Alzheimer.variables.inFun()) {
                            throw new CompilerException("end without fun:",t.file,t.line);}
                        Alzheimer.variables.pop();
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
                    case "type":
                        cpuCode.addAll(new TypeStatement(tokens).compile(this));
                        break;
                    case "for":
                        cpuCode.addAll(new ForStatement(tokens).compile(this));
                        break;
                    case "endfor":
                        cpuCode.addAll(ForStatement.end(tokens,this));
                        break;
                    default:
                        throw new CompilerException("Illegal keyword '"+t.value+"'",t.file,t.line);
                }
            }
            else
            {
                tokens.add(0,t);
                cpuCode.addAll(new SexprStatement(tokens).compile(this));
            }
        }
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
        cpuCode.add(0,cmd);
    }
    private String alzheimerStr()
    {
        JSONObject json=new JSONObject();
        JSONArray types=new JSONArray();
        for(String tname:Alzheimer.types.keySet())
        {
            Type t=Alzheimer.types.get(tname);
            if(t.getClass().equals(CustomType.class))
            {
                CustomType ct=(CustomType)t;
                if(ct.export)
                {
                    types.put(ct.toJSON());
                }
            }
        }
        json.put("types",types);
        return json.toString();
    }
    private String dependenciesStr()throws Exception
    {
        String res="";
        for(int i=0;i<dependencies.size();i++)
        {
            Token d=dependencies.get(i);
            if(!d.isString()){
                throw new CompilerException("String expected!",d.file,d.line);}
            res+=d.value+",";
        }
        res+="\"alzheimer\"";
        return res;
    }
    private Token pop(ArrayList<Token> tokens)
    {
        Token t=tokens.get(0);
        tokens.remove(0);
        return t;
    }
}
