package de.tudbut.net.http;

import java.nio.charset.StandardCharsets;

/**
 * Class to build HTTP responses
 */
public class HTTPResponseFactory {
    
    /**
     * Creates a HTTPResponse
     * @param responseCode {@link HTTPResponseCode}
     * @param body The body of the request
     * @param contentType {@link HTTPContentType}
     * @param headers {@link HTTPHeader} Headers for the response
     * @return The constructed {@link HTTPResponse}
     */
    public static HTTPResponse create(HTTPResponseCode responseCode, String body, HTTPContentType contentType, HTTPHeader... headers) {
        return create(responseCode, body, contentType.asHeaderString, headers);
    }
    /**
     * Creates a HTTPResponse
     * @param responseCode {@link HTTPResponseCode}
     * @param body The body of the request
     * @param contentType {@link HTTPContentType}
     * @param headers {@link HTTPHeader} Headers for the response
     * @return The constructed {@link HTTPResponse}
     */
    public static HTTPResponse create(HTTPResponseCode responseCode, String body, String contentType, HTTPHeader... headers) {
        return create(responseCode, body, new HTTPHeader("Content-Type", contentType), headers);
    }
    
    /**
     * Creates a HTTPResponse
     * @param responseCode {@link HTTPResponseCode}
     * @param body The body of the request
     * @param contentType content type
     * @param headers {@link HTTPHeader} Headers for the response
     * @return The constructed {@link HTTPResponse}
     */
    public static HTTPResponse create(HTTPResponseCode responseCode, String body, HTTPHeader contentType, HTTPHeader... headers) {
        StringBuilder builder = new StringBuilder();

        builder.append("HTTP/1.1 ").append(responseCode.asInt).append(" ").append(responseCode.name()).append("\r\n");
        builder.append(new HTTPHeader("Content-Type", contentType.value(), contentType.parameter())).append("\r\n");
        builder.append(new HTTPHeader("Content-Length", body.getBytes(StandardCharsets.ISO_8859_1).length + "")).append("\r\n");
        for (HTTPHeader header : headers) {
            builder.append(header.toString()).append("\r\n");
        }
        builder.append("\r\n");
        builder.append(body);
        
        return new HTTPResponse(builder.toString(), true);
    }
}
