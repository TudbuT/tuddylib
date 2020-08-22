package de.tudbut.tools;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public interface AsyncRunnable<T> {
    void run(AtomicReference<T> backValue) throws Exception;
}
