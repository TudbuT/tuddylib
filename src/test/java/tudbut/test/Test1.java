package tudbut.test;

import tudbut.net.smtp.SMTPDataOutput;
import tudbut.net.smtp.SMTPSender;
import tudbut.parsing.JSON;
import tudbut.parsing.TCN;

import java.io.IOException;
import java.io.PrintStream;

public class Test1 {
    
    public static void main(String[] args) throws IOException, JSON.JSONFormatException {
    
        // {"test":"lol","xd":"OMG"}
        TCN tcn = JSON.read("{\"test\": \"lol\", \"teli845u\":rtrt, \"xd\": \"OMG\",\"hello\":{\"lol\":\"XDD\"}}");
        System.out.println(tcn.toString());
        System.out.println(JSON.write(tcn));
        System.out.println(JSON.write(JSON.read(JSON.write(tcn))));
    }
}
