package de.tudbut.type;

import tudbut.tools.ArrayTools;

import java.util.ArrayList;

public class CharArrayList extends ArrayList<Character> {

    public char[] toCharArray() {
        Character[] a = ArrayTools.arrayFromList(this);
        char[] b = new char[size()];

        for (int i = 0; i < size(); i++) {
            b[i] = a[i];
        }

        return b;
    }
}
