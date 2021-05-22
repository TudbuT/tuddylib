package tudbut.parsing;

import de.tudbut.tools.Tools;
import tudbut.obj.InstanceBoundMap;

import java.util.ArrayList;
import java.util.Map;

public class Lang extends InstanceBoundMap<String, String> {

    private String language = "default";
    private static final ArrayList<Factory> factories = new ArrayList<>();

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String languageIn) {
        language = languageIn;
    }

    public String get(String key) {
        return get(language, key);
    }

    private Lang() {
    }

    public static Factory factory() {
        for (int i = 0; i < factories.size(); i++) {
            if(!factories.get(i).inUse) {
                for (int j = i+1; j < factories.size(); j++) {
                    factories.remove(i);
                    i--;
                }
                factories.get(i).theLang = new Lang();
                return factories.get(i);
            }
        }
        Factory f = new Factory();
        factories.add(f);
        return f;
    }

    public static class Factory {
        private boolean inUse = true;
        private Lang theLang = new Lang();
        
        private Factory() {}

        public Factory addLanguage(String name, Map<String, String> map) {
            for (String key : map.keySet()) {
                theLang.set(name, key, map.get(key));
            }
            return this;
        }

        public Factory addLanguage(String name, String langFile) {
            return addLanguage(name, Tools.stringToMap(langFile.replaceAll("\n", ";")));
        }

        public Lang build(String defaultLanguage) {
            theLang.setLanguage(defaultLanguage);
            inUse = false;
            return theLang;
        }
    }
}
