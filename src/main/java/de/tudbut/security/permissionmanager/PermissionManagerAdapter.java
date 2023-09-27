package de.tudbut.security.permissionmanager;

import de.tudbut.security.PermissionManager;
import de.tudbut.security.Strictness;

public abstract class PermissionManagerAdapter implements PermissionManager {

    protected final PermissionManager parent;

    public PermissionManagerAdapter(PermissionManager parent) {
        if(parent == null)
            parent = new AllowPermissionManager();
        this.parent = parent;
    }

    @Override
    public boolean checkCaller(Strictness strictnessLevel) {
        return parent.checkCaller(strictnessLevel);
    }

    @Override
    public <T> boolean checkLambda(Strictness strictnessLevel, T lambda) {
        return parent.checkLambda(strictnessLevel, lambda);
    }

    @Override
    public void crash(Strictness strictnessLevel) {
        parent.crash(strictnessLevel);
    }

    @Override
    public void killReflection() {
        parent.killReflection();
        PermissionManager.super.killReflection();
    }
}
