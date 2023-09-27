package de.tudbut.security.permissionmanager;

import de.tudbut.security.PermissionManager;

public abstract class PermissionManagerAdapter<S> implements PermissionManager<S> {

    protected final PermissionManager<S> parent;

    protected PermissionManagerAdapter(PermissionManager<S> parent) {
        if(parent == null)
            parent = new AllowPermissionManager<>();
        this.parent = parent;
    }

    @Override
    public boolean checkCaller(S strictnessLevel) {
        return parent.checkCaller(strictnessLevel);
    }

    @Override
    public <T> boolean checkLambda(S strictnessLevel, T lambda) {
        return parent.checkLambda(strictnessLevel, lambda);
    }

    @Override
    public void crash(S strictnessLevel) {
        parent.crash(strictnessLevel);
    }
}
