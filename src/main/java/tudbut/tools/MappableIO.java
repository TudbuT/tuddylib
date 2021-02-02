package tudbut.tools;

import de.tudbut.tools.Tools;
import tudbut.obj.Mappable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class MappableIO {
    
    public static Map<String, String> map(Mappable mappable) {
        Map<String, String> map =  mappable.map();
        map.put("Object.class", mappable.getClass().getName());
        return map;
    }
    
    public static <T extends Mappable> T get(Class<T> tClass, Map<String, String> map) {
        try {
            Method m = tClass.getMethod("fromMap", Map.class);
            if (m.getReturnType().equals(tClass)) {
                return (T) m.invoke(null, map);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) { }
        throw new IllegalArgumentException();
    }
    
    public static <T extends Mappable> T get(Class<T> tClass, String mapString) {
        return get(tClass, Tools.stringToMap(mapString));
    }
    
    public static <T extends Mappable> T get(Map<String, String> map) {
        try {
            Class<T> tClass = (Class<T>) Class.forName(map.get("Object.class"));
            Method m = tClass.getMethod("fromMap", Map.class);
            if (m.getReturnType().equals(tClass)) {
                return (T) m.invoke(null, map);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            throw new IllegalArgumentException(Tools.mapToString(map), e);
        }
        throw new IllegalArgumentException(Tools.mapToString(map));
    }
    
    public static <T extends Mappable> T get(String mapString) {
        return get(Tools.stringToMap(mapString));
    }
}
