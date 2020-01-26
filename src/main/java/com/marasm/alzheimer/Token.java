package com.marasm.alzheimer;


import java.util.Stack;

/**
 * Created by SR3u on 08.02.2016.
 */
public class Token {
    public String value = "";
    public long line = 0;
    public String file = "<UNKNOWN>";

    public boolean isNumber() {
        if (value.length() == 0) {
            return false;
        }
        return value.matches("[-+]?\\d*\\.?\\d+");
    }

    public boolean isType() {
        if (value.length() == 0) {
            return false;
        }
        return value.startsWith(":");
    }

    public String toString() {
        return "'" + value + "'";
    }

    public boolean isKeyword() {
        return Alzheimer.keywords.contains(value);
    }

    public boolean isCharacter() {
        return value.length() > 1 && value.startsWith("\'") && value.endsWith("\'");
    }

    public boolean isString() {
        return value.length() > 1 && value.startsWith("\"") && value.endsWith("\"");
    }

    public String pushString() {
        if (!isString()) {
            return "";
        }
        String value = this.value.replaceFirst("\"", "");
        StringBuilder b = new StringBuilder(value);
        b.replace(value.lastIndexOf("\""), value.lastIndexOf("\"") + 1, "");
        value = b.toString();
        Stack<String> reverse = new Stack<>();
        String res = "";
        res += "push 0 ; push " + this.value + " \n";
        for (int i = 0; i < value.length(); i++) {
            String strchr = Character.toString(value.charAt(i));
            if (strchr.equals("\\")) {
                if (i < value.length() - 1) {
                    i++;
                    strchr += Character.toString(value.charAt(i));
                }
            }
            reverse.push(strchr);
        }
        while (reverse.size() > 0) {
            res += "push '" + reverse.pop() + "'\n";
        }
        return res;
    }

    public String valueWithoutIndex() {
        int idx = value.indexOf("[");
        if (idx == -1) {
            return value;
        }
        return value.substring(0, idx);
    }

    public String valueAfterIndex() {
        int idx = value.indexOf("]");
        if (idx == -1) {
            return "";
        }
        return value.substring(idx).replaceAll("\\]", "");
    }

    public String indexValue() {
        int idx1 = value.indexOf("[");
        if (idx1 == -1) {
            return "";
        }
        int idx2 = value.indexOf("]");
        if (idx2 == -1) {
            return "";
        }
        return value.substring(idx1, idx2 + 1);
    }

    public String valueBeforeFirstDot() {
        return value.split("\\.", 2)[0];
    }

    public String valueAfterFirstDot() {
        String[] arr = value.split("\\.", 2);
        if (arr.length < 2) {
            return "";
        }
        return arr[1];
    }

    public boolean isArray() {
        return value.contains("[") && value.contains("]");
    }
}
