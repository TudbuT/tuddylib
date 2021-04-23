package tudbut.net.http;

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
    String getBody();
    
    /**
     * @return The headers of the request
     */
    HTTPHeader[] getHeaders();
}