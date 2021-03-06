package tudbut.net.pbic2;

import tudbut.io.TypedInputStream;
import tudbut.io.TypedOutputStream;
import tudbut.net.http.*;

import java.io.*;
import java.net.Socket;

public final class PBIC2Client implements PBIC2 {
    
    Socket socket;
    public final TypedInputStream in;
    public final TypedOutputStream out;
    
    public PBIC2Client(HTTPRequest request) throws IOException {
        this(request, i -> i);
    }
    
    public PBIC2Client(HTTPRequest request, PBIC2Passthrough passthrough) throws IOException {
        this(request, passthrough, passthrough);
    }
    
    public PBIC2Client(HTTPRequest request, PBIC2Passthrough passthroughIn, PBIC2Passthrough passthroughOut) throws IOException {
        request.headers.removeIf(h -> h.key().equals("Connection"));
        request.headers.add(new HTTPHeader("Connection", "Upgrade"));
        request.headers.add(new HTTPHeader("Upgrade", "TudbuT/PBIC2"));
        socket = request.sendNoRead();
        socket.setSoLinger(false, 0);
        StringBuilder s = new StringBuilder();
        int res;
        while ((res = socket.getInputStream().read()) != 0) {
            s.append((char) res);
        }
        if(new HTTPResponse(s.toString()).parse().getStatusCodeAsEnum() != HTTPResponseCode.SwitchingProtocols) {
            throw new IOException("Invalid response.");
        }
        socket.getOutputStream().write(0);
        in = new TypedInputStream(new InputStream() {
            @Override
            public int read() throws IOException {
                return passthroughIn.pass(socket.getInputStream().read());
            }
        });
        out = new TypedOutputStream(new OutputStream() {
            @Override
            public void write(int i) throws IOException {
                socket.getOutputStream().write(passthroughOut.pass(i));
            }
        });
    }
    
    public String readMessage() throws IOException {
        return in.readString();
    }
    
    public synchronized String writeMessage(String s) throws IOException {
        return out.writeString(s);
    }
    
    public synchronized String writeMessageWithResponse(String s) throws IOException {
        out.writeString(s);
        return in.readString();
    }
    
    @Override
    public Socket getSocket() {
        return socket;
    }
    
    @Override
    public TypedInputStream getInput() {
        return in;
    }
    
    @Override
    public TypedOutputStream getOutput() {
        return out;
    }
}
