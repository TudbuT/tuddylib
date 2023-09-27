package de.tudbut.net.http.serverimpl;

import de.tudbut.net.http.HTTPContentType;
import de.tudbut.net.http.HTTPResponse;
import de.tudbut.net.http.HTTPResponseCode;
import de.tudbut.net.http.HTTPResponseFactory;

public class Response extends RuntimeException {

    HTTPResponse response;
    
    public Response(HTTPResponseCode code, String body, HTTPContentType contentType) {
        response = HTTPResponseFactory.create(code, body, contentType);
    }
}
