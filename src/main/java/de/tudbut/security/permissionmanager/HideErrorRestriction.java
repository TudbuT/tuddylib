package de.tudbut.security.permissionmanager;

import de.tudbut.security.PermissionManager;

public class HideErrorRestriction extends Restriction {
    public HideErrorRestriction(PermissionManager parent) {
        super(parent);
    }

    @Override
    public boolean showErrors() {
        return false;
    }
}
