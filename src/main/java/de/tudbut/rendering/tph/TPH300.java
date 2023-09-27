package de.tudbut.rendering.tph;

import de.tudbut.type.FInfo;
import de.tudbut.type.Vector3d;

@FInfo(s = "TuddyProjection Helper 3D 00: Rotation")
public class TPH300 {
    public static final int ID = 302;
    public static final int X = ID + 1;
    public static final int Y = ID + 2;
    public static final int Z = ID + 3;

    public static Vector3d applyRotation(Vector3d vector, Vector3d rotPoint, int type, double angle) {
        angle = (angle / 360) * Math.PI * 2;
        type = type - ID;
        switch (type) {
            case 1:
                Vector3d p1 = vector.clone();
                Vector3d c1 = rotPoint.clone();
                c1.negate();
                p1.add(c1);
                p1.set(
                        p1.getX(),
                        p1.getY() * Math.cos(angle) + p1.getZ() * -Math.sin(angle),
                        p1.getY() * Math.sin(angle) + p1.getZ() * Math.cos(angle)
                );
                c1.negate();
                p1.add(c1);
                vector.set(p1);
                return vector;
            case 2:
                Vector3d p2 = vector.clone();
                Vector3d c2 = rotPoint.clone();
                c2.negate();
                p2.add(c2);
                p2.set(
                        p2.getX() * Math.cos(angle) + p2.getZ() * Math.sin(angle),
                        p2.getY(),
                        p2.getX() * -Math.sin(angle) + p2.getZ() * Math.cos(angle)
                );
                c2.negate();
                p2.add(c2);
                vector.set(p2);
                return vector;
            case 3:
                Vector3d p3 = vector.clone();
                Vector3d c3 = rotPoint.clone();
                c3.negate();
                p3.add(c3);
                p3.set(
                        p3.getX() * Math.cos(angle) + p3.getY() * -Math.sin(angle),
                        p3.getX() * Math.sin(angle) + p3.getY() * Math.cos(angle),
                        p3.getZ()
                );
                c3.negate();
                p3.add(c3);
                vector.set(p3);
                return vector;

        }
        return null;
    }
}
