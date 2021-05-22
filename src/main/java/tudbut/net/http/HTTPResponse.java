package tudbut.net.http;

import tudbut.tools.Value;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

/**
 * Response to {@link HTTPRequest}
 */
public class HTTPResponse extends Value<String> {
    
    /**
     * Constructs a HTTPResponse from non-exact content
     * @param value The content
     */
    public HTTPResponse(String value) {
        super(spl(value));
    }
    
    /**
     * Constructs a HTTPResponse from content
     * @param value The content
     */
    public HTTPResponse(String value, boolean exact) {
        super(exact ? value : spl(value));
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
     * Parses the response
     * @return The {@link ParsedHTTPValue} of the response
     */
    public ParsedHTTPValue parse() {
        String[] splitValue = value.split("\n", 2)[0].split(" ");
    
        String httpVersion = splitValue[0];
        int statusCode = Integer.parseInt(splitValue[1]);
        String statusCodeString = splitValue[2];
       HTTPResponseCode code = null;
        for (int i = 0 ; i < HTTPResponseCode.values().length; i++) {
            HTTPResponseCode responseCode = HTTPResponseCode.values()[i];
            if(responseCode.asInt == statusCode) {
                code = responseCode;
            }
        }
        ArrayList<HTTPHeader> headersList = new ArrayList<>();
        String s = value;
        s = s.substring(s.split("\n")[0].length() + 1);
        for (String line : s.split("\n")) {
            if (line.equals(""))
                break;
            headersList.add(new HTTPHeader(line.split(": ")[0], line.split(": ")[1]));
        }
        HTTPHeader[] headers = headersList.toArray(new HTTPHeader[0]);
        StringBuilder body = new StringBuilder();
        try {
            int start = value.indexOf("\n\n") + 2;
            HTTPHeader header = null;
            for (int i = 0; i < headers.length; i++) {
                if(headers[i].key().equals("Content-Length"))
                    header = headers[i];
            }
            if(header != null) {
                int end = start + Integer.parseInt(header.value());
                body = new StringBuilder(value.substring(start, end));
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
                    body.append(stream.toString());
                }
            }
        } catch (Exception ignored) {
        }
    
        HTTPResponseCode finalCode = code;
        String finalBody = body.toString();
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
            public HTTPResponseCode getStatusCodeAsEnum() {
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
