package de.tudbut.timer;

public interface Ticker {
    default int getDelay() {
        return 0;
    }

    void run();

    default boolean doRun() {
        return true;
    }
}
