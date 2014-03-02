package org.apache.hupa.server.guice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Customized Junit Runner able to inject components with guice
 */
public class GuiceJunitRunner extends BlockJUnit4ClassRunner {

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  public @interface GuiceModules {
    Class<?>[] value();
  }

  private Module[] guiceModules;

  @Override
  public Object createTest() throws Exception {
    Injector injector = Guice.createInjector(guiceModules);
    return injector.getInstance(getTestClass().getJavaClass());
  }

  public GuiceJunitRunner(Class<?> clz) throws Exception {
    super(clz);

    GuiceModules ann = clz.getAnnotation(GuiceModules.class);
    if (ann == null) {
      throw new InitializationError("No @GuiceModules annotation for  '" + clz.getName() + "'");
    }

    Class<?>[] classes = ann.value();
    guiceModules = new Module[classes.length];
    for (int i = 0; i < classes.length; i++) {
      guiceModules[i] = (Module) (classes[i]).newInstance();
    }
  }
}