package de.tudbut.async;

import java.util.concurrent.atomic.AtomicBoolean;

import tudbut.global.DebugStateManager;
import tudbut.tools.Queue;

/**
 * @author TudbuT
 * @since 03 Jun 2022
 */

public class TaskQueue extends Thread {
    
    public static final TaskQueue main = new TaskQueue();
    
    private boolean stop = false;
    final Queue<Task<?>> queue = new Queue<>();
    public final CallbackList<Throwable> rejectionHandlers = new CallbackList<>();
    private AtomicBoolean waiting = new AtomicBoolean();
    private boolean running = false;
    private boolean queueEmpty = true;
    
    public TaskQueue() {
        this.start();
        while (!waiting.get());
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {}
    }
    
    public Queue<Task<?>> stopProcessing() throws IllegalAccessException {
        if(this == main)
            throw new IllegalAccessException("Can't stop main queue!");
        if(stop)
            throw new IllegalStateException("Already stopped!");
        stop = true;
        synchronized (this) {
            this.notifyAll();
        }
        return queue;
    }
    
    public void finish() {
        if(stop)
            throw new IllegalStateException("Already stopped!");
        synchronized (this) {
            this.notifyAll();
        }
        while (queue.hasNext() || running || !queueEmpty) {
            synchronized (queue) {
                try {
                    queue.wait();
                }
                catch (InterruptedException ignored) {
                }
            }
        }
        stop = true;
    }
    
    <T> Task<T> register(Task<T> task) {
        task.queue = this;
        queue.add(task);
        synchronized (this) {
            this.notifyAll();
        }
        return task;
    }
    
    /**
     *
     * @param task The task to make
     * @param clazz The clazz of the return value. This is only used to determine T.
     * @return The task that was created
     */
    <T> Task<T> register(TaskCallable<T> task, Class<T> clazz) {
        return register(new Task<>(task));
    }
    
    @Override
    public void run() {
        if(DebugStateManager.isDebugEnabled())
            System.out.println("[TaskQueue] Thread started.");
        while (!stop) {
            try {
                synchronized (this) {
                    waiting.set(true);
                    wait();
                }
            }
            catch (InterruptedException e) {
                return;
            }
            finally {
                waiting.set(false);
            }
            if(stop)
                return;
    
            process();
        }
    }
    
    public void process() {
        while(queue.hasNext()) {
            queueEmpty = false;
            try {
                processNextHere();
            }
            catch (Throwable e) {
                if(!rejectionHandlers.exists()) {
                    System.err.println("!! Unhandled Task rejection:");
                    e.printStackTrace();
                    continue;
                }
                rejectionHandlers.call(e);
            }
        }
        queueEmpty = true;
        synchronized (queue) {
            queue.notifyAll();
        }
    }
    
    public <T> void processNextHere() {
        try {
            if(!queue.hasNext())
                return;
            
            running = true;
            Task<T> task = (Task<T>) queue.next();
            
            // Execute the task. If it rejects using reject(), throw a reject to be handled by the exception block.
            // If it resolves using throwResolve, redirect that to task.resolve.
            // If it throws something, redirect that to task.reject if possible, otherwise throw a Reject of it to be handled.
            try {
                try {
                    task.callable.execute((t) -> {
                        if (!task.resolve.done())
                            task.resolve.call(t);
                        task.setDone(null);
                    }, (t) -> {
                        throw new InternalReject(task, new Reject(t));
                    });
                }
                catch (Resolve resolve) {
                    if (!task.resolve.done())
                        task.resolve.call(resolve.getReal());
                    task.setDone(null);
                }
                catch (Reject reject) {
                    throw new InternalReject(task, reject);
                }
                catch (InternalReject r) { throw r; }
                catch (Throwable throwable) {
                    throw new InternalReject(task, new Reject(throwable));
                }
            }
            catch(InternalReject rej) {
                reject(rej.task, rej.real.getReal());
            } finally {
                running = false;
            }
        }
        catch (Reject e) {
            if(!rejectionHandlers.exists()) {
                System.err.println("!! Unhandled Task rejection:");
                e.printStackTrace();
                e.getCause().printStackTrace();
                return;
            }
            rejectionHandlers.call(e);
        }
    }

    private <T> void reject(Task<T> task, Throwable real) {
        if (!task.reject.done()) {
            task.setDone(real);
            if (task.reject.exists() || task.isAwaiting) {
                try {
                    task.reject.call(real);
                } catch(InternalReject r) {
                    reject(r.task, r.real.getReal());
                }
            }
            else {
                throw new Reject(real);
            }
        }
    }
    
}
