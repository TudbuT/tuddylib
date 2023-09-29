package de.tudbut.security.permissionmanager;

import de.tudbut.security.PermissionManager;
import de.tudbut.security.Strictness;
import de.tudbut.tools.ReflectUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Only allows classes loaded by a certain class loader, and the classloader itself.
 *
 * Supported strictness properties:
 * - Restriction.ClassLoader.MaxDistance (int): How far down the stack trace should the restriction look until it fails
 * - Restriction.ClassLoader.RestrictLambda (bool): If the restriction should apply to lambdas. If true, ONLY classes in the
 *   allowlist pass, instead of allowing the allowed classes to call "through" others.
 */
public class ClassLoaderRestriction extends Restriction {
    private final Set<ClassLoader> allow;

    public ClassLoaderRestriction(PermissionManager parent, ClassLoader... allowFromClassLoaders) {
        super(parent);
        this.allow = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(allowFromClassLoaders)));
    }

    public ClassLoaderRestriction(ClassLoader... allowFromClassLoaders) {
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
                Class<?> cls = getClassObject(element.getClassName());
                // is the classloader or loaded by it?
                if(allow.stream().anyMatch(x -> x.getClass() == cls) || allow.contains(cls.getClassLoader())) {
                    isCalledByAllowed = true;
                    break;
                }
            } catch (Exception e) {
                // it'll just stay false
            }
        }
        return isCalledByAllowed && super.checkCaller(strictnessLevel);
    }

    private Class<?> getClassObject(String className) throws ClassNotFoundException {
        try {
            Method findLoadedClass = ClassLoader.class.getDeclaredMethod("findLoadedClass", String.class);
            ReflectUtil.forceAccessible(findLoadedClass);
            for (ClassLoader allowed : allow) {
                Class<?> clazz = (Class<?>) findLoadedClass.invoke(allowed, className);
                if(clazz != null) {
                    return clazz;
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return Class.forName(className);
    }

    @Override
    public <T> boolean checkLambda(Strictness strictnessLevel, T lambda) {
        boolean b = true;
        if(strictnessLevel.getBoolProperty("Restriction.ClassLoader.RestrictLambda")) {
            // might get more complex soon.
            // is classloader, inner class of it, or loaded by it?


            //noinspection SuspiciousMethodCalls
            b = allow.contains(lambda)
                    || allow.contains(lambda.getClass().getClassLoader());

            // is enclosed class (e.g. anonymous class)
            Class<?> enclosingClass = lambda.getClass().getEnclosingClass();
            if (enclosingClass != null)
                b = b || allow.stream().anyMatch(x -> x.getClass() == enclosingClass);

            // is lambda in allowed class?
            String name = lambda.getClass().getName().replaceAll("\\$\\$Lambda.*$", "");
            b = b || allow.stream().anyMatch(x -> x.getClass().getName().equals(name)); // is lambda in classloader
            try {
                b = b || allow.contains(getClassObject(name).getClassLoader()); // is lambda in classloader-loaded class
            } catch (Exception e) {
                // it'll just stay false
            }
        }
        return b && super.checkLambda(strictnessLevel, lambda);
    }
}
