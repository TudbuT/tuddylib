package tudbut.net.http;

import de.tudbut.type.CInfo;
import tudbut.tools.Value;

import java.util.ArrayList;
import java.util.Map;

public class HTTPResponse extends Value<String> {
    public HTTPResponse(String value) {
        super(value.replaceAll("\r", ""));
    }

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
            int start = value.split("\n\n")[0].length() + 2;
            HTTPHeader header = null;
            for (int i = 0; i < headers.length; i++) {
                if(headers[i].key().equals("Content-Length"))
                    header = headers[i];
            }
            assert header != null;
            int end = start + Integer.parseInt(header.value());
            body = value.substring(start, end);
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
