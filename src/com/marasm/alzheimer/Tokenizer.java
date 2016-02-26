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
    int line=0;
    public ArrayList<Token> tokenize(File _file) throws FileNotFoundException, CompilerException {
        FileReader file=new FileReader(_file);
        tokens=new ArrayList<>();
        String fileName=_file.getName();
        line=0;
        try {
            String chr=readChar(file);
            Token t=new Token();
            while(chr!=null) {
                Token tmp=processCharacter(chr, file,fileName,t);
                {
                    if(tmp!=null)
                    {
                        t=tmp;
                    }
                }
                chr=readChar(file);
            }
            addToken(t,fileName,line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tokens;
    }
    String readChar(FileReader file) throws IOException {
        int chr = file.read();
        if(chr==-1){return null;}
        String chrstr = Character.toString((char) chr);
        return chrstr;
    }
    Token processCharacter(String chr,FileReader file,String fileName,Token t) throws IOException, CompilerException {
        if(chr.equals("#"))
        {
            while(!chr.equals("\n")){
                chr=readChar(file);
                if(chr==null){return null;}
            }
            if(chr.equals("\n")){line++;}
            return t;
        }
        if(isWhitespace(chr))
        {
            t=addToken(t,fileName,line);
            if(chr.equals("\n")){line++;}
            return t;
        }
        if(chr.equals(";")||chr.equals("(")||chr.equals(")"))
        {
            t=addToken(t,fileName,line);
            t.value=chr;
            t=addToken(t,fileName,line);
            return t;
        }
        if(chr.equals("="))
        {
            t=addToken(t,fileName,line);
            t.value=chr;
            t=addToken(t,fileName,line);
            return t;
        }
        if(chr.equals("'"))
        {
            t=addToken(t,fileName,line);
            t.value+=chr;
            chr=readChar(file);
            t.value+=chr;
            while(!chr.equals("'"))
            {
                chr=readChar(file);
                if(chr==null){return null;}
                t.value+=chr;
            }
            if(t.value.length()<3){throw  new CompilerException("'"+t.value+"' is too short for char constant",fileName,line);}
            if(t.value.length()>4){throw  new CompilerException("'"+t.value+"' is too long for char constant",fileName,line);}
            t=addToken(t,fileName,line);
            return t;
        }
        if(chr.equals("\""))
        {
            t=addToken(t,fileName,line);
            t.value+=chr;
            chr=readChar(file);
            t.value+=chr;
            while(!chr.equals("\""))
            {
                chr=readChar(file);
                if(chr==null){return null;}
                t.value+=chr;
            }
            t=addToken(t,fileName,line);
            return t;
        }
        t.value+=chr;
        return null;
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
