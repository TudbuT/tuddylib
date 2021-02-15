package de.tudbut;

import tudbut.obj.Save;
import tudbut.obj.TLMap;
import tudbut.parsing.TCN;
import tudbut.tools.NoiseGenerator;
import tudbut.tools.StringTools;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class ee {
    @Save
    public int i = (int) (Math.random() * 50);
    @Save
    public static TLMap<String, Object> data = new TLMap<>();
    
    public static void main(String[] args) throws IllegalAccessException, ClassNotFoundException, NoSuchFieldException, InstantiationException, IOException, TCN.TCNException, NoSuchAlgorithmException {
        float[][][] map = NoiseGenerator.generateRandom(1, 500, 500, 100, 2, new Random());
    
        System.out.println();
        for (int x = 0; x < 1; x++) {
            for (float y = 0; y < 500; y+=5.25) {
                for (int z = 0; z < 500; z++) {
                    if(map[x][(int) y][z] >= 1.015)
                        System.out.print(" ");
                    else
                        System.out.print("#");
                }
                System.out.println();
            }
        }
    }
}
