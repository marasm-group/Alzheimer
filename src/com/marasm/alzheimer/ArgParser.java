package com.marasm.alzheimer;

/**
 * Created by abogdanov on 09.02.16.
 */

import org.apache.commons.cli.*;

import java.util.ArrayList;

public class ArgParser {
    private String srcPath;
    private String dstPath;
    private String author;
    private boolean dontCompile=false;
    String[]args;
    Options options=new Options();

    private ArrayList<String> cmdArgs;

    private final String[] keywords = {"--src", "--file", "--author", "--help"};

    public ArgParser(String[] args)
    {
        options.addOption("src","Alzheimer source file");
        options.addOption("in","Alzheimer source file");
        options.addOption("out","output marasm file");
        options.addOption("author","Author of file");
        options.addOption("h",false,"print help");
        options.addOption("mvmHome",true,"set custom mvm home directory");
        dontCompile = false;
        this.args=args;
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
        PrintInfo();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse( options, args);
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("alzheimer", options);
            System.exit(127);
        }
        if(cmd.hasOption("h"))
        {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("mvm", options);
            System.exit(0);
        }
        if(cmd.hasOption("mvmHome"))
        {
            Utils.setMarasmHome(cmd.getOptionValue("mvmHome"));
        }
        if(cmd.hasOption("src"))
        {
            srcPath=cmd.getOptionValue("src");
        }
        if(cmd.hasOption("in"))
        {
            srcPath=cmd.getOptionValue("in");
        }
        if(cmd.hasOption("out"))
        {
            dstPath=cmd.getOptionValue("out");
        }
        if(cmd.hasOption("author"))
        {
            dstPath=cmd.getOptionValue("author");
        }
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
