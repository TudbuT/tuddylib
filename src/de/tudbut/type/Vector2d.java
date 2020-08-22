package de.tudbut.type;

public class Vector2d {
    private double x, y;

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void set(Vector2d vec) {
        this.x = vec.x;
        this.y = vec.y;
    }

    public Vector2d add(Vector2d vec) {
        set(x+vec.x, y+vec.y);
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

    public Vector2d negate() {
        set(-x, -y);
        return this;
    }
}
