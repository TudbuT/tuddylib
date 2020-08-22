package de.tudbut.type;

import tudbut.obj.InstanceBoundMap;

public interface Stoppable {
    InstanceBoundMap<String, Boolean> vars = new InstanceBoundMap<>();

    default void stop() {
        vars.set(this, C.STOPPED, true);
    }

    default void start() {
        vars.set(this, C.STOPPED, false);
    }

    default boolean isStopped() {
        vars.setIfNull(this, C.STOPPED, false);
        return vars.get(this, C.STOPPED);
    }

    class C {
        private static final String STOPPED = "stopped";
    }
}
