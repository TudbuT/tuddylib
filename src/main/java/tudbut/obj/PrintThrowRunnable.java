package tudbut.obj;

public interface PrintThrowRunnable extends Runnable {
    
    default void run() {
        try {
            this.doRun();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    void doRun() throws Throwable;
}
