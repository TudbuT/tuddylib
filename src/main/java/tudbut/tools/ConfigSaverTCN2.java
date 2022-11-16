package tudbut.tools;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;

import sun.misc.Unsafe;
import tudbut.obj.Save;
import tudbut.obj.Transient;
import tudbut.parsing.TCN;
import tudbut.parsing.TCNArray;

public class ConfigSaverTCN2 {

    static Unsafe theSafe;
    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            theSafe = (Unsafe) f.get(null);
        } catch (Throwable e) {
            throw new Error(e); // Don't recover.
        }
    }

    static ArrayList<Class<?>> tcnPrimitives = new ArrayList<>(Arrays.asList(
        boolean.class, Boolean.class,
        byte.class, Byte.class,
        short.class, Short.class,
        char.class, Character.class,
        int.class, Integer.class,
        float.class, Float.class,
        long.class, Long.class,
        double.class, Double.class,
        String.class,
        TCN.class,
        TCNArray.class
    ));

    public static Object write(Object object, boolean writeAll, boolean writeStatic) {
        if(object == null) {
            TCN tcn = new TCN();
            tcn.set("$", "null");
            return tcn;
        }
        Class<?> objectClass = object.getClass();
        if(objectClass == Class.class && writeStatic) {
            objectClass = (Class<?>) object;
            object = null;
        }
        if(tcnPrimitives.contains(objectClass)) {
            return object; // just write the object without any wrapping lol
        }

        TCN tcn = new TCN();
        if(objectClass.isArray()) {
            int len = Array.getLength(object);
            tcn.set("$", "[]");
            tcn.set("*", objectClass.getComponentType().getName());
            tcn.set("length", len);
            TCNArray tcnArray = new TCNArray();
            for(int i = 0; i < len; i++) {
                tcnArray.add(write(Array.get(object, i), true, writeStatic));
            }
            tcn.set("items", tcnArray);
        }
        else {
            tcn.set("$", objectClass.getName());
            if(objectClass.isEnum()) {
                tcn.set("*", Arrays.asList(objectClass.getEnumConstants()).indexOf(object)); // Return the equivalent of .ordinal()
            }
            ArrayList<Field> fields = new ArrayList<>();

            // Read fields
            ArrayList<Class<?>> supers = getSupers(objectClass); // getSupers because interfaces can't have fields
            for(Class<?> superclass : supers) {
                fields.addAll(Arrays.asList(superclass.getDeclaredFields()));
            }
            fields.addAll(Arrays.asList(objectClass.getDeclaredFields()));
            
            for(Field field : fields) {
                boolean isStatic = (field.getModifiers() & Modifier.STATIC) != 0;
                // Ignore field if writeAll is not set and it isn't @Save
                if(!writeAll && field.getDeclaredAnnotation(Save.class) == null)
                    continue;
                if(!writeStatic && isStatic)
                    continue;
                if(field.getDeclaredAnnotation(Transient.class) != null)
                    continue;

                forceAccessible(field); // lovely java 18 bypass
                Object o;
                try {
                    o = field.get(isStatic ? null : object);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // These can't happen
                    System.err.println("forceAccessible silently failed. Exiting.");
                    throw new Error("ConfigSaverTCN2: forceAccessible failed");
                }
                tcn.set(field.getName(), write(o, true, writeStatic));
            }
        }

        return tcn;
    }

    public static Object convertPrimitive(Object object, Class<?> type) {
        if(object instanceof String) {
            switch(type.getSimpleName()) {
                case "boolean":
                case "Boolean":
                    return Boolean.parseBoolean((String) object);
                case "byte":
                case "Byte":
                    return (byte) Integer.parseInt((String) object);
                case "short":
                case "Short":
                    return (short) Integer.parseInt((String) object);
                case "char":
                case "Character":
                    return (char) Integer.parseInt((String) object);
                case "int":
                case "Integer":
                    return Integer.parseInt((String) object);
                case "float":
                case "Float":
                    return Float.parseFloat((String) object);
                case "long":
                case "Long":
                    return Long.parseLong((String) object);
                case "double":
                case "Double":
                    return Double.parseDouble((String) object);
            }
        }
        return object;
    }

    public static Object read(Object object, Object toReadTo) throws ClassNotFoundException {
        Class<?> objectClass = object.getClass();
        if(tcnPrimitives.contains(objectClass) && !(object instanceof TCN)) {
            return object; // just write the object without any wrapping
        }

        boolean forceAllow = ((Integer)(-1)).equals(toReadTo);
        if(forceAllow) toReadTo = null;

        TCN tcn = (TCN) object;
        if(tcn.getString("$") == null && toReadTo != null) tcn.set("$", toReadTo.getClass().getName());
        if(tcn.getString("$").equals("null")) 
            return null;
        if(tcn.getString("$").equals("[]")) {
            objectClass = Class.forName(tcn.getString("*"));
            TCNArray tcnArray = tcn.getArray("items");
            Object jArray = Array.newInstance(objectClass, tcn.getInteger("length"));
            for(int i = 0; i < tcnArray.size(); i++) {
                Array.set(jArray, i, convertPrimitive(read(tcnArray.get(i), null), objectClass));
            }
            return jArray;
        }
        else {
            objectClass = Class.forName(tcn.getString("$"));
            Object instance = toReadTo;
            if(objectClass.isEnum()) {
                instance = objectClass.getEnumConstants()[tcn.getInteger("*")];
            }
            if(instance == null) {
                try {
                    instance = theSafe.allocateInstance(objectClass);
                } catch (InstantiationException e1) {
                    // This can't happen
                    throw new Error(e1);
                }
            }
            ArrayList<Field> fields = new ArrayList<>();

            // Read fields
            ArrayList<Class<?>> supers = getSupers(objectClass); // getSupers because interfaces can't have fields
            for(Class<?> superclass : supers) {
                fields.addAll(Arrays.asList(superclass.getDeclaredFields()));
            }
            fields.addAll(Arrays.asList(objectClass.getDeclaredFields()));
            
            for(Field field : fields) {
                boolean isStatic = (field.getModifiers() & Modifier.STATIC) != 0;
                if(field.getDeclaredAnnotation(Transient.class) != null)
                    continue;
                forceAccessible(field); // lovely java 18 bypass
                eraseFinality(field); // other lovely java 18 bypass
                Object o = tcn.get(field.getName());
                if(o == null) {
                    if(toReadTo != null || isStatic || forceAllow)
                        continue;
                    else
                        throw new IllegalArgumentException("TCN is not complete. Try adding a toReadTo parameter.");
                }
                try {
                    field.set(isStatic ? null : instance, convertPrimitive(read(o, null), field.getType()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    // These can't happen
                    System.err.println("forceAccessible silently failed. Exiting.");
                    throw new Error("ConfigSaverTCN2: forceAccessible failed");
                }
            }

            return instance;
        }

    }

    public static ArrayList<Class<?>> getSupers(Class<?> primary) {
        ArrayList<Class<?>> supers = new ArrayList<>();
        Class<?> c = primary;
        while(c != null) {
            c = c.getSuperclass();
            if(c != null) supers.add(c);
        }
        return supers;
    }

    public static ArrayList<Class<?>> getInterfaces(Class<?> primary) {
        ArrayList<Class<?>> supers = new ArrayList<>();
        Class<?> c = primary;
        while(c != null) {
            supers.addAll(Arrays.asList(c.getInterfaces()));
            c = c.getSuperclass();
            if(c != null) supers.add(c);
        }
        return supers;
    }
    
    // JVM hacks
    private static class FakeAccessibleObject {
        boolean override;
    }
    private static void forceAccessible(AccessibleObject thing) {
        try {
            theSafe.putBoolean(thing, theSafe.objectFieldOffset(FakeAccessibleObject.class.getDeclaredField("override")), true);
        } catch(Exception e) { // we are doomed
            e.printStackTrace();
            System.err.println("Failed to set accessible property. We are doomed.");
            System.exit(1);
        }
    }
    private static class FakeField extends FakeAccessibleObject {
        private Class<?> clazz;
        private int slot;
        private String name;
        private Class<?> type;
        private int modifiers;
    }
    private static void eraseFinality(Field thing) {
        try {
            long offset = theSafe.objectFieldOffset(FakeField.class.getDeclaredField("modifiers"));
            theSafe.putInt(thing, offset, theSafe.getInt(thing, offset) & ~Modifier.FINAL); // EZ
        } catch(Exception e) { // we are doomed
            e.printStackTrace();
            System.err.println("Failed to set modifier property. We are doomed.");
            System.exit(1);
        }
    }
}
