package tudbut.obj;

public class TypedList<T> extends TypedArray<T> {
    public TypedList() {
        super(0);
    }
    
    public TypedList(T[] ts) {
        super(ts);
    }
    
    public synchronized T add(T t) {
        T[] n = new TypedArray<T>(length() + 1, ts).toArray();
        n[n.length - 1] = t;
        ts = n;
        return t;
    }
    
    public synchronized T add(int i, T t) {
        if(i == ts.length - 1)
            return add(t);
        
        T[] n = new TypedArray<T>(length() + 1, ts).toArray();
        int tPos = 0;
        for (int j = 0; j < n.length; j++) {
            if(j == i) {
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
    
    public synchronized T pop(int i) {
        int aP = 0;
        boolean removed = false;
        T t = null;
        for (int j = 0; j < ts.length; j++) {
            if(j == i && !removed) {
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
    
    public synchronized T pop() {
        T t = ts[ts.length - 1];
        decrementSize();
        return t;
    }
    
    public synchronized void remove(int i) {
        pop(i);
    }
    
    public synchronized void removeLast() {
        pop();
    }
    
    private synchronized void decrementSize() {
        ts = new TypedArray<T>(length() - 1, ts).toArray();
    }
}
