/*
 * Copyright 2016 Poznan Supercomputing and Networking Center (PSNC)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.indigo.cdmi.backend.radosgw;

import org.indigo.cdmi.backend.ObjectStoreBackend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackendGatewayFactory {

	private static final Logger log = LoggerFactory.getLogger(BackendGatewayFactory.class);
	
	public static BackendGateway create() {
				
		GatewayConfiguration config = GatewayConfiguration.getInstance();
		
		String gatewayClass = config.get(ObjectStoreBackend.CONF_BACKEND_GATEWAY);
		if(gatewayClass == null) {
			//to avoid logs pollution it will be logged on ObjectStoreBacked's method level (entry point to SPI) 
			//log.error("Cannot create BackendGateway. Parameter {} is not defined in configuration resources", ObjectStoreBackend.CONF_BACKEND_GATEWAY);
			throw new RuntimeException("Cannot create BackendGateway. Parameter " + ObjectStoreBackend.CONF_BACKEND_GATEWAY + " is not defined in configuration resources");
		}
		log.info("Actual BackendGateway: {}", gatewayClass);
		
		
		try {
			Class<?> clazz = Class.forName(gatewayClass);
			return (BackendGateway) clazz.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			//to avoid logs pollution it will be logged on ObjectStoreBacked's method level (entry point to SPI)
			//log.error("Failed to create instance of {} class; exception: {};", gatewayClass, e);
			throw new RuntimeException(e);
		}
	
	} // createBackendGateway()
	
} // end of BackendGatewayFactory class
