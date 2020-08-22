package de.tudbut.net.ws;

import de.tudbut.tools.Tools;
import de.tudbut.type.Stoppable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Tools.ObjectMapping implements Runnable, Stoppable {
    private final ServerSocket serverSocket;
    private final ConnectionHandler[] handlers = new ConnectionHandler[256];
    private int latestHandler = -1;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void addHandler(ConnectionHandler connectionHandler) {
        latestHandler++;
        handlers[latestHandler] = connectionHandler;
    }

    public void run() {
        new Thread(() -> {
            while (!isStopped()) {
                try {
                    Socket s = serverSocket.accept();
                    s.setKeepAlive(true);
                    for (ConnectionHandler handler : handlers) {
                        if (handler != null)
                            new Thread(() -> {
                                try {
                                    handler.run(new Connection(s));
                                }
                                catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
