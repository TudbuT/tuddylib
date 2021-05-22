package tudbut.tools.encryption;

import de.tudbut.tools.Hasher;
import de.tudbut.tools.Tools;
import tudbut.parsing.TCN;
import tudbut.tools.ObjectSerializerTCN;

import java.util.Objects;

/**
 * Key to encrypt objects and strings
 */
public class Key implements Cloneable {
    
    protected final String string;
    
    /**
     * Generates a random Key
     */
    public Key() {
        StringBuilder string;
        string = new StringBuilder();
        for (int i = 0 ; i < 16 ; i++) {
            if(i != 0)
                string.append("_");
            string.append(Tools.randomAlphanumericString(64));
        }
        this.string = string.toString();
    }
    
    /**
     * Constructs a Key
     * @param s Key as string
     */
    public Key(String s) {
        string = s;
    }
    
    /**
     * Constructs a Key
     * @param bytes Key as byte[]
     */
    public Key(byte[] bytes) {
        string = new String(bytes);
    }
    
    /**
     * Compares two keys
     * @param other The key to compare to
     * @return If other is equal to this
     */
    public boolean equals(Key other) {
        return string.equals(other.string);
    }
    
    /**
     * Hashes the Kay
     * @return the hash
     */
    public String toHashString() {
        String[] strings = string.split("_");
        StringBuilder hash = new StringBuilder();
        for (int i = 0 ; i < strings.length ; i++) {
            hash.append(Hasher.sha512hex(strings[i]));
        }
        return hash.toString();
    }
    
    /**
     * Gets the bytes of the key
     * @return the bytes of the key
     */
    public byte[] toBytes() {
        return string.getBytes();
    }
    
    /**
     * Returns the key as string. USE {@link #toHashString} TO GET A HASH, THIS WILL RETURN THE ENCRYPTION KEY!
     * @return the key as string
     */
    @Override
    public String toString() {
        return string;
    }
    
    /**
     * Encrypts a string
     * @param s string to encrypt
     * @return encrypted string
     */
    public String encryptString(String s) {
        char[] bytes = s.toCharArray();
        char[] eb = string.toCharArray();
        int len = bytes.length;
        int p = eb.length;
        for (int i = 0 ; i < len ; i+=p) {
            for (int j = 0 ; j < p && i + j < len ; j++) {
                int idx = i + j;
                bytes[idx] = (char) ((int) bytes[idx] + (int) eb[j]);
            }
        }
        return new String(bytes);
    }
    
    /**
     * Decrypts a string
     * @param s string to decrypt
     * @return decrypted string
     */
    public String decryptString(String s) {
        char[] bytes = s.toCharArray();
        char[] eb = string.toCharArray();
        int len = bytes.length;
        int p = eb.length;
        for (int i = 0 ; i < len ; i+=p) {
            for (int j = 0 ; j < p && i + j < len ; j++) {
                int idx = i + j;
                bytes[idx] = (char) ((int) bytes[idx] - (int) eb[j]);
            }
        }
        return new String(bytes);
    }
    
    /**
     * Encrypts an object
     * @param o object to encrypt
     * @return encrypted string
     */
    public String encryptObject(Object o) {
        return encryptString(Tools.mapToString(Objects.requireNonNull(new ObjectSerializerTCN(o).convertAll().done(new TCN())).toMap()));
    }
    
    /**
     * Decrypts an object
     * @param s string to decrypt
     * @return decrypted object
     */
    public <T> T decryptObject(String s) {
        return new ObjectSerializerTCN(TCN.readMap(Tools.stringToMap(decryptString(s)))).convertAll().done();
    }
    
    @SuppressWarnings("CloneDoesntDeclareCloneNotSupportedException")
    @Override
    protected Key clone() {
        try {
            return (Key) super.clone();
        }
        catch (CloneNotSupportedException e) {
            return new Key(string);
        }
    }
}
