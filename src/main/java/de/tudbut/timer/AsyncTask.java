package de.tudbut.timer;

import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import static de.tudbut.tools.BetterJ.t;

public class AsyncTask<T> {
    private final AtomicReference<T> backValue = new AtomicReference<>();
    private Exception exception = null;
    private boolean isCatched = false;
    private AsyncCatcher catcher;
    private AsyncThenRunnable<T> thenRunnable;
    private boolean hasThenRunnable = false;
    private volatile boolean done = false;
    private long timeout = -1;
    private final long startTime = new Date().getTime();

    public AsyncTask(AsyncRunnable<T> runnable) {
        Thread runner = t(() -> {
            try {
                Thread.sleep(2);
            }
            catch (InterruptedException ignore) {
            }
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
                        System.exit(1);
                    }
                }
            }
            if (hasThenRunnable)
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
                            System.exit(1);
                        }
                    }
                }
            done = true;
        });
        Thread stopper = t(() -> {
            while (new Date().getTime() < startTime + timeout || timeout == -1) {
                try {
                    Thread.sleep(1);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            runner.stop();
            if (hasThenRunnable)
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
                            System.exit(1);
                        }
                    }
                }
            done = true;
        });
    }

    public void catchExceptions(AsyncCatcher catcher) {
        this.catcher = catcher;
        isCatched = true;
    }

    public void then(AsyncThenRunnable<T> runnable) {
        this.thenRunnable = runnable;
        hasThenRunnable = true;
    }

    public T waitForFinish() {
        while (!done);
        
        return backValue.get();
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
