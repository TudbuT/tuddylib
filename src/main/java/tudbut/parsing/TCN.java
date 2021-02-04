package tudbut.parsing;


import tudbut.tools.Stack;
import tudbut.tools.StringTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * T udbuT
 * C onfig
 * N otation
 */
public class TCN {
    
    public Map<String, Object> map = new HashMap<>();
    
    private TCN() { }
    
    public void set(String key, Object o) {
        map.put(key, o);
    }
    
    public String getString(String key) {
        Object o = map.get(key);
        if(o != null && o.getClass() == String.class)
            return (String) map.get(key);
        else
            return null;
    }
    
    public Short getShort(String key) {
        if(map.get(key).getClass() == String.class)
            return Short.valueOf((String) map.get(key));
        else
            return null;
    }
    
    public Integer getInteger(String key) {
        Object o = map.get(key);
        if(o != null && o.getClass() == String.class)
            return Integer.valueOf((String) map.get(key));
        else
            return null;
    }
    
    public Boolean getBoolean(String key) {
        Object o = map.get(key);
        if(o != null && o.getClass() == String.class)
            return Boolean.valueOf((String) map.get(key));
        else
            return null;
    }
    
    public Float getFloat(String key) {
        Object o = map.get(key);
        if(o != null && o.getClass() == String.class)
            return Float.valueOf((String) map.get(key));
        else
            return null;
    }
    
    public Double getDouble(String key) {
        Object o = map.get(key);
        if(o != null && o.getClass() == String.class)
            return Double.valueOf((String) map.get(key));
        else
            return null;
    }
    
    public TCN getSub(String key) {
        Object o = map.get(key);
        if(o != null && o.getClass() == TCN.class)
            return (TCN) map.get(key);
        else
            return null;
    }
    
    public Object get(String key) {
        if(map.get(key) != null)
            return map.get(key);
        else
            return null;
    }
    
    public String toString() {
        StringBuilder s = new StringBuilder();
        int i = 0;
        
        HashMap<Stack<String>, String> fullMap = new HashMap<>();
        ArrayList<Stack<String>> paths = new ArrayList<>();
        Stack<TCN> tcnStack = new Stack<>();
        Stack<String> path = new Stack<>();
        tcnStack.add(this);
        path.add("");
        while (tcnStack.size() > 0) {
            boolean b = false;
            for(String key : tcnStack.peek().map.keySet()) {
                Object o = tcnStack.peek().map.get(key);
                if(o.getClass() == TCN.class) {
                    path.add(key);
                    if(!paths.contains(path)) {
                        paths.add(path.clone());
                        tcnStack.add((TCN) o);
                        String indent = StringTools.multiply("    ", i);
                        s.append("\n").append(indent).append(key).append(" {\n");
                        i++;
                        b = true;
                        break;
                    } else
                        path.next();
                } else {
                    path.add(key);
                    if(!paths.contains(path)) {
                        paths.add(path.clone());
                        String indent = StringTools.multiply("    ", i);
                        s.append(indent).append(key).append(": ").append(o.toString().replaceAll("\n", "\\\\n ").replaceAll("\\\\\\n", "\\\\n")).append("\n");
                        b = true;
                    }
                    path.next();
                }
            }
            if(!b) {
                tcnStack.next();
                path.next();
                i--;
                String indent = StringTools.multiply("    ", i);
                s.append(indent).append("}\n\n");
            }
        }
        s.setLength(s.length() - "\n#\n\n".length());
        s.trimToSize();
        return s.toString();
    }
    
    public static TCN read(String s) throws TCNException {
        TCN tcn = new TCN();
        
        Map<Stack<String>, String> scanned = deepScan(s);
        Set<Stack<String>> keys = scanned.keySet();
        for (Stack<String> path : keys) {
            deepPut(path.clone(), tcn, scanned.get(path));
        }
        
        return tcn;
    }
    
    private static void deepPut(Stack<String> path, TCN tcn, String value) throws TCNException {
        try {
            if (path.size() == 1) {
                tcn.map.put(path.next(), value);
            }
            else {
                TCN toPut = (TCN) tcn.map.getOrDefault(path.getBottom(), new TCN());
                tcn.map.put(path.popBottom(), toPut);
                deepPut(path, toPut, value);
            }
        } catch (Exception e) {
            throw new TCNException(null, pathToString(path) + ": " + value, e);
        }
    }
    
    private static Map<Stack<String>, String> deepScan(String s) throws TCNException {
        String[] lines = s.split("\n");
        Map<Stack<String>, String> map = new HashMap<>();
    
        Stack<String> path = new Stack<>();
        for (int i = 0; i < lines.length; i++) {
            try {
                String line = removePrefixSpaces(lines[i]);
                if (!line.isEmpty()) {
                    if (line.equals("}")) {
                        path.next();
                    }
                    else if (line.endsWith(" {")) {
                        path.add(line.split(" \\{")[0]);
                    }
                    else {
                        Stack<String> p = path.clone();
                        p.add(line.split(": ")[0]);
                        map.put(p, line.substring(line.split(": ")[0].length() + 2).replaceAll("\\\\n ", "\n"));
                    }
                }
            } catch (Exception e) {
                throw new TCNException(i, lines[i], e);
            }
        }
        
        return map;
    }
    
    private static String pathToString(Stack<String> path) {
        StringBuilder s = new StringBuilder("/");
        path = path.clone();
        while (path.hasNext()) {
            s.append(path.popBottom()).append("/");
        }
        return s.toString();
    }
    
    private static String removePrefixSpaces(String s) {
        while (s.startsWith(" "))
            s = s.substring(1);
        return s;
    }
    
    public static TCN getEmpty() {
        return new TCN();
    }
    
    public static class TCNException extends Exception {
        
        public TCNException(Integer line, String lineString, Exception e) {
            super("Error in line " + line + " (" + lineString + ")", e);
        }
    }
}
