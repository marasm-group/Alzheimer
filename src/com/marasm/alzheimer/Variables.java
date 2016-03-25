package com.marasm.alzheimer;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by vhq473 on 25.03.2016.
 */
public class Variables
{
    static private Map<String,Variable> variables=new HashMap<>();
    static private Map<String,Variable> globalVariables=new HashMap<>();
    static public Stack<Map<String,Variable>> variablesStack=new Stack<>();

    public void removeGlobal(String key){remove(key,true);}
    public void remove(String key){remove(key,false);}
    public void remove(String key,boolean global)
    {
        if(global){globalVariables.remove(key);}
        else {variables.remove(key);}
    }

    public void addGlobal(String key,Variable v){add(key,v,true);}
    public void add(String key,Variable v){add(key,v,false);}
    public void add(String key,Variable v,boolean global)
    {
        if(global) {globalVariables.put(key,v);}
        else{variables.put(key, v);}
    }
    public boolean exists(String key)
    {
        if(variables.containsKey(key)){return true;}
        return globalVariables.containsKey(key);
    }
    public Variable get(String key)
    {
        if(variables.containsKey(key)){return variables.get(key);}
        return globalVariables.get(key);
    }
    public void push(){
        variablesStack.push(variables);
        variables = new HashMap<>();
    }
    public void pop(){variables=variablesStack.pop();}
    public boolean inFun(){return variablesStack.size()>0;}
}
