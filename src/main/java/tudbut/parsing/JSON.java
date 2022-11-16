package tudbut.parsing;

import tudbut.tools.Stack;
import tudbut.tools.StringTools;

import java.util.ArrayList;

/**
 * Interconverting JSON and TCN
 */
public class JSON {
    
    /**
     * Converts a JSON string to a TCN object
     * @param string The JSON string, supports most compact and readable formats
     * @return The parsed TCN object
     * @throws JSONFormatException If a format error is found
     */
    public static TCN read(String string) throws JSONFormatException {
        while (string.startsWith(" ")) {
            string = string.substring(1);
        }
        if(!string.startsWith("{") && !string.startsWith("[")) {
            throw new JSONFormatException("Expected: { or [ at 0 (String is '" + string + "')");
        }
        boolean array = string.startsWith("[");
        TCN tcn = new TCN("JSON", array);
        boolean escape = false;
        int pos = 1;
        char[] a = string.toCharArray();
        char c = a[pos];
        int arrayPos = 0;
        boolean inString = false;
        boolean startString = false;
        StringBuilder theString = new StringBuilder();
        boolean kv = false;
        String key = "";
        boolean inStringKV = false;
        boolean inObjectKV = false;
        TCN sub = null;
        
        try {
            while (inString || (c != '}' && c != ']')) {
                if(array) {
                    kv = true;
                }
                
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
                        theString = new StringBuilder();
                    }
                    else {
                        inString = false;
                        if (!kv) { // Key
                            key = theString.toString();
                        }
                    }
                }
                if (inString) {
                    if (!escape)
                        theString.append(c);
                    else {
                        a:
                        {
                            // Make escapes work
                            if (c == 'n')
                                theString.append('\n');
                            if (c == 'r')
                                theString.append('\r');
                            if (c == 'u') {
                                String e = "";
                                e += c = a[++pos];
                                e += c = a[++pos];
                                e += c = a[++pos];
                                e += c = a[++pos];
                                theString.append((char) Integer.parseInt(e, 16));
                                break a;
                            }
                            if (c == '0') {
                                String e = "";
                                e += c = a[pos];
                                e += c = a[++pos];
                                e += c = a[++pos];
                                theString.append((char) Integer.parseInt(e, 8));
                                break a;
                            }
                            if (c == '1') {
                                String e = "";
                                e += c = a[pos];
                                e += c = a[++pos];
                                e += c = a[++pos];
                                theString.append((char) Integer.parseInt(e, 8));
                                break a;
                            }
                            if (c == '2') {
                                String e = "";
                                e += c = a[pos];
                                e += c = a[++pos];
                                e += c = a[++pos];
                                theString.append((char) Integer.parseInt(e, 8));
                                break a;
                            }
                            if (c == '3') {
                                String e = "";
                                e += c = a[pos];
                                e += c = a[++pos];
                                e += c = a[++pos];
                                theString.append((char) Integer.parseInt(e, 8));
                                break a;
                            }
                            if (c == 'x') {
                                String e = "";
                                e += c = a[++pos];
                                e += c = a[++pos];
                                theString.append((char) Integer.parseInt(e, 16));
                                break a;
                            }
                            if (c == '"') {
                                theString.append("\"");
                            }
                        }
                    }
                }
        
                // Booleans, ints, etc
                else if (kv && !startString && !inStringKV && c != ',' && (Character.isLetterOrDigit(c) || c == '.' || c == '-')) {
                    theString.append(c);
                }
        
                // SubObjects
                if (!inString && c == '{') {
                    inObjectKV = true;
                    escape = false;
                    theString = new StringBuilder("{");
                    int layer = 1;
                    while (layer > 0) {
                        c = a[++pos];
                        theString.append(c);
                        if(c == '{' && !inString) {
                            layer++;
                        }
                        if(c == '}' && !inString) {
                            layer--;
                        }
                        
                        if (c == '\\') {
                            escape = !escape;
                        }
                        if (c == '\"' && !escape) {
                            inString = !inString;
                        }
                        if (c != '\\') {
                            escape = false;
                        }
                    }
                    theString.append("}");
                    sub = read(theString.toString());
                    theString = new StringBuilder();
                }
                // Arrays
                if (!inString && c == '[') {
                    inObjectKV = true;
                    escape = false;
                    theString = new StringBuilder("[");
                    int layer = 1;
                    while (layer != 0) {
                        c = a[++pos];
                        theString.append(c);
                        if(c == '[' && !inString) {
                            layer++;
                        }
                        if(c == ']' && !inString) {
                            layer--;
                        }
        
                        if (c == '\\') {
                            escape = !escape;
                        }
                        if (c == '\"' && !escape) {
                            inString = !inString;
                        }
                        if (c != '\\') {
                            escape = false;
                        }
                    }
                    theString.append(']');
                    sub = read(theString.toString());
                    theString = new StringBuilder();
                }
                
                
                if(array) {
                    kv = true;
                }
                // Key vs Value parsing
                if (!inString && c == ':') {
                    theString = new StringBuilder();
                    if (!kv)
                        kv = true;
                    else
                        throw new JSONFormatException("Unexpected: '" + c + "' at " + pos + " - Should be ','");
                }
                if (!inString && c == ',') {
                    if(array)
                        key = String.valueOf(arrayPos++);
    
                    if (inObjectKV) {
                        tcn.set(key, sub);
                    } else if(inStringKV || !theString.toString().equals("null")) {
                        tcn.set(key, theString.toString());
                    }
                    inObjectKV = false;
                    inStringKV = false;
                    theString = new StringBuilder();
                    if (kv)
                        kv = false;
                    else
                        throw new JSONFormatException("Unexpected: '" + c + "' at " + pos + " - Should be ':'");
                    
                }
        
                if (c != '\\') {
                    escape = false;
                }
        
                c = a[++pos];
            }
            if(kv) {
                if (array)
                    key = String.valueOf(arrayPos);
                if (inObjectKV)
                    tcn.set(key, sub);
                else if(inStringKV || !theString.toString().equals("null"))
                    tcn.set(key, theString.toString());
            }
            
            
            for (String theKey : tcn.map.keys()) {
                TCN.deepConvert(theKey, tcn.get(theKey), tcn);
            }
            return tcn;
        } catch (JSONFormatException e) {
            throw e;
        } catch (Throwable e) {
            throw new JSONFormatException("At " + pos + " in " + string + " (Debug: " + inString + " " + kv + " " + theString + " " + key + " " + array + ")", e);
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
     * Converts a TCN object to a JSON string
     * @param tcn The TCN to write to JSON
     * @param spaces If the JSON string should have spaces after : and ,
     * @return The JSON string
     */
    public static String write(TCN tcn, boolean spaces) {
        return write(tcn, false, spaces, 0);
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
     * Converts a TCN object to a JSON string, uses newlines with an indent of 2 and spaces
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
        s.append(tcn.isArray ? "[" : "{").append(newlines ? "\n" : "");
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
            
                String k = key.replaceAll("\\\\", "\\\\\\\\").replaceAll("\n", "\\\\n").replaceAll("\"", "\\\\\"");
                if(o.getClass() == TCN.class) {
                    path.add(key);
                    if(!paths.contains(path)) {
                        paths.add(path.clone());
                        TCN theTCN = tcnStack.peek();
                        tcnStack.add((TCN) o);
                        if(theTCN.isArray) {
                            s.append(indent(newlines, i, indentLength)).append("{").append(newlines ? "\n" : "");
                        }
                        else
                            s.append(indent(newlines, i, indentLength)).append("\"").append(k).append("\":").append(spaces ? " " : "").append("{").append(newlines ? "\n" : "");
                        b = true;
                        i++;
                    } else
                        path.next();
                } else if(o.getClass() == TCNArray.class) {
                    path.add(key);
                    if(!paths.contains(path)) {
                        paths.add(path.clone());
                        TCN theTCN = tcnStack.peek();
                        tcnStack.add(((TCNArray) o).toTCN());
                        if(theTCN.isArray) {
                            s.append(indent(newlines, i, indentLength)).append("[").append(newlines ? "\n" : "");
                        }
                        else
                            s.append(indent(newlines, i, indentLength)).append("\"").append(k).append("\":").append(spaces ? " " : "").append("[").append(newlines ? "\n" : "");
                        i++;
                        b = true;
                    } else
                        path.next();
                } else if (o instanceof String) {
                    path.add(key);
                    if(!paths.contains(path)) {
                        paths.add(path.clone());
                        String val = o.toString().replaceAll("\\\\", "\\\\\\\\").replaceAll("\n", "\\\\n").replaceAll("\r", "\\\\r").replaceAll("\"", "\\\\\"");
                        if(tcnStack.peek().isArray) {
                            s.append(indent(newlines, i, indentLength)).append("\"").append(val).append("\",").append(spaces ? " " : "").append(newlines ? "\n" : "");
                        }
                        else
                            s.append(indent(newlines, i, indentLength)).append("\"").append(k).append("\":").append(spaces ? " \"" : "\"").append(val).append("\",").append(spaces ? " " : "").append(newlines ? "\n" : "");
                        b = true;
                    }
                    path.next();
                }
                else {
                    path.add(key);
                    if(!paths.contains(path)) {
                        paths.add(path.clone());
                        String val = o.toString();
                        if(tcnStack.peek().isArray) {
                            s.append(indent(newlines, i, indentLength)).append(val).append(",").append(spaces ? " " : "").append(newlines ? "\n" : "");
                        }
                        else
                            s.append(indent(newlines, i, indentLength)).append("\"").append(k).append("\":").append(spaces ? " " : "").append(val).append(",").append(spaces ? " " : "").append(newlines ? "\n" : "");
                        b = true;
                    }
                    path.next();
                }
            }
            if(!b) {
                TCN theTCN = tcnStack.next();
                path.next();
                i--;
                if(theTCN.map.keys().isEmpty()) {
                    s.append(",").append(spaces ? " " : "");
                }
                s.delete(s.length() - ((newlines ? 2 : 1) + (spaces ? 1 : 0)), s.length());
                s.append(newlines ? "\n" : "").append(indent(newlines, i, indentLength)).append(theTCN.isArray ? "]" : "}").append(",").append(spaces ? " " : "").append(newlines ? "\n" : "");
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
