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
        options.addOption("src",true,"Alzheimer source file");
        options.addOption("in",true,"Alzheimer source file");
        options.addOption("out",true,"output marasm file");
        options.addOption("author",true,"Author of file");
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

    private void PrintInfo()
    {
        System.out.println("Alzheimer Language Compiler");
        System.out.println("2015-2016(c) marasm-group");
        System.out.println("More info at: http://github.com/marasm-group/Alzheimer");
    }
}
