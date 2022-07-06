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
    Task<?> parent = null;
    
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
        if(this.resolve.done()) {
            resolve.call(result);
        }
        return this;
    }
    
    public <R> Task<R> compose(ComposeCallback<T, R> callback) {
        return new Task<R>((res, rej) -> {
            callback.call(result, res, rej);
        }).appendTo(this);
    }
    
    private Task<T> appendTo(Task<?> task) {
        this.parent = task;
        task.then(done -> {
            task.queue.register(this);
        });
        return this;
    }
    
    public Task<T> err(Callback<Throwable> reject) {
        if(parent != null)
            parent.err(reject);
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
        if(rejection != null && !reject.exists())
            throw new Reject(rejection);
        return result;
    }
    
    void setDone(Throwable rejection) {
        if(this.rejection == null) {
            this.rejection = rejection;
        }
        if (!done) {
            synchronized (this) {
                done = true;
                notifyAll();
            }
        }
    }
    
    public boolean done() {
        return done;
    }
    
    public Task<T> ok() {
        return ok(Async.context.get());
    }
    public Task<T> ok(TaskQueue queue) {
        if(this.queue != null)
            return this;
        this.queue = queue;
        if (parent != null) {
            parent.ok(queue);
            return this;
        }
        queue.register(this);
        return this;
    }
}
