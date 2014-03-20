package org.apache.hupa.shared.storage;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * AppCache implementation in Memory and JVM
 *
 * @author manolo
 */
public class AppCacheMemory extends AppCache {

    public AppCacheMemory() {
    }

    /**
     * AppCache implementation in Memory and JVM with serialization.
     */
    public AppCacheMemory(AppSerializer s) {
        this.serializer = s;
    }

    @Override
    public void createStorageImplementationSync() {
        syncStorage = new SyncStorage() {

            HashMap<String, Object> cache = new HashMap<String, Object>();

            @Override
            public void setItem(String id, Object value) {
                cache.put(id, value);
            }

            @Override
            public void removeItem(String id) {
                cache.remove(id);
            }

            @Override
            public int getLength() {
                return cache.size();
            }

            @Override
            public Object getItem(String id) {
                return cache.get(id);
            }

            @Override
            public boolean containsKey(String id) {
                return cache.containsKey(id);
            }

            @Override
            public void clear() {
                cache.clear();
            }

            @Override
            public String key(int i) {
                int c = 0;
                for (Entry<String, ?> e : cache.entrySet()) {
                    if (i == c) {
                        return e.getKey();
                    }
                    c++;
                }
                return null;
            }

            @Override
            public void log(Object o) {
                System.out.println(o);
            }

            @Override
            public int getSize() {
                return cache.size();
            }
        };

    }
}
