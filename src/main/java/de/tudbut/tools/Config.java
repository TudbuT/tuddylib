package de.tudbut.tools;


import de.tudbut.type.StringArray;

import java.io.*;

public class Config {
    private final File configFile;

    public Config(String file) throws Exception {
        this.configFile = new File(file);
        if (!this.configFile.exists()) {
            this.configFile.createNewFile();
            new BufferedWriter(new FileWriter(this.configFile)).write(" : ;");
        }


    }


    public String get(String cfgPath) throws Exception {

        FileReader reader = new FileReader(this.configFile);
        BufferedReader breader = new BufferedReader(reader);

        String cfg = breader.readLine();

        if (cfg == null) {
            cfg = " : ;";
        }

        String[] cfgp1 = cfg.split(";");
        for (String x : cfgp1) {
            String[] cfgp2 = x.split(":");
            if (cfgp2[0].equals(cfgPath)) {
                return cfgp2[1].replaceAll("&a", ";").replaceAll("&b", ":").replaceAll("&d", "\n").replaceAll("&c", "&");
            }
        }
        return null;
    }

    public void set(String cfgPath, String value) throws Exception {
        FileReader reader = new FileReader(this.configFile);
        BufferedReader breader = new BufferedReader(reader);
        String cfg = breader.readLine();
        breader.close();
        reader.close();

        FileWriter writer = new FileWriter(this.configFile);
        BufferedWriter bwriter = new BufferedWriter(writer);

        if (cfg == null) {
            cfg = " : ;";
        }

        String[] cfgp1 = cfg.split(";");
        for (String x : cfgp1) {
            if (!x.split(":")[0].equals(cfgPath) && !x.equals(" : ")) bwriter.write(x + ";");
        }


        bwriter.write(cfgPath);
        bwriter.write(":");
        bwriter.write(value.replaceAll("&", "&c").replaceAll(":", "&b").replaceAll(";", "&a").replaceAll("\n", "&d"));
        bwriter.write(";");
        bwriter.close();
    }

    public StringArray getList(String cfgPath) throws Exception {
        StringArray r = new StringArray();
        for (int i = 0; i < Integer.parseInt(get(cfgPath + "[length]")); i++) {
            r.add(get(cfgPath + "[" + i + "]"));
        }

        return r;
    }

    public void setList(String cfgPath, String[] list) throws Exception {
        for (int i = 0; i < list.length; i++) {
            set(cfgPath + "[" + i + "]", list[i]);
        }
        set(cfgPath + "[length]", String.valueOf(list.length));
    }
}