package tudbut.tools;

import tudbut.obj.Save;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ConfigSaver {
    
    public static Map<String, String> saveConfig(Object object) {
        Map<String, String> map = new HashMap<>();
        
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
                        ObjectSerializer serializer = new ObjectSerializer(o);
                        o = serializer.useReadableValues(true).convertAll().done();
                        map.put(field.getName(), (String) (o));
                    }
        
                    // Revert access privileges to default
                    field.setAccessible(b);
                }
            } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            
            clazz = clazz.getSuperclass();
        }
        
        return map;
    }
    
    public static void loadConfig(Object object, Map<String, String> map) {
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
                        if (map.containsKey(field.getName())) {
                            Object o;
                            ObjectSerializer serializer = new ObjectSerializer(map.get(field.getName()));
                            o = serializer.useReadableValues(true).convertAll().done();
                            if (o != null)
                                field.set(object, o);
                        }
                        
                        // Revert access privileges to default
                        field.setAccessible(b);
                    }
                }
            } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            
            clazz = clazz.getSuperclass();
        }
    }
    
    public static boolean shouldSave(Field field) {
        return field.getDeclaredAnnotation(Save.class) != null;
    }
}
