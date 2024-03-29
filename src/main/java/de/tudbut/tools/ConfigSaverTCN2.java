package de.tudbut.tools;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;

import de.tudbut.parsing.TCN;
import de.tudbut.parsing.TCNArray;
import sun.misc.Unsafe;
import de.tudbut.obj.Save;
import de.tudbut.obj.Transient;

public class ConfigSaverTCN2 {

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
        return write(object, writeAll, writeStatic, true);
    }

    public static Object write(Object object, boolean writeAll, boolean writeStatic, boolean allowPrimitives) {
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
        if(tcnPrimitives.contains(objectClass) && allowPrimitives) {
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
                tcnArray.add(write(Array.get(object, i), true, false, objectClass.getComponentType() != Object.class));
            }
            tcn.set("items", tcnArray);
        }
        else {
            tcn.set("$", objectClass.getName());
            if(objectClass.isEnum()) {
                tcn.set("*", Arrays.asList(objectClass.getEnumConstants()).indexOf(object)); // Return the equivalent of .ordinal()
                return tcn;
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
                if(isStatic && (field.getModifiers() & Modifier.FINAL) != 0)
                    continue;
                if(field.getDeclaredAnnotation(Transient.class) != null)
                    continue;

                ReflectUtil.forceAccessible(field); // lovely java 18 bypass
                Object o;
                try {
                    o = field.get(isStatic ? null : object);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // These can't happen
                    System.err.println("ReflectUtil.forceAccessible silently failed. Exiting.");
                    throw new Error("ConfigSaverTCN2: ReflectUtil.forceAccessible failed");
                }
                tcn.set(field.getName(), write(o, true, false, field.getType() != Object.class));
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
                    return ((String) object).charAt(0);
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
            try {
                objectClass = Class.forName(tcn.getString("*"));
            } catch (ClassNotFoundException e) {
                switch(tcn.getString("*")) {
                    case "boolean":
                        objectClass = boolean.class;
                        break; 
                    case "byte":
                        objectClass = byte.class;
                        break; 
                    case "short":
                        objectClass = short.class;
                        break; 
                    case "char":
                        objectClass = char.class;
                        break; 
                    case "int":
                        objectClass = int.class;
                        break; 
                    case "float":
                        objectClass = float.class;
                        break; 
                    case "long":
                        objectClass = long.class;
                        break; 
                    case "double":
                        objectClass = double.class;
                        break; 
                }
            }
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
                return instance;
            }
            if(instance == null) {
                try {
                    instance = ReflectUtil.theSafe.allocateInstance(objectClass);
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
                if(isStatic && (field.getModifiers() & Modifier.FINAL) != 0)
                    continue;
                ReflectUtil.forceAccessible(field); // lovely java 18 bypass
                ReflectUtil.eraseFinality(field); // other lovely java 18 bypass
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
                    System.err.println("ReflectUtil.forceAccessible silently failed. Exiting.");
                    throw new Error("ConfigSaverTCN2: ReflectUtil.forceAccessible failed");
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
    
}
