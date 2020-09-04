package tudbut.obj;

public interface IgnoreThrowRunnable extends Runnable {
    
    default void run() {
        try {
            this.doRun();
        } catch (Throwable ignore) { }
    }
    
    void doRun() throws Throwable;
}
