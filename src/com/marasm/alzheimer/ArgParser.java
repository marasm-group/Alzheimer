package com.marasm.alzheimer;

/**
 * Created by abogdanov on 09.02.16.
 */

import java.util.ArrayList;

public class ArgParser {
    private String srcPath;
    private String dstPath;
    private String author;
    private boolean dontCompile;

    private ArrayList<String> cmdArgs;

    private final String[] keywords = {"--src", "--file", "--author", "--help"};

    public ArgParser(String[] args)
    {
        cmdArgs = new ArrayList<String>();
        for(String tmp : args)
        {
            cmdArgs.add(tmp);
        }
        dontCompile = false;
    }

    public String getSrcPath()
    {
        return srcPath;
    }

    public String getDstPath()
    {
        return dstPath;
    }

    public String getAuthor()
    {
        return author;
    }

    public boolean getDontCompile()
    {
        return dontCompile;
    }

    public void Parse()
    {
        while(cmdArgs.size() > 0)
        {
            if(keywords[0].equals(cmdArgs.get(0)))
            {
                if(cmdArgs.size() > 1)
                {
                    srcPath = cmdArgs.get(1);
                }
                else {
                    BadArgs();
                    return;
                }

            }
            else if(keywords[1].equals(cmdArgs.get(0)))
            {
                if(cmdArgs.size() > 1)
                {
                    dstPath = cmdArgs.get(1);
                }
                else {
                    BadArgs();
                    return;
                }
            }
            else if(keywords[2].equals(cmdArgs.get(0)))
            {
                if(cmdArgs.size() > 1)
                {
                    author = cmdArgs.get(1);
                }
                else {
                    BadArgs();
                    return;
                }
            }
            else if(keywords[3].equals(cmdArgs.get(0)))
            {
                PrintHelp();
                return;
            }
            else
            {
                BadArgs();
                return;
            }
            cmdArgs.remove(0);
            cmdArgs.remove(0);
        }
        PrintInfo();
    }

    private void BadArgs()
    {
        System.out.println("Bad command line arguments");
        PrintHelp();
    }

    private void PrintHelp()
    {
        PrintInfo();
        System.out.println("Available arguments: ");
        System.out.println("    --src <filename> - specify alzheimer source file location.");
        System.out.println("    --file <filename> - specify mvm output file destination.");
        System.out.println("    --author <name> - specify author name.");
        System.out.println("    --help - print this help message");
        srcPath = null;
        dstPath = null;
        author = null;
        dontCompile = true;
    }

    private void PrintInfo()
    {
        System.out.println("Alzheimer Language Compiler");
        System.out.println("2015-2016(c) marasm-group");
        System.out.println("More info at: http://github.com/marasm-group/Alzheimer");
    }
}
