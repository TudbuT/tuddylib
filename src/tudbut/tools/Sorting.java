package tudbut.tools;

import de.tudbut.tools.ExtendedMath;
import de.tudbut.tools.Tools;

import java.util.ArrayList;

public class Sorting {

    public static <T> T[] sort(T[] toSort, Sorter<T> sorter) throws Throwable {
        if(toSort.length == 0)
            return toSort;
        T[] nt = (T[]) ArrayGetter.newArray(toSort.length, toSort.getClass().getComponentType());

        sorter.reset();

        int max = (int) ExtendedMath.max(
                ArrayTools.convertToNative(
                        ArrayTools.<T, Double>getFromArray(toSort, t ->
                                (double) sorter.sort(t)
                        )
                )
        );

        sorter.reset();

        int min = (int) ExtendedMath.min(
                ArrayTools.convertToNative(
                        ArrayTools.<T, Double>getFromArray(toSort, t ->
                                (double) sorter.sort(t)
                        )
                )
        );

        sorter.reset();

        ArrayList<Integer> done = new ArrayList<>();

        int pos = 0;
        for (int i = min; i <= max; i++) {
            for (int j = 0; j < nt.length; j++) {
                if(sorter.sort(toSort[j]) == i) {
                    if(!done.contains(j)) {
                        nt[pos] = toSort[j];
                        done.add(j);
                        pos++;
                    }
                }
            }
            sorter.reset();
        }

        return nt;
    }

    public static <T> T[] sortSet(T[] toSort, Sorter<T> sorter) throws Throwable {
        T[] sorted = sort(toSort, sorter);
        Tools.copyArray(sorted, toSort, toSort.length);
        return sorted;
    }

    public interface Sorter<T> {
        int sort(T t) throws Throwable;

        default void reset() throws Throwable { }
    }
}
