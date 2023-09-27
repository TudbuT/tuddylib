package de.tudbut.net.smtp;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class SMTPDataOutput {
    private final SMTPSender parent;
    final PrintWriter write;
    
    private boolean headersOpen = true;
    private boolean dataOpen = false;
    
    SMTPDataOutput(SMTPSender sender, PrintWriter writer) {
        parent = sender;
        this.write = writer;
    }
    
    private void ensureHeadersOpen() {
        if(!headersOpen)
            throw new HeadersClosedException();
    }
    private void ensureDataOpen() {
        if(!dataOpen)
            throw new DataClosedException();
    }
    
    public void from(String s) {
        ensureHeadersOpen();
        write.println("From: " + s);
    }
    
    public void to(String s) {
        ensureHeadersOpen();
        write.println("To: " + s);
    }
    
    public void subject(String s) {
        ensureHeadersOpen();
        write.println("Subject: " + s);
    }
    
    public void date(Date date) {
        ensureHeadersOpen();
        write.println("Date: " + DateTimeFormatter.ISO_DATE_TIME.format(date.toInstant()));
    }
    
    public void header(String k, String v) {
        ensureHeadersOpen();
        write.println(k + ": " + v);
    }
    
    public void closeHeaders() {
        ensureHeadersOpen();
        write.println();
        headersOpen = false;
    }
    
    public PrintStream getDataOutputStream() {
        if(headersOpen)
            closeHeaders();
        
        return new PrintStream(new OutputStream() {
            int last = 0x0A;
            
            @Override
            public void write(int i) throws IOException {
                if(i != '.' && last == 0x0A)
                    write.write(i);
                else
                    write.write(" .");
                last = i;
            }
    
            @Override
            public void close() throws IOException {
                super.close();
                closeData();
            }
        });
    }
    
    public void closeData() throws IOException {
        ensureDataOpen();
        write.println(".");
        dataOpen = false;
        parent.expect250();
    }
    
    public static class HeadersClosedException extends RuntimeException {
    
    }
    public static class DataClosedException extends RuntimeException {
    
    }
}
