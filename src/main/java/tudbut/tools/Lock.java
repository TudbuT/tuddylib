package tudbut.tools;

import java.util.concurrent.atomic.AtomicInteger;

public class Lock {
    
    private static class Locker {
        
        public synchronized void lockHere() throws InterruptedException {
            wait();
        }
        public synchronized void lockHere(long timeout) throws InterruptedException {
            wait(timeout);
        }
    
        public synchronized void unlock() {
            notifyAll();
        }
    }
    
    private Locker locker = new Locker();
    private boolean b = false;
    private final AtomicInteger waiting = new AtomicInteger();
    private volatile boolean[] waiterLocker = null;
    
    public void waitHere() throws InterruptedException {
        if(b) {
            locker.lockHere();
        }
    }
    public void waitHere(long timeout) throws InterruptedException {
        if(b) {
            locker.lockHere(timeout);
        }
    }
    public void unlock() {
        if(b) {
            locker.unlock();
        }
        b = false;
    }
    public void lock() {
        b = true;
    }
    
    public void synchronize(int amount) throws InterruptedException {
        this.b = true;
        if(waiterLocker == null)
            waiterLocker = new boolean[amount];
        int i = waiting.get();
        waiting.getAndIncrement();
        locker.unlock();
        while (amount > waiting.get()) {
            locker.lockHere();
        }
        locker.unlock();
        boolean b;
        waiterLocker[i] = true;
        b = true;
        try {
            while (b) {
                b = false;
                for (int j = 0 ; j < waiterLocker.length ; j++) {
                    if (!waiterLocker[j]) {
                        b = true;
                        break;
                    }
                }
            }
        } catch (Exception ignored) { }
        Thread.sleep(1);
        waiting.getAndDecrement();
        waiterLocker = null;
        this.b = false;
    }
}
