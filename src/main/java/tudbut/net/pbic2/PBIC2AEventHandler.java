package tudbut.net.pbic2;

import de.tudbut.type.Stoppable;
import tudbut.io.AdaptiveSocketInputStream;
import tudbut.io.TypedInputStream;
import tudbut.obj.TLMap;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public final class PBIC2AEventHandler implements Stoppable {
    
    private final ArrayList<PBIC2> array = new ArrayList<>();
    private final TLMap<PBIC2, Integer> map = new TLMap<>();
    private final TLMap<PBIC2, PBIC2AListener> listeners = new TLMap<>();
    private final TLMap<PBIC2, InputStream> inputStreams = new TLMap<>();
    
    public void start(PBIC2 pbic2, PBIC2AListener listener) throws IOException {
        if(array.contains(pbic2))
            return;
        array.add(pbic2);
        listeners.set(pbic2, listener);
        inputStreams.set(pbic2, pbic2.getInput().getStream());
    }
    public void remove(PBIC2 pbic2) {
        if(!array.contains(pbic2))
            return;
        array.remove(pbic2);
        map.set(pbic2, null);
        listeners.set(pbic2, null);
        inputStreams.set(pbic2, null);
    }
    
    public PBIC2AEventHandler() {
        new Thread(this::run).start();
    }
    
    private void runOn(PBIC2 pbic2) throws IOException {
        Socket socket = pbic2.getSocket();
        InputStream stream = inputStreams.get(pbic2);
        TypedInputStream tis = pbic2.getInput();
        if (!socket.isConnected() || socket.isClosed()) {
            listeners.get(pbic2).onError(new PBIC2ADisconnect());
            remove(pbic2);
        }
        if(pbic2.isSSL()) {
            if (pbic2.getRealSocket().getInputStream().available() > 0) {
                listeners.get(pbic2).onMessage(tis.readString());
            }
        }
        else {
            if (map.get(pbic2) == null) {
                if (stream.available() >= 4)
                    map.set(pbic2, tis.readInt());
            }
            else if (stream.available() >= map.get(pbic2) * 2) {
                int i = map.get(pbic2);
                StringBuilder builder = new StringBuilder();
                for (int j = 0 ; j < i ; j++) {
                    builder.append(tis.readChar());
                }
                listeners.get(pbic2).onMessage(builder.toString());
                map.set(pbic2, null);
            }
        }
    }

    private void run() {
        while (!isStopped()) {
            ArrayList<PBIC2> array = this.array;
            try {
                for (int i = 0 ; i < array.size() ; i++) {
                    try {
                        runOn(array.get(i));
                    }
                    catch (Throwable e) {
                        listeners.get(array.get(i)).onError(e);
                    }
                }
            } catch (Exception ignored) { }
            try {
                Thread.sleep(50);
            }
            catch (InterruptedException ignored) { }
        }
    }
    
    private int readWithTimeout(TypedInputStream stream) throws IOException {
        try {
            return stream.read();
        } catch (SocketTimeoutException e) {
            // There is no data available.
            return -1;
        }
    }
}
