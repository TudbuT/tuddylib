package tudbut.net.pbic2;

import de.tudbut.type.Stoppable;
import tudbut.obj.TLMap;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public final class PBIC2AEventHandler implements Stoppable {
    public void start(PBIC2 pbic2, PBIC2AListener listener) {
        if(array.contains(pbic2))
            return;
        array.add(pbic2);
        listeners.set(pbic2, listener);
    }
    public void remove(PBIC2 pbic2) {
        if(!array.contains(pbic2))
            return;
        array.remove(pbic2);
        map.set(pbic2, null);
        listeners.set(pbic2, null);
    }
    
    private final ArrayList<PBIC2> array = new ArrayList<>();
    private final TLMap<PBIC2, Integer> map = new TLMap<>();
    private final TLMap<PBIC2, PBIC2AListener> listeners = new TLMap<>();
    
    public PBIC2AEventHandler() {
        new Thread(this::run).start();
    }
    
    private void runOn(PBIC2 pbic2) throws IOException {
        Socket socket = pbic2.getSocket();
        if (socket.getInputStream().available() >= 4)
            if (map.get(pbic2) == null) {
                map.set(pbic2, pbic2.getInput().readInt());
            }
        if(map.get(pbic2) != null && socket.getInputStream().available() >= map.get(pbic2) * 2) {
            int i = map.get(pbic2);
            StringBuilder builder = new StringBuilder();
            for (int j = 0 ; j < i ; j++) {
                builder.append(pbic2.getInput().readChar());
            }
            listeners.get(pbic2).onMessage(builder.toString());
            map.set(pbic2, null);
        }
    }
    
    private void run() {
        while (!isStopped()) {
            for (int i = 0 ; i < array.size() ; i++) {
                try {
                    runOn(array.get(i));
                }
                catch (Throwable e) {
                    listeners.get(array.get(i)).onError(e);
                }
            }
            try {
                Thread.sleep(50);
            }
            catch (InterruptedException ignored) { }
        }
    }
}
