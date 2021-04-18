package de.tudbut;

import tudbut.io.FileBus;
import tudbut.io.FileBusTransmitter;
import tudbut.io.StreamRedirect;
import tudbut.io.TypedOutputStream;
import tudbut.net.http.HTTPRequest;
import tudbut.net.http.HTTPRequestType;
import tudbut.obj.Save;
import tudbut.obj.TLMap;
import tudbut.parsing.TCN;
import tudbut.tools.*;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class ee {
    
    public static void main(String[] args) throws IllegalAccessException, ClassNotFoundException, NoSuchFieldException, InstantiationException, IOException, TCN.TCNException, NoSuchAlgorithmException, InterruptedException {
    
        FileBus client0 = new FileBus("AAA.bus");
        FileBus client1 = new FileBus("AAA.bus");
        
        FileBus server0 = new FileBus("AAA2.bus");
        FileBus server1 = new FileBus("AAA2.bus");
    
        ServerSocket serverSocket = new ServerSocket(15485);
        new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 15485);
                FileBusTransmitter transmitter = new FileBusTransmitter(client0, socket.getInputStream(), socket.getOutputStream(), runnable -> new Thread(runnable).start(), runnable -> new Thread(runnable).start(), theException -> {});
                
                client1.startWrite();
                client1.getTypedWriter().writeBoolean(true);
                client1.stopWrite();
                while(true) {
                    System.out.println("C: " + client1.getTypedReader().readString());
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }, "Client").start();
        Socket socket = serverSocket.accept();
        FileBusTransmitter transmitter = new FileBusTransmitter(server0, socket.getInputStream(), socket.getOutputStream(), runnable -> new Thread(runnable).start(), runnable -> new Thread(runnable).start(), theException -> {});
    
        System.out.println("S: " + server1.getTypedReader().readBoolean());
        server1.startWrite();
        server1.getTypedWriter().writeString("hi");
        server1.stopWrite();
    }
}
