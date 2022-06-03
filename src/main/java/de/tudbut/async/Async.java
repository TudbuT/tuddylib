package de.tudbut.async;

/**
 * @author TudbuT
 * @since 03 Jun 2022
 * Include this with import static.
 */
public class Async {
    
    static final ThreadLocal<TaskQueue> context = ThreadLocal.withInitial(() -> TaskQueue.main);
    
    public static <T> T await(Task<T> task) {
        return task.await();
    }
    public static void context(TaskQueue queue) {
        context.set(queue);
    }
}
