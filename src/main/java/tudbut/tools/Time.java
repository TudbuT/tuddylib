package tudbut.tools;

public class Time {
    
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
    
    public static long predictTimeLeft(long start, float progress) {
        long current = System.currentTimeMillis();
        long diff = current - start;
        return (long) (diff / progress) + start - current;
    }
}
