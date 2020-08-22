package tudbut.tools;

import java.util.List;

public class ArrayTools {
    public static <T> T[] replace(T[] array, T repl, T with) { array=array.clone();for (int i=0;i<array.length;i++){if(array[i]!=null){if(array[i].equals(repl))array[i]=with;}else if(repl==null)array[i]=with;}return array; }

    @SafeVarargs
    public static <T> T[] arrayFromList(List<T> list, T... empty) {
        return list.toArray(empty);
    }

    public static <T, O> O[] getFromArray(T[] array, Getter<T, O> getter, O... ignore) {
        O[] os = (O[]) ArrayGetter.newArray(array.length, ignore.getClass().getComponentType());
        for (int i = 0; i < array.length; i++) {
            os[i] = getter.get(array[i]);
        }
        return os;
    }

    public interface Getter<T, O> {
        O get(T t);
    }

    public static double[] convertToNative(Double[] doubles) {
        double[] d = new double[doubles.length];
        for (int i = 0; i < d.length; i++) {
            d[i] = doubles[i];
        }
        return d;
    }
}
