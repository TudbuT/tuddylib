package tudbut.tools;

import de.tudbut.tools.Tools;
import tudbut.parsing.TudSort;

import java.lang.reflect.*;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ObjectSerializer {
    
    private static final Map<Thread, ArrayList<Object>> doneObjects = new HashMap<>();
    
    private static final Class<?>[] nativeTypes = new Class[] {
            boolean.class,
            byte.class,
            short.class,
            char.class,
            int.class,
            float.class,
            long.class,
            double.class,
            void.class,
            String.class
    };
    
    Map<String, String> map = new HashMap<>();
    Object toBuild = null;
    boolean type;
    boolean array;
    boolean unable = false;
    
    {
        if(!doneObjects.containsKey(Thread.currentThread()))
            doneObjects.put(Thread.currentThread(), new ArrayList<>());
    }
    
    public ObjectSerializer(String s) {
        map = Tools.stringToMap(s);
        type = false;
    }
    
    public ObjectSerializer(Object o) {
        toBuild = o;
        type = true;
    }
    
    public ObjectSerializer(Class<?> c) {
        type = true;
    }
    
    boolean rv = false;
    
    public ObjectSerializer useReadableValues(boolean b) {
        rv = b;
        return this;
    }
    
    public <T> T done(T... ignore) {
        if (type)
            if(unable)
                return null;
            else
                return (T) Tools.mapToString(map);
        else
            if(unable)
                return null;
            else
                return (T) toBuild;
    }
    
    private ObjectSerializer convertAll0() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        convertHeader();
        boolean b = false;
        if(toBuild != null || !type) {
            for (int i = 0; i < TypeConverter.values().length; i++) {
                if (TypeConverter.values()[i].impl.doesApply(forName(map.get("$type")))) {
                    if (type) {
                        map.put("f", TypeConverter.values()[i].impl.string(toBuild, rv));
                    }
                    else {
                        toBuild = TypeConverter.values()[i].impl.object(map.get("f"), rv);
                    }
                    unable = false;
                    return this;
                }
            }
            
        }
        if(unable)
            return this;
        convertNativeVars();
        convertObjectVars();
        return this;
    }
    
    public ObjectSerializer convertAll() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        doneObjects.get(Thread.currentThread()).clear();
        return convertAll0();
    }
    
    private boolean checkShouldNotConvert(Object o) {
        ArrayList<Object> converted = doneObjects.get(Thread.currentThread());
    
        for (int i = 0; i < converted.size(); i++) {
            if(converted.get(i) == o)
                return true;
        }
        converted.add(o);
        return false;
    }
    
    private void convertHeader() throws ClassNotFoundException {
        if(type) {
            if(toBuild == null) {
                map.put("$type", "null");
                map.put("$isArray", "false");
                unable = true;
                return;
            }
            
            array = toBuild.getClass().isArray();
            map.put("$isArray", String.valueOf(array));
            String s;
            if(array)
                s = toBuild.getClass().getComponentType().getName();
            else
                s = toBuild.getClass().getName();
            map.put("$type", s);
        }
        else {
            array = Boolean.parseBoolean(map.get("$isArray"));
            if(array)
                toBuild = Array.newInstance(forName(map.get("$type")), (Integer) TypeConverter.INT.impl.object(map.get("len"), rv));
            else
                toBuild = forceNewInstance(map.get("$type"));
            if(toBuild == null) {
                unable = true;
            }
        }
    }
    
    private void convertNativeVars() throws IllegalAccessException {
        if(unable)
            return;
        
        if(array) {
            boolean b = false;
            for (int j = 0; j < nativeTypes.length; j++) {
                if (nativeTypes[j] == toBuild.getClass().getComponentType()) {
                    b = true;
                    break;
                }
            }
            if (!b)
                return;
            if(type) {
                int len = Array.getLength(toBuild);
                map.put("len", TypeConverter.INT.impl.string(len, rv));
                for (int i = 0; i < len; i++) {
                    String s = "";
                    for (int j = 0; j < TypeConverter.values().length; j++) {
                        TypeConverter converter = TypeConverter.values()[j];
                        if (converter.impl.doesApply(toBuild.getClass().getComponentType())) {
                            s = converter.impl.string(Array.get(toBuild, i), rv);
                        }
                    }
                    map.put(TypeConverter.INT.impl.string(i, rv), s);
                }
            }
            else {
                int len = (int) TypeConverter.INT.impl.object(map.get("len"), rv);
                for (int i = 0; i < len; i++) {
                    Object o = null;
                    for (int j = 0; j < TypeConverter.values().length; j++) {
                        TypeConverter converter = TypeConverter.values()[j];
                        if (converter.impl.doesApply(toBuild.getClass().getComponentType())) {
                            o = converter.impl.object(map.get(TypeConverter.INT.impl.string(i, rv)), rv);
                        }
                    }
                    Array.set(toBuild, i, o);
                }
            }
        }
        else {
            Field[] fields = toBuild.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                boolean b = field.isAccessible();
                boolean b1 = false;
                for (int j = 0; j < nativeTypes.length; j++) {
                    if (nativeTypes[j] == field.getType()) {
                        b1 = true;
                        break;
                    }
                }
                if (!b1)
                    continue;
                if (!b)
                    field.setAccessible(true);
        
                if (type) {
                    String s = "";
                    for (int j = 0; j < TypeConverter.values().length; j++) {
                        TypeConverter converter = TypeConverter.values()[j];
                        if (converter.impl.doesApply(field.getType())) {
                            s = converter.impl.string(field.get(toBuild), rv);
                        }
                    }
                    map.put(field.getName(), s);
                }
                else {
                    if (map.containsKey(field.getName())) {
                        Object o = null;
                        for (int j = 0; j < TypeConverter.values().length; j++) {
                            TypeConverter converter = TypeConverter.values()[j];
                            if (converter.impl.doesApply(field.getType())) {
                                o = converter.impl.object(map.get(field.getName()), rv);
                            }
                        }
                        if(!Modifier.toString(field.getModifiers()).contains("final"))
                            field.set(toBuild, o);
                    }
                }
                field.setAccessible(b);
            }
        }
    }
    
    private void convertObjectVars() throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        if(unable)
            return;
        if(array) {
            boolean b = false;
            for (int j = 0; j < nativeTypes.length; j++) {
                if (nativeTypes[j] == toBuild.getClass().getComponentType()) {
                    b = true;
                    break;
                }
            }
            if (b)
                return;
            if(type) {
                int len = Array.getLength(toBuild);
                map.put("len", TypeConverter.INT.impl.string(len, rv));
                for (int i = 0; i < len; i++) {
                    Object o;
                    if(checkShouldNotConvert(o = Array.get(toBuild, i))) {
                        continue;
                    }
                    else
                        doneObjects.get(Thread.currentThread()).add(o);
                    ObjectSerializer serializer = new ObjectSerializer(o).useReadableValues(rv);
                    serializer.convertAll0();
                    map.put(TypeConverter.INT.impl.string(i, rv), serializer.done());
                }
            }
            else {
                int len = (int) TypeConverter.INT.impl.object(map.get("len"), rv);
                for (int i = 0; i < len; i++) {
                    ObjectSerializer serializer = new ObjectSerializer(map.get(TypeConverter.INT.impl.string(i, rv))).useReadableValues(rv);
                    serializer.convertAll0();
                    if (serializer.done() != null)
                        Array.set(toBuild, i, serializer.done());
                }
            }
        }
        else {
            Field[] fields = toBuild.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                boolean b = field.isAccessible();
                boolean b1 = false;
                for (int j = 0; j < nativeTypes.length; j++) {
                    if (nativeTypes[j] == field.getType()) {
                        b1 = true;
                        break;
                    }
                }
                if (b1)
                    continue;
                if (!b)
                    field.setAccessible(true);
        
                if (type) {
                    Object o;
                    if(checkShouldNotConvert(o = field.get(toBuild))) {
                        continue;
                    }
                    else
                        doneObjects.get(Thread.currentThread()).add(o);
                    ObjectSerializer serializer = new ObjectSerializer(o).useReadableValues(rv);
                    serializer.convertAll0();
                    map.put(field.getName(), serializer.done());
                }
                else {
                    if (map.containsKey(field.getName())) {
                        ObjectSerializer serializer = new ObjectSerializer(map.get(field.getName())).useReadableValues(rv);
                        serializer.convertAll0();
                        Object o = serializer.done();
                        if (!Modifier.toString(field.getModifiers()).contains("final")) {
                            if (o != null) {
                                field.set(toBuild, o);
                            }
                        }
                        else if(field.getType().isArray()) {
                            Object array = field.get(toBuild);
                            if(Array.getLength(array) == Array.getLength(o)) {
                                System.arraycopy(o, 0, array, 0, Array.getLength(o));
                            }
                        }
                    }
                }
                field.setAccessible(b);
            }
        }
    }
    
    private Object forceNewInstance(String type) {
        if("null".equals(type) || type == null)
            return null;
    
        try {
            Class<?> clazz = forName(type);
            return AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                try {
                    assert clazz != null;
                    Constructor<?>[] constructors = clazz.getDeclaredConstructors();
                    constructors = TudSort.sort(constructors, Constructor::getParameterCount);
                    boolean b = constructors[0].isAccessible();
                    constructors[0].setAccessible(true);
                    Object o = constructors[0].newInstance((Object[]) Array.newInstance(Object.class, constructors[0].getParameterCount()));
                    constructors[0].setAccessible(b);
                    return o;
                }
                catch (ArrayIndexOutOfBoundsException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    try {
                        return clazz.newInstance();
                    }
                    catch (Throwable ignore) {
                        return null;
                    }
                }
            });
        } catch (NullPointerException e) {
            return null;
        }
    }
    
    public interface TypeConverterImpl {
        String string(Object o, boolean rv);
        Object object(String s, boolean rv);
        
        boolean doesApply(Class<?> clazz);
    }
    
    public enum TypeConverter {
        BOOLEAN(new TypeConverterImpl() {
            @Override
            public String string(Object o, boolean rv) {
                if(rv)
                    return String.valueOf(o);
                
                return ((boolean) o) ? "\u0001" : "\u0000";
            }
    
            @Override
            public Object object(String s, boolean rv) {
                if(rv)
                    return Boolean.valueOf(s);
    
                return s.equals("\u0001");
            }
    
            @Override
            public boolean doesApply(Class<?> clazz) {
                return clazz == boolean.class || clazz == Boolean.class;
            }
        }),
        BYTE(new TypeConverterImpl() {
            @Override
            public String string(Object o, boolean rv) {
                if(rv)
                    return String.valueOf(o);
                
                return new String(new char[] { (char) (byte) o });
            }
        
            @Override
            public Object object(String s, boolean rv) {
                if(rv)
                    return Byte.valueOf(s);
    
                return (byte) s.charAt(0);
            }
    
            @Override
            public boolean doesApply(Class<?> clazz) {
                return clazz == byte.class || clazz == Byte.class;
            }
        }),
        SHORT(new TypeConverterImpl() {
            @Override
            public String string(Object o, boolean rv) {
                if(rv)
                    return String.valueOf(o);
    
                String r = "";
                r += (char) (((short) o >> 1 * 8) & 0xff);
                r += (char) (((short) o >> 0 * 8) & 0xff);
                return r;
            }
        
            @Override
            public Object object(String s, boolean rv) {
                if(rv)
                    return Short.valueOf(s);
    
                short r = 0;
                r += ((short) s.charAt(0) & 0xff) << 1 * 8;
                r += ((short) s.charAt(1) & 0xff) << 0 * 8;
                return r;
            }
    
            @Override
            public boolean doesApply(Class<?> clazz) {
                return clazz == short.class || clazz == Short.class;
            }
        }),
        CHAR(new TypeConverterImpl() {
            @Override
            public String string(Object o, boolean rv) {
                if(rv)
                    return String.valueOf(o);
    
                String r = "";
                r += (char) (((char) o >> 1 * 8) & 0xff);
                r += (char) (((char) o >> 0 * 8) & 0xff);
                return r;
            }
        
            @Override
            public Object object(String s, boolean rv) {
                if(rv)
                    return (char) Integer.parseInt(s);
    
                char r = 0;
                r += (s.charAt(0) & 0xff) << 1 * 8;
                r += (s.charAt(1) & 0xff) << 0 * 8;
                return r;
            }
    
            @Override
            public boolean doesApply(Class<?> clazz) {
                return clazz == char.class || clazz == Character.class;
            }
        }),
        INT(new TypeConverterImpl() {
            @Override
            public String string(Object o, boolean rv) {
                if(rv)
                    return String.valueOf(o);
    
                String r = "";
                r += (char) (((int) o >> 3 * 8) & 0xff);
                r += (char) (((int) o >> 2 * 8) & 0xff);
                r += (char) (((int) o >> 1 * 8) & 0xff);
                r += (char) (((int) o >> 0 * 8) & 0xff);
                return r;
            }
        
            @Override
            public Object object(String s, boolean rv) {
                if(rv)
                    return Integer.valueOf(s);
    
                int r = 0;
                r += ((int) s.charAt(0) & 0xff) << 3 * 8;
                r += ((int) s.charAt(1) & 0xff) << 2 * 8;
                r += ((int) s.charAt(2) & 0xff) << 1 * 8;
                r += ((int) s.charAt(3) & 0xff) << 0 * 8;
                return r;
            }
    
            @Override
            public boolean doesApply(Class<?> clazz) {
                return clazz == int.class || clazz == Integer.class;
            }
        }),
        FLOAT(new TypeConverterImpl() {
            @Override
            public String string(Object o, boolean rv) {
                if(rv)
                    return String.valueOf(o);
    
                return TypeConverter.INT.impl.string(Float.floatToIntBits((Float) o), rv);
            }
        
            @Override
            public Object object(String s, boolean rv) {
                if(rv)
                    return Float.valueOf(s);
                
                return Float.intBitsToFloat((Integer) TypeConverter.INT.impl.object(s, rv));
            }
    
            @Override
            public boolean doesApply(Class<?> clazz) {
                return clazz == float.class || clazz == Float.class;
            }
        }),
        LONG(new TypeConverterImpl() {
            @Override
            public String string(Object o, boolean rv) {
                if(rv)
                    return String.valueOf(o);
    
                String r = "";
                r += (char) ((long) o >> 7 * 8);
                r += (char) ((long) o >> 6 * 8);
                r += (char) ((long) o >> 5 * 8);
                r += (char) ((long) o >> 4 * 8);
                r += (char) ((long) o >> 3 * 8);
                r += (char) ((long) o >> 2 * 8);
                r += (char) ((long) o >> 1 * 8);
                r += (char) ((long) o >> 0 * 8);
                return r;
            }
        
            @Override
            public Object object(String s, boolean rv) {
                if(rv)
                    return Long.valueOf(s);
                
                long r = 0;
                r += ((long) s.charAt(0) & 0xff) << 7 * 8;
                r += ((long) s.charAt(1) & 0xff) << 6 * 8;
                r += ((long) s.charAt(2) & 0xff) << 5 * 8;
                r += ((long) s.charAt(3) & 0xff) << 4 * 8;
                r += ((long) s.charAt(4) & 0xff) << 3 * 8;
                r += ((long) s.charAt(5) & 0xff) << 2 * 8;
                r += ((long) s.charAt(6) & 0xff) << 1 * 8;
                r += ((long) s.charAt(7) & 0xff) << 0 * 8;
                return r;
            }
    
            @Override
            public boolean doesApply(Class<?> clazz) {
                return clazz == long.class || clazz == Long.class;
            }
        }),
        DOUBLE(new TypeConverterImpl() {
            @Override
            public String string(Object o, boolean rv) {
                if(rv)
                    return String.valueOf(o);
                
                return TypeConverter.LONG.impl.string(Double.doubleToLongBits((Double) o), rv);
            }
        
            @Override
            public Object object(String s, boolean rv) {
                if(rv)
                    return Double.valueOf(s);
    
                return Double.longBitsToDouble((Long) TypeConverter.LONG.impl.object(s, rv));
            }
    
            @Override
            public boolean doesApply(Class<?> clazz) {
                return clazz == double.class || clazz == Double.class;
            }
        }),
        STRING(new TypeConverterImpl() {
            @Override
            public String string(Object o, boolean rv) {
                return (String) o;
            }
        
            @Override
            public Object object(String s, boolean rv) {
                return s;
            }
        
            @Override
            public boolean doesApply(Class<?> clazz) {
                return clazz == String.class;
            }
        }),
        ;
        
        public final TypeConverterImpl impl;
        
        TypeConverter(TypeConverterImpl impl) {
            this.impl = impl;
        }
        
        public static TypeConverter forType(Class<?> clazz) {
            TypeConverter[] v = values();
            for (int i = 0; i < v.length; i++) {
                if(v[i].impl.doesApply(clazz))
                    return v[i];
            }
            
            return null;
        }
    }
    
    public static Class<?> forName(String s) {
        try {
            for (int i = 0; i < nativeTypes.length; i++) {
                if (nativeTypes[i].getName().equals(s))
                    return nativeTypes[i];
            }
            return Class.forName(s);
        } catch (ClassNotFoundException | NullPointerException e) {
            return null;
        }
    }
}
