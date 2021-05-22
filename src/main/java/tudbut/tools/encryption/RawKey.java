package tudbut.tools.encryption;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

/**
 * Key to encrypt objects and strings
 */
public class RawKey extends Key {
    
    /**
     * Generates a random Key
     */
    public RawKey() {
        super();
    }
    
    /**
     * Constructs a Key
     * @param s Key as string
     */
    public RawKey(String s) {
        super(s);
    }
    
    /**
     * Constructs a Key
     * @param bytes Key as byte[]
     */
    public RawKey(byte[] bytes) {
        super(new String(bytes, StandardCharsets.ISO_8859_1));
    }
    
    /**
     * Gets the bytes of the key
     * @return the bytes of the key
     */
    public byte[] toBytes() {
        return string.getBytes(StandardCharsets.ISO_8859_1);
    }
    
    /**
     * Encrypts a string
     * @param s string to encrypt
     * @return encrypted string
     */
    public String encryptString(String s) {
        byte[] bytes = s.getBytes(StandardCharsets.ISO_8859_1);
        byte[] eb = string.getBytes(StandardCharsets.ISO_8859_1);
        int len = bytes.length;
        int p = eb.length;
        for (int i = 0 ; i < len ; i+=p) {
            for (int j = 0 ; j < p && i + j < len ; j++) {
                int idx = i + j;
                bytes[idx] = (byte) ((int) bytes[idx] + (int) eb[j]);
            }
        }
        return new String(bytes, StandardCharsets.ISO_8859_1);
    }
    
    /**
     * Decrypts a string
     * @param s string to decrypt
     * @return decrypted string
     */
    public String decryptString(String s) {
        byte[] bytes = s.getBytes(StandardCharsets.ISO_8859_1);
        byte[] eb = string.getBytes(StandardCharsets.ISO_8859_1);
        int len = bytes.length;
        int p = eb.length;
        for (int i = 0 ; i < len ; i+=p) {
            for (int j = 0 ; j < p && i + j < len ; j++) {
                int idx = i + j;
                bytes[idx] = (byte) ((int) bytes[idx] - (int) eb[j]);
            }
        }
        return new String(bytes, StandardCharsets.ISO_8859_1);
    }
    
    @Override
    protected RawKey clone() {
        try {
            return (RawKey) Object.class.getDeclaredMethod("clone").invoke(this);
        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return new RawKey(string);
        }
    }
}
