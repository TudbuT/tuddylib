package de.tudbut.timer;

public interface AsyncRunnable<T> {
    T run() throws Exception;
}
