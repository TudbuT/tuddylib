package tudbut.obj;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TLMap<K, V> {
    protected Binding<K, V> binding = new Binding<>();

    public void set(K key, V value) {
        binding.set(key, value);
    }

    public void setIfNull(K key, V value) {
        if(binding.get(key) == null) {
            binding.set(key, value);
        }
    }

    public V get(K key) {
        return binding.get(key);
    }

    public final Set<K> keys() {
        return binding.keys();
    }

    public final Set<V> values() {
        return binding.values();
    }


    protected static class Binding<K, V> {
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

        private Set<K> keys() {
            HashSet<K> keys = new HashSet<>();
            for (int i = 0; i < entries.size(); i++) {
                keys.add(entries.get(i).key);
            }
            return keys;
        }

        private Set<V> values() {
            HashSet<V> vals = new HashSet<>();
            for (int i = 0; i < entries.size(); i++) {
                vals.add(entries.get(i).val);
            }
            return vals;
        }

        private class Entry {
            private K key;
            private V val;
    
            private Entry() {
            }
            
            private Entry(K key, V val) {
                this.key = key;
                this.val = val;
            }
        }
    }
}
