package tudbut.tools;

import java.lang.reflect.*;

public class ClassUtils {
    public static Class<?>[] getTypeArguments(Class<?> clazz) {
        Class<?>[] typeArgs = new Class<?>[((ParameterizedType)clazz.getGenericSuperclass()).getActualTypeArguments().length];
        for (int i = 0; i < clazz.getTypeParameters().length; i++) {
            try {
                typeArgs[i] = Class.forName(((ParameterizedType)clazz.getGenericSuperclass()).getActualTypeArguments()[i].getTypeName());
            }
            catch (ClassNotFoundException e) {
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                    }
                    catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    Runtime.getRuntime().exit(1);
                }).start();
                throw new Error("Something impossible happened!", e);
            }
        }
        return typeArgs;
    }

    public static <T> T[] instantiateTypeArray(Class<?> clazz, int typeParameter, int length) {
        Class<?> type = getTypeArguments(clazz)[typeParameter];
        return instantiateArray(type, length);
    }

    public static <T> T[] instantiateArray(Class<?> clazz, int length) {
        return (T[]) Array.newInstance(clazz, length);
    }

    public static <T> T[] instantiateMultiArray(Class<? extends T> clazz, int... length) {
        return (T[]) Array.newInstance(clazz, length);
    }

    public static Method getMethodByName(Class<?> clazz, String method) {
        Method r = null;
        for (int i = 0; i < clazz.getDeclaredMethods().length; i++) {
            if(clazz.getDeclaredMethods()[i].getName().equals(method))
                r = clazz.getDeclaredMethods()[i];
        }
        return r;
    }

    public static Class<?>[] getTypeArguments(Method method) {
        Class<?>[] typeArgs = new Class<?>[method.getGenericParameterTypes().length];
        for (int i = 0; i < typeArgs.length; i++) {
            Type type = method.getTypeParameters()[i].getBounds()[0];
            try {
                typeArgs[i] = Class.forName(type.getTypeName());
            }
            catch (ClassNotFoundException e) {
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                    }
                    catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    Runtime.getRuntime().exit(1);
                }).start();
                throw new Error("Something impossible happened!", e);
            }
        }
        return typeArgs;
    }
}
