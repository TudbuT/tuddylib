package tudbut.net.ic;

import de.tudbut.tools.BetterJ;
import de.tudbut.tools.Tools;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PBIC {
    
    public static int getInt(byte[] bytes) {
        if(bytes.length == 4) {
            int i = 0;
            int[] ints = Tools.byteArrayToIntArray(bytes);
            i += ints[0] << 3 * 8;
            i += ints[1] << 2 * 8;
            i += ints[2] << 1 * 8;
            i += ints[3] << 0 * 8;
            return i;
        }
        else
            throw new NumberFormatException();
    }
    
    public static byte[] putInt(int i) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((i >> 3 * 8) & 0xff);
        bytes[1] = (byte) ((i >> 2 * 8) & 0xff);
        bytes[2] = (byte) ((i >> 1 * 8) & 0xff);
        bytes[3] = (byte) ((i >> 0 * 8) & 0xff);
        return bytes;
    }
    
    public static class Server implements Closeable {
        private final int port;
        protected final ServerSocket server;
        public ArrayList<Socket> sockets = new ArrayList<>();
        public ArrayList<Bus> busses = new ArrayList<>();
        public ArrayList<Connection> connections = new ArrayList<>();
        public ArrayList<Runnable> onJoin = new ArrayList<>();
        public Connection lastConnection;
        
        public Server(int port) throws IOException {
            this.port = port;
            server = new ServerSocket();
        }
        
        protected void listen() {
            while (server.isBound() && !server.isClosed()) {
                try {
                    Socket socket = server.accept();
                    
                    sockets.add(socket);
                    Bus bus = new Bus(socket);
                    busses.add(bus);
                    lastConnection = onJoin(socket, bus);
                    Runnable[] array = onJoin.toArray(new Runnable[0]);
                    for (int i = 0; i < array.length; i++) {
                        BetterJ.t(array[i]);
                    }
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        private Connection onJoin(Socket socket, Bus bus) {
            PBIC.Connection connection = new PBIC.Connection() {
                public Bus getBus() {
                    return bus;
                }
        
                public Socket getSocket() {
                    return socket;
                }
            };
            this.connections.add(connection);
            return connection;
        }
        
        public void start() throws IOException {
            server.bind(new InetSocketAddress(port));
            BetterJ.t(this::listen);
        }
        
        public int getPort() {
            return port;
        }
    
        @Override
        public void close() throws IOException {
            int i;
            for(i = 0; i < this.busses.size(); ++i) {
                this.busses.get(i).close();
            }
    
            for(i = 0; i < this.sockets.size(); ++i) {
                this.sockets.get(i).close();
            }
    
            this.server.close();
        }
        
    }
    
    public static class PBICException extends Exception {
        public PBICException(String message) {
            super(message);
        }
        public PBICException(String message, Exception cause) {
            super(message, cause);
        }
    
        public PBICException(Exception cause) {
            super(cause);
        }
    
        public static class PBICReadException extends PBICException {
    
            public PBICReadException(Exception cause) {
                super(cause);
            }
        }
        public static class PBICWriteException extends PBICException {
    
            public PBICWriteException(Exception cause) {
                super(cause);
            }
        }
    }
    
    public interface Connection {
        
        Bus getBus();
        
        Socket getSocket();
    
        Map<Connection, Boolean> oSync = new HashMap<>();
        Map<Connection, Boolean> iSync = new HashMap<>();
        
        default boolean isClosed() {
            return getBus().isClosed() || getSocket().isClosed();
        }
        
        default void writePacket(Packet packet) throws PBICException.PBICWriteException {
            
            while (oSync.getOrDefault(this, false));
            
            oSync.put(this, true);
            
            ByteBuffer buffer;
    
            int length = packet.getLength();
            byte[] content = packet.getContentBytes();
    
            try {
                // Send length
                buffer = ByteBuffer.allocate(Integer.BYTES);
                buffer.put(putInt(length));
                getBus().write(buffer);
    
                // Send content
                buffer = ByteBuffer.allocate(length);
                for (int i = 0; i < content.length; i++) {
                    buffer.put(content[i]);
                }
                getBus().write(buffer);
            } catch (Exception e) {
                oSync.put(this, false);
                throw new PBICException.PBICWriteException(e);
            }
    
            oSync.put(this, false);
        }
        
        default Packet readPacket() throws PBICException.PBICReadException {
    
            while (iSync.getOrDefault(this, false));
    
            iSync.put(this, true);
            
            ByteBuffer buffer;
            
            int length;
            byte[] content;
    
            String strContent;
            try {
                // Read length
                buffer = ByteBuffer.allocate(Integer.BYTES);
                getBus().read(buffer);
                length = getInt(buffer.array());
    
                // Read content
                buffer = ByteBuffer.allocate(length);
                getBus().read(buffer);
                content = buffer.array();
    
                // Parse content
                strContent = new String(content);
            } catch (Exception e) {
                iSync.put(this, false);
                throw new PBICException.PBICReadException(e);
            }
    
            iSync.put(this, false);
            
            return new Packet() {
                @Override
                public int getLength() {
                    return length;
                }
                
                @Override
                public String getContent() {
                    return strContent;
                }
            };
        }
    }
    
    public interface Packet {
        default int getLength() {
            return getContentBytes().length;
        }
        default byte[] getContentBytes() {
            return getContent().getBytes();
        }
        
        String getContent();
    }
    
    public static class Client implements Closeable {
        private final Socket client;
        public Connection connection;
        
        public Client(String ip, int port) throws IOException {
            client = new Socket(ip, port);
            
            start(new Bus(client));
        }
        
        private void start(Bus bus) {
            connection = new Connection() {
                @Override
                public Bus getBus() {
                    return bus;
                }
    
                @Override
                public Socket getSocket() {
                    return client;
                }
            };
        }
    
        @Override
        public void close() throws IOException {
            connection.getBus().close();
            client.close();
        }
    }
    
}
