package tudbut.tools;

public class CalibratingInterpolator extends FloatInterpolator {
    
    private float sensitivity = 1;
    
    @Override
    public float get(float f) {
        if(f < upper && f > lower)
            setBounds(
                    ((lower * sensitivity) + f) / (sensitivity + 1),
                    ((upper * sensitivity) + f) / (sensitivity + 1)
            );
        else {
            setBounds(Math.min(lower, f), Math.max(upper, f));
        }
        return super.get(f);
    }
    
    public float getSensitivity() {
        return 1 / sensitivity;
    }
    
    public void setSensitivity(float sensitivity) {
        this.sensitivity = 1 / sensitivity;
    }
}
