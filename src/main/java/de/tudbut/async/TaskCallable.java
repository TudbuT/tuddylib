package de.tudbut.async;

/**
 * @author TudbuT
 * @since 03 Jun 2022
 */

public interface TaskCallable<T> {

    void execute(Callback<T> resolve, Callback<Throwable> reject) throws Resolve, Reject;
}
