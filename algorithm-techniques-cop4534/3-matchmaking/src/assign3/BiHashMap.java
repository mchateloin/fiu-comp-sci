package assign3;

import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author Miguel A. Chateloin <mchat003@fiu.edu>
 * @param <K>
 */
public class BiHashMap<K extends Object, V extends Object> {
    private Map<K,V> forward = new Hashtable<>();
    private Map<V,K> backward = new Hashtable<>();

    public void put(K key, V value) {
      forward.put(key, value);
      backward.put(value, key);
    }
    
    public V getForward(K key) {
      return forward.get(key);
    }

    public K getBackward(V key) {
      return backward.get(key);
    }
}
