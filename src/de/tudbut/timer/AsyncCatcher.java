package de.tudbut.timer;

public interface AsyncCatcher {
    void run(Exception theException) throws Exception;
}
