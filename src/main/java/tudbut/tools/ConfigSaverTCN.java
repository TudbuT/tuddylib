package tudbut.tools;

import tudbut.obj.Save;
import tudbut.parsing.TCN;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ConfigSaverTCN {
    
    public static TCN saveConfig(Object object) {
        TCN map = TCN.getEmpty();
        
        Class<?> clazz = object.getClass();
        while (clazz != Object.class) {
            Field[] fields = clazz.getDeclaredFields();
            try {
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    boolean b = field.isAccessible();
                    // I want to access it
                    field.setAccessible(true);
    
                    if(shouldSave(field)) {
                        Object o = field.get(object);
                        ObjectSerializerTCN serializer = new ObjectSerializerTCN(o);
                        o = serializer.convertAll().done();
                        map.set(field.getName(), o);
                    }
        
                    // Revert access privileges to default
                    field.setAccessible(b);
                }
            } catch (IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            
            clazz = clazz.getSuperclass();
        }
        
        return map;
    }
    
    public static void loadConfig(Object object, TCN tcn) {
        Class<?> clazz = object.getClass();
        while (clazz != Object.class) {
            Field[] fields = clazz.getDeclaredFields();
            try {
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    
                    if(shouldSave(field)) {
                        boolean b = field.isAccessible();
                        // I want to access it
                        field.setAccessible(true);
                        if (tcn.map.containsKey(field.getName())) {
                            Object o;
                            ObjectSerializerTCN serializer = new ObjectSerializerTCN(tcn.getSub(field.getName()));
                            o = serializer.convertAll().done();
                            if (o != null)
                                field.set(object, o);
                        }
                        
                        // Revert access privileges to default
                        field.setAccessible(b);
                    }
                }
            } catch (IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            
            clazz = clazz.getSuperclass();
        }
    }
    
    public static boolean shouldSave(Field field) {
        return field.getDeclaredAnnotation(Save.class) != null;
    }
}
