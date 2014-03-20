package org.apache.hupa.client;


import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.hupa.client.ioc.GinClientTestModule;
import org.apache.hupa.server.FileItemRegistry;
import org.apache.hupa.server.IMAPStoreCache;
import org.apache.hupa.server.guice.GuiceJunitRunner;
import org.apache.hupa.server.guice.GuiceJunitRunner.GuiceModules;
import org.apache.hupa.server.guice.GuiceServerTestModule;
import org.apache.hupa.server.utils.SessionUtils;
import org.apache.hupa.shared.SConsts;
import org.apache.hupa.shared.domain.User;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.junit.Before;
import org.junit.runner.RunWith;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.sun.mail.imap.IMAPStore;

/**
 * @author manolo
 */
@RunWith(GuiceJunitRunner.class)
@GuiceModules({GuiceServerTestModule.class, GinClientTestModule.class})
public abstract class HupaClientTestBase  {

  static {
    LogManager.getRootLogger().setLevel(Level.INFO);
  }

  @Inject protected Log logger;
  @Inject protected Injector injector;
  @Inject protected HttpSession httpSession;
  @Inject protected User testUser;
  @Inject protected IMAPStoreCache storeCache;
  protected IMAPStore store;
  protected FileItemRegistry registry;

  @Before
  public void setup() throws Exception {
      SessionUtils.cleanSessionAttributes(httpSession);
      store = storeCache.get(testUser);
      httpSession.setAttribute(SConsts.USER_SESS_ATTR, testUser);
      registry = SessionUtils.getSessionRegistry(logger, httpSession);
  }

}
