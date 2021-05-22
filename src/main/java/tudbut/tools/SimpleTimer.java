package tudbut.tools;

import java.util.Date;

public class SimpleTimer {
    
    private final Lock lock = new Lock();
    
    private final int time;
    private int timesRan = 0;
    private long lastTimeTaken = 0;
    
    private long last;
    public SimpleTimer(int delay) {
        time = delay;
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
    
    private int getDelayTime() {
        return time;
    }
}
