package tudbut.parsing;

import tudbut.obj.TLMap;
import tudbut.tools.Stack;
import tudbut.tools.StringTools;

import java.util.ArrayList;
import java.util.HashMap;

public class JSON {
    
    public static TCN read(String string) throws JSONFormatException {
        TCN tcn = new TCN();
        boolean escape = false;
        while (string.startsWith(" ")) {
            string = string.substring(1);
        }
        if(!string.startsWith("{")) {
            throw new JSONFormatException("Expected: { at 0 (String is '" + string + "')");
        }
        int pos = 1;
        char[] a = string.toCharArray();
        char c = a[pos];
        boolean inString = false;
        boolean startString = false;
        String theString = "";
        boolean kv = false;
        String key = "";
        boolean inStringKV = false;
        boolean inObjectKV = false;
        TCN sub = null;
        
        try {
            while (inString || c != '}') {
                if (startString) {
                    inString = true;
                    startString = false;
                }
                if (c == '\\') {
                    escape = !escape;
                }
                if (!escape && c == '"') {
                    startString = !inString;
                    if (startString) {
                        if (kv)
                            inStringKV = true;
                        theString = "";
                    }
                    else {
                        inString = false;
                        if (!kv) { // Key
                            key = theString;
                        }
                    }
                }
                if (inString) {
                    if (!escape)
                        theString += c;
                    else {
                        // Make \n work
                        if (c == 'n')
                            theString += '\n';
                    }
                }
        
                // Booleans, ints, etc
                else if (kv && !startString && !inStringKV && c != ',') {
                    theString += c;
                }
        
                // SubObjects
                if (!inString && c == '{') {
                    inObjectKV = true;
                    inString = false;
                    escape = false;
                    c = a[++pos];
                    while (c != '}' || inString) {
                        if (c == '\\') {
                            escape = !escape;
                        }
                        if (c == '\"' && !escape) {
                            inString = !inString;
                        }
                        if (c != '\\') {
                            escape = false;
                        }
                        theString += c;
                        c = a[++pos];
                    }
                    theString += c;
                    sub = read(theString);
                }
        
                // Key vs Value parsing
                if (!inString && c == ':') {
                    theString = "";
                    if (!kv)
                        kv = true;
                    else
                        throw new JSONFormatException("Unexpected: : at " + pos + " - Should be ,");
                }
                if (!inString && c == ',') {
                    if (inObjectKV)
                        tcn.set(key, sub);
                    else
                        tcn.set(key, theString);
                    inObjectKV = false;
                    inStringKV = false;
                    if (kv)
                        kv = false;
                    else
                        throw new JSONFormatException("Unexpected: : at " + pos + " - Should be ,");
                }
        
                if (c != '\\') {
                    escape = false;
                }
        
                c = a[++pos];
            }
            if (inObjectKV)
                tcn.set(key, sub);
            else
                tcn.set(key, theString);
    
            return tcn;
        } catch (JSONFormatException e) {
            throw e;
        } catch (Throwable e) {
            throw new JSONFormatException("At " + pos + " in " + string, e);
        }
    }
    
    private static String indent(boolean b, int i, int il) {
        if(b)
            return StringTools.multiply(StringTools.multiply(" ", il), i);
        else
            return "";
    }
    
    public static String write(TCN tcn) {
        return write(tcn, false, false, 0);
    }
    
    public static String write(TCN tcn, boolean spaces) {
        return write(tcn, true, spaces, 2);
    }
    
    public static String write(TCN tcn, int indent) {
        return write(tcn, true, false, indent);
    }
    
    public static String writeReadable(TCN tcn) {
        return write(tcn, true, true, 2);
    }
    
    public static String writeReadable(TCN tcn, int indent) {
        return write(tcn, true, true, indent);
    }
    
    public static String write(TCN tcn, boolean newlines, boolean spaces, int indentLength) {
    
        StringBuilder s = new StringBuilder();
        s.append("{").append(newlines ? "\n" : "");
        int i = 1;
    
        ArrayList<Stack<String>> paths = new ArrayList<>();
        Stack<TCN> tcnStack = new Stack<>();
        Stack<String> path = new Stack<>();
        tcnStack.add(tcn);
        path.add("");
        while (tcnStack.size() > 0) {
            boolean b = false;
            for(String key : tcnStack.peek().map.keys()) {
                Object o = tcnStack.peek().map.get(key);
            
                if(o == null)
                    continue;
            
                String k = key.replaceAll("\\\\", "\\\\").replaceAll("\n", "\\n").replaceAll("\"", "\\\"");
                if(o.getClass() == TCN.class) {
                    path.add(key);
                    if(!paths.contains(path)) {
                        paths.add(path.clone());
                        tcnStack.add((TCN) o);
                        s.append(indent(newlines, i, indentLength)).append("\"").append(k).append("\":").append(spaces ? " " : "").append("{").append(newlines ? "\n" : "");
                        b = true;
                        i++;
                        break;
                    } else
                        path.next();
                } else {
                    path.add(key);
                    if(!paths.contains(path)) {
                        paths.add(path.clone());
                        s.append(indent(newlines, i, indentLength)).append("\"").append(k).append("\":").append(spaces ? " \"" : "\"").append(o.toString().replaceAll("\\\\", "\\\\").replaceAll("\n", "\\n").replaceAll("\"", "\\\"")).append("\",").append(spaces ? " " : "").append(newlines ? "\n" : "");
                        b = true;
                    }
                    path.next();
                }
            }
            if(!b) {
                tcnStack.next();
                path.next();
                i--;
                s.delete(s.length() - ((newlines ? 2 : 1) + (spaces ? 1 : 0)), s.length());
                s.append(newlines ? "\n" : "").append(indent(newlines, i, indentLength)).append("},").append(spaces ? " " : "").append(newlines ? "\n" : "");
            }
        }
    
        s.delete(s.length() - ((newlines ? 2 : 1) + (spaces ? 1 : 0)), s.length());
        return s.toString();
    }
    
    public static class JSONFormatException extends Exception {
        public JSONFormatException(String s, Throwable e) {
            super(s, e);
        }
        public JSONFormatException(String s) {
            super(s);
        }
    }
}
