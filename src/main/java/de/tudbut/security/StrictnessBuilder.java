package de.tudbut.security;

import de.tudbut.parsing.TCN;

import java.util.HashMap;

public class StrictnessBuilder {
    static {
        AccessKiller.killReflectionFor(StrictnessBuilder.class);
    }

    private final HashMap<String, Object> properties = new HashMap<>();

    public static StrictnessBuilder create() {
        return new StrictnessBuilder();
    }

    public static Strictness empty() {
        return new StrictnessBuilder().build();
    }

    public StrictnessBuilder property(String s, Object o) {
        properties.put(s, o);
        return this;
    }

    public StrictnessBuilder fromTCN(TCN tcn) {
        properties.putAll(tcn.toMap());
        return this;
    }

    public Strictness build() {
        return new StrictnessImpl((HashMap<String, Object>) properties.clone());
    }

    private static class StrictnessImpl implements Strictness {
        static {
            AccessKiller.killReflectionFor(StrictnessImpl.class);
        }

        private final HashMap<String, Object> properties;

        public StrictnessImpl(HashMap<String, Object> properties) {
            this.properties = properties;
        }

        @Override
        public Object getRawProperty(String name) {
            return properties.get(name);
        }
    }
}
