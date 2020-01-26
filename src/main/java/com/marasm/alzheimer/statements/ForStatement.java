package com.marasm.alzheimer.statements;

import com.marasm.alzheimer.Compiler;
import com.marasm.alzheimer.Statement;
import com.marasm.alzheimer.Token;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by vhq473 on 25.02.2016.
 */
public class ForStatement extends Statement {
    static Stack<Statement> afterEachStack = new Stack<>();
    VarStatement var = null;
    SexprStatement prepare = null;
    WhileStatement internalWhile = null;
    SexprStatement afterEach = null;

    public ForStatement(ArrayList<Token> _tokens) {
        int i = 0;
        ArrayList<Token> prepareStatementTokens = readBefore(_tokens, ";", i);
        i += prepareStatementTokens.size();
        ArrayList<Token> whileStatementTokens = readBefore(_tokens, ";", i);
        whileStatementTokens.remove(whileStatementTokens.size() - 1);
        Token tk = new Token();
        tk.value = ":";
        tk.file = whileStatementTokens.get(0).file;
        tk.line = whileStatementTokens.get(0).line;
        whileStatementTokens.add(tk);
        i += whileStatementTokens.size();
        ArrayList<Token> afterEachTokens = readBefore(_tokens, ":", i);
        i += afterEachTokens.size();
        if (prepareStatementTokens.get(0).value.equals("var")) {
            ArrayList<Token> varStatementTokens = readBefore(prepareStatementTokens, "=", 0);
            varStatementTokens.remove(varStatementTokens.size() - 1);
            varStatementTokens.remove(0);
            Token t = new Token();
            t.value = ";";
            t.file = varStatementTokens.get(0).file;
            t.line = varStatementTokens.get(0).line;
            varStatementTokens.add(t);
            for (int j = 0; j < varStatementTokens.size() - 1; j++) {
                prepareStatementTokens.remove(0);
            }
            this.var = new VarStatement(varStatementTokens);
        }
        prepare = new SexprStatement(prepareStatementTokens);
        internalWhile = new WhileStatement(whileStatementTokens);
        afterEach = new SexprStatement(afterEachTokens);
        afterEachStack.push(afterEach);
        for (; i > 0; i--) {
            _tokens.remove(0);
        }
    }

    private static ArrayList<Token> readBefore(ArrayList<Token> _tokens, String end, int i) {
        ArrayList<Token> res = new ArrayList<>();
        for (; i < _tokens.size(); i++) {
            Token t = _tokens.get(i);
            res.add(t);
            if (t.value.equals(end)) {
                break;
            }
        }
        return res;
    }

    public static ArrayList<String> end(ArrayList<Token> _tokens, Compiler compiler) throws Exception {
        ArrayList<String> res = new ArrayList<>();
        if (afterEachStack.size() > 0) {
            res.addAll(afterEachStack.pop().compile(compiler));
        }
        ArrayList<Token> tks = new ArrayList<>();
        Token t = new Token();
        t.value = "endwhile";
        tks.add(t);
        res.addAll(WhileStatement.end(tks, compiler));
        return res;
    }

    public ArrayList<String> compile(Compiler compiler) throws Exception {
        ArrayList<String> res = new ArrayList<>();
        if (var != null) {
            boolean gs = compiler.globalScope;
            compiler.globalScope = false;
            res.addAll(var.compile(compiler));
            compiler.globalScope = gs;
        }
        res.addAll(prepare.compile(compiler));
        res.addAll(internalWhile.compile(compiler));
        return res;
    }
}
