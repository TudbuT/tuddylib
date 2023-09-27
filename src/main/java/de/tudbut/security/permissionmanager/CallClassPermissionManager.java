package de.tudbut.security.permissionmanager;

import de.tudbut.security.PermissionManager;

import java.util.*;

public class CallClassPermissionManager<S> extends PermissionManagerAdapter<S> {

    private final Set<String> allow;

    public CallClassPermissionManager(PermissionManager<S> parent, String... allowFromClasses) {
        super(parent);
        allow = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(allowFromClasses)));
    }
    public CallClassPermissionManager(String... allowFromClasses) {
        this(null, allowFromClasses);
    }

    @Override
    public boolean checkCaller(S strictnessLevel) {
        StackTraceElement[] st = Thread.currentThread().getStackTrace();

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
    public <T> boolean checkLambda(S strictnessLevel, T lambda) {
        // might get more complex soon.
        // is class, inner class of it, loaded by it, or lambda in it?
        boolean b = allow.contains(lambda.getClass().getName())
                || allow.contains(lambda.getClass().getEnclosingClass().getName())
                || allow.contains(lambda.getClass().getName().replaceAll("\\$\\$Lambda.*$", ""));
        return b && super.checkLambda(strictnessLevel, lambda);
    }
}
