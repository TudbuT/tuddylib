package de.tudbut.logger;

import java.sql.Time;
import java.util.Date;

public class DetailedLogger implements LoggerSink {
    String name;
    String caller;


    public DetailedLogger(String name) {
        this.name = name;
    }

    public DetailedLogger subChannel(String subChannel) {
        return new DetailedLogger(name + "] [" + subChannel);
    }

    private void set() {
        this.caller = Thread.currentThread().getStackTrace()[3].getClassName() + "." + Thread.currentThread().getStackTrace()[3].getMethodName() + "@" + Thread.currentThread().getStackTrace()[3].getLineNumber();
    }

    @Override
    public String getName() {
        return name;
    }

    public void info(String string) {
        set();
        String time = String.valueOf(new Date().getTime());
        System.out.println("[ " + new Time(new Date().getTime()) + "." + time.substring(time.length() - 3) + "  " + Thread.currentThread().getName() + "  " + caller + "  INFO ]  [" + this.name + "] " + string);
    }

    public void debug(String string) {
        set();
        String time = String.valueOf(new Date().getTime());
        System.out.println("[ " + new Time(new Date().getTime()) + "." + time.substring(time.length() - 3) + "  " + Thread.currentThread().getName() + "  " + caller + "  DEBUG ]  [" + this.name + "] " + string);
    }

    public void debug(Exception exception) {
        set();
        String time = String.valueOf(new Date().getTime());
        String substring = time.substring(time.length() - 3);
        System.out.println("[ " + new Time(new Date().getTime()) + "." + substring + "  " + Thread.currentThread().getName() + "  " + caller + "  DEBUG ]  [" + this.name + "] " + exception.getClass().getName());
        System.out.println("[ " + new Time(new Date().getTime()) + "." + substring + "  " + Thread.currentThread().getName() + "  " + caller + "  DEBUG ]  [" + this.name + "] " + exception.getMessage());
        for (StackTraceElement element : exception.getStackTrace())
            System.out.println("[ " + new Time(new Date().getTime()) + "." + substring + "  " + Thread.currentThread().getName() + "  " + caller + "  DEBUG ]  [" + this.name + "] " + element.toString());
    }

    public void warn(String string) {
        set();
        String time = String.valueOf(new Date().getTime());
        System.out.println("[ " + new Time(new Date().getTime()) + "." + time.substring(time.length() - 3) + "  " + Thread.currentThread().getName() + "  " + caller + "  WARN ]  [" + this.name + "] " + string);
    }

    public void error(String string) {
        set();
        String time = String.valueOf(new Date().getTime());
        System.out.println("[ " + new Time(new Date().getTime()) + "." + time.substring(time.length() - 3) + "  " + Thread.currentThread().getName() + "  " + caller + "  ERROR ]  [" + this.name + "] " + string);
    }

    public void error(Exception exception) {
        set();
        String time = String.valueOf(new Date().getTime());
        String substring = time.substring(time.length() - 3);
        System.out.println("[ " + new Time(new Date().getTime()) + "." + substring + "  " + Thread.currentThread().getName() + "  " + caller + "  ERROR ]  [" + this.name + "] " + exception.getClass().getName());
        System.out.println("[ " + new Time(new Date().getTime()) + "." + substring + "  " + Thread.currentThread().getName() + "  " + caller + "  ERROR ]  [" + this.name + "] " + exception.getMessage());
        for (StackTraceElement element : exception.getStackTrace())
            System.out.println("[ " + new Time(new Date().getTime()) + "." + substring + "  " + Thread.currentThread().getName() + "  " + caller + "  ERROR ]  [" + this.name + "] " + element.toString());
    }
}
