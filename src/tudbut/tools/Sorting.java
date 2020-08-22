package tudbut.tools;

import de.tudbut.tools.ExtendedMath;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Sorting {

    public static <T> T[] sort(T[] toSort, Sorter<T> sorter) {
        if(toSort.length == 0)
            return toSort;
        T[] nt = (T[]) ArrayGetter.newArray(toSort.length, toSort.getClass().getComponentType());

        int max = (int) ExtendedMath.max(
                ArrayTools.convertToNative(
                        ArrayTools.<T, Double>getFromArray(toSort, t0 ->
                                ExtendedMath.max(
                                        ArrayTools.convertToNative(
                                                ArrayTools.<T, Double>getFromArray(toSort, t1 ->
                                                        (double) sorter.sort(t0, t1)
                                                )
                                        )
                                )
                        )
                )
        );

        int min = (int) ExtendedMath.min(
                ArrayTools.convertToNative(
                        ArrayTools.<T, Double>getFromArray(toSort, t0 ->
                                ExtendedMath.min(
                                        ArrayTools.convertToNative(
                                                ArrayTools.<T, Double>getFromArray(toSort, t1 ->
                                                        (double) sorter.sort(t0, t1)
                                                )
                                        )
                                )
                        )
                )
        );

        ArrayList<Integer> done = new ArrayList<>();

        int pos = 0;
        for (int i = min; i <= max; i++) {
            for (int j = 0; j < nt.length-1; j++) {
                    if(sorter.sort(toSort[j], toSort[j + 1]) == i) {
                        if(!done.contains(j)) {
                            nt[pos] = toSort[j];
                            done.add(j);
                            pos++;
                        }
                    }
            }
            if(sorter.sort(toSort[toSort.length - 1], toSort[0]) == i) {
                if(!done.contains(toSort.length - 1)) {
                    nt[pos] = toSort[toSort.length - 1];
                    done.add(toSort.length - 1);
                    pos++;
                }
            }
        }

        return nt;
    }

    public interface Sorter<T> {
        int sort(T t1, T t2);
    }
}
