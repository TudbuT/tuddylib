package de.tudbut.async;

/**
 * @author TudbuT
 * @since 03 Jun 2022
 */

public class Resolve extends RuntimeException {
    final Object real;
    
    public Resolve(Throwable throwable) {
        super(throwable);
        real = throwable;
    }
    public Resolve(Object object) {
        super(object.toString());
        real = object;
    }
    
    public <T> T getReal() {
        return (T) real;
    }
}
