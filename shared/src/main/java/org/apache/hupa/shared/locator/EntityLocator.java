package org.apache.hupa.shared.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

public abstract class EntityLocator extends Locator<ValueProxy, Long> {

	@Override
	public ValueProxy create(Class<? extends ValueProxy> clazz) {
		return null;
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
