package tudbut.io;

import tudbut.tools.ReflectUtil;

import java.io.*;

public class CLSPrintWriter extends PrintWriter {
    
    public String customLineSeparator = "\n";
    
    public CLSPrintWriter(Writer writer) {
        super(writer);
    }
    
    public CLSPrintWriter(Writer writer, boolean b) {
        super(writer, b);
    }
    
    public CLSPrintWriter(OutputStream outputStream) {
        super(outputStream);
    }
    
    public CLSPrintWriter(OutputStream outputStream, boolean b) {
        super(outputStream, b);
    }
    
    public CLSPrintWriter(String s) throws FileNotFoundException {
        super(s);
    }
    
    public CLSPrintWriter(String s, String s1) throws FileNotFoundException, UnsupportedEncodingException {
        super(s, s1);
    }
    
    public CLSPrintWriter(File file) throws FileNotFoundException {
        super(file);
    }
    
    public CLSPrintWriter(File file, String s) throws FileNotFoundException, UnsupportedEncodingException {
        super(file, s);
    }
    
    @Override
    public void println() {
        try {
            synchronized(this.lock) {
                if (this.out == null) {
                    throw new IOException("Stream closed");
                }
                this.out.write(customLineSeparator);
                if (ReflectUtil.getPrivateFieldByTypeIndex(PrintWriter.class, this, boolean.class, 0)) {
                    this.out.flush();
                }
            }
        } catch (InterruptedIOException var4) {
            Thread.currentThread().interrupt();
        } catch (IOException var5) {
            ReflectUtil.setPrivateFieldByTypeIndex(PrintWriter.class, this, boolean.class, 1, true);
        }
    }
}
