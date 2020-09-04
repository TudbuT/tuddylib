package de.tudbut.handling;

import de.tudbut.io.StreamWriter;
import de.tudbut.logger.GlobalLogger;
import de.tudbut.logger.LoggerSink;
import de.tudbut.tools.FileRW;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class ExceptionHandler {
    private static final ArrayList<ExceptionHandler> instances = new ArrayList<>();
    private final OutputType type;
    private LoggerSink logger;
    private StreamWriter stream;
    private String dirPath;
    private ExceptionHandler(LoggerSink logger) {
        type = OutputType.LOGGER;
        this.logger = logger;
    }


    private ExceptionHandler(StreamWriter writer) {
        type = OutputType.STREAM;
        this.stream = writer;
    }

    private ExceptionHandler(String dirPath) {
        type = OutputType.FILE;
        this.dirPath = dirPath;
    }

    private ExceptionHandler() {
        type = OutputType.GLOBAL_LOGGER;
    }

    public static ExceptionHandler get(LoggerSink logger) {
        for (ExceptionHandler handler : instances) {
            if (handler.type == OutputType.LOGGER) {
                if (handler.logger == logger || handler.logger.getName().equals(logger.getName())) {
                    return handler;
                }
            }
        }

        ExceptionHandler i = new ExceptionHandler(logger);
        instances.add(i);
        return i;
    }

    public static ExceptionHandler get(String dirPath) {
        for (ExceptionHandler handler : instances) {
            if (handler.type == OutputType.FILE) {
                if (handler.dirPath.equals(dirPath)) {
                    return handler;
                }
            }
        }

        ExceptionHandler i = new ExceptionHandler(dirPath);
        instances.add(i);
        return i;
    }

    public static ExceptionHandler get(StreamWriter stream) {
        for (ExceptionHandler handler : instances) {
            if (handler.type == OutputType.STREAM) {
                if (handler.stream.equals(stream)) {
                    return handler;
                }
            }
        }

        ExceptionHandler i = new ExceptionHandler(stream);
        instances.add(i);
        return i;
    }

    public static ExceptionHandler get() {
        for (ExceptionHandler handler : instances) {
            if (handler.type == OutputType.GLOBAL_LOGGER) {
                return handler;
            }
        }

        ExceptionHandler i = new ExceptionHandler();
        instances.add(i);
        return i;
    }

    public void handle(Exception e) throws IOException {
        String format = "" +
                        "= = = = = EXCEPTION REPORT = = = = =\n" +
                        "  from %DATE%\n" +
                        "  at   %TIME%\n" +
                        "  in   %APP%\n" +
                        "  \n" +
                        "  EXCEPTION:\n" +
                        "%EXCEP%" +
                        "  \n" +
                        "  APPLICATION INFO:\n" +
                        "    Name: %APPNAME%\n" +
                        "    Hash: %APPHASH%\n" +
                        "    File: %APPFILE%";

        String exception = "";
        for (int i = 0; i < e.getStackTrace().length; i++) {
            exception += "    " + e.getStackTrace()[i].toString() + "\n";
        }

        format = format.replaceAll("%EXCEP%", exception);

        switch (type) {
            case GLOBAL_LOGGER:
                GlobalLogger.error(format);
                break;
            case LOGGER:
                logger.error(format);
                break;
            case STREAM:
                stream.writeChars(format.toCharArray());
                break;
            case FILE:
                new FileRW(dirPath + "/EXCEPTION__" + new Date().toString().replaceAll(":", "") + "_" + new Date().getTime())
                        .setContent(format)
                ;
        }
    }

    private enum OutputType {
        LOGGER,
        GLOBAL_LOGGER,
        FILE,
        STREAM
    }
}
