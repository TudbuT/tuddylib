package de.tudbut.net.pbic2;

import de.tudbut.io.TypedInputStream;
import de.tudbut.io.TypedOutputStream;
import de.tudbut.tools.ReflectUtil;

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
            ReflectUtil.forceAccessible(self);
            return (Socket) self.get(getSocket());
        }
        catch (Exception e) {
            throw new IllegalStateException("cannot call getRealSocket() on this JVM");
        }
    }
}
