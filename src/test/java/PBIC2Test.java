import de.tudbut.net.http.HTTPRequest;
import de.tudbut.net.http.HTTPRequestType;
import de.tudbut.net.http.HTTPServer;
import de.tudbut.net.pbic2.PBIC2AEventHandler;
import de.tudbut.net.pbic2.PBIC2AListener;
import de.tudbut.net.pbic2.PBIC2Client;
import de.tudbut.net.pbic2.PBIC2Server;
import de.tudbut.tools.encryption.KeyStream;
import de.tudbut.tools.encryption.RawKey;

import java.io.IOException;

public class PBIC2Test {
    
    public static void main(String[] args) throws IOException {
    
        HTTPServer server = new HTTPServer(5000, null, Runnable::run);
        RawKey key = new RawKey("hello");
        KeyStream stream0 = new KeyStream(key);
        KeyStream stream1 = new KeyStream(key);
    
        server.addHandler(request -> {
            PBIC2Server serv = new PBIC2Server(request, stream0::encrypt, stream0::decrypt);
            for (int i = 0 ; i < 100000 ; i++) {
                StringBuilder builder = new StringBuilder();
                double l = Math.random() * 1000;
                System.out.println((int) l);
                for (int j = 0 ; j < (int) l ; j++) {
                    builder.append((char) (Math.random() * Character.MAX_VALUE + 1));
                }
                System.out.println(serv.writeMessageWithResponse(builder.toString()).length());
                Thread.sleep(100);
            }
        });
        server.listen();
    
        HTTPRequest request = new HTTPRequest(HTTPRequestType.POST, "localhost", 5000, "/");
        PBIC2Client client = new PBIC2Client(request, stream1::encrypt, stream1::decrypt);
        PBIC2AEventHandler handler = new PBIC2AEventHandler();
        handler.start(client, new PBIC2AListener() {
            @Override
            public void onMessage(String message) throws IOException {
                System.out.println(message.length());
                client.writeMessage("hhhh");
            }
    
            @Override
            public void onError(Throwable throwable) {
        
            }
        });
    }
}
