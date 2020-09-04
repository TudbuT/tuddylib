package tudbut.obj;

import tudbut.tools.ArrayGetter;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class DoubleObject<T> {
    private final T t1;
    private final T t2;

    public DoubleObject(T in1, T in2) {
        t1 = in1;
        t2 = in2;
    }

    @SafeVarargs
    public final T[] get(T... ignore) {
        T[] ts = ArrayGetter.newGenericArray(2, ignore);
        ts[0] = t1;
        ts[1] = t2;
        return ts;
    }

    public T get1() {
        return t1;
    }

    public T get2() {
        return t2;
    }

    public DoubleObject<T> clone() {
        return new DoubleObject<>(t1, t2);
    }
}
