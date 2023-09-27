package de.tudbut.tools;

import de.tudbut.type.FileFormatException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class YMLFile extends FileRW {
    protected Map<String, String> map = new HashMap<>();

    public YMLFile(String path) throws Exception {
        super(path);
        if (!path.endsWith(".yml") && !path.endsWith(".yaml")) {
            throw new FileFormatException();
        }
        else {
            this.remap();
        }
    }

    public void remap() throws Exception {
        map = new HashMap<>();


        String[] var1 = this.getContent().asArray();

        for (String line : var1) {
            if (!line.equals("")) {
                String theLine = line.substring(line.split(": ")[0].length() + 2);
                if (theLine.startsWith("\"") && theLine.endsWith("\"")) {
                    theLine = theLine.substring(1, theLine.length() - 1);
                }

                this.map.put(line.split(": ")[0], theLine);
            }
        }

    }

    public void mapToObj(Object object) throws IllegalAccessException {
        Field[] fields = object.getClass().getFields();
        int var4 = fields.length;

        for (Field field : fields) {
            field.set(object, this.map.get(field.getName()));
        }

    }

    public String getValue(String key) {
        return this.map.get(key);
    }
}
