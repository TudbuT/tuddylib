package de.tudbut.io;

import de.tudbut.type.IntArrayList;
import tudbut.obj.CarrierException;

import java.io.*;
import java.net.SocketException;


/**
 * Helper for reading {@link java.io.InputStream}
 */
public class StreamReader {
    
    /**
     * Buffer size for reading. Directly affects speed.
     */
    public static int BUFFER_SIZE = 4096;
    
    /**
     * The stream to read from
     */
    public final InputStream inputStream;
    
    private boolean endReached = false;
    
    /**
     * Constructs a StreamReader for an InputStream
     * @param stream The stream to read
     */
    public StreamReader(InputStream stream) {
        this.inputStream = stream;
    }
    
    /**
     * Reads a byte from the input stream. Unaffected by {@link StreamReader#BUFFER_SIZE}
     * @return read byte
     * @throws IOException Inherited from {@link InputStream#read}
     * @throws ArrayIndexOutOfBoundsException When the stream end was reached
     */
    public byte readNextByte() throws IOException, ArrayIndexOutOfBoundsException {
        int got = inputStream.read();
        if (got < 0) {
            endReached = true;
            throw new ArrayIndexOutOfBoundsException("Stream end reached");
        }
        return (byte) got;
    }
    
    /**
     * Reads a byte from the input stream. Unaffected by {@link StreamReader#BUFFER_SIZE}.
     * Byte is converted to int, being unsigned in the process.
     * @return read unsigned bytes
     * @throws IOException Inherited from {@link StreamReader#readNextByte()}
     * @throws ArrayIndexOutOfBoundsException Inherited from {@link StreamReader#readNextByte()}
     */
    public int readNextUnsignedByte() throws IOException, ArrayIndexOutOfBoundsException {
        return Byte.toUnsignedInt(readNextByte());
    }
    
    /**
     * Reads bytes from the input stream until end is reached. Unaffected by {@link StreamReader#BUFFER_SIZE}.
     * Byte is converted to int, being unsigned in the process.
     * @return read unsigned bytes
     * @throws IOException Inherited from {@link StreamReader#readNextUnsignedByte()}
     */
    public int[] readAllAsUnsignedBytes() throws IOException {
        IntArrayList bytes = new IntArrayList();
        int currentByte;
        while (!endReached) {
            try {
                currentByte = readNextUnsignedByte();
                bytes.add(currentByte);
            }
            catch (ArrayIndexOutOfBoundsException ignore) {
            }
        }
        return bytes.toIntArray();
    }
    
    /**
     * Reads bytes from the input stream until end is reached. Affected by {@link StreamReader#BUFFER_SIZE}.
     * @return read bytes
     * @throws IOException Inherited from {@link StreamReader#readNextByte()}
     */
    public byte[] readAllAsBytes() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] currentBytes = new byte[BUFFER_SIZE];
        int len;
        try {
            while ((len = inputStream.read(currentBytes)) > 0) {
                bytes.write(currentBytes, 0, len);
            }
        } catch (IOException e) {
            throw new CarrierException(e, bytes.toByteArray());
        }
        return bytes.toByteArray();
    }
    
    /**
     * Reads all bytes in the stream and converts them to a char[]
     * @return {@link Character} array (native)
     * @throws IOException Inherited from {@link StreamReader#readAllAsBytes()}
     */
    public char[] readAllAsChars() throws IOException {
        return new String(readAllAsBytes()).toCharArray();
    }
    
    /**
     * Reads all bytes in the stream and converts them to a char[]
     * @param encoding The encoding to use for conversion
     * @return {@link Character} array (native)
     * @throws IOException Inherited from {@link StreamReader#readAllAsBytes()}
     */
    public char[] readAllAsChars(String encoding) throws IOException {
        return new String(readAllAsBytes(), encoding).toCharArray();
    }
    
    /**
     * Same as {@link StreamReader#readAllAsChars()}, but returns a string instead.
     * @return The string
     * @throws IOException Inherited from {@link StreamReader#readAllAsBytes()}
     */
    public String readAllAsString() throws IOException {
        return new String(readAllAsBytes());
    }
    
    /**
     * Returns all lines in the file. All line ending are supported (\r\n, \n, \r).
     * @return The lines
     * @throws IOException Inherited from {@link StreamReader#readAllAsBytes()}
     */
    public String[] readAllAsLines() throws IOException {
        return new String(readAllAsBytes()).replaceAll("\r\n", "\n").replaceAll("\r", "\n").split("\n");
    }
    
    /**
     * Same as {@link StreamReader#readAllAsChars()}, but returns a string instead.
     * @param encoding The encoding to use
     * @return The string
     * @throws IOException Inherited from {@link StreamReader#readAllAsBytes()}
     */
    public String readAllAsString(String encoding) throws IOException {
        return new String(readAllAsBytes(), encoding);
    }
    
    /**
     * Returns all lines in the file. All line ending are supported (\r\n, \n, \r).
     * @param encoding The encoding to use
     * @return The lines
     * @throws IOException Inherited from {@link StreamReader#readAllAsBytes()}
     */
    public String[] readAllAsLines(String encoding) throws IOException {
        return new String(readAllAsBytes(), encoding).replaceAll("\r\n", "\n").replaceAll("\r", "\n").split("\n");
    }
    
}
