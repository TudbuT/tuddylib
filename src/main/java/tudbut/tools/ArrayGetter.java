package tudbut.tools;

import java.lang.reflect.Array;

public class ArrayGetter {
    @SafeVarargs
    public static <T> T[] newGenericArray(int length, T... ts) {
        return newArray(length, (Class<T>) ts.getClass().getComponentType());
    }

    public static <T> T[] newArray(int length, Class<T> type) {
        return (T[]) Array.newInstance(type, length);
    }
}
