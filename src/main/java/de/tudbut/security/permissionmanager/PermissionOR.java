package de.tudbut.security.permissionmanager;

import de.tudbut.security.PermissionManager;

public class PermissionOR<S> implements PermissionManager<S> {

    private final PermissionManager<S> primary, secondary;

    public PermissionOR(PermissionManager<S> primary, PermissionManager<S> secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    @Override
    public boolean checkCaller(S strictnessLevel) {
        return primary.checkCaller(strictnessLevel) || secondary.checkCaller(strictnessLevel);
    }

    @Override
    public <T> boolean checkLambda(S strictnessLevel, T lambda) {
        return primary.checkLambda(strictnessLevel, lambda) || secondary.checkLambda(strictnessLevel, lambda);
    }

    @Override
    public void crash(S strictnessLevel) {
        primary.crash(strictnessLevel);
        secondary.crash(strictnessLevel);
    }

    @Override
    public boolean showErrors() {
        return primary.showErrors() || secondary.showErrors();
    }
}
