package tudbut.parsing;

import java.util.ArrayList;

public class DatabaseTCNConverter {
    
    public static TCN dbToUsable(TCN db) {
        TCN tcn = new TCN();
        ArrayList<String> keys = new ArrayList<>(db.map.keys());
        ArrayList<Object> objects = new ArrayList<>();
        for (int i = 0 ; i < keys.size() ; i++) {
            if(!keys.get(i).startsWith("_")) {
                Object o = db.get("_" + db.getInteger(keys.get(i)));
                if(o instanceof TCN) {
                    o = recursiveReverse(db, (TCN) o, objects, db.getInteger(keys.get(i)));
                }
                tcn.set(keys.get(i), o);
            }
        }
        return tcn;
    }
    
    public static TCN recursiveReverse(TCN db, TCN theTCN, ArrayList<Object> objects, int n) {
        TCN tcn = new TCN();
        ArrayList<String> keys = new ArrayList<>(theTCN.map.keys());
        for (int i = 0 ; i < keys.size() ; i++) {
                Object o = db.get("_" + theTCN.getInteger(keys.get(i)));
                if (o instanceof TCN) {
                    if (objects.size() > n) {
                        o = objects.get(n);
                        System.out.println("RECURSION " + keys.get(i) + " " + n);
                    }
                    else {
                        objects.add(o);
                        o = recursiveReverse(db, (TCN) o, objects, theTCN.getInteger(keys.get(i)));
                    }
                    tcn.set(keys.get(i), o);
                    continue;
                }
                tcn.set(keys.get(i), o);
            
        }
        return tcn;
    }
    
    public static TCN usableToDB(TCN tcn) {
        TCN db = new TCN();
        ArrayList<Object> allObjects = new ArrayList<>();
        recursiveScan(allObjects, db, tcn.getSub("main"));
        return db;
    }
    
    private static void recursiveScan(ArrayList<Object> objects, TCN main, TCN tcn) {
        for (String key : tcn.map.keys()) {
            main.set(key, add(objects, main, key, tcn.get(key)));
        }
    }
    
    private static int add(ArrayList<Object> objects, TCN main, String key, Object o) {
        if(o instanceof TCN) {
            if(!objects.contains(o)) {
                int i = objects.size();
                objects.add(o);
                TCN tcn = new TCN();
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
