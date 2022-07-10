package tudbut.net.http;

import tudbut.obj.DoubleObject;
import tudbut.obj.DoubleTypedObject;
import tudbut.obj.TLMap;
import tudbut.tools.Lock;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * Utils for sending/receiving with HTTP
 */
public class HTTPUtils {
    public static String encodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, String.valueOf(StandardCharsets.UTF_8)).replace("+", "%20");
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Too old OS");
        }
    }
    public static String decodeUTF8(String s) {
        try {
            return URLDecoder.decode(s.replace("+", "%20"), String.valueOf(StandardCharsets.UTF_8));
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Too old OS");
        }
    }
    
    public static String rawToUtf8(String raw) {
        return new String(raw.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }
    
    public static String utf8ToRaw(String utf8) {
        return new String(utf8.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
    }
    
    public static HTTPServer.HTTPHandler createRateLimitingHandler(int rps, HTTPServer.HTTPHandler requestHandler) {
        TLMap<String, DoubleTypedObject<Integer, Lock>> map = new TLMap<>();
        return new HTTPServer.HTTPHandler() {
            @Override
            public void handle(HTTPServerRequest request) throws Exception {
                requestHandler.handle(request);
            }
    
            @Override
            public boolean accept(SocketAddress address) {
                String ip = ((InetSocketAddress) address).getHostString();
                DoubleTypedObject<Integer, Lock> user = map.get(ip);
                if(user == null)
                    map.set(ip, user = new DoubleTypedObject<>(0, new Lock()));
                user.o++;
                if (user.t.isLocked()) {
                    if (user.o > rps)
                        return false;
                }
                else {
                    user.t.lock(1000);
                    user.o = 1;
                }
                return requestHandler.accept(address);
            }
    
            @Override
            public void handleDeny(HTTPServerRequest request) throws Exception {
                requestHandler.handleDeny(request);
            }
    
            @Override
            public HTTPResponse onError(HTTPServerRequest request, HTTPResponse defaultResponse, Throwable theError) throws Exception {
                return requestHandler.onError(request, defaultResponse, theError);
            }
        };
    }
}
