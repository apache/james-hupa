package org.apache.hupa.shared.storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.hupa.shared.algorithms.B64;
import org.apache.hupa.shared.algorithms.RC4;
import org.apache.hupa.shared.data.HasFullName;
import org.apache.hupa.shared.data.HasId;

import com.google.gwt.user.client.ui.HasName;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.requestfactory.shared.BaseProxy;

/**
 * @author manolo
 */
public abstract class AppCache {

    private static final String TIMESTAMP = "_TS_";

    public static String RC4_SESS_KEY = "rOQcK1D7M1leWtu2ywzU8YAVg/KKOXAoN";

    private String pfx = "";

    AppSerializer serializer;

    @Inject
    AutoBeanFactory beanFactory;

    protected interface SyncStorage {
        void clear();

        boolean containsKey(String id);

        Object getItem(String id);

        int getLength();

        int getSize();

        void removeItem(String id);

        void setItem(String id, Object value);

        String key(int i);

        void log(Object o);
    }

    class AsyncObj {
        String id;
        int sort;
        Object val;

        public AsyncObj(int a, String b, Object c) {
            sort = a;
            id = b;
            val = c;
        }
    }

    public void setPrefix(String prefix) {
        pfx = prefix;
    }

    public abstract void createStorageImplementationSync();

    protected SyncStorage syncStorage;

    public AppCache() {
        createStorageImplementationSync();
    }

    public void clear() {
        syncStorage.clear();
    }

    boolean containsKey(String id) {
        return syncStorage.containsKey(pfx + id);
    }

    void setItem(String id, Object value, int expires) {
        setItem(id, value);
        setExpires(id, expires);
    }

    public void setItem(String id, Object value) {
        syncStorage.setItem(pfx + id, value);
    }

    public void removeItem(String id) {
        syncStorage.removeItem(pfx + TIMESTAMP + id);
        syncStorage.removeItem(pfx + id);
    }

    public int getSize() {
        return syncStorage.getSize();
    }

    public int getLength() {
        return syncStorage.getLength();
    }

    public String getItem(String id) {
        return (String) syncStorage.getItem(pfx + id);
    }

    public void log(Object o) {
        syncStorage.log(o);
    }

    public void dump() {
        String t = "Dump --- \n";
        for (int i = 0, l = getLength(); i < l; i++) {
            String key = syncStorage.key(i);
            Object val = syncStorage.getItem(key);
            t += key + " -> " + val + "\n";
        }
        System.out.println(t);
    }

    private String getKey(BaseProxy p) {
        return (p instanceof HasId ? ((HasId)p).getId() :
                p instanceof HasFullName ? ((HasFullName)p).getFullName() :
                p instanceof HasName ? ((HasName)p).getName() :
                String.valueOf(p)).replaceAll("[\\,\\s]+", "");
    }

    public <T extends BaseProxy> void storeProxies(String key, List<T> proxies) {
        storeProxies(key, proxies, true, false);
    }

    public <T extends BaseProxy> void storeProxiesCrypt(String key, List<T> proxies) {
        storeProxies(key, proxies, true, true);
    }

