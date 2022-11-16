package de.tudbut.tools;

import de.tudbut.type.StringArray;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.sql.Time;
import java.util.*;
import tudbut.obj.TypedArray;

public class Tools {

    public static String[] readf(String format, String s) {
        // extracts parts of a string denoted by {}
        try {
            if(!format.contains("{}"))
                return format.equals(s) ? new String[]{} : null;
            if(format.contains("{}{}")) throw new IllegalArgumentException("Ambiguous argument: '{}{}' found in format string");
            String f = format;
            int occurences = 0;
            for(; f.indexOf("{}") != -1; occurences++) {
                f = f.substring(f.indexOf("{}") + 2);
            }
            String[] result = new String[occurences];

            String originalFormat = format;
            for(int n = 0; n <= occurences; n++) { // This may throw if it doesn't match, but that's the same outcome
                // shave off blanking space
                int i = format.indexOf("{}");
                if(i == -1) i = format.length();
                if(!format.substring(0, i).equals(s.substring(0, i))) return null; // If the previous part didn't match, we can forget about it.
                if(n == occurences) {
                    break;
                }
                format = format.substring(i + 2);
                s = s.substring(i);
                // populate braces
                int x = format.indexOf("{}");
                if(x != -1) {
                    result[n] = s.substring(0, s.indexOf(format.substring(0, x)));
                }
                else {
                    result[n] = s.substring(0, s.length() - (originalFormat.length() - originalFormat.lastIndexOf("{}") - 2));
                }
                s = s.substring(result[n].length());
            }
            if(result[occurences - 1] == null) // this happens when a later part doesnt match;
                return null;

            return result;
        } catch(Exception e) {
            return null;
        }
    }

    public static String readf1(String format, String s) {
        // extracts parts of a string denoted by {}
        String[] r = readf(format, s);
        if(r == null) return null;
        if(r.length == 0) return "";
        return r[0];
    }

    public static BufferedReader getStdInput() {
        return new BufferedReader(new InputStreamReader(System.in));
    }

    public static String randomOutOfArray(StringArray stringArray) {
        return stringArray.asArray()[(int) Math.floor(Math.random() * stringArray.asArray().length)];
    }

    public static <T> T randomOutOfArray(T[] array) {
        return array[(int) Math.floor(Math.random() * array.length)];
    }

    public static <T> T randomOutOfArray(TypedArray<T> array) {
        return array.get((int) Math.floor(Math.random() * array.length()));
    }

    public static String randomString(int length, String pool) {
        StringBuilder r = new StringBuilder();

        for (int i = 0; i < length; i++) {
            r.append(pool.charAt(ExtendedMath.random(0, pool.length() - 1)));
        }

        return r.toString();
    }

    public static void copyArray(Object array1, Object array2, int copyLength) {
        System.arraycopy(array1, 0, array2, 0, copyLength);
    }

    public static String randomAlphanumericString(int length) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        String pool = alphabet + alphabet.toUpperCase() + "0123456789";

