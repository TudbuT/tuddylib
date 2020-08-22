package tudbut.obj;

public class PAtomic<T> implements AtomicSink<T> {
    public T object;

    public PAtomic() {}
    public PAtomic(T object) { this.object = object; }

    public void set(T object) { this.object = object; }

    public T get() { return object; }

    @Override
    public String toString() { return object.toString(); }
}
