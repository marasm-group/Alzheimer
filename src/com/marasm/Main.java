package com.marasm;

import com.marasm.alzheimer.*;
import com.marasm.alzheimer.Compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args)
    {
        String srcPath;
        if(args.length > 0)
        {
            srcPath = args[0];
        }
        else
        {
            srcPath = "/Users/vhq473/test.alz";
        }
        try {
            Tokenizer T=new Tokenizer();
            ArrayList<Token> tokens=T.tokenize(new File(srcPath));
            Compiler C=new Compiler();
            C.compile(tokens);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
