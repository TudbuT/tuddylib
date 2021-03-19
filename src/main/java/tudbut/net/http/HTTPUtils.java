package tudbut.net.http;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class HTTPUtils {
    public static String encodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException impossible) {
            throw new RuntimeException(impossible);
        }
    }
    public static String decodeUTF8(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException impossible) {
            throw new RuntimeException(impossible);
        }
    }
}
