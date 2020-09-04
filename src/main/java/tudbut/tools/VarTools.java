package tudbut.tools;

import de.tudbut.timer.AsyncRunnable;

public class VarTools {

    public static <T,O> O objectOrNull(T t, ArrayTools.Getter<T, O> getter) {
        try {
            return getter.get(t);
        } catch (Throwable throwable) {
            return null;
        }
    }
    
    public static <O> O objectOrNull(ArrayTools.Getter<Object, O> getter) {
        try {
            return getter.get(null);
        } catch (Throwable throwable) {
            return null;
        }
    }
    
    public static <O> O objectOrNull(AsyncRunnable<O> getter) {
        try {
            return getter.run();
        } catch (Throwable throwable) {
            return null;
        }
    }
}
