package tudbut.tools.encryption;

import de.tudbut.tools.Hasher;
import de.tudbut.tools.Tools;
import tudbut.parsing.TCN;
import tudbut.tools.ObjectSerializerTCN;

import java.util.Objects;

public class Key {

    protected final String string;
    
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
    
    public Key(String s) {
        string = s;
    }
    
    public Key(byte[] bytes) {
        string = new String(bytes);
    }
    
    public boolean equals(Key other) {
        return string.equals(other.string);
    }
    
    public String toHashString() {
        String[] strings = string.split("_");
        StringBuilder hash = new StringBuilder();
        for (int i = 0 ; i < strings.length ; i++) {
            hash.append(Hasher.sha512hex(strings[i]));
        }
        return hash.toString();
    }
    
    public byte[] toBytes() {
        return string.getBytes();
    }
    
    @Override
    public String toString() {
        return string;
    }
    
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
    
    public String encryptObject(Object o) {
        return encryptString(Tools.mapToString(Objects.requireNonNull(new ObjectSerializerTCN(o).convertAll().done(TCN.getEmpty())).toMap()));
    }
    
    public <T> T decryptObject(String s) {
        return new ObjectSerializerTCN(TCN.readMap(Tools.stringToMap(decryptString(s)))).convertAll().done();
    }
}
