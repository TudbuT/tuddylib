package tudbut.math;

public interface Function {
    
    double solve(double n);
    
    default double solve(ComplexNumber n) {
        return solve(n.getReal());
    }
}
