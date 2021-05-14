package tudbut.net.pbic2;

import tudbut.io.TypedInputStream;
import tudbut.io.TypedOutputStream;
import tudbut.net.http.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public final class PBIC2Server implements PBIC2 {
    
    Socket socket;
    public final TypedInputStream in;
    public final TypedOutputStream out;
    
    public PBIC2Server(HTTPServerRequest request) throws IOException {
        this(request, i -> i);
    }
    
    public PBIC2Server(HTTPServerRequest request, PBIC2Passthrough passthrough) throws IOException {
        this(request, passthrough, passthrough);
    }
    
    public PBIC2Server(HTTPServerRequest request, PBIC2Passthrough passthroughIn, PBIC2Passthrough passthroughOut) throws IOException {
        ParsedHTTPValue p = request.parse();
        if(Arrays.stream(p.getHeaders()).noneMatch(h -> h.toString().equals("Connection: Upgrade"))) {
            throw new IOException("Invalid request.");
        }
        if(Arrays.stream(p.getHeaders()).noneMatch(h -> h.toString().equals("Upgrade: TudbuT/PBIC2"))) {
            throw new IOException("Invalid request.");
        }
        socket = request.socket;
        socket.getOutputStream().write(HTTPResponseFactory.create(HTTPResponseCode.SwitchingProtocols, "\u0000", HTTPContentType.ANY).value.getBytes(StandardCharsets.ISO_8859_1));
        while (socket.getInputStream().read() != 0);
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
