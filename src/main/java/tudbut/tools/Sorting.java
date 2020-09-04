package tudbut.tools;

import de.tudbut.tools.ExtendedMath;
import de.tudbut.tools.Tools;

import java.util.ArrayList;
import java.util.Arrays;

public class Sorting {

    public static <T> T[] sort(T[] toSort, Sorter<T> sorter) throws Throwable {
        if(toSort.length == 0)
            return toSort;
        T[] nt = (T[]) ArrayGetter.newArray(toSort.length, toSort.getClass().getComponentType());
        int[] ids = new int[toSort.length];
        
        sorter.reset();
    
        for (int i = 0; i < toSort.length; i++) {
            ids[i] = sorter.sort(toSort[i]);
        }

        sorter.reset();

        int max = ExtendedMath.max(
                ArrayTools.convertToNative(
                        ArrayTools.getFromArray(toSort, sorter::sort)
                )
        );
    
        sorter.reset();
    
        int min = ExtendedMath.min(
                ArrayTools.convertToNative(
                        ArrayTools.getFromArray(toSort, sorter::sort)
                )
        );

        sorter.reset();
        
        int smallestStep = 1;
    
        for (int i = 0; i < toSort.length; i++) {
            for (int j = 0; j < toSort.length; j++) {
                int n = ExtendedMath.highestMinusLowest(ids[i], ids[j]);
                if(n < smallestStep && n != 0) {
                    smallestStep = n;
                }
            }
        }
        int arrayPos = 0;
    
        for (int i = min; i <= max; i+=smallestStep) {
            for (int j = 0; j < toSort.length; j++) {
                if(ids[j] == i) {
                    nt[arrayPos] = toSort[j];
                    arrayPos++;
                }
            }
        }

        return nt;
    }

    public static <T> void sortSet(T[] toSort, Sorter<T> sorter) throws Throwable {
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
        int sort(T t) throws Throwable;

        default void reset() throws Throwable { }
    }
}
