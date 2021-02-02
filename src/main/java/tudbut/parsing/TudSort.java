package tudbut.parsing;

import de.tudbut.tools.Tools;
import tudbut.tools.ArrayGetter;
import tudbut.tools.ArrayTools;

public class TudSort {
    
    public static <T> T[] sort(T[] toSort, Sorter<T> sorter) {
        if(toSort.length == 0)
            return toSort;
        T[] nt = (T[]) ArrayGetter.newArray(toSort.length, toSort.getClass().getComponentType());
        
        int min = Integer.MIN_VALUE;
        for (int i = 0; i < toSort.length; i++) {
            int smallest = Integer.MAX_VALUE;
            int smallestIndex = 0;
            for (int j = 0; j < toSort.length; j++) {
                int id = sorter.get(toSort[j]);
                if(id <= smallest && id > min) {
                    smallest = id;
                    smallestIndex = j;
                }
            }
            nt[i] = toSort[smallestIndex];
            min = smallest;
        }
        
        return nt;
    }
    
    public static <T> T[] sortDouble(T[] toSort, SorterDouble<T> sorter) {
        if(toSort.length == 0)
            return toSort;
        T[] nt = (T[]) ArrayGetter.newArray(toSort.length, toSort.getClass().getComponentType());
        
        double min = Double.MIN_VALUE;
        for (int i = 0; i < toSort.length; i++) {
            double smallest = Double.MAX_VALUE;
            int smallestIndex = 0;
            for (int j = 0; j < toSort.length; j++) {
                double id = sorter.get(toSort[j]);
                if(id <= smallest && id > min) {
                    smallest = id;
                    smallestIndex = j;
                }
            }
            nt[i] = toSort[smallestIndex];
            min = smallest;
        }
        
        return nt;
    }
    
    public static <T> void sortSet(T[] toSort, Sorter<T> sorter) {
        T[] sorted = sort(toSort, sorter);
        Tools.copyArray(sorted, toSort, toSort.length);
    }
    
    public static <T> T find(T[] toFindIn, ArrayTools.Getter<T, Object> getter, Object o) throws Throwable {
        for (T t : toFindIn) {
            if (getter.get(t).equals(o))
                return t;
        }
        return null;
    }
    
    public interface Sorter<T> {
        int sort(T t);
    
        default int get(T t) {
            if(
                    sort(t) > 500000
            )
                return 500000 + 500000;
    
            if(
                    sort(t) < -500000
            )
                return -500000 + 500000;
    
            return
                    sort(t) + 500000;
        }
    }
    
    public interface SorterDouble<T> {
        double sort(T t);
        
        default double get(T t) {
            if(
                    sort(t) > 500000
            )
                return 500000 + 500000;
            
            if(
                    sort(t) < -500000
            )
                return -500000 + 500000;
            
            return
                    sort(t) + 500000;
        }
    }
}