        return randomString(length, pool);
    }

    public static String randomReadableString(int length) {
        String pool = "bcdfghjklmnpqrstvwxyz";
        String readablePool = "aeiou";

        StringBuilder r = new StringBuilder();
        for (int i = 0; i < length; i++) {
            r.append(pool.charAt(ExtendedMath.random(0, pool.length() - 1)))
                    .append(readablePool.charAt(ExtendedMath.random(0, readablePool.length() - 1)));
        }
        return r.substring(0, length);
    }

    public static String getTime() {
        return new Time(new Date().getTime()).toString();
    }

    public static String stringSwitch(Map<String, String> switchMap, String value) {
        if (switchMap.get(value) != null) {
            return switchMap.get(value);
        }

        return switchMap.get("__default");
    }

    public static Map<String, String> toSwitchMap(Map<String, String> alreadyExisting, String newKey, String newVal) {
        alreadyExisting.put(newKey, newVal);
        alreadyExisting.putIfAbsent("__default", "");
        return alreadyExisting;
    }

    public static Map<String, String> newSwitchMap(String defaultVal) {
        HashMap<String, String> r = new HashMap<>();

        r.put("__default", defaultVal);

        return r;
    }

    public static Map<String, String> stringToMap(String mapStringParsable) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        String[] splitTiles = mapStringParsable.split(";");
        for (int i = 0; i < splitTiles.length; i++) {
            String tile = splitTiles[i];
            String[] splitTile = tile.split(":");
            if (tile.contains(":")) {
                if (splitTile.length == 2)
                    map.put(
                            splitTile[0]
                                    .replaceAll("%I", ":")
                                    .replaceAll("%B", ";")
                                    .replaceAll("%P", "%"),
                            splitTile[1].equals("%N")
                                    ? null
                                    : splitTile[1]
                                            .replaceAll("%I", ":")
                                            .replaceAll("%B", ";")
                                            .replaceAll("%P", "%"));
                else
                    map.put(
                            splitTile[0]
                                    .replaceAll("%I", ":")
                                    .replaceAll("%B", ";")
                                    .replaceAll("%P", "%"),
                            "");
            }
        }

        return map;
    }

    public static String mapToString(Map<String, String> map) {
        StringBuilder r = new StringBuilder();

        for (String key : map.keySet().toArray(new String[0])) {

            r.append(key.replaceAll("%", "%P").replaceAll(";", "%B").replaceAll(":", "%I"))
                    .append(":")
                    .append(
                            map.get(key) == null
                                    ? "%N"
                                    : map.get(key)
                                            .replaceAll("%", "%P")
                                            .replaceAll(";", "%B")
                                            .replaceAll(":", "%I"))
                    .append(";");
        }

        return r.toString();
    }

    public static byte[] charArrayToByteArray(char[] chars) {
        byte[] bytes = new byte[chars.length];

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (int) chars[i];
        }

        return bytes;
    }

    public static int[] charArrayToIntArray(char[] chars) {
        int[] ints = new int[chars.length];

        for (int i = 0; i < ints.length; i++) {
            ints[i] = chars[i];
        }

        return ints;
    }

    public static char[] intArrayToCharArray(int[] ints) {
        char[] chars = new char[ints.length];

        for (int i = 0; i < ints.length; i++) {
            chars[i] = (char) ints[i];
        }

        return chars;
    }

    public static int[] byteArrayToIntArray(byte[] bytes) {
        int[] ints = new int[bytes.length];

        for (int i = 0; i < ints.length; i++) {
            ints[i] = Byte.toUnsignedInt(bytes[i]);
        }

        return ints;
    }

    public static int[] byteArrayToUnsignedIntArray(byte[] bytes) {
        int[] ints = new int[bytes.length];

        for (int i = 0; i < ints.length; i++) {
            ints[i] = Byte.toUnsignedInt(bytes[i]);
        }

        return ints;
    }

    public static String wildcardToRegex(String s) {
        String r = "";
        char[] charArray = s.toCharArray();
        for (int i = 0, charArrayLength = charArray.length; i < charArrayLength; i++) {
            char c = charArray[i];
            r += ("[" + c + "]").replaceAll("\\^", "\\^");
        }
        return "^"
                + r.replaceAll("\\[\\\\]", "[\\\\}")
                        .replaceAll("\\[\\*]", "(.|\n)*")
                        .replaceAll("\\[\\?]", "[.\n]") + "$";
    }

    public static class TFS {

        public static String createTFS(String sep) {
            Map<String, String> mainMap = new HashMap<>();
            mainMap.put("head", "\u0000\u0001" + sep + "\u0000\u0002\u0020\u0000\u0003/\u0000");
            return mapToString(mainMap);
        }

        public static String getFromHead(String tfs, String key) {
            Map<String, String> tfsMap = stringToMap(tfs);

            for (String val : tfsMap.get("head").split("\\x{0000}")) {
                if (val.startsWith(key)) return val.substring(1);
            }
            return null;
        }

        public static String getPath(String tfs, String path) {
            String p = Objects.requireNonNull(getFromHead(tfs, "\u0003"));
            return path.startsWith("/") ? path : (p.equals("/") ? p : p + "/") + path;
        }

        public static String getFile(String tfs, String path) {
            Map<String, String> tfsMap = stringToMap(tfs);

            return tfsMap.get(getPath(tfs, path));
        }

        public static String getFileContent(String file) {
            return stringToMap(file).get("content");
        }

        public static String createFile(String tfs, String path, String content)
                throws TFSException.TFSFileAlreadyExistsException {
            if (getFile(tfs, path) == null) {
                Map<String, String> tfsMap = stringToMap(tfs);
                Map<String, String> fileMap = new HashMap<>();

                fileMap.put("head", "\u0000");
                fileMap.put("content", content);
                fileMap.put("mods", String.valueOf(new Date().getTime()));
                fileMap.put("lastMod", String.valueOf(new Date().getTime()));

                tfsMap.put(getPath(tfs, path), mapToString(fileMap));
                return mapToString(tfsMap);
            } else throw new TFSException.TFSFileAlreadyExistsException();
        }

        public static String modFile(String tfs, String path, String newContent)
                throws TFSException.TFSFileNotFoundException {
            if (getFile(tfs, path) != null) {
                Map<String, String> tfsMap = stringToMap(tfs);
                Map<String, String> fileMap = stringToMap(tfsMap.get(getPath(tfs, path)));

                fileMap.put("content", newContent);
                fileMap.put("mods", fileMap.get("mods") + ";" + new Date().getTime());
                fileMap.put("lastMod", String.valueOf(new Date().getTime()));

                tfsMap.put(getPath(tfs, path), mapToString(fileMap));
                return mapToString(tfsMap);
            } else throw new TFSException.TFSFileNotFoundException();
        }

        public static String cd(String tfs, String path) throws TFSException.TFSPathNotFromRootException {
            if (path.startsWith(Objects.requireNonNull(getFromHead(tfs, "\u0001")))) {
                Map<String, String> tfsMap = stringToMap(tfs);

                StringBuilder newHead = new StringBuilder();
                for (String val : tfsMap.get("head").split("\\x{0000}")) {
                    if (val.startsWith("\u0003")) val = "\u0003" + path;
                    newHead.append(val).append("\u0000");
                }
                tfsMap.put("head", newHead.toString());

                return mapToString(tfsMap);
            } else throw new TFSException.TFSPathNotFromRootException();
        }

        public static class TFSException extends Exception {

            public static class TFSFileAlreadyExistsException extends TFSException {}

            public static class TFSFileNotFoundException extends TFSException {}

            public static class TFSPathNotFromRootException extends TFSException {}
        }
    }

    public static class ObjectMapping {
        public static Map<String, String> objectToMap(Object o) throws IllegalAccessException {
            Map<String, String> map = new HashMap<>();

            Class<?> c = o.getClass();
            for (Field field : c.getFields()) {
                if (field.getType() == String.class) {
                    map.put(
                            field.getName(),
                            "str\u0000"
                                    + ((String) field.get(new Object()))
                                            .replaceAll("\\x{0000}", "\u00010")
                                            .replaceAll("\\x{0001}", "\u00011"));
                }

                if (field.getType().isInstance(new HashMap<String, String>())) {
                    map.put(
                            field.getName(),
                            "map\u0000"
                                    + mapToString((Map<String, String>) field.get(new Object()))
                                            .replaceAll("\\x{0000}", "\u00010")
                                            .replaceAll("\\x{0001}", "\u00011"));
                }

                if (field.getType() == int.class) {
                    map.put(field.getName(), "int\u0000" + field.get(new Object()));
                }

                if (field.getType() == long.class) {
                    map.put(field.getName(), "lon\u0000" + field.get(new Object()));
                }

                if (field.getType() == double.class) {
                    map.put(field.getName(), "dou\u0000" + field.get(new Object()));
                }

                if (field.getType() == float.class) {
                    map.put(field.getName(), "flo\u0000" + field.get(new Object()));
                }

                if (field.getType() == boolean.class) {
                    map.put(field.getName(), "boo\u0000" + field.get(new Object()));
                }
            }

            return map;
        }

        public static void mapToObject(Object o, Map<String, String> map) throws IllegalAccessException {
            Class<?> c = o.getClass();
            for (String key : map.keySet()) {
                String type = map.get(key).split("\\x{0000}")[0];
                String val = map.get(key)
                        .split("\\x{0000}")[1]
                        .replaceAll("\\x{0001}0", "\u0000")
                        .replaceAll("\\x{0001}1", "\u0001");

                try {
                    Field field = c.getField(key);

                    if (type.equals("str")) {
                        field.set(new Object(), val);
                    }

                    if (type.equals("map")) {
                        field.set(new Object(), stringToMap(val));
                    }

                    if (type.equals("int")) {
                        field.set(new Object(), Integer.parseInt(val));
                    }

                    if (type.equals("lon")) {
                        field.set(new Object(), Long.parseLong(val));
                    }

                    if (type.equals("dou")) {
                        field.set(new Object(), Double.parseDouble(val));
                    }

                    if (type.equals("flo")) {
                        field.set(new Object(), Float.parseFloat(val));
                    }

                    if (type.equals("boo")) {
                        field.set(new Object(), Boolean.parseBoolean(val));
                    }
                } catch (NoSuchFieldException ignore) {
                }
            }
        }

        public static Map<String, String> staticObjectToMap(Class<?> c) throws IllegalAccessException {
            Map<String, String> map = new HashMap<>();

            for (Field field : c.getFields()) {
                if (field.getType() == String.class && field.get(new Object()) != null) {
                    map.put(
                            field.getName(),
                            "str\u0000"
                                    + ((String) field.get(new Object()))
                                            .replaceAll("\\x{0000}", "\u00010")
                                            .replaceAll("\\x{0001}", "\u00011"));
                }

                if (field.getType().isInstance(new HashMap<String, String>()) && field.get(new Object()) != null) {
                    map.put(
                            field.getName(),
                            "map\u0000"
                                    + mapToString((Map<String, String>) field.get(new Object()))
                                            .replaceAll("\\x{0000}", "\u00010")
                                            .replaceAll("\\x{0001}", "\u00011"));
                }

                if (field.getType() == int.class) {
                    map.put(field.getName(), "int\u0000" + field.get(new Object()));
                }

                if (field.getType() == long.class) {
                    map.put(field.getName(), "lon\u0000" + field.get(new Object()));
                }

                if (field.getType() == double.class) {
                    map.put(field.getName(), "dou\u0000" + field.get(new Object()));
                }

                if (field.getType() == float.class) {
                    map.put(field.getName(), "flo\u0000" + field.get(new Object()));
                }

                if (field.getType() == boolean.class) {
                    map.put(field.getName(), "boo\u0000" + field.get(new Object()));
                }
            }

            return map;
        }

        public static void mapToStaticObject(Class<?> c, Map<String, String> map) throws IllegalAccessException {
            for (String key : map.keySet()) {
                String type = map.get(key).split("\\x{0000}")[0];
                String val = map.get(key)
                        .split("\\x{0000}")[1]
                        .replaceAll("\\x{0001}0", "\u0000")
                        .replaceAll("\\x{0001}1", "\u0001");

                try {
                    Field field = c.getField(key);

                    if (type.equals("str")) {
                        field.set(new Object(), val);
                    }

                    if (type.equals("map")) {
                        field.set(new Object(), stringToMap(val));
                    }

                    if (type.equals("int")) {
                        field.set(new Object(), Integer.parseInt(val));
                    }

                    if (type.equals("lon")) {
                        field.set(new Object(), Long.parseLong(val));
                    }

                    if (type.equals("dou")) {
                        field.set(new Object(), Double.parseDouble(val));
                    }

                    if (type.equals("flo")) {
                        field.set(new Object(), Float.parseFloat(val));
                    }

                    if (type.equals("boo")) {
                        field.set(new Object(), Boolean.parseBoolean(val));
                    }
                } catch (NoSuchFieldException ignore) {
                }
            }
        }
    }
}
