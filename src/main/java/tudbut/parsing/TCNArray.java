package tudbut.parsing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * TCN-Compatible Arrays
 */
public class TCNArray extends ArrayList<Object> {
    
    /**
     * Creates a new, empty TCNArray
     */
    public TCNArray() { }
    /**
     * Creates a new TCNArray from a collection
     */
    public TCNArray(Collection<?> collection) {
        addAll(collection);
    }
    
    public String getString(int key) {
        Object o = get(key);
        if(o != null)
            return o.toString();
        else
            return null;
    }
    
    public Short getShort(int key) {
        Object o = get(key);
        if(o != null)
            return Short.valueOf(String.valueOf(o));
        else
            return null;
    }
    
    public Integer getInteger(int key) {
        Object o = get(key);
        if(o != null)
            return Integer.valueOf(String.valueOf(o));
        else
            return null;
    }
    
    public Boolean getBoolean(int key) {
        Object o = get(key);
        if(o != null)
            return Boolean.valueOf(String.valueOf(o));
        else
            return null;
    }
    
    public Float getFloat(int key) {
        Object o = get(key);
        if(o != null)
            return Float.valueOf(String.valueOf(o));
        else
            return null;
    }
    
    public Long getLong(int key) {
        Object o = get(key);
        if(o != null)
            return Long.valueOf(String.valueOf(o));
        else
            return null;
    }
    
    public Double getDouble(int key) {
        Object o = get(key);
        if(o != null)
            return Double.valueOf(String.valueOf(o));
        else
            return null;
    }
    
    public TCN getSub(int key) {
        Object o = get(key);
        if(o != null && o.getClass() == TCN.class)
            return (TCN) o;
        else
            return null;
    }
    
    public TCNArray getArray(int key) {
        Object o = get(key);
        if(o != null && o.getClass() == TCNArray.class)
            return (TCNArray) o;
        else
            return null;
    }
    
    /**
     *
     * @return a TCN object from this TCNArray, this is the only way to get a TCN with {@link TCN#isArray} true
     */
    public TCN toTCN() {
        TCN tcn = new TCN(true);
        for (int i = 0 ; i < this.size() ; i++) {
            tcn.set(String.valueOf(i), get(i));
        }
        return tcn;
    }
    
    /**
     * Converts a TCN to a TCNArray
     * @param tcn The TCN to convert from
     * @return The created TCNArray
     */
    public static TCNArray fromTCN(TCN tcn) {
        TCNArray array = new TCNArray();
        for (String key : tcn.map.keys()) {
            array.add(tcn.get(key));
        }
        return array;
    }
    
    /**
     * Converts the TCNArray to a TCN, then maps it
     * @return the created map
     */
    public Map<String, String> toMap() {
        return toTCN().toMap();
    }
}
