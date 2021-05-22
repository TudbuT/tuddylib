package tudbut.tools;

import de.tudbut.type.Stoppable;
import tudbut.obj.NotSupportedException;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPool implements Stoppable {
    private final Thread[] threads;
    private final Runnable[] toDo;
    private final Lock[] locks;
    private final Lock hasFree = new Lock();
    private final AtomicInteger freeThreads = new AtomicInteger();
    
    public ThreadPool(final int amount, final String name, final boolean enableCrashRecovery) {
        threads = new Thread[amount];
        toDo = new Runnable[amount];
        locks = new Lock[amount];
        for (int i = 0 ; i < locks.length ; i++) {
            locks[i] = new Lock();
            locks[i].lock();
        }
        updateFree();
    
        for (int i = 0; i < threads.length; i++) {
            final int threadID = i;
            threads[i] = new Thread(() -> {
                while (!isStopped()) {
                    freeThreads.getAndIncrement();
                    updateFree();
                    locks[threadID].waitHere();
                    locks[threadID].lock();
                    freeThreads.getAndDecrement();
                    updateFree();
                    
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
                }
            }, name + ":" + i);
            threads[i].start();
        }
    }
    
    private void updateFree() {
        if (freeThreads.get() == 0) {
            hasFree.lock();
        }
        else {
            hasFree.unlock();
        }
    }
    
    public void run(Runnable runnable) {
        boolean found = false;
        while (!found) {
            hasFree.waitHere();
            for (int i = 0; i < threads.length; i++) {
                if(locks[i].isLocked()) {
                    toDo[i] = runnable;
                    locks[i].unlock();
                    found = true;
                    break;
                }
            }
        }
    }
    
    public void start() throws NotSupportedException {
        throw new NotSupportedException();
    }
    
    public int available() {
        return freeThreads.get();
    }
    
    public boolean isBlocked() {
        return hasFree.isLocked();
    }
}
