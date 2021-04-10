package tudbut.tools;

import java.util.ArrayList;
import java.util.Date;

public class Stopwatch {
    
    public Stopwatch() {
    
    }
    
    long sa = new Date().getTime();
    ArrayList<Long> laps = new ArrayList<>();
    
    public long lap() {
        long l = getPassedTime();
        laps.add(l);
        return l;
    }
    
    public long getLap(int idx) {
        return laps.get(idx);
    }
    
    public long getPassedTime() {
        return new Date().getTime() - sa;
    }
}
