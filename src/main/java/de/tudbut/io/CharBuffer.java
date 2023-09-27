package de.tudbut.io;

public class CharBuffer extends Buffer {
    private char[] content;

    private CharBuffer() {
    }

    public static CharBuffer create(int length) {
        CharBuffer buffer = new CharBuffer();
        buffer.content = new char[length / Character.BYTES];
        return buffer;
    }

    public static CharBuffer createN(int length) {
        CharBuffer buffer = new CharBuffer();
        buffer.content = new char[length];
        return buffer;
    }

    public Object get() {
        return content;
    }

    public String getAsString() {
        return new String(content);
    }
}
