package tudbut.tools;

import tudbut.obj.NotSupportedException;

import java.util.Date;

public class SimpleTimer {
    
    Lock lock = new Lock();
    
    int time;
    int timesRan = 0;
    long lastTimeTaken = 0;
    
    long last;
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
