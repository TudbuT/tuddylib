package de.tudbut.security.permissionmanager;

import de.tudbut.security.PermissionManager;
import de.tudbut.security.Strictness;

public abstract class Restriction implements PermissionManager {

    protected PermissionManager parent;

    public Restriction(PermissionManager parent) {
        if(parent == null)
            parent = new AllowAllRestriction();
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

    @Override
    public PermissionManager clone() {
        try {
            Restriction cloned = (Restriction) super.clone();
            cloned.parent = parent.clone();
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
