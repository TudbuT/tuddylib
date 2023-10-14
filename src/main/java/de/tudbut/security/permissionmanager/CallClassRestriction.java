package de.tudbut.security.permissionmanager;

import de.tudbut.security.PermissionManager;
import de.tudbut.security.Strictness;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Supported strictness properties:
 * - Restriction.CallClass.MaxDistance (int): How far down the stack trace should the restriction look until it fails
 * - Restriction.CallClass.RestrictLambda (bool): If the restriction should apply to lambdas. If true, ONLY classes in the
 *   allowlist pass, instead of allowing the allowed classes to call "through" others.
 */
public class CallClassRestriction extends Restriction {

    private final Set<String> allow;

    public CallClassRestriction(PermissionManager parent, Class<?>... allowFromClasses) {
        super(parent);
        allow = Collections.unmodifiableSet(Arrays.stream(allowFromClasses).map(this::getClassName).collect(Collectors.toSet()));
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
            if (allow.contains(element.getClassName())) {
                isCalledByAllowed = true;
                break;
            }
        }
        return isCalledByAllowed && super.checkCaller(strictnessLevel);
    }

    @Override
    public <T> boolean checkLambda(Strictness strictnessLevel, T lambda) {
        boolean b = true;
        if(strictnessLevel.getBoolProperty("Restriction.CallClass.RestrictLambda")) {
            // might get more complex soon.
            // is class, inner class of it, loaded by it, or lambda in it?
            Class<?> enclosingClass = lambda.getClass().getEnclosingClass();
            boolean[] cache = new boolean[2];
            b = allow.contains(getClassName(lambda.getClass(), cache, 0))
                    || allow.contains(getClassName(lambda.getClass(), cache, 0).replaceAll("\\$\\$Lambda.*$", ""));
            if (enclosingClass != null)
                b = b || allow.contains(getClassName(enclosingClass));
        }
        return b && super.checkLambda(strictnessLevel, lambda);
    }
}
