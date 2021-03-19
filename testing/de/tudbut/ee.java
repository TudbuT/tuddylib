package de.tudbut;

import tudbut.obj.Save;
import tudbut.obj.TLMap;
import tudbut.parsing.TCN;
import tudbut.tools.MoreMath;
import tudbut.tools.NoiseGenerator;
import tudbut.tools.StringTools;
import tudbut.tools.Time;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

public class ee {
    @Save
    public int i = (int) (Math.random() * 50);
    @Save
    public static TLMap<String, Object> data = new TLMap<>();
    
    public static void main(String[] args) throws IllegalAccessException, ClassNotFoundException, NoSuchFieldException, InstantiationException, IOException, TCN.TCNException, NoSuchAlgorithmException, InterruptedException {
    
        System.out.println(new Date().getTime());
        System.out.println(new Date().toInstant().getNano());
        Time.wait(0, 100000);
        System.out.println(new Date().getTime());
        System.out.println(new Date().toInstant().getNano());
    }
}
