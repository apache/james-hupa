package org.apache.hupa.client.storage;

import org.apache.hupa.shared.storage.AppCacheMemory;
import org.apache.hupa.shared.storage.AppSerializer;

import com.google.gwt.storage.client.Storage;
import com.google.gwt.storage.client.StorageMap;
import com.google.inject.Inject;

/**
 * AppCache implementation in Browser localStorage.
 *
 * @author manolo
 */

public class AppCacheHTML5 extends AppCacheMemory {

    Storage localStorage;
    StorageMap storageMap;

    @Inject
    public AppCacheHTML5(AppSerializer s) {
        super(s);
    }

    @Override
    public void createStorageImplementationSync() {

        localStorage = Storage.getLocalStorageIfSupported();
        if (localStorage == null) {
            super.createStorageImplementationSync();
            return;
        }

        try {
            storageMap = new StorageMap(localStorage);
            localStorage.setItem(".", ".");
            localStorage.getItem(".");
            storageMap.containsKey(".");
            localStorage.removeItem(".");
        } catch (Exception e) {
            super.createStorageImplementationSync();
            return;
        }

        syncStorage = new SyncStorage() {
            @Override
            public void setItem(String key, Object data) {
                localStorage.setItem(key, "" + data);
            }

            @Override
            public void removeItem(String key) {
                if (storageMap.containsKey(key)) {
                    localStorage.removeItem(key);
                }
            }

            @Override
            public int getLength() {
                return localStorage.getLength();
            }

            @Override
            public String getItem(String key) {
                String ret =  localStorage.getItem(key);
                return ret;
            }

            @Override
            public boolean containsKey(String key) {
                return storageMap.containsKey(key);
            }

            @Override
            public void clear() {
                localStorage.clear();
            }

            @Override
            public String key(int i) {
                return localStorage.key(i);
            }

            @Override
            public native void log(Object o) /*-{
                if ($wnd.console && typeof $wnd.console.log == 'function')
                  $wnd.console.log(o);
            }-*/;

            @Override
            public native int getSize() /*-{
                try {
                  return JSON.stringify(localStorage).length;
                } catch (e) {
                  return 0;
                }
            }-*/;
        };
    }
}
