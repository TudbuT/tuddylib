package tudbut.tools;

import tudbut.parsing.TudSort;

import java.util.*;

/**
 * Cache (Map with passively expiring values)
 * @param <K>
 * @param <V>
 */
public class Cache<K, V> {
    protected final ArrayList<Entry<K, V>>[] entries = (ArrayList<Entry<K,V>>[]) new ArrayList[50];
    {
        for (int i = 0 ; i < entries.length ; i++) {
            entries[i] = new ArrayList<>();
        }
    }
    
    /**
     * Add a key
     * @param key Key
     * @param ttl Time to live
     * @param retriever Retriever
     */
    public void add(K key, long ttl, CacheRetriever<K, V> retriever) {
        add(key, retriever.doRetrieve(null, key), ttl, retriever);
    }
    /**
     * Add/Set a value to a key
     * @param key Key
     * @param val The value to set
     * @param ttl Time to live
     * @param retriever Re-Retriever
     */
    public void add(K key, V val, long ttl, CacheRetriever<K, V> retriever) {
        boolean exists = false;
        ArrayList<Entry<K, V>> entries = this.entries[Math.abs(key.hashCode() % this.entries.length)];
        for (int i = 0; i < entries.size(); i++) {
            Entry<K, V> entry = entries.get(i);
            if (key == entry.key || entry.key.equals(key)) {
                exists = true;
                if(val == null || retriever == null) {
                    entries.remove(i);
                    return;
                }
                entry.val = val;
                entry.timer.lock((int) (entry.ttl = ttl));
            }
        }
        if(!exists) {
            entries.add(new Entry<>(key, val, ttl, retriever));
        }
    }
    
    /**
     * Get the value associated to a key
     * @param key Key
     * @return The value
     */
    public V get(K key) {
        ArrayList<Entry<K, V>> entries = this.entries[Math.abs(key.hashCode() % this.entries.length)];
        V v = null;
        for (int i = 0 ; i < entries.size() ; i++) {
            Entry<K, V> entry = entries.get(i);
            v = entry.get();
            if(v == null)
                entries.remove(entry);
            else if (key == entry.key || entry.key.equals(key))
                break;
            else
                v = null;
        }
        return v;
    }
    
    /**
     *
     * @return All keys
     */
    public Set<K> keys() {
        ArrayList<K> keys = new ArrayList<>();
        for (int i = 0 ; i < entries.length ; i++) {
            for (int j = 0 ; j < entries[i].size() ; j++) {
                keys.add(entries[i].get(j).key);
            }
        }
        return new LinkedHashSet<K>(keys);
    }
    
    /**
     *
     * @return All values
     */
    public Set<V> values() {
        LinkedHashSet<V> vals = new LinkedHashSet<>();
        for (int i = 0 ; i < entries.length ; i++) {
            for (int j = 0 ; j < entries[i].size() ; j++) {
                vals.add(entries[i].get(j).val);
            }
        }
        return vals;
    }
    
    public LinkedHashMap<K, V> linkedHashMap() {
        ArrayList<Entry<K, V>> allEntries = new ArrayList<>();
        for (int i = 0 ; i < entries.length ; i++) {
            allEntries.addAll(entries[i]);
        }
        Entry<K, V>[] entries = TudSort.sort(allEntries.toArray(new Entry[0]), entry -> entry.i);
        LinkedHashMap<K, V> map = new LinkedHashMap<>();
        for (int i = 0 ; i < entries.length ; i++) {
            map.put(entries[i].key, entries[i].val);
        }
        return map;
    }
    
    /**
     * Flip keys and values
     * @return The flipped cache
     */
    public Cache<V, K> flip() {
        Cache<V, K> cache = new Cache<>();
        ArrayList<Entry<V, K>> allEntries = new ArrayList<>();
        for (int i = 0 ; i < entries.length ; i++) {
            for (int j = 0 ; j < entries[i].size() ; j++) {
                allEntries.add(entries[i].get(j).flip());
            }
        }
        Entry<V, K>[] entries = TudSort.sort(allEntries.toArray(new Entry[0]), entry -> entry.i);
        for (int i = 0 ; i < entries.length ; i++) {
            cache.add(entries[i].key, entries[i].val, entries[i].ttl, entries[i].retriever);
        }
        return cache;
    }
    
    protected static class Entry<K, V> {
        protected K key;
        protected V val;
        protected Lock timer;
        protected long ttl;
        protected CacheRetriever<K, V> retriever;
        protected static int ni = 0;
        protected int i = ni++;
        
        protected Entry() {
        }
        
        protected V get() {
            if(!timer.isLocked()) {
                V v = retriever.doRetrieve(val, key);
                timer.lock((int) ttl);
                val = v;
            }
            return val;
        }
        
        protected Entry(K key, V val, long ttl, CacheRetriever<K, V> retriever) {
            this.key = key;
            this.val = val;
            this.timer = new Lock();
            this.ttl = ttl;
            this.timer.lock((int) ttl);
            this.retriever = retriever;
        }
        
        protected Entry<V, K> flip() {
            return new Entry<>(val, key, 0, new CacheRetriever<V, K>() { });
        }
    }
    
    public interface CacheRetriever<K, V> {
        default V retrieveFromOld(V old) {
            return old;
        }
        default V retrieveFromKey(K key) {
            return null;
        }
        default V doRetrieve(V old, K key) {
            V v;
            if((v = retrieveFromKey(key)) == null) {
                return retrieveFromOld(old);
            }
            return v;
        }
    }
}
