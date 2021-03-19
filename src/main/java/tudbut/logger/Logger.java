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

    public Logger subChannel(String subChannel) {
        PrintStream stream = out;
        return new Logger(name + "] [" + subChannel) {
            {
                out = stream;
            }
        };
    }

    public PrintWriter infoAsWriter() {
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
    public PrintStream infoAsStream() {
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
    
    public PrintWriter debugAsWriter() {
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
    public PrintStream debugAsStream() {
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
    
    public PrintWriter warnAsWriter() {
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
    public PrintStream warnAsStream() {
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
    
    public PrintWriter errorAsWriter() {
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
    public PrintStream errorAsStream() {
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
    
    public void info(String string) {
        out.println("[" + new Time(new Date().getTime()) + " INFO] [" + this.name + "] " + string);
    }

    public void debug(String string) {
        out.println("[" + new Time(new Date().getTime()) + " DEBUG] [" + this.name + "] " + string);
    }

    public void debug(Throwable throwable) {
        throwable.printStackTrace(debugAsWriter());
    }

    public void warn(String string) {
        out.println("[" + new Time(new Date().getTime()) + " WARN] [" + this.name + "] " + string);
    }
    
    public void warn(Exception exception) {
        exception.printStackTrace(warnAsWriter());
    }

    public void error(String string) {
        out.println("[" + new Time(new Date().getTime()) + " ERROR] [" + this.name + "] " + string);
    }

    public void error(Throwable throwable) {
        throwable.printStackTrace(errorAsWriter());
    }
}
