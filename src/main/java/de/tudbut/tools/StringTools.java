package de.tudbut.tools;

public class StringTools {
    public static String replaceRaw(String s, String repl, String with) {
        StringBuilder r = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            int rml = 0;
            boolean cnt = false;
            for (int rl = 0; rl < repl.length(); rl++) {
                try {
                    if (s.toCharArray()[i + rl] == repl.toCharArray()[rl]) {
                        rml++;
                        if (rml == repl.length()) {
                            i += rl;
                            r.append(with);
                            cnt = true;
                        }
                    }
                }
                catch (ArrayIndexOutOfBoundsException ignore) {
                }
            }
            if (cnt)
                continue;
            r.append(s.toCharArray()[i]);
        }


        return r.toString();
    }
}
