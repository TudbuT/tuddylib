package de.tudbut.security;

import de.tudbut.parsing.TCN;

public interface Strictness {

    Object getRawProperty(String name);

    default <T> T getProperty(String name) {
        return (T) getRawProperty(name);
    }
    default boolean getBoolProperty(String name) {
        Boolean b = getProperty(name);
        if(b == null)
            return false;
        return b;
    }
    default String getStringProperty(String name) {
        return getProperty(name);
    }
    default int getIntProperty(String name) {
        return getProperty(name);
    }
    default boolean hasProperty(String name) {
        return getRawProperty(name) != null;
    }
}
