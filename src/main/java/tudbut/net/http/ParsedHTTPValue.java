package tudbut.net.http;

import java.util.Map;

public interface ParsedHTTPValue {
    String getHTTPVersion();
    
    int getStatusCode();
    
    String getStatusCodeAsString();
    
    String getPath();
    
    Map<String, String> getQuery();
    
    Object getStatusCodeAsEnum();
    
    String getBody();
    
    HTTPHeader[] getHeaders();
}