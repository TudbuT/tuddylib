package de.tudbut.security.permissionmanager;

import de.tudbut.security.PermissionManager;
import de.tudbut.security.Strictness;

public class DenyPermissionManager implements PermissionManager {
    @Override
    public boolean checkCaller(Strictness strictnessLevel) {
        return false;
    }

    @Override
    public <T> boolean checkLambda(Strictness strictnessLevel, T lambda) {
        return false;
    }
}
