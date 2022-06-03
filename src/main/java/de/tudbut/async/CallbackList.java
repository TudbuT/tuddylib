package de.tudbut.async;

import java.util.ArrayList;

/**
 * @author TudbuT
 * @since 03 Jun 2022
 */

public class CallbackList<T> implements Callback<T> {

    private final ArrayList<Callback<T>> callbacks = new ArrayList<>();
    private boolean done = false;
    
    public void add(Callback<T> callback) {
        callbacks.add(callback);
    }
    
    @Override
    public void call(T t) {
        done = true;
        for (int i = 0 ; i < callbacks.size() ; i++) {
            callbacks.get(i).call(t);
        }
    }
    
    public boolean done() {
        return done;
    }
    
    public void setDone() {
        done = true;
    }
    
    public boolean exists() {
        return callbacks.size() != 0;
    }
}
