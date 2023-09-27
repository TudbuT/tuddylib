package de.tudbut.security.permissionmanager;

import de.tudbut.security.PermissionManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Only allows classes loaded by a certain class loader, and the classloader itself.
 * @param <S>
 */
public class ClassLoaderPermissionManager<S> extends PermissionManagerAdapter<S> {
    private final Set<Class<?>> allow;

    public ClassLoaderPermissionManager(PermissionManager<S> parent, Class<?>... allowFromClassLoaders) {
        super(parent);
        this.allow = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(allowFromClassLoaders)));
    }

    public ClassLoaderPermissionManager(Class<?>... allowFromClassLoaders) {
        this(null, allowFromClassLoaders);
    }

    @Override
    public boolean checkCaller(S strictnessLevel) {
        StackTraceElement[] st = Thread.currentThread().getStackTrace();

        boolean isCalledByAllowed = false;
        for (StackTraceElement element : st) {
            try {
                Class<?> cls = Class.forName(element.getClassName());
                // is the call the classloader or loaded by it?
                if(allow.contains(cls) || allow.contains(cls.getClassLoader().getClass())) {
                    isCalledByAllowed = true;
                    break;
                }
            } catch (ClassNotFoundException e) {
                // it'll just stay false
            }
        }
        return isCalledByAllowed && super.checkCaller(strictnessLevel);
    }

    @Override
    public <T> boolean checkLambda(S strictnessLevel, T lambda) {
        // might get more complex soon.
        // is classloader, inner class of it, or loaded by it?
        boolean b = allow.contains(lambda.getClass())
                || allow.contains(lambda.getClass().getEnclosingClass())
                || allow.contains(lambda.getClass().getClassLoader().getClass());
        // is lambda in allowed class?
        String name = lambda.getClass().getName().replaceAll("\\$\\$Lambda.*$", "");
        for (Class<?> clazz : allow) {
            if (clazz.getName().equals(name)) {
                b = true;
                break;
            }
        }
        return b && super.checkLambda(strictnessLevel, lambda);
    }
}
