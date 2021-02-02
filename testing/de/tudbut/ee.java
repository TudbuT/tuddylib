package de.tudbut;

import de.tudbut.tools.FileRW;
import de.tudbut.tools.Tools;
import tudbut.logger.DetailedLogger;
import tudbut.logger.Logger;
import tudbut.obj.Save;
import tudbut.tools.ConfigSaver;
import tudbut.tools.ObjectSerializer;

import java.io.IOException;
import java.util.Locale;

public class ee {
    @Save()
    public int i = (int) (Math.random() * 50);
    
    public static void main(String[] args) throws IllegalAccessException, ClassNotFoundException, NoSuchFieldException, InstantiationException, IOException {
        String s;
        System.out.println(s = Tools.mapToString(ConfigSaver.saveConfig(new ee())));
        ee ee = new ee();
        ConfigSaver.loadConfig(ee, Tools.stringToMap(s));
        System.out.println(ee.i);
    }
}