    public <T extends BaseProxy> void storeProxies(String key, List<T> proxies, boolean letRemoveProxy, boolean crypt) {
        List<String> ids = getIds(proxies);
        List<String> current = getStoredIdsByIdx(key);
        if (letRemoveProxy) {
            for (String i : current) {
                removeProxy(i);
            }
            current = ids;
        }

        if (proxies != null) for (T i : proxies) {
            String id = getKey(i);
            storeProxy(i, id, crypt);
            if (!current.contains(id)) {
                current.add(id);
            }
        }

        if (key != null) {
            if (current.isEmpty()) {
                removeItem(key);
                removeItem(TIMESTAMP + key);
            } else {
                setItem(key, getIdsAsString(current));
            }
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void removeProxies(String key) {
        storeProxies(key, new ArrayList(), true, false);
    }

    private <T extends BaseProxy> ArrayList<String> getIds(Collection<T> proxies) {
        ArrayList<String> ids = new ArrayList<String>();
        for (T i : proxies) {
            String id = getKey(i);
            if (id != null) {
                ids.add(id);
            }
        }
        return ids;
    }

    protected List<String> getStoredIdsByIdx(String idx) {
        String ids = getItem(idx);
        String tmp[] = ids == null ? new String[0] : ids.split(",");
        return new ArrayList<String>(Arrays.asList(tmp));
    }

    protected String getIdsAsString(Collection<String> ids) {
        String ret = "";
        for (String id : ids) {
            ret += (ret.isEmpty() ? "" : ",") + id.replaceAll("[\\,\\s]+", "_");
        }
        return ret;
    }

    protected <T extends BaseProxy> String getProxyIdsAsString(
            Collection<T> proxies) {
        return getIdsAsString(getIds(proxies));
    }

    public <T extends BaseProxy> List<T> restoreProxies(Class<T> clz, String key) {
        return restoreProxies(clz, key, false);
    }

    public <T extends BaseProxy> List<T> restoreProxiesCrypt(Class<T> clz, String key) {
        return restoreProxies(clz, key, true);
    }

    public <T extends BaseProxy> List<T> restoreProxies(Class<T> clz, String key, boolean crypt) {
        ArrayList<T> ret = new ArrayList<T>();
        for (String i : getStoredIdsByIdx(key)) {
            T proxy = restoreProxy(clz, i, crypt);
            if (proxy != null) {
                ret.add(proxy);
            }
        }
        return ret;
    }

    protected void removeProxy(String id) {
        removeItem(id);
    }

    public <J> J restoreJs(String id, Class<J> clazz, boolean create) {
        if (containsKey(id)) {
            try {
                return deSerializeBean(clazz, getItem(id));
            } catch (Exception e) {
                log("Error deserializing object with id: " + id + " " + e.getMessage());
                removeItem(id);
            }
        }
        return create ? deSerializeBean(clazz, null) : null;
    }

    public <T extends BaseProxy> T restoreProxy(Class<T> clz, String id, boolean crypt) {
        try {
            Object payload = getItem(id);
            if (crypt && payload instanceof String) {
                payload = deCrypt((String)payload);
            }
            return deSerialize(clz, payload);
        } catch (Exception e) {
            log("Error deserializing object with id: " + id + " " + e.getMessage());
            removeItem(id);
            return null;
        }
    }

    public <J> void storeJs(J o, String id) {
        setItem(id, serializeBean(o));
    }

    public void storeProxy(BaseProxy proxy, String id) {
        storeProxy(proxy, id, false);
    }

    public void storeProxy(BaseProxy proxy, String id, boolean crypt) {
        if (id == null || proxy == null) {
            return;
        }
        Object data = null;
        try {
            data = serialize(proxy);
            if (crypt && data instanceof String) {
                data = enCrypt((String)data);
            }
            setItem(id, data);
        } catch (Throwable e) {
            e.printStackTrace();
            log("Error in storeProxy serializing proxy with id: " + id + " " + e.getMessage() + " " + proxy);
        }
    }

    public String deCrypt(String s) {
        return new String(new RC4(RC4_SESS_KEY).rc4(B64.fromBase64(s)));
    }

    public String enCrypt(String s) {
        return B64.toBase64(new RC4(RC4_SESS_KEY).rc4(s));
    }

    public double getExpiresTimestamp(String key) {
        String tss = getItem(TIMESTAMP + key);
        if (tss != null) {
            return Double.parseDouble(tss);
        }
        return 0;
    }

    public void removeExpiresTimestamp(String key) {
        removeItem(TIMESTAMP + key);
    }

    public void setExpires(String key, int seconds) {
        if (seconds <= 0) {
            removeProxies(key);
        } else {
            setItem(TIMESTAMP + key, System.currentTimeMillis() + (seconds * 1000));
        }
    }

    protected Object serializeBean(Object o) {
        return serializer == null ? o : serializer.serializeBean(o);
    }

    @SuppressWarnings("unchecked")
    protected <T> T deSerializeBean(Class<T> clz, Object o) {
        assert beanFactory != null;
        if (o == null)
            return beanFactory.create(clz).as();
        return serializer == null ? (T) o : serializer.deserializeBean(clz,
                o.toString());
    }

    @SuppressWarnings("unchecked")
    protected <T extends BaseProxy> T deSerialize(Class<T> clz, Object o) {
        return serializer == null ? (T) o : serializer.deserialize(clz,
                o.toString());
    }

    protected <T extends BaseProxy> Object serialize(T o) {
        return serializer == null ? o : serializer.serialize(o);
    }
}
