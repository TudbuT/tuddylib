package de.tudbut.net.http;

/**
 * Request types
 */
public enum HTTPRequestType {
    /**
     * Get request, default for browsers
     */
    GET,
    
    /**
     * Post request, common for APIs
     */
    POST,
    
    /**
     * Head request, asks the server to respond with the same headers
     * as a {@link #GET}, but without content. Commonly used in browsers
     * that use caching. <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/HEAD">More</a>
     */
    HEAD,
    
    /**
     * Put request, used for writing files to a server
     */
    PUT,
    
    /**
     * Patch request, used for changing files on a server.
     * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/PATCH">More</a>
     */
    PATCH,
    
    /**
     * Delete request, used for deleting files on a server.
     * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/DELETE">More</a>
     */
    DELETE,
    
    /**
     * Trace request, used for debugging
     */
    TRACE,
    
    /**
     * Options request, used for asking the server for allowed communication methods.
     * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/OPTIONS">More</a>
     */
    OPTIONS,
    
    /**
     * Connect request, used to indicate start of a two-way transfer.
     * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/CONNECT">More</a>
     */
    CONNECT,
    ;
}
