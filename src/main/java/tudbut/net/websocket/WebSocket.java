package tudbut.net.websocket;

import de.tudbut.type.O;
import tudbut.io.TypedInputStream;

import java.io.IOException;
import java.io.InputStream;

public class WebSocket {

    InputStream in;
    TypedInputStream tin;
    
    public Operation readFrame() throws IOException {
        int i;
        i = in.read();
        boolean fin = (i & 0b10000000) != 0;
        if((i & 0b01110000) != 0) {
            fail();
        }
        int opcode = i & 0xF;
        i = in.read();
        boolean mask = (i & 0b10000000) != 0;
        long payloadLen = i & 0b01111111;
        long tmpLen = payloadLen;
        if(payloadLen == 126) {
            tmpLen = tin.readChar();
        }
        if(payloadLen == 127) {
            tmpLen = tin.readLong();
        }
        payloadLen = tmpLen;
        int maskKey = 0;
        if(mask) {
            maskKey = tin.readInt();
        }
        int[] appData = new int[(int) Math.ceil(payloadLen / 4d)];
        for (int j = 0 ; j < appData.length ; j++) {
            i = tin.readInt();
            appData[j] = mask ? i ^ maskKey : i;
        }
        byte[] appDataBytes = new byte[(int) (appData.length - (4 - (payloadLen % 4)))];
        for (int j = 0 ; j < appData.length ;) {
            // TODO check the way this is executed by JVM! (should work tho)
            if(j < appDataBytes.length)
                appDataBytes[j] = (byte) (appData[j++] >> 8 * 3 & 0xff);
            if(j < appDataBytes.length)
                appDataBytes[j] = (byte) (appData[j++] >> 8 * 2 & 0xff);
            if(j < appDataBytes.length)
                appDataBytes[j] = (byte) (appData[j++] >> 8 * 1 & 0xff);
            if(j < appDataBytes.length)
                appDataBytes[j] = (byte) (appData[j++] >> 8 * 0 & 0xff);
        }
        return new Operation(opcode, appDataBytes);
    }
    
    private void fail() {
    
    }
}
