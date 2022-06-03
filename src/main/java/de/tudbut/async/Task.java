package de.tudbut.async;

/**
 * @author TudbuT
 * @since 03 Jun 2022
 */

public class Task<T> {
    TaskQueue queue;
    final TaskCallable<T> callable;
    final CallbackList<T> resolve = new CallbackList<>();
    final CallbackList<Throwable> reject = new CallbackList<>();
    private boolean done = false;
    private T result = null;
    private Throwable rejection = null;
    boolean isAwaiting = false;
    
    public Task(TaskCallable<T> callable) {
        this.callable = callable;
        resolve.add((t) -> this.result = t);
    }
    public Task(TaskCallable<T> callable, Class<T> clazz) {
        this.callable = callable;
        resolve.add((t) -> this.result = t);
    }
    
    public Task<T> then(Callback<T> resolve) {
        this.resolve.add(resolve);
        return this;
    }
    
    public Task<T> err(Callback<Throwable> reject) {
        this.reject.add(reject);
        return this;
    }
    
    public T await() {
        try {
            if(!done) {
                isAwaiting = true;
                // If it is in the queue already
                if(Thread.currentThread() == queue) {
                    while(!done) {
                        // Work while awaiting
                        queue.processNextHere();
                    }
                }
                else {
                    synchronized (this) {
                        if(!done) {
                            wait();
                        }
                    }
                }
            }
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(rejection != null)
            throw new Reject(rejection);
        return result;
    }
    
    void setDone(Throwable rejection) {
        if(this.rejection == null) {
            this.rejection = rejection;
            if (!done) {
                synchronized (this) {
                    done = true;
                    notifyAll();
                }
            }
        }
    }
    
    public boolean done() {
        return done;
    }
    
    public Task<T> ok() {
        Async.context.get().register(this);
        return this;
    }
    public Task<T> ok(TaskQueue queue) {
        queue.register(this);
        return this;
    }
}
