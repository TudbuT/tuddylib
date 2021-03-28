package tudbut.net.http;

import de.tudbut.io.StreamWriter;
import tudbut.obj.Closable;
import tudbut.obj.ClosedClosableException;
import tudbut.obj.NotSupportedException;
import tudbut.tools.Value;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HTTPServerRequest extends Value<String> implements Closable {
    public final Socket socket;

    public HTTPServerRequest(String request, Socket socketIn) {
        super(request.replaceAll("\r", ""));
        socket = socketIn;
    }

    @Override
    public void close() throws IOException {
        Closable.super.close();
        socket.close();
    }

    @Override
    public void open() throws NotSupportedException {
        throw new NotSupportedException();
    }

    public void respond(HTTPResponse response) throws IOException {
        if(isClosed())
            return;
        StreamWriter writer = new StreamWriter(socket.getOutputStream());
        writer.writeChars(response.value.toCharArray());
        close();
    }

    public void forceRespond(HTTPResponse response) throws IOException, ClosedClosableException {
        if(isClosed())
            throw new ClosedClosableException();
        StreamWriter writer = new StreamWriter(socket.getOutputStream());
        writer.writeChars(response.value.toCharArray());
        close();
    }

    public HTTPResponse.ParsedHTTPValue parse() {
        String[] splitValue = value.split("\n")[0].split(" ");
        
        String httpVersion = splitValue[2];
        String statusCode = splitValue[0];
        String actualPath = splitValue[1];
        String path = actualPath.split("\\?")[0];
        Map<String, String> map = new HashMap<>();
        try {
            String query = actualPath.split("\\?")[0];
        
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
        String s = value.substring(value.split("\n")[0].length() + "\n".length());
        String line;
        int i = 0;
        while (!(line = s.split("\n")[i]).equals("")) {
            headersList.add(new HTTPHeader(line.split(": ")[0], line.split(": ")[1].split("\r")[0]));
        }
        HTTPHeader[] headers = headersList.toArray(new HTTPHeader[0]);
        String body = "";
        try {
            int start = value.split("\n\n")[0].length() + 2;
            HTTPHeader header = null;
            for (int j = 0; j < headers.length; j++) {
                if(headers[j].key().equals("Content-Length"))
                    header = headers[j];
            }
            assert header != null;
            int end = start + Integer.parseInt(header.value());
            body = value.substring(start, end);
        } catch (Exception ignored) {
        }
        String finalBody = body;
        return new HTTPResponse.ParsedHTTPValue() {
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
            public String getBody() {
                return finalBody;
            }

            @Override
            public HTTPHeader[] getHeaders() {
                return headers;
            }
        };
    }
}
