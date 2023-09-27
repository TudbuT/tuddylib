package de.tudbut.security.permissionmanager;

import de.tudbut.security.PermissionManager;

public class AllowPermissionManager<S> implements PermissionManager<S> {
    @Override
    public boolean checkCaller(S strictnessLevel) {
        return true;
    }

    @Override
    public <T> boolean checkLambda(S strictnessLevel, T lambda) {
        return true;
    }
}
