package de.tudbut.tools;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.Function;

public class Nullable<T> {

    public T object;

    public Nullable(T object) {
        this.object = object;
    }

    public <R> Nullable<R> ensure(Function<T, R> func) {
        if(object == null)
            throw new NullPointerException();
        return new Nullable<R>(func.apply(object));
    }

    public <R> Nullable<R> apply(Function<T, R> func) {
        if(object == null)
            return new Nullable<R>(null);
        return new Nullable<R>(func.apply(object));
    }

    public void ensureConsume(Consumer<T> func) {
        if(object == null)
            throw new NullPointerException();
        func.accept(object);
    }

    public void consume(Consumer<T> func) {
        if(object == null)
            return;
        func.accept(object);
    }

    public T ensureGet() {
        if(object == null)
            throw new NullPointerException();
        return object;
    }

    public T get() {
        return object;
    }

    public void except(Runnable runnable) {
        if(object == null)
            runnable.run();
    }

    public <R> Nullable<R> except(Supplier<R> supplier) {
        if(object == null)
            return new Nullable<>(supplier.get());
        return new Nullable<>(null);
    }

    public Nullable<T> or(Supplier<T> supplier) {
        if(object == null)
            return new Nullable<>(supplier.get());
        return this;
    }
    
    public String toString() {
        return String.valueOf(object);
    }

}
