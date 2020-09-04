package tudbut.obj;

import java.util.concurrent.ExecutorService;

public class ClosedClosableException extends Exception {

    public ClosedClosableException() { }
    public ClosedClosableException(String message) { super(message); }
}
