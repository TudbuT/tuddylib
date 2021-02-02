package de.tudbut.io;

import de.tudbut.type.ByteArrayList;
import de.tudbut.type.CharArrayList;
import de.tudbut.type.IntArrayList;

import java.io.*;

public class StreamReader {
    public static int BUFFER_SIZE = 4096;
    
    public final BufferedReader stream;
    public final InputStream inputStream;
    private int position;
    private boolean endReached = false;

    public StreamReader(InputStream stream) {
        this.inputStream = stream;
        this.stream = new BufferedReader(new InputStreamReader(stream));
    }

    public byte readNextByte() throws IOException, ArrayIndexOutOfBoundsException {
        int got = inputStream.read();
        if (got < 0) {
            endReached = true;
            throw new ArrayIndexOutOfBoundsException("Stream end reached");
        }
        return (byte) got;
    }

    public int readNextUnsignedByte() throws IOException, ArrayIndexOutOfBoundsException {
        return Byte.toUnsignedInt(readNextByte());
    }

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

    public byte[] readAllAsBytes() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] currentBytes = new byte[BUFFER_SIZE];
        int len;
        while ((len = inputStream.read(currentBytes)) > 0) {
            bytes.write(currentBytes, 0, len);
        }
        return bytes.toByteArray();
    }

    public char[] readAllAsChars() throws IOException {
        return new String(readAllAsBytes()).toCharArray();
    }

    public char[] readAllAsCharsUntil(char c) throws IOException {
        CharArrayList chars = new CharArrayList();
        String line;
        boolean stop = false;
        while ((line = stream.readLine()) != null && !stop) {
            for (int i = 0; i < line.length(); i++) {
                chars.add(line.toCharArray()[i]);
                if (line.toCharArray()[i] == c) {
                    stop = true;
                    break;
                }
            }
            chars.add('\n');
        }
        try {
            chars.remove(chars.size() - 1);
            chars.remove(chars.size() - 2);
        }
        catch (Exception ignore) {
        }
        return chars.toCharArray();
    }
}
