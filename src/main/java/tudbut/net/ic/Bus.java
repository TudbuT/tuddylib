package tudbut.net.ic;

import de.tudbut.type.CInfo;
import de.tudbut.type.O;
import tudbut.tools.Lock;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Bus implements Closeable { // Work in progress, will comment later!
    
    final InputStream i;
    final OutputStream o;
    final Lock syncI = new Lock(), syncO = new Lock();
    boolean isClosed;
    
    public Bus(InputStream i, OutputStream o) {
        this.i = i;
        this.o = o;
    }
    
    public Bus(Socket socket) throws IOException {
        this(socket.getInputStream(), socket.getOutputStream());
    }
    
    public void write(ByteBuffer buffer) throws IOException {
        String r = "Written: ";
        syncO.waitHere();
        syncO.lock();
        try {
            byte[] bytes = buffer.array();
            for (int j = 0; j < bytes.length; j++) {
                o.write(bytes[j]);
                r += (char) Byte.toUnsignedInt(bytes[j]);
            }
            o.flush();
            syncO.unlock();
        } catch (IOException e) {
            syncO.unlock();
            throw new IOException(r, e);
        }
    }
    
    public void read(ByteBuffer buffer) throws IOException {
        String r = "Read: ";
        syncI.waitHere();
        syncI.lock();
        try {
            byte[] bytes = buffer.array();
            for (int j = 0; j < bytes.length; j++) {
                bytes[j] = (byte) i.read();
                r += (char) Byte.toUnsignedInt(bytes[j]);
            }
            syncI.unlock();
        } catch (IOException e) {
            syncI.unlock();
            throw new IOException(r, e);
        }
    }
    
    public void close() throws IOException {
        i.close();
        o.close();
        syncI.unlock();
        syncO.unlock();
        isClosed = true;
    }
    
    public boolean isClosed() {
        if(isClosed)
            return true;
        try {
            i.available();
        }
        catch (IOException e) {
            isClosed = true;
        }
        return isClosed;
    }
}
