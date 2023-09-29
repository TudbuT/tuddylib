package de.tudbut.security;

public class ExtendedStrictness implements Strictness {
    static {
        AccessKiller.killReflectionFor(ExtendedStrictness.class);
    }
    private final Strictness primary, secondary;

    public ExtendedStrictness(Strictness primary, Strictness secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    @Override
    public Object getRawProperty(String name) {
        if(primary.hasProperty(name))
            return primary.getRawProperty(name);
        return secondary.getRawProperty(name);
    }

    @Override
    public Strictness clone() {
        return new ExtendedStrictness(primary.clone(), secondary.clone());
    }
}
