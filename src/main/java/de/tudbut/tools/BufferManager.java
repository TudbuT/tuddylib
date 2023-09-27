package de.tudbut.tools;

import de.tudbut.io.Buffer;

import java.util.HashMap;
import java.util.Map;

public class BufferManager {
    private static final Map<Long, Buffer> buffers = new HashMap<>();

    public static long createResource(Buffer buffer) {
        long s = newResourceID();
        buffers.put(s, buffer);
        return s;
    }

    public static Buffer getBufferFromID(long id) {
        return buffers.get(id);
    }

    private static long newResourceID() {
        long s = ExtendedMath.randomLong(0, Long.MAX_VALUE - 1);

        while (buffers.containsKey(s))
            s = ExtendedMath.randomLong(0, Long.MAX_VALUE - 1);

        return s;
    }

    public static void removeBuffer(long id) {
        buffers.remove(id);
    }
}
