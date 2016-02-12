package com.marasm.alzheimer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SR3u on 08.02.2016.
 */
public class Tokenizer
{
    ArrayList<Token> tokens;
    public ArrayList<Token> tokenize(File _file) throws FileNotFoundException, CompilerException {
        FileReader file=new FileReader(_file);
        tokens=new ArrayList<>();
        String fileName=_file.getName();
        int line=0;
        try {
            int chr=0;
            Token t=new Token();
            chr=file.read();
            while(chr!=-1)
            {
                String chrstr=Character.toString((char)chr);
                if(chrstr.equals("'"))
                {
                    if(t.value.length()>0) {
                        t = addToken(t, fileName, line);
                    }
                        t.value+=chrstr;
                        chr=file.read();
                        chrstr=Character.toString((char)chr);
                        while ((!chrstr.equals("'")||(t.value.endsWith("\\")))&&!(chr <0))
                        {
                            t.value+=chrstr;
                            chr=file.read();
                            chrstr=Character.toString((char)chr);
                        }
                        t.value+=chrstr;
                        if(t.value.length()<3){throw  new CompilerException("short size for char constant",fileName,line);}
                        t=addToken(t,fileName,line);
                        chr=file.read();

                    chrstr=Character.toString((char)chr);
                }
                if(chrstr.equals("\""))
                {
                    if(t.value.length()>0) {
                        t = addToken(t, fileName, line);
                    }
                    t.value+=chrstr;
                    chr=file.read();
                    chrstr=Character.toString((char)chr);
                    while ((!chrstr.equals("\"")||(t.value.endsWith("\\")))&&!(chr <0))
                    {
                        t.value+=chrstr;
                        chr=file.read();
                        chrstr=Character.toString((char)chr);
                    }
                    t.value+=chrstr;
                    if(t.value.length()<3){throw  new CompilerException("short size for char constant",fileName,line);}
                    t=addToken(t,fileName,line);
                    chr=file.read();
                    chrstr=Character.toString((char)chr);
                }
                if(chrstr.equals("#"))
                {
                    while(!chrstr.equals("\n"))
                    {
                        chr=file.read();
                        chrstr=Character.toString((char)chr);
                    }
                    chr=file.read();
                    chrstr=Character.toString((char)chr);
                }
                if(isWhitespace(chrstr))
                {
                    if(chrstr.equals("\n")){line++;}
                    t=addToken(t,fileName,line);
                }
                else
                {
                    if(chrstr.equals("(")||chrstr.equals(")")||chrstr.equals(",")||chrstr.equals(";")||chrstr.equals("="))
                    {
                        t=addToken(t,fileName,line);
                        t.value+=chrstr;
                        t=addToken(t,fileName,line);
                    }
                    else
                    {
                        t.value+=chrstr;
                    }
                }
                chr=file.read();
            }
            t=addToken(t,fileName,line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tokens;
    }
    public boolean isWhitespace(String s)
    {
        boolean check=s.trim().length() <= 0;
        if(check){return check;}
        check=s.replaceAll("\\,","").length()<=0;
        return check;
    }
    public Token addToken(Token t,String file,long line)
    {
        if(t.value.length()==0){return t;}
        t.line=line;
        t.file=file;
        tokens.add(t);
        return new Token();
    }
}
