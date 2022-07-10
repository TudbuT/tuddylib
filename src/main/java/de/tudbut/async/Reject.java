package de.tudbut.async;

/**
 * @author TudbuT
 * @since 03 Jun 2022
 */

public class Reject extends Error {
    final Object real;
    
    public Reject(Throwable throwable) {
        super(throwable);
        real = throwable;
    }
    public Reject(Object object) {
        super(object.toString());
        real = object;
    }
    
    public <T> T getReal() {
        return (T) real;
    }
}
