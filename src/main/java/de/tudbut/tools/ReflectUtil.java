package de.tudbut.tools;

import com.sun.org.apache.xpath.internal.operations.Mod;
import de.tudbut.io.CLSPrintWriter;
import de.tudbut.parsing.TCN;
import sun.misc.Unsafe;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

public class ReflectUtil {
    
    public static boolean hasAnnotation(Field field, Class<? extends Annotation> clazz) {
        return field.getDeclaredAnnotation(clazz) != null;
    }
    
    public static <T> T getPrivateFieldByTypeIndex(Class<?> clazz, Object o, Class<? extends T> type, int index) {
        int idx = 0;
        for (Field field : clazz.getDeclaredFields()) {
            if(field.getType() == type) {
                if(idx++ == index) {
                    ReflectUtil.forceAccessible(field);
                    try {
                        return (T) field.get(o);
                    }
                    catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        throw new NullPointerException();
    }
    public static <T> T setPrivateFieldByTypeIndex(Class<?> clazz, Object o, Class<? extends T> type, int index, T t) {
        int idx = 0;
        for (Field field : clazz.getDeclaredFields()) {
            if(field.getType() == type) {
                if(idx++ == index) {
                    ReflectUtil.forceAccessible(field);
                    try {
                        field.set(o, t);
                        return t;
                    }
                    catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
    public static <T> T forceClone(T t) {
        if(t.getClass() != Object.class) {
            try {
                return (T) t.getClass().getDeclaredMethod("clone").invoke(t);
            }
            catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
                throw new IllegalArgumentException();
            }
        }
        else
            return (T) new Object();
    }

    public static Unsafe theSafe;
    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            theSafe = (Unsafe) f.get(null);
        } catch (Throwable e) {
            throw new Error(e); // Don't recover.
        }
    }


    // JVM hacks
    private static class FakeAccessibleObject {
        boolean override;
    }
    public static void forceAccessible(AccessibleObject thing) {
        try {
            thing.setAccessible(true);
            if(!thing.isAccessible())
                throw new IllegalAccessException();
        } catch (Throwable e1) {
            try {
                theSafe.putBoolean(thing, theSafe.objectFieldOffset(AccessibleObject.class.getDeclaredField("override")), true);
                if(!thing.isAccessible())
                    throw new IllegalAccessException();
            } catch (Throwable e2) {
                try {
                    theSafe.putBoolean(thing, theSafe.objectFieldOffset(FakeAccessibleObject.class.getDeclaredFields()[0]), true);
                    if(!thing.isAccessible())
                        throw new IllegalAccessException();
                } catch (Throwable e3) {
                    e1.printStackTrace();
                    e2.printStackTrace();
                    e3.printStackTrace();
                    throw new AssertionError("This JVM does not support changing the override");
                }
            }
        }
    }

    public static void eraseFinality(Field thing) {
        try {
            Field f = Field.class.getDeclaredField("modifiers");
            forceAccessible(f);
            f.set(thing, f.getInt(thing) & ~Modifier.FINAL);
            if((thing.getModifiers() & Modifier.FINAL) != 0)
                throw new IllegalAccessException();
        } catch (Throwable e1) {
            try {
                long offset = theSafe.objectFieldOffset(Field.class.getDeclaredField("modifiers"));
                theSafe.putInt(thing, offset, theSafe.getInt(thing, offset) & ~Modifier.FINAL); // EZ
                if((thing.getModifiers() & Modifier.FINAL) != 0)
                    throw new IllegalAccessException();
            } catch (Throwable e2) {
                e1.printStackTrace();
                e2.printStackTrace();
                throw new AssertionError("This JVM does not support changing field modifiers");
            }
        }
    }
}
