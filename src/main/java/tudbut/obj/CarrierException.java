package tudbut.obj;

public class CarrierException extends RuntimeException {

    public final Object carried;

    public CarrierException(Exception e, Object o) {
        super(e);
        carried = o;
    }
}
