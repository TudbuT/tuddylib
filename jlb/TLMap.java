package tudbut.obj;

import de.tudbut.tools.Tools;
import tudbut.tools.ClassUtils;


public class TLMap<K, V> {
    private TLMapEntry<K, V>[] entries = new TLMapEntry[0];
    private PAtomic<K>[] keys = new PAtomic[0];

    public void set(K key, V val) {
        for (int i = 0; i < entries.length; i++) {
            if (entries[i].key.equals(key)) {
                entries[i].val = val;
                return;
            }
        }
        int oldEntries = entries.length;
        Tools.copyArray(entries, entries = new TLMapEntry[entries.length + 1], oldEntries);
        entries[entries.length - 1] = new TLMapEntry<>(key, val);
    }

    public V get(K key) {
        for (int i = 0; i < entries.length; i++) {
            if (entries[i].key.equals(key))
                return entries[i].val;
        }
        return null;
    }

    public K[] keys() {
        updateKeys();
        return PAtomic.toArray(keys);
    }

    public PAtomic<K>[] rawKeys() {
        updateKeys();
        return keys;
    }

    void updateKeys() {
        keys = new PAtomic[entries.length];
        for (int i = 0; i < entries.length; i++) {
            keys[i] = new PAtomic<>(entries[i].key);
        }
    }

    private static class TLMapEntry<K, V> {
        private TLMapEntry(K key, V val) {
            this.key = key;
            this.val = val;
        }

        private final K key;
        private V val;
    }
}
