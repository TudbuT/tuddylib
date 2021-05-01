package tudbut.test;

import tudbut.parsing.JSON;
import tudbut.tools.encryption.Key;

import java.io.IOException;

public class Test1 {
    
    public static void main(String[] args) throws IOException, JSON.JSONFormatException {
    
        Key key = new Key("\uffff\ufffe\ufffd\ufffc\ufffb\ufffa");
        System.out.println(key.encryptString("test"));
        System.out.println(key.decryptString(key.encryptString("test")));
    }
}
