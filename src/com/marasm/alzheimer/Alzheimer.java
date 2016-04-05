package com.marasm.alzheimer;

import com.marasm.alzheimer.Types.NumberType;

import java.util.*;

/**
 * Created by SR3u on 08.02.2016.
 */
public class Alzheimer
{
    static public ArrayList<String> keywords=new ArrayList<>(Arrays.asList(
            "gvar",//Global VARiable
            "var",//local VARiable
            "if","else","endif","while","endwhile","break","continue",
            "for","endfor",
            "fun:",//FUNction declaration
            "return",//RETURN from function/or program
            "type",//custom TYPE declaration
            "end",// end of function/if/while clause
            ";",// end of statement
            "$:",// https://en.wikipedia.org/wiki/S-expression
            "asm:",//assembler instruction
            "import"
    ));
    static public Map<String,Type>types;
    static {
        types=new HashMap<>();
        types.put(":number",new NumberType());
        types.put(":Number",new NumberType());
    }
    static public boolean LogCPUInstructions=false;
    static public boolean trimMarasmComments=true;
    static public boolean useStackGuard = false;
    static public Variables variables=new Variables();

    static public ArrayList<String> compile(String code) throws Exception
    {
        Tokenizer t=new Tokenizer();
        Compiler c=new Compiler();
        return c.compile(t.tokenize(code),false);
    }
}