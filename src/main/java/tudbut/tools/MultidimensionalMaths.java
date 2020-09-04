package tudbut.tools;

public class MultidimensionalMaths {
    public static int getLocationIn2DArray(int x, int y, int sizeY) {
        return (
                x +
                y * sizeY
        );
    }

    public static int getLocationIn3DArray(int x, int y, int z, int sizeY, int sizeZ) {
        return (
                x +
                y * sizeY +
                z * sizeY * sizeZ
        );
    }

    public static int getLocationIn4DArray(int x, int y, int z, int t, int sizeY, int sizeZ, int sizeT) {
        return (
                x +
                y * sizeY +
                z * sizeY * sizeZ +
                t * sizeY * sizeZ * sizeT
        );
    }

    public static int getLocationIn5DArray(int x, int y, int z, int t, int a, int sizeY, int sizeZ, int sizeT, int sizeA) {
        return (
                x +
                y * sizeY +
                z * sizeY * sizeZ +
                t * sizeY * sizeZ * sizeT +
                a * sizeY * sizeZ * sizeT * sizeA
        );
    }

}
