package de.tudbut.net.http;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * {@link HTTPResponse#parse}
 * {@link HTTPServerRequest#parse}
 */
public interface ParsedHTTPValue {
    
    /**
     * @return The version used to send the request/response
     */
    String getHTTPVersion();
    
    /**
     * @return The status code
     */
    int getStatusCode();
    
    /**
     * @return The status code
     */
    String getStatusCodeAsString();
    
    /**
     * @return The path. Empty if a response
     */
    String getPath();
    
    /**
     * @return The query. Empty if a response
     */
    Map<String, String> getQuery();
    
    /**
     * @return The status code
     */
    Object getStatusCodeAsEnum();
    
    /**
     * @return The body of the request
     */
    String getBodyRaw();
    
    /**
     * @return The body of the request as bytes (raw)
     */
    default byte[] getBodyBytes() {
        return getBodyRaw().getBytes(StandardCharsets.ISO_8859_1);
    }
    
    /**
     * @return The body of the request as UTF-8
     */
    default String getBody() {
        return HTTPUtils.rawToUtf8(getBodyRaw());
    }
    
    /**
     * @return The headers of the request
     */
    HTTPHeader[] getHeaders();
}