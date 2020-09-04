package tudbut.obj;

import de.tudbut.tools.Tools;

import java.util.Arrays;

public class TypedArray<T> {
    
    private boolean hasClass = false;
    private Class<?> tClass = null;
    
    protected T[] ts;
    
    public TypedArray(int length) {
        ts = (T[]) new Object[length];
    }
    
    public TypedArray(T[] ts) {
        hasClass = true;
        tClass = ts.getClass().getComponentType();
        if(ts.length != 0)
            tClass = ts[0].getClass();
        this.ts = ts;
    }
    
    public TypedArray(int length, T[] ts) {
        T[] oTs = ts;
        ts = (T[]) new Object[length];
        Tools.copyArray(oTs, ts, Math.min(ts.length, oTs.length));
        hasClass = true;
        tClass = ts.getClass().getComponentType();
        if(ts.length != 0 && ts[0] != null)
            tClass = ts[0].getClass();
        this.ts = ts;
        
    }
    
    public T get(int i) throws ArrayIndexOutOfBoundsException {
        return ts[i];
    }
    
    public T set(int i, T t) throws ArrayIndexOutOfBoundsException {
        return ts[i] = t;
    }
    
    public String toString() {
        return Arrays.toString(ts);
    }
    
    public T[] toArray() {
        return ts;
    }
    
    public int length() {
        return ts.length;
    }
    
    @SafeVarargs
    public static <T> T[] convertToArray(T... ts) {
        return new TypedArray<>(ts).toArray();
    }
    
    public static Object[] convertToObjectArray(Object... objects) {
        return new TypedArray<>(objects).toArray();
    }
    
    @SafeVarargs
    public static <T> TypedArray<T> convert(T... ts) {
        return new TypedArray<>(ts);
    }
    
    public static TypedArray<?> convertObjects(Object... objects) {
        return new TypedArray<>(objects);
    }
    
    public static String string(Object... objects) {
        return Arrays.toString(objects);
    }
}
