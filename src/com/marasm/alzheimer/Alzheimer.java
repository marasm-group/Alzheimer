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
    }
    static public boolean LogCPUInstructions=false;

    static public Map<String,Variable> variables=new HashMap<>();
    static public Map<String,Variable> globalVariables=new HashMap<>();
    static public Stack<Map<String,Variable>> variablesStack=new Stack<>();
}