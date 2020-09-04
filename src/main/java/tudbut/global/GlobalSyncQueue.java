package tudbut.global;

import com.sun.istack.internal.NotNull;
import de.tudbut.timer.Ticker;
import de.tudbut.tools.ThrowingRunnable;
import tudbut.tools.Queue;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class GlobalSyncQueue {
    private static final Queue<ThrowingRunnable> queue = new Queue<>();
    private static final Queue<ThrowingRunnable> immediateQueue = new Queue<>();
    private static final ArrayList<Object[]> timers = new ArrayList<>();

    static {
        initAndRunAsynchronously();
    }

    public static void add(ThrowingRunnable runnable) {
        if(runnable == null)
            throw new IllegalArgumentException();
        queue.add(runnable);
    }

    public static void addImmediate(ThrowingRunnable runnable) {
        if(runnable == null)
            throw new IllegalArgumentException();
        immediateQueue.add(runnable);
    }

    public static void addTimer(Ticker ticker, AtomicBoolean stop, int delay) {
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

    public static void addTimer(Ticker ticker, int delay) {
        addTimer(new Ticker() {
            public int getDelay() {
                return delay;
            }

            public void run() {
                ticker.run();
            }
        });
    }

    public static void addTimer(Ticker ticker) {
        timers.add(new Object[]{ticker, 0L});
    }

    private static void initAndRun() {
        while (true) {
            if (immediateQueue.hasNext()) {
                try {
                    immediateQueue.next().run();
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                continue;
            }
            if (queue.hasNext()) {
                try {
                    queue.next().run();
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            if (immediateQueue.hasNext()) {
                try {
                    immediateQueue.next().run();
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                continue;
            }
            // An enhanced loop will throw an exception, fuck you intellij for this shit suggestion
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
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                continue;
            }
        }
    }

    public static void initAndRunAsynchronously() {
        new Thread(GlobalSyncQueue::initAndRun, "GlobalSyncQueue").start();
    }
}
