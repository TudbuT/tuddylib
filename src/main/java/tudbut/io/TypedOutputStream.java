package tudbut.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TypedOutputStream {
    
    OutputStream stream;
    
    public TypedOutputStream(OutputStream stream) {
        this.stream = stream;
    }
    
    public byte writeByte(byte b) throws IOException {
        stream.write(Byte.toUnsignedInt(b));
        return b;
    }
    
    public short writeShort(short s) throws IOException {
        stream.write(s >> 8*1 & 0xff);
        stream.write(s >> 8*0 & 0xff);
        return s;
    }
    
    public int writeInt(int i) throws IOException {
        stream.write(i >> 8*3 & 0xff);
        stream.write(i >> 8*2 & 0xff);
        stream.write(i >> 8*1 & 0xff);
        stream.write(i >> 8*0 & 0xff);
        return i;
    }
    
    public float writeFloat(float f) throws IOException {
        writeInt(Float.floatToIntBits(f));
        return f;
    }
    
    public long writeLong(long l) throws IOException {
        stream.write((int) (l >> 8*7 & 0xff));
        stream.write((int) (l >> 8*6 & 0xff));
        stream.write((int) (l >> 8*5 & 0xff));
        stream.write((int) (l >> 8*4 & 0xff));
        stream.write((int) (l >> 8*3 & 0xff));
        stream.write((int) (l >> 8*2 & 0xff));
        stream.write((int) (l >> 8*1 & 0xff));
        stream.write((int) (l >> 8*0 & 0xff));
        return l;
    }
    
    public double writeDouble(double d) throws IOException {
        writeLong(Double.doubleToLongBits(d));
        return d;
    }
    
    public boolean writeBoolean(boolean b) throws IOException {
        stream.write(b ? 1 : 0);
        return b;
    }
    
    public boolean[] writeBooleans(boolean[] booleans) throws IOException {
        int i = 0;
        for (int j = 0 ; j < 8 ; ) {
            i += (booleans[j] ? 1 : 0) << 8 >> ++j;
        }
        stream.write(i);
        return booleans;
    }
}
