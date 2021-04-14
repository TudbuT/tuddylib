package tudbut.io;

import java.io.*;
import java.net.URI;

public class FileBus extends File {
    {
        try {
            if(createNewFile());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    RandomAccessFile file = new RandomAccessFile(this, "rw");
    InputStream i;
    OutputStream o;
    
    {
        try {
            file.seek((int) file.length());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        i = new InputStream() {
            @Override
            public int read() throws IOException {
                return file.read();
            }
        };
        o = new OutputStream() {
            @Override
            public void write(int i) throws IOException {
                file.write(i);
            }
        };
    }
    TypedInputStream ir = new TypedInputStream(i);
    TypedOutputStream ow = new TypedOutputStream(o);
    
    public FileBus(String s) throws FileNotFoundException {
        super(s);
    }
    
    public FileBus(String s, String s1) throws FileNotFoundException {
        super(s, s1);
    }
    
    public FileBus(File file, String s) throws FileNotFoundException {
        super(file, s);
    }
    
    public FileBus(URI uri) throws FileNotFoundException {
        super(uri);
    }
    
    public FileBus(File file) throws FileNotFoundException {
        super(file.getAbsolutePath());
    }
    
    public TypedInputStream getTypedReader() {
        return ir;
    }
    
    public TypedOutputStream getTypedWriter() {
        return ow;
    }
    
    public OutputStream getWriter() {
        return o;
    }
    
    public InputStream getReader() {
        return i;
    }
    
    public void waitForInput() throws IOException {
        ir.waitForInput();
    }
}
