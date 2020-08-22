package de.tudbut.type;

import java.util.ArrayList;

public class IntArrayList extends ArrayList<Integer> {

    public int[] toIntArray() {
        Integer[] a = toArray(new Integer[0]);
        int[] b = new int[size()];

        for (int i = 0; i < size(); i++) {
            b[i] = a[i];
        }

        return b;
    }
}
