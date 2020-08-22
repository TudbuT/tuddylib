package tudbut.parsing;

import java.util.HashMap;
import java.util.Map;

public class ArgumentParser {
    public static Map<String, String> parseDefault(String[] args) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            try {
                // -a | -abc &! -a bc
                if (args[i].startsWith("-") && args[i].charAt(1) != '-' && (args.length == i + 1 || args[i + 1].startsWith("-"))) {
                    for (int j = 1; j < args[i].length(); j++) {
                        map.put(String.valueOf(args[i].toCharArray()[j]), "true");
                    }
                }

                // -a bc
                if (args[i].startsWith("-") && args[i].charAt(1) != '-' && args.length != i + 1 && !args[i + 1].startsWith("-")) {
                    map.put(String.valueOf(args[i].charAt(1)), args[i + 1]);
                    i++;
                }

                // --abc &! -abc de
                if (args[i].startsWith("-") && args[i].charAt(1) == '-' && (args.length == i + 1 || args[i + 1].startsWith("-"))) {
                    map.put(args[i].substring(2), "true");
                }

                // --abc de
                if (args[i].startsWith("-") && args[i].charAt(1) == '-' && args.length != i + 1 && !args[i + 1].startsWith("-")) {
                    map.put(args[i].substring(2), args[i + 1]);
                    i++;
                }
            }
            catch (Exception ignore) {
            }
        }
        return map;
    }

    public static Map<String, String> parseSlash(String[] args) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            try {
                // -a | -abc &! -a bc
                if (args[i].startsWith("/") && args[i].charAt(1) != '/' && (args.length == i + 1 || args[i + 1].startsWith("/"))) {
                    for (int j = 1; j < args[i].length(); j++) {
                        map.put(String.valueOf(args[i].toCharArray()[j]), "true");
                    }
                }

                // -a bc
                if (args[i].startsWith("/") && args[i].charAt(1) != '/' && args.length != i + 1 && !args[i + 1].startsWith("/")) {
                    map.put(String.valueOf(args[i].charAt(1)), args[i + 1]);
                    i++;
                }

                // --abc &! --abc de
                if (args[i].startsWith("/") && args[i].charAt(1) == '/' && (args.length == i + 1 || args[i + 1].startsWith("/"))) {
                    map.put(args[i].substring(2), "true");
                }

                // --abc de
                if (args[i].startsWith("/") && args[i].charAt(1) == '/' && args.length != i + 1 && !args[i + 1].startsWith("/")) {
                    map.put(args[i].substring(2), args[i + 1]);
                    i++;
                }
            }
            catch (Exception ignore) {
            }
        }
        return map;
    }
}
