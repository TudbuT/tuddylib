package de.tudbut.rendering;

import de.tudbut.tools.ExtendedMath;
import de.tudbut.type.Vector3d;

public class Maths3D {
    public static boolean[] getRelation(Rectangle3D measuringObject, Rectangle3D toMeasure) {
        boolean[] b = new boolean[6];
        Vector3d a0 = toMeasure.getPos();
        Vector3d a1 = toMeasure.getEndPoint();
        Vector3d b0 = measuringObject.getPos();
        Vector3d b1 = measuringObject.getEndPoint();
        
        b[0] = a0.getX() < b1.getX();
        b[1] = a0.getY() < b1.getY();
        b[2] = a0.getZ() < b1.getZ();
        b[3] = a1.getX() > b0.getX();
        b[4] = a1.getY() > b0.getY();
        b[5] = a1.getZ() > b0.getZ();
    
        for (int i = 0; i < b.length; i++) {
            b[i] = !b[i];
        }
        
        return b;
    }
    
    public static boolean collides(Rectangle3D rectangle0, Rectangle3D rectangle1) {
        boolean b = true;
        boolean[] rel = getRelation(rectangle0, rectangle1);
        for (int i = 0; i < rel.length; i++) {
            if (rel[i]) {
                b = false;
                break;
            }
        }
        return b;
    }
    
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
}
