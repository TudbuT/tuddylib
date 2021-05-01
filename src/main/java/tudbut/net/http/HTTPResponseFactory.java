package tudbut.net.http;

import java.nio.charset.StandardCharsets;

/**
 * Class to build HTTP responses
 */
public class HTTPResponseFactory {
    
    /**
     * Creates a HTTPResponse
     * @param responseCode {@link ResponseCode}
     * @param body The body of the request
     * @param contentType {@link HTTPContentType}
     * @param headers {@link HTTPHeader} Headers for the response
     * @return The constructed {@link HTTPResponse}
     */
    public static HTTPResponse create(ResponseCode responseCode, String body, HTTPContentType contentType, HTTPHeader... headers) {
        StringBuilder builder = new StringBuilder();

        builder.append("HTTP/1.1 ").append(responseCode.asInt).append(" ").append(responseCode.name()).append("\r\n");
        builder.append(new HTTPHeader("Content-Type", contentType.asHeaderString).toString()).append("\r\n");
        builder.append(new HTTPHeader("Content-Length", String.valueOf(body.getBytes(StandardCharsets.ISO_8859_1).length)).toString()).append("\r\n");
        for (HTTPHeader header : headers) {
            builder.append(header.toString()).append("\r\n");
        }
        builder.append("\r\n");
        builder.append(body);


        return new HTTPResponse(builder.toString());
    }
}
