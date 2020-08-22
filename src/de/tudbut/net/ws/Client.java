package de.tudbut.net.ws;

import java.io.IOException;

public class Client extends Connection {
    public Client(String ip, int port) throws IOException {
        super(ip, port);
    }
}
