package org.apache.hupa.shared.rf;

import java.io.Serializable;

public class EntityBase implements Serializable {
	private static final long serialVersionUID = 1L;

	protected Long id;

	private Long version;

	public Long getId() {
		return id;
	}

	public Long getVersion() {
		return version;
	}
}
