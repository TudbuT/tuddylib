package tudbut.global;

import tudbut.logger.DetailedLogger;
import tudbut.logger.EmptyLogger;
import tudbut.logger.Logger;
import tudbut.logger.LoggerSink;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class DebugStateManager {
    private static final ArrayList<AtomicReference<LoggerSink>> toBeUpdated = new ArrayList<>();
    private static final ArrayList<AtomicReference<LoggerSink>> dToBeUpdated = new ArrayList<>();
    private static final Logger debugLogger = new Logger("DEBUG");
    private static final DetailedLogger debugDLogger = new DetailedLogger("DEBUG");
    private static boolean debugEnabled = false;

    static {
        updateState();
    }

    public static void enableDebug() {
        debugEnabled = true;
        updateState();
    }

    public static void disableDebug() {
        debugEnabled = false;
        updateState();
    }

    public static boolean isDebugEnabled() {
        return debugEnabled;
    }

    public static AtomicBoolean debugEnabled() {
        return new AtomicBoolean(debugEnabled);
    }

    public static void updateState() {
        for (AtomicReference<LoggerSink> reference : toBeUpdated) {
            reference.set(getDebugLogger(reference.get().getName()));
        }
        for (AtomicReference<LoggerSink> reference : dToBeUpdated) {
            reference.set(getDebugDLogger(reference.get().getName()));
        }
    }

    public static LoggerSink getDebugLogger() {
        if (isDebugEnabled())
            return debugLogger;
        else
            return new EmptyLogger("DEBUG");
    }

    public static LoggerSink getDebugLogger(String name) {
        if (isDebugEnabled()) {
            if (name == null || name.equals("DEBUG")) {
                return debugLogger;
            }
            else
                return new Logger(name);
        }
        else
            return new EmptyLogger(name);
    }

    public static AtomicReference<LoggerSink> debugLoggerReference() {
        AtomicReference<LoggerSink> reference = new AtomicReference<>(getDebugLogger());
        toBeUpdated.add(reference);
        return reference;
    }

    public static AtomicReference<LoggerSink> debugLoggerReference(String name) {
        AtomicReference<LoggerSink> reference = new AtomicReference<>(getDebugLogger(name));
        toBeUpdated.add(reference);
        return reference;
    }

    public static LoggerSink getDebugDLogger() {
        if (isDebugEnabled())
            return debugDLogger;
        else
            return new EmptyLogger("DEBUG");
    }

    public static LoggerSink getDebugDLogger(String name) {
        if (isDebugEnabled()) {
            if (name == null || name.equals("DEBUG")) {
                return debugDLogger;
            }
            else
                return new DetailedLogger(name);
        }
        else
            return new EmptyLogger(name);
    }

    public static AtomicReference<LoggerSink> debugDLoggerReference() {
        AtomicReference<LoggerSink> reference = new AtomicReference<>(getDebugDLogger());
        dToBeUpdated.add(reference);
        return reference;
    }

    public static AtomicReference<LoggerSink> debugDLoggerReference(String name) {
        AtomicReference<LoggerSink> reference = new AtomicReference<>(getDebugDLogger(name));
        dToBeUpdated.add(reference);
        return reference;
    }
}
