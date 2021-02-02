package tudbut.tools;

import de.tudbut.tools.Tools;
import de.tudbut.tools.bintools.BinFileRW;

import java.awt.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class Serializing {
    
    public static byte[] serialize(Object o, Class<?> c) throws IllegalAccessException {
        Map<String, String> map = new HashMap<>();
    
        map.put(idToString(0x00), c.getName());
        
        if(c.getSuperclass() != null)
            map.put(
                    idToString(0x01),
                    new String(
                            Tools.intArrayToCharArray(
                                    Tools.byteArrayToUnsignedIntArray(
                                            serialize(o, c.getSuperclass())
                                    )
                            )
                    )
            );
        
        Map<String, String> fields = new HashMap<>();
        for (Field field : c.getDeclaredFields()) {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            
            writeField(fields, field, o);
            
            field.setAccessible(accessible);
        }
        
        map.put(idToString(0x02), prep(fields));
        
        return Tools.charArrayToByteArray(prep(map).toCharArray());
    }
    
    @SuppressWarnings("DuplicatedCode")
    private static void writeField(Map<String, String> fields, Field field, Object o) throws IllegalAccessException {
    
        if(field.getType() == boolean.class) {
            fields.put(field.getName(), writeBoolean(field.getBoolean(o)));
        }
        else
        if(field.getType() == byte.class) {
            fields.put(field.getName(), writeByte(field.getByte(o)));
        }
        else
        if(field.getType() == short.class) {
            fields.put(field.getName(), writeShort(field.getShort(o)));
        }
        else
        if(field.getType() == char.class) {
            fields.put(field.getName(), writeChar(field.getChar(o)));
        }
        else
        if(field.getType() == int.class) {
            fields.put(field.getName(), writeInt(field.getInt(o)));
        }
        else
        if(field.getType() == long.class) {
            fields.put(field.getName(), writeLong(field.getLong(o)));
        }
        else
        if(field.getType() == double.class) {
            fields.put(field.getName(), writeDouble(field.getDouble(o)));
        }
        else
        if(field.getType() == float.class) {
            fields.put(field.getName(), writeFloat(field.getFloat(o)));
        }
        else
        if(field.getType().isArray()) {
            Map<String, String> arrayMap = new HashMap<>();
            writeArray(arrayMap, field.get(o));
            fields.put(field.getName(), prep(arrayMap));
        }
        else
        {
            fields.put(field.getName(), writeObject(field.get(o)));
        }
    }
    
    private static void writeArray(Map<String, String> map, Object o) throws IllegalAccessException {
        Class<?> c = o.getClass();
        
        if(c.getComponentType() == boolean.class) {
            boolean[] a = (boolean[]) o;
            map.put(idToString(0x00), writeInt(a.length));
            for (int i = 0; i < a.length; i++) {
                map.put(writeInt(i), writeBoolean(a[i]));
            }
        }
        else
        if(c.getComponentType() == byte.class) {
            byte[] a = (byte[]) o;
            map.put(idToString(0x00), writeInt(a.length));
            for (int i = 0; i < a.length; i++) {
                map.put(writeInt(i), writeByte(a[i]));
            }
        }
        else
        if(c.getComponentType() == short.class) {
            short[] a = (short[]) o;
            map.put(idToString(0x00), writeInt(a.length));
            for (int i = 0; i < a.length; i++) {
                map.put(writeInt(i), writeShort(a[i]));
            }
        }
        else
        if(c.getComponentType() == char.class) {
            char[] a = (char[]) o;
            map.put(idToString(0x00), writeInt(a.length));
            for (int i = 0; i < a.length; i++) {
                map.put(writeInt(i), writeChar(a[i]));
            }
        }
        else
        if(c.getComponentType() == int.class) {
            int[] a = (int[]) o;
            map.put(idToString(0x00), writeInt(a.length));
            for (int i = 0; i < a.length; i++) {
                map.put(writeInt(i), writeInt(a[i]));
            }
        }
        else
        if(c.getComponentType() == long.class) {
            long[] a = (long[]) o;
            map.put(idToString(0x00), writeInt(a.length));
            for (int i = 0; i < a.length; i++) {
                map.put(writeInt(i), writeLong(a[i]));
            }
        }
        else
        if(c.getComponentType() == double.class) {
            double[] a = (double[]) o;
            map.put(idToString(0x00), writeInt(a.length));
            for (int i = 0; i < a.length; i++) {
                map.put(writeInt(i), writeDouble(a[i]));
            }
        }
        else
        if(c.getComponentType() == float.class) {
            float[] a = (float[]) o;
            map.put(idToString(0x00), writeInt(a.length));
            for (int i = 0; i < a.length; i++) {
                map.put(writeInt(i), writeFloat(a[i]));
            }
        }
        else
        if(c.getComponentType().isArray()) {
            int len = Array.getLength(o);
            map.put(idToString(0x00), writeInt(len));
            for (int i = 0; i < len; i++) {
                Map<String, String> arrayMap = new HashMap<>();
                writeArray(arrayMap, Array.get(o, i));
                map.put(writeInt(i), prep(arrayMap));
            }
        }
        else
        {
            Object[] a = (Object[]) o;
            map.put(idToString(0x00), writeInt(a.length));
            for (int i = 0; i < a.length; i++) {
                map.put(writeInt(i), writeObject(a[i]));
            }
        }
    }
    
    private static String writeBoolean(boolean o) {
        return idToString(o ? 0x01 : 0x00);
    }
    
    public static String writeByte(byte o) {
        return idToString(o);
    }
    
    public static String writeShort(short o) {
        return idToString(
                o >> 8 * 1 & 0xff,
                o >> 8 * 0 & 0xff
        );
    }
    
    public static String writeChar(char o) {
        return idToString(
                o >> 8 * 1 & 0xff,
                o >> 8 * 0 & 0xff
        );
    }
    
    public static String writeInt(int o) {
        return idToString(
                o >> 8 * 3 & 0xff,
                o >> 8 * 2 & 0xff,
                o >> 8 * 1 & 0xff,
                o >> 8 * 0 & 0xff
        );
    }
    
    public static String writeLong(long o) {
        return idToString(
                o >> 8 * 7 & 0xff,
                o >> 8 * 6 & 0xff,
                o >> 8 * 5 & 0xff,
                o >> 8 * 4 & 0xff,
                o >> 8 * 3 & 0xff,
                o >> 8 * 2 & 0xff,
                o >> 8 * 1 & 0xff,
                o >> 8 * 0 & 0xff
        );
    }
    
    public static String writeDouble(double o) {
        return writeLong(Double.doubleToLongBits(o));
    }
    
    public static String writeFloat(float o) {
        return writeInt(Float.floatToIntBits(o));
    }
    
    private static String writeObject(Object o) throws IllegalAccessException {
        return idToString(Tools.byteArrayToUnsignedIntArray(serialize(o, o.getClass())));
    }
    
    
    public static Object deserialize(byte[] bytes) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        return deserialize(new String(Tools.intArrayToCharArray(Tools.byteArrayToUnsignedIntArray(bytes))), null);
    }
    
    private static Object deserialize(String s, Object o) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        
        Map<String, String> map = prep(s);
        
        
        
        Class<?> c = Class.forName(map.get(idToString(0x00)));
        
        if(o == null)
            o = c.newInstance();
        
        if(c.getSuperclass() != null)
            deserialize(map.get(idToString(0x01)), o);
    
        Map<String, String> fields = new HashMap<>();
        for (Field field : c.getDeclaredFields()) {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
        
            readField(fields, field, o);
        
            field.setAccessible(accessible);
        }
        
        return o;
    }
    
    private static void readField(Map<String, String> fields, Field field, Object o) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
    
        if(field.getType() == boolean.class) {
            field.setBoolean(o, readBoolean(fields.get(field.getName())));
        }
        else
        if(field.getType() == byte.class) {
            field.setByte(o, readByte(fields.get(field.getName())));
        }
        else
        if(field.getType() == short.class) {
            field.setShort(o, readShort(fields.get(field.getName())));
        }
        else
        if(field.getType() == char.class) {
            field.setChar(o, readChar(fields.get(field.getName())));
        }
        else
        if(field.getType() == int.class) {
            field.setInt(o, readInt(fields.get(field.getName())));
        }
        else
        if(field.getType() == long.class) {
            field.setLong(o, readLong(fields.get(field.getName())));
        }
        else
        if(field.getType() == double.class) {
            field.setDouble(o, readDouble(fields.get(field.getName())));
        }
        else
        if(field.getType() == float.class) {
            field.setFloat(o, readFloat(fields.get(field.getName())));
        }
        else
        if(field.getType().isArray()) {
            field.set(o, readArray(field.getType(), prep(fields.get(field.getName()))));
        }
        else
        {
            field.set(o, deserialize(fields.get(field.getName()), null));
        }
    }
    
    private static Object readArray(Class<?> c, Map<String, String> data)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        
        Object r;
    
        if(c.getComponentType() == boolean.class) {
            boolean[] a = new boolean[readInt(data.get(idToString(0x00)))];
            for (int i = 0; i < a.length; i++) {
                a[i] = readBoolean(data.get(writeInt(i)));
            }
            r = a;
        }
        else
        if(c.getComponentType() == byte.class) {
            byte[] a = new byte[readInt(data.get(idToString(0x00)))];
            for (int i = 0; i < a.length; i++) {
                a[i] = readByte(data.get(writeInt(i)));
            }
            r = a;
        }
        else
        if(c.getComponentType() == short.class) {
            short[] a = new short[readInt(data.get(idToString(0x00)))];
            for (int i = 0; i < a.length; i++) {
                a[i] = readShort(data.get(writeInt(i)));
            }
            r = a;
        }
        else
        if(c.getComponentType() == char.class) {
            char[] a = new char[readInt(data.get(idToString(0x00)))];
            for (int i = 0; i < a.length; i++) {
                a[i] = readChar(data.get(writeInt(i)));
            }
            r = a;
        }
        else
        if(c.getComponentType() == int.class) {
            int[] a = new int[readInt(data.get(idToString(0x00)))];
            for (int i = 0; i < a.length; i++) {
                a[i] = readInt(data.get(writeInt(i)));
            }
            r = a;
        }
        else
        if(c.getComponentType() == long.class) {
            long[] a = new long[readInt(data.get(idToString(0x00)))];
            for (int i = 0; i < a.length; i++) {
                a[i] = readLong(data.get(writeInt(i)));
            }
            r = a;
        }
        else
        if(c.getComponentType() == double.class) {
            double[] a = new double[readInt(data.get(idToString(0x00)))];
            for (int i = 0; i < a.length; i++) {
                a[i] = readDouble(data.get(writeInt(i)));
            }
            r = a;
        }
        else
        if(c.getComponentType() == float.class) {
            float[] a = new float[readInt(data.get(idToString(0x00)))];
            for (int i = 0; i < a.length; i++) {
                a[i] = readFloat(data.get(writeInt(i)));
            }
            r = a;
        }
        else
        if(c.getComponentType().isArray()) {
            Object[] a = (Object[]) Array.newInstance(c.getComponentType(), readInt(data.get(idToString(0x00))));
            for (int i = 0; i < a.length; i++) {
                a[i] = readArray(c, prep(data.get(writeInt(i))));
            }
            r = a;
        }
        else
        {
            Object[] a = (Object[]) Array.newInstance(c.getComponentType(), readInt(data.get(idToString(0x00))));
            for (int i = 0; i < a.length; i++) {
                a[i] = deserialize(data.get(writeInt(i)), null);
            }
            r = a;
        }
        
        return r;
    }
    
    private static boolean readBoolean(String s) {
        return s.equals(idToString(0x01));
    }
    
    private static byte readByte(String s) {
        return (byte) stringToID(s);
    }
    
    private static short readShort(String s) {
        int[] o = stringToIDs(s);
        return
                (short) (
                        (o[0] << 8 * 1) +
                        (o[1] << 8 * 0)
                )
                ;
    }
    
    private static char readChar(String s) {
        int[] o = stringToIDs(s);
        return
                (char) (
                        (o[0] << 8 * 1) +
                        (o[1] << 8 * 0)
                )
                ;
    }
    
    private static int readInt(String s) {
        int[] o = stringToIDs(s);
        return
                (o[0] << 8 * 3) +
                (o[1] << 8 * 2) +
                (o[2] << 8 * 1) +
                (o[3] << 8 * 0)
                ;
    }
    
    private static long readLong(String s) {
        int[] o = stringToIDs(s);
        return
                ((long) o[0] << 8 * 7) +
                ((long) o[1] << 8 * 6) +
                ((long) o[2] << 8 * 5) +
                ((long) o[3] << 8 * 4) +
                ((long) o[4] << 8 * 3) +
                ((long) o[5] << 8 * 2) +
                ((long) o[6] << 8 * 1) +
                ((long) o[7] << 8 * 0);
    }
    
    private static double readDouble(String s) {
        return Double.longBitsToDouble(readLong(s));
    }
    
    private static float readFloat(String s) {
        return Float.intBitsToFloat(readInt(s));
    }
    
    
    
    private static int[] stringToIDs(String id) {
        int[] ints = new int[id.length()];
        for (int i = 0; i < id.length(); i++) {
            ints[i] = id.charAt(i) & 0xff;
        }
        return ints;
    }
    private static int stringToID(String id) {
        return (int) id.charAt(0) & 0xff;
    }
    
    private static String idToString(int... id) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < id.length; i++) {
            builder.append((char) id[i]);
        }
        return builder.toString();
    }
    
    private static String idToString(long... id) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < id.length; i++) {
            builder.append((char) id[i]);
        }
        return builder.toString();
    }
    
    private static String prep(Map<String, String> map) {
        StringBuilder r = new StringBuilder();
    
        for (String key : map.keySet().toArray(new String[0])) {
        
            r
                    .append(
                            key
                                    .replaceAll(idToString(0xfd), idToString(0xfd, 0x0d))
                                    .replaceAll(idToString(0xff), idToString(0xfd, 0x0f))
                                    .replaceAll(idToString(0xfe), idToString(0xfd, 0x0e))
                    )
                    .append(idToString(0xfe))
                    .append(
                            map.get(key)
                                    .replaceAll(idToString(0xfd), idToString(0xfd, 0x0d))
                                    .replaceAll(idToString(0xff), idToString(0xfd, 0x0f))
                                    .replaceAll(idToString(0xfe), idToString(0xfd, 0x0e))
                    )
                    .append(idToString(0xff))
            ;
        }
    
        return r.toString();
    }
    
    private static Map<String, String> prep(String string) {
    
        HashMap<String, String> map = new HashMap<>();
    
        for (int i = 0; i < string.split(idToString(0xff)).length; i++) {
            String tile = string.split(idToString(0xff))[i];
            if (tile.contains(idToString(0xfe))) {
                if (tile.split(idToString(0xfe)).length == 2)
                    map.put(
                            tile
                                    .split(idToString(0xfe))[0]
                                    .replaceAll(idToString(0xfd, 0x0e), idToString(0xfe))
                                    .replaceAll(idToString(0xfd, 0x0f), idToString(0xff))
                                    .replaceAll(idToString(0xfd, 0x0d), idToString(0xfd)),
                            tile
                                    .split(idToString(0xfe))[1]
                                    .replaceAll(idToString(0xfd, 0x0e), idToString(0xfe))
                                    .replaceAll(idToString(0xfd, 0x0f), idToString(0xff))
                                    .replaceAll(idToString(0xfd, 0x0d), idToString(0xfd))
                    );
                else
                    map.put(
                            tile
                                    .split(idToString(0xfe))[0]
                                    .replaceAll(idToString(0xfd, 0x0e), idToString(0xfe))
                                    .replaceAll(idToString(0xfd, 0x0f), idToString(0xff))
                                    .replaceAll(idToString(0xfd, 0x0d), idToString(0xfd))
                            , ""
                    );
            }
        }
    
        return map;
    }
}
