package tudbut.logger;

import java.io.*;
import java.sql.Time;
import java.util.Date;

public class Logger implements LoggerSink {
    String name;
    public PrintStream out;
    
    {
        out = System.out;
    }

    public Logger(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public DetailedLogger subChannel(String subChannel) {
        return new DetailedLogger(name + "] [" + subChannel);
    }

    public PrintWriter infoAsWriter() {
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
    
    public PrintWriter debugAsWriter() {
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
    
    public PrintWriter warnAsWriter() {
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
    
    public PrintWriter errorAsWriter() {
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
    
    public void info(String string) {
        out.println("[" + new Time(new Date().getTime()) + ".INFO] [" + this.name + "] " + string);
    }

    public void debug(String string) {
        out.println("[" + new Time(new Date().getTime()) + ".DEBUG] [" + this.name + "] " + string);
    }

    public void debug(Throwable throwable) {
        throwable.printStackTrace(debugAsWriter());
    }

    public void warn(String string) {
        out.println("[" + new Time(new Date().getTime()) + ".WARN] [" + this.name + "] " + string);
    }
    
    public void warn(Exception exception) {
        exception.printStackTrace(warnAsWriter());
    }

    public void error(String string) {
        out.println("[" + new Time(new Date().getTime()) + ".ERROR] [" + this.name + "] " + string);
    }

    public void error(Throwable throwable) {
        throwable.printStackTrace(errorAsWriter());
    }
}
