package tudbut.parsing;

import de.tudbut.async.Task;
import tudbut.tools.Stack;
import tudbut.tools.StringTools;

import java.util.ArrayList;

/**
 * Interconverting JSON and TCN
 */
public class AsyncJSON {
    
    /**
     * Converts a JSON string to a TCN object
     * @param str The JSON string, supports most compact and readable formats
     * @return The parsed TCN object
     */
    public static Task<TCN> read(String str) {
        return new Task<>((gres, grej) -> {
            String string = str;
    
            while (string.startsWith(" ")) {
                string = string.substring(1);
            }
            if (!string.startsWith("{") && !string.startsWith("[")) {
                grej.call(new JSON.JSONFormatException("Expected: { or [ at 0 (String is '" + string + "')"));
                return;
            }
            boolean array = string.startsWith("[");
            TCN tcn = new TCN("AJSON", array);
            final boolean[] escape = { false };
            final int[] pos = { 1 };
            char[] a = string.toCharArray();
            final char[] c = { a[pos[0]] };
            final int[] arrayPos = { 0 };
            final boolean[] inString = { false };
            final boolean[] startString = { false };
            final StringBuilder[] theString = { new StringBuilder() };
            final boolean[] kv = { false };
            final String[] key = { "" };
            final boolean[] inStringKV = { false };
            final boolean[] inObjectKV = { false };
            final TCN[] sub = { null };
    
            try {
                while (inString[0] || (c[0] != '}' && c[0] != ']')) {
                    new Task<Void>((res, rej) -> {
                        if (array) {
                            kv[0] = true;
                        }
                
                        if (startString[0]) {
                            inString[0] = true;
                            startString[0] = false;
                        }
                        if (c[0] == '\\') {
                            escape[0] = !escape[0];
                        }
                        if (!escape[0] && c[0] == '"') {
                            startString[0] = !inString[0];
                            if (startString[0]) {
                                if (kv[0])
                                    inStringKV[0] = true;
                                theString[0] = new StringBuilder();
                            }
                            else {
                                inString[0] = false;
                                if (!kv[0]) { // Key
                                    key[0] = theString[0].toString();
                                }
                            }
                        }
                        if (inString[0]) {
                            if (!escape[0])
                                theString[0].append(c[0]);
                            else {
                                a:
                                {
                                    // Make escapes work
                                    if (c[0] == 'n')
                                        theString[0].append('\n');
                                    if (c[0] == 'r')
                                        theString[0].append('\r');
                                    if (c[0] == 'u') {
                                        String e = "";
                                        e += c[0] = a[++pos[0]];
                                        e += c[0] = a[++pos[0]];
                                        e += c[0] = a[++pos[0]];
                                        e += c[0] = a[++pos[0]];
                                        theString[0].append((char) Integer.parseInt(e, 16));
                                        break a;
                                    }
                                    if (c[0] == '0') {
                                        String e = "";
                                        e += c[0] = a[pos[0]];
                                        e += c[0] = a[++pos[0]];
                                        e += c[0] = a[++pos[0]];
                                        theString[0].append((char) Integer.parseInt(e, 8));
                                        break a;
                                    }
                                    if (c[0] == '1') {
                                        String e = "";
                                        e += c[0] = a[pos[0]];
                                        e += c[0] = a[++pos[0]];
                                        e += c[0] = a[++pos[0]];
                                        theString[0].append((char) Integer.parseInt(e, 8));
                                        break a;
                                    }
                                    if (c[0] == '2') {
                                        String e = "";
                                        e += c[0] = a[pos[0]];
                                        e += c[0] = a[++pos[0]];
                                        e += c[0] = a[++pos[0]];
                                        theString[0].append((char) Integer.parseInt(e, 8));
                                        break a;
                                    }
                                    if (c[0] == '3') {
                                        String e = "";
                                        e += c[0] = a[pos[0]];
                                        e += c[0] = a[++pos[0]];
                                        e += c[0] = a[++pos[0]];
                                        theString[0].append((char) Integer.parseInt(e, 8));
                                        break a;
                                    }
                                    if (c[0] == 'x') {
                                        String e = "";
                                        e += c[0] = a[++pos[0]];
                                        e += c[0] = a[++pos[0]];
                                        theString[0].append((char) Integer.parseInt(e, 16));
                                        break a;
                                    }
                                    if (c[0] == '"') {
                                        theString[0].append("\"");
                                    }
                                }
                            }
                        }
                
                        // Booleans, ints, etc
                        else if (kv[0] && !startString[0] && !inStringKV[0] && c[0] != ',' && (Character.isLetterOrDigit(c[0]) || c[0] == '.' || c[0] == '-')) {
                            theString[0].append(c[0]);
                        }
                
                        // SubObjects
                        if (!inString[0] && c[0] == '{') {
                            inObjectKV[0] = true;
                            escape[0] = false;
                            theString[0] = new StringBuilder("{");
                            int layer = 1;
                            while (layer > 0) {
                                c[0] = a[++pos[0]];
                                theString[0].append(c[0]);
                                if (c[0] == '{' && !inString[0]) {
                                    layer++;
                                }
                                if (c[0] == '}' && !inString[0]) {
                                    layer--;
                                }
                        
                                if (c[0] == '\\') {
                                    escape[0] = !escape[0];
                                }
                                if (c[0] == '\"' && !escape[0]) {
                                    inString[0] = !inString[0];
                                }
                                if (c[0] != '\\') {
                                    escape[0] = false;
                                }
                            }
                            theString[0].append("}");
                            sub[0] = read(theString[0].toString()).err(rej).ok().await();
                            theString[0] = new StringBuilder();
                        }
                        // Arrays
                        if (!inString[0] && c[0] == '[') {
                            inObjectKV[0] = true;
                            escape[0] = false;
                            theString[0] = new StringBuilder("[");
                            int layer = 1;
                            while (layer != 0) {
                                c[0] = a[++pos[0]];
                                theString[0].append(c[0]);
                                if (c[0] == '[' && !inString[0]) {
                                    layer++;
                                }
                                if (c[0] == ']' && !inString[0]) {
                                    layer--;
                                }
                        
                                if (c[0] == '\\') {
                                    escape[0] = !escape[0];
                                }
                                if (c[0] == '\"' && !escape[0]) {
                                    inString[0] = !inString[0];
                                }
                                if (c[0] != '\\') {
                                    escape[0] = false;
                                }
                            }
                            theString[0].append(']');
                            sub[0] = read(theString[0].toString()).err(rej).ok().await();
                            theString[0] = new StringBuilder();
                        }
                
                
                        if (array) {
                            kv[0] = true;
                        }
                        // Key vs Value parsing
                        if (!inString[0] && c[0] == ':') {
                            theString[0] = new StringBuilder();
                            if (!kv[0])
                                kv[0] = true;
                            else {
                                rej.call(new JSON.JSONFormatException("Unexpected: '" + c[0] + "' at " + pos[0] + " - Should be ','"));
                                return;
                            }
                        }
                        if (!inString[0] && c[0] == ',') {
                            if (array)
                                key[0] = String.valueOf(arrayPos[0]++);
                    
                            if (inObjectKV[0]) {
                                tcn.set(key[0], sub[0]);
                            }
                            else if (inStringKV[0] || !theString[0].toString().equals("null")) {
                                tcn.set(key[0], theString[0].toString());
                            }
                            inObjectKV[0] = false;
                            inStringKV[0] = false;
                            theString[0] = new StringBuilder();
                            if (kv[0])
                                kv[0] = false;
                            else {
                                rej.call(new JSON.JSONFormatException("Unexpected: '" + c[0] + "' at " + pos[0] + " - Should be ':'"));
                                return;
                            }
                    
                        }
                
                        if (c[0] != '\\') {
                            escape[0] = false;
                        }
                
                        c[0] = a[++pos[0]];
                        res.call(null);
                    }).err(grej).ok().await();
                }
                if (kv[0]) {
                    if (array)
                        key[0] = String.valueOf(arrayPos[0]);
                    if (inObjectKV[0])
                        tcn.set(key[0], sub[0]);
                    else if (inStringKV[0] || !theString[0].toString().equals("null"))
                        tcn.set(key[0], theString[0].toString());
                }
        
        
                for (String theKey : tcn.map.keys()) {
                    TCN.deepConvert(theKey, tcn.get(theKey), tcn);
                }
            }
            catch (Throwable e) {
                grej.call(new JSON.JSONFormatException("At " + pos[0] + " in " + string + " (Debug: " + inString[0] + " " + kv[0] + " " + theString[0] + " " + key[0] + " " + array + ")", e));
            }
            gres.call(tcn);
        });
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
    public static Task<String> write(TCN tcn) {
        return write(tcn, false, false, 0);
    }
    
    /**
     * Converts a TCN object to a JSON string
     * @param tcn The TCN to write to JSON
     * @param spaces If the JSON string should have spaces after : and ,
     * @return The JSON string
     */
    public static Task<String> write(TCN tcn, boolean spaces) {
        return write(tcn, false, spaces, 0);
    }
    
    /**
     * Converts a TCN object to a JSON string, uses newlines with selected indent and without spaces
     * @param tcn The TCN to write to JSON
     * @param indent The indent to use
     * @return The JSON string
     */
    public static Task<String> write(TCN tcn, int indent) {
        return write(tcn, true, false, indent);
    }
    
    /**
     * Converts a TCN object to a JSON string, uses newlines with an indent of 2 and spaces
     * @param tcn The TCN to write to JSON
     * @return The JSON string
     */
    public static Task<String> writeReadable(TCN tcn) {
        return write(tcn, true, true, 2);
    }
    
    /**
     * Converts a TCN object to a JSON string, uses newlines with selected indent and spaces
     * @param tcn The TCN to write to JSON
     * @param indent The indent to use
     * @return The JSON string
     */
    public static Task<String> writeReadable(TCN tcn, int indent) {
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
    public static Task<String> write(TCN tcn, boolean newlines, boolean spaces, int indentLength) {
        return new Task<>((gres, grej) -> {
            StringBuilder s = new StringBuilder();
            s.append(tcn.isArray ? "[" : "{").append(newlines ? "\n" : "");
            final int[] i = { 1 };
    
            ArrayList<Stack<String>> paths = new ArrayList<>();
            Stack<TCN> tcnStack = new Stack<>();
            Stack<String> path = new Stack<>();
            tcnStack.add(tcn);
            path.add("");
            while (tcnStack.size() > 0) {
                final boolean[] b = { false };
                for (String key : tcnStack.peek().map.keys()) {
                    new Task<Void>((res, rej) -> {
                        Object o = tcnStack.peek().map.get(key);
    
                        if (o == null) {
                            res.call(null);
                            return;
                        }
    
                        String k = key.replaceAll("\\\\", "\\\\\\\\").replaceAll("\n", "\\\\n").replaceAll("\"", "\\\\\"");
                        if (o.getClass() == TCN.class) {
                            path.add(key);
                            if (!paths.contains(path)) {
                                paths.add(path.clone());
                                TCN theTCN = tcnStack.peek();
                                tcnStack.add((TCN) o);
                                if (theTCN.isArray) {
                                    s.append(indent(newlines, i[0], indentLength)).append("{").append(newlines ? "\n" : "");
                                }
                                else
                                    s.append(indent(newlines, i[0], indentLength)).append("\"").append(k).append("\":").append(spaces ? " " : "").append("{").append(newlines ? "\n" : "");
                                b[0] = true;
                                i[0]++;
                            }
                            else
                                path.next();
                        }
                        else if (o.getClass() == TCNArray.class) {
                            path.add(key);
                            if (!paths.contains(path)) {
                                paths.add(path.clone());
                                TCN theTCN = tcnStack.peek();
                                tcnStack.add(((TCNArray) o).toTCN());
                                if (theTCN.isArray) {
                                    s.append(indent(newlines, i[0], indentLength)).append("[").append(newlines ? "\n" : "");
                                }
                                else
                                    s.append(indent(newlines, i[0], indentLength)).append("\"").append(k).append("\":").append(spaces ? " " : "").append("[").append(newlines ? "\n" : "");
                                i[0]++;
                                b[0] = true;
                            }
                            else
                                path.next();
                        }
                        else if (o instanceof String) {
                            path.add(key);
                            if (!paths.contains(path)) {
                                paths.add(path.clone());
                                String val = o.toString().replaceAll("\\\\", "\\\\\\\\").replaceAll("\n", "\\\\n").replaceAll("\r", "\\\\r").replaceAll("\"", "\\\\\"");
                                if (tcnStack.peek().isArray) {
                                    s.append(indent(newlines, i[0], indentLength)).append("\"").append(val).append("\",").append(spaces ? " " : "").append(newlines ? "\n" : "");
                                }
                                else
                                    s.append(indent(newlines, i[0], indentLength)).append("\"").append(k).append("\":").append(spaces ? " \"" : "\"").append(val).append("\",").append(spaces ? " " : "").append(newlines ? "\n" : "");
                                b[0] = true;
                            }
                            path.next();
                        }
                        else {
                            path.add(key);
                            if (!paths.contains(path)) {
                                paths.add(path.clone());
                                String val = o.toString();
                                if (tcnStack.peek().isArray) {
                                    s.append(indent(newlines, i[0], indentLength)).append(val).append(",").append(spaces ? " " : "").append(newlines ? "\n" : "");
                                }
                                else
                                    s.append(indent(newlines, i[0], indentLength)).append("\"").append(k).append("\":").append(spaces ? " " : "").append(val).append(",").append(spaces ? " " : "").append(newlines ? "\n" : "");
                                b[0] = true;
                            }
                            path.next();
                        }
                        res.call(null);
                    }).err(grej).ok().await();
                }
                if (!b[0]) {
                    TCN theTCN = tcnStack.next();
                    path.next();
                    i[0]--;
                    if (theTCN.map.keys().isEmpty())
                        s.append(",").append(spaces ? " " : "");
                    s.delete(s.length() - ((newlines ? 2 : 1) + (spaces ? 1 : 0)), s.length());
                    s.append(newlines ? "\n" : "").append(indent(newlines, i[0], indentLength)).append(theTCN.isArray ? "]" : "}").append(",").append(spaces ? " " : "").append(newlines ? "\n" : "");
                }
            }
            s.delete(s.length() - ((newlines ? 2 : 1) + (spaces ? 1 : 0)), s.length());
            gres.call(s.toString());
        });
    }
    
}
