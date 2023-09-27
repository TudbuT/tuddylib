package de.tudbut.math;

public interface ComplexFunction {
    
    ComplexNumber solve(ComplexNumber n);
    
    default ComplexNumber solve(double n) {
        return solve(ComplexNumber.create(n, 0));
    }
}
