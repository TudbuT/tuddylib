package tudbut.rendering;

import de.tudbut.type.Vector2d;
import de.tudbut.type.Vector3d;
import tudbut.obj.RelativeVector2d;

public class Rectangle2D {
    Vector2d pos;
    RelativeVector2d size;
    
    public Rectangle2D(Vector2d pos, Vector2d size) {
        if(size.getClass() == RelativeVector2d.class) {
            construct(pos, (RelativeVector2d) size);
        }
        else {
            construct(pos, new RelativeVector2d(pos, size));
        }
    }
    
    public Rectangle2D(Vector2d pos, RelativeVector2d size) {
        construct(pos, size);
    }
    
    private void construct(Vector2d pos, RelativeVector2d size) {
        this.pos = pos.clone();
        this.size = size.clone();
    }
    
    public Vector2d getPos() {
        return pos;
    }
    
    public Vector2d getSize() {
        return size.getRelativePos();
    }
    
    public Vector2d getEndPoint() {
        return size;
    }
}
