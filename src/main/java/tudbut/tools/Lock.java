package tudbut.tools;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Helper for synchronization and timing
 */
public class Lock {
    
    private final Locker locker = new Locker();
    private boolean locked = false;
    private int t = 0;
    private long ts = 0;
    private final AtomicInteger waiting = new AtomicInteger();
    private volatile boolean[] waiterLocker = null;
    
    /**
     * Object to handle thread locking
     */
    private static class Locker {
    
        /**
         * Make the thread wait until {@link Locker#unlock()} is called
         * @throws InterruptedException Inherited from {@link Object#wait}
         */
        public synchronized void lockHere() throws InterruptedException {
            wait();
        }
        
        /**
         * Make the thread wait until {@link Locker#unlock()} is called or the timeout runs out
         * @throws InterruptedException Inherited from {@link Object#wait}
         * @param timeout Maximal wait time
         */
        public synchronized void lockHere(long timeout) throws InterruptedException {
            wait(timeout);
        }
    
        /**
         * Stop locking
         */
        public synchronized void unlock() {
            notifyAll();
        }
    }
    
    /**
     * Creates a Lock without default state
     */
    public Lock() {
    
    }
    
    /**
     * Creates a Lock with default state
     * @param locked Default state
     */
    public Lock(boolean locked) {
        this.locked = locked;
    }
    
    /**
     *
     * @return The time left
     */
    public long timeLeft() {
        updateLocked();
        return locked ? (ts + t) - new Date().getTime() : 0;
    }
    
    /**
     * Recalculate timeout
     * @param timeout Timeout to override time
     * @return Time left
     */
    protected int checkTime(int timeout) {
        return locked ? checkNegative(Math.min((int) (t - (new Date().getTime() - ts ) ), timeout <= 0 ? Integer.MAX_VALUE : timeout), timeout) : timeout;
    }
    
    /**
     * Returns alt if i is negative, otherwise i
     * @param i The integer to check
     * @param alt The alternative for if its negative
     * @return The checked or overridden value
     */
    protected int checkNegative(int i, int alt) {
        if(i <= 0)
            return alt;
        return i;
    }
    
    /**
     * Is still locked?
     */
    protected void updateLocked() {
        if(new Date().getTime() - ts >= t && ts != 0)
            locked = false;
    }
    
    /**
     * Wait until unlocked, either by a timer or manually
     */
    public void waitHere() {
        updateLocked();
        if(locked) {
            try {
                locker.lockHere(checkTime(0));
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        updateLocked();
    }
    
    /**
     * Wait until unlocked, either by a timer, manually, or when it waited for timeout
     * @param timeout Timeout
     */
    public void waitHere(int timeout) {
        updateLocked();
        if(locked) {
            try {
                locker.lockHere(checkTime(timeout));
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        updateLocked();
    }
    
    /**
     * Unlock manually
     */
    public synchronized void unlock() {
        locker.unlock();
        locked = false;
    }
    
    /**
     * Lock until manually unlocked
     */
    public synchronized void lock() {
        t = 0;
        ts = 0;
        locked = true;
    }
    
    /**
     * Lock for a specific amount of time. Timer is passive.
     * @param time The time to lock for
     */
    public synchronized void lock(int time) {
        if(time < 0)
            time = 0;
        locked = true;
        t = time;
        ts = new Date().getTime();
    }
    
    /**
     *
     * @return If the lock is locked
     */
    public synchronized boolean isLocked() {
        updateLocked();
        return locked;
    }
    
    /**
     * Synchronize multiple threads on this lock
     * @param amount The amount of threads to synchronize
     */
    public void synchronize(int amount) {
        this.locked = true;
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
        this.locked = false;
    }
}
