package de.tudbut.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author TudbuT
 * @since 03 Oct 2021
 */

// Fuck BufferedReader, im making my own.
public class RawLineReader extends InputStream {
    
    private boolean isAtCR = false;
    private boolean readLineInvoked = false;
    
    private final InputStream stream;
    
    public RawLineReader(InputStream stream) {
        this.stream = stream;
    }
    
    @Override
    public synchronized int read() throws IOException {
        int i = stream.read();
        if(readLineInvoked && i == '\n' && isAtCR) {
            readLineInvoked = false;
            return read();
        }
        if(isAtCR) {
            isAtCR = false;
        }
        if(i == '\r') {
            isAtCR = true;
        }
        return i;
    }
    
    @Override
    public int available() throws IOException {
        return stream.available();
    }
    
    @Override
    public void close() throws IOException {
        stream.close();
    }
    
    public synchronized String readLine() throws IOException {
        String line = "";
        int i;
        boolean _isAtCR = isAtCR;
        while ((i = read()) != '\r' && (_isAtCR || i != '\n')) {
            if(i != '\n')
                line += (char) i;
        }
        readLineInvoked = true;
        return line;
    }
}
