package de.tudbut.obj;

public class Atomic<T> implements AtomicSink<T> {
    private T object;

    public Atomic() {}
    public Atomic(T object) { this.object = object; }

    public void set(T object) { this.object = object; }

    public T get() { return object; }

    @Override
    public String toString() { return object.toString(); }
}
