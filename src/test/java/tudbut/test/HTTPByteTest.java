package tudbut.test;

import de.tudbut.tools.Tools;
import tudbut.net.http.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class HTTPByteTest {
    
    public static void main(String[] args) throws IOException {
        HTTPServer server = new HTTPServer(28735, HTTPResponseFactory.create(HTTPResponseFactory.ResponseCode.NotImplemented, "", HTTPContentType.ANY), Runnable::run);
        
        AtomicReference<String> check = new AtomicReference<>("");
        
        server.addHandler(request -> {
            String s = Arrays.toString(Tools.byteArrayToIntArray(request.parse().getBodyBytes()));
            check.updateAndGet(v -> v + s + "\n");
            System.out.println(s);
            request.respond(
                    HTTPResponseFactory.create(
                            HTTPResponseFactory.ResponseCode.OK,
                            new String(new byte[] { 0, 1, 2, (byte) 255 }, StandardCharsets.ISO_8859_1),
                            HTTPContentType.BIN
                    )
            );
        });
        server.listen();
    
        HTTPRequest request = new HTTPRequest(HTTPRequestType.GET, "localhost", 28735, "/", HTTPContentType.BIN, "\u0000\u0001\u0002\u00ff");
        String s = Arrays.toString(Tools.byteArrayToIntArray(request.send().parse().getBodyBytes()));
        check.updateAndGet(v -> v + s + "\n");
        System.out.println(s);
        System.out.println("\n--- TEST DONE ---");
        
        System.out.println("\n- Expected output -");
        System.out.println("[0, 1, 2, 255]\n" +
                           "[0, 1, 2, 255]");
    
        System.out.println("\n- CHECK -");
        boolean pass = check.get().equals("[0, 1, 2, 255]\n" +
                                          "[0, 1, 2, 255]\n");
        if(pass) {
            System.out.println("PASSED");
            System.exit(0);
        } else {
            System.out.println("FAILED");
            System.exit(1);
        }
    }
}
