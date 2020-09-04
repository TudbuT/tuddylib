package de.tudbut.tools;

import java.util.concurrent.atomic.AtomicBoolean;

import static de.tudbut.tools.BetterJ.t;

public class Catcher {
    public Exception currentException;

    public void run(ThrowingRunnable runnable) {
        AtomicBoolean done = new AtomicBoolean(false);
        t(() -> {
            try {
                runnable.run();
            }
            catch (Exception e) {
                currentException = e;
            }
            done.set(true);
        });
        while (!done.get()) {
            try {
                Thread.sleep(0);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void throwNextHere() throws Exception {
        while (true) {
            if (currentException != null) {
                throw currentException;
            }
        }
    }
}
