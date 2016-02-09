package com.marasm;

import com.marasm.alzheimer.*;
import com.marasm.alzheimer.Compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args)
    {
        String srcPath;
        String dstPath;
        if(args.length > 0)
        {
            srcPath = args[0];
        }
        else
        {
            srcPath = "/Users/vhq473/test.alz";
        }
        if(args.length > 1)
        {
            dstPath = args[1];
        }
        else
        {
            dstPath = "/Users/vhq473/test.marasm";
        }
        try {
            Tokenizer T=new Tokenizer();
            ArrayList<Token> tokens=T.tokenize(new File(srcPath));
            Compiler C=new Compiler();
            ArrayList<String> cpuCode=C.compile(tokens);
            FileWriter outFile=new FileWriter(dstPath);
            for (String cmd:cpuCode)
            {
                outFile.write(cmd+"\n");
            }
            outFile.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
