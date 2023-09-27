package de.tudbut.rendering.tph;

import de.tudbut.type.FInfo;
import de.tudbut.type.Vector2d;
import de.tudbut.type.Vector3d;
import de.tudbut.io.DoubleBuffer;

import static de.tudbut.tools.BufferManager.createResource;
import static de.tudbut.tools.BufferManager.getBufferFromID;
import static de.tudbut.rendering.tph.TPH300.*;

@FInfo(s = "TuddyProjection Helper 3D 01: Cameras")
public class TPH301 {
    public static final int ID = 301;

    public static long createCamera(Vector3d position, int rotX, int rotY) {
        DoubleBuffer res = DoubleBuffer.create(6 * 8);
        double[] doubles = (double[]) res.get();
        doubles[0] = position.getX();
        doubles[1] = position.getY();
        doubles[2] = position.getZ();
        doubles[3] = rotX;
        doubles[4] = rotY;
        return createResource(res);
    }

    public static long createCamera(Vector3d position, int rotX, int rotY, int rotZ) {
        DoubleBuffer res = DoubleBuffer.create(6 * 8);
        double[] doubles = (double[]) res.get();
        doubles[0] = position.getX();
        doubles[1] = position.getY();
        doubles[2] = position.getZ();
        doubles[3] = rotX;
        doubles[4] = rotY;
        doubles[5] = rotZ;
        return createResource(res);
    }

    public static long createCamera(Vector3d position, Vector2d rot) {
        DoubleBuffer res = DoubleBuffer.create(6 * 8);
        double[] doubles = (double[]) res.get();
        doubles[0] = position.getX();
        doubles[1] = position.getY();
        doubles[2] = position.getZ();
        doubles[3] = rot.getX();
        doubles[4] = rot.getY();
        return createResource(res);
    }

    public static long createCamera(Vector3d position, Vector3d rot) {
        DoubleBuffer res = DoubleBuffer.create(6 * 8);
        double[] doubles = (double[]) res.get();
        doubles[0] = position.getX();
        doubles[1] = position.getY();
        doubles[2] = position.getZ();
        doubles[3] = rot.getX();
        doubles[4] = rot.getY();
        doubles[5] = rot.getZ();
        return createResource(res);
    }

    public static void setCameraPosition(Vector3d pos, long cam) {
        double[] d = (double[]) getBufferFromID(cam).get();
        d[0] = pos.getX();
        d[1] = pos.getY();
        d[2] = pos.getZ();
    }

    public static void setCameraRotation(Vector2d rot, long cam) {
        double[] d = (double[]) getBufferFromID(cam).get();
        d[3] = rot.getX();
        d[4] = rot.getY();
    }

    public static void setCameraRotation(Vector3d rot, long cam) {
        double[] d = (double[]) getBufferFromID(cam).get();
        d[3] = rot.getX();
        d[4] = rot.getY();
        d[5] = rot.getZ();
    }

    public static Vector3d translate(Vector3d vec, long camera) {
        DoubleBuffer cam = (DoubleBuffer) getBufferFromID(camera);
        double[] d = (double[]) cam.get();
        Vector3d pos = new Vector3d(-d[0], d[1], d[2]);
        double rotX = d[3];
        double rotY = d[4];
        double rotZ = d[5];

        applyRotation(vec, pos.clone().negate(), Y, -rotX);
        applyRotation(vec, pos.clone().negate(), X, rotY);
        applyRotation(vec, pos.clone().negate(), Z, rotZ);
        vec.add(pos);
        return vec;
    }
}
