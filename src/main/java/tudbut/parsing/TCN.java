package tudbut.parsing;


import de.tudbut.tools.Tools;
import tudbut.obj.DoubleTypedObject;
import tudbut.obj.TLMap;
import tudbut.tools.Stack;
import tudbut.tools.StringTools;

import java.util.*;

/**
 * T udbuT<br>
 * C onfig<br>
 * N otation<br>
 */
public class TCN {
    
    /**
     * The map
     */
    public TLMap<String, Object> map = new TLMap<>();
    public final boolean isArray;
    public String type;
    
    /**
     * Creates a new, empty TCN
     */
    public TCN() {
        isArray = false;
        this.type = "TCN";
    }
    
    TCN(boolean array) {
        isArray = array;
        this.type = "TCN";
    }

    public TCN(String type) {
        isArray = false;
        this.type = type;
    }

    TCN(String type, boolean array) {
        isArray = array;
        this.type = type;
    }


    /**
     * Sets something in the map
     * @param key Key
     * @param o Object, can be a native type, string, or another TCN (or TCNArray)
     */
    public void set(String key, Object o) {
        /*TLMap<String, Object> map = this.map;
        ArrayList<String> path = new ArrayList<>(Arrays.asList(key.split("#")));
    
        while (path.size() > 1) {
            map = ((TCN) map.get(path.remove(0))).map;
        }
         */
        map.set(key, o);
    }
    
    public String getString(String key) {
        Object o = map.get(key);
        if(o != null)
            return get(key).toString();
        else
            return null;
    }
    
    public Short getShort(String key) {
        Object o = get(key);
        if(o != null)
            return Short.valueOf(String.valueOf(o));
        else
            return null;
    }
    
    public Integer getInteger(String key) {
        Object o = get(key);
        if(o != null)
            return Integer.valueOf(String.valueOf(o));
        else
            return null;
    }
    
    public Boolean getBoolean(String key) {
        Object o = get(key);
        if(o != null)
            return Boolean.valueOf(String.valueOf(o));
        else
            return null;
    }
    
    public Float getFloat(String key) {
        Object o = get(key);
        if(o != null)
            return Float.valueOf(String.valueOf(o));
        else
            return null;
    }
    
    public Long getLong(String key) {
        Object o = get(key);
        if(o != null)
            return Long.valueOf(String.valueOf(o));
        else
            return null;
    }
    
    public Double getDouble(String key) {
        Object o = get(key);
        if(o != null)
            return Double.valueOf(String.valueOf(o));
        else
            return null;
    }
    
    public TCN getSub(String key) {
        Object o = get(key);
        if(o != null && o.getClass() == TCN.class)
            return (TCN) map.get(key);
        else
            return null;
    }
    
    public TCNArray getArray(String key) {
        Object o = get(key);
        if(o != null && o.getClass() == TCNArray.class)
            return (TCNArray) map.get(key);
        else
            return null;
    }
    
    public Object get(String key) {
        TLMap<String, Object> map = this.map;
        ArrayList<String> path = new ArrayList<>(Collections.singletonList(key));
        
        while (path.size() > 1) {
            map = ((TCN) map.get(path.remove(0))).map;
        }
        
        return map.get(path.get(0));
    }
    
    /**
     * Converts a Map to a TCN
     * @param map The map to convert
     * @return The converted TCN
     */
    public static TCN readMap(Map<String, String> map) {
        TCN tcn = new TCN(map.containsKey("TCN%isArray") && Boolean.parseBoolean(map.get("TCN%isArray")));
    
        map.remove("TCN%isArray");
        
        String[] array = map.keySet().toArray(new String[0]);
        for (int i = 0, arrayLength = array.length; i < arrayLength; i++) {
            String key = array[i];
            String s = map.get(key);
            
            if(s.contains(":")) {
                tcn.map.set(key, TCN.readMap(Tools.stringToMap(s)));
            }
            else {
                tcn.map.set(key, s.replaceAll("%C", ":").replaceAll("%P", "%"));
            }
        }
        
        for (String key : tcn.map.keys()) {
            deepConvert(key, tcn.get(key), tcn);
        }
        
        return tcn;
    }
    
    /**
     * Converts this TCN object to a Map
     * {@link #readMap}
     * @return The converted Map
     */
    public Map<String, String> toMap() {
        Map<String, String> r = new LinkedHashMap<>();
        
        if(isArray)
            r.put("TCN%isArray", "true");
    
        String[] array = map.keys().toArray(new String[0]);
        for (int i = 0, arrayLength = array.length; i < arrayLength; i++) {
            String key = array[i];
            Object o = map.get(key);
            
            if(o == null)
                continue;
            
            if(o.getClass() == TCN.class) {
                r.put(key, Tools.mapToString(((TCN) o).toMap()));
            }
            else if(o.getClass() == TCNArray.class) {
                r.put(key, Tools.mapToString(((TCNArray) o).toMap()));
            }
            else
                r.put(key, o.toString().replaceAll("%", "%P").replaceAll(":", "%C"));
        }
        
        return r;
    }
    
