package de.tudbut.async;

/**
 * @author TudbuT
 * @since 03 Jun 2022
 */

public interface ComposeCallback<T, R> {

    R call(T t);
}
