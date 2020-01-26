package com.marasm;

import com.marasm.alzheimer.ArgParser;
import com.marasm.alzheimer.Compiler;
import com.marasm.alzheimer.Token;
import com.marasm.alzheimer.Tokenizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {
    public static String PWD() {
        return Paths.get(".").toAbsolutePath().normalize().toString() + File.separator;
    }

    public static void main(String[] args) {
        String srcPath;
        String dstPath;
        String author;

        ArgParser parser = new ArgParser(args);
        parser.Parse();

        if (parser.getDontCompile() == true) {
            return;
        }
        if ((srcPath = parser.getSrcPath()) == null) {
            srcPath = PWD() + "in.alz";
        }
        if ((dstPath = parser.getDstPath()) == null) {
            dstPath = PWD() + "out.marasm";
        }
        author = parser.getAuthor();

        try {
            System.out.println("Compiling alzheimer source " + srcPath);
            Tokenizer T = new Tokenizer();
            ArrayList<Token> tokens = T.tokenize(new File(srcPath));
            Compiler C = new Compiler(author);
            ArrayList<String> cpuCode = C.compile(tokens);
            FileWriter outFile = new FileWriter(dstPath);
            for (String cmd : cpuCode) {
                outFile.write(cmd + "\n");
            }
            outFile.flush();
            System.out.println("Created mvm executable file " + dstPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
