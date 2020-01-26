package com.marasm.alzheimer;

import com.marasm.alzheimer.Types.CustomType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vhq473 on 17.02.2016.
 */
public class ModuleLoader {
    static Map<String, JSONObject> filesLoaded = new HashMap<>();

    public static void loadModule(String name) {
        boolean flag = true;
        if (!name.endsWith(".marasm")) {
            name = name + ".marasm";
        }
        if (name.startsWith("./")) {
            File f = new File(name);
            name = Utils.workingDir() + File.separator + f.getName();
            flag = false;
        }
        if (name.startsWith("~")) {
            File f = new File(name);
            name = Utils.homeDir() + File.separator + f.getName();
            flag = false;
        }
        if (flag) {
            name = Utils.marasmModules() + name;
        }
        loadFile(name);
    }

    public static void loadFile(String path) {
        if (filesLoaded.get(path) != null) {
            return;
        }
        ArrayList<String> file;
        try {
            file = readLines(path);
        } catch (IOException e) {
            System.out.println(e.toString() + "\n" + e.getLocalizedMessage() + "\n" + e.getStackTrace().toString());
            return;
        }
        for (int i = 0; i < file.size(); i++) {
            file.set(i, file.get(i).trim());
        }
        JSONObject fileInfo = loadFileHeader(file);
        if (fileInfo == null) {
            System.out.println("In file " + path + "\nFailed to load header from file " + path);
        }
        loadTypes(fileInfo, path);
        filesLoaded.put(path, fileInfo);
        try {
            JSONArray deps = fileInfo.getJSONArray("dependencies");
            for (int i = 0; i < deps.length(); i++) {
                loadModule(deps.getString(i));
            }
        } catch (JSONException e) {
        }
    }

    public static JSONObject loadFileHeader(ArrayList<String> file) {
        int i;
        for (i = 0; i < file.size(); i++) {
            if (file.get(i).startsWith("#json")) {
                break;
            }
        }
        if (i > file.size()) {
            return null;
        }
        long headerBegin, headerEnd;
        headerEnd = headerBegin = i;
        ArrayList<String> headerStr = new ArrayList<>();
        file.remove(i);
        while (file.size() > 0) {
            String tmp = file.get(i);
            file.remove(i);
            headerEnd++;
            if (tmp.startsWith("#end")) {
                break;
            }
            headerStr.add(tmp);
        }
        try {
            String jsonStr = new String();
            for (int j = 0; j < headerStr.size(); j++) {
                jsonStr += headerStr.get(j);
            }
            JSONObject header = new JSONObject(jsonStr);
            header.append("headerBegin", new Long(headerBegin));
            header.append("headerEnd", new Long(headerEnd));
            return header;
        } catch (JSONException e) {
            return null;
        }
    }

    static ArrayList<String> readLines(String filename) throws IOException {
        FileReader fileReader = new FileReader(filename);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> lines = new ArrayList<String>();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        bufferedReader.close();
        return new ArrayList<>(lines);
    }

    static void loadTypes(JSONObject header, String path) {
        try {
            JSONObject alzheimer = header.getJSONObject("Alzheimer");
            JSONArray types = alzheimer.getJSONArray("types");
            for (int i = 0; i < types.length(); i++) {
                JSONObject type = types.getJSONObject(i);
                CustomType customType = new CustomType(type);
                Alzheimer.types.put(customType.name, customType);
            }
        } catch (JSONException e) {
        }
    }
}
