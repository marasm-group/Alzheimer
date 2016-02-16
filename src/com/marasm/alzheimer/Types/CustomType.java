package com.marasm.alzheimer.Types;

import com.marasm.alzheimer.Alzheimer;
import com.marasm.alzheimer.Token;
import com.marasm.alzheimer.Type;
import com.marasm.alzheimer.Variable;

import java.util.ArrayList;

/**
 * Created by vhq473 on 16.02.2016.
 */
public class CustomType extends Type
{
    private String varname;

    public static class Field
    {
        public String type=":number";
        public String name="";
        public Field(){}
        public Field(String name,String type)
        {
            this.name=name;
            this.type=type;
        }
    }
    public ArrayList<Field> fields=new ArrayList<>();

    @Override
    public ArrayList<String> allocate(String name)
    {
        ArrayList<String> res=new ArrayList<>();
        for(Field i:fields)
        {
            Type t=Alzheimer.types.get(i.type);
            String varname=getVarName(name,i);
            Variable v=new Variable();
            v.name=varname;
            Alzheimer.variables.put(varname,v);
            res.addAll(t.allocate(varname));
        }
        Variable v=new Variable();
        v.name=name;
        v.type=this;
        Alzheimer.variables.put(name,v);
        return res;
    }
    public ArrayList<String> gallocate(String name)
    {
        ArrayList<String> res=new ArrayList<>();
        for(Field i:fields)
        {
            Type t=Alzheimer.types.get(i.type);
            String varname=getVarName(name,i);
            Variable v=new Variable();
            v.name=varname;
            Alzheimer.variables.put(varname,v);
            res.addAll(t.gallocate(varname));
        }
        Variable v=new Variable();
        v.name=name;
        v.type=this;
        Alzheimer.variables.put(name,v);
        return res;
    }
    public ArrayList<String> deallocate(String name)
    {
        ArrayList<String> res=new ArrayList<>();
        for(Field i:fields)
        {
            Type t=Alzheimer.types.get(i.type);
            String varname=getVarName(name,i);
            Variable v=new Variable();
            v.name=varname;
            Alzheimer.variables.remove(varname);
            res.addAll(t.deallocate(varname));
        }
        Alzheimer.variables.remove(name);
        return res;
    }
    public ArrayList<String> gdeallocate(String name)
    {
        ArrayList<String> res=new ArrayList<>();
        for(Field i:fields)
        {
            Type t=Alzheimer.types.get(i.type);
            String varname=getVarName(name,i);
            Variable v=new Variable();
            v.name=varname;
            Alzheimer.variables.remove(varname);
            res.addAll(t.gdeallocate(varname));
        }
        Alzheimer.variables.remove(name);
        return res;
    }
    public ArrayList<String> push(String name)
    {
        ArrayList<String> res=new ArrayList<>();
        for(int idx=0;idx<fields.size();idx++)
        {
            Field i=fields.get(idx);
            Type t=Alzheimer.types.get(i.type);
            String varname=getVarName(name,i);
            res.addAll(t.push(varname));
        }
        return res;
    }
    public ArrayList<String> pop(String name)
    {
        ArrayList<String> res=new ArrayList<>();
        for(int idx=fields.size()-1;idx>=0;idx--)
        {
            Field i=fields.get(idx);
            Type t=Alzheimer.types.get(i.type);
            String varname=getVarName(name,i);
            res.addAll(t.pop(varname));
        }
        return res;
    }

    public String toString()
    {
        String res="type{\n";
        for(Field f:fields)
        {
            res+=f.name+" "+f.type+"\n";
        }
        res+="}";
        return res;
    }

    private String getVarName(String name,Field i)
    {
        if(!name.contains("[")){return name+ "."+i.name;}
        Variable v= new Variable();
        v.name=name;
        v.type=this;
        String n=v.nameWithoutIndex();
        String idx=name.substring(n.length());
        return n+"."+i.name+idx;
    }
}
