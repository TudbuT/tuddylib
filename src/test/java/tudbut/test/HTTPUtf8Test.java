package tudbut.test;

import tudbut.net.http.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class HTTPUtf8Test {
    
    public static void main(String[] args) throws IOException {
        HTTPServer server = new HTTPServer(28735, HTTPResponseFactory.create(HTTPResponseCode.NotImplemented, "", HTTPContentType.ANY), Runnable::run);
    
        AtomicReference<String> check = new AtomicReference<>("");
    
        server.addHandler(request -> {
            String s = request.parse().getBody();
            check.updateAndGet(v -> v + s + "\n");
            System.out.println(s);
            request.respond(
                    HTTPResponseFactory.create(
                            HTTPResponseCode.OK,
                            HTTPUtils.utf8ToRaw("Hello world! Ǫ"),
                            HTTPContentType.BIN
                    )
            );
        });
        server.listen();
        
        String s;
        HTTPRequest request;
        
        request = new HTTPRequest(HTTPRequestType.GET, "localhost", 28735, "/", HTTPContentType.BIN, HTTPUtils.utf8ToRaw("Hello world! Ǫ"));
        s = request.send().parse().getBody();
        String finalS = s;
        check.updateAndGet(v -> v + finalS + "\n");
        System.out.println(s);
        request = new HTTPRequest(HTTPRequestType.GET, "localhost", 28735, "/", HTTPContentType.BIN, "Hello world! Ǫ");
        s = request.send().parse().getBodyRaw();
        String finalS1 = s;
        check.updateAndGet(v -> v + finalS1 + "\n");
        System.out.println(s);
        System.out.println("\n--- TEST DONE ---\n");
    
        System.out.println("Expected output: ");
        System.out.println("Hello world! Ǫ\n" +
                           "Hello world! Ǫ\n" +
                           "Hello world! ?\n" +
                           "Hello world! Çª");
    
        System.out.println("\n- CHECK -");
        boolean pass = check.get().equals("Hello world! Ǫ\n" +
                                          "Hello world! Ǫ\n" +
                                          "Hello world! ?\n" +
                                          "Hello world! Çª\n");
        if(pass) {
            System.out.println("PASSED");
            System.exit(0);
        } else {
            System.out.println("FAILED");
            System.exit(1);
        }
    }
}
