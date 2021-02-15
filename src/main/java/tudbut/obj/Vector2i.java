package tudbut.obj;

import de.tudbut.tools.Tools;
import de.tudbut.type.Vector2d;

import java.util.Map;

public class Vector2i implements Mappable {
    private int x, y;

    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(Vector2i vec) {
        this.x = vec.x;
        this.y = vec.y;
    }

    public Vector2i add(Vector2i vec) {
        set(x+vec.x, y+vec.y);
        return this;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public Vector2i clone() {
        return new Vector2i(x, y);
    }
    
    public Vector2i multiply(double m) {
        set((int) (x * m), (int) (y * m));
        return this;
    }
    
    public Vector2i multiply(double mx, double my) {
        set((int) (x * mx), (int) (y * my));
        return this;
    }
    
    public Vector2i multiply(Vector2d vec) {
        set((int) (x * vec.getX()), (int) (y * vec.getY()));
        return this;
    }
    
    
    public Vector2i negate() {
        set(-x, -y);
        return this;
    }
    
    public String toString() {
        return "x:" + x + ";y:" + y;
    }
    
    @Override
    public Map<String, String> map() {
        return Tools.stringToMap("x:" + x + ";y:" + y);
    }
    
    public static Vector2i fromMap(Map<String, String> map) {
        return
                new Vector2i(
                        Integer.parseInt(map.get("x")),
                        Integer.parseInt(map.get("y"))
                )
        ;
    }
}
