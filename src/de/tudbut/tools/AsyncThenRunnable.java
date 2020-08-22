package de.tudbut.tools;

public interface AsyncThenRunnable<T> {
    void run(T theValue) throws Exception;
}
