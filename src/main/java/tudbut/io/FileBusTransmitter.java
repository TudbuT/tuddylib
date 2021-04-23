package tudbut.io;

import de.tudbut.timer.AsyncCatcher;
import de.tudbut.type.Stoppable;
import tudbut.obj.NotSupportedException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Executor;

/**
 * Transmits a FileBus over a different InputStream. always use a separate instance of FileBus
 * to avoid losing data!
 */
public class FileBusTransmitter implements Stoppable {
    
    @Override
    public void start() throws NotSupportedException {
        throw new NotSupportedException();
    }
    
    public FileBusTransmitter(FileBus bus, InputStream i, OutputStream o, Executor e0, Executor e1, AsyncCatcher onError) {
        e0.execute(() -> runI(bus,i,onError));
        e1.execute(() -> runO(bus,o,onError));
    }
    
    private void runI(FileBus bus, InputStream i, AsyncCatcher onError) {
        boolean locked = false;
        boolean wait = false;
        while (!isStopped()) {
            try {
                if (i.available() == 0) {
                    if (!wait) {
                        wait = true;
                        Thread.sleep(1);
                        continue;
                    }
                    else if(locked) {
                        wait = false;
                        locked = false;
                        bus.stopWrite();
                    }
                }
                int input = i.read();
                if(input != -1) {
                    if(!locked) {
                        locked = true;
                        bus.startWrite();
                    }
                    bus.o.write(input);
                }
                else if(!wait) {
                    wait = true;
                    Thread.sleep(1);
                    continue;
                }
                else if(locked) {
                    wait = false;
                    locked = false;
                    bus.stopWrite();
                }
                wait = false;
            }
            catch (IOException | InterruptedException e) {
                try {
                    onError.run(e);
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
    
    private void runO(FileBus bus, OutputStream o, AsyncCatcher onError) {
        boolean locked = false;
        boolean wait = false;
        while (!isStopped()) {
            try {
                int input = bus.i.read();
                if(input != -1) {
                    o.write(input);
                }
                else if(!wait) {
                    wait = true;
                    Thread.sleep(1);
                    continue;
                }
                wait = false;
            }
            catch (IOException | InterruptedException e) {
                try {
                    onError.run(e);
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
}
