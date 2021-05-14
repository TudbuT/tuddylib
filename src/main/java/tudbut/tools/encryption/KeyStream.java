package tudbut.tools.encryption;

public class KeyStream {
    
    private int epos = 0, dpos = 0;
    private final String key;
    
    public KeyStream(Key key) {
        this.key = key.string;
    }
    
    public int encrypt(int i) {
        return proc(key.charAt(epos++ % key.length()) + i);
    }
    
    public int decrypt(int i) {
        return proc(key.charAt(dpos++ % key.length()) - i);
    }
    
    private int proc(int i) {
        if(i < 0) {
            i = -i;
        }
        return i & 0xff;
    }
}
