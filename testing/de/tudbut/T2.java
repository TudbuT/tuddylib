package de.tudbut;

import tudbut.tools.Sorting;

import java.util.Arrays;

public class T2 {

    public static void main(String[] args) {
        System.out.println(Arrays.toString(Sorting.sort(new String[]{"1234", "1", "12345789"}, (t1, t2) -> {
            return t2.length() - t1.length();
        })));
    }
}
