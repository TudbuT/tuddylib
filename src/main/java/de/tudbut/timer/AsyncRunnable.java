package de.tudbut.timer;

import java.util.concurrent.atomic.AtomicReference;

public interface AsyncRunnable<T> {
    T run() throws Exception;
}
