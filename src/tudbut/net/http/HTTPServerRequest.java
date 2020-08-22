package tudbut.net.http;

import de.tudbut.io.StreamWriter;
import de.tudbut.type.CInfo;
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
    private final Socket socket;

    public HTTPServerRequest(String request, Socket socketIn) {
        super(request);
        socket = socketIn;
    }

    @Override
    public void close() throws IOException {
        vars.set(this, C.CLOSED, true);
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

    public HTTPResponse.ParsedHTTPResponse parse() {
        return new HTTPResponse.ParsedHTTPResponse() {
            @Override
            public String getHTTPVersion() {
                return value.split(" ")[2];
            }

            @Override
            public int getStatusCode() {
                return 0;
            }

            @Override
            public String getStatusCodeAsString() {
                return value.split(" ")[0];
            }

            @Override
            public String getPath() {
                return value.split(" ")[1].split("\\?")[0];
            }

            @Override
            public Map<String, String> getQuery() {
                Map<String, String> map = new HashMap<>();

                try {
                    String actualPath = value.split(" ")[1];
                    String query = actualPath.split("\\?")[1];

                    for (int i = 0; i < query.split("&").length; i++) {
                        try {
                            map.put(HTTPUtils.decodeUTF8(query.split("&")[i].split("=")[0]), HTTPUtils.decodeUTF8(query.split("&")[i].split("=")[1]));
                        } catch (Exception ignore) { }
                    }
                } catch (Exception ignore) { }

                return map;
            }

            @Override
            public HTTPRequestType getStatusCodeAsEnum() {
                HTTPRequestType code = null;
                for (int i = 0; i < HTTPRequestType.values().length; i++) {
                    HTTPRequestType requestType = HTTPRequestType.values()[i];
                    if(requestType.name().equals(getStatusCodeAsString())) {
                        code = requestType;
                    }
                }
                return code;
            }

            @Override
            public String getBody() {
                try {
                    return value.
                            substring(
                                    value.replaceAll("\r", "")
                                            .split("\n\n")[0].length() +
                                            (value.replaceAll("\r", "").split("\n\n")[0].contains("\r") ?
                                                    "\r\n\r\n".length() :
                                                    "\n\n".length()
                                            )
                            );
                } catch (Exception e) {
                    CInfo.s("Seems like the body doesnt exist.");
                    return "";
                }
            }

            @Override
            public HTTPHeader[] getHeaders() {
                ArrayList<HTTPHeader> headers = new ArrayList<>();
                String s = value.substring(value.split("\n")[0].length() + "\n".length());
                String line;
                int i = 0;
                while (!(line = s.split("\n")[i]).equals("")) {
                    headers.add(new HTTPHeader(line.split(": ")[0], line.split(": ")[1].split("\r")[0]));
                }
                return headers.toArray(new HTTPHeader[0]);
            }
        };
    }
}
