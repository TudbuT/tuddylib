package de.tudbut;

import de.tudbut.tools.ExtendedMath;
import de.tudbut.tools.Tools;
import de.tudbut.tools.bintools.BinFileRW;
import tudbut.tools.Serializing;

import java.io.IOException;

public class n {
    
    private String hi = "hey";
    
    public static void main(String[] args) throws IOException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        int r = ExtendedMath.random(30, 50);
        int i;
        
        for (i = 0; i < r; i++) {
            System.out.println(obfF(Tools.randomAlphanumericString(ExtendedMath.random(10, 50)), i));
        }
    
        System.out.println(obfF("alert('got');", i));
        
        r = ExtendedMath.random(30, 50);
        
        int oi = i;
    
        for (i++; i < r + oi; i++) {
            System.out.println(obfF(Tools.randomAlphanumericString(ExtendedMath.random(10, 50)), i));
        }
        
        System.out.println(oi);
    }
    
    public static String obfF(String s, int id) {
        return "_" + id + ":function(){let x=a(0);eval(" + obf(s) + ");},";
    }
    
    public static String obf(String s) {
        String r = "";
    
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            r += obfY(c);
            for (int j = 0; j < ExtendedMath.random(0, 3); j++) {
                r += obfN(Tools.randomAlphanumericString(1).toCharArray()[0]);
            }
        }
        return (r.substring(0, r.length() - 1));
    }
    
    public static String obfY(char c) {
        switch (ExtendedMath.random(0, 2)) {
            case 0:
                return "x._1(\"" + c + "\", null)+";
            case 1:
                return "x._1(\"Ö\")+x._1(\"" + c + "\")+";
            case 2:
                return "x._1(\"" + c + "\",\"\")+";
        }
        return "x._1(\"" + c + "\", null)+";
    }
    
    public static String obfN(char c) {
        switch (ExtendedMath.random(0, 2)) {
            case 0:
                return "x._1(\"" + c + "\")+";
            case 1:
                return "x._1(\"Ü\")+x._1(\"" + c + "\", null)+";
            case 2:
                return "x._1(\"" + c + "\",\"" + c + "\")+";
        }
        return "x._1(\"" + c + "\")+";
    }
}
