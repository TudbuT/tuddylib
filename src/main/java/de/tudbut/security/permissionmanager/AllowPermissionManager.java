package de.tudbut.security.permissionmanager;

import de.tudbut.security.PermissionManager;
import de.tudbut.security.Strictness;

public class AllowPermissionManager implements PermissionManager {
    @Override
    public boolean checkCaller(Strictness strictnessLevel) {
        return true;
    }

    @Override
    public <T> boolean checkLambda(Strictness strictnessLevel, T lambda) {
        return true;
    }
}
