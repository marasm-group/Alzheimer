package com.marasm.alzheimer;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vhq473 on 08.02.2016.
 */
public class Tokenizer
{
    ArrayList<Token> tokens;
    public ArrayList<Token> tokenize(FileReader file)
    {
       tokens=new ArrayList<>();
        int line=0;
        try {
            int chr=0;
            Token t=new Token();
            chr=file.read();
            while(chr!=-1)
            {
                String chrstr=Character.toString((char)chr);
                if(isWhitespace(chrstr))
                {
                    if(chrstr=="\n"){line++;}
                    t=addToken(t,line);
                }
                else
                {
                    if(chrstr.equals("(")||chrstr.equals(")")||chrstr.equals(",")||chrstr.equals(";")||chrstr.equals("="))
                    {
                        t=addToken(t,line);
                        t.value+=chrstr;
                        t=addToken(t,line);
                    }
                    else
                    {
                        t.value+=chrstr;
                    }
                }
                chr=file.read();
            }
            t=addToken(t,line);
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
    public Token addToken(Token t,int line)
    {
        if(t.value.length()==0){return t;}
        t.line=line;
        tokens.add(t);
        return new Token();
    }
}
