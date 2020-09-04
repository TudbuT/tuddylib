package de.tudbut.tools;


import de.tudbut.logger.GlobalLogger;
import de.tudbut.logger.Logger;
import de.tudbut.type.StringArray;

import java.io.*;

public class Config {
    private File configFile = null;
    private Logger logger = null;

    public Config(String file) throws Exception {
        GlobalLogger.info("Initializing ConfigLoader '" + file + "'");
        this.configFile = new File(file);
        this.logger = new Logger(file);
        if (!this.configFile.exists()) {
            this.logger.info("Creating file...");
            this.configFile.createNewFile();
            new BufferedWriter(new FileWriter(this.configFile)).write(" : ;");
            this.logger.info("Done!");
        }


        this.logger.info("ConfigLoader '" + file + "' initialized.");
    }


    public String get(String cfgPath) throws Exception {
        this.logger.info("Getting value '" + cfgPath + "'");

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
                this.logger.info("Done!");
                return cfgp2[1].replaceAll("&a", ";").replaceAll("&b", ":").replaceAll("&d", "\n").replaceAll("&c", "&");
            }
        }
        this.logger.info("Failed to grab value");
        return null;
    }

    public void set(String cfgPath, String value) throws Exception {
        this.logger.info("Setting '" + cfgPath + "'");

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