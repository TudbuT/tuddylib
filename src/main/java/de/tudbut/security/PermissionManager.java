package de.tudbut.security;

import de.tudbut.tools.ReflectUtil;

import java.lang.reflect.Method;
import java.util.Arrays;

public interface PermissionManager extends Cloneable {
    boolean checkCaller(Strictness strictnessLevel);

    <T> boolean checkLambda(Strictness strictnessLevel, T lambda);

    default void crash(Strictness strictnessLevel) {
        DataKeeper.forgetAll = true;
        try {
            Class<?> shutdownClass = Class.forName("java.lang.Shutdown");
            Method exitMethod = shutdownClass.getDeclaredMethod("exit", int.class);
            ReflectUtil.forceAccessible(exitMethod);
            exitMethod.invoke(null, 1);
        } catch (Exception ignored) {}
        System.exit(1);
        throw new Error();
    }

    default boolean showErrors() {
        return true;
    }

    default void killReflection() {
        Class<?> clazz = getClass();
        while(Arrays.stream(clazz.getInterfaces()).anyMatch(x -> x == PermissionManager.class)) {
            AccessKiller.killReflectionFor(clazz);
            clazz = clazz.getSuperclass();
        }
    }

    PermissionManager clone();
}
