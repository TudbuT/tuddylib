package tudbut.obj;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class InstanceBoundMap<K, V> {
    private final HashMap<Object, Binding> bindings = new HashMap<>();

    public void createBindingIfNonExistent(Object bindingObject) {
        if(!bindings.containsKey(bindingObject))
            bindings.put(bindingObject, new Binding());
    }

    public void set(Object bindingObject, K key, V value) {
        createBindingIfNonExistent(bindingObject);
        bindings.get(bindingObject).set(key, value);
    }

    public void setIfNull(Object bindingObject, K key, V value) {
        createBindingIfNonExistent(bindingObject);
        if(bindings.get(bindingObject).get(key) == null) {
            bindings.get(bindingObject).set(key, value);
        }
    }

    public V get(Object bindingObject, K key) {
        return bindings.get(bindingObject).get(key);
    }

    public K[] keys(Object bindingObject) {
        return bindings.get(bindingObject).keys();
    }

    public V[] values(Object bindingObject) {
        return bindings.get(bindingObject).values();
    }


    class Binding {
        private ArrayList<Entry> entries = new ArrayList<>();

        private void set(K key, V value) {
            boolean exists = false;
            for (int i = 0; i < entries.size(); i++) {
                Entry entry = entries.get(i);
                if (key == entry.key || entry.key.equals(key)) {
                    entry.val = value;
                    exists = true;
                }
            }
            if(!exists) {
                this.entries.add(new Entry(key, value));
            }
        }

        private V get(K key) {
            ArrayList<Entry> entries = (ArrayList<Entry>) this.entries.clone();
            for (Entry entry : entries) {
                if (key == entry.key || entry.key.equals(key))
                    return entry.val;
            }
            return null;
        }

        private K[] keys() {
            HashSet<K> keys = new HashSet<>();
            for (int i = 0; i < entries.size(); i++) {
                keys.add(entries.get(i).key);
            }
            if(keys.size() == 0) {
                return (K[]) new Object[0];
            }
            return keys.toArray((K[]) Array.newInstance(entries.get(0).key.getClass(), keys.size()));
        }

        private V[] values() {
            HashSet<V> vals = new HashSet<>();
            for (int i = 0; i < entries.size(); i++) {
                vals.add(entries.get(i).val);
            }
            if(vals.size() == 0) {
                return (V[]) new Object[0];
            }
            return vals.toArray((V[]) Array.newInstance(entries.get(0).val.getClass(), vals.size()));
        }

        private class Entry {
            private final K key;
            private V val;

            private Entry(K key, V val) {
                this.key = key;
                this.val = val;
            }
        }
    }
}
