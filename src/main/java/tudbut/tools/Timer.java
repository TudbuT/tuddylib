package tudbut.tools;

import java.util.Date;

public class Timer {
    
    Lock lock = new Lock();
    
    int time;
    int timesRan = 0;
    long lastTimeTaken = 0;
    int needed = 0;
    
    long last;
    public Timer(int delay) {
        time = delay;
        last = new Date().getTime();
    }
    
    private void waitHere() {
        lock.waitHere();
        lock.lock(getDelayTime());
        last = new Date().getTime();
    }
    
    public int getTimesRan() {
        return timesRan;
    }
    
    public long getLastTimeTaken() {
        return lastTimeTaken;
    }
    
    public void run(Runnable runnable) {
        timesRan++;
        waitHere();
        try {
            runnable.run();
        } finally {
            lastTimeTaken = new Date().getTime() - last;
        }
    }
    
    public boolean isFastForward() {
        return needed > 0;
    }
    
    public boolean isSuperFastForward() {
        return needed > time;
    }
    
    private int getDelayTime() {
        long last = this.last;
        int time = this.time;
        long l = new Date().getTime();
        if (l - last > time) {
            needed += l - last - time;
        }
        if (needed > 0) {
            needed -= time - (l - last);
            if (needed > time * 2L) {
                return 1;
            }
            else {
                return time - needed;
            }
        }
        else
            needed = 0;
        return time;
    }
}
