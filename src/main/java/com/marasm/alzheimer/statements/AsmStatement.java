package com.marasm.alzheimer.statements;

import com.marasm.alzheimer.Statement;
import com.marasm.alzheimer.Token;

import java.util.ArrayList;

/**
 * Created by SR3u on 08.02.2016.
 */
public class AsmStatement extends Statement {
    public AsmStatement(ArrayList<Token> _tokens) {
        super();
        Token t = _tokens.remove(0);
        tokens = new ArrayList<>();
        for (int i = 0; !t.value.equals("end"); i++) {
            tokens.add(t);
            t = _tokens.remove(0);
        }
        tokens.add(t);
    }

    public ArrayList<String> compile(com.marasm.alzheimer.Compiler compiler) throws Exception {
        ArrayList<String> res = new ArrayList<>();
        String cmd = "";
        for (Token t : tokens) {
            if (t.value.startsWith(";")) {
                cmd += t.value;
                exec(cmd, res);
                cmd = "";
            } else {
                if (t.value.equals("end")) {
                    exec(cmd, res);
                    cmd = "";
                } else {
                    cmd += t.value + " ";
                }
            }
        }
        return res;
    }
}
