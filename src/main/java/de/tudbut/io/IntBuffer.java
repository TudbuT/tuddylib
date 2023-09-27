package de.tudbut.io;

public class IntBuffer extends Buffer {
    private int[] content;

    private IntBuffer() {
    }

    public static IntBuffer create(int length) {
        IntBuffer buffer = new IntBuffer();
        buffer.content = new int[length / Integer.BYTES];
        return buffer;
    }

    public static IntBuffer createN(int length) {
        IntBuffer buffer = new IntBuffer();
        buffer.content = new int[length];
        return buffer;
    }

    public Object get() {
        return content;
    }
}
