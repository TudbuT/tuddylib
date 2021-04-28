package tudbut.tools;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class ReflectUtil {
    
    public static boolean hasAnnotation(Field field, Class<? extends Annotation> clazz) {
        return field.getDeclaredAnnotation(clazz) != null;
    }
    
    public static <T> T getPrivateFieldByTypeIndex(Class<?> clazz, Object o, Class<? extends T> type, int index) {
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
    public static <T> T setPrivateFieldByTypeIndex(Class<?> clazz, Object o, Class<? extends T> type, int index, T t) {
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
}
