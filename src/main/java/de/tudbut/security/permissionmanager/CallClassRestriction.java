package de.tudbut.security.permissionmanager;

import de.tudbut.security.PermissionManager;
import de.tudbut.security.Strictness;

import java.util.*;
import java.util.stream.Collectors;

public class CallClassRestriction extends Restriction {

    private final Set<String> allow;

    public CallClassRestriction(PermissionManager parent, Class<?>... allowFromClasses) {
        super(parent);
        allow = Collections.unmodifiableSet(Arrays.stream(allowFromClasses).map(Class::getName).collect(Collectors.toSet()));
    }
    public CallClassRestriction(Class<?>... allowFromClasses) {
        this(null, allowFromClasses);
    }

    @Override
    public boolean checkCaller(Strictness strictnessLevel) {
        StackTraceElement[] st = Thread.currentThread().getStackTrace();

        if(strictnessLevel.hasProperty("Restriction.CallClass.MaxDistance")) {
            int maxDist = strictnessLevel.getIntProperty("Restriction.CallClass.MaxDistance");
            if(st.length > maxDist) {
                StackTraceElement[] elements = new StackTraceElement[maxDist];
                System.arraycopy(st, 0, elements, 0, maxDist);
                st = elements;
            }
        }

        boolean isCalledByAllowed = false;
        for (StackTraceElement element : st) {
            if(allow.contains(element.getClassName())) {
                isCalledByAllowed = true;
                break;
            }
        }
        return isCalledByAllowed && super.checkCaller(strictnessLevel);
    }

    @Override
    public <T> boolean checkLambda(Strictness strictnessLevel, T lambda) {
        // might get more complex soon.
        // is class, inner class of it, loaded by it, or lambda in it?
        Class<?> enclosingClass = lambda.getClass().getEnclosingClass();
        boolean b = allow.contains(lambda.getClass().getName())
                || allow.contains(lambda.getClass().getName().replaceAll("\\$\\$Lambda.*$", ""));
        if(enclosingClass != null)
            b = b || allow.contains(enclosingClass.getName());
        return b && super.checkLambda(strictnessLevel, lambda);
    }
}
