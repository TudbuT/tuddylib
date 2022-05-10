package tudbut.tools.encryption;

public class KeyStream {
    
    private int encPos = 0, decPos = 0;
    private final char[] key;
    
    public KeyStream(Key key) {
        this.key = key.string.toCharArray();
    }
    
    public int encrypt(int i) {
        return proc(i + key[encPos++ % key.length]);
    }
    
    public int decrypt(int i) {
        return proc(i - key[decPos++ % key.length]);
    }
    
    private int proc(int i) {
        return i & 0xff;
    }
}
