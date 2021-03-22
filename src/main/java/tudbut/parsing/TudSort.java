package tudbut.parsing;

import de.tudbut.tools.Tools;
import tudbut.tools.ArrayGetter;
import tudbut.tools.ArrayTools;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TudSort {
    
    public static <T> T[] sort(T[] toSort, Sorter<T> sorter) {
        return sort(toSort, sorter, false);
    }
    
    public static <T> T[] sort(T[] toSort, Sorter<T> sorter, boolean reverse) {
        if(toSort.length == 0)
            return toSort;
        T[] nt = (T[]) ArrayGetter.newArray(toSort.length, toSort.getClass().getComponentType());
        
        boolean[] indexes = new boolean[toSort.length];
        for (int i = 0; i < toSort.length; i++) {
            long smallest = Long.MAX_VALUE;
            int smallestIndex = 0;
            for (int j = 0; j < toSort.length; j++) {
                long id = sorter.get(toSort[j]);
                if(id <= smallest && !indexes[j]) {
                    smallest = id;
                    smallestIndex = j;
                }
            }
            nt[i] = toSort[smallestIndex];
            indexes[smallestIndex] = true;
        }
        
        if(reverse) {
            List<T> list = Arrays.asList(nt);
            Collections.reverse(list);
            return list.toArray(toSort);
        }
        
        return nt;
    }
    
    public static <T> T[] sortDouble(T[] toSort, SorterDouble<T> sorter) {
        if(toSort.length == 0)
            return toSort;
        T[] nt = (T[]) ArrayGetter.newArray(toSort.length, toSort.getClass().getComponentType());
    
        boolean[] indexes = new boolean[toSort.length];
        for (int i = 0; i < toSort.length; i++) {
            double smallest = Integer.MAX_VALUE;
            int smallestIndex = 0;
            for (int j = 0; j < toSort.length; j++) {
                double id = sorter.get(toSort[j]);
                if(id <= smallest && !indexes[j]) {
                    smallest = id;
                    smallestIndex = j;
                }
            }
            nt[i] = toSort[smallestIndex];
            indexes[smallestIndex] = true;
        }
    
        return nt;
    }
    
    public static <T> void sortSet(T[] toSort, Sorter<T> sorter) {
        T[] sorted = sort(toSort, sorter);
        Tools.copyArray(sorted, toSort, toSort.length);
    }
    
    public static <T> T find(T[] toFindIn, ArrayTools.Getter<T, Object> getter, Object o) {
        for (T t : toFindIn) {
            try {
                if (getter.get(t).equals(o))
                    return t;
            }
            catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }
        return null;
    }
    
    public interface Sorter<T> {
        long sort(T t);
    
        default long get(T t) {
            return sort(t);
        }
    }
    
    public interface SorterDouble<T> {
        double sort(T t);
        
        default double get(T t) {
            return sort(t);
        }
    }
}
