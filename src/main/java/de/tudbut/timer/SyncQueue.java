package de.tudbut.timer;

import de.tudbut.tools.ThrowingRunnable;
import tudbut.tools.Queue;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class SyncQueue {
    private final Queue<ThrowingRunnable> queue = new Queue<>();
    private final Queue<ThrowingRunnable> immediateQueue = new Queue<>();
    private final ArrayList<Object[]> timers = new ArrayList<>();
    
    {
        initAndRunAsynchronously();
    }
    
    public void add(ThrowingRunnable runnable) {
        if(runnable == null)
            throw new IllegalArgumentException();
        queue.add(runnable);
    }
    
    public void addImmediate(ThrowingRunnable runnable) {
        if(runnable == null)
            throw new IllegalArgumentException();
        immediateQueue.add(runnable);
    }
    
    public void addTimer(Ticker ticker, AtomicBoolean stop, int delay) {
        addTimer(new Ticker() {
            public int getDelay() {
                return delay;
            }
            
            public void run() {
                ticker.run();
            }
            
            public boolean doRun() {
                return !stop.get();
            }
        });
    }
    
    public void addTimer(Ticker ticker, int delay) {
        addTimer(new Ticker() {
            public int getDelay() {
                return delay;
            }
            
            public void run() {
                ticker.run();
            }
        });
    }
    
    public void addTimer(Ticker ticker) {
        timers.add(new Object[]{ticker, 0L});
    }
    
    private void initAndRun() {
        while (true) {
            if (immediateQueue.hasNext()) {
                try {
                    immediateQueue.next().run();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                continue;
            }
            if (queue.hasNext()) {
                try {
                    queue.next().run();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            if (immediateQueue.hasNext()) {
                try {
                    immediateQueue.next().run();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                continue;
            }
            // An enhanced loop will throw an exception, fuck you intellij for this shit suggestion
            //noinspection ForLoopReplaceableByForEach
            for (int i = 0; i < timers.size(); i++) {
                Object[] timer = timers.get(i);
                if (new Date().getTime() - ((Long) timer[1]) >= ((Ticker) timer[0]).getDelay()) {
                    if (((Ticker) timer[0]).doRun()) {
                        ((Ticker) timer[0]).run();
                        timer[1] = new Date().getTime();
                    }
                }
            }
            if (immediateQueue.hasNext()) {
                try {
                    immediateQueue.next().run();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
    
    public void initAndRunAsynchronously() {
        new Thread(this::initAndRun, "SyncQueue").start();
    }
}
