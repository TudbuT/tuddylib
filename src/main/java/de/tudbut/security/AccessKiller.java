package de.tudbut.security;

import de.tudbut.tools.ReflectUtil;

import java.lang.ref.SoftReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccessKiller {

    private static final Field reflectionData;

    static {
        try {
            reflectionData = getField(Class.class.getDeclaredField("reflectionData"));
        } catch (NoSuchFieldException e) {
            throw new InternalError(e);
        }
    }

    private static Field getField(Field f) {
        ReflectUtil.forceAccessible(f);
        return f;
    }

    private static Object getReflectionData(Class<?> clazz) throws ReflectiveOperationException {
        // make sure ReflectionData is populated
        clazz.getDeclaredMethods();
        clazz.getDeclaredFields();
        clazz.getDeclaredConstructors();
        clazz.getInterfaces();

        SoftReference<?> data = (SoftReference<?>) reflectionData.get(clazz);
        Object reflectionData = data.get();
        assert reflectionData != null;
        return reflectionData;
    }

    /**
     * WARNING!!! Can only erase private fields!!
     * @param clazz Class to kill fields of
     * @param fieldsToKill Field names to kill, or empty to kill all.
     */
    public static void killFieldAccess(Class<?> clazz, String... fieldsToKill) {
        List<String> toKill = Arrays.asList(fieldsToKill);
        try {
            Object reflectionData = getReflectionData(clazz);
            Field data = getField(reflectionData.getClass().getDeclaredField("declaredFields"));
            List<Field> fields = new ArrayList<>(Arrays.asList((Field[]) data.get(reflectionData)));
            if(!toKill.isEmpty()) {
                for (int i = 0; i < fields.size(); i++) {
                    if (toKill.contains(fields.get(i).getName()))
                        fields.remove(i--);
                }
            }
            else {
                fields.clear();
            }
            data.set(reflectionData, fields.toArray(new Field[0]));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * WARNING!!! Can only erase private methods!!
     * @param clazz Class to kill methods of
     * @param methodsToKill Method names to kill, or empty to kill all.
     */
    public static void killMethodAccess(Class<?> clazz, String... methodsToKill) {
        List<String> toKill = Arrays.asList(methodsToKill);
        try {
            Object reflectionData = getReflectionData(clazz);
            Field data = getField(reflectionData.getClass().getDeclaredField("declaredMethods"));
            List<Method> methods = new ArrayList<>(Arrays.asList((Method[]) data.get(reflectionData)));
            if(!toKill.isEmpty()) {
                for (int i = 0; i < methods.size(); i++) {
                    if (toKill.contains(methods.get(i).getName()))
                        methods.remove(i--);
                }
            }
            else {
                methods.clear();
            }
            data.set(reflectionData, methods.toArray(new Method[0]));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * WARNING!!! Can only erase private constructors!!
     * @param clazz Class to kill constructors of
     */
    public static void killConstructorAccess(Class<?> clazz) {
        try {
            Object reflectionData = getReflectionData(clazz);
            Field data = getField(reflectionData.getClass().getDeclaredField("declaredConstructors"));
            data.set(reflectionData, new Constructor<?>[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void killReflectionFor(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            killConstructorAccess(clazz);
            killMethodAccess(clazz);
            killFieldAccess(clazz);
        }
    }

    /**
     * Stops any code from making further changes to reflectionData.
     * This also stops any further AccessKiller calls. <br>
     * Use with EXTREME caution!!
     */
    public static void killClassReflection() {
        killReflectionFor(Class.class);
    }

    /**
     * Kills access to possible ways to restore reflective access after it has been removed.
     * This should prevent all other ways of accessing fields, but other ways may exist.
     */
    public static void ensureKills() {
        killMethodAccess(Class.class, "getDeclaredFields0", "getDeclaredMethods0", "getDeclaredConstructors0");
    }
}
