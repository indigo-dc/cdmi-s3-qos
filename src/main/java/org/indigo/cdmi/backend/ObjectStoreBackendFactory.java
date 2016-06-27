package org.indigo.cdmi.backend;

import java.util.Map;

import org.indigo.cdmi.spi.StorageBackend;
import org.indigo.cdmi.spi.StorageBackendFactory;

public class ObjectStoreBackendFactory implements StorageBackendFactory {

	private final String type = "radosgw";
	private final String description = "Radosgw and S3 based back-end implementation";
	
	@Override
	public StorageBackend createStorageBackend(Map<String, String> properties) throws IllegalArgumentException {
		return new ObjectStoreBackend();
	}

	
	@Override
	public String getDescription() {
		return description;
	}

	
	@Override
	public String getType() {
		return type;
	}

}
