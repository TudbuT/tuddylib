package tudbut.obj;

import de.tudbut.tools.Tools;

import java.util.List;

public class TypedList<T> extends TypedArray<T> {
    public TypedList() {
        super(0);
    }
    
    public TypedList(T[] ts) {
        super(ts);
    }
    
    public static <T> TypedList<T> get(TypedArray<T> typedArray) {
        TypedList<T> list = new TypedList<>(typedArray.toArray());
        if(typedArray.isLocked())
            list.lock();
        return list;
    }
    
    public synchronized T[] add(T[] t) {
        synchronized (synchronizer) {
            checkLocked();
            T[] n = new TypedArray<>(length() + t.length, ts).toArray();
            for (int i = ts.length, j = 0; j < t.length; i++, j++) {
                n[i] = t[j];
            }
            ts = n;
            return t;
        }
    }
    
    public synchronized TypedArray<T> add(TypedArray<T> t) {
        synchronized (synchronizer) {
            add(t.ts);
            return t;
        }
    }
    
    public synchronized List<T> add(List<T> t) {
        synchronized (synchronizer) {
            add(t.toArray(ts));
            return t;
        }
    }
    
    public synchronized T add(T t) {
        synchronized (synchronizer) {
            checkLocked();
            T[] n = new TypedArray<>(length() + 1, ts).toArray();
            n[n.length - 1] = t;
            ts = n;
            return t;
        }
    }
    
    public synchronized T add(int i, T t) {
        synchronized (synchronizer) {
            checkLocked();
            if (i == ts.length - 1)
                return add(t);
    
            T[] n = new TypedArray<>(length() + 1, ts).toArray();
            int tPos = 0;
            for (int j = 0; j < n.length; j++) {
                if (j == i) {
                    n[j] = t;
                    j++;
                    continue;
                }
                n[j] = ts[tPos];
                tPos++;
            }
            ts = n;
            return t;
        }
    }
    
    public synchronized T pop(int i) {
        synchronized (synchronizer) {
            checkLocked();
            int aP = 0;
            boolean removed = false;
            T t = null;
            for (int j = 0; j < ts.length; j++) {
                if (j == i && !removed) {
                    t = ts[j];
                    removed = true;
                    continue;
                }
                ts[aP] = ts[j];
                aP++;
            }
            decrementSize();
            return t;
        }
    }
    
    public synchronized T pop() {
        synchronized (synchronizer) {
            checkLocked();
            T t = ts[ts.length - 1];
            decrementSize();
            return t;
        }
    }
    
    public synchronized void remove(int i) {
        synchronized (synchronizer) {
            checkLocked();
            pop(i);
        }
    }
    
    public synchronized void removeLast() {
        synchronized (synchronizer) {
            checkLocked();
            pop();
        }
    }
    
    private synchronized void decrementSize() {
        checkLocked();
        ts = new TypedArray<>(length() - 1, ts).toArray();
    }
    
    public T random() {
        synchronized (synchronizer) {
            return Tools.randomOutOfArray(this);
        }
    }
    
    public TypedList<T> clone() {
        return new TypedList<>(ts);
    }
}
