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
                            "[{\"name\":\"IDEqe\"}]");
        System.out.println(tcn.toString());
    }
}
