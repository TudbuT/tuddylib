package de.tudbut.net.http;

import de.tudbut.global.DebugStateManager;
import de.tudbut.io.StreamReader;
import de.tudbut.io.StreamWriter;
import de.tudbut.obj.DoubleTypedObject;
import de.tudbut.obj.Partial;
import de.tudbut.timer.AsyncTask;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Fully compatible, fast version of java HTTP requests
 */
public class HTTPRequest {
    public final ArrayList<HTTPHeader> headers = new ArrayList<>();
    public final String content;
    private final HTTPRequestType requestType;
    private final String path;
    private final String host;
    private final int port;
    private final boolean ssl;
    
    /**
     * Constructs a HTTPRequest without sending it
     * @param requestType The type of the request, see {@link HTTPRequestType}.
     * @param host Host, add "https://" in front to attempt an HTTPS connection, otherwise, dont specify protocol
     * @param port Port, 80 is standard for HTTP, 443 is standard for HTTPS
     * @param path Path to request, use "/" for main path
     * @param headers HTTPHeaders to use, can be empty
     */
    public HTTPRequest(HTTPRequestType requestType, String host, int port, String path, HTTPHeader... headers) {
        this(requestType, host, port, path, (String) null, "", headers);
    }
    
    /**
     * Constructs a HTTPRequest without sending it
     * @param requestTypeIn The type of the request, see {@link HTTPRequestType}.
     * @param hostIn Host, add "https://" in front to attempt a HTTPS connection, otherwise, dont specify protocol
     * @param portIn Port, 80 is standard for HTTP, 443 is standard for HTTPS
     * @param pathIn Path to request, use "/" for main path
     * @param type The type of the body
     * @param contentIn The body
     * @param headersIn HTTPHeaders to use, can be empty
     */
    public HTTPRequest(HTTPRequestType requestTypeIn, String hostIn, int portIn, String pathIn, HTTPContentType type, String contentIn, HTTPHeader... headersIn) {
        this(requestTypeIn, hostIn, portIn, pathIn, type.asHeaderString, contentIn, headersIn);
    }
    /**
     * Constructs a HTTPRequest without sending it
     * @param requestTypeIn The type of the request, see {@link HTTPRequestType}.
     * @param hostIn Host, add "https://" in front to attempt a HTTPS connection, otherwise, dont specify protocol
     * @param portIn Port, 80 is standard for HTTP, 443 is standard for HTTPS
     * @param pathIn Path to request, use "/" for main path
     * @param type The type of the body
     * @param contentIn The body
     * @param headersIn HTTPHeaders to use, can be empty
     */
    public HTTPRequest(HTTPRequestType requestTypeIn, String hostIn, int portIn, String pathIn, String type, String contentIn, HTTPHeader... headersIn) {
        this(requestTypeIn, hostIn, portIn, pathIn, new HTTPHeader("Content-Type", type), contentIn, headersIn);
    }
    
    /**
     * Constructs a HTTPRequest without sending it
     * @param requestTypeIn The type of the request, see {@link HTTPRequestType}.
     * @param hostIn Host, add "https://" in front to attempt a HTTPS connection, otherwise, dont specify protocol
     * @param portIn Port, 80 is standard for HTTP, 443 is standard for HTTPS
     * @param pathIn Path to request, use "/" for main path
     * @param type The type of the body
     * @param contentIn The body
     * @param headersIn HTTPHeaders to use, can be empty
     */
    public HTTPRequest(HTTPRequestType requestTypeIn, String hostIn, int portIn, String pathIn, HTTPHeader type, String contentIn, HTTPHeader... headersIn) {
        ssl = hostIn.startsWith("https://");
        
        requestType = requestTypeIn;
        path = pathIn;
        host = hostIn;
        port = portIn;
        headers.add(new HTTPHeader("Host", ssl ? host.split("https://")[1] : host));
        if(!contentIn.equals("")) {
            headers.add(new HTTPHeader("Content-Type", type.value(), type.parameter()));
            headers.add(new HTTPHeader("Content-Length", String.valueOf(contentIn.getBytes(StandardCharsets.ISO_8859_1).length)));
        }
        if(Arrays.stream(headersIn).noneMatch(httpHeader -> httpHeader.toString().startsWith("Connection: ")))
            headers.add(new HTTPHeader("Connection", "Close"));
        headers.addAll(Arrays.asList(headersIn));
        content = contentIn;
    }
    
