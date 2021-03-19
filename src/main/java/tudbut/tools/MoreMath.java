package tudbut.tools;

public class MoreMath {
    
    public static double wrap(double d, double min, double max) {
        max -= min;
        d -= min;
        d = d % max;
        d += min;
        if(min >= 0 && d < 0)
            d = max + d;
        return d;
    }
    public static long wrap(long d, long min, long max) {
        max -= min;
        d -= min;
        d = d % max;
        d += min;
        if(min >= 0 && d < 0)
            d = max + d;
        return d;
    }
    public static int wrap(int d, int min, int max) {
        max -= min;
        d -= min;
        d = d % max;
        d += min;
        if(min >= 0 && d < 0)
            d = max + d;
        return d;
    }
}
