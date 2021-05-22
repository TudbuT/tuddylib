package tudbut.io;

import java.io.IOException;
import java.io.Reader;

public class LineReader {
    
    public static String readLineLF(Reader stream) throws IOException {
        StringBuilder buffer = new StringBuilder();
        char[] chars = new char[1];
        stream.read(chars);
        while (chars[0] != 0x0A) {
            stream.read(chars);
            buffer.append(chars[0]);
        }
        buffer.setLength(buffer.length()-1);
        buffer.trimToSize();
        return buffer.toString();
    }
    
    public static String readLineCRLForLF(Reader stream) throws IOException {
        StringBuilder buffer = new StringBuilder();
        char[] chars = new char[1];
        stream.read(chars);
        while (chars[0] != 0x0A) {
            stream.read(chars);
            buffer.append(chars[0]);
        }
        if(buffer.toString().endsWith("\r\n"))
            buffer.setLength(buffer.length()-1);
        buffer.trimToSize();
        return buffer.toString();
    }
    
    public static String readLineCRLF(Reader stream) throws IOException {
        StringBuilder buffer = new StringBuilder();
        char[] chars = new char[1];
        stream.read(chars);
        while (chars[0] != 0x0A) {
            buffer.append(chars[0]);
            stream.read(chars);
        }
        buffer.setLength(buffer.length()-1);
        buffer.trimToSize();
        return buffer.toString();
    }
}
