package de.tudbut.security;

import de.tudbut.obj.DoubleTypedObject;
import de.tudbut.tools.Lock;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

// Keeps some data as safe as possible, unable to be accessed even by reflection
public class DataKeeper<T> {
    public static boolean forgetAll = false;


    private final PermissionManager permissionManager;
    private Supplier<T> dataInsertion;
    private final Strictness strictness;
    private final Lock lock = new Lock(true);
    private final Queue<DoubleTypedObject<Consumer<Accessor<T>>, Lock>> nextFunctionToRun = new LinkedList<>();
    private final Thread keeper = new Thread(this::keep, "DataKeeper"); { keeper.start(); }

    static { initSecurity(); }

    private static void initSecurity() {
        // this should prevent any reflection, but is not a 100% guarantee!
        AccessKiller.killReflectionFor(DataKeeper.class, Accessor.class);
    }

    public DataKeeper(PermissionManager permissionManager, Strictness strictness, T toKeep) {
        // make sure reflection is killed for it
        permissionManager.killReflection();

        this.permissionManager = permissionManager;
        dataInsertion = () -> toKeep;
        this.strictness = strictness;
        lock.unlock();
    }

    public Strictness getStrictness() {
        return strictness;
    }

    public void access(Consumer<Accessor<T>> accessor) {
        if(!permissionManager.checkCaller(strictness)) {
            if(permissionManager.showErrors())
                throw new IllegalAccessError("The active PermissionManager does not allow you to access this DataKeeper.");
        }
        Lock waitLock = new Lock(true);
        nextFunctionToRun.add(new DoubleTypedObject<>(accessor, waitLock));
        lock.unlock();
        waitLock.waitHere(500);
    }

    private void keep() {
        lock.waitHere();
        lock.lock();
        PermissionManager permissionManager = this.permissionManager;
        AtomicReference<T> data = new AtomicReference<>(dataInsertion.get());
        Strictness strictness = this.strictness;
        dataInsertion = null;
        while(!forgetAll) {
            lock.waitHere();
            lock.lock(500);

            DoubleTypedObject<Consumer<Accessor<T>>, Lock> itm = nextFunctionToRun.poll();
            if(itm == null)
                continue;
            Consumer<Accessor<T>> toRun = itm.o;
            Lock lock = itm.t;
            // second layer of protection, crashes this time.
            if(!permissionManager.checkLambda(strictness, toRun))
                permissionManager.crash(strictness);

            toRun.accept(new Accessor<>(permissionManager, strictness, data));
            lock.unlock();
        }
    }

    // A very last, third layer of protection, not actually that necessary.
    public static class Accessor<T> {
        // The accessor will only ever be in local variables, so it does
        // not have to be reflection-safe. But it is anyway due to AccessKiller.
        private final PermissionManager permissionManager;
        private final AtomicReference<T> value;
        private final Strictness strictness;

        public Accessor(PermissionManager permissionManager, Strictness strictness, AtomicReference<T> data) {
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
