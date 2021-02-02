package tudbut.rendering;

import de.tudbut.type.Vector3d;
import tudbut.obj.RelativeVector3d;

public class Rectangle3D {
    Vector3d pos;
    Vector3d size;
    
    public Rectangle3D(Vector3d pos, Vector3d size) {
        construct(pos, size);
    }
    
    private void construct(Vector3d pos, Vector3d size) {
        this.pos = pos.clone();
        this.size = size.clone();
    }
    
    public void sort() {
        if(size.getX() <= 0) {
           pos.setX(pos.getX() - size.getX());
           size.setX(-size.getX());
        }
        if(size.getY() <= 0) {
            pos.setY(pos.getY() - size.getY());
            size.setY(-size.getY());
        }
        if(size.getZ() <= 0) {
            pos.setZ(pos.getZ() - size.getZ());
            size.setZ(-size.getZ());
        }
    }
    
    public Vector3d getPos() {
        return pos;
    }
    
    public Vector3d getSize() {
        return size;
    }
    
    public Vector3d getEndPoint() {
        return pos.clone().add(size);
    }
}
