package de.tudbut;

import de.tudbut.tools.FileRW;
import de.tudbut.tools.Tools;
import tudbut.logger.DetailedLogger;
import tudbut.logger.Logger;
import tudbut.obj.Atomic;
import tudbut.obj.Save;
import tudbut.parsing.TCN;
import tudbut.tools.ConfigSaver;
import tudbut.tools.ConfigSaverTCN;
import tudbut.tools.ObjectSerializer;
import tudbut.tools.Value;

import java.io.IOException;
import java.util.Locale;

public class ee {
    @Save
    public int i = (int) (Math.random() * 50);
    @Save
    public Atomic<String> data = new Atomic<>("al");
    
    public static void main(String[] args) throws IllegalAccessException, ClassNotFoundException, NoSuchFieldException, InstantiationException, IOException, TCN.TCNException {
        TCN tcn = TCN.read(new FileRW("test.tcn").getContent().join("\n"));
        if(tcn.get("init") != null && tcn.getBoolean("init")) {
            ee ee = new ee();
            ConfigSaverTCN.loadConfig(ee, tcn.getSub("main"));
            System.out.println(ee.i);
            System.out.println(ee.data.get());
        }
        else {
            tcn.set("main", ConfigSaverTCN.saveConfig(new ee()));
            tcn.set("init", true);
            new FileRW("test.tcn").setContent(tcn.toString());
        }
    }
}
