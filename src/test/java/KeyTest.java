import tudbut.tools.encryption.SaferRawKey;

public class KeyTest {
    
    public static void main(String[] args) {
        SaferRawKey key = new SaferRawKey("hi");
        System.out.println(key.encryptString("hello!"));
        System.out.println(key.decryptString(key.encryptString("hello!")));
    }
}
