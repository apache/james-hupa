package org.apache.hupa.shared.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;

public interface AbstractEntityProxy extends EntityProxy{
	Long getId();
	Long getVersion();
}
