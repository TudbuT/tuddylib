package tudbut.net.http;

import tudbut.tools.Value;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

/**
 * Response to {@link HTTPRequest}
 */
public class HTTPResponse extends Value<String> {
    
    private String original;
    
    /**
     * Constructs a HTTPResponse from plain content
     * @param value The content
     */
    public HTTPResponse(String value) {
        super(value.replaceAll("\r", ""));
    }
    
    /**
     * Parses the response
     * @return The {@link ParsedHTTPValue} of the response
     */
    public ParsedHTTPValue parse() {
        String[] splitValue = value.split("\n")[0].split(" ");
    
        String httpVersion = splitValue[0];
        int statusCode = Integer.parseInt(splitValue[1]);
        String statusCodeString = splitValue[2];
        HTTPResponseFactory.ResponseCode code = null;
        for (int i = 0; i < HTTPResponseFactory.ResponseCode.values().length; i++) {
            HTTPResponseFactory.ResponseCode responseCode = HTTPResponseFactory.ResponseCode.values()[i];
            if(responseCode.asInt == statusCode) {
                code = responseCode;
            }
        }
        ArrayList<HTTPHeader> headersList = new ArrayList<>();
        String s = value.replaceAll("\r", "");
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
                if(headers[i].key().equals("Content-Length"))
                    header = headers[i];
            }
            if(header != null) {
                int end = start + Integer.parseInt(header.value());
                body = value.substring(start, end);
            }
            else {
    
                /*
                 * INCREDIBLY hacky way to make chunk transfer work, will make better later
                 */
                ByteArrayInputStream b = new ByteArrayInputStream(value.substring(start).getBytes(StandardCharsets.UTF_8));
                
                for (int chunk = 0, i = -1 ; i != 0 ; chunk++) {
                    String sbuf = "";
                    while (!sbuf.endsWith("\n")) {
                        sbuf += (char) b.read();
                    }
                    i = Integer.parseInt(sbuf.split("\n")[0], 16);
                    byte[] buf = new byte[i];
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    b.read(buf);
                    stream.write(buf);
                    body += stream.toString();
                }
            }
        } catch (Exception ignored) {
        }
    
        HTTPResponseFactory.ResponseCode finalCode = code;
        String finalBody = body;
        return new ParsedHTTPValue() {
            @Override
            public String getHTTPVersion() {
                return httpVersion;
            }

            @Override
            public int getStatusCode() {
                return statusCode;
            }

            @Override
            public String getStatusCodeAsString() {
                return statusCodeString;
            }

            @Override
            public String getPath() {
                return null;
            }

            @Override
            public Map<String, String> getQuery() {
                return null;
            }

            @Override
            public HTTPResponseFactory.ResponseCode getStatusCodeAsEnum() {
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
