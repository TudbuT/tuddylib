package tudbut.tools;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ReflectUtil {
    
    public static boolean hasAnnotation(Field field, Class<? extends Annotation> clazz) {
        return field.getDeclaredAnnotation(clazz) != null;
    }
    
}
