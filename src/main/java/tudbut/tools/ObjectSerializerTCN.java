package tudbut.tools;

import tudbut.parsing.TCN;
import tudbut.parsing.TudSort;

import java.lang.reflect.*;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ObjectSerializerTCN {
    
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
    
    TCN map = TCN.getEmpty();
    Object toBuild = null;
    boolean type;
    boolean array;
    boolean unable = false;
    
    static {
        doneObjects.put(Thread.currentThread(), new ArrayList<>());
    }
    
    public ObjectSerializerTCN(TCN tcn) {
        map = tcn;
        type = false;
    }
    
    public ObjectSerializerTCN(String s) throws TCN.TCNException {
        map = TCN.read(s);
        type = false;
    }
    
    public ObjectSerializerTCN(Object o) {
        toBuild = o;
        type = true;
    }
    
    public ObjectSerializerTCN(Class<?> c) {
        type = true;
    }
    
    @SafeVarargs
    public final <T> T done(T... ignore) {
        if (type)
            if(unable)
                return null;
            else
                return (T) map;
        else
            if(unable)
                return null;
            else
                return (T) toBuild;
    }
    
    private ObjectSerializerTCN convertAll0() throws ClassNotFoundException, IllegalAccessException {
        convertHeader();
        boolean b = false;
        if(toBuild != null || !type) {
            for (int i = 0; i < TypeConverter.values().length; i++) {
                if (TypeConverter.values()[i].impl.doesApply(Class.forName(map.getString("type")))) {
                    if (type) {
                        map.set("f", toBuild);
                    }
                    else {
                        toBuild = TypeConverter.values()[i].impl.object(map.getString("f"));
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
    
    public ObjectSerializerTCN convertAll() throws ClassNotFoundException, IllegalAccessException {
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
                map.set("type", "null");
                map.set("isArray", "false");
                unable = true;
                return;
            }
            
            array = toBuild.getClass().isArray();
            map.set("isArray", String.valueOf(array));
            String s;
            if(array)
                s = toBuild.getClass().getComponentType().getName();
            else
                s = toBuild.getClass().getName();
            map.set("type", s);
        }
        else {
            array = map.getBoolean("isArray");
            if(array)
                toBuild = Array.newInstance(forName(map.getString("type")), map.getInteger("f_len"));
            else
                toBuild = forceNewInstance(map.getString("type"));
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
                map.set("f_len", len);
                for (int i = 0; i < len; i++) {
                    String s = "";
                    for (int j = 0; j < TypeConverter.values().length; j++) {
                        TypeConverter converter = TypeConverter.values()[j];
                        if (converter.impl.doesApply(toBuild.getClass().getComponentType())) {
                            s = converter.impl.string(Array.get(toBuild, i));
                        }
                    }
                    map.set("f_" + i, s);
                }
            }
            else {
                int len = map.getInteger("f_len");
                for (int i = 0; i < len; i++) {
                    Object o = null;
                    for (int j = 0; j < TypeConverter.values().length; j++) {
                        TypeConverter converter = TypeConverter.values()[j];
                        if (converter.impl.doesApply(toBuild.getClass().getComponentType())) {
                            o = converter.impl.object(map.getString("f_" + i));
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
                            s = converter.impl.string(field.get(toBuild));
                        }
                    }
                    map.set("f_" + field.getName(), s);
                }
                else {
                    if (map.map.containsKey("f_" + field.getName())) {
                        Object o = null;
                        for (int j = 0; j < TypeConverter.values().length; j++) {
                            TypeConverter converter = TypeConverter.values()[j];
                            if (converter.impl.doesApply(field.getType())) {
                                o = converter.impl.object(map.getString("f_" + field.getName()));
                            }
                        }
                        if(!Modifier.toString(field.getModifiers()).contains("final") && !Modifier.toString(field.getModifiers()).contains("static") )
                            field.set(toBuild, o);
                    }
                }
                field.setAccessible(b);
            }
        }
    }
    
    private void convertObjectVars() throws IllegalAccessException, ClassNotFoundException {
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
                map.set("f_len", len);
                for (int i = 0; i < len; i++) {
                    Object o;
                    if(checkShouldNotConvert(o = Array.get(toBuild, i))) {
                        continue;
                    }
                    else
                        doneObjects.get(Thread.currentThread()).add(o);
                    ObjectSerializerTCN serializer = new ObjectSerializerTCN(o);
                    serializer.convertAll0();
                    map.set("f_" + i, serializer.map);
                }
            }
            else {
                int len = map.getInteger("f_len");
                for (int i = 0; i < len; i++) {
                    ObjectSerializerTCN serializer = new ObjectSerializerTCN(map.getSub("f_" + i));
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
                    ObjectSerializerTCN serializer = new ObjectSerializerTCN(o);
                    serializer.convertAll0();
                    map.set("f_" + field.getName(), serializer.map);
                }
                else {
                    if (map.map.containsKey("f_" + field.getName())) {
                        ObjectSerializerTCN serializer = new ObjectSerializerTCN(map.getSub("f_" + field.getName()));
                        serializer.convertAll0();
                        Object o = serializer.done();
                        if (!Modifier.toString(field.getModifiers()).contains("final")) {
                            if (!Modifier.toString(field.getModifiers()).contains("static") && o != null) {
                                field.set(toBuild, o);
                            }
                        }
                        else if(field.getType().isArray()) {
                            Object array = field.get(toBuild);
                            if(Array.getLength(array) == Array.getLength(o)) {
                                assert o != null;
                                System.arraycopy(o, 0, array, 0, Array.getLength(o));
                            }
                        }
                    }
                }
                field.setAccessible(b);
            }
        }
    }
    
    private Object forceNewInstance(String type) throws ClassNotFoundException {
        if("null".equals(type) || type == null)
            return null;
        
        Class<?> clazz = forName(type);
        return AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
            try {
                Constructor<?>[] constructors = clazz.getConstructors();
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
    }
    
    public interface TypeConverterImpl {
        String string(Object o);
        Object object(String s);
        
        boolean doesApply(Class<?> clazz);
    }
    
    public enum TypeConverter {
        BOOLEAN(new TypeConverterImpl() {
            @Override
            public String string(Object o) {
                return String.valueOf(o);
            }
    
            @Override
            public Object object(String s) {
                return Boolean.valueOf(s);
            }
    
            @Override
            public boolean doesApply(Class<?> clazz) {
                return clazz == boolean.class || clazz == Boolean.class;
            }
        }),
        BYTE(new TypeConverterImpl() {
            @Override
            public String string(Object o) {
                return String.valueOf(o);
            }
        
            @Override
            public Object object(String s) {
                return Byte.valueOf(s);
            }
    
            @Override
            public boolean doesApply(Class<?> clazz) {
                return clazz == byte.class || clazz == Byte.class;
            }
        }),
        SHORT(new TypeConverterImpl() {
            @Override
            public String string(Object o) {
                return String.valueOf(o);
            }
        
            @Override
            public Object object(String s) {
                return Short.valueOf(s);
            }
    
            @Override
            public boolean doesApply(Class<?> clazz) {
                return clazz == short.class || clazz == Short.class;
            }
        }),
        CHAR(new TypeConverterImpl() {
            @Override
            public String string(Object o) {
                return String.valueOf(o);
            }
        
            @Override
            public Object object(String s) {
                return (char) Integer.parseInt(s);
            }
    
            @Override
            public boolean doesApply(Class<?> clazz) {
                return clazz == char.class || clazz == Character.class;
            }
        }),
        INT(new TypeConverterImpl() {
            @Override
            public String string(Object o) {
                return String.valueOf(o);
            }
        
            @Override
            public Object object(String s) {
                return Integer.valueOf(s);
            }
    
            @Override
            public boolean doesApply(Class<?> clazz) {
                return clazz == int.class || clazz == Integer.class;
            }
        }),
        FLOAT(new TypeConverterImpl() {
            @Override
            public String string(Object o) {
                return String.valueOf(o);
            }
        
            @Override
            public Object object(String s) {
                return Float.valueOf(s);
            }
    
            @Override
            public boolean doesApply(Class<?> clazz) {
                return clazz == float.class || clazz == Float.class;
            }
        }),
        LONG(new TypeConverterImpl() {
            @Override
            public String string(Object o) {
                return String.valueOf(o);
            }
        
            @Override
            public Object object(String s) {
                return Long.valueOf(s);
            }
    
            @Override
            public boolean doesApply(Class<?> clazz) {
                return clazz == long.class || clazz == Long.class;
            }
        }),
        DOUBLE(new TypeConverterImpl() {
            @Override
            public String string(Object o) {
                return String.valueOf(o);
            }
        
            @Override
            public Object object(String s) {
                    return Double.valueOf(s);
            }
    
            @Override
            public boolean doesApply(Class<?> clazz) {
                return clazz == double.class || clazz == Double.class;
            }
        }),
        STRING(new TypeConverterImpl() {
            @Override
            public String string(Object o) {
                return (String) o;
            }
        
            @Override
            public Object object(String s) {
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
    
    public static Class<?> forName(String s) throws ClassNotFoundException {
        for (int i = 0; i < nativeTypes.length; i++) {
            if(nativeTypes[i].getName().equals(s))
                return nativeTypes[i];
        }
        return Class.forName(s);
    }
}
