package tudbut.net.pbic2;

import tudbut.io.AdaptiveSocketInputStream;
import tudbut.io.TypedInputStream;
import tudbut.io.TypedOutputStream;
import tudbut.obj.TLMap;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Socket;

public interface PBIC2 {
    
    String readMessage() throws IOException;
    
    String writeMessage(String s) throws IOException;
    
    String writeMessageWithResponse(String s) throws IOException;
    
    Socket getSocket();
    TypedInputStream getInput();
    TypedOutputStream getOutput();

    default boolean isSSL() {
        return getSocket() instanceof SSLSocket;
    }
    default Socket getRealSocket() {
        if(!isSSL())
            throw new IllegalStateException("getRealSocket() called despite socket not being SSL");
        try {
            Class<?> BaseSSLSocketImpl = Class.forName("sun.security.ssl.BaseSSLSocketImpl");
            Field self = BaseSSLSocketImpl.getDeclaredField("self");
            self.setAccessible(true);
            return (Socket) self.get(getSocket());
        }
        catch (Exception e) {
            throw new IllegalStateException("cannot call getRealSocket() on this JVM");
        }
    }
}
