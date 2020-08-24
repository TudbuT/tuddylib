package de.tudbut;

import tudbut.tools.Sorting;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class T2 {

    public static void main(String[] args) throws Throwable {
        Double[] a = {0d,1d,2d,3d,4d,5d,6d,7.15d,8d,9d,100000.2d,100000d,7.1d};
        System.out.println("x");
        Sorting.sortSet(a, integer -> {
            return (int) (integer / 0.01);
        });
        System.out.println(Arrays.toString(a));
    }
}
