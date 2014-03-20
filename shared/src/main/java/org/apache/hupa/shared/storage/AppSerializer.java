package org.apache.hupa.shared.storage;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.requestfactory.shared.BaseProxy;
import com.google.web.bindery.requestfactory.shared.DefaultProxyStore;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

public class AppSerializer {

  @Inject RequestFactory requestFactory;
  @Inject AutoBeanFactory beanFactory;

  private final RegExp proxyIdRegexp = RegExp.compile("^.*?\"([^\"]+@\\d+@[^\"]+)\":\\{\"[POSTY]\":(.+)$");
  private final RegExp proxyMultIdRegexp = RegExp.compile("^.*?([^#]+@\\d+@[^#]+)#~#(.+)$");

  private boolean containsMultipleProxies(String data) {
    MatchResult r = proxyIdRegexp.exec(data);
    return r != null && proxyIdRegexp.exec(r.getGroup(2)) != null;
  }

  public <T extends BaseProxy> T deserialize(Class<T> clz, String payload) {
    String keyData[] = splitKeyData(payload);
    DefaultProxyStore store = new DefaultProxyStore(keyData[1]);
    return requestFactory.getSerializer(store).deserialize(clz, keyData[0]);
  }

  private String[] splitKeyData(String payload) {
    MatchResult r;
    r = proxyMultIdRegexp.exec(payload);
    if (r != null) {
      payload = r.getGroup(2);
    } else {
      r = proxyIdRegexp.exec(payload);
    }
    return new String[]{r.getGroup(1), payload};
  }

  public <T extends BaseProxy> String serialize(T proxy) {
    DefaultProxyStore store = new DefaultProxyStore();
    String key = requestFactory.getSerializer(store).serialize(proxy);
    String data = store.encode();
    if (containsMultipleProxies(data)) {
      data = key + "#~#" + data;
    }
    return data;
  }

  public <T> T deserializeBean(Class<T> clz, String payload) {
    AutoBean<T> a = AutoBeanCodex.decode(beanFactory, clz, payload);
    assert a != null : "There is no info about AutoBean: " + clz + ", maybe you forgot to include it in the App AutoBeanFactory";
    return a.as();
  }

  public <T> String serializeBean(T proxy) {
    AutoBean<T> bean = AutoBeanUtils.getAutoBean(proxy);
    String json = AutoBeanCodex.encode(bean).getPayload();
    return json;
  }


}
