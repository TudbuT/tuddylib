package de.tudbut.logger;

import java.sql.Time;
import java.util.Date;

public class GlobalLogger {
    public static void info(String string) {
        System.out.println("[" + new Time(new Date().getTime()) + " INFO] " + string);
    }

    public static void debug(String string) {
        System.out.println("[" + new Time(new Date().getTime()) + " DEBUG] " + string);
    }

    public static void debug(Exception exception) {
        System.out.println("[" + new Time(new Date().getTime()) + " DEBUG] " + exception.getClass().getName());
        System.out.println("[" + new Time(new Date().getTime()) + " DEBUG] " + exception.getMessage());
        for (StackTraceElement element : exception.getStackTrace())
            System.out.println("[" + new Time(new Date().getTime()) + " DEBUG] " + element.toString());
    }

    public static void warn(String string) {
        System.out.println("[" + new Time(new Date().getTime()) + " WARN] " + string);
    }

    public static void error(String string) {
        System.out.println("[" + new Time(new Date().getTime()) + " ERROR] " + string);
    }

    public static void error(Exception exception) {
        System.out.println("[" + new Time(new Date().getTime()) + " ERROR] " + exception.getClass().getName());
        System.out.println("[" + new Time(new Date().getTime()) + " ERROR] " + exception.getMessage());
        for (StackTraceElement element : exception.getStackTrace())
            System.out.println("[" + new Time(new Date().getTime()) + " ERROR] " + element.toString());
    }
}
