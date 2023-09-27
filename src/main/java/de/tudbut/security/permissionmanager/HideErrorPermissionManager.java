package de.tudbut.security.permissionmanager;

import de.tudbut.security.PermissionManager;

public class HideErrorPermissionManager extends PermissionManagerAdapter {
    public HideErrorPermissionManager(PermissionManager parent) {
        super(parent);
    }

    @Override
    public boolean showErrors() {
        return false;
    }
}
