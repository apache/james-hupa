package org.apache.hupa.shared.locator;

import org.apache.hupa.shared.rf.EntityBase;

import com.google.web.bindery.requestfactory.shared.Locator;

public abstract class EntityLocator extends Locator<EntityBase, Long> {

	@Override
	public EntityBase create(Class<? extends EntityBase> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public abstract EntityBase find(Class<? extends EntityBase> clazz, Long id);

	@Override
	public Class<EntityBase> getDomainType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Long getId(EntityBase domainObject) {
		return domainObject.getId();
	}

	@Override
	public Class<Long> getIdType() {
		return Long.class;
	}

	@Override
	public Object getVersion(EntityBase domainObject) {
		return domainObject.getVersion();
	}

}
