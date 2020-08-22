package de.tudbut.timer;

public interface AsyncThenRunnable<T> {
    void run(T theValue) throws Exception;
}
