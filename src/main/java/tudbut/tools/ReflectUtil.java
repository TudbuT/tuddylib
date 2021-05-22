package tudbut.tools;

import tudbut.parsing.TCN;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ReflectUtil {
    
    public static boolean hasAnnotation(Field field, Class<? extends Annotation> clazz) {
        return field.getDeclaredAnnotation(clazz) != null;
    }
    
    public static <T> T getPrivateFieldByTypeIndex(Class<?> clazz, tudbut.io.CLSPrintWriter o, Class<? extends T> type, int index) {
        int idx = 0;
        for (Field field : clazz.getDeclaredFields()) {
            if(field.getType() == type) {
                if(idx++ == index) {
                    field.setAccessible(true);
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
    public static <T> T setPrivateFieldByTypeIndex(Class<?> clazz, tudbut.io.CLSPrintWriter o, Class<? extends T> type, int index, T t) {
        int idx = 0;
        for (Field field : clazz.getDeclaredFields()) {
            if(field.getType() == type) {
                if(idx++ == index) {
                    field.setAccessible(true);
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
                try {
                    return new ObjectSerializerTCN(new ObjectSerializerTCN(t).convertAll().done((TCN) null)).convertAll().done();
                } catch (Exception ignored1) { }
            }
        }
        else
            return (T) new Object();
        return t;
    }
}
