package tudbut.net.http;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Utils for sending/receiving with HTTP
 */
public class HTTPUtils {
    public static String encodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, String.valueOf(StandardCharsets.UTF_8));
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Too old OS");
        }
    }
    public static String decodeUTF8(String s) {
        try {
            return URLDecoder.decode(s, String.valueOf(StandardCharsets.UTF_8));
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
}
