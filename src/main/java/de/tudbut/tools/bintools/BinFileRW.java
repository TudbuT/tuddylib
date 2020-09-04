package de.tudbut.tools.bintools;

import de.tudbut.tools.FileRW;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BinFileRW extends FileRW {
    int[] chars;

    public BinFileRW(String path) throws IOException {
        super(path);
    }

    public int[] getBinContent() throws IOException {
        rereadFile();
        return chars;
    }

    public void setBinContent(int[] content) throws IOException {
        this.chars = content;
        int[] nChars = new int[this.chars.length];
        System.arraycopy(chars, 0, nChars, 0, chars.length);
        chars = nChars;
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream fileWriter = new BufferedOutputStream(fos);
        for (int ch : chars) {
            fileWriter.write(ch);
        }
        fileWriter.close();
    }

    public void rereadFile() throws IOException {
        FileInputStream fileReader = new FileInputStream(file);
        chars = new int[(int) file.length()];
        for (int i = 0; i < file.length(); i++) {
            chars[i] = fileReader.read();
        }
    }
}
