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
                        ArrayTools.<T, Double>getFromArray(toSort, t ->
                                (double) sorter.sort(t)
                        )
                )
        );

        int min = (int) ExtendedMath.min(
                ArrayTools.convertToNative(
                        ArrayTools.<T, Double>getFromArray(toSort, t ->
                                (double) sorter.sort(t)
                        )
                )
        );

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
        }

        return nt;
    }

    public interface Sorter<T> {
        int sort(T t);
    }
}
