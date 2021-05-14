package tudbut.net.pbic2;

import tudbut.io.TypedInputStream;
import tudbut.io.TypedOutputStream;

import java.io.IOException;
import java.net.Socket;

public interface PBIC2 {
    
    String readMessage() throws IOException;
    
    String writeMessage(String s) throws IOException;
    
    String writeMessageWithResponse(String s) throws IOException;
    
    Socket getSocket();
    TypedInputStream getInput();
    TypedOutputStream getOutput();
}
