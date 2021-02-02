package tudbut.logger;

import java.io.OutputStream;
import java.io.PrintWriter;

public interface LoggerSink {
    String getName();

    LoggerSink subChannel(String subChannel);
    
    default PrintWriter infoAsWriter() {
        return new PrintWriter(new OutputStream() {
            String s = "";
            
            @Override
            public void write(int i) {
                s += (char) i;
            }
            
            public void flush() {
                info(s);
            }
        }, true);
    }
    
    default PrintWriter debugAsWriter() {
        return new PrintWriter(new OutputStream() {
            String s = "";
            
            @Override
            public void write(int i) {
                s += (char) i;
            }
            
            public void flush() {
                debug(s);
            }
        }, true);
    }
    
    default PrintWriter warnAsWriter() {
        return new PrintWriter(new OutputStream() {
            String s = "";
            
            @Override
            public void write(int i) {
                s += (char) i;
            }
            
            public void flush() {
                warn(s);
            }
        }, true);
    }
    
    default PrintWriter errorAsWriter() {
        return new PrintWriter(new OutputStream() {
            String s = "";
            
            @Override
            public void write(int i) {
                s += (char) i;
            }
            
            public void flush() {
                warn(s);
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