    /**
     * Converts this TCN to a reversible string
     * @return The converted string
     */
    public String toString() {
        if(type.equalsIgnoreCase("TCN")) {
            StringBuilder s = new StringBuilder();
            int i = 0;

            ArrayList<Stack<String>> paths = new ArrayList<>();
            Stack<TCN> tcnStack = new Stack<>();
            Stack<String> path = new Stack<>();
            tcnStack.add(this);
            path.add("");
            while (tcnStack.size() > 0) {
                boolean b = false;
                for (String key : tcnStack.peek().map.keys()) {
                    Object o = tcnStack.peek().map.get(key);

                    if (o == null)
                        continue;

                    String k = key.replaceAll("%", "%P").replaceAll(":", "%C").replaceAll("\n", "%N");
                    if (k.startsWith(" ")) {
                        if (!k.equals(" ")) {
                            k = "%S" + k.substring(1);
                        } else
                            k = "%S";
                    }
                    if (k.startsWith("#")) {
                        if (!k.equals("#")) {
                            k = "%H" + k.substring(1);
                        } else
                            k = "%H";
                    }
                    if (o.getClass() == TCN.class) {
                        path.add(key);
                        if (!paths.contains(path)) {
                            paths.add(path.clone());
                            TCN tcn = tcnStack.peek();
                            tcnStack.add((TCN) o);
                            String indent = StringTools.multiply("    ", i);
                            if (tcn.isArray) {
                                s.append("\n").append(indent).append(";").append(((TCN) o).isArray ? " [\n" : " {\n");
                            } else
                                s.append("\n").append(indent).append(k).append(((TCN) o).isArray ? " [\n" : " {\n");
                            i++;
                            b = true;
                            break;
                        } else
                            path.next();
                    } else if (o.getClass() == TCNArray.class) {
                        path.add(key);
                        if (!paths.contains(path)) {
                            paths.add(path.clone());
                            TCN tcn = tcnStack.peek();
                            tcnStack.add(((TCNArray) o).toTCN());
                            String indent = StringTools.multiply("    ", i);
                            if (tcn.isArray) {
                                s.append("\n").append(indent).append(";").append(" [\n");
                            } else
                                s.append("\n").append(indent).append(k).append(" [\n");
                            i++;
                            b = true;
                            break;
                        } else
                            path.next();
                    } else {
                        path.add(key);
                        if (!paths.contains(path)) {
                            paths.add(path.clone());
                            String indent = StringTools.multiply("    ", i);
                            String val = o.toString().replaceAll("%", "%P").replaceAll("\n", "%N");
                            if (tcnStack.peek().isArray) {
                                s.append(indent).append("; ").append(val).append("\n");
                            } else
                                s.append(indent).append(k).append(": ").append(val).append("\n");
                            b = true;
                        }
                        path.next();
                    }
                }
                if (!b) {
                    TCN tcn = tcnStack.next();
                    path.next();
                    i--;
                    String indent = StringTools.multiply("    ", i);
                    s.append(indent).append(tcn.isArray ? "]\n\n" : "}\n\n");
                }
            }
            try {
                s.setLength(s.length() - "\n#\n\n".length());
                s.trimToSize();
            } catch (Exception ignored) {
            }

            return s.toString();
        }
        else if(type.equalsIgnoreCase("JSON")) {
            return JSON.write(this);
        }
        else if(type.equalsIgnoreCase("AJSON")) {
            return AsyncJSON.write(this).ok().await();
        }
        return "";
    }
    
    /**
     * Converts a string {@link #toString()} to a TCN object
     * @param s The string
     * @return The converted TCN
     * @throws TCNException If a format error occurs
     */
    public static TCN read(String s) throws TCNException {
        TCN tcn = new TCN();
    
        Map<DoubleTypedObject<Stack<String>, Stack<Integer>>, String> scanned = deepScan(s);
        Set<DoubleTypedObject<Stack<String>, Stack<Integer>>> keys = scanned.keySet();
        for (DoubleTypedObject<Stack<String>, Stack<Integer>> path : keys) {
            deepPut(path.clone(), tcn, scanned.get(path));
        }
        for (String key : tcn.map.keys()) {
            deepConvert(key, tcn.get(key), tcn);
        }
        
        return tcn;
    }
    
    /**
     * Converts stray arrays to TCNArray objects, recursive.<br>
     * <pre>Example:{@code
     *     for (String key : tcn.map.keys()) {
     *         deepConvert(key, tcn.get(key), tcn);
     *     }
     * }</pre>
     * @param key The key of o
     * @param o Any value in a TCN
     * @param top The TCN that o is embedded in
     */
    public static void deepConvert(String key, Object o, TCN top) {
        if(!(o instanceof TCN) && !(o instanceof TCNArray))
            return;
        TCN tcn;
        if(o instanceof TCN) {
            tcn = (TCN) o;
        }
        else
            tcn = ((TCNArray) o).toTCN();
        
        for (String theKey : tcn.map.keys()) {
            deepConvert(theKey, tcn.get(theKey), tcn);
        }
        
        if (tcn.isArray) {
            top.set(key, TCNArray.fromTCN(tcn));
        }
    }
    
