package de.tudbut.io;

import de.tudbut.type.ByteArrayList;
import de.tudbut.type.CharArrayList;
import de.tudbut.type.IntArrayList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamReader {
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
        ByteArrayList bytes = new ByteArrayList();
        byte currentByte;
        while (!endReached) {
            try {
                currentByte = readNextByte();
                bytes.add(currentByte);
            }
            catch (ArrayIndexOutOfBoundsException ignore) {
            }
        }
        return bytes.toByteArray();
    }

    public char[] readAllAsChars() throws IOException {
        CharArrayList chars = new CharArrayList();
        String line;
        while ((line = stream.readLine()) != null) {
            for (int i = 0; i < line.length(); i++) {
                chars.add(line.toCharArray()[i]);
            }
            chars.add('\n');
        }
        try {
            chars.remove(chars.size() - 1);
        }
        catch (Exception ignore) {
        }
        return chars.toCharArray();
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
