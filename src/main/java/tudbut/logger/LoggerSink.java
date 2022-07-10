package tudbut.logger;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.PrintStream;

public interface LoggerSink {
    String getName();

    LoggerSink subChannel(String subChannel);
    
    default PrintWriter infoAsWriter() {
        return new PrintWriter(new OutputStream() {
            String s = "";
    
            @Override
            public void write(int i) {
                if((char) i == '\n') {
                    info(s);
                    s = "";
                    return;
                }
                s += (char) i;
            }
        }, true);
    }
    default PrintStream infoAsStream() {
        return new PrintStream(new OutputStream() {
            String s = "";
            
            @Override
            public void write(int i) {
                if((char) i == '\n') {
                    info(s);
                    s = "";
                    return;
                }
                s += (char) i;
            }
        }, true);
    }
    
    default PrintWriter debugAsWriter() {
        return new PrintWriter(new OutputStream() {
            String s = "";
    
            @Override
            public void write(int i) {
                if((char) i == '\n') {
                    debug(s);
                    s = "";
                    return;
                }
                s += (char) i;
            }
        }, true);
    }
    default PrintStream debugAsStream() {
        return new PrintStream(new OutputStream() {
            String s = "";
    
            @Override
            public void write(int i) {
                if((char) i == '\n') {
                    debug(s);
                    s = "";
                    return;
                }
                s += (char) i;
            }
        }, true);
    }
    
    default PrintWriter warnAsWriter() {
        return new PrintWriter(new OutputStream() {
            String s = "";
    
            @Override
            public void write(int i) {
                if((char) i == '\n') {
                    warn(s);
                    s = "";
                    return;
                }
                s += (char) i;
            }
        }, true);
    }
    default PrintStream warnAsStream() {
        return new PrintStream(new OutputStream() {
            String s = "";
    
            @Override
            public void write(int i) {
                if((char) i == '\n') {
                    warn(s);
                    s = "";
                    return;
                }
                s += (char) i;
            }
        }, true);
    }
    
    default PrintWriter errorAsWriter() {
        return new PrintWriter(new OutputStream() {
            String s = "";
    
            @Override
            public void write(int i) {
                if((char) i == '\n') {
                    error(s);
                    s = "";
                    return;
                }
                s += (char) i;
            }
        }, true);
    }
    default PrintStream errorAsStream() {
        return new PrintStream(new OutputStream() {
            String s = "";
    
            @Override
            public void write(int i) {
                if((char) i == '\n') {
                    error(s);
                    s = "";
                    return;
                }
                s += (char) i;
            }
        }, true);
    }
    
    void info(String string);

    void debug(String string);

    default void debug(Throwable throwable) {
        throwable.printStackTrace(debugAsWriter());
    }

    void warn(String string);
    
    default void warn(Exception exception) {
        exception.printStackTrace(warnAsWriter());
    }
    
    void error(String string);

    default void error(Throwable throwable) {
        throwable.printStackTrace(errorAsWriter());
    }
}
