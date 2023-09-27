package de.tudbut.security.permissionmanager;

import de.tudbut.security.PermissionManager;
import de.tudbut.security.Strictness;

/**
 * Equivalent to setting primary's parent to secondary.
 */
public class PermissionAND implements PermissionManager {

    private final PermissionManager primary, secondary;

    public PermissionAND(PermissionManager primary, PermissionManager secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    @Override
    public boolean checkCaller(Strictness strictnessLevel) {
        return primary.checkCaller(strictnessLevel) && secondary.checkCaller(strictnessLevel);
    }

    @Override
    public <T> boolean checkLambda(Strictness strictnessLevel, T lambda) {
        return primary.checkLambda(strictnessLevel, lambda) && secondary.checkLambda(strictnessLevel, lambda);
    }

    @Override
    public void crash(Strictness strictnessLevel) {
        primary.crash(strictnessLevel);
        secondary.crash(strictnessLevel);
    }

    @Override
    public boolean showErrors() {
        return primary.showErrors() && secondary.showErrors();
    }

    @Override
    public void killReflection() {
        PermissionManager.super.killReflection();
        primary.killReflection();
        secondary.killReflection();
    }
}
