package tudbut.tools;

import java.util.ArrayList;

public class Queue<T> {
    
    private ArrayList<T> ts = new ArrayList<>();
    
    public Queue() { }
    
    protected Queue(Queue<T> queue) {
        ts = (ArrayList<T>) queue.ts.clone();
    }
    
    public synchronized T pushTop(T t) {
        ts.add(t);
        notifyAll();
        return t;
    }
    
    public synchronized T pushBottom(T t) {
        ts.add(0, t);
        notifyAll();
        return t;
    }
    
    public synchronized T getTop() {
        return ts.get(ts.size() - 1);
    }
    
    public synchronized T getBottom() {
        return ts.get(0);
    }
    
    public synchronized T popBottom() {
        T t = ts.get(0);
        ts.remove(0);
        notifyAll();
        return t;
    }
    
    public synchronized T popTop() {
        T t = ts.get(ts.size() - 1);
        ts.remove(ts.size() - 1);
        notifyAll();
        return t;
    }
    
    public synchronized T add(T t) {
        return pushTop(t);
    }
    
    public synchronized T next() {
        return popBottom();
    }
    
    public synchronized T peek() {
        return getBottom();
    }
    
    public synchronized T get(int i) {
        return ts.get(i);
    }
    
    public synchronized int size() {
        return ts.size();
    }
    
    public synchronized boolean hasNext() {
        return ts.size() > 0;
    }
    
    public synchronized ArrayList<T> toList() {
        return (ArrayList<T>) ts.clone();
    }
    
    @Override
    public boolean equals(Object o) {
        return o == this || (o instanceof Queue && ((Queue<?>) o).ts.equals(ts));
    }
    
    @Override
    public Queue<T> clone() {
        return new Queue<>(this);
    }
}
