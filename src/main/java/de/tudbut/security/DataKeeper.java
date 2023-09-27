package de.tudbut.security;

import de.tudbut.obj.DoubleTypedObject;
import de.tudbut.tools.Lock;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

// Keeps some data as safe as possible, unable to be accessed even by reflection
public class DataKeeper<T, S> {
    public static boolean forgetAll = false;


    private final PermissionManager<S> permissionManager;
    private Supplier<T> dataInsertion;
    private final S strictness;
    private final Lock lock = new Lock(true);
    private final Queue<DoubleTypedObject<Consumer<Accessor<T, S>>, Lock>> nextFunctionToRun = new LinkedList<>();
    private final Thread keeper = new Thread(this::keep, "DataKeeper"); { keeper.start(); }

    static { initSecurity(); }

    private static void initSecurity() {
        // this should prevent any reflection, but is not a 100% guarantee!
        AccessKiller.killReflectionFor(DataKeeper.class, Accessor.class);
    }

    public DataKeeper(PermissionManager<S> permissionManager, S strictness, T toKeep) {
        AccessKiller.killReflectionFor(permissionManager.getClass());
        this.permissionManager = permissionManager;
        dataInsertion = () -> toKeep;
        this.strictness = strictness;
        lock.unlock();
    }

    public boolean access(Consumer<Accessor<T, S>> accessor) {
        if(!permissionManager.checkCaller(strictness)) {
            // false => success (true) = lie, true => failure (false) = truth
            return !permissionManager.showErrors();
        }
        Lock waitLock = new Lock(true);
        nextFunctionToRun.add(new DoubleTypedObject<>(accessor, waitLock));
        lock.unlock();
        waitLock.waitHere(500);
        // success
        return true;
    }

    private void keep() {
        lock.waitHere();
        lock.lock();
        PermissionManager<S> permissionManager = this.permissionManager;
        AtomicReference<T> data = new AtomicReference<>(dataInsertion.get());
        S strictness = this.strictness;
        dataInsertion = null;
        while(!forgetAll) {
            lock.waitHere();
            lock.lock(500);

            DoubleTypedObject<Consumer<Accessor<T, S>>, Lock> itm = nextFunctionToRun.poll();
            if(itm == null)
                continue;
            Consumer<Accessor<T, S>> toRun = itm.o;
            Lock lock = itm.t;
            // second layer of protection, crashes this time.
            if(!permissionManager.checkLambda(strictness, toRun))
                permissionManager.crash(strictness);

            toRun.accept(new Accessor<>(permissionManager, strictness, data));
            lock.unlock();
        }
    }

    // A very last, third layer of protection, not actually that necessary.
    public static class Accessor<T, S> {
        // The accessor will only ever be in local variables, so it does
        // not have to be reflection-safe. But it is anyway due to AccessKiller.
        private final PermissionManager<S> permissionManager;
        private final AtomicReference<T> value;
        private final S strictness;

        public Accessor(PermissionManager<S> permissionManager, S strictness, AtomicReference<T> data) {
            this.permissionManager = permissionManager;
            this.strictness = strictness;
            value = data;
        }

        public T setValue(T newValue) {
            // check is in getValue
            T old = getValue();
            value.set(newValue);
            return old;
        }

        public T getValue() {
            if(permissionManager.checkCaller(strictness))
                return value.get();
            else {
                // crash soon
                new Thread(() -> {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    permissionManager.crash(strictness);
                }).start();
                // generate a weird error
                return (T) value.get().getClass().cast(new Object());
            }
        }
    }
}
