package de.tudbut.security.permissionmanager;

import de.tudbut.security.PermissionManager;

public class DenyPermissionManager<S> implements PermissionManager<S> {
    @Override
    public boolean checkCaller(S strictnessLevel) {
        return false;
    }

    @Override
    public <T> boolean checkLambda(S strictnessLevel, T lambda) {
        return false;
    }
}
