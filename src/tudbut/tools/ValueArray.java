package tudbut.tools;

public class ValueArray<T> {
    private final T[] values;
    public final int length;

    public ValueArray(T[] values) {
        this.values = values.clone();
        this.length = this.values.length;
    }

    public T get(int i) {
        return values[i];
    }
}
