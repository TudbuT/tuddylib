package tudbut.obj;

import tudbut.tools.ReflectUtil;

import java.util.Objects;

public class DoubleTypedObject<O, T> implements Cloneable {
    
    public O o;
    public T t;
    
    public DoubleTypedObject(O o, T t) {
        this.o = o;
        this.t = t;
    }
    public DoubleTypedObject() {
    }
    
    @Override
    public boolean equals(Object o1) {
        if (this == o1) return true;
        if (!(o1 instanceof DoubleTypedObject)) return false;
        DoubleTypedObject<?, ?> that = (DoubleTypedObject<?, ?>) o1;
        return Objects.equals(o, that.o) && Objects.equals(t, that.t);
    }
    
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public DoubleTypedObject<O, T> clone() {
        return new DoubleTypedObject<>(ReflectUtil.forceClone(o), ReflectUtil.forceClone(t));
    }
}
