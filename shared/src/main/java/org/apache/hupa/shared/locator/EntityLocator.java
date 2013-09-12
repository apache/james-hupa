package org.apache.hupa.shared.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

public abstract class EntityLocator extends Locator<ValueProxy, Long> {

	@Override
	public ValueProxy create(Class<? extends ValueProxy> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public abstract ValueProxy find(Class<? extends ValueProxy> clazz, Long id);

	@Override
	public Class<ValueProxy> getDomainType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Class<Long> getIdType() {
		return Long.class;
	}

}
