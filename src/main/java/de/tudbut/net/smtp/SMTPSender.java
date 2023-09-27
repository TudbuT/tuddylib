package de.tudbut.net.smtp;

import de.tudbut.io.CLSPrintWriter;

import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SMTPSender {
    
    InputStreamReader in;
    OutputStreamWriter out;
    PrintWriter write;
    BufferedReader read;
    
    public BufferedReader getReader() {
        return read;
    }
    
    
    public SMTPSender(String host, int port, boolean ssl) throws IOException {
        this(socket(host, port, ssl));
    }
    
    private static Socket socket(String host, int port, boolean ssl) throws IOException {
        Socket socket;
        if(ssl) {
            socket = SSLSocketFactory.getDefault().createSocket(host, port);
        }
        else {
            socket = new Socket(host, port);
        }
        return socket;
    }
    
    public SMTPSender(InputStream inputStream, OutputStream outputStream) {
        in = new InputStreamReader(inputStream, StandardCharsets.ISO_8859_1);
        out = new OutputStreamWriter(outputStream, StandardCharsets.ISO_8859_1);
        write = new CLSPrintWriter(out);
        ((CLSPrintWriter)write).customLineSeparator = "\r\n";
        read = new BufferedReader(in);
    }
    
    public SMTPSender(Socket socket) throws IOException {
        this(socket.getInputStream(), socket.getOutputStream());
    }
    
    public void helo(String name) throws IOException {
        write.println("HELO " + name);
        expect220();
    }
    
    public void from(String sender) throws IOException {
        write.println("MAIL FROM:" + sender);
        expect250();
    }
    
    public void to(String recipient) throws IOException {
        write.println("RCPT TO:" + recipient);
        expect250();
    }
    
    public SMTPDataOutput data() throws IOException {
        write.println("DATA");
        expect354();
        return new SMTPDataOutput(this, write);
    }
    
    public void quit() throws IOException {
        write.println("QUIT");
        expect221();
    }
    
    void expect220() throws IOException {
        String s = read.readLine();
        if(!s.replaceAll("-", " ").split(" ", 2)[0].equals("220")) {
            thrUnexpected(s);
        }
    }
    void expect250() throws IOException {
        String s = read.readLine();
        if(!s.replaceAll("-", " ").split(" ", 2)[0].equals("250")) {
            thrUnexpected(s);
        }
    }
    void expect354() throws IOException {
        String s = read.readLine();
        if(!s.replaceAll("-", " ").split(" ", 2)[0].equals("354")) {
            thrUnexpected(s);
        }
    }
    void expect221() throws IOException {
        String s = read.readLine();
        if(!s.replaceAll("-", " ").split(" ", 2)[0].equals("221")) {
            thrUnexpected(s);
        }
    }
    
    private void thrUnexpected(String code) {
        throw new SMTPServerErrorException(code);
    }
}
