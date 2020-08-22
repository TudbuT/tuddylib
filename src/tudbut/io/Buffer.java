package tudbut.io;

public class Buffer {
    private byte[] content;

    protected Buffer() {
    }

    public static Buffer create(int length) {
        Buffer buffer = new Buffer();
        buffer.content = new byte[length];
        return buffer;
    }

    public Object get() {
        return content;
    }
}
