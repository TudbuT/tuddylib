package tudbut.obj;

import tudbut.tools.Retriever;

import java.util.*;

public class TLMap<K, V> {
    protected Binding<K, V> binding = new Binding<>();
    
    public static TLMap<String, String> stringToMap(String mapStringParsable) {
        TLMap<String, String> map = new TLMap<>();
        
        String[] splitTiles = mapStringParsable.split(";");
        for (int i = 0; i < splitTiles.length; i++) {
            String tile = splitTiles[i];
            String[] splitTile = tile.split(":");
            if (tile.contains(":")) {
                if (splitTile.length == 2)
                    map.set(
                            splitTile[0].replaceAll("%I", ":").replaceAll("%B", ";").replaceAll("%P", "%"),
                            splitTile[1].equals("%N") ? null : splitTile[1].replaceAll("%I", ":").replaceAll("%B", ";").replaceAll("%P", "%")
                    );
                else
                    map.set(splitTile[0].replaceAll("%I", ":").replaceAll("%B", ";").replaceAll("%P", "%"), "");
            }
        }
        
        return map;
    }
    
    public static String mapToString(TLMap<String, String> map) {
        StringBuilder r = new StringBuilder();
        
        for (String key : map.keys().toArray(new String[0])) {
            
            r
                    .append(key.replaceAll("%", "%P").replaceAll(";", "%B").replaceAll(":", "%I"))
                    .append(":")
                    .append(map.get(key) == null ? "%N" : map.get(key).replaceAll("%", "%P").replaceAll(";", "%B").replaceAll(":", "%I"))
                    .append(";")
            ;
        }
        
        return r.toString();
    }

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
    public V get(K key, Retriever<V> def) {
        V v = binding.get(key);
        return v == null ? def.retrieve() : v;
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
    
    public ArrayList<Entry<K, V>> entries() {
        return binding.entries();
    }
    
    public TLMap<V, K> flip() {
        TLMap<V, K> map = new TLMap<>();
        map.binding = binding.flip();
        return map;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TLMap<?, ?> tlMap = (TLMap<?, ?>) o;
        return tlMap.binding.entries.equals(binding.entries);
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
    
        protected ArrayList<Entry<K, V>> entries() {
            ArrayList<Entry<K, V>> vals = new ArrayList<>();
            vals.addAll(entries);
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
    }
    
    public static class Entry<K, V> {
        public K key;
        public V val;
        
        protected Entry() {
        }
        
        protected Entry(K key, V val) {
            this.key = key;
            this.val = val;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entry<?, ?> entry = (Entry<?, ?>) o;
            if (!Objects.equals(key, entry.key)) return false;
            return Objects.equals(val, entry.val);
        }
    }
    
    @Override
    public TLMap<K, V> clone() {
        TLMap<K, V> n = new TLMap<>();
        Object[] keys = keys().toArray();
        Object[] vals = values().toArray();
        for (int i = 0, arrayLength = keys.length ; i < arrayLength ; i++) {
            Object key = keys[i];
            n.set((K)key, (V)vals[i]);
        }
        return n;
    }
}
