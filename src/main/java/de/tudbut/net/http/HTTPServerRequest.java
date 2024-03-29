package de.tudbut.net.http;

import de.tudbut.io.StreamWriter;
import de.tudbut.obj.Closable;
import de.tudbut.obj.ClosedClosableException;
import de.tudbut.obj.NotSupportedException;
import de.tudbut.tools.Value;

import javax.net.ssl.SSLSocket;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Used by the server to handle incoming HTTP requests
 */
public class HTTPServerRequest extends Value<String> implements Closable {
    /**
     * The socket of the request
     */
    public final Socket socket;
    
    public HTTPServerRequest(String value, Socket s) {
        super(spl(value));
        socket = s;
    }
    
    private static String spl(String s) {
        String[] splitString = s.split("\r\n\r\n", 2);
        if(splitString.length == 1) {
            return s.replaceAll("\r", "");
        }
        else {
            return splitString[0].replaceAll("\r", "") + "\n\n" + splitString[1];
        }
    }
    
    /**
     * Close the streams
     */
    @Override
    public void close() throws IOException {
        Closable.super.close();
        socket.shutdownOutput();
        if(socket instanceof SSLSocket) {
            socket.setSoLinger(true, 100);
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
            try {
                SSLSocket.class.getMethod("close").invoke(socket);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                System.err.println("!!! ERROR: OBFUSCATED OR INVALID JRE");
                socket.close();
            }
        }
        else
            socket.close();
    }
    
    /**
     * Not supported.
     * @throws NotSupportedException [Always thrown]
     */
    @Override
    public void open() throws NotSupportedException {
        throw new NotSupportedException();
    }
    
    /**
     * Responds to the request. Returns if the request was already responded to.
     * @param response The response to send
     * @throws IOException Inherited
     */
    public void respond(HTTPResponse response) throws IOException {
        if(isClosed())
            return;
        StreamWriter writer = new StreamWriter(socket.getOutputStream());
        writer.writeChars(response.value.toCharArray(), "ISO_8859_1");
        close();
    }
    
    /**
     * Responds to the request. Throws if the request was already responded to.
     * @param response The response to send
     * @throws IOException Inherited
     * @throws ClosedClosableException If the request has already been responded to.
     */
    public void forceRespond(HTTPResponse response) throws IOException, ClosedClosableException {
        if(isClosed())
            throw new ClosedClosableException();
        StreamWriter writer = new StreamWriter(socket.getOutputStream());
        writer.writeChars(response.value.toCharArray(), "ISO_8859_1");
        close();
    }
    
    /**
     * Parses the request
     * @return The {@link ParsedHTTPValue} of the request
     */
    public ParsedHTTPValue parse() {
        String[] splitValue = value.split("\n")[0].split(" ");
        
        String httpVersion = splitValue[2];
        String statusCode = splitValue[0];
        String actualPath = splitValue[1];
        String path = actualPath.split("\\?")[0];
        Map<String, String> map = new HashMap<>();
        try {
            String query = actualPath.split("\\?")[1];
        
            String[] splitByAnd = query.split("&");
            for (int i = 0; i < splitByAnd.length; i++) {
                try {
                    String[] v = splitByAnd[i].split("=");
                    map.put(HTTPUtils.decodeUTF8(v[0]), HTTPUtils.decodeUTF8(v[1]));
                } catch (Exception ignore) { }
            }
        } catch (Exception ignore) { }
        HTTPRequestType code = null;
        for (int i = 0; i < HTTPRequestType.values().length; i++) {
            HTTPRequestType requestType = HTTPRequestType.values()[i];
            if(requestType.name().equals(statusCode)) {
                code = requestType;
            }
        }
        HTTPRequestType finalCode = code;
        ArrayList<HTTPHeader> headersList = new ArrayList<>();
        String s = value;
        s = s.substring(s.split("\n")[0].length() + 1);
        for (String line : s.split("\n")) {
            if (line.equals(""))
                break;
            headersList.add(new HTTPHeader(line.split(": ")[0], line.split(": ")[1]));
        }
        HTTPHeader[] headers = headersList.toArray(new HTTPHeader[0]);
        String body = "";
        try {
            int start = value.indexOf("\n\n") + 2;
            HTTPHeader header = null;
            for (int i = 0; i < headers.length; i++) {
                if(headers[i].key().equalsIgnoreCase("Content-Length"))
                    header = headers[i];
            }
            if(header != null) {
                int end = value.length();
                body = value.substring(start, end);
            }
            else {
                /*
                 * INCREDIBLY hacky way to make chunk transfer work, will make better later
                 */
                ByteArrayInputStream b = new ByteArrayInputStream(value.substring(start).getBytes(StandardCharsets.ISO_8859_1));
            
                for (int chunk = 0, i = -1 ; i != 0 ; chunk++) {
                    StringBuilder sbuf = new StringBuilder();
                    int c;
                    while (!sbuf.toString().endsWith("\n") && (c = b.read()) != -1) {
                        sbuf.append((char) c);
                    }
                    i = Integer.parseInt(sbuf.toString().replaceAll("\r", "").split("\n")[0], 16);
                    byte[] buf = new byte[i];
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    b.read(buf);
                    stream.write(buf);
                    body += stream.toString();
                }
            }
        } catch (Exception ignored) {
        }
        String finalBody = body;
        return new ParsedHTTPValue() {
            @Override
            public String getHTTPVersion() {
                return httpVersion;
            }

            @Override
            public int getStatusCode() {
                return 0;
            }

            @Override
            public String getStatusCodeAsString() {
                return statusCode;
            }

            @Override
            public String getPath() {
                return path;
            }

            @Override
            public Map<String, String> getQuery() {
                return map;
            }

            @Override
            public HTTPRequestType getStatusCodeAsEnum() {
                return finalCode;
            }

            @Override
            public String getBodyRaw() {
                return finalBody;
            }
    
            @Override
            public HTTPHeader[] getHeaders() {
                return headers;
            }
        };
    }
}
