package tudbut.io;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DirectBuffers {
    
    public static ByteBuffer createDirectByteBuffer(int size) {
        return prepareBuffer(ByteBuffer.allocateDirect(size));
    }
    
    private static ByteBuffer prepareBuffer(ByteBuffer buffer) {
        return buffer.order(ByteOrder.nativeOrder());
    }
}