    private static void deepPut(DoubleTypedObject<Stack<String>, Stack<Integer>> path, TCN tcn, String value) throws TCNException {
        try {
            if (path.o.size() == 1) {
                tcn.map.set(path.o.next(), value);
            }
            else {
                int arrayPos = path.t.popBottom();
                TCN toPut = (TCN) tcn.map.get(path.o.getBottom(), () -> new TCN(arrayPos != -1));
                tcn.map.set(path.o.popBottom(), toPut);
                deepPut(path, toPut, value);
            }
        } catch (Exception e) {
            throw new TCNException(null, pathToString(path.o) + ": " + value, e);
        }
    }
    
    private static Map<DoubleTypedObject<Stack<String>, Stack<Integer>>, String> deepScan(String s) throws TCNException {
        String[] lines = s.split("\n");
        Map<DoubleTypedObject<Stack<String>, Stack<Integer>>, String> map = new LinkedHashMap<>();
    
        Stack<Boolean> array = new Stack<>();
        array.add(false);
        Stack<Integer> arrayPos = new Stack<>();
        Stack<String> path = new Stack<>();
        for (int i = 0; i < lines.length; i++) {
            try {
                String line = removePrefixSpaces(lines[i]);
                if (!line.isEmpty() && !line.startsWith("#")) {
                    if (line.equals("}") || line.equals("]")) {
                        path.next();
                        array.next();
                        arrayPos.next();
                    }
                    else if (line.endsWith(" {") && !line.contains(": ")) {
                        String k = line.split(" \\{")[0].replaceAll("%C", ":").replaceAll("%N", "\n");
                        if (k.startsWith("%S")) {
                            if (!k.equals("%S"))
                                k = " " + k.substring(2);
                            else
                                k = " ";
                        }
                        if (k.startsWith("%H")) {
                            if (!k.equals("%H"))
                                k = "#" + k.substring(2);
                            else
                                k = "#";
                        }
                        if(!array.peek())
                            path.add(k.replaceAll("%P", "%"));
                        else {
                            path.add(arrayPos.peek() + "");
                            arrayPos.add(arrayPos.next() + 1);
                        }
                        array.add(false);
                        arrayPos.add(-1);
                    }
                    else if (line.endsWith(" [") && !line.contains(": ")) {
                        String k = line.split(" \\[")[0].replaceAll("%C", ":").replaceAll("%N", "\n");
                        if (k.startsWith("%S")) {
                            if (!k.equals("%S"))
                                k = " " + k.substring(2);
                            else
                                k = " ";
                        }
                        if (k.startsWith("%H")) {
                            if (!k.equals("%H"))
                                k = "#" + k.substring(2);
                            else
                                k = "#";
                        }
                        if(!array.peek())
                            path.add(k.replaceAll("%P", "%"));
                        else {
                            path.add(arrayPos.peek() + "");
                            arrayPos.add(arrayPos.next() + 1);
                        }
                        array.add(true);
                        arrayPos.add(0);
                    }
                    else {
                        Stack<String> p = path.clone();
                        if(array.peek()) {
                            p.add(String.valueOf(arrayPos.peek()));
                            if(line.equals(";"))
                                line = "; ";
                            map.put(new DoubleTypedObject<>(p, arrayPos.clone()), line.substring(2).replaceAll("%N", "\n").replaceAll("%P", "%"));
                            arrayPos.add(arrayPos.next() + 1);
                        }
                        else {
                            String rawk = line.split(": ")[0].replaceAll("%C", ":").replaceAll("%N", "\n");
                            String k = rawk;
                            if (k.startsWith("%S")) {
                                if (!k.equals("%S"))
                                    k = " " + k.substring(2);
                                else
                                    k = " ";
                            }
                            if (k.startsWith("%H")) {
                                if (!k.equals("%H"))
                                    k = "#" + k.substring(2);
                                else
                                    k = "#";
                            }
                            p.add(k.replaceAll("%P", "%"));
                            map.put(new DoubleTypedObject<>(p, arrayPos.clone()), line.substring(rawk.length() + 2).replaceAll("%N", "\n").replaceAll("%P", "%"));
                        }
                    }
                }
            } catch (Exception e) {
                throw new TCNException(i, lines[i], e);
            }
        }
        
        return map;
    }
    
    static String pathToString(Stack<String> path) {
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
    
    /**
     * Creates a new, empty TCN object
     * @deprecated Use {@code new} {@link #TCN()}
     * @return The new TCN
     */
    @Deprecated
    public static TCN getEmpty() {
        return new TCN();
    }
    
    public static class TCNException extends Exception {
        
        public TCNException(Integer line, String lineString, Exception e) {
            super("Error in line " + line + " (" + lineString + ")", e);
        }
    }
}
