package de.tudbut.type;

import de.tudbut.tools.Tools;

public class Token {
    public static byte LENGTH = 64;
    private final int[] ints = new int[LENGTH];

    public Token(String token) {
        if (token.length() == LENGTH) {
            for (int i = 0; i < token.length(); i++) {
                ints[i] = token.toCharArray()[i];
            }
        }
        else
            throw new StringIndexOutOfBoundsException("Must be " + LENGTH);
    }

    public static Token gen() {
        StringBuilder pool = new StringBuilder();
        for (char i = 0; i < '\u00ff'; i++) {
            pool.append(i);
        }

        return new Token(Tools.randomString(LENGTH, pool.toString()));
    }

    public String toString() {
        StringBuilder string = new StringBuilder();

        for (int i = 0; i < ints.length; i++) {
            string.append((char) ints[i]);
        }

        return string.toString();
    }
}

