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
        if(!string.startsWith("{")) {
            throw new JSONFormatException("Expected: { at 0");
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
    
    public static String write(TCN tcn) {
    
        StringBuilder s = new StringBuilder();
        s.append("{");
    
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
                        s.append("\"").append(k).append("\":").append("{");
                        b = true;
                        break;
                    } else
                        path.next();
                } else {
                    path.add(key);
                    if(!paths.contains(path)) {
                        paths.add(path.clone());
                        s.append("\"").append(k).append("\":\"").append(o.toString().replaceAll("\\\\", "\\\\").replaceAll("\n", "\\n").replaceAll("\"", "\\\"")).append("\",");
                        b = true;
                    }
                    path.next();
                }
            }
            if(!b) {
                tcnStack.next();
                path.next();
                s.deleteCharAt(s.length() - 1);
                s.append("},");
            }
        }
    
        s.deleteCharAt(s.length() - 1);
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
