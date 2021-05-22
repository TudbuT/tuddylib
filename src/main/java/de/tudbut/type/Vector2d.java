package de.tudbut.type;

import de.tudbut.tools.Tools;
import tudbut.obj.Mappable;

import java.util.Map;

public class Vector2d implements Mappable {
    private double x, y;

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2d set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2d set(Vector2d vec) {
        return set(vec.x, vec.y);
    }

    public Vector2d add(Vector2d vec) {
        set(x + vec.x, y + vec.y);
        return this;
    }
    
    public Vector2d add(double x, double y) {
        set(x + this.x, y + this.y);
        return this;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public Vector2d clone() {
        return new Vector2d(x, y);
    }
    
    public Vector2d multiply(double i) {
        set(x * i, y * i);
        return this;
    }
    public Vector2d multiply(double mx, double my) {
        set(x * mx, y * my);
        return this;
    }
    public Vector2d multiply(Vector2d vec) {
        set(x * vec.x, y * vec.y);
        return this;
    }

    public Vector2d negate() {
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
    
    public static Vector2d fromMap(Map<String, String> map) {
        return
                new Vector2d(
                        Double.parseDouble(map.get("x")),
                        Double.parseDouble(map.get("y"))
                )
        ;
    }
}
