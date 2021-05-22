package tudbut.net.http;

import de.tudbut.io.StreamWriter;
import de.tudbut.type.Stoppable;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.ArrayList;
import java.util.concurrent.Executor;

/**
 * A HTTP server
 */
public class HTTPServer implements Stoppable {
    private final int port;
    private final ServerSocket serverSocket;
    private final HTTPResponse serverError;
    private final ArrayList<HTTPHandler> handlers = new ArrayList<>();
    private final Executor executor;
    
    /**
     * Constructs a HTTPServer without HTTPS
     * @param portIn Port to listen on
     * @throws IOException Inherited
     */
    public HTTPServer(int portIn) throws IOException {
        port = portIn;
        serverError = HTTPResponseFactory.create(HTTPResponseCode.InternalServerError, "501 InternalServerError", HTTPContentType.TXT);
        serverSocket = new ServerSocket(port);
        executor = Runnable::run;
    }
    
    /**
     * Constructs a HTTPServer without HTTPS
     * @param portIn Port to listen on
     * @param serverErrorIn Response to send on error
     * @param executorIn Executor
     * @throws IOException Inherited
     */
    public HTTPServer(int portIn, HTTPResponse serverErrorIn, Executor executorIn) throws IOException {
        port = portIn;
        serverError = serverErrorIn;
        serverSocket = new ServerSocket(port);
        executor = executorIn;
    }
    
    /**
     * Constructs a HTTPServer with HTTPS
     * @param portIn Port to listen on
     * @param serverErrorIn Response to send on error
     * @param executorIn Executor
     * @param keyStore {@link KeyStore} The PRIVATE KEY KeyStore
     * @param keyStorePass Password for the KeyStore
     * @throws IOException Inherited
     */
    public HTTPServer(int portIn, HTTPResponse serverErrorIn, Executor executorIn, KeyStore keyStore, String keyStorePass) throws IOException {
        port = portIn;
        serverError = serverErrorIn;
        try {
            SSLContext context = SSLContext.getInstance("TLS");
            KeyManagerFactory managerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            managerFactory.init(keyStore, keyStorePass.toCharArray());
            context.init(managerFactory.getKeyManagers(), null, null);
            serverSocket = context.getServerSocketFactory().createServerSocket(portIn);
        }
        catch (NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException | KeyManagementException e) {
            throw new IllegalArgumentException(e);
        }
    
        executor = executorIn;
    }
    
    /**
     * Start listening for requests
     */
    public void listen() {
        new Thread(() -> {
            Socket socket;
            while (!isStopped()) {
                try {
                    socket = serverSocket.accept();
                    Socket finalSocket = socket;
                    boolean b = true;
                    for (int i = 0 ; i < handlers.size() ; i++) {
                        if(!handlers.get(i).accept(socket.getRemoteSocketAddress()) || !handlers.get(i).accept(socket, socket.getRemoteSocketAddress()))
                            b = false;
                    }
                    if(b) {
                        executor.execute(() -> {
                            HTTPServerRequest serverRequest = new HTTPServerRequest("", finalSocket);
                            try {
                                HTTPHandler[] handlers = this.handlers.toArray(new HTTPHandler[0]);
            
                                String s;
                                ArrayList<HTTPHeader> headers = new ArrayList<>();
                                StringBuilder fullRequest = new StringBuilder();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(finalSocket.getInputStream(), StandardCharsets.ISO_8859_1));
                                int line = 0;
                                while ((s = reader.readLine()) != null) {
                                    fullRequest.append(s).append("\n");
                                    if (s.equals("")) {
                                        break;
                                    }
                                    if (line != 0) {
                                        headers.add(new HTTPHeader(s.split(": ")[0], s.split(": ")[1]));
                                    }
                                    line++;
                                }
                                int contentLength = 0;
                                for (HTTPHeader header : headers) {
                                    if (header.key().equalsIgnoreCase("Content-Length")) {
                                        contentLength = Integer.parseInt(header.value());
                                    }
                                }
                                if(contentLength != 0) {
                                    for (int i = 0 ; i < contentLength ; i++) {
                                        fullRequest.append((char) reader.read());
                                    }
                                }
                                HTTPServerRequest request = new HTTPServerRequest(fullRequest.toString(), finalSocket);
                                serverRequest = request;
                                for (HTTPHandler handler : handlers) {
                                    handler.handle(request);
                                }
                            }
                            catch (Throwable e) {
                                try {
                                    new StreamWriter(finalSocket.getOutputStream()).writeChars(handlers.get(0).onError(serverRequest, serverError, e).value.toCharArray(), "ISO-8859-1");
                                }
                                catch (Throwable ignore) {
                                }
                            }
                        });
                    }
                    else {
                        executor.execute(() -> {
                            for (int i = 0 ; i < handlers.size() ; i++) {
                                try {
                                    handlers.get(i).handleDeny(new HTTPServerRequest("", finalSocket));
                                }
                                catch (Exception ignored) { }
                            }
                        });
                    }
                } catch (IOException ignore) { }
            }
        }).start();
    }
    
    /**
     * Add a handler to handle requests
     * @param handler The handler to add
     */
    public void addHandler(HTTPHandler handler) {
        handlers.add(handler);
    }
    
    /**
     * Handler for incoming HTTP request
     */
    public interface HTTPHandler {
        /**
         * Handle a request to the server
         * @param request The request to handle
         * @throws Exception To make handling easier (less catching required)
         */
        void handle(HTTPServerRequest request) throws Exception;
    
        /**
         * Should the connection be accepted? (Useful for rate-limiting)
         * @param address The address the connection is coming from
         * @return If the connection should be accepted
         */
        default boolean accept(SocketAddress address) {
            return true;
        }
        /**
         * Should the connection be accepted? (Useful for rate-limiting)
         * @param socket The socket
         * @param address The address the connection is coming from
         * @return If the connection should be accepted
         */
        default boolean accept(Socket socket, SocketAddress address) {
            return true;
        }
    
        /**
         *
         * @param request An empty request to hold the response methods
         * @throws Exception To make handling easier (less catching required)
         */
        default void handleDeny(HTTPServerRequest request) throws Exception { }
    
        /**
         * @param request The request as constructed so far
         * @param defaultResponse The server's default response
         * @param theError The error
         * @throws Exception To make handling easier (less catching required)
         */
        default HTTPResponse onError(HTTPServerRequest request, HTTPResponse defaultResponse, Throwable theError) throws Exception {
            return defaultResponse;
        }
    }
}
