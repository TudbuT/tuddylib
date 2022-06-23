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

    public static <T> Task<T> t(TaskCallable<T> callable) {
        return new Task<>(callable);
    }
    
    /**
     * This can be problematic if errors might happen in the callable. use t() instead.
     */
    public static <T> Task<T> s(TaskCallable<T> callable) {
        return new Task<>(callable).ok();
    }
    
    public static Task<Void> loop(TaskCallable<Boolean> condition, TaskCallable<Void> body) {
        return new Task<>((res, rej) -> {
            while(new Task<>(condition).err(rej).ok().await()) {
                new Task<>(body).err(rej).ok().await();
                ((TaskQueue)Thread.currentThread()).processNextHere();
            }
            res.call(null);
        });
    }

    public static boolean unblockQueue() {
        if(Thread.currentThread() instanceof TaskQueue) {
            TaskQueue q = (TaskQueue)Thread.currentThread();
            if(q.queue.hasNext()) {
                while(q.queue.hasNext()) {
                    q.processNextHere();
                }
                return true;
            }
        }
        return false;
    }
}
