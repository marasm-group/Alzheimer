package com.marasm.alzheimer.statements;

import com.google.common.collect.ImmutableMap;
import com.marasm.alzheimer.Compiler;
import com.marasm.alzheimer.*;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by SR3u on 08.02.2016.
 */
public class SexprStatement extends Statement {
    //static String A="__ALZ_A";
    //static String B="__ALZ_B";
    boolean useStackGuard = Alzheimer.useStackGuard;
    private Map<String, String> commonOperations = ImmutableMap.<String, String>builder()
            .put("=", "eq")
            .put("<", "less")
            .put(">", "more")
            .put("<=", "moreeq")
            .put(">=", "lesseq")
            .put("%", "mod")
            .build();

    public SexprStatement(ArrayList<Token> _tokens) {
        this(_tokens, ";");
    }

    public SexprStatement(ArrayList<Token> _tokens, String last) {
        this(_tokens, last, Alzheimer.useStackGuard);
    }

    public SexprStatement(ArrayList<Token> _tokens, boolean stackguard) {
        this(_tokens, ";", stackguard);
    }

    public SexprStatement(ArrayList<Token> _tokens, String last, boolean stackguard) {
        super();
        useStackGuard = stackguard;
        Token t = _tokens.remove(0);
        tokens = new ArrayList<>();
        while (!t.value.equals(last)) {
            tokens.add(t);
            if (_tokens.size() <= 0) {
                break;
            }
            t = _tokens.remove(0);
        }
    }

    public SexprStatement() {
    }

    static private ArrayList<String> pushVariable(Token t) throws Exception {
        ArrayList<String> res = new ArrayList<>();
        Variable v = Alzheimer.variables.get(t.valueWithoutIndex());
        if (v == null) {
            if (!t.isNumber() && !t.isString() && !t.isCharacter()) {
                if (!t.isType()) {
                    throw new CompilerException("Unknown variable " + t.value, t.file, t.line);
                } else {
                    Type T = Alzheimer.types.get(t.valueBeforeFirstDot());
                    if (T == null) {
                        throw new CompilerException("Unknown type " + t.valueBeforeFirstDot(), t.file, t.line);
                    } else {
                        switch (t.valueAfterFirstDot()) {
                            case "size":
                                exec("push " + T.size() + "; " + t.value, res);
                                break;
                            default:
                                throw new CompilerException("Type '" + t.valueBeforeFirstDot() + "' does not have property '" + t.valueAfterFirstDot() + "' ", t.file, t.line);
                        }
                    }
                }
            } else {
                exec("push " + t.value + ";", res);
            }
        } else {
            if (v.isArray && !t.isArray()) {
                res.addAll(pushArray(v, t.file, t.line));
            } else {
                if (t.isArray()) {
                    v = new Variable();
                    v.name = t.value;
                    String alz = "" + v.accessVarName() + "=( " + v.arrayIndex() + " );\n";
                    res.addAll(Alzheimer.compile(alz));
                    res.addAll(v.type.push(v.nameWithoutIndex() + "[" + v.accessVarName() + "]"));
                } else {
                    res.addAll(v.type.push(t.value));
                }
            }
        }
        return res;
    }

    static ArrayList<String> pushArray(Variable v, String fileName, long line) throws CompilerException {
        ArrayList<String> res = new ArrayList<>();
        Tokenizer t = new Tokenizer();
        String code1 = "" +
                "#push array\n" +
                "for var __ALZ_I=0; less(__ALZ_I " + v.sizeVarName() + "); __ALZ_I=add(__ALZ_I 1);\n";
        String code2 = "" +
                "endfor\n" +
                "asm:\n" +
                "\tpush " + v.nameWithoutIndex() + ".size ;\n" +
                "end";
        ArrayList<Token> tokens1 = t.tokenize(code1);
        ArrayList<Token> tokens2 = t.tokenize(code2);
        Compiler c = new Compiler();
        try {
            res.addAll(c.compile(tokens1, false));
            res.addAll(v.type.push(v.nameWithoutIndex() + "[__ALZ_I]"));
            res.addAll(c.compile(tokens2, false));
        } catch (Exception e) {
            CompilerException ex = new CompilerException("failed to push array " + v.nameWithoutIndex(), fileName, line);
            ex.initCause(e);
            throw ex;
        }
        exec("delv __ALZ_I ;", res);
        return res;
    }

    static ArrayList<String> popArray(Variable v, String fileName, long line) throws CompilerException {
        return popArray(v, fileName, line, false);
    }

    static ArrayList<String> popArray(Variable v, String fileName, long line, boolean sizeKnown) throws CompilerException {
        ArrayList<String> res = new ArrayList<>();
        Tokenizer t = new Tokenizer();
        String code1 = "#pop array\n";
        if (!sizeKnown) {
            code1 += "asm:\n" +
                    "\tpop " + v.sizeVarName() + " ;\n" +
                    "end\n";
        }
        code1 += "for var __ALZ_I=sub(" + v.sizeVarName() + " 1); moreeq(__ALZ_I 0); __ALZ_I=sub(__ALZ_I 1);\n";
        String code2 = "endfor\n";
        ArrayList<Token> tokens1 = t.tokenize(code1);
        ArrayList<Token> tokens2 = t.tokenize(code2);
        Compiler c = new Compiler();
        try {
            res.addAll(c.compile(tokens1, false));
            res.addAll(v.type.pop(v.nameWithoutIndex() + "[__ALZ_I]"));
            res.addAll(c.compile(tokens2, false));
        } catch (Exception e) {
            CompilerException ex = new CompilerException("failed to pop array " + v.nameWithoutIndex(), fileName, line);
            ex.initCause(e);
            throw ex;
        }
        exec("delv __ALZ_I ;", res);
        return res;
    }

