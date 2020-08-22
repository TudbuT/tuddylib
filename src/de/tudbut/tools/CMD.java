package de.tudbut.tools;

import de.tudbut.type.StringArray;

import java.io.*;

public class CMD extends Bash {
    private final File file;
    private final FileRW frw;
    private BufferedReader stdO;
    private BufferedReader stdE;
    private BufferedWriter stdI;
    private Process p;
    private final StringArray output = new StringArray();
    private final StringArray error = new StringArray();

    public CMD(String tempFile) throws Exception {
        super(tempFile);
        this.file = new File(tempFile);
        frw = new FileRW(this.file.getAbsolutePath());
    }

    @Override
    public void run(String command, boolean enableSTDINPUT) throws Exception {
        frw.setContent(command);

        this.p = Runtime.getRuntime().exec(file.getAbsolutePath());
        this.stdO = new BufferedReader(new
                                               InputStreamReader(
                p.getInputStream()));
        this.stdE = new BufferedReader(new
                                               InputStreamReader(
                p.getErrorStream()));
        this.stdI = new BufferedWriter(new
                                               OutputStreamWriter(
                p.getOutputStream()));

        if (!enableSTDINPUT) {
            String s = "";
            while ((s = this.stdO.readLine()) != null)
                this.output.add(s);
            while ((s = this.stdE.readLine()) != null)
                this.error.add(s);
        }
    }

    @Override
    public void run(StringArray command, boolean enableSTDINPUT) throws Exception {
        frw.setContent(command);

        this.p = Runtime.getRuntime().exec(file.getAbsolutePath());
        this.stdO = new BufferedReader(new
                                               InputStreamReader(
                p.getInputStream()));
        this.stdE = new BufferedReader(new
                                               InputStreamReader(
                p.getErrorStream()));
        this.stdI = new BufferedWriter(new
                                               OutputStreamWriter(
                p.getOutputStream()));

        if (!enableSTDINPUT) {
            String s = "";
            while ((s = this.stdO.readLine()) != null)
                this.output.add(s);
            while ((s = this.stdE.readLine()) != null)
                this.error.add(s);
        }
    }

    @Override
    public void run(String command) throws Exception {
        frw.setContent(command);

        this.p = Runtime.getRuntime().exec(file.getAbsolutePath());
        this.stdO = new BufferedReader(new
                                               InputStreamReader(
                p.getInputStream()));
        this.stdE = new BufferedReader(new
                                               InputStreamReader(
                p.getErrorStream()));
        this.stdI = new BufferedWriter(new
                                               OutputStreamWriter(
                p.getOutputStream()));

        String s = "";
        while ((s = this.stdO.readLine()) != null)
            this.output.add(s);
        while ((s = this.stdE.readLine()) != null)
            this.error.add(s);
    }

    @Override
    public void run(StringArray command) throws Exception {
        frw.setContent(command);

        this.p = Runtime.getRuntime().exec(file.getAbsolutePath());
        this.stdO = new BufferedReader(new
                                               InputStreamReader(
                p.getInputStream()));
        this.stdE = new BufferedReader(new
                                               InputStreamReader(
                p.getErrorStream()));
        this.stdI = new BufferedWriter(new
                                               OutputStreamWriter(
                p.getOutputStream()));

        String s = "";
        while ((s = this.stdO.readLine()) != null)
            this.output.add(s);
        while ((s = this.stdE.readLine()) != null)
            this.error.add(s);
    }

    @Override
    public StringArray getOutput() {
        return this.output;
    }

    @Override
    public StringArray getError() {
        return this.error;
    }

    @Override
    public String getNextOutput() throws Exception {
        return this.stdO.readLine();
    }

    @Override
    public String getNextError() throws Exception {
        return this.stdE.readLine();
    }

    @Override
    public BufferedWriter getInputWriter() {
        return this.stdI;
    }
}
