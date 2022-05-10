package tudbut.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * OutputStream for more data types
 */
public class TypedOutputStream {
    
    public OutputStream getStream() {
        return stream;
    }
    
    OutputStream stream;
    private WriteDirection direction = WriteDirection.HIGH_FIRST;
    
    public TypedOutputStream(OutputStream stream) {
        this.stream = stream;
    }
    
    public byte writeByte(byte b) throws IOException {
        stream.write(Byte.toUnsignedInt(b));
        stream.flush();
        return b;
    }
    
    public short writeShort(short s) throws IOException {
        if(direction == WriteDirection.HIGH_FIRST) {
            stream.write(s >> 8 * 1 & 0xff);
            stream.write(s >> 8 * 0 & 0xff);
        }
        else {
            stream.write(s >> 8 * 0 & 0xff);
            stream.write(s >> 8 * 1 & 0xff);
        }
        stream.flush();
        return s;
    }
    
    public char writeChar(char c) throws IOException {
        if(direction == WriteDirection.HIGH_FIRST) {
            stream.write(c >> 8 * 1 & 0xff);
            stream.write(c >> 8 * 0 & 0xff);
        }
        else {
            stream.write(c >> 8 * 0 & 0xff);
            stream.write(c >> 8 * 1 & 0xff);
        }
        stream.flush();
        return c;
    }
    
    public int writeInt(int i) throws IOException {
        if(direction == WriteDirection.HIGH_FIRST) {
            stream.write(i >> 8 * 3 & 0xff);
            stream.write(i >> 8 * 2 & 0xff);
            stream.write(i >> 8 * 1 & 0xff);
            stream.write(i >> 8 * 0 & 0xff);
        }
        else {
            stream.write(i >> 8 * 0 & 0xff);
            stream.write(i >> 8 * 1 & 0xff);
            stream.write(i >> 8 * 2 & 0xff);
            stream.write(i >> 8 * 3 & 0xff);
        }
        stream.flush();
        return i;
    }
    
    public float writeFloat(float f) throws IOException {
        writeInt(Float.floatToIntBits(f));
        return f;
    }
    
    public long writeLong(long l) throws IOException {
        if(direction == WriteDirection.HIGH_FIRST) {
            stream.write((int) (l >> 8 * 7 & 0xff));
            stream.write((int) (l >> 8 * 6 & 0xff));
            stream.write((int) (l >> 8 * 5 & 0xff));
            stream.write((int) (l >> 8 * 4 & 0xff));
            stream.write((int) (l >> 8 * 3 & 0xff));
            stream.write((int) (l >> 8 * 2 & 0xff));
            stream.write((int) (l >> 8 * 1 & 0xff));
            stream.write((int) (l >> 8 * 0 & 0xff));
        }
        else {
            stream.write((int) (l >> 8 * 0 & 0xff));
            stream.write((int) (l >> 8 * 1 & 0xff));
            stream.write((int) (l >> 8 * 2 & 0xff));
            stream.write((int) (l >> 8 * 3 & 0xff));
            stream.write((int) (l >> 8 * 4 & 0xff));
            stream.write((int) (l >> 8 * 5 & 0xff));
            stream.write((int) (l >> 8 * 6 & 0xff));
            stream.write((int) (l >> 8 * 7 & 0xff));
        }
        stream.flush();
        return l;
    }
    
    public double writeDouble(double d) throws IOException {
        writeLong(Double.doubleToLongBits(d));
        return d;
    }
    
    public boolean writeBoolean(boolean b) throws IOException {
        stream.write(b ? 1 : 0);
        stream.flush();
        return b;
    }
    
    public boolean[] writeBooleans(boolean[] booleans) throws IOException {
        int i = 0;
        for (int j = 0 ; j < 8 ; ) {
            i += (booleans[j] ? 1 : 0) << 8 >> ++j;
        }
        stream.write(i);
        stream.flush();
        return booleans;
    }
    
    public String writeString(String string) throws IOException {
        int i = string.length();
        writeInt(i);
        char[] chars = string.toCharArray();
        for (int j = 0 ; j < i ; j++) {
            writeChar(chars[j]);
        }
        return string;
    }
    
    public WriteDirection getDirection() {
        return direction;
    }
    
    public void setDirection(WriteDirection direction) {
        if(direction == null)
            throw new IllegalArgumentException();
        this.direction = direction;
    }
}
