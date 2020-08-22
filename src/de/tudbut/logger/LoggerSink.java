package de.tudbut.logger;

public interface LoggerSink {
    String getName();

    LoggerSink subChannel(String subChannel);

    void info(String string);

    void debug(String string);

    void debug(Exception exception);

    void warn(String string);

    void error(String string);

    void error(Exception exception);
}
