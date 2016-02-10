package com.marasm.alzheimer;

/**
 * Created by abogdanov on 09.02.16.
 */

import java.util.ArrayList;

public class ArgParser {
    private String srcPath;
    private String dstPath;
    private String author;

    private ArrayList<String> cmdArgs;

    private final String[] keywords = {"--src", "--file", "--author"};

    public ArgParser(String[] args)
    {
        cmdArgs = new ArrayList<String>();
        for(String tmp : args)
        {
            cmdArgs.add(tmp);
        }
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

    public void Parse()
    {
        System.out.println(cmdArgs.toString());
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
            else
            {
                BadArgs();
                return;
            }
            cmdArgs.remove(0);
            cmdArgs.remove(0);
        }
    }

    private void BadArgs()
    {
        System.out.println("Bad command line arguments");
        srcPath = null;
        dstPath = null;
        author = null;
    }
}
