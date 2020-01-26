package com.marasm.alzheimer;

import com.marasm.alzheimer.Types.NumberType;

import java.util.*;

/**
 * Created by SR3u on 08.02.2016.
 */
public class Alzheimer {
    static public ArrayList<String> keywords = new ArrayList<>(Arrays.asList(
            "gvar",//Global VARiable
            "var",//local VARiable
            "if", "else", "endif", "while", "endwhile", "break", "continue",
            "for", "endfor",
            "fun:",//FUNction declaration
            "return",//RETURN from function/or program
            "type",//custom TYPE declaration
            "end",// end of function/if/while clause
            ";",// end of statement
            "$:",// https://en.wikipedia.org/wiki/S-expression
            "asm:",//assembler instruction
            "import"
    ));
    static public Map<String, Type> types;
    static public boolean LogCPUInstructions = false;
    static public boolean trimMarasmComments = true;
    static public boolean useStackGuard = false;
    static public Variables variables = new Variables();
    static long stackguardLoops = 0;

    static {
        types = new HashMap<>();
        types.put(":number", new NumberType());
        types.put(":Number", new NumberType());
    }

    static public ArrayList<String> compile(String code) throws Exception {
        Tokenizer t = new Tokenizer();
        Compiler c = new Compiler();
        return c.compile(t.tokenize(code), false);
    }

    public static ArrayList<String> stackGuard(ArrayList<String> res) {
        String before = "__ALZ_SG_B_" + stackguardLoops;
        String after = "__ALZ_SG_A_" + stackguardLoops;
        String dummy = "__ALZ_SG_DUMMY_" + stackguardLoops;
        execBefore("in " + before + " 0.1", res);
        execBefore("var " + after, res);
        execBefore("var " + before, res);
        execBefore("var " + dummy + " ; stack guard initialization", res);
        String tag = "@__ALZ_SG_" + stackguardLoops;
        String tagEnd = "@__ALZ_SG_END_" + stackguardLoops;
        exec(tag + " ; stack guard loop", res);
        exec("in " + after + " 0.1", res);
        exec("sub " + after + " " + after + " " + before, res);
        exec("jz " + after + " " + tagEnd, res);
        exec("pop " + dummy, res);
        exec("jmz " + after + " " + tag, res);
        exec(tagEnd + " ; end of stack guard loop", res);
        exec("delv " + dummy, res);
        exec("delv " + before, res);
        exec("delv " + after + " ; end of stack guard", res);
        stackguardLoops++;
        return res;
    }

    public static void execBefore(String cmd, ArrayList<String> res) {
        res.add(0, cmd);
        if (Alzheimer.LogCPUInstructions) {
            System.out.println(cmd);
        }
    }

    public static void exec(String cmd, Stack<String> result) {
        if (Alzheimer.LogCPUInstructions) {
            System.out.println(cmd);
        }
        result.push(cmd + " ;");
    }

    public static void exec(String cmd, ArrayList<String> result) {
        if (Alzheimer.LogCPUInstructions) {
            System.out.println(cmd);
        }
        result.add(cmd + " ;");
    }
}