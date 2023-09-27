package de.tudbut.security.permissionmanager;

import de.tudbut.security.PermissionManager;
import de.tudbut.security.Strictness;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Only allows classes loaded by a certain class loader, and the classloader itself.
 */
public class ClassLoaderRestriction extends Restriction {
    private final Set<Class<?>> allow;

    public ClassLoaderRestriction(PermissionManager parent, Class<?>... allowFromClassLoaders) {
        super(parent);
        this.allow = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(allowFromClassLoaders)));
    }

    public ClassLoaderRestriction(Class<?>... allowFromClassLoaders) {
        this(null, allowFromClassLoaders);
    }

    @Override
    public boolean checkCaller(Strictness strictnessLevel) {
        StackTraceElement[] st = Thread.currentThread().getStackTrace();

        if(strictnessLevel.hasProperty("Restriction.ClassLoader.MaxDistance")) {
            int maxDist = strictnessLevel.getIntProperty("Restriction.ClassLoader.MaxDistance");
            if(st.length > maxDist) {
                StackTraceElement[] elements = new StackTraceElement[maxDist];
                System.arraycopy(st, 0, elements, 0, maxDist);
                st = elements;
            }
        }

        boolean isCalledByAllowed = false;
        for (StackTraceElement element : st) {
            try {
                Class<?> cls = Class.forName(element.getClassName());
                // is the call the classloader or loaded by it?
                if(allow.contains(cls) || allow.contains(cls.getClassLoader().getClass())) {
                    isCalledByAllowed = true;
                    break;
                }
            } catch (Exception e) {
                // it'll just stay false
            }
        }
        return isCalledByAllowed && super.checkCaller(strictnessLevel);
    }

    @Override
    public <T> boolean checkLambda(Strictness strictnessLevel, T lambda) {
        // might get more complex soon.
        // is classloader, inner class of it, or loaded by it?
        boolean b = allow.contains(lambda.getClass())
                || allow.contains(lambda.getClass().getClassLoader().getClass());
        Class<?> enclosingClass = lambda.getClass().getEnclosingClass();
        if(enclosingClass != null)
            b = b || allow.contains(enclosingClass);
        // is lambda in allowed class?
        String name = lambda.getClass().getName().replaceAll("\\$\\$Lambda.*$", "");
        for (Class<?> clazz : allow) {
            if (clazz.getName().equals(name)) {
                b = true;
                break;
            }
        }
        try {
            b = b || allow.contains(Class.forName(name).getClassLoader().getClass());
        } catch (Exception e) {
            // it'll just stay false
        }
        return b && super.checkLambda(strictnessLevel, lambda);
    }
}
