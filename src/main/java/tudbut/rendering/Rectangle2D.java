package tudbut.rendering;

import de.tudbut.type.Vector2d;
import tudbut.obj.RelativeVector2d;

public class Rectangle2D {
    Vector2d pos;
    RelativeVector2d end;
    
    public Rectangle2D(Vector2d pos, Vector2d size) {
        construct(pos, new RelativeVector2d(pos, size));
    }
    
    private void construct(Vector2d pos, RelativeVector2d size) {
        this.pos = pos.clone();
        this.end = size.clone();
    }
    
    public Vector2d getPos() {
        return pos.clone();
    }
    
    public Vector2d getSize() {
        return end.getRelativePos().clone();
    }
    
    public RelativeVector2d getEndPoint() {
        return end.clone();
    }
}
