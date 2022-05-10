package de.tudbut.io;

import java.io.IOException;
import java.io.OutputStream;


/**
 * Write data to an {@link OutputStream}
 */
public class StreamWriter {
    /**
     * The output stream to write to
     */
    public final OutputStream stream;
    
    /**
     * Constructs a new StreamWriter
     * @param stream
     */
    public StreamWriter(OutputStream stream) {
        this.stream = stream;
    }

    public void writeChars(char[] c) throws IOException {
        byte[] bytes = new String(c).getBytes();
        stream.write(bytes);
        stream.flush();
    }
    
    public void writeChars(char[] c, String encoding) throws IOException {
        byte[] bytes = new String(c).getBytes(encoding);
        for (int i = 0; i < bytes.length; i++) {
            writeByte(bytes[i]);
        }
        stream.flush();
    }

    public void writeByte(byte b) throws IOException {
        stream.write(Byte.toUnsignedInt(b));
    }

    public void writeUnsignedByte(short b) throws IOException {
        stream.write(b);
    }

    public void writeUnsignedByte(int b) throws IOException {
        stream.write(b);
    }

    public void writeBytes(byte[] bytes) throws IOException {
        stream.write(bytes);
        stream.flush();
    }

    public void writeUnsignedBytes(int[] bytes) throws IOException {
        for (int theByte : bytes) {
            stream.write(theByte);
        }
        stream.flush();
    }

    public void writeUnsignedBytes(short[] bytes) throws IOException {
        for (short theByte : bytes) {
            stream.write(theByte);
        }
        stream.flush();
    }
}
