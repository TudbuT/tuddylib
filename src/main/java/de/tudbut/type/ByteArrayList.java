package de.tudbut.type;

import tudbut.tools.ArrayTools;

import java.util.ArrayList;

public class ByteArrayList extends ArrayList<Byte> {

    public byte[] toByteArray() {
        Byte[] a = ArrayTools.arrayFromList(this);
        byte[] b = new byte[size()];

        for (int i = 0; i < size(); i++) {
            b[i] = a[i];
        }

        return b;
    }
}
