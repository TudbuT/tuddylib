package tudbut.tools;

import java.util.HashMap;
import java.util.Map;

public class MapTools {
    public static <K, V> Map<V, K> invert(Map<K, V> map) {
        Map<V, K> inverted = new HashMap<>();
        for (K key : map.keySet()) {
            inverted.put(map.get(key), key);
        }
        return inverted;
    }
}
