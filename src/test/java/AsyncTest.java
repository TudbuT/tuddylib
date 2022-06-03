import de.tudbut.async.Reject;
import de.tudbut.async.Resolve;
import de.tudbut.async.Task;
import de.tudbut.async.TaskQueue;
import static de.tudbut.async.Async.*;

/**
 * @author TudbuT
 * @since 03 Jun 2022
 */

public class AsyncTest {
    
    public static void main(String[] args) throws IllegalAccessException, InterruptedException {
        context(TaskQueue.main);
        new Task<>((resolve, reject) -> {
            if (5 / 0 == 0) {
                resolve.call("works!");
            }
        }, String.class).then(System.err::println).err(Throwable::printStackTrace).ok();
        new Task<String>((resolve, reject) -> {
            try {
                if(5 / 0 == 0) {
                    resolve.call("works!");
                }
            } catch (Exception e) {
                reject.call(e);
            }
        }).then(System.err::println).err(Throwable::printStackTrace).ok();
        new Task<>((resolve, reject) -> {
            if (5 / 0 == 0) {
                throw new Resolve("works!");
            }
        }, String.class).then(System.err::println).err(Throwable::printStackTrace).ok();
        new Task<String>((resolve, reject) -> {
            try {
                if(5 / 0 == 0) {
                    throw new Resolve("works!");
                }
            } catch (Exception e) {
                throw new Reject(e);
            }
        }).then(System.err::println).err(Throwable::printStackTrace).ok();
        new Task<String>((resolve, reject) -> {
            try {
                if(5 / 0 == 0) {
                    throw new Resolve("works!");
                }
            } catch (Exception e) {
                throw new Reject(e);
            }
        }).then(System.err::println).ok();
        new Task<>((resolve, reject) -> {
            if (5 * 0 == 0) {
                resolve.call("works!");
            }
        }, String.class).then(System.err::println).err(Throwable::printStackTrace).ok();
        new Task<String>((resolve, reject) -> {
            try {
                if(5 * 0 == 0) {
                    resolve.call("works!");
                }
            } catch (Exception e) {
                reject.call(e);
            }
        }).then(System.err::println).err(Throwable::printStackTrace).ok();
        new Task<>((resolve, reject) -> {
            if (5 * 0 == 0) {
                throw new Resolve("works!");
            }
        }, String.class).then(System.err::println).err(Throwable::printStackTrace).ok();
        new Task<String>((resolve, reject) -> {
            try {
                if(5 * 0 == 0) {
                    throw new Resolve("works!");
                }
            } catch (Exception e) {
                throw new Reject(e);
            }
        }).then(System.err::println).err(Throwable::printStackTrace).ok();
    
        new Task<String>((resolve, reject) -> {
            new Task<String>((resolve1, reject1) -> {
                resolve.call("works!");
            }).ok().await();
        }).then(System.err::println).err(Throwable::printStackTrace).ok();
        new Task<Integer>(((resolve, reject) -> {
            resolve.call(500);
        }))
                .compose(String::valueOf)
                .then(System.err::println)
                .ok();
        
        TaskQueue.main.finish();
        Thread.sleep(5);
        System.err.println("\n>>> Output should be 5 exceptions and 5 'works!' and 1 '500'!!!");
        System.exit(0);
    }
}
