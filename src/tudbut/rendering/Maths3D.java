package tudbut.rendering;

import de.tudbut.tools.ExtendedMath;
import de.tudbut.type.Vector3d;

public class Maths3D {
    public static void prepareVectorsForRectangle(RenderObject3D o, int renderOutputX, int renderOutputY, double var0, double fovMod) {
        Vector3d pos1 = o.vectors[0];
        Vector3d pos2 = o.vectors[1];
        Vector3d pos3 = o.vectors[2];
        Vector3d pos4 = o.vectors[3];

        if ((1 / fovMod) / (ExtendedMath.min(pos1.getZ(), pos2.getZ(), pos3.getZ(), pos4.getZ()) / 15) <= 0)
            o.isClipped = true;

        includeZDirection(pos1, fovMod);
        includeZDirection(pos2, fovMod);
        includeZDirection(pos3, fovMod);
        includeZDirection(pos4, fovMod);

        for (int i = 0; i < o.vectors.length; i++) {
            Vector3d vec = o.vectors[i];
            vec.multiply(var0);
            vec.set(vec.getX() + (double) renderOutputX / 2, vec.getY() + (double) renderOutputY / 2, vec.getZ());
        }
    }

    public static void prepareVectorsForTriangle(RenderObject3D o, int renderOutputX, int renderOutputY, double var0, double fovMod) {
        Vector3d pos1 = o.vectors[0];
        Vector3d pos2 = o.vectors[1];
        Vector3d pos3 = o.vectors[2];

        if ((1 / fovMod) / (ExtendedMath.min(pos1.getZ(), pos2.getZ(), pos3.getZ()) / 15) <= 0)
            o.isClipped = true;

        includeZDirection(pos1, fovMod);
        includeZDirection(pos2, fovMod);
        includeZDirection(pos3, fovMod);

        for (int i = 0; i < o.vectors.length; i++) {
            Vector3d vec = o.vectors[i];
            vec.multiply(var0);
            vec.set(vec.getX() + (double) renderOutputX / 2, vec.getY() + (double) renderOutputY / 2, vec.getZ());
        }
    }

    public static void includeZDirection(Vector3d vec, double fovMod) {
        vec.multiply((1 / fovMod) / (vec.getZ() / 15));
    }

    public static int[][] getFillingCoordinatesForRectangle(RenderObject3D o) {
        int[][] r = new int[2][4];

        r[0][0] = (int) o.vectors[0].getX();
        r[0][1] = (int) o.vectors[1].getX();
        r[0][2] = (int) o.vectors[2].getX();
        r[0][3] = (int) o.vectors[3].getX();

        r[1][0] = (int) o.vectors[0].getY();
        r[1][1] = (int) o.vectors[1].getY();
        r[1][2] = (int) o.vectors[2].getY();
        r[1][3] = (int) o.vectors[3].getY();

        return r;
    }

    public static int[][] getFillingCoordinatesForTriangle(RenderObject3D o) {
        int[][] r = new int[2][3];

        r[0][0] = (int) o.vectors[0].getX();
        r[0][1] = (int) o.vectors[1].getX();
        r[0][2] = (int) o.vectors[2].getX();

        r[1][0] = (int) o.vectors[0].getY();
        r[1][1] = (int) o.vectors[1].getY();
        r[1][2] = (int) o.vectors[2].getY();

        return r;
    }


    /*/**
     * @param direction
     *      -2: X-
     *      -1: Y-
     *      1:  X+
     *      2:  Y+
     * /
    static void rotateObjects(int direction, int amount, RenderObject3D... objects) {
        double xAvrg = 0d;
        double yAvrg = 0d;
        double zAvrg = 0d;

        for (int i = 0; i < objects.length; i++) {
            RenderObject3D object = objects[i];

            xAvrg += object.vectors[0].getX();
            xAvrg += object.vectors[1].getX();
            xAvrg += object.vectors[2].getX();
            if(object.vectors.length == 4)
                xAvrg += object.vectors[3].getX();

            yAvrg += object.vectors[0].getY();
            yAvrg += object.vectors[1].getY();
            yAvrg += object.vectors[2].getY();
            if(object.vectors.length == 4)
                yAvrg += object.vectors[3].getY();

            zAvrg += object.vectors[0].getZ();
            zAvrg += object.vectors[1].getZ();
            zAvrg += object.vectors[2].getZ();
            if(object.vectors.length == 4)
                zAvrg += object.vectors[3].getZ();
        }

        xAvrg = xAvrg / objects.length;
        yAvrg = yAvrg / objects.length;
        zAvrg = zAvrg / objects.length;


        rotateObjectsAroundPoint(direction, amount, xAvrg, yAvrg, zAvrg, objects);
    }

    /**
     * @param direction
     *      -2: X-
     *      -1: Y-
     *      1:  X+
     *      2:  Y+
     * /
    static void rotateObjectsAroundPoint(int direction, int amount, double xAvrg, double yAvrg, double zAvrg, RenderObject3D... objects) {
        if(direction == 1) {
            amount
        }
    }*/
}
