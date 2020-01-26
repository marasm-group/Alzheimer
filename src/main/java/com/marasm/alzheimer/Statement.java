package com.marasm.alzheimer;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by SR3u on 08.02.2016.
 */
public class Statement {
    protected ArrayList<Token> tokens;

    public Statement() {
    }

    public Statement(ArrayList<Token> _tokens) {
        tokens = _tokens;
    }

    protected static void exec(String cmd, Stack<String> result) {
        Alzheimer.exec(cmd, result);
    }

    protected static void exec(String cmd, ArrayList<String> result) {
        Alzheimer.exec(cmd, result);
    }

    public ArrayList<String> compile(Compiler compiler) throws Exception {
        return new ArrayList<>();
    }

}
