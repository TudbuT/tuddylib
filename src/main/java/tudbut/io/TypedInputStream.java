package tudbut.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * InputStream for more data types
 */
public class TypedInputStream {
    
    protected InputStream stream;
    public int last = -1;
    private final Object waitForInputLock = new Object();
    private final Object readLock = new Object();
    
    public InputStream getStream() {
        return stream;
    }
    
    private WriteDirection direction = WriteDirection.HIGH_FIRST;
    
    public TypedInputStream(InputStream stream) {
        this.stream = stream;
    }
    
    public byte readByte() throws IOException {
        return (byte) read();
    }
    
    public short readShort() throws IOException {
        short s = 0;
        if(direction == WriteDirection.HIGH_FIRST) {
            s += (read() << 8 * 1);
            s += (read() << 8 * 0);
        }
        else {
            s += (read() << 8 * 0);
            s += (read() << 8 * 1);
        }
        return s;
    }
    
    public char readChar() throws IOException {
        char c = 0;
        if(direction == WriteDirection.HIGH_FIRST) {
            c += (read() << 8 * 1);
            c += (read() << 8 * 0);
        }
        else {
            c += (read() << 8 * 0);
            c += (read() << 8 * 1);
        }
        return c;
    }
    
    public int readInt() throws IOException {
        int i = 0;
        if(direction == WriteDirection.HIGH_FIRST) {
            i += (read() << 8 * 3);
            i += (read() << 8 * 2);
            i += (read() << 8 * 1);
            i += (read() << 8 * 0);
        }
        else {
            i += (read() << 8 * 0);
            i += (read() << 8 * 1);
            i += (read() << 8 * 2);
            i += (read() << 8 * 3);
        }
        return i;
    }
    
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }
    
    public long readLong() throws IOException {
        long l = 0;
        if(direction == WriteDirection.HIGH_FIRST) {
            l += ((long) read() << 8 * 7);
            l += ((long) read() << 8 * 6);
            l += ((long) read() << 8 * 5);
            l += ((long) read() << 8 * 4);
            l += ((long) read() << 8 * 3);
            l += ((long) read() << 8 * 2);
            l += ((long) read() << 8 * 1);
            l += ((long) read() << 8 * 0);
        }
        else {
            l += ((long) read() << 8 * 0);
            l += ((long) read() << 8 * 1);
            l += ((long) read() << 8 * 2);
            l += ((long) read() << 8 * 3);
            l += ((long) read() << 8 * 4);
            l += ((long) read() << 8 * 5);
            l += ((long) read() << 8 * 6);
            l += ((long) read() << 8 * 7);
        }
        return l;
    }
    
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }
    
    public boolean readBoolean() throws IOException {
        return (read() & 1) != 0;
    }
    
    public boolean[] readBooleans() throws IOException {
        boolean[] booleans = new boolean[8];
        int i = read() & 0xff;
        for (int j = 0 ; j < 8 ; ) {
            booleans[j] = (i & 1 << 8 >> ++j ) != 0;
        }
        return booleans;
    }
    
    public String readString() throws IOException {
        int i = readInt();
        StringBuilder builder = new StringBuilder();
        for (int j = 0 ; j < i ; j++) {
            builder.append(readChar());
        }
        return builder.toString();
    }
    
    public void waitForInput() throws IOException {
        synchronized (waitForInputLock) {
            if(last != -1)
                return;
            while ((last = stream.read()) == -1) ;
        }
    }
    
    public int read() throws IOException {
        synchronized (readLock) {
            synchronized (waitForInputLock) {
                int i;
                if (last == -1) {
                    while ((i = stream.read()) == -1) ;
                }
                else {
                    i = last;
                    last = -1;
                }
                return i;
            }
        }
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
