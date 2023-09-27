package de.tudbut.security.permissionmanager;

import de.tudbut.security.PermissionManager;

public class HideErrorPermissionManager<S> extends PermissionManagerAdapter<S> {
    protected HideErrorPermissionManager(PermissionManager<S> parent) {
        super(parent);
    }

    @Override
    public boolean showErrors() {
        return false;
    }
}
