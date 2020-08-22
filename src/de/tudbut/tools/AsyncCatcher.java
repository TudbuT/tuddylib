package de.tudbut.tools;

import java.util.concurrent.atomic.AtomicReference;

public interface AsyncCatcher {
    void run(Exception theException) throws Exception;
}
