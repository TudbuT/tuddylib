package tudbut.tools;

import de.tudbut.type.FInfo;
import tudbut.obj.TypedArray;
import tudbut.obj.TypedList;

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
    
    @FInfo(
            s="" +
                    "@return TypedArray, but castable to TypedList"
    )
    public TypedArray<T> toTypedArray() {
        return new TypedList<>(values.clone()).lock();
    }
}
