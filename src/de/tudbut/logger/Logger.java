package de.tudbut.logger;

import java.sql.Time;
import java.util.Date;

public class Logger implements LoggerSink {
    String name = null;


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

    public void info(String string) {
        System.out.println("[" + new Time(new Date().getTime()) + " INFO] [" + this.name + "] " + string);
    }

    public void debug(String string) {
        System.out.println("[" + new Time(new Date().getTime()) + " DEBUG] [" + this.name + "] " + string);
    }

    public void debug(Exception exception) {
        System.out.println("[" + new Time(new Date().getTime()) + " DEBUG] [" + this.name + "] " + exception.getClass().getName());
        System.out.println("[" + new Time(new Date().getTime()) + " DEBUG] [" + this.name + "] " + exception.getMessage());
        for (StackTraceElement element : exception.getStackTrace())
            System.out.println("[" + new Time(new Date().getTime()) + " DEBUG] [" + this.name + "] " + element.toString());
    }

    public void warn(String string) {
        System.out.println("[" + new Time(new Date().getTime()) + " WARN] [" + this.name + "] " + string);
    }

    public void error(String string) {
        System.out.println("[" + new Time(new Date().getTime()) + " ERROR] [" + this.name + "] " + string);
    }

    public void error(Exception exception) {
        System.out.println("[" + new Time(new Date().getTime()) + " ERROR] [" + this.name + "] " + exception.getClass().getName());
        System.out.println("[" + new Time(new Date().getTime()) + " ERROR] [" + this.name + "] " + exception.getMessage());
        for (StackTraceElement element : exception.getStackTrace())
            System.out.println("[" + new Time(new Date().getTime()) + " ERROR] [" + this.name + "] " + element.toString());
    }
}
