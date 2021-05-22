package tudbut.obj;

import de.tudbut.tools.Tools;

import java.lang.reflect.Array;
import java.util.Arrays;

public class TypedArray<T> {
    
    private Class<?> tClass = null;
    private boolean locked = false;
    protected final Object synchronizer = new Object();
    
    protected T[] ts;
    
    public TypedArray(int length) {
        ts = (T[]) new Object[length];
    }
    
    public TypedArray(T[] ts) {
        tClass = ts.getClass().getComponentType();
        if(ts.length != 0)
            tClass = ts[0].getClass();
        this.ts = ts;
    }
    
    public TypedArray(int length, T[] ts) {
        T[] oTs = ts;
        ts = (T[]) new Object[length];
        Tools.copyArray(oTs, ts, Math.min(ts.length, oTs.length));
        tClass = ts.getClass().getComponentType();
        if(ts.length != 0 && ts[0] != null)
            tClass = ts[0].getClass();
        this.ts = ts;
        
    }
    
    public T get(int i) throws ArrayIndexOutOfBoundsException {
        synchronized (synchronizer) {
            return ((T[]) ts)[i];
        }
    }
    
    public T set(int i, T t) throws ArrayIndexOutOfBoundsException {
        synchronized (synchronizer) {
            checkLocked();
            return ts[i] = t;
        }
    }
    
    public String toString() {
        synchronized (synchronizer) {
            return Arrays.toString(ts);
        }
    }
    
    public T[] toArray(T... type) {
        synchronized (synchronizer) {
            tClass = type.getClass().getComponentType();
            T[] nts = (T[]) Array.newInstance(tClass, ts.length);
            Tools.copyArray(ts, nts, nts.length);
            ts = nts;
            return ts;
        }
    }
    
    public int length() {
        synchronized (synchronizer) {
            return ts.length;
        }
    }
    
    public TypedArray<T> lock() {
        locked = true;
        return this;
    }
    
    public boolean isLocked() {
        return locked;
    }
    
    protected void checkLocked() throws IllegalStateException {
        if(isLocked())
            throw new IllegalStateException("TypedArray is locked");
    }
    
    @Override
    public TypedArray<T> clone() {
        return new TypedArray<>(ts);
    }
    
    public boolean equals(Object o) {
        return o instanceof TypedArray && (Arrays.equals(((TypedArray<?>) o).ts, ts));
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
