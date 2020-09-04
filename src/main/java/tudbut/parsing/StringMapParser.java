package tudbut.parsing;

import java.util.Map;

public class StringMapParser {
    public static boolean getBoolean(Map<String, String> map, String key) {
        try {
            return Boolean.parseBoolean(map.getOrDefault(key, "false"));
        }
        catch (Exception e) {
            return false;
        }
    }

    public static String get(Map<String, String> map, String key) {
        try {
            return map.getOrDefault(key, "");
        }
        catch (Exception e) {
            return "";
        }
    }

    public static int getInt(Map<String, String> map, String key) {
        try {
            return Integer.parseInt(map.getOrDefault(key, "0"));
        }
        catch (Exception e) {
            return 0;
        }
    }
}
