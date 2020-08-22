package tudbut.global;

import de.tudbut.timer.Ticker;
import de.tudbut.tools.ThrowingRunnable;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class GlobalSyncQueue {
    private static final ArrayList<ThrowingRunnable> queue = new ArrayList<>();
    private static final ArrayList<ThrowingRunnable> immediateQueue = new ArrayList<>();
    private static final ArrayList<Object[]> timers = new ArrayList<>();
    private static int position;
    private static int immediatePosition = 0;

    static {
        initAndRunAsynchronously();
    }

    public static void add(ThrowingRunnable runnable) {
        queue.add(runnable);
    }

    public static void addImmediate(ThrowingRunnable runnable) {
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
            if (immediateQueue.size() > immediatePosition) {
                try {
                    immediateQueue.get(immediatePosition).run();
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                immediatePosition++;
                continue;
            }
            if (queue.size() > position) {
                try {
                    if(queue.get(position) != null)
                        queue.get(position).run();
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                position++;
            }
            if (immediateQueue.size() > immediatePosition) {
                try {
                    immediateQueue.get(immediatePosition).run();
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                immediatePosition++;
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
            if (immediateQueue.size() > immediatePosition) {
                try {
                    immediateQueue.get(immediatePosition).run();
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                immediatePosition++;
            }
        }
    }

    public static void initAndRunAsynchronously() {
        new Thread(GlobalSyncQueue::initAndRun, "GlobalSyncQueue").start();
    }
}
