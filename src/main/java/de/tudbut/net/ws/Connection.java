package de.tudbut.net.ws;

import de.tudbut.io.StreamReader;
import de.tudbut.io.StreamWriter;
import de.tudbut.timer.AsyncTask;
import de.tudbut.type.IntArrayList;
import de.tudbut.type.Nothing;
import de.tudbut.type.Stoppable;

import java.io.*;
import java.net.Socket;

public class Connection implements Stoppable, Runnable {
    private final Socket theSocket;
    private final Runnable[] handlers = new Runnable[256];
    private int latestHandler = -1;

    Connection(String ip, int port) throws IOException {
        theSocket = new Socket(ip, port);
    }

    Connection(Socket socket) {
        theSocket = socket;
    }

    public Socket getSocket() {
        return theSocket;
    }

    public void send(String s) throws IOException {
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(theSocket.getOutputStream()));
        writer.println(s);
        writer.flush();
    }

    public void send(int[] bin) throws IOException {
        StreamWriter writer = new StreamWriter(theSocket.getOutputStream());
        for (int i = 0; i < bin.length; i++) {
            if (bin[i] == 0x00) {
                writer.writeUnsignedByte(0x01);
                writer.writeUnsignedByte(0x01);
                continue;
            }
            if (bin[i] == 0x01) {
                writer.writeUnsignedByte(0x01);
                writer.writeUnsignedByte(0x02);
            }
            writer.writeUnsignedByte(bin[i]);
        }
        writer.writeUnsignedByte(0x00);
    }

    public AsyncTask<Nothing> sendAsync(String s) {
        return new AsyncTask<>(() -> {
            send(s);
            return null;
        });
    }

    public AsyncTask<Nothing> sendAsync(int[] s) {
        return new AsyncTask<>(() -> {
            send(s);
            return null;
        });
    }

    public int[] receiveBin() throws IOException {
        while (!new InputStreamReader(getSocket().getInputStream()).ready());
        StreamReader reader = new StreamReader(theSocket.getInputStream());
        IntArrayList list = new IntArrayList();
        int i;
        while ((i = reader.readNextUnsignedByte()) != 0x00) {
            if(i == 0x01) {
                if(reader.readNextUnsignedByte() == 0x01)
                    i = 0x00;
                if(reader.readNextUnsignedByte() == 0x02)
                    i = 0x01;
            }

            list.add(i);
        }
        return list.toIntArray();
    }

    public String receive() throws IOException {
        while (!new InputStreamReader(getSocket().getInputStream()).ready());
        return new BufferedReader(new InputStreamReader(theSocket.getInputStream())).readLine();
    }

    public void addReceiveHook(Runnable runnable) {
        latestHandler++;
        handlers[latestHandler] = runnable;
    }

    @Override
    public void run() {
        new Thread(() -> {
            while (true) {
                try {
                    if ((!isStopped() && !theSocket.isClosed() && theSocket.getInputStream().available() > 0)) {
                        for (Runnable handler : handlers) {
                            if (handler != null)
                                new Thread(handler).start();
                        }
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
