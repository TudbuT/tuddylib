package tudbut.net.http.serverimpl;

import tudbut.net.http.HTTPContentType;
import tudbut.net.http.HTTPResponse;
import tudbut.net.http.HTTPResponseCode;
import tudbut.net.http.HTTPResponseFactory;

public class Response extends RuntimeException {

    HTTPResponse response;
    
    public Response(HTTPResponseCode code, String body, HTTPContentType contentType) {
        response = HTTPResponseFactory.create(code, body, contentType);
    }
}