    public ArrayList<String> compile(Compiler compiler) throws Exception {
        ArrayList<String> res = new ArrayList<>();
        //exec("var "+A+" ; "+statementString()+" \n"+"var "+B,res);
        exec(";" + statementString(), res);
        String eq = tokens.get(1).value;
        Token result = tokens.get(0);
        if (eq.equals("=")) {
            tokens.remove(0);
            tokens.remove(0);
        }
        res.addAll(expression(tokens));
        if (eq.equals("=")) {
            Variable v = Alzheimer.variables.get(result.value);
            if (v == null) {
                v = Alzheimer.variables.get(result.valueWithoutIndex());
                if (v == null) {
                    throw new CompilerException("Unknown variable " + result.value, result.file, result.line);
                }
                String rest = result.valueAfterIndex();
                if (rest.length() <= 0) {
                    Variable tmp = new Variable();
                    tmp.name = result.value;
                    String alz = "" + v.accessVarName() + "=( " + tmp.arrayIndex() + " );\n";
                    res.addAll(Alzheimer.compile(alz));
                    res.addAll(v.type.pop(v.nameWithoutIndex() + "[" + v.accessVarName() + "]"));
                } else {
                    String realName = result.valueWithoutIndex() + result.valueAfterIndex() + "[" + v.arraySize() + "]";
                    Variable real = Alzheimer.variables.get(realName);
                    if (real == null) {
                        throw new CompilerException("Unknown variable " + result.value, result.file, result.line);
                    }
                    Variable tmp = new Variable();
                    tmp.name = result.value;
                    String alz = "" + real.accessVarName() + "=( " + tmp.arrayIndex() + " );\n";
                    res.addAll(Alzheimer.compile(alz));
                    res.addAll(real.type.pop(real.nameWithoutIndex() + "[" + real.accessVarName() + "]"));
                }
            } else {
                if (v.isArray) {
                    res.addAll(popArray(v, result.file, result.line));
                } else {
                    res.addAll(v.type.pop(result.value));
                }
            }
        }
        exec("; end of statement", res);
        //exec("delv "+A+" \n"+"delv "+B+" ; end of statement",res);
        if (useStackGuard) {
            res = Alzheimer.stackGuard(res);
        }
        return res;
    }

    private ArrayList<String> expression(ArrayList<Token> tokens) throws Exception {
        ArrayList<String> res = new ArrayList<>();
        Token opTok = tokens.remove(0);
        if (tokens.size() < 1) {
            Variable v = Alzheimer.variables.get(opTok.valueWithoutIndex());
            if (v == null && !opTok.isType()) {
                if (!opTok.isNumber()) {
                    throw new CompilerException("Number, variable or type property expected", opTok.file, opTok.line);
                }
                res.add("push " + opTok.value + ";");
                return res;
            }
            res.addAll(pushVariable(opTok));
            return res;
        }
        if (!opTok.value.equals("(")) {
            if (tokens.size() != 0) {
                Token tmp = tokens.remove(0);
                if (!tmp.value.equals("(")) {
                    throw new CompilerException("( expected", tmp.file, tmp.line);
                }
            } else {
                throw new CompilerException("Something is wrong!", opTok.file, opTok.line);
            }
        }
        ArrayList<String> args = new ArrayList<>();
        while (tokens.size() != 0) {
            Token t;
            if (tokens.size() > 1) {
                t = tokens.get(1);
                if (t.value.equals("(")) {
                    res.addAll(expression(tokens));
                    args.add("");
                } else {
                    t = tokens.remove(0);
                    if (t.value.equals(")")) {
                        break;
                    }
                    if (t.isString()) {
                        exec(t.pushString(), res);
                    } else {
                        res.addAll(pushVariable(t));
                    }
                }
            } else {
                t = tokens.remove(0);
                if (t.value.equals(")")) {
                    break;
                }
                if (t.isString()) {
                    exec(t.pushString(), res);
                } else {
                    res.addAll(pushVariable(t));
                }
            }

        }
        switch (opTok.value) {
            case "(":
                break;
            case "+":
            case "add":
                exec("add", res);
                break;
            case "-":
            case "sub":
                exec("sub", res);
                break;
            case "*":
            case "mul":
                exec("mul", res);
                break;
            case "/":
            case "div":
                exec("div", res);
                break;
            case "floor":
                exec("floor", res);
                break;
            case "ceil":
                exec("ceil", res);
                break;
            default:
                exec("call $" + substituteCommonOperations(opTok), res);

        }
        return res;
    }

    private String substituteCommonOperations(Token opTok) {
        return commonOperations.getOrDefault(opTok.value, opTok.value);
    }

    public String statementString() {
        String res = "";
        for (Token t : tokens) {
            res += t.value + " ";
        }
        return res + ";";
    }
}
