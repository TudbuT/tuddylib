package tudbut.obj;

import de.tudbut.type.Vector3d;
import tudbut.rendering.Maths3D;
import tudbut.rendering.Rectangle3D;

public class RayCast {
    
    private final Vector3d step = new Vector3d(0, 0, -1);
    private final Vector3d origin;
    
    public RayCast(Vector3d origin, Vector3d step) {
        this.origin = origin;
        this.step.set(step);
    }
    
    public boolean hits(Rectangle3D rectangle, int maxSteps) {
        Vector3d pos = origin.clone();
        for (int i = 0; i < maxSteps; i++) {
            if(intersects(
                    rectangle,
                    new Rectangle3D(
                            origin,
                            pos.clone().negate().add(origin).negate()
                    )
            )) {
                return true;
            }
            pos.add(step);
        }
        return
                intersects(
                        rectangle,
                        new Rectangle3D(
                                origin,
                                pos.clone().negate().add(origin).negate()
                        )
                );
    }
    
    private boolean intersects(Rectangle3D rectangle, Rectangle3D castRectangle) {
        rectangle.sort();
        castRectangle.sort();
        boolean[] relation = Maths3D.getRelation(rectangle, castRectangle);
        return
                !relation[0] &&
                !relation[1] &&
                !relation[2] &&
                !relation[3] &&
                !relation[4] &&
                !relation[5]  ;
    }
}
