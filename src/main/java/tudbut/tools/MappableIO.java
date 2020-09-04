package tudbut.tools;

import de.tudbut.tools.Tools;
import tudbut.obj.Mappable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class MappableIO {
    
    public static Map<String, String> map(Mappable mappable) {
        return mappable.map();
    }
    
    public static <T extends Mappable> T get(Class<T> tClass, Map<String, String> map) {
        try {
            Method m = tClass.getMethod("fromMap");
            if (m.getReturnType().equals(tClass)) {
                return (T) m.invoke(null, map);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) { }
        throw new IllegalArgumentException();
    }
    
    public static <T extends Mappable> T get(Class<T> tClass, String mapString) {
        return get(tClass, Tools.stringToMap(mapString));
    }
}
