package tudbut.tools;

import de.tudbut.type.Stoppable;
import tudbut.obj.NotSupportedException;

import java.util.Arrays;

public class ThreadPool implements Stoppable {
    private final Thread[] threads;
    private final boolean[] available;
    private final Runnable[] toDo;
    private final boolean[] shouldRun;
    
    public ThreadPool(final int amount, final String name, final boolean enableCrashRecovery) {
        threads = new Thread[amount];
        toDo = new Runnable[amount];
        available = new boolean[amount];
        Arrays.fill(available, true);
        shouldRun = new boolean[amount];
    
        for (int i = 0; i < threads.length; i++) {
            final int threadID = i;
            threads[i] = new Thread(() -> {
                while (!isStopped()) {
                    while (!shouldRun[threadID]) {
                        try {
                            //noinspection BusyWait
                            Thread.sleep(0, 100000);
                        } catch (InterruptedException ignore) { }
                    }
                    shouldRun[threadID] = false;
                    
                    try {
                        toDo[threadID].run();
                    } catch (Throwable e) {
                        if (enableCrashRecovery) {
                            new RuntimeException("Thread crash: " + threads[threadID].getName() + "!", e).fillInStackTrace().printStackTrace();
                            System.err.println("Thread recovered: " + threads[threadID].getName() + ".");
                        } else {
                            throw new RuntimeException("Thread crash: " + threads[threadID].getName() + "!", e);
                        }
                    }
                    available[threadID] = true;
                }
            }, name + ":" + i);
            threads[i].start();
        }
    }
    
    public synchronized void run(Runnable runnable) {
        boolean found = false;
        while (!found) {
            for (int i = 0; i < threads.length; i++) {
                if(available[i]) {
                    available[i] = false;
                    found = true;
                    toDo[i] = runnable;
                    shouldRun[i] = true;
                    break;
                }
            }
        }
    }
    
    public void start() throws NotSupportedException {
        throw new NotSupportedException();
    }
}
