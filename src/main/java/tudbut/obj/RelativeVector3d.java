package tudbut.obj;

import de.tudbut.type.Vector3d;

public class RelativeVector3d extends Vector3d {
    private final Vector3d relativeTo = new Vector3d(0,0, 0);
    
    public RelativeVector3d(Vector3d relativeTo, double x, double y, double z) {
        super(relativeTo.getX() + x, relativeTo.getY() + y, relativeTo.getZ() + z);
        this.relativeTo.set(relativeTo);
    }
    
    public RelativeVector3d(Vector3d relativeTo, Vector3d relative) {
        super(relativeTo.getX() + relative.getX(), relativeTo.getY() + relative.getY(), relativeTo.getZ() + relative.getZ());
        this.relativeTo.set(relativeTo);
    }
    
    @Override
    public Vector3d set(double x, double y, double z) {
        Vector3d rpos = getRelativePos();
        relativeTo.set(x - rpos.getX(), y - rpos.getY(), z - rpos.getZ());
        return super.set(x, y, z);
    }
    
    public Vector3d setSize(double x, double y, double z) {
        super.set(relativeTo.getX() + x, relativeTo.getY() + y, relativeTo.getZ());
        return this;
    }
    
    public Vector3d getRelativeTo() {
        return relativeTo.clone();
    }
    
    public Vector3d getRelativePos() {
        return new Vector3d(getX() - relativeTo.getX(), getY() - relativeTo.getY(), getZ() - relativeTo.getZ());
    }
    
    public double getRX() {
        return getRelativePos().getX();
    }
    
    public double getRY() {
        return getRelativePos().getY();
    }
    
    public double getRZ() {
        return getRelativePos().getZ();
    }
    
    public RelativeVector3d clone() {
        return new RelativeVector3d(relativeTo, getRelativePos());
    }
}
