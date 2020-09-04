package de.tudbut.tools.bintools.encoding;

import de.tudbut.tools.Hasher;

public class Seed {
    private final String seed;

    public Seed(String s) {
        seed = s;
    }

    public static String random() {
        try {
            return Hasher.sha512hex(Math.random() + "_" + Math.random());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSeed() {
        return seed;
    }
}
