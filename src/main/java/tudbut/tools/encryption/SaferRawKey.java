package tudbut.tools.encryption;

import java.nio.charset.StandardCharsets;

public class SaferRawKey extends RawKey {
    
    public SaferRawKey() {
        super();
    }
    
    public SaferRawKey(String s) {
        super(s);
    }
    
    public SaferRawKey(byte[] bytes) {
        super(bytes);
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
                byte o = bytes[idx];
                bytes[idx] += eb[j];
                eb[(j + 1) % p] += o;
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
                bytes[idx] -= eb[j];
                eb[(j + 1) % p] += bytes[idx];
            }
        }
        return new String(bytes, StandardCharsets.ISO_8859_1);
    }
}
