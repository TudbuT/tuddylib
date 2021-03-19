package tudbut.obj;

import de.tudbut.type.Vector2d;

public class RelativeVector2d extends Vector2d {
    private final Vector2d relativeTo = new Vector2d(0,0);
    
    public RelativeVector2d(Vector2d relativeTo, double x, double y) {
        super(relativeTo.getX() + x, relativeTo.getY() + y);
        this.relativeTo.set(relativeTo);
    }
    
    public RelativeVector2d(Vector2d relativeTo, Vector2d relative) {
        super(relativeTo.getX() + relative.getX(), relativeTo.getY() + relative.getY());
        this.relativeTo.set(relativeTo);
    }
    
    @Override
    public Vector2d set(double x, double y) {
        Vector2d rpos = getRelativePos();
        relativeTo.set(x - rpos.getX(), y - rpos.getY());
        return super.set(x, y);
    }
    
    public Vector2d setSize(double x, double y) {
        return super.set(relativeTo.getX() + x, relativeTo.getY() + y);
    }
    
    public Vector2d getRelativeTo() {
        return relativeTo.clone();
    }
    
    public Vector2d getRelativePos() {
        return new Vector2d(getX() - relativeTo.getX(), getY() - relativeTo.getY());
    }
    
    public double getRX() {
        return getRelativePos().getX();
    }
    
    public double getRY() {
        return getRelativePos().getY();
    }
    
    public RelativeVector2d clone() {
        return new RelativeVector2d(relativeTo, getRelativePos());
    }
}
