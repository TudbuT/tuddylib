package tudbut.tools;

import java.util.Random;

public class NoiseGenerator {
    public static float[][][] generateRandom(int sizeX, int sizeY, int sizeZ, int smoothness, float scale, Random random) {
        float[][][] map = new float[sizeX][sizeY][sizeZ];
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                for (int z = 0; z < sizeZ; z++) {
                    map[x][y][z] = random.nextFloat();
                }
            }
        }
        smooth(map, 0, 0, 0, sizeX, sizeY, sizeZ, smoothness);
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                for (int z = 0; z < sizeZ; z++) {
                    map[x][y][z] = map[x][y][z] * scale;
                }
            }
        }
    
        System.gc();
        return map;
    }
    
    public static void smooth(float[][][] floats, int sx, int sy, int sz, int ex, int ey, int ez, int amount) {
        smooth(floats, sx, sy, sz, ex, ey, ez, amount, 2);
    }
    
    public static void smooth(float[][][] floats, int sx, int sy, int sz, int ex, int ey, int ez, int amount, float m) {
        for (int s = 0; s < amount; s++) {
            for (int x = sx; x < ex; x++) {
                for (int y = sy; y < ey; y++) {
                    for (int z = sz; z < ez; z++) {
                        float i = m;
                        float f = floats[x][y][z] * m;
                        for (int x1 = -1; x1 <= 1; x1++) {
                            for (int y1 = -1; y1 <= 1; y1++) {
                                for (int z1 = -1; z1 <= 1; z1++) {
                                    if(x + x1 >= 0 && y + y1 >= 0 && z + z1 >= 0) {
                                        if(x + x1 < floats.length && y + y1 < floats[0].length && z + z1 < floats[0][0].length) {
                                            f += floats[x + x1][y + y1][z + z1];
                                            i++;
                                        }
                                    }
                                }
                            }
                        }
                        floats[x][y][z] = f / i;
                    }
                }
            }
        }
    }
}
