package tudbut.test;

import tudbut.net.http.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class HTTPTextTest {
    
    public static void main(String[] args) throws IOException {
        HTTPServer server = new HTTPServer(28735, HTTPResponseFactory.create(HTTPResponseFactory.ResponseCode.NotImplemented, "", HTTPContentType.ANY), Runnable::run);
    
        AtomicReference<String> check = new AtomicReference<>("");
    
        server.addHandler(request -> {
            String s = request.parse().getBodyRaw();
            check.updateAndGet(v -> v + s + "\n");
            System.out.println(s);
            request.respond(
                    HTTPResponseFactory.create(
                            HTTPResponseFactory.ResponseCode.OK,
                            "Hello world!",
                            HTTPContentType.BIN
                    )
            );
        });
        server.listen();
    
        HTTPRequest request = new HTTPRequest(HTTPRequestType.GET, "localhost", 28735, "/", HTTPContentType.BIN, "Hello world!");
        String s = request.send().parse().getBodyRaw();
        check.updateAndGet(v -> v + s + "\n");
        System.out.println(s);
        System.out.println("\n--- TEST DONE ---\n");
    
        System.out.println("Expected output: ");
        System.out.println("Hello world!\n" +
                           "Hello world!");
    
        System.out.println("\n- CHECK -");
        boolean pass = check.get().equals("Hello world!\n" +
                                          "Hello world!\n");
        if(pass) {
            System.out.println("PASSED");
            System.exit(0);
        } else {
            System.out.println("FAILED");
            System.exit(1);
        }
    }
}
