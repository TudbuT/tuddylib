package tudbut.test;

import tudbut.net.http.HTTPUtils;
import tudbut.parsing.JSON;
import tudbut.tools.encryption.Key;
import tudbut.tools.encryption.RawKey;

import java.io.IOException;

public class Test1 {
    
    public static void main(String[] args) throws IOException, JSON.JSONFormatException {
    
        RawKey key = new RawKey();
        System.out.println(key.encryptString("\u00ff"));
        System.out.println(key.decryptString(key.encryptString("\u00ff")) + "\u00ff");
    }
}
