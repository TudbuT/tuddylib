package de.tudbut.net.ws;


import java.io.IOException;

public interface ConnectionHandler {
    void run(Connection connection) throws IOException;
}
