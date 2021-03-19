package de.tudbut.type;

import de.tudbut.tools.Tools;
import tudbut.obj.Mappable;

import java.util.Map;

public class Vector3d implements Mappable {
    private double x, y, z;

    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vector3d add(Vector3d vector1, Vector3d vector2) {
        return new Vector3d(vector1.getX() + vector2.getX(), vector1.getY() + vector2.getY(), vector1.getZ() + vector2.getZ());
    }

    public static Vector3d subtract(Vector3d vector1, Vector3d vector2) {
        return new Vector3d(vector1.getX() - vector2.getX(), vector1.getY() - vector2.getY(), vector1.getZ() - vector2.getZ());
    }

    public static Vector3d multiply(Vector3d vector1, Vector3d vector2) {
        return new Vector3d(vector1.getX() * vector2.getX(), vector1.getY() * vector2.getY(), vector1.getZ() * vector2.getZ());
    }

    public static Vector3d divide(Vector3d vector1, Vector3d vector2) {
        return new Vector3d(vector1.getX() / vector2.getX(), vector1.getY() / vector2.getY(), vector1.getZ() / vector2.getZ());
    }

    public static double length(Vector3d vector) {
        return Math.sqrt(vector.getX() * vector.getX() + vector.getY() * vector.getY() + vector.getZ() * vector.getZ());
    }

    public static Vector3d normalize(Vector3d vector) {
        double len = Vector3d.length(vector);
        return Vector3d.divide(vector, new Vector3d(len, len, len));
    }

    public static double dot(Vector3d vector1, Vector3d vector2) {
        return vector1.getX() * vector2.getX() + vector1.getY() * vector2.getY() + vector1.getZ() * vector2.getZ();
    }

    public Vector3d set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3d set(Vector3d vector) {
        return set(vector.x, vector.y, vector.z);
    }

    public Vector3d add(Vector3d vector) {
        set(x + vector.x, y + vector.y, z + vector.z);
        return this;
    }
    
    public Vector3d add(double x, double y, double z) {
        set(x + this.x, y + this.y, z + this.z);
        return this;
    }

    @Override
    public int hashCode() {
        final long prime = 31;
        long result = 1;
        result = prime * result + Double.doubleToLongBits(x);
        result = prime * result + Double.doubleToLongBits(y);
        result = prime * result + Double.doubleToLongBits(z);
        return (int) result / 4;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vector3d other = (Vector3d) obj;
        if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
            return false;
        if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
            return false;
        return Double.doubleToLongBits(z) == Double.doubleToLongBits(other.z);
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

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public Vector3d multiply(double i) {
        set(x * i, y * i, z * i);
        return this;
    }

    public Vector3d multiply(double x, double y, double z) {
        set(this.x * x, this.y * y, this.z * z);
        return this;
    }
    
    public Vector3d multiply(Vector3d vec) {
        set(this.x * vec.x, this.y * vec.y, this.z * vec.z);
        return this;
    }

    public Vector3d negate() {
        set(-x, -y, -z);
        return this;
    }
    
    public boolean isGreaterThan(Vector3d vec) {
        return
                x > vec.x &&
                y > vec.y &&
                z > vec.z;
    }
    
    public boolean isSmallerThan(Vector3d vec) {
        return
                x < vec.x &&
                y < vec.y &&
                z < vec.z;
    }
    
    public boolean isGreaterOrEqualThan(Vector3d vec) {
        return
                x >= vec.x &&
                y >= vec.y &&
                z >= vec.z;
    }
    
    public boolean isSmallerOrEqualThan(Vector3d vec) {
        return
                x <= vec.x &&
                y <= vec.y &&
                z <= vec.z;
    }

    @Override
    public Vector3d clone() {
        return new Vector3d(x, y, z);
    }
    
    public String toString() {
        return "x:" + x + ";y:" + y + ";z:" + z;
    }
    
    @Override
    public Map<String, String> map() {
        return Tools.stringToMap("x:" + x + ";y:" + y + ";z:" + z);
    }

    public static Vector3d fromMap(Map<String, String> map) {
        return new Vector3d(Double.parseDouble(map.get("x")), Double.parseDouble(map.get("y")), Double.parseDouble(map.get("z")));
    }
}
