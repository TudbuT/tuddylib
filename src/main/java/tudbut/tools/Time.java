package tudbut.tools;

import java.util.Date;

public class Time {
    public static void wait(int ms, int ns) throws InterruptedException {
        int startedAt = new Date().toInstant().getNano();
        while (nanos(startedAt) / 10000000 < ns / 10000000);
        Thread.sleep(ms);
    }
    
    public static long nanos(int last) {
        return last + (Math.max(new Date().toInstant().getNano() - last, 0L));
    }
    
    public static String ydhmsString(long seconds) {
        String s = "";
        s += (seconds / 60 / 60 / 24 / 360) + ":";
        s += (seconds / 60 / 60 / 24 % 360) + ":";
        s += (seconds / 60 / 60 % 24) + ":";
        s += (seconds / 60 % 60) + ":";
        s += (seconds % 60);
        return s;
    }
    
    public static String[] ydhmsStrings(long seconds) {
        return ydhmsString(seconds).split(":");
    }
    
    public static String ydhms(long seconds) {
        String s = "";
        s += (seconds / 60 / 60 / 24 / 360) + "y ";
        s += (seconds / 60 / 60 / 24 % 360) + "d ";
        s += (seconds / 60 / 60 % 24) + "h ";
        s += (seconds / 60 % 60) + "m ";
        s += (seconds % 60) + "s";
        return s;
    }
}
