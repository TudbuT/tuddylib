package tudbut.net.http;

import de.tudbut.type.CInfo;
import tudbut.tools.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HTTPResponse extends Value<String> {
    public HTTPResponse(String value) {
        super(value);
    }

    public ParsedHTTPResponse parse() {
        return new ParsedHTTPResponse() {
            @Override
            public String getHTTPVersion() {
                return value.replaceAll("\r", "").split("\n")[0].split(" ")[0];
            }

            @Override
            public int getStatusCode() {
                return Integer.parseInt(value.replaceAll("\r", "").split("\n")[0].split(" ")[1]);
            }

            @Override
            public String getStatusCodeAsString() {
                return value.replaceAll("\r", "").split("\n")[0].split(" ")[2];
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
                HTTPResponseFactory.ResponseCode code = null;
                for (int i = 0; i < HTTPResponseFactory.ResponseCode.values().length; i++) {
                    HTTPResponseFactory.ResponseCode responseCode = HTTPResponseFactory.ResponseCode.values()[i];
                    if(responseCode.asInt == getStatusCode()) {
                        code = responseCode;
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
                String s = value.replaceAll("\r", "");
                s = s.substring(s.split("\n")[0].length() + 1);
                for (String line : s.split("\n")) {
                    if (line.equals(""))
                        break;
                    headers.add(new HTTPHeader(line.split(": ")[0], line.split(": ")[1]));
                }
                return headers.toArray(new HTTPHeader[0]);
            }
        };
    }

    public interface ParsedHTTPResponse {
        String getHTTPVersion();
        int getStatusCode();
        String getStatusCodeAsString();
        String getPath();
        Map<String, String> getQuery();
        Object getStatusCodeAsEnum();
        String getBody();
        HTTPHeader[] getHeaders();
    }
}
