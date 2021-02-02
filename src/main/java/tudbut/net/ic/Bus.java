package tudbut.net.ic;

import de.tudbut.type.CInfo;
import de.tudbut.type.O;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Bus implements Closeable { // Work in progress, will comment later!
    
    final InputStream i;
    final OutputStream o;
    boolean syncI, syncO;
    boolean isClosed;
    
    public Bus(InputStream i, OutputStream o) {
        this.i = i;
        this.o = o;
    }
    
    public Bus(Socket socket) throws IOException {
        this(socket.getInputStream(), socket.getOutputStream());
    }
    
    public void write(ByteBuffer buffer) throws IOException {
        CInfo.s("Synchronizing will 90% of the time block the thread indefinitely");
        
        String r = "Written: ";
        while(syncO);
        syncO = true;
        try {
            byte[] bytes = buffer.array();
            for (int j = 0; j < bytes.length; j++) {
                o.write(bytes[j]);
                r += (char) Byte.toUnsignedInt(bytes[j]);
            }
            o.flush();
            syncO = false;
        } catch (IOException e) {
            syncO = false;
            
        }
    }
    
    public void read(ByteBuffer buffer) throws IOException {
        CInfo.s("Synchronizing will 90% of the time block the thread indefinitely");
        
        String r = "Read: ";
        while (syncI) ;
        syncI = true;
        try {
            byte[] bytes = buffer.array();
            for (int j = 0; j < bytes.length; j++) {
                bytes[j] = (byte) i.read();
                r += (char) Byte.toUnsignedInt(bytes[j]);
            }
            syncI = false;
        } catch (IOException e) {
            syncI = false;
            throw new IOException(r, e);
        }
    }
    
    public void close() throws IOException {
        i.close();
        o.close();
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
