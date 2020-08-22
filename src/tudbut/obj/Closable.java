package tudbut.obj;

import java.io.IOException;

public interface Closable {
    InstanceBoundMap<String, Boolean> vars = new InstanceBoundMap<>();

    default void close() throws IOException {
        vars.set(this, C.CLOSED, true);
    }

    default void open() throws NotSupportedException {
        vars.set(this, C.CLOSED, false);
    }

    default boolean isClosed() {
        vars.setIfNull(this, C.CLOSED, false);
        return vars.get(this, C.CLOSED);
    }

    default boolean isOpen() {
        vars.setIfNull(this, C.CLOSED, false);
        return !vars.get(this, C.CLOSED);
    }

    class C {
        public static final String CLOSED = "closed";
    }
}
