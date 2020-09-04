package de.tudbut.tools;

import de.tudbut.type.StringArray;

import java.io.*;

public class Bash {
    private final File file;
    private final FileRW frw;
    private BufferedReader stdO;
    private BufferedReader stdE;
    private BufferedWriter stdI;
    private Process p;
    private final StringArray output = new StringArray();
    private final StringArray error = new StringArray();

    public Bash(String tempFile) throws Exception {
        this.file = new File(tempFile);
        frw = new FileRW(this.file.getAbsolutePath());
    }

    public void run(String command, boolean enableSTDINPUT) throws Exception {
        frw.setContent(command);

        this.p = Runtime.getRuntime().exec("bash " + file.getAbsolutePath());
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

    public void run(StringArray command, boolean enableSTDINPUT) throws Exception {
        frw.setContent(command);

        this.p = Runtime.getRuntime().exec("bash " + file.getAbsolutePath());
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

    public void run(String command) throws Exception {
        frw.setContent(command);

        this.p = Runtime.getRuntime().exec("bash " + file.getAbsolutePath());
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

    public void run(StringArray command) throws Exception {
        frw.setContent(command);

        this.p = Runtime.getRuntime().exec("bash " + file.getAbsolutePath());
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

    public StringArray getOutput() {
        return this.output;
    }

    public StringArray getError() {
        return this.error;
    }

    public String getNextOutput() throws Exception {
        return this.stdO.readLine();
    }

    public String getNextError() throws Exception {
        return this.stdE.readLine();
    }

    public BufferedWriter getInputWriter() {
        return this.stdI;
    }
}