    /**
     * @return The string to send to the server
     */
    public String toString() {
        String s = "";

        s += requestType.name() + " " + path + " HTTP/1.1\r\n";
        for (HTTPHeader header : headers) {
            s += header.toString() + "\r\n";
        }
        s += "\r\n";
        s += content;
        
        return s;
    }
    
    /**
     * Sends the request synchronously
     * @return The response
     * @throws IOException Inherited
     */
    public HTTPResponse send() throws IOException {
        Socket socket = connect();
        return new HTTPResponse(new String(new StreamReader(socket.getInputStream()).readAllAsBytes(), StandardCharsets.ISO_8859_1));
    }
    
    /**
     * Sends the request synchronously without returning a response
     * @return The socket
     * @throws IOException Inherited
     */
    public Socket sendNoRead() throws IOException {
        return connect();
    }
    
    /**
     * Sends the request asynchronously
     * @return The response enclosed in a {@link Partial}
     */
    public Partial<HTTPResponse> sendKeepAlive() {
        return sendKeepAlive(-1);
    }
    
    /**
     * Sends the request asynchronously with optional timeout
     * @param timeout The timeout at which to stop receiving, use -1 for infinity
     * @return The response enclosed in a {@link Partial}
     */
    public Partial<HTTPResponse> sendKeepAlive(int timeout) {
        Partial<HTTPResponse> partialResponse = new Partial<>(null);
        AsyncTask<HTTPResponse> task = new AsyncTask<>(() -> {
            try {
                Socket socket = connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.ISO_8859_1));
                int c;
                StringBuilder builder = new StringBuilder();
                while ((c = reader.read()) != -1) {
                    builder.append((char)c);
                    if(c == '\n')
                        partialResponse.change(new HTTPResponse(builder.toString()));
                }
                socket.close();
                partialResponse.change(new HTTPResponse(builder.toString()));
                partialResponse.complete(partialResponse.get());
            } catch (Exception ignored) { }
            return partialResponse.get();
        });
        task.setTimeout(timeout);
        return partialResponse;
    }
    
    /**
     * Sends the request asynchronously with optional timeout
     * @param timeout The timeout at which to stop receiving, use -1 for infinity
     * @return The task and enclosed response (in a {@link Partial}), enclosed in a {@link DoubleTypedObject}
     */
    public DoubleTypedObject<Partial<HTTPResponse>, AsyncTask<HTTPResponse>> sendKeepAliveWithTask(int timeout) {
        Partial<HTTPResponse> partialResponse = new Partial<>(null);
        AsyncTask<HTTPResponse> task = new AsyncTask<>(() -> {
            try {
                Socket socket = connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.ISO_8859_1));
                int c;
                StringBuilder builder = new StringBuilder();
                while ((c = reader.read()) != -1) {
                    builder.append((char)c);
                    if(c == '\n')
                        partialResponse.change(new HTTPResponse(builder.toString()));
                }
                socket.close();
                partialResponse.change(new HTTPResponse(builder.toString()));
                partialResponse.complete(partialResponse.get());
                return partialResponse.get();
            } catch (Exception ignored) { }
            return partialResponse.get();
        });
        task.setTimeout(timeout);
        return new DoubleTypedObject<>(partialResponse, task);
    }
    /**
     * Sends the request and return the socket from which {@link #send} and {@link #sendKeepAlive} will read
     * @return The created socket
     * @throws IOException Inherited
     */
    private Socket connect() throws IOException {
        Socket socket;
        if (ssl) {
            socket = new Socket(host.substring("https://".length()), port);
            SSLSocket sslSocket = (SSLSocket) ((SSLSocketFactory) SSLSocketFactory.getDefault()).createSocket(socket, host.substring("https://".length()), port, true);
            sslSocket.startHandshake();
            if(DebugStateManager.isDebugEnabled()) {
                DebugStateManager.getDebugLogger().debug("HTTPRequest using SSL/TLS version " + sslSocket.getSession().getProtocol());
            }
            socket = sslSocket;
        }
        else
            socket = new Socket(InetAddress.getByName(host), port);
        socket.setSoTimeout(10000);
        socket.setSoLinger(true, 1000);
        StreamWriter writer = new StreamWriter(socket.getOutputStream());
        writer.writeChars(toString().toCharArray(), "ISO_8859_1");
        return socket;
    }
    
}
