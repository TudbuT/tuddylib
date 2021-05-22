package de.tudbut.tools;

import de.tudbut.io.StreamReader;
import de.tudbut.io.StreamWriter;
import tudbut.logger.LoggerSink;
import de.tudbut.type.StringArray;
import tudbut.global.DebugStateManager;

import java.io.*;
import java.util.concurrent.atomic.AtomicReference;

public class FileRW {

    private final AtomicReference<LoggerSink> logger;
    private final StringArray lines;
    protected final File file;

    public FileRW(String path) throws IOException {

        this.file = new File(path);
        logger = DebugStateManager.debugLoggerReference("FileRW " + path);

        logger.get().info("INIT");
        if (!this.file.exists()) {
            logger.get().info("Creating file...");
            this.file.createNewFile();
            new BufferedWriter(new FileWriter(this.file)).write("\n");
            logger.get().info("Done.");
        }
        this.lines = new StringArray();
        logger.get().info("Reading file...");
        rereadFile();
        logger.get().info("Read file.");
    }

    public StringArray getContent() {
        return this.lines;
    }

    public void setContent(String content) throws IOException {
        logger.get().info("Writing file...");
        this.lines.clear();
        this.lines.set(content.split("\n"));
        FileOutputStream fileWriter = new FileOutputStream(this.file);
        new StreamWriter(fileWriter).writeChars(content.toCharArray());
        fileWriter.close();
        logger.get().info("Done.");
    }

    public void setContent(StringArray content) throws IOException {
        setContent(content.join("\n"));
    }

    public void rereadFile() throws IOException {
        StreamReader reader = new StreamReader(new FileInputStream(file));
        char[] chars = reader.readAllAsChars();
        String[] s = new String(chars).split("\n");
        lines.clear();
        for (String value : s) {
            lines.add(value);
        }
    }
} 

/*import tudbut.logger.GlobalLogger;
import tudbut.logger.Logger;
import de.tudbut.type.StringArray;

import java.io.*;

public class FileRW {
    private File file = null;
    private Logger logger = null;
    private StringArray lines = null;

    public FileRW(String path) throws Exception {
        GlobalLogger.info("Initialising FileRW " + path);
        this.file = new File(path);
        this.logger = new Logger("FileRW " + path);
        if(!this.file.exists()) {
            this.logger.info("Creating file...");
            this.file.createNewFile();
            new BufferedWriter(new FileWriter(this.file)).write("\n");
            this.logger.info("Done!");
        }
        FileReader fileReader = new FileReader(this.file);
        BufferedReader reader = new BufferedReader(fileReader);
        this.lines = new StringArray(new String[]{""});
        String _line = "";
        while((_line = reader.readLine()) != null) {
            this.lines = this.lines.add(_line);
        }
        this.logger.info("FileRW " + path + " initialized.");
    }

    public String getContent() throws Exception {
        FileReader fileReader = new FileReader(this.file);
        BufferedReader reader = new BufferedReader(fileReader);
        this.lines = new StringArray(new String[]{""});
        String _line = "";
        String __lines = "";
        while((_line = reader.readLine()) != null) {
            /*this.lines = this.lines.add(_line);
            this.logger.info(this.lines.join("\n"));*//*
            __lines = __lines + "\n" + _line;
        }
        return __lines;
    }

    public String getLine(int line) throws Exception {
        FileReader fileReader = new FileReader(this.file);
        BufferedReader reader = new BufferedReader(fileReader);

        reader.close();
        return lines.asArray()[line];
    }

    public void setContent(String content) throws Exception {
        FileWriter fileWriter = new FileWriter(this.file);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        this.lines = new StringArray(content.split("\n"));
        for (String line : lines.asArray()) {
            writer.write(line);
            writer.newLine();
        }
        writer.close();
    }

    public void setLine(int line, String content) throws Exception {
        FileWriter fileWriter = new FileWriter(this.file);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        lines.asArray()[line] = content;
        for (String line_ : lines.asArray()) {
            writer.write(line_);
            writer.newLine();
        }
        writer.close();
    }
}
*/