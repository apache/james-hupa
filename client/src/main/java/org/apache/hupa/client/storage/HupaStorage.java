package org.apache.hupa.client.storage;

import java.util.List;

import org.apache.hupa.client.rf.FetchMessagesRequest;
import org.apache.hupa.client.rf.HupaRequestFactory;
import org.apache.hupa.shared.algorithms.Md5;
import org.apache.hupa.shared.domain.FetchMessagesAction;
import org.apache.hupa.shared.domain.FetchMessagesResult;
import org.apache.hupa.shared.domain.ImapFolder;
import org.apache.hupa.shared.domain.Settings;
import org.apache.hupa.shared.domain.User;
import org.apache.hupa.shared.events.LoginEvent;
import org.apache.hupa.shared.events.LoginEventHandler;
import org.apache.hupa.shared.storage.AppCache;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Promise;
import com.google.gwt.query.client.plugins.deferred.PromiseFunction;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.gquery.PromiseRF;

/**
 * Methods to cache/store Hupa stuff.
 * @author manolo
 */
@Singleton
public class HupaStorage {

    @Inject protected AppCache cache;
    @Inject protected HupaRequestFactory rf;

    protected static String KEY_CACHE_FOLDERS = "fld";
    protected static String KEY_CACHE_SERVER = "srv";
    protected static String KEY_CACHE_MSGS = "msg";

    protected User user;

    private Promise loginPromise;
    private HandlerRegistration onLogin;

    public static class FunctionPromise extends Function{
        private Promise p;
        public FunctionPromise(Promise promise) {
            p = promise;
        }
        public Object f(Object... args) {
            return p;
        }
    }

    @Inject
    public HupaStorage(EventBus eventBus) {
        onLogin = eventBus.addHandler(LoginEvent.TYPE, new LoginEventHandler() {
            public void onLogin(LoginEvent event) {
                user = event.getUser();
                onLogin.removeHandler();
            }
        });
    }

    public Function waitingForLogin() {
        return new Function() {
            public Object f(Object... data) {
                if (loginPromise == null) {
                    loginPromise = new PromiseFunction() {
                        public void f(final Deferred dfd) {
                            Scheduler.get().scheduleIncremental(new RepeatingCommand() {
                                public boolean execute() {
                                    if (user != null) {
                                        String uk = Md5.md5Hex(user.getName()).substring(0, 8) + "_";
                                        cache.setPrefix(uk);
                                        dfd.resolve(user);
                                        return false;
                                    }
                                    return true;
                                }
                            });
                        }
                    };
                }
                return loginPromise;
            }
        };
    }

    public Promise gettingFolders() {
        return gettingFolders(false);
    }

    public Promise gettingFolders(final boolean skipCache) {
        return GQuery
        .when(waitingForLogin())
        .then(new Function() {
            public Object f(Object... args) {
                List<ImapFolder> t = skipCache ? null: cache.restoreProxies(ImapFolder.class, KEY_CACHE_FOLDERS);
                if (t != null && !t.isEmpty()) {
                    return GQuery.Deferred().resolve(t).promise();
                } else {
                    return new PromiseRF(rf.fetchFoldersRequest().fetch(null, Boolean.TRUE))
                    .done(new Function(){public void f() {
                        List<ImapFolder> folderNodes = arguments(0);
                        cache.storeProxies(KEY_CACHE_FOLDERS, folderNodes);
                    }});
                }
            }
        });
    }

    public Promise gettingMessages(final boolean skipCache, final String folderFullName, final int start, final int offset, final String search) {
        return GQuery
        .when(waitingForLogin())
        .then(new Function() {
            public Object f(Object... args) {
                List<ImapFolder> t = skipCache ? null: cache.restoreProxies(ImapFolder.class, KEY_CACHE_MSGS);
                if (t != null && !t.isEmpty()) {
                    return GQuery.Deferred().resolve(t).promise();
                } else {
                    FetchMessagesRequest req = rf.messagesRequest();
                    FetchMessagesAction action = req.create(FetchMessagesAction.class);
                    final ImapFolder f = req.create(ImapFolder.class);
                    f.setFullName(folderFullName);
                    action.setFolder(f);
                    action.setOffset(offset);
                    action.setSearchString(search);
                    action.setStart(start);
                    return new PromiseRF(req.fetch(action))
                    .done(new Function(){public void f() {
                        FetchMessagesResult result = arguments(0);
                        // cache.storeProxies(KEY_CACHE_FOLDERS, folderNodes);
                    }});
                }
            }
        });
    }

    public Promise gettingSettings(final boolean skipCache) {
        return GQuery
        .when(waitingForLogin())
        .then(new Function() {
            public Object f(Object... args) {
                List<ImapFolder> t = skipCache ? null: cache.restoreProxies(ImapFolder.class, KEY_CACHE_FOLDERS);
                if (t != null && !t.isEmpty()) {
                    return GQuery.Deferred().resolve(t).promise();
                } else {
                    return new PromiseRF(rf.fetchFoldersRequest().fetch(null, Boolean.TRUE))
                    .done(new Function(){public void f() {
                        List<ImapFolder> folderNodes = arguments(0);
                        cache.storeProxies(KEY_CACHE_FOLDERS, folderNodes);
                    }});
                }
            }
        });
    }

    public void expireFolders() {
        cache.setExpires(KEY_CACHE_FOLDERS, 0);
    }

    public Settings getSettingsByEmail(String email) {
        cache.setPrefix(Md5.md5Hex(email).substring(0, 8) + "_");
        return cache.restoreProxy(Settings.class, KEY_CACHE_SERVER, false);
    }

    public void saveSettings(String email, Settings settings) {
        cache.setPrefix(Md5.md5Hex(email).substring(0, 8) + "_");
        cache.storeProxy(settings, KEY_CACHE_SERVER);
    }
}
