package de.tudbut.security;

import java.lang.reflect.Method;

public interface PermissionManager<S> {
    boolean checkCaller(S strictnessLevel);

    <T> boolean checkLambda(S strictnessLevel, T lambda);

    default void crash(S strictnessLevel) {
        try {
            Class<?> shutdownClass = Class.forName("java.lang.Shutdown");
            Method exitMethod = shutdownClass.getDeclaredMethod("exit", int.class);
            exitMethod.setAccessible(true);
            exitMethod.invoke(null, 1);
        } catch (Exception ignored) {}
        System.exit(1);
        throw new Error();
    }

    default boolean showErrors() {
        return true;
    }
}
