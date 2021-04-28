package tudbut.parsing;

import tudbut.tools.Stack;
import tudbut.tools.StringTools;

import java.util.ArrayList;

/**
 * Interconverting JSON & TCN
 */
public class JSON {
    
    /**
     * Converts a JSON string to a TCN object
     * @param string The JSON string, supports most readable formats
     * @return The parsed TCN object
     * @throws JSONFormatException If a format error is found
     */
    public static TCN read(String string) throws JSONFormatException {
        while (string.startsWith(" ")) {
            string = string.substring(1);
        }
        if(!string.startsWith("{")) {
            throw new JSONFormatException("Expected: { at 0 (String is '" + string + "')");
        }
        TCN tcn = new TCN();
        boolean escape = false;
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
    
    /**
     * Converts a TCN object to a JSON string, uses the most compact form
     * @param tcn The TCN to write to JSON
     * @return The JSON string
     */
    public static String write(TCN tcn) {
        return write(tcn, false, false, 0);
    }
    
    /**
     * Converts a TCN object to a JSON string, uses newlines and a indent of 2
     * @param tcn The TCN to write to JSON
     * @param spaces If the JSON string should have spaces after : and ,
     * @return The JSON string
     */
    public static String write(TCN tcn, boolean spaces) {
        return write(tcn, true, spaces, 2);
    }
    
    /**
     * Converts a TCN object to a JSON string, uses newlines with selected indent and without spaces
     * @param tcn The TCN to write to JSON
     * @param indent The indent to use
     * @return The JSON string
     */
    public static String write(TCN tcn, int indent) {
        return write(tcn, true, false, indent);
    }
    
    /**
     * Converts a TCN object to a JSON string, uses newlines with a indent of 2 and spaces
     * @param tcn The TCN to write to JSON
     * @return The JSON string
     */
    public static String writeReadable(TCN tcn) {
        return write(tcn, true, true, 2);
    }
    
    /**
     * Converts a TCN object to a JSON string, uses newlines with selected indent and spaces
     * @param tcn The TCN to write to JSON
     * @param indent The indent to use
     * @return The JSON string
     */
    public static String writeReadable(TCN tcn, int indent) {
        return write(tcn, true, true, indent);
    }
    
    /**
     * Converts a TCN object to a JSON string
     * @param tcn The TCN to write to JSON
     * @param newlines If the JSON string should have newlines and indents
     * @param spaces If the JSON string should have spaces after : and ,
     * @param indentLength The indent to use
     * @return The JSON string
     */
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
            
                String k = key.replaceAll("\\\\", "\\\\\\\\").replaceAll("\n", "\\\\n").replaceAll("\"", "\\\"");
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
                        s.append(indent(newlines, i, indentLength)).append("\"").append(k).append("\":").append(spaces ? " \"" : "\"").append(o.toString().replaceAll("\\\\", "\\\\\\\\").replaceAll("\n", "\\\\n").replaceAll("\"", "\\\"")).append("\",").append(spaces ? " " : "").append(newlines ? "\n" : "");
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
