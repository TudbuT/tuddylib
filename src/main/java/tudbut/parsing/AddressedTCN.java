package tudbut.parsing;

import tudbut.obj.TLMap;

import java.util.ArrayList;

public class AddressedTCN {
    
    public static TCN addressedToNormal(TCN db) {
        TCN tcn = new TCN();
        ArrayList<String> keys = new ArrayList<>(db.map.keys());
        TLMap<String, Object> objects = new TLMap<>();
        for (int i = 0 ; i < keys.size() ; i++) {
            if(keys.get(i).startsWith("_")) {
                Object o = db.get(keys.get(i));
                objects.set(keys.get(i).substring(1), o);
            }
        }
        reverse(objects);
        tcn.map = db.getSub("_" + db.getInteger("main")).map;
        return tcn;
    }
    
    private static void reverse(TLMap<String, Object> objects) {
        for (Object v : objects.values()) {
            if(v instanceof TCN) {
                TCN tcn = (TCN) v;
                for (String key : tcn.map.keys()) {
                    int i = tcn.getInteger(key);
                    tcn.set(key, objects.get(String.valueOf(i)));
                }
            }
            if(v instanceof TCNArray) {
                TCNArray tcn = (TCNArray) v;
                for (int l = 0 ; l < tcn.size() ; l++) {
                    int i = tcn.getInteger(l);
                    tcn.set(l, objects.get(String.valueOf(i)));
                }
            }
        }
    }
    
    public static TCN normalToAddressed(TCN tcn) {
        TCN db = new TCN();
        ArrayList<Object> allObjects = new ArrayList<>();
        TCN theTCN = new TCN();
        theTCN.set("main", tcn);
        recursiveScan(allObjects, db, theTCN);
        db.set("main", 0);
        for (String key : db.map.keys()) {
            TCN.deepConvert(key, db.get(key), db);
        }
        return db;
    }
    
    private static void recursiveScan(ArrayList<Object> objects, TCN main, TCN tcn) {
        for (String key : tcn.map.keys()) {
            if(tcn.get(key) instanceof TCNArray)
                add(objects, main, key, ((TCNArray) tcn.get(key)).toTCN());
            else
                add(objects, main, key, tcn.get(key));
        }
    }
    
    private static Integer add(ArrayList<Object> objects, TCN main, String key, Object o) {
        if(o instanceof TCN) {
            if(!objects.contains(o)) {
                int i = objects.size();
                objects.add(o);
                TCN tcn = ((TCN) o).isArray ? new TCNArray().toTCN() : new TCN();
                recursiveScan(objects, main, (TCN) o);
                for (String theKey : ((TCN) o).map.keys()) {
                    tcn.set(theKey, add(objects, main, theKey, ((TCN) o).get(theKey)));
                }
                main.set("_" + i, tcn);
                return i;
            }
            return objects.indexOf(o);
        }
        if(!objects.contains(o)) {
            int i = objects.size();
            objects.add(o);
            main.set("_" + i, o);
            return i;
        }
        return objects.indexOf(o);
    }
}
