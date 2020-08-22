package de.tudbut.tools.bintools.encoding;

import java.util.Objects;

public class Value {
    private final String stringValue;
    private final char[] charValue;
    private final int[] intValue;
    private final byte[] byteValue;

    public Value(String s) {
        stringValue = s;
        byteValue = null;
        this.intValue = null;
        charValue = null;
    }

    public Value(char[] cs) {
        charValue = cs;
        byteValue = null;
        this.stringValue = null;
        this.intValue = null;
    }

    public Value(int[] is) {
        intValue = is;
        byteValue = null;
        this.stringValue = null;
        this.charValue = null;
    }

    public Value(byte[] bs) {
        byteValue = bs;
        intValue = null;
        this.stringValue = null;
        this.charValue = null;
    }

    public String getS() {
        return stringValue;
    }

    public char[] getC() {
        return Objects.requireNonNull(charValue).clone();
    }

    public int[] getI() {
        return Objects.requireNonNull(intValue).clone();
    }

    public byte[] getB() {
        return Objects.requireNonNull(byteValue).clone();
    }
}
