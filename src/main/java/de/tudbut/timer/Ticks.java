package de.tudbut.timer;

import tudbut.logger.Logger;
import tudbut.global.GlobalSyncQueue;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class Ticks {
    private static final Logger logger = new Logger("Ticker");

    public static long perSecound(int count) {
        return 1000 / count;
    }

    public static void onTick(int TPS, Ticker ticker) {
        try {
            int waitTicks = 1000 / TPS;
            long ticksPassed = 0;

            while (true) {
                long sAt = new Date().getTime();
                ticker.run();
                ticksPassed++;
                try {
                    Thread.sleep(waitTicks - (new Date().getTime() - sAt));
                }
                catch (Exception ignore) {
                }
            }
        }
        catch (Exception e) {
            logger.error(e);
        }
    }

    public static void inQueue(int TPS, Ticker ticker, AtomicBoolean stop, SyncQueue queue) {
        try {
            int waitTicks = 1000 / TPS;

            queue.addTimer(ticker, stop, waitTicks);
        }
        catch (Exception e) {
            logger.error(e);
        }
    }

    public static void inQueue(int TPS, Ticker ticker, AtomicBoolean stop) {
        try {
            int waitTicks = 1000 / TPS;

            GlobalSyncQueue.addTimer(ticker, stop, waitTicks);
        }
        catch (Exception e) {
            logger.error(e);
        }
    }
}
