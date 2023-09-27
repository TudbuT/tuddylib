package de.tudbut.security;

import de.tudbut.parsing.TCN;

public interface Strictness {

    Object getRawProperty(String name);

    default <T> T getProperty(String name) {
        return (T) getProperty(name);
    }
    default boolean getBoolProperty(String name) {
        return getProperty(name);
    }
    default String getStringProperty(String name) {
        return getProperty(name);
    }
    default int getIntProperty(String name) {
        return getProperty(name);
    }
}
