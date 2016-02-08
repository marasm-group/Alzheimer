package com.marasm;

import com.marasm.alzheimer.*;
import com.marasm.alzheimer.Compiler;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args)
    {
        try {
            Tokenizer T=new Tokenizer();
            ArrayList<Token> tokens=T.tokenize(new FileReader("/Users/vhq473/test.alz"));
            Compiler C=new Compiler();
            C.compile(tokens);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
