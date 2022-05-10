package tudbut.io;


import de.tudbut.type.IntArrayList;
import tudbut.tools.Queue;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class AdaptiveSocketInputStream extends InputStream {

    public final InputStream parent;
    public final Socket socket;
    private final Queue<Integer> cache = new Queue<>();

    public AdaptiveSocketInputStream(Socket socket) throws IOException {
        this.socket = socket;
        this.parent = socket.getInputStream();
    }

    @Override
    public int read() throws IOException {
        if(cache.hasNext())
            return cache.next();
        else
            return parent.read();
    }

    @Override
    public int available() throws IOException {
        int timeout = socket.getSoTimeout();
        socket.setSoTimeout(1);
        try {
            while(true) cache.add(parent.read());
        } catch (IOException e) {
            Throwable throwable = e;
            // Expected!
            while (!(throwable instanceof SocketTimeoutException) && throwable != null) {
                throwable = throwable.getCause();
            }
            if (throwable == null)
                throw e;
        }
        socket.setSoTimeout(timeout);
        if(parent.available() != 0)
            return parent.available();
        return cache.size() + parent.available();
    }
}
