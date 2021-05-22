package tudbut.tools;

import tudbut.debug.Debug;
import tudbut.debug.DebugProfiler;
import tudbut.obj.Save;
import tudbut.parsing.TCN;

import java.lang.reflect.Field;

public class ConfigSaverTCN {
    
    public static TCN saveConfig(Object object) {
        DebugProfiler profiler = Debug.getDebugProfiler(ConfigSaverTCN.class, false);
        
        profiler.next("Create TCN");
        TCN map = new TCN();
        profiler.next("Get class");
        Class<?> clazz = object.getClass();
        while (clazz != Object.class) {
            profiler.next("Classes");
            Field[] fields = clazz.getDeclaredFields();
            try {
                for (int i = 0; i < fields.length; i++) {
                    profiler.next("Fields");
                    Field field = fields[i];
    
                    if(shouldSave(field)) {
                        // I want to access it
                        field.setAccessible(true);
                        Object o = field.get(object);
                        ObjectSerializerTCN serializer = new ObjectSerializerTCN(o);
                        o = serializer.convertAll().done();
                        map.set(field.getName(), o);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            
            clazz = clazz.getSuperclass();
        }
        
        profiler.endAll();
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
                        // I want to access it
                        field.setAccessible(true);
                        if (tcn.map.get(field.getName()) != null) {
                            Object o;
                            ObjectSerializerTCN serializer = new ObjectSerializerTCN(tcn.getSub(field.getName()));
                            o = serializer.convertAll().done();
                            if (o != null)
                                field.set(object, o);
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            
            clazz = clazz.getSuperclass();
        }
    }
    
    public static boolean shouldSave(Field field) {
        return field.getDeclaredAnnotation(Save.class) != null;
    }
}
