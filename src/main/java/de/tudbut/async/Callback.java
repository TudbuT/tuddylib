package de.tudbut.async;

/**
 * @author TudbuT
 * @since 03 Jun 2022
 */

public interface Callback<T> {
    
    void call(T t);
}
