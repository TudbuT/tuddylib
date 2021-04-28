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
        TCN tcn = JSON.read("" +
                            "{\n" +
                            "  \"test\": \"lol\", \n" +
                            "  \"teli845u\": \"rtrt\", \n" +
                            "  \"xd\": \"OMG\\\\n\", \n" +
                            "  \"hello\": {\n" +
                            "    \"lol\": \"XDD\"\n" +
                            "  }\n" +
                            "}");
        System.out.println(tcn.toString());
        System.out.println(JSON.writeReadable(tcn));
        System.out.println(JSON.writeReadable(JSON.read(JSON.writeReadable(tcn))));
    }
}
