package tudbut.io;

public class DoubleBuffer extends Buffer {
    private double[] content;

    private DoubleBuffer() {
    }

    public static DoubleBuffer create(int length) {
        DoubleBuffer buffer = new DoubleBuffer();
        buffer.content = new double[length / Double.BYTES];
        return buffer;
    }

    public static DoubleBuffer createN(int length) {
        DoubleBuffer buffer = new DoubleBuffer();
        buffer.content = new double[length];
        return buffer;
    }

    public Object get() {
        return content;
    }
}
