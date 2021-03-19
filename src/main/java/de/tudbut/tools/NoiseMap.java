package de.tudbut.tools;

public class NoiseMap {
    public static byte[][] generate2D(int sizeX, int sizeY, int multiplier1, int multiplier2, int unifier1, int unifier2) {
        byte[][] r = new byte[sizeX][sizeY];

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                r[x][y] = (byte) 0;
            }
        }

        for (int i = 0; i < multiplier1; i++) {
            for (int x = 0; x < sizeX; x++) {
                for (int y = 0; y < sizeY; y++) {
                    int n = (r[x - 1 == -1 ? x : x - 1][y] + r[x][y - 1 == -1 ? y : y - 1] + r[x + 1 == sizeX ? x : x + 1][y] + r[x][y + 1 == sizeY ? y : y + 1] + r[x][y]) / 5;

                    r[x][y] = (byte) (n + ExtendedMath.random(-multiplier2, multiplier2));
                }
            }
        }

        for (int i = 0; i < unifier1; i++) {
            for (int x = 0; x < sizeX; x++) {
                for (int y = 0; y < sizeY; y++) {
                    int n = (r[x - 1 == -1 ? x : x - 1][y] + r[x][y - 1 == -1 ? y : y - 1] + r[x + 1 == sizeX ? x : x + 1][y] + r[x][y + 1 == sizeY ? y : y + 1] + r[x][y]) / 5;
                    r[x][y] = (byte) (n / unifier2);
                }
            }
        }
        
        System.gc();


        return r;
    }
}
