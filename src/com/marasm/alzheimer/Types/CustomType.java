package com.marasm.alzheimer.Types;

import com.marasm.alzheimer.Alzheimer;
import com.marasm.alzheimer.Type;
import com.marasm.alzheimer.Variable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by vhq473 on 16.02.2016.
 */
public class CustomType extends Type
{
    private String varname;
    public String name;
    public boolean export=true;
    public CustomType(String name)
    {
        this.name=name;
    }
    public CustomType(JSONObject json) throws JSONException
    {
        super();
        this.name=json.getString("name");
        JSONArray fields=json.getJSONArray("fields");
        for(int i=0;i<fields.length();i++)
        {
            this.fields.add(new Field(fields.getJSONObject(i)));
        }
        export=false;
    }


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
        public Field(JSONObject json) throws JSONException
        {
            this.name=json.getString("name");
            this.type=json.getString("type");
        }
        public JSONObject toJSON()
        {
            JSONObject json=new JSONObject();
            json.put("name",name);
            json.put("type",type);
            return json;
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
            Alzheimer.variables.add(varname,v);
            res.addAll(t.allocate(varname));
        }
        Variable v=new Variable();
        v.name=name;
        v.type=this;
        Alzheimer.variables.add(name,v);
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
            Alzheimer.variables.addGlobal(varname,v);
            res.addAll(t.gallocate(varname));
        }
        Variable v=new Variable();
        v.name=name;
        v.type=this;
        Alzheimer.variables.addGlobal(name,v);
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
            Alzheimer.variables.removeGlobal(varname);
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
        return toJSON().toString();
    }
    public JSONObject toJSON()
    {
        JSONObject json=new JSONObject();
        json.put("name",name);
        JSONArray fields=new JSONArray();
        for(Field f:this.fields)
        {
            fields.put(f.toJSON());
        }
        json.put("fields",fields);
        return json;
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
