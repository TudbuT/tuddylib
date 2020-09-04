package de.tudbut.type;

import tudbut.tools.ArrayTools;

import java.util.ArrayList;

public class ShortArrayList extends ArrayList<Short> {

    public short[] toIntArray() {
        Short[] a = ArrayTools.arrayFromList(this);
        short[] b = new short[size()];

        for (int i = 0; i < size(); i++) {
            b[i] = a[i];
        }

        return b;
    }
}
