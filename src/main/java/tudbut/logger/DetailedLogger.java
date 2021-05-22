package tudbut.logger;

import java.io.PrintStream;
import java.sql.Time;
import java.util.Date;

public class DetailedLogger implements LoggerSink {
    String name;
    public PrintStream out;
    
    {
        out = System.out;
    }


    public DetailedLogger(String name) {
        this.name = name;
    }

    public DetailedLogger subChannel(String subChannel) {
        PrintStream stream = out;
        return new DetailedLogger(name + "] [" + subChannel) {
            {
                out = stream;
            }
        };
    }

    @Override
    public String getName() {
        return name;
    }

    public void info(String string) {
        String time = String.valueOf(new Date().getTime());
        out.println("[ " + new Time(new Date().getTime()) + "." + time.substring(time.length() - 3) + "  " + Thread.currentThread().getName() + "  INFO ]  [" + this.name + "] " + string);
    }

    public void debug(String string) {
        String time = String.valueOf(new Date().getTime());
        out.println("[ " + new Time(new Date().getTime()) + "." + time.substring(time.length() - 3) + "  " + Thread.currentThread().getName() + "  DEBUG ]  [" + this.name + "] " + string);
    }

    public void warn(String string) {
        String time = String.valueOf(new Date().getTime());
        out.println("[ " + new Time(new Date().getTime()) + "." + time.substring(time.length() - 3) + "  " + Thread.currentThread().getName() + "  WARN ]  [" + this.name + "] " + string);
    }

    public void error(String string) {
        String time = String.valueOf(new Date().getTime());
        out.println("[ " + new Time(new Date().getTime()) + "." + time.substring(time.length() - 3) + "  " + Thread.currentThread().getName() + "  ERROR ]  [" + this.name + "] " + string);
    }
}
