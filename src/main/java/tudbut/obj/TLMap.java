package tudbut.obj;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
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
    
    public V get(K key, V def) {
        V v = binding.get(key);
        return v == null ? def : v;
    }

    public Set<K> keys() {
        return binding.keys();
    }
    
    public int size() {
        return binding.size();
    }

    public Set<V> values() {
        return binding.values();
    }
    
    public TLMap<V, K> flip() {
        TLMap<V, K> map = new TLMap<>();
        map.binding = binding.flip();
        return map;
    }


    protected static class Binding<K, V> {
        protected ArrayList<Entry<K, V>> entries = new ArrayList<>();

        protected void set(K key, V value) {
            boolean exists = false;
            for (int i = 0; i < entries.size(); i++) {
                Entry<K, V> entry = entries.get(i);
                if (key == entry.key || entry.key.equals(key)) {
                    exists = true;
                    if(value == null) {
                        entries.remove(i);
                        break;
                    }
                    entry.val = value;
                }
            }
            if(!exists && value != null && key != null) {
                this.entries.add(new Entry<>(key, value));
            }
        }

        protected V get(K key) {
            ArrayList<Entry<K, V>> entries = (ArrayList<Entry<K, V>>) this.entries.clone();
            for (Entry<K, V> entry : entries) {
                if (key == entry.key || entry.key.equals(key))
                    return entry.val;
            }
            return null;
        }

        protected Set<K> keys() {
            Set<K> keys = new LinkedHashSet<>();
            for (int i = 0; i < entries.size(); i++) {
                keys.add(entries.get(i).key);
            }
            return keys;
        }

        protected Set<V> values() {
            Set<V> vals = new LinkedHashSet<>();
            for (int i = 0; i < entries.size(); i++) {
                vals.add(entries.get(i).val);
            }
            return vals;
        }
    
        protected Binding<V, K> flip() {
            Binding<V, K> binding = new Binding<>();
            for (int i = 0 ; i < entries.size() ; i++) {
                Entry<K, V> entry = entries.get(i);
                binding.entries.add(new Entry<>(entry.val, entry.key));
            }
            return binding;
        }
    
        public int size() {
            return entries.size();
        }
    
        protected static class Entry<K, V> {
            protected K key;
            protected V val;
    
            protected Entry() {
            }
            
            protected Entry(K key, V val) {
                this.key = key;
                this.val = val;
            }
        }
    }
}
