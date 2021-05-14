package tudbut.tools;

public class FloatInterpolator {
    
    float diff, lower, upper;
    
    public void setBounds(float lower, float upper) {
        if(lower > upper)
            throw new IllegalArgumentException("Lower bound > Upper bound");
        
        this.lower = lower;
        this.upper = upper;
        this.diff = upper - lower;
    }
    
    public float get(float f) {
        f = f - lower;
        return f / diff;
    }
}
