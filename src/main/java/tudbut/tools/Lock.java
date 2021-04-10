package tudbut.tools;

import java.util.Date;
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
    private int t = 0;
    private long ts = 0;
    private final AtomicInteger waiting = new AtomicInteger();
    private volatile boolean[] waiterLocker = null;
    
    public Lock() {
    
    }
    
    public Lock(boolean locked) {
        b = locked;
    }
    
    public long timeLeft() {
        updateLocked();
        return (ts + t) - new Date().getTime();
    }
    
    protected int checkTime(int timeout) {
        return b ? checkNegative(Math.min((int) ( t - ( new Date().getTime() - ts ) ), timeout <= 0 ? Integer.MAX_VALUE : timeout), timeout) : timeout;
    }
    
    protected int checkNegative(int i, int alt) {
        if(i <= 0)
            return alt;
        return i;
    }
    
    protected void updateLocked() {
        if(new Date().getTime() - ts >= t && ts != 0)
            b = false;
    }
    
    public void waitHere() {
        updateLocked();
        if(b) {
            try {
                locker.lockHere(checkTime(0));
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        updateLocked();
    }
    public void waitHere(int timeout) {
        updateLocked();
        if(b) {
            try {
                locker.lockHere(checkTime(timeout));
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        updateLocked();
    }
    public synchronized void unlock() {
        if(b) {
            locker.unlock();
        }
        b = false;
    }
    public synchronized void lock() {
        t = 0;
        ts = 0;
        b = true;
    }
    public synchronized void lock(int time) {
        if(time < 0)
            time = 0;
        b = true;
        t = time;
        ts = new Date().getTime();
    }
    
    public synchronized boolean isLocked() {
        updateLocked();
        return b;
    }
    
    public void synchronize(int amount) {
        this.b = true;
        if(waiterLocker == null)
            waiterLocker = new boolean[amount];
        int i = waiting.get();
        waiting.getAndIncrement();
        locker.unlock();
        while (amount > waiting.get()) {
            try {
                locker.lockHere();
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
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
        try {
            Thread.sleep(1);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        waiting.getAndDecrement();
        waiterLocker = null;
        this.b = false;
    }
}
