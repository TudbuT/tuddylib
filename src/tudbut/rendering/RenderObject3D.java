package tudbut.rendering;

import de.tudbut.tools.Tools;
import de.tudbut.type.Vector3d;

public class RenderObject3D {
    final double multiplier;
    Vector3d[] vectors;
    RenderObjectType type;
    int color;
    boolean isClipped;

    public RenderObject3D(Vector3d pos1, Vector3d pos2, Vector3d pos3, boolean fill, double multiplier, int color) {
        type = fill ? RenderObjectType.FULL_TRIANGLE : RenderObjectType.TRIANGLE;
        vectors = new Vector3d[]{pos1, pos2, pos3};
        this.multiplier = multiplier;
        this.color = color;

        updateMultiplier();
    }

    public RenderObject3D(Vector3d pos1, Vector3d pos2, Vector3d pos3, Vector3d pos4, boolean fill, double multiplier, int color) {
        type = fill ? RenderObjectType.FULL_RECTANGLE : RenderObjectType.RECTANGLE;
        vectors = new Vector3d[]{pos1, pos2, pos3, pos4};
        this.multiplier = multiplier;
        this.color = color;

        updateMultiplier();
    }

    private RenderObject3D(Vector3d[] vectors, RenderObjectType type, double multiplier, int color) {
        this.vectors = vectors;
        this.type = type;
        this.multiplier = multiplier;
        this.color = color;
    }

    void updateMultiplier() {
        /*for (Vector3d vec : vectors) {
            vec.set(vec.getX() * multiplier, vec.getY() * multiplier, vec.getZ());
        }*/

    }

    @Override
    public RenderObject3D clone() {
        Vector3d[] lvectors = new Vector3d[vectors.length];
        Tools.copyArray(vectors, lvectors, vectors.length);

        for (int i = 0; i < lvectors.length; i++) {
            lvectors[i] = lvectors[i].clone();
        }

        return new RenderObject3D(lvectors, type, multiplier, color);
    }
}
