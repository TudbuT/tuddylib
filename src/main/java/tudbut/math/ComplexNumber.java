package tudbut.math;

import de.tudbut.type.Vector2d;

public class ComplexNumber {
    
    private double real = 0;
    private double imaginary = 0;
    private Vector2d vec;
    public boolean autoRecalculate = true;
    
    public double getReal() {
        return real;
    }
    
    public double getImaginary() {
        return imaginary;
    }
    
    public Vector2d getVec() {
        return vec;
    }
    
    public void setReal(double real) {
        this.real = real;
        vec = null;
        if(autoRecalculate)
            recalculateVector();
    }
    
    public void setImaginary(double imaginary) {
        this.imaginary = imaginary;
        vec = null;
        if(autoRecalculate)
            recalculateVector();
    }
    
    private ComplexNumber() {}
    
    public static ComplexNumber create(double real, double imaginary) {
        ComplexNumber n = new ComplexNumber();
        n.real = real;
        n.imaginary = imaginary;
        return n;
    }
    
    public Vector2d recalculateVector() {
        vec = new Vector2d(real, imaginary);
        return vec;
    }
    
    public Vector2d getVector() {
        return vec == null ? recalculateVector() : vec;
    }
}
