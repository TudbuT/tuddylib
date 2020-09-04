package de.tudbut.tools;

public class StringTools {
    public static String replaceRaw(String s, String repl, String with) {
        String r = "";

        for (int i = 0; i < s.length(); i++) {
            int rml = 0;
            boolean cnt = false;
            for (int rl = 0; rl < repl.length(); rl++) {
                try {
                    if (s.toCharArray()[i + rl] == repl.toCharArray()[rl]) {
                        rml++;
                        if (rml == repl.length()) {
                            i += rl;
                            r += with;
                            cnt = true;
                        }
                    }
                }
                catch (ArrayIndexOutOfBoundsException ignore) {
                }
            }
            if (cnt)
                continue;
            r += s.toCharArray()[i];
        }


        return r;
    }
}
