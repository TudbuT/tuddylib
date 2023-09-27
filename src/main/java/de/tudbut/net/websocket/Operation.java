package de.tudbut.net.websocket;

class Operation {
    final int opcode;
    final byte[] data;
    
    Operation(int opcode, byte[] data) {
        this.opcode = opcode;
        this.data = data;
    }
}
