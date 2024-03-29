package de.tudbut.security;

import de.tudbut.tools.ReflectUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
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

    default String getClassName(Class<?> clazz) {
        return getClassName(clazz, null, 0);
    }
    default String getClassName(Class<?> clazz, boolean[] cache, int idx) {
        if(cache != null && cache[0])
            return clazz.getName();
        try {
            // Reset the name field so that it must be cached again
            Field nameField = clazz.getClass().getDeclaredField("name");
            ReflectUtil.forceAccessible(nameField);
            nameField.set(clazz, null);
            // name is clean, getName can now be used.
            if(cache != null)
                cache[idx] = true;
            return clazz.getName();
        }
        catch(Exception e) {
            // name can't be cleaned, getName can't be used.
            if(cache != null)
                cache[idx] = false;
            try {
                // Unable to reset the name field, invoking the native that gets the name directly
                Method initClassName = clazz.getClass().getDeclaredMethod("initClassName");
                ReflectUtil.forceAccessible(initClassName);
                return (String) initClassName.invoke(clazz);
            }
            catch(Exception e1) {
                return null;
            }
        }
    }
}
