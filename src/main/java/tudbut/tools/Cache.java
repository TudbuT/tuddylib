package tudbut.tools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Cache (Map with passively expiring values)
 * @param <K>
 * @param <V>
 */
public class Cache<K, V> {
    protected final ArrayList<Entry<K, V>> entries = new ArrayList<>();
    
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
        if(!exists && key != null) {
            this.entries.add(new Entry<>(key, val, ttl, retriever));
        }
    }
    
    /**
     * Get the value associated to a key
     * @param key Key
     * @return The value
     */
    public V get(K key) {
        ArrayList<Entry<K, V>> entries = (ArrayList<Entry<K, V>>) this.entries.clone();
        V v = null;
        for (int i = 0 ; i < entries.size() ; i++) {
            Entry<K, V> entry = entries.get(i);
            v = entry.get();
            if(v == null)
                this.entries.remove(entry);
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
        HashSet<K> keys = new HashSet<>();
        for (int i = 0; i < entries.size(); i++) {
            keys.add(entries.get(i).key);
        }
        return keys;
    }
    
    /**
     *
     * @return All values
     */
    public Set<V> values() {
        HashSet<V> vals = new HashSet<>();
        for (int i = 0; i < entries.size(); i++) {
            vals.add(entries.get(i).get());
        }
        return vals;
    }
    
    /**
     * Flip keys and values
     * @return The flipped cache
     */
    public Cache<V, K> flip() {
        Cache<V, K> cache = new Cache<>();
        for (int i = 0 ; i < entries.size() ; i++) {
            Entry<K, V> entry = entries.get(i);
            cache.entries.add(new Entry<>(entry.val, entry.key, 0, new CacheRetriever<V, K>() { }));
        }
        return cache;
    }
    
    protected static class Entry<K, V> {
        protected K key;
        protected V val;
        protected Lock timer;
        protected long ttl;
        protected CacheRetriever<K, V> retriever;
        
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
