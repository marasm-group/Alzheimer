package com.marasm.alzheimer;


/**
 * Created by vhq473 on 09.02.2016.
 */
public class CompilerException extends Exception
{
    public String file="<UNKNOWN>";
    public long line=0;
    public CompilerException(Object message, String file, long line)
    {
        super(""+message);
        this.line=line;
        this.file=file;
    }
    CompilerException(Object message,long line){this(message,"<UNKNOWN>",line);}
}
