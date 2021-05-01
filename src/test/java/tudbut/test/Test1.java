package tudbut.test;

import tudbut.net.http.HTTPRequest;
import tudbut.net.http.HTTPRequestType;
import tudbut.net.smtp.SMTPDataOutput;
import tudbut.net.smtp.SMTPSender;
import tudbut.parsing.JSON;
import tudbut.parsing.TCN;

import java.io.IOException;
import java.io.PrintStream;
import java.util.UUID;

public class Test1 {
    
    public static void main(String[] args) throws IOException, JSON.JSONFormatException {
    
        UUID uuid = UUID.fromString("fdee323e-7f0c-4c15-8d1c-0f277442342a");
        // {"test":"lol","xd":"OMG"}
        HTTPRequest req = new HTTPRequest(HTTPRequestType.GET, "https://api.mojang.com", 443, "/user/profiles/" + (uuid.toString().replaceAll("-", "")) + "/names");
    
        req.sendKeepAlive().addChangeListener(completed -> System.out.println(completed.value));
    }
}
