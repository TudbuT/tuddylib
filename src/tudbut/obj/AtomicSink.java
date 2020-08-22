package tudbut.obj;

public interface AtomicSink<T> {

    void set(T object);
    T get();
}