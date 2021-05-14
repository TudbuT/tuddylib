package de.tudbut.timer;

import tudbut.tools.Lock;

import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import static de.tudbut.tools.BetterJ.t;

public class AsyncTask<T> {
    private final AtomicReference<T> backValue = new AtomicReference<>();
    private Exception exception = null;
    private boolean isCatched = false;
    private AsyncCatcher catcher;
    private AsyncThenRunnable<T> thenRunnable = null;
    
    // Has the program finished? (Useful to know if it hit the timeout or exited.)
    private volatile boolean done = false;
    // We want efficiency, don't use a while loop to wait!
    private final Lock threadLock = new Lock();
    
    // When to force termination
    private long timeout = -1;
    // Start of the execution
    private long startTime = new Date().getTime();

    public AsyncTask(AsyncRunnable<T> runnable) {
        threadLock.lock();
        Lock stopperLock = new Lock(true);
        Lock sLock = new Lock(true);
        Thread runner = t(() -> {
            startTime = new Date().getTime();
            stopperLock.waitHere();
            sLock.unlock();
    
            startTime = new Date().getTime();
            try {
                backValue.set(runnable.run());
            }
            catch (Exception e) {
                exception = e;
                if (!isCatched)
                    exception.printStackTrace();
                else {
                    try {
                        catcher.run(exception);
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
    
            done = true;
            done();
        });
        Thread stopper = t(() -> {
            while (sLock.isLocked())
                stopperLock.unlock();
            while ((new Date().getTime() < startTime + timeout || timeout == -1) && !done) {
                threadLock.waitHere(1);
            }
            if(!done) {
                runner.stop();
                done();
            }
        });
    }
    
    private void done() {
        if (thenRunnable != null) {
            try {
                thenRunnable.run(backValue.get());
            }
            catch (Exception e) {
                exception = e;
                if (!isCatched)
                    exception.printStackTrace();
                else {
                    try {
                        catcher.run(exception);
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        done = true;
        threadLock.unlock();
    }

    public void catchExceptions(AsyncCatcher catcher) {
        this.catcher = catcher;
        isCatched = true;
    }

    public void then(AsyncThenRunnable<T> runnable) {
        this.thenRunnable = runnable;
    }

    public T waitForFinish(int waitTimeout) {
        threadLock.waitHere(waitTimeout);
    
        return backValue.get();
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
