package de.tudbut.tools;

import de.tudbut.type.FileFormatException;

import java.util.HashMap;
import java.util.Map;

public class BYMLFile extends FileRW {
    protected Map<String, String> map = new HashMap<>();

    public BYMLFile(String path) throws Exception {
        super(path);
        if (!path.endsWith(".byml") && !path.endsWith(".byaml")) {
            throw new FileFormatException();
        }
        else {
            this.remap();
        }
    }

    public void remap() throws Exception {
        map = Tools.stringToMap(getContent().join(";").replaceAll(": ", ":").replaceAll("\\$\\$\\$", "\u0000").replaceAll("\"", ""));
    }

    public void mapToObj(Object object) throws IllegalAccessException {
        Tools.ObjectMapping.mapToObject(object, map);
    }

    public void mapToSObj(Class<?> clazz) throws IllegalAccessException {
        Tools.ObjectMapping.mapToStaticObject(clazz, map);
    }

    public String getValue(String key) {
        return this.map.get(key);
    }
}
