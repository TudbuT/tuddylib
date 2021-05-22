package tudbut.obj;

import tudbut.tools.ArrayGetter;

import java.util.ArrayList;

public class Partial<T> {
    private T current;
    private final ArrayList<Listener<T>> listeners = new ArrayList<>();
    private boolean completed = false;

    public Partial(T original) {
        current = original;
    }

    @SafeVarargs
    public final T change(T changed, Listener<T>... ignore) {
        if(completed)
            return null;

        Listener<T>[] listeners = ArrayGetter.newGenericArray(this.listeners.size(), ignore);
        for (int i = 0; i < listeners.length; i++) {
            listeners[i] = this.listeners.get(i);
        }
        for (Listener<T> listener : listeners) {
            listener.onChange(current, changed);
        }
        current = changed;
        return changed;
    }

    @SafeVarargs
    public final void complete(T completedValue, Listener<T>... ignore) {
        completed = true;
        current = completedValue;
        Listener<T>[] listeners = ArrayGetter.newGenericArray(this.listeners.size(), ignore);
        for (int i = 0; i < listeners.length; i++) {
            listeners[i] = this.listeners.get(i);
        }
        for (Listener<T> listener : listeners) {
            listener.onComplete(current);
        }
    }

    public T get() {
        return current;
    }

    public void addChangeListener(Listener<T> listener) {
        listeners.add(listener);
    }

    public interface Listener<T> {
        default void onChange(T original, T changed) {
        
        }
        
        void onComplete(T completed);
    }
}